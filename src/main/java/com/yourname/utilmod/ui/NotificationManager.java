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
    // Timing uses nanoTime: changing the system clock must not make toasts stick
    // around forever or vanish instantly.
    private record Toast(Text title, Text message, Kind kind, long start, long duration) {}

    private static final long LIFETIME_NANOS = 2_600_000_000L;
    private static final long FADE_IN_NANOS = 180_000_000L;
    private static final long FADE_OUT_NANOS = 320_000_000L;

    private static final List<Toast> TOASTS = new ArrayList<>();
    private static boolean enabled = false;
    private static final int MAX_TOASTS = 5;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 26;
    private static final int GAP = 8;

    private NotificationManager() {}

    /** Position presets for the toast stack. */
    public enum Position { TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT }

    public static synchronized void setEnabled(boolean value) {
        enabled = value;
        if (!value) TOASTS.clear();
    }

    public static synchronized boolean isEnabled() {
        return enabled;
    }

    public static synchronized void push(String title, String message, Kind kind) {
        if (!enabled) return;
        TOASTS.removeIf(t -> t.title().getString().equals(title)
            && t.message().getString().equals(message));
        TOASTS.add(new Toast(Text.literal(title), Text.literal(message),
            kind, System.nanoTime(), LIFETIME_NANOS));
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
    public static synchronized void render(DrawContext context, Position position,
                                           int accent, int panel, int text, int textSecondary) {
        if (TOASTS.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        // Cancel Minecraft's GUI scale the same way the menu does. Toast text
        // then uses Minecraft's default font at one physical pixel per unit.
        float scale = 1.0f / Math.max(0.0001f, mc.getWindow().getScaleFactor());
        int sw = mc.getWindow().getWidth();
        int sh = mc.getWindow().getHeight();
        long now = System.nanoTime();
        int index = 0;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);

        Iterator<Toast> it = TOASTS.iterator();
        while (it.hasNext()) {
            Toast toast = it.next();
            long age = now - toast.start;
            if (age >= toast.duration || age < 0L) {
                it.remove();
                continue;
            }
            float in = Math.min(1.0f, age / (float) FADE_IN_NANOS);
            float out = Math.min(1.0f, (toast.duration - age) / (float) FADE_OUT_NANOS);
            float alpha = Math.min(in, out);
            int slide = (int) ((1.0f - easeOut(in)) * 26.0f);

            int topY = 10;
            int bottomY = sh - 10 - HEIGHT;
            int baseY = (position == Position.TOP_RIGHT || position == Position.TOP_LEFT)
                ? topY : bottomY;
            // Newest toast sits closest to its screen edge; older ones stack
            // inwards. Top positions grow down, bottom positions grow up.
            int y = (position == Position.TOP_RIGHT || position == Position.TOP_LEFT)
                ? baseY + index * (HEIGHT + GAP)
                : baseY - index * (HEIGHT + GAP);
            if (y < 0 || y + HEIGHT > sh) continue;

            boolean right = position == Position.TOP_RIGHT || position == Position.BOTTOM_RIGHT;
            int x = right ? sw - WIDTH - 10 + slide : 10 - slide;

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

            // Fit both lines so a long module name cannot spill into a
            // neighbouring toast or off the side of the screen.
            String titleText = SlGuiPaint.fit(mc.textRenderer, toast.title().getString(),
                WIDTH - 20, SlGuiPaint.FONT_UNIT);
            String messageText = SlGuiPaint.fit(mc.textRenderer, toast.message().getString(),
                WIDTH - 20, SlGuiPaint.FONT_UNIT);
            SlGuiPaint.text(context, mc.textRenderer, titleText,
                x + 10, y + 7, tinted(text, alphaByte));
            SlGuiPaint.text(context, mc.textRenderer, messageText,
                x + 10, y + 21, tinted(textSecondary, alphaByte));

            // Thin rounded progress bar that drains over the toast lifetime.
            int barW = (int) ((WIDTH - 4) * (1.0f - age / (float) toast.duration));
            SlGuiPaint.fillRounded(context, x + 2, y + HEIGHT - 3, x + 2 + barW,
                y + HEIGHT - 1, tinted(kindColor, alphaByte / 2), 1);
            index++;
        }
        context.getMatrices().popMatrix();
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

    public static synchronized int activeCount() {
        long now = System.nanoTime();
        int count = 0;
        for (Toast toast : TOASTS) {
            if (now - toast.start < toast.duration) count++;
        }
        return count;
    }
}
