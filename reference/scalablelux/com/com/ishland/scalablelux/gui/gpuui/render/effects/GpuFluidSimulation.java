/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.BlendFunction
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  com.mojang.blaze3d.vertex.VertexFormatElement
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_10799
 *  net.minecraft.class_10868
 *  net.minecraft.class_11231
 *  net.minecraft.class_11244
 *  net.minecraft.class_11246
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3264
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 *  net.minecraft.class_4588
 *  net.minecraft.class_8030
 *  org.joml.Matrix3x2fc
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL
 *  org.lwjgl.opengl.GL33
 *  org.lwjgl.opengl.GLCapabilities
 */
package com.ishland.scalablelux.gui.gpuui.render.effects;

import com.ishland.scalablelux.gui.gpuui.render.GpuUiClipMask;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.util.ClientInternalLog;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.RenderBackendDetector;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_10799;
import net.minecraft.class_10868;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_11246;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

@Environment(value=EnvType.CLIENT)
public final class GpuFluidSimulation {
    private static final class_2960 LIQUID_VERTEX = class_2960.method_60655((String)GpuFluidSimulation.ni("\ucd08\u9edb\u1dea\ue6cd\u139b\ud17a\u1fd6\u9516\uf102\u97ef\u98f8\uae43\uc8f9", -343077035, 0x636CFAF2 ^ CryptoVault.mask()), (String)GpuFluidSimulation.ni("\ufbb9\u5d62\uc724\u0a58\u939e\u5a63\ud14c\uf991\u5cbb\u47f7\ueb03\u0971\u3ba0\u0ef4\u69c6\u96e9\u0982\u1c54\u9037", 1992516387, 0x636CFAF2 ^ CryptoVault.mask()));
    private static final class_2960 DISPLAY_FRAGMENT = class_2960.method_60655((String)GpuFluidSimulation.ni("\ua373\ue600\ue915\u900a\u0a77\u1464\ufd1f\u3afd\u640a\u1c81\ud7bb\u886a\u5e4b\u1aad\u600c", 5079953, 0x636CFAF2 ^ CryptoVault.mask()), (String)GpuFluidSimulation.ni("\u4421\u5492\u99e8\ue6b6\uc488\u18bb\u9cd7\u1063\u17fb\ud94a\uab26\u9aaf\u28db\u138e\u7b30\uada4\u39af\uf9df\ub05c\u953c\uc8f7\u4244\u8a3b\uaff7\u50d1\ubb8d", 1389253887, 0x636CFAF2 ^ CryptoVault.mask()));
    private static final VertexFormat UI_FORMAT = VertexFormat.builder().add(GpuFluidSimulation.ni("\ufb1b\ube28\u6431\u6a98\uf57e\uf36e\u7ea3\ub721\u36dc", 2018443854, 0x636CFAF2 ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuFluidSimulation.ni("\u37f1\u247b\u1b32\u7f01\u0b4d\u4e69", -1226851865, 0x636CFAF2 ^ CryptoVault.mask()), VertexFormatElement.COLOR).add(GpuFluidSimulation.ni("\ub657\u7d1f\u9415\ud0b1\u16e6\uf123\u7aef", 1039475613, 0x636CFAF2 ^ CryptoVault.mask()), VertexFormatElement.UV0).add(GpuFluidSimulation.ni("\ua21b\u886d\u20ba\ud91c\u6457\u182c", 1486609868, 0x636CFAF2 ^ CryptoVault.mask()), VertexFormatElement.UV1).add(GpuFluidSimulation.ni("\uc935\u783a\u22ee\u59f9\ua566\ua3a3\ud236\u3cbc\uf715\uc2b4\u5f2c\u5f04\u2f2e", -866535022, 0x636CFAF2 ^ CryptoVault.mask()), VertexFormatElement.LINE_WIDTH).build();
    private static final RenderPipeline DISPLAY_PIPELINE = SafePipelines.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuFluidSimulation.ni("\ucd87\u5804\ud9ff\ue66c\u198c\u939c\ufaba\u99a7\ua337\ud01f\u3cf7\uecc9", 1417006705, 0x636CFAF2 ^ CryptoVault.mask()), (String)GpuFluidSimulation.ni("\u2a1a\u70bc\u7f9a\u1236\u9d20\u5a51\u79ca\u77cb\ucecc\u466c\u2e31\u1aac\ud207\ubf03\uee10\u7500\ucdf7\u3152\ufacf\u4859\u721a\u1a48\u5e15\u1f5b\u0bff\ue9b8\u33f5\uda2a\u35b7\ue297", -1220557213, 0x636CFAF2 ^ CryptoVault.mask()))).withVertexShader(LIQUID_VERTEX).withFragmentShader(DISPLAY_FRAGMENT).withVertexFormat(UI_FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuFluidSimulation.ni("\u0919\u0282\u17fe\uf25c\u5953\u6af0\ub676\uf449\u54ff\u5403\u6067", -914372343, 0x636CFAF2 ^ CryptoVault.mask())).withSampler(GpuFluidSimulation.ni("\uc5e3\u36a6\u23f4\u7831\ufcad\u3451\ua116\u4604\u6c1d\u5502\u6e17\udf2e", 136079241, 0x636CFAF2 ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
    private static final float PRESSURE_DECAY = 0.8f;
    private static final float COLOR_UPDATE_SPEED = 10.0f;
    private static final int BLOOM_ITERATIONS = 8;
    private static final float BLOOM_INTENSITY = 0.8f;
    private static final float BLOOM_THRESHOLD = 0.6f;
    private static final float BLOOM_SOFT_KNEE = 0.7f;
    private static final float MAX_DT = 0.016666668f;
    private static final long SESSION_GAP_NANOS = 350000000L;
    private static final int[][] TIERS = new int[][]{{64, 256, 128}, {128, 512, 256}, {128, 1024, 256}};
    private static int cfgQuality = 2;
    private static float cfgCurl = 30.0f;
    private static float cfgSplatRadius = 0.25f;
    private static float cfgSplatForce = 6000.0f;
    private static float cfgDyeDissipation = 1.0f;
    private static float cfgVelocityDissipation = 0.2f;
    private static int cfgPressureIterations = 20;
    private static boolean cfgShading = true;
    private static boolean cfgBloom = true;
    private static boolean cfgRainbow = true;
    private static boolean cfgRandomSplats = true;
    private static boolean cfgPaused;
    private static boolean pending;
    private static int unconsumedFrames;
    private static long lastRequestNanos;
    private static boolean newSession;
    private static float reqCursorU;
    private static float reqCursorV;
    private static boolean reqInject;
    private static float reqFlow;
    private static int reqDye0;
    private static int reqDye1;
    private static int reqDye2;
    private static int reqColorCount;
    private static int simW;
    private static int simH;
    private static int dyeW;
    private static int dyeH;
    private static int bloomW;
    private static int bloomH;
    private static float aspect;
    private static int qualityDrop;
    private static float avgFrameSeconds;
    private static int slowFrames;
    private static long lastStepNanos;
    private static float prevU;
    private static float prevV;
    private static float colorTimer;
    private static int paletteIndex;
    private static final float[] pointerColor;
    private static final float[] scratchColor;
    private static boolean failed;
    private static boolean cleanupRegistered;
    private static volatile boolean releaseRequested;
    private static final Object LOCK;
    private static GpuUiEffectPasses.Target output;
    private static int outputFbo;
    private static int outputFboTexture;
    private static boolean glReady;
    private static int vao;
    private static Program copyProgram;
    private static Program clearProgram;
    private static Program splatProgram;
    private static Program advectionProgram;
    private static Program divergenceProgram;
    private static Program curlProgram;
    private static Program vorticityProgram;
    private static Program pressureProgram;
    private static Program gradientProgram;
    private static Program bloomPrefilterProgram;
    private static Program bloomBlurProgram;
    private static Program bloomFinalProgram;
    private static Program displayProgram;
    private static int[] fmtRgba;
    private static int[] fmtRg;
    private static int[] fmtR;
    private static DoubleFbo velocity;
    private static DoubleFbo dye;
    private static DoubleFbo pressure;
    private static Fbo divergence;
    private static Fbo curl;
    private static Fbo bloom;
    private static final Fbo[] bloomChain;
    private static int bloomChainLength;
    private static boolean samplerObjects;
    private static final IntBuffer VIEWPORT;
    private static final ByteBuffer COLOR_MASK;
    private static final FloatBuffer CLEAR_COLOR;

    private GpuFluidSimulation() {
    }

    public static void registerPipelines() {
        if (DISPLAY_PIPELINE == null) {
            throw new IllegalStateException(GpuFluidSimulation.ni("\ued4d\ua029\u7671\ue769\u1c91\ud26c\u5225\u44f2\u17fb\ub7a5\u0178\u7e2d\u0944\u7006\u38be\u367f\u6fd2\u0ecb\u42f6\uc9b4\u1d71\ue4dc\u8576\u0af3\u2fb3\u42cc\u3b19\uecda\u9321\uced7\u6d96\u7a7e\u8278\u43ad\uc403\udb4b\u3a81\u1eb8\ud055\u9e44\u3eba\u3782", -603077696, 0x636CFAF2 ^ CryptoVault.mask()));
        }
        GpuFluidSimulation.ensureCleanupRegistered();
    }

    public static void resetQuality() {
        qualityDrop = 0;
    }

    public static boolean usable() {
        if (failed) {
            return false;
        }
        try {
            return !RenderBackendDetector.isVulkanBackend() && GpuUiEffectPasses.isAvailable() && SafePipelines.usable(DISPLAY_PIPELINE);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static void configure(int n2, float f2, float f3, float f4, float f5, float f6, int n3, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        cfgQuality = Math.max(0, Math.min(2, n2));
        cfgCurl = GpuFluidSimulation.clamp(f2, 0.0f, 100.0f);
        cfgSplatRadius = GpuFluidSimulation.clamp(f3, 0.01f, 2.0f);
        cfgSplatForce = GpuFluidSimulation.clamp(f4, 0.0f, 50000.0f);
        cfgDyeDissipation = GpuFluidSimulation.clamp(f5, 0.0f, 10.0f);
        cfgVelocityDissipation = GpuFluidSimulation.clamp(f6, 0.0f, 10.0f);
        cfgPressureIterations = Math.max(1, Math.min(80, n3));
        cfgShading = bl;
        cfgBloom = bl2;
        cfgRainbow = bl3;
        cfgRandomSplats = bl4;
        cfgPaused = bl5;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void render(class_11246 class_112462, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, int n2, int n3, int n4, int n5, float f11, float f12, boolean bl, class_8030 class_80302, GpuUiClipMask gpuUiClipMask) {
        if (class_112462 == null || !(f4 > 0.0f) || !(f5 > 0.0f)) {
            return;
        }
        if (!Float.isFinite(f11) || f11 <= 0.0f) {
            return;
        }
        if (!GpuFluidSimulation.usable()) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        releaseRequested = false;
        Object object = LOCK;
        synchronized (object) {
            try {
                class_8030 class_80303;
                class_8030 class_80304 = GpuFluidSimulation.rectangle(f2, f3, f4, f5).method_65185(matrix3x2fc);
                class_8030 class_80305 = class_80303 = class_80302 == null ? null : class_80302.method_49701(class_80304);
                if (class_80302 != null && class_80303 == null) {
                    return;
                }
                long l2 = System.nanoTime();
                if (lastRequestNanos == 0L || l2 - lastRequestNanos > 350000000L) {
                    newSession = true;
                    qualityDrop = 0;
                    slowFrames = 0;
                }
                lastRequestNanos = l2;
                class_310 class_3102 = class_310.method_1551();
                float f13 = 1.0f;
                if (class_3102 != null && class_3102.method_22683() != null && class_3102.method_22683().method_4486() > 0) {
                    f13 = (float)class_3102.method_22683().method_4489() / (float)class_3102.method_22683().method_4486();
                }
                float f14 = Math.max(16.0f, (float)class_80304.comp_1196() * f13);
                float f15 = Math.max(16.0f, (float)class_80304.comp_1197() * f13);
                GpuFluidSimulation.computeDimensions(f14, f15);
                if (!GpuFluidSimulation.ensureOutput(dyeW, dyeH)) {
                    return;
                }
                int n6 = unconsumedFrames = pending ? unconsumedFrames + 1 : 0;
                if (unconsumedFrames > 60) {
                    GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u83ad\u226d\uba69\udd31\u5158\u9db2\uf5fe\ua6a9\ud4c6\u09e3\u2d1a\u4851U\u8c1d\u5334\ua3ba\uc9b7\ud66d\u226f\u0fbb\ua451\u1301\u5595\uc9e6\uf1e4\u3e29\u6df9\u9a47\u26fa\u4a0b\u8ae6\uef83\u596d\ue92e\u2bac\u7df2\u47c7\uc4d2\ua17d\u753e\uc259\u346b\ua757\u70d0\ucadd\ud8a6\u8a00\u00be\ue56b\u993b\ucdd5\u47d1\u515a\uf160\u6685\u29ca\uc14b\u6418\u99e6\uf261\ucfaf\u8586\u810c\u7748\ue8ac\ucd9f", 1038315829, 0x636CFAF2 ^ CryptoVault.mask()), null);
                    return;
                }
                reqCursorU = f7;
                reqCursorV = f8;
                reqInject = bl;
                reqFlow = GpuFluidSimulation.clamp(f12, 0.05f, 4.0f);
                reqDye0 = n2;
                reqDye1 = n3;
                reqDye2 = n4;
                reqColorCount = n5;
                pending = true;
                int n7 = Math.round(GpuFluidSimulation.clamp(f11, 0.0f, 1.0f) * 255.0f);
                int n8 = n7 << 24 | 0xFFFFFF;
                float f16 = GpuFluidSimulation.clamp(f6, 0.0f, Math.min(f4, f5) * 0.5f);
                class_11231 class_112312 = (gpuUiClipMask == null ? GpuUiClipMask.none() : gpuUiClipMask).withMask(GpuUiEffectPasses.texture(output));
                GpuUiRenderer.submit(class_112462, new FluidState(DISPLAY_PIPELINE, class_112312, matrix3x2fc, f2, f3, f4, f5, f16, n8, class_80304, class_80303));
            }
            catch (LinkageError | RuntimeException throwable) {
                GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u5780\u9fbb\u0f1d\u3412\u26e2\ua919\ua538\uacad\u884d\u60c2\u227a\uc8a9\u1ba0\ue4ba\u1a51\u9824\u019d\u9812\u9bdb\ua108\u8170\ua6d5\ue157\u1f59\udcfa\u9674\u7f7f\u2c3d\u676c\u598f\udae9\u419b\uc98c\u5b75\u8c0c\u8395\u97c0\uc488\ue112\u935e\u7943\u806c\ub46f\u3dbb\uac47\u5ed2\u5f39\ua7dc\u09f2\ubf34\uc55a\u30db\ue63b\u441f", 1164431131, 0x636CFAF2 ^ CryptoVault.mask()), throwable);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void runPending() {
        if (!pending) {
            return;
        }
        pending = false;
        if (failed || output == null || output.closed()) {
            return;
        }
        if (!RenderSystem.isOnRenderThread()) {
            return;
        }
        Object object = LOCK;
        synchronized (object) {
            SavedState savedState = SavedState.INSTANCE;
            boolean bl = false;
            try {
                savedState.capture();
                bl = true;
                if (!GpuFluidSimulation.ensureGl()) {
                    return;
                }
                GpuFluidSimulation.prepareCommonState();
                GpuFluidSimulation.ensureFields();
                GpuFluidSimulation.ensureOutputFbo();
                long l2 = System.nanoTime();
                float f2 = lastStepNanos == 0L ? 0.016666668f : (float)(l2 - lastStepNanos) / 1.0E9f;
                lastStepNanos = l2;
                GpuFluidSimulation.adaptQuality(f2);
                float f3 = Math.min(Math.max(f2, 5.0E-4f), 0.016666668f);
                GpuFluidSimulation.prepareCommonState();
                if (newSession) {
                    newSession = false;
                    GpuFluidSimulation.clearAllFields();
                    prevU = reqCursorU;
                    prevV = reqCursorV;
                    colorTimer = 0.0f;
                    GpuFluidSimulation.nextPointerColor();
                    if (cfgRandomSplats) {
                        GpuFluidSimulation.multipleSplats(5 + ThreadLocalRandom.current().nextInt(20));
                    }
                }
                if (!cfgPaused) {
                    GpuFluidSimulation.updateColors(f3);
                    GpuFluidSimulation.applyPointer();
                    GpuFluidSimulation.step(Math.min(f3 * reqFlow, 0.05f));
                }
                GpuFluidSimulation.renderDisplay();
            }
            catch (Throwable throwable) {
                GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u2e20\u7be7\u7ae4\u4dd1\u7351\uce48\uf390\u336b\ueac1\u72a4\ucedb\u1bf0\u146b\uf0db\u8069\u337c\ufe6c\u5c3b\u6f77\u4d7a\u8dbe\u0866\ue5b6\u01b7\u2118\ub7fc\u6ab7\ub983\ubb0b\u82aa\ueee2\ud68f\u587c", -121433566, 0x636CFAF2 ^ CryptoVault.mask()), throwable);
            }
            finally {
                if (bl) {
                    savedState.restore(samplerObjects);
                }
            }
        }
    }

    public static void releaseSoon() {
        releaseRequested = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void finishFrame() {
        if (!releaseRequested) {
            return;
        }
        releaseRequested = false;
        if (RenderSystem.isOnRenderThread()) {
            Object object = LOCK;
            synchronized (object) {
                GpuFluidSimulation.releaseAll();
            }
        }
    }

    private static void step(float f2) {
        GL33.glDisable((int)3042);
        float f3 = 1.0f / (float)GpuFluidSimulation.velocity.width;
        float f4 = 1.0f / (float)GpuFluidSimulation.velocity.height;
        GpuFluidSimulation.use(curlProgram, f3, f4);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.blit(curl);
        GpuFluidSimulation.use(vorticityProgram, f3, f4);
        GL33.glUniform1f((int)GpuFluidSimulation.vorticityProgram.curl, (float)cfgCurl);
        GL33.glUniform1f((int)GpuFluidSimulation.vorticityProgram.dt, (float)f2);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.bind(1, GpuFluidSimulation.curl.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.velocity.write);
        velocity.swap();
        GpuFluidSimulation.use(divergenceProgram, f3, f4);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.blit(divergence);
        GpuFluidSimulation.use(clearProgram, f3, f4);
        GL33.glUniform1f((int)GpuFluidSimulation.clearProgram.value, (float)0.8f);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.pressure.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.pressure.write);
        pressure.swap();
        GpuFluidSimulation.use(pressureProgram, f3, f4);
        GpuFluidSimulation.bind(1, GpuFluidSimulation.divergence.texture);
        for (int i2 = 0; i2 < cfgPressureIterations; ++i2) {
            GpuFluidSimulation.bind(0, GpuFluidSimulation.pressure.read.texture);
            GpuFluidSimulation.blit(GpuFluidSimulation.pressure.write);
            pressure.swap();
        }
        GpuFluidSimulation.use(gradientProgram, f3, f4);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.pressure.read.texture);
        GpuFluidSimulation.bind(1, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.velocity.write);
        velocity.swap();
        GpuFluidSimulation.use(advectionProgram, f3, f4);
        GL33.glUniform1f((int)GpuFluidSimulation.advectionProgram.dt, (float)f2);
        GL33.glUniform1f((int)GpuFluidSimulation.advectionProgram.dissipation, (float)cfgVelocityDissipation);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.bind(1, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.velocity.write);
        velocity.swap();
        GL33.glUniform1f((int)GpuFluidSimulation.advectionProgram.dissipation, (float)cfgDyeDissipation);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.bind(1, GpuFluidSimulation.dye.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.dye.write);
        dye.swap();
    }

    private static void applyPointer() {
        boolean bl;
        float f2 = reqCursorU;
        float f3 = reqCursorV;
        boolean bl2 = bl = Float.isFinite(f2) && Float.isFinite(f3) && f2 >= 0.0f && f2 <= 1.0f && f3 >= 0.0f && f3 <= 1.0f;
        if (!bl) {
            prevU = Float.NaN;
            prevV = Float.NaN;
            return;
        }
        if (!(reqInject && Float.isFinite(prevU) && Float.isFinite(prevV))) {
            prevU = f2;
            prevV = f3;
            return;
        }
        float f4 = f2 - prevU;
        float f5 = -(f3 - prevV);
        prevU = f2;
        prevV = f3;
        if (f4 == 0.0f && f5 == 0.0f) {
            return;
        }
        if (aspect < 1.0f) {
            f4 *= aspect;
        }
        if (aspect > 1.0f) {
            f5 /= aspect;
        }
        GpuFluidSimulation.splat(f2, 1.0f - f3, f4 * cfgSplatForce, f5 * cfgSplatForce, pointerColor[0], pointerColor[1], pointerColor[2]);
    }

    private static void multipleSplats(int n2) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (int i2 = 0; i2 < n2; ++i2) {
            float[] fArray = GpuFluidSimulation.generateColor(scratchColor);
            float f2 = fArray[0] * 10.0f;
            float f3 = fArray[1] * 10.0f;
            float f4 = fArray[2] * 10.0f;
            float f5 = threadLocalRandom.nextFloat();
            float f6 = threadLocalRandom.nextFloat();
            float f7 = 1000.0f * (threadLocalRandom.nextFloat() - 0.5f);
            float f8 = 1000.0f * (threadLocalRandom.nextFloat() - 0.5f);
            GpuFluidSimulation.splat(f5, f6, f7, f8, f2, f3, f4);
        }
    }

    private static void splat(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = cfgSplatRadius / 100.0f;
        if (aspect > 1.0f) {
            f9 *= aspect;
        }
        GpuFluidSimulation.use(splatProgram, 1.0f / (float)GpuFluidSimulation.velocity.width, 1.0f / (float)GpuFluidSimulation.velocity.height);
        GL33.glUniform1f((int)GpuFluidSimulation.splatProgram.aspectRatio, (float)aspect);
        GL33.glUniform2f((int)GpuFluidSimulation.splatProgram.point, (float)f2, (float)f3);
        GL33.glUniform1f((int)GpuFluidSimulation.splatProgram.radius, (float)f9);
        GL33.glUniform3f((int)GpuFluidSimulation.splatProgram.color, (float)f4, (float)f5, (float)0.0f);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.velocity.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.velocity.write);
        velocity.swap();
        GL33.glUniform3f((int)GpuFluidSimulation.splatProgram.color, (float)f6, (float)f7, (float)f8);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.dye.read.texture);
        GpuFluidSimulation.blit(GpuFluidSimulation.dye.write);
        dye.swap();
    }

    private static void updateColors(float f2) {
        if ((colorTimer += f2 * 10.0f) >= 1.0f) {
            colorTimer %= 1.0f;
            GpuFluidSimulation.nextPointerColor();
        }
    }

    private static void nextPointerColor() {
        float[] fArray = GpuFluidSimulation.generateColor(scratchColor);
        GpuFluidSimulation.pointerColor[0] = fArray[0];
        GpuFluidSimulation.pointerColor[1] = fArray[1];
        GpuFluidSimulation.pointerColor[2] = fArray[2];
    }

    private static float[] generateColor(float[] fArray) {
        if (cfgRainbow) {
            GpuFluidSimulation.hsvToRgb(ThreadLocalRandom.current().nextFloat(), 1.0f, 1.0f, fArray);
        } else {
            int n2;
            int n3 = Math.max(1, Math.min(3, reqColorCount));
            int n4 = (n2 = Math.floorMod(paletteIndex++, n3)) == 0 ? reqDye0 : (n2 == 1 ? reqDye1 : reqDye2);
            fArray[0] = (float)(n4 >> 16 & 0xFF) / 255.0f;
            fArray[1] = (float)(n4 >> 8 & 0xFF) / 255.0f;
            fArray[2] = (float)(n4 & 0xFF) / 255.0f;
        }
        fArray[0] = fArray[0] * 0.15f;
        fArray[1] = fArray[1] * 0.15f;
        fArray[2] = fArray[2] * 0.15f;
        return fArray;
    }

    private static void hsvToRgb(float f2, float f3, float f4, float[] fArray) {
        int n2 = (int)Math.floor(f2 * 6.0f);
        float f5 = f2 * 6.0f - (float)n2;
        float f6 = f4 * (1.0f - f3);
        float f7 = f4 * (1.0f - f5 * f3);
        float f8 = f4 * (1.0f - (1.0f - f5) * f3);
        switch (Math.floorMod(n2, 6)) {
            case 0: {
                fArray[0] = f4;
                fArray[1] = f8;
                fArray[2] = f6;
                break;
            }
            case 1: {
                fArray[0] = f7;
                fArray[1] = f4;
                fArray[2] = f6;
                break;
            }
            case 2: {
                fArray[0] = f6;
                fArray[1] = f4;
                fArray[2] = f8;
                break;
            }
            case 3: {
                fArray[0] = f6;
                fArray[1] = f7;
                fArray[2] = f4;
                break;
            }
            case 4: {
                fArray[0] = f8;
                fArray[1] = f6;
                fArray[2] = f4;
                break;
            }
            default: {
                fArray[0] = f4;
                fArray[1] = f6;
                fArray[2] = f7;
            }
        }
    }

    private static void renderDisplay() {
        boolean bl;
        boolean bl2 = bl = cfgBloom && bloom != null && bloomChainLength >= 2;
        if (bl) {
            GpuFluidSimulation.applyBloom();
        }
        GL33.glDisable((int)3042);
        GL33.glBindFramebuffer((int)36160, (int)outputFbo);
        GL33.glViewport((int)0, (int)0, (int)output.width(), (int)output.height());
        GL33.glUseProgram((int)GpuFluidSimulation.displayProgram.id);
        GL33.glUniform2f((int)GpuFluidSimulation.displayProgram.texelSize, (float)(1.0f / (float)GpuFluidSimulation.dye.width), (float)(1.0f / (float)GpuFluidSimulation.dye.height));
        GL33.glUniform1i((int)GpuFluidSimulation.displayProgram.shading, (int)(cfgShading ? 1 : 0));
        GL33.glUniform1i((int)GpuFluidSimulation.displayProgram.bloomOn, (int)(bl ? 1 : 0));
        GpuFluidSimulation.bind(0, GpuFluidSimulation.dye.read.texture);
        GpuFluidSimulation.bind(1, bl ? GpuFluidSimulation.bloom.texture : GpuFluidSimulation.dye.read.texture);
        GpuFluidSimulation.draw();
    }

    private static void applyBloom() {
        Fbo fbo;
        int n2;
        float f2 = 0.4201f;
        GL33.glDisable((int)3042);
        GpuFluidSimulation.use(bloomPrefilterProgram, 0.0f, 0.0f);
        GL33.glUniform3f((int)GpuFluidSimulation.bloomPrefilterProgram.curve, (float)(0.6f - f2), (float)(f2 * 2.0f), (float)(0.25f / f2));
        GL33.glUniform1f((int)GpuFluidSimulation.bloomPrefilterProgram.threshold, (float)0.6f);
        GpuFluidSimulation.bind(0, GpuFluidSimulation.dye.read.texture);
        GpuFluidSimulation.blit(bloom);
        Fbo fbo2 = bloom;
        GL33.glUseProgram((int)GpuFluidSimulation.bloomBlurProgram.id);
        for (n2 = 0; n2 < bloomChainLength; ++n2) {
            fbo = bloomChain[n2];
            GL33.glUniform2f((int)GpuFluidSimulation.bloomBlurProgram.texelSize, (float)(1.0f / (float)fbo2.width), (float)(1.0f / (float)fbo2.height));
            GpuFluidSimulation.bind(0, fbo2.texture);
            GpuFluidSimulation.blit(fbo);
            fbo2 = fbo;
        }
        GL33.glBlendFunc((int)1, (int)1);
        GL33.glBlendEquation((int)32774);
        GL33.glEnable((int)3042);
        for (n2 = bloomChainLength - 2; n2 >= 0; --n2) {
            fbo = bloomChain[n2];
            GL33.glUniform2f((int)GpuFluidSimulation.bloomBlurProgram.texelSize, (float)(1.0f / (float)fbo2.width), (float)(1.0f / (float)fbo2.height));
            GpuFluidSimulation.bind(0, fbo2.texture);
            GpuFluidSimulation.blit(fbo);
            fbo2 = fbo;
        }
        GL33.glDisable((int)3042);
        GpuFluidSimulation.use(bloomFinalProgram, 1.0f / (float)fbo2.width, 1.0f / (float)fbo2.height);
        GL33.glUniform1f((int)GpuFluidSimulation.bloomFinalProgram.intensity, (float)0.8f);
        GpuFluidSimulation.bind(0, fbo2.texture);
        GpuFluidSimulation.blit(bloom);
    }

    private static int effectiveQuality() {
        return Math.max(0, cfgQuality - qualityDrop);
    }

    private static void computeDimensions(float f2, float f3) {
        f2 = Math.max(128.0f, (float)Math.ceil(f2 / 128.0f) * 128.0f);
        f3 = Math.max(128.0f, (float)Math.ceil(f3 / 128.0f) * 128.0f);
        aspect = f2 / f3;
        int[] nArray = TIERS[GpuFluidSimulation.effectiveQuality()];
        int n2 = (int)Math.min(f2, f3);
        int n3 = 4096;
        try {
            n3 = Math.max(256, RenderSystem.getDevice().getMaxTextureSize());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        long l2 = GpuFluidSimulation.resolution(nArray[0], aspect);
        simW = GpuFluidSimulation.quantize((int)(l2 >> 32), 8, n3);
        simH = GpuFluidSimulation.quantize((int)l2, 8, n3);
        int n4 = Math.min(nArray[1], Math.max(64, n2));
        long l3 = GpuFluidSimulation.resolution(n4, aspect);
        dyeW = GpuFluidSimulation.quantize((int)(l3 >> 32), 16, n3);
        dyeH = GpuFluidSimulation.quantize((int)l3, 16, n3);
        int n5 = Math.min(nArray[2], n4);
        long l4 = GpuFluidSimulation.resolution(n5, aspect);
        bloomW = GpuFluidSimulation.quantize((int)(l4 >> 32), 8, n3);
        bloomH = GpuFluidSimulation.quantize((int)l4, 8, n3);
    }

    private static long resolution(int n2, float f2) {
        float f3 = f2 < 1.0f ? 1.0f / f2 : f2;
        int n3 = n2;
        int n4 = Math.round((float)n2 * f3);
        int n5 = f2 >= 1.0f ? n4 : n3;
        int n6 = f2 >= 1.0f ? n3 : n4;
        return (long)n5 << 32 | (long)n6 & 0xFFFFFFFFL;
    }

    private static int quantize(int n2, int n3, int n4) {
        int n5 = Math.max(n3, Math.round((float)n2 / (float)n3) * n3);
        return Math.min(n5, n4);
    }

    private static void adaptQuality(float f2) {
        if (!(f2 > 0.0f) || f2 > 0.5f) {
            return;
        }
        if ((avgFrameSeconds = avgFrameSeconds * 0.95f + f2 * 0.05f) > 0.033333335f) {
            ++slowFrames;
        } else if (slowFrames > 0) {
            --slowFrames;
        }
        if (slowFrames > 120 && GpuFluidSimulation.effectiveQuality() > 0) {
            ++qualityDrop;
            slowFrames = 0;
            avgFrameSeconds = 0.016666668f;
        }
    }

    private static boolean ensureOutput(int n2, int n3) {
        if (output != null && !output.closed() && output.width() == n2 && output.height() == n3) {
            return true;
        }
        GpuFluidSimulation.releaseOutput();
        output = GpuUiEffectPasses.allocatePersistentTarget(n2, n3);
        if (output == null || output.closed()) {
            output = null;
            return false;
        }
        if (!(output.texture() instanceof class_10868)) {
            GpuFluidSimulation.releaseOutput();
            GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u2419\ud0a3\uc24a\u1d62\u0e6e\u835d\u97e1\u1d6a\u0a44\ub436\u695e\u0913\ubc8b\ufea2\u170c\u73e5\u9e96\u2d1a\uf900\u5c53\u1275\u7ac9\ue31e\u50d5\u9642\u181b\u6989\u297e\uebe1\u8a08\u9337\u83bd\ubbbc\ua8c8\u970c\uc3d4\u7909\uc5a7\udf44\u8ade\ub69f\ud303\u85ca\u0e94\u5d8d\ub4d9\u6805\ucc4b\u85ef", 1711271046, 0x636CFAF2 ^ CryptoVault.mask()), null);
            return false;
        }
        GpuUiEffectPasses.clearTarget(output, 0);
        return true;
    }

    private static void releaseOutput() {
        if (outputFbo != 0) {
            try {
                GL33.glDeleteFramebuffers((int)outputFbo);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        outputFbo = 0;
        outputFboTexture = 0;
        GpuUiEffectPasses.releasePersistentTarget(output);
        output = null;
    }

    private static void ensureOutputFbo() {
        int n2 = ((class_10868)output.texture()).method_68427();
        if (outputFbo != 0 && outputFboTexture == n2) {
            return;
        }
        if (outputFbo != 0) {
            GL33.glDeleteFramebuffers((int)outputFbo);
        }
        outputFbo = GL33.glGenFramebuffers();
        outputFboTexture = n2;
        GL33.glBindFramebuffer((int)36160, (int)outputFbo);
        GL33.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n2, (int)0);
        int n3 = GL33.glCheckFramebufferStatus((int)36160);
        if (n3 != 36053) {
            throw new IllegalStateException(GpuFluidSimulation.ni("\ub269\u2548\u53c2\ufce3\u348a2\u7990\u4fca\ub855\uc48f\u040f\ua9ec\ue4e0\u1283\ubfe3\u873f\ue9a5\uf720\u67f2\uf290\u11a2\ub41a\u41ac\ufc04\u2fa9\u4e09\u6f53\ud456\u767e\ud81b\u4a84\ue476\u8dd8\ud860\uc387\u464c", -1150392011, 0x636CFAF2 ^ CryptoVault.mask()) + Integer.toHexString(n3) + ")");
        }
    }

    private static boolean ensureGl() {
        if (glReady) {
            return true;
        }
        GLCapabilities gLCapabilities = GL.getCapabilities();
        if (gLCapabilities == null || !gLCapabilities.OpenGL30) {
            GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u7b83\uf743\u9a68\u8c4a\u36e1\u66f7\uc53c\u119c\u7330\u24e4\u5685\u2fc0\u4b11\u2fe7\u85e2\u3e54\ufa37\u030e\ucd6a\uee27\u4662\u4096\uba05\u8b6f\ud4f3\u963d\ucea7\uf85f\u2d01\u487f\ua340\u77f9\ub898\uf95c\u8c96\u2d43\u9944\ua5a7\u95f5\u935d\u706b\u6dcc\uc2c0\uc9e1\u89b7\u7f4e\u3f87\u6a48\ub054\ue505\u32bc\ufcba\u4dfd\u3663\ua16es", 1808035322, 0x636CFAF2 ^ CryptoVault.mask()), null);
            return false;
        }
        samplerObjects = gLCapabilities.OpenGL33 || gLCapabilities.GL_ARB_sampler_objects;
        GL33.glBindBuffer((int)35052, (int)0);
        fmtRgba = GpuFluidSimulation.supportedFormat(34842, 6408, null);
        if (fmtRgba == null) {
            GpuFluidSimulation.fail(GpuFluidSimulation.ni("\u9ac8\u7047\u3374\uf8f2\u2cf9\u63a7\u82ac\u6e5d\u2051\u7ff2\u4a0d\u59fd\u43c0\u5e20\u7b5a\u939b\ub157\uc29c\u3edf\u730f\u9034\ubc2d\u91ac\u96c6\uf810\uedf6\u2fdb\u1c71\u6b4e\ubb27\ubb73\ud585\u7ea5\u76db\u6f28\ucda2\u33c8\u33fd\ub1d0\u2685\u821a\uf9f2\u455d\uc291\u42be\ub3f2\u41ae\u6edf\u9d8e\u65e4\ud9a0\u7389\ua1da\u2dde\u05d6\u18d7\uade3\ud7bf\u0c83\uea89\ua920\uf989\uee82\u7bb2\u0cbe\u2533\u12e6\u93fe\u5361\u7470\u16d3\u7830\uea45\u9a58\u8ebf\u14d5\u9d2c\ud523\u3c0c\u5bf6\u81ec", 1571446652, 0x636CFAF2 ^ CryptoVault.mask()), null);
            return false;
        }
        fmtRg = GpuFluidSimulation.supportedFormat(33327, 33319, fmtRgba);
        fmtR = GpuFluidSimulation.supportedFormat(33325, 6403, fmtRg);
        copyProgram = Program.create(GpuFluidSimulation.ni("\u6dd1\u9c47\u4aad\u8fbb\u33cf\u780c\u304b", -1952274136, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\uafab\u563e\ub5f0\u3419\ub74f\u0c91\u49b7\uf9ae\uf632\ub0b1\ubafc\u766c\ua248\u31fe\u23f2\u3b4f\u9dbb\u4b18\u8f5d\uc579\u16d5\u1d00\u1c89\u2803\u5523\ub93c\ufbc6\u766d\udd6b\u907c\ude03\uf40a\u6e09\u5f7b\uae6a\ue8dd\uaa61\u6a3a\u8c60\ucc2d\u0417\u1210\u4b7c\ua0e3\u70fe\u242f\ua820\u9f89\u875a\u137b\u672e\u288d\u8ace\ubf8f\u94c7\u6200\u7c4e\uaca7\u60c1\u95fe\u60db\u2ae4\u1a5e\u1e8e\u2840\udf4d\uade6\ud76d\u00db\ufb34\u124c\u4f69\u841a\u347a\u06b4\u0938\ua4e9\u609d\u8be4\u43f2\uf8eb\u9c78\u35a9\ud854\uabcd\u989d\uf2ea\u5127\u07e3\u5e37\u9e7b\u0593\u3884\uccc8\u8b29\ua8c3\uc0ac\u48f5\u2e5e\u813b\u5fa4\uf1c4\udef2\u7d0f\uc1a4\ub0b2\ua14c\u812a\u0952\u180a\u7c7f\uabd4\uc3df\u86ac\ud374\uf678\u2af6\u7681\u8605\u98a4\u07d6\ue203\udb1c\ubec6\ua3ea\u8d00\ua5a9\u36e2\u5db6\u459e\u6d36\u723f\u47c5\u2f23\uf1d0\ub20b\ufe3c\ua1bc\u0edb\uaa8a\ue8da\ub8e2\u2a31\ud3cc\u8ada\u7ce2\ue9cc\udc4f\u34e0\u1ca3\u0445\u1f83\ua7ce\u8ad5\uc651\ufee2\ua983\u178f\ub884\u7f03\u4be8\uc67f\u515c\u9a9c\uaae2\u4f70\uda68\uc327\u063f\uda7c\u5a16\u9517\ue049\uaebb\u0fc9\u5066\u629a\ud2eb\ubb8d\u1cea\u1a91\ub530\ubc1c", -641818059, 0x636CFAF2 ^ CryptoVault.mask()));
        clearProgram = Program.create(GpuFluidSimulation.ni("\u648f\ube43\ub346\u7f13\u4752\u0db99", -1655078080, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\ufd6f\u7045\u6922\u6faa\u8603\u77c9\u7e66\ua8c9\uf0f0\ub55c\u1baf\u05d6\udcc1\ua573\uce93\uf9ce\ub72a\u0705\ub2eb\u707d\u768b\u60fa\uad35\ue918\u2c28\u00d6\ue4a6\u2584\u3ccd\u10aa\uc216\u7e46\ud26f\u2ddc\ud1a7\u5613\u589e\ufb1f\u28fb\uae11\u0d8a\ufd5a\uc949\u8ef3\u951f\ud875\u4bcf\ub553\u6bf3\u03d2\u863c\ua8fe\ua21a\u7c65\u8c18\u0c7e\u0b62\ua397\u7254\u2827\ub6a0\u0b07\ue5de\u289e\u6100\uf5ea\u6926\u4080\u0f87\u1cb6\u6896\u1c33\u4b39\u9265\u2a85\ub58c\u16df\u0966\u04b6\u7030\uda45\u759b\uc14c\ua2a0\u42e5\uf1f0\ubf22\u08fd\u3264\u614b\u01af\ueb7f\u4fcc\u7017\uf4c6\u914d\u171b\ud4f2\ub161\u76f5\uc37f\u7272\u3ed4\u81f9\u1f02\udd11\u070a\ua25f\u94ea\uc54c\uaa34\u32f7\u1735\u53bd\ua531\u0568\ua027\uceba\u36ec\u77c9\u8a77\u74a5\u83ca\ude55\u9dd1\ue88a\ue73a\uc79e\u5e17\ud5ce\ub973\u3dc5\ue768\uf573\u9c96\ucf3f\ucc09\ub892\u0235\u6ec0\uf919\u8356\ua981\uaac0\ud949\u17b2\u0cd9\ud493\ua0cd\uff05\u4eda\u4635\ua23d\uf157\u0b2a\u47b9\uec60\u4dec\u68e1\u20e1\u3cff\u9245\u7760\u597b\u6d6f\u3a43\u3f2f\uf0dd\uc1df\uaeb0\u1481\u4891\ucb06\u43f4\uab1f\ub7e0\u57be\u143a\u2f10\u2c4d\u6fbc\u8f18\u9982\ue7cd\u8138\u3cef\ube15\u16a0\u1c5f\u5a4b\uebe0\u62f6\u0df4\u6403\ub4f7\u4172\ubfcd\ua4fd\ud7a9\uae4c\u9b90\u8714\ub573\u06ce\ud88c\u0201\u0812\u787c\u29ed\u7968", -1677285050, 0x636CFAF2 ^ CryptoVault.mask()));
        splatProgram = Program.create(GpuFluidSimulation.ni("\u77f2\u99cc\u5486\u0504\ube6f\u3150\uc806\u7116", 1651551771, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u8089\u9fdb\u1e11\u5dd8\u2ac0\u36b4\u3944\ud6e2\u9d2c\u1daa\u6e55\u4d5e\u0d13`\ua6ab\u9ad0\u67c7\ucbe4\uf5b0\uc3d8\u0944\u4152\u7e93\u6761\ub6a5\ud80c\u661e\u6cd1\ub12f\ude17\u2dbf\u209e\ua5a8\u0688\u4a06\u4832\u90b1\u355d\ue5e2\u6ef0\ub380\u8f81\u7be6\u3426\uddad\ue3a3\u3e45\ua2c0\u5796\u10c0\uf281\u596f\uf1ff\ue8ec\u2ee5\ucb73\uaec9\u5cef\ua517\u99c1\u9384\uf189\u5d9a\u657f\u9be8\u15b3\u18c5\uc4a1\udcc7\u82db\ucbd1\udfa9\u17b0\u7e2f\u3808\uc4c7\udffe\ufa9e\ua29b\u3362\ud915\u12ed\uaf7d\u8bc5\u3844\u3e18\u9d6a\u5b56\u8dd2\ub56b\u6bfd\u341c\u90f8\ucaf9\u1542\ueb94\u5f21\u143c\u2682\u2d40\ud303\u16c6\ub01d\u8ba3\ued4e\ueb1b\u0d59\uf5c6\u866f\uab3e\u3e61\u7597\u21e7\u7a53\ub70b\udb45\u4957\u8c4b\u5628\u196d\u83bb\uef82\u1281\ube8e\ua997\u125e\ue46d\u0340\ua1ce\uc786\uf6bf\u1b60\udc31\u8761\u2beb\u684c\u083b\udc46\u26d7\u8a07\u6061\ue16f\ued7d\u0775\ucf9b\u816b\u2279\u225d\u8974\u0bca\u2e5a\u6875\uf1ea\u39ad\u0d53\ua3ce\u3ffe\ucc9a\u7735\ua179\u4876\u851b\u9b69\u0818\u5901\uc2b2\ufea7\u101c\u4bfa\u83fc\ud07a\ud58f\uf2b9\u7834\u89a2\u315a\uc21a\u9311\ucc6f\uebb6\u0fd8\u9700\u760e\u6fdb\ue4d8\u43dd\u489a\u316b\u2796\u2997\ucf1a\ub7d3\ua8c6\ue314\ub662\uaed1\ueaa4\ubc52\u1f2f\u44dd\u9b14\u00b7\ue362\ufe9f\u91db\u83b3\u73f5\u7c34\ue662\uba58\ua7a7\u2138\u668d\u42b0\u05ae\u7871\u4e0a\u4d25\ud433\u5a9f\u8cdf\u9df9\ua3ec\ub1dc\uca2b\u2221\ucae1\u452f\ucc4d\u7057\udf1a\u5f43\ud776\u25dd\u8b19\ue755\uf107\u4e29\u9ff0\u9ef4\u85d3\ud277\u8f8c\ud484\u548e\u04e2\u7bac\ue9ce\ua8f9\ud050\ud1cc\u5b32\u7875\u8327\u6e1b\uc3a7\u6050\udbf8\ufd74\u6d15\u7a51\ufb28\uad0f\u5e80\u7e2b\ua66d\udb96\uf454\u7e54\ufc2f\u63c9\u3c88\u6234\u41bc\uf325\u8b75\ud855\u5a6d\ub4dd\ub3f7\u505b\u0298\u633d\u942f\uac63\u0802\u4859\u5c29\u68fc\u907f\u8672\u0c97\ud3f7\uf7c6\uf738\uaa74\u51e3\ueb8b\u395a\udcf9\u1a71\u21cb\ub19f\u35c9\ud864\u9dfb\u599d\ua503\ucd5b\ub4a4\uc80b\uda2b\uebbe\uea30\u1dd9\u9b4c\u81b0\u510e\u3ae6\u13f4\u4c7f\uf9b5\u9421\u9270\u4b81\u0c85\u32b6\u4142\uef54\u98b0\u6752\u01c9\ube4d\uc76d\u5869\u574b\u3377\uaef2\u5926\u6a02\u969a\u76d0\uebac\u6e51\u065c\uded1\u7295\ub55b\u582a\uee28\ued6f\u5d87\u665c\u5dcd\ub648\ube57\ub01f\u53f9\ubc7c\u46fe\u992f\u106c\u3031\u037b\u3dca\u2932\u9c9e\u1a28\u5b0f\uc9e5\ua504\u27f5\u89b4\uee06\ua3cf\u7bf9\u86bf\ued46\ue2d8\u1d63\ud186\ufbf4\ua7d7\u0dd9\ua6cb\u72e7\u5394\u6634\u9f0d\ucb3d\u6bb4\u16d3\uae60\u6a9d\u7507\ud32a\ub989\u8691\ud180\u0a62\uf06c\u7135\uf9b2\ucd63\ubfed\ueda4\u2fdd\u2939\u4a7b\uf7f2\u706b\ueee4\ue7df\u0684\uc6e8\u5f79\u7cd6\ub6f7\u760d\u6a3d\ucbf1", -737296132, 0x636CFAF2 ^ CryptoVault.mask()));
        advectionProgram = Program.create(GpuFluidSimulation.ni("\u8d03\uf26c\ud38c\u73d4\uf8be\ue2b4\u8229\ubf02\u2f6b\u926f\u304d", 1326976058, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u4c41\u86a9\u5521\u80c8\u84b4\u0530\u05c7\u094f\ue3af\u901c\ua103\uaf84\ueb18\u203a\u388f\u3f0b\u5997\ub913\ub3cf\u3b5a\uf59f\ube85\u1d60\u3943\u333d\u64f8\u8193\u5426\u44b8\uc6e9\u6ca6\uf0be\uaa46\u0781\ub4ac\u4304\u1ff5\u2fc6\u9481\u2ae1\uf0f8\uaa6d\u48ae\u427c\u0ee1\ud2f0\u83fd\u4738\uaf14\u0e4b\ud94f\u1cbe\u4d38\ua6d5\ub01e\u0628\uc41a\u8a89\u4ecb\u080a\u1129\u5d5a\u8588\u798b\uc338\ua794\u4cc5\uf912\u7a1f\u813c\u31c0\u4cce\ud824\u86b7\u78cb\u31c2\u6b9d\u87d8\u7925\ua22d\u7685\ua28b\u133e\u3591\ub67c\ua7e5\u84d9\ub12d\ud546\u2f1a\ud66a\uf6f2\ub5e0\uacc5\u96bf\u5a69\u3b71\u3582\uf121\uf4d9\u50e4\u4a85\uf523\u14d6\u191c\uda4d\ue6b3\u16c7\u8b92\u87b2\u7cc4\ub840\u0a9f\u7462\u12d1\u5428\u52f3\u350e\uf58e\u8baf\u9158\ubc1c\u7ebe\u8b4f\u393a\ub72c\uf43f\ue5f9\u83c7\u428b\uafbf\u4e25\u6236\u73b2\uf206\u92ec\u7ca2\ua6a8\u050a\u7b0c\uaddb\ue0cf\u3276\u1a63\uca8d\u3f17\u2ce7\udc61\u7246\u5152\u375c\u0c09\u6ef1\u8975\u2ec3\ubb92\u82c4\u8077\u3632\u3614\uaed6\ufa0c\u5f1c\ub038\u9acd\u31d8\u5fca\uab87\u4eea\uee89\ud235\u968f\u7767\u5bb1\u9e1d\u6b0d\u9635\u4cd5\uec3d\u92b3\u77ce\ubc39\u7c2c\u5f2b\u80b5\u4bbf\u2d1c\ud94c\uec20\ub864\u9e6c\ucded\u3ac0\ufec4\u64c7\ud97c\u4889\ud6f4\u92ae\u627b\u0dd8\ua401\ubf4f\u373b\u2566\u85bf\ud872\u5e6a\uf84f\u1872\u16a1\u347d\u763e\ud1b7\ub133\uca61\u8f30\u9959\u1776\uba17\u3878\uced5\ue6ed\uf01f\uc39a\u395f\u3c9e\uda01\u6efb\ud02a\u33ef\uf93f\u1898\uc859\ude88\u881c\u3d86\ucc72\u0be0\uc9c2\u223f\u5766\u4dc8\ua082\u1334\u0ced\uf535\ue46f\u8c97\udfa6\ub0c3\uf8bb\uc35c\u4943\uf8cb\uc1b2\u563c\u10f8\u2491\u5ec9\ud069\ub5a6\u0c96\u77f2\uba23\u7c92\u66e7\u7e27\u055c\u0fec\ub576\u280b\ud419\u3a3d\u7071\u8521\udc75\u7683\u535b\u37b4\u30ef\u1576\ud810\u7990\ud484\uadb5\u4044\u49c4\uce3a\u0f0f\u7a43\ub6a4\ucb9e\uaf19\ua4ea\u6ac5\ufe81\u82f6\u8ff2\ue842\u9815\u907e\u4c0c\u9f89\u4395\u2762\u50be\u287d\u2f10\u69d6\uc3d5\u4447\u394d\uebb6\u0002\u8b42\u6faa\uc852\u22d8\u9d7d\u1865\u1677\u585b\ua794\uac39\uab69\u0b42\uc517\u3b43\ub233\u65a3\ucc47\u32af\ud5e6\u1ca9\u649d\ufd50\u2791\u03b0\uddb9\u5956\u3fff\u0560\uab58\ue575\u0514\u35c5\u1691\u05d6\u3361\u5eb3\u4202\u6659\ud527\u20cb\u3062\u9cb7\ua524\uabc2\u5b4d\ud577\u6956\u62f9\u14fe\uca84\uc31f\uc76f\ua8c6\u46d3\ua90c\u687e\u07b9\uc48e\ue144\u4d42\uf6ee\uee49\u747e\ud8d3\ucf70\u2212\ub360\u6084\u257a\uf7d1\u3cd7\u5b00\u0a29\u480b\u3e38\udf33\u4f5e\u7d1d\u9788\u184d\u09c9\u5710\u54ec\u0693\uddb0\u1891\udb9f\u83ae\udecd\ucba3\u2645\u4b2a\ubb58\ud06b\u5064\ub25f\u0a83\u1b06\u6680\u14f5\u80cd\uf52e\uc2c6\u9a1e\ubd87\u94d4\u050c\u21ff\ucb2f\u184d\u0551", -107449937, 0x636CFAF2 ^ CryptoVault.mask()));
        divergenceProgram = Program.create(GpuFluidSimulation.ni("\u6240\u4467\uf5fe\u1dbf\u81a5\u3d04\ufd79\u0bf3\u66b0\udb30\u4d36\u942a\u8cf9", 1619604878, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u41c2\u57a2\u3354\u0b29\u61da\ucc9b\u4785\u6661\u86fa\u80d2\u2661\ue91c\u6fce\u2f7a\u5912\u773a\u0bb2\u074f\uc09e\ua79d\u3eb9\ue1e2\ucc37\u0f5e\u9e93\u0414\u8b63\ue907\uf695\u0bc8\uf12f\u8605\uc259\ue2e5\ud914\u85fb\u8b95\ufcfd\u8283\u7c63\u4f2c\u47f5\uc2ba\ue669\u7832\ue97c\u2e22\u56f3\u2bcd\u7d5d\u0695\ue953\u75fc\u3e2e\u2c26\u11df\u7bad\u0840\u7d86\u1a40\u4196\ud95e\u7483\ubf7b\u8468\u5022\ud16b\uf5a2\u4996\u91a6\u00d6\u184d\u7992\uf3c6\u26a4\uf8bd\u2988\u097d\u42fa\uddb5\udeff\ue9be\u6bb1\u1034\u74c8\u3022\ue8c8\u6175\uef6d\u25d5\u11df\ud58a\u12b9\uc531\u07d4\ub8c8\u9a6d\u32c3\ufa3c\uc4c3\u8f8f\ufdf1\u9ff9\u8558\u9bb9\u4ed1\u82f8\ue70b\ub2ee\ucb52\u3a6f\u0991\u5aea\uf741\ub09f\uea35\u9cf8\u44bd\u4860\u0627\uf3a9\ue46e\u791b\uc9fb\ua930\ub620\u0a08\ucdc3\ue12d\u1e8f\u72d6\uf79a\u4c1c\uf357\u7258\u6325\ua3ae\u5ad5\ue0ba\uaa8a\u6ec6\ubf7d\u6cbd\u3f73\ua55e\u54eb\ub354\u799b\u7fc4\u10d1\uf0df\u043e\u1048\u3f87\u2ca7\u77d3\uf2c2\u9d34\u4361\u5324\u3210\u67b6\ueaba\ufbaa\uce27\ucb20\u98ae\u6353\ucce7\u765e\ub75f\u6e4f\u93cb\uf5e2\u5a20\u896c\u9297\u7796\ueb56\u8f82\ue01c\u2a4b\u203f\u1da2\uf4ac\u614d\u9789\u079d\u6002\ub7d5\ue962\u849a\ufa6d\ueb7a\u518f\u2127\ue147\u873d\u4800\u35f1\u256f\u47d0\u82c1\u907c\u304f\u5d18\u896e\u160d\ub299\u9df5\ue432\u5d55\u4b0e\u1142\u855a\ud119\uc982\ufa9b\u9376\uc86a\u00cc\u7bfd\uf8e8\ub54d\udcb8\u898c\u3ee6\u2f0b\ue7ed\ue545\u706f\u4fc4\uad0e\u2199\ua943\ue500\uf83e\ue1f9\uef01\uef03\uf8de\u0db4\ubd86\u5650\u333b\u7253\u06d8\u5f76\ua1fa\u999f\u9b92\u821d\uf163\ubef3\u3e20\u8398\u66d5\u9945\ub556\u6422\u109f\u601c\u3f81\ucce9\u533f\u3de0\u6f14\u444e\u5ef4\u2678\u3fc2\u857f\u9f68\u5e8c\uc458\u87ac\ue94b\uf5ac\ub579\u62c3\u66ad\u8f3f\u4851\uef57\u2e60\u92bf\ubc2e\uc85e\ucfa2\u3758\ueef6\u16f7\u0cef\u575a\u9ca1\u57dd\ud514\u1e2b\uaea9\u63de\u423d\u88bb\udfff\u98a7\u11e9\u3224\u4917\u123f\u8585\u64f7\u7183\ua3ce\ue06d\u310b\u1fb9\u194c\u1bac\u1378\ue891\u64e5\u27f1\u6778\u1060\ud644\udd85\ucbbb\u3b83\u8ef8\u6f1a\ue68e\u9495\u5e15\u8c5c\u26e9\u4cdd\u4612\u4a47\ud019\ua6b4\uf62f\u577a\udef7\u5d25\u7c5f\u0b59\u3f5a\u23bf\ufb46\ue34c\ucba2\u4516\u143c\ud7c6\u0a3a\u8b75\u3a64\u52f4\uc308\u04fe\u2cce\ubb0b\ue2e1\u4904\ua9aa\u6d7b\u1d61\u6b30\u3484\ufae9\u4bfd\u5f61\u90fb\u8373\ud477\u158e\u4266\u6886\u0a5a\ub538\u88df\u57ee\ud9b6\ub719\u21bc\u5070\u0671\u3a8b\ua808\u6903\u79a9\u6282\udb1b\u77b8\u9e8a\u0b0e\u982a\ua144\u169f\ufbdd\ucc0a\u7c51\ud43c\ubbfe\ub063\u3205\ufe76\uc7d3\u99be\u9853\u2717\ue83a\u1fba\u06d4\u4378\ue78c\uf805\uaad9\ua71d\ubd78\u5688\uee92\ub6cf\u4e4a\u8049\uefa0\u1fd1\u86ce\ufe12\u31aa\u0c5d\u1fe5\u9932\u91e1\u4d67\u8515\ub982\ufba3\ua8c0\u5059\u84e2\uda2a\u713a\ucfca\ue1c6\uab9b\u0e28\u2f1e\u0781\u6f69\u2346\ucca1\u4263\uaf4d\u096f\uc477\ube3f\ue569\u1cf3\uf9fa\u97ad\u569c\uee6c\u88d7\u1248\u69b1\u7cd5\u623e\u0f20\ua0a7\u4f3b\u6e7d\u9bc5\u7903\ud415\u41ee\ub99b\u08a8\u8f3d\u8751\u0727\u146a\u838c\u6a98\uc469\ud2d8\u3388\ub63c\u99fb\u5d64\ue0ab\u82c3\u526e\u7cdb\udfa6\u5a7f\u9ef1\u1bb0\ub5d2\ue5e0\ubd9c\u20ae\ubadb\u98bd\u8502\u5624\u7cfb\ud7a6\u0190\u7be2\u4f6a\u009e\ud336\uce62\u3580\u9e3d\uc4c3\ua97c\u78c0\u37bb\u412d\ua390\u9e3d\u0b52\u7019\u9f31\u655b\u8527\u4971\ua932\u30dd\uc219\uf7ad\ubfa3\u8f96\u4365\u772a\ua112\u46e8\ua8e1\u4895\u7706\u873f\u29bc\ucca1\u8225\u908e\u90e5\ue624\ua3ba\u653c\u5c60\ufe73\u2c78\u6693\u6f89\u2fb0\ua03d\u9366\ued93\ud36d\u6647\u257b\u19a7\uc95f\ua328\u7620", 1065513456, 0x636CFAF2 ^ CryptoVault.mask()));
        curlProgram = Program.create(GpuFluidSimulation.ni("\ufac0\u20d6\ub554\u1f23\uf85b\u14b3\u4532\u0c83", 1056927592, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u8565\ue6e5\u8ed6\u1433\ucefe\u64df\u9015\u851c\ua308\u3ecb\u7d1c\u0697\ue40f\u0e52\u6c78\ud2f3\udf5e\udfc6\ufd1c\uebd7\uda75\u5ace\u581c\u55b2\u4a1f\u1783\ua1a6\u3187\ue86a\u5572\u900e\u304a\u4374\u14e6\u5fa0\u74ae\udbf9\ub8b0\uba2e\uc6f1\u9b39\u15e3\ube1c\u4a09\u7ee5\u67f2\u0984\u9ac1\u860b\u21e8\uee82\ufe3b\u2f06\udbe5\ub3f3\u6c82\u5544\u6e2e\uab0d\uef13\u482c\u8127\u4526\u6fc6\uef83\u98ec\u7eec\uc368\u2029\u704f\u1fcf\ud46a\uc883\uea07\u1c5a\ub11a\u27fe\ua580\u1e3f\u9d43\u00fc\uddbc\u64fe\u0afe\u3328\u328a\u71b8\u632a\ue8b3\u9548\uc3a6\ud4cd\u871c\u83ea\u1b7c\u3f29\ud48b\u86c4\u8787\u356e\uc5dd\u9ae6\u9bd5\u4456\ucdd3\u86ca\ua553\u7ea5\uc36a\u4f6b\ub1e9\u450b\ud17b\u6dba\u2db3\ub02c\uc66e\u6a08\uc278\u8631\uc11a\uf99f\u9123\ua59b\u2cc9\u2b0a\uf267\ud8c4\uf892\udb63\ue8ce\ua5b1\u233b\u27b4\u6cc6\u1c82\uec6c\u6ea3\u620e\u4b36\ub028\ud46a\u95fb\uc0a0\u8222\u893b\udf2c\u49af\ub464\u1e09\uaac0\u3797\u628a\u2d18\u0fba\u64be\ue66e\uf408\u480b\ua8ff\uf73d\u4322\u1202\u7144\u8422\u9ea3\ub8a7\u2765\u1aae\udafd\u466e\u5f14\ubbe9\u16d6\ucc48\udc23\u5333\u62a5\u04ba\ue9ba\u5857\u083d\u0741\u342c\ud548\u80cc\udd8a\ua919\ub9c6\u467c\u138a\uce54\u37c7\u0fb1\ub45f\ua0ba\u018f\u88e7\ub724\ue3be\u0f58\u232d\u3791\ud9ec\u4226\u6ae8\u2c07\uc1e5\u1ef3\u737a\u8b80\u98ff\uc150\uec93\u328c\u166e\ua483\u6309\u402a\uba54\ud747\ua002\u89fc\ua06f\u31af\u0662\ued58\u86b2\u768b\uc45e\u64ae\u539b\u47f4\u2934\u1747\u5b4b\u3989\u3838\u1252\u7634\uf3fa\u35b8\u05d5\u5db8\u79ed\u7e90\u6862\ud3f7\udd32\u43bd\udb57\uc4ab\uaa5d\uca3a\u2a76\u61e7\u1d28\u7e4d\uaba8\u7f4a\u6c3f\ue74e\ua8f1\u471c\ud6ef\u8cf7\uc845\ucc0a\u630e\u95cb\u3cf3\uc23c\u4129\u947a\ubc72\uf11e\u8f42\uc082\u6452\u1f9b\ucb5a\ue09e\u6add\u77a0\u69e4\u664f\u239a\u3d87\udc54\u5081\u4511\u1023\u8989\u63ac\u1a37\u7438\ua462\u904f\ua87d\u7284\u5088\u7546\ucb70\ua929\u5914\ubc74\u886c\u2edc\u218b\u094b\u6a98\u8d4d\udda7\u49cf\ue23f\u76f8\u463b\ub759\ue198\u92c3\u2788\uf6fb\u6936\ua67f\u96b1\u96e0\u5ff4\u574c\u0d02\u0c9d\u0d67\u71f7\ud3d7\u0eeb\u1e74\ueecb\uf4b5\u6a41\u79eb\u65c4\ucd45\ud203\u474c\u81f3\ub36c\uceb6\u0b55\u279f\ud8e8\u534f\u3fb0\ub133\u1f3e\uedd7\u795d\ub3ae\ud2af\u4edf\uf8dd\u79ec\u5334\ued5e\u6e61\uacf6\uc9b8\u120a\u5877\u0a6b\u0c9f\uebc6\u14c7\uec02\u8ba6\ubb34\u1747\ub8ce\u6c96\uec48\ua37c\uf234\ufaf6\ub69c\ueafa\uafee\uba01\ua085\uf47c\u588c\u62f6\uf755\ueffc\u6827\u6d55\u8537\u0717\u4200\u4f0c\ub6d5\uc4a3", -40085078, 0x636CFAF2 ^ CryptoVault.mask()));
        vorticityProgram = Program.create(GpuFluidSimulation.ni("\ue848\u34e2\u7984\ue30a\u6dc3\u18b6\ued35\u9078\u497a\u8d4d", 2031988742, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\ue885\u162a\u7e89\ue6d7\u3267\u5a56\u41be\u1e4c\ub15e\u4794\u046d\uf45a\u8382\u74c6\uaf22\uadea\ub50f\u1b1a\u3dc8\u2ddf\u0f70\u72a4\u3956\ua218\uf1ba\u88dd\ub40b\u810a\uf53f\u9902\uf403\uad20\u285d\u3d0a\u5567\u57e4\u09fb\ua0ef\u56dc\u39d0\u39d3\uffce\u94ca\u0704\u25c2\u3b19\u7f30\u2266\u61e9\u72f3\uca39\u9f2f\uea55\uc57c\u382a\u03c0\u54c3\u7090\ue7a3\u1e54\u8d8d\ube7d\u365e\u3dc5\ufc56\u471e\u8dd7\u4465\uc4ff\uc620\u8ecf\u848b\u56c5\udb01\ub0d0\u4dde\u9f8f\u3662\ubdc6\ud327\uf430\u1cd5\ue64c\u3f27\u8b20\uf97b\ub974\uc2d8\uaf5f\ue22b\u98a1\u2eaf\u189b\u598a\u432e\u26b1\u979d\u3d80\u64aa\u6fd8\u86e5\u3327\ua45a\u7f9e\u1889\u4b50\ub08a\u1f91\u7d8a\ua292\u389b\u5d86\u9826\u82f5\u6995\uc0c2\u1c68\uadce\u1696\u1b69\ua076\uf621\u3e91\uc3b6\u0e11\u3550\uc780\ua299\ua71e\u5804\u5f0e\u7159\u795a\u4f2d\u65ad\u9727\uf869\u45a6\u55f8\u9c87\uacf3\u295e\uef28\u764a\udc5c\u81f5\udad4\uce9a\ucc65\ucd08\ub1bf\ubef8\u3bd0\uf386\u4c1b\u05e6\u21f0\ua8cb\u90a2\u4d64\u11a3\u1592\u3e4a\u233d\u2e9d\ue114\u9f52\u37bb\u5b45\uc3de\uccf6\ue1c6\ube39\ud768\u600d\uf4cb\u7b81\ub9b7\ubc91\ue19f\ud5c0\u0159\u2de0\uec3c\u7a50\u1fae\ufc72\u91df\u44f8\udbbb\u86b7\uc250\u8a23\uf3bd\u4ae3\u6c88\u4270\uf3da\u0be2\u35ed\ua625\u90e3\u8a4f\u1265\u0803\uae42\u3266\u577e\u6fd3\u198d\ufd45\uf688\uff76\u264a\ubad6\u904e\u623c\u1384\uce5e\u968c\u0a17\uee2e\u232d\u6eb2\u5b43\u8933\udeac\ua263\u924d\ub5d6\uaece\u1917\u191a\ucb6a\u851b\u6727\u6f3d\u509b\ua523\uff90\u94f4\u885b\u03fc\u1263\ud7f6\uf3c4\u126d\u39df\u7bb3\uee1d\u8fd9\u3c93\u2ffd\u48ba\u0391\u0fd8\udb38\u76f1\uc83d\u10dc\u6378\u93e3\u4cb9\u3590\u2da8\u4e6c\u008e\ua3d2\u7d22\u5532\uca84\u32ae\u1fdb\u7a51\u2a2e\u3dce\ufe73\u80ce\ua81b\u2d50\u6ed1\u26a6\ufb1e\udb01\ufa91\u9ad2\u7b0c\uc36d\ub44d\u4c2c\uee45\u1653\u3540\u4d3f\u1ff8\ufd36\u67d0\u0ca9\u8765\u5d70\u9909\u03de\u3adb\u04bf\u438e\u3c19\u366b\uaa4a\u7c04\u21c8\ua3b9\ue278\u52b6\u83b7\u7a43\uaa04\u4cc3\udbb7\u0ae5\ua526\uc3ce\u4fb2\uf3fd\ub0cd\u2836\u98a2\uebe2\uc9ca\uc732\u1cb5\u5853\u9d76\ue3f8\uc5c4\u59c9\uab0a\u39ce\u8fbc\uf242\u5122\u26f2\u2274\uf80d\u02b0\u9687\ubee7\u19f0\u2a8a\u1b74\u2a32\ub946\u146c\ueaca\uefdd\u04fc\u5576\u588d\u5e4e\u451e\uc927\u68c5\uf486\u1308\u52a3\ucd21\ue5bf\u619d\u53d1\u54ed\u0413\u876d\u7b35\ud8b9\u4593\ub1bb\ub039\ua1a7\u2c29\ud536\u3434\u5f2a\u0757\ufef0\u0625\u6c21\ua4c1\u8fe1\u791f\u58d0\u48a7\uf07d\uf6ad\uf1cc\uc4a7\u7159\ufd19\u7436\u7fd6\ua37c\u7d51\u3559\ua4d4\ucd7c\u8960\uad4a\ue938\uec52\udd26\u41a6\ua5bb\uc47e\u6288\u4789\u4b20\u7667\u57ea\u8da4\u1742\uecc4\u2dd9\u228b\u46a7\u753e\u9624\u6772\u0ea0\u621a\uf16c\u5ef7\u8a5d\uf9d5\u16e2\u611e\uad5c\u87b9\u2b55\u603f\u980a\u5d29\u4027\ucb09\u2520\u8046\u89c1\u80db\ube61\u1d14\u48f6\u3d20\ud945\u7c95\u8b62\u6679\ub977\ub71e\u9c3e\u7807\ue412\u3642\ub174\u8152\uf843\u7cbf\ud069\u83b4\u0f70\ua4d5\u98f8\u4cf4\u5b8d\u7be9\u87e2\u1826\u816b\u15ce\u14c3\ua8cb\u78b1\u5f5e\u5285\u4563\uaa53\ucf30\udb44\ue6c7\uef32\u13cd\u7246\u4153\ucb68\u601f\u7c34\uf851\u096b\u4135\u5a3e\ueef8\u7efe\uc1d7\u5361\u2280\u67d2\u9c10\ubfc2\u6b6f\uad1a\u0f5c\ude01\u5339\u44fd\u25ac\u458a\u988a\u93de\u1a14\u0c6c\uaa34\ud72f\u3aab\u4ada\u18d8\ub75c\u9bae\u1f32\u68e6\u9f43\ufc73\u4302\u402d\u26eb\u3f17\uf9cb\ufb1a\ua97f\u426b\u53b6\udc75\u0407\ud5d7\u9446\ua0f0\uffae\u0430\ua534\u2926\u3faf\u7560\udf98\u6c1e\ua620\u433c\u5749\u18d4\u553a\uc85a\u49a4\u035f\u6458\u7bad\u975e\ud8d1\u05eb\u5d3e\ucbef\u1c75\ud554\ud122\u06ba\uf62f\u4e64\u396c\u8527\u315c\u3f69\ucf42\u13d3\ub082\u060f\ua460\u4995\u3326\ubb47\uaba3\u8701\u2cc9\u0d33\ud348\u9542\ud8cf\ucbf4\u3844\u12ba\ueef5\ud86e\ue4d0\u4dff\uaa2d\u8099\uf81c\uc603\uc9a2\u8464\u2ee9\u3661\ue153\u7aea\ue345\u807c\uf2e8\u8d29\u02e0\u4c95\u60fd\uc96a\u1f92\u60dc\ufc90\ude64\u0cfb\u330b\ud417\ua319\u5c6f\u284d\ub7a9\uc275\u21cb\u0e8f\u7805\u6072\u84f1\u5141\uc057\u5baf\udf01\u12e7\ubf0c\u156a\u304c\u7404\u0c47\ub1e7\ub470\ub45f\u8f11\u1d39\u3112\u8794\u0695\u90be\uc4ce\u0c2e\u3809\u0e7f\u0194\ua29f\u554e\u4458\u85fb\u21c4\u1dc5\u0551\u0243\u7434\ue6b9\u1570\uefa7\ub66d\ued6e\u57da\u5b89\u722f\u089f\uf3d8\u2a19\ufca9\u6ad5\u4911\u5aff\ue2c4\uc93d\u277f\u6fde\uf190\uc1d9\u9194\ua8f3\u2d02\u128c\u3e6d\u806f\uc2ba\u08bc\u2696\u5775\ua08f\u49c9\uc584\u064d\u0e16\u6eb4\u01fc\u29c4", 1098552494, 0x636CFAF2 ^ CryptoVault.mask()));
        pressureProgram = Program.create(GpuFluidSimulation.ni("\u29f4\u8b5e\u9a71\u5a24\ua8c5\u98f8\u6e85\ue058\u0983", -1943197921, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u5e18\ud2cd\u31ab\ua33f\u6b23\u4b2b\ub5e2\uf725\u88c3\ua659\ua347\u6631\u6545\ua7cc\u2e0d\u20e7\u9614\u484a\uef6e\u11a1\ubfb9\uc5e4\uc5e9\u4884\u44d9\u8294\u3fa8\u59a6\ua603\uc929\u6efa\ua86b\u745c\u62c4\ub742\ue8f4\u501d\u48de\ub099\ufe78\u3726\uaded\u5fe7\u8eb3\u0139\uc25e\u1ce2\u8701\u5323\u2f4c\u4baf\ud59f\u27b5\u10e3\ue543\uaedb\uda17\u9f03\ue0ae\u2779\ubc3a\u840b\ube28\u273b\u7cdc\ud174\ue783\u73f1\u4f2f\u13af\u50bb\uf296\ub685\u546a\u8ae4\u7ebf\u1f44\u01c0\ua74d\u3cc7\ufd19\uf96c\ub6eb\u8384\ueb94\uefe1\u72c5\u808b\ub1ce\uefa1\ud281\ud6f6\u554a\ub42a\u2ab5\u7eab\u9e7d\u2989\ucfb2\uf8e7\ud21a\u83f2\u7bd1\u84e6\ued3b\u9362\uc76e\u5363\u66d6\u3ba7\u8cdb\u4200\ud539\ua409\u84d8\u69bc\u7e44\u6226\ub4f7\u3ab5\u8a68\u1e85\ud4e0\u3ef2\u9321\ubbdd\u110f\u9bc0\u7adf\u068f\ua39d\u79c8\u368b\uabff\ua93a\u3fb3\u6bf8\u09e7\uc6b7\u45b5\uc0c1\uaf9d\u42c5\uf93b\u8014\u17d1\ub315\u2b54\u685c\u3a63\u534a\u956d\u4945\u7a0b\udb5e\u268e\u65ae\ue92e\u93af\u9b09\ua52d\uc94d\u3741\u69cd\u9cb8\ue5b5\uebf4\ufe5d\u630b\u41f9\ue9c2\uc6f2\u5765\u98ac\u9fd5\u1c57\ufbde\u8c4a\u0604\u8e7e\ua206\u7e36\ud1e9\u728b\ubb49\u4473\u4cdf\u5d5f\ud422\udc7c\u7b31\ua5ff\uccd2\u39ae\u0b2a\u3383\u585c\u94e1\u253f\ucd20\ud525\u4946\ufad0\u8390\u8c54\u6368\ueedd\u7219\u948a\uec13\u2098\u71b3\uc72c\ue8f1\ube14\u6562\ue513\ua5fb\ua2f9\u38b3\u4765\u9f8b\u2333\ub602\uc60b\uc389\u7845\u180c\u7cf9\u9de0\u9c59\u3701\u1286\u70b5\u276e\u7b8a\ue24c\u2096\uebc9\u6b9a\ue395\ud464\uc677\u438f\u5fff\ufc3a\u40e3\u0ab9\u8465\udf81\ub273\ud410\ud210\ubab1\u0e43\u2245\u48bc\u184a\ue80e\u3b67\u82e1\u8f79\ua0a2\u3680\ueb59\u178c\uf458\ub431\ub09e\ucfa4\u5318\u76bf\uc14d\uc762\u8526\ubfbf\u50ce\u2ffb\u0354\u5cbc\u7c65\u0423\ua64c\u4f17\udf1e\uee63\ue334\ufe77\ub4df\u477e\ud03b\u642b\ub12f\u9545\u52fb\u13df\u3bb6\u8711\uaee9\u2e30\u1100\ue35d\u6bce\u6830\u079d\u3a86\u2274\u2e93\u0f98\udccf\ue7a4\u1abb\u4d7f\u1cbc\u35f5\ue8fd\u4144\u448c\ub695\ua078\u9a9a\uf84e\u2732\uc9bc\ue8ed\u62ff\udf49\u541a\u88b7\uda6b\u8c75\uc14c\u4201\u1ac8\u2ab0\uc859\ue352\u1f35\ucc17\u1500\u0b0d\u0776\u90fa\u2b62\u5c5a\u2407\u6c78\ubb4c\u8c34\u6c2b\ue867\u06c6\u31ac\u349e\u7154\u7487\uc8b4\uc2b9\u9867\ue5c0\ua039\u6d03\ufe49\u6644\ud24e\u40f1\u8c03\u653d\ua048\uc947\u6811\u9e23\ud3d7\uad41\uec0e\ub6cd\u300d\ueb34\u05fb\uf346\u0ad4\u5fa2\ua2d6\u056e\uae08\u01f0\u3023\uc40b\uc94d\u9449\u7e76\ua095\u2838\uc190\u6d1f\ua59a\uefe3\u1c35\u0536\ucab6\u0e96\ufe4e\u9c14\udf86\u5058\ud1c1\u22cc\u5513\ud664\u6784\uc7cb\ua2d3\u9bb4\u2481\ud1fb\u9e5a\u08b6\ue0cd\u086e\ua67d\uc36e\u2974\u507d\ubde8\u5807\u66ed\u30d1\ucabf\u670a\uaf9b\uf036\u6420\ue85c\uf97c\u01f8\u160b\u60ee\u1f9f\u6b1d\uef47\u19c1\u6d78\ue1e6\ud1e9\ub55c\ub911\u9610\u82c9\u3748\u7f75\u4724\u85e9\u23d3\u4d8a\u5016\u61b4\u7f37\uba42\u15ce\u70fd\u223e\ub397\u96f2\u59b8\ubaf4\u9174\uf992\ue538\ucee4\ub63b\udc7f\uc8c8\u1e04\u3721\ua381\ue7c5\u4525\u245f\ubc3d\u4e43\u809b\u7bf7\uceca\ubf01\u62c9\u6c18\u6cbc\ue1db\u38a0\u910c\u910c\ue053\ubfb7", -640148568, 0x636CFAF2 ^ CryptoVault.mask()));
        gradientProgram = Program.create(GpuFluidSimulation.ni("\u2d3d\u5c1a\u506e\uc049\u8eca\u8752\ue4ff\u45f2\u6d3a", -2114096431, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\uae53\ubbbd\u29e8\u0a5a\u58cd\u9284\uc62d\u5091\u14d2\u1989\uea18\ua175\ua60d\u1a3b\uef89\uab66\u4339\u5c3f\u197a\u097a\ucf79\u0977\u300d\uec3d\uadb4\u2513\ue75a\u8045\uc306\u7a64\u37c9\u782f\u4ca7\u85d1\uf1f6\u47d1\uf333\ub26c\uc778\u6c92\u8920\u7ba9\u29d9\uf747\u0e93\u001e\u3aa3\ub899\ucc53\ue9d8\uc5da\u7155\u1033\udfc5\u214d\u7980\u0807\u60f1\u0ba9\uab5a\u67cd\u0cb3\ue79c\u4654\uf667\uaecc\u0f1a\uf4c9\u20bb\u4bfe\ucfd2\u930b\u18a0\u4084\u948a\u4a39\u2ef3\u7fd6\u26a8\u3137\ub99b\u67a5\u7a2d\u28a3\ue485\u996f\u4c2b\u3a95\uab4e\u4653\u2d75\ud799\u35fc\u7e9f\u3134\u7f34\u90eb\u83c3\ud186\u7988\ue5c5\u5592\u1762\u9b9f\uabca\udcc6\u3f75\u3ea8\u372c\u48a2\u49ab\u1bda\u8974\u5edc\u9882\ue286\u1d27\ub46d\u2556\uc836\u5246\ubf3a\ubc52\u16de\u0de1\uc9b6\uc0f6\ub7cd\ud8e6\u3c0d\u1017\uf069\u1156\u7ab6\uc8ed\u66ca\ua124\u6d01\u97cf\u3426\u9667\u8725\u23b6\u67e6\u5c43\u10e0\u0ed9\u3092\u15ae\u1538\u484b\ue662\u9e23\u537a\u4fb6\udc88\u1c91\ue721\u6fe8\ua0a4\u2a4f\ud1eb\ue6ef\u7a91\u15e1\u3e0a\u30d6\ub829\udd7c\u2c25\u81fc\udd7b\ua96a\ua24f\u8244\u4d31\u4a1f\uf76e\ud7f1\uf97b\u797c\u9e9a\u6064\uad4f\uabef\u41d8\ude9f\uad2d\ud461\u230e\u6460\u3ab4\u7083\ueeb8\ue29d\ucf92\u3c6b\u0313\u9bcb\ub70b\ue0fd\u17ad\uf2f7\ud947\ue643\uabad\ud26f\u5ae1\uba9a\uddcf\u9025\u792f\ue825\ua398\uc303\ub929\u61ba\udff0\u5182\uac7c\u12c3\u7b00\uaf80\u8e7c\u4136\u8328\u4225\u1bf2\ue604\u5d1e\u71ac\u4bdf\u869a\uf79d\ua2a5\uddfa\u9ea4\u434b\u1329\u9c5d\ub81a\uc700\uf404\ub00a\u066d\uef49\u1996\u4837\ucb5d\ua595\ubb80\ub97d\ufd09\uf109\uc36c\ubf37\u5f61\u20b1\u77c7\u4aeb\u8c4e\uf575\u7528\u5089\u7f00\u70b1\u27f1\u691d\u8c71\ud5e1\ua0be\uf453\u9604\uf6df\ufc7b\u03e3\u606d\u97be\udbd9\u79d3\u12c6\uac8e\u76f8\uf90c\uf784\u2014\u7c7f\u3289\u9a3e\ucaa6\uc210\udd87\u36d5\u19f1\u2365\u339f\u0bb1\uc243\ue186\u9fbe\u7e85\u28ac\uf394\ub09b\u3000\ua46d\u6280\ucf71\ue307\u3b1f\u296b\u7367\u8dac\ueac6\u1933\u17f7\u390d\u701f\u7e10\uda87\u457f\ubd9e\ufa80\u277f\u99f6\ud78f(\u7519\ua5cc\ucef1\u43ca\uaf52\ua390\ueae6\u7e4f\u9502\u195d\ua438\ub6e3\ud6bb\uebba\u6f04\ud86f\u0114\ub2a4\u1b5c\u5945\u2144\ue927\ue12c\u88e4\u3f32\u6f51\uc25f>\u72c3\u689a\u7ebf\u01d2\u0577\uab48\ud895\u074e\ud6c7\u2c67\uc1e6\u077e\ub4ca\ua1d3\ud071\u6902\u3a77\u5d7e\ub70a\u4811\u6384\uca31\u3eb0\ud044\uca86\u6d00\uadd6\u6962\ubf61\u1986\ud81d\u3a48\uedf4\u735f\uea73\ua6a0\ude27\u323d\ua052\u33de\ue81c\u0495\ucd45\ue1bd\u55e8\uc2dc\ud182\u481c\udff6\ua493\u68dd\u90a1\u9bac\u40a9\ubb37\u8b3e\u3d95\ude1b\u33d1\u6580\udaad\u1738\uc0ab\udc85\u60ea\u2fc5\u98ae\u63a6\u0107\u496a\u0691\ucee5\u526f\u59e2\ud54b\u4703\u0e47\udc54\u1c4f\u5a03\u26d9\u59e8\u5770\u67cc\u65e7\uff48\u1480\u3190\u19e9\u04a1\u2ef5\uddb4\u1d1f\u2d51\u996e\uf611\u485c\ueece\u3b27\u286e\u2e3f\u2dee\u2045\ue4ce\ucecc\u87f9\udcb0\u8290\uc9d5\u312b", -816108296, 0x636CFAF2 ^ CryptoVault.mask()));
        bloomPrefilterProgram = Program.create(GpuFluidSimulation.ni("\u8702\u9917\uf13d\u05f8\u062e\u55eb\u75e5\u24a9\u8de5\u0938\u88b3\ua073\u00bc\u3f98\u41a0\ue494\uefe1e", 1732667641, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\ueb72\u5b49\u8501\ucf30\u8522\udf09\u2705\u8f9a\ucc89\u4708\u8cba\uac5b\ue95c\u2a8b\u4615\u3716\ua23b\u2400\u424a\u7d77\ub997\u5089\ue9f2\u3091\u7b7a\u1633\u00f6\u4cb4\u2b61\u3863\u1229\ufe94\u6958\uab4e\u4125\u8617\u7631\uf0cd\uea91\u1758\u2e78\u4636\ue559\u365d\u4392\ua941\ub955\u317f\u2c74\u1c32\u47ae\uead8\u4d21\uca16\u4ac6\u9607\ubb97\uf923\ud83d\ue205\u9666\u17d5\uaa2d\u2d24\u096b\u3c84\u8516\u09eb\u566a\uab6f\u63f8\uc641\u56fa\ub345\u0e81\u85b0\ueebf\u8bfe\u6583\ua6d1\u8e92\ubb03\uab12\u7e42\ucde4\ubaa5\u91e0\u0827\u7b2e\ucdf6\u705c\uab93\u70e0\u1677\uda55\u26a1\u66f9\ue48d\u86aa\ucb33\u9d62\u1829\uedf5\u4840\ue5f0\ub425\u9951\u72c4\u5726\u4d09\u55da\ua149\u9578\u80f8\u283d\u5f72\u2625\ufec8\udd75\uf866\uae2b\ucd5b\u15f2\u4e59\ua76a\u8542\ub027\u910e\u4852\u53b2\u5d52\uc2bc\u78e6\ua8fd\u5e57\u95b0\u6b17\u432b\ubac6\u9219\ua1a7\ucabd\uf441\u4fa3\u884f\u4d29\ubb68\ufb7c\u2937\ud21f\udfb5\u32ea\udbd5\u8364\u84ef\ufece\ua24d\uc4b8\u9d07\u193f\u891f\u8d6d\u6253\u7475\u0abb\u87ca\u00ab\ued34\u185b\ufc65\u04ea\u1caf\u7d05\u87da\u9535\u9d2d\ua84a\uc3a2\u884f\u0be9\u6fcd\ue29d\u1785\ue82d\ue1f3\uc322\u63d1\uf3eb\u3500\u1514\u2be5\uf997\ud329\ua6f8\u5c33\u4c17\u269e\u76e0\u9530\u153f\ua7ba\u7c44\u9b6e\u4965\u0ff8\u8464\u3fa1\u6eac\udacb\ue8c7\ue741\u0dd2\ud35c\u9cbc\u7c3e\u4cfc\u82c3\ua17a\ued16\ub7c1\uee8b\ue3bf\ua72e\uaf19\u321b\uce4e\u4c7f\u3dcf\u4484\u4045\uda18\u1079\u80e4\u8fa8\uc6de\u9d4a\u8e5d\u1239\ua975\u1d79\u799d\u8568\u1fdd\u80b5\u2f91\u1cde\ucf02\u0548\u3377\uad02\u0c0c\uc8f4\u6b9c\u8ba0\u0c35\ud0cf\ud580\u1ac3\u2a98\uab49\ue6ca\u05ad\u736c\u3692\u5e7c\u7701\u15ba\u7717\uca1f\u091f\u8603\uc6e9\uc567\ua158\u691f\ue6f3\u8fd9\ud56c\u328b\u7176\u4d9d\ub474\u0980\ub665\u16b4\ua98d\u0c76\ube8c\ud0af\ude47\u7f6f\u808d\ue230\u210f\u5503\u9a8f\u52cb\u5a87\u5616\u817f\ufee8\u9618\uac26\ud5ca\u81b4\u7234\uc81a\ue829\ufac2N\u430d\u5895\uf092\u5eaa\u8265\u9711\u4b6b\u15ef\ua410\u414f\u8448\u583c\u918d\u48af\u7213\uc019\uea13\u1d67\u0392\u15ff\u04ae\u90e4\u2d80\ud37e\ua5a5\u6261\ueba5\ufb63\ufb8e\ud9fc\u2b9b\ua094\u470e\u40d3\ufdeb\u607c\uadf0\uf804\u016b\u0aa6\u296d\uc717\ued6e\uaec0\uc232\uf26a\u90ff\u32b0\ue05d\u5c96\u3ecc\u3d71\u3c5f\u294b\u0d9b\u7e83\uf3aa\u6ef9\u9e96\uc45e\uc576\ua91c\ua986\u342b\u821e\u6b01\ud3d3\u0dd1\u46a7\u3d89\u1fb3\ub2b1\u464b\u9b0f\uc82d\ua3d7\u86c4\u901e\u9ae9\ue3b8\ueb40\u5998\u6e2e\u039e\ud928\u4507\u1ece\ufe6f\u7e55\uf5ed\uab3f\ue883\ucfa1\u54e0\u5210\u4d39\u278f\uf23f\uc6eb\u6231\u29e9\u4dc7\u77a5\u065d\u9e0a\uf2e4\ubaf5\u1bfe\ue3e8\u7891\ue351\u5d16\u9518\u87f2\u28bd\u76d1\u1285\u28df\u9845\u97ba\uf84c", -985575483, 0x636CFAF2 ^ CryptoVault.mask()));
        bloomBlurProgram = Program.create(GpuFluidSimulation.ni("\u66df\u759e\ueaef\ue569\uf2da\u331e\u46da\u2ff7\u3434\u0e3c\u24f8\u8426\u0155", -1251440162, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\u1473\u59da\u1284\uc8e3\ud116\ud13c\uf016\u0c19\u6d74\ub153\u1e1e\uc3a8\ud099\u0be6\u57c1\u25b2\ua3a9\u0cd7\u2ad4\u9fb6\u25aa\u3537\u0554\ub6ca\u7f5a\uff73\u207f\u14d5\ue133\u095f\u2a75\ufbb2\u97c5\u67e6\uca32\u49c6\u8091\ue811\u8164\u27fc\u4248\ubcb1\u84cb\u62c3\uf3fc\u6fe9\u33f5\u2911\u9dde\ubca8\u3b03\u9498\u2930\u1daf\uf67d\uab8e\ufb69\uef6c\ub42d\u93c9\uab9e\u938d\u7a70\ud3fa\ub27f\ufd24\ub89e\u24b8\u6fab\u3ed0\u5280\u599d\ub49b\u5327\u39b3\u860b\ua6bf\u5862\u7916\u5f32\u8e11\u68c4\u2e42\u4045\u39ac\ub8bf\u6f8f\ud945\uffd3\u84b3\u57fa\u74a4\ue437\u0e9b\u88f0\u5e4d\u8929\udf43\u438f\u12f3\u0197\ub08f\u7e1f\uc595\u2d31\ud7d8\u9f95\u8809\u3930\u3395\u6584\u3b93\u993e\u7402\ueef4\u1f2c\u9f27\ub142\uc562\u6e63\u2f3e\u249c\uc36d\ubb5c\ucb0b\u6ec7\u8af7\u63b4\u8c80\uc155\u04b2\u57ce\uabb8\u8b58\u8015\uc8eb\u55bd\u1603\u7c1d\u4f13\u8acb\u32ff\u2d61\uccfb\u531b\u3ecd\u95b2\u8f00\udf60\u5c4f\u7699\u6481\ucabc\u1558\ud3cf\uf8c1\u4eb4\u68c4\u9b19\u4153\ufd55\u2f96\u3175\u54f5\u2f2a\u67f3\u8896\ue03b\udf86\u5595\u7938\uf457\u31b8\ude5f\uc556\u3522\u83b9\udb02\u5b0e\u8d94\ue865\ufa55\u8464\u8142\u5097\u40bc\u8fe3\u0b10\ue6dd\ua5b5\u02ed\u374d\u6c09\u04d8\u9443\u5cd5\u208f\ub86d\u3853\uf51c\ud540\u225f\u693e\uddc3\ub1f2\ua8fa\u2d36\u7fd3\ud212\ud6de\u2724\u6c1e\udeaa\u0825\u930b\u156f\ufefe\u86d1\u8cc5\u2d9c\u04b2\ud193\u3b34\uf12d\u0a71\u3112\uf83b\u90a1\u0536\u7e26\ub806\u0f81\u4a13\uec72\u0b8a\u66c6\uda33\u84d6\uce04\ud775\uda84\u8d1b\u222a\ud7f2\ufc21\uc3c4\u03f1\ud632\u0c9d\ub195\u8043\ue495\u5566\u182d\ucc0f\u3530\u264d\u70bd\u5bad\u6c5d\u2007\u3d1e\u904a\ufef9\ub96b\u365a\ub3b2\u0fb1\u822f\u65fc\u0612\ub8e0\u7929\u35ee\u8374\u9bfc\u013a\u9e80\u1321\uf07b\u8272\u85fb\ubc26\ucbc8\u62b7\ucc58\uf8bf\ubb19\uab66\uaaa0\u7ba7\uff82\ub863\uef43\uf774\u21fd\ud46d\u3352\ub5f8\ucb40\u39f7\u2cd3\u33a1\ue43a\u93c8\udcb8\u07e0\u0160\ua31e\u286b\ue880\ufce9\uffd2\u77b4\ua61c\u13d6\u3608\u06f6\u7c21\u9677\ub4e4\u79b4\u397c\u6bff\u03f5\uc044\u792a\ufcd0\u22d3\u6d10\u3ad6\u18c3\u0b7b\uce47\ufbe7\u498a\u42fb\u3713\uf5dc\ub1c2\ubd4e\u13b5\u6311\uf1c0\ue293", 842716311, 0x636CFAF2 ^ CryptoVault.mask()));
        bloomFinalProgram = Program.create(GpuFluidSimulation.ni("\u43f4\u09b9\u6025\ua191\u75b0\u513a\ub6cb\u0983\u1c14\ub07b\uee03\ue5eb\ud379", -793303738, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\ua795\ue977\u20f4\u805b\u0e9b\u6e84\uc33e\u4f18\ub78a\uf0a4\ue0b8\ub202\ub3e4\uc566\uc5fc\ub625\u8567\uf32d\uef12\u6430\u54fd\u727d\ua1ba\u1589\ua747\u17bb\u4d11\ub739\u2faf\u3e14\uf716\u33b8\u33b1\u10fd\u5574\uaf0b\u50aa\u6ef0\u9a65\ufdfb\u4840\u15f7\uf618\u4fd3\u44f6\u2912\ufb48\u9cfd\u8fa2\u9cb0\u8e05\ucf3a\udafc\u9205\ub119\u6e1d\u6729\ub48a\ue7b8\u32ee\ue35b\ua621\u55a0\u1fc2\u36b7\u1ead\u5dfe\u79e8\u21a0\u263f\ud693\u2d24\u5829\u62a5\u4bb2\ua0cd\u27a5\ud2ff\u20ae\ucc6c\u00d9\uc86b\u74ee\u99d3\u4d73\ue0c7\uf303\u5f5a\u7642\ua5f5\u2154\u91d1\u25f9\u8e5f\u9790\ud763\u9e6f\ufe1b\u83ad\uc160\u27c6\u577d\u64dd\u4d39\u0ece\u298b\u6561\ue9c5\u4a4f\u259d\ucba9\ufab3\ue580\ucd10\u9de6\u11f2\u9c56\u5a25\u5b49\ub9ad\u08a0\u6641\ud6d7\u9074\ue9bf\u3918\uc96b\u5eb1\u9335\u3719\uf526\ucbba\udd6f\u2331\uf828\uece8\u7398\uc2c7\u8a1f\ucb07\ue31b\uf0f9\u7294\u0d87\u2020\u4eeb\ufef8\u711e\u1229\uda6c\ubfe4\ub069\u0aef\u3993\u3099\uac5c\ued99\u2c84\u6a5d\uc2bf\uec5c\u29a4\ud35f\u5d7d\u1da9\u32e4\uf400\u34ef\ub6b7\u6b14\ua313\uc271\u7a39\u582d\ub68b\udba8\u75d0\u1836\u455d\u43bd\ud3e8\u86b5\ub6c8\ue539\ufb03\u3a7a\uba28\u8fad\uef00\u4ade\ud984\uc228\u8c40\u9370\uca26\ua421\u136b\u5d1c\ue34d\u8151\ud8c8\u6b57\u6a00\ucb79\ueecd\u8d7a\ua33d\u0ab8\ue095\u46ec\u4423\u5c39\uc610\u691f\u8ada\u277e\u2c72\u785a\ubf22\u1357\u9039\u0f9a\u53db\u2c53\ubee7\uc165\u22bd\u9995\ubb1f\u8532\u9566\u9279\u6a39\udd1f\uefeb\u9224\udff5\u421a\u56ef\u59f2\ue0e5\u9d82\uf20c\u303a\ua7de\u8512\u1b11\udd74\u5a60\u1732\u242c\u9907\ub0b9\u254d\uc6f1\u6b15\u21d4\u2652\u0d87\ua221\uf9d3\u8d87\u4d34\u0d5c\u22d3\uf316\uebbd\u8206\uf224\u2ee0\u225e\u2b6b\ub965\ub407\u3768\uf5e7\ub1c8\u3780\u7af3\uf35a\u32a0\u6dc0\u3a26\u94a8\u5632\ud5df\uf072\ufdac\u1a8f\ucc3d\u1930\ub9dc\u9500\u9723\u0c66\uf0ac\u0c32\u6dd4\ue162\uf961\u2a5e\u96b9\u7b97\u1456\u63ff\ud51f\u334c\ue9c7\u5694\u3b95\ude55\uf81e\u5211\udca4\u668a\ud809\u67e6\u4748\udb43\uf13d\ue267\u6172\ue740\ufbc7\u9fff\uad04\uc142\ud7a3\u913e\uea1e\u775a\ue506\u24e6\ua052\ua096\uf5df\u0218\u460d\u009b\u075b\u2bad\u21ec\uc241\u1dd0\ucc5c\u5f88\u580f\u96cd\u9b58\u609f\u556d\ue180\u151b\u55b3\u497e\u25d2\u34e5\u52d4\u8a9e\u4145\u48a8\u5692\u3407\u800b\u7e77\u05b2\uf85e\uc317\u9335\ufe4e\uc4cd\uc8d0\uc795\uaedf\udba2\ua6c1\u83bc\uf195\ua5d3\ud79f\uef3b\uad74", 1959445331, 0x636CFAF2 ^ CryptoVault.mask()));
        displayProgram = Program.create(GpuFluidSimulation.ni("\u904e\u94b8\u9054\u679b\uc839\ubf45\u7f86\u89ad\u07dd", -787675340, 0x636CFAF2 ^ CryptoVault.mask()), GpuFluidSimulation.ni("\udf5c\ue0d7\u209d\u5513\udb00\u8b3b\u49e4\uc9e7\ucf33\u3503\u18bf\uf7bd\uc973\u34ae\u4998\ua09c\ufc9e\uf201\u2af6\u72b4\u20ba\udb2d\ud434\ufb28\ua20e\u9e67\u5415\u6ac9\u96d5\u6888\u79ee\u65f7\u97d0\ue67a\ud943\u178d\u61f5\ua29b\u5cc1\ueefe\ud15e\u5b55\u5327\uda04\u24ab\u1c94\u3b2b\u7dea\uf226\u7fa9\u997b\u276a\u1f0a\uf872\u932a\uddd2\uf54e\u71e9\u9579\ue686\ub3a7\u836d\u19ab\u55ad\u92fe\u710a\ucb27\uc286\u242a\u9791\u5b20\u114c\u4ec8\u6154\u7ebd\u7868\u8705\u7a4c\u171a\u719d\ufbab\uaedd\u76cb\ub47d\uba8d\uc20e\u1102\u488d\ue477\uc493\ud1ef\u0df5\udd24\ub70d\u3aa8\u8feb\u6725\ubf79\u2d81\u505e\u8b02\u1c96\u96ee\uc018\ue817\u8d7a\u7e24\u8c32\u2626\u86df\u5a83\u2dda\u78ac\u0eed\uac76\ub976\u8181\uf0fc\u994f\ud14b\u494c\u6e28\u8117\uac11\u5fab\u0a75\u13e5\u632a\uf863\ud933\u23c5\u6547\u044e\ue016\ud5ee\u89d8\u5cd3\u6513\u1e99\ub15b\ucd2d\ue761\u08b8\ubf69\u43c5\u6970\u378e\ud033\u8ab5\ufb38\uf76f\u7f0a\ue64c\u2121\ue55e\u298f\u0c9b\u9a52\u24a2\u06f0\u9c6d\u588e\u1bf7\ua0ae\ucefe\u915a\u58aa\u5ced\u1325\u2126\u4fb1\ub990\uffe4\u1019\uafe7\ueb3d\ub5fa\u2b25\u0685\u171c\ufb69\u0cff\u4722\uf150\u21a7\u62e8\uc8df\u5ce8\u02e6\uc45c\u37c4\uf31f\ue56f\u9f22\u4a37\u9622\u8cd0\ufc43\u5335\uba99\u82d6\u75cd\uc86b\u998d\u76c6\u24f3\u8312\u4fb8\u4798\ucdc3\ue2f8\u91c9\u91f8\u877d\ue314\u640d\u1778\u113b\u60c1\u113a\u1ac6\u6365\u62b5\u4696\u6044\u086a\u2aa1\u12fc\ue8e6\u4028\u76ef\uc7e8\ud0ca\uee36\u5168\uf0e0\ua69a\u4474\uf3d4\u349e\u0d21\u9486\ud9cb\u7ab8\u46ca\uf3de\u700a\u18d3\u3811\udcf8\uf764\u9bf5\ud0d9\ua0fb\u8d22\ubff7\u9ffe\u4535\u1330\ubcf2\u10b8\u498a\u1925\u93ef\u7d7e\u54c7\u3453\u3afa\uf909\u5a77\u95c6\ueca8\ub6e4\u7504\u3c38\ud9fa\u56e2\u1ea0\ud852\uf543\ua942\ua404\u5e80\u613d\u260a\u06bb\u4a33\u44e2\u17ab\u8d0e\ud510\uc00a\udadd\u160e\u5854\u5160\ub784\ude3d\u6986\u2d3c\u2005\ua7e0\u10a7\uc09b\u5760\u8f5c\ude74\uc52d\u7250\ub234\u6f1b\u4ee9\uf528\ub1b3\u64c6\u2f0f\ud4ea\u32e0\u0b4a\u1482\ue75f\u80b3\u51f3\u6a41\uc24c\u582c\u1c66\u79f5\ud152\u1691\u95d6\u61cf\u8f2f\u59bc\ub1a2\u9bc8\uef81\u6ed2\ue634\uf0dc\uadcd\ua5ff\ua4f9\ub00e\u7903\ub9f0\uc07a\u71f1\ucce4\ud381\u686d\u7838\ud6bd\u738f\ub989\u3a0b\u8f55\u9323\u19fe\u5b70\ucd9d\u2e0c\u9b8d\uc1a3\u4567\u41ed\u74c8\u650d\u7fcb\u6250\u11ae\uf1c2\u3161\uabac\ucbfb\u75fe\u0b0f\u34f8\ube34\uddd6\u19c4\u97ec\u7676\ubebb\uc2d3\u98b8\ub08b\u0e2f\u9785\u6437\ub5b9\u7414\u556b\u41a8\uec73\u1291\u0735\u3c33\u2dcb\uee04\u0b1d\u9624\u907b\uce7f\ub729\ue403\ub2ea\uedef\u606b\u0463\uf650\udaeb\u89b2\u80bd\u5c45\u2b24\u44ad\u8819\u172f\u43d0\uabe8\ufc22\u1688\u2ee1\ue3bc\udead\u80d1\u86e9\u9da3\u7eb3\uc7d2\u58d4\ufae1\uef87\u5641\u41aa\u7bfe\u0cfb\u208d\ub4e4\u8d07\uee3c\ucb78\u9257\u57f4\u5752\u041f\u9f2d\uac3f\u1b8f\ue0f1\ude92\ua472\u4ee4\u8d5c\uda3d\u77bc\u0b00\ud01f\ud082\u7002\u9ffa\ue4e7\udddc\ud900\ubc58\uedf4\u9d40\ue9f8\ud98b\u350e\u41e0\u9021\u0ebc\ubdd3\ua6f8\u2666\ubc25\u8424\u9459\ua72b\u5980\u4323\u3045\ua040\ue270\uc35d\u1804\ubfa6\u5158\u88ad\u0b99\ua294\u2b01\u8534\udcc1\ufe02\uf308\uac03\ueed8\u1eb2\u41f3\u7bb1\uce86\ue0c1\u5e92\u0a85\u42ea\u47ea\u02ff\ua592\uad20\u499f\u4240\ub89a\u0a0e\uc463\ud4ba\u8f19\u6b0e\u3240\u353e\ub3ff\u84a0\u5a27\u259f\u5bfa\ue87e\uf6d0\u1290\u1ae6\u03a2\u1900\u937c\uba12\u6b9b\u5ae2\uf216\u1da4\u8e3f\ua4d1\ue899\u270a\ua040\u4cfb\u4432\u6153\u3637\u6dcf\u6c1c\u0d38\u575b\uebc5\u157c\u79f7\uf46e\ubae9\u4f12\ud5d7\u392e\u7bb2\ua123\u33c2\u22d2\ua4e6\u2fbd\u1bf1\u370e\udd39\u768b\u145c\ubd57\u976f\ucb28\u5d00\ud994\u5d61\u58c8\u9722\ueb7c\u0853\u9e67\u7b74\u5d30\u7ffc\u3d65\u5c8a\ue480\u82b4\u0e4f\ub900\ue6a6\u2d50\ufa5e\u7bed\ueac2\u44b8\u05af\udb9a\u2fc7\ua8c9\u96cb\u0ab9\u8b92\ua587\u1fc9\uce0a\uf129\ufd80\ub6f0\u7eb6\u9c7f\ub0ff\uf942\ufeda\uc9c0\ud825\u3ee2\u82b9\ubaa3\u6b12\u42c1\u1c13\u9c1d\u3661\uac58\u6f62\u8178\ucb7c\ud22a\ud941\u506e\uc751\u11bf\u891d\u97b9\u98b5\u7241\u5fcc\u2e7f\uc675\ua9a6\u23b9\u80ad\u3409\ua0f6\u7fb5\u9c52\u360e\ue0de\u2bc2\u211d\ua2e8\u255a\u378a\udc76\u0863\u04fe\u6c7a\ud2af\u22f3\u4bd7\ub2a8\u0c6e\u7928\u19dc\u583f\ub3ba\uec3f\u1b16\u540c\u9ad5\ueb3a\uef81\ucaf9\uddfc\uee10\ufe83\ue561\u85b9\u0d8a\u72bd\u8e97\ufb45\ubb0a\u2df7\ucce9\ue018\u6b33\u1890\ucec0\u7019\uc6d6\u8dc5\u15d4\u0e14\u6655\u2ab2\u00c3\u67db\u5b86\u4135\u96a4\u70fd\u2ab5\u06d2\ue8ba\u1b05\ub679\u615d\u0358\u48f4\ucfc7\u7d63\u5ebb\u4941\u77b1\uee28\uf0ff\u1de2\u9e93\u7375\ud92e\u5c13\u6787\u5952\ue77c\uf5a7\u8953\u8bdf\ue66f\ube85\u6ac7\u6903\u5e1f\ub058\ue9e6\u56ea\uce8f\u09e9\u1690\uc3da\ua2e8\u5a33\ud605\u92c5\u4327\u8a95\ue7a6\uc363\ud0d7\ue93f\u2c0b\ude49\u8626\u2204\u3443\u2f6f\u924e\ud811\u5a49\u33d9\u20ae\u9044\u4a1f\uce60\u6af3\u249e\u0449\u128c\u0569\u6aef\u9a89\ubca3\u3688\u9b85\ue2a1\u8451\u33c8\ua959\ue7f1\u398f\uc217\u5d9b\u82ef\ud985\u71b5\ucd5a\ud2d3\u6e87\u0c2c\u0d92\u6937\u521a\u68c8\u1849\ue132\u0820\u09bc\u3600\u2df4\uefde\u8de0\uc360\u8c50\u2133\u252a\ufeeb\ub163\u428d\u9e38\ub30c\u172f\u7bce\ua611\u81c0\udb32\u6743\ub91e\u0435\uf8e4\ude7a\ud27a\u51a8\ub89d\u4b1e\u319a\u3b21\u434b\ucba5\ua54b\u60fd\u23b2\uf78f\ucb3c\u992c\u6431\u2379\ue122\udf34\uc28f\u1e6c\uf615\u7f1e\u430e\u5533\ub439\u7af1\ue378\ud26f\uea56\uf15e\u806c\u54b2\ucedb\u2f61\uda01\u1967\uab54\uda63\u1204\ub43d\u67d7\u224f\uf46a\u99f2\u151b\u1074\uf51d\uad53\u5644\u3a98\u07a4\ua4b4\u88b1\u0fc9\ub3c0\u9fd4\ufec8\uceff\u4504\u4a72\uc91e\u2181\uab70\u951f\u68fe\ufbed\ub5a7\ub9c0\u7c2f\u37e4\u8e3f\u9861\uf6dc\u7641\u1523\uad47\u273a\u6412\u4e52\ue409\uf057\u0201\u3e5e\uf838\ub405\u3fb7\u2345\ue157\u64ca\u065b\u5054\u48b4\u9cae\u5806\u3c05\ucd40\ud8cb\u162e\u6ab1\ueeec\ud8cb\ubb24\u02f2\u4b2f\u732c\u9c95\u2827\u62a0\u38e8\u772c\u071d\ud4ac\u5fc4\u5781\u14aa\u2f0a\u9b5f\u8ee3\u829c\u154f\uab30\ua345\u83b4\u0fa8\uefc7\u1646\u049a\uca8c\u9ecf\ueac4\uc445\u37ed\uc523\uefdf\u7df3\u64b5\u17a7\u9834\ub7ed\u7fb9\u3be9\ubced\uc07b\u9252\u0d45\u38d5\u6c9e\u6148\u9c53\u527e\ud403\ud533\uf507\u034a\u7ed9\ufd75\u942f\u4458\u8aac\uca85\u24be\u65bc\u3ebf\u1c4b\uc7c2\ua548\ub7cc\ua0af\u50d2\ufa7c\u4e7a\u0ccd\u4147\u34b4\u9201\u7aba\u5e53\ub7bc\ua12e\u70ec\u2cfd\u5e11\ud03f\ue199\u1f60\u988f\u25fb\ue342\u1fd5\ue25e\u6522\u48d8\u358b\u9471\u76de\u7a1d\ud9ab\uf1e7\ud4da\ue547\u5250\ud5ad\u8332\u97fc\uc914\u1112\ubdfb\u736a\u407e\ueb04\u2921\u4779\u6933\u1401\u02f5\u814f\u2d4e\ufb57\u1091\ueb16\ub669\u1f61\u6f7e\u4834\ud5ea\ufa98\ufd08\ue67b\u998a\u27a2\ue3d2\u0216\ue200\ue577\u44a0\u7e40\u9f7a\ue20a\u9162\u0723\u533d\ub4f6\u8c4e\u2955\u4aff\u7c95\u168f\ua923\u1d44\u46f3\u7a97\uf605\u9fda\u0dc1\ue674\uec85\u448f\u240f\ub6c6\u4226\uc8fb\u3f8a\ue351\u3cec\u53fa\u226f\u8bd6\u4154\ub338\u44d0\u9094\u50f2\u0c98\ud4ba\ubdfe\u06f6\u056c\ua329\ufabc\u179d\ufd3f\ud3c2\u7050\u3086\u02b8\ud2fa\udf2d\u8adb\uebeb\u94ac\u7299\u08f1\u15ac\u491a\u7ec5\u31f7\uc9be\u341d\ue01e\u2afd\u6411\u9030\u3fea\ucc49\u37c6\u762e\u7155\uadb6\u7e26\u67e8\ufe73\uc7a4\udc25\u69dc\u4378\u2e90\uf6a6\ua7d8\ue72d\ucacd\uc12e\uf7a4\u33a7\u456a\u0e91\u968f\u753f\uc5ca\u9dc6\uafba\u2429\ubb7b\u3f26\uf129\u61b3\ud8f5\uff81\u56ce\ud639\ub853\u7939\ue3f5\ub4a0\u89ce\u86f8\u0d8a\u93e7\ud1db\uc2c1\u519c\uc986\ubba3\ufb8c\ufca4\u660c\u7cf2\ucdda\u383f\u9b2a\uc66f\ua6b7\uee52\uaf7a\u5270\uf1d1\ue76e\u1a14\u3b94\u5519\u6ff4\ufde2\ua4d4\u3f31\u61f4\uf833\uee70\ub115\ua1a1\u1605\u1c24\u67c0\u7839\u262b\u023a\u7bf7\uc733\u20c4\u0fc1\u2522\uc32c\u73eb\u1f10\u25f4\u4b3a\u417c\ud832\udd4f\uc520\u45dc\u7e22\u91c7\u3c20\u7518\uceac\u10c7\u3c95\ue5f0\u04f6\ua589\uc787\u7797\u252b\u47e7\u45ba\u304c\u724a\u68ea\u0bf3\u9161\u62c2\u6f34\u81ef\uc1ae\u60ad\u4088\ud2f6\uf0ea\u8f0b\ud379\u451f\u9e3d\u41ed\u6cd7\ube98\uef39\u2c31\u2fb8\u3a32\uc4da\u5227\u0d7b\ud202\u8230\uac8b\ube40\u8318\u9338\uac55\u8029\uf310\ua420\ued4f\u9b7c\u6cb1\ud88f\uc292\uc426\uf17d\u18e9\u1bf1\u8338\ufdeb\u7527\u1e97\u2255\ue64c\ubad2\u4b99\u4c8c\u2123\ua370\ua8b6\u1570\u115d\ued9d\u3519\ua191\u05a5\u24b5\u0df3\u111b\uad9d\u4226\u5045\ude9c\ubdee\uf8b1\uf988\ud43d\u3efe\u190c\u0f2e\ue855\u90df\u3a40\u5ccd\u1730\u0ee6\u30e8\u6d0c\ud147\uc054\ue9e8\u4a63\u02db\u4c81\u8971\uf0c4\u099a\u1572\u50de\uc212\ua881\u930b\u2ccf\ub620\uc145\u520a\u9d48\ua99f\u2120\u60aa\uf5ed\u56dc\u01c7\ub108\u5221\ucc9c\u418b\ub1ed\u391c\u0415\ue21c\u4eef\udbee\ue575\ucb0b\uedb5\u5289\ude3c\u1d25\u7bbe\ua791\ub1b1\u8494\u6f24\ueb92\uaef5\ucabc\u5ae1\u565e\u4e17\u32bc\u4b6e\uc812\u3c36\u92dc\u7ea9\u1b12\ud446\ud6fc\u52e1\u0cc2\u3195\ua667\ue80b\u102d\uab31\u2b0c\u00a2\u4805\u5ba2\u30ae\u68f1\u12a4\ub70f\u3c51\ufee9\u18d8\uf244\uff44\u28e6\uf29e\ua6f3\ufac6\u2f80\u621c\udd07\u80d6\u3b09\uf3ee\ud82a\ufc8d\ue664\u87b2\u00e9\u0357\u28e7\ue752\uc958\ub6ec\ua86b\u0cde\ubd0e\u11d9\u0d84\u1bab\u607a\u9996\u0a6b\ua997\u916c\ubc38\u9db8\ud43a\ud292\u3ded\u7251\ubc2er\u9744\u082c\u0873\u4d7c\ufb1a\ud73e\u2045\u235f\u771e\u7ad3\u5dcd\ue613\ube9b\ub3a0\ufa60\u15e4\ud64a\u983a\u28c1\uab92\uc3a0\u6cce\u2772\u9b10\ucb39\u73ff\ucc41\u4de6\u4288\u9fba\u63bb\u82a5\ud889\u6297\ue598\u5100\u573b\ubb12\u5c00\ub60e\u0a97\u24c2\u18e1\u594b\u5c25\u6074\u007f\ufe6e\u4f9d\u8ee9\uf25b\uda90\u6e17\ua4a4\u98a5\uf23a\u8e59\ue129\u0f2c\ua60c\u96f8\u422b\ud177\u8b2b\u23c4\u580c\u330b\ubacd\u59e4\u919f\u73dc\u19bf\ud0f7\u4a06\u622c\u16c8\udd19\u2831\ua50d\u218a\u907f\u4094", -1085212457, 0x636CFAF2 ^ CryptoVault.mask()));
        GpuFluidSimulation.bindSamplers(advectionProgram, GpuFluidSimulation.ni("\u6409\uc5c9\u6461\uacae\u122e\uf3cb\u7c44\udfbd\ub85f\u1532", 1054032468, 0x636CFAF2 ^ CryptoVault.mask()), 0, GpuFluidSimulation.ni("\u4d0e\uc058\u3dd6\ue6a0\u3c01\u9a5f\u8452\ucf1d\u28b7", -199347640, 0x636CFAF2 ^ CryptoVault.mask()), 1);
        GpuFluidSimulation.bindSamplers(vorticityProgram, GpuFluidSimulation.ni("\u71aa\uf9db\ue3fd\u7709\u6460\u97c2\uf8de\ue79d\u042a\u6a7c\u885a", -417911219, 0x636CFAF2 ^ CryptoVault.mask()), 0, GpuFluidSimulation.ni("\ue546\u53ef\u10ef\u0a10\u2598\u2816", -1367452540, 0x636CFAF2 ^ CryptoVault.mask()), 1);
        GpuFluidSimulation.bindSamplers(pressureProgram, GpuFluidSimulation.ni("\u8dca\uc291\ubecb\u6b9d\u37cc\ud801\u3c25\u1e9a\u8e23\u79d5\u633a", -702143238, 0x636CFAF2 ^ CryptoVault.mask()), 0, GpuFluidSimulation.ni("\ude5f\u8c3d\uda67\ube3b\u7376\u43e4\ufb98\u7470\u6216\u5fd5\uc306\uf4ac\u8fdc\ua762", -2071222764, 0x636CFAF2 ^ CryptoVault.mask()), 1);
        GpuFluidSimulation.bindSamplers(gradientProgram, GpuFluidSimulation.ni("\ue4f1\u3e45\u270e\ueecc\u4d0f\uf048\u039a\u082c\ubd2e\u5ff0", 363214401, 0x636CFAF2 ^ CryptoVault.mask()), 0, GpuFluidSimulation.ni("\u98a9\u7cb1\uea14\u20f9\uda8f\u9b8c\u6eef\uff16\u46dd\uba7a\uc1b3", -1013051749, 0x636CFAF2 ^ CryptoVault.mask()), 1);
        GpuFluidSimulation.bindSamplers(displayProgram, GpuFluidSimulation.ni("\ubd70\u2f9e\uc956\u34d0\u5609\u757a\u714b\u9ded\ua5d0", 894452910, 0x636CFAF2 ^ CryptoVault.mask()), 0, GpuFluidSimulation.ni("\ue597\u0e4d\u46c9\u4b41\ubaac\udbbc\uc3a7", 34801754, 0x636CFAF2 ^ CryptoVault.mask()), 1);
        GpuFluidSimulation.bindSamplers(copyProgram, GpuFluidSimulation.ni("\ud6b2\u3e4a\u1571\ud21d\u693b\u7d24\u13df\uc11b\u0c9b", 1917716410, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(clearProgram, GpuFluidSimulation.ni("\uc835\uedab\u1cf5\u296b\u834f\ua5e1\u16d9\u767d\u0f83", 796652825, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(splatProgram, GpuFluidSimulation.ni("\ue958\udaa1\u469b\u2a2d\u1fe9\u0b2c\u0ee2\u4b67\u19b0\u3084", -1846800577, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(divergenceProgram, GpuFluidSimulation.ni("\ufa63\ub327\u4229\u5a9a\uf646\u12a4\u3af7\uf5c2\u9e7f\u8606\uaa23\u9e2a", 717122447, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(curlProgram, GpuFluidSimulation.ni("\u483c\u7f76\u9280\ufd1d\ud116\ua2d2\uba91\u96a6\u9af7\u9dd9\u121a\u9f50\u1640", 289417373, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(bloomPrefilterProgram, GpuFluidSimulation.ni("\uaa2b\u1034\ucae8\u530b\u75c3\udbc2\u4f62\u47d3\ue99f", -623505659, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(bloomBlurProgram, GpuFluidSimulation.ni("\u06dc\u294f\ued0b\u0b7e\ueaf2\u29c4\u2eaf\u8b4f\u3ef6", 1251205528, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        GpuFluidSimulation.bindSamplers(bloomFinalProgram, GpuFluidSimulation.ni("\ueb2f\u6b69\ufbc4\u3693\uc5bc\ub074\u1b12\u628f\u17dd\ub433\uba86\uf04f", -1928034502, 0x636CFAF2 ^ CryptoVault.mask()), 0, null, 0);
        vao = GL33.glGenVertexArrays();
        glReady = true;
        return true;
    }

    private static void bindSamplers(Program program, String string, int n2, String string2, int n3) {
        int n4;
        GL33.glUseProgram((int)program.id);
        int n5 = GL33.glGetUniformLocation((int)program.id, (CharSequence)string);
        if (n5 >= 0) {
            GL33.glUniform1i((int)n5, (int)n2);
        }
        if (string2 != null && (n4 = GL33.glGetUniformLocation((int)program.id, (CharSequence)string2)) >= 0) {
            GL33.glUniform1i((int)n4, (int)n3);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int[] supportedFormat(int n2, int n3, int[] nArray) {
        int n4 = GL33.glGenTextures();
        int n5 = GL33.glGenFramebuffers();
        try {
            int[] nArray2;
            boolean bl;
            GL33.glActiveTexture((int)33984);
            GL33.glBindTexture((int)3553, (int)n4);
            GL33.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL33.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL33.glTexParameteri((int)3553, (int)10242, (int)33071);
            GL33.glTexParameteri((int)3553, (int)10243, (int)33071);
            GL33.glTexImage2D((int)3553, (int)0, (int)n2, (int)4, (int)4, (int)0, (int)n3, (int)5131, (ByteBuffer)null);
            GL33.glBindFramebuffer((int)36160, (int)n5);
            GL33.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n4, (int)0);
            boolean bl2 = bl = GL33.glCheckFramebufferStatus((int)36160) == 36053;
            if (bl) {
                int[] nArray3 = new int[2];
                nArray3[0] = n2;
                nArray2 = nArray3;
                nArray3[1] = n3;
            } else {
                nArray2 = nArray;
            }
            int[] nArray4 = nArray2;
            return nArray4;
        }
        finally {
            GL33.glBindFramebuffer((int)36160, (int)0);
            GL33.glBindTexture((int)3553, (int)0);
            GL33.glDeleteFramebuffers((int)n5);
            GL33.glDeleteTextures((int)n4);
        }
    }

    private static void ensureFields() {
        GL33.glBindBuffer((int)35052, (int)0);
        if (velocity == null || GpuFluidSimulation.velocity.width != simW || GpuFluidSimulation.velocity.height != simH) {
            velocity = GpuFluidSimulation.resizeDouble(velocity, simW, simH, fmtRg);
            GpuFluidSimulation.deleteDouble(pressure);
            pressure = DoubleFbo.create(simW, simH, fmtR);
            GpuFluidSimulation.deleteFbo(divergence);
            divergence = Fbo.create(simW, simH, fmtR);
            GpuFluidSimulation.deleteFbo(curl);
            curl = Fbo.create(simW, simH, fmtR);
        }
        if (dye == null || GpuFluidSimulation.dye.width != dyeW || GpuFluidSimulation.dye.height != dyeH) {
            dye = GpuFluidSimulation.resizeDouble(dye, dyeW, dyeH, fmtRgba);
        }
        if (bloom == null || GpuFluidSimulation.bloom.width != bloomW || GpuFluidSimulation.bloom.height != bloomH) {
            int n2;
            GpuFluidSimulation.deleteFbo(bloom);
            for (n2 = 0; n2 < bloomChainLength; ++n2) {
                GpuFluidSimulation.deleteFbo(bloomChain[n2]);
                GpuFluidSimulation.bloomChain[n2] = null;
            }
            bloomChainLength = 0;
            bloom = Fbo.create(bloomW, bloomH, fmtRgba);
            for (n2 = 0; n2 < 8; ++n2) {
                int n3 = bloomW >> n2 + 1;
                int n4 = bloomH >> n2 + 1;
                if (n3 < 2 || n4 < 2) break;
                GpuFluidSimulation.bloomChain[n2] = Fbo.create(n3, n4, fmtRgba);
                bloomChainLength = n2 + 1;
            }
        }
    }

    private static DoubleFbo resizeDouble(DoubleFbo doubleFbo, int n2, int n3, int[] nArray) {
        DoubleFbo doubleFbo2 = DoubleFbo.create(n2, n3, nArray);
        if (doubleFbo != null) {
            GL33.glDisable((int)3042);
            GL33.glDisable((int)3089);
            GL33.glUseProgram((int)GpuFluidSimulation.copyProgram.id);
            GpuFluidSimulation.bind(0, doubleFbo.read.texture);
            GpuFluidSimulation.blit(doubleFbo2.read);
            GpuFluidSimulation.deleteDouble(doubleFbo);
        }
        return doubleFbo2;
    }

    private static void clearAllFields() {
        GL33.glDisable((int)3089);
        GL33.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GL33.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.velocity.read);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.velocity.write);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.dye.read);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.dye.write);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.pressure.read);
        GpuFluidSimulation.clearFbo(GpuFluidSimulation.pressure.write);
        GpuFluidSimulation.clearFbo(divergence);
        GpuFluidSimulation.clearFbo(curl);
        GL33.glBindFramebuffer((int)36160, (int)outputFbo);
        GL33.glViewport((int)0, (int)0, (int)output.width(), (int)output.height());
        GL33.glClear((int)16384);
    }

    private static void clearFbo(Fbo fbo) {
        GL33.glBindFramebuffer((int)36160, (int)fbo.fbo);
        GL33.glViewport((int)0, (int)0, (int)fbo.width, (int)fbo.height);
        GL33.glClear((int)16384);
    }

    private static void prepareCommonState() {
        GL33.glDisable((int)3042);
        GL33.glDisable((int)2929);
        GL33.glDisable((int)2884);
        GL33.glDisable((int)3089);
        GL33.glDisable((int)2960);
        GL33.glDisable((int)32823);
        GL33.glDisable((int)3058);
        GL33.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GL33.glBindVertexArray((int)vao);
        if (samplerObjects) {
            GL33.glBindSampler((int)0, (int)0);
            GL33.glBindSampler((int)1, (int)0);
        }
    }

    private static void use(Program program, float f2, float f3) {
        GL33.glUseProgram((int)program.id);
        if (program.texelSize >= 0) {
            GL33.glUniform2f((int)program.texelSize, (float)f2, (float)f3);
        }
    }

    private static void bind(int n2, int n3) {
        GL33.glActiveTexture((int)(33984 + n2));
        GL33.glBindTexture((int)3553, (int)n3);
    }

    private static void blit(Fbo fbo) {
        GL33.glBindFramebuffer((int)36160, (int)fbo.fbo);
        GL33.glViewport((int)0, (int)0, (int)fbo.width, (int)fbo.height);
        GpuFluidSimulation.draw();
    }

    private static void draw() {
        GL33.glBindVertexArray((int)vao);
        GL33.glDrawArrays((int)4, (int)0, (int)3);
    }

    private static void fail(String string, Throwable throwable) {
        if (!failed) {
            failed = true;
            String string2 = (throwable == null ? string : string + " (" + throwable.getClass().getSimpleName() + ": " + throwable.getMessage() + ")") + GpuFluidSimulation.ni("\u00e3\ub6e5\u296e\u68f4\ud4f5\u5dee\uaa3e\u53cb\u0756\ud8b6\ua5f5\ufbb4\u458b\u0a5e\ud5f5\u838a\ub00c\uca79\u0bae\u90d0\u5a92\u18ee\uf359\ub5be\u10d1\u4c0d\ucf32\u511a\u4ee3\u827d\u774a\uc914", -1514071935, 0x636CFAF2 ^ CryptoVault.mask());
            try {
                ClientInternalLog.warn(GpuFluidSimulation.ni("\u0168\u6b48\u10db\uf5d1\u309a\u89d0\u6f0a\u1f14\udf4c", -998769764, 0x636CFAF2 ^ CryptoVault.mask()), string2);
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
        }
        pending = false;
        try {
            GpuFluidSimulation.disposeGl();
        }
        catch (Throwable throwable3) {
            // empty catch block
        }
        try {
            GpuFluidSimulation.releaseOutput();
        }
        catch (Throwable throwable4) {
            // empty catch block
        }
    }

    private static void ensureCleanupRegistered() {
        if (cleanupRegistered) {
            return;
        }
        cleanupRegistered = true;
        ClientLifecycleEvents.CLIENT_STOPPING.register(class_3102 -> GpuFluidSimulation.releaseSafely());
        ResourceLoader.get((class_3264)class_3264.field_14188).registerReloader(class_2960.method_60655((String)GpuFluidSimulation.ni("\uf507\u2269\u09b1\ue24a\u741a\u724e\u3af8\u870c\ua676\u81ef\u5f31\uab60\u0dad\ub950\uc752", 698631473, 0x636CFAF2 ^ CryptoVault.mask()), (String)GpuFluidSimulation.ni("\ube82\u5e13\u5a2f\ub58e\u12d1\ufb0a\ufb8f\u39a8\uf3a3\ue9b4\u7179\u2ef6\u7ace\u3fb1\uae9a\u7294\u1010\u7ec2\uadaf\ubccd\uc4fd\u22b9\u0f30\u8b17", 499711812, 0x636CFAF2 ^ CryptoVault.mask())), (class_3302)((class_4013)class_33002 -> GpuFluidSimulation.releaseSafely()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void releaseSafely() {
        block7: {
            try {
                if (RenderSystem.tryGetDevice() == null) {
                    return;
                }
                if (RenderSystem.isOnRenderThread()) {
                    Object object = LOCK;
                    synchronized (object) {
                        GpuFluidSimulation.releaseAll();
                        break block7;
                    }
                }
                RenderSystem.queueFencedTask(() -> {
                    Object object = LOCK;
                    synchronized (object) {
                        GpuFluidSimulation.releaseAll();
                    }
                });
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    private static void releaseAll() {
        pending = false;
        newSession = true;
        lastStepNanos = 0L;
        try {
            GpuFluidSimulation.disposeGl();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            GpuFluidSimulation.releaseOutput();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void disposeGl() {
        Program[] programArray;
        GpuFluidSimulation.deleteDouble(velocity);
        GpuFluidSimulation.deleteDouble(dye);
        GpuFluidSimulation.deleteDouble(pressure);
        pressure = null;
        dye = null;
        velocity = null;
        GpuFluidSimulation.deleteFbo(divergence);
        GpuFluidSimulation.deleteFbo(curl);
        GpuFluidSimulation.deleteFbo(bloom);
        bloom = null;
        curl = null;
        divergence = null;
        for (int i2 = 0; i2 < 8; ++i2) {
            GpuFluidSimulation.deleteFbo(bloomChain[i2]);
            GpuFluidSimulation.bloomChain[i2] = null;
        }
        bloomChainLength = 0;
        for (Program program : programArray = new Program[]{copyProgram, clearProgram, splatProgram, advectionProgram, divergenceProgram, curlProgram, vorticityProgram, pressureProgram, gradientProgram, bloomPrefilterProgram, bloomBlurProgram, bloomFinalProgram, displayProgram}) {
            if (program == null || program.id == 0) continue;
            GL33.glDeleteProgram((int)program.id);
        }
        divergenceProgram = null;
        advectionProgram = null;
        splatProgram = null;
        clearProgram = null;
        copyProgram = null;
        gradientProgram = null;
        pressureProgram = null;
        vorticityProgram = null;
        curlProgram = null;
        displayProgram = null;
        bloomFinalProgram = null;
        bloomBlurProgram = null;
        bloomPrefilterProgram = null;
        if (vao != 0) {
            GL33.glDeleteVertexArrays((int)vao);
        }
        vao = 0;
        if (outputFbo != 0) {
            GL33.glDeleteFramebuffers((int)outputFbo);
        }
        outputFbo = 0;
        outputFboTexture = 0;
        glReady = false;
    }

    private static void deleteDouble(DoubleFbo doubleFbo) {
        if (doubleFbo == null) {
            return;
        }
        GpuFluidSimulation.deleteFbo(doubleFbo.read);
        GpuFluidSimulation.deleteFbo(doubleFbo.write);
    }

    private static void deleteFbo(Fbo fbo) {
        if (fbo == null) {
            return;
        }
        if (fbo.fbo != 0) {
            GL33.glDeleteFramebuffers((int)fbo.fbo);
        }
        if (fbo.texture != 0) {
            GL33.glDeleteTextures((int)fbo.texture);
        }
    }

    private static float clamp(float f2, float f3, float f4) {
        return Float.isFinite(f2) ? Math.max(f3, Math.min(f4, f2)) : f3;
    }

    private static class_8030 rectangle(float f2, float f3, float f4, float f5) {
        int n2 = (int)Math.floor(f2);
        int n3 = (int)Math.floor(f3);
        int n4 = (int)Math.ceil(f2 + f4);
        int n5 = (int)Math.ceil(f3 + f5);
        return new class_8030(n2, n3, Math.max(0, n4 - n2), Math.max(0, n5 - n3));
    }

    static {
        newSession = true;
        reqCursorU = Float.NaN;
        reqCursorV = Float.NaN;
        reqFlow = 1.0f;
        reqColorCount = 3;
        aspect = 1.0f;
        avgFrameSeconds = 0.016666668f;
        prevU = Float.NaN;
        prevV = Float.NaN;
        pointerColor = new float[]{0.15f, 0.0f, 0.0f};
        scratchColor = new float[3];
        LOCK = new Object();
        bloomChain = new Fbo[8];
        VIEWPORT = BufferUtils.createIntBuffer((int)16);
        COLOR_MASK = BufferUtils.createByteBuffer((int)16);
        CLEAR_COLOR = BufferUtils.createFloatBuffer((int)16);
    }

    private static /* synthetic */ String ni(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    private static final class FluidState
    extends Record
    implements class_11244 {
        private final RenderPipeline pipeline;
        private final class_11231 textureSetup;
        private final Matrix3x2fc pose;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final float cornerRadius;
        private final int argb;
        private final class_8030 bounds;
        private final class_8030 scissorArea;

        private FluidState(RenderPipeline renderPipeline, class_11231 class_112312, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, int n2, class_8030 class_80302, class_8030 class_80303) {
            this.pipeline = renderPipeline;
            this.textureSetup = class_112312;
            this.pose = matrix3x2fc;
            this.x = f2;
            this.y = f3;
            this.width = f4;
            this.height = f5;
            this.cornerRadius = f6;
            this.argb = n2;
            this.bounds = class_80302;
            this.scissorArea = class_80303;
        }

        public void method_70917(class_4588 class_45882) {
            int n2 = Math.min(65535, Math.max(1, Math.round(this.width)));
            int n3 = Math.min(65535, Math.max(1, Math.round(this.height)));
            int n4 = n3 << 16 | n2;
            this.vertex(class_45882, this.x, this.y, 0.0f, 0.0f, n4);
            this.vertex(class_45882, this.x + this.width, this.y, this.width, 0.0f, n4);
            this.vertex(class_45882, this.x + this.width, this.y + this.height, this.width, this.height, n4);
            this.vertex(class_45882, this.x, this.y + this.height, 0.0f, this.height, n4);
        }

        private void vertex(class_4588 class_45882, float f2, float f3, float f4, float f5, int n2) {
            class_45882.method_70815(this.pose, f2, f3).method_39415(this.argb).method_22913(f4, f5).method_22922(n2).method_75298(this.cornerRadius);
        }

        public RenderPipeline comp_4055() {
            return this.pipeline;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{FluidState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{FluidState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{FluidState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this, object);
        }

        public class_11231 comp_4056() {
            return this.textureSetup;
        }

        public Matrix3x2fc pose() {
            return this.pose;
        }

        public float x() {
            return this.x;
        }

        public float y() {
            return this.y;
        }

        public float width() {
            return this.width;
        }

        public float height() {
            return this.height;
        }

        public float cornerRadius() {
            return this.cornerRadius;
        }

        public int argb() {
            return this.argb;
        }

        public class_8030 comp_4274() {
            return this.bounds;
        }

        public class_8030 comp_4069() {
            return this.scissorArea;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class SavedState {
        static final SavedState INSTANCE = new SavedState();
        int drawFbo;
        int readFbo;
        int program;
        int vertexArray;
        int activeTexture;
        int unpackBuffer;
        int texture0;
        int texture1;
        int sampler0;
        int sampler1;
        int blendSrcRgb;
        int blendDstRgb;
        int blendSrcAlpha;
        int blendDstAlpha;
        int blendEqRgb;
        int blendEqAlpha;
        boolean blend;
        boolean depth;
        boolean cull;
        boolean scissor;
        boolean stencil;
        boolean polygonOffset;
        boolean logicOp;
        final int[] viewport = new int[4];
        final boolean[] colorMask = new boolean[4];
        final float[] clearColor = new float[4];

        private SavedState() {
        }

        void capture() {
            int n2;
            this.drawFbo = GL33.glGetInteger((int)36006);
            this.readFbo = GL33.glGetInteger((int)36010);
            this.program = GL33.glGetInteger((int)35725);
            this.vertexArray = GL33.glGetInteger((int)34229);
            this.unpackBuffer = GL33.glGetInteger((int)35055);
            this.activeTexture = GL33.glGetInteger((int)34016);
            boolean bl = GL.getCapabilities() != null && (GL.getCapabilities().OpenGL33 || GL.getCapabilities().GL_ARB_sampler_objects);
            GL33.glActiveTexture((int)33984);
            this.texture0 = GL33.glGetInteger((int)32873);
            this.sampler0 = bl ? GL33.glGetInteger((int)35097) : 0;
            GL33.glActiveTexture((int)33985);
            this.texture1 = GL33.glGetInteger((int)32873);
            this.sampler1 = bl ? GL33.glGetInteger((int)35097) : 0;
            GL33.glActiveTexture((int)this.activeTexture);
            this.blend = GL33.glIsEnabled((int)3042);
            this.depth = GL33.glIsEnabled((int)2929);
            this.cull = GL33.glIsEnabled((int)2884);
            this.scissor = GL33.glIsEnabled((int)3089);
            this.stencil = GL33.glIsEnabled((int)2960);
            this.polygonOffset = GL33.glIsEnabled((int)32823);
            this.logicOp = GL33.glIsEnabled((int)3058);
            this.blendSrcRgb = GL33.glGetInteger((int)32969);
            this.blendDstRgb = GL33.glGetInteger((int)32968);
            this.blendSrcAlpha = GL33.glGetInteger((int)32971);
            this.blendDstAlpha = GL33.glGetInteger((int)32970);
            this.blendEqRgb = GL33.glGetInteger((int)32777);
            this.blendEqAlpha = GL33.glGetInteger((int)34877);
            VIEWPORT.clear();
            GL33.glGetIntegerv((int)2978, (IntBuffer)VIEWPORT);
            for (n2 = 0; n2 < 4; ++n2) {
                this.viewport[n2] = VIEWPORT.get(n2);
            }
            COLOR_MASK.clear();
            GL33.glGetBooleanv((int)3107, (ByteBuffer)COLOR_MASK);
            for (n2 = 0; n2 < 4; ++n2) {
                this.colorMask[n2] = COLOR_MASK.get(n2) != 0;
            }
            CLEAR_COLOR.clear();
            GL33.glGetFloatv((int)3106, (FloatBuffer)CLEAR_COLOR);
            for (n2 = 0; n2 < 4; ++n2) {
                this.clearColor[n2] = CLEAR_COLOR.get(n2);
            }
        }

        void restore(boolean bl) {
            GL33.glBindFramebuffer((int)36009, (int)this.drawFbo);
            GL33.glBindFramebuffer((int)36008, (int)this.readFbo);
            GL33.glUseProgram((int)this.program);
            GL33.glBindVertexArray((int)this.vertexArray);
            GL33.glBindBuffer((int)35052, (int)this.unpackBuffer);
            GL33.glActiveTexture((int)33984);
            GL33.glBindTexture((int)3553, (int)this.texture0);
            if (bl) {
                GL33.glBindSampler((int)0, (int)this.sampler0);
            }
            GL33.glActiveTexture((int)33985);
            GL33.glBindTexture((int)3553, (int)this.texture1);
            if (bl) {
                GL33.glBindSampler((int)1, (int)this.sampler1);
            }
            GL33.glActiveTexture((int)this.activeTexture);
            SavedState.toggle(3042, this.blend);
            SavedState.toggle(2929, this.depth);
            SavedState.toggle(2884, this.cull);
            SavedState.toggle(3089, this.scissor);
            SavedState.toggle(2960, this.stencil);
            SavedState.toggle(32823, this.polygonOffset);
            SavedState.toggle(3058, this.logicOp);
            GL33.glBlendFuncSeparate((int)this.blendSrcRgb, (int)this.blendDstRgb, (int)this.blendSrcAlpha, (int)this.blendDstAlpha);
            GL33.glBlendEquationSeparate((int)this.blendEqRgb, (int)this.blendEqAlpha);
            GL33.glViewport((int)this.viewport[0], (int)this.viewport[1], (int)this.viewport[2], (int)this.viewport[3]);
            GL33.glColorMask((boolean)this.colorMask[0], (boolean)this.colorMask[1], (boolean)this.colorMask[2], (boolean)this.colorMask[3]);
            GL33.glClearColor((float)this.clearColor[0], (float)this.clearColor[1], (float)this.clearColor[2], (float)this.clearColor[3]);
        }

        private static void toggle(int n2, boolean bl) {
            if (bl) {
                GL33.glEnable((int)n2);
            } else {
                GL33.glDisable((int)n2);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class DoubleFbo {
        Fbo read;
        Fbo write;
        final int width;
        final int height;

        private DoubleFbo(Fbo fbo, Fbo fbo2) {
            this.read = fbo;
            this.write = fbo2;
            this.width = fbo.width;
            this.height = fbo.height;
        }

        static DoubleFbo create(int n2, int n3, int[] nArray) {
            Fbo fbo = Fbo.create(n2, n3, nArray);
            try {
                return new DoubleFbo(fbo, Fbo.create(n2, n3, nArray));
            }
            catch (RuntimeException runtimeException) {
                GpuFluidSimulation.deleteFbo(fbo);
                throw runtimeException;
            }
        }

        void swap() {
            Fbo fbo = this.read;
            this.read = this.write;
            this.write = fbo;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class Program {
        final int id;
        final int texelSize;
        final int dt;
        final int dissipation;
        final int curl;
        final int value;
        final int aspectRatio;
        final int color;
        final int point;
        final int radius;
        final int curve;
        final int threshold;
        final int intensity;
        final int shading;
        final int bloomOn;

        private Program(int n2) {
            this.id = n2;
            this.texelSize = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u26c4\u8e50\u36a1\ua314\ue87c\u355a\ue29c\u3352\u0ffe\ub561\u7957\ucda5\ua2d3", -924807718, 0xC75667DB ^ CryptoVault.mask()));
            this.dt = GL33.glGetUniformLocation((int)n2, (CharSequence)"dt");
            this.dissipation = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u5099\ubfea\ubede\u7f1f\u2cf4\u241e\u9727\uc88a\u8d46\u2585\u6a56\u365d", -718452653, 0xC75667DB ^ CryptoVault.mask()));
            this.curl = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u56dc\u326e\ueacb\u9e66\u457a", 1615360042, 0xC75667DB ^ CryptoVault.mask()));
            this.value = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u2a1b\u6075\uc01f\uba5a\u7193\uda0f\u82a9\ue17e\u116c", 1433276799, 0xC75667DB ^ CryptoVault.mask()));
            this.aspectRatio = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u4c5c\ud91b\u91bc\uedbc\u3496\ue4b5\ude63\u46a4\ub643\u0b85\ud1be\u9b37\u30f5\u7f89\u0f25", -1690657399, 0xC75667DB ^ CryptoVault.mask()));
            this.color = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u38e0\uc51f\u5dd8\u7146\u03b6\u9295", -1913798768, 0xC75667DB ^ CryptoVault.mask()));
            this.point = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u0d31\u1a35\ua38c\u3d8c\u7371\ufccc\u71b0\ucf3a\u53c3", 105367255, 0xC75667DB ^ CryptoVault.mask()));
            this.radius = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\ub493\u374d\u6a05\u206f\ua5bc\u3fdd\u4323", 1813604897, 0xC75667DB ^ CryptoVault.mask()));
            this.curve = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\ue5cb\u7e0f\u1ecb\uc3a6\ucde3\u9b1f\ued54\u965a\uf6d4", 35401955, 0xC75667DB ^ CryptoVault.mask()));
            this.threshold = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u02cc\u7c03\u28d9\ubdc3\u29c2\u8504\uf0ec\ufb49\u1ba8\ud0ac\ucf9c\u0ae4\ud412", 1082415127, 0xC75667DB ^ CryptoVault.mask()));
            this.intensity = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\uc506\ufb78\u7f90\u7897\u58b0\u6acc\uc920\u44d8\ud50d\u246d\ubd5a\u8c7f", -900833049, 0xC75667DB ^ CryptoVault.mask()));
            this.shading = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\u0b75\ufb9d\u9c12\u7264\ube03\ua468\u5f04\ubd7f\u3446\uf42e\u1b35", -1896229199, 0xC75667DB ^ CryptoVault.mask()));
            this.bloomOn = GL33.glGetUniformLocation((int)n2, (CharSequence)Program.fi("\uc9b7\u148a\uafc1\u9197\uaf5b\u90ef\u7e5f\uc275\u830c\u152c\ubce9", 1172872262, 0xC75667DB ^ CryptoVault.mask()));
        }

        static Program create(String string, String string2) {
            int n2;
            int n3 = Program.compile(35633, Program.fi("\u00f7\uba13\ubff4\u35b4\u440d\u66fc\u9343\u1537\u20b6\u70f6\u083a\u858a\u9705\u6773\ue184\u1676\u0b4d\u3b83\u820d\u89de\ua197\uf254\u14e4\u8f78\uf4f4\uc70a\u9d70\u9bf0\u92fd\u56a0\uaef5\ubfb4\u6f74\u8968\uc578\ufb01\ud60b\ue9ce\uc4dc\u207e\ua253\u5d1f\uea14\u40c9\u3b56\u793d\ubc6c\u3935\ud85e\uac9e\u61bf\u9ff9\ud93e\uf4a2\udc85\u8722\u9a75\u7028\u1700\ucf1e\ue06f\ue386\uc32d\ud587\u1e66\uce5d\u7bfb\u89d9\u8af3\u2ae7\u9110\u0ecb\u4180\u49dc\u6ccd\u9083\u975b\ua48a\u5f29\uf4f4\u0e4d\ud2ee\uec41\u7b54\ub018\u4c42\uba0f\ub5d8\uddae\u1175\u6596\ueb96\ue9f0\u88b7\ua0d2\uf370\ue386\u4bc2\u1443\ua14a\u2ed0\u0ceb\u1cc7\u7dbe\u2518\uf050\u9d9d\u47a1\u2dae\u7ee4\u1fd6\u1854\ufa2f\uf4a1\ub99d\u04f0\udddc\u92a4\uc374\u275a\uddce\u319a\uba48\u03c6\u2efc\u7027\u0e25\u45c5\ufe32\u3a3a\ud0c8\ufba8\u3ed6\udfd9\u397f\u7380\u308d\ue506\ua850\udbaf\u9a5f\uc9c7\u2731\uee15\u30cb\u9593\u7e9f\ub9df\u827d\u7fe5\u51ff\u79b8\u8928\ucd8d\u58a9\u83de\uab1c\u17de\u65e9\u83f2\u75d3\u1bf4\u7a07\u215d\u3760\ua57c\u91f9\ufb8c\u252ca\ud66f\u7e98\uf93c\u050d\uec44\ud818\u5e9b\ub059\u331d\u4be8\u3076\ubc66\u1ce9\ub40c\uab97\u411d\ueb62\u0d16\u81f4\ud5a0\uf19a\u67dd\uaeef\uf223\u2a25\uf9e4\u23df\ubcb4\ucd0d\u267e\u8cc4\u9398\u9da5\u1096\u56ed\u2ed7\u637b\u3e79\ua01c\u144f\u73d1\ua991\u555a\u85c4\u0916\u0f67\u2d17\u6320\ub4af\u2dee\uf157\u3787\uc80c\u5dd1\uc199\u5690\u2a3b\u4602\u8857\ua08a\u0381\ud4cc\uc725\ua77f\u0090\u40c6\u82cd\ucf77\uae1d\ubaeb\uaf55\u8e7e\u07be\ue883\u08fa\u7c18\u3524\uc4d8\u0c5b\u1e90\u191e\u2f2e\u04f1\u0368\ua372\u3b03\u1998\uc386\ue696\ufdb0\uecc0\u6cb2\u3434\u9aaa\u6435\u341d\uac14\ucc60\ub2d1\u04ca\ubc31\u3dbe\u4998\uf859\ud0e8\u254b\ucab4\u01c3\u08b0\uee03\u62a7\u826a\ue2a5\ubacf\uf114\ud90b\u022c\u6d81\u7bab\uf331\u0d09\u2d04\u43cf\u812d\u0e47\ua0cc\u3af6\u3e81\u0cdb\uabb9\ud54a\ub989\u679e\u1c68\ua496\u8c49\u8a90\u283a\u5990\ub053\u99c6\uf377\u83e7\u6917\u11cd\uc2a1\u559a\ue343\u6fc5\u87d8\u6504\ub5f2\u5a32\u5a4e\u90ea\u7e8d\u1f40\u23c5\u3297\u2f3f\u2007\uaa70\ua0ab\ud44f\u272f\u765d\ub5fd\u0642\ud7ac\u57c5\ua6be\uf91d\uafe7\uf874\u7997\u753e\u0a58\ufae8\u15ee\u4ab2\ub77e\ud7bf\uf755\u6472\u1b69\u3d7e\u194d\uffe7\ub793\u7dcf\u669d\u0eda\udd84\u5fcb\u4590\u8d5b\uf974\u4263\u27e6\u2b43\ud41f\u05b1\u1694\u42f4\u4ccf\u17be\ud0e5\uecab\uc948\u476c\u240e\u2ea4\u1486\uc775\ue701\ucf27\uc0e2\u032f\uc834\ue8f3\u9b20\u32bf\uc5f8\ue9e3\ubf97\u2564\u5a13\ucaf3\u47de\ufc52\ud366\u75f3\u1cef\uc5ac\u6078\ubecb\ub2c0\u1272\u539e\uedff\uf667\u8df2\u8eb6\udf64\ua8b5\u6b2c\ub520\u9702\ua1d1\ubdf3\u35b8\ud9ff\u2afc", -1926652220, 0xC75667DB ^ CryptoVault.mask()), string);
            try {
                n2 = Program.compile(35632, string2, string);
            }
            catch (RuntimeException runtimeException) {
                GL33.glDeleteShader((int)n3);
                throw runtimeException;
            }
            int n4 = GL33.glCreateProgram();
            GL33.glAttachShader((int)n4, (int)n3);
            GL33.glAttachShader((int)n4, (int)n2);
            GL33.glLinkProgram((int)n4);
            GL33.glDetachShader((int)n4, (int)n3);
            GL33.glDetachShader((int)n4, (int)n2);
            GL33.glDeleteShader((int)n3);
            GL33.glDeleteShader((int)n2);
            if (GL33.glGetProgrami((int)n4, (int)35714) == 0) {
                String string3 = GL33.glGetProgramInfoLog((int)n4);
                GL33.glDeleteProgram((int)n4);
                throw new IllegalStateException(Program.fi("\ufae3\u7756\u58cb\u0305\uc2b8\u6292\ub285\u9aae\ud45c\ua2e3\u895b\udfcd\u60b5\u9752\u4dd8\u26e5", -1256390845, 0xC75667DB ^ CryptoVault.mask()) + string + Program.fi("\u5f5d\u5e69\uaa91\u81fe\u8adf\u65d8\u967b\uac16\u682d\u283d\ua789\u813d\ud5d3\u6f63\ua1c1\u239a\u863d\ue5f9\u6083\u7ec7\u12e6", 1399421535, 0xC75667DB ^ CryptoVault.mask()) + string3);
            }
            return new Program(n4);
        }

        private static int compile(int n2, String string, String string2) {
            int n3 = GL33.glCreateShader((int)n2);
            GL33.glShaderSource((int)n3, (CharSequence)string);
            GL33.glCompileShader((int)n3);
            if (GL33.glGetShaderi((int)n3, (int)35713) == 0) {
                String string3 = GL33.glGetShaderInfoLog((int)n3);
                GL33.glDeleteShader((int)n3);
                throw new IllegalStateException(Program.fi("\ue686\ubb44\u9a39\u74c1\u3cf0\ucf31\u7497\u49b4\u6db1\ud3bb\u14a5\u3dde\u2cb8\uf8aa\u02a5\u99a2", -2005317434, 0xC75667DB ^ CryptoVault.mask()) + string2 + Program.fi("\u41da\u6026\u5b36\uc51f\u3664\uce20\u3cc8\u9073\u36cf\ud61e\u7a93\u326a\ucc6c\uf145\ud76e\u95cd\u317b\u944a\u0270\u8b49\u53ef\ud9d7", -1472241241, 0xC75667DB ^ CryptoVault.mask()) + string3);
            }
            return n3;
        }

        private static /* synthetic */ String fi(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    private static final class Fbo {
        final int texture;
        final int fbo;
        final int width;
        final int height;

        private Fbo(int n2, int n3, int n4, int n5) {
            this.texture = n2;
            this.fbo = n3;
            this.width = n4;
            this.height = n5;
        }

        static Fbo create(int n2, int n3, int[] nArray) {
            int n4 = GL33.glGenTextures();
            int n5 = 0;
            try {
                GL33.glActiveTexture((int)33984);
                GL33.glBindTexture((int)3553, (int)n4);
                GL33.glTexParameteri((int)3553, (int)10241, (int)9729);
                GL33.glTexParameteri((int)3553, (int)10240, (int)9729);
                GL33.glTexParameteri((int)3553, (int)10242, (int)33071);
                GL33.glTexParameteri((int)3553, (int)10243, (int)33071);
                GL33.glTexParameteri((int)3553, (int)33085, (int)0);
                GL33.glTexImage2D((int)3553, (int)0, (int)nArray[0], (int)n2, (int)n3, (int)0, (int)nArray[1], (int)5131, (ByteBuffer)null);
                n5 = GL33.glGenFramebuffers();
                GL33.glBindFramebuffer((int)36160, (int)n5);
                GL33.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n4, (int)0);
                int n6 = GL33.glCheckFramebufferStatus((int)36160);
                if (n6 != 36053) {
                    throw new IllegalStateException(Fbo.wr("\ub7be\ue760\u5564\ue11f\u449c\u37ec\uf81d\u58cb\ua554\uecb7\ub719\ud50f\u6962\u2107z\ubcbd\u87bd\u3dc9\ud01d\u8764", 665017874, 0x1F5D1183 ^ CryptoVault.mask()) + n2 + "x" + n3 + Fbo.wr("\u4176\u6d49\u58f9\u4706\ua576\u675a\ub2fc\u2c8a\u7988\uc377\u76d8\u5258\u17b2\u17c1\u5414\u0557\udd4b", -909260753, 0x1F5D1183 ^ CryptoVault.mask()) + Integer.toHexString(n6) + ")");
                }
                GL33.glViewport((int)0, (int)0, (int)n2, (int)n3);
                GL33.glDisable((int)3089);
                GL33.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                GL33.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
                GL33.glClear((int)16384);
                return new Fbo(n4, n5, n2, n3);
            }
            catch (RuntimeException runtimeException) {
                if (n5 != 0) {
                    GL33.glDeleteFramebuffers((int)n5);
                }
                GL33.glDeleteTextures((int)n4);
                throw runtimeException;
            }
        }

        private static /* synthetic */ String wr(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    private static final class Shaders {
        static final String BASE_VERTEX = Shaders.ql("\u3900\u3af7\u59f1\ub607\u2360\u86ff\u8168\u926e\u98a2\uff5f\u8b73\uaad5\u0c96\u52e7\u16ea\u4543\u8075\uf8f7\ufe89\uc611\uf501\u9b25\u8f4f\u8c47\ue134\ucbe1\uba6e\u73a4\u58f4\u3bc4\u599f\udf4e\u1e61\u5cf6\u72de\u8405\uadfb\u4c96\ueb30\u41b5\ubb2f\u0782\u36f8\u1901\u151f\u02ab\ue7f7\u4c2e\u3477\u4242\u6531\u9c14\u9b89\u8bb7\u2afd\ue274\u8524\uc1cd\u5374\u8cb7\ua99a\ufe7c\uf5e6\u2b9b\uc88f\ubfc6\u030b\u0721\uc7a9\u0142\u7864\uf1d2\u41a9\u6e81\ua241\u2391\u69e1\u28c3\u46c5\u0a16\u74ae\u345e\u0522\u0bdf\u4bb2\u0e60\u6f5c\u1048\ua6b1\uf5e6\u1fa4\u5290\u65dc\uec96\ue896\u2ea1\u0fc9\u6df8\u0940\u1b17\u02b0\u9013\u3cab\uaca4\ue52a\ud28e\ucabf\u988e\u47ca\u63e3\u1db5\u9770\u292d\u3b8c\ue314\u251b\u8563\u4dc8\u1b6f\ua5dc\ubbd3\u11c6\u1f1e\u3e72\u9e3f\ud004\u1c83\uc051\u1e26\ude5b\u6191\ua68a\u1706\u801d\u166f\uba22\u34cd\u1ae6\u8aaa\u09d9\ucef1\u3e0b\uef2a\u618f\u55b7\u3cab\u5806\u3b0a\u068b\u8b2c\u1cef\u55f8\u61c8\ued03\u1e25\u044a\uf95b\u05da\uf798\ub4ea\u4731\uc948\u0147\u9a55\u907e\uf6e3\ua1fc\u0631\ueda5\u4a98\u985c\u51cc\u56ad\u7afb\u21b9\u364c\u8b0b\u8e76\u26b2\u4623\u9956\ube27\u136c\u7104\u346f\uf37b\u0373\u8c35\ue2d2\u6625\uf663\u1b22\u529d\ufa8a\u842d\u1af3\u6780\u8f8b\u6f49\u3775\u848b\u10b2\uab97\udd41\ud75b\ua7f4\u3e98\u744b\uaa13\u74d2\u3be4\u5623\u3188\u70cc\ub790\u9949\ucf2a\u938e\u985c\u0d7b\u67c8\u3423\u6d33\ud6f8\u0989\u62e2\ue5e8\u5bd5\u88ea\u3111\u8a14\u6e38\ue10c\ua455\u9989\u57d7\u54b9\uc303\u81b5\u6eed\u96d8\ua636\ue3cc\u3d5b\u8511\u4f23\u21a7\uaf09\u8fe3\ue02f\ubc1d\u9608\ud180\u73eb\ua3dd\ua4c4\ufd5a\u2390\u5af3\u32b4\u3595\ue476\ue10f\uc914\u365a\uc82c\u8485\u2897\uad8a\u7bc4\uead1\ub740\u8c3c\ue344\u144a\u295a\u0a10\u469c\u7fb8\ucc67\uad33\u48a1\udca0\u27e5\u6528\u61aa\u6f43\uea0c\u5e9b\ub5e8\u07ed\u32c7\u6510\u0c1c\ud2d5\u3e7e\u5d65\u6ac8\ufce8\u76d5\u3e0f\u7256\u1eb2\u4a1d\u1fd7\u9780\ubec7\ucd42\u74bb\u2d14\u1293\u783c\ufe4d\u9ce0\uac40\u9c1c\ua43f\ud857\ufb74\ucf65\uda12\u6a08\ua306\u8f2d\u310c\u4cfe\u9b22\uf049\u6a2b\uddf0\ue50c\ucf30\u8220\ue822\uf0da\u1766\ua6b6\uf7c9\u19e9\ue772\udaac\u7b15\uf9f7\u9298\u764e\u87c1\u69ae\u9937\u7671\u9fa2\u6064\u5044\uafc1\uda5c\uae2f\ubd89\u5552\u5b65\uf9b8\uaf74\u6e71\ufacd\u0093\uc2bc\ubdbc\u7596\ufa14\u62e8\u79e8\u0bb5\u5658\u9067\u4ae5\u0ffe\u57c2\uc845\u6756\u1c67\u82a0\u77af\ue240\u7171\ub08d\u5e0e\u9c17\ua80b\u7a55\u96fa\u6fd8\u42e2\u0d4a\ua5a0\ua69e\ud76b\u9299\u87d8\ua110\u3ad1\u3ddc\ub456\ua548\ubccc\u6d85\ud0f6\u9232\ufdee\uaa18\u7e3b\u639c\u2cb0\u48db\u1adf\u77b2\u707b\uca59\uad76\u658a\ue551\u5e0e\uee61\u8e11\u9417\u3d47\u14d8\uc091", 2008880132, 0xC6E28B41 ^ CryptoVault.mask());
        private static final String HEAD = Shaders.ql("\u2770\u2b34\u5e74\ubc3c\ua212\u50a8\ue0da\u65fe\uc8b0\u92f6\u8807\u802a\u2908\u67a6\u9a1e\u367e\u774e\u2836\uada0\u5639\ua1a4\ucc00\u8a7e\u8b4d\u8e8e\uaf12\u16c9\u595a\uca37\ueaef\u72ee\u7e56\u0d85\u2052\ufe0e\uff5c\uc561\uac06\u7d64\u59e7\ua698\ucc49\uf408\ua286\u1d85\u0158\uc7ec\u0daf\u6d09\ub686\u1117\u50c6\u83ae\u3af7\u448e\u60fa\u88be\u5f38\uffd0\ucb70\u00f4\uf5cb\u359f\u2581\u8cd7\ub311\u65c9\uc7b5\u2521\uf7c6\uf2d6\ubebf\u4785\ufda1\uab3b\ud923\u9197\u5587\u28d4\ud421\u2b86\u55fd\u32a9\u25ec\u25a9\ufc91\uc11d\uc381\u2ae5\u0cec\uf6f0\ub9ec\u3136\u5e1d\udc74\u5564\u68c2\u56ed\ubf4b\ude0d\ucdd3\u18a7", 968862415, 0xC6E28B41 ^ CryptoVault.mask());
        static final String COPY = Shaders.ql("\u5a45\ue470\uc41c\u0ed7\ua9b0\uc74b\u0525\u592e\u390d\u7c49\u1e24\uca37\u0f2c\u3b8b\u6cce\u88cb\ue10c\uc140\ubfc2\u9693\u78cd\ubb4e\udcd6\u5e0b\u607c\ufc46\u798e\uaf62\uc553\uc038\u98fd\ubfa1\ubab7\u69d6\u4449\u8b75\ud07a\u8033\u40cb\ua4f0\u749c\u4894\uc3dd\uec3c\u7bf1\ud579\ue921\u6973\u0f5c\u013d\u0aad\u8ba9\uf3bb\uaefe\u675f\uf3f8\u1b62\u8bc9\ub166\u87d7\u2bf7\u2fb5\ucf32\ua661\u25fa\u9114\ucb57\ubb33\u84d9\ub5db\uc7c3\ued5f\u3a7e\u1363\ub203\u17ca\u7c51\u3908\u17de\ub1bd\u5095\u4911\ube75\ufce3\u1628\ub27f\u8445\ud5eb\u74d7\u126c\u24a1\uffd9\ua940\u4cf9\uf763\u5f63\u1689\ucf6c\ud34a\u0665\u7c6a\ud072\ud381\ue7ed\u373b\uaa32\ub60e\u6804\u5a6c\u3bf8\ue62d\u73d6\uab53\u4b3e\u581d\uaa77\ud562\u4c82\u1321\uafd8\u8631\u520c\u4593\ud420\u30c6\u2b41\ue8a3\u13af\uec00\ue8ee\u719c\u76c1\u34ed\ue615\u43c5\uc091\ua7a3\ufad2\uce17\u5d43\u09b0\u5528\uf7af\u1751\uc490\u639d\u1565\u2f36\u9a8a\uac7e\ud177\u6c69\u4ac7\u6358\u71ac\ucaaf\u0c2f\u5dfb\u462b\uc94a\u67da\uce42\u9d5f\u7f24\uf088\u19bc\u204a\u3610\u531a\u7eb9\u9ade\u3e68\u3a6c\ue47e\uefc7\ua293\u1d2d\u68ac\ud8cd\u72e9\u0ecb\u8585", 1801139130, 0xC6E28B41 ^ CryptoVault.mask());
        static final String CLEAR = Shaders.ql("\u1d43\u3c75\u3985\u39c6\u1bdc\u4f69\u63d4\uf63c\u7331\u4df0\u8b37\u42eb\u6280\uaab1\u4a89\u8f21\ud171\u5404\u138c\ua4ba\uab24\u491f\uc980\uf400\u0541\u2d77\ub20f\u7562\u12d4\ub338\ua5d0\ua1f7\u11ad\u8b21\u9fb4\u6513\ud314\ued14\u4959\uf62b\u5d78\ufc86\u5ba0\ud854\u2055\u36d4\u1d35\u5922\ue1b9\u202e\u7862\u6498\u7999\u7107\u3503\u9aee\u5a71\ua5b8\uea38\u7895\uc02e\uec63\u71ea\u6588\uf958\u5416\u36d6\u8b94\u9409\u27fe\u83cb\u5f3c\uf7e2\u2721\u564f\u9ad5\uc5d5\uc3fc\udde5\u1cd9\u7136\u2244\u6b04\u2c03\u46de\uf8b4\udb5c\uf919\ua490\ua8e8\ua960\uff22\u5746\u9e39\u5b85\u9db8\ua8df\u4f98\ue8f0\ue369\u0a3d\u4cde\ue323\u4626\uae7c\u56d7\u5ad2\u6538\u6b8e\ue13d\u494d\uc7f5\u0b96\u26e6\u8662\u3dc1\u1d4e\u1f26\u05e4\udc8d\ub2b1\ucf11\u7d1b\ued30\u8045\u12e7\u7f9b\u4394\u2e3f\uee56\u0d71\ue02a\uf9c6\u7724\u9ba5\uda2e\u996a\u744e\u36aa\ud8e7\u8551\u7503\ucc4e\u31ee\uacfa\uc307\u1241\uf31d\ub711\u9c5a\u58de\u1a23\ub4f2\u6fbb\u8e34\uc5ac\u4f47\u250c\ua700\u32e0\u4fb7\u25bb\u6f58\u94f8\u4e21\ue15b\u8fe6\u9d84\u379a\ubc64\u3b32\u3d6b\ue3c7\u9377\u8047\u2651\uda01\u7400\u2be5\u4383\u3576\u2021\u6e03\uf56b\u21a6\ua252\u6fa1\udc29\u0caf\ue3bd\uceff\u3ddd\ufb4a\ua79c\u18a9\u54ba\ude82\ue676\u3d45\uae01\ua74e\u7c1d\ud587\ubdd2\u923c\u6523\u20f9\uf5c5\u1cc5\u6070\u260c\uc28b", -869534838, 0xC6E28B41 ^ CryptoVault.mask());
        static final String SPLAT = Shaders.ql("\ucfa2\ud547\ucec9\u023f\u509c\ua782\u1f4a\u16a9\uc76c\u6a3c\uf914\ue75a\ua397\u6baf\u6129\u47ed\u4ca3\u7463\ubc2e\uc9a1\u21fe\u5d4c\u9c00\u920f\ue09f\u0f13\u3ef6\uf294\uac91\u4427\ubf72\u666a\u934f\ub794\u2d41\ufae3\u7535\u9ad4\ua3d4\u405d\u578d\uaef1\uc676\u02b3\u3ea5\ud475\u69e8\u373c\ua595\u988d\u8096\u3719\ub75b\u8341\uc800\u1b8a\uf951\u5131\u4cf3\u3f4e\uba99\u6b0d\u4e4f\udde3\uc264\uf75d\u02f7\u00df\u9815\u31ce\uc1b1\u1570\uba72\u6cfe\uce97\uc3f4\ufb49\ucd44\uc91a\u13b3\u644b\u6b87\ua6b4\u5d17\u7f51\u73f6\ue71c\u434d\u9f0f\uaf89\u074e\u96aa\ueff7\uf42a\ue23b\uc0f5\u050c\u1977\u4aee\u532e\ud6f8\u4430\u2e5c\ue409\u7203\u56a1\u6d9b\u382c\ued46\u929b\u91ce\u30ac\ucfe7\ub735\u8adf\u8e07\ue188\u1025\ud64d\ueef4\u7221\u5ad0\u2043\ua6e4\u14f6\u9d26\u86bd\u4a7c\u18ec\uaaf4\ub06a\u5cd8\ua1bd\u8f8f\u8916\ud1f0\uce92\ub10c\ue0cc\udcfa\u7f27\uaa44\ud90d\u02cc\u452b\u4317\u8e8b\u90e7\u81e4\u2d52\uf903\uca86\ud112\ua299\u25f9\ua83a\ud99c\udb35\ucea2\u7a12\uef8c\u1e5d\u0576\ub2f2\u326e\ue0fb\u2f76\uce55\u7d27`\u894b\uae4e\udb74\ucf94\ub5da\u935f\u82e3\u639b\ua20c\uffd7\u4c02\u01ec\u220b\ucffa\u0da3\u2ad3\u881b\u6b2e\u89fa\ud1b2\ubfc8\ud3e9\u8727\u7b35\u5e52\ufa71\uad8d\uebcd\u4e20\ub9e1\uc4a7\ud60d\u3bf1\u61cf\u746a\u4181\uc1a4\uea96\u96e6\u6507\ud377\u1c4f\ubfbd\u54e1\ucb13\u6d2e\u35ba\u8dab\ub66f\u2040\u9614\u1204\u99a2\u3f0c\u3c34\ua6e2\uf9d2\u016e\ufea7\uc6e6\u54b6\ueede\u781d\uba37\u06f7\ud9da\ua73d\uaab7\u6d8b\u2197\u5650\u1b58\uaad1\u8d51\u444a\u5e1a\u2e58\u2325\u7571\u5c2c\uceb8\ubf5e\u56a2\ufe96\ue106\u976f\ueaa0\u781d\u20d2\udb02\u18e0\u7e1b\u1099\u9a1d\u633d\u9543\u8701\ue0ab\u0ed0\ud215\u900b\u1d7c\u4bb0\u5584\uae48\uce70\u834f\ue250\ueb51\u7247\u025d\u469d\uc1c0\uf29d\u4137\ufb0e\u1126\u7b48\u8553\udc55\u3ab6\uc345\uf03a\u4838\u24ac\u9b0b\u2dbd\uc56e\ud722\uc5c9\ue094\ub477\ua918\u11a8\ua182\ucf1a\ud9ad\u43ed\u7e05\u7521\ud205\uc4d4\uc52f\u01b1\uf387\ufceb\u33e8\ucc6b\u57fa\u654c\u76aa\ubb48\u8ab9\ub0b1\uad89\u8e07\u8767\u99f4\u9a7c\ucc76\u6cad\u3295\uf52c\uf670\ue0f9\u9cf7\ue594\ufc62\u4d1d\uab60\u2e2d\ua600\u83b1\u92e2\u8c03\ua536\uff6b\uce5e\ue12e\ue687\u70b1\u6347\u2654\uf4d1\ub8ea\ufc45\u4bbb\u2f20\u1154\u7a2c\u776d\uffbd\u2a88\u8ee3\u59c4\u5114\ufa3f\uda73\ua18d\u866c\u1b25\ufaba\uee54\u7b0e\ua3f3\u3d66\u3f6f\u5289\ue3e0\u8b0a\uaab8\u59be\u1773\u0e39\u7998\ue281\u04c2\u3e94\u6844\u7670\ud2d0\u87c3\u58b8\u7c59\u3aa2\u29d5\u34fb\uc7b5\u40a0\u7c0a\u3275\u2830\ud185\u1f05\u9c32\u941b\u3b91\u91b9\u54d3\u4fbd\uc7e1\u4b3b\u3b67\ud9e1\u3321\u2b3a\ubc51\u77c6\ufc5c\u5f73", -812437780, 0xC6E28B41 ^ CryptoVault.mask());
        static final String ADVECTION = Shaders.ql("\uf024\ud4c5\u59d1\u11d8\ud9b1\u0209\ueeff\u8052\ubd75\ubc51\u8b73\u52b8\u4397\ucd91\u9c76\uaf28\u3bab\u4e52\ud42b\u91e1\u0ea3\u7dac\u9b4e\u6247\u766e\ue423\u8a6c\uf25f\ue9c9\u3938\u8dcd\u9578\u347c\ubb8d\u9c52\u1109\u97ee\ucfa1\uc0f5\u89c9\ucc3c\u7a1c\uaec9\u56d0\u17df\u8716\ub403\u6f48\u5892\u446e\uee9f\udf49\u527a\ua460\ubd0c\u91dd\u9ef8\u0c7c\u8408\u5243\uc584\ucc88\u2519\u1f24\u4148\uc01d\uf2ee\u64cb\u5579\ud98e\ud532\u7940\u31e6\uaac8\u71c6\u4520\u55a9\u0f0d\u61ce\ucc2d\u9954\ue894\u3d65\u30dc\ue537\uf60c\ua661\u7b6d\ufada\u140f\u3d77\u3e28\ucfc7\ufbaa\ub9d5\u3bbf\ucfe2\u4d3d\ub21f\u3b11\u4242\ua701\ude47\u6e68\u92d4\uce91\uec27\ub1fa\ua9cd\u83f1\u8ccf\u999d\u1b8a\u81bb\u20b8\ue64b\ufe3c\ud617\ua23b\u56aa\uf6da\u73c2\u645f\uc976\uf020\ub223\u9071\ub9f9\u4c9e\uef27\uaca5\u8f67\u1ac5\u7586\u64b4\u505f\u815c\u7e11\ub3df\u0fd5\u5252\u38d4\uc5e4\udf6f\u9b3c\ue997\u0f41\ubf15\u8206\ub096\uca95\u30f1\u38ac\ufa64\u23a1\u750c\u95b9\uca94\ud19f\ub3bf\u36af\u69cc\u2a49\u2c9c\ubcc0\uca7c\ue445\ud66b\u0357\u21c0\u7ae0\u27cf\u604a\u0a1f\u1697\u27a2\u3af8\u7b2d\u2d25\ud17c\u879b\u330a\u051e\u0a6c\ud1d3\ua31b\u7b18\u3a19\u10f5\ubd80\ua34c\u733b\ub3cb\uac8b\u909c\uc30c\u6c6c\u614d\u03dc_\ua00e\u6e6d\u0bd1\u33ac\udd01\uaecd\u19e2\uad59\ub461\uf29a\u4f1c\uae8e\u7e44\uc93c\ua8fd\u2522\u73e1\ue196\ue41a\u5154\u57db\u4f82\ub1df\u3539\u067b\u8571\u400c\ue517\ua878\u9370\u481c\ubeaa\u7cc7\u92f0\u2541\uf97d\ufb5a\u6676\ud9aa\u3495\u72a7\ua972\u6ab0\u46dc\u370b\ueb02\ue123\ufa39\u04ee\u2c5f\ub0ac\ue51f\u97e3\uc160\u2c08\uc7a3\u89e7\ud0bf\u00bf\u894a\ue112\u228f\u4a3c\uc86c\u6d4f\ubdd3\u7b06\uca1d\u6ed3\u19b7\u185e\u7ffc\u3546\u7ed2\u11b1\u54c3\u9512\u52f1\u992c\ub857\uf82c\ud997\uc474\uec3e\u4c57\uc93e\u22ad\u727f\u165b\u9bf3\u961f\u8cc1\ue6f5\ud58f\ubfed\u550d\u0c90\ub934\u1a19\uda04\u9bed\ub6e9\uab4a\u730c\u7168\u1eb1\ue275\u3e76\u01cb\u50a2\u497a\u0e6b\u3224\u6ba9\u3456\ud94a\ue11c\ua4d1\u0677\u6a1f\u728a\u3ad5\u0c78\u28f1\uf7f1\u8e2a\u60e3\u81bf\u89ce\u1af9\u7345\u65e4\ud42b\u6c88\ud959\u9132\u74b1\u393f\u093a\u75ac\u6161\ub794\u67d9\u6cb4\ue05f\ubba0\u1e96\u22eb\u982b\uf7c9\uaeed\u17a8\uf264\ud589\u505f\u69bf\u3344\u93ea\u5510\u9712\u23fd\u7189\u58e8\u05d5\u133c\u4dc4\u5d6b\ud034\u9372\u2639\u71bc\u7a5e\u453f\u16ae\u5c76\ubde1\u76c7\u0490\u9e51\u5bd9\u0bc0\u3edf\ua9e1\uc5f1\uc729\ub669\u77a3\u1d13\ua41a\ud973\ub245\ueed7\u2701\uc135\ucf98\ua6f3\u0e0d\u0b08\uc197\ue0e8\u5e20\ued04\ue655\u1348\u276f\ue516\ueb6b\ubad6\u122f\u8417\u81ed\u8292\ub6dc\u90de\u8ada\u849a\u1280\u42be\ue3c1\u5341\uce8b\uceb3\u4f27\uf6dd\u56f8\u2a06\u8c24\u8b0a\ub4bd", 1875322847, 0xC6E28B41 ^ CryptoVault.mask());
        static final String DIVERGENCE = Shaders.ql("\u9582\ue872\u2d4a\uf917\u2e65\ua7f9\u6b5a\uc1c2\u31d8\ueaf0\u765c\u8dbd\u6574\u80eb\u1355\u37db\u174b\u3ec4\u6a38\ub1c2\uff3b\u44fd\udac6\u3b16\u46a5\ua306\u3bb0\ua261\uf13a\ua9ae\u0f4c\u32b1\u5bb6\ud140\u2a2c\u51fd\u758e\u5db2\ue8ca\ud682\ua517\u2a2e\u0da2\ue303\u3855\ua334\u6257\u1dcc\ue45a\u129b\u8b10\ubcc9\ucc2c\u5001\uc755\u970e\u8a92\ue55a\u55da\u63ef\ua768\u7460\ud065\u6668\uafef\u2557\u5299\ua37a\u14bb\u9068\ue51b\u7733\u37ec\ufafa\uabf4\ubc51\udc5e\u9620\ua003\u4588\u2889\uf22b\u679f\ua462\ud8aa\u199c\ubb04\uab3c\u858e\u081b\u751e\ub175\u6d1a\ud18b\u63c1\ua9b5\u8143\uee30\u69fa\udc69\u8643\ub690\ufdd8\u2814\ud8a6\u176f\u3b54\u465f\u6fe4\u1e31\ud40a\ud314\uc08b\u288c\u670b\ua4cc\ud4b4\uafb8\u127a\u7d32\u773a\u1724\ude2f\uf862\ufeeb\uf56d\ua282\u04c4\u1f7e\u81cb\u1f23\uefa3\uba6b\u0dd4\u6d73\u5dd1\u1f8f\uda62\u9601\u1313\uf291\ub021\u78c3\u8de7\u53c0\u6b1b\udbc0\ud388\u0532\u1844\u55fb\udb06\uc60e\ubd22\u99c8\u6760\u80ba\ub551\uf9eb\u31c0\u8b1c\uaacd\ud30e\u7104\uce0e\uacb4\u0195\ueb1e\uf02e\u906b\u7896\u7696\u5117\u41ed\u1b97\ue14f\u2f00\ua77c\u83e2\ucc50\u6579\u5a7d\u4d11\u4dd3\uf306\u08ea\ud2e3\u7f63\ud9eb\u5142\u3f54\u9d58\u1613\u2379\u1229\u34ae\uc884\uf9fb\u1cfa\uc82a\uc51b\u6904\u156d\u2056\uda11\u0149\u46e4\uaf57\uef10\u3c30\u29f4\u27f2\ub196\uce44\u11aa\u639d\u98f0\u4288\u7a5e\u8e94\u9399\u35f5\uce4a\uf97e\u66cc\u6f78\u3f44\u8d57\u14a2\ub3fe\u1405\uc5e9\uf1d9\u525b\u844b\u0688\u4155\u05b5\ud730\ufb50\u1e3c\u7ede\u9715\u2890\u652e\uad81\u0f5d\ud7dd\ufb15\uc7f0\u9b9f\u11f9\ubc0d\u653e\u2ce6\u88d6\ud184\uf3eb\u1232\u56ea\uccf4\u4341\u2c8a\uec3f\u9e65\u39bb\ucf3e\uedc9\u7784\u3166\uc585\u5a50\u8449\u83d7\uf719\u8ea3\u323d\ucb99\ud921\u33ba\u5d1e\ufab1\u4cb8\ub21f\uc1db\u6759\ufa79\uda40\ue136\u6f65\u238c\u4b1a\ud263\ucea7\ud40d\u7c6f\u3cdb\ud4d4\u4519\ufbe2\udd0b\uf1ad\u530f\u1cfa\u5eb6\u5982\ua1bd\u7dcc\uf544\u3f3b\uc56a\u1a58\u401a\ud53d\u3542\u82ab\ue549\uab23\u5784\u6d86\u7c2e\u8cc1\u2299\ue6ef\ue676\ua1c2\u21c4\ub15f\ub8b5\uc6b2\uafb0\ua0f7\u67b7\u69eb\u0acb\ubbb6\u9268\u3a46\u7362\uac2f\ud64c\u1d4a\u5dd6\ub113\ub355\u409b\u5dfa\uf62e\u0090\u9084\u7431\u798e\u04ff\uede3\u0ad3\u3bb3\u4f01\ub543\u05be\u6c21\ubab6\u79db\u8107\ua60d\u4658\u2243\ubfc5\ud257\u0271\uc720\u6e1e\u8870\uf220\uca75\u8944\u011c\u719e\u8ce0\ub17a\u97d4\ufa73\u4a38\ue232\ue3b1\u91c2\u0a6b\u96d9\u6119\uf1f0\u07a6\uafe6\u4829\u7c12\u9ea3\u2d93\u06ed\ud0af\u9f79\u62cc\u94d4\u47e8\u0c7f\u68c0\u8a84\uf55a\u1eeb\u18b7\ua5b7\ub866\ud7ba\ucb98\u2677\u18a0\u75c4\u3700\uc366\u5381\u85e0\ue1d6\udacf\u5f55\u713a\u3715\u3e25\uf2c7\ubb8a\ud0e1\u6700\ua198\u9b6c\u943c\u08ec\uddd2\u6d67\ucbfa\u6ace\ucc95\ua0cc\u5049\u5685\ubda4\u744b\u088d\u05b8\u0975\u6f14\u35bb\u79e0\uc52e\u3526\u7020\u2162\u4d38\u1ef8\u8b07\u5de8\ufaa2\u1906\uc43c\uf7d3\ua85f\u96c9\ud5f7\u84eb\u01ad\uf108\u2b14\udfe3\u9fc7\u3708\uab59\ua1c3\ud40d\u8afa\ue1b3\u5157\u648e\ub4cb\uf19f\ub07d\u1f23\uf5f8\uc6b9\u863d\ud5ed\u10a8\u1621\u1909\u94bd\ufec9\ub748\u04c4\u2fce\ucff0\u6a85\ud385\u3b8d\uc5c5\ub703\u3ac6\ud717\u20ca\u94fe\ufb90\uc24a\u22f0\u7f93\u3c65\u6b59\u0b7b\u729f\u0752\ud7fc\ua4ee\ubc9f\u6c68\u70d2\u8754\ucdef\ud7ba\u5d97\u6211\ucdda\u870b\uc835\ud430\ude83\u358d\uaefc\u813a\ub70d\u202e\uaf54\uc7f3\u7e63\u2deb\u655e\u2abe\u8c46\u70c4\ucebc\u87b9\uee16\ucf99\ubb2c\ud181\u3c75\u47ba\u0dcf\ue95d\ud0ab\u90e7\u7d53\ud3f2\u89dd\u3589\u9187\uc49f\ude37\u7e39\u5d6a\u7625\u1411\u78bf\ud1ce", -571126318, 0xC6E28B41 ^ CryptoVault.mask());
        static final String CURL = Shaders.ql("\u3d36\u1cb1\uf98d\u906f\u4fc4\u20f4\u4551\u0e5e\u2645\ue4c0\u200d\ubadc\ue83f\u19e2\u11cd\u6ed9\u62c1\u1ac2\u5c06\u55ec\ue649\u6ac5\u33cd\u0c9c\ud267\u1837\u8e5e\u0e26\u4910\u33d6\ubd47\u7057\u6d6d\u10f7\ua04f\u1556\u448f\u32cd\ub091\ue3ca\u429f\ub33d\u58fc\u518d\ubd5d\u573f\u5425\uac56\u5415\ufe6f\uc7fb\uf7aa\uc632\u9e18\ud3e2\u6766\u3d53\uf24a\u7ea8\u37ff\uf592\u50fa\u2304\u24e3\uc45e\u1474\u2691\ube8d\u11af\uc241\ub225\udc95\u6630\u5928\u3a87\u2c6d\u4dd3\u9e59\u32cf\u53ec\u9cf9\uaf01\ub935\udd88\u52e8\u8472\uba10\u4b0a\u2acc\u9378\u0901\uc328\ub0c1\ua623\u51c5\ub4cc\u4f11\ua94c\u1fe6\uf3a9\u6078\uf645\uba50\ued36\u5997\u8b05\u7dcc\ud5c5\u7647\u9aed\ud4e1\u42d5\u8ec1\u0acd\u7be5\ue741\ufd5b\u1eac\uf2ad\u0712\u1748\u1fc7\udb80\uf1b6\ua565\u5e3f\u4abb\u3994\u95e3\u7be1\u9274\u6943\ucae6\u13f0\ucd32\u7703\u28c1\u0627\u1598\u3f17\ua909\u4600\u8eaa\u6c3c\u26a5\uefcd\ubddd\u2c47\u0551\ua9de\u7024\ufb5a\u7bcd\ue921\ufc2f\u0ec2\u047a\u265b\u3c5c\u68ff\uddc1\u5581\u91b5\ub64f\u5c55\udb90\u28dd\u0948\uec71\u3910\u099e\uc1c2\u1ab4\uc165\u6a48\u43d7\ucc84\u6d33\u3f6c\uafe6\u8242\u3fae\ua0e8\u4bd1\u14f3\u3409\u2aa6\u93bd\u9818\u4f61\ud929\u0a9b\uc9d4\ub897\u01d0\u7797\u90c0\u29ab\u330b\u5ac1\uc1bf\ud956\u1605\u63bb\u466f\uca22\u282a\u0b01\u297d\uc7da\u28a3\ue5f8\u8c2e\u6aa4\u8c10\u7812\ue6c2\ub614\u338a\u2e75\u8e41\u74c9\u9037\uad0c\u9d11\u116e\ub358\u4d42\u407d\uffb7\uee6e\u36ce\u5d17\u2aae\ua026\ub309\u08cf\u940c\u8839\ua861\u94d7\uabdb\u5be2\u08a2\ub681\u39ff\u9416\u90f1\udc92\ua43c\ub384\uca84\ucf55\u390a\u8b2d\u22ac\u5146\u4e7f\ud854\u38dd\u8697\uc2b3\ue09e\u3a3c\u1bb6\u3b69\uc77f\u95fa\uf33e\u8c72\u7873\u0ff9\ua1ee\u60a3\u8241\u09ed\ue7cc\u9427\ub90c\u5cc1\ue5ab\uc114\u67eb\ue391\u9c79\u17dd\u0e70\u20d5\u79e2\ucc3d\uf3d5\u16bc\u6629\ud553\ub990\u1e35\u1b39\u7129\u1b3e\u6eca\u11fa\u382c\u3728\uf79e\u2588\u96f8\u30ec\uf7a2\u5d5a\u010d\u4414\u44cd\u6f20\u0152\udce9\u951a\u525d\u6630\uc718\ud1ca\u2374\u03a2\uf554\u7607\u088a\u8e05\u5f99\u9777\u9f25\ua68a\uef8b\u2b84\ue485\u3e18\ub168\u72c1\u8d70\ue71b\u1f2c\uf499\u322c\uab78\u5d57\u08be\ub0b5\u27e8\u9e65\u5363\u746d\u75b7\u9f5d\ud281\u22ba\ua93f\udc7c\u978e\u70bc\u233a\u3449\u457b\ue71a\u7b95\u8ca4\u3c90\uef0c\u2366\uac84\ucf99\ue619\u6f53<\u5e8f\u53ef\u6cfb\u91c2\u2a7a\u3b1a\u878e\u5b40\ud130\ud1b2\u8a2e\ufd12\ub532\u723a\u8e72\u252e\ua37c\u6bf4\u3639\u76a3\u8287\u6eda\u7a37\ubeca\ub4e8\u5af7\u19b7\u8b15", -1584520299, 0xC6E28B41 ^ CryptoVault.mask());
        static final String VORTICITY = Shaders.ql("\ub143\u203e\u4d03\u848d\u1e42\ubf49\ub7fd\u2c09\ube77\u83df\u668b\ua1ce\u74c4\ud6e4\u4028\uda32\u8253\u1bdd\uec61\u7f89\u0491\uebf5\uf7f4\u74ef\ua784\uaaa9\uf31f\ua97b\uae67\uaa8e\u56b0\ub11b\u3308\u9c71\u31ad\u3530\uc1be\u0c52\u2498\ud167\uf9e3\uad1e\uf9e1\u270c\u2f4d\uf1ab\u1492\u949e\uf5e7\u4fe6\ucb1a\u73fb\u0821\u81a9\u86f9\uf56a\ued5e\u4c27\u0a16\ubf29\ua32f\u2c88\ue2ea\ua908\u5e24\u3167\u50bf\uc9b0\u2a9b\ud619\u563f\u7a9d\uee1a\u2593\u34f2\uf35e\ub52d\u2dfb\u293c\u2527\ue3a3\uebd7\u57de\u73cf\uf7fb\u57f5\uf7f7\u0a7e\ubc58\u7fe7\ue952\u79fe\u0aea\u55b4\u1ada\u03c4\uf691\u21e9\u6e54\ue812\ub6f8\ubf62\ua399\ud12f\ue0ae\ud258\u15c0\ubdf4\ud335\uede0\u6e24\ud9dc\u7d7d\u0b28\ua527\ude8e\ud7e3\uf3de\u8dff\u6d22\u2da1\u7624\ud76c\u9b19\ubb4a\u7fe6\ufa37\u9f2e\ua9b0\u9c80\ubdf7\ubeef\ub5df\u75cf\u6bf1\u1b89\u3865\u5c0c\ud97d\uf2b1\u3cff\u4561\uf89f\u39cc\ucd0d\uabef\u5077\ue413\u66f3\u454a\uea00\u5e40\u9970\u4e77\u7eb2\u2a6c\ua732\u9aeb\u5f8e\u5495\u4aa4\u3751\u1582\ua4cd\u7017\u8c72\ub7f8\ud81b\u85a3\uab80\u49dc\u9d05\ucb27\ue291\uf0bd\u29bd\uaf88\u5805\u9442\u32ac\u68d6\u875a\u48b7\u6d05\u7277\u3f3e\u0ec4\u272d\u59b5\ua9a6\u8792\uefba\u1512\ubcc1\ua158\ub81f\ua11f\u853a\uf8ac\u8a80\u49e2\u197f\u434c\u938b\u1ada\uf2ff\u0e45\u9541\u5dd5\uf208\ufc65\u6a0a\u3358\u19e9\ubff1\u901f\ud645\u7cbc\u0c04\u7d1f\u4f9e\ud9d8\u5f19\udf1c\ub66e\u717b\u0bfb\uca95\u43e8\u1067\u479f\u85b4\u5324\u6faf\u518b\u8f9d\u1a75\u177e\u5a2d\u2bb2\u10b0\u410f\ufeba\ueef5\u4523\ud540\u4286\ue20d\ub670\u82f9\u6aa1\u081f\ub139\u97e0\uf198\u03ba\ucdb7\u9abd\ue8be\ud027\u342c\u9b60\u8eb0\u35a3\u21b1\uffb7\ud128\ud706\u7126\ud254\u55b6\u88aa\ud01d\uac87\u31a1\ub73f\u95bb\ud765\u6b5f\u7955\u6134\u4208\u3bc9\ud6f4\ua23e\u87c6\u5ebf\u77d9\u41b8\uda69\u3efa\u6bdc\u2c29\ue916\u6101\u5a13\ub07d\uc38d\u07d9\ue57a\u4dba\u2505\ud526\u5575\u46cf\u018f\u0734\ua019\u995f\u22e1\u2a57\u7fbf\u3753\uabf4\udee9\u81df\u5682\u395f\u04ea\u43b0\u0d04\u4f13\u7dc2\u0e2f\ubd63\u8cda\u97fd\u3438\u7a63\u7d58\uc835\uc7f3\u6546\u5264\uda41\u6811\uaef0\u1d3c\u3f7f\ub837\u6d04\u5a7f\u13ce\ufe9b\u352f\u3b50\u9692\uf664\ucca2\uacab\u3148\uefe8\u75de\u5d6b\u390b\uafef\u1a6c\ud105\u026e\u89b7\ucfa6\u6e0f\u45b1\u77c1\u8c86\u5cea\u9bd6\u1531\ub394\u8b40\u6cf9\u21fb\u023e\uaea7\ua1d5\u3f71\u5211\uf725\ud6db\u957c\u46c7\ua198\ubaad\u3abb\u1ec1\u80e7\u9f34\u2f6a\u2c87\uf035\u06fb\ua1c0\u0af6\u090e\u8719\ub343\uc752\u31af\ub030\ud13c\u80bd\ufda1\ua082\ua778\u2c64\u5b21\u6665\u6516\ua64d\u7c8d\u69fb\u2c26\u651a\u31f0\u0814\u11b0\ub3cc\u983e\u33d3\uc2b8\uf688\u339c\uecb8\u9249\u5c86\uf35c\u0f9c\u51c9\u4a8c\ubb5b\ue573\udb98\udc37\u1dbb\u74e4\u49e6\u14d9\u3b9a\u98e7\u8978\u3739\u8ab3\u8262\u316d\uac26\ua0b3\ua177\u6a0f\uf71a\u986d\u5bd1\u40ff\ud86e\ua4ae\u6224\u3bb1\u3627\u8cd7\u2f79\u072d\u61dc\u64d4\u0b52\u35fb\u4f89\u78fe\ufc90\u80a7\u2fef\ub542\uae5b\u6de3\u9c6d\ubd9c\u8bc3\u60b9\u171d\u15f9\u05fe\u20e3\u9313\u45ae\ua46a\u6218\u63b7\u7cbc\uf9c1\u0cc2\u3f72\ua196\u5e46\u39ac\u8898\udcf6\u39d6\u0615\ued45\u9fb8\u359d\u921b\u3ba6\ue150\u9bc9\u44db\ub851\ufc3d\u923a\ud6b2\u293d\u6aa0\u6d3a\u7382\u3089\u54e8\u5fa6\u63a4\ued5b\ue001\u4578\u5d86\ub440\ua86b\ue9af\u5430\uf386\u4be7\u7eba\ud079\u2d50\ud8f9\u6beb\u8cbf\uc8ac\u5dc0\u8af6\u7631\u59d7\u0a1a\u92b7\ue10b\u7c2f\u6adc\u62cf\uc40f\u1d79\u36f4\u6a79\u91ce\uca8f\u3724\u1c8e\u7e81\u0ac8\ucd66\ua0bb\u3ee5\u0828\u5574\uffed\u5016\ufc21\uf0ed\ua940\u1056\u88a4\uf54a\u6296\u2d36\ub875\u66e4\u1d91\u1fa6\ufe58\ufa05\uc3d2\u92bb\u46eb\u8333\u904f\u470c\ud11b\u997f\u1d45\ua3a8\uf24e\u695e\ud870\u1bb3\ub037\u1495\u5c02\u1ab7\u1225\uf66a\uf48e\ubc14\u0c91\u5cc1\uffcb\u4379\u75ed\u401e\u5e60\uc790\u8b1d\u958e\u33f0\u2dad\uafcf\u04b8\u6af3\u846e\ud108\u0e64\ub7be\ufcdf\ufb0f\ufe86\u80d4\u8b9c\u7ae1\uea63\u37bf\ua395\u17c4\u6911\u46fd\ud969\u494b\u7f51\u1e4a\ud69c\uef56\u10af\u8ff5\u9bc1\u6150\u475c\u165d\u2e70\ub4b2\uc521\u25b5\u371e\u4b03\u1e76\uabdf\ud214\uc77e\ucf32\u7a83\u98e7\uc3a6\u4d91\u3f46\u499a\ufc85\uc8da\ud17c\uf2a6\u5709\ua1e1\u406a\uecfd\u27c4\u1d9d\u7e6d\u58f3\uf00e\u6626\uea55\ud7fa\u923e\ua0f8\u1641\ua23e\uaadd\u88f3\u2eb6\uac7e\ud059\u2ae9\u4124\u70aa\ue54d\u0f97\u6d69\ucffb\ud8fd\u5676\u6336\ufa35\uc646\u475c\u144c\uc95a\u14c7\u962a\u7d5b\u3075\u8f60\u1d48\u680b\u9aeb\u3613\u5f50", -208397862, 0xC6E28B41 ^ CryptoVault.mask());
        static final String PRESSURE = Shaders.ql("\u4af7\u2451\u4deb\uf22b\uf16d\u6297\u60b7\ue147\u59d6\ub5bf\uccbe\ue803\u99f9\ubba5\uad05\u9fcc\ub0f0\u2948\uaf72\u25b1\u3a0d\u65af\ubcfc\u339c\ubeb6\u6355\u0978\u2358\uc427\ud5e5\u556e\u31a0\udcb0\u4450\u3df5\ube8b\udb53\ue75a\u165b\u857c\uf20d\uaf97\uf435\ue33e\u917b\ue442\u930e\u0ae2\ucf2f\u9887\ua899\ue72a\u32ec\u14f7\u762d\u38e5\ue687\ud517\u602e\ub536\u2785\u558a\u5d55\u38bd\u8e8c\u00d1\u2ff9\u94d3\ua763\u89a8\u3a5f\u2381\ud8a9\u82a5\u5814\u1966\u4232\uc708\u3e00\u845b\u4ea7\uacca\u3509\u6c57\u60c7\ua979\ub13b\udbf4\ub55f\uc76d\uee30\ub1b4\ue33c\u6461\ua996\u5966\u8a45\u99cb\u6e86\ucd3c\udc16\u0a75\ue834\ud4a9\ub516\ub747\u6799\u53b5\u25bd\ucd3b\u49ae\u9c95\u6369\ube35\ua085\u1ba7\u3743\ue87d\u7bcf\u7bd8\ue9d7\uda62\u7eb1\udfa9\u6826\u8c9b\ue772\ua78b\u172a\u185c\u4619\u5bf1\u0a05\u76fe\u46bd\uc75d\u030c\ube73\ucdc2\u466b\ubfee\ua1b6\ufae2\ub947\ud573\u9de2\uf02b\ud6d3\u9cdf\u29fc\uf6f8\u8724\u48bf\u24cd\u4626\ueb07\u4ff0\u64ed\u3433\u3627\ue88c\uf43b\ud909\ue60b\u9473\u1a47\uacde\u14d5\uc46d\uca1f\uc274\u352a\ud94c\ucbee\u9fcd\uab7a\ub914\uc9f7\u65a3\ucde1\ucde7\ueb22\ua48d\u0640\u6a81\uca15\u2a53\ue50d\ufcef\ubfce\u7808\u2c17\uf5d5\u29f4\u1e32\ua5a2\ufe25\u21c4\ub789\u6300\u5b85\u7a47\uec24\u6560\u69de\ucd09\u1cdf\u3e26\u2b88\u61ac\u96e0\ua488\u45bb\ude47\u427e\ud191\u50d0\ua50b\uebe6\u35bd\u2871\u1ff5\uec46\ucc6e\u4534\u5af7\u0644\u2f89\udeb2\uf0a7\u203c\u2084\u17c7\u6af2\uaad1\uf84d\uff6b\ued45\ub910\uac55\u9fcb\u2241\uc187\u8f26\ub172\u2c6d\u1bed\ue767\ub97b\u040e\ua41f\u4a0a\u5f8a\u995a\uf02d\u426d\u176a\ufbfb\u11d3\ufb0a\u2d2b\u460e\ubd1a\uc451\ua0ba\u1033\u1b77\u93f2\ube15\u7076\u7476\u34b3\ubb98\u2aaf\u4fec\u19dd\u7237\u4baf\u16e5\ua028\ubb9a\u056d\u9913\uc6df\u0afe\u5237\u5fd1\ubf4f\u48df\udf69\u9311\u115c\u450b\u1a0a\u2eeb\u9742\u0f58\u06bc\u9b83\u1f04\u66a3\u0e72\u8244\u1819\ue4e3\u4879\ub61e\uc2a9\u6012\u3fc9\u8914\u500b\u7509\u59fe\ue70d\uf96d\u6558\u2da1\u1c29\u6dba\u7887\u4eae\u02f1\ue39d\uc1b7\u5c1e\ua5ba\u7f44\u7d93\ua832\u818e\uca09\u1445\uaa77\ue235\uda7c\u2f35\u855c\u69f6\uff7a\u1141\uae71\u899e\u905d\u2fc7\u3ee8\uef30\ue179\u44bc\u5bcd\ue3a4\ue081\ua32d\u2a28\ub4c9\u9aec\u98cc\ud1a4\ud839\udb55\u812c\ue907\u06eb\u7fbe\uc7d5\ua267\u06f8\u19c7\ucc55\ue193\u40af\u95de\udd4a\uaff4\ueaa7\uf47a\uf2cc\uad6a\u8d64\uba65\u2ea3\u483c\u6253\udd27\u985a\u5c43\ua9e4\u9863\u9850\u8aa4\ud52a\u8c91\ua317\u72d0\u01b1\u1409\u3607\u2ade\u5d59\u2085\u6608\u2aef\u28f9\uc94d\u2dab\u69ba\u2c56\u432d\u1003\ub18b\ua530\ub3a1\ue3ec\u32f7\u6dd4\u0fd5\ubeca\u72b4\u4159\u7c19\u2ff8\u3431\uba6a\u1da8\ue845\ua8cb\uc755\u3fe2\u3478\ue096\ubffe\u13de\u904f\u3a58\u0b62\ua099\u4fae\u7086\u72fd\ucba3\ue68d\u5880\uc50b\u07d7\u6b12\ufa0b\udf3b\ueb95\uee2a\ubf08\ue9d4\ufe4a\u093a\uf9df\u21e3\uea97\u036c\u90ac\u6110\u00c5\ua7ef\ud99e\u324d\u32f9\u4ab5\ue51c\u1d50\u3df7\u651f\u5b9f\u8eed\ucbed\ua181\u5dab\ua88a\ua660\u849a\u0f85\ud2ff\u2ee6\u919d\u2fce\u1d9e\uab52\uc608\u7312\u15e4\u60ad\u2270\uc54a\u406b\u89ca\u0711\u7018\uc584", -849276629, 0xC6E28B41 ^ CryptoVault.mask());
        static final String GRADIENT = Shaders.ql("\u6af5\u09a2\u09c2\u626c\ub949\u14d2\u6cbe\u24e6\u41d6\u2bc4\uad03\uc97b\u3644\uaf3f\u3878\uc648\u4eb0\ub2de\u08ba\u265b\ub42a\u3e75\u9637\uff41\u259e\u7d56\u0c81\u9b04\u0b9f\ub557\u1ce8\u365e\ued82\uf098\u9260\u1b62\u6578\u88ed\ua707\u2555\u51ec\u0757\ud042\u6106\ubeee\u3c79\u7b48\u0158\u9ae0\uff3f\u243e\uedbb\u01fe\u9b75\ub5a9\u9b6f\ue7d1\u5eca\ud564\u3d64\u42b0\uf86e\u401c\u51be\u6303\ubfe8\ua916\u1966\u1b6f\u692c\u9b08\u6f15\ue65a\uc789\u92c6\u0ca6\ud18d\u26cf\ubcb3\u8dd0\u86dd\ube2a\ue2fe\u3586\u900a\u3bb7\u5d78\u8fa3\u2acf\ue405\u398c\u78a8\u7701\u9770\uf3e9\ud8fc\uce41\u21fa\ue71e\u8e12\uec91\ud067\u8ab1\u57f0\u7da4\ubbaf\uc293\u866b\ude1f\u64f6\ub155\ud634\u4798\u7aa9\ub6c3\ue679\u3b44\u905e\ueb32\u759f\u38ff\u6dea\uf871\u5793\uddcb\udd83\ua26a\u0d4d\u6b9d\u11ec\u6547\u5758\ub186\ucf01\u807c\ua7c3\u20f5\ub430\uacc4\u3899\u8e90\u7325\ub601\u0505\u59bf\u1c80\u8ea1\u6a61\u3870\u821b\u2f80\u7498\ufd2e\u9f32\ufcb5\ua66a\u996a\u3c01\u60a3\u5a33\u6dfb\u3741\uf6f6\u3aa6\u13ee\ub697\u2027\u6e3c\u857f\uc7d2\u9d1f\ueebb\u91e7\u1412\uc1c2\ub9be\u7678\u930e\u9388\u760c\ub5f0\u38de\u3aa8\ufdcb\u9306\ua0da\u10fa\u2a60\u1608\uacfa\ufaf0\u458d\u11d9\ua6a6\u4c9b\ubd2d\ud88d\u2dd0\ub9df\u521a\u0f22\u2dc0\uf173\ud6a5\u561f\udbcf\u62b2\uca36\ude9a\ub4f3\u5a50\u2b81\ub1e4\u564e\u7278\u1402\u96e0\u8917\u4f7f\u6439\u2184\u303e\u3af8\u2740\udedd\u681b\u07e7\u4960\u5491\ud3ba\u7353\u61b9\ud5ab\u0a09\u5b6b\ufc30\ub839\ubd23\ua8cf\u2175\uae9b\uc504\ubba7\ud3b5\u448c\u3b9e\u6ed4\u841e\u9ce6\ube94\u2dde\ud5a8\u7df8\ua0c2\ubd69\u0e8a\ue335\ue585\u468a\u2d09\u10ca\ud1cf\u3835\uc994\uaf87\u974b\u0add\u8c74\uac6c\u1714\ud7a2\u3409\u041d\u38d4\u3907\u5fc2\u6dcd\ud511\u88c1\ue93f\uc389\u6645\u873f\uca30\u1e7c\u6678\uca57\uc256\ub0e6\u4dfc\u07e2\ua009\u16bc\u2324\ue879\u5d78\ua30d\u86af\ub509\ucec2\u9c47\u512e\u8ccd\u8cf7\u7179\u907e\ud2fc\ue68b\u79bf\u1f98\u0ab8\ub2b2\uf278\u4fb9\u8c07\ucf04\u8c0e\u842a\u0360\u0335\u97e2\u9538\u199d\uf6c2\uaf73\ufcfd\u843a\u2f4b\u425f\u5ab4\ufbe3\ue6e1\u9855\u7e9a\ua87c\u661f\u2ef2\uad38\u86cb\u3795\u618f\uc99c\u1dcd\u6b59\u2772\u8ab8\u35c1\uf9d2\u370f\u5757\u7fbf\u98c8\u076e\ue589\u2f06\uafdf\ue2e9\u034f\u9059\u3568\u866b\ubcf2\u1491\ud95a\ueb89\udf16\uaefb\u4b78\u4b39\u5709\u84c0\u2425\u20e0\u7e68\u031d\ua093\ud585\ude4e\u501e\u1b86\u9170\uccb0\u0a78\u1547\u5744\uca9a\u5427\u271d\u8fa4\u026b\uc0df\u7bb0\uf173\ua0a9\ub09e\u4e5b\u8cd7\ud200\u367c\ubb93\u712a\u0179\ue7cb\u438c\u7136\u14be\u64d8\u720a\ua8e4\u5887\u5d72\u8cbe\u2287\u7188\u2dd9\u1efd\u424d\u33f1\u649f\u4d8a\u98ff\ua2e3\udb9f\uc953\u2e54\u5270\u375d\u1dc3\u1315\ua7d0\uc180\u27df\u108d\ud79b\u3f70\u6494\u6214\uc50b\u4a4e\u3569\u1c2f\u7367\u4e73\u32e8\u8b5d\u3d78\u484e\u32f8\ud294\u9fe3\u13cd\ue8a8\uc240\ue8bb\u3801\ua134\u3640\u3a0a\u7bfd\u8d88\u1322\ud2f7\uf367\u95e0\u9fad\u34f7", 974501248, 0xC6E28B41 ^ CryptoVault.mask());
        static final String BLOOM_PREFILTER = Shaders.ql("\u5245\ue2c3\uc3e4\u2ef9\u0fcc\u5d47\u7883\ua889\u6fdb\ue3cc\ub4ed\u0c38\u2985\u391a\u37ee\u0bd0\u4500\u4f04\u0801\u4dc6\uad49\u729e\uef52\u1a7f\u6b8d\u4d2c\u6cba\u2d7e\ud881\u04f6\ucc14\u8cc4\u9cda\u7264\u9003\ubd93\u98d7\u0843\u2e47\u69c4\u5a88\u53fe\u40db\u99cc\u75a4\ua51f\ufbee\uafe8\u699f\u9348\u026b\u4584\u55b4\ub11c\uce8e\ub681\u029c\u2c40\ucb15\u9183\u175f\u4186\u2774\u6028\u2a63\u7e77\u9513\u79b5\uc2af\u80ec\u09d8\ue24c\u3d43\ude16\u4d66\u09a0\u4806\u1a31\udad7\ude00\u5423\u1866\u20cf\u4b0a\u10eb\u4ed6\u9fb4\u1bcf\u5ebe\ude2a\u5f96\u3955\u2a69\udff3\u6adb\u5acb\u8bd8\u92bc\u4730\u91e1\u8ef1\u843b\u92f0\ude80\uc0ff\u1776\u05d8\u33bb\u0d5a\u08a1\ue1b4\u5643\u8781\u2b88\u9901\u98b3\u5663\ua93c\u90cd\u77f2\u65c8\ub130\u17bc\u58fe\u6d93\uf445\u2275\uff55\u55d3\ub612\u9de1\u24ab\u2ed2\u6541\u9d38\ub459\ub8f2\u6aa5\ua934\ucab0\ucfa9\uec3d\u0b28\u18ff\ubf49\u5d9b\u85ff\uac78\u5f6a\u7c21\u163a\u2b2c\u3968\u75d1\u00c9\uf239\u242a\ub196\u5844\ude44\u0537\ua90e\u4d0b\u4dc8\uabd3\u85b8\uf2f6\ue95e\u9a17\uc739\ucb79\u2454\uc3d7\ud0ce\u0c97\u575c\u6419\u2888\u89b4\u2a1a\u6459\ua81e\u247a\ue2b1\ueeb6\u405f\u1c2c\u6feb\u9b69\ub25d\u696e\ubfbd\ue94d\u1e26\uf3c0\u5bbc\u655f\u2d73\u81c4\u322f\u2019\u489c\udcc5\u7397\u1c26\ufaa2\u8c60\ue21f\u3047\u8605\u2cb8\u20ad\u2974\ua615\u176b\ucc03\u542b\u60f3\u6d55\uc0a2\uac56\u3ea9\u9665\uf293\u0e72\u51e1\u734a\u12f1\ube32\u5c8e\u33ba\u986d\ub96b\ua56a\ue266\u6e2b\u94a4\ued94\ua779\ub325\u6f6b\ue671\u7b4c\uee3b\u63b9\uaf7f\u0d6b\u0c64f\u7568\ue8c9\ufe54\u51f9\u9044\u3009\uf1a8\u2f0d\ud499\u4fe5\u267f\u08b0\u2050\u4863\u5681\ua4c0\ub493\u4dc3\uc0a1\ufa41\ue263\ufb56\uc154\uce7b\u5dda\ue75b\uff73\ub756\ubd10\u32d2\ua958\u85b0\u6603\u2e92\u6e18\u6ae3\u62b2\u6242\u610a\u03ef\u57c0\ua941\u65d6\u46f4\u2d5f\u12a2\udf01\ufc95\u322e\u4a05\u134e\u416b\u6b86\u0fba\u6f7c\uc5ba\ub3a2\u43d7\u87a2\u5a4e\u0d4b\u4a22\u56ef\uf90b\u6f87\uc397\u37f1\u6a69\u1fcc\ue9a5\uc61d\ued65\ue816\u4d07\u1115\u6a08\u2605\u7e6b\ua618\ud6fb\u463c\ua8ec\u31eb\u9b24\uc61a\uf8b0\u84fb\u1e8a\u4487\u1165\u8d2b\uaf8c\uc54d\u2451\ua40c\u8f48\uf563\u8285\u6d9e\u45f6\u925f\ubb2b\u0d6b\ua4ff\u98af\ud696\u6632\u9f86\ud11f\ud92e\u634c\u970a\u75fd\u015f\u2a8a\u5661\ubb8f\u2d9a\uc8d4\u5b74\ue5fc\u8ce1\ue15b\uc7ad\ua7d0\u8b17\u8e74\ucc72\ue788\u3668\ufc18\u2ffc\u874d\u5f42\ubd7b\u06dc\ufa14\u2a4a\u1c2f\u8553\uf847\u7218\uc29e\ua8a1\u6ea8\ua1be\u7ee0\u0f3c\u8d20\u5d9d\ua0b2\u790f\u1e6f\u05ca\uc359\ued04\u2d58\u935b\u4c6d\uf786\u0749\u256d\u7363\u2a51\u7661\u4187\u8869\u4c5e\u330c\ue14b\u52b0\u71e7\u6bdf\u48a1\u906c\u376a\ude69\u95d7\ue4a3\u9519\u3582\ue4ec\u12e1\u5275", -1747872617, 0xC6E28B41 ^ CryptoVault.mask());
        static final String BLOOM_BLUR = Shaders.ql("\u7e5e\u7352\u8177\u903a\ue57f\u5d33\uda27\u7a25\u3ee9\u07de\uabae\u3faf\ue622\u15b2\uab4e\u1982\u9e47\u782d\u53e6\uc18f\u1e7a\u322f\u7be7\u1e56\u1f72\ue5e4\u3fa9\uf0fe\ue336\ubd43\u87fa\u5fb6\u99c0\ub38a\u6824\ue34f\u4fef\u2764\u16e3\u3935\u03b6\u00d5\ud3ec\u07af\ud6f1\u8352\u18a4\ue8c9\u4d8d\ude8e\u0eed\u84cc\ue75b\u1b66\u353a\u95ae\u31c0\u211e\u76bf\u9676\u55e2\ufab9\ubfea\ub77a\uc0ab\u5388\u92f7\u55af\u56d8\udab0\u7e36\u19c6\u2cd7\u99cd\uda3c\u65cb\u4efd\ub39e\uf09d\ud974\u55ef\u9811\ub753\u33df\ud5ed\u9ac9\ufe85\uec38\u6027\uf8d8\u8dc2\uebcf\u270d\u63da\u8083\ucb43\uf63a\u51ec\u536a\ude62\ua4f0\u1ec3\u17b8\ua679\u396e\uac34\u8398\u8c9a\ubbec\u6ebd\ub080\u7f03\u2894\u605d\uc1c6\u2b9b\ud13d\ucdbf\u8f0f\u39b1\u5ebf\u06b4\uede1\ucc5b\ud921\u3afe\u82cf\u2745\ua0dc\ubb3a\u642a\u62b4\u1a7c\u8335\ua522\u1782\ubfce\u181a\u343b\u2a8c\u3fba\u2580\ud13d\uc393\ub9f0\uba69\u96fd\u8f4a\ud4cb\u7571\u3d48\ua2df\u8952\u8676\u5f43\uec2a\u1383\u9425\u23cb\u994b\uc764\u752e\u6628\u2608\u0fb8\u65f8\ue3c9\u8612\u0d46\ub3e0\ua623\uf156\u8952\ud660\ufd0a\u9c8c\u7a16\u5734\u5b32\ueba2\uf858\u456a\u84f8\u02f6\u84dd\uc59d\ucdb2\uc09d\u97e6\u495d\u579a\u9042\u343a\u8da8\u3f52\u3fb7\u33e2\u1690\u1571\u6b03\uc03e\ufc99\u7fc2\u9099\u5b59\u2824\u2262\u0507\u8590\u4b03\uc5d8\u6eaf\u80b7\ubcba\uc60b\u48a9\uffab\u18cf\uf955\uf1d9\udce2\u6824\u49b8\ud675\ub631\ue98c\u2bcc\u3c09\uffaf\ue5fe\u2139\uedd5\u2d4e\ua2de\u2bd1\u4e24\ue0f3\u4303\u7779\udc05\udc54\u912c\uae25\u8dd2\ueeca\u6219\u0ed2\u5348\u9ed3\u32e0\u65b2\u1c6c\u56a0\u6da8\uff3e\uafec\u98ef\ub981\u54c0\u00e7\u84f2\u7dd6\u9b75\ub897\u1f00\u6e8f\uc737\u7027\u0433\ud320\u465a\uae7f\ud7d4\ua382\u52ee\u8196\uc9ef\ud5aa\ua9d8\uca78\uc2e2\ud1e6\u9c88\uece2\u03fe\u617e\u0d15\u8922\u7d39\u39af\uc44b\udb1b\u7a0c\u8116\u09a8\ub513\u137d\ufa7d\uf445\u85ac\u38ed\u4fbc\ua549\ubad4\uac82\u13ad\ucb3d\ub312\u7947\u3b29\u7c51\ua03c\u6ed9\u320b\u2ade\u2ac7\u0d0d\ucdb4\ucff4\ue2f3\uf760\u712a\u5126\u36e7\u674f\u5560\ua30a\ua628\u36da\ub82a\u6162\ud664\u7921\u647d\u9361\ufb06\u69d7\u5951\u5f45\u481b\ud6d5\u4a86\u71ac\u01c9\ua4ef", -629556951, 0xC6E28B41 ^ CryptoVault.mask());
        static final String BLOOM_FINAL = Shaders.ql("\uab22\ucd9a\u7447\u1a21\u7994\uf8df\u41bf\u6a03\u7a5a\u6303\ufc01\ub33a\ue199\ud329\u7aa5\uaa78\u88b8\u80b0\u0b37\u8303\u3977\ud76f\uf8cd\ufc5c\ua420\u5a44\uba33\u1a3f\ud9e9\ud8c0\u9673\u89b6\ufba2\uef4e\u7e6f\u1967\ua755\u5276\uca79\u8f4d\ua15b\u1020\u856e\u433a\u03f1\ubceb\u323f\ubf8a\u8bab\u7be9\u16b1\ud76e\uc1e8\u056b\ud0a5\ue5ad\u944a\uaa68\u001b\u4d0a\u14fc\u51c5\u49ba\ua4a9\u3fa9\u1109\ue554\ua6a8\u6caf\ue017\uab95\u4d05\u3512\u77fd\u84fd\u0b43\u7bb1\u8e63\u7ccb\u09a6\u2595\uc3b1\ubf8f\u9e9a\ud26a\uf3f2\ued09\u51cd\u2990\u0e7a\u88e7\ufaa5\u3aec\u9ceb\u66e3\uad5e\u0942\uaada\uc34d\ufea2\u2db7\u1890\u295f\uceb5\ue149\u47ac\u2fd0\uf9dd\u7668\ud05c\u4853\u74d4\ue6d8\uc51e\u7908\u5587\ua557\ue281\ue323\uee63\u7761\u9a0d\u29db\u6755\ua09b\u9310\ud092\ue692\ud46c\uc517\uf63d\u4f2e\u0675\u7f61\uebd6\u9208\u5618\u103a\u6a72\u49e5\u2c38\u5ccf\uf096\u1f18\uc10a\u5827\ue4d8\u1a41\ubfa0\u1bac\u20c3\u83b0\uf785\uded2\u749b\u67ac\u1685\u1a30\u5806\u4558\ufa30\u8c02\u5395\u4c73\u4933\u8f8e\u209d\u4fa6\u8212\u9250\u99f7\uf312\u9d63\u67c4\uddf4\u28c3\u760e\u4c9a\u925d\u3cea\u6d31\u5d73\uf834\u3849\uacb3\ue981\ubf08\u5f21\u2215\u2237\udb13\ucd05\ufe8a\u5d3b\u5187\u0bea\uf7ac\uf0c9\u4799\u5baa\u09be\u2c53\u8bfe\u5788\u3912\uc671\u30fc\u6cca\ucf25\uae34\u43d0\u88f9\u72c4\u61aa\u40ee\uba44\u80db\u98c0\u0670\uf952\ub8f0\u2fe2\u4488\uc78c\u3d44\u7927\u0bec\u87cf\u39da\uebde\u6411\u84f5\u5789\u2138\u21d4\u2e0c\u1214\u9c0f\udd60\u2d69\u3d2c\uf49f\u88a1\u0a34\ud06d\ua363\u5edc\u2d77\u8e1c\u14bd\u4b8c\u1ea5\ud2b9\u2a1a\uc839\u4b95\uff71\u2462\ud033\u4acd\u930d\u6f46\u90c7\u7970\u024b\uffc8\u3969\u260d\ue8cb\ubd89\u53d9\u7dd7\u1c05\u4ad8\u2500\udbd6\u8420\ueec2\ua381\ufe4e\ud28b\u5a4a\uf7c7\ueccc\u9ae2\u21b7\u8bc9\u110b\u352a\u07a5\ubbb3\ubeff\u87e8\u374f\u480b\u7172\u9e44\u40cf\uff23\uf0b3\uca2b\uf632\u3918\u2ed1\u7a1f\ua8f4\uac82\u824e\u5079\u5573\ue6b6\u2226\u7b8b\u333e\u9452\uc6c5\ua224\u0db4\u08a0\uf4df\u1c1d\u4527\u46de\uf180\uf9a4\uceb7\uf4fc\ud2e3\u2fa2\u9a98\ucd3d\uad60\u5474\ub991\u09b7\u8a72\u59fc\u9b6a\u6e3f\u3206\ua83b\u4d11\u9130\u0ffa\ub831\u3041\u7e9b\u424f\u6a3b\u8af2\u7d38\u63de\u6409\ud9a4\u917f\u389f\u7c64\u2220\u3653\uf834\u5483\u67b4\ube83\u3473\u1617\ua8e1\u65db\ua9b1\ufffa\u726d\u40fa\u4141\u0f4b\u038a\u13c3\uf74b\u7b28\u01ab\ue73c\u6174\u5d18\u5595\uadad\u8420", 1009237498, 0xC6E28B41 ^ CryptoVault.mask());
        static final String DISPLAY = Shaders.ql("\u04f5\ubc3d\u2a32\u1e3c\ub2e0\u821f\u8db9\uf096\uf4a9\u948d\u8ba4\u8160\u81e2\ud68e\u5b27\u0fba\u820e\uff67\u3cc5\u3b3e\u2119\u1ba6\u8c72\ue89d\ub35f\ue8fb\u5893\u61b3\u0a0e\u2ea6\u637c\ue2d1\uce20\u8efd\uc6b6\u0fd0\u1373\u8fbf\ud3ed\u885b\ufda5\u2cd5\u0274\u8284\u2f6e\u65e2\udd04\u9c27\ub2b1\u19c3\ubec1\u0379\u155d\u98c8\u1f30\u8647\ufaad\u3434\u65dc\ue7d9\ud415\u47de\u4ba6\ua028\ubc37\uf688\u14c9\u2b3b\u695e\u8031\u4cc4\u74a0\u7d7f\u38e5\u751f\u83e9\u0672\u02e3\ue1ac\u536c\u91e1\u2541\u6731\u6e30\u7b64\ubb0a\u384a\ue79a\uf367\u711c\uaafd\udfb8\u5f1c\u6c90\ud21f\u3e0f\uec1f\u52ac\u4e4a\ua368\u33df\ub338\uf180\ua686\uceb2\u807e\uf3c1\u1f85\u3c07\ua638\u1302\u13ee\u5aa1\ud03f\u6ff5\u6d45\ube73\u3e80\u8e8f\u4a02\u01d8\ub1f7\u9d4a\u1482\u5cbf\u82e8\u6b4c\u5083\u6d45\uf0c4\u5fa4\u7fc8\u2afa\uc424\u8dcd\ub5da\uac21\u4d0b\uad77\uafae\u0658\ucf66\u7ccf\ua8a5\uc3a0\uec66\ub345\ufd5a\u6d6b\u707d\u4de5\ufc3b\udf1a\u678c\u05f1\uce55\u5540\u9e3c\u7df4\u35d9\uc6b2\uc8d1\u01b1\ufcd5\u01c6\uf2b3\u4472\ua8f9\u397a\u2a0d\u02f7\u86ee\u2f2a\u2cdb\u2e84\ud29f\ucfba\ud275\ufc29\u4d32\uf543\u0acf\u79e6\uea13\ueaaf\ue988\ub8f4\u0da6\u9c22\u7d07\u24f7\ufaeb\u8fe1\u982c\u4885\u9bed\u40bd\ubcd0\uc276\u4eed\ue527\u160a\u26e5\u4646\ue919\ubd3a\u5e7a\u88f6\u4c76\ubed3\u93ce\ub2ae\u8b7c\uc216\uf8be\u7ae8\u64e5\uc42f\u9921\ua863\u550b\u5cc7\u8127\u0c10\u64ae\ua35e\u3c54\uac34\u00be\ud062\uea69\ud975\ufa90\u333f\u6251\u9e73\u8f52\uf916\ube90\ua511\u77df\u7bf8\ue01d\ude49\uab3e\ubfd9\u8b6d\ucd70\u2a83\u71fa\u60af\uca11\uf387\ub341\u1269\u12a0\u430f\u42dc\ue406\u271b\u3ff9\u6cc5\uf055\ud550\u5c77\u616f\u3c5d\u42ce\uda1b\ua0dc\u4fd1\u180c\ue99e\u9939\u6829\ud466\u362a\ub32d\u4371\ufcc1\u995e\ua6b0\ua1b6\u98cd\u1b7f\u04b4\uc035\u01ac\u2dcc\u2fa9\ua7e4\u3c38\u4833\u0375\u1f14\u6afa\u5be3\ua379\ue290\udf05\u1cd2\u1779\uca4e\ub909\ud18d\u301e\ude46\u60a7\u8977\ua3e5\u08e3\u21c9\uc776\u0705\u1f06\ucc47\u0df7\uaf83\u3dc4\u47ec\u9512\u2c8c\u98c2\u0b08\ud88f\ua27c\u7f5c\uc1db\u597b\u959f\u83df\ub2b7\ud634\u75f3\uc7d5\uade0\uf8c4\u776c\u42fb\u365a\udca0\u30b3\uf543\ub8b4\u7061\u306b\u5e68\u9200\ued6c\u1788\u15c6\uf5aa\u2bc1\u3ea8\ud56e\u0d6d\u71b3\ub28d\u09f8\u6978\u223d\u65b0\u6e40\ue7fb\u6f28\uea7f\u2810\u2ebb\uc26c\ud32d\u1e6d\u60ae\u1797\u2f41\u32a8\u1a70\u5cc6\ue2fa\uea84\ue544\u9bb7\u4c9e\uc604\ub5d3\u8e7b\u2b79\ud70a\ua15c\u182c\u9c81\ubf80\uf7a6\u00b2\u1fda\u811a\u3e82\ua1b1\ucb3e\u454b\ue842\u2d6e\u658c\uf99e\u1788\ua464\u5ecd\u7e15\ub585\uae2d\ua8ba\u2cfc\u6941\u6900\u0125\u570c\ube4b\u46a6\u9b48\u4310\u611d\u1637\u8510\u12da\u6bbe\u6b87\u9fa9\u599b\u4993\u3fb2\u32dc\uaa5c\u73a9\u9bb1\u6a0b\ua161\u3aa6\ufde7\u2afa\u1214\ubc24\u37a8\ufbc6\uddd7\u4b40\u3046\u7695\u9f79\u051f\u1276\u16a6\uae3a\ua417\u77f3\u0e91\ua785\u3b7e\u2da7\u7276\u4ed6\uc847\u2590\u4924\ufc6c\ua9f0\uc478\u414b\ue53a\u8883\u05c8\u1a73\u7e6c\u1d31\u8a85\udfdc\u7105\u0d82\ua808\uacd8\ufd4a\u8c0d\ue5fa\u8c11\u065c\u8f02\u47c0\u2158\u6250\u44ad\u97ff\ua507\u94e1\u12dc\u8045\u022f\u59c0\u28a1\u74d2\u7f90\u00cb\u286f\uaeaa\u0c1b\ue169\u4e40\u3f32\ufc86\ucc2d\u9fd6\ucc5d\u65ce\uaa79\uc15f\udd78\ue1ea\u91e8\uff4f\u12cb\u4f9f\u5366\uda41\uadd7\ue7bd\u9578\ueb42\uf5f3\u05c3\u9f13\u73bc\uae28\u9828\u230e\u2bbf\u93e2\uad1c\u2902\u6c8d\u3b67\u48ac\u496a\ub86c\u9cf3\ue21d\u2507\u8797\u8d51\u6295\u09b8\u8adc\u8bab\uebd0\u85cb\ub00e\u3019\u8cdb\u7d25\ueade\u12b8\u7228\u6887\uf099\u75c6\ua87a\ued9c\u3a2c\u9108\ue78d\u43e8\uc615\ub544\u7efeQ\ue730\ud9be\ud049\u2a43\u467b\ub595\u77cf\ud234\ua3bf\u626e\u1674\u23b0\u06aa\u64a2\u6a66\u208c\uc7ee\u347a\ua522\u3e85\u6858\u0c52\ua3f7\ue02a\ube89\u7907\u7b48\u9269\ucf24\ubb16\uf431\uacc6\ue291\u9eaf\ub656\u7d41\u7d7b\udb31\u01de\uc781\ub31c\u240f\ue48a\u8d6c\uc6da\ua566\u5fbc\u3a41\uf50d\u4529\udcab\u2d8c\uf183\uf529\uf098\ud812\u49de\udd66\u86ed\u95e6\udda1\u1924\ueaf0\u8d67\u2553\ub8ce\u7691\uff01\u774d\ua788\u2356\udafd\uabcd\u92bc\uce27\uc5b9\ua192\u7385\ue015\u2f29\u8201\u4170\u509e\u5e03\ud7ee\u57ba\u5cf8\uf381\ub003\u427f\u7b4a\u6acb\uec5bb\uf5fb\u3bbd\uefab\u40e0\u75c5\u33ee\u2336\ue409\u7abb\uce1c\ub473\uf191\u058a\u8743\u8605\u0a2c\ud8e0\ua357\u95b1\u0716\u33c4\udc21\u23e7\u6fe0\ua724\u47c0\u6b78\ufdc4\u482c\u5a4a\uf77e\ue1ca\u0810\u6091\u4d00\u317f\u3e62\u9f7b\uca9f\ud8d3\u32fb\uda80\u9c9e\u0610\ub917\ud77c\u7bb6\ua999\uaeb9\u93b8\u322e\u889f\uac7e\ubcfc\u8882\u30c6\u3d63\ua8f3\u2c90\ue884\u64d7\uc95c\u41d2\u99ac\u4bcc\u1a57\u28c7\ufde5\u811f\u8dc0\u2ec8\ud9a3\ud33d\ubdc0\u5213\u1706\u047c\u4fd9\ufcde\u54ba\u842e\uad03\ud888\u4566\u7836\ueec4\u0e24\u9693\uedb3\u8419\u0b65\u4bcc\u6a75\ud1f4\ucee2\uf85f\u45af\u8910\u6e8d\ud4c6\u5fbe\u7204\u8f66\ufb07\u7b2f\u2945\uddba\ua0d7\u572a\udc67\uc563\u025a\u036b\u381e\uaed2\ue33b\ue5a6\u4851\u6dd2\u7f5e\u5349\ufbf5\u979d\u7562\u1a23\u86de\ube06\u6cef\u1130\u2e17\u49af\u96dd\u420f\u67aa\u8402\u8e37\u91d3\u0b60\u8297\u1ad4\u9445\uafb2\u0c10\u4a07\u7bba\ua6df\u662d\u930a\u0c99\uce54\u1fb5\u808d\ud457\u6534\u6027\ueef8\uee76\u5772\u13d8\uea17\ub38b\u8a1e\u6bfd\u7b08\ue063\uf6df\uf846\u23fa\u8727\u3a1c\ub77e\ud1be\u3daa\uda15\uc430\u105c\ucefb\u5669\u6225\uad84\u2234\u1293\u3e15\uc933\u5225\u6c6e\u639e\u0c13\u46fa\u6a90\u235f\u9628\ue721\ua2a2\ubce2\u4089\ub22c\ub8c9\u37a7\ue57f\u23e1\uf546\uc967\u1297\u7987\u2f9f\u4338\ubcf9\u8269\u4e70\ue283\u9342\u8a59\ude21\uceb0\u882b\u0eeb\uaa72\u62d1\ue0bf\u176e\ue030\u1adf\u9a92\u58ee\u7a3c\u57a3\u8e8d\u389e\ufff4\u27e4\uea4a\ua106\u4e2b\u4789\u447e\ua32c\u7f36\uf30d\u8aaa\ue385\uf333\u155b\uf9f2\uc69a\u103e\u11da\u82d4\uf3cf\u0dc2\u134a\ue3de\uf084\uefc1\u680d\ufb0e\u5e1e\ud339\u8e37\ua24b\uca01\uaf96\uf447\uec5f\u99e2\uafaa\ud51f\uc5e5\uad43\udb62\u057a\u4032\u556d\ua2d3\ua772\u1abe\u261e\u51eb\u5c8b\ufa07\u37a9\ud9f9\u80d4\uc780\u8590\u9962\uf5b4\u3bc8\u2e6d\uc797\u571d\u052f\u2e9e\u7b0d\u0d0c\ua50f\ub892\u507d\ua06d\ubf31\u88c0\ude48\u2888\u48f1\ubc3c\u1577\u8fd3\u39ad\ucaef\ub98a\u6bb0\u0a21\u87fc\u9c51\u9d66\u7108\u0b2b\u6f76\ufb54\u3b2f\uff2c\u632e\u0414\u92ce\ua5f2\u25ce\u4be8\u3bac\ucb1e\ua39f\u0487\ufc21\u78e3\u899b\u9761\uc8b3\ua0a1\uad64\ud45f\u0a77\uf817\ub121\u5240\uae3c\u4703\u156f\uc3dc\u313b\u5530\u9d48\uc702\ue463\u8bed\ueb93\ufb91\u6e16\ub4fe\ub1e0\u6079\u8df5\ua0a7\u9c43\uebc6\u768e\u7874\uf92c\ud90d\u9c4c\u6556\uec3f\uf247\uaa2f\ua85f\u4189\u9776\ub693\ueede\u0326\u6b85\ue342\uc412\uc861\ub04f\uc940\uf45c\u1117\u980a\uc88c\u9399\u1a6a\u6935\uff7c\u649d\ud455\u7dcc\u2fee\ufa13\u93cb\uf02c\u45d0\u5661\uf074\uc8ab\u3fd9\ucbfa\ua1e7\u2399\u72f5\u6556\ucc27\u0610\ube5f\u2321\ub094\u7b00\ube4b\u49fc\u341b\u158e\u206b\ub840\uc0d8\uedd8\uf972\u18fb\u7a5a\udf7c\ud3b3\u0086\u06b0\u7d3d\u955c\u4160\ufb66\uf8e8\u674e\u1306\u2a1bZ\uc745\u1067\u3bac\ue953\u6322\uec5c\u764e\ud75e\uc354\u4297\u9465\u6e34\ue19c\u1d54\ubb60\uf8a6\u7ea5\u0550\u7e09\uffb0\uf128\u14e3\u4fb9\ud86b\uf920\u1858\u43ba\ub081\ucb9d\u3f38\u3753\ud62d\uf7a8\ue9bc\u54b0\u492a\u8f86\uadb8\u702a\u7486\u938f\ud33d\u03d5\u9033\u9612\u77b5\u2be8\u069d\ub4bb\u97ca\u5b7e\u0718\u6e03\u3eed\u453b\ub90e\ubf58\ud4e7\u0da8\u5463\uc191\u9cc0\u48d4\u4d29\ua51f\u95d9\u6311\u6261\u67cf\u47b6\ucd4e\ue1f8\ucf2d\u5636\u7f1c\uc4c8\ua38d\u8f44\ud722\u7602\u9605\u2d00\u0c9a\uc97c\u7164\ue5317\udcf4\u6c7e\u145b\ue928\u94d1\u0f7a\u3250\u7cea\u5baa\u3a14\u66be\uf494\ubd12\u36e2\u09c9\u7398\u2217\u61aa\ud656\u9b4e\u4217\u9b43\u1502\u1f42\u0176\u7c0b\u349c\u68ff\u0daa\u5636\u13b4\u04b1\u4703\u104b\ub9e6\u8b0b\u1624\u44d4\u820a\u5cc0\u10cf\uf376\u1a9b\u1d66\u5906\ua0f7\u023b\uaa79\ua736\udbbe\u7c99\u8419\uba02\u0418\u5209\u3251\u11fd\udc53\ud196\u126b\udb9a\u87fc\uba4b\u3f89\u55a9\u8cf8\ue21f\u9dc8\u5a24\u2fae\ue6b3\u52c2\u427d\u302a\u2c8d\u3d8f\u527b\ubf5b\u4b0f\ub36b\u0b6c\u75fb\u5f6e\ub5f5\u6aed\u0e99\u7799\u09bd\uf0cf\u3041\uf38e\u1b18\u2273\ucb9f\u8760\uc63c\u51ae\u25aa\u10e3\ud539\u0d46\u1560\ua616\u6947\u4317\u68fb\ua709\uaec8\u8197\ua269\u8472\ue485\u2d8e\u7f94\u5787\ua8c9\u169c\u700c\u948e\ufa1f\u7a85\u3775\u2d9b\u971f\ubffa\u6867\ua51d\u550d\ud752\u297c\u739e\u8ad9\ud813\u4bd8\u5023\u8371\u862e\u832e\uf281\u8069\u3946\ua81d\u0199\u8ec1\ufcd1\u7c98\u065a\ub5c1\u7b05\ub36a\ue012\u604c\u4cd2\u1c6e\uadb8\u2fb7\u69cb\u904c\ue747\uf6cc\u5460\uc262\u6c59\u73b3\ud02b\u8b69\u72e3\u8328\u3a67\ua10f\u81c1\u27eb\u6de8\u48bb\u2128\ub42a\uc99e\u1fab\ua355\ua5cc\ua05a\uccf0\uc1ef\u8459\ub259\uffbf\u761b\u4b73\u04a9\ufd81\udccd\uf987\u8037\u2298\u0b5c\u2f89\u0cd0\u384d\uc49d\ue140\u1679\ub443\u46e3\u11f0\uc4aa\u919c\u66d9\u5be7\u91d6\u202a\uda47\u21be\u928d\uc217\ud4d1\u4866\u4565\ud2f3\u065d\ua1fa\u6d25\u3298\u364b\u7d41\u7e98\ua174\u9a80\u79fe\ue9d9\u5e82\uf9b3\u717b\u60a7\u82fd\u6e18\ue97b\ube68\ubd92\uf723\u9065\u2545\ue083\ucff5\u65f8\ub6f5\u5067\u0281\udd64\u6c0d\ub385\u47ae\udcea\u89a1\u25ff\u9309\u6d84\u6710\u8f55\u1f20\u714f\u1198\u7bfe\u2757\u366c\ub55b\u5ffb\ub9f9\u7a37\u4329\u5984\ue37d\ueed6\ua97d\u1548\u0b5b\u8f3d\uffd9\uc4eb\u31bf\u815b\u0a32\u0ed4\u2f33\uba65\ucbc1\ue8f0\u7691\u1169\u6162\ufc1f\u0525\u3420\u2df3\u0785\u548b\u5ede\u59e2\ue925\u59dc\ufbc5\u4132\u273b\u33f3\ub029\ue0bb\uc7ab\u47c1\uc9d8\u44f3\uff03\ue4f2\u7466\uf2f2\ub000\u4b77\ue63b\ud420\ude66\u45cf\u0726\u4e74\ua6df\u4ca5\ueacc\u5d2e\u4339\u8c74\udb7a\u1e1a\ua44a", -1240051255, 0xC6E28B41 ^ CryptoVault.mask());

        private Shaders() {
        }

        private static /* synthetic */ String ql(String string, int n2, int n3) {
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

