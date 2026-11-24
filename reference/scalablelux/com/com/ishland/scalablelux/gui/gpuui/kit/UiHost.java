/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextTarget;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public interface UiHost {
    public void hit(float var1, float var2, float var3, float var4, Runnable var5);

    public void hitSecondary(float var1, float var2, float var3, float var4, Runnable var5);

    public void beginHitClip(float var1, float var2, float var3, float var4);

    public void endHitClip();

    public boolean hovered(float var1, float var2, float var3, float var4, float var5, float var6);

    public int accent();

    public int textColor();

    public int mutedColor();

    public int muted2Color();

    default public int titleColor() {
        return this.textColor();
    }

    public int lineColor();

    public int cardFill();

    public int controlFill(int var1);

    public long now();

    public float frameDelta();

    default public float anim(String string, float f2, float f3) {
        return f2;
    }

    public double pressX();

    public double pressY();

    public void focus(UiTextTarget var1);

    public UiTextTarget focused();

    public void captureKey(IntConsumer var1);

    public boolean capturingKey();

    public String clipboard();

    public void setClipboard(String var1);

    default public float cornerScale() {
        return 1.0f;
    }

    default public void panel(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        builder.rect(f2, f3, f4, f5, f6, UiKit.panelFill());
    }
}

