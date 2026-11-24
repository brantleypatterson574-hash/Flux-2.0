/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.buffers.GpuBufferSlice
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Builder
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.RenderPass
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.FilterMode
 *  com.mojang.blaze3d.textures.GpuTexture
 *  com.mojang.blaze3d.textures.GpuTextureView
 *  com.mojang.blaze3d.textures.TextureFormat
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  com.mojang.blaze3d.vertex.VertexFormatElement
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
 *  net.fabricmc.fabric.api.resource.v1.ResourceLoader
 *  net.minecraft.class_10789
 *  net.minecraft.class_11231
 *  net.minecraft.class_12137
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3264
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 *  org.slf4j.Logger
 *  org.slf4j.helpers.NOPLogger
 */
package com.ishland.scalablelux.gui.gpuui.render.effects;

import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_10789;
import net.minecraft.class_11231;
import net.minecraft.class_12137;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

@Environment(value=EnvType.CLIENT)
public final class GpuUiEffectPasses {
    private static final Logger LOGGER = NOPLogger.NOP_LOGGER;
    private static final class_2960 MATERIAL_VERTEX = class_2960.method_60655((String)GpuUiEffectPasses.yw("\u5965\u6b90\u052c\u761f\udceb\ud530\u079c\ua67f\u3b0f\u1634\uc81d\u60e3\ud543", -1720632705, 0xC4466BAA ^ CryptoVault.mask()), (String)GpuUiEffectPasses.yw("\u0f8a\ubf98\u9ce2\uee2d\uee70\u450a\ufd2d\u4bc2\u85d1\uc42a\uced3\u69e3\uf25d\u51af\u6551\u06e6\u703e\ue4b8\ud470\u2399", -1866635059, 0xC4466BAA ^ CryptoVault.mask()));
    private static final int PARAMETER_VEC4S = 16;
    private static final int PARAMETER_BYTES = 256;
    private static final int UNIFORM_PAGE_BYTES = 65536;
    private static final int MAX_POOLED_TARGETS = Runtime.getRuntime().maxMemory() < 0x60000000L ? 16 : 48;
    private static final int TARGET_USAGE = 12;
    private static final VertexFormat FULLSCREEN_FORMAT = VertexFormat.builder().add(GpuUiEffectPasses.yw("\u792d\ua8b6\u5f9a\u834e\u8e90\u6c60\ub1b1\u9f58\u074a\udcc2\u8307", -1426817559, 0xC4466BAA ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuUiEffectPasses.yw("\uf6cb\u81c2\u361b\ue2c7\u1ded\uc695", 1869837874, 0xC4466BAA ^ CryptoVault.mask()), VertexFormatElement.UV0).build();
    private static final List<Target> pooledTargets = new ArrayList<Target>();
    private static final ByteBuffer PARAMETER_SCRATCH = ByteBuffer.allocateDirect(256).order(ByteOrder.nativeOrder());
    private static final List<UniformPage> uniformPages = new ArrayList<UniformPage>();
    private static final List<QueuedPass> queuedPasses = new ArrayList<QueuedPass>();
    private static final Map<class_2960, RenderPipeline> materialPipelines = new HashMap<class_2960, RenderPipeline>();
    private static final Map<class_2960, RenderPipeline> proceduralPipelines = new HashMap<class_2960, RenderPipeline>();
    private static final Map<class_2960, RenderPipeline> dualMaterialPipelines = new HashMap<class_2960, RenderPipeline>();
    private static final Set<RenderPipeline> dualInputPipelines = new HashSet<RenderPipeline>();
    private static final Set<RenderPipeline> pipelinesWithoutInput = new HashSet<RenderPipeline>();
    private static final Set<FailedTarget> failedTargetAllocations = new HashSet<FailedTarget>();
    private static GpuBuffer fullscreenVertices;
    private static Target sceneTarget;
    private static Target whiteTarget;
    private static boolean sceneCaptureRequested;
    private static int uniformSlot;
    private static int lastSurfaceWidth;
    private static int lastSurfaceHeight;
    private static long surfaceGeneration;
    private static boolean warnedPassFailure;
    private static boolean effectsUnavailable;

    private GpuUiEffectPasses() {
    }

    public static void registerPipelines() {
    }

    public static Target createTarget(int n2, int n3) {
        if (effectsUnavailable || n2 <= 0 || n3 <= 0) {
            return null;
        }
        RenderSystem.assertOnRenderThread();
        GpuUiEffectPasses.ensureDevice();
        GpuUiEffectPasses.trackCurrentSurfaceSize();
        for (Target target : pooledTargets) {
            if (target.leased || target.width != n2 || target.height != n3 || target.format != TextureFormat.RGBA8 || target.generation != surfaceGeneration || target.closed()) continue;
            target.leased = true;
            return target;
        }
        Target target = GpuUiEffectPasses.allocateTarget(n2, n3, TextureFormat.RGBA8, 12, GpuUiEffectPasses.yw("\u0103\u3d8c\u49f5\uc8a7\ucc00\ud830\ue69c\uf7b1\u21a4\u6b9b\u3a96\u56fc\ub3d4\u0c3b\u986a\u2f26", 580198819, 0xC4466BAA ^ CryptoVault.mask()) + n2 + "x" + n3, surfaceGeneration);
        if (target == null) {
            return null;
        }
        target.leased = true;
        pooledTargets.add(target);
        return target;
    }

    public static Target createBucketedTarget(int n2, int n3) {
        if (n2 <= 0 || n3 <= 0) {
            return null;
        }
        return GpuUiEffectPasses.createTarget((n2 + 63) / 64 * 64, (n3 + 63) / 64 * 64);
    }

    public static void trimPool() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.queueFencedTask(GpuUiEffectPasses::trimPool);
            return;
        }
        pooledTargets.removeIf(target -> {
            if (target.leased) {
                return false;
            }
            GpuUiEffectPasses.closeTarget(target);
            return true;
        });
    }

    public static Target requestSceneTexture() {
        RenderSystem.assertOnRenderThread();
        if (GpuUiEffectPasses.blocked()) {
            return null;
        }
        GpuUiEffectPasses.ensureDevice();
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || McCompat.mainRenderTarget(class_3102) == null || McCompat.mainRenderTarget(class_3102).method_30277() == null) {
            return null;
        }
        GpuTexture gpuTexture = McCompat.mainRenderTarget(class_3102).method_30277();
        int n2 = gpuTexture.getWidth(0);
        int n3 = gpuTexture.getHeight(0);
        if (n2 <= 0 || n3 <= 0) {
            return null;
        }
        GpuUiEffectPasses.trackSurfaceSize(n2, n3);
        if (sceneTarget == null || GpuUiEffectPasses.sceneTarget.width != n2 || GpuUiEffectPasses.sceneTarget.height != n3 || GpuUiEffectPasses.sceneTarget.format != gpuTexture.getFormat() || sceneTarget.closed()) {
            GpuUiEffectPasses.closeTarget(sceneTarget);
            sceneTarget = GpuUiEffectPasses.allocateTarget(n2, n3, gpuTexture.getFormat(), 13, GpuUiEffectPasses.yw("\u1b27\u3812\uf311\u6e84\ud709\u849a\u2186\uaa2e\ud94f\uee3f\u922a\u5cf7\u95f9\uc633\u25cc\u61d8\ue5cf\ufcca\u24b2\u218b\u0d9d\u8327\u535c", 1974991706, 0xC4466BAA ^ CryptoVault.mask()), surfaceGeneration);
        }
        if (sceneTarget != null) {
            sceneCaptureRequested = true;
        }
        return sceneTarget;
    }

    public static GpuTextureView whiteTextureView() {
        RenderSystem.assertOnRenderThread();
        GpuUiEffectPasses.ensureDevice();
        if ((whiteTarget == null || whiteTarget.closed()) && (whiteTarget = GpuUiEffectPasses.allocateTarget(1, 1, TextureFormat.RGBA8, 13, GpuUiEffectPasses.yw("\u3495\u2869\u9439\u5885\ucfe5\ub344\u65e9\u7fa0\u5c1a\u0e42\u82cd\u21cc\u3626\u648e\u8cee\u9567\u6af6\u4b8d\u7745\ub1df\u4ff2\ub120\uf476\ub408\uf70d", 7668967, 0xC4466BAA ^ CryptoVault.mask()), surfaceGeneration)) != null) {
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(GpuUiEffectPasses.whiteTarget.texture, -1);
        }
        return whiteTarget == null ? null : GpuUiEffectPasses.whiteTarget.view;
    }

    public static GpuBufferSlice writeParameters(float f2, float f3, float f4, float f5, float[][] fArray) {
        Object object;
        int n2;
        int n3;
        RenderSystem.assertOnRenderThread();
        if (GpuUiEffectPasses.blocked()) {
            return null;
        }
        GpuUiEffectPasses.ensureDevice();
        ByteBuffer byteBuffer = PARAMETER_SCRATCH;
        byteBuffer.clear();
        byteBuffer.putFloat(GpuUiEffectPasses.finiteOrZero(f2)).putFloat(GpuUiEffectPasses.finiteOrZero(f3)).putFloat(GpuUiEffectPasses.finiteOrZero(f4)).putFloat(GpuUiEffectPasses.finiteOrZero(f5));
        int n4 = fArray == null ? 0 : Math.min(15, fArray.length);
        for (n3 = 0; n3 < 15; ++n3) {
            float[] fArray2 = n3 < n4 ? fArray[n3] : null;
            for (n2 = 0; n2 < 4; ++n2) {
                float f6 = fArray2 != null && n2 < fArray2.length ? fArray2[n2] : 0.0f;
                byteBuffer.putFloat(GpuUiEffectPasses.finiteOrZero(f6));
            }
        }
        byteBuffer.flip();
        n3 = Math.max(1, RenderSystem.getDevice().getUniformOffsetAlignment());
        int n5 = GpuUiEffectPasses.align(256, n3);
        n2 = uniformSlot * n5;
        int n6 = n2 / 65536;
        int n7 = n2 % 65536;
        while (uniformPages.size() <= n6) {
            object = RenderSystem.getDevice().createBuffer(() -> GpuUiEffectPasses.yw("\ub362\uedc9\ub894\ue8da\u0989\u416e\u890e\u7c4c\u9d26\u2189\u2204\u3098\u1a4e\u6da2\ud2e5\ua91a\ue2f3\uf031\u0e67\u606d\uca8c\ub9ea\u1299\u602b\u4d2e\ub1cb", 1533054401, 0xC4466BAA ^ CryptoVault.mask()) + uniformPages.size(), 136, 65536L);
            uniformPages.add(new UniformPage((GpuBuffer)object));
        }
        ++uniformSlot;
        object = uniformPages.get(n6);
        GpuBufferSlice gpuBufferSlice = object.buffer.slice((long)n7, 256L);
        RenderSystem.getDevice().createCommandEncoder().writeToBuffer(gpuBufferSlice, byteBuffer);
        return gpuBufferSlice;
    }

    public static RenderPipeline materialPipeline(class_2960 class_29602) {
        return GpuUiEffectPasses.materialPipeline(class_29602, true);
    }

    public static RenderPipeline materialPipeline(class_2960 class_29603, boolean bl) {
        if (class_29603 == null) {
            throw new IllegalArgumentException(GpuUiEffectPasses.yw("\ub758\u7f01\u7267\ub456\u8bb1\ub4b3\udaa2\ue97b\uc03b\u30bf\u4d36\ucf65\ub841\ucf59\u6136\u1a39\ub542\u6130\u0f10\u5510\uc889\u457d\u3b0d\u3dfa\ucfbb\uae12\u3b3b\uefe7\u2a40\ua4d3\uc1b3\u6b0c", 820523791, 0xC4466BAA ^ CryptoVault.mask()));
        }
        Map<class_2960, RenderPipeline> map = bl ? materialPipelines : proceduralPipelines;
        return map.computeIfAbsent(class_29603, class_29602 -> {
            String string = class_29602.method_12836() + "/" + class_29602.method_12832();
            String string2 = string.replaceAll(GpuUiEffectPasses.yw("\uef21\u39ad\uabe4\u5d3d\u9ec5\uab90\ue703\uce2e\u8294\u53ec\ueaf5\uaf8f\ueb9e\u8b3e\ub87f\u66d5\u3c38\u3676", 1870698334, 0xC4466BAA ^ CryptoVault.mask()), "_");
            class_2960 class_29603 = class_2960.method_60655((String)GpuUiEffectPasses.yw("\u4f9f\ub56e\u51fb\u2798\u1663\u57f1\u5d50\u58d7\u4b78\u92c2\u1546\u3ad0", -1559166754, 0xC4466BAA ^ CryptoVault.mask()), (String)((bl ? GpuUiEffectPasses.yw("\ub00b\ube3a\u11f9\u7f88\u7248\u6056\ua1d2\u3a64\u622a\u55a3\u1af9\uf95f\ud66d\u7d33\u1087\u996f\u47c8\u36fa\u296a\u3a44\ub92a\u7ccf\u3388\u3444\u6ed5\ude90\udee2\u2bd4", 718044675, 0xC4466BAA ^ CryptoVault.mask()) : GpuUiEffectPasses.yw("\u0d3b\u6fdf\u573f\ue0c5\ua80b\u2d20\ucc8a\ud393\u1645\u9610\ud9a2\u540f\u1454\u3709\ube72\udf0e\uc61a\u0962\u8a2a\uf2f0\u3888\u697b\u1db2\uf23b\u576c\u235a\u9ae8\u0cfc\u31e2\u5da7\u862b\u741e\u3659\u14d6\u0fb5\u2351\ue52a\uaa58\ud780", 1876051118, 0xC4466BAA ^ CryptoVault.mask())) + string2));
            RenderPipeline.Builder builder = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]).withLocation(class_29603).withVertexShader(MATERIAL_VERTEX).withFragmentShader(class_29602).withVertexFormat(FULLSCREEN_FORMAT, VertexFormat.class_5596.field_27379);
            if (bl) {
                builder = builder.withSampler(GpuUiEffectPasses.yw("\ud70b\u5df3\ue031\uf87c\u5dd1\u1d29\u2021\uc3b2\u497b\ucf5d\uc2f5\uf0f7", 1744790964, 0xC4466BAA ^ CryptoVault.mask()));
            }
            RenderPipeline renderPipeline = builder.withUniform(GpuUiEffectPasses.yw("\u93c0\u617a\u57ef\u1b45\u7136\uf399\uf445\ued5d\ue9b3\u5467\uf90c\uaa18\ua3dc\u8fa7", -818337194, 0xC4466BAA ^ CryptoVault.mask()), class_10789.field_60031).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false).withCull(false).withoutBlend().build();
            RenderPipeline renderPipeline2 = SafePipelines.register(renderPipeline);
            if (!bl) {
                pipelinesWithoutInput.add(renderPipeline2);
            }
            return renderPipeline2;
        });
    }

    public static RenderPipeline materialPipelineDual(class_2960 class_29603) {
        if (class_29603 == null) {
            throw new IllegalArgumentException(GpuUiEffectPasses.yw("\u4576\ubeda\u63c6\ua176\u0b5b\u79d5\u958d\u3640\u2a5e\u368e\u928e\u9346\u3a00\u740f\uaefe\u778e\uee3b\u4aa2\u7d22\u607b\u2ea8\u070f\u21cc\ucced\uc7ca\u4078\u71a2\u3c4d\uc00d\u190c\u8d0b", 566458651, 0xC4466BAA ^ CryptoVault.mask()));
        }
        return dualMaterialPipelines.computeIfAbsent(class_29603, class_29602 -> {
            String string = class_29602.method_12836() + "/" + class_29602.method_12832();
            String string2 = string.replaceAll(GpuUiEffectPasses.yw("\u7484\ue8ce\uf67e\uc39c\u8d75\u14a1\uf2bc\u74e5\u3e15\ua09e\ucfb2\u70ca\uefcf\u9907\ubb2c\u3ec2\u649d\ub35f\u4e5c\u588c", 1888523911, 0xC4466BAA ^ CryptoVault.mask()), "_");
            class_2960 class_29603 = class_2960.method_60655((String)GpuUiEffectPasses.yw("\u82f5\uf234\udc9f\ubb98\udc86\u2b63\u377e\ufefc\uedca\ud762\ue38b\u94c8\u81fb\u7020\uc9c0", -2058049471, 0xC4466BAA ^ CryptoVault.mask()), (String)(GpuUiEffectPasses.yw("\u5142\udf58\uac1f\u6ceb\udc20\ud087\u52a5\u47b6\u70e2\u2963\u4f66\uf9b0\u52b0\ua23f\ued00\u41f9\ua7c6\uca6d\u400f\uc3c3\u6c70\u0898\ufa62\uf7e0\ucc75\u0f32\u447f\u133a\u34fc\u9e13\ue061\u06ec\u8f0c", 1425208746, 0xC4466BAA ^ CryptoVault.mask()) + string2));
            RenderPipeline renderPipeline = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]).withLocation(class_29603).withVertexShader(MATERIAL_VERTEX).withFragmentShader(class_29602).withVertexFormat(FULLSCREEN_FORMAT, VertexFormat.class_5596.field_27379).withSampler(GpuUiEffectPasses.yw("\u874a\ua0ed\u8391\u3368\ubb78\u0de8\ubf70\udfe1\ud35c\u1ab2\u1ca3\u4555", -609314681, 0xC4466BAA ^ CryptoVault.mask())).withSampler(GpuUiEffectPasses.yw("\ubd92\u1bfc\uac78\u8dae\u8eb6\u08ef\ua077\ud363\ub1f8", 1420099982, 0xC4466BAA ^ CryptoVault.mask())).withUniform(GpuUiEffectPasses.yw("\uf6a5\uf05a\u809c\u65bb\u7ee6\u627e\u7e33\u399a\u200f\u8fb7\u92ca\u88a0\u8e22\uef13\ud7b8\ud4aa", -1666997579, 0xC4466BAA ^ CryptoVault.mask()), class_10789.field_60031).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false).withCull(false).withoutBlend().build();
            RenderPipeline renderPipeline2 = SafePipelines.register(renderPipeline);
            dualInputPipelines.add(renderPipeline2);
            return renderPipeline2;
        });
    }

    public static Target allocatePersistentTarget(int n2, int n3) {
        if (GpuUiEffectPasses.blocked() || n2 <= 0 || n3 <= 0) {
            return null;
        }
        RenderSystem.assertOnRenderThread();
        if (RenderSystem.tryGetDevice() == null) {
            return null;
        }
        GpuUiEffectPasses.trackCurrentSurfaceSize();
        return GpuUiEffectPasses.allocateTarget(n2, n3, TextureFormat.RGBA8, 13, GpuUiEffectPasses.yw("\u9c51\ue6e0\u04e6\ua92d\uc6d8\u91a2\ub2cd\u8cbf\u49fc\uf0b3\u861a\ue2b5\u2fde\ubdf2\u8ccf\u6784\u4e6f\u8940\u62f8\u5a8b\uf5a8", 1917700079, 0xC4466BAA ^ CryptoVault.mask()) + n2 + "x" + n3, surfaceGeneration);
    }

    public static void releasePersistentTarget(Target target) {
        GpuUiEffectPasses.closeTarget(target);
    }

    public static void clearTarget(Target target, int n2) {
        if (target == null || target.closed() || RenderSystem.tryGetDevice() == null) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture(target.texture, n2);
    }

    public static class_11231 texture(Target target) {
        if (target == null || target.closed()) {
            return class_11231.method_70899();
        }
        return class_11231.method_70900((GpuTextureView)target.view, (class_12137)RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
    }

    public static void queueFullscreen(RenderPipeline renderPipeline, GpuTextureView gpuTextureView, Target target, GpuBufferSlice gpuBufferSlice) {
        if (effectsUnavailable || renderPipeline == null || target == null || target.closed() || gpuBufferSlice == null) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        if (!SafePipelines.usable(renderPipeline)) {
            return;
        }
        if (gpuTextureView == null) {
            Target target2 = GpuUiEffectPasses.requestSceneTexture();
            GpuTextureView gpuTextureView2 = gpuTextureView = target2 == null ? GpuUiEffectPasses.whiteTextureView() : target2.view;
        }
        if (gpuTextureView == null) {
            return;
        }
        if (gpuTextureView.texture() == target.texture) {
            throw new IllegalArgumentException(GpuUiEffectPasses.yw("\u764c\u1fe9\u74da\u8056\uebd0\u9f3a\u72eb\u5c8c\u8687\u008a\u77fe\u47de\u7a8f\uc358\u5ee2\u0199\uc230\u5b46\ue42f\u04ab\ucc77\u0172\u9bb8\ufad4\u455d\u5294\ufaac\u80a7\u2bdb\uef0b\u6156\uf12b\u876c\ub232\uad4a\uafca\u5b7c\u9021\ue95f\uce4f\u94c6\ubbf8\u0fa5\u4302\u458b\u2be5\u9e41\u45c2\u6c30\ufe9f\uab78\u7835\u3db4\u5237", 442368413, 0xC4466BAA ^ CryptoVault.mask()));
        }
        queuedPasses.add(new QueuedPass(renderPipeline, gpuTextureView, null, target, gpuBufferSlice));
    }

    public static void queueFullscreen(RenderPipeline renderPipeline, GpuTextureView gpuTextureView, GpuTextureView gpuTextureView2, Target target, GpuBufferSlice gpuBufferSlice) {
        if (GpuUiEffectPasses.blocked() || renderPipeline == null || target == null || target.closed() || gpuBufferSlice == null) {
            return;
        }
        if (gpuTextureView == null || gpuTextureView2 == null) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        if (!SafePipelines.usable(renderPipeline)) {
            return;
        }
        if (gpuTextureView.texture() == target.texture || gpuTextureView2.texture() == target.texture) {
            throw new IllegalArgumentException(GpuUiEffectPasses.yw("\u3c06\u2577\ue615\uec39\u733d\u2124\ucf8d\u217a\uda33\ubce9\uf484\u4b3d\u3bf0\u729e\u3d49\udb89\u21f5\u3d48\u6247\u4e95\uf483\u1eb2\u28cc\u139b\uda3b\u1f44\ue2db\u72df\u294c\u9ccd\u2f37\u05fb\ub4f6\u7721\uda96\ub40c\u684f\u61bb\u2303\u9e0f\u2de8\uc634\u5a3c\u9d9e\u505a\uf4fb\u9c2c\uae73\u86dc\uea86\udfb0\u1406\uf717\u768d\ue8fe\ue195", -1855963294, 0xC4466BAA ^ CryptoVault.mask()));
        }
        queuedPasses.add(new QueuedPass(renderPipeline, gpuTextureView, gpuTextureView2, target, gpuBufferSlice));
    }

    public static void captureAndRunQueuedPasses() {
        RenderSystem.assertOnRenderThread();
        if (effectsUnavailable) {
            queuedPasses.clear();
            sceneCaptureRequested = false;
            return;
        }
        if (!sceneCaptureRequested && queuedPasses.isEmpty()) {
            return;
        }
        try {
            if (sceneCaptureRequested) {
                GpuUiEffectPasses.copySceneToSnapshot();
            }
            if (!queuedPasses.isEmpty()) {
                GpuUiEffectPasses.ensureFullscreenBuffer();
            }
        }
        catch (LinkageError | RuntimeException throwable) {
            GpuUiEffectPasses.warnPassFailure(throwable);
            effectsUnavailable = true;
            queuedPasses.clear();
            sceneCaptureRequested = false;
            return;
        }
        for (QueuedPass queuedPass : queuedPasses) {
            try {
                if (queuedPass.output.closed() || queuedPass.input.isClosed() || queuedPass.input2 != null && queuedPass.input2.isClosed()) continue;
                GpuUiEffectPasses.runPass(queuedPass);
            }
            catch (LinkageError | RuntimeException throwable) {
                GpuUiEffectPasses.warnPassFailure(throwable);
            }
        }
        queuedPasses.clear();
        sceneCaptureRequested = false;
    }

    private static void warnPassFailure(Throwable throwable) {
        if (warnedPassFailure) {
            return;
        }
        warnedPassFailure = true;
        LOGGER.warn(GpuUiEffectPasses.yw("\ue3dc\u186e\u6291\u1396\uf9bb\u75a0\u04e9\u691c\uef02\uae77\u0c2b\uf79e\ub532\ube84\u8785\u45a2\u4de0\u045f\uc2cf\uf009\u0a9c\u230e\u4b8b\ue421\u3286\u8077\u0aa4\ua008\ua973\u0bcc\u31ed\u5f50\u341c\ua0b7\ue048\ud272\ue27a\u94fa\ua275\uc59e\uc411\u103f\u7309\u6445\u6197\u816c\u083e\ud4dd\u383b\ud68b\u9025\u6c0b\u9041\ud385\u7d38\u75da\u2541\u8ba8\u762c\u2c2e\uc867\u0828\ue57f\ufa5b\uff0a\ua6cf\u2fd1\ud339\u1a28\u2824\ud093\ubb9b\u4948\ubbeb\ud93a\u7b38\u8caa\u94d5\uc872\u2ce0\u93b3\ua7e3\u00b7\u1623\ud7a6\u7a79\u135c\uafcf\u1e93\uc25f", -760873812, 0xC4466BAA ^ CryptoVault.mask()), throwable);
    }

    static boolean isAvailable() {
        return !effectsUnavailable;
    }

    private static boolean blocked() {
        return effectsUnavailable;
    }

    static void disableAfterFailure(Throwable throwable) {
        RenderSystem.assertOnRenderThread();
        GpuUiEffectPasses.warnPassFailure(throwable);
        effectsUnavailable = true;
        queuedPasses.clear();
        sceneCaptureRequested = false;
    }

    public static void finishFrame() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.queueFencedTask(GpuUiEffectPasses::finishFrame);
            return;
        }
        for (Target target : pooledTargets) {
            target.leased = false;
        }
        GpuUiEffectPasses.pruneStaleAndExcessTargets();
        uniformSlot = 0;
    }

    public static void release() {
        RenderSystem.assertOnRenderThread();
        queuedPasses.clear();
        for (Target object : pooledTargets) {
            GpuUiEffectPasses.closeTarget(object);
        }
        pooledTargets.clear();
        GpuUiEffectPasses.closeTarget(sceneTarget);
        sceneTarget = null;
        GpuUiEffectPasses.closeTarget(whiteTarget);
        whiteTarget = null;
        for (UniformPage uniformPage : uniformPages) {
            try {
                uniformPage.buffer.close();
            }
            catch (LinkageError | RuntimeException throwable) {}
        }
        uniformPages.clear();
        uniformSlot = 0;
        lastSurfaceWidth = -1;
        lastSurfaceHeight = -1;
        ++surfaceGeneration;
        failedTargetAllocations.clear();
        if (fullscreenVertices != null) {
            try {
                fullscreenVertices.close();
            }
            catch (LinkageError | RuntimeException throwable) {
                // empty catch block
            }
        }
        fullscreenVertices = null;
        sceneCaptureRequested = false;
        warnedPassFailure = false;
        effectsUnavailable = false;
    }

    private static void copySceneToSnapshot() {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || McCompat.mainRenderTarget(class_3102) == null || sceneTarget == null) {
            return;
        }
        GpuTexture gpuTexture = McCompat.mainRenderTarget(class_3102).method_30277();
        if (gpuTexture == null || gpuTexture.getWidth(0) != GpuUiEffectPasses.sceneTarget.width || gpuTexture.getHeight(0) != GpuUiEffectPasses.sceneTarget.height || gpuTexture.getFormat() != GpuUiEffectPasses.sceneTarget.format) {
            return;
        }
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(gpuTexture, GpuUiEffectPasses.sceneTarget.texture, 0, 0, 0, 0, 0, GpuUiEffectPasses.sceneTarget.width, GpuUiEffectPasses.sceneTarget.height);
    }

    private static void runPass(QueuedPass queuedPass) {
        GpuTextureView gpuTextureView = queuedPass.input;
        GpuTextureView gpuTextureView2 = queuedPass.output.view;
        if (!SafePipelines.usable(queuedPass.pipeline)) {
            return;
        }
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> GpuUiEffectPasses.yw("\u1d1a\u47b4\ud101\u5319\ua3c7\udd99\u1829\u6bf1\u2e72\u6635\u164b\uc665\ueeb8\u4870\uee86\u3d70\u971d", 527314663, 0xC4466BAA ^ CryptoVault.mask()) + queuedPass.pipeline.getLocation(), gpuTextureView2, OptionalInt.empty());){
            renderPass.setPipeline(queuedPass.pipeline);
            if (!pipelinesWithoutInput.contains(queuedPass.pipeline)) {
                renderPass.bindTexture(GpuUiEffectPasses.yw("\u0550\u65c9\uf9a8\ufdf3\u2bc2\u9061\u0791\u98d9\uf0df\u986d\u380d\ube91", -1572356976, 0xC4466BAA ^ CryptoVault.mask()), gpuTextureView, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
            }
            if (queuedPass.input2 != null) {
                renderPass.bindTexture(GpuUiEffectPasses.yw("\u9735\u32df\u7d15\u23e8\u7157\u801f\u8f68\u04f4\u917b\uf85b", -760230881, 0xC4466BAA ^ CryptoVault.mask()), queuedPass.input2, RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
            }
            if (queuedPass.uniforms != null) {
                renderPass.setUniform(GpuUiEffectPasses.yw("\ud699\uff3d\u55f3\u2c34\u4bf8\ub319\ub6b5\u0efe\ua856\ud4ec\u0d23\u25a0\u56a9", -1024972664, 0xC4466BAA ^ CryptoVault.mask()), queuedPass.uniforms);
            }
            renderPass.setVertexBuffer(0, fullscreenVertices);
            renderPass.draw(0, 6);
        }
    }

    private static void ensureFullscreenBuffer() {
        if (fullscreenVertices != null && !fullscreenVertices.isClosed()) {
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(120).order(ByteOrder.nativeOrder());
        GpuUiEffectPasses.putVertex(byteBuffer, -1.0f, -1.0f, 0.0f, 1.0f);
        GpuUiEffectPasses.putVertex(byteBuffer, 1.0f, -1.0f, 1.0f, 1.0f);
        GpuUiEffectPasses.putVertex(byteBuffer, 1.0f, 1.0f, 1.0f, 0.0f);
        GpuUiEffectPasses.putVertex(byteBuffer, -1.0f, -1.0f, 0.0f, 1.0f);
        GpuUiEffectPasses.putVertex(byteBuffer, 1.0f, 1.0f, 1.0f, 0.0f);
        GpuUiEffectPasses.putVertex(byteBuffer, -1.0f, 1.0f, 0.0f, 0.0f);
        byteBuffer.flip();
        fullscreenVertices = RenderSystem.getDevice().createBuffer(() -> GpuUiEffectPasses.yw("\u7519\ud114\ubc3d\uaeea\uf3ee\uea70\ua21d\u9f72\u6749\u1244\uba9e\u600b\uefad\ubbf7\ub8ae\uc9a8\u3783\u84b2\ufd67\u3654\u3415\ub896\u4df5\u540d\u60ce\uf66b", -1939277719, 0xC4466BAA ^ CryptoVault.mask()), 32, byteBuffer);
    }

    private static void putVertex(ByteBuffer byteBuffer, float f2, float f3, float f4, float f5) {
        byteBuffer.putFloat(f2).putFloat(f3).putFloat(0.0f).putFloat(f4).putFloat(f5);
    }

    private static Target allocateTarget(int n2, int n3, TextureFormat textureFormat, int n4, String string, long l2) {
        FailedTarget failedTarget = new FailedTarget(n2, n3, textureFormat, n4, l2);
        if (failedTargetAllocations.contains(failedTarget)) {
            return null;
        }
        if (n2 > RenderSystem.getDevice().getMaxTextureSize() || n3 > RenderSystem.getDevice().getMaxTextureSize()) {
            failedTargetAllocations.add(failedTarget);
            LOGGER.warn(GpuUiEffectPasses.yw("\ua471\u2c15\u619e\u5ead\uf94c\u829c\u3a41\u3489\ufea2\u7137\u14bb\ucea6\u7e0c\ud8ed\u8298\u145a\ua7e7\ub5cb\u9990\u87b6\ue5af\u9342\u9eeb\u258d\u3841\ua578\u7e1d\u8db6\ud48b\u4c09\uc8fc\ufd17\uadbc\u85a1\u9b6d\u30c4\u1cff\u8264\u5a82\u4129\ud013\u28c0", 956077763, 0xC4466BAA ^ CryptoVault.mask()), (Object)n2, (Object)n3);
            return null;
        }
        GpuTexture gpuTexture = null;
        GpuTextureView gpuTextureView = null;
        try {
            gpuTexture = RenderSystem.getDevice().createTexture(string, n4, textureFormat, n2, n3, 1, 1);
            gpuTextureView = RenderSystem.getDevice().createTextureView(gpuTexture);
            return new Target(gpuTexture, gpuTextureView, n2, n3, textureFormat, l2);
        }
        catch (LinkageError | RuntimeException throwable) {
            if (gpuTextureView != null) {
                try {
                    gpuTextureView.close();
                }
                catch (LinkageError | RuntimeException throwable2) {
                    // empty catch block
                }
            }
            if (gpuTexture != null) {
                try {
                    gpuTexture.close();
                }
                catch (LinkageError | RuntimeException throwable3) {
                    // empty catch block
                }
            }
            failedTargetAllocations.add(failedTarget);
            LOGGER.warn(GpuUiEffectPasses.yw("\u6e1b\ua8ed\u5caa\ucad5\u2c9c\uf0a3\u5f18\ubea0\uc323\u387b\u8e3a\ud44c\u4ab3\u8b26\uc1f3\u7a51\ub1d9\u0457\uf002\ubb40\u77cd\ub8bc\u41da\u39ac\u0ef2\u6376\u75ef\ua150\u6055\u64e2\ub257\u06c2\u5b47\uaf0d\u86fa\u4f85\u2c27\u8c35\u54d5\uca0a\u47d7\u51f2", -2002280164, 0xC4466BAA ^ CryptoVault.mask()), new Object[]{n2, n3, throwable});
            return null;
        }
    }

    private static void closeTarget(Target target) {
        if (target == null || target.closed()) {
            return;
        }
        try {
            target.view.close();
        }
        catch (LinkageError | RuntimeException throwable) {
            // empty catch block
        }
        try {
            target.texture.close();
        }
        catch (LinkageError | RuntimeException throwable) {
            // empty catch block
        }
    }

    private static void ensureDevice() {
        if (RenderSystem.tryGetDevice() == null) {
            throw new IllegalStateException(GpuUiEffectPasses.yw("\u89b1\u6f25\uade1\u4b6d\uf3a1\u824c\u6600\u078d\u365f\ufbb5\u3462\ufb9f\uc90d\uab6a\u108d\u2a32\ub949\u9900\ua5f5\u3393\u86c9\u1142\u4792\uc6f7\uf60c\ud7ca\uf984\ueb2d\u9a54\uc846\u519e\uc69a\ua6ed\u543f\udb99\ue641\u9486\ud145\u8f31\u27ba\u0e3f\uf825\u38c1\ub74b\u6871", -1753637918, 0xC4466BAA ^ CryptoVault.mask()));
        }
    }

    private static void trackCurrentSurfaceSize() {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || class_3102.method_22683() == null) {
            return;
        }
        GpuUiEffectPasses.trackSurfaceSize(class_3102.method_22683().method_4489(), class_3102.method_22683().method_4506());
    }

    private static void trackSurfaceSize(int n2, int n3) {
        if (n2 <= 0 || n3 <= 0 || n2 == lastSurfaceWidth && n3 == lastSurfaceHeight) {
            return;
        }
        lastSurfaceWidth = n2;
        lastSurfaceHeight = n3;
        ++surfaceGeneration;
        failedTargetAllocations.clear();
        pooledTargets.removeIf(target -> {
            if (target.leased) {
                return false;
            }
            GpuUiEffectPasses.closeTarget(target);
            return true;
        });
    }

    private static void pruneStaleAndExcessTargets() {
        pooledTargets.removeIf(target -> {
            if (target.leased) {
                return false;
            }
            if (target.generation != surfaceGeneration) {
                GpuUiEffectPasses.closeTarget(target);
                return true;
            }
            return false;
        });
        while (pooledTargets.size() > MAX_POOLED_TARGETS) {
            int n2 = -1;
            for (int i2 = 0; i2 < pooledTargets.size(); ++i2) {
                if (GpuUiEffectPasses.pooledTargets.get((int)i2).leased) continue;
                n2 = i2;
                break;
            }
            if (n2 < 0) break;
            GpuUiEffectPasses.closeTarget(pooledTargets.remove(n2));
        }
    }

    private static void releaseSafely() {
        if (RenderSystem.tryGetDevice() == null) {
            return;
        }
        if (RenderSystem.isOnRenderThread()) {
            GpuUiEffectPasses.release();
        } else {
            RenderSystem.queueFencedTask(GpuUiEffectPasses::release);
        }
    }

    private static float finiteOrZero(float f2) {
        return Float.isFinite(f2) ? f2 : 0.0f;
    }

    private static int align(int n2, int n3) {
        int n4 = n2 % n3;
        return n4 == 0 ? n2 : n2 + n3 - n4;
    }

    static {
        lastSurfaceWidth = -1;
        lastSurfaceHeight = -1;
        ClientLifecycleEvents.CLIENT_STOPPING.register(class_3102 -> GpuUiEffectPasses.releaseSafely());
        ResourceLoader.get((class_3264)class_3264.field_14188).registerReloader(class_2960.method_60655((String)GpuUiEffectPasses.yw("\u6a0d\ufd36\uda32\u90f5\u2434\u35c6\u37d6\u4519\uf2b7\uc935\u3492\uc821\u4812", -815099163, 0xC4466BAA ^ CryptoVault.mask()), (String)GpuUiEffectPasses.yw("\u8a13\uc9a1\udae2\ucf1b\ud80f\u905f\udf2e\u72bb\u72f6\u86db\uc57d\ufb73\u6150\u079f\u81e9\ub025\ubf2a\ub204\ud883\ud3af", -2059285056, 0xC4466BAA ^ CryptoVault.mask())), (class_3302)((class_4013)class_33002 -> GpuUiEffectPasses.releaseSafely()));
    }

    private static /* synthetic */ String yw(String string, int n2, int n3) {
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
    public static final class Target {
        private final GpuTexture texture;
        private final GpuTextureView view;
        private final int width;
        private final int height;
        private final TextureFormat format;
        private final long generation;
        private boolean leased;

        private Target(GpuTexture gpuTexture, GpuTextureView gpuTextureView, int n2, int n3, TextureFormat textureFormat, long l2) {
            this.texture = gpuTexture;
            this.view = gpuTextureView;
            this.width = n2;
            this.height = n3;
            this.format = textureFormat;
            this.generation = l2;
        }

        public GpuTexture texture() {
            return this.texture;
        }

        public GpuTextureView view() {
            return this.view;
        }

        public int width() {
            return this.width;
        }

        public int height() {
            return this.height;
        }

        public boolean closed() {
            return this.texture.isClosed() || this.view.isClosed();
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record UniformPage(GpuBuffer buffer) {
    }

    @Environment(value=EnvType.CLIENT)
    private record QueuedPass(RenderPipeline pipeline, GpuTextureView input, GpuTextureView input2, Target output, GpuBufferSlice uniforms) {
    }

    @Environment(value=EnvType.CLIENT)
    private record FailedTarget(int width, int height, TextureFormat format, int usage, long generation) {
    }
}

