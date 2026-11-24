/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  org.lwjgl.glfw.GLFW
 */
package com.ishland.scalablelux.gui.new_ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.config.ClientPrefs;
import com.ishland.scalablelux.config.ConfigManager;
import com.ishland.scalablelux.gui.ClickGuiRouter;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.kit.ResponsiveLayout;
import com.ishland.scalablelux.gui.gpuui.kit.SliderValueEdit;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.hud.GpuHudRenderer;
import com.ishland.scalablelux.hud.HUDElement;
import com.ishland.scalablelux.hud.HUDManager;
import com.ishland.scalablelux.hud.HudHistory;
import com.ishland.scalablelux.hud.HudStyleClipboard;
import com.ishland.scalablelux.hud.ItemIconWidget;
import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.ColorSetting;
import com.ishland.scalablelux.module.ModeSetting;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.module.SliderSetting;
import com.ishland.scalablelux.module.StringSetting;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import me.sootysplash.box.render.HitboxDrawPresentationStyle264081;
import me.sootysplash.box.render.TargetBoxMetricsWidget55572;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public final class HudEditorScreen
extends class_437 {
    private static final float GRID = 16.0f;
    private static final float SNAP = 8.0f;
    private static final float GUIDE_SNAP = 4.0f;
    private static final float ROW = 27.0f;
    private static final float PICKER_HEIGHT = 153.0f;
    private static final float SV_HEIGHT = 84.0f;
    private static final long RESET_ARM_MS = 3000L;
    private static final long SCALE_FLASH_MS = 900L;
    private static final double SCALE_STEP = 0.05;
    private static final int[] PRESETS;
    private static final int[] HUE_STOPS;
    private static final String[] LEGEND_KEYS;
    private static final String[] LEGEND_LABELS;
    private static final int COMPACT_LEGEND = 3;
    private static final long HINT_MS = 2600L;
    private static final long DELETE_ARM_MS = 3000L;
    private static final int LAYOUT_NAME_MAX = 32;
    private final LinkedHashSet<HUDElement> multi = new LinkedHashSet();
    private boolean layoutsOpen;
    private boolean alignOpen;
    private int layoutsScroll;
    private String armedDeleteLayout;
    private long armedDeleteUntil;
    private boolean editingLayoutName;
    private final StringBuilder layoutName = new StringBuilder();
    private String hint;
    private long hintUntil;
    private boolean showTitle = true;
    private float layoutsX;
    private float layoutsW;
    private float alignX;
    private float alignW;
    private int accent = -2170138;
    private int primary = -920588;
    private int secondary = -6512218;
    private int muted = -10065040;
    private int border = 0x3AFFFFFF;
    private int panelTop = -182638538;
    private int panelBottom = -233958113;
    private int backdrop = -737735918;
    private int gridTint = 0xFFFFFF;
    private int toggleOff = -12299933;
    private int toggleOn = -2170138;
    private static final int DANGER = -38037;
    private final class_437 parent;
    private HUDElement dragging;
    private HUDElement selected;
    private HUDElement settingsElement;
    private SliderSetting draggingSlider;
    private StringSetting editingString;
    private SliderSetting editingSlider;
    private boolean editingSliderPercent;
    private boolean sliderFresh;
    private final StringBuilder sliderBuffer = new StringBuilder();
    private int settingsScrollOffset;
    private double sliderMinX;
    private double sliderMaxX;
    private boolean saved;
    private float chromeHide;
    private long lastFrameNanos;
    private float guideX = Float.NaN;
    private float guideY = Float.NaN;
    private ColorSetting expandedColor;
    private float pickHue;
    private float pickSat;
    private float pickBri;
    private PickPart pickDrag = PickPart.NONE;
    private boolean editingHex;
    private final StringBuilder hexBuffer = new StringBuilder(9);
    private float svX;
    private float svY;
    private float svW;
    private float svH;
    private float hueX;
    private float hueY;
    private float hueW;
    private float hueH;
    private float alphaX;
    private float alphaY;
    private float alphaW;
    private float alphaH;
    private float hexX;
    private float hexY;
    private float hexW;
    private float hexH;
    private float swX;
    private float swY;
    private float swSize;
    private float swGap;
    private float hdrX;
    private float hdrY;
    private float hdrW;
    private float hdrH;
    private float btnY;
    private float btnH;
    private float styleX;
    private float styleW;
    private float resetX;
    private float resetW;
    private float doneX;
    private float doneW;
    private long resetArmedUntil;
    private HUDElement scaleFlash;
    private long scaleFlashUntil;
    private String hoverDescription;
    private boolean snapOn = ClientPrefs.get().gui().hudSnap();
    private boolean gridOn = ClientPrefs.get().gui().hudGrid();
    private float gridAmt;
    private float frameDt;
    private boolean libCollapsed;
    private float libSlide;
    private boolean ctxOpen;
    private HUDElement ctxEl;
    private float ctxX;
    private float ctxY;
    private float ctxAnim;
    private HUDElement animFor;
    private float animX;
    private float animY;
    private float animW;
    private float animH;
    private float selFade;
    private HUDElement hoverEl;
    private float hoverFade;
    private final IdentityHashMap<HUDElement, Float> libSwitchT = new IdentityHashMap();
    private final HudHistory history = new HudHistory();
    private final HudStyleClipboard styleClip = new HudStyleClipboard();
    private boolean libraryOpen;
    private float libBtnX;
    private float libBtnW;
    private float libScroll;
    private final StringBuilder libSearch = new StringBuilder();
    private boolean libSearchFocus;
    private final List<Object[]> libIconJobs = new ArrayList<Object[]>();
    private HUDElement scalingEl;
    private double scaleStart;
    private float scaleOx;
    private float scaleOy;
    private float scaleBase;
    private boolean boxing;
    private float boxX0;
    private float boxY0;
    private float boxX1;
    private float boxY1;
    private long lastNudgeMs;
    private long lastWheelMs;
    private int lastSizeW;
    private int lastSizeH;
    private static final String COLOURS_LABEL;

    public HudEditorScreen(class_437 class_4372) {
        super((class_2561)class_2561.method_43470((String)HudEditorScreen.nc("\u1425\u0f29\u2dc5\u731f\ue2f6\ud281\u78f1\u3cba\u5cc7\u8735\u5504\uf7bd\u441f\u45f0", 1407179688, 0x1AFDE8A7 ^ CryptoVault.mask())));
        this.parent = class_4372;
    }

    private boolean still() {
        return HitboxConfigScreen752809.j != null && HitboxConfigScreen752809.j.isEnabled();
    }

    private float ease(float f2) {
        return this.still() ? 1.0f : 1.0f - (float)Math.exp(-f2 * this.frameDt);
    }

    private int fade(int n2, float f2) {
        return this.withAlpha(n2, Math.round((float)(n2 >>> 24 & 0xFF) * Math.max(0.0f, Math.min(1.0f, f2))));
    }

    private static int mix(int n2, int n3, float f2) {
        int n4 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f2) + (float)(n3 >> 16 & 0xFF) * f2);
        int n5 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f2) + (float)(n3 >> 8 & 0xFF) * f2);
        int n6 = Math.round((float)(n2 & 0xFF) * (1.0f - f2) + (float)(n3 & 0xFF) * f2);
        return n4 << 16 | n5 << 8 | n6;
    }

    /*
     * Unable to fully structure code
     */
    private void refreshTheme() {
        var1_1 = null;
        try {
            var2_2 = SlApp.getInstance().getModuleManager();
            var1_1 = var2_2 == null ? null : var2_2.getModule(HitboxConfigScreen752809.class);
        }
        catch (Throwable var2_3) {
            // empty catch block
        }
        var2_2 = UiThemeMaterial.theme(var1_1 == null ? "" : HitboxConfigScreen752809.a.getCurrentMode());
        if (var1_1 == null) ** GOTO lbl-1000
        if (!HitboxConfigScreen752809.I.isEnabled()) ** GOTO lbl-1000
        if ((HitboxConfigScreen752809.K.A().getRGB() & 0xFFFFFF) != 15264752) {
            v0 = -16777216 | HitboxConfigScreen752809.K.A().getRGB();
        } else lbl-1000:
        // 3 sources

        {
            v0 = var2_2.accent();
        }
        this.accent = v0;
        this.primary = -16777216 | HitboxConfigScreen752809.a(HitboxConfigScreen752809.al, var2_2.text());
        this.secondary = -16777216 | HitboxConfigScreen752809.a(HitboxConfigScreen752809.am, var2_2.muted());
        this.muted = var2_2.muted2();
        var3_4 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ai, var2_2.tint()) & 0xFFFFFF;
        var4_5 = var2_2.panelBase() & 0xFFFFFF;
        var5_6 = var2_2.deep() & 0xFFFFFF;
        var6_7 = 0x58000000 | HudEditorScreen.mix(var3_4, this.accent & 0xFFFFFF, 0.35f);
        this.border = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ak, var6_7);
        this.panelTop = -201326592 | HudEditorScreen.mix(var4_5, var3_4, 0.1f);
        this.panelBottom = -234881024 | HudEditorScreen.mix(var5_6, var4_5, 0.45f);
        this.backdrop = -872415232 | var5_6;
        this.gridTint = var3_4;
        this.toggleOff = -16777216 | HitboxConfigScreen752809.a(HitboxConfigScreen752809.ao, var2_2.toggleOff());
        this.toggleOn = -16777216 | HitboxConfigScreen752809.a(HitboxConfigScreen752809.an, this.accent);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        HUDElement hUDElement;
        HUDElement hUDElement2;
        HUDElement hUDElement32;
        this.refreshTheme();
        long l2 = System.nanoTime();
        float f3 = this.lastFrameNanos == 0L ? 0.0f : Math.min(0.1f, (float)(l2 - this.lastFrameNanos) / 1.0E9f);
        this.lastFrameNanos = l2;
        this.frameDt = f3;
        float f4 = this.dragging != null ? 1.0f : 0.0f;
        this.chromeHide += (f4 - this.chromeHide) * this.ease(16.0f);
        if (Math.abs(f4 - this.chromeHide) < 0.002f) {
            this.chromeHide = f4;
        }
        float f5 = this.dragging != null || this.boxing || this.scalingEl != null ? 1.0f : 0.0f;
        this.gridAmt += (f5 - this.gridAmt) * this.ease(10.0f);
        float f6 = this.libCollapsed ? 1.0f : 0.0f;
        this.libSlide += (f6 - this.libSlide) * this.ease(14.0f);
        if (Math.abs(f6 - this.libSlide) < 0.004f) {
            this.libSlide = f6;
        }
        this.ctxAnim += ((this.ctxOpen ? 1.0f : 0.0f) - this.ctxAnim) * this.ease(20.0f);
        if (!this.ctxOpen) {
            this.ctxAnim = 0.0f;
        }
        int n4 = class_3322.method_51421();
        int n5 = class_3322.method_51443();
        float f7 = GpuHudRenderer.viewportScale(n4, n5);
        List<HUDElement> list = HUDManager.getInstance().getElements();
        if (n4 != this.lastSizeW || n5 != this.lastSizeH) {
            if (this.lastSizeW != 0) {
                for (HUDElement hUDElement32 : list) {
                    this.clampToScreen(hUDElement32);
                }
            }
            this.lastSizeW = n4;
            this.lastSizeH = n5;
        }
        UiDisplayList.Builder builder = UiDisplayList.builder();
        builder.rect(0.0f, 0.0f, n4, n5, 0.0f, this.backdrop);
        this.drawGrid(builder, n4, n5);
        GpuUiRenderer.render(class_3322, builder.build());
        hUDElement32 = class_3322.method_51448();
        hUDElement32.pushMatrix();
        try {
            hUDElement32.scale(f7, f7);
            GpuHudRenderer.renderElements(class_3322, this.visibleElements(), Math.round((float)n4 / f7), Math.round((float)n5 / f7), f2, true);
        }
        finally {
            hUDElement32.popMatrix();
        }
        if (this.field_22787 != null && this.field_22787.field_1724 != null) {
            HUDManager.renderArmorIcons(class_3322, this.visibleElements(), f7);
        } else {
            HUDManager.renderWidgetIcons(class_3322, this.visibleElements(), f7);
        }
        this.libIconJobs.clear();
        UiDisplayList.Builder builder2 = UiDisplayList.builder();
        HUDElement hUDElement4 = hUDElement2 = this.ctxOpen && this.ctxMenu(null, n4, n5, n2, n3) != null ? null : this.elementAt(list, n2, n3, n4, n5);
        if (hUDElement2 != this.hoverEl) {
            this.hoverEl = hUDElement2;
            this.hoverFade = this.still() ? 1.0f : 0.0f;
        }
        this.hoverFade += (1.0f - this.hoverFade) * this.ease(16.0f);
        HUDElement hUDElement5 = hUDElement = this.dragging != null ? this.dragging : hUDElement2;
        if (this.dragging == null && hUDElement2 != null && hUDElement2 != this.selected && !this.multi.contains(hUDElement2)) {
            this.drawHover(builder2, hUDElement2, n4, n5, this.hoverFade);
        }
        if (this.selected != null && HUDManager.isWidget(this.selected) && !this.selected.isEditorHidden()) {
            this.drawSelection(builder2, this.selected, n4, n5);
        } else {
            this.animFor = null;
        }
        if (this.boxing) {
            this.drawMarquee(builder2);
        }
        if (this.multi.size() > 1) {
            for (HUDElement hUDElement6 : this.multi) {
                if (hUDElement6 == hUDElement) continue;
                float f8 = GpuHudRenderer.editorX(hUDElement6, n4, n5);
                float f9 = GpuHudRenderer.editorY(hUDElement6, n4, n5);
                float f10 = GpuHudRenderer.editorWidth(hUDElement6, n4, n5);
                float f11 = GpuHudRenderer.editorHeight(hUDElement6, n4, n5);
                builder2.rect(f8 - 1.0f, f9 - 1.0f, f10 + 2.0f, f11 + 2.0f, Math.min(18.0f, f11 * 0.5f), this.withAlpha(this.accent, 28));
                builder2.border(f8 - 2.0f, f9 - 2.0f, f10 + 4.0f, f11 + 4.0f, Math.min(18.0f, f11 * 0.5f), 1.4f, this.withAlpha(this.accent, 208));
            }
        }
        if (this.dragging != null) {
            this.drawGuides(builder2, n4, n5);
        }
        this.layoutHeader(n4, n5);
        if (this.chromeHide < 0.999f) {
            this.drawHeader(builder2, n4, n5, n2, n3);
            this.drawFooter(builder2, n4, n5, HudEditorScreen.widgetCount(list));
            if (this.layoutsOpen) {
                this.drawLayoutsPanel(builder2, n4, n5, n2, n3);
            }
            if (this.alignOpen) {
                this.drawAlignPanel(builder2, n4, n5, n2, n3);
            }
            if (this.libraryOpen) {
                this.libraryPanel(builder2, n4, n5, n2, n3);
            }
            this.toolbar(builder2, n4, n5, n2, n3);
        }
        this.drawHint(builder2, n4, n5);
        this.hoverDescription = null;
        if (this.dragging == null) {
            if (hUDElement2 != null && this.settingsElement == null && !this.ctxOpen) {
                this.drawElementLabel(builder2, hUDElement2, n2, n3, n4, n5);
            }
            if (this.settingsElement != null) {
                this.drawSettingsPopup(builder2, this.settingsElement, n4, n5, n2, n3);
            }
        }
        if (this.ctxOpen) {
            this.ctxMenu(builder2, n4, n5, n2, n3);
        }
        if (this.hoverDescription != null && this.settingsElement != null) {
            this.drawDescription(builder2, this.settingsElement, n4, n5);
        }
        GpuUiRenderer.render(class_3322, builder2.build());
        if (this.libraryOpen && this.chromeHide < 0.999f) {
            this.runIconJobs(class_3322);
        }
    }

    private List<HUDElement> visibleElements() {
        List<HUDElement> list = HUDManager.getInstance().getElements();
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>(list.size());
        for (HUDElement hUDElement : list) {
            if (hUDElement.isEditorHidden()) continue;
            arrayList.add(hUDElement);
        }
        return arrayList;
    }

    private List<HUDElement> sel() {
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>();
        if (this.multi.size() > 1) {
            arrayList.addAll(this.multi);
        } else if (this.selected != null && HUDManager.isWidget(this.selected)) {
            arrayList.add(this.selected);
        }
        return arrayList;
    }

    private void layer(List<HUDElement> list, int n2) {
        this.history.checkpoint();
        int n3 = this.shiftDown() ? n2 * 1000 : n2;
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>(list);
        if (n2 > 0) {
            Collections.reverse(arrayList);
        }
        for (HUDElement hUDElement : arrayList) {
            HUDManager.getInstance().moveLayer(hUDElement, n3);
        }
        this.showHint(n2 > 0 ? (this.shiftDown() ? HudEditorScreen.nc("\u7fb7\u4785\ufb0f\uddc6\u1bb2\ucb32\u8eab\ude2c\u24b9\ue710\uf915\u5ea2\u46a0\u2100\ue24d\u716e\uae9e", 911939954, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u06e6\u6c92\u1530\u1611\u34ec\u3368\u4323\uc724\u52e8\u6086\ufdba\uec13\u38c8\u7aa8\u1005\ue4e7\u1a9d\uf9e4", -569445253, 0x1AFDE8A7 ^ CryptoVault.mask())) : (this.shiftDown() ? HudEditorScreen.nc("\u0ce1\u8307\uf122\u5a35\u75c9\u5e47\ubba2\u1d17\u6cfa\ubb2f\u0acd\uf417\ub182", -133234108, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u53b6\ue990\u320e\u3838\u3475\u186e\ub8bd\u802c\u7f90\u7c6c\u826f\u2e0b\u837e\u7a62", 1106304497, 0x1AFDE8A7 ^ CryptoVault.mask())));
    }

    private void doUndo() {
        this.showHint(this.history.undo() ? HudEditorScreen.nc("\u0bf2\ue41a\u7388\u472d\uff53\u1d61\ue05d\u0d46", 643821440, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u7914\u25be\u2ab8\u3796\u910b\ufb79\u1ddb\u1397\u3a22\u1059\u3711\u4843\ud4df\ufd5f\ud356\u014c\u9e1e", 1584308057, 0x1AFDE8A7 ^ CryptoVault.mask()));
    }

    private void doRedo() {
        this.showHint(this.history.redo() ? HudEditorScreen.nc("\ud270\u8452\uafcf\u2bf1\u85c4\u1d4a", -281724753, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\uc7b2\u4b3a\u0abe\ub6fc\u1821\uded7\u10f1\u4304\ubc5f\u5af4\u4526\u9657\ubb9d\ub8eb\ufbaa\ucf1e\u1faa\u5419", 640244798, 0x1AFDE8A7 ^ CryptoVault.mask()));
    }

    private void copyStyle(HUDElement hUDElement) {
        this.styleClip.copy(hUDElement);
        this.showHint(HudEditorScreen.nc("\u2523\uedec\udbc6\u4307\ucc1e\u5fed\u7d89\u8ce8\u9ccc\uc9a0\u1244\u696f\uf157\u13b5\u2008\uf74c\u3b64\u4b87\u7972\u06ca\u8292", -159012104, 0x1AFDE8A7 ^ CryptoVault.mask()) + hUDElement.getName());
    }

    private void pasteStyle(List<HUDElement> list) {
        if (this.styleClip.isEmpty()) {
            this.showHint(HudEditorScreen.nc("\u8c96\u5e8c\u15ec\ub0a7\uea5c\ua0f8\ub869\u2935\ub926\u671e\u8201\u9abb\u8757\u3815\uc466\u7a12c\ua05c\u9413\u39dd\u19ab\u6051", -8368789, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return;
        }
        int n2 = 0;
        for (HUDElement hUDElement : list) {
            n2 += this.styleClip.paste(hUDElement);
        }
        for (HUDElement hUDElement : list) {
            this.clampToScreen(hUDElement);
        }
        this.showHint(HudEditorScreen.nc("\ucc43\u614c\uf91d\ud745\u8e7d\uba61\u085a\u3fb2\u471b\u88ba", -1601322281, 0x1AFDE8A7 ^ CryptoVault.mask()) + n2 + HudEditorScreen.nc("\u3109\ub731\u65eb\u98bd\u7d9c\u0aac\uf427\ub4cd\u155d\u212c\ua1fe\u9adc\u541f\u8df1\u7076\ua76b\u5127\u41bc\ub714", 1221585056, 0x1AFDE8A7 ^ CryptoVault.mask()) + this.styleClip.source());
    }

    private Runnable toolbar(UiDisplayList.Builder builder, int n2, int n3, double d2, double d3) {
        Runnable runnable;
        float f2 = this.chromeScale(n2, n3);
        List<HUDElement> list = this.sel();
        HUDElement hUDElement = list.isEmpty() ? null : list.get(0);
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<Runnable> arrayList2 = new ArrayList<Runnable>();
        ArrayList<Integer> arrayList3 = new ArrayList<Integer>();
        arrayList.add(HudEditorScreen.nc("\u5146\u8eed\u77de\udc79\ue93d\ue8fb\uc799", -1268932515, 0x1AFDE8A7 ^ CryptoVault.mask()));
        arrayList2.add(this::doUndo);
        arrayList3.add(this.secondary);
        arrayList.add(HudEditorScreen.nc("\u4165\u837f\u9bf3\udf45\u1cf4\ue545\u39d9", -2119022752, 0x1AFDE8A7 ^ CryptoVault.mask()));
        arrayList2.add(this::doRedo);
        arrayList3.add(this.secondary);
        arrayList.add(this.snapOn ? HudEditorScreen.nc("\u328f\ub09c\u37d3\uc473\u5136\u30f1\u9638\ub33a\uc32c\u7952\uf826", 868210908, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u5a6e\u3442\uee22\u5510\ua03c\uc501\u212e\u0721\u9522\u47a4\ucd2e\ue4ea", 1124026693, 0x1AFDE8A7 ^ CryptoVault.mask()));
        arrayList2.add(() -> {
            this.snapOn = !this.snapOn;
            this.saveToggles();
            this.showHint(this.snapOn ? HudEditorScreen.nc("\uca01\u8f31\u40b7\u0315\uc30c\u5168\u9b4b\u4c23\u8548\ud711\ubf24\u7286\u6048\ucf45\uf409\u3494\u2109\u24fe\ub0b2\u0fd3\uf7fc\u95a5\u6d42\u5b32\ub5f9\u8cb2\u5ccf\u83b1\ub225\uf1c1\u2395", -6630718, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u03d4\uf961\u1afb\ua692\u135e\uf58b\ucdf6\uf716\ue93f\u8445\uc8a1\u127f\u5cbc\u99ef\u039f", 125969380, 0x1AFDE8A7 ^ CryptoVault.mask()));
        });
        arrayList3.add(this.snapOn ? this.accent : this.secondary);
        arrayList.add(this.gridOn ? HudEditorScreen.nc("\u8633\u29a8\uca5b\uefbc\u5622\uac83\u434d\u78ac\ud01c", 895868554, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u6e3b\u8b2f\uc462\u4594\u9ca5\u85f1\ua9de\uf8a7\u2b2e\u81fd\u4ab9\u729d", -777495080, 0x1AFDE8A7 ^ CryptoVault.mask()));
        arrayList2.add(() -> {
            this.gridOn = !this.gridOn;
            this.saveToggles();
        });
        arrayList3.add(this.gridOn ? this.accent : this.secondary);
        if (hUDElement != null) {
            boolean bl = hUDElement.isLocked();
            arrayList.add(HudEditorScreen.nc("\u4965\u542c\u19ef\u8807\ua875\u5cb9\u801f\u81c4\uf461\u4538\ueeec\u4186", -535652381, 0x1AFDE8A7 ^ CryptoVault.mask()));
            arrayList2.add(() -> this.resetPositions(list));
            arrayList3.add(this.secondary);
            arrayList.add(bl ? HudEditorScreen.nc("\ue0ff\u945e\u163a\u7d23\u5a9a\u244e\uaf37\u1e06\u84b9\ue2ab", 291649428, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u2272\ufee5\uedb7\u9c27\u9e81\u50cc\u77d5", 1817246203, 0x1AFDE8A7 ^ CryptoVault.mask()));
            arrayList2.add(() -> {
                for (HUDElement hUDElement : list) {
                    hUDElement.setLocked(!bl);
                }
                this.showHint(bl ? HudEditorScreen.nc("\u40f2\ua895\ued82\u9bfe\ue4e2\u8e19\ua610\u8177\u5797\u3124", 1448984176, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u6ea7\u3d0a\ue5c9\u5cf6\u3376\u45f9\u27f3\uaccc\ue45b\u4d09\ue445\udebd\ud488\u1d41\uc4d6\u510d\u2c5b\u16b0\u209d\u0862\u9746\uca53\u5b66\u5199\ucbcb\uc8e6\uaa13\u8e18\uc9b4\u8c66\u079b\ue759\u24c3\u1531\u4962\udc14", -71506021, 0x1AFDE8A7 ^ CryptoVault.mask()));
            });
            arrayList3.add(bl ? this.accent : this.secondary);
            arrayList.add(HudEditorScreen.nc("\u2469\ud317\udb55\ued91\u8b80\u01a5\uaf5c\u1797", -54524392, 0x1AFDE8A7 ^ CryptoVault.mask()));
            arrayList2.add(() -> this.hideTargets(list));
            arrayList3.add(this.secondary);
            arrayList.add(HudEditorScreen.nc("\u9128\u41bb\u2b01\udbc2\ufc3b\u3d1c\uc347\ub431\u6be6\u15ce\u1b2e\u2e81\ue30a", -2104087401, 0x1AFDE8A7 ^ CryptoVault.mask()));
            arrayList2.add(() -> this.copyStyle(hUDElement));
            arrayList3.add(this.accent);
            if (!this.styleClip.isEmpty()) {
                arrayList.add(HudEditorScreen.nc("\u5e41\u8d0c\uc1b1\ua99b\u2b17\u26e6\uf023\u0b1f\u367f\ud528\u6842\ua780\u49fb\u9a25\u203e", 626376110, 0x1AFDE8A7 ^ CryptoVault.mask()));
                arrayList2.add(() -> this.pasteStyle(list));
                arrayList3.add(this.accent);
            }
        }
        float f3 = (float)n2 - 16.0f * f2;
        float f4 = 16.0f * f2 - 5.0f * f2;
        for (String string : arrayList) {
            f4 += GpuUiRenderer.textWidth(string, 7.5f * f2) + 16.0f * f2 + 5.0f * f2;
        }
        float f5 = f4 > f3 ? f3 / f4 : 1.0f;
        float f6 = f2 * f5;
        float f7 = 19.0f * f6;
        float f8 = 5.0f * f6;
        float f9 = 8.0f * f6;
        float f10 = f9 * 2.0f - f8;
        float[] fArray = new float[arrayList.size()];
        for (int i2 = 0; i2 < fArray.length; ++i2) {
            fArray[i2] = GpuUiRenderer.textWidth((String)arrayList.get(i2), 7.5f * f6) + 16.0f * f6;
            f10 += fArray[i2] + f8;
        }
        float f11 = ((float)n2 - f10) * 0.5f;
        float f12 = 27.0f * f2;
        float f13 = (float)n3 - 26.0f * f2 - 12.0f * f2 - f12 - 8.0f * f2 + this.chromeHide * (f12 + 60.0f * f2);
        Runnable runnable2 = runnable = HudEditorScreen.inRect(d2, d3, f11, f13, f10, f12) ? () -> {} : null;
        if (builder != null) {
            builder.shadow(f11, f13 + 2.0f * f2, f10, f12, f12 * 0.5f, 14.0f * f2, 1.0f, 0x50000000);
            builder.pill(f11, f13, f10, f12, this.panelTop);
            builder.border(f11, f13, f10, f12, f12 * 0.5f, Math.max(0.9f, f2 * 1.1f), this.border);
        }
        float f14 = f11 + f9;
        float f15 = f13 + (f12 - f7) * 0.5f;
        for (int i3 = 0; i3 < fArray.length; ++i3) {
            boolean bl = HudEditorScreen.inRect(d2, d3, f14, f15, fArray[i3], f7);
            if (builder != null) {
                this.drawButton(builder, f14, f15, fArray[i3], f7, (String)arrayList.get(i3), bl, (Integer)arrayList3.get(i3), f6);
            }
            if (bl) {
                runnable = (Runnable)arrayList2.get(i3);
            }
            f14 += fArray[i3] + f8;
        }
        return runnable;
    }

    private void hideTargets(List<HUDElement> list) {
        for (HUDElement hUDElement : list) {
            hUDElement.setEditorHidden(true);
        }
        this.selected = null;
        this.multi.clear();
        this.ctxOpen = false;
        this.showHint(HudEditorScreen.nc("\u7a60\u4d61\u313d\ud0ec\u8a23\u9c87\u8b95\u5508\u996d\u76ae\u15e7\uc96a\u0b9f\u10b3\ub677\u7e29\u36c1\u7e42\uc393\udfe1\uc5f7\udbb1\u7d4c\ua2d2\u92f3\uaec3\u418d\u4022\u7580\u4f31\uc9ab\udfed\uc781\u488a\u23e9\u0f04\u09c8\uee39\u0166\u9390\u96ff\ufc77\ueabb\ufc4f\u4558\u350d\u7ccb\u28e7\u8460\udc1c\u32aa\ucb33\u680a\ubf25\u8a0c\u62ff", 264283125, 0x1AFDE8A7 ^ CryptoVault.mask()));
    }

    private void resetPositions(List<HUDElement> list) {
        this.history.checkpoint();
        int n2 = 0;
        for (HUDElement hUDElement : list) {
            if (hUDElement.isLocked() || !hUDElement.resetPosition()) continue;
            this.clampToScreen(hUDElement);
            ++n2;
        }
        this.history.settle();
        this.showHint(n2 == 0 ? HudEditorScreen.nc("\u2b37\ue67f\u056e\ufb24\ub21a\ufe12\u334c\u9788\uf41a\u45ee\ua0d7\u5ec0\uefbf\u7e3e\uc5f7\ub074\ucf9c\u8b8e", 529172561, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\ue5ae\u8618\u8706\u0e38\u280c\u18ac\u13ba\ub9fd\u1ec0\ud417\u4b11\u8e26\u9227\uacc6\u29d3\ufab0\u09aa\uaec0", 249317230, 0x1AFDE8A7 ^ CryptoVault.mask()));
    }

    private List<HUDElement> ctxTargets() {
        if (this.multi.size() > 1 && this.multi.contains(this.ctxEl)) {
            return new ArrayList<HUDElement>(this.multi);
        }
        return new ArrayList<HUDElement>(List.of(this.ctxEl));
    }

    private void ctxScale(List<HUDElement> list, int n2) {
        this.history.checkpoint();
        boolean bl = false;
        for (HUDElement hUDElement : list) {
            if (hUDElement.isLocked()) {
                bl = true;
                continue;
            }
            double d2 = n2 == 0 ? 1.0 : (double)Math.round((hUDElement.getScale() + (double)n2 * 0.05) / 0.05) * 0.05;
            hUDElement.setScale(d2);
            this.clampToScreen(hUDElement);
        }
        this.history.settle();
        if (bl) {
            this.showHint(HudEditorScreen.nc("\ue0b1\u5855\uaef0\u3970\u287a\udbd4\udf5c\u31b9\ufaa5\u265f\u2c19\ue282\u4f3d\u31be\ucd18\u5ce5\ubc0a\u5cd1\ube55\u111e\u485f\u38d9\u75ef\u12e7\u1e45\ufd70\u0d8c\u64d8\uf0f2", -1614495233, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }
    }

    private void ctxAlign(List<HUDElement> list, int n2) {
        this.history.checkpoint();
        float f2 = GpuHudRenderer.viewportScale(this.field_22789, this.field_22790);
        double d2 = Math.round((float)this.field_22789 / f2);
        for (HUDElement hUDElement : list) {
            if (hUDElement.isLocked() || hUDElement instanceof TargetBoxMetricsWidget55572) continue;
            this.unanchor(hUDElement, this.field_22789, this.field_22790);
            double d3 = HudEditorScreen.widthOf(hUDElement);
            hUDElement.setX(n2 == 0 ? 0.0 : (n2 == 1 ? (d2 - d3) * 0.5 : d2 - d3));
            this.clampToScreen(hUDElement);
        }
        this.history.settle();
    }

    private List<CtxRow> ctxRows() {
        ArrayList<CtxRow> arrayList = new ArrayList<CtxRow>();
        HUDElement hUDElement = this.ctxEl;
        List<HUDElement> list = this.ctxTargets();
        int n2 = (int)Math.round(hUDElement.getScale() * 100.0);
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u1963\u699d\u647b\u6e9d\u3f35\u2ec1\u1cab\u23a9\u250a", -1231792411, 0x1AFDE8A7 ^ CryptoVault.mask()), n2 + "%", HudEditorScreen.nc("\uac75\uff51\ufda9\u0085\ud1b0\u2cb6\u3f20\u2aa9\u76c3\u568b", 1566749694, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> this.ctxScale(list, -1), () -> this.ctxScale(list, 0), () -> this.ctxScale(list, 1)}, this.accent, true));
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u6487\u8774\ufb87\uf8f0\udd40\u7347\u065a", 85204611, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u19f0\u63b2\u81ec\ucb48\u39bb\uac82\u0fe3\ua928", 49002178, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\ue5c6\uaeb1\u0c09\u3f73\ue861\uddd9", 1343877508, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> this.ctxAlign(list, 0), () -> this.ctxAlign(list, 1), () -> this.ctxAlign(list, 2)}, this.accent, false));
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u72d2\ufd49\u2dbc\u10d5\ue700\u376e\u6d0c\ue594\u874f\u7995", -704261989, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u0a05\u8856\u4228\u7bc4\ud1e6\u1900\u6ae9\u5a44\ub4f3\uea53\u91d2\ua205\u45c9\u59e3\ue6ff\u1c00", -1807061262, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> this.layer(list, -1), () -> this.layer(list, 1)}, this.accent, true));
        boolean bl = hUDElement.isLocked();
        arrayList.add(new CtxRow(new String[]{bl ? HudEditorScreen.nc("\uf369\ua027\u9a63\ua10d\ue8f7\u2b5f\u7705\u8545", 1564037000, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\uc598\u03a5\ubd9b\u3ace\u5197\u41ef\ub070\ue18b", 1861525735, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> {
            for (HUDElement hUDElement : list) {
                hUDElement.setLocked(!bl);
            }
            this.showHint(bl ? HudEditorScreen.nc("\uc5e4\u997c\ubaaf\uc1c5\u1656\u793c\ufb21\u9a76\u781e\u31a1\ubf72\u937c", -2139422087, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u1637\u9d80\ucfd4\ua97c\uaccc\u5390\uaff3\uf34a\uba63\u14ff\u7856\uaf02\u5b96\u54bd\ufad4\u4b77\u9c28\u1729\u030b\u152f\ub1e2\u552c\ud9a7\udf27\ub21b\u8ebb\ua503\u1192\uc69c\u56c4\u33d7\u183f\ua46a\u6da9\ubf52\u93ed", -747702441, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }}, bl ? this.accent : this.secondary, false));
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u2930\u4912\ub24a\u5d1c\u4835", -2066209932, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> this.hideTargets(list)}, this.secondary, false));
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u1216\ue264\u6299\u7029\u458c\u34e8\ucb70\ue337\u5be0\uc62a\uf086\u094b\ud766\ud1eb\u3a41", 1357078511, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> this.resetPositions(list)}, this.secondary, false));
        arrayList.add(new CtxRow(new String[]{hUDElement.isEnabled() ? HudEditorScreen.nc("\ue031\u5b50\uc51c\u51fc\ua9d8\u7a1f\u1951\u6cd0\ubedb\u66f8\u009f\u38c7", 89514275, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u1ce8\u5090\uc8f6\u289b\uec3d\uf653\uf2c2\u2572\ue09d\u254a", -354085377, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> {
            this.history.checkpoint();
            hUDElement.setEnabled(!hUDElement.isEnabled());
            this.history.settle();
        }}, this.secondary, false));
        arrayList.add(new CtxRow(new String[]{HudEditorScreen.nc("\u6f2a\ue65b\u8d67\ud82c\u8d18\u5386\u2db2\u35e2\u3548\ue608\u0e22\u973d", -743024721, 0x1AFDE8A7 ^ CryptoVault.mask())}, new Runnable[]{() -> {
            this.closePopup();
            this.settingsElement = hUDElement;
            this.settingsScrollOffset = 0;
        }}, this.accent, false));
        return arrayList;
    }

    private Runnable ctxMenu(UiDisplayList.Builder builder, int n2, int n3, double d2, double d3) {
        Runnable runnable;
        float f2;
        if (!this.ctxOpen || this.ctxEl == null) {
            return null;
        }
        float f3 = this.chromeScale(n2, n3);
        List<CtxRow> list = this.ctxRows();
        float f4 = 22.0f * f3;
        float f5 = 19.0f * f3;
        float f6 = 3.0f * f3;
        float f7 = 7.0f * f3;
        float f8 = f4 + (float)list.size() * (f5 + f6) + f7 - f6;
        float f9 = f8 > (f2 = (float)n3 - 8.0f * f3) ? f3 * f2 / f8 : f3;
        f4 = 22.0f * f9;
        f5 = 19.0f * f9;
        f6 = 3.0f * f9;
        f7 = 7.0f * f9;
        f8 = f4 + (float)list.size() * (f5 + f6) + f7 - f6;
        float f10 = Math.min(176.0f * f9, (float)n2 - 8.0f);
        float f11 = this.still() ? 1.0f : Math.max(0.001f, this.ctxAnim);
        float f12 = Math.max(4.0f, Math.min((float)n2 - f10 - 4.0f, this.ctxX));
        float f13 = Math.max(4.0f, Math.min((float)n3 - f8 - 4.0f, this.ctxY)) + (1.0f - f11) * 6.0f * f9;
        Runnable runnable2 = runnable = HudEditorScreen.inRect(d2, d3, f12, f13, f10, f8) ? () -> {} : null;
        if (builder != null) {
            builder.shadow(f12, f13, f10, f8, 11.0f * f9, 14.0f * f9, f9, this.fade(0x72000000, f11));
            builder.gradient(f12, f13, f10, f8, 11.0f * f9, this.fade(this.panelTop, f11), this.fade(this.panelBottom, f11), this.fade(this.panelBottom, f11), this.fade(this.panelTop, f11));
            builder.border(f12, f13, f10, f8, 11.0f * f9, Math.max(0.7f, f9), this.fade(this.border, f11));
            String string = HudEditorScreen.fit(this.ctxEl.getName(), f10 - 24.0f * f9, 8.5f * f9);
            builder.text(f12 + 11.0f * f9, f13 + (f4 - 8.5f * f9) * 0.5f + 1.0f * f9, 8.5f * f9, this.fade(this.primary, f11), string, UiDrawCommand.TextWeight.SEMIBOLD);
        }
        float f14 = f13 + f4;
        for (CtxRow ctxRow : list) {
            int n4 = ctxRow.labels().length;
            float f15 = 4.0f * f9;
            float f16 = (f10 - f7 * 2.0f - f15 * (float)(n4 - 1)) / (float)n4;
            for (int i2 = 0; i2 < n4; ++i2) {
                float f17 = f12 + f7 + (float)i2 * (f16 + f15);
                boolean bl = HudEditorScreen.inRect(d2, d3, f17, f14, f16, f5);
                if (builder != null) {
                    this.drawButton(builder, f17, f14, f16, f5, HudEditorScreen.fit(ctxRow.labels()[i2], f16 - 8.0f * f9, 7.5f * f9), bl, this.fade(ctxRow.tone(), f11), f9);
                }
                if (!bl) continue;
                Runnable runnable3 = ctxRow.acts()[i2];
                boolean bl2 = ctxRow.keepOpen();
                runnable = () -> {
                    if (!bl2) {
                        this.ctxOpen = false;
                    }
                    runnable3.run();
                };
            }
            f14 += f5 + f6;
        }
        return runnable;
    }

    private float[] libTab(int n2, int n3) {
        float f2 = this.chromeScale(n2, n3);
        return new float[]{6.0f * f2, this.panelsTop(f2), 26.0f * f2, 36.0f * f2};
    }

    private boolean handleHit(HUDElement hUDElement, double d2, double d3, int n2, int n3) {
        float f2;
        double d4;
        if (hUDElement == null || !HUDManager.isWidget(hUDElement) || hUDElement.isLocked() || hUDElement.isEditorHidden()) {
            return false;
        }
        float f3 = this.chromeScale(n2, n3);
        float f4 = GpuHudRenderer.editorX(hUDElement, n2, n3) + GpuHudRenderer.editorWidth(hUDElement, n2, n3);
        double d5 = d2 - (double)f4;
        return d5 * d5 + (d4 = d3 - (double)(f2 = GpuHudRenderer.editorY(hUDElement, n2, n3) + GpuHudRenderer.editorHeight(hUDElement, n2, n3))) * d4 <= (double)(64.0f * f3 * f3);
    }

    private void drawHandle(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3) {
        float f2 = this.chromeScale(n2, n3);
        float f3 = GpuHudRenderer.editorX(hUDElement, n2, n3) + GpuHudRenderer.editorWidth(hUDElement, n2, n3);
        float f4 = GpuHudRenderer.editorY(hUDElement, n2, n3) + GpuHudRenderer.editorHeight(hUDElement, n2, n3);
        builder.circle(f3, f4, 7.0f * f2, this.withAlpha(this.accent, this.scalingEl == hUDElement ? 85 : 48));
        builder.circle(f3, f4, 3.6f * f2, this.withAlpha(this.accent, 255));
        builder.border(f3 - 3.6f * f2, f4 - 3.6f * f2, 7.2f * f2, 7.2f * f2, 3.6f * f2, 1.0f, this.withAlpha(0xFFFFFF, 160));
    }

    private void finishBox() {
        this.boxing = false;
        float f2 = Math.min(this.boxX0, this.boxX1);
        float f3 = Math.max(this.boxX0, this.boxX1);
        float f4 = Math.min(this.boxY0, this.boxY1);
        float f5 = Math.max(this.boxY0, this.boxY1);
        if (f3 - f2 < 5.0f && f5 - f4 < 5.0f) {
            if (!this.shiftDown()) {
                this.selected = null;
                this.multi.clear();
                this.alignOpen = false;
            }
            return;
        }
        for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
            if (!HUDManager.isWidget(hUDElement) || hUDElement instanceof TargetBoxMetricsWidget55572 || hUDElement.isEditorHidden()) continue;
            float f6 = GpuHudRenderer.editorX(hUDElement, this.field_22789, this.field_22790);
            float f7 = GpuHudRenderer.editorY(hUDElement, this.field_22789, this.field_22790);
            float f8 = GpuHudRenderer.editorWidth(hUDElement, this.field_22789, this.field_22790);
            float f9 = GpuHudRenderer.editorHeight(hUDElement, this.field_22789, this.field_22790);
            if (f8 <= 0.0f || f9 <= 0.0f || !(f6 < f3) || !(f6 + f8 > f2) || !(f7 < f5) || !(f7 + f9 > f4)) continue;
            this.multi.add(hUDElement);
        }
        HUDElement hUDElement = this.selected = this.multi.isEmpty() ? null : (HUDElement)this.multi.iterator().next();
        if (this.multi.size() == 1) {
            this.multi.clear();
        } else if (this.multi.size() > 1) {
            this.showHint(this.multi.size() + HudEditorScreen.nc("\uf975\ued66\u1915\u6aa6\u068e\uad09\u146f\u5790\uac38\u8f76\uc553\u80ed\u90c7\u5654\u47f8\uf42b\ua573\uc5ae\u1234\u6252\u6406\u8ef3\uf8a9\ufae0\u9ee2\ue13a\ucba0\u3588\u6105\u878f\u7aa6\uc886\u44da\u0fe4\u985c\u2365", -1383268514, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }
    }

    private void drawMarquee(UiDisplayList.Builder builder) {
        float f2 = Math.min(this.boxX0, this.boxX1);
        float f3 = Math.min(this.boxY0, this.boxY1);
        float f4 = Math.abs(this.boxX1 - this.boxX0);
        float f5 = Math.abs(this.boxY1 - this.boxY0);
        builder.rect(f2, f3, f4, f5, 3.0f, this.withAlpha(this.accent, 26));
        builder.border(f2, f3, f4, f5, 3.0f, 1.2f, this.withAlpha(this.accent, 184));
    }

    private List<HUDElement> libraryList() {
        String string = this.libSearch.toString().trim().toLowerCase(Locale.ROOT);
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>();
        for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
            if (!HUDManager.isWidget(hUDElement) || !string.isEmpty() && !hUDElement.getName().toLowerCase(Locale.ROOT).contains(string)) continue;
            arrayList.add(hUDElement);
        }
        return arrayList;
    }

    private static boolean previewable(HUDElement hUDElement) {
        return !(hUDElement instanceof TargetBoxMetricsWidget55572) && hUDElement.getX() >= 0.0;
    }

    private Runnable libraryPanel(UiDisplayList.Builder builder, int n2, int n3, double d2, double d3) {
        float f2;
        float f3;
        Object object;
        float f4 = this.chromeScale(n2, n3);
        float f5 = Math.min(272.0f * f4, (float)n2 * 0.5f);
        float f6 = 10.0f * f4 - this.libSlide * (f5 + 14.0f * f4);
        float f7 = this.panelsTop(f4);
        float f8 = Math.max(140.0f * f4, (float)n3 - f7 - 84.0f * f4);
        List<HUDElement> list = this.libraryList();
        int n4 = 0;
        for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
            if (!HUDManager.isWidget(hUDElement) || !hUDElement.isEnabled()) continue;
            ++n4;
        }
        if (builder != null && this.libSlide > 0.02f) {
            object = this.libTab(n2, n3);
            float f9 = this.libSlide;
            builder.shadow((float)object[0], (float)object[1], (float)object[2], (float)object[3], 9.0f * f4, 10.0f * f4, f4, this.fade(0x60000000, f9));
            builder.gradient((float)object[0], (float)object[1], (float)object[2], (float)object[3], 9.0f * f4, this.fade(this.panelTop, f9), this.fade(this.panelBottom, f9), this.fade(this.panelBottom, f9), this.fade(this.panelTop, f9));
            builder.border((float)object[0], (float)object[1], (float)object[2], (float)object[3], 9.0f * f4, Math.max(0.7f, f4), HudEditorScreen.inRect(d2, d3, (float)object[0], (float)object[1], (float)object[2], (float)object[3]) ? this.fade(this.accent, f9) : this.fade(this.border, f9));
            f3 = 9.0f * f4;
            builder.text((float)(object[0] + (object[2] - GpuUiRenderer.textWidth(">", f3)) * 0.5f), (float)(object[1] + 5.0f * f4), f3, this.fade(this.primary, f9), ">", UiDrawCommand.TextWeight.SEMIBOLD);
            String string = Integer.toString(n4);
            builder.text((float)(object[0] + (object[2] - GpuUiRenderer.textWidth(string, 7.0f * f4)) * 0.5f), (float)(object[1] + 21.0f * f4), 7.0f * f4, this.fade(this.muted, f9), string);
        }
        if (this.libSlide > 0.985f) {
            return null;
        }
        object = HudEditorScreen.inRect(d2, d3, f6, f7, f5, f8) ? () -> {
            this.libSearchFocus = false;
        } : null;
        float f10 = 16.0f * f4;
        f3 = 14.0f * f4;
        float f11 = f6 + f5 - 10.0f * f4 - f10;
        float f12 = f7 + 7.0f * f4;
        if (builder != null) {
            builder.shadow(f6, f7, f5, f8, 12.0f * f4, 14.0f * f4, f4, 0x72000000);
            builder.gradient(f6, f7, f5, f8, 12.0f * f4, this.panelTop, this.panelBottom, this.panelBottom, this.panelTop);
            builder.border(f6, f7, f5, f8, 12.0f * f4, Math.max(0.7f, f4), this.border);
            builder.text(f6 + 12.0f * f4, f7 + 10.0f * f4, 8.5f * f4, this.primary, HudEditorScreen.nc("\u6596\ud36d\u1e3b\u2f2b\uebec\u6e06\ub3e0\u2b71\u94d7", -2012401233, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            String string = n4 + HudEditorScreen.nc("\u3ccc\u8d49\ua25b\u9627\uec8e", 1483186282, 0x1AFDE8A7 ^ CryptoVault.mask());
            builder.text(f11 - 8.0f * f4 - GpuUiRenderer.textWidth(string, 7.5f * f4), f7 + 11.0f * f4, 7.5f * f4, this.muted, string);
            this.drawSmall(builder, f11, f12, f10, f3, "<", HudEditorScreen.inRect(d2, d3, f11, f12, f10, f3), this.accent, f4);
        }
        float f13 = f6 + 10.0f * f4;
        float f14 = f7 + 27.0f * f4;
        float f15 = f5 - 20.0f * f4;
        float f16 = 20.0f * f4;
        if (builder != null) {
            String string;
            builder.rect(f13, f14, f15, f16, 4.0f * f4, this.libSearchFocus ? this.withAlpha(this.accent, 48) : this.withAlpha(this.muted, 48));
            builder.border(f13, f14, f15, f16, 4.0f * f4, Math.max(0.7f, f4), this.libSearchFocus ? this.withAlpha(this.accent, 190) : this.withAlpha(this.muted, 128));
            String string2 = string = this.libSearch.length() == 0 && !this.libSearchFocus ? HudEditorScreen.nc("\uc8d9\u4390\uc76b\u054d\ub3e6\u03da\u9914\uceec\u446e\u65e4\ucd1f\u4c38\u9f4a\u4591\ub72d\ua6f6\u3c3e", -437283292, 0x1AFDE8A7 ^ CryptoVault.mask()) : this.libSearch.toString();
            if (this.libSearchFocus && System.currentTimeMillis() / 500L % 2L == 0L) {
                string = string + "_";
            }
            builder.text(f13 + 7.0f * f4, f14 + (f16 - 8.0f * f4) * 0.5f - 0.3f, 8.0f * f4, this.libSearch.length() == 0 && !this.libSearchFocus ? this.muted : this.primary, HudEditorScreen.fit(string, f15 - 14.0f * f4, 8.0f * f4));
        }
        if (HudEditorScreen.inRect(d2, d3, f13, f14, f15, f16)) {
            object = () -> {
                this.libSearchFocus = true;
            };
        }
        if (HudEditorScreen.inRect(d2, d3, f11, f12, f10, f3)) {
            object = () -> {
                this.libCollapsed = true;
                this.libSearchFocus = false;
            };
        }
        float f17 = f7 + 54.0f * f4;
        float f18 = f8 - 54.0f * f4 - 8.0f * f4;
        float f19 = 22.0f * f4;
        float f20 = 9.0f * f4;
        float f21 = 0.0f;
        for (HUDElement hUDElement : list) {
            f21 += f19 + this.cardHeight(hUDElement) + f20;
        }
        this.libScroll = Math.max(0.0f, Math.min(this.libScroll, Math.max(0.0f, f21 - f18)));
        if (list.isEmpty() && builder != null) {
            builder.text(f6 + 12.0f * f4, f17 + 6.0f * f4, 8.0f * f4, this.secondary, HudEditorScreen.nc("\u00b5\u864a\u3dcd\u7dc3\u378e\ud43c\ud535\ub5f0\uc403\u8130\u2a39\u9017\u8cb2\u2e76\udfcd\u0296\uf713\u925f\ud65e\u7bbf", -1560611249, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }
        if (builder != null) {
            builder.clipPush(f6 + 3.0f * f4, f17, f5 - 6.0f * f4, f18, 8.0f * f4);
        }
        float f22 = f17 - this.libScroll;
        boolean bl = HudEditorScreen.inRect(d2, d3, f6, f17, f5, f18);
        for (HUDElement hUDElement : list) {
            f2 = this.cardHeight(hUDElement);
            float f23 = f19 + f2 + f20;
            if (f22 + f23 >= f17 && f22 <= f17 + f18) {
                boolean bl2;
                float f24 = 16.0f * f4;
                float f25 = f22 + 2.0f * f4;
                String string = hUDElement.isEditorHidden() ? HudEditorScreen.nc("\u47eb\u343e\u423a\u12e8\u86b7\uc121\u93d9\u3846", -1041777995, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\udd55\u8f67\u54c1\u168d\uce12\ue23d\u6d84", 1142193287, 0x1AFDE8A7 ^ CryptoVault.mask());
                float f26 = GpuUiRenderer.textWidth(string, 7.0f * f4) + 12.0f * f4;
                float f27 = 0.8f * f4;
                float f28 = 34.0f * f27;
                float f29 = 15.0f * f27;
                float f30 = f6 + f5 - 10.0f * f4 - f26;
                float f31 = f30 - 6.0f * f4 - f28;
                float f32 = f22 + 2.0f * f4 + (f24 - f29) * 0.5f;
                float f33 = f6 + 10.0f * f4;
                float f34 = f22 + f19;
                float f35 = f5 - 20.0f * f4;
                boolean bl3 = bl2 = bl && HudEditorScreen.inRect(d2, d3, f33, f34, f35, f2);
                if (builder != null) {
                    float f36 = hUDElement.isEnabled() ? 1.0f : 0.0f;
                    float f37 = this.libSwitchT.getOrDefault(hUDElement, Float.valueOf(f36)).floatValue();
                    if (Math.abs(f36 - (f37 += (f36 - f37) * this.ease(18.0f))) < 0.01f) {
                        f37 = f36;
                    }
                    this.libSwitchT.put(hUDElement, Float.valueOf(f37));
                    builder.text(f6 + 12.0f * f4, f22 + 5.0f * f4, 8.5f * f4, hUDElement.isEnabled() ? this.primary : this.secondary, HudEditorScreen.fit(hUDElement.getName(), f31 - f6 - 18.0f * f4, 8.5f * f4), UiDrawCommand.TextWeight.SEMIBOLD);
                    this.drawPill(builder, f31, f32, f37, f27);
                    this.drawSmall(builder, f30, f25, f26, f24, string, bl && HudEditorScreen.inRect(d2, d3, f30, f25, f26, f24), hUDElement.isEditorHidden() ? this.accent : this.secondary, f4);
                    builder.rect(f33, f34, f35, f2, 8.0f * f4, 0x66000000);
                    this.drawLibraryPreview(builder, hUDElement, f33, f34, f35, f2, f4, n2, n3, this.libSlide < 0.02f && f22 >= f17 && f22 + f19 + f2 <= f17 + f18);
                    builder.border(f33, f34, f35, f2, 8.0f * f4, bl2 ? 1.3f : Math.max(0.7f, f4), this.withAlpha(bl2 ? this.accent : this.border, bl2 ? 192 : 96));
                }
                if (bl) {
                    if (HudEditorScreen.inRect(d2, d3, f31 - 3.0f * f4, f25, f28 + 6.0f * f4, f24) || bl2) {
                        object = () -> this.toggleFromLibrary(hUDElement);
                    } else if (HudEditorScreen.inRect(d2, d3, f30, f25, f26, f24)) {
                        object = () -> {
                            hUDElement.setEditorHidden(!hUDElement.isEditorHidden());
                            if (hUDElement.isEditorHidden() && this.selected == hUDElement) {
                                this.selected = null;
                            }
                            this.multi.remove(hUDElement);
                        };
                    }
                }
            }
            f22 += f23;
        }
        if (builder != null) {
            builder.clipPop();
        }
        if (builder != null && f21 > f18) {
            float f38 = f18;
            float f39 = Math.max(18.0f * f4, f38 * f18 / f21);
            f2 = f17 + (f38 - f39) * (this.libScroll / Math.max(1.0f, f21 - f18));
            builder.rect(f6 + f5 - 5.0f * f4, f2, 2.5f * f4, f39, 1.2f * f4, this.withAlpha(this.accent, 144));
        }
        return object;
    }

    private float cardHeight(HUDElement hUDElement) {
        float f2 = this.chromeScale(this.field_22789, this.field_22790);
        if (!HudEditorScreen.previewable(hUDElement)) {
            return 30.0f * f2;
        }
        return Math.max(30.0f, Math.min(78.0f, (float)hUDElement.getHeight() + 14.0f));
    }

    private void drawSmall(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, String string, boolean bl, int n2, float f6) {
        builder.pill(f2, f3, f4, f5, this.withAlpha(n2, bl ? 72 : 34));
        builder.border(f2, f3, f4, f5, f5 * 0.5f, Math.max(0.6f, f6), this.withAlpha(n2, bl ? 176 : 112));
        float f7 = GpuUiRenderer.textWidth(string, 7.0f * f6);
        builder.text(f2 + (f4 - f7) * 0.5f, f3 + (f5 - 7.0f * f6) * 0.5f - 0.4f, 7.0f * f6, this.primary, string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private void toggleFromLibrary(HUDElement hUDElement) {
        this.history.checkpoint();
        boolean bl = !hUDElement.isEnabled();
        hUDElement.setEnabled(bl);
        if (bl) {
            hUDElement.setEditorHidden(false);
            this.selected = hUDElement;
            this.multi.clear();
            this.clampToScreen(hUDElement);
            this.showHint(hUDElement.getName() + HudEditorScreen.nc("\u037b\u45d8\u9baa\u6b16\u8efe\u47f5\u5701\uc4c3", -1202243991, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void drawLibraryPreview(UiDisplayList.Builder builder, HUDElement hUDElement, float f2, float f3, float f4, float f5, float f6, int n2, int n3, boolean bl) {
        if (!HudEditorScreen.previewable(hUDElement)) {
            String string = HudEditorScreen.nc("\u73a2\u5a5e\u0304\u6bd7\uba51\u9b93\u470a\u69a5\u804d\u1505\u596c\u5cc8\u5ff6\u6552\uac95\u5344\u8b4b\u1da5\ud0e9\u6144\u94bf\udd79\u6f38", -363865979, 0x1AFDE8A7 ^ CryptoVault.mask());
            float f7 = GpuUiRenderer.textWidth(string, 7.5f * f6);
            builder.text(f2 + (f4 - f7) * 0.5f, f3 + (f5 - 7.5f * f6) * 0.5f, 7.5f * f6, this.muted, string);
            return;
        }
        double d2 = hUDElement.getX();
        double d3 = hUDElement.getY();
        float f8 = f2 + 8.0f;
        float f9 = f3 + 7.0f;
        builder.clipPush(f2 + 1.0f, f3 + 1.0f, f4 - 2.0f, f5 - 2.0f, 8.0f * f6);
        hUDElement.setX(f8);
        hUDElement.setY(f9);
        try {
            GpuHudRenderer.appendTo(builder, List.of(hUDElement), n2, n3, 0.0f, true);
        }
        catch (Throwable throwable) {
        }
        finally {
            hUDElement.setX(d2);
            hUDElement.setY(d3);
        }
        builder.clipPop();
        if (bl && hUDElement instanceof ItemIconWidget) {
            ItemIconWidget itemIconWidget = (ItemIconWidget)hUDElement;
            this.libIconJobs.add(new Object[]{itemIconWidget, Float.valueOf(f8), Float.valueOf(f9), Float.valueOf(f3 + f5 - 2.0f)});
        }
    }

    private void runIconJobs(class_332 class_3322) {
        for (Object[] objectArray : this.libIconJobs) {
            ((ItemIconWidget)objectArray[0]).renderItemIconsAt(class_3322, ((Float)objectArray[1]).floatValue(), ((Float)objectArray[2]).floatValue(), ((Float)objectArray[3]).floatValue());
        }
        this.libIconJobs.clear();
    }

    private static int widgetCount(List<HUDElement> list) {
        int n2 = 0;
        for (HUDElement hUDElement : list) {
            if (!HUDManager.isWidget(hUDElement)) continue;
            ++n2;
        }
        return n2;
    }

    private void drawHover(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3, float f2) {
        float f3 = GpuHudRenderer.editorX(hUDElement, n2, n3);
        float f4 = GpuHudRenderer.editorY(hUDElement, n2, n3);
        float f5 = GpuHudRenderer.editorWidth(hUDElement, n2, n3);
        float f6 = GpuHudRenderer.editorHeight(hUDElement, n2, n3);
        float f7 = Math.min(14.0f, f6 * 0.5f + 2.0f);
        builder.rect(f3 - 1.0f, f4 - 1.0f, f5 + 2.0f, f6 + 2.0f, f7, this.withAlpha(this.accent, Math.round(16.0f * f2)));
        builder.border(f3 - 1.0f, f4 - 1.0f, f5 + 2.0f, f6 + 2.0f, f7, 1.2f, this.withAlpha(this.accent, Math.round(152.0f * f2)));
    }

    private void drawSelection(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3) {
        String string;
        float f2;
        int n4;
        float f3;
        boolean bl;
        float f4 = this.chromeScale(n2, n3);
        float f5 = GpuHudRenderer.editorX(hUDElement, n2, n3);
        float f6 = GpuHudRenderer.editorY(hUDElement, n2, n3);
        float f7 = GpuHudRenderer.editorWidth(hUDElement, n2, n3);
        float f8 = GpuHudRenderer.editorHeight(hUDElement, n2, n3);
        boolean bl2 = bl = this.dragging != null || this.scalingEl != null;
        if (this.animFor != hUDElement) {
            this.selFade = this.still() ? 1.0f : 0.0f;
            this.animFor = hUDElement;
            this.animX = f5;
            this.animY = f6;
            this.animW = f7;
            this.animH = f8;
        } else if (bl || this.still()) {
            this.animX = f5;
            this.animY = f6;
            this.animW = f7;
            this.animH = f8;
        } else {
            f3 = this.ease(24.0f);
            this.animX += (f5 - this.animX) * f3;
            this.animY += (f6 - this.animY) * f3;
            this.animW += (f7 - this.animW) * f3;
            this.animH += (f8 - this.animH) * f3;
        }
        this.selFade += (1.0f - this.selFade) * this.ease(14.0f);
        f3 = this.selFade;
        float f9 = this.animX;
        float f10 = this.animY;
        float f11 = this.animW;
        float f12 = this.animH;
        boolean bl3 = this.dragging == hUDElement;
        float f13 = Math.min(14.0f, f12 * 0.5f + 2.0f);
        builder.shadow(f9 - 2.0f, f10 - 2.0f, f11 + 4.0f, f12 + 4.0f, f13, bl3 ? 22.0f : 14.0f, 0.0f, this.withAlpha(this.accent, Math.round((float)(bl3 ? 102 : 56) * f3)));
        builder.rect(f9 - 2.0f, f10 - 2.0f, f11 + 4.0f, f12 + 4.0f, f13, this.withAlpha(this.accent, Math.round((float)(bl3 ? 36 : 20) * f3)));
        builder.border(f9 - 2.0f, f10 - 2.0f, f11 + 4.0f, f12 + 4.0f, f13, bl3 ? 1.8f : 1.5f, this.withAlpha(this.accent, Math.round(240.0f * f3)));
        float f14 = Math.max(4.0f, 5.0f * f4);
        boolean bl4 = !bl && !hUDElement.isLocked() && HUDManager.isWidget(hUDElement);
        float[][] fArrayArray = new float[][]{{f9 - 2.0f, f10 - 2.0f}, {f9 + f11 + 2.0f, f10 - 2.0f}, {f9 - 2.0f, f10 + f12 + 2.0f}, {f9 + f11 + 2.0f, f10 + f12 + 2.0f}};
        for (n4 = 0; n4 < 4; ++n4) {
            if (n4 == 3 && bl4) continue;
            float f15 = fArrayArray[n4][0] - f14 * 0.5f;
            f2 = fArrayArray[n4][1] - f14 * 0.5f;
            builder.rect(f15, f2, f14, f14, f14 * 0.3f, this.withAlpha(0xFFFFFF, Math.round(240.0f * f3)));
            builder.border(f15, f2, f14, f14, f14 * 0.3f, 1.0f, this.withAlpha(this.accent, Math.round(255.0f * f3)));
        }
        if (bl4) {
            this.drawHandle(builder, hUDElement, n2, n3);
        }
        n4 = (int)Math.round(hUDElement.getScale() * 100.0);
        if (this.multi.size() > 1) {
            string = this.multi.size() + HudEditorScreen.nc("\u2b0a\u8632\uf767\u980f\ub17f\ub757\uc87c\u9e53\ub7db\u1180\uff01\ubca7\u1d61", -1587398097, 0x1AFDE8A7 ^ CryptoVault.mask());
        } else {
            string = hUDElement.getName();
            string = this.dragging == hUDElement ? string + HudEditorScreen.nc("\u8394\u7219\uc607\u57ff\u53a5\u85e7\u5365", -675947695, 0x1AFDE8A7 ^ CryptoVault.mask()) + Math.round(hUDElement.getX()) + HudEditorScreen.nc("\u0f08\uacda\ud4ba\udbf4\uf962", 2082898366, 0x1AFDE8A7 ^ CryptoVault.mask()) + Math.round(hUDElement.getY()) : string + HudEditorScreen.nc("\u2c43\u1845\ue35a\u5de4\u3b62\uc241", 342705570, 0x1AFDE8A7 ^ CryptoVault.mask()) + Math.round(hUDElement.getWidth() * hUDElement.getScale()) + HudEditorScreen.nc("\u32c0\ue951\u9785\u27d7\ua2ef\u22d2\u68d2", -133215512, 0x1AFDE8A7 ^ CryptoVault.mask()) + Math.round(hUDElement.getHeight() * hUDElement.getScale());
            if (n4 != 100) {
                string = string + HudEditorScreen.nc("\u6a5e\ua7ac\u3c23\u46a1\ufb68\uea88\u66e6", -1075024069, 0x1AFDE8A7 ^ CryptoVault.mask()) + n4 + "%";
            }
            if (hUDElement.isLocked()) {
                string = string + HudEditorScreen.nc("\u8741\u09a9\uad46\u2fe5\u1b77\ufae8\u53b7\u1205\u21a1\u976d\u265f\ue1b3\ucb48\u5af1\u2f0d", -1030221700, 0x1AFDE8A7 ^ CryptoVault.mask());
            }
        }
        f2 = 7.6f * f4;
        float f16 = 15.0f * f4;
        String string2 = HudEditorScreen.fit(string, (float)n2 - 24.0f * f4, f2);
        float f17 = GpuUiRenderer.textWidth(string2, f2) + 14.0f * f4;
        float f18 = Math.max(4.0f * f4, Math.min((float)n2 - f17 - 4.0f * f4, f9 + f11 * 0.5f - f17 * 0.5f));
        float f19 = f10 - f16 - 8.0f * f4;
        if (f19 < 4.0f * f4) {
            f19 = f10 + f12 + 8.0f * f4;
        }
        if (f19 + f16 > (float)n3 - 4.0f * f4) {
            f19 = Math.max(4.0f * f4, f10 + 4.0f * f4);
        }
        builder.shadow(f18, f19, f17, f16, f16 * 0.5f, 8.0f, 0.0f, this.withAlpha(0, Math.round(96.0f * f3)));
        builder.pill(f18, f19, f17, f16, this.withAlpha(this.accent, Math.round(230.0f * f3)));
        builder.text(f18 + 7.0f * f4, f19 + (f16 - f2) * 0.5f - 0.4f, f2, this.fade(HudEditorScreen.contrastOn(this.accent), f3), string2, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private void drawGrid(UiDisplayList.Builder builder, int n2, int n3) {
        int n4;
        float f2 = GpuHudRenderer.viewportScale(n2, n3);
        float f3 = 16.0f * f2;
        float f4 = this.gridOn ? 1.0f : 0.0f;
        boolean bl = this.gridOn && this.gridAmt > 0.02f;
        int n5 = this.withAlpha(this.gridTint, Math.round(22.0f * this.gridAmt));
        int n6 = this.withAlpha(this.gridTint, Math.round((18.0f + 26.0f * this.gridAmt) * f4));
        int n7 = this.withAlpha(this.gridTint, Math.round((64.0f + 48.0f * this.gridAmt) * f4));
        int n8 = 0;
        if (this.gridOn) {
            float f5 = 0.0f;
            while (f5 <= (float)n2) {
                int n9 = Math.round(f5);
                if (n8 % 4 == 0) {
                    builder.rect(n9, 0.0f, 1.0f, n3, 0.0f, n6);
                } else if (bl) {
                    builder.rect(n9, 0.0f, 1.0f, n3, 0.0f, n5);
                }
                f5 += f3;
                ++n8;
            }
            n4 = 0;
            float f6 = 0.0f;
            while (f6 <= (float)n3) {
                int n10 = Math.round(f6);
                if (n4 % 4 == 0) {
                    builder.rect(0.0f, n10, n2, 1.0f, 0.0f, n6);
                } else if (bl) {
                    builder.rect(0.0f, n10, n2, 1.0f, 0.0f, n5);
                }
                f6 += f3;
                ++n4;
            }
            for (f6 = 0.0f; f6 <= (float)n3; f6 += f3 * 4.0f) {
                for (float f7 = 0.0f; f7 <= (float)n2; f7 += f3 * 4.0f) {
                    builder.circle((float)Math.round(f7) + 0.5f, (float)Math.round(f6) + 0.5f, 1.5f, n7);
                }
            }
        }
        n4 = this.withAlpha(this.accent, Math.round(46.0f + 56.0f * this.gridAmt));
        builder.rect((float)n2 * 0.5f - 0.5f, 0.0f, 1.0f, n3, 0.0f, n4);
        builder.rect(0.0f, (float)n3 * 0.5f - 0.5f, n2, 1.0f, 0.0f, n4);
    }

    private void drawGuides(UiDisplayList.Builder builder, int n2, int n3) {
        int n4 = this.withAlpha(this.accent, 176);
        if (!Float.isNaN(this.guideX)) {
            builder.rect(Math.round(this.guideX), 0.0f, 1.0f, n3, 0.0f, n4);
        }
        if (!Float.isNaN(this.guideY)) {
            builder.rect(0.0f, Math.round(this.guideY), n2, 1.0f, 0.0f, n4);
        }
    }

    private void snapDragged(HUDElement hUDElement, int n2, int n3, float f2) {
        this.guideX = Float.NaN;
        this.guideY = Float.NaN;
        float f3 = (float)hUDElement.getWidth() * (float)hUDElement.getScale();
        float f4 = (float)hUDElement.getHeight() * (float)hUDElement.getScale();
        double d2 = (float)Math.round(hUDElement.getX() / 8.0) * 8.0f;
        double d3 = (float)Math.round(hUDElement.getY() / 8.0) * 8.0f;
        double d4 = 4.001;
        double d5 = 4.001;
        double[] dArray = new double[]{(double)n2 * 0.5 - (double)f3 * 0.5, 0.0, (float)n2 - f3};
        double[] dArray2 = new double[]{(double)n3 * 0.5 - (double)f4 * 0.5, 0.0, (float)n3 - f4};
        double[] dArray3 = new double[]{(double)n2 * 0.5, 0.0, n2};
        double[] dArray4 = new double[]{(double)n3 * 0.5, 0.0, n3};
        double d6 = d2;
        double d7 = d3;
        double d8 = Double.NaN;
        double d9 = Double.NaN;
        for (int i2 = 0; i2 < 3; ++i2) {
            double d10 = Math.abs(hUDElement.getX() - dArray[i2]);
            if (d10 < d4) {
                d4 = d10;
                d6 = dArray[i2];
                d8 = dArray3[i2];
            }
            if (!((d10 = Math.abs(hUDElement.getY() - dArray2[i2])) < d5)) continue;
            d5 = d10;
            d7 = dArray2[i2];
            d9 = dArray4[i2];
        }
        for (HUDElement hUDElement2 : HUDManager.getInstance().getElements()) {
            if (hUDElement2 == hUDElement || hUDElement2 instanceof TargetBoxMetricsWidget55572 || !HUDManager.isWidget(hUDElement2) || hUDElement2.getX() < 0.0 || hUDElement2.getWidth() <= 1.0) continue;
            double d11 = hUDElement2.getX();
            double d12 = hUDElement2.getY();
            double d13 = hUDElement2.getWidth() * hUDElement2.getScale();
            double d14 = hUDElement2.getHeight() * hUDElement2.getScale();
            double[] dArray5 = new double[]{d11, d11 + d13, d11 + d13 * 0.5};
            double[] dArray6 = new double[]{hUDElement.getX(), hUDElement.getX() + (double)f3, hUDElement.getX() + (double)f3 * 0.5};
            for (int i3 = 0; i3 < 3; ++i3) {
                double d15 = Math.abs(dArray6[i3] - dArray5[i3]);
                if (!(d15 < d4)) continue;
                d4 = d15;
                d6 = hUDElement.getX() + (dArray5[i3] - dArray6[i3]);
                d8 = dArray5[i3];
            }
            double[] dArray7 = new double[]{d12, d12 + d14, d12 + d14 * 0.5};
            double[] dArray8 = new double[]{hUDElement.getY(), hUDElement.getY() + (double)f4, hUDElement.getY() + (double)f4 * 0.5};
            for (int i4 = 0; i4 < 3; ++i4) {
                double d16 = Math.abs(dArray8[i4] - dArray7[i4]);
                if (!(d16 < d5)) continue;
                d5 = d16;
                d7 = hUDElement.getY() + (dArray7[i4] - dArray8[i4]);
                d9 = dArray7[i4];
            }
        }
        hUDElement.setX(d6);
        hUDElement.setY(d7);
        if (!Double.isNaN(d8)) {
            this.guideX = (float)d8 * f2;
        }
        if (!Double.isNaN(d9)) {
            this.guideY = (float)d9 * f2;
        }
    }

    private float chromeScale(int n2, int n3) {
        float f2 = 1.0f;
        if (this.field_22787 != null && this.field_22787.method_22683() != null && this.field_22787.method_22683().method_4486() > 0) {
            f2 = (float)this.field_22787.method_22683().method_4489() / (float)this.field_22787.method_22683().method_4486();
        }
        return ResponsiveLayout.hudChromeScale(GpuHudRenderer.viewportScale(n2, n3), (float)n3 * f2, f2, false);
    }

    private void layoutHeader(int n2, int n3) {
        float f2 = this.chromeScale(n2, n3);
        float f3 = GpuUiRenderer.textWidth(HudEditorScreen.nc("\u4f51\u846d\u60e5\ud834\u2ea6\ud6a2\u6888\uca2d\u6209\u1b02\uf5b2", 607274701, 0x1AFDE8A7 ^ CryptoVault.mask()), 9.5f * f2);
        this.btnH = 20.0f * f2;
        this.hdrH = 30.0f * f2;
        this.styleW = GpuUiRenderer.textWidth(HudEditorScreen.nc("\uef24\uff1c\uf786\u5d49\u91f2\u2d6a\u5b7c\u8b6c\ud8ee\uf9a4\u60d8", -77282181, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2) + 22.0f * f2;
        this.layoutsW = GpuUiRenderer.textWidth(HudEditorScreen.nc("\u3682\ub9e3\u1db8\uece9\u0ecb\u469a\u572d\u1433", -1762501077, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2) + 22.0f * f2;
        this.alignW = GpuUiRenderer.textWidth(HudEditorScreen.nc("\u0e0e\u665f\u3d0e\u92b4\uaed3\ue9bb\u9f49", 1139526684, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2) + 22.0f * f2;
        this.resetW = Math.max(GpuUiRenderer.textWidth(HudEditorScreen.nc("\u9870\u2575\u832f\ub739\ucfba\ubea9\uc173\ucddc\u6d53\ub9c8\ube4b\u3f0e\uda83\u4be9\uc03f\u3dcd", -345140169, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2), GpuUiRenderer.textWidth(HudEditorScreen.nc("\u1224\uad54\u9e6f\u73d3\ucbdb\ube7f\u5aaa\u738d\u5478\u2bd9\u4a13\u16fc\u0d06\ue71f\uf4ad\u6a9d\u1aa8", -1963730922, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2)) + 20.0f * f2;
        this.doneW = 44.0f * f2;
        this.libBtnW = GpuUiRenderer.textWidth(HudEditorScreen.nc("\ud4fb\u2b48\u3eca\u86bf\u4c7f\u2033\u9360\u6c18\u50bb\u8f11\u3160\ua501", -525500411, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f2) + 22.0f * f2;
        float f4 = this.libBtnW + 6.0f * f2 + this.styleW + 6.0f * f2 + this.layoutsW + 6.0f * f2 + this.alignW + 6.0f * f2 + this.resetW + 6.0f * f2 + this.doneW + 6.0f * f2;
        float f5 = 23.0f * f2 + f3 + 16.0f * f2 + f4;
        this.showTitle = f5 <= (float)n2 - 24.0f * f2;
        this.hdrW = Math.min(this.showTitle ? f5 : 14.0f * f2 + f4, (float)n2 - 24.0f * f2);
        this.hdrX = ((float)n2 - this.hdrW) * 0.5f;
        this.hdrY = 12.0f * f2 - this.chromeHide * (this.hdrH + 24.0f * f2);
        this.btnY = this.hdrY + (this.hdrH - this.btnH) * 0.5f;
        this.doneX = this.hdrX + this.hdrW - this.doneW - 6.0f * f2;
        this.resetX = this.doneX - 6.0f * f2 - this.resetW;
        this.alignX = this.resetX - 6.0f * f2 - this.alignW;
        this.layoutsX = this.alignX - 6.0f * f2 - this.layoutsW;
        this.styleX = this.layoutsX - 6.0f * f2 - this.styleW;
        this.libBtnX = this.styleX - 6.0f * f2 - this.libBtnW;
    }

    private boolean resetArmed() {
        return System.currentTimeMillis() < this.resetArmedUntil;
    }

    private void drawHeader(UiDisplayList.Builder builder, int n2, int n3, int n4, int n5) {
        float f2 = this.chromeScale(n2, n3);
        builder.shadow(this.hdrX, this.hdrY + 2.0f * f2, this.hdrW, this.hdrH, this.hdrH * 0.5f, 20.0f * f2, 1.0f, 0x78000000);
        builder.gradient(this.hdrX, this.hdrY, this.hdrW, this.hdrH, this.hdrH * 0.5f, this.panelTop, this.panelBottom, this.panelBottom, this.panelTop);
        builder.border(this.hdrX, this.hdrY, this.hdrW, this.hdrH, this.hdrH * 0.5f, Math.max(0.9f, f2 * 1.1f), this.border);
        if (this.showTitle) {
            builder.circle(this.hdrX + 14.0f * f2, this.hdrY + this.hdrH * 0.5f, 2.8f * f2, this.accent);
            float f3 = this.hdrY + (this.hdrH - 9.5f * f2) * 0.5f - 0.5f;
            builder.text(this.hdrX + 23.0f * f2, f3, 9.5f * f2, this.primary, HudEditorScreen.nc("\u4acc\uaa28\ud264\ubb1d\u7c1c\u5ef2\ue3dd\ude9b\u2dd1\u9f90\ua381\u82e4", -1589463121, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        }
        this.drawButton(builder, this.libBtnX, this.btnY, this.libBtnW, this.btnH, HudEditorScreen.nc("\u1acf\ubfa4\u6f22\uec15\u5aea\uf9a5\uba95\u0c68\u17ba", -482454292, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.libBtnX, this.btnY, this.libBtnW, this.btnH) || this.libraryOpen, this.accent, f2);
        boolean bl = this.settingsElement instanceof HitboxDrawPresentationStyle264081;
        this.drawButton(builder, this.styleX, this.btnY, this.styleW, this.btnH, HudEditorScreen.nc("\uf201\ud9ea\ubfb9\u4c4b\u3b67\ud755\ua0d4\uc40c\ufb10", -113358611, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.styleX, this.btnY, this.styleW, this.btnH) || bl, this.accent, f2);
        this.drawButton(builder, this.layoutsX, this.btnY, this.layoutsW, this.btnH, HudEditorScreen.nc("\u9d2a\uf98e\u8826\u0845\ue3e1\u83f0\ud93c\u4ddc\u2aca\u2ec1\u838e", -757618850, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.layoutsX, this.btnY, this.layoutsW, this.btnH) || this.layoutsOpen, this.accent, f2);
        this.drawButton(builder, this.alignX, this.btnY, this.alignW, this.btnH, HudEditorScreen.nc("\ucafd\u9379\udaae\ua857\ub754\u7592\u2b61", -519163067, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.alignX, this.btnY, this.alignW, this.btnH) || this.alignOpen, this.accent, f2);
        boolean bl2 = this.resetArmed();
        this.drawButton(builder, this.resetX, this.btnY, this.resetW, this.btnH, bl2 ? HudEditorScreen.nc("\ub162\u3ab1\u21ce\ueaee\u44e7\uedb3\u1d97\u2954\uadbc\u5bc6\u8ade\u19b9\uda15\u32db\u60a1\u8886\ue02f\u1ae4\uae6e", -2010991086, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u58a3\uf62e\u86f0\uff08\ubae0\u85ca\u60f9\u3260\u9351\ucf95\u00b4\ub482\u6d04\ua85c\uf790", 351127328, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.resetX, this.btnY, this.resetW, this.btnH) || bl2, bl2 ? -38037 : this.secondary, f2);
        this.drawButton(builder, this.doneX, this.btnY, this.doneW, this.btnH, HudEditorScreen.nc("\u52d3\u857f\u64c9\ua8f3\ueb89\u8cc4\u3291", 341854068, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(n4, n5, this.doneX, this.btnY, this.doneW, this.btnH), this.accent, f2);
    }

    private float panelsTop(float f2) {
        return this.hdrY + this.hdrH + 8.0f * f2;
    }

    private int layoutRowsVisible(int n2, int n3, float f2) {
        float f3 = (float)n3 - this.panelsTop(f2) - 10.0f * f2 - 28.0f * f2 - 38.0f * f2;
        return Math.max(1, Math.min(Math.max(1, n2), (int)(f3 / (22.0f * f2))));
    }

    private Runnable layoutsPanel(UiDisplayList.Builder builder, int n2, int n3, double d2, double d3) {
        float f2;
        float f3;
        float f4;
        float f5;
        Runnable runnable;
        float f6 = this.chromeScale(n2, n3);
        List<String> list = ClientPrefs.get().hudLayoutNames();
        int n4 = this.layoutRowsVisible(list.size(), n3, f6);
        this.layoutsScroll = Math.max(0, Math.min(this.layoutsScroll, Math.max(0, list.size() - n4)));
        float f7 = Math.min(230.0f * f6, (float)n2 - 20.0f * f6);
        float f8 = Math.max(10.0f * f6, Math.min((float)n2 - f7 - 10.0f * f6, this.layoutsX + this.layoutsW * 0.5f - f7 * 0.5f));
        Runnable runnable2 = runnable = HudEditorScreen.inRect(d2, d3, f8, f5 = this.panelsTop(f6), f7, f4 = 28.0f * f6 + (float)n4 * 22.0f * f6 + 38.0f * f6) ? () -> {} : null;
        if (builder != null) {
            builder.shadow(f8, f5, f7, f4, 12.0f * f6, 12.0f * f6, f6, 0x72000000);
            builder.gradient(f8, f5, f7, f4, 12.0f * f6, this.panelTop, this.panelBottom, this.panelBottom, this.panelTop);
            builder.border(f8, f5, f7, f4, 12.0f * f6, Math.max(0.7f, f6), this.border);
            builder.text(f8 + 12.0f * f6, f5 + 10.0f * f6, 8.5f * f6, this.primary, HudEditorScreen.nc("\u4317\uc7a9\u8e6b\u91f7\u8d31\u946d\u9635\u3c12\ucb4a\uaba2\u8a89\u1b5c\u0e43\u5289\ubc5d", 1645807266, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            String string = list.size() + "/" + 16;
            builder.text(f8 + f7 - 12.0f * f6 - GpuUiRenderer.textWidth(string, 7.5f * f6), f5 + 11.0f * f6, 7.5f * f6, this.muted, string);
        }
        if (list.isEmpty() && builder != null) {
            builder.text(f8 + 12.0f * f6, f5 + 33.0f * f6, 8.0f * f6, this.secondary, HudEditorScreen.nc("\ud598\u45aa\u6394\u6fa8\ua553\ue778\u7cca\ua97b\u9fc2\u8121\ucc6d\uc9ed\u7a47\ueb5f\u2933\u1f1a\uea43\u6a76\ud0ad\uc2b2\u5bc6\u7b35\ufb0a\u8d54\u409d\u6bcb\uff37\u1c10\ub8cb\ud785\u5683\u37ee\u604d\ucd8a\u4fa4\u9a8f\u2831\u3783\u3ed3\u5508\uc17e\uae6a\uc7c8\uf3a4\u5a28\u5ed5\u060f", -982452512, 0x1AFDE8A7 ^ CryptoVault.mask()));
        }
        long l2 = System.currentTimeMillis();
        for (int i2 = 0; i2 < n4 && this.layoutsScroll + i2 < list.size(); ++i2) {
            String string = list.get(this.layoutsScroll + i2);
            f3 = f5 + 28.0f * f6 + (float)i2 * 22.0f * f6;
            boolean bl = string.equals(this.armedDeleteLayout) && l2 < this.armedDeleteUntil;
            String string2 = bl ? HudEditorScreen.nc("\u1f6b\u44f9\u41c2\ue0d6\u97b3\u391c\udf9f", -337855468, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\ua3c3\u4144\uec4e\u3632\u7a29\ua6a8\u7cb4", 1347909207, 0x1AFDE8A7 ^ CryptoVault.mask());
            f2 = Math.max(GpuUiRenderer.textWidth(HudEditorScreen.nc("\u9899\u5b77\u654f\u86c6\uac80\u1167\u3a45", 681044043, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f6), GpuUiRenderer.textWidth(HudEditorScreen.nc("\u95b3\ude9d\uf08e\u0f39\ub40e", -1505751881, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f6)) + 14.0f * f6;
            float f9 = GpuUiRenderer.textWidth(HudEditorScreen.nc("\uf8c5\u2bf9\u1c55\uef8c\u1d08\ub96c\ub82e", -1190010557, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f6) + 14.0f * f6;
            float f10 = 17.0f * f6;
            float f11 = f3 + 2.5f * f6;
            float f12 = f8 + f7 - 10.0f * f6 - f2;
            float f13 = f12 - 5.0f * f6 - f9;
            if (builder != null) {
                if (i2 > 0) {
                    builder.rect(f8 + 10.0f * f6, f3, f7 - 20.0f * f6, Math.max(0.7f, f6), 0.0f, this.withAlpha(this.border, 48));
                }
                builder.text(f8 + 12.0f * f6, f3 + 7.0f * f6, 8.5f * f6, this.primary, HudEditorScreen.fit(string, f13 - f8 - 20.0f * f6, 8.5f * f6));
                this.drawButton(builder, f13, f11, f9, f10, HudEditorScreen.nc("\u30e4\u1a61\ud241\ue775\u7877\u0184\ube7f\ue155", 861467210, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(d2, d3, f13, f11, f9, f10), this.accent, f6);
                this.drawButton(builder, f12, f11, f2, f10, string2, HudEditorScreen.inRect(d2, d3, f12, f11, f2, f10) || bl, bl ? -38037 : this.secondary, f6);
            }
            if (HudEditorScreen.inRect(d2, d3, f13, f11, f9, f10)) {
                runnable = () -> this.applyLayoutPreset(string);
                continue;
            }
            if (!HudEditorScreen.inRect(d2, d3, f12, f11, f2, f10)) continue;
            runnable = () -> {
                if (bl) {
                    ClientPrefs.get().removeHudLayout(string);
                    this.armedDeleteLayout = null;
                    this.showHint(HudEditorScreen.nc("\u5c6f\u96f7\uf3df\u91a5\ub430\u892f\u3d43\uf852\u04ed\u3dc6\u3c10\u7fb0\u9d7e\ue439\u6065\ub531\u62a9\u17ad\uba53", 652435349, 0x1AFDE8A7 ^ CryptoVault.mask()) + string + "\"");
                } else {
                    this.armedDeleteLayout = string;
                    this.armedDeleteUntil = System.currentTimeMillis() + 3000L;
                }
            };
        }
        float f14 = f5 + 28.0f * f6 + (float)n4 * 22.0f * f6 + 7.0f * f6;
        float f15 = 22.0f * f6;
        f3 = GpuUiRenderer.textWidth(HudEditorScreen.nc("\ua26d\ua342\u0f96\ud69b\ua0e2\ue379\u4a71", 1693667486, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.5f * f6) + 18.0f * f6;
        float f16 = f8 + 10.0f * f6;
        float f17 = f7 - 20.0f * f6 - f3 - 6.0f * f6;
        f2 = f16 + f17 + 6.0f * f6;
        if (builder != null) {
            String string;
            builder.rect(f16, f14, f17, f15, 4.0f * f6, this.editingLayoutName ? this.withAlpha(this.accent, 48) : this.withAlpha(this.muted, 48));
            builder.border(f16, f14, f17, f15, 4.0f * f6, Math.max(0.7f, f6), this.editingLayoutName ? this.withAlpha(this.accent, 190) : this.withAlpha(this.muted, 128));
            String string3 = string = this.layoutName.isEmpty() && !this.editingLayoutName ? HudEditorScreen.nc("\u0564\u6f16\u8cad\u50b6\u7667\u8c52\ue9d0\ub6b8\u0736\u68d7\u4f4d\u51c7\u5cf2\u120c\u3337\u8c0a\ud891\u6583", -1109590266, 0x1AFDE8A7 ^ CryptoVault.mask()) : this.layoutName.toString();
            if (this.editingLayoutName && System.currentTimeMillis() / 500L % 2L == 0L) {
                string = string + "_";
            }
            builder.text(f16 + 6.0f * f6, f14 + (f15 - 8.0f * f6) * 0.5f - 0.3f, 8.0f * f6, this.layoutName.isEmpty() && !this.editingLayoutName ? this.muted : this.primary, HudEditorScreen.fit(string, f17 - 12.0f * f6, 8.0f * f6));
            this.drawButton(builder, f2, f14, f3, f15, HudEditorScreen.nc("\u4ace\uf796\ubfcf\ucf65\ud466\u9a5f\uab72\uf444", 761281544, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.inRect(d2, d3, f2, f14, f3, f15), this.accent, f6);
        }
        if (HudEditorScreen.inRect(d2, d3, f16, f14, f17, f15)) {
            runnable = () -> {
                this.editingLayoutName = true;
            };
        } else if (HudEditorScreen.inRect(d2, d3, f2, f14, f3, f15)) {
            runnable = this::saveLayoutPreset;
        }
        return runnable;
    }

    private void saveLayoutPreset() {
        List<String> list = ClientPrefs.get().hudLayoutNames();
        String string = this.layoutName.toString().trim();
        if (string.isEmpty()) {
            int n2 = list.size() + 1;
            while (list.contains(string = HudEditorScreen.nc("\u77f5\ua7dd\ue70a\u8a14\u959b\u04d0\uc926\u27c0\uc8be\ubf6e\uac23", 1460977316, 0x1AFDE8A7 ^ CryptoVault.mask()) + n2++)) {
            }
        }
        JsonObject jsonObject = new JsonObject();
        for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
            if (!HUDManager.isWidget(hUDElement)) continue;
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("x", (Number)hUDElement.getX());
            jsonObject2.addProperty("y", (Number)hUDElement.getY());
            jsonObject2.addProperty(HudEditorScreen.nc("\u45c5\u20dd\u8b1b\u2968\u2d5f\u1e18\u5e96\u09a0\uadad\u1845\uf079", 1506144282, 0x1AFDE8A7 ^ CryptoVault.mask()), Boolean.valueOf(hUDElement.isEnabled()));
            jsonObject2.addProperty(HudEditorScreen.nc("\ue1dc\ub500\uf328\u39c4\u4bfc\ua4b8", 246919166, 0x1AFDE8A7 ^ CryptoVault.mask()), (Number)hUDElement.getScale());
            jsonObject.add(hUDElement.getName(), (JsonElement)jsonObject2);
        }
        boolean bl = list.contains(string);
        if (!ClientPrefs.get().putHudLayout(string, jsonObject)) {
            this.showHint(HudEditorScreen.nc("\u94cf\u7c02\u6d93\uabaf\ub622\ua937\u5673\uc13d\u7b22\u424d\uc442\ue1cf\u04aa\u041c\u7dad\u0658\uca62\uf9b6\u3bbe\ud93d\uc292\u3ca3\uff15\uef04\ue640\u5283\u18d4\u1248\uc435\udbdd\ud85e\u849e\ue740\u47f2\u612e\ub060\u13a2\ud68a\u13aa\u1190", -428802357, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return;
        }
        this.layoutName.setLength(0);
        this.editingLayoutName = false;
        this.showHint((bl ? HudEditorScreen.nc("\ubf8c\u4127\ud18d\ud791\u823d\u34cd\uc420\ua937\u6593\u8e3f", 1616819704, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u85f6\ua42a\u2837\u9886\udb90\u8c0a\ufb4a\uf877\u4c1b", 1091238135, 0x1AFDE8A7 ^ CryptoVault.mask())) + HudEditorScreen.nc("\u8ccf\u2b97\u00f0\ubed2\u436e\uca17\u0119\ub110\u1020\ud779\u5846\uf244", -1230885151, 0x1AFDE8A7 ^ CryptoVault.mask()) + string + "\"");
    }

    private void applyLayoutPreset(String string) {
        this.history.checkpoint();
        JsonObject jsonObject = ClientPrefs.get().hudLayout(string);
        if (jsonObject == null) {
            return;
        }
        this.closePopup();
        this.settingsElement = null;
        for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
            if (!HUDManager.isWidget(hUDElement)) continue;
            JsonElement jsonElement = jsonObject.get(hUDElement.getName());
            String string2 = HUDManager.legacyName(hUDElement.getName());
            if (!(jsonElement != null && jsonElement.isJsonObject() || string2 == null)) {
                jsonElement = jsonObject.get(string2);
            }
            if (jsonElement == null || !jsonElement.isJsonObject()) continue;
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            try {
                if (jsonObject2.has(HudEditorScreen.nc("\ue01e\ub35b\ufc2f\u0bdc\u50fd\u5fec\u9d50\uc90f\u4bf7", -150380651, 0x1AFDE8A7 ^ CryptoVault.mask()))) {
                    hUDElement.setScale(jsonObject2.get(HudEditorScreen.nc("\ue8b4\u6072\ucfe5\u8ce5\uabf3\u1efa\u3136", 1351932358, 0x1AFDE8A7 ^ CryptoVault.mask())).getAsDouble());
                }
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
            try {
                if (jsonObject2.has("x")) {
                    hUDElement.setX(jsonObject2.get("x").getAsDouble());
                }
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
            try {
                if (jsonObject2.has("y")) {
                    hUDElement.setY(jsonObject2.get("y").getAsDouble());
                }
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
            try {
                boolean bl;
                if (jsonObject2.has(HudEditorScreen.nc("\ued1f\uca3a\u1ecd\u6b50\u1482\uee25\u9547\ue511\uf1c0", 847641644, 0x1AFDE8A7 ^ CryptoVault.mask())) && (bl = jsonObject2.get(HudEditorScreen.nc("\ufcc5\u3c22\u55eb\u80d9\u44cc\u7f41\u6b17\u20fd\u0b3d", -2107141146, 0x1AFDE8A7 ^ CryptoVault.mask())).getAsBoolean()) != hUDElement.isEnabled()) {
                    hUDElement.setEnabled(bl);
                }
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
            this.clampToScreen(hUDElement);
        }
        this.multi.clear();
        this.selected = null;
        this.showHint(HudEditorScreen.nc("\u8ef5\u6da0\u7003\u8c94\u770d\u41b5\u61f3\u04df\ub85c\u1e21\u5583\u65c5\ueffa\u2354\u1c15\ucd79\ua96c\u75ab\uf894", 1507783270, 0x1AFDE8A7 ^ CryptoVault.mask()) + string + "\"");
    }

    private Runnable alignPanel(UiDisplayList.Builder builder, int n2, int n3, double d2, double d3) {
        float f2;
        float f3;
        float f4 = this.chromeScale(n2, n3);
        AlignOp[] alignOpArray = AlignOp.values();
        int n4 = (alignOpArray.length + 1) / 2;
        float f5 = Math.min(236.0f * f4, (float)n2 - 20.0f * f4);
        float f6 = Math.max(10.0f * f4, Math.min((float)n2 - f5 - 10.0f * f4, this.alignX + this.alignW * 0.5f - f5 * 0.5f));
        Runnable runnable = HudEditorScreen.inRect(d2, d3, f6, f3 = this.panelsTop(f4), f5, f2 = 30.0f * f4 + (float)n4 * 25.0f * f4 + 16.0f * f4) ? () -> {} : null;
        int n5 = this.alignTargets().size();
        if (builder != null) {
            builder.shadow(f6, f3, f5, f2, 12.0f * f4, 12.0f * f4, f4, 0x72000000);
            builder.gradient(f6, f3, f5, f2, 12.0f * f4, this.panelTop, this.panelBottom, this.panelBottom, this.panelTop);
            builder.border(f6, f3, f5, f2, 12.0f * f4, Math.max(0.7f, f4), this.border);
            builder.text(f6 + 12.0f * f4, f3 + 10.0f * f4, 8.5f * f4, this.primary, HudEditorScreen.nc("\u6e94\u9dd6\u09bf\u96fe\uc084\ua104\u72e2\u5e4a\u8783", 909495835, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            String string = n5 == 0 ? HudEditorScreen.nc("\ufbfd\ud0ee\udb7e\ue993\u866c\uad23\uf53e\u8ad3\u86ca\u870e\u9bc7\uf64a\u24a5\u6963\ud1d5\uef83\u1eee\ud975\u0dd2\ub339", 1835288347, 0x1AFDE8A7 ^ CryptoVault.mask()) : (n5 == 1 ? HudEditorScreen.nc("\ud916\u83c0\u46de\u1391\u1e51\uaf1d\u0698\ub4de\u4dcb\u47d9\ub0ff\u2b99\u87e4\u901d\u6fb4\ufcad\uaa8f\ud59f\ub34e\u5917", -1046627263, 0x1AFDE8A7 ^ CryptoVault.mask()) : n5 + HudEditorScreen.nc("\u5441\u4f52\ubd69\u262e\u6c28\u210d\u68f5\uf21b\ua342\u6114\u9983", 77957199, 0x1AFDE8A7 ^ CryptoVault.mask()));
            builder.text(f6 + f5 - 12.0f * f4 - GpuUiRenderer.textWidth(string, 7.5f * f4), f3 + 11.0f * f4, 7.5f * f4, this.muted, string);
            builder.text(f6 + 12.0f * f4, f3 + f2 - 13.0f * f4, 7.0f * f4, this.muted, HudEditorScreen.fit(HudEditorScreen.nc("\u7168\u2b3e\ue0c7\ubdbd\u1fba\u3a44\ud761\uf4da\u3052\u21b6\u328f\u4539\u1674\u6d88\u7cc0\u8160\uf184\u25e0\u50e5\uf073\uab18\u4092\ud2a3\ud3a4\u29cb\u9a8b\ufa43\u6045\u459b\u9678\u9339\u44cc\ub847\ue731\udc9f\ue9ca\u5986\u9ca0\ucd25\u6ae8\ud9f1\u8d5d\uf64b\ue414\ufd57\ub68a\ue50d\u1627\u1275\uf20e\ub70e\uc409\ua2f6\uf933\u7c42\u743f\u3ed7\uf4c9", -1287797382, 0x1AFDE8A7 ^ CryptoVault.mask()), f5 - 24.0f * f4, 7.0f * f4));
        }
        float f7 = (f5 - 20.0f * f4 - 6.0f * f4) / 2.0f;
        float f8 = 20.0f * f4;
        for (int i2 = 0; i2 < alignOpArray.length; ++i2) {
            AlignOp alignOp = alignOpArray[i2];
            float f9 = f6 + 10.0f * f4 + (float)(i2 % 2) * (f7 + 6.0f * f4);
            float f10 = f3 + 30.0f * f4 + (float)(i2 / 2) * 25.0f * f4;
            boolean bl = HudEditorScreen.inRect(d2, d3, f9, f10, f7, f8);
            if (builder != null) {
                this.drawButton(builder, f9, f10, f7, f8, alignOp.label, bl, alignOp == AlignOp.ALL || alignOp == AlignOp.NONE ? this.secondary : this.accent, f4);
            }
            if (!bl) continue;
            runnable = () -> this.applyAlign(alignOp);
        }
        return runnable;
    }

    private List<HUDElement> alignTargets() {
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>();
        if (!this.multi.isEmpty()) {
            for (HUDElement hUDElement : this.multi) {
                if (hUDElement instanceof TargetBoxMetricsWidget55572) continue;
                arrayList.add(hUDElement);
            }
        } else if (this.selected != null && !(this.selected instanceof TargetBoxMetricsWidget55572) && HUDManager.isWidget(this.selected)) {
            arrayList.add(this.selected);
        }
        return arrayList;
    }

    private void applyAlign(AlignOp alignOp) {
        boolean bl;
        if (alignOp != AlignOp.ALL && alignOp != AlignOp.NONE) {
            this.history.checkpoint();
        }
        if (alignOp == AlignOp.ALL) {
            this.multi.clear();
            for (HUDElement hUDElement2 : HUDManager.getInstance().getElements()) {
                if (!HUDManager.isWidget(hUDElement2) || !hUDElement2.isEnabled() || hUDElement2 instanceof TargetBoxMetricsWidget55572) continue;
                this.multi.add(hUDElement2);
            }
            this.selected = this.multi.isEmpty() ? null : (HUDElement)this.multi.iterator().next();
            this.showHint(this.multi.size() + HudEditorScreen.nc("\u8945\u2fb3\u75db\u87e7\u457d\u2fb6\u81cd\u3288\u7c42\ucf5c\u92e4\u7db2\ub321\ub2c1\ud3e6\u73d4\ud1ea\u8cfe\udc8a\u08e3", -2094029504, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return;
        }
        if (alignOp == AlignOp.NONE) {
            this.multi.clear();
            this.selected = null;
            return;
        }
        List<HUDElement> list = this.alignTargets();
        if (list.isEmpty()) {
            this.showHint(HudEditorScreen.nc("\u844a\ub193\u252e\u3ec2\ucea7\u24df\u65a5\u1fee\u1fd1\uf69b\u257b\u263a\u1f75\u935d\u09ba\u9739\u0eea\u3f3f\ud682\u40f2\uba94\u0591\ude18\uf1f7\u687a\u5153\u19cc\u6119\u71c3\u544d\u24d7\u29e4\ufe77\u7de7\ub72c\uf826\u8c0c\uf7d5\u5277\uf268\u39d0\u8063\u10e5\u58bd\u703b\ua60e\u89bb\u5e64", 123075626, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return;
        }
        boolean bl2 = bl = alignOp == AlignOp.SPREAD_H || alignOp == AlignOp.SPREAD_V;
        if (bl && list.size() < 3) {
            this.showHint(HudEditorScreen.nc("\uc7e0\u8f50\u0436\u22a7\u4680\u6386\u9c49\u7679\u27bf\ub84d\udc55\u0c1b\u05bf\uf212\u2f16\u21ae\ufba6\u752f\u16af\u47a1\u56c5\u89b0\u50fc\u113d\u5404\u1f35\u08c0\ubda9\u7158\u0785\uf995\ucf34\u470a\u0dcc\u3972\u747d\u976d\u4f7e\ubf30\ub61b\u572b\u4606\ud53e\u172d\uda9f\ubbd9\u59ac\ufc4e\ufbd3", 286519024, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return;
        }
        float f2 = GpuHudRenderer.viewportScale(this.field_22789, this.field_22790);
        double d2 = Math.round((float)this.field_22789 / f2);
        double d3 = Math.round((float)this.field_22790 / f2);
        for (HUDElement hUDElement3 : list) {
            this.unanchor(hUDElement3, this.field_22789, this.field_22790);
        }
        double d4 = Double.MAX_VALUE;
        double d5 = Double.MAX_VALUE;
        double d6 = -1.7976931348623157E308;
        double d7 = -1.7976931348623157E308;
        for (HUDElement hUDElement4 : list) {
            d4 = Math.min(d4, hUDElement4.getX());
            d5 = Math.min(d5, hUDElement4.getY());
            d6 = Math.max(d6, hUDElement4.getX() + HudEditorScreen.widthOf(hUDElement4));
            d7 = Math.max(d7, hUDElement4.getY() + HudEditorScreen.heightOf(hUDElement4));
        }
        boolean bl3 = list.size() == 1;
        double d8 = bl3 ? 0.0 : d4;
        double d9 = bl3 ? d2 : d6;
        double d10 = bl3 ? 0.0 : d5;
        double d11 = bl3 ? d3 : d7;
        switch (alignOp.ordinal()) {
            case 0: {
                list.forEach(hUDElement -> hUDElement.setX(d8));
                break;
            }
            case 1: {
                list.forEach(hUDElement -> hUDElement.setX(d9 - HudEditorScreen.widthOf(hUDElement)));
                break;
            }
            case 2: {
                list.forEach(hUDElement -> hUDElement.setX((d8 + d9) * 0.5 - HudEditorScreen.widthOf(hUDElement) * 0.5));
                break;
            }
            case 4: {
                list.forEach(hUDElement -> hUDElement.setY(d10));
                break;
            }
            case 5: {
                list.forEach(hUDElement -> hUDElement.setY(d11 - HudEditorScreen.heightOf(hUDElement)));
                break;
            }
            case 3: {
                list.forEach(hUDElement -> hUDElement.setY((d10 + d11) * 0.5 - HudEditorScreen.heightOf(hUDElement) * 0.5));
                break;
            }
            case 6: {
                HudEditorScreen.spread(list, true, d4, d6);
                break;
            }
            case 7: {
                HudEditorScreen.spread(list, false, d5, d7);
                break;
            }
        }
        for (HUDElement hUDElement5 : list) {
            this.clampToScreen(hUDElement5);
        }
        this.showHint(alignOp.label.charAt(0) + alignOp.label.substring(1).toLowerCase(Locale.ROOT) + (bl3 ? HudEditorScreen.nc("\u4551\u6cac\ufd44\ubf8d\u4ab3\u79f2\ud179\ud037\u73b8\ud414\u32e6\u064f\u2f07\ubf7f\u0d4b\ufeb0", 1155707427, 0x1AFDE8A7 ^ CryptoVault.mask()) : " (" + list.size() + HudEditorScreen.nc("\udb4f\u8769\u10f0\u5ddc\u8462\u3e3c\uc302\u3ce4\ud7e6\u97aa\u0e68", 1543850414, 0x1AFDE8A7 ^ CryptoVault.mask())));
    }

    private static void spread(List<HUDElement> list, boolean bl, double d2, double d3) {
        ArrayList<HUDElement> arrayList = new ArrayList<HUDElement>(list);
        arrayList.sort(Comparator.comparingDouble(hUDElement -> bl ? hUDElement.getX() : hUDElement.getY()));
        int n2 = arrayList.size();
        double d4 = 0.0;
        for (HUDElement hUDElement2 : arrayList) {
            d4 += bl ? HudEditorScreen.widthOf(hUDElement2) : HudEditorScreen.heightOf(hUDElement2);
        }
        double d5 = (d3 - d2 - d4) / (double)(n2 - 1);
        if (d5 >= 0.0) {
            double d6 = d2;
            for (HUDElement hUDElement3 : arrayList) {
                if (bl) {
                    hUDElement3.setX(d6);
                } else {
                    hUDElement3.setY(d6);
                }
                d6 += (bl ? HudEditorScreen.widthOf(hUDElement3) : HudEditorScreen.heightOf(hUDElement3)) + d5;
            }
        } else {
            HUDElement hUDElement4 = (HUDElement)arrayList.get(0);
            HUDElement hUDElement5 = (HUDElement)arrayList.get(n2 - 1);
            double d7 = bl ? hUDElement4.getX() + HudEditorScreen.widthOf(hUDElement4) * 0.5 : hUDElement4.getY() + HudEditorScreen.heightOf(hUDElement4) * 0.5;
            double d8 = bl ? hUDElement5.getX() + HudEditorScreen.widthOf(hUDElement5) * 0.5 : hUDElement5.getY() + HudEditorScreen.heightOf(hUDElement5) * 0.5;
            for (int i2 = 0; i2 < n2; ++i2) {
                HUDElement hUDElement6 = (HUDElement)arrayList.get(i2);
                double d9 = d7 + (d8 - d7) * (double)i2 / (double)(n2 - 1);
                if (bl) {
                    hUDElement6.setX(d9 - HudEditorScreen.widthOf(hUDElement6) * 0.5);
                    continue;
                }
                hUDElement6.setY(d9 - HudEditorScreen.heightOf(hUDElement6) * 0.5);
            }
        }
    }

    private static double widthOf(HUDElement hUDElement) {
        return hUDElement.getWidth() * hUDElement.getScale();
    }

    private static double heightOf(HUDElement hUDElement) {
        return hUDElement.getHeight() * hUDElement.getScale();
    }

    private void saveToggles() {
        ClientPrefs.get().gui().setHudEditor(this.snapOn, this.gridOn);
    }

    private void showHint(String string) {
        this.hint = string;
        this.hintUntil = System.currentTimeMillis() + 2600L;
    }

    private void drawHint(UiDisplayList.Builder builder, int n2, int n3) {
        if (this.hint == null || System.currentTimeMillis() > this.hintUntil) {
            return;
        }
        float f2 = this.chromeScale(n2, n3);
        float f3 = 8.5f * f2;
        String string = HudEditorScreen.fit(this.hint, (float)n2 - 60.0f * f2, f3);
        float f4 = GpuUiRenderer.textWidth(string, f3) + 20.0f * f2;
        float f5 = 20.0f * f2;
        float f6 = ((float)n2 - f4) * 0.5f;
        float f7 = (float)n3 - 26.0f * f2 - 12.0f * f2 - f5 - 8.0f * f2;
        builder.pill(f6, f7, f4, f5, this.withAlpha(this.accent, 216));
        builder.text(f6 + 10.0f * f2, f7 + (f5 - f3) * 0.5f - 0.3f, f3, HudEditorScreen.contrastOn(this.accent), string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private static String fit(String string, float f2, float f3) {
        if (string == null) {
            return "";
        }
        if (f2 <= 0.0f) {
            return "";
        }
        if (GpuUiRenderer.textWidth(string, f3) <= f2) {
            return string;
        }
        String string2 = string;
        while (!string2.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!(GpuUiRenderer.textWidth(stringBuilder.append(string2).append("\u2026").toString(), f3) > f2)) break;
            string2 = string2.substring(0, string2.length() - 1);
        }
        return string2 + "\u2026";
    }

    private void drawLayoutsPanel(UiDisplayList.Builder builder, int n2, int n3, int n4, int n5) {
        this.layoutsPanel(builder, n2, n3, n4, n5);
    }

    private void drawAlignPanel(UiDisplayList.Builder builder, int n2, int n3, int n4, int n5) {
        this.alignPanel(builder, n2, n3, n4, n5);
    }

    private void drawButton(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, String string, boolean bl, int n2, float f6) {
        builder.pill(f2, f3, f4, f5, this.withAlpha(n2, bl ? 72 : 34));
        builder.border(f2, f3, f4, f5, f5 * 0.5f, Math.max(0.6f, f6), this.withAlpha(n2, bl ? 176 : 112));
        float f7 = GpuUiRenderer.textWidth(string, 7.5f * f6);
        builder.text(f2 + (f4 - f7) * 0.5f, f3 + (f5 - 7.5f * f6) * 0.5f - 0.5f, 7.5f * f6, this.primary, string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private void drawFooter(UiDisplayList.Builder builder, int n2, int n3, int n4) {
        float f2;
        int n5 = LEGEND_KEYS.length;
        float f3 = this.chromeScale(n2, n3);
        float f4 = 6.8f * f3;
        float f5 = this.legendWidth(n5, f4, f2 = 7.6f * f3, f3);
        if (f5 + 40.0f * f3 > (float)n2 - 24.0f * f3) {
            n5 = 3;
            f5 = this.legendWidth(n5, f4, f2, f3);
        }
        float f6 = Math.min((float)n2 - 24.0f * f3, f5 + 40.0f * f3);
        float f7 = ((float)n2 - f6) * 0.5f;
        float f8 = 26.0f * f3;
        float f9 = (float)n3 - f8 - 12.0f * f3 + this.chromeHide * (f8 + 24.0f * f3);
        builder.shadow(f7, f9 + 2.0f * f3, f6, f8, f8 * 0.5f, 16.0f * f3, 1.0f, 0x50000000);
        builder.pill(f7, f9, f6, f8, this.panelTop);
        builder.border(f7, f9, f6, f8, f8 * 0.5f, Math.max(0.9f, f3 * 1.1f), this.border);
        float f10 = f7 + 13.0f * f3;
        for (int i2 = 0; i2 < n5; ++i2) {
            float f11 = GpuUiRenderer.textWidth(LEGEND_KEYS[i2], f4) + 10.0f * f3;
            float f12 = 14.0f * f3;
            float f13 = f9 + (f8 - f12) * 0.5f;
            builder.rect(f10, f13, f11, f12, 3.0f * f3, this.withAlpha(this.accent, 34));
            builder.border(f10, f13, f11, f12, 3.0f * f3, Math.max(0.6f, f3 * 0.8f), this.withAlpha(this.accent, 102));
            builder.text(f10 + 5.0f * f3, f13 + (f12 - f4) * 0.5f - 0.3f, f4, this.primary, LEGEND_KEYS[i2], UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f10 += f11 + 4.0f * f3, f9 + (f8 - f2) * 0.5f - 0.3f, f2, this.secondary, LEGEND_LABELS[i2]);
            f10 += GpuUiRenderer.textWidth(LEGEND_LABELS[i2], f2) + 11.0f * f3;
        }
        String string = Integer.toString(n4);
        builder.text(f7 + f6 - 13.0f * f3 - GpuUiRenderer.textWidth(string, 8.0f * f3), f9 + 8.0f * f3, 8.0f * f3, this.muted, string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private float legendWidth(int n2, float f2, float f3, float f4) {
        float f5 = 0.0f;
        for (int i2 = 0; i2 < n2; ++i2) {
            f5 += GpuUiRenderer.textWidth(LEGEND_KEYS[i2], f2) + 10.0f * f4 + 4.0f * f4 + GpuUiRenderer.textWidth(LEGEND_LABELS[i2], f3) + 11.0f * f4;
        }
        return f5;
    }

    private void drawElementLabel(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3, int n4, int n5) {
        float f2 = this.chromeScale(n4, n5);
        int n6 = (int)Math.round(hUDElement.getScale() * 100.0);
        String string = hUDElement.getName() + (hUDElement.isEnabled() ? HudEditorScreen.nc("\ub6fe\u8aa9\u0a1c\ube84\udc21\u4d29\u712a\ue4e6\u8040\uc4d4\ub311", 231919427, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\u48b6\u4dd1\u3ae7\u466f\u636d\ue840\u4c070\u5546\ub1c2\u6d1b\ua7db", -357126577, 0x1AFDE8A7 ^ CryptoVault.mask())) + (n6 != 100 ? HudEditorScreen.nc("\u10fd\u811e\uadf5\ub38b\ua9aa\ua047\u2f6d\u0c55\u0afd", 1366478813, 0x1AFDE8A7 ^ CryptoVault.mask()) + n6 + "%" : "");
        float f3 = GpuUiRenderer.textWidth(string, 8.5f * f2) + 18.0f * f2;
        float f4 = Math.min((float)n4 - f3 - 8.0f * f2, (float)n2 + 13.0f * f2);
        float f5 = Math.min((float)n5 - 32.0f * f2, (float)n3 + 10.0f * f2);
        builder.pill(f4, f5, f3, 21.0f * f2, this.panelTop);
        builder.border(f4, f5, f3, 21.0f * f2, 10.5f * f2, Math.max(0.7f, f2), this.border);
        builder.text(f4 + 9.0f * f2, f5 + 6.0f * f2, 8.5f * f2, hUDElement.isEnabled() ? this.primary : this.secondary, string);
    }

    private static List<Setting> shownSettings(HUDElement hUDElement) {
        List<Setting> list = hUDElement.getSettings();
        if (list == null || list.isEmpty()) {
            return list;
        }
        boolean bl = true;
        for (Setting object : list) {
            if (object.isVisible()) continue;
            bl = false;
            break;
        }
        if (bl) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Setting setting : list) {
            if (!setting.isVisible()) continue;
            arrayList.add(setting);
        }
        return arrayList;
    }

    private void drawSettingsPopup(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3, int n4, int n5) {
        int n6;
        Object object;
        List<Setting> list = HudEditorScreen.shownSettings(hUDElement);
        if (list == null || list.isEmpty()) {
            return;
        }
        float[] fArray = this.popupBounds(hUDElement, list, n2, n3);
        float f2 = fArray[0];
        float f3 = fArray[1];
        float f4 = fArray[2];
        float f5 = fArray[3];
        float f6 = this.chromeScale(n2, n3);
        this.settingsScrollOffset = Math.max(0, Math.min(this.settingsScrollOffset, this.maxSettingsScrollOffset(list, f5, f6)));
        builder.shadow(f2, f3, f4, f5, 14.0f * f6, 12.0f * f6, f6, 0x72000000);
        builder.gradient(f2, f3, f4, f5, 14.0f * f6, this.panelTop, this.panelBottom, this.panelBottom, this.panelTop);
        builder.border(f2, f3, f4, f5, 14.0f * f6, Math.max(0.7f, f6), this.border);
        String string = hUDElement instanceof HitboxDrawPresentationStyle264081 ? HudEditorScreen.nc("\u663f\u815e\u7a40\u662e\uca69\ua6f0\u20c0\ub0d7\uc151\u5b05\u0707\u0b99\u0997\udd56\u0aeb", -452046838, 0x1AFDE8A7 ^ CryptoVault.mask()) : hUDElement.getName();
        float[] fArray2 = this.popupResetBounds(f2, f3, f4, f6);
        boolean bl = HudEditorScreen.inRect(n4, n5, fArray2[0], fArray2[1], fArray2[2], fArray2[3]);
        boolean bl2 = hUDElement.isModifiedFromDefaults();
        int n7 = bl2 ? this.accent : this.muted;
        builder.pill(fArray2[0], fArray2[1], fArray2[2], fArray2[3], this.withAlpha(n7, bl ? 72 : 28));
        builder.border(fArray2[0], fArray2[1], fArray2[2], fArray2[3], fArray2[3] * 0.5f, Math.max(0.6f, f6 * 0.8f), this.withAlpha(n7, bl ? 176 : 96));
        builder.text(fArray2[0] + 8.0f * f6, fArray2[1] + (fArray2[3] - 7.0f * f6) * 0.5f - 0.3f, 7.0f * f6, bl2 ? this.primary : this.secondary, HudEditorScreen.nc("\ube95\ub566\u4a99\u2212\uc1bc\ue80d", -1514090685, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        int n8 = this.visibleSettingsEnd(list, f5, f6);
        float f7 = fArray2[0] - 8.0f * f6;
        if (hUDElement instanceof HitboxDrawPresentationStyle264081) {
            object = this.popupMatchBounds(fArray2, f6);
            n6 = HudEditorScreen.inRect(n4, n5, object[0], object[1], object[2], object[3]);
            builder.pill(object[0], object[1], object[2], object[3], this.withAlpha(this.accent, n6 != 0 ? 72 : 36));
            builder.border(object[0], object[1], object[2], object[3], object[3] * 0.5f, Math.max(0.6f, f6 * 0.8f), this.withAlpha(this.accent, n6 != 0 ? 176 : 112));
            builder.text(object[0] + 8.0f * f6, object[1] + (object[3] - 7.0f * f6) * 0.5f - 0.3f, 7.0f * f6, this.primary, HudEditorScreen.nc("\u6ec8\u8e05\u8156\ue147\u13cf\ue9b0\u9dda\u41b8\u8760\u42af\u2ba4\u2b2a\uad81\u4a03", 225234553, 0x1AFDE8A7 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            if (n6 != 0) {
                this.hoverDescription = HudEditorScreen.nc("\ub8c8\uf8d7\u3c71\ua66c\ud21f\uc9d9\u2e4a\u9513\u0713\u94fb\ub955\u47dd\uee80\u69eb\uf547\ue64e\udbe4\u8fc1\u2db0\u5d7b\u1b78\u4eb5\u945a\ufd55\ua0ac\ue4df\ue8cb\u5b7a\ufd75\ue09c\ueed1\u332c\ubeb9\u452d\u751c\u7561\u1882\u32f4\u9700\u4d8d\u34cf\ufe65\u6eed\u49a7\u988b\u175a\u63b8\ub3af\ue0db\u2e6d\ubbdf\ubce9\u09dd\uf652\u1df3\u66a4\u7986\u4896\u94af\ubb65\u13c7\udc4d\ucf28\u04f1\ud392\u01b2\uc53b\u9f23\u73cc\udb4c\uf0cb\u62f1\u38fc\u0836\ud272\u959e\ucb82\ud334\ub15a\u882b\ufc42\uf388\uaa3b\u8120\ueeb6\u752a\u94d5\u95ab\u32c0\u8a97\u4edf\u44fc\u5ab8\u44ac\u96f8\u07d7\u834a\ua4b1\u0ebc\u7c0c\u0e7e\u477c\u140f\u7756\u82bd\u4332\u57f3\u9155\ua3fd\u6a8f\u6a41\ua4d3\u6ae3\u717f\u8be4\u46ac\u5db3\u866c\u4179\uf2ab\uc35e\u6571\ud21c\ub4c2\uf4fe\u54e8", 1244714052, 0x1AFDE8A7 ^ CryptoVault.mask());
            }
            f7 = object[0] - 8.0f * f6;
        }
        if (list.size() > n8 - this.settingsScrollOffset) {
            object = this.settingsScrollOffset + 1 + "-" + n8 + "/" + list.size();
            float f8 = GpuUiRenderer.textWidth((String)object, 7.0f * f6);
            builder.text(f7 - f8, f3 + 13.0f * f6, 7.0f * f6, this.secondary, (String)object, UiDrawCommand.TextWeight.SEMIBOLD);
            f7 -= f8 + 8.0f * f6;
        }
        builder.text(f2 + 14.0f * f6, f3 + 12.0f * f6, 9.5f * f6, this.primary, HudEditorScreen.fit(string, f7 - f2 - 14.0f * f6, 9.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
        float f9 = f3 + 32.0f * f6;
        for (n6 = this.settingsScrollOffset; n6 < n8; ++n6) {
            Setting setting = list.get(n6);
            float f10 = this.settingRowHeight(setting) * f6;
            if ((float)n4 >= f2 && (float)n4 <= f2 + f4 && (float)n5 >= f9 && (float)n5 <= f9 + Math.min(f10, 27.0f * f6) && setting.getDescription() != null && !setting.getDescription().isBlank()) {
                this.hoverDescription = setting.getDescription();
            }
            builder.rect(f2 + 10.0f * f6, f9, f4 - 20.0f * f6, Math.max(0.7f, f6), 0.0f, this.withAlpha(this.border, 48));
            builder.text(f2 + 13.0f * f6, f9 + 8.0f * f6, 9.0f * f6, this.primary, HudEditorScreen.fit(setting.getName(), HudEditorScreen.nameRoom(hUDElement, setting, f2, f4, f6), 9.0f * f6));
            if (setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting)setting;
                this.drawSwitch(builder, f2 + f4 - 49.0f * f6, f9 + 6.0f * f6, booleanSetting.isEnabled(), f6);
            } else if (setting instanceof ModeSetting) {
                ModeSetting modeSetting = (ModeSetting)setting;
                float f11 = HudEditorScreen.modePillWidth(modeSetting, f4, f6);
                String string2 = HudEditorScreen.fit(modeSetting.getCurrentMode(), f11 - 12.0f * f6, 8.0f * f6);
                var32_38 = f2 + f4 - f11 - 11.0f * f6;
                builder.pill(var32_38, f9 + 4.0f * f6, f11, 19.0f * f6, this.withAlpha(this.accent, 40));
                builder.text(var32_38 + (f11 - GpuUiRenderer.textWidth(string2, 8.0f * f6)) * 0.5f, f9 + 9.0f * f6, 8.0f * f6, this.accent, string2, UiDrawCommand.TextWeight.SEMIBOLD);
            } else if (setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting)setting;
                float f12 = f2 + f4 - 119.0f * f6;
                float f13 = f9 + 5.0f * f6;
                var32_38 = 106.0f * f6;
                var33_39 = 23.0f * f6;
                boolean bl3 = this.editingString == stringSetting;
                builder.rect(f12, f13, var32_38, var33_39, 3.0f * f6, bl3 ? this.withAlpha(this.accent, 48) : this.withAlpha(this.muted, 48));
                builder.border(f12, f13, var32_38, var33_39, 3.0f * f6, Math.max(0.7f, f6), bl3 ? this.withAlpha(this.accent, 190) : this.withAlpha(this.muted, 128));
                String string3 = stringSetting.X().isBlank() ? HudEditorScreen.nc("\u5963\u575d\u555e\u45ac\ud5bb\ud022\u9124\u2589\uc780\ub01d\u0127\u357c\ub00a", -1596662358, 0x1AFDE8A7 ^ CryptoVault.mask()) : stringSetting.X();
                String string4 = string3 = string3.length() > 15 ? string3.substring(0, 14) + "\u2026" : string3;
                if (bl3 && System.currentTimeMillis() / 500L % 2L == 0L) {
                    string3 = string3 + "_";
                }
                builder.text(f12 + 7.0f * f6, f13 + 7.0f * f6, 8.0f * f6, bl3 ? this.primary : this.secondary, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            } else if (setting instanceof SliderSetting) {
                double d2;
                SliderSetting sliderSetting = (SliderSetting)setting;
                String string5 = setting == hUDElement.getScaleSetting() ? Math.round(sliderSetting.B() * 100.0) + "%" : sliderSetting.W();
                float f14 = f2 + 13.0f * f6;
                var32_38 = f9 + 25.0f * f6;
                var33_39 = f4 - 26.0f * f6;
                double d3 = d2 = sliderSetting.E() <= sliderSetting.D() ? 0.0 : (sliderSetting.B() - sliderSetting.D()) / (sliderSetting.E() - sliderSetting.D());
                if (this.editingSlider == sliderSetting) {
                    String string6 = this.sliderBuffer.toString();
                    float f15 = GpuUiRenderer.textWidth(string6, 8.0f * f6);
                    float f16 = Math.max(f15 + 14.0f * f6, 46.0f * f6);
                    float f17 = 15.0f * f6;
                    float f18 = f2 + f4 - 9.0f * f6 - f16;
                    float f19 = f9 + 4.5f * f6;
                    builder.rect(f18, f19, f16, f17, 3.0f * f6, this.withAlpha(this.accent, 48));
                    builder.border(f18, f19, f16, f17, 3.0f * f6, Math.max(0.7f, f6), this.withAlpha(this.accent, 190));
                    if (this.sliderFresh && f15 > 0.0f) {
                        builder.rect(f18 + 6.0f * f6, f19 + 2.5f * f6, f15 + 2.0f * f6, f17 - 5.0f * f6, 1.5f * f6, this.withAlpha(this.accent, 85));
                    }
                    String string7 = !this.sliderFresh && System.currentTimeMillis() / 500L % 2L == 0L ? string6 + "_" : string6;
                    builder.text(f18 + 7.0f * f6, f9 + 8.0f * f6, 8.0f * f6, this.primary, string7, UiDrawCommand.TextWeight.SEMIBOLD);
                } else {
                    builder.text(f2 + f4 - 13.0f * f6 - GpuUiRenderer.textWidth(string5, 8.0f * f6), f9 + 8.0f * f6, 8.0f * f6, this.secondary, string5);
                }
                builder.rect(f14, var32_38, var33_39, 3.0f * f6, 1.5f * f6, this.withAlpha(this.muted, 128));
                builder.rect(f14, var32_38, var33_39 * (float)d2, 3.0f * f6, 1.5f * f6, this.accent);
                builder.circle(f14 + var33_39 * (float)d2, var32_38 + 1.5f * f6, 4.0f * f6, this.accent);
            } else if (setting instanceof ColorSetting) {
                ColorSetting colorSetting = (ColorSetting)setting;
                this.drawColorRow(builder, colorSetting, f2, f9, f4, f6);
            }
            f9 += f10;
        }
    }

    private static float modePillWidth(ModeSetting modeSetting, float f2, float f3) {
        float f4 = Math.max(50.0f * f3, GpuUiRenderer.textWidth(modeSetting.getCurrentMode(), 8.0f * f3) + 18.0f * f3);
        return Math.min(f4, Math.max(50.0f * f3, (f2 - 26.0f * f3) * 0.55f));
    }

    private static float nameRoom(HUDElement hUDElement, Setting setting, float f2, float f3, float f4) {
        float f5;
        if (setting instanceof BooleanSetting) {
            f5 = f2 + f3 - 49.0f * f4;
        } else if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            f5 = f2 + f3 - 11.0f * f4 - HudEditorScreen.modePillWidth(modeSetting, f3, f4);
        } else if (setting instanceof StringSetting) {
            f5 = f2 + f3 - 119.0f * f4;
        } else if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            String string = setting == hUDElement.getScaleSetting() ? Math.round(sliderSetting.B() * 100.0) + "%" : sliderSetting.W();
            f5 = f2 + f3 - 13.0f * f4 - GpuUiRenderer.textWidth(string, 8.0f * f4);
        } else if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            f5 = f2 + f3 - 42.0f * f4 - GpuUiRenderer.textWidth(HudEditorScreen.hex(HudEditorScreen.argbOf(colorSetting.A())), 7.5f * f4);
        } else {
            f5 = f2 + f3 - 13.0f * f4;
        }
        return f5 - 6.0f * f4 - (f2 + 13.0f * f4);
    }

    private float[] popupMatchBounds(float[] fArray, float f2) {
        float f3 = GpuUiRenderer.textWidth(HudEditorScreen.nc("\u02a4\u7c49\ua786\ub849\uad27\u5a53\u74df\u174b\uc013\u24ea\ub2f9\ueb9b\u00bd\uc0f8", 1093946566, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.0f * f2) + 16.0f * f2;
        return new float[]{fArray[0] - 6.0f * f2 - f3, fArray[1], f3, fArray[3]};
    }

    private float[] popupResetBounds(float f2, float f3, float f4, float f5) {
        float f6 = GpuUiRenderer.textWidth(HudEditorScreen.nc("\ue54e\u0298\u5eb2\uad5f\u3668\u7632", -555486549, 0x1AFDE8A7 ^ CryptoVault.mask()), 7.0f * f5) + 16.0f * f5;
        float f7 = 15.0f * f5;
        return new float[]{f2 + f4 - f6 - 11.0f * f5, f3 + 9.0f * f5, f6, f7};
    }

    private void drawDescription(UiDisplayList.Builder builder, HUDElement hUDElement, int n2, int n3) {
        List<Setting> list = HudEditorScreen.shownSettings(hUDElement);
        if (list == null || list.isEmpty()) {
            return;
        }
        float f2 = this.chromeScale(n2, n3);
        float[] fArray = this.popupBounds(hUDElement, list, n2, n3);
        float f3 = 7.6f * f2;
        float f4 = 8.0f * f2;
        float f5 = 10.5f * f2;
        float f6 = fArray[2] - f4 * 2.0f;
        String[] stringArray = this.hoverDescription.split(" ");
        String[] stringArray2 = new String[3];
        int n4 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringArray) {
            String string2;
            String string3 = string2 = stringBuilder.isEmpty() ? string : stringBuilder + " " + string;
            if (GpuUiRenderer.textWidth(string2, f3) > f6 && !stringBuilder.isEmpty()) {
                if (n4 == 2) {
                    stringBuilder.append("\u2026");
                    break;
                }
                stringArray2[n4++] = stringBuilder.toString();
                stringBuilder.setLength(0);
                stringBuilder.append(string);
                continue;
            }
            stringBuilder.setLength(0);
            stringBuilder.append(string2);
        }
        if (!stringBuilder.isEmpty() && n4 < 3) {
            stringArray2[n4++] = stringBuilder.toString();
        }
        float f7 = f4 * 2.0f + (float)n4 * f5 - (f5 - f3);
        float f8 = fArray[0];
        float f9 = fArray[1] + fArray[3] + 6.0f * f2;
        if (f9 + f7 > (float)n3 - 6.0f * f2) {
            f9 = fArray[1] - f7 - 6.0f * f2;
        }
        builder.rect(f8, f9, fArray[2], f7, 8.0f * f2, this.panelTop);
        builder.border(f8, f9, fArray[2], f7, 8.0f * f2, Math.max(0.7f, f2), this.border);
        for (int i2 = 0; i2 < n4; ++i2) {
            builder.text(f8 + f4, f9 + f4 + (float)i2 * f5, f3, this.secondary, stringArray2[i2]);
        }
    }

    private void drawPill(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5) {
        int n2 = 0xFF000000 | HudEditorScreen.mix(this.toggleOff & 0xFFFFFF, this.toggleOn & 0xFFFFFF, f4);
        builder.pill(f2, f3, 34.0f * f5, 15.0f * f5, n2);
        builder.circle(f2 + (8.0f + 18.0f * f4) * f5, f3 + 7.5f * f5, 5.2f * f5, this.primary);
    }

    private void drawSwitch(UiDisplayList.Builder builder, float f2, float f3, boolean bl, float f4) {
        int n2 = bl ? this.toggleOn : this.toggleOff;
        builder.pill(f2, f3, 34.0f * f4, 15.0f * f4, n2);
        builder.circle(bl ? f2 + 26.0f * f4 : f2 + 8.0f * f4, f3 + 7.5f * f4, 5.2f * f4, this.primary);
    }

    private static int argbOf(Color color) {
        return color == null ? -1 : color.getRGB();
    }

    private static String hex(int n2) {
        int n3 = n2 >>> 24 & 0xFF;
        return n3 == 255 ? String.format(Locale.ROOT, HudEditorScreen.nc("\uf351\ub952\ue9e2\ucba5\u052e\ubf56\ua65f\u9238\u4dc2", 1385149740, 0x1AFDE8A7 ^ CryptoVault.mask()), n2 & 0xFFFFFF) : String.format(Locale.ROOT, HudEditorScreen.nc("\u5157\uc6c5\u5047\u6621\u74d3\u13aa\ubfd9\ub32f", 2031791860, 0x1AFDE8A7 ^ CryptoVault.mask()), n2);
    }

    private void drawColorRow(UiDisplayList.Builder builder, ColorSetting colorSetting, float f2, float f3, float f4, float f5) {
        int n2 = HudEditorScreen.argbOf(colorSetting.A());
        boolean bl = this.expandedColor == colorSetting;
        float f6 = f2 + f4 - 36.0f * f5;
        float f7 = f3 + 6.0f * f5;
        float f8 = 22.0f * f5;
        float f9 = 14.0f * f5;
        this.drawChecker(builder, f6, f7, f8, f9, 7.0f * f5, 3.5f * f5);
        builder.pill(f6, f7, f8, f9, n2);
        builder.border(f6, f7, f8, f9, 7.0f * f5, Math.max(0.7f, f5), bl ? this.withAlpha(this.accent, 208) : this.border);
        String string = HudEditorScreen.hex(n2);
        builder.text(f6 - 6.0f * f5 - GpuUiRenderer.textWidth(string, 7.5f * f5), f3 + 9.0f * f5, 7.5f * f5, bl ? this.accent : this.secondary, string, UiDrawCommand.TextWeight.SEMIBOLD);
        if (!bl) {
            return;
        }
        this.layoutPicker(f2, f3, f4, f5);
        int n3 = 0xFF000000 | Color.HSBtoRGB(this.pickHue, 1.0f, 1.0f) & 0xFFFFFF;
        builder.gradient(this.svX, this.svY, this.svW, this.svH, 4.0f * f5, -1, n3, n3, -1);
        builder.gradient(this.svX, this.svY, this.svW, this.svH, 4.0f * f5, 0, 0, -16777216, -16777216);
        builder.border(this.svX, this.svY, this.svW, this.svH, 4.0f * f5, Math.max(0.6f, f5 * 0.8f), this.withAlpha(this.border, 128));
        float f10 = this.svX + this.pickSat * this.svW;
        float f11 = this.svY + (1.0f - this.pickBri) * this.svH;
        builder.circle(f10, f11, 4.6f * f5, -16777216);
        builder.circle(f10, f11, 3.8f * f5, -1);
        builder.circle(f10, f11, 2.8f * f5, 0xFF000000 | n2 & 0xFFFFFF);
        float f12 = this.hueH / (float)(HUE_STOPS.length - 1);
        for (int i2 = 0; i2 < HUE_STOPS.length - 1; ++i2) {
            float f13 = this.hueY + (float)i2 * f12;
            builder.gradient(this.hueX, f13, this.hueW, f12 + 0.5f, 0.0f, HUE_STOPS[i2], HUE_STOPS[i2], HUE_STOPS[i2 + 1], HUE_STOPS[i2 + 1]);
        }
        builder.border(this.hueX, this.hueY, this.hueW, this.hueH, 2.0f * f5, Math.max(0.6f, f5 * 0.8f), this.withAlpha(this.border, 128));
        float f14 = this.hueY + this.pickHue * this.hueH;
        builder.rect(this.hueX - 2.0f * f5, f14 - 1.5f * f5, this.hueW + 4.0f * f5, 3.0f * f5, 1.5f * f5, -1);
        builder.border(this.hueX - 2.0f * f5, f14 - 1.5f * f5, this.hueW + 4.0f * f5, 3.0f * f5, 1.5f * f5, Math.max(0.5f, f5 * 0.6f), -1073741824);
        int n4 = n2 >>> 24 & 0xFF;
        this.drawChecker(builder, this.alphaX, this.alphaY, this.alphaW, this.alphaH, this.alphaH * 0.5f, this.alphaH * 0.5f);
        builder.gradient(this.alphaX, this.alphaY, this.alphaW, this.alphaH, this.alphaH * 0.5f, n2 & 0xFFFFFF, 0xFF000000 | n2, 0xFF000000 | n2, n2 & 0xFFFFFF);
        builder.border(this.alphaX, this.alphaY, this.alphaW, this.alphaH, this.alphaH * 0.5f, Math.max(0.6f, f5 * 0.8f), this.withAlpha(this.border, 128));
        float f15 = this.alphaX + (float)n4 / 255.0f * this.alphaW;
        builder.rect(f15 - 1.5f * f5, this.alphaY - 2.0f * f5, 3.0f * f5, this.alphaH + 4.0f * f5, 1.5f * f5, -1);
        builder.border(f15 - 1.5f * f5, this.alphaY - 2.0f * f5, 3.0f * f5, this.alphaH + 4.0f * f5, 1.5f * f5, Math.max(0.5f, f5 * 0.6f), -1073741824);
        builder.rect(this.hexX, this.hexY, this.hexW, this.hexH, 3.0f * f5, this.editingHex ? this.withAlpha(this.accent, 48) : this.withAlpha(this.muted, 48));
        builder.border(this.hexX, this.hexY, this.hexW, this.hexH, 3.0f * f5, Math.max(0.7f, f5), this.editingHex ? this.withAlpha(this.accent, 190) : this.withAlpha(this.muted, 128));
        String string2 = this.editingHex ? this.hexBuffer + (System.currentTimeMillis() / 500L % 2L == 0L ? "_" : "") : string;
        builder.text(this.hexX + 6.0f * f5, this.hexY + (this.hexH - 8.0f * f5) * 0.5f - 0.3f, 8.0f * f5, this.editingHex ? this.primary : this.secondary, string2, UiDrawCommand.TextWeight.SEMIBOLD);
        String string3 = "A " + Math.round((float)n4 / 2.55f) + "%";
        builder.text(this.hexX + this.hexW + 8.0f * f5, this.hexY + (this.hexH - 7.5f * f5) * 0.5f - 0.3f, 7.5f * f5, this.muted, string3, UiDrawCommand.TextWeight.SEMIBOLD);
        String string4 = this.editingHex ? HudEditorScreen.nc("\udfc9\u2aea\u9a83\ufe73\uadce\ude16", -330624114, 0x1AFDE8A7 ^ CryptoVault.mask()) : HudEditorScreen.nc("\ueaec\u2b59\u5d91\udbcd\ua831\ucf4d\u2540\ub14b", 689619640, 0x1AFDE8A7 ^ CryptoVault.mask());
        builder.text(this.svX + this.svW + this.hueW + 6.0f * f5 - GpuUiRenderer.textWidth(string4, 6.8f * f5) - 0.0f, this.hexY + (this.hexH - 6.8f * f5) * 0.5f - 0.3f, 6.8f * f5, this.muted, string4, UiDrawCommand.TextWeight.SEMIBOLD);
        for (int i3 = 0; i3 <= PRESETS.length; ++i3) {
            int n5 = i3 == 0 ? 0xFF000000 | this.accent : PRESETS[i3 - 1];
            float f16 = this.swX + (float)i3 * (this.swSize + this.swGap);
            boolean bl2 = (n5 & 0xFFFFFF) == (n2 & 0xFFFFFF);
            builder.rect(f16, this.swY, this.swSize, this.swSize, this.swSize * 0.5f, n5);
            builder.border(f16, this.swY, this.swSize, this.swSize, this.swSize * 0.5f, bl2 ? Math.max(1.0f, 1.4f * f5) : Math.max(0.6f, f5 * 0.7f), bl2 ? this.withAlpha(this.primary, 240) : this.withAlpha(this.border, 144));
            if (i3 != 0) continue;
            builder.circle(f16 + this.swSize * 0.5f, this.swY + this.swSize * 0.5f, 1.6f * f5, HudEditorScreen.contrastOn(n5));
        }
    }

    private void drawChecker(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7) {
        builder.rect(f2, f3, f4, f5, f6, -6645094);
        int n2 = (int)Math.ceil(f4 / f7);
        int n3 = (int)Math.ceil(f5 / f7);
        for (int i2 = 0; i2 < n3; ++i2) {
            for (int i3 = i2 & 1; i3 < n2; i3 += 2) {
                float f8 = f2 + (float)i3 * f7;
                float f9 = f3 + (float)i2 * f7;
                builder.rect(f8, f9, Math.min(f7, f2 + f4 - f8), Math.min(f7, f3 + f5 - f9), 0.0f, -2565928);
            }
        }
    }

    private void layoutPicker(float f2, float f3, float f4, float f5) {
        float f6 = f2 + 13.0f * f5;
        float f7 = f3 + 27.0f * f5;
        float f8 = f4 - 26.0f * f5;
        this.hueW = 11.0f * f5;
        this.svX = f6;
        this.svY = f7;
        this.svW = f8 - this.hueW - 7.0f * f5;
        this.svH = 84.0f * f5;
        this.hueX = this.svX + this.svW + 7.0f * f5;
        this.hueY = f7;
        this.hueH = this.svH;
        this.alphaX = f6;
        this.alphaY = f7 + this.svH + 8.0f * f5;
        this.alphaW = f8;
        this.alphaH = 9.0f * f5;
        this.hexX = f6;
        this.hexY = this.alphaY + this.alphaH + 8.0f * f5;
        this.hexW = 78.0f * f5;
        this.hexH = 18.0f * f5;
        int n2 = PRESETS.length + 1;
        this.swSize = 14.0f * f5;
        this.swGap = Math.max(2.0f * f5, (f8 - (float)n2 * this.swSize) / (float)(n2 - 1));
        this.swX = f6;
        this.swY = this.hexY + this.hexH + 6.0f * f5;
    }

    private void expandColor(ColorSetting colorSetting) {
        this.finishHexEdit(false);
        if (this.expandedColor == colorSetting) {
            this.expandedColor = null;
            return;
        }
        this.expandedColor = colorSetting;
        this.syncPickerFrom(HudEditorScreen.argbOf(colorSetting.A()), true);
    }

    private void syncPickerFrom(int n2, boolean bl) {
        float[] fArray = Color.RGBtoHSB(n2 >> 16 & 0xFF, n2 >> 8 & 0xFF, n2 & 0xFF, null);
        if (bl || fArray[1] > 0.001f) {
            this.pickHue = fArray[0];
        }
        this.pickSat = fArray[1];
        this.pickBri = fArray[2];
    }

    private boolean handlePickerClick(double d2, double d3) {
        if (HudEditorScreen.inRect(d2, d3, this.svX, this.svY, this.svW, this.svH)) {
            this.pickDrag = PickPart.SV;
        } else if (HudEditorScreen.inRect(d2, d3, this.hueX - 2.0f, this.hueY, this.hueW + 4.0f, this.hueH)) {
            this.pickDrag = PickPart.HUE;
        } else if (HudEditorScreen.inRect(d2, d3, this.alphaX, this.alphaY - 3.0f, this.alphaW, this.alphaH + 6.0f)) {
            this.pickDrag = PickPart.ALPHA;
        } else {
            if (HudEditorScreen.inRect(d2, d3, this.hexX, this.hexY, this.hexW, this.hexH)) {
                this.beginHexEdit();
                return true;
            }
            this.finishHexEdit(true);
            for (int i2 = 0; i2 <= PRESETS.length; ++i2) {
                float f2 = this.swX + (float)i2 * (this.swSize + this.swGap);
                if (!HudEditorScreen.inRect(d2, d3, f2, this.swY, this.swSize, this.swSize)) continue;
                int n2 = i2 == 0 ? 0xFF000000 | this.accent : PRESETS[i2 - 1];
                int n3 = HudEditorScreen.argbOf(this.expandedColor.A()) & 0xFF000000;
                this.applyColor(n2 & 0xFFFFFF | n3);
                this.syncPickerFrom(n2, true);
                return true;
            }
            return true;
        }
        this.finishHexEdit(true);
        this.applyPick(d2, d3);
        return true;
    }

    private void applyPick(double d2, double d3) {
        if (this.expandedColor == null) {
            return;
        }
        int n2 = HudEditorScreen.argbOf(this.expandedColor.A());
        int n3 = n2 >>> 24 & 0xFF;
        switch (this.pickDrag.ordinal()) {
            case 1: {
                this.pickSat = HudEditorScreen.clamp01((float)((d2 - (double)this.svX) / (double)Math.max(1.0f, this.svW)));
                this.pickBri = 1.0f - HudEditorScreen.clamp01((float)((d3 - (double)this.svY) / (double)Math.max(1.0f, this.svH)));
                break;
            }
            case 2: {
                this.pickHue = Math.min(0.9999f, HudEditorScreen.clamp01((float)((d3 - (double)this.hueY) / (double)Math.max(1.0f, this.hueH))));
                break;
            }
            case 3: {
                n3 = Math.round(HudEditorScreen.clamp01((float)((d2 - (double)this.alphaX) / (double)Math.max(1.0f, this.alphaW))) * 255.0f);
                break;
            }
            default: {
                return;
            }
        }
        int n4 = this.pickDrag == PickPart.ALPHA ? n2 & 0xFFFFFF : Color.HSBtoRGB(this.pickHue, this.pickSat, this.pickBri) & 0xFFFFFF;
        this.applyColor(n3 << 24 | n4);
    }

    private void applyColor(int n2) {
        if (this.expandedColor == null) {
            return;
        }
        if (HudEditorScreen.argbOf(this.expandedColor.A()) != n2) {
            this.expandedColor.A(new Color(n2, true));
        }
    }

    private void beginHexEdit() {
        this.finishStringEdit();
        this.editingHex = true;
        this.hexBuffer.setLength(0);
        this.hexBuffer.append(HudEditorScreen.hex(HudEditorScreen.argbOf(this.expandedColor == null ? null : this.expandedColor.A())));
    }

    private void finishHexEdit(boolean bl) {
        if (!this.editingHex) {
            return;
        }
        this.editingHex = false;
        if (!bl || this.expandedColor == null) {
            return;
        }
        Integer n2 = HudEditorScreen.parseHex(this.hexBuffer.toString(), HudEditorScreen.argbOf(this.expandedColor.A()));
        if (n2 != null) {
            this.applyColor(n2);
            this.syncPickerFrom(n2, false);
        }
    }

    private static Integer parseHex(String string, int n2) {
        if (string == null) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("#")) {
            string2 = string2.substring(1);
        }
        if (string2.startsWith("0x") || string2.startsWith("0X")) {
            string2 = string2.substring(2);
        }
        try {
            if (string2.length() == 3) {
                int n3 = Integer.parseInt(string2.substring(0, 1), 16) * 17;
                int n4 = Integer.parseInt(string2.substring(1, 2), 16) * 17;
                int n5 = Integer.parseInt(string2.substring(2, 3), 16) * 17;
                return n2 & 0xFF000000 | n3 << 16 | n4 << 8 | n5;
            }
            if (string2.length() == 6) {
                return n2 & 0xFF000000 | (int)(Long.parseLong(string2, 16) & 0xFFFFFFL);
            }
            if (string2.length() == 8) {
                return (int)(Long.parseLong(string2, 16) & 0xFFFFFFFFL);
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return null;
    }

    private static boolean isHexChar(int n2) {
        return n2 >= 48 && n2 <= 57 || n2 >= 97 && n2 <= 102 || n2 >= 65 && n2 <= 70;
    }

    private void pasteHex() {
        if (this.field_22787 == null || this.field_22787.field_1774 == null) {
            return;
        }
        String string = this.field_22787.field_1774.method_1460();
        if (string == null) {
            return;
        }
        this.hexBuffer.setLength(0);
        this.hexBuffer.append('#');
        String string2 = string.trim();
        if (string2.startsWith("#")) {
            string2 = string2.substring(1);
        }
        if (string2.startsWith("0x") || string2.startsWith("0X")) {
            string2 = string2.substring(2);
        }
        for (int i2 = 0; i2 < string2.length() && this.hexBuffer.length() < 9; ++i2) {
            char c2 = string2.charAt(i2);
            if (!HudEditorScreen.isHexChar(c2)) continue;
            this.hexBuffer.append(Character.toUpperCase(c2));
        }
    }

    private float[] popupBounds(HUDElement hUDElement, List<Setting> list, int n2, int n3) {
        float f2 = this.chromeScale(n2, n3);
        float f3 = 38.0f * f2;
        for (Setting setting : list) {
            f3 += this.settingRowHeight(setting) * f2;
        }
        float f4 = Math.min(f3, Math.max(80.0f * f2, (float)n3 - 20.0f * f2));
        float f5 = 238.0f * f2;
        if (hUDElement instanceof HitboxDrawPresentationStyle264081) {
            float f6 = Math.max(10.0f * f2, Math.min((float)n2 - f5 - 10.0f * f2, this.styleX + this.styleW * 0.5f - f5 * 0.5f));
            float f7 = 12.0f * f2 + 30.0f * f2 + 8.0f * f2;
            float f8 = Math.max(10.0f * f2, Math.min((float)n3 - f4 - 10.0f * f2, f7));
            return new float[]{f6, f8, f5, f4};
        }
        float f9 = GpuHudRenderer.editorX(hUDElement, n2, n3);
        float f10 = GpuHudRenderer.editorY(hUDElement, n2, n3);
        float f11 = f9 + GpuHudRenderer.editorWidth(hUDElement, n2, n3) + 10.0f * f2;
        if (f11 + f5 > (float)n2 - 10.0f * f2) {
            f11 = f9 - f5 - 10.0f * f2;
        }
        f11 = Math.max(10.0f * f2, Math.min((float)n2 - f5 - 10.0f * f2, f11));
        float f12 = Math.max(10.0f * f2, Math.min((float)n3 - f4 - 10.0f * f2, f10));
        return new float[]{f11, f12, f5, f4};
    }

    private int visibleSettingsEnd(List<Setting> list, float f2, float f3) {
        float f4;
        int n2;
        float f5 = Math.max(0.0f, f2 - 38.0f * f3);
        float f6 = 0.0f;
        for (n2 = this.settingsScrollOffset; !(n2 >= list.size() || f6 + (f4 = this.settingRowHeight(list.get(n2)) * f3) > f5 + 0.01f && n2 > this.settingsScrollOffset); ++n2) {
            f6 += f4;
        }
        return n2;
    }

    private int maxSettingsScrollOffset(List<Setting> list, float f2, float f3) {
        float f4;
        int n2;
        float f5 = Math.max(0.0f, f2 - 38.0f * f3);
        float f6 = 0.0f;
        for (n2 = list.size(); !(n2 <= 0 || f6 + (f4 = this.settingRowHeight(list.get(n2 - 1)) * f3) > f5 + 0.01f && n2 < list.size()); --n2) {
            f6 += f4;
        }
        return n2;
    }

    private float settingRowHeight(Setting setting) {
        if (setting instanceof SliderSetting) {
            return 39.0f;
        }
        if (setting instanceof StringSetting) {
            return 38.0f;
        }
        if (setting instanceof ColorSetting && setting == this.expandedColor) {
            return 180.0f;
        }
        return 27.0f;
    }

    public boolean method_25402(class_11909 class_119092, boolean bl) {
        boolean bl2;
        Object object;
        boolean bl3;
        double d2 = class_119092.comp_4798();
        double d3 = class_119092.comp_4799();
        int n2 = class_119092.method_74245();
        int n3 = this.field_22789;
        int n4 = this.field_22790;
        this.finishSliderEdit(true);
        this.layoutHeader(n3, n4);
        boolean bl4 = bl3 = this.chromeHide <= 0.01f;
        if (this.ctxOpen) {
            object = this.ctxMenu(null, n3, n4, d2, d3);
            if (object != null) {
                if (n2 == 0) {
                    object.run();
                }
                return true;
            }
            this.ctxOpen = false;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.doneX, this.btnY, this.doneW, this.btnH)) {
            this.method_25419();
            return true;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.libBtnX, this.btnY, this.libBtnW, this.btnH)) {
            boolean bl5 = !this.libraryOpen;
            this.closePanels();
            this.closePopup();
            this.settingsElement = null;
            this.libraryOpen = bl5;
            if (bl5) {
                this.libCollapsed = false;
            }
            this.libSearchFocus = false;
            return true;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.styleX, this.btnY, this.styleW, this.btnH)) {
            object = HUDManager.getInstance().getStyle();
            this.closePanels();
            this.closePopup();
            if (this.settingsElement != object) {
                this.settingsElement = object;
                this.settingsScrollOffset = 0;
            } else {
                this.settingsElement = null;
            }
            return true;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.resetX, this.btnY, this.resetW, this.btnH)) {
            if (this.resetArmed()) {
                this.resetArmedUntil = 0L;
                this.closePopup();
                this.history.checkpoint();
                HUDManager.getInstance().resetLayout();
                for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
                    this.clampToScreen(hUDElement);
                }
                this.history.settle();
            } else {
                this.resetArmedUntil = System.currentTimeMillis() + 3000L;
            }
            return true;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.layoutsX, this.btnY, this.layoutsW, this.btnH)) {
            boolean bl6 = !this.layoutsOpen;
            this.closePanels();
            this.closePopup();
            this.settingsElement = null;
            this.layoutsOpen = bl6;
            return true;
        }
        if (n2 == 0 && bl3 && HudEditorScreen.inRect(d2, d3, this.alignX, this.btnY, this.alignW, this.btnH)) {
            boolean bl7 = !this.alignOpen;
            this.closePanels();
            this.closePopup();
            this.settingsElement = null;
            this.alignOpen = bl7;
            return true;
        }
        if (this.layoutsOpen) {
            Object object2 = object = bl3 ? this.layoutsPanel(null, n3, n4, d2, d3) : null;
            if (object != null) {
                if (n2 == 0) {
                    this.editingLayoutName = false;
                    object.run();
                }
                return true;
            }
            this.closePanels();
            return true;
        }
        if (this.alignOpen && bl3 && (object = this.alignPanel(null, n3, n4, d2, d3)) != null) {
            if (n2 == 0) {
                object.run();
            }
            return true;
        }
        if (this.libraryOpen && this.libCollapsed && bl3 && n2 == 0 && HudEditorScreen.inRect(d2, d3, (object = (Object)this.libTab(n3, n4))[0], object[1], object[2], object[3])) {
            this.libCollapsed = false;
            return true;
        }
        if (this.libraryOpen && bl3) {
            object = this.libraryPanel(null, n3, n4, d2, d3);
            if (object != null) {
                if (n2 == 0) {
                    object.run();
                }
                return true;
            }
            this.libSearchFocus = false;
        }
        if (bl3 && n2 == 0 && (object = this.toolbar(null, n3, n4, d2, d3)) != null) {
            object.run();
            return true;
        }
        if (this.settingsElement != null && this.handleSettingsClick(this.settingsElement, d2, d3, n2, n3, n4)) {
            return true;
        }
        if (this.settingsElement != null) {
            this.closePopup();
            this.settingsElement = null;
            this.settingsScrollOffset = 0;
        }
        object = HUDManager.getInstance().getElements();
        if (n2 == 0 && this.selected != null && this.handleHit(this.selected, d2, d3, n3, n4)) {
            this.history.checkpoint();
            this.scalingEl = this.selected;
            this.scaleStart = this.selected.getScale();
            this.scaleOx = GpuHudRenderer.editorX(this.selected, n3, n4);
            this.scaleOy = GpuHudRenderer.editorY(this.selected, n3, n4);
            this.scaleBase = Math.max(8.0f, GpuHudRenderer.editorWidth(this.selected, n3, n4) + GpuHudRenderer.editorHeight(this.selected, n3, n4));
            return true;
        }
        HUDElement hUDElement = this.elementAt((List<HUDElement>)object, d2, d3, n3, n4);
        if (hUDElement == null) {
            if (n2 == 0) {
                if (!this.shiftDown()) {
                    this.selected = null;
                    this.multi.clear();
                }
                this.boxing = true;
                this.boxX0 = this.boxX1 = (float)d2;
                this.boxY0 = this.boxY1 = (float)d3;
                return true;
            }
            this.selected = null;
            this.multi.clear();
            this.alignOpen = false;
            return super.method_25402(class_119092, bl);
        }
        if (n2 == 0 && (this.alignOpen || this.shiftDown()) && !(hUDElement instanceof TargetBoxMetricsWidget55572)) {
            if (this.multi.isEmpty() && this.selected != null && this.selected != hUDElement && !(this.selected instanceof TargetBoxMetricsWidget55572) && HUDManager.isWidget(this.selected)) {
                this.multi.add(this.selected);
            }
            if (!this.multi.remove(hUDElement)) {
                this.multi.add(hUDElement);
            }
            this.selected = this.multi.contains(hUDElement) ? hUDElement : (this.multi.isEmpty() ? null : (HUDElement)this.multi.iterator().next());
            return true;
        }
        boolean bl8 = bl2 = n2 == 0 && this.multi.size() > 1 && this.multi.contains(hUDElement);
        if (n2 == 0 && !bl2) {
            this.multi.clear();
        }
        this.selected = hUDElement;
        if (n2 == 0 && hUDElement.isLocked()) {
            this.showHint(hUDElement.getName() + HudEditorScreen.nc("\u8ac4\u6de9\u274f\u1dd9\ud53c\udfa5\ud903\u2f66\u4256\ubbc9\uad09\u561c\u24f7\u64e8\u1873\uf2eb\ub8be\u280f\u4f04\ue1ba\u9c35\u7c79\u736e\ue195\u0b09\u998f\ube0e\uf255\u2e4f\uae36\u3f76\u713b\ua9cd\u7446\u5a88\u9955\ud4fb\ub6b3\u83d2\u679c\udde6\ud7d6\u5cb3\uf513\ue84f\ud43b\udc24\ub29b\u8fbb\u8e35\ucd42\u7b35\u6518", 970135777, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return true;
        }
        if (n2 == 0) {
            this.history.checkpoint();
            if (bl2) {
                for (HUDElement hUDElement2 : this.multi) {
                    this.unanchor(hUDElement2, n3, n4);
                }
            }
            this.unanchor(hUDElement, n3, n4);
            this.dragging = hUDElement;
            float f2 = GpuHudRenderer.viewportScale(n3, n4);
            hUDElement.startDragging(d2 / (double)f2, d3 / (double)f2);
            return true;
        }
        if (n2 == 1) {
            if (this.shiftDown()) {
                this.closePopup();
                this.settingsElement = hUDElement;
                this.settingsScrollOffset = 0;
            } else {
                if (!this.multi.contains(hUDElement)) {
                    this.multi.clear();
                }
                this.ctxEl = hUDElement;
                this.ctxX = (float)d2 + 2.0f;
                this.ctxY = (float)d3 + 2.0f;
                this.ctxOpen = true;
                this.ctxAnim = 0.0f;
            }
            return true;
        }
        return true;
    }

    private void unanchor(HUDElement hUDElement, int n2, int n3) {
        if (hUDElement.getX() < 0.0 && !(hUDElement instanceof TargetBoxMetricsWidget55572)) {
            float f2 = GpuHudRenderer.viewportScale(n2, n3);
            hUDElement.setX(GpuHudRenderer.editorX(hUDElement, n2, n3) / f2);
            hUDElement.setY(GpuHudRenderer.editorY(hUDElement, n2, n3) / f2);
        }
    }

    private void closePopup() {
        this.finishSliderEdit(true);
        this.finishStringEdit();
        this.finishHexEdit(true);
        this.expandedColor = null;
        this.pickDrag = PickPart.NONE;
        this.draggingSlider = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean handleSettingsClick(HUDElement hUDElement, double d2, double d3, int n2, int n3, int n4) {
        List<Setting> list = HudEditorScreen.shownSettings(hUDElement);
        if (list == null) return false;
        if (list.isEmpty()) {
            return false;
        }
        float[] fArray = this.popupBounds(hUDElement, list, n3, n4);
        if (d2 < (double)fArray[0]) return false;
        if (d2 > (double)(fArray[0] + fArray[2])) return false;
        if (d3 < (double)fArray[1]) return false;
        if (d3 > (double)(fArray[1] + fArray[3])) {
            return false;
        }
        if (n2 != 0) {
            return true;
        }
        float f2 = this.chromeScale(n3, n4);
        float[] fArray2 = this.popupResetBounds(fArray[0], fArray[1], fArray[2], f2);
        if (HudEditorScreen.inRect(d2, d3, fArray2[0], fArray2[1], fArray2[2], fArray2[3])) {
            this.closePopup();
            this.history.checkpoint();
            hUDElement.resetToDefaults();
            this.clampToScreen(hUDElement);
            this.history.settle();
            return true;
        }
        if (hUDElement instanceof HitboxDrawPresentationStyle264081) {
            HitboxDrawPresentationStyle264081 hitboxDrawPresentationStyle264081 = (HitboxDrawPresentationStyle264081)hUDElement;
            float[] fArray3 = this.popupMatchBounds(fArray2, f2);
            if (HudEditorScreen.inRect(d2, d3, fArray3[0], fArray3[1], fArray3[2], fArray3[3])) {
                this.history.checkpoint();
                hitboxDrawPresentationStyle264081.matchMenuTheme();
                this.history.settle();
                return true;
            }
        }
        this.settingsScrollOffset = Math.max(0, Math.min(this.settingsScrollOffset, this.maxSettingsScrollOffset(list, fArray[3], f2)));
        float f3 = fArray[1] + 32.0f * f2;
        int n5 = this.visibleSettingsEnd(list, fArray[3], f2);
        int n6 = this.settingsScrollOffset;
        while (n6 < n5) {
            Setting setting = list.get(n6);
            float f4 = this.settingRowHeight(setting) * f2;
            if (d3 >= (double)f3 && d3 <= (double)(f3 + f4)) {
                SliderSetting sliderSetting;
                if (setting instanceof ColorSetting) {
                    ColorSetting colorSetting = (ColorSetting)setting;
                    if (d3 <= (double)(f3 + 27.0f * f2)) {
                        this.expandColor(colorSetting);
                        return true;
                    }
                    if (this.expandedColor != colorSetting) return true;
                    this.layoutPicker(fArray[0], f3, fArray[2], f2);
                    this.handlePickerClick(d2, d3);
                    return true;
                }
                this.finishHexEdit(true);
                if (setting instanceof BooleanSetting) {
                    BooleanSetting booleanSetting = (BooleanSetting)setting;
                    booleanSetting.toggle();
                    return true;
                }
                if (setting instanceof ModeSetting) {
                    ModeSetting modeSetting = (ModeSetting)setting;
                    modeSetting.cycle();
                    return true;
                }
                if (setting instanceof StringSetting) {
                    StringSetting stringSetting = (StringSetting)setting;
                    this.beginStringEdit(stringSetting);
                    return true;
                }
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting2 = (SliderSetting)setting;
                    if (d3 <= (double)(f3 + 20.0f * f2)) {
                        float f5 = fArray[0] + fArray[2] - 13.0f * f2;
                        String string = hUDElement.getScaleSetting() == sliderSetting2 ? Math.round(sliderSetting2.B() * 100.0) + "%" : sliderSetting2.W();
                        if (d2 >= (double)(f5 - GpuUiRenderer.textWidth(string, 8.0f * f2) - 8.0f * f2)) {
                            this.beginSliderEdit(hUDElement, sliderSetting2);
                            return true;
                        }
                    }
                }
                if (!(setting instanceof SliderSetting)) return true;
                this.draggingSlider = sliderSetting = (SliderSetting)setting;
                this.sliderMinX = fArray[0] + 13.0f * f2;
                this.sliderMaxX = fArray[0] + fArray[2] - 13.0f * f2;
                this.updateSlider(d2);
                return true;
            }
            f3 += f4;
            ++n6;
        }
        return true;
    }

    public boolean method_25401(double d2, double d3, double d4, double d5) {
        Object object;
        if (d5 == 0.0) {
            return false;
        }
        if (this.ctxOpen && this.ctxMenu(null, this.field_22789, this.field_22790, d2, d3) != null) {
            return true;
        }
        if (this.libraryOpen && this.libraryPanel(null, this.field_22789, this.field_22790, d2, d3) != null) {
            this.libScroll = Math.max(0.0f, this.libScroll - (float)d5 * 42.0f);
            return true;
        }
        if (this.layoutsOpen && this.layoutsPanel(null, this.field_22789, this.field_22790, d2, d3) != null) {
            this.layoutsScroll += d5 < 0.0 ? 1 : -1;
            if (this.layoutsScroll < 0) {
                this.layoutsScroll = 0;
            }
            return true;
        }
        if (this.settingsElement != null && (object = HudEditorScreen.shownSettings(this.settingsElement)) != null && !object.isEmpty()) {
            float f2 = this.chromeScale(this.field_22789, this.field_22790);
            float[] fArray = this.popupBounds(this.settingsElement, (List<Setting>)object, this.field_22789, this.field_22790);
            if (d2 >= (double)fArray[0] && d2 <= (double)(fArray[0] + fArray[2]) && d3 >= (double)fArray[1] && d3 <= (double)(fArray[1] + fArray[3])) {
                int n2 = this.maxSettingsScrollOffset((List<Setting>)object, fArray[3], f2);
                int n3 = d5 < 0.0 ? 1 : -1;
                this.settingsScrollOffset = Math.max(0, Math.min(n2, this.settingsScrollOffset + n3));
                return true;
            }
        }
        if ((object = this.elementAt(HUDManager.getInstance().getElements(), d2, d3, this.field_22789, this.field_22790)) == null || this.dragging != null) {
            return false;
        }
        if (((HUDElement)object).isLocked()) {
            this.showHint(((HUDElement)object).getName() + HudEditorScreen.nc("\udb43\u0804\u89a1\u636c\u8a95\ud317\u5121\u4ae6\u7b55\u8f42\u4396\uca30\ua794", -183305188, 0x1AFDE8A7 ^ CryptoVault.mask()));
            return true;
        }
        long l2 = System.currentTimeMillis();
        if (l2 - this.lastWheelMs > 600L) {
            this.history.checkpoint();
        }
        this.lastWheelMs = l2;
        double d6 = (double)Math.round((((HUDElement)object).getScale() + (d5 > 0.0 ? 0.05 : -0.05)) / 0.05) * 0.05;
        ((HUDElement)object).setScale(d6);
        this.clampToScreen((HUDElement)object);
        this.selected = object;
        this.scaleFlash = object;
        this.scaleFlashUntil = System.currentTimeMillis() + 900L;
        return true;
    }

    private void beginStringEdit(StringSetting stringSetting) {
        this.finishStringEdit();
        this.editingString = stringSetting;
        stringSetting.L(true);
    }

    private void finishStringEdit() {
        if (this.editingString != null) {
            this.editingString.L(false);
        }
        this.editingString = null;
    }

    private void beginSliderEdit(HUDElement hUDElement, SliderSetting sliderSetting) {
        this.finishSliderEdit(true);
        this.finishStringEdit();
        this.editingSlider = sliderSetting;
        this.editingSliderPercent = hUDElement.getScaleSetting() == sliderSetting;
        this.sliderBuffer.setLength(0);
        this.sliderBuffer.append(this.editingSliderPercent ? String.valueOf(Math.round(sliderSetting.B() * 100.0)) : sliderSetting.W());
        this.sliderFresh = true;
    }

    private void finishSliderEdit(boolean bl) {
        SliderSetting sliderSetting = this.editingSlider;
        if (sliderSetting == null) {
            return;
        }
        this.editingSlider = null;
        this.sliderFresh = false;
        if (!bl) {
            return;
        }
        double d2 = this.editingSliderPercent ? 100.0 : 1.0;
        double d3 = SliderValueEdit.parse(this.sliderBuffer.toString(), sliderSetting.D() * d2, sliderSetting.E() * d2, this.editingSliderPercent ? 0 : sliderSetting.W());
        if (Double.isNaN(d3)) {
            return;
        }
        sliderSetting.B(d3 / d2);
        if (this.settingsElement != null && sliderSetting == this.settingsElement.getScaleSetting()) {
            this.clampToScreen(this.settingsElement);
        }
    }

    private void sliderTyped(int n2, SliderSetting sliderSetting) {
        boolean bl;
        boolean bl2 = bl = n2 >= 48 && n2 <= 57 || n2 == 46 || n2 == 45 && sliderSetting.D() < 0.0;
        if (!bl) {
            return;
        }
        if (this.sliderFresh) {
            this.sliderBuffer.setLength(0);
            this.sliderFresh = false;
        }
        if (this.sliderBuffer.length() < 14) {
            this.sliderBuffer.appendCodePoint(n2);
        }
    }

    private void updateSlider(double d2) {
        if (this.draggingSlider == null) {
            return;
        }
        double d3 = Math.max(1.0, this.sliderMaxX - this.sliderMinX);
        double d4 = Math.max(0.0, Math.min(1.0, (d2 - this.sliderMinX) / d3));
        this.draggingSlider.B(this.draggingSlider.D() + d4 * (this.draggingSlider.E() - this.draggingSlider.D()));
        if (this.settingsElement != null && this.draggingSlider == this.settingsElement.getScaleSetting()) {
            this.clampToScreen(this.settingsElement);
        }
    }

    private void clampToScreen(HUDElement hUDElement) {
        float f2 = GpuHudRenderer.viewportScale(this.field_22789, this.field_22790);
        int n2 = Math.round((float)this.field_22789 / f2);
        int n3 = Math.round((float)this.field_22790 / f2);
        if (hUDElement instanceof TargetBoxMetricsWidget55572) {
            TargetBoxMetricsWidget55572 targetBoxMetricsWidget55572 = (TargetBoxMetricsWidget55572)hUDElement;
            float f3 = targetBoxMetricsWidget55572.getEditorX(n2);
            float f4 = targetBoxMetricsWidget55572.getEditorY(n3);
            float f5 = Math.max(0.0f, (float)n2 - targetBoxMetricsWidget55572.getWidgetWidth() * (float)targetBoxMetricsWidget55572.getScale());
            float f6 = Math.max(0.0f, (float)n3 - targetBoxMetricsWidget55572.getWidgetHeight() * (float)targetBoxMetricsWidget55572.getScale());
            targetBoxMetricsWidget55572.setX(targetBoxMetricsWidget55572.getX() + (double)Math.max(0.0f, Math.min(f5, f3)) - (double)f3);
            targetBoxMetricsWidget55572.setY(targetBoxMetricsWidget55572.getY() + (double)Math.max(0.0f, Math.min(f6, f4)) - (double)f4);
        } else if (hUDElement.getX() >= 0.0) {
            float f7 = Math.max(0.0f, (float)n2 - (float)hUDElement.getWidth() * (float)hUDElement.getScale());
            float f8 = Math.max(0.0f, (float)n3 - (float)hUDElement.getHeight() * (float)hUDElement.getScale());
            hUDElement.setX(Math.max(0.0, Math.min((double)f7, hUDElement.getX())));
            hUDElement.setY(Math.max(0.0, Math.min((double)f8, hUDElement.getY())));
        }
    }

    public boolean method_25403(class_11909 class_119092, double d2, double d3) {
        if (this.pickDrag != PickPart.NONE) {
            this.applyPick(class_119092.comp_4798(), class_119092.comp_4799());
            return true;
        }
        if (this.draggingSlider != null) {
            this.updateSlider(class_119092.comp_4798());
            return true;
        }
        if (this.scalingEl != null) {
            double d4 = (class_119092.comp_4798() - (double)this.scaleOx + (class_119092.comp_4799() - (double)this.scaleOy)) / (double)this.scaleBase;
            this.scalingEl.setScale((double)Math.round(Math.max(0.1, this.scaleStart * d4) * 100.0) / 100.0);
            this.clampToScreen(this.scalingEl);
            this.scaleFlash = this.scalingEl;
            this.scaleFlashUntil = System.currentTimeMillis() + 900L;
            return true;
        }
        if (this.boxing) {
            this.boxX1 = (float)class_119092.comp_4798();
            this.boxY1 = (float)class_119092.comp_4799();
            return true;
        }
        if (this.dragging != null) {
            double d5 = this.dragging.getX();
            double d6 = this.dragging.getY();
            float f2 = GpuHudRenderer.viewportScale(this.field_22789, this.field_22790);
            double d7 = class_119092.comp_4798() / (double)f2;
            double d8 = class_119092.comp_4799() / (double)f2;
            this.dragging.mouseDragged(d7, d8);
            int n2 = Math.round((float)this.field_22789 / f2);
            int n3 = Math.round((float)this.field_22790 / f2);
            this.clampToScreen(this.dragging);
            if (!(this.dragging instanceof TargetBoxMetricsWidget55572)) {
                if (this.snapOn && !this.altDown()) {
                    this.snapDragged(this.dragging, n2, n3, f2);
                } else {
                    this.guideX = Float.NaN;
                    this.guideY = Float.NaN;
                }
                this.clampToScreen(this.dragging);
            }
            if (this.multi.size() > 1 && this.multi.contains(this.dragging)) {
                double d9 = this.dragging.getX() - d5;
                double d10 = this.dragging.getY() - d6;
                for (HUDElement hUDElement : this.multi) {
                    if (hUDElement == this.dragging || hUDElement.isLocked()) continue;
                    hUDElement.setX(hUDElement.getX() + d9);
                    hUDElement.setY(hUDElement.getY() + d10);
                    this.clampToScreen(hUDElement);
                }
            }
            return true;
        }
        return super.method_25403(class_119092, d2, d3);
    }

    public boolean method_25406(class_11909 class_119092) {
        if (class_119092.method_74245() == 0) {
            if (this.boxing) {
                this.finishBox();
            }
            this.scalingEl = null;
        }
        if (class_119092.method_74245() == 0 && this.dragging != null) {
            this.dragging.stopDragging();
            this.dragging = null;
            this.guideX = Float.NaN;
            this.guideY = Float.NaN;
            this.history.settle();
        }
        if (class_119092.method_74245() == 0) {
            this.history.settle();
            this.draggingSlider = null;
            this.pickDrag = PickPart.NONE;
        }
        return super.method_25406(class_119092);
    }

    public boolean method_25404(class_11908 class_119082) {
        List<HUDElement> list;
        boolean bl;
        int n3 = class_119082.comp_4795();
        int n4 = class_119082.comp_4797();
        boolean bl2 = (n4 & 0xA) != 0;
        boolean bl3 = bl = (n4 & 1) != 0;
        if (this.ctxOpen && n3 == 256) {
            this.ctxOpen = false;
            return true;
        }
        if (this.editingLayoutName) {
            if (n3 == 256) {
                this.editingLayoutName = false;
            } else if (n3 == 257 || n3 == 335) {
                this.saveLayoutPreset();
            } else if (n3 == 259) {
                if (!this.layoutName.isEmpty()) {
                    this.layoutName.setLength(this.layoutName.length() - 1);
                }
            } else if (bl2 && n3 == 86 && this.field_22787 != null && this.field_22787.field_1774 != null) {
                String string = this.field_22787.field_1774.method_1460();
                if (string != null) {
                    string.codePoints().forEach(this::appendLayoutNameChar);
                }
            } else if (bl2 && n3 == 65) {
                this.layoutName.setLength(0);
            }
            return true;
        }
        if (n3 == 256 && (this.layoutsOpen || this.alignOpen || this.libraryOpen)) {
            this.closePanels();
            return true;
        }
        if (this.editingHex) {
            if (n3 == 256) {
                this.finishHexEdit(false);
            } else if (n3 == 257 || n3 == 335) {
                this.finishHexEdit(true);
            } else if (n3 == 259) {
                if (!this.hexBuffer.isEmpty()) {
                    this.hexBuffer.setLength(this.hexBuffer.length() - 1);
                }
            } else if (bl2 && n3 == 86) {
                this.pasteHex();
            } else if (bl2 && n3 == 65) {
                this.hexBuffer.setLength(0);
            } else if (bl2 && n3 == 67 && this.field_22787 != null && this.field_22787.field_1774 != null) {
                this.field_22787.field_1774.method_1455(this.hexBuffer.toString());
            }
            return true;
        }
        if (this.editingSlider != null) {
            if (n3 == 256) {
                this.finishSliderEdit(false);
            } else if (n3 == 257 || n3 == 335) {
                this.finishSliderEdit(true);
            } else if (n3 == 259) {
                if (this.sliderFresh) {
                    this.sliderBuffer.setLength(0);
                    this.sliderFresh = false;
                } else if (this.sliderBuffer.length() > 0) {
                    this.sliderBuffer.setLength(this.sliderBuffer.length() - 1);
                }
            } else if (bl2 && n3 == 86 && this.field_22787 != null && this.field_22787.field_1774 != null) {
                String string = this.field_22787.field_1774.method_1460();
                if (string != null) {
                    SliderSetting sliderSetting = this.editingSlider;
                    string.codePoints().forEach(n2 -> this.sliderTyped(n2, sliderSetting));
                }
            } else if (bl2 && n3 == 65) {
                this.sliderFresh = true;
            }
            return true;
        }
        if (this.editingString != null) {
            if (n3 == 256 || n3 == 257 || n3 == 335) {
                this.finishStringEdit();
                return true;
            }
            if (n3 == 259) {
                String string = this.editingString.X();
                if (!string.isEmpty()) {
                    this.editingString.G(this.removeLastCodePoint(string));
                }
                return true;
            }
        }
        if (this.libSearchFocus) {
            if (n3 == 256 || n3 == 257 || n3 == 335) {
                this.libSearchFocus = false;
            } else if (n3 == 259) {
                if (this.libSearch.length() > 0) {
                    this.libSearch.setLength(this.libSearch.length() - 1);
                }
            } else if (bl2 && n3 == 65) {
                this.libSearch.setLength(0);
            }
            this.libScroll = 0.0f;
            return true;
        }
        if (this.expandedColor != null && bl2 && n3 == 67 && this.field_22787 != null && this.field_22787.field_1774 != null) {
            this.field_22787.field_1774.method_1455(HudEditorScreen.hex(HudEditorScreen.argbOf(this.expandedColor.A())));
            return true;
        }
        if (this.expandedColor != null && bl2 && n3 == 86) {
            this.beginHexEdit();
            this.pasteHex();
            this.finishHexEdit(true);
            return true;
        }
        if (this.editingString == null && this.expandedColor == null && bl2) {
            if (n3 == 90) {
                if (bl) {
                    this.doRedo();
                } else {
                    this.doUndo();
                }
                return true;
            }
            if (n3 == 89) {
                this.doRedo();
                return true;
            }
            if (n3 == 67 && this.selected != null && HUDManager.isWidget(this.selected)) {
                this.copyStyle(this.selected);
                return true;
            }
            if (n3 == 86) {
                List<HUDElement> list2 = this.sel();
                if (!list2.isEmpty()) {
                    this.pasteStyle(list2);
                }
                return true;
            }
            if (n3 == 65) {
                this.multi.clear();
                for (HUDElement hUDElement : HUDManager.getInstance().getElements()) {
                    if (!HUDManager.isWidget(hUDElement) || hUDElement instanceof TargetBoxMetricsWidget55572 || hUDElement.isEditorHidden() || !(hUDElement.getWidth() > 0.0)) continue;
                    this.multi.add(hUDElement);
                }
                this.selected = this.multi.isEmpty() ? null : (HUDElement)this.multi.iterator().next();
                return true;
            }
        }
        if (!(this.editingString != null || n3 != 91 && n3 != 93 || (list = this.sel()).isEmpty())) {
            this.layer(list, n3 == 93 ? 1 : -1);
            return true;
        }
        if (n3 == 256) {
            if (this.expandedColor != null) {
                this.expandedColor = null;
            } else if (this.settingsElement != null) {
                this.closePopup();
                this.settingsElement = null;
                this.settingsScrollOffset = 0;
            } else {
                this.method_25419();
            }
            return true;
        }
        if (n3 == 261 && this.editingString == null && this.dragging == null && !(list = this.sel()).isEmpty()) {
            this.hideTargets(list);
            return true;
        }
        if (this.editingString == null && this.selected != null && this.dragging == null) {
            int n5;
            int n6;
            int n7;
            this.ctxOpen = false;
            int n8 = n7 = bl ? 10 : 1;
            int n9 = n3 == 263 ? -n7 : (n6 = n3 == 262 ? n7 : 0);
            int n10 = n3 == 265 ? -n7 : (n5 = n3 == 264 ? n7 : 0);
            if (n6 != 0 || n5 != 0) {
                long l2 = System.currentTimeMillis();
                if (l2 - this.lastNudgeMs > 600L) {
                    this.history.checkpoint();
                }
                this.lastNudgeMs = l2;
                if (this.multi.size() > 1) {
                    for (HUDElement hUDElement : this.multi) {
                        this.nudge(hUDElement, n6, n5);
                    }
                } else {
                    this.nudge(this.selected, n6, n5);
                }
                return true;
            }
        }
        return super.method_25404(class_119082);
    }

    private boolean altDown() {
        if (this.field_22787 == null) {
            return false;
        }
        try {
            long l2 = this.field_22787.method_22683().method_4490();
            return GLFW.glfwGetKey((long)l2, (int)342) == 1 || GLFW.glfwGetKey((long)l2, (int)346) == 1;
        }
        catch (LinkageError | RuntimeException throwable) {
            return false;
        }
    }

    private void closePanels() {
        this.ctxOpen = false;
        this.libraryOpen = false;
        this.libSearchFocus = false;
        this.layoutsOpen = false;
        this.alignOpen = false;
        this.editingLayoutName = false;
        this.armedDeleteLayout = null;
    }

    private void appendLayoutNameChar(int n2) {
        if (this.layoutName.length() >= 32) {
            return;
        }
        if (Character.isLetterOrDigit(n2) || n2 == 32 || n2 == 45 || n2 == 95 || n2 == 46) {
            if (n2 == 32 && this.layoutName.isEmpty()) {
                return;
            }
            this.layoutName.appendCodePoint(n2);
        }
    }

    private void nudge(HUDElement hUDElement, int n2, int n3) {
        if (hUDElement.isLocked()) {
            return;
        }
        this.unanchor(hUDElement, this.field_22789, this.field_22790);
        hUDElement.setX(hUDElement.getX() + (double)n2);
        hUDElement.setY(hUDElement.getY() + (double)n3);
        this.clampToScreen(hUDElement);
    }

    public boolean method_25400(class_11905 class_119052) {
        int n2 = class_119052.comp_4793();
        if (this.libraryOpen && this.libSearchFocus) {
            if (this.libSearch.length() < 24 && n2 >= 32 && n2 != 127) {
                this.libSearch.appendCodePoint(n2);
                this.libScroll = 0.0f;
            }
            return true;
        }
        if (this.editingLayoutName) {
            this.appendLayoutNameChar(n2);
            return true;
        }
        if (this.editingHex) {
            if (n2 == 35 && this.hexBuffer.isEmpty()) {
                this.hexBuffer.append('#');
            } else if (HudEditorScreen.isHexChar(n2) && this.hexBuffer.length() < 9) {
                this.hexBuffer.append(Character.toUpperCase((char)n2));
            }
            return true;
        }
        if (this.editingSlider != null) {
            this.sliderTyped(n2, this.editingSlider);
            return true;
        }
        if (this.editingString == null) {
            return super.method_25400(class_119052);
        }
        if (!Character.isValidCodePoint(n2) || Character.isISOControl(n2)) {
            return true;
        }
        String string = this.editingString.X();
        if (string.codePointCount(0, string.length()) >= 32) {
            return true;
        }
        this.editingString.G(string + new String(Character.toChars(n2)));
        return true;
    }

    private String removeLastCodePoint(String string) {
        if (string.isEmpty()) {
            return string;
        }
        return string.substring(0, string.offsetByCodePoints(string.length(), -1));
    }

    public void method_25419() {
        this.closePopup();
        if (this.field_22787 == null) {
            this.saveLayout();
            return;
        }
        class_437 class_4372 = this.parent;
        if (class_4372 == null && this.field_22787.field_1687 != null) {
            class_4372 = ClickGuiRouter.create();
        }
        McCompat.setScreen(this.field_22787, class_4372);
    }

    public void method_25432() {
        this.closePopup();
        this.saveLayout();
    }

    private void saveLayout() {
        if (this.saved) {
            return;
        }
        this.saved = true;
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            String string = configManager.getLastLoadedConfigName();
            if (string == null || string.isBlank()) {
                string = HudEditorScreen.nc("\u7369\ubf20\u4de9\ub662\ud6ed\u1870\u828c\u1e38\u72f3\ue4c7\u5a24", -272107344, 0x1AFDE8A7 ^ CryptoVault.mask());
            }
            configManager.saveAndSyncCloud(string);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public boolean method_25421() {
        return false;
    }

    private HUDElement elementAt(List<HUDElement> list, double d2, double d3, int n2, int n3) {
        for (int i2 = list.size() - 1; i2 >= 0; --i2) {
            HUDElement hUDElement = list.get(i2);
            if (!HUDManager.isWidget(hUDElement) || hUDElement.isEditorHidden()) continue;
            float f2 = GpuHudRenderer.editorX(hUDElement, n2, n3);
            float f3 = GpuHudRenderer.editorY(hUDElement, n2, n3);
            float f4 = GpuHudRenderer.editorWidth(hUDElement, n2, n3);
            float f5 = GpuHudRenderer.editorHeight(hUDElement, n2, n3);
            if (f4 <= 0.0f || f5 <= 0.0f || !(d2 >= (double)f2) || !(d2 <= (double)(f2 + f4)) || !(d3 >= (double)f3) || !(d3 <= (double)(f3 + f5))) continue;
            return hUDElement;
        }
        return null;
    }

    private static boolean inRect(double d2, double d3, float f2, float f3, float f4, float f5) {
        return d2 >= (double)f2 && d2 <= (double)(f2 + f4) && d3 >= (double)f3 && d3 <= (double)(f3 + f5);
    }

    private static float clamp01(float f2) {
        return Math.max(0.0f, Math.min(1.0f, f2));
    }

    private static int contrastOn(int n2) {
        int n3 = n2 >> 16 & 0xFF;
        int n4 = n2 >> 8 & 0xFF;
        int n5 = n2 & 0xFF;
        return (0.2126f * (float)n3 + 0.7152f * (float)n4 + 0.0722f * (float)n5) / 255.0f > 0.62f ? -15724011 : -1;
    }

    private boolean shiftDown() {
        if (this.field_22787 == null) {
            return false;
        }
        try {
            long l2 = this.field_22787.method_22683().method_4490();
            return GLFW.glfwGetKey((long)l2, (int)340) == 1 || GLFW.glfwGetKey((long)l2, (int)344) == 1;
        }
        catch (LinkageError | RuntimeException throwable) {
            return false;
        }
    }

    private int withAlpha(int n2, int n3) {
        return Math.max(0, Math.min(255, n3)) << 24 | n2 & 0xFFFFFF;
    }

    static {
        COLOURS_LABEL = HudEditorScreen.nc("\ue761\ua48c\u9a86\ud1b8\u698a\u03e9\u8b47\ubfe1\u6def\ud0e7", 409510002, 0x1AFDE8A7 ^ CryptoVault.mask());
        PRESETS = new int[]{-1, -42401, -22195, -11205, -11415706, -14229306, -11686921, -5138436, -556116};
        HUE_STOPS = new int[]{-65536, -256, -16711936, -16711681, -16776961, -65281, -65536};
        LEGEND_KEYS = new String[]{HudEditorScreen.nc("\u67d1\uddff\u77e5\u09b6\u8649\ub0ec\ub3c8", 409565329, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u38b4\ub0c7\u493b\uf50a\u38a9\ua6ca\ue7a8\ua6cc\u7cab", -898438390, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u2f10\u5015\uf42d\u1cb7\uab05\u6b2d\u1751\u8916\uc35b\ubcd3\uf616\uc7f3\u69ee\uec7a\u99d4\u64a0", 384297511, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u9c07\u96cc\u5f96\u5542\u4500\u16a3\ub44d\u2f3a\u8207\uc729\uf0a1\u7740\u04ef\u2ec0\u2013", -617570738, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u8498\u37f2\u247c\u2a22\ua463\u02a9\ufa67\ua705\u7165", -1112767556, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u94bd\ud7d0\u4155\ud8d5\u16d6\uc008\u91ff\u2c48", -871801300, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\ucac0\u6f66\u54cb\u6c9f\u4077\u4efa\u3c9b\u66b8\u4de2", -1963247820, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\udbb0\ud7e5\uee82\ub8ef\udb56\ubafa\u4ebf\u19f3\u1e67\u9f6f\u62a7\ua18b\u9a5f", -317200274, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u55e7\u1759\u17ae\u57df\u1773\ueaab\u6c82\u57e5\u51bf\u9e87", 1656681590, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u57f9\ub6d8\u4033\uc905\u0272\u6f73\u36d3\u8254\u4e42\u76a8\uccd0\uab4b", -937371346, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u8c58\u52d4\u7738\u484e\u25ac\ue9bd\u0272", 519695343, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u1615\u0a9e\u161f\uc7dc", 1525594049, 0x1AFDE8A7 ^ CryptoVault.mask())};
        LEGEND_LABELS = new String[]{HudEditorScreen.nc("\u14dd\u89e9\u9ca6\u11ac\u58fe", -2011276849, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\ud943\uea29\u38c5\u4018\uf56f\ufa3d", 74757949, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u3d59\u2384\ue1c2\u9435\u969a\ube41\u387c\u439c\u2742", -145123520, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\ued79\u485f\u4fd4\ufe3a\ub3d8\uc7ad\uba08\u1506\u09dc\ufbc6\u41ff\u202e\ua089", 606321362, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u00c2\uc79e\u3ea9\ua556\ud08c\u8f51\udd9a\ubf49\u9438", 1882749525, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u3369\u6389\u2c7b\u06fc", 979139206, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\uabdb\u3726\u0885\uf4c1\u9f4f\ue104\ucf95\u13ba", 161557644, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u8220\u685b\u324c\uf719\uc927\ua860\ua580\u2c1c\u4f3d\u7fb5\u2ba2\u1529", 1575397662, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u699c\ubac0\uf3a0\u4eee\ub7a2\uacc3", -1614927263, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u6271\ua920\ud2ba\u157d\ua5b9\u8d36\u33e3\uae1d\u1d64\u020b\u1f4e\u08bf\ua1eb\u8c3d\u0fa3\u683c\u965f", 1043380087, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u8b78\u5f02\u5c81\u7efc\ubfd5\uf2dc\u33d9\ua980\u522a", -1556048525, 0x1AFDE8A7 ^ CryptoVault.mask()), HudEditorScreen.nc("\u47e3\uda54\u2245\u20a0\ue99c", -1626310167, 0x1AFDE8A7 ^ CryptoVault.mask())};
    }

    private static /* synthetic */ String nc(String string, int n2, int n3) {
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
    private static enum PickPart {
        NONE,
        SV,
        HUE,
        ALPHA;


        private static /* synthetic */ String vg(String string, int n2, int n3) {
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
    private record CtxRow(String[] labels, Runnable[] acts, int tone, boolean keepOpen) {
    }

    @Environment(value=EnvType.CLIENT)
    private static enum AlignOp {
        LEFT(AlignOp.wr("\u435f\uefa3\uce35\u9481\u9b8b\ua6fe\u4228\u9ee5\u1119\ud417\u8f05\u4f81\ube6c\u8894", 419352839, 0x84E4BA11 ^ CryptoVault.mask())),
        RIGHT(AlignOp.wr("\u4e64\u9c69\u6380\u99a9\u1f90\u68f9\uf7c7\u931c\u0bbc\uf679\u7db4\u5ffd\u6444\u0f02", -909791813, 0x84E4BA11 ^ CryptoVault.mask())),
        HCENTER(AlignOp.wr("\u8524\uceae\ue264\ub1e8\ue4e0\ua90d\u97b0\ue61d\u3060\u58fb\u08b8\ue151", 1088955157, 0x84E4BA11 ^ CryptoVault.mask())),
        VCENTER(AlignOp.wr("\ucd74\u28b3\u17b3\u7d3c\ue475\ueb46\u84bf\u6d4f\ucf65\u68f2\u865f\ub85d", -815421634, 0x84E4BA11 ^ CryptoVault.mask())),
        TOP(AlignOp.wr("\ua7b8\u6786\u2d8a\uc73c\uee28\u292c\u2df6\u3322\u6f12\u4f2b\u6e5d\u941a\u9ee8", 1325208634, 0x84E4BA11 ^ CryptoVault.mask())),
        BOTTOM(AlignOp.wr("\u2a4b\ucb48\u6a49\u172b\udfc3\u1b53\ufaa8\u220f\u5e6e\u170d\u577f\ubed3\ue367\u086b\u24e1", -1693621464, 0x84E4BA11 ^ CryptoVault.mask())),
        SPREAD_H(AlignOp.wr("\u5ddf\uc4c5\u2c8a\u6b98\u270a\uaecf\udcec\ufaed\u4d9d\u0c56\ud101\u5a4e", -1010079191, 0x84E4BA11 ^ CryptoVault.mask())),
        SPREAD_V(AlignOp.wr("\ub3f6\ub9c3\u6448\ue3af\ud756\uaa75\ua063\u874a\udb02\u347b\ucdcc\u52c2", -894564979, 0x84E4BA11 ^ CryptoVault.mask())),
        ALL(AlignOp.wr("\uba70\ue8a4\u1624\ua297\u010f\u62bb\u7143\u9f38\u39cf\ud420\u85cb\u260f\u9643\u50d7", -2017896225, 0x84E4BA11 ^ CryptoVault.mask())),
        NONE(AlignOp.wr("\ud5de\udc71\ub9d8\u926c\ua599\u352b\u5a83\ud794", 963174770, 0x84E4BA11 ^ CryptoVault.mask()));

        final String label;

        private AlignOp(String string2) {
            this.label = string2;
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
}

