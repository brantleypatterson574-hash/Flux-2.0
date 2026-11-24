/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_6382
 */
package com.ishland.scalablelux.gui.new_ui;

import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.GuiShaderStyle;
import com.ishland.scalablelux.util.VanillaRendererImpl;
import java.awt.Color;
import java.util.function.Consumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_6382;

@Environment(value=EnvType.CLIENT)
public class SlButton
extends class_339 {
    private static final class_2960 FONT = class_2960.method_60655((String)SlButton.my("\ua5c3\u44e8\u821b\u276d\u49a2\u287a\u851a\uac1f\u226d\u3390\ue79e\u31ad\u2c22\ub0d0", 1824049132, 0x6AB8CB58 ^ CryptoVault.mask()), (String)SlButton.my("\u7fbc\u1960\ua2a1\ue141\uf9b8", 442085357, 0x6AB8CB58 ^ CryptoVault.mask()));
    private static final float FONT_SIZE = 1.4f;
    private static final Color AX_INK0 = new Color(255, 255, 255);
    private static final Color AX_INK1 = new Color(161, 161, 170);
    private static final Color AX_BG0 = new Color(9, 10, 14);
    private static final Color AX_LINE1 = new Color(255, 255, 255, 38);
    private static final Color AX_LINE2 = new Color(255, 255, 255, 180);
    private final VanillaRendererImpl renderer;
    private final Consumer<SlButton> onPress;
    private float hoverAnim = 0.0f;
    private float clickAnim = 0.0f;
    private Style style = Style.THEMED;

    public SlButton(int n2, int n3, int n4, int n5, String string, VanillaRendererImpl vanillaRendererImpl, Consumer<SlButton> consumer) {
        super(n2, n3, n4, n5, (class_2561)class_2561.method_43470((String)string));
        this.renderer = vanillaRendererImpl;
        this.onPress = consumer;
    }

    public SlButton style(Style style) {
        this.style = style == null ? Style.THEMED : style;
        return this;
    }

    public void method_25348(class_11909 class_119092, boolean bl) {
        if (this.field_22763 && this.onPress != null) {
            this.clickAnim = 1.0f;
            this.onPress.accept(this);
        }
    }

    public void method_48579(class_332 class_3322, int n2, int n3, float f2) {
        float f3 = this.method_49606() && this.field_22763 ? 1.0f : 0.0f;
        this.hoverAnim += (f3 - this.hoverAnim) * Math.min(1.0f, f2 * 0.2f);
        this.clickAnim *= (float)Math.pow(0.85, f2);
        if (this.style != Style.THEMED) {
            this.renderArmory(class_3322);
            return;
        }
        float f4 = this.clickAnim;
        float f5 = Math.max(4.0f, (float)Math.min(this.method_25368(), this.method_25364()) * 0.5f);
        float f6 = Math.max(4.0f, Math.min(f5, HitboxConfigScreen752809.b(0.42f)));
        Color color = this.withAlpha(HitboxConfigScreen752809.p(), 0.86);
        Color color2 = this.withAlpha(HitboxConfigScreen752809.q(), 0.96);
        Color color3 = this.withAlpha(HitboxConfigScreen752809.F(), 0.75);
        Color color4 = this.withAlpha(HitboxConfigScreen752809.G(), 0.88);
        Color color5 = this.withAlpha(HitboxConfigScreen752809.x(), 0.92);
        Color color6 = this.withAlpha(HitboxConfigScreen752809.w(), 1.0);
        Color color7 = this.field_22763 ? this.lerpColor(color, color2, this.hoverAnim) : this.withAlpha(color, 0.45);
        Color color8 = this.field_22763 ? this.lerpColor(color3, color4, this.hoverAnim) : this.withAlpha(color3, 0.42);
        int n4 = this.field_22763 ? this.lerpColor(color5, color6, this.hoverAnim).getRGB() : this.withAlpha(color5, 0.5).getRGB();
        float f7 = 1.0f;
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 != null) {
            f7 = (float)class_3102.method_22683().method_4495() * HitboxConfigScreen752809.ae();
        }
        this.renderer.beginFrame(class_3322.method_51421(), class_3322.method_51443(), f7);
        this.renderer.drawRoundedRectStyled(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), f6, color7, GuiShaderStyle.CONTROL);
        if (this.field_22763 && this.hoverAnim > 0.01f) {
            this.renderer.drawRoundedRectStyled(class_3322, this.method_46426() + 1, this.method_46427() + 1, this.method_25368() - 2, this.method_25364() / 2 - 1, Math.max(2.0f, f6 - 2.0f), this.withAlpha(HitboxConfigScreen752809.F(), 0.16 * (double)this.hoverAnim), GuiShaderStyle.ROW_ENABLED);
        }
        if (f4 > 0.05f) {
            this.renderer.drawRoundedRectStyled(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), f6, this.withAlpha(HitboxConfigScreen752809.G(), 0.26 * (double)f4), GuiShaderStyle.GLOW);
        }
        this.renderer.drawRoundedRectOutline(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), f6, 1.0f, color8);
        this.renderer.endFrame();
        if (class_3102 != null && class_3102.field_1772 != null) {
            String string = this.method_25369().getString();
            int n5 = class_3102.field_1772.method_1727(string);
            int n6 = this.method_46426() + (this.method_25368() - n5) / 2;
            int n7 = this.method_46427() + (this.method_25364() - 8) / 2;
            class_3322.method_51433(class_3102.field_1772, string, n6, n7, n4, false);
        }
    }

    private void renderArmory(class_332 class_3322) {
        Color color;
        Object object;
        float f2 = 1.0f;
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 != null) {
            f2 = (float)class_3102.method_22683().method_4495() * HitboxConfigScreen752809.ae();
        }
        this.renderer.beginFrame(class_3322.method_51421(), class_3322.method_51443(), f2);
        if (this.style == Style.ARMORY_PRIMARY) {
            object = this.lerpColor(new Color(244, 244, 245), Color.WHITE, this.hoverAnim);
            var6_5 = this.field_22763 ? object : this.withAlpha(new Color(244, 244, 245), 0.35);
            color = this.field_22763 ? AX_BG0 : this.withAlpha(AX_BG0, 0.6);
            this.renderer.drawRoundedRectStyled(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 0.0f, var6_5, GuiShaderStyle.CONTROL);
            if (this.field_22763) {
                this.renderer.drawRoundedRectOutline(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 0.0f, 1.0f, this.lerpColor(new Color(255, 255, 255, 200), Color.WHITE, this.hoverAnim));
            }
        } else if (this.style == Style.ARMORY_TEXT) {
            color = this.field_22763 ? this.lerpColor(AX_INK1, AX_INK0, this.hoverAnim) : this.withAlpha(AX_INK1, 0.45);
        } else {
            object = new Color(14, 15, 20, this.field_22763 ? (int)(180.0f + 40.0f * this.hoverAnim) : 100);
            var6_5 = this.field_22763 ? this.lerpColor(new Color(60, 64, 80), Color.WHITE, this.hoverAnim) : new Color(38, 40, 50);
            color = this.field_22763 ? this.lerpColor(AX_INK1, AX_INK0, this.hoverAnim) : this.withAlpha(AX_INK1, 0.5);
            this.renderer.drawRoundedRectStyled(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 0.0f, (Color)object, GuiShaderStyle.CONTROL);
            this.renderer.drawRoundedRectOutline(class_3322, this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), 0.0f, 1.0f, var6_5);
        }
        this.renderer.endFrame();
        if (class_3102 != null && class_3102.field_1772 != null) {
            object = this.method_25369().getString();
            int n2 = class_3102.field_1772.method_1727((String)object);
            int n3 = this.method_46426() + (this.method_25368() - n2) / 2;
            int n4 = this.method_46427() + (this.method_25364() - 8) / 2;
            class_3322.method_51433(class_3102.field_1772, (String)object, n3, n4, color.getRGB(), false);
        }
    }

    protected void method_47399(class_6382 class_63822) {
        this.method_37021(class_63822);
    }

    private Color lerpColor(Color color, Color color2, float f2) {
        f2 = Math.max(0.0f, Math.min(1.0f, f2));
        return new Color((int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f2), (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f2), (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f2), (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f2));
    }

    private Color withAlpha(Color color, double d2) {
        double d3 = Math.max(0.0, Math.min(1.0, d2));
        int n2 = Math.max(0, Math.min(255, (int)Math.round((double)color.getAlpha() * d3)));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n2);
    }

    private static /* synthetic */ String my(String string, int n2, int n3) {
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
    public static enum Style {
        THEMED,
        ARMORY_PRIMARY,
        ARMORY_GHOST,
        ARMORY_TEXT;


        private static /* synthetic */ String sr(String string, int n2, int n3) {
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

