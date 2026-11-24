/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.config.ClientPrefs;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiSurface;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.kit.UiColorPicker;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.module.Category;
import com.ishland.scalablelux.module.ColorSetting;
import com.ishland.scalablelux.module.ModeSetting;
import com.ishland.scalablelux.util.CryptoVault;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class SetupPicker {
    private static final String DROPDOWN_SETTINGS_BODY = SetupPicker.yv("\ud727\u4a87\ud575\ub4e7\u7ed7\u5d42\uc06a\u7582\u7414\ufcfb\ub8e7\u4b12\ub49c\u1af4\uaaa1\uc6f3\u65ea\uacba\u3f13\u7774\u4b2d\u94b1\ube77\u715e\u2bb4\uad8f\u669c\ua58f\ud979\u2fd8\u21f9\u91cf\u6074\u528c\u63e6\u5284\uf965\uf400\uc1ba\udffe\u9d55\u5754\uf094\u8c8d\uea02\u4e70\u38ec\ud1f5\u81ef\uc061\u1acb\u6eb4\u3149\uc25d\u455c\u2ed2\u0876\u95cb\u1479\u2f5b\u7370\u2264\ub33b\u6b79\uc7df\u2a0e\u67ba\u849f\u6bc4\u712f\u4961\u940f\u2be8\u1859\uf5cb\u9710\ude33\ucaed\u5c34\u7a63\uc80d\u8f95\uce68\u21b8\u9792\ua5d3\u13c5\u989b\u62f3\ufa27\u104f\u321d\ua401\u0611\u9646\u13ef\ue858\ud96f\u2d10\u3e4f\ufa47\u5983\uef80\u8919\ue31a\u2f4f\ue62a\u5390\u65a5\u2c1b\u69cd\u0c9a\ubd84\u1d46\u8eec\u23da\ue842\uc03e\ua2da\uf75e\uc6d8\u4cb3\u0e03\ub75e\u0f32\u1719\u28d5\u4ec4\u1f29\u9213\u5ee5\u8fa6\u5ee3\u1b20\u818c\u2f06\ue5cc\u6ec1", -356733124, 0xDA7D000E ^ CryptoVault.mask());
    private static final String[] SOUND_LABELS = new String[]{SetupPicker.yv("\u8335\ud61f\u1914\u1ec2\ub202\ub974\u4135", -1564243677, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u91d1\u3f32\u8f98\u2519\u6385\u4dfd\ucac1\u715f\u5832\u585b", -610082960, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u9125\ue1f3\uf81c\u05ea\u0405\u91b8\u56a5\ua813", -1441096837, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u3c31\ub9fe\uea26\u4055\uccc1\ue4ee\udcf9\u20fe", -927933675, 0xDA7D000E ^ CryptoVault.mask())};
    private boolean active;
    private final UiColorPicker accentPicker = new UiColorPicker().compact(true);
    private Category editingAccent;
    private boolean layoutSwitched;
    private int index;
    private String layoutAtStart = "";
    private float themeScroll;
    private float themeMaxScroll;
    private float lastS = 1.0f;
    private final boolean[] touched = new boolean[Step.values().length];
    private static int pendingIndex = -1;
    private static boolean[] pendingTouched;
    private static String pendingLayout;
    private static SetupPicker activeInstance;

    public boolean active() {
        return this.active;
    }

    public void begin() {
        this.active = true;
        this.layoutSwitched = false;
        this.index = 0;
        this.themeScroll = 0.0f;
        Arrays.fill(this.touched, false);
        this.layoutAtStart = SetupPicker.currentLayout();
        activeInstance = this;
    }

    public static boolean hasPendingResume(String string) {
        return pendingIndex >= 0 && pendingLayout.equalsIgnoreCase(string) && SetupPicker.currentLayout().equalsIgnoreCase(string);
    }

    public static void captureForLayoutSwitch() {
        SetupPicker setupPicker = activeInstance;
        if (setupPicker == null || !setupPicker.active) {
            return;
        }
        if (SetupPicker.currentLayout().equals(setupPicker.layoutAtStart)) {
            return;
        }
        pendingIndex = setupPicker.index;
        pendingTouched = (boolean[])setupPicker.touched.clone();
        SetupPicker.pendingTouched[Step.LAYOUT.ordinal()] = true;
        pendingLayout = SetupPicker.currentLayout();
        setupPicker.layoutSwitched = true;
        setupPicker.active = false;
        activeInstance = null;
    }

    public void resumePending() {
        int n2 = pendingIndex;
        boolean[] blArray = pendingTouched;
        pendingIndex = -1;
        pendingTouched = null;
        this.begin();
        this.index = Math.max(0, Math.min(n2, this.steps().size() - 1));
        if (blArray != null) {
            System.arraycopy(blArray, 0, this.touched, 0, Math.min(blArray.length, this.touched.length));
        }
    }

    public boolean consumeLayoutSwitch() {
        boolean bl = this.layoutSwitched;
        this.layoutSwitched = false;
        return bl;
    }

    public void next() {
        if (!this.active) {
            return;
        }
        if (this.index >= this.steps().size() - 1) {
            this.finishSetup();
        } else {
            ++this.index;
            this.themeScroll = 0.0f;
        }
    }

    public void back() {
        if (!this.active || this.index <= 0) {
            return;
        }
        --this.index;
        this.themeScroll = 0.0f;
    }

    public void skipSetup() {
        if (this.active) {
            this.finishSetup();
        }
    }

    public void scroll(double d2) {
        if (!this.active || this.steps().get(Math.min(this.index, this.steps().size() - 1)) != Step.THEME) {
            return;
        }
        this.themeScroll = Math.max(0.0f, Math.min(this.themeMaxScroll, this.themeScroll - (float)d2 * 46.0f * this.lastS));
    }

    private void finishSetup() {
        this.active = false;
        if (activeInstance == this) {
            activeInstance = null;
        }
        pendingIndex = -1;
        pendingTouched = null;
        pendingLayout = "";
        try {
            ClientPrefs.get().setSetupPickerSeen(true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static String currentLayout() {
        return HitboxConfigScreen752809.U == null ? "" : HitboxConfigScreen752809.U.getCurrentMode();
    }

    private List<Step> steps() {
        ArrayList<Step> arrayList = new ArrayList<Step>();
        arrayList.add(Step.THEME);
        arrayList.add(Step.LAYOUT);
        if (HitboxConfigScreen752809.m != null) {
            arrayList.add(Step.MODULE_SETTINGS);
        }
        arrayList.add(Step.PERFORMANCE);
        arrayList.add(Step.ACCENTS);
        arrayList.add(Step.SS_PROOF);
        if (HitboxConfigScreen752809.as != null && HitboxConfigScreen752809.as.isEnabled() && HitboxConfigScreen752809.j() && HitboxConfigScreen752809.k()) {
            arrayList.add(Step.CLEAN_CAPTURE);
        }
        arrayList.add(Step.COMFORT);
        return arrayList;
    }

    public static boolean shouldOffer() {
        try {
            if (ClientPrefs.get().isSetupPickerSeen()) {
                return false;
            }
            return HitboxConfigScreen752809.V == null || !HitboxConfigScreen752809.V.getValue();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, LayoutPainter layoutPainter) {
        if (!this.active) {
            return;
        }
        if (!SetupPicker.currentLayout().equals(this.layoutAtStart)) {
            pendingIndex = this.index;
            pendingTouched = (boolean[])this.touched.clone();
            SetupPicker.pendingTouched[Step.LAYOUT.ordinal()] = true;
            pendingLayout = SetupPicker.currentLayout();
            this.layoutSwitched = true;
            this.active = false;
            return;
        }
        this.lastS = f4;
        List<Step> list = this.steps();
        if (this.index >= list.size()) {
            this.index = list.size() - 1;
        }
        Step step = list.get(this.index);
        uiHost.hit(0.0f, 0.0f, f2, f3, () -> {});
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, -1946157056);
        float f7 = Math.round(20.0f * f4);
        float f8 = Math.round(Math.min(600.0f * f4, f2 - 32.0f * f4));
        float f9 = Math.round(Math.min(f3 - 24.0f * f4, 440.0f * f4));
        float f10 = Math.round((f2 - f8) * 0.5f);
        float f11 = Math.round((f3 - f9) * 0.5f);
        float f12 = Math.round(16.0f * f4 * uiHost.cornerScale());
        builder.shadow(f10, f11 + 4.0f * f4, f8, f9, f12, 26.0f * f4, 0.0f, -1610612736);
        uiHost.panel(builder, f10, f11, f8, f9, f12);
        builder.border(f10, f11, f8, f9, f12, Math.max(1.0f, f4), UiKit.withAlpha(uiHost.accent(), 112));
        uiHost.hit(f10, f11, f8, f9, () -> {});
        float f13 = ((float)this.index + 1.0f) / (float)list.size();
        float f14 = Math.max(2.0f, (float)Math.round(2.5f * f4));
        builder.clipPush(f10, f11, f8, f9, f12);
        builder.rect(f10, f11, f8, f14, 0.0f, 0x14FFFFFF);
        builder.rect(f10, f11, f8 * uiHost.anim(SetupPicker.yv("\u96f4\uf904\u81ac\u6e9e\uaae2\ua200\u2600\u00b0\u6652\ufc16\u713e\u46aa\u3a61\uc3ef\udeb6\u2604\ud49b\u7ffe", 267604061, 0xDA7D000E ^ CryptoVault.mask()), f13, 14.0f), f14, 0.0f, uiHost.accent());
        builder.clipPop();
        float f15 = 15.0f * f4;
        builder.text(f10 + f7, f11 + f7, f15, uiHost.titleColor(), UiKit.fit(step.title, f8 - f7 * 2.0f - 70.0f * f4, f15, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        String string = this.index + 1 + SetupPicker.yv("\ub901\u3831\u7f94\ua376\ubccd", 1064753876, 0xDA7D000E ^ CryptoVault.mask()) + list.size();
        float f16 = GpuUiFontMetrics.textWidth(string, 9.5f * f4);
        builder.text(f10 + f8 - f7 - f16, f11 + f7 + 3.0f * f4, 9.5f * f4, uiHost.muted2Color(), string);
        float f17 = 10.5f * f4;
        float f18 = Math.round(15.0f * f4);
        String string2 = step == Step.MODULE_SETTINGS && SetupPicker.dropdownSettingsStep() ? SetupPicker.yv("\u5698\u13ea\u012b\uf7ca\u0500\u54c0\ub527\u3944\u8fef\u7459\uf057\uf5fc\ud5ef\uce27\u171f\uee1f\uf115\ue775\u0329\uf715\ueec6\u707d\u0301\u0842\uae98\u71ab\u2ac1\u5d46\u9f32\uad54\u7e7e\u9fa2\u7d47\ub013\u262b\u8f8a\ud3f5\u52c1\u19a4\ue3e7\u93f7\u98d6\u6435\ud508\u860d\u2ef0\u1d71\uba76\ufe51\u0d0d\u67d7\u4048\u045f\udad1\u97dc\ubabe\ub1ee\u6ca1\ub8b7\ud5e8\u8d9c\u416d\ub6b7\u16c6\u302c\u9cb0\ue9f2\u9ced\uc15b\ud792\u7d35\u117d\ue55d\u6051\u081c\u8f19\u1928\ud57c\u686b\u5027\uf5eb\u7569\u9a19\u7955\u2b7f\u014a\u684d\u2ff5\udd05\ueaf4\u83d3\ufb6c\ucf2b\u19e5\u008e\u50a6\ua42b\ue7f5\u7ed1\u7de0\u0455\u28c8\ub881\u97b6\ucd5b\u174e\u9ba5\ua57c\u8657\udfa8\ufea6\uf41a\u541a\u8cde\u6f1d\ue4cd\uac87\uc2ae\u329e\u650a\u3e86\u0d3f\u5379\ub738\ue68a\u2fa7\ue366\u47e8\u66de\ubf4d\ud3da\u93a0\ue031\u93b4\u6046\ud477\u325f\u340f\ufd6a\u3aed\u231c", -975301048, 0xDA7D000E ^ CryptoVault.mask()) : step.body;
        List<String> list2 = UiKit.wrap(string2, f8 - f7 * 2.0f, f17, 3);
        float f19 = f11 + f7 + 24.0f * f4;
        for (int i2 = 0; i2 < list2.size(); ++i2) {
            builder.text(f10 + f7, f19 + (float)i2 * f18, f17, uiHost.mutedColor(), list2.get(i2));
        }
        float f20 = Math.round(30.0f * f4);
        float f21 = f11 + f9 - f7 - f20;
        float f22 = f10 + f7;
        float f23 = f19 + (float)list2.size() * f18 + 12.0f * f4;
        float f24 = f8 - f7 * 2.0f;
        float f25 = f21 - 14.0f * f4 - f23;
        switch (step.ordinal()) {
            case 0: {
                this.drawThemes(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 1: {
                if (layoutPainter == null) break;
                layoutPainter.paint(builder, f22, f23, f24, Math.min(f25, 250.0f * f4), f4, f5, f6);
                break;
            }
            case 2: {
                this.drawModuleSettings(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 3: {
                this.drawPerformance(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 4: {
                this.drawAccents(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 5: {
                this.drawSsProof(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 6: {
                this.drawCleanCapture(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
                break;
            }
            case 7: {
                this.drawComfort(builder, uiHost, f22, f23, f24, f25, f4, f5, f6);
            }
        }
        float f26 = UiKit.buttonWidth(SetupPicker.yv("\u9a5c\udcf8\u5a13\u1af5\ud8de\u9d84\ufcd8\u9e69\u63d9\u1347\ue4ff\u71b1\ufbb3\u96f4", -859865398, 0xDA7D000E ^ CryptoVault.mask()), f4);
        UiKit.button(builder, uiHost, SetupPicker.yv("\udda0\uc99b\u5a04\udfeb\u67f7\u401e\uafb9\u0fee\u880c\u3f4c\ud3e5\u453f\ue29d\u690d", 360849246, 0xDA7D000E ^ CryptoVault.mask()), f10 + f7, f21, f26, f20, f4, UiKit.Style.SECONDARY, true, f5, f6, this::skipSetup);
        boolean bl = this.index == list.size() - 1;
        boolean bl2 = this.touched[step.ordinal()];
        String string3 = bl ? SetupPicker.yv("\u8257\u4f61\u9f06\u7e2b\ubd08\u6c6a\u482f\u607f", 1713660980, 0xDA7D000E ^ CryptoVault.mask()) : (bl2 ? SetupPicker.yv("\ubd08\u417b\u6d37\u584f\u9f27\u1220\u357c\ud588", -2015988508, 0xDA7D000E ^ CryptoVault.mask()) : SetupPicker.yv("\u72ad\u55d7\ucad1\u8e21\u4897\u5eed", 1351499535, 0xDA7D000E ^ CryptoVault.mask()));
        float f27 = Math.max(UiKit.buttonWidth(SetupPicker.yv("\ud89b\ube11\uc980\u253b\ucb61\u6034\ud930\ub2d2\u86a3\u64ae", 84386507, 0xDA7D000E ^ CryptoVault.mask()), f4), UiKit.buttonWidth(string3, f4)) + (float)Math.round(6.0f * f4);
        float f28 = UiKit.buttonWidth(SetupPicker.yv("\u9e61\u5808\u6992\ucf9d\uea6b\ude40\u8db5", 255984676, 0xDA7D000E ^ CryptoVault.mask()), f4);
        UiKit.button(builder, uiHost, string3, f10 + f8 - f7 - f27, f21, f27, f20, f4, bl || bl2 ? UiKit.Style.PRIMARY : UiKit.Style.GHOST, true, f5, f6, this::next);
        UiKit.button(builder, uiHost, SetupPicker.yv("\u2c9a\u3015\u2ed0\u4a8b\u9530\ucc1e\ua0cf\u426a", 1573306805, 0xDA7D000E ^ CryptoVault.mask()), f10 + f8 - f7 - f27 - (float)Math.round(8.0f * f4) - f28, f21, f28, f20, f4, UiKit.Style.SECONDARY, this.index > 0, f5, f6, this::back);
    }

    private void drawThemes(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        if (HitboxConfigScreen752809.a == null) {
            return;
        }
        List<String> list = HitboxConfigScreen752809.a.getModes();
        float f10 = Math.round(8.0f * f6);
        int n2 = Math.max(2, (int)((f4 + f10) / ((float)Math.round(104.0f * f6) + f10)));
        float f11 = (f4 - f10 * (float)(n2 - 1)) / (float)n2;
        float f12 = Math.round(38.0f * f6);
        float f13 = f12 + (float)Math.round(24.0f * f6);
        int n3 = (list.size() + n2 - 1) / n2;
        float f14 = (float)n3 * (f13 + f10) - f10;
        this.themeMaxScroll = Math.max(0.0f, f14 - f5);
        this.themeScroll = Math.max(0.0f, Math.min(this.themeMaxScroll, this.themeScroll));
        float f15 = Math.round(10.0f * f6 * uiHost.cornerScale());
        float f16 = Math.max(0.0f, f15 - 3.0f * f6);
        builder.clipPush(f2 - 2.0f * f6, f3, f4 + 4.0f * f6, f5, 0.0f);
        uiHost.beginHitClip(f2 - 2.0f * f6, f3, f4 + 4.0f * f6, f5);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            String string = list.get(i2);
            f9 = f2 + (float)(i2 % n2) * (f11 + f10);
            float f17 = f3 + (float)(i2 / n2) * (f13 + f10) - this.themeScroll;
            if (f17 + f13 < f3 || f17 > f3 + f5) continue;
            boolean bl = HitboxConfigScreen752809.a.is(string);
            boolean bl2 = uiHost.hovered(f7, f8, f9, f17, f11, f13) && f8 >= f3 && f8 <= f3 + f5;
            builder.rect(f9, f17, f11, f13, f15, uiHost.controlFill(bl2 ? 38 : 20));
            float f18 = f9 + 4.0f * f6;
            float f19 = f17 + 4.0f * f6;
            float f20 = f11 - 8.0f * f6;
            builder.clipPush(f18, f19, f20, f12, f16);
            UiSurface uiSurface = UiSurface.byName(string);
            if (!uiSurface.isTheme()) {
                builder.material(f18, f19, f20, f12, f16, uiSurface.preview(UiThemeMaterial.FLUX.accent() & 0xFFFFFF));
            } else if (UiThemeMaterial.LIQUID_GLASS.name().equalsIgnoreCase(string)) {
                int n4 = UiThemeMaterial.LIQUID_GLASS.accent() & 0xFFFFFF;
                builder.gradient(f18, f19, f20, f12, f16, n4 | 0x40000000, 0x22FFFFFF, n4 | 0x18000000, 0x10FFFFFF);
            } else {
                builder.material(f18, f19, f20, f12, f16, UiThemeMaterial.preview(string));
            }
            builder.clipPop();
            builder.border(f18, f19, f20, f12, f16, Math.max(1.0f, f6), 0x1AFFFFFF);
            float f21 = 9.0f * f6;
            builder.text(f9 + 8.0f * f6, GpuUiFontMetrics.topInBox(f17 + f12 + 4.0f * f6, f13 - f12 - 4.0f * f6, f21), f21, bl ? uiHost.accent() : uiHost.textColor(), UiKit.fit(string, f11 - 16.0f * f6, f21, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.border(f9, f17, f11, f13, f15, bl ? Math.max(1.5f, 1.6f * f6) : Math.max(1.0f, f6), bl ? uiHost.accent() : (bl2 ? 0x40FFFFFF : 0x1AFFFFFF));
            uiHost.hit(f9, f17, f11, f13, () -> {
                HitboxConfigScreen752809.a.setCurrentMode(string);
                this.touched[Step.THEME.ordinal()] = true;
            });
        }
        uiHost.endHitClip();
        builder.clipPop();
        if (this.themeMaxScroll > 0.0f) {
            float f22 = f5;
            float f23 = Math.max(18.0f * f6, f22 * f5 / (f5 + this.themeMaxScroll));
            f9 = f3 + (f22 - f23) * (this.themeScroll / this.themeMaxScroll);
            builder.rect(f2 + f4 + 2.0f * f6, f9, Math.max(2.0f, 2.5f * f6), f23, 2.0f * f6, 0x40FFFFFF);
        }
    }

    private void drawChoice(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, String string, String string2, String string3, boolean bl, boolean bl2, Runnable runnable) {
        boolean bl3 = bl2 && uiHost.hovered(f7, f8, f2, f3, f4, f5);
        float f9 = uiHost.anim(SetupPicker.yv("\ub455\uc8ad\uc372\u560a\u9169\u2c1b\u2311\uf7f2\ua54f\ufbd0\ub1a0\u3d7a\ua022\ue9ef\u7bb5", -912791372, 0xDA7D000E ^ CryptoVault.mask()) + string, bl ? 1.0f : 0.0f, 18.0f);
        float f10 = Math.round(12.0f * f6 * uiHost.cornerScale());
        builder.rect(f2, f3, f4, f5, f10, bl2 ? uiHost.controlFill(Math.round(18.0f + 12.0f * (bl3 ? 1.0f : 0.0f))) : uiHost.controlFill(8));
        if (f9 > 0.01f) {
            builder.rect(f2, f3, f4, f5, f10, UiKit.withAlpha(uiHost.accent(), Math.round(44.0f * f9)));
            builder.shadow(f2, f3 + 3.0f * f6, f4, f5, f10, 14.0f * f6, 0.0f, UiKit.withAlpha(uiHost.accent(), Math.round(40.0f * f9)));
        }
        builder.border(f2, f3, f4, f5, f10, f9 > 0.5f ? Math.max(1.5f, 1.6f * f6) : Math.max(1.0f, f6), f9 > 0.5f ? uiHost.accent() : (bl3 ? 0x40FFFFFF : 0x1AFFFFFF));
        float f11 = 12.0f * f6;
        int n2 = !bl2 ? uiHost.muted2Color() : (bl ? uiHost.accent() : uiHost.textColor());
        builder.text(f2 + 14.0f * f6, f3 + 12.0f * f6, f11, n2, UiKit.fit(string2, f4 - 50.0f * f6, f11, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        float f12 = 8.0f * f6;
        float f13 = f2 + f4 - 16.0f * f6;
        float f14 = f3 + 12.0f * f6 + f11 * 0.5f;
        builder.circle(f13, f14, f12, uiHost.controlFill(48));
        builder.circle(f13, f14, f12 * 0.62f, UiKit.panelOpaque());
        if (f9 > 0.01f) {
            builder.circle(f13, f14, f12 * 0.42f * f9, uiHost.accent());
        }
        List<String> list = UiKit.wrap(string3, f4 - 28.0f * f6, 9.5f * f6, 3);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            builder.text(f2 + 14.0f * f6, f3 + 32.0f * f6 + (float)i2 * 13.0f * f6, 9.5f * f6, bl2 ? uiHost.mutedColor() : uiHost.muted2Color(), list.get(i2));
        }
        if (bl2) {
            uiHost.hit(f2, f3, f4, f5, runnable);
        }
    }

    private void drawOnOff(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, Step step, String string, boolean bl, boolean bl2, String string2, String string3, Consumer<Boolean> consumer) {
        float f8 = Math.round(10.0f * f5);
        float f9 = (f4 - f8) * 0.5f;
        float f10 = Math.round(76.0f * f5);
        this.drawChoice(builder, uiHost, f2, f3, f9, f10, f5, f6, f7, string + SetupPicker.yv("\u5c7b\u6523\u87e6\ueeca", -1438524795, 0xDA7D000E ^ CryptoVault.mask()), "On", string2, bl, bl2, () -> {
            consumer.accept(true);
            this.touched[step.ordinal()] = true;
        });
        this.drawChoice(builder, uiHost, f2 + f9 + f8, f3, f9, f10, f5, f6, f7, string + SetupPicker.yv("\ua013\ub41c\ud808\u903d\u221a\u02c0\uf726", 1565481128, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u8e65\u8017\u9e71\u444b\ube2b\uea3e\u8ecd", -1456882136, 0xDA7D000E ^ CryptoVault.mask()), string3, !bl, bl2, () -> {
            consumer.accept(false);
            this.touched[step.ordinal()] = true;
        });
    }

    private static boolean dropdownSettingsStep() {
        return SetupPicker.yv("\u8400\ue47c\ua8a3\udb85\u2ce7\u2708\uca6a\ud67c\uab1e\u84ca\u0e66\u0d52", 1772663607, 0xDA7D000E ^ CryptoVault.mask()).equalsIgnoreCase(SetupPicker.currentLayout()) && HitboxConfigScreen752809.be != null;
    }

    private void drawModuleSettings(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        String[][] stringArrayArray;
        ModeSetting modeSetting;
        boolean bl = SetupPicker.dropdownSettingsStep();
        ModeSetting modeSetting2 = modeSetting = bl ? HitboxConfigScreen752809.be : HitboxConfigScreen752809.m;
        if (bl) {
            String[][] stringArrayArray2 = new String[2][];
            stringArrayArray2[0] = new String[]{SetupPicker.yv("\u0d75\ue0cd\u156e\u861e\ub116\u1eae\ufba1\ud4d6", -1762047237, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\uce6d\u534e\ue586\u7f2b\u5773\u3a97\uc130\u5014\ue53b\ua605\u2506\ubebc\uc567\uf298\uec8a\ud2c9\u0db9\u7ed9\u89ab\u92f2\u6825\u96a2\u59ca\uae6a\u1dbe\u49f0\ue322\ud255\u14a1", -1673329910, 0xDA7D000E ^ CryptoVault.mask())};
            stringArrayArray = stringArrayArray2;
            stringArrayArray2[1] = new String[]{SetupPicker.yv("\u7496\udb23\ua93f\u637c\uc93f\ua0b0\u5fab\u2e93\ufbf0\uc4ac", -858728694, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u6347\u5975\u865b\ud27e\u826f\ua986\u9431\u6b8e\ufa30\ub9d1\u6aeb\u6902\u5bf1\u9539\u015c\ua0dd\u5d15\u09aa\ud06a\u4038\ued56\ucf9e\u598d\u9e45\u2cf1\u0d53\u46e6", -1417817054, 0xDA7D000E ^ CryptoVault.mask())};
        } else {
            String[][] stringArrayArray3 = new String[3][];
            stringArrayArray3[0] = new String[]{SetupPicker.yv("\u81fe\uf8bc\u0ead\u5512\u2f03\ub43a\ua308\u836a\u076d\u80b2\ue568\ue35e\u962d\u6e19", 540534157, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u8e44\u0fde\u11c0\ue2af\u4048\u27d5\ub5da\u0aef\uc7cb\u6659\uca35\u2093\ufbac\uff11\ub490\u0938\udf1c\u146b\u9b73\u9810\uf0f0\u2c46\ufce8\u8017\udeda\u79e4\ue1df", -756243465, 0xDA7D000E ^ CryptoVault.mask())};
            stringArrayArray3[1] = new String[]{SetupPicker.yv("\u5b90\u0476\u2801\u1377\ucfde\uf5cc\ub33c\u3e29\uabea\uaf90\ub86e\uef8f", -297571176, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u6b4c\ue3f6\u7322\u33ff\u0150\ua474\u417c\u792c\u8cdb\udb89\ue4bc\uf24b\uf43d\u21b0\ue15b\u55f4\ub006\uab2b\uf6a2\uada5\u8dc1\uc666\uc192\u81af\u0472", -405155950, 0xDA7D000E ^ CryptoVault.mask())};
            stringArrayArray = stringArrayArray3;
            stringArrayArray3[2] = new String[]{SetupPicker.yv("\uc0a2\ubf15\u049a\uaf93\u81d8\u3a01\uec0e\u140a\ucf1e\u78df\uc434\ua0d1\u5501", -1913888257, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\uc3d7\u706c\u9bd4\uaf9f\uc439\u3689\u799f\u93ed\u58f7\u8970\uf807\u0976\uf6e1\u5059\uc5aa\uc2d1\u8f15\uf963\u6cc1\u6e9f\u2de2\u117c\u49ff\uf417\uae3f\u638c\u7505\u0779\u04d9\udb7d\ub65f\u759c", 1360892847, 0xDA7D000E ^ CryptoVault.mask())};
        }
        String[][] stringArrayArray4 = stringArrayArray;
        int n2 = stringArrayArray4.length;
        float f9 = Math.round(10.0f * f6);
        float f10 = (f4 - f9 * (float)(n2 - 1)) / (float)n2;
        float f11 = Math.round(Math.min(110.0f * f6, Math.max(60.0f * f6, f5 - 52.0f * f6)));
        float f12 = f11 + (float)Math.round(46.0f * f6);
        float f13 = Math.round(12.0f * f6 * uiHost.cornerScale());
        float f14 = Math.max(0.0f, f13 - 4.0f * f6);
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArrayArray4[i2][0];
            float f15 = f2 + (float)i2 * (f10 + f9);
            boolean bl2 = modeSetting.is(string);
            boolean bl3 = uiHost.hovered(f7, f8, f15, f3, f10, f12);
            float f16 = uiHost.anim((bl ? SetupPicker.yv("\ua228\uf2ea\u3d9d\u8cb0\u52dc\uedb5\u8431\u15bc\u9eb6\u4f94\u8837\u9fcc\ub391\u33f1\uf8ca\u9539\uf2ab\u5774\ucd2d", -422500335, 0xDA7D000E ^ CryptoVault.mask()) : SetupPicker.yv("\u2121\uce92\u3295\uaa10\ufd9f\u3dae\u6e0d\u17a6\u552f\uf12d\u2f64\uc36e\ud30a\u8351\ud408\ub44a\u4714\ue887", 2113157562, 0xDA7D000E ^ CryptoVault.mask())) + i2, bl2 ? 1.0f : 0.0f, 18.0f);
            builder.rect(f15, f3, f10, f12, f13, uiHost.controlFill(bl3 ? 30 : 18));
            if (f16 > 0.01f) {
                builder.rect(f15, f3, f10, f12, f13, UiKit.withAlpha(uiHost.accent(), Math.round(44.0f * f16)));
                builder.shadow(f15, f3 + 3.0f * f6, f10, f12, f13, 14.0f * f6, 0.0f, UiKit.withAlpha(uiHost.accent(), Math.round(40.0f * f16)));
            }
            float f17 = f15 + 6.0f * f6;
            float f18 = f3 + 6.0f * f6;
            float f19 = f10 - 12.0f * f6;
            builder.clipPush(f17, f18, f19, f11, f14);
            if (bl) {
                this.drawDropdownPreview(builder, uiHost, string, f17, f18, f19, f11, f6);
            } else {
                this.drawSettingsPreview(builder, uiHost, string, f17, f18, f19, f11, f6);
            }
            builder.clipPop();
            builder.border(f17, f18, f19, f11, f14, Math.max(1.0f, f6), 0x1AFFFFFF);
            float f20 = 11.0f * f6;
            builder.text(f15 + 10.0f * f6, f18 + f11 + 8.0f * f6, f20, bl2 ? uiHost.accent() : uiHost.textColor(), UiKit.fit(string, f10 - 20.0f * f6, f20, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f15 + 10.0f * f6, f18 + f11 + 24.0f * f6, 9.0f * f6, uiHost.mutedColor(), UiKit.fit(stringArrayArray4[i2][1], f10 - 20.0f * f6, 9.0f * f6));
            builder.border(f15, f3, f10, f12, f13, f16 > 0.5f ? Math.max(1.5f, 1.6f * f6) : Math.max(1.0f, f6), f16 > 0.5f ? uiHost.accent() : (bl3 ? 0x40FFFFFF : 0x1AFFFFFF));
            uiHost.hit(f15, f3, f10, f12, () -> {
                modeSetting.setCurrentMode(string);
                this.touched[Step.MODULE_SETTINGS.ordinal()] = true;
            });
        }
    }

    private void drawSettingsPreview(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4, float f5, float f6) {
        int n2;
        int n3 = uiHost.accent();
        builder.rect(f2, f3, f4, f5, 0.0f, UiKit.panelOpaque());
        float f7 = Math.round(f4 * 0.16f);
        builder.rect(f2, f3, f7, f5, 0.0f, 0x14FFFFFF);
        for (n2 = 0; n2 < 4; ++n2) {
            builder.rect(f2 + f7 * 0.25f, f3 + 8.0f * f6 + (float)n2 * 9.0f * f6, f7 * 0.5f, 3.0f * f6, 1.5f * f6, n2 == 0 ? UiKit.withAlpha(n3, 192) : 0x30FFFFFF);
        }
        n2 = SetupPicker.yv("\ub663\u0b8e\u0bcf\u444e\ufa1b\u7c6b\ub0ed\u3409\ue329\u16de\ub2fc", 349465259, 0xDA7D000E ^ CryptoVault.mask()).equals(string) ? 1 : 0;
        boolean bl = SetupPicker.yv("\uedcf\u5b99\u2520\u0c63\u8674\u4731\ub33d\uf1b7\uc2e1\ua049\u6062\u7ad1\ua688", 1115169172, 0xDA7D000E ^ CryptoVault.mask()).equals(string);
        float f8 = f2 + f7 + 5.0f * f6;
        float f9 = (n2 != 0 ? f2 + f4 * 0.62f : f2 + f4) - f8 - 5.0f * f6;
        float f10 = Math.round(Math.max(8.0f * f6, f5 * 0.13f));
        float f11 = 4.0f * f6;
        float f12 = f3 + 6.0f * f6;
        for (int i2 = 0; i2 < 6 && f12 + f10 <= f3 + f5 - 3.0f * f6; ++i2) {
            boolean bl2 = i2 == 1;
            builder.rect(f8, f12, f9, f10, 3.0f * f6, bl2 && !SetupPicker.yv("\u1b3a\u2ae6\u9452\uf4b9\u18e2\ub832\ua3bd\u9095\u1e40", 1580712903, 0xDA7D000E ^ CryptoVault.mask()).equals(string) ? UiKit.withAlpha(n3, 64) : 0x1CFFFFFF);
            builder.rect(f8 + 4.0f * f6, f12 + f10 * 0.5f - 1.5f * f6, f9 * 0.4f, 3.0f * f6, 1.5f * f6, 0x40FFFFFF);
            f12 += f10 + f11;
            if (!bl2 || !bl) continue;
            f12 = SetupPicker.drawExpandedPreview(builder, n3, f8, f12, f9, f10, f11, f6);
        }
        if (n2 != 0) {
            float f13 = f2 + f4 * 0.62f;
            float f14 = f2 + f4 - f13;
            builder.rect(f13, f3, f14, f5, 0.0f, UiKit.brighten(UiKit.panelOpaque(), 0.04f));
            builder.rect(f13, f3, Math.max(1.0f, f6), f5, 0.0f, 0x26FFFFFF);
            builder.rect(f13 + 6.0f * f6, f3 + 8.0f * f6, f14 * 0.55f, 4.0f * f6, 2.0f * f6, UiKit.withAlpha(n3, 192));
            for (int i3 = 0; i3 < 4; ++i3) {
                builder.rect(f13 + 6.0f * f6, f3 + 20.0f * f6 + (float)i3 * 11.0f * f6, f14 - 12.0f * f6, 6.0f * f6, 3.0f * f6, 0x1CFFFFFF);
            }
        } else if (!bl) {
            SetupPicker.drawFloatPreview(builder, n3, f2, f3, f4, f5, f6);
        }
    }

    private void drawDropdownPreview(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4, float f5, float f6) {
        int n2 = uiHost.accent();
        boolean bl = !SetupPicker.yv("\u4065\u3f84\u5445\u8794\u29e1\u38b2\u9063\uc762\uf9b4\u42c5", 614720276, 0xDA7D000E ^ CryptoVault.mask()).equals(string);
        builder.rect(f2, f3, f4, f5, 0.0f, UiKit.panelOpaque());
        float f7 = 5.0f * f6;
        float f8 = (f4 - f7 * 4.0f) / 3.0f;
        float f9 = Math.round(Math.max(7.0f * f6, f5 * 0.11f));
        float f10 = 3.0f * f6;
        for (int i2 = 0; i2 < 3; ++i2) {
            float f11 = f2 + f7 + (float)i2 * (f8 + f7);
            float f12 = f3 + 6.0f * f6;
            float f13 = Math.round(f9 * 1.3f);
            builder.rect(f11, f12, f8, f5 - 12.0f * f6, 4.0f * f6, 0x12FFFFFF);
            builder.rect(f11 + 4.0f * f6, f12 + f13 * 0.5f - 1.5f * f6, f8 * 0.5f, 3.0f * f6, 1.5f * f6, i2 == 1 ? UiKit.withAlpha(n2, 192) : 0x40FFFFFF);
            float f14 = f12 + f13 + 3.0f * f6;
            for (int i3 = 0; i3 < 5 && f14 + f9 <= f12 + f5 - 15.0f * f6; ++i3) {
                boolean bl2 = i2 == 1 && i3 == 1;
                builder.rect(f11 + 3.0f * f6, f14, f8 - 6.0f * f6, f9, 2.5f * f6, bl2 && bl ? UiKit.withAlpha(n2, 64) : 0x1CFFFFFF);
                f14 += f9 + f10;
                if (!bl2 || !bl) continue;
                f14 = SetupPicker.drawExpandedPreview(builder, n2, f11 + 3.0f * f6, f14, f8 - 6.0f * f6, f9, f10, f6);
            }
        }
        if (!bl) {
            SetupPicker.drawFloatPreview(builder, n2, f2, f3, f4, f5, f6);
        }
    }

    private static float drawExpandedPreview(UiDisplayList.Builder builder, int n2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = Math.round(f5 * 2.6f);
        builder.rect(f2 + 4.0f * f7, f3 - f6 + 1.0f * f7, f4 - 4.0f * f7, f8, 3.0f * f7, 0x10FFFFFF);
        builder.rect(f2 + 4.0f * f7, f3 - f6 + 1.0f * f7, 2.0f * f7, f8, 1.0f * f7, UiKit.withAlpha(n2, 144));
        for (int i2 = 0; i2 < 3; ++i2) {
            builder.rect(f2 + 10.0f * f7, f3 + 2.0f * f7 + (float)i2 * f8 / 3.2f, (f4 - 18.0f * f7) * (i2 == 1 ? 0.55f : 0.8f), 2.5f * f7, 1.25f * f7, 0x38FFFFFF);
        }
        return f3 + f8 + f6;
    }

    private static void drawFloatPreview(UiDisplayList.Builder builder, int n2, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        builder.rect(f2, f3, f4, f5, 0.0f, -1728053248);
        float f8 = f4 * 0.56f;
        float f9 = f5 * 0.66f;
        float f10 = f2 + (f4 - f8) * 0.5f;
        float f11 = f3 + (f5 - f9) * 0.5f;
        builder.shadow(f10, f11 + 2.0f * f6, f8, f9, 5.0f * f6, 10.0f * f6, 0.0f, -1879048192);
        builder.rect(f10, f11, f8, f9, 5.0f * f6, UiKit.brighten(UiKit.panelOpaque(), 0.05f));
        builder.border(f10, f11, f8, f9, 5.0f * f6, Math.max(1.0f, f6), UiKit.withAlpha(n2, 112));
        builder.rect(f10 + 6.0f * f6, f11 + 7.0f * f6, f8 * 0.45f, 4.0f * f6, 2.0f * f6, UiKit.withAlpha(n2, 192));
        for (int i2 = 0; i2 < 3 && !((f7 = f11 + 18.0f * f6 + (float)i2 * 10.0f * f6) + 6.0f * f6 > f11 + f9 - 3.0f * f6); ++i2) {
            builder.rect(f10 + 6.0f * f6, f7, f8 - 12.0f * f6, 6.0f * f6, 3.0f * f6, 0x1CFFFFFF);
        }
        float f12 = 3.0f * f6;
        builder.circle(f10 + f8 - 6.0f * f6 - f12, f11 + 9.0f * f6, f12, 0x30FFFFFF);
    }

    private void drawPerformance(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (HitboxConfigScreen752809.bc == null) {
            return;
        }
        boolean bl = HitboxConfigScreen752809.bc.isEnabled();
        float f9 = Math.round(10.0f * f6);
        float f10 = (f4 - f9) * 0.5f;
        float f11 = Math.round(84.0f * f6);
        this.drawChoice(builder, uiHost, f2, f3, f10, f11, f6, f7, f8, SetupPicker.yv("\u62dd\u9d58\u0757\u7491\ua09e\u4662\ue90d\ua0b1\uefb0\uf306\uca38", 1765839351, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\uffb6\u176b\u24ce\u5d1c\u6d48\uabb8\u3503\u4b99", -1532001644, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\uf1e7\u7924\u5774\ubd8b\u8da8\uf93a\ue698\uf4b0\uce05\ud707\u0416\u1d91\u5cc1\u4887\u4703\ua06a\ua95d\u33ba\ub361\u0271\u5a7c\u365b\uaef0\ua04a\u06ec\udd28\u324b\u6060\ue40d\ufd8c\udeca\u4e97\u11ff\uf05d\ua878\ucfc0\u84e9\u1120\ua2c9\ud698\u3084\ue56e\ubb5c\ubf7d\u969c\u3571\ucab2\u441d\ufa72\ue57b\u811f\u4d5e\uef37\u3c82\ube70\uca50\u52cd\u2f31\ucb4c\u822a\uf1e2\u2f87\u2d2f\u172a\u38b0\uc08a\ua760\u7f1c\u3260\uf8eb\udf50\u4eb5\ubc7a\ue3f1\u988b\uf075\u87b4\u3eb4\ueeb7\ub38c\u2172\u3ad3\ue588\u4870\ub9e0\u6a24\u2283\uc043\ucad6\u5e35\u430f\ub508\u73a5\u19ba", -453160577, 0xDA7D000E ^ CryptoVault.mask()), !bl, true, () -> {
            HitboxConfigScreen752809.bc.setEnabled(false);
            this.touched[Step.PERFORMANCE.ordinal()] = true;
        });
        this.drawChoice(builder, uiHost, f2 + f10 + f9, f3, f10, f11, f6, f7, f8, SetupPicker.yv("\u0cd9\ua70f\u048a\u026b\u3f5f\u0db1\u3baa\u89c3\u7266\ubec1\u7728\u6cb0", -76837656, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u1b52\u16ac\ue352\u4239\u35db\u94bd\u0792", 1813225311, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u08e1\udbb1\u321d\ud0d8\u9d03\u689c\u32ae\ud0cf\u2cba\uffd0\ua690\ua4f4\ubfcf\u19e3\u2c03\ud869\u1362\u45b0\u4c86\ub082\ue7d3\u7681\u334c\u95cb\u9063\u34ee\ubb95\ucfeb\u22a7\ubd30\u8536\ueb00\u4f3f\udbb8\u25e7\u3030\uc4df\u4161\ud77b\u34f5\ufa74\ud785\u49f8\u1c15\ud278\ub15e\u8131\ub400\ubeeb\u0a1e\u775b\u4301\u5445\u7bc8\u41ff\uce6f\u6cb7\uc509\u8559\u0ce4\u78d2\ua8f7\u2487\ue8fa\u4ff4\u4c21\ub61b\u172c\u47f2\ubca5\ud6cb\u8f00\u99c6\u6253\ucb72\u407f\u93a9\ubf9e\u14d7\ub68c\u3cd6\u8bc0o\udefd\ub3c3\u9a21\u5df0\u7dec\u36ab\uada9", -491864889, 0xDA7D000E ^ CryptoVault.mask()), bl, true, () -> {
            HitboxConfigScreen752809.bc.setEnabled(true);
            this.touched[Step.PERFORMANCE.ordinal()] = true;
        });
        float f12 = f3 + f11 + 14.0f * f6;
        if (f12 + 12.0f * f6 < f3 + f5) {
            builder.text(f2, f12, 9.5f * f6, uiHost.muted2Color(), SetupPicker.yv("\u5413\u2b0a\uda16\u23d2\u71d1\u78c9\u7b61\uf618\uebe6\uae03a\u9039\u7ca0\u2792\ud370\ub3c8\uea19\u10fa\uc98d\u0387\u7ac8\u63e0\ucb75\u7422\ub363\u6789\u699f\ueb9d\ufe0c\ub455\u46cb\u50c3\u00fe\uf784\uca0e\u2aae\uc574\u78b1\u47df\u7386\u5624\u216f\ue1d4\u79cf\u086d\ub433\u4280\u7804\uee16\uc48c\u68eb\u1f90\uc07e\ubb36\uf710\u5675\ua153\u0ee4\u5b9f\u94b8\u5941\u268b\uacf8\u094f\u00f4\uc830\u46bb\u1698\uf14b\ub444\u6cc4\ue803\u2034\u67de\u8f1c\u2408\u8d19\ua81d\u5747\ud453\u3827\u4c61\u6cbf\uff7c\u451e\uad60\u82c4\u44f3\u51a6\u8034\u8ba8\u1a81\ud790\u5b6b\ubaa5\u5541\u49f4\u0be9\u5fc6\u9c08\ucac6\udaf2\u6bf4\u5cf5\u6aae\ue9d9\u99cb\u4ddf\u75d0", -2096238867, 0xDA7D000E ^ CryptoVault.mask()));
        }
    }

    private void drawAccents(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        if (HitboxConfigScreen752809.l == null) {
            return;
        }
        boolean bl2 = HitboxConfigScreen752809.l.isEnabled();
        if (bl2 && this.editingAccent != null) {
            this.drawAccentEditor(builder, uiHost, f2, f3, f4, f6, f7, f8);
            return;
        }
        this.drawOnOff(builder, uiHost, f2, f3, f4, f6, f7, f8, Step.ACCENTS, SetupPicker.yv("\u3012\u9185\uc0d4\uc67e\u4f67\ua4d2\uf058\u9ccd", 1623878552, 0xDA7D000E ^ CryptoVault.mask()), bl2, true, SetupPicker.yv("\uf68d\ucca0\u828f\ubeef\u6020\uf215\ube5a\u4a0c\ua0b5\u1757\u1b30\u38fe\u72a2\u8b69\u651c\u707c\u60c4\u6017\uc69e\u512c\u0e4d\u15e3\u629f\uaa11\ua4ca\u473b\u9dd2\u2c57\u3222\u7363\uc3f1\u8e17\ud894\u1fe7\u8333\u2131\ua452\u5732\u2af6\ud4d5\ua83c\u52f8\u4192\u28b0\u5ff7\u23e3\u2493\u841d\ua09d\u814a", 751207045, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\uadad\u8478\ua215\uc21d\u4c4d\u2aa6\uc928\u40f7\ufd1a\u3b3d\ud0f9\uf6ae\ucc5b\u5e10\ufc5b\u670b\u7e7b\u189b\uf6ac\uf086\u9abc\u845e\udf00\uc6dc\u8155\uc2fa\u3fde\u864d\ua105\u94d4\u66f4\ue4de\u289c\udddd\uee30\u419d\ua44c\u6a2a\ub7c1\u6a5d\u8769\u2bb4\u3865\u365b\uaa2e\uc64e\ubefc\uafad\ud148\u6ff9\ub985\udf57\u9086\u402f\uc436\u6a62\uc280", 1464909008, 0xDA7D000E ^ CryptoVault.mask()), bl -> HitboxConfigScreen752809.l.setEnabled((boolean)bl));
        float f10 = f3 + (float)Math.round(76.0f * f6) + 16.0f * f6;
        Category[] categoryArray = new Category[]{Category.CE, Category.lr, Category.Mr, Category.yZ, Category.Be, Category.ve, Category.rr, Category.fK, Category.vR};
        float f11 = Math.round(6.0f * f6);
        int n2 = f4 > 380.0f * f6 ? 4 : 2;
        float f12 = (f4 - f11 * (float)(n2 - 1)) / (float)n2;
        float f13 = Math.round(26.0f * f6);
        for (int i2 = 0; i2 < categoryArray.length; ++i2) {
            boolean bl3;
            float f14 = f2 + (float)(i2 % n2) * (f12 + f11);
            float f15 = f10 + (float)(i2 / n2) * (f13 + f11);
            if (f15 + f13 > f3 + f5) break;
            int n3 = HitboxConfigScreen752809.b(categoryArray[i2]);
            boolean bl4 = n3 >= 0;
            float f16 = uiHost.anim(SetupPicker.yv("\u1f94\uad05\u961a\ub328\ud29d\uc74e\ub20f\u567c\uedc2\ua0fd\ueda3\ua1ef\u0d2d", 789619436, 0xDA7D000E ^ CryptoVault.mask()) + i2, bl4 ? 1.0f : 0.0f, 12.0f);
            int n4 = bl4 ? n3 | 0xFF000000 : uiHost.accent();
            builder.rect(f14, f15, f12, f13, f13 * 0.5f, uiHost.controlFill(20));
            if (bl4) {
                builder.rect(f14, f15, f12, f13, f13 * 0.5f, UiKit.withAlpha(n4, Math.round(36.0f * f16)));
            }
            float f17 = 4.5f * f6;
            if (bl4) {
                builder.shadow(f14 + 8.0f * f6, f15 + f13 * 0.5f - f17, f17 * 2.0f, f17 * 2.0f, f17, 8.0f * f6, 0.0f, UiKit.withAlpha(n4, Math.round(112.0f * f16)));
            }
            builder.circle(f14 + 8.0f * f6 + f17, f15 + f13 * 0.5f, f17, bl4 ? n4 : uiHost.muted2Color());
            float f18 = 9.5f * f6;
            builder.text(f14 + 8.0f * f6 + f17 * 2.0f + 7.0f * f6, GpuUiFontMetrics.topInBox(f15, f13, f18), f18, bl4 ? uiHost.textColor() : uiHost.mutedColor(), UiKit.fit(categoryArray[i2].zw, f12 - 30.0f * f6, f18, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            if (!bl4) continue;
            boolean bl5 = bl3 = f7 >= f14 && f7 < f14 + f12 && f8 >= f15 && f8 < f15 + f13;
            if (bl3) {
                builder.border(f14, f15, f12, f13, f13 * 0.5f, Math.max(1.0f, f6), UiKit.withAlpha(n4, 160));
            }
            Category category = categoryArray[i2];
            uiHost.hit(f14, f15, f12, f13, () -> {
                this.editingAccent = category;
                this.accentPicker.reset();
            });
        }
        if ((f9 = f10 + 2.0f * (f13 + f11) + (n2 == 2 ? 2.0f * (f13 + f11) : 0.0f) + 4.0f * f6) + 12.0f * f6 < f3 + f5) {
            builder.text(f2, f9, 9.5f * f6, uiHost.muted2Color(), bl2 ? SetupPicker.yv("\uec6b\u9fa9\u3ee6\ud95c\u342a\ub808\u3770\ufaac\u72dc\ubf14\ub759\ubd60\ua6c5\u8e14\ua457\u4b78\ud034\uaf76\ub312\u00d7\u15bd\u92b9\ue7f3\uebde\u2559\u11d1\u5cc3\u4f03\u1482\uf3c6\uff2e\u0169\u991e\u2a14\u9133\ud97b\u9c49\u3785\uf6fa\u7b2d\ud084\u14f4\ue3da\ue639\u1cc3\u0b96\u0fc3\u8322\u96fe\u981a\uf7b3\u241c\u8fd1\u7dc7\u7267\u99d1\u50e9\uacff\u6b05\u1a14\u4a6c\ud31c\uf250\u86b4\u77d6\u5d9f\ua4bc\u3764\u252d\u7870\u9f9a\u87e5\u2cb7\uee3d\ucf81\u6bac\u1365\u79f9\uc3d5", 436803801, 0xDA7D000E ^ CryptoVault.mask()) : SetupPicker.yv("\u5c7d\udcc7\ua057\uf8f6\u7cd5\u1a27\ua1da\u53a0\u2247\ua3a3\u5eda\u3ab0\u1739\uad0c\ua6e9\u1ea2\u4d88\uda29\u62d2\u4c92\ud29b\u584d\uc657\u167f\u2983\u70ef\u68da\ub49e\u5fb9\u49a5\ua6b1\ua4b9\u8c82\u62ea\u8b16\u5d99\uf890\uad17\u72e1\uafac\uad0c\u87b1\u8c57\ubafe\u4f5b\u8fe2\ub38c\udf04\uf351\u6639\ub013\u8c4f\udf71\u01a6\u14a3\ucebb\u669f\u3e1d\u4c53\uc523\u914f\uf5cf\u8ef7\u017d\u2196\u6890\ua60b\u1c0c\uf9c1\ucef7\ufd7b", 359120668, 0xDA7D000E ^ CryptoVault.mask()));
        }
    }

    private void drawAccentEditor(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7) {
        ColorSetting colorSetting = HitboxConfigScreen752809.a(this.editingAccent);
        if (colorSetting == null) {
            this.editingAccent = null;
            return;
        }
        float f8 = Math.round(26.0f * f5);
        String string = SetupPicker.yv("\u69aa\u1fed\uba8c\ude53\u1b59", 1112842413, 0xDA7D000E ^ CryptoVault.mask());
        float f9 = UiKit.buttonWidth(string, f5);
        float f10 = 12.0f * f5;
        builder.text(f2, GpuUiFontMetrics.topInBox(f3, f8, f10), f10, uiHost.textColor(), UiKit.fit(this.editingAccent.zw + SetupPicker.yv("\u88b2\uceab\u46b8\u66e9\u0b58\u4f65\uda29", -763461932, 0xDA7D000E ^ CryptoVault.mask()), f4 - f9 - 12.0f * f5, f10, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        UiKit.button(builder, uiHost, string, f2 + f4 - f9, f3, f9, f8, f5, UiKit.Style.PRIMARY, true, f6, f7, () -> {
            this.editingAccent = null;
            this.accentPicker.reset();
        });
        int n3 = colorSetting.A().getRGB() | 0xFF000000;
        this.accentPicker.draw(builder, uiHost, f2, f3 + f8 + (float)Math.round(12.0f * f5), f4, f5, f6, f7, n3, false, n2 -> {
            colorSetting.A(new Color(n2 & 0xFFFFFF));
            this.touched[Step.ACCENTS.ordinal()] = true;
        });
    }

    public boolean mouseDragged(double d2, double d3) {
        return this.active && this.accentPicker.drag(d2, d3);
    }

    public void mouseReleased() {
        this.accentPicker.release();
    }

    private void drawSsProof(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (HitboxConfigScreen752809.as == null) {
            return;
        }
        String string = HitboxConfigScreen752809.l();
        boolean bl2 = string == null;
        boolean bl3 = bl2 && HitboxConfigScreen752809.as.isEnabled();
        this.drawOnOff(builder, uiHost, f2, f3, f4, f6, f7, f8, Step.SS_PROOF, SetupPicker.yv("\u966e\ua9e6\u2845\uf791\u5313\u4815\uf74a\ufa87", -797282924, 0xDA7D000E ^ CryptoVault.mask()), bl3, bl2, SetupPicker.yv("\u6f4c\ua783\u2a44\u6f00\ub9a8\ua8cf\ua54d\u307c\uc130\u9f5e\ue731\u8108\ufa42\u8df8\ub12d\uc658\uf112\u49f9\u5485\u9b21\u81d7\uaa60\u97c7\ucbf6\u4c21\u0d97\u1c69\u5702\u42e3\u1ffd\ub59a\ub598\ud2c8\u3069\ub792\u8159\ue121\u6802\u3396\ue7cb\u47ff\u5c78\uf163", 114613870, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\u055e\u703a\ud874\u064d\u23a9\u420b\ufac0\u88df\u90d3\ua6bb\u1a04\ub54a\u5300\ua6f0\u6621\u576b\ud8fc\u36f3\u3a5b\ufb5c\u854a\ua4f8\u137c\u928f\u3dbe\u84c6\u5a46\u5f26\u681e\u12d8\uc8f8\uef28\u6bfc\u7366\ue333\uf6f5\ua6e6\udba0\u055a\u4cee\u2624\u8645\u8b3a\u3f37\u5556\ub258\uc73c\u7cee\u236f\u8ace\ub85c\u6cdd\uc84a", -1885097491, 0xDA7D000E ^ CryptoVault.mask()), bl -> HitboxConfigScreen752809.as.setEnabled((boolean)bl));
        float f9 = f3 + (float)Math.round(76.0f * f6) + 14.0f * f6;
        if (!bl2) {
            float f10 = 10.0f * f6;
            builder.text(f2, f9, f10, -19641, UiKit.fit(SetupPicker.yv("\u1b76\u9ba8\u0d83\uaf0b\u0c9f\uf4d9\u86fb\u3700\u966c\u3804\ud4d0\uea2d\u0276\ubcde\u8899\ub5b7\u17a3\u8592\u9468\uc15d", 365033557, 0xDA7D000E ^ CryptoVault.mask()) + string, f4, f10, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2, f9 + 16.0f * f6, 9.5f * f6, uiHost.muted2Color(), SetupPicker.yv("\u50e3\u2780\u92b6\ue812\u1142\u2a8d\u871c\u2760\u3edf\udcec\u1156\u90d9\udb9a\u0261\u3c1d\ua47c\ud996\u87ba\udf4e\ua537\ud00d\u37f3\u4038\uae90\ud915\ub8ad\ud5b1\uadce\u55b7\ua601\u7478\uc46f\u3346\u23fb\ucc45\u789f\ue92d\uaa04\u0a83\u8fec\u6ba4\uefc7\u7ec6\uf492\u1077\ube61", -370603578, 0xDA7D000E ^ CryptoVault.mask()));
        } else if (bl3) {
            builder.text(f2, f9, 9.5f * f6, uiHost.muted2Color(), SetupPicker.yv("\uebc1\ud4e4\u6ba3\u8f9c\u68eb\udd84\u937e\ub8aa\u08f4\ua69a\uc4a2\u5e95\u1acf\u3c44\u98c8\uaf06\u1629\u1773\u5fb2\ub6aa\ufff4\u1962\u63bf\u068c\ud8e3\u40db\ubd5f\uc939\u86e5\ue19f\ufb12\u2280\u49ab\ua9d6\u2310\uf7ec\ufa24\u1079\u717b\u0e10\u0adc\u62b7\u5aaf\uf61b\u2d71\ue410\u9715\u963b\ud411\ub7ab\u6438\ue754\uc70f\u7189\u88cc\u1d0e\ub362\u9ba3\u7006\udd14\u76b1\u7dc5\uf290\ue07f\u8803\ubcb1\uf977\uce88\uf837\u9c3b\ua594", -593501988, 0xDA7D000E ^ CryptoVault.mask()));
        }
    }

    private void drawCleanCapture(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (HitboxConfigScreen752809.au == null) {
            return;
        }
        boolean bl2 = HitboxConfigScreen752809.au.isEnabled();
        this.drawOnOff(builder, uiHost, f2, f3, f4, f6, f7, f8, Step.CLEAN_CAPTURE, SetupPicker.yv("\u7bb3\u80bb\uf6b9\ua148\ub4aa\u9770\u92c7\ub245\u815e\uc56f\u8a9e\u234b\u9ef0\u21ca\u7c3a", -417732422, 0xDA7D000E ^ CryptoVault.mask()), bl2, true, SetupPicker.yv("\u6a51\uf60a\u7c57\u89d6\udb6d\u622e\ued6f\u35f5\u32a4\u858e\uaf15m\u9c02\u01e1\ube48\u0c23\u0a0e\uda91\u7dcf\uecbf\ufee9\ud6e4\ub335\u1d1e\u3dd0\ufaaf\u8b8a\u6378\ue805\uc7e3\u8a31\u2fcd\u44c1\u940c\u01e3\u50fd\uaf9f\ub6ed\u789e\u12ed\u8169\u9d88\ud309\udafc\uca43\uf56a\u04cc\u81f8\ub4b3\u97e6\u55a1\u913e\u4723\ue185\u1883\uda44\u6a98\u4a38\ue538\u2ade\u8112\u58a6\u4fa0\ue3a5\ua165\u445b\u21b1\uf0e4\uc76b\ueb32\u8926\uc66e\u810f\ub2ac\ude69\u4434\u41a6\u3854\u4b8a\u090f\u307e\u0eae\u2b2e\u5d82\u693e\u63d3\u5ec8\ud5fe\u3f48\u9c95\u880c\u23a3", 389669009, 0xDA7D000E ^ CryptoVault.mask()), SetupPicker.yv("\ub858\u21e9\uea0b\uf581\u817a\u50be\u01f4\ud658\ucc88\ud942\u2756\u8059\u0ac5\ub822\u8c81\uc21e\u519a\ue708\u5235\ufd6e\uaa68\u60af\u373e\ua7b6\uf9d6\uee6d\u0c30\u8e03\u6e02\u2b5d\u0016\uf2d9\u0fc7\u5f51\ufded\ue30b\u01da\uce56\u3fa4\u349b\u14f2\u8fa7\uf94e\u7b42\u17a9\u2b5f\u8b55\u139a\uae0d\ue8d9\ubc64\ub1b1\uf3a3\u889c\u86dc\ub7e0\ue068\u85c8\u4a74\uf5a5\uc559\u1d8b", 739194591, 0xDA7D000E ^ CryptoVault.mask()), bl -> HitboxConfigScreen752809.au.setEnabled((boolean)bl));
        float f9 = f3 + (float)Math.round(76.0f * f6) + 14.0f * f6;
        builder.text(f2, f9, 9.5f * f6, uiHost.muted2Color(), SetupPicker.yv("\u03b1\u4d39\u3c26\u7f33\u25d2\u0fc9\u5ac3\ude41\u3e41\u196f\u02e5\u2b8a\u0562\u1335\u9c51\ucb8b\u7adf\udb67\ub7a9\u55ab\u0bc7\u5e9e\uf3ca\u1002\uf2c0\u909a\u7612\ucfaa\udb87\u95fe\ub588\uc442\udb69\u942e\ua376\u75a3\ub570\u5eed\u37d3\u16d8\u6de1\u4f8c\udd33\uf97d\u063f\u2f40\u7415\u48ca\u7b7d\u7443\ueff3\u979f\u73e2\uca15\uf3e7\ubd20\u57f1\u4488\u1761\u7840\ubfce\ub5dd\u60a7\ud447\uc7f9\u3703\u5ca3\u4abd\ud683\ud007", -1091843644, 0xDA7D000E ^ CryptoVault.mask()));
    }

    private void drawComfort(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.round(58.0f * f6);
        float f10 = Math.round(12.0f * f6 * uiHost.cornerScale());
        if (HitboxConfigScreen752809.aI != null && HitboxConfigScreen752809.aJ != null) {
            UiKit.card(builder, uiHost, f2, f3, f4, f9, f6);
            float f11 = 11.5f * f6;
            builder.text(f2 + 16.0f * f6, f3 + 12.0f * f6, f11, uiHost.textColor(), SetupPicker.yv("\ua27a\uaefa\uf122\ue4c0\u02b5\u9cc3\u75d0\u1676\u92ee\u4d49\u164c\uc98d", 1659192779, 0xDA7D000E ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2 + 16.0f * f6, f3 + 30.0f * f6, 9.5f * f6, uiHost.mutedColor(), SetupPicker.yv("\u1ca2\uefca\udc6c\u5036\u529d\u09ff\u0c32\u2e6a\u5358\u4d3e\u7c8c\u8cd4\ue2c2\u7045\u9c37\uaef4\uce25\ud744\u6baf\u6f61\ua352\uae8a\u1101\uf7a1\ub898\uc97d\uaaad\u8be3\u78a4\uac41\u1008\u0248\u5f83\u6596\ub3b8\u83d8\ud36d\u1d9e\u295b\ucbf0\u26db\u74d0\u99a3\u14b2\u63f7\u20bd\ue11b\u9a0d\uabf3\u7b43\u1b55\u6c4a\u467c\ucfe7\u7594\ua0c5\u702c\u6dd2", 254230490, 0xDA7D000E ^ CryptoVault.mask()));
            int n3 = 0;
            if (HitboxConfigScreen752809.aI.isEnabled()) {
                for (int i2 = 1; i2 < SOUND_LABELS.length; ++i2) {
                    if (!HitboxConfigScreen752809.aJ.is(SOUND_LABELS[i2])) continue;
                    n3 = i2;
                }
            }
            float f12 = Math.min(250.0f * f6, f4 * 0.5f);
            float f13 = Math.round(28.0f * f6);
            UiKit.segmented(builder, uiHost, SOUND_LABELS, n3, f2 + f4 - 14.0f * f6 - f12, f3 + (f9 - f13) * 0.5f, f12, f13, f6, f7, f8, n2 -> {
                if (n2 == 0) {
                    HitboxConfigScreen752809.aI.setEnabled(false);
                } else {
                    HitboxConfigScreen752809.aJ.setCurrentMode(SOUND_LABELS[n2]);
                    HitboxConfigScreen752809.aI.setEnabled(true);
                }
                this.touched[Step.COMFORT.ordinal()] = true;
            });
            f3 += f9 + 10.0f * f6;
        }
        if (HitboxConfigScreen752809.j != null) {
            UiKit.card(builder, uiHost, f2, f3, f4, f9, f6);
            builder.text(f2 + 16.0f * f6, f3 + 12.0f * f6, 11.5f * f6, uiHost.textColor(), SetupPicker.yv("\u3af7\ufd45\ubfb1\udf81\uf59a\u0f0c\ub56a\ub4b7\ue16d\uf947\u3826\u1c3d\ue433\u0407\uae7e\ue8e1\u5bf0\u069f", 238946868, 0xDA7D000E ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2 + 16.0f * f6, f3 + 30.0f * f6, 9.5f * f6, uiHost.mutedColor(), SetupPicker.yv("\udb0a\ucb03\u5b1c\ua0ea\u6c02\u00e5\uca55\uc78c\u395d\u77bb\ue485\u29ff\u1679\u9199\ubc36\u8c15\ubb66\uf061\uc413\ud4d5\uca10\ua2c0\u461c\u460e\ub363\u1797\u17a3\uc681\u955d\u4454\uf770\u8dea\u48b8\u60d1\uf8ce\udc57\uac7a\u7c39\u59a8\ud6f0\udc4f\u2ec8\u39bb\u2695\uef47\u90a7\ua5f6\u855e\ue5dc\u39b1\u654c\u7ba0\u6172\u6ec0\u5ce9\u6d19\u00c5\u0412\u58cb\ud416\u6b28\u291e", 1779471781, 0xDA7D000E ^ CryptoVault.mask()));
            UiKit.toggle(builder, uiHost, f2 + f4 - 14.0f * f6 - 34.0f * f6, f3 + (f9 - 18.0f * f6) * 0.5f, f6, HitboxConfigScreen752809.j.isEnabled(), () -> {
                HitboxConfigScreen752809.j.setEnabled(!HitboxConfigScreen752809.j.isEnabled());
                this.touched[Step.COMFORT.ordinal()] = true;
            });
        }
    }

    static {
        pendingLayout = "";
    }

    private static /* synthetic */ String yv(String string, int n2, int n3) {
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
    private static enum Step {
        THEME(Step.yx("\uf5b2\uc1fc\ua274\u1609\u7406\ud5bb\uc2e3\u8161\u2ee0\uef01\u83d5\u8575\uec84\uea9e\u3dde\uaa5e", 34709818, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\u8e03\ub688\u09fd\ue451\ued02\u56ee\ud584\u8034\u656b\u8fde\ucd3b\ub8d8\u591e\u1958\u9873\u470b\u4994\u16bf\u854a\u10fb\u3130\u73cc\u5b25\u9414\ue366\ud535\u5bd2\ufd8c\u9178\u3c10\u8d22\u102c\u5646\u22c5\u16ff\uc1bb\uabaa\u4fc5\ua810\u47d9\u21ad\udd23\uade7\u782b\u1344\u4366\u8200\uc156\u4b30\u80e2\u37b7\u7d9b\u2b1c\ue18b\u1d3b\u175d\ub3df\u09ff\ud99f\u4abf\u3a95\u28d4\ua563\u2309\u825a\ue0ba\u4724\ua824\udaa6\u108c\u644b\uf139\ue128\u93d8\u15b9\ufe21\u24e3\u6951\u3700\u08f3\u4cc4\ue1fb\u6267\u85e8\u2d1a\u213d\ucb13\u0907\u1b28\u5adc\u7825\u298f\uf856\u807a\u3231\u9df7\ua9c1\udb1d\uef23\u23bb\ucecb\u84f8\u363a\u0ff6\u8206\uac0f\u3736\ucf0f\ua767\u1180\ub85f\u9018\uca9b\ud03e\u97cc\u5691\ub08b\ufa21\u5523\u9e0d\ue19d\u9645\ubf4a\u662f\u5693\u6cb2\u1d51\u0b02\u4cf5\uec23\ubeb5\u0232\u2726\u833e\u41d3\uc16e\ua4b1\ud491\ue845\u3d3f\ud849\ub645\u9875\ubec4\u681b\uc6bd\u7cff\u6e3c\u397b\u20b6\u9377\ua3e3\u69c9\udc24", -691831460, 0xFD65DC6F ^ CryptoVault.mask())),
        LAYOUT(Step.yx("\uf6fa\u559a\u9ee5\u3d6d\ua574\u375f\u700e\u39b7\u2e93\uc51d\u0fa4\udc3f\u7f3f\uf239\u8930\u82dc\u3630\uee58\u498d\u48c8", 1714735959, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\uf8f7\u7837\uc2aa\u5c4a\u8beb\u79da\u918b\u1b91\ue4eb\uf6ef\u0f2b\u3edf\uae19\u0758\u102c\u67cb\u45c5\u32b7\ud7ea\u64f1\ubb9e\ua831\u1c89\u4bc9\u1976\ue500\uf1f4\ua30c\ubd28\ufcea\u8be3\u2691\u9f43\u8d45\udafa\ue06c\ubf74\u2044\ude67\u14bf\ucf8d\u5858\u75d0\u0925\ucb38\u5b9d\u441a\u8a3f\ube57\u1f85\u33d9\uadbd\u4f15\uea17\ub7a6\ua819\u2c04\uc6f4\u395b\u615a\ub1fd\u7006\ucaa5\u83fe\ue473\u9ba9\u5bd1\ue19b\uf865\uc592\uc242\u966f\ua590\ueafb\uc25a\uad2a\uc4be\u6ce3\ua99d\u18b3\u30ce\u7534\u8343\u02f6\u13bb\u51bf\u849c\u2cde\uf3ac\u8af2\uc280\udc8a\u7389\uae6b\u3963\u8045\udb1d\u6124\uec32\u906d\u1616\u6c4e\ua14c\u9f62\u8049\u9814\u70dc\ue340\u3339\u7ce4\u015e\u24b2\u3fa1\u7927\ue57c\ued6e\u75a9\u422d\ufce8\ua2f9\u976d\uca92\u3692\ua12c\uba23\u0622\ucb8e\ub403\u5219\u539f\ud092\ue415\u15e9\ued0f\ub1db\ub62d\u0a8d\ue168\ua234\u9ba8\u0a15\u0a9b\u6a9d\uda09\udf20\u6907\uf00e\u1ad5\u0d97\u03c2\u9a61\u223d\u537f\uc612\ud9c5\u6afc\u7630\u5d14\uc54a\ua22d\u65eb\u956e\udbed\u87f6\u8eba\u30d8\u1fd2\uccc2\ub551\uac00\u0519\u882b", 1823417289, 0xFD65DC6F ^ CryptoVault.mask())),
        MODULE_SETTINGS(Step.yx("\uede7\uebfe\uead2\uec93\u8a0f\uc243\u496d\u4650\ud86a\uecf7\uc40b\ua5d8\u54c7\uafe4\ud339\u718b\ue8e3\u4280\uc1d2", 1637763391, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\u9e6a\ua225\u870e\u375b\uaef5\ufc41\ua112\uf78b\u2d03\u8e12\u46b7\u3f1d\uae34\u514e\u46e4\u4a5e\uf90a\uabfd\ude52\u6127\uba7c\u8e19\u91cb\u8059\u6d42\ud6be\u7ebc\u4aa3\u3b4c\u91c5\u1e20\uffbf\u206a\ue00a\u20bd\u7bfa\ubbdb\ub22b\u56e8\ub05f\ua070\uc838\u44d4\uefe9\ufbc7\u8a92\uc32a\u6902\u5ef7\uc588\ua47a\u2b50\u7011\ubc5f\u1ead\u02c6\uee18\ua57b\ua24e\u06f8\u03a3\u6dfc\u7774\u167f\u7042\u199c\u6baf\uc0d9\u098e\u8b88\uefdf\ud63c\ubaaa\u87dc\u2593\ucc8d\u13de\uc19b\u34eb\u9c0d\u21e9\ubf6c\ufadc\udca8\u95fc\u115b\ua673\u9e05\ud0c1\u2b1e\u8441\ubed1\u28f5\u9380\u636a\ua681\u6872\u2d88\u52f5\ub981\u2070", -560220988, 0xFD65DC6F ^ CryptoVault.mask())),
        PERFORMANCE(Step.yx("\udc37\u2460\u3fb5\u3e7f\ue136\ucdac\u209d\ub91f\u77ac\u27f2\u4de0\u304f\u6eaa\uf4c1", -1165180883, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\ub8e4\u51a2\u8d7a\u862d\u57e3\u7bf3\u01c8\u9849\ud8c5\ufeab\u5608\u2d64\u54d3\u1d47\u7d4a\u1656\ucb06\ud942\uea88\u4cda\u8781\u3c43\u182a\u63df\ubc30\u5465\u6338\ua841\ud1b1\u1ea1\ubb31\ucafa\u86af\udf35\u6a21\u649f\u1165\u05c9\u815a\ucd1f\ud92a\u76bb\u0cb5\uf784\ud695\u1566\u6bf6\u3f1f\u933a\u0727\u1a05\ucf3c\u6467\u8956\udccf\ud5d0\ufdf2\uf5aa\u854d\ud788\u33bf\udd5c\ucb68\u6396\u5d20\u4b8e\ub4be\ue8cc\u7df8\u63e0\ub5f1\uea9f\u3645\u80f6\u5b7e\ubd6f\uae6d\uf19a\u479e\ua163\u7fa8\u23b1\u445a\ue332\uabd8\ue1fc\u4c7f\u56cc\u184b\u9a55\u12ad\u0855\uabb7\u2b56\ud4c3\uf612\u38d0\u3453\u79e3\u8bc6\u2580\ufdd5\u7c9a\u0e19\uaabc\uc523\ua35d\ue88d\u66d5\ub65c\uf51a\u380d\uf711\ue668\u3880\ufcde\ua06f\u1062\u3072\u2def\u7a5b\ubc06\u6303\u57c5\u03d0\uee20\u6c43\u59da\u8ae8\uad9b\u721d\u8b18\u600b\u8cfd\u6b86\uad09\ub530\ufce8\u0720\u9cb1\u35c7\u8e44\uf8af\ub993\u61a4\ud947\u963d\ub700\ue0c2\u455d\u3312\u9a44\u8652\ub272\u8e7b\u9250\uba57\uc071\u0356\u8ee9\u55af\uaeb8\u9663\u6d3a\ua692\u47c4\u5253\ue6e5\u4067\uc8c2\u27f5\u08a7\u40c6\u3f57\u604b\u7d81\u9064\u8dc3\u1012\uf90f\u9f13\u85b4\u012d\u2ebd\ub4b8\ub8ca\udab6\u169c\ue8ae\u3d37\ub066\u150e\u8a24\u9093\uaaed\u9d17\u364b\u6893\u80a6\u5dca\u6e9a\uca14", 1315932783, 0xFD65DC6F ^ CryptoVault.mask())),
        ACCENTS(Step.yx("\ud266\uf67b\uc5c0\u65ba\u54b2\uc2f0\uc217\u5cfe\ubbe2\u40f8\ua31f\u6400\u17f0\u9f2c\u0d00\ubddd\u64ae\u0afb\ue564\uaed4", -993367604, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\u15b2\uf79a\u7927\u9eb1\udda8\u8eee\ue2ac\ueab2\u61b8\u7a4f\u8696\uc05c\u4c8c\u06da\u97f2\u47b1\u07b7\uf5df\u10ec\u8c79\u54cd\u99c3\u0eb5\u955f\ubc66\uec5a\ub880\u8cd7\ufdf2\u0f46\uebd4\uc391\u78db\uec7f\ua2ed\u5127\u1abf\u611b\u7718\u3bad\ue1b7\u431e\u530f\ud15f\uca04\ue310\u61f8\u648b\uc872\u7269\uea92\u1651\u4422\uc2ca\udc38\u8eed\u5c30\u8e1c\u1b38\u83e5\u42e5\u5d59\ub6e4\u5187\u3555\u900e\uf599\u6445\ub5fd\u96ac\ue895\u30ab\u6a76\u1e09\u7e16\u2b2b\u0e2c\u7aaa\ubc32\u5aa6\u54ba\u329c\ue478\uebf9\u284b\u6e23\u03f8\ube0e\u3dab\uaba6\u774f\ucc1f\u8d46\u6dcc\u22bf\u5e6b", 1456650401, 0xFD65DC6F ^ CryptoVault.mask())),
        SS_PROOF(Step.yx("\ua8b5\ue3ac\u76ca\ub6d2\uc94c\uda1b\uf771\u011f\u42f6\u5b2b\ucd25\ud01b\udfd0\u6b4a\ufdc4\u8af3\u7a94\u4caa", -20575648, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\u2317\u1caa\u0b78\u0646\u55e5\uc964\uff1e\uc903\uef3f\u2a5d\u7899\u9c88\uf81f\ua340\uf256\u6afa\ub6c2\ud5fe\ud543\u4e43\ua596\uf40c\u03d6\u7322\u5c1e\uc33d\u1a55\uc2f6\u1426\u888f\ue20d\uaf3a\u180c\u06aa\u0952\u3031\u9936\ua396\u5a5e\u83de\uc7d6\u3294\u9752\uf5da\u7042\u6939\u0e51\u7ead\u08a3\u8e55\u1a6f\u3d26\u6512\u0814\uf0dc\u7d9e\u613e\u7a65\u4ad7\u06e8\ude6d\u49c8\uce01\uc3a4\uf741\u7bae\u0fde\u0aa6\u7dc4\u8107\u33bb\u07ad\ue9e4\u212b\u380a\u7878\uf8f0\u4105\ud407\u56bb\u0da2\u0b46\u6144\u5fcd\u20dc\u74f1\u6b28\u077d\u28db\u413c\uc4fc\u9877\u6676\ud776\uc4f1\u59bb\ue7da\u79b6\u4313\u6903\u8d9f\u07c1\u6b61\u28a1\u994c\u11e0\uad5f\u2024\u680e\u24dc\ue5c3\uc2a6\u8d0c\u7b85\u9605\u2956\u3d3f\u5734\uc799\ufc88\ua3eb\uf70e\u9787\u94a8\u85bc\u76e4\u3e49", -1073704364, 0xFD65DC6F ^ CryptoVault.mask())),
        CLEAN_CAPTURE(Step.yx("\uda5d\u5c93\ue295\uc645\u3459\ua22c\u5c09\u17d4\uf907\u2266\uf249\u0ed1\u9385\ufbb7", -1638015147, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\ue931\u17bf\u57ce\ud63a\uaef6\u59a7\u5cb3\u569c\ue097\uc7a0\u6a58\u534a\u7738\uf248\u389e\u93df\u64d0\ud82d\u99a4\u70fe\uc0a1\ud5c0\uca54\u0fbe\u35e8\u490b\uad1e\ue373\u98a2\u39eb\u9ebb\u928c\u3257\ue70d\u9279\u49dc\u4be0\u6c10\u62a3\u80fe\u01f7\uc86d\ua88c\ua4b4\u73bb\uc2a3\ua185\uf1e2\u7835\u6279\ue847\ua301\u778a\u2685\u1584\uf9ba\u2150\u1ab8\udc59\u2cbf\u0431\u7762\uafd0\u00de\u9b14\u0c57\u7bfb\uf297\u8089\u9742\udb14\u43a9\u07c5\u3346\u44c3\uc57e\u0d44\u4256\u8344\u113b\u61ba\uf66a\u71ef\u39db\uff23\u2743\ubb70\u809f\u4b24\u8357\u6e68\ua806\u6fe1\u17e4\ubd79\u31e8\ufbc8\u80eb\ue984\u9d49\u5626\u6fcd\u8c83\u933d\u86a9\u50cd\ue45f\u80eb\uf619\u6a9c\u1466\u70c6\u4049\u9c8c\u8b44\udcc3\u99f1\ua5d5\u5785\uec89\uff69\u4460\ue8f2\u0a88\u7e4b\u3dcb\u2291\u4d10\u00bd\u8006\u0d41\u1a94\ufb51\ub746\ucb49\ud82a\u9a66\u2d39\u1336\u3363\u2b04\ucbc4\uaa31\u0293\u4ce7\uc0e7\uef2c\uad2e\ubd36\ub7cb\u8a65\ue7af\u1448\u1604\u8757\u13bb\ud58b", -1586563731, 0xFD65DC6F ^ CryptoVault.mask())),
        COMFORT(Step.yx("\u2ab0\u063f\u16ab\u7dd3\u52bb\u7192\uf6b2\udc6f\ub6b1\ua97f\uddb5", 1760456057, 0xFD65DC6F ^ CryptoVault.mask()), Step.yx("\u6b88\u4e20\uaf94\u2494\uda9d\ub992\uc0b8\u68d9\u934c\u9c7c\u2fee\ub99d\u8a75\udb1c\u8735\u29f1\u37e2\ufa71\u6765\u7736\u92ea\ueaed\u1cc5\uec5d\ueeb0\u7d58\u08d6\u8156\u6851\uf801\uc4eb\u6455\ub84f\u8f32\u7681\uf43c\u1435\u67f6\u44f3\u43aa\u4288\ubf52\u9866\ub784\ue3a8\u0353\u92ca\u8423\u4f10\udaa9\u0241\uf958\u5ddf\uddba\uf061\u9cac\uaf6d\udb23\ue219\u9a0d\u6e4a\u0a54\u6a5f\u4a6e\u53e3\ub7e0\u87cd\u0c2d\udc35\u2427\ufea6\u6007\u7635\u2385\u8f2f\ub57e\u4e94\u8a75\u3d8d\uf6c7\u27df\u19b6", 392610529, 0xFD65DC6F ^ CryptoVault.mask()));

        final String title;
        final String body;

        private Step(String string2, String string3) {
            this.title = string2;
            this.body = string3;
        }

        private static /* synthetic */ String yx(String string, int n2, int n3) {
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
    public static interface LayoutPainter {
        public void paint(UiDisplayList.Builder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);
    }
}

