/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.new_ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class CrosshairGrid {
    public static final int SIZE = 17;

    private CrosshairGrid() {
    }

    public static boolean[] decode(String string) {
        int n2;
        boolean[] blArray = new boolean[289];
        if (string == null || string.isBlank()) {
            return blArray;
        }
        int n3 = string.indexOf(58);
        if (n3 < 0) {
            return blArray;
        }
        try {
            n2 = Integer.parseInt(string.substring(0, n3));
        }
        catch (NumberFormatException numberFormatException) {
            return blArray;
        }
        String string2 = string.substring(n3 + 1);
        if (n2 != 17 || string2.length() != blArray.length) {
            return blArray;
        }
        for (int i2 = 0; i2 < blArray.length; ++i2) {
            blArray[i2] = string2.charAt(i2) == '1';
        }
        return blArray;
    }

    public static String encode(boolean[] blArray) {
        StringBuilder stringBuilder = new StringBuilder(18 + blArray.length);
        stringBuilder.append(17).append(':');
        for (boolean bl : blArray) {
            stringBuilder.append(bl ? (char)'1' : '0');
        }
        return stringBuilder.toString();
    }
}

