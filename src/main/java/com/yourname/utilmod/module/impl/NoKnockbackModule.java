package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.network.NoKnockbackPayload;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Meteor-style Velocity with independent horizontal/vertical knockback scales.
 *
 * <p>Knockback is server-authoritative, so the scales are mirrored to the private
 * server through the custom payload. The one client-side option, No Sinking, only
 * smooths liquid movement and never rewrites incoming motion packets.</p>
 */
public class NoKnockbackModule extends Module {
    private final NumberSetting horizontal = new NumberSetting("Horizontal Knockback", 0.0, 0.0, 1.0)
        .description("Fraction of horizontal knockback to keep. 0 removes it entirely.");
    private final NumberSetting vertical = new NumberSetting("Vertical Knockback", 0.0, 0.0, 1.0)
        .description("Fraction of vertical knockback to keep. 0 removes it entirely.");
    private final BooleanSetting noSinking = new BooleanSetting("No Sinking", false)
        .description("Cancels downward velocity in water or lava while not jumping or sneaking.");

    private ClientPlayerEntity lastPlayer;
    private double lastHorizontal = -1.0;
    private double lastVertical = -1.0;

    public NoKnockbackModule() {
        super("Velocity", "Controls knockback and liquid push. Requires matching server-side support for the changed values.", Category.MOVEMENT);
        addSetting(horizontal);
        addSetting(vertical);
        addSetting(noSinking);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        resetState();
        apply();
    }

    @Override
    public void onDisable() {
        send(1.0, 1.0);
        resetState();
    }

    private void resetState() {
        lastPlayer = null;
        lastHorizontal = -1.0;
        lastVertical = -1.0;
    }

    private void onTick(net.minecraft.client.MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            resetState();
            return;
        }
        if (player != lastPlayer) {
            lastPlayer = player;
            lastHorizontal = -1.0;
            lastVertical = -1.0;
        }
        if (isEnabled()) {
            apply();
            if (noSinking.isEnabled()) stopSinking(player);
        }
    }

    private void apply() {
        double h = horizontal.getValue();
        double v = vertical.getValue();
        if (h == lastHorizontal && v == lastVertical) return;
        send(h, v);
        lastHorizontal = h;
        lastVertical = v;
    }

    private void stopSinking(ClientPlayerEntity player) {
        if (!player.isTouchingWater() && !player.isInLava()) return;
        if (mc.options.jumpKey.isPressed() || mc.options.sneakKey.isPressed()) return;
        Vec3d velocity = player.getVelocity();
        if (velocity.y >= 0.0) return;
        player.setVelocity(new Vec3d(velocity.x, 0.0, velocity.z));
    }

    private void send(double h, double v) {
        if (mc.getNetworkHandler() == null) return;
        if (!ClientPlayNetworking.canSend(NoKnockbackPayload.ID)) return;
        ClientPlayNetworking.send(new NoKnockbackPayload(h, v));
    }
}
