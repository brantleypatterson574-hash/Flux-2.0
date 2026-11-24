package com.yourname.utilmod.server;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Server-side per-player block-breaking multiplier for FastMine. */
public final class FastMineManager {
    private static final Map<UUID, Float> MULTIPLIERS = new ConcurrentHashMap<>();

    private FastMineManager() {}

    public static void set(UUID uuid, float multiplier) {
        if (uuid == null) return;
        float clamped = Float.isFinite(multiplier) ? Math.max(1.0f, Math.min(32.0f, multiplier)) : 1.0f;
        if (clamped <= 1.0f) {
            MULTIPLIERS.remove(uuid);
        } else {
            MULTIPLIERS.put(uuid, clamped);
        }
    }

    public static float multiplier(UUID uuid) {
        Float value = uuid == null ? null : MULTIPLIERS.get(uuid);
        return value == null ? 1.0f : value;
    }

    public static void clear(UUID uuid) {
        if (uuid != null) MULTIPLIERS.remove(uuid);
    }

    public static void clearAll() {
        MULTIPLIERS.clear();
    }
}
