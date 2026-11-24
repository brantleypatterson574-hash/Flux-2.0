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
public final class UiSmoothScroll {
    private static final float FOLLOW_RATE = 18.0f;
    private static final long BAR_VISIBLE_NANOS = 900000000L;
    private float target;
    private float pos;
    private float max;
    private float view;
    private float content;
    private long lastActivityNanos = -4611686018427387904L;

    public float offset() {
        return Math.round(this.pos);
    }

    public float target() {
        return this.target;
    }

    public float max() {
        return this.max;
    }

    public boolean scrollable() {
        return this.max > 0.5f;
    }

    public void reset() {
        this.target = 0.0f;
        this.pos = 0.0f;
    }

    public void wheel(double d2, float f2, long l2) {
        if (d2 == 0.0 || !Float.isFinite((float)d2)) {
            return;
        }
        this.target = UiSmoothScroll.clamp(this.target - (float)d2 * f2, 0.0f, Math.max(this.max, 0.0f));
        this.lastActivityNanos = l2;
    }

    public void scrollTo(float f2, boolean bl) {
        this.target = UiSmoothScroll.clamp(f2, 0.0f, Math.max(this.max, 0.0f));
        if (bl) {
            this.pos = this.target;
        }
    }

    public void update(float f2, float f3, float f4, long l2) {
        this.view = Math.max(0.0f, f3);
        this.content = Math.max(0.0f, f4);
        this.max = Math.max(0.0f, this.content - this.view);
        this.target = UiSmoothScroll.clamp(this.target, 0.0f, this.max);
        float f5 = 1.0f - (float)Math.exp(-Math.max(0.0f, f2) * 18.0f);
        this.pos += (this.target - this.pos) * f5;
        if (Math.abs(this.target - this.pos) < 0.3f) {
            this.pos = this.target;
        }
        this.pos = UiSmoothScroll.clamp(this.pos, 0.0f, this.max);
        if (this.pos != this.target) {
            this.lastActivityNanos = l2;
        }
    }

    public void paintBar(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, int n2, long l2) {
        if (this.max <= 0.5f || f4 <= 0.0f) {
            return;
        }
        float f6 = (float)(l2 - this.lastActivityNanos) / 9.0E8f;
        float f7 = UiSmoothScroll.clamp(1.2f - f6, 0.28f, 1.0f);
        float f8 = Math.max(2.0f, (float)Math.round(3.0f * f5));
        float f9 = Math.max(28.0f * f5, f4 * this.view / Math.max(this.content, 1.0f));
        float f10 = f3 + (f4 - f9) * (this.max <= 0.0f ? 0.0f : this.pos / this.max);
        int n3 = Math.round(144.0f * f7);
        builder.rect(f2, Math.round(f10), f8, Math.round(f9), f8 * 0.5f, n3 << 24 | n2 & 0xFFFFFF);
    }

    private static float clamp(float f2, float f3, float f4) {
        return Math.max(f3, Math.min(f4, f2));
    }
}

