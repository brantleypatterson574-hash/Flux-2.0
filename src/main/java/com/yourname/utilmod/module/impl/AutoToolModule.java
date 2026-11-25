package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;

/**
 * Holds the fastest hotbar tool for the block under the crosshair.
 * Only the selected slot changes; mining speed and break rules stay server side.
 */
public class AutoToolModule extends Module {
    private final BooleanSetting attackOnly = new BooleanSetting("Mining Only", true)
        .description("Only switches while you are holding the attack or use key.");
    private final NumberSetting minSpeed = new NumberSetting("Minimum Speed Gain", 0.5, 0.0, 10.0)
        .description("How much faster a tool must be before the client switches to it.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original slot when the crosshair leaves the block.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 1.0, 0.0, 20.0)
        .description("Ticks between tool checks.");

    private int originalSlot = -1;
    private long nextCheck;

    public AutoToolModule() {
        super("Auto Tool", "Switches to the best hotbar tool for the block you are looking at.", Category.PLAYER);
        addSetting(attackOnly);
        addSetting(minSpeed);
        addSetting(swapBack);
        addSetting(delayTicks);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null
            || !(client.crosshairTarget instanceof BlockHitResult hit)) {
            restore();
            return;
        }
        if (attackOnly.isEnabled()
            && !client.options.attackKey.isPressed() && !client.options.useKey.isPressed()) {
            restore();
            return;
        }

        BlockState state = client.world.getBlockState(hit.getBlockPos());
        if (state.isAir()) {
            restore();
            return;
        }

        long now = client.world.getTime();
        if (now < nextCheck) return;
        nextCheck = now + (long) delayTicks.getValue();

        int best = -1;
        float bestSpeed = 1.0f;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = client.player.getInventory().getStack(slot);
            if (stack.isEmpty()) continue;
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed + minSpeed.getFloat()) {
                bestSpeed = speed;
                best = slot;
            }
        }

        if (best < 0 || best == client.player.getInventory().getSelectedSlot()) return;
        if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
        ItemUtil.selectSlot(client.player, best);
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        if (swapBack.isEnabled()) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
