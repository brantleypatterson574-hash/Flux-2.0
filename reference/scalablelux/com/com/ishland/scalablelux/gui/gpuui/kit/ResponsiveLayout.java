/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ResponsiveLayout {
    public static final float BODY_TEXT_UNITS = 11.0f;
    public static final float SMALL_TEXT_UNITS = 8.5f;
    public static final float MIN_BODY_TEXT_PX = 12.0f;
    public static final float MIN_TOUCH_PX = 44.0f;
    public static final float COMPACT_RAIL_BREAKPOINT = 760.0f;
    public static final float HEADER_COMPACT_BREAKPOINT = 700.0f;
    public static final float RAIL_W = 150.0f;
    public static final float COMPACT_RAIL_W = 56.0f;
    public static final float COMPACT_MIN_WINDOW_W = 440.0f;
    public static final float MAX_WINDOW_W = 1400.0f;
    public static final float MAX_WINDOW_H = 1000.0f;
    public static final float MOBILE_MIN_CONTROL_UNITS = 28.0f;
    public static final float MOBILE_MIN_UNIT_PX = 1.6f;
    public static final float MOBILE_MAX_UNIT_PX = 6.0f;
    public static final float MOBILE_MAX_CONTENT_UNITS = 520.0f;
    public static final float HUD_LABEL_UNITS = 7.5f;
    public static final float HUD_BUTTON_UNITS = 20.0f;
    public static final float HUD_MIN_LABEL_PX = 13.0f;
    public static final float DRAG_SLOP_PX = 6.0f;
    public static final long FLING_RECENT_NS = 120000000L;
    public static final float FLING_MAX_PX_PER_S = 6000.0f;
    private static final float FLING_DECAY_PER_S = 3.5f;

    private ResponsiveLayout() {
    }

    public static float classicAutoScale(float f2, float f3) {
        float f4 = Math.max(0.7f, Math.min(3.2f, f2 / 800.0f)) * f3;
        return Math.max(f4, 1.0909091f);
    }

    public static float availableUnits(float f2, float f3) {
        return f2 / Math.max(0.05f, f3);
    }

    public static boolean compactRail(float f2) {
        return f2 < 760.0f;
    }

    public static boolean dockSidePanel(float f2, float f3, float f4) {
        return f2 >= f3 + f4;
    }

    public static float mobileUnitPx(float f2, float f3) {
        float f4 = Math.max(1.6f, Math.min(6.0f, f2 / 270.0f));
        return Math.max(f4 *= Math.max(0.05f, f3), 1.5714285f);
    }

    public static float hudChromeScale(float f2, float f3, float f4, boolean bl) {
        float f5 = Math.max(0.01f, f4);
        float f6 = Math.max(f2, 13.0f / (7.5f * f5));
        float f7 = 8.5f * Math.max(0.7f, Math.min(3.2f, f3 / 800.0f));
        f6 = Math.max(f6, f7 / (7.5f * f5));
        if (bl) {
            f6 = Math.max(f6, 44.0f / (20.0f * f5));
        }
        return f6;
    }

    public static float blendVelocity(float f2, float f3, float f4) {
        if (!(f4 > 0.0f)) {
            return f2;
        }
        float f5 = f3 / Math.max(f4, 0.001f);
        return f2 * 0.6f + f5 * 0.4f;
    }

    public static float flingFor(float f2, long l2) {
        if (l2 > 120000000L) {
            return 0.0f;
        }
        return Math.max(-6000.0f, Math.min(6000.0f, f2));
    }

    public static float decayFling(float f2, float f3, float f4) {
        float f5 = f2 * (float)Math.exp(-Math.max(0.0f, f3) * 3.5f);
        return Math.abs(f5) < f4 ? 0.0f : f5;
    }
}

