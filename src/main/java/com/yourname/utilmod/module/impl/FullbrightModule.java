package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private enum FullbrightMode { GAMMA, MAXIMUM }

    private final EnumSetting<FullbrightMode> mode = new EnumSetting<>("Mode", FullbrightMode.class, FullbrightMode.GAMMA).description("Uses your chosen brightness level or the maximum brightness setting.");
    private final NumberSetting brightness = new NumberSetting("Brightness", 16.0, 1.0, 32.0).description("Gamma brightness used in Gamma mode.");
    private final BooleanSetting updateWhileEnabled = new BooleanSetting("Update While Enabled", true).description("Keeps reapplying the brightness while the module is on.");
    private final BooleanSetting restoreOnDisable = new BooleanSetting("Restore When Disabled", true).description("Returns brightness to your original setting when the module is turned off.");

    private double previousGamma = 1.0;

    public FullbrightModule() {
        super("Fullbright", "Maximizes brightness in dark areas.", Category.RENDER);
        addSetting(mode);
        addSetting(brightness);
        addSetting(updateWhileEnabled);
        addSetting(restoreOnDisable);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            previousGamma = mc.options.getGamma().getValue();
            applyGamma();
        }
    }

    @Override
    public void onDisable() {
        if (restoreOnDisable.isEnabled() && mc.options != null) {
            mc.options.getGamma().setValue(previousGamma);
        }
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.options == null) return;
        if (updateWhileEnabled.isEnabled()) applyGamma();
    }

    private void applyGamma() {
        if (mc.options == null) return;
        double target = mode.getValue() == FullbrightMode.MAXIMUM
            ? brightness.getMax()
            : brightness.getValue();
        if (Math.abs(mc.options.getGamma().getValue() - target) > 0.001) {
            mc.options.getGamma().setValue(target);
        }
    }
}
