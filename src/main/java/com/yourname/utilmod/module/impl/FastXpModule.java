package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

/** Throws held experience bottles on a short timer using the vanilla use action. */
public class FastXpModule extends Module {
    private final BooleanSetting autoUse = new BooleanSetting("Auto Use", true)
        .description("Throws bottles automatically while one is held.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 0.0, 40.0)
        .description("Ticks between bottle throws.");
    private final BooleanSetting requireTarget = new BooleanSetting("Require Target", false)
        .description("Only throws when another entity is inside the configured range.");
    private final NumberSetting range = new NumberSetting("Range", 6.0, 1.0, 32.0)
        .unit("blocks")
        .description("Search radius used by Require Target.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Plays the normal arm swing with each throw.");

    private long nextThrow;

    public FastXpModule() {
        super("Fast XP", "Throws held experience bottles on a configurable delay.", Category.PLAYER);
        addSetting(autoUse);
        addSetting(delayTicks);
        addSetting(requireTarget);
        addSetting(range);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextThrow = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (!client.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) return;
        if (!autoUse.isEnabled()) return;

        long now = client.world.getTime();
        if (now < nextThrow) return;
        if (requireTarget.isEnabled() && client.world.getEntitiesByClass(
            net.minecraft.entity.Entity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            e -> e != client.player).isEmpty()) return;

        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        if (swing.isEnabled()) client.player.swingHand(Hand.MAIN_HAND);
        nextThrow = now + (long) delayTicks.getValue();
    }
}
