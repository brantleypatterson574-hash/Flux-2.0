package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Sends one configured chat line when a key is pressed. Nothing is rewritten. */
public class ChatMacroModule extends Module {
    private final KeybindSetting key = new KeybindSetting("Key", GLFW.GLFW_KEY_V)
        .description("Key that sends the chat message.");
    private final StringSetting message = new StringSetting("Message", "gg")
        .description("Text to send. The slash is added automatically for commands.");
    private final BooleanSetting command = new BooleanSetting("Command", false)
        .description("Sends the text as a server command by prefixing it with a slash.");
    private final NumberSetting cooldown = new NumberSetting("Cooldown", 500.0, 0.0, 10000.0)
        .unit("ms")
        .description("Minimum time between messages.");

    private long lastSent;

    public ChatMacroModule() {
        super("Chat Macro", "Sends a configured chat message when you press a key.", Category.UTIL);
        addSetting(key);
        addSetting(message);
        addSetting(command);
        addSetting(cooldown);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.player.networkHandler == null || client.currentScreen != null) return;
        if (!key.wasPressed(client)) return;
        long now = System.currentTimeMillis();
        if (now - lastSent < cooldown.getValue()) return;

        String text = message.getValue().trim();
        if (text.isEmpty()) return;
        if (command.isEnabled() && !text.startsWith("/")) text = "/" + text;
        client.player.networkHandler.sendChatMessage(text);
        lastSent = now;
    }
}
