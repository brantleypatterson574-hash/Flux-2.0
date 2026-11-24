package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Highlights nearby inventory-style block entities with wireframes and optional tracers. */
public class StorageEspModule extends Module implements HudOverlay {
    private record Container(BlockPos pos, double distanceSq) {}

    private final NumberSetting range = new NumberSetting("Range", 36.0, 6.0, 160.0)
        .description("Only containers within this distance are highlighted.");
    private final NumberSetting maxContainers = new NumberSetting("Maximum Containers", 48.0, 4.0, 160.0)
        .description("Caps how many containers can be drawn at once.");
    private final BooleanSetting showTracers = new BooleanSetting("Show Tracers", false)
        .description("Draws a line from your crosshair toward each container.");
    private final BooleanSetting showDistance = new BooleanSetting("Show Distance", true)
        .description("Shows how far away each highlighted container is.");
    private final NumberSetting boxColor = new NumberSetting("Box Color", 0xFBBF24, 0, 0xFFFFFF)
        .description("Colour of the container outline.");
    private final NumberSetting tracerColor = new NumberSetting("Tracer Color", 0x67E8F9, 0, 0xFFFFFF)
        .description("Colour of the optional tracer lines.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of outlines and tracers in screen pixels.");

    private final List<Container> containers = new ArrayList<>();
    private int tick;

    public StorageEspModule() {
        super("Storage ESP", "Finds nearby chests, barrels and similar blocks. Draws on your screen only. Loot radar; opening the chest is still your job.",
            Category.RENDER);
        addSetting(range);
        addSetting(maxContainers);
        addSetting(showTracers);
        addSetting(showDistance);
        addSetting(boxColor);
        addSetting(tracerColor);
        addSetting(lineWidth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        containers.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            containers.clear();
            tick = 0;
            return;
        }
        if (tick++ % 8 != 0) return;
        containers.clear();

        double rSq = range.getValue() * range.getValue();
        for (BlockEntity blockEntity : client.world.getBlockEntities()) {
            if (!isContainer(blockEntity)) continue;
            BlockPos pos = blockEntity.getPos();
            if (pos == null) continue;
            double dx = client.player.getX() - (pos.getX() + 0.5);
            double dy = client.player.getY() - (pos.getY() + 0.5);
            double dz = client.player.getZ() - (pos.getZ() + 0.5);
            double distanceSq = dx * dx + dy * dy + dz * dz;
            if (distanceSq > rSq) continue;
            containers.add(new Container(pos, distanceSq));
        }

        containers.sort(Comparator.comparingDouble(Container::distanceSq));
        int cap = Math.max(1, (int) maxContainers.getValue());
        if (containers.size() > cap) {
            containers.subList(cap, containers.size()).clear();
        }
    }

    /** Internal type filter only - no user-facing checklist needed for this overlay. */
    private static boolean isContainer(BlockEntity blockEntity) {
        BlockEntityType<?> type = blockEntity.getType();
        return type == BlockEntityType.CHEST
            || type == BlockEntityType.TRAPPED_CHEST
            || type == BlockEntityType.ENDER_CHEST
            || type == BlockEntityType.BARREL
            || type == BlockEntityType.SHULKER_BOX
            || type == BlockEntityType.HOPPER
            || type == BlockEntityType.DISPENSER
            || type == BlockEntityType.DROPPER
            || type == BlockEntityType.FURNACE
            || type == BlockEntityType.BLAST_FURNACE
            || type == BlockEntityType.SMOKER
            || type == BlockEntityType.BREWING_STAND
            || type == BlockEntityType.DECORATED_POT
            || type == BlockEntityType.CRAFTER
            || type == BlockEntityType.CHISELED_BOOKSHELF
            || type == BlockEntityType.VAULT;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || containers.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int thickness = Math.max(1, (int) lineWidth.getValue());
        int fillRgb = boxColor.getColorValue();
        int traceRgb = tracerColor.getColorValue();
        int centerX = mc.getWindow().getScaledWidth() / 2;
        int centerY = mc.getWindow().getScaledHeight() / 2;
        double rSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);
        boolean wantTracer = showTracers.isEnabled();
        boolean wantDistance = showDistance.isEnabled();

        for (Container container : containers) {
            BlockPos pos = container.pos();
            double dx = mc.player.getX() - (pos.getX() + 0.5);
            double dy = mc.player.getY() - (pos.getY() + 0.5);
            double dz = mc.player.getZ() - (pos.getZ() + 0.5);
            double distanceSq = dx * dx + dy * dy + dz * dz;
            if (distanceSq > rSq) continue;

            OverlayRender.box3DFilled(context,
                pos.getX() + 0.03, pos.getY() + 0.03, pos.getZ() + 0.03,
                pos.getX() + 0.97, pos.getY() + 0.97, pos.getZ() + 0.97,
                SlEspStyle.argb(230, fillRgb), SlEspStyle.argb(26, fillRgb), thickness, false);

            if (!wantTracer && !wantDistance) continue;
            int[] projected = OverlayRender.project(
                pos.getX() + 0.5, pos.getY() + 1.15, pos.getZ() + 0.5);
            if (projected == null) continue;

            if (wantTracer) {
                OverlayRender.line(context, centerX, centerY, projected[0], projected[1],
                    SlEspStyle.argb(180, traceRgb), Math.max(1, thickness - 1));
            }
            if (wantDistance) {
                OverlayRender.text(context, OverlayRender.meters(Math.sqrt(distanceSq)),
                    projected[0], projected[1] - 10, SlEspStyle.argb(235, fillRgb));
            }
        }
    }
}
