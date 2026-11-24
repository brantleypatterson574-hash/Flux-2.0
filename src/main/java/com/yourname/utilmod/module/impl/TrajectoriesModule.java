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
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Sketch-style flight guides for throwable items and for projectiles that are
 * already in the world. The prediction is a simple local calculation from the
 * player's view direction; no item is used and no packet is sent.
 */
public class TrajectoriesModule extends Module implements HudOverlay {
    private static final double BASE_SPEED = 1.5;

    private final BooleanSetting showWindCharges = new BooleanSetting("Show Wind Charges", true)
        .description("Draws a guide when you are holding a wind charge.");
    private final BooleanSetting showEnderPearls = new BooleanSetting("Show Ender Pearls", true)
        .description("Draws a guide when you are holding an ender pearl.");
    private final BooleanSetting showSnowballs = new BooleanSetting("Show Snowballs", true)
        .description("Draws a guide when you are holding a snowball.");
    private final BooleanSetting showEggs = new BooleanSetting("Show Eggs", true)
        .description("Draws a guide when you are holding an egg.");
    private final BooleanSetting showLiveProjectiles = new BooleanSetting("Show Live Projectiles", true)
        .description("Marks projectiles already in the world using positions visible to the client.");
    private final NumberSetting steps = new NumberSetting("Steps", 48.0, 8.0, 160.0)
        .description("How many simulation ticks the held-item guide covers.");
    private final NumberSetting gravity = new NumberSetting("Gravity", 0.03, 0.0, 0.12)
        .description("Approximate downward acceleration added on each simulation step.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Thickness of each flight guide in screen pixels.");
    private final NumberSetting color = new NumberSetting("Color", 0xFDE68A, 0x000000, 0xFFFFFF)
        .description("Colour of the predicted flight guides.");

    private final List<ProjectileEntity> liveProjectiles = new ArrayList<>();
    private ClientWorld lastWorld;

    public TrajectoriesModule() {
        super("Trajectories", "Predicts throwable flight paths and marks nearby projectiles. Visual only. Math, but with confetti.",
            Category.RENDER);
        addSetting(showWindCharges);
        addSetting(showEnderPearls);
        addSetting(showSnowballs);
        addSetting(showEggs);
        addSetting(showLiveProjectiles);
        addSetting(steps);
        addSetting(gravity);
        addSetting(lineWidth);
        addSetting(color);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        liveProjectiles.clear();
        lastWorld = null;
    }

    private void onTick(MinecraftClient client) {
        liveProjectiles.clear();
        if (!isEnabled() || client.world == null || client.player == null) {
            lastWorld = null;
            return;
        }
        if (client.world != lastWorld) {
            lastWorld = client.world;
            liveProjectiles.clear();
        }
        if (!showLiveProjectiles.isEnabled()) return;

        double range = Math.min(128.0, Math.max(16.0, steps.getValue() * 0.8));
        Box area = new Box(
            client.player.getX() - range, client.player.getY() - range, client.player.getZ() - range,
            client.player.getX() + range, client.player.getY() + range, client.player.getZ() + range);
        for (Entity entity : client.world.getOtherEntities(client.player, area,
            candidate -> candidate instanceof ProjectileEntity)) {
            liveProjectiles.add((ProjectileEntity) entity);
            if (liveProjectiles.size() >= 64) break;
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int width = Math.max(1, (int) lineWidth.getValue());
        drawHeldGuide(context, rgb, width);
        drawLiveProjectiles(context, rgb, width);
    }

    private void drawHeldGuide(DrawContext context, int rgb, int width) {
        ItemStack held = mc.player.getMainHandStack();
        if (held.isEmpty()) return;

        double speed = approximateSpeed(held.getItem());
        if (speed < 0.0) return;

        Vec3d eye = mc.player.getEyePos();
        // getRotationVec uses the player's current yaw and pitch, which follow the
        // camera. The rest of the simulation is plain arithmetic, entirely local.
        Vec3d look = mc.player.getRotationVec(1.0f);
        double vx = look.x * speed;
        double vy = look.y * speed;
        double vz = look.z * speed;
        double drop = gravity.getValue();

        int count = Math.max(2, (int) steps.getValue());
        int[] previous = new int[3];
        int[] current = new int[3];
        boolean hasPrevious = OverlayRender.projectInto(eye.x, eye.y, eye.z, previous);

        for (int i = 0; i < count; i++) {
            vy -= drop;
            eye = eye.add(vx, vy, vz);
            boolean visible = OverlayRender.projectInto(eye.x, eye.y, eye.z, current);
            if (visible && hasPrevious) {
                int alpha = (int) (215 - 175 * (i / (float) count));
                OverlayRender.line(context, previous[0], previous[1], current[0], current[1],
                    SlEspStyle.argb(alpha, rgb), width);
            }
            if (visible) {
                previous[0] = current[0];
                previous[1] = current[1];
                hasPrevious = true;
            } else {
                hasPrevious = false;
            }
        }
    }

    private void drawLiveProjectiles(DrawContext context, int rgb, int width) {
        if (!showLiveProjectiles.isEnabled() || liveProjectiles.isEmpty()) return;
        int marker = width + 2;
        for (ProjectileEntity projectile : liveProjectiles) {
            if (projectile.isRemoved()) continue;
            if (!OverlayRender.projectInto(projectile.getX(), projectile.getY(), projectile.getZ(), markerScratch)) continue;
            OverlayRender.box(context, markerScratch[0] - marker, markerScratch[1] - marker,
                markerScratch[0] + marker, markerScratch[1] + marker,
                SlEspStyle.argb(225, rgb), Math.max(1, width - 1));
        }
    }

    private final int[] markerScratch = new int[3];

    /** Returns an approximate speed in blocks per step, or -1 for other items. */
    private double approximateSpeed(Item item) {
        if (item == Items.WIND_CHARGE && showWindCharges.isEnabled()) return BASE_SPEED * 1.1;
        if (item == Items.ENDER_PEARL && showEnderPearls.isEnabled()) return BASE_SPEED;
        if (item == Items.SNOWBALL && showSnowballs.isEnabled()) return BASE_SPEED;
        if (item == Items.EGG && showEggs.isEnabled()) return BASE_SPEED;
        return -1.0;
    }
}
