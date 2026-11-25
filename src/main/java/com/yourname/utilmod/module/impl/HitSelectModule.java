package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;

/**
 * Cancels a click when the entity under the crosshair does not match the
 * chosen rules. Real attacks still travel through the normal vanilla path.
 */
public class HitSelectModule extends Module {
    public static HitSelectModule INSTANCE;

    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Only applies the rules to other players.");
    private final BooleanSetting skipShields = new BooleanSetting("Skip Shields", true)
        .description("Does not click while the target is holding up a shield.");
    private final BooleanSetting groundedOnly = new BooleanSetting("Grounded Only", false)
        .description("Only clicks when the target is standing on a block.");
    private final NumberSetting maxHealth = new NumberSetting("Maximum Health", 20.0, 1.0, 20.0)
        .description("Ignores targets above this many half-hearts.");

    public HitSelectModule() {
        super("Hit Select", "Only clicks when the target under your crosshair matches your rules.", Category.SWORD);
        INSTANCE = this;
        addSetting(playersOnly);
        addSetting(skipShields);
        addSetting(groundedOnly);
        addSetting(maxHealth);
    }

    /** Called from the doAttack mixin before any attack packet is produced. */
    public static boolean shouldCancel(MinecraftClient client) {
        HitSelectModule module = INSTANCE;
        if (module == null || !module.isEnabled() || client.player == null) return false;
        if (!(client.crosshairTarget instanceof EntityHitResult hit)) return false;
        Entity entity = hit.getEntity();
        if (!(entity instanceof LivingEntity living) || entity == client.player) return false;
        if (module.playersOnly.isEnabled() && !(living instanceof PlayerEntity)) return false;
        if (module.skipShields.isEnabled() && living.isBlocking()) return true;
        if (module.groundedOnly.isEnabled() && !living.isOnGround()) return true;
        return living.getHealth() > module.maxHealth.getValue();
    }
}
