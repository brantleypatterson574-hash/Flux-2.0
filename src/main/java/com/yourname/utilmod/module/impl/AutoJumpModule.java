package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

/**
 * Jumps automatically while moving on the ground. Purely local input assistance;
 * it never sends a packet the vanilla jump key would not send.
 */
public class AutoJumpModule extends Module {
    private final BooleanSetting requireForward = new BooleanSetting("Require Forward", false)
        .description("Only jump while the forward key is held. Off jumps for any movement direction.");
    private final BooleanSetting requireSprint = new BooleanSetting("Require Sprint", false)
        .description("Only jump while sprinting.");
    private final BooleanSetting avoidStairs = new BooleanSetting("Avoid On Stairs", true)
        .description("Skip the jump when a solid block is directly above the player.");

    public AutoJumpModule() {
        super("Auto Jump", "Jumps automatically while walking, for smooth obstacle traversal.",
            Category.MOVEMENT);
        addSetting(requireForward);
        addSetting(requireSprint);
        addSetting(avoidStairs);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.currentScreen != null) return;
        if (!client.player.isOnGround() || client.player.isSneaking()) return;
        if (requireSprint.isEnabled() && !client.player.isSprinting()) return;
        if (requireForward.isEnabled() && !client.options.forwardKey.isPressed()) return;

        boolean moving = client.options.forwardKey.isPressed()
            || client.options.backKey.isPressed()
            || client.options.leftKey.isPressed()
            || client.options.rightKey.isPressed();
        if (!moving) return;

        if (avoidStairs.isEnabled() && client.world != null
            && client.world.getBlockState(client.player.getBlockPos().up()).isSolid()) return;

        client.player.jump();
    }
}
