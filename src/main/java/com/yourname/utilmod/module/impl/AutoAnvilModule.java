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

/** Drops anvils above a nearby target from the hotbar. */
public class AutoAnvilModule extends TickModule {
    private final NumberSetting targetRange = new NumberSetting("Target Range", 10.0, 3.0, 24.0)
        .description("Maximum distance to the player being targeted.");
    private final NumberSetting delay = new NumberSetting("Delay", 8.0, 0.0, 40.0)
        .description("Ticks between anvil placements.");
    private final NumberSetting height = new NumberSetting("Height", 2.0, 1.0, 4.0)
        .description("How many blocks above the target's feet the anvil is placed.");
    private final BooleanSetting placeAtFeet = new BooleanSetting("Place At Feet", false)
        .description("Places at head height instead of the configured height.");
    private final BooleanSetting rotate = new BooleanSetting("Silent Rotate", true)
        .description("Faces the placement briefly, then restores your view.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after each placement.");

    private long nextPlace;

    public AutoAnvilModule() {
        super("Auto Anvil", "Places anvils above a nearby target from your hotbar. The server validates each block.",
            Category.CRYSTAL);
        addSetting(targetRange);
        addSetting(delay);
        addSetting(height);
        addSetting(placeAtFeet);
        addSetting(rotate);
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

        int lift = placeAtFeet.isEnabled() ? 1 : (int) height.getValue();
        BlockPos pos = target.getBlockPos().up(lift);
        if (!client.world.getBlockState(pos).isAir()) return;

        int original = client.player.getInventory().getSelectedSlot();
        boolean placed = CombatBlockHelper.place(client, pos, Items.ANVIL,
            rotate.isEnabled(), true);
        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed) nextPlace = now + (long) delay.getValue();
    }
}
