package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/**
 * Places a block into the space under your feet while falling. It is a normal
 * block placement, so the server still decides whether the space can be changed.
 */
public class BurrowModule extends TickModule {
    private enum BlockChoice { OBSIDIAN, COBBLESTONE }

    private final EnumSetting<BlockChoice> blockChoice =
        new EnumSetting<>("Block", BlockChoice.class, BlockChoice.OBSIDIAN)
            .description("Block placed under your feet.");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", 1.5, 0.5, 12.0)
        .description("How far you must fall before a block is placed.");
    private final NumberSetting cooldown = new NumberSetting("Cooldown", 20.0, 0.0, 100.0)
        .description("Ticks to wait after a successful placement.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after placing.");

    private long nextPlace;

    public BurrowModule() {
        super("Burrow", "Places a block under your feet while falling from a hotbar block.",
            Category.UTIL);
        addSetting(blockChoice);
        addSetting(fallDistance);
        addSetting(cooldown);
        addSetting(swapBack);
    }

    @Override
    public void onEnable() {
        nextPlace = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextPlace || client.player.isOnGround()) return;
        if (client.player.getVelocity().y >= 0.0
            || client.player.fallDistance < fallDistance.getValue()) return;

        BlockPos feet = client.player.getBlockPos();
        if (!client.world.getBlockState(feet).isAir()) return;

        int original = client.player.getInventory().getSelectedSlot();
        Item item = blockChoice.getValue() == BlockChoice.OBSIDIAN
            ? Items.OBSIDIAN : Items.COBBLESTONE;
        boolean placed = CombatBlockHelper.place(client, feet, item, true, true);
        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed) nextPlace = now + (long) cooldown.getValue();
    }
}
