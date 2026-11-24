/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.CommandEncoder
 *  com.mojang.blaze3d.systems.GpuDevice
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.GpuTexture
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_276
 *  net.minecraft.class_310
 *  net.minecraft.class_6367
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.gui.overlay.CaptureSafety;
import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.mixin.LightmapTextureManagerAccessor;
import com.ishland.scalablelux.module.hooks.CaptureAware;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.ModuleAccess;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_6367;

@Environment(value=EnvType.CLIENT)
public final class CleanCapture {
    private static volatile boolean capturing;
    private static boolean redirecting;
    private static boolean activeThisFrame;
    private static boolean failed;
    private static long lastCaptureNanos;
    private static class_6367 scene;
    private static class_6367 clean;
    private static boolean cleanValid;
    private static int sceneW;
    private static int sceneH;
    private static Thread renderThread;
    private static final Set<class_1297> FLUX_ENTITIES;
    private static final Set<Object> FLUX_PARTICLES;
    private static final Set<Object> USER_HIDDEN_PARTICLES;
    private static final ThreadLocal<Boolean> HIDE_NEXT;
    private static final ThreadLocal<Boolean> STICKY_HIDE;
    private static final Map<Object, float[]> ORIGINAL_COLORS;
    private static Boolean irisApiPresent;

    private CleanCapture() {
    }

    public static boolean isCapturing() {
        return capturing && Thread.currentThread() == renderThread;
    }

    public static boolean isActive() {
        return activeThisFrame;
    }

    public static boolean wanted() {
        if (failed) {
            return false;
        }
        try {
            return CleanCapture.enabledSetting() && OverlayCompositor.isSsProofReady() && !CleanCapture.irisShadersActive();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static void hideNextParticleForUser(boolean bl) {
        HIDE_NEXT.set(bl);
    }

    public static boolean consumeHideNext() {
        boolean bl = HIDE_NEXT.get();
        HIDE_NEXT.set(false);
        return bl || STICKY_HIDE.get() != false;
    }

    public static void hideParticlesForUser(boolean bl) {
        STICKY_HIDE.set(bl);
    }

    public static void rememberOriginalColor(Object object, float f2, float f3, float f4) {
        if (object != null) {
            ORIGINAL_COLORS.put(object, new float[]{f2, f3, f4});
        }
    }

    public static float[] originalColor(Object object) {
        return object == null || !CleanCapture.isCapturing() ? null : ORIGINAL_COLORS.get(object);
    }

    public static void tagParticle(Object object, boolean bl, boolean bl2) {
        if (object == null) {
            return;
        }
        if (bl) {
            FLUX_PARTICLES.add(object);
        }
        if (bl2) {
            USER_HIDDEN_PARTICLES.add(object);
        }
    }

    public static boolean skipParticle(Object object) {
        if (object == null || !activeThisFrame) {
            return false;
        }
        if (CleanCapture.isCapturing()) {
            return FLUX_PARTICLES.contains(object);
        }
        return USER_HIDDEN_PARTICLES.contains(object);
    }

    public static boolean isRedirecting() {
        return redirecting;
    }

    public static class_276 scene() {
        return scene;
    }

    public static void markFluxEntity(class_1297 class_12972) {
        if (class_12972 != null) {
            FLUX_ENTITIES.add(class_12972);
        }
    }

    public static boolean hideFromCapture(class_1297 class_12972) {
        return class_12972 != null && CleanCapture.isCapturing() && FLUX_ENTITIES.contains(class_12972);
    }

    public static void beginFrame(class_310 class_3102) {
        activeThisFrame = false;
        redirecting = false;
        if (!CleanCapture.tryBegin(class_3102)) {
            cleanValid = false;
            lastCaptureNanos = 0L;
        }
    }

    private static boolean tryBegin(class_310 class_3102) {
        if (failed || class_3102 == null || class_3102.field_1687 == null) {
            return false;
        }
        if (!CleanCapture.enabledSetting() || !OverlayCompositor.isSsProofReady()) {
            return false;
        }
        if (CleanCapture.irisShadersActive()) {
            return false;
        }
        class_276 class_2762 = McCompat.mainRenderTarget(class_3102);
        if (class_2762 == null || class_2762.field_1482 <= 0 || class_2762.field_1481 <= 0) {
            return false;
        }
        if (!CleanCapture.ensureScene(class_2762)) {
            return false;
        }
        renderThread = Thread.currentThread();
        activeThisFrame = true;
        redirecting = true;
        return true;
    }

    public static void userFrameRendered(class_310 class_3102) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void renderPasses(class_310 class_3102, Runnable runnable, Runnable runnable2) {
        boolean bl;
        if (!activeThisFrame) {
            runnable.run();
            return;
        }
        try {
            CleanCapture.markLightmapDirty(class_3102);
            runnable.run();
        }
        catch (Throwable throwable) {
            redirecting = false;
            activeThisFrame = false;
            failed = true;
            throw throwable;
        }
        redirecting = false;
        long l2 = System.nanoTime();
        long l3 = (long)(1.0E9 / Math.max(5.0, CleanCapture.captureFps()));
        boolean bl2 = bl = lastCaptureNanos == 0L || l2 - lastCaptureNanos >= l3;
        if (!bl) {
            return;
        }
        lastCaptureNanos = l2;
        List<CaptureAware> list = ModuleAccess.getEnabledHooks(CaptureAware.class);
        for (CaptureAware captureAware : list) {
            try {
                captureAware.beginCapture();
            }
            catch (Throwable throwable) {}
        }
        capturing = true;
        try {
            CleanCapture.clear(McCompat.mainRenderTarget(class_3102));
            CleanCapture.markLightmapDirty(class_3102);
            if (runnable2 != null) {
                runnable2.run();
            }
            runnable.run();
        }
        catch (Throwable throwable) {
            failed = true;
            cleanValid = false;
            try {
                CleanCapture.clear(McCompat.mainRenderTarget(class_3102));
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
        finally {
            capturing = false;
            for (CaptureAware captureAware : list) {
                try {
                    captureAware.endCapture();
                }
                catch (Throwable throwable) {}
            }
            CleanCapture.markLightmapDirty(class_3102);
        }
    }

    public static void endFrame() {
        redirecting = false;
    }

    private static boolean enabledSetting() {
        return HitboxConfigScreen752809.k() && HitboxConfigScreen752809.au != null && (HitboxConfigScreen752809.au.isEnabled() || CaptureSafety.anyVanillaPixelVisualEnabled());
    }

    private static double captureFps() {
        return HitboxConfigScreen752809.aw == null ? 30.0 : HitboxConfigScreen752809.aw.B();
    }

    private static class_6367 newTarget(String string, class_276 class_2762) {
        return new class_6367(string, class_2762.field_1482, class_2762.field_1481, true);
    }

    private static boolean ensureScene(class_276 class_2762) {
        int n2 = class_2762.field_1482;
        int n3 = class_2762.field_1481;
        try {
            if (scene == null) {
                scene = CleanCapture.newTarget(CleanCapture.qp("\u616e\ub73a\u6f90\u9ef3\u6fc4\u220b\u0606\ub006\u7ff0\u098c\u1c89\uc00e\u471d\uaba2\u10bd", 308273551, 0x90495EDD ^ CryptoVault.mask()), class_2762);
                sceneW = n2;
                sceneH = n3;
                cleanValid = false;
            } else if (n2 != sceneW || n3 != sceneH) {
                scene.method_1234(n2, n3);
                sceneW = n2;
                sceneH = n3;
                cleanValid = false;
            }
            return true;
        }
        catch (Throwable throwable) {
            failed = true;
            return false;
        }
    }

    private static void copyTarget(class_276 class_2762, class_276 class_2763) {
        GpuDevice gpuDevice = RenderSystem.tryGetDevice();
        if (gpuDevice == null || class_2762 == null || class_2763 == null) {
            return;
        }
        GpuTexture gpuTexture = class_2762.method_30277();
        GpuTexture gpuTexture2 = class_2763.method_30277();
        if (gpuTexture == null || gpuTexture2 == null || class_2762.field_1482 != class_2763.field_1482 || class_2762.field_1481 != class_2763.field_1481) {
            return;
        }
        gpuDevice.createCommandEncoder().copyTextureToTexture(gpuTexture, gpuTexture2, 0, 0, 0, 0, 0, class_2762.field_1482, class_2762.field_1481);
    }

    private static void clear(class_276 class_2762) {
        GpuDevice gpuDevice = RenderSystem.tryGetDevice();
        if (gpuDevice == null || class_2762 == null || class_2762.method_30277() == null) {
            return;
        }
        CommandEncoder commandEncoder = gpuDevice.createCommandEncoder();
        if (class_2762.method_30278() != null) {
            commandEncoder.clearColorAndDepthTextures(class_2762.method_30277(), 0, class_2762.method_30278(), 1.0);
        } else {
            commandEncoder.clearColorTexture(class_2762.method_30277(), 0);
        }
    }

    private static void markLightmapDirty(class_310 class_3102) {
        try {
            if (class_3102.field_1773 != null) {
                ((LightmapTextureManagerAccessor)class_3102.field_1773.method_22974()).sl$setDirty(true);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static boolean irisShadersActive() {
        if (Boolean.FALSE.equals(irisApiPresent)) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName(CleanCapture.qp("\u4ae8\uc845\ua861\u9fd9\u6100\u7d1a\u20c3\uc988\u8919\u0141\uc3b2\uf5a2\u3705\u0a12\ufa0c\u1ec1\u44f7\u0e73\u1104\u775e\ua2fa\u7585\u0e31\ucc76\u76f8\uf045\ub421\u72ac\u97c5\u2dc4\u0d40\u5b6f\uf2ee\udf7d\ubb5e\u6738\u5d1f\uf323\udde4", 490836991, 0x90495EDD ^ CryptoVault.mask()));
            Object object = clazz.getMethod(CleanCapture.qp("\ub8e2\u4f6d\uddda\uae28\u15de\u656f\u4011\u3868\u975a\u0d20\u1cab\u0a8b\u282f\u9c03\ub7ab", 1006066822, 0x90495EDD ^ CryptoVault.mask()), new Class[0]).invoke(null, new Object[0]);
            irisApiPresent = true;
            return (Boolean)clazz.getMethod(CleanCapture.qp("\uf04f\u2e71\u261d\uf482\u6e26\uf261\ud108\u87bc\ub86c\u3781\uaf7b\udf2b\ucb25\u7be8\ub860\ud58b\u1b89\u9cb4\u0a0a\u922f\uf0e0", 353800322, 0x90495EDD ^ CryptoVault.mask()), new Class[0]).invoke(object, new Object[0]);
        }
        catch (Throwable throwable) {
            irisApiPresent = false;
            return false;
        }
    }

    public static Map<String, Object> debugState() {
        return Map.of(CleanCapture.qp("\ub1a0\u6fe6\u31f1\u8798\u644f\uf36c\u5522\u36f5", 980032294, 0x90495EDD ^ CryptoVault.mask()), activeThisFrame, CleanCapture.qp("\u02f9\u8c69\ub07c\ub66b\u6274\u4832\ub8d7", -1417356338, 0x90495EDD ^ CryptoVault.mask()), failed);
    }

    static {
        FLUX_ENTITIES = Collections.newSetFromMap(Collections.synchronizedMap(new WeakHashMap()));
        FLUX_PARTICLES = Collections.newSetFromMap(Collections.synchronizedMap(new WeakHashMap()));
        USER_HIDDEN_PARTICLES = Collections.newSetFromMap(Collections.synchronizedMap(new WeakHashMap()));
        HIDE_NEXT = ThreadLocal.withInitial(() -> false);
        STICKY_HIDE = ThreadLocal.withInitial(() -> false);
        ORIGINAL_COLORS = Collections.synchronizedMap(new WeakHashMap());
    }

    private static /* synthetic */ String qp(String string, int n2, int n3) {
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

