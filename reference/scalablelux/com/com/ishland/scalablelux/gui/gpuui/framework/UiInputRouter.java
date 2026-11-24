/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiElement;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiInputRouter {
    private UiElement focused;
    private UiElement captured;
    private UiElement hovered;
    private float lastX;
    private float lastY;

    public UiElement focused() {
        return this.focused;
    }

    public UiElement captured() {
        return this.captured;
    }

    public UiElement hovered() {
        return this.hovered;
    }

    public void clearFocus() {
        this.focused = null;
    }

    public void setFocused(UiElement uiElement) {
        this.focused = uiElement != null && uiElement.focusable() && UiInputRouter.isActive(uiElement) ? uiElement : null;
    }

    public boolean pointerDown(UiElement uiElement, float f2, float f3, int n2) {
        UiElement uiElement2;
        this.lastX = f2;
        this.lastY = f3;
        this.hovered = uiElement2 = UiInputRouter.hit(uiElement, f2, f3);
        if (uiElement2 == null) {
            this.focused = null;
            this.captured = null;
            return false;
        }
        this.setFocused(UiInputRouter.nearestFocusable(uiElement2));
        boolean bl = uiElement2.onPointerDown(f2, f3, n2);
        this.captured = bl ? uiElement2 : null;
        return bl;
    }

    public boolean pointerMove(UiElement uiElement, float f2, float f3) {
        UiElement uiElement2;
        this.hovered = uiElement2 = UiInputRouter.hit(uiElement, f2, f3);
        UiElement uiElement3 = this.captured != null && UiInputRouter.isActive(this.captured) ? this.captured : uiElement2;
        boolean bl = uiElement3 != null && uiElement3.onPointerMove(f2, f3, f2 - this.lastX, f3 - this.lastY);
        this.lastX = f2;
        this.lastY = f3;
        return bl;
    }

    public boolean pointerUp(UiElement uiElement, float f2, float f3, int n2) {
        UiElement uiElement2 = this.captured;
        this.captured = null;
        this.hovered = UiInputRouter.hit(uiElement, f2, f3);
        return uiElement2 != null && uiElement2.onPointerUp(f2, f3, n2);
    }

    public boolean pointerCancel() {
        if (this.captured == null) {
            return false;
        }
        boolean bl = this.captured.onPointerUp(this.lastX, this.lastY, -1);
        this.captured = null;
        return bl;
    }

    public boolean scroll(UiElement uiElement, float f2, float f3, double d2, double d3) {
        for (UiElement uiElement2 = UiInputRouter.hit(uiElement, f2, f3); uiElement2 != null; uiElement2 = uiElement2.parent()) {
            if (!uiElement2.onScroll(f2, f3, d2, d3)) continue;
            return true;
        }
        return false;
    }

    public boolean keyPressed(int n2, int n3) {
        if (n2 == 258 && this.focused != null) {
            this.focusNext(this.focused.parent() == null ? this.focused : UiInputRouter.rootOf(this.focused), (n3 & 1) != 0);
            return true;
        }
        return this.focused != null && UiInputRouter.isActive(this.focused) && this.focused.onKeyPressed(n2, n3);
    }

    public void focusNext(UiElement uiElement, boolean bl) {
        ArrayList<UiElement> arrayList = new ArrayList<UiElement>();
        UiInputRouter.collectFocusable(uiElement, arrayList);
        if (arrayList.isEmpty()) {
            this.focused = null;
            return;
        }
        int n2 = arrayList.indexOf(this.focused);
        int n3 = n2 < 0 ? (bl ? arrayList.size() - 1 : 0) : Math.floorMod(n2 + (bl ? -1 : 1), arrayList.size());
        this.focused = (UiElement)arrayList.get(n3);
    }

    private static UiElement nearestFocusable(UiElement uiElement) {
        for (UiElement uiElement2 = uiElement; uiElement2 != null; uiElement2 = uiElement2.parent()) {
            if (!uiElement2.focusable()) continue;
            return uiElement2;
        }
        return null;
    }

    private static UiElement rootOf(UiElement uiElement) {
        UiElement uiElement2 = uiElement;
        while (uiElement2.parent() != null) {
            uiElement2 = uiElement2.parent();
        }
        return uiElement2;
    }

    private static void collectFocusable(UiElement uiElement, List<UiElement> list) {
        if (uiElement == null || !uiElement.visible() || !uiElement.enabled()) {
            return;
        }
        if (uiElement.focusable()) {
            list.add(uiElement);
        }
        for (UiElement uiElement2 : uiElement.children()) {
            UiInputRouter.collectFocusable(uiElement2, list);
        }
    }

    private static UiElement hit(UiElement uiElement, float f2, float f3) {
        if (!(uiElement != null && uiElement.visible() && uiElement.enabled() && uiElement.bounds().contains(f2, f3))) {
            return null;
        }
        List<UiElement> list = uiElement.children();
        for (int i2 = list.size() - 1; i2 >= 0; --i2) {
            UiElement uiElement2 = UiInputRouter.hit(list.get(i2), f2, f3);
            if (uiElement2 == null) continue;
            return uiElement2;
        }
        return uiElement;
    }

    private static boolean isActive(UiElement uiElement) {
        for (UiElement uiElement2 = uiElement; uiElement2 != null; uiElement2 = uiElement2.parent()) {
            if (uiElement2.visible() && uiElement2.enabled()) continue;
            return false;
        }
        return true;
    }
}

