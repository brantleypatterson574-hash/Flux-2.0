package com.yourname.utilmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Shared block-interaction helpers.
 *
 * Placing a block or an end crystal requires {@code interactBlock} with a real
 * {@link BlockHitResult}. Calling {@code interactItem} only uses the item and
 * never places anything, which is why the old crystal/trap modules were no-ops.
 */
public final class PlacementUtil {
    private PlacementUtil() {}

    /** Uses the exact crosshair block face reported by the client. */
    public static boolean interactCrosshairBlock(MinecraftClient client, Hand hand) {
        if (client.player == null || client.interactionManager == null
            || !(client.crosshairTarget instanceof BlockHitResult hit)) return false;
        ActionResult result = client.interactionManager.interactBlock(client.player, hand, hit);
        if (result.isAccepted()) client.player.swingHand(hand);
        return result.isAccepted();
    }

    /** Places into an air block by using the nearest valid neighbouring face. */
    public static boolean placeInAir(MinecraftClient client, BlockPos target, Hand hand) {
        if (client.player == null || client.world == null || client.interactionManager == null) return false;
        if (!client.world.getBlockState(target).isAir()) return false;

        for (Direction side : Direction.values()) {
            BlockPos support = target.offset(side);
            if (client.world.getBlockState(support).isAir()) continue;
            if (interactFace(client, support, side.getOpposite(), hand)) return true;
        }
        return false;
    }

    /** Places/uses an item against a known block face (for example on top of obsidian). */
    public static boolean interactFace(MinecraftClient client, BlockPos support, Direction face, Hand hand) {
        if (client.player == null || client.interactionManager == null) return false;
        Vec3d pos = Vec3d.ofCenter(support).add(
            new Vec3d(face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5));
        BlockHitResult hit = new BlockHitResult(pos, face, support, false);
        ActionResult result = client.interactionManager.interactBlock(client.player, hand, hit);
        if (result.isAccepted()) client.player.swingHand(hand);
        return result.isAccepted();
    }
}
