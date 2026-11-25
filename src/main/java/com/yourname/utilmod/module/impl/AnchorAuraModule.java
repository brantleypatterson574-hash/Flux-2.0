package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import com.yourname.utilmod.util.ItemUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/** Charges and lights respawn anchors near a target using ordinary hotbar clicks. */
public class AnchorAuraModule extends TickModule {
    private final NumberSetting targetRange = new NumberSetting("Target Range", 12.0, 3.0, 32.0)
        .description("How close a player must be before an anchor is used.");
    private final NumberSetting delay = new NumberSetting("Delay", 5.0, 0.0, 40.0)
        .description("Ticks between charging and lighting actions.");
    private final BooleanSetting charge = new BooleanSetting("Charge Anchor", true)
        .description("Uses glowstone from the hotbar to charge a nearby respawn anchor.");
    private final BooleanSetting ignite = new BooleanSetting("Ignite Anchor", true)
        .description("Uses flint and steel to light a charged respawn anchor.");
    private final BooleanSetting antiSuicide = new BooleanSetting("Anti Suicide", true)
        .description("Skips ignition when your own health is too low for the blast.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar slot when the anchor work stops.");

    private long nextAction;
    private int originalSlot = -1;

    public AnchorAuraModule() {
        super("Anchor Aura", "Charges and lights respawn anchors near a target. The server still validates every action.",
            Category.CRYSTAL);
        addSetting(targetRange);
        addSetting(delay);
        addSetting(charge);
        addSetting(ignite);
        addSetting(antiSuicide);
        addSetting(swapBack);
    }

    @Override
    public void onEnable() {
        nextAction = 0L;
        originalSlot = -1;
    }

    @Override
    public void onDisable() {
        restore();
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextAction) return;

        PlayerEntity target = CombatBlockHelper.nearestPlayer(client, targetRange.getValue(), p -> true);
        BlockPos anchor = target == null ? null
            : CombatBlockHelper.findBlockNear(client, target.getBlockPos(), Blocks.RESPAWN_ANCHOR, 4);
        if (anchor == null) {
            restore();
            return;
        }

        if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
        boolean acted = false;
        if (charge.isEnabled() && CombatBlockHelper.hold(client, Items.GLOWSTONE)) {
            acted = CombatBlockHelper.useOnBlock(client, anchor, Items.GLOWSTONE, false, true);
        }
        boolean safeToIgnite = !antiSuicide.isEnabled()
            || client.player.getHealth() + client.player.getAbsorptionAmount() > 6.0f;
        if (!acted && ignite.isEnabled() && safeToIgnite
            && CombatBlockHelper.hold(client, Items.FLINT_AND_STEEL)) {
            acted = CombatBlockHelper.useOnBlock(client, anchor, Items.FLINT_AND_STEEL, false, true);
        }
        if (acted) nextAction = now + (long) delay.getValue();
        else restore();
    }

    private void restore() {
        if (originalSlot >= 0 && swapBack.isEnabled() && mc.player != null) {
            ItemUtil.selectSlot(mc.player, originalSlot);
        }
        originalSlot = -1;
    }
}
