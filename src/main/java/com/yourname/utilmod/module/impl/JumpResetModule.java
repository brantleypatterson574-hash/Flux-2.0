package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/** Jumps shortly after taking damage so knockback is easier to control. */
public class JumpResetModule extends Module {
    private final NumberSetting delayTicks = new NumberSetting("Delay", 1.0, 0.0, 10.0)
        .description("Ticks to wait after the damage before jumping.");
    private final NumberSetting minDamage = new NumberSetting("Minimum Damage", 0.5, 0.1, 10.0)
        .description("Ignores tiny health changes below this many half-hearts.");
    private final BooleanSetting groundOnly = new BooleanSetting("On Ground Only", true)
        .description("Only jumps when your feet are on a solid block.");

    private float lastHealth;
    private int pendingTicks = -1;

    public JumpResetModule() {
        super("Jump Reset", "Jumps briefly after you take damage.", Category.SWORD);
        addSetting(delayTicks);
        addSetting(minDamage);
        addSetting(groundOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        lastHealth = mc.player == null ? 0.0f : mc.player.getHealth();
        pendingTicks = -1;
    }

    @Override
    public void onDisable() {
        pendingTicks = -1;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (pendingTicks >= 0 && --pendingTicks <= 0) {
            if (!groundOnly.isEnabled() || client.player.isOnGround()) client.player.jump();
            pendingTicks = -1;
        }

        float health = client.player.getHealth();
        if (health < lastHealth - minDamage.getFloat() && pendingTicks < 0) {
            pendingTicks = Math.max(0, (int) delayTicks.getValue());
            if (pendingTicks == 0) {
                if (!groundOnly.isEnabled() || client.player.isOnGround()) client.player.jump();
                pendingTicks = -1;
            }
        }
        lastHealth = health;
    }
}
