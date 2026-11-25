package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

/** Keeps a chosen utility item in the offhand through normal inventory clicks. */
public class OffhandModule extends TickModule {
    private enum HoldItem { TOTEM, END_CRYSTAL, GLOWSTONE, COBWEB }

    private final EnumSetting<HoldItem> holdItem =
        new EnumSetting<>("Item", HoldItem.class, HoldItem.TOTEM)
            .description("Item to keep in the offhand.");
    private final NumberSetting delay = new NumberSetting("Delay", 2.0, 0.0, 40.0)
        .description("Ticks between offhand swaps.");
    private final NumberSetting minHealth = new NumberSetting("Health Threshold", 0.0, 0.0, 20.0)
        .description("0 always holds the item. Above 0 only holds it at or below this health.");
    private final BooleanSetting hotbarOnly = new BooleanSetting("Hotbar Only", false)
        .description("Searches only the hotbar instead of the whole inventory.");

    private long nextSwap;

    public OffhandModule() {
        super("Offhand", "Keeps a chosen item in your offhand. Turn off Auto Totem if both want that slot.",
            Category.UTIL);
        addSetting(holdItem);
        addSetting(delay);
        addSetting(minHealth);
        addSetting(hotbarOnly);
    }

    @Override
    public void onEnable() {
        nextSwap = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextSwap) return;
        if (minHealth.getValue() > 0.0
            && client.player.getHealth() + client.player.getAbsorptionAmount() > minHealth.getValue()) return;

        Item wanted = switch (holdItem.getValue()) {
            case END_CRYSTAL -> Items.END_CRYSTAL;
            case GLOWSTONE -> Items.GLOWSTONE;
            case COBWEB -> Items.COBWEB;
            default -> Items.TOTEM_OF_UNDYING;
        };
        if (client.player.getOffHandStack().isOf(wanted)) return;

        int slot = ItemUtil.findSlot(client.player, wanted, hotbarOnly.isEnabled(), false);
        if (slot < 0) return;
        if (ItemUtil.swapOffhand(client, slot)) nextSwap = now + (long) delay.getValue();
    }
}
