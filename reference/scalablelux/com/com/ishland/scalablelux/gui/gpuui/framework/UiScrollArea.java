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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiScrollArea
extends UiElement
implements UiWidget {
    private final Map<UiElement, UiBounds> contentBounds = new IdentityHashMap<UiElement, UiBounds>();
    private float scrollOffset;
    private float contentHeight;
    private float radius;

    public UiScrollArea(String string, float f2) {
        super(string);
        this.radius = Math.max(0.0f, f2);
    }

    public float scrollOffset() {
        return this.scrollOffset;
    }

    public float maxScrollOffset() {
        return Math.max(0.0f, this.contentHeight - this.bounds().height());
    }

    public void setRadius(float f2) {
        this.radius = Math.max(0.0f, f2);
    }

    public void addContent(UiElement uiElement, UiBounds uiBounds) {
        this.add(uiElement);
        this.setContentBounds(uiElement, uiBounds);
    }

    public void setContentBounds(UiElement uiElement, UiBounds uiBounds) {
        if (uiElement.parent() != this) {
            throw new IllegalArgumentException(UiScrollArea.ss("\u2715\u7cc9\u84aa\uff80\ucb0b\uc33b\uccd8\u90e9\u7794\u5b2b\ucb1c\u6916\u258d\u1cf2\u95ce\u6740\u45c3\u18d7\u5318\u5b5d\u4af4\ue49b\u0a14\u44cb\u6d34\udef4\u6e39\u2371\u3fa0\u2f68\uf169\ub87d\u410a\uabf6\u890b\udff8\ub305\ua2aa\u965b\u47fe\ufc04\uc0fc\ufefa\u722f\u5f42\u8523\u609a\ub582\u82ee\u6391\u2caa\u633d", -639774053, 0x9BFAB50F ^ CryptoVault.mask()));
        }
        this.contentBounds.put(uiElement, uiBounds);
        this.applyOffset(uiElement, uiBounds);
        this.contentHeight = Math.max(this.contentHeight, uiBounds.y() + uiBounds.height() - this.bounds().y());
        this.clampOffset();
    }

    public void setContentHeight(float f2) {
        if (!Float.isFinite(f2)) {
            throw new IllegalArgumentException(UiScrollArea.ss("\u0584\ueb62\ue23a\uc23e\udb3c\uef65\ua682\u06c4\ue164\u44d4\udbd6\u8dee\u8395\u6e91\uac66\u31d2\uc6cc\ub11b\uc067\u3f19\u66c2\ue946\u3105\u414f\uf418\u3dfe\u3650\uc9cd\u19ce\u7119\u2157", 1091401898, 0x9BFAB50F ^ CryptoVault.mask()));
        }
        this.contentHeight = Math.max(0.0f, f2);
        this.clampOffset();
        this.relayout();
    }

    public void setScrollOffset(float f2) {
        if (!Float.isFinite(f2)) {
            return;
        }
        this.scrollOffset = Math.max(0.0f, Math.min(this.maxScrollOffset(), f2));
        this.relayout();
    }

    public void scrollBy(float f2) {
        if (!Float.isFinite(f2)) {
            return;
        }
        this.setScrollOffset(this.scrollOffset + f2);
    }

    public void layoutVertical(float f2, float f3, float f4, float f5, List<Float> list) {
        if (list.size() != this.children().size()) {
            throw new IllegalArgumentException(UiScrollArea.ss("\u9076\ueee8\ueea2\u35e9\u723f\u1f50\ub16f\udb37\u3872\u817c\u77c2\u3159\u815d\u2b48\uff3a\ud693\u32c0\uc089\u75d1\u63a7\u37a4\u38c2\uc2ed\u4654\u2a1d\u420f\ue9b7\u6f21\u7459\u1057\u132a\ubc4d\uf75d", 298519407, 0x9BFAB50F ^ CryptoVault.mask()));
        }
        float f6 = f3;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            UiElement uiElement = this.children().get(i2);
            float f7 = Math.max(0.0f, list.get(i2).floatValue());
            this.setContentBounds(uiElement, new UiBounds(f2, f6, f4, f7));
            f6 += f7 + f5;
        }
        this.contentHeight = Math.max(0.0f, f6 - f5 - this.bounds().y());
        this.clampOffset();
        this.relayout();
    }

    @Override
    public boolean onScroll(float f2, float f3, double d2, double d3) {
        if (d3 == 0.0 || this.maxScrollOffset() <= 0.0f) {
            return false;
        }
        this.scrollBy((float)(-d3 * 24.0));
        return true;
    }

    @Override
    public void paint(UiDisplayList.Builder builder) {
        UiBounds uiBounds = this.bounds();
        builder.clipPush(uiBounds.x(), uiBounds.y(), uiBounds.width(), uiBounds.height(), this.radius);
        for (UiElement uiElement : this.children()) {
            if (!uiElement.visible() || !(uiElement instanceof UiWidget)) continue;
            UiWidget uiWidget = (UiWidget)((Object)uiElement);
            uiWidget.paint(builder);
        }
        builder.clipPop();
    }

    private void clampOffset() {
        this.scrollOffset = Math.max(0.0f, Math.min(this.maxScrollOffset(), this.scrollOffset));
    }

    private void relayout() {
        for (UiElement uiElement : this.children()) {
            UiBounds uiBounds = this.contentBounds.get(uiElement);
            if (uiBounds == null) continue;
            this.applyOffset(uiElement, uiBounds);
        }
    }

    private void applyOffset(UiElement uiElement, UiBounds uiBounds) {
        uiElement.setBounds(new UiBounds(uiBounds.x(), uiBounds.y() - this.scrollOffset, uiBounds.width(), uiBounds.height()));
    }

    private static /* synthetic */ String ss(String string, int n2, int n3) {
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

