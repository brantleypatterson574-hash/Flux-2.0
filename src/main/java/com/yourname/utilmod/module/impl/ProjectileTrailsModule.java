package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/** Draws fading paths behind nearby projectiles using positions observed on the client. */
public class ProjectileTrailsModule extends Module implements HudOverlay {
    private static final class Trail {
        final ArrayDeque<Vec3d> points = new ArrayDeque<>();
    }

    private final NumberSetting range = new NumberSetting("Range", 64.0, 8.0, 192.0).description("Only records projectiles within this distance.");
    private final NumberSetting trailLength = new NumberSetting("Trail Length", 16.0, 3.0, 64.0).description("Number of past positions kept in each trail.");
    private final NumberSetting recordInterval = new NumberSetting("Record Interval", 1.0, 1.0, 10.0).description("Ticks between recorded trail positions.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0).description("Thickness of the trail lines.");
    private final BooleanSetting showOwn = new BooleanSetting("Show Your Projectiles", true)
        .description("Tracks projectiles you fired. Nothing is changed about how they behave.");
    private final BooleanSetting showOthers = new BooleanSetting("Show Other Projectiles", true).description("Draws trails for projectiles fired by others as well as your own.");
    private final NumberSetting ownColor = new NumberSetting("Your Trail Color", 0x67E8F9, 0, 0xFFFFFF).description("Colour of trails from your own projectiles.");
    private final NumberSetting otherColor = new NumberSetting("Other Trail Color", 0xFB7185, 0, 0xFFFFFF).description("Colour of trails from other players' projectiles.");

    private final Map<Entity, Trail> trails = new IdentityHashMap<>();
    private final Set<Entity> seen = new HashSet<>();
    private int tick;

    public ProjectileTrailsModule() {
        super("Projectile Trails", "Shows fading flight paths for nearby projectiles. Visual only.",
            Category.RENDER);
        addSetting(range);
        addSetting(trailLength);
        addSetting(recordInterval);
        addSetting(lineWidth);
        addSetting(showOwn);
        addSetting(showOthers);
        addSetting(ownColor);
        addSetting(otherColor);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        trails.clear();
        seen.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            trails.clear();
            seen.clear();
            return;
        }
        if (tick++ % Math.max(1, (int) recordInterval.getValue()) != 0) return;

        seen.clear();
        double r = range.getValue();
        Box area = new Box(
            client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
        );

        int cap = Math.max(3, (int) trailLength.getValue());
        for (Entity entity : client.world.getOtherEntities(client.player, area,
            candidate -> candidate instanceof ProjectileEntity)) {
            ProjectileEntity projectile = (ProjectileEntity) entity;
            Entity owner = projectile.getOwner();
            boolean mine = owner == client.player || owner == null;
            if (mine && !showOwn.isEnabled()) continue;
            if (!mine && !showOthers.isEnabled()) continue;

            Trail trail = trails.computeIfAbsent(entity, ignored -> new Trail());
            trail.points.addLast(new Vec3d(entity.getX(),
                entity.getY() + entity.getHeight() * 0.5, entity.getZ()));
            while (trail.points.size() > cap) trail.points.removeFirst();
            seen.add(entity);
        }

        trails.keySet().removeIf(entity -> !seen.contains(entity) || entity.isRemoved());
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || trails.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int width = Math.max(1, (int) lineWidth.getValue());
        int[] previous = new int[3];
        int[] current = new int[3];

        for (Map.Entry<Entity, Trail> entry : trails.entrySet()) {
            Entity entity = entry.getKey();
            Trail trail = entry.getValue();
            if (trail.points.size() < 2) continue;

            int rgb = (entity instanceof ProjectileEntity projectile && projectile.getOwner() == mc.player)
                ? ownColor.getColorValue() : otherColor.getColorValue();
            boolean previousValid = false;
            int index = 0;
            int last = trail.points.size() - 1;

            for (Vec3d point : trail.points) {
                boolean ok = OverlayRender.projectInto(point.x, point.y, point.z, current);
                if (ok && previousValid && (previous[0] != current[0] || previous[1] != current[1])) {
                    float fraction = (float) index / last;
                    int alpha = (int) (40 + 200 * fraction);
                    OverlayRender.line(context, previous[0], previous[1], current[0], current[1],
                        SlEspStyle.argb(alpha, rgb), width);
                }
                if (ok) {
                    previous[0] = current[0];
                    previous[1] = current[1];
                    previous[2] = current[2];
                    previousValid = true;
                } else {
                    previousValid = false;
                }
                index++;
            }

            if (!entity.isRemoved() && OverlayRender.projectInto(entity.getX(),
                entity.getY() + entity.getHeight() * 0.5, entity.getZ(), current)) {
                int marker = width + 1;
                OverlayRender.box(context, current[0] - marker, current[1] - marker,
                    current[0] + marker, current[1] + marker, SlEspStyle.argb(235, rgb), 1);
            }
        }
    }
}
