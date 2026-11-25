package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

/** Shift-clicks items out of an open container using the server's own inventory rules. */
public class ChestStealModule extends Module {
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 0.0, 40.0)
        .description("Ticks between items moved out of the container.");
    private final BooleanSetting requireContainer = new BooleanSetting("Container Only", true)
        .description("Only works while a chest, barrel or similar container screen is open.");
    private final BooleanSetting closeWhenEmpty = new BooleanSetting("Close When Empty", false)
        .description("Closes the screen once the container has no more items.");

    private long nextMove;

    public ChestStealModule() {
        super("Chest Steal", "Shift-clicks items from an open container into your inventory.", Category.UTIL);
        addSetting(delayTicks);
        addSetting(requireContainer);
        addSetting(closeWhenEmpty);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextMove = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (client.currentScreen == null) return;
        if (requireContainer.isEnabled() && !(client.currentScreen instanceof HandledScreen<?>)) return;

        long now = client.world.getTime();
        if (now < nextMove) return;

        var handler = client.player.currentScreenHandler;
        for (Slot slot : handler.slots) {
            if (slot.inventory == client.player.getInventory()) continue;
            if (slot.getStack().isEmpty() || !slot.canTakeItems(client.player)) continue;
            client.interactionManager.clickSlot(handler.syncId, slot.id, 0,
                SlotActionType.QUICK_MOVE, client.player);
            nextMove = now + (long) delayTicks.getValue();
            return;
        }

        if (closeWhenEmpty.isEnabled() && client.currentScreen instanceof HandledScreen<?>) {
            client.currentScreen.close();
        }
    }
}
