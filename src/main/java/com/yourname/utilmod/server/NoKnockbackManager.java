package com.yourname.utilmod.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Server-side per-player knockback scales for the Velocity module. */
public final class NoKnockbackManager {
    private static final Map<UUID, double[]> SCALES = new ConcurrentHashMap<>();

    private NoKnockbackManager() {}

    public static void set(UUID uuid, double horizontal, double vertical) {
        if (uuid == null) return;
        double h = Math.max(0.0, Math.min(1.0, horizontal));
        double v = Math.max(0.0, Math.min(1.0, vertical));
        if (h >= 1.0 && v >= 1.0) {
            SCALES.remove(uuid);
        } else {
            SCALES.put(uuid, new double[] { h, v });
        }
    }

    /** Legacy helper for a full knockback cancel. */
    public static void set(UUID uuid, boolean enabled) {
        set(uuid, enabled ? 0.0 : 1.0, enabled ? 0.0 : 1.0);
    }

    public static double horizontal(UUID uuid) {
        double[] values = uuid == null ? null : SCALES.get(uuid);
        return values == null ? 1.0 : values[0];
    }

    public static double vertical(UUID uuid) {
        double[] values = uuid == null ? null : SCALES.get(uuid);
        return values == null ? 1.0 : values[1];
    }

    public static boolean isEnabled(UUID uuid) {
        return uuid != null && SCALES.containsKey(uuid);
    }

    public static void clear(UUID uuid) {
        if (uuid != null) SCALES.remove(uuid);
    }

    public static void clearAll() {
        SCALES.clear();
    }
}
