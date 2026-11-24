package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Clears Minecraft's vanilla toast stack (advancements, recipes and hints).
 * Flux's own corner notifications are drawn separately and are not affected.
 */
public class ToastRemoverModule extends Module {
    public static ToastRemoverModule INSTANCE;

    private final NumberSetting interval = new NumberSetting("Check Interval", 2.0, 1.0, 40.0)
        .unit("ticks")
        .description("How often to clear the vanilla pop-up stack. Lower reacts faster.");
    private final BooleanSetting onlyInWorld = new BooleanSetting("Only In World", true)
        .description("Leave menus alone and only clear pop-ups while you are in a world.");

    private int counter;

    public ToastRemoverModule() {
        super("Toast Remover", "Clears Minecraft's vanilla pop-up toasts so advancement and recipe cards never block the view.",
            Category.UTIL);
        INSTANCE = this;
        addSetting(interval);
        addSetting(onlyInWorld);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.getToastManager() == null) return;
        if (onlyInWorld.isEnabled() && client.world == null) return;
        if (++counter < Math.max(1, (int) interval.getValue())) return;
        counter = 0;
        client.getToastManager().clear();
    }

    @Override
    public void onEnable() {
        counter = 0;
    }

    @Override
    public void onDisable() {
        counter = 0;
    }
}
