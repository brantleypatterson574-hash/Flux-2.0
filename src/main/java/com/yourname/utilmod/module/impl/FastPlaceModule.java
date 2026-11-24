package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.PlacementUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class FastPlaceModule extends Module {
    private final KeybindSetting keybind = new KeybindSetting("Key", GLFW.GLFW_KEY_F).description("Extra key that can trigger the repeated placement.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 0.0, 60.0).description("Ticks between repeated placement attempts.");
    private final BooleanSetting onlyWhenHolding = new BooleanSetting("Only When Holding", true).description("Only places while the use key or extra key is held.");
    private final BooleanSetting requireBlockInHand = new BooleanSetting("Require Block In Hand", true).description("Skips placement unless a block item is in your main hand.");
    private final BooleanSetting rightClickOnly = new BooleanSetting("Right Click Only", false).description("Uses only the right mouse button, not the extra key.");

    private int ticks;

    public FastPlaceModule() {
        super("Fast Place", "Repeats item use on a delay for faster block placement.", Category.PLAYER);
        addSetting(keybind);
        addSetting(delayTicks);
        addSetting(onlyWhenHolding);
        addSetting(requireBlockInHand);
        addSetting(rightClickOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        boolean useHeld = client.options.useKey.isPressed();
        boolean bindHeld = keybind.isHeld(client);
        boolean inputActive = rightClickOnly.isEnabled() ? useHeld : (useHeld || bindHeld);

        if (onlyWhenHolding.isEnabled() && !inputActive) {
            ticks = 0;
            return;
        }
        if (ticks > 0) {
            ticks--;
            return;
        }

        ItemStack held = client.player.getStackInHand(Hand.MAIN_HAND);
        if (requireBlockInHand.isEnabled() && !(held.getItem() instanceof BlockItem)) return;
        if (client.interactionManager == null) return;

        // Block items need interactBlock with the crosshair face; interactItem
        // only uses the item and never places a block.
        if (held.getItem() instanceof BlockItem) {
            PlacementUtil.interactCrosshairBlock(client, Hand.MAIN_HAND);
        } else {
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            client.player.swingHand(Hand.MAIN_HAND);
        }
        ticks = (int) delayTicks.getValue();
    }
}
