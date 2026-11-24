package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.world.ClientWorld;

/**
 * Client-only sky mood switcher. It edits the render state after vanilla has
 * built it, so the world time, weather and server packets are untouched.
 */
public class SkyPaletteModule extends Module {
    private enum Preset { REAL, DAY, GOLDEN_HOUR, SUNSET, BLUE_HOUR, NIGHT }

    private record Palette(int sky, int horizon, float sunAngle, float stars) {}

    private static final Palette DAY = new Palette(0xFF78B4FF, 0xFFFFD9A0, 0.18f, 0.0f);
    private static final Palette GOLDEN = new Palette(0xFF7FA8FF, 0xFFFFB36B, 0.30f, 0.0f);
    private static final Palette SUNSET = new Palette(0xFF4A2A66, 0xFFFF7A3C, 0.72f, 0.15f);
    private static final Palette BLUE_HOUR = new Palette(0xFF1E2A55, 0xFF6F4A9C, 0.82f, 0.65f);
    private static final Palette NIGHT = new Palette(0xFF0A0E24, 0xFF202A55, 0.50f, 1.0f);

    public static SkyPaletteModule INSTANCE;

    private final EnumSetting<Preset> preset =
        new EnumSetting<>("Mood", Preset.class, Preset.GOLDEN_HOUR)
            .description("REAL keeps vanilla. The other moods recolour only your sky render state.");
    private final BooleanSetting customColors = new BooleanSetting("Custom Colors", false)
        .description("Lets the two colour pickers below replace the preset colours.");
    private final NumberSetting skyColor = new NumberSetting("Sky Color", 0x78B4FF, 0x000000, 0xFFFFFF)
        .description("Custom upper-sky colour. Double-click to type a hex value.");
    private final NumberSetting horizonColor = new NumberSetting("Horizon Color", 0xFFB36B, 0x000000, 0xFFFFFF)
        .description("Custom sunrise/sunset colour. Double-click to type a hex value.");
    private final NumberSetting sunAngleOverride = new NumberSetting("Sun Angle Override", -1.0, -1.0, 1.0)
        .description("0..1 around the sun's orbit. Leave at -1 to use the preset angle.");
    private final NumberSetting starOverride = new NumberSetting("Star Override", -1.0, -1.0, 1.0)
        .description("0 hides stars, 1 makes them bright. Leave at -1 to use the preset.");
    private final BooleanSetting clearRain = new BooleanSetting("Clear Rain Visuals", true)
        .description("Hides the rain gradient in the sky. The server weather is unchanged.");

    public SkyPaletteModule() {
        super("Sky Mood", "Client-only sky presets: daylight, golden hour, sunset, blue hour and night.",
            Category.WORLD);
        INSTANCE = this;
        addSetting(preset);
        addSetting(customColors);
        addSetting(skyColor);
        addSetting(horizonColor);
        addSetting(sunAngleOverride);
        addSetting(starOverride);
        addSetting(clearRain);
    }

    /** Called from SkyRenderingMixin after vanilla has populated the render state. */
    public static void apply(ClientWorld world, float tickDelta, SkyRenderState state) {
        SkyPaletteModule module = INSTANCE;
        if (module == null || !module.isEnabled() || world == null || state == null) return;
        Preset mood = module.preset.getValue();
        if (mood == Preset.REAL) return;

        Palette palette = palette(mood);
        if (module.customColors.isEnabled()) {
            state.skyColor = 0xFF000000 | module.skyColor.getColorValue();
            state.sunriseAndSunsetColor = 0xFF000000 | module.horizonColor.getColorValue();
        } else {
            state.skyColor = palette.sky();
            state.sunriseAndSunsetColor = palette.horizon();
        }

        float angle = (float) module.sunAngleOverride.getValue();
        state.sunAngle = angle >= 0.0f
            ? angle * 2.0f * (float) Math.PI
            : palette.sunAngle() * 2.0f * (float) Math.PI;

        float stars = (float) module.starOverride.getValue();
        state.starBrightness = stars >= 0.0f ? stars : palette.stars();
        state.shouldRenderSkyDark = state.starBrightness > 0.35f;

        if (module.clearRain.isEnabled()) state.rainGradient = 0.0f;
    }

    private static Palette palette(Preset mood) {
        return switch (mood) {
            case DAY -> DAY;
            case GOLDEN_HOUR -> GOLDEN;
            case SUNSET -> SUNSET;
            case BLUE_HOUR -> BLUE_HOUR;
            case NIGHT -> NIGHT;
            case REAL -> DAY;
        };
    }
}
