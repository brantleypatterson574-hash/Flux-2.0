package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

/** Drops listed items from the backpack through the server-synced inventory handler. */
public class AutoDropModule extends Module {
    private final StringSetting items = new StringSetting("Items", "dirt,cobblestone")
        .description("Comma-separated item names to drop from your inventory.");
    private final BooleanSetting auto = new BooleanSetting("Auto", false)
        .description("Drops matching items continuously instead of only on the key press.");
    private final KeybindSetting key = new KeybindSetting("Key", GLFW.GLFW_KEY_X)
        .description("Key that drops one matching stack at a time.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 0.0, 40.0)
        .description("Ticks between dropped stacks.");
    private final BooleanSetting exactMatch = new BooleanSetting("Exact Match", false)
        .description("Matches the full item ID instead of any part of its name.");

    private long nextDrop;

    public AutoDropModule() {
        super("Auto Drop", "Drops chosen items from your inventory on a key press or automatically.",
            Category.UTIL);
        addSetting(items);
        addSetting(auto);
        addSetting(key);
        addSetting(delayTicks);
        addSetting(exactMatch);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextDrop = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (client.currentScreen != null) return;
        boolean requested = auto.isEnabled() || key.wasPressed(client);
        if (!requested) return;

        long now = client.world.getTime();
        if (now < nextDrop) return;

        for (int slot = 9; slot < client.player.getInventory().size(); slot++) {
            ItemStack stack = client.player.getInventory().getStack(slot);
            if (stack.isEmpty() || !matches(stack)) continue;
            client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId,
                slot, 0, SlotActionType.THROW, client.player);
            nextDrop = now + (long) delayTicks.getValue();
            return;
        }
    }

    private boolean matches(ItemStack stack) {
        String id = Registries.ITEM.getId(stack.getItem()).toString();
        String path = id.substring(id.indexOf(':') + 1).toLowerCase(Locale.ROOT);
        for (String raw : items.getValue().split(",")) {
            String token = raw.trim().toLowerCase(Locale.ROOT);
            if (token.isEmpty()) continue;
            if (exactMatch.isEnabled()) {
                if (id.equalsIgnoreCase(token) || path.equals(token)) return true;
            } else if (path.contains(token)) {
                return true;
            }
        }
        return false;
    }
}
