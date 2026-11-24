package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.EntityTargetCache;
import com.yourname.utilmod.util.EspOptionLists;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.Set;

/**
 * Clean name tags over entities. Uses the shared entity cache, so enabling this
 * alongside ESP / Tracers does not add another world scan.
 */
public class NameTagsModule extends Module implements HudOverlay {
    private final ChecklistSetting entities = new ChecklistSetting(
        "Entity Types", "nametags.entities", EspOptionLists.entityOptions(), EspOptionLists.defaultEntityIds()).description("Which entity types show a name tag.");
    private final NumberSetting range = new NumberSetting("Range", 64.0, 4.0, 256.0).description("Maximum distance at which name tags are drawn.");
    private final NumberSetting updateTicks = new NumberSetting("Update Interval", 2.0, 1.0, 20.0).description("Ticks between refreshes of the name tag list.");
    private final NumberSetting maxEntities = new NumberSetting("Maximum Entities", 96.0, 8.0, 256.0).description("Caps how many name tags are drawn at once.");
    private final BooleanSetting health = new BooleanSetting("Show Health", true).description("Shows health on each name tag.");
    private final BooleanSetting armor = new BooleanSetting("Armor", true).description("Shows the armour value on player name tags.");
    private final BooleanSetting distance = new BooleanSetting("Show Distance", true).description("Shows how far away each tagged entity is.");
    private final BooleanSetting background = new BooleanSetting("Background", true).description("Draws a shaded panel behind each name tag.");
    private final NumberSetting textColor = new NumberSetting("Text Color", 0xFFFFFF, 0x000000, 0xFFFFFF).description("Colour of the name tag text.");

    // Appended ScalableLux player-model visual rules. Original keys are unchanged.
    private final BooleanSetting depthColors = new BooleanSetting("Depth Colors", true)
        .description("Blends toward Far Color for distant name tags.");
    private final NumberSetting farColor = new NumberSetting("Far Color", 0x9FBBD9, 0x000000, 0xFFFFFF)
        .description("Colour used for name tags beyond Detail Distance.");
    private final NumberSetting detailDistance = new NumberSetting("Detail Distance", 48.0, 4.0, 256.0)
        .description("Distance where the far name colour replaces Text Color.");
    private final BooleanSetting softText = new BooleanSetting("Soft Text", true)
        .description("Draws name tags without the heavy vanilla text shadow.");
    private final BooleanSetting distanceFade = new BooleanSetting("Distance Fade", false)
        .description("Fades name tags toward the scan range.");
    private final NumberSetting fadeStartPercent = new NumberSetting("Fade Start %", 60.0, 0.0, 100.0)
        .unit("%")
        .description("Percentage of Range where Distance Fade begins.");
    private final NumberSetting fadeMinimumPercent = new NumberSetting("Fade Minimum %", 50.0, 0.0, 100.0)
        .unit("%")
        .description("Lowest name opacity, in percent, at the scan edge.");

    public NameTagsModule() {
        super("Nametags", "Draws readable name tags with health, armor and distance over chosen entity types.", Category.HUD);
        addSetting(entities);
        addSetting(range);
        addSetting(updateTicks);
        addSetting(maxEntities);
        addSetting(health);
        addSetting(armor);
        addSetting(distance);
        addSetting(background);
        addSetting(textColor);
        addSetting(depthColors);
        addSetting(farColor);
        addSetting(detailDistance);
        addSetting(softText);
        addSetting(distanceFade);
        addSetting(fadeStartPercent);
        addSetting(fadeMinimumPercent);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        EntityTargetCache.INSTANCE.remove(this);
    }

    private void onTick(MinecraftClient client) {
        EntityTargetCache.INSTANCE.configure(this, isEnabled(), entities.selectedView(),
            range.getValue(), (int) maxEntities.getValue(), (int) updateTicks.getValue());
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.world == null || mc.player == null || mc.options.hudHidden) return;
        List<Entity> cached = EntityTargetCache.INSTANCE.targets(mc);
        Set<String> selected = entities.selectedView();
        int baseRgb = ((int) textColor.getValue()) & 0xFFFFFF;
        double rangeSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);
        int centerX = mc.getWindow().getScaledWidth() / 2;

        for (Entity entity : cached) {
            if (entity.isRemoved() || mc.player.squaredDistanceTo(entity) > rangeSq) continue;
            if (!EntityTargetCache.matches(entity, selected)) continue;
            int[] projected = OverlayRender.project(
                entity.getX(), entity.getY() + entity.getHeight() + 0.45, entity.getZ());
            if (projected == null) continue;

            double entityDistance = mc.player.distanceTo(entity);
            int textRgb = depthColors.isEnabled()
                ? SlEspStyle.depthColor(baseRgb, farColor.getColorValue(),
                    entityDistance, detailDistance.getValue())
                : baseRgb;
            int fade = SlEspStyle.fadeAlphaPercent(entityDistance, range.getValue(),
                fadeStartPercent.getValue(), fadeMinimumPercent.getValue(), distanceFade.isEnabled());
            textRgb = SlEspStyle.argb(fade, textRgb);

            String label = entity.getName().getString();
            if (distance.isEnabled()) {
                label += "  " + OverlayRender.meters(entityDistance);
            }
            int width = mc.textRenderer.getWidth(label);
            int x = clamp(projected[0] - width / 2, 2, mc.getWindow().getScaledWidth() - width - 2);
            int y = clamp(projected[1], 2, mc.getWindow().getScaledHeight() - 24);
            if (background.isEnabled()) {
                context.fill(x - 3, y - 2, x + width + 3, y + 11, SlEspStyle.argb((fade * 0x90) / 255, 0x000000));
            }
            context.drawText(mc.textRenderer, label, x, y, textRgb, !softText.isEnabled());

            if (health.isEnabled() && entity instanceof LivingEntity living) {
                float fraction = living.getHealth() / Math.max(1.0f, living.getMaxHealth());
                OverlayRender.healthBar(context, x, y + 13, width, fraction, healthColor(fraction));
            }
            if (armor.isEnabled() && entity instanceof PlayerEntity player) {
                int armorValue = player.getArmor();
                if (armorValue > 0) {
                    OverlayRender.text(context, "Armor " + armorValue,
                        Math.max(2, centerX - 14), y + (health.isEnabled() ? 24 : 13), 0xFFB9D5FF);
                }
            }
        }
    }

    private static int healthColor(float fraction) {
        if (fraction > 0.6f) return 0x55FF55;
        if (fraction > 0.3f) return 0xFFFF55;
        return 0xFF5555;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
