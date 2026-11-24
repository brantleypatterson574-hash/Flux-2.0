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
public final class CatalogDrag {
    private CatalogDrag() {
    }

    public static Drop drop(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n2, int n3) {
        n2 = Math.max(1, n2);
        if (n3 <= 0) {
            return new Drop(0, 0, 0);
        }
        float f10 = Math.max(1.0f, f7 + f9);
        float f11 = Math.max(1.0f, f6 + f8);
        int n4 = (n3 + n2 - 1) / n2;
        if (n2 == 1) {
            int n5 = Math.round((f3 - f5 + f9 * 0.5f) / f10);
            n5 = Math.max(0, Math.min(n3, n5));
            return new Drop(n5, n5, 0);
        }
        int n6 = (int)Math.floor((f3 - f5 + f9 * 0.5f) / f10);
        n6 = Math.max(0, Math.min(n4 - 1, n6));
        int n7 = Math.min(n2, n3 - n6 * n2);
        int n8 = Math.round((f2 - f4 + f8 * 0.5f) / f11);
        n8 = Math.max(0, Math.min(n7, n8));
        return new Drop(n6 * n2 + n8, n6, n8);
    }

    public static int clampToGroup(int n2, boolean bl, int n3, int n4) {
        int n5 = bl ? 0 : n3;
        int n6 = bl ? n3 : n4;
        return Math.max(n5, Math.min(n6, n2));
    }

    @Environment(value=EnvType.CLIENT)
    public record Drop(int index, int row, int column) {
    }
}

