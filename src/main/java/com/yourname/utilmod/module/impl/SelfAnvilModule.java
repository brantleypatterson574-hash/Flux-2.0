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

/** Places an anvil above your own head as cover. */
public class SelfAnvilModule extends TickModule {
    private final NumberSetting delay = new NumberSetting("Delay", 8.0, 0.0, 40.0)
        .description("Ticks between anvil placements.");
    private final BooleanSetting requireEnemy = new BooleanSetting("Require Enemy", true)
        .description("Only places an anvil when another player is close by.");
    private final NumberSetting range = new NumberSetting("Enemy Range", 12.0, 3.0, 32.0)
        .description("How close another player must be for Require Enemy to pass.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot after placing.");

    private long nextPlace;

    public SelfAnvilModule() {
        super("Self Anvil", "Places an anvil above your head from the hotbar as quick cover.",
            Category.UTIL);
        addSetting(delay);
        addSetting(requireEnemy);
        addSetting(range);
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
        if (requireEnemy.isEnabled()
            && CombatBlockHelper.nearestPlayer(client, range.getValue(), p -> true) == null) return;

        BlockPos head = client.player.getBlockPos().up();
        if (!client.world.getBlockState(head).isAir()) return;

        int original = client.player.getInventory().getSelectedSlot();
        boolean placed = CombatBlockHelper.place(client, head, Items.ANVIL, true, true);
        if (swapBack.isEnabled()) ItemUtil.selectSlot(client.player, original);
        if (placed) nextPlace = now + (long) delay.getValue();
    }
}
