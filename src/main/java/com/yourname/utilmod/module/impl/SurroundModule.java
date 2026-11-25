package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Builds a small protective shell around your own player from the hotbar.
 * Blocks are really placed, so this is only for a private server running the
 * matching jar. The server still checks every placement.
 */
public class SurroundModule extends Module {
    private enum BlockChoice { OBSIDIAN, WEB }

    private static final int[][] SIDE_OFFSETS = {
        { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 },
        { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }
    };

    private final EnumSetting<BlockChoice> blockChoice =
        new EnumSetting<>("Block Choice", BlockChoice.class, BlockChoice.OBSIDIAN)
            .description("Obsidian makes a tough shell; cobweb slows anything trying to walk in.");
    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 40.0)
        .description("Ticks between placements while the shell is being built.");
    private final NumberSetting maxBlocksPerTick = new NumberSetting("Max Blocks Per Tick", 2.0, 1.0, 9.0)
        .description("How many shell positions to try in a single tick.");
    private final BooleanSetting includeUnderfoot = new BooleanSetting("Include Underfoot", true)
        .description("Also fills the block directly beneath your feet.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Faces each placement spot briefly, then puts your view back.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Animates your hand when a placement is accepted.");

    private final java.util.Set<BlockPos> placed = new java.util.HashSet<>();
    private BlockPos lastBase;
    private ClientWorld lastWorld;
    private long nextPlace;
    private int originalSlot = -1;

    public SurroundModule() {
        super("Surround", "Places obsidian or cobweb around you. Block changes are validated by the server.",
            Category.UTIL);
        addSetting(blockChoice);
        addSetting(delay);
        addSetting(maxBlocksPerTick);
        addSetting(includeUnderfoot);
        addSetting(silentRotate);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        placed.clear();
        lastBase = null;
        lastWorld = null;
        nextPlace = 0L;
        restoreSlot();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;
        if (client.world != lastWorld) {
            placed.clear();
            lastBase = null;
            lastWorld = client.world;
        }
        long now = client.world.getTime();
        if (now < nextPlace) return;

        Item wanted = blockChoice.getValue() == BlockChoice.OBSIDIAN ? Items.OBSIDIAN : Items.COBWEB;
        int slot = ItemUtil.findSlot(client.player, wanted, true, false);
        if (slot < 0) return;
        if (originalSlot < 0 && !client.player.getMainHandStack().isOf(wanted)) {
            originalSlot = client.player.getInventory().getSelectedSlot();
        }
        if (!client.player.getMainHandStack().isOf(wanted)) ItemUtil.selectSlot(client.player, slot);

        BlockPos base = client.player.getBlockPos();
        if (!base.equals(lastBase)) {
            placed.clear();
            lastBase = base;
        }
        int attempts = Math.max(1, (int) maxBlocksPerTick.getValue());
        int placedNow = 0;
        for (int[] offset : SIDE_OFFSETS) {
            if (placedNow >= attempts) break;
            BlockPos spot = base.add(offset[0], offset[1], offset[2]);
            if (placed.contains(spot) || !client.world.getBlockState(spot).isAir()) continue;
            if (place(client, client.world, spot)) {
                placed.add(spot);
                placedNow++;
            }
        }
        if (includeUnderfoot.isEnabled() && placedNow < attempts) {
            BlockPos under = base.down();
            if (!placed.contains(under) && client.world.getBlockState(under).isAir()
                && place(client, client.world, under)) {
                placed.add(under);
                placedNow++;
            }
        }
        if (placedNow > 0) nextPlace = now + (long) delay.getValue();
    }

    private boolean place(MinecraftClient client, World world, BlockPos spot) {
        for (Direction side : Direction.values()) {
            BlockPos support = spot.offset(side);
            if (!world.getBlockState(support).isSolid()) continue;
            Direction face = side.getOpposite();
            Vec3d hitPos = Vec3d.ofCenter(support).add(
                face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5);
            BlockHitResult hit = new BlockHitResult(hitPos, face, support, false);
            float[] previousView = silentRotate.isEnabled()
                ? RotationUtil.face(client, Vec3d.ofCenter(spot)) : null;
            ActionResult result = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
            if (previousView != null) RotationUtil.restore(client, previousView);
            if (result.isAccepted()) {
                if (swing.isEnabled()) client.player.swingHand(Hand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }

    private void restoreSlot() {
        if (originalSlot >= 0 && mc.player != null) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
