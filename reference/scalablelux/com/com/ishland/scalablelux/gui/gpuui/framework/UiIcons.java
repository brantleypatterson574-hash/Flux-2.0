/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.Locale;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiIcons {
    private static final float VIEWBOX = 24.0f;

    private UiIcons() {
    }

    public static void drawLogo(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2) {
        ClassicIcons.drawLogo(builder, f2, f3, f4, n2);
    }

    public static Icon forModule(String string, Icon icon) {
        Objects.requireNonNull(icon, UiIcons.or("\u2329\ub443\u5976\u2315\uaf26\u3322\u3a79\uaad4\u9635\u8c6d\ue96a\u2e22\u2c0a\ufd05\u700c\u74dd\u55d0\u6fbe\u1485", -1771903174, 0xF2203B42 ^ CryptoVault.mask()));
        if (string == null) {
            return icon;
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        if (string2.contains(UiIcons.or("\u7ab2\uf9f0\u354d\uea7a", -117924981, 0xF2203B42 ^ CryptoVault.mask())) || string2.contains(UiIcons.or("\u2be8\uf6cc\u7a0c\uba0b\u3d05\u7145\u94df", -38138426, 0xF2203B42 ^ CryptoVault.mask())) || string2.contains(UiIcons.or("\u35dd\u0c6d\ufbab\u3f12\udd2d\u34c2\ue725\ufb21\ub838\u4cbf\u48e6", -1315405790, 0xF2203B42 ^ CryptoVault.mask()))) {
            return Icon.SILENT;
        }
        if (string2.contains(UiIcons.or("\u46e8\ua833\u330c\ud7eb\u8a5c\u8cc3\u273a\u1efe", -1620149417, 0xF2203B42 ^ CryptoVault.mask())) || string2.contains(UiIcons.or("\u0342\ub0d0\uc28d\u3137\u69db\ua885\u8dac\ud3bf\u5bd0", 312352768, 0xF2203B42 ^ CryptoVault.mask()))) {
            return Icon.SHIELD;
        }
        if (string2.contains(UiIcons.or("\u56a0\u7a47\u8fc0\uc68c\u2c54\u3ff4", 1038590750, 0xF2203B42 ^ CryptoVault.mask())) || string2.contains(UiIcons.or("\u831b\u3e6b\u35a9\u11a8", 759835070, 0xF2203B42 ^ CryptoVault.mask()))) {
            return Icon.KEY;
        }
        if (string2.contains(UiIcons.or("\uf9ce\u8fcd\u63e2\ua94a\uffde", -1428428018, 0xF2203B42 ^ CryptoVault.mask())) || string2.contains(UiIcons.or("\ue524\u9659\u8428\ub298\ufc2f\u82ee\ud67b\ud2a0\ufee1\u9b5d", -49432138, 0xF2203B42 ^ CryptoVault.mask()))) {
            return Icon.RENDER;
        }
        return icon;
    }

    public static void draw(UiDisplayList.Builder builder, Icon icon, float f2, float f3, float f4, int n2) {
        Objects.requireNonNull(builder, UiIcons.or("\u5ca9\ufb46\u8c0d\ufe8a\ub64f\uf4f4\u5d00", 343489270, 0xF2203B42 ^ CryptoVault.mask()));
        Objects.requireNonNull(icon, UiIcons.or("\u6931\ub69d\u2e31\u3e4c\u8863", -983977048, 0xF2203B42 ^ CryptoVault.mask()));
        if (!Float.isFinite(f2) || !Float.isFinite(f3) || !Float.isFinite(f4) || f4 <= 0.0f) {
            return;
        }
        Pen pen = new Pen(builder, f2, f3, f4 / 24.0f, n2);
        switch (icon.ordinal()) {
            case 0: {
                pen.path(14.5f, 17.5f, 3.0f, 6.0f, 3.0f, 3.0f, 6.0f, 3.0f, 17.5f, 14.5f);
                pen.path(13.0f, 19.0f, 19.0f, 13.0f);
                pen.path(16.0f, 16.0f, 20.0f, 20.0f);
                break;
            }
            case 1: {
                pen.ring(13.0f, 4.0f, 2.0f, 2.0f);
                pen.path(6.0f, 20.0f, 9.0f, 16.0f, 12.0f, 18.0f, 14.0f, 13.0f);
                pen.path(9.0f, 12.0f, 11.0f, 9.0f, 15.0f, 10.0f, 17.0f, 13.0f);
                break;
            }
            case 2: {
                pen.box();
                break;
            }
            case 3: {
                pen.cubic(2.0f, 12.0f, 2.0f, 12.0f, 5.5f, 5.0f, 12.0f, 5.0f);
                pen.cubic(12.0f, 5.0f, 18.5f, 5.0f, 22.0f, 12.0f, 22.0f, 12.0f);
                pen.cubic(22.0f, 12.0f, 22.0f, 12.0f, 18.5f, 19.0f, 12.0f, 19.0f);
                pen.cubic(12.0f, 19.0f, 5.5f, 19.0f, 2.0f, 12.0f, 2.0f, 12.0f);
                pen.ring(12.0f, 12.0f, 3.0f, 2.0f);
                break;
            }
            case 4: 
            case 6: {
                pen.ring(12.0f, 7.0f, 4.0f, 2.0f);
                pen.cubic(5.5f, 21.0f, 5.5f, 12.3f, 18.5f, 12.3f, 18.5f, 21.0f);
                break;
            }
            case 5: {
                for (int i2 = 0; i2 < 8; ++i2) {
                    double d2 = (double)i2 * Math.PI / 4.0;
                    pen.line(12.0f + 8.0f * (float)Math.cos(d2), 12.0f + 8.0f * (float)Math.sin(d2), 12.0f + 10.0f * (float)Math.cos(d2), 12.0f + 10.0f * (float)Math.sin(d2));
                }
                pen.ring(12.0f, 12.0f, 8.0f, 2.0f);
                pen.ring(12.0f, 12.0f, 3.0f, 2.0f);
                break;
            }
            case 7: {
                pen.ring(12.0f, 12.0f, 8.0f, 2.0f);
                break;
            }
            case 19: {
                pen.cubic(14.7f, 6.3f, 10.7f, 3.8f, 7.1f, 7.5f, 9.3f, 11.7f);
                pen.path(9.3f, 11.7f, 3.0f, 18.0f, 6.0f, 21.0f, 12.3f, 14.7f);
                pen.cubic(12.3f, 14.7f, 16.5f, 16.9f, 20.2f, 13.3f, 17.7f, 9.3f);
                pen.path(17.7f, 9.3f, 15.1f, 11.9f, 13.1f, 9.9f, 15.7f, 7.3f);
                break;
            }
            case 20: {
                pen.box();
                break;
            }
            case 21: {
                pen.ring(12.0f, 12.0f, 10.0f, 2.0f);
                pen.path(22.0f, 12.0f, 18.0f, 12.0f);
                pen.path(6.0f, 12.0f, 2.0f, 12.0f);
                pen.path(12.0f, 6.0f, 12.0f, 2.0f);
                pen.path(12.0f, 22.0f, 12.0f, 18.0f);
                break;
            }
            case 18: {
                pen.path(10.3f, 3.9f, 1.8f, 18.0f, 3.5f, 21.0f, 20.5f, 21.0f, 22.2f, 18.0f, 13.7f, 3.9f, 10.3f, 3.9f);
                pen.path(12.0f, 9.0f, 12.0f, 13.0f);
                pen.circle(12.0f, 17.0f, 1.0f);
                break;
            }
            case 22: {
                pen.path(12.0f, 22.0f, 20.0f, 17.0f, 20.0f, 5.0f, 12.0f, 2.0f, 4.0f, 5.0f, 4.0f, 17.0f, 12.0f, 22.0f);
                break;
            }
            case 23: {
                pen.ring(7.5f, 15.5f, 4.5f, 2.0f);
                pen.path(10.5f, 12.5f, 17.5f, 5.5f);
                pen.path(15.0f, 8.0f, 17.5f, 10.5f);
                pen.path(18.0f, 5.0f, 20.5f, 7.5f);
                break;
            }
            case 8: {
                pen.ring(11.0f, 11.0f, 7.0f, 2.0f);
                pen.line(20.0f, 20.0f, 16.5f, 16.5f);
                break;
            }
            case 9: {
                pen.ring(12.0f, 12.0f, 10.0f, 2.0f);
                pen.path(15.0f, 9.0f, 9.0f, 15.0f);
                pen.path(9.0f, 9.0f, 15.0f, 15.0f);
                break;
            }
            case 10: {
                pen.path(9.0f, 6.0f, 15.0f, 12.0f, 9.0f, 18.0f);
                break;
            }
            case 11: {
                pen.path(6.0f, 9.0f, 12.0f, 15.0f, 18.0f, 9.0f);
                break;
            }
            case 12: {
                pen.path(15.0f, 6.0f, 9.0f, 12.0f, 15.0f, 18.0f);
                break;
            }
            case 13: {
                pen.path(4.0f, 7.0f, 14.0f, 7.0f);
                pen.path(18.0f, 7.0f, 20.0f, 7.0f);
                pen.ring(16.0f, 7.0f, 2.0f, 2.0f);
                pen.path(4.0f, 17.0f, 6.0f, 17.0f);
                pen.path(10.0f, 17.0f, 20.0f, 17.0f);
                pen.ring(8.0f, 17.0f, 2.0f, 2.0f);
                break;
            }
            case 14: {
                pen.path(20.0f, 6.0f, 9.0f, 17.0f, 4.0f, 12.0f);
                break;
            }
            case 15: {
                pen.path(12.0f, 5.0f, 12.0f, 19.0f);
                pen.path(5.0f, 12.0f, 19.0f, 12.0f);
                break;
            }
            case 16: {
                pen.path(5.0f, 12.0f, 19.0f, 12.0f);
                break;
            }
            case 17: {
                pen.ring(12.0f, 12.0f, 10.0f, 2.0f);
                pen.circle(12.0f, 8.0f, 1.0f);
                pen.path(12.0f, 12.0f, 12.0f, 16.0f);
            }
        }
    }

    private static /* synthetic */ String or(String string, int n2, int n3) {
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
    public static enum Icon {
        COMBAT,
        MOVEMENT,
        MACROS,
        RENDER,
        PLAYER,
        CLIENT,
        FRIENDS,
        ENEMIES,
        SEARCH,
        CLOSE,
        CHEVRON_RIGHT,
        CHEVRON_DOWN,
        BACK,
        SLIDERS,
        CHECK,
        PLUS,
        MINUS,
        INFO,
        EXTENDED,
        UTILITY,
        MISC,
        SILENT,
        SHIELD,
        KEY;


        private static /* synthetic */ String rv(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    private static final class Pen {
        private final UiDisplayList.Builder list;
        private final float x;
        private final float y;
        private final float scale;
        private final int color;

        private Pen(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2) {
            this.list = builder;
            this.x = f2;
            this.y = f3;
            this.scale = f4;
            this.color = n2;
        }

        private void circle(float f2, float f3, float f4) {
            this.list.circle(this.x + f2 * this.scale, this.y + f3 * this.scale, f4 * this.scale, this.color);
        }

        private void ring(float f2, float f3, float f4, float f5) {
            float f6 = this.x + (f2 - f4) * this.scale;
            float f7 = this.y + (f3 - f4) * this.scale;
            float f8 = f4 * 2.0f * this.scale;
            this.list.border(f6, f7, f8, f8, f4 * this.scale, f5 * this.scale, this.color);
        }

        private void path(float ... fArray) {
            for (int i2 = 2; i2 < fArray.length; i2 += 2) {
                this.line(fArray[i2 - 2], fArray[i2 - 1], fArray[i2], fArray[i2 + 1]);
            }
        }

        private void box() {
            this.path(21.0f, 8.0f, 12.0f, 3.0f, 3.0f, 8.0f, 3.0f, 16.0f, 12.0f, 21.0f, 21.0f, 16.0f, 21.0f, 8.0f);
            this.path(3.0f, 8.0f, 12.0f, 13.0f, 21.0f, 8.0f);
            this.path(12.0f, 13.0f, 12.0f, 21.0f);
        }

        private void cubic(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
            float f10 = f2;
            float f11 = f3;
            for (int i2 = 1; i2 <= 16; ++i2) {
                float f12 = (float)i2 / 16.0f;
                float f13 = 1.0f - f12;
                float f14 = f13 * f13 * f13 * f2 + 3.0f * f13 * f13 * f12 * f4 + 3.0f * f13 * f12 * f12 * f6 + f12 * f12 * f12 * f8;
                float f15 = f13 * f13 * f13 * f3 + 3.0f * f13 * f13 * f12 * f5 + 3.0f * f13 * f12 * f12 * f7 + f12 * f12 * f12 * f9;
                this.line(f10, f11, f14, f15);
                f10 = f14;
                f11 = f15;
            }
        }

        private void line(float f2, float f3, float f4, float f5) {
            float f6 = 2.0f * this.scale;
            float f7 = this.x + f2 * this.scale;
            float f8 = this.y + f3 * this.scale;
            float f9 = this.x + f4 * this.scale;
            float f10 = this.y + f5 * this.scale;
            if (Math.abs(f8 - f10) < 0.001f) {
                this.list.pill(Math.min(f7, f9) - f6 * 0.5f, f8 - f6 * 0.5f, Math.abs(f9 - f7) + f6, f6, this.color);
                return;
            }
            if (Math.abs(f7 - f9) < 0.001f) {
                this.list.rect(f7 - f6 * 0.5f, Math.min(f8, f10) - f6 * 0.5f, f6, Math.abs(f10 - f8) + f6, f6 * 0.5f, this.color);
                return;
            }
            int n2 = Math.max(1, (int)Math.ceil(Math.hypot(f9 - f7, f10 - f8) / (double)(f6 * 0.65f)));
            for (int i2 = 0; i2 <= n2; ++i2) {
                float f11 = (float)i2 / (float)n2;
                this.list.circle(f7 + (f9 - f7) * f11, f8 + (f10 - f8) * f11, f6 * 0.5f, this.color);
            }
        }
    }
}

