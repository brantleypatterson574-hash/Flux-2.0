package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.mixin.MinecraftClientInvokerMixin;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.EntityHitResult;

/** Repeats real vanilla attack/use presses at a configurable CPS with jitter. */
public class AutoClickerModule extends Module {
    private enum ClickButton { LEFT, RIGHT, BOTH }

    private final EnumSetting<ClickButton> button =
        new EnumSetting<>("Button", ClickButton.class, ClickButton.LEFT).description("Repeats the left attack click, the right use click, or both.");
    private final NumberSetting minCps = new NumberSetting("Minimum CPS", 8.0, 1.0, 40.0).description("Slowest click rate when the clicker varies its timing.");
    private final NumberSetting maxCps = new NumberSetting("Maximum CPS", 12.0, 1.0, 40.0).description("Fastest click rate when the clicker varies its timing.");
    private final NumberSetting jitter = new NumberSetting("Jitter", 18.0, 0.0, 60.0).description("Random timing variation as a percentage of the click interval.");
    private final BooleanSetting requireTarget = new BooleanSetting("Require Target", false).description("Only clicks while an entity is under the crosshair.");
    private final BooleanSetting pauseInMenus = new BooleanSetting("Pause In Menus", true).description("Stops clicking while a GUI or menu is open.");

    private long nextClick;

    public AutoClickerModule() {
        super("Auto Clicker",
            "Repeats real attack/use presses with configurable CPS and timing jitter.",
            Category.SWORD);
        addSetting(button);
        addSetting(minCps);
        addSetting(maxCps);
        addSetting(jitter);
        addSetting(requireTarget);
        addSetting(pauseInMenus);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextClick = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (pauseInMenus.isEnabled() && client.currentScreen != null) return;
        long now = System.currentTimeMillis();
        if (now < nextClick) return;

        if (requireTarget.isEnabled() && !(client.crosshairTarget instanceof EntityHitResult)) {
            nextClick = now + 50L;
            return;
        }

        MinecraftClientInvokerMixin invoker = (MinecraftClientInvokerMixin) client;
        switch (button.getValue()) {
            case LEFT -> invoker.utilmod$doAttack();
            case RIGHT -> invoker.utilmod$doItemUse();
            case BOTH -> {
                invoker.utilmod$doAttack();
                invoker.utilmod$doItemUse();
            }
        }
        nextClick = now + nextDelay();
    }

    private long nextDelay() {
        double low = Math.min(minCps.getValue(), maxCps.getValue());
        double high = Math.max(minCps.getValue(), maxCps.getValue());
        long stamp = System.nanoTime();
        double roll = ((stamp >>> 20) & 0xFFFFL) / 65535.0;
        double cps = low + (high - low) * roll;
        cps = Math.max(1.0, cps);

        double base = 1000.0 / cps;
        double jitterAmount = base * (jitter.getValue() / 100.0);
        double spread = (((stamp >>> 36) & 0xFFL) / 255.0) * 2.0 - 1.0;
        return Math.max(10L, Math.round(base + jitterAmount * spread));
    }
}
