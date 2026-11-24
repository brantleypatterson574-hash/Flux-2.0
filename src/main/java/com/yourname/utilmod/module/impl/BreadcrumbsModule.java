package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;

import java.util.ArrayDeque;

/**
 * Leaves a short, fading record of where the local player has walked.
 * Everything is sampled from the client and drawn on the HUD; no packets are
 * sent and nothing in the world is changed.
 */
public class BreadcrumbsModule extends Module implements HudOverlay {
    private static final int SECTION_LENGTH = 24;
    private static final int MAX_SECTIONS = 8;
    private static final long FADE_TIME_MS = 8000L;

    private static final class Crumb {
        final double x;
        final double y;
        final double z;
        final long recordedAt;

        Crumb(double x, double y, double z, long recordedAt) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.recordedAt = recordedAt;
        }
    }

    private final NumberSetting maximumPoints = new NumberSetting("Maximum Points", 96.0, 12.0, 240.0)
        .description("How many recent footsteps to keep before the oldest ones disappear.");
    private final NumberSetting recordInterval = new NumberSetting("Record Interval", 3.0, 1.0, 20.0)
        .description("Ticks between recorded footsteps; lower values make a smoother trail.");
    private final NumberSetting color = new NumberSetting("Color", 0x7DD3FC, 0x000000, 0xFFFFFF)
        .description("Colour of the breadcrumb trail.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Thickness of the trail in screen pixels.");
    private final BooleanSetting fade = new BooleanSetting("Fade", true)
        .description("Older breadcrumbs gradually become more transparent.");

    private final ArrayDeque<ArrayDeque<Crumb>> sections = new ArrayDeque<>();
    private ClientWorld lastWorld;
    private int crumbCount;
    private int tick;

    public BreadcrumbsModule() {
        super("Breadcrumbs", "Draws a fading trail of your recent footsteps. Visual only. Hansel and Gretel, but local.",
            Category.RENDER);
        addSetting(maximumPoints);
        addSetting(recordInterval);
        addSetting(color);
        addSetting(lineWidth);
        addSetting(fade);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        clearState();
    }

    private void clearState() {
        sections.clear();
        crumbCount = 0;
        tick = 0;
        lastWorld = null;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            clearState();
            return;
        }
        if (client.world != lastWorld) {
            clearState();
            lastWorld = client.world;
        }
        if (tick++ % Math.max(1, (int) recordInterval.getValue()) != 0) return;

        ArrayDeque<Crumb> current = sections.peekLast();
        if (current == null || current.size() >= SECTION_LENGTH) {
            current = new ArrayDeque<>();
            sections.addLast(current);
            while (sections.size() > MAX_SECTIONS) sections.removeFirst();
        }
        current.addLast(new Crumb(client.player.getX(), client.player.getY() + 0.08,
            client.player.getZ(), System.currentTimeMillis()));
        crumbCount++;
        trimToBudget();
    }

    /** Keeps both the per-section layout and the overall point budget bounded. */
    private void trimToBudget() {
        int budget = Math.max(2, (int) maximumPoints.getValue());
        while (crumbCount > budget && !sections.isEmpty()) {
            ArrayDeque<Crumb> oldest = sections.peekFirst();
            if (oldest.isEmpty()) {
                sections.removeFirst();
                continue;
            }
            oldest.removeFirst();
            crumbCount--;
            if (oldest.isEmpty()) sections.removeFirst();
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || crumbCount < 2 || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int width = Math.max(1, (int) lineWidth.getValue());
        long now = System.currentTimeMillis();
        int[] previous = new int[3];
        int[] current = new int[3];

        for (ArrayDeque<Crumb> section : sections) {
            boolean hasPrevious = false;
            Crumb previousCrumb = null;
            for (Crumb crumb : section) {
                boolean visible = OverlayRender.projectInto(crumb.x, crumb.y, crumb.z, current);
                if (visible && hasPrevious && previousCrumb != null) {
                    float remaining = 1.0f;
                    if (fade.isEnabled()) {
                        long age = Math.max(0L, now - crumb.recordedAt);
                        remaining = 1.0f - Math.min(1.0f, age / (float) FADE_TIME_MS);
                    }
                    int alpha = (int) (45 + 190 * remaining);
                    OverlayRender.line(context, previous[0], previous[1], current[0], current[1],
                        SlEspStyle.argb(alpha, rgb), width);
                }
                if (visible) {
                    previous[0] = current[0];
                    previous[1] = current[1];
                    hasPrevious = true;
                    previousCrumb = crumb;
                } else {
                    hasPrevious = false;
                    previousCrumb = null;
                }
            }
        }

        if (OverlayRender.projectInto(mc.player.getX(), mc.player.getY() + 0.08, mc.player.getZ(), current)) {
            int marker = width + 1;
            OverlayRender.box(context, current[0] - marker, current[1] - marker,
                current[0] + marker, current[1] + marker, SlEspStyle.argb(235, rgb), 1);
        }
    }
}
