package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Focused combat overlay for crystals, projectiles, explosives and hostile players/mobs. */
public class ThreatRadarModule extends Module implements HudOverlay {
    private enum BoxStyle { CORNERS, FULL, GLOW }

    private final NumberSetting range = new NumberSetting("Range", 48.0, 8.0, 160.0).description("How far away combat threats are highlighted.");
    private final NumberSetting maximumThreats = new NumberSetting("Maximum Threats", 32.0, 4.0, 96.0).description("Caps how many threats are drawn at once.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 2.0, 1.0, 20.0).description("Ticks between threat searches.");
    private final BooleanSetting showCrystals = new BooleanSetting("Show End Crystals", true).description("Highlights nearby end crystals.");
    private final BooleanSetting showProjectiles = new BooleanSetting("Show Projectiles", true).description("Highlights arrows and other flying projectiles.");
    private final BooleanSetting showExplosives = new BooleanSetting("Show TNT", true).description("Highlights nearby or lit TNT.");
    private final BooleanSetting showHostiles = new BooleanSetting("Show Hostile Mobs", true).description("Highlights hostile mobs.");
    private final BooleanSetting showPlayers = new BooleanSetting("Show Other Players", true).description("Highlights other players.");
    private final EnumSetting<BoxStyle> boxStyle =
        new EnumSetting<>("Box Style", BoxStyle.class, BoxStyle.CORNERS).description("Draws threat boxes as corners, full outlines, or a glow.");
    private final BooleanSetting tracers = new BooleanSetting("Show Tracers", false).description("Draws lines from your crosshair to highlighted threats.");
    private final BooleanSetting showDistance = new BooleanSetting("Show Distance", true).description("Shows how far away each threat is.");
    private final BooleanSetting fadeWithDistance = new BooleanSetting("Fade With Distance", true).description("Makes threat highlights fade toward the edge of the scan range.");
    private final NumberSetting crystalColor = new NumberSetting("Crystal Color", 0xFF67E8F9, 0, 0xFFFFFF).description("Colour of end-crystal highlights.");
    private final NumberSetting projectileColor = new NumberSetting("Projectile Color", 0xFFFFD166, 0, 0xFFFFFF).description("Colour of projectile highlights.");
    private final NumberSetting explosiveColor = new NumberSetting("TNT Color", 0xFFFB7185, 0, 0xFFFFFF).description("Colour of TNT highlights.");
    private final NumberSetting hostileColor = new NumberSetting("Hostile Color", 0xFFC084FC, 0, 0xFFFFFF).description("Colour of hostile-mob highlights.");
    private final NumberSetting playerColor = new NumberSetting("Player Color", 0xFFFF5555, 0, 0xFFFFFF).description("Colour of other-player highlights.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0).description("Thickness of threat highlight lines.");

    private final List<Entity> targets = new ArrayList<>();
    private int tick;

    public ThreatRadarModule() {
        super("Threat Radar", "Highlights combat dangers around you. Only draws on your screen.",
            Category.RENDER);
        addSetting(range);
        addSetting(maximumThreats);
        addSetting(scanInterval);
        addSetting(showCrystals);
        addSetting(showProjectiles);
        addSetting(showExplosives);
        addSetting(showHostiles);
        addSetting(showPlayers);
        addSetting(boxStyle);
        addSetting(tracers);
        addSetting(showDistance);
        addSetting(fadeWithDistance);
        addSetting(crystalColor);
        addSetting(projectileColor);
        addSetting(explosiveColor);
        addSetting(hostileColor);
        addSetting(playerColor);
        addSetting(lineWidth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        targets.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            targets.clear();
            tick = 0;
            return;
        }
        if (tick++ % Math.max(1, (int) scanInterval.getValue()) != 0) return;
        targets.clear();

        double r = range.getValue();
        double rSq = r * r;
        Box area = new Box(
            client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
        );

        List<Entity> found = client.world.getOtherEntities(client.player, area, entity ->
            entity != null && !entity.isRemoved() && isThreat(entity)
                && client.player.squaredDistanceTo(entity) <= rSq
        );
        targets.addAll(found);
        targets.sort(Comparator.comparingDouble(client.player::squaredDistanceTo));
        int cap = Math.max(1, (int) maximumThreats.getValue());
        if (targets.size() > cap) {
            targets.subList(cap, targets.size()).clear();
        }
    }

    private boolean isThreat(Entity entity) {
        if (entity instanceof EndCrystalEntity) return showCrystals.isEnabled();
        if (entity instanceof TntEntity || entity instanceof TntMinecartEntity) return showExplosives.isEnabled();
        if (entity instanceof ProjectileEntity) return showProjectiles.isEnabled();
        if (entity instanceof PlayerEntity) return showPlayers.isEnabled();
        if (entity instanceof HostileEntity) return showHostiles.isEnabled();
        return false;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || targets.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int centerX = mc.getWindow().getScaledWidth() / 2;
        int centerY = mc.getWindow().getScaledHeight() / 2;
        int width = Math.max(1, (int) lineWidth.getValue());
        double r = range.getValue();

        for (Entity entity : targets) {
            if (entity.isRemoved()) continue;
            double distance = mc.player.distanceTo(entity);
            if (distance > r + 2.0) continue;

            int rgb = colorFor(entity);
            float fade = fadeWithDistance.isEnabled()
                ? SlEspStyle.fadeAlphaPercent(distance, r, 55.0, 40.0, true) / 255.0f
                : 1.0f;
            int drawnColor = SlEspStyle.blend(0x000000, rgb, fade);

            int[] bounds = OverlayRender.entityBounds(entity, 0.12f);
            if (bounds != null) {
                switch (boxStyle.getValue()) {
                    case CORNERS -> OverlayRender.cornerBox(context, bounds[0], bounds[1],
                        bounds[2], bounds[3], SlEspStyle.argb(220, drawnColor), width, 0.25f);
                    case FULL -> {
                        context.fill(bounds[0], bounds[1], bounds[2], bounds[3], SlEspStyle.argb(40, drawnColor));
                        OverlayRender.box(context, bounds[0], bounds[1], bounds[2], bounds[3],
                            SlEspStyle.argb(230, drawnColor), width);
                    }
                    case GLOW -> OverlayRender.prestigeBox(context, bounds[0], bounds[1],
                        bounds[2], bounds[3], drawnColor, (int) (50 * fade), true);
                }
            }

            if (tracers.isEnabled()) {
                int[] projected = OverlayRender.project(entity.getX(),
                    entity.getY() + entity.getHeight() * 0.5, entity.getZ());
                if (projected != null) {
                    OverlayRender.line(context, centerX, centerY, projected[0], projected[1],
                        SlEspStyle.argb((int) (180 * fade), drawnColor), Math.max(1, width - 1));
                }
            }

            if (showDistance.isEnabled() && bounds != null) {
                OverlayRender.text(context, OverlayRender.meters(distance), bounds[2] + 3, bounds[1],
                    SlEspStyle.argb((int) (220 * fade), 0xFFFFFF));
            }
        }
    }

    private int colorFor(Entity entity) {
        if (entity instanceof EndCrystalEntity) return crystalColor.getColorValue();
        if (entity instanceof TntEntity || entity instanceof TntMinecartEntity) return explosiveColor.getColorValue();
        if (entity instanceof ProjectileEntity) return projectileColor.getColorValue();
        if (entity instanceof PlayerEntity) return playerColor.getColorValue();
        return hostileColor.getColorValue();
    }
}
