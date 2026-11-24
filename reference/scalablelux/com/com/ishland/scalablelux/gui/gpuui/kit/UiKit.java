/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.l.a;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntConsumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiKit {
    public static final int ON_ACCENT = -15724012;
    public static final int DANGER = -38037;
    public static final int SUCCESS = -9247307;

    public static int panelFill() {
        UiThemeMaterial.Theme theme = UiThemeMaterial.theme(HitboxConfigScreen752809.a == null ? "" : HitboxConfigScreen752809.a.getCurrentMode());
        int n2 = theme.panelBase() & 0xFFFFFF;
        int n3 = a.at() & 0xFFFFFF;
        int n4 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ai, 0);
        if (n4 != 0) {
            float f2 = Math.min(0.6f, (float)(n4 >>> 24 & 0xFF) / 255.0f);
            n2 = UiKit.mixRgb(n2, n4, f2) & 0xFFFFFF;
        }
        int n5 = ((n2 >> 16 & 0xFF) * 90 + (n3 >> 16 & 0xFF) * 10) / 100;
        int n6 = ((n2 >> 8 & 0xFF) * 90 + (n3 >> 8 & 0xFF) * 10) / 100;
        int n7 = ((n2 & 0xFF) * 90 + (n3 & 0xFF) * 10) / 100;
        return 0xF4000000 | n5 << 16 | n6 << 8 | n7;
    }

    private UiKit() {
    }

    public static int argb(float f2, int n2) {
        return Math.max(0, Math.min(255, Math.round(f2))) << 24 | n2 & 0xFFFFFF;
    }

    public static int withAlpha(int n2, int n3) {
        return n3 << 24 | n2 & 0xFFFFFF;
    }

    public static void card(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6) {
        float f7 = Math.round(12.0f * f6 * uiHost.cornerScale());
        builder.rect(f2, f3, f4, f5, f7, uiHost.cardFill());
        builder.border(f2, f3, f4, f5, f7, 1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.ak, uiHost.lineColor()));
    }

    public static void divider(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4) {
        builder.rect(f2, f3, f4, 1.0f, 0.0f, uiHost.lineColor());
    }

    public static void title(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4) {
        builder.text(f2, f3, 16.0f * f4, uiHost.titleColor(), string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    public static void label(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4) {
        builder.text(f2, f3, 9.5f * f4, uiHost.muted2Color(), string.toUpperCase(Locale.ROOT), UiDrawCommand.TextWeight.SEMIBOLD);
    }

    public static void button(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4, float f5, float f6, Style style, boolean bl, float f7, float f8, Runnable runnable) {
        int n2;
        int n3;
        boolean bl2 = bl && uiHost.hovered(f7, f8, f2, f3, f4, f5);
        float f9 = f5 * 0.5f * Math.min(1.0f, uiHost.cornerScale());
        int n4 = uiHost.accent();
        int n5 = switch (style.ordinal()) {
            case 0 -> {
                n3 = bl2 ? UiKit.brighten(n4, 0.12f) : n4;
                n2 = 0;
                yield UiKit.onAccent(n4);
            }
            case 2 -> {
                n3 = UiKit.withAlpha(-38037, bl2 ? 64 : 36);
                n2 = UiKit.withAlpha(-38037, 96);
                yield -25958;
            }
            case 3 -> {
                n3 = bl2 ? uiHost.controlFill(26) : 0;
                n2 = uiHost.lineColor();
                yield uiHost.textColor();
            }
            default -> {
                n3 = uiHost.controlFill(bl2 ? 48 : 28);
                n2 = uiHost.lineColor();
                yield uiHost.textColor();
            }
        };
        if (!bl) {
            n3 = style == Style.PRIMARY ? UiKit.withAlpha(n4, 64) : uiHost.controlFill(12);
            int n6 = n5 = style == Style.PRIMARY ? UiKit.withAlpha(UiKit.onAccent(n4), 144) : uiHost.muted2Color();
        }
        if (n3 >>> 24 != 0) {
            builder.rect(f2, f3, f4, f5, f9, n3);
        }
        if (n2 >>> 24 != 0) {
            builder.border(f2, f3, f4, f5, f9, 1.0f, n2);
        }
        float f10 = 10.0f * f6;
        String string2 = UiKit.fit(string, Math.max(0.0f, f4 - 14.0f * f6), f10, UiDrawCommand.TextWeight.SEMIBOLD);
        float f11 = GpuUiFontMetrics.textWidth(string2, f10, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2 + (f4 - f11) * 0.5f, GpuUiFontMetrics.topInBox(f3, f5, f10), f10, n5, string2, UiDrawCommand.TextWeight.SEMIBOLD);
        if (bl) {
            uiHost.hit(f2, f3, f4, f5, runnable);
        }
    }

    public static float buttonWidth(String string, float f2) {
        return Math.round(GpuUiFontMetrics.textWidth(string, 10.0f * f2, UiDrawCommand.TextWeight.SEMIBOLD) + 28.0f * f2);
    }

    public static float chip(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4, int n2) {
        float f5 = 8.5f * f4;
        float f6 = Math.round(GpuUiFontMetrics.textWidth(string, f5, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f4);
        float f7 = Math.round(17.0f * f4);
        builder.rect(f2, f3, f6, f7, f7 * 0.5f * Math.min(1.0f, uiHost.cornerScale()), UiKit.withAlpha(n2, 38));
        builder.text(f2 + 8.0f * f4, GpuUiFontMetrics.topInBox(f3, f7, f5), f5, n2, string, UiDrawCommand.TextWeight.SEMIBOLD);
        return f6;
    }

    public static void segmented(UiDisplayList.Builder builder, UiHost uiHost, String[] stringArray, int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, IntConsumer intConsumer) {
        float f9;
        float f10 = Math.round(3.0f * f6);
        float f11 = f5 - f10 * 2.0f;
        float f12 = Math.min(1.0f, uiHost.cornerScale());
        builder.rect(f2, f3, f4, f5, (f11 * 0.5f + f10) * f12, uiHost.controlFill(18));
        builder.border(f2, f3, f4, f5, (f11 * 0.5f + f10) * f12, 1.0f, uiHost.lineColor());
        float f13 = (f4 - f10 * 2.0f) / (float)stringArray.length;
        if (stringArray.length > 0 && n2 >= 0) {
            float f14 = uiHost.anim(UiKit.cl("\u31d8\uc0b7\u7cb1\u514a\u5fd3\u19af\u5e92\u5bbd\ufc15\u4b40\u8e79", -1319449334, 0xB0C11A9E ^ CryptoVault.mask()) + Math.round(f2) + "_" + Math.round(f3), n2, 18.0f);
            f9 = Math.round(f2 + f10 + f14 * f13);
            builder.rect(f9, f3 + f10, Math.round(f13), f11, f11 * 0.5f * f12, UiKit.withAlpha(uiHost.accent(), 52));
        }
        int n3 = 0;
        while (n3 < stringArray.length) {
            f9 = Math.round(f2 + f10 + (float)n3 * f13);
            float f15 = (float)Math.round(f2 + f10 + (float)(n3 + 1) * f13) - f9;
            boolean bl = n3 == n2;
            boolean bl2 = uiHost.hovered(f7, f8, f9, f3, f15, f5);
            if (!bl && bl2) {
                builder.rect(f9, f3 + f10, f15, f11, f11 * 0.5f * f12, uiHost.controlFill(20));
            }
            float f16 = 10.0f * f6;
            String string = UiKit.fit(stringArray[n3], Math.max(0.0f, f15 - 12.0f * f6), f16, UiDrawCommand.TextWeight.SEMIBOLD);
            float f17 = GpuUiFontMetrics.textWidth(string, f16, UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f9 + (f15 - f17) * 0.5f, GpuUiFontMetrics.topInBox(f3, f5, f16), f16, bl ? uiHost.accent() : (bl2 ? uiHost.textColor() : uiHost.mutedColor()), string, UiDrawCommand.TextWeight.SEMIBOLD);
            int n4 = n3++;
            uiHost.hit(f9, f3, f15, f5, () -> intConsumer.accept(n4));
        }
    }

    public static void toggle(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, boolean bl, Runnable runnable) {
        f2 = Math.round(f2);
        f3 = Math.round(f3);
        float f5 = Math.round(34.0f * f4);
        float f6 = Math.round(18.0f * f4);
        float f7 = f6 * 0.5f;
        float f8 = uiHost.anim(UiKit.cl("\ub0ea\ucc3a\u0615\ucda0\u9740\u1101\uf0cc\u4f94\ud4ee", -1917865555, 0xB0C11A9E ^ CryptoVault.mask()) + Math.round(f2) + "_" + Math.round(f3), bl ? 1.0f : 0.0f, 22.0f);
        float f9 = f7 * Math.min(1.0f, uiHost.cornerScale());
        int n2 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.an, uiHost.accent()) | 0xFF000000;
        builder.rect(f2, f3, f5, f6, f9, uiHost.controlFill(40));
        if (f8 > 0.003f) {
            builder.rect(f2, f3, f5, f6, f9, UiKit.withAlpha(n2, Math.round(255.0f * f8)));
        }
        float f10 = f7 - Math.max(1.0f, (float)Math.round(2.0f * f4));
        float f11 = f2 + f7 + (f5 - f6) * f8;
        builder.circle(f11, f3 + f7 + Math.max(1.0f, 0.5f * f4), f10, 0x30000000);
        float f12 = ((float)(n2 >> 16 & 0xFF) * 0.299f + (float)(n2 >> 8 & 0xFF) * 0.587f + (float)(n2 & 0xFF) * 0.114f) / 255.0f;
        builder.circle(f11, f3 + f7, f10, bl && f12 > 0.62f ? -15460581 : -723208);
        uiHost.hit(f2, f3, f5, f6, runnable);
    }

    public static String fit(String string, float f2, float f3) {
        return UiKit.fit(string, f2, f3, UiDrawCommand.TextWeight.REGULAR);
    }

    public static String fit(String string, float f2, float f3, UiDrawCommand.TextWeight textWeight) {
        if (string == null) {
            return "";
        }
        if (GpuUiFontMetrics.textWidth(string, f3, textWeight) <= f2) {
            return string;
        }
        String string2 = "\u2026";
        float f4 = f2 - GpuUiFontMetrics.textWidth(string2, f3, textWeight);
        int n2 = 0;
        int n3 = string.length();
        while (n2 < n3) {
            int n4 = n2 + n3 + 1 >>> 1;
            if (GpuUiFontMetrics.textWidth(string.substring(0, n4), f3, textWeight) <= f4) {
                n2 = n4;
                continue;
            }
            n3 = n4 - 1;
        }
        return string.substring(0, n2).stripTrailing() + string2;
    }

    public static List<String> wrap(String string, float f2, float f3, int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (string == null || string.isBlank() || f2 <= 0.0f) {
            return arrayList;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : string.trim().split(UiKit.cl("\ud9e1\u1add\u3855\u5796\ub7d5\u6199", 1798531003, 0xB0C11A9E ^ CryptoVault.mask()))) {
            String string3;
            String string4 = string3 = stringBuilder.length() == 0 ? string2 : stringBuilder + " " + string2;
            if (GpuUiFontMetrics.textWidth(string3, f3) <= f2 || stringBuilder.length() == 0) {
                stringBuilder.setLength(0);
                stringBuilder.append(string3);
                continue;
            }
            arrayList.add(stringBuilder.toString());
            stringBuilder.setLength(0);
            stringBuilder.append(string2);
            if (arrayList.size() == n2) break;
        }
        if (arrayList.size() < n2 && stringBuilder.length() > 0) {
            arrayList.add(stringBuilder.toString());
        }
        if (arrayList.size() >= n2) {
            int n3 = n2 - 1;
            String string5 = (String)arrayList.get(n3);
            int n4 = string.trim().length() > String.join((CharSequence)" ", arrayList).length() ? 1 : 0;
            arrayList = new ArrayList(arrayList.subList(0, n2));
            if (n4 != 0) {
                arrayList.set(n3, UiKit.fit(string5.stripTrailing() + "\u2026", f2, f3));
            }
        }
        return arrayList;
    }

    public static void emptyState(UiDisplayList.Builder builder, UiHost uiHost, String string, String string2, float f2, float f3, float f4, float f5) {
        float f6 = 12.0f * f5;
        float f7 = 10.0f * f5;
        builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string, f6, UiDrawCommand.TextWeight.SEMIBOLD)) * 0.5f, f3, f6, uiHost.mutedColor(), string, UiDrawCommand.TextWeight.SEMIBOLD);
        if (string2 != null && !string2.isEmpty()) {
            builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string2, f7)) * 0.5f, f3 + 18.0f * f5, f7, uiHost.muted2Color(), string2);
        }
    }

    public static int onAccent(int n2) {
        float f2 = ((float)(n2 >> 16 & 0xFF) * 0.299f + (float)(n2 >> 8 & 0xFF) * 0.587f + (float)(n2 & 0xFF) * 0.114f) / 255.0f;
        return f2 > 0.55f ? -15724012 : -591879;
    }

    public static int panelOpaque() {
        return 0xFF000000 | UiKit.panelFill() & 0xFFFFFF;
    }

    public static int mixRgb(int n2, int n3, float f2) {
        float f3 = Math.max(0.0f, Math.min(1.0f, f2));
        int n4 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f3) + (float)(n3 >> 16 & 0xFF) * f3);
        int n5 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f3) + (float)(n3 >> 8 & 0xFF) * f3);
        int n6 = Math.round((float)(n2 & 0xFF) * (1.0f - f3) + (float)(n3 & 0xFF) * f3);
        return 0xFF000000 | n4 << 16 | n5 << 8 | n6;
    }

    public static int brighten(int n2, float f2) {
        int n3 = n2 >>> 24;
        int n4 = Math.min(255, Math.round((float)(n2 >> 16 & 0xFF) + 255.0f * f2));
        int n5 = Math.min(255, Math.round((float)(n2 >> 8 & 0xFF) + 255.0f * f2));
        int n6 = Math.min(255, Math.round((float)(n2 & 0xFF) + 255.0f * f2));
        return n3 << 24 | n4 << 16 | n5 << 8 | n6;
    }

    private static /* synthetic */ String cl(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    public static enum Style {
        PRIMARY,
        SECONDARY,
        DANGER,
        GHOST;


        private static /* synthetic */ String vl(String string, int n2, int n3) {
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
}

