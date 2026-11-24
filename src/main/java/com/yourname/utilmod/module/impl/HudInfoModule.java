package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Configurable stats sidebar: FPS, coordinates, biome, speed, ping, facing and
 * memory. Lines are rebuilt on a tick interval instead of every frame so the
 * HUD stays cheap. Purely client-side, reads local state only.
 */
public class HudInfoModule extends Module implements HudOverlay {
    private final BooleanSetting showFps = new BooleanSetting("Show FPS", true).description("Shows your current frames per second.");
    private final BooleanSetting showCoords = new BooleanSetting("Show Coordinates", true).description("Shows your X, Y and Z position.");
    private final BooleanSetting showBiome = new BooleanSetting("Show Biome", true).description("Shows the biome you are in.");
    private final BooleanSetting showSpeed = new BooleanSetting("Show Speed", true).description("Shows how fast you are moving.");
    private final BooleanSetting showPing = new BooleanSetting("Show Ping", true).description("Shows your connection latency.");
    private final BooleanSetting showFacing = new BooleanSetting("Show Facing", false).description("Shows the direction you are facing.");
    private final BooleanSetting showMemory = new BooleanSetting("Show Memory", false).description("Shows Java memory use.");
    private final NumberSetting x = new NumberSetting("X", 4.0, 0.0, 1000.0).advanced().description("Horizontal position of the stats panel on screen.");
    private final NumberSetting y = new NumberSetting("Y", 4.0, 0.0, 1000.0).advanced().description("Vertical position of the stats panel on screen.");
    private final NumberSetting spacing = new NumberSetting("Line Spacing", 11.0, 8.0, 24.0).description("Pixels between lines in the stats panel.");
    private final NumberSetting interval = new NumberSetting("Update Interval", 10.0, 1.0, 60.0)
        .description("Ticks between stat refreshes. Higher is cheaper.");
    private final BooleanSetting background = new BooleanSetting("Background", true).description("Draws a shaded panel behind the stats text.");
    private final NumberSetting color = new NumberSetting("Color", 0xFFFFFF, 0x000000, 0xFFFFFF).description("Colour of the stats text.");
    private final NumberSetting accent = new NumberSetting("Accent", 0x78B4FF, 0x000000, 0xFFFFFF).description("Accent colour for highlighted values in the stats panel.");

    private final List<String> cachedLines = new ArrayList<>();
    private int cachedWidth;
    private int tickCounter;

    public HudInfoModule() {
        super("Stats HUD", "Configurable FPS, coordinates, biome, speed, ping and memory overlay.",
            Category.HUD);
        addSetting(showFps);
        addSetting(showCoords);
        addSetting(showBiome);
        addSetting(showSpeed);
        addSetting(showPing);
        addSetting(showFacing);
        addSetting(showMemory);
        addSetting(x);
        addSetting(y);
        addSetting(spacing);
        addSetting(interval);
        addSetting(background);
        addSetting(color);
        addSetting(accent);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        if (++tickCounter < (int) interval.getValue()) return;
        tickCounter = 0;
        cachedLines.clear();

        if (showFps.isEnabled()) cachedLines.add("FPS  " + client.getCurrentFps());
        if (showCoords.isEnabled()) {
            var pos = client.player.getBlockPos();
            cachedLines.add("XYZ  " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ());
        }
        if (showBiome.isEnabled() && client.world != null) {
            String biome = client.world.getBiome(client.player.getBlockPos())
                .getKey().map(key -> key.getValue().getPath()).orElse("unknown");
            cachedLines.add("Biome  " + biome);
        }
        if (showSpeed.isEnabled()) {
            Vec3d velocity = client.player.getVelocity();
            double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z) * 20.0;
            cachedLines.add(String.format("Speed  %.1f b/s", speed));
        }
        if (showPing.isEnabled() && client.player.networkHandler != null) {
            PlayerListEntry entry = client.player.networkHandler
                .getPlayerListEntry(client.player.getUuid());
            cachedLines.add("Ping  " + (entry == null ? "?" : entry.getLatency() + " ms"));
        }
        if (showFacing.isEnabled()) {
            cachedLines.add(String.format("Facing  %.0f / %.0f",
                client.player.getYaw(), client.player.getPitch()));
        }
        if (showMemory.isEnabled()) {
            Runtime runtime = Runtime.getRuntime();
            long usedMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
            long maxMb = runtime.maxMemory() / (1024L * 1024L);
            cachedLines.add("RAM  " + usedMb + " / " + maxMb + " MB");
        }

        cachedWidth = 0;
        for (String line : cachedLines) {
            cachedWidth = Math.max(cachedWidth, client.textRenderer.getWidth(line));
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden || cachedLines.isEmpty()) return;
        int startX = (int) x.getValue();
        int startY = (int) y.getValue();
        int step = Math.max(8, (int) spacing.getValue());
        int rgb = 0xFF000000 | ((int) color.getValue() & 0xFFFFFF);

        if (background.isEnabled()) {
            int width = cachedWidth;
            context.fill(startX - 4, startY - 4, startX + width + 6,
                startY + cachedLines.size() * step + 2, 0x990C0D12);
            context.fill(startX - 4, startY - 4, startX - 2,
                startY + cachedLines.size() * step + 2,
                0xFF000000 | ((int) accent.getValue() & 0xFFFFFF));
        }
        for (int i = 0; i < cachedLines.size(); i++) {
            OverlayRender.text(context, cachedLines.get(i), startX, startY + i * step, rgb);
        }
    }
}
