package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/** Tops up and lights nearby respawn anchors without wasting extra items. */
public class AutoAnchorModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 8.0, 2.0, 24.0).description("How close a player must be before their respawn anchor is used.");
    private final NumberSetting actionDelay = new NumberSetting("Action Delay", 6.0, 0.0, 40.0).description("Ticks between charging and igniting actions.");
    private final NumberSetting chargeTarget = new NumberSetting("Charge Target", 4.0, 1.0, 4.0)
        .description("Stop charging once the anchor has this many charges.");
    private final BooleanSetting autoCharge = new BooleanSetting("Auto Charge", true).description("Uses glowstone to top up a nearby respawn anchor.");
    private final BooleanSetting autoIgnite = new BooleanSetting("Auto Ignite", true).description("Uses flint and steel to light a charged respawn anchor once.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your original hotbar slot when the anchor work is done.");

    private int originalSlot = -1;
    private long nextAction;
    private BlockPos lastIgnitePos;
    private int lastIgniteCharges = -1;

    public AutoAnchorModule() {
        super("Auto Anchor",
            "Tops up nearby respawn anchors to the chosen charge target and lights them once.",
            Category.CRYSTAL);
        addSetting(range);
        addSetting(actionDelay);
        addSetting(chargeTarget);
        addSetting(autoCharge);
        addSetting(autoIgnite);
        addSetting(swapBack);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        nextAction = 0L;
        lastIgnitePos = null;
        lastIgniteCharges = -1;
    }

    @Override
    public void onDisable() {
        restore();
        lastIgnitePos = null;
        lastIgniteCharges = -1;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;
        long now = client.world.getTime();
        if (now < nextAction) return;

        PlayerEntity target = client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            p -> p != client.player && p.isAlive()).stream().findFirst().orElse(null);
        BlockPos anchor = target == null ? null : findAnchor(client, target.getBlockPos());
        if (anchor == null) {
            lastIgnitePos = null;
            lastIgniteCharges = -1;
            restore();
            return;
        }

        var state = client.world.getBlockState(anchor);
        if (!state.isOf(Blocks.RESPAWN_ANCHOR)) {
            lastIgnitePos = null;
            lastIgniteCharges = -1;
            restore();
            return;
        }
        int charges = state.get(RespawnAnchorBlock.CHARGES);
        int targetCharges = (int) Math.round(Math.max(1.0, Math.min(4.0, chargeTarget.getValue())));
        boolean toppedUp = charges >= targetCharges;

        int glowstone = ItemUtil.findSlot(client.player, Items.GLOWSTONE, true, false);
        int flint = ItemUtil.findSlot(client.player, Items.FLINT_AND_STEEL, true, false);

        if (autoCharge.isEnabled() && !toppedUp && glowstone >= 0) {
            lastIgnitePos = null;
            lastIgniteCharges = -1;
            if (use(client, anchor, glowstone)) {
                nextAction = now + (long) actionDelay.getValue();
                return;
            }
        }

        boolean chargedEnoughToLight = toppedUp || (!autoCharge.isEnabled() && charges > 0);
        boolean alreadyLit = lastIgnitePos != null && lastIgnitePos.equals(anchor)
            && lastIgniteCharges == charges;
        if (autoIgnite.isEnabled() && flint >= 0 && chargedEnoughToLight && !alreadyLit
            && use(client, anchor, flint)) {
            lastIgnitePos = anchor;
            lastIgniteCharges = charges;
            nextAction = now + (long) actionDelay.getValue();
            return;
        }
        restore();
    }

    private BlockPos findAnchor(MinecraftClient client, BlockPos base) {
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -2; dy <= 3; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    if (client.world.getBlockState(pos).isOf(Blocks.RESPAWN_ANCHOR)) return pos;
                }
            }
        }
        return null;
    }

    private boolean use(MinecraftClient client, BlockPos anchor, int slot) {
        if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
        ItemUtil.selectSlot(client.player, slot);
        Vec3d hitPos = new Vec3d(anchor.getX() + 0.5, anchor.getY() + 0.5, anchor.getZ() + 0.5);
        BlockHitResult hit = new BlockHitResult(hitPos, Direction.UP, anchor, false);
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        client.player.swingHand(Hand.MAIN_HAND);
        return true;
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        if (swapBack.isEnabled()) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
