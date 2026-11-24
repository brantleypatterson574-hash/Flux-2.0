/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.new_ui;

import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.l.a;
import java.awt.Color;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
final class ThemedPalette {
    final Color panel;
    final Color border;
    final Color title;
    final Color muted;
    final Color control;
    final Color controlHover;
    final Color line;
    final Color accent;

    private ThemedPalette(Color color, Color color2, Color color3, Color color4, Color color5, Color color6, Color color7, Color color8) {
        this.panel = color;
        this.border = color2;
        this.title = color3;
        this.muted = color4;
        this.control = color5;
        this.controlHover = color6;
        this.line = color7;
        this.accent = color8;
    }

    static ThemedPalette current() {
        UiThemeMaterial.Theme theme = UiThemeMaterial.theme(HitboxConfigScreen752809.a == null ? "" : HitboxConfigScreen752809.a.getCurrentMode());
        int n2 = theme.panelBase() & 0xFFFFFF;
        int n3 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ai, theme.tint()) & 0xFFFFFF;
        int n4 = a.at() & 0xFFFFFF;
        int n5 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.al, theme.text()) & 0xFFFFFF;
        int n6 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.am, theme.muted()) & 0xFFFFFF;
        int n7 = ThemedPalette.blend(n2, n3, 0.12f);
        int n8 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ak, Integer.MIN_VALUE | ThemedPalette.blend(n3, n4, 0.35f));
        return new ThemedPalette(new Color(0xF8000000 | n7, true), new Color(n8, true), new Color(0xFF000000 | n5, true), new Color(0xFF000000 | n6, true), new Color(0xFF000000 | ThemedPalette.blend(n7, n5, 0.1f), true), new Color(0xFF000000 | ThemedPalette.blend(n7, n5, 0.22f), true), new Color(0xFF000000 | ThemedPalette.blend(n7, n5, 0.32f), true), new Color(0xFF000000 | n4, true));
    }

    static int blend(int n2, int n3, float f2) {
        int n4 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f2) + (float)(n3 >> 16 & 0xFF) * f2);
        int n5 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f2) + (float)(n3 >> 8 & 0xFF) * f2);
        int n6 = Math.round((float)(n2 & 0xFF) * (1.0f - f2) + (float)(n3 & 0xFF) * f2);
        return n4 << 16 | n5 << 8 | n6;
    }
}

