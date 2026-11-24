/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11909
 *  net.minecraft.class_11910
 *  net.minecraft.class_364
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.PlatformPreset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_11910;
import net.minecraft.class_364;

@Environment(value=EnvType.CLIENT)
public final class TouchInput {
    private static final double SLOP = 6.0;
    private static final long LONG_PRESS_NS = 450000000L;
    private final double pixelsPerNotch;
    private boolean dispatching;
    private boolean down;
    private boolean decided;
    private boolean scrolling;
    private class_11909 press;
    private boolean pressDoubled;
    private long pressAt;
    private double moveX;
    private double moveY;

    public TouchInput(double d2) {
        this.pixelsPerNotch = d2;
    }

    public static boolean enabled() {
        return PlatformPreset.touchUi() || Boolean.getBoolean(TouchInput.hm("\u210f\uc424\u51a7\u49fb\uf2a2\ub3cd\u0a14\u417a\u340d\ub3a0\u5b1d\uceb1\u82d1\u0f91", -564666491, 0x1055EB05 ^ CryptoVault.mask()));
    }

    public boolean intercepts(class_11909 class_119092) {
        return !this.dispatching && class_119092.method_74245() == 0 && TouchInput.enabled();
    }

    public boolean press(class_11909 class_119092, boolean bl) {
        this.down = true;
        this.decided = false;
        this.scrolling = false;
        this.press = class_119092;
        this.pressDoubled = bl;
        this.pressAt = System.nanoTime();
        this.moveX = 0.0;
        this.moveY = 0.0;
        return true;
    }

    public boolean drag(class_364 class_3642, class_11909 class_119092, double d2, double d3) {
        if (!this.down) {
            return false;
        }
        if (!this.decided) {
            this.moveX += d2;
            this.moveY += d3;
            if (Math.abs(this.moveX) < 6.0 && Math.abs(this.moveY) < 6.0) {
                return true;
            }
            this.decided = true;
            boolean bl = this.scrolling = Math.abs(this.moveY) >= Math.abs(this.moveX);
            if (this.scrolling) {
                this.scroll(class_3642, class_119092.comp_4798(), class_119092.comp_4799(), this.moveY);
            } else {
                this.dispatch(() -> class_3642.method_25402(this.press, this.pressDoubled));
                this.dispatch(() -> class_3642.method_25403(class_119092, this.moveX, this.moveY));
            }
            return true;
        }
        if (this.scrolling) {
            this.scroll(class_3642, class_119092.comp_4798(), class_119092.comp_4799(), d3);
            return true;
        }
        this.dispatch(() -> class_3642.method_25403(class_119092, d2, d3));
        return true;
    }

    public boolean release(class_364 class_3642, class_11909 class_119092) {
        if (!this.down) {
            return false;
        }
        this.down = false;
        if (this.decided) {
            if (!this.scrolling) {
                this.dispatch(() -> class_3642.method_25406(class_119092));
            }
            return true;
        }
        if (System.nanoTime() - this.pressAt >= 450000000L) {
            class_11909 class_119093 = new class_11909(this.press.comp_4798(), this.press.comp_4799(), new class_11910(1, this.press.comp_4797()));
            this.dispatch(() -> class_3642.method_25402(class_119093, false));
            this.dispatch(() -> class_3642.method_25406(class_119093));
        } else {
            this.dispatch(() -> class_3642.method_25402(this.press, this.pressDoubled));
            this.dispatch(() -> class_3642.method_25406(class_119092));
        }
        return true;
    }

    public void reset() {
        this.down = false;
        this.decided = false;
        this.scrolling = false;
    }

    private void scroll(class_364 class_3642, double d2, double d3, double d4) {
        double d5 = d4 / this.pixelsPerNotch;
        if (d5 != 0.0) {
            this.dispatch(() -> class_3642.method_25401(d2, d3, 0.0, d5));
        }
    }

    private void dispatch(Runnable runnable) {
        boolean bl = this.dispatching;
        this.dispatching = true;
        try {
            runnable.run();
        }
        finally {
            this.dispatching = bl;
        }
    }

    private static /* synthetic */ String hm(String string, int n2, int n3) {
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

