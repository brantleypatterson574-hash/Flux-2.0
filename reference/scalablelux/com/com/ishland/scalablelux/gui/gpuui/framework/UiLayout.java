/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiBounds;
import com.ishland.scalablelux.gui.gpuui.framework.UiElement;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiLayout {
    private UiLayout() {
    }

    public static void vertical(UiElement uiElement, float f2, float f3, float f4, float f5, List<Float> list) {
        if (list.size() != uiElement.children().size()) {
            throw new IllegalArgumentException(UiLayout.sl("\udb37\u7ef3\uaf4f\u95c3\u7d1c\u2908\uee59\u4de7\ua38c\u445a\u973f\u3af9\u54df\u859e\uf03e\u9294\ud02a\ua97c\u8ed0\u34bd\ud38d\u2f66\u7159\u6aca\ubc33\u20d5\ua7d0\u9265\u016e\u3d53\ufb29\ud2d2\u5b0f\u9bc2", 510635618, 0x4C2D1C4E ^ CryptoVault.mask()));
        }
        float f6 = f3;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f7 = Math.max(0.0f, list.get(i2).floatValue());
            uiElement.children().get(i2).setBounds(new UiBounds(f2, f6, Math.max(0.0f, f4), f7));
            f6 += f7 + f5;
        }
    }

    public static void horizontal(UiElement uiElement, float f2, float f3, float f4, float f5, List<Float> list) {
        if (list.size() != uiElement.children().size()) {
            throw new IllegalArgumentException(UiLayout.sl("\uc90d\u7748\u394e\u4058\u9861\u4971\ud405\u0ee0\u6766\u6781\u6b18\uef92\u6b21\ub06f\u089c\uaf58\ua04b\u1cae\u58cd\u4ff7\u3be6\u6cbf\ue867\u8194\u969e\ub227\u1750\u75c8\u4bca\uc702\uc4db\uc8b4\u8d73\u441e", 974755715, 0x4C2D1C4E ^ CryptoVault.mask()));
        }
        float f6 = f2;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f7 = Math.max(0.0f, list.get(i2).floatValue());
            uiElement.children().get(i2).setBounds(new UiBounds(f6, f3, f7, Math.max(0.0f, f4)));
            f6 += f7 + f5;
        }
    }

    private static /* synthetic */ String sl(String string, int n2, int n3) {
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

