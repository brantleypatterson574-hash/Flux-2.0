package com.yourname.utilmod.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds hitbox multipliers for the local client and for individual server players.
 *
 * <p>The multiplier stays fully customizable in the GUI. The physical/attack
 * expansion is deliberately capped: a 1000x physics box makes vanilla collision
 * and entity lookup scan enormous volumes and freezes the game. Rendering uses
 * the same bounded expansion so the drawn box matches what can actually be hit.</p>
 */
public final class HitboxManager {
    /** Largest safe expansion applied to an entity box, in blocks. */
    private static final double MAX_MARGIN = 6.0;
    /** A player is roughly 0.6 blocks wide, so half-width is about 0.3. */
    private static final double PLAYER_HALF_WIDTH = 0.3;

    private static final Map<UUID, Double> PLAYER_MULTIPLIERS = new ConcurrentHashMap<>();
    private static volatile double localMultiplier = 1.0;
    private static volatile UUID localPlayerUuid = null;
    // Hot-path gate: hitbox scaling is off by default, so mixins can skip UUID
    // and map work with one volatile read.
    private static volatile boolean anyMargin;

    private HitboxManager() {}

    private static void refreshAny() {
        anyMargin = localMultiplier > 1.0 || !PLAYER_MULTIPLIERS.isEmpty();
    }

    /** Cheap global check before doing UUID/map work in mixins. */
    public static boolean hasAnyMargin() {
        return anyMargin;
    }

    /** The multiplier currently set in the local GUI. */
    public static double getMultiplier() {
        return localMultiplier;
    }

    public static void setMultiplier(double value) {
        setMultiplier(value, null);
    }

    /** Sets the local GUI value and remembers which player it belongs to. */
    public static void setMultiplier(double value, UUID uuid) {
        if (!Double.isFinite(value)) return;
        localMultiplier = Math.max(0.1, Math.min(1000.0, value));
        if (uuid != null) localPlayerUuid = uuid;
        refreshAny();
    }

    /** Server-side value received from one player's C2S payload. */
    public static void setPlayerMultiplier(UUID uuid, double value) {
        if (uuid == null || !Double.isFinite(value)) return;
        double clamped = Math.max(0.1, Math.min(1000.0, value));
        if (clamped <= 1.0 + 1e-6) {
            PLAYER_MULTIPLIERS.remove(uuid);
        } else {
            PLAYER_MULTIPLIERS.put(uuid, clamped);
        }
        refreshAny();
    }

    public static void clear(UUID uuid) {
        if (uuid == null) return;
        PLAYER_MULTIPLIERS.remove(uuid);
        if (uuid.equals(localPlayerUuid)) localPlayerUuid = null;
        refreshAny();
    }

    public static void clearAll() {
        PLAYER_MULTIPLIERS.clear();
        localPlayerUuid = null;
        // The local multiplier has to be reset too - leaving it above 1 keeps
        // anyMargin true, so the mixins keep expanding hits after the module
        // is switched off in the menu.
        localMultiplier = 1.0;
        refreshAny();
    }

    private static double multiplierFor(UUID uuid) {
        if (uuid == null) return localMultiplier;
        if (uuid.equals(localPlayerUuid)) return localMultiplier;
        return PLAYER_MULTIPLIERS.getOrDefault(uuid, 1.0);
    }

    /** Bounded extra margin used by attacks and the visual box. */
    public static double effectiveMargin(UUID uuid) {
        return Math.min(MAX_MARGIN, PLAYER_HALF_WIDTH * Math.max(0.0, multiplierFor(uuid) - 1.0));
    }

    public static double effectiveMargin() {
        return effectiveMargin(localPlayerUuid);
    }

    /** Small extra padding so wireframes do not z-fight with the real box. */
    public static double visualMargin() {
        return effectiveMargin(localPlayerUuid);
    }
}
