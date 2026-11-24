package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Holds a movement key so you travel in a fixed direction without touching the
 * keyboard. Closing the module always releases every movement key, so it cannot
 * leave the player stuck walking.
 */
public class AutoWalkModule extends Module {
    private enum WalkDirection { FORWARD, BACKWARD, LEFT, RIGHT }

    private final EnumSetting<WalkDirection> direction =
        new EnumSetting<>("Direction", WalkDirection.class, WalkDirection.FORWARD).description("Which way to walk: forward, backward, left, or right.");
    private final BooleanSetting stopOnHeightChange = new BooleanSetting("Stop On Height Change", false)
        .description("Turns the module off if the player falls or steps up.");

    private double lastY;
    private boolean wasInWorld;

    public AutoWalkModule() {
        super("Auto Walk", "Automatically holds a movement key.", Category.MOVEMENT);
        addSetting(direction);
        addSetting(stopOnHeightChange);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = mc;
        lastY = client != null && client.player != null ? client.player.getY() : 0.0;
        wasInWorld = client != null && client.player != null;
    }

    @Override
    public void onDisable() {
        releaseAll();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) {
            if (wasInWorld) releaseAll();
            wasInWorld = false;
            return;
        }
        wasInWorld = true;

        if (stopOnHeightChange.isEnabled() && Math.abs(client.player.getY() - lastY) > 0.05) {
            toggle();
            return;
        }
        lastY = client.player.getY();

        releaseAll();
        switch (direction.getValue()) {
            case FORWARD -> client.options.forwardKey.setPressed(true);
            case BACKWARD -> client.options.backKey.setPressed(true);
            case LEFT -> client.options.leftKey.setPressed(true);
            case RIGHT -> client.options.rightKey.setPressed(true);
        }
    }

    private void releaseAll() {
        MinecraftClient client = mc;
        if (client == null) return;
        client.options.forwardKey.setPressed(false);
        client.options.backKey.setPressed(false);
        client.options.leftKey.setPressed(false);
        client.options.rightKey.setPressed(false);
    }
}
