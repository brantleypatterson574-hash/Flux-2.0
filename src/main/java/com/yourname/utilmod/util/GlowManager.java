package com.yourname.utilmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Tells Minecraft which entities should use its built-in model outline pass.
 *
 * The old version called {@code Entity.setGlowing(true)} and restored the flag
 * later. That mutates real entity state and can fight with the server or other
 * mods. Instead, {@link com.yourname.utilmod.mixin.EntityGlowMixin} makes
 * {@code Entity.isGlowing()} report true for our targets, and
 * {@link com.yourname.utilmod.mixin.EntityGlowColorMixin} supplies the colour.
 * Nothing is mutated, so there is no state to restore.
 */
public final class GlowManager {
    private static final Map<Entity, Integer> COLORS = new IdentityHashMap<>();
    // isGlowing()/getTeamColorValue() run for every rendered entity. A single
    // volatile read lets the mixins skip all map work when glow is unused.
    private static volatile boolean active;

    private GlowManager() {}

    public static boolean isActive() {
        return active;
    }

    /** Replaces the whole glow set. Call once per client tick from the owning module. */
    public static void apply(Map<Entity, Integer> desired) {
        MinecraftClient mc = MinecraftClient.getInstance();
        // Reuse the live map instead of allocating a full copy every client tick.
        COLORS.clear();
        if (mc.world == null) {
            active = false;
            return;
        }
        for (Map.Entry<Entity, Integer> entry : desired.entrySet()) {
            Entity entity = entry.getKey();
            if (entity.isRemoved() || entity.getEntityWorld() != mc.world) continue;
            COLORS.put(entity, entry.getValue() & 0xFFFFFF);
        }
        active = !COLORS.isEmpty();
    }

    public static void clear() {
        if (!active) return;
        COLORS.clear();
        active = false;
    }

    /** True when this entity should be included in the outline pass. */
    public static boolean shouldGlow(Entity entity) {
        return COLORS.containsKey(entity);
    }

    /** Colour used by the outline pass. Returns 0 when this entity is not ours. */
    public static int color(Entity entity) {
        Integer color = COLORS.get(entity);
        return color == null ? 0 : color;
    }
}
