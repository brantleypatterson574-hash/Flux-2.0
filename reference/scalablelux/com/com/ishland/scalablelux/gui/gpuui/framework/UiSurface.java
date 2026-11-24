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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public enum UiSurface {
    THEME(UiSurface.mc("\u7c90\u9038\uc7a0\u6d3e\u260c\u5f61", -472786239, 0x5881C66 ^ CryptoVault.mask()), -1, false),
    FROSTED(UiSurface.mc("\u6152\u6a48\u275d\u1053\u9a68\ua69f\ub164\ue6ed\u11a5\ud885\uefc3\ubafb\uf0b4\u73f0\u0bac", 1142931026, 0x5881C66 ^ CryptoVault.mask()), 0, true),
    SMOKED(UiSurface.mc("\u68e8\ud6f4\uccdc\u6973\ua730\ucfdd\u349a\u2c80\uf7b6\ub0c7\u58ee\ua344\u0ca1\u4cb8", -672249200, 0x5881C66 ^ CryptoVault.mask()), 1, true),
    OBSIDIAN(UiSurface.mc("\u2d52\ub4af\u4b69\u277e\u3abf\uc138\u328a\u90b2\ub456\u60b1\u81f1", 1820333117, 0x5881C66 ^ CryptoVault.mask()), 2, false),
    CARBON(UiSurface.mc("\u4e20\u9f78\u25de\uc31d\u39b2\ufb95\u1f04\u2157\u7a77\ud6fa\ue547\ub1b8\u585e\u11b3\u20dd\ua755", 539998035, 0x5881C66 ^ CryptoVault.mask()), 3, false),
    TITANIUM(UiSurface.mc("\uc701\u629a\ubf64\u4ee8\u54c3\u5eab\ud1ca\u865d\u85dc\ue53d\u194e\u67f7\ua9c7\uaf88\u6f25\udd3d\u1e86", 810412207, 0x5881C66 ^ CryptoVault.mask()), 4, false),
    MARQUINA(UiSurface.mc("\ua334\u68f9\u5a9b\u495e\u82b5\u2bcf\u36a2\u329d\u633b\ufcf3\uf1bd\ua929\ue4cf\ue4bc\u4a6b", 200550094, 0x5881C66 ^ CryptoVault.mask()), 5, false),
    AURORA(UiSurface.mc("\u75a9\uf168\u7341\uab9f\u0cfb\u6f83\ud3c5\u70f7\uaf53", 118274262, 0x5881C66 ^ CryptoVault.mask()), 6, false),
    DEEP_SEA(UiSurface.mc("\u47c5\u3696\u3c4c\uab11\ud9c0\u6010\u0edf\ud02c\u08b4\u5af5\u3f8b", -786792359, 0x5881C66 ^ CryptoVault.mask()), 7, false),
    NEBULA(UiSurface.mc("\u3913\ucaa3\u9f9d\u9b65\u805b\u3c47\u9d90\u2c14\u3e4b", 1436757143, 0x5881C66 ^ CryptoVault.mask()), 8, false),
    MESH(UiSurface.mc("\uaaaf\u156b\u9495\ud506\ub00b\uba1b\u56bb\uf107\u3d58\u36d7\ua37b\u3d8a\u4cde\ua855\u7836\u3952\ua496", -788650939, 0x5881C66 ^ CryptoVault.mask()), 9, false),
    MAGMA(UiSurface.mc("\u2353\uc3f8\u15d5\ud5cf\uf03e\u546d\u3fbb", 2107609873, 0x5881C66 ^ CryptoVault.mask()), 10, false),
    GLACIER(UiSurface.mc("\ua9ba\u9e91\uc1a9\u3692\u2289\ub3e4\ubf66\ud3d5", 1036804730, 0x5881C66 ^ CryptoVault.mask()), 11, false),
    CIRCUIT(UiSurface.mc("\u321b\u595d\uc08a\uad61\ub845\u625f\u9c3b\u8fe8\uc584\u8890\ubc28", -385809357, 0x5881C66 ^ CryptoVault.mask()), 12, false),
    PRISM(UiSurface.mc("\u79ee\uc2f4\u7d50\uc1f6\ua4cd\ub1d3", 443917846, 0x5881C66 ^ CryptoVault.mask()), 13, false),
    PLASMA(UiSurface.mc("\u0b4c\u7308\u0842\u0376\u6b61\u1efd\udd17", -300064670, 0x5881C66 ^ CryptoVault.mask()), 14, false),
    EMBER(UiSurface.mc("\u87c9\u84ae\uaa51\ud240\ub8b5\u5b00\u4d5e", -1473982552, 0x5881C66 ^ CryptoVault.mask()), 15, false),
    RAIN(UiSurface.mc("\ud77b\ud2f0\u490e\u66c1\ub34d", 833082013, 0x5881C66 ^ CryptoVault.mask()), 16, false),
    GALAXY(UiSurface.mc("\u4c6d\u2c60\u972f\ucaa6\uac73\u45ee\uc378\u6425\uff46\u8625", -2089596855, 0x5881C66 ^ CryptoVault.mask()), 17, false),
    BIOLUME(UiSurface.mc("\u47e6\u5f7c\u2d92\uf285\u4864\u4c80\u3611\u4f5d\ub992\u00e9\uc86a\u3af9\ufd6d\ua17c\u1769\u9c48", 1003395085, 0x5881C66 ^ CryptoVault.mask()), 18, false),
    SILK(UiSurface.mc("\u86fa\u6a49\u0d12\u4505\ud868\uf2af\ub0ad\u6da3", -1942622458, 0x5881C66 ^ CryptoVault.mask()), 19, false),
    QUARTZ(UiSurface.mc("\ue5ff\uda1f\uc0d1\u2332\ufe58\u5c2b\u1af9", 439590103, 0x5881C66 ^ CryptoVault.mask()), 20, false),
    DUNES(UiSurface.mc("\u03f8\u0cb2\ubb26\u459a\ubc89\u2d15", -2053665150, 0x5881C66 ^ CryptoVault.mask()), 21, false),
    RIFT(UiSurface.mc("\u26bc\u73c7\u0400\u9610\u7fbb", -1528826334, 0x5881C66 ^ CryptoVault.mask()), 22, false),
    CHROME(UiSurface.mc("\u3b56\u7598\uf410\uafac\uf38b\uc6a7\uf4b4\ue562", -985077863, 0x5881C66 ^ CryptoVault.mask()), 23, false);

    private static final class_2960 SHADER;
    private final String displayName;
    private final int shaderMode;
    private final boolean seeThrough;

    private UiSurface(String string2, int n3, boolean bl) {
        this.displayName = string2;
        this.shaderMode = n3;
        this.seeThrough = bl;
    }

    public String displayName() {
        return this.displayName;
    }

    public boolean seeThrough() {
        return this.seeThrough;
    }

    public boolean isTheme() {
        return this == THEME;
    }

    public static String[] names() {
        return (String[])Arrays.stream(UiSurface.values()).map(UiSurface::displayName).toArray(String[]::new);
    }

    public static UiSurface byName(String string) {
        if (string == null) {
            return THEME;
        }
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        for (UiSurface uiSurface : UiSurface.values()) {
            if (!uiSurface.displayName.toLowerCase(Locale.ROOT).equals(string2)) continue;
            return uiSurface;
        }
        return THEME;
    }

    public static List<UiSurface> selectable() {
        return List.of(UiSurface.values());
    }

    public UiMaterial material(float f2, float f3, float f4, float f5, float f6, boolean bl, int n2, float f7) {
        if (this.isTheme()) {
            throw new IllegalStateException(UiSurface.mc("\u27ad\ud9c9\u7b4d\u605f\u5ce2\u6a71\u3fbe\u1f6e\u65dc\uc878\u257c\u2f9e\u92dc\ub774\u2fe8\u2735\uc9d8\u0643\uc432\u0d2d\u4518\u1123\ufc8c\u5848\u5336\ueef3\u92a9\udee6\u8b73\u7932\u97fc\uc0b0\u9a57\ube14\ue8b6\ucfc5\u39f6\u7591\u3403\ubc4a\u16c1\u6065", 2136226297, 0x5881C66 ^ CryptoVault.mask()));
        }
        return new UiMaterial(SHADER, null, -1, this.shaderMode, 1.0f, UiSurface.clamp(f2, 0.0f, 1.0f), UiSurface.clamp(f3, 0.0f, 2.0f), f4, f5, UiSurface.clamp(f6, 0.0f, 1.0f), bl ? 1.0f : 0.0f, (float)(n2 >> 16 & 0xFF) / 255.0f, (float)(n2 >> 8 & 0xFF) / 255.0f, (float)(n2 & 0xFF) / 255.0f, UiSurface.clamp(f7, 0.0f, 1.0f));
    }

    public UiMaterial preview(int n2) {
        return this.material(0.6f, 0.6f, -2.0f, -2.0f, 0.0f, false, n2, 0.35f);
    }

    private static float clamp(float f2, float f3, float f4) {
        return Math.max(f3, Math.min(f4, f2));
    }

    static {
        SHADER = class_2960.method_60655((String)UiSurface.mc("\u0e0f\u3527\udc30\u998a\uba4d\u29e5\u2814\u2c2e\udd93\u4dda\u9226\ua7aa\u1029\ub934\uff0d", 638222469, 0x5881C66 ^ CryptoVault.mask()), (String)UiSurface.mc("\u68e1\u6322\u4839\u3230\u78a5\uf5bd\u2bd2\u6e74\ubd14\u469b\u13f2\u2b05\u2001\ufba7\u0f70\uea09\ucca5\u75ef\u4538\ua801\uf58a", -33613921, 0x5881C66 ^ CryptoVault.mask()));
    }

    private static /* synthetic */ String mc(String string, int n2, int n3) {
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

