package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.friend.FriendManager;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

/**
 * Shared attack-assist loop for the three PvP weapon categories. Target scans
 * are throttled and the box is capped, so enabling several auras does not turn
 * into several full-world scans every frame.
 */
public abstract class AuraModule extends Module {
    private enum Priority { DISTANCE, ANGLE, HEALTH }

    protected final NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 16.0).description("Maximum distance from which the aura can pick a target.");
    private final NumberSetting fov = new NumberSetting("View Angle", 90.0, 15.0, 360.0).description("How wide the target search cone is, in degrees around where you are looking.");
    private final NumberSetting delay = new NumberSetting("Attack Delay", 2.0, 0.0, 20.0).description("Ticks to wait between automatic attacks.");
    private final BooleanSetting players = new BooleanSetting("Players", true).description("Allow the aura to target other players.");
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true).description("Allow the aura to target mobs.");
    private final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true)
        .description("Never target players on your friends list.");
    private final BooleanSetting swing = new BooleanSetting("Swing Hand", true).description("Animates a hand swing with each attack.");
    private final EnumSetting<Priority> priority =
        new EnumSetting<>("Target Priority", Priority.class, Priority.DISTANCE).description("Picks the nearest target, the one most centred on your view, or the weakest one.");

    private long nextAttackTick = -1L;

    protected AuraModule(String name, String description, Category category) {
        super(name, description, category);
        addSetting(range);
        addSetting(fov);
        addSetting(delay);
        addSetting(players);
        addSetting(mobs);
        addSetting(ignoreFriends);
        addSetting(swing);
        addSetting(priority);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    /** Weapon gate. Return true when the held item is valid for this aura. */
    protected boolean itemAllowed(ItemStack stack) {
        return true;
    }

    /** Extra filter for specialised auras (e.g. crystals only). */
    protected boolean targetAllowed(Entity entity) {
        return true;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (!itemAllowed(client.player.getMainHandStack())) return;
        if (client.world.getTime() < nextAttackTick) return;

        Entity target = findTarget(client);
        if (target == null) return;

        client.interactionManager.attackEntity(client.player, target);
        if (swing.isEnabled()) client.player.swingHand(client.player.getActiveHand());
        nextAttackTick = client.world.getTime() + Math.max(0L, (long) delay.getValue());
    }

    private Entity findTarget(MinecraftClient client) {
        double maxRange = range.getValue();
        Box box = client.player.getBoundingBox().expand(maxRange);
        Vec3d eye = client.player.getEyePos();
        Vec3d look = client.player.getRotationVec(1.0f);
        double maxAngleCos = Math.cos(Math.min(Math.PI, Math.toRadians(fov.getValue()) / 2.0));

        Entity best = null;
        double bestScore = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntitiesByClass(Entity.class, box, e -> e != client.player)) {
            if (entity.isRemoved() || !entity.isAlive()) continue;
            if (!(entity instanceof LivingEntity) && !(entity instanceof EndCrystalEntity)) continue;
            if (entity instanceof PlayerEntity && !players.isEnabled()) continue;
            if (entity instanceof MobEntity && !mobs.isEnabled()) continue;
            if (ignoreFriends.isEnabled() && FriendManager.isFriend(entity)) continue;
            if (!targetAllowed(entity)) continue;

            double distance = eye.distanceTo(entity.getEyePos());
            if (distance > maxRange) continue;
            Vec3d to = entity.getEyePos().subtract(eye).normalize();
            if (look.dotProduct(to) < maxAngleCos) continue;

            double score = switch (priority.getValue()) {
                case ANGLE -> 1.0 - look.dotProduct(to);
                case HEALTH -> entity instanceof LivingEntity living ? living.getHealth() : distance;
                case DISTANCE -> distance;
            };
            if (score < bestScore) {
                bestScore = score;
                best = entity;
            }
        }
        return best;
    }
}
