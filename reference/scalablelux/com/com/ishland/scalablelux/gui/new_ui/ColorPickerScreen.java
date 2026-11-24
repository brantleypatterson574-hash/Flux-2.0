/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_437
 *  net.minecraft.class_768
 */
package com.ishland.scalablelux.gui.new_ui;

import com.ishland.scalablelux.gui.new_ui.ThemedPalette;
import com.ishland.scalablelux.l.b;
import com.ishland.scalablelux.l.c;
import com.ishland.scalablelux.module.ColorSetting;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.ISlRenderer;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.Renderer;
import java.awt.Color;
import java.util.Locale;
import java.util.function.Consumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_437;
import net.minecraft.class_768;

@Environment(value=EnvType.CLIENT)
public class ColorPickerScreen
extends class_437 {
    private final class_437 parent;
    private final ColorSetting colorSetting;
    private final Consumer<Color> onColorChange;
    private final b theme;
    private float hue;
    private float saturation;
    private float brightness;
    private float alpha;
    private class_768 panelBounds;
    private class_768 sbBox;
    private class_768 hueStrip;
    private class_768 alphaStrip;
    private class_768 previewBox;
    private boolean draggingSB;
    private boolean draggingHue;
    private boolean draggingAlpha;
    private class_342 hexField;
    private Color panelColor = new Color(25, 25, 25, 250);
    private Color borderColor = new Color(60, 60, 60);
    private Color titleColor = Color.LIGHT_GRAY;
    private Color mutedColor = Color.GRAY;
    private Color controlColor = new Color(50, 50, 50);

    public ColorPickerScreen(class_437 class_4372, ColorSetting colorSetting, Consumer<Color> consumer) {
        super((class_2561)class_2561.method_43470((String)ColorPickerScreen.un("\u8021\ud624\u9c78\u728f\ub2e8\u1521\u624d\u7d45\u9d78\uc903\u3240\u7e9a\ub892", 691163126, 0x165923CE ^ CryptoVault.mask())));
        this.parent = class_4372;
        this.colorSetting = colorSetting;
        this.onColorChange = consumer;
        this.theme = c.aa().aa();
        Color color = colorSetting.A();
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = fArray[0];
        this.saturation = fArray[1];
        this.brightness = fArray[2];
        this.alpha = (float)color.getAlpha() / 255.0f;
    }

    protected void method_25426() {
        int n2 = 260;
        int n3 = 226;
        int n4 = (this.field_22789 - n2) / 2;
        int n5 = (this.field_22790 - n3) / 2;
        this.panelBounds = new class_768(n4, n5, n2, n3);
        int n6 = 15;
        int n7 = 15;
        int n8 = 50;
        int n9 = 8;
        int n10 = n2 - n6 * 3 - n7;
        int n11 = n3 - n6 * 3 - n9 - n8 - 10;
        this.sbBox = new class_768(n4 + n6, n5 + n6 + 15, n10, n11);
        this.hueStrip = new class_768(n4 + n6 + n10 + n6, n5 + n6 + 15, n7, n11);
        this.alphaStrip = new class_768(n4 + n6, this.sbBox.method_3322() + n11 + 10, n2 - n6 * 2, n9);
        this.previewBox = new class_768(n4 + n6, this.alphaStrip.method_3322() + n9 + 15, n2 - n6 * 2, 15);
        int n12 = this.previewBox.method_3322() + this.previewBox.method_3320() + 8;
        this.hexField = new class_342(this.field_22793, n4 + n6, n12, n2 - n6 * 2, 16, (class_2561)class_2561.method_43470((String)ColorPickerScreen.un("\u5419\u847e\ua815\u31c3\u4f11", -1388234396, 0x165923CE ^ CryptoVault.mask())));
        this.hexField.method_1880(9);
        this.hexField.method_1852(this.currentHexText());
        this.hexField.method_1863(this::onHexChanged);
        this.method_37063((class_364)this.hexField);
    }

    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        ISlRenderer iSlRenderer = Renderer.get();
        iSlRenderer.beginFrame(this.field_22789, this.field_22790, this.field_22787.method_22683().method_4495());
        iSlRenderer.drawRect(class_3322, 0.0f, 0.0f, this.field_22789, this.field_22790, new Color(0, 0, 0, 100));
        this.refreshPalette();
        this.drawPanel(iSlRenderer, class_3322);
        this.drawSatBriBox(iSlRenderer, class_3322);
        this.drawHueStrip(iSlRenderer, class_3322);
        this.drawAlphaStrip(iSlRenderer, class_3322);
        this.drawPreviewAndText(iSlRenderer, class_3322);
        iSlRenderer.endFrame();
        this.hexField.method_25394(class_3322, n2, n3, f2);
    }

    private void drawPanel(ISlRenderer iSlRenderer, class_332 class_3322) {
        Color color = this.panelColor;
        Color color2 = this.borderColor;
        iSlRenderer.drawRoundedRect(class_3322, this.panelBounds.method_3321(), this.panelBounds.method_3322(), this.panelBounds.method_3319(), this.panelBounds.method_3320(), 8.0f, color);
        iSlRenderer.drawRoundedRectOutline(class_3322, this.panelBounds.method_3321(), this.panelBounds.method_3322(), this.panelBounds.method_3319(), this.panelBounds.method_3320(), 8.0f, 1.0f, color2);
        iSlRenderer.drawCenteredText(class_3322, ColorPickerScreen.un("\u0b84\ud1ca\uf9b4\u8100\u3a98\u15d2\u16bd\u18f7\ub9d6\u6772\ucf27\u1f53\ud844", 105393458, 0x165923CE ^ CryptoVault.mask()), (float)this.panelBounds.method_3321() + (float)this.panelBounds.method_3319() / 2.0f, this.panelBounds.method_3322() + 8, 10.0f, this.titleColor, HitboxConfigScreen752809.Y());
    }

    private void refreshPalette() {
        ThemedPalette themedPalette = ThemedPalette.current();
        this.panelColor = themedPalette.panel;
        this.borderColor = themedPalette.border;
        this.titleColor = themedPalette.title;
        this.mutedColor = themedPalette.muted;
        this.controlColor = themedPalette.control;
    }

    private void drawSatBriBox(ISlRenderer iSlRenderer, class_332 class_3322) {
        float f2 = this.sbBox.method_3321();
        float f3 = this.sbBox.method_3322();
        float f4 = this.sbBox.method_3319();
        float f5 = this.sbBox.method_3320();
        Color color = Color.getHSBColor(this.hue, 1.0f, 1.0f);
        iSlRenderer.drawRect(class_3322, f2, f3, f4, f5, Color.WHITE);
        iSlRenderer.drawRoundedRectGradient(class_3322, f2, f3, f4, f5, 0.0f, new Color(255, 255, 255, 0), color, false);
        iSlRenderer.drawRoundedRectGradient(class_3322, f2, f3, f4, f5, 0.0f, new Color(0, 0, 0, 0), Color.BLACK, true);
        iSlRenderer.drawRectOutline(class_3322, f2, f3, f4, f5, 1.0f, new Color(0, 0, 0, 100));
        float f6 = f2 + this.saturation * f4;
        float f7 = f3 + (1.0f - this.brightness) * f5;
        float f8 = 4.5f;
        float f9 = 3.0f;
        iSlRenderer.drawRoundedRect(class_3322, f6 - f8, f7 - f8, f8 * 2.0f, f8 * 2.0f, f8, Color.WHITE);
        iSlRenderer.drawRoundedRectOutline(class_3322, f6 - f8, f7 - f8, f8 * 2.0f, f8 * 2.0f, f8, 1.0f, new Color(0, 0, 0, 140));
        iSlRenderer.drawRoundedRect(class_3322, f6 - f9, f7 - f9, f9 * 2.0f, f9 * 2.0f, f9, Color.getHSBColor(this.hue, this.saturation, this.brightness));
    }

    private void drawHueStrip(ISlRenderer iSlRenderer, class_332 class_3322) {
        float f2 = this.hueStrip.method_3321();
        float f3 = this.hueStrip.method_3322();
        float f4 = this.hueStrip.method_3319();
        float f5 = this.hueStrip.method_3320();
        Color[] colorArray = new Color[]{Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
        float f6 = f5 / (float)(colorArray.length - 1);
        for (int i2 = 0; i2 < colorArray.length - 1; ++i2) {
            float f7 = f3 + (float)i2 * f6;
            iSlRenderer.drawRoundedRectGradient(class_3322, f2, f7, f4, f6 + 1.0f, 0.0f, colorArray[i2], colorArray[i2 + 1], true);
        }
        iSlRenderer.drawRectOutline(class_3322, f2, f3, f4, f5, 1.0f, new Color(0, 0, 0, 100));
        float f8 = f3 + this.hue * f5;
        iSlRenderer.scissor(class_3322, f2, f3, f4, f5);
        iSlRenderer.drawRect(class_3322, f2, f8 - 1.5f, f4, 3.0f, new Color(0, 0, 0, 150));
        iSlRenderer.drawRect(class_3322, f2, f8 - 1.0f, f4, 2.0f, Color.WHITE);
        iSlRenderer.resetScissor();
    }

    private void drawAlphaStrip(ISlRenderer iSlRenderer, class_332 class_3322) {
        float f2 = this.alphaStrip.method_3321();
        float f3 = this.alphaStrip.method_3322();
        float f4 = this.alphaStrip.method_3319();
        float f5 = this.alphaStrip.method_3320();
        Color color = Color.getHSBColor(this.hue, this.saturation, this.brightness);
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        iSlRenderer.drawRoundedRectGradient(class_3322, f2, f3, f4, f5, 2.0f, color2, color, false);
        float f6 = Math.max(f2 + 0.5f, Math.min(f2 + f4 - 0.5f, f2 + this.alpha * f4));
        iSlRenderer.scissor(class_3322, f2, f3, f4, f5);
        iSlRenderer.drawRect(class_3322, f6 - 1.5f, f3, 3.0f, f5, new Color(0, 0, 0, 150));
        iSlRenderer.drawRect(class_3322, f6 - 1.0f, f3, 2.0f, f5, Color.WHITE);
        iSlRenderer.resetScissor();
    }

    private void drawPreviewAndText(ISlRenderer iSlRenderer, class_332 class_3322) {
        Color color = this.getColor();
        String string = String.format(Locale.ROOT, ColorPickerScreen.un("\u5fab\ubc3d\u00c6\u82d8\ub4e4\ua409\u8897\u4dde\u4d5f\u90e2\u8050\u3f3c\u0afa\u68a1\uf694\u4e6b\ucaa4\u84e8", -1576052397, 0x165923CE ^ CryptoVault.mask()), color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()).toUpperCase();
        iSlRenderer.drawText(class_3322, string, this.previewBox.method_3321(), this.previewBox.method_3322() - 9, 8.0f, this.mutedColor, HitboxConfigScreen752809.Y());
        iSlRenderer.drawText(class_3322, ColorPickerScreen.un("\ufd0c\ubba7\ue1fb\u106f\ua290\ub65c\u13d1", -1299148818, 0x165923CE ^ CryptoVault.mask()), this.alphaStrip.method_3321(), this.alphaStrip.method_3322() - 8, 7.0f, this.mutedColor, HitboxConfigScreen752809.Y());
        iSlRenderer.drawRoundedRect(class_3322, this.previewBox.method_3321(), this.previewBox.method_3322(), this.previewBox.method_3319(), this.previewBox.method_3320(), 4.0f, this.controlColor);
        iSlRenderer.drawRoundedRect(class_3322, this.previewBox.method_3321(), this.previewBox.method_3322(), this.previewBox.method_3319(), this.previewBox.method_3320(), 4.0f, color);
        iSlRenderer.drawRoundedRectOutline(class_3322, this.previewBox.method_3321(), this.previewBox.method_3322(), this.previewBox.method_3319(), this.previewBox.method_3320(), 4.0f, 1.0f, this.borderColor);
    }

    private Color getColor() {
        Color color = Color.getHSBColor(this.hue, this.saturation, this.brightness);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(this.alpha * 255.0f));
    }

    private void updateColor() {
        this.onColorChange.accept(this.getColor());
        this.syncHexField();
    }

    private String currentHexText() {
        Color color = this.getColor();
        return String.format(Locale.ROOT, ColorPickerScreen.un("\u156a\u5a90\u576d\u63bf\u203e\uf67f\u0fe7\u35d7\ubb4f\u48d7\ue58f\ud04d\udeb5\u8491\u848f\u7e98\u5388\u899f", -841226916, 0x165923CE ^ CryptoVault.mask()), color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    private void syncHexField() {
        if (this.hexField != null && !this.hexField.method_25370()) {
            this.hexField.method_1852(this.currentHexText());
        }
    }

    private void onHexChanged(String string) {
        Color color = ColorPickerScreen.parseHexColor(string);
        if (color == null) {
            return;
        }
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = fArray[0];
        this.saturation = fArray[1];
        this.brightness = fArray[2];
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.onColorChange.accept(this.getColor());
    }

    private static Color parseHexColor(String string) {
        if (string == null) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("#")) {
            string2 = string2.substring(1);
        }
        try {
            if (string2.length() == 6) {
                int n2 = Integer.parseInt(string2, 16);
                return new Color(n2 >> 16 & 0xFF, n2 >> 8 & 0xFF, n2 & 0xFF, 255);
            }
            if (string2.length() == 8) {
                long l2 = Long.parseLong(string2, 16);
                int n3 = (int)(l2 >> 24 & 0xFFL);
                int n4 = (int)(l2 >> 16 & 0xFFL);
                int n5 = (int)(l2 >> 8 & 0xFFL);
                int n6 = (int)(l2 & 0xFFL);
                return new Color(n4, n5, n6, n3);
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return null;
    }

    public boolean method_25402(class_11909 class_119092, boolean bl) {
        double d2 = class_119092.comp_4798();
        double d3 = class_119092.comp_4799();
        int n2 = class_119092.method_74245();
        if (n2 == 0) {
            if (this.sbBox.method_3318((int)d2, (int)d3)) {
                this.draggingSB = true;
                this.updateSB(d2, d3);
                return true;
            }
            if (this.hueStrip.method_3318((int)d2, (int)d3)) {
                this.draggingHue = true;
                this.updateHue(d3);
                return true;
            }
            if (this.alphaStrip.method_3318((int)d2, (int)d3)) {
                this.draggingAlpha = true;
                this.updateAlpha(d2);
                return true;
            }
        }
        return super.method_25402(class_119092, bl);
    }

    public boolean method_25403(class_11909 class_119092, double d2, double d3) {
        double d4 = class_119092.comp_4798();
        double d5 = class_119092.comp_4799();
        if (this.draggingSB) {
            this.updateSB(d4, d5);
        } else if (this.draggingHue) {
            this.updateHue(d5);
        } else if (this.draggingAlpha) {
            this.updateAlpha(d4);
        }
        return super.method_25403(class_119092, d2, d3);
    }

    public boolean method_25406(class_11909 class_119092) {
        this.draggingSB = false;
        this.draggingHue = false;
        this.draggingAlpha = false;
        return super.method_25406(class_119092);
    }

    private void updateSB(double d2, double d3) {
        this.saturation = (float)((d2 - (double)this.sbBox.method_3321()) / (double)this.sbBox.method_3319());
        this.brightness = 1.0f - (float)((d3 - (double)this.sbBox.method_3322()) / (double)this.sbBox.method_3320());
        this.clamp();
        this.updateColor();
    }

    private void updateHue(double d2) {
        this.hue = (float)((d2 - (double)this.hueStrip.method_3322()) / (double)this.hueStrip.method_3320());
        this.clamp();
        this.updateColor();
    }

    private void updateAlpha(double d2) {
        this.alpha = (float)((d2 - (double)this.alphaStrip.method_3321()) / (double)this.alphaStrip.method_3319());
        this.clamp();
        this.updateColor();
    }

    private void clamp() {
        this.hue = Math.max(0.0f, Math.min(1.0f, this.hue));
        this.saturation = Math.max(0.0f, Math.min(1.0f, this.saturation));
        this.brightness = Math.max(0.0f, Math.min(1.0f, this.brightness));
        this.alpha = Math.max(0.0f, Math.min(1.0f, this.alpha));
    }

    public boolean method_25404(class_11908 class_119082) {
        int n2 = class_119082.comp_4795();
        if (n2 == 256) {
            this.method_25419();
            return true;
        }
        return super.method_25404(class_119082);
    }

    public void method_25419() {
        McCompat.setScreen(this.field_22787, this.parent);
    }

    private static /* synthetic */ String un(String string, int n2, int n3) {
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

