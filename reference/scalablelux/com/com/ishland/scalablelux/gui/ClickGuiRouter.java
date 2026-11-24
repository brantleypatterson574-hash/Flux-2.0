/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  net.minecraft.class_342
 *  net.minecraft.class_3928
 *  net.minecraft.class_408
 *  net.minecraft.class_412
 *  net.minecraft.class_424
 *  net.minecraft.class_429
 *  net.minecraft.class_433
 *  net.minecraft.class_435
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_4667
 *  net.minecraft.class_500
 *  net.minecraft.class_6599
 */
package com.ishland.scalablelux.gui;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.gui.gpuui.DropdownGpuClickGuiScreen;
import com.ishland.scalablelux.gui.gpuui.GpuClickGuiScreen;
import com.ishland.scalablelux.gui.mobile.PayloadLoadingScreen;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_342;
import net.minecraft.class_3928;
import net.minecraft.class_408;
import net.minecraft.class_412;
import net.minecraft.class_424;
import net.minecraft.class_429;
import net.minecraft.class_433;
import net.minecraft.class_435;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4667;
import net.minecraft.class_500;
import net.minecraft.class_6599;

@Environment(value=EnvType.CLIENT)
public final class ClickGuiRouter {
    private ClickGuiRouter() {
    }

    public static boolean menuOwnsKey(class_437 class_4372, int n2) {
        if (class_4372 instanceof GpuClickGuiScreen) {
            GpuClickGuiScreen gpuClickGuiScreen = (GpuClickGuiScreen)class_4372;
            return gpuClickGuiScreen.isCapturingKeybind() || gpuClickGuiScreen.capturedKeyRecently(n2, 500L) || gpuClickGuiScreen.sawKeyRecently(n2, 300L);
        }
        return false;
    }

    public static class_437 create() {
        return ClickGuiRouter.create(null);
    }

    public static class_437 create(class_437 class_4372) {
        class_310 class_3102;
        if (class_4372 == null && HitboxConfigScreen752809.av != null && HitboxConfigScreen752809.av.isEnabled() && (class_3102 = class_310.method_1551()) != null && class_3102.field_1687 != null) {
            class_4372 = new class_433(true);
        }
        if (!SlApp.isMenuReady()) {
            return new PayloadLoadingScreen(class_4372);
        }
        boolean bl = HitboxConfigScreen752809.U != null && HitboxConfigScreen752809.U.is(ClickGuiRouter.xj("\ucaf4\u5ba3\u462b\u843c\ua34d\u5851\u9e72\u4424\u1239", 1320347486, 0xAE0D5864 ^ CryptoVault.mask()));
        GpuClickGuiScreen gpuClickGuiScreen = bl ? new DropdownGpuClickGuiScreen() : new GpuClickGuiScreen();
        return gpuClickGuiScreen.withReturnScreen(class_4372);
    }

    public static boolean canOpenOver(class_437 class_4372) {
        if (class_4372 == null || ClickGuiRouter.isMenu(class_4372)) {
            return false;
        }
        if (class_4372 instanceof class_408) {
            return false;
        }
        if (class_4372 instanceof class_6599) {
            return false;
        }
        if (class_4372.method_25399() instanceof class_342) {
            return false;
        }
        if (ClickGuiRouter.keepsTicking(class_4372)) {
            return SlApp.isMenuReady();
        }
        return class_4372 instanceof class_442 || class_4372 instanceof class_500 || class_4372 instanceof class_429 || class_4372 instanceof class_4667;
    }

    public static boolean keepsTicking(class_437 class_4372) {
        return class_4372 instanceof class_412 || class_4372 instanceof class_3928 || class_4372 instanceof class_435 || class_4372 instanceof class_424;
    }

    public static void tickCoveredScreen(class_310 class_3102) {
        class_437 class_4372;
        if (class_3102 == null || !((class_4372 = McCompat.screen(class_3102)) instanceof GpuClickGuiScreen)) {
            return;
        }
        GpuClickGuiScreen gpuClickGuiScreen = (GpuClickGuiScreen)class_4372;
        class_4372 = gpuClickGuiScreen.returnScreen();
        if (class_4372 == null || !ClickGuiRouter.keepsTicking(class_4372)) {
            return;
        }
        try {
            class_4372.method_25393();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void toast(String string, String string2) {
    }

    public static boolean isMenu(class_437 class_4372) {
        return class_4372 instanceof GpuClickGuiScreen;
    }

    private static /* synthetic */ String xj(String string, int n2, int n3) {
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

