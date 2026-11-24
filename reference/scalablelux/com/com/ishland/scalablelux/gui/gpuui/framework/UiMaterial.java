/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.util.CryptoVault;
import java.util.Arrays;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public final class UiMaterial {
    private final class_2960 shader;
    private final class_2960 texture;
    private final float[] parameters;
    private final int tint;

    public UiMaterial(class_2960 class_29602, class_2960 class_29603, int n2, float ... fArray) {
        this.shader = Objects.requireNonNull(class_29602, UiMaterial.em("\u3db8\u8c2d\ue79d\u78f0\u0740\ue2b2\u0c6c", -1715761951, 0xF6FF4B1C ^ CryptoVault.mask()));
        this.texture = class_29603;
        this.tint = n2;
        if (fArray.length > 60) {
            throw new IllegalArgumentException(UiMaterial.em("\u42f5\u887a\u2ab7\u87de\u8c2b\u9be4\u34cb\ub0fd\uac47\u61af\ue681\u8c2f\u1de7\u15e7\u61df\u17aa\ubc87\u8ec4\u560f\u1273\u67ac\uf8bb\u547e\u7ca2\u7aac\u88ec\u88a4\ub02d\u6182\u1d16\u481f\u3ee8\u965d\u87b8\u847c\ufefc\ue6f6\uc335\u94c4\u94f2", 1815997611, 0xF6FF4B1C ^ CryptoVault.mask()));
        }
        for (float f2 : this.parameters = Arrays.copyOf(fArray, 60)) {
            if (Float.isFinite(f2)) continue;
            throw new IllegalArgumentException(UiMaterial.em("\u5fee\uc174\uaf1d\u399f\udc03\uc457\u16f8\u5521\uebdc\u3eac\u58f2\ub379\u41fc\u8d31\u24d4\ubb04\u9c22\ue7ab\uf178\ud8d1\u2d5a\u7eb8\u9732\u98ee\u5f54\u43f2\u3787\u526b", -730960121, 0xF6FF4B1C ^ CryptoVault.mask()));
        }
    }

    public class_2960 shader() {
        return this.shader;
    }

    public class_2960 texture() {
        return this.texture;
    }

    public int tint() {
        return this.tint;
    }

    public float parameter(int n2) {
        return this.parameters[n2];
    }

    public float[] parameters() {
        return (float[])this.parameters.clone();
    }

    private static /* synthetic */ String em(String string, int n2, int n3) {
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

