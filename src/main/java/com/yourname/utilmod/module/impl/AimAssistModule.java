package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** Turns the view slowly toward a nearby target while you are attacking. */
public class AimAssistModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 5.0, 1.0, 16.0)
        .unit("blocks")
        .description("How far away a target can be before aim assistance starts.");
    private final NumberSetting viewAngle = new NumberSetting("View Angle", 70.0, 10.0, 360.0)
        .description("Width of the cone in front of you that is searched for targets.");
    private final NumberSetting maxTurn = new NumberSetting("Turn Speed", 12.0, 1.0, 90.0)
        .description("Maximum degrees the view may turn per tick.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Ignore mobs when picking a target.");
    private final BooleanSetting whileAttacking = new BooleanSetting("While Attacking Only", true)
        .description("Only assists while the attack key is held.");

    public AimAssistModule() {
        super("Aim Assist", "Gently turns your view toward a nearby target while you attack.", Category.SWORD);
        addSetting(range);
        addSetting(viewAngle);
        addSetting(maxTurn);
        addSetting(playersOnly);
        addSetting(whileAttacking);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.currentScreen != null) return;
        if (whileAttacking.isEnabled() && !client.options.attackKey.isPressed()) return;

        LivingEntity target = null;
        double bestDistance = Double.MAX_VALUE;
        for (LivingEntity candidate : client.world.getEntitiesByClass(LivingEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            e -> e != client.player && e.isAlive())) {
            if (playersOnly.isEnabled() && !(candidate instanceof PlayerEntity)) continue;
            double distance = candidate.distanceTo(client.player);
            if (distance < bestDistance) {
                bestDistance = distance;
                target = candidate;
            }
        }
        if (target == null) return;

        Vec3d eyes = client.player.getEyePos();
        Vec3d point = target.getEyePos();
        double dx = point.x - eyes.x;
        double dy = point.y - eyes.y;
        double dz = point.z - eyes.z;
        float wantedYaw = (float) (Math.atan2(-dx, dz) * 180.0 / Math.PI);
        float wantedPitch = (float) (-Math.atan2(dy, Math.hypot(dx, dz)) * 180.0 / Math.PI);

        float yawDelta = MathHelper.wrapDegrees(wantedYaw - client.player.getYaw());
        if (Math.abs(yawDelta) > viewAngle.getValue() * 0.5f) return;

        float step = Math.min(maxTurn.getFloat(), Math.abs(yawDelta));
        float yaw = client.player.getYaw() + Math.copySign(step, yawDelta);
        float pitchDelta = wantedPitch - client.player.getPitch();
        float pitchStep = MathHelper.clamp(pitchDelta, -step, step);
        RotationUtil.apply(client, yaw, client.player.getPitch() + pitchStep);
    }
}
