/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiBounds;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiElement;
import com.ishland.scalablelux.gui.gpuui.framework.UiWidget;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.Locale;
import java.util.Objects;
import java.util.function.DoubleConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiSlider
extends UiElement
implements UiWidget {
    private final String label;
    private final double min;
    private final double max;
    private final DoubleConsumer onChange;
    private double value;
    private boolean dragging;

    public UiSlider(String string, String string2, double d2, double d3, double d4, DoubleConsumer doubleConsumer) {
        super(string);
        if (!Double.isFinite(d2) || !Double.isFinite(d3) || d3 < d2) {
            throw new IllegalArgumentException(UiSlider.la("\u9857\uacc9\ubf79\ua135\ud1e3\u7db3\u4b2f\u1ba8\u9800\ub877\u26fd\uee96\u0e4d\u90d4%\ua2fc\u258f\ua235\u228d\u3c9e\u1c80", 388527482, 0x70EB7C1B ^ CryptoVault.mask()));
        }
        this.label = Objects.requireNonNull(string2, UiSlider.la("\u92eb\u9851\u00bc\u51fd\u188f\uf728\u192e", -1168750620, 0x70EB7C1B ^ CryptoVault.mask()));
        this.min = d2;
        this.max = d3;
        this.onChange = Objects.requireNonNull(doubleConsumer, UiSlider.la("\u14e0\u698c\u63b7\uc0c8\uee13\ueca3\ucb7d\u7148\u5c75\u66c1\u80ff", 1933384133, 0x70EB7C1B ^ CryptoVault.mask()));
        this.setFocusable(true);
        this.setValue(d4, false);
    }

    public double value() {
        return this.value;
    }

    public void setValue(double d2) {
        this.setValue(d2, true);
    }

    private void setValue(double d2, boolean bl) {
        if (!Double.isFinite(d2)) {
            return;
        }
        this.value = Math.max(this.min, Math.min(this.max, d2));
        if (bl) {
            this.onChange.accept(this.value);
        }
    }

    @Override
    public void paint(UiDisplayList.Builder builder) {
        UiBounds uiBounds = this.bounds();
        builder.text(uiBounds.x(), uiBounds.y(), 9.0f, -1446411, this.label + "  " + String.format(Locale.ROOT, UiSlider.la("\u3432\u16e8\ua458\u0ded\ucb58", 711118127, 0x70EB7C1B ^ CryptoVault.mask()), this.value));
        float f2 = uiBounds.x();
        float f3 = uiBounds.y() + uiBounds.height() - 4.0f;
        float f4 = uiBounds.width();
        float f5 = this.max == this.min ? 0.0f : (float)((this.value - this.min) / (this.max - this.min));
        builder.rect(f2, f3, f4, 3.0f, 2.0f, -12892587);
        builder.rect(f2, f3, f4 * f5, 3.0f, 2.0f, -6521601);
    }

    @Override
    public boolean onPointerDown(float f2, float f3, int n2) {
        if (n2 != 0) {
            return false;
        }
        this.dragging = true;
        this.update(f2);
        return true;
    }

    @Override
    public boolean onPointerMove(float f2, float f3, float f4, float f5) {
        if (!this.dragging) {
            return false;
        }
        this.update(f2);
        return true;
    }

    @Override
    public boolean onPointerUp(float f2, float f3, int n2) {
        if (!this.dragging) {
            return false;
        }
        this.dragging = false;
        return true;
    }

    private void update(float f2) {
        UiBounds uiBounds = this.bounds();
        double d2 = uiBounds.width() <= 0.0f ? 0.0 : (double)Math.max(0.0f, Math.min(1.0f, (f2 - uiBounds.x()) / uiBounds.width()));
        this.setValue(this.min + d2 * (this.max - this.min));
    }

    private static /* synthetic */ String la(String string, int n2, int n3) {
        int n4;
        int n5;
        char[] cArray = string.toCharArray();
        if (cArray.length == 0) {
            return "";
        }
        long l2 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
        l2 ^= 0x9E3779B97F4A7C15L;
        l2 = (l2 ^ l2 >>> 30) * -4658895280553007687L;
        l2 = (l2 ^ l2 >>> 27) * -7723592293110705685L;
        l2 ^= l2 >>> 31;
        long l3 = l2 ^ 0x5A5A5A5A5A5A5A5AL;
        l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
        l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
        l3 ^= l3 >>> 31;
        int n6 = (int)(l3 >>> 16) & 0xFFFF;
        long l4 = l2 ^ 0xA5A5A5A5A5A5A5A5L;
        l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
        l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
        if ((n5 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
            n5 = 19997;
        }
        int n7 = n5;
        for (int i2 = 0; i2 < 4; ++i2) {
            n7 *= 2 - n5 * n7;
        }
        long l5 = (l2 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        long l6 = (l2 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        l6 = (l6 ^ l6 >>> 30) * -4658895280553007687L;
        l6 = (l6 ^ l6 >>> 27) * -7723592293110705685L;
        int n8 = (n7 &= 0xFFFF) * (cArray[0] - n6 & 0xFFFF) & 0xFFFF;
        if ((n4 = n8 ^ (int)((l6 ^= l6 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
            return string;
        }
        char[] cArray2 = new char[n4];
        int n9 = 0;
        for (int i3 = 0; i3 < cArray.length - 1; ++i3) {
            int n10 = n7 * (cArray[i3 + 1] - n6 & 0xFFFF) & 0xFFFF;
            long l7 = (long)n9 * -7046029254386353131L;
            long l8 = l5 ^ l7;
            l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
            l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
            l8 ^= l8 >>> 31;
            if (i3 < n4) {
                cArray2[i3] = (char)(n10 ^ (int)(l8 >>> 48) & 0xFFFF);
            }
            l5 = l5 * 6364136223846793005L + 1442695040888963407L;
            n9 = n10;
        }
        return new String(cArray2);
    }
}

