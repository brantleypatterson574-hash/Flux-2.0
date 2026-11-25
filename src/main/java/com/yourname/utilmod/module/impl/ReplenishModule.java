package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

/** Moves a matching stack from the backpack into a nearly empty hotbar slot. */
public class ReplenishModule extends Module {
    private final NumberSetting refillAt = new NumberSetting("Refill At", 1.0, 1.0, 32.0)
        .description("Refills a hotbar slot once it drops to this many items.");
    private final BooleanSetting skipHeld = new BooleanSetting("Skip Held Slot", false)
        .description("Leaves the item in your hand alone.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 0.0, 40.0)
        .description("Ticks between refills.");

    private long nextRefill;

    public ReplenishModule() {
        super("Replenish", "Refills low hotbar stacks from matching items in your inventory.", Category.PLAYER);
        addSetting(refillAt);
        addSetting(skipHeld);
        addSetting(delayTicks);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextRefill = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.currentScreen != null) return;
        long now = client.world.getTime();
        if (now < nextRefill) return;

        int held = client.player.getInventory().getSelectedSlot();
        for (int hotbar = 0; hotbar < 9; hotbar++) {
            if (skipHeld.isEnabled() && hotbar == held) continue;
            ItemStack low = client.player.getInventory().getStack(hotbar);
            if (low.isEmpty() || low.getCount() > refillAt.getValue()) continue;

            for (int source = 9; source < client.player.getInventory().size(); source++) {
                ItemStack candidate = client.player.getInventory().getStack(source);
                if (candidate.isEmpty() || candidate.getCount() <= low.getCount()) continue;
                if (!ItemStack.areItemsAndComponentsEqual(candidate, low)) continue;
                if (ItemUtil.swapHotbar(client, source, hotbar)) {
                    nextRefill = now + (long) delayTicks.getValue();
                    return;
                }
            }
        }
    }
}
