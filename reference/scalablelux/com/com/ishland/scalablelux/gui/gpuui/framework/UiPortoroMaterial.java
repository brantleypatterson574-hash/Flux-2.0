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
public final class UiPortoroMaterial {
    private static final class_2960 SHADER = class_2960.method_60655((String)UiPortoroMaterial.qc("\u9ad1\ud211\ue9ac\ue24e\uc550\uf3d5\u32e9\uaed4\u6ad8\u3dff\u6066\u93be", -1877700851, 0x4F6F4FC8 ^ CryptoVault.mask()), (String)UiPortoroMaterial.qc("\u935f\u8c4d\u1e09\ua646\u64c8\uc2b9\u4d6e\u64f4\u99c8\ueea2\uef11\uecd7\u32b4\ufab7\u395a\uc112\u323a\udabc\u3da7\u68cd\u45c0", 1234401350, 0x4F6F4FC8 ^ CryptoVault.mask()));
    private static final UiMaterial DEFAULT = UiPortoroMaterial.create(-1.0f, -1.0f, false, 0.0f);

    private UiPortoroMaterial() {
    }

    public static UiMaterial neroPortoro() {
        return DEFAULT;
    }

    public static UiMaterial neroPortoro(float f2, float f3, boolean bl, float f4) {
        return UiPortoroMaterial.create(f2, f3, bl, f4);
    }

    private static UiMaterial create(float f2, float f3, boolean bl, float f4) {
        return new UiMaterial(SHADER, null, -1, 0.76f, 0.62f, 0.78f, 0.34f, f2, f3, UiPortoroMaterial.clamp(f4, 0.0f, 1.0f), bl ? 1.0f : 0.0f);
    }

    private static float clamp(float f2, float f3, float f4) {
        return Float.isFinite(f2) ? Math.max(f3, Math.min(f2, f4)) : f3;
    }

    private static /* synthetic */ String qc(String string, int n2, int n3) {
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

