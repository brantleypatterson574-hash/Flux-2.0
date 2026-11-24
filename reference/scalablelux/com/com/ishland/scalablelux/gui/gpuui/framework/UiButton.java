/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiBounds;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiElement;
import com.ishland.scalablelux.gui.gpuui.framework.UiWidget;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiButton
extends UiElement
implements UiWidget {
    private String label;
    private final Runnable action;
    private int color;
    private int hoverColor;
    private int textColor;
    private float radius;
    private float textInset;
    private float textSize = 9.0f;
    private boolean hovered;

    public UiButton(String string, String string2, Runnable runnable, int n2, int n3, int n4, float f2, float f3) {
        super(string);
        this.setFocusable(true);
        this.label = Objects.requireNonNull(string2, UiButton.cn("\u2ddc\u142a\ua5fe\u04c3\u36ff\u17af", -238263439, 0x239C8959 ^ CryptoVault.mask()));
        this.action = Objects.requireNonNull(runnable, UiButton.cn("\u21b3\u2aa4\u7eec\u52e3\uc9e1\uf4d3\uacb3\u866d", 1511636752, 0x239C8959 ^ CryptoVault.mask()));
        this.color = n2;
        this.hoverColor = n3;
        this.textColor = n4;
        this.radius = Math.max(0.0f, f2);
        this.textInset = Math.max(0.0f, f3);
    }

    public void setLabel(String string) {
        this.label = Objects.requireNonNull(string, UiButton.cn("\ub4e7\u72f4\ucc9f\u3a0b\u1b3c\u4f5d\u9fca", -911266273, 0x239C8959 ^ CryptoVault.mask()));
    }

    public void setHovered(boolean bl) {
        this.hovered = bl;
    }

    public void setTextSize(float f2) {
        this.textSize = Math.max(1.0f, f2);
    }

    public void setColors(int n2, int n3, int n4) {
        this.color = n2;
        this.hoverColor = n3;
        this.textColor = n4;
    }

    @Override
    public void paint(UiDisplayList.Builder builder) {
        UiBounds uiBounds = this.bounds();
        builder.rect(uiBounds.x(), uiBounds.y(), uiBounds.width(), uiBounds.height(), this.radius, this.hovered ? this.hoverColor : this.color);
        builder.text(uiBounds.x() + this.textInset, uiBounds.y() + (uiBounds.height() - this.textSize) * 0.5f, this.textSize, this.textColor, this.label);
    }

    @Override
    public boolean onPointerDown(float f2, float f3, int n2) {
        if (n2 != 0 || !this.enabled()) {
            return false;
        }
        this.action.run();
        return true;
    }

    @Override
    public boolean onPointerUp(float f2, float f3, int n2) {
        return n2 == 0;
    }

    private static /* synthetic */ String cn(String string, int n2, int n3) {
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

