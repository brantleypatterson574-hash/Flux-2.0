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
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiTabs
extends UiElement
implements UiWidget {
    private final List<String> labels;
    private final IntConsumer onSelect;
    private int selected;

    public UiTabs(String string, List<String> list, int n2, IntConsumer intConsumer) {
        super(string);
        this.labels = List.copyOf(list);
        if (this.labels.isEmpty()) {
            throw new IllegalArgumentException(UiTabs.dz("\u6701\u70d2\u4a12\u3202\u45f3\u93cf\u49e2\u609b\u2b78\uba78\u8d73\udd59\u5452\ue215\u465e\u065b\uc062\ua712\u7d5c\u8025\u4dc1\u3a94\u15a5\ubf87\u25b0\u4f1a\u8f0a\udde2\uf78c\uc430\ue725\u616f\u0406", -727119272, 0x683D51EB ^ CryptoVault.mask()));
        }
        this.onSelect = Objects.requireNonNull(intConsumer, UiTabs.dz("\u87fb\u82bb\ub1f2\ucf3f\ub80b\ub4e8\u190f\u6e59\u68cb\u91e6", 785317689, 0x683D51EB ^ CryptoVault.mask()));
        this.setFocusable(true);
        this.setSelected(n2, false);
    }

    public int selected() {
        return this.selected;
    }

    public void setSelected(int n2) {
        this.setSelected(n2, true);
    }

    private void setSelected(int n2, boolean bl) {
        this.selected = Math.max(0, Math.min(this.labels.size() - 1, n2));
        if (bl) {
            this.onSelect.accept(this.selected);
        }
    }

    @Override
    public void paint(UiDisplayList.Builder builder) {
        UiBounds uiBounds = this.bounds();
        float f2 = uiBounds.width() / (float)this.labels.size();
        for (int i2 = 0; i2 < this.labels.size(); ++i2) {
            float f3 = uiBounds.x() + (float)i2 * f2;
            boolean bl = i2 == this.selected;
            builder.rect(f3, uiBounds.y(), f2, uiBounds.height(), Math.min(6.0f, uiBounds.height() * 0.5f), bl ? -13357744 : -14670029);
            builder.text(f3 + 8.0f, uiBounds.y() + (uiBounds.height() - 10.0f) * 0.5f, 9.0f, bl ? -1446411 : -7497302, this.labels.get(i2));
        }
    }

    @Override
    public boolean onPointerDown(float f2, float f3, int n2) {
        if (n2 != 0 || this.bounds().width() <= 0.0f) {
            return false;
        }
        int n3 = (int)((f2 - this.bounds().x()) / this.bounds().width() * (float)this.labels.size());
        this.setSelected(n3);
        return true;
    }

    @Override
    public boolean onKeyPressed(int n2, int n3) {
        if (n2 == 263) {
            this.setSelected(Math.floorMod(this.selected - 1, this.labels.size()));
            return true;
        }
        if (n2 == 262) {
            this.setSelected((this.selected + 1) % this.labels.size());
            return true;
        }
        return super.onKeyPressed(n2, n3);
    }

    private static /* synthetic */ String dz(String string, int n2, int n3) {
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

