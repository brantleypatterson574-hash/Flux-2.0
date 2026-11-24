package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ColorUtil;
import net.minecraft.client.render.state.SkyRenderState;

/**
 * Rewrites the copied sky render state so rain, shade and stars look different
 * without changing the world's weather or sending any packets. The mixin hook
 * lives in SkyRenderingMixin; this class only owns the settings and the values.
 */
public class WeatherChangerModule extends Module {
    private static WeatherChangerModule INSTANCE;

    private final NumberSetting rainAmount = new NumberSetting("Rain Amount", 0.0, 0.0, 1.0)
        .description("How much precipitation to draw in the sky. Zero is clear, one is a downpour.");
    private final NumberSetting thunderShadeAmount = new NumberSetting("Thunder Shade Amount", 0.0, 0.0, 1.0)
        .description("How strongly the sky colours darken while this module is active.");
    private final BooleanSetting shadeSkyBackdrop = new BooleanSetting("Override Clouds", false)
        .description("Also shades the sky behind the clouds. Cloud shapes themselves are never changed.");
    private final BooleanSetting keepStars = new BooleanSetting("Keep Stars", true)
        .description("Leaves star brightness alone. Turn off for a permanently overcast sky.");

    public WeatherChangerModule() {
        super("Weather Changer", "Sets your own rain and sky shade for visuals only. Server weather is untouched. Personal weather, zero meteorological consequences.",
            Category.WORLD);
        INSTANCE = this;
        addSetting(rainAmount);
        addSetting(thunderShadeAmount);
        addSetting(shadeSkyBackdrop);
        addSetting(keepStars);
    }

    /** Called from SkyRenderingMixin after vanilla has populated the render state. */
    public static void apply(SkyRenderState state) {
        WeatherChangerModule module = INSTANCE;
        if (module == null || !module.isEnabled() || state == null) return;

        float rain = (float) Math.max(0.0, Math.min(1.0, module.rainAmount.getValue()));
        state.rainGradient = rain;

        float shade = (float) Math.max(0.0, Math.min(1.0, module.thunderShadeAmount.getValue()));
        if (shade > 0.0f && module.shadeSkyBackdrop.isEnabled()) {
            int stormTint = 0xFF0B1020;
            state.skyColor = ColorUtil.mix(state.skyColor, stormTint, shade * 0.70f);
            state.sunriseAndSunsetColor = ColorUtil.mix(state.sunriseAndSunsetColor, stormTint, shade * 0.45f);
            if (shade >= 0.35f) state.shouldRenderSkyDark = true;
        }

        if (!module.keepStars.isEnabled()) state.starBrightness = 0.0f;
    }
}
