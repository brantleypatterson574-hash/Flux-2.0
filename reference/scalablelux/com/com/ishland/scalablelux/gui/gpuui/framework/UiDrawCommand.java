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
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public sealed interface UiDrawCommand {

    @Environment(value=EnvType.CLIENT)
    public record Fluid(float x, float y, float width, float height, float radius, float cursorU, float cursorV, float cursorVelU, float cursorVelV, int dye0, int dye1, int dye2, int colorCount, float opacity, float flow, boolean inject) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Material(float x, float y, float width, float height, float radius, UiMaterial material) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Image(float x, float y, float width, float height, float radius, int argb, class_2960 texture, float u0, float v0, float u1, float v1, boolean pixelated) implements UiDrawCommand
    {
        public Image(float f2, float f3, float f4, float f5, float f6, int n2, class_2960 class_29602, float f7, float f8, float f9, float f10) {
            this(f2, f3, f4, f5, f6, n2, class_29602, f7, f8, f9, f10, false);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record Border(float x, float y, float width, float height, float radius, float thickness, int argb) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Shadow(float x, float y, float width, float height, float radius, float softness, float spread, int argb) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Effect(float x, float y, float width, float height, float radius, EffectKind kind, float strength, int argb) implements UiDrawCommand
    {
        public Effect(float f2, float f3, float f4, float f5, float f6, EffectKind effectKind, float f7) {
            this(f2, f3, f4, f5, f6, effectKind, f7, -1);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum EffectKind {
        BLUR,
        BACKDROP,
        BLOOM,
        GLASS;


        private static /* synthetic */ String fk(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    public record ClipPop() implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record ClipPush(float x, float y, float width, float height, float radius) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Text(float x, float y, float size, int argb, String text, TextWeight weight) implements UiDrawCommand
    {
        public Text(float f2, float f3, float f4, int n2, String string) {
            this(f2, f3, f4, n2, string, TextWeight.REGULAR);
        }

        public Text {
            Objects.requireNonNull(string, Text.jp("\u41e1\u505e\u3a06\u88f5\ua46e\uf49b\uf634\u6fca", 805427962, 0x83E19438 ^ CryptoVault.mask()));
            Objects.requireNonNull(textWeight, Text.jp("\u697f\u8f6d\uf141\u622c\ucaee\uee92\uc7ef\uf5aa\u2a82", -992381410, 0x83E19438 ^ CryptoVault.mask()));
        }

        private static /* synthetic */ String jp(String string, int n2, int n3) {
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
    public static enum TextWeight {
        REGULAR,
        SEMIBOLD;


        private static /* synthetic */ String yd(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    public record GradientRect(float x, float y, float width, float height, float radius, int topLeft, int topRight, int bottomRight, int bottomLeft) implements UiDrawCommand
    {
    }

    @Environment(value=EnvType.CLIENT)
    public record Rect(float x, float y, float width, float height, float radius, int argb) implements UiDrawCommand
    {
    }
}

