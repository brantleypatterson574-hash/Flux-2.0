/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.util.CryptoVault;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiColorPicker {
    private static final int[] PRESETS = new int[]{0xFFFFFF, 10134706, 1316906, 16731486, 16752474, 15914076, 6284193, 5232336, 5941759, 8160255, 11963647, 16740312};
    private static final int RECENT_MAX = 8;
    private static final ArrayDeque<Integer> RECENT = new ArrayDeque();
    private final UiTextField hex = new UiTextField(UiColorPicker.cw("\u0b18\u94e3\ua322\udbee\ub5f7\uffee\u49b1\u0d3a\u02c5\ub9fc\u8de6", -1376292000, 0x718E3C7 ^ CryptoVault.mask())).maxLength(9);
    private float hue;
    private float sat = 1.0f;
    private float val = 1.0f;
    private float alpha = 1.0f;
    private int lastArgb = 1193046;
    private boolean synced;
    private boolean showAlpha;
    private boolean compact;
    private Part part = Part.NONE;
    private float svX;
    private float svY;
    private float svW;
    private float svH;
    private float hueX;
    private float hueW;
    private float alX;
    private float alW;
    private IntConsumer target;
    private UiHost lastHost;

    public UiColorPicker() {
        this.hex.allow(n2 -> n2 == 35 || n2 < 128 && Character.digit(n2, 16) >= 0);
        this.hex.onChange(this::applyHex);
    }

    public UiColorPicker compact(boolean bl) {
        this.compact = bl;
        return this;
    }

    public void reset() {
        this.synced = false;
        this.part = Part.NONE;
    }

    public boolean active() {
        return this.part != Part.NONE;
    }

    public float draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, int n2, boolean bl, IntConsumer intConsumer) {
        float f8;
        int n3;
        this.lastHost = uiHost;
        this.target = intConsumer;
        this.showAlpha = bl;
        if (!(this.part != Part.NONE || this.synced && n2 == this.lastArgb)) {
            this.sync(n2);
        }
        float f9 = Math.min(f4, (float)Math.round(260.0f * f5));
        float f10 = Math.round(8.0f * f5 * Math.min(1.0f, uiHost.cornerScale()));
        int n4 = Color.HSBtoRGB(this.hue, 1.0f, 1.0f) & 0xFFFFFF;
        int n5 = this.rgb();
        this.svX = f2;
        this.svY = f3;
        this.svW = f9;
        this.svH = Math.round((this.compact ? 84.0f : 124.0f) * f5);
        builder.shadow(this.svX, this.svY, this.svW, this.svH, f10, Math.round(10.0f * f5), 0.0f, 0x50000000);
        builder.gradient(this.svX, this.svY, this.svW, this.svH, f10, -1, 0xFF000000 | n4, -1, 0xFF000000 | n4);
        builder.gradient(this.svX, this.svY, this.svW, this.svH, f10, 0, 0, -16777216, -16777216);
        builder.border(this.svX, this.svY, this.svW, this.svH, f10, Math.max(1.0f, f5), uiHost.lineColor());
        float f11 = this.svX + this.sat * this.svW;
        float f12 = this.svY + (1.0f - this.val) * this.svH;
        float f13 = Math.round(7.0f * f5);
        builder.circle(f11, f12 + Math.max(1.0f, f5), f13, 0x40000000);
        builder.circle(f11, f12, f13, -1);
        builder.circle(f11, f12, f13 - Math.max(1.5f, 2.0f * f5), 0xFF000000 | n5);
        uiHost.hit(this.svX, this.svY, this.svW, this.svH, () -> this.begin(Part.SV, uiHost.pressX(), uiHost.pressY()));
        float f14 = Math.round(12.0f * f5);
        float f15 = this.svY + this.svH + (float)Math.round(12.0f * f5);
        this.hueX = f2;
        this.hueW = f9;
        float f16 = f9 / 6.0f;
        for (int i2 = 0; i2 < 6; ++i2) {
            int n6 = Color.HSBtoRGB((float)i2 / 6.0f, 1.0f, 1.0f);
            n3 = Color.HSBtoRGB((float)(i2 + 1) / 6.0f, 1.0f, 1.0f);
            f8 = i2 == 0 || i2 == 5 ? f14 * 0.5f : 0.0f;
            builder.gradient(f2 + f16 * (float)i2, f15, f16 + 0.5f, f14, f8, 0xFF000000 | n6, 0xFF000000 | n3, 0xFF000000 | n6, 0xFF000000 | n3);
        }
        this.drawBarKnob(builder, f2 + this.hue * f9, f15 + f14 * 0.5f, f14, f5, n4);
        uiHost.hit(f2, f15 - 4.0f * f5, f9, f14 + 8.0f * f5, () -> this.begin(Part.HUE, uiHost.pressX(), uiHost.pressY()));
        float f17 = f15 + f14;
        if (this.showAlpha) {
            float f18 = f17 + (float)Math.round(10.0f * f5);
            this.alX = f2;
            this.alW = f9;
            n3 = Math.max(4, Math.round(f9 / (6.0f * f5)));
            f8 = f9 / (float)n3;
            for (int i3 = 0; i3 < n3; ++i3) {
                for (int i4 = 0; i4 < 2; ++i4) {
                    boolean bl2 = (i3 + i4 & 1) == 0;
                    builder.rect(f2 + (float)i3 * f8, f18 + (float)i4 * f14 * 0.5f, f8 + 0.5f, f14 * 0.5f, 0.0f, bl2 ? -12960444 : -10723224);
                }
            }
            builder.gradient(f2, f18, f9, f14, f14 * 0.5f, 0 | n5, 0xFF000000 | n5, 0 | n5, 0xFF000000 | n5);
            builder.border(f2, f18, f9, f14, f14 * 0.5f, Math.max(1.0f, f5), uiHost.lineColor());
            this.drawBarKnob(builder, f2 + this.alpha * f9, f18 + f14 * 0.5f, f14, f5, n5);
            uiHost.hit(f2, f18 - 4.0f * f5, f9, f14 + 8.0f * f5, () -> this.begin(Part.ALPHA, uiHost.pressX(), uiHost.pressY()));
            f17 = f18 + f14;
        }
        float f19 = f17 + (float)Math.round(12.0f * f5);
        float f20 = Math.round(28.0f * f5);
        builder.circle(f2 + f20 * 0.5f, f19 + f20 * 0.5f, f20 * 0.5f, 0xFF000000 | n5);
        builder.border(f2, f19, f20, f20, f20 * 0.5f, Math.max(1.0f, f5), uiHost.lineColor());
        this.hex.draw(builder, uiHost, f2 + f20 + (float)Math.round(8.0f * f5), f19, Math.min(f9 - f20 - 8.0f * f5, (float)Math.round(130.0f * f5)), f20, f5);
        f17 = f19 + f20;
        if (this.compact) {
            return f17 + (float)Math.round(10.0f * f5);
        }
        f8 = Math.round(8.0f * f5);
        float f21 = Math.round(21.0f * f5);
        float f22 = f2 + f8;
        float f23 = f17 + (float)Math.round(12.0f * f5) + f8;
        int n7 = n5;
        for (int i5 = 0; i5 < PRESETS.length + RECENT.size(); ++i5) {
            int n8;
            int n9 = n8 = i5 < PRESETS.length ? PRESETS[i5] : UiColorPicker.recentAt(i5 - PRESETS.length);
            if (i5 == PRESETS.length && !RECENT.isEmpty()) {
                f22 = f2 + f8;
                f23 += f21;
            }
            if (f22 + f8 > f2 + f9) {
                f22 = f2 + f8;
                f23 += f21;
            }
            boolean bl3 = n8 == n7;
            boolean bl4 = uiHost.hovered(f6, f7, f22 - f8, f23 - f8, f8 * 2.0f, f8 * 2.0f);
            if (bl3) {
                builder.circle(f22, f23, f8 + Math.max(2.0f, 2.5f * f5), UiKit.withAlpha(uiHost.accent(), 208));
            } else if (bl4) {
                builder.circle(f22, f23, f8 + Math.max(1.0f, 1.5f * f5), uiHost.controlFill(80));
            }
            builder.circle(f22, f23, f8, 0xFF000000 | n8);
            int n10 = n8;
            uiHost.hit(f22 - f8, f23 - f8, f8 * 2.0f, f8 * 2.0f, () -> {
                this.sync(this.alphaByte() << 24 | n10);
                this.emit();
                UiColorPicker.remember(n10);
            });
            f22 += f21;
        }
        return f23 + f8 + (float)Math.round(12.0f * f5);
    }

    public boolean drag(double d2, double d3) {
        if (this.part == Part.NONE) {
            return false;
        }
        this.update(d2, d3);
        return true;
    }

    public void release() {
        if (this.part == Part.NONE) {
            return;
        }
        this.part = Part.NONE;
        UiColorPicker.remember(this.rgb());
    }

    private void drawBarKnob(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, int n2) {
        builder.circle(f2, f3 + Math.max(1.0f, f5), f4 * 0.7f, 0x40000000);
        builder.circle(f2, f3, f4 * 0.7f, -1);
        builder.circle(f2, f3, f4 * 0.7f - Math.max(1.5f, 2.0f * f5), 0xFF000000 | n2);
    }

    private void begin(Part part, double d2, double d3) {
        this.part = part;
        this.update(d2, d3);
    }

    private void update(double d2, double d3) {
        switch (this.part.ordinal()) {
            case 1: {
                this.sat = UiColorPicker.clamp01((float)((d2 - (double)this.svX) / (double)Math.max(1.0f, this.svW)));
                this.val = 1.0f - UiColorPicker.clamp01((float)((d3 - (double)this.svY) / (double)Math.max(1.0f, this.svH)));
                break;
            }
            case 2: {
                this.hue = UiColorPicker.clamp01((float)((d2 - (double)this.hueX) / (double)Math.max(1.0f, this.hueW)));
                break;
            }
            case 3: {
                this.alpha = UiColorPicker.clamp01((float)((d2 - (double)this.alX) / (double)Math.max(1.0f, this.alW)));
                break;
            }
            default: {
                return;
            }
        }
        this.emit();
    }

    private void emit() {
        int n2;
        this.lastArgb = n2 = this.current();
        if (this.lastHost == null || !this.hex.focused(this.lastHost)) {
            this.hex.setValue(this.hexOf(n2));
        }
        if (this.target != null) {
            this.target.accept(n2);
        }
    }

    private void sync(int n2) {
        this.synced = true;
        this.lastArgb = n2;
        float[] fArray = Color.RGBtoHSB(n2 >> 16 & 0xFF, n2 >> 8 & 0xFF, n2 & 0xFF, null);
        if (fArray[1] > 0.001f && fArray[2] > 0.001f) {
            this.hue = fArray[0];
        }
        this.sat = fArray[1];
        this.val = fArray[2];
        float f2 = this.alpha = this.showAlpha ? (float)(n2 >>> 24 & 0xFF) / 255.0f : 1.0f;
        if (this.lastHost == null || !this.hex.focused(this.lastHost)) {
            this.hex.setValue(this.hexOf(n2));
        }
    }

    private void applyHex() {
        String string;
        String string2 = string = this.hex.value().startsWith("#") ? this.hex.value().substring(1) : this.hex.value();
        if (!(string.length() == 6 || this.showAlpha && string.length() == 8)) {
            return;
        }
        try {
            int n2;
            long l2 = Long.parseLong(string, 16);
            int n3 = string.length() == 8 ? (int)(l2 >> 8) & 0xFFFFFF : (int)l2 & 0xFFFFFF;
            int n4 = string.length() == 8 ? (int)(l2 & 0xFFL) : this.alphaByte();
            float[] fArray = Color.RGBtoHSB(n3 >> 16 & 0xFF, n3 >> 8 & 0xFF, n3 & 0xFF, null);
            if (fArray[1] > 0.001f && fArray[2] > 0.001f) {
                this.hue = fArray[0];
            }
            this.sat = fArray[1];
            this.val = fArray[2];
            this.alpha = this.showAlpha ? (float)n4 / 255.0f : 1.0f;
            this.lastArgb = n2 = this.current();
            if (this.target != null) {
                this.target.accept(n2);
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }

    private int rgb() {
        return Color.HSBtoRGB(this.hue, this.sat, this.val) & 0xFFFFFF;
    }

    private int alphaByte() {
        return this.showAlpha ? Math.round(this.alpha * 255.0f) : 255;
    }

    private int current() {
        return this.alphaByte() << 24 | this.rgb();
    }

    private String hexOf(int n2) {
        int n3 = n2 >>> 24 & 0xFF;
        if (this.showAlpha && n3 != 255) {
            return String.format(Locale.ROOT, UiColorPicker.cw("\ud91c\udf63\ucc09\u16fd\uf5e2\u3c6d\uf49f\u0198\u925e\ubb03\ue4c9\ueca2\u7d69", 721839093, 0x718E3C7 ^ CryptoVault.mask()), n2 & 0xFFFFFF, n3);
        }
        return String.format(Locale.ROOT, UiColorPicker.cw("\u8879\ud3aa\u2363\u043a\ua39b\u25f2", -1439727178, 0x718E3C7 ^ CryptoVault.mask()), n2 & 0xFFFFFF);
    }

    private static void remember(int n2) {
        n2 &= 0xFFFFFF;
        for (int n3 : PRESETS) {
            if (n3 != n2) continue;
            return;
        }
        RECENT.remove(n2);
        RECENT.addFirst(n2);
        while (RECENT.size() > 8) {
            RECENT.removeLast();
        }
    }

    private static int recentAt(int n2) {
        int n3 = 0;
        for (int n4 : RECENT) {
            if (n3++ != n2) continue;
            return n4;
        }
        return 0;
    }

    private static float clamp01(float f2) {
        return f2 < 0.0f ? 0.0f : Math.min(1.0f, f2);
    }

    private static /* synthetic */ String cw(String string, int n2, int n3) {
        int n4;
        long l2;
        int n5;
        int n6;
        char[] cArray = string.toCharArray();
        if (cArray.length == 0) {
            return "";
        }
        long l3 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
        l3 ^= 0x9E3779B97F4A7C15L;
        l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
        l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
        l3 ^= l3 >>> 31;
        long l4 = l3 ^ 0xA5A5A5A5A5A5A5A5L;
        l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
        l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
        if ((n6 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
            n6 = 19997;
        }
        long l5 = l3 ^ 0x5A5A5A5A5A5A5A5AL;
        l5 = (l5 ^ l5 >>> 30) * -4658895280553007687L;
        l5 = (l5 ^ l5 >>> 27) * -7723592293110705685L;
        l5 ^= l5 >>> 31;
        int n7 = (int)(l5 >>> 16) & 0xFFFF;
        int n8 = 1;
        for (n5 = 0; n5 < 5; ++n5) {
            n8 *= 2 - n6 * n8;
        }
        n5 = n8 & 0xFFFF;
        long l6 = (l3 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        long l7 = l2 = (l3 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        l7 = (l7 ^ l7 >>> 30) * -4658895280553007687L;
        l7 = (l7 ^ l7 >>> 27) * -7723592293110705685L;
        int n9 = n5 * (cArray[0] - n7 & 0xFFFF) & 0xFFFF;
        if ((n4 = n9 ^ (int)((l7 ^= l7 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
            return string;
        }
        char[] cArray2 = new char[n4];
        int n10 = 0;
        for (int i2 = 0; i2 < cArray.length - 1; ++i2) {
            int n11 = n5 * (cArray[i2 + 1] - n7 & 0xFFFF) & 0xFFFF;
            long l8 = l6 ^ (long)n10 * -7046029254386353131L;
            l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
            l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
            l8 ^= l8 >>> 31;
            int n12 = n11 ^ (int)(l8 >>> 48) & 0xFFFF;
            l6 = l6 * 6364136223846793005L + 1442695040888963407L;
            if (i2 < n4) {
                cArray2[i2] = (char)n12;
            }
            n10 = n11;
        }
        return new String(cArray2);
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Part {
        NONE,
        SV,
        HUE,
        ALPHA;


        private static /* synthetic */ String mv(String string, int n2, int n3) {
            int n4;
            long l2;
            int n5;
            int n6;
            char[] cArray = string.toCharArray();
            if (cArray.length == 0) {
                return "";
            }
            long l3 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
            l3 ^= 0x9E3779B97F4A7C15L;
            l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
            l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
            l3 ^= l3 >>> 31;
            long l4 = l3 ^ 0xA5A5A5A5A5A5A5A5L;
            l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
            l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
            if ((n6 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
                n6 = 19997;
            }
            long l5 = l3 ^ 0x5A5A5A5A5A5A5A5AL;
            l5 = (l5 ^ l5 >>> 30) * -4658895280553007687L;
            l5 = (l5 ^ l5 >>> 27) * -7723592293110705685L;
            l5 ^= l5 >>> 31;
            int n7 = (int)(l5 >>> 16) & 0xFFFF;
            int n8 = 1;
            for (n5 = 0; n5 < 5; ++n5) {
                n8 *= 2 - n6 * n8;
            }
            n5 = n8 & 0xFFFF;
            long l6 = (l3 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            long l7 = l2 = (l3 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            l7 = (l7 ^ l7 >>> 30) * -4658895280553007687L;
            l7 = (l7 ^ l7 >>> 27) * -7723592293110705685L;
            int n9 = n5 * (cArray[0] - n7 & 0xFFFF) & 0xFFFF;
            if ((n4 = n9 ^ (int)((l7 ^= l7 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
                return string;
            }
            char[] cArray2 = new char[n4];
            int n10 = 0;
            for (int i2 = 0; i2 < cArray.length - 1; ++i2) {
                int n11 = n5 * (cArray[i2 + 1] - n7 & 0xFFFF) & 0xFFFF;
                long l8 = l6 ^ (long)n10 * -7046029254386353131L;
                l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
                l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
                l8 ^= l8 >>> 31;
                int n12 = n11 ^ (int)(l8 >>> 48) & 0xFFFF;
                l6 = l6 * 6364136223846793005L + 1442695040888963407L;
                if (i2 < n4) {
                    cArray2[i2] = (char)n12;
                }
                n10 = n11;
            }
            return new String(cArray2);
        }
    }
}

