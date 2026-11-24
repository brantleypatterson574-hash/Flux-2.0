/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_1657
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  org.lwjgl.glfw.GLFW
 */
package com.ishland.scalablelux.gui.gpuui;

import com.ishland.scalablelux.SlAuth;
import com.ishland.scalablelux.config.CloudConfigSync;
import com.ishland.scalablelux.config.CloudConfigs;
import com.ishland.scalablelux.config.ConfigKeybindManager;
import com.ishland.scalablelux.config.ConfigManager;
import com.ishland.scalablelux.config.GuiPrefs;
import com.ishland.scalablelux.config.hub.PrivateConfigManager;
import com.ishland.scalablelux.d.e;
import com.ishland.scalablelux.e.c;
import com.ishland.scalablelux.g.a;
import com.ishland.scalablelux.g.d;
import com.ishland.scalablelux.gui.ClickGuiRouter;
import com.ishland.scalablelux.gui.gpuui.BindModeControl;
import com.ishland.scalablelux.gui.gpuui.ClassicSettingLayout;
import com.ishland.scalablelux.gui.gpuui.GpuClickGuiScreen;
import com.ishland.scalablelux.gui.gpuui.KeybindsView;
import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiAuroraMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiClassicMistMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiSprings;
import com.ishland.scalablelux.gui.gpuui.framework.UiStarfieldMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiSurface;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiWaterMaterial;
import com.ishland.scalablelux.gui.gpuui.kit.AutoMenuScale;
import com.ishland.scalablelux.gui.gpuui.kit.CatalogDrag;
import com.ishland.scalablelux.gui.gpuui.kit.CatalogSections;
import com.ishland.scalablelux.gui.gpuui.kit.ConfigsView;
import com.ishland.scalablelux.gui.gpuui.kit.CosmeticsView;
import com.ishland.scalablelux.gui.gpuui.kit.KeybindCapture;
import com.ishland.scalablelux.gui.gpuui.kit.OnboardingTour;
import com.ishland.scalablelux.gui.gpuui.kit.PeopleView;
import com.ishland.scalablelux.gui.gpuui.kit.ResponsiveLayout;
import com.ishland.scalablelux.gui.gpuui.kit.SliderValueEdit;
import com.ishland.scalablelux.gui.gpuui.kit.StudioTab;
import com.ishland.scalablelux.gui.gpuui.kit.ThemeMakerView;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextTarget;
import com.ishland.scalablelux.gui.gpuui.kit.WhatsNewPanel;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuFluidSimulation;
import com.ishland.scalablelux.gui.new_ui.HudEditorScreen;
import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.gui.sound.UiSounds;
import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.Category;
import com.ishland.scalablelux.module.ColorSetting;
import com.ishland.scalablelux.module.ConfigKeybindListSetting;
import com.ishland.scalablelux.module.ModeSetting;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.MultiSelectSetting;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.module.SliderSetting;
import com.ishland.scalablelux.module.StringSetting;
import com.ishland.scalablelux.module.hooks.CosmeticsProvider;
import com.ishland.scalablelux.render.ThumbnailCache;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.ModuleAccess;
import com.ishland.scalablelux.util.PerfProbe;
import com.ishland.scalablelux.util.PremiumGate;
import com.ishland.scalablelux.util.ScissorClamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import me.sootysplash.box.render.HitboxKeyBinding655023;
import me.sootysplash.box.render.HitboxRenderStateReset726175;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_364;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public class DropdownGpuClickGuiScreen
extends GpuClickGuiScreen {
    private static final Category[] PANELS;
    private static final float SIDE = 30.0f;
    private static final float TOP = 104.0f;
    private static final float BOTTOM = 28.0f;
    private static final float STRIP_Y = 66.0f;
    private static final float STRIP_H = 28.0f;
    private static final String QUICK_KEY;
    private static final String FOLDER_PREFIX;
    private static final float PANEL_W = 206.0f;
    private static final float PANEL_GAP = 12.0f;
    private static final float HEADER_H = 36.0f;
    private static final float ROW_H = 27.0f;
    private static final float IPAD = 11.0f;
    private static final float INDENT = 10.0f;
    private static final long CLOSE_NS = 160000000L;
    private static final Map<String, float[]> POS;
    private static final Map<String, Float> SCROLL;
    private static final Set<String> COLLAPSED;
    private static final Set<String> EXPANDED;
    private static float laidOutW;
    private static float laidOutH;
    private static String lastLayout;
    private static final List<String> Z_ORDER;
    private List<PanelDef> lastDefs = List.of();
    private final List<Hit> hits = new ArrayList<Hit>();
    private final List<Hit> rightHits = new ArrayList<Hit>();
    private boolean secondaryIsMiddle;
    private final List<Grip> grips = new ArrayList<Grip>();
    private final List<ScrollZone> scrollZones = new ArrayList<ScrollZone>();
    private final List<RowRect> rowRects = new ArrayList<RowRect>();
    private final Map<String, List<Module>> panelLists = new HashMap<String, List<Module>>();
    private final Map<String, List<CatalogSections.Group>> panelGroups = new HashMap<String, List<CatalogSections.Group>>();
    private static final float SECTION_H = 20.0f;
    private final UiSprings anims = new UiSprings();
    private StringSetting editingString;
    private HitboxKeyBinding655023 bindingSetting;
    private Module bindingModule;
    private BooleanSetting bindingBool;
    private Setting draggingSlider;
    private float sliderPx;
    private float sliderWpx;
    private int sliderNotch = Integer.MIN_VALUE;
    private String dragPanel;
    private float dragOffX;
    private float dragOffY;
    private String armPanel;
    private double armX;
    private double armY;
    private boolean closing;
    private float waterPrevMx = Float.NaN;
    private float waterPrevMy;
    private float waterVelX;
    private float waterVelY;
    private String hoverOwnerPanel;
    private final UiTextField searchField = new UiTextField(DropdownGpuClickGuiScreen.hj("\u8487\u55ac\u9e9d\uc111\uc724\u87bd\uc45a\u306b\udf4b\u4637\u1728\u6126\u9bff\u9d36\u6eb7\u188a\u9dc7\u5795\u8fc4\u045c\u27af\ua7de\ua014\ud5e4\ud0bc\u4cf9\ufdf9\uf3f7\ua437\u2bf2\ua9bc\uc5fc", 1871643386, 0x229D082C ^ CryptoVault.mask())).maxLength(48).bare(true).padding(30.0f, 26.0f).fontSize(10.5f).onChange(this::onSearchChanged).onSubmit(() -> this.activateNav(false));
    private final UiTextField folderNameField = new UiTextField(DropdownGpuClickGuiScreen.hj("\u2ca7\ud4ec\u74bb\u1def\u8e97\ub761\u6de2\u418e\u1bfe\u0191\u3025\u6bf0\u8295\u8f99\u652e", -1226310147, 0x229D082C ^ CryptoVault.mask())).maxLength(24).fontSize(10.5f).onSubmit(this::commitFolderRename).onBlur(this::commitFolderRename);
    private final Map<String, UiTextField> settingFilters = new HashMap<String, UiTextField>();
    private String renamingFolderId;
    private String resetArmedModule;
    private long resetArmedAt;
    private boolean signOutArmed;
    private long signOutArmedAt;
    private int navIndex = -1;
    private boolean navVisible;
    private List<Module> navModules = new ArrayList<Module>();
    private Module rowOwner;
    private String rowPrefix;
    private List<MenuItem> ctxItems;
    private String ctxTitle;
    private float ctxX;
    private float ctxY;
    private RowPress pendingPress;
    private double pendingX;
    private double pendingY;
    private Module dragModule;
    private String dragSource;
    private List<Module> dragVisible = List.of();
    private float dragPointerX;
    private float dragPointerY;
    private int dragDropIndex = -1;
    private String dragFolderTarget;
    private boolean reorderable;
    private float dropBarX;
    private float dropBarY;
    private float dropBarW;
    private float pkSpeedX;
    private float pkSpeedW;
    private int hoverSoundKey;
    private float hoverSoundX = Float.NaN;
    private float hoverSoundY = Float.NaN;
    private long openedAt;
    private long closedAt;
    private long lastFrame;
    private float ppg = 1.0f;
    private float dt;
    private float s = 1.0f;
    private float sw;
    private float sh;
    private float viewW;
    private float viewH;
    private int mx;
    private int my;
    private boolean ready;
    private String tipTitle;
    private String tipDesc;
    private int accent = -13638657;
    private int text = -920588;
    private int muted = -6512218;
    private int toggleOff = -14013393;
    private int rim = 0x38FFFFFF;
    private int panelBase = 461328;
    private int deep = 132106;
    private String themeName = "";
    private UiSurface activeSurface = UiSurface.THEME;
    private boolean materialPanel;
    private final StudioTab macroView = new StudioTab(StudioTab.Kind.MACROS);
    private final StudioTab scriptView = new StudioTab(StudioTab.Kind.SCRIPTS);
    private final PeopleView peopleView = new PeopleView(new PeopleBackendImpl());
    private final KeybindsView keybindsView = new KeybindsView(false);
    private final ThemeMakerView themeMaker = new ThemeMakerView();
    private final ConfigsView configsView = new ConfigsView(new DropdownConfigsBackend());
    private List<String> savedConfigs = List.of();
    private Map<String, PrivateConfigManager.PrivateConfigInfo> cloudProfiles = Map.of();
    private String configStatusMessage = "";
    private String pendingProfileSaveName;
    private boolean pendingSavePublic;
    private List<String> pendingSaveTags = List.of();
    private String pendingProfileActivationName;
    private boolean profileLoadPending;
    private final DropdownHost host = new DropdownHost();
    private final OnboardingTour ddTour = OnboardingTour.forDropdown(this::studioUnlocked, this::cosmeticsUnlocked);
    private boolean tourReopenedFirstPanel;
    private final EnumMap<OnboardingTour.Target, float[]> tourRects = new EnumMap(OnboardingTour.Target.class);
    private final CosmeticsView cosmeticsView = new CosmeticsView(new e());
    private float overlayScroll;
    private boolean ddArmed;
    private long ddArmedAt;
    private String bindingConfigName;
    private String overlayView;
    private Module overlayModule;
    private UiTextTarget kitFocus;
    private IntConsumer kitKeyCapture;
    private final SliderValueEdit sliderEdit = new SliderValueEdit();
    private final KeybindCapture captureOrigin = new KeybindCapture();
    private double pressX;
    private double pressY;
    private boolean hostClip;
    private float hcx;
    private float hcy;
    private float hcw;
    private float hch;
    private static float separatedW;
    private static float separatedH;
    private static String separatedLayout;
    private static int separatedCount;
    private static final float NOTICE_H = 78.0f;
    private static final float QUICK_LABEL_H = 4.0f;
    private static final float BIND_H = 38.0f;
    private static final float TOOLS_H = 30.0f;
    private static final float AI_HEAD_H = 46.0f;
    private static final float AI_AIM_HEAD_H = 52.0f;
    private final Map<Module, ModuleView> viewCache = new IdentityHashMap<Module, ModuleView>();
    private static final float CHIP_H = 24.0f;
    private static final float CHIP_GAP = 6.0f;
    private static final float CHIP_TOP = 28.0f;
    private static final float ROW_PAD = 8.0f;
    private float decorX;
    private float decorY;
    private boolean navScrollPending;
    private static final float THEME_TILE_GAP = 6.0f;
    private static final float THEME_PREVIEW_H = 26.0f;
    private static final float THEME_TILE_H = 42.0f;
    private boolean decorOn;
    private static final String[] MOD_KEY_PREFIX;
    private final Map<Module, String[]> modKeys = new IdentityHashMap<Module, String[]>();
    private long swallowCharsUntilMs;

    private void loadLayout() {
        StringSetting stringSetting = HitboxConfigScreen752809.W;
        if (stringSetting == null) {
            return;
        }
        String string = stringSetting.X();
        if (string == null || string.isEmpty() || string.equals(lastLayout)) {
            return;
        }
        lastLayout = string;
        COLLAPSED.clear();
        EXPANDED.clear();
        ArrayList<String> arrayList = new ArrayList<String>();
        block8: for (String string2 : string.split(";")) {
            if (string2.length() < 3 || string2.charAt(1) != ':') continue;
            String string3 = string2.substring(2);
            switch (string2.charAt(0)) {
                case 'P': {
                    String[] stringArray = string3.split(",");
                    if (stringArray.length != 3) continue block8;
                    try {
                        float f2 = Float.parseFloat(stringArray[1]);
                        float f3 = Float.parseFloat(stringArray[2]);
                        POS.put(stringArray[0], new float[]{f2 * this.viewW, f3 * this.viewH});
                    }
                    catch (NumberFormatException numberFormatException) {}
                    continue block8;
                }
                case 'C': {
                    COLLAPSED.add(string3);
                    continue block8;
                }
                case 'E': {
                    EXPANDED.add(string3);
                    continue block8;
                }
                case 'Z': {
                    arrayList.add(string3);
                    continue block8;
                }
            }
        }
        if (!arrayList.isEmpty()) {
            Z_ORDER.clear();
            Z_ORDER.addAll(arrayList);
        }
    }

    private void saveLayout() {
        StringSetting stringSetting = HitboxConfigScreen752809.W;
        if (stringSetting == null || this.viewW <= 0.0f || this.viewH <= 0.0f) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (PanelDef object : this.lastDefs) {
            float[] fArray = POS.get(object.key());
            if (fArray == null || fArray[0] <= 0.0f && fArray[1] <= 0.0f) continue;
            if (stringBuilder.length() > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append("P:").append(object.key()).append(',').append(String.format(Locale.ROOT, DropdownGpuClickGuiScreen.hj("\ud02b\ue7d1\uc0ad\u93e1\ua2b9\u12b4\u97c9\u5e2f", 1261118809, 0x229D082C ^ CryptoVault.mask()), Float.valueOf(fArray[0] / this.viewW))).append(',').append(String.format(Locale.ROOT, DropdownGpuClickGuiScreen.hj("\u20a9\ud97d\u1bfb\uf0f9\u931d\u2366", -597891717, 0x229D082C ^ CryptoVault.mask()), Float.valueOf(fArray[1] / this.viewH)));
        }
        if (this.lastDefs.isEmpty()) {
            return;
        }
        for (String string : COLLAPSED) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append("C:").append(string);
        }
        for (String string : EXPANDED) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append("E:").append(string);
        }
        for (String string : Z_ORDER) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(';');
            }
            stringBuilder.append("Z:").append(string);
        }
        String string = stringBuilder.toString();
        if (string.equals(lastLayout)) {
            return;
        }
        lastLayout = string;
        stringSetting.G(string);
    }

    private List<PanelDef> panelDefs() {
        ArrayList<PanelDef> arrayList = new ArrayList<PanelDef>();
        for (Category category : PANELS) {
            arrayList.add(new PanelDef(category.name(), category.zw, ClassicIcons.forCategory(category.name()), category, null, false));
        }
        for (GuiPrefs.CustomCategory customCategory : DropdownGpuClickGuiScreen.guiPrefs().customCategories()) {
            arrayList.add(new PanelDef(DropdownGpuClickGuiScreen.hj("\ub072\ubaf8\ud3bc\u62ee\u1a85\u4bd8\u3c26\u8b07\ua259\u6dad", 247411265, 0x229D082C ^ CryptoVault.mask()) + customCategory.id(), customCategory.name(), DropdownGpuClickGuiScreen.hj("\ua546\u9329\u5147\u0444", 2043270537, 0x229D082C ^ CryptoVault.mask()), null, customCategory.id(), false));
        }
        if (this.searchField.isEmpty() && moduleFilter == GpuClickGuiScreen.ModuleFilter.ALL && !this.quickPins().isEmpty()) {
            arrayList.add(new PanelDef(DropdownGpuClickGuiScreen.hj("\u183a\ud499\ub70b\ued04\ub775\u4728\u2add\ue590\ua966", -1461346136, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u4866\u8788\ud1b3\ua6c4\ud4b9\u446d\u7c35\u93fb\u9843\u7eb2\uce41\u3f35\ue206\u384e\ue4e1\u678d\ua7c4", -926030640, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ue766\u0e0e\u0c29\ube73\uaab8\u8931", 320136197, 0x229D082C ^ CryptoVault.mask()), null, null, true));
        }
        return arrayList;
    }

    private static List<PanelDef> zOrderedPanels(List<PanelDef> list) {
        for (PanelDef object : list) {
            if (Z_ORDER.contains(object.key())) continue;
            Z_ORDER.add(object.key());
        }
        Z_ORDER.retainAll(list.stream().map(PanelDef::key).toList());
        ArrayList arrayList = new ArrayList(Z_ORDER.size());
        for (String string : Z_ORDER) {
            for (PanelDef panelDef : list) {
                if (!panelDef.key().equals(string)) continue;
                arrayList.add(panelDef);
            }
        }
        return arrayList;
    }

    private static void bringToFront(String string) {
        Z_ORDER.remove(string);
        Z_ORDER.add(string);
    }

    public DropdownGpuClickGuiScreen() {
        this.lastFrame = this.openedAt = this.nanoTime();
        try {
            this.refreshSavedConfigs();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Override
    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        float f3;
        if (this.closing && this.nanoTime() - this.closedAt > 160000000L) {
            if (this.field_22787 != null) {
                McCompat.setScreen(this.field_22787, this.returnScreen);
            }
            return;
        }
        this.ppg = f3 = this.pixelsPerGuiUnit();
        float f4 = (float)n2 * f3;
        float f5 = (float)n3 * f3;
        if (this.field_22787 != null && this.field_22787.field_1729 != null && this.field_22787.method_22683() != null && this.field_22787.method_22683().method_4480() > 0) {
            double d2 = (double)this.field_22787.method_22683().method_4486() / (double)this.field_22787.method_22683().method_4480();
            f4 = (float)(this.field_22787.field_1729.method_1603() * d2 * (double)f3);
            f5 = (float)(this.field_22787.field_1729.method_1604() * d2 * (double)f3);
        }
        long l2 = PerfProbe.on ? System.nanoTime() : 0L;
        UiDisplayList uiDisplayList = this.buildDropdown(Math.round(f4), Math.round(f5), (float)this.field_22789 * f3, (float)this.field_22790 * f3);
        if (l2 != 0L) {
            PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u305a\ucf34\u17e7\u4a78\u3c5f\u1de8\uc9bf\u8752\uba1a\ue0bf\u3143\u053f\u5d80\ud2da", 1966832376, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u2025\u4990\u7555\u9912\u10f9\u7d92\u61c4\u81a4\uc8f2", -1343739995, 0x229D082C ^ CryptoVault.mask()), System.nanoTime() - l2);
        }
        if (uiDisplayList != null) {
            GpuUiRenderer.renderPixels(class_3322, uiDisplayList, f3);
        }
        if (uiDisplayList != null) {
            this.hoverSound(f4, f5, (float)this.field_22789 * f3 * (float)this.field_22790 * f3);
        }
        if (uiDisplayList != null && DropdownGpuClickGuiScreen.hj("\u5c2e\u51c2\ucc75\uf4ae\u4401\u714a\u111c\ue5c5\u4caa\uba40", 1177046930, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && !this.closing) {
            this.drawCosmeticsLayers(class_3322, f3);
        }
    }

    private void hoverSound(float f2, float f3, float f4) {
        int n2;
        int n3 = 0;
        for (n2 = this.hits.size() - 1; n2 >= 0; --n2) {
            Hit hit = this.hits.get(n2);
            if (!hit.contains(f2, f3)) continue;
            if (hit.w() * hit.h() > f4 * 0.25f) break;
            n3 = (Math.round(hit.x()) * 31 + Math.round(hit.y())) * 31 + Math.round(hit.w()) * 7 + Math.round(hit.h()) | 1;
            break;
        }
        n2 = f2 != this.hoverSoundX || f3 != this.hoverSoundY ? 1 : 0;
        this.hoverSoundX = f2;
        this.hoverSoundY = f3;
        if (n3 == this.hoverSoundKey) {
            return;
        }
        this.hoverSoundKey = n3;
        if (n3 != 0 && n2 != 0 && !this.closing && this.draggingSlider == null && this.dragPanel == null) {
            UiSounds.play(UiSounds.Event.HOVER);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void drawCosmeticsLayers(class_332 class_3322, float f2) {
        CosmeticsProvider cosmeticsProvider = ModuleAccess.getHook(CosmeticsProvider.class);
        if (cosmeticsProvider == null) {
            return;
        }
        float[] fArray = this.cosmeticsView.previewRect();
        if (fArray[2] > 0.0f) {
            cosmeticsProvider.drawPreview(class_3322, Math.round(fArray[0] / f2), Math.round(fArray[1] / f2), Math.round((fArray[0] + fArray[2]) / f2), Math.round((fArray[1] + fArray[3]) / f2), this.cosmeticsView.previewYaw(), this.cosmeticsView.previewPitch(), this.cosmeticsView.previewRoll());
        }
        List<CosmeticsView.Thumbnail> list = this.cosmeticsView.thumbnails();
        ThumbnailCache.P();
        if (list.isEmpty()) {
            return;
        }
        float[] fArray2 = this.cosmeticsView.thumbnailClip();
        ScissorClamp.enable(class_3322, Math.round(fArray2[0] / f2), Math.round(fArray2[1] / f2), Math.round((fArray2[0] + fArray2[2]) / f2), Math.round((fArray2[1] + fArray2[3]) / f2));
        try {
            for (CosmeticsView.Thumbnail thumbnail : list) {
                if (thumbnail.weapon() != null) {
                    cosmeticsProvider.drawWeaponThumbnail(class_3322, Math.round(thumbnail.x() / f2), Math.round(thumbnail.y() / f2), Math.round((thumbnail.x() + thumbnail.w()) / f2), Math.round((thumbnail.y() + thumbnail.h()) / f2), thumbnail.weapon());
                    continue;
                }
                cosmeticsProvider.drawThumbnail(class_3322, Math.round(thumbnail.x() / f2), Math.round(thumbnail.y() / f2), Math.round((thumbnail.x() + thumbnail.w()) / f2), Math.round((thumbnail.y() + thumbnail.h()) / f2), thumbnail.selection(), thumbnail.frame());
            }
        }
        finally {
            class_3322.method_44380();
        }
    }

    private UiDisplayList buildDropdown(int n2, int n3, float f9, float f10) {
        int n4;
        String string;
        boolean bl;
        float f11;
        boolean bl2;
        long l2 = this.nanoTime();
        this.dt = (float)Math.min(0.1, Math.max(0.0, (double)(l2 - this.lastFrame) / 1.0E9));
        this.lastFrame = l2;
        this.pruneEases();
        this.mx = n2;
        this.my = n3;
        this.sw = f9;
        this.sh = f10;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        float f12 = HitboxConfigScreen752809.S != null ? (float)HitboxConfigScreen752809.b() : 1.0f;
        float f13 = 1.0f;
        if (HitboxConfigScreen752809.T != null && DropdownGpuClickGuiScreen.hj("\u938d\uea7f\u7642\u0e93\u14a3", -274507955, 0x229D082C ^ CryptoVault.mask()).equals(HitboxConfigScreen752809.T.getCurrentMode())) {
            f13 = AutoMenuScale.correction(this.sh, AutoMenuScale.currentPxPerMm(this.field_22787));
        }
        this.s = Math.max(0.5f, ResponsiveLayout.classicAutoScale(this.sh, f13) * f12);
        this.viewW = this.sw / this.s;
        this.viewH = this.sh / this.s;
        long l3 = PerfProbe.on ? System.nanoTime() : 0L;
        this.hoverOwnerPanel = this.panelAt(this.mx, this.my);
        this.hits.clear();
        this.rightHits.clear();
        this.grips.clear();
        this.scrollZones.clear();
        this.rowRects.clear();
        this.panelLists.clear();
        this.panelGroups.clear();
        this.viewCache.clear();
        this.tourRects.clear();
        this.tipTitle = null;
        this.tipDesc = null;
        this.ready = true;
        this.palette();
        this.lastDefs = this.panelDefs();
        this.ensureLayout();
        if (!this.closing) {
            this.ddTour.maybeAutoStart();
        }
        if (this.ddTour.active()) {
            OnboardingTour.Target target = this.ddTour.target();
            if ((target == OnboardingTour.Target.PANEL_CHEVRON || target == OnboardingTour.Target.MODULE_ROW || target == OnboardingTour.Target.MODULE_CHEVRON) && COLLAPSED.remove(PANELS[0].name())) {
                this.tourReopenedFirstPanel = true;
            }
        } else if (this.tourReopenedFirstPanel) {
            COLLAPSED.add(PANELS[0].name());
            this.tourReopenedFirstPanel = false;
        }
        float f14 = Math.max(0.0f, (float)(l2 - this.openedAt) / 1.0E9f);
        float f15 = this.closing ? 1.0f - DropdownGpuClickGuiScreen.clamp((float)(l2 - this.closedAt) / 1.6E8f, 0.0f, 1.0f) : 1.0f;
        float f16 = Math.min(1.0f, f14 * 3.0f) * f15;
        UiDisplayList.Builder builder2 = UiDisplayList.builder();
        Canvas canvas = new Canvas(builder2);
        boolean bl3 = bl2 = this.field_22787 == null || this.field_22787.field_1687 != null;
        if (bl2 && HitboxConfigScreen752809.M != null && HitboxConfigScreen752809.M.isEnabled() && (f11 = Math.min(8.0f, (float)HitboxConfigScreen752809.N.B() * 0.4f) * f16) > 0.3f) {
            builder2.blur(0.0f, 0.0f, this.sw, this.sh, 0.0f, f11);
        }
        builder2.gradient(0.0f, 0.0f, this.sw, this.sh, 0.0f, DropdownGpuClickGuiScreen.argb(64.0f * f16, 0), DropdownGpuClickGuiScreen.argb(64.0f * f16, 0), DropdownGpuClickGuiScreen.argb(134.0f * f16, 0), DropdownGpuClickGuiScreen.argb(134.0f * f16, 0));
        if (l3 != 0L) {
            long l4 = System.nanoTime();
            PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u3b55\udafb\u6be8\uf079\uc730\u9163\u0d10\u8888\u6902\ucd64\u909a\uf65f\u397b", -339519803, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ue6c0\u35bc\u5776\u1181\u2237\u030e", -1940444989, 0x229D082C ^ CryptoVault.mask()), l4 - l3);
            l3 = l4;
        }
        this.drawAtmosphere(canvas, f16);
        if (l3 != 0L) {
            long l5 = System.nanoTime();
            PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u7b0f\ue7c0\u738b\ub349\u529b\ubfad\ua2a7\uda8a\ud4e8\u591a\ud4d1\u5fed\u707b\u3aab\uacad", -1593670418, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u5966\ua222\ua437\u40d0\u2194\uc595\u7007\u16e2\uc8ab\u23ca\u382b", -1397692010, 0x229D082C ^ CryptoVault.mask()), l5 - l3);
            l3 = l5;
        }
        int n5 = this.mx;
        int n6 = this.my;
        boolean bl4 = bl = this.ctxItems != null || this.overlayView != null || this.colorPicker != null || this.whatsNew.isOpen() || this.ddTour.active();
        if (bl) {
            this.mx = -100000;
            this.my = -100000;
        }
        this.drawTopBar(canvas, f14, f15);
        this.drawToolStrip(canvas, f14, f15);
        if (l3 != 0L) {
            long l6 = System.nanoTime();
            PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u9745\u7615\u03bf\u4547\u0ab6\u24ea\ufb19\uab61\u61c1\u52f3\u7ae0\u987b\uf520\u28f8", -1832433283, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\uf805\u80fc\u22f9\u8de2\u18da\udc86", -1581888639, 0x229D082C ^ CryptoVault.mask()), l6 - l3);
            l3 = l6;
        }
        boolean bl5 = !(string = this.searchField.value().trim()).isEmpty() || moduleFilter != GpuClickGuiScreen.ModuleFilter.ALL;
        this.reorderable = !bl5 && moduleSort == GpuClickGuiScreen.ModuleSort.DEFAULT;
        this.navModules = new ArrayList<Module>();
        List<PanelDef> list = DropdownGpuClickGuiScreen.zOrderedPanels(this.lastDefs);
        int n7 = 0;
        for (n4 = 0; n4 < list.size(); ++n4) {
            List<GpuClickGuiScreen.CatalogEntry> list2;
            PanelDef panelDef = list.get(n4);
            long l7 = l3 != 0L ? System.nanoTime() : 0L;
            List<GpuClickGuiScreen.CatalogEntry> list3 = list2 = panelDef.quick() ? List.of() : this.panelEntries(panelDef, string);
            if (l7 != 0L) {
                PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u9e48\ua7e2\u4187\uad9e\u3561\ubc52\ua78c\ud9be\ub11c\uca4d\uf493\uad84\uf0aa\u77fc", -2051643430, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u72bf\u13b9\u5702\u8f4e\ufe34\u56d7\ub2e6\ub716\ue48f\ufdda\uad0b\uf41c\ua47a\ub248\u316d\u0cbb", 1991668854, 0x229D082C ^ CryptoVault.mask()), System.nanoTime() - l7);
            }
            if (bl5 && !panelDef.quick() && list2.isEmpty()) continue;
            float f17 = this.reveal(f14, 0.06f + (float)n4 * 0.03f);
            canvas.hoverSuppressed = !panelDef.key().equals(this.hoverOwnerPanel);
            this.drawPanel(canvas, panelDef, list2, bl5, f15 * f17, n4);
            ++n7;
        }
        canvas.hoverSuppressed = false;
        if (l3 != 0L) {
            long l8 = System.nanoTime();
            PerfProbe.module(DropdownGpuClickGuiScreen.hj("\u826a\ua724\u94f0\uc5d0\u2424\u2f04\ua72d\ucb3c\u9d58\ub388\ua3a3\ud448\u2dc2\u7760", 1341382650, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u9c8f\u47e4\u4ba0\ub1be\u4dee\u39fd\u06ba\u8782\u9c5b\uf3ff\u0ee3\uccc1\u6e86", 1012323283, 0x229D082C ^ CryptoVault.mask()), l8 - l3);
            l3 = l8;
        }
        if (bl5 && n7 == 0) {
            canvas.alpha = f15;
            canvas.text(34.0f, 110.0f, 11.0f, this.muted, string.isEmpty() ? DropdownGpuClickGuiScreen.hj("\u944f\u2f3d\u6689\u3b41\uf787\u3952\uba53\uf347\ubb96\u3b58\ua803\u551e\ud99d\ud9be\ufa8e\u1e1a\u5cfd\ub488\u06cf\u746d\u2f16\udcaa\ud044\uf66b\u3e66\u4680\uaa73\uc393\u1d91\u46ea", 1011094627, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\ub84d\ub924\u4973\u7cf1\ub995\u8ff4\ubeb7\u11ec\uec83\u8212\u0d09\u9bc0\ud02f\u973f\u8134\u9b7d\u7db7\ua0dc\ue525\uf4d5\uc119\ud25d\u4754\u75c8\u20e7\ueacc\u4f80\ue112\u5dc4\u23c5\u1449\u6d8c", 1617390856, 0x229D082C ^ CryptoVault.mask()));
            canvas.alpha = 1.0f;
        }
        if (this.navIndex >= this.navModules.size()) {
            this.navIndex = this.navModules.isEmpty() ? -1 : this.navModules.size() - 1;
        }
        this.mx = n5;
        this.my = n6;
        if (this.dragModule != null) {
            this.drawDragOverlay(canvas, f15);
        }
        int n8 = n4 = this.colorPicker != null || this.whatsNew.isOpen() || this.ddTour.active() ? 1 : 0;
        if (n4 != 0) {
            this.mx = -100000;
            this.my = -100000;
        }
        if (this.ctxItems != null && this.overlayView == null) {
            this.tipTitle = null;
            this.tipDesc = null;
            this.drawContextMenu(canvas, f15);
        }
        if (this.overlayView != null) {
            this.tipTitle = null;
            this.tipDesc = null;
            this.hits.clear();
            this.rightHits.clear();
            this.grips.clear();
            this.scrollZones.clear();
            this.drawOverlay(canvas, f15);
            if (this.ctxItems != null) {
                this.drawContextMenu(canvas, f15);
            }
        }
        this.mx = n5;
        this.my = n6;
        if (this.colorPicker != null) {
            this.tipTitle = null;
            this.tipDesc = null;
            this.hits.clear();
            this.rightHits.clear();
            this.grips.clear();
            this.scrollZones.clear();
            this.drawColorPicker(canvas, f15);
        }
        if (this.ddTour.active() || this.whatsNew.isOpen()) {
            this.tipTitle = null;
            this.tipDesc = null;
        }
        if (this.tipDesc != null && !this.tipDesc.isBlank()) {
            this.drawTooltip(canvas, f15);
        }
        if (this.whatsNew.isOpen() && !this.closing) {
            this.hits.clear();
            this.rightHits.clear();
            this.grips.clear();
            this.scrollZones.clear();
            this.whatsNew.draw(builder2, this.host, this.sw, this.sh, this.s, this.mx, this.my);
        }
        if (this.ddTour.active() && !this.closing) {
            this.hits.clear();
            this.rightHits.clear();
            this.grips.clear();
            this.ddTour.setLayoutPainter((builder, f2, f3, f4, f5, f6, f7, f8) -> this.drawLayoutReplicas(builder, f2, f3, f4, f5));
            this.ddTour.draw(builder2, this.host, this.sw, this.sh, this.s, this.tourRect(), this.mx, this.my);
        }
        if (OverlayCompositor.isSsProofActive() && this.field_22787 != null && this.field_22787.method_22683() != null) {
            ClassicIcons.drawCursor(builder2, this.mx, this.my, this.field_22787.method_22683().method_4495());
        }
        return builder2.build();
    }

    private void palette() {
        String string;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        this.themeName = string = hitboxConfigScreen752809 == null || HitboxConfigScreen752809.a == null ? "" : HitboxConfigScreen752809.a.getCurrentMode();
        this.activeSurface = HitboxConfigScreen752809.c();
        this.materialPanel = !this.activeSurface.isTheme() || UiThemeMaterial.isStone(string);
        UiThemeMaterial.Theme theme = UiThemeMaterial.theme(string);
        this.accent = 0xFF000000 | (hitboxConfigScreen752809 != null && HitboxConfigScreen752809.I != null && HitboxConfigScreen752809.I.isEnabled() ? HitboxConfigScreen752809.K.A().getRGB() : theme.accent());
        this.text = HitboxConfigScreen752809.a(HitboxConfigScreen752809.al, theme.text());
        this.muted = HitboxConfigScreen752809.a(HitboxConfigScreen752809.am, theme.muted());
        this.toggleOff = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ao, theme.toggleOff());
        this.rim = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ar, theme.outer());
        this.panelBase = theme.panelBase() & 0xFFFFFF;
        int n2 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ai, 0);
        if (n2 != 0) {
            this.panelBase = DropdownGpuClickGuiScreen.mix(this.panelBase | 0xFF000000, n2 | 0xFF000000, Math.min(0.6f, (float)(n2 >>> 24 & 0xFF) / 255.0f)) & 0xFFFFFF;
        }
        this.deep = theme.deep() & 0xFFFFFF;
    }

    private void drawAtmosphere(Canvas canvas, float f2) {
        boolean bl;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        if (hitboxConfigScreen752809 == null) {
            return;
        }
        boolean bl2 = HitboxConfigScreen752809.c().isTheme() && HitboxConfigScreen752809.b != null && HitboxConfigScreen752809.b.is("On");
        boolean bl3 = HitboxConfigScreen752809.g != null && HitboxConfigScreen752809.g.isEnabled();
        boolean bl4 = HitboxConfigScreen752809.h != null && HitboxConfigScreen752809.h.isEnabled();
        boolean bl5 = bl = HitboxConfigScreen752809.i != null && HitboxConfigScreen752809.i.isEnabled();
        if (bl2) {
            canvas.b.material(0.0f, 0.0f, this.sw, this.sh, 0.0f, UiClassicMistMaterial.flux());
        }
        if (bl3) {
            float f3;
            float f4 = this.sw > 0.0f ? DropdownGpuClickGuiScreen.clamp((float)this.mx / this.sw, 0.0f, 1.0f) : 0.5f;
            float f5 = f3 = this.sh > 0.0f ? DropdownGpuClickGuiScreen.clamp((float)this.my / this.sh, 0.0f, 1.0f) : 0.5f;
            if (Float.isNaN(this.waterPrevMx)) {
                this.waterPrevMx = f4;
                this.waterPrevMy = f3;
                this.waterVelX = 0.0f;
                this.waterVelY = 0.0f;
            }
            float f6 = Math.min(1.0f, this.dt * 12.0f);
            this.waterVelX += (f4 - this.waterPrevMx - this.waterVelX) * f6;
            this.waterVelY += (f3 - this.waterPrevMy - this.waterVelY) * f6;
            this.waterPrevMx = f4;
            this.waterPrevMy = f3;
            if (Math.abs(this.waterVelX) < 3.0E-4f) {
                this.waterVelX = 0.0f;
            }
            if (Math.abs(this.waterVelY) < 3.0E-4f) {
                this.waterVelY = 0.0f;
            }
            float f7 = (float)(HitboxConfigScreen752809.t.B() / 100.0) * f2;
            float f8 = (float)(HitboxConfigScreen752809.u.B() / 100.0);
            int n2 = (int)Math.round(HitboxConfigScreen752809.p.B());
            if (GpuFluidSimulation.usable()) {
                GpuFluidSimulation.configure(HitboxConfigScreen752809.w.is(DropdownGpuClickGuiScreen.hj("\u9a8e\ueb12\u0a25\u0701", -672402685, 0x229D082C ^ CryptoVault.mask())) ? 0 : (HitboxConfigScreen752809.w.is(DropdownGpuClickGuiScreen.hj("\ua7d3\u69f5\ub54e\uf7df\u0837\u550d\ua35f\u2056\uc324", -1150374441, 0x229D082C ^ CryptoVault.mask())) ? 1 : 2), (float)HitboxConfigScreen752809.y.B(), (float)HitboxConfigScreen752809.z.B(), (float)HitboxConfigScreen752809.A.B(), (float)HitboxConfigScreen752809.B.B(), (float)HitboxConfigScreen752809.C.B(), (int)Math.round(HitboxConfigScreen752809.D.B()), HitboxConfigScreen752809.E.isEnabled(), HitboxConfigScreen752809.F.isEnabled(), !HitboxConfigScreen752809.x.is(DropdownGpuClickGuiScreen.hj("\u1728\u7f84\ub01a\u191c\u6c0e\u54d5\u063d\u3b11\u77b5\u0560\u5abb\u6fc6\u7d80", -591970867, 0x229D082C ^ CryptoVault.mask())), HitboxConfigScreen752809.G.isEnabled(), this.closing);
                canvas.b.fluid(0.0f, 0.0f, this.sw, this.sh, 0.0f, f4, f3, this.waterVelX, this.waterVelY, HitboxConfigScreen752809.q.A().getRGB(), HitboxConfigScreen752809.r.A().getRGB(), HitboxConfigScreen752809.s.A().getRGB(), n2, f7, f8, HitboxConfigScreen752809.v.isEnabled() && !this.closing);
            } else {
                canvas.b.material(0.0f, 0.0f, this.sw, this.sh, 0.0f, UiWaterMaterial.of(f4, f3, this.waterVelX, this.waterVelY, HitboxConfigScreen752809.q.A().getRGB(), HitboxConfigScreen752809.r.A().getRGB(), HitboxConfigScreen752809.s.A().getRGB(), n2, f7, f8, HitboxConfigScreen752809.v.isEnabled()));
            }
        }
        if (bl4) {
            canvas.b.material(0.0f, 0.0f, this.sw, this.sh, 0.0f, UiAuroraMaterial.of(this.accent & 0xFFFFFF, (float)(HitboxConfigScreen752809.n.B() / 100.0) * f2));
        }
        if (bl) {
            canvas.b.material(0.0f, 0.0f, this.sw, this.sh, 0.0f, UiStarfieldMaterial.of(this.accent & 0xFFFFFF, (float)(HitboxConfigScreen752809.o.B() / 100.0) * f2));
        }
    }

    /*
     * Exception decompiling
     */
    private void ensureLayout() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.UnsupportedOperationException
         *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.NewAnonymousArray.getDimSize(NewAnonymousArray.java:142)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.isNewArrayLambda(LambdaRewriter.java:455)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:409)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:167)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:105)
         *     at org.benf.cfr.reader.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper.applyForwards(ExpressionRewriterHelper.java:12)
         *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractMemberFunctionInvokation.applyExpressionRewriterToArgs(AbstractMemberFunctionInvokation.java:101)
         *     at org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractMemberFunctionInvokation.applyExpressionRewriter(AbstractMemberFunctionInvokation.java:88)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:103)
         *     at org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredExpressionStatement.rewriteExpressions(StructuredExpressionStatement.java:70)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewrite(LambdaRewriter.java:88)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.rewriteLambdas(Op04StructuredStatement.java:1137)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:912)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void separateOverlappingPanels() {
        ArrayList<float[]> arrayList = new ArrayList<float[]>();
        ArrayList<float[]> arrayList2 = new ArrayList<float[]>();
        for (PanelDef object : this.lastDefs) {
            float[] fArray3 = POS.get(object.key());
            if (fArray3 == null) continue;
            arrayList2.add(fArray3);
        }
        arrayList2.sort((fArray, fArray2) -> Math.abs(fArray[1] - fArray2[1]) > 36.0f ? Float.compare(fArray[1], fArray2[1]) : Float.compare(fArray[0], fArray2[0]));
        float f2 = Math.max(4.0f, this.viewW - 206.0f - 4.0f);
        for (float[] fArray3 : arrayList2) {
            for (int i2 = 0; i2 < 40; ++i2) {
                float[] fArray4 = null;
                for (float[] fArray5 : arrayList) {
                    if (!(Math.abs(fArray5[1] - fArray3[1]) < 36.0f) || !(Math.abs(fArray5[0] - fArray3[0]) < 144.2f)) continue;
                    fArray4 = fArray5;
                    break;
                }
                if (fArray4 == null) break;
                void var8_11 = fArray4[0] + 206.0f + 12.0f;
                if (var8_11 <= f2) {
                    fArray3[0] = var8_11;
                    continue;
                }
                fArray3[0] = 30.0f;
                fArray3[1] = Math.min(fArray3[1] + 220.0f, Math.max(96.0f, this.viewH - 36.0f - 8.0f));
            }
            arrayList.add(fArray3);
        }
    }

    private float[] tourRect() {
        float[] fArray = this.tourRects.get((Object)this.ddTour.target());
        return fArray == null || fArray[2] <= 0.0f || fArray[3] <= 0.0f ? null : fArray;
    }

    private void tourMark(OnboardingTour.Target target2, float f2, float f3, float f4, float f5) {
        float[] fArray = this.tourRects.computeIfAbsent(target2, target -> new float[4]);
        fArray[0] = f2 * this.s;
        fArray[1] = f3 * this.s;
        fArray[2] = f4 * this.s;
        fArray[3] = f5 * this.s;
    }

    /*
     * Exception decompiling
     */
    private static OnboardingTour.Target tourTargetFor(String var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void replayTour() {
        this.closeOverlay();
        this.ddTour.start();
    }

    private float panelBodyLimit() {
        return Math.max(120.0f, this.viewH - 28.0f);
    }

    private float panelBottomLimit(String string, float f2, float f3) {
        float f4 = this.panelBodyLimit();
        for (PanelDef panelDef : this.lastDefs) {
            float[] fArray;
            if (panelDef.key().equals(string) || (fArray = POS.get(panelDef.key())) == null || fArray[1] <= f3 + 36.0f || Math.abs(fArray[0] - f2) >= 206.0f) continue;
            f4 = Math.min(f4, fArray[1] - 12.0f);
        }
        return Math.max(f3 + 36.0f + 60.0f, f4);
    }

    private List<GpuClickGuiScreen.CatalogEntry> panelEntries(PanelDef panelDef, String string) {
        Record record;
        List<Module> list = panelDef.cat() != null ? this.modulesForCategory(panelDef.cat()) : (panelDef.folderId() != null ? this.modulesForFolder(panelDef.folderId()) : List.of());
        String string2 = string.toLowerCase(Locale.ROOT);
        ArrayList<GpuClickGuiScreen.CatalogEntry> arrayList = new ArrayList<GpuClickGuiScreen.CatalogEntry>();
        for (Module object2 : list) {
            if (object2 instanceof HitboxConfigScreen752809 || !this.passesFilter(object2) || (record = this.searchEntry(object2, string2)) == null) continue;
            arrayList.add((GpuClickGuiScreen.CatalogEntry)record);
        }
        Comparator<GpuClickGuiScreen.CatalogEntry> comparator = Comparator.comparingInt(catalogEntry -> catalogEntry.module().isFavorite() ? 0 : 1);
        Comparator<GpuClickGuiScreen.CatalogEntry> comparator2 = switch (moduleSort) {
            default -> throw new MatchException(null, null);
            case GpuClickGuiScreen.ModuleSort.DEFAULT -> comparator.thenComparingInt(GpuClickGuiScreen.CatalogEntry::rank);
            case GpuClickGuiScreen.ModuleSort.AZ -> comparator.thenComparing(catalogEntry -> catalogEntry.module().getName(), String.CASE_INSENSITIVE_ORDER);
            case GpuClickGuiScreen.ModuleSort.ENABLED_FIRST -> comparator.thenComparingInt(catalogEntry -> catalogEntry.module().isEnabled() ? 0 : 1).thenComparingInt(GpuClickGuiScreen.CatalogEntry::rank);
        };
        arrayList.sort(comparator2);
        if (string2.isEmpty() && panelDef.cat() != null) {
            record = CatalogSections.arrange(arrayList, catalogEntry -> catalogEntry.module().getSubCategory());
            arrayList = new ArrayList(((CatalogSections.Arranged)record).items());
            this.panelGroups.put(panelDef.key(), ((CatalogSections.Arranged)record).groups());
        } else {
            this.panelGroups.remove(panelDef.key());
        }
        return arrayList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private float quickRowHeight(Canvas canvas, GpuClickGuiScreen.QuickPin quickPin, float f2) {
        String string = this.rowPrefix;
        this.rowPrefix = quickPin.module().getName() + DropdownGpuClickGuiScreen.hj("\u8bfc\u7a46\uef33\u390a", 85692534, 0x229D082C ^ CryptoVault.mask());
        try {
            float f3 = this.rowHeight(canvas, quickPin.setting(), f2);
            return f3;
        }
        finally {
            this.rowPrefix = string;
        }
    }

    private void drawPanel(Canvas canvas, PanelDef panelDef, List<GpuClickGuiScreen.CatalogEntry> list, boolean bl, float f2, int n2) {
        float f3;
        float f4;
        boolean bl2;
        float f5;
        String string = panelDef.key();
        float[] fArray = POS.get(string);
        float f6 = fArray[0];
        float f7 = fArray[1];
        boolean bl3 = COLLAPSED.contains(string) && !bl;
        boolean bl4 = panelDef.quick();
        List<Object> list2 = bl4 ? this.quickPins() : List.of();
        boolean bl5 = panelDef.cat() == Category.fK && !bl && !bedwarsNoticeContinued && (HitboxConfigScreen752809.aH == null || !HitboxConfigScreen752809.aH.isEnabled());
        boolean bl6 = panelDef.folderId() != null && list.isEmpty() && !bl;
        float f8 = 184.0f;
        ArrayList<Module> arrayList = new ArrayList<Module>(list.size());
        for (GpuClickGuiScreen.CatalogEntry object2 : list) {
            arrayList.add(object2.module());
        }
        this.panelLists.put(string, arrayList);
        List list3 = this.panelGroups.getOrDefault(string, List.of());
        HashMap<Integer, CatalogSections.Group> hashMap = new HashMap<Integer, CatalogSections.Group>();
        Object object = list3.iterator();
        while (object.hasNext()) {
            CatalogSections.Group f52 = (CatalogSections.Group)object.next();
            hashMap.put(f52.start(), f52);
        }
        object = new HashMap();
        float f9 = f5 = bl5 ? 78.0f : 0.0f;
        if (bl6) {
            f5 += 26.0f;
        }
        boolean bl7 = bl2 = panelDef.cat() == Category.vR;
        if (bl2) {
            f5 += this.aiBodyHeight(canvas, f8);
        } else if (bl4) {
            for (GpuClickGuiScreen.QuickPin f11 : list2) {
                f5 += 4.0f + this.quickRowHeight(canvas, f11, f8);
            }
        } else {
            f5 += (float)list3.size() * 20.0f;
            for (GpuClickGuiScreen.CatalogEntry catalogEntry : list) {
                Module module = catalogEntry.module();
                f4 = this.ease(this.modKey(0, module), !DropdownGpuClickGuiScreen.floatingModuleSettings() && EXPANDED.contains(module.getName()) ? 1.0f : 0.0f, 14.0f);
                object.put(module.getName(), Float.valueOf(f4));
                f5 += this.moduleBlockHeight(canvas, module, f4);
            }
        }
        float f10 = Math.min(f5 += 6.0f, this.panelBottomLimit(string, f6, f7) - (f7 + 36.0f));
        f10 = Math.max(0.0f, f10);
        float f11 = this.ease(DropdownGpuClickGuiScreen.hj("\ue514\u84aa\u833c\u8e42\u4f29\u267a\u7776\u58f7\ua36a", -1757752466, 0x229D082C ^ CryptoVault.mask()) + string, bl3 ? 0.0f : 1.0f, 15.0f);
        float f12 = f10 * f11;
        f4 = 36.0f + f12 + (f12 > 0.0f ? 8.0f : 0.0f);
        this.scrollZones.add(new ScrollZone(f6 * this.s, f7 * this.s, 206.0f * this.s, f4 * this.s, string));
        boolean bl8 = string.equals(PANELS[0].name());
        if (bl8) {
            this.tourMark(OnboardingTour.Target.PANEL, f6, f7, 206.0f, f4);
            this.tourMark(OnboardingTour.Target.PANEL_CHEVRON, f6 + 206.0f - 30.0f, f7 + 7.0f, 24.0f, 22.0f);
        }
        canvas.hit(f6, f7, 206.0f, f4, () -> {});
        canvas.alpha = f2;
        this.panel(canvas, f6, f7, 206.0f, f4, 14.0f, 1.0f);
        float f13 = this.reducedMotion() ? 0.5f : 0.5f + 0.5f * (float)Math.sin((double)System.nanoTime() / 2.2E9 + (double)n2 * 0.8);
        float f14 = this.reducedMotion() ? 1.0f : Math.min(1.0f, Math.max(0.0f, (float)(System.nanoTime() - this.openedAt) / 1.0E9f - 0.06f - (float)n2 * 0.03f));
        canvas.border(f6, f7, 206.0f, f4, 14.0f, this.hair(), DropdownGpuClickGuiScreen.argb((int)(20.0f + 12.0f * f13 + 64.0f * (1.0f - f14) * f14 * 4.0f * (f14 < 1.0f ? 1.0f : 0.0f)), this.accent));
        int n3 = 0;
        for (GpuClickGuiScreen.CatalogEntry catalogEntry : list) {
            if (!catalogEntry.module().isEnabled()) continue;
            ++n3;
        }
        boolean bl9 = canvas.hov(f6, f7, 206.0f, 36.0f);
        boolean bl10 = panelDef.folderId() != null && panelDef.folderId().equals(this.renamingFolderId);
        canvas.icon(panelDef.icon(), f6 + 14.0f, f7 + 10.0f, 16.0f, DropdownGpuClickGuiScreen.mix(this.muted, this.text, bl9 ? 1.0f : 0.55f));
        if (bl10) {
            this.folderNameField.draw(canvas.b, this.host, (f6 + 36.0f) * this.s, (f7 + 7.0f) * this.s, 130.0f * this.s, 22.0f * this.s, this.s);
        } else {
            canvas.textMid(f6 + 36.0f, f7, 36.0f, 12.5f, this.text, this.fit(canvas, panelDef.title(), 12.5f, 114.0f, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        }
        float f15 = f6 + 206.0f - 28.0f;
        boolean bl11 = canvas.hov(f15, f7 + 8.0f, 20.0f, 20.0f);
        canvas.chevron(!bl3, f15 + 3.0f, f7 + 11.0f, 14.0f, bl11 ? this.text : this.muted);
        canvas.hit(f15 - 2.0f, f7 + 7.0f, 24.0f, 22.0f, () -> this.toggleCollapse(string));
        if (panelDef.folderId() != null) {
            String string2 = panelDef.folderId();
            canvas.rhit(f6, f7, 172.0f, 36.0f, () -> this.openFolderMenu(string2));
        }
        if (n3 > 0) {
            float f16 = this.reducedMotion() ? 0.5f : 0.5f + 0.5f * (float)Math.sin((double)System.nanoTime() / 1.8E9 + (double)n2);
            canvas.gradient(f6 + 14.0f, f7 + 36.0f - 5.0f, 178.0f, 1.5f, 0.75f, DropdownGpuClickGuiScreen.argb((int)(32.0f + 32.0f * f16), this.accent), DropdownGpuClickGuiScreen.argb(160.0f, this.accent), DropdownGpuClickGuiScreen.argb(160.0f, this.accent), DropdownGpuClickGuiScreen.argb((int)(32.0f + 32.0f * f16), this.accent));
            canvas.shadow(f6 + 20.0f, f7 + 36.0f - 4.0f, 166.0f, 1.5f, 0.75f, 10.0f, 0.0f, DropdownGpuClickGuiScreen.argb(48.0f, this.accent));
        }
        if (bl9) {
            canvas.gradient(f6 + 2.0f, f7 + 2.0f, 202.0f, 32.0f, 12.0f, DropdownGpuClickGuiScreen.argb(20.0f, this.accent), DropdownGpuClickGuiScreen.argb(4.0f, this.accent), DropdownGpuClickGuiScreen.argb(2.0f, this.accent), DropdownGpuClickGuiScreen.argb(10.0f, this.accent));
        }
        if (!bl10) {
            this.grips.add(new Grip(canvas.px(f6), canvas.px2(f7), 172.0f * this.s, 36.0f * this.s, string));
        }
        if (f12 <= 0.5f) {
            canvas.alpha = 1.0f;
            return;
        }
        float f17 = f7 + 36.0f;
        float f18 = SCROLL.getOrDefault(string, Float.valueOf(0.0f)).floatValue();
        float f19 = Math.max(0.0f, f5 - f10);
        f18 = DropdownGpuClickGuiScreen.clamp(f18, 0.0f, f19);
        SCROLL.put(string, Float.valueOf(f18));
        float f20 = this.ease(DropdownGpuClickGuiScreen.hj("\u5f3f\u7d19\uea07\ud171\u44b5", 2025421452, 0x229D082C ^ CryptoVault.mask()) + string, f18, 16.0f);
        f20 = DropdownGpuClickGuiScreen.clamp(f20, 0.0f, f19);
        canvas.beginClip(f6 + 2.0f, f17, 202.0f, f12);
        float f21 = f17 + 4.0f - f20;
        float f22 = 4.0f;
        if (bl5) {
            this.drawBedwarsNotice(canvas, f6 + 11.0f, f21, f8);
            f21 += 78.0f;
            f22 += 78.0f;
        }
        if (bl6) {
            canvas.text(f6 + 11.0f + 4.0f, f21 + 6.0f, 9.0f, this.muted, DropdownGpuClickGuiScreen.hj("\u26c3\u76a7\u9b0b\u5fb9\ub779\uf52f\u9d79\ube60\u33a4\ue22a\u47ba\u45f8\u3516\u99a1\u0755\u46c3\u2891\u4e8d\ufa36\u8645\ue589\u7beb\u7303\ub020\u176f\u2ab9\ubad6\u5d01\uc2e2\uca1d\uf3a9\uc765\u9373\u1a09\ud48f\u00aa\u0520\u8788\u1a06\u1ff5\udc37\uff51\u6cfd\u626f\u67f7\u399e", -1895140961, 0x229D082C ^ CryptoVault.mask()));
            f21 += 26.0f;
            f22 += 26.0f;
        }
        if (bl2) {
            this.drawAiBody(canvas, f6 + 11.0f, f21, f8);
        } else if (bl4) {
            for (GpuClickGuiScreen.QuickPin quickPin : list2) {
                f3 = this.quickRowHeight(canvas, quickPin, f8);
                if (f21 + f3 + 4.0f >= f17 - 2.0f && f21 <= f17 + f12 + 2.0f) {
                    canvas.rect(f6 + 11.0f, f21 + 4.0f, f8, f3, 8.0f, DropdownGpuClickGuiScreen.argb(22.0f, 0));
                    Module module = this.rowOwner;
                    String string3 = this.rowPrefix;
                    this.rowOwner = quickPin.module();
                    this.rowPrefix = quickPin.module().getName() + DropdownGpuClickGuiScreen.hj("\u0f4b\uf58a\uaacf\u681d", 560544648, 0x229D082C ^ CryptoVault.mask());
                    this.drawRow(canvas, quickPin.setting(), f6 + 11.0f + 8.0f, f21 + 4.0f, f8 - 16.0f, f3);
                    this.rowOwner = module;
                    this.rowPrefix = string3;
                }
                f21 += f3 + 4.0f;
            }
        } else {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                float f23;
                GpuClickGuiScreen.CatalogEntry catalogEntry = list.get(i2);
                Module module = catalogEntry.module();
                CatalogSections.Group group = (CatalogSections.Group)hashMap.get(i2);
                if (group != null) {
                    if (f21 + 20.0f >= f17 - 2.0f && f21 <= f17 + f12 + 2.0f) {
                        String string4 = group.sub().getName();
                        canvas.textMid(f6 + 11.0f + 4.0f, f21 + 2.0f, 16.0f, 9.0f, this.muted, string4, UiDrawCommand.TextWeight.SEMIBOLD);
                        f23 = f6 + 11.0f + 4.0f + canvas.tw(string4, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 6.0f;
                        String string5 = String.valueOf(group.size());
                        canvas.textMid(f23, f21 + 2.0f, 16.0f, 8.5f, this.muted, string5, UiDrawCommand.TextWeight.REGULAR);
                        float f24 = f6 + 11.0f + f8 - (f23 += canvas.tw(string5, 8.5f, UiDrawCommand.TextWeight.REGULAR) + 8.0f);
                        if (f24 > 8.0f) {
                            canvas.rect(f23, f21 + 10.0f, f24, 1.0f, 0.0f, DropdownGpuClickGuiScreen.argb(24.0f, 0xFFFFFF));
                        }
                    }
                    f21 += 20.0f;
                    f22 += 20.0f;
                }
                float f25 = object.getOrDefault(module.getName(), Float.valueOf(0.0f)).floatValue();
                f23 = this.moduleBlockHeight(canvas, module, f25);
                int n4 = this.navModules.size();
                this.navModules.add(module);
                if (this.navScrollPending && n4 == this.navIndex) {
                    this.navScrollPending = false;
                    if (f22 < f18) {
                        SCROLL.put(string, Float.valueOf(f22));
                    } else if (f22 + 27.0f > f18 + f10 - 8.0f) {
                        SCROLL.put(string, Float.valueOf(f22 + 27.0f - f10 + 8.0f));
                    }
                }
                if (f21 + f23 >= f17 - 2.0f && f21 <= f17 + f12 + 2.0f) {
                    this.drawModuleBlock(canvas, panelDef, i2, catalogEntry, f6 + 11.0f, f21, f8, f25, n4);
                    this.rowRects.add(new RowRect(string, module, i2, (f6 + 11.0f) * this.s, f21 * this.s, f8 * this.s, 27.0f * this.s));
                    if (bl8 && !this.tourRects.containsKey((Object)OnboardingTour.Target.MODULE_ROW)) {
                        this.tourMark(OnboardingTour.Target.MODULE_ROW, f6 + 11.0f, f21, f8, 27.0f);
                        this.tourMark(OnboardingTour.Target.MODULE_CHEVRON, f6 + 206.0f - 11.0f - 24.0f, f21 + 2.5f, 24.0f, 22.0f);
                    }
                }
                f21 += f23;
                f22 += f23;
            }
        }
        canvas.endClip();
        if (f19 > 1.0f) {
            float f26 = f12 - 8.0f;
            float f27 = Math.max(24.0f, f26 * (f10 / f5));
            f3 = f17 + 4.0f + (f26 - f27) * (f20 / f19);
            canvas.rect(f6 + 206.0f - 5.0f, f17 + 4.0f, 2.5f, f26, 1.25f, DropdownGpuClickGuiScreen.argb(18.0f, 0xFFFFFF));
            canvas.rect(f6 + 206.0f - 5.0f, f3, 2.5f, f27, 1.25f, DropdownGpuClickGuiScreen.argb(112.0f, this.accent));
        }
        canvas.alpha = 1.0f;
    }

    private void drawBedwarsNotice(Canvas canvas, float f2, float f3, float f4) {
        float f5 = 70.0f;
        canvas.rect(f2, f3, f4, f5, 10.0f, DropdownGpuClickGuiScreen.argb(24.0f, this.accent));
        canvas.border(f2, f3, f4, f5, 10.0f, this.hair(), DropdownGpuClickGuiScreen.argb(112.0f, this.accent));
        canvas.text(f2 + 10.0f, f3 + 8.0f, 10.5f, this.text, DropdownGpuClickGuiScreen.hj("\u772c\u70f2\uadbd\u1f72\ud717\uabe0\ude77\u553e\u3196\udd69\ud1ba\u738d\u4b00\uf56f\uce11\u7a8d\u05b8", 518160444, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        List<String> list = this.wrap(canvas, DropdownGpuClickGuiScreen.hj("\u8e9f\u5c17\ub653\ue9da\uf45d\u3268\uebd7\u33ad\u59a8\ueaae\u195b\ufcd0\u58b2\uf321\uac8c\ub17a\u1ca3\u4e12\u0d2b\uc136\ua177\u7037\ufac1\u2613\u5411\ub6ff\ufe52\u56b8\u6fe9\u5d36\u582b\u59c2\ub2b3\u15d5\ue465\uf720\u5a8f\uf7ae\u39b9\ua5d9\u38c4\u70a1\u9ddd\u4dbd\uc7a6\u876b\u840d", -921894949, 0x229D082C ^ CryptoVault.mask()), 9.0f, f4 - 20.0f, 2);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            canvas.text(f2 + 10.0f, f3 + 24.0f + (float)i2 * 11.0f, 9.0f, this.muted, list.get(i2));
        }
        float f6 = f3 + 46.0f;
        float f7 = 20.0f;
        String string = DropdownGpuClickGuiScreen.hj("\uc2fa\u46ed\u8706\ucbe7\u689f\u31c8\u5984\ue01e\u56fa\uaa43\uce45\u681a", 2052776703, 0x229D082C ^ CryptoVault.mask());
        String string2 = DropdownGpuClickGuiScreen.hj("\ue978\ucc72\u444d\u0e65\u49d1\uf569\u16fc\u64ae\u03f4\u5f13\ue0ee\ud113\u7b8e\u94fd\u2e44\ub4a6\ue01e\uf748", -457774498, 0x229D082C ^ CryptoVault.mask());
        float f8 = canvas.tw(string, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f;
        float f9 = canvas.tw(string2, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f;
        float f10 = f2 + f4 - 8.0f - f8;
        float f11 = f10 - 6.0f - f9;
        boolean bl = canvas.hov(f10, f6, f8, f7);
        boolean bl2 = canvas.hov(f11, f6, f9, f7);
        canvas.rect(f10, f6, f8, f7, 10.0f, DropdownGpuClickGuiScreen.argb(bl ? 112.0f : 80.0f, this.accent));
        canvas.textMid(f10 + 8.0f, f6, f7, 9.0f, this.text, string, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.hit(f10, f6, f8, f7, () -> {
            bedwarsNoticeContinued = true;
        });
        canvas.rect(f11, f6, f9, f7, 10.0f, DropdownGpuClickGuiScreen.argb(bl2 ? 28.0f : 14.0f, 0xFFFFFF));
        canvas.textMid(f11 + 8.0f, f6, f7, 9.0f, bl2 ? this.text : this.muted, string2, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.hit(f11, f6, f9, f7, () -> {
            if (HitboxConfigScreen752809.aH != null) {
                HitboxConfigScreen752809.aH.setEnabled(true);
            }
        });
    }

    private Module aiModule(String string) {
        for (Module module : this.modulesForCategory(Category.vR)) {
            if (!string.equals(module.getName())) continue;
            return module;
        }
        return null;
    }

    private float aiBodyHeight(Canvas canvas, float f2) {
        Module module = this.aiModule(DropdownGpuClickGuiScreen.hj("\uba1d\u3b35\u9695\u60a1\ua60f\ue2a3\u9289\u12dd\u64d8", 1522063902, 0x229D082C ^ CryptoVault.mask()));
        Module module2 = this.aiModule(DropdownGpuClickGuiScreen.hj("\ua5d4\u2ebc\uc2f6\u57ce\u1ff4\u7073\u8bdd\udb90", 1538690266, 0x229D082C ^ CryptoVault.mask()));
        float f3 = 0.0f;
        if (module != null) {
            f3 += 90.0f + this.sectionsHeight(canvas, this.viewFor(module).sections(), f2 - 16.0f);
        }
        if (module2 != null) {
            f3 += 52.0f + this.sectionsHeight(canvas, this.viewFor(module2).sections(), f2 - 16.0f);
        }
        return f3;
    }

    private void drawAiBody(Canvas canvas, float f2, float f3, float f4) {
        Module module = this.aiModule(DropdownGpuClickGuiScreen.hj("\u3b7e\ubfe1\ub90f\u01bb\uef8c\ud892\u0f40", -1113116679, 0x229D082C ^ CryptoVault.mask()));
        Module module2 = this.aiModule(DropdownGpuClickGuiScreen.hj("\u4e44\u3b1c\u6255\u79df\ue767\u5b40\u8392", 1293497392, 0x229D082C ^ CryptoVault.mask()));
        float f5 = f3;
        Module module3 = this.rowOwner;
        if (module != null) {
            boolean bl = module.isEnabled();
            float f6 = 40.0f;
            boolean bl2 = canvas.hov(f2, f5, f4, f6);
            float f7 = this.ease(DropdownGpuClickGuiScreen.hj("\uf508\u1672\u2f12\u20ce\u63ed\u01ca\u74f2\u919e\ub07a\u7609", -938697932, 0x229D082C ^ CryptoVault.mask()), bl ? 1.0f : 0.0f, 12.0f);
            canvas.rect(f2, f5, f4, f6, 10.0f, DropdownGpuClickGuiScreen.argb(bl2 ? 28.0f : 16.0f, 0xFFFFFF));
            if (f7 > 0.01f) {
                canvas.gradient(f2, f5, f4, f6, 10.0f, DropdownGpuClickGuiScreen.argb((int)(52.0f * f7), this.accent), DropdownGpuClickGuiScreen.argb((int)(12.0f * f7), this.accent), DropdownGpuClickGuiScreen.argb((int)(6.0f * f7), this.accent), DropdownGpuClickGuiScreen.argb((int)(24.0f * f7), this.accent));
                canvas.border(f2, f5, f4, f6, 10.0f, Math.max(1.0f, this.s), DropdownGpuClickGuiScreen.argb((int)(80.0f * f7), this.accent));
            }
            canvas.text(f2 + 12.0f, f5 + 8.0f, 12.0f, bl ? DropdownGpuClickGuiScreen.mix(this.text, this.accent, 0.25f) : this.text, DropdownGpuClickGuiScreen.hj("\uc4f1\ubbf5\ubcfc\u64f7\u9cdf\u00f7\ue1c9", 1186217038, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            float f8 = f2 + 12.0f + canvas.tw(DropdownGpuClickGuiScreen.hj("\ua39a\u50f4\u983f\u9ac5\u165c\ue1c1\u0bda\u8aa8\u3e2b", 48233200, 0x229D082C ^ CryptoVault.mask()), 12.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 6.0f;
            float f9 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u2af9\u9244\ufcae\u450a\u5739\u3542\udfa1\u2687\u2ba9\u6e92", -104965295, 0x229D082C ^ CryptoVault.mask()), 7.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 10.0f;
            canvas.rect(f8, f5 + 8.0f, f9, 13.0f, 6.5f, 872394784);
            canvas.border(f8, f5 + 8.0f, f9, 13.0f, 6.5f, Math.max(1.0f, this.s * 0.8f), -1711296480);
            canvas.textMid(f8 + 5.0f, f5 + 8.0f, 13.0f, 7.5f, -14741, DropdownGpuClickGuiScreen.hj("\uc4ef\ub4c0\u75d3\ue280\uc14f\udf75\u62ee\u3cc8\u5efe\ua289", -950595087, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.text(f2 + 12.0f, f5 + 24.0f, 8.5f, this.muted, this.fit(canvas, bl ? "On" : DropdownGpuClickGuiScreen.hj("\uc9dc\uc024\ud54f\uaa5d\u68fb\u07dc\ua5bb\u7430\u66c5\u4d84\u74a7\u37fb\ud810\ucaa2\ue18d\u0e98\u60a8\ud159\ud4e1\ua906\ucf29\u885f\u5d5a", 1069782996, 0x229D082C ^ CryptoVault.mask()), 8.5f, f4 - 70.0f));
            float f10 = 30.0f;
            float f11 = 16.0f;
            float f12 = f2 + f4 - f10 - 10.0f;
            float f13 = f5 + (f6 - f11) * 0.5f;
            canvas.rect(f12, f13, f10, f11, f11 * 0.5f, bl ? DropdownGpuClickGuiScreen.argb(208.0f, this.accent) : DropdownGpuClickGuiScreen.argb(48.0f, 0xFFFFFF));
            canvas.circle(f12 + f11 * 0.5f + (f10 - f11) * f7, f13 + f11 * 0.5f, f11 * 0.5f - 2.5f, -1);
            if (bl2) {
                this.tipTitle = DropdownGpuClickGuiScreen.hj("\ud1a3\u210c\u50ff\u90a3\u8d0b\ud099\u8323\u1ac2\u2831\ue05d", -963194504, 0x229D082C ^ CryptoVault.mask());
                String string = this.tipDesc = module.getDescription() == null ? "" : module.getDescription();
            }
            if (!module.isLocked()) {
                canvas.hit(f2, f5, f4, f6, module::toggle);
            }
            this.drawBindRow(canvas, module, f2 + 8.0f, f5 += 46.0f, f4 - 16.0f);
            f5 += 44.0f;
            this.rowOwner = module;
            f5 = this.drawSections(canvas, this.viewFor(module).sections(), f2 + 8.0f, f5, f4 - 16.0f);
        }
        if (module2 != null) {
            canvas.rect(f2 + 8.0f, f5 + 6.0f, f4 - 16.0f, 1.0f, 0.0f, DropdownGpuClickGuiScreen.argb(24.0f, 0xFFFFFF));
            canvas.text(f2 + 8.0f, f5 + 14.0f, 11.5f, this.text, DropdownGpuClickGuiScreen.hj("\ub135\uf23c\uc603\u4605\u7eae\ub1eb\u6f82\u48b3", 950298145, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.text(f2 + 8.0f, f5 + 30.0f, 8.5f, this.muted, this.fit(canvas, DropdownGpuClickGuiScreen.hj("\uc3fe\ue954\u29f2\uc528\u57d1\uce0c\u7ef8\u7561\u1e17\u4c39\u9cde\u3516\u1370\u4d4c\u5b1c\u138c\ub4c8\uad94\u75aa\u84cc\uf4ad\u62ac\ubff1\ueab5\udab0\u87af\uf561\u03ad\u1f89\u9683\u792f\u8080\u7a1a\uc883\uffeb\u8f47\u6736\u56f5\u2597\u95d7\u425b\u940c\u8278\uaae9\u6667\u55ca", -1062880363, 0x229D082C ^ CryptoVault.mask()), 8.5f, f4 - 16.0f));
            this.rowOwner = module2;
            this.drawSections(canvas, this.viewFor(module2).sections(), f2 + 8.0f, f5 += 52.0f, f4 - 16.0f);
        }
        this.rowOwner = module3;
    }

    private float moduleBlockHeight(Canvas canvas, Module module, float f2) {
        float f3 = 27.0f;
        if (f2 > 0.001f) {
            f3 += this.settingsHeight(canvas, module) * f2;
        }
        return f3;
    }

    private void beginModuleCapture(Module module) {
        this.cancelCapture();
        this.bindingModule = module;
        module.setBinding(true);
    }

    private void drawModuleBlock(Canvas canvas, PanelDef panelDef, int n2, GpuClickGuiScreen.CatalogEntry catalogEntry, float f2, float f3, float f4, float f5, int n3) {
        float f6;
        float string6;
        int n4;
        boolean bl;
        String string;
        boolean bl2;
        Module module = catalogEntry.module();
        boolean bl4 = module.isEnabled();
        boolean bl5 = DropdownGpuClickGuiScreen.floatingModuleSettings() ? DropdownGpuClickGuiScreen.hj("\u6cde\u091e\u14c3\u6d2f\ud296\u96eb\u18b9\u34b4\u55bf", 1668657625, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.overlayModule == module : EXPANDED.contains(module.getName());
        boolean bl6 = canvas.hov(f2, f3, f4, 27.0f);
        float f8 = this.ease(this.modKey(1, module), bl4 ? 1.0f : 0.0f, 12.0f);
        float f9 = 8.0f;
        float f10 = this.ease(this.modKey(2, module), bl6 ? 1.0f : 0.0f, 18.0f);
        int n5 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bf, 0);
        float f11 = Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.bj, 1.0f));
        if (f10 > 0.01f) {
            canvas.rect(f2, f3 + 1.0f, f4, 25.0f, f9, DropdownGpuClickGuiScreen.argb((int)(18.0f * f10 * f11), n5 != 0 ? n5 : 0xFFFFFF));
        }
        if (f8 > 0.01f) {
            int bl3 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bh, this.accent);
            float bl7 = this.reducedMotion() ? 0.5f : 0.5f + 0.5f * (float)Math.sin((double)System.nanoTime() / 1.6E9 + (double)f3 * 0.02);
            canvas.gradient(f2, f3 + 1.0f, f4, 25.0f, f9, DropdownGpuClickGuiScreen.argb((int)(48.0f * (f8 *= Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.bl, 1.0f))) + 12.0f * f8 * bl7), bl3), DropdownGpuClickGuiScreen.argb((int)(10.0f * f8), bl3), DropdownGpuClickGuiScreen.argb((int)(4.0f * f8), bl3), DropdownGpuClickGuiScreen.argb((int)(20.0f * f8), bl3));
            canvas.border(f2, f3 + 1.0f, f4, 25.0f, f9, Math.max(1.0f, this.s), DropdownGpuClickGuiScreen.argb((int)(58.0f * f8), bl3));
            canvas.shadow(f2 + 1.0f, f3 + 3.0f, 3.0f, 21.0f, 1.5f, 7.0f, 0.0f, DropdownGpuClickGuiScreen.argb((int)(144.0f * f8), bl3));
            canvas.rect(f2, f3 + 4.0f, 3.0f, 19.0f, 1.5f, DropdownGpuClickGuiScreen.argb((int)(224.0f * f8), bl3));
        }
        if (this.navVisible && n3 == this.navIndex) {
            canvas.border(f2 - 1.5f, f3, f4 + 3.0f, 27.0f, f9 + 1.5f, Math.max(1.4f, this.s * 1.3f), DropdownGpuClickGuiScreen.argb(208.0f, this.accent));
        }
        boolean bl3 = module.isOneShot();
        boolean bl7 = module.isFavorite();
        float f7 = f2 + f4 - 22.0f;
        float f12 = f7 - 16.0f;
        boolean bl8 = bl2 = bl3 && this.bindingModule == module;
        String string2 = bl3 ? (bl2 ? DropdownGpuClickGuiScreen.hj("\u4fba\u6713\u59e8\u2f9c\uaf96\u58c0\u0621\ub9d3\uc7fc\u0466\ubb3b\u427e\u4fab", 489290158, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.keyLabel(module.getKeyCode())) : (string = DropdownGpuClickGuiScreen.isBound(module) ? DropdownGpuClickGuiScreen.keyLabel(module.getKeyCode()) : null);
        float f13 = string == null ? 0.0f : (bl3 ? Math.min(72.0f, Math.max(38.0f, canvas.tw(string, 8.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 14.0f)) : Math.min(46.0f, Math.max(20.0f, canvas.tw(string, 8.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 10.0f)));
        float f14 = f12 - 3.0f - f13;
        float f15 = (string != null ? f14 : f12) - 4.0f - (f2 + 12.0f);
        UiDrawCommand.TextWeight textWeight = bl4 ? UiDrawCommand.TextWeight.SEMIBOLD : UiDrawCommand.TextWeight.REGULAR;
        String string3 = this.fit(canvas, module.getName(), 11.5f, f15, textWeight);
        String string4 = this.searchField.value().trim().toLowerCase(Locale.ROOT);
        if (!string4.isEmpty()) {
            int f19 = module.getName().toLowerCase(Locale.ROOT).indexOf(string4);
            bl = string3.endsWith("\u2026");
            if (!(f19 < 0 || bl && f19 + string4.length() >= string3.length())) {
                n4 = Math.min(f19 + string4.length(), bl ? string3.length() - 1 : string3.length());
                float n7 = f2 + 12.0f + canvas.tw(module.getName().substring(0, f19), 11.5f, textWeight);
                string6 = canvas.tw(module.getName().substring(f19, n4), 11.5f, textWeight);
                canvas.rect(n7 - 1.0f, f3 + 6.0f, string6 + 2.0f, 15.0f, 3.0f, DropdownGpuClickGuiScreen.argb(72.0f, this.accent));
            }
        }
        canvas.textMid(f2 + 12.0f, f3, 27.0f, 11.5f, bl4 ? DropdownGpuClickGuiScreen.mix(this.text, this.accent, 0.25f) : DropdownGpuClickGuiScreen.mix(this.text, this.muted, 0.35f), string3, textWeight);
        if (module.isExtendedFlagged()) {
            float f21 = f2 + 12.0f + canvas.tw(string3, 11.5f, textWeight) + 8.0f;
            float f16 = string != null ? f14 : f12;
            if (f21 < f16 - 2.0f) {
                canvas.circle(f21, f3 + 13.5f, 2.2f, -28779);
            }
        }
        float f17 = this.ease(this.modKey(3, module), bl7 || bl6 ? 1.0f : 0.0f, 16.0f);
        bl = canvas.hov(f12, f3 + 5.5f, 16.0f, 16.0f);
        if (f17 > 0.01f) {
            n4 = bl7 ? this.accent : (bl ? this.text : this.muted);
            int string5 = Math.round(f17 * 255.0f * (bl7 || bl ? 1.0f : 0.7f));
            DropdownGpuClickGuiScreen.drawStar(canvas.b, (f12 + 8.0f) * this.s, (f3 + 13.5f) * this.s, 5.0f * this.s, canvas.col(string5 << 24 | n4 & 0xFFFFFF));
        }
        canvas.chevron(bl5, f7 + 2.0f, f3 + 7.5f, 12.0f, (n4 = (int)(canvas.hov(f7, f3 + 4.5f, 18.0f, 18.0f) ? 1 : 0)) != 0 || bl5 ? this.text : this.muted);
        if (bl6) {
            String runnable = module.getDescription() == null ? "" : module.getDescription();
            String bl10 = catalogEntry.note();
            if (bl10 != null) {
                String string5 = runnable = runnable.isBlank() ? bl10 : bl10 + ". " + runnable;
            }
            if (!runnable.isBlank()) {
                this.tipTitle = module.getName();
                this.tipDesc = runnable;
            }
        }
        Runnable runnable = () -> {
            if (bl3 || module.isLocked()) {
                this.toggleExpand(module);
            } else {
                module.toggle();
            }
        };
        canvas.hit(f2, f3, f4, 27.0f, new RowPress(module, panelDef.key(), runnable));
        canvas.rhit(f2, f3, f4, 27.0f, () -> {
            if (this.secondaryIsMiddle) {
                this.openModuleMenu(module);
            } else {
                this.toggleExpand(module);
            }
        });
        if (string != null) {
            boolean f22;
            boolean bl9 = canvas.hov(f14, f3 + 4.5f, f13, 18.0f);
            boolean bl10 = f22 = !bl3 || DropdownGpuClickGuiScreen.isBound(module) || bl2;
            canvas.rect(f14, f3 + 4.5f, f13, 18.0f, 9.0f, f22 ? DropdownGpuClickGuiScreen.argb(bl9 ? 58.0f : 34.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl9 ? 34.0f : 18.0f, 0xFFFFFF));
            if (bl2) {
                canvas.border(f14, f3 + 4.5f, f13, 18.0f, 9.0f, this.hair(), DropdownGpuClickGuiScreen.argb(192.0f, this.accent));
            }
            f6 = Math.min(f13 - 8.0f, canvas.tw(this.fit(canvas, string, 8.0f, f13 - 8.0f, UiDrawCommand.TextWeight.SEMIBOLD), 8.0f, UiDrawCommand.TextWeight.SEMIBOLD));
            canvas.textMid(f14 + (f13 - f6) * 0.5f, f3, 27.0f, 8.0f, bl2 ? this.accent : (bl9 ? this.text : (f22 ? DropdownGpuClickGuiScreen.mix(this.text, this.muted, 0.2f) : this.muted)), this.fit(canvas, string, 8.0f, f13 - 8.0f, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            if (bl9) {
                this.tipTitle = (bl3 ? DropdownGpuClickGuiScreen.hj("\u251b\u0180\u7e37\u1919\uadc0\uf296\u6986\u974e\u284d\u3911\u4729\ue2b8\u3455\u08da", 189599450, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u2cc5\ud24d\uae1e\uc892\ud07d\ue1c9\ub52f\u8755\u40d0\u2852\u3ea7\u7b44", -589102968, 0x229D082C ^ CryptoVault.mask())) + module.getKeyDisplayName();
                this.tipDesc = bl3 ? DropdownGpuClickGuiScreen.hj("\u29a5\u2b08\u3c71\ub32f\ud49e\u26bd\u395a\uf2a8\u5cea\u4429\u4ba7\u90d0\uc8cf\u0b1e\u302e\u6c32\u23d6\u4fae\u302e\ua38a\u6f06\u4d4c\u2147\u5ba9\ub195\ua440\u5b52\uf9fa\ud679\u5c49\u4d16\uf10e\u4408\u2b73\udbfa\ueff5\u8828\uc59b\u5d5f\u5ae1\u9b6b\u6f2d\u4dac\u6f79\ue611\u5207\uf21c\ue77f\udb91\u79ca\ucb79\uf2d0\ud8ea\ua39b\u5332\u1cde\u24d6\ued02\ucb12\u6b25\uccfc\u8699\u6780", -439205565, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u64d1\uf789\ude67\u7bee\u08da\ue4d1\u83b7\ue8fb\uac81\u48ee\u38ef\ue404\u14f5\u8dad\ucff9\u5d0d\u1f58\uae9e\u2477\u3c6e\u5286\u6847\uf1d5\u1f0e\u8584\udd16\ufa77\ub4a0\uc131\ufdca\ue2de\u992e\u23db\u5223\u97e6\uce75\u21a6\u01d2\u93e1\ueba2", 518845218, 0x229D082C ^ CryptoVault.mask());
            }
            canvas.hit(f14, f3, f13, 27.0f, () -> this.beginModuleCapture(module));
            if (bl3) {
                canvas.rhit(f14, f3, f13, 27.0f, () -> {
                    if (this.bindingModule == module) {
                        this.cancelCapture();
                    }
                    module.setKeyCode(-1);
                });
            }
        }
        if (f17 > 0.01f && (bl7 || bl6)) {
            if (bl) {
                this.tipTitle = bl7 ? DropdownGpuClickGuiScreen.hj("\ud010\u32b5\ub4e3\u1ea8\u7b92\u11b9\ub6bf\u67c3\u287a\u544e\u9005", 1899037590, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u88dd\ud97b\u72c6\ucd2b\u95e6\u2be9\ubad1\u54e7\u2571\u0583\u3ab1", -1179074328, 0x229D082C ^ CryptoVault.mask());
                this.tipDesc = bl7 ? DropdownGpuClickGuiScreen.hj("\u35f1\u9032\u8562\u48cd\u121a\u5acc\uf326\u542a\uc74e\u20e4\u09ae\u53d9\u6ca1\ub9f5\udad4\u33f3\ud6ab\ua1de\u877f\ud2a3\u8118\u98e8\u1848\u9ea7\u2491\ud51b\ub4bb\u0901\ue722\u4c92\u309a\u3e3a\u15fa\u3040\u4677\u8365", 1239535523, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\uaf4e\ud775\u17d0\u97dd\uce2e\u3fa0\u8578\ud590\ue0e4\u7f52\uc5b6\u35de\ucd1e\ud8ab\u1bef\u1bc2\ua190\u689e\ub35c\u97b2\u668f\u9d4f\u44ee\ua991\u2ffa\u1fbb\u48e2\u281d\u273f\ub6a7\u760b\ud273\ue27d\u2dba\u19ec\ua539\ud834\u41a4\u7f63\udeec\u0d14\u905c\u7884\ued07\u40ab\uf877\u98bb", 2051345808, 0x229D082C ^ CryptoVault.mask());
            }
            canvas.hit(f12, f3, 16.0f, 27.0f, () -> module.setFavorite(!module.isFavorite()));
        }
        if (module == this.dragModule) {
            canvas.rect(f2, f3 + 1.0f, f4, 25.0f, f9, DropdownGpuClickGuiScreen.argb(144.0f, 724758));
        }
        if (f5 <= 0.001f) {
            return;
        }
        string6 = this.settingsHeight(canvas, module);
        float f18 = string6 * f5;
        f6 = f2 + 10.0f;
        float f19 = f4 - 10.0f;
        float f20 = f3 + 27.0f;
        canvas.beginClip(f2, f20, f4, f18);
        canvas.rect(f6 - 2.0f, f20, f19 + 2.0f, string6, 8.0f, DropdownGpuClickGuiScreen.argb(22.0f, 0));
        canvas.rect(f6 - 2.0f, f20, 2.0f, string6, 1.0f, DropdownGpuClickGuiScreen.argb(64.0f, this.accent));
        ModuleView moduleView = this.viewFor(module);
        float f21 = f20 + 6.0f;
        Module module2 = this.rowOwner;
        this.rowOwner = module;
        if (this.toolsHeight(moduleView) > 0.0f) {
            this.drawToolsRow(canvas, module, moduleView, f6 + 8.0f, f21, f19 - 16.0f);
            f21 += this.toolsHeight(moduleView);
        }
        if (moduleView.sections().isEmpty()) {
            canvas.textMid(f6 + 8.0f, f21, 22.0f, 10.0f, this.muted, moduleView.total() == 0 ? DropdownGpuClickGuiScreen.hj("\u776a\u9e96\u00c0\u29c3\ue598\u30ce\ua590\u6493\u634d\ua04b\u09cd\u53ae\u7356\u8d02\u66b4\ubef9\u5bf4\u0f21\ue721\u7569\u595f\uf792", -164206269, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\uee7f\uf0ad\u86fe\u5598\uf9b8\u0254\u9112\u3cdf\u28b4\u3916\uca5c\uec49\u4b2c\u0607\ubf9c\u8cc7\u43bb\ua620\udaee\ubd60", -806594032, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
            f21 += 22.0f;
        }
        f21 = this.drawSections(canvas, moduleView.sections(), f6 + 8.0f, f21, f19 - 16.0f);
        if (module != this.clickGuiModule()) {
            this.drawBindRow(canvas, module, f6 + 8.0f, f21, f19 - 16.0f);
        }
        this.rowOwner = module2;
        canvas.endClip();
    }

    private void drawToolsRow(Canvas canvas, Module module, ModuleView moduleView, float f2, float f3, float f4) {
        float f5 = 0.0f;
        if (moduleView.anyModified()) {
            boolean bl = module.getName().equals(this.resetArmedModule) && this.nanoTime() - this.resetArmedAt < 3000000000L;
            String string2 = bl ? DropdownGpuClickGuiScreen.hj("\u82aa\u5d0f\u544b\u9366\u5e37\u8d21\u00c3\u7c70", 838795168, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\ua1ce\u1259\ue4f3\u2e67\ubc75\ubc78\ub455\ua6f5\u0115\u3eeb\uf343\u7e1d", 285022898, 0x229D082C ^ CryptoVault.mask());
            f5 = canvas.tw(string2, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f;
            float f6 = f2 + f4 - f5;
            float f7 = f3 + 3.0f;
            float f8 = 22.0f;
            boolean bl2 = canvas.hov(f6, f7, f5, f8);
            canvas.rect(f6, f7, f5, f8, 11.0f, bl ? DropdownGpuClickGuiScreen.argb(51.0f, 0xFF5555) : DropdownGpuClickGuiScreen.argb(bl2 ? 28.0f : 14.0f, 0xFFFFFF));
            canvas.border(f6, f7, f5, f8, 11.0f, this.hair(), bl ? DropdownGpuClickGuiScreen.argb(176.0f, 0xFF6B6B) : DropdownGpuClickGuiScreen.argb(bl2 ? 48.0f : 28.0f, 0xFFFFFF));
            canvas.textMid(f6 + 9.0f, f7, f8, 9.0f, bl ? -30070 : (bl2 ? this.text : this.muted), string2, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.hit(f6, f7, f5, f8, () -> {
                if (module.getName().equals(this.resetArmedModule) && this.nanoTime() - this.resetArmedAt < 3000000000L) {
                    for (Setting setting : module.getSettings()) {
                        if (setting == null || setting instanceof HitboxKeyBinding655023) continue;
                        setting.resetToDefault();
                    }
                    this.resetArmedModule = null;
                } else {
                    this.resetArmedModule = module.getName();
                    this.resetArmedAt = this.nanoTime();
                }
            });
            if (bl2) {
                this.tipTitle = DropdownGpuClickGuiScreen.hj("\u7688\u4e6c\ubf86\uee66\u0822\u06e4\ua71f\u7a5e\u4790\u90bc\u3ecf\u3032\u1fc7\u7718\u9300\u3ea5\u442e\ua626\ue1af\u1738\u23e4", -1116141456, 0x229D082C ^ CryptoVault.mask());
                this.tipDesc = DropdownGpuClickGuiScreen.hj("\u82d9\u0ce8\u0e95\u4949\ub14a\u6ea2\u7f7e\ub9f0\u89e6\u1cbf\u33c0\ue833\uaf12\u3d35\u27b0\ud59d\u74b3\u77d7\ud8b8\u2595\uf271\u9a52\u27fb\ub74d\u9792", -1692690294, 0x229D082C ^ CryptoVault.mask()) + module.getName() + DropdownGpuClickGuiScreen.hj("\u0ba6\u0f74\u228d\u02ee\u4c51\u8a38\u5743\ub3c1\udfbc\uf5d0\ua98f\u291e\uff02\u27b7\u9f1b\u6fca\u4b4d\uabb2\u1d16\u0eaa\uab7f\u8ec5\ue9ee\u39a1\u5397\u14bb\u22ac\ua045\u1c9b\u0663\ua388\u5f7d\uf6e7\u9dd9\u361d\u83aa\udcd4\u700e\u67fd\uf5c1\u8e9a\u8e38\ua90e\ubf48\uc190\u5f0e\u8bd6\u853c\u4d74\uf11d\u76a2\u08e2\u47ab\u7e77\udc86\ub06d\ucfc9\ufa2c\u52e4\u9244\u4d92\ufdd2\u9198\ud736\u4cdb\u2385\ub17e", -491531273, 0x229D082C ^ CryptoVault.mask());
            }
            f5 += 6.0f;
        }
        if (moduleView.total() > 5) {
            UiTextField uiTextField = this.settingFilters.computeIfAbsent(module.getName(), string -> new UiTextField(DropdownGpuClickGuiScreen.hj("\ud545\uba28\u0524\u0c50\u295f\u2b31\u6c17\u2965\u552a\u4f5c\u07b7\uab6c\uac0a\u0826\ud4f1\u4a22\u4167\u5694\ue715\uf176", -1547763779, 0x229D082C ^ CryptoVault.mask())).maxLength(40).fontSize(9.5f));
            this.hostClipTo(canvas);
            uiTextField.draw(canvas.b, this.host, f2 * this.s, (f3 + 3.0f) * this.s, (f4 - f5) * this.s, 22.0f * this.s, this.s);
            this.hostClip = false;
        }
    }

    private void hostClipTo(Canvas canvas) {
        float[] fArray = canvas.clip();
        if (fArray == null) {
            return;
        }
        this.hcx = fArray[0];
        this.hcy = fArray[1];
        this.hcw = fArray[2];
        this.hch = fArray[3];
        this.hostClip = true;
    }

    private void drawBindRow(Canvas canvas, Module module, float f2, float f3, float f4) {
        boolean bl = this.bindingModule == module;
        boolean bl2 = DropdownGpuClickGuiScreen.isBound(module);
        String string = module.isOneShot() || module.isKeyTriggered() ? DropdownGpuClickGuiScreen.hj("\u368f\u26dc\u7b3d\u84e6\uce6e\u5ad7\u6f7a\u1026\u476c\u9ca8\u971a\u28a7\u4ba6\u1f0d", -95364986, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u8603\ua9cf\ud94f\ua1e2\u1ae2\u70e7\u20ae\u4c4c\u072a\u567b\u7a5f", -705895757, 0x229D082C ^ CryptoVault.mask());
        String string2 = bl ? DropdownGpuClickGuiScreen.hj("\u4f1e\uef36\ua4c7\u0cef\uafe5\u0b68\u9828\u09ce\u42d4\u114e\uff01\u943c\u2faa", 1966104946, 0x229D082C ^ CryptoVault.mask()) : string + ": " + DropdownGpuClickGuiScreen.keyLabel(module.getKeyCode());
        float f5 = 26.0f;
        float f6 = f3 + 6.0f;
        float f7 = !bl && BindModeControl.available(module) ? canvas.tw(DropdownGpuClickGuiScreen.hj("\u4cee\u96a6\ufb91\u9bbd\u8514\uc656\u9f87\ue100", 1496688482, 0x229D082C ^ CryptoVault.mask()), 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f : 0.0f;
        float f8 = bl2 && !bl ? canvas.tw(DropdownGpuClickGuiScreen.hj("\ue1e9\ue9f2\u5c1c\u2701\u4a36\u8dce\u8a14\u484a", 1353092608, 0x229D082C ^ CryptoVault.mask()), 9.5f, UiDrawCommand.TextWeight.REGULAR) + 12.0f : 0.0f;
        float f9 = (f7 > 0.0f ? 6.0f : 0.0f) + (f8 > 0.0f ? 4.0f : 0.0f);
        float f10 = f4 - f7 - f8 - f9;
        this.keyCap(canvas, f2, f6, f10, f5, this.fit(canvas, string2, 9.5f, f10 - 12.0f, UiDrawCommand.TextWeight.SEMIBOLD), bl);
        if (canvas.hov(f2, f6, f10, f5) && !bl) {
            this.tipTitle = string;
            this.tipDesc = DropdownGpuClickGuiScreen.hj("\u6acf\u46bc\ue34c\u6083\ue3f9\u2977\uc98a\u257f\u2352\ub8f1\udd8b\ucf66\u937b\u59d5\ub9ee\uca85\u49c2\u9b56\ud5c9\u0cc9\ud947\u9882\u1be6\ub5e1\ue64c\u5fab\u0a8f\u841f\ub030\u05fb\ud528\uf076\u45a4\uae61\u43ce\u8c97\uc0b9\u04d5\u80eb\u5091\uf58a\u8ae1\udd75\ud1c5\ud1ab\u4d62\ue74d\u3318\ud4c3\u9caa\u02fe\u1449\uae28\u7fd6\u3844\u55c4\uc439\ufcd1\u2bf9\u4384\ud0cd\u426b\u4957\u3eeb\uf7c8\ude8e\u73a3\u3213\u7b51\uac51\u1623\u4d53\ue0b8\ua83e\u0cbb\uec9f", 2144800242, 0x229D082C ^ CryptoVault.mask());
        }
        canvas.hit(f2, f6, f10, f5, () -> this.beginModuleCapture(module));
        float f11 = f2 + f10 + (f7 > 0.0f ? 6.0f : 0.0f);
        if (f7 > 0.0f) {
            String string3 = BindModeControl.label(module);
            boolean bl3 = BindModeControl.hold(module);
            boolean bl4 = canvas.hov(f11, f6, f7, f5);
            canvas.rect(f11, f6, f7, f5, 8.0f, bl3 ? DropdownGpuClickGuiScreen.argb(48.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl4 ? 24.0f : 14.0f, 0xFFFFFF));
            canvas.border(f11, f6, f7, f5, 8.0f, this.hair(), bl3 ? DropdownGpuClickGuiScreen.argb(144.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl4 ? 44.0f : 28.0f, 0xFFFFFF));
            canvas.textMid(f11 + (f7 - canvas.tw(string3, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD)) * 0.5f, f6, f5, 9.0f, bl3 ? this.accent : this.text, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.hit(f11, f6, f7, f5, () -> BindModeControl.toggle(module));
            if (bl4) {
                this.tipTitle = DropdownGpuClickGuiScreen.hj("\u2ed3\u4e44\u9587\u1c9d\ua128\u82de\u0b71\u068a\uc969", -589490263, 0x229D082C ^ CryptoVault.mask());
                this.tipDesc = BindModeControl.tip(module);
            }
            f11 += f7 + 4.0f;
        }
        if (f8 > 0.0f) {
            boolean bl5 = canvas.hov(f11, f6, f8, f5);
            canvas.textMid(f11 + 6.0f, f6, f5, 9.5f, bl5 ? this.text : this.muted, DropdownGpuClickGuiScreen.hj("\u82d1\u9678\u6400\uff9c\u55db\uba49", -2141746315, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
            canvas.hit(f11, f6, f8, f5, () -> module.setKeyCode(-1));
        }
    }

    private void toggleCollapse(String string) {
        if (!COLLAPSED.add(string)) {
            COLLAPSED.remove(string);
        }
        this.saveLayout();
    }

    private static boolean floatingModuleSettings() {
        return HitboxConfigScreen752809.m();
    }

    private void openModuleOverlay(Module module) {
        this.openOverlay(DropdownGpuClickGuiScreen.hj("\u6395\ud7aa\ua9bb\u1784\u0ded\ueed4\u6f49", 919198355, 0x229D082C ^ CryptoVault.mask()));
        this.overlayModule = module;
    }

    private void toggleExpand(Module module) {
        if (DropdownGpuClickGuiScreen.floatingModuleSettings()) {
            if (DropdownGpuClickGuiScreen.hj("\ud15a\u5772\uf2a9\u197a\u5768\u9c0e\uafd6", 2064824064, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.overlayModule == module) {
                this.closeOverlay();
            } else {
                this.openModuleOverlay(module);
            }
            return;
        }
        if (!EXPANDED.add(module.getName())) {
            EXPANDED.remove(module.getName());
        }
        this.saveLayout();
        this.stopEditing();
        this.cancelCapture();
    }

    private List<Section> sectionsFor(Module module) {
        return this.viewFor(module).sections();
    }

    private ModuleView viewFor(Module module) {
        Object object;
        UiTextField uiTextField;
        ArrayList<Setting> arrayList;
        ModuleView moduleView = this.viewCache.get(module);
        if (moduleView != null) {
            return moduleView;
        }
        boolean bl = module == this.clickGuiModule();
        boolean bl2 = DropdownGpuClickGuiScreen.hj("\u2264\ub042\u6590\ue918\u0fef\uec34\u2234\u0968\uab67\u46e4", 625459124, 0x229D082C ^ CryptoVault.mask()).equals(module.getName());
        List<GpuClickGuiScreen.SettingGroup> list = DropdownGpuClickGuiScreen.settingGroups(module);
        IdentityHashMap<Setting, Integer> identityHashMap = new IdentityHashMap<Setting, Integer>(SUB_DEPTH);
        IdentityHashMap identityHashMap2 = new IdentityHashMap(SUB_VISIBLE);
        ArrayList<GpuClickGuiScreen.SettingGroup> arrayList2 = new ArrayList<GpuClickGuiScreen.SettingGroup>();
        int n2 = 0;
        boolean bl3 = false;
        for (GpuClickGuiScreen.SettingGroup object2 : list) {
            arrayList = new ArrayList<Setting>();
            for (Setting setting : object2.settings()) {
                if (!identityHashMap2.getOrDefault(setting, true).booleanValue() || setting instanceof ConfigKeybindListSetting || bl && setting == HitboxConfigScreen752809.ax || (bl || bl2) && !HitboxConfigScreen752809.a(setting)) continue;
                arrayList.add(setting);
                ++n2;
                if (!setting.isModified()) continue;
                bl3 = true;
            }
            if (arrayList.isEmpty()) continue;
            arrayList2.add(new GpuClickGuiScreen.SettingGroup(object2.title(), arrayList));
        }
        Object object4 = "";
        if (!bl && n2 > 5 && (uiTextField = this.settingFilters.get(module.getName())) != null) {
            object4 = uiTextField.value().trim().toLowerCase(Locale.ROOT);
        }
        ArrayList<GpuClickGuiScreen.SettingGroup> arrayList3 = ((String)object4).isEmpty() ? arrayList2 : DropdownGpuClickGuiScreen.filterGroups(arrayList2, (String)object4);
        arrayList = new ArrayList();
        for (GpuClickGuiScreen.SettingGroup settingGroup : arrayList3) {
            String string = settingGroup.title();
            arrayList.add((Setting)((Object)new Section(string == null ? DropdownGpuClickGuiScreen.hj("\ucdb0\u9b57\uc176\u07b9\u3bd6\u94c3\u945f\u3381\u889a", 733986327, 0x229D082C ^ CryptoVault.mask()) : string, settingGroup.settings(), string == null ? null : module.getName() + "|" + string, identityHashMap)));
        }
        if (bl) {
            object = new ArrayList();
            for (Setting setting : module.getSettings()) {
                HitboxKeyBinding655023 hitboxKeyBinding655023;
                if (!(setting instanceof HitboxKeyBinding655023) || (hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting).a() != module || !hitboxKeyBinding655023.isVisible()) continue;
                object.add(setting);
            }
            if (!object.isEmpty()) {
                arrayList.add((Setting)((Object)new Section(DropdownGpuClickGuiScreen.hj("\u374d\u980a\u423b\u88d3\u6d08\uf67f\ub7b0\ub9a1", 1572412891, 0x229D082C ^ CryptoVault.mask()), (List<Setting>)object)));
            }
        }
        object = new ModuleView(arrayList, n2, bl3);
        this.viewCache.put(module, (ModuleView)object);
        return object;
    }

    private float toolsHeight(ModuleView moduleView) {
        return moduleView.total() > 5 || moduleView.anyModified() ? 30.0f : 0.0f;
    }

    private float settingsHeight(Canvas canvas, Module module) {
        ModuleView moduleView = this.viewFor(module);
        float f2 = 6.0f + this.toolsHeight(moduleView);
        f2 += this.sectionsHeight(canvas, moduleView.sections(), 158.0f);
        if (moduleView.sections().isEmpty()) {
            f2 += 22.0f;
        }
        if (module != this.clickGuiModule()) {
            f2 += 38.0f;
        }
        return f2;
    }

    private static float indentOf(Section section, Setting setting) {
        return (float)Math.min(2, section.depth().getOrDefault(setting, 0)) * 10.0f;
    }

    private static boolean sectionCollapsed(Section section) {
        return section.key() != null && COLLAPSED_GROUPS.contains(section.key());
    }

    private float sectionsHeight(Canvas canvas, List<Section> list, float f2) {
        float f3 = 0.0f;
        for (Section section : list) {
            f3 += 16.0f;
            if (!DropdownGpuClickGuiScreen.sectionCollapsed(section)) {
                for (Setting setting : section.rows()) {
                    f3 += this.rowHeight(canvas, setting, f2 - DropdownGpuClickGuiScreen.indentOf(section, setting));
                }
            }
            f3 += 4.0f;
        }
        return f3;
    }

    private float drawSections(Canvas canvas, List<Section> list, float f2, float f3, float f4) {
        float f5 = f3;
        for (Section section : list) {
            boolean bl = section.key() != null;
            boolean bl2 = DropdownGpuClickGuiScreen.sectionCollapsed(section);
            boolean bl3 = bl && canvas.hov(f2 - 4.0f, f5 - 2.0f, f4 + 8.0f, 18.0f);
            int n2 = bl3 ? this.text : this.muted;
            float f6 = f2;
            String string = section.name().toUpperCase(Locale.ROOT);
            if (bl) {
                canvas.chevron(!bl2, f2 - 2.0f, f5 + 1.0f, 11.0f, n2);
                f6 += 12.0f;
                String string2 = section.key();
                canvas.hit(f2 - 4.0f, f5 - 2.0f, f4 + 8.0f, 18.0f, () -> DropdownGpuClickGuiScreen.toggleGroupCollapsed(string2));
            }
            canvas.text(f6, f5 + 2.0f, 8.5f, n2, string, UiDrawCommand.TextWeight.SEMIBOLD);
            if (bl2) {
                int n3 = 0;
                for (Setting setting : section.rows()) {
                    if (!setting.isModified()) continue;
                    ++n3;
                }
                String string2 = section.rows().size() + (section.rows().size() == 1 ? DropdownGpuClickGuiScreen.hj("\u29e1\u0d0a\u9bc1\uded1\u8a57\u1168\u6a0b\u90ea\uf860\ucb4c\ubec9\u6f8a", 1786372087, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u2850\u04b9\u9d21\u9598\u37a6\uca97\uf1e3\u1892\u91d6\u4258\u0798\u15bd", -1874569385, 0x229D082C ^ CryptoVault.mask())) + (n3 > 0 ? ", " + n3 + DropdownGpuClickGuiScreen.hj("\u0133\ub8c3\uc55e\uda94\u3fb6\ud773\u55e7\ue4b0\uf723\uda8b\u4a9f\ucb23", 106866121, 0x229D082C ^ CryptoVault.mask()) : "");
                canvas.text(f6 + canvas.tw(string, 8.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 8.0f, f5 + 2.5f, 8.0f, this.muted, string2);
            }
            f5 += 16.0f;
            if (!bl2) {
                for (Setting setting : section.rows()) {
                    float f7 = DropdownGpuClickGuiScreen.indentOf(section, setting);
                    float f8 = this.rowHeight(canvas, setting, f4 - f7);
                    if (f7 > 0.0f) {
                        canvas.rect(f2 + f7 - 5.0f, f5 + 6.0f, 1.5f, Math.max(0.0f, f8 - 12.0f), 0.75f, DropdownGpuClickGuiScreen.argb(102.0f, this.accent));
                    }
                    this.drawRow(canvas, setting, f2 + f7, f5, f4 - f7, f8);
                    f5 += f8;
                }
            }
            f5 += 4.0f;
        }
        return f5;
    }

    private float rowHeight(Canvas canvas, Setting setting, float f2) {
        if (setting == HitboxConfigScreen752809.a) {
            return this.themeGridHeight((ModeSetting)setting, f2);
        }
        if (setting instanceof SliderSetting) {
            return 50.0f;
        }
        if (setting instanceof StringSetting) {
            return 62.0f;
        }
        if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            int n2 = this.modeRows(canvas, modeSetting, f2);
            return 28.0f + (float)n2 * 24.0f + (float)(n2 - 1) * 6.0f + 8.0f;
        }
        if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            float[][] fArray = this.multiSlots(canvas, multiSelectSetting, f2);
            int n3 = fArray.length == 0 ? 1 : (int)fArray[fArray.length - 1][1] + 1;
            return 28.0f + (float)n3 * 24.0f + (float)(n3 - 1) * 6.0f + 8.0f;
        }
        return 34.0f + (float)(this.inlineLabel(canvas, setting, f2).size() - 1) * 13.0f;
    }

    private static float inlineReserve(Setting setting) {
        float f2 = (setting.isModified() ? 10.0f : 0.0f) + (setting.isExtended() ? 8.0f : 0.0f);
        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting)setting;
            return 44.0f + f2 + (booleanSetting.isBindable() ? 52.0f : 0.0f);
        }
        if (setting instanceof ColorSetting) {
            return 74.0f + f2;
        }
        if (setting instanceof HitboxKeyBinding655023) {
            return 80.0f + f2;
        }
        return f2;
    }

    private String rowLabel(Setting setting) {
        String string = setting instanceof SliderSetting ? DropdownGpuClickGuiScreen.stripUnitSuffix(setting.getName()) : setting.getName();
        return this.rowPrefix == null ? string : this.rowPrefix + string;
    }

    private List<String> inlineLabel(Canvas canvas, Setting setting, float f2) {
        List<String> list = this.wrap(canvas, this.rowLabel(setting), 11.0f, Math.max(20.0f, f2 - DropdownGpuClickGuiScreen.inlineReserve(setting)), 2);
        if (list.isEmpty()) {
            list.add("");
        }
        return list;
    }

    private void drawInlineLabel(Canvas canvas, Setting setting, float f2, float f3, float f4, float f5) {
        List<String> list = this.inlineLabel(canvas, setting, f4);
        float f6 = f3 + (f5 - 13.0f * (float)list.size()) * 0.5f;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            canvas.textMid(f2, f6 + (float)i2 * 13.0f, 13.0f, 11.0f, this.text, list.get(i2), UiDrawCommand.TextWeight.REGULAR);
        }
        this.decorX = f2 + canvas.tw(list.get(list.size() - 1), 11.0f, UiDrawCommand.TextWeight.REGULAR) + 8.0f;
        this.decorY = f6 + (float)(list.size() - 1) * 13.0f + 6.5f;
    }

    private void rowDecor(Canvas canvas, Setting setting) {
        float f2 = this.decorX;
        float f3 = this.decorY;
        if (setting.isModified()) {
            boolean bl = canvas.hov(f2 - 6.0f, f3 - 6.0f, 12.0f, 12.0f);
            if (bl) {
                canvas.circle(f2, f3, 5.0f, DropdownGpuClickGuiScreen.argb(64.0f, this.accent));
                canvas.circle(f2, f3, 3.0f, this.accent);
                this.tipTitle = DropdownGpuClickGuiScreen.hj("\uefa4\u9c13\u1801\u36da\u5df8\u0ee9\u6b52\ue840\u2b8c", 1848403280, 0x229D082C ^ CryptoVault.mask()) + setting.getName();
                this.tipDesc = DropdownGpuClickGuiScreen.hj("\ud71b\u6b13\ud59a\u2216\u5822\uabd7\u9ec3\u4739\uc4c4\ude55\ue44e\u091a\udf37\u6af5\u5e9f\u3209\u5f6b\u323a\uf371\u2256\u7b84\u377a\ucac7\u5ccc\uf473\u63f3\ud6ee\uf737\u0731\u61fb\u1ecd\u6336\u3052\ufdd2\u453f\u7e87\u714a\uc4c2\ub1a2\u5827\u70ae\ue0f2\u4c23\u60ea\u5763\u48f6\u7201\ued5a\ud290\u6cb1", 2090200072, 0x229D082C ^ CryptoVault.mask());
            } else {
                canvas.circle(f2, f3, 2.0f, this.accent);
            }
            canvas.hit(f2 - 6.0f, f3 - 6.0f, 12.0f, 12.0f, setting::resetToDefault);
            f2 += 9.0f;
        }
        if (setting.isExtended()) {
            canvas.circle(f2 + 1.0f, f3, 2.2f, -28779);
        }
    }

    private void openSettingMenu(Setting setting, Module module) {
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\uc5c2\u2b1b\u27c9\uf927\ua4eb\u3311\u1f22\ub061\uc96d\u7519\ud7e9\u504e\uc804\u91df\u7a28\u1323\ua2b5", -102684834, 0x229D082C ^ CryptoVault.mask()), false, setting.isModified(), false, setting::resetToDefault));
        if (DropdownGpuClickGuiScreen.pinnable(setting) && module != null) {
            GuiPrefs guiPrefs = DropdownGpuClickGuiScreen.guiPrefs();
            boolean bl = guiPrefs.isPinned(module.getName(), setting.getName());
            String string = bl ? DropdownGpuClickGuiScreen.hj("\u11b0\u2997\u5906\u105e\u2c02\ub691\uad9c\ud74e\uf655\u9863\u4d27\u48b0\u1b97\u688e\ufa21\u06d1\ua5bd\u3054\u9a05\u14c8\u8df4\u31e9\ub322\ud7e1\uf069\ud7b7\uc25c\ub384\u0dcb", 549075271, 0x229D082C ^ CryptoVault.mask()) : (guiPrefs.canPin() ? DropdownGpuClickGuiScreen.hj("\uc5bc\u3ea8\u7563\ufe7e\ub875\uf7f8\u2615\udeac\u0efa\u3869\u0a02\u3884\u1c7d\u574d\u3a45\uad22\u9e42\uee30\u735f\ue78b\uc3e0\u7155\ufa1d", 570943859, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\ub277\ue6bc\ufbcb\u9055\u7e4f\ud46a\ubb10\u3ddd\u76f0\u7b4d\u1b33\uceee\u8a89\u1636\ufd15\u4185\u6929\ua93c\u44c3\ud337\udcff\u5eaa\u5255\ueb8c\u35f5", 1263270618, 0x229D082C ^ CryptoVault.mask()));
            arrayList.add(new MenuItem(string, false, bl || guiPrefs.canPin(), false, () -> guiPrefs.togglePin(module.getName(), setting.getName())));
        }
        this.openContextMenu(setting.getName(), arrayList);
    }

    private void openModuleMenu(Module module) {
        GuiPrefs guiPrefs = DropdownGpuClickGuiScreen.guiPrefs();
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        boolean bl = DropdownGpuClickGuiScreen.floatingModuleSettings() ? DropdownGpuClickGuiScreen.hj("\u893d\u7b85\ue322\u70ca\ue086\ue3f2\u85b6\ub230\ue130", -1573195860, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.overlayModule == module : EXPANDED.contains(module.getName());
        arrayList.add(new MenuItem(bl ? DropdownGpuClickGuiScreen.hj("\ucabd\uea1c\u8462\u0420\ufb77\u4303\uc58d\u72d5\ub799\u1f9f\u7f7e\u41a3\u206c\ub602Z\u0950\uca74", 251918322, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u2399\u81a8\udc55\u8547\u8d24\u25c3\ufef4\u7ebb\u7a3a\u32c9\u39fa\u72c3", 87189308, 0x229D082C ^ CryptoVault.mask()), false, true, false, () -> this.toggleExpand(module)));
        arrayList.add(new MenuItem(module.isFavorite() ? DropdownGpuClickGuiScreen.hj("\u27e3\ueba9\u43ba\u874b\ufa52\u53fb\u09b0\u90bb\uabe0\u27e4\u2276\u8ddf", -1185029678, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u7bf7\udb3b\u3658\u75f2\u95db\u5048\u7155\u9ccc\u77d7\udb3c\u8ccf", 486611444, 0x229D082C ^ CryptoVault.mask()), false, true, false, () -> module.setFavorite(!module.isFavorite())));
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\u501f\u0bad\u695c\ua287\ufd1b\ue3a5\udf99\u9d83\udaad\u35fa\u8271\u5837\uf5c9\u7deb\u4352\u1e2f\u4d9f", 1587874202, 0x229D082C ^ CryptoVault.mask()), false, true, false, () -> this.host.setClipboard(DropdownGpuClickGuiScreen.moduleDebugInfo(module))));
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\u8ac1\ue555\u0312\u0e17\u66c9\u8a78\u0bbb\u0914\uda9b\u7c40\ue891\ue26c\ua626\uaf44\ub435\u7b1f\ub25d\u1ea0\ucced", -811133858, 0x229D082C ^ CryptoVault.mask()), false, DropdownGpuClickGuiScreen.moduleModified(module), true, () -> {
            DropdownGpuClickGuiScreen.resetModuleSettings(module);
            this.viewCache.clear();
        }));
        List<GuiPrefs.CustomCategory> list = guiPrefs.customCategories();
        arrayList.add(MenuItem.divider());
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\uc8d1\u9c8d\ueded\u72a9\uf35b\u0c9d\ub403\u9e87", -485292922, 0x229D082C ^ CryptoVault.mask()), false, false, false, null));
        for (GuiPrefs.CustomCategory customCategory : list) {
            String string = customCategory.id();
            arrayList.add(new MenuItem(customCategory.name(), guiPrefs.isMember(string, module.getName()), true, false, () -> guiPrefs.toggleMember(string, module.getName())));
        }
        if (guiPrefs.canCreateCategory()) {
            arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\u7f05\u5657\u88ba\u5480\uba7d\u996b\ud965\u9e56\u6bf1\ufe17\u3bd5\u5641\ubce1\udbe2\u47a9\u5972\u3125\u2ac1\u7c52\u21c4\uac4d\u6268\ub9a1\u73db\ub633\uc7a9\u36ca\u4ee5\u6721\u2326\ucb68", -1379684304, 0x229D082C ^ CryptoVault.mask()), false, true, false, () -> {
                String string = guiPrefs.createCategory(null);
                if (string == null) {
                    return;
                }
                guiPrefs.toggleMember(string, module.getName());
                this.startFolderRename(string);
            }));
        } else if (list.isEmpty()) {
            arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\u89b8\u1f2a\u4447\u6ebe\u7ae3\u743a\u656e\ub214\u4c23\ue226\ue203\u06b4", -1794701695, 0x229D082C ^ CryptoVault.mask()), false, false, false, () -> {}));
        }
        this.openContextMenu(module.getName(), arrayList);
    }

    private void openFolderMenu(String string) {
        GuiPrefs.CustomCategory customCategory = DropdownGpuClickGuiScreen.guiPrefs().customCategory(string);
        if (customCategory == null) {
            return;
        }
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\ube72\u86af\u223c\uab11\u367f\uee81\ud7a0\ubd4a\u6201", 1430698812, 0x229D082C ^ CryptoVault.mask()), false, true, false, () -> this.startFolderRename(string)));
        arrayList.add(MenuItem.divider());
        arrayList.add(new MenuItem(DropdownGpuClickGuiScreen.hj("\u6be2\ucccb\ue278\uc258\ufa8e\u9603\u7e10\ue203\ud7c4\uf753\u72ed\u7aee\uefca\ud033\u66e2", 1375734106, 0x229D082C ^ CryptoVault.mask()), false, true, true, () -> this.deleteFolder(string)));
        this.openContextMenu(customCategory.name(), arrayList);
    }

    private void openContextMenu(String string, List<MenuItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.ctxTitle = string;
        this.ctxItems = list;
        this.ctxX = (float)this.pressX;
        this.ctxY = (float)this.pressY;
    }

    private void closeContextMenu() {
        this.ctxItems = null;
        this.ctxTitle = null;
    }

    private void createFolder() {
        String string = DropdownGpuClickGuiScreen.guiPrefs().createCategory(null);
        if (string != null) {
            this.startFolderRename(string);
        }
    }

    private void deleteFolder(String string) {
        if (string == null) {
            return;
        }
        if (string.equals(this.renamingFolderId)) {
            this.renamingFolderId = null;
            if (this.kitFocus == this.folderNameField) {
                this.kitFocus = null;
            }
        }
        DropdownGpuClickGuiScreen.guiPrefs().deleteCategory(string);
        String string2 = DropdownGpuClickGuiScreen.hj("\u7c7e\u0889\u6153\u04ea\u10fa\u5f08\ua8cf\uddea\ud5e7\ucd79\u4d11", 112738227, 0x229D082C ^ CryptoVault.mask()) + string;
        POS.remove(string2);
        COLLAPSED.remove(string2);
        Z_ORDER.remove(string2);
        this.saveLayout();
    }

    private void startFolderRename(String string) {
        GuiPrefs.CustomCategory customCategory = DropdownGpuClickGuiScreen.guiPrefs().customCategory(string);
        if (customCategory == null) {
            return;
        }
        this.commitFolderRename();
        this.host.focus(this.folderNameField);
        this.renamingFolderId = string;
        this.folderNameField.setValue(customCategory.name());
        this.folderNameField.selectAll();
        COLLAPSED.remove(DropdownGpuClickGuiScreen.hj("\ue856\u6239\uf1b5\u1e01\u0083\ub67c\u48f6\ufc1a\uefda\u2576", -1273323435, 0x229D082C ^ CryptoVault.mask()) + string);
    }

    private void commitFolderRename() {
        String string = this.renamingFolderId;
        if (string == null) {
            return;
        }
        this.renamingFolderId = null;
        if (this.kitFocus == this.folderNameField) {
            this.kitFocus = null;
        }
        DropdownGpuClickGuiScreen.guiPrefs().renameCategory(string, this.folderNameField.value());
    }

    private void drawContextMenu(Canvas canvas, float f2) {
        List<MenuItem> list = this.ctxItems;
        if (list == null) {
            return;
        }
        canvas.alpha = f2;
        this.hits.add(new Hit(0.0f, 0.0f, this.sw, this.sh, this::closeContextMenu));
        this.rightHits.add(new Hit(0.0f, 0.0f, this.sw, this.sh, this::closeContextMenu));
        float f3 = 24.0f;
        float f4 = 16.0f;
        float f5 = 8.0f;
        float f6 = 5.0f;
        float f7 = this.ctxTitle == null ? 0.0f : 22.0f;
        float f8 = this.ctxTitle == null ? 0.0f : canvas.tw(this.ctxTitle, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
        float f9 = f6 * 2.0f + f7;
        for (MenuItem menuItem : list) {
            if (menuItem.label() == null) {
                f9 += f5;
                continue;
            }
            f9 += menuItem.action() == null ? f4 : f3;
            f8 = Math.max(f8, canvas.tw(menuItem.label(), 10.0f, UiDrawCommand.TextWeight.SEMIBOLD));
        }
        float f10 = Math.min(Math.max(160.0f, f8 + 48.0f), Math.max(160.0f, this.viewW * 0.5f));
        float f11 = this.ctxX / this.s + 2.0f;
        float f12 = this.ctxY / this.s - f6 - f7 - f3 * 0.5f;
        f11 = DropdownGpuClickGuiScreen.clamp(f11, 6.0f, Math.max(6.0f, this.viewW - f10 - 6.0f));
        f12 = DropdownGpuClickGuiScreen.clamp(f12, 6.0f, Math.max(6.0f, this.viewH - f9 - 6.0f));
        this.panel(canvas, f11, f12, f10, f9, 12.0f, 0.6f);
        this.hits.add(new Hit(f11 * this.s, f12 * this.s, f10 * this.s, f9 * this.s, () -> {}));
        this.rightHits.add(new Hit(f11 * this.s, f12 * this.s, f10 * this.s, f9 * this.s, () -> {}));
        float f13 = f12 + f6;
        if (this.ctxTitle != null) {
            canvas.textMid(f11 + 12.0f, f13, f7, 9.5f, this.muted, this.fit(canvas, this.ctxTitle, 9.5f, f10 - 24.0f, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            f13 += f7;
        }
        float f14 = f11 + f6;
        float f15 = f10 - f6 * 2.0f;
        for (MenuItem menuItem : list) {
            int n2;
            boolean bl;
            if (menuItem.label() == null) {
                canvas.rect(f14 + 6.0f, f13 + f5 * 0.5f, f15 - 12.0f, this.hair() / this.s, 0.0f, DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
                f13 += f5;
                continue;
            }
            if (menuItem.action() == null) {
                canvas.textMid(f14 + 8.0f, f13, f4, 8.5f, this.muted, menuItem.label().toUpperCase(Locale.ROOT), UiDrawCommand.TextWeight.SEMIBOLD);
                f13 += f4;
                continue;
            }
            boolean bl2 = bl = menuItem.enabled() && canvas.hov(f14, f13, f15, f3);
            if (bl) {
                canvas.rect(f14, f13, f15, f3, 8.0f, DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
            }
            int n3 = !menuItem.enabled() ? DropdownGpuClickGuiScreen.mix(this.muted, -16777216, 0.35f) : (menuItem.danger() ? -30070 : (n2 = bl ? this.text : DropdownGpuClickGuiScreen.mix(this.text, this.muted, 0.15f)));
            if (menuItem.checked()) {
                canvas.circle(f14 + 13.0f, f13 + f3 * 0.5f, 3.0f, this.accent);
            }
            canvas.textMid(f14 + 26.0f, f13, f3, 10.0f, n2, this.fit(canvas, menuItem.label(), 10.0f, f15 - 34.0f, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            if (menuItem.enabled()) {
                Runnable runnable = menuItem.action();
                this.hits.add(new Hit(f14 * this.s, f13 * this.s, f15 * this.s, f3 * this.s, () -> {
                    this.closeContextMenu();
                    runnable.run();
                }));
            }
            f13 += f3;
        }
        canvas.alpha = 1.0f;
    }

    private void drawDragOverlay(Canvas canvas, float f2) {
        Module module = this.dragModule;
        if (module == null) {
            return;
        }
        canvas.alpha = f2;
        this.dragDropIndex = -1;
        this.dragFolderTarget = null;
        String string = this.panelAt(this.dragPointerX, this.dragPointerY);
        if (string != null && string.startsWith(DropdownGpuClickGuiScreen.hj("\u7076\ud369\u3f27\ue076\u04c0\uc0e0\u75cb\u30b8\ueef3\u4a7f\u3140", 491629070, 0x229D082C ^ CryptoVault.mask())) && !string.equals(this.dragSource)) {
            this.dragFolderTarget = string.substring(DropdownGpuClickGuiScreen.hj("\u7882\u83b2\ue92e\u4e16\ud791\uf37a\u06e1\ue465", -1823741459, 0x229D082C ^ CryptoVault.mask()).length());
            for (ScrollZone scrollZone : this.scrollZones) {
                if (!scrollZone.cat().equals(string)) continue;
                canvas.border(scrollZone.x() / this.s, scrollZone.y() / this.s, scrollZone.w() / this.s, scrollZone.h() / this.s, 14.0f, Math.max(1.5f, this.hair()), this.accent);
            }
        } else if (string != null && string.equals(this.dragSource) && this.reorderable) {
            Object object;
            Object object2;
            ArrayList arrayList = new ArrayList();
            for (RowRect rowRect : this.rowRects) {
                if (!rowRect.panelKey().equals(this.dragSource)) continue;
                arrayList.add(rowRect);
            }
            int n2 = this.dragVisible.size();
            int n3 = arrayList.isEmpty() ? 0 : ((RowRect)arrayList.get(0)).index();
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext() && this.dragPointerY > ((RowRect)(object2 = (RowRect)iterator.next())).y() + ((RowRect)object2).h() * 0.5f) {
                n3 = ((RowRect)object2).index() + 1;
            }
            int n4 = 0;
            for (Module list2 : this.dragVisible) {
                if (!list2.isFavorite()) continue;
                ++n4;
            }
            int n5 = this.dragVisible.indexOf(module);
            List<CatalogSections.Group> list = this.panelGroups.getOrDefault(this.dragSource, List.of());
            if (list.isEmpty()) {
                n3 = CatalogDrag.clampToGroup(n3, module.isFavorite(), n4, n2);
            } else {
                object = new boolean[n2];
                for (int rowRect = 0; rowRect < n2; ++rowRect) {
                    object[rowRect] = this.dragVisible.get(rowRect).isFavorite();
                }
                n3 = CatalogSections.clampDrop(n3, n5, list, module.isFavorite(), (boolean[])object);
            }
            this.dragDropIndex = n3;
            if (n3 != n5 && n3 != n5 + 1) {
                object = arrayList.iterator();
                while (object.hasNext()) {
                    RowRect rowRect = (RowRect)object.next();
                    if (rowRect.index() == n3 - 1) {
                        canvas.rect(rowRect.x() / this.s, (rowRect.y() + rowRect.h()) / this.s - 1.5f, rowRect.w() / this.s, 3.0f, 1.5f, this.accent);
                    } else {
                        if (rowRect.index() != n3) continue;
                        canvas.rect(rowRect.x() / this.s, rowRect.y() / this.s - 1.5f, rowRect.w() / this.s, 3.0f, 1.5f, this.accent);
                    }
                    break;
                }
            }
        }
        float f3 = canvas.tw(module.getName(), 10.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f;
        float f4 = this.dragPointerX / this.s + 10.0f;
        float f5 = this.dragPointerY / this.s - 12.0f;
        this.panel(canvas, f4, f5, f3, 24.0f, 12.0f, 0.5f);
        canvas.rect(f4, f5, f3, 24.0f, 12.0f, DropdownGpuClickGuiScreen.argb(48.0f, this.accent));
        canvas.textMid(f4 + 12.0f, f5, 24.0f, 10.5f, this.text, module.getName(), UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.alpha = 1.0f;
    }

    private void commitDrag() {
        Module module = this.dragModule;
        List<Module> list = this.dragVisible;
        int n2 = this.dragDropIndex;
        String string = this.dragFolderTarget;
        String string2 = this.dragSource;
        this.cancelDrag();
        if (module == null) {
            return;
        }
        GuiPrefs guiPrefs = DropdownGpuClickGuiScreen.guiPrefs();
        if (string != null) {
            if (!guiPrefs.isMember(string, module.getName())) {
                guiPrefs.toggleMember(string, module.getName());
            }
            return;
        }
        if (n2 < 0 || string2 == null) {
            return;
        }
        ArrayList<String> arrayList = new ArrayList<String>(list.size());
        for (Module object : list) {
            arrayList.add(object.getName());
        }
        List<String> list2 = GuiPrefs.move(arrayList, module.getName(), n2);
        if (list2.equals(arrayList)) {
            return;
        }
        if (string2.startsWith(DropdownGpuClickGuiScreen.hj("\u229f\uae1e\u1b8a\u050a\ub818\uf98e\uc831\ub506\u4953\ua9c3\u1d6e", 1829356100, 0x229D082C ^ CryptoVault.mask()))) {
            String string3 = string2.substring(DropdownGpuClickGuiScreen.hj("\u8192\u2282\u825b\u74ed\uba52\u4882\ue075\u73a5", -2127706197, 0x229D082C ^ CryptoVault.mask()).length());
            GuiPrefs.CustomCategory customCategory = guiPrefs.customCategory(string3);
            if (customCategory != null) {
                guiPrefs.setMembers(string3, GuiPrefs.mergeVisible(customCategory.modules(), list2));
            }
        } else {
            guiPrefs.setOrder(string2, GuiPrefs.mergeVisible(guiPrefs.order(string2), list2));
        }
    }

    private void cancelDrag() {
        this.pendingPress = null;
        this.dragModule = null;
        this.dragSource = null;
        this.dragVisible = List.of();
        this.dragDropIndex = -1;
        this.dragFolderTarget = null;
    }

    private void onSearchChanged() {
        this.navIndex = this.searchField.isEmpty() ? -1 : 0;
        this.navScrollPending = this.navVisible = !this.searchField.isEmpty();
    }

    private void activateNav(boolean bl) {
        int n2;
        if (this.navModules.isEmpty()) {
            return;
        }
        this.navIndex = n2 = this.navIndex >= 0 && this.navIndex < this.navModules.size() ? this.navIndex : 0;
        this.navVisible = true;
        Module module = this.navModules.get(n2);
        if ((bl || module.isOneShot() || module.isLocked()) && DropdownGpuClickGuiScreen.floatingModuleSettings()) {
            this.openModuleOverlay(module);
        } else if (bl || module.isOneShot() || module.isLocked()) {
            EXPANDED.add(module.getName());
            this.saveLayout();
        } else {
            module.toggle();
        }
    }

    private boolean moveNav(int n2) {
        int n3 = this.navModules.size();
        if (n3 == 0) {
            return false;
        }
        if (this.navIndex < 0 || this.navIndex >= n3 || !this.navVisible) {
            this.navIndex = Math.max(0, Math.min(n3 - 1, this.navIndex));
            this.navVisible = true;
            this.navScrollPending = true;
            return true;
        }
        if (n2 == 265 || n2 == 263) {
            this.navIndex = Math.max(0, this.navIndex - 1);
        } else if (n2 == 264 || n2 == 262) {
            this.navIndex = Math.min(n3 - 1, this.navIndex + 1);
        } else {
            return false;
        }
        this.navScrollPending = true;
        return true;
    }

    private void focusSearch() {
        this.host.focus(this.searchField);
    }

    private float themeGridHeight(ModeSetting modeSetting, float f2) {
        return this.themeGridHeight(modeSetting, f2, 2);
    }

    private float themeGridHeight(ModeSetting modeSetting, float f2, int n2) {
        int n3 = (modeSetting.getModes().size() + n2 - 1) / n2;
        return 16.0f + (float)n3 * 48.0f - 6.0f + 6.0f;
    }

    private void drawThemeGrid(Canvas canvas, ModeSetting modeSetting, float f2, float f3, float f4) {
        this.drawThemeGrid(canvas, modeSetting, f2, f3, f4, 2, DropdownGpuClickGuiScreen.hj("\u6cbd\u335e\u723a\u13e8\u45b6\ueb19\u0f28\u57ec", 1761095982, 0x229D082C ^ CryptoVault.mask()), modeSetting::setCurrentMode);
    }

    private void drawThemeGrid(Canvas canvas, ModeSetting modeSetting, float f2, float f3, float f4, int n2, String string, Consumer<String> consumer) {
        canvas.text(f2, f3, 10.0f, this.muted, string, UiDrawCommand.TextWeight.SEMIBOLD);
        float f5 = f3 + 16.0f;
        List<String> list = modeSetting.getModes();
        float f6 = 6.0f;
        float f7 = (f4 - f6 * (float)(n2 - 1)) / (float)n2;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            String string2 = list.get(i2);
            float f8 = f2 + (float)(i2 % n2) * (f7 + f6);
            float f9 = f5 + (float)(i2 / n2) * (42.0f + f6);
            boolean bl = modeSetting.is(string2);
            boolean bl2 = canvas.hov(f8, f9, f7, 42.0f);
            canvas.rect(f8, f9, f7, 42.0f, 8.0f, DropdownGpuClickGuiScreen.argb(48.0f, 0));
            float f10 = f8 + 3.0f;
            float f11 = f9 + 3.0f;
            float f12 = f7 - 6.0f;
            UiSurface uiSurface = UiSurface.byName(string2);
            if (!uiSurface.isTheme()) {
                canvas.b.material(f10 * this.s, f11 * this.s, f12 * this.s, 26.0f * this.s, 6.0f * this.s, uiSurface.preview(UiThemeMaterial.FLUX.accent() & 0xFFFFFF));
            } else if (UiThemeMaterial.LIQUID_GLASS.name().equalsIgnoreCase(string2)) {
                int n3 = UiThemeMaterial.LIQUID_GLASS.accent() & 0xFFFFFF;
                canvas.gradient(f10, f11, f12, 26.0f, 6.0f, n3 | 0x40000000, 0x22FFFFFF, n3 | 0x18000000, 0x10FFFFFF);
            } else {
                canvas.b.material(f10 * this.s, f11 * this.s, f12 * this.s, 26.0f * this.s, 6.0f * this.s, UiThemeMaterial.preview(string2));
            }
            canvas.border(f10, f11, f12, 26.0f, 6.0f, this.hair(), DropdownGpuClickGuiScreen.argb(26.0f, 0xFFFFFF));
            String string3 = this.fit(canvas, string2, 9.0f, f7 - 10.0f, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.text(f8 + 6.0f, f9 + 26.0f + 6.0f, 9.0f, bl ? this.accent : this.text, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.border(f8, f9, f7, 42.0f, 8.0f, bl ? Math.max(1.3f, this.hair()) : this.hair(), bl ? this.accent : (bl2 ? DropdownGpuClickGuiScreen.argb(64.0f, 0xFFFFFF) : DropdownGpuClickGuiScreen.argb(24.0f, 0xFFFFFF)));
            String string4 = string2;
            canvas.hit(f8, f9, f7, 42.0f, () -> consumer.accept(string4));
        }
    }

    private float[][] chipSlots(Canvas canvas, ModeSetting modeSetting, float f2) {
        List<String> list = modeSetting.getModes();
        float[][] fArray = new float[list.size()][3];
        int n2 = 0;
        float f3 = 0.0f;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f4 = this.chipWidth(canvas, list.get(i2));
            if (f3 > 0.0f && f3 + f4 > f2) {
                ++n2;
                f3 = 0.0f;
            }
            fArray[i2][0] = f3;
            fArray[i2][1] = n2;
            fArray[i2][2] = f4;
            f3 += f4 + 6.0f;
        }
        return fArray;
    }

    private int modeRows(Canvas canvas, ModeSetting modeSetting, float f2) {
        float[][] fArray = this.chipSlots(canvas, modeSetting, f2);
        return fArray.length == 0 ? 1 : (int)fArray[fArray.length - 1][1] + 1;
    }

    private float[][] multiSlots(Canvas canvas, MultiSelectSetting multiSelectSetting, float f2) {
        float f3;
        int n2;
        List<String> list = multiSelectSetting.E();
        float[][] fArray = new float[list.size()][3];
        int n3 = 0;
        float f4 = 0.0f;
        for (n2 = 0; n2 < list.size(); ++n2) {
            f3 = Math.min(f2, this.chipWidth(canvas, list.get(n2)) + 14.0f);
            if (f4 > 0.0f && f4 + f3 > f2) {
                ++n3;
                f4 = 0.0f;
            }
            fArray[n2][0] = f4;
            fArray[n2][1] = n3;
            fArray[n2][2] = f3;
            f4 += f3 + 6.0f;
        }
        for (n2 = 0; n2 <= n3; ++n2) {
            float f5;
            f3 = 0.0f;
            int n4 = 0;
            for (float[] fArray2 : fArray) {
                if ((int)fArray2[1] != n2) continue;
                f3 = fArray2[0] + fArray2[2];
                ++n4;
            }
            if (n4 == 0 || (f5 = (f2 - f3) / (float)n4) <= 0.0f) continue;
            int n5 = 0;
            for (float[] fArray3 : fArray) {
                if ((int)fArray3[1] != n2) continue;
                fArray3[0] = fArray3[0] + f5 * (float)n5;
                fArray3[2] = fArray3[2] + f5;
                ++n5;
            }
        }
        return fArray;
    }

    private void drawMultiSelect(Canvas canvas, MultiSelectSetting multiSelectSetting, float f2, float f3, float f4) {
        float f5 = canvas.tw(DropdownGpuClickGuiScreen.hj("\ua64f\u0cb4\ua842\u2f7c\u54fa\ua74e", 520922738, 0x229D082C ^ CryptoVault.mask()), 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 14.0f;
        float f6 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u0bc5\u6bb8\u6860\u0d26\u82b3", -1659488625, 0x229D082C ^ CryptoVault.mask()), 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 14.0f;
        float f7 = f2 + f4 - f6;
        float f8 = f7 - 5.0f - f5;
        float f9 = f3 + 3.0f;
        float f10 = 18.0f;
        String string = multiSelectSetting.V();
        float f11 = canvas.tw(string, 9.0f, UiDrawCommand.TextWeight.REGULAR);
        boolean bl = f4 >= 260.0f;
        this.topLabel(canvas, multiSelectSetting, f2, f3, f8 - (bl ? f11 + 18.0f : 12.0f));
        if (bl) {
            canvas.text(f8 - 8.0f - f11, f3 + 8.6f, 9.0f, this.muted, string, UiDrawCommand.TextWeight.REGULAR);
        }
        boolean bl2 = canvas.hov(f8, f9, f5, f10);
        boolean bl3 = canvas.hov(f7, f9, f6, f10);
        canvas.rect(f8, f9, f5, f10, 9.0f, DropdownGpuClickGuiScreen.argb(bl2 ? 28.0f : 14.0f, 0xFFFFFF));
        canvas.border(f8, f9, f5, f10, 9.0f, this.hair(), DropdownGpuClickGuiScreen.argb(bl2 ? 44.0f : 28.0f, 0xFFFFFF));
        canvas.textMid(f8 + 7.0f, f9, f10, 9.0f, multiSelectSetting.x() ? this.accent : this.text, DropdownGpuClickGuiScreen.hj("\u1c6e\ud6f4\u9162\u8a0b\u3325\uad9a\u21ae", -1578674586, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.hit(f8, f9, f5, f10, multiSelectSetting::I);
        canvas.rect(f7, f9, f6, f10, 9.0f, DropdownGpuClickGuiScreen.argb(bl3 ? 28.0f : 14.0f, 0xFFFFFF));
        canvas.border(f7, f9, f6, f10, 9.0f, this.hair(), DropdownGpuClickGuiScreen.argb(bl3 ? 44.0f : 28.0f, 0xFFFFFF));
        canvas.textMid(f7 + 7.0f, f9, f10, 9.0f, multiSelectSetting.y() ? this.accent : this.text, DropdownGpuClickGuiScreen.hj("\uc7c0\u81f5\u91f8\u8b71\u2d83", 664200961, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.hit(f7, f9, f6, f10, multiSelectSetting::J);
        float[][] fArray = this.multiSlots(canvas, multiSelectSetting, f4);
        List<String> list = multiSelectSetting.E();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            String string2 = list.get(i2);
            float f12 = fArray[i2][0];
            float f13 = fArray[i2][2];
            float f14 = f3 + 28.0f + fArray[i2][1] * 30.0f;
            boolean bl4 = multiSelectSetting.D(string2);
            boolean bl5 = canvas.hov(f2 + f12, f14, f13, 24.0f);
            if (bl5 && multiSelectSetting.C(string2) != null) {
                this.tipTitle = string2;
                this.tipDesc = multiSelectSetting.C(string2);
            }
            float f15 = this.ease(DropdownGpuClickGuiScreen.hj("\uea25\u2fdb\uc2fa\ud50e\u25d1\u50ac\u00b4\u2e56\ua0e4", 1503554436, 0x229D082C ^ CryptoVault.mask()) + System.identityHashCode(multiSelectSetting) + string2, bl4 ? 1.0f : 0.0f, 14.0f);
            canvas.rect(f2 + f12, f14, f13, 24.0f, 12.0f, DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(bl5 ? 22.0f : 11.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(52.0f, this.accent), f15));
            canvas.border(f2 + f12, f14, f13, 24.0f, 12.0f, this.hair(), DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(22.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(192.0f, this.accent), f15));
            int n2 = bl4 || bl5 ? this.text : this.muted;
            float f16 = f2 + f12 + 11.0f;
            float f17 = f14 + 12.0f;
            if (bl4) {
                canvas.circle(f16, f17, 3.4f, this.accent);
            } else {
                canvas.border(f16 - 3.4f, f17 - 3.4f, 6.8f, 6.8f, 3.4f, 1.0f, n2);
            }
            String string3 = DropdownGpuClickGuiScreen.trim(string2, 18);
            float f18 = f13 - 24.0f;
            float f19 = canvas.tw(string3, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.textMid(f2 + f12 + 20.0f + Math.max(0.0f, (f18 - f19) * 0.5f), f14, 24.0f, 9.5f, n2, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.hit(f2 + f12, f14, f13, 24.0f, () -> multiSelectSetting.E(string2));
        }
    }

    private float chipWidth(Canvas canvas, String string) {
        return Math.min(150.0f, canvas.tw(DropdownGpuClickGuiScreen.trim(string, 18), 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 22.0f);
    }

    private void topLabel(Canvas canvas, Setting setting, float f2, float f3, float f4) {
        String string = this.fit(canvas, this.rowLabel(setting), 11.0f, f4);
        canvas.text(f2, f3 + 8.0f, 11.0f, this.text, string, UiDrawCommand.TextWeight.REGULAR);
        this.decorX = f2 + canvas.tw(string, 11.0f, UiDrawCommand.TextWeight.REGULAR) + 8.0f;
        this.decorY = f3 + 14.0f;
        this.decorOn = true;
    }

    private void drawRow(Canvas canvas, Setting setting, float f2, float f3, float f4, float f5) {
        Object object;
        Object object2;
        boolean bl = canvas.hov(f2, f3, f4, f5);
        float f6 = this.ease(DropdownGpuClickGuiScreen.hj("\u57af\u3cd4\u770e\u28a0\ue2d9\u830c\u1504", -2061777919, 0x229D082C ^ CryptoVault.mask()) + System.identityHashCode(setting), bl ? 1.0f : 0.0f, 16.0f);
        if (f6 > 0.01f) {
            canvas.rect(f2 - 4.0f, f3 + 1.0f, f4 + 8.0f, f5 - 2.0f, 8.0f, DropdownGpuClickGuiScreen.argb((int)(14.0f * f6), 0xFFFFFF));
        }
        if (bl) {
            Object object3 = object2 = setting.getDescription() == null ? "" : setting.getDescription().trim();
            Object object4 = setting.isModified() ? DropdownGpuClickGuiScreen.hj("\ud95e\ub08d\uac19\u49df\u0750\ued6d\ufc12\u7d20\u88da\ud66d\u220e\u90ca\u7f02\u033f\u0f7f\ud4bc\ueb7e\u05ef\u6f36\u3265\uc393\ufd53\u569f\u568c\ue86e\u2acc\u1e43\u2c11\u4a0a\u5840\ud874\uf71a\uee7b\ud62f\u98cb\uda85\ubd00\u88ca7\u33ab\ud277\u79e0\ubbb7\u0e71\ue93e\ufe54", 1694230011, 0x229D082C ^ CryptoVault.mask()) : (object = DropdownGpuClickGuiScreen.pinnable(setting) && this.rowOwner != null ? DropdownGpuClickGuiScreen.hj("\u67d3\u894c\u8433\ua524\u9617\u93f2\uf938\u5bff\ua3ad\ud71d\u3ebc\u6f29\udd5a\ud6b1\u0526\u2026\u172d\u8940\u4476\u7c88\uc96a\u7041\u0706\u2185\u714a\ucc24\u74a6\u7fdc\ub860\u0e08\ud14f\u1ec2\uc2a5\udb43\ud857\u8e3a\uc74c\u1014\u3277\ud8a9\u457f\ucfbd\u384c\ud8d4", -1986525069, 0x229D082C ^ CryptoVault.mask()) : "");
            if (!((String)object).isEmpty()) {
                Object object5 = object2 = ((String)object2).isEmpty() ? object : (String)object2 + " " + (String)object;
            }
            if (!((String)object2).isEmpty()) {
                this.tipTitle = setting.getName();
                this.tipDesc = object2;
            }
        }
        this.decorOn = false;
        object2 = this.rowOwner;
        canvas.rhit(f2, f3, f4, f5, () -> this.lambda$drawRow$0(setting, (Module)object2));
        if (setting instanceof BooleanSetting) {
            object = (BooleanSetting)setting;
            this.drawInlineLabel(canvas, setting, f2, f3, f4, f5);
            this.decorOn = true;
            this.toggleSwitch(canvas, DropdownGpuClickGuiScreen.hj("\u47e8\u038b\ud5fb\u7555\u1762\u8bea\udcd4", -133759631, 0x229D082C ^ CryptoVault.mask()) + System.identityHashCode(object), f2 + f4 - 34.0f, f3 + (f5 - 18.0f) * 0.5f, ((BooleanSetting)object).isEnabled());
            canvas.hit(f2, f3, f4, f5, ((BooleanSetting)object)::toggle);
            if (((BooleanSetting)object).isBindable()) {
                boolean bl2 = this.bindingBool == object;
                int n2 = ((BooleanSetting)object).getKeyCode();
                String string = bl2 ? DropdownGpuClickGuiScreen.hj("\uca6f\uf1cd\uc077\ue4ac\u4fd3\uaca2\u95e6\u28df\u3bcf\u80be\ufd0a\uc129\uc54e\u5649\uca40", -501787652, 0x229D082C ^ CryptoVault.mask()) : (n2 == -1 || n2 == 0 ? DropdownGpuClickGuiScreen.hj("\ua8d7\u05ec\u5491\ue392\uc622\u5f5f", -834723045, 0x229D082C ^ CryptoVault.mask()) : ((BooleanSetting)object).getKeyDisplayName() + (((BooleanSetting)object).isKeyHoldMode() ? DropdownGpuClickGuiScreen.hj("\ua585\uc809\u427e\uc860\u2146\ud293\ub557\udf55\u9582", -1206363307, 0x229D082C ^ CryptoVault.mask()) : ""));
                float f7 = 52.0f;
                float f8 = 20.0f;
                float f9 = f2 + f4 - 34.0f - 6.0f - f7;
                float f10 = f3 + (f5 - f8) * 0.5f;
                boolean bl3 = canvas.hov(f9, f10, f7, f8);
                canvas.rect(f9, f10, f7, f8, 8.0f, DropdownGpuClickGuiScreen.argb(bl2 ? 36.0f : (bl3 ? 24.0f : 14.0f), 0xFFFFFF));
                canvas.border(f9, f10, f7, f8, 8.0f, this.hair(), bl2 ? DropdownGpuClickGuiScreen.argb(192.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl3 ? 44.0f : 28.0f, 0xFFFFFF));
                String string2 = this.fit(canvas, string, 8.5f, f7 - 8.0f, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.textMid(f9 + (f7 - canvas.tw(string2, 8.5f, UiDrawCommand.TextWeight.SEMIBOLD)) * 0.5f, f10, f8, 8.5f, bl2 ? this.accent : (n2 == -1 || n2 == 0 ? this.muted : this.text), string2, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.hit(f9, f10, f7, f8, () -> this.lambda$drawRow$1((BooleanSetting)object));
                canvas.rhit(f9, f10, f7, f8, () -> DropdownGpuClickGuiScreen.lambda$drawRow$2((BooleanSetting)object));
                if (bl3) {
                    this.tipTitle = setting.getName() + DropdownGpuClickGuiScreen.hj("\ufe65\u4210\u8f52\u2635\u4991", 1619017637, 0x229D082C ^ CryptoVault.mask());
                    this.tipDesc = DropdownGpuClickGuiScreen.hj("\u0f4e\u8bb9\u6fcf\u88ad\u8200\ubdb7\u63c8\u0ce4\u6822\u74cf\u6fd0\u353f\u2f95\u0bcf\u965b\u8b47\u910f\ue6ec\u3df0\ub2b8\ud4f1\u2aa1\ubf8a\u945f\udfd5\u16fc\u4530\ua455\ucd62\u98cb\ued99\u7fb5\u8312\u62c4\u0396\u39dd\ue1d4\uf530\u6d63\u3efa\u3e76\u54fd\u53a7\u140e\u10e2\ud5b3\u5836\u9c92\uc79f\u5435\u56b9\ud097\uffe8\ua1bf\uac8f\u5462\ub0e6\u8a43\u5414\u5cfc\u616c\u9d12\uf429\u6995\uc45b", 1983265343, 0x229D082C ^ CryptoVault.mask()) + (((BooleanSetting)object).isKeyHoldMode() ? DropdownGpuClickGuiScreen.hj("\u5890\u0d8e\ud545\u4203\u9bf7\u416b\u6a17\u9d0a\ubc32\u3736\ud7ea\uc033\u7e3e\u713f\ufc10\uc697\u9669\u0504", 1745283116, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u33b1\u3229\u7004\uaec8\ud4fb\u55c6\uae28\u9be4\ucaac\u048d\u5c3c\uaf56\u175f\ub011\u43d3\u3cab\u823f\u7bae\u60e5\u33ad\u5183\u983e\u637b\u860e\u6604\u114e\u1c73\uc718\u8c34\u6bcf\u4a28\uafaa\u8665\u70b5\u7166\uf002\u5dfb", -1930199273, 0x229D082C ^ CryptoVault.mask()));
                }
            }
        } else if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            String string = DropdownGpuClickGuiScreen.sliderValueText(sliderSetting);
            float f11 = canvas.tw(string, 10.0f, UiDrawCommand.TextWeight.SEMIBOLD);
            this.topLabel(canvas, setting, f2, f3, f4 - f11 - 20.0f - (setting.isModified() ? 10.0f : 0.0f));
            float f12 = Math.max(f11 + 16.0f, 52.0f);
            float f13 = 18.0f;
            float f14 = f2 + f4 - f12 + 4.0f;
            float f15 = f3 + 4.0f;
            if (this.sliderEdit.editing(sliderSetting, false)) {
                this.sliderEdit.draw(canvas.b, this.host, f14 * this.s, f15 * this.s, f12 * this.s, f13 * this.s, this.s);
            } else {
                boolean bl4 = canvas.hov(f14, f15, f12, f13);
                if (bl4) {
                    canvas.rect(f14, f15, f12, f13, f13 * 0.5f, DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
                }
                canvas.text(f2 + f4 - f11, f3 + 8.6f, 10.0f, this.accent, string, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.hit(f14, f15, f12, f13, () -> this.sliderEdit.begin(this.host, sliderSetting, false));
                if (bl4) {
                    this.tipTitle = setting.getName();
                    this.tipDesc = DropdownGpuClickGuiScreen.hj("\u0dd0\u2e92\uc8ab\uc7e2\uae7b\u905a\u638c\u95a7\u2e45\u3d2d\u8ec7\u61c0\u1c13\u6851\u1284\u559b\u9c64\u9a87\uc59b\u340f\u5e48\uaf4d\u83e5\ubcca\u73a0\ubacc\u632c\ude91\u9792\u1c41\uce39\u3ad3\u6e55\u66f4\u255f\u0b4a\ua958\ue1bd\ue982\ub0b7\ub457\u2159\ue0a6\uc949\u4b04\u36d3\u4045\u74e0\uacc6\ub229\u8869\uff1c\udb15\u6df3\u18f1\ube83\u7633\u4cff\u1472\u0f40\u2860\u62e3\u0c9d\u5c97\u09a6\u83b7\u1f82\u9290\uc59c", -1945575542, 0x229D082C ^ CryptoVault.mask());
                }
            }
            float f16 = f2;
            float f17 = f4;
            float f18 = f3 + f5 - 15.0f;
            float f19 = 4.5f;
            double d2 = sliderSetting.E() - sliderSetting.D();
            float f20 = d2 == 0.0 ? 0.0f : DropdownGpuClickGuiScreen.clamp((float)((sliderSetting.B() - sliderSetting.D()) / d2), 0.0f, 1.0f);
            canvas.rect(f16, f18, f17, f19, f19 * 0.5f, DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
            if (f20 > 0.0f) {
                canvas.shadow(f16, f18 + 1.0f, Math.max(f19, f17 * f20), f19, f19 * 0.5f, 8.0f, 0.0f, DropdownGpuClickGuiScreen.argb(80.0f, this.accent));
                canvas.rect(f16, f18, Math.max(f19, f17 * f20), f19, f19 * 0.5f, DropdownGpuClickGuiScreen.argb(255.0f, this.accent));
            }
            boolean bl5 = this.draggingSlider == sliderSetting || canvas.hov(f16 - 6.0f, f18 - 8.0f, f17 + 12.0f, 20.0f);
            float f21 = bl5 ? 6.5f : 5.5f;
            float f22 = f16 + f17 * f20;
            canvas.shadow(f22 - f21, f18 + f19 * 0.5f - f21 + 1.0f, f21 * 2.0f, f21 * 2.0f, f21, 5.0f, 0.0f, 0x66000000);
            canvas.circle(f22, f18 + f19 * 0.5f, f21, -526086);
            float f23 = canvas.px(f16);
            float f24 = f17 * this.s;
            canvas.hit(f16 - 6.0f, f3 + 16.0f, f17 + 12.0f, f5 - 16.0f, () -> {
                this.draggingSlider = sliderSetting;
                this.sliderPx = f23;
                this.sliderWpx = f24;
                this.applySlider(sliderSetting, this.pressX);
            });
        } else if (setting == HitboxConfigScreen752809.a) {
            this.drawThemeGrid(canvas, (ModeSetting)setting, f2, f3, f4);
        } else if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            this.topLabel(canvas, setting, f2, f3, f4 - 12.0f);
            float[][] fArray = this.chipSlots(canvas, modeSetting, f4);
            List<String> list = modeSetting.getModes();
            for (int i2 = 0; i2 < list.size(); ++i2) {
                String string = list.get(i2);
                float f25 = fArray[i2][0];
                float f26 = fArray[i2][2];
                float f27 = f3 + 28.0f + fArray[i2][1] * 30.0f;
                boolean bl6 = string.equals(modeSetting.getCurrentMode());
                boolean bl7 = canvas.hov(f2 + f25, f27, f26, 24.0f);
                float f28 = this.ease(DropdownGpuClickGuiScreen.hj("\u3ccb\u98e5\uee20\u642d\u95c5\uff5f", -1879113495, 0x229D082C ^ CryptoVault.mask()) + System.identityHashCode(modeSetting) + string, bl6 ? 1.0f : 0.0f, 14.0f);
                canvas.rect(f2 + f25, f27, f26, 24.0f, 12.0f, DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(bl7 ? 22.0f : 11.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(52.0f, this.accent), f28));
                canvas.border(f2 + f25, f27, f26, 24.0f, 12.0f, this.hair(), DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(22.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(192.0f, this.accent), f28));
                String string3 = DropdownGpuClickGuiScreen.trim(string, 18);
                float f29 = canvas.tw(string3, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.textMid(f2 + f25 + (f26 - f29) * 0.5f, f27, 24.0f, 9.5f, bl6 || bl7 ? this.text : this.muted, string3, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.hit(f2 + f25, f27, f26, 24.0f, () -> modeSetting.setCurrentMode(string));
            }
        } else if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            this.drawMultiSelect(canvas, multiSelectSetting, f2, f3, f4);
        } else if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            this.drawInlineLabel(canvas, setting, f2, f3, f4, f5);
            this.decorOn = true;
            int n3 = colorSetting.A().getRGB();
            String string = colorSetting.v() ? DropdownGpuClickGuiScreen.hj("\uff37\u6ec2\ud99a\u4900\u339c\ua704\u8f49\ua7fc\u78ea\uf598\ud9d0", 1347022990, 0x229D082C ^ CryptoVault.mask()) : String.format(Locale.ROOT, DropdownGpuClickGuiScreen.hj("\u6f9e\u7b07\uf5c6\ub3b1\u17c3\ubad8", -943159905, 0x229D082C ^ CryptoVault.mask()), n3 & 0xFFFFFF);
            float f30 = canvas.tw(string, 9.5f, UiDrawCommand.TextWeight.REGULAR);
            canvas.circle(f2 + f4 - 13.0f, f3 + f5 * 0.5f, 11.0f, DropdownGpuClickGuiScreen.argb(52.0f, 0xFFFFFF));
            canvas.circle(f2 + f4 - 13.0f, f3 + f5 * 0.5f, 10.0f, n3 | 0xFF000000);
            canvas.textMid(f2 + f4 - 32.0f - f30, f3, f5, 9.5f, this.muted, string, UiDrawCommand.TextWeight.REGULAR);
            canvas.hit(f2, f3, f4, f5, () -> this.openPicker(colorSetting));
        } else if (setting instanceof StringSetting) {
            String string;
            StringSetting stringSetting = (StringSetting)setting;
            this.topLabel(canvas, setting, f2, f3, f4 - 12.0f);
            float f31 = f2;
            float f32 = f3 + 28.0f;
            float f33 = f4;
            float f34 = 26.0f;
            boolean bl8 = stringSetting.Y();
            canvas.rect(f31, f32, f33, f34, 8.0f, DropdownGpuClickGuiScreen.argb(bl8 ? 24.0f : 12.0f, 0xFFFFFF));
            canvas.border(f31, f32, f33, f34, 8.0f, this.hair(), bl8 ? DropdownGpuClickGuiScreen.argb(176.0f, this.accent) : DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
            String string4 = string = stringSetting.X().isEmpty() ? DropdownGpuClickGuiScreen.hj("\uad91\u4491\u9d85\u441a\ucaca\ubdf0\u091a\u4772\uff60\u3d3c\u6ee7\u0f00\u2e89\ub9e2", 303096140, 0x229D082C ^ CryptoVault.mask()) : stringSetting.X();
            if (bl8 && this.nanoTime() / 500000000L % 2L == 0L) {
                string = string + "|";
            }
            String string5 = this.fit(canvas, string, 9.5f, f33 - 20.0f);
            if (bl8 && !string5.equals(string)) {
                int n4;
                for (n4 = 0; n4 < string.length() - 1; ++n4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (!(canvas.tw(stringBuilder.append("\u2026").append(string.substring(n4)).toString(), 9.5f, UiDrawCommand.TextWeight.REGULAR) > f33 - 20.0f)) break;
                }
                string5 = "\u2026" + string.substring(n4);
            }
            canvas.textMid(f31 + 10.0f, f32, f34, 9.5f, stringSetting.X().isEmpty() && !bl8 ? this.muted : this.text, string5, UiDrawCommand.TextWeight.REGULAR);
            canvas.hit(f31, f32, f33, f34, () -> this.beginEdit(stringSetting));
        } else if (setting instanceof HitboxKeyBinding655023) {
            boolean bl9;
            HitboxKeyBinding655023 hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting;
            this.drawInlineLabel(canvas, setting, f2, f3, f4, f5);
            Module module = hitboxKeyBinding655023.a();
            boolean bl10 = bl9 = this.bindingSetting == hitboxKeyBinding655023;
            String string = bl9 ? DropdownGpuClickGuiScreen.hj("\ud0dc\u4bb7\u366b\u1b42\u7282\u9c48\ubd74\u39ec\u7c9b\u9c95\u1b4b\u6787\u221e\u89d2", -620828882, 0x229D082C ^ CryptoVault.mask()) : (module == null || module.getKeyCode() == -1 || module.getKeyCode() == 0 ? DropdownGpuClickGuiScreen.hj("\u0158\u2802\uad77\u1865\u3997\ua057\u2b2a\ud821\u257a", -43532861, 0x229D082C ^ CryptoVault.mask()) : module.getKeyDisplayName());
            float f35 = Math.max(64.0f, canvas.tw(string, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f);
            float f36 = 26.0f;
            this.keyCap(canvas, f2 + f4 - f35, f3 + (f5 - f36) * 0.5f, f35, f36, string, bl9);
            canvas.hit(f2, f3, f4, f5, () -> {
                this.cancelCapture();
                this.bindingSetting = hitboxKeyBinding655023;
                if (hitboxKeyBinding655023.a() != null) {
                    hitboxKeyBinding655023.a().setBinding(true);
                }
            });
            if (!bl9 && BindModeControl.available(module)) {
                String string6 = BindModeControl.label(module);
                float f37 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u7f19\u6d42\udf1d\u6e89\uad4a\u4524\u38c6", 1324296904, 0x229D082C ^ CryptoVault.mask()), 9.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f;
                float f38 = f2 + f4 - f35 - 8.0f - f37;
                float f39 = f3 + (f5 - f36) * 0.5f;
                boolean bl11 = BindModeControl.hold(module);
                boolean bl12 = canvas.hov(f38, f39, f37, f36);
                canvas.rect(f38, f39, f37, f36, 8.0f, bl11 ? DropdownGpuClickGuiScreen.argb(48.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl12 ? 24.0f : 14.0f, 0xFFFFFF));
                canvas.border(f38, f39, f37, f36, 8.0f, this.hair(), bl11 ? DropdownGpuClickGuiScreen.argb(144.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl12 ? 44.0f : 28.0f, 0xFFFFFF));
                canvas.textMid(f38 + (f37 - canvas.tw(string6, 9.0f, UiDrawCommand.TextWeight.SEMIBOLD)) * 0.5f, f39, f36, 9.0f, bl11 ? this.accent : this.text, string6, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.hit(f38, f39, f37, f36, () -> BindModeControl.toggle(module));
            }
        }
        if (this.decorOn && setting != HitboxConfigScreen752809.a) {
            this.rowDecor(canvas, setting);
        }
    }

    private void keyCap(Canvas canvas, float f2, float f3, float f4, float f5, String string, boolean bl) {
        boolean bl2 = canvas.hov(f2, f3, f4, f5);
        canvas.rect(f2, f3, f4, f5, 8.0f, DropdownGpuClickGuiScreen.argb(bl ? 36.0f : (bl2 ? 24.0f : 14.0f), 0xFFFFFF));
        canvas.border(f2, f3, f4, f5, 8.0f, this.hair(), bl ? DropdownGpuClickGuiScreen.argb(192.0f, this.accent) : DropdownGpuClickGuiScreen.argb(bl2 ? 44.0f : 28.0f, 0xFFFFFF));
        float f6 = canvas.tw(string, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.textMid(f2 + (f4 - f6) * 0.5f, f3, f5, 9.5f, bl ? this.accent : this.text, string, UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private void toggleSwitch(Canvas canvas, String string, float f2, float f3, boolean bl) {
        float f4 = this.ease(string, bl ? 1.0f : 0.0f, 18.0f);
        float f5 = 32.0f;
        float f6 = 18.0f;
        int n2 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ao, this.toggleOff) | 0xFF000000;
        int n3 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.an, this.accent) | 0xFF000000;
        canvas.rect(f2, f3, f5, f6, f6 * 0.5f, DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF), n2, 0.55f), n3, f4));
        canvas.border(f2, f3, f5, f6, f6 * 0.5f, this.hair(), DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(64.0f, 0xFFFFFF), f4));
        float f7 = f6 - 5.0f;
        float f8 = f2 + 2.5f + (f5 - f7 - 5.0f) * f4;
        float f9 = f3 + 2.5f;
        canvas.shadow(f8, f9 + 1.0f, f7, f7, f7 * 0.5f, 3.0f, 0.0f, 0x55000000);
        float f10 = ((float)(n3 >> 16 & 0xFF) * 0.299f + (float)(n3 >> 8 & 0xFF) * 0.587f + (float)(n3 & 0xFF) * 0.114f) / 255.0f;
        canvas.circle(f8 + f7 * 0.5f, f9 + f7 * 0.5f, f7 * 0.5f, DropdownGpuClickGuiScreen.mix(-526086, f10 > 0.62f ? -15460581 : -526086, f4));
    }

    private void drawTopBar(Canvas canvas, float f2, float f3) {
        float f4;
        int n2;
        float f5 = this.reveal(f2, 0.0f);
        canvas.alpha = f3 * f5;
        float f6 = 30.0f;
        float f7 = 20.0f;
        float f8 = 38.0f;
        float f9 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u69d1\u071f\u423a\u40cd\u6406\u669c", 1053740077, 0x229D082C ^ CryptoVault.mask()), 14.0f, UiDrawCommand.TextWeight.SEMIBOLD);
        float f10 = 46.0f + f9 + 18.0f;
        this.panel(canvas, f6, f7, f10, f8, f8 * 0.5f, 1.0f);
        canvas.icon(DropdownGpuClickGuiScreen.hj("\u014c\u8634\u6833\u1491\ueda9\u3ae6\u467c", -1070763808, 0x229D082C ^ CryptoVault.mask()), f6 + 14.0f, f7 + 8.0f, 22.0f, this.text);
        canvas.textMid(f6 + 14.0f + 22.0f + 10.0f, f7, f8, 14.0f, this.text, DropdownGpuClickGuiScreen.hj("\uf645\ucad1\u0a87\ufa68\u8085\ucb23\uf1e3", 453369380, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        boolean bl = PremiumGate.studio();
        ArrayList<Util> arrayList = new ArrayList<Util>();
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u980f\u6430\u4f56\u0cbf\u49e6", 1345314550, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u85f3\u2d0d\u1df0\u5f35\u0084\u5c52\u2441\u95d5\uc807\u132d\ufada", -1432231012, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\ucc2c\u0aae\u2bb3\u75ba\u30f6\ubd0d\u0dbf\u0ce8\u89d8\uc14e", 804150843, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\ue97f\u01b0\u14fe\u0b27\uba5e\u16b5\u2c67\uc761\ude77", 647258509, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u030a\u2d48\ucae5\ua240\ub222\u9527\u3d31\ub3d9\u7d29\uad26", -2026766596, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u55e5\u99e8\u2e85\ud222\uc711\u4c19\u4ebf\uef9f\u7a7e\ue6c7", 18605427, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\ue6e0\u0ae3\u1a33\u384a\u5cc4\u824d\u3a34\uaa35\u2ad2\u3ff9", -1699592655, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u716c\u2807\u46a0\uddf0\u54cc\u437d\ua0c3\u0d4a", -913038190, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        if (bl) {
            arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u584a\u4fc5\u7f00\u2886\u2cc1\u92cf\u47c7", -352098525, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ud905\ub90d\u7d2c\u1ec6\uce64\ufeb9\u17b0\u8daa\u54bd", 464067590, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\ube22\u8269\uae0b\ua9f8\u7116\uaa7f\u2219", -1987621870, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u7547\u7104\u1f61\ucd03\u623e\ue02c\u300b", -2085816916, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
            arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u3830\ue7e6\uf966\ub916\u09ba\u3533\ud4b3", -1809295406, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u4130\ue8f7\u94d7\ufb40\u4ae0\u091a\uae52\u6f5a\u8b53\u327a\ub4b5", 1562429435, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\u988a\uaf48\u78cd\u2a4d\u878f\ub243\u1d23\u7165", -1306767859, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\ue56e\u0951\ubfc9\u2016\u00e8\ucb5e\u5746\u1012", -1377322355, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        }
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u9292\u1aec\u54fb\u461b\u2384\u19c4\u1844\u7a64\ud6a9\u4723\u23ba", -1942858738, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\uacda\ucb88\u4275\uf417\ue41c\ud0c4\uc28b", 1577831348, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\uc22f\u50a9\ud5f0\ue6e5\ua514\u8265\ud008\u124a", 229038761, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u7e19\uf7b8\uc9dd\ua3cf\u72d4\u879d\u1129\u2949\uc31d\u9503", 149484683, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        if (this.cosmeticsUnlocked()) {
            arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u8d10\u66f1\u4992\uf947\u162d\u4b58\udb68\ufeeb\ufe96\u389c", -687809077, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u02ab\uc833\udfe8\u73c4\u1419\u9fd8\u26b0\u1f02\u4b11\u7d68\uf613\ubf5a", 214929355, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\u6be7\u2147\u4e88\ub215\u25ce\uf5c9\ue96b\ub7bf\u277b\ua910\udd95\u6d45\u54ae", -1434938251, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u1c04\ucca3\u952c\u0f85\ud970\ud36c\ude85\u109f\u406f\u4ade\u4bab\u2dde\uf50d", -1558979051, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        }
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u433f\u56e2\u788a\u02f9", -968628786, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u4213\uf8cd\u1abd\u54c4\u4efd\uf8d4\u4b8d\ub693\uce10\u75ba\u8316\u7538\u1ad9", 1985721731, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\u5575\u9af7\udaa5\u1ee0\u97f0\u56cd\ua982", -14356030, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u8e79\u783d\u01d5\u17dd\uf56c\u41bf\ub217\u3aa9\u3700", 620654450, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\ufd4d\u171f\u9f1e\u9f37\u9490\uafc4\uc00a", 2139861682, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ubf52\u2bd1\uc16a\u14a8\u832a\ub705\u93a2\uc88a\u4dd5\uc33f\ub21d", -2000198269, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\u1adb\u4e4c\uf29e\uc7e8\u6463\ua4b8\u74c5\u9992\ufdfa\u9a38\ua503\u0521", 447772809, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\uf83f\u1341\u5c5b\u3ae2\ud769\u986a\ue21d\u2616\u3c01\u78eb", -419511174, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\uda07\u26f1\uf6b3\u0add\u437b\u8470\uf05e\uf40e\u77c5", 1300281949, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u3d73\u437d\uac55\u43a8\u8ba4\ue040\u186d\u0703\u80a7\u324e\u3709\uc97c\u5a73\ub5c9\u079c", 415802441, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\uc8b0\u9848\u577c\u893f\u69e8\uf7dd\uf592\u8f87\u97e0\uc7e2\uf9d4\u75ca\ub1df\ua73a\u3d13", 941309723, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\ua548\u8489\ue7c9\udb8b\u0144\u9479\u9c19\u2e7b\u7667\ud56d\u5831\u49a4\ub5d1\u79f0\u8a76", 1970478428, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u3fc2\ue7fa\u094c\u9110\u4af4\u8839\u4344", -1204003990, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u4ca2\u5d7d\u36e4\u7328\u1aab\udb59\u8145\uf392\ue468\uef0c\u98f0\uc946\u8608\ub6c9\uaa57\ubf10\u4b0c\ud951", -740919443, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\udf57\u9aa1\u64ac\uf11e\u9a10\uf1a3\u39aa\u2143\ub5da\u671d", 1056613897, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\ua9ad\u833a\u68a1\u7f67\ubf00\u6f0b\u40f0\u9369", 614212698, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) || HitboxConfigScreen752809.ac()));
        arrayList.add(new Util(DropdownGpuClickGuiScreen.hj("\u5873\u774c\u6c82\u37f0\uc32b", -1686702271, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u75e0\u0565\u5f9a\u2faa\u2754\ue0a6\u0f32\ue955\u0c54\uf3aa\u2db7\u9747", 1537134029, 0x229D082C ^ CryptoVault.mask()), () -> this.openOverlay(DropdownGpuClickGuiScreen.hj("\ud058\u8831\u875b\u3591\u1842\u9a6e\u3fba\u49b1\u2714", -857175938, 0x229D082C ^ CryptoVault.mask())), DropdownGpuClickGuiScreen.hj("\u5725\u837c\ud469\u6416\ue5db\ueacc\u4b7c\u0162\u0e4c", -915267197, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)));
        String[] stringArray = new String[]{DropdownGpuClickGuiScreen.hj("\ua7af\ua4a6\ue774\u8b0c\udadc\uaf97\u7ef6\ubf5f\u08ba\u5973\u3d3c\uc3ba", 487999284, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ueafc\u3473\u4a67\ucfb3\u2cc7\uaf5b\u6564\u2bd1\u1579\u0c19\u6255\u6578", 1262354437, 0x229D082C ^ CryptoVault.mask())};
        float f11 = 15.0f;
        float f12 = 5.0f;
        float f13 = 4.0f;
        float[] fArray = new float[stringArray.length];
        float f14 = f12 * 2.0f + f13 * (float)(stringArray.length - 1);
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            fArray[i2] = canvas.tw(stringArray[i2], 10.5f, UiDrawCommand.TextWeight.SEMIBOLD) + f11 * 2.0f;
            f14 += fArray[i2];
        }
        float f15 = this.viewW - 30.0f - f14;
        canvas.alpha = f3 * this.reveal(f2, 0.04f);
        this.panel(canvas, f15, f7, f14, f8, f8 * 0.5f, 1.0f);
        float f16 = f8 - f12 * 2.0f;
        float f17 = f15 + f12;
        for (int i3 = 0; i3 < stringArray.length; ++i3) {
            int n3 = i3;
            boolean bl2 = i3 == 0 && DropdownGpuClickGuiScreen.hj("\ub831\ub81d\u74ce\u3a61\u1aab\u2c43\u27c5\u7bd8\uba83\ud0f7\u280d\u1cfe", -1471642004, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView);
            n2 = canvas.hov(f17, f7 + f12, fArray[i3], f16);
            float f18 = this.ease(DropdownGpuClickGuiScreen.hj("\ua856\u59d8\ud32c\u2319\u9bd2\u7936\ub65f", 1872107074, 0x229D082C ^ CryptoVault.mask()) + i3, bl2 || n2 != 0 ? 1.0f : 0.0f, 16.0f);
            canvas.rect(f17, f7 + f12, fArray[i3], f16, f16 * 0.5f, DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.argb(0.0f, 0xFFFFFF), bl2 ? DropdownGpuClickGuiScreen.argb(48.0f, this.accent) : DropdownGpuClickGuiScreen.argb(24.0f, 0xFFFFFF), f18));
            if (bl2) {
                canvas.gradient(f17, f7 + f12, fArray[i3], f16, f16 * 0.5f, DropdownGpuClickGuiScreen.argb(80.0f, this.accent), DropdownGpuClickGuiScreen.argb(32.0f, this.accent), DropdownGpuClickGuiScreen.argb(16.0f, this.accent), DropdownGpuClickGuiScreen.argb(48.0f, this.accent));
                canvas.shadow(f17, f7 + f12 + 2.0f, fArray[i3], f16, f16 * 0.5f, 10.0f, 0.0f, DropdownGpuClickGuiScreen.argb(64.0f, this.accent));
                canvas.border(f17, f7 + f12, fArray[i3], f16, f16 * 0.5f, this.hair(), DropdownGpuClickGuiScreen.argb(176.0f, this.accent));
            }
            f4 = canvas.tw(stringArray[i3], 10.5f, UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.textMid(f17 + (fArray[i3] - f4) * 0.5f, f7 + f12, f16, 10.5f, bl2 ? this.accent : DropdownGpuClickGuiScreen.mix(this.muted, this.text, f18), stringArray[i3], UiDrawCommand.TextWeight.SEMIBOLD);
            canvas.hit(f17, f7 + f12, fArray[i3], f16, () -> {
                if (n3 == 0) {
                    this.openOverlay(DropdownGpuClickGuiScreen.hj("\u9908\u71bc\uba8e\ud67c\u8026\ua716\u451a\u236b\uff1e\ubbf9\u0981", 217935919, 0x229D082C ^ CryptoVault.mask()));
                } else if (this.field_22787 != null && this.field_22787.field_1687 == null) {
                    ClickGuiRouter.toast(DropdownGpuClickGuiScreen.hj("\u8a4b\u7c03\u4338\u751d\u87b0\u8424\uf2c1\ucf17\u3794\u8a3e\u1567", 2107347112, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\ud660\u5c0f\ua8ee\uc5d5\u36c9\ucce4\u5994\ub725\ud7ef\u8679\u6472\u047d\u88ab\ucc63\uf4c3\ud6c3\u39d3\u8f50\u34ae\ub6f2\u02cc\u07e3\uebcf\u6d88\ucf69\u9b1e\u4739\uddec\u28fc\u58b3\ud702", 538717928, 0x229D082C ^ CryptoVault.mask()));
                } else if (this.field_22787 != null) {
                    McCompat.setScreen(this.field_22787, new HudEditorScreen(this));
                }
            });
            this.tourMark(i3 == 0 ? OnboardingTour.Target.TAB_CLICKGUI : OnboardingTour.Target.TAB_HUD, f17, f7 + f12, fArray[i3], f16);
            f17 += fArray[i3] + f13;
        }
        float f19 = 38.0f;
        float f20 = 9.0f;
        float f21 = f15 - 16.0f - ((float)arrayList.size() * f19 + (float)(arrayList.size() - 1) * f20);
        this.tourMark(OnboardingTour.Target.BAR_UTILS, f21 - 4.0f, f7 - 4.0f, (float)arrayList.size() * f19 + (float)(arrayList.size() - 1) * f20 + 8.0f, f19 + 8.0f);
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            Util util = (Util)arrayList.get(n2);
            f4 = f21 + (float)n2 * (f19 + f20);
            this.tourMark(DropdownGpuClickGuiScreen.tourTargetFor(util.label()), f4 - 3.0f, f7 - 3.0f, f19 + 6.0f, f19 + 6.0f);
            canvas.alpha = f3 * this.reveal(f2, 0.05f + (float)n2 * 0.025f);
            boolean bl3 = canvas.hov(f4, f7, f19, f19);
            float f22 = this.ease(DropdownGpuClickGuiScreen.hj("\uc797\u2236\u29f5\uf40e\u5896\u7215\u20f9\ub5fe", -1318308933, 0x229D082C ^ CryptoVault.mask()) + n2, util.active() || bl3 ? 1.0f : 0.0f, 16.0f);
            if (util.active()) {
                canvas.shadow(f4, f7, f19, f19, f19 * 0.5f, 22.0f, 2.0f, DropdownGpuClickGuiScreen.argb(80.0f, this.accent));
            }
            this.glassDisc(canvas, f4 + f19 * 0.5f, f7 + f19 * 0.5f, f19 * 0.5f);
            if (util.active()) {
                canvas.border(f4 - 2.5f, f7 - 2.5f, f19 + 5.0f, f19 + 5.0f, f19 * 0.5f + 2.5f, this.hair(), DropdownGpuClickGuiScreen.argb(192.0f, this.accent));
            }
            canvas.icon(util.icon(), f4 + 10.5f, f7 + 10.5f, 17.0f, DropdownGpuClickGuiScreen.mix(this.muted, util.active() ? this.accent : this.text, f22));
            canvas.hit(f4, f7, f19, f19, util.action());
            if (!bl3) continue;
            float f23 = canvas.tw(util.label(), 10.0f, UiDrawCommand.TextWeight.SEMIBOLD) + 22.0f;
            float f24 = Math.min(f4 + f19 * 0.5f - f23 * 0.5f, this.viewW - 30.0f - f23 + 6.0f);
            float f25 = f7 + f19 + 8.0f;
            this.panel(canvas, f24, f25, f23, 24.0f, 12.0f, 0.3f);
            canvas.textMid(f24 + 11.0f, f25, 24.0f, 10.0f, this.text, util.label(), UiDrawCommand.TextWeight.SEMIBOLD);
        }
        canvas.alpha = 1.0f;
    }

    private void stripChip(Canvas canvas, float f2, float f3, float f4, float f5, String string, boolean bl, boolean bl2, String string2, String string3, Runnable runnable) {
        boolean bl3 = canvas.hov(f2, f3, f4, f5);
        this.panel(canvas, f2, f3, f4, f5, f5 * 0.5f, 0.35f);
        float f6 = this.ease(DropdownGpuClickGuiScreen.hj("\u2fc2\uc068\u0ddc\u3e89\u8700\ua143\u8612\u3390\uadc4", -1132691118, 0x229D082C ^ CryptoVault.mask()) + string, bl || bl3 ? 1.0f : 0.0f, 16.0f);
        if (bl) {
            canvas.gradient(f2, f3, f4, f5, f5 * 0.5f, DropdownGpuClickGuiScreen.argb(80.0f, this.accent), DropdownGpuClickGuiScreen.argb(32.0f, this.accent), DropdownGpuClickGuiScreen.argb(16.0f, this.accent), DropdownGpuClickGuiScreen.argb(48.0f, this.accent));
            canvas.border(f2, f3, f4, f5, f5 * 0.5f, this.hair(), DropdownGpuClickGuiScreen.argb(176.0f, this.accent));
        } else if (bl3) {
            canvas.rect(f2, f3, f4, f5, f5 * 0.5f, DropdownGpuClickGuiScreen.argb(20.0f, 0xFFFFFF));
        }
        float f7 = canvas.tw(string, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.textMid(f2 + (f4 - f7) * 0.5f, f3, f5, 9.5f, bl ? this.accent : DropdownGpuClickGuiScreen.mix(this.muted, this.text, f6), string, UiDrawCommand.TextWeight.SEMIBOLD);
        if (bl2) {
            canvas.circle(f2 + f4 - 7.0f, f3 + 7.0f, 3.0f, this.accent);
        }
        if (bl3 && string3 != null) {
            this.tipTitle = string2;
            this.tipDesc = string3;
        }
        canvas.hit(f2, f3, f4, f5, runnable);
    }

    private void drawToolStrip(Canvas canvas, float f2, float f3) {
        String string;
        float f4;
        String string2;
        float f5;
        float f6;
        canvas.alpha = f3 * this.reveal(f2, 0.03f);
        float f7 = 66.0f;
        float f8 = 28.0f;
        float f9 = 30.0f;
        String string3 = DropdownGpuClickGuiScreen.hj("\u5d4a\u2a27\ue4ce\u9810\udd5a\uf681\uf0a6\u0217\u725f\ucef3\u5d86\ubd9a", 1298404413, 0x229D082C ^ CryptoVault.mask());
        float f10 = canvas.tw(string3, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f;
        float f11 = this.viewW - 30.0f - f10;
        float f12 = f11 - 10.0f;
        float f13 = DropdownGpuClickGuiScreen.clamp(this.viewW * 0.22f, 160.0f, 260.0f);
        boolean bl = this.searchField.focused(this.host);
        this.panel(canvas, f9, f7, f13, f8, f8 * 0.5f, 0.35f);
        if (bl) {
            canvas.border(f9, f7, f13, f8, f8 * 0.5f, this.hair(), DropdownGpuClickGuiScreen.argb(176.0f, this.accent));
        }
        canvas.icon(DropdownGpuClickGuiScreen.hj("\ufe74\ueb92\u1758\ue407\u147d\u1e57\uefe5", 6823486, 0x229D082C ^ CryptoVault.mask()), f9 + 11.0f, f7 + (f8 - 13.0f) * 0.5f, 13.0f, bl ? this.accent : this.muted);
        this.searchField.draw(canvas.b, this.host, f9 * this.s, f7 * this.s, f13 * this.s, f8 * this.s, this.s);
        if (!this.searchField.isEmpty()) {
            float f14;
            float f15;
            boolean bl2 = canvas.hov(f15 = f9 + f13 - (f6 = 18.0f) - 6.0f, f14 = f7 + (f8 - f6) * 0.5f, f6, f6);
            canvas.circle(f15 + f6 * 0.5f, f14 + f6 * 0.5f, f6 * 0.5f, DropdownGpuClickGuiScreen.argb(bl2 ? 42.0f : 20.0f, 0xFFFFFF));
            canvas.icon(DropdownGpuClickGuiScreen.hj("\ue316\u7b79\ueafb\u5e9a\u5d84\u6d6a\ud165\uf182", -1406153265, 0x229D082C ^ CryptoVault.mask()), f15 + 5.0f, f14 + 5.0f, 8.0f, bl2 ? this.text : this.muted);
            canvas.hit(f15, f14, f6, f6, () -> {
                this.searchField.clear();
                this.host.focus(this.searchField);
            });
        }
        f6 = f9 + f13;
        f9 += f13 + 10.0f;
        GpuClickGuiScreen.ModuleFilter[] moduleFilterArray = GpuClickGuiScreen.ModuleFilter.values();
        float[] fArray = new float[moduleFilterArray.length];
        float f16 = 10.0f + 3.0f * (float)(moduleFilterArray.length - 1);
        for (int i2 = 0; i2 < moduleFilterArray.length; ++i2) {
            fArray[i2] = canvas.tw(moduleFilterArray[i2].label, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 22.0f;
            f16 += fArray[i2];
        }
        if (f9 + f16 <= f12) {
            this.panel(canvas, f9, f7, f16, f8, f8 * 0.5f, 0.35f);
            float f17 = f9 + 5.0f;
            f5 = f8 - 10.0f;
            for (int i3 = 0; i3 < moduleFilterArray.length; ++i3) {
                GpuClickGuiScreen.ModuleFilter moduleFilter = moduleFilterArray[i3];
                boolean bl3 = DropdownGpuClickGuiScreen.moduleFilter == moduleFilter;
                boolean bl4 = canvas.hov(f17, f7 + 5.0f, fArray[i3], f5);
                float f18 = this.ease(DropdownGpuClickGuiScreen.hj("\ue22b\ue6cc\u62be\uac04\u6346\uaa97\u12eb\ue135", -2112176396, 0x229D082C ^ CryptoVault.mask()) + i3, bl3 || bl4 ? 1.0f : 0.0f, 16.0f);
                if (bl3) {
                    canvas.gradient(f17, f7 + 5.0f, fArray[i3], f5, f5 * 0.5f, DropdownGpuClickGuiScreen.argb(80.0f, this.accent), DropdownGpuClickGuiScreen.argb(32.0f, this.accent), DropdownGpuClickGuiScreen.argb(16.0f, this.accent), DropdownGpuClickGuiScreen.argb(48.0f, this.accent));
                    canvas.border(f17, f7 + 5.0f, fArray[i3], f5, f5 * 0.5f, this.hair(), DropdownGpuClickGuiScreen.argb(176.0f, this.accent));
                } else if (bl4) {
                    canvas.rect(f17, f7 + 5.0f, fArray[i3], f5, f5 * 0.5f, DropdownGpuClickGuiScreen.argb(20.0f, 0xFFFFFF));
                }
                float f19 = canvas.tw(moduleFilter.label, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.textMid(f17 + (fArray[i3] - f19) * 0.5f, f7 + 5.0f, f5, 9.5f, bl3 ? this.accent : DropdownGpuClickGuiScreen.mix(this.muted, this.text, f18), moduleFilter.label, UiDrawCommand.TextWeight.SEMIBOLD);
                canvas.hit(f17, f7 + 5.0f, fArray[i3], f5, () -> DropdownGpuClickGuiScreen.setModuleFilter(moduleFilter));
                f17 += fArray[i3] + 3.0f;
            }
            f6 = f9 + f16;
            f9 += f16 + 10.0f;
        }
        if (f9 + (f5 = canvas.tw(string2 = DropdownGpuClickGuiScreen.hj("\u2386\u533b\u6a91\u25b9\ua549\u85b4\u4cd4", -2092820137, 0x229D082C ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.moduleSort.label, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f) <= f12) {
            this.stripChip(canvas, f9, f7, f5, f8, string2, false, false, DropdownGpuClickGuiScreen.hj("\u5d8d\u0807\uc84d\ub6cf\uacdc\u30eb\u0237", 1874360802, 0x229D082C ^ CryptoVault.mask()), moduleSort == GpuClickGuiScreen.ModuleSort.DEFAULT ? DropdownGpuClickGuiScreen.hj("\u337e\u9289\uae26\u2636\u2d4b\ub777\u17c6\u0b30\u3a8d\u6c2b\u7f8d\u393a\ub22c\u42e2\u8452\uee53\u0b45\ub8f1\u8c00\uda2a\u25e7\u72fd\udd88\u24b5\u6a3b\uafe2\u462a\u6b92\u79cc\u90ee\ud977\u839b\u3a95\u1a34\u7169\uef24\u16d9\u52c2\uae2c\uec61\u4dc3\u0c33\u3e44\u27b8\uc8ee\u3581\u909a\udaf9\u8a18\uc161\ucbcd\ufbd0\u8e22\u66c7", -1753368944, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u9e59\ub7c9\u2c9a\ue681\uec6e\u9f60\u9617\u3272\u9414\uebe8\uf83e\u9ecb\u9b4a\u3096\ufd16\u455e\uc4b1\u3f23\uddb6\uc15b\u4bce\u702f\u0be2\u78cd\u52cd\u1e35\u1126\ud08c\u66a0\u55e8\ue113\u1afc\ub117\u91eb\u801b\u71a1\u7214\u2d97\u34cc\u9e6c\u65f8\uddbf\u6026\ub613\u6653\u4f2d\u0bea\u2cc1\u62d9\u6fd1\u3d74\uc165\ue278\ud239\ua1de\ue58b\u84e2\u5980\u97e7\u0e47\u47f8", -1131207496, 0x229D082C ^ CryptoVault.mask()), GpuClickGuiScreen::cycleModuleSort);
            f6 = f9 + f5;
            f9 += f5 + 10.0f;
        }
        if (DropdownGpuClickGuiScreen.guiPrefs().canCreateCategory() && f9 + (f4 = canvas.tw(string = DropdownGpuClickGuiScreen.hj("\ub9c2\u5c00\u398b\u726f\u4f72\u898f\u344b\ue302\u228a\uf341\u971a\u1aa7", -1495332762, 0x229D082C ^ CryptoVault.mask()), 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f) <= f12) {
            this.stripChip(canvas, f9, f7, f4, f8, string, false, false, DropdownGpuClickGuiScreen.hj("\u9c28\u9796\ua771\u804d\u1ea8\u556f\ue4b1\u06ce\u0754\u85b6\u6e17\u892c\u5009", 1604782171, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u41ce\u5501\ufbb7\u3ce4\u7798\udac8\u7259\u1e18\u3fb1\u9719\u8a98\ue04f\u5860\ucf8c\ub3da\u7ab4\u901e\uac3a\u9e2c\uecca\u3186\u576c\u1e18\ue7d2\u7fc8\u6d05\ud284\u88bc\u14a9\uad36\u59fa\u2d87\uda4e\u205c\u3457\ub9e6\u565f\u5fd1\ub09a\u4ff2\u8bd3\uc5c5\uf412\u2935\uca85\u30a2\ud1a0\u5040\u600c\ua852\ue607\u74d9\uda81\u8a89\u261f\u078c\ue7a3\u8a5e\uaba6\u490b\u2fdd\u2021\ub347\u9aec\uf6be\uf8c1\u1833\u8e26\u64ef\u5b58\u21f4\u86d1\uebde\u744a\u74dd\ufb9b\u3c76\u116b\u454e\uf097\ub645\u3f79\u1d78\u8a68\u9443\ub369\u580b\u58f3\u60b5", 2064808217, 0x229D082C ^ CryptoVault.mask()), this::createFolder);
            f6 = f9 + f4;
        }
        this.stripChip(canvas, f11, f7, f10, f8, string3, false, WhatsNewPanel.hasUnread(), null, null, this.whatsNew::open);
        this.tourMark(OnboardingTour.Target.TOOLSTRIP, 26.0f, f7 - 3.0f, f6 - 30.0f + 8.0f, f8 + 6.0f);
        canvas.alpha = 1.0f;
    }

    private void openPicker(ColorSetting colorSetting) {
        if (this.colorPicker != null && this.colorPicker != colorSetting) {
            this.closePicker();
        }
        this.stopEditing();
        this.cancelCapture();
        this.colorPicker = colorSetting;
        this.pickerOpenedWith = colorSetting.B().getRGB();
        this.settingPicker.reset();
    }

    private void closePicker() {
        this.colorPicker = null;
        this.colorDrag = GpuClickGuiScreen.ColorDrag.NONE;
        this.settingPicker.release();
        if (this.kitFocus != null) {
            UiTextTarget uiTextTarget = this.kitFocus;
            this.kitFocus = null;
            uiTextTarget.onBlur();
        }
    }

    private void pickerDrag(double d2, double d3) {
        if (this.colorDrag != GpuClickGuiScreen.ColorDrag.RAINBOW_SPEED || this.pkSpeedW <= 0.0f || this.colorPicker == null) {
            return;
        }
        double d4 = DropdownGpuClickGuiScreen.clamp((float)((d2 - (double)this.pkSpeedX) / (double)this.pkSpeedW), 0.0f, 1.0f);
        this.colorPicker.A(0.1 + d4 * 4.9);
    }

    private void drawColorPicker(Canvas canvas, float f2) {
        ColorSetting colorSetting = this.colorPicker;
        if (colorSetting == null) {
            return;
        }
        canvas.alpha = f2;
        float f3 = 440.0f;
        float f4 = 336.0f;
        float f5 = Math.max(6.0f, (this.viewW - f3) * 0.5f);
        float f6 = Math.max(6.0f, (this.viewH - f4) * 0.5f);
        this.hits.add(new Hit(0.0f, 0.0f, this.sw, this.sh, this::closePicker));
        this.hits.add(new Hit(f5 * this.s, f6 * this.s, f3 * this.s, f4 * this.s, () -> {}));
        canvas.b.rect(0.0f, 0.0f, this.sw, this.sh, 0.0f, 0x66000000);
        this.panel(canvas, f5, f6, f3, f4, 16.0f, 1.0f);
        canvas.textMid(f5 + 18.0f, f6 + 8.0f, 30.0f, 13.0f, this.text, this.fit(canvas, colorSetting.getName(), 13.0f, f3 - 80.0f, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        float f7 = 24.0f;
        float f8 = f5 + f3 - f7 - 14.0f;
        float f9 = f6 + 11.0f;
        boolean bl = canvas.hov(f8, f9, f7, f7);
        canvas.circle(f8 + f7 * 0.5f, f9 + f7 * 0.5f, f7 * 0.5f, DropdownGpuClickGuiScreen.argb(bl ? 34.0f : 14.0f, 0xFFFFFF));
        canvas.icon(DropdownGpuClickGuiScreen.hj("\uacd9\u08c7\ud1aa\u57de\u944d\ub1fc\u31af\ua90a\u759f", -1458192254, 0x229D082C ^ CryptoVault.mask()), f8 + 6.0f, f9 + 6.0f, 12.0f, bl ? this.text : this.muted);
        canvas.hit(f8, f9, f7, f7, this::closePicker);
        float f10 = f5 + 18.0f;
        float f11 = f6 + 46.0f;
        float f12 = 250.0f;
        this.settingPicker.draw(canvas.b, this.host, f10 * this.s, f11 * this.s, f12 * this.s, this.s, this.mx, this.my, colorSetting.B().getRGB(), true, this::applyPicked);
        float f13 = f10 + f12 + 18.0f;
        float f14 = f5 + f3 - 18.0f - f13;
        float f15 = 30.0f;
        float f16 = Math.round(f14 * 0.5f);
        canvas.rect(f13, f11, f16, f15, 0.0f, this.pickerOpenedWith | 0xFF000000);
        canvas.rect(f13 + f16, f11, f14 - f16, f15, 0.0f, colorSetting.A().getRGB() | 0xFF000000);
        canvas.border(f13, f11, f14, f15, 6.0f, this.hair(), DropdownGpuClickGuiScreen.argb(51.0f, 0xFFFFFF));
        float f17 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u2510\ub7a7\u3431\u5dd4\u2157\uffdb\u112a\ud55b\u6e5f", -2145586125, 0x229D082C ^ CryptoVault.mask()), 8.0f, UiDrawCommand.TextWeight.REGULAR);
        float f18 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u55b4\u26a0\u20f2\u7689\u59b2", 375378191, 0x229D082C ^ CryptoVault.mask()), 8.0f, UiDrawCommand.TextWeight.REGULAR);
        canvas.text(f13 + (f16 - f17) * 0.5f, f11 + f15 + 3.0f, 8.0f, this.muted, DropdownGpuClickGuiScreen.hj("\u9ef8\u7f50\u7dcc\uafa5\uc2f1\u5fe4\ub86a\ub706", -1365206400, 0x229D082C ^ CryptoVault.mask()));
        canvas.text(f13 + f16 + (f14 - f16 - f18) * 0.5f, f11 + f15 + 3.0f, 8.0f, this.muted, DropdownGpuClickGuiScreen.hj("\u7637\u8e94\uf2bb\u98d0\u6951\u85d6", -891720376, 0x229D082C ^ CryptoVault.mask()));
        float f19 = 22.0f;
        float f20 = Math.round((f14 - 6.0f) * 0.5f);
        float f21 = f11 + f15 + 18.0f;
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\u5da6\u4430\uff89\u38ad\u70e3\ud916\ufe08", 231941941, 0x229D082C ^ CryptoVault.mask()), f13 * this.s, f21 * this.s, f14 * this.s, f19 * this.s, this.s * 0.9f, UiKit.Style.SECONDARY, colorSetting.B().getRGB() != this.pickerOpenedWith, this.mx, this.my, () -> this.applyPicked(this.pickerOpenedWith));
        float f22 = f21 + f19 + 6.0f;
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\uab80\u549d\u42e4\uef25\ufa68\ua49c\ucbd1\ucb99\u4267\uce18\u5d04\u121f\u9a82", -533851633, 0x229D082C ^ CryptoVault.mask()), f13 * this.s, f22 * this.s, f14 * this.s, f19 * this.s, this.s * 0.9f, UiKit.Style.SECONDARY, true, this.mx, this.my, () -> this.applyPicked(colorSetting.B().getRGB() & 0xFF000000 | this.accent & 0xFFFFFF));
        float f23 = f22 + f19 + 6.0f;
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\u7be6\u8aac\u0a33\u5ede\ufe5a\u610c\uf93c", 874329183, 0x229D082C ^ CryptoVault.mask()), f13 * this.s, f23 * this.s, f20 * this.s, f19 * this.s, this.s * 0.9f, UiKit.Style.SECONDARY, true, this.mx, this.my, () -> this.host.setClipboard(DropdownGpuClickGuiScreen.hexOf(colorSetting.B().getRGB(), colorSetting.B().getAlpha() != 255)));
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\u40a6\ua696\u69e1\u0976\u8393\u3218\ue052\u2b5b\u91d1", 1210366176, 0x229D082C ^ CryptoVault.mask()), (f13 + f14 - f20) * this.s, f23 * this.s, f20 * this.s, f19 * this.s, this.s * 0.9f, UiKit.Style.SECONDARY, true, this.mx, this.my, () -> {
            Integer n2 = DropdownGpuClickGuiScreen.parseHex(this.host.clipboard());
            if (n2 != null) {
                this.applyPicked(n2);
            }
        });
        float f24 = f23 + f19 + 10.0f;
        float f25 = 22.0f;
        canvas.textMid(f13, f24, f25, 10.0f, this.text, DropdownGpuClickGuiScreen.hj("\u9aed\ubd4e\u1671\u367e\ubb7a\u84b8\u83f2\u77bd\u3073\u9bd7\u6085", -365939452, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        this.toggleSwitch(canvas, DropdownGpuClickGuiScreen.hj("\u7acd\ua35d\uc433\u28b5\u80b4\ua4a9\ubb5a\u7405\ube00\ue9bc\ufbc5\u2f6b\u41c2\u1b9f\uee58\ua09f\u906e\ucb09", -1013327125, 0x229D082C ^ CryptoVault.mask()), f13 + f14 - 32.0f, f24 + (f25 - 18.0f) * 0.5f, colorSetting.v());
        canvas.hit(f13, f24, f14, f25, () -> colorSetting.K(!colorSetting.v()));
        if (colorSetting.v()) {
            float f26 = f24 + f25 + 4.0f;
            canvas.text(f13, f26, 9.0f, this.muted, String.format(Locale.ROOT, DropdownGpuClickGuiScreen.hj("\u8e88\u11ab\u2189\u89e6\ucf76\u4d4d\u2688\ub97a\u18d3\uc588\u738b\u4a52\u96cd", 1419952130, 0x229D082C ^ CryptoVault.mask()), colorSetting.A()));
            float f27 = f26 + 18.0f;
            float f28 = 4.0f;
            this.pkSpeedX = f13;
            this.pkSpeedW = f14;
            double d2 = (colorSetting.A() - 0.1) / 4.9;
            float f29 = (float)((double)f14 * Math.max(0.0, Math.min(1.0, d2)));
            canvas.rect(f13, f27, f14, f28, f28 * 0.5f, DropdownGpuClickGuiScreen.argb(28.0f, 0xFFFFFF));
            if (f29 > 0.5f) {
                canvas.rect(f13, f27, Math.max(f28, f29), f28, f28 * 0.5f, this.accent);
            }
            canvas.circle(f13 + f29, f27 + f28 * 0.5f, 5.0f, -526086);
            canvas.hit(f13 - 4.0f, f27 - 8.0f, f14 + 8.0f, f28 + 16.0f, () -> {
                this.colorDrag = GpuClickGuiScreen.ColorDrag.RAINBOW_SPEED;
                this.pickerDrag(this.pressX / (double)this.s, this.pressY / (double)this.s);
            });
        }
        canvas.alpha = 1.0f;
    }

    private void drawTooltip(Canvas canvas, float f2) {
        canvas.alpha = f2;
        float f3 = 9.5f;
        float f4 = 11.0f;
        float f5 = 240.0f;
        boolean bl = this.tipTitle != null && !this.tipTitle.isBlank();
        List<String> list = this.wrap(canvas, this.tipDesc, f3, f5 - f4 * 2.0f, 4);
        if (list.isEmpty()) {
            canvas.alpha = 1.0f;
            return;
        }
        String string = bl ? this.fit(canvas, this.tipTitle, 10.0f, f5 - f4 * 2.0f, UiDrawCommand.TextWeight.SEMIBOLD) : "";
        float f6 = bl ? canvas.tw(string, 10.0f, UiDrawCommand.TextWeight.SEMIBOLD) : 0.0f;
        for (String string2 : list) {
            f6 = Math.max(f6, canvas.tw(string2, f3, UiDrawCommand.TextWeight.REGULAR));
        }
        f6 = Math.min(f5, f6 + f4 * 2.0f);
        float f7 = f4 * 2.0f - 2.0f + (bl ? 16.0f : 0.0f) + (float)list.size() * 13.0f;
        float f8 = (float)this.mx / this.s + 16.0f;
        float f9 = (float)this.my / this.s + 18.0f;
        if (f8 + f6 > this.viewW - 12.0f) {
            f8 = (float)this.mx / this.s - f6 - 14.0f;
        }
        if (f9 + f7 > this.viewH - 12.0f) {
            f9 = (float)this.my / this.s - f7 - 14.0f;
        }
        f8 = DropdownGpuClickGuiScreen.clamp(f8, 6.0f, Math.max(6.0f, this.viewW - f6 - 6.0f));
        f9 = DropdownGpuClickGuiScreen.clamp(f9, 6.0f, Math.max(6.0f, this.viewH - f7 - 6.0f));
        this.panel(canvas, f8, f9, f6, f7, 12.0f, 0.4f);
        float f10 = f9 + f4 - 1.0f;
        if (bl) {
            canvas.text(f8 + f4, f10, 10.0f, this.text, string, UiDrawCommand.TextWeight.SEMIBOLD);
            f10 += 16.0f;
        }
        for (String string3 : list) {
            canvas.text(f8 + f4, f10, f3, this.muted, string3);
            f10 += 13.0f;
        }
        canvas.alpha = 1.0f;
    }

    private void openOverlay(String string) {
        this.overlayView = string;
        this.kitFocus = null;
        this.kitKeyCapture = null;
        this.pressY = 0.0;
        this.pressX = 0.0;
        this.overlayScroll = 0.0f;
        this.ddArmed = false;
        this.bindingConfigName = null;
        this.stopEditing();
        this.cancelCapture();
        if (DropdownGpuClickGuiScreen.hj("\uec67\u5d0b\ue93b\u7410\u45a8\u24d8\uc625\ua62f\uffdf", -49938914, 0x229D082C ^ CryptoVault.mask()).equals(string)) {
            this.refreshSavedConfigs();
        }
        if (DropdownGpuClickGuiScreen.hj("\u00e3\u5d5f\u434f\u4710\u24bc\u3ccc\u73cd\uee09\ue791", 1973638366, 0x229D082C ^ CryptoVault.mask()).equals(string)) {
            this.keybindsView.reset();
        }
        if (DropdownGpuClickGuiScreen.hj("\ua2e6\ua916s\u2bb2\ue5f3\u3aea\u18eb\u9d1c\u8eb9", 1755986452, 0x229D082C ^ CryptoVault.mask()).equals(string)) {
            this.macroView.refresh();
        } else if (DropdownGpuClickGuiScreen.hj("\ud89b\u4847\ue2ec\u380e\u1197\u4efb\u2bce\u6d85\ub5fb\u849c\u8d4d", -254999167, 0x229D082C ^ CryptoVault.mask()).equals(string)) {
            this.scriptView.refresh();
        } else if (DropdownGpuClickGuiScreen.hj("\ud99c\u9a43\u370d\uf610\uf183\u9ce0\u7d80\ue67c\u4291", -499352987, 0x229D082C ^ CryptoVault.mask()).equals(string)) {
            this.refreshSavedConfigs();
            this.configsView.refresh();
        }
    }

    private void closeOverlay() {
        if (DropdownGpuClickGuiScreen.hj("\u2856\u64ca\u3cff\uecec\ub0b3\u766d\uf7f5\u06ff\ufe4c\uc589\ud25d", 1274379260, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)) {
            this.scriptView.flush();
        }
        this.stopEditing();
        this.cancelCapture();
        this.bindingConfigName = null;
        this.overlayView = null;
        this.overlayModule = null;
        this.kitFocus = null;
        this.kitKeyCapture = null;
        this.hostClip = false;
    }

    /*
     * Exception decompiling
     */
    private void drawOverlay(Canvas var1_1, float var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private boolean rowsOverlay() {
        return this.settingsOverlay() || DropdownGpuClickGuiScreen.hj("\uba47\uef33\ue164\u8e6b\u027a\u82c1\u2199\u07eb\u2ec1", 1781624446, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) || DropdownGpuClickGuiScreen.hj("\u9cdb\uae99\uc45d\u3584\ud929\u94d0\ue09d\ufd49\u7702\ufe63\ub8f1", -972405905, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView);
    }

    private void drawThemeMakerEntry(Canvas canvas, float f2, float f3, float f4, float f5) {
        canvas.rect(f2, f3, f4, f5, 10.0f, DropdownGpuClickGuiScreen.argb(48.0f, 0));
        canvas.border(f2, f3, f4, f5, 10.0f, this.hair(), DropdownGpuClickGuiScreen.argb(24.0f, 0xFFFFFF));
        canvas.text(f2 + 12.0f, f3 + 8.0f, 10.5f, this.text, DropdownGpuClickGuiScreen.hj("\u6db5\u0a02\u3499\u6ce7\u53ee\u9fc3\u3425\u2364\u90c6\u4206\u5c9a\u234e\ub239\u7219", -975016828, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f6 = UiKit.buttonWidth(DropdownGpuClickGuiScreen.hj("\uf5ee\ub1c8\ud2ae\u6161\u40e5\u45ae\uec17\uc843\u228b\u93e1\u0962\u35fb\uad36\u7317\u6fb9\u8ba4\u2bed\u75ad\u4539", 2072287682, 0x229D082C ^ CryptoVault.mask()), this.s) / this.s;
        float f7 = 26.0f;
        canvas.text(f2 + 12.0f, f3 + 23.0f, 9.0f, this.muted, this.fit(canvas, DropdownGpuClickGuiScreen.hj("\ub3bd\u8bac\ue431\u4548\u7c8c\u41ed\udc74\u6295\u14f6\ufb41\u8cd2\ud558\ue682\ua8c3\u11ab\u106f\u43a8\u18c6\u9064\u5651\u2ba8\u8759\u00af\uede8\u4ffa\ua8dd\udf53\ua7c6\u19c9\u5b3f\uab9a\ue786\u3e6c\ucaee\uaae5\u8cf7\ua256\u28df\u2a50", -113097806, 0x229D082C ^ CryptoVault.mask()), 9.0f, Math.max(0.0f, f4 - f6 - 36.0f), UiDrawCommand.TextWeight.REGULAR), UiDrawCommand.TextWeight.REGULAR);
        float[] fArray = canvas.clip();
        if (fArray != null) {
            this.host.beginHitClip(fArray[0], fArray[1], fArray[2], fArray[3]);
        }
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\ue1f1\u5177\ua3c5\ufafa\uee31\u646a\u7e45\u445c\u2097\uc4b7\u6705\uc2d7\u4302\u1982\u6b62\uec59\ud42a", -1555193089, 0x229D082C ^ CryptoVault.mask()), (f2 + f4 - f6 - 10.0f) * this.s, (f3 + (f5 - f7) * 0.5f) * this.s, f6 * this.s, f7 * this.s, this.s, UiKit.Style.PRIMARY, true, this.mx, this.my, this::openThemeMaker);
        if (fArray != null) {
            this.host.endHitClip();
        }
    }

    private void openThemeMaker() {
        this.openOverlay(DropdownGpuClickGuiScreen.hj("\u29ba\u5a64\u11de\u0470\u0e5e\uaf26\u6d93\u4edd\u8f90\u50f8\ue57a\u1cd6", -1760398246, 0x229D082C ^ CryptoVault.mask()));
        this.themeMaker.open();
    }

    private void drawThemeMakerEditor(Canvas canvas, float f2, float f3, float f4, float f5) {
        int n2 = f4 > 520.0f ? 4 : 3;
        float f6 = this.themeGridHeight(HitboxConfigScreen752809.a, f4, n2) + 8.0f;
        List<Section> list = this.appearanceSections();
        float f7 = this.clampOverlayScroll(f6 + this.sectionListHeight(canvas, list, f4 - 16.0f) + 8.0f, f5);
        canvas.beginClip(f2, f3, f4, f5);
        this.drawThemeGrid(canvas, HitboxConfigScreen752809.a, f2, f3 - f7, f4, n2, DropdownGpuClickGuiScreen.hj("\u03f0\u6b48\u6aee\uabf8\uf08b\u78c3\uac53\u5ca1\u0c89\u2791\u38c3\u6e24\ube76\u6547", 1208697031, 0x229D082C ^ CryptoVault.mask()), com.ishland.scalablelux.l.d::ab);
        this.drawSectionList(canvas, list, f2 + 8.0f, f3 - f7 + f6, f4 - 16.0f);
        canvas.endClip();
    }

    private List<Section> appearanceSections() {
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Section> arrayList = new ArrayList<Section>();
        for (Map.Entry<String, List<Setting>> entry : HitboxConfigScreen752809.f().entrySet()) {
            ArrayList<Setting> arrayList2 = new ArrayList<Setting>();
            for (Setting setting : entry.getValue()) {
                ClassicSettingLayout.Row row;
                if (setting == null || !HitboxConfigScreen752809.a(setting)) continue;
                ClassicSettingLayout.Row row2 = row = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, setting);
                if (row != null && !row.visible()) continue;
                arrayList2.add(setting);
            }
            if (arrayList2.isEmpty()) continue;
            arrayList.add(new Section(entry.getKey(), arrayList2));
        }
        return arrayList;
    }

    private boolean settingsOverlay() {
        return DropdownGpuClickGuiScreen.hj("\u12d9\u22bd\ua926\ub73c\u161b\uf7d8\u8567\u7595\u69fe\u23b8\u9e6e", 552352455, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) || DropdownGpuClickGuiScreen.hj("\uf668\ua4bc\u2245\u60b6\ud38e\uced2\ubfdf\ua7ab\uaae6\uefff\u2369\uc307\u711c\ued30\uc92f\ucb5d", 1228233629, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) || DropdownGpuClickGuiScreen.hj("\u0e69\u2b75\u9ec2\u2bf1\u787e\u5637\u44f6\u5176", 201645662, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) || DropdownGpuClickGuiScreen.hj("\ucb75\u53ac\u6014\u8c2e\u9fed\u391d\ud9af", -935230575, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView);
    }

    private void drawModuleOverlay(Canvas canvas, float f2, float f3, float f4, float f5) {
        Module module = this.overlayModule;
        if (module == null) {
            return;
        }
        String string = module.getDescription() == null ? "" : module.getDescription();
        float f6 = f3;
        if (!string.isBlank()) {
            canvas.text(f2 + 8.0f, f3 + 2.0f, 10.0f, this.muted, this.fit(canvas, string, 10.0f, f4 - 16.0f), UiDrawCommand.TextWeight.REGULAR);
            f6 = f3 + 22.0f;
        }
        ModuleView moduleView = this.viewFor(module);
        float f7 = f4 - 16.0f;
        float f8 = 6.0f + this.toolsHeight(moduleView) + this.sectionsHeight(canvas, moduleView.sections(), f7) + (moduleView.sections().isEmpty() ? 22.0f : 0.0f) + (module != this.clickGuiModule() ? 38.0f : 0.0f) + 10.0f;
        float f9 = f5 - (f6 - f3);
        float f10 = this.clampOverlayScroll(f8, f9);
        canvas.beginClip(f2, f6, f4, f9);
        float f11 = f6 - f10 + 6.0f;
        if (this.toolsHeight(moduleView) > 0.0f) {
            this.drawToolsRow(canvas, module, moduleView, f2 + 8.0f, f11, f7);
            f11 += this.toolsHeight(moduleView);
        }
        if (moduleView.sections().isEmpty()) {
            canvas.textMid(f2 + 8.0f, f11, 22.0f, 10.0f, this.muted, moduleView.total() == 0 ? DropdownGpuClickGuiScreen.hj("\ub6be\u0734\ub15f\u8ea5\ua5c9\uf434\ube9e\u8282\ub9cc\u43b4\u6e9e\ua881\ue969\ue56f\u1fd9\u8a59\u8702\uf309\u595a\u9bd7\u776c\u7bc0", -2055727922, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u26bc\u8fc7\u5a98\u8f30\ucdc7\u459a\u2f94\u1d64\u8446\u5aa2\u4d22\uf679\ufd2d\u74a3\u662c\ud6d5\u6166\u74fc\ue3fe\u8b81\u9ccf\u9a58", -843297361, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
            f11 += 22.0f;
        }
        f11 = this.drawSections(canvas, moduleView.sections(), f2 + 8.0f, f11, f7);
        if (module != this.clickGuiModule()) {
            this.drawBindRow(canvas, module, f2 + 8.0f, f11, f7);
        }
        canvas.endClip();
    }

    private float sectionListHeight(Canvas canvas, List<Section> list, float f2) {
        return 6.0f + this.sectionsHeight(canvas, list, f2);
    }

    private float drawSectionList(Canvas canvas, List<Section> list, float f2, float f3, float f4) {
        return this.drawSections(canvas, list, f2, f3 + 6.0f, f4);
    }

    private float clampOverlayScroll(float f2, float f3) {
        this.overlayScroll = DropdownGpuClickGuiScreen.clamp(this.overlayScroll, 0.0f, Math.max(0.0f, f2 - f3));
        return this.overlayScroll;
    }

    private void drawClickGuiOverlay(Canvas canvas, float f2, float f3, float f4, float f5) {
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Section> arrayList = new ArrayList<Section>();
        if (hitboxConfigScreen752809 != null) {
            for (Section section : this.sectionsFor(hitboxConfigScreen752809)) {
                ArrayList<Setting> arrayList2 = new ArrayList<Setting>();
                for (Setting setting : section.rows()) {
                    if (setting instanceof ConfigKeybindListSetting || setting == HitboxConfigScreen752809.aC || setting == HitboxConfigScreen752809.aD || setting == HitboxConfigScreen752809.aE || setting == HitboxConfigScreen752809.aF || setting == HitboxConfigScreen752809.aG || setting == HitboxConfigScreen752809.as || setting == HitboxConfigScreen752809.at || setting == HitboxConfigScreen752809.au || setting == HitboxConfigScreen752809.aw || HitboxConfigScreen752809.b(setting)) continue;
                    arrayList2.add(setting);
                }
                if (arrayList2.isEmpty()) continue;
                arrayList.add(new Section(section.name(), arrayList2, section.key(), section.depth()));
            }
        }
        float f6 = 28.0f;
        float f7 = 8.0f;
        float f8 = UiKit.buttonWidth(DropdownGpuClickGuiScreen.hj("\u38b6\u84e4\u4511\uc4b8\u3a54\ued0c\u0f80\u471b\u4d23\u0d46\ud833\uad97\u17a5", -302852241, 0x229D082C ^ CryptoVault.mask()), this.s) / this.s;
        UiKit.button(canvas.b, this.host, DropdownGpuClickGuiScreen.hj("\u2616\u617c\ufdbc\u2d33\udb44\u547b\ub806\u8f85\u6ee3\u1ac5\u7be9\u93db\u6277", 399635184, 0x229D082C ^ CryptoVault.mask()), (f2 + 8.0f) * this.s, f3 * this.s, f8 * this.s, f6 * this.s, this.s, UiKit.Style.SECONDARY, true, this.mx, this.my, this::replayTour);
        String string = WhatsNewPanel.hasUnread() ? DropdownGpuClickGuiScreen.hj("\u3ef2\ua06c\u60fd\ufd9a\u50cd\u49fc\u2c53\u1880\u7525\u6f89\ub9d3\u8eb0\u660a\udec7\ue9ab", 2116615068, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u6320\uc852\u1130\ueb44\ue559\ue549\u41fd\u11e6\u1d86\u502d\u0e8c\u6818\u9abb\udf8e", 1386379644, 0x229D082C ^ CryptoVault.mask());
        float f9 = UiKit.buttonWidth(string, this.s) / this.s;
        UiKit.button(canvas.b, this.host, string, (f2 + 8.0f + f8 + 8.0f) * this.s, f3 * this.s, f9 * this.s, f6 * this.s, this.s, UiKit.Style.SECONDARY, true, this.mx, this.my, this.whatsNew::open);
        boolean bl = this.signOutArmed && this.nanoTime() - this.signOutArmedAt < 3500000000L;
        String string2 = bl ? DropdownGpuClickGuiScreen.hj("\u7987\u946f\ub082\u5775\u35cc\ucbdf\u8cc4\u2179\ua91b\u5585\ufede\udce0\u3ce0\u05c8\u1bbc\u03c7\ua559\u8c1d\ucdd5\ue31b\uff2f\ue555\u869b\u217c\u3708", 1627965153, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u3c73\u2a66\ue724\uf550\u08e3\ua276\u8779\u7548\ub24e\u273c\u9302\ucb8b", -523301048, 0x229D082C ^ CryptoVault.mask());
        float f10 = UiKit.buttonWidth(string2, this.s) / this.s;
        UiKit.button(canvas.b, this.host, string2, (f2 + f4 - 8.0f - f10) * this.s, f3 * this.s, f10 * this.s, f6 * this.s, this.s, UiKit.Style.SECONDARY, true, this.mx, this.my, () -> {
            if (this.signOutArmed && this.nanoTime() - this.signOutArmedAt < 3500000000L) {
                this.signOutArmed = false;
                SlAuth.signOutAndPrompt(DropdownGpuClickGuiScreen.hj("\u26a7\u5c4a\uec4c\u06cb\uab6a\ubfd0\u207d\u3ec5\ufa20\uaf28\uf0a3\u95c1\udf8b\u5d35\u0894\u1ecd\u20e2\uf112\ub3b6\u8af6\u7ccd\u13c2\ua500\u8977\ua334\u8e23\u3a74\u5572\u72df\u78ce\uea5f\u90f1\u39e4\u187a\u6d7d\ue1d9\u0424\ue3b3\u3da5", 1459016121, 0x229D082C ^ CryptoVault.mask()));
            } else {
                this.signOutArmed = true;
                this.signOutArmedAt = this.nanoTime();
            }
        });
        float f11 = f4 - 16.0f;
        float f12 = this.sectionListHeight(canvas, arrayList, f11);
        float f13 = this.clampOverlayScroll(f12, f5 -= f6 + f7);
        canvas.beginClip(f2, f3 += f6 + f7, f4, f5);
        if (arrayList.isEmpty()) {
            canvas.text(f2 + 8.0f, f3 + 10.0f, 10.0f, this.muted, DropdownGpuClickGuiScreen.hj("\u017e\u6719\u2559\u9f8d\uaa62\ue673\u6c7f\u2a51\u68db\u57b2\udc90\uc226\u45ee\u6782\uc3d3\uaefc\u6ac8\u2ecf\u91e2\u1ec4\u8238\u1804\uc4bc\u5e9e\uc71d\ue3d4", 1741866369, 0x229D082C ^ CryptoVault.mask()));
        }
        this.drawSectionList(canvas, arrayList, f2 + 8.0f, f3 - f13, f11);
        canvas.endClip();
    }

    private void drawSsProofOverlay(Canvas canvas, float f2, float f3, float f4, float f5) {
        String string = HitboxConfigScreen752809.l();
        String string2 = string != null ? string : DropdownGpuClickGuiScreen.hj("\u268f\u79f0\u3423\u258d\u9615\u347a\u4d74\u5e00\u80fe\uf055\ufd98\uc07d\u3b7d\u6e71\u487b\ubafc\u9a07\u68a2\ufc91\u4dcf\uff63\u0838\uf40f\ue09a\u341b\u7030\u7ca4\u42e4\uc5ae\uaa21\ua8d1\uac7e\u3b1c\u9ca2\u01fb\ua9ae\u89d4\ud2f0\uf8e2\ub730\u35a3\ubc41\u75d2\u6628\udf16\u46f2\u5d54\u3e10\u1f3f\u9dcb\u2473\u2a66\ue058\uc3db\ucfdb\ud83a\ucbaf\u47a5\u7bea\u5723\ub28a\udce2\udda5\u55bb\u224a\u87e8\u340e\ue7b4\u35d3\u3599\u448e\u3888\u1ad6\u3292\uae28\u35ea\u47eb\u0e15\u6908\u6650\uc0a6", -1239454891, 0x229D082C ^ CryptoVault.mask());
        canvas.text(f2 + 8.0f, f3 + 2.0f, 10.0f, string != null ? -30070 : this.muted, this.fit(canvas, string2, 10.0f, f4 - 16.0f), UiDrawCommand.TextWeight.REGULAR);
        float f6 = f3 + 22.0f;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting f9 : new Setting[]{HitboxConfigScreen752809.as, HitboxConfigScreen752809.at, HitboxConfigScreen752809.au, HitboxConfigScreen752809.aw}) {
            ClassicSettingLayout.Row f10;
            if (f9 == null) continue;
            ClassicSettingLayout.Row row = f10 = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, f9);
            if (f10 != null && !f10.visible()) continue;
            arrayList.add(f9);
        }
        List<Section> list = List.of(new Section(DropdownGpuClickGuiScreen.hj("\ua436\uef07\u15b3\ucde8\uffda\ub939\ufaf4\u561d", -2125704124, 0x229D082C ^ CryptoVault.mask()), arrayList));
        float f7 = f4 - 16.0f;
        float f8 = this.sectionListHeight(canvas, list, f7) + 10.0f;
        float f9 = f5 - (f6 - f3);
        float f10 = this.clampOverlayScroll(f8, f9);
        canvas.beginClip(f2, f6, f4, f9);
        this.drawSectionList(canvas, list, f2 + 8.0f, f6 - f10, f7);
        canvas.endClip();
    }

    private void drawPurgeOverlay(Canvas canvas, float f2, float f3, float f4, float f5) {
        canvas.text(f2 + 8.0f, f3 + 2.0f, 10.0f, this.muted, this.fit(canvas, DropdownGpuClickGuiScreen.hj("\u2f50\u31d4\u04c5\u1231\uc207\uf096\uce85\u2dee\u78dc\ue819\u2f86\u55db\u6975\u62f5\u6e7c\u9438\uae5a\u7431\ucbaa\u9e98\u9869\u31ad\u8607\ue625\u8878\u1f30\u434b\u1f39\u98bb\uf66c\ub801\u6d7c\u3617\uf2f1\u4f43\u3636\ub6f8\u9c8d\u39ec\ud437\u18af\u6b21\u7584\u0820\u0c36\u7e88\u954b\u7880\u49b0\uf7ca\ueea9\u6871\ub663\u3c8d\u6de5\u6898\ua37d\ubbb7\ue324\ue142\u5d2d\u0afe\u0a78\u932a\u1f8d\u1228\u2360\ub958\u7353\u7090\u2896\uee75\u07fa\u77c7\u4449\ucb5a\uae96\u752b", -1013174013, 0x229D082C ^ CryptoVault.mask()), 10.0f, f4 - 16.0f), UiDrawCommand.TextWeight.REGULAR);
        float f6 = f3 + 22.0f;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting f9 : new Setting[]{HitboxConfigScreen752809.aC, HitboxConfigScreen752809.aD, HitboxConfigScreen752809.aE, HitboxConfigScreen752809.aF, HitboxConfigScreen752809.aG}) {
            ClassicSettingLayout.Row f10;
            if (f9 == null) continue;
            ClassicSettingLayout.Row row = f10 = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, f9);
            if (f10 != null && !f10.visible() || f9 == HitboxConfigScreen752809.aG && HitboxConfigScreen752809.aE != null && HitboxConfigScreen752809.aE.isEnabled()) continue;
            arrayList.add(f9);
        }
        List<Section> list = List.of(new Section(DropdownGpuClickGuiScreen.hj("\u728f\u03f8\u22c8\u1d87\u9e39\ue540\u82c0\u8920\u686f\u9dfd\u9c2e", -879329170, 0x229D082C ^ CryptoVault.mask()), arrayList));
        float f7 = f4 - 16.0f;
        float f8 = 44.0f;
        float f9 = this.sectionListHeight(canvas, list, f7) + 10.0f + f8;
        float f10 = f5 - (f6 - f3);
        float f11 = this.clampOverlayScroll(f9, f10);
        canvas.beginClip(f2, f6, f4, f10);
        float f12 = this.drawSectionList(canvas, list, f2 + 8.0f, f6 - f11, f7) + 10.0f;
        boolean bl = this.ddArmed && this.nanoTime() - this.ddArmedAt < 3500000000L;
        int n2 = -38037;
        boolean bl2 = canvas.hov(f2 + 8.0f, f12, f7, f8);
        canvas.rect(f2 + 8.0f, f12, f7, f8, 10.0f, bl ? DropdownGpuClickGuiScreen.argb(51.0f, 0xFF5555) : DropdownGpuClickGuiScreen.argb(bl2 ? 28.0f : 16.0f, 0xFF5555));
        canvas.border(f2 + 8.0f, f12, f7, f8, 10.0f, this.hair(), bl ? n2 : DropdownGpuClickGuiScreen.argb(64.0f, n2));
        String string = bl ? DropdownGpuClickGuiScreen.hj("\u19f1\u02fe\u63c3\u6437\ubd1a\ue7e0\u515a\u2831\u9158\u7933\ub4a1\u606b\ucdf8\ufc43\u3f2f\u10e8\u6e0d\u4ee7\ua2a5\udc4a\u8e64\u1bd7\u8d6a\u83d1\u3519\u960c\u25e1\u54c4\ufaa0\u25f7", -478395256, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u34fd\u0745\u7314\u1945\u5284\ueeab\uf0f1\uf364\ud23a\u4698\udc20\ufdfc\ufa0c\u0440\u8b16\u9982\ua7a8\ubcc7\u1417\u8ab5\u1c3a", -340594180, 0x229D082C ^ CryptoVault.mask());
        float f13 = canvas.tw(string, 11.5f, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.textMid(f2 + 8.0f + (f7 - f13) * 0.5f, f12, f8, 11.5f, bl ? n2 : -30070, string, UiDrawCommand.TextWeight.SEMIBOLD);
        canvas.hit(f2 + 8.0f, f12, f7, f8, () -> {
            if (this.ddArmed && this.nanoTime() - this.ddArmedAt < 3500000000L) {
                this.ddArmed = false;
                if (this.field_22787 != null) {
                    McCompat.setScreen(this.field_22787, null);
                }
                HitboxRenderStateReset726175.a();
            } else {
                this.ddArmed = true;
                this.ddArmedAt = this.nanoTime();
            }
        });
        canvas.endClip();
    }

    private void drawBindsOverlay(Canvas canvas, float f2, float f3, float f4, float f5) {
        ConfigKeybindManager configKeybindManager = ConfigKeybindManager.getInstance();
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        for (String object : this.savedConfigs) {
            if (object == null || object.isBlank() || DropdownGpuClickGuiScreen.hj("\ub2a9\u240c\u3d83\u382f\ufbbc\uae7a\ueffb\u6707\uac98\u09fb\u6df3\uf654\ud6d9\ufd33\uf0c4\ucb42", 547350140, 0x229D082C ^ CryptoVault.mask()).equals(object)) continue;
            boolean bl = this.cloudProfiles.isEmpty() ? !DropdownGpuClickGuiScreen.hj("\u5d04\u986e\u6410\u643e\u8637\u64fb\u3629\u79f4\udf01\u1ca9", -1052702163, 0x229D082C ^ CryptoVault.mask()).equalsIgnoreCase(object) || !CloudConfigs.signedIn() : this.cloudProfiles.containsKey(object.toLowerCase(Locale.ROOT));
            boolean bl2 = bl;
            if (!bl2) continue;
            linkedHashSet.add(object);
        }
        for (ConfigKeybindManager.ConfigKeybind configKeybind : configKeybindManager.getKeybinds()) {
            if (configKeybind.configName == null || configKeybind.configName.isBlank()) continue;
            linkedHashSet.add(configKeybind.configName);
        }
        canvas.text(f2 + 4.0f, f3 + 2.0f, 10.0f, this.muted, DropdownGpuClickGuiScreen.hj("\uf304\ua08a\ub16e\uc005\ub3cd\u8c8a\ub15b\u8b6f\u0dc0\ueba9\u1aa0\u728f\u0b32\u4a64\u076c\u0310\ub0d3\u041d\uc8cf\uc753\u6658\u46c4\u1d51\u5ac8\u872c\u95d6\uebf5\ud205\u5e23\u89d7\ua4df\u38fd\ud412\u0e1b\u0a0b\u9aed\u4659", -1337795886, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f6 = f3 + 22.0f;
        if (linkedHashSet.isEmpty()) {
            canvas.text(f2 + 4.0f, f6 + 4.0f, 9.5f, this.muted, this.fit(canvas, CloudConfigs.signedIn() ? DropdownGpuClickGuiScreen.hj("\uaf0e\ua71d\u6ede\u40ed\u72eb\u3b1c\u3a4f\uf875\ud2ab\u55b2\ub046\u5508\ufa3d\u3b13\u5dbe\u3eff\uf086\ub035\u86be\u7e86\ud7fb\u4872\u1905\u2b25\u536a\u8b4d\u8a30\ua953\u126f\u8c42\u6d5d\u5e33\u14ae\u935a\udb11\u9a44\u4f54\uf536\ubc30\u761b\uc1b3\ud93d\u0460\ua7a6\ufa41\u9342\u6118\u5301\u0476\ua0f8\u9241\u5ea4\udaba\ucf1a\uc48c\u542b\u4dfa\u5ebd", -303906535, 0x229D082C ^ CryptoVault.mask()) : DropdownGpuClickGuiScreen.hj("\u137b\udfe9\u320b\u1289\u8580\uf7f8\u4f0f\u15f4\u097b\u02fd\u26ca\u24af\uf8b4\uee1c\u5955\u983b\uc551\u0cf5\u4954\u788a\u2437\uc1c6\u1ba3\u50be\u484c\u370e\ua0e4\u3495\ue077\ud1cb\u8da7\ub01b\ue079\ue354\u99bd\u062b\u3eea\uab2e", -2092673889, 0x229D082C ^ CryptoVault.mask()), 9.5f, f4 - 8.0f));
        }
        float f7 = 42.0f;
        float f8 = f7 + 4.0f;
        float f9 = f5 - (f6 - f3);
        float f10 = this.clampOverlayScroll((float)linkedHashSet.size() * f8, f9);
        canvas.beginClip(f2, f6, f4, f9);
        int n2 = 0;
        for (String string : linkedHashSet) {
            boolean bl;
            float f11 = f6 - f10 + (float)n2++ * f8;
            ConfigKeybindManager.ConfigKeybind configKeybind = configKeybindManager.get(string);
            boolean bl3 = bl = string.equals(this.bindingConfigName) && this.kitKeyCapture != null;
            String string2 = bl ? DropdownGpuClickGuiScreen.hj("\ue2de\ua381\uf3e2\u4a1e\uc609\u3ed4\u4310\u5e5a\uf805\u4bb3\u8f4a\ub4b9", 228634158, 0x229D082C ^ CryptoVault.mask()) : (configKeybind == null ? DropdownGpuClickGuiScreen.hj("\u4f04\ud8a8\ud84f\u5ef0\u9470\uaf8b", 1838249920, 0x229D082C ^ CryptoVault.mask()) : configKeybind.getDisplayName());
            float f12 = f11 + f7 * 0.5f;
            canvas.rect(f2, f11, f4, f7, 9.0f, DropdownGpuClickGuiScreen.argb(20.0f, 0xFFFFFF));
            float f13 = 26.0f;
            float f14 = Math.min(120.0f, Math.max(64.0f, canvas.tw(string2, 9.5f, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f));
            float f15 = f2 + f4 - f14 - 8.0f;
            float f16 = f12 - f13 * 0.5f;
            float f17 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u084a\u4272\u0e76\uf82c\u2c1e\u9d37\u4416\u0db3", -696775699, 0x229D082C ^ CryptoVault.mask()), 9.5f, UiDrawCommand.TextWeight.REGULAR) + 12.0f;
            float f18 = f15 - 6.0f - f17;
            float f19 = Math.max(0.0f, (configKeybind != null ? f18 : f15) - 10.0f - (f2 + 12.0f));
            canvas.textMid(f2 + 12.0f, f11, f7, 10.5f, this.text, this.fit(canvas, string, 10.5f, f19, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            this.keyCap(canvas, f15, f16, f14, f13, this.fit(canvas, string2, 9.5f, f14 - 16.0f, UiDrawCommand.TextWeight.SEMIBOLD), bl);
            canvas.hit(f15, f16, f14, f13, () -> {
                this.bindingConfigName = string;
                this.kitFocus = null;
                this.kitKeyCapture = n2 -> {
                    this.bindingConfigName = null;
                    if (n2 != -1) {
                        configKeybindManager.setKeybind(string, n2, -1);
                    }
                };
            });
            if (configKeybind == null) continue;
            boolean bl4 = canvas.hov(f18, f16, f17, f13);
            float f20 = canvas.tw(DropdownGpuClickGuiScreen.hj("\u7164\u0fff\ue90b\u7701\u6b74\u307d\uf925\u750c", 335353525, 0x229D082C ^ CryptoVault.mask()), 9.5f, UiDrawCommand.TextWeight.REGULAR);
            canvas.textMid(f18 + (f17 - f20) * 0.5f, f16, f13, 9.5f, bl4 ? this.text : this.muted, DropdownGpuClickGuiScreen.hj("\ufef4\uffe4\u1dbc\u69d7\u30d8\u1dc5\u9306\u5859", 519129725, 0x229D082C ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
            canvas.hit(f18, f16, f17, f13, () -> configKeybindManager.setKeybind(string, -1, -1));
        }
        canvas.endClip();
    }

    private void drawLayoutReplicas(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5) {
        this.refreshReplicaPalette();
        this.drawLayoutCards(builder, f2, f3, f4, f5, this.s, this.mx, this.my, this.host);
    }

    private void addHostHit(List<Hit> list, float f2, float f3, float f4, float f5, Runnable runnable) {
        if (this.hostClip) {
            float f6 = Math.min(f2 + f4, this.hcx + this.hcw);
            float f7 = Math.min(f3 + f5, this.hcy + this.hch);
            f2 = Math.max(f2, this.hcx);
            f3 = Math.max(f3, this.hcy);
            f4 = f6 - f2;
            f5 = f7 - f3;
            if (f4 <= 0.0f || f5 <= 0.0f) {
                return;
            }
        }
        list.add(new Hit(f2, f3, f4, f5, runnable));
    }

    private void refreshSavedConfigs() {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        try {
            linkedHashSet.addAll(ConfigManager.getInstance().getAvailableConfigs());
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.savedConfigs = List.copyOf(linkedHashSet);
        if (CloudConfigs.signedIn()) {
            PrivateConfigManager.getInstance().listConfigs(list -> {
                LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
                HashMap hashMap = new HashMap();
                try {
                    linkedHashSet.addAll(ConfigManager.getInstance().getAvailableConfigs());
                }
                catch (Exception exception) {
                    // empty catch block
                }
                list.forEach(privateConfigInfo -> {
                    linkedHashSet.add(privateConfigInfo.slug);
                    hashMap.put(privateConfigInfo.slug.toLowerCase(Locale.ROOT), privateConfigInfo);
                });
                this.cloudProfiles = Map.copyOf(hashMap);
                this.savedConfigs = List.copyOf(linkedHashSet);
            }, string -> {
                this.cloudProfiles = Map.of();
                if (string != null && !string.isBlank() && DropdownGpuClickGuiScreen.hj("\ub7f0\ubb11\u5578\u4e4f\uec44\ucb15\u5009\u20ea\u9986\u882c", -275212324, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)) {
                    this.configStatusMessage = DropdownGpuClickGuiScreen.hj("\u764a\u416e\u93a8\u18f7\u31cc\ue41e\ua67f\u69f9\ub81b\uc2e1\u7d6d\uae27\u8722\u779e\u5b9d\uf691\u883b\uf032\u4cdf\u54bd\ud01a\ubc57\ub73e\u7c4e\uc7e7\ued40\u0f0e\udcd0\u5d77", -1062015178, 0x229D082C ^ CryptoVault.mask()) + string;
                }
            });
        } else {
            this.cloudProfiles = Map.of();
        }
    }

    private static String cloudError() {
        String string = CloudConfigs.lastError();
        return string == null || string.isBlank() ? DropdownGpuClickGuiScreen.hj("\u6d34\u00f2\u662c\ua01b\u8a65\u52e4\u55e8\ue58e\u6dcd\u9515\u9464\uafb8\u745d\u053b\u24ba\ub4aa\ucb5c\u1105\ue910\ub65c\uacd1\u9254\u829c\u7fba\u2225\u7cc7\ufd60", 645961789, 0x229D082C ^ CryptoVault.mask()) : string;
    }

    private void runCloudOp(BooleanSupplier booleanSupplier, Consumer<Boolean> consumer) {
        Thread thread = new Thread(() -> {
            boolean bl;
            try {
                bl = booleanSupplier.getAsBoolean();
            }
            catch (Throwable throwable) {
                bl = false;
            }
            boolean bl2 = bl;
            if (this.field_22787 != null) {
                this.field_22787.execute(() -> {
                    consumer.accept(bl2);
                    this.refreshSavedConfigs();
                });
            }
        }, DropdownGpuClickGuiScreen.hj("\u146c\ueea1\ua9d7\ubee7\u1e8a\ud85b\uf298\u364b\u95bf\uad46\uc861\u7a55\u7e7f\ue45b\ubeb9", 1426375236, 0x229D082C ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    private void applyPendingSaveMetadata(String string, boolean bl, List<String> list) {
        boolean bl2;
        boolean bl3 = bl2 = list != null && !list.isEmpty();
        if (!bl && !bl2) {
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                String string2 = CloudConfigs.idForName(string);
                if (string2 == null) {
                    return;
                }
                if (bl) {
                    CloudConfigs.setPublicById(string2, true);
                }
                if (bl2) {
                    CloudConfigs.setTagsById(string2, list);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            if (this.field_22787 != null) {
                this.field_22787.execute(this::refreshSavedConfigs);
            }
        }, DropdownGpuClickGuiScreen.hj("\u5028\u30b0\u7283\ua5d2\u6b02\u8031\uc35a\ub8f0\u0771\u4020\u38d6\u06f9\u62fb\u4182\uff31\u7e68\ud421\u55bc\ud060", 1171168390, 0x229D082C ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    private void applyPendingConfigActions() {
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            configManager.save(configManager.getLastLoadedConfigName());
            String string2 = configManager.exportCurrentConfigJsonForCloud();
            if (this.pendingProfileSaveName != null && string2 != null) {
                String string3 = this.pendingProfileSaveName;
                boolean bl = this.pendingSavePublic;
                List<String> list = this.pendingSaveTags;
                PrivateConfigManager.getInstance().saveActiveConfig(string3, string2, () -> {
                    this.applyPendingSaveMetadata(string3, bl, list);
                    this.refreshSavedConfigs();
                }, string -> {});
                this.pendingProfileSaveName = null;
                this.pendingSavePublic = false;
                this.pendingSaveTags = List.of();
            } else if (this.pendingProfileActivationName != null) {
                String string4 = this.pendingProfileActivationName;
                PrivateConfigManager.getInstance().activateConfig(string4, () -> {}, string -> {});
                this.pendingProfileActivationName = null;
            } else if (!this.profileLoadPending && string2 != null) {
                CloudConfigSync.saveIfChangedNow(string2);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private void panel(Canvas canvas, float f2, float f3, float f4, float f5, float f6, float f7) {
        canvas.shadow(f2, f3 + 9.0f * f7, f4, f5, f6, 26.0f * f7 + 6.0f, 0.0f, DropdownGpuClickGuiScreen.argb(74.0f, 0));
        canvas.shadow(f2, f3 + 2.0f, f4, f5, f6, 7.0f, 0.0f, DropdownGpuClickGuiScreen.argb(48.0f, 0));
        if (this.materialPanel) {
            if (!this.activeSurface.isTheme()) {
                float f8 = (float)HitboxConfigScreen752809.d.B() / 100.0f;
                float f9 = (float)HitboxConfigScreen752809.e.B() / 100.0f;
                float f10 = (float)HitboxConfigScreen752809.f.B() / 100.0f;
                canvas.b.material(f2 * this.s, f3 * this.s, f4 * this.s, f5 * this.s, f6 * this.s, this.activeSurface.material(f8, f10, -2.0f, -2.0f, 0.0f, false, UiThemeMaterial.theme(this.themeName).accent() & 0xFFFFFF, f9));
            } else {
                canvas.b.material(f2 * this.s, f3 * this.s, f4 * this.s, f5 * this.s, f6 * this.s, UiThemeMaterial.forTheme(this.themeName));
            }
            canvas.rect(f2, f3, f4, f5, f6, DropdownGpuClickGuiScreen.argb(44.0f, 0));
        } else {
            canvas.rect(f2, f3, f4, f5, f6, DropdownGpuClickGuiScreen.argb(236.0f, this.panelBase));
        }
        canvas.gradient(f2, f3, f4, f5, f6, DropdownGpuClickGuiScreen.argb(20.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(10.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(28.0f, this.deep), DropdownGpuClickGuiScreen.argb(38.0f, this.deep));
        canvas.border(f2, f3, f4, f5, f6, this.hair(), this.rim);
        if (f4 > f6 * 2.0f + 4.0f) {
            canvas.rect(f2 + f6, f3 + 0.75f, f4 - f6 * 2.0f, 0.75f, 0.0f, DropdownGpuClickGuiScreen.argb(36.0f, 0xFFFFFF));
        }
    }

    private void glassDisc(Canvas canvas, float f2, float f3, float f4) {
        canvas.shadow(f2 - f4, f3 - f4 + 4.0f, f4 * 2.0f, f4 * 2.0f, f4, 16.0f, 0.0f, DropdownGpuClickGuiScreen.argb(68.0f, 0));
        canvas.rect(f2 - f4, f3 - f4, f4 * 2.0f, f4 * 2.0f, f4, DropdownGpuClickGuiScreen.argb(236.0f, this.panelBase));
        canvas.gradient(f2 - f4, f3 - f4, f4 * 2.0f, f4 * 2.0f, f4, DropdownGpuClickGuiScreen.argb(30.0f, 0xFFFFFF), DropdownGpuClickGuiScreen.argb(10.0f, 0xFFFFFF), 0xFFFFFF, DropdownGpuClickGuiScreen.argb(18.0f, this.deep));
        canvas.border(f2 - f4, f3 - f4, f4 * 2.0f, f4 * 2.0f, f4, this.hair(), this.rim);
    }

    private float hair() {
        return Math.max(1.0f, (float)Math.round(this.s * 0.75f));
    }

    private float reveal(float f2, float f3) {
        float f4 = DropdownGpuClickGuiScreen.clamp((f2 - f3) / 0.4f, 0.0f, 1.0f);
        return 1.0f - (1.0f - f4) * (1.0f - f4) * (1.0f - f4);
    }

    private void pruneEases() {
        this.anims.endFrame();
    }

    private String modKey(int n2, Module module) {
        String[] stringArray = this.modKeys.get(module);
        if (stringArray == null) {
            stringArray = new String[MOD_KEY_PREFIX.length];
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                stringArray[i2] = MOD_KEY_PREFIX[i2] + module.getName();
            }
            this.modKeys.put(module, stringArray);
        }
        return stringArray[n2];
    }

    private float ease(Object object, float f2, float f3) {
        return this.anims.ease(object, f2, f3, this.dt);
    }

    private String fit(Canvas canvas, String string, float f2, float f3) {
        return this.fit(canvas, string, f2, f3, UiDrawCommand.TextWeight.REGULAR);
    }

    private String fit(Canvas canvas, String string, float f2, float f3, UiDrawCommand.TextWeight textWeight) {
        int n2;
        if (string == null || string.isEmpty()) {
            return "";
        }
        if (canvas.tw(string, f2, textWeight) <= f3) {
            return string;
        }
        for (n2 = string.length(); n2 > 1; --n2) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!(canvas.tw(stringBuilder.append(string.substring(0, n2)).append("\u2026").toString(), f2, textWeight) > f3)) break;
        }
        return string.substring(0, Math.max(1, n2)).stripTrailing() + "\u2026";
    }

    private List<String> wrap(Canvas canvas, String string, float f2, float f3, int n2) {
        return this.wrap(canvas, string, f2, f3, n2, UiDrawCommand.TextWeight.REGULAR);
    }

    private List<String> wrap(Canvas canvas, String string2, float f2, float f3, int n2, UiDrawCommand.TextWeight textWeight) {
        boolean bl;
        ArrayList<String> arrayList = new ArrayList<String>();
        if (string2 == null || string2.isBlank()) {
            return arrayList;
        }
        String[] stringArray = string2.trim().split(DropdownGpuClickGuiScreen.hj("\u0f5c\u3d0a\u2c25\u26c2", 1307872592, 0x229D082C ^ CryptoVault.mask()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String string3 : stringArray) {
            String string4;
            String string5 = string4 = stringBuilder.length() == 0 ? string3 : stringBuilder + " " + string3;
            if (canvas.tw(string4, f2, textWeight) <= f3 || stringBuilder.length() == 0) {
                stringBuilder = new StringBuilder(string4);
                continue;
            }
            arrayList.add(stringBuilder.toString());
            stringBuilder = new StringBuilder(string3);
            if (arrayList.size() == n2) break;
        }
        if (arrayList.size() < n2 && stringBuilder.length() > 0) {
            arrayList.add(stringBuilder.toString());
        }
        boolean bl2 = bl = arrayList.size() == n2 && String.join((CharSequence)" ", arrayList).length() < string2.trim().length() - 1;
        if (bl) {
            arrayList.set(n2 - 1, this.fit(canvas, ((String)arrayList.get(n2 - 1)).stripTrailing() + "\u2026", f2, f3, textWeight));
        }
        arrayList.replaceAll(string -> this.fit(canvas, (String)string, f2, f3, textWeight));
        return arrayList;
    }

    private static String trim(String string, int n2) {
        return string.length() <= n2 ? string : string.substring(0, Math.max(1, n2 - 1)) + "\u2026";
    }

    private static float clamp(float f2, float f3, float f4) {
        return Math.max(f3, Math.min(f4, f2));
    }

    private static int argb(float f2, int n2) {
        return Math.max(0, Math.min(255, Math.round(f2))) << 24 | n2 & 0xFFFFFF;
    }

    private static int mix(int n2, int n3, float f2) {
        f2 = DropdownGpuClickGuiScreen.clamp(f2, 0.0f, 1.0f);
        int n4 = Math.round((float)(n2 >>> 24 & 0xFF) * (1.0f - f2) + (float)(n3 >>> 24 & 0xFF) * f2);
        int n5 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f2) + (float)(n3 >> 16 & 0xFF) * f2);
        int n6 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f2) + (float)(n3 >> 8 & 0xFF) * f2);
        int n7 = Math.round((float)(n2 & 0xFF) * (1.0f - f2) + (float)(n3 & 0xFF) * f2);
        return n4 << 24 | n5 << 16 | n6 << 8 | n7;
    }

    private boolean capturing() {
        return this.bindingModule != null || this.bindingSetting != null || this.bindingBool != null;
    }

    private void cancelCapture() {
        if (this.bindingModule != null) {
            this.bindingModule.setBinding(false);
        }
        if (this.bindingSetting != null && this.bindingSetting.a() != null) {
            this.bindingSetting.a().setBinding(false);
        }
        this.bindingModule = null;
        this.bindingSetting = null;
        this.bindingBool = null;
    }

    private void applyCapture(int n2) {
        if (this.bindingBool != null) {
            this.bindingBool.setKeyCode(n2);
            this.cancelCapture();
            return;
        }
        Module module = this.bindingModule != null ? this.bindingModule : (this.bindingSetting != null ? this.bindingSetting.a() : null);
        boolean bl = module instanceof HitboxConfigScreen752809;
        if (!(module == null || bl && n2 == -1)) {
            module.setKeyCode(n2);
        }
        this.cancelCapture();
    }

    private void captureMouse(int n2, double d2, double d3) {
        KeybindCapture.Action action = this.captureOrigin.action(n2, d2, d3, System.currentTimeMillis());
        if (action == KeybindCapture.Action.IGNORE) {
            return;
        }
        if (action == KeybindCapture.Action.CANCEL) {
            this.kitKeyCapture = null;
            this.cancelCapture();
            return;
        }
        this.captureOrigin.clear();
        int n3 = KeybindCapture.encodeMouse(n2);
        this.noteCapturedKey(n3);
        if (this.kitKeyCapture != null) {
            IntConsumer intConsumer = this.kitKeyCapture;
            this.kitKeyCapture = null;
            intConsumer.accept(n3);
            return;
        }
        this.applyCapture(n3);
    }

    @Override
    public boolean isCapturingKeybind() {
        return this.capturing() || this.kitKeyCapture != null || super.isCapturingKeybind();
    }

    private static int menuKey() {
        Module module = ModuleAccess.getModule(DropdownGpuClickGuiScreen.hj("\uc9f0\uc192\ub724\ucdc3\u5bbb\u61dd\u6007\u7d65\u8751\ud1de\u4480\u2ad3", 726422819, 0x229D082C ^ CryptoVault.mask()));
        int n2 = module == null ? -1 : module.getKeyCode();
        return n2 == -1 || n2 == 0 ? 344 : n2;
    }

    private void stopEditing() {
        if (this.editingString != null) {
            this.editingString.L(false);
        }
        this.editingString = null;
    }

    private void beginEdit(StringSetting stringSetting) {
        this.stopEditing();
        this.editingString = stringSetting;
        stringSetting.L(true);
    }

    private void blurFocus() {
        if (this.kitFocus != null) {
            UiTextTarget uiTextTarget = this.kitFocus;
            this.kitFocus = null;
            uiTextTarget.onBlur();
        }
    }

    private void applySlider(SliderSetting sliderSetting, double d2) {
        if (this.sliderWpx <= 0.0f) {
            return;
        }
        double d3 = DropdownGpuClickGuiScreen.clamp((float)((d2 - (double)this.sliderPx) / (double)this.sliderWpx), 0.0f, 1.0f);
        double d4 = sliderSetting.B();
        sliderSetting.B(sliderSetting.D() + d3 * (sliderSetting.E() - sliderSetting.D()));
        int n2 = (int)Math.floor(d3 * 20.0);
        if (sliderSetting.B() != d4 && n2 != this.sliderNotch) {
            this.sliderNotch = n2;
            UiSounds.play(UiSounds.Event.SLIDER);
        }
    }

    private Runnable topHit(List<Hit> list, double d2, double d3) {
        for (int i2 = list.size() - 1; i2 >= 0; --i2) {
            if (!list.get(i2).contains(d2, d3)) continue;
            return list.get(i2).action();
        }
        return null;
    }

    @Override
    public boolean method_25402(class_11909 class_119092, boolean bl) {
        boolean bl2;
        if (this.touch.intercepts(class_119092)) {
            return this.touch.press(class_119092, bl);
        }
        this.pressX = class_119092.comp_4798() * (double)this.ppg;
        this.pressY = class_119092.comp_4799() * (double)this.ppg;
        UiTextTarget uiTextTarget = this.kitFocus;
        boolean bl3 = bl2 = this.overlayView == null && !this.ddTour.active() && !this.whatsNew.isOpen();
        if (bl2) {
            this.kitFocus = null;
        }
        boolean bl4 = this.isCapturingKeybind();
        boolean bl5 = this.handleClick(class_119092);
        if (bl2 && uiTextTarget != null && this.kitFocus != uiTextTarget) {
            uiTextTarget.onBlur();
        }
        if (!this.isCapturingKeybind()) {
            this.captureOrigin.clear();
        } else if (!bl4) {
            this.captureOrigin.clear();
            if (class_119092.method_74245() == 0) {
                for (int i2 = this.hits.size() - 1; i2 >= 0; --i2) {
                    Hit hit = this.hits.get(i2);
                    if (!hit.contains(this.pressX, this.pressY)) continue;
                    this.captureOrigin.mark(hit.x(), hit.y(), hit.w(), hit.h(), System.currentTimeMillis());
                    break;
                }
            }
        }
        return bl5;
    }

    private boolean handleClick(class_11909 class_119092) {
        Runnable runnable;
        double d2 = this.pressX;
        double d3 = this.pressY;
        int n2 = class_119092.method_74245();
        boolean bl = this.secondaryIsMiddle = n2 == 2;
        if (this.ddTour.active()) {
            Runnable runnable2;
            if (n2 == 0 && (runnable2 = this.topHit(this.hits, d2, d3)) != null) {
                DropdownGpuClickGuiScreen.pressWithSound(runnable2);
            }
            return true;
        }
        if (this.whatsNew.isOpen()) {
            Runnable runnable3;
            if (n2 == 0 && (runnable3 = this.topHit(this.hits, d2, d3)) != null) {
                DropdownGpuClickGuiScreen.pressWithSound(runnable3);
            }
            return true;
        }
        if (this.colorPicker != null) {
            Runnable runnable4;
            if (n2 == 0 && (runnable4 = this.topHit(this.hits, d2, d3)) != null) {
                DropdownGpuClickGuiScreen.pressWithSound(runnable4);
            }
            return true;
        }
        if (this.overlayView != null) {
            Runnable runnable5;
            if (this.kitKeyCapture != null || this.rowsOverlay() && this.capturing()) {
                this.captureMouse(n2, d2, d3);
                return true;
            }
            if (this.rowsOverlay() && this.editingString != null) {
                this.stopEditing();
            }
            if ((runnable5 = this.topHit(n2 == 1 || n2 == 2 ? this.rightHits : this.hits, d2, d3)) != null) {
                DropdownGpuClickGuiScreen.pressWithSound(runnable5);
            }
            return true;
        }
        if (this.capturing()) {
            this.captureMouse(n2, d2, d3);
            return true;
        }
        if (this.ctxItems != null) {
            Runnable runnable6 = this.topHit(n2 == 1 || n2 == 2 ? this.rightHits : this.hits, d2, d3);
            if (runnable6 != null) {
                DropdownGpuClickGuiScreen.pressWithSound(runnable6);
            } else {
                this.closeContextMenu();
            }
            return true;
        }
        boolean bl2 = this.editingString != null;
        String string = this.panelAt(d2, d3);
        if (string != null) {
            DropdownGpuClickGuiScreen.bringToFront(string);
        }
        if ((n2 == 1 || n2 == 2) && (runnable = this.topHit(this.rightHits, d2, d3)) != null) {
            DropdownGpuClickGuiScreen.pressWithSound(runnable);
            return true;
        }
        if (n2 == 0) {
            this.cancelDrag();
            for (int i2 = this.grips.size() - 1; i2 >= 0; --i2) {
                if (!this.grips.get(i2).contains(d2, d3)) continue;
                this.armPanel = this.grips.get(i2).key();
                this.armX = d2;
                this.armY = d3;
                if (bl2) {
                    this.stopEditing();
                }
                return true;
            }
            Runnable runnable7 = this.topHit(this.hits, d2, d3);
            if (runnable7 != null) {
                if (bl2) {
                    this.stopEditing();
                }
                if (runnable7 instanceof RowPress) {
                    RowPress rowPress = (RowPress)runnable7;
                    if (this.reorderable) {
                        this.pendingPress = rowPress;
                        this.pendingX = d2;
                        this.pendingY = d3;
                        return true;
                    }
                }
                DropdownGpuClickGuiScreen.pressWithSound(runnable7);
                return true;
            }
            if (bl2) {
                this.stopEditing();
            }
        }
        return true;
    }

    private String panelAt(double d2, double d3) {
        for (int i2 = this.scrollZones.size() - 1; i2 >= 0; --i2) {
            ScrollZone scrollZone = this.scrollZones.get(i2);
            if (!scrollZone.contains(d2, d3)) continue;
            return scrollZone.cat();
        }
        return null;
    }

    private static void pressWithSound(Runnable runnable) {
        UiSounds.beginPress();
        try {
            runnable.run();
        }
        finally {
            UiSounds.endPress(true);
        }
    }

    @Override
    public boolean method_25406(class_11909 class_119092) {
        if (this.touch.intercepts(class_119092)) {
            return this.touch.release((class_364)this, class_119092);
        }
        this.ddTour.mouseReleased();
        if (this.ddTour.active()) {
            return true;
        }
        this.draggingSlider = null;
        this.colorDrag = GpuClickGuiScreen.ColorDrag.NONE;
        this.settingPicker.release();
        if (this.dragModule != null) {
            this.dragPointerX = (float)(class_119092.comp_4798() * (double)this.ppg);
            this.dragPointerY = (float)(class_119092.comp_4799() * (double)this.ppg);
            this.commitDrag();
        } else if (this.pendingPress != null) {
            RowPress rowPress = this.pendingPress;
            this.pendingPress = null;
            DropdownGpuClickGuiScreen.pressWithSound(rowPress);
        }
        if (this.overlayView != null) {
            if (this.kitFocus != null) {
                this.kitFocus.mouseReleased();
            }
            if (DropdownGpuClickGuiScreen.hj("\u18a9\u3d86\u4412\ua76f\u5836\u599f\u6252\u3b05\ua239\u3102", 1231569482, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)) {
                this.cosmeticsView.mouseReleased();
            }
            return true;
        }
        if (this.kitFocus != null) {
            this.kitFocus.mouseReleased();
        }
        this.armPanel = null;
        this.dragPanel = null;
        this.saveLayout();
        return true;
    }

    @Override
    public boolean method_25403(class_11909 class_119092, double d2, double d3) {
        float[] fArray;
        if (this.touch.intercepts(class_119092)) {
            return this.touch.drag((class_364)this, class_119092, d2, d3);
        }
        double d4 = class_119092.comp_4798() * (double)this.ppg;
        double d5 = class_119092.comp_4799() * (double)this.ppg;
        if (this.ddTour.active()) {
            this.ddTour.mouseDragged(d4, d5);
            return true;
        }
        if (this.colorPicker != null) {
            if (this.colorDrag != GpuClickGuiScreen.ColorDrag.NONE) {
                this.pickerDrag(d4 / (double)this.s, d5 / (double)this.s);
            } else {
                if (this.settingPicker.drag(d4, d5)) {
                    return true;
                }
                if (this.kitFocus != null) {
                    this.kitFocus.mouseDragged(d4, d5);
                }
            }
            return true;
        }
        Setting setting = this.draggingSlider;
        if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            this.applySlider(sliderSetting, d4);
            return true;
        }
        if (this.overlayView != null) {
            if (DropdownGpuClickGuiScreen.hj("\u5cd4\u5f97\ue7a8\u6893}\ua4d8\ube4b\uc303\ua3f2\u5bda", -1911572651, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.cosmeticsView.mouseDragged(d4, d5)) {
                return true;
            }
            if (this.kitFocus != null) {
                this.kitFocus.mouseDragged(d4, d5);
            }
            return true;
        }
        if (this.pendingPress != null || this.dragModule != null) {
            if (this.dragModule == null && Math.hypot(d4 - this.pendingX, d5 - this.pendingY) > (double)(6.0f * Math.max(1.0f, this.s))) {
                RowPress rowPress = this.pendingPress;
                this.pendingPress = null;
                this.dragModule = rowPress.module;
                this.dragSource = rowPress.panelKey;
                this.dragVisible = List.copyOf(this.panelLists.getOrDefault(rowPress.panelKey, List.of()));
                this.dragDropIndex = -1;
            }
            this.dragPointerX = (float)d4;
            this.dragPointerY = (float)d5;
            return true;
        }
        if (this.kitFocus != null && this.kitFocus.mouseDragged(d4, d5)) {
            return true;
        }
        if (this.dragPanel == null && this.armPanel != null && (Math.abs(d4 - this.armX) > (double)(6.0f * Math.max(1.0f, this.s)) || Math.abs(d5 - this.armY) > (double)(6.0f * Math.max(1.0f, this.s)))) {
            this.dragPanel = this.armPanel;
            fArray = POS.get(this.dragPanel);
            this.dragOffX = (float)(d4 / (double)this.s - (double)fArray[0]);
            this.dragOffY = (float)(d5 / (double)this.s - (double)fArray[1]);
        }
        if (this.dragPanel != null) {
            fArray = POS.get(this.dragPanel);
            fArray[0] = (float)(d4 / (double)this.s - (double)this.dragOffX);
            fArray[1] = (float)(d5 / (double)this.s - (double)this.dragOffY);
            return true;
        }
        return true;
    }

    /*
     * Exception decompiling
     */
    @Override
    public boolean method_25401(double var1_1, double var3_2, double var5_3, double var7_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public boolean method_25404(class_11908 class_119082) {
        boolean bl;
        int n2 = class_119082.comp_4795();
        int n3 = class_119082.comp_4797();
        this.noteKeySeen(n2);
        if (this.ddTour.active()) {
            if (n2 == 256) {
                this.ddTour.finish();
            } else if (n2 == 262 || n2 == 257 || n2 == 335) {
                this.ddTour.next();
            } else if (n2 == 263) {
                this.ddTour.back();
            }
            return true;
        }
        if (this.whatsNew.isOpen()) {
            if (n2 == 256) {
                this.whatsNew.close();
            }
            return true;
        }
        if (this.colorPicker != null) {
            if (n2 == 256 || n2 == DropdownGpuClickGuiScreen.menuKey() && this.kitFocus == null) {
                this.closePicker();
                return true;
            }
            if (this.kitFocus != null) {
                this.kitFocus.keyPressed(n2, n3);
            }
            return true;
        }
        if (this.overlayView != null && (!this.rowsOverlay() || !this.capturing() && this.editingString == null)) {
            if (this.kitKeyCapture != null) {
                IntConsumer intConsumer = this.kitKeyCapture;
                this.kitKeyCapture = null;
                this.noteCapturedKey(n2);
                this.swallowCharsUntilMs = System.currentTimeMillis() + 250L;
                intConsumer.accept(n2 == 256 ? -1 : n2);
                return true;
            }
            if (this.ctxItems != null) {
                this.closeContextMenu();
                if (n2 == 256) {
                    return true;
                }
            }
            if (n2 == 256 && this.kitFocus != null && this.kitFocus == this.sliderEdit.field()) {
                this.sliderEdit.cancel();
                this.blurFocus();
                return true;
            }
            if (this.kitFocus != null && this.kitFocus.keyPressed(n2, n3)) {
                return true;
            }
            if (n2 == 256 && DropdownGpuClickGuiScreen.hj("\u2e24\ub2da\u2bd7\uc62c\ue20a\u6fe2\u89a4\u1c45\ub7c8\u28b6\u97da", 1754756644, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.scriptView.fullscreen()) {
                this.scriptView.exitFullscreen();
                return true;
            }
            if (n2 == 256 && DropdownGpuClickGuiScreen.hj("\uea29\u323e\u690d\u7fdf\ua919\ube5b\u443a\u8b70\ubaa0", 1507958400, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView) && this.configsView.editing()) {
                this.configsView.closeEdit();
                return true;
            }
            if (n2 == 256 && DropdownGpuClickGuiScreen.hj("\ue4ef\uabb9\uc494\ufb81\u33e6\ub27d\u5b6e\u2e8b\u9188\u397e\u58b0\uf02b\ud718\ua22d", 877813086, 0x229D082C ^ CryptoVault.mask()).equals(this.overlayView)) {
                this.openOverlay(DropdownGpuClickGuiScreen.hj("\u2be7\ube64\u7bae\u63fd\u1a6c\u9af6>\ub8ce\u6ec1", -206609592, 0x229D082C ^ CryptoVault.mask()));
                return true;
            }
            if (n2 == 256) {
                this.closeOverlay();
                return true;
            }
            if (n2 == DropdownGpuClickGuiScreen.menuKey() && this.guiKeyReleasedSinceOpen) {
                this.closeOverlay();
                this.method_25419();
                return true;
            }
            return true;
        }
        if (this.capturing()) {
            this.noteCapturedKey(n2);
            this.swallowCharsUntilMs = System.currentTimeMillis() + 250L;
            this.applyCapture(n2 == 256 ? -1 : n2);
            return true;
        }
        if (this.ctxItems != null) {
            this.closeContextMenu();
            if (n2 == 256) {
                return true;
            }
        }
        boolean bl2 = this.editingString != null || this.kitFocus != null;
        boolean bl3 = (n3 & 0xA) != 0;
        boolean bl4 = bl = (n3 & 1) != 0;
        if (n2 == 256) {
            if (this.dragModule != null || this.pendingPress != null) {
                this.cancelDrag();
                return true;
            }
            if (this.editingString != null) {
                this.stopEditing();
                return true;
            }
            if (this.kitFocus != null) {
                if (this.kitFocus == this.sliderEdit.field()) {
                    this.sliderEdit.cancel();
                }
                this.blurFocus();
                return true;
            }
            this.method_25419();
            return true;
        }
        if (n2 == DropdownGpuClickGuiScreen.menuKey()) {
            if (bl2 || !this.guiKeyReleasedSinceOpen) {
                return true;
            }
            this.method_25419();
            return true;
        }
        if (bl3 && n2 == 70 && this.editingString == null && (this.kitFocus == null || this.kitFocus == this.searchField)) {
            this.focusSearch();
            this.searchField.selectAll();
            return true;
        }
        if (this.kitFocus == this.searchField) {
            if (n2 == 265 || n2 == 264) {
                this.moveNav(n2);
                return true;
            }
            if (n2 == 257 || n2 == 335) {
                this.activateNav(bl);
                return true;
            }
        } else if (this.kitFocus == null && this.editingString == null) {
            switch (n2) {
                case 262: 
                case 263: 
                case 264: 
                case 265: {
                    if (!this.navVisible || !this.moveNav(n2)) break;
                    return true;
                }
                case 257: 
                case 335: {
                    if (!this.navVisible || this.navIndex < 0) break;
                    this.activateNav(bl);
                    return true;
                }
            }
        }
        if (this.kitFocus != null && this.kitFocus.keyPressed(n2, n3)) {
            return true;
        }
        if (n2 == 259 && this.editingString != null && !this.editingString.X().isEmpty()) {
            this.editingString.G(DropdownGpuClickGuiScreen.removeLast(this.editingString.X()));
            return true;
        }
        if (this.editingString != null && (n2 == 257 || n2 == 335)) {
            this.stopEditing();
            return true;
        }
        if (this.editingString != null && bl3 && n2 == 86 && this.field_22787 != null) {
            String string = this.field_22787.field_1774.method_1460();
            if (string != null && !string.isEmpty()) {
                this.editingString.G(this.editingString.X() + string.replaceAll(DropdownGpuClickGuiScreen.hj("\uc010\u94ab\u74b1\u8b3a\u9301\u22e2\u2250\u8b75\u9da3\u0818\u7427\u5f67\u0901\ua9ab\u1a8f", -19582415, 0x229D082C ^ CryptoVault.mask()), ""));
            }
            return true;
        }
        return true;
    }

    @Override
    public boolean method_25400(class_11905 class_119052) {
        int n2 = class_119052.comp_4793();
        if (!Character.isValidCodePoint(n2) || Character.isISOControl(n2)) {
            return false;
        }
        String string = new String(Character.toChars(n2));
        if (this.whatsNew.isOpen() || this.ddTour.active()) {
            return true;
        }
        if (this.colorPicker != null) {
            if (this.kitFocus != null) {
                this.kitFocus.charTyped(string);
            }
            return true;
        }
        if (!(this.overlayView == null || this.rowsOverlay() && this.editingString != null)) {
            if (this.kitFocus != null) {
                this.kitFocus.charTyped(string);
            }
            return true;
        }
        if (this.swallowCharsUntilMs != 0L) {
            boolean bl = System.currentTimeMillis() <= this.swallowCharsUntilMs;
            this.swallowCharsUntilMs = 0L;
            if (bl) {
                return true;
            }
        }
        if (this.capturing()) {
            return true;
        }
        if (this.kitFocus != null) {
            return this.kitFocus.charTyped(string);
        }
        if (this.editingString != null) {
            this.editingString.G(this.editingString.X() + string);
            return true;
        }
        if (this.overlayView == null && !Character.isWhitespace(n2)) {
            this.focusSearch();
            return this.searchField.charTyped(string);
        }
        return false;
    }

    private static String removeLast(String string) {
        return string.substring(0, string.offsetByCodePoints(string.length(), -1));
    }

    private boolean shiftDown() {
        try {
            long l2 = this.field_22787 != null && this.field_22787.method_22683() != null ? this.field_22787.method_22683().method_4490() : 0L;
            return l2 != 0L && (GLFW.glfwGetKey((long)l2, (int)340) == 1 || GLFW.glfwGetKey((long)l2, (int)344) == 1);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    @Override
    public void method_25419() {
        if (this.closing) {
            return;
        }
        try {
            this.scriptView.flush();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        this.stopEditing();
        this.cancelCapture();
        this.cancelDrag();
        this.closeContextMenu();
        this.commitFolderRename();
        this.closePicker();
        this.saveLayout();
        this.closing = true;
        this.closedAt = this.nanoTime();
        try {
            this.applyPendingConfigActions();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        super.method_25419();
    }

    @Override
    public void method_25432() {
        this.anims.clear();
        this.stopEditing();
        this.cancelCapture();
        this.cancelDrag();
        this.closeContextMenu();
        this.commitFolderRename();
        this.saveLayout();
        super.method_25432();
    }

    private static /* synthetic */ void lambda$drawOverlay$2() {
    }

    private /* synthetic */ void lambda$drawOverlay$1(Canvas canvas, UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.drawThemeMakerEditor(canvas, f2 / this.s, f3 / this.s, f4 / this.s, f5 / this.s);
    }

    private static /* synthetic */ void lambda$drawOverlay$0() {
    }

    private static /* synthetic */ void lambda$drawRow$2(BooleanSetting booleanSetting) {
        booleanSetting.setKeyHoldMode(!booleanSetting.isKeyHoldMode());
    }

    private /* synthetic */ void lambda$drawRow$1(BooleanSetting booleanSetting) {
        this.cancelCapture();
        this.bindingBool = booleanSetting;
    }

    private /* synthetic */ void lambda$drawRow$0(Setting setting, Module module) {
        this.openSettingMenu(setting, module);
    }

    private static /* synthetic */ float[] lambda$ensureLayout$1(String string) {
        return new float[]{30.0f, 104.0f};
    }

    static {
        QUICK_KEY = DropdownGpuClickGuiScreen.hj("\u5f68\u41e6\uc031\uc6c9\u0e9e\ucf49\u9e07\uad04\u8832", 314913471, 0x229D082C ^ CryptoVault.mask());
        FOLDER_PREFIX = DropdownGpuClickGuiScreen.hj("\ufa8c\ue976\u821e\u922e\ud6d9\ud036\uc298\uf678\uc09f\u766e\ufa9a", -775293286, 0x229D082C ^ CryptoVault.mask());
        PANELS = new Category[]{Category.CE, Category.vR, Category.lr, Category.Mr, Category.yZ, Category.Be, Category.ve, Category.rr, Category.fK, Category.hV};
        POS = new HashMap<String, float[]>();
        SCROLL = new HashMap<String, Float>();
        COLLAPSED = new LinkedHashSet<String>();
        EXPANDED = new LinkedHashSet<String>();
        laidOutW = -1.0f;
        laidOutH = -1.0f;
        lastLayout = "";
        Z_ORDER = new ArrayList<String>();
        separatedW = -1.0f;
        separatedH = -1.0f;
        separatedLayout = "";
        separatedCount = -1;
        MOD_KEY_PREFIX = new String[]{DropdownGpuClickGuiScreen.hj("\uea66\u7d1d\u01cf\ue9c6\ucbd5\u4cd8", 1099771046, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\udf1a\u4ea9\u269f\uc898\uf784", 2044674502, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u8ea2\uaf56\uda24\u95c5\uf53a", 1614817372, 0x229D082C ^ CryptoVault.mask()), DropdownGpuClickGuiScreen.hj("\u1e19\u0267\u0a26\u6183\ud452\u4f29\ud37f\u0756\u5be3\ua841", -1725926264, 0x229D082C ^ CryptoVault.mask())};
    }

    private static /* synthetic */ String hj(String string, int n2, int n3) {
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
    private record PanelDef(String key, String title, String icon, Category cat, String folderId, boolean quick) {
    }

    @Environment(value=EnvType.CLIENT)
    private final class PeopleBackendImpl
    implements PeopleView.Backend {
        private PeopleBackendImpl() {
        }

        @Override
        public List<PeopleView.Person> saved() {
            ArrayList<PeopleView.Person> arrayList = new ArrayList<PeopleView.Person>();
            for (a object : d.A().C()) {
                arrayList.add(new PeopleView.Person(object.P(), object.B(), true, c.A().A(object.B())));
            }
            for (com.ishland.scalablelux.e.a a2 : c.A().B()) {
                if (d.A().B(a2.A())) continue;
                arrayList.add(new PeopleView.Person(a2.N(), a2.A(), false, true));
            }
            arrayList.sort(Comparator.comparing(PeopleView.Person::name, String.CASE_INSENSITIVE_ORDER));
            return arrayList;
        }

        @Override
        public List<PeopleView.Person> nearby() {
            if (DropdownGpuClickGuiScreen.this.field_22787 == null || ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1687 == null) {
                return List.of();
            }
            ArrayList<PeopleView.Person> arrayList = new ArrayList<PeopleView.Person>();
            for (class_1657 class_16572 : ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1687.method_18456()) {
                if (((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1724 != null && class_16572.method_5667().equals(((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1724.method_5667())) continue;
                arrayList.add(new PeopleView.Person(class_16572.method_5477().getString(), class_16572.method_5667(), d.A().B(class_16572.method_5667()), c.A().A(class_16572.method_5667())));
            }
            arrayList.sort(Comparator.comparing(PeopleView.Person::name, String.CASE_INSENSITIVE_ORDER));
            return arrayList;
        }

        @Override
        public String toggleFriend(PeopleView.Person person) {
            if (d.A().B(person.uuid())) {
                d.A().B(person.uuid());
                return PeopleBackendImpl.ev("\u982f\u7f0b\u74fa\u347f\u3810\u2d0d\u3873\ufbf8\u4f4b\ufde1\ue929\u3ebd\u2240\u88eb\u8d29\ua346", -1984245995, 0x207D0C ^ CryptoVault.mask()) + person.name();
            }
            d.A().B(person.name(), person.uuid());
            return PeopleBackendImpl.ev("\u106f\u89bf\u5abc\ubc6d\u1317\u060a\ud54d\ua822\ue7d7\u8380\udbfd\udb89\u7678\u2c50\u0958\u7553", 558151973, 0x207D0C ^ CryptoVault.mask()) + person.name();
        }

        @Override
        public String toggleEnemy(PeopleView.Person person) {
            if (c.A().A(person.uuid())) {
                c.A().A(person.uuid());
                return PeopleBackendImpl.ev("\ue245\uc320\ue995\u1c9b\ud946\u845b\u46ca\u872e\u96e5\u00a3\u81b1\u6b12\uda65\ud544\uc3f6", 668391440, 0x207D0C ^ CryptoVault.mask()) + person.name();
            }
            c.A().A(person.name(), person.uuid());
            return PeopleBackendImpl.ev("\u2930\u4bdb\u0de9\ua206\u10a8\u8747\ua538\u9266\udbb4\uf661\u8ca5\ua762\uec14\u7de5", -609799588, 0x207D0C ^ CryptoVault.mask()) + person.name();
        }

        @Override
        public String remove(PeopleView.Person person) {
            if (person.friend()) {
                d.A().B(person.uuid());
            }
            if (person.enemy()) {
                c.A().A(person.uuid());
            }
            return PeopleBackendImpl.ev("\u715d\u2242\ubb1b\u383d\ue8be\u6e81\u08c8\u67c0\ud168", -897893292, 0x207D0C ^ CryptoVault.mask()) + person.name();
        }

        private static /* synthetic */ String ev(String string, int n2, int n3) {
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
    private final class DropdownConfigsBackend
    implements ConfigsView.Backend {
        private DropdownConfigsBackend() {
        }

        @Override
        public List<String> savedConfigs() {
            return DropdownGpuClickGuiScreen.this.savedConfigs;
        }

        @Override
        public ConfigsView.Info info(String string) {
            PrivateConfigManager.PrivateConfigInfo privateConfigInfo = DropdownGpuClickGuiScreen.this.cloudProfiles.get(string.toLowerCase(Locale.ROOT));
            return privateConfigInfo == null ? null : new ConfigsView.Info(privateConfigInfo.id, privateConfigInfo.gameMode, privateConfigInfo.active, privateConfigInfo.isPublic, privateConfigInfo.tags);
        }

        @Override
        public boolean signedIn() {
            return CloudConfigs.signedIn();
        }

        @Override
        public void load(String string) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u05cf\u2bc0\u21ee\u91bd\uef22\u1f71\u299a\u76c2\u34b7\u8162\ue53a\u9d79\u93ac\u0ef2\u9db3\u9b00\u5a6a\ue5e5\u4bc6\u0fea\uc699\u4ed0\uecbb\u5e03\u9ee1\u43a1\ub145\uc5de\ua14f\ue53c\ue4cb\u2ef9\u7cc2\ue3f1", -1595803757, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.profileLoadPending = true;
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uc174\uc98d\u8f23\u46a8\u6baa\u79b7\ub6c2\ueaba\u8b7c\ucb39\u8a24\u8cf9\u34ca", 498830976, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\u491b\ud96d\udf5b\u79b7\ue6ac\u8800\uc467\ud87a\ub9ec\u6775\u68ed\u41d2\u82a1\u9cc7\u0273\u5e79\u68b2", 1633919181, 0x5F0BCC13 ^ CryptoVault.mask());
            PrivateConfigManager.getInstance().loadConfig(string, true, bl -> {
                DropdownGpuClickGuiScreen.this.profileLoadPending = false;
                if (!bl.booleanValue()) {
                    DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uc3a3\u2d1c\ubd36\ue990\u44ca\ua779\ua8b1\u5a2a\u8a97\ub829\u84e3\ua798\u9df5\ue957\u4e57\u93d6\ub4a9\u9269\u7948\u857e", 1190964044, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\"";
                    return;
                }
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u6fc4\u8c9e\u94df\u97ae\u30ca\ue30d\ub939\u42b3\u7e36\ucf37\uc1f6\u9d72", 425389351, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\ue087\u0617\u16e2\ub647\u6455\ufd97\u38df\ud3a5\ub098\u9924\u86d1\u6144\u2243\u3e03\u8604\u3b44\u9941\u4b43\u4ba7\uaa16\u3349\u5843\u707e\u7b60\u403c\u240e\ufbf4\u91dd\uef02\u5e85\u8738\u5649\ub04a\u6079\u87b8\udfca\u88ae\uaf65\ud725\udd56\u5b82\u60e9\ue0b3\u9f92\u834b\u066d\u7c84\u1565\uf042\u3ca8\u7e03", -361302460, 0x5F0BCC13 ^ CryptoVault.mask());
                if (DropdownGpuClickGuiScreen.this.closing) {
                    PrivateConfigManager.getInstance().activateConfig(string, () -> {}, string -> {});
                } else {
                    DropdownGpuClickGuiScreen.this.pendingProfileActivationName = string;
                }
            }, string2 -> {
                DropdownGpuClickGuiScreen.this.profileLoadPending = false;
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ue804\u7fa1\u1dba\u9458\u0149\ue209\u4eca\u98bb\u2b22\ub1c2\u2cec\ub2cc\ueaec\u7bd2\u97cd\ub48a\u8a23", -113491416, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\uc7f9\u4ba0\u1d56\ucc64", 626803783, 0x5F0BCC13 ^ CryptoVault.mask()) + string2;
            });
        }

        @Override
        public void save(String string, boolean bl, List<String> list) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u2e18\u32a4\u8d94\u7f69\ub548\u90e8\ubbcd\u18b0\u9d91\uaf07\u7859\u67c7\u9878\ub08a\u063c\u18c2\u3cc3\uc94b\uaa12\u7d3b\ubd16\u2fba\ue46b\u345b\ub6f4\u3ba4\u9d2e\u2889\u6ee1\ue961\u7358\u26d0\u2b12\u8540\u3269\u41ed\u7516\u28b8\u8341\uf6f7\u7102\u9ec2\u7996\u92fd\u8e6e\u6a2f\u3b90\ud8e3\u52d3\u4e2d\u275d\u23b8\ub195\u620e\u2b52\u3004\u3be5\u198e\u1e64\u4963\u3f3e\u78a1\uba62\ub4a0\ufe54\uf2d6\ue2b3\u6d37\u8ef5\u6302\u1e9c\uf7ab\ud4fe\u1569\u99ff\ub6d9", -714284051, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            boolean bl2 = DropdownGpuClickGuiScreen.this.cloudProfiles.containsKey(string.toLowerCase(Locale.ROOT));
            if (!bl2 && GpuClickGuiScreen.cloudSlotsUsed(DropdownGpuClickGuiScreen.this.cloudProfiles) >= PremiumGate.cloudConfigLimit()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ua927\u7dec\u4839\u401f\ucce7\u6f85\u561a\uba1f\ub305\u8e43\u4274\u4596\u99be\uf8ee\uc978\u0b37\u676e\u5d6e\u9287\u7e7a\ud585\u1a8f\uc41c\u618f", 1367725491, 0x5F0BCC13 ^ CryptoVault.mask()) + PremiumGate.cloudConfigLimit() + DropdownConfigsBackend.yn("\u99a1\uc056\u7a49\u7d02\u9d83\u4214\ud8be\uc82e\u9555\uf38f\u331c\u6350\u8779\u8771\u6a28\u1236\u2337\u7666\ua219\u178b\uf00f\ucf62\u2ee7\uaba0", -1705668468, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.pendingProfileSaveName = string;
            DropdownGpuClickGuiScreen.this.pendingSavePublic = bl;
            DropdownGpuClickGuiScreen.this.pendingSaveTags = list == null ? List.of() : List.copyOf(list);
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u4a2c\ufabc\uc822\u2364\udf88\ue76c\u36fc\uf53b\ucdbd\u4dfc\u6950\ue295\uc37c", -765087920, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\"" + (bl ? DropdownConfigsBackend.yn("\ubb0f\ua90f\ua0d1\ub946\uc8f3\u5aff\uc10a\u6fca\u38df\u9800\u16f2\uffbf", -769810574, 0x5F0BCC13 ^ CryptoVault.mask()) : "") + (list != null && !list.isEmpty() ? DropdownConfigsBackend.yn("\u05e0\u3273\u2981\u8a42\uf765\ue1a7\u562e\u55b9\u38c6\u039b\uc1b3\u33a6\uca1b\u387dM\ubad8\uf68f", 806323396, 0x5F0BCC13 ^ CryptoVault.mask()) : "") + DropdownConfigsBackend.yn("\u9e25\u84ac\u98c2\ua221\u482d\ub2f2\u77c6\u09c9\ud058\u6877\ua32f\u9de4\ue53a\u8448\u4ac4\u711e\ua449\u3268\u6d68\ubf53\ub4f1\ufb6c\uff60\u0736", -2073225478, 0x5F0BCC13 ^ CryptoVault.mask());
        }

        @Override
        public void deleteCloud(String string) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ud060\ue895\ua059\u4af1\uef46\u6716\u73af\u5b5d\u5c0c\u26e5\u290b\u074c\u0f64\u6850\u5b4b\uc6f7\u2c27", -105559856, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uf659\ua139\u937b\u6c0e\u4e45\uabea\u5282\u60f3\ue42e\udb77\u1197\ubeb6\ua9b8\u8d6d", -1973838714, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\u8c36\u68a0\ufc5d\udb7f\u672d", 1348591691, 0x5F0BCC13 ^ CryptoVault.mask());
            DropdownGpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.deleteNamed(string), bl -> {
                DropdownGpuClickGuiScreen.this.configStatusMessage = bl != false ? DropdownConfigsBackend.yn("\u8dd9\u2916\uc857\ua1f5\u75ba\uf5ca\ue67b\uf379\u2460\uf7bb", 628244525, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\"" : DropdownConfigsBackend.yn("\u3278\uc14d\u7495\u09e6\u62b0\u6a18\ub8d3\u2d75\u5dc4\u7339\u944a\ubc6e\uac73\ub14f\u19e4\u84f2", -1742621635, 0x5F0BCC13 ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void setPublic(String string, boolean bl) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u72e8\u235b\u3831\u91c9\udd8d\u0d87\u145e\u20c7\ua9ef\u2016\ud082\u8b89\u0198\u8399\u55fe\u0af4\uad6f", 880737461, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u7e9d\u07fc\u99a5\u7ac6\u57a2\u7ae9\u195d\uf312\ube80\u1ba6\u9559\u53fa\udf5a\ue589\ubdde>\uc21d\ub5c3\u4110\u655b\u5439\u37fd\uba8b\u2b58", 1612416835, 0x5F0BCC13 ^ CryptoVault.mask());
            DropdownGpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.setPublic(string, bl), bl2 -> {
                DropdownGpuClickGuiScreen.this.configStatusMessage = bl2 != false ? "\"" + string + DropdownConfigsBackend.yn("\u16ba\u5c93\u3f3a\u620b\u64a2\ubf44\uc1db\u91df\uc4b0\u53bb\u8f45", 133846062, 0x5F0BCC13 ^ CryptoVault.mask()) + (bl ? DropdownConfigsBackend.yn("\u5487\u0bbb\u4437\u477d\ue570\uacb6\u241a\ua4db\u7f2a\u58d8", -54748634, 0x5F0BCC13 ^ CryptoVault.mask()) : DropdownConfigsBackend.yn("\u8588\u0740\u6089\ud533\u178a\u5a96\u17bc\u9f3d\uf05b\uc46c\u0f2c", -424240528, 0x5F0BCC13 ^ CryptoVault.mask())) : DropdownConfigsBackend.yn("\u3593\u23a4\u259b\uc850\u92c2\ucf98\u0f53\ub0d3\u62fb\ud4cc\u7d17\u9ae2\u74a3\uc2d2\u48a5\ue4b1\u695d\ue190\u9f36\u9353\u34a8\u3a33\ufaf4\u40c4\u6633\u09cf\u7bc9\u6da6\ua19f\ufc50", 1479493653, 0x5F0BCC13 ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void rename(String string, String string2) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u7877\u5a40\u2610\u9238\u7a91\u37cf\ue9ae\u0e55\u6bd8\u445b\u17b8\u2f93\u2273\u79df\uc2d2\ufe93", -1007717079, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u279d\uac92\ua6e4\u21ea\u101c\u3b30\u3256\ue361\u1d39\ud387\u70ea\u47db\u2d85\u4230\u5ff7", -2091922151, 0x5F0BCC13 ^ CryptoVault.mask());
            DropdownGpuClickGuiScreen.this.runCloudOp(() -> {
                String string3 = CloudConfigs.idForName(string);
                return string3 != null && CloudConfigs.renameById(string3, string2);
            }, bl -> {
                DropdownGpuClickGuiScreen.this.configStatusMessage = bl != false ? DropdownConfigsBackend.yn("\uca05\u1bf2\u9b5c\uf6cd\ue0a8\ub524\u4b8b\uf24b\u6ddb\ue872\u6f06\u98fc\u001a\u70d0", 647852935, 0x5F0BCC13 ^ CryptoVault.mask()) + string2 + "\"" : DropdownConfigsBackend.yn("\u20ab\ue58c\u5ee3\u4782\uc70a\u48c4\u9b23\u629c\uebff\uabf9\u6f5f\u6722\u50f5\u2fe4\udaa3\ud600\uba34", 1816789310, 0x5F0BCC13 ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void setTags(String string, List<String> list) {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u3145\ua768\uac2f\u5d12\u58c1\u399c\uabd4\u1bb1\ufa26\u5e3e\u6370\u06a4\u2107\u2cd6\ucc6b\u4ea3\u8251\u718c", -1464402377, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.runCloudOp(() -> {
                String string2 = CloudConfigs.idForName(string);
                return string2 != null && CloudConfigs.setTagsById(string2, list);
            }, bl -> {
                if (!bl.booleanValue()) {
                    DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u202c\uc608\uafd1\u6b51\u6bf9\u9107\u8f43\ubcf5\u0ca1\u015f\uac4f\u698c\ubddf\u5920f\uf4a9\ua9ff\u6733\u147c\u1888\u14fe\u464b\u3a8a\ua7d1\uaa2c", 1462048394, 0x5F0BCC13 ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.cloudError();
                }
            });
        }

        @Override
        public boolean premium() {
            return PremiumGate.isPremium();
        }

        @Override
        public void saveGenerated(String string, String string2, String string3, List<String> list, boolean bl, Consumer<Boolean> consumer) {
            String string4;
            String string5 = !CloudConfigs.signedIn() ? DropdownConfigsBackend.yn("\u9d29\u4bc7\ue13e\u93d2\uaef8\u4ebf\u28f9\u06cc\u94ee\uf35b\ue858\u99ad\u885d\uf848\udc9c\u3a24\u0c9a\ubcb2\u1919\u4cd5\u9828\u2a11\ubd23\u6ca9\uceed\ubb65\u9100\u55b1\uf8c2\u5dd1\ubbba\u7f7c\u4917\u05e0\u36d3\u4242\ub6fc\u5e6c\u5a0a\uff73\u5f60\uad76\ufafd\u55dc\u2646\u5466\u0904\udf47\u3e2c\uff36\ued71\u1116\u81ea\udbf5\uceb0\u0158\u91ba\u5a74\ufe1d\u1f8a\uf170\uf82e\uae8f\u50b0\ud25a\u84f5\ucc5f\ub1fc\ub85f\u8828\u727b\uf3eb\u1cc8\ub269\ubc37", -533921706, 0x5F0BCC13 ^ CryptoVault.mask()) : (DropdownGpuClickGuiScreen.this.cloudProfiles.containsKey(string.toLowerCase(Locale.ROOT)) ? DropdownConfigsBackend.yn("\ucf02\udde0\ue6f0\u3b44\u2f61\ua16f\ufffa\u7a9e\u0e5f\u4a37\u4090\u8d3a\u5bd8\u28eb\uba32\ud3f0\u4641\u823b\u2370\u2223\ub1f9\u9180\ubcbf\ue715\u0d27\u4a3f\ud8a3\u16d6\u0cdb\u3121\u9329\u0a1d\uf021\ud9e1\u1f27\u0c64\uddbe", 1689455925, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\ue15e\u1cee\u2097\u5acb\ub8c1\u63ed\u185b\u7d79\u62a8\u71bb\u7abb\u5e63\u6692\u51af\u0dc4\u7352\u6a04\ue34e\u700e\uf18c\ubb98\u270c\u5f2b\u1c9b", 313608967, 0x5F0BCC13 ^ CryptoVault.mask()) : (string4 = GpuClickGuiScreen.cloudSlotsUsed(DropdownGpuClickGuiScreen.this.cloudProfiles) >= PremiumGate.cloudConfigLimit() ? DropdownConfigsBackend.yn("\u26f1\ua0b5\uc79c\u6e66\u659d\ub5eb\u2b71\u961b\ue221\uee14\u0a4f\u2654\u0715\u484e\u2893\udeb6\u40a4\uacc0\u692a\ue854\ua705\u7a77\u4e44\u29dd", 1521298007, 0x5F0BCC13 ^ CryptoVault.mask()) + PremiumGate.cloudConfigLimit() + DropdownConfigsBackend.yn("\u1b42\u093a\ub29a\u0bcb\uf445\ud1a3\u9077\ue073\u7a91\u0a74\ucb4e\uf03b\ufcd0\u8e9c\ud72f\uce72\uc81f\u748e\uf3ee\u4053\uf81f\ue72c\u23ba\ua00e", 473182575, 0x5F0BCC13 ^ CryptoVault.mask()) : null));
            if (string4 != null) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = string4;
                consumer.accept(false);
                return;
            }
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ued39\u3a6f\ubde9\ubb16\ucf01\u4634\u0179\ufa43\u2713\u6347\u4a34\udd74", -1601162126, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\uf13c\u5ea7\ud63c\u5375\u76d2\ue370", 1671853515, 0x5F0BCC13 ^ CryptoVault.mask());
            DropdownGpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.saveNamed(string, string2, false, string3, DropdownConfigsBackend.yn("\uc1f1\u7545\u1497\ufc4c\ucf2f\u6881\u50b9\u501f\u867f\u954e\u01fa\uf01e\uf542\u700e\u3ae2\ue43e\u4b4f\u7e69\u68da\ucba7\u490f\u127c\ua878\u7f3a", 697460356, 0x5F0BCC13 ^ CryptoVault.mask()), list) != null, bl2 -> {
                consumer.accept((Boolean)bl2);
                if (!bl2.booleanValue()) {
                    DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u4ccc\u47c9\u53d0\ued46\u5079\u7d90\u786d\u7de6\u8bd8\u3936\u1cc2\ud1c4\ud932\uc88a\u7f94\u9a40\u1afa", -2083277926, 0x5F0BCC13 ^ CryptoVault.mask()) + DropdownGpuClickGuiScreen.cloudError();
                    return;
                }
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u1bfb\u5f23\u8ea1\u37ac\u4b71\u0dd8\ud45b\u5b82", -1241697989, 0x5F0BCC13 ^ CryptoVault.mask()) + string + DropdownConfigsBackend.yn("\ua6aa\uf3a9\u2384\u73d3\ub325\u259a\ua73b\ue461\u3240\uec7c\u35a0\ue6aa\u3521\u4699\u71f1\u7ee6\u518d\u0b5f\u5d6d\u49a4\uc05a\u5ff7\u50f8\u7034\uc0a3\uc94a\uc854\u65a5", 269139887, 0x5F0BCC13 ^ CryptoVault.mask());
                if (bl) {
                    this.load(string);
                }
            });
        }

        @Override
        public boolean canDeleteLocal(String string) {
            return ConfigManager.isSaveLocalConfig() && !DropdownConfigsBackend.yn("\u8b01\u2c84\u75bf\uf6b5\ucaa4\udca5\u7ccc\u2c4d\u2037", -1300251993, 0x5F0BCC13 ^ CryptoVault.mask()).equalsIgnoreCase(string);
        }

        @Override
        public void deleteLocal(String string) {
            try {
                if (!ConfigManager.getInstance().delete(string)) {
                    DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ufc2a\u4cef\ub460\u419a\u5f84\u6147\u40cd\u27ae\u9d9b\ue0ab\ue0d4\u3284\udbbb\ua4c1\ud924\u9b9b\ua48f\u1d26\u49c2", -418703867, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\"";
                    return;
                }
                DropdownGpuClickGuiScreen.this.refreshSavedConfigs();
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u7847\u6f15\ucf11\u940b\ue175\u54e3\u23e5\u9608\u8ed8\ue2c9\ua095\uf78f\u47f5", -1543428243, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\"";
            }
            catch (Exception exception) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uf08b\ud141\ua996\ub463\u7c47\uf571\u4ab8\u1b89\u2fb5\udbf7\ub8bc\ub325\u491a\u0c07\u42a2\u2718\ud444", 489673073, 0x5F0BCC13 ^ CryptoVault.mask()) + exception.getMessage();
            }
        }

        @Override
        public int cloudLimit() {
            return PremiumGate.cloudConfigLimit();
        }

        @Override
        public int cloudCount() {
            return GpuClickGuiScreen.cloudSlotsUsed(DropdownGpuClickGuiScreen.this.cloudProfiles);
        }

        @Override
        public String status() {
            return DropdownGpuClickGuiScreen.this.configStatusMessage;
        }

        @Override
        public void setStatus(String string) {
            DropdownGpuClickGuiScreen.this.configStatusMessage = string == null ? "" : string;
        }

        @Override
        public void closeMenu() {
            DropdownGpuClickGuiScreen.this.method_25419();
        }

        @Override
        public String loadedConfig() {
            return ConfigManager.getInstance().getLoadedConfigName();
        }

        @Override
        public boolean unsavedChanges() {
            return ConfigManager.getInstance().hasUnsavedChanges();
        }

        @Override
        public void overwrite() {
            if (!CloudConfigs.signedIn()) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ucb92\u3d0b\ufea0\u8494\u021d\uc831\u6959\ua1dc\u20bd\u8c77\ue48c\ue460\uca29\u1ede\ue6a1\udd37\u4d02\u20ff\u45d5\u8d6b\u5bed\u9188\u4e71\u9f33\u9584\u64c8", 1249606581, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            if (DropdownGpuClickGuiScreen.this.profileLoadPending) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\ufdb4\ue224\ub3fe\u289a\u2480\u697e\u6d1c\uee40\u6cab\u7a92\u1aac\u8dd5\uf116\u0913\u39fc\u41ca\u1242\ufe93\uf9b8\ua93a\u8e25\ua5b5\u4024\ue0a6\ub0e6\u7516\u30ad\u63d1\u6be2\u13e2\u03a5\u6766\u0ae9\u0fe2\udcb0\ua1f7\u2adc\u06d5\u4b22\u13dd\u3d74", -1976631006, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            String string2 = ConfigManager.getInstance().getLoadedConfigName();
            if (string2 == null) {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uecd3\u5bec\u0826\u8993\u6c49\uefca\ua237\u13b2\ua788\ufd0d\ubb84\ub1c9\ube6f\udef4\u81ef\u2681\u8f3e\u2c2d\u75d4\u7376\u1f06\ub9d3\udba2\uabcc\u9b30\u9d29\ub096\u2d95\ub61c\u1e35\u087a\u9d6b\ub0bf\u8a54\u06ce\uea0b\u779e\ude24\u6c3c\u441f\u0920\uc1c8\u3a8c\uf68c\u1395\ue020\u8e95\u7a4e\u09af\u82a0\ufff4\uee14\uee38", -815459915, 0x5F0BCC13 ^ CryptoVault.mask());
                return;
            }
            DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\uca3a\u81f7\u9694\u63a2\u1e5b\ue7b2\u0175\ubb26\ub477\u075b\u107f\u19e1\u661f\ucc64\uaf73\ucf8d\u24b1\ue6de\u67c3\ub298\ub68d\ua328\u0815\u8aaf\u7666\u3d86\u6a28\u8b5e", -905662772, 0x5F0BCC13 ^ CryptoVault.mask()) + string2 + DropdownConfigsBackend.yn("\uff8c\u5fb3\u00df\u30bd\ucfde\udb82\u1b14", 1422368137, 0x5F0BCC13 ^ CryptoVault.mask());
            PrivateConfigManager.getInstance().overwriteLoaded(string -> {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u80be\uee83\ufb51\u16a6\uc8f1\u31bf\u2007\u71f2\udfaa\ub477\u783c\u5644\u1f2a\ua479\u7fbb\u378e\uf279\u66d3\u745d\u82a0\u7d39\u5296\ufe2b\u52e3\uaa9c\u96f0\uda5e", 329281753, 0x5F0BCC13 ^ CryptoVault.mask()) + string + "\".";
                DropdownGpuClickGuiScreen.this.refreshSavedConfigs();
            }, string -> {
                DropdownGpuClickGuiScreen.this.configStatusMessage = DropdownConfigsBackend.yn("\u8db2\ud416\u1b62\u02da\ua176\ud59d\ua558\u93e4\u6dcc\u29f9\u7c53\uf3a1\ub33e\u7ae0\ufb38\u1f24\uf6e9\u84b7\ud976\u9d6e", 2041280412, 0x5F0BCC13 ^ CryptoVault.mask()) + string;
            });
        }

        private static /* synthetic */ String yn(String string, int n2, int n3) {
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
    private final class DropdownHost
    implements UiHost {
        private DropdownHost() {
        }

        @Override
        public void hit(float f2, float f3, float f4, float f5, Runnable runnable) {
            DropdownGpuClickGuiScreen.this.addHostHit(DropdownGpuClickGuiScreen.this.hits, f2, f3, f4, f5, runnable);
        }

        @Override
        public void hitSecondary(float f2, float f3, float f4, float f5, Runnable runnable) {
            DropdownGpuClickGuiScreen.this.addHostHit(DropdownGpuClickGuiScreen.this.rightHits, f2, f3, f4, f5, runnable);
        }

        @Override
        public void beginHitClip(float f2, float f3, float f4, float f5) {
            DropdownGpuClickGuiScreen.this.hcx = f2;
            DropdownGpuClickGuiScreen.this.hcy = f3;
            DropdownGpuClickGuiScreen.this.hcw = f4;
            DropdownGpuClickGuiScreen.this.hch = f5;
            DropdownGpuClickGuiScreen.this.hostClip = true;
        }

        @Override
        public void endHitClip() {
            DropdownGpuClickGuiScreen.this.hostClip = false;
        }

        @Override
        public boolean hovered(float f2, float f3, float f4, float f5, float f6, float f7) {
            if (DropdownGpuClickGuiScreen.this.hostClip && (f2 < DropdownGpuClickGuiScreen.this.hcx || f3 < DropdownGpuClickGuiScreen.this.hcy || f2 > DropdownGpuClickGuiScreen.this.hcx + DropdownGpuClickGuiScreen.this.hcw || f3 > DropdownGpuClickGuiScreen.this.hcy + DropdownGpuClickGuiScreen.this.hch)) {
                return false;
            }
            return f2 >= f4 && f3 >= f5 && f2 <= f4 + f6 && f3 <= f5 + f7;
        }

        @Override
        public int accent() {
            return DropdownGpuClickGuiScreen.this.accent;
        }

        @Override
        public int textColor() {
            return DropdownGpuClickGuiScreen.this.text;
        }

        @Override
        public int mutedColor() {
            return DropdownGpuClickGuiScreen.this.muted;
        }

        @Override
        public int muted2Color() {
            return DropdownGpuClickGuiScreen.this.muted;
        }

        @Override
        public int titleColor() {
            return DropdownGpuClickGuiScreen.this.text;
        }

        @Override
        public int lineColor() {
            return DropdownGpuClickGuiScreen.argb(26.0f, 0xFFFFFF);
        }

        @Override
        public int cardFill() {
            return HitboxConfigScreen752809.a(HitboxConfigScreen752809.aj, DropdownGpuClickGuiScreen.argb(92.0f, DropdownGpuClickGuiScreen.this.panelBase));
        }

        @Override
        public float cornerScale() {
            return HitboxConfigScreen752809.Z == null ? 1.0f : Math.max(0.0f, Math.min(3.0f, (float)HitboxConfigScreen752809.Z.B() / 10.0f));
        }

        @Override
        public void panel(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
            DropdownGpuClickGuiScreen.this.panel(new Canvas(builder), f2 / DropdownGpuClickGuiScreen.this.s, f3 / DropdownGpuClickGuiScreen.this.s, f4 / DropdownGpuClickGuiScreen.this.s, f5 / DropdownGpuClickGuiScreen.this.s, f6 / DropdownGpuClickGuiScreen.this.s, 1.0f);
        }

        @Override
        public int controlFill(int n2) {
            return Math.max(0, Math.min(255, n2)) << 24 | DropdownGpuClickGuiScreen.mix(DropdownGpuClickGuiScreen.this.panelBase | 0xFF000000, -1, 0.08f) & 0xFFFFFF;
        }

        @Override
        public long now() {
            return DropdownGpuClickGuiScreen.this.nanoTime();
        }

        @Override
        public float frameDelta() {
            return DropdownGpuClickGuiScreen.this.dt;
        }

        @Override
        public float anim(String string, float f2, float f3) {
            return DropdownGpuClickGuiScreen.this.ease(DropdownHost.te("\u1ee4\uf455\u9e07\ue683\ue680\uff52\u2f1d", 1498637040, 0x5728953 ^ CryptoVault.mask()) + string, f2, f3);
        }

        @Override
        public double pressX() {
            return DropdownGpuClickGuiScreen.this.pressX;
        }

        @Override
        public double pressY() {
            return DropdownGpuClickGuiScreen.this.pressY;
        }

        @Override
        public void focus(UiTextTarget uiTextTarget) {
            if (uiTextTarget == DropdownGpuClickGuiScreen.this.kitFocus) {
                return;
            }
            if (DropdownGpuClickGuiScreen.this.kitFocus != null) {
                DropdownGpuClickGuiScreen.this.kitFocus.onBlur();
            }
            DropdownGpuClickGuiScreen.this.kitFocus = uiTextTarget;
            if (uiTextTarget != null) {
                uiTextTarget.onFocus();
            }
        }

        @Override
        public UiTextTarget focused() {
            return DropdownGpuClickGuiScreen.this.kitFocus;
        }

        @Override
        public void captureKey(IntConsumer intConsumer) {
            DropdownGpuClickGuiScreen.this.kitFocus = null;
            DropdownGpuClickGuiScreen.this.kitKeyCapture = intConsumer;
        }

        @Override
        public boolean capturingKey() {
            return DropdownGpuClickGuiScreen.this.kitKeyCapture != null;
        }

        @Override
        public String clipboard() {
            return DropdownGpuClickGuiScreen.this.field_22787 != null && ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1774 != null ? ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1774.method_1460() : "";
        }

        @Override
        public void setClipboard(String string) {
            if (DropdownGpuClickGuiScreen.this.field_22787 != null && ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1774 != null) {
                ((DropdownGpuClickGuiScreen)DropdownGpuClickGuiScreen.this).field_22787.field_1774.method_1455(string);
            }
        }

        private static /* synthetic */ String te(String string, int n2, int n3) {
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
    private record Hit(float x, float y, float w, float h, Runnable action) {
        boolean contains(double d2, double d3) {
            return d2 >= (double)this.x && d3 >= (double)this.y && d2 <= (double)(this.x + this.w) && d3 <= (double)(this.y + this.h);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private final class Canvas {
        final UiDisplayList.Builder b;
        float alpha = 1.0f;
        boolean hoverSuppressed;
        private final Deque<float[]> clips = new ArrayDeque<float[]>();

        Canvas(UiDisplayList.Builder builder) {
            this.b = builder;
        }

        float px(float f2) {
            return f2 * DropdownGpuClickGuiScreen.this.s;
        }

        float px2(float f2) {
            return f2 * DropdownGpuClickGuiScreen.this.s;
        }

        int col(int n2) {
            int n3 = Math.round((float)(n2 >>> 24 & 0xFF) * this.alpha);
            return n3 << 24 | n2 & 0xFFFFFF;
        }

        void rect(float f2, float f3, float f4, float f5, float f6, int n2) {
            this.b.rect(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, f6 * DropdownGpuClickGuiScreen.this.s, this.col(n2));
        }

        void circle(float f2, float f3, float f4, int n2) {
            this.b.circle(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, this.col(n2));
        }

        void gradient(float f2, float f3, float f4, float f5, float f6, int n2, int n3, int n4, int n5) {
            this.b.gradient(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, f6 * DropdownGpuClickGuiScreen.this.s, this.col(n2), this.col(n3), this.col(n4), this.col(n5));
        }

        void border(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.b.border(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, f6 * DropdownGpuClickGuiScreen.this.s, f7, this.col(n2));
        }

        void shadow(float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2) {
            this.b.shadow(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, f6 * DropdownGpuClickGuiScreen.this.s, f7 * DropdownGpuClickGuiScreen.this.s, f8 * DropdownGpuClickGuiScreen.this.s, this.col(n2));
        }

        void glass(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.b.glass(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, f6 * DropdownGpuClickGuiScreen.this.s, f7, this.col(n2));
        }

        void text(float f2, float f3, float f4, int n2, String string) {
            this.text(f2, f3, f4, n2, string, UiDrawCommand.TextWeight.REGULAR);
        }

        void text(float f2, float f3, float f4, int n2, String string, UiDrawCommand.TextWeight textWeight) {
            this.b.text(f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, this.col(n2), string, textWeight);
        }

        void textMid(float f2, float f3, float f4, float f5, int n2, String string, UiDrawCommand.TextWeight textWeight) {
            float f6 = GpuUiFontMetrics.topInBox(f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s);
            this.b.text(f2 * DropdownGpuClickGuiScreen.this.s, f6, f5 * DropdownGpuClickGuiScreen.this.s, this.col(n2), string, textWeight);
        }

        float tw(String string, float f2, UiDrawCommand.TextWeight textWeight) {
            return GpuUiRenderer.textWidth(string, f2 * DropdownGpuClickGuiScreen.this.s, textWeight) / DropdownGpuClickGuiScreen.this.s;
        }

        void icon(String string, float f2, float f3, float f4, int n2) {
            ClassicIcons.draw(this.b, string, f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, this.col(n2));
        }

        void chevron(boolean bl, float f2, float f3, float f4, int n2) {
            UiIcons.draw(this.b, bl ? UiIcons.Icon.CHEVRON_DOWN : UiIcons.Icon.CHEVRON_RIGHT, f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, this.col(n2));
        }

        void beginClip(float f2, float f3, float f4, float f5) {
            float f6 = f2 * DropdownGpuClickGuiScreen.this.s;
            float f7 = f3 * DropdownGpuClickGuiScreen.this.s;
            float f8 = f4 * DropdownGpuClickGuiScreen.this.s;
            float f9 = f5 * DropdownGpuClickGuiScreen.this.s;
            float[] fArray = this.clips.peek();
            if (fArray != null) {
                float f10 = Math.max(f6, fArray[0]);
                float f11 = Math.max(f7, fArray[1]);
                float f12 = Math.min(f6 + f8, fArray[0] + fArray[2]);
                float f13 = Math.min(f7 + f9, fArray[1] + fArray[3]);
                f6 = f10;
                f7 = f11;
                f8 = Math.max(0.0f, f12 - f10);
                f9 = Math.max(0.0f, f13 - f11);
            }
            this.clips.push(new float[]{f6, f7, f8, f9});
            this.b.clipPush(f6, f7, f8, f9, 0.0f);
        }

        void endClip() {
            if (!this.clips.isEmpty()) {
                this.clips.pop();
            }
            this.b.clipPop();
        }

        float[] clip() {
            return this.clips.peek();
        }

        boolean hov(float f2, float f3, float f4, float f5) {
            if (this.hoverSuppressed) {
                return false;
            }
            if (!DropdownGpuClickGuiScreen.this.ready || !(this.alpha > 0.2f)) {
                return false;
            }
            float[] fArray = this.clips.peek();
            if (fArray != null && ((float)DropdownGpuClickGuiScreen.this.mx < fArray[0] || (float)DropdownGpuClickGuiScreen.this.my < fArray[1] || (float)DropdownGpuClickGuiScreen.this.mx > fArray[0] + fArray[2] || (float)DropdownGpuClickGuiScreen.this.my > fArray[1] + fArray[3])) {
                return false;
            }
            double d2 = f2 * DropdownGpuClickGuiScreen.this.s;
            double d3 = f3 * DropdownGpuClickGuiScreen.this.s;
            return (double)DropdownGpuClickGuiScreen.this.mx >= d2 && (double)DropdownGpuClickGuiScreen.this.my >= d3 && (double)DropdownGpuClickGuiScreen.this.mx <= d2 + (double)(f4 * DropdownGpuClickGuiScreen.this.s) && (double)DropdownGpuClickGuiScreen.this.my <= d3 + (double)(f5 * DropdownGpuClickGuiScreen.this.s);
        }

        void hit(float f2, float f3, float f4, float f5, Runnable runnable) {
            this.addHit(DropdownGpuClickGuiScreen.this.hits, f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, runnable);
        }

        void rhit(float f2, float f3, float f4, float f5, Runnable runnable) {
            this.addHit(DropdownGpuClickGuiScreen.this.rightHits, f2 * DropdownGpuClickGuiScreen.this.s, f3 * DropdownGpuClickGuiScreen.this.s, f4 * DropdownGpuClickGuiScreen.this.s, f5 * DropdownGpuClickGuiScreen.this.s, runnable);
        }

        private void addHit(List<Hit> list, float f2, float f3, float f4, float f5, Runnable runnable) {
            if (this.alpha < 0.25f) {
                return;
            }
            float[] fArray = this.clips.peek();
            if (fArray != null) {
                float f6 = Math.min(f2 + f4, fArray[0] + fArray[2]);
                float f7 = Math.min(f3 + f5, fArray[1] + fArray[3]);
                f2 = Math.max(f2, fArray[0]);
                f3 = Math.max(f3, fArray[1]);
                f4 = f6 - f2;
                f5 = f7 - f3;
                if (f4 <= 0.0f || f5 <= 0.0f) {
                    return;
                }
            }
            list.add(new Hit(f2, f3, f4, f5, runnable));
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record ScrollZone(float x, float y, float w, float h, String cat) {
        boolean contains(double d2, double d3) {
            return d2 >= (double)this.x && d3 >= (double)this.y && d2 <= (double)(this.x + this.w) && d3 <= (double)(this.y + this.h);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record Grip(float x, float y, float w, float h, String key) {
        boolean contains(double d2, double d3) {
            return d2 >= (double)this.x && d3 >= (double)this.y && d2 <= (double)(this.x + this.w) && d3 <= (double)(this.y + this.h);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record RowRect(String panelKey, Module module, int index, float x, float y, float w, float h) {
    }

    @Environment(value=EnvType.CLIENT)
    private record ModuleView(List<Section> sections, int total, boolean anyModified) {
    }

    @Environment(value=EnvType.CLIENT)
    private static final class RowPress
    implements Runnable {
        final Module module;
        final String panelKey;
        final Runnable click;

        RowPress(Module module, String string, Runnable runnable) {
            this.module = module;
            this.panelKey = string;
            this.click = runnable;
        }

        @Override
        public void run() {
            this.click.run();
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record Section(String name, List<Setting> rows, String key, Map<Setting, Integer> depth) {
        Section(String string, List<Setting> list) {
            this(string, list, null, Map.of());
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record MenuItem(String label, boolean checked, boolean enabled, boolean danger, Runnable action) {
        static MenuItem divider() {
            return new MenuItem(null, false, false, false, null);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record Util(String icon, String label, Runnable action, boolean active) {
    }
}

