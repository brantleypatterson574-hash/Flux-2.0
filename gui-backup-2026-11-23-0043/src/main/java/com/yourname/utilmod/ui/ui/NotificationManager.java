package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.impl.GuiThemeModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Small animated toast cards shown when a module is toggled or an alert fires.
 * Purely visual and client-side - the kind of polish modern utility clients add
 * around the dropdown menu. Implemented from scratch with no external assets.
 */
public final class NotificationManager {
    public enum Kind { ENABLE, DISABLE, INFO, ALERT }

    // Text objects are built once when the toast is pushed, not rebuilt every frame.
    private record Toast(Text title, Text message, Kind kind, long start, long duration) {}

    private static final List<Toast> TOASTS = new ArrayList<>();
    private static boolean enabled = false;
    private static final int MAX_TOASTS = 5;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 38;
    private static final int GAP = 6;

    private NotificationManager() {}

    /** Position presets for the toast stack. */
    public enum Position { TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT }

    public static void setEnabled(boolean value) {
        enabled = value;
        if (!value) TOASTS.clear();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void push(String title, String message, Kind kind) {
        if (!enabled) return;
        TOASTS.removeIf(t -> t.title().getString().equals(title)
            && t.message().getString().equals(message));
        TOASTS.add(new Toast(Text.literal(title), Text.literal(message),
            kind, System.currentTimeMillis(), 2600L));
        while (TOASTS.size() > MAX_TOASTS) TOASTS.remove(0);
    }

    public static void moduleToggled(String moduleName, boolean enabled) {
        push(enabled ? "Enabled" : "Disabled", moduleName,
            enabled ? Kind.ENABLE : Kind.DISABLE);
    }

    public static void info(String title, String message) {
        push(title, message, Kind.INFO);
    }

    public static void alert(String title, String message) {
        push(title, message, Kind.ALERT);
    }

    /** Draw the active toast stack. Colors are supplied by the current theme. */
    public static void render(DrawContext context, Position position,
                              int accent, int panel, int text, int textSecondary) {
        if (TOASTS.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();
        long now = System.currentTimeMillis();
        int index = 0;

        Iterator<Toast> it = TOASTS.iterator();
        while (it.hasNext()) {
            Toast toast = it.next();
            long age = now - toast.start;
            if (age >= toast.duration) {
                it.remove();
                continue;
            }
            float in = Math.min(1.0f, age / 180.0f);
            float out = Math.min(1.0f, (toast.duration - age) / 320.0f);
            float alpha = Math.min(in, out);
            int slide = (int) ((1.0f - easeOut(in)) * 26.0f);

            int baseY = switch (position) {
                case TOP_RIGHT, TOP_LEFT -> 10;
                case BOTTOM_RIGHT, BOTTOM_LEFT -> sh - 10 - HEIGHT;
            };
            int y = switch (position) {
                case TOP_RIGHT, BOTTOM_RIGHT -> baseY + index * (HEIGHT + GAP);
                case TOP_LEFT, BOTTOM_LEFT -> baseY - index * (HEIGHT + GAP);
            };
            int x = switch (position) {
                case TOP_RIGHT, BOTTOM_RIGHT -> sw - WIDTH - 10 + slide;
                case TOP_LEFT, BOTTOM_LEFT -> 10 - slide;
            };

            int alphaByte = (int) (255 * alpha);
            int box = tinted(panel, alphaByte);
            int radius = GuiThemeModule.INSTANCE == null || GuiThemeModule.INSTANCE.rounded()
                ? 10 : 0;
            int kindColor = kindColor(toast.kind(), accent);
            SlGuiPaint.fillRounded(context, x + 1, y + 2, x + WIDTH + 1, y + HEIGHT + 2,
                tinted(0x000000, alphaByte / 3), radius);
            SlGuiPaint.fillRounded(context, x, y, x + WIDTH, y + HEIGHT, box, radius);
            SlGuiPaint.fillRounded(context, x + 1, y + 2, x + 4, y + HEIGHT - 2,
                tinted(kindColor, alphaByte), 2);
            SlGuiPaint.strokeRounded(context, x, y, WIDTH, HEIGHT,
                tinted(accent, alphaByte / 3), radius, 1);

            SlGuiPaint.text(context, mc.textRenderer, toast.title().getString(),
                x + 10, y + 7, tinted(text, alphaByte));
            SlGuiPaint.text(context, mc.textRenderer, toast.message().getString(),
                x + 10, y + 21, tinted(textSecondary, alphaByte));

            // Thin rounded progress bar that drains over the toast lifetime.
            int barW = (int) ((WIDTH - 4) * (1.0f - age / (float) toast.duration));
            SlGuiPaint.fillRounded(context, x + 2, y + HEIGHT - 3, x + 2 + barW,
                y + HEIGHT - 1, tinted(kindColor, alphaByte / 2), 1);
            index++;
        }
    }

    private static int kindColor(Kind kind, int accent) {
        return switch (kind) {
            case ENABLE -> 0xFF44DD88;
            case DISABLE -> 0xFFDD5555;
            case ALERT -> 0xFFFFBB33;
            case INFO -> accent;
        };
    }

    private static int tinted(int color, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (color & 0x00FFFFFF);
    }

    private static float easeOut(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t) * (1.0f - t);
    }

    public static int activeCount() {
        return TOASTS.size();
    }
}
