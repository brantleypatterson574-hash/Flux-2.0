package com.yourname.utilmod.module.impl;

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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Shield counter for sword and mace fights. When the target in front of you is
 * actually blocking with a shield, the module briefly equips an axe, lands one
 * stun hit and returns to the weapon you were holding. No target, no swap.
 *
 * <p>Block and inventory changes need this jar on the private server if the
 * server restricts them. Intended for singleplayer, LAN and private servers.</p>
 */
public class ShieldBreakerModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 4.5, 1.0, 12.0)
        .description("How close a blocking target must be before the stun is attempted.");
    private final NumberSetting viewAngle = new NumberSetting("View Angle", 90.0, 15.0, 360.0)
        .description("Width of the cone in front of you that is searched for a shielding target.");
    private final NumberSetting shieldHitDelay = new NumberSetting("Shield Hit Delay", 6.0, 0.0, 40.0)
        .description("Ticks between axe stun hits, so one blocking target is not stunned every tick.");
    private final NumberSetting swapBackDelay = new NumberSetting("Swap Back Delay", 3.0, 0.0, 20.0)
        .description("Ticks after the stun hit before returning to your original hotbar slot.");
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only Players", true)
        .description("Ignores blocking mobs and only reacts to other players.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Turn toward the target for the stun hit, then restore your original view.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Swing your hand when the axe stun lands.");
    private final BooleanSetting privateServerNote = new BooleanSetting("Private Server Note", false)
        .description("Informational only: block/inventory changes need this jar on your private server if applicable. Ticking this box does not change behaviour.");

    private int originalSlot = -1;
    private int swapBackTicks;
    private long nextHitTick = -1L;

    public ShieldBreakerModule() {
        super("Shield Breaker",
            "Faces a shielding opponent, swaps briefly to an axe to stun the shield, then swaps back. Block/inventory changes require this jar on your private server if applicable. Shields are not voted off the island.",
            Category.SWORD);
        addSetting(range);
        addSetting(viewAngle);
        addSetting(shieldHitDelay);
        addSetting(swapBackDelay);
        addSetting(onlyPlayers);
        addSetting(silentRotate);
        addSetting(swing);
        addSetting(privateServerNote);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        originalSlot = -1;
        swapBackTicks = 0;
        nextHitTick = -1L;
    }

    @Override
    public void onDisable() {
        restoreSlot(mc.player);
        originalSlot = -1;
        swapBackTicks = 0;
        nextHitTick = -1L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null) return;
        ClientPlayerEntity player = client.player;

        tickSwapBack(player);
        if (client.currentScreen != null) return;
        if (swapBackTicks > 0 || originalSlot != -1) return;
        if (!isWeapon(player.getMainHandStack())) return;
        if (client.world.getTime() < nextHitTick) return;

        LivingEntity target = findBlockingTarget(client, player);
        if (target == null) return;

        int axeSlot = ItemUtil.findSlot(player, stack -> stack.isIn(ItemTags.AXES), true, false);
        if (axeSlot < 0) return;

        float[] previousRotation = null;
        if (silentRotate.isEnabled()) {
            Vec3d point = target.getEntityPos().add(0.0, target.getHeight() * 0.5, 0.0);
            previousRotation = RotationUtil.face(client, point);
        }

        originalSlot = player.getInventory().getSelectedSlot();
        ItemUtil.selectSlot(player, axeSlot);
        client.interactionManager.attackEntity(player, target);
        if (swing.isEnabled()) player.swingHand(Hand.MAIN_HAND);
        if (previousRotation != null) RotationUtil.restore(client, previousRotation);

        swapBackTicks = (int) swapBackDelay.getValue();
        if (swapBackTicks == 0) restoreSlot(player);
        nextHitTick = client.world.getTime() + (long) shieldHitDelay.getValue();
    }

    private void tickSwapBack(ClientPlayerEntity player) {
        if (swapBackTicks <= 0) return;
        swapBackTicks--;
        if (swapBackTicks == 0) restoreSlot(player);
    }

    private void restoreSlot(ClientPlayerEntity player) {
        if (player != null && originalSlot >= 0 && player.getInventory().getSelectedSlot() != originalSlot) {
            ItemUtil.selectSlot(player, originalSlot);
        }
        originalSlot = -1;
    }

    /** Finds the closest visible entity that is actively blocking with a shield. */
    private LivingEntity findBlockingTarget(MinecraftClient client, ClientPlayerEntity player) {
        double maxRange = range.getValue();
        Box area = player.getBoundingBox().expand(maxRange);
        Vec3d eyes = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        double cosLimit = Math.cos(Math.min(Math.PI, Math.toRadians(viewAngle.getValue()) / 2.0));

        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntitiesByClass(Entity.class, area, e -> e != player)) {
            if (!(entity instanceof LivingEntity living) || !living.isAlive()) continue;
            if (onlyPlayers.isEnabled() && !(living instanceof PlayerEntity)) continue;
            if (!player.canSee(living) || !isBlockingShield(living)) continue;

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

    private static boolean isWeapon(ItemStack stack) {
        return stack.isIn(ItemTags.SWORDS) || stack.isOf(Items.MACE);
    }

    private static boolean isBlockingShield(LivingEntity entity) {
        boolean shieldInHand = entity.getStackInHand(Hand.OFF_HAND).isOf(Items.SHIELD)
            || entity.getStackInHand(Hand.MAIN_HAND).isOf(Items.SHIELD);
        return shieldInHand && entity.isBlocking();
    }
}
