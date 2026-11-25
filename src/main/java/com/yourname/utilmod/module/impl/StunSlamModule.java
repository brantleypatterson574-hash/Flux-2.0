package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.friend.FriendManager;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Shield-stun combo: an axe hit disables a raised shield, then a mace hit is
 * sent right behind it. Nothing here changes shield state locally; the combo
 * is only a sequence of real vanilla attacks, so server rules and i-frames
 * decide whether both hits register.
 */
public class StunSlamModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 4.5, 1.0, 8.0)
        .description("How close a player must be before the combo starts.");
    private final NumberSetting viewAngle = new NumberSetting("View Angle", 90.0, 15.0, 360.0)
        .description("Width of the cone in front of you that the combo searches for a target.");
    private final BooleanSetting onlyWhenBlocking = new BooleanSetting("Only When Blocking", true)
        .description("Starts the combo only when the target has a raised shield.");
    private final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true)
        .description("Never runs the combo on players from your friends list.");
    private final NumberSetting followUpDelay = new NumberSetting("Mace Delay (Ticks)", 0.0, 0.0, 20.0)
        .description("0 sends the axe and mace hits in the same tick. Raise it if your server delays the second hit.");
    private final NumberSetting comboCooldown = new NumberSetting("Combo Cooldown", 20.0, 5.0, 120.0)
        .description("Ticks to wait before the combo can target someone again.");
    private final NumberSetting swapBackDelay = new NumberSetting("Swap Back Delay", 3.0, 0.0, 20.0)
        .description("Ticks after the mace hit before returning to your original hotbar slot.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Turns toward the target for the combo, then restores your original view.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Animates a hand swing with each attack in the combo.");

    private int comboCooldownTicks;
    private int followUpTicks;
    private int swapBackTicks;
    private int originalSlot = -1;
    private float[] heldRotation;
    private LivingEntity pendingTarget;

    public StunSlamModule() {
        super("Stun Slam",
            "Axe shield disable followed by a mace hit. At zero delay both attacks are sent in one tick; servers still decide if they land.",
            Category.MACE);
        addSetting(range);
        addSetting(viewAngle);
        addSetting(onlyWhenBlocking);
        addSetting(ignoreFriends);
        addSetting(followUpDelay);
        addSetting(comboCooldown);
        addSetting(swapBackDelay);
        addSetting(silentRotate);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        clearState();
    }

    private void clearState() {
        MinecraftClient client = mc;
        if (client != null) {
            restoreSlot(client.player);
            if (heldRotation != null) RotationUtil.restore(client, heldRotation);
        }
        comboCooldownTicks = 0;
        followUpTicks = 0;
        swapBackTicks = 0;
        originalSlot = -1;
        heldRotation = null;
        pendingTarget = null;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.interactionManager == null) {
            clearState();
            return;
        }
        ClientPlayerEntity player = client.player;

        if (comboCooldownTicks > 0) comboCooldownTicks--;
        if (swapBackTicks > 0 && --swapBackTicks == 0) restoreSlot(player);

        if (followUpTicks > 0 && --followUpTicks == 0) {
            if (pendingTarget != null && pendingTarget.isAlive()) {
                int mace = findMaceSlot(player);
                if (mace >= 0 && equip(player, mace)) attack(client, player, pendingTarget);
            }
            pendingTarget = null;
            if (heldRotation != null) {
                RotationUtil.restore(client, heldRotation);
                heldRotation = null;
            }
            swapBackTicks = (int) swapBackDelay.getValue();
            if (swapBackTicks == 0) restoreSlot(player);
            return;
        }

        if (player.isSpectator() || player.getAbilities().creativeMode || client.currentScreen != null) return;
        if (comboCooldownTicks > 0) return;

        LivingEntity target = findTarget(client, player);
        if (target == null) return;
        if (onlyWhenBlocking.isEnabled() && !isBlockingShield(target)) return;

        int axe = findAxeSlot(player);
        int mace = findMaceSlot(player);
        if (axe < 0 || mace < 0) return;

        if (silentRotate.isEnabled()) {
            Vec3d point = target.getEntityPos().add(0.0, target.getHeight() * 0.5, 0.0);
            heldRotation = RotationUtil.face(client, point);
        }
        if (!equip(player, axe)) {
            if (heldRotation != null) {
                RotationUtil.restore(client, heldRotation);
                heldRotation = null;
            }
            return;
        }
        attack(client, player, target);

        long delay = Math.max(0L, (long) followUpDelay.getValue());
        if (delay == 0) {
            if (equip(player, mace)) attack(client, player, target);
            if (heldRotation != null) {
                RotationUtil.restore(client, heldRotation);
                heldRotation = null;
            }
            swapBackTicks = (int) swapBackDelay.getValue();
            if (swapBackTicks == 0) restoreSlot(player);
        } else {
            pendingTarget = target;
            followUpTicks = (int) delay;
        }
        comboCooldownTicks = (int) comboCooldown.getValue();
    }

    private void attack(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        client.interactionManager.attackEntity(player, target);
        if (swing.isEnabled()) player.swingHand(Hand.MAIN_HAND);
    }

    private LivingEntity findTarget(MinecraftClient client, ClientPlayerEntity player) {
        double maxRange = range.getValue();
        Box area = player.getBoundingBox().expand(maxRange);
        Vec3d eyes = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        double cosLimit = Math.cos(Math.min(Math.PI, Math.toRadians(viewAngle.getValue()) / 2.0));

        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntitiesByClass(Entity.class, area, e -> e != player)) {
            if (!(entity instanceof PlayerEntity living) || !living.isAlive()) continue;
            if (ignoreFriends.isEnabled() && FriendManager.isFriend(living)) continue;
            if (!player.canSee(living)) continue;
            double distance = eyes.distanceTo(living.getEyePos());
            if (distance > maxRange) continue;
            Vec3d direction = living.getEyePos().subtract(eyes).normalize();
            if (look.dotProduct(direction) < cosLimit) continue;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = living;
            }
        }
        return best;
    }

    private boolean equip(ClientPlayerEntity player, int slot) {
        if (slot < 0 || slot > 8) return false;
        if (player.getInventory().getSelectedSlot() == slot) return true;
        if (originalSlot == -1) originalSlot = player.getInventory().getSelectedSlot();
        ItemUtil.selectSlot(player, slot);
        return true;
    }

    private void restoreSlot(ClientPlayerEntity player) {
        if (player != null && originalSlot >= 0 && player.getInventory().getSelectedSlot() != originalSlot) {
            ItemUtil.selectSlot(player, originalSlot);
        }
        originalSlot = -1;
    }

    private static boolean isBlockingShield(LivingEntity entity) {
        boolean holdingShield = entity.getStackInHand(Hand.OFF_HAND).isOf(Items.SHIELD)
            || entity.getStackInHand(Hand.MAIN_HAND).isOf(Items.SHIELD);
        return holdingShield && entity.isBlocking();
    }

    private static int findAxeSlot(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isIn(ItemTags.AXES)) return i;
        }
        return -1;
    }

    private static int findMaceSlot(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isOf(Items.MACE)) return i;
        }
        return -1;
    }
}
