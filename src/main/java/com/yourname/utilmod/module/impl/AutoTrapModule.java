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

/** Builds a simple box around a nearby target from hotbar blocks. */
public class AutoTrapModule extends TickModule {
    private enum BlockChoice { OBSIDIAN, COBBLESTONE }

    private final NumberSetting targetRange = new NumberSetting("Target Range", 12.0, 3.0, 32.0)
        .description("How close a player must be before a trap is built.");
    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 40.0)
        .description("Ticks between block placements.");
    private final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", 2.0, 1.0, 8.0)
        .description("How many trap positions to try in one tick.");
    private final EnumSetting<BlockChoice> blockChoice =
        new EnumSetting<>("Block", BlockChoice.class, BlockChoice.OBSIDIAN)
            .description("Block used for the trap walls and roof.");
    private final BooleanSetting roof = new BooleanSetting("Roof", true)
        .description("Also closes the top half of the box.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after a placement pass.");

    private long nextPlace;

    public AutoTrapModule() {
        super("Auto Trap", "Builds an obsidian or cobblestone box around a nearby target.",
            Category.UTIL);
        addSetting(targetRange);
        addSetting(delay);
        addSetting(blocksPerTick);
        addSetting(blockChoice);
        addSetting(roof);
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

        PlayerEntity target = CombatBlockHelper.nearestPlayer(client, targetRange.getValue(), p -> true);
        if (target == null) return;

        int original = client.player.getInventory().getSelectedSlot();
        Item item = blockChoice.getValue() == BlockChoice.OBSIDIAN
            ? Items.OBSIDIAN : Items.COBBLESTONE;
        int attempts = Math.max(1, (int) blocksPerTick.getValue());
        int placed = 0;
        BlockPos base = target.getBlockPos();

        outer:
        for (int lift = 0; lift <= (roof.isEnabled() ? 1 : 0); lift++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
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
