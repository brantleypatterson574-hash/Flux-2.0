package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/** Finds nearby two-block defensive holes and draws them as client-side boxes. */
public class SafeSpotEspModule extends Module implements HudOverlay {
    private record Spot(BlockPos pos, int kind) {}

    private static final int[] SIDE_X = { 1, -1, 0, 0 };
    private static final int[] SIDE_Z = { 0, 0, 1, -1 };

    private final NumberSetting horizontalRange = new NumberSetting("Horizontal Range", 14.0, 4.0, 48.0).description("How far sideways to search for safe bedrock and obsidian holes.");
    private final NumberSetting verticalRange = new NumberSetting("Vertical Range", 3.0, 1.0, 12.0).description("How many blocks above and below to search for holes.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 8.0, 2.0, 60.0).description("Ticks between safety-hole scans.");
    private final NumberSetting maximumSpots = new NumberSetting("Maximum Spots", 48.0, 4.0, 160.0).description("Caps how many safe spots are drawn at once.");
    private final NumberSetting bedrockColor = new NumberSetting("Bedrock Color", 0x67E8F9, 0, 0xFFFFFF).description("Colour of holes made of bedrock.");
    private final NumberSetting obsidianColor = new NumberSetting("Obsidian Color", 0xC084FC, 0, 0xFFFFFF).description("Colour of holes made of obsidian.");
    private final NumberSetting mixedColor = new NumberSetting("Mixed Color", 0xFBBF24, 0, 0xFFFFFF).description("Colour of holes that mix bedrock and obsidian.");
    private final BooleanSetting showDistance = new BooleanSetting("Show Distance", true).description("Shows the distance to each marked safe spot.");
    private final BooleanSetting glow = new BooleanSetting("Glow", true).description("Adds a soft glow around safe-spot outlines.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0).description("Thickness of the safe-spot outline lines.");

    private final List<Spot> spots = new ArrayList<>();
    private final BlockPos.Mutable cursor = new BlockPos.Mutable();
    private int tick;

    public SafeSpotEspModule() {
        super("Safe Spot ESP", "Marks nearby bedrock and obsidian holes. Does not change any blocks.",
            Category.RENDER);
        addSetting(horizontalRange);
        addSetting(verticalRange);
        addSetting(scanInterval);
        addSetting(maximumSpots);
        addSetting(bedrockColor);
        addSetting(obsidianColor);
        addSetting(mixedColor);
        addSetting(showDistance);
        addSetting(glow);
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
        rescan(client);
    }

    private void rescan(MinecraftClient client) {
        spots.clear();
        World world = client.world;
        int hRadius = (int) horizontalRange.getValue();
        int vRadius = (int) verticalRange.getValue();
        int cap = Math.max(1, (int) maximumSpots.getValue());
        int px = client.player.getBlockX();
        int py = client.player.getBlockY();
        int pz = client.player.getBlockZ();

        for (int dx = -hRadius; dx <= hRadius; dx++) {
            for (int dz = -hRadius; dz <= hRadius; dz++) {
                for (int dy = -vRadius; dy <= vRadius; dy++) {
                    int x = px + dx;
                    int y = py + dy;
                    int z = pz + dz;
                    if (!world.getBlockState(cursor.set(x, y, z)).isAir()) continue;
                    if (!world.getBlockState(cursor.set(x, y + 1, z)).isAir()) continue;

                    int kind = classify(world, x, y, z);
                    if (kind < 0) continue;
                    spots.add(new Spot(new BlockPos(x, y, z), kind));
                    if (spots.size() >= cap) return;
                }
            }
        }
    }

    /** Returns 0 for bedrock, 1 for obsidian-like, 2 for mixed, -1 when not enclosed. */
    private int classify(World world, int x, int y, int z) {
        boolean allBedrock = true;
        boolean allObsidian = true;

        for (int side = 0; side < SIDE_X.length; side++) {
            for (int level = 0; level < 2; level++) {
                BlockState state = world.getBlockState(
                    cursor.set(x + SIDE_X[side], y + level, z + SIDE_Z[side]));
                if (state.isAir()) return -1;
                Block block = state.getBlock();
                if (block != Blocks.BEDROCK) allBedrock = false;
                if (!isObsidianLike(block)) allObsidian = false;
            }
        }

        Block bottom = world.getBlockState(cursor.set(x, y - 1, z)).getBlock();
        if (bottom != Blocks.BEDROCK) allBedrock = false;
        if (!isObsidianLike(bottom)) allObsidian = false;

        if (allBedrock) return 0;
        if (allObsidian) return 1;
        return 2;
    }

    private static boolean isObsidianLike(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN || block == Blocks.NETHERITE_BLOCK;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || spots.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int width = Math.max(1, (int) lineWidth.getValue());
        for (Spot spot : spots) {
            BlockPos pos = spot.pos();
            int rgb = switch (spot.kind()) {
                case 0 -> bedrockColor.getColorValue();
                case 1 -> obsidianColor.getColorValue();
                default -> mixedColor.getColorValue();
            };

            OverlayRender.box3DFilled(context,
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0,
                SlEspStyle.argb(235, rgb), SlEspStyle.argb(34, rgb), width, glow.isEnabled());

            if (showDistance.isEnabled()) {
                int[] projected = OverlayRender.project(
                    pos.getX() + 0.5, pos.getY() + 2.2, pos.getZ() + 0.5);
                if (projected != null) {
                    double dx = mc.player.getX() - (pos.getX() + 0.5);
                    double dy = mc.player.getY() - pos.getY();
                    double dz = mc.player.getZ() - (pos.getZ() + 0.5);
                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    OverlayRender.text(context, OverlayRender.meters(distance),
                        projected[0], projected[1] - 2, SlEspStyle.argb(225, rgb));
                }
            }
        }
    }
}
