package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Marks solid blocks around other players that could be mined out from under them. */
public class CityEspModule extends Module implements HudOverlay {
    private final NumberSetting range = new NumberSetting("Range", 40.0, 8.0, 160.0)
        .description("Only nearby players within this distance are checked.");
    private final NumberSetting maxMarks = new NumberSetting("Maximum Marks", 48.0, 4.0, 160.0)
        .description("Caps how many block outlines can be drawn at once.");
    private final NumberSetting color = new NumberSetting("Color", 0xFB7185, 0, 0xFFFFFF)
        .description("Colour of the block outlines.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of each outline in screen pixels.");

    private final List<BlockPos> marks = new ArrayList<>();
    private int tick;

    public CityEspModule() {
        super("City ESP", "Outlines mineable blocks next to nearby players. Never places or breaks blocks. No blocks were harmed in the making of this overlay.",
            Category.RENDER);
        addSetting(range);
        addSetting(maxMarks);
        addSetting(color);
        addSetting(lineWidth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        marks.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            marks.clear();
            tick = 0;
            return;
        }
        if (tick++ % 8 != 0) return;
        marks.clear();

        double r = range.getValue();
        double rSq = r * r;
        Box area = new Box(
            client.player.getX() - r, client.player.getY() - 8.0, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + 8.0, client.player.getZ() + r
        );
        List<Entity> players = client.world.getOtherEntities(client.player, area,
            entity -> entity instanceof PlayerEntity && !entity.isRemoved() && !entity.isSpectator()
                && client.player.squaredDistanceTo(entity) <= rSq);
        if (players.isEmpty()) return;

        int cap = Math.max(1, (int) maxMarks.getValue());
        Set<BlockPos> seen = new HashSet<>();
        for (Entity entity : players) {
            int px = entity.getBlockX();
            int py = entity.getBlockY();
            int pz = entity.getBlockZ();

            // Feet level and the block above it: the ring around a standing player.
            for (int level = 0; level <= 1; level++) {
                int y = py + level;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dz == 0) continue;
                        BlockPos pos = new BlockPos(px + dx, y, pz + dz);
                        if (!seen.add(pos)) continue;
                        BlockState state = client.world.getBlockState(pos);
                        if (state.isAir() || state.isOf(Blocks.BEDROCK) || state.isOf(Blocks.BARRIER)) {
                            continue;
                        }
                        marks.add(pos);
                        if (marks.size() >= cap) return;
                    }
                }
            }
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || marks.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int thickness = Math.max(1, (int) lineWidth.getValue());
        double rSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);

        for (BlockPos pos : marks) {
            double dx = mc.player.getX() - (pos.getX() + 0.5);
            double dy = mc.player.getY() - (pos.getY() + 0.5);
            double dz = mc.player.getZ() - (pos.getZ() + 0.5);
            if (dx * dx + dy * dy + dz * dz > rSq) continue;

            OverlayRender.box3DFilled(context,
                pos.getX() + 0.03, pos.getY() + 0.03, pos.getZ() + 0.03,
                pos.getX() + 0.97, pos.getY() + 0.97, pos.getZ() + 0.97,
                SlEspStyle.argb(230, rgb), SlEspStyle.argb(24, rgb), thickness, false);
        }
    }
}
