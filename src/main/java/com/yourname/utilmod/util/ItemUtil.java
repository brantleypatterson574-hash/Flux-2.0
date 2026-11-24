package com.yourname.utilmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

/** Shared inventory helpers for the automation modules. */
public final class ItemUtil {
    private ItemUtil() {}

    public static int findSlot(PlayerEntity player, Item item, boolean hotbarOnly, boolean reverse) {
        return findSlot(player, stack -> stack.isOf(item), hotbarOnly, reverse);
    }

    public static int findSlot(PlayerEntity player, Predicate<ItemStack> predicate,
                               boolean hotbarOnly, boolean reverse) {
        int size = hotbarOnly ? 9 : player.getInventory().size();
        if (reverse) {
            for (int i = size - 1; i >= 0; i--) {
                if (predicate.test(player.getInventory().getStack(i))) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (predicate.test(player.getInventory().getStack(i))) return i;
            }
        }
        return -1;
    }

    public static int count(PlayerEntity player, Item item, boolean hotbarOnly) {
        int count = 0;
        int size = hotbarOnly ? 9 : player.getInventory().size();
        for (int i = 0; i < size; i++) {
            if (player.getInventory().getStack(i).isOf(item)) count++;
        }
        return count;
    }

    /** Counts the actual items in matching stacks (not just the number of stacks). */
    public static int countItems(PlayerEntity player, Item item, boolean hotbarOnly) {
        int count = 0;
        int size = hotbarOnly ? 9 : player.getInventory().size();
        for (int i = 0; i < size; i++) {
            var stack = player.getInventory().getStack(i);
            if (stack.isOf(item)) count += stack.getCount();
        }
        return count;
    }

    public static Hand handHolding(PlayerEntity player, Item item) {
        if (player.getStackInHand(Hand.MAIN_HAND).isOf(item)) return Hand.MAIN_HAND;
        if (player.getStackInHand(Hand.OFF_HAND).isOf(item)) return Hand.OFF_HAND;
        return null;
    }

    /** Selects a hotbar slot locally and tells the server, so the held item stays in sync. */
    public static void selectSlot(PlayerEntity player, int slot) {
        if (slot < 0 || slot >= 9 || player == null) return;
        player.getInventory().setSelectedSlot(slot);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == player && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }
    }

    /**
     * Converts a {@link PlayerInventory} index into the slot index used by the
     * player's screen handler. Hotbar indices 0-8 become screen slots 36-44;
     * main-inventory indices 9-35 already line up with screen slots 9-35.
     *
     * <p>{@code clickSlot}'s first slot argument is a screen-handler slot id, not a
     * raw inventory index. Passing a hotbar index directly hits the crafting
     * result slot (id 0) instead, which silently does nothing for offhand swaps.</p>
     */
    private static int toScreenSlot(int inventorySlot) {
        if (inventorySlot >= 0 && inventorySlot < PlayerInventory.HOTBAR_SIZE) {
            return PlayerScreenHandler.HOTBAR_START + inventorySlot;
        }
        return inventorySlot;
    }

    /** Swaps any inventory slot with a hotbar slot through the server-synced screen handler. */
    public static boolean swapHotbar(MinecraftClient client, int sourceSlot, int hotbarSlot) {
        if (client.player == null || client.interactionManager == null) return false;
        if (sourceSlot < 0 || hotbarSlot < 0 || hotbarSlot >= PlayerInventory.HOTBAR_SIZE) return false;
        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId,
            toScreenSlot(sourceSlot), hotbarSlot, SlotActionType.SWAP, client.player);
        return true;
    }

    /** Swaps any inventory slot with the offhand through the server-synced screen handler. */
    public static boolean swapOffhand(MinecraftClient client, int sourceSlot) {
        if (client.player == null || client.interactionManager == null || sourceSlot < 0) return false;
        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId,
            toScreenSlot(sourceSlot), PlayerInventory.OFF_HAND_SLOT, SlotActionType.SWAP, client.player);
        return true;
    }

    /** Swaps an inventory slot with the selected hotbar slot (client-side inventory state). */
    public static void swapToSelected(PlayerEntity player, int slot) {
        if (slot < 0 || slot >= 9) return;
        int selected = player.getInventory().getSelectedSlot();
        ItemStack fromSlot = player.getInventory().getStack(slot).copy();
        ItemStack fromHand = player.getInventory().getStack(selected).copy();
        player.getInventory().setStack(selected, fromSlot);
        player.getInventory().setStack(slot, fromHand);
    }
}
