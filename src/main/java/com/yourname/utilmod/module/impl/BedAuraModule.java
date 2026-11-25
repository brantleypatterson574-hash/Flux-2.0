package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/** Places and breaks beds around a nearby target with standard block interactions. */
public class BedAuraModule extends TickModule {
    private final NumberSetting targetRange = new NumberSetting("Target Range", 10.0, 3.0, 24.0)
        .description("How close a player must be before a bed is placed or broken.");
    private final NumberSetting delay = new NumberSetting("Delay", 6.0, 0.0, 40.0)
        .description("Ticks between bed actions.");
    private final BooleanSetting autoPlace = new BooleanSetting("Auto Place", true)
        .description("Places a bed near the target while one is held in the hotbar.");
    private final BooleanSetting autoBreak = new BooleanSetting("Auto Break", true)
        .description("Attacks beds already close to the target.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after each action.");

    private long nextAction;

    public BedAuraModule() {
        super("Bed Aura", "Places and breaks beds near a target. Bed damage and block changes are server side.",
            Category.CRYSTAL);
        addSetting(targetRange);
        addSetting(delay);
        addSetting(autoPlace);
        addSetting(autoBreak);
        addSetting(swapBack);
    }

    @Override
    public void onEnable() {
        nextAction = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextAction) return;

        PlayerEntity target = CombatBlockHelper.nearestPlayer(client, targetRange.getValue(), p -> true);
        if (target == null) return;

        int original = client.player.getInventory().getSelectedSlot();
        boolean acted = false;
        if (autoBreak.isEnabled()) {
            BlockPos bed = CombatBlockHelper.findBedNear(client, target.getBlockPos(), 4);
            if (bed != null) acted = CombatBlockHelper.breakBlock(client, bed, Direction.UP);
        }
        if (!acted && autoPlace.isEnabled()) {
            BlockPos spot = target.getBlockPos().north();
            if (client.world.getBlockState(spot).isAir()) {
                acted = CombatBlockHelper.place(client, spot, Items.RED_BED, true, true);
            }
        }
        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (acted) nextAction = now + (long) delay.getValue();
    }
}
