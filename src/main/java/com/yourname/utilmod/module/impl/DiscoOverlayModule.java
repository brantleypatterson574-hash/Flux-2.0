package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.module.impl.GuiThemeModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.minecraft.client.gui.DrawContext;

/** Gentle full-screen colour bands. Purely visual and capped at a low opacity. */
public class DiscoOverlayModule extends Module implements HudOverlay {
    public static DiscoOverlayModule INSTANCE;

    private enum ColorMode { RAINBOW, THEME, FIXED }

    private final EnumSetting<ColorMode> colorMode =
        new EnumSetting<>("Color Mode", ColorMode.class, ColorMode.RAINBOW)
            .description("Choose rainbow colours, the active menu theme, or one fixed colour.");
    private final NumberSetting fixedColor = new NumberSetting("Fixed Color", 0xFF55FF, 0, 0xFFFFFF)
        .description("Colour used when Color Mode is Fixed.");
    private final NumberSetting cycleSpeed = new NumberSetting("Cycle Speed", 1.6, 0.3, 6.0)
        .unit("seconds")
        .description("Seconds for one full rainbow cycle.");
    private final NumberSetting maxOpacity = new NumberSetting("Max Opacity", 10.0, 2.0, 35.0)
        .unit("%")
        .description("Maximum tint strength. Kept low so the world stays readable.");
    private final BooleanSetting smoothPulse = new BooleanSetting("Smooth Pulse", true)
        .description("Fades the tint in and out instead of holding a constant strength.");
    private final NumberSetting stripeHeight = new NumberSetting("Stripe Height", 56.0, 8.0, 200.0)
        .unit("px")
        .description("Height of each colour band. Lower values make more, thinner bands.");
    private final BooleanSetting onlyInWorld = new BooleanSetting("Only In World", true)
        .description("Skip the overlay while a menu or loading screen is open.");

    public DiscoOverlayModule() {
        super("Disco Overlay", "Paints soft, slowly changing colour bands over the world. It is deliberately subtle and draws nothing else.",
            Category.RENDER);
        INSTANCE = this;
        addSetting(colorMode);
        addSetting(fixedColor);
        addSetting(cycleSpeed);
        addSetting(maxOpacity);
        addSetting(smoothPulse);
        addSetting(stripeHeight);
        addSetting(onlyInWorld);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden) return;
        if (onlyInWorld.isEnabled() && (mc.world == null || mc.currentScreen != null)) return;

        double seconds = System.nanoTime() / 1_000_000_000.0;
        double cycle = Math.max(0.3, cycleSpeed.getValue());
        float hue = (float) ((seconds / cycle) % 1.0);
        int rgb = switch (colorMode.getValue()) {
            case RAINBOW -> hsvToRgb(hue, 0.72f, 1.0f);
            case THEME -> GuiThemeModule.INSTANCE == null
                ? 0xFF55FF : GuiThemeModule.INSTANCE.getTheme().accent & 0xFFFFFF;
            case FIXED -> fixedColor.getColorValue();
        };

        int alpha = (int) (255 * Math.max(0.02, Math.min(0.35, maxOpacity.getValue() / 100.0)));
        if (smoothPulse.isEnabled()) {
            float pulse = 0.55f + 0.45f * (float) Math.sin(seconds * Math.PI * 2.0 / cycle);
            alpha = (int) (alpha * pulse);
        }
        int color = (Math.max(0, Math.min(255, alpha)) << 24) | rgb;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int band = Math.max(8, (int) stripeHeight.getValue());
        int accent = mix(rgb, 0xFFFFFF, 0.35f);
        int accentColor = (Math.max(12, alpha / 2) << 24) | accent;
        for (int y = 0; y < height; y += band * 2) {
            context.fill(0, y, width, Math.min(height, y + band), color);
            int altStart = y + band;
            if (altStart < height) {
                context.fill(0, altStart, width, Math.min(height, altStart + band), accentColor);
            }
        }
    }

    private static int hsvToRgb(float h, float s, float v) {
        int sector = Math.min(5, Math.max(0, (int) (h * 6.0f)));
        float f = h * 6.0f - sector;
        float p = v * (1.0f - s);
        float q = v * (1.0f - f * s);
        float t = v * (1.0f - (1.0f - f) * s);
        float r = 0, g = 0, b = 0;
        switch (sector) {
            case 0 -> { r = v; g = t; b = p; }
            case 1 -> { r = q; g = v; b = p; }
            case 2 -> { r = p; g = v; b = t; }
            case 3 -> { r = p; g = q; b = v; }
            case 4 -> { r = t; g = p; b = v; }
            default -> { r = v; g = p; b = q; }
        }
        return ((int) (r * 255) << 16) | ((int) (g * 255) << 8) | (int) (b * 255);
    }

    private static int mix(int fromRgb, int toRgb, float t) {
        int r = channel((fromRgb >> 16) & 0xFF, (toRgb >> 16) & 0xFF, t);
        int g = channel((fromRgb >> 8) & 0xFF, (toRgb >> 8) & 0xFF, t);
        int b = channel(fromRgb & 0xFF, toRgb & 0xFF, t);
        return (r << 16) | (g << 8) | b;
    }

    private static int channel(int from, int to, float t) {
        return Math.max(0, Math.min(255, (int) (from + (to - from) * t)));
    }
}
