package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.NotificationManager;
import com.yourname.utilmod.ui.SlGuiPaint;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

/** Lightweight structural scanner that flags dense clusters of base-like blocks. */
public class BaseFinderModule extends Module implements HudOverlay {
    private final NumberSetting radius = new NumberSetting("Radius", 16.0, 6.0, 48.0).description("How far around you the scanner checks for base-like blocks.");
    private final NumberSetting interval = new NumberSetting("Scan Interval", 40.0, 10.0, 200.0).description("Ticks between background scans.");
    private final NumberSetting minimum = new NumberSetting("Minimum Signals", 4.0, 2.0, 20.0).description("Number of base-like blocks needed before a cluster is reported.");
    private final BooleanSetting showHud = new BooleanSetting("Show HUD", true).description("Shows the signal count and found position on the HUD.");
    private final NumberSetting x = new NumberSetting("X", 6.0, 0.0, 1000.0).description("Horizontal position of the scanner readout on screen.");
    private final NumberSetting y = new NumberSetting("Y", 96.0, 0.0, 1000.0).description("Vertical position of the scanner readout on screen.");

    private int scans;
    private int signalCount;
    private BlockPos signalPos;
    private long lastNotice;

    public BaseFinderModule() {
        super("Base Finder",
            "Scans nearby blocks for player-built structures and reports dense clusters.",
            Category.DONUT);
        addSetting(radius);
        addSetting(interval);
        addSetting(minimum);
        addSetting(showHud);
        addSetting(x);
        addSetting(y);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        scans = 0;
        signalCount = 0;
        signalPos = null;
        lastNotice = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (++scans < interval.getValue()) return;
        scans = 0;

        int r = (int) radius.getValue();
        int found = 0;
        BlockPos first = null;
        BlockPos center = client.player.getBlockPos();
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -8; dy <= 16; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    if (isSignal(client, pos)) {
                        found++;
                        if (first == null) first = pos;
                    }
                }
            }
        }

        boolean nowFound = found >= minimum.getValue();
        signalCount = found;
        signalPos = nowFound ? first : null;

        if (nowFound && System.currentTimeMillis() - lastNotice > 12_000L) {
            lastNotice = System.currentTimeMillis();
            NotificationManager.info("Base Finder", found + " structural signals nearby");
        }
    }

    private static boolean isSignal(MinecraftClient client, BlockPos pos) {
        var state = client.world.getBlockState(pos);
        return state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CHEST)
            || state.isOf(Blocks.FURNACE) || state.isOf(Blocks.CRAFTING_TABLE)
            || state.isOf(Blocks.GLASS) || state.isOf(Blocks.LANTERN);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || !showHud.isEnabled() || mc.player == null) return;
        String text = signalPos == null
            ? "BASE FINDER  " + signalCount + " signals"
            : "BASE FOUND  " + signalCount + " signals  @ " + signalPos.getX()
                + ", " + signalPos.getY() + ", " + signalPos.getZ();
        SlGuiPaint.text(context, mc.textRenderer, text,
            (int) x.getValue(), (int) y.getValue(), 0xFFFF72B6, 9.0f);
    }
}
