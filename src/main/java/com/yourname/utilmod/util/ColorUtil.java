package com.yourname.utilmod.util;

/**
 * Original colour helpers shared by the GUI and HUD. Supports ARGB blending,
 * alpha replacement and a smooth rainbow gradient - the kind of live accent
 * colour controls popular clients expose, implemented here from scratch.
 */
public final class ColorUtil {
    private ColorUtil() {}

    /** Blend two ARGB colours. t=0 returns from, t=1 returns to. */
    public static int mix(int from, int to, float t) {
        float f = Math.max(0.0f, Math.min(1.0f, t));
        int a = (int) (((from >>> 24) & 0xFF) * (1 - f) + ((to >>> 24) & 0xFF) * f);
        int r = (int) (((from >> 16) & 0xFF) * (1 - f) + ((to >> 16) & 0xFF) * f);
        int g = (int) (((from >> 8) & 0xFF) * (1 - f) + ((to >> 8) & 0xFF) * f);
        int b = (int) ((from & 0xFF) * (1 - f) + (to & 0xFF) * f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Replace only the alpha byte of an ARGB colour. */
    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    /** Strip alpha, keeping only RGB. */
    public static int rgb(int color) {
        return color & 0x00FFFFFF;
    }

    /** Smooth rainbow colour that cycles over time. speed in cycles/second. */
    public static int rainbow(long timeMs, float speed, int alpha) {
        float hue = (timeMs / 1000.0f * speed) % 1.0f;
        return withAlpha(hsbToRgb(hue, 0.75f, 1.0f), alpha);
    }

    /** Slowly shifts a base colour's brightness for a subtle breathing accent. */
    public static int pulse(int color, long timeMs, float amount) {
        float wave = (float) (0.5 + 0.5 * Math.sin(timeMs / 380.0));
        float factor = 1.0f - amount + amount * wave;
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    /** Convert HSB (all 0..1) to an opaque ARGB colour, without java.awt. */
    public static int hsbToRgb(float hue, float saturation, float brightness) {
        hue = ((hue % 1.0f) + 1.0f) % 1.0f;
        saturation = Math.max(0.0f, Math.min(1.0f, saturation));
        brightness = Math.max(0.0f, Math.min(1.0f, brightness));
        float c = brightness * saturation;
        float x = c * (1.0f - Math.abs((hue * 6.0f) % 2.0f - 1.0f));
        float m = brightness - c;
        float r, g, b;
        switch ((int) (hue * 6.0f)) {
            case 0 -> { r = c; g = x; b = 0; }
            case 1 -> { r = x; g = c; b = 0; }
            case 2 -> { r = 0; g = c; b = x; }
            case 3 -> { r = 0; g = x; b = c; }
            case 4 -> { r = x; g = 0; b = c; }
            default -> { r = c; g = 0; b = x; }
        }
        int rr = Math.round((r + m) * 255.0f);
        int gg = Math.round((g + m) * 255.0f);
        int bb = Math.round((b + m) * 255.0f);
        return 0xFF000000 | (rr << 16) | (gg << 8) | bb;
    }
}
