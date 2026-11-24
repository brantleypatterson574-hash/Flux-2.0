package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.NotificationManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

/**
 * Disconnects from the current world when health drops below a threshold.
 * Purely client-side: it just closes the session, like pressing Escape > Disconnect.
 * Intended for private-server testing, not public-server anticheat evasion.
 */
public class AutoLogModule extends Module {
    private final NumberSetting health = new NumberSetting("Health", 6.0, 1.0, 20.0)
        .description("Disconnect when health falls to this value or lower (half-hearts).");
    private final BooleanSetting onlyInWorld = new BooleanSetting("Only In World", true)
        .description("Ignore health changes while not in a playable world.");

    private boolean triggered;

    public AutoLogModule() {
        super("Auto Log", "Disconnects automatically at low health. Client-side only.",
            Category.PLAYER);
        addSetting(health);
        addSetting(onlyInWorld);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        triggered = false;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        if (onlyInWorld.isEnabled() && client.world == null) return;

        float current = client.player.getHealth();
        if (current <= 0.0f || current > health.getValue()) {
            triggered = false;
            return;
        }

        // Fire once per health event so we do not spam notifications while the
        // disconnect screen is opening.
        if (triggered) return;
        triggered = true;
        NotificationManager.alert("Auto Log", "Health reached " + format(current) + " - disconnecting");

        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        if (handler != null) handler.getConnection().disconnect(net.minecraft.text.Text.literal("Auto Log"));
    }

    private static String format(float value) {
        return value == Math.floor(value) ? Integer.toString((int) value)
            : String.format("%.1f", value);
    }
}
