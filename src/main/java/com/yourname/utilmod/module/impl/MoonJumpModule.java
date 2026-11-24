package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Gives your own player a floaty jump and gentle descent while the module is on.
 * The server still owns your real position, so other servers may correct it.
 */
public class MoonJumpModule extends Module {
    public static MoonJumpModule INSTANCE;

    private final NumberSetting jumpStrength = new NumberSetting("Jump Strength", 1.4, 0.5, 3.0)
        .description("Upward speed applied when you jump. Higher values feel lighter.");
    private final BooleanSetting requireSpace = new BooleanSetting("Require Space", true)
        .description("Only boost jumps made with Space. Turn off for continuous hopping while grounded.");
    private final BooleanSetting softFall = new BooleanSetting("Soft Fall", true)
        .description("Reduces your downward speed while the module is active.");
    private final NumberSetting fallFactor = new NumberSetting("Fall Factor", 0.55, 0.10, 1.0)
        .description("Fraction of normal falling speed used when Soft Fall is on. Lower floats longer.");

    public MoonJumpModule() {
        super("Moon Jump", "Makes your own jumps springy and floaty, like the moon has poor customer service. Servers may correct the motion.",
            Category.MOVEMENT);
        INSTANCE = this;
        addSetting(jumpStrength);
        addSetting(requireSpace);
        addSetting(softFall);
        addSetting(fallFactor);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (client.player.isSpectator()) return;
        PlayerEntity player = client.player;
        boolean jumping = !requireSpace.isEnabled() || client.options.jumpKey.isPressed();

        if (jumping && player.isOnGround()) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, jumpStrength.getValue(), velocity.z);
            player.setOnGround(false);
        } else if (jumping && softFall.isEnabled()) {
            Vec3d velocity = player.getVelocity();
            if (velocity.y < 0.0) {
                player.setVelocity(velocity.x, velocity.y * fallFactor.getValue(), velocity.z);
            }
        }
    }
}
