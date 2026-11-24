/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  org.lwjgl.PointerBuffer
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWVidMode
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.util.GlfwPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

@Environment(value=EnvType.CLIENT)
public final class AutoMenuScale {
    static final float REFERENCE_HEIGHT_MM = 299.0f;
    static final float EXPONENT = 0.3f;
    static final float MIN_CORRECTION = 0.85f;
    static final float MAX_CORRECTION = 1.25f;
    static final float MIN_PX_PER_MM = 1.5f;
    static final float MAX_PX_PER_MM = 40.0f;
    private static final float CSS_PX_PER_MM = 3.7795277f;
    private static final long PROBE_INTERVAL_NS = 1000000000L;
    private static float cachedPxPerMm = Float.NaN;
    private static long probedAt = -4611686018427387904L;

    private AutoMenuScale() {
    }

    public static float correction(float f2, float f3) {
        if (!(f2 > 0.0f && Float.isFinite(f2) && f3 > 0.0f && Float.isFinite(f3))) {
            return 1.0f;
        }
        float f4 = f2 / f3;
        float f5 = (float)Math.pow(299.0f / f4, 0.3f);
        if (!Float.isFinite(f5)) {
            return 1.0f;
        }
        return Math.max(0.85f, Math.min(1.25f, f5));
    }

    static float pxPerMm(int n2, int n3, float f2, float f3) {
        float f4;
        float f5;
        float f6 = f5 = f2 > 0.0f && Float.isFinite(f2) ? f2 : 1.0f;
        if (n2 > 0 && n3 >= 40 && n3 <= 2000 && (f4 = (float)n2 * f5 / (float)n3) >= 1.5f && f4 <= 40.0f) {
            return f4;
        }
        if (f3 > 0.0f && Float.isFinite(f3) && (f4 = 3.7795277f * f3) >= 1.5f && f4 <= 40.0f) {
            return f4;
        }
        return Float.NaN;
    }

    public static float currentPxPerMm(class_310 class_3102) {
        long l2 = System.nanoTime();
        if (l2 - probedAt < 1000000000L) {
            return cachedPxPerMm;
        }
        probedAt = l2;
        float f2 = Float.NaN;
        try {
            if (class_3102 != null && class_3102.method_22683() != null) {
                f2 = AutoMenuScale.probe(class_3102.method_22683().method_4490());
            }
        }
        catch (Throwable throwable) {
            f2 = Float.NaN;
        }
        cachedPxPerMm = f2;
        return f2;
    }

    private static float probe(long l2) {
        if (l2 == 0L) {
            return Float.NaN;
        }
        long l3 = GLFW.glfwGetWindowMonitor((long)l2);
        if (l3 == 0L) {
            l3 = AutoMenuScale.monitorUnderWindow(l2);
        }
        if (l3 == 0L) {
            l3 = GLFW.glfwGetPrimaryMonitor();
        }
        if (l3 == 0L) {
            return Float.NaN;
        }
        GLFWVidMode gLFWVidMode = GLFW.glfwGetVideoMode((long)l3);
        int[] nArray = new int[1];
        int[] nArray2 = new int[1];
        GLFW.glfwGetMonitorPhysicalSize((long)l3, (int[])nArray, (int[])nArray2);
        float[] fArray = new float[1];
        float[] fArray2 = new float[1];
        GLFW.glfwGetMonitorContentScale((long)l3, (float[])fArray, (float[])fArray2);
        int[] nArray3 = new int[1];
        int[] nArray4 = new int[1];
        int[] nArray5 = new int[1];
        int[] nArray6 = new int[1];
        GLFW.glfwGetWindowSize((long)l2, (int[])nArray3, (int[])nArray4);
        GLFW.glfwGetFramebufferSize((long)l2, (int[])nArray5, (int[])nArray6);
        float f2 = nArray4[0] > 0 && nArray6[0] > 0 ? (float)nArray6[0] / (float)nArray4[0] : 1.0f;
        return AutoMenuScale.pxPerMm(gLFWVidMode == null ? 0 : gLFWVidMode.height(), nArray2[0], f2, fArray2[0]);
    }

    private static long monitorUnderWindow(long l2) {
        if (GlfwPlatform.isWayland()) {
            return 0L;
        }
        PointerBuffer pointerBuffer = GLFW.glfwGetMonitors();
        if (pointerBuffer == null) {
            return 0L;
        }
        int[] nArray = new int[1];
        int[] nArray2 = new int[1];
        int[] nArray3 = new int[1];
        int[] nArray4 = new int[1];
        GLFW.glfwGetWindowPos((long)l2, (int[])nArray, (int[])nArray2);
        GLFW.glfwGetWindowSize((long)l2, (int[])nArray3, (int[])nArray4);
        int n2 = nArray[0] + nArray3[0] / 2;
        int n3 = nArray2[0] + nArray4[0] / 2;
        int[] nArray5 = new int[1];
        int[] nArray6 = new int[1];
        for (int i2 = 0; i2 < pointerBuffer.limit(); ++i2) {
            long l3 = pointerBuffer.get(i2);
            GLFWVidMode gLFWVidMode = GLFW.glfwGetVideoMode((long)l3);
            if (gLFWVidMode == null) continue;
            GLFW.glfwGetMonitorPos((long)l3, (int[])nArray5, (int[])nArray6);
            if (n2 < nArray5[0] || n3 < nArray6[0] || n2 >= nArray5[0] + gLFWVidMode.width() || n3 >= nArray6[0] + gLFWVidMode.height()) continue;
            return l3;
        }
        return 0L;
    }
}

