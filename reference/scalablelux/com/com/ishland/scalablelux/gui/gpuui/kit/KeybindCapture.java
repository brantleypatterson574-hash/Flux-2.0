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
public final class KeybindCapture {
    public static final long GUARD_MS = 200L;
    public static final int LAST_BUTTON = 7;
    private float x;
    private float y;
    private float w;
    private float h;
    private boolean set;
    private long startMs;

    public static int encodeMouse(int n2) {
        return -(n2 + 100);
    }

    public static int decodeMouse(int n2) {
        return -n2 - 100;
    }

    public static boolean isMouseCode(int n2) {
        if (n2 >= 0) {
            return false;
        }
        int n3 = KeybindCapture.decodeMouse(n2);
        return n3 >= 0 && n3 <= 7;
    }

    public void clear() {
        this.set = false;
    }

    public void mark(float f2, float f3, float f4, float f5, long l2) {
        this.x = f2;
        this.y = f3;
        this.w = f4;
        this.h = f5;
        this.startMs = l2;
        this.set = true;
    }

    public boolean armed() {
        return this.set;
    }

    public boolean onTarget(double d2, double d3) {
        return this.set && d2 >= (double)this.x && d3 >= (double)this.y && d2 <= (double)(this.x + this.w) && d3 <= (double)(this.y + this.h);
    }

    public Action action(int n2, double d2, double d3, long l2) {
        if (n2 < 0 || n2 > 7) {
            return Action.CANCEL;
        }
        if (n2 != 0) {
            return Action.BIND;
        }
        if (!this.onTarget(d2, d3)) {
            return Action.CANCEL;
        }
        return l2 - this.startMs >= 200L ? Action.BIND : Action.IGNORE;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Action {
        CANCEL,
        IGNORE,
        BIND;


        private static /* synthetic */ String um(String string, int n2, int n3) {
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

