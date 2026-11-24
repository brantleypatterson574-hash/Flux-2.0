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
 *  net.minecraft.class_10868
 *  net.minecraft.class_11228
 *  net.minecraft.class_276
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_4587
 *  net.minecraft.class_6367
 *  net.minecraft.class_758
 *  net.minecraft.class_758$class_4596
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.gui.overlay.CleanCapture;
import com.ishland.scalablelux.gui.overlay.OverlayWindow;
import com.ishland.scalablelux.gui.overlay.SsProofPolicy;
import com.ishland.scalablelux.gui.overlay.SsProofSelfHeal;
import com.ishland.scalablelux.hud.HUDElement;
import com.ishland.scalablelux.hud.HUDManager;
import com.ishland.scalablelux.manager.ModuleManager;
import com.ishland.scalablelux.mixin.GameRendererAccessor;
import com.ishland.scalablelux.mixin.GuiRendererAccessor;
import com.ishland.scalablelux.module.Category;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.hooks.SsProofProvider;
import com.ishland.scalablelux.util.BorderlessFullscreen;
import com.ishland.scalablelux.util.ClientInternalLog;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.GlfwPlatform;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.ModuleAccess;
import com.ishland.scalablelux.util.Notifier;
import com.ishland.scalablelux.util.RenderBackendDetector;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10868;
import net.minecraft.class_11228;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_6367;
import net.minecraft.class_758;

@Environment(value=EnvType.CLIENT)
public final class OverlayCompositor {
    private static class_6367 fbo;
    private static int fbW;
    private static int fbH;
    private static boolean activeThisFrame;
    private static boolean worldVisualActiveThisFrame;
    private static boolean fboClearedThisFrame;
    private static boolean hudFlushActive;
    private static boolean vanillaHudFlushActive;
    private static boolean contentDrawnThisFrame;
    private static SsProofPolicy.State currentState;
    private static long pendingSinceMs;
    private static int consecutiveAffinityFailures;
    private static SsProofPolicy.Reason lastNoticeReason;
    private static volatile boolean waylandNoticeShown;
    private static boolean unsupportedNoticeShown;
    private static volatile boolean vulkanNoticeShown;
    private static final Map<Class<?>, Boolean> DRAWS;
    private static final SsProofSelfHeal selfHeal;

    private OverlayCompositor() {
    }

    public static boolean isSsProofEnabled() {
        try {
            SsProofProvider ssProofProvider = ModuleAccess.getHook(SsProofProvider.class);
            return ssProofProvider != null && ssProofProvider.isSsProofEnabled() && !GlfwPlatform.isWayland() && !RenderBackendDetector.isVulkanBackend();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static void noteUnsupportedPlatform() {
        if (unsupportedNoticeShown) {
            return;
        }
        unsupportedNoticeShown = true;
        OverlayCompositor.notice(OverlayCompositor.sv("\u55eb\ub5fb\ucd9d\u5e28\u1446\ue45b\ube78\u18cf\ub996\uc4ee\u917d\u6af7\u379c\u544a\u63c8\uefb8\ubaab\u9c51\u9bc2\u98d6\u9e29\u9d46\uc7e9", 277271829, 0x22591C59 ^ CryptoVault.mask()), SsProofPolicy.noticeText(SsProofPolicy.Reason.UNSUPPORTED_PLATFORM));
    }

    public static void noteVulkanUnavailable() {
        if (vulkanNoticeShown) {
            return;
        }
        vulkanNoticeShown = true;
        OverlayCompositor.notice(OverlayCompositor.sv("\u957a\u4d32\uf071\ud368\u9e56\u249e\u36d2\u2ab5\ucf60\ubada\u22a7\udad1\uee43\u1a60\u518a\u2fd9\u0770\ub105\u99ca\ueec6\u3af2\u2cf1\ua269\ua64d\u24ff\u3d8d\u3372\u8207\u5066\u7b56\u8d7c\u09f4\u1418\ubc1d", -1808093815, 0x22591C59 ^ CryptoVault.mask()), OverlayCompositor.sv("\ub564\u9f0e\u6d72\ufd23\u114b\uee5e\u950b\u0d59\u3a4c\u4121\ucd32\ua3c2\ufa83\u8814\ue203\u202a\u45e5\u55c8\u7db3\u850f\u6c0d\udbb7\u84f7\u7a82\u9966\ue844\ube9f\u5ca4\u7137\ub5dd\udf6c\u1c77\u235f\uaf79\ude80\u7ce2\ua0d6\u7f74\ua80b\u4c41\u7dba\ue6cc\u7bf5\ud0be\u5666\uf045\ub643\u7b49\u48c3\ub7cd\uf23a\u0fc2\u30b0\u025c\u4933\ud77a\u6e75\ub8e3\ud9d1\u54f7\ucb97\uad14\uc6e5\u8b3b\u4723\udcf3\u41a3\u4a68\u48dd\uea17\u59a5\udb8c\u2934\ufba1\u733b\u56ca\u9008\u6595\u5bfe\u55a5\u298d\ua432\u3eb7\ucf40\u5ad6\u48fb\ud238\u2ac0\u0740\u64ec\u0291\ue284\u6642\u3176\ue36c", 89754645, 0x22591C59 ^ CryptoVault.mask()));
    }

    public static void noteWaylandUnavailable() {
        if (waylandNoticeShown) {
            return;
        }
        waylandNoticeShown = true;
        OverlayCompositor.notice(OverlayCompositor.sv("\ua457\u60b1\u4e72\u6aec\u81b7\uaf2b\ua2c0\uc380\u749f\ue6d0\ue86a\u2f34\u3555\u8151\u5ad2\u5504\u6e68\u4401\u07b0\u4ddf\u2ebb\u4307\u7e93\u3e34\u6ff0\u38dd\u4edf\u62be\ub069\u16da\uba07\ua6e9", -648564507, 0x22591C59 ^ CryptoVault.mask()), OverlayCompositor.sv("\ud91d\u5dd5\u20be\u5131\u7d0c\uc2cd\udd9b\u589e\u3bb3\u4aea\ucdc4\u2736\uaa5b\uc88c\u0b03\u6651\ua645\u710f\u111c\udaa7\udc85\u712e\u4647\ud107\u407c\u91f6\u3c3e\ua763\uc8c0\udff3\u2339\u894b\u7c79\u62e8\u2a3f\u3eea\u53ee\u185c\u2b58\ufe64\u3a3c\ue258\u5f4b\u34ff\u5c81\u47e6\u94d4\uddd7\u22bb\u5d6f\ufb3b\u19f5\u1c22\u05ea\u1c56\u6971\u14ef\ubb62\u7e33\ubc93\u82eb\u49e8\u476a\udfda\u01d3\u9615", -79409768, 0x22591C59 ^ CryptoVault.mask()));
    }

    public static boolean isSsProofActive() {
        return currentState == SsProofPolicy.State.READY;
    }

    public static boolean isSsProofReady() {
        return OverlayCompositor.isSsProofActive();
    }

    public static boolean isSsProofOn() {
        return currentState != SsProofPolicy.State.OFF;
    }

    public static boolean isOverlayCaptureActive() {
        return activeThisFrame;
    }

    public static boolean isFluxScreen(class_437 class_4372) {
        return SsProofPolicy.isFluxScreenClass(class_4372.getClass().getName(), OverlayCompositor.class.getPackageName());
    }

    public static boolean shouldRedirect() {
        if (currentState == SsProofPolicy.State.OFF || CleanCapture.isActive()) {
            return false;
        }
        class_310 class_3102 = class_310.method_1551();
        return class_3102 != null && McCompat.screen(class_3102) != null && OverlayCompositor.isFluxScreen(McCompat.screen(class_3102));
    }

    private static boolean drawsVisuals(Module module) {
        return DRAWS.computeIfAbsent(module.getClass(), clazz -> {
            try {
                if (clazz.getMethod(OverlayCompositor.sv("\u7e1e\ua2a2\ub7ae\uac19\u46c9\u69c9\u9907\ucf87\uc493\u1eb4\ud62e\ufe58\u3f0c\u347d\uf44c\u836c", -1997661291, 0x22591C59 ^ CryptoVault.mask()), class_4587.class, Float.TYPE).getDeclaringClass() != Module.class) {
                    return true;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            for (Method method : clazz.getMethods()) {
                if (!method.getName().equals(OverlayCompositor.sv("\ud4e3\u94d6\uf87f\u9f67\u7af9\u24a7\u4176\ua061\u10c3\u7aa6", -614216512, 0x22591C59 ^ CryptoVault.mask())) || method.getParameterCount() != 2 || method.getDeclaringClass() == Module.class) continue;
                return true;
            }
            return false;
        });
    }

    private static boolean hasActiveRenderVisuals() {
        ModuleManager moduleManager;
        SlApp slApp = SlApp.getInstance();
        ModuleManager moduleManager2 = moduleManager = slApp != null ? slApp.getModuleManager() : null;
        if (moduleManager == null) {
            return false;
        }
        for (Module module : moduleManager.getModules()) {
            if (!module.isEnabled()) continue;
            String string = module.getName();
            if (OverlayCompositor.sv("\u3fe4\u090d\ufdd1\u838c\ucd69\ude5b\u7223\ub6f5\ufdbe\ucda3\u2e54", 1202294865, 0x22591C59 ^ CryptoVault.mask()).equalsIgnoreCase(string) || OverlayCompositor.sv("\u171f\ud774\u3018\u7598\udf2b\u7924\u8b90\u0bae\ud39f\u9847\uef95\u4b2d", 395138821, 0x22591C59 ^ CryptoVault.mask()).equalsIgnoreCase(string) || module.getCategory() != Category.yZ && !OverlayCompositor.drawsVisuals(module)) continue;
            return true;
        }
        return false;
    }

    private static boolean hasActiveHudVisuals() {
        HUDManager hUDManager = HUDManager.getInstance();
        if (hUDManager == null) {
            return false;
        }
        for (HUDElement hUDElement : hUDManager.getElements()) {
            if (!hUDElement.isEnabled()) continue;
            return true;
        }
        return false;
    }

    public static boolean shouldRedirectHud() {
        if (currentState == SsProofPolicy.State.OFF || CleanCapture.isActive()) {
            return false;
        }
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null) {
            return false;
        }
        if (McCompat.screen(class_3102) != null && OverlayCompositor.isFluxScreen(McCompat.screen(class_3102))) {
            return false;
        }
        return OverlayCompositor.hasActiveRenderVisuals() || OverlayCompositor.hasActiveHudVisuals();
    }

    public static void frameTick() {
        if (CleanCapture.isCapturing()) {
            return;
        }
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 != null) {
                BorderlessFullscreen.poll(class_3102.method_22683());
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        fboClearedThisFrame = false;
        contentDrawnThisFrame = false;
        currentState = OverlayCompositor.resolveState();
        if (currentState == SsProofPolicy.State.OFF) {
            if (OverlayWindow.isShown()) {
                OverlayWindow.hide();
            }
            return;
        }
        OverlayWindow.ensureCreated();
    }

    private static SsProofPolicy.State resolveState() {
        int n2;
        if (!OverlayCompositor.isSsProofEnabled()) {
            pendingSinceMs = 0L;
            consecutiveAffinityFailures = 0;
            lastNoticeReason = SsProofPolicy.Reason.NONE;
            return SsProofPolicy.State.OFF;
        }
        boolean bl = SsProofPolicy.isSupportedPlatform(System.getProperty(OverlayCompositor.sv("\ubd93\u3a13\u2454\u9b11\u7212\ue68b\u4297\u3387\u78c0", -2086987917, 0x22591C59 ^ CryptoVault.mask()), ""));
        boolean bl2 = OverlayWindow.isAvailable();
        int n3 = bl2 ? OverlayWindow.affinityState() : (n2 = OverlayWindow.creationGaveUp() ? 2 : 0);
        if (n2 == 1) {
            consecutiveAffinityFailures = 0;
        } else if (n2 == 2) {
            ++consecutiveAffinityFailures;
        }
        SsProofPolicy.State state = SsProofPolicy.resolve(true, bl, bl2, n2);
        long l2 = System.currentTimeMillis();
        if (state == SsProofPolicy.State.PENDING) {
            if (pendingSinceMs == 0L) {
                pendingSinceMs = l2;
            }
        } else {
            pendingSinceMs = 0L;
        }
        if (state == SsProofPolicy.State.PENDING && pendingSinceMs > 0L && l2 - pendingSinceMs >= 8000L) {
            consecutiveAffinityFailures = Math.max(consecutiveAffinityFailures, 3);
        }
        if (HitboxConfigScreen752809.aa() && SsProofPolicy.shouldFailOpen(true, consecutiveAffinityFailures)) {
            HitboxConfigScreen752809.ab();
            OverlayCompositor.showNotice(SsProofPolicy.failOpenNoticeText());
            consecutiveAffinityFailures = 0;
            lastNoticeReason = SsProofPolicy.Reason.NONE;
            return SsProofPolicy.State.OFF;
        }
        SsProofPolicy.Reason reason = SsProofPolicy.reason(state, bl, n2, pendingSinceMs, l2);
        if ((reason == SsProofPolicy.Reason.AFFINITY_FAILED || reason == SsProofPolicy.Reason.START_TIMEOUT) && OverlayWindow.IRIS_PRESENT && OverlayWindow.creationGaveUp()) {
            reason = SsProofPolicy.Reason.RENDER_MOD_CONFLICT;
        }
        if (reason != SsProofPolicy.Reason.NONE) {
            if (reason != lastNoticeReason) {
                lastNoticeReason = reason;
                OverlayCompositor.showNotice(SsProofPolicy.noticeText(reason));
            }
        } else {
            lastNoticeReason = SsProofPolicy.Reason.NONE;
        }
        return state;
    }

    public static void noteMenuClosed(long l2) {
        try {
            if (!OverlayCompositor.isSsProofEnabled()) {
                return;
            }
            if (!selfHeal.note(l2, System.currentTimeMillis())) {
                return;
            }
            HitboxConfigScreen752809.ab();
            OverlayCompositor.showNotice(OverlayCompositor.sv("\u4472\u92a9\ubcc5\u0fdf\u2336\u089d\u53d3\ucef7\u0bf6\u6397\u286b\u7d10\ufd08\uffb9\u68dd\ucc0e\u6388\u1bd5\u93cc\u9f50\u928b\u927f\uc2b6\ubcc7\u028d\ud06e\u5a48\u91c4\u4ef1\u2da9\ubcaa\ubb8f\ub3bc\u3511\u776c\uc9e9\u16e5\ua12d\ue781\udb08\ub31b\u54d6\u8919\u2922\u9641\ua610\u9da1\u3a8f\u2db8\u9ee4\u8404\uc7b3\ua550\ue9f1\uc6c4\u1856\u4184\uf82b\ubd58\u106e\ub5f8\ue970\u0012\u8012\u2999\u3c5a\u094a\u7940\ue338\u3cdb\u78c1\u40a4\u72c2\u3ce9\u1c6a\u8004\ud1b0\ud87e\u449d\u5577\u0e6a\u2a72\uf387\u433c\u0add\u1cbf\u2b34\ue2ef\u43e4\u1c10\uc519\u009b\u9267\u2944\u9c40\u127b\uf633\u945e\uf52f\u6f11\u291d\ub9d0\u7190\u22d7\u1686\u0b76\u9fe1\u0f35\ufc2f\u2f88\ub9a2\u0dab\u8cea\u2c01", 718023465, 0x22591C59 ^ CryptoVault.mask()));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void overlayStayedBlack() {
        try {
            HitboxConfigScreen752809.ab();
            ClientInternalLog.warn(OverlayCompositor.sv("\u8102\u2f94\ueacb\u83e5\u4592\uc153\u656b\ucd61\ub6f5", -379544667, 0x22591C59 ^ CryptoVault.mask()), OverlayCompositor.sv("\u07af\ua594\ub785\udfa9\ua75e\u3b85\u1797\u0e2f\u6f7f\u8083\u1da7\u2320\uda6b\u7a56\uebaf\u518a\ued08\u701c\ube47\u1f70\ud74a\u3845\u5dff\u20d0\ufadc\uae21\uf9ae\u2e29\u1e1d\ubf4c\ue564\uef5c\u9c6b\ud200\ufd35\ucc0e\u645e\ua8b6\ube68\u8745\ub2af\u199a\uf2cf\u1d83\u7e5c\u8063\ube1b\ud349\ub800\uf132\ucae7\u2c2e\u9de9\uc761\u446f\uf4c8\u364e\u82eb\u9dd3\uef6b\u397e\u30a7\u10f1\u5549\u9781\ua980\u29e4\u26f3\u848e\u5bb6", -1079729453, 0x22591C59 ^ CryptoVault.mask()));
            OverlayCompositor.showNotice(OverlayCompositor.sv("\u1a01\u3826\uea34\u881f\uf03d\u1cef\u99c5\u51c1\ue62c\ud819\u83fc\u1de8\u1973\u262e\ua4b3\u1c66\u89c4\u8506\uf3f1\ub946\u817c\u444a\ue519\u622a\u8f2b\uba5d\ucee2\u6ece\u3d48\uad8a\u292a\u8b77\u9202\u3084\u3ea0\uf264\u4940\u5b24\u7e20\ub769\u1ee1\u11f2\u2be0\u6ff1\uea74\u3b86\udb7b\uca0f\u6f46\u205b\u3f45\u736e\u4ae1\u871c\u29d8\u9842\u6a1c\u9182\ubd2e\u609d\ud53a\u9e30\u9510\u9f85\uc059\uc0d0\u85ea\u8bb7\u2094\u421d\u83c1\u4681\ud920\ue37b\uded1\u2c3b\u2a55\u5fe8\uf69e\u1ed3\uc201\u18d9\u4ad6\uff10\u0831\ud35a\u8268\u1754\u78c4\ud726\u7925\u6bb4\u3a1e\ud2c8\uf9dd\uc4c6\u709f\u0506\uc31b\uf33a\u6fbf\u96ad\u5e6f\u8673\u9e37\u4b20\ubefa\u612a\u123d\u5a49\ua802\ubd47\u48eb\u1af1\u0d27\u5889\u0e42\u4937\uf2d1\u24e7\uc5aa\u4f74\u94e1\u030a\ud64b\u610d\uc0ee\u22dc\u3924\ud78b\u47f6\u3666\uf5d2\u6f77\u0828\ufd73\u1eab\uc403\u6cca\uff12\u6d48\uab23\ub388\u03d9\u98ee\ucaa9", -1791742959, 0x22591C59 ^ CryptoVault.mask()));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void showNotice(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        OverlayCompositor.notice(OverlayCompositor.sv("\u230d\u5315\uff04\ua9af\u7d98\ub4ab\ub4b8\uba9f\u9ea3\u109e\u5b03\u7894", -430828894, 0x22591C59 ^ CryptoVault.mask()), string);
    }

    private static void notice(String string, String string2) {
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null) {
                return;
            }
            class_3102.execute(() -> Notifier.warn(string, string2));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void beginWorldRender() {
        worldVisualActiveThisFrame = false;
        if (currentState == SsProofPolicy.State.OFF || CleanCapture.isActive() || !OverlayCompositor.hasActiveRenderVisuals()) {
            return;
        }
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null) {
            return;
        }
        class_276 class_2762 = McCompat.mainRenderTarget(class_3102);
        if (class_2762 == null) {
            return;
        }
        int n2 = class_2762.field_1482;
        int n3 = class_2762.field_1481;
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        if (!OverlayCompositor.ensureFbo(n2, n3)) {
            return;
        }
        if (!fboClearedThisFrame) {
            OverlayCompositor.clearFbo();
            fboClearedThisFrame = true;
        }
        try {
            RenderSystem.outputColorTextureOverride = fbo.method_71639();
            RenderSystem.outputDepthTextureOverride = class_2762.method_71640();
            worldVisualActiveThisFrame = true;
            contentDrawnThisFrame = true;
        }
        catch (Throwable throwable) {
            worldVisualActiveThisFrame = false;
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
        }
    }

    public static void endWorldRender() {
        if (!worldVisualActiveThisFrame) {
            return;
        }
        try {
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void renderAndFlushHudEarly(class_332 class_3322, float f2) {
        if (!OverlayCompositor.shouldRedirectHud()) {
            return;
        }
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || class_3102.field_1773 == null) {
            return;
        }
        class_276 class_2762 = McCompat.mainRenderTarget(class_3102);
        if (class_2762 == null) {
            return;
        }
        int n2 = class_2762.field_1482;
        int n3 = class_2762.field_1481;
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        if (!OverlayCompositor.ensureFbo(n2, n3)) {
            return;
        }
        try {
            HUDManager hUDManager;
            SlApp slApp = SlApp.getInstance();
            if (slApp != null) {
                slApp.getModuleManager().onRender(class_3322, f2);
            }
            if ((hUDManager = HUDManager.getInstance()) != null && McCompat.screen(class_3102) == null) {
                hUDManager.render(class_3322, f2);
            }
        }
        catch (Throwable throwable) {
            return;
        }
        if (!fboClearedThisFrame) {
            OverlayCompositor.clearFbo();
            fboClearedThisFrame = true;
        }
        hudFlushActive = true;
        try {
            if (OverlayCompositor.flushGui(class_3102)) {
                contentDrawnThisFrame = true;
            }
        }
        finally {
            hudFlushActive = false;
        }
    }

    public static void flushVanillaHudBehindMenu() {
        if (!OverlayCompositor.shouldRedirect()) {
            return;
        }
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || class_3102.field_1773 == null) {
            return;
        }
        vanillaHudFlushActive = true;
        try {
            OverlayCompositor.flushGui(class_3102);
        }
        finally {
            vanillaHudFlushActive = false;
        }
    }

    private static boolean flushGui(class_310 class_3102) {
        try {
            GameRendererAccessor gameRendererAccessor = (GameRendererAccessor)class_3102.field_1773;
            class_758 class_7582 = gameRendererAccessor.sl$getFogRenderer();
            class_11228 class_112282 = gameRendererAccessor.sl$getGuiRenderer();
            if (class_7582 != null && class_112282 instanceof GuiRendererAccessor) {
                GuiRendererAccessor guiRendererAccessor = (GuiRendererAccessor)class_112282;
                guiRendererAccessor.sl$invokeRender(class_7582.method_71109(class_758.class_4596.field_60101));
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public static void beginFrame() {
        activeThisFrame = false;
        if (vanillaHudFlushActive || !OverlayCompositor.shouldRedirect()) {
            return;
        }
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null) {
                return;
            }
            class_276 class_2762 = McCompat.mainRenderTarget(class_3102);
            if (class_2762 == null) {
                return;
            }
            int n2 = class_2762.field_1482;
            int n3 = class_2762.field_1481;
            if (n2 <= 0 || n3 <= 0) {
                return;
            }
            if (!OverlayCompositor.ensureFbo(n2, n3)) {
                return;
            }
            if (!fboClearedThisFrame) {
                OverlayCompositor.clearFbo();
                fboClearedThisFrame = true;
            }
            activeThisFrame = true;
            contentDrawnThisFrame = true;
        }
        catch (Throwable throwable) {
            activeThisFrame = false;
        }
    }

    public static class_276 guiTarget(class_276 class_2762) {
        if ((activeThisFrame || hudFlushActive) && fbo != null) {
            return fbo;
        }
        return class_2762;
    }

    public static void endFrame() {
        activeThisFrame = false;
    }

    public static void finishFrame() {
        if (CleanCapture.isCapturing()) {
            return;
        }
        try {
            class_276 class_2762;
            int n2;
            if (CleanCapture.isActive() && currentState == SsProofPolicy.State.READY && CleanCapture.scene() != null && (n2 = OverlayCompositor.extractGlId((class_2762 = CleanCapture.scene()).method_30277())) > 0) {
                OverlayWindow.presentOpaque(n2, class_2762.field_1482, class_2762.field_1481);
                return;
            }
            if (contentDrawnThisFrame && fbo != null && currentState == SsProofPolicy.State.READY && (n2 = OverlayCompositor.extractGlId((GpuTexture)(class_2762 = fbo.method_30277()))) > 0) {
                OverlayWindow.presentMirror(n2, fbW, fbH, false);
                return;
            }
            if (OverlayWindow.isShown()) {
                OverlayWindow.hide();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static boolean ensureFbo(int n2, int n3) {
        if (n2 <= 0 || n3 <= 0) {
            return false;
        }
        try {
            if (fbo == null) {
                fbo = new class_6367(OverlayCompositor.sv("\uba49\u8d7f\ue30e\ua7b3\u672a\u6ed0\uf4ef\u3f80\uad7b\u85bb\uf56e\u0254", -1802576642, 0x22591C59 ^ CryptoVault.mask()), n2, n3, true);
                fbW = n2;
                fbH = n3;
            } else if (n2 != fbW || n3 != fbH) {
                fbo.method_1234(n2, n3);
                fbW = n2;
                fbH = n3;
            }
        }
        catch (Throwable throwable) {
            try {
                if (fbo != null) {
                    fbo.method_1238();
                }
            }
            catch (Throwable throwable2) {
                // empty catch block
            }
            fbo = null;
            fbH = 0;
            fbW = 0;
            return false;
        }
        return true;
    }

    private static void clearFbo() {
        try {
            GpuDevice gpuDevice = RenderSystem.tryGetDevice();
            if (gpuDevice != null && fbo != null && fbo.method_30277() != null) {
                CommandEncoder commandEncoder = gpuDevice.createCommandEncoder();
                if (fbo.method_30278() != null && OverlayCompositor.fbo.field_1478) {
                    commandEncoder.clearColorAndDepthTextures(fbo.method_30277(), 0, fbo.method_30278(), 1.0);
                } else {
                    commandEncoder.clearColorTexture(fbo.method_30277(), 0);
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static int extractGlId(GpuTexture gpuTexture) {
        Class<?> clazz;
        if (gpuTexture == null) {
            return -1;
        }
        if (gpuTexture instanceof class_10868) {
            class_10868 class_108682 = (class_10868)gpuTexture;
            return class_108682.method_68427();
        }
        for (clazz = gpuTexture.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (AccessibleObject accessibleObject : clazz.getDeclaredMethods()) {
                if (((Method)accessibleObject).getParameterCount() != 0 || ((Method)accessibleObject).getReturnType() != Integer.TYPE || !((Method)accessibleObject).getName().equals(OverlayCompositor.sv("\uc6bb\u8f84\u73ab\u72d2\u13c4\u307c\uf3ec\ue1f0\u25a3\u7ad1", -1543461282, 0x22591C59 ^ CryptoVault.mask())) && !((Method)accessibleObject).getName().equals(OverlayCompositor.sv("\uc87c\u2051\ue9aa\ub88e\u2696\u17e6", 1421596427, 0x22591C59 ^ CryptoVault.mask()))) continue;
                try {
                    Integer n2;
                    ((Method)accessibleObject).setAccessible(true);
                    Object object = ((Method)accessibleObject).invoke(gpuTexture, new Object[0]);
                    if (!(object instanceof Integer) || (n2 = (Integer)object) <= 0) continue;
                    return n2;
                }
                catch (Throwable n3) {
                    // empty catch block
                }
            }
        }
        for (clazz = gpuTexture.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (AccessibleObject accessibleObject : clazz.getDeclaredFields()) {
                if (((Field)accessibleObject).getType() != Integer.TYPE || !((Field)accessibleObject).getName().equals(OverlayCompositor.sv("\u8c0c\ua6d9\u09f9\ud274\u6f7f\u1a61\u1b95", 470108513, 0x22591C59 ^ CryptoVault.mask())) && !((Field)accessibleObject).getName().equals("id")) continue;
                try {
                    ((Field)accessibleObject).setAccessible(true);
                    int n3 = ((Field)accessibleObject).getInt(gpuTexture);
                    if (n3 <= 0) continue;
                    return n3;
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
        return -1;
    }

    public static void dispose() {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 != null && !class_3102.method_18854()) {
            if (class_3102.method_22108()) {
                class_3102.execute(OverlayCompositor::dispose);
            }
            return;
        }
        try {
            if (fbo != null) {
                fbo.method_1238();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        fbo = null;
        fbH = 0;
        fbW = 0;
        activeThisFrame = false;
        worldVisualActiveThisFrame = false;
        fboClearedThisFrame = false;
        hudFlushActive = false;
        vanillaHudFlushActive = false;
        contentDrawnThisFrame = false;
        currentState = SsProofPolicy.State.OFF;
        pendingSinceMs = 0L;
        consecutiveAffinityFailures = 0;
        lastNoticeReason = SsProofPolicy.Reason.NONE;
    }

    static {
        fbW = 0;
        fbH = 0;
        activeThisFrame = false;
        worldVisualActiveThisFrame = false;
        fboClearedThisFrame = false;
        hudFlushActive = false;
        vanillaHudFlushActive = false;
        contentDrawnThisFrame = false;
        currentState = SsProofPolicy.State.OFF;
        pendingSinceMs = 0L;
        consecutiveAffinityFailures = 0;
        lastNoticeReason = SsProofPolicy.Reason.NONE;
        DRAWS = new ConcurrentHashMap();
        selfHeal = new SsProofSelfHeal();
    }

    private static /* synthetic */ String sv(String string, int n2, int n3) {
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

