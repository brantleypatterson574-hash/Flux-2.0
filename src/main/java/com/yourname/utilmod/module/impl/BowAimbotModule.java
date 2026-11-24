package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

/**
 * Gentle aim assistance that only runs while a bow or crossbow is in your main
 * hand. It changes your local view towards the target; it never fires, switches
 * items or sends any network packet of its own.
 */
public class BowAimbotModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 32.0, 4.0, 96.0)
        .description("Maximum distance to a target you can aim at.");
    private final NumberSetting viewAngle = new NumberSetting("View Angle", 120.0, 15.0, 360.0)
        .description("Only targets inside this cone around your current view are considered.");
    private final NumberSetting smoothingSpeed = new NumberSetting("Smoothing Speed", 12.0, 0.5, 45.0)
        .description("Degrees per tick that the view turns towards the target. Lower is smoother.");
    private final BooleanSetting visibleOnly = new BooleanSetting("Visible Only", true)
        .description("Skips targets hidden behind blocks.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", false)
        .description("Limits targets to other players instead of all living entities.");

    private LivingEntity target;
    private float aimYaw;
    private float aimPitch;
    private boolean hasAimState;

    public BowAimbotModule() {
        super("Bow Aimbot", "Turns your view towards a target while holding a bow or crossbow. Fires nothing. Your arrows still need your help.",
            Category.SWORD);
        addSetting(range);
        addSetting(viewAngle);
        addSetting(smoothingSpeed);
        addSetting(visibleOnly);
        addSetting(playersOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        target = null;
        hasAimState = false;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) {
            clearAim();
            return;
        }
        boolean holdingRanged = client.player.getMainHandStack().isOf(Items.BOW)
            || client.player.getMainHandStack().isOf(Items.CROSSBOW);
        if (!holdingRanged) {
            clearAim();
            return;
        }

        LivingEntity found = findTarget(client);
        if (found == null) {
            clearAim();
            return;
        }
        if (found != target) {
            target = found;
            hasAimState = false;
        }

        Vec3d eyes = client.player.getEyePos();
        Vec3d point = found.getEyePos();
        Vec3d delta = point.subtract(eyes);
        float wantedYaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
        float wantedPitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)));
        wantedPitch = Math.max(-90.0f, Math.min(90.0f, wantedPitch));

        if (!hasAimState) {
            aimYaw = client.player.getYaw();
            aimPitch = client.player.getPitch();
            hasAimState = true;
        }

        float step = (float) Math.max(0.1, smoothingSpeed.getValue());
        float yawDifference = wrapDegrees(wantedYaw - aimYaw);
        aimYaw = Math.abs(yawDifference) <= step
            ? wantedYaw : wrapDegrees(aimYaw + Math.signum(yawDifference) * step);
        float pitchDifference = wantedPitch - aimPitch;
        aimPitch = Math.abs(pitchDifference) <= step
            ? wantedPitch : aimPitch + Math.signum(pitchDifference) * step;

        // Local view only: no aim packet and no attack/use packet is generated here.
        client.player.setYaw(aimYaw);
        client.player.setPitch(Math.max(-90.0f, Math.min(90.0f, aimPitch)));
    }

    private void clearAim() {
        target = null;
        hasAimState = false;
    }

    private LivingEntity findTarget(MinecraftClient client) {
        Vec3d eyes = client.player.getEyePos();
        Vec3d look = client.player.getRotationVec(1.0f);
        double rangeSq = range.getValue() * range.getValue();
        double limit = Math.cos(Math.min(Math.PI, Math.toRadians(viewAngle.getValue()) * 0.5));
        LivingEntity best = null;
        double bestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : client.world.getEntitiesByClass(LivingEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            candidate -> candidate != client.player && candidate.isAlive() && !candidate.isSpectator())) {
            if (playersOnly.isEnabled() && !(entity instanceof PlayerEntity)) continue;
            if (visibleOnly.isEnabled() && !client.player.canSee(entity)) continue;

            double distanceSq = eyes.distanceTo(entity.getEyePos());
            if (distanceSq > rangeSq || distanceSq >= bestDistance) continue;
            Vec3d direction = entity.getEyePos().subtract(eyes).normalize();
            if (look.dotProduct(direction) < limit) continue;

            bestDistance = distanceSq;
            best = entity;
        }
        return best;
    }

    private static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) value -= 360.0f;
        if (value < -180.0f) value += 360.0f;
        return value;
    }
}
