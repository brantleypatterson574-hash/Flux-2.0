package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/** Fills empty one-block holes around a nearby target. */
public class HoleFillerModule extends TickModule {
    private enum BlockChoice { OBSIDIAN, COBBLESTONE }

    private final NumberSetting targetRange = new NumberSetting("Target Range", 10.0, 3.0, 24.0)
        .description("How close a player must be before nearby holes are filled.");
    private final NumberSetting searchRadius = new NumberSetting("Search Radius", 4.0, 1.0, 10.0)
        .description("Horizontal radius around the target that is checked for holes.");
    private final NumberSetting delay = new NumberSetting("Delay", 4.0, 0.0, 40.0)
        .description("Ticks between fill passes.");
    private final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", 2.0, 1.0, 8.0)
        .description("How many holes may be filled in one pass.");
    private final EnumSetting<BlockChoice> blockChoice =
        new EnumSetting<>("Block", BlockChoice.class, BlockChoice.OBSIDIAN)
            .description("Block used to fill the hole.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after each pass.");

    private long nextPass;

    public HoleFillerModule() {
        super("Hole Filler", "Fills empty holes around a nearby target with hotbar blocks.",
            Category.UTIL);
        addSetting(targetRange);
        addSetting(searchRadius);
        addSetting(delay);
        addSetting(blocksPerTick);
        addSetting(blockChoice);
        addSetting(swapBack);
    }

    @Override
    public void onEnable() {
        nextPass = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextPass) return;

        PlayerEntity target = CombatBlockHelper.nearestPlayer(client, targetRange.getValue(), p -> true);
        if (target == null) return;

        int radius = Math.max(1, (int) searchRadius.getValue());
        int attempts = Math.max(1, (int) blocksPerTick.getValue());
        int placed = 0;
        int original = client.player.getInventory().getSelectedSlot();
        Item item = blockChoice.getValue() == BlockChoice.OBSIDIAN
            ? Items.OBSIDIAN : Items.COBBLESTONE;
        BlockPos base = target.getBlockPos();

        outer:
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos hole = base.add(dx, 0, dz);
                if (!client.world.getBlockState(hole).isAir()) continue;
                if (!client.world.getBlockState(hole.down()).isSolid()) continue;
                if (CombatBlockHelper.place(client, hole, item, true, true)) placed++;
                if (placed >= attempts) break outer;
            }
        }

        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed > 0) nextPass = now + (long) delay.getValue();
    }
}
