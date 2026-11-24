package com.yourname.utilmod.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds the Reach module's extra block and entity reach values.
 *
 * <p>The client stores its local values so block/entity interactions can look
 * farther. The server stores per-player values received over the custom C2S
 * payload so the attack-range check accepts the same extra reach. Both sides
 * are needed for the Reach module on a private server.</p>
 */
public final class ReachManager {
    private static final Map<UUID, double[]> PLAYER_REACH = new ConcurrentHashMap<>();
    private static volatile double localBlockReach = 0.0;
    private static volatile double localEntityReach = 0.0;
    private static volatile UUID localPlayerUuid = null;
    // Hot-path gate: reach is off by default, so mixins can skip map work.
    private static volatile boolean anyReach;

    private ReachManager() {}

    private static boolean has(double[] values) {
        return values != null && (values[0] > 0.0 || values[1] > 0.0);
    }

    private static boolean mapHasReach() {
        for (double[] values : PLAYER_REACH.values()) {
            if (has(values)) return true;
        }
        return false;
    }

    private static void refreshAny() {
        anyReach = localBlockReach > 0.0 || localEntityReach > 0.0 || mapHasReach();
    }

    /** Cheap global check before doing UUID/map work in mixins. */
    public static boolean hasAnyReach() {
        return anyReach;
    }

    public static double clamp(double value) {
        if (!Double.isFinite(value)) return 0.0;
        return Math.max(0.0, Math.min(32.0, value));
    }

    public static double getLocalBlockReach() {
        return localBlockReach;
    }

    public static double getLocalEntityReach() {
        return localEntityReach;
    }

    /** Legacy accessor used by the server anti-cheat; entity reach is the attack value. */
    public static double getLocalReach() {
        return localEntityReach;
    }

    public static void setLocalReach(double block, double entity, UUID uuid) {
        localBlockReach = clamp(block);
        localEntityReach = clamp(entity);
        if (uuid != null) localPlayerUuid = uuid;
        refreshAny();
    }

    /** Legacy overload kept for callers that only need entity reach. */
    public static void setLocalReach(double value, UUID uuid) {
        setLocalReach(0.0, value, uuid);
    }

    public static void clearLocal() {
        localBlockReach = 0.0;
        localEntityReach = 0.0;
        localPlayerUuid = null;
        refreshAny();
    }

    public static double getPlayerBlockReach(UUID uuid) {
        double[] values = uuid == null ? null : PLAYER_REACH.get(uuid);
        return has(values) ? values[0] : 0.0;
    }

    public static double getPlayerEntityReach(UUID uuid) {
        double[] values = uuid == null ? null : PLAYER_REACH.get(uuid);
        return has(values) ? values[1] : 0.0;
    }

    /** Legacy accessor used by the server anti-cheat; entity reach is the attack value. */
    public static double getPlayerReach(UUID uuid) {
        return getPlayerEntityReach(uuid);
    }

    public static void setPlayerReach(UUID uuid, double block, double entity) {
        if (uuid == null) return;
        double clampedBlock = clamp(block);
        double clampedEntity = clamp(entity);
        if (clampedBlock <= 0.0 && clampedEntity <= 0.0) {
            PLAYER_REACH.remove(uuid);
        } else {
            PLAYER_REACH.put(uuid, new double[] { clampedBlock, clampedEntity });
        }
        refreshAny();
    }

    /** Legacy overload kept for callers that only send entity reach. */
    public static void setPlayerReach(UUID uuid, double value) {
        setPlayerReach(uuid, 0.0, value);
    }

    /** Extra block reach for the attacker on this logical side. */
    public static double effectiveBlockReach(Object attacker) {
        if (!(attacker instanceof net.minecraft.entity.player.PlayerEntity player)) return 0.0;
        UUID uuid = player.getUuid();
        if (uuid != null && uuid.equals(localPlayerUuid)) return localBlockReach;
        return getPlayerBlockReach(uuid);
    }

    /** Extra entity/attack reach for the attacker on this logical side. */
    public static double effectiveReach(Object attacker) {
        if (!(attacker instanceof net.minecraft.entity.player.PlayerEntity player)) return 0.0;
        UUID uuid = player.getUuid();
        if (uuid != null && uuid.equals(localPlayerUuid)) return localEntityReach;
        return getPlayerEntityReach(uuid);
    }

    public static void clear(UUID uuid) {
        if (uuid == null) return;
        PLAYER_REACH.remove(uuid);
        if (uuid.equals(localPlayerUuid)) {
            localPlayerUuid = null;
            localBlockReach = 0.0;
            localEntityReach = 0.0;
        }
        refreshAny();
    }

    public static void clear() {
        PLAYER_REACH.clear();
        localBlockReach = 0.0;
        localEntityReach = 0.0;
        localPlayerUuid = null;
        refreshAny();
    }
}
