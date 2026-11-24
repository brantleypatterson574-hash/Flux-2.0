package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.NotificationManager;
import com.yourname.utilmod.util.AttackClickTracker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayDeque;

/**
 * Client-side "am I acting human?" monitor. Watches your own attack rate,
 * hotbar swapping and Breach-swap automation, then warns when a configurable
 * limit is crossed. This is a self-check/safety tool - it never inspects or
 * interferes with other players.
 */
public class ReactionMonitorModule extends Module {
    private final NumberSetting cpsLimit = new NumberSetting("CPS Limit", 40.0, 5.0, 80.0).description("Clicks per second above which you get a warning.");
    private final NumberSetting swapLimit = new NumberSetting("Swap Limit", 8.0, 1.0, 30.0).description("Hotbar swaps in a second that count as too fast.");
    private final NumberSetting autoSwapLimit = new NumberSetting("Auto Swap Limit", 6.0, 1.0, 30.0).description("Module-driven swaps in a second that count as too fast.");
    private final NumberSetting cooldown = new NumberSetting("Alert Cooldown", 3.0, 1.0, 15.0).description("Seconds to wait before showing the same warning again.");
    private final BooleanSetting flagCps = new BooleanSetting("Flag High CPS", true).description("Warns when your attack rate exceeds the CPS limit.");
    private final BooleanSetting flagSwaps = new BooleanSetting("Flag Fast Swaps", true).description("Warns when you change hotbar slots too quickly.");
    private final BooleanSetting flagAutoSwaps = new BooleanSetting("Flag Auto Breach", true).description("Warns when Breach Swap changes slots too often.");
    private final BooleanSetting showAlerts = new BooleanSetting("Show Alerts", true).description("Shows the warning messages on screen.");

    private final ArrayDeque<Long> swaps = new ArrayDeque<>();
    private int lastSlot = -1;
    private long lastAlert;
    private long autoWindowStart;
    private int autoWindowCount;

    public ReactionMonitorModule() {
        super("Reaction Monitor",
            "Warns when your own click speed, swapping or Breach automation looks inhuman. Purely client-side self-check.",
            Category.UTIL);
        addSetting(cpsLimit);
        addSetting(swapLimit);
        addSetting(autoSwapLimit);
        addSetting(cooldown);
        addSetting(flagCps);
        addSetting(flagSwaps);
        addSetting(flagAutoSwaps);
        addSetting(showAlerts);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        swaps.clear();
        lastSlot = -1;
        lastAlert = 0L;
        autoWindowStart = System.currentTimeMillis();
        autoWindowCount = 0;
        AutoBreachModule.consumeAutoSwaps();
        AttackClickTracker.clear();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        long now = System.currentTimeMillis();
        autoWindowCount += AutoBreachModule.consumeAutoSwaps();

        int slot = client.player.getInventory().getSelectedSlot();
        if (lastSlot >= 0 && slot != lastSlot) {
            synchronized (swaps) {
                swaps.addLast(now);
                while (!swaps.isEmpty() && now - swaps.peekFirst() > 1000L) swaps.pollFirst();
            }
        }
        lastSlot = slot;

        if (now - lastAlert < (long) (cooldown.getValue() * 1000.0)) return;
        if (!showAlerts.isEnabled()) return;

        int cps = AttackClickTracker.cps();
        if (flagCps.isEnabled() && cps > (int) cpsLimit.getValue()) {
            warn("High CPS", cps + " clicks/s exceeds the " + (int) cpsLimit.getValue() + " limit");
            return;
        }

        int rate;
        synchronized (swaps) {
            rate = swaps.size();
        }
        if (flagSwaps.isEnabled() && rate > (int) swapLimit.getValue()) {
            warn("Fast Swapping", rate + " swaps/s exceeds the " + (int) swapLimit.getValue() + " limit");
            return;
        }

        if (now - autoWindowStart >= 1000L) {
            int autoRate = autoWindowCount;
            autoWindowStart = now;
            autoWindowCount = 0;
            if (flagAutoSwaps.isEnabled() && autoRate > (int) autoSwapLimit.getValue()) {
                warn("Auto Breach Spam", autoRate + " module swaps/s exceeds the "
                    + (int) autoSwapLimit.getValue() + " limit");
            }
        }
    }

    private void warn(String title, String message) {
        lastAlert = System.currentTimeMillis();
        NotificationManager.alert(title, message);
    }
}
