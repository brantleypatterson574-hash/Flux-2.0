package com.yourname.utilmod.server;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Server-side per-player AntiHunger configuration. */
public final class AntiHungerManager {
    public record Config(boolean enabled, float exhaustionAmount, boolean whileSneakingOnly,
                         boolean whileMovingOnly, boolean whileFlyingOnly) {}

    private static final Map<UUID, Config> CONFIGS = new ConcurrentHashMap<>();

    private AntiHungerManager() {}

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

    /** Returns the exhaustion left after the player's configured reduction. */
    public static float apply(UUID uuid, PlayerEntity player, float amount) {
        if (player == null || player.getEntityWorld().isClient() || amount <= 0.0f) return amount;
        Config config = uuid == null ? null : CONFIGS.get(uuid);
        if (config == null || !config.enabled()) return amount;

        if (config.whileSneakingOnly() && !player.isSneaking()) return amount;
        if (config.whileFlyingOnly() && !player.getAbilities().flying) return amount;
        if (config.whileMovingOnly()
            && player.getVelocity().horizontalLength() < 0.01
            && !player.isSneaking()) return amount;

        return Math.max(0.0f, amount - config.exhaustionAmount());
    }
}
