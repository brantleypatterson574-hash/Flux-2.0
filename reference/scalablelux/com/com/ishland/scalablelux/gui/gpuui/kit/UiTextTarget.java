/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public interface UiTextTarget {
    public boolean charTyped(String var1);

    public boolean keyPressed(int var1, int var2);

    default public boolean mouseDragged(double d2, double d3) {
        return false;
    }

    default public void mouseReleased() {
    }

    default public void onFocus() {
    }

    default public void onBlur() {
    }
}

