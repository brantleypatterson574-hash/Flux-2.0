package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;

/** Client-side weather clean-up: hides rain/snow and optionally clouds. Read by WorldRendererMixin. */
public class WeatherModule extends Module {
    public static WeatherModule INSTANCE;

    private final BooleanSetting hideWeather = new BooleanSetting("Hide Rain And Snow", true)
        .description("Stops rain and snow from being drawn.");
    private final BooleanSetting hideClouds = new BooleanSetting("Hide Clouds", false)
        .description("Stops clouds from being drawn.");

    public WeatherModule() {
        super("Weather", "Hides rain, snow and clouds on your screen. Nothing changes on the server.", Category.WORLD);
        INSTANCE = this;
        addSetting(hideWeather);
        addSetting(hideClouds);
    }

    public static boolean skipWeather() {
        WeatherModule m = INSTANCE;
        return m != null && m.isEnabled() && m.hideWeather.isEnabled();
    }

    public static boolean skipClouds() {
        WeatherModule m = INSTANCE;
        return m != null && m.isEnabled() && m.hideClouds.isEnabled();
    }
}
