/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiBounds;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class UiElement {
    private final String id;
    private final List<UiElement> children = new ArrayList<UiElement>();
    private UiElement parent;
    private UiBounds bounds = new UiBounds(0.0f, 0.0f, 0.0f, 0.0f);
    private boolean visible = true;
    private boolean enabled = true;
    private boolean focusable;

    public UiElement(String string) {
        this.id = Objects.requireNonNull(string, "id");
    }

    public String id() {
        return this.id;
    }

    public UiElement parent() {
        return this.parent;
    }

    public List<UiElement> children() {
        return Collections.unmodifiableList(this.children);
    }

    public UiBounds bounds() {
        return this.bounds;
    }

    public void setBounds(UiBounds uiBounds) {
        this.bounds = Objects.requireNonNull(uiBounds, UiElement.ta("\u02ce\u2585\ud284\u16bc\uc6d5\u3056\u64e7\uff9a\ub029", 295384448, 0x96238EC0 ^ CryptoVault.mask()));
    }

    public boolean visible() {
        return this.visible;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    public boolean focusable() {
        return this.focusable;
    }

    public void setFocusable(boolean bl) {
        this.focusable = bl;
    }

    public void add(UiElement uiElement) {
        Objects.requireNonNull(uiElement, UiElement.ta("\uf5d3\ub202\u4cdd\u926d\u7679\u024f\ud340", -2134926089, 0x96238EC0 ^ CryptoVault.mask()));
        if (uiElement == this || this.isDescendantOf(uiElement)) {
            throw new IllegalArgumentException(UiElement.ta("\u6b96\u558f\u932e\u9117\ua81a\uf7af\ud474\u5d04\u1285\uc0a0\uaf08\u2bd1\u744e\u91b9\uaf52\ua01b\u3209\u4a60\u8dfd\u5a56\ub313\uf9eb\u60e9\ue38e\u6168\ub0e8\u8dda\ud3e5\u5f3d\uafe6\u3274\u3b4c\ub2b0\u182d", 1791979455, 0x96238EC0 ^ CryptoVault.mask()));
        }
        if (uiElement.parent != null) {
            uiElement.parent.remove(uiElement);
        }
        uiElement.parent = this;
        this.children.add(uiElement);
    }

    public boolean remove(UiElement uiElement) {
        if (!this.children.remove(uiElement)) {
            return false;
        }
        uiElement.parent = null;
        return true;
    }

    private boolean isDescendantOf(UiElement uiElement) {
        UiElement uiElement2 = this;
        while (uiElement2 != null) {
            if (uiElement2 == uiElement) {
                return true;
            }
            uiElement2 = uiElement2.parent;
        }
        return false;
    }

    public boolean onPointerDown(float f2, float f3, int n2) {
        return false;
    }

    public boolean onPointerMove(float f2, float f3, float f4, float f5) {
        return false;
    }

    public boolean onPointerUp(float f2, float f3, int n2) {
        return false;
    }

    public boolean onScroll(float f2, float f3, double d2, double d3) {
        return false;
    }

    public boolean onKeyPressed(int n2, int n3) {
        if (n2 == 257 || n2 == 32) {
            UiBounds uiBounds = this.bounds;
            return this.onPointerDown(uiBounds.x() + uiBounds.width() * 0.5f, uiBounds.y() + uiBounds.height() * 0.5f, 0);
        }
        return false;
    }

    private static /* synthetic */ String ta(String string, int n2, int n3) {
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

