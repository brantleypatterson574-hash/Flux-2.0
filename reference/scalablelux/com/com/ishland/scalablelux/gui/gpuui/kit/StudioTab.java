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
import com.ishland.scalablelux.module.hooks.StudioProvider;
import com.ishland.scalablelux.util.ModuleAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class StudioTab {
    private final Kind kind;
    private StudioProvider.Panel previewPanel;

    public StudioTab(Kind kind) {
        this.kind = kind;
    }

    private StudioProvider.Panel panel() {
        StudioProvider studioProvider;
        if (this.previewPanel != null) {
            return this.previewPanel;
        }
        try {
            studioProvider = ModuleAccess.getHook(StudioProvider.class);
        }
        catch (Throwable throwable) {
            return null;
        }
        if (studioProvider == null || !studioProvider.studioAvailable()) {
            return null;
        }
        return this.kind == Kind.MACROS ? studioProvider.macroPanel() : studioProvider.scriptPanel();
    }

    public void previewWith(StudioProvider.Panel panel) {
        this.previewPanel = panel;
    }

    public void refresh() {
        StudioProvider.Panel panel = this.panel();
        if (panel != null) {
            panel.refresh();
        }
    }

    public void flush() {
        StudioProvider.Panel panel = this.panel();
        if (panel != null) {
            panel.flush();
        }
    }

    public boolean fullscreen() {
        StudioProvider.Panel panel = this.panel();
        return panel != null && panel.fullscreen();
    }

    public void exitFullscreen() {
        StudioProvider.Panel panel = this.panel();
        if (panel != null) {
            panel.exitFullscreen();
        }
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        StudioProvider.Panel panel = this.panel();
        return panel != null && panel.scroll(d2, d3, d4, l2);
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        StudioProvider.Panel panel = this.panel();
        if (panel != null) {
            panel.draw(builder, uiHost, f2, f3, f4, f5, f6, f7, f8);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Kind {
        MACROS,
        SCRIPTS;


        private static /* synthetic */ String ed(String string, int n2, int n3) {
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
}

