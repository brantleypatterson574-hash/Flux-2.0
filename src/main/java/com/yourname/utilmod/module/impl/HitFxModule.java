package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ColorUtil;
import com.yourname.utilmod.util.OverlayRender;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

/** Original hit confirmation effects, drawn entirely on the 2D HUD layer. */
public class HitFxModule extends Module implements HudOverlay {
    private enum Style { RING, CROSS, CORNERS }

    private static final int RING_SEGMENTS = 20;
    private static final double[] RING_COS = new double[RING_SEGMENTS];
    private static final double[] RING_SIN = new double[RING_SEGMENTS];
    static {
        for (int i = 0; i < RING_SEGMENTS; i++) {
            double angle = i * 2.0 * Math.PI / RING_SEGMENTS;
            RING_COS[i] = Math.cos(angle);
            RING_SIN[i] = Math.sin(angle);
        }
    }

    private final EnumSetting<Style> style = new EnumSetting<>("Style", Style.class, Style.RING)
        .description("Visual shape shown when one of your attacks connects.");
    private final NumberSetting duration = new NumberSetting("Duration (Milliseconds)", 260.0, 80.0, 900.0)
        .description("How long the hit effect stays on screen.");
    private final NumberSetting radius = new NumberSetting("Radius", 18.0, 6.0, 64.0)
        .description("Maximum size of the effect in pixels.");
    private final NumberSetting thickness = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Effect line thickness in pixels.");
    private final NumberSetting color = new NumberSetting("Color", 0x44FF88, 0x000000, 0xFFFFFF)
        .description("Effect colour. Double-click to type a hex value.");
    private final BooleanSetting onlyPlayers = new BooleanSetting("Only Players", false)
        .description("Only shows the effect when the attacked entity is a player.");
    private final BooleanSetting showTargetBox = new BooleanSetting("Target Box", true)
        .description("Outlines the entity that was hit for the duration of the effect.");

    private Entity hitEntity;
    private long startedAt;

    public HitFxModule() {
        super("Hit Color", "Shows a short colored confirmation effect whenever your attack connects.", Category.RENDER);
        addSetting(style);
        addSetting(duration);
        addSetting(radius);
        addSetting(thickness);
        addSetting(color);
        addSetting(onlyPlayers);
        addSetting(showTargetBox);
    }

    /** Called from the client attack mixin; no attack behaviour is changed. */
    public static void onAttack(PlayerEntity attacker, Entity target) {
        if (attacker == null || target == null || !UtilModClient.isModuleEnabled(HitFxModule.class)) return;
        HitFxModule module = get();
        if (module == null) return;
        if (module.onlyPlayers.isEnabled() && !(target instanceof PlayerEntity)) return;
        module.hitEntity = target;
        module.startedAt = System.currentTimeMillis();
    }

    private static HitFxModule get() {
        if (UtilModClient.getModules() == null) return null;
        for (var module : UtilModClient.getModules()) {
            if (module instanceof HitFxModule hitFx) return hitFx;
        }
        return null;
    }

    @Override
    public void onDisable() {
        hitEntity = null;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || hitEntity == null || hitEntity.isRemoved()
            || mc.options.hudHidden || mc.player == null) return;
        long age = System.currentTimeMillis() - startedAt;
        long total = (long) duration.getValue();
        if (age < 0 || age > total) {
            hitEntity = null;
            return;
        }
        int[] bounds = OverlayRender.entityBounds(hitEntity, 0.15f);
        if (bounds == null) return;
        float t = Math.min(1.0f, age / (float) Math.max(1L, total));
        int cx = (bounds[0] + bounds[2]) / 2;
        int cy = (bounds[1] + bounds[3]) / 2;
        int alpha = (int) (220 * (1.0f - t));
        int rgb = color.getColorValue();
        int lineWidth = Math.max(1, (int) thickness.getValue());
        int currentRadius = (int) (6 + radius.getValue() * t);

        if (showTargetBox.isEnabled()) {
            OverlayRender.cornerBox(context, bounds[0], bounds[1], bounds[2], bounds[3],
                ColorUtil.withAlpha(rgb, alpha), lineWidth, 0.28f);
        }
        switch (style.getValue()) {
            case RING -> drawRing(context, cx, cy, currentRadius, rgb, alpha, lineWidth);
            case CROSS -> {
                OverlayRender.line(context, cx - currentRadius, cy, cx + currentRadius, cy,
                    ColorUtil.withAlpha(rgb, alpha), lineWidth);
                OverlayRender.line(context, cx, cy - currentRadius, cx, cy + currentRadius,
                    ColorUtil.withAlpha(rgb, alpha), lineWidth);
            }
            case CORNERS -> OverlayRender.cornerBox(context,
                cx - currentRadius, cy - currentRadius, cx + currentRadius, cy + currentRadius,
                ColorUtil.withAlpha(rgb, alpha), lineWidth, 0.4f);
        }
    }

    private static void drawRing(DrawContext context, int cx, int cy, int r,
                                 int rgb, int alpha, int thickness) {
        int color = ColorUtil.withAlpha(rgb, alpha);
        for (int i = 0; i < RING_SEGMENTS; i++) {
            int next = (i + 1) % RING_SEGMENTS;
            OverlayRender.line(context,
                cx + (int) (RING_COS[i] * r), cy + (int) (RING_SIN[i] * r),
                cx + (int) (RING_COS[next] * r), cy + (int) (RING_SIN[next] * r),
                color, thickness);
        }
    }
}
