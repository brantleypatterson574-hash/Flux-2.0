package com.yourname.utilmod.util;

import net.minecraft.block.Block;
import net.minecraft.block.BedBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Original helpers for the crystal-PvP modules. Everything goes through the
 * normal client interaction manager; the server still decides what is allowed.
 */
public final class CombatBlockHelper {
    private CombatBlockHelper() {}

    public static PlayerEntity nearestPlayer(MinecraftClient client, double range,
                                             Predicate<PlayerEntity> filter) {
        if (client.player == null || client.world == null) return null;
        return client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range),
            p -> p != client.player && p.isAlive() && filter.test(p)
        ).stream().min(Comparator.comparingDouble(client.player::squaredDistanceTo)).orElse(null);
    }

    public static BlockPos findBlockNear(MinecraftClient client, BlockPos base,
                                         Block block, int radius) {
        if (client.world == null) return null;
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -2; dy <= 3; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    if (!client.world.getBlockState(pos).isOf(block)) continue;
                    double dist = pos.getSquaredDistance(base);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = pos;
                    }
                }
            }
        }
        return best;
    }

    public static BlockPos findBedNear(MinecraftClient client, BlockPos base, int radius) {
        if (client.world == null) return null;
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = base.add(dx, dy, dz);
                    if (!(client.world.getBlockState(pos).getBlock() instanceof BedBlock)) continue;
                    double dist = pos.getSquaredDistance(base);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = pos;
                    }
                }
            }
        }
        return best;
    }

    /** Selects a hotbar item if the player is not already holding it. */
    public static boolean hold(MinecraftClient client, Item item) {
        if (client.player == null) return false;
        if (client.player.getMainHandStack().isOf(item)) return true;
        int slot = ItemUtil.findSlot(client.player, item, true, false);
        if (slot < 0) return false;
        ItemUtil.selectSlot(client.player, slot);
        return true;
    }

    /** Places a block at an empty position using a normal adjacent-face click. */
    public static boolean place(MinecraftClient client, BlockPos pos, Item item,
                                boolean rotate, boolean swing) {
        if (client.world == null || client.player == null || client.interactionManager == null) return false;
        if (!client.world.getBlockState(pos).isAir() || !hold(client, item)) return false;

        for (Direction side : Direction.values()) {
            BlockPos support = pos.offset(side);
            if (!client.world.getBlockState(support).isSolid()) continue;
            Direction face = side.getOpposite();
            Vec3d hitPos = Vec3d.ofCenter(support).add(
                face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5);
            BlockHitResult hit = new BlockHitResult(hitPos, face, support, false);
            float[] previousView = rotate
                ? RotationUtil.face(client, Vec3d.ofCenter(pos)) : null;
            ActionResult result = client.interactionManager.interactBlock(
                client.player, Hand.MAIN_HAND, hit);
            if (previousView != null) RotationUtil.restore(client, previousView);
            if (result.isAccepted()) {
                if (swing) client.player.swingHand(Hand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }

    /** Uses an item directly on an existing block, for anchors, beds and similar blocks. */
    public static boolean useOnBlock(MinecraftClient client, BlockPos pos, Item item,
                                     boolean rotate, boolean swing) {
        if (client.world == null || client.player == null || client.interactionManager == null) return false;
        if (!hold(client, item)) return false;
        BlockHitResult hit = new BlockHitResult(
            new Vec3d(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5),
            Direction.UP, pos, false);
        float[] previousView = rotate
            ? RotationUtil.face(client, Vec3d.ofCenter(pos)) : null;
        ActionResult result = client.interactionManager.interactBlock(
            client.player, Hand.MAIN_HAND, hit);
        if (previousView != null) RotationUtil.restore(client, previousView);
        if (result.isAccepted() && swing) client.player.swingHand(Hand.MAIN_HAND);
        return result.isAccepted();
    }

    public static boolean breakBlock(MinecraftClient client, BlockPos pos, Direction side) {
        if (client.interactionManager == null) return false;
        return client.interactionManager.attackBlock(pos, side);
    }
}
