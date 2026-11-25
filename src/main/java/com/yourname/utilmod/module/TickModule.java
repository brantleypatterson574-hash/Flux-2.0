package com.yourname.utilmod.module;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Small shared base for modules that only need a guarded client tick.
 * Config loading, keybinds and settings still live in {@link Module}.
 */
public abstract class TickModule extends Module {
    protected TickModule(String name, String description, Category category) {
        super(name, description, category);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;
        onTick(client);
    }

    protected abstract void onTick(MinecraftClient client);
}
