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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiPanel
extends UiElement
implements UiWidget {
    private float radius;
    private int color;

    public UiPanel(String string, int n2, float f2) {
        super(string);
        this.color = n2;
        this.radius = Math.max(0.0f, f2);
    }

    public void setColor(int n2) {
        this.color = n2;
    }

    public void setRadius(float f2) {
        this.radius = Math.max(0.0f, f2);
    }

    @Override
    public void paint(UiDisplayList.Builder builder) {
        UiBounds uiBounds = this.bounds();
        builder.rect(uiBounds.x(), uiBounds.y(), uiBounds.width(), uiBounds.height(), this.radius, this.color);
        for (UiElement uiElement : this.children()) {
            if (!uiElement.visible() || !uiElement.enabled() || !(uiElement instanceof UiWidget)) continue;
            UiWidget uiWidget = (UiWidget)((Object)uiElement);
            uiWidget.paint(builder);
        }
    }
}

