package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Places cobweb spaces around a nearby living target. This module changes
 * blocks in the world, so it is intended only for a private server running the
 * matching jar. The server still validates every placement.
 */
public class AutoWebModule extends Module {
    private static final int[][] OFFSETS = {
        { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 },
        { 1, 1, 0 }, { -1, 1, 0 }, { 0, 1, 1 }, { 0, 1, -1 }
    };

    private final NumberSetting range = new NumberSetting("Range", 8.0, 2.0, 24.0)
        .description("Maximum distance to the player or mob being webbed.");
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 4.0, 0.0, 40.0)
        .description("Ticks to wait after a placement before trying again.");
    private final NumberSetting maxWebsPerTick = new NumberSetting("Max Webs Per Tick", 2.0, 1.0, 8.0)
        .description("How many cobweb positions to attempt in one tick.");
    private final BooleanSetting onlyNearbyTarget = new BooleanSetting("Only Nearby Target", true)
        .description("Requires a target inside Range. Turn off to web your own feet as a defence.");
    private final BooleanSetting requireOnGround = new BooleanSetting("Require On Ground", true)
        .description("Only places while the chosen target is standing on the ground.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Turns toward each spot, then restores your previous view right after placing.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Animates your hand when the server accepts a placement.");

    private final Set<BlockPos> placed = new HashSet<>();
    private LivingEntity target;
    private BlockPos lastBase;
    private ClientWorld lastWorld;
    private long nextPlace;
    private int originalSlot = -1;

    public AutoWebModule() {
        super("Auto Web", "Places cobweb around a nearby target. Block changes are validated by the server.",
            Category.SWORD);
        addSetting(range);
        addSetting(placeDelay);
        addSetting(maxWebsPerTick);
        addSetting(onlyNearbyTarget);
        addSetting(requireOnGround);
        addSetting(silentRotate);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        placed.clear();
        target = null;
        lastBase = null;
        lastWorld = null;
        nextPlace = 0L;
        restoreSlot();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) {
            return;
        }
        if (client.world != lastWorld) {
            placed.clear();
            target = null;
            lastBase = null;
            lastWorld = client.world;
        }
        long now = client.world.getTime();
        if (now < nextPlace) return;

        LivingEntity found = findTarget(client);
        if (onlyNearbyTarget.isEnabled() && found == null) {
            placed.clear();
            return;
        }
        if (found != target) {
            placed.clear();
            target = found;
        }
        if (requireOnGround.isEnabled()
            && !(found == null ? client.player.isOnGround() : found.isOnGround())) return;

        int slot = ItemUtil.findSlot(client.player, Items.COBWEB, true, false);
        if (slot < 0) return;
        if (originalSlot < 0 && !client.player.getMainHandStack().isOf(Items.COBWEB)) {
            originalSlot = client.player.getInventory().getSelectedSlot();
        }
        if (!client.player.getMainHandStack().isOf(Items.COBWEB)) ItemUtil.selectSlot(client.player, slot);

        BlockPos base = found == null ? client.player.getBlockPos() : found.getBlockPos();
        if (!base.equals(lastBase)) {
            placed.clear();
            lastBase = base;
        }
        int attempts = Math.max(1, (int) maxWebsPerTick.getValue());
        int placedNow = 0;
        for (int[] offset : OFFSETS) {
            if (placedNow >= attempts) break;
            BlockPos spot = base.add(offset[0], offset[1], offset[2]);
            if (placed.contains(spot) || !client.world.getBlockState(spot).isAir()) continue;

            float[] previousView = silentRotate.isEnabled()
                ? RotationUtil.face(client, Vec3d.ofCenter(spot)) : null;
            boolean ok = placeInAir(client.world, spot, client);
            if (previousView != null) RotationUtil.restore(client, previousView);
            if (!ok) continue;

            placed.add(spot);
            placedNow++;
        }
        if (placedNow > 0) nextPlace = now + (long) placeDelay.getValue();
    }

    private LivingEntity findTarget(MinecraftClient client) {
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (LivingEntity entity : client.world.getEntitiesByClass(LivingEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            candidate -> candidate != client.player && candidate.isAlive() && !candidate.isSpectator())) {
            double distance = client.player.squaredDistanceTo(entity);
            if (distance <= range.getValue() * range.getValue() && distance < bestDistance) {
                bestDistance = distance;
                best = entity;
            }
        }
        return best;
    }

    /** Uses the nearest solid neighbour face, so cobweb can go into open air. */
    private boolean placeInAir(World world, BlockPos spot, MinecraftClient client) {
        for (Direction side : Direction.values()) {
            BlockPos support = spot.offset(side);
            if (!world.getBlockState(support).isSolid()) continue;
            Direction face = side.getOpposite();
            Vec3d hitPos = Vec3d.ofCenter(support).add(
                face.getOffsetX() * 0.5, face.getOffsetY() * 0.5, face.getOffsetZ() * 0.5);
            BlockHitResult hit = new BlockHitResult(hitPos, face, support, false);
            ActionResult result = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
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
