package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.EntityTargetCache;
import com.yourname.utilmod.util.EspOptionLists;
import com.yourname.utilmod.util.GlowManager;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Prestige-style entity ESP using the shared entity cache and batched glow lines. */
public class ESPModule extends Module implements HudOverlay {
    private enum BoxMode { MODEL_GLOW, GLOW, FULL, CORNER, NONE }

    private final ChecklistSetting entities = new ChecklistSetting(
        "Entity Types", "esp.entities", EspOptionLists.entityOptions(), EspOptionLists.defaultEntityIds()).description("Which entity types get an ESP highlight.");
    private final NumberSetting range = new NumberSetting("Range", 64.0, 4.0, 256.0).description("Maximum distance at which entities are highlighted.");
    private final NumberSetting updateTicks = new NumberSetting("Update Interval", 2.0, 1.0, 20.0).description("Ticks between refreshes of the entity list.");
    private final NumberSetting maxEntities = new NumberSetting("Maximum Entities", 96.0, 8.0, 256.0).description("Caps how many entities can be highlighted at once.");
    private final EnumSetting<BoxMode> boxMode =
        new EnumSetting<>("Box Style", BoxMode.class, BoxMode.MODEL_GLOW).description("Draws entity outlines as a glow, full box, corners, or nothing.");
    private final BooleanSetting fill = new BooleanSetting("Fill", true).description("Fills entity outlines with a translucent colour.");
    private final NumberSetting fillAlpha = new NumberSetting("Fill Alpha", 55.0, 0.0, 160.0).description("Opacity of the filled entity boxes.");
    private final BooleanSetting glow = new BooleanSetting("Glow", true).description("Adds a soft glow around entity outlines.");
    private final BooleanSetting tracers = new BooleanSetting("Tracers", false).description("Draws lines from your crosshair to highlighted entities.");
    private final BooleanSetting names = new BooleanSetting("Show Names", true).description("Shows the entity name above its box.");
    private final BooleanSetting health = new BooleanSetting("Show Health", true).description("Shows a health value for living entities.");
    private final BooleanSetting distance = new BooleanSetting("Show Distance", true).description("Shows how far away each highlighted entity is.");
    private final BooleanSetting colorByType = new BooleanSetting("Color By Type", true)
        .description("Uses a fixed colour per entity category. Turn off to use the custom colour below.");
    private final NumberSetting color = new NumberSetting("Color", 0xFFFFFF, 0x000000, 0xFFFFFF)
        .description("Custom entity colour. Only used while Color By Type is off.");
    private final NumberSetting tracerColor = new NumberSetting("Tracer Color", 0x55FFFF, 0x000000, 0xFFFFFF).description("Colour of the lines pointing to entities.");
    private final NumberSetting lineThickness = new NumberSetting("Line Width", 2.0, 1.0, 5.0).description("Thickness of ESP outline and tracer lines.");

    // Appended ScalableLux ShaderESP / chams rules. Original keys are untouched.
    private final BooleanSetting chamsColors = new BooleanSetting("Chams Colors", true)
        .description("Blends Visible Color and Occluded Color with distance, like the ScalableLux chams depth colours.");
    private final NumberSetting visibleColor = new NumberSetting("Visible Color", 0xB5FFD9, 0x000000, 0xFFFFFF)
        .description("Colour used for nearby entities when Chams Colors is on.");
    private final NumberSetting occludedColor = new NumberSetting("Occluded Color", 0x4C6FA8, 0x000000, 0xFFFFFF)
        .description("Colour used for distant entities when Chams Colors is on.");
    private final BooleanSetting distanceFade = new BooleanSetting("Distance Fade", true)
        .description("Fades entity boxes toward the scan edge.");
    private final NumberSetting fadeStartPercent = new NumberSetting("Fade Start %", 45.0, 0.0, 100.0)
        .unit("%")
        .description("Percentage of Range where Distance Fade begins.");
    private final NumberSetting fadeMinimumPercent = new NumberSetting("Fade Minimum %", 35.0, 0.0, 100.0)
        .unit("%")
        .description("Lowest box opacity, in percent, at the scan edge.");
    private final NumberSetting glowLayers = new NumberSetting("Glow Layers", 2.0, 1.0, 4.0)
        .description("Number of expanding glow passes around each entity box.");
    private final NumberSetting glowScale = new NumberSetting("Glow Scale", 1.08, 1.0, 1.5)
        .description("How much each extra glow layer expands beyond the box.");

    // Reused every tick; GlowManager copies these into its own live map.
    private final Map<Entity, Integer> desiredGlow = new HashMap<>();

    public ESPModule() {
        super("Entity ESP", "Prestige-style entity ESP with a selectable list of every entity type.",
            Category.HUD);
        addSetting(entities);
        addSetting(range);
        addSetting(updateTicks);
        addSetting(maxEntities);
        addSetting(boxMode);
        addSetting(fill);
        addSetting(fillAlpha);
        addSetting(glow);
        addSetting(tracers);
        addSetting(names);
        addSetting(health);
        addSetting(distance);
        addSetting(colorByType);
        addSetting(color);
        addSetting(tracerColor);
        addSetting(lineThickness);
        addSetting(chamsColors);
        addSetting(visibleColor);
        addSetting(occludedColor);
        addSetting(distanceFade);
        addSetting(fadeStartPercent);
        addSetting(fadeMinimumPercent);
        addSetting(glowLayers);
        addSetting(glowScale);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        EntityTargetCache.INSTANCE.remove(this);
        GlowManager.clear();
    }

    private void onTick(MinecraftClient client) {
        boolean enabled = isEnabled();
        if (!enabled || client.world == null || client.player == null) {
            GlowManager.clear();
            return;
        }

        Set<String> selected = entities.selectedView();
        EntityTargetCache.INSTANCE.configure(this, true, selected,
            range.getValue(), (int) maxEntities.getValue(), (int) updateTicks.getValue());

        if (boxMode.getValue() != BoxMode.MODEL_GLOW) {
            GlowManager.clear();
            return;
        }

        desiredGlow.clear();
        double rangeSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);
        int baseRgb = ((int) color.getValue()) & 0xFFFFFF;
        for (Entity entity : EntityTargetCache.INSTANCE.targets(client)) {
            if (entity.isRemoved() || client.player.squaredDistanceTo(entity) > rangeSq) continue;
            if (!EntityTargetCache.matches(entity, selected)) continue;
            desiredGlow.put(entity, styledColor(entity, client.player.distanceTo(entity), baseRgb));
        }
        GlowManager.apply(desiredGlow);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.world == null || mc.player == null || mc.options.hudHidden) return;
        List<Entity> cached = EntityTargetCache.INSTANCE.targets(mc);
        if (cached.isEmpty()) return;

        Set<String> selected = entities.selectedView();
        int baseRgb = ((int) color.getValue()) & 0xFFFFFF;
        int traceRgb = ((int) tracerColor.getValue()) & 0xFFFFFF;
        int thickness = Math.max(1, (int) lineThickness.getValue());
        int alpha = Math.max(0, Math.min(255, (int) fillAlpha.getValue()));
        int centerX = mc.getWindow().getScaledWidth() / 2;
        int centerY = mc.getWindow().getScaledHeight() / 2;
        double rangeSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);
        boolean useGlow = glow.isEnabled();
        BoxMode mode = boxMode.getValue();
        boolean wantsLabels = names.isEnabled() || health.isEnabled() || distance.isEnabled();
        boolean wantsBox = mode != BoxMode.MODEL_GLOW && mode != BoxMode.NONE;
        boolean needsBounds = wantsBox || wantsLabels;

        for (Entity entity : cached) {
            if (entity.isRemoved() || mc.player.squaredDistanceTo(entity) > rangeSq) continue;
            if (!EntityTargetCache.matches(entity, selected)) continue;

            double entityDistance = mc.player.distanceTo(entity);
            int rgb = styledColor(entity, entityDistance, baseRgb);
            int fade = SlEspStyle.fadeAlphaPercent(entityDistance, range.getValue(),
                fadeStartPercent.getValue(), fadeMinimumPercent.getValue(), distanceFade.isEnabled());
            int fadedRgb = SlEspStyle.blend(0x000000, rgb, fade / 255.0f);
            int fadedFill = (int) Math.round(alpha * fade / 255.0);
            // MODEL_GLOW with tracers only needs no projected corners at all.
            int[] bounds = needsBounds ? OverlayRender.entityBounds(entity, 0.1f) : null;

            // MODEL_GLOW is drawn by the game's outline pass, so no 2D box here.
            if (bounds != null && mode != BoxMode.MODEL_GLOW) {
                drawStyledBox(context, bounds, fadedRgb, fadedFill, thickness, mode, useGlow);
            }

            if (tracers.isEnabled()) {
                int[] projected = OverlayRender.project(
                    entity.getX(), entity.getY() + entity.getHeight() * 0.5, entity.getZ()
                );
                if (projected != null) {
                    OverlayRender.line(context, centerX, centerY, projected[0], projected[1],
                        0xFF000000 | traceRgb, thickness);
                }
            }

            if (bounds != null) {
                int textY = bounds[1] - 2;
                if (names.isEnabled()) {
                    String label = entity instanceof ItemEntity item
                        ? item.getStack().getName().getString()
                        : entity.getName().getString();
                    OverlayRender.text(context, label, bounds[0], textY - 10, 0xFF000000 | rgb);
                    textY += 10;
                }
                if (health.isEnabled() && entity instanceof LivingEntity living) {
                    float max = Math.max(1.0f, living.getMaxHealth());
                    OverlayRender.healthBar(context, bounds[0], bounds[3] + 2,
                        Math.max(12, bounds[2] - bounds[0]), living.getHealth() / max,
                        healthColor(living));
                }
                if (distance.isEnabled()) {
                    OverlayRender.text(context, OverlayRender.meters(mc.player.distanceTo(entity)),
                        bounds[2] + 3, bounds[1], 0xDDFFFFFF);
                }
            }
        }
    }

    /** Depth-blended chams colour, falling back to the original category colours. */
    private int styledColor(Entity entity, double distance, int baseRgb) {
        if (!chamsColors.isEnabled()) return entityColor(entity, baseRgb);
        return SlEspStyle.depthColor(visibleColor.getColorValue(), occludedColor.getColorValue(),
            distance, range.getValue());
    }

    /** Box styles with the layered glow and distance fade from ScalableLux's ESP passes. */
    private void drawStyledBox(DrawContext context, int[] bounds, int rgb, int fillAlphaValue,
                               int thickness, BoxMode mode, boolean useGlow) {
        int x1 = bounds[0], y1 = bounds[1], x2 = bounds[2], y2 = bounds[3];
        switch (mode) {
            case FULL -> {
                if (fill.isEnabled() && fillAlphaValue > 0) {
                    context.fill(x1, y1, x2, y2, SlEspStyle.argb(fillAlphaValue, rgb));
                }
                if (useGlow) {
                    glowLayers(context, x1, y1, x2, y2, rgb, thickness);
                } else {
                    OverlayRender.box(context, x1, y1, x2, y2, 0xFF000000 | rgb, thickness);
                }
            }
            case CORNER -> {
                if (useGlow) {
                    glowLayers(context, x1, y1, x2, y2, rgb, thickness);
                } else {
                    OverlayRender.cornerBox(context, x1, y1, x2, y2,
                        0xFF000000 | rgb, thickness, 0.22f);
                }
            }
            case GLOW -> OverlayRender.prestigeBox(context, x1, y1, x2, y2, rgb,
                fill.isEnabled() ? fillAlphaValue : 0, useGlow);
            case NONE, MODEL_GLOW -> { }
        }
    }

    private void glowLayers(DrawContext context, int x1, int y1, int x2, int y2,
                            int rgb, int thickness) {
        int layers = Math.max(1, (int) glowLayers.getValue());
        float growth = (float) Math.max(0.0, glowScale.getValue() - 1.0);
        for (int layer = 0; layer < layers; layer++) {
            int expand = (int) Math.round(Math.min(x2 - x1, y2 - y1) * growth * layer * 0.5f);
            OverlayRender.glowBox(context, x1 - expand, y1 - expand,
                x2 + expand, y2 + expand, rgb, thickness);
        }
    }

    private int entityColor(Entity entity, int baseRgb) {
        if (!colorByType.isEnabled()) return baseRgb;
        if (entity instanceof PlayerEntity) return 0xFF5555;
        if (entity instanceof ItemEntity) return 0xFFD75F;
        return 0xFF9A5C;
    }

    private int healthColor(LivingEntity entity) {
        float fraction = entity.getHealth() / Math.max(1.0f, entity.getMaxHealth());
        if (fraction > 0.6f) return 0x55FF55;
        if (fraction > 0.3f) return 0xFFFF55;
        return 0xFF5555;
    }
}
