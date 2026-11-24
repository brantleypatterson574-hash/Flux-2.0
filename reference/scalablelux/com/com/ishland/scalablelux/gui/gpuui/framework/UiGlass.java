/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiGlass {
    public static final float DEFAULT_BLUR = 16.0f;
    public static final int DEFAULT_BODY_RGB = 329740;

    private UiGlass() {
    }

    public static void pane(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        UiGlass.pane(builder, f2, f3, f4, f5, f6, f7, 16.0f, UiGlass.argb(68.0f * f8, 329740), -1);
    }

    public static void pane(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2, int n3) {
        UiGlass.pane(builder, f2, f3, f4, f5, f6, f7, f8, n2, n3, true, true, 1.0f);
    }

    public static void pane(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2, int n3, boolean bl, boolean bl2, float f9) {
        if (!(f4 > 0.0f) || !(f5 > 0.0f)) {
            return;
        }
        float f10 = Math.max(0.0f, Math.min(1.0f, f7));
        float f11 = f10 * Math.max(0.0f, Math.min(2.0f, f9));
        int n4 = n3 & 0xFFFFFF;
        if (bl2) {
            builder.shadow(f2, f3 + 3.0f, f4, f5, f6, 12.0f, 0.0f, UiGlass.tint(0x50000000, f10));
        }
        builder.glass(f2, f3, f4, f5, f6, f8, UiGlass.tint(n2, f10));
        builder.gradient(f2, f3, f4, f5, f6, UiGlass.tint(0x30000000 | n4, f11), UiGlass.tint(0x18000000 | n4, f11), UiGlass.tint(0x4000000 | n4, f11), UiGlass.tint(0xA000000 | n4, f11));
        builder.gradient(f2 + 1.0f, f3 + 1.0f, f4 - 2.0f, Math.max(1.0f, f5 * 0.45f), Math.max(0.0f, f6 - 1.0f), UiGlass.tint(0x1A000000 | n4, f11), UiGlass.tint(0x1A000000 | n4, f11), UiGlass.tint(n4, f11), UiGlass.tint(n4, f11));
        if (bl) {
            builder.border(f2, f3, f4, f5, f6, 0.9f, UiGlass.tint(0x50000000 | n4, f11));
            builder.border(f2 + 1.0f, f3 + 1.0f, f4 - 2.0f, f5 - 2.0f, Math.max(0.0f, f6 - 1.0f), 0.6f, UiGlass.tint(0x12000000 | n4, f11));
        }
    }

    public static int argb(float f2, int n2) {
        return Math.max(0, Math.min(255, Math.round(f2))) << 24 | n2 & 0xFFFFFF;
    }

    public static int tint(int n2, float f2) {
        int n3 = Math.round((float)(n2 >>> 24 & 0xFF) * Math.max(0.0f, Math.min(1.0f, f2)));
        return n3 << 24 | n2 & 0xFFFFFF;
    }
}

