package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

/**
 * Moves better armor from the player's own inventory into empty armor slots.
 * Items are moved with the normal inventory screen handler, a few actions at
 * a time. Nothing is taken from other players, containers or the ground.
 */
public class AutoArmorModule extends Module {
    private static final EquipmentSlot[] ARMOR_SLOTS = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private final NumberSetting delayTicks = new NumberSetting("Delay Ticks", 10.0, 1.0, 100.0)
        .description("Ticks between armor moves. A larger delay is gentler on the server.");
    private final BooleanSetting replaceWeaker = new BooleanSetting("Replace Weaker", true)
        .description("Swap out equipped armor when a better piece is available.");
    private final NumberSetting minDurability = new NumberSetting("Min Durability %", 25.0, 0.0, 100.0)
        .unit("%")
        .description("Ignore armor with less remaining durability than this.");

    private ClientWorld lastWorld;
    private int tick;

    public AutoArmorModule() {
        super("Auto Armor", "Moves better armor into your own armor slots. Only touches your inventory. Diamond pants are a lifestyle upgrade.",
            Category.SWORD);
        addSetting(delayTicks);
        addSetting(replaceWeaker);
        addSetting(minDurability);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        tick = 0;
    }

    @Override
    public void onDisable() {
        tick = 0;
        lastWorld = null;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.interactionManager == null) return;
        // While a screen is open the server expects clicks for that screen, not
        // for the player inventory, so leave the player's GUI alone.
        if (client.currentScreen != null) return;
        if (client.world != lastWorld) {
            lastWorld = client.world;
            tick = 0;
        }
        if (tick++ % Math.max(1, (int) delayTicks.getValue()) != 0) return;

        PlayerInventory inventory = client.player.getInventory();
        PlayerScreenHandler handler = client.player.playerScreenHandler;
        if (!handler.getCursorStack().isEmpty()) return;

        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            EquipmentSlot slot = ARMOR_SLOTS[i];
            ItemStack worn = client.player.getEquippedStack(slot);
            if (!worn.isEmpty() && !replaceWeaker.isEnabled()) continue;

            int source = findBestSource(client, inventory, slot, worn);
            if (source < 0) continue;
            moveToSlot(client, handler, toScreenSlot(source),
                PlayerScreenHandler.EQUIPMENT_START + i);
            return;
        }
    }

    /** Returns the inventory slot with the highest simple armor score for this slot. */
    private int findBestSource(MinecraftClient client, PlayerInventory inventory,
                               EquipmentSlot slot, ItemStack worn) {
        if (ItemUtil.findSlot(client.player,
            stack -> isCandidate(client, stack, slot), false, false) < 0) return -1;

        double best = worn.isEmpty() ? Double.NEGATIVE_INFINITY : score(worn, slot);
        int bestSlot = -1;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack candidate = inventory.getStack(i);
            if (!isCandidate(client, candidate, slot)) continue;
            double value = score(candidate, slot);
            if (value > best + 0.0001) {
                best = value;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    private boolean isCandidate(MinecraftClient client, ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) return false;
        if (client.player.getPreferredEquipmentSlot(stack) != slot) return false;
        int maxDamage = stack.getMaxDamage();
        if (maxDamage <= 0) return true;
        double minPercent = minDurability.getValue();
        if (minPercent <= 0.0) return true;
        double remaining = (maxDamage - stack.getDamage()) * 100.0 / maxDamage;
        return remaining >= minPercent;
    }

    /** Armor value first, with toughness and durability as small tie breakers. */
    private static double score(ItemStack stack, EquipmentSlot slot) {
        AttributeModifiersComponent modifiers = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        double protection = modifiers == null
            ? 0.0 : modifiers.applyOperations(EntityAttributes.ARMOR, 0.0, slot);
        double toughness = modifiers == null
            ? 0.0 : modifiers.applyOperations(EntityAttributes.ARMOR_TOUGHNESS, 0.0, slot);
        return protection + toughness * 0.25 + stack.getMaxDamage() * 0.001;
    }

    /**
     * Picks up the source item, places it in the armor slot, then returns any
     * replaced piece to the now-empty source slot. One click group per action.
     */
    private void moveToSlot(MinecraftClient client, PlayerScreenHandler handler,
                            int sourceSlot, int armorSlot) {
        int sync = handler.syncId;
        client.interactionManager.clickSlot(sync, sourceSlot, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(sync, armorSlot, 0, SlotActionType.PICKUP, client.player);
        if (!handler.getCursorStack().isEmpty()) {
            client.interactionManager.clickSlot(sync, sourceSlot, 0, SlotActionType.PICKUP, client.player);
        }
    }

    /** Inventory indices and screen-handler slot ids only differ for the hotbar. */
    private static int toScreenSlot(int inventorySlot) {
        if (inventorySlot >= 0 && inventorySlot < PlayerInventory.HOTBAR_SIZE) {
            return PlayerScreenHandler.HOTBAR_START + inventorySlot;
        }
        return inventorySlot;
    }
}
