package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/** Briefly releases the forward key on each landed attack, like a manual W-tap. */
public class WTapModule extends Module {
    public static WTapModule INSTANCE;

    private final NumberSetting releaseTicks = new NumberSetting("Release Ticks", 2.0, 1.0, 10.0)
        .description("How many ticks the forward key stays released after a hit.");
    private final NumberSetting cooldownTicks = new NumberSetting("Cooldown", 6.0, 1.0, 40.0)
        .description("Minimum ticks between W-taps.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Only taps when the attack lands on another player.");

    private boolean tapping;
    private boolean originalPressed;
    private int ticksLeft;
    private long nextAllowed;

    public WTapModule() {
        super("W Tap", "Releases the forward key briefly after each attack.", Category.SWORD);
        INSTANCE = this;
        addSetting(releaseTicks);
        addSetting(cooldownTicks);
        addSetting(playersOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    /** Called from the shared attack hook. Never rewrites the attack itself. */
    public static void onAttack(PlayerEntity attacker, Entity target) {
        WTapModule module = INSTANCE;
        if (module == null || !module.isEnabled() || target == null) return;
        if (module.playersOnly.isEnabled() && !(target instanceof PlayerEntity)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != attacker || module.tapping) return;
        long now = client.world == null ? System.currentTimeMillis() : client.world.getTime();
        if (now < module.nextAllowed) return;

        module.originalPressed = client.options.forwardKey.isPressed();
        client.options.forwardKey.setPressed(false);
        module.tapping = true;
        module.ticksLeft = (int) module.releaseTicks.getValue();
        module.nextAllowed = now + (long) module.cooldownTicks.getValue();
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (!tapping || client.player == null) return;
        if (--ticksLeft <= 0) restore();
    }

    private void restore() {
        if (tapping && mc.player != null) {
            mc.options.forwardKey.setPressed(originalPressed);
        }
        tapping = false;
        ticksLeft = 0;
    }
}
