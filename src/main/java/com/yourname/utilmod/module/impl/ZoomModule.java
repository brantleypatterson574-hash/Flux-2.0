package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Hold a key to zoom in; your normal FOV is restored when you let go. */
public class ZoomModule extends Module {
    private final KeybindSetting key = new KeybindSetting("Zoom Key", GLFW.GLFW_KEY_C)
        .description("Hold to zoom.");
    private final NumberSetting zoomFov = new NumberSetting("Zoom FOV", 30.0, 30.0, 90.0)
        .description("Field of view while zooming. Lower is closer (30 is the vanilla minimum).");

    private boolean zooming;
    private int savedFov = 70;

    public ZoomModule() {
        super("Zoom", "Hold a key to zoom in and release to return to your normal FOV.", Category.RENDER);
        addSetting(key);
        addSetting(zoomFov);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        restore(mc);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.options == null) return;
        boolean hold = client.currentScreen == null && key.isHeld(client);
        if (hold) {
            if (!zooming) {
                savedFov = client.options.getFov().getValue();
                zooming = true;
            }
            int target = (int) zoomFov.getValue();
            if (client.options.getFov().getValue() != target) client.options.getFov().setValue(target);
        } else {
            restore(client);
        }
    }

    private void restore(MinecraftClient client) {
        if (!zooming || client == null || client.options == null) return;
        client.options.getFov().setValue(savedFov);
        zooming = false;
    }
}
