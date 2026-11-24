package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.network.HitboxMultiplierPayload;
import com.yourname.utilmod.server.HitboxManager;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Expands the margin used by attack raycasts so targets are easier to hit.
 *
 * <p>The heavy lifting already exists in {@link HitboxManager} and the
 * ProjectileUtil/PlayerReach/AttackRange mixins: they widen the <em>attack</em>
 * box without touching physics or collision, which is what used to freeze the
 * game at large multipliers. This module is the missing menu-side controller:
 * it applies the GUI value every tick (settings have no change callback) and
 * mirrors it to the private server through {@link HitboxMultiplierPayload}.</p>
 *
 * <p>Server-authoritative hit checks only change when this same jar is installed
 * on the server. Margin expansion is capped at 6 blocks by HitboxManager.</p>
 */
public class HitboxAdjusterModule extends Module {
    // Non-neutral default so enabling the module has a visible effect straight
    // away instead of silently doing nothing until the slider is touched.
    private final NumberSetting multiplier = new NumberSetting("Size Multiplier", 5.0, 1.0, 20.0).description("How much wider the attack hitbox feels when aiming at entities.");
    private final BooleanSetting syncToServer = new BooleanSetting("Sync To Server", true).description("Shares the wider hitbox with a private server running this mod; other servers keep their own rules.");

    private ClientPlayerEntity lastPlayer;
    private double lastApplied = -1.0;
    private double lastSent = -1.0;
    private boolean synced;

    public HitboxAdjusterModule() {
        super("Hitbox Adjuster",
            "Widens your attack hitbox so targets are easier to hit. Server-side checks need this jar on your private server.",
            Category.UTIL);
        addSetting(multiplier);
        addSetting(syncToServer);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            lastPlayer = null;
            synced = false;
            return;
        }
        // Re-apply after a respawn/world switch: the UUID can be reused.
        if (player != lastPlayer) {
            lastPlayer = player;
            lastApplied = -1.0;
            synced = false;
        }
        if (!isEnabled()) return;

        double value = multiplier.getValue();
        if (value != lastApplied) {
            HitboxManager.setMultiplier(value, player.getUuid());
            lastApplied = value;
        }

        if (syncToServer.isEnabled()) {
            if (!synced || value != lastSent) send(value);
        } else if (synced) {
            // Turning sync off must explicitly restore the server value once.
            // send() marks synced=true, so clear it here to avoid a reset
            // packet being sent on every subsequent tick.
            send(1.0);
            synced = false;
        }
    }

    @Override
    public void onEnable() {
        lastApplied = -1.0;
        synced = false;
        if (mc.player != null) HitboxManager.setMultiplier(multiplier.getValue(), mc.player.getUuid());
    }

    @Override
    public void onDisable() {
        HitboxManager.clearAll();
        send(1.0);
        lastApplied = -1.0;
        lastSent = -1.0;
        synced = false;
    }

    private void send(double value) {
        if (mc.getNetworkHandler() == null) return;
        if (!ClientPlayNetworking.canSend(HitboxMultiplierPayload.ID)) return;
        ClientPlayNetworking.send(new HitboxMultiplierPayload(value));
        lastSent = value;
        synced = true;
    }
}
