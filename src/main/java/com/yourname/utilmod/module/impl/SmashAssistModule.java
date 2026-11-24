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
import net.minecraft.item.Items;

/** Swaps to the best Density mace during a fall strike and snaps back after. */
public class SmashAssistModule extends Module {
    private final NumberSetting minFallDistance = new NumberSetting("Minimum Fall Distance", 1.5, 0.5, 20.0).description("Fall distance needed before the Density mace swap is made.");
    private final NumberSetting range = new NumberSetting("Target Range", 6.0, 2.0, 16.0).description("How close an enemy must be before the swap is made.");
    private final NumberSetting snapbackTicks = new NumberSetting("Snap Back Delay", 3.0, 0.0, 20.0).description("Ticks to wait before returning to your previous hotbar slot.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your previous hotbar slot after the strike.");
    private final BooleanSetting requireDensity = new BooleanSetting("Require Density", true).description("Only swaps when a Density mace is available.");

    private int originalSlot = -1;
    private long swapBackAt;

    public SmashAssistModule() {
        super("Smash Assist",
            "Automatically holds a Density mace when a fall strike is ready, then returns to your previous slot.",
            Category.MACE);
        addSetting(minFallDistance);
        addSetting(range);
        addSetting(snapbackTicks);
        addSetting(swapBack);
        addSetting(requireDensity);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        swapBackAt = 0L;
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        long now = client.world.getTime();
        int current = client.player.getInventory().getSelectedSlot();

        if (swapBackAt > 0L && now >= swapBackAt) {
            restore();
            swapBackAt = 0L;
            return;
        }

        boolean falling = client.player.fallDistance >= minFallDistance.getValue()
            || (!client.player.isOnGround() && client.player.getVelocity().y < -0.35);
        if (!falling) {
            if (swapBackAt == 0L) restore();
            return;
        }

        boolean targetNear = !client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            p -> p != client.player && p.isAlive()).isEmpty();
        if (!targetNear) return;

        int maceSlot = findDensityMace(client.player);
        if (maceSlot < 0 || maceSlot == current) return;
        if (originalSlot == -1) originalSlot = current;
        ItemUtil.selectSlot(client.player, maceSlot);
        swapBackAt = now + (swapBack.isEnabled() ? (long) snapbackTicks.getValue() : 0L);
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }

    private int findDensityMace(PlayerEntity player) {
        int fallback = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isOf(Items.MACE)) continue;
            if (fallback < 0) fallback = i;
            boolean density = EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
                .anyMatch(enchantment -> enchantment.getKey()
                    .map(key -> key.getValue().getPath().contains("density"))
                    .orElse(false));
            if (density || !requireDensity.isEnabled()) return i;
        }
        return fallback;
    }
}
