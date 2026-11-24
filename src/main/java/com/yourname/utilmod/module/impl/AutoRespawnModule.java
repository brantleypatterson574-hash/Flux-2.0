package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;

/** Respawns automatically after death, with a small configurable delay. */
public class AutoRespawnModule extends Module {
    private final NumberSetting delayTicks = new NumberSetting("Delay Ticks", 20.0, 0.0, 200.0)
        .description("How many ticks to wait on the death screen before respawning.");
    private final BooleanSetting requireDeathScreen = new BooleanSetting("Require Death Screen", true)
        .description("Only respawn when the death screen is open, not merely while the player is dead.");

    private long elapsed;
    private boolean wasDead;

    public AutoRespawnModule() {
        super("Auto Respawn", "Respawns automatically after you die.", Category.PLAYER);
        addSetting(delayTicks);
        addSetting(requireDeathScreen);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        elapsed = 0;
        wasDead = false;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;

        boolean deathScreen = client.currentScreen instanceof DeathScreen;
        boolean dead = client.player.isDead();
        boolean active = requireDeathScreen.isEnabled() ? deathScreen : (dead || deathScreen);

        if (!active) {
            elapsed = 0;
            wasDead = false;
            return;
        }

        // A fresh death should restart the countdown even if the old screen lingered.
        if (dead && !wasDead) elapsed = 0;
        wasDead = dead;

        if (elapsed++ < (long) delayTicks.getValue()) return;
        client.player.requestRespawn();
        elapsed = 0;
        wasDead = false;
    }
}
