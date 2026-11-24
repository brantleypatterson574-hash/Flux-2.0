/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11719
 *  net.minecraft.class_11719$class_11721
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_5250
 *  net.minecraft.class_5348
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11719;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5250;
import net.minecraft.class_5348;

@Environment(value=EnvType.CLIENT)
public final class GpuUiFallbackText {
    public static final float SCALE = 0.92f;
    public static final float CAP_CENTER_PX = 3.6f;
    private static final class_2583 REGULAR = class_2583.field_24360.method_27704((class_11719)new class_11719.class_11721(class_2960.method_60655((String)GpuUiFallbackText.jw("\uefed\u1e60\uc259\u3cc5\u24bb\u7aa3\u2e17\u4ea8\u60d6\u49c7\ue02a\uf54a", -1594178664, 0x1E3879D0 ^ CryptoVault.mask()), (String)GpuUiFallbackText.jw("\u8233\ucdaf\u2cf9\u4fa9\ufed6\u3ec9\uc723", 276971308, 0x1E3879D0 ^ CryptoVault.mask()))));
    private static final class_2583 BOLD = class_2583.field_24360.method_27704((class_11719)new class_11719.class_11721(class_2960.method_60655((String)GpuUiFallbackText.jw("\u49f6\uf5d2\ub899\u6e61\uefb8\uc0c9\u3d18\u2fcd\uac0b\u5db7\u0bad\u28cf\ub70b", 1384921225, 0x1E3879D0 ^ CryptoVault.mask()), (String)GpuUiFallbackText.jw("\ube6b\ucae0\ucdc9\u6c8d\u748e\uf92a\ue2c0\u0f68\ue3ba\ub495\u4fd0", 1466035705, 0x1E3879D0 ^ CryptoVault.mask()))));
    private static final Map<String, class_2561> CACHE_REGULAR = new HashMap<String, class_2561>();
    private static final Map<String, class_2561> CACHE_BOLD = new HashMap<String, class_2561>();

    private GpuUiFallbackText() {
    }

    public static boolean covers(int n2) {
        return n2 < 128;
    }

    public static String shape(String string) {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || class_3102.field_1772 == null) {
            return string;
        }
        boolean bl = false;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if ((c2 < '\u0590' || c2 > '\u08ff') && (c2 < '\ufb1d' || c2 > '\ufeff')) continue;
            bl = true;
            break;
        }
        if (!bl) {
            return string;
        }
        try {
            return class_3102.field_1772.method_1721(string);
        }
        catch (Throwable throwable) {
            return string;
        }
    }

    public static class_2561 component(String string, UiDrawCommand.TextWeight textWeight) {
        Map<String, class_2561> map = textWeight == UiDrawCommand.TextWeight.SEMIBOLD ? CACHE_BOLD : CACHE_REGULAR;
        class_2561 class_25612 = map.get(string);
        if (class_25612 != null) {
            return class_25612;
        }
        if (map.size() > 1500) {
            map.clear();
        }
        class_5250 class_52502 = class_2561.method_43470((String)GpuUiFallbackText.shape(string)).method_10862(textWeight == UiDrawCommand.TextWeight.SEMIBOLD ? BOLD : REGULAR);
        map.put(string, (class_2561)class_52502);
        return class_52502;
    }

    public static float width(String string, float f2, UiDrawCommand.TextWeight textWeight) {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || class_3102.field_1772 == null) {
            return (float)string.length() * f2 * 0.6f;
        }
        Objects.requireNonNull(class_3102.field_1772);
        float f3 = f2 * 0.92f / 9.0f;
        return (float)class_3102.field_1772.method_27525((class_5348)GpuUiFallbackText.component(string, textWeight)) * f3;
    }

    private static /* synthetic */ String jw(String string, int n2, int n3) {
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

