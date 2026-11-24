package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.PlacementUtil;
import com.yourname.utilmod.util.RotationUtil;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BedBlock;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Watches for loaded beds close to you and marks them in the HUD. When Warn
 * Only is off it also tries to cover the bed with a block from your hotbar.
 * That placement really changes the world, so use it only on a private server
 * running the matching jar; the server still validates the placement.
 */
public class AntiBedModule extends Module implements HudOverlay {
    private final NumberSetting range = new NumberSetting("Range", 12.0, 3.0, 48.0)
        .description("How far away a bed can be before it is reported.");
    private final BooleanSetting warnOnly = new BooleanSetting("Warn Only", true)
        .description("Only draws the warning. Turn off to also place a block over the bed.");
    private final NumberSetting placeBlockDelay = new NumberSetting("Place Block Delay", 6.0, 1.0, 40.0)
        .description("Ticks between block placements when Warn Only is off.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Faces the bed briefly before placing, then restores your view.");
    private final NumberSetting color = new NumberSetting("Color", 0xFB7185, 0, 0xFFFFFF)
        .description("Colour of the bed warning box and label.");

    private final List<BlockPos> beds = new ArrayList<>();
    private long nextPlace;
    private int originalSlot = -1;

    public AntiBedModule() {
        super("Anti Bed", "Warns about nearby beds and can cover them with a block. Placement changes blocks - private server only.",
            Category.CRYSTAL);
        addSetting(range);
        addSetting(warnOnly);
        addSetting(placeBlockDelay);
        addSetting(silentRotate);
        addSetting(color);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        beds.clear();
        nextPlace = 0L;
        restoreSlot();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null) {
            beds.clear();
            return;
        }

        beds.clear();
        for (BlockEntity entity : client.world.getBlockEntities()) {
            if (!(entity instanceof BedBlockEntity)) continue;
            BlockPos pos = entity.getPos();
            if (pos == null || Vec3d.ofCenter(pos).distanceTo(client.player.getEyePos()) > range.getValue()) continue;
            // The two halves of a bed share one entity layout; keep only the foot.
            var state = client.world.getBlockState(pos);
            if (state.getBlock() instanceof BedBlock && state.get(BedBlock.PART) != BedPart.FOOT) continue;
            beds.add(pos);
        }

        if (warnOnly.isEnabled() || beds.isEmpty()) return;
        long now = client.world.getTime();
        if (now < nextPlace) return;

        int slot = ItemUtil.findSlot(client.player, Items.OBSIDIAN, true, false);
        if (slot < 0) slot = ItemUtil.findSlot(client.player, Items.COBWEB, true, false);
        if (slot < 0) slot = ItemUtil.findSlot(client.player, stack -> stack.getItem() instanceof BlockItem, true, false);
        if (slot < 0) return;
        if (originalSlot < 0 && client.player.getInventory().getSelectedSlot() != slot) {
            originalSlot = client.player.getInventory().getSelectedSlot();
        }
        if (client.player.getInventory().getSelectedSlot() != slot) ItemUtil.selectSlot(client.player, slot);

        BlockPos spot = beds.get(0).up();
        if (!client.world.getBlockState(spot).isAir()) return;
        float[] previousView = silentRotate.isEnabled()
            ? RotationUtil.face(client, Vec3d.ofCenter(spot)) : null;
        boolean placed = PlacementUtil.placeInAir(client, spot, Hand.MAIN_HAND);
        if (previousView != null) RotationUtil.restore(client, previousView);
        if (placed) nextPlace = now + (long) placeBlockDelay.getValue();
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || beds.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        for (BlockPos pos : beds) {
            OverlayRender.box3DFilled(context,
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1.0, pos.getY() + 0.6, pos.getZ() + 1.0,
                SlEspStyle.argb(225, rgb), SlEspStyle.argb(30, rgb), 2, false);
            int[] projected = OverlayRender.project(pos.getX() + 0.5, pos.getY() + 1.15, pos.getZ() + 0.5);
            if (projected != null) {
                OverlayRender.text(context, "BED NEARBY", projected[0] - 30, projected[1] - 6,
                    SlEspStyle.argb(240, rgb));
            }
        }
    }

    private void restoreSlot() {
        if (originalSlot >= 0 && mc.player != null) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
