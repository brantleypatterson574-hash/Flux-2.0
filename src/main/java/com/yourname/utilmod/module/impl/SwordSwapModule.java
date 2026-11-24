package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

/** Automatically equips the best hotbar sword when a target is close. */
public class SwordSwapModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 6.0, 2.0, 16.0).description("How close a target must be before swapping to a sword.");
    private final NumberSetting swapDelay = new NumberSetting("Swap Delay", 2.0, 0.0, 20.0).description("Ticks between sword swap attempts.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your previous hotbar slot when there is no target.");
    private final BooleanSetting preferSharpest = new BooleanSetting("Prefer Sharpest", true).description("Chooses the sword with the best enchantments, especially Sharpness.");

    private int originalSlot = -1;
    private long nextSwapTick;

    public SwordSwapModule() {
        super("Sword Swap",
            "Equips the best hotbar sword when an enemy is nearby, then optionally returns to your old slot.",
            Category.SWORD);
        addSetting(range);
        addSetting(swapDelay);
        addSetting(swapBack);
        addSetting(preferSharpest);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        nextSwapTick = 0L;
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        int current = client.player.getInventory().getSelectedSlot();

        boolean targetNear = !client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            p -> p != client.player && p.isAlive()).isEmpty();

        if (!targetNear) {
            restore();
            return;
        }

        int swordSlot = findSwordSlot(client.player);
        if (swordSlot < 0 || swordSlot == current) return;
        if (client.world.getTime() < nextSwapTick) return;

        if (originalSlot == -1) originalSlot = current;
        ItemUtil.selectSlot(client.player, swordSlot);
        nextSwapTick = client.world.getTime() + (long) swapDelay.getValue();
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        if (!swapBack.isEnabled()) {
            originalSlot = -1;
            return;
        }
        ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }

    private int findSwordSlot(PlayerEntity player) {
        int bestSlot = -1;
        int bestScore = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isIn(ItemTags.SWORDS)) continue;
            int score = preferSharpest.isEnabled() ? enchantScore(stack) : 0;
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    private static int enchantScore(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .mapToInt(enchantment -> enchantment.getKey()
                .map(key -> {
                    String path = key.getValue().getPath();
                    if (path.contains("sharpness")) return 4;
                    if (path.contains("smite") || path.contains("arthropods")) return 3;
                    if (path.contains("knockback") || path.contains("fire")) return 2;
                    return 1;
                })
                .orElse(0))
            .sum();
    }
}
