/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiMaterial;
import com.ishland.scalablelux.util.CryptoVault;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public final class UiWaterMaterial {
    private static final class_2960 SHADER = class_2960.method_60655((String)UiWaterMaterial.qf("\u20e0\u4e00\u1fdd\ue6c2\u4a77\u3750\uc760\ufa2d\u014b\uabec\u2705\u61f0\uce1e", 40834158, 0xE89EF780 ^ CryptoVault.mask()), (String)UiWaterMaterial.qf("\u3207\ua89e\u67ff\uff62\u8dd1\u222f\uc077\u5faf\u6b3f\u4ecf\u5a44\u331c\ufa15\u597e\uab60\u3bc0\u7a5c\u93b5", 1174075316, 0xE89EF780 ^ CryptoVault.mask()));

    private UiWaterMaterial() {
    }

    public static UiMaterial of(float f2, float f3, float f4, float f5, int n2, int n3, int n4, int n5, float f6, float f7, boolean bl) {
        float[] fArray = UiWaterMaterial.rgb(n2);
        float[] fArray2 = UiWaterMaterial.rgb(n3);
        float[] fArray3 = UiWaterMaterial.rgb(n4);
        return new UiMaterial(SHADER, null, -1, f2, f3, f4, f5, fArray[0], fArray[1], fArray[2], UiWaterMaterial.clamp01(f6), fArray2[0], fArray2[1], fArray2[2], Math.max(1.0f, Math.min(3.0f, (float)n5)), fArray3[0], fArray3[1], fArray3[2], Math.max(0.05f, f7), bl ? 1.0f : 0.0f, 0.0f, 0.0f, 0.0f);
    }

    private static float[] rgb(int n2) {
        return new float[]{(float)(n2 >> 16 & 0xFF) / 255.0f, (float)(n2 >> 8 & 0xFF) / 255.0f, (float)(n2 & 0xFF) / 255.0f};
    }

    private static float clamp01(float f2) {
        return f2 < 0.0f ? 0.0f : (f2 > 1.0f ? 1.0f : f2);
    }

    private static /* synthetic */ String qf(String string, int n2, int n3) {
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

