package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/** Charges and ignites nearby respawn anchors for defensive burst passages. */
public class AutoAnchorModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 8.0, 2.0, 24.0).description("How close a player must be before their respawn anchor is used.");
    private final NumberSetting actionDelay = new NumberSetting("Action Delay", 6.0, 0.0, 40.0).description("Ticks between charging and igniting actions.");
    private final BooleanSetting autoCharge = new BooleanSetting("Auto Charge", true).description("Uses glowstone to charge a nearby respawn anchor.");
    private final BooleanSetting autoIgnite = new BooleanSetting("Auto Ignite", true).description("Uses flint and steel to light a charged respawn anchor.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your original hotbar slot when the anchor work is done.");

    private int originalSlot = -1;
    private long nextAction;

    public AutoAnchorModule() {
        super("Auto Anchor",
            "Charges nearby respawn anchors with glowstone and ignites them with flint and steel.",
            Category.UTIL);
        addSetting(range);
        addSetting(actionDelay);
        addSetting(autoCharge);
        addSetting(autoIgnite);
        addSetting(swapBack);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        nextAction = 0L;
    }

    @Override
    public void onDisable() {
        restore();
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
            restore();
            return;
        }

        int glowstone = ItemUtil.findSlot(client.player, Items.GLOWSTONE, true, false);
        int flint = ItemUtil.findSlot(client.player, Items.FLINT_AND_STEEL, true, false);

        if (autoCharge.isEnabled() && glowstone >= 0 && use(client, anchor, glowstone)) {
            nextAction = now + (long) actionDelay.getValue();
            return;
        }
        if (autoIgnite.isEnabled() && flint >= 0 && use(client, anchor, flint)) {
            nextAction = now + (long) actionDelay.getValue();
        }
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
