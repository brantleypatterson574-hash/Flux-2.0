package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.EspOptionLists;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Personal Xray: a client-side overlay that highlights chosen blocks through
 * terrain. It never rewrites rendered chunks or sends anything to the server,
 * which keeps it safe on the private server and cheap to toggle.
 *
 * <p>The scan is split one Y layer per tick so a large range never causes a
 * frame hitch. Found positions are packed into primitive arrays and reused,
 * and the 3D box projection reuses OverlayRender's scratch buffers.</p>
 */
public class XrayModule extends Module implements HudOverlay {
    private final ChecklistSetting blocks = new ChecklistSetting(
        "Blocks", "xray.blocks", EspOptionLists.blockOptions(), EspOptionLists.defaultBlockIds()).description("Which block types are highlighted through terrain.");
    private final NumberSetting range = new NumberSetting("Range", 40.0, 8.0, 96.0)
        .description("Horizontal and vertical scan radius in blocks.");
    private final NumberSetting maxBlocks = new NumberSetting("Maximum Blocks", 160.0, 16.0, 512.0)
        .description("Cap on highlighted blocks so crowded caves stay readable.");
    private final BooleanSetting exposedOnly = new BooleanSetting("Exposed Only", false)
        .description("Only highlight blocks touching air, so buried ore seams are hidden.");
    private final BooleanSetting fill = new BooleanSetting("Fill", true).description("Fills found blocks with a translucent colour.");
    private final NumberSetting opacity = new NumberSetting("Opacity", 110.0, 0.0, 200.0)
        .description("Transparency of each highlighted block's body.");
    private final BooleanSetting outline = new BooleanSetting("Outline", true).description("Draws outlines around found blocks.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 1.5, 1.0, 4.0).description("Thickness of block outlines and tracer lines.");
    private final BooleanSetting glow = new BooleanSetting("Glow", true).description("Adds a soft glow around found blocks.");
    private final NumberSetting color = new NumberSetting("Color", 0x55FFFF, 0x000000, 0xFFFFFF)
        .description("Highlight colour shared by every selected block.");
    private final BooleanSetting tracers = new BooleanSetting("Tracers", false).description("Draws lines from your crosshair to found blocks.");
    private final BooleanSetting names = new BooleanSetting("Show Names", true).description("Shows the block name on each highlight.");
    private final BooleanSetting distance = new BooleanSetting("Show Distance", true).description("Shows how far away each found block is.");

    // Packed block positions, reused every scan. Rendering happens on one thread.
    private int[] found = new int[3 * 512];
    private int foundCount;
    private int scanY;
    private int lastRange = -1;
    private int lastCenterX = Integer.MIN_VALUE;
    private int lastCenterZ = Integer.MIN_VALUE;

    private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();
    private final int[] tracerStart = new int[3];
    private final int[] tracerEnd = new int[3];

    public XrayModule() {
        super("Xray", "Highlights selected blocks through walls. Personal, client-side overlay.",
            Category.HUD);
        addSetting(blocks);
        addSetting(range);
        addSetting(maxBlocks);
        addSetting(exposedOnly);
        addSetting(fill);
        addSetting(opacity);
        addSetting(outline);
        addSetting(lineWidth);
        addSetting(glow);
        addSetting(color);
        addSetting(tracers);
        addSetting(names);
        addSetting(distance);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        foundCount = 0;
        scanY = 0;
        lastCenterX = Integer.MIN_VALUE;
    }

    @Override
    public void onDisable() {
        foundCount = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        int r = (int) Math.round(range.getValue());
        int centerX = client.player.getBlockX();
        int centerY = client.player.getBlockY();
        int centerZ = client.player.getBlockZ();
        if (centerX != lastCenterX || centerZ != lastCenterZ || r != lastRange) {
            lastCenterX = centerX;
            lastCenterZ = centerZ;
            lastRange = r;
            scanY = centerY - r;
            foundCount = 0;
        }

        int bottom = centerY - r;
        int top = centerY + r;
        // Restart when the player moves vertically out of the scan window.
        // Without this, descending leaves stale layers above the new range and
        // ascending can leave the cursor below it until it naturally wraps.
        if (scanY < bottom || scanY > top) {
            scanY = bottom;
            foundCount = 0;
        }
        scanLayer(client.world, centerX, scanY, centerZ, r);
        scanY++;
    }

    private void scanLayer(World world, int centerX, int y, int centerZ, int r) {
        Set<String> wanted = blocks.selectedView();
        if (wanted.isEmpty()) return;

        int cap = (int) Math.round(maxBlocks.getValue());
        int capacity = cap * 3;
        if (found.length < capacity) found = new int[capacity];

        for (int dx = -r; dx <= r; dx++) {
            int x = centerX + dx;
            for (int dz = -r; dz <= r; dz++) {
                int z = centerZ + dz;
                BlockState state = world.getBlockState(mutablePos.set(x, y, z));
                if (state.isAir() || !wanted.contains(EspOptionLists.blockId(state.getBlock()))) continue;
                if (exposedOnly.isEnabled() && !isExposed(world, x, y, z)) continue;
                if (foundCount >= cap) return;
                int i = foundCount * 3;
                found[i] = x;
                found[i + 1] = y;
                found[i + 2] = z;
                foundCount++;
            }
        }
    }

    private boolean isExposed(World world, int x, int y, int z) {
        return world.getBlockState(mutablePos.set(x + 1, y, z)).isAir()
            || world.getBlockState(mutablePos.set(x - 1, y, z)).isAir()
            || world.getBlockState(mutablePos.set(x, y + 1, z)).isAir()
            || world.getBlockState(mutablePos.set(x, y - 1, z)).isAir()
            || world.getBlockState(mutablePos.set(x, y, z + 1)).isAir()
            || world.getBlockState(mutablePos.set(x, y, z - 1)).isAir();
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || foundCount == 0 || mc.player == null) return;

        int rgb = ((int) color.getValue()) & 0xFFFFFF;
        int alpha = (int) Math.max(0, Math.min(255, opacity.getValue()));
        int outlineColor = 0xFF000000 | rgb;
        int fillColor = fill.isEnabled() && alpha > 0 ? (alpha << 24) | rgb : 0;
        int thickness = Math.max(1, (int) Math.round(lineWidth.getValue()));
        boolean drawOutline = outline.isEnabled();
        boolean drawGlow = glow.isEnabled();

        for (int i = 0; i < foundCount; i++) {
            int index = i * 3;
            int x = found[index];
            int y = found[index + 1];
            int z = found[index + 2];

            if (drawOutline) {
                OverlayRender.box3DFilled(context,
                    x - 0.01, y - 0.01, z - 0.01,
                    x + 1.01, y + 1.01, z + 1.01,
                    outlineColor, fillColor, thickness, drawGlow);
            } else if (fillColor != 0) {
                OverlayRender.box3DFilled(context,
                    x - 0.01, y - 0.01, z - 0.01,
                    x + 1.01, y + 1.01, z + 1.01,
                    0, fillColor, thickness, false);
            }

            if (!tracers.isEnabled() && !names.isEnabled() && !distance.isEnabled()) continue;
            if (!OverlayRender.projectInto(x + 0.5, y + 0.5, z + 0.5, tracerEnd)) continue;

            if (tracers.isEnabled()
                && OverlayRender.projectInto(mc.player.getX(), mc.player.getY() + 1.62,
                    mc.player.getZ(), tracerStart)) {
                OverlayRender.line(context, tracerStart[0], tracerStart[1],
                    tracerEnd[0], tracerEnd[1], outlineColor, 1);
            }

            if (names.isEnabled() || distance.isEnabled()) {
                String label = null;
                if (names.isEnabled()) {
                    BlockState state = mc.world.getBlockState(mutablePos.set(x, y, z));
                    label = state.getBlock().getName().getString();
                }
                if (distance.isEnabled()) {
                    String dist = OverlayRender.meters(mc.player.getEyePos().distanceTo(
                        new net.minecraft.util.math.Vec3d(x + 0.5, y + 0.5, z + 0.5)));
                    label = label == null ? dist : label + "  " + dist;
                }
                if (label != null) {
                    OverlayRender.text(context, label, tracerEnd[0] + 4, tracerEnd[1] - 8,
                        0xFF000000 | rgb);
                }
            }
        }
    }
}
