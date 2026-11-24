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
 *  net.minecraft.class_437
 *  net.minecraft.class_768
 */
package com.ishland.scalablelux.gui.new_ui;

import com.ishland.scalablelux.gui.new_ui.CrosshairGrid;
import com.ishland.scalablelux.gui.new_ui.ThemedPalette;
import com.ishland.scalablelux.l.b;
import com.ishland.scalablelux.l.c;
import com.ishland.scalablelux.module.hooks.CrosshairMakerSource;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.ISlRenderer;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.Renderer;
import java.awt.Color;
import java.util.Arrays;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_768;

@Environment(value=EnvType.CLIENT)
public class CrosshairMakerScreen
extends class_437 {
    private static final int CELL_PX = 16;
    private static final int GRID = 17;
    private final class_437 parent;
    private final CrosshairMakerSource module;
    private final boolean[] grid;
    private boolean mirror = true;
    private boolean dragging;
    private boolean dragValue;
    private class_768 panelBounds;
    private class_768 gridBounds;
    private class_768 clearButton;
    private class_768 mirrorButton;
    private class_768 doneButton;
    private ThemedPalette palette = ThemedPalette.current();

    public CrosshairMakerScreen(class_437 class_4372, CrosshairMakerSource crosshairMakerSource) {
        super((class_2561)class_2561.method_43470((String)CrosshairMakerScreen.lj("\uc788\u0ee0\u60a9\uaed7\u529d\ue237\u3e04\u7c82\ufa1d\ufd1b\u73b9\u5f61\u9cb3\uf487\u2242\u9c25", 1224210579, 0xCEF537D1 ^ CryptoVault.mask())));
        this.parent = class_4372;
        this.module = crosshairMakerSource;
        this.grid = CrosshairGrid.decode(crosshairMakerSource.a().X());
    }

    protected void method_25426() {
        int n2 = 272;
        int n3 = 18;
        int n4 = 34;
        int n5 = 40;
        int n6 = n2 + n3 * 2;
        int n7 = n2 + n3 * 2 + n4 + n5;
        int n8 = (this.field_22789 - n6) / 2;
        int n9 = (this.field_22790 - n7) / 2;
        this.panelBounds = new class_768(n8, n9, n6, n7);
        this.gridBounds = new class_768(n8 + n3, n9 + n3 + n4, n2, n2);
        int n10 = this.gridBounds.method_3322() + this.gridBounds.method_3320() + 10;
        int n11 = 22;
        int n12 = 8;
        int n13 = (n6 - n3 * 2 - n12 * 2) / 3;
        this.clearButton = new class_768(n8 + n3, n10, n13, n11);
        this.mirrorButton = new class_768(this.clearButton.method_3321() + n13 + n12, n10, n13, n11);
        this.doneButton = new class_768(this.mirrorButton.method_3321() + n13 + n12, n10, n13, n11);
    }

    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        ISlRenderer iSlRenderer = Renderer.get();
        iSlRenderer.beginFrame(this.field_22789, this.field_22790, this.field_22787.method_22683().method_4495());
        iSlRenderer.drawRect(class_3322, 0.0f, 0.0f, this.field_22789, this.field_22790, new Color(0, 0, 0, 120));
        this.palette = ThemedPalette.current();
        this.drawPanel(iSlRenderer, class_3322);
        this.drawGrid(iSlRenderer, class_3322, n2, n3);
        this.drawButtons(iSlRenderer, class_3322, n2, n3);
        iSlRenderer.endFrame();
    }

    private void drawPanel(ISlRenderer iSlRenderer, class_332 class_3322) {
        b b2 = c.aa().aa();
        Color color = this.palette.panel;
        Color color2 = this.palette.border;
        iSlRenderer.drawRoundedRect(class_3322, this.panelBounds.method_3321(), this.panelBounds.method_3322(), this.panelBounds.method_3319(), this.panelBounds.method_3320(), 8.0f, color);
        iSlRenderer.drawRoundedRectOutline(class_3322, this.panelBounds.method_3321(), this.panelBounds.method_3322(), this.panelBounds.method_3319(), this.panelBounds.method_3320(), 8.0f, 1.0f, color2);
        iSlRenderer.drawCenteredText(class_3322, CrosshairMakerScreen.lj("\ud846\u9875\u3df6\u3cf6\u0e16\ue453\ufa6e\u9208\u1323\u8ef8\u3be0\u7b96\u217f\u8214\u7441\ub6fb\ubc81\ufa60\u9a87", -1013741634, 0xCEF537D1 ^ CryptoVault.mask()), (float)this.panelBounds.method_3321() + (float)this.panelBounds.method_3319() / 2.0f, this.panelBounds.method_3322() + 10, 10.0f, this.palette.title, HitboxConfigScreen752809.Y());
        iSlRenderer.drawCenteredText(class_3322, CrosshairMakerScreen.lj("\u921c\uc56d\u1090\uc745\u8e99\u11a1\ua04e\u79ab\u5794\u8a7a\uf33a\ud1a4\ua918\uc7c2\u633c\u91bd\u369a\u956d\u6a91\u9bad\ueb26\u7edf\u6ea3\u9213\ubc78\u4ae3\u5d5d\u7e7d\u06de\u335d\ud51c\ufd64\u245d\u2bbd\u9129\u3394\u6d72\u7aca\ub3c5\u7643\u1cd8\uc0eb\u6bd5\u2096\u8017\u9cd7\ub3ea.\u8f7f\u6a78\ub66c\u6162\ubdde\u6078", 1938534612, 0xCEF537D1 ^ CryptoVault.mask()), (float)this.panelBounds.method_3321() + (float)this.panelBounds.method_3319() / 2.0f, this.panelBounds.method_3322() + 22, 7.0f, this.palette.muted, HitboxConfigScreen752809.Y());
    }

    private void drawGrid(ISlRenderer iSlRenderer, class_332 class_3322, int n2, int n3) {
        int n4;
        int n5;
        int n6 = this.gridBounds.method_3321();
        int n7 = this.gridBounds.method_3322();
        Color color = this.module.b();
        Color color2 = this.module.c() ? this.module.d() : null;
        Color color3 = this.palette.control;
        Color color4 = new Color(0, 0, 0, 90);
        int[] nArray = this.cellAt(n2, n3);
        iSlRenderer.drawRect(class_3322, n6, n7, this.gridBounds.method_3319(), this.gridBounds.method_3320(), color3);
        for (n5 = 0; n5 < 17; ++n5) {
            for (n4 = 0; n4 < 17; ++n4) {
                int n8 = n6 + n4 * 16;
                int n9 = n7 + n5 * 16;
                if (this.grid[n5 * 17 + n4]) {
                    iSlRenderer.drawRect(class_3322, n8, n9, 16.0f, 16.0f, color);
                    if (color2 != null) {
                        iSlRenderer.drawRectOutline(class_3322, n8, n9, 16.0f, 16.0f, 1.0f, color2);
                    }
                }
                iSlRenderer.drawRectOutline(class_3322, n8, n9, 16.0f, 16.0f, 1.0f, color4);
            }
        }
        if (nArray != null) {
            n5 = n6 + nArray[0] * 16;
            n4 = n7 + nArray[1] * 16;
            iSlRenderer.drawRectOutline(class_3322, n5, n4, 16.0f, 16.0f, 1.5f, this.palette.accent);
        }
        iSlRenderer.drawRectOutline(class_3322, n6, n7, this.gridBounds.method_3319(), this.gridBounds.method_3320(), 1.0f, this.palette.line);
    }

    private void drawButtons(ISlRenderer iSlRenderer, class_332 class_3322, int n2, int n3) {
        this.drawButton(iSlRenderer, class_3322, this.clearButton, CrosshairMakerScreen.lj("\uf3c6\u269b\"\uffa2\u75a7\u8993\ud11d\u35f1\u92df", 759167728, 0xCEF537D1 ^ CryptoVault.mask()), n2, n3);
        this.drawButton(iSlRenderer, class_3322, this.mirrorButton, this.mirror ? CrosshairMakerScreen.lj("\u50ab\ue741\u5d45\u4dea\ued1d\u2a0b\uc8dc\u7f4d\u63f0\u117f\u2e09", 1253221554, 0xCEF537D1 ^ CryptoVault.mask()) : CrosshairMakerScreen.lj("\ucfc7\u8d44\u0f97\u4cec\uabd2\ua6df\u8a6a\u6ea0\u9378\ub3a9\uf34c\u8d92\u41e4\ucfb9\u4e8d", -872718561, 0xCEF537D1 ^ CryptoVault.mask()), n2, n3);
        this.drawButton(iSlRenderer, class_3322, this.doneButton, CrosshairMakerScreen.lj("\u4fa1\u361b\u1a82\ubd30\uc47e\u040a\u4eb5\u1588", -2122455978, 0xCEF537D1 ^ CryptoVault.mask()), n2, n3);
    }

    private void drawButton(ISlRenderer iSlRenderer, class_332 class_3322, class_768 class_7682, String string, int n2, int n3) {
        boolean bl = class_7682.method_3318(n2, n3);
        Color color = bl ? this.palette.controlHover : this.palette.control;
        iSlRenderer.drawRoundedRect(class_3322, class_7682.method_3321(), class_7682.method_3322(), class_7682.method_3319(), class_7682.method_3320(), 4.0f, color);
        iSlRenderer.drawRoundedRectOutline(class_3322, class_7682.method_3321(), class_7682.method_3322(), class_7682.method_3319(), class_7682.method_3320(), 4.0f, 1.0f, bl ? this.palette.accent : this.palette.line);
        iSlRenderer.drawCenteredText(class_3322, string, (float)class_7682.method_3321() + (float)class_7682.method_3319() / 2.0f, (float)class_7682.method_3322() + (float)class_7682.method_3320() / 2.0f - 3.0f, 8.0f, this.palette.title, HitboxConfigScreen752809.Y());
    }

    private int[] cellAt(double d2, double d3) {
        if (!this.gridBounds.method_3318((int)d2, (int)d3)) {
            return null;
        }
        int n2 = (int)((d2 - (double)this.gridBounds.method_3321()) / 16.0);
        int n3 = (int)((d3 - (double)this.gridBounds.method_3322()) / 16.0);
        if (n2 < 0 || n3 < 0 || n2 >= 17 || n3 >= 17) {
            return null;
        }
        return new int[]{n2, n3};
    }

    private void paint(int n2, int n3, boolean bl) {
        this.setCell(n2, n3, bl);
        if (this.mirror) {
            int n4 = 16 - n2;
            int n5 = 16 - n3;
            this.setCell(n4, n3, bl);
            this.setCell(n2, n5, bl);
            this.setCell(n4, n5, bl);
        }
        this.module.a().G(CrosshairGrid.encode(this.grid));
    }

    private void setCell(int n2, int n3, boolean bl) {
        if (n2 < 0 || n3 < 0 || n2 >= 17 || n3 >= 17) {
            return;
        }
        this.grid[n3 * 17 + n2] = bl;
    }

    private void clearGrid() {
        Arrays.fill(this.grid, false);
        this.module.a().G(CrosshairGrid.encode(this.grid));
    }

    public boolean method_25402(class_11909 class_119092, boolean bl) {
        int[] nArray;
        double d2 = class_119092.comp_4798();
        double d3 = class_119092.comp_4799();
        int n2 = class_119092.method_74245();
        if ((n2 == 0 || n2 == 1) && (nArray = this.cellAt(d2, d3)) != null) {
            this.dragging = true;
            this.dragValue = n2 == 0;
            this.paint(nArray[0], nArray[1], this.dragValue);
            return true;
        }
        if (n2 == 0) {
            if (this.clearButton.method_3318((int)d2, (int)d3)) {
                this.clearGrid();
                return true;
            }
            if (this.mirrorButton.method_3318((int)d2, (int)d3)) {
                this.mirror = !this.mirror;
                return true;
            }
            if (this.doneButton.method_3318((int)d2, (int)d3)) {
                this.method_25419();
                return true;
            }
        }
        return super.method_25402(class_119092, bl);
    }

    public boolean method_25403(class_11909 class_119092, double d2, double d3) {
        if (this.dragging) {
            int[] nArray = this.cellAt(class_119092.comp_4798(), class_119092.comp_4799());
            if (nArray != null) {
                this.paint(nArray[0], nArray[1], this.dragValue);
            }
            return true;
        }
        return super.method_25403(class_119092, d2, d3);
    }

    public boolean method_25406(class_11909 class_119092) {
        this.dragging = false;
        return super.method_25406(class_119092);
    }

    public boolean method_25404(class_11908 class_119082) {
        if (class_119082.comp_4795() == 256) {
            this.method_25419();
            return true;
        }
        return super.method_25404(class_119082);
    }

    public void method_25419() {
        McCompat.setScreen(this.field_22787, this.parent);
    }

    private static /* synthetic */ String lj(String string, int n2, int n3) {
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

