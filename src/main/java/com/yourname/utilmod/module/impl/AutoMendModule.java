package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/** Keeps damaged Mending gear in the offhand so experience repairs it. */
public class AutoMendModule extends Module {
    private final BooleanSetting force = new BooleanSetting("Force", false)
        .description("Replace a non-repairable offhand item instead of leaving it alone.");
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true)
        .description("Turn the module off once nothing repairable is left.");
    private final BooleanSetting includeInventory = new BooleanSetting("Include Inventory", true)
        .description("Search the main inventory as well as the hotbar.");

    private boolean movedFromInventory;

    public AutoMendModule() {
        super("Auto Mend", "Automatically repairs Mending gear by keeping damaged items offhand.", Category.PLAYER);
        addSetting(force);
        addSetting(autoDisable);
        addSetting(includeInventory);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        movedFromInventory = false;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.currentScreen != null) return;

        ItemStack offhand = client.player.getStackInHand(Hand.OFF_HAND);
        if (!offhand.isEmpty() && hasMending(offhand) && isDamaged(offhand)) return;
        if (!offhand.isEmpty() && !force.isEnabled()) return;

        int slot = findRepairable(client);
        if (slot >= 0) {
            if (ItemUtil.swapOffhand(client, slot)) movedFromInventory = true;
            return;
        }

        if (!autoDisable.isEnabled()) return;
        if (movedFromInventory) {
            int empty = findEmpty(client.player.getInventory());
            if (empty >= 0) ItemUtil.swapOffhand(client, empty);
        }
        movedFromInventory = false;
        setEnabled(false);
    }

    private int findRepairable(MinecraftClient client) {
        PlayerInventory inventory = client.player.getInventory();
        int end = includeInventory.isEnabled() ? inventory.size() : 9;
        for (int i = 0; i < end; i++) {
            if (i == inventory.getSelectedSlot()) continue;
            ItemStack stack = inventory.getStack(i);
            if (hasMending(stack) && isDamaged(stack)) return i;
        }
        return -1;
    }

    private static int findEmpty(PlayerInventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) return i;
        }
        return -1;
    }

    private static boolean isDamaged(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Integer damage = stack.get(DataComponentTypes.DAMAGE);
        return damage != null && damage > 0;
    }

    private static boolean hasMending(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return net.minecraft.enchantment.EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .anyMatch(enchantment -> enchantment.getKey()
                .map(key -> "mending".equals(key.getValue().getPath()))
                .orElse(false));
    }
}
