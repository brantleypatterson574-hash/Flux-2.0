package com.yourname.utilmod.server;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Server-side per-player NoFall configuration. */
public final class NoFallManager {
    public static final int MODE_RESET = 0;
    public static final int MODE_MINIMIZE = 1;

    public record Config(boolean enabled, int mode, float minDistance, float minimizedDistance,
                         boolean onlyWhenFallingFast, float fallSpeedThreshold,
                         boolean onlyBelowHealth, float healthThreshold) {}

    private static final Map<UUID, Config> CONFIGS = new ConcurrentHashMap<>();

    private NoFallManager() {}

    public static void set(UUID uuid, Config config) {
        if (uuid == null) return;
        if (config == null || !config.enabled()) CONFIGS.remove(uuid);
        else CONFIGS.put(uuid, config);
    }

    public static void clear(UUID uuid) {
        if (uuid != null) CONFIGS.remove(uuid);
    }

    public static void clearAll() {
        CONFIGS.clear();
    }

    /** Returns the fall distance the server should use for damage calculation. */
    public static double apply(UUID uuid, PlayerEntity player, double fallDistance) {
        if (player == null || player.getEntityWorld().isClient() || fallDistance <= 0.0) return fallDistance;
        Config config = uuid == null ? null : CONFIGS.get(uuid);
        if (config == null || !config.enabled()) return fallDistance;
        if (fallDistance < config.minDistance()) return fallDistance;

        if (config.onlyWhenFallingFast()
            && -player.getVelocity().y < config.fallSpeedThreshold()) return fallDistance;
        if (config.onlyBelowHealth()
            && player.getHealth() > config.healthThreshold()) return fallDistance;

        if (config.mode() == MODE_MINIMIZE) {
            return Math.min(fallDistance, config.minimizedDistance());
        }
        return 0.0;
    }
}
