package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

/** Throws a pearl downward just before a long fall would land. */
public class PearlCatchModule extends Module {
    private final NumberSetting minFall = new NumberSetting("Minimum Fall Distance", 5.0, 2.0, 60.0)
        .unit("blocks")
        .description("Starts looking for a catch once you have fallen this far.");
    private final NumberSetting throwPitch = new NumberSetting("Throw Pitch", 90.0, 0.0, 90.0)
        .description("Angle used for the pearl throw; 90 points straight down.");
    private final BooleanSetting requireEnemy = new BooleanSetting("Require Enemy", true)
        .description("Only catches when another player is nearby.");
    private final NumberSetting enemyRange = new NumberSetting("Enemy Range", 16.0, 2.0, 48.0)
        .unit("blocks")
        .description("Search radius used by Require Enemy.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your previous hotbar slot after the throw.");
    private final NumberSetting cooldownTicks = new NumberSetting("Cooldown", 40.0, 5.0, 200.0)
        .description("Minimum ticks between automatic pearl catches.");

    private int originalSlot = -1;
    private float[] oldRotation;
    private int restoreTicks;
    private long nextCatch;

    public PearlCatchModule() {
        super("Pearl Catch", "Throws an ender pearl downward to catch a long fall.", Category.MACE);
        addSetting(minFall);
        addSetting(throwPitch);
        addSetting(requireEnemy);
        addSetting(enemyRange);
        addSetting(swapBack);
        addSetting(cooldownTicks);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextCatch = 0L;
        restore();
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;

        if (restoreTicks > 0 && --restoreTicks == 0) restore();

        if (client.player.fallDistance < minFall.getValue()
            || client.player.isOnGround() || client.player.getVelocity().y >= 0.0) return;
        if (requireEnemy.isEnabled() && client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(enemyRange.getValue()),
            p -> p != client.player && p.isAlive()).isEmpty()) return;

        long now = client.world.getTime();
        if (now < nextCatch) return;
        int pearlSlot = ItemUtil.findSlot(client.player, Items.ENDER_PEARL, true, false);
        if (pearlSlot < 0) return;

        if (pearlSlot != client.player.getInventory().getSelectedSlot()) {
            originalSlot = client.player.getInventory().getSelectedSlot();
            ItemUtil.selectSlot(client.player, pearlSlot);
        }
        oldRotation = new float[] { client.player.getYaw(), client.player.getPitch() };
        RotationUtil.apply(client, client.player.getYaw(), throwPitch.getFloat());
        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        client.player.swingHand(Hand.MAIN_HAND);
        restoreTicks = 1;
        nextCatch = now + (long) cooldownTicks.getValue();
    }

    private void restore() {
        if (mc.player != null) {
            if (oldRotation != null) RotationUtil.restore(mc, oldRotation);
            if (swapBack.isEnabled() && originalSlot >= 0) ItemUtil.selectSlot(mc.player, originalSlot);
        }
        oldRotation = null;
        originalSlot = -1;
        restoreTicks = 0;
    }
}
