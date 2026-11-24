/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.FilterMode
 *  com.mojang.blaze3d.textures.GpuTextureView
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
 *  net.minecraft.class_3264
 *  net.minecraft.class_3302
 *  net.minecraft.class_4013
 *  org.joml.Matrix3x2f
 *  org.joml.Matrix3x2fc
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.class_10789;
import net.minecraft.class_11231;
import net.minecraft.class_12137;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3302;
import net.minecraft.class_4013;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

@Environment(value=EnvType.CLIENT)
public final class GpuUiClipMask {
    private static final class_2960 VERTEX = class_2960.method_60655((String)GpuUiClipMask.pj("\u137b\ubba3\ubb56\u68c7\ud1d3\u4fee\u74b0\ub74d\u3824\u184d\uf85f\u424e", -399915083, 0x5DC7CF21 ^ CryptoVault.mask()), (String)GpuUiClipMask.pj("\u9f37\u426c\ud7ae\uf3f3\uaf11\u74ac\u9438\u5b10\uc2e7\u3377\uc376\ue61b\uc4a6\u4382\u33ed\u9d41\udc14\uedc7\u88f3\uf9a3\u226f", -202802982, 0x5DC7CF21 ^ CryptoVault.mask()));
    private static final class_2960 FRAGMENT = class_2960.method_60655((String)GpuUiClipMask.pj("\u7df5\u96d6\ua898\u9e09\u365c\u8c81\u3cf8\u7e57\u10d8\u7513\u628d\u5c47\ueea6\ufdb0\ua431", 575299655, 0x5DC7CF21 ^ CryptoVault.mask()), (String)GpuUiClipMask.pj("\u312f\u15f8\u9e9d\u8e87\ua59a\ue72d\udcd9\u7374\u028b\u83ef\u3395\u38c7\u6855\u1837\u8340\uca55\u4ac3\u5707\u9314\u4e57\u3ef4\u57af\u47e8", 1098129134, 0x5DC7CF21 ^ CryptoVault.mask()));
    private static final VertexFormat FORMAT = VertexFormat.builder().add(GpuUiClipMask.pj("\u609d\u674c\uc317\u3798\ue11c\u9448\ub297\u72a5\u97cb", 838699558, 0x5DC7CF21 ^ CryptoVault.mask()), VertexFormatElement.POSITION).add(GpuUiClipMask.pj("\ua6a0\u547c\u9866\u09e0", -1154915718, 0x5DC7CF21 ^ CryptoVault.mask()), VertexFormatElement.UV0).build();
    private static final RenderPipeline PIPELINE = SafePipelines.register(RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]).withLocation(class_2960.method_60655((String)GpuUiClipMask.pj("\ue7bb\u9a73\uc634\u0fde\u0854\u0f12\uf00d\u25d6\ue3c6\ub7bf\uf8af\uf997\u863b\u3f36", -2036064383, 0x5DC7CF21 ^ CryptoVault.mask()), (String)GpuUiClipMask.pj("\u5a8c\u38a0\u8f28\u3913\uf84f\uda39\u5763\u096e\u76ec\ub28f\u336a\u866a\ucd70\ub138\uc8c9\u6fa4\u21e8\uf8fc\u6b50\uad39\u6df7\u3cfc\ubab7\ue543\u6cbe\u03a5\u9515", 1247451563, 0x5DC7CF21 ^ CryptoVault.mask()))).withVertexShader(VERTEX).withFragmentShader(FRAGMENT).withVertexFormat(FORMAT, VertexFormat.class_5596.field_27379).withSampler(GpuUiClipMask.pj("\u1c53\u688a\ubc1b\u73f8\u67ed\ue872\u49e1\u18a0\u4f36", 1132756407, 0x5DC7CF21 ^ CryptoVault.mask())).withUniform(GpuUiClipMask.pj("\uc805\ue11a\u3f7a\ueafb\u3671\uefac\ub12a\u79c6\ucf80\u43a7\u9dec\u0fce\uecaa\ua99d", 643251505, 0x5DC7CF21 ^ CryptoVault.mask()), class_10789.field_60031).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withoutBlend().build());
    private static final GpuUiClipMask NONE = new GpuUiClipMask(null);
    private final GpuUiEffectPasses.Target target;
    private static final Map<CacheKey, CacheEntry> CROSS_FRAME_CACHE = new HashMap<CacheKey, CacheEntry>();
    private static final ArrayDeque<GpuUiEffectPasses.Target> SPARE_TARGETS = new ArrayDeque();
    private static final int MAX_SPARE_TARGETS = 8;

    private GpuUiClipMask(GpuUiEffectPasses.Target target) {
        this.target = target;
    }

    public static void registerPipelines() {
        if (PIPELINE == null) {
            throw new IllegalStateException(GpuUiClipMask.pj("\ua166\uf40f\ue8dc\udcdd\uc4b9\ue604\u2c17\u9627\ue499\ub402\uc4bb\u38be\u34ad\u8da4\ufd4b\u93e0\u8617\uab21\u99d6\ud0f3\u44e8\uacd1\ude61\u20d9\ud021\ub1bf\u160d\u7f77\uc358\u2fb8\u152b\ub55b\u3d61\uf4de\u7387\ue45b\u3649\u2bc2\u59d2\uc5cf", -1534500150, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
    }

    public static boolean available() {
        return SafePipelines.usable(PIPELINE);
    }

    public static GpuUiClipMask none() {
        return NONE;
    }

    public GpuUiClipMask push(Matrix3x2fc matrix3x2fc, UiDrawCommand.ClipPush clipPush, int n2, int n3, int n4, int n5) {
        Object object;
        if (matrix3x2fc == null || clipPush == null) {
            throw new NullPointerException(GpuUiClipMask.pj("\u2799\u4490\u5974\u341c\u90b6\u4de4\ud620\u0fc6\u1493\ufa89\u3501\ue95b\ud311\udf24\uec9a\ud200\u2516\uf350\uce23\u9aed\ub134\ubb71\uc3a0\u506d\u70ce\u2b67\ufc64\ue7be\u81cf", -658143717, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        if (!GpuUiClipMask.available()) {
            throw new IllegalStateException(GpuUiClipMask.pj("\ua66c\ucf0c\u55ca\ud199\u49eb\u01fb\u690d\uffca\u0235\u0806\ucd17\u192c\ua3e1\u33b5\u0dc1\u3e47\u854e\ucab7\u1f88\uf0bd\u0af1\uf705\u6270\u8245\u8a9a\u2b2c\u22b4\u8484\u6e5f\u86f6\u947b\ud7df\u6c6a\u2697\u2482\u3b5f\u78c4\ua544\ud714\ua296\u3d4d\u60c7\u93f5\u17f7\u8853\u929d", 1110557216, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        if (n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0) {
            throw new IllegalArgumentException(GpuUiClipMask.pj("\ub9f3\u04a8\u71b5\ub8ae\ue713\u53d1\u3039\udf91\ubc80\u7b8c\ubc26\ub4a7\u9358\uf565\u155a\u584f\u50e0\ue33b\uabe2\ud7f5\ub7fe\ude46\uba6e\u722b\uc4d0\u59e2\u3be6\u3962\u87bb\ua220\u311e\uc850\ua6ef\u4caa\u7e15\uc699\u8e71\ufb97\uf2fb\ufa76\u3302\u41a6\ub9e2\ubbf7\u3b0b\u76e9\u8daf\uddc7\u3317\uaf80\uec11", -2064407081, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        if (!(Float.isFinite(clipPush.x()) && Float.isFinite(clipPush.y()) && Float.isFinite(clipPush.width()) && Float.isFinite(clipPush.height()) && Float.isFinite(clipPush.radius()) && !(clipPush.width() < 0.0f) && !(clipPush.height() < 0.0f))) {
            throw new IllegalArgumentException(GpuUiClipMask.pj("\u2f15\u7b79\ue0c1\ua4cb\u3dbd\u0846\u7d2d\uabea\ub007\u9e79\u913e\uc094\uc5ba\u9cb2\u8b19\u3aaa\u0cd2\u1cd1\uf789\ud81b\u478f\ub23e\ue64e\u39bb\ubc6a\u8f0b\u2811\ua5cf\uc5db\u1037\u9b19\u411e\ud431", 1434637797, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        Matrix3x2f matrix3x2f = new Matrix3x2f(matrix3x2fc);
        CacheKey cacheKey = new CacheKey(this.target, clipPush, matrix3x2f.m00(), matrix3x2f.m01(), matrix3x2f.m10(), matrix3x2f.m11(), matrix3x2f.m20(), matrix3x2f.m21(), n2, n3, n4, n5);
        CacheEntry cacheEntry = CROSS_FRAME_CACHE.get(cacheKey);
        if (cacheEntry != null && !cacheEntry.target.closed()) {
            cacheEntry.usedThisFrame = true;
            return new GpuUiClipMask(cacheEntry.target);
        }
        float f2 = matrix3x2f.determinant();
        if (!Float.isFinite(f2) || Math.abs(f2) < 1.0E-8f) {
            throw new IllegalArgumentException(GpuUiClipMask.pj("\u7ab5\u709f\u44af\u92ed\u6483\ue141\u05f5\u6d64\u6dbc\ue159\uc0bc\u8e26\uc874\ucb35\u237a\u8b3c\ua8a0\uddb6\u46b2\ud6c8\uc2cf\u96f3\u612e\uc9fc\u1b46\uef42\ub6d2\u1914\u7e86\ua9a5\u2f1b\ub20a\u85c1\u17e1\uf84c\u1126\u6612\ua3b3\u055a\uce19\uc2c3\u174c\u9dc9\u079f\ua529\u1879\uf0ee\u7212\ue2bd\udf9b\u11b0\ue6c3\uff0b\ufb13\ue66a\u155f\u6add\u54f7\ua7b0\uc452\u2ea8", -2050873837, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        matrix3x2f.invert();
        float f3 = Math.max(0.0f, Math.min(clipPush.radius(), Math.min(clipPush.width(), clipPush.height()) * 0.5f));
        float[][] fArray = new float[4][4];
        fArray[0] = new float[]{clipPush.x(), clipPush.y(), clipPush.width(), clipPush.height()};
        fArray[1] = new float[]{f3, matrix3x2f.m00(), matrix3x2f.m01(), matrix3x2f.m10()};
        fArray[2] = new float[]{matrix3x2f.m11(), matrix3x2f.m20(), matrix3x2f.m21(), n4};
        fArray[3] = new float[]{n5, 0.0f, 0.0f, 0.0f};
        GpuUiEffectPasses.Target target = GpuUiClipMask.acquireTarget(n2, n3);
        if (target == null || target.view() == null) {
            if (target != null) {
                GpuUiEffectPasses.releasePersistentTarget(target);
            }
            throw new IllegalStateException(GpuUiClipMask.pj("\u0aa2\uc87e\u8d6f\u260c\ud119\ud6c0\u48ca\u207c\u78d6\uae44\u23c6\u7908\u5d90\u0d7c\u26bd\u9ff4\u4bfe\u85e9\uea96\uf5fa\u1439\u0e89\u34d9\uc897\ua7d0\u2d2b\uabde\uee97\u7d1c\u10d3\ued56\u3e61\u7bf6\ua805\ufad1\u151f\u525b\u136a\u78c5\uc351\u90f6\u4370\u80f8\ud4c2\u638a\u5357\u1c17", -993964124, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        try {
            object = GpuUiEffectPasses.writeParameters(n2, n3, 0.0f, 0.0f, fArray);
            if (object == null) {
                throw new IllegalStateException(GpuUiClipMask.pj("\u6c78\u134c\u33d7\u326b\u0d88\ub329\u8e06\u887e\ua30a\ufa83\u9dc7\u2c52\ud222\u6910\ud609\u764f\uaee8\ud5e4\ud1fb\u8a84\u5b52\u147a\u17d8\ue490\u031c\u4ea0\ude15\u0c82\u984d\uc848\uaacd\u9d3c\uf762\u3e53\u640f\u48a1\u3f78\u2357\ufea7\udc50\u6dc4\ueb79\u3014\uaee9", -1257389893, 0x5DC7CF21 ^ CryptoVault.mask()));
            }
            GpuTextureView gpuTextureView = this.view();
            if (gpuTextureView == null) {
                throw new IllegalStateException(GpuUiClipMask.pj("\u41dd\ucec4\ub236\u6f36\uf8ae\u8685\ub0d8\u5922\ufbca\ud554\u22be\ub0a4\uc233\u05b7\ucea2\uded2\u2bf6\u17fb\uff37\u2ad2\uc330\u5c16\ua9dd\u022d\u9561\u8e3f\u7a23\ua9fe\u2b34\ucf65\u133f\u03c4\u7b0d\ue0cf\u938d\u0b88\u0c07\u3bb2\ua04a\u7c3e\u6cb4\u95b3\uc79f\u6ed5\u963c\u7e59\udc74", 1095080973, 0x5DC7CF21 ^ CryptoVault.mask()));
            }
            GpuUiEffectPasses.queueFullscreen(PIPELINE, gpuTextureView, target, object);
        }
        catch (Error | RuntimeException throwable) {
            GpuUiClipMask.recycle(target);
            throw throwable;
        }
        if (cacheEntry != null) {
            GpuUiClipMask.recycle(cacheEntry.target);
        }
        object = new CacheEntry(target);
        object.usedThisFrame = true;
        CROSS_FRAME_CACHE.put(cacheKey, (CacheEntry)object);
        return new GpuUiClipMask(target);
    }

    public static void finishFrame() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.queueFencedTask(GpuUiClipMask::finishFrame);
            return;
        }
        Iterator<Map.Entry<CacheKey, CacheEntry>> iterator = CROSS_FRAME_CACHE.entrySet().iterator();
        while (iterator.hasNext()) {
            CacheEntry cacheEntry = iterator.next().getValue();
            if (!cacheEntry.usedThisFrame || cacheEntry.target.closed()) {
                GpuUiClipMask.recycle(cacheEntry.target);
                iterator.remove();
                continue;
            }
            cacheEntry.usedThisFrame = false;
        }
    }

    private static GpuUiEffectPasses.Target acquireTarget(int n2, int n3) {
        GpuUiEffectPasses.Target target;
        while ((target = SPARE_TARGETS.poll()) != null) {
            if (!target.closed() && target.width() == n2 && target.height() == n3) {
                return target;
            }
            GpuUiEffectPasses.releasePersistentTarget(target);
        }
        return GpuUiEffectPasses.allocatePersistentTarget(n2, n3);
    }

    private static void recycle(GpuUiEffectPasses.Target target) {
        if (target == null || target.closed()) {
            return;
        }
        if (SPARE_TARGETS.size() >= 8) {
            GpuUiEffectPasses.releasePersistentTarget(target);
        } else {
            SPARE_TARGETS.addLast(target);
        }
    }

    private static void releaseCache() {
        Object object = CROSS_FRAME_CACHE.values().iterator();
        while (object.hasNext()) {
            CacheEntry cacheEntry = object.next();
            try {
                GpuUiEffectPasses.releasePersistentTarget(cacheEntry.target);
            }
            catch (LinkageError | RuntimeException throwable) {}
        }
        CROSS_FRAME_CACHE.clear();
        while ((object = SPARE_TARGETS.poll()) != null) {
            try {
                GpuUiEffectPasses.releasePersistentTarget((GpuUiEffectPasses.Target)object);
            }
            catch (LinkageError | RuntimeException throwable) {}
        }
    }

    public static void releaseCacheOnClose() {
        GpuUiClipMask.releaseCacheSafely();
    }

    private static void releaseCacheSafely() {
        try {
            if (RenderSystem.tryGetDevice() == null) {
                CROSS_FRAME_CACHE.clear();
                SPARE_TARGETS.clear();
                return;
            }
            if (RenderSystem.isOnRenderThread()) {
                GpuUiClipMask.releaseCache();
            } else {
                RenderSystem.queueFencedTask(GpuUiClipMask::releaseCache);
            }
        }
        catch (Throwable throwable) {
            CROSS_FRAME_CACHE.clear();
            SPARE_TARGETS.clear();
        }
    }

    public class_11231 withMask(class_11231 class_112312) {
        class_11231 class_112313;
        class_11231 class_112314 = class_112313 = class_112312 == null ? class_11231.method_70899() : class_112312;
        if (class_112313.comp_4053() != null || class_112313.comp_4054() != null) {
            throw new IllegalArgumentException(GpuUiClipMask.pj("\ue8d9\u7ad0\u0cba\u5221\u2fbb\u88ff\u57aa\ucb3c\u312b\ud3ae\ua689\u8e13\uf3e9\u1794\ud3d5\u2a48\u94e9\uef24\u4103\ud331\u5967\uaa93\ue399\ub9d6\u18df\udf10\uccd2\u2a0c\ucc6e\u923a\u6832\u3eee\u164b\u8373\ud037\ua818\u2dfa\u1816\ud365\ub91a\u3ff9\uc7d2\u03a7\u627d\ua1cb", -1829053571, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        class_12137 class_121372 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
        if (class_121372 == null) {
            throw new IllegalStateException(GpuUiClipMask.pj("\u4cd5\u4037\ue0e5\u1cf9\uf1f9\ua648\ua265\u739a\u14c2\u8506\ue4fc\u9cd9\u0695\u701f\u2f41\u5281\u6fd1\u2db4\uc5c5\ube18\u58e7\ud2e1\u498f\u3452\uc906\u5c1a\u8d07\u2261\uc4e8\u5fa2\u3de5\u3425\u476b", -1868998609, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        GpuTextureView gpuTextureView = this.view();
        if (gpuTextureView == null) {
            throw new IllegalStateException(GpuUiClipMask.pj("\uf8f7\uba32\uae33\u5fa7\u7f5f\ucac2\u62f4\u5959\u6894\u3c3e\uf44d\uc062\uaf3e\u39a5\u959b\u59d4\u43e0\u3fd9\ua863\u60eb\u6478\ua45e\uefa5\u9976\uc9b6\udc20\u471f\u9ae5\u9884\u0233\u7143\uf0eb\ua12f\ue63d\u0dcf\ufacf\u5c84\uf6d3\u407d\u234e\ue595\uc1f1", 1476394984, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        if (class_112313.comp_4052() == null) {
            GpuTextureView gpuTextureView2 = GpuUiEffectPasses.whiteTextureView();
            if (gpuTextureView2 == null) {
                throw new IllegalStateException(GpuUiClipMask.pj("\u1f90\u31d3\u684d\uca2e\u7c86\u8e7d\u211d\ud68d\u6171\u13a3\u0163\u270a\u28c4\ufd58\u446c\ua113\ua1b8\ud1b2\u9b9a\uab04\u0610\ubd09\uce14\u48b6\u07e0\u3d92\uc7b3\uac22\uac26\uf20c\u6b6c\u58dd\ua0db\u210f\u2aaa\u1017\u6e89", 1659200499, 0x5DC7CF21 ^ CryptoVault.mask()));
            }
            return class_11231.method_70901((GpuTextureView)gpuTextureView2, (class_12137)class_121372, (GpuTextureView)gpuTextureView, (class_12137)class_121372);
        }
        if (class_112313.comp_5053() == null) {
            throw new IllegalArgumentException(GpuUiClipMask.pj("\u6309\u6e5c\u96ed\u25b6\u1272\u0201\u85a7\u83df\uda4e\u2661\u63f8\ue482\u7731\u7add\u6985\ucad8\u43e1\u89d0\ubaf9\u18f5\ua4b6\u7e3a\u0ff4\u0ee5\ud159\u5bec\u18b3\u80cd\uc963\u9adf\uf260\ub84a\u9e2c\u2c13", -2030383411, 0x5DC7CF21 ^ CryptoVault.mask()));
        }
        return class_11231.method_70901((GpuTextureView)class_112313.comp_4052(), (class_12137)class_112313.comp_5053(), (GpuTextureView)gpuTextureView, (class_12137)class_121372);
    }

    private GpuTextureView view() {
        return this.target == null ? GpuUiEffectPasses.whiteTextureView() : this.target.view();
    }

    static {
        ClientLifecycleEvents.CLIENT_STOPPING.register(class_3102 -> GpuUiClipMask.releaseCacheSafely());
        ResourceLoader.get((class_3264)class_3264.field_14188).registerReloader(class_2960.method_60655((String)GpuUiClipMask.pj("\u5973\u4372\u6c61\u8be0\u53f2\u3f67\u5a53\ue200\u08d2\u948f\u16eb\uff57\u028b", 1082193857, 0x5DC7CF21 ^ CryptoVault.mask()), (String)GpuUiClipMask.pj("\ue3d1\uf1b7\ueef8\u9d61\ude61\u538a\uca57\ud840\uc2c2\u7b8b\ubd99\u284e\u2251\u0baa\u08c1\ue531\u8ab1\uc0a8\u4580\u7b71\u4bac\u31de\u10e8\uf19a", 1546784347, 0x5DC7CF21 ^ CryptoVault.mask())), (class_3302)((class_4013)class_33002 -> GpuUiClipMask.releaseCacheSafely()));
    }

    private static /* synthetic */ String pj(String string, int n2, int n3) {
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
    private record CacheKey(GpuUiEffectPasses.Target parent, UiDrawCommand.ClipPush clip, float m00, float m01, float m10, float m11, float m20, float m21, int framebufferWidth, int framebufferHeight, int scaledWidth, int scaledHeight) {
    }

    @Environment(value=EnvType.CLIENT)
    private static final class CacheEntry {
        final GpuUiEffectPasses.Target target;
        boolean usedThisFrame;

        CacheEntry(GpuUiEffectPasses.Target target) {
            this.target = target;
        }
    }
}

