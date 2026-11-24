/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.BlendFunction
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.AddressMode
 *  com.mojang.blaze3d.textures.FilterMode
 *  com.mojang.blaze3d.textures.GpuTextureView
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  com.mojang.blaze3d.vertex.VertexFormatElement
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_1044
 *  net.minecraft.class_10799
 *  net.minecraft.class_11231
 *  net.minecraft.class_11244
 *  net.minecraft.class_11246
 *  net.minecraft.class_12137
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_4588
 *  net.minecraft.class_8030
 *  org.joml.Matrix3x2f
 *  org.joml.Matrix3x2fStack
 *  org.joml.Matrix3x2fc
 *  org.joml.Vector2f
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiAwtText;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiClipMask;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFallbackText;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiMaterialRenderer;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuBackdropBlur;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuFluidSimulation;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.util.ClientInternalLog;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.I18n;
import com.ishland.scalablelux.util.PerfProbe;
import com.ishland.scalablelux.util.ScissorClamp;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1044;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11244;
import net.minecraft.class_11246;
import net.minecraft.class_12137;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix3x2fc;
import org.joml.Vector2f;

@Environment(value=EnvType.CLIENT)
public final class GpuUiRenderer {
    private static final class_2960 SHADOW_VERTEX = class_2960.method_60655((String)GpuUiRenderer.fk("\ua146\ua585\u6d89\u5d95\u1975\u41f6\u930c\u1ec0\u0dd9\u6097\u2b12\ua3d3", -1445425149, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u0f74\uef2a\uf350\u95ce\uffe4\u7f14\u3a67\u0fd2\ud8aa\ua270\ucd01\u8001\u6ae1\u22c3\ue8a1\ud615\u62ab\u3f33", -1139131468, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 SHADOW_FRAGMENT = class_2960.method_60655((String)GpuUiRenderer.fk("\u0352\u1b47\u1de5\u1a76\u2195\u2c55\u0a09\uc714\ueda8\u48a6\uae0b\u323c\u2c0f\u7213", 1964424415, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u6260\u2f0d\u0934\uca42\ua56a\u83d3\u4337\ud717\u0fb6\u50bf\u737d\u75d2\ucce4\u6b8d\u74fd\u59c2\u98ff\u7e1e\u347b", -1549732696, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 RECT_VERTEX = class_2960.method_60655((String)GpuUiRenderer.fk("\u70cc\uf3ab\u59a2\uf74b\ucce3\u4686\u36eb\u3a51\u684b\u0130\u7dbd\ua450", 1462626607, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u9da5\u53f2\uaed5\u78cf\udd60\ud64e\u6889\ua0d8\uf660\u584e\u5ac3\uccf0\u83a0\u7741\uc8ac\u7566\ufe37\u4647\u52c3", 2071312515, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 RECT_FRAGMENT = class_2960.method_60655((String)GpuUiRenderer.fk("\u1184\ucb86\ud3ed\u60a5\uc730\u9504\ufbf6\uad89\ue39b\u7995\ude47\u6f6f\u8adf", 1039791912, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\ud399\uf04e\ua16e\uead9\u974e\uc675\u1b1d\udf68\u4f79\ucfba\u2053\ua740\u7142\u2405\u79fa\ue2dd", 1245784922, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 TEXT_VERTEX = class_2960.method_60655((String)GpuUiRenderer.fk("\u8ac4\u9221\ufe23\u1fe6\u855c\u1700\ua12d\ua5d7\ub35a\u72d2\u3756\u95c9\ua5c8", 70245718, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\uad74\u8ae5\ubdce\u363e\u5a06\u8dae\u2d98\ubcae\u1a9d\u99c8\uf0c5\u5e2d\uc817\u98e4\u8de5\ub378", 56415653, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 TEXT_FRAGMENT = class_2960.method_60655((String)GpuUiRenderer.fk("\u5dbb\ueba9\u4414\u1695\u1aff\u00a5\u1e98\u51f9\u2b5c\uc9b7\ubebc\udd4e\uf0dd\u55be", -422518046, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u859a\u61fb\u643e\u3f10\u2f4f\u652b\u8a29\ub79d\ub67c\ub680\ucd4a\u3b68\uf113\ud962\ucb77\ub14a\u1589\u4eda\ud619", -1182602910, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 IMAGE_VERTEX = class_2960.method_60655((String)GpuUiRenderer.fk("\u3b89\u79a3\u1fdc\ufd9a\uc756\u5f8e\ud17f\ueb87\uf294\udb49\ub5e9\u5e96\u4463\ucae2\u5e8f", -386254973, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\uc65f\u8e32\u2778\u4d6a\ueecc\ue82c\u1471\u6ef5\u3499\u8961\u11dd\u21f4\ufbba\ue1ff\uc2c6\ub26c\u8e9e", 1244903622, 0x57FC071B ^ CryptoVault.mask()));
    private static final class_2960 IMAGE_FRAGMENT = class_2960.method_60655((String)GpuUiRenderer.fk("\u2ef7\uab05\u0ccc\u772c\uf52b\u2c3c\u21bf\ub2ce\ud35b\u3b0b\uffd3\ufa06\u19b4", -5309621, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\uca19\u9976\u78e9\uf680\u6d92\ua353\ub2b3\ub230\u659a\uf6c6\uead9\u9354\u0816\u248b\u8222\u3582\u9c07", -884739861, 0x57FC071B ^ CryptoVault.mask()));
    private static boolean warnedRoundedClipFallback;
    private static final VertexFormat UI_FORMAT;
    private static final VertexFormat SHADOW_FORMAT;
    private static final RenderPipeline RECT_PIPELINE;
    private static final RenderPipeline TEXT_PIPELINE;
    private static final RenderPipeline SHADOW_PIPELINE;
    private static final RenderPipeline IMAGE_PIPELINE;
    private static boolean pixelSpace;
    private static float pixelsPerGuiUnit;
    private static class_11246 orderedState;
    private static int orderedDepth;
    private static final Matrix3x2f FENCE_POSE;
    private static final int QUAD_FLOATS = 11;
    private static final int MAX_OPEN_BATCHES = 48;
    private static final QuadBatch[] BATCHES;
    private static int openBatches;

    private GpuUiRenderer() {
    }

    private static RenderPipeline register(RenderPipeline renderPipeline) {
        return SafePipelines.register(renderPipeline);
    }

    public static void registerPipelines() {
        if (RECT_PIPELINE == null || TEXT_PIPELINE == null || SHADOW_PIPELINE == null || IMAGE_PIPELINE == null) {
            throw new IllegalStateException(GpuUiRenderer.fk("\uc700\ucbb9\u141d\u58c1\u1fad\u8061\u73ec\ub6d8\u8ad5\u8eb2\ub8c0\ubf1d\u2fdf\uf570\u2cfe\ue1e6\ueb4b\uff0c\udbb4\u8780\u7f90\ubb54\u6681\u11c4\u1ee5\ua124\u3c63\u349d\u7f52\ubf5e\u75b8\ua9d8\u699b\u9646\u64d3\ucdf1\ue3b7\u46d2\u611b", -1822021051, 0x57FC071B ^ CryptoVault.mask()));
        }
        GpuUiClipMask.registerPipelines();
        GpuUiEffectPasses.registerPipelines();
        GpuBackdropBlur.registerPipelines();
        GpuFluidSimulation.registerPipelines();
    }

    public static void invalidateFontMetrics() {
        GpuUiFontMetrics.invalidate();
    }

    public static void render(class_332 class_3322, UiDisplayList uiDisplayList) {
        GpuUiRenderer.render(class_3322, uiDisplayList, false, 1.0f);
    }

    public static void renderPixels(class_332 class_3322, UiDisplayList uiDisplayList, float f2) {
        GpuUiRenderer.render(class_3322, uiDisplayList, true, Math.max(1.0f, f2));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void render(class_332 class_3322, UiDisplayList uiDisplayList, boolean bl, float f2) {
        boolean bl2;
        class_11246 class_112462;
        block7: {
            if (class_3322 == null || uiDisplayList == null || uiDisplayList.commands().isEmpty()) {
                return;
            }
            pixelSpace = bl;
            pixelsPerGuiUnit = f2;
            class_112462 = class_3322.field_59826;
            boolean bl3 = bl2 = orderedDepth == 0;
            if (bl2) {
                GpuUiRenderer.submitLayerFence(class_112462);
                orderedState = class_112462;
            }
            ++orderedDepth;
            long l2 = PerfProbe.on ? System.nanoTime() : 0L;
            try {
                GpuUiRenderer.renderCommands(class_3322, uiDisplayList);
                if (l2 == 0L) break block7;
                PerfProbe.module(bl2 ? GpuUiRenderer.fk("\u22c4\u8a5b\u940b\ub34a\ue49a\uada4\uf76c\uad18\udbab", 1601254141, 0x57FC071B ^ CryptoVault.mask()) : GpuUiRenderer.fk("\u0419\ued36\u8b45\u0712\uc421\u8acb\ud444\u1ede\uc0f0\u27ef\uc459\u3ff0\uf1ec", -32537369, 0x57FC071B ^ CryptoVault.mask()), GpuUiRenderer.fk("\ue203\u0897\u6185\u9d34\u9a97\u4129\ub217", 12304195, 0x57FC071B ^ CryptoVault.mask()), System.nanoTime() - l2);
            }
            catch (Throwable throwable) {
                if (l2 != 0L) {
                    PerfProbe.module(bl2 ? GpuUiRenderer.fk("\ua9fc\u4ca4\uc9d3\uaa1a\u84fc\ude20", 832850026, 0x57FC071B ^ CryptoVault.mask()) : GpuUiRenderer.fk("\uf26f\u1046\ua144\u3033\u55d9\u7472\u94f3\u6765\u4e3f\u1078\u8194\u3512", -108900950, 0x57FC071B ^ CryptoVault.mask()), GpuUiRenderer.fk("\u8b4e\ucbb3\u9f38\u3026\ufdc6\ud0e4\u658f\ua7ab\u47b1\u716e", -508289898, 0x57FC071B ^ CryptoVault.mask()), System.nanoTime() - l2);
                }
                --orderedDepth;
                if (bl2) {
                    orderedState = null;
                    GpuUiRenderer.submitLayerFence(class_112462);
                }
                pixelSpace = false;
                pixelsPerGuiUnit = 1.0f;
                throw throwable;
            }
        }
        --orderedDepth;
        if (bl2) {
            orderedState = null;
            GpuUiRenderer.submitLayerFence(class_112462);
        }
        pixelSpace = false;
        pixelsPerGuiUnit = 1.0f;
    }

    public static void submit(class_11246 class_112462, class_11244 class_112442) {
        GpuUiRenderer.flushBatch();
        GpuUiRenderer.submitRaw(class_112462, class_112442);
    }

    private static void submitRaw(class_11246 class_112462, class_11244 class_112442) {
        if (class_112462 == orderedState) {
            class_112462.method_71997(class_112442);
        } else {
            class_112462.method_70919(class_112442);
        }
    }

    private static void submitLayerFence(class_11246 class_112462) {
        class_1041 class_10412 = class_310.method_1551().method_22683();
        class_8030 class_80302 = new class_8030(0, 0, Math.max(1, class_10412.method_4486()), Math.max(1, class_10412.method_4502()));
        QuadState quadState = new QuadState(class_10799.field_56879, class_11231.method_70899(), (Matrix3x2fc)FENCE_POSE, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, false, false, class_80302, null);
        class_112462.method_70919((class_11244)quadState);
    }

    private static void renderCommands(class_332 class_3322, UiDisplayList uiDisplayList) {
        GpuUiRenderer.flushBatch();
        class_11246 class_112462 = class_3322.field_59826;
        Matrix3x2f matrix3x2f = new Matrix3x2f((Matrix3x2fc)class_3322.method_51448());
        if (pixelSpace && pixelsPerGuiUnit != 1.0f) {
            matrix3x2f.scale(1.0f / pixelsPerGuiUnit);
        }
        ArrayDeque<ClipState> arrayDeque = new ArrayDeque<ClipState>();
        ClipState clipState = ClipState.root();
        HashMap<List, GpuUiClipMask> hashMap = new HashMap<List, GpuUiClipMask>();
        boolean bl = HitboxConfigScreen752809.bc != null && HitboxConfigScreen752809.bc.isEnabled();
        boolean bl2 = GpuUiClipMask.available() && !bl;
        class_1041 class_10412 = class_310.method_1551().method_22683();
        int n2 = class_10412.method_4489();
        int n3 = class_10412.method_4506();
        int n4 = class_10412.method_4486();
        int n5 = class_10412.method_4502();
        int n6 = 0;
        for (UiDrawCommand uiDrawCommand : uiDisplayList.commands()) {
            if (uiDrawCommand instanceof UiDrawCommand.Rect) {
                UiDrawCommand.Rect rect = (UiDrawCommand.Rect)uiDrawCommand;
                if (!clipState.empty) {
                    GpuUiRenderer.addQuad(class_112462, rect.x(), rect.y(), rect.width(), rect.height(), rect.radius(), rect.argb(), (Matrix3x2fc)matrix3x2f, clipState, class_11231.method_70899(), 0.0f, 0.0f, 1.0f, 1.0f, false, false, 0.0f);
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.GradientRect) {
                UiDrawCommand.GradientRect gradientRect = (UiDrawCommand.GradientRect)uiDrawCommand;
                if (!clipState.empty && gradientRect.width() > 0.0f && gradientRect.height() > 0.0f) {
                    class_8030 class_80302 = GpuUiRenderer.screenBounds(gradientRect.x(), gradientRect.y(), gradientRect.width(), gradientRect.height(), (Matrix3x2fc)matrix3x2f);
                    var28_31 = GpuUiRenderer.intersectionForDraw(clipState, class_80302);
                    if (clipState.scissor == null || var28_31 != null) {
                        boolean bl3 = SafePipelines.usable(RECT_PIPELINE);
                        GpuUiRenderer.submit(class_112462, new GradientState((Matrix3x2fc)matrix3x2f, gradientRect, bl3 ? RECT_PIPELINE : class_10799.field_56879, bl3 ? clipState.mask.withMask(class_11231.method_70899()) : class_11231.method_70899(), var28_31 != null ? var28_31 : class_80302, clipState.scissor));
                    }
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Text) {
                UiDrawCommand.Text text = (UiDrawCommand.Text)uiDrawCommand;
                if (!clipState.empty) {
                    GpuUiRenderer.drawText(class_3322, class_112462, text, (Matrix3x2fc)matrix3x2f, clipState);
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Image) {
                class_11231 class_112312;
                UiDrawCommand.Image image = (UiDrawCommand.Image)uiDrawCommand;
                if (!clipState.empty && (class_112312 = GpuUiRenderer.imageTexture(image.texture(), image.pixelated())) != null) {
                    GpuUiRenderer.addQuad(class_112462, image.x(), image.y(), image.width(), image.height(), image.radius(), image.argb(), (Matrix3x2fc)matrix3x2f, clipState, class_112312, image.u0(), image.v0(), image.u1(), image.v1(), false, true, 0.0f);
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Border) {
                UiDrawCommand.Border border = (UiDrawCommand.Border)uiDrawCommand;
                if (!clipState.empty && border.thickness() > 0.0f) {
                    GpuUiRenderer.addQuad(class_112462, border.x(), border.y(), border.width(), border.height(), border.radius(), border.argb(), (Matrix3x2fc)matrix3x2f, clipState, class_11231.method_70899(), 0.0f, 0.0f, 1.0f, 1.0f, false, false, border.thickness());
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Material) {
                UiDrawCommand.Material material = (UiDrawCommand.Material)uiDrawCommand;
                if (!clipState.empty && material.material() != null && !bl) {
                    int n7 = pixelSpace ? Math.max(1, (int)Math.ceil(material.width())) : GpuUiRenderer.physicalSize(material.width(), n2, n4);
                    int n8 = pixelSpace ? Math.max(1, (int)Math.ceil(material.height())) : GpuUiRenderer.physicalSize(material.height(), n3, n5);
                    class_11231 class_112313 = GpuUiMaterialRenderer.prepare(material.material(), n7, n8);
                    if (class_112313 != null) {
                        GpuUiRenderer.addQuad(class_112462, material.x(), material.y(), material.width(), material.height(), material.radius(), material.material().tint(), (Matrix3x2fc)matrix3x2f, clipState, class_112313, 0.0f, 1.0f, 1.0f, 0.0f, false, true, 0.0f);
                    }
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.ClipPush) {
                GpuUiClipMask gpuUiClipMask;
                List<UiDrawCommand.ClipPush> list2;
                boolean bl4;
                class_8030 class_80303;
                block40: {
                    UiDrawCommand.ClipPush clipPush = (UiDrawCommand.ClipPush)uiDrawCommand;
                    UiDrawCommand.ClipPush clipPush2 = GpuUiRenderer.snapClip(clipPush);
                    arrayDeque.push(clipState);
                    if (!(Float.isFinite(clipPush2.x()) && Float.isFinite(clipPush2.y()) && Float.isFinite(clipPush2.width()) && Float.isFinite(clipPush2.height()) && Float.isFinite(clipPush2.radius()) && !(clipPush2.width() < 0.0f) && !(clipPush2.height() < 0.0f))) {
                        throw new IllegalArgumentException(GpuUiRenderer.fk("\u39d8\ue57e\ua9a0\u0b28\u5ecb\ufbfb\u34ae\u60e7\u0a4f\ua833\ufcc3\ua705\u3aa2\u1218\u72bd\u1ad8\u75b1\uc514\u9b9c\u67e7\u27ea\uffac\u620c\u722a\u252e\ub830\u583d\ud79d\u8f64\ubc0d\u669e\uc38d\ua539\u880a", 1642158731, 0x57FC071B ^ CryptoVault.mask()) + n6);
                    }
                    var28_31 = ScissorClamp.clamp(GpuUiRenderer.rect(clipPush2.x(), clipPush2.y(), clipPush2.width(), clipPush2.height()).method_65185((Matrix3x2fc)matrix3x2f));
                    class_80303 = clipState.scissor == null ? var28_31 : clipState.scissor.method_49701(var28_31);
                    bl4 = clipState.empty || var28_31.comp_1196() <= 0 || var28_31.comp_1197() <= 0 || class_80303 == null;
                    list2 = GpuUiRenderer.append(clipState.chain, clipPush2);
                    gpuUiClipMask = clipState.mask;
                    if (!bl4 && bl2 && clipPush2.radius() > 0.0f) {
                        GpuUiClipMask gpuUiClipMask2 = clipState.mask;
                        try {
                            gpuUiClipMask = hashMap.computeIfAbsent(list2, list -> gpuUiClipMask2.push((Matrix3x2fc)matrix3x2f, clipPush2, n2, n3, n4, n5));
                        }
                        catch (LinkageError | RuntimeException throwable) {
                            bl2 = false;
                            gpuUiClipMask = gpuUiClipMask2;
                            if (warnedRoundedClipFallback) break block40;
                            warnedRoundedClipFallback = true;
                            ClientInternalLog.warn(GpuUiRenderer.fk("\u26e9\ub082\u947b\uf136\u3849\u8f14\u71b4\u0a62\u0b56\u73dc", 2131106451, 0x57FC071B ^ CryptoVault.mask()), GpuUiRenderer.fk("\ue601\ud2b6\u51f1\u879b\u32c1\u8e96\ub0a9\u6a61\ud67e\uf1b3\uda38\u7203\ud6c2\u3bcd\u6918\udca6\ufc3f\u4b78\u5d0d\ucd97\u68d4\u8ea6\u59a6\ucb5f\u4e82\u3b06\u8a11\ua9c8\u2a45?\u7e49\ud64e\uae65\ua94d\u3581\uddac\ude44\ua8fa\uc7cb\u5d28\u3d02\ue4d2\u4938\u7c51\ueac4\u1d00\uda94\u4815\u8d5c\u087e\u5d9a\uc55d\ue0f4\ue808\u569e\u28f1\uf701\ufe7a\uca6e\u6f01", -907194542, 0x57FC071B ^ CryptoVault.mask()), throwable);
                        }
                    }
                }
                clipState = new ClipState(class_80303, gpuUiClipMask, bl4, list2);
            } else if (uiDrawCommand instanceof UiDrawCommand.ClipPop) {
                if (arrayDeque.isEmpty()) {
                    throw new IllegalStateException(GpuUiRenderer.fk("\u4fc7\ub91d\ue45b\u5a2c\uea2f\u9471\uae95\u53d7\uce21\u9849\ubc66\u8057\u7519\u010f\u5879\u65e4\u932c\u7f27\u7b0c\u0c47\u023f\ud16f\u0c82\u36db\uf838\u53c1\ub98f\u12de\u9b6e\u033f\u897e\ud8ce\ua211\u66df\u787f\u1647\u1b6d\u47dd\u9a1e\uf0d5\uc796", 847119244, 0x57FC071B ^ CryptoVault.mask()) + n6);
                }
                clipState = (ClipState)arrayDeque.pop();
            } else if (uiDrawCommand instanceof UiDrawCommand.Effect) {
                UiDrawCommand.Effect effect = (UiDrawCommand.Effect)uiDrawCommand;
                if (!clipState.empty && !bl) {
                    GpuBackdropBlur.addEffect(class_112462, (Matrix3x2fc)matrix3x2f, effect.x(), effect.y(), effect.width(), effect.height(), effect.radius(), effect.kind(), effect.strength(), effect.argb(), clipState.scissor, clipState.mask);
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Shadow) {
                UiDrawCommand.Shadow shadow = (UiDrawCommand.Shadow)uiDrawCommand;
                if (!clipState.empty && !bl) {
                    GpuUiRenderer.addShadow(class_112462, shadow, (Matrix3x2fc)matrix3x2f, clipState);
                }
            } else if (uiDrawCommand instanceof UiDrawCommand.Fluid) {
                UiDrawCommand.Fluid fluid = (UiDrawCommand.Fluid)uiDrawCommand;
                if (!clipState.empty && !bl) {
                    GpuFluidSimulation.render(class_112462, (Matrix3x2fc)matrix3x2f, fluid.x(), fluid.y(), fluid.width(), fluid.height(), fluid.radius(), fluid.cursorU(), fluid.cursorV(), fluid.cursorVelU(), fluid.cursorVelV(), fluid.dye0(), fluid.dye1(), fluid.dye2(), fluid.colorCount(), fluid.opacity(), fluid.flow(), fluid.inject(), clipState.scissor, clipState.mask);
                }
            }
            ++n6;
        }
        GpuUiRenderer.flushBatch();
        if (!arrayDeque.isEmpty()) {
            throw new IllegalStateException(GpuUiRenderer.fk("\ue240\u416d\u4356\u7352\ubfb8\ub38c\u91eb\u69b2\ue821\udbef\ue725\u0ec8\uc5e7\u0e4a\ub4f3\u1a9a\u8c94\u823c\u44c2\u515f\uf845\ub915\u5443\u4c5f\u868f\u5b99\uddec\u94ca\ub804\uc71a\ud8b4\uda3c\ud6a0\u37a8\u233a\u4bf2\ucb30\u2aaa\u75f1\udcc3\ub105\u7b52\u8c6f\u21cc\u76cd\u6dfb\u932c\u4e04\u053b\u7385", -1251238413, 0x57FC071B ^ CryptoVault.mask()));
        }
    }

    private static UiDrawCommand.ClipPush snapClip(UiDrawCommand.ClipPush clipPush) {
        if (!(pixelSpace && !(pixelsPerGuiUnit <= 1.0f) && Float.isFinite(clipPush.x()) && Float.isFinite(clipPush.y()) && Float.isFinite(clipPush.width()) && Float.isFinite(clipPush.height()))) {
            return clipPush;
        }
        float f2 = pixelsPerGuiUnit;
        float f3 = (float)Math.round(clipPush.x() / f2) * f2;
        float f4 = (float)Math.round(clipPush.y() / f2) * f2;
        float f5 = (float)Math.round((clipPush.x() + clipPush.width()) / f2) * f2;
        float f6 = (float)Math.round((clipPush.y() + clipPush.height()) / f2) * f2;
        return new UiDrawCommand.ClipPush(f3, f4, Math.max(0.0f, f5 - f3), Math.max(0.0f, f6 - f4), clipPush.radius());
    }

    private static void addShadow(class_11246 class_112462, UiDrawCommand.Shadow shadow, Matrix3x2fc matrix3x2fc, ClipState clipState) {
        float f2 = shadow.width();
        float f3 = shadow.height();
        if (!(f2 > 0.0f) || !(f3 > 0.0f)) {
            return;
        }
        if (!SafePipelines.usable(SHADOW_PIPELINE)) {
            return;
        }
        float f4 = GpuUiRenderer.clampFinite(shadow.spread(), 0.0f, 64.0f);
        float f5 = GpuUiRenderer.clampFinite(shadow.softness(), 0.0f, 64.0f);
        float f6 = Math.min(GpuUiRenderer.clampFinite(shadow.radius(), 0.0f, Math.min(f2, f3) * 0.5f), Math.min(f2, f3) * 0.5f);
        float f7 = f4 + f5;
        float f8 = shadow.x() - f7;
        float f9 = shadow.y() - f7;
        float f10 = f2 + f7 * 2.0f;
        float f11 = f3 + f7 * 2.0f;
        class_8030 class_80302 = GpuUiRenderer.screenBounds(f8, f9, f10, f11, matrix3x2fc);
        class_8030 class_80303 = GpuUiRenderer.intersectionForDraw(clipState, class_80302);
        if (clipState.scissor != null && class_80303 == null) {
            return;
        }
        GpuUiRenderer.submit(class_112462, new ShadowState(matrix3x2fc, f8, f9, f10, f11, f7, f2, f3, f6, f5, f4, shadow.argb(), clipState.mask.withMask(class_11231.method_70899()), class_80303 != null ? class_80303 : class_80302, clipState.scissor));
    }

    private static float clampFinite(float f2, float f3, float f4) {
        return Float.isFinite(f2) ? Math.max(f3, Math.min(f2, f4)) : f3;
    }

    private static void drawText(class_332 class_3322, class_11246 class_112462, UiDrawCommand.Text text, Matrix3x2fc matrix3x2fc, ClipState clipState) {
        if (text.size() <= 0.0f || text.text().isEmpty()) {
            return;
        }
        UiDrawCommand.TextWeight textWeight = text.weight();
        String string = I18n.tr(text.text());
        boolean bl = GpuUiFontMetrics.needsFallback(string, textWeight);
        if (bl && SafePipelines.usable(IMAGE_PIPELINE) && GpuUiAwtText.usable(string, textWeight) && GpuUiRenderer.drawAwtText(class_112462, text, string, matrix3x2fc, clipState)) {
            return;
        }
        if (!SafePipelines.usable(TEXT_PIPELINE) || bl) {
            GpuUiRenderer.drawVanillaText(class_3322, text, string, matrix3x2fc, clipState);
            return;
        }
        GpuUiFontMetrics.Face face = GpuUiFontMetrics.face(textWeight);
        class_11231 class_112312 = GpuUiRenderer.fontTexture(face);
        if (class_112312 == null) {
            return;
        }
        float f10 = pixelSpace ? (float)Math.round(text.x()) : text.x();
        float f11 = pixelSpace ? (float)Math.round(text.y()) : text.y();
        int n2 = text.argb();
        GpuUiFontMetrics.layoutPrepared(string, f10, f11, text.size(), textWeight, (f2, f3, f4, f5, f6, f7, f8, f9) -> {
            if (f4 > 0.0f && f5 > 0.0f) {
                GpuUiRenderer.batchQuad(class_112462, TEXT_PIPELINE, class_112312, clipState, matrix3x2fc, f2, f3, f4, f5, 0.0f, n2, 0.0f, f6, f7, f8, f9, true, false);
            }
        });
    }

    private static boolean drawAwtText(class_11246 class_112462, UiDrawCommand.Text text, String string, Matrix3x2fc matrix3x2fc, ClipState clipState) {
        class_1041 class_10412 = class_310.method_1551().method_22683();
        float f2 = (float)Math.sqrt(matrix3x2fc.m00() * matrix3x2fc.m00() + matrix3x2fc.m01() * matrix3x2fc.m01());
        float f3 = (float)class_10412.method_4495() * f2;
        if (!(f3 > 0.0f)) {
            return false;
        }
        float f4 = text.size() * GpuUiFontMetrics.textScale();
        GpuUiAwtText.Entry entry = GpuUiAwtText.get(string, text.weight(), f4 * f3);
        if (entry == null) {
            return false;
        }
        class_11231 class_112312 = GpuUiRenderer.imageTexture(entry.id());
        if (class_112312 == null) {
            return false;
        }
        float f5 = f4 / (float)entry.bucket();
        float f6 = text.y() + text.size() * 0.6055f + 0.36f * f4;
        float f7 = text.x() - (float)entry.padding() * f5;
        float f8 = f6 - ((float)entry.padding() + entry.ascent()) * f5;
        GpuUiRenderer.addQuad(class_112462, f7, f8, (float)entry.width() * f5, (float)entry.height() * f5, 0.0f, text.argb(), matrix3x2fc, clipState, class_112312, 0.0f, 0.0f, 1.0f, 1.0f, false, true, 0.0f);
        return true;
    }

    private static void addQuad(class_11246 class_112462, float f2, float f3, float f4, float f5, float f6, int n2, Matrix3x2fc matrix3x2fc, ClipState clipState, class_11231 class_112312, float f7, float f8, float f9, float f10, boolean bl, boolean bl2, float f11) {
        GpuUiRenderer.addQuad(class_112462, f2, f3, f4, f5, f6, n2, matrix3x2fc, clipState, class_112312, f7, f8, f9, f10, bl, bl2, f11, TEXT_PIPELINE);
    }

    private static void addQuad(class_11246 class_112462, float f2, float f3, float f4, float f5, float f6, int n2, Matrix3x2fc matrix3x2fc, ClipState clipState, class_11231 class_112312, float f7, float f8, float f9, float f10, boolean bl, boolean bl2, float f11, RenderPipeline renderPipeline) {
        RenderPipeline renderPipeline2;
        float f12;
        if (!(f4 > 0.0f) || !(f5 > 0.0f)) {
            return;
        }
        if (!(bl || bl2 && !pixelSpace)) {
            float f13;
            float f14;
            float f15;
            if (f6 > 0.0f && f6 * 2.0f >= Math.min(f4, f5) - 0.01f) {
                f15 = Math.max(1.0f, (float)Math.round(f4));
                f12 = Math.max(1.0f, (float)Math.round(f5));
                f14 = Math.round(f2 + (f4 - f15) * 0.5f);
                f13 = Math.round(f3 + (f5 - f12) * 0.5f);
                f6 = Math.min(f15, f12) * 0.5f;
            } else {
                f14 = Math.round(f2);
                f13 = Math.round(f3);
                f15 = Math.max(1.0f, (float)Math.round(f2 + f4) - f14);
                f12 = Math.max(1.0f, (float)Math.round(f3 + f5) - f13);
            }
            f2 = f14;
            f3 = f13;
            f4 = f15;
            f5 = f12;
            if (f11 > 0.0f) {
                f11 = Math.max(1.0f, pixelSpace ? (float)Math.round(f11) : f11);
            }
        }
        RenderPipeline renderPipeline3 = bl ? renderPipeline : (renderPipeline2 = bl2 ? IMAGE_PIPELINE : RECT_PIPELINE);
        if (!SafePipelines.usable(renderPipeline2)) {
            class_8030 class_80302 = GpuUiRenderer.screenBounds(f2, f3, f4, f5, matrix3x2fc);
            class_8030 class_80303 = GpuUiRenderer.intersectionForDraw(clipState, class_80302);
            if (clipState.scissor != null && class_80303 == null) {
                return;
            }
            if (bl) {
                return;
            }
            if (!bl2 && f11 > 0.0f) {
                f12 = Math.min(f11, Math.min(f4, f5) * 0.5f);
                GpuUiRenderer.addFlatQuad(class_112462, f2, f3, f4, f12, n2, matrix3x2fc, clipState);
                GpuUiRenderer.addFlatQuad(class_112462, f2, f3 + f5 - f12, f4, f12, n2, matrix3x2fc, clipState);
                GpuUiRenderer.addFlatQuad(class_112462, f2, f3 + f12, f12, f5 - 2.0f * f12, n2, matrix3x2fc, clipState);
                GpuUiRenderer.addFlatQuad(class_112462, f2 + f4 - f12, f3 + f12, f12, f5 - 2.0f * f12, n2, matrix3x2fc, clipState);
                return;
            }
            GpuUiRenderer.submit(class_112462, new QuadState(bl2 ? class_10799.field_56883 : class_10799.field_56879, bl2 ? class_112312 : class_11231.method_70899(), matrix3x2fc, f2, f3, f4, f5, 0.0f, n2, 0.0f, f7, f8, f9, f10, false, bl2, class_80303 != null ? class_80303 : class_80302, clipState.scissor));
            return;
        }
        class_11231 class_112313 = bl || bl2 ? class_112312 : class_11231.method_70899();
        GpuUiRenderer.batchQuad(class_112462, renderPipeline2, class_112313, clipState, matrix3x2fc, f2, f3, f4, f5, Math.min(Math.max(f6, 0.0f), Math.min(f4, f5) * 0.5f), n2, Math.max(0.0f, Math.min(f11, Math.min(f4, f5) * 0.5f)), f7, f8, f9, f10, bl, bl2);
    }

    private static void batchQuad(class_11246 class_112462, RenderPipeline renderPipeline, class_11231 class_112312, ClipState clipState, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, int n2, float f7, float f8, float f9, float f10, float f11, boolean bl, boolean bl2) {
        Object object;
        int n3;
        float f12 = matrix3x2fc.m00() * f2 + matrix3x2fc.m10() * f3 + matrix3x2fc.m20();
        float f13 = matrix3x2fc.m01() * f2 + matrix3x2fc.m11() * f3 + matrix3x2fc.m21();
        float f14 = matrix3x2fc.m00() * (f2 + f4) + matrix3x2fc.m10() * (f3 + f5) + matrix3x2fc.m20();
        float f15 = matrix3x2fc.m01() * (f2 + f4) + matrix3x2fc.m11() * (f3 + f5) + matrix3x2fc.m21();
        int n4 = (int)Math.floor(Math.min(f12, f14));
        int n5 = (int)Math.floor(Math.min(f13, f15));
        int n6 = (int)Math.ceil(Math.max(f12, f14));
        int n7 = (int)Math.ceil(Math.max(f13, f15));
        class_8030 class_80302 = clipState.scissor;
        if (class_80302 != null) {
            n4 = Math.max(n4, class_80302.method_49620());
            n5 = Math.max(n5, class_80302.method_49618());
            n6 = Math.min(n6, class_80302.method_49621());
            n7 = Math.min(n7, class_80302.method_49619());
            if (n6 <= n4 || n7 <= n5) {
                return;
            }
        }
        QuadBatch quadBatch = null;
        for (n3 = openBatches - 1; n3 >= 0; --n3) {
            object = BATCHES[n3];
            if (((QuadBatch)object).accepts(class_112462, renderPipeline, class_112312, clipState.mask, matrix3x2fc, class_80302, bl, bl2)) {
                quadBatch = object;
                break;
            }
            if (n4 < ((QuadBatch)object).right && n6 > ((QuadBatch)object).left && n5 < ((QuadBatch)object).bottom && n7 > ((QuadBatch)object).top) break;
        }
        if (quadBatch == null) {
            if (openBatches == 48) {
                GpuUiRenderer.flushOldestBatch();
            }
            if ((quadBatch = BATCHES[openBatches]) == null) {
                quadBatch = GpuUiRenderer.BATCHES[GpuUiRenderer.openBatches] = new QuadBatch();
            }
            ++openBatches;
            quadBatch.count = 0;
            quadBatch.state = class_112462;
            quadBatch.pipeline = renderPipeline;
            quadBatch.base = class_112312;
            quadBatch.mask = clipState.mask;
            quadBatch.pose = matrix3x2fc;
            quadBatch.scissorArea = class_80302;
            quadBatch.text = bl;
            quadBatch.image = bl2;
            quadBatch.left = n4;
            quadBatch.top = n5;
            quadBatch.right = n6;
            quadBatch.bottom = n7;
        } else {
            quadBatch.left = Math.min(quadBatch.left, n4);
            quadBatch.top = Math.min(quadBatch.top, n5);
            quadBatch.right = Math.max(quadBatch.right, n6);
            quadBatch.bottom = Math.max(quadBatch.bottom, n7);
        }
        n3 = quadBatch.count * 11;
        if (n3 + 11 > quadBatch.data.length) {
            quadBatch.data = Arrays.copyOf(quadBatch.data, quadBatch.data.length * 2);
        }
        object = quadBatch.data;
        object[n3] = f2;
        object[n3 + 1] = f3;
        object[n3 + 2] = f4;
        object[n3 + 3] = f5;
        object[n3 + 4] = f6;
        object[n3 + 5] = f7;
        object[n3 + 6] = f8;
        object[n3 + 7] = f9;
        object[n3 + 8] = f10;
        object[n3 + 9] = f11;
        object[n3 + 10] = Float.intBitsToFloat(n2);
        ++quadBatch.count;
    }

    private static void flushBatch() {
        int n2 = openBatches;
        if (n2 == 0) {
            return;
        }
        openBatches = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            GpuUiRenderer.submitBatch(BATCHES[i2]);
        }
    }

    private static void flushOldestBatch() {
        QuadBatch quadBatch = BATCHES[0];
        System.arraycopy(BATCHES, 1, BATCHES, 0, openBatches - 1);
        GpuUiRenderer.BATCHES[GpuUiRenderer.openBatches - 1] = quadBatch;
        --openBatches;
        GpuUiRenderer.submitBatch(quadBatch);
    }

    private static void submitBatch(QuadBatch quadBatch) {
        int n2 = quadBatch.count;
        quadBatch.count = 0;
        class_11246 class_112462 = quadBatch.state;
        class_11231 class_112312 = quadBatch.mask.withMask(quadBatch.base);
        QuadBatchState quadBatchState = new QuadBatchState(quadBatch.pipeline, class_112312, quadBatch.pose, Arrays.copyOf(quadBatch.data, n2 * 11), n2, quadBatch.text, quadBatch.image, new class_8030(quadBatch.left, quadBatch.top, quadBatch.right - quadBatch.left, quadBatch.bottom - quadBatch.top), quadBatch.scissorArea);
        quadBatch.state = null;
        quadBatch.pipeline = null;
        quadBatch.base = null;
        quadBatch.mask = null;
        quadBatch.pose = null;
        quadBatch.scissorArea = null;
        GpuUiRenderer.submitRaw(class_112462, quadBatchState);
    }

    private static void addFlatQuad(class_11246 class_112462, float f2, float f3, float f4, float f5, int n2, Matrix3x2fc matrix3x2fc, ClipState clipState) {
        if (!(f4 > 0.0f) || !(f5 > 0.0f)) {
            return;
        }
        class_8030 class_80302 = GpuUiRenderer.screenBounds(f2, f3, f4, f5, matrix3x2fc);
        class_8030 class_80303 = GpuUiRenderer.intersectionForDraw(clipState, class_80302);
        if (clipState.scissor != null && class_80303 == null) {
            return;
        }
        GpuUiRenderer.submit(class_112462, new QuadState(class_10799.field_56879, class_11231.method_70899(), matrix3x2fc, f2, f3, f4, f5, 0.0f, n2, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, false, false, class_80303 != null ? class_80303 : class_80302, clipState.scissor));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void drawVanillaText(class_332 class_3322, UiDrawCommand.Text text, String string, Matrix3x2fc matrix3x2fc, ClipState clipState) {
        boolean bl;
        GpuUiRenderer.flushBatch();
        class_310 class_3102 = class_310.method_1551();
        if (class_3322 == null || class_3102 == null || class_3102.field_1772 == null) {
            return;
        }
        Matrix3x2fStack matrix3x2fStack = class_3322.method_51448();
        boolean bl2 = bl = clipState.scissor != null;
        if (bl) {
            matrix3x2fStack.pushMatrix();
            matrix3x2fStack.identity();
            ScissorClamp.enable(class_3322, clipState.scissor.method_49620(), clipState.scissor.method_49618(), clipState.scissor.method_49621(), clipState.scissor.method_49619());
            matrix3x2fStack.popMatrix();
        }
        matrix3x2fStack.pushMatrix();
        try {
            matrix3x2fStack.set(matrix3x2fc);
            float f2 = text.size() * GpuUiFontMetrics.textScale() * 0.92f;
            Objects.requireNonNull(class_3102.field_1772);
            float f3 = f2 / 9.0f;
            matrix3x2fStack.translate(text.x(), text.y() + text.size() * 0.6055f - 3.6f * f3);
            matrix3x2fStack.scale(f3, f3);
            class_2561 class_25612 = GpuUiFallbackText.component(string, text.weight());
            class_3322.method_51439(class_3102.field_1772, class_25612, 0, 0, text.argb(), false);
        }
        finally {
            matrix3x2fStack.popMatrix();
            if (bl) {
                class_3322.method_44380();
            }
        }
    }

    private static class_8030 intersectionForDraw(ClipState clipState, class_8030 class_80302) {
        return clipState.scissor == null ? null : clipState.scissor.method_49701(class_80302);
    }

    private static List<UiDrawCommand.ClipPush> append(List<UiDrawCommand.ClipPush> list, UiDrawCommand.ClipPush clipPush) {
        ArrayList<UiDrawCommand.ClipPush> arrayList = new ArrayList<UiDrawCommand.ClipPush>(list.size() + 1);
        arrayList.addAll(list);
        arrayList.add(clipPush);
        return List.copyOf(arrayList);
    }

    private static int physicalSize(float f2, int n2, int n3) {
        if (!(f2 > 0.0f) || n2 <= 0 || n3 <= 0) {
            return 0;
        }
        double d2 = Math.ceil((double)(f2 * (float)n2) / (double)n3);
        return (int)Math.max(1.0, Math.min(2.147483647E9, d2));
    }

    private static class_11231 imageTexture(class_2960 class_29602) {
        return GpuUiRenderer.imageTexture(class_29602, false);
    }

    private static class_11231 imageTexture(class_2960 class_29602, boolean bl) {
        if (class_29602 == null) {
            return null;
        }
        class_1044 class_10442 = class_310.method_1551().method_1531().method_4619(class_29602);
        class_12137 class_121372 = RenderSystem.getSamplerCache().method_75294(bl ? FilterMode.NEAREST : FilterMode.LINEAR);
        if (class_10442.method_71659() == null || class_121372 == null) {
            return null;
        }
        return class_11231.method_70900((GpuTextureView)class_10442.method_71659(), (class_12137)class_121372);
    }

    private static class_8030 screenBounds(float f2, float f3, float f4, float f5, Matrix3x2fc matrix3x2fc) {
        Vector2f vector2f = matrix3x2fc.transformPosition(f2, f3, new Vector2f());
        Vector2f vector2f2 = matrix3x2fc.transformPosition(f2 + f4, f3 + f5, new Vector2f());
        int n2 = (int)Math.floor(Math.min(vector2f.x, vector2f2.x));
        int n3 = (int)Math.floor(Math.min(vector2f.y, vector2f2.y));
        int n4 = (int)Math.ceil(Math.max(vector2f.x, vector2f2.x));
        int n5 = (int)Math.ceil(Math.max(vector2f.y, vector2f2.y));
        return new class_8030(n2, n3, Math.max(0, n4 - n2), Math.max(0, n5 - n3));
    }

    private static class_8030 rect(float f2, float f3, float f4, float f5) {
        int n2 = (int)Math.floor(f2);
        int n3 = (int)Math.floor(f3);
        int n4 = (int)Math.ceil(f2 + f4);
        int n5 = (int)Math.ceil(f3 + f5);
        return new class_8030(n2, n3, Math.max(0, n4 - n2), Math.max(0, n5 - n3));
    }

    public static float textWidth(String string, float f2) {
        return GpuUiRenderer.textWidth(string, f2, UiDrawCommand.TextWeight.REGULAR);
    }

    public static float textWidth(String string, float f2, UiDrawCommand.TextWeight textWeight) {
        return GpuUiFontMetrics.textWidth(string, f2, textWeight);
    }

    private static class_11231 fontTexture(GpuUiFontMetrics.Face face) {
        class_1044 class_10442 = class_310.method_1551().method_1531().method_4619(face.texture());
        if (class_10442.method_71659() == null) {
            return null;
        }
        class_12137 class_121372 = RenderSystem.getSamplerCache().method_75293(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, false);
        return class_11231.method_70900((GpuTextureView)class_10442.method_71659(), (class_12137)class_121372);
    }

    static {
        UI_FORMAT = VertexFormat.builder().add(GpuUiRenderer.fk("\u6c26\ub6f0\ub6be\u8bd8\ua66b\u9c7a\ub4b2\u3460\u53ee", 718469370, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuUiRenderer.fk("\uf1dc\u20e5\u860e\u93be\uafdc\u37aa\u4989", -928331408, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.COLOR).add(GpuUiRenderer.fk("\ua406\u365a\u1f08\uc856", 757907386, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV0).add(GpuUiRenderer.fk("\u9b7e\u86fd\u6770\ubd81\u6f25\uc07f\u28f9", 270009360, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV1).add(GpuUiRenderer.fk("\u8f1e\u8233\u30db\u09a9\u4ddf\u143f\uf1ce\u01a6\u5326\u8a1b\u76e3\u7b7b\u9e30", 61598924, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.LINE_WIDTH).add(GpuUiRenderer.fk("\u5640\u3985\ub081\u85aa\u35fe", -850319223, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV2).build();
        SHADOW_FORMAT = VertexFormat.builder().add(GpuUiRenderer.fk("\ub295\u36e6\u69fd\u7ec9\u18b1\u055a\ua3c0\u84ea\u3cb3\uca4a\ub5be\u1bee", 837302954, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuUiRenderer.fk("\u5f0c\u8b44\u8346\u420c\ub99b\u5c5b\u3b8d", -629786710, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.COLOR).add(GpuUiRenderer.fk("\uf0a3\uc181\uab32\u208c\u7a14\u9ed5\u0e12", -1360513626, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV0).add(GpuUiRenderer.fk("\u32b9\u1dcc\u8fac\u91b4\u085b\u8628", -1264913184, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV1).add(GpuUiRenderer.fk("\ua267\u461f\u1d59\u4569\u913c\ub36d\ufd3e\u8fbc\ubaa4\u5b75", -123898220, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.LINE_WIDTH).add(GpuUiRenderer.fk("\u6d6d\uc1ae\u2398\u4abf\u42ab\ucaf7", 306404281, 0x57FC071B ^ CryptoVault.mask()), VertexFormatElement.UV2).build();
        RECT_PIPELINE = GpuUiRenderer.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuUiRenderer.fk("\u83db\u7f0b\uf097\u2e76\u8b8a\u41f6\ubb4e\uc324\u2988\uf572\u408f\u6290\u67c1", 1745591052, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u9401\u68d3\u12a2\u21be\u443b\ue45d\ucf7c\u4bf6\u1317\u9aa6\u9883\u541a\u2d2a\u5072\u48af\u61cb\ue3a1\u6cb1\uf07c\ub1be", 277379025, 0x57FC071B ^ CryptoVault.mask()))).withVertexShader(RECT_VERTEX).withFragmentShader(RECT_FRAGMENT).withVertexFormat(UI_FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuUiRenderer.fk("\uf67e\ubc87\ud55c\u6a42\u3e9b\ubb29\u97ee\ue7a1\ue0c7", 1951187015, 0x57FC071B ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
        TEXT_PIPELINE = GpuUiRenderer.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuUiRenderer.fk("\ucd52\uda08\u9e5d\ucaf9\ub3e8\u68e0\u2e46\u90f3\u8102\u5802\u3363\u7f72\ua07f\ue3f6\udd8d", -1567529215, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u6df1\u93db\u7ae5\u83a5\u9a20\u8714\ufc10\u2880\uf2b6\u49bf\u1512\u43d4\ub2a5\uc742\ub8f9\ubc22\u27b7\u502d\u74f7\u6fc0\u16d5\ueda0", -1633283604, 0x57FC071B ^ CryptoVault.mask()))).withVertexShader(TEXT_VERTEX).withFragmentShader(TEXT_FRAGMENT).withVertexFormat(UI_FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuUiRenderer.fk("\u9153\u673e\ufa5b\u047e\uaea4\u6a69\ue280\u4de2\u3468\uc578\u110c", -2024090430, 0x57FC071B ^ CryptoVault.mask())).withSampler(GpuUiRenderer.fk("\u9094\u5e22\u81be\ub950\ubb69\u6fc9\ufc84\ua421\u5fb8", -1049906190, 0x57FC071B ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
        SHADOW_PIPELINE = GpuUiRenderer.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuUiRenderer.fk("\uf0bb\u352f\uef76\uba1b\u0665\uccb6\u57b0\u1eba\u5c4d\ucb38\u32c4\u3816\u9f78", 1698879894, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\u3433\ub125\ub208\u3b3a\u3d90\ueb07\u4f1f\u957c\u5c93\uf3c1\u92f8\u2486\u5d54\u823f\u808b\ue506\u3c60\u0a6a\ucba5\u93e9\ube99\u4ca6\u7594", 1220736891, 0x57FC071B ^ CryptoVault.mask()))).withVertexShader(SHADOW_VERTEX).withFragmentShader(SHADOW_FRAGMENT).withVertexFormat(SHADOW_FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuUiRenderer.fk("\ubf4a\ue11f\u28be\u6d2f\ub392\u405c\ua82a\u49c3\u4fbf", 771786436, 0x57FC071B ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
        IMAGE_PIPELINE = GpuUiRenderer.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56863}).withLocation(class_2960.method_60655((String)GpuUiRenderer.fk("\u202a\u2061\u83af\ue146\u46b2\u68eb\uc89d\u57ab\uaa12\ua447\ud2f5\ud26f", -387427679, 0x57FC071B ^ CryptoVault.mask()), (String)GpuUiRenderer.fk("\ud045\u4e9a\ue4fd\ua15a\u6d3f\u21e4\uc789\ufc48\uf637\ueb63\ue980\u5d73\ud56c\u754a\u9946\u3bfe\u3d18\uac71\ue1af\u2ea1\u2690\u6df1\uc38e\u8ece", 1358060540, 0x57FC071B ^ CryptoVault.mask()))).withVertexShader(IMAGE_VERTEX).withFragmentShader(IMAGE_FRAGMENT).withVertexFormat(UI_FORMAT, VertexFormat.class_5596.field_27382).withSampler(GpuUiRenderer.fk("\uba50\u6f55\uc40a\ub8cc\u8590\u144b\ub39d\u4702\u2afb", 1220432853, 0x57FC071B ^ CryptoVault.mask())).withSampler(GpuUiRenderer.fk("\u5cec\uf99e\u9f60\u1903\u8477\u6a6f\u1b9e\ufd5c\u802e\u94ff\u70fb\u3bf0", 1421872915, 0x57FC071B ^ CryptoVault.mask())).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withCull(false).withBlend(BlendFunction.TRANSLUCENT).build());
        pixelsPerGuiUnit = 1.0f;
        FENCE_POSE = new Matrix3x2f();
        BATCHES = new QuadBatch[48];
    }

    private static /* synthetic */ String fk(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    private static final class QuadState
    extends Record
    implements class_11244 {
        private final RenderPipeline pipeline;
        private final class_11231 textureSetup;
        private final Matrix3x2fc pose;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final float radius;
        private final int color;
        private final float borderThickness;
        private final float u0;
        private final float v0;
        private final float u1;
        private final float v1;
        private final boolean text;
        private final boolean image;
        private final class_8030 bounds;
        private final class_8030 scissorArea;

        private QuadState(RenderPipeline renderPipeline, class_11231 class_112312, Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, int n2, float f7, float f8, float f9, float f10, float f11, boolean bl, boolean bl2, class_8030 class_80302, class_8030 class_80303) {
            this.pipeline = renderPipeline;
            this.textureSetup = class_112312;
            this.pose = matrix3x2fc;
            this.x = f2;
            this.y = f3;
            this.width = f4;
            this.height = f5;
            this.radius = f6;
            this.color = n2;
            this.borderThickness = f7;
            this.u0 = f8;
            this.v0 = f9;
            this.u1 = f10;
            this.v1 = f11;
            this.text = bl;
            this.image = bl2;
            this.bounds = class_80302;
            this.scissorArea = class_80303;
        }

        public void method_70917(class_4588 class_45882) {
            int n2 = Math.min(65535, Math.max(1, Math.round(this.width)));
            int n3 = Math.min(65535, Math.max(1, Math.round(this.height)));
            int n4 = n3 << 16 | n2;
            this.vertex(class_45882, this.x, this.y, 0.0f, 0.0f, this.u0, this.v0, n4);
            this.vertex(class_45882, this.x + this.width, this.y, this.width, 0.0f, this.u1, this.v0, n4);
            this.vertex(class_45882, this.x + this.width, this.y + this.height, this.width, this.height, this.u1, this.v1, n4);
            this.vertex(class_45882, this.x, this.y + this.height, 0.0f, this.height, this.u0, this.v1, n4);
        }

        private void vertex(class_4588 class_45882, float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            int n3 = this.image ? QuadState.packNormalized(f4, this.width, f5, this.height) : Math.min(Short.MAX_VALUE, Math.max(0, Math.round(this.borderThickness * 256.0f)));
            class_45882.method_70815(this.pose, f2, f3).method_39415(this.color).method_22913(this.text || this.image ? f6 : f4, this.text || this.image ? f7 : f5).method_22922(n2).method_75298(this.radius).method_60803(n3);
        }

        private static int packNormalized(float f2, float f3, float f4, float f5) {
            int n2 = Math.round(GpuUiRenderer.clampFinite(f2 / f3, 0.0f, 1.0f) * 65535.0f);
            int n3 = Math.round(GpuUiRenderer.clampFinite(f4 / f5, 0.0f, 1.0f) * 65535.0f);
            return n3 << 16 | n2;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{QuadState.class, "pipeline;textureSetup;pose;x;y;width;height;radius;color;borderThickness;u0;v0;u1;v1;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "radius", "color", "borderThickness", "u0", "v0", "u1", "v1", "text", "image", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{QuadState.class, "pipeline;textureSetup;pose;x;y;width;height;radius;color;borderThickness;u0;v0;u1;v1;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "radius", "color", "borderThickness", "u0", "v0", "u1", "v1", "text", "image", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{QuadState.class, "pipeline;textureSetup;pose;x;y;width;height;radius;color;borderThickness;u0;v0;u1;v1;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "x", "y", "width", "height", "radius", "color", "borderThickness", "u0", "v0", "u1", "v1", "text", "image", "bounds", "scissorArea"}, this, object);
        }

        public RenderPipeline comp_4055() {
            return this.pipeline;
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

        public float radius() {
            return this.radius;
        }

        public int color() {
            return this.color;
        }

        public float borderThickness() {
            return this.borderThickness;
        }

        public float u0() {
            return this.u0;
        }

        public float v0() {
            return this.v0;
        }

        public float u1() {
            return this.u1;
        }

        public float v1() {
            return this.v1;
        }

        public boolean text() {
            return this.text;
        }

        public boolean image() {
            return this.image;
        }

        public class_8030 comp_4274() {
            return this.bounds;
        }

        public class_8030 comp_4069() {
            return this.scissorArea;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record ClipState(class_8030 scissor, GpuUiClipMask mask, boolean empty, List<UiDrawCommand.ClipPush> chain) {
        private static ClipState root() {
            return new ClipState(null, GpuUiClipMask.none(), false, List.of());
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class GradientState
    extends Record
    implements class_11244 {
        private final Matrix3x2fc pose;
        private final UiDrawCommand.GradientRect gradient;
        private final RenderPipeline pipeline;
        private final class_11231 textureSetup;
        private final class_8030 bounds;
        private final class_8030 scissorArea;

        private GradientState(Matrix3x2fc matrix3x2fc, UiDrawCommand.GradientRect gradientRect, RenderPipeline renderPipeline, class_11231 class_112312, class_8030 class_80302, class_8030 class_80303) {
            this.pose = matrix3x2fc;
            this.gradient = gradientRect;
            this.pipeline = renderPipeline;
            this.textureSetup = class_112312;
            this.bounds = class_80302;
            this.scissorArea = class_80303;
        }

        public void method_70917(class_4588 class_45882) {
            float f2 = Math.round(this.gradient.x());
            float f3 = Math.round(this.gradient.y());
            float f4 = Math.max(1.0f, (float)Math.round(this.gradient.x() + this.gradient.width()) - f2);
            float f5 = Math.max(1.0f, (float)Math.round(this.gradient.y() + this.gradient.height()) - f3);
            float f6 = Math.min(Math.max(this.gradient.radius(), 0.0f), Math.min(f4, f5) * 0.5f);
            int n2 = Math.min(65535, Math.max(1, Math.round(f4)));
            int n3 = Math.min(65535, Math.max(1, Math.round(f5)));
            int n4 = n3 << 16 | n2;
            this.vertex(class_45882, f2, f3, 0.0f, 0.0f, this.gradient.topLeft(), n4, f6);
            this.vertex(class_45882, f2 + f4, f3, f4, 0.0f, this.gradient.topRight(), n4, f6);
            this.vertex(class_45882, f2 + f4, f3 + f5, f4, f5, this.gradient.bottomRight(), n4, f6);
            this.vertex(class_45882, f2, f3 + f5, 0.0f, f5, this.gradient.bottomLeft(), n4, f6);
        }

        private void vertex(class_4588 class_45882, float f2, float f3, float f4, float f5, int n2, int n3, float f6) {
            class_45882.method_70815(this.pose, f2, f3).method_39415(n2).method_22913(f4, f5).method_22922(n3).method_75298(f6).method_60803(Float.floatToRawIntBits(0.0f));
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{GradientState.class, "pose;gradient;pipeline;textureSetup;bounds;scissorArea", "pose", "gradient", "pipeline", "textureSetup", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{GradientState.class, "pose;gradient;pipeline;textureSetup;bounds;scissorArea", "pose", "gradient", "pipeline", "textureSetup", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{GradientState.class, "pose;gradient;pipeline;textureSetup;bounds;scissorArea", "pose", "gradient", "pipeline", "textureSetup", "bounds", "scissorArea"}, this, object);
        }

        public Matrix3x2fc pose() {
            return this.pose;
        }

        public UiDrawCommand.GradientRect gradient() {
            return this.gradient;
        }

        public RenderPipeline comp_4055() {
            return this.pipeline;
        }

        public class_11231 comp_4056() {
            return this.textureSetup;
        }

        public class_8030 comp_4274() {
            return this.bounds;
        }

        public class_8030 comp_4069() {
            return this.scissorArea;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class ShadowState
    extends Record
    implements class_11244 {
        private final Matrix3x2fc pose;
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final float padding;
        private final float shapeWidth;
        private final float shapeHeight;
        private final float radius;
        private final float softness;
        private final float spread;
        private final int color;
        private final class_11231 textureSetup;
        private final class_8030 bounds;
        private final class_8030 scissorArea;

        private ShadowState(Matrix3x2fc matrix3x2fc, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, int n2, class_11231 class_112312, class_8030 class_80302, class_8030 class_80303) {
            this.pose = matrix3x2fc;
            this.x = f2;
            this.y = f3;
            this.width = f4;
            this.height = f5;
            this.padding = f6;
            this.shapeWidth = f7;
            this.shapeHeight = f8;
            this.radius = f9;
            this.softness = f10;
            this.spread = f11;
            this.color = n2;
            this.textureSetup = class_112312;
            this.bounds = class_80302;
            this.scissorArea = class_80303;
        }

        public RenderPipeline comp_4055() {
            return SHADOW_PIPELINE;
        }

        public void method_70917(class_4588 class_45882) {
            int n2 = Math.min(65535, Math.max(1, Math.round(this.shapeWidth)));
            int n3 = Math.min(65535, Math.max(1, Math.round(this.shapeHeight)));
            int n4 = n3 << 16 | n2;
            int n5 = Math.round(this.spread * 256.0f) << 16 | Math.round(this.softness * 256.0f);
            this.vertex(class_45882, this.x, this.y, -this.padding, -this.padding, n4, n5);
            this.vertex(class_45882, this.x + this.width, this.y, this.shapeWidth + this.padding, -this.padding, n4, n5);
            this.vertex(class_45882, this.x + this.width, this.y + this.height, this.shapeWidth + this.padding, this.shapeHeight + this.padding, n4, n5);
            this.vertex(class_45882, this.x, this.y + this.height, -this.padding, this.shapeHeight + this.padding, n4, n5);
        }

        private void vertex(class_4588 class_45882, float f2, float f3, float f4, float f5, int n2, int n3) {
            class_45882.method_70815(this.pose, f2, f3).method_39415(this.color).method_22913(f4, f5).method_22922(n2).method_75298(this.radius).method_60803(n3);
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{ShadowState.class, "pose;x;y;width;height;padding;shapeWidth;shapeHeight;radius;softness;spread;color;textureSetup;bounds;scissorArea", "pose", "x", "y", "width", "height", "padding", "shapeWidth", "shapeHeight", "radius", "softness", "spread", "color", "textureSetup", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ShadowState.class, "pose;x;y;width;height;padding;shapeWidth;shapeHeight;radius;softness;spread;color;textureSetup;bounds;scissorArea", "pose", "x", "y", "width", "height", "padding", "shapeWidth", "shapeHeight", "radius", "softness", "spread", "color", "textureSetup", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{ShadowState.class, "pose;x;y;width;height;padding;shapeWidth;shapeHeight;radius;softness;spread;color;textureSetup;bounds;scissorArea", "pose", "x", "y", "width", "height", "padding", "shapeWidth", "shapeHeight", "radius", "softness", "spread", "color", "textureSetup", "bounds", "scissorArea"}, this, object);
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

        public float padding() {
            return this.padding;
        }

        public float shapeWidth() {
            return this.shapeWidth;
        }

        public float shapeHeight() {
            return this.shapeHeight;
        }

        public float radius() {
            return this.radius;
        }

        public float softness() {
            return this.softness;
        }

        public float spread() {
            return this.spread;
        }

        public int color() {
            return this.color;
        }

        public class_11231 comp_4056() {
            return this.textureSetup;
        }

        public class_8030 comp_4274() {
            return this.bounds;
        }

        public class_8030 comp_4069() {
            return this.scissorArea;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class QuadBatch {
        class_11246 state;
        RenderPipeline pipeline;
        class_11231 base;
        GpuUiClipMask mask;
        Matrix3x2fc pose;
        class_8030 scissorArea;
        boolean text;
        boolean image;
        float[] data = new float[704];
        int count;
        int left;
        int top;
        int right;
        int bottom;

        private QuadBatch() {
        }

        boolean accepts(class_11246 class_112462, RenderPipeline renderPipeline, class_11231 class_112312, GpuUiClipMask gpuUiClipMask, Matrix3x2fc matrix3x2fc, class_8030 class_80302, boolean bl, boolean bl2) {
            return this.state == class_112462 && this.pipeline == renderPipeline && this.mask == gpuUiClipMask && this.pose == matrix3x2fc && this.text == bl && this.image == bl2 && Objects.equals(this.scissorArea, class_80302) && this.base.equals((Object)class_112312);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class QuadBatchState
    extends Record
    implements class_11244 {
        private final RenderPipeline pipeline;
        private final class_11231 textureSetup;
        private final Matrix3x2fc pose;
        private final float[] data;
        private final int count;
        private final boolean text;
        private final boolean image;
        private final class_8030 bounds;
        private final class_8030 scissorArea;

        private QuadBatchState(RenderPipeline renderPipeline, class_11231 class_112312, Matrix3x2fc matrix3x2fc, float[] fArray, int n2, boolean bl, boolean bl2, class_8030 class_80302, class_8030 class_80303) {
            this.pipeline = renderPipeline;
            this.textureSetup = class_112312;
            this.pose = matrix3x2fc;
            this.data = fArray;
            this.count = n2;
            this.text = bl;
            this.image = bl2;
            this.bounds = class_80302;
            this.scissorArea = class_80303;
        }

        public void method_70917(class_4588 class_45882) {
            for (int i2 = 0; i2 < this.count; ++i2) {
                int n2 = i2 * 11;
                float f2 = this.data[n2];
                float f3 = this.data[n2 + 1];
                float f4 = this.data[n2 + 2];
                float f5 = this.data[n2 + 3];
                float f6 = this.data[n2 + 4];
                float f7 = this.data[n2 + 5];
                float f8 = this.data[n2 + 6];
                float f9 = this.data[n2 + 7];
                float f10 = this.data[n2 + 8];
                float f11 = this.data[n2 + 9];
                int n3 = Float.floatToRawIntBits(this.data[n2 + 10]);
                int n4 = Math.min(65535, Math.max(1, Math.round(f4)));
                int n5 = Math.min(65535, Math.max(1, Math.round(f5)));
                int n6 = n5 << 16 | n4;
                this.vertex(class_45882, f2, f3, 0.0f, 0.0f, f8, f9, n6, n3, f6, f7, f4, f5);
                this.vertex(class_45882, f2 + f4, f3, f4, 0.0f, f10, f9, n6, n3, f6, f7, f4, f5);
                this.vertex(class_45882, f2 + f4, f3 + f5, f4, f5, f10, f11, n6, n3, f6, f7, f4, f5);
                this.vertex(class_45882, f2, f3 + f5, 0.0f, f5, f8, f11, n6, n3, f6, f7, f4, f5);
            }
        }

        private void vertex(class_4588 class_45882, float f2, float f3, float f4, float f5, float f6, float f7, int n2, int n3, float f8, float f9, float f10, float f11) {
            int n4 = this.image ? QuadState.packNormalized(f4, f10, f5, f11) : Math.min(Short.MAX_VALUE, Math.max(0, Math.round(f9 * 256.0f)));
            class_45882.method_70815(this.pose, f2, f3).method_39415(n3).method_22913(this.text || this.image ? f6 : f4, this.text || this.image ? f7 : f5).method_22922(n2).method_75298(f8).method_60803(n4);
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{QuadBatchState.class, "pipeline;textureSetup;pose;data;count;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "data", "count", "text", "image", "bounds", "scissorArea"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{QuadBatchState.class, "pipeline;textureSetup;pose;data;count;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "data", "count", "text", "image", "bounds", "scissorArea"}, this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{QuadBatchState.class, "pipeline;textureSetup;pose;data;count;text;image;bounds;scissorArea", "pipeline", "textureSetup", "pose", "data", "count", "text", "image", "bounds", "scissorArea"}, this, object);
        }

        public RenderPipeline comp_4055() {
            return this.pipeline;
        }

        public class_11231 comp_4056() {
            return this.textureSetup;
        }

        public Matrix3x2fc pose() {
            return this.pose;
        }

        public float[] data() {
            return this.data;
        }

        public int count() {
            return this.count;
        }

        public boolean text() {
            return this.text;
        }

        public boolean image() {
            return this.image;
        }

        public class_8030 comp_4274() {
            return this.bounds;
        }

        public class_8030 comp_4069() {
            return this.scissorArea;
        }
    }
}

