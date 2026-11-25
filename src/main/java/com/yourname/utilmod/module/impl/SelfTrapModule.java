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

/** Builds a small cover shell over your own head. */
public class SelfTrapModule extends TickModule {
    private enum BlockChoice { OBSIDIAN, COBBLESTONE }

    private final EnumSetting<BlockChoice> blockChoice =
        new EnumSetting<>("Block", BlockChoice.class, BlockChoice.OBSIDIAN)
            .description("Block used for the shell.");
    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 40.0)
        .description("Ticks between placements.");
    private final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", 2.0, 1.0, 8.0)
        .description("How many shell positions to try in one tick.");
    private final BooleanSetting topOnly = new BooleanSetting("Top Only", false)
        .description("Only closes the upper layer instead of both layers.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after each pass.");

    private long nextPlace;

    public SelfTrapModule() {
        super("Self Trap", "Places blocks above and around your own head from the hotbar.",
            Category.CRYSTAL);
        addSetting(blockChoice);
        addSetting(delay);
        addSetting(blocksPerTick);
        addSetting(topOnly);
        addSetting(swapBack);
    }

    @Override
    public void onEnable() {
        nextPlace = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextPlace) return;

        int startLayer = topOnly.isEnabled() ? 2 : 1;
        int attempts = Math.max(1, (int) blocksPerTick.getValue());
        int placed = 0;
        int original = client.player.getInventory().getSelectedSlot();
        Item item = blockChoice.getValue() == BlockChoice.OBSIDIAN
            ? Items.OBSIDIAN : Items.COBBLESTONE;
        BlockPos base = client.player.getBlockPos();

        outer:
        for (int lift = startLayer; lift <= 2; lift++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = base.add(dx, lift, dz);
                    if (!client.world.getBlockState(pos).isAir()) continue;
                    if (CombatBlockHelper.place(client, pos, item, true, true)) placed++;
                    if (placed >= attempts) break outer;
                }
            }
        }

        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed > 0) nextPlace = now + (long) delay.getValue();
    }
}
