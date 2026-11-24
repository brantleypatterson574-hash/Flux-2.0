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
 *  net.minecraft.class_10799
 *  net.minecraft.class_11231
 *  net.minecraft.class_11244
 *  net.minecraft.class_11246
 *  net.minecraft.class_2960
 *  net.minecraft.class_4588
 *  net.minecraft.class_8030
 *  org.joml.Matrix3x2fc
 */
package com.ishland.scalablelux.gui.gpuui.render.effects;

import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiClipMask;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiMaterialRenderer;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuFluidSimulation;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_11246;
import net.minecraft.class_2960;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2fc;

@Environment(value=EnvType.CLIENT)
public final class GpuBackdropBlur {
    private static final class_2960 VERTEX = class_2960.method_60655((String)GpuBackdropBlur.hp("\u5272\ueae0\u3a88\u84c1\u85cc\u390a\u0ff8\u9f1b\ue0aa\u0483\ub05a\uf1f9\u5435", -1331028324, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u2c9e\u5eff\u99a3\udd87\ue9dd\udd6e\u43d3\u7dba\uf8f6\u5713\u7463\ubfe5\ud349\u82d8\ueece\ub3b4\uce28\u9ae3\u50bb\u32c9\u9227\udc44\u17da", -606925708, 0xAD50BF0A ^ CryptoVault.mask()));
    private static final class_2960 FRAGMENT = class_2960.method_60655((String)GpuBackdropBlur.hp("\ucf34\u33e3\u4dfa\u7df3\u9208\u68ff\ubdb1\u5cef\udf55\u46f7\u0957\u2153\u84d2\u1951\uda27", 1097989726, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\ub6f6\u1d7d\u73a1\u7ba8\uc761\u7e6a\u3738\u5de0\u7785\uc376\uc0b3\udeac\ue4dd\u536b\u809c\u212a\ua444\uc70b\u7c8e\u5bf1", 1870855598, 0xAD50BF0A ^ CryptoVault.mask()));
    private static final VertexFormat FORMAT = VertexFormat.builder().add(GpuBackdropBlur.hp("\uafa5\u5c42\u34ea\uf4ae\u23ed\uabde\uefdd\u0ea2\u76de\u0410", 147566594, 0xAD50BF0A ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuBackdropBlur.hp("\u668a\u5f79\u22b6\ub617\ud992\ueef1", -89939920, 0xAD50BF0A ^ CryptoVault.mask()), VertexFormatElement.COLOR).add(GpuBackdropBlur.hp("\ucad8\u7641\u09cd\ubbac\u9074", -799089471, 0xAD50BF0A ^ CryptoVault.mask()), VertexFormatElement.UV0).add(GpuBackdropBlur.hp("\u853c\u962e\u9f66\u3820\u37ce\ub218", -1428721728, 0xAD50BF0A ^ CryptoVault.mask()), VertexFormatElement.UV1).add(GpuBackdropBlur.hp("\u86af\u6595\u164e\ubad3\ue55e\u9d3e\uad59\u9818\u93de\ue42a\u5983", -298235115, 0xAD50BF0A ^ CryptoVault.mask()), VertexFormatElement.LINE_WIDTH).build();
    private static final RenderPipeline BLUR_PIPELINE = GpuBackdropBlur.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuBackdropBlur.hp("\u5d81\ub7c9\u5b9d\uba3e\u561e\u507d\u4081\ub586\ud748\uafee\u5d9d\u6252", -780195789, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\uf1bd\u1447\u5061\uf51b\u0625\u1276\u06fe\ucaef\u3410\ue458\u1d54\u82a8\uc919\u8309\u805e\uca23\u4723\uf242\ufcaf\uc367\u590d\ud9c3\ufd67\u0c4a\u04d6", 2139441929, 0xAD50BF0A ^ CryptoVault.mask()))).withVertexShader(VERTEX).withFragmentShader(FRAGMENT).withVertexFormat(FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuBackdropBlur.hp("\u9263\u4a95\u353b\ue2a2\u0d5c\ub32b\ud033\ub91d\uaa56\u3c6f\u42fa", -464623082, 0xAD50BF0A ^ CryptoVault.mask())).withSampler(GpuBackdropBlur.hp("\ubd24\uc12d\u62f0\u7bb5\u5238\u1153\u402a\ub194\ub894", -251461110, 0xAD50BF0A ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
    private static final RenderPipeline BLOOM_PIPELINE = GpuBackdropBlur.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuBackdropBlur.hp("\uc261\u99cf\u2d0c\u9340\u6ad4\uc057\u06d5\u2a2c\ude3c\u523b\uf526\uc44a\u9727\u3910\u791f", 1038038231, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u70ca\u821d\u81e3\uc2a0\ud034\ua54e\u57fc\uf86b\uded5\u3280\u1e85\u973b\u5995\ubf43\u095d\u708a\ua38a\u95db\u63c6\ud3c7\u3214\u7a97", 1487833919, 0xAD50BF0A ^ CryptoVault.mask()))).withVertexShader(VERTEX).withFragmentShader(FRAGMENT).withVertexFormat(FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuBackdropBlur.hp("\u4801\u961f\u0823\ue2e7\uf821\u1efa\u5f09\u29c7\ue9fb\u2f60", 4952788, 0xAD50BF0A ^ CryptoVault.mask())).withSampler(GpuBackdropBlur.hp("\u9bf4\u4c9d\u1e48\u6c75\ua680\u95c7\u23ff\udbc8\ubd22\u288f\u8173\u7f91", -1019732790, 0xAD50BF0A ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.ADDITIVE).build());
    private static final class_2960 LIQUID = class_2960.method_60655((String)GpuBackdropBlur.hp("\uabff\u49d0\u2b43\u658c\u6a37\u1d1b\u0cfb\ufe56\u2580\ud785\ub735\uced9", -1209484253, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\ud604\u5fec\u3d2a\uf16e\u06ec\ufd7c\u0e9c\u7c34\u6d01\u3322\ua467\u3282\u56e8\u8a55\u6b1b\u311d\u7a24\uab56", -1646156443, 0xAD50BF0A ^ CryptoVault.mask()));
    private static final RenderPipeline GLASS_PIPELINE = GpuBackdropBlur.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuBackdropBlur.hp("\uf09f\uf9ef\uf477\ub946\u61c3\u0a82\u8b11\u5ddd\ua85c\ucc95\udb93\u06fb\ua279\u9534\ucdc0", -1847563555, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u8644\u8e49\u26bd\u5c11\uc5b3\u7536\u8e17\u9159\u0de3\uc855\u5ed1\u3752\u26c6\u2d03\ud9ec\ude01\ua462\ude11\u4713\u6a71\u319d\ue5ea\u7ad3\u199d", 2139695498, 0xAD50BF0A ^ CryptoVault.mask()))).withVertexShader(LIQUID).withFragmentShader(LIQUID).withVertexFormat(FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuBackdropBlur.hp("\u0f75\uc935\ua391\u6de9\u5f77\ueca6\u86c0\u0c58\ua9e7\udb0b\u1750\u1c3c", 604839735, 0xAD50BF0A ^ CryptoVault.mask())).withSampler(GpuBackdropBlur.hp("\u513e\u847e\u149d\ufdfa\u6d7a\uc1b8\ucb22\u7588\u9e2d\u3306\u0899\uab45", 1731704210, 0xAD50BF0A ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
    private static final RenderPipeline KAWASE_DOWN = GpuUiEffectPasses.materialPipeline(class_2960.method_60655((String)GpuBackdropBlur.hp("\ue0b4\u8565\u0b00\u9c8a\u24fb\u9549\u4335\ue4c1\u092b\u1ea5\u821d\ua655\u6894\ua44a", 1681196954, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u6314\u5bca\u4cbd\u0c1f\u3925\ua602\ubfe1\ua699\u986b\ue862\u031a\u6240\u8ab1\u473b\u61aa\u7755\ud406\ub9ac\ue5ba\ub3c2\ua536\u83ce\ueb90\u9664\u84ef\u8bb4\u55e0\uc0f5\uccc6\uebbc\ue6eb", 1241047102, 0xAD50BF0A ^ CryptoVault.mask())));
    private static final RenderPipeline KAWASE_UP = GpuUiEffectPasses.materialPipeline(class_2960.method_60655((String)GpuBackdropBlur.hp("\uddb8\u3d62\u44ec\u0418\u1b7b\ub013\u872c\udd99\uab72\uedf7\u6a5e\u1814\u0248\u2dd5\u5fd6", -1224018541, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u9f25\u6106\ucf0e\ub210\uf49c\u2bdf\u5737\u4832\u946c\ua41a\u9788\u12bd\u8d05\u5484\u332c\u34f4\u5fb0\u827e\u486a\u7156\u2312\u1326\u6079\u4ae0\u15fa\u95d0\ue480\ub164\u3c06\u0c85", -323305310, 0xAD50BF0A ^ CryptoVault.mask())));
    private static final RenderPipeline BLOOM_EXTRACT = GpuUiEffectPasses.materialPipeline(class_2960.method_60655((String)GpuBackdropBlur.hp("\u95c9\ud964\u14c1\u0637\uc75f\ubd4a\uef9a\u31c9\ub60e\u4500\ucd79\u6a63\ubd36\u5fdb", -123066957, 0xAD50BF0A ^ CryptoVault.mask()), (String)GpuBackdropBlur.hp("\u9eec\u6e89\ud52b\u2840\u2a3c\uf41b\uc630\u9c11\u8717\u2e9e\u83ca\u7964\u0e9c\ud031\uaaa7\u3b89\ucc31\uce86\uef0b\u009e\u1130\u6d33\u3c0f\ue4f3\uefac\u2125\u5711\u22bb\u2b3e\u4bff\u822a\ua161", 1636218311, 0xAD50BF0A ^ CryptoVault.mask())));
    private static final int MAX_LEVELS = 6;
    private static final double PRESET_STEP = Math.log(1.18);
    private static final Map<Integer, GpuUiEffectPasses.Target> frameCache = new HashMap<Integer, GpuUiEffectPasses.Target>();

    private GpuBackdropBlur() {
    }

    public static void registerPipelines() {
    }

    public static void addBlur(class_11246 class_112462, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, float f7, class_8030 class_80302) {
        GpuBackdropBlur.addEffect(class_112462, matrix3x2fc, f2, f3, f4, f5, f6, UiDrawCommand.EffectKind.BLUR, f7, class_80302, GpuUiClipMask.none());
    }

    public static void addEffect(class_11246 class_112462, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, UiDrawCommand.EffectKind effectKind, float f7, class_8030 class_80302, GpuUiClipMask gpuUiClipMask) {
        GpuBackdropBlur.addEffect(class_112462, matrix3x2fc, f2, f3, f4, f5, f6, effectKind, f7, -1, class_80302, gpuUiClipMask);
    }

    public static void addEffect(class_11246 class_112462, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, UiDrawCommand.EffectKind effectKind, float f7, int n2, class_8030 class_80302, GpuUiClipMask gpuUiClipMask) {
        RenderPipeline renderPipeline;
        boolean bl;
        if (!(GpuUiEffectPasses.isAvailable() && class_112462 != null && f4 > 0.0f && f5 > 0.0f && Float.isFinite(f7) && !(f7 <= 0.0f))) {
            return;
        }
        if (OverlayCompositor.isOverlayCaptureActive()) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        boolean bl2 = bl = effectKind == UiDrawCommand.EffectKind.BLOOM;
        RenderPipeline renderPipeline2 = effectKind == UiDrawCommand.EffectKind.GLASS ? GLASS_PIPELINE : (renderPipeline = bl ? BLOOM_PIPELINE : BLUR_PIPELINE);
        if (!SafePipelines.usable(renderPipeline) || !SafePipelines.usable(KAWASE_DOWN) || !SafePipelines.usable(KAWASE_UP) || bl && !SafePipelines.usable(BLOOM_EXTRACT)) {
            return;
        }
        try {
            GpuUiEffectPasses.Target target = GpuUiEffectPasses.requestSceneTexture();
            if (target == null) {
                return;
            }
            int n3 = Math.round((float)(Math.log(Math.max(0.25f, Math.min(64.0f, f7))) / PRESET_STEP));
            float f8 = (float)Math.exp((double)n3 * PRESET_STEP);
            int n4 = (bl ? 0x40000000 : 0) | n3 + 512;
            GpuUiEffectPasses.Target target2 = frameCache.get(n4);
            if (target2 == null || target2.closed()) {
                target2 = GpuBackdropBlur.queueDualKawase(target, bl, f8);
                if (target2 == null) {
                    return;
                }
                frameCache.put(n4, target2);
            }
            class_8030 class_80303 = GpuBackdropBlur.rectangle(f2, f3, f4, f5).method_65185(matrix3x2fc);
            class_8030 class_80304 = class_80302 == null ? null : class_80302.method_49701(class_80303);
            int n5 = bl ? Math.round(GpuBackdropBlur.clamp(f7 / 16.0f, 0.08f, 0.9f) * 255.0f) : 255;
            boolean bl3 = effectKind == UiDrawCommand.EffectKind.GLASS;
            GpuUiRenderer.submit(class_112462, new EffectState(renderPipeline, (gpuUiClipMask == null ? GpuUiClipMask.none() : gpuUiClipMask).withMask(GpuUiEffectPasses.texture(target2)), matrix3x2fc, f2, f3, f4, f5, GpuBackdropBlur.clamp(f6, 0.0f, Math.min(f4, f5) * 0.5f), bl ? n5 << 24 | 0xFFFFFF : (bl3 ? n2 : -1), class_80304 != null ? class_80304 : class_80303, class_80302));
        }
        catch (LinkageError | RuntimeException throwable) {
            GpuUiEffectPasses.disableAfterFailure(throwable);
        }
    }

    private static GpuUiEffectPasses.Target queueDualKawase(GpuUiEffectPasses.Target target, boolean bl, float f2) {
        int n2;
        float f3 = GpuBackdropBlur.clamp(f2, 0.0f, 30.0f);
        int n3 = 1 + (int)(f3 / 7.5f);
        float f4 = 1.0f + f3 % 7.5f / 7.5f * 1.25f;
        if (target.height() > 1600) {
            ++n3;
        }
        n3 = Math.max(1, Math.min(6, n3));
        GpuUiEffectPasses.Target[] targetArray = new GpuUiEffectPasses.Target[n3 + 1];
        targetArray[0] = target;
        int n4 = target.width();
        int n5 = target.height();
        int n6 = 0;
        int n7 = 1;
        while (n7 <= n3 && n4 >= 4 && n5 >= 4) {
            GpuUiEffectPasses.Target target2 = GpuUiEffectPasses.createTarget(n4 = Math.max(1, (n4 + 1) / 2), n5 = Math.max(1, (n5 + 1) / 2));
            if (target2 == null) {
                return null;
            }
            n2 = bl && n7 == 1 ? 1 : 0;
            GpuUiEffectPasses.queueFullscreen(n2 != 0 ? BLOOM_EXTRACT : KAWASE_DOWN, targetArray[n7 - 1].view(), target2, GpuUiEffectPasses.writeParameters(n4, n5, 0.0f, 0.0f, new float[][]{{f4, 0.68f, 0.0f, 0.0f}}));
            targetArray[n7] = target2;
            n6 = n7++;
        }
        if (n6 == 0) {
            return null;
        }
        GpuUiEffectPasses.Target target3 = targetArray[n6];
        for (int i2 = n6 - 1; i2 >= 0; --i2) {
            int n8;
            n2 = targetArray[i2].width();
            GpuUiEffectPasses.Target target4 = GpuUiEffectPasses.createTarget(n2, n8 = targetArray[i2].height());
            if (target4 == null) {
                return null;
            }
            GpuUiEffectPasses.queueFullscreen(KAWASE_UP, target3.view(), target4, GpuUiEffectPasses.writeParameters(n2, n8, 0.0f, 0.0f, new float[][]{{f4, 0.0f, 0.0f, 0.0f}}));
            target3 = target4;
        }
        return target3;
    }

    public static void captureScene() {
        GpuUiEffectPasses.captureAndRunQueuedPasses();
        GpuFluidSimulation.runPending();
    }

    public static void finishFrame() {
        frameCache.clear();
        GpuUiMaterialRenderer.finishFrame();
        GpuUiEffectPasses.finishFrame();
        GpuFluidSimulation.finishFrame();
    }

    private static RenderPipeline register(RenderPipeline renderPipeline) {
        return SafePipelines.register(renderPipeline);
    }

    private static class_8030 rectangle(float f2, float f3, float f4, float f5) {
        int n2 = (int)Math.floor(f2);
        int n3 = (int)Math.floor(f3);
        int n4 = (int)Math.ceil(f2 + f4);
        int n5 = (int)Math.ceil(f3 + f5);
        return new class_8030(n2, n3, Math.max(0, n4 - n2), Math.max(0, n5 - n3));
    }

    private static float clamp(float f2, float f3, float f4) {
        return Float.isFinite(f2) ? Math.max(f3, Math.min(f4, f2)) : f3;
    }

    private static /* synthetic */ String hp(String string, int n2, int n3) {
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
    private static final class EffectState
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

        private EffectState(RenderPipeline renderPipeline, class_11231 class_112312, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, int n2, class_8030 class_80302, class_8030 class_80303) {
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
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{EffectState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{EffectState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{EffectState.class, "pipeline;textureSetup;pose;x;y;width;height;cornerRadius;argb;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "cornerRadius", "argb", "bounds", "scissorArea"}, this, object);
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
}

