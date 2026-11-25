package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/** Mines the blocks beside a nearby target's feet with normal attack clicks. */
public class AutoCityModule extends TickModule {
    private final NumberSetting targetRange = new NumberSetting("Target Range", 8.0, 3.0, 24.0)
        .description("How close a player must be before nearby blocks are mined.");
    private final NumberSetting breakRange = new NumberSetting("Break Range", 5.0, 2.0, 10.0)
        .description("Maximum distance from your own player to a block being mined.");
    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 40.0)
        .description("Ticks between mining passes.");
    private final NumberSetting blocksPerTick = new NumberSetting("Blocks Per Tick", 1.0, 1.0, 4.0)
        .description("How many adjacent blocks may be attacked in one pass.");
    private final BooleanSetting includeCorners = new BooleanSetting("Include Corners", true)
        .description("Also attacks the diagonal blocks around the target.");

    private long nextPass;

    public AutoCityModule() {
        super("Auto City", "Attacks mineable blocks around a nearby target's feet. Break rules are server side.",
            Category.UTIL);
        addSetting(targetRange);
        addSetting(delay);
        addSetting(breakRange);
        addSetting(blocksPerTick);
        addSetting(includeCorners);
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

        int attempts = Math.max(1, (int) blocksPerTick.getValue());
        int attacked = 0;
        double rangeSq = breakRange.getValue() * breakRange.getValue();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (!includeCorners.isEnabled() && dx != 0 && dz != 0) continue;
                BlockPos pos = target.getBlockPos().add(dx, 0, dz);
                if (client.player.getBlockPos().getSquaredDistance(pos) > rangeSq) continue;
                if (client.world.getBlockState(pos).isAir()) continue;
                if (CombatBlockHelper.breakBlock(client, pos, Direction.UP)) attacked++;
                if (attacked >= attempts) break;
            }
            if (attacked >= attempts) break;
        }
        if (attacked > 0) nextPass = now + (long) delay.getValue();
    }
}
