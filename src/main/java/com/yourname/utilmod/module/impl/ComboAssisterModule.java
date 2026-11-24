package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/** Keeps a clean attack rhythm and avoids frantic target switching during combos. */
public class ComboAssisterModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 12.0).description("How close a target must be to join the combo.");
    private final NumberSetting fov = new NumberSetting("View Angle", 110.0, 20.0, 360.0).description("How wide the target search cone is, in degrees around your view.");
    private final NumberSetting comboDelay = new NumberSetting("Combo Delay (Ms)", 220.0, 50.0, 900.0).description("Milliseconds between attacks while staying on the same target.");
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay (Ms)", 320.0, 0.0, 1500.0).description("Milliseconds to wait before switching to a different target.");
    private final BooleanSetting lockTarget = new BooleanSetting("Lock Target", true).description("Keeps the current target until the switch delay passes.");
    private final BooleanSetting players = new BooleanSetting("Players", true).description("Allow other players to be combo targets.");
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true).description("Allow mobs to be combo targets.");
    private final BooleanSetting swing = new BooleanSetting("Swing Hand", true).description("Animates a hand swing with each attack.");

    private LivingEntity target;
    private long targetSince;
    private long lastAttack;

    public ComboAssisterModule() {
        super("Combo Assister",
            "Locks onto a target and spaces attacks into a steady combo instead of rapid target flipping.",
            Category.SWORD);
        addSetting(range);
        addSetting(fov);
        addSetting(comboDelay);
        addSetting(switchDelay);
        addSetting(lockTarget);
        addSetting(players);
        addSetting(mobs);
        addSetting(swing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        target = null;
        targetSince = 0L;
        lastAttack = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;

        long now = System.currentTimeMillis();
        if (target != null && (!target.isAlive() || target.isRemoved()
            || client.player.getEyePos().distanceTo(target.getEyePos()) > range.getValue() + 1.0)) {
            target = null;
        }

        if (target == null || !lockTarget.isEnabled()) {
            LivingEntity candidate = findTarget(client);
            boolean canSwitch = candidate != null && (target == null
                || now - targetSince >= switchDelay.getValue());
            if (canSwitch && candidate != target) {
                target = candidate;
                targetSince = now;
            }
        }
        if (target == null) return;

        if (now - lastAttack < comboDelay.getValue()) return;
        if (client.player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        client.interactionManager.attackEntity(client.player, target);
        if (swing.isEnabled()) client.player.swingHand(client.player.getActiveHand());
        lastAttack = now;
    }

    private LivingEntity findTarget(MinecraftClient client) {
        Vec3d eye = client.player.getEyePos();
        Vec3d look = client.player.getRotationVec(1.0f);
        double cos = Math.cos(Math.min(Math.PI, Math.toRadians(fov.getValue()) / 2.0));
        Box box = client.player.getBoundingBox().expand(range.getValue());

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntitiesByClass(LivingEntity.class, box,
            e -> e != client.player && e.isAlive())) {
            if (entity instanceof PlayerEntity && !players.isEnabled()) continue;
            if (entity instanceof MobEntity && !mobs.isEnabled()) continue;
            double dist = eye.distanceTo(entity.getEyePos());
            if (dist > range.getValue()) continue;
            if (look.dotProduct(entity.getEyePos().subtract(eye).normalize()) < cos) continue;
            if (dist < bestDist) {
                bestDist = dist;
                best = (LivingEntity) entity;
            }
        }
        return best;
    }
}
