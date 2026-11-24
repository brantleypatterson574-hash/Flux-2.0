package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/** Waits for a real vanilla critical-hit window before attacking with a sword. */
public class CritAssistModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 10.0).description("How close a target must be before a critical hit is attempted.");
    private final NumberSetting delay = new NumberSetting("Attack Delay", 2.0, 0.0, 20.0).description("Ticks between critical-hit attacks.");
    private final BooleanSetting requireSword = new BooleanSetting("Require Sword", true).description("Only attacks while holding a sword.");
    private final BooleanSetting requireFalling = new BooleanSetting("Require Falling", true).description("Only attacks while you are falling, when critical hits occur.");

    private long nextAttack;

    public CritAssistModule() {
        super("Crit Assist",
            "Only swings when the vanilla critical-hit conditions are met: airborne, falling and cooldown ready.",
            Category.SWORD);
        addSetting(range);
        addSetting(delay);
        addSetting(requireSword);
        addSetting(requireFalling);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextAttack = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null) return;
        if (client.currentScreen != null) return;

        ItemStack held = client.player.getMainHandStack();
        if (requireSword.isEnabled() && !held.isIn(ItemTags.SWORDS)) return;
        if (client.player.getAttackCooldownProgress(0.5f) < 1.0f) return;
        if (client.player.isOnGround()) return;
        if (requireFalling.isEnabled() && client.player.getVelocity().y >= 0.0) return;
        if (client.world.getTime() < nextAttack) return;

        Vec3d eye = client.player.getEyePos();
        Box box = client.player.getBoundingBox().expand(range.getValue());
        Entity target = client.world.getEntitiesByClass(PlayerEntity.class, box,
            p -> p != client.player && p.isAlive()
                && eye.distanceTo(p.getEyePos()) <= range.getValue()).stream().findFirst().orElse(null);
        if (target == null) return;

        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(client.player.getActiveHand());
        nextAttack = client.world.getTime() + (long) delay.getValue();
    }
}
