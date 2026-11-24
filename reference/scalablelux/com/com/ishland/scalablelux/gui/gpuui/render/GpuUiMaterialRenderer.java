/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBufferSlice
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.textures.GpuTextureView
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1044
 *  net.minecraft.class_11231
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.ishland.scalablelux.gui.gpuui.framework.UiMaterial;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.render.SafePipelines;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1044;
import net.minecraft.class_11231;
import net.minecraft.class_2960;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class GpuUiMaterialRenderer {
    private static final long START_NANOS;
    private static long lastNanos;
    public static final double LOOP_SECONDS = 7200.0;
    private static final int MAX_STATIC_ENTRIES = 32;
    private static final Map<class_2960, double[]> PHASES;
    private static final Map<Key, GpuUiEffectPasses.Target> FRAME_CACHE;
    private static final Map<Key, StaticEntry> STATIC_CACHE;

    private GpuUiMaterialRenderer() {
    }

    public static class_11231 prepare(UiMaterial uiMaterial, int n2, int n3) {
        boolean bl;
        class_1044 class_10442;
        Object object;
        int n4;
        if (uiMaterial == null || n2 <= 0 || n3 <= 0) {
            return null;
        }
        if (!SafePipelines.usable(GpuUiEffectPasses.materialPipeline(uiMaterial.shader(), uiMaterial.texture() != null))) {
            return null;
        }
        float[][] fArray = new float[15][4];
        for (n4 = 0; n4 < 15; ++n4) {
            for (int i2 = 0; i2 < 4; ++i2) {
                fArray[n4][i2] = uiMaterial.parameter(n4 * 4 + i2);
            }
        }
        n4 = GpuUiMaterialRenderer.speedFactor(uiMaterial) == 0.0f ? 1 : 0;
        Key key = new Key(uiMaterial.shader(), uiMaterial.texture(), n2, n3, fArray);
        if (n4 != 0) {
            object = STATIC_CACHE.get(key);
            if (object != null && !((StaticEntry)object).target.closed()) {
                ((StaticEntry)object).usedThisFrame = true;
                return GpuUiEffectPasses.texture(((StaticEntry)object).target);
            }
        } else {
            object = FRAME_CACHE.get(key);
            if (object != null && !((GpuUiEffectPasses.Target)object).closed()) {
                return GpuUiEffectPasses.texture((GpuUiEffectPasses.Target)object);
            }
        }
        object = null;
        if (uiMaterial.texture() != null && (object = (class_10442 = class_310.method_1551().method_1531().method_4619(uiMaterial.texture())).method_71659()) == null) {
            return null;
        }
        int n5 = (n2 + 63) / 64 * 64;
        int n6 = (n3 + 63) / 64 * 64;
        GpuUiEffectPasses.Target target = n4 != 0 && STATIC_CACHE.size() < 32 ? GpuUiEffectPasses.allocatePersistentTarget(n5, n6) : null;
        boolean bl2 = bl = target != null;
        if (target == null) {
            target = GpuUiEffectPasses.createTarget(n5, n6);
        }
        if (target == null) {
            return null;
        }
        long l2 = System.nanoTime();
        float f2 = n4 != 0 ? 0.0f : Math.max(0.0f, Math.min(0.1f, (float)(l2 - lastNanos) * 1.0E-9f));
        lastNanos = l2;
        float f3 = GpuUiMaterialRenderer.advancePhase(uiMaterial, l2);
        GpuBufferSlice gpuBufferSlice = GpuUiEffectPasses.writeParameters(n2, n3, f3, f2, fArray);
        GpuUiEffectPasses.queueFullscreen(GpuUiEffectPasses.materialPipeline(uiMaterial.shader(), object != null), (GpuTextureView)(object != null ? object : GpuUiEffectPasses.whiteTextureView()), target, gpuBufferSlice);
        if (bl) {
            StaticEntry staticEntry = new StaticEntry(target);
            staticEntry.usedThisFrame = true;
            StaticEntry staticEntry2 = STATIC_CACHE.put(key, staticEntry);
            if (staticEntry2 != null) {
                GpuUiEffectPasses.releasePersistentTarget(staticEntry2.target);
            }
        } else {
            FRAME_CACHE.put(key, target);
        }
        return GpuUiEffectPasses.texture(target);
    }

    public static void finishFrame() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.queueFencedTask(GpuUiMaterialRenderer::finishFrame);
            return;
        }
        FRAME_CACHE.clear();
        Iterator<Map.Entry<Key, StaticEntry>> iterator = STATIC_CACHE.entrySet().iterator();
        while (iterator.hasNext()) {
            StaticEntry staticEntry = iterator.next().getValue();
            if (!staticEntry.usedThisFrame || staticEntry.target.closed()) {
                GpuUiEffectPasses.releasePersistentTarget(staticEntry.target);
                iterator.remove();
                continue;
            }
            staticEntry.usedThisFrame = false;
        }
    }

    public static void releaseCache() {
        try {
            if (RenderSystem.tryGetDevice() == null) {
                STATIC_CACHE.clear();
                FRAME_CACHE.clear();
                return;
            }
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.queueFencedTask(GpuUiMaterialRenderer::releaseCache);
                return;
            }
            for (StaticEntry staticEntry : STATIC_CACHE.values()) {
                try {
                    GpuUiEffectPasses.releasePersistentTarget(staticEntry.target);
                }
                catch (LinkageError | RuntimeException throwable) {}
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        STATIC_CACHE.clear();
        FRAME_CACHE.clear();
    }

    private static float speedFactor(UiMaterial uiMaterial) {
        String string = uiMaterial.shader().method_12832();
        if (string.endsWith(GpuUiMaterialRenderer.bu("\ua544\u98f8\u2c7a\ua119\u2fbf\u6e38\u8c1f\u2cf4\u2ef9\ufd57\u4a46\u3f7c\u8a55\u3497\u5fbc\u6401", 609390781, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return 0.15f + 0.85f * uiMaterial.parameter(7);
        }
        if (string.endsWith(GpuUiMaterialRenderer.bu("\u5122\ue4de\uddbb\u7ccd\u65e7\u2a21\u60eb\u3f4a\ua6b6\uc752\u2b10\u9a51\uddd9\ubf10\ud792\u6726", 1065837116, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return GpuUiMaterialRenderer.clamp(uiMaterial.parameter(7), 0.0f, 2.0f);
        }
        if (string.endsWith(GpuUiMaterialRenderer.bu("\ub083\u3bbb\u7110\u7bba\ue96e\u1210\ud227\u8d4b\u9050\uc1e0\u230c\u787f\u71d6\u7aba\ufb5d", -518900229, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return GpuUiMaterialRenderer.clamp(uiMaterial.parameter(7), 0.0f, 1.0f);
        }
        if (string.endsWith(GpuUiMaterialRenderer.bu("\u3bb1\uc4ef\u1c68\u34c8\ub663\ua996\u2ad5\ue182\u6720\u300e\uf374\u7732\u9e57", -524407879, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return 0.55f * Math.max(0.05f, uiMaterial.parameter(19));
        }
        if (string.endsWith(GpuUiMaterialRenderer.bu("\udd5a\uaafd\u632a\udaa6\uff81\u3a36\uad16\u5d70\uc537\u4f67\ua40d\u318a\ua110", -756341361, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return GpuUiMaterialRenderer.clamp(uiMaterial.parameter(7), 0.0f, 1.0f);
        }
        if (string.endsWith(GpuUiMaterialRenderer.bu("\uaf40\u8806\uc7cd\u1561\u7a37\uf34a\uc51f\uf033\ub167\u753f\u747b\u0764\uf00b\ufee0\u81d7\u813c\ub36f\u5931\u9a5d", -355897541, 0x2CEFC987 ^ CryptoVault.mask())) || string.endsWith(GpuUiMaterialRenderer.bu("\ud3e4\uae77\u70af\uba03\uf8b3\ubdef\u3af6\uc5b1\u42df\u42ef", 335766112, 0x2CEFC987 ^ CryptoVault.mask()))) {
            return GpuUiMaterialRenderer.clamp(uiMaterial.parameter(5), 0.0f, 2.0f);
        }
        return 1.0f;
    }

    private static float clamp(float f2, float f3, float f4) {
        return Math.max(f3, Math.min(f4, f2));
    }

    private static float advancePhase(UiMaterial uiMaterial, long l2) {
        double d2;
        double[] dArray = PHASES.computeIfAbsent(uiMaterial.shader(), class_29602 -> new double[]{l2, 0.0});
        double d3 = Math.max(0.0, Math.min(0.25, ((double)l2 - dArray[0]) * 1.0E-9));
        dArray[0] = l2;
        dArray[1] = d2 = (dArray[1] + d3 * (double)GpuUiMaterialRenderer.speedFactor(uiMaterial)) % 7200.0;
        return (float)d2;
    }

    static {
        lastNanos = START_NANOS = System.nanoTime();
        PHASES = new HashMap<class_2960, double[]>();
        FRAME_CACHE = new HashMap<Key, GpuUiEffectPasses.Target>();
        STATIC_CACHE = new HashMap<Key, StaticEntry>();
    }

    private static /* synthetic */ String bu(String string, int n2, int n3) {
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
    private static final class Key {
        final class_2960 shader;
        final class_2960 texture;
        final int width;
        final int height;
        final float[] parameters;
        final int hash;

        Key(class_2960 class_29602, class_2960 class_29603, int n2, int n3, float[][] fArray) {
            this.shader = class_29602;
            this.texture = class_29603;
            this.width = n2;
            this.height = n3;
            this.parameters = new float[fArray.length * 4];
            for (int i2 = 0; i2 < fArray.length; ++i2) {
                System.arraycopy(fArray[i2], 0, this.parameters, i2 * 4, 4);
            }
            this.hash = Objects.hash(class_29602, class_29603, n2, n3) * 31 + Arrays.hashCode(this.parameters);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Key)) {
                return false;
            }
            Key key = (Key)object;
            return this.width == key.width && this.height == key.height && Objects.equals(this.shader, key.shader) && Objects.equals(this.texture, key.texture) && Arrays.equals(this.parameters, key.parameters);
        }

        public int hashCode() {
            return this.hash;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class StaticEntry {
        final GpuUiEffectPasses.Target target;
        boolean usedThisFrame;

        StaticEntry(GpuUiEffectPasses.Target target) {
            this.target = target;
        }
    }
}

