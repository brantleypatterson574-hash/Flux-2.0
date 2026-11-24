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

/** Finds open air columns with nothing solid beneath them and outlines the edge. */
public class VoidEspModule extends Module implements HudOverlay {
    private final NumberSetting radius = new NumberSetting("Radius", 16.0, 4.0, 64.0)
        .description("How far around you to look for open holes and void edges.");
    private final NumberSetting depthCheck = new NumberSetting("Depth Check", 12.0, 2.0, 48.0)
        .description("How far down the check looks for solid ground below a gap.");
    private final NumberSetting maxSpots = new NumberSetting("Maximum Spots", 48.0, 4.0, 160.0)
        .description("Caps how many gap markers can be drawn at once.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 8.0, 2.0, 60.0)
        .description("Ticks between scans. Existing markers stay visible between scans.");
    private final NumberSetting color = new NumberSetting("Color", 0xA3E635, 0, 0xFFFFFF)
        .description("Colour of the gap outlines.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of each outline in screen pixels.");

    private final List<BlockPos> spots = new ArrayList<>();
    private final BlockPos.Mutable cursor = new BlockPos.Mutable();
    private int tick;

    public VoidEspModule() {
        super("Void ESP", "Marks nearby holes and open void edges. Only draws an overlay. Holes are just floors with confidence issues.",
            Category.RENDER);
        addSetting(radius);
        addSetting(depthCheck);
        addSetting(maxSpots);
        addSetting(scanInterval);
        addSetting(color);
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
            tick = 0;
            return;
        }
        if (tick++ % Math.max(2, (int) scanInterval.getValue()) != 0) return;
        spots.clear();

        World world = client.world;
        int r = (int) radius.getValue();
        int depth = Math.max(1, (int) depthCheck.getValue());
        int cap = Math.max(1, (int) maxSpots.getValue());
        int px = client.player.getBlockX();
        int py = client.player.getBlockY();
        int pz = client.player.getBlockZ();
        int rSq = r * r;

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx * dx + dz * dz > rSq) continue;
                int x = px + dx;
                int z = pz + dz;
                if (!world.getBlockState(cursor.set(x, py, z)).isAir()) continue;

                boolean grounded = false;
                for (int d = 1; d <= depth; d++) {
                    cursor.set(x, py - d, z);
                    if (world.getBlockState(cursor).isSolidBlock(world, cursor)) {
                        grounded = true;
                        break;
                    }
                }
                if (grounded) continue;

                spots.add(new BlockPos(x, py, z));
                if (spots.size() >= cap) return;
            }
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || spots.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int thickness = Math.max(1, (int) lineWidth.getValue());
        double rSq = (radius.getValue() + 2.0) * (radius.getValue() + 2.0);

        for (BlockPos pos : spots) {
            double dx = mc.player.getX() - (pos.getX() + 0.5);
            double dy = mc.player.getY() - (pos.getY() + 0.5);
            double dz = mc.player.getZ() - (pos.getZ() + 0.5);
            if (dx * dx + dy * dy + dz * dz > rSq) continue;

            OverlayRender.box3DFilled(context,
                pos.getX() + 0.04, pos.getY() + 0.02, pos.getZ() + 0.04,
                pos.getX() + 0.96, pos.getY() + 0.98, pos.getZ() + 0.96,
                SlEspStyle.argb(230, rgb), SlEspStyle.argb(22, rgb), thickness, false);
        }
    }
}
