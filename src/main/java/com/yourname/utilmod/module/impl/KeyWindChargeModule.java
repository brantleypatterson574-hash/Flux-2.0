package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class KeyWindChargeModule extends Module {
    private enum Stage { IDLE, WAIT, THROW, RESTORE }

    public static KeyWindChargeModule INSTANCE;

    private final KeybindSetting key = new KeybindSetting("Key", GLFW.GLFW_KEY_G)
        .description("Key that throws the wind charge.");
    private final NumberSetting throwPitch = new NumberSetting("Throw Pitch", 90.0, -90.0, 90.0)
        .description("View pitch used for the throw; 90 points straight down.");
    private final BooleanSetting silentAim = new BooleanSetting("Silent Aim", true)
        .description("Uses the throw pitch briefly, then restores your original view.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your original hotbar item after the throw.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 1.0, 0.0, 20.0)
        .description("Ticks to wait after pressing the key before throwing.");
    private final BooleanSetting onlyWhenFalling = new BooleanSetting("Only When Falling", false)
        .description("Requires you to already be falling before the macro throws.");
    private final BooleanSetting perfectJump = new BooleanSetting("Perfect Jump", true)
        .description("Jumps on the exact tick your own wind charge bursts, while you are still rising.");

    private Stage stage = Stage.IDLE;
    private int timer;
    private int originalSlot = -1;
    private float originalPitch;
    private boolean changedSlot;
    private boolean changedPitch;
    private boolean restorePitch;
    private boolean armed;
    private int armedTicks;

    public KeyWindChargeModule() {
        super("Wind Charge Jump", "Keybind macro that throws a wind charge at a configured pitch for a quick boost.", Category.MOVEMENT);
        INSTANCE = this;
        addSetting(key);
        addSetting(throwPitch);
        addSetting(silentAim);
        addSetting(swapBack);
        addSetting(delayTicks);
        addSetting(onlyWhenFalling);
        addSetting(perfectJump);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        armed = false;
        armedTicks = 0;
    }

    @Override
    public void onDisable() {
        finish();
        armed = false;
        armedTicks = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;

        if (armed && ++armedTicks > 30) {
            armed = false;
            armedTicks = 0;
        }

        if (key.wasPressed(client) && stage == Stage.IDLE) {
            if (onlyWhenFalling.isEnabled() && !isFalling(client.player)) return;
            stage = Stage.WAIT;
            timer = (int) delayTicks.getValue();
            if (timer <= 0) stage = Stage.THROW;
        }

        switch (stage) {
            case WAIT -> {
                if (--timer <= 0) stage = Stage.THROW;
            }
            case THROW -> {
                performThrow(client);
                stage = Stage.RESTORE;
                timer = 1;
            }
            case RESTORE -> {
                if (--timer <= 0) finish();
            }
            default -> {
            }
        }
    }

    private void performThrow(MinecraftClient client) {
        PlayerEntity player = client.player;
        Hand hand = ItemUtil.handHolding(player, Items.WIND_CHARGE);

        if (hand == null) {
            int slot = ItemUtil.findSlot(player, Items.WIND_CHARGE, true, false);
            if (slot == -1) {
                finish();
                return;
            }
            originalSlot = player.getInventory().getSelectedSlot();
            ItemUtil.selectSlot(player, slot);
            changedSlot = true;
            hand = Hand.MAIN_HAND;
        }

        // Always snapshot the pitch and the restore intent at throw time. If the
        // setting is toggled mid-throw (or the module is disabled while a throw is
        // pending) the original view must still be restored exactly once.
        originalPitch = player.getPitch();
        changedPitch = true;
        restorePitch = silentAim.isEnabled();
        player.setPitch(throwPitch.getFloat());

        armed = perfectJump.isEnabled();
        armedTicks = 0;
        if (client.interactionManager != null) {
            client.interactionManager.interactItem(player, hand);
        }
    }

    /**
     * Called from the wind-charge explosion mixin before the burst impulse is
     * applied. Jumping here keeps the player in the rising part of the vanilla
     * jump, which is what produces the tallest launch.
     */
    public static void onExplosion(Entity owner, Vec3d burstPos) {
        KeyWindChargeModule module = INSTANCE;
        if (module == null || !module.isEnabled() || !module.armed) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || owner != client.player) return;

        module.armed = false;
        module.armedTicks = 0;
        if (!module.perfectJump.isEnabled()) return;
        if (client.player.isOnGround()) client.player.jump();
    }

    private void finish() {
        if (mc.player != null) {
            if (changedPitch && restorePitch) {
                mc.player.setPitch(originalPitch);
            }
            if (changedSlot && swapBack.isEnabled() && originalSlot >= 0) {
                ItemUtil.selectSlot(mc.player, originalSlot);
            }
        }
        stage = Stage.IDLE;
        timer = 0;
        originalSlot = -1;
        changedSlot = false;
        changedPitch = false;
        restorePitch = false;
        // The burst can happen several ticks after the throw. Keep the Perfect
        // Jump arming alive until the explosion callback or its timeout clears it.
    }

    private static boolean isFalling(PlayerEntity player) {
        return !player.isOnGround() && player.getVelocity().y < 0.0;
    }
}
