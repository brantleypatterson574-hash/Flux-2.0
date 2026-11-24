package com.yourname.utilmod.util;

/**
 * Deterministic, safe 2D equivalents of the ScalableLux visual hook concepts.
 *
 * <p>ScalableLux exposes chams, item-glow, target-overlay and totem-pop hooks
 * that are driven by shader/material state. Those shaders are not part of this
 * project, so these helpers reproduce the *rules* - depth colour blending,
 * distance fading, layered glow, deterministic palettes - with the ordinary
 * {@link net.minecraft.client.gui.DrawContext} primitives already used by the
 * Flux overlays. Nothing here reads packets, changes game state or rotates the
 * player.</p>
 */
public final class SlEspStyle {
    private SlEspStyle() {}

    /** Packs an RGB value with an explicit 0..255 alpha. */
    public static int argb(int alpha, int rgb) {
        int a = (int) clamp(alpha, 0, 255);
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    /** Multiplies only the alpha channel, preserving the colour. */
    public static int withAlphaFactor(int argb, float factor) {
        int a = (int) clamp(((argb >>> 24) & 0xFF) * clamp(factor, 0.0f, 1.0f), 0, 255);
        return (a << 24) | (argb & 0x00FFFFFF);
    }

    /** Linear RGB interpolation. Equivalent to the chams visible/occluded blend. */
    public static int blend(int fromRgb, int toRgb, float t) {
        float f = (float) clamp(t, 0.0, 1.0);
        int r = channel(fromRgb >> 16, toRgb >> 16, f);
        int g = channel(fromRgb >> 8, toRgb >> 8, f);
        int b = channel(fromRgb, toRgb, f);
        return (r << 16) | (g << 8) | b;
    }

    private static int channel(int from, int to, float t) {
        return (int) (((from & 0xFF) * (1.0f - t)) + ((to & 0xFF) * t));
    }

    /**
     * Blends from {@code farRgb} at the scan edge to {@code nearRgb} up close.
     * This is the safe stand-in for an occluded/visible chams colour pair.
     */
    public static int depthColor(int nearRgb, int farRgb, double distance, double range) {
        float near = 1.0f - (float) clamp(distance / Math.max(1.0, range), 0.0, 1.0);
        return blend(farRgb, nearRgb, near);
    }

    /** Distance fade using absolute block distances, matching Block Outline. */
    public static int fadeAlpha(double distance, double range, double start,
                                double minimumPercent, boolean enabled) {
        if (!enabled) return 255;
        double begin = Math.max(1.0, Math.min(start, range));
        double span = Math.max(1.0, range - begin);
        double fraction = clamp((distance - begin) / span, 0.0, 1.0);
        double minimum = clamp(minimumPercent, 0.0, 100.0) / 100.0;
        return (int) Math.round(255.0 * (1.0 - fraction * (1.0 - minimum)));
    }

    /** Distance fade using a percentage of the configured scan range. */
    public static int fadeAlphaPercent(double distance, double range,
                                       double startPercent, double minimumPercent,
                                       boolean enabled) {
        if (!enabled) return 255;
        return fadeAlpha(distance, range,
            range * clamp(startPercent, 0.0, 100.0) / 100.0,
            minimumPercent, true);
    }

    /** Deterministic palette entry, used by the cosmetic totem-pop styles. */
    public static int palette(int first, int second, int index, int total) {
        if (total <= 1) return first;
        float t = (float) (Math.floorMod(index, total)) / (total - 1);
        return blend(first, second, t);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
