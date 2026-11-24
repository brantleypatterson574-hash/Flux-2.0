package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/** Toggle-sprint: keeps you sprinting while you hold a movement key. */
public class SprintModule extends Module {
    private final BooleanSetting allDirections = new BooleanSetting("All Directions", false)
        .description("Sprint in every direction, not just forward.");
    private final BooleanSetting whileUsingItem = new BooleanSetting("While Using Items", false)
        .description("Keep sprinting while eating, blocking or drawing a bow.");
    private final NumberSetting minHunger = new NumberSetting("Minimum Hunger", 7.0, 0.0, 20.0)
        .description("Sprinting stops below this food level (vanilla needs 7 or more).");

    public SprintModule() {
        super("Sprint", "Keeps you sprinting while you move, so you never have to double-tap.", Category.MOVEMENT);
        addSetting(allDirections);
        addSetting(whileUsingItem);
        addSetting(minHunger);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        ClientPlayerEntity player = client.player;
        if (client.currentScreen != null || player.isSneaking()) return;
        if (!whileUsingItem.isEnabled() && player.isUsingItem()) return;
        if (player.getHungerManager().getFoodLevel() < (int) minHunger.getValue()) return;

        boolean forward = client.options.forwardKey.isPressed();
        boolean sideways = client.options.backKey.isPressed()
            || client.options.leftKey.isPressed() || client.options.rightKey.isPressed();
        boolean moving = allDirections.isEnabled() ? (forward || sideways) : forward;
        if (!moving || player.horizontalCollision) return;

        if (!player.isSprinting()) player.setSprinting(true);
    }
}
