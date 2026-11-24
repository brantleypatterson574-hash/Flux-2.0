/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  org.joml.Matrix3x2fStack
 */
package com.ishland.scalablelux.gui.mobile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_327;
import net.minecraft.class_332;
import org.joml.Matrix3x2fStack;

@Environment(value=EnvType.CLIENT)
final class MobileUi {
    private MobileUi() {
    }

    static void roundRect(class_332 class_3322, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n4 <= 0 || n5 <= 0) {
            return;
        }
        int n8 = Math.max(0, Math.min(n6, Math.min(n4, n5) / 2));
        if (n8 == 0) {
            class_3322.method_25294(n2, n3, n2 + n4, n3 + n5, n7);
            return;
        }
        for (int i2 = 0; i2 < n8; ++i2) {
            double d2 = (double)(n8 - i2) - 0.5;
            int n9 = n8 - (int)Math.round(Math.sqrt(Math.max(0.0, (double)n8 * (double)n8 - d2 * d2)));
            class_3322.method_25294(n2 + n9, n3 + i2, n2 + n4 - n9, n3 + i2 + 1, n7);
            class_3322.method_25294(n2 + n9, n3 + n5 - 1 - i2, n2 + n4 - n9, n3 + n5 - i2, n7);
        }
        class_3322.method_25294(n2, n3 + n8, n2 + n4, n3 + n5 - n8, n7);
    }

    static void text(class_332 class_3322, class_327 class_3272, String string, float f2, float f3, float f4, int n2) {
        if (string == null || string.isEmpty()) {
            return;
        }
        Matrix3x2fStack matrix3x2fStack = class_3322.method_51448();
        matrix3x2fStack.pushMatrix();
        matrix3x2fStack.translate(f2, f3);
        matrix3x2fStack.scale(f4, f4);
        class_3322.method_51433(class_3272, string, 0, 0, n2, false);
        matrix3x2fStack.popMatrix();
    }

    static float width(class_327 class_3272, String string, float f2) {
        return string == null ? 0.0f : (float)class_3272.method_1727(string) * f2;
    }
}

