package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Uses Minecraft's own upside-down living-entity render flag. Nothing about the
 * entity, its name or its hitbox changes; only the client-side model pose does.
 */
public class DinnerboneModeModule extends Module {
    public static DinnerboneModeModule INSTANCE;

    private enum Target { PLAYERS, PLAYERS_AND_MOBS, EVERYTHING }

    private final EnumSetting<Target> target =
        new EnumSetting<>("Affected Entities", Target.class, Target.PLAYERS)
            .description("Choose which living entities render upside down.");
    private final BooleanSetting includeSelf = new BooleanSetting("Include Yourself", false)
        .description("Also flip your own player model, if your view allows seeing it.");
    private final NumberSetting chance = new NumberSetting("Chance", 100.0, 5.0, 100.0)
        .unit("%")
        .description("Percentage of matching entities that get flipped. Uses a stable per-entity roll.");

    public DinnerboneModeModule() {
        super("Dinnerbone Mode", "Turns nearby living models upside down using Minecraft's own rendering flag. Hitboxes and names stay normal.",
            Category.RENDER);
        INSTANCE = this;
        addSetting(target);
        addSetting(includeSelf);
        addSetting(chance);
    }

    /** Called from the living-entity renderer mixin before it builds render state. */
    public static boolean shouldFlip(LivingEntity entity) {
        DinnerboneModeModule module = INSTANCE;
        if (module == null || !module.isEnabled() || entity == null || !entity.isAlive()) return false;

        MinecraftClient client = MinecraftClient.getInstance();
        if (entity == client.player && !module.includeSelf.isEnabled()) return false;

        Target mode = module.target.getValue();
        boolean player = entity instanceof PlayerEntity;
        if (mode == Target.PLAYERS && !player) return false;
        if (mode == Target.PLAYERS_AND_MOBS && !(entity instanceof LivingEntity)) return false;

        float limit = Math.max(0.0f, Math.min(100.0f, (float) module.chance.getValue()));
        if (limit >= 100.0f) return true;
        float roll = Math.abs(entity.getUuid().hashCode() % 20_000) / 20_000.0f * 100.0f;
        return roll < limit;
    }
}
