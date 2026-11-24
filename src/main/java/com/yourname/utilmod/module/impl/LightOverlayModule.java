package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Marks nearby solid block tops with a small cross. The colour reports whether
 * the space above is bright enough to be safe. Only the HUD is affected; no
 * block, light source or world state is ever changed.
 */
public class LightOverlayModule extends Module implements HudOverlay {
    private record Spot(BlockPos pos, boolean safe) {}

    private final NumberSetting radius = new NumberSetting("Radius", 12.0, 3.0, 40.0)
        .description("How many blocks around you are checked for marker spots.");
    private final NumberSetting maximumSpots = new NumberSetting("Maximum Spots", 80.0, 4.0, 250.0)
        .description("Caps the number of crosses drawn at once to keep the HUD light.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 8.0, 2.0, 60.0)
        .description("Ticks between scans. Higher values use less CPU.");
    private final NumberSetting safeThreshold = new NumberSetting("Safe Threshold", 8.0, 0.0, 15.0)
        .description("Light level at or above which a spot counts as safe.");
    private final NumberSetting safeColor = new NumberSetting("Safe Color", 0x4ADE80, 0, 0xFFFFFF)
        .description("Cross colour for spots at or above the safe threshold.");
    private final NumberSetting unsafeColor = new NumberSetting("Unsafe Color", 0xFB7185, 0, 0xFFFFFF)
        .description("Cross colour for spots below the safe threshold.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of each cross stroke in pixels.");

    private final List<Spot> spots = new ArrayList<>();
    private final BlockPos.Mutable cursor = new BlockPos.Mutable();
    private final BlockPos.Mutable aboveCursor = new BlockPos.Mutable();
    private int tick;

    public LightOverlayModule() {
        super("Light Overlay", "Shows safe and unsafe light levels as crosses on nearby block tops. Visual only. Tiny crosses with strong opinions.",
            Category.RENDER);
        addSetting(radius);
        addSetting(maximumSpots);
        addSetting(scanInterval);
        addSetting(safeThreshold);
        addSetting(safeColor);
        addSetting(unsafeColor);
        addSetting(lineWidth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        spots.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            spots.clear();
            return;
        }
        if (tick++ % Math.max(2, (int) scanInterval.getValue()) != 0) return;
        rescan(client.world, client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ());
    }

    private void rescan(World world, int px, int py, int pz) {
        spots.clear();
        int r = (int) radius.getValue();
        int cap = Math.max(1, (int) maximumSpots.getValue());
        int threshold = (int) safeThreshold.getValue();

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                for (int dy = -2; dy <= 2; dy++) {
                    int x = px + dx;
                    int y = py + dy;
                    int z = pz + dz;
                    if (world.getBlockState(cursor.set(x, y, z)).isAir()) continue;
                    if (!world.getBlockState(aboveCursor.set(x, y + 1, z)).isAir()) continue;

                    int light = world.getLightLevel(aboveCursor);
                    spots.add(new Spot(new BlockPos(x, y, z), light >= threshold));
                    if (spots.size() >= cap) return;
                }
            }
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || spots.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int[] left = new int[3];
        int[] right = new int[3];
        int thickness = Math.max(1, (int) lineWidth.getValue());
        for (Spot spot : spots) {
            BlockPos pos = spot.pos();
            int rgb = spot.safe() ? safeColor.getColorValue() : unsafeColor.getColorValue();
            int color = SlEspStyle.argb(235, rgb);
            double top = pos.getY() + 1.02;
            double cx = pos.getX() + 0.5;
            double cz = pos.getZ() + 0.5;
            double arm = 0.32;

            drawCrossStroke(context, left, right, thickness, color,
                cx - arm, top, cz - arm, cx + arm, top, cz + arm);
            drawCrossStroke(context, left, right, thickness, color,
                cx - arm, top, cz + arm, cx + arm, top, cz - arm);
        }
    }

    /** Projects both ends of one diagonal stroke and draws it when visible. */
    private static void drawCrossStroke(DrawContext context, int[] a, int[] b, int thickness,
                                        int color, double x1, double y1, double z1,
                                        double x2, double y2, double z2) {
        if (OverlayRender.projectInto(x1, y1, z1, a) && OverlayRender.projectInto(x2, y2, z2, b)) {
            OverlayRender.line(context, a[0], a[1], b[0], b[1], color, thickness);
        }
    }
}
