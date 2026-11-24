package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.util.ColorUtil;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Crosshair target card with an optional ESP box - a familiar Prestige/Flux HUD piece. */
public class TargetHudModule extends Module implements HudOverlay {
    private enum Priority { CROSSHAIR, NEAREST, LOWEST_HEALTH }

    private final EnumSetting<Priority> priority = new EnumSetting<>("Priority", Priority.class, Priority.CROSSHAIR)
        .description("Crosshair uses what you are actually looking at; the other modes pick a target inside the FOV.");
    private final NumberSetting range = new NumberSetting("Range", 12.0, 3.0, 48.0)
        .description("Maximum target distance for the FOV fallback modes.");
    private final NumberSetting fov = new NumberSetting("View Angle", 90.0, 20.0, 360.0)
        .description("Angle in degrees used by Nearest and Lowest Health targeting.");
    private final BooleanSetting players = new BooleanSetting("Players", true)
        .description("Allows players as targets.");
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true)
        .description("Allows non-player mobs as targets.");
    private final BooleanSetting showBackground = new BooleanSetting("Background", true)
        .description("Draws a themed panel behind the target card.");
    private final BooleanSetting showArmor = new BooleanSetting("Armor", true)
        .description("Shows the target's armour value.");
    private final BooleanSetting showDistance = new BooleanSetting("Show Distance", true)
        .description("Shows the target's distance in metres.");
    private final BooleanSetting highlight = new BooleanSetting("Highlight Target", true)
        .description("Draws an ESP box around the current target.");
    private final NumberSetting color = new NumberSetting("Color", 0xFF5555, 0x000000, 0xFFFFFF)
        .description("Accent colour for the card and target box.");
    private final NumberSetting x = new NumberSetting("X", 4.0, 0.0, 1000.0)
        .description("Horizontal position of the target card.").advanced();
    private final NumberSetting y = new NumberSetting("Y", 160.0, 0.0, 1000.0)
        .description("Vertical position of the target card.").advanced();

    private LivingEntity target;
    private String cachedName = "";
    private String cachedLine2 = "";
    private int cachedWidth;

    public TargetHudModule() {
        super("Target HUD", "Shows a live health card and ESP box for the entity you are targeting.", Category.HUD);
        addSetting(priority);
        addSetting(range);
        addSetting(fov);
        addSetting(players);
        addSetting(mobs);
        addSetting(showBackground);
        addSetting(showArmor);
        addSetting(showDistance);
        addSetting(highlight);
        addSetting(color);
        addSetting(x);
        addSetting(y);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        target = null;
        cachedName = "";
        cachedLine2 = "";
        cachedWidth = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        LivingEntity found = null;

        if (priority.getValue() == Priority.CROSSHAIR
            && client.crosshairTarget instanceof EntityHitResult hit
            && hit.getEntity() instanceof LivingEntity living && isValid(client.player, living)) {
            found = living;
        }

        if (found == null) {
            double r = range.getValue();
            Box box = new Box(
                client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
                client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
            );
            List<LivingEntity> candidates = new ArrayList<>(
                client.world.getEntitiesByClass(LivingEntity.class, box,
                    entity -> isValid(client.player, entity))
            );
            candidates.removeIf(entity -> Math.abs(
                wrapDegrees(yawTo(client.player, entity) - client.player.getYaw())
            ) > fov.getValue() / 2.0);
            if (!candidates.isEmpty()) {
                if (priority.getValue() == Priority.LOWEST_HEALTH) {
                    candidates.sort(Comparator.comparingDouble(LivingEntity::getHealth));
                } else {
                    candidates.sort(Comparator.comparingDouble(client.player::squaredDistanceTo));
                }
                found = candidates.get(0);
            }
        }

        target = found != null && !found.isDead() ? found : null;
        if (target == null) {
            cachedName = "";
            cachedLine2 = "";
            cachedWidth = 0;
            return;
        }

        cachedName = target.getName().getString();
        cachedLine2 = String.format("%.1f / %.1f HP", target.getHealth(), target.getMaxHealth());
        if (showArmor.isEnabled()) cachedLine2 += "   Armor " + target.getArmor();
        if (showDistance.isEnabled()) {
            cachedLine2 += "   " + OverlayRender.meters(client.player.distanceTo(target));
        }
        cachedWidth = Math.max(client.textRenderer.getWidth(cachedName),
            client.textRenderer.getWidth(cachedLine2)) + 16;
    }

    private boolean isValid(PlayerEntity self, LivingEntity entity) {
        if (entity == self || entity.isDead() || entity.getHealth() <= 0 || entity.isSpectator()) return false;
        return entity instanceof PlayerEntity ? players.isEnabled() : mobs.isEnabled();
    }

    private static float yawTo(PlayerEntity from, LivingEntity to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return (float) (Math.atan2(-dx, dz) * 180.0 / Math.PI);
    }

    private static float wrapDegrees(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) angle -= 360.0f;
        if (angle < -180.0f) angle += 360.0f;
        return angle;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden || mc.player == null || mc.world == null || target == null) return;
        // Identity check by entity id is enough to confirm the cached target is
        // still this live entity; the old code allocated a Box and a list per frame.
        if (target.isDead() || mc.world.getEntityById(target.getId()) != target) return;

        int rgb = color.getColorValue();
        if (highlight.isEnabled()) {
            int[] bounds = OverlayRender.entityBounds(target, 0.1f);
            if (bounds != null) {
                OverlayRender.prestigeBox(context, bounds[0], bounds[1], bounds[2], bounds[3], rgb, 35);
            }
        }

        int startX = (int) x.getValue();
        int startY = (int) y.getValue();
        int width = cachedWidth;

        GuiTheme theme = GuiThemeModule.INSTANCE != null
            ? GuiThemeModule.INSTANCE.getTheme() : GuiTheme.FLUX;
        if (showBackground.isEnabled()) {
            context.fill(startX - 4, startY - 4, startX + width + 4, startY + 38,
                ColorUtil.withAlpha(theme.panel, 0xC0));
            context.fill(startX - 4, startY - 4, startX - 1, startY + 38, ColorUtil.withAlpha(rgb, 255));
        }
        context.drawTextWithShadow(mc.textRenderer, cachedName, startX + 4, startY, ColorUtil.withAlpha(rgb, 255));
        context.drawTextWithShadow(mc.textRenderer, cachedLine2, startX + 4, startY + 12, theme.textSecondary);
        OverlayRender.healthBar(context, startX + 4, startY + 26, width - 8,
            target.getHealth() / Math.max(1.0f, target.getMaxHealth()), rgb);
    }
}
