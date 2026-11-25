package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/** Places cobwebs around your own feet when an enemy is close. */
public class SelfWebModule extends TickModule {
    private final NumberSetting range = new NumberSetting("Enemy Range", 8.0, 3.0, 24.0)
        .description("How close another player must be before webs are placed.");
    private final NumberSetting delay = new NumberSetting("Delay", 4.0, 0.0, 40.0)
        .description("Ticks between web passes.");
    private final BooleanSetting doublePlace = new BooleanSetting("Double Place", false)
        .description("Also places webs in the upper body layer.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after each pass.");

    private long nextPass;

    public SelfWebModule() {
        super("Self Web", "Places cobweb around your own feet from the hotbar when an enemy is close.",
            Category.CRYSTAL);
        addSetting(range);
        addSetting(delay);
        addSetting(doublePlace);
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
        if (CombatBlockHelper.nearestPlayer(client, range.getValue(), p -> true) == null) return;

        int original = client.player.getInventory().getSelectedSlot();
        int placed = 0;
        BlockPos base = client.player.getBlockPos();
        int topLayer = doublePlace.isEnabled() ? 1 : 0;

        outer:
        for (int lift = 0; lift <= topLayer; lift++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    BlockPos pos = base.add(dx, lift, dz);
                    if (!client.world.getBlockState(pos).isAir()) continue;
                    if (CombatBlockHelper.place(client, pos, Items.COBWEB, true, true)) placed++;
                    if (placed >= 4) break outer;
                }
            }
        }

        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed > 0) nextPass = now + (long) delay.getValue();
    }
}
