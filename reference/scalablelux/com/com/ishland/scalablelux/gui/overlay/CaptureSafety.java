/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.gui.overlay.CleanCapture;
import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.manager.ModuleManager;
import com.ishland.scalablelux.module.hooks.CameraClipProvider;
import com.ishland.scalablelux.module.hooks.CrosshairProvider;
import com.ishland.scalablelux.module.hooks.EntityChamsProvider;
import com.ishland.scalablelux.module.hooks.GlowDecisionProvider;
import com.ishland.scalablelux.module.hooks.HitColorProvider;
import com.ishland.scalablelux.module.hooks.ItemGlowProvider;
import com.ishland.scalablelux.module.hooks.PlayerModelVisualProvider;
import com.ishland.scalablelux.module.hooks.ShaderEspProvider;
import com.ishland.scalablelux.module.hooks.ShieldTintProvider;
import com.ishland.scalablelux.module.hooks.TargetEffectOverlayProvider;
import com.ishland.scalablelux.module.hooks.TotemPopVisualProvider;
import com.ishland.scalablelux.module.hooks.ViewTransformProvider;
import com.ishland.scalablelux.module.hooks.WorldSceneVisualProvider;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.ModuleAccess;
import java.util.Set;
import me.sootysplash.box.render.DrawStyleRegistry11;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class CaptureSafety {
    private static final Set<Class<?>> VANILLA_PIXEL_HOOKS = Set.of(DrawStyleRegistry11.class, CameraClipProvider.class, CrosshairProvider.class, EntityChamsProvider.class, GlowDecisionProvider.class, HitColorProvider.class, ItemGlowProvider.class, PlayerModelVisualProvider.class, ShaderEspProvider.class, ShieldTintProvider.class, TargetEffectOverlayProvider.class, TotemPopVisualProvider.class, ViewTransformProvider.class, WorldSceneVisualProvider.class);
    private static final String[] VANILLA_PIXEL_MODULES = new String[]{CaptureSafety.ur("\u8bed\u8851\udc6c\u412d\u9a5d\uf70e\u2965\u7f5f\u0d2a\u836d\u3125", 733835915, 0x7E177BA4 ^ CryptoVault.mask()), CaptureSafety.ur("\u5bd0\uf1f6\u7855\u81cd\u333d\u5f34", 1842601059, 0x7E177BA4 ^ CryptoVault.mask()), CaptureSafety.ur("\u7d56\u3693\ua1be\ua237\ubbb0\uf620\u548a\u13e6\ufbd7\u5d90", -1286581830, 0x7E177BA4 ^ CryptoVault.mask()), CaptureSafety.ur("\u87ea\uca47\u74f7\u0b4e\u87de\ue41a\u320b\u6ec9\u21af\u9574", 1330828522, 0x7E177BA4 ^ CryptoVault.mask()), CaptureSafety.ur("\u262b\ubdb0\ud587\uea0f\ubfd8\u797c\udd07\u5472\uaf4d\u5cbe\u7651\u3c2c", -1786884958, 0x7E177BA4 ^ CryptoVault.mask()), CaptureSafety.ur("\u1ca2\ufde6\uc7e2\u1c81\u04c1\ua1b4\u2590\u18f4\ud681\u8836\u8d8a\ub957\u10a6", 1581552068, 0x7E177BA4 ^ CryptoVault.mask())};
    private static long lastScanMs;
    private static boolean lastScan;

    private CaptureSafety() {
    }

    public static boolean ssProofOn() {
        try {
            return OverlayCompositor.isSsProofOn();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static boolean vanillaPixelVisualsHidden() {
        try {
            return OverlayCompositor.isSsProofOn() && !CleanCapture.isActive();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static boolean anyVanillaPixelVisualEnabled() {
        boolean bl;
        block5: {
            long l2 = System.currentTimeMillis();
            if (l2 - lastScanMs < 250L || CleanCapture.isCapturing()) {
                return lastScan;
            }
            lastScanMs = l2;
            bl = false;
            try {
                ModuleManager moduleManager;
                SlApp slApp = SlApp.getInstance();
                ModuleManager moduleManager2 = moduleManager = slApp != null ? slApp.getModuleManager() : null;
                if (moduleManager == null) break block5;
                for (Class<?> clazz : VANILLA_PIXEL_HOOKS) {
                    if (moduleManager.getEnabledHooks(clazz).isEmpty()) continue;
                    bl = true;
                    break;
                }
                if (bl) break block5;
                for (String string : VANILLA_PIXEL_MODULES) {
                    if (!ModuleAccess.isEnabled(string)) continue;
                    bl = true;
                    break;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        lastScan = bl;
        return bl;
    }

    public static boolean hidesHook(Class<?> clazz) {
        return clazz != null && VANILLA_PIXEL_HOOKS.contains(clazz) && CaptureSafety.vanillaPixelVisualsHidden();
    }

    private static /* synthetic */ String ur(String string, int n2, int n3) {
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

