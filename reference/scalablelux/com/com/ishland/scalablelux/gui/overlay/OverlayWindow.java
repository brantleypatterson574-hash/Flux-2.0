/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_310
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWNativeCocoa
 *  org.lwjgl.glfw.GLFWNativeWin32
 *  org.lwjgl.opengl.GL
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GL32
 *  org.lwjgl.opengl.GLCapabilities
 *  org.lwjgl.system.APIUtil
 *  org.lwjgl.system.JNI
 *  org.lwjgl.system.MemoryStack
 *  org.lwjgl.system.MemoryUtil
 *  org.lwjgl.system.SharedLibrary
 *  org.lwjgl.system.macosx.ObjCRuntime
 *  org.lwjgl.system.windows.User32
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.gui.overlay.OverlayCursor;
import com.ishland.scalablelux.gui.overlay.SsProofPolicy;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.PlatformPreset;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeCocoa;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.macosx.ObjCRuntime;
import org.lwjgl.system.windows.User32;

@Environment(value=EnvType.CLIENT)
public final class OverlayWindow {
    private static long handle;
    private static GLCapabilities overlayCaps;
    private static GLCapabilities mcCaps;
    private static boolean initFailed;
    private static long initFailedAtMs;
    private static int initAttempts;
    private static final int MAX_INIT_ATTEMPTS = 4;
    private static long lastAffinityCheckMs;
    private static int affinityRetries;
    private static final int MAX_AFFINITY_RETRIES = 12;
    private static boolean visible;
    private static final String OS_NAME;
    public static final boolean IS_WINDOWS;
    public static final boolean IS_MAC;
    public static final boolean IRIS_PRESENT;
    private static boolean blitReady;
    private static boolean blitFailed;
    private static int blitProgram;
    private static int blitVao;
    private static int blitVbo;
    private static int blitTexUniform;
    private static int blitFlipUniform;
    private static final long WS_EX_NOACTIVATE = 0x8000000L;
    private static final long WS_EX_TOOLWINDOW = 128L;
    private static final long WS_EX_TRANSPARENT = 32L;
    private static final int SWP_NOSIZE = 1;
    private static final int SWP_NOMOVE = 2;
    private static final int SWP_NOZORDER = 4;
    private static final int SWP_NOACTIVATE = 16;
    private static final int SWP_FRAMECHANGED = 32;
    private static final long HWND_TOPMOST = -1L;
    private static final int WDA_EXCLUDEFROMCAPTURE = 17;
    private static SharedLibrary cachedUser32;
    private static long cachedSetAffinityFn;
    private static long cachedGetAffinityFn;
    private static boolean affinityLookupDone;
    private static int affinityState;
    private static final long NS_WINDOW_SHARING_NONE = 0L;
    private static final long NS_FLOATING_WINDOW_LEVEL = 3L;
    private static final long NS_COLLECTION_CAN_JOIN_ALL_SPACES = 1L;
    private static final long NS_COLLECTION_STATIONARY = 16L;
    private static final long NS_COLLECTION_FULLSCREEN_AUXILIARY = 256L;
    private static final long NS_COLLECTION_OVERLAY = 273L;
    private static long boundsHandle;
    private static int boundsX;
    private static int boundsY;
    private static int boundsW;
    private static int boundsH;
    private static boolean presentOpaque;
    private static final long BLACK_OVERLAY_LIMIT_MS = 6000L;
    private static final int BLACK_SAMPLE_EVERY = 15;
    private static final int BLACK_SAMPLES = 25;
    private static int blackSampleTick;
    private static long blackSinceMs;
    private static int blackPbo;
    private static long blackFence;

    private OverlayWindow() {
    }

    private static long mcHandle() {
        class_310 class_3102 = class_310.method_1551();
        return class_3102 != null && class_3102.method_22683() != null ? class_3102.method_22683().method_4490() : 0L;
    }

    public static boolean isShown() {
        return handle != 0L && visible;
    }

    public static boolean isAvailable() {
        return handle != 0L && !initFailed;
    }

    public static int affinityState() {
        return affinityState;
    }

    public static boolean creationGaveUp() {
        return initFailed && initAttempts >= 4;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void ensureCreated() {
        if (handle != 0L) {
            OverlayWindow.recheckAffinity();
            return;
        }
        if (!PlatformPreset.overlayWindow()) {
            initFailed = true;
            initAttempts = 4;
            return;
        }
        if (initFailed) {
            if (initAttempts >= 4 || System.currentTimeMillis() - initFailedAtMs < 3000L) {
                return;
            }
            initFailed = false;
        }
        long l2 = GLFW.glfwGetCurrentContext();
        try {
            long l3 = OverlayWindow.mcHandle();
            if (l3 == 0L) {
                return;
            }
            mcCaps = GL.getCapabilities();
            int[] nArray = new int[1];
            int[] nArray2 = new int[1];
            GLFW.glfwGetWindowSize((long)l3, (int[])nArray, (int[])nArray2);
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint((int)131076, (int)0);
            GLFW.glfwWindowHint((int)131077, (int)0);
            GLFW.glfwWindowHint((int)131079, (int)1);
            GLFW.glfwWindowHint((int)131082, (int)1);
            GLFW.glfwWindowHint((int)131084, (int)0);
            GLFW.glfwWindowHint((int)131075, (int)0);
            GLFW.glfwWindowHint((int)131073, (int)0);
            try {
                GLFW.glfwWindowHint((int)131085, (int)1);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            GLFW.glfwWindowHint((int)139265, (int)196609);
            GLFW.glfwWindowHint((int)139266, (int)3);
            GLFW.glfwWindowHint((int)139267, (int)2);
            GLFW.glfwWindowHint((int)139272, (int)204801);
            GLFW.glfwWindowHint((int)139270, (int)1);
            handle = GLFW.glfwCreateWindow((int)Math.max(1, nArray[0]), (int)Math.max(1, nArray2[0]), (CharSequence)SsProofPolicy.randomWindowTitle(new Random()), (long)0L, (long)l3);
            if (handle == 0L) {
                OverlayWindow.markInitFailed();
                return;
            }
            GLFW.glfwMakeContextCurrent((long)handle);
            overlayCaps = GL.createCapabilities();
            GLFW.glfwSwapInterval((int)0);
            OverlayWindow.syncBounds();
            OverlayWindow.applyMousePassthrough();
            OverlayWindow.applyCaptureExclusion();
            OverlayWindow.applyMacOSSharingExclusion();
        }
        catch (Throwable throwable) {
            OverlayWindow.markInitFailed();
        }
        finally {
            OverlayWindow.restore(l2);
        }
    }

    private static void markInitFailed() {
        initFailed = true;
        initFailedAtMs = System.currentTimeMillis();
        ++initAttempts;
        if (handle != 0L) {
            try {
                GLFW.glfwDestroyWindow((long)handle);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            handle = 0L;
        }
        blackPbo = 0;
        blackFence = 0L;
    }

    private static void recheckAffinity() {
        if (!IS_WINDOWS && !IS_MAC || affinityState == 1) {
            return;
        }
        long l2 = System.currentTimeMillis();
        if (l2 - lastAffinityCheckMs < 1000L) {
            return;
        }
        lastAffinityCheckMs = l2;
        if (IS_WINDOWS) {
            OverlayWindow.applyCaptureExclusion();
        } else {
            OverlayWindow.applyMacOSSharingExclusion();
        }
        if (affinityState != 1 && ++affinityRetries >= 12 && affinityState == 0) {
            affinityState = 2;
        }
    }

    public static void show() {
        if (handle == 0L) {
            OverlayWindow.ensureCreated();
        }
        if (handle == 0L) {
            return;
        }
        OverlayWindow.syncBounds();
        if (!visible) {
            GLFW.glfwShowWindow((long)handle);
            OverlayWindow.applyMousePassthrough();
            OverlayWindow.applyCaptureExclusion();
            OverlayWindow.applyMacOSSharingExclusion();
            if (IS_WINDOWS) {
                try {
                    long l2 = GLFWNativeWin32.glfwGetWin32Window((long)handle);
                    if (l2 != 0L) {
                        User32.SetWindowPos((long)l2, (long)-1L, (int)0, (int)0, (int)0, (int)0, (int)19);
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            visible = true;
        }
    }

    private static boolean gameWindowActive() {
        try {
            long l2 = OverlayWindow.mcHandle();
            if (l2 == 0L) {
                return false;
            }
            return GLFW.glfwGetWindowAttrib((long)l2, (int)131073) == 1 && GLFW.glfwGetWindowAttrib((long)l2, (int)131074) != 1;
        }
        catch (Throwable throwable) {
            return true;
        }
    }

    public static void hide() {
        if (handle != 0L && visible) {
            GLFW.glfwHideWindow((long)handle);
        }
        visible = false;
    }

    public static void syncBounds() {
        if (handle == 0L) {
            return;
        }
        try {
            long l2 = OverlayWindow.mcHandle();
            if (l2 == 0L) {
                return;
            }
            int[] nArray = new int[1];
            int[] nArray2 = new int[1];
            int[] nArray3 = new int[1];
            int[] nArray4 = new int[1];
            GLFW.glfwGetWindowPos((long)l2, (int[])nArray, (int[])nArray2);
            GLFW.glfwGetWindowSize((long)l2, (int[])nArray3, (int[])nArray4);
            int n2 = Math.max(1, nArray3[0]);
            int n3 = Math.max(1, nArray4[0]);
            if (handle == boundsHandle && nArray[0] == boundsX && nArray2[0] == boundsY && n2 == boundsW && n3 == boundsH) {
                return;
            }
            if (nArray[0] != boundsX || nArray2[0] != boundsY || handle != boundsHandle) {
                GLFW.glfwSetWindowPos((long)handle, (int)nArray[0], (int)nArray2[0]);
            }
            if (n2 != boundsW || n3 != boundsH || handle != boundsHandle) {
                GLFW.glfwSetWindowSize((long)handle, (int)n2, (int)n3);
            }
            boundsHandle = handle;
            boundsX = nArray[0];
            boundsY = nArray2[0];
            boundsW = n2;
            boundsH = n3;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void applyMousePassthrough() {
        if (handle == 0L) {
            return;
        }
        try {
            GLFW.glfwSetWindowAttrib((long)handle, (int)131085, (int)1);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (IS_WINDOWS) {
            try {
                long l2;
                long l3;
                long l4 = GLFWNativeWin32.glfwGetWin32Window((long)handle);
                if (l4 != 0L && (l3 = (l2 = User32.GetWindowLongPtr((long)l4, (int)-20)) & 0xFFFFFFFFFFFFFFDFL | 0x20L | 0x8000000L | 0x80L) != l2) {
                    User32.SetWindowLongPtr((long)l4, (int)-20, (long)l3);
                    User32.SetWindowPos((long)l4, (long)0L, (int)0, (int)0, (int)0, (int)0, (int)55);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    private static void applyCaptureExclusion() {
        if (!IS_WINDOWS || handle == 0L) {
            return;
        }
        try {
            long l2 = GLFWNativeWin32.glfwGetWin32Window((long)handle);
            if (l2 == 0L) {
                return;
            }
            if (!affinityLookupDone) {
                affinityLookupDone = true;
                cachedUser32 = APIUtil.apiCreateLibrary((String)OverlayWindow.sp("\ucd5c\u53f4\u0083\u650e\u6c19\uabad\u227a\ud462", -910446008, 0xD91BBC65 ^ CryptoVault.mask()));
                cachedSetAffinityFn = APIUtil.apiGetFunctionAddressOptional((SharedLibrary)cachedUser32, (String)OverlayWindow.sp("\u7545\ub6c1\u66f7\ufb97\u0332\ub605\uc6e5\uc3fa\u67cc\ue8cb\u99eb\u6488\uc2db\u705e\u7039\u51a1\udf2c\u5063\u4b01\ue1a3\ue1d7\ub72d\u1621\udbcc\uf56b", -1588143415, 0xD91BBC65 ^ CryptoVault.mask()));
                cachedGetAffinityFn = APIUtil.apiGetFunctionAddressOptional((SharedLibrary)cachedUser32, (String)OverlayWindow.sp("\u9376\u16d7\u8dba\uef03\u9a70\ue3b8\u3d23\u869c\ue963\u2b2c\u41de\ue5c3\u9dc9\u9220\ua177\uc744\u96e1\udfe4\u37c7\ubd7d\u65a9\udcd5\ud7e9\ue57f\uc43c\u98a2\u9931", -321386689, 0xD91BBC65 ^ CryptoVault.mask()));
            }
            if (cachedSetAffinityFn != 0L) {
                int n2 = JNI.invokePI((long)l2, (int)17, (long)cachedSetAffinityFn);
            }
            affinityState = OverlayWindow.verifyCaptureExclusion(l2);
        }
        catch (Throwable throwable) {
            affinityState = 2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int verifyCaptureExclusion(long l2) {
        if (cachedGetAffinityFn == 0L) {
            return 0;
        }
        try (MemoryStack memoryStack = MemoryStack.stackPush();){
            IntBuffer intBuffer = memoryStack.mallocInt(1);
            intBuffer.put(0, 0);
            int n5 = JNI.invokePPI((long)l2, (long)MemoryUtil.memAddress((IntBuffer)intBuffer), (long)cachedGetAffinityFn);
            int n3 = intBuffer.get(0);
            if (n5 == 0) {
                int n4 = 0;
                return n4;
            }
            int n2 = n3 == 17 ? 1 : 2;
            return n2;
        }
        catch (Throwable throwable3) {
            return 0;
        }
    }

    private static void applyMacOSSharingExclusion() {
        if (!IS_MAC || handle == 0L) {
            return;
        }
        try {
            long l2 = GLFWNativeCocoa.glfwGetCocoaWindow((long)handle);
            if (l2 == 0L) {
                affinityState = 2;
                return;
            }
            long l3 = ObjCRuntime.getLibrary().getFunctionAddress((CharSequence)OverlayWindow.sp("\uedf7\u86c2\u1fb8\u5aaa\u8c6e\uafdf\u3bda\u0d31\ub7a3\u8cb6\uf306\u4f48\ub4a6\u1b67", 1236136201, 0xD91BBC65 ^ CryptoVault.mask()));
            if (l3 == 0L) {
                affinityState = 2;
                return;
            }
            OverlayWindow.objcSend1(l2, l3, OverlayWindow.sp("\u7cd0\ua2e1\u142e\u1745\u843e\uf663\u77c5\u4b9b\ub634\ub033\u2cc2\u57e8\ubc74\u3da8\u30b9\ue048", 832210532, 0xD91BBC65 ^ CryptoVault.mask()), 0L);
            OverlayWindow.objcSend1(l2, l3, OverlayWindow.sp("\u519f\ubd40\uab33\uea33\u4db9\u6100\uff11\uf52e\u5fda\ua498\ue8d0\u219d\ue309\u5705\u2074\u3351\uf6b3\ub8ca\u6525\u2900\u9a8b\ubc70\udee9\u3e36\u0c2c", 81173345, 0xD91BBC65 ^ CryptoVault.mask()), 273L);
            OverlayWindow.objcSend1(l2, l3, OverlayWindow.sp("\ua4e9\u3c3c\ud2d7\u6ed6\u98dd\ub085\u6b6f\u18c6\u2985\ude75\ub75a\u1656\uaa5e", -790139046, 0xD91BBC65 ^ CryptoVault.mask()), 3L);
            affinityState = OverlayWindow.verifyMacOSSharingExclusion(l2, l3);
        }
        catch (Throwable throwable) {
            affinityState = 2;
        }
    }

    private static void objcSend1(long l2, long l3, String string, long l4) {
        long l5 = ObjCRuntime.sel_getUid((CharSequence)string);
        if (l5 != 0L) {
            JNI.invokePPPV((long)l2, (long)l5, (long)l4, (long)l3);
        }
    }

    private static int verifyMacOSSharingExclusion(long l2, long l3) {
        try {
            long l4 = ObjCRuntime.sel_getUid((CharSequence)OverlayWindow.sp("\u0f32\u255e\ud060\u375d\u7f3b\ua3ba\ub299\u18ce\u87c3\u812d\ud720\u1cde\u6318", -234947223, 0xD91BBC65 ^ CryptoVault.mask()));
            if (l4 == 0L) {
                return 0;
            }
            long l5 = JNI.invokePPP((long)l2, (long)l4, (long)l3);
            return l5 == 0L ? 1 : 2;
        }
        catch (Throwable throwable) {
            return 0;
        }
    }

    public static void presentOpaque(int n2, int n3, int n4) {
        presentOpaque = true;
        try {
            OverlayWindow.presentMirror(n2, n3, n4, false);
        }
        finally {
            presentOpaque = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void presentMirror(int n2, int n3, int n4, boolean bl) {
        if (n2 <= 0) {
            return;
        }
        if (!OverlayWindow.gameWindowActive()) {
            OverlayWindow.hide();
            return;
        }
        if (!visible) {
            OverlayWindow.show();
        }
        if (handle == 0L || !visible) {
            return;
        }
        long l2 = GLFW.glfwGetCurrentContext();
        try {
            long l3 = GL32.glFenceSync((int)37143, (int)0);
            GL11.glFlush();
            GLFW.glfwMakeContextCurrent((long)handle);
            GL.setCapabilities((GLCapabilities)overlayCaps);
            if (l3 != 0L) {
                GL32.glWaitSync((long)l3, (int)0, (long)-1L);
                GL32.glDeleteSync((long)l3);
            }
            OverlayWindow.syncBounds();
            int[] nArray = new int[1];
            int[] nArray2 = new int[1];
            GLFW.glfwGetFramebufferSize((long)handle, (int[])nArray, (int[])nArray2);
            GL11.glViewport((int)0, (int)0, (int)nArray[0], (int)nArray2[0]);
            GL11.glDisable((int)3089);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)2884);
            GL11.glDisable((int)3042);
            GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            GL11.glClear((int)16384);
            if (!OverlayWindow.ensureBlit()) {
                GLFW.glfwSwapBuffers((long)handle);
                return;
            }
            GL20.glUseProgram((int)blitProgram);
            GL13.glActiveTexture((int)33984);
            GL11.glBindTexture((int)3553, (int)n2);
            if (blitTexUniform >= 0) {
                GL20.glUniform1i((int)blitTexUniform, (int)0);
            }
            if (blitFlipUniform >= 0) {
                GL20.glUniform1f((int)blitFlipUniform, (float)(bl ? 1.0f : 0.0f));
            }
            GL30.glBindVertexArray((int)blitVao);
            GL11.glDrawArrays((int)5, (int)0, (int)4);
            GL30.glBindVertexArray((int)0);
            GL11.glBindTexture((int)3553, (int)0);
            GL20.glUseProgram((int)0);
            try {
                OverlayCursor.draw(OverlayWindow.mcHandle(), nArray[0], nArray2[0]);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (presentOpaque) {
                GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)true);
                GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glClear((int)16384);
                GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                OverlayWindow.watchForBlackOverlay(nArray[0], nArray2[0]);
            }
            GLFW.glfwSwapBuffers((long)handle);
        }
        catch (Throwable throwable) {
        }
        finally {
            OverlayWindow.restore(l2);
        }
    }

    private static void watchForBlackOverlay(int n2, int n3) {
        if (++blackSampleTick % 15 != 0) {
            return;
        }
        try {
            boolean bl;
            class_310 class_3102 = class_310.method_1551();
            boolean bl2 = bl = class_3102 != null && class_3102.field_1690.field_1842;
            if (class_3102 == null || bl || n2 < 8 || n3 < 8) {
                blackSinceMs = 0L;
                return;
            }
            if (blackFence != 0L) {
                if (GL32.glClientWaitSync((long)blackFence, (int)0, (long)0L) == 37147) {
                    return;
                }
                GL32.glDeleteSync((long)blackFence);
                blackFence = 0L;
                OverlayWindow.judgeBlack(OverlayWindow.readBlackSamples());
            }
            if (blackPbo == 0) {
                blackPbo = GL15.glGenBuffers();
                GL15.glBindBuffer((int)35051, (int)blackPbo);
                GL15.glBufferData((int)35051, (long)100L, (int)35041);
            } else {
                GL15.glBindBuffer((int)35051, (int)blackPbo);
            }
            GL11.glReadBuffer((int)1029);
            int n4 = 0;
            for (int i2 = 1; i2 <= 5; ++i2) {
                for (int i3 = 1; i3 <= 5; ++i3) {
                    GL11.glReadPixels((int)(n2 * i3 / 6), (int)(n3 * i2 / 6), (int)1, (int)1, (int)6408, (int)5121, (long)((long)n4++ * 4L));
                }
            }
            GL15.glBindBuffer((int)35051, (int)0);
            blackFence = GL32.glFenceSync((int)37143, (int)0);
        }
        catch (Throwable throwable) {
            blackSinceMs = 0L;
            try {
                GL15.glBindBuffer((int)35051, (int)0);
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
    }

    /*
     * Exception decompiling
     */
    private static boolean readBlackSamples() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[FORLOOP]], but top level block is 4[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static void releaseBlackWatch() {
        try {
            if (blackFence != 0L) {
                GL32.glDeleteSync((long)blackFence);
            }
            if (blackPbo != 0) {
                GL15.glDeleteBuffers((int)blackPbo);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        blackFence = 0L;
        blackPbo = 0;
        blackSinceMs = 0L;
    }

    private static void judgeBlack(boolean bl) {
        long l2 = System.currentTimeMillis();
        if (!bl) {
            blackSinceMs = 0L;
        } else if (blackSinceMs == 0L) {
            blackSinceMs = l2;
        } else if (l2 - blackSinceMs >= 6000L) {
            blackSinceMs = 0L;
            OverlayCompositor.overlayStayedBlack();
        }
    }

    private static boolean ensureBlit() {
        if (blitReady) {
            return true;
        }
        if (blitFailed) {
            return false;
        }
        try {
            String string = OverlayWindow.sp("\uc944\ua445\u8141\u5cd7\uf16e\u935d\ud4b9\uc513\u2767\u53dc\u5988\ubbfd\ue750\u8855\uc52a\u442a\ub609\ua24f\u0dbe\ub015\ub233\u128f\ud48c\u32fa\uf6e9\ud205\ub301\u18bd\uc9ab\u66cd\ua07e\u1be0\u31ef\u3c2b\u9ec8\u422b\u40bb\u6d56\ud31b\uef36\uf079\ufbe4\uf750\ub402\uab29\uf3bc\u58aa\udc30\u02e9\ucae4\u8feb\u56cf\u8ccb\ub0d0\u5187\uf6fb\u5595\ub527\uf687\u2c17\u6b99\u990d\u6af7\u090a\ue8e5\u194d\u6793\u8e8c\ue7b1\u78ba\u35c5\ufddc\u1894\udb0f\u3301\ueee9\uaffa\u32a6\u8244\u72e1\u5233\ud48d\udffb\u39a7\ucc5f\ub0c7\uf5d9\u1c5c\ucbca\u7e13\u0b45\ue537\u30dc\udc02\u500d\u6389\u5ed4\udcb7\u18ff\ucc9d\uac32\u30c6\ud6a5\u4c13\uede6\uabe3\u86f1\ua885\u3eec\u59ee\u26b2\ube4b\ue58f\u5cfd\u6b19\ub3fa\u4c3f\uc313\u72b2\u7c5a\ufe97\ubcef\udcef", 2035646137, 0xD91BBC65 ^ CryptoVault.mask());
            String string2 = OverlayWindow.sp("\u057e\u1d12\u3b97\u7377\ufd40\u9e28\uc5e4\u2c8c\u7c68\u8e09\udf54\u929e\u21b2\ue60b\ua5d4\uf759\uefef\udaa0\ufb65\u435b\u2786\u050f\u9b90\uae92\u2279\u98f5\u9f74\u4ae7\u651a\u03b3\ufd4d\u6d18\u5684\uda4d\ue6f6\uaea0\udfd8\u8621\u1843\u71ba\uc00c\u53ad\u1e16\ub74f\ua723\u2260\u9509\ue423\ufb61\u93e8\u6f91\ua294\u2d16\u242a\u41f0\u743f\u3731\u8bad\u15a2\ud396\u9929\u0898\u90bc\u89b2\ucf4e\u611a\ucfc2\u270d\uee6b\u1ab3\u1d6e\uc8ee\ue515\uf563\u0b0c\u5b75\u8d2a\ua7e3\u53b9\ue4e5\udf9a\u7fb1\ub69c\ufd1e\ub6ce\u4481\u5958\u4618\u3c3b\uab34\u221f\u5129\u4f5b\u1322\u8b9d\u6204\u0dd5\u407a\u2c27\u72ff\u7d9d\u01de\ufd92\u2576\u7684\udf58\uf4b7\u7489\ufa29\uf3b4\u42e2\u6230\uea7b\u4cc4\u8721\ufcda\u4c6e\udc7b\u3714\u6bfd\u5180\ucc5e\ud961\u08a7\uc4a1\u11b7\u04d7\ub754\uf5ec\u6328\ub4e7\u4b6e\u9411\u576e\u8629\ue2a8\ueac1\udd2f\u0162\u7420\u9dba\u686e\u3f5b\u8f88\ub540\u925a\u6593\u5f18\u729a\u7a08\ud872\uc6a5\u1f5d\u8600\u8387\ub339\u126f\u41e3\u362c\u4632\u4c49\u20cd\u5b46\ua8ec\u1d20\ubd28\uccd1\u527d\uf696\uf6f4\ub747\uc96a\ued90\ub66a\u2cd1\u72e9\u681e\u53f2\uffa8\u6d9a\uf919\u1160\ue0d9\udb4f\ua783\u17eb\u843e\u222f\u3a52\u0606\u1e97\u5081", -1436226932, 0xD91BBC65 ^ CryptoVault.mask());
            int n2 = OverlayWindow.compile(35633, string);
            int n3 = OverlayWindow.compile(35632, string2);
            blitProgram = GL20.glCreateProgram();
            GL20.glAttachShader((int)blitProgram, (int)n2);
            GL20.glAttachShader((int)blitProgram, (int)n3);
            GL20.glBindAttribLocation((int)blitProgram, (int)0, (CharSequence)OverlayWindow.sp("\ubb7e\u1d8e\ub7d3\u4226\u56bc\u8d46\u88f0\u75ed", -478705764, 0xD91BBC65 ^ CryptoVault.mask()));
            GL20.glBindAttribLocation((int)blitProgram, (int)1, (CharSequence)OverlayWindow.sp("\u8970\u7260\ufc72\u7b74", 1502094969, 0xD91BBC65 ^ CryptoVault.mask()));
            GL20.glLinkProgram((int)blitProgram);
            if (GL20.glGetProgrami((int)blitProgram, (int)35714) == 0) {
                GL20.glDeleteShader((int)n2);
                GL20.glDeleteShader((int)n3);
                GL20.glDeleteProgram((int)blitProgram);
                blitProgram = 0;
                blitFailed = true;
                return false;
            }
            GL20.glDeleteShader((int)n2);
            GL20.glDeleteShader((int)n3);
            blitTexUniform = GL20.glGetUniformLocation((int)blitProgram, (CharSequence)OverlayWindow.sp("\u73ff\ud738\u8052\uc1b7\uf0fe\u3edd\u5bf0", 1928912121, 0xD91BBC65 ^ CryptoVault.mask()));
            blitFlipUniform = GL20.glGetUniformLocation((int)blitProgram, (CharSequence)OverlayWindow.sp("\ua89b\ubfc8\uebc3\ua9ca\u4451\ub4fb\ue9dd\uc392", 66897080, 0xD91BBC65 ^ CryptoVault.mask()));
            float[] fArray = new float[]{-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
            FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)fArray.length);
            floatBuffer.put(fArray).flip();
            blitVao = GL30.glGenVertexArrays();
            blitVbo = GL15.glGenBuffers();
            GL30.glBindVertexArray((int)blitVao);
            GL15.glBindBuffer((int)34962, (int)blitVbo);
            GL15.glBufferData((int)34962, (FloatBuffer)floatBuffer, (int)35044);
            GL20.glEnableVertexAttribArray((int)0);
            GL20.glVertexAttribPointer((int)0, (int)2, (int)5126, (boolean)false, (int)16, (long)0L);
            GL20.glEnableVertexAttribArray((int)1);
            GL20.glVertexAttribPointer((int)1, (int)2, (int)5126, (boolean)false, (int)16, (long)8L);
            GL30.glBindVertexArray((int)0);
            GL15.glBindBuffer((int)34962, (int)0);
            blitReady = true;
            return true;
        }
        catch (Throwable throwable) {
            blitFailed = true;
            return false;
        }
    }

    private static int compile(int n2, String string) {
        int n3 = GL20.glCreateShader((int)n2);
        GL20.glShaderSource((int)n3, (CharSequence)string);
        GL20.glCompileShader((int)n3);
        return n3;
    }

    private static void restore(long l2) {
        try {
            GLFW.glfwMakeContextCurrent((long)l2);
            if (GLFW.glfwGetCurrentContext() != l2) {
                long l3;
                GLFW.glfwMakeContextCurrent((long)l2);
                if (GLFW.glfwGetCurrentContext() != l2 && l2 == 0L && (l3 = OverlayWindow.mcHandle()) != 0L) {
                    GLFW.glfwMakeContextCurrent((long)l3);
                }
            }
            if (mcCaps != null) {
                GL.setCapabilities((GLCapabilities)mcCaps);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void handOffFocusToGame() {
        try {
            long l2 = OverlayWindow.mcHandle();
            if (l2 != 0L && handle != 0L && GLFW.glfwGetWindowAttrib((long)handle, (int)131073) == 1) {
                GLFW.glfwFocusWindow((long)l2);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null) {
                return;
            }
            class_3102.execute(() -> {
                try {
                    if (McCompat.screen(class_3102) == null && class_3102.field_1729 != null && !class_3102.field_1729.method_1613()) {
                        class_3102.field_1729.method_1612();
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            });
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void destroy() {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 != null && !class_3102.method_18854()) {
            if (class_3102.method_22108()) {
                class_3102.execute(OverlayWindow::destroy);
            }
            return;
        }
        if (handle != 0L) {
            long l2 = GLFW.glfwGetCurrentContext();
            try {
                if (blitProgram != 0 || blitVao != 0 || blitVbo != 0 || blackPbo != 0 || blackFence != 0L) {
                    GLFW.glfwMakeContextCurrent((long)handle);
                    if (overlayCaps != null) {
                        GL.setCapabilities((GLCapabilities)overlayCaps);
                    }
                    if (blitProgram != 0) {
                        GL20.glDeleteProgram((int)blitProgram);
                    }
                    if (blitVao != 0) {
                        GL30.glDeleteVertexArrays((int)blitVao);
                    }
                    if (blitVbo != 0) {
                        GL15.glDeleteBuffers((int)blitVbo);
                    }
                    OverlayWindow.releaseBlackWatch();
                }
            }
            finally {
                OverlayWindow.restore(l2);
            }
        }
        visible = false;
        initFailed = false;
        affinityState = 0;
        affinityLookupDone = false;
        blitReady = false;
        blitFailed = false;
        initAttempts = 0;
        affinityRetries = 0;
        blitProgram = 0;
        blitVao = 0;
        blitVbo = 0;
        overlayCaps = null;
        OverlayCompositor.dispose();
    }

    static {
        boolean bl;
        handle = 0L;
        initFailed = false;
        visible = false;
        OS_NAME = System.getProperty(OverlayWindow.sp("\u7bf0\ubc07\u0b9f\u39e8\ueddb\u58bf\ue0ad\u1851\u1283\u49b8", -2017663622, 0xD91BBC65 ^ CryptoVault.mask()), "").toLowerCase(Locale.ROOT);
        IS_WINDOWS = OS_NAME.contains(OverlayWindow.sp("\u386e\u5c9e\ubd78\uf5dc\u5441\u1476\u8ca8", 1003674090, 0xD91BBC65 ^ CryptoVault.mask()));
        IS_MAC = OS_NAME.contains(OverlayWindow.sp("\ud5d9\u410f\ua6f9\ue35a\ua981", 688233905, 0xD91BBC65 ^ CryptoVault.mask())) || OS_NAME.contains(OverlayWindow.sp("\u09e8\u46ea\uc30b\ue980\ubc08\ua36a\udefb", 437133281, 0xD91BBC65 ^ CryptoVault.mask()));
        try {
            bl = FabricLoader.getInstance().isModLoaded(OverlayWindow.sp("\u6a13\u1282\uaedf\u019e\u4445", -1515402649, 0xD91BBC65 ^ CryptoVault.mask()));
        }
        catch (Throwable throwable) {
            bl = false;
        }
        IRIS_PRESENT = bl;
        blitReady = false;
        blitFailed = false;
        blitProgram = 0;
        blitVao = 0;
        blitVbo = 0;
        blitTexUniform = -1;
        blitFlipUniform = -1;
        affinityState = 0;
        boundsX = Integer.MIN_VALUE;
        boundsY = Integer.MIN_VALUE;
        boundsW = -1;
        boundsH = -1;
    }

    private static /* synthetic */ String sp(String string, int n2, int n3) {
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

