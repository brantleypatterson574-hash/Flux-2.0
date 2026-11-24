package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.hit.EntityHitResult;

/** Attacks whatever is under the crosshair after a configurable reaction delay. */
public class TriggerBotModule extends Module {
    private final NumberSetting delay = new NumberSetting("Reaction Delay (Ms)", 80.0, 0.0, 600.0).description("Milliseconds to wait after the crosshair lands on a target before attacking.");
    private final NumberSetting range = new NumberSetting("Range", 5.0, 1.0, 16.0).description("Maximum distance to an entity that can be attacked.");
    private final BooleanSetting players = new BooleanSetting("Players", true).description("Allow other players to be attacked.");
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true).description("Allow mobs to be attacked.");
    private final BooleanSetting requireSword = new BooleanSetting("Require Sword", false).description("Only attacks while holding a sword.");
    private final BooleanSetting swing = new BooleanSetting("Swing Hand", true).description("Animates a hand swing with each attack.");

    private long targetSince = -1L;
    private long nextAttack;

    public TriggerBotModule() {
        super("Trigger Bot",
            "Attacks the entity under your crosshair after a configurable human-like delay.",
            Category.SWORD);
        addSetting(delay);
        addSetting(range);
        addSetting(players);
        addSetting(mobs);
        addSetting(requireSword);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        targetSince = -1L;
        nextAttack = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;

        ItemStack held = client.player.getMainHandStack();
        if (requireSword.isEnabled() && !held.isIn(ItemTags.SWORDS)) return;

        Entity target = client.crosshairTarget instanceof EntityHitResult hit ? hit.getEntity() : null;
        if (target == null || target == client.player || !target.isAlive()) {
            targetSince = -1L;
            return;
        }
        // Any rejection must clear the timer. Otherwise a target that becomes
        // valid later inherits the old timestamp and is attacked instantly,
        // defeating the configured reaction delay.
        if (target instanceof PlayerEntity && !players.isEnabled()) {
            targetSince = -1L;
            return;
        }
        if (target instanceof MobEntity && !mobs.isEnabled()) {
            targetSince = -1L;
            return;
        }
        if (client.player.getEyePos().distanceTo(target.getEyePos()) > range.getValue()) {
            targetSince = -1L;
            return;
        }

        long now = System.currentTimeMillis();
        if (targetSince < 0L) targetSince = now;
        if (now - targetSince < delay.getValue() || now < nextAttack) return;

        client.interactionManager.attackEntity(client.player, target);
        if (swing.isEnabled()) client.player.swingHand(client.player.getActiveHand());
        nextAttack = now + 50L;
    }
}
