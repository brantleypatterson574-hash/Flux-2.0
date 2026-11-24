package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Small local idle keep-alive: occasionally swings the hand and nudges the
 * view. Intended for private servers / AFK testing only. It sends no chat
 * messages and makes no anticheat-evasion claims.
 */
public class AntiAfkModule extends Module {
    private final NumberSetting intervalTicks = new NumberSetting("Interval", 100.0, 20.0, 600.0)
        .description("Ticks between each idle action.");
    private final BooleanSetting swing = new BooleanSetting("Swing Hand", true).description("Occasionally swings your held item while you are idle.");
    private final BooleanSetting look = new BooleanSetting("Nudge View", true).description("Turns the view slightly left and right while you are idle.");
    private final NumberSetting lookAmount = new NumberSetting("View Amount", 2.0, 0.1, 15.0)
        .description("Degrees added to yaw on each nudge.");

    private int timer;
    private boolean flip;

    public AntiAfkModule() {
        super("Anti AFK", "Keeps the local session active with small idle animations.",
            Category.UTIL);
        addSetting(intervalTicks);
        addSetting(swing);
        addSetting(look);
        addSetting(lookAmount);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        timer = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.currentScreen != null) return;
        if (++timer < (int) intervalTicks.getValue()) return;
        timer = 0;

        if (swing.isEnabled()) client.player.swingHand(client.player.getActiveHand());
        if (look.isEnabled()) {
            flip = !flip;
            float delta = (float) lookAmount.getValue() * (flip ? 1.0f : -1.0f);
            client.player.setYaw(client.player.getYaw() + delta);
        }
    }
}
