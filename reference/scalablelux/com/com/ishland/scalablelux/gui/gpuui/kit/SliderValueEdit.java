/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextTarget;
import com.ishland.scalablelux.module.SliderSetting;
import com.ishland.scalablelux.util.CryptoVault;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class SliderValueEdit {
    public static final String HINT = SliderValueEdit.yc("\u9c67\ua288\u61b9\ubdea\u1e88\ufcdb\u5d2a\u7e0e\u8406\u06d8\uc27d\u4a83\ub739\u583c\ube6e\ub358\ud2b4\u7019\ua25a\uf5a4\ubaf3\u479e\u6de2\ue5d3\ua734\u3b08\u6d52\ud813\u6df3\uc673\u0d63\u727d\u7724\u5dc8\u10e3\u5a8f\ub3a9\ub90d\uf93c\u082c\u4a12\uf27c\ua766\uc02b\ue2da\ue971\u2b91\ue4ef\ud6bb\ua65f\ub936\u39ae\u4e83\u9a34\u56ee\ue331\u7d32\ub4cd\ud0f6\u5a22\ucd17\u196e\u12f5\uc7ac\u13ed\u42a3\u9367\ua24f\ue483", -430856176, 0x37A522D6 ^ CryptoVault.mask());
    private final UiTextField field = new UiTextField("").maxLength(14).fontSize(10.0f).padding(8.0f, 8.0f).allow(n2 -> UiTextField.isDigit(n2) || n2 == 46 || n2 == 45 && this.target != null && this.target.D() < 0.0).onSubmit(this::submit).onBlur(this::commit);
    private SliderSetting target;
    private boolean quick;
    private UiHost lastHost;

    public UiTextTarget field() {
        return this.field;
    }

    public boolean editing() {
        return this.target != null;
    }

    public boolean editing(SliderSetting sliderSetting, boolean bl) {
        return this.target == sliderSetting && this.quick == bl;
    }

    public void begin(UiHost uiHost, SliderSetting sliderSetting, boolean bl) {
        if (this.target != null && this.target != sliderSetting) {
            this.commit();
        }
        this.target = sliderSetting;
        this.quick = bl;
        this.lastHost = uiHost;
        this.field.setValue(sliderSetting.W());
        uiHost.focus(this.field);
        this.field.selectAll();
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6) {
        this.lastHost = uiHost;
        this.field.draw(builder, uiHost, f2, f3, f4, f5, f6);
    }

    public void cancel() {
        this.target = null;
    }

    public void commit() {
        SliderSetting sliderSetting = this.target;
        if (sliderSetting == null) {
            return;
        }
        this.target = null;
        double d2 = SliderValueEdit.parse(this.field.value(), sliderSetting.D(), sliderSetting.E(), sliderSetting.W());
        if (!Double.isNaN(d2)) {
            sliderSetting.B(d2);
        }
    }

    private void submit() {
        this.commit();
        if (this.lastHost != null && this.lastHost.focused() == this.field) {
            this.lastHost.focus(null);
        }
    }

    public static double parse(String string, double d2, double d3, int n2) {
        double d4;
        if (string == null) {
            return Double.NaN;
        }
        String string2 = string.trim();
        if (string2.isEmpty()) {
            return Double.NaN;
        }
        int n3 = 0;
        boolean bl = false;
        for (int i2 = 0; i2 < string2.length(); ++i2) {
            char c2 = string2.charAt(i2);
            if (c2 >= '0' && c2 <= '9') {
                bl = true;
                continue;
            }
            if (c2 == '.') {
                ++n3;
                continue;
            }
            if (c2 == '-' && i2 == 0 && d2 < 0.0) continue;
            return Double.NaN;
        }
        if (!bl || n3 > 1) {
            return Double.NaN;
        }
        try {
            d4 = Double.parseDouble(string2);
        }
        catch (NumberFormatException numberFormatException) {
            return Double.NaN;
        }
        if (!Double.isFinite(d4)) {
            return Double.NaN;
        }
        double d5 = Math.min(d2, d3);
        double d6 = Math.max(d2, d3);
        d4 = Math.max(d5, Math.min(d6, d4));
        return BigDecimal.valueOf(d4).setScale(Math.max(0, n2), RoundingMode.HALF_UP).doubleValue();
    }

    private static /* synthetic */ String yc(String string, int n2, int n3) {
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

