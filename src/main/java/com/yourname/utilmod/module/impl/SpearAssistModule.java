package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/** Holds a trident while the nearest enemy is too far away for a melee hit. */
public class SpearAssistModule extends Module {
    private final NumberSetting swapRange = new NumberSetting("Swap Range", 9.0, 2.0, 24.0)
        .unit("blocks")
        .description("Picks a target inside this distance.");
    private final NumberSetting meleeRange = new NumberSetting("Melee Range", 3.5, 1.0, 10.0)
        .unit("blocks")
        .description("Returns to your old item when the target comes this close.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to the previous slot when no spear target is present.");
    private final BooleanSetting anyTrident = new BooleanSetting("Any Trident", true)
        .description("Uses any trident; otherwise only an enchanted one is considered.");

    private int originalSlot = -1;
    private boolean holdingSpear;

    public SpearAssistModule() {
        super("Spear Assist", "Holds a trident while the nearest enemy is outside melee range.", Category.MACE);
        addSetting(swapRange);
        addSetting(meleeRange);
        addSetting(swapBack);
        addSetting(anyTrident);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.currentScreen != null) return;

        PlayerEntity target = client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(swapRange.getValue()),
            p -> p != client.player && p.isAlive()).stream()
            .filter(p -> p.distanceTo(client.player) > meleeRange.getValue())
            .findFirst().orElse(null);

        if (target == null) {
            restore();
            return;
        }

        if (client.player.getMainHandStack().isOf(Items.TRIDENT)) {
            holdingSpear = true;
            return;
        }

        int tridentSlot = findTrident(client);
        if (tridentSlot < 0 || tridentSlot == client.player.getInventory().getSelectedSlot()) return;
        if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
        ItemUtil.selectSlot(client.player, tridentSlot);
        holdingSpear = true;
    }

    private int findTrident(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (!stack.isOf(Items.TRIDENT)) continue;
            if (anyTrident.isEnabled() || stack.hasEnchantments()) return i;
        }
        return -1;
    }

    private void restore() {
        if (!holdingSpear && originalSlot < 0) return;
        if (mc.player != null && swapBack.isEnabled() && originalSlot >= 0) {
            ItemUtil.selectSlot(mc.player, originalSlot);
        }
        originalSlot = -1;
        holdingSpear = false;
    }
}
