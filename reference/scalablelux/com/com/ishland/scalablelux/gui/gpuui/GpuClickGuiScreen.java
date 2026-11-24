/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_1657
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_3675
 *  net.minecraft.class_437
 *  org.lwjgl.glfw.GLFW
 */
package com.ishland.scalablelux.gui.gpuui;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.SlAuth;
import com.ishland.scalablelux.config.ClientPrefs;
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
import com.ishland.scalablelux.gui.gpuui.DropdownGpuClickGuiScreen;
import com.ishland.scalablelux.gui.gpuui.KeybindsView;
import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.gpuui.framework.GcsCategoryIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiAuroraMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiBounds;
import com.ishland.scalablelux.gui.gpuui.framework.UiClassicMistMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiElement;
import com.ishland.scalablelux.gui.gpuui.framework.UiFogMaterial;
import com.ishland.scalablelux.gui.gpuui.framework.UiGlass;
import com.ishland.scalablelux.gui.gpuui.framework.UiIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiPanel;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
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
import com.ishland.scalablelux.gui.gpuui.kit.TouchInput;
import com.ishland.scalablelux.gui.gpuui.kit.UiColorPicker;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextTarget;
import com.ishland.scalablelux.gui.gpuui.kit.WhatsNewPanel;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiClipMask;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiMaterialRenderer;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuFluidSimulation;
import com.ishland.scalablelux.gui.gpuui.render.effects.GpuUiEffectPasses;
import com.ishland.scalablelux.gui.new_ui.CrosshairGrid;
import com.ishland.scalablelux.gui.new_ui.HudEditorScreen;
import com.ishland.scalablelux.gui.overlay.OverlayCompositor;
import com.ishland.scalablelux.gui.overlay.OverlayWindow;
import com.ishland.scalablelux.gui.sound.UiSounds;
import com.ishland.scalablelux.manager.ModuleManager;
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
import com.ishland.scalablelux.module.hooks.CrosshairMakerSource;
import com.ishland.scalablelux.module.hooks.FileDropHandler;
import com.ishland.scalablelux.render.ThumbnailCache;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import com.ishland.scalablelux.util.ModuleAccess;
import com.ishland.scalablelux.util.PerfProbe;
import com.ishland.scalablelux.util.PremiumGate;
import com.ishland.scalablelux.util.ScissorClamp;
import java.awt.Color;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import me.sootysplash.box.render.HitboxKeyBinding655023;
import me.sootysplash.box.render.HitboxRenderStateReset726175;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_3675;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public class GpuClickGuiScreen
extends class_437 {
    private static volatile String lastCloudConfigStatusMessage = "";
    private static final Category[] NAV_CATEGORIES = new Category[]{Category.CE, Category.vR, Category.lr, Category.Mr, Category.yZ, Category.Be, Category.ve, Category.rr, Category.fK};
    private static final int DEFAULT_TITLE = -591879;
    private static final int DEFAULT_ACCENT = -13638657;
    private static final int KNOB_WHITE = -723208;
    private int titleColor = -591879;
    private int controlRgb = 0xFFFFFF;
    private int accent2 = -13638657;
    private boolean gradientAccents;
    private int toggleOnColor = -13638657;
    private int sliderFillColor = -13638657;
    private int hoverRgb = 0xFFFFFF;
    private int selectionRing = -13638657;
    private float hoverK = 1.0f;
    private float panelOpacityK = 1.0f;
    private boolean showSeparators = true;
    private boolean showSidebarSeparator = true;
    private boolean showLogoSeparator = true;
    private boolean showRims = true;
    private boolean lg;
    private boolean floatMaterial;
    private boolean floatMist;
    private float floatBlur;
    private int floatTint;
    private String floatTheme = "";
    private float glassBlurValue = 16.0f;
    private float glassOpacityK = 1.0f;
    private int panelTintOverride;
    private int rimRgb = 0xFFFFFF;
    private float rimAlphaK = 1.0f;
    private float richness = 0.0f;
    private int textColor = -920588;
    private int mutedColor = -6512218;
    private int muted2Color = -10065040;
    private int toggleOffColor = -14013393;
    private int lineColor = 0x1AFFFFFF;
    private int lineBorderColor = 0x1FFFFFFF;
    private int lineSubtleColor = 0xBFFFFFF;
    private int railBg = -1945828090;
    private int listBg = 1376258313;
    private int cardBg = 0x47000000;
    private int cardBorder = 0x29FFFFFF;
    private int outerBorder = 0x38FFFFFF;
    private final List<Hit> hits = new ArrayList<Hit>();
    private final List<Hit> moduleCardRightHits = new ArrayList<Hit>();
    private boolean secondaryIsMiddle;
    private final UiElement inputRoot = new UiElement(GpuClickGuiScreen.bf("\u5319\u3ad0\uc082\u7739\u03bf\u6f5f\u8cda\uf66e\u0c3c\u90e2\u0f7e\uce7b\uaeee\ub69e", 515288344, 0x1B1EF9FC ^ CryptoVault.mask()));
    private final UiPanel headerPanel = new UiPanel(GpuClickGuiScreen.bf("\udfc3\u1d1c\uc9ca\u6017\u8832\u0b6a\uc624\uef04\ub2c4", -24351872, 0x1B1EF9FC ^ CryptoVault.mask()), 0, 15.0f);
    private final List<ActionElement> inputNodes = new ArrayList<ActionElement>();
    private View view = View.MODULES;
    private SettingsTab settingsTab = SettingsTab.CLIENT_CONTROLS;
    private Category category = Category.CE;
    private Module selected;
    private int settingsScroll;
    private float hotkeyScroll;
    private float hotkeyScrollShown;
    private float railPillY = Float.NaN;
    private float railPillH = 29.0f;
    private float topPillX;
    private float topPillW;
    private float topPillTX;
    private float topPillTW;
    private boolean topPillShow;
    private long catalogSwitchAt;
    private String catalogKey = "";
    private double moduleInspectorScrollRemainder;
    private float appearanceScroll;
    private float settingsScrollShown;
    private float appearanceScrollShown;
    private boolean settingsDrawerOpen;
    private boolean themesDrawerOpen;
    private boolean themeMakerOpen;
    private final ThemeMakerView themeMaker = new ThemeMakerView();
    private final OnboardingTour tour = new OnboardingTour(this::studioUnlocked);
    protected final WhatsNewPanel whatsNew = new WhatsNewPanel();
    private final float[] tourRailRect = new float[4];
    private final float[] tourCatalogRect = new float[4];
    private final float[] tourMacrosRect = new float[4];
    private final float[] tourScriptsRect = new float[4];
    private final float[][] tourNavRects = new float[5][4];
    private float drawerRegionX;
    private float drawerRegionY;
    private float drawerRegionW;
    private float drawerRegionH;
    private String newConfigName = "";
    private boolean newConfigFocused;
    private boolean closing;
    private final UiTextField searchField = new UiTextField(GpuClickGuiScreen.bf("\u9011\u2297\ub178\ud002\u5554\ubb60\u5560\ua821\u903c\ubd5c\u412f\u70ca\u3698\u1a51\ufff6\u9456\u9b66\ufad9\u5218\udd19\u2bec\u3f22\uc10a\u851e\ueff2\u7e1b\ua4a8\u6be1\u9b36\u2459\ub322\u1a97\u1dbd\uf836\ud365\ub366\u1fb4\u4682\ubdc5\uce8b\ucf27\u6d70\u84a3\ubc87\ubd05\u29d5\ud80f\u88cd\ue478\u685c\u8d98", 241878128, 0x1B1EF9FC ^ CryptoVault.mask())).maxLength(48).bare(true).padding(34.0f, 34.0f).fontSize(12.5f).onChange(this::onSearchChanged).onSubmit(() -> this.activateNav(false));
    protected static ModuleFilter moduleFilter = ModuleFilter.ALL;
    protected static ModuleSort moduleSort = ModuleSort.DEFAULT;
    private List<CatalogEntry> catalogEntries = List.of();
    private String catalogCacheKey = "";
    private long catalogCacheState;
    private long catalogCacheAt;
    private int navIndex = -1;
    private boolean searchScoped;
    private boolean navVisible;
    private boolean navScrollPending;
    private int lastCatalogCols = 1;
    private List<CatalogSections.Group> catalogGroups = List.of();
    private CatalogSections.Layout lastLayout;
    private float lastCatalogRowPitch = 1.0f;
    private float lastCatalogViewH;
    private String folderId;
    private String renamingFolderId;
    private final UiTextField folderNameField = new UiTextField(GpuClickGuiScreen.bf("\u82ae\u96a8\ucb1b\u8778\u36f7\ue504\u435c\u6da4\ua683\ud2dc\u6ef3\u424b\u845b\u9dd9", -1691687672, 0x1B1EF9FC ^ CryptoVault.mask())).maxLength(24).fontSize(13.0f).onSubmit(this::commitFolderRename).onBlur(this::commitFolderRename);
    private boolean deleteFolderArmed;
    private long deleteFolderArmedAt;
    private float restoreCatalogScroll = -1.0f;
    private boolean catalogDrawn;
    private static boolean quickCollapsed;
    private String rowLabelPrefix;
    private CardPress pendingCardPress;
    private double pendingPressX;
    private double pendingPressY;
    private Module dragModule;
    private List<Module> dragVisible = List.of();
    private int dragDropIndex = -1;
    private float dragPointerX;
    private float dragPointerY;
    private boolean catalogReorderable;
    private List<Module> lastCatalogModules = List.of();
    private List<MenuItem> contextItems;
    private String contextTitle;
    private float contextX;
    private float contextY;
    private final UiTextField settingsFilter = new UiTextField(GpuClickGuiScreen.bf("\u32b0\u95c5\u0c84\ubc33\u0ed4\u8588\u471d\uf76c\uf24d\u99eb\u45a2\u7f3b\u9f50\ud30f\ua517\ub3f2\u8afb\ub9d4\uf710\ub365", -202005736, 0x1B1EF9FC ^ CryptoVault.mask())).maxLength(40).fontSize(10.5f).onChange(() -> {
        this.settingsScroll = 0;
    });
    private Module settingsFilterFor;
    protected static final Set<String> COLLAPSED_GROUPS;
    private boolean resetAllArmed;
    private long resetAllArmedAt;
    protected final UiColorPicker settingPicker = new UiColorPicker();
    protected int pickerOpenedWith;
    private int currentAccent = -13638657;
    private Setting draggingSlider;
    private boolean draggingSliderQuick;
    private float sliderX;
    private float sliderW;
    protected ColorSetting colorPicker;
    protected CrosshairMakerSource crosshairMaker;
    private boolean[] crosshairGrid;
    private boolean crosshairMirror;
    private int crosshairPaintValue = -1;
    private float chGridX;
    private float chGridY;
    private float chCellPx;
    private StringSetting editingString;
    private HitboxKeyBinding655023 binding;
    private BooleanSetting bindingBool;
    private int capturedKey = Integer.MIN_VALUE;
    private long capturedKeyAtMs;
    private long swallowCharsUntilMs;
    protected static boolean bedwarsNoticeContinued;
    private boolean bindingModuleKey;
    private Module bindingModuleTarget;
    private String bindingConfigName;
    private String configBindStatusMessage = "";
    private float clipX;
    private float clipY;
    private float clipW;
    private float clipH;
    private boolean clippingHits;
    protected ColorDrag colorDrag = ColorDrag.NONE;
    private float rainbowSpeedX;
    private float rainbowSpeedW;
    private float inspectorBoundaryX;
    private float moduleInspectorProgress;
    private float moduleInspectorTargetProgress;
    private long moduleInspectorAnimationStartedAt;
    private boolean inspectorRevealHitClip;
    private float inspectorRevealX;
    private float inspectorRevealY;
    private float inspectorRevealW;
    private float inspectorRevealH;
    private long openedAt;
    private long closedAt;
    private long lastFrameAt = this.openedAt = this.nanoTime();
    private float frameDelta;
    private final UiSprings springs = new UiSprings();
    private static final Object DRAWER_FLOAT;
    private static final Object CONFIG_EDIT_FLOAT;
    private final UiSmoothScroll catalogScroll = new UiSmoothScroll();
    private final KitHost kitHost = new KitHost();
    private final ConfigsView configsView = new ConfigsView(new ConfigsBackend());
    private final StudioTab macroView = new StudioTab(StudioTab.Kind.MACROS);
    private final StudioTab scriptView = new StudioTab(StudioTab.Kind.SCRIPTS);
    private final PeopleView peopleView = new PeopleView(new PeopleBackend());
    private final KeybindsView keybindsView = new KeybindsView();
    private final CosmeticsView cosmeticsView = new CosmeticsView(new e());
    private UiTextTarget kitFocus;
    private IntConsumer kitKeyCapture;
    private final SliderValueEdit sliderEdit = new SliderValueEdit();
    private final KeybindCapture captureOrigin = new KeybindCapture();
    private static final float BASE_PANEL_RADIUS = 24.0f;
    private static final float BASE_CARD_RADIUS = 18.0f;
    private float panelRadius = 24.0f;
    private float cardRadius = 18.0f;
    private float cornerScale = 1.0f;
    private float appliedMenuScale = Float.NaN;
    private static final float MIN_WINDOW_W = 600.0f;
    private static final float MIN_WINDOW_H = 400.0f;
    private static float windowW;
    private static float windowH;
    private static float windowOffX;
    private static float windowOffY;
    private static final int EDGE_L = 1;
    private static final int EDGE_R = 2;
    private static final int EDGE_T = 4;
    private static final int EDGE_B = 8;
    private static final int DRAG_MOVE = 16;
    private int windowDrag;
    private double pressX;
    private double pressY;
    private float dragStartOffX;
    private float dragStartOffY;
    private float dragStartL;
    private float dragStartT;
    private float dragStartW;
    private float dragStartH;
    private float lastFitS = 1.0f;
    private float lastSw;
    private float lastSh;
    private float lastPanelsW;
    private float lastWinX;
    private float lastWinY;
    private float lastWinW;
    private float lastWinH;
    private float railExpand;
    private float parX;
    private float parY;
    private int railPillTint;
    private long viewSwitchAt;
    private long settingsTabSwitchAt;
    private View lastViewDrawn;
    private float lastRailDrawW;
    private float minWinW = 600.0f;
    private float laidOutSw;
    private float laidOutSh;
    private boolean panning;
    private double panLastY;
    private float panVel;
    private float flingVel;
    private float flingX;
    private float flingY;
    private long panLastNs;
    private int cursorShape = -1;
    private static final Map<Integer, Long> STANDARD_CURSORS;
    private float waterPrevMx = Float.NaN;
    private float waterPrevMy = Float.NaN;
    private float waterVelX;
    private float waterVelY;
    protected final TouchInput touch = new TouchInput(30.0);
    private String hoveredTooltipTitle;
    private String hoveredTooltipDesc;
    private List<String> savedConfigs = List.of();
    private Map<String, PrivateConfigManager.PrivateConfigInfo> cloudProfiles = Map.of();
    private String configStatusMessage = "";
    private String pendingProfileSaveName;
    private boolean pendingSavePublic;
    private List<String> pendingSaveTags = List.of();
    private String pendingProfileActivationName;
    private boolean profileLoadPending;
    private int browseRevision = -1;
    protected class_437 returnScreen;
    private boolean openSoundPlayed;
    private float lastOpenSpring = 1.0f;
    private float lastOpenFade = 1.0f;
    private float closeFromSpring = 1.0f;
    private float closeFromFade = 1.0f;
    private int hoverSoundKey;
    private float hoverSoundX = Float.NaN;
    private float hoverSoundY = Float.NaN;
    private int sliderSoundNotch = Integer.MIN_VALUE;
    private UiSurface surface = UiSurface.THEME;
    private boolean cardFillCustom;
    private boolean cardBorderCustom;
    private final IdentityHashMap<Object, String[]> animKeyCache = new IdentityHashMap();
    private int hoverHl;
    private int sidebarHl;
    private int enabledHl;
    private int tooltipBg;
    private int tileActiveOverride;
    private float hoverGlowK = 1.0f;
    private float sidebarGlowK = 1.0f;
    private float enabledK = 1.0f;
    private final List<FolderTarget> railFolderRects = new ArrayList<FolderTarget>();
    private boolean sdArmed;
    private long sdArmedAt;
    private long signOutArmedAt;
    private float aiContentH;
    private static final float QUICK_HEADER_H = 26.0f;
    private static final float[] STAR_X;
    private static final float[] STAR_Y;
    private float inspectorHeaderInset;
    private static final float INLINE_BIND_H = 42.0f;
    private static final float MODE_GRID_TOP = 33.0f;
    private static final float MODE_CELL_H = 26.0f;
    private static final float MODE_CELL_GAP = 5.0f;
    private static final float MODE_TRAY_PAD = 3.0f;
    private static final float MODE_LABEL_MID = 16.0f;
    private static final float MULTI_CELL_H = 24.0f;
    protected static final Map<Setting, Boolean> SUB_VISIBLE;
    protected static final Map<Setting, Integer> SUB_DEPTH;
    private final Map<Setting, Fold> folds = new IdentityHashMap<Setting, Fold>();
    private long foldFrame;
    private int lastKeySeen = Integer.MIN_VALUE;
    private long lastKeySeenAt;
    private int editBeginCount;
    private View focusView = View.MODULES;
    private boolean guiKeyChecked;
    protected boolean guiKeyReleasedSinceOpen = true;
    private int navStagger;
    private static final Map<WrapKey, List<String>> WRAP_CACHE;

    public void showView(String string) {
        if (GpuClickGuiScreen.bf("\u6fd7\u7186\u6ebf\u5ff4\u5a96\u59ff\u5d61", -2081690878, 0x1B1EF9FC ^ CryptoVault.mask()).equals(string)) {
            this.themesDrawerOpen = true;
            this.themeMakerOpen = false;
            this.settingsDrawerOpen = false;
            this.appearanceScroll = 0.0f;
            return;
        }
        try {
            View view = View.valueOf(string);
            if (this.viewAllowed(view)) {
                this.view = view;
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }

    protected boolean studioUnlocked() {
        return PremiumGate.studio();
    }

    protected boolean cosmeticsUnlocked() {
        return PremiumGate.cosmetics();
    }

    private boolean viewAllowed(View view) {
        return switch (view.ordinal()) {
            case 2, 3 -> this.studioUnlocked();
            case 6 -> this.cosmeticsUnlocked();
            default -> true;
        };
    }

    public GpuClickGuiScreen() {
        super((class_2561)class_2561.method_43470((String)GpuClickGuiScreen.bf("\uadef\u29e0\u0eff\ub20d\ud889\ucd08\u009a\ua35d\uffa1\u244d\u99fa\ue15d\u75a4\u4259\u83a3", -367100760, 0x1B1EF9FC ^ CryptoVault.mask())));
        this.inputRoot.add(this.headerPanel);
        this.configStatusMessage = lastCloudConfigStatusMessage;
        if (!(this instanceof DropdownGpuClickGuiScreen)) {
            this.refreshSavedConfigs();
        }
        WhatsNewPanel.preload();
        this.restoreLastView();
        this.tour.setLayoutPainter(this::drawLayoutCards);
    }

    protected static GuiPrefs guiPrefs() {
        return ClientPrefs.get().gui();
    }

    private static <E extends Enum<E>> E enumOr(E[] EArray, String string, E e2) {
        for (E e3 : EArray) {
            if (!((Enum)e3).name().equals(string)) continue;
            return e3;
        }
        return e2;
    }

    private void syncBrowseState() {
        GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
        int n2 = guiPrefs.revision();
        if (n2 == this.browseRevision) {
            return;
        }
        this.browseRevision = n2;
        moduleFilter = (ModuleFilter)GpuClickGuiScreen.enumOr((Enum[])ModuleFilter.values(), (String)guiPrefs.browseFilter(), (Enum)ModuleFilter.ALL);
        moduleSort = (ModuleSort)GpuClickGuiScreen.enumOr((Enum[])ModuleSort.values(), (String)guiPrefs.browseSort(), (Enum)ModuleSort.DEFAULT);
        this.settingsTab = (SettingsTab)GpuClickGuiScreen.enumOr((Enum[])SettingsTab.values(), (String)guiPrefs.settingsTab(), (Enum)SettingsTab.CLIENT_CONTROLS);
        quickCollapsed = guiPrefs.quickCollapsed();
        COLLAPSED_GROUPS.clear();
        COLLAPSED_GROUPS.addAll(guiPrefs.collapsedGroups());
        this.catalogCacheKey = "";
    }

    protected static void setModuleFilter(ModuleFilter moduleFilter) {
        GpuClickGuiScreen.moduleFilter = moduleFilter;
        GpuClickGuiScreen.guiPrefs().setBrowse(GpuClickGuiScreen.moduleFilter.name(), moduleSort.name());
    }

    protected static void cycleModuleSort() {
        moduleSort = ModuleSort.values()[(moduleSort.ordinal() + 1) % ModuleSort.values().length];
        GpuClickGuiScreen.guiPrefs().setBrowse(moduleFilter.name(), moduleSort.name());
    }

    protected static void toggleGroupCollapsed(String string) {
        if (!COLLAPSED_GROUPS.remove(string)) {
            COLLAPSED_GROUPS.add(string);
        }
        GpuClickGuiScreen.guiPrefs().setCollapsedGroups(COLLAPSED_GROUPS);
    }

    private void setSettingsTab(SettingsTab settingsTab) {
        this.settingsTab = settingsTab;
        GpuClickGuiScreen.guiPrefs().setSettingsTab(settingsTab.name());
    }

    private void restoreLastView() {
        Object object;
        this.syncBrowseState();
        GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
        String string = guiPrefs.lastCategory();
        if (string.startsWith(GpuClickGuiScreen.bf("\u3e8b\ud664\uf4f1\uf066\u42be\ueb5f\ua15a\u3da8\u8edb\u9ac4", 1181697701, 0x1B1EF9FC ^ CryptoVault.mask()))) {
            object = string.substring(GpuClickGuiScreen.bf("\u0c9b\u240c\u40a7\u83bb\u2e1a\u4bd0\u8ba2\u9371\u7e88\uaf50", -17072437, 0x1B1EF9FC ^ CryptoVault.mask()).length());
            if (guiPrefs.customCategory((String)object) != null) {
                this.folderId = object;
            }
        } else if (!string.isEmpty()) {
            for (Category category : NAV_CATEGORIES) {
                if (!category.name().equals(string)) continue;
                this.category = category;
            }
        }
        object = guiPrefs.lastView();
        if (!((String)object).isEmpty()) {
            try {
                View view = View.valueOf((String)object);
                if (view != View.SELF_DESTRUCT && this.viewAllowed(view)) {
                    this.view = view;
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        try {
            if (this.view == View.SCRIPTS) {
                this.scriptView.refresh();
            } else if (this.view == View.MACROS) {
                this.macroView.refresh();
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (this.view == View.MODULES && guiPrefs.lastScroll() > 0.0f) {
            this.restoreCatalogScroll = guiPrefs.lastScroll();
        }
    }

    private void rememberView() {
        try {
            GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
            float f2 = this.view != View.MODULES ? 0.0f : (this.catalogDrawn ? this.catalogScroll.target() : (this.restoreCatalogScroll >= 0.0f ? this.restoreCatalogScroll : guiPrefs.lastScroll()));
            guiPrefs.rememberView(this.view.name(), this.folderId != null ? GpuClickGuiScreen.bf("\ufd1c\u9617\u9dfe\uedb3\ud65a\u7977\u858b\u75e1", 181105616, 0x1B1EF9FC ^ CryptoVault.mask()) + this.folderId : this.category.name(), f2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private boolean modalOverlayOpen() {
        return this.whatsNew.isOpen() || this.tour.active() || this.crosshairMaker != null;
    }

    private float[] tourRect() {
        float[] fArray = switch (this.tour.target()) {
            case OnboardingTour.Target.RAIL -> this.tourRailRect;
            case OnboardingTour.Target.CATALOG -> this.tourCatalogRect;
            case OnboardingTour.Target.NAV_SETTINGS -> this.tourNavRects[0];
            case OnboardingTour.Target.NAV_CONFIGS -> this.tourNavRects[1];
            case OnboardingTour.Target.NAV_PEOPLE -> this.tourNavRects[2];
            case OnboardingTour.Target.NAV_HUD -> this.tourNavRects[3];
            case OnboardingTour.Target.NAV_SELF_DESTRUCT -> this.tourNavRects[4];
            case OnboardingTour.Target.RAIL_MACROS -> this.tourMacrosRect;
            case OnboardingTour.Target.RAIL_SCRIPTS -> this.tourScriptsRect;
            default -> null;
        };
        return fArray == null || fArray[2] <= 0.0f || fArray[3] <= 0.0f ? null : fArray;
    }

    private void replayTour() {
        this.settingsDrawerOpen = false;
        this.themesDrawerOpen = false;
        this.whatsNew.close();
        this.view = View.MODULES;
        this.tour.start();
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
                if (string != null && !string.isBlank()) {
                    lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\u454c\u2f28\uff93\u1f0d\u97b3\u2d65\u5701\u8282\u29bf\u333e\ueb0e\uc5b0\ue8cb\u7cca\ua4d4\u931c\uc9c2\u3a6c\uec35\u0835\ud8ec\u69a6\u0dc4\u35ba\u7266\uf980\uca2c\u8dd3\uc5f0", -2004263728, 0x1B1EF9FC ^ CryptoVault.mask()) + string;
                    if (this.view == View.CONFIGS) {
                        this.configStatusMessage = lastCloudConfigStatusMessage;
                    }
                }
            });
        } else {
            this.cloudProfiles = Map.of();
        }
    }

    private static String cloudError() {
        String string = CloudConfigs.lastError();
        return string == null || string.isBlank() ? GpuClickGuiScreen.bf("\ue80c\u47fb\ufb39\uc815\u0344\u5d30\uf6a7\u53b8\u605d\ufa16\uc679\uea71\uc65f\u38ef\ufd76\u922c\u0113\u9eea\u2dfe\u5510\u0494\uca30\u22fd\u555b\u415e\u51d1", -596329965, 0x1B1EF9FC ^ CryptoVault.mask()) : string;
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
        }, GpuClickGuiScreen.bf("\uf4f4\u9f03\u2ec5\uf2c6\u82e0\uf464\u1abc\u67cb\u9abd\uc1dc\u7c69\u1c15\ua0aa\u74cf\u1d0c\ud739\ud944\uc087", -1097330531, 0x1B1EF9FC ^ CryptoVault.mask()));
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
        }, GpuClickGuiScreen.bf("\uab95\uc2c7\ud00a\u273e\u067e\ue6b7\u089e\u996d\u8329\u8e43\u31aa\u70c5\u5b73\ue7db\u4521\u7f1a\u499c", -77356161, 0x1B1EF9FC ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    protected ModuleManager moduleManager() {
        SlApp slApp = SlApp.getInstance();
        return slApp == null ? null : slApp.getModuleManager();
    }

    protected HitboxConfigScreen752809 clickGuiModule() {
        ModuleManager moduleManager = this.moduleManager();
        return moduleManager == null ? null : moduleManager.getModule(HitboxConfigScreen752809.class);
    }

    protected List<Module> modulesForCategory(Category category) {
        ModuleManager moduleManager = this.moduleManager();
        if (moduleManager == null || category == null) {
            return List.of();
        }
        List<Module> list = moduleManager.getModulesForCategory(category).stream().filter(module -> !module.isHidden()).toList();
        return GuiPrefs.applyOrder(list, Module::getName, GpuClickGuiScreen.guiPrefs().order(category.name()));
    }

    protected List<Module> modulesForFolder(String string) {
        GuiPrefs.CustomCategory customCategory;
        GuiPrefs.CustomCategory customCategory2 = customCategory = string == null ? null : GpuClickGuiScreen.guiPrefs().customCategory(string);
        if (customCategory == null) {
            return List.of();
        }
        HashMap<String, Module> hashMap = new HashMap<String, Module>();
        for (Module object : this.allModules()) {
            hashMap.putIfAbsent(object.getName(), object);
        }
        ArrayList arrayList = new ArrayList();
        for (String string2 : customCategory.modules()) {
            Module module = (Module)hashMap.get(string2);
            if (module == null) continue;
            arrayList.add(module);
        }
        return arrayList;
    }

    public GpuClickGuiScreen withReturnScreen(class_437 class_4372) {
        this.returnScreen = class_4372;
        return this;
    }

    public class_437 returnScreen() {
        return this.returnScreen;
    }

    protected void method_25426() {
        super.method_25426();
        if (!this.guiKeyChecked) {
            this.guiKeyChecked = true;
            boolean bl = this.guiKeyReleasedSinceOpen = !this.guiKeyHeld();
        }
        if (!this.openSoundPlayed) {
            this.openSoundPlayed = true;
            GpuFluidSimulation.resetQuality();
            UiSounds.play(UiSounds.Event.OPEN);
        }
        if (HitboxConfigScreen752809.ac() && this.field_22787 != null && this.field_22787.method_22683() != null) {
            double d2 = this.field_22787.field_1729 != null ? this.field_22787.field_1729.method_1603() : (double)this.field_22789 * 0.5;
            double d3 = this.field_22787.field_1729 != null ? this.field_22787.field_1729.method_1604() : (double)this.field_22790 * 0.5;
            class_3675.method_15984((class_1041)this.field_22787.method_22683(), (int)212994, (double)d2, (double)d3);
        }
    }

    protected void method_57734(class_332 class_3322) {
        try {
            if (OverlayCompositor.shouldRedirect()) {
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (this.field_22787 != null && this.field_22787.field_1687 != null && HitboxConfigScreen752809.M != null && HitboxConfigScreen752809.M.isEnabled()) {
            return;
        }
        super.method_57734(class_3322);
    }

    public boolean method_25421() {
        return false;
    }

    public void method_29638(List<Path> list) {
        try {
            FileDropHandler fileDropHandler = ModuleAccess.getHook(FileDropHandler.class);
            if (fileDropHandler != null) {
                fileDropHandler.onFilesDropped(list);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public void method_25419() {
        if (this.closing) {
            return;
        }
        this.cancelKeybindCapture();
        this.closeContextMenu();
        this.cancelCardDrag();
        this.closing = true;
        this.closedAt = this.nanoTime();
        OverlayCompositor.noteMenuClosed((this.closedAt - this.openedAt) / 1000000L);
        this.closeFromSpring = this.lastOpenSpring;
        this.closeFromFade = this.lastOpenFade;
        UiSounds.play(UiSounds.Event.CLOSE);
    }

    public void method_25432() {
        this.springs.clear();
        this.animKeyCache.clear();
        this.commitFolderRename();
        this.rememberView();
        this.closeContextMenu();
        this.cancelCardDrag();
        this.scriptView.flush();
        if (this.cursorShape > 0 && this.field_22787 != null && this.field_22787.method_22683() != null) {
            try {
                GLFW.glfwSetCursor((long)this.field_22787.method_22683().method_4490(), (long)0L);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        this.cursorShape = -1;
        GpuFluidSimulation.releaseSoon();
        GpuUiEffectPasses.trimPool();
        GpuUiClipMask.releaseCacheOnClose();
        GpuUiMaterialRenderer.releaseCache();
        this.clearTextFocus();
        this.cancelKeybindCapture();
        OverlayWindow.hide();
        OverlayWindow.handOffFocusToGame();
        if (this.field_22787 != null && this.field_22787.method_22683() != null && this.field_22787.field_1729 != null) {
            class_3675.method_15984((class_1041)this.field_22787.method_22683(), (int)212993, (double)this.field_22787.field_1729.method_1603(), (double)this.field_22787.field_1729.method_1604());
        }
        if (!(this instanceof DropdownGpuClickGuiScreen)) {
            boolean bl;
            ConfigManager configManager = ConfigManager.getInstance();
            configManager.save(configManager.getLastLoadedConfigName());
            String string2 = configManager.exportCurrentConfigJsonForCloud();
            boolean bl2 = bl = this.pendingProfileSaveName != null || this.pendingProfileActivationName != null;
            if (!this.closing && bl) {
                string2 = null;
            }
            if (this.pendingProfileSaveName != null && string2 != null) {
                String string3 = this.pendingProfileSaveName;
                boolean bl3 = this.pendingSavePublic;
                List<String> list = this.pendingSaveTags;
                lastCloudConfigStatusMessage = this.configStatusMessage = GpuClickGuiScreen.bf("\u1f0b\u1a25\u1d8b\uaf23\u6fa9\uab58\u4823\u1956\ud724\u8242\u72db\u45fb\u9ae0\u79be\uc00e\u3a93\u5592\ua1da\u7b7a\u313c\ua6a5\u4ee8\u228c\u27f5", 165761161, 0x1B1EF9FC ^ CryptoVault.mask()) + string3 + GpuClickGuiScreen.bf("\u0aec\u6583\u78dc\u0bfe\u60e5\u9c95", 1152510938, 0x1B1EF9FC ^ CryptoVault.mask());
                PrivateConfigManager.getInstance().saveActiveConfig(string3, string2, () -> {
                    lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\u8023\ub57f\ub6e8\u87c8\u7ee8\u8e73\u683d\ufc18\u1390\u71f2\u3c59\u51fd\u3459\u73b4\uf61a\ucb37\u0978\u2c10\u83c3\uce25\ubc4b\uf1de\u7942\uf0aa", 1269656588, 0x1B1EF9FC ^ CryptoVault.mask()) + string3 + "\".";
                    this.applyPendingSaveMetadata(string3, bl3, list);
                    this.refreshSavedConfigs();
                }, string -> {
                    lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\u6454\ue2e4\u2369\uedb6\u9f90\u0cf0\ua09a\u2404\ucf8c\ue8ac\uaa58\u905d\uf7e1\u370b\ube91\u8260\ub162\uba5b\ub88e\u85e6\ubbb6\u184f\ub2f4\uc0df\u2eea\ue029\ubea2\u8b53\u9fd0\u4784\u4f76", -1648500020, 0x1B1EF9FC ^ CryptoVault.mask()) + string;
                });
                this.pendingProfileSaveName = null;
                this.pendingSavePublic = false;
                this.pendingSaveTags = List.of();
            } else if (this.pendingProfileActivationName != null && this.closing) {
                String string4 = this.pendingProfileActivationName;
                lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\u3f74\u5b7e\ub6bd\u02bc\u8327\ueafd\u11f2\u909f\uce3e\u1035\u6a48\uee5f\u3b7f\uaab1\ud1d9\u94cd\u778d\u92e9\u7560\u45dd\u04c8\ua755\uba55\u2809\u270a\u0688\ueb5d\u7bdb", -78181005, 0x1B1EF9FC ^ CryptoVault.mask()) + string4 + GpuClickGuiScreen.bf("\u3436\u7b1c\u425c\ubfb6\u9fbc\u6b2d", 303315857, 0x1B1EF9FC ^ CryptoVault.mask());
                PrivateConfigManager.getInstance().activateConfig(string4, () -> {
                    lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\u5f60\ufd89\u490e\uf331\ua35e\u14fc\ue713\u2db9\u35ed\u6723\ua34c\u619d\ucb3a\uef81\u75d5\u37ee\u1860\uf5cd\ubd88\u8a6c\uda46\u0a97\u13f0\u4875", -1736697068, 0x1B1EF9FC ^ CryptoVault.mask()) + string4 + "\".";
                }, string -> {
                    lastCloudConfigStatusMessage = GpuClickGuiScreen.bf("\ub522\uea2c\ue6f4\ue44f\ufee0\u1389\uebdb\u26c4\ue13b\ua1d8\u0a3c\uc7dc\u9e21\u20d6\u94d3\u2caa\uf03a\u2c92\u3fb2\ud9b2\ua355\u875b\u1256\ue384\u9589\u6944\u9eba\u5532\u31fa\u8dc9\u9c49\u9d71\u81dc\u9468\u323b\u0aed\u7409", 1700390767, 0x1B1EF9FC ^ CryptoVault.mask()) + string;
                });
                this.pendingProfileActivationName = null;
            } else if (!this.profileLoadPending && string2 != null) {
                CloudConfigSync.saveIfChangedNow(string2);
            }
        }
        super.method_25432();
    }

    private void cancelKeybindCapture() {
        this.captureOrigin.clear();
        this.kitKeyCapture = null;
        this.bindingBool = null;
        if (this.bindingModuleTarget != null) {
            this.bindingModuleTarget.setBinding(false);
        }
        this.binding = null;
        this.bindingModuleKey = false;
        this.bindingModuleTarget = null;
        this.bindingConfigName = null;
    }

    protected long nanoTime() {
        return System.nanoTime();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        this.syncBrowseState();
        float f3 = this.pixelsPerGuiUnit();
        long l2 = PerfProbe.on ? System.nanoTime() : 0L;
        UiDisplayList uiDisplayList = this.buildFrame((float)n2 * f3, (float)n3 * f3);
        if (l2 != 0L) {
            PerfProbe.module(GpuClickGuiScreen.bf("\u68ea\uff84\ub7bf\ua4ae\u4fdc\u2369\ue332\u8eee\uf0c5\uc469\u0b03\u3910\u666f", 1417564724, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uee5d\u6a53\u505a\u1c46\u5b19\u8b6e\u89e0\u37aa", -2125025271, 0x1B1EF9FC ^ CryptoVault.mask()), System.nanoTime() - l2);
        }
        if (uiDisplayList != null) {
            GpuUiRenderer.renderPixels(class_3322, uiDisplayList, f3);
            this.hoverSound((float)n2 * f3, (float)n3 * f3, (float)this.field_22789 * f3 * (float)this.field_22790 * f3);
            float[] fArray = this.cosmeticsView.previewRect();
            CosmeticsProvider cosmeticsProvider = ModuleAccess.getHook(CosmeticsProvider.class);
            if (this.view == View.COSMETICS && !this.closing && !GpuClickGuiScreen.overlayCaptureActive() && cosmeticsProvider != null) {
                if (fArray[2] > 0.0f) {
                    cosmeticsProvider.drawPreview(class_3322, Math.round(fArray[0] / f3), Math.round(fArray[1] / f3), Math.round((fArray[0] + fArray[2]) / f3), Math.round((fArray[1] + fArray[3]) / f3), this.cosmeticsView.previewYaw(), this.cosmeticsView.previewPitch(), this.cosmeticsView.previewRoll());
                }
                List<CosmeticsView.Thumbnail> list = this.cosmeticsView.thumbnails();
                ThumbnailCache.P();
                if (!list.isEmpty()) {
                    float[] fArray2 = this.cosmeticsView.thumbnailClip();
                    ScissorClamp.enable(class_3322, Math.round(fArray2[0] / f3), Math.round(fArray2[1] / f3), Math.round((fArray2[0] + fArray2[2]) / f3), Math.round((fArray2[1] + fArray2[3]) / f3));
                    try {
                        for (CosmeticsView.Thumbnail thumbnail : list) {
                            if (thumbnail.weapon() != null) {
                                cosmeticsProvider.drawWeaponThumbnail(class_3322, Math.round(thumbnail.x() / f3), Math.round(thumbnail.y() / f3), Math.round((thumbnail.x() + thumbnail.w()) / f3), Math.round((thumbnail.y() + thumbnail.h()) / f3), thumbnail.weapon());
                                continue;
                            }
                            cosmeticsProvider.drawThumbnail(class_3322, Math.round(thumbnail.x() / f3), Math.round(thumbnail.y() / f3), Math.round((thumbnail.x() + thumbnail.w()) / f3), Math.round((thumbnail.y() + thumbnail.h()) / f3), thumbnail.selection(), thumbnail.frame());
                        }
                    }
                    finally {
                        class_3322.method_44380();
                    }
                }
            }
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
        if (n3 != 0 && n2 != 0 && !this.closing && this.draggingSlider == null && this.windowDrag == 0) {
            UiSounds.play(UiSounds.Event.HOVER);
        }
    }

    protected float pixelsPerGuiUnit() {
        class_1041 class_10412;
        class_1041 class_10413 = class_10412 = this.field_22787 != null ? this.field_22787.method_22683() : null;
        if (class_10412 == null || class_10412.method_4486() <= 0) {
            return 1.0f;
        }
        return (float)class_10412.method_4489() / (float)class_10412.method_4486();
    }

    protected UiDisplayList buildFrame(float f2, float f3) {
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        boolean bl;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        if (this.view != this.focusView) {
            this.clearTextFocus();
            this.focusView = this.view;
        }
        this.inspectorRevealW = 0.0f;
        this.inspectorRevealHitClip = false;
        if (this.closing && this.nanoTime() - this.closedAt > 170000000L) {
            if (this.field_22787 != null) {
                McCompat.setScreen(this.field_22787, this.returnScreen);
            }
            return null;
        }
        float f19 = this.pixelsPerGuiUnit();
        float f20 = (float)this.field_22789 * f19;
        float f21 = (float)this.field_22790 * f19;
        long l2 = this.nanoTime();
        this.frameDelta = (float)Math.min(0.1, Math.max(0.0, (double)(l2 - this.lastFrameAt) / 1.0E9));
        this.lastFrameAt = l2;
        ++this.foldFrame;
        this.springs.endFrame();
        if (this.flingVel != 0.0f && !this.closing) {
            this.method_25401(this.flingX / f19, this.flingY / f19, 0.0, this.flingVel * this.frameDelta / this.panStepPx(this.flingX, this.flingY));
            this.flingVel = ResponsiveLayout.decayFling(this.flingVel, this.frameDelta, 30.0f);
        }
        this.updateModuleInspectorAnimation(l2);
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        this.currentAccent = hitboxConfigScreen752809 == null ? -13638657 : (HitboxConfigScreen752809.M() ? HitboxConfigScreen752809.K.A().getRGB() : UiThemeMaterial.theme(HitboxConfigScreen752809.a.getCurrentMode()).accent());
        String string = hitboxConfigScreen752809 == null ? "" : HitboxConfigScreen752809.a.getCurrentMode();
        boolean bl2 = UiThemeMaterial.LIQUID_GLASS.name().equalsIgnoreCase(string);
        this.lg = bl2 && !GpuClickGuiScreen.overlayCaptureActive();
        this.applyPalette(UiThemeMaterial.theme(string));
        float f22 = 460.0f;
        float f23 = 14.0f;
        float f24 = f23 + f22;
        if (Float.isNaN(this.appliedMenuScale) || HitboxConfigScreen752809.S == null || this.draggingSlider != HitboxConfigScreen752809.S) {
            this.appliedMenuScale = hitboxConfigScreen752809 == null || HitboxConfigScreen752809.S == null ? 1.0f : (float)HitboxConfigScreen752809.b();
        }
        float f25 = this.appliedMenuScale;
        float f26 = 1.0f;
        if (HitboxConfigScreen752809.T != null && GpuClickGuiScreen.bf("\u703c\u49bf\ufe52\u35eb\u2bc2\u4b7b\u6c9a\ue4a7", -322707969, 0x1B1EF9FC ^ CryptoVault.mask()).equals(HitboxConfigScreen752809.T.getCurrentMode())) {
            f26 = AutoMenuScale.correction(f21, AutoMenuScale.currentPxPerMm(this.field_22787));
        }
        float f27 = ResponsiveLayout.classicAutoScale(f21, f26) * f25;
        float f28 = Math.round(14.0f * f27);
        float f29 = Math.max(1.0f, f20 - f28 * 2.0f);
        float f30 = Math.max(1.0f, f21 - f28 * 2.0f);
        float f31 = ResponsiveLayout.availableUnits(f29, f27);
        boolean bl3 = ResponsiveLayout.compactRail(f31);
        boolean bl4 = HitboxConfigScreen752809.k != null && HitboxConfigScreen752809.k.isEnabled();
        boolean bl5 = bl3 || bl4;
        float f32 = bl5 ? 56.0f : 150.0f;
        boolean bl6 = this.view == View.MODULES && this.sidePanelSettingsMode() && (this.moduleInspectorProgress > 0.0f || this.moduleInspectorTargetProgress > 0.0f);
        float f33 = bl6 ? this.moduleInspectorProgress : 0.0f;
        float f34 = bl3 ? 440.0f : 600.0f;
        boolean bl7 = ResponsiveLayout.dockSidePanel(f31, f34, f24);
        this.minWinW = f34;
        float f35 = bl7 ? f24 * f33 : 0.0f;
        float f36 = Math.min(windowW > 0.0f ? windowW : 1400.0f, 1400.0f);
        float f37 = Math.min(windowH > 0.0f ? windowH : 1000.0f, 1000.0f);
        if (this.laidOutSw != f20 || this.laidOutSh != f21) {
            this.laidOutSw = f20;
            this.laidOutSh = f21;
            this.closeContextMenu();
            this.cancelCardDrag();
            this.panning = false;
            this.flingVel = 0.0f;
            this.hoverSoundKey = 0;
        }
        float f38 = Math.max(0.55f, Math.min(f27, Math.min(f29 / (f34 + f35), f30 / 400.0f)));
        float f39 = Math.max(f34, Math.min(f36, f29 / f38 - f35));
        float f40 = Math.max(400.0f, Math.min(f37, f30 / f38));
        float f41 = Math.min(1.0f, (float)(this.nanoTime() - (this.closing ? this.closedAt : this.openedAt)) / (this.closing ? 1.7E8f : 5.2E8f));
        float f42 = this.closing ? this.closeFromSpring * (1.0f - f41) : 1.0f - (float)(Math.exp(-7.0 * (double)f41) * Math.cos(11.0 * (double)f41));
        float f43 = f18 = this.closing ? this.closeFromFade * (1.0f - f41) : Math.min(1.0f, f41 * 2.2f);
        if (!this.closing) {
            this.lastOpenSpring = f42;
            this.lastOpenFade = f18;
        }
        float f44 = f18 * f18 * (3.0f - 2.0f * f18);
        float f45 = 0.92f + 0.08f * f42;
        float f46 = f38 * f45;
        float f47 = Math.round(f39 * f46);
        float f48 = Math.round(f40 * f46);
        float f49 = f47 + f35 * f46;
        float f50 = GpuClickGuiScreen.clampRange((f20 - f49) * 0.5f + windowOffX * f46, f28 * 0.5f, f20 - f49 - f28 * 0.5f);
        float f51 = Math.round(f50);
        float f52 = GpuClickGuiScreen.clampRange((f21 - f48) * 0.5f + windowOffY * f46, f28 * 0.5f, f21 - f48 - f28 * 0.5f);
        float f53 = Math.round(f52 + (1.0f - f42) * 18.0f * f46);
        float f54 = Math.round(this.panelRadius * f46);
        float f55 = Math.round(46.0f * f46);
        float f56 = bl6 ? this.moduleInspectorProgress : 0.0f;
        this.lastFitS = f38;
        this.lastSw = f20;
        this.lastSh = f21;
        this.lastPanelsW = f35;
        this.lastWinX = f51;
        this.lastWinY = f53;
        this.lastWinW = f47;
        this.lastWinH = f48;
        UiDisplayList.Builder builder = UiDisplayList.builder();
        this.hits.clear();
        this.moduleCardRightHits.clear();
        this.hoveredTooltipTitle = null;
        this.hoveredTooltipDesc = null;
        this.surface = hitboxConfigScreen752809 == null ? UiSurface.THEME : HitboxConfigScreen752809.c();
        boolean bl8 = hitboxConfigScreen752809 != null && !bl2 && (!this.surface.isTheme() || HitboxConfigScreen752809.a(string));
        boolean bl9 = hitboxConfigScreen752809 != null && this.surface.isTheme() && HitboxConfigScreen752809.b != null && HitboxConfigScreen752809.b.is("On");
        this.floatMaterial = bl8;
        this.floatMist = bl9;
        this.floatTheme = string;
        boolean bl10 = hitboxConfigScreen752809 != null && HitboxConfigScreen752809.g.isEnabled();
        boolean bl11 = hitboxConfigScreen752809 != null && HitboxConfigScreen752809.h.isEnabled();
        boolean bl12 = hitboxConfigScreen752809 != null && HitboxConfigScreen752809.i.isEnabled();
        boolean bl13 = !bl8 && !bl2;
        boolean bl14 = true;
        boolean bl15 = this.field_22787 == null || this.field_22787.field_1687 != null;
        float f57 = f17 = bl14 && bl15 && hitboxConfigScreen752809 != null && HitboxConfigScreen752809.M.isEnabled() ? Math.min(8.0f, (float)HitboxConfigScreen752809.N.B() * 0.4f) * f44 : 0.0f;
        if (f17 > 0.3f) {
            builder.blur(0.0f, 0.0f, f20, f21, 0.0f, f17);
        }
        if (bl14) {
            builder.gradient(0.0f, 0.0f, f20, f21, 0.0f, GpuClickGuiScreen.argb(38.0f * f44, 0), GpuClickGuiScreen.argb(38.0f * f44, 0), GpuClickGuiScreen.argb(112.0f * f44, 0), GpuClickGuiScreen.argb(112.0f * f44, 0));
        }
        int n2 = GpuClickGuiScreen.mixRgb(this.currentAccent, -7101251, 0.55f) & 0xFFFFFF;
        if (bl14) {
            builder.rect(0.0f, 0.0f, f20, f21, 0.0f, Math.round(f44 * (float)(bl8 ? 102 : 64)) << 24);
        }
        float f58 = this.glassBlurValue * Math.max(0.35f, f44);
        int n3 = GpuClickGuiScreen.mixRgb(-16250094, this.currentAccent, 0.16f) & 0xFFFFFF | 0x8C000000;
        if (this.panelTintOverride != 0) {
            n3 = this.panelTintOverride;
        }
        int n4 = this.panelTintOverride;
        if (!this.lg) {
            n3 = GpuClickGuiScreen.scaleAlpha(n3, this.glassOpacityK);
        }
        this.floatBlur = f58;
        this.floatTint = n3;
        if (this.lg) {
            this.glassPane(builder, f51, f53, f47, f48, f54, f44, 102, true, n4);
        } else {
            this.panelShadow(builder, f51, f53, f47, f48, f54, f46, f44);
            if (!bl8) {
                builder.glass(f51, f53, f47, f48, f54, f58, n3);
            }
        }
        boolean bl16 = this.reducedMotion() || this.closing;
        float f59 = bl16 ? 0.0f : GpuClickGuiScreen.clampRange((f2 - (f51 + f47 * 0.5f)) / Math.max(1.0f, f20 * 0.5f), -1.0f, 1.0f);
        float f60 = bl16 ? 0.0f : GpuClickGuiScreen.clampRange((f3 - (f53 + f48 * 0.5f)) / Math.max(1.0f, f21 * 0.5f), -1.0f, 1.0f);
        float f61 = bl16 ? 1.0f : Math.min(1.0f, this.frameDelta * 5.0f);
        this.parX += (f59 - this.parX) * f61;
        this.parY += (f60 - this.parY) * f61;
        float f62 = this.reducedMotion() ? 0.0f : (float)Math.round(10.0f * f46);
        float f63 = f51 - f62 - this.parX * 5.0f * f46;
        float f64 = f53 - f62 - this.parY * 5.0f * f46;
        float f65 = f51 - f62 - this.parX * 9.0f * f46;
        float f66 = f53 - f62 - this.parY * 9.0f * f46;
        float f67 = f47 + f62 * 2.0f;
        float f68 = f48 + f62 * 2.0f;
        builder.clipPush(f51, f53, f47, f48, f54);
        if (bl8) {
            this.paintSurface(builder, string, f63, f64, f67, f68, f54, bl9);
        } else {
            this.drawGlassWash(builder, f51, f53, f47, f48, f54);
        }
        if (bl13) {
            builder.material(f65, f66 + f48 * 0.25f, f67, f68 - f48 * 0.25f, 0.0f, UiFogMaterial.of(0.34f * f44, 0.35f, 1.7f, 1.0f, n2));
        }
        if (bl9 && !bl8) {
            builder.material(f65, f66, f67, f68, f54, UiClassicMistMaterial.flux());
        }
        if (bl10 && hitboxConfigScreen752809 != null) {
            f16 = f47 > 0.0f ? GpuClickGuiScreen.clampRange((f2 - f51) / f47, 0.0f, 1.0f) : 0.5f;
            float f69 = f15 = f48 > 0.0f ? GpuClickGuiScreen.clampRange((f3 - f53) / f48, 0.0f, 1.0f) : 0.5f;
            if (Float.isNaN(this.waterPrevMx)) {
                this.waterPrevMx = f16;
                this.waterPrevMy = f15;
                this.waterVelX = 0.0f;
                this.waterVelY = 0.0f;
            }
            f14 = Math.min(1.0f, this.frameDelta * 12.0f);
            this.waterVelX += (f16 - this.waterPrevMx - this.waterVelX) * f14;
            this.waterVelY += (f15 - this.waterPrevMy - this.waterVelY) * f14;
            this.waterPrevMx = f16;
            this.waterPrevMy = f15;
            if (Math.abs(this.waterVelX) < 3.0E-4f) {
                this.waterVelX = 0.0f;
            }
            if (Math.abs(this.waterVelY) < 3.0E-4f) {
                this.waterVelY = 0.0f;
            }
            f13 = (float)(HitboxConfigScreen752809.t.B() / 100.0) * f44;
            f12 = (float)(HitboxConfigScreen752809.u.B() / 100.0);
            int n5 = (int)Math.round(HitboxConfigScreen752809.p.B());
            if (GpuFluidSimulation.usable()) {
                GpuFluidSimulation.configure(HitboxConfigScreen752809.w.is(GpuClickGuiScreen.bf("\u1ae8\u7d82\u6934\u77bc\u83c7\uc50c\u678d", 326072293, 0x1B1EF9FC ^ CryptoVault.mask())) ? 0 : (HitboxConfigScreen752809.w.is(GpuClickGuiScreen.bf("\u2b94\u0fdc\ucb5e\u91bd\u27fc\u67ee\u0eb6\u1cc5\ue96c\ua334", -189214217, 0x1B1EF9FC ^ CryptoVault.mask())) ? 1 : 2), (float)HitboxConfigScreen752809.y.B(), (float)HitboxConfigScreen752809.z.B(), (float)HitboxConfigScreen752809.A.B(), (float)HitboxConfigScreen752809.B.B(), (float)HitboxConfigScreen752809.C.B(), (int)Math.round(HitboxConfigScreen752809.D.B()), HitboxConfigScreen752809.E.isEnabled(), HitboxConfigScreen752809.F.isEnabled(), !HitboxConfigScreen752809.x.is(GpuClickGuiScreen.bf("\u557c\u7c55\u03d7\ufcab\ub939\uac0f\u24d1\u4ab7\ucc05\u9e76\ud390", 581310200, 0x1B1EF9FC ^ CryptoVault.mask())), HitboxConfigScreen752809.G.isEnabled(), this.closing);
                f11 = f47 > 0.0f ? (f2 - f51) / f47 : -1.0f;
                f10 = f48 > 0.0f ? (f3 - f53) / f48 : -1.0f;
                builder.fluid(f51, f53, f47, f48, f54, f11, f10, this.waterVelX, this.waterVelY, HitboxConfigScreen752809.q.A().getRGB(), HitboxConfigScreen752809.r.A().getRGB(), HitboxConfigScreen752809.s.A().getRGB(), n5, f13, f12, HitboxConfigScreen752809.v.isEnabled() && !this.closing);
            } else {
                builder.material(f51, f53, f47, f48, f54, UiWaterMaterial.of(f16, f15, this.waterVelX, this.waterVelY, HitboxConfigScreen752809.q.A().getRGB(), HitboxConfigScreen752809.r.A().getRGB(), HitboxConfigScreen752809.s.A().getRGB(), n5, f13, f12, HitboxConfigScreen752809.v.isEnabled()));
            }
        }
        if (bl11) {
            builder.material(f65, f66, f67, f68, f54, UiAuroraMaterial.of(this.currentAccent & 0xFFFFFF, (float)(HitboxConfigScreen752809.n.B() / 100.0) * f44));
        }
        if (bl12) {
            builder.material(f65, f66, f67, f68, f54, UiStarfieldMaterial.of(this.currentAccent & 0xFFFFFF, (float)(HitboxConfigScreen752809.o.B() / 100.0) * f44));
        }
        f16 = f53 + f55;
        f15 = Math.max(0.0f, f48 - f55);
        f14 = f32 * f46;
        f13 = 150.0f * f46;
        f12 = Math.max(1.0f, 1.0f * f46);
        f11 = f9 = Math.max(0.0f, f47 - f14 - f12);
        f10 = bl7 ? f22 * f46 : Math.min(f22 * f46, Math.max(0.0f, f47 - f14));
        float f70 = f51 + f47;
        float f71 = bl7 ? f70 + f23 * f46 - (1.0f - f56) * (f10 + f23 * f46) : f70 - f56 * f10;
        float f72 = bl7 ? Math.max(f70, f71) : f71;
        float f73 = bl7 ? Math.max(0.0f, f71 + f10 - f72) : f56 * f10;
        this.inspectorRevealX = f72;
        this.inspectorRevealY = f53;
        this.inspectorRevealW = f73;
        this.inspectorRevealH = f48;
        float f74 = this.inspectorBoundaryX = bl6 ? f72 : f51 + f47;
        if (this.lg) {
            float f75 = Math.round(7.0f * f46);
            this.glassPane(builder, f51 + f75, f16 + (float)Math.round(4.0f * f46), f14 - f75 * 2.0f, f15 - (float)Math.round(4.0f * f46) - f75, this.cardRadius * f46, f44, 58, false, 0);
        } else {
            builder.rect(f51, f16, f14, f15, 0.0f, this.railBg);
            builder.rect(f51 + f14, f16, f9, f15, 0.0f, this.listBg);
            if (this.showSidebarSeparator) {
                builder.rect(f51 + f14 - f12, f16, f12, f15, 0.0f, this.lineColor);
            }
            builder.gradient(f51 + f14, f16, Math.round(10.0f * f46), f15, 0.0f, 0x2E000000, 0, 0, 0x2E000000);
            builder.gradient(f51, f16, f47, Math.round(8.0f * f46), 0.0f, 0x26000000, 0x26000000, 0, 0);
        }
        if (this.lastViewDrawn != this.view) {
            if (this.lastViewDrawn != null) {
                this.viewSwitchAt = this.nanoTime();
            }
            this.lastViewDrawn = this.view;
        }
        boolean bl17 = GpuClickGuiScreen.floatingView(this.view) || this.settingsDrawerOpen || this.themesDrawerOpen || this.colorPicker != null || this.modalOverlayOpen() || this.view == View.SCRIPTS && this.scriptView.fullscreen() || this.moduleFloatActive();
        float f76 = bl17 ? -100000.0f : f2;
        float f77 = bl17 ? -100000.0f : f3;
        boolean bl18 = bl = bl4 && !bl3;
        if (!bl) {
            this.drawRail(builder, f51, f16, f14, f15, bl3, f46, f76, f77);
            this.lastRailDrawW = f14;
        }
        this.drawCatalog(builder, f51 + f14, f16, f11, f15, false, f46, f76, f77);
        float f78 = (float)(this.nanoTime() - this.viewSwitchAt) / 1.0E9f;
        if (!this.reducedMotion() && this.viewSwitchAt != 0L && f78 < 0.24f && f78 >= 0.0f) {
            f8 = 1.0f - f78 / 0.24f;
            builder.rect(f51 + f14, f16, f11, f15, 0.0f, GpuClickGuiScreen.argb(184.0f * f8 * f8, 724758));
        }
        if (bl) {
            boolean bl19 = this.colorPicker == null && !this.modalOverlayOpen() && this.contextItems == null && this.dragModule == null && this.windowDrag == 0 && !this.closing;
            f7 = f14 + (f13 - f14) * this.railExpand;
            int n6 = this.tour.active() || bl19 && GpuClickGuiScreen.inside(f76, f77, f51, f16, Math.max(f7, f14), f15) ? 1 : 0;
            float f79 = f6 = n6 != 0 ? 1.0f : 0.0f;
            this.railExpand = this.reducedMotion() ? f6 : (this.railExpand += (f6 - this.railExpand) * Math.min(1.0f, this.frameDelta * 16.0f));
            if (Math.abs(this.railExpand - f6) < 0.004f) {
                this.railExpand = f6;
            }
            f5 = f14 + (f13 - f14) * this.railExpand;
            if (this.railExpand > 0.01f) {
                f4 = this.railExpand;
                builder.shadow(f51, f16, f5, f15, 0.0f, 18.0f * f46, 0.0f, GpuClickGuiScreen.argb(144.0f * f4, 0));
                builder.rect(f51, f16, f5, f15, 0.0f, GpuClickGuiScreen.argb(238.0f * f4, 592656));
                builder.rect(f51, f16, f5, f15, 0.0f, this.railBg);
                builder.rect(f51 + f5 - f12, f16, f12, f15, 0.0f, this.lineColor);
            }
            this.drawRail(builder, f51, f16, f5, f15, this.railExpand < 0.55f, f46, f76, f77);
            this.lastRailDrawW = f5;
        }
        this.tourRailRect[0] = f51;
        this.tourRailRect[1] = f16;
        this.tourRailRect[2] = this.lastRailDrawW;
        this.tourRailRect[3] = f15;
        this.tourCatalogRect[0] = f51 + f14;
        this.tourCatalogRect[1] = f16;
        this.tourCatalogRect[2] = f11;
        this.tourCatalogRect[3] = f15;
        this.drawHeader(builder, f51, f53, f47, f55, f46, f76, f77);
        if (f44 < 0.999f) {
            builder.rect(f51, f53, f47, f48, f54, GpuClickGuiScreen.argb(176.0f * (1.0f - f44), 658965));
        }
        builder.clipPop();
        if (!this.lg) {
            this.panelRim(builder, f51, f53, f47, f48, f54, f46);
        }
        if (f44 > 0.01f) {
            f8 = (float)(this.nanoTime() - this.openedAt) / 1.0E9f;
            f7 = this.reducedMotion() ? 0.0f : 0.5f + 0.5f * (float)Math.sin((double)this.nanoTime() / 2.2E9);
            float f80 = this.reducedMotion() ? 0.0f : Math.max(0.0f, 1.0f - f8 / 0.9f);
            builder.border(f51, f53, f47, f48, f54, Math.max(1.0f, f46), GpuClickGuiScreen.scaleAlpha(this.accentArgb() | 0xFF000000, f44 * (0.1f + 0.07f * f7 + 0.35f * f80)));
        }
        if (bl6) {
            f8 = f10;
            f7 = bl7 ? f53 : f16;
            float f81 = bl7 ? f48 : f48 - (f16 - f53);
            f6 = f71;
            if (f56 > 0.004f) {
                boolean bl20;
                f5 = this.panelRadius * f46;
                f4 = f8 + f23 * f46 + 64.0f * f46;
                if (bl7) {
                    builder.clipPush(f70, f7 - 48.0f * f46, f4, f81 + 96.0f * f46, 0.0f);
                } else {
                    builder.clipPush(f51, f53, f47, f48, f54);
                }
                this.panelShadow(builder, f6, f7, f8, f81, f5, f46, f56);
                builder.clipPush(f6, f7, f8, f81, f5);
                if (this.lg) {
                    this.glassPane(builder, f6, f7, f8, f81, this.panelRadius * f46, 1.0f, 68, false, n4);
                } else if (bl8) {
                    this.paintSurface(builder, string, f6, f7, f8, f81, this.panelRadius * f46, bl9);
                    builder.rect(f6, f7, f8, f81, 0.0f, this.listBg);
                } else {
                    builder.glass(f6, f7, f8, f81, this.panelRadius * f46, f58, n3);
                    this.drawGlassWash(builder, f6, f7, f8, f81, this.panelRadius * f46);
                }
                this.inspectorRevealHitClip = true;
                if (!bl7) {
                    this.hit(f72, f7, f73, f81, () -> {});
                }
                this.drawModuleInspector(builder, f6, f7, f8, f81, false, f46, (bl20 = GpuClickGuiScreen.inside(f76, f77, f72, f7, f73, f81)) ? f76 : -1.0f, bl20 ? f77 : -1.0f);
                this.inspectorRevealHitClip = false;
                builder.clipPop();
                if (!this.lg) {
                    this.panelRim(builder, f6, f7, f8, f81, f5, f46);
                }
                builder.clipPop();
            }
        }
        this.drawerRegionW = 0.0f;
        this.headerPanel.setBounds(new UiBounds(f51, f53, 0.0f, 0.0f));
        this.clippingHits = false;
        if (GpuClickGuiScreen.floatingView(this.view) && !this.closing && this.colorPicker == null && !this.modalOverlayOpen()) {
            this.drawFloatingPage(builder, f20, f21, f46, f2, f3);
        } else if (!GpuClickGuiScreen.floatingView(this.view)) {
            for (View view : View.values()) {
                this.springs.remove(new PageFloatKey(view));
            }
        }
        if ((this.settingsDrawerOpen || this.themesDrawerOpen) && !this.closing && this.colorPicker == null && !this.modalOverlayOpen()) {
            this.drawFloatingDrawer(builder, f20, f21, f46, f2, f3);
        } else {
            this.springs.remove(DRAWER_FLOAT);
        }
        if (this.view == View.CONFIGS && this.configsView.editing() && !this.closing && this.colorPicker == null && !this.modalOverlayOpen()) {
            this.drawConfigEditFloat(builder, f20, f21, f46, f2, f3);
        } else {
            this.springs.remove(CONFIG_EDIT_FLOAT);
        }
        if (this.moduleFloatActive() && !this.closing) {
            boolean bl21 = this.colorPicker != null || this.modalOverlayOpen();
            this.drawModuleFloat(builder, f20, f21, f46, bl21 ? -100000.0f : f2, bl21 ? -100000.0f : f3);
        }
        if (this.view == View.SCRIPTS && !this.closing && this.colorPicker == null && !this.modalOverlayOpen() && this.scriptView.fullscreen()) {
            this.hits.clear();
            this.moduleCardRightHits.clear();
            this.hoveredTooltipDesc = null;
            builder.rect(0.0f, 0.0f, f20, f21, 0.0f, GpuClickGuiScreen.withA(GpuClickGuiScreen.darken(UiKit.panelOpaque(), 0.3f), 232));
            float f82 = Math.round(14.0f * f46);
            float f83 = this.panelRadius * f46;
            builder.shadow(f82, f82, f20 - f82 * 2.0f, f21 - f82 * 2.0f, f83, Math.round(24.0f * f46), 0.0f, 0x70000000);
            builder.rect(f82, f82, f20 - f82 * 2.0f, f21 - f82 * 2.0f, f83, 0xFF000000 | this.railBg & 0xFFFFFF);
            builder.border(f82, f82, f20 - f82 * 2.0f, f21 - f82 * 2.0f, f83, 1.0f, this.lineColor);
            this.scriptView.draw(builder, this.kitHost, f82, f82, f20 - f82 * 2.0f, f21 - f82 * 2.0f, f46, f2, f3);
        }
        if (this.colorPicker != null) {
            this.drawColorPicker(builder, f20, f21, f46, f2, f3);
        } else if (this.crosshairMaker != null) {
            this.drawCrosshairMaker(builder, f20, f21, f46, f2, f3);
        } else {
            if (!this.closing) {
                this.tour.maybeAutoStart();
            }
            if (this.whatsNew.isOpen()) {
                this.hits.clear();
                this.moduleCardRightHits.clear();
                this.hoveredTooltipDesc = null;
                this.whatsNew.draw(builder, this.kitHost, f20, f21, f46, f2, f3);
            } else if (this.tour.active()) {
                this.hits.clear();
                this.moduleCardRightHits.clear();
                this.hoveredTooltipDesc = null;
                this.tour.draw(builder, this.kitHost, f20, f21, f46, this.tourRect(), f2, f3);
            } else if (this.contextItems != null) {
                this.drawContextMenu(builder, f20, f21, f46, f2, f3);
            }
        }
        if (this.colorPicker != null || this.modalOverlayOpen()) {
            this.closeContextMenu();
        }
        if (this.dragModule != null) {
            this.drawDragGhost(builder, f46);
        }
        if (this.hoveredTooltipDesc != null && !this.hoveredTooltipDesc.isBlank() && this.dragModule == null) {
            this.drawFloatingTooltip(builder, f2, f3, f20, f21, f46);
        }
        this.headerPanel.setEnabled(this.colorPicker == null && !this.modalOverlayOpen() && this.contextItems == null);
        if (HitboxConfigScreen752809.ac() && this.field_22787 != null && this.field_22787.method_22683() != null) {
            ClassicIcons.drawCursor(builder, f2, f3, this.field_22787.method_22683().method_4495());
        }
        if (this.colorPicker == null && !this.modalOverlayOpen() && this.contextItems == null && this.dragModule == null) {
            this.registerWindowZones(f51, f53, f47, f48, f46, f2, f3);
        } else if (this.contextItems != null || this.dragModule != null) {
            this.updateResizeCursor(0);
        }
        this.rebuildInputTree(f20, f21);
        return builder.build();
    }

    private void panelShadow(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (f8 <= 0.01f || f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        float f9 = (8.0f + 11.0f * this.richness) * f7;
        int n2 = Math.round(60.0f + 64.0f * this.richness);
        builder.shadow(f2, f3 + (2.5f + 2.0f * this.richness) * f7, f4, f5, f6, f9, 0.0f, GpuClickGuiScreen.argb((float)n2 * f8, 0));
    }

    private void panelRim(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (f4 <= 0.0f || f5 <= 0.0f || !this.showRims) {
            return;
        }
        builder.border(f2, f3, f4, f5, f6, Math.max(1.1f, f7 * 1.1f), this.outerBorder);
        builder.rect(f2 + f6, f3 + 0.5f * f7, Math.max(0.0f, f4 - f6 * 2.0f), Math.max(1.0f, f7), 0.5f, GpuClickGuiScreen.argb(44.0f * Math.min(1.0f, this.rimAlphaK), this.rimRgb));
    }

    private void glassPane(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, int n2, boolean bl, int n3) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        int n4 = n3 >>> 24 != 0 ? GpuClickGuiScreen.scaleAlpha(n3, (float)n2 / 68.0f * this.glassOpacityK) : GpuClickGuiScreen.argb((float)n2 * this.glassOpacityK, 329740);
        UiGlass.pane(builder, f2, f3, f4, f5, f6, f7, this.glassBlurValue, n4, this.rimRgb, this.showRims, bl, this.rimAlphaK);
    }

    private static int scaleAlpha(int n2, float f2) {
        int n3 = Math.round((float)(n2 >>> 24 & 0xFF) * Math.max(0.0f, f2));
        return Math.min(255, n3) << 24 | n2 & 0xFFFFFF;
    }

    private static boolean overlayCaptureActive() {
        try {
            return OverlayCompositor.isOverlayCaptureActive();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private void accentFill(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
        int n3;
        if (f7 <= 0.003f || f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        int n4 = n3 = n2 != 0 ? n2 : this.accentArgb();
        if (n2 == 0 && this.gradientAccents) {
            int n5 = GpuClickGuiScreen.scaleAlpha(n3 | 0xFF000000, f7);
            int n6 = GpuClickGuiScreen.scaleAlpha(this.accent2 | 0xFF000000, f7);
            builder.gradient(f2, f3, f4, f5, f6, n5, n6, n6, n5);
        } else {
            builder.rect(f2, f3, f4, f5, f6, GpuClickGuiScreen.scaleAlpha(n3 | 0xFF000000, f7 * ((float)(n3 >>> 24 & 0xFF) / 255.0f)));
        }
    }

    private int hoverWash(int n2) {
        return GpuClickGuiScreen.argb((float)n2 * this.hoverK, this.hoverRgb);
    }

    private void paintSurface(UiDisplayList.Builder builder, String string, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        if (!this.surface.isTheme()) {
            if (this.surface.seeThrough()) {
                builder.glass(f2, f3, f4, f5, f6, 8.0f, 704972813);
            }
            float f7 = (float)HitboxConfigScreen752809.d.B() / 100.0f;
            float f8 = (float)HitboxConfigScreen752809.e.B() / 100.0f;
            float f9 = (float)HitboxConfigScreen752809.f.B() / 100.0f;
            builder.material(f2, f3, f4, f5, f6, this.surface.material(f7, f9, -2.0f, -2.0f, 0.0f, false, UiThemeMaterial.theme(string).accent() & 0xFFFFFF, f8));
            if (HitboxConfigScreen752809.L()) {
                this.gradientWash(builder, f2, f3, f4, f5, f6);
            }
            return;
        }
        builder.material(f2, f3, f4, f5, f6, UiThemeMaterial.forTheme(string));
        if (bl) {
            builder.material(f2, f3, f4, f5, f6, UiClassicMistMaterial.flux());
        }
        if (UiThemeMaterial.isStone(string)) {
            int n2 = this.accentArgb() & 0xFFFFFF;
            builder.gradient(f2, f3, f4, f5, f6, n2 | 0x1E000000, n2 | 0x6000000, n2 | 0x10000000, 0);
        }
        if (HitboxConfigScreen752809.L()) {
            this.gradientWash(builder, f2, f3, f4, f5, f6);
        }
    }

    private void applyPalette(UiThemeMaterial.Theme theme) {
        boolean bl;
        boolean bl2 = theme.stone();
        int n2 = theme.tint() & 0xFFFFFF;
        int n3 = theme.deep() & 0xFFFFFF;
        this.cornerScale = HitboxConfigScreen752809.Z == null ? 1.0f : GpuClickGuiScreen.clampRange((float)HitboxConfigScreen752809.Z.B() / 10.0f, 0.0f, 3.0f);
        this.panelRadius = 24.0f * this.cornerScale;
        this.cardRadius = 18.0f * this.cornerScale;
        this.richness = HitboxConfigScreen752809.aa != null && HitboxConfigScreen752809.aa.is(GpuClickGuiScreen.bf("\uad7c\uab59\ud1a8\u8190\ufe3b\uac84\ue713", -385661577, 0x1B1EF9FC ^ CryptoVault.mask())) ? 1.0f : 0.0f;
        this.showSeparators = HitboxConfigScreen752809.Q();
        this.showSidebarSeparator = HitboxConfigScreen752809.R();
        this.showLogoSeparator = HitboxConfigScreen752809.S();
        this.showRims = HitboxConfigScreen752809.Z();
        this.hoverK = !HitboxConfigScreen752809.T() ? 0.0f : Math.min(2.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.ae, 1.0f));
        this.panelOpacityK = Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.ah, 1.0f));
        this.glassOpacityK = Math.min(2.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.ag, 1.0f));
        this.glassBlurValue = HitboxConfigScreen752809.af == null ? 16.0f : (float)Math.max(0.0, HitboxConfigScreen752809.af.B());
        this.panelTintOverride = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ai, 0);
        this.textColor = HitboxConfigScreen752809.a(HitboxConfigScreen752809.al, theme.text());
        boolean bl3 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.am, 0) != 0;
        this.mutedColor = HitboxConfigScreen752809.a(HitboxConfigScreen752809.am, theme.muted());
        this.muted2Color = bl3 ? GpuClickGuiScreen.scaleAlpha(this.mutedColor | 0xFF000000, 0.8f) : theme.muted2();
        boolean bl4 = bl = HitboxConfigScreen752809.a(HitboxConfigScreen752809.al, 0) != 0;
        this.titleColor = HitboxConfigScreen752809.g() && HitboxConfigScreen752809.ad != null ? HitboxConfigScreen752809.ad.A().getRGB() | 0xFF000000 : (bl ? this.textColor : -591879);
        this.toggleOffColor = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ao, theme.toggleOff());
        this.toggleOnColor = HitboxConfigScreen752809.a(HitboxConfigScreen752809.an, 0);
        this.sliderFillColor = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ap, 0);
        int n4 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.aq, 0);
        this.hoverRgb = n4 != 0 ? n4 & 0xFFFFFF : 0xFFFFFF;
        this.selectionRing = n4 != 0 ? n4 | 0xFF000000 : this.accentArgb();
        this.hoverHl = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bf, 0);
        this.sidebarHl = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bg, 0);
        this.enabledHl = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bh, 0);
        this.tooltipBg = HitboxConfigScreen752809.a(HitboxConfigScreen752809.bi, 0);
        this.hoverGlowK = Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.bj, 1.0f));
        this.sidebarGlowK = Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.bk, 1.0f));
        this.enabledK = Math.min(1.0f, HitboxConfigScreen752809.a(HitboxConfigScreen752809.bl, 1.0f));
        this.controlRgb = n2;
        this.gradientAccents = HitboxConfigScreen752809.M() && HitboxConfigScreen752809.N() && HitboxConfigScreen752809.L != null;
        this.accent2 = this.gradientAccents ? HitboxConfigScreen752809.L.A().getRGB() | 0xFF000000 : GpuClickGuiScreen.mixRgb(this.accentArgb(), -1, 0.35f);
        int n5 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ar, 0);
        this.rimRgb = n5 != 0 ? n5 & 0xFFFFFF : 0xFFFFFF;
        this.rimAlphaK = (n5 != 0 ? Math.min(2.0f, (float)(n5 >>> 24 & 0xFF) / 48.0f) : 1.0f) * (0.75f + 0.6f * this.richness);
        this.outerBorder = n5 != 0 ? n5 : theme.outer();
        this.lineColor = 0x1A000000 | n2;
        this.lineBorderColor = (bl2 ? 0x26000000 : 0x1F000000) | n2;
        this.lineSubtleColor = 0xB000000 | n2;
        this.railBg = GpuClickGuiScreen.scaleAlpha((bl2 ? -1509949440 : -1946157056) | n3, this.panelOpacityK);
        this.listBg = GpuClickGuiScreen.scaleAlpha((bl2 ? 0x73000000 : 0x52000000) | n3, this.panelOpacityK);
        this.cardBg = GpuClickGuiScreen.scaleAlpha(HitboxConfigScreen752809.a(HitboxConfigScreen752809.aj, this.lg ? Integer.MIN_VALUE : (bl2 ? 0x66000000 : 0x47000000)), this.panelOpacityK);
        this.cardFillCustom = HitboxConfigScreen752809.a(HitboxConfigScreen752809.aj, 0) != 0;
        int n6 = HitboxConfigScreen752809.a(HitboxConfigScreen752809.ak, 0);
        boolean bl5 = this.cardBorderCustom = n6 != 0;
        this.cardBorder = n6 != 0 ? n6 : (bl2 ? 0x2E000000 : 0x29000000) | n2;
    }

    private void drawGlassWash(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        if (HitboxConfigScreen752809.L()) {
            this.gradientWash(builder, f2, f3, f4, f5, f6);
            return;
        }
        if (this.lg) {
            return;
        }
        int n2 = this.currentAccent & 0xFFFFFF;
        builder.gradient(f2, f3, f4, f5, f6, n2 | 0x26000000, n2 | 0x8000000, n2 | 0x14000000, 0);
    }

    private void gradientWash(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        int n2 = this.currentAccent & 0xFFFFFF;
        int n3 = this.accent2 & 0xFFFFFF;
        int n4 = GpuClickGuiScreen.mixRgb(this.currentAccent, this.accent2, 0.5f) & 0xFFFFFF;
        builder.gradient(f2, f3, f4, f5, f6, n2 | 0x30000000, n4 | 0x16000000, n3 | 0x26000000, n4 | 0x10000000);
    }

    private float easeScroll(float f2, float f3) {
        float f4 = f2 + (f3 - f2) * (1.0f - (float)Math.exp(-this.frameDelta * 18.0f));
        return Math.abs(f4 - f3) < 0.05f ? f3 : f4;
    }

    private String animKey(Object object2, int n2, String string) {
        String[] stringArray = this.animKeyCache.computeIfAbsent(object2, object -> new String[6]);
        String string2 = stringArray[n2];
        if (string2 == null) {
            String string3;
            StringBuilder stringBuilder = new StringBuilder().append(string);
            if (object2 instanceof Module) {
                Module module = (Module)object2;
                string3 = module.getName();
            } else {
                string3 = String.valueOf(System.identityHashCode(object2));
            }
            stringArray[n2] = string2 = stringBuilder.append(string3).toString();
        }
        return string2;
    }

    private float anim(Object object, float f2, float f3) {
        return this.springs.ease(object, f2, f3, this.frameDelta);
    }

    private float spring(Object object, float f2, float f3, float f4) {
        return this.springs.spring(object, f2, f3, f4, this.frameDelta);
    }

    private static int argb(float f2, int n2) {
        return Math.max(0, Math.min(255, Math.round(f2))) << 24 | n2 & 0xFFFFFF;
    }

    private void glassTile(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n2 = (this.tileActiveOverride != 0 ? this.tileActiveOverride : this.accentArgb()) & 0xFFFFFF;
        f8 = Math.max(0.0f, f8) * this.hoverK;
        if (this.lg) {
            this.glassPane(builder, f2, f3, f4, f5, f6, 1.0f, Math.round(44.0f + 10.0f * Math.min(1.0f, f8)), false, 0);
            if (this.cardFillCustom) {
                builder.rect(f2, f3, f4, f5, f6, this.cardBg);
            }
            if (f8 > 0.01f && this.hoverHl != 0) {
                int n3 = this.hoverHl & 0xFFFFFF;
                builder.gradient(f2, f3, f4, f5, f6, GpuClickGuiScreen.argb(20.0f * this.hoverK, n3), GpuClickGuiScreen.argb(16.0f * this.hoverK, n3), GpuClickGuiScreen.argb(4.0f * this.hoverK, n3), GpuClickGuiScreen.argb(6.0f * this.hoverK, n3));
            } else if (f8 > 0.01f) {
                builder.gradient(f2, f3, f4, f5, f6, this.hoverWash(20), this.hoverWash(16), this.hoverWash(4), this.hoverWash(6));
            }
            if (f9 > 0.01f) {
                this.activeWash(builder, f2, f3, f4, f5, f6, f9, n2);
            }
            if (this.cardBorderCustom) {
                builder.border(f2, f3, f4, f5, f6, Math.max(1.0f, f7 * 0.8f), this.cardBorder);
            }
            return;
        }
        if (this.cardFillCustom) {
            builder.rect(f2, f3, f4, f5, f6, this.cardBg);
        }
        float f10 = 20.0f + 14.0f * f8 + 6.0f * f9;
        float f11 = 5.0f + 6.0f * f8;
        builder.gradient(f2, f3, f4, f5, f6, GpuClickGuiScreen.argb(f10, this.hoverRgb), GpuClickGuiScreen.argb(f10, this.hoverRgb), GpuClickGuiScreen.argb(f11, this.hoverRgb), GpuClickGuiScreen.argb(f11, this.hoverRgb));
        if (f9 > 0.01f) {
            this.activeWash(builder, f2, f3, f4, f5, f6, f9, n2);
        }
        int n4 = this.cardBorderCustom ? this.cardBorder : GpuClickGuiScreen.argb(32.0f + 20.0f * f8 + 24.0f * f9, f9 > 0.01f ? GpuClickGuiScreen.mixRgb(0xFFFFFF, n2, f9 * 0.35f) : 0xFFFFFF);
        builder.border(f2, f3, f4, f5, f6, Math.max(1.0f, f7 * 0.8f), n4);
        if (f9 > 0.01f) {
            float f12 = f6 * 0.7f;
            builder.rect(f2 + f12, f3 + Math.max(1.0f, f7 * 0.6f), Math.max(0.0f, f4 - f12 * 2.0f), Math.max(1.2f, 1.5f * f7), 0.75f, GpuClickGuiScreen.argb(Math.round(112.0f * Math.min(1.0f, f9)), n2));
        } else {
            builder.rect(f2 + f6 * 0.8f, f3 + f7 * 0.5f, Math.max(0.0f, f4 - f6 * 1.6f), Math.max(1.0f, f7 * 0.8f), 0.5f, GpuClickGuiScreen.argb(Math.min(255.0f, 48.0f + 32.0f * f8), 0xFFFFFF));
        }
    }

    private void activeWash(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
        int n3 = this.gradientAccents && this.tileActiveOverride == 0 ? this.accent2 & 0xFFFFFF : n2;
        builder.gradient(f2, f3, f4, f5, f6, GpuClickGuiScreen.argb(24.0f * f7, n2), GpuClickGuiScreen.argb(14.0f * f7, n3), GpuClickGuiScreen.argb(8.0f * f7, n3), GpuClickGuiScreen.argb(16.0f * f7, n2));
    }

    private static float clampRange(float f2, float f3, float f4) {
        if (f4 < f3) {
            return f3;
        }
        return Math.max(f3, Math.min(f4, f2));
    }

    private void drawHeader(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        int n2;
        int n3;
        float f9;
        float f10;
        float f11;
        int n4;
        float f12;
        boolean bl;
        if (!this.lg) {
            builder.rect(f2, f3, f4, f5, 0.0f, this.railBg);
        }
        if (this.showLogoSeparator) {
            builder.rect(f2, f3 + f5 - 1.0f, f4, 1.0f, 0.0f, this.lineColor);
        }
        this.hit(f2, f3, f4, f5, this::beginWindowMove);
        float f13 = Math.round(20.0f * f6);
        ClassicIcons.drawLogo(builder, f2 + (float)Math.round(18.0f * f6), f3 + (f5 - f13) * 0.5f, f13, this.titleColor);
        boolean bl2 = bl = f4 < 700.0f * f6;
        if (!bl) {
            builder.text(f2 + (float)Math.round(18.0f * f6) + f13 + (float)Math.round(9.0f * f6), GpuUiFontMetrics.topInBox(f3, f5, 12.0f * f6), 12.0f * f6, this.titleColor, GpuClickGuiScreen.bf("\u8ba3\u9403\ufbc0\u5e6a\u3e43\ub46c\u2689", 261854359, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        }
        float f14 = Math.round(30.0f * f6);
        float f15 = Math.round(3.0f * f6);
        float f16 = f14 - f15 * 2.0f;
        float f17 = Math.round(2.0f * f6);
        float[] fArray = new float[]{Math.round(78.0f * f6), Math.round(92.0f * f6), Math.round(70.0f * f6), Math.round(66.0f * f6), Math.round(84.0f * f6), Math.round(88.0f * f6), Math.round(100.0f * f6), Math.round(66.0f * f6), Math.round(74.0f * f6), Math.round(82.0f * f6)};
        boolean bl3 = this.cosmeticsUnlocked();
        if (!bl3) {
            fArray[4] = 0.0f;
        }
        float f18 = f12 = f17 * (float)(fArray.length - (bl3 ? 1 : 2));
        float[] fArray2 = fArray;
        int n5 = fArray2.length;
        for (n4 = 0; n4 < n5; ++n4) {
            f11 = fArray2[n4];
            f18 += f11;
        }
        float f19 = Math.max(0.0f, f4 - (float)Math.round((bl ? 110.0f : 300.0f) * f6));
        if (f18 > f19 && f18 > 0.0f) {
            float f20 = f19 / f18;
            f18 = f12;
            for (n4 = 0; n4 < fArray.length; ++n4) {
                fArray[n4] = Math.round(fArray[n4] * f20);
                f18 += fArray[n4];
            }
        }
        float f21 = f18 + f15 * 2.0f;
        float f22 = Math.round(f2 + (f4 - f21) * 0.5f);
        f11 = Math.round(f3 + (f5 - f14) * 0.5f);
        float f23 = Math.min(1.0f, this.cornerScale);
        float f24 = f14 * 0.5f * f23;
        if (this.lg) {
            this.glassPane(builder, f22, f11, f21, f14, f24, 1.0f, 48, false, 0);
        } else {
            builder.rect(f22, f11, f21, f14, f24, GpuClickGuiScreen.argb(64.0f * this.panelOpacityK, 0));
            builder.border(f22, f11, f21, f14, f24, 1.0f, this.lineBorderColor);
        }
        float f25 = Math.min(1.0f, this.frameDelta * 16.0f);
        if (this.topPillW <= 0.0f) {
            this.topPillX = this.topPillTX;
            this.topPillW = this.topPillTW;
        }
        this.topPillX += (this.topPillTX - this.topPillX) * f25;
        this.topPillW += (this.topPillTW - this.topPillW) * f25;
        float f26 = this.anim(GpuClickGuiScreen.bf("\u77ee\uda95\u20a7\u9c32\u6da0\ucc50\u4aa4\u6243\u8422\u1781\ub57d\u7385\u6e5e", -245050569, 0x1B1EF9FC ^ CryptoVault.mask()), this.topPillShow ? 1.0f : 0.0f, 14.0f);
        this.topPillShow = false;
        if (f26 > 0.01f && this.topPillW > 0.0f) {
            f10 = this.topPillX;
            f9 = f11 + f15;
            float f27 = f16 * 0.5f * f23;
            builder.gradient(f10, f9, this.topPillW, f16, f27, GpuClickGuiScreen.argb(48.0f * f26, 0), GpuClickGuiScreen.argb(48.0f * f26, 0), GpuClickGuiScreen.argb(144.0f * f26, 0), GpuClickGuiScreen.argb(144.0f * f26, 0));
            if (this.gradientAccents) {
                n3 = this.accentArgb() & 0xFFFFFF;
                n2 = this.accent2 & 0xFFFFFF;
                builder.gradient(f10, f9, this.topPillW, f16, f27, GpuClickGuiScreen.argb(34.0f * f26, n3), GpuClickGuiScreen.argb(34.0f * f26, n2), GpuClickGuiScreen.argb(16.0f * f26, n2), GpuClickGuiScreen.argb(16.0f * f26, n3));
            }
            builder.border(f10, f9, this.topPillW, f16, f27, 1.0f, GpuClickGuiScreen.argb(64.0f * f26, 0xFFFFFF));
        }
        f10 = f22 + f15;
        this.drawTopNavigationItem(builder, f10, f11 + f15, fArray[0], f16, f6, f7, f8, GpuClickGuiScreen.bf("\ue3ae\ucdb4\u3556\u2847\uf478\u471b\u2e2d\u88ce", 202023342, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ub3b9\u929e\uaba5\u5404\u5bed\u618a\ub29b\u4da0\u653a\ufce1\u2b79\ud8ec", -1308064734, 0x1B1EF9FC ^ CryptoVault.mask()), this.settingsDrawerOpen, () -> {
            if (GpuClickGuiScreen.floatingView(this.view)) {
                this.closePage();
            }
            boolean bl = this.settingsDrawerOpen = !this.settingsDrawerOpen;
            if (this.settingsDrawerOpen) {
                this.themesDrawerOpen = false;
            }
            this.appearanceScroll = 0.0f;
        });
        this.drawTopNavigationItem(builder, f10 += fArray[0] + f17, f11 + f15, fArray[1], f16, f6, f7, f8, GpuClickGuiScreen.bf("\ud9fd\u25cd\ufa97\uced4\uf674\uc531\u4a03\ucff7\u412a", -1499725624, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ufdd8\uad43\udbed\uc8ea\uabd5\u4fd7\u54bd\ub122\udd89\ud277\u6fd6", -1402315520, 0x1B1EF9FC ^ CryptoVault.mask()), this.themesDrawerOpen, () -> {
            if (GpuClickGuiScreen.floatingView(this.view)) {
                this.closePage();
            }
            this.themesDrawerOpen = !this.themesDrawerOpen;
            this.themeMakerOpen = false;
            if (this.themesDrawerOpen) {
                this.settingsDrawerOpen = false;
            }
            this.appearanceScroll = 0.0f;
        });
        this.drawTopNavigationItem(builder, f10 += fArray[1] + f17, f11 + f15, fArray[2], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u9714\u51b4\u5e22\udf56\ua1c2\u106a\u495c\u27f0\u4f47", 1968391188, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u06cd\ue68b\u413f\uad9f\uf758\ubbd7\uca59\ud91a\ub9f4\u3625", -1508568165, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.CONFIGS, () -> {
            this.view = View.CONFIGS;
            this.refreshSavedConfigs();
        });
        this.drawTopNavigationItem(builder, f10 += fArray[2] + f17, f11 + f15, fArray[3], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u8ae4\u96fb\uffa6\u1d99\u699d\u495c\u41fd\u3754", -133639348, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ua77b\ub105\u3480\u5ab6\u13a9\u3fab\ubec1\u95cd", 2109182641, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.FRIENDS, () -> {
            this.view = View.FRIENDS;
        });
        f10 += fArray[3] + f17;
        if (bl3) {
            this.drawTopNavigationItem(builder, f10, f11 + f15, fArray[4], f16, f6, f7, f8, GpuClickGuiScreen.bf("\ub195\u4271\u8524\u7af4\u6977\u6bb0\u2eb4\u37f6\u3098\udd1a", -465420597, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u1496\ubcf4\u4cf7\u2a00\u28ad\u9ffe\uccc6\ufbed\uae1d\u10ee\u042c", -1104008621, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.COSMETICS, () -> {
                if (this.cosmeticsUnlocked()) {
                    this.view = View.COSMETICS;
                }
            });
            f10 += fArray[4] + f17;
        }
        this.drawTopNavigationItem(builder, f10, f11 + f15, fArray[5], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u7dd1\ubf1a\u9d62\u87bb\uc4f3\ueb0c\u36f9\u05ae\u9f04\u4ba0\uffb2\u92c8", 99475163, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uaeb5\u8f3f\u3be5\u5f2a\u926c\ufb50\u63fe\ubd49\u4f85\u634f\u4830\uec4a\uda68\u0382", 1010092234, 0x1B1EF9FC ^ CryptoVault.mask()), false, () -> {
            if (this.field_22787 != null && this.field_22787.field_1687 == null) {
                ClickGuiRouter.toast(GpuClickGuiScreen.bf("\ua3c9\ueef6\u6e89\u33d1\udebc\u8cf2\uca82\u5cb5\u65f9\u861f\ubddb", -880615575, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u9377\u66c6\u6d92\u7034\ueed2\u4360\u4115\u9123\ua393\ufafd\ufea3\ub1ce\uacbf\u6fc2\u2cff\u671a\u4803\u7fe5\u69bc\u6d84\u9ff2\ucf23\u3c0b\ub7f0\u1957\u13b7\u334d\u4376\u572e\u7b3d\u2d24", -1425972622, 0x1B1EF9FC ^ CryptoVault.mask()));
            } else if (this.field_22787 != null) {
                McCompat.setScreen(this.field_22787, new HudEditorScreen(this));
            }
        });
        this.drawTopNavigationItem(builder, f10 += fArray[5] + f17, f11 + f15, fArray[6], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u0727\ucdd8\u55f1\u80e5\u5b2a\ue269", 1765569192, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u3000\u6edf\ub5fa\uefea\ub6c1\u67fa\ub17e\ubdd3\u02c6\u1285\ubaeb\u37c9\ue7bb\u0860\u82d6\u7e2f", 843048628, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.SELF_DESTRUCT, () -> {
            this.view = View.SELF_DESTRUCT;
            this.settingsDrawerOpen = false;
            this.themesDrawerOpen = false;
            this.sdArmed = false;
        });
        this.drawTopNavigationItem(builder, f10 += fArray[6] + f17, f11 + f15, fArray[7], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u27f6\u081f\uffa2\u0f55\uf181\ua0cc\uc3d7\u449e", 1760169493, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ua37b\u021c\ucb00\ua7ff\u10c1\u6c99\u878c\u5cd9\u792d", -1327431678, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.LAYOUT, () -> {
            this.view = View.LAYOUT;
            this.settingsDrawerOpen = false;
            this.themesDrawerOpen = false;
        });
        this.drawTopNavigationItem(builder, f10 += fArray[7] + f17, f11 + f15, fArray[8], f16, f6, f7, f8, GpuClickGuiScreen.bf("\u13ed\u8e36\u5ac3\u2d86\u9c83\u7d2e\uc045\ua0cd\u9240\u7185", -1860412489, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uf419\u92fc\u0171\u8945\u053a\ub308\u6451\uaefe\ua062\u266b\uaba0\u709d", -2040231127, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.SS_PROOF, () -> {
            this.view = View.SS_PROOF;
            this.settingsDrawerOpen = false;
            this.themesDrawerOpen = false;
        });
        this.drawTopNavigationItem(builder, f10 += fArray[8] + f17, f11 + f15, fArray[9], f16, f6, f7, f8, GpuClickGuiScreen.bf("\udf3b\uff87\u6daf\u9457\uf079", -184985717, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u1f44\u3ffb\u729c\u833c\ue902\u7bac\u073e\ub2d4\u4cda\u6120\ue47b\u2e63", -550047798, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.KEYBINDS, () -> {
            this.keybindsView.reset();
            this.view = View.KEYBINDS;
            this.settingsDrawerOpen = false;
            this.themesDrawerOpen = false;
        });
        f9 = f22 + f15;
        int[] nArray = new int[]{0, -1, 1, 2, -1, 3, 4, -1, -1, -1};
        for (n3 = 0; n3 < fArray.length; ++n3) {
            n2 = nArray[n3];
            if (n2 >= 0 && n2 < this.tourNavRects.length) {
                this.tourNavRects[n2][0] = f9;
                this.tourNavRects[n2][1] = f11 + f15;
                this.tourNavRects[n2][2] = fArray[n3];
                this.tourNavRects[n2][3] = f16;
            }
            if (!(fArray[n3] > 0.0f)) continue;
            f9 += fArray[n3] + f17;
        }
        float f28 = 9.5f * f6;
        float f29 = Math.round(GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u24d9\u95ef\u817d\ubd00\ue9d8\u779c\ued6f\ud109\uaa62\ueca8\uf395\ub8f9\uec1f", 592794774, 0x1B1EF9FC ^ CryptoVault.mask()), f28, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f6);
        float f30 = Math.round(24.0f * f6);
        float f31 = Math.round(f2 + f4 - f29 - 14.0f * f6);
        float f32 = Math.round(f3 + (f5 - f30) * 0.5f);
        if (f31 > f22 + f21 + 10.0f * f6) {
            boolean bl4 = this.hovered(f7, f8, f31, f32, f29, f30);
            builder.rect(f31, f32, f29, f30, f30 * 0.5f * Math.min(1.0f, this.cornerScale), this.controlFill(bl4 ? 48 : 22));
            builder.border(f31, f32, f29, f30, f30 * 0.5f * Math.min(1.0f, this.cornerScale), 1.0f, this.lineBorderColor);
            GpuClickGuiScreen.textCentered(builder, f31 + f29 * 0.5f, f32 + f30 * 0.5f, f28, bl4 ? this.textColor : this.mutedColor, GpuClickGuiScreen.bf("\ubf0c\uc0d0\uecbd\u4b7f\ud774\ucdb0\u66e4\u0cf1\u37ec\u5a70\u75fa\u4da8", -1893500338, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            if (WhatsNewPanel.hasUnread()) {
                builder.circle(f31 + f29 - 5.0f * f6, f32 + 5.0f * f6, 3.6f * f6, this.accentArgb());
            }
            this.hit(f31, f32, f29, f30, this.whatsNew::open);
        }
    }

    private void beginWindowMove() {
        this.windowDrag = 16;
        this.dragStartOffX = windowOffX;
        this.dragStartOffY = windowOffY;
    }

    private void beginWindowResize(int n2) {
        this.windowDrag = n2;
        this.dragStartL = this.lastWinX;
        this.dragStartT = this.lastWinY;
        this.dragStartW = this.lastWinW;
        this.dragStartH = this.lastWinH;
    }

    private static int edgesAt(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.max(6.0f, (float)Math.round(7.0f * f8));
        float f10 = f9 * 0.5f;
        float f11 = f9 * 0.6f;
        if (f2 < f4 - f10 || f2 > f4 + f6 + f10 || f3 < f5 - f10 || f3 > f5 + f7 + f10) {
            return 0;
        }
        int n2 = 0;
        if (f2 <= f4 + f11) {
            n2 |= 1;
        } else if (f2 >= f4 + f6 - f11) {
            n2 |= 2;
        }
        if (f3 <= f5 + f11) {
            n2 |= 4;
        } else if (f3 >= f5 + f7 - f11) {
            n2 |= 8;
        }
        return n2;
    }

    private void registerWindowZones(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.max(6.0f, (float)Math.round(7.0f * f6));
        float f10 = f9 * 0.5f;
        float f11 = f9 * 0.6f;
        float f12 = f9 * 2.4f;
        this.hit(f2 - f10, f3 + f12, f10 + f11, f5 - f12 * 2.0f, () -> this.beginWindowResize(1));
        this.hit(f2 + f4 - f11, f3 + f12, f10 + f11, f5 - f12 * 2.0f, () -> this.beginWindowResize(2));
        this.hit(f2 + f12, f3 - f10, f4 - f12 * 2.0f, f10 + f11, () -> this.beginWindowResize(4));
        this.hit(f2 + f12, f3 + f5 - f11, f4 - f12 * 2.0f, f10 + f11, () -> this.beginWindowResize(8));
        this.hit(f2 - f10, f3 - f10, f12 + f10, f12 + f10, () -> this.beginWindowResize(5));
        this.hit(f2 + f4 - f12, f3 - f10, f12 + f10, f12 + f10, () -> this.beginWindowResize(6));
        this.hit(f2 - f10, f3 + f5 - f12, f12 + f10, f12 + f10, () -> this.beginWindowResize(9));
        this.hit(f2 + f4 - f12, f3 + f5 - f12, f12 + f10, f12 + f10, () -> this.beginWindowResize(10));
        int n2 = this.windowDrag == 16 ? 16 : (this.windowDrag >= 1 && this.windowDrag < 16 ? this.windowDrag : GpuClickGuiScreen.edgesAt(f7, f8, f2, f3, f4, f5, f6));
        this.updateResizeCursor(n2);
    }

    private void dragWindow(double d2, double d3) {
        if (this.windowDrag == 0 || this.lastFitS <= 0.0f) {
            return;
        }
        float f2 = this.lastFitS;
        float f3 = (float)(d2 - this.pressX);
        float f4 = (float)(d3 - this.pressY);
        if (this.windowDrag == 16) {
            windowOffX = this.dragStartOffX + f3 / f2;
            windowOffY = this.dragStartOffY + f4 / f2;
            return;
        }
        float f5 = this.dragStartL;
        float f6 = this.dragStartT;
        float f7 = this.dragStartL + this.dragStartW;
        float f8 = this.dragStartT + this.dragStartH;
        if ((this.windowDrag & 1) != 0) {
            f5 += f3;
        }
        if ((this.windowDrag & 2) != 0) {
            f7 += f3;
        }
        if ((this.windowDrag & 4) != 0) {
            f6 += f4;
        }
        if ((this.windowDrag & 8) != 0) {
            f8 += f4;
        }
        float f9 = Math.round(14.0f * f2);
        float f10 = Math.max(this.minWinW * f2, Math.min(1400.0f * f2, this.lastSw - f9 * 2.0f - this.lastPanelsW * f2));
        float f11 = Math.max(400.0f * f2, Math.min(1000.0f * f2, this.lastSh - f9 * 2.0f));
        float f12 = Math.max(this.minWinW * f2, Math.min(f10, f7 - f5));
        float f13 = Math.max(400.0f * f2, Math.min(f11, f8 - f6));
        if ((this.windowDrag & 1) != 0) {
            f5 = f7 - f12;
        } else {
            f7 = f5 + f12;
        }
        if ((this.windowDrag & 4) != 0) {
            f6 = f8 - f13;
        } else {
            f8 = f6 + f13;
        }
        windowW = f12 / f2;
        windowH = f13 / f2;
        float f14 = f12 + this.lastPanelsW * f2;
        windowOffX = (f5 - (this.lastSw - f14) * 0.5f) / f2;
        windowOffY = (f6 - (this.lastSh - f13) * 0.5f) / f2;
    }

    private void updateResizeCursor(int n2) {
        int n3;
        if (this.field_22787 == null || this.field_22787.method_22683() == null || HitboxConfigScreen752809.ac()) {
            return;
        }
        if (n2 == 16) {
            n3 = 221193;
        } else if (n2 == 0) {
            n3 = 0;
        } else {
            boolean bl;
            boolean bl2 = (n2 & 3) != 0;
            boolean bl3 = bl = (n2 & 0xC) != 0;
            if (bl2 && bl) {
                boolean bl4 = (n2 & 1) != 0 && (n2 & 4) != 0 || (n2 & 2) != 0 && (n2 & 8) != 0;
                n3 = bl4 ? 221191 : 221192;
            } else {
                n3 = bl2 ? 221189 : 221190;
            }
        }
        if (n3 == this.cursorShape) {
            return;
        }
        this.cursorShape = n3;
        try {
            long l2 = this.field_22787.method_22683().method_4490();
            GLFW.glfwSetCursor((long)l2, (long)GpuClickGuiScreen.standardCursor(n3));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static long standardCursor(int n2) {
        long l2;
        if (n2 == 0) {
            return 0L;
        }
        Long l3 = STANDARD_CURSORS.get(n2);
        if (l3 != null) {
            return l3;
        }
        try {
            l2 = GLFW.glfwCreateStandardCursor((int)n2);
        }
        catch (Throwable throwable) {
            l2 = 0L;
        }
        STANDARD_CURSORS.put(n2, l2);
        return l2;
    }

    private void drawTopNavigationItem(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8, String string, String string2, boolean bl, Runnable runnable) {
        boolean bl2 = this.hovered(f7, f8, f2, f3, f4, f5);
        float f9 = f5 * 0.5f * Math.min(1.0f, this.cornerScale);
        if (bl) {
            this.topPillShow = true;
            this.topPillTX = f2;
            this.topPillTW = f4;
        } else if (bl2) {
            builder.rect(f2, f3, f4, f5, f9, this.hoverWash(20));
        }
        float f10 = 9.5f * f6;
        string2 = UiKit.fit(string2, Math.max(0.0f, f4 - 4.0f * f6), f10, UiDrawCommand.TextWeight.SEMIBOLD);
        float f11 = GpuUiFontMetrics.textWidth(string2, f10, UiDrawCommand.TextWeight.SEMIBOLD);
        float f12 = f2 + Math.max(4.0f * f6, (f4 - f11) * 0.5f);
        builder.text(f12, GpuUiFontMetrics.topInBox(f3, f5, f10), f10, bl ? this.accentArgb() : (bl2 ? this.textColor : this.mutedColor), string2, UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f2, f3, f4, f5, runnable);
    }

    private void drawToggleSwitch(UiDisplayList.Builder builder, String string, float f2, float f3, float f4, float f5, boolean bl, float f6) {
        float f7 = this.anim(GpuClickGuiScreen.bf("\u0bc4\u16ed\ue092\u0864\ue8a0\ud85b", 1485204916, 0x1B1EF9FC ^ CryptoVault.mask()) + string, bl ? 1.0f : 0.0f, 22.0f);
        f2 = Math.round(f2);
        f3 = Math.round(f3);
        f4 = Math.round(f4);
        f5 = Math.round(f5);
        float f8 = f5 * 0.5f;
        builder.rect(f2, f3, f4, f5, f8, this.toggleOffColor);
        this.accentFill(builder, f2, f3, f4, f5, f8, f7, this.toggleOnColor);
        float f9 = f8 - Math.max(2.0f, 2.5f * f6);
        float f10 = f2 + f8 + (f4 - f5) * f7;
        float f11 = f3 + f8;
        builder.circle(f10, f11 + Math.max(1.0f, 0.75f * f6), f9, 0x40000000);
        builder.circle(f10, f11, f9, -526086);
    }

    private static void textMid(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2, String string, UiDrawCommand.TextWeight textWeight) {
        builder.text(f2, GpuUiFontMetrics.topForCenter(f3, f4), f4, n2, string, textWeight);
    }

    private static void textCentered(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2, String string, UiDrawCommand.TextWeight textWeight) {
        float f5 = GpuUiFontMetrics.textWidth(string, f4, textWeight);
        builder.text(f2 - f5 * 0.5f, GpuUiFontMetrics.topForCenter(f3, f4), f4, n2, string, textWeight);
    }

    private void drawFloatingTooltip(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        float f7 = 9.5f * f6;
        float f8 = 8.5f * f6;
        float f9 = 8.0f * f6;
        float f10 = 11.0f * f6;
        float f11 = Math.min(240.0f * f6, f4 - 40.0f);
        List<String> list = GpuClickGuiScreen.wrapText(this.hoveredTooltipDesc, f11 - f9 * 2.0f, f8, 10);
        if (list.isEmpty()) {
            return;
        }
        boolean bl = this.hoveredTooltipTitle != null && !this.hoveredTooltipTitle.isBlank();
        String string = bl ? UiKit.fit(this.hoveredTooltipTitle, f11 - f9 * 2.0f, f7, UiDrawCommand.TextWeight.SEMIBOLD) : "";
        float f12 = bl ? GpuUiFontMetrics.textWidth(string, f7, UiDrawCommand.TextWeight.SEMIBOLD) : 0.0f;
        for (String string2 : list) {
            f12 = Math.max(f12, GpuUiFontMetrics.textWidth(string2, f8));
        }
        float f13 = Math.min(f11, f12 + f9 * 2.0f);
        float f14 = f9 * 2.0f - 2.0f * f6 + (bl ? 13.0f * f6 : 0.0f) + (float)list.size() * f10;
        float f15 = f2 + 12.0f * f6;
        float f16 = f3 + 14.0f * f6;
        if (f15 + f13 > f4 - 10.0f) {
            f15 = f2 - f13 - 8.0f * f6;
        }
        if (f16 + f14 > f5 - 10.0f) {
            f16 = f3 - f14 - 8.0f * f6;
        }
        f15 = Math.max(6.0f, f15);
        f16 = Math.max(6.0f, f16);
        float f17 = 8.0f * f6 * this.cornerScale;
        if (this.lg) {
            this.glassPane(builder, f15, f16, f13, f14, f17, 1.0f, 180, true, this.tooltipBg);
        } else {
            this.panelShadow(builder, f15, f16, f13, f14, f17, f6, 1.0f);
            builder.glass(f15, f16, f13, f14, f17, 10.0f, this.tooltipBg != 0 ? this.tooltipBg : this.popupTint(224));
            if (this.showRims) {
                builder.border(f15, f16, f13, f14, f17, Math.max(1.0f, f6 * 0.8f), this.outerBorder);
            }
        }
        float f18 = f16 + f9 - 1.0f * f6;
        if (bl) {
            builder.text(f15 + f9, f18, f7, this.titleColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
            f18 += 13.0f * f6;
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            builder.text(f15 + f9, f18 + (float)i2 * f10, f8, this.mutedColor, list.get(i2));
        }
    }

    private void drawRail(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, boolean bl, float f6, float f7, float f8) {
        int n2;
        int n3;
        int n4;
        this.navStagger = 0;
        float f9 = Math.round(38.0f * f6);
        float f10 = f9 + (float)Math.round(4.0f * f6);
        float f11 = bl ? (float)Math.round(Math.min(f4 - 16.0f * f6, 44.0f * f6)) : (float)Math.round(f4 - 24.0f * f6);
        float f12 = Math.round(f2 + (f4 - f11) * 0.5f);
        float f13 = Math.round(f3 + 12.0f * f6);
        float f14 = Math.round(f3 + f5 - 12.0f * f6 - f9);
        float f15 = f13;
        GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
        List<GuiPrefs.CustomCategory> list = guiPrefs.customCategories();
        if (this.folderId != null && guiPrefs.customCategory(this.folderId) == null) {
            this.folderId = null;
        }
        this.railFolderRects.clear();
        boolean bl2 = guiPrefs.canCreateCategory();
        int n5 = NAV_CATEGORIES.length + list.size() + (bl2 ? 1 : 0);
        int n6 = -1;
        if (this.folderId == null) {
            for (n4 = 0; n4 < NAV_CATEGORIES.length; ++n4) {
                if (NAV_CATEGORIES[n4] != this.category) continue;
                n6 = n4;
            }
        } else {
            for (n4 = 0; n4 < list.size(); ++n4) {
                if (!list.get(n4).id().equals(this.folderId)) continue;
                n6 = NAV_CATEGORIES.length + n4;
            }
        }
        n4 = this.view == View.MODULES && n6 >= 0 ? 1 : 0;
        boolean bl3 = this.studioUnlocked();
        boolean bl4 = bl3 && this.view == View.MACROS;
        boolean bl5 = bl3 && this.view == View.SCRIPTS;
        float f16 = f14 - f10;
        float f17 = (bl3 ? f16 - (float)Math.round(12.0f * f6) : f14 + f9) - f13;
        float f18 = Math.round((n5 > NAV_CATEGORIES.length ? 16.0f : 20.0f) * f6);
        float f19 = Math.max(f18, Math.min(f10, (float)Math.floor(f17 / (float)n5)));
        float f20 = Math.min(f9, f19 - Math.max(1.0f, (float)Math.round(2.0f * f6)));
        float f21 = n4 != 0 ? f15 + (float)n6 * f19 : (bl5 ? f16 : f14);
        float f22 = n4 != 0 ? f20 : f9;
        this.railPillY = f15 + this.spring(GpuClickGuiScreen.bf("\ue833\ubfcc\u3e53\ufa99\u936a\ue529\u3916\ua34a\u79fb\ua45e\ua861\ud2ee", -620314565, 0x1B1EF9FC ^ CryptoVault.mask()), f21 - f15, 260.0f, 24.0f);
        this.railPillH = this.spring(GpuClickGuiScreen.bf("\ue9d3\uf432\u7b00\u607b\u0a18\ua948\uf0cc\ua88c\uf5cc\ueed8\u9808\ue0ac\u2819\ua9ed\u4ca8", 307502497, 0x1B1EF9FC ^ CryptoVault.mask()), f22, 260.0f, 24.0f);
        float f23 = this.anim(GpuClickGuiScreen.bf("\ua994\ucd9e\u9a6c\ub628\u1f6e\u5173\u12b8\ua0a6\u74fe\u39a6\ufa6f\u925e\u8b31\u9d84\u03ee", 1280119628, 0x1B1EF9FC ^ CryptoVault.mask()), n4 != 0 || bl4 || bl5 ? 1.0f : 0.0f, 14.0f);
        if (f23 > 0.01f) {
            float f24 = 11.0f * f6 * this.cornerScale;
            float f25 = 0.5f + 0.5f * (float)Math.sin((double)this.nanoTime() / 1.4E9 * (this.reducedMotion() ? 0.3 : 1.0));
            n3 = n4 != 0 && this.folderId == null ? HitboxConfigScreen752809.b(this.category) : -1;
            int n7 = this.sidebarHl != 0 ? this.sidebarHl | 0xFF000000 : this.accentArgb() | 0xFF000000;
            n2 = n3 >= 0 ? GpuClickGuiScreen.mixRgb(n7, n3 | 0xFF000000, 0.7f) : n7;
            this.railPillTint = this.railPillTint == 0 || this.reducedMotion() ? n2 : GpuClickGuiScreen.mixRgb(this.railPillTint, n2, Math.min(1.0f, this.frameDelta * 12.0f));
            int n8 = GpuClickGuiScreen.scaleAlpha((n3 >= 0 || HitboxConfigScreen752809.i() ? this.railPillTint : n7) | 0xFF000000, f23 * this.sidebarGlowK);
            int n9 = GpuClickGuiScreen.scaleAlpha((this.sidebarHl != 0 ? n7 : this.accent2) | 0xFF000000, f23 * this.sidebarGlowK);
            builder.shadow(f12, this.railPillY + 3.0f * f6, f11, this.railPillH, f24, 16.0f * f6, 0.0f, GpuClickGuiScreen.scaleAlpha(n8, 0.22f + 0.08f * f25));
            builder.gradient(f12, this.railPillY, f11, this.railPillH, f24, GpuClickGuiScreen.scaleAlpha(n8, 0.26f + 0.08f * f25), GpuClickGuiScreen.scaleAlpha(n9, 0.08f), GpuClickGuiScreen.scaleAlpha(n8, 0.12f), GpuClickGuiScreen.scaleAlpha(n9, 0.04f + 0.04f * f25));
            builder.border(f12, this.railPillY, f11, this.railPillH, f24, Math.max(1.0f, f6), GpuClickGuiScreen.scaleAlpha(n8, 0.34f));
            builder.rect(f12 + f24, this.railPillY + 0.5f * f6, Math.max(0.0f, f11 - f24 * 2.0f), Math.max(1.0f, f6), 0.5f, GpuClickGuiScreen.scaleAlpha(-1, 0.16f * f23 * this.sidebarGlowK));
        }
        for (Category category : NAV_CATEGORIES) {
            n2 = this.view == View.MODULES && this.folderId == null && this.category == category ? 1 : 0;
            this.drawNavItem(builder, f12, f15, f11, f20, category.zw, GcsCategoryIcons.forCategory(category.name()), n2 != 0, bl, f6, f7, f8, () -> this.openCategory(category, null), HitboxConfigScreen752809.b(category));
            f15 += f19;
        }
        for (GuiPrefs.CustomCategory customCategory : list) {
            n3 = this.view == View.MODULES && customCategory.id().equals(this.folderId) ? 1 : 0;
            String string = customCategory.id();
            String string2 = UiKit.fit(customCategory.name(), Math.max(0.0f, f11 - 58.0f * f6), 10.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            this.drawNavItem(builder, f12, f15, f11, f20, string2, GpuClickGuiScreen.bf("\ub5ed\ucc35\ufc84\uc6f3", 1021977717, 0x1B1EF9FC ^ CryptoVault.mask()), n3 != 0, bl, f6, f7, f8, () -> this.openCategory(this.category, string));
            if (this.hovered(f7, f8, f12, f15, f11, f20)) {
                this.hoveredTooltipTitle = customCategory.name();
                this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\u4012\ud4e9\u3193\uc1d9\u6a84\u0e13\u3eed\uab03\u9e46\u9845\u712c\ua20c\ueca6\u4b7e\ub7fa", -1552913547, 0x1B1EF9FC ^ CryptoVault.mask()) + customCategory.modules().size() + (customCategory.modules().size() == 1 ? GpuClickGuiScreen.bf("\uf98b\ub497\uf674\ue70f\u4e06\uafac\u131a\uf122\u7b5f\u5831\u8bf9", 126617844, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\ua574\u1f27\u97ee\u6f3e\ub211\ua7f9\ue201\u6e0c\u92fb\ub8aa", 362314966, 0x1B1EF9FC ^ CryptoVault.mask())) + GpuClickGuiScreen.bf("\u1a29\ufe42\ua57f\u96bf\u496a\u9eae\u29ed\uc5a7\ud376\u98d1\uf99d\ue0ea\uabf7\u7cdf\u736f\u32bd\ucdb2\u1f1d\u739e\ud291\ub1b2\ua249\u239e\u5fe7\ue58c\u690e\u1b3b\u7948\ufebb\ucd62\ub9a2\u54a7\ub960\uaeb6\u309b\ud9cd\u140c\uf7d8\u84be\uc385\u23dd\ua08f\uf69c\u0438\uf112\u9649\uff90\u5091\u46a8\u8ec9\u2aee\u6cf2\u7d06\u3658\u2a70\u5293\ubf6f\ub286\u2897\ud714\uc136\ue8a6\ubeca\uf66e\ub1e9\u55c0\u8126\ued58\u86d1\ua955\ue7f2\uc3ba\ud8ea\ue95d\u6982\u34af\u2c16\uc85c\u7178\u1b59\u06a8\u042b\u3d38\u5dd9\u0fcf\u8672\u8737\u9137\u5c07", 2136622173, 0x1B1EF9FC ^ CryptoVault.mask());
            }
            this.hitModuleCardRight(f12, f15, f11, f20, () -> this.openFolderMenu(string));
            this.railFolderRects.add(new FolderTarget(string, f12, f15, f11, f20));
            if (this.dragModule != null && this.dragPointerX >= f12 && this.dragPointerX <= f12 + f11 && this.dragPointerY >= f15 && this.dragPointerY <= f15 + f20) {
                builder.border(f12, f15, f11, f20, 10.0f * f6 * this.cornerScale, Math.max(1.5f, 1.6f * f6), this.accentArgb());
            }
            f15 += f19;
        }
        if (bl2) {
            boolean bl6 = this.hovered(f7, f8, f12, f15, f11, f20);
            if (bl6) {
                builder.rect(f12, f15, f11, f20, 10.0f * f6 * this.cornerScale, this.hoverWash(20));
            }
            float f26 = bl ? f12 + f11 * 0.5f : f12 + 14.0f * f6 + (float)Math.round(17.0f * f6) * 0.5f;
            GpuClickGuiScreen.textCentered(builder, f26, f15 + f20 * 0.5f, 13.0f * f6, bl6 ? this.textColor : this.muted2Color, "+", UiDrawCommand.TextWeight.SEMIBOLD);
            if (!bl) {
                builder.text(f12 + 38.0f * f6, GpuUiFontMetrics.topInBox(f15, f20, 10.0f * f6), 10.0f * f6, bl6 ? this.textColor : this.muted2Color, GpuClickGuiScreen.bf("\u063e\u7eec\u2bae\u5519\u00e6\u4072\u3feb\u1739\u6f52\u1e8d\u673c\u3cb6\u5bf3\u9df5", 1896621994, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            }
            if (bl6) {
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\uc591\u78ff\u98db\u81a5\ue542\u6c66\ubeb3\uf514\ub973\u7948\u1448\uc074", -607871245, 0x1B1EF9FC ^ CryptoVault.mask());
                this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\u5cbb\u8904\uae30\u3ccd\uf325\u5406\u55e9\u8af7\uc4a0\u0d22\u5056\uf39d\u2273\ucc4c\ucf39\ue046\u78b7\u665a\ud543\uf7ec\u1ec2\ub3a2\u1ec8\u9d6a\u8b62\u433b\u0eb8\u9aa8\ub55c\ue14c\u28c3\ub50f\u03c9\u9d5e\ub81f\u61c4\ucef8\udb33\u98a1\u846e\uf26c\u97e8\u97fd\u6158\u2c96\u4e7d\u0c15\u02f2\u5dcf\u907a\udbcc\ucea5\u8f33\u5e1e\ub572\u36f1\uddc8\ubc5f\ucf0d\u174d\ue1ca\uad99\u5625\ubd4b\u9819\uf0d0\u14db\u37ee\u0233\ue0d5\ufdba\ud66e\ue138\ue80c\u7e7c\u8119", -1083882457, 0x1B1EF9FC ^ CryptoVault.mask());
            }
            this.hit(f12, f15, f11, f20, this::createFolder);
        }
        if (!bl3) {
            this.tourScriptsRect[2] = 0.0f;
            this.tourMacrosRect[2] = 0.0f;
            return;
        }
        if (this.showSeparators) {
            builder.rect(f12, f16 - (float)Math.round(8.0f * f6), f11, Math.max(1.0f, (float)Math.round(f6)), 0.0f, this.lineColor);
        }
        this.tourScriptsRect[0] = f12;
        this.tourScriptsRect[1] = f16;
        this.tourScriptsRect[2] = f11;
        this.tourScriptsRect[3] = f9;
        this.tourMacrosRect[0] = f12;
        this.tourMacrosRect[1] = f14;
        this.tourMacrosRect[2] = f11;
        this.tourMacrosRect[3] = f9;
        this.drawNavItem(builder, f12, f16, f11, f9, GpuClickGuiScreen.bf("\uad81\ue1da\uc4d6\u27d7\u95e3\u5ce3\u4322\ube4c", 871582576, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u28be\udc73\u42de\u4e7a\uae36\ua414\u6751\u53db\ua4ce\u4a79", 1064008224, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.SCRIPTS, bl, f6, f7, f8, () -> {
            this.view = View.SCRIPTS;
            this.scriptView.refresh();
        });
        this.drawNavItem(builder, f12, f14, f11, f9, GpuClickGuiScreen.bf("\ud09a\ua2f9\u53da\u3e32\uc3b6\u27e7\ueb3f", 1631540297, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uc5f6\ud55f\u8952\u8509\u92cf\u307b\ud53d", 854965062, 0x1B1EF9FC ^ CryptoVault.mask()), this.view == View.MACROS, bl, f6, f7, f8, () -> {
            this.view = View.MACROS;
            this.macroView.refresh();
        });
    }

    /*
     * Exception decompiling
     */
    private static String navDescription(String var0) {
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

    private void drawNavItem(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, String string, String string2, boolean bl, boolean bl2, float f6, float f7, float f8, Runnable runnable) {
        this.drawNavItem(builder, f2, f3, f4, f5, string, string2, bl, bl2, f6, f7, f8, runnable, -1);
    }

    private void drawNavItem(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, String string, String string2, boolean bl, boolean bl2, float f6, float f7, float f8, Runnable runnable, int n2) {
        float f9;
        float f10;
        float f11;
        boolean bl3;
        boolean bl4 = bl3 = n2 >= 0;
        int n3 = bl3 ? n2 | 0xFF000000 : (this.sidebarHl != 0 ? this.sidebarHl | 0xFF000000 : this.accentArgb());
        boolean bl5 = this.hovered(f7, f8, f2, f3, f4, f5);
        float f12 = this.anim(GpuClickGuiScreen.bf("\u0b93\u97a6\uae91\ue5a6\ubd99\u3633\uc32b\u6ef6", -417491148, 0x1B1EF9FC ^ CryptoVault.mask()) + string, bl5 ? 1.0f : 0.0f, 18.0f);
        float f13 = 1.0f;
        if (!this.reducedMotion()) {
            f11 = (float)(this.nanoTime() - this.openedAt) / 1.0E9f - 0.06f - (float)this.navStagger * 0.045f;
            f13 = Math.max(0.0f, Math.min(1.0f, f11 / 0.34f));
            f13 = 1.0f - (1.0f - f13) * (1.0f - f13) * (1.0f - f13);
        }
        ++this.navStagger;
        f11 = this.anim(GpuClickGuiScreen.bf("\u46d4\u7b58\ubf26\ucc8f\u3c63\ud543\u81c4", -1209791655, 0x1B1EF9FC ^ CryptoVault.mask()) + string, bl ? 1.0f : 0.0f, 16.0f);
        if (bl2 && bl5) {
            this.hoveredTooltipTitle = string;
            this.hoveredTooltipDesc = GpuClickGuiScreen.navDescription(string);
        }
        float f14 = Math.round(17.0f * f6);
        float f15 = Math.round(f3 + (f5 - f14) * 0.5f);
        float f16 = Math.round(f2 + (bl2 ? (f4 - f14) * 0.5f : 14.0f * f6));
        float f17 = GpuUiFontMetrics.topInBox(f3, f5, 10.0f * f6);
        float f18 = f12 * 1.6f * f6 * (this.reducedMotion() ? 0.4f : 1.0f);
        float f19 = f14 + (float)Math.round(10.0f * f6);
        float f20 = (f16 += f18 - (1.0f - f13) * 12.0f * f6) - (f19 - f14) * 0.5f;
        float f21 = f15 - (f19 - f14) * 0.5f;
        if (!bl2 || f11 > 0.01f || f12 > 0.01f) {
            f10 = Math.max(f11, f12 * 0.5f);
            builder.rect(f20, f21, f19, f19, f19 * 0.32f * this.cornerScale, GpuClickGuiScreen.scaleAlpha(GpuClickGuiScreen.mixRgb(-1, n3, f11) | 0xFF000000, 0.07f * f12 * (1.0f - f11) + 0.2f * f11 * this.sidebarGlowK));
            if (f11 > 0.01f) {
                builder.border(f20, f21, f19, f19, f19 * 0.32f * this.cornerScale, Math.max(1.0f, f6), GpuClickGuiScreen.scaleAlpha(n3 | 0xFF000000, 0.45f * f11 * this.sidebarGlowK));
            }
        }
        if (f11 > 0.01f) {
            f10 = Math.max(2.0f, 2.6f * f6);
            f9 = f5 * (0.35f + 0.35f * f11);
            builder.shadow(f2 - f10, f3 + (f5 - f9) * 0.5f, f10 * 2.0f, f9, f10, 8.0f * f6, 0.0f, GpuClickGuiScreen.argb(144.0f * f11 * this.sidebarGlowK, n3));
            builder.rect(f2 - f10 * 0.5f, f3 + (f5 - f9) * 0.5f, f10, f9, f10 * 0.5f, GpuClickGuiScreen.argb(Math.round(255.0f * f11), n3));
        }
        if (bl3) {
            f10 = Math.max(f11, 0.55f + 0.45f * f12);
            f9 = Math.max(1.5f, 2.2f * f6);
            float f22 = f2 + Math.max(5.0f * f6, 4.0f);
            float f23 = f3 + f5 * 0.5f;
            if (f11 > 0.01f || f12 > 0.01f) {
                builder.shadow(f22 - f9, f23 - f9, f9 * 2.0f, f9 * 2.0f, f9, 9.0f * f6, 0.0f, GpuClickGuiScreen.argb(128.0f * Math.max(f11, f12 * 0.6f), n3));
            }
            builder.circle(f22, f23, f9, GpuClickGuiScreen.argb(255.0f * f10 * (1.0f - 0.35f * (1.0f - f11)), n3));
            if (f11 > 0.01f) {
                builder.gradient(f2, f3, f4, f5, 11.0f * f6 * this.cornerScale, GpuClickGuiScreen.argb(28.0f * f11, n3), GpuClickGuiScreen.argb(4.0f * f11, n3), GpuClickGuiScreen.argb(12.0f * f11, n3), GpuClickGuiScreen.argb(2.0f * f11, n3));
            }
        }
        if (f12 > 0.01f && f11 < 0.99f) {
            builder.rect(f2, f3, f4, f5, 11.0f * f6 * this.cornerScale, this.hoverWash(Math.round(18.0f * f12 * (1.0f - f11))));
        }
        int n4 = bl ? GpuClickGuiScreen.mixRgb(n3, -1, 0.25f) : (bl3 ? GpuClickGuiScreen.mixRgb(GpuClickGuiScreen.mixRgb(this.mutedColor, this.textColor, f12), n3, 0.3f + 0.2f * f12) : GpuClickGuiScreen.mixRgb(this.mutedColor, this.textColor, f12));
        GcsCategoryIcons.draw(builder, string2, f16, f15, f14, GpuClickGuiScreen.scaleAlpha(n4, f13));
        if (!bl2) {
            int n5 = GpuClickGuiScreen.mixRgb(GpuClickGuiScreen.mixRgb(this.mutedColor, this.textColor, f12), this.titleColor, f11);
            builder.text(f2 + 38.0f * f6 + f18 - (1.0f - f13) * 12.0f * f6, f17, 10.0f * f6, GpuClickGuiScreen.scaleAlpha(n5, f13), string, UiDrawCommand.TextWeight.SEMIBOLD);
        }
        this.hit(f2, f3, f4, f5, runnable);
    }

    private void openCategory(Category category, String string) {
        if (!Objects.equals(this.folderId, string)) {
            this.commitFolderRename();
            this.deleteFolderArmed = false;
        }
        this.view = View.MODULES;
        this.category = category;
        this.folderId = string;
        this.animateModuleInspectorTo(0.0f);
        this.searchScoped = !this.searchField.isEmpty() || moduleFilter != ModuleFilter.ALL;
        this.navIndex = -1;
        this.catalogScroll.reset();
        this.restoreCatalogScroll = -1.0f;
        this.moduleInspectorScrollRemainder = 0.0;
        this.settingsScroll = 0;
    }

    private String createFolder() {
        String string = GpuClickGuiScreen.guiPrefs().createCategory(null);
        if (string == null) {
            return null;
        }
        this.openCategory(this.category, string);
        this.startFolderRename(string);
        return string;
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
        GpuClickGuiScreen.guiPrefs().deleteCategory(string);
        this.deleteFolderArmed = false;
        if (string.equals(this.folderId)) {
            this.openCategory(this.category, null);
        }
        this.catalogCacheKey = "";
    }

    private void startFolderRename(String string) {
        GuiPrefs.CustomCategory customCategory = GpuClickGuiScreen.guiPrefs().customCategory(string);
        if (customCategory == null) {
            return;
        }
        this.commitFolderRename();
        this.kitHost.focus(this.folderNameField);
        this.renamingFolderId = string;
        this.folderNameField.setValue(customCategory.name());
        this.folderNameField.selectAll();
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
        GpuClickGuiScreen.guiPrefs().renameCategory(string, this.folderNameField.value());
    }

    private void openContextMenu(String string, List<MenuItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.contextTitle = string;
        this.contextItems = list;
        this.contextX = (float)this.pressX;
        this.contextY = (float)this.pressY;
    }

    private void closeContextMenu() {
        this.contextItems = null;
        this.contextTitle = null;
    }

    private void openModuleMenu(Module module) {
        GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        boolean bl = this.selected == module && this.moduleInspectorTargetProgress > 0.0f;
        arrayList.add(new MenuItem(bl ? GpuClickGuiScreen.bf("\u0d9e\u4b1a\ucbfe\ufd25\u203a\ub6b6\u9b6c\u3c7f\u7fa3\uc1c8\u77b1\u5187\u4204\u57b0\ue6b1", -1914828268, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u87a9\u7362\u59ab\u2e35\u013f\ufe7d\u3fac\u536a\u1fec\u658a\u4a7e", -430955079, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> this.toggleModuleInspector(module)));
        arrayList.add(new MenuItem(module.isFavorite() ? GpuClickGuiScreen.bf("\u5681\u932e\ud89a\u2b26\u0296\u8ead\u84d8\uedbc\u9cd5\u8a92\u1db1\uf5eb", -1913074874, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\ua16c\u4d5f\u7fd5\u75c4\ue425\ue342\ub1dd\ub6b1\u7826", 1669327524, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> {
            module.setFavorite(!module.isFavorite());
            this.catalogCacheKey = "";
        }));
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u71e1\udd68\u7e26\u1c1f\u7bba\ua747\uef64\u5114\ub105\u6f60\u2fcd\u41fc\udfe3\ua4f6\ued32\u56e4", 2063450133, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> this.kitHost.setClipboard(GpuClickGuiScreen.moduleDebugInfo(module))));
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\ueb12\ucf2f\u91c1\u122f\uacde\u4c93\ueedf\u1ba9\u2807\u0afe\ubdc3\uafc9\ua022\u1b01\u8af1\u56fb\u09d5\u5d2f\u571d\ubaff\uaf62", 1185275711, 0x1B1EF9FC ^ CryptoVault.mask()), false, GpuClickGuiScreen.moduleModified(module), true, () -> GpuClickGuiScreen.resetModuleSettings(module)));
        List<GuiPrefs.CustomCategory> list = guiPrefs.customCategories();
        arrayList.add(MenuItem.divider());
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u7782\u31ea\u738e\u609d\u3b70\uc911\u47a8\u38b9", 1047640487, 0x1B1EF9FC ^ CryptoVault.mask()), false, false, false, null));
        for (GuiPrefs.CustomCategory customCategory : list) {
            String string = customCategory.id();
            arrayList.add(new MenuItem(customCategory.name(), guiPrefs.isMember(string, module.getName()), true, false, () -> {
                guiPrefs.toggleMember(string, module.getName());
                this.catalogCacheKey = "";
            }));
        }
        if (guiPrefs.canCreateCategory()) {
            arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u7f0f\ud98f\u338e\u4919\uf9f0\u2de1\u5258\uaab9\u2634\u554f\u9bad\ua1c4\u1594\u91d4\ud106\ud1e1\uae78\ub9e4\ucef5\ud438\u74e7\u7e40\uc51d\ub6b0\ue270\u8fdd\u7db7\udfab\ue5e2", -1009515644, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> {
                String string = guiPrefs.createCategory(null);
                if (string == null) {
                    return;
                }
                guiPrefs.toggleMember(string, module.getName());
                this.openCategory(this.category, string);
                this.startFolderRename(string);
            }));
        } else if (list.isEmpty()) {
            arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u8101\u37fe\u4313\uce01\u8af0\uc08f\ud23a\u69c5\uf02c\u4a78\uade0\u88bc", 2026356058, 0x1B1EF9FC ^ CryptoVault.mask()), false, false, false, () -> {}));
        }
        this.openContextMenu(module.getName(), arrayList);
    }

    protected static int cloudSlotsUsed(Map<String, ? extends Object> map) {
        if (map == null) {
            return 0;
        }
        int n2 = map.size();
        if (map.containsKey(GpuClickGuiScreen.bf("\u9d1a\u2327\uea10\ueb2f\ub519\ud92a\u2fc1\u33a2\uba60\u0463\u0a9d\ue254\ucc5b\ua0b9", -38147998, 0x1B1EF9FC ^ CryptoVault.mask()).toLowerCase(Locale.ROOT))) {
            --n2;
        }
        return Math.max(0, n2);
    }

    protected static boolean moduleModified(Module module) {
        for (Setting setting : module.getSettings()) {
            if (setting == null || setting instanceof HitboxKeyBinding655023 || !setting.isModified()) continue;
            return true;
        }
        return false;
    }

    protected static void resetModuleSettings(Module module) {
        for (Setting setting : module.getSettings()) {
            if (setting == null || setting instanceof HitboxKeyBinding655023) continue;
            setting.resetToDefault();
        }
    }

    protected static String moduleDebugInfo(Module module) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GpuClickGuiScreen.bf("\ub491\u12dc\uea7f\ud47a\u2e6c\u996e\uec36\u05e4\u601f\u3902\u8d39\u7ab2\u7ea9\u8afa\u8035\uc4e4", 1703234178, 0x1B1EF9FC ^ CryptoVault.mask())).append(module.getName()).append('\n');
        stringBuilder.append(GpuClickGuiScreen.bf("\u4ee4\u9142\uf1af\u2e85\uc5f7\u7384\udfe7\ubd67\u32e8\u346b\u9a97\uc4ab\ueeb9", 1063097827, 0x1B1EF9FC ^ CryptoVault.mask())).append(module.getCategory() == null ? "?" : module.getCategory().zw).append('\n');
        stringBuilder.append(GpuClickGuiScreen.bf("\u6e96\u3ccb\u3c93\u95e0\ua9ec\u5ef4\u863b\ue2f8\ud3cb\ub4a3", -1197125786, 0x1B1EF9FC ^ CryptoVault.mask())).append(module.isEnabled()).append('\n');
        stringBuilder.append(GpuClickGuiScreen.bf("\ud286\u5d3f\ue615\u9ff3\u5023\u7e9a", 1602932701, 0x1B1EF9FC ^ CryptoVault.mask())).append(module.getKeyDisplayName()).append('\n');
        for (Setting setting : module.getSettings()) {
            if (setting == null || setting instanceof HitboxKeyBinding655023 || setting instanceof ConfigKeybindListSetting) continue;
            stringBuilder.append("  ").append(setting.getName()).append(GpuClickGuiScreen.bf("\u8de7\uae07\ub521\u96d1\u88a1", 54679492, 0x1B1EF9FC ^ CryptoVault.mask())).append(GpuClickGuiScreen.settingValueText(setting));
            if (setting.isModified()) {
                stringBuilder.append(" *");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private static String settingValueText(Setting setting) {
        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting)setting;
            return booleanSetting.isEnabled() ? "on" : GpuClickGuiScreen.bf("\u4ddc\u0584\u0cae\ub605\u704d\u2461\u0800", 1078818548, 0x1B1EF9FC ^ CryptoVault.mask());
        }
        if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            return sliderSetting.W();
        }
        if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            return modeSetting.getCurrentMode();
        }
        if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            return String.join((CharSequence)", ", multiSelectSetting.F());
        }
        if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            return colorSetting.A() == null ? "-" : String.format(Locale.ROOT, GpuClickGuiScreen.bf("\ua956\u6008\u1f69\u699c\uf236\u6834", 348659701, 0x1B1EF9FC ^ CryptoVault.mask()), colorSetting.A().getRGB());
        }
        if (setting instanceof StringSetting) {
            StringSetting stringSetting = (StringSetting)setting;
            return String.valueOf(stringSetting.X());
        }
        return "";
    }

    private void openFolderMenu(String string) {
        GuiPrefs.CustomCategory customCategory = GpuClickGuiScreen.guiPrefs().customCategory(string);
        if (customCategory == null) {
            return;
        }
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\ub9d1\u6f19\uadf7\u171a\u9b4c", -1609084777, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> this.openCategory(this.category, string)));
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\ubbb1\u980e\u1014\ub6ce\u4f78\u0780\ucbea\u3f0a\u1dde\u3391", -990489343, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, false, () -> {
            this.openCategory(this.category, string);
            this.startFolderRename(string);
        }));
        arrayList.add(MenuItem.divider());
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u196d\ub4d7\u0931\uabe3\u6890\u464d\u0965\u4442\u0e61\u8a1e\ud804\uabae\uf152\u31e0\uc1a6", -1361535856, 0x1B1EF9FC ^ CryptoVault.mask()), false, true, true, () -> this.deleteFolder(string)));
        this.openContextMenu(customCategory.name(), arrayList);
    }

    private void openSettingMenu(Setting setting) {
        Module module;
        if (setting == null) {
            return;
        }
        ArrayList<MenuItem> arrayList = new ArrayList<MenuItem>();
        arrayList.add(new MenuItem(GpuClickGuiScreen.bf("\u0701\uf186\u5ed1\u4dd8\u8fa5\u82bf\uc01d\u039a\u3a9f\u2891\u0b08\u1f5e\u341b\ub841\uf077\ud7db\u2445\ub66f", 1438153791, 0x1B1EF9FC ^ CryptoVault.mask()), false, setting.isModified(), false, setting::resetToDefault));
        Module module2 = module = GpuClickGuiScreen.pinnable(setting) ? this.ownerOf(setting) : null;
        if (module != null) {
            GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
            boolean bl = guiPrefs.isPinned(module.getName(), setting.getName());
            String string = bl ? GpuClickGuiScreen.bf("\u7697\u4aa1\udb68\u7570\u27c7\uc627\ufc7e\u5e2d\u1ccf\u836f\uce2e\uc356\udcbb\u3067\u49ba\u044c\uab40\u2653\u6a1d\u9e5f\u8dff\ubc2c\ufce6\udb45\ud609\u28dc\ue5a4\u055a", 900927625, 0x1B1EF9FC ^ CryptoVault.mask()) : (guiPrefs.canPin() ? GpuClickGuiScreen.bf("\u2657\ua861\ub67b\uf8b2\uc078\uc0e0\u8a69\u92b7\u191d\u9154\u2723\ufc20\uca52\u14e4\ud617\uc318\uf253\ue8cf\u62b8\u90cd\ufe60\ue54c\u81f7\ucd5a", 165375369, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u90df\ud25d\u7658\u0732\ud25b\ufe46\uc60e\ue6fb\u0d9e\uf0be\u8c92\u5241\uea88\ube75\u00a3\u1c9f\ue83b\u0b3d\uf9a1\u498e\ub76e\uc863\u24ba\u502b", -424427534, 0x1B1EF9FC ^ CryptoVault.mask()));
            arrayList.add(new MenuItem(string, false, bl || guiPrefs.canPin(), false, () -> guiPrefs.togglePin(module.getName(), setting.getName())));
        }
        this.openContextMenu(setting.getName(), arrayList);
    }

    private Module ownerOf(Setting setting) {
        if (this.selected != null && GpuClickGuiScreen.containsSame(this.selected.getSettings(), setting)) {
            return this.selected;
        }
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        if (hitboxConfigScreen752809 != null && GpuClickGuiScreen.containsSame(hitboxConfigScreen752809.getSettings(), setting)) {
            return hitboxConfigScreen752809;
        }
        ModuleManager moduleManager = this.moduleManager();
        if (moduleManager == null) {
            return null;
        }
        for (Module module : moduleManager.getModules()) {
            if (module == null || !GpuClickGuiScreen.containsSame(module.getSettings(), setting)) continue;
            return module;
        }
        return null;
    }

    private static boolean containsSame(List<? extends Setting> list, Setting setting) {
        if (list == null) {
            return false;
        }
        for (Setting setting2 : list) {
            if (setting2 != setting) continue;
            return true;
        }
        return false;
    }

    private void drawContextMenu(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        List<MenuItem> list = this.contextItems;
        if (list == null) {
            return;
        }
        this.hits.clear();
        this.moduleCardRightHits.clear();
        this.hoveredTooltipTitle = null;
        this.hoveredTooltipDesc = null;
        boolean bl = this.clippingHits;
        boolean bl2 = this.inspectorRevealHitClip;
        this.clippingHits = false;
        this.inspectorRevealHitClip = false;
        this.hit(0.0f, 0.0f, f2, f3, this::closeContextMenu);
        this.hitModuleCardRight(0.0f, 0.0f, f2, f3, this::closeContextMenu);
        float f7 = Math.round(26.0f * f4);
        float f8 = Math.round(14.0f * f4);
        float f9 = Math.round(9.0f * f4);
        float f10 = Math.round(5.0f * f4);
        float f11 = 10.5f * f4;
        float f12 = this.contextTitle == null ? 0.0f : (float)Math.round(24.0f * f4);
        float f13 = this.contextTitle == null ? 0.0f : GpuUiFontMetrics.textWidth(this.contextTitle, 9.5f * f4, UiDrawCommand.TextWeight.SEMIBOLD);
        float f14 = f10 * 2.0f + f12;
        for (MenuItem menuItem : list) {
            if (menuItem.label() == null) {
                f14 += f9;
                continue;
            }
            boolean bl3 = menuItem.action() == null;
            f14 += bl3 ? f8 + (float)Math.round(4.0f * f4) : f7;
            f13 = Math.max(f13, GpuUiFontMetrics.textWidth(menuItem.label(), bl3 ? 8.5f * f4 : f11, UiDrawCommand.TextWeight.SEMIBOLD));
        }
        float f15 = Math.round(Math.min(Math.max(170.0f * f4, f13 + 52.0f * f4), Math.max(170.0f * f4, f2 * 0.5f)));
        float f16 = Math.round(this.contextX + 2.0f * f4);
        float f17 = Math.round(this.contextY - f10 - f12 - f7 * 0.5f);
        f16 = Math.max(6.0f, Math.min(f16, f2 - f15 - 6.0f));
        f17 = Math.max(6.0f, Math.min(f17, f3 - f14 - 6.0f));
        float f18 = 10.0f * f4 * this.cornerScale;
        if (this.lg) {
            this.glassPane(builder, f16, f17, f15, f14, f18, 1.0f, 192, true, 0);
        } else {
            this.panelShadow(builder, f16, f17, f15, f14, f18, f4, 1.0f);
            builder.glass(f16, f17, f15, f14, f18, 10.0f, this.popupTint(240));
            if (this.showRims) {
                builder.border(f16, f17, f15, f14, f18, Math.max(1.0f, f4 * 0.8f), this.outerBorder);
            }
        }
        this.hit(f16, f17, f15, f14, () -> {});
        float f19 = f17 + f10;
        if (this.contextTitle != null) {
            GpuClickGuiScreen.textMid(builder, f16 + 12.0f * f4, f19 + f12 * 0.5f, 9.5f * f4, this.mutedColor, UiKit.fit(this.contextTitle, f15 - 24.0f * f4, 9.5f * f4, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            f19 += f12;
        }
        float f20 = f16 + f10;
        float f21 = f15 - f10 * 2.0f;
        for (MenuItem menuItem : list) {
            int n2;
            boolean bl4;
            if (menuItem.label() == null) {
                builder.rect(f20 + 6.0f * f4, f19 + f9 * 0.5f, f21 - 12.0f * f4, Math.max(1.0f, f4), 0.0f, this.lineColor);
                f19 += f9;
                continue;
            }
            if (menuItem.action() == null) {
                GpuClickGuiScreen.textMid(builder, f20 + 8.0f * f4, (f19 += (float)Math.round(4.0f * f4)) + f8 * 0.5f, 8.5f * f4, this.muted2Color, menuItem.label().toUpperCase(Locale.ROOT), UiDrawCommand.TextWeight.SEMIBOLD);
                f19 += f8;
                continue;
            }
            boolean bl5 = bl4 = menuItem.enabled() && this.hovered(f5, f6, f20, f19, f21, f7);
            if (bl4) {
                builder.rect(f20, f19, f21, f7, Math.max(0.0f, f18 - f10), this.hoverWash(32));
            }
            int n3 = !menuItem.enabled() ? this.muted2Color : (menuItem.danger() ? -30070 : (n2 = bl4 ? this.titleColor : this.textColor));
            if (menuItem.checked()) {
                float f22 = Math.round(12.0f * f4);
                GcsCategoryIcons.draw(builder, GpuClickGuiScreen.bf("\u41e8\uac5f\u567a\u6dff\ud759\uca01\uac60\ud85a\u1cf0", 1255305819, 0x1B1EF9FC ^ CryptoVault.mask()), Math.round(f20 + 8.0f * f4), Math.round(f19 + (f7 - f22) * 0.5f), f22, this.accentArgb());
            }
            GpuClickGuiScreen.textMid(builder, f20 + 28.0f * f4, f19 + f7 * 0.5f, f11, n2, UiKit.fit(menuItem.label(), f21 - 36.0f * f4, f11, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            if (menuItem.enabled()) {
                Runnable runnable = menuItem.action();
                this.hit(f20, f19, f21, f7, () -> {
                    this.closeContextMenu();
                    runnable.run();
                });
            }
            f19 += f7;
        }
        this.clippingHits = bl;
        this.inspectorRevealHitClip = bl2;
    }

    private void cancelCardDrag() {
        this.pendingCardPress = null;
        this.dragModule = null;
        this.dragVisible = List.of();
        this.dragDropIndex = -1;
    }

    private void commitCardDrag() {
        Module module = this.dragModule;
        List<Module> list2 = this.dragVisible;
        int n2 = this.dragDropIndex;
        float f2 = this.dragPointerX;
        float f3 = this.dragPointerY;
        this.cancelCardDrag();
        if (module == null) {
            return;
        }
        GuiPrefs guiPrefs = GpuClickGuiScreen.guiPrefs();
        for (FolderTarget list3 : this.railFolderRects) {
            if (!(f2 >= list3.x()) || !(f2 <= list3.x() + list3.w()) || !(f3 >= list3.y()) || !(f3 <= list3.y() + list3.h())) continue;
            if (!guiPrefs.isMember(list3.id(), module.getName())) {
                guiPrefs.toggleMember(list3.id(), module.getName());
            }
            this.catalogCacheKey = "";
            return;
        }
        if (n2 < 0 || this.view != View.MODULES || !this.catalogReorderable) {
            return;
        }
        ArrayList arrayList = new ArrayList(list2.size());
        for (Module module2 : list2) {
            arrayList.add(module2.getName());
        }
        List<String> list = GuiPrefs.move(arrayList, module.getName(), n2);
        if (list.equals(arrayList)) {
            return;
        }
        if (this.folderId != null) {
            GuiPrefs.CustomCategory customCategory = guiPrefs.customCategory(this.folderId);
            if (customCategory != null) {
                guiPrefs.setMembers(this.folderId, GuiPrefs.mergeVisible(customCategory.modules(), list));
            }
        } else {
            guiPrefs.setOrder(this.category.name(), GuiPrefs.mergeVisible(guiPrefs.order(this.category.name()), list));
        }
        this.catalogCacheKey = "";
    }

    private void drawDragGhost(UiDisplayList.Builder builder, float f2) {
        Module module = this.dragModule;
        if (module == null) {
            return;
        }
        float f3 = 11.0f * f2;
        float f4 = GpuUiFontMetrics.textWidth(module.getName(), f3, UiDrawCommand.TextWeight.SEMIBOLD);
        float f5 = Math.round(28.0f * f2);
        float f6 = Math.round(f4 + 28.0f * f2);
        float f7 = Math.round(this.dragPointerX + 10.0f * f2);
        float f8 = Math.round(this.dragPointerY - f5 * 0.5f);
        float f9 = f5 * 0.5f * Math.min(1.0f, this.cornerScale);
        this.panelShadow(builder, f7, f8, f6, f5, f9, f2, 1.0f);
        builder.glass(f7, f8, f6, f5, f9, 10.0f, this.popupTint(224));
        builder.rect(f7, f8, f6, f5, f9, GpuClickGuiScreen.withA(this.accentArgb(), 48));
        builder.border(f7, f8, f6, f5, f9, Math.max(1.0f, f2), GpuClickGuiScreen.withA(this.accentArgb(), 160));
        GpuClickGuiScreen.textCentered(builder, f7 + f6 * 0.5f, f8 + f5 * 0.5f, f3, this.titleColor, module.getName(), UiDrawCommand.TextWeight.SEMIBOLD);
    }

    private void drawCatalog(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, boolean bl, float f6, float f7, float f8) {
        if (!this.viewAllowed(this.view)) {
            this.view = View.MODULES;
        }
        if (GpuClickGuiScreen.floatingView(this.view)) {
            this.drawModulesCatalog(builder, f2, f3, f4, f5, bl, f6, -100000.0f, -100000.0f);
            return;
        }
        this.drawPageContent(builder, f2, f3, f4, f5, bl, f6, f7, f8);
    }

    private static boolean floatingView(View view) {
        return view == View.CONFIGS || view == View.FRIENDS || view == View.SELF_DESTRUCT || view == View.COSMETICS || view == View.LAYOUT || view == View.SS_PROOF || view == View.KEYBINDS;
    }

    private void closePage() {
        if (this.view == View.COSMETICS) {
            this.cosmeticsView.mouseReleased();
        }
        this.view = View.MODULES;
    }

    private void drawFloatingPage(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        this.springs.seed(new PageFloatKey(this.view), 0.0f);
        this.hoveredTooltipTitle = null;
        this.hoveredTooltipDesc = null;
        float f7 = this.springs.ease(new PageFloatKey(this.view), 1.0f, 16.0f, this.frameDelta);
        if (this.reducedMotion()) {
            f7 = 1.0f;
        }
        float f8 = 1.0f - (1.0f - f7) * (1.0f - f7) * (1.0f - f7);
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, GpuClickGuiScreen.argb(166.0f * f8, 329483));
        this.hit(0.0f, 0.0f, f2, f3, this::closePage);
        this.hitModuleCardRight(0.0f, 0.0f, f2, f3, this::closePage);
        boolean bl = this.view == View.COSMETICS || this.view == View.CONFIGS;
        float f9 = Math.min(f2 - (float)Math.round(48.0f * f4), (float)Math.round((bl ? 1000.0f : 760.0f) * f4));
        float f10 = Math.min(f3 - (float)Math.round(48.0f * f4), (float)Math.round((bl ? 680.0f : 600.0f) * f4));
        float f11 = (1.0f - f8) * (float)Math.round(14.0f * f4);
        float f12 = Math.round((f2 - f9) * 0.5f);
        float f13 = Math.round((f3 - f10) * 0.5f + f11);
        float f14 = this.panelRadius * f4;
        builder.shadow(f12, f13, f9, f10, f14, Math.round(30.0f * f4), 0.0f, GpuClickGuiScreen.argb(144.0f * f8, 0));
        if (this.lg) {
            this.glassPane(builder, f12, f13, f9, f10, f14, f8, 68, false, 0);
        } else if (this.floatMaterial) {
            builder.clipPush(f12, f13, f9, f10, f14);
            this.paintSurface(builder, this.floatTheme, f12, f13, f9, f10, f14, this.floatMist);
            builder.rect(f12, f13, f9, f10, 0.0f, this.listBg);
            builder.clipPop();
        } else {
            builder.glass(f12, f13, f9, f10, f14, this.floatBlur, this.floatTint);
            this.drawGlassWash(builder, f12, f13, f9, f10, f14);
        }
        builder.border(f12, f13, f9, f10, f14, 1.0f, this.lineColor);
        this.hit(f12, f13, f9, f10, () -> {});
        this.hitModuleCardRight(f12, f13, f9, f10, () -> {});
        builder.clipPush(f12, f13, f9, f10, f14);
        this.drawPageContent(builder, f12, f13, f9, f10, false, f4, f5, f6);
        builder.clipPop();
        if (f8 < 0.999f) {
            builder.rect(f12, f13, f9, f10, f14, GpuClickGuiScreen.argb(160.0f * (1.0f - f8), 724758));
        }
        String string = GpuClickGuiScreen.bf("\u2d16\u17b5\u8000\u509b\ud1bf\u12d7\uba92\u4d7c\ub7b3\u4337\u80d9\ud668\uff12\ufeab\ucddc\u89a1J\ufb74\u2e4d\uc5ca\u1dd2\u9d5c\ub51d\u33fe\u7cc3\u256a\u1769\ua7a2\ua55e\u92cc\u9d4b\u723d\u9d26\u8fb4\uf434\u3e92\u41bf", 2066087201, 0x1B1EF9FC ^ CryptoVault.mask());
        float f15 = 9.5f * f4;
        float f16 = f13 + f10 + (float)Math.round(10.0f * f4);
        if (f16 + f15 < f3 - 4.0f * f4) {
            builder.text(f12 + (f9 - GpuUiFontMetrics.textWidth(string, f15)) * 0.5f, f16, f15, GpuClickGuiScreen.argb(176.0f * f8, this.muted2Color & 0xFFFFFF), string);
        }
    }

    private void closeDrawers() {
        this.settingsDrawerOpen = false;
        this.themesDrawerOpen = false;
    }

    private void drawConfigEditFloat(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        this.springs.seed(CONFIG_EDIT_FLOAT, 0.0f);
        float f7 = this.springs.ease(CONFIG_EDIT_FLOAT, 1.0f, 16.0f, this.frameDelta);
        if (this.reducedMotion()) {
            f7 = 1.0f;
        }
        float f8 = 1.0f - (1.0f - f7) * (1.0f - f7) * (1.0f - f7);
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, GpuClickGuiScreen.argb(166.0f * f8, 329483));
        this.hit(0.0f, 0.0f, f2, f3, this.configsView::closeEdit);
        this.hitModuleCardRight(0.0f, 0.0f, f2, f3, this.configsView::closeEdit);
        float f9 = Math.min(f2 - (float)Math.round(48.0f * f4), (float)Math.round(460.0f * f4));
        float f10 = Math.min(f3 - (float)Math.round(48.0f * f4), (float)Math.round(380.0f * f4));
        float f11 = (1.0f - f8) * (float)Math.round(14.0f * f4);
        float f12 = Math.round((f2 - f9) * 0.5f);
        float f13 = Math.round((f3 - f10) * 0.5f + f11);
        float f14 = this.panelRadius * f4;
        builder.shadow(f12, f13, f9, f10, f14, Math.round(30.0f * f4), 0.0f, GpuClickGuiScreen.argb(144.0f * f8, 0));
        if (this.lg) {
            this.glassPane(builder, f12, f13, f9, f10, f14, f8, 68, false, 0);
        } else if (this.floatMaterial) {
            builder.clipPush(f12, f13, f9, f10, f14);
            this.paintSurface(builder, this.floatTheme, f12, f13, f9, f10, f14, this.floatMist);
            builder.rect(f12, f13, f9, f10, 0.0f, this.listBg);
            builder.clipPop();
        } else {
            builder.glass(f12, f13, f9, f10, f14, this.floatBlur, this.floatTint);
            this.drawGlassWash(builder, f12, f13, f9, f10, f14);
        }
        builder.border(f12, f13, f9, f10, f14, 1.0f, this.lineColor);
        this.hit(f12, f13, f9, f10, () -> {});
        this.hitModuleCardRight(f12, f13, f9, f10, () -> {});
        builder.clipPush(f12, f13, f9, f10, f14);
        boolean bl = GpuClickGuiScreen.inside(f5, f6, f12, f13, f9, f10);
        this.configsView.drawEditFloating(builder, this.kitHost, f12, f13, f9, f10, f4, bl ? f5 : -1.0f, bl ? f6 : -1.0f);
        builder.clipPop();
        if (f8 < 0.999f) {
            builder.rect(f12, f13, f9, f10, f14, GpuClickGuiScreen.argb(160.0f * (1.0f - f8), 724758));
        }
        String string = GpuClickGuiScreen.bf("\u3ce0\u5b87\u4707\ub831\u04ed\u7468\u3dc5\ua354\uf169\uecc5\u4a58\u1d87\u9386\u4b97\u9491\u4dfe\u9aac\uf6ce\u5386\uda89\uce45\u8a5f\u1d7c\u9c48\u88a0\u3529\u14e0\u6092\ud32c\u8f2b\u696c\u7ea1\uf8cd\u1f96\ua1b5\u0d3c\ue919\uca2c\u9a67", -1060517680, 0x1B1EF9FC ^ CryptoVault.mask());
        float f15 = 9.5f * f4;
        float f16 = f13 + f10 + (float)Math.round(10.0f * f4);
        if (f16 + f15 < f3 - 4.0f * f4) {
            builder.text(f12 + (f9 - GpuUiFontMetrics.textWidth(string, f15)) * 0.5f, f16, f15, GpuClickGuiScreen.argb(176.0f * f8, this.muted2Color & 0xFFFFFF), string);
        }
    }

    private void drawFloatingDrawer(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        this.springs.seed(DRAWER_FLOAT, 0.0f);
        float f7 = this.springs.ease(DRAWER_FLOAT, 1.0f, 16.0f, this.frameDelta);
        if (this.reducedMotion()) {
            f7 = 1.0f;
        }
        float f8 = 1.0f - (1.0f - f7) * (1.0f - f7) * (1.0f - f7);
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, GpuClickGuiScreen.argb(166.0f * f8, 329483));
        this.hit(0.0f, 0.0f, f2, f3, this::closeDrawers);
        this.hitModuleCardRight(0.0f, 0.0f, f2, f3, this::closeDrawers);
        boolean bl = this.themesDrawerOpen && this.themeMakerOpen;
        float f9 = Math.min(f2 - (float)Math.round(48.0f * f4), (float)Math.round((bl ? 960.0f : (this.themesDrawerOpen ? 620.0f : 520.0f)) * f4));
        float f10 = Math.min(f3 - (float)Math.round(48.0f * f4), (float)Math.round((bl ? 660.0f : 640.0f) * f4));
        float f11 = (1.0f - f8) * (float)Math.round(14.0f * f4);
        float f12 = Math.round((f2 - f9) * 0.5f);
        float f13 = Math.round((f3 - f10) * 0.5f + f11);
        float f14 = this.panelRadius * f4;
        builder.shadow(f12, f13, f9, f10, f14, Math.round(30.0f * f4), 0.0f, GpuClickGuiScreen.argb(144.0f * f8, 0));
        if (this.lg) {
            this.glassPane(builder, f12, f13, f9, f10, f14, f8, 68, false, 0);
        } else if (this.floatMaterial) {
            builder.clipPush(f12, f13, f9, f10, f14);
            this.paintSurface(builder, this.floatTheme, f12, f13, f9, f10, f14, this.floatMist);
            builder.rect(f12, f13, f9, f10, 0.0f, this.listBg);
            builder.clipPop();
        } else {
            builder.glass(f12, f13, f9, f10, f14, this.floatBlur, this.floatTint);
            this.drawGlassWash(builder, f12, f13, f9, f10, f14);
        }
        builder.border(f12, f13, f9, f10, f14, 1.0f, this.lineColor);
        this.hit(f12, f13, f9, f10, () -> {});
        this.hitModuleCardRight(f12, f13, f9, f10, () -> {});
        float f15 = this.inspectorRevealX;
        float f16 = this.inspectorRevealY;
        float f17 = this.inspectorRevealW;
        float f18 = this.inspectorRevealH;
        boolean bl2 = this.inspectorRevealHitClip;
        this.inspectorRevealX = f12;
        this.inspectorRevealY = f13;
        this.inspectorRevealW = f9;
        this.inspectorRevealH = f10;
        this.inspectorRevealHitClip = true;
        builder.clipPush(f12, f13, f9, f10, f14);
        boolean bl3 = GpuClickGuiScreen.inside(f5, f6, f12, f13, f9, f10);
        this.drawDrawerContent(builder, f12, f13, f9, f10, f4, bl3 ? f5 : -1.0f, bl3 ? f6 : -1.0f);
        builder.clipPop();
        this.inspectorRevealX = f15;
        this.inspectorRevealY = f16;
        this.inspectorRevealW = f17;
        this.inspectorRevealH = f18;
        this.inspectorRevealHitClip = bl2;
        this.drawerRegionX = f12;
        this.drawerRegionY = f13;
        this.drawerRegionW = f9;
        this.drawerRegionH = f10;
        if (f8 < 0.999f) {
            builder.rect(f12, f13, f9, f10, f14, GpuClickGuiScreen.argb(160.0f * (1.0f - f8), 724758));
        }
        String string = GpuClickGuiScreen.bf("\uc25f\u2faf\u11fa\uf60f\ub4db\uda94\ud308\u308f\u8541\ud871\ud17a\u2990\uc4ed\u9ddd\u26a7\u78ef\u8267\u43ae\u490c\u8216\u7c66\uea44\uc34f\u47b6\u41b1\ue32c\u5680\ua881\u5286\uad13\u33d1\ue09e\u5a86\ua0b9\ufc93\u90bb\u3a6c\u30aa\u2975", 1839766625, 0x1B1EF9FC ^ CryptoVault.mask());
        float f19 = 9.5f * f4;
        float f20 = f13 + f10 + (float)Math.round(10.0f * f4);
        if (f20 + f19 < f3 - 4.0f * f4) {
            builder.text(f12 + (f9 - GpuUiFontMetrics.textWidth(string, f19)) * 0.5f, f20, f19, GpuClickGuiScreen.argb(176.0f * f8, this.muted2Color & 0xFFFFFF), string);
        }
    }

    private void drawPageContent(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, boolean bl, float f6, float f7, float f8) {
        switch (this.view.ordinal()) {
            case 0: {
                this.drawModulesCatalog(builder, f2, f3, f4, f5, bl, f6, f7, f8);
                break;
            }
            case 1: {
                this.configsView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 2: {
                this.macroView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 3: {
                if (this.scriptView.fullscreen()) break;
                this.scriptView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 4: {
                this.peopleView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 5: {
                this.drawPurgePanel(builder, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 7: {
                this.drawLayoutPage(builder, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 6: {
                this.cosmeticsView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 8: {
                this.drawSsProofPage(builder, f2, f3, f4, f5, f6, f7, f8);
                break;
            }
            case 9: {
                this.keybindsView.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8);
            }
        }
    }

    private void drawSsProofPage(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.round(22.0f * f6);
        float f10 = f2 + f9;
        float f11 = f4 - f9 * 2.0f;
        float f12 = f3 + (float)Math.round(20.0f * f6);
        builder.text(f10, f12, 16.0f * f6, this.textColor, GpuClickGuiScreen.bf("\uadeb\uad7b\u3e02\uc870\u104c\uae78\uc265\ufb89\ua1f3\u0a63\ubfe9\u175e\u2887\uf386\ucf73\u64b1\ube6f\u392b\u74dd\udde2", -926172105, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        String string = HitboxConfigScreen752809.l();
        String string2 = string != null ? string : GpuClickGuiScreen.bf("\u0698\u1cbf\u7805\u8dfd\u8d14\ub05e\uf2fc\uae02\u585a\ub0a7\u2c42\u07d2\u84d7\u2b1f\ub5dd\ua9b7\u6c0c\uc548\u0de6\u923f\u21c5\ufd86\ubab5\ua532\u098a\u567b\u76f6\uf8e3\u0edb\ua081\u43ed\u6763\uf804\ue85c\uf7fc\uc604\ue3be\u7ac1\u3ed2\u9173\u34d2\ub787\u249f\ueaf2\u9007\u0c40\u9e24\u1554\ub1a5\uae83\uec0d\u9055\uc2ad\ud4d4\u659b\u416b\ud2b8\u31db\u16b6\u1b96\u3ce4\u6837\u3ac7\uec42\u4f1c\u388d\u98bd\ucc5f\u368c\u0282\uf2c7\u7a69\ub0c7\u1994\u1e63\ucff9\u857e\uff08", 889459702, 0x1B1EF9FC ^ CryptoVault.mask());
        builder.text(f10, f12 + (float)Math.round(22.0f * f6), 10.5f * f6, string != null ? -30070 : this.mutedColor, UiKit.fit(string2, f11, 10.5f * f6));
        float f13 = f12 + (float)Math.round(44.0f * f6);
        float f14 = f3 + f5 - (float)Math.round(16.0f * f6);
        float f15 = Math.max(0.0f, f14 - f13);
        builder.clipPush(f10, f13, f11, f15, this.cardRadius * f6);
        this.clipX = f10;
        this.clipY = f13;
        this.clipW = f11;
        this.clipH = f15;
        this.clippingHits = true;
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        for (Setting setting : new Setting[]{HitboxConfigScreen752809.as, HitboxConfigScreen752809.at, HitboxConfigScreen752809.au, HitboxConfigScreen752809.aw}) {
            ClassicSettingLayout.Row row;
            if (setting == null) continue;
            ClassicSettingLayout.Row row2 = row = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, setting);
            if (row != null && !row.visible()) continue;
            arrayList.add(setting);
        }
        this.drawSettingGroup(builder, GpuClickGuiScreen.bf("\u11f8\uec00\ua4e4\u82f2\u7dbd\u92aa\u55aa\u3e11", -1272002484, 0x1B1EF9FC ^ CryptoVault.mask()), arrayList, f10, f13, f11, f6, f7, f8);
        this.clippingHits = false;
        builder.clipPop();
    }

    private void drawLayoutPage(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.round(22.0f * f6);
        float f10 = f2 + f9;
        float f11 = f4 - f9 * 2.0f;
        float f12 = f3 + (float)Math.round(20.0f * f6);
        builder.text(f10, f12, 16.0f * f6, this.textColor, GpuClickGuiScreen.bf("\u1e75\u7c1c\ub7d3\u0e4b\u6642\ucb04\ud74e", 848129781, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f10, f12 + (float)Math.round(22.0f * f6), 10.5f * f6, this.mutedColor, UiKit.fit(GpuClickGuiScreen.bf("\uc673\uff4d\u8c8a\u3138\ufc72\ub70c\ucfd7\ufa77\u75bc\u76d5\uf002\u310b\u4269\u2146\ue77d\ud322\u8a18\ua31d\u677d\u50a9\u66c8\u8f5f\u4461\u25eb\uc690\u4e25\u64cf\u2517\ucdb5\ua4ed\ua2c1\u3d12\u1e03\u39a7\ubd6b\ubeb9\u81f5\uc6d5\u5897\u7242\uc879\ub9dd\ua01b\u8057\udcc0\ud117\ub168\ud3c2\u3c33\u891a\u0239\u0426\ua9ef\u4426", 1372784934, 0x1B1EF9FC ^ CryptoVault.mask()), f11, 10.5f * f6));
        float f13 = f12 + (float)Math.round(44.0f * f6);
        float f14 = f3 + f5 - (float)Math.round(16.0f * f6);
        float f15 = Math.max(0.0f, f14 - f13);
        builder.clipPush(f10, f13, f11, f15, this.cardRadius * f6);
        this.clipX = f10;
        this.clipY = f13;
        this.clipW = f11;
        this.clipH = f15;
        this.clippingHits = true;
        String string = HitboxConfigScreen752809.U != null ? HitboxConfigScreen752809.U.getCurrentMode() : GpuClickGuiScreen.bf("\u6bf2\u5aa6\u980c\u91ca\udd90\u7370\u8a89", 362613600, 0x1B1EF9FC ^ CryptoVault.mask());
        float f16 = 12.0f * f6;
        float f17 = (f11 - f16) / 2.0f;
        float f18 = Math.round(Math.max(170.0f, Math.min(330.0f, f15 / f6 - 130.0f)) * f6);
        this.drawLayoutCard(builder, f10, f13, f17, f18, GpuClickGuiScreen.bf("\ub31e\u4e31\u9617\u9632\u22e6", -1472022342, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u61e7\u6b9f\ucb1c\uf273\ue38b\u0a23\ue9f1\ua8ba\ue37d\u60b5\u2563\uf0db\uf04b\u1b51\ud7ea\uc3ae\u1be6\udaff\uecf0\u0d7a\u5508\u2edd\ub1fd\u0736\u14b3\u8128\u1d1b\ua4e3\uc4b7\u7c93\u2c54\u2a58\u490c\u6da1\ue411\u92d0\udb55\u98e0\u8bfd\u2aeb\u46bc\u6b7f\u1b39\u860e\uafef\u59d6\ub2be\ub66c\u056e\uce1f\u20a9\u2af9\ud952\uca73\u0f3c\ua2c0\udf00\u5b1e\u3df5\u60cb\ua8fe\u8880\u0f4e\u5b6b\u3f46\u9770\uf626\u3142\ueab5\u51c3\uae25\ucf1a\u0e67\ue502\uc1b8\u9728\u3e0f\u8821\u405f\u1d5b\uca52", -2100752633, 0x1B1EF9FC ^ CryptoVault.mask()), string.equalsIgnoreCase(GpuClickGuiScreen.bf("\uba38\u33d7\uf109\ua164\u8241\u99ab\ue0e6\ue42b", -841066856, 0x1B1EF9FC ^ CryptoVault.mask())), true, f6, f7, f8, this.kitHost);
        this.drawLayoutCard(builder, f10 + f17 + f16, f13, f17, f18, GpuClickGuiScreen.bf("\u7e86\u377b\u26c1\u4965\ua77c\u24b4\ub037\u5925\uc56a\ue190\u0497", -475523792, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ucc60\uc363\u13cf\u259b\u70e7\u0e23\u5e82\u5df1\u2193\u46fb\ub53b\ubb2e\u8304\ua2c7\u414d\ud864\uce36\ua27d\u0a5f\ucfd9\u1528\u016e\u3db3\ubf16\u4eec\u65a7\u7808\u008e\ub912\u2e83\ub756\ud7d7\u3ed9\u555e\u760a\uc563\u6578\ud88d\ue907\u39d2\u6351\ub3f6\u30e0\u92b8\u7f4f\u1b2d\u32a2\u9d94\ue114\u8d33\uab13\u5191\u3071\uccc3\u1fae\u9ea8\u5eda\ub32b\u0887\uac8c\u3cbe\ua536\u8077\u19f0\u76be\u14b9\u6b7e\u9d32\uced0\u284c\uf935\uf4eb\u1858", -868131280, 0x1B1EF9FC ^ CryptoVault.mask()), string.equalsIgnoreCase(GpuClickGuiScreen.bf("\u4212\u1a5b\ue6e4\ufd53\u25e2\ub0e7\ub41e\ucb6f\ud97f\ub480\u57aa", 1765419257, 0x1B1EF9FC ^ CryptoVault.mask())), false, f6, f7, f8, this.kitHost);
        float f19 = f13 + f18 + 16.0f * f6;
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        if (HitboxConfigScreen752809.T != null) {
            arrayList.add(HitboxConfigScreen752809.T);
        }
        if (HitboxConfigScreen752809.S != null && !HitboxConfigScreen752809.a()) {
            arrayList.add(HitboxConfigScreen752809.S);
        }
        this.drawSettingGroup(builder, GpuClickGuiScreen.bf("\ude0d\u52f1\udb59\ua943\u9e09\u9527\u89eb\ua62d\u8d96", 1129803529, 0x1B1EF9FC ^ CryptoVault.mask()), arrayList, f10, f19, f11, f6, f7, f8);
        this.clippingHits = false;
        builder.clipPop();
    }

    private void drawPurgePanel(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.round(22.0f * f6);
        float f10 = f2 + f9;
        float f11 = f4 - f9 * 2.0f;
        float f12 = f3 + (float)Math.round(20.0f * f6);
        builder.text(f10, f12, 16.0f * f6, this.textColor, GpuClickGuiScreen.bf("\u5690\ue080\u7957\u1b0e\u8229\u3e2d\u705c\u56e8\u8167\u4228\u8287\u0273\u4269\uf976\u3caa\ud968\uc760", -970419931, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f10, f12 + (float)Math.round(22.0f * f6), 10.5f * f6, this.mutedColor, UiKit.fit(GpuClickGuiScreen.bf("\ubb50\u94a6\u95c2\ua3fe\ue862\u7896\ued55\u66b6\uc0bc\ub9e5\uc6ec\ufca1\u8211\u94fd\u8dfb\u43b9\u3156\u2ccd\u1cab\u0e60\uec04\uda8f\u981b\ucb21\u6079\uaeac\u3373\u185f\u847f\u62f0\uad6f\u218b\u1654\ue08a\u23a0\ua12c\uf533\u255a\u64fc\ub9fa\u3982\u546b\u9142\uc4d3\u504d\u3149\uc9ba\u6bb2\u1185\u412d\u661d\u8fcc\u170d\u6d2b\ub58a\uf2fc\ubd6b\uc4ed\uebc0\uc125\uefff\u1821\ue2e1\u43e3\u9ba4\u9005\u0609\u47da\u83a8\u6bd5\uf488\ud43f\u5179\u0c89\u87e7\u70fb\ud6d4\uce36\u1ee2", 1413944020, 0x1B1EF9FC ^ CryptoVault.mask()), f11, 10.5f * f6));
        float f13 = f12 + (float)Math.round(44.0f * f6);
        float f14 = f3 + f5 - (float)Math.round(16.0f * f6);
        float f15 = Math.max(0.0f, f14 - f13);
        builder.clipPush(f10, f13, f11, f15, this.cardRadius * f6);
        this.clipX = f10;
        this.clipY = f13;
        this.clipW = f11;
        this.clipH = f15;
        this.clippingHits = true;
        float f16 = f13;
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        for (Setting setting : new Setting[]{HitboxConfigScreen752809.aC, HitboxConfigScreen752809.aD, HitboxConfigScreen752809.aE, HitboxConfigScreen752809.aF, HitboxConfigScreen752809.aG}) {
            ClassicSettingLayout.Row row;
            if (setting == null) continue;
            ClassicSettingLayout.Row row2 = row = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, setting);
            if (row != null && !row.visible() || setting == HitboxConfigScreen752809.aG && HitboxConfigScreen752809.aE != null && HitboxConfigScreen752809.aE.isEnabled()) continue;
            arrayList.add(setting);
        }
        f16 = this.drawSettingGroup(builder, GpuClickGuiScreen.bf("\u143f\u010c\udffa\u944a\ud88c\u8e30\u769d\u164b\u3196\ua0d4", 138694593, 0x1B1EF9FC ^ CryptoVault.mask()), arrayList, f10, f16, f11, f6, f7, f8);
        float f17 = Math.round(46.0f * f6);
        int n2 = this.sdArmed && this.nanoTime() - this.sdArmedAt < 3500000000L ? 1 : 0;
        builder.rect(f10, f16 += 6.0f * f6, f11, f17, this.cardRadius * f6, n2 != 0 ? GpuClickGuiScreen.withA(-43691, 51) : this.cardBg);
        builder.border(f10, f16, f11, f17, this.cardRadius * f6, 1.0f, n2 != 0 ? -38037 : GpuClickGuiScreen.withA(-38037, 48));
        float f18 = f16 + f17 * 0.5f;
        GpuClickGuiScreen.textCentered(builder, f10 + f11 * 0.5f, f18, 12.5f * f6, n2 != 0 ? -38037 : -30070, n2 != 0 ? GpuClickGuiScreen.bf("\u7676\u6b55\uf7aa\uc5eb\u8c8d\u0c42\u8ebc\ub9aa\u2fe0\ua8e5\u07fe\u2000\u7985\u62d3\uaa51\u3ad8\u67c5\u4414\u3ab5\u3c76\u1927\u4dc6\u5c80\u1f87\u7c58\u2b4c\u1f63\u88a5\u9de1\u5ee9", 2029530286, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\udeee\u0f09\u0df0\u5fbb\u2055\u8693\ucb08\u9d62\u26d2\u463a\ua9ce\u8d58\uf461\u521d\u9669\uf0df\uafd6\u6ba3\u751d", 945145325, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f10, f16, f11, f17, () -> {
            if (this.sdArmed && this.nanoTime() - this.sdArmedAt < 3500000000L) {
                this.sdArmed = false;
                this.triggerPurgePanel();
            } else {
                this.sdArmed = true;
                this.sdArmedAt = this.nanoTime();
            }
        });
        this.clippingHits = false;
        builder.clipPop();
    }

    protected static int withA(int n2, int n3) {
        return n3 << 24 | n2 & 0xFFFFFF;
    }

    private void triggerPurgePanel() {
        if (this.field_22787 != null) {
            McCompat.setScreen(this.field_22787, null);
        }
        HitboxRenderStateReset726175.a();
    }

    private float drawBedwarsNotice(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7) {
        boolean bl = HitboxConfigScreen752809.aH != null && HitboxConfigScreen752809.aH.isEnabled();
        float f8 = this.anim(GpuClickGuiScreen.bf("\u1e57\u17bb\u1df5\u012a\u1b91\u1f41\u1126\u2c73\uf75f\uf0ca\u3e1e\u2283\ua584\u5a3f\u564c\u502b\uda2e\u54c3", -972462446, 0x1B1EF9FC ^ CryptoVault.mask()), bl || bedwarsNoticeContinued ? 0.0f : 1.0f, 9.0f);
        if (f8 < 0.01f) {
            return f3;
        }
        float f9 = f8 * f8 * (3.0f - 2.0f * f8);
        float f10 = Math.round(54.0f * f5);
        float f11 = f10 * f9;
        float f12 = f9;
        builder.clipPush(f2, f3, f4, f11, this.cardRadius * f5);
        float f13 = f3 - (1.0f - f9) * 10.0f * f5;
        float f14 = this.cardRadius * f5;
        int n2 = this.accentArgb();
        if (this.lg) {
            this.glassPane(builder, f2, f13, f4, f10, f14, f12, 44, false, 0);
        } else {
            builder.rect(f2, f13, f4, f10, f14, GpuClickGuiScreen.argb(46.0f * f12 * this.panelOpacityK, 0));
        }
        builder.rect(f2, f13, f4, f10, f14, GpuClickGuiScreen.argb(24.0f * f12, n2 & 0xFFFFFF));
        builder.border(f2, f13, f4, f10, f14, Math.max(1.0f, f5 * 0.8f), GpuClickGuiScreen.argb(112.0f * f12, n2 & 0xFFFFFF));
        float f15 = Math.round(18.0f * f5);
        float f16 = f13 + f10 * 0.5f;
        GcsCategoryIcons.draw(builder, GpuClickGuiScreen.bf("\ubf6f\u53ed\uf78b\ubb3d\u8e16\u1cc6\ud6a6\u6381\u9ffc", 1683861431, 0x1B1EF9FC ^ CryptoVault.mask()), Math.round(f2 + 14.0f * f5), Math.round(f16 - f15 * 0.5f), f15, GpuClickGuiScreen.argb(255.0f * f12, n2 & 0xFFFFFF));
        float f17 = Math.round(24.0f * f5);
        String string = GpuClickGuiScreen.bf("\uebb2\u3ba3\u4a58\u3e32\uec06\u1b7e\u4ead\udb1c\u1ad5\u8f27\ua8cd\ud18e\ua707\u3ab8\u536f\u9a03\u7ea3\uf16a", 981795836, 0x1B1EF9FC ^ CryptoVault.mask());
        String string2 = GpuClickGuiScreen.bf("\ucf4e\ubad5\u3ecc\u8862\u3028\u4af3\u0a34\ud0f2\u46f3\ubc78\u6e59\u3b68", -906375444, 0x1B1EF9FC ^ CryptoVault.mask());
        float f18 = UiKit.buttonWidth(string, f5 * 0.9f);
        float f19 = UiKit.buttonWidth(string2, f5 * 0.9f);
        float f20 = Math.round(f2 + f4 - 12.0f * f5 - f19);
        float f21 = Math.round(f16 - f17 * 0.5f);
        float f22 = Math.round(f20 - 8.0f * f5 - f18);
        float f23 = f2 + 14.0f * f5 + f15 + 12.0f * f5;
        float f24 = Math.max(0.0f, f22 - 12.0f * f5 - f23);
        GpuClickGuiScreen.textMid(builder, f23, f16 - 7.0f * f5, 11.5f * f5, GpuClickGuiScreen.argb(255.0f * f12, this.textColor & 0xFFFFFF), UiKit.fit(GpuClickGuiScreen.bf("\u2277\ueeaf\u4693\u01d4\udbde\u3b05\ud97c\u6b39\u4be2\u605c\uef23\uec3c\ue0e3\u31d3\ud7b5\u437f\u45af", -1748592870, 0x1B1EF9FC ^ CryptoVault.mask()), f24, 11.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        GpuClickGuiScreen.textMid(builder, f23, f16 + 8.0f * f5, 9.5f * f5, GpuClickGuiScreen.argb(255.0f * f12, this.mutedColor & 0xFFFFFF), UiKit.fit(GpuClickGuiScreen.bf("\ue180\u717f\ue2d9\u0c00\ue6a1\ue491\ued7f\u1112\ub9b7\uc346\u7c6b\ue40f\ub4db\u3593\ud927\u1881\ua990\u7f29\ueff7\u33c0\udef8\ubb6b\u43d1\u8eaa\u528d\u597e\udc5c\u9bfe\u2a74\u6e18\u66b9\u70a4\ufc82\ub180\u3ea0\u89b9\u4db2\u0a3f\u62d4\u3e70\ucf38\uc421\u9964\uf9d0\u1312\u20c7\u4976\u087e", 808752512, 0x1B1EF9FC ^ CryptoVault.mask()), f24, 9.5f * f5), UiDrawCommand.TextWeight.REGULAR);
        if (f9 > 0.6f) {
            UiKit.button(builder, this.kitHost, string, f22, f21, f18, f17, f5 * 0.9f, UiKit.Style.GHOST, true, f6, f7, () -> {
                if (HitboxConfigScreen752809.aH != null) {
                    HitboxConfigScreen752809.aH.setEnabled(true);
                }
            });
            UiKit.button(builder, this.kitHost, string2, f20, f21, f19, f17, f5 * 0.9f, UiKit.Style.PRIMARY, true, f6, f7, () -> {
                bedwarsNoticeContinued = true;
            });
        }
        builder.clipPop();
        return f3 + f11 + 10.0f * f5 * f9;
    }

    private Module aiModule(String string) {
        for (Module module : this.modulesForCategory(Category.vR)) {
            if (!string.equals(module.getName())) continue;
            return module;
        }
        return null;
    }

    private void drawAiPage(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        Module module = this.aiModule(GpuClickGuiScreen.bf("\u7fbf\u5dce\u78dc\u6f2b\ubd1e\u976b", 1203697746, 0x1B1EF9FC ^ CryptoVault.mask()));
        Module module2 = this.aiModule(GpuClickGuiScreen.bf("\u15c7\uebbc\u2811\u3533\u0299\ufd9c\uceb2\u0c8b", -1034305984, 0x1B1EF9FC ^ CryptoVault.mask()));
        this.lastCatalogModules = List.of();
        this.catalogReorderable = false;
        this.catalogDrawn = true;
        float f10 = f3 + 18.0f * f6;
        builder.text(f2 + 20.0f * f6, f10 + 1.0f * f6, 16.0f * f6, this.titleColor, "AI", UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2 + 20.0f * f6, f10 + 24.0f * f6, 10.5f * f6, this.mutedColor, UiKit.fit(GpuClickGuiScreen.bf("\u58db\u84c6\ub197\u6cef\uc929\u76a2\u5ff4\u6ba7\u7509\ubd39\ua5e4\u9512\uabc8\ua3ae\u96f2\u800a\u4b6a\u86e0\ucc2d\ua2ed\ue743\uff9d\ucc9b\u0939\u8796\ufc78\ua3ca\ua51a\u3689\uc184\u5814\ucbc1\u57c2\ub31c\udb07\u97b3\uc7a5\u5c06\ueee8\ufa8c\uaa6b\u35fe\u6155\uccbb\uf4bb\u8d4d\u95eb\u4ad0\u00cf\u3b7c\u6703\u41c5", 703298773, 0x1B1EF9FC ^ CryptoVault.mask()), f4 - 40.0f * f6, 10.5f * f6));
        if (this.showSeparators) {
            builder.rect(f2 + 16.0f * f6, f10 + 42.0f * f6, f4 - 32.0f * f6, f6, 0.0f, this.lineColor);
        }
        float f11 = f10 + 50.0f * f6;
        float f12 = f3 + f5 - 12.0f * f6;
        float f13 = Math.max(0.0f, f12 - f11);
        if (!GpuClickGuiScreen.bf("\uc96b\ufa05\u539a\u1f78\u89cc\ufc50\u10fa\ua01d\ufa12", 1308944390, 0x1B1EF9FC ^ CryptoVault.mask()).equals(this.catalogKey)) {
            this.catalogKey = GpuClickGuiScreen.bf("\u1468\u91c8\uf704\ud922\ue920\u8182\ue270\u0a7b\u1a06\u9c02", 736912928, 0x1B1EF9FC ^ CryptoVault.mask());
            this.catalogSwitchAt = this.nanoTime();
            this.catalogScroll.reset();
        }
        this.catalogScroll.update(this.frameDelta, f13, Math.max(0.0f, this.aiContentH), this.nanoTime());
        float f14 = this.catalogScroll.offset();
        builder.clipPush(f2 + 10.0f * f6, f11, f4 - 20.0f * f6, f13, 0.0f);
        this.clipX = f2 + 10.0f * f6;
        this.clipY = f11;
        this.clipW = f4 - 20.0f * f6;
        this.clipH = f13;
        this.clippingHits = true;
        float f15 = Math.min(f4 - 32.0f * f6, (float)Math.round(860.0f * f6));
        float f16 = Math.round(f2 + (f4 - f15) * 0.5f);
        float f17 = f9 = f11 + 8.0f * f6 - f14;
        float f18 = this.cardRadius * f6;
        if (module != null) {
            float f19 = Math.round(62.0f * f6);
            this.surfaceCard(builder, f16, f17, f15, f19, f18);
            if (module.isEnabled()) {
                builder.border(f16, f17, f15, f19, f18, Math.max(1.0f, f6), GpuClickGuiScreen.withA(this.accentArgb(), 128));
            }
            float f20 = 34.0f * f6;
            float f21 = 18.0f * f6;
            float f22 = f16 + f15 - f20 - 16.0f * f6;
            float f23 = f17 + (f19 - f21) * 0.5f;
            float f24 = Math.max(0.0f, f22 - 16.0f * f6 - (f16 + 16.0f * f6));
            GpuClickGuiScreen.textMid(builder, f16 + 16.0f * f6, f17 + f19 * 0.5f - 8.0f * f6, 13.5f * f6, this.textColor, GpuClickGuiScreen.bf("\ucf06\u184b\u8065\u0248\u6fff\ub34f\u449c\u4cec\u3994", 358219199, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            float f25 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\uebf4\uab5a3\u544b\u5d76\u473b\u0bfc", -609316805, 0x1B1EF9FC ^ CryptoVault.mask()), 13.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            float f26 = 8.5f * f6;
            float f27 = 6.0f * f6;
            float f28 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u3586\u408c\ub121\ucc6d\u6a48\uc66d\u41ad\u37ec\ud7a7\u33f9\u7d79\uf1c9", 1359371934, 0x1B1EF9FC ^ CryptoVault.mask()), f26, UiDrawCommand.TextWeight.SEMIBOLD) + f27 * 2.0f;
            float f29 = Math.round(15.0f * f6);
            float f30 = f16 + 16.0f * f6 + f25 + 8.0f * f6;
            float f31 = f17 + f19 * 0.5f - 8.0f * f6 - f29 * 0.5f;
            builder.rect(f30, f31, f28, f29, f29 * 0.5f, 872394784);
            builder.border(f30, f31, f28, f29, f29 * 0.5f, Math.max(1.0f, f6 * 0.8f), -1711296480);
            GpuClickGuiScreen.textMid(builder, f30 + f27, f31 + f29 * 0.5f, f26, -14741, GpuClickGuiScreen.bf("\udc91\u34d1\uc1c3\u9410\u8d35\uc643\u8f91\uc133\u764a", 1139975699, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            GpuClickGuiScreen.textMid(builder, f16 + 16.0f * f6, f17 + f19 * 0.5f + 10.0f * f6, 10.0f * f6, this.mutedColor, UiKit.fit(module.isEnabled() ? GpuClickGuiScreen.bf("\uec70\u3a9f\u6510\ub962\u40fe\u4c47\u46ad\uaad6\ub84f\u000e\u260c\u7f76\uf49d\u556a\u78a2\u0157\u010d\u5bc4\ufd40\u397f\ud782\u58b5\uf6da\ufe4b\u87e1\u1c4c\u31dc\u0d01\ua85b\u2425\uf7b5\u3f4d\u2ea8\uc3f8\u6ff4\u55ef\u62c3\u4940\u33b8\uafc4", -1317737355, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u033d\u569e\u375c\u5bd1\u04f8\uc1aa\ucbe2\u0b7e\u6e8c\u8761\ufc5b\u4f0c\u6d98\ufc17\uc7cf\u2bc4\u5a54\u57a0\u558b\ufcdb\u17da\u5cd2\u6036\uba5c\ub223\uc819\u1d19\uf77b\u2f1a\uce04\ud043\u6de6\u7f2c\u074e\u1260\u6fd9\ub6e4\ufc63\u66b5\u9678\u1cf5\ue9a4\ue23b", 636592587, 0x1B1EF9FC ^ CryptoVault.mask()), f24, 10.0f * f6), UiDrawCommand.TextWeight.REGULAR);
            this.drawToggleSwitch(builder, GpuClickGuiScreen.bf("\ub26c\ua652\ub6c2\u6909\u13fa\ud03f\ub20f\uc3d3", 730549563, 0x1B1EF9FC ^ CryptoVault.mask()), f22, f23, f20, f21, module.isEnabled(), f6);
            if (!module.isLocked()) {
                this.hit(f16, f17, f15, f19, module::toggle);
            }
            if (this.hovered(f7, f8, f16, f17, f15, f19)) {
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\u03f2\u2fec\u996d\u33d9\u9b45\ud213\ubb14\u25e4", 1444721476, 0x1B1EF9FC ^ CryptoVault.mask());
                this.hoveredTooltipDesc = GpuClickGuiScreen.safeText(module.getDescription());
            }
            this.drawInlineBind(builder, module, f16, f17 += f19 + 4.0f * f6, f15, f6, f7, f8);
            f17 += 42.0f * f6 + 6.0f * f6;
            for (SettingGroup settingGroup : GpuClickGuiScreen.settingGroups(module)) {
                f17 = this.drawSettingGroup(builder, settingGroup.title(), settingGroup.settings(), f16, f17, f15, f6, f7, f8, GpuClickGuiScreen.groupKey(module, settingGroup));
            }
        }
        if (module2 != null) {
            f17 += 10.0f * f6;
            if (this.showSeparators) {
                builder.rect(f16, f17, f15, f6, 0.0f, this.lineColor);
            }
            builder.text(f16 + 4.0f * f6, f17 += 14.0f * f6, 13.5f * f6, this.textColor, GpuClickGuiScreen.bf("\u092f\udad5\ua1f8\u35d9\u924c\uf04b\uf36e\ue06e", -1011899215, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            f17 += 20.0f * f6;
            List<String> list = GpuClickGuiScreen.wrapText(GpuClickGuiScreen.bf("\u7caf\u1c67\u74b1\ub01e\udf54\uecfe\u6332\u2454\ub0ec\u78c8\u4a14\u377c\u0854\u9730\u5722\u4d9e\u4520\u68b5\ua6df\u99bb\uc6cf\ud3b8\uf507\ud0eb\u2e7b\ud417\ue4a8\u70c3\ue8b1\u025e\u419f\uadbf\u2c35\u3c2a\u4d2e\uac96\ue555\u0744\ub133\u4331\u7c9d\u18c9\ua800\u01ce\u1e75\u05c2\u5ce1\ue78f\u736a\uc82d\ueac4\u8840\u30cf\u4e8a\u3fa7\u54d0\ue728\u170a\ua014\ubff8\uee81\ua9a1\uc582\u67e2\u8eee\u3292\u7f83\u2d85\uc2dc\u7aa1\u6743\ubdc9\u0759\u574b\u2608\u25d0\uadb7\u6cf9\u2671\u81a5\u755b\u74db\u3ced\u7e0c\ua45d\u937a\u4214\u338f\u053c\u15f5\ue3eb\ud114\u3771\ucc7f\u84ee\ud70f\u63ac\uec6f\ueaa6\ue356\u52eb\u5e96\ubacb\u6ffe\u7195\uf619\u8e2d\u18ce\u7603\u4a33\ud6ce\u6ee4\uc2ee\ua43a\u8e1e\u12a2\uccaf\ub648\u612b\ub5a8\ubb04\u8d92\u620c\ua9ea\uce42\u3c44\ufa06\u6b01\u7d08\u1854\u0cf7\u20ae\u07cb\u64d8\ue591\ub5a9\u8f35\u20f4\ud459\u0a4c\u085a\u83d5\u53ef\uaf69\u3180\u8bcd\u4f43\ud9b9\ubaab\u8849\u0d5c\u6ed4\u7da4\ud5d0\udd6b\u9891\ue85e\ude90\ufc31\u2ede\uf4e7\udeb3\u2991\u426e\u5b2b\u5caf\u6406\u821f", 1735672242, 0x1B1EF9FC ^ CryptoVault.mask()), f15 - 8.0f * f6, 10.5f * f6, 3);
            for (String string : list) {
                builder.text(f16 + 4.0f * f6, f17, 10.5f * f6, this.mutedColor, string);
                f17 += 14.0f * f6;
            }
            f17 += 14.0f * f6;
            for (SettingGroup settingGroup : GpuClickGuiScreen.settingGroups(module2)) {
                f17 = this.drawSettingGroup(builder, settingGroup.title(), settingGroup.settings(), f16, f17, f15, f6, f7, f8, GpuClickGuiScreen.groupKey(module2, settingGroup));
            }
        }
        if (module == null && module2 == null) {
            UiKit.emptyState(builder, this.kitHost, GpuClickGuiScreen.bf("\uff75\u2ef2\uc54c\u4d16\u7791\u6f03\uba6b\ued45\u7845\u1aba\u3aeb\ub65a\u16d4\ud511\u2a38\u1051\u5a9b\u07cc\ua68f\u8894\uf691\uf72e", 1005169325, 0x1B1EF9FC ^ CryptoVault.mask()), "", f2 + 10.0f * f6, f9 + 34.0f * f6, f4 - 20.0f * f6, f6);
            f17 += 60.0f * f6;
        }
        this.aiContentH = f17 - f9 + 20.0f * f6;
        this.clippingHits = false;
        builder.clipPop();
    }

    /*
     * WARNING - void declaration
     */
    private void drawModulesCatalog(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, boolean bl, float f6, float f7, float f8) {
        float f9;
        Object f52;
        float f10;
        float f11;
        float f12;
        float f13;
        int n2;
        CatalogSections.Layout layout;
        float bl9;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        float f19;
        float f20;
        boolean f29;
        float f21;
        float f22;
        float f23;
        float f24;
        float string4;
        String string;
        GuiPrefs.CustomCategory customCategory;
        boolean bl3;
        if (this.category == Category.vR && this.folderId == null && moduleFilter == ModuleFilter.ALL && this.searchField.value().trim().isEmpty()) {
            this.drawAiPage(builder, f2, f3, f4, f5, f6, f7, f8);
            return;
        }
        String string2 = this.searchField.value().trim();
        boolean bl2 = bl3 = !string2.isEmpty();
        if (!bl3 && moduleFilter == ModuleFilter.ALL) {
            this.searchScoped = false;
        }
        boolean bl5 = moduleFilter != ModuleFilter.ALL && !this.searchScoped;
        GuiPrefs.CustomCategory customCategory2 = customCategory = this.folderId == null ? null : GpuClickGuiScreen.guiPrefs().customCategory(this.folderId);
        if (this.folderId != null && customCategory == null) {
            this.folderId = null;
        }
        List<CatalogEntry> list = this.catalog(string2, bl5);
        String string22 = string2.toLowerCase(Locale.ROOT);
        if (this.navIndex >= list.size()) {
            this.navIndex = list.isEmpty() ? -1 : list.size() - 1;
        }
        this.catalogReorderable = !bl3 && moduleFilter == ModuleFilter.ALL && moduleSort == ModuleSort.DEFAULT && !bl5;
        ArrayList<Module> arrayList = new ArrayList<Module>(list.size());
        for (CatalogEntry object22 : list) {
            arrayList.add(object22.module());
        }
        this.lastCatalogModules = arrayList;
        if (!(this.dragModule == null || this.catalogReorderable && arrayList.contains(this.dragModule))) {
            this.cancelCardDrag();
        }
        if (!(this.renamingFolderId == null || customCategory != null && this.renamingFolderId.equals(this.folderId))) {
            this.commitFolderRename();
        }
        this.catalogDrawn = true;
        float f27 = f3 + 18.0f * f6;
        String string3 = string = customCategory != null ? customCategory.name() : this.category.zw;
        String string5 = bl3 ? GpuClickGuiScreen.bf("\ucfbe\u9379\u6416\u9dcf\ua291\u0d7d\u66d4", -1210813591, 0x1B1EF9FC ^ CryptoVault.mask()) : (moduleFilter != ModuleFilter.ALL ? GpuClickGuiScreen.moduleFilter.label : string);
        boolean bl4 = customCategory != null && customCategory.id().equals(this.renamingFolderId) && !bl3 && moduleFilter == ModuleFilter.ALL;
        float f25 = f27 + 1.0f * f6 + 16.0f * f6 * 0.727f * 0.5f;
        if (bl4) {
            string3 = Math.round(28.0f * f6);
            string4 = Math.round(Math.min(240.0f * f6, f4 * 0.45f));
            this.folderNameField.draw(builder, this.kitHost, f2 + 14.0f * f6, Math.round(f25 - string3 * 0.5f), string4, string3, f6);
            f24 = f2 + 14.0f * f6 + string4 + 10.0f * f6;
        } else {
            string3 = Math.max(0.0f, f4 - 46.0f * f6 - UiKit.buttonWidth(GpuClickGuiScreen.bf("\u5095\uc186\ub7ef\u7601\u9566\u4770\u73aa", 301194737, 0x1B1EF9FC ^ CryptoVault.mask()) + GpuClickGuiScreen.moduleSort.label, f6 * 0.9f));
            string5 = UiKit.fit(string5, string3, 16.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2 + 20.0f * f6, f27 + 1.0f * f6, 16.0f * f6, this.titleColor, string5, UiDrawCommand.TextWeight.SEMIBOLD);
            f24 = f2 + 26.0f * f6 + GpuUiFontMetrics.textWidth(string5, 16.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
        }
        if (bl3 || moduleFilter != ModuleFilter.ALL) {
            String string52 = list.size() == 1 ? GpuClickGuiScreen.bf("\u3b49\ubbd3\ue7ef\u1414\uea79\u1555\u0e07\u7b4a\u8d12\u1d5d\ub7ab", 1629435333, 0x1B1EF9FC ^ CryptoVault.mask()) : list.size() + GpuClickGuiScreen.bf("\u67e0\u13b9\u7c41\ube8b\uf362\u4cc6\u4766\ue1dc\u925b", -308838882, 0x1B1EF9FC ^ CryptoVault.mask());
            GpuClickGuiScreen.textMid(builder, f24, f25, 10.0f * f6, this.muted2Color, string52, UiDrawCommand.TextWeight.REGULAR);
            f24 += GpuUiFontMetrics.textWidth(string52, 10.0f * f6) + 10.0f * f6;
            if (this.searchScoped) {
                String string6 = GpuClickGuiScreen.bf("\u261a\ud055\u234b\ub40d", -1782982873, 0x1B1EF9FC ^ CryptoVault.mask()) + string + GpuClickGuiScreen.bf("\uebe3\u1647\u44a1\ubaf9\ua0e4", 1280957943, 0x1B1EF9FC ^ CryptoVault.mask());
                f23 = Math.round(18.0f * f6);
                f22 = Math.round(GpuUiFontMetrics.textWidth(string6, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f6);
                f21 = Math.round(f25 - f23 * 0.5f);
                f29 = this.hovered(f7, f8, f24, f21, f22, f23);
                builder.rect(f24, f21, f22, f23, f23 * 0.5f * Math.min(1.0f, this.cornerScale), GpuClickGuiScreen.withA(this.accentArgb(), f29 ? 64 : 36));
                GpuClickGuiScreen.textCentered(builder, f24 + f22 * 0.5f, f21 + f23 * 0.5f, 8.5f * f6, this.accentArgb(), string6, UiDrawCommand.TextWeight.SEMIBOLD);
                this.hit(f24, f21, f22, f23, () -> {
                    this.searchScoped = false;
                    this.navIndex = this.searchField.isEmpty() ? -1 : 0;
                });
            }
        }
        String string7 = GpuClickGuiScreen.bf("\u48dc\ua230\uc12d\u8e66\u927e\udcef\ube62\ua112", -1232949364, 0x1B1EF9FC ^ CryptoVault.mask()) + GpuClickGuiScreen.moduleSort.label;
        string4 = Math.round(22.0f * f6);
        f23 = UiKit.buttonWidth(string7, f6 * 0.9f);
        f22 = Math.round(f2 + f4 - 16.0f * f6 - f23);
        f21 = Math.round(f25 - string4 * 0.5f);
        UiKit.button(builder, this.kitHost, string7, f22, f21, f23, string4, f6 * 0.9f, UiKit.Style.GHOST, true, f7, f8, () -> {
            GpuClickGuiScreen.cycleModuleSort();
            this.catalogCacheKey = "";
        });
        if (this.hovered(f7, f8, f22, f21, f23, string4) && !bl3 && moduleFilter == ModuleFilter.ALL) {
            this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\ue4a7\u5b35\u99a9\uea2e\u2294\uf1a3\ue665", -699534166, 0x1B1EF9FC ^ CryptoVault.mask());
            String string8 = this.hoveredTooltipDesc = moduleSort == ModuleSort.DEFAULT ? GpuClickGuiScreen.bf("\uf217\u9c3e\ue669\uf571\uad89\ua4ed\ud90d\u3bc9\ub3cd\u1416\u13d2\ud723\u104f\udacf\u4def\u8a94\uc6d9\ub416\u1a07\ud7db\u220b\uac32\udeba\u27b6\u21b1\u22a9\u2217\u7905\u551a\u9f32\uf9d8\u5962\ud057\ufef5\u085c\u00a3\uf186\u06ce\u3aad\ue664\u8f69\u358a\u7daa\ubf27\u0dc3\uf83e\ubc7c\ub3a9\u6929\u0ce3", 546859234, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u32d6\u1538\u9d8a\u4b33\ubb0b\u0fc7\u785c\u5558\ufa4e\ubc63\u6a2d\ud9f9\u2bdb\ueeed\u7cd9\u10d9\u1a3a7\u9993\ub88d\ub0f4\u7191\ud623\u4c03\u25f9\uff33\ubb93\uc677\u82a2\ue543\ue8b6\u8594\u557f\ueb94\ud411\ue05d\u4c30\uaf12\uda83\u894a\u176f\ud416\u4252\u7f09\u2ba0\uca97\u8927\u828f\uef92\uc5f5\u1647\u6d79\u5b20\u0dfa\u7dbf\u424c\u45e8\u40b0", 1402896947, 0x1B1EF9FC ^ CryptoVault.mask());
        }
        if (customCategory != null && !bl3 && moduleFilter == ModuleFilter.ALL) {
            boolean bl6 = f29 = this.deleteFolderArmed && this.nanoTime() - this.deleteFolderArmedAt < 3000000000L;
            if (!f29) {
                this.deleteFolderArmed = false;
            }
            String f30 = f29 ? GpuClickGuiScreen.bf("\u2e87\u38c1\uba31\u035b\udf93\u3765\uc313\uaee1\u6288\u8506\uefa8\u2911\u0fa6\u4c5d\ue7f6\ufbe1\udfe6", -111143654, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u6003\u40c6\u182b\ue69a\u3d52\ueab7\u5e5d", -2086339898, 0x1B1EF9FC ^ CryptoVault.mask());
            f20 = UiKit.buttonWidth(f30, f6 * 0.9f);
            f19 = Math.round(f22 - 8.0f * f6 - f20);
            String bl8 = customCategory.id();
            if (f19 > f24 + 4.0f * f6) {
                UiKit.button(builder, this.kitHost, f30, f19, f21, f20, string4, f6 * 0.9f, f29 ? UiKit.Style.DANGER : UiKit.Style.GHOST, true, f7, f8, () -> {
                    if (this.deleteFolderArmed && this.nanoTime() - this.deleteFolderArmedAt < 3000000000L) {
                        this.deleteFolder(string8);
                    } else {
                        this.deleteFolderArmed = true;
                        this.deleteFolderArmedAt = this.nanoTime();
                    }
                });
                if (!bl4 && (f18 = (float)Math.round(f19 - 6.0f * f6 - (f17 = UiKit.buttonWidth(GpuClickGuiScreen.bf("\u3bdf\u4f67\ub1d1\udf54\ucc9e\uc61d\u4afc\u1eb0\ub03a", 1699648348, 0x1B1EF9FC ^ CryptoVault.mask()), f6 * 0.9f)))) > f24 + 4.0f * f6) {
                    UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u5fea\u73cc\ue779\u1a95\u4dd0\ua2e5\ue7cf", -1163747036, 0x1B1EF9FC ^ CryptoVault.mask()), f18, f21, f17, string4, f6 * 0.9f, UiKit.Style.GHOST, true, f7, f8, () -> this.startFolderRename(string8));
                }
            }
        }
        if (this.showSeparators) {
            builder.rect(f2 + 16.0f * f6, f27 + 26.0f * f6, f4 - 32.0f * f6, f6, 0.0f, this.lineColor);
        }
        float f26 = f27 + 36.0f * f6;
        float f28 = Math.round(36.0f * f6);
        f20 = f2 + 16.0f * f6;
        f19 = f4 - 32.0f * f6;
        boolean bl7 = this.searchField.focused(this.kitHost);
        f17 = Math.min(f28 * 0.5f, this.cardRadius * f6);
        if (this.lg) {
            this.glassPane(builder, f20, f26, f19, f28, f17, 1.0f, bl7 ? 58 : 44, false, 0);
        } else {
            builder.rect(f20, f26, f19, f28, f17, GpuClickGuiScreen.argb((float)(bl7 ? 56 : 42) * this.panelOpacityK, 0));
        }
        builder.border(f20, f26, f19, f28, f17, Math.max(1.0f, f6 * 0.8f), bl7 ? GpuClickGuiScreen.argb(144.0f, this.accentArgb()) : 0x1EFFFFFF);
        f18 = Math.round(14.0f * f6);
        GcsCategoryIcons.draw(builder, GpuClickGuiScreen.bf("\uc0e3\u57d6\ua7d0\u5b63\u03d6\u5345\ub334", 507374406, 0x1B1EF9FC ^ CryptoVault.mask()), Math.round(f20 + 12.0f * f6), Math.round(f26 + (f28 - f18) * 0.5f), f18, bl7 ? this.accentArgb() : this.mutedColor);
        this.searchField.draw(builder, this.kitHost, f20, f26, f19, f28, f6);
        if (this.searchField.isEmpty() && !bl7 && f19 > 260.0f * f6) {
            String f31 = GpuClickGuiScreen.bf("\ud196\u6b37\u22d7\uc4f2\ub13e\u6ff3\u3483\ue66b\u76bd", 1578665114, 0x1B1EF9FC ^ CryptoVault.mask());
            f16 = GpuUiFontMetrics.textWidth(f31, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 12.0f * f6;
            f15 = Math.round(18.0f * f6);
            f14 = Math.round(f20 + f19 - f16 - 9.0f * f6);
            bl9 = Math.round(f26 + (f28 - f15) * 0.5f);
            builder.rect(f14, bl9, f16, f15, f15 * 0.5f * Math.min(1.0f, this.cornerScale), this.controlFill(20));
            GpuClickGuiScreen.textCentered(builder, f14 + f16 * 0.5f, bl9 + f15 * 0.5f, 8.5f * f6, this.muted2Color, f31, UiDrawCommand.TextWeight.SEMIBOLD);
        }
        if (!this.searchField.isEmpty()) {
            float f32;
            boolean bl8 = this.hovered(f7, f8, f15 = f20 + f19 - (f32 = 20.0f * f6) - 8.0f * f6, f14 = f26 + (f28 - (f16 = 20.0f * f6)) * 0.5f, f32, f16);
            builder.circle(f15 + f32 * 0.5f, f14 + f16 * 0.5f, 8.0f * f6, bl8 ? this.controlFill(56) : this.controlFill(26));
            GpuClickGuiScreen.textCentered(builder, f15 + f32 * 0.5f, f14 + f16 * 0.5f, 9.0f * f6, bl8 ? this.titleColor : this.mutedColor, "x", UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f15, f14, f32, f16, () -> {
                this.searchField.clear();
                this.kitHost.focus(this.searchField);
            });
        }
        float f30 = f26 + f28 + 12.0f * f6;
        if (this.category == Category.fK && !bl5 && this.folderId == null) {
            f30 = this.drawBedwarsNotice(builder, f2 + 16.0f * f6, f30, f4 - 32.0f * f6, f6, f7, f8);
        } else {
            this.springs.set(GpuClickGuiScreen.bf("\ud90c\u51ff\ue2b6\ufb47\u86e5\u319b\ud6cd\uef70\u2266\u49ed\u817e\u1c70\u5748\udb44\u4b6f\ue1b5\u85f5\u60af", -811530626, 0x1B1EF9FC ^ CryptoVault.mask()), 0.0f);
        }
        f16 = f3 + f5 - 12.0f * f6;
        f15 = Math.round(58.0f * f6);
        f14 = Math.round(10.0f * f6);
        bl9 = Math.round(12.0f * f6);
        float f31 = Math.round(14.0f * f6);
        int n3 = Math.max(1, (int)((f4 - f31 * 2.0f + bl9) / ((float)Math.round(360.0f * f6) + bl9)));
        float f32 = Math.max(0.0f, (float)Math.round((f4 - f31 * 2.0f - bl9 * (float)(n3 - 1)) / (float)n3));
        float f33 = f15 + f14;
        float f34 = Math.max(0.0f, f16 - f30);
        this.lastCatalogCols = n3;
        this.lastCatalogRowPitch = f33;
        this.lastCatalogViewH = f34;
        float f35 = Math.round(24.0f * f6);
        this.lastLayout = layout = CatalogSections.layout(this.catalogGroups, list.size(), n3, f33, f35);
        int n4 = -1;
        float f36 = 0.0f;
        if (this.inlineSettingsMode() && this.selected != null && this.moduleInspectorProgress > 0.002f) {
            for (n2 = 0; n2 < list.size(); ++n2) {
                if (list.get(n2).module() != this.selected) continue;
                n4 = n2;
                break;
            }
            if (n4 >= 0) {
                f36 = this.inlineUnits(this.selected) * f6 * this.moduleInspectorProgress;
            }
        }
        n2 = n4 >= 0 ? layout.row[n4] : -1;
        String string9 = (Object)((Object)this.category) + "|" + this.folderId + "|" + string2;
        if (!string9.equals(this.catalogKey)) {
            this.catalogKey = string9;
            this.catalogSwitchAt = this.nanoTime();
            this.catalogScroll.reset();
        }
        float f37 = Math.max(6.0f * f6, this.hoverK * 2.5f * f6 + 2.0f * f6);
        List<QuickPin> list2 = !bl3 && moduleFilter == ModuleFilter.ALL ? this.quickPins() : List.of();
        float f38 = this.quickStripHeight(list2, n3, f6);
        this.catalogScroll.update(this.frameDelta, f34, Math.max(0.0f, f38 + layout.height - f14 + f37 + f36), this.nanoTime());
        if (this.restoreCatalogScroll >= 0.0f) {
            this.catalogScroll.scrollTo(this.restoreCatalogScroll, true);
            this.restoreCatalogScroll = -1.0f;
        }
        if (this.dragModule != null) {
            f13 = 28.0f * f6;
            f12 = 700.0f * f6 * this.frameDelta;
            if (this.dragPointerY < f30 + f13) {
                this.catalogScroll.scrollTo(this.catalogScroll.target() - f12, false);
            } else if (this.dragPointerY > f16 - f13) {
                this.catalogScroll.scrollTo(this.catalogScroll.target() + f12, false);
            }
        }
        if (this.navScrollPending && this.navIndex >= 0) {
            this.navScrollPending = false;
            f13 = f38 + layout.y(Math.min(this.navIndex, layout.n() - 1));
            if (f13 < (f12 = this.catalogScroll.target())) {
                this.catalogScroll.scrollTo(f13, false);
            } else if (f13 + f15 > f12 + f34) {
                this.catalogScroll.scrollTo(f13 + f15 - f34, false);
            }
        }
        f13 = this.catalogScroll.offset();
        f12 = (float)(this.nanoTime() - this.catalogSwitchAt) / 1.0E9f;
        if (!this.reducedMotion()) {
            f12 = Math.min(f12, (float)(this.nanoTime() - this.openedAt) / 1.0E9f - 0.18f);
        }
        builder.clipPush(f2 + 10.0f * f6, f30, f4 - 20.0f * f6, f16 - f30, 0.0f);
        this.clipX = f2 + 10.0f * f6;
        this.clipY = f30;
        this.clipW = f4 - 20.0f * f6;
        this.clipH = f16 - f30;
        this.clippingHits = true;
        if (list.isEmpty()) {
            String f42;
            String f41;
            if (bl3) {
                f41 = GpuClickGuiScreen.bf("\u944b\ufb24\u0b65\uee7b\ua041\u94ec\u801f\uf827\u744d\uf098\u60ba\ud4f3\u7a7f\u2419\u9a88\u0294\u54e9\ua261\ud91e", -1507706817, 0x1B1EF9FC ^ CryptoVault.mask()) + UiKit.fit(string2, 160.0f * f6, 12.0f * f6) + "\"";
                f42 = this.searchScoped ? GpuClickGuiScreen.bf("\ufac6\u6deb\u97a5\u4290\ub93d\uac09\u4e01", -902437385, 0x1B1EF9FC ^ CryptoVault.mask()) + this.category.zw + GpuClickGuiScreen.bf("\u5a15\u47da\u0134\u8841\u8d64\u0307\u794f\ud029\u09b8\u00bc\ub979\u8a3f\ue3c6\u87ec\u3e53\u9d85\u38c9\u9f30\u9e39\u6b74\ueaf8\u9db2\u2aff\ud62b\u4ff9\u0188\u9328\u7b2d\u95ff\uc095\uc612\u1ab8\u08de", 1304268752, 0x1B1EF9FC ^ CryptoVault.mask()) + this.category.zw + GpuClickGuiScreen.bf("\u5237\uf783\u39af\ua454\ue055\u5d00\u9513\u8122!\u7bae\ufb05\u1ec8\u368e\uab1f\u058a\u4d0a\u51d8\u41a4\u03b5\u9c48\ud8ee\u2e3c\u3d92\uaac0\u56f6\uac19\u288b\u595b\ud6dd\ud0f9", 1499143631, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u4d1a\ub9ee\u95ee\u4382\u8e3a\u924b\u0e17\u078e\udabf\u055a\u3bed\u785b\uba78\ue817\u9f83\ud4ad\u842a\ue964\ub92a\ufd0c\u546e\ueedb\u669d\u79c7\u26ce\udcbf\u9f21\ua037\u4733\u770b\u8f7e\u755f\ubd44\u4853\u394e\u56a4\ua0c9\u1187\u8b5d\uab6d\u085c\u567f\u884a\u5d44\uf846\u6e42\u3b16\ue400\u71e1\u50b0\u959d\u931e\uff4c\u8522\u02e6\u211c\u15af\uc249\u71c0\u132e\ua21a\ue473\u2d9d", -1369761779, 0x1B1EF9FC ^ CryptoVault.mask());
            } else if (moduleFilter == ModuleFilter.FAVORITES) {
                f41 = GpuClickGuiScreen.bf("\u22f5\ue291\u1b5a\uc710\ud989\u3561\u0ffb\u3019\u15bf\u6f2f\u7f91\u6be3\u34fa\uf86a\u814d\u1082\u1cdb\u8cd0\u45ec", -329915102, 0x1B1EF9FC ^ CryptoVault.mask());
                f42 = GpuClickGuiScreen.bf("\u172b\uf042\ue355\u2479\u4c13\ueebf\uda3f\u319b\u9cd1\u6e7a\u4db0\u06c3\u5cb6\u4162\u6bf7\u96a6\u2629\ud119\u70cb\u28b1\ucb30\uf9f1\u210e\u4676\u201d\u77cc\uff1f\ufd64\ub9a4\u546f\u3cac\uab42\u59ba\u6815\u8fd5\ud2e0\u5358\u994c\u6a8f\u7f23\ud75b\u76f9\ub6b1\u69a8\ud4fb\ub85e\uc6e4\ua664\u45c4\u136e\ub55e\u22b7\ub1d3\u446b\ue117", -2115134721, 0x1B1EF9FC ^ CryptoVault.mask());
            } else if (moduleFilter == ModuleFilter.ENABLED) {
                f41 = GpuClickGuiScreen.bf("\u0a5a\ud209\uaf82\u1f3c\u4afb\u9a50\ud35b\ub26d\u3795\ubd6e\u28cb\ucf15\u858f\ub55a\u9b15\ub22e\u0f97\u4b83\u73cd\u4688", -74332813, 0x1B1EF9FC ^ CryptoVault.mask());
                f42 = GpuClickGuiScreen.bf("\ubbb2\u1561\u5f05\ufdc7\ubaac\uc40c\uaf88\u1839\u6ec9\u45fe\u0863\u2f62\ue10e\uea81\uf79e\u5d5b\u0d16\u4d21\ub660\ub6f1\ua0fc\u4b91\u54bc\udccb\ua7df\u78a2\ua65e\u82c0\ue0ea\u7a7d\u975d\u6b37\u39e0\uf780\u16d7\u01f1\u6fb7\ue707", -1800400951, 0x1B1EF9FC ^ CryptoVault.mask());
            } else if (moduleFilter == ModuleFilter.BOUND) {
                f41 = GpuClickGuiScreen.bf("\uf730\ua2b9\u21ca\ub57c\u943d\u1d27\u280b\ua352\u520b\u5e2b\ub873\u3c77\u5c9a\u9a89\u71c3\u6ba6\ucfdd\u518f\uc9c1\u67cc\ua244\uedc6\u027f\u2aab\u1589", -2058726768, 0x1B1EF9FC ^ CryptoVault.mask());
                f42 = GpuClickGuiScreen.bf("\ucaca\uc610\u3337\u5d9c\u5768\u697f\u10d0\u2055\u100e\ua9b2\ue7de\ue1e9\ua1f7\ufca8\uddad\u53c8\ua9d2\u51ec\u1222\u78ee\uc7cd\u8b1e\u7166\u4932\uf2ed\u97a2\u17e4\u4c97\ubdbe\uad7e\ua84a\ua8f9\u51ec\ueb98\u4898\u970b\ue3d3\u9094\uf1d0\ud9a5\u6344\ue0d4\uc1da\u3170\u128e\u0b9c\u1741\u0fc3\udb9c\uccc7\u9fd0\u9a6e\u1541\uf84a\u665e\u8726\uc523\u53f2\u4362\u295b\u96bc\ub7cc\u3eeb\ua02a\u6f24\uea21\u79e1\u70ba\u4b6b\u2b43\u4c1e\u7082\u29a4\u9382\u419d\u3412", -1619842922, 0x1B1EF9FC ^ CryptoVault.mask());
            } else if (customCategory != null) {
                f41 = GpuClickGuiScreen.bf("\ufc50\ub1a8\u81fc\u4691\u1aa2\u845c\u22ce\uf9a6\ue99c\u1dfb\u6ebb\ud225\udf3a\u5a58\u32be\u1630\u0aa8\ubb0d\u8b93\ua5ec\ub30b\u2b90\ubb64\ue8ff", -1168087223, 0x1B1EF9FC ^ CryptoVault.mask());
                f42 = GpuClickGuiScreen.bf("\u9117\u6370\u3fbe\u3cc1\u8e11\u6db4\uec9c\u6c52\u259e\u504d\u7229\uc40b\ubd26\u8974\u30f3\u6cd1\u1878\u0284\ud745\u7602\u6d18\u6d67\u48eb\u0e61\u43e0\u8821\uca98\u22d7\u885b\u88d6\uc520\u9629\u1151\u1c4a\u8280\ubd5b\ue978\ub2b6\u4e5b\u0bf8\uaea4\u2496", 193084898, 0x1B1EF9FC ^ CryptoVault.mask()) + customCategory.name() + GpuClickGuiScreen.bf("\u9ea7\ub189\u2e0b\u3cad\ue3a1\ue9cd\u767f\uc61c\u055d\ubf33\u2231\u1171\ud922\ua1d5\u25a9\u67ae\u28d3\u8e39\u9339", -1089014320, 0x1B1EF9FC ^ CryptoVault.mask());
            } else {
                f41 = GpuClickGuiScreen.bf("\ud780\ucd6e\udb91\u1ba9\uf60e\udc72\ubd6e\ub01d\u3c84\u99f7\ue6b1\u2247\u8e7f\u3ff6\ud8a4\ub2d9\uec0c\uf60d\u2687", -1537453565, 0x1B1EF9FC ^ CryptoVault.mask());
                f42 = "";
            }
            UiKit.emptyState(builder, this.kitHost, f41, UiKit.fit(f42, f4 - 60.0f * f6, 10.0f * f6), f2 + 10.0f * f6, f30 + f37 + f38 - f13 + 34.0f * f6, f4 - 20.0f * f6, f6);
        }
        float f39 = f2 + f31;
        float f40 = f30 + f37 + f38 - f13;
        if (!list2.isEmpty()) {
            this.drawQuickStrip(builder, list2, f39, f30 + f37 - f13, f4 - f31 * 2.0f, n3, f32, bl9, f14, f6, f7, f8);
        }
        if (!this.catalogGroups.isEmpty()) {
            float n5 = 10.0f * f6;
            f11 = f39 + (float)n3 * f32 + (float)(n3 - 1) * bl9;
            for (int f47 = 0; f47 < this.catalogGroups.size(); ++f47) {
                CatalogSections.Group f48 = this.catalogGroups.get(f47);
                f10 = f40 + layout.headerY[f47] + (n2 >= 0 && layout.row[f48.start()] > n2 ? f36 : 0.0f);
                if (f36 <= 0.0f && (f10 + f35 < f30 || f10 > f16)) continue;
                float n7 = f10 + 10.0f * f6;
                f52 = f48.sub().getName();
                float n6 = f39 + 4.0f * f6;
                GpuClickGuiScreen.textMid(builder, n6, n7, n5, this.mutedColor, (String)f52, UiDrawCommand.TextWeight.SEMIBOLD);
                float drop = n6 + GpuUiFontMetrics.textWidth((String)f52, n5, UiDrawCommand.TextWeight.SEMIBOLD) + 8.0f * f6;
                String f49 = String.valueOf(f48.size());
                GpuClickGuiScreen.textMid(builder, drop, n7, 9.0f * f6, this.muted2Color, f49, UiDrawCommand.TextWeight.REGULAR);
                drop += GpuUiFontMetrics.textWidth(f49, 9.0f * f6) + 10.0f * f6;
                if (!this.showSeparators || !(f11 - drop > 12.0f * f6)) continue;
                builder.rect(drop, Math.round(n7), f11 - drop, Math.max(1.0f, f6), 0.0f, this.lineColor);
            }
        }
        int n5 = -1;
        f11 = 0.0f;
        float f41 = 0.0f;
        float f42 = 0.0f;
        f10 = 0.0f;
        if (this.dragModule != null && this.catalogReorderable && !this.catalogGroups.isEmpty()) {
            int n9;
            int n8 = arrayList.size();
            f52 = new boolean[n8];
            for (n9 = 0; n9 < n8; ++n9) {
                f52[n9] = ((Module)arrayList.get(n9)).isFavorite();
            }
            n9 = arrayList.indexOf(this.dragModule);
            if (n9 >= 0) {
                CatalogSections.Drop drop = CatalogSections.drop(layout, n9, (boolean[])f52, this.dragModule.isFavorite(), this.dragPointerX, this.dragPointerY, f39, f40, f32, f15, bl9, f14);
                n5 = drop.index();
                this.dragDropIndex = drop.index();
                if (drop.index() != n9 && drop.index() != n9 + 1) {
                    float n11 = Math.max(2.0f, (float)Math.round(3.0f * f6));
                    var68_103 = drop.anchor();
                    f9 = f40 + layout.y(var68_103);
                    if (n3 == 1) {
                        f11 = f39;
                        f42 = f32;
                        f10 = n11;
                        f41 = drop.after() ? f9 + f15 + f14 * 0.5f - n11 * 0.5f : f9 - f14 * 0.5f - n11 * 0.5f;
                    } else {
                        f42 = n11;
                        f10 = f15;
                        f41 = f9;
                        f11 = f39 + (float)(layout.col[var68_103] + (drop.after() ? 1 : 0)) * (f32 + bl9) - bl9 * 0.5f - n11 * 0.5f;
                    }
                }
            } else {
                this.dragDropIndex = -1;
            }
        } else if (this.dragModule != null && this.catalogReorderable) {
            void var67_101;
            int f51 = arrayList.size();
            f52 = CatalogDrag.drop(this.dragPointerX, this.dragPointerY, f39, f40, f32, f15, bl9, f14, n3, f51);
            int f53 = 0;
            for (Module module : arrayList) {
                if (!module.isFavorite()) continue;
                ++f53;
            }
            int f54 = CatalogDrag.clampToGroup(((CatalogDrag.Drop)f52).index(), this.dragModule.isFavorite(), f53, f51);
            int n6 = ((CatalogDrag.Drop)f52).row();
            var68_103 = ((CatalogDrag.Drop)f52).column();
            if (f54 != ((CatalogDrag.Drop)f52).index()) {
                int n7 = n3 == 1 ? f54 : f54 / n3;
                int n8 = var68_103 = n3 == 1 ? 0 : f54 % n3;
                if (n3 > 1 && var68_103 == 0 && f54 > 0 && f54 == f51 && f51 % n3 == 0) {
                    --var67_101;
                    var68_103 = n3;
                }
            }
            int n9 = arrayList.indexOf(this.dragModule);
            n5 = f54;
            this.dragDropIndex = f54;
            if (f54 != n9 && f54 != n9 + 1) {
                float f43 = Math.max(2.0f, (float)Math.round(3.0f * f6));
                if (n3 == 1) {
                    f11 = f39;
                    f42 = f32;
                    f10 = f43;
                    f41 = f40 + (float)var67_101 * f33 - f14 * 0.5f - f43 * 0.5f;
                } else {
                    f42 = f43;
                    f10 = f15;
                    f11 = f39 + (float)var68_103 * (f32 + bl9) - bl9 * 0.5f - f43 * 0.5f;
                    f41 = f40 + (float)var67_101 * f33;
                }
            }
        } else {
            this.dragDropIndex = -1;
        }
        float f44 = 13.5f * f6;
        float f45 = 9.0f * f6;
        float f46 = 13.0f * f6;
        float f47 = 8.0f * f6;
        float f48 = f44 * 0.727f;
        float f49 = f45 * 0.727f;
        f9 = this.cardRadius * f6;
        int n10 = -1;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            int n11;
            float f50;
            float f51;
            float f53;
            float f54;
            float f55;
            float f56;
            int n12;
            CatalogEntry catalogEntry = list.get(i2);
            Module module = catalogEntry.module();
            int n13 = layout.col[i2];
            float f57 = f40 + layout.y(i2) + (n2 >= 0 && layout.row[i2] > n2 ? f36 : 0.0f);
            if (f36 <= 0.0f && (f57 + f15 + 14.0f * f6 < f30 || f57 - 14.0f * f6 > f16)) continue;
            if (n10 < 0) {
                n10 = layout.row[i2];
            }
            float f58 = f2 + f31 + (float)n13 * (f32 + bl9);
            float f59 = f32;
            boolean bl10 = this.hovered(f7, f8, f58, f57, f59, f15);
            boolean bl11 = this.selected == module;
            boolean bl12 = module.isEnabled();
            boolean bl13 = module.isOneShot();
            boolean bl14 = GpuClickGuiScreen.isBound(module);
            if (bl10 && !GpuClickGuiScreen.safeText(module.getDescription()).isBlank()) {
                this.hoveredTooltipTitle = module.getName();
                this.hoveredTooltipDesc = module.getDescription();
            }
            float f60 = this.spring(this.animKey(module, 0, GpuClickGuiScreen.bf("\ue36d\uf3bd\ue9cf\uda50\uf152\u720e\ua661\uabbd", -159578765, 0x1B1EF9FC ^ CryptoVault.mask())), bl10 ? 1.0f : 0.0f, 340.0f, 22.0f);
            float f61 = this.anim(this.animKey(module, 1, GpuClickGuiScreen.bf("\uf1ad\ua7b2\u35f8\u4eb5\u5b5f\u39e0\u5e26\uc406\u55cb", -240043252, 0x1B1EF9FC ^ CryptoVault.mask())), bl12 ? 1.0f : 0.0f, 12.0f);
            float f62 = this.anim(this.animKey(module, 2, GpuClickGuiScreen.bf("\ue693\uc2f7\u08e2\uca54\ucd0c\u2dca\uf4b5\ufffa\uf2d3\ub2d4", -665538009, 0x1B1EF9FC ^ CryptoVault.mask())), this.pendingCardPress != null && this.pendingCardPress.module == module ? 1.0f : 0.0f, 32.0f);
            float f63 = (float)Math.min(Math.max(0, layout.row[i2] - n10), 8) * 0.035f + (float)n13 * 0.012f;
            float f64 = Math.max(0.0f, Math.min(1.0f, (f12 - f63) / 0.32f));
            float f65 = 1.0f - f64;
            f64 = 1.0f - f65 * f65 * f65;
            f57 = Math.round(f57 - f60 * this.hoverK * 2.5f * f6 + (1.0f - f64) * 12.0f * f6 + f62 * 1.6f * f6);
            this.tileActiveOverride = this.enabledHl;
            this.glassTile(builder, f58, f57, f59, f15, f9, f6, Math.max(0.0f, Math.min(1.0f, f60)), f61 * this.enabledK);
            this.tileActiveOverride = 0;
            float f66 = Math.max(0.0f, Math.min(1.0f, f60)) * f64;
            int n14 = this.accentArgb() | 0xFF000000;
            int n15 = this.accent2 | 0xFF000000;
            int n16 = this.enabledHl != 0 ? this.enabledHl | 0xFF000000 : n14;
            int n17 = this.enabledHl != 0 ? n16 : n15;
            int n18 = n12 = this.hoverHl != 0 ? this.hoverHl | 0xFF000000 : n14;
            if (f61 > 0.01f) {
                f56 = this.reducedMotion() ? 0.5f : 0.5f + 0.5f * (float)Math.sin((double)this.nanoTime() / 1.6E9 + (double)i2 * 0.7);
                f55 = f61 * f64 * this.enabledK;
                builder.gradient(f58, f57, f59, f15, f9, GpuClickGuiScreen.scaleAlpha(n16, f55 * (0.26f + 0.05f * f56)), GpuClickGuiScreen.scaleAlpha(n17, f55 * 0.08f), GpuClickGuiScreen.scaleAlpha(n16, f55 * 0.12f), GpuClickGuiScreen.scaleAlpha(n17, f55 * 0.04f));
                builder.border(f58, f57, f59, f15, f9, Math.max(1.0f, f6), GpuClickGuiScreen.scaleAlpha(n16, f55 * 0.85f));
            }
            if (f66 > 0.01f && this.hoverGlowK > 0.0f) {
                builder.border(f58, f57, f59, f15, f9, Math.max(1.0f, f6), GpuClickGuiScreen.scaleAlpha(GpuClickGuiScreen.mixRgb(-1, n12, 0.5f) | 0xFF000000, f66 * 0.3f * this.hoverGlowK));
                builder.shadow(f58, f57 + 4.0f * f6, f59, f15, f9, 14.0f * f6, 0.0f, GpuClickGuiScreen.scaleAlpha(n12, f66 * 0.14f * this.hoverGlowK));
            }
            builder.rect(f58 + f9, f57 + 0.5f * f6, Math.max(0.0f, f59 - f9 * 2.0f), Math.max(1.0f, f6), 0.5f, GpuClickGuiScreen.scaleAlpha(-1, (0.07f + 0.06f * f66) * f64));
            if (f62 > 0.02f) {
                builder.rect(f58, f57, f59, f15, f9, GpuClickGuiScreen.argb(48.0f * f62, 0));
            }
            if (bl11) {
                builder.border(f58, f57, f59, f15, f9, Math.max(1.2f, 1.3f * f6), GpuClickGuiScreen.argb(160.0f, this.selectionRing));
            }
            if (this.navVisible && i2 == this.navIndex) {
                f66 = Math.round(3.0f * f6);
                builder.border(f58 - f66, f57 - f66, f59 + f66 * 2.0f, f15 + f66 * 2.0f, f9 + f66, Math.max(1.5f, 1.6f * f6), GpuClickGuiScreen.argb(208.0f, this.selectionRing));
            }
            f66 = f58 + 16.0f * f6;
            if (f61 > 0.01f) {
                float f67 = (f15 - 16.0f * f6) * f61;
                this.accentFill(builder, f58 + 6.0f * f6, f57 + f15 * 0.5f - f67 * 0.5f, 3.0f * f6, f67, 1.5f * f6, f61 * this.enabledK, this.enabledHl != 0 ? this.enabledHl | 0xFF000000 : 0);
            }
            float f68 = 34.0f * f6;
            float f69 = 18.0f * f6;
            float f70 = f58 + f59 - f68 - 8.0f * f6;
            float f71 = f57 + (f15 - f69) * 0.5f;
            String string10 = bl14 && !bl13 ? GpuClickGuiScreen.keyLabel(module.getKeyCode()) : null;
            float f72 = f56 = string10 == null ? 0.0f : (float)Math.round(Math.max(22.0f * f6, Math.min(70.0f * f6, GpuUiFontMetrics.textWidth(string10, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 12.0f * f6)));
            f55 = bl13 ? 92.0f * f6 : 54.0f * f6 + (string10 == null ? 0.0f : f56 + 6.0f * f6);
            float f73 = f58 + f59 - f55 + 4.0f * f6;
            float f74 = Math.max(12.0f * f6, f59 - f55 - 16.0f * f6);
            String string11 = catalogEntry.note();
            List<String> list3 = GpuClickGuiScreen.wrapText(GpuClickGuiScreen.safeText(module.getDescription()), f74, f45, string11 == null ? 1 : 0);
            float f75 = f48 + f47 + f49;
            float f76 = f57 + (f15 - f75) * 0.5f;
            float f77 = f76 + f48 * 0.5f;
            String string12 = UiKit.fit(module.getName(), Math.max(0.0f, f73 - f66), f44, UiDrawCommand.TextWeight.SEMIBOLD);
            if (bl3) {
                this.highlightMatch(builder, module.getName(), string12, string22, f66, f77, f44, UiDrawCommand.TextWeight.SEMIBOLD, f6);
            }
            GpuClickGuiScreen.textMid(builder, f66, f77, f44, bl12 ? GpuClickGuiScreen.mixRgb(this.titleColor, this.accentArgb(), 0.12f * f61) : GpuClickGuiScreen.mixRgb(this.textColor, this.titleColor, 0.6f * f60), string12, UiDrawCommand.TextWeight.SEMIBOLD);
            float f78 = f66 + GpuUiFontMetrics.textWidth(string12, f44, UiDrawCommand.TextWeight.SEMIBOLD) + 7.0f * f6;
            float f79 = Math.round(14.0f * f6);
            float f80 = Math.round(f77 - f79 * 0.5f);
            if (bl13) {
                f78 = this.drawCardTag(builder, GpuClickGuiScreen.bf("\u07c6\u3061\u1dd8\u0417\u21ab\u2a98\uce25\ue6ad\u55f9\uabab\udf79", -1400264262, 0x1B1EF9FC ^ CryptoVault.mask()), f78, f80, f79, f73, f6, false);
            }
            if (module.isExtendedFlagged()) {
                f78 = this.drawCardTag(builder, GpuClickGuiScreen.bf("\uc192\u8986\u2007\ue147\ue21d\u09c3\u63f3\ua9e4", -845327067, 0x1B1EF9FC ^ CryptoVault.mask()), f78, f80, f79, f73, f6, true);
            }
            if ((bl5 || customCategory != null) && module.getCategory() != null && f78 + (f54 = GpuUiFontMetrics.textWidth(module.getCategory().zw, 8.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 12.0f * f6) <= f73) {
                builder.rect(f78, f80, f54, f79, f79 * 0.5f * Math.min(1.0f, this.cornerScale), this.controlFill(22));
                GpuClickGuiScreen.textCentered(builder, f78 + f54 * 0.5f, f80 + f79 * 0.5f, 8.0f * f6, this.mutedColor, module.getCategory().zw, UiDrawCommand.TextWeight.SEMIBOLD);
            }
            f54 = f76 + f48 + f47 + f49 * 0.5f;
            int n19 = 0;
            if (string11 != null) {
                String string13 = UiKit.fit(string11, f74, f45);
                if (catalogEntry.noteMatch() >= 0) {
                    this.highlightRange(builder, string11, string13, catalogEntry.noteMatch(), catalogEntry.noteMatch() + string22.length(), f66, f54, f45, UiDrawCommand.TextWeight.REGULAR, f6);
                }
                GpuClickGuiScreen.textMid(builder, f66, f54, f45, GpuClickGuiScreen.withA(this.accentArgb(), 230), string13, UiDrawCommand.TextWeight.REGULAR);
                n19 = 1;
            }
            for (String string14 : list3) {
                GpuClickGuiScreen.textMid(builder, f66, f54 + (float)n19 * f46, f45, GpuClickGuiScreen.mixRgb(this.mutedColor, this.textColor, 0.3f * Math.max(0.0f, Math.min(1.0f, f60))), string14, UiDrawCommand.TextWeight.REGULAR);
                ++n19;
            }
            this.hit(f58, f57, Math.max(0.0f, f59 - f55), f15, new CardPress(module, () -> {
                this.navVisible = false;
                if (module.isOneShot() || module.isLocked()) {
                    this.selectModule(module);
                } else {
                    module.toggle();
                }
            }));
            this.hitModuleCardRight(f58, f57, f59, f15, () -> {
                if (this.secondaryIsMiddle) {
                    this.openModuleMenu(module);
                } else {
                    this.toggleModuleInspector(module);
                }
            });
            if (!bl13) {
                this.drawToggleSwitch(builder, GpuClickGuiScreen.bf("\u0bf3\u7217\ub0c2\u2137\u967b\u9528", 576002113, 0x1B1EF9FC ^ CryptoVault.mask()) + module.getName(), f70, f71, f68, f69, bl12, f6);
                if (!module.isLocked()) {
                    this.hit(f70 - 4.0f * f6, f57, f68 + 8.0f * f6, f15, module::toggle);
                }
                if (string10 != null) {
                    float f81 = Math.round(18.0f * f6);
                    float f82 = Math.round(f70 - 6.0f * f6 - f56);
                    f53 = Math.round(f57 + (f15 - f81) * 0.5f);
                    boolean bl15 = this.hovered(f7, f8, f82, f53, f56, f81);
                    builder.rect(f82, f53, f56, f81, f81 * 0.5f * Math.min(1.0f, this.cornerScale), GpuClickGuiScreen.withA(this.accentArgb(), bl15 ? 58 : 34));
                    GpuClickGuiScreen.textCentered(builder, f82 + f56 * 0.5f, f53 + f81 * 0.5f, 8.5f * f6, bl15 ? this.titleColor : this.textColor, UiKit.fit(string10, f56 - 8.0f * f6, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
                    if (bl15) {
                        this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\u0549\u3a71\ude6e\ua77f\ue13c\u67df\u2be2\u4dc0\ue475\u3941\u5a04\u0a3d", 1575135677, 0x1B1EF9FC ^ CryptoVault.mask()) + module.getKeyDisplayName();
                        this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\u3d88\uad55\u5830\ub6f7\u2832\uae5a\u93e4\u7c9d\u630a\ud91b\u115f\u99be\u218e\uda35\ue97c\u75b0\u0392\u3878\u5d61\u0397\u7582\u8b7c\uca3e\uf558\uf348\u8612\ub3b1\uf697\ued40\u37bb\uf3e2\u5b47\u2de8\u087b\ue6ed\uca1f\u50b9\u302e\u0759\u6dc5\ub954\u08f7\u09aa\u08cd", 207104025, 0x1B1EF9FC ^ CryptoVault.mask());
                    }
                    this.hit(f82 - 2.0f * f6, f57, f56 + 4.0f * f6, f15, () -> this.beginModuleKeyCapture(module));
                }
            } else {
                String string14;
                boolean bl16 = this.bindingModuleKey && this.bindingModuleTarget == module;
                string14 = bl16 ? GpuClickGuiScreen.bf("\u9a04\uce01\ud93e\ubba2\uefbf\u51f7\ufb8d\u68e7\u687a\uf998\u831c\u2aae\u02e5", -107095273, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.keyLabel(module.getKeyCode());
                f53 = Math.max(38.0f * f6, Math.min(76.0f * f6, GpuUiFontMetrics.textWidth(string14, 9.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 14.0f * f6));
                f51 = Math.round(f58 + f59 - f53 - 8.0f * f6);
                float f83 = Math.round(22.0f * f6);
                f50 = Math.round(f57 + (f15 - f83) * 0.5f);
                n11 = this.hovered(f7, f8, f51 - 4.0f * f6, f57, f53 + 8.0f * f6, f15);
                float f84 = f83 * 0.5f * Math.min(1.0f, this.cornerScale);
                builder.rect(f51, f50, f53, f83, f84, bl14 || bl16 ? GpuClickGuiScreen.withA(this.accentArgb(), n11 != 0 ? 58 : 38) : this.controlFill(n11 != 0 ? 42 : 26));
                builder.border(f51, f50, f53, f83, f84, Math.max(1.0f, f6 * 0.8f), bl16 ? GpuClickGuiScreen.withA(this.accentArgb(), 192) : (bl14 ? GpuClickGuiScreen.withA(this.accentArgb(), 80) : this.lineBorderColor));
                GpuClickGuiScreen.textCentered(builder, f51 + f53 * 0.5f, f50 + f83 * 0.5f, 9.0f * f6, bl16 ? this.accentArgb() : (bl14 || n11 != 0 ? this.textColor : this.mutedColor), UiKit.fit(string14, f53 - 12.0f * f6, 9.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
                if (n11 != 0) {
                    this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\ud98f\ub44b\u2304\ua063\uaad1\ued64\uc667\uc569\u4ccc\ufae2\u0670\ufcba\uc089\u25a0\u0d36", 1387373816, 0x1B1EF9FC ^ CryptoVault.mask()) + module.getKeyDisplayName();
                    this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\ud112\ub83f\u60b3\ue3c9\u8501\u7170\u18df\u72eb\u8603\ubd4b\ufdff\u30d3\uf8fe\u8975\uf460\u24a1\ua76d\u4d89\u4a8e\udbe5\ufdb1\u7f49\uf199\u862a\u62b6\u5151\uda68\ubdf9\ua270\u46da\u053a\u8b38\u8f40\u2bd5\u60e0\uea75\u05d5\ua1e9\u9fbb\u457c\u67e4\ubdb6\ud27c\u1f44\u86fc\ub12c\uf022\ue4ca\u830e\u09df\uaa80\ub6d1\uf1c5\udb25\ue319\u0227\u1b0e\uff35\u7be1\uacb5\u4ccf\uf7e7\u12cf", -1076129079, 0x1B1EF9FC ^ CryptoVault.mask());
                }
                this.hit(f51 - 4.0f * f6, f57, f53 + 8.0f * f6, f15, () -> this.beginModuleKeyCapture(module));
                this.hitModuleCardRight(f51 - 4.0f * f6, f57, f53 + 8.0f * f6, f15, () -> {
                    if (this.bindingModuleTarget == module) {
                        this.cancelKeybindCapture();
                    }
                    module.setKeyCode(-1);
                });
            }
            boolean bl17 = module.isFavorite();
            float f85 = Math.round(5.5f * f6);
            f53 = f58 + f59 - 14.0f * f6;
            f51 = f57 + 11.0f * f6;
            boolean bl18 = this.hovered(f7, f8, f53 - 9.0f * f6, f51 - 8.0f * f6, 18.0f * f6, 16.0f * f6);
            f50 = this.anim(this.animKey(module, 3, GpuClickGuiScreen.bf("\u4fd2\uc937\u2945\ue8c1\ude7a\u0308\u8a3e\ude2f\uabaa\u60bd\u6992", 785437480, 0x1B1EF9FC ^ CryptoVault.mask())), bl17 || bl10 ? 1.0f : 0.0f, 16.0f);
            if (f50 > 0.01f) {
                n11 = bl17 ? this.accentArgb() : (bl18 ? this.textColor : this.mutedColor);
                GpuClickGuiScreen.drawStar(builder, f53, f51, f85, GpuClickGuiScreen.scaleAlpha(n11 | 0xFF000000, f50 * (bl17 || bl18 ? 1.0f : 0.7f)));
            }
            if (bl17 || bl10) {
                this.hit(f53 - 9.0f * f6, f51 - 8.0f * f6, 18.0f * f6, 16.0f * f6, () -> {
                    module.setFavorite(!module.isFavorite());
                    this.catalogCacheKey = "";
                });
                if (bl18) {
                    this.hoveredTooltipTitle = bl17 ? GpuClickGuiScreen.bf("\u1e8f\u62a3\ucfc7\uf271\u9067\ua073\u1bb7\u636f\u4d08\ue2aa\u1ea0\u3267\ua516", -693479011, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\ua096\u1f6f\u8354\uf7da\u7628\u224a\u2d30\u9b00\u8847", -1888704358, 0x1B1EF9FC ^ CryptoVault.mask());
                    String string15 = this.hoveredTooltipDesc = bl17 ? GpuClickGuiScreen.bf("\ub19c\u5219\u1848\uda34\ucb11\u0175\u3c6e\u9113\u48bb\u6e9b\u823a\uc76c\u648c\u7c39\u9f1e\uefea\ub2e4\uf14d\u6bbf\u55a6\ucbe4\uf4f5\u0099\u4375\u4bff\ue52d\u9d3d\u0561\u1d91\u9114\ua804\u9f87\u2e26\u290c", 197558626, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u9e40\u1ebf\u378a\ue19e\u59e7\u60c4\ude05\u7404\ud874\ub6b8\ue6aa\u4b43\ude6f\ub2e4\u86e8\u4be5\u07ff\u57a7\u32e9\u4c03\ucbe8\uf34d\ub0c5\u8f47\u884f\u1c11\ubd12\ud326\u289b\u9f05\ucc9c\u9137", -1380343502, 0x1B1EF9FC ^ CryptoVault.mask());
                }
            }
            if (f64 < 0.999f) {
                builder.rect(f58, f57, f59, f15, f9, GpuClickGuiScreen.argb(160.0f * (1.0f - f64), 724758));
            }
            if (module != this.dragModule) continue;
            builder.rect(f58, f57, f59, f15, f9, GpuClickGuiScreen.argb(144.0f, 724758));
        }
        if (n5 >= 0 && f42 > 0.0f) {
            builder.rect(f11, f41, f42, f10, Math.min(f42, f10) * 0.5f, this.accentArgb());
        }
        if (f36 > 0.0f && n2 >= 0 && this.selected != null) {
            float f86 = f40 + layout.y(n4) + f15 + 3.0f * f6;
            this.drawInlineBlock(builder, this.selected, f39 + (float)(n4 % n3) * (f32 + bl9), f86, f32, f36, f6, f7, f8, f30, f16);
        }
        this.clippingHits = false;
        builder.clipPop();
        this.catalogScroll.paintBar(builder, f2 + f4 - (float)Math.round(8.0f * f6), f30, f34, f6, this.textColor, this.nanoTime());
    }

    protected List<QuickPin> quickPins() {
        List<GuiPrefs.Pin> list = GpuClickGuiScreen.guiPrefs().pins();
        if (list.isEmpty()) {
            return List.of();
        }
        ModuleManager moduleManager = this.moduleManager();
        if (moduleManager == null) {
            return List.of();
        }
        HashMap<String, Module> hashMap = new HashMap<String, Module>();
        for (Module object : moduleManager.getModules()) {
            if (object == null) continue;
            hashMap.putIfAbsent(object.getName(), object);
        }
        ArrayList arrayList = new ArrayList();
        for (GuiPrefs.Pin pin : list) {
            ClassicSettingLayout.Row row;
            Module module = (Module)hashMap.get(pin.module());
            Setting setting = module == null ? null : module.getSetting(pin.setting());
            if (setting == null || !setting.isVisible() || !GpuClickGuiScreen.pinnable(setting) || (row = ClassicSettingLayout.row(module, setting)) != null && !row.visible() || module instanceof HitboxConfigScreen752809 && !HitboxConfigScreen752809.a(setting)) continue;
            arrayList.add(new QuickPin(module, setting));
        }
        return arrayList;
    }

    protected static boolean pinnable(Setting setting) {
        return setting != null && !(setting instanceof HitboxKeyBinding655023) && !(setting instanceof ConfigKeybindListSetting);
    }

    private float quickStripHeight(List<QuickPin> list, int n2, float f2) {
        if (list.isEmpty()) {
            return 0.0f;
        }
        float f3 = Math.round(26.0f * f2);
        if (quickCollapsed) {
            return f3 + (float)Math.round(6.0f * f2);
        }
        float f4 = Math.round(10.0f * f2);
        for (int i2 = 0; i2 < list.size(); i2 += n2) {
            int n3 = 0;
            for (int i3 = i2; i3 < Math.min(list.size(), i2 + n2); ++i3) {
                n3 = Math.max(n3, this.settingHeight(list.get(i3).setting()));
            }
            f3 += (float)Math.round((float)n3 * f2) + f4;
        }
        return f3 + (float)Math.round(8.0f * f2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void drawQuickStrip(UiDisplayList.Builder builder, List<QuickPin> list, float f2, float f3, float f4, int n2, float f5, float f6, float f7, float f8, float f9, float f10) {
        float f11 = Math.round(26.0f * f8);
        float f12 = f3 + f11 * 0.5f - 2.0f * f8;
        String string = GpuClickGuiScreen.bf("\u77c2\u946c\ub558\u04cb\ud95d\u5f0a\u07c2\u0545\udf7f\ub26f\u475b\u7921\u30fe\u1a70\u8fc7\u9fab", -754637079, 0x1B1EF9FC ^ CryptoVault.mask());
        GpuClickGuiScreen.textMid(builder, f2 + 4.0f * f8, f12, 9.0f * f8, this.mutedColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
        float f13 = f2 + 4.0f * f8 + GpuUiFontMetrics.textWidth(string, 9.0f * f8, UiDrawCommand.TextWeight.SEMIBOLD) + 8.0f * f8;
        GpuClickGuiScreen.textMid(builder, f13, f12, 9.0f * f8, this.muted2Color, String.valueOf(list.size()), UiDrawCommand.TextWeight.REGULAR);
        String string2 = quickCollapsed ? GpuClickGuiScreen.bf("\u4315\u04f0\udcc7\u0da2\u2f70", 1853377930, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u5999\u34d7\uea87\u1e71\u5f52\ucd54\u99f3\ua6e2", -1566930032, 0x1B1EF9FC ^ CryptoVault.mask());
        float f14 = GpuUiFontMetrics.textWidth(string2, 9.0f * f8, UiDrawCommand.TextWeight.SEMIBOLD) + 14.0f * f8;
        float f15 = Math.round(18.0f * f8);
        float f16 = Math.round(f2 + f4 - f14);
        float f17 = Math.round(f12 - f15 * 0.5f);
        boolean bl = this.hovered(f9, f10, f16, f17, f14, f15);
        if (bl) {
            builder.rect(f16, f17, f14, f15, this.pillR(f15), this.controlFill(30));
        }
        GpuClickGuiScreen.textCentered(builder, f16 + f14 * 0.5f, f17 + f15 * 0.5f, 9.0f * f8, bl ? this.textColor : this.mutedColor, string2, UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f16, f17, f14, f15, () -> {
            quickCollapsed = !quickCollapsed;
            GpuClickGuiScreen.guiPrefs().setQuickCollapsed(quickCollapsed);
        });
        if (this.hovered(f9, f10, f2, f3, f16 - f2, f11)) {
            this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\ue213\u1f5a\u775c\ua62f\u59cf\uec7f\uaab8\uadf0\u4b67\uae7d\u37d7\u0baa\u3fff\ud5ce\u94c8\ua769\udd40", 292044120, 0x1B1EF9FC ^ CryptoVault.mask());
            this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\ue4a3\u4721\uc145\ubc96\ua5df\u6839\u889e\u33b7\uf1f0\u74c2\ucfc3\u1c47\u8c7e\u504a\u41b5\u6b9e\u59ec\ufb40\u77d0\u0d3d\ueaa8\u7e07\u4c0c\uad0f\u1a6d\ucf5f\u6ea4\u4ef3\ufe2a\ua635\ud325\u6b65\u4557\uace0\u80f3\u4397\u6485\uc539\uaa7c\ub249\ubfd3\u8de6\u2e40\u5deb\u093d\u2728\u21a0\u472e\u04f0\udd84\u95b2\u72d3\ua7f5\ue4a3\u2e5c\u7ac4\u5c4c\u7da7\u032a\u12e4\u45b8\u1dcb\uab93\u1e1e\u26d6\u886a\u47c9\u34c0\ub614\u6d0c\ub850\u7d46\u5e47\ue988\u18c2\u026a\u24a6\ue940\u0146\ua172\u05ce\ue86d\uc382\u0d82\uee0a\uc537\u9c16\u3e37\u92e6\ufd9a\u1ed0\ub24c\u6d42\ucd0c\ue344\u82ef\ufaa7\ufe6f", 1828500728, 0x1B1EF9FC ^ CryptoVault.mask());
        }
        if (quickCollapsed) {
            return;
        }
        float f18 = f3 + f11;
        for (int i2 = 0; i2 < list.size(); i2 += n2) {
            int n3 = 0;
            for (int i3 = i2; i3 < Math.min(list.size(), i2 + n2); ++i3) {
                n3 = Math.max(n3, this.settingHeight(list.get(i3).setting()));
            }
            float f19 = Math.round((float)n3 * f8);
            boolean bl2 = f18 + f19 >= this.clipY && f18 <= this.clipY + this.clipH;
            for (int i4 = i2; bl2 && i4 < Math.min(list.size(), i2 + n2); ++i4) {
                QuickPin quickPin = list.get(i4);
                float f20 = f2 + (float)(i4 - i2) * (f5 + f6);
                float f21 = Math.round((float)this.settingHeight(quickPin.setting()) * f8);
                this.surfaceCard(builder, f20, f18, f5, f21, this.cardRadius * f8);
                this.rowLabelPrefix = quickPin.module().getName() + GpuClickGuiScreen.bf("\u683d\ucdf5\u782e\u4f8b\ud0c7\udca0", 218784175, 0x1B1EF9FC ^ CryptoVault.mask());
                boolean bl3 = this.showSeparators;
                this.showSeparators = false;
                try {
                    this.drawSettingRow(builder, quickPin.setting(), f20, f18, f5, f21, f8, f9, f10);
                    continue;
                }
                finally {
                    this.showSeparators = bl3;
                    this.rowLabelPrefix = null;
                }
            }
            f18 += f19 + f7;
        }
    }

    private float drawCardTag(UiDisplayList.Builder builder, String string, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        float f7 = GpuUiFontMetrics.textWidth(string, 8.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 12.0f * f6;
        if (f2 + f7 > f5) {
            return f2;
        }
        float f8 = f4 * 0.5f * Math.min(1.0f, this.cornerScale);
        builder.rect(f2, f3, f7, f4, f8, this.accentArgb() & 0xFFFFFF | 0x1D000000);
        if (bl) {
            builder.border(f2, f3, f7, f4, f8, Math.max(1.0f, 0.8f * f6), this.accentArgb() & 0xFFFFFF | Integer.MIN_VALUE);
        }
        GpuClickGuiScreen.textCentered(builder, f2 + f7 * 0.5f, f3 + f4 * 0.5f, 8.0f * f6, this.accentArgb(), string, UiDrawCommand.TextWeight.SEMIBOLD);
        return f2 + f7 + 5.0f * f6;
    }

    private void highlightMatch(UiDisplayList.Builder builder, String string, String string2, String string3, float f2, float f3, float f4, UiDrawCommand.TextWeight textWeight, float f5) {
        if (string3.isEmpty()) {
            return;
        }
        int n2 = string.toLowerCase(Locale.ROOT).indexOf(string3);
        if (n2 >= 0) {
            this.highlightRange(builder, string, string2, n2, n2 + string3.length(), f2, f3, f4, textWeight, f5);
        }
    }

    private void highlightRange(UiDisplayList.Builder builder, String string, String string2, int n2, int n3, float f2, float f3, float f4, UiDrawCommand.TextWeight textWeight, float f5) {
        boolean bl = !string2.equals(string) && string2.endsWith("\u2026");
        int n4 = Math.min(string.length(), bl ? string2.length() - 1 : string2.length());
        if (n2 < 0 || n2 >= n4) {
            return;
        }
        int n5 = Math.min(n3, n4);
        float f6 = f2 + GpuUiFontMetrics.textWidth(string.substring(0, n2), f4, textWeight);
        float f7 = GpuUiFontMetrics.textWidth(string.substring(n2, n5), f4, textWeight);
        float f8 = Math.round(f4 * 1.25f);
        builder.rect(f6 - 1.0f * f5, Math.round(f3 - f8 * 0.5f), f7 + 2.0f * f5, f8, Math.max(1.0f, 3.0f * f5), GpuClickGuiScreen.withA(this.accentArgb(), 72));
    }

    protected static void drawStar(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2) {
        float f5 = f3 - f4;
        float f6 = f3 + f4 * 0.81f;
        int n3 = Math.max(4, Math.round(f6 - f5));
        float f7 = (f6 - f5) / (float)n3;
        float[] fArray = new float[10];
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4;
            float f8 = f5 + ((float)i2 + 0.5f) * f7;
            int n5 = 0;
            for (n4 = 0; n4 < 10; ++n4) {
                int n6 = (n4 + 1) % 10;
                float f9 = f3 + STAR_Y[n4] * f4;
                float f10 = f3 + STAR_Y[n6] * f4;
                if (!(f9 <= f8 && f8 < f10) && (!(f10 <= f8) || !(f8 < f9))) continue;
                float f11 = f2 + STAR_X[n4] * f4;
                float f12 = f2 + STAR_X[n6] * f4;
                fArray[n5++] = f11 + (f8 - f9) * (f12 - f11) / (f10 - f9);
            }
            Arrays.sort(fArray, 0, n5);
            n4 = 0;
            while (n4 + 1 < n5) {
                builder.rect(fArray[n4], f5 + (float)i2 * f7, fArray[n4 + 1] - fArray[n4], f7 * 1.02f, 0.0f, n2);
                n4 += 2;
            }
        }
    }

    protected static boolean isBound(Module module) {
        int n2 = module.getKeyCode();
        return n2 != -1 && n2 != 0;
    }

    protected List<Module> allModules() {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        Set set = Collections.newSetFromMap(new IdentityHashMap());
        for (Category category : Category.values()) {
            for (Module module : this.modulesForCategory(category)) {
                if (module instanceof HitboxConfigScreen752809 || !set.add(module)) continue;
                arrayList.add(module);
            }
        }
        return arrayList;
    }

    protected boolean passesFilter(Module module) {
        return switch (moduleFilter.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> true;
            case 1 -> module.isEnabled();
            case 2 -> module.isFavorite();
            case 3 -> GpuClickGuiScreen.isBound(module);
        };
    }

    protected CatalogEntry searchEntry(Module module, String string) {
        if (string.isEmpty()) {
            return new CatalogEntry(module, 0, null, -1);
        }
        String string2 = module.getName().toLowerCase(Locale.ROOT);
        if (string2.startsWith(string)) {
            return new CatalogEntry(module, 0, null, -1);
        }
        if (string2.contains(string)) {
            return new CatalogEntry(module, 1, null, -1);
        }
        if (module.getCategory() != null && module.getCategory().zw.toLowerCase(Locale.ROOT).contains(string)) {
            return new CatalogEntry(module, 2, null, -1);
        }
        if (GpuClickGuiScreen.isBound(module) && (Module.getSingleKeyName(module.getKeyCode()).equalsIgnoreCase(string) || module.getKeyDisplayName().equalsIgnoreCase(string))) {
            return new CatalogEntry(module, 3, GpuClickGuiScreen.bf("\ucb37\u721d\ud2b1\u9d6c\u5945\u7a97\u9b06\ud561\u4ca0\u6aed\ue20d\u5e24\u4ed8", -1393926324, 0x1B1EF9FC ^ CryptoVault.mask()) + module.getKeyDisplayName(), GpuClickGuiScreen.bf("\u784f\uf813\u58ca\udd55\ufd4b\u2dc5\u1139\u63eb\u22ce\u89ed\u44f2\u1bd6\u3d90", 254105695, 0x1B1EF9FC ^ CryptoVault.mask()).length());
        }
        for (Setting setting : module.getSettings()) {
            if (setting == null || setting instanceof HitboxKeyBinding655023 || setting.getName() == null) continue;
            int n2 = setting.getName().toLowerCase(Locale.ROOT).indexOf(string);
            if (n2 >= 0) {
                return new CatalogEntry(module, 4, GpuClickGuiScreen.bf("\u3001\u4824\u1308\u546b\u1bb7\ucebd\ub09a\u4e9a\ub6fb\ude5b\udc27\ue8c2", -429888590, 0x1B1EF9FC ^ CryptoVault.mask()) + setting.getName(), GpuClickGuiScreen.bf("\ub906\u4f7d\u607d\ubc7b\uf979\u39bd\ua911\ucaf8\uce32\uea81\ueb79\ub9a6", 369566382, 0x1B1EF9FC ^ CryptoVault.mask()).length() + n2);
            }
            if (!(setting instanceof MultiSelectSetting)) continue;
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            for (String string3 : multiSelectSetting.E()) {
                int n3 = string3.toLowerCase(Locale.ROOT).indexOf(string);
                if (n3 < 0) continue;
                return new CatalogEntry(module, 4, GpuClickGuiScreen.bf("\u4457\uc1f0\u7ca4\ua863\u7c00\u3b73\ud2f7\u0c13\uce79\ua3ba\u9a3b\uda55\ubb48", 2078710399, 0x1B1EF9FC ^ CryptoVault.mask()) + setting.getName() + GpuClickGuiScreen.bf("\u3a91\u1419\uda66\u5ecd", 592950981, 0x1B1EF9FC ^ CryptoVault.mask()) + string3, GpuClickGuiScreen.bf("\u0b27\u355a\u4b68\ud06f\u1c00\u246d\u2f73\u5bec\u2f1f\u5806\u8736\u54d6\u9ac8", 1577824663, 0x1B1EF9FC ^ CryptoVault.mask()).length() + setting.getName().length() + 3 + n3);
            }
        }
        if (GpuClickGuiScreen.safeText(module.getDescription()).toLowerCase(Locale.ROOT).contains(string)) {
            return new CatalogEntry(module, 5, null, -1);
        }
        return null;
    }

    private List<CatalogEntry> catalog(String string, boolean bl) {
        List<CatalogSections.Group> list;
        List<Module> list2 = bl ? this.allModules() : (this.folderId != null ? this.modulesForFolder(this.folderId) : this.modulesForCategory(this.category));
        long l2 = list2.size();
        for (Module object22 : list2) {
            l2 = l2 * 31L + (long)(object22.isEnabled() ? 1 : 0) + (long)(object22.isFavorite() ? 2 : 0) + 4L * (long)object22.getKeyCode();
        }
        String string2 = string + '\u0001' + bl + '\u0001' + (Object)((Object)this.category) + '\u0001' + this.folderId + '\u0001' + GpuClickGuiScreen.guiPrefs().revision() + '\u0001' + (Object)((Object)moduleFilter) + '\u0001' + (Object)((Object)moduleSort);
        if (string2.equals(this.catalogCacheKey) && l2 == this.catalogCacheState) {
            return this.catalogEntries;
        }
        String string3 = string.toLowerCase(Locale.ROOT);
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (Module module : list2) {
            if (module instanceof HitboxConfigScreen752809 || module.getCategory() == Category.vR || !this.passesFilter(module) || (list = this.searchEntry(module, string3)) == null) continue;
            arrayList.add(list);
        }
        Comparator<CatalogEntry> comparator = Comparator.comparingInt(catalogEntry -> catalogEntry.module().isFavorite() ? 0 : 1);
        Comparator<CatalogEntry> comparator2 = switch (moduleSort.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> comparator.thenComparingInt(CatalogEntry::rank);
            case 1 -> comparator.thenComparing(catalogEntry -> catalogEntry.module().getName(), String.CASE_INSENSITIVE_ORDER);
            case 2 -> comparator.thenComparingInt(catalogEntry -> catalogEntry.module().isEnabled() ? 0 : 1).thenComparingInt(CatalogEntry::rank);
        };
        arrayList.sort(comparator2);
        list = List.of();
        if (!bl && this.folderId == null && string3.isEmpty()) {
            CatalogSections.Arranged<CatalogEntry> arranged = CatalogSections.arrange(arrayList, catalogEntry -> catalogEntry.module().getSubCategory());
            arrayList = new ArrayList<CatalogEntry>(arranged.items());
            list = arranged.groups();
        }
        this.catalogGroups = list;
        this.catalogEntries = List.copyOf(arrayList);
        this.catalogCacheKey = string2;
        this.catalogCacheState = l2;
        return this.catalogEntries;
    }

    private void onSearchChanged() {
        this.catalogScroll.reset();
        this.catalogCacheKey = "";
        this.navIndex = this.searchField.isEmpty() ? -1 : 0;
        boolean bl = this.navVisible = !this.searchField.isEmpty();
        if (this.view != View.MODULES) {
            this.view = View.MODULES;
        }
    }

    private void activateNav(boolean bl) {
        int n2;
        List<CatalogEntry> list = this.catalogEntries;
        if (this.view != View.MODULES || list.isEmpty()) {
            return;
        }
        this.navIndex = n2 = this.navIndex >= 0 && this.navIndex < list.size() ? this.navIndex : 0;
        this.navVisible = true;
        Module module = list.get(n2).module();
        if (bl || module.isOneShot() || module.isLocked()) {
            this.selectModule(module);
        } else {
            module.toggle();
        }
    }

    private boolean moveNav(int n2) {
        List<CatalogEntry> list = this.catalogEntries;
        int n3 = list.size();
        if (this.view != View.MODULES || n3 == 0) {
            return false;
        }
        int n4 = Math.max(1, this.lastCatalogCols);
        if (this.navIndex < 0 || this.navIndex >= n3 || !this.navVisible) {
            this.navIndex = Math.max(0, Math.min(n3 - 1, this.navIndex));
            this.navVisible = true;
            this.navScrollPending = true;
            return true;
        }
        CatalogSections.Layout layout = this.lastLayout != null && this.lastLayout.n() == n3 ? this.lastLayout : null;
        int n5 = layout != null ? layout.col[this.navIndex] : this.navIndex % n4;
        switch (n2) {
            case 265: {
                this.navIndex = layout != null ? layout.step(this.navIndex, -1) : Math.max(0, this.navIndex - n4);
                break;
            }
            case 264: {
                this.navIndex = layout != null ? layout.step(this.navIndex, 1) : Math.min(n3 - 1, this.navIndex + n4);
                break;
            }
            case 263: {
                if (n5 > 0) {
                    --this.navIndex;
                    break;
                }
                if (!(this.moduleInspectorTargetProgress > 0.0f)) break;
                this.animateModuleInspectorTo(0.0f);
                break;
            }
            case 262: {
                if (n5 < n4 - 1 && this.navIndex < n3 - 1 && (layout == null || layout.row[this.navIndex + 1] == layout.row[this.navIndex])) {
                    ++this.navIndex;
                    break;
                }
                this.activateNav(true);
                break;
            }
            default: {
                return false;
            }
        }
        this.navScrollPending = true;
        return true;
    }

    private void selectModule(Module module) {
        this.selected = module;
        this.settingsScroll = 0;
        this.moduleInspectorScrollRemainder = 0.0;
        if (this.settingsFilterFor != module) {
            this.settingsFilter.setValue("");
            this.settingsFilterFor = module;
        }
        this.resetAllArmed = false;
        this.animateModuleInspectorTo(1.0f);
    }

    private void toggleModuleInspector(Module module) {
        if (this.selected == module && this.moduleInspectorTargetProgress > 0.0f) {
            this.animateModuleInspectorTo(0.0f);
            return;
        }
        this.selectModule(module);
    }

    private void animateModuleInspectorTo(float f2) {
        if (this.moduleInspectorTargetProgress == (f2 = GpuClickGuiScreen.clamp01(f2))) {
            return;
        }
        this.springs.seed(GpuClickGuiScreen.bf("\ue71e\ue978\u4ca5\u053a\udac6\u6f34\u8419\u3ca1\u4e67\ude64\u7a86\u9df8\ua49e\u0002\ud4fd\ub94f\uc07c\ub166\u8d72", -134220484, 0x1B1EF9FC ^ CryptoVault.mask()), this.moduleInspectorTargetProgress);
        this.moduleInspectorTargetProgress = f2;
        this.moduleInspectorAnimationStartedAt = this.nanoTime();
    }

    private void updateModuleInspectorAnimation(long l2) {
        if (this.moduleInspectorAnimationStartedAt == 0L) {
            return;
        }
        float f2 = this.anim(GpuClickGuiScreen.bf("\u4d6c\u440a\u5c1c\uc939\ude45\u237d\ub7d4\u951b\u2c13\u1cb1\u0cfe\u1ec6\uf87d\uf9fc\u7a3d\u1e2c\u7065\u5a5a\u5419", -1361123731, 0x1B1EF9FC ^ CryptoVault.mask()), this.moduleInspectorTargetProgress, 14.0f);
        this.moduleInspectorProgress = f2 * f2 * (3.0f - 2.0f * f2);
        if (f2 == this.moduleInspectorTargetProgress) {
            this.moduleInspectorProgress = this.moduleInspectorTargetProgress;
            this.moduleInspectorAnimationStartedAt = 0L;
            if (this.moduleInspectorTargetProgress == 0.0f) {
                this.selected = null;
            }
        }
    }

    private void drawModuleInspector(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, boolean bl, float f6, float f7, float f8) {
        boolean bl2;
        float f9;
        float f10;
        float f11;
        boolean bl3;
        float f12;
        float f13;
        boolean bl4;
        if (this.selected == null) {
            String string = GpuClickGuiScreen.bf("\u5997\u5978\u68bd\uac02\u9eb5\uc061\u1c72\u32e8\u0357\u1eb7\u4919\u7b48\u5e51\u4be4\u079c\ubcc4\uddb5\u9348\u1afd\ua00f\uc2dc\udc7a\u45cf\ue99d\u0cef\u1a04\ud4a8\u5a86\uff30\u43cd\u8afa\u581b\u77aa\u8d34\ua747\uc31d\u2244\ue171\ua9f5\u6beb\u5534\u07e0\ud1f2", -696167409, 0x1B1EF9FC ^ CryptoVault.mask());
            List<String> list = GpuClickGuiScreen.wrapText(string, Math.max(0.0f, f4 - 32.0f * f6), 11.0f * f6, 2);
            float f14 = 14.0f * f6;
            float f15 = f3 + f5 * 0.5f - (float)(list.size() - 1) * f14 * 0.5f;
            for (int i2 = 0; i2 < list.size(); ++i2) {
                GpuClickGuiScreen.textCentered(builder, f2 + f4 * 0.5f, f15 + (float)i2 * f14, 11.0f * f6, this.mutedColor, list.get(i2), UiDrawCommand.TextWeight.REGULAR);
            }
            return;
        }
        if (this.settingsFilterFor != this.selected) {
            this.settingsFilter.setValue("");
            this.settingsFilterFor = this.selected;
            this.resetAllArmed = false;
        }
        float f16 = 24.0f * f6;
        float f17 = f3 + 18.0f * f6;
        boolean bl5 = false;
        for (Setting setting : this.selected.getSettings()) {
            if (setting == null || setting instanceof HitboxKeyBinding655023 || !setting.isModified()) continue;
            bl5 = true;
            break;
        }
        boolean bl6 = bl4 = this.resetAllArmed && this.nanoTime() - this.resetAllArmedAt < 3000000000L;
        if (!bl4) {
            this.resetAllArmed = false;
        }
        float f18 = 0.0f;
        if (bl5) {
            String string = bl4 ? GpuClickGuiScreen.bf("\udd89\u242c\u24c3\u0183\u2bc6\u19a5\ufedd\ufcdd\u5aaf\u579a\ue56e\ua19e\u7e56\u6960\ue212\ud838\udd88\u0c90", -1209497294, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u73db\u5453\u9f4c\u84c7\u16bb\u5458\u87eb\u1f91\ueb09\ubea9\uf363", 1595609373, 0x1B1EF9FC ^ CryptoVault.mask());
            float f19 = Math.round(22.0f * f6);
            f18 = UiKit.buttonWidth(string, f6 * 0.9f);
            f13 = Math.round(f2 + f4 - f16 - f18 - this.inspectorHeaderInset);
            float f20 = Math.round(f17 + 1.0f * f6);
            Module module = this.selected;
            UiKit.button(builder, this.kitHost, string, f13, f20, f18, f19, f6 * 0.9f, bl4 ? UiKit.Style.DANGER : UiKit.Style.SECONDARY, true, f7, f8, () -> {
                if (this.resetAllArmed && this.nanoTime() - this.resetAllArmedAt < 3000000000L) {
                    for (Setting setting : module.getSettings()) {
                        if (setting == null || setting instanceof HitboxKeyBinding655023) continue;
                        setting.resetToDefault();
                    }
                    this.resetAllArmed = false;
                } else {
                    this.resetAllArmed = true;
                    this.resetAllArmedAt = this.nanoTime();
                }
            });
            if (this.hovered(f7, f8, f13, f20, f18, f19)) {
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\udea8\u6aec\u192a\u80e9\u7994\u983f\u8c04\u8f79\ufcdc\u149a\u94ed\u3815\u8c7a\u0b0d\u07e3\ufc5a\uad9f\u2b18\u679b\uf417\u1e52", -1329602085, 0x1B1EF9FC ^ CryptoVault.mask());
                this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\u0cf2\u2ed8\u7f9e\ud0bf\u1067\u30ff\uce06\u76f3\u0d57\ud288\u95c9\ud6e6\u9206\u1baf\uef67\uef82\udaa0\u5602\u0fc6\uae04\ua639\ua911\u2c07", -985498011, 0x1B1EF9FC ^ CryptoVault.mask()) + this.selected.getName() + GpuClickGuiScreen.bf("\ub7ec\u7db6\uce16\u925a\u8870\u5d8a\u918e\u27e5\u074c\ub405\ua1f4\u9516\u6316\ucce6\uc943\ue792\ucb70\ufd0c\u88a6\u56e6\uc268\uf99e\u01b9\ubb7d\u3452\u83c4\u68db\ufdc1\u7c1d\u1ff9\u3e12\u225c\u54a1\uef3c\ua84a\u8050\uad79\uc049\uec42\uc47b\u1b8f\u432e\ua32b\u086e\ue6f1\ue54b\ubaff\ucdbb\u2093\u4b80\uefd4\u6354\u8831\u0600\u17ea\uafb7\u8bf4\uc2e7\ue770\uc2ce\uf86e\u39de\u998d\uca72\u3c7e\u63c1\u4684", -1162262676, 0x1B1EF9FC ^ CryptoVault.mask());
            }
        }
        builder.text(f2 + f16, f17 + 2.0f * f6, 16.0f * f6, this.titleColor, UiKit.fit(this.selected.getName(), Math.max(0.0f, f4 - 2.0f * f16 - this.inspectorHeaderInset - (f18 > 0.0f ? f18 + 10.0f * f6 : 0.0f)), 16.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        float f21 = f4 - 2.0f * f16;
        List<String> list = GpuClickGuiScreen.wrapText(GpuClickGuiScreen.safeText(this.selected.getDescription()), f21, 11.0f * f6, 4);
        f13 = f17 + 24.0f * f6;
        for (int i3 = 0; i3 < list.size(); ++i3) {
            builder.text(f2 + f16, f13 + (float)i3 * 14.0f * f6, 11.0f * f6, this.mutedColor, list.get(i3));
        }
        float f22 = f12 = list.isEmpty() ? f17 + 36.0f * f6 : f13 + (float)list.size() * 14.0f * f6 + 6.0f * f6;
        if (this.showSeparators) {
            builder.rect(f2 + f16, f12, f4 - 2.0f * f16, f6, 0.0f, this.lineColor);
        }
        float f23 = f12 + 12.0f * f6;
        float f24 = f3 + f5 - 54.0f * f6;
        List<SettingGroup> list2 = GpuClickGuiScreen.settingGroups(this.selected);
        boolean bl7 = list2.isEmpty();
        int n2 = 0;
        for (SettingGroup settingGroup : list2) {
            n2 += settingGroup.settings().size();
        }
        Object object = "";
        if (n2 > 5) {
            float f25 = Math.round(28.0f * f6);
            this.settingsFilter.draw(builder, this.kitHost, f2 + f16, f23, f4 - 2.0f * f16, f25, f6);
            f23 += f25 + 10.0f * f6;
            object = this.settingsFilter.value().trim().toLowerCase(Locale.ROOT);
        } else if (!this.settingsFilter.isEmpty()) {
            this.settingsFilter.setValue("");
        }
        boolean bl8 = bl3 = !((String)object).isEmpty();
        if (bl3) {
            list2 = GpuClickGuiScreen.filterGroups(list2, (String)object);
        }
        float f26 = 0.0f;
        for (SettingGroup settingGroup : list2) {
            f26 += this.groupHeight(settingGroup, bl3 ? null : GpuClickGuiScreen.groupKey(this.selected, settingGroup));
        }
        float f27 = (this.selected.isOneShot() || bl3 ? 0.0f : 58.0f) + (list2.isEmpty() ? 36.0f : f26);
        float f28 = Math.max(0.0f, (f24 - f23) / f6);
        int n3 = (int)Math.ceil(Math.max(0.0f, f27 - f28));
        this.settingsScroll = Math.min(this.settingsScroll, n3);
        this.settingsScrollShown = this.easeScroll(this.settingsScrollShown, this.settingsScroll);
        builder.clipPush(f2 + f16, f23, f4 - 2.0f * f16, f24 - f23, 0.0f);
        this.clipX = f2 + f16;
        this.clipY = f23;
        this.clipW = f4 - 2.0f * f16;
        this.clipH = f24 - f23;
        this.clippingHits = true;
        float f29 = f23 - this.settingsScrollShown * f6;
        float f30 = f2 + f16;
        float f31 = f4 - 2.0f * f16;
        if (!this.selected.isOneShot() && !bl3) {
            f11 = 46.0f * f6;
            this.surfaceCard(builder, f30, f29, f31, f11, this.cardRadius * f6);
            GpuClickGuiScreen.textMid(builder, f30 + 16.0f * f6, f29 + f11 * 0.5f, 12.5f * f6, this.textColor, GpuClickGuiScreen.bf("\u21af\u4090\ud8f7\u427e\ub484\u56f7\ua508\uce1c\ub0a4\u384b\u60ab", -647150168, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            float f32 = 34.0f * f6;
            f10 = 18.0f * f6;
            float f33 = f30 + f31 - f32 - 14.0f * f6;
            float f34 = f29 + (f11 - f10) * 0.5f;
            this.drawToggleSwitch(builder, GpuClickGuiScreen.bf("\ud752\u52e4\ub87b\u9cd4\u8c52\u6900\u1166\u2ffa\ua673", 761952762, 0x1B1EF9FC ^ CryptoVault.mask()) + this.selected.getName(), f33, f34, f32, f10, this.selected.isEnabled(), f6);
            if (!this.selected.isLocked()) {
                this.hit(f33 - 4.0f * f6, f29, f32 + 8.0f * f6, f11, this.selected::toggle);
            }
            f29 += f11 + 12.0f * f6;
        }
        if (bl7) {
            builder.text(f30 + 4.0f * f6, f29 + 16.0f * f6, 11.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\u9268\ube22\u27be\u4ad7\u3d06\u7e7c\u3378\u8694\u09d3\u8556\ue60d\u81a0\uddb2\ua61a\u7862\u0ca4\ud54a\uc8aa\uf1f2\u6a7e\udf82\ud244\u92fc\u6408\u68aa\uaf80\u8b11\u2a34\u36b4\u0dd7\u7f45\u1339\uabea\ucf9d\u015c\uc1bb\uf211\u977c\u09bb\uf8d2", 1631207092, 0x1B1EF9FC ^ CryptoVault.mask()));
            f29 += 36.0f * f6;
        } else if (list2.isEmpty()) {
            builder.text(f30 + 4.0f * f6, f29 + 16.0f * f6, 11.0f * f6, this.mutedColor, UiKit.fit(GpuClickGuiScreen.bf("\u70f4\u12cb\u804e\u9574\u7b24\ubf69\u6815\u1d83\u8d71\ucb24\u98bb\u12cc\ueae1\u7b14\u46ad\ue7f0\u6e0e\u9d09\u0fd8\uef79", -658477528, 0x1B1EF9FC ^ CryptoVault.mask()) + this.settingsFilter.value().trim() + "\".", f31 - 8.0f * f6, 11.0f * f6));
            f29 += 36.0f * f6;
        } else {
            for (SettingGroup settingGroup : list2) {
                f29 = this.drawSettingGroup(builder, settingGroup.title(), settingGroup.settings(), f30, f29, f31, f6, f7, f8, bl3 ? null : GpuClickGuiScreen.groupKey(this.selected, settingGroup));
            }
        }
        this.clippingHits = false;
        builder.clipPop();
        f11 = f3 + f5 - 50.0f * f6;
        if (this.showSeparators) {
            builder.rect(f2 + f16, f11, f4 - 2.0f * f16, f6, 0.0f, this.lineColor);
        }
        String string = this.selected.isOneShot() || this.selected.isKeyTriggered() ? GpuClickGuiScreen.bf("\uf6cc\u7cda\ueb5f\u6ccc\u52a7\u2120\u797f\u715d\u0856\u5377\u837b\u55ec\u48f9\u3211\u4dec", -565163304, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u0fbb\u534f\ua43f\uee55\u4949\u666e\u48db\uc887\u017e\u41c7", 129391729, 0x1B1EF9FC ^ CryptoVault.mask());
        f10 = f11 + 18.0f * f6;
        GpuClickGuiScreen.textMid(builder, f2 + f16 + 2.0f * f6, f10, 11.5f * f6, this.textColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
        boolean bl9 = this.bindingModuleKey && this.bindingModuleTarget == this.selected;
        String string2 = bl9 ? GpuClickGuiScreen.bf("\u2457\u0dc1\u2d65\uab64\ub0dd\ub446\u1c73\u1283\uec72\u1607\u5c22\u4b72\uc5bd\uc6a9\u1f58", 1249033341, 0x1B1EF9FC ^ CryptoVault.mask()) : this.selected.getKeyDisplayName();
        float f35 = Math.round(26.0f * f6);
        float f36 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u6af5\u779e\u494d\u466a\u20dc\ubbce", 71337284, 0x1B1EF9FC ^ CryptoVault.mask()), 10.0f * f6) + 8.0f * f6;
        float f37 = Math.round(Math.min(150.0f * f6, Math.max(56.0f * f6, GpuUiFontMetrics.textWidth(string2, 10.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f6)));
        float f38 = Math.round(f2 + f4 - f16 - f36 - 8.0f * f6 - f37);
        float f39 = Math.round(f10 - f35 * 0.5f);
        builder.rect(f38, f39, f37, f35, this.pillR(f35), bl9 ? this.controlFill(72) : this.controlFill(26));
        GpuClickGuiScreen.textCentered(builder, f38 + f37 * 0.5f, f10, 10.5f * f6, bl9 ? this.accentArgb() : this.textColor, UiKit.fit(string2, f37 - 16.0f * f6, 10.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f38, f39, f37, f35, () -> this.beginModuleKeyCapture(this.selected));
        if (BindModeControl.available(this.selected)) {
            boolean bl10 = BindModeControl.hold(this.selected);
            String string3 = BindModeControl.label(this.selected);
            float f40 = Math.round(GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\ua1a0\u016d\u60db\u4f73\u7e95\u3bc7\u00d7\u2c1e", -1381812115, 0x1B1EF9FC ^ CryptoVault.mask()), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f6);
            float f41 = Math.round(f38 - 8.0f * f6 - f40);
            boolean bl11 = this.hovered(f7, f8, f41, f39, f40, f35);
            builder.rect(f41, f39, f40, f35, this.pillR(f35), bl10 ? UiKit.withAlpha(this.accentArgb(), 48) : this.controlFill(bl11 ? 42 : 26));
            if (bl10) {
                builder.border(f41, f39, f40, f35, this.pillR(f35), 1.0f, UiKit.withAlpha(this.accentArgb(), 112));
            }
            GpuClickGuiScreen.textCentered(builder, f41 + f40 * 0.5f, f10, 9.5f * f6, bl10 ? this.accentArgb() : this.textColor, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f41, f39, f40, f35, () -> BindModeControl.toggle(this.selected));
            if (bl11) {
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\u228e\u7490\u9832\u371c\u3f27\u2b54\u9583\u8e11\u4ed8", -1597166219, 0x1B1EF9FC ^ CryptoVault.mask());
                this.hoveredTooltipDesc = BindModeControl.tip(this.selected);
            }
        }
        GpuClickGuiScreen.textCentered(builder, f9 + f36 * 0.5f, f10, 10.0f * f6, (bl2 = this.hovered(f7, f8, f9 = f2 + f4 - f16 - f36, f39, f36, f35)) ? this.textColor : this.mutedColor, GpuClickGuiScreen.bf("\ub328\u1149\u9c9e\ucc41\uaa6f\u5106\u97c1\u3e46", 2019839118, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
        this.hit(f9, f39, f36, f35, () -> {
            this.selected.setKeyCode(-1);
            if (this.bindingModuleTarget == this.selected) {
                this.cancelKeybindCapture();
            }
        });
    }

    private boolean inlineSettingsMode() {
        return GpuClickGuiScreen.bf("\u49cc\ud7b5\u91ed\u8fd2\u7b98\u46b9\udb96\uacd7\u560c\u94f3\u89b8\uabe7\u16a1", -1340448474, 0x1B1EF9FC ^ CryptoVault.mask()).equals(HitboxConfigScreen752809.e());
    }

    private boolean sidePanelSettingsMode() {
        return GpuClickGuiScreen.bf("\ucb7c\u44d3\uef89\u7735\ucb3c\u9147\u68f2\uc33b\u4c47\u2f75\u0465", 221539452, 0x1B1EF9FC ^ CryptoVault.mask()).equals(HitboxConfigScreen752809.e());
    }

    private boolean moduleFloatActive() {
        return this.view == View.MODULES && this.selected != null && GpuClickGuiScreen.bf("\u2a7a\ub31d\ud575\ub3dc\u87ee\u3242\u9c3c\u1ec8\uecc2", 1416475422, 0x1B1EF9FC ^ CryptoVault.mask()).equals(HitboxConfigScreen752809.e()) && (this.moduleInspectorProgress > 0.002f || this.moduleInspectorTargetProgress > 0.0f);
    }

    private void drawModuleFloat(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8 = GpuClickGuiScreen.clamp01(this.moduleInspectorProgress);
        Runnable runnable = () -> this.animateModuleInspectorTo(0.0f);
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, GpuClickGuiScreen.argb(166.0f * f8, 329483));
        this.hit(0.0f, 0.0f, f2, f3, runnable);
        this.hitModuleCardRight(0.0f, 0.0f, f2, f3, runnable);
        float f9 = Math.min(f2 - (float)Math.round(48.0f * f4), (float)Math.round(560.0f * f4));
        float f10 = Math.min(f3 - (float)Math.round(48.0f * f4), (float)Math.round(620.0f * f4));
        float f11 = (1.0f - f8) * (float)Math.round(14.0f * f4);
        float f12 = Math.round((f2 - f9) * 0.5f);
        float f13 = Math.round((f3 - f10) * 0.5f + f11);
        float f14 = this.panelRadius * f4;
        builder.shadow(f12, f13, f9, f10, f14, Math.round(30.0f * f4), 0.0f, GpuClickGuiScreen.argb(144.0f * f8, 0));
        if (this.lg) {
            this.glassPane(builder, f12, f13, f9, f10, f14, f8, 68, false, 0);
        } else if (this.floatMaterial) {
            builder.clipPush(f12, f13, f9, f10, f14);
            this.paintSurface(builder, this.floatTheme, f12, f13, f9, f10, f14, this.floatMist);
            builder.rect(f12, f13, f9, f10, 0.0f, this.listBg);
            builder.clipPop();
        } else {
            builder.glass(f12, f13, f9, f10, f14, this.floatBlur, this.floatTint);
            this.drawGlassWash(builder, f12, f13, f9, f10, f14);
        }
        builder.border(f12, f13, f9, f10, f14, 1.0f, this.lineColor);
        this.hit(f12, f13, f9, f10, () -> {});
        this.hitModuleCardRight(f12, f13, f9, f10, () -> {});
        boolean bl = this.inspectorRevealHitClip;
        this.inspectorRevealX = f12;
        this.inspectorRevealY = f13;
        this.inspectorRevealW = f9;
        this.inspectorRevealH = f10;
        this.inspectorRevealHitClip = true;
        builder.clipPush(f12, f13, f9, f10, f14);
        boolean bl2 = this.moduleInspectorTargetProgress > 0.0f && GpuClickGuiScreen.inside(f5, f6, f12, f13, f9, f10);
        this.inspectorHeaderInset = f7 = (float)Math.round(28.0f * f4);
        this.drawModuleInspector(builder, f12, f13, f9, f10, false, f4, bl2 ? f5 : -1.0f, bl2 ? f6 : -1.0f);
        this.inspectorHeaderInset = 0.0f;
        float f15 = Math.round(f12 + f9 - 16.0f * f4 - f7);
        float f16 = Math.round(f13 + 16.0f * f4);
        boolean bl3 = bl2 && GpuClickGuiScreen.inside(f5, f6, f15, f16, f7, f7);
        builder.circle(f15 + f7 * 0.5f, f16 + f7 * 0.5f, f7 * 0.5f, GpuClickGuiScreen.argb((float)(bl3 ? 34 : 14) * f8, 0xFFFFFF));
        ClassicIcons.draw(builder, GpuClickGuiScreen.bf("\u8836\u8bf0\u8697\ua442\u9119\u9e57\u83ed", 1522122063, 0x1B1EF9FC ^ CryptoVault.mask()), f15 + 8.0f * f4, f16 + 8.0f * f4, 12.0f * f4, bl3 ? this.textColor : this.mutedColor);
        this.hit(f15, f16, f7, f7, runnable);
        builder.clipPop();
        this.inspectorRevealHitClip = bl;
        if (f8 < 0.999f) {
            builder.rect(f12, f13, f9, f10, f14, GpuClickGuiScreen.argb(160.0f * (1.0f - f8), 724758));
        }
        String string = GpuClickGuiScreen.bf("\u0dde\udfd2\u89ea\u3f89\u21b0\u6b23\u26f9\u1a6c\u5ea0\u6141\u1d9f\u238c\u0cd9\ucb0f\u1210\u7805\uf406\uc80e\u0af3\uaabb\u584b\u0e78\ufdc2\uff1e\u058b\u0603\ue5af\udb17\u7eb2\ufbbc\u4e76\uc65f\u7e0c\ub2a5\u36d3\u834b\uf422\u967a", -920284029, 0x1B1EF9FC ^ CryptoVault.mask());
        float f17 = 9.5f * f4;
        float f18 = f13 + f10 + (float)Math.round(10.0f * f4);
        if (f18 + f17 < f3 - 4.0f * f4) {
            builder.text(f12 + (f9 - GpuUiFontMetrics.textWidth(string, f17)) * 0.5f, f18, f17, GpuClickGuiScreen.argb(176.0f * f8, this.muted2Color & 0xFFFFFF), string);
        }
    }

    private float inlineUnits(Module module) {
        List<SettingGroup> list = GpuClickGuiScreen.settingGroups(module);
        float f2 = 8.0f;
        if (list.isEmpty()) {
            f2 += 30.0f;
        } else {
            for (SettingGroup settingGroup : list) {
                f2 += this.groupHeight(settingGroup, GpuClickGuiScreen.groupKey(module, settingGroup));
            }
        }
        f2 += 42.0f;
        return f2 += 8.0f;
    }

    private void drawInlineBlock(UiDisplayList.Builder builder, Module module, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (module == null || f5 <= 0.0f) {
            return;
        }
        float f11 = this.cardRadius * f6;
        builder.rect(f2, f3, f4, f5, f11, GpuClickGuiScreen.withA(724758, 102));
        if (this.cardBorderCustom || this.showSeparators) {
            builder.border(f2, f3, f4, f5, f11, 1.0f, this.lineColor);
        }
        builder.rect(f2, f3, Math.max(2.0f, 2.0f * f6), f5, f11, GpuClickGuiScreen.withA(this.accentArgb(), 144));
        float f12 = Math.max(f3, f9);
        float f13 = Math.min(f3 + f5, f10);
        if (f13 <= f12) {
            return;
        }
        float f14 = this.clipX;
        float f15 = this.clipY;
        float f16 = this.clipW;
        float f17 = this.clipH;
        boolean bl = this.clippingHits;
        builder.clipPush(f2, f12, f4, f13 - f12, 0.0f);
        this.clipX = f2;
        this.clipY = f12;
        this.clipW = f4;
        this.clipH = f13 - f12;
        this.clippingHits = true;
        float f18 = 14.0f * f6;
        float f19 = f2 + f18;
        float f20 = Math.max(0.0f, f4 - f18 * 2.0f);
        float f21 = f3 + 12.0f * f6;
        List<SettingGroup> list = GpuClickGuiScreen.settingGroups(module);
        if (list.isEmpty()) {
            builder.text(f19 + 2.0f * f6, f21 + 12.0f * f6, 11.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\u2eee\uc531\u9c98\u6338\uf1ba\u96f3\u8f7c\udc2c\uaafb\ue421\u9984\u2948\ue38b\ua9b7\uf4f0\u4ec9\u2be0\u5c22\u7554\ue6a1\u9ccd\u8dd6\ueba3\ue451\u4b84\u79de\u8d9c\uf4cd\u3a9b\ua31e\ua47f\u7077\uadc2\u40ed\u4e73\ua46e\uac16\u4870\u7556\u0013\u5d7e", 129031326, 0x1B1EF9FC ^ CryptoVault.mask()));
            f21 += 30.0f * f6;
        } else {
            for (SettingGroup settingGroup : list) {
                f21 = this.drawSettingGroup(builder, settingGroup.title(), settingGroup.settings(), f19, f21, f20, f6, f7, f8, GpuClickGuiScreen.groupKey(module, settingGroup));
            }
        }
        this.drawInlineBind(builder, module, f19, f21, f20, f6, f7, f8);
        this.clipX = f14;
        this.clipY = f15;
        this.clipW = f16;
        this.clipH = f17;
        this.clippingHits = bl;
        builder.clipPop();
    }

    private void drawInlineBind(UiDisplayList.Builder builder, Module module, float f2, float f3, float f4, float f5, float f6, float f7) {
        boolean bl;
        float f8;
        float f9 = Math.round(26.0f * f5);
        float f10 = Math.round(f3 + 6.0f * f5);
        float f11 = f10 + f9 * 0.5f;
        String string = module.isOneShot() || module.isKeyTriggered() ? GpuClickGuiScreen.bf("\ue69d\u91f6\ucdd5\u339d\u2b7f\ubdf5\ud763\ua625\u972f\u257a\u61c4\u7122\uf004\u40f4\u6b04", -1221174656, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u90f1\u3d84\u57d2\ue8ed\uc60a\uf12a\ufaa8\uc289\u667b", -1805447561, 0x1B1EF9FC ^ CryptoVault.mask());
        GpuClickGuiScreen.textMid(builder, f2 + 2.0f * f5, f11, 11.0f * f5, this.textColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
        boolean bl2 = this.bindingModuleKey && this.bindingModuleTarget == module;
        String string2 = bl2 ? GpuClickGuiScreen.bf("\u6cc7\u26b0\ua9c4\ua305\u2ac8\u0fce\uf163\u36b4\ua796\ufd73\ue885\u2a06\u90fa\ua887", -1273252254, 0x1B1EF9FC ^ CryptoVault.mask()) : module.getKeyDisplayName();
        float f12 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\uc635\u22e7\u3f8a\u1334\ueaf8\udf81\u9b80\u91cb\ufdb3", 520867757, 0x1B1EF9FC ^ CryptoVault.mask()), 10.0f * f5) + 8.0f * f5;
        float f13 = Math.round(Math.min(150.0f * f5, Math.max(56.0f * f5, GpuUiFontMetrics.textWidth(string2, 10.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f5)));
        float f14 = Math.round(f2 + f4 - f12 - 8.0f * f5 - f13);
        float f15 = Math.round(f11 - f9 * 0.5f);
        builder.rect(f14, f15, f13, f9, this.pillR(f9), bl2 ? this.controlFill(72) : this.controlFill(26));
        GpuClickGuiScreen.textCentered(builder, f14 + f13 * 0.5f, f11, 10.5f * f5, bl2 ? this.accentArgb() : this.textColor, UiKit.fit(string2, f13 - 16.0f * f5, 10.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f14, f15, f13, f9, () -> this.beginModuleKeyCapture(module));
        if (BindModeControl.available(module)) {
            boolean bl3 = BindModeControl.hold(module);
            String string3 = BindModeControl.label(module);
            float f16 = Math.round(GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\ub011\u1107\u060e\u1106\ue259\uae3e\ud222", 1406685389, 0x1B1EF9FC ^ CryptoVault.mask()), 9.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f5);
            float f17 = Math.round(f14 - 8.0f * f5 - f16);
            boolean bl4 = this.hovered(f6, f7, f17, f15, f16, f9);
            builder.rect(f17, f15, f16, f9, this.pillR(f9), bl3 ? GpuClickGuiScreen.withA(this.accentArgb(), 48) : this.controlFill(bl4 ? 42 : 26));
            if (bl3) {
                builder.border(f17, f15, f16, f9, this.pillR(f9), 1.0f, GpuClickGuiScreen.withA(this.accentArgb(), 112));
            }
            GpuClickGuiScreen.textCentered(builder, f17 + f16 * 0.5f, f11, 9.5f * f5, bl3 ? this.accentArgb() : this.textColor, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f17, f15, f16, f9, () -> BindModeControl.toggle(module));
            if (bl4) {
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\u5d75\u4ac7\ua622\u3020\u22cf\ubfb9\u5399\ud339\u776f\u20e7\ua519\u679f", 516731176, 0x1B1EF9FC ^ CryptoVault.mask());
                this.hoveredTooltipDesc = BindModeControl.tip(module);
            }
        }
        GpuClickGuiScreen.textCentered(builder, f8 + f12 * 0.5f, f11, 10.0f * f5, (bl = this.hovered(f6, f7, f8 = f2 + f4 - f12, f15, f12, f9)) ? this.textColor : this.mutedColor, GpuClickGuiScreen.bf("\u26ed\u6919\ue2a5\ubc12\u90ab\u89aa\udcde\u2cad\u411f", -296266104, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
        this.hit(f8, f15, f12, f9, () -> {
            module.setKeyCode(-1);
            if (this.bindingModuleTarget == module) {
                this.cancelKeybindCapture();
            }
        });
    }

    private float pillR(float f2) {
        return f2 * 0.5f * Math.min(1.0f, this.cornerScale);
    }

    private void surfaceCard(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        if (this.lg) {
            this.glassPane(builder, f2, f3, f4, f5, f6, 1.0f, 42, false, 0);
            if (this.cardFillCustom) {
                builder.rect(f2, f3, f4, f5, f6, this.cardBg);
            }
        } else {
            builder.rect(f2, f3, f4, f5, f6, this.cardBg);
        }
        if (this.cardBorderCustom) {
            builder.border(f2, f3, f4, f5, f6, 1.0f, this.cardBorder);
        }
    }

    private static String groupKey(Module module, SettingGroup settingGroup) {
        return settingGroup.title() == null ? null : module.getName() + "|" + settingGroup.title();
    }

    protected static List<SettingGroup> filterGroups(List<SettingGroup> list, String string) {
        ArrayList<SettingGroup> arrayList = new ArrayList<SettingGroup>();
        for (SettingGroup settingGroup : list) {
            boolean bl = settingGroup.title() != null && settingGroup.title().toLowerCase(Locale.ROOT).contains(string);
            ArrayList<Setting> arrayList2 = new ArrayList<Setting>();
            for (Setting setting2 : settingGroup.settings()) {
                if (!bl && !setting2.getName().toLowerCase(Locale.ROOT).contains(string) && !GpuClickGuiScreen.safeText(setting2.getDescription()).toLowerCase(Locale.ROOT).contains(string)) continue;
                arrayList2.add(setting2);
            }
            if (!arrayList2.stream().anyMatch(setting -> SUB_VISIBLE.getOrDefault(setting, true))) continue;
            arrayList.add(new SettingGroup(settingGroup.title(), arrayList2));
        }
        return arrayList;
    }

    private void beginModuleKeyCapture(Module module) {
        this.cancelKeybindCapture();
        this.bindingModuleTarget = module;
        boolean bl = this.bindingModuleKey = module != null;
        if (module != null) {
            module.setBinding(true);
        }
    }

    private void beginSettingKeyCapture(HitboxKeyBinding655023 hitboxKeyBinding655023) {
        this.cancelKeybindCapture();
        this.binding = hitboxKeyBinding655023;
        this.bindingModuleTarget = hitboxKeyBinding655023.a();
        if (this.bindingModuleTarget != null) {
            this.bindingModuleTarget.setBinding(true);
        }
    }

    private void beginConfigKeyCapture(String string) {
        this.cancelKeybindCapture();
        this.bindingConfigName = string;
    }

    private int settingHeight(Setting setting) {
        if (setting == HitboxConfigScreen752809.U) {
            return 172;
        }
        if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            int n2 = modeSetting.getModes().size();
            int n3 = Math.max(1, n2 <= 4 ? n2 : (n2 <= 6 ? 3 : 4));
            int n4 = Math.max(1, (n2 + n3 - 1) / n3);
            return Math.round(33.0f + (float)n4 * 26.0f + (float)(n4 - 1) * 5.0f + 3.0f + 12.0f);
        }
        if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            int n5 = multiSelectSetting.E().size();
            int n6 = Math.max(1, (n5 + GpuClickGuiScreen.multiCols(n5) - 1) / GpuClickGuiScreen.multiCols(n5));
            return Math.round(33.0f + (float)n6 * 24.0f + (float)(n6 - 1) * 5.0f + 3.0f + 12.0f);
        }
        if (setting instanceof SliderSetting) {
            return 53;
        }
        return 46;
    }

    private void drawLayoutChooserRow(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 14.0f * f6;
        this.drawLayoutCards(builder, f2 + f9, f3 + 32.0f * f6, f4 - f9 * 2.0f, f5 - 32.0f * f6 - 10.0f * f6, f6, f7, f8);
    }

    private void drawLayoutCards(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.drawLayoutCards(builder, f2, f3, f4, f5, f6, f7, f8, this.kitHost);
    }

    protected void refreshReplicaPalette() {
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        this.currentAccent = hitboxConfigScreen752809 == null ? -13638657 : (HitboxConfigScreen752809.M() ? HitboxConfigScreen752809.K.A().getRGB() : UiThemeMaterial.theme(HitboxConfigScreen752809.a.getCurrentMode()).accent());
        this.applyPalette(UiThemeMaterial.theme(hitboxConfigScreen752809 == null ? "" : HitboxConfigScreen752809.a.getCurrentMode()));
    }

    protected void drawLayoutCards(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8, UiHost uiHost) {
        String string = HitboxConfigScreen752809.U != null ? HitboxConfigScreen752809.U.getCurrentMode() : GpuClickGuiScreen.bf("\uf997\ufd47\u0b2b\u09fc\ud33a\ub1df\ud44c", -1292610578, 0x1B1EF9FC ^ CryptoVault.mask());
        float f9 = 10.0f * f6;
        float f10 = (f4 - f9) / 2.0f;
        this.drawLayoutCard(builder, f2, f3, f10, f5, GpuClickGuiScreen.bf("\u5fb0\ud8ce\u881a\u5760\u757f\u6b0c", 292456609, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u15a3\ueffd\ucab9\u9eaa\u2631\u0bc7\u9f4e\u9e83\u0ffc\u7744\ua729\u6b0f\u4284\ud49c\ueb55\u6959\u2644\u026f\ucc61\u95b2\u0fa0\uf48b\u266a\ua0e2\ucc50\u6440\u595f\ua21e\ueb20\ubda9\ue67b\u5497\u7400\uf6de\u7de2\ubc8c\u6f35\ufdf8\ud3c8\ubb7e\ud80a\uafec\u620b\u05bc\u1140\u260e\u8657\u2923\u6b85\u2b88\u9517\u0faf\u778b\u1fc2\u533d\ufb48\u8395\ue84e\u17c3\u8f6f\uaf1c\u36c4\ub2d2\u5f36\ubfdd\u1fee\u85e8\u7aed\ud1b7\u3fee\ucd44\u8676\ud8c7\u42e9\ucaa5\ub3e4\u73f8\uf09a\ue537\ubfac", -1265207195, 0x1B1EF9FC ^ CryptoVault.mask()), string.equalsIgnoreCase(GpuClickGuiScreen.bf("\u6967\uf4fe\u865f\uac4c\u12a0\u54a9\ue3b8", -1271612023, 0x1B1EF9FC ^ CryptoVault.mask())), true, f6, f7, f8, uiHost);
        this.drawLayoutCard(builder, f2 + f10 + f9, f3, f10, f5, GpuClickGuiScreen.bf("\u17f9\u9e27\ucea3\ud3d3\udbf3\ub2a3\u08a0\u743b\u625f\ua013", 770511065, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u1757\u6d41\ud8bc\u45e4\u57e9\u7e59\u753d\u02b7\u4456\ueab6\ub648\u28ac\ub12b\u2fcc\uf079\u3465\ufcb7\u77b2\u796c\ub3cc\u140d\u6389\ua099\uaebf\u25de\u04a3\ue914\ucc5b\uee52\u1c2a\u6311\u4a62\u1077\uc946\u78da\u1c15\ub9da\u8401\u2769\u175a\u45a0\ue125\ud573\u4a54\u1120\u272f\uee2b\u4e76\uf442\u708d\u9b3d\u10847\u7e69\u43ed\u21cb\u49b1\u16ba\u9c1e\u5e6d\u4510\ue3d4\ucddb\u083e\u4052\u8872\u0b80\udec8\u1cb7\uefc5\u4d99\ucdcb\uea64", -931526737, 0x1B1EF9FC ^ CryptoVault.mask()), string.equalsIgnoreCase(GpuClickGuiScreen.bf("\u1fc1\u6f79\ue2f4\u81bb\ucf69\u178d\u158e\ufedd\u6866", 1897276904, 0x1B1EF9FC ^ CryptoVault.mask())), false, f6, f7, f8, uiHost);
    }

    private void drawMenuReplica(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5) {
        float f6 = Math.min(f4 / 640.0f, f5 / 400.0f);
        float f7 = 640.0f * f6;
        float f8 = 400.0f * f6;
        builder.clipPush(f2 += (f4 - f7) * 0.5f, f3 += (f5 - f8) * 0.5f, f7, f8, 9.0f * f6);
        builder.rect(f2, f3, f7, f8, 9.0f * f6, 0xF0000000 | this.railBg & 0xFFFFFF);
        this.accentFill(builder, f2, f3, f7, f8, 9.0f * f6, 0.06f, 0);
        float f9 = 32.0f * f6;
        float f10 = 118.0f * f6;
        builder.rect(f2, f3 + f9, f10, f8 - f9, 0.0f, this.railBg);
        builder.rect(f2 + f10, f3 + f9, f7 - f10, f8 - f9, 0.0f, this.listBg);
        builder.rect(f2, f3 + f9, f7, Math.max(1.0f, f6), 0.0f, this.lineColor);
        builder.rect(f2 + f10, f3 + f9, Math.max(1.0f, f6), f8 - f9, 0.0f, this.lineColor);
        GpuClickGuiScreen.textMid(builder, f2 + 12.0f * f6, f3 + f9 * 0.5f, 11.0f * f6, this.accentArgb(), "F", UiDrawCommand.TextWeight.SEMIBOLD);
        GpuClickGuiScreen.textMid(builder, f2 + 23.0f * f6, f3 + f9 * 0.5f, 9.0f * f6, this.textColor, GpuClickGuiScreen.bf("\u2d79\u38f3\uf533\uc159\u5a90\u13b2\ue5dd\u933e", 1027387975, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        String[] stringArray = new String[]{GpuClickGuiScreen.bf("\u163e\ub332\uc6a6\ua295\ucff8\ua3a8\u6cc2\ue72b\ucb74\uf253\u8dea\u8dd1", -1119219349, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uc290\u13b1\u1272\u5be8\uc6fe\u539a\u9ba7\u2664\u9765", 2108377943, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\ua332\uba35\uda80\uee70\ud3be\ua12e\u395c\u06ea\u2e96", 1622145838, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u4a3f\u9fb2\ud821\ua8f5\u03bf\u99e9\u87c7\u708e\uaf97\u2f15\u72ec\u2ddb", -1034030215, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uad19\ue930\u473b\ua078\u74d8\ud792\ub2a4\u9858\u5f5c\ufec7\u7b81\uc28b\u3649\ue710", -2061062952, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\uea6c\u3a8e\u88a7\uc5ce\u6214\u028c\u6a6d\u02ff\u466b\u15f3\uc92f\u1d83\u71ca\u1654\u16a8\ud711\udb93", 1031401267, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\udca8\ua6f2\u05c8\uc686\u71c8\u989d\u0f7c\ue5f9\u9e29\u71cd", 1114087136, 0x1B1EF9FC ^ CryptoVault.mask())};
        float f11 = 6.6f * f6;
        float f12 = 0.0f;
        for (String string : stringArray) {
            f12 += GpuUiFontMetrics.textWidth(string, f11, UiDrawCommand.TextWeight.REGULAR) + 14.0f * f6;
        }
        float f13 = f2 + Math.max(70.0f * f6, (f7 - f12) * 0.5f);
        builder.rect(f13 - 3.0f * f6, f3 + 7.0f * f6, f12 + 6.0f * f6, 18.0f * f6, 9.0f * f6, GpuClickGuiScreen.argb(38.0f, 0));
        String[] stringArray2 = stringArray;
        int n2 = stringArray2.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = stringArray2[i2];
            float f14 = GpuUiFontMetrics.textWidth(string, f11, UiDrawCommand.TextWeight.REGULAR) + 14.0f * f6;
            GpuClickGuiScreen.textMid(builder, f13 + 7.0f * f6, f3 + f9 * 0.5f, f11, this.mutedColor, string, UiDrawCommand.TextWeight.REGULAR);
            f13 += f14;
        }
        float f15 = f3 + f9 + 10.0f * f6;
        for (n2 = 0; n2 < NAV_CATEGORIES.length; ++n2) {
            Category category = NAV_CATEGORIES[n2];
            float f16 = 22.0f * f6;
            if (n2 == 0) {
                builder.shadow(f2 + 7.0f * f6, f15 + 2.0f * f6, f10 - 14.0f * f6, f16, 8.0f * f6, 8.0f * f6, 0.0f, GpuClickGuiScreen.argb(64.0f, this.accentArgb()));
                this.accentFill(builder, f2 + 7.0f * f6, f15, f10 - 14.0f * f6, f16, 8.0f * f6, 0.35f, 0);
                builder.border(f2 + 7.0f * f6, f15, f10 - 14.0f * f6, f16, 8.0f * f6, Math.max(1.0f, f6), GpuClickGuiScreen.argb(144.0f, this.accentArgb()));
            }
            int n3 = n2 == 0 ? this.textColor : this.mutedColor;
            GcsCategoryIcons.draw(builder, GcsCategoryIcons.forCategory(category.name()), f2 + 15.0f * f6, f15 + 5.0f * f6, 12.0f * f6, n3);
            GpuClickGuiScreen.textMid(builder, f2 + 33.0f * f6, f15 + f16 * 0.5f, 7.5f * f6, n3, category.zw, UiDrawCommand.TextWeight.REGULAR);
            f15 += 27.0f * f6;
        }
        float f17 = f2 + f10 + 14.0f * f6;
        float f18 = f7 - f10 - 28.0f * f6;
        float f19 = f3 + f9 + 12.0f * f6;
        builder.text(f17, f19, 12.0f * f6, this.titleColor, GpuClickGuiScreen.NAV_CATEGORIES[0].zw, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.rect(f17, f19 + 22.0f * f6, f18, 18.0f * f6, 9.0f * f6, this.controlFill(96));
        GcsCategoryIcons.draw(builder, GpuClickGuiScreen.bf("\u5bc4\u0096\ubc15\u7040\u4784\u6a7b\u883d\ua54c", 2258698, 0x1B1EF9FC ^ CryptoVault.mask()), f17 + 7.0f * f6, f19 + 26.0f * f6, 10.0f * f6, this.mutedColor);
        GpuClickGuiScreen.textMid(builder, f17 + 22.0f * f6, f19 + 31.0f * f6, 6.8f * f6, this.mutedColor, GpuClickGuiScreen.bf("\u2929\u497c\ud878\u2cea\uc69c\u3daf\u7a1d\u6a24\u9ee0\u23fb\ua1f2\u46ec\u4a9a\u2aeb\ud8ff\u5274\u0a46\u1723\u48c3\ub25e\ucfec\ueebb\u1c59", 1782051524, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
        List<Module> list = this.modulesForCategory(NAV_CATEGORIES[0]);
        float f20 = f19 + 48.0f * f6;
        float f21 = 8.0f * f6;
        float f22 = (f18 - f21) * 0.5f;
        float f23 = 44.0f * f6;
        for (int i3 = 0; i3 < Math.min(8, list.size()); ++i3) {
            Module module = list.get(i3);
            float f24 = f17 + (float)(i3 % 2) * (f22 + f21);
            float f25 = f20 + (float)(i3 / 2) * (f23 + f21);
            if (f25 + f23 > f3 + f8 - 6.0f * f6) break;
            builder.rect(f24, f25, f22, f23, 8.0f * f6, this.cardBg);
            builder.border(f24, f25, f22, f23, 8.0f * f6, Math.max(1.0f, f6 * 0.8f), GpuClickGuiScreen.argb(26.0f, 0xFFFFFF));
            builder.text(f24 + 9.0f * f6, f25 + 8.0f * f6, 8.2f * f6, this.textColor, UiKit.fit(module.getName(), f22 - 50.0f * f6, 8.2f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
            String string = module.getDescription() == null ? "" : module.getDescription();
            float f26 = f25 + 22.0f * f6;
            for (String string2 : GpuClickGuiScreen.wrapText(string, f22 - 52.0f * f6, 5.6f * f6, 2)) {
                builder.text(f24 + 9.0f * f6, f26, 5.6f * f6, this.mutedColor, string2, UiDrawCommand.TextWeight.REGULAR);
                f26 += 7.5f * f6;
            }
            this.drawToggleSwitch(builder, GpuClickGuiScreen.bf("\ue00d\u237a\u020a\u8583\ua7a9\u00eb\u4eac\uaa96\u98e1\u2177\u43ce", 2071956914, 0x1B1EF9FC ^ CryptoVault.mask()) + i3, f24 + f22 - 36.0f * f6, f25 + (f23 - 14.0f * f6) * 0.5f, 26.0f * f6, 14.0f * f6, module.isEnabled(), f6);
        }
        builder.clipPop();
        builder.border(f2, f3, f7, f8, 9.0f * f6, Math.max(1.0f, f6), GpuClickGuiScreen.argb(64.0f, this.accentArgb()));
    }

    private void drawDropdownReplica(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5) {
        float f6;
        float f7 = Math.min(f4 / 640.0f, f5 / 400.0f);
        float f8 = 640.0f * f7;
        float f9 = 400.0f * f7;
        builder.clipPush(f2 += (f4 - f8) * 0.5f, f3 += (f5 - f9) * 0.5f, f8, f9, 9.0f * f7);
        builder.rect(f2, f3, f8, f9, 9.0f * f7, GpuClickGuiScreen.argb(85.0f, 0));
        String[] stringArray = new String[]{GpuClickGuiScreen.bf("\u63ab\u4aad\u3868\uacc8\uf4d7\ufab2\u3b32\u6bec\u6d70\uf2df", -432346341, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.bf("\u5e2f\u0dc5\ud966\uefd5\ufefd\uc69d\ubea8\uf483\u79a1\u27a7\uc31f", 216112104, 0x1B1EF9FC ^ CryptoVault.mask())};
        float f10 = f2 + f8 - 12.0f * f7;
        for (int i2 = stringArray.length - 1; i2 >= 0; --i2) {
            f6 = GpuUiFontMetrics.textWidth(stringArray[i2], 6.8f * f7, UiDrawCommand.TextWeight.REGULAR) + 18.0f * f7;
            builder.rect(f10 -= f6, f3 + 10.0f * f7, f6, 18.0f * f7, 9.0f * f7, i2 == 0 ? GpuClickGuiScreen.argb(64.0f, this.accentArgb()) : this.controlFill(112));
            builder.border(f10, f3 + 10.0f * f7, f6, 18.0f * f7, 9.0f * f7, Math.max(1.0f, f7), i2 == 0 ? GpuClickGuiScreen.argb(160.0f, this.accentArgb()) : GpuClickGuiScreen.argb(34.0f, 0xFFFFFF));
            GpuClickGuiScreen.textMid(builder, f10 + 9.0f * f7, f3 + 19.0f * f7, 6.8f * f7, i2 == 0 ? this.textColor : this.mutedColor, stringArray[i2], UiDrawCommand.TextWeight.REGULAR);
            f10 -= 6.0f * f7;
        }
        Category[] categoryArray = new Category[]{Category.CE, Category.lr, Category.yZ, Category.Be};
        f6 = 140.0f * f7;
        float f11 = (f8 - 24.0f * f7 - f6 * (float)categoryArray.length) / (float)(categoryArray.length - 1);
        for (int i3 = 0; i3 < categoryArray.length; ++i3) {
            float f12 = f2 + 12.0f * f7 + (float)i3 * (f6 + f11);
            float f13 = f3 + 40.0f * f7 + (float)(i3 % 2) * 18.0f * f7;
            List<Module> list = this.modulesForCategory(categoryArray[i3]);
            int n2 = Math.min(i3 == 0 ? 6 : 8, list.size());
            float f14 = 24.0f * f7;
            float f15 = 19.0f * f7;
            float f16 = (float)n2 * f15 + (i3 == 0 ? 40.0f * f7 : 0.0f) + 8.0f * f7;
            float f17 = Math.min(f14 + f16, f3 + f9 - 8.0f * f7 - f13);
            builder.shadow(f12, f13 + 4.0f * f7, f6, f17, 8.0f * f7, 14.0f * f7, 0.0f, GpuClickGuiScreen.argb(85.0f, 0));
            builder.clipPush(f12, f13, f6, f17, 8.0f * f7);
            builder.rect(f12, f13, f6, f17, 0.0f, 0xF0000000 | this.railBg & 0xFFFFFF);
            builder.rect(f12, f13, f6, f17, 0.0f, this.listBg);
            this.accentFill(builder, f12, f13, f6, f14, 0.0f, 0.28f, 0);
            GcsCategoryIcons.draw(builder, GcsCategoryIcons.forCategory(categoryArray[i3].name()), f12 + 8.0f * f7, f13 + 6.0f * f7, 12.0f * f7, this.textColor);
            GpuClickGuiScreen.textMid(builder, f12 + 25.0f * f7, f13 + f14 * 0.5f, 8.0f * f7, this.textColor, categoryArray[i3].zw, UiDrawCommand.TextWeight.SEMIBOLD);
            float f18 = f13 + f14 + 4.0f * f7;
            for (int i4 = 0; i4 < n2; ++i4) {
                Module module = list.get(i4);
                boolean bl = module.isEnabled();
                if (bl) {
                    builder.rect(f12 + 4.0f * f7, f18 + 3.0f * f7, 2.0f * f7, f15 - 6.0f * f7, 1.0f * f7, this.accentArgb());
                }
                GpuClickGuiScreen.textMid(builder, f12 + 11.0f * f7, f18 + f15 * 0.5f, 7.0f * f7, bl ? this.accentArgb() : this.textColor, UiKit.fit(module.getName(), f6 - 22.0f * f7, 7.0f * f7), UiDrawCommand.TextWeight.REGULAR);
                f18 += f15;
                if (i3 != 0 || i4 != 0) continue;
                builder.rect(f12 + 8.0f * f7, f18, f6 - 16.0f * f7, 36.0f * f7, 6.0f * f7, GpuClickGuiScreen.argb(48.0f, 0));
                GpuClickGuiScreen.textMid(builder, f12 + 14.0f * f7, f18 + 10.0f * f7, 6.2f * f7, this.mutedColor, GpuClickGuiScreen.bf("\u095e\uceaa\u2fc2\u89fb\ueec3\u0d2e\ucf64\u0179\u3914\u5b85", 133962150, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
                builder.rect(f12 + 14.0f * f7, f18 + 22.0f * f7, f6 - 28.0f * f7, 3.0f * f7, 1.5f * f7, GpuClickGuiScreen.argb(48.0f, 0xFFFFFF));
                this.accentFill(builder, f12 + 14.0f * f7, f18 + 22.0f * f7, (f6 - 28.0f * f7) * 0.55f, 3.0f * f7, 1.5f * f7, 1.0f, 0);
                builder.rect(f12 + 10.0f * f7 + (f6 - 28.0f * f7) * 0.55f, f18 + 19.5f * f7, 8.0f * f7, 8.0f * f7, 4.0f * f7, -1);
                f18 += 40.0f * f7;
            }
            builder.clipPop();
            builder.border(f12, f13, f6, f17, 8.0f * f7, Math.max(1.0f, f7), GpuClickGuiScreen.argb(48.0f, 0xFFFFFF));
        }
        builder.clipPop();
        builder.border(f2, f3, f8, f9, 9.0f * f7, Math.max(1.0f, f7), GpuClickGuiScreen.argb(48.0f, 0xFFFFFF));
    }

    private void drawLayoutCard(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, String string, String string2, boolean bl, boolean bl2, float f6, float f7, float f8, UiHost uiHost) {
        boolean bl3 = this.hovered(f7, f8, f2, f3, f4, f5);
        float f9 = 10.0f * f6;
        builder.rect(f2, f3, f4, f5, f9, bl ? GpuClickGuiScreen.argb(48.0f, this.accentArgb()) : GpuClickGuiScreen.argb(bl3 ? 20.0f : 10.0f, 0xFFFFFF));
        builder.border(f2, f3, f4, f5, f9, Math.max(1.0f, f6), bl ? this.accentArgb() : GpuClickGuiScreen.argb(bl3 ? 58.0f : 28.0f, 0xFFFFFF));
        List<String> list = GpuClickGuiScreen.wrapText(string2, f4 - 24.0f * f6, 8.5f * f6, 2);
        float f10 = 18.0f * f6 + (float)list.size() * 11.0f * f6 + 6.0f * f6;
        float f11 = f2 + 10.0f * f6;
        float f12 = f3 + 10.0f * f6;
        float f13 = f4 - 20.0f * f6;
        float f14 = Math.max(20.0f * f6, f5 - 10.0f * f6 - f10 - 6.0f * f6);
        builder.rect(f11, f12, f13, f14, 6.0f * f6, GpuClickGuiScreen.argb(112.0f, 0));
        if (bl2) {
            this.drawMenuReplica(builder, f11 + 4.0f * f6, f12 + 4.0f * f6, f13 - 8.0f * f6, f14 - 8.0f * f6);
        } else {
            this.drawDropdownReplica(builder, f11 + 4.0f * f6, f12 + 4.0f * f6, f13 - 8.0f * f6, f14 - 8.0f * f6);
        }
        float f15 = f12 + f14 + 6.0f * f6 + 8.0f * f6;
        GpuClickGuiScreen.textMid(builder, f2 + 12.0f * f6, f15, 11.0f * f6, bl ? this.accentArgb() : this.titleColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
        float f16 = f15 + 9.0f * f6;
        for (String string3 : list) {
            builder.text(f2 + 12.0f * f6, f16, 8.5f * f6, this.mutedColor, string3, UiDrawCommand.TextWeight.REGULAR);
            f16 += 11.0f * f6;
        }
        uiHost.hit(f2, f3, f4, f5, () -> {
            if (HitboxConfigScreen752809.U != null) {
                HitboxConfigScreen752809.U.setCurrentMode(string);
            }
        });
    }

    private float controlReserve(Setting setting, float f2) {
        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting)setting;
            if (!booleanSetting.isBindable()) {
                return 34.0f * f2 + 14.0f * f2;
            }
            String string = this.bindingBool == booleanSetting ? GpuClickGuiScreen.bf("\u476a\u45ab\uac19\ua80d\u4429\ue89f\ud517\u9e37\ucbc8\uf945\u9696\u0442\u1fd3\ub52a\u3735\uf1c4", 1985408088, 0x1B1EF9FC ^ CryptoVault.mask()) : (booleanSetting.getKeyCode() == -1 ? GpuClickGuiScreen.bf("\ua38f\u77f0\u669d\u72cf\u8443\ua255\u1593\u575b", 501488156, 0x1B1EF9FC ^ CryptoVault.mask()) : booleanSetting.getKeyDisplayName()) + (booleanSetting.getKeyCode() != -1 && booleanSetting.isKeyHoldMode() ? GpuClickGuiScreen.bf("\u766a\u13d1\u5960\u386a\u8234\u4559\ue2b4\uc70f\u9a4d\u0cce", 705797981, 0x1B1EF9FC ^ CryptoVault.mask()) : "");
            float f3 = Math.min(110.0f * f2, Math.max(46.0f * f2, GpuUiFontMetrics.textWidth(string, 9.0f * f2, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f2));
            return 34.0f * f2 + 14.0f * f2 + 8.0f * f2 + f3;
        }
        if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            float f4 = GpuClickGuiScreen.sliderValueWidth(sliderSetting, f2);
            return f4 + 14.0f * f2 + 12.0f * f2 + 60.0f * f2 + 12.0f * f2;
        }
        if (setting instanceof ColorSetting) {
            return 22.0f * f2 + 14.0f * f2 + 62.0f * f2;
        }
        if (setting instanceof HitboxKeyBinding655023) {
            HitboxKeyBinding655023 hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting;
            Module module = hitboxKeyBinding655023.a();
            String string = this.binding == hitboxKeyBinding655023 ? GpuClickGuiScreen.bf("\u193f\u8166\u27ee\uc8d6\uaf46\uc673\u79de\ub24a\uc484\u52f8\ue58f\u5a47\u0286\u7c30\u2300", 1770896526, 0x1B1EF9FC ^ CryptoVault.mask()) : (module == null ? GpuClickGuiScreen.bf("\u6de6\u944a\u939a\u235f\u0866\uf344\ube96\u9084", 1741548580, 0x1B1EF9FC ^ CryptoVault.mask()) : module.getKeyDisplayName());
            return Math.min(140.0f * f2, Math.max(64.0f * f2, GpuUiFontMetrics.textWidth(string, 9.5f * f2, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f2)) + 14.0f * f2;
        }
        if (setting instanceof StringSetting) {
            return 90.0f * f2 + 14.0f * f2;
        }
        if (setting instanceof ConfigKeybindListSetting) {
            return GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u0673\ud847\ud12d\ue1b0\u059c", -842207973, 0x1B1EF9FC ^ CryptoVault.mask()), 9.5f * f2, UiDrawCommand.TextWeight.SEMIBOLD) + 32.0f * f2 + 14.0f * f2;
        }
        return 0.0f;
    }

    protected static String sliderValueText(SliderSetting sliderSetting) {
        return GpuClickGuiScreen.sliderValueText(sliderSetting, sliderSetting.B());
    }

    private static String sliderValueText(SliderSetting sliderSetting, double d2) {
        return String.format(Locale.ROOT, "%." + sliderSetting.W() + "f", d2) + GpuClickGuiScreen.sliderUnit(sliderSetting.getName(), sliderSetting.E());
    }

    private static float sliderValueWidth(SliderSetting sliderSetting, float f2) {
        float f3 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.sliderValueText(sliderSetting, sliderSetting.D()), 10.0f * f2, UiDrawCommand.TextWeight.SEMIBOLD);
        float f4 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.sliderValueText(sliderSetting, sliderSetting.E()), 10.0f * f2, UiDrawCommand.TextWeight.SEMIBOLD);
        return Math.max(52.0f * f2, Math.max(f3, f4) + 18.0f * f2);
    }

    private static int multiCols(int n2) {
        return n2 <= 3 ? Math.max(1, n2) : (n2 <= 6 ? 3 : 4);
    }

    private void drawMultiSelect(UiDisplayList.Builder builder, MultiSelectSetting multiSelectSetting, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        int n2;
        float f9;
        float f10;
        float f11;
        List<String> list = multiSelectSetting.E();
        int n3 = list.size();
        int n4 = GpuClickGuiScreen.multiCols(n3);
        int n5 = Math.max(1, (n3 + n4 - 1) / n4);
        float f12 = Math.round(5.0f * f5);
        float f13 = Math.round(3.0f * f5);
        float f14 = Math.round(24.0f * f5);
        float f15 = Math.round(f2 + 14.0f * f5 + f13);
        float f16 = Math.round(f3 + 33.0f * f5);
        float f17 = (float)Math.round(f4 - 28.0f * f5) - f13 * 2.0f;
        float f18 = (float)n5 * f14 + (float)(n5 - 1) * f12 + f13 * 2.0f;
        float f19 = this.pillR(f14);
        builder.rect(f15 - f13, f16 - f13, f17 + f13 * 2.0f, f18, f19 + f13, 0x1C000000);
        int n6 = System.identityHashCode(multiSelectSetting);
        for (int i2 = 0; i2 < n3; ++i2) {
            float f20;
            String string = list.get(i2);
            int n7 = i2 % n4;
            float f21 = f15 + (float)Math.round((float)n7 * (f17 + f12) / (float)n4);
            f11 = f15 + (float)Math.round((float)(n7 + 1) * (f17 + f12) / (float)n4) - f12;
            f10 = f11 - f21;
            f9 = f16 + (float)(i2 / n4) * (f14 + f12);
            boolean bl = multiSelectSetting.D(string);
            boolean bl2 = this.hovered(f6, f7, f21, f9, f10, f14);
            if (bl2 && multiSelectSetting.C(string) != null) {
                this.hoveredTooltipTitle = string;
                this.hoveredTooltipDesc = multiSelectSetting.C(string);
            }
            if ((f20 = this.anim(GpuClickGuiScreen.bf("\ub5bc\u9dd0\u31be\u1e1e\uba17\u25c0\ub3f4", -136540155, 0x1B1EF9FC ^ CryptoVault.mask()) + n6 + string, bl ? 1.0f : 0.0f, 16.0f)) > 0.01f) {
                builder.shadow(f21, f9 + 2.0f * f5, f10, f14, f19, 8.0f * f5, 0.0f, GpuClickGuiScreen.argb(64.0f * f20, this.accentArgb()));
                this.accentFill(builder, f21, f9, f10, f14, f19, f20, 0);
            } else if (bl2) {
                builder.rect(f21, f9, f10, f14, f19, this.controlFill(26));
            }
            n2 = GpuClickGuiScreen.mixRgb(bl2 ? this.titleColor : this.mutedColor, -15724012, f20);
            float f22 = 3.2f * f5;
            f8 = f21 + 10.0f * f5 + f22;
            float f23 = f9 + f14 * 0.5f;
            if (bl) {
                builder.circle(f8, f23, f22, n2);
            } else {
                builder.border(f8 - f22, f23 - f22, f22 * 2.0f, f22 * 2.0f, f22, Math.max(1.0f, 0.9f * f5), n2);
            }
            float f24 = Math.max(0.0f, f10 - 24.0f * f5 - f22 * 2.0f);
            String string2 = UiKit.fit(string, f24, 9.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD);
            float f25 = GpuUiFontMetrics.textWidth(string2, 9.5f * f5, UiDrawCommand.TextWeight.SEMIBOLD);
            float f26 = f8 + f22 + 7.0f * f5 + Math.max(0.0f, (f24 - f25) * 0.5f);
            GpuClickGuiScreen.textMid(builder, f26, f9 + f14 * 0.5f, 9.5f * f5, n2, string2, UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f21, f9, f10, f14, () -> multiSelectSetting.E(string));
        }
        float f27 = f3 + 16.0f * f5;
        float f28 = Math.round(18.0f * f5);
        String string = GpuClickGuiScreen.bf("\u9896\u8080\u5be5\u1a01\u866a", 1635967677, 0x1B1EF9FC ^ CryptoVault.mask());
        String string3 = GpuClickGuiScreen.bf("\u7c64\u3d18\u848c\u0e32\u94d4\u312b", 2076429844, 0x1B1EF9FC ^ CryptoVault.mask());
        f11 = Math.round(GpuUiFontMetrics.textWidth(string, 9.0f * f5, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f5);
        f10 = Math.round(GpuUiFontMetrics.textWidth(string3, 9.0f * f5, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f5);
        f9 = Math.round(f27 - f28 * 0.5f);
        float f29 = Math.round(f2 + f4 - 14.0f * f5 - f11);
        float f30 = Math.round(f29 - 6.0f * f5 - f10);
        boolean bl = this.hovered(f6, f7, f30, f9, f10, f28);
        n2 = this.hovered(f6, f7, f29, f9, f11, f28);
        builder.rect(f30, f9, f10, f28, this.pillR(f28), this.controlFill(bl ? 42 : 26));
        GpuClickGuiScreen.textCentered(builder, f30 + f10 * 0.5f, f9 + f28 * 0.5f, 9.0f * f5, multiSelectSetting.x() ? this.accentArgb() : this.textColor, string3, UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f30, f9, f10, f28, multiSelectSetting::I);
        builder.rect(f29, f9, f11, f28, this.pillR(f28), this.controlFill(n2 != 0 ? 42 : 26));
        GpuClickGuiScreen.textCentered(builder, f29 + f11 * 0.5f, f9 + f28 * 0.5f, 9.0f * f5, multiSelectSetting.y() ? this.accentArgb() : this.textColor, string, UiDrawCommand.TextWeight.SEMIBOLD);
        this.hit(f29, f9, f11, f28, multiSelectSetting::J);
        String string4 = multiSelectSetting.V();
        f8 = GpuUiFontMetrics.textWidth(string4, 9.0f * f5, UiDrawCommand.TextWeight.REGULAR);
        GpuClickGuiScreen.textMid(builder, f30 - 10.0f * f5 - f8, f27, 9.0f * f5, this.mutedColor, string4, UiDrawCommand.TextWeight.REGULAR);
    }

    private void drawSettingRow(UiDisplayList.Builder builder, Setting setting, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        String string;
        boolean bl = this.hovered(f7, f8, f2, f3, f4, f5);
        if (this.showSeparators) {
            builder.rect(f2 + 14.0f * f6, f3 + f5 - Math.max(1.0f, f6), f4 - 28.0f * f6, Math.max(1.0f, f6), 0.0f, this.lineSubtleColor);
        }
        boolean bl2 = setting.isModified();
        float f9 = setting instanceof ModeSetting || setting instanceof MultiSelectSetting ? f3 + 16.0f * f6 : f3 + f5 * 0.5f;
        float f10 = this.anim(this.animKey(setting, 4, GpuClickGuiScreen.bf("\ufbba\u4228\uabb5\ue0b6\udce0\u98b4\u7ba1\ue3e6", 666241570, 0x1B1EF9FC ^ CryptoVault.mask())), bl ? 1.0f : 0.0f, 16.0f);
        if (f10 > 0.01f) {
            builder.rect(f2 + 4.0f * f6, f3 + 2.0f * f6, f4 - 8.0f * f6, f5 - 4.0f * f6, Math.max(0.0f, this.cardRadius - 4.0f) * f6, this.hoverWash(Math.round(16.0f * f10)));
        }
        if (bl) {
            String string2;
            string = GpuClickGuiScreen.safeText(setting.getDescription()).trim();
            String string3 = bl2 ? GpuClickGuiScreen.bf("\u3e5e\uc84a\ua1db\u9cd4\ue1a4\ue73e\u15b4\u0676\u9cb1\u4291\ue155\u1cb1\u0f1a\ude4d\u51bc\uc66f\uaa3c\u2fca\uc4db\ued9e\uee08\u5d8a\udb60\uf7ad\u6344\u8276\u6716\ua1eb\u739d\u0cc7\u6794\u3e23\uc2a3\u200c\u866f\ud3a1\u9eac\u2181\uda4e\ub12b\ucb07\uda7d\ub400\u84cb\ua446\ua4e8\u77c1\u181e\u678d\u497c\ud36c\u54be\u45db\u4499\u4b4d\u3965\u1e7a\uaa77\uebe0", 738021142, 0x1B1EF9FC ^ CryptoVault.mask()) : (string2 = GpuClickGuiScreen.pinnable(setting) ? GpuClickGuiScreen.bf("\u2c5d\u07b6\ucc8a\uea9b\u6d7d\ue169\u4a27\u90d6\u53f0\uc244\ufb41\u6922\u5bba\u9ac4\u21df\uda0e\u9a2b\u42d4\u0914\ue994\u5e85\u0926\u7e35\u6c2f\uafe2\u7db4\ucf8c\u5718\u6fb0\u5b4b\ufea6\u6d0a\ub941\u3780\u2d4d\u28e0\u4f12\u1273\ud4e5\ubdd1\u7f09\u5033", 245102161, 0x1B1EF9FC ^ CryptoVault.mask()) : "");
            if (!string2.isEmpty()) {
                String string4 = string = string.isEmpty() ? string2 : string + " " + string2;
            }
            if (!string.isEmpty()) {
                this.hoveredTooltipTitle = setting.getName();
                this.hoveredTooltipDesc = string;
            }
        }
        this.hitModuleCardRight(f2, f3, f4, f5, () -> this.openSettingMenu(setting));
        String string5 = string = setting instanceof SliderSetting ? GpuClickGuiScreen.stripUnitSuffix(setting.getName()) : setting.getName();
        if (this.rowLabelPrefix != null) {
            string = this.rowLabelPrefix + string;
        }
        float f11 = (bl2 ? 9.0f * f6 : 0.0f) + (setting.isExtended() ? GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u0b3e\u7c7e\u4c0f\u385e\u591a\ufebf\u8efe\u5225\ubec3\u81a3\ub9bd", -108803181, 0x1B1EF9FC ^ CryptoVault.mask()), 8.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f6 : 0.0f);
        float f12 = setting instanceof MultiSelectSetting ? Math.max(0.0f, f4 - 32.0f * f6 - f11 - 150.0f * f6) : (setting instanceof ModeSetting ? Math.max(0.0f, f4 - 32.0f * f6 - f11) : Math.max(0.0f, f4 - 16.0f * f6 - this.controlReserve(setting, f6) - 10.0f * f6 - f11));
        string = UiKit.fit(string, f12, 11.5f * f6);
        GpuClickGuiScreen.textMid(builder, f2 + 16.0f * f6, f9, 11.5f * f6, this.textColor, string, UiDrawCommand.TextWeight.REGULAR);
        float f13 = f2 + 16.0f * f6 + GpuUiFontMetrics.textWidth(string, 11.5f * f6) + 6.0f * f6;
        float f14 = Float.NaN;
        if (bl2) {
            f14 = f13 + 2.0f * f6;
            boolean bl3 = this.hovered(f7, f8, f14 - 7.0f * f6, f9 - 7.0f * f6, 14.0f * f6, 14.0f * f6);
            if (bl3) {
                builder.circle(f14, f9, 5.5f * f6, GpuClickGuiScreen.withA(this.accentArgb(), 64));
                builder.circle(f14, f9, 3.0f * f6, this.accentArgb());
                this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\u0cf3\u5923\u99be\u9e79\u0104\u344c\ub3cc\uc164\u4db9\uadb3", 245435081, 0x1B1EF9FC ^ CryptoVault.mask()) + setting.getName();
                this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\ubd3e\u10d1\u4328\ud215\u72d5\uf423\uf558\uec10\ubd8c\u0fb8\uccb4\u6ffe\ub4ad\u9ce2\ud081\u16fc\u723f\u06be\u3532\uf1d3\uab75\uf9a4\u660c\u3b76\udebd\u6a32\uc2c4\u4d4f\u243f\u8731\ube32\ud028\u75fb\ue403\ua5b7\ucc01\u7a22\u6c55\u4510\u1a65\udf71\u54d5\ub588\uce17\u304c\uf320\u7758\udfe9\ueaf3", -310577382, 0x1B1EF9FC ^ CryptoVault.mask());
            } else {
                builder.circle(f14, f9, 2.0f * f6, this.accentArgb());
            }
            f13 += 9.0f * f6;
        }
        if (setting.isExtended()) {
            float f15 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u0345\ud8a6\uf757\u93ce\u61f6\u9f46\u3172\uab3e\ub366\ua5b2", -555305738, 0x1B1EF9FC ^ CryptoVault.mask()), 8.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 12.0f * f6;
            float f16 = Math.round(14.0f * f6);
            float f17 = Math.round(f9 - f16 * 0.5f);
            builder.border(f13, f17, f15, f16, f16 * 0.5f, Math.max(1.0f, 0.8f * f6), -1711306102);
            GpuClickGuiScreen.textCentered(builder, f13 + f15 * 0.5f, f17 + f16 * 0.5f, 8.0f * f6, -25958, GpuClickGuiScreen.bf("\ua92e\uc427\ue332\u460e\u5889\u8695\u561c\ud40c\u575e", -1285121315, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            f13 += f15 + 6.0f * f6;
        }
        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting)setting;
            float f18 = 34.0f * f6;
            float f19 = 18.0f * f6;
            float f20 = f2 + f4 - f18 - 14.0f * f6;
            float f21 = f3 + (f5 - f19) * 0.5f;
            this.drawToggleSwitch(builder, this.animKey(setting, 5, GpuClickGuiScreen.bf("\u3021\ud99f\ub6d7\u0236\u251e\u277d", 1398356534, 0x1B1EF9FC ^ CryptoVault.mask())), f20, f21, f18, f19, booleanSetting.isEnabled(), f6);
            this.hit(f2, f3, f4, f5, booleanSetting::toggle);
            if (booleanSetting.isBindable()) {
                boolean bl4;
                boolean bl5 = bl4 = this.bindingBool == booleanSetting;
                String string6 = bl4 ? GpuClickGuiScreen.bf("\u2b41\u9696\ud969\u05a6\u3347\u237b\u2b82\u4399\u9d9f\ue737\u509f\ub98b\u0385\u18b6\u3ede\u79c6", 288863301, 0x1B1EF9FC ^ CryptoVault.mask()) : (booleanSetting.getKeyCode() == -1 ? GpuClickGuiScreen.bf("\u0e37\u8240\u3b78\ua0f0\ufd7d\ubf65", 111474806, 0x1B1EF9FC ^ CryptoVault.mask()) : booleanSetting.getKeyDisplayName()) + (booleanSetting.getKeyCode() != -1 && booleanSetting.isKeyHoldMode() ? GpuClickGuiScreen.bf("\uf753\uf80f\u5b3d\u4747\u384c\u4d1b\u3b0a\u98bf\u072d\u81cd\u7fa6", 1803357225, 0x1B1EF9FC ^ CryptoVault.mask()) : "");
                float f22 = Math.round(20.0f * f6);
                float f23 = Math.round(Math.min(110.0f * f6, Math.max(46.0f * f6, GpuUiFontMetrics.textWidth(string6, 9.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f6)));
                float f24 = Math.round(f20 - 8.0f * f6 - f23);
                float f25 = Math.round(f3 + (f5 - f22) * 0.5f);
                boolean bl6 = this.hovered(f7, f8, f24, f25, f23, f22);
                builder.rect(f24, f25, f23, f22, this.pillR(f22), bl4 ? this.controlFill(72) : this.controlFill(bl6 ? 42 : 26));
                GpuClickGuiScreen.textCentered(builder, f24 + f23 * 0.5f, f25 + f22 * 0.5f, 9.0f * f6, bl4 ? this.accentArgb() : (booleanSetting.getKeyCode() == -1 ? this.mutedColor : this.textColor), UiKit.fit(string6, f23 - 10.0f * f6, 9.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
                this.hit(f24, f25, f23, f22, () -> {
                    this.cancelKeybindCapture();
                    this.bindingBool = booleanSetting;
                });
                this.hitModuleCardRight(f24, f25, f23, f22, () -> booleanSetting.setKeyHoldMode(!booleanSetting.isKeyHoldMode()));
                if (bl6) {
                    this.hoveredTooltipTitle = setting.getName() + GpuClickGuiScreen.bf("\u5717\ub05e\u4345\u4536\u1860\u331e\ufa57", -1322977697, 0x1B1EF9FC ^ CryptoVault.mask());
                    this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\ua261\u228d\ua7e8\u97ef\u22e3\u62a1\u4f68\u9974\u4e04\u0b7d\u3c7b\u9f4c\u28b5\ufbc0\u2846\ubd98\u324c\u0930\ue54e\u6df3\ud07b\u5a59\uca70\ue813\u9aaf\u196a\ud55d\u13bf\u71f0\u9e75\u7bf1\uacbf\uef34\ue153\u9820\u733b\uc168\u367f\u92e8\u4870\u357e\ue153\uf046\ucd07\u1e38\ud2e2\ua845\u4b8b\u557c\ue625\u57a3\u20f7\udd0b\u23ba\u6275\u5bf8\u5706\u4112\uab08\u5b29\u7809\u78cb\ud227\u5393\u5708\u3f2d", -465271298, 0x1B1EF9FC ^ CryptoVault.mask()) + (booleanSetting.isKeyHoldMode() ? GpuClickGuiScreen.bf("\u789a\u069c\u0e79\udc67\u73e7\u4134\u7cba\u2318\u7ab9\ud68b\u0622\u960f\uabb5\u7ee8\u7629\u3673\u2b08\ufe78\u1c7d\u46d0", 1488686796, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u66aa\udb9c\u05bc\u19df\u16b2\u4dfb\u09c2\u88a3\u6c7c\ufdba\u7d96\u81d6\u640b\u16f0\u5d15\u18c1\u5a14\u701b\u298b\u0fd7\u2ac1\u2ab9\u926e\ud8ca\u0d53\uba8f\u3491\u8950\ue746\ucd87\u6152\u23f3\u9ee4\u38fe\u6b4d\u8472\u2867", -405656177, 0x1B1EF9FC ^ CryptoVault.mask()));
                }
            }
        } else if (setting instanceof SliderSetting) {
            boolean bl7;
            boolean bl8;
            SliderSetting sliderSetting = (SliderSetting)setting;
            String string7 = GpuClickGuiScreen.sliderValueText(sliderSetting);
            float f26 = Math.round(GpuClickGuiScreen.sliderValueWidth(sliderSetting, f6));
            float f27 = Math.round(20.0f * f6);
            float f28 = Math.round(f2 + f4 - f26 - 14.0f * f6);
            float f29 = Math.round(f3 + (f5 - f27) * 0.5f);
            float f30 = f13 + 12.0f * f6;
            float f31 = f28 - 12.0f * f6;
            float f32 = Math.max(40.0f * f6, Math.min(180.0f * f6, f31 - f30));
            float f33 = Math.max(f30, f31 - f32);
            f32 = Math.max(1.0f * f6, Math.min(f32, f31 - f33));
            boolean bl9 = bl8 = this.rowLabelPrefix != null;
            if (this.draggingSlider == sliderSetting && this.draggingSliderQuick == bl8) {
                this.sliderX = f33;
                this.sliderW = f32;
            }
            float f34 = Math.max(2.0f, (float)Math.round(5.0f * f6));
            float f35 = Math.round(f3 + (f5 - f34) * 0.5f);
            double d2 = sliderSetting.E() - sliderSetting.D();
            double d3 = d2 == 0.0 ? 0.0 : (sliderSetting.B() - sliderSetting.D()) / d2;
            d3 = Math.max(0.0, Math.min(1.0, d3));
            float f36 = (float)((double)f32 * d3);
            builder.rect(f33, f35, f32, f34, f34 * 0.5f, this.controlFill(36));
            if (f36 > 0.5f) {
                builder.shadow(f33, f35 + 1.0f * f6, Math.max(f34, f36), f34, this.pillR(f34), 8.0f * f6, 0.0f, GpuClickGuiScreen.argb(80.0f, this.accentArgb()));
                this.accentFill(builder, f33, f35, Math.max(f34, f36), f34, this.pillR(f34), 1.0f, this.sliderFillColor);
            }
            float f37 = f33 + f36;
            float f38 = f35 + f34 * 0.5f;
            boolean bl10 = bl7 = this.draggingSlider == sliderSetting || this.hovered(f7, f8, f33 - 6.0f * f6, f3, f32 + 12.0f * f6, f5);
            if (bl7) {
                builder.circle(f37, f38, 9.5f * f6, this.accentArgb() & 0xFFFFFF | 0x26000000);
            }
            builder.circle(f37, f38 + 1.0f * f6, 7.5f * f6, 0x45000000);
            float f39 = this.anim(this.animKey(sliderSetting, 5, GpuClickGuiScreen.bf("\u14e0\uba45\u852e\ucc36\uf010\ua8bc", 225754861, 0x1B1EF9FC ^ CryptoVault.mask())), bl7 ? 1.0f : 0.0f, 18.0f);
            builder.circle(f37, f38, 6.5f * f6 + f39 * 1.0f * f6, GpuClickGuiScreen.mixRgb(-723208, 0xFF000000 | this.controlRgb, 0.12f));
            if (this.sliderEdit.editing(sliderSetting, bl8)) {
                this.sliderEdit.draw(builder, this.kitHost, f28, f29, f26, f27, f6);
            } else {
                boolean bl11 = this.hovered(f7, f8, f28, f29, f26, f27);
                builder.rect(f28, f29, f26, f27, f27 * 0.5f, this.controlFill(bl11 ? 52 : 36));
                GpuClickGuiScreen.textCentered(builder, f28 + f26 * 0.5f, f29 + f27 * 0.5f, 10.0f * f6, this.accentArgb(), string7, UiDrawCommand.TextWeight.SEMIBOLD);
                this.hit(f28, f29, f26, f27, () -> this.sliderEdit.begin(this.kitHost, sliderSetting, bl8));
                if (bl11) {
                    this.hoveredTooltipTitle = setting.getName();
                    this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\u99d0\u50a4\u7252\uaa8c\ud9e5\u91c6\u3254\u0eb3\u66d6\u0407\u12cb\u6a77\ub27f\u8a32\u8feb\uf7f3\u40e2\uc937\u7f00\u41f8\u5f04\u3abe\u82d7\ua2f6\ue46c\ue342\ufa9b\u44ce\u8f61\ud6d1\u574c\u6323\u3f88\u3bcf\u2f79\u840d\ucff4\u3504\ue0d5\u807b\ud54d\ud851\u3e2b\u97d4\u5baf\u01ac\u6a49\ua196\u5f2c\ubfe3\u5050\u294d\uda6f\u781e\u2ef6\u4e7c\uc86d\u2d28\u6ce4\ud784\u7dc0\u90ce\u95f4\u0c27\ufcb7\ue86f\ufeb4\u74c5\ua7e2\u1806", -1949812158, 0x1B1EF9FC ^ CryptoVault.mask());
                }
            }
            float f40 = f33;
            float f41 = f32;
            this.hit(f33 - 6.0f * f6, f3, f32 + 12.0f * f6, f5, () -> {
                this.draggingSlider = sliderSetting;
                this.draggingSliderQuick = bl8;
                this.sliderX = f40;
                this.sliderW = f41;
                if (f41 > 0.0f) {
                    double d2 = GpuClickGuiScreen.clamp01((float)((this.pressX - (double)f40) / (double)f41));
                    sliderSetting.B(sliderSetting.D() + d2 * (sliderSetting.E() - sliderSetting.D()));
                }
            });
        } else if (setting == HitboxConfigScreen752809.U) {
            this.drawLayoutChooserRow(builder, f2, f3, f4, f5, f6, f7, f8);
        } else if (setting instanceof ModeSetting) {
            int n2;
            float f42;
            float f43;
            ModeSetting modeSetting = (ModeSetting)setting;
            float f44 = Math.round(f2 + 14.0f * f6 + 3.0f * f6);
            float f45 = Math.round(f3 + 33.0f * f6);
            int n3 = modeSetting.getModes().size();
            int n4 = Math.max(1, n3 <= 4 ? n3 : (n3 <= 6 ? 3 : 4));
            int n5 = Math.max(1, (n3 + n4 - 1) / n4);
            float f46 = Math.round(5.0f * f6);
            float f47 = Math.round(3.0f * f6);
            float f48 = (float)Math.round(f4 - 28.0f * f6) - f47 * 2.0f;
            float f49 = Math.round(26.0f * f6);
            float f50 = f48 + f47 * 2.0f;
            float f51 = (float)n5 * f49 + (float)(n5 - 1) * f46 + f47 * 2.0f;
            float f52 = f44 - f47;
            float f53 = f45 - f47;
            float f54 = this.pillR(f49);
            builder.rect(f52, f53, f50, f51, f54 + f47, 0x1C000000);
            float[] fArray = new float[n3 + 1];
            if (n5 == 1) {
                float[] fArray2 = new float[n3];
                f43 = 0.0f;
                for (int i2 = 0; i2 < n3; ++i2) {
                    fArray2[i2] = GpuUiFontMetrics.textWidth(modeSetting.getModes().get(i2), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 22.0f * f6;
                    f43 += fArray2[i2];
                }
                f42 = f48 - f46 * (float)(n3 - 1);
                float f55 = (f42 - f43) / (float)n3;
                float f56 = 0.0f;
                for (n2 = 0; n2 < n3; ++n2) {
                    fArray[n2] = f44 + (float)Math.round(f56);
                    f56 += (f55 >= 0.0f ? fArray2[n2] + f55 : fArray2[n2] * f42 / f43) + f46;
                }
                fArray[n3] = f44 + (float)Math.round(f56);
            }
            float f57 = Float.NaN;
            f43 = 0.0f;
            f42 = 0.0f;
            int n6 = System.identityHashCode(modeSetting);
            for (int i3 = 0; i3 < 2; ++i3) {
                if (i3 == 1 && !Float.isNaN(f57)) {
                    float f58 = f44 + this.spring(GpuClickGuiScreen.bf("\u2535\ucf59\u9c68\u1c07\u5242\u7614f\udb77", -800024673, 0x1B1EF9FC ^ CryptoVault.mask()) + n6, f57 - f44, 420.0f, 30.0f);
                    float f59 = f45 + this.spring(GpuClickGuiScreen.bf("\u416c\ubc44\u80ab\u328e\ue0b0\ue8c8", 628599989, 0x1B1EF9FC ^ CryptoVault.mask()) + n6, f43 - f45, 420.0f, 30.0f);
                    float f60 = this.spring(GpuClickGuiScreen.bf("\u62cb\udff7\uc3c7\u3d23\u47f4\u3b71\u8d2c\udf19", -1256577796, 0x1B1EF9FC ^ CryptoVault.mask()) + n6, f42, 420.0f, 30.0f);
                    if (this.reducedMotion()) {
                        f58 = f57;
                        f59 = f43;
                        f60 = f42;
                    }
                    builder.shadow(f58, f59 + 2.0f * f6, f60, f49, f54, 10.0f * f6, 0.0f, GpuClickGuiScreen.argb(64.0f, this.accentArgb()));
                    this.accentFill(builder, f58, f59, f60, f49, f54, 1.0f, 0);
                    builder.rect(f58 + f54, f59 + 0.5f * f6, Math.max(0.0f, f60 - f54 * 2.0f), Math.max(1.0f, f6), 0.5f, 0x38FFFFFF);
                }
                for (n2 = 0; n2 < n3; ++n2) {
                    float f61;
                    String string8 = modeSetting.getModes().get(n2);
                    int n7 = n2 % n4;
                    float f62 = f61 = n5 == 1 ? fArray[n2] : f44 + (float)Math.round((float)n7 * (f48 + f46) / (float)n4);
                    float f63 = n5 == 1 ? (n2 == n3 - 1 ? f44 + f48 : fArray[n2 + 1] - f46) : f44 + (float)Math.round((float)(n7 + 1) * (f48 + f46) / (float)n4) - f46;
                    float f64 = f63 - f61;
                    float f65 = f45 + (float)(n2 / n4) * (f49 + f46);
                    boolean bl12 = string8.equals(modeSetting.getCurrentMode());
                    boolean bl13 = this.hovered(f7, f8, f61, f65, f64, f49);
                    float f66 = this.anim(GpuClickGuiScreen.bf("\u70f3\u1f14\u4ce3\u1b3d\u5dea\u7446\u364f", 959035421, 0x1B1EF9FC ^ CryptoVault.mask()) + System.identityHashCode(modeSetting) + string8, bl12 ? 1.0f : 0.0f, 16.0f);
                    if (i3 == 0) {
                        if (!bl12) continue;
                        f57 = f61;
                        f43 = f65;
                        f42 = f64;
                        continue;
                    }
                    if (!bl12 && bl13) {
                        builder.rect(f61, f65, f64, f49, f54, this.hoverK > 0.0f ? this.controlFill(Math.round(26.0f * Math.min(1.5f, this.hoverK))) : 0);
                    }
                    int n8 = GpuClickGuiScreen.mixRgb(bl13 ? this.titleColor : this.mutedColor, -15724012, f66);
                    String string9 = UiKit.fit(string8, Math.max(0.0f, f64 - 12.0f * f6), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
                    GpuClickGuiScreen.textCentered(builder, f61 + f64 * 0.5f, f65 + f49 * 0.5f, 9.5f * f6, n8, string9, UiDrawCommand.TextWeight.SEMIBOLD);
                    this.hit(f61, f65, f64, f49, () -> modeSetting.setCurrentMode(string8));
                }
            }
        } else if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            this.drawMultiSelect(builder, multiSelectSetting, f2, f3, f4, f6, f7, f8);
        } else if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            float f67 = Math.round(22.0f * f6);
            float f68 = Math.round(f2 + f4 - f67 - 14.0f * f6);
            float f69 = Math.round(f3 + (f5 - f67) * 0.5f);
            Integer n9 = colorSetting.isModified() ? null : this.themeValueFor(colorSetting);
            int n10 = n9 != null ? n9.intValue() : colorSetting.A().getRGB();
            builder.circle(f68 + f67 * 0.5f, f69 + f67 * 0.5f, f67 * 0.5f, n10);
            String string10 = colorSetting.v() ? GpuClickGuiScreen.bf("\u5d19\udd6d\u1bf7\ud616\u4354\u94cd\uad6d\ua2e9\ue8c9\uda92", 1330283559, 0x1B1EF9FC ^ CryptoVault.mask()) : (n9 != null ? GpuClickGuiScreen.bf("\u4cd0\u6156\ubf53\u19cb\u9e3c\ub3c7\ueb1e", -1048813246, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.hexOf(colorSetting.B().getRGB(), false));
            GpuClickGuiScreen.textMid(builder, f68 - 8.0f * f6 - GpuUiFontMetrics.textWidth(string10, 9.5f * f6), f3 + f5 * 0.5f, 9.5f * f6, this.mutedColor, string10, UiDrawCommand.TextWeight.REGULAR);
            builder.border(f68, f69, f67, f67, f67 * 0.5f, Math.max(1.0f, (float)Math.round(f6)), 0x33FFFFFF);
            this.hit(f2, f3, f4, f5, () -> this.openColorPicker(colorSetting));
        } else if (setting instanceof HitboxKeyBinding655023) {
            String string11;
            boolean bl14;
            HitboxKeyBinding655023 hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting;
            Module module = hitboxKeyBinding655023.a();
            boolean bl15 = this.binding == hitboxKeyBinding655023;
            boolean bl16 = bl14 = module != null && module.getKeyCode() != -1 && !module.isOneShot() && !module.isKeyTriggered() && !(module instanceof HitboxConfigScreen752809);
            String string12 = bl15 ? GpuClickGuiScreen.bf("\u9707\u96e8\u7109\ufb80\u4cbd\u0f5c\ud460\u51a1\uf9f7\u9946\u5fe7\u5f56\ua1f9", -1817379552, 0x1B1EF9FC ^ CryptoVault.mask()) : (module == null ? GpuClickGuiScreen.bf("\ufcab\u9dcf\ua3a6\u36bd\ub9a8", -1714043614, 0x1B1EF9FC ^ CryptoVault.mask()) : (string11 = module.getKeyDisplayName() + (bl14 && module.isHoldMode() ? GpuClickGuiScreen.bf("\u01f4\u75bd\uc962\u8bd6\u2bb3\u1303\u977c\u6e5e\ude24\u5147", -878773396, 0x1B1EF9FC ^ CryptoVault.mask()) : "")));
            if (bl14) {
                this.hitModuleCardRight(f2, f3, f4, f5, () -> module.setHoldMode(!module.isHoldMode()));
                if (this.hovered(f7, f8, f2, f3, f4, f5)) {
                    this.hoveredTooltipTitle = GpuClickGuiScreen.bf("\ub12c\ud05f\u1b42\u84a3\u715b\ue46d\u2922\ucf10\uf884", 912561176, 0x1B1EF9FC ^ CryptoVault.mask());
                    this.hoveredTooltipDesc = GpuClickGuiScreen.bf("\ua2be\uf6a1\u0169\ud18b\ua808\uf154\uf777\u292b\u4a6f\ud431\u1e93\udd51\u5842\u5aff\uaf71\udcad\ua6bd\uaed0\u5c96\u9250\u0c68\u0f57\u5120\u6de8\ubf0a\u045c\ua2c4\u1258\u49ba\u44ea\u9416\ucc5a\u802f\u51c9\u7521\u61a2\u5142\ub1dc\u3765\u731e\u96b5\u986e\ua1e4\u2fed\u6542\u9d2b\u349e\u51c5\u72d1\ua99c\u7031", 1424278025, 0x1B1EF9FC ^ CryptoVault.mask()) + (module.isHoldMode() ? GpuClickGuiScreen.bf("\u8c36\u1392\u8baf\u2e67\u1795\uf250\u3b11\u8230\u30a2\uf86c\u46d5\u06c6\ufac6\ufc7d\u71f3\u7b64\u9004\uedeb\u73f8\u7c89", -704310255, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u45fa\u530a\ua49b\u3faa\u9496\u9bd9\ua1af\u2bd2\u6099\u5ef0\ua161\uf95f\u1ae8\ub08c\ud724\u5f7e\u8282\u70cc\ub29d\u6591\ubff5\u1de1\ua2d8\ue31a\u6654\u5b2e\u364f\u2477\u7428\udef7\ude30\uf7a4\u6d77\u570e\ubb6e\ue845\uc328\u4418", -556243628, 0x1B1EF9FC ^ CryptoVault.mask()));
                }
            }
            float f70 = Math.round(24.0f * f6);
            float f71 = Math.round(Math.min(140.0f * f6, Math.max(64.0f * f6, GpuUiFontMetrics.textWidth(string11, 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f6)));
            float f72 = Math.round(f2 + f4 - f71 - 14.0f * f6);
            float f73 = Math.round(f3 + (f5 - f70) * 0.5f);
            builder.rect(f72, f73, f71, f70, f70 * 0.5f, bl15 ? this.controlFill(72) : this.controlFill(26));
            GpuClickGuiScreen.textCentered(builder, f72 + f71 * 0.5f, f73 + f70 * 0.5f, 9.5f * f6, bl15 ? this.accentArgb() : this.textColor, UiKit.fit(string11, f71 - 16.0f * f6, 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f2, f3, f4, f5, () -> this.beginSettingKeyCapture(hitboxKeyBinding655023));
        } else if (setting instanceof StringSetting) {
            StringSetting stringSetting = (StringSetting)setting;
            float f74 = Math.round(24.0f * f6);
            float f75 = Math.round(Math.max(0.0f, Math.min(160.0f * f6, f2 + f4 - 14.0f * f6 - (f13 + 8.0f * f6))));
            float f76 = Math.round(f2 + f4 - f75 - 14.0f * f6);
            float f77 = Math.round(f3 + (f5 - f74) * 0.5f);
            builder.rect(f76, f77, f75, f74, f74 * 0.5f, stringSetting.Y() ? this.controlFill(50) : this.controlFill(26));
            String string13 = stringSetting.X().isEmpty() ? GpuClickGuiScreen.bf("\u082c\ue14c\u83b9\u53ac\u8dda\u9431\udff2\u9ad9\u2993\u81db\u95f2\ued29\u8127\u4f28\uf467", -1859058856, 0x1B1EF9FC ^ CryptoVault.mask()) : stringSetting.X();
            float f78 = Math.max(0.0f, f75 - 22.0f * f6);
            GpuClickGuiScreen.textMid(builder, f76 + 11.0f * f6, f77 + f74 * 0.5f, 10.0f * f6, stringSetting.Y() ? this.accentArgb() : this.textColor, stringSetting.Y() ? GpuClickGuiScreen.fitTail(string13, f78, 10.0f * f6) : UiKit.fit(string13, f78, 10.0f * f6), UiDrawCommand.TextWeight.REGULAR);
            this.hit(f2, f3, f4, f5, () -> this.beginEdit(stringSetting));
        } else if (setting instanceof ConfigKeybindListSetting) {
            float f79 = Math.round(24.0f * f6);
            float f80 = Math.round(GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u7a57\ub7d6\u3f07\ubc23\u2ca3\u460e", -1278246203, 0x1B1EF9FC ^ CryptoVault.mask()), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 32.0f * f6);
            float f81 = Math.round(f2 + f4 - f80 - 14.0f * f6);
            float f82 = Math.round(f3 + (f5 - f79) * 0.5f);
            builder.rect(f81, f82, f80, f79, f79 * 0.5f, this.controlFill(26));
            GpuClickGuiScreen.textCentered(builder, f81 + f80 * 0.5f, f82 + f79 * 0.5f, 9.5f * f6, this.textColor, GpuClickGuiScreen.bf("\ub2e4\u3af8\u46cb\u559d\uf75f\u7eae", 1107508835, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f2, f3, f4, f5, () -> {
                this.settingsDrawerOpen = true;
                this.themesDrawerOpen = false;
                this.setSettingsTab(SettingsTab.CONFIG_BINDS);
                this.refreshSavedConfigs();
            });
        }
        if (!Float.isNaN(f14)) {
            this.hit(f14 - 7.0f * f6, f9 - 7.0f * f6, 14.0f * f6, 14.0f * f6, setting::resetToDefault);
        }
    }

    private void drawDrawerContent(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10;
        float f11;
        if (this.themesDrawerOpen && this.themeMakerOpen) {
            this.themeMaker.draw(builder, this.kitHost, f2, f3, f4, f5, f6, f7, f8, true, this::drawThemeMakerEditor);
            return;
        }
        if (this.themesDrawerOpen) {
            this.drawThemesDrawer(builder, f2, f3, f4, f5, f6, f7, f8);
            return;
        }
        float f12 = 18.0f * f6;
        float f13 = f3 + 18.0f * f6;
        builder.text(f2 + f12, f13 + 2.0f * f6, 15.0f * f6, this.titleColor, GpuClickGuiScreen.bf("\u8154\ucb83\uc655\ucaa2\u998b\ua91b\ud119\ufb07\ub2e4\uc2cf", -1302444194, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f14 = Math.round(f2 + f12);
        float f15 = Math.round(f13 + 28.0f * f6);
        float f16 = Math.round(f4 - 2.0f * f12);
        float f17 = Math.round(30.0f * f6);
        float f18 = Math.max(2.0f, (float)Math.round(3.0f * f6));
        builder.rect(f14, f15, f16, f17, f17 * 0.5f, 0x38000000);
        builder.border(f14, f15, f16, f17, f17 * 0.5f, Math.max(1.0f, (float)Math.round(f6)), this.lineBorderColor);
        SettingsTab[] settingsTabArray = SettingsTab.values();
        float f19 = f16 - f18 * 2.0f;
        float f20 = f17 - f18 * 2.0f;
        for (int i2 = 0; i2 < settingsTabArray.length; ++i2) {
            SettingsTab settingsTab = settingsTabArray[i2];
            f11 = f14 + f18 + (float)Math.round((float)i2 * f19 / (float)settingsTabArray.length);
            f10 = f14 + f18 + (float)Math.round((float)(i2 + 1) * f19 / (float)settingsTabArray.length) - f11;
            f9 = f15 + f18;
            boolean bl = this.settingsTab == settingsTab;
            boolean bl2 = this.hovered(f7, f8, f11, f15, f10, f17);
            if (bl) {
                builder.rect(f11, f9, f10, f20, f20 * 0.5f, 0x24FFFFFF);
                builder.border(f11, f9, f10, f20, f20 * 0.5f, Math.max(1.0f, (float)Math.round(f6)), 0x33FFFFFF);
            } else if (bl2) {
                builder.rect(f11, f9, f10, f20, f20 * 0.5f, 0xFFFFFFF);
            }
            String string = settingsTab == SettingsTab.CLIENT_CONTROLS ? GpuClickGuiScreen.bf("\ueade\u70d2\u529f\uddcd\u5057\ufed3\u47ae", -1055312125, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\uf092\ud445\u08a5\uc90d\u95d8\u50ba\u112f\uc566", 564640589, 0x1B1EF9FC ^ CryptoVault.mask());
            String string2 = UiKit.fit(string, Math.max(0.0f, f10 - 12.0f * f6), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            GpuClickGuiScreen.textCentered(builder, f11 + f10 * 0.5f, f9 + f20 * 0.5f, 9.5f * f6, bl ? this.accentArgb() : (bl2 ? this.textColor : this.mutedColor), string2, UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f11, f15, f10, f17, () -> {
                if (this.settingsTab != settingsTab) {
                    this.settingsTabSwitchAt = this.nanoTime();
                }
                this.setSettingsTab(settingsTab);
                this.appearanceScroll = 0.0f;
                if (settingsTab == SettingsTab.CONFIG_BINDS) {
                    this.refreshSavedConfigs();
                }
            });
        }
        float f21 = f15 + f17 + 14.0f * f6;
        float f22 = f2 + f12;
        f11 = f4 - 2.0f * f12;
        if (this.settingsTab == SettingsTab.CLIENT_CONTROLS) {
            this.drawClientSettings(builder, f22, f21, f11, f3 + f5 - 14.0f * f6, f6, f7, f8);
        } else {
            this.drawConfigKeybinds(builder, f22, f21, f11, f3 + f5 - 14.0f * f6, f6);
        }
        f10 = (float)(this.nanoTime() - this.settingsTabSwitchAt) / 1.0E9f;
        if (!this.reducedMotion() && this.settingsTabSwitchAt != 0L && f10 >= 0.0f && f10 < 0.2f) {
            f9 = 1.0f - f10 / 0.2f;
            builder.rect(f2, f21, f4, f3 + f5 - f21, 0.0f, GpuClickGuiScreen.argb(168.0f * f9 * f9, 724758));
        }
    }

    private float drawAppearanceGroups(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7) {
        for (Map.Entry<String, List<Setting>> entry : HitboxConfigScreen752809.f().entrySet()) {
            f3 = this.drawAppearanceSettingGroup(builder, entry.getKey(), entry.getValue(), f2, f3, f4, f5, f6, f7);
        }
        return f3;
    }

    private void drawThemesDrawer(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = 18.0f * f6;
        float f10 = f3 + 18.0f * f6;
        builder.text(f2 + f9, f10 + 2.0f * f6, 15.0f * f6, this.titleColor, GpuClickGuiScreen.bf("\u3d38\ud2d0\u66b8\u5f16\u02b4\u2683\uad0c\u3a4e\u7d10\u5c1b\u0337\u5b1d", 98691980, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f11 = f2 + f9;
        float f12 = f4 - 2.0f * f9;
        float f13 = Math.round(f10 + 30.0f * f6);
        this.drawThemesInspector(builder, f11, f13, f12, f3 + f5 - 14.0f * f6, f6, f7, f8);
    }

    private void drawThemesInspector(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        if (hitboxConfigScreen752809 == null) {
            builder.text(f2 + 4.0f * f6, f3 + 18.0f * f6, 11.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\ufd0f\u44dd\udbe1\uff3e\udc99\ue6b4\u61b5\u6cda\u39f4\ubbf3\ua8be\u081f\uab42\u5803\u2fc0\u0e66\uc278\uea9d\u5d2a\ue13a\ub2cd\u9d56\u5c46\ub747\ua214", 85136154, 0x1B1EF9FC ^ CryptoVault.mask()));
            return;
        }
        float f9 = Math.max(0.0f, f5 - f3);
        builder.clipPush(f2, f3, f4, f9, this.cardRadius * f6);
        this.clipX = f2;
        this.clipY = f3;
        this.clipW = f4;
        this.clipH = f9;
        this.clippingHits = true;
        this.appearanceScrollShown = this.easeScroll(this.appearanceScrollShown, this.appearanceScroll);
        float f10 = f3 - this.appearanceScrollShown * f6;
        float f11 = Math.round(54.0f * f6);
        UiKit.card(builder, this.kitHost, f2, f10, f4, f11, f6);
        builder.text(f2 + 14.0f * f6, f10 + 12.0f * f6, 11.0f * f6, this.titleColor, GpuClickGuiScreen.bf("\u4554\ua77e\u269b\uffa6\u1838\u89fc\u038b\u60ac\ufc96\u9449\u7b51\u6984", -772507945, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f12 = UiKit.buttonWidth(GpuClickGuiScreen.bf("\ub716\u4372\u0274\uad2e\u3900\ue8a2\u6d45\u9810\u953a\u41fa\u28a2\u2fac\ub9bb\uf52a\u01dc\ud0a3\ue78c", -1272319244, 0x1B1EF9FC ^ CryptoVault.mask()), f6);
        builder.text(f2 + 14.0f * f6, f10 + 29.0f * f6, 9.5f * f6, this.mutedColor, UiKit.fit(GpuClickGuiScreen.bf("\u8b76\uf847\u0838\u4455\uf011\ub87b\u11c3\ub555\u78a1\uc7d0\u3e80\u3edc\uc042\u82a4\ub0e1\u739e\u9b1c\u95d4\uaa2c\ua735\ub68a\u1a22\u61be\ucc6f\u4454\u993a\u633f\ub8c3\u990e\uef70\u07a5\ubfc9\ua1d9\u7b3e\ucf49\u3854\u4c79\u40af\ubc80", 852920562, 0x1B1EF9FC ^ CryptoVault.mask()), Math.max(0.0f, f4 - f12 - 40.0f * f6), 9.5f * f6));
        float f13 = Math.round(28.0f * f6);
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u40fc\uf77a\u7b0e\uac16\ue9c4\udc2d\u8e22\u6bc8\u1be6\u0880\u9534\ue79c\u1e51\u7297\uba9b\u08cf\uae2b", -1554760231, 0x1B1EF9FC ^ CryptoVault.mask()), f2 + f4 - f12 - 12.0f * f6, f10 + (f11 - f13) * 0.5f, f12, f13, f6, UiKit.Style.PRIMARY, true, f7, f8, this::openThemeMaker);
        f10 += f11 + 12.0f * f6;
        f10 = this.drawThemeTiles(builder, hitboxConfigScreen752809, f2, f10, f4, 2, false, f6, f7, f8) + 14.0f * f6;
        f10 = this.drawAppearanceGroups(builder, f2, f10, f4, f6, f7, f8);
        float f14 = Math.max(0.0f, f10 - (f3 - this.appearanceScrollShown * f6));
        float f15 = Math.max(0.0f, (f14 - f9) / f6);
        this.appearanceScroll = Math.max(0.0f, Math.min(this.appearanceScroll, f15));
        this.clippingHits = false;
        builder.clipPop();
    }

    private void openThemeMaker() {
        this.themeMaker.open();
        this.themeMakerOpen = true;
        this.themesDrawerOpen = true;
        this.settingsDrawerOpen = false;
        this.appearanceScroll = 0.0f;
        this.appearanceScrollShown = 0.0f;
    }

    private void drawThemeMakerEditor(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        if (hitboxConfigScreen752809 == null) {
            return;
        }
        builder.clipPush(f2, f3, f4, f5, this.cardRadius * f6);
        this.clipX = f2;
        this.clipY = f3;
        this.clipW = f4;
        this.clipH = f5;
        this.clippingHits = true;
        this.appearanceScrollShown = this.easeScroll(this.appearanceScrollShown, this.appearanceScroll);
        float f10 = f9 = f3 - this.appearanceScrollShown * f6;
        builder.text(f2 + 4.0f * f6, f10, 9.5f * f6, this.muted2Color, GpuClickGuiScreen.bf("\ubf1b\u8409\u90a5\u2f45\u7339\ucfaf\u8d3e\u696f\u2dfe\ud219\ua790", 1291400585, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        f10 += 18.0f * f6;
        f10 = this.drawThemeTiles(builder, hitboxConfigScreen752809, f2, f10, f4, f4 > 520.0f * f6 ? 4 : 3, true, f6, f7, f8) + 14.0f * f6;
        f10 = this.drawAppearanceGroups(builder, f2, f10, f4, f6, f7, f8);
        float f11 = Math.max(0.0f, f10 - f9);
        float f12 = Math.max(0.0f, (f11 - f5) / f6);
        this.appearanceScroll = Math.max(0.0f, Math.min(this.appearanceScroll, f12));
        this.clippingHits = false;
        builder.clipPop();
    }

    private float drawThemeTiles(UiDisplayList.Builder builder, HitboxConfigScreen752809 hitboxConfigScreen752809, float f2, float f3, float f4, int n2, boolean bl, float f5, float f6, float f7) {
        int n3;
        List<String> list = HitboxConfigScreen752809.a.getModes();
        float f8 = 8.0f * f5;
        float f9 = (f4 - f8 * (float)(n2 - 1)) / (float)n2;
        float f10 = 44.0f * f5;
        float f11 = f10 + 26.0f * f5;
        float f12 = f3;
        for (n3 = 0; n3 < list.size(); ++n3) {
            String string = list.get(n3);
            float f13 = f2 + (float)(n3 % n2) * (f9 + f8);
            float f14 = f12 + (float)(n3 / n2) * (f11 + f8);
            boolean bl2 = HitboxConfigScreen752809.a.is(string);
            boolean bl3 = this.hovered(f6, f7, f13, f14, f9, f11);
            builder.rect(f13, f14, f9, f11, this.cardRadius * f5, this.cardBg);
            float f15 = f13 + 4.0f * f5;
            float f16 = f14 + 4.0f * f5;
            float f17 = f9 - 8.0f * f5;
            builder.clipPush(f15, f16, f17, f10, Math.max(0.0f, this.cardRadius - 4.0f) * f5);
            UiSurface uiSurface = UiSurface.byName(string);
            if (!uiSurface.isTheme()) {
                builder.material(f15, f16, f17, f10, Math.max(0.0f, this.cardRadius - 4.0f) * f5, uiSurface.preview(UiThemeMaterial.FLUX.accent() & 0xFFFFFF));
            } else if (UiThemeMaterial.LIQUID_GLASS.name().equalsIgnoreCase(string)) {
                int n4 = UiThemeMaterial.LIQUID_GLASS.accent() & 0xFFFFFF;
                builder.gradient(f15, f16, f17, f10, Math.max(0.0f, this.cardRadius - 4.0f) * f5, n4 | 0x40000000, 0x22FFFFFF, n4 | 0x18000000, 0x10FFFFFF);
            } else {
                builder.material(f15, f16, f17, f10, Math.max(0.0f, this.cardRadius - 4.0f) * f5, UiThemeMaterial.preview(string));
            }
            builder.clipPop();
            builder.border(f15, f16, f17, f10, Math.max(0.0f, this.cardRadius - 4.0f) * f5, f5, 0x1AFFFFFF);
            builder.text(f13 + 10.0f * f5, f14 + f10 + 10.0f * f5, 10.0f * f5, bl2 ? this.accentArgb() : this.textColor, UiKit.fit(string, Math.max(0.0f, f9 - 20.0f * f5), 10.0f * f5, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.border(f13, f14, f9, f11, this.cardRadius * f5, bl2 ? 1.5f * f5 : f5, bl2 ? this.accentArgb() : (bl3 ? 0x40FFFFFF : this.cardBorder));
            String string2 = string;
            this.hit(f13, f14, f9, f11, () -> {
                if (bl) {
                    com.ishland.scalablelux.l.d.ab(string2);
                } else {
                    HitboxConfigScreen752809.a.setCurrentMode(string2);
                }
            });
        }
        n3 = (list.size() + n2 - 1) / n2;
        return f12 + (float)n3 * (f11 + f8) - f8;
    }

    private void drawClientSettings(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10 = Math.max(0.0f, f5 - f3);
        builder.clipPush(f2, f3, f4, f10, this.cardRadius * f6);
        this.clipX = f2;
        this.clipY = f3;
        this.clipW = f4;
        this.clipH = f10;
        this.clippingHits = true;
        this.appearanceScrollShown = this.easeScroll(this.appearanceScrollShown, this.appearanceScroll);
        float f11 = f9 = f3 - this.appearanceScrollShown * f6;
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting setting : new Setting[]{HitboxConfigScreen752809.aB, HitboxConfigScreen752809.ay, HitboxConfigScreen752809.az, HitboxConfigScreen752809.aA}) {
            if (setting == null) continue;
            arrayList.add(setting);
        }
        f11 = this.drawSettingGroup(builder, GpuClickGuiScreen.bf("\ud451\u013b\u7f18\ub7e9\u2501\u1dd0\u2557\u875c\u38b6\ua525\uc5a4\u1ca2\ue542\ued50\ub274\u348a\u30cb\u5547\u34d1\u8a26", 8978920, 0x1B1EF9FC ^ CryptoVault.mask()), arrayList, f2, f11, f4, f6, f7, f8);
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Setting> arrayList2 = new ArrayList<Setting>();
        if (hitboxConfigScreen752809 != null) {
            for (Setting setting : hitboxConfigScreen752809.getSettings()) {
                HitboxKeyBinding655023 hitboxKeyBinding655023;
                if (!(setting instanceof HitboxKeyBinding655023) || (hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting).a() != hitboxConfigScreen752809 || !GpuClickGuiScreen.bf("\uc66f\u1be3\uc043\ud9c3\ue958\u2c0b\ud234\ueb39\ucb59\u9edd", -852711190, 0x1B1EF9FC ^ CryptoVault.mask()).equals(setting.getName())) continue;
                arrayList2.add(setting);
            }
        }
        f11 = this.drawSettingGroup(builder, GpuClickGuiScreen.bf("\uca34\ue578\u83e5\u33fc\u168b\u3ef2", 524778718, 0x1B1EF9FC ^ CryptoVault.mask()), arrayList2, f2, f11, f4, f6, f7, f8);
        builder.text(f2 + 4.0f * f6, f11 += 4.0f * f6, 10.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\ub97b\uc6a4\u8b7d\u5b95\ud258\ua573", -1300116374, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f12 = Math.round(30.0f * f6);
        float f13 = Math.round(8.0f * f6);
        float f14 = Math.round((f4 - f13) * 0.5f);
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\uad18\u737c\uf32c\u3213\uda1a\u560e\u2807\uc1b9\u0963\ue76c\ubd7f\ub6b0", 1711935089, 0x1B1EF9FC ^ CryptoVault.mask()), f2, f11 += 18.0f * f6, f14, f12, f6, UiKit.Style.SECONDARY, true, f7, f8, this::replayTour);
        UiKit.button(builder, this.kitHost, WhatsNewPanel.hasUnread() ? GpuClickGuiScreen.bf("\u6066\ub4c9\ufa0f\ua58b\u2698\u6df0\ue697\u13c9\u9812\u0a0c\ue553\uf24b\u807b\u564e\ue0aa\u0b14\ua79f", -656804245, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u427c\ue3a7\u2725\ub5ae\u51be\u9a0b\u7be8\u3dc5\u4f36\u3cef\u6f14\u0492\ua0c8\u0a6b", 459111360, 0x1B1EF9FC ^ CryptoVault.mask()), f2 + f14 + f13, f11, f4 - f14 - f13, f12, f6, UiKit.Style.SECONDARY, true, f7, f8, this.whatsNew::open);
        builder.text(f2 + 4.0f * f6, f11 += f12 + 12.0f * f6, 10.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\u873f\ue356\u0452\u47d4\ua1aa\u9ba5\u359a\uc9f7\u2fd7\u7554\u9f8d", -1460421527, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        boolean bl = this.signOutArmedAt != 0L && this.nanoTime() - this.signOutArmedAt < 3500000000L;
        UiKit.button(builder, this.kitHost, bl ? GpuClickGuiScreen.bf("\u2048\u4f09\u4bdf\ub878\u700d\u56b4\u85a5\u27b6\u8a27\u5f4b\uff0d\uc6b7\u1455\ua6cc\u7c51\ue190\u18b6\u323b\u87f5\u4fbb\u52dd\ua6b8\ub083\u2f1c\uf63d", 1002635470, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u315e\u8dc5\u025e\u21c6\ud75b\u77cf\u2b25\uf1d4\u8775\u19d9\ub1e4\u1560", -1677835088, 0x1B1EF9FC ^ CryptoVault.mask()), f2, f11 += 18.0f * f6, f4, f12, f6, UiKit.Style.SECONDARY, true, f7, f8, () -> {
            if (this.signOutArmedAt != 0L && this.nanoTime() - this.signOutArmedAt < 3500000000L) {
                this.signOutArmedAt = 0L;
                SlAuth.signOutAndPrompt(GpuClickGuiScreen.bf("\u92d6\u9316\u88b0\u57a4\ue2bb\ue56b\u3fd5\uce2f\u3f27\u2b8d\ua946\u5742\u33b9\u66a1\uefc1\ucca0\u54b9\u5176\u4d8f\u5b7f\u73bc\u6e8d\ue311\u20fb\u9d81\ufa3c\u0d1d\u965f\u7f8a\ud9a7\ud53f\uddca\u7c28\u68f6\u9a5f\udd9d\ubbb9\ufd8b", -917335161, 0x1B1EF9FC ^ CryptoVault.mask()));
            } else {
                this.signOutArmedAt = this.nanoTime();
            }
        });
        float f15 = Math.max(0.0f, ((f11 += f12 + 12.0f * f6) - f9 - f10) / f6);
        this.appearanceScroll = Math.max(0.0f, Math.min(this.appearanceScroll, f15));
        this.clippingHits = false;
        builder.clipPop();
    }

    private void drawConfigKeybinds(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f3;
        ConfigKeybindManager configKeybindManager = ConfigKeybindManager.getInstance();
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        Map<String, PrivateConfigManager.PrivateConfigInfo> map = this.cloudProfiles;
        for (String object : this.savedConfigs) {
            if (object == null || object.isBlank() || GpuClickGuiScreen.bf("\u476e\u71b3\u4e6b\u0b9d\u4c40\u4deb\u9253\u7878\ub7cc\u5890\u16eb\uf1e6\u7004\u5eef\u6d52\u0dbf", -138830357, 0x1B1EF9FC ^ CryptoVault.mask()).equals(object)) continue;
            boolean bl = map.isEmpty() ? !GpuClickGuiScreen.bf("\u4ec6\ud68c\ueeaa\u7087\uf1de\u5f1e\u495d\ub80d\u20b1", -693026093, 0x1B1EF9FC ^ CryptoVault.mask()).equalsIgnoreCase(object) || !CloudConfigs.signedIn() : map.containsKey(object.toLowerCase(Locale.ROOT));
            boolean bl2 = bl;
            if (!bl2) continue;
            linkedHashSet.add(object);
        }
        for (ConfigKeybindManager.ConfigKeybind configKeybind : configKeybindManager.getKeybinds()) {
            if (configKeybind.configName == null || configKeybind.configName.isBlank()) continue;
            linkedHashSet.add(configKeybind.configName);
        }
        builder.text(f2 + 4.0f * f6, f7, 10.0f * f6, this.mutedColor, GpuClickGuiScreen.bf("\ucab7\uf49c\u8de7\u519b\u38af\u37b6\ue127\u29e8\u26d6\u8468\u7e5a\u8ddb\u2db7\ud260\u8c81\u1b8f\u45a4\u232c\u6c45\u5be6\u1afb\uae0f\u950f\u5a49\ue2f3\ua8c6\u34ae\uc3dc\ud836\u5823\uc5bf\u0669\u6958\u98ce\u2b04\u4f27\uc7a8", -81840103, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        f7 += 18.0f * f6;
        if (linkedHashSet.isEmpty()) {
            builder.text(f2 + 4.0f * f6, f7 + 4.0f * f6, 9.5f * f6, this.muted2Color, UiKit.fit(CloudConfigs.signedIn() ? GpuClickGuiScreen.bf("\u0da2\u4c7c\u329c\ubf90\u40e0\u1139\u9a9b\u67be\u4ad7\udd72\u85bf\u014a\ud4e6\u4abd\u7fea\ud79a\u319e\ubdd8\u6ac4\u274d\u1642\ue7b9\u5415\u2628\u9823\uc74e\ud4e3\u2b79\u6178\u483c\u1560\u5dc1\u9583\u0714\u844b\ueb60\uc0d6\u7f15\u3c96\u440b\u2008\ud073\udbdc\ubdde\u2c1b\u2dca\u582a\u32f9\u9590\ucb3d\ud08f\u891a\ub3eb\uf96f\uaee8\u87aa\uaeb9\ue827\u6c35\u2612\ucd31", -726921686, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u575c\u95ce\u1a9e\u42d8\u5e97\u8081\u17eb\ue685\ue4da\u0289\u8c30\ucc62\uc718\u0506\u3b8f\u4f39\u01be\ue842\ub03c\ud81e\ua020\u4fd8\u8d1c\u7a45\u7962\ue8cbV\u05a2\u543e\u951f}\u221f\u5c04\uf015\u29c8\ub08e\u910d\u3da2", 1830935773, 0x1B1EF9FC ^ CryptoVault.mask()), f4 - 8.0f * f6, 9.5f * f6));
        }
        float f8 = 42.0f * f6;
        boolean bl = !this.configBindStatusMessage.isBlank();
        float f9 = bl ? f5 - 20.0f * f6 : f5;
        builder.clipPush(f2, f7, f4, Math.max(0.0f, f9 - f7), 0.0f);
        this.clipX = f2;
        this.clipY = f7;
        this.clipW = f4;
        this.clipH = Math.max(0.0f, f9 - f7);
        this.clippingHits = true;
        float f10 = Math.max(0.0f, f9 - f7);
        float f11 = (float)linkedHashSet.size() * (f8 + 3.0f * f6);
        this.hotkeyScroll = Math.max(0.0f, Math.min(this.hotkeyScroll, Math.max(0.0f, f11 - f10)));
        this.hotkeyScrollShown = this.easeScroll(this.hotkeyScrollShown, this.hotkeyScroll);
        int n2 = 0;
        for (String string : linkedHashSet) {
            float f12 = f7 - this.hotkeyScrollShown + (float)n2++ * (f8 + 3.0f * f6);
            ConfigKeybindManager.ConfigKeybind configKeybind = configKeybindManager.get(string);
            String string2 = string.equals(this.bindingConfigName) ? GpuClickGuiScreen.bf("\u56ae\udf8f\uc09b\u4696\u46c3\ud841\u2623\u28b9\ucffd\u3812\ue8da\u5e24\ub2c9", 1825134591, 0x1B1EF9FC ^ CryptoVault.mask()) : (configKeybind == null ? GpuClickGuiScreen.bf("\u873d\ucfa7\u6a65\u1d92\u99a6\ua0b8\ucf37", -155651784, 0x1B1EF9FC ^ CryptoVault.mask()) : configKeybind.getDisplayName());
            f12 = Math.round(f12);
            float f13 = f12 + f8 * 0.5f;
            builder.rect(f2, f12, f4, f8, Math.max(0.0f, this.cardRadius - 2.0f) * f6, this.cardBg);
            float f14 = Math.round(24.0f * f6);
            float f15 = Math.round(Math.min(110.0f * f6, Math.max(64.0f * f6, GpuUiFontMetrics.textWidth(string2, 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f6)));
            float f16 = Math.round(f2 + f4 - f15 - 7.0f * f6);
            float f17 = Math.round(f13 - f14 * 0.5f);
            float f18 = GpuUiFontMetrics.textWidth(GpuClickGuiScreen.bf("\u3e73\ube3b\u32dd\u1d49\u93e8\ua94c\ufb12", -1709214129, 0x1B1EF9FC ^ CryptoVault.mask()), 9.5f * f6) + 10.0f * f6;
            float f19 = f16 - 6.0f * f6 - f18;
            float f20 = Math.max(0.0f, (configKeybind != null ? f19 : f16) - 10.0f * f6 - (f2 + 12.0f * f6));
            GpuClickGuiScreen.textMid(builder, f2 + 12.0f * f6, f13, 10.0f * f6, this.textColor, UiKit.fit(string, f20, 10.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.rect(f16, f17, f15, f14, f14 * 0.5f, string.equals(this.bindingConfigName) ? this.controlFill(72) : this.controlFill(26));
            GpuClickGuiScreen.textCentered(builder, f16 + f15 * 0.5f, f13, 9.5f * f6, string.equals(this.bindingConfigName) ? this.accentArgb() : this.textColor, UiKit.fit(string2, f15 - 16.0f * f6, 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            this.hit(f16, f17, f15, f14, () -> this.beginConfigKeyCapture(string));
            if (configKeybind == null) continue;
            GpuClickGuiScreen.textCentered(builder, f19 + f18 * 0.5f, f13, 9.5f * f6, this.mutedColor, GpuClickGuiScreen.bf("\u90be\u7ebc\u24a1\u41c3\ucf50\ud0d9\ub719\u29f1", -1692718870, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
            this.hit(f19, f17, f18, f14, () -> configKeybindManager.setKeybind(string, -1, -1));
        }
        this.clippingHits = false;
        builder.clipPop();
        if (bl) {
            GpuClickGuiScreen.textMid(builder, f2 + 4.0f * f6, f5 - 8.0f * f6, 8.5f * f6, this.accentArgb(), UiKit.fit(this.configBindStatusMessage, f4 - 8.0f * f6, 8.5f * f6), UiDrawCommand.TextWeight.REGULAR);
        }
    }

    private float drawAppearanceSettingGroup(UiDisplayList.Builder builder, String string, List<? extends Setting> list, float f2, float f3, float f4, float f5, float f6, float f7) {
        HitboxConfigScreen752809 hitboxConfigScreen752809 = this.clickGuiModule();
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting setting : list) {
            ClassicSettingLayout.Row row;
            if (setting == null || !HitboxConfigScreen752809.a(setting)) continue;
            ClassicSettingLayout.Row row2 = row = hitboxConfigScreen752809 == null ? null : ClassicSettingLayout.row(hitboxConfigScreen752809, setting);
            if (row != null && !row.visible()) continue;
            arrayList.add(setting);
        }
        return this.drawSettingGroup(builder, string, arrayList, f2, f3, f4, f5, f6, f7, GpuClickGuiScreen.bf("\u48a1\uef85\u760b\uf1d4\u6ec0\uf784\u2d8b\u10b4\ufcc7\u08bc\u93af\u4fba\u401d\u7ce0", -60724557, 0x1B1EF9FC ^ CryptoVault.mask()) + string);
    }

    protected static List<SettingGroup> settingGroups(Module module) {
        LinkedHashMap<String, List> linkedHashMap = new LinkedHashMap<String, List>();
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        List<ClassicSettingLayout.Row> list = ClassicSettingLayout.rows(module);
        for (ClassicSettingLayout.Row object2 : list) {
            SUB_VISIBLE.remove(object2.setting());
            SUB_DEPTH.remove(object2.setting());
        }
        for (ClassicSettingLayout.Row row : list) {
            Object object;
            Setting setting = row.setting();
            if (!setting.isVisible() || setting instanceof HitboxKeyBinding655023 && ((HitboxKeyBinding655023)(object = (HitboxKeyBinding655023)setting)).a() == module) continue;
            object = setting.getDependsOn();
            if (object != null && !((String)object).isBlank()) {
                SUB_VISIBLE.put(setting, row.visible());
            }
            if (setting.isAdvanced()) {
                arrayList.add(setting);
                continue;
            }
            String string2 = setting.getSection() == null ? "" : setting.getSection().trim();
            linkedHashMap.computeIfAbsent(string2, string -> new ArrayList()).add(setting);
        }
        ArrayList arrayList2 = new ArrayList();
        List list3 = (List)linkedHashMap.remove("");
        if (list3 != null) {
            arrayList2.add(new SettingGroup(null, GpuClickGuiScreen.nestDependents(module, list3)));
        }
        linkedHashMap.forEach((string, list2) -> arrayList2.add(new SettingGroup((String)string, GpuClickGuiScreen.nestDependents(module, list2))));
        if (!arrayList.isEmpty()) {
            arrayList2.add(new SettingGroup(GpuClickGuiScreen.bf("\u461a\uf950\uf291\u1acc\ua473\u3ae7\u56ac\ud3bb\ufe15\ua853\ue1a0\ud170", 291226956, 0x1B1EF9FC ^ CryptoVault.mask()), GpuClickGuiScreen.nestDependents(module, arrayList)));
        }
        arrayList2.removeIf(settingGroup -> settingGroup.settings().stream().noneMatch(setting -> SUB_VISIBLE.getOrDefault(setting, true)));
        return arrayList2;
    }

    private static Setting dependencyParent(Module module, Setting setting) {
        int n2;
        String string = setting.getDependsOn();
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.split(",", -1)[0].trim();
        if (string2.startsWith("!")) {
            string2 = string2.substring(1).trim();
        }
        if ((n2 = string2.indexOf(58)) >= 0) {
            string2 = string2.substring(0, n2).trim();
        }
        if (string2.isEmpty()) {
            return null;
        }
        Setting setting2 = module.getSetting(string2);
        return setting2 == setting ? null : setting2;
    }

    private static List<Setting> nestDependents(Module module, List<Setting> list) {
        Set set = Collections.newSetFromMap(new IdentityHashMap());
        set.addAll(list);
        IdentityHashMap<Setting, List<Setting>> identityHashMap = new IdentityHashMap<Setting, List<Setting>>();
        ArrayList<Setting> arrayList = new ArrayList<Setting>();
        for (Setting object2 : list) {
            Setting setting2 = GpuClickGuiScreen.dependencyParent(module, object2);
            if (setting2 != null && set.contains(setting2)) {
                identityHashMap.computeIfAbsent(setting2, setting -> new ArrayList()).add(object2);
                continue;
            }
            arrayList.add(object2);
        }
        ArrayList arrayList2 = new ArrayList(list.size());
        Set<Setting> set2 = Collections.newSetFromMap(new IdentityHashMap());
        for (Setting setting3 : arrayList) {
            GpuClickGuiScreen.emitNested(setting3, 0, identityHashMap, arrayList2, set2);
        }
        for (Setting setting4 : list) {
            if (!set2.add(setting4)) continue;
            arrayList2.add(setting4);
        }
        return arrayList2;
    }

    private static void emitNested(Setting setting, int n2, Map<Setting, List<Setting>> map, List<Setting> list, Set<Setting> set) {
        if (!set.add(setting)) {
            return;
        }
        list.add(setting);
        if (n2 > 0) {
            SUB_DEPTH.put(setting, n2);
        }
        for (Setting setting2 : map.getOrDefault(setting, List.of())) {
            GpuClickGuiScreen.emitNested(setting2, n2 + 1, map, list, set);
        }
    }

    private float fold(Setting setting) {
        Boolean bl = SUB_VISIBLE.get(setting);
        if (bl == null) {
            return 1.0f;
        }
        float f2 = bl != false ? 1.0f : 0.0f;
        Fold fold = this.folds.get(setting);
        if (fold == null) {
            fold = new Fold();
            fold.value = f2;
            fold.frame = this.foldFrame;
            this.folds.put(setting, fold);
            return f2;
        }
        if (fold.frame != this.foldFrame) {
            fold.frame = this.foldFrame;
            fold.value += (f2 - fold.value) * Math.min(1.0f, this.frameDelta * 14.0f);
            if (Math.abs(fold.value - f2) < 0.003f) {
                fold.value = f2;
            }
        }
        return fold.value * fold.value * (3.0f - 2.0f * fold.value);
    }

    private float groupHeight(SettingGroup settingGroup, String string) {
        float f2;
        float f3 = f2 = settingGroup.title() == null ? 0.0f : 18.0f;
        if (settingGroup.title() != null && string != null && COLLAPSED_GROUPS.contains(string)) {
            return f2 + 6.0f;
        }
        for (Setting setting : settingGroup.settings()) {
            f2 += (float)this.settingHeight(setting) * this.fold(setting);
        }
        return f2 + 12.0f;
    }

    private float drawSettingGroup(UiDisplayList.Builder builder, String string, List<Setting> list, float f2, float f3, float f4, float f5, float f6, float f7) {
        return this.drawSettingGroup(builder, string, list, f2, f3, f4, f5, f6, f7, null);
    }

    private float drawSettingGroup(UiDisplayList.Builder builder, String string, List<Setting> list, float f2, float f3, float f4, float f5, float f6, float f7, String string2) {
        float f8;
        float f9;
        boolean bl;
        if (list.isEmpty()) {
            return f3;
        }
        float f10 = f3;
        boolean bl2 = string != null && string2 != null;
        boolean bl3 = bl = bl2 && COLLAPSED_GROUPS.contains(string2);
        if (string != null) {
            int n2;
            f9 = 10.0f * f5;
            float f11 = f3 - GpuUiFontMetrics.topForCenter(0.0f, f9);
            float f12 = f2 + 4.0f * f5;
            boolean bl4 = bl2 && this.hovered(f6, f7, f2, f3 - 4.0f * f5, f4, 18.0f * f5);
            int n3 = n2 = bl4 ? this.textColor : this.mutedColor;
            if (bl2) {
                f8 = Math.round(11.0f * f5);
                UiIcons.draw(builder, bl ? UiIcons.Icon.CHEVRON_RIGHT : UiIcons.Icon.CHEVRON_DOWN, Math.round(f12 - 2.0f * f5), Math.round(f11 - f8 * 0.5f), f8, n2);
                f12 += f8 + 2.0f * f5;
                this.hit(f2, f3 - 4.0f * f5, f4, 18.0f * f5, () -> GpuClickGuiScreen.toggleGroupCollapsed(string2));
            }
            GpuClickGuiScreen.textMid(builder, f12, f11, f9, n2, string, UiDrawCommand.TextWeight.SEMIBOLD);
            if (bl) {
                int n4 = 0;
                for (Setting setting : list) {
                    if (!setting.isModified()) continue;
                    ++n4;
                }
                String string3 = list.size() + (list.size() == 1 ? GpuClickGuiScreen.bf("\u1c2c\uf2e7\u8b12\u0bf8\ud447\uc6a3\u1063\ue784\u98d9", 1927310993, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\udf9c\u6692\ud8c5\ub616\ued47\u76d3\u929c\u6f14\u9a75\ue508\u992c\u5bb1\u3c83", -1560317536, 0x1B1EF9FC ^ CryptoVault.mask())) + (n4 > 0 ? ", " + n4 + GpuClickGuiScreen.bf("\ud910\u5818\u5cbd\uf7a6\u1f7a\u0415\ucd2b\u438e\ueb5f", 1390999575, 0x1B1EF9FC ^ CryptoVault.mask()) : "");
                GpuClickGuiScreen.textMid(builder, f12 + GpuUiFontMetrics.textWidth(string, f9, UiDrawCommand.TextWeight.SEMIBOLD) + 8.0f * f5, f11, 9.0f * f5, this.muted2Color, string3, UiDrawCommand.TextWeight.REGULAR);
            }
            f10 = f3 + 18.0f * f5;
        }
        if (bl) {
            return f10 + 6.0f * f5;
        }
        f9 = 0.0f;
        for (Setting setting : list) {
            f9 += (float)this.settingHeight(setting) * f5 * this.fold(setting);
        }
        this.surfaceCard(builder, f2, f10, f4, f9, this.cardRadius * f5);
        float f13 = f10;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            Setting setting = list.get(i2);
            float f14 = this.fold(setting);
            f8 = (float)this.settingHeight(setting) * f5;
            float f15 = f8 * f14;
            if (f14 < 0.01f) continue;
            int n5 = SUB_DEPTH.getOrDefault(setting, 0);
            float f16 = (float)Math.min(3, n5) * 16.0f * f5;
            boolean bl5 = f14 < 0.999f;
            float f17 = this.clipX;
            float f18 = this.clipY;
            float f19 = this.clipW;
            float f20 = this.clipH;
            boolean bl6 = this.clippingHits;
            if (bl5) {
                builder.clipPush(f2, f13, f4, f15, 0.0f);
                float f21 = Math.max(f13, bl6 ? f18 : f13);
                float f22 = Math.min(f13 + f15, bl6 ? f18 + f20 : f13 + f15);
                this.clipX = bl6 ? f17 : f2;
                this.clipW = bl6 ? f19 : f4;
                this.clipY = f21;
                this.clipH = Math.max(0.0f, f22 - f21);
                this.clippingHits = true;
            }
            if (n5 > 0) {
                builder.rect(f2 + f16 - 4.0f * f5, f13 + 6.0f * f5, Math.max(1.0f, 1.5f * f5), Math.max(0.0f, f8 - 12.0f * f5), 1.0f, GpuClickGuiScreen.withA(this.accentArgb(), 102));
            }
            this.drawSettingRow(builder, setting, f2 + f16, f13, f4 - f16, f8, f5, f6, f7);
            if (bl5) {
                this.clipX = f17;
                this.clipY = f18;
                this.clipW = f19;
                this.clipH = f20;
                this.clippingHits = bl6;
                builder.clipPop();
            }
            f13 += f15;
            if (i2 >= list.size() - 1 || !this.showSeparators || !(f14 > 0.5f)) continue;
            builder.rect(f2 + 14.0f * f5 + f16, f13 - f5, f4 - 28.0f * f5 - f16, f5, 0.0f, this.lineColor);
        }
        return f10 + f9 + 12.0f * f5;
    }

    protected static String hexOf(int n2, boolean bl) {
        return bl ? String.format(Locale.ROOT, GpuClickGuiScreen.bf("\ub670\u0f49\ua56a\uf98c\ud100\ub4b2\u8cf8", 669702071, 0x1B1EF9FC ^ CryptoVault.mask()), n2) : String.format(Locale.ROOT, GpuClickGuiScreen.bf("\u3665\ua82e\u35ca\ua203\ub439\ubc94\u9169", -1976714951, 0x1B1EF9FC ^ CryptoVault.mask()), n2 & 0xFFFFFF);
    }

    protected static Integer parseHex(String string) {
        if (string == null) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("#")) {
            string2 = string2.substring(1);
        } else if (string2.startsWith("0x") || string2.startsWith("0X")) {
            string2 = string2.substring(2);
        }
        if (string2.length() == 3) {
            StringBuilder stringBuilder = new StringBuilder(6);
            for (int i2 = 0; i2 < 3; ++i2) {
                stringBuilder.append(string2.charAt(i2)).append(string2.charAt(i2));
            }
            string2 = stringBuilder.toString();
        }
        if (string2.length() != 6 && string2.length() != 8) {
            return null;
        }
        try {
            long l2 = Long.parseLong(string2, 16);
            return string2.length() == 6 ? (int)(l2 | 0xFF000000L) : (int)l2;
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private Integer themeValueFor(ColorSetting colorSetting) {
        if (colorSetting == null) {
            return null;
        }
        if (colorSetting == HitboxConfigScreen752809.al) {
            return this.textColor;
        }
        if (colorSetting == HitboxConfigScreen752809.am) {
            return this.mutedColor;
        }
        if (colorSetting == HitboxConfigScreen752809.an || colorSetting == HitboxConfigScreen752809.ap) {
            return this.accentArgb() | 0xFF000000;
        }
        if (colorSetting == HitboxConfigScreen752809.ao) {
            return this.toggleOffColor;
        }
        if (colorSetting == HitboxConfigScreen752809.aq) {
            return -1;
        }
        if (colorSetting == HitboxConfigScreen752809.ar) {
            return this.outerBorder;
        }
        if (colorSetting == HitboxConfigScreen752809.bf || colorSetting == HitboxConfigScreen752809.bg || colorSetting == HitboxConfigScreen752809.bh) {
            return this.accentArgb() | 0xFF000000;
        }
        if (colorSetting == HitboxConfigScreen752809.bi) {
            return -536211947;
        }
        if (colorSetting == HitboxConfigScreen752809.aj) {
            return this.cardBg;
        }
        if (colorSetting == HitboxConfigScreen752809.ak) {
            return this.cardBorder;
        }
        if (colorSetting == HitboxConfigScreen752809.ai) {
            return this.lg ? GpuClickGuiScreen.argb(68.0f, 329740) : GpuClickGuiScreen.mixRgb(-16250094, this.currentAccent, 0.16f) & 0xFFFFFF | 0x8C000000;
        }
        return null;
    }

    private void openColorPicker(ColorSetting colorSetting) {
        if (this.colorPicker != null && this.colorPicker != colorSetting) {
            this.closeColorPicker();
        }
        this.colorPicker = colorSetting;
        this.pickerOpenedWith = colorSetting.B().getRGB();
        this.settingPicker.reset();
    }

    private void closeColorPicker() {
        this.colorPicker = null;
        this.colorDrag = ColorDrag.NONE;
        this.settingPicker.release();
        this.clearTextFocus();
    }

    protected void applyPicked(int n2) {
        if (this.colorPicker == null) {
            return;
        }
        if (this.colorPicker.B().getRGB() != n2) {
            this.colorPicker.A(new Color(n2, true));
        }
    }

    public void openCrosshairMaker(CrosshairMakerSource crosshairMakerSource) {
        if (crosshairMakerSource == null) {
            return;
        }
        if (this.colorPicker != null) {
            this.closeColorPicker();
        }
        this.crosshairMaker = crosshairMakerSource;
        this.crosshairGrid = CrosshairGrid.decode(crosshairMakerSource.a().X());
        if (this.crosshairGrid == null) {
            this.crosshairGrid = new boolean[289];
        }
        this.crosshairMirror = false;
        this.crosshairPaintValue = -1;
    }

    private void closeCrosshairMaker() {
        this.crosshairMaker = null;
        this.crosshairGrid = null;
        this.crosshairPaintValue = -1;
        this.clearTextFocus();
    }

    private void commitCrosshair() {
        if (this.crosshairMaker != null && this.crosshairGrid != null) {
            this.crosshairMaker.a().G(CrosshairGrid.encode(this.crosshairGrid));
        }
    }

    private void setCrosshairCell(int n2, int n3, boolean bl) {
        int n4 = 17;
        if (this.crosshairGrid == null || n2 < 0 || n3 < 0 || n2 >= n4 || n3 >= n4) {
            return;
        }
        this.crosshairGrid[n3 * n4 + n2] = bl;
    }

    private void applyCrosshairCell(int n2, int n3, boolean bl) {
        int n4 = 17;
        this.setCrosshairCell(n2, n3, bl);
        if (this.crosshairMirror) {
            this.setCrosshairCell(n4 - 1 - n2, n3, bl);
        }
        this.commitCrosshair();
    }

    private void toggleCrosshairCell(int n2, int n3) {
        int n4 = 17;
        boolean bl = this.crosshairGrid == null || !this.crosshairGrid[n3 * n4 + n2];
        this.crosshairPaintValue = bl ? 1 : 0;
        this.applyCrosshairCell(n2, n3, bl);
    }

    private void clearCrosshair() {
        if (this.crosshairGrid != null) {
            Arrays.fill(this.crosshairGrid, false);
        }
        this.commitCrosshair();
    }

    private void paintCrosshairAt(double d2, double d3) {
        if (this.chCellPx <= 0.0f) {
            return;
        }
        int n2 = (int)Math.floor((d2 - (double)this.chGridX) / (double)this.chCellPx);
        int n3 = (int)Math.floor((d3 - (double)this.chGridY) / (double)this.chCellPx);
        int n4 = 17;
        if (n2 < 0 || n3 < 0 || n2 >= n4 || n3 >= n4) {
            return;
        }
        this.applyCrosshairCell(n2, n3, this.crosshairPaintValue == 1);
    }

    private void drawCrosshairMaker(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8;
        this.hits.clear();
        this.moduleCardRightHits.clear();
        this.hoveredTooltipTitle = null;
        this.hoveredTooltipDesc = null;
        int n2 = 17;
        float f9 = 15.0f;
        float f10 = f9 * (float)n2;
        float f11 = f10 + 44.0f;
        float f12 = 42.0f + f10 + 48.0f;
        float f13 = Math.min(f4, Math.min((f2 - 32.0f) / f11, (f3 - 32.0f) / f12));
        f13 = Math.max(0.35f, f13);
        float f14 = Math.round(f11 * f13);
        float f15 = Math.round(f12 * f13);
        float f16 = Math.round((f2 - f14) * 0.5f);
        float f17 = Math.round((f3 - f15) * 0.5f);
        this.hit(0.0f, 0.0f, f2, f3, this::closeCrosshairMaker);
        this.hit(f16, f17, f14, f15, () -> {});
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, 0x66000000);
        float f18 = 14.0f * f13 * this.cornerScale;
        if (this.lg) {
            this.glassPane(builder, f16, f17, f14, f15, f18, 1.0f, 208, true, 0);
        } else {
            this.panelShadow(builder, f16, f17, f14, f15, f18, f13, 1.0f);
            builder.rect(f16, f17, f14, f15, f18, 0xF0000000 | this.railBg & 0xFFFFFF);
            builder.rect(f16, f17, f14, f15, f18, this.listBg);
            if (this.showRims) {
                builder.border(f16, f17, f14, f15, f18, Math.max(1.0f, f13), this.outerBorder);
            }
        }
        builder.text(f16 + 18.0f * f13, f17 + 15.0f * f13, 13.0f * f13, this.titleColor, GpuClickGuiScreen.bf("\ua7a4\u8b7a\u9718\ub8ff\u3e0e\u6097\u4c90\u1dc5\u9a7f\u0512\uc048\u3865\u0c38\u7ade\ufaf5\ue21c\u086d\u1157", 913664089, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f19 = Math.max(3.0f, (float)Math.round(f9 * f13));
        float f20 = f19 * (float)n2;
        float f21 = Math.round(f16 + (f14 - f20) * 0.5f);
        float f22 = Math.round(f17 + 38.0f * f13);
        this.chGridX = f21;
        this.chGridY = f22;
        this.chCellPx = f19;
        int n3 = this.accentArgb();
        int n4 = (this.crosshairMaker != null ? this.crosshairMaker.b().getRGB() : n3) | 0xFF000000;
        builder.rect(f21 - 3.0f, f22 - 3.0f, f20 + 6.0f, f20 + 6.0f, 4.0f * f13, 0x55000000);
        boolean[] blArray = this.crosshairGrid;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n5 = 0;
            while (n5 < n2) {
                boolean bl;
                f8 = f21 + (float)n5 * f19;
                f7 = f22 + (float)i2 * f19;
                boolean bl2 = blArray != null && blArray[i2 * n2 + n5];
                boolean bl3 = bl = n5 == n2 / 2 && i2 == n2 / 2;
                int n6 = bl2 ? n4 : (bl ? 0x40000000 | n3 & 0xFFFFFF : ((n5 + i2 & 1) == 0 ? 0xEFFFFFF : 0x18FFFFFF));
                builder.rect(f8 + 0.5f, f7 + 0.5f, f19 - 1.0f, f19 - 1.0f, 1.0f, n6);
                int n7 = n5++;
                int n8 = i2;
                this.hit(f8, f7, f19, f19, () -> this.toggleCrosshairCell(n7, n8));
            }
        }
        builder.border(f21, f22, f20, f20, 2.0f * f13, Math.max(1.0f, f13), this.outerBorder);
        float f23 = Math.round(22.0f * f13);
        float f24 = f17 + f15 - f23 - 13.0f * f13;
        f8 = 8.0f * f13;
        f7 = Math.round((f14 - 18.0f * f13 * 2.0f - f8 * 2.0f) / 3.0f);
        float f25 = f16 + 18.0f * f13;
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u1dff\u1113\u16fb\u7c49\uf91e\u0fb3\uc93a\u7649\ud825", -1217415153, 0x1B1EF9FC ^ CryptoVault.mask()), f25, f24, f7, f23, f13, UiKit.Style.SECONDARY, true, f5, f6, this::clearCrosshair);
        UiKit.button(builder, this.kitHost, this.crosshairMirror ? GpuClickGuiScreen.bf("\uea8c\u9fae\ud5a1\u18f0\ud7ac\ub54d\u52b5\ud9e7\ue2bd\u5f5c\ub415\uabc2", 967361299, 0x1B1EF9FC ^ CryptoVault.mask()) : GpuClickGuiScreen.bf("\u6c09\u6eea\u9f10\ufd27\u5ac5\u95bf\ua0d0\ub5a2\uc1e4\ua1f4", 87185857, 0x1B1EF9FC ^ CryptoVault.mask()), f25 + f7 + f8, f24, f7, f23, f13, UiKit.Style.SECONDARY, true, f5, f6, () -> {
            this.crosshairMirror = !this.crosshairMirror;
        });
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u7f90\u71e7\ud0a6\ud7ac\u595c\u2773\u4d40\uab36", -1534545467, 0x1B1EF9FC ^ CryptoVault.mask()), f25 + (f7 + f8) * 2.0f, f24, f7, f23, f13, UiKit.Style.PRIMARY, true, f5, f6, this::closeCrosshairMaker);
    }

    private void drawColorPicker(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
        this.hits.clear();
        this.moduleCardRightHits.clear();
        this.hoveredTooltipTitle = null;
        this.hoveredTooltipDesc = null;
        float f7 = 460.0f;
        float f8 = 352.0f;
        float f9 = Math.min(f4, Math.min((f2 - 32.0f) / f7, (f3 - 32.0f) / f8));
        f9 = Math.max(0.35f, f9);
        float f10 = Math.round(f7 * f9);
        float f11 = Math.round(f8 * f9);
        float f12 = Math.round((f2 - f10) * 0.5f);
        float f13 = Math.round((f3 - f11) * 0.5f);
        ColorSetting colorSetting = this.colorPicker;
        this.hit(0.0f, 0.0f, f2, f3, this::closeColorPicker);
        this.hit(f12, f13, f10, f11, () -> {});
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, 0x66000000);
        float f14 = 14.0f * f9 * this.cornerScale;
        if (this.lg) {
            this.glassPane(builder, f12, f13, f10, f11, f14, 1.0f, 208, true, 0);
        } else {
            this.panelShadow(builder, f12, f13, f10, f11, f14, f9, 1.0f);
            builder.rect(f12, f13, f10, f11, f14, 0xF0000000 | this.railBg & 0xFFFFFF);
            builder.rect(f12, f13, f10, f11, f14, this.listBg);
            if (this.showRims) {
                builder.border(f12, f13, f10, f11, f14, Math.max(1.0f, f9), this.outerBorder);
            }
        }
        builder.text(f12 + 20.0f * f9, f13 + 18.0f * f9, 14.0f * f9, this.titleColor, UiKit.fit(colorSetting.getName(), f10 - 40.0f * f9, 14.0f * f9, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        float f15 = f12 + 20.0f * f9;
        float f16 = f13 + 46.0f * f9;
        float f17 = Math.round(260.0f * f9);
        Integer n2 = colorSetting.isModified() ? null : this.themeValueFor(colorSetting);
        int n3 = n2 != null ? n2.intValue() : colorSetting.B().getRGB();
        this.settingPicker.draw(builder, this.kitHost, f15, f16, f17, f9, f5, f6, n3, true, this::applyPicked);
        float f18 = f15 + f17 + 20.0f * f9;
        float f19 = f12 + f10 - 20.0f * f9 - f18;
        float f20 = 34.0f * f9;
        float f21 = Math.round(f19 * 0.5f);
        builder.rect(f18, f16, f21, f20, 0.0f, this.pickerOpenedWith);
        builder.rect(f18 + f21, f16, f19 - f21, f20, 0.0f, colorSetting.A().getRGB());
        builder.border(f18, f16, f19, f20, 6.0f * f9, Math.max(1.0f, f9), 0x33FFFFFF);
        GpuClickGuiScreen.textCentered(builder, f18 + f21 * 0.5f, f16 + f20 + 8.0f * f9, 8.0f * f9, this.muted2Color, GpuClickGuiScreen.bf("\u2725\u3230\u6356\u3c87\u23d8\ucbed\u0281\u3302", 557732176, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
        GpuClickGuiScreen.textCentered(builder, f18 + f21 + (f19 - f21) * 0.5f, f16 + f20 + 8.0f * f9, 8.0f * f9, this.muted2Color, GpuClickGuiScreen.bf("\u221f\uaf43\u3bae\ud96f", 659596299, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.REGULAR);
        float f22 = Math.round(22.0f * f9);
        float f23 = Math.round((f19 - 6.0f * f9) * 0.5f);
        float f24 = f16 + f20 + 20.0f * f9;
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u3512\ua4ce\u904b\u58aa\u0a37\u067c\uee84", 446855441, 0x1B1EF9FC ^ CryptoVault.mask()), f18, f24, f19, f22, f9 * 0.9f, UiKit.Style.SECONDARY, colorSetting.B().getRGB() != this.pickerOpenedWith, f5, f6, () -> this.applyPicked(this.pickerOpenedWith));
        float f25 = f24 + f22 + 6.0f * f9;
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\u516e\ue10a\u7b90\u37f0\ue65f\ua573\ud123\ud3ed\u41b6\u4b8c\ua581\u02e7\u166f\u63f7\u0b58", -1532370717, 0x1B1EF9FC ^ CryptoVault.mask()), f18, f25, f19, f22, f9 * 0.9f, UiKit.Style.SECONDARY, true, f5, f6, () -> this.applyPicked(colorSetting.B().getRGB() & 0xFF000000 | this.accentArgb() & 0xFFFFFF));
        float f26 = f25 + f22 + 6.0f * f9;
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\udb2d\u44c6\u218b\uf888\ubf69\ud4c3\u129b\u0ab8", -622259345, 0x1B1EF9FC ^ CryptoVault.mask()), f18, f26, f23, f22, f9 * 0.9f, UiKit.Style.SECONDARY, true, f5, f6, () -> this.kitHost.setClipboard(GpuClickGuiScreen.hexOf(colorSetting.B().getRGB(), colorSetting.B().getAlpha() != 255)));
        UiKit.button(builder, this.kitHost, GpuClickGuiScreen.bf("\uf989\u9520\u7a8d\ua435\u9678\u99ed\u2504", 1283806763, 0x1B1EF9FC ^ CryptoVault.mask()), f18 + f19 - f23, f26, f23, f22, f9 * 0.9f, UiKit.Style.SECONDARY, true, f5, f6, () -> {
            Integer n2 = GpuClickGuiScreen.parseHex(this.kitHost.clipboard());
            if (n2 != null) {
                this.applyPicked(n2);
            }
        });
        float f27 = f26 + f22 + 12.0f * f9;
        float f28 = Math.round(22.0f * f9);
        GpuClickGuiScreen.textMid(builder, f18, f27 + f28 * 0.5f, 10.5f * f9, this.textColor, GpuClickGuiScreen.bf("\u62b6\u5930\u729b\u392b\u0541\u73a6\ubfcd\u8eff\u11eb", -1156252493, 0x1B1EF9FC ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f29 = 30.0f * f9;
        float f30 = 16.0f * f9;
        this.drawToggleSwitch(builder, GpuClickGuiScreen.bf("\ufced\u93e9\u3128\u7b97\u7ebc\u9fc7\u69c2\ua29b\u269a\u8717\u8ccd\u726a\u6e93\u7fa2\u4692\ue415\u5ed0\uf46f", -1050072060, 0x1B1EF9FC ^ CryptoVault.mask()), f18 + f19 - f29, f27 + (f28 - f30) * 0.5f, f29, f30, colorSetting.v(), f9);
        this.hit(f18, f27, f19, f28, () -> colorSetting.K(!colorSetting.v()));
        if (colorSetting.v()) {
            float f31 = f27 + f28 + 6.0f * f9;
            String string = String.format(Locale.ROOT, GpuClickGuiScreen.bf("\u68d5\u221b\u628c\u052e\u8060\u5e5b\u05fb\uf132\u7850\ue416\u56fe\uaf76\u240d\u7372", 274787708, 0x1B1EF9FC ^ CryptoVault.mask()), colorSetting.A());
            GpuClickGuiScreen.textMid(builder, f18, f31 + 6.0f * f9, 9.0f * f9, this.mutedColor, string, UiDrawCommand.TextWeight.REGULAR);
            float f32 = f31 + 16.0f * f9;
            float f33 = Math.max(2.0f, (float)Math.round(4.0f * f9));
            this.rainbowSpeedX = f18;
            this.rainbowSpeedW = f19;
            double d2 = (colorSetting.A() - 0.1) / 4.9;
            float f34 = (float)((double)f19 * Math.max(0.0, Math.min(1.0, d2)));
            builder.rect(f18, f32, f19, f33, f33 * 0.5f, this.controlFill(36));
            if (f34 > 0.5f) {
                this.accentFill(builder, f18, f32, Math.max(f33, f34), f33, f33 * 0.5f, 1.0f, this.sliderFillColor);
            }
            builder.circle(f18 + f34, f32 + f33 * 0.5f, 5.0f * f9, -723208);
            float f35 = f18;
            float f36 = f19;
            this.hit(f18 - 4.0f * f9, f32 - 8.0f * f9, f19 + 8.0f * f9, f33 + 16.0f * f9, () -> {
                this.colorDrag = ColorDrag.RAINBOW_SPEED;
                if (f36 <= 0.0f) {
                    return;
                }
                double d2 = GpuClickGuiScreen.clamp01((float)((this.pressX - (double)f35) / (double)f36));
                colorSetting.A(0.1 + d2 * 4.9);
            });
        }
        builder.text(f12 + 20.0f * f9, f13 + f11 - 18.0f * f9, 9.0f * f9, this.muted2Color, UiKit.fit(GpuClickGuiScreen.bf("\u1051\u20ce\uafed\u94aa\u6b6f\u3750\u0bb8\uf313\u3832\u9734\uf678\u0283\u9b62\udb70\u943d\u1500\u05bf\ud96c\udb70\ub225\ubfba\ufef1\u6020\u6d1c\u5596\u8265\u4cc0\ub934\u3b15\u85a2\uad08\u2b6a\u0328\ufa24\u18af\u9e5f\ucedb\ub64e\u0fa2\u08d0\u6751\ub6fd\uf83a\u3d37\u23a1\u3a37\ua91a\u67b9\u7e2d\u7259\u8940\ud145\u9297\u9dc7\u0eae\uc7f9\u92ba\u1cc7\ud396\u2cd1\u06fd\u1aed\u7fa4\u9d79\uaf74\u11e0\u3534\u2cc7\uc30a\u84ad\u8df1\u4f23", 32385603, 0x1B1EF9FC ^ CryptoVault.mask()), f10 - 40.0f * f9, 9.0f * f9));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean method_25402(class_11909 class_119092, boolean bl) {
        if (this.touch.intercepts(class_119092)) {
            return this.touch.press(class_119092, bl);
        }
        if (this.closing) {
            return true;
        }
        UiTextTarget uiTextTarget = this.kitFocus;
        this.kitFocus = null;
        StringSetting stringSetting = this.editingString;
        int n2 = this.editBeginCount;
        boolean bl2 = false;
        UiSounds.beginPress();
        try {
            bl2 = this.handleMouseClicked(class_119092, bl);
        }
        finally {
            UiSounds.endPress(bl2);
        }
        if (uiTextTarget != null && this.kitFocus != uiTextTarget) {
            uiTextTarget.onBlur();
        }
        if (stringSetting != null && this.editingString == stringSetting && this.editBeginCount == n2) {
            this.editingString.L(false);
            this.editingString = null;
        }
        return bl2;
    }

    public ConfigsView configsView() {
        return this.configsView;
    }

    public StudioTab macroView() {
        return this.macroView;
    }

    public StudioTab scriptView() {
        return this.scriptView;
    }

    public PeopleView peopleView() {
        return this.peopleView;
    }

    private boolean handleMouseClicked(class_11909 class_119092, boolean bl) {
        Hit hit;
        int n2;
        float f2 = this.pixelsPerGuiUnit();
        double d2 = class_119092.comp_4798() * (double)f2;
        double d3 = class_119092.comp_4799() * (double)f2;
        this.pressX = d2;
        this.pressY = d3;
        this.navVisible = false;
        this.panning = false;
        this.flingVel = 0.0f;
        int n3 = class_119092.method_74245();
        if (this.isCapturingKeybind()) {
            switch (this.captureOrigin.action(n3, d2, d3, System.currentTimeMillis())) {
                case CANCEL: {
                    this.cancelKeybindCapture();
                    break;
                }
                case BIND: {
                    this.captureKeybind(KeybindCapture.encodeMouse(n3));
                    break;
                }
            }
            return true;
        }
        this.captureOrigin.clear();
        if (n3 == 2 || n3 == 1) {
            this.secondaryIsMiddle = n3 == 2;
            for (n2 = this.moduleCardRightHits.size() - 1; n2 >= 0; --n2) {
                hit = this.moduleCardRightHits.get(n2);
                if (!hit.contains(d2, d3)) continue;
                hit.action().run();
                return true;
            }
        }
        if (n3 == 0) {
            this.cancelCardDrag();
            for (n2 = this.hits.size() - 1; n2 >= 0; --n2) {
                hit = this.hits.get(n2);
                if (!hit.contains(d2, d3)) continue;
                Runnable runnable = hit.action();
                if (runnable instanceof CardPress) {
                    CardPress cardPress = (CardPress)runnable;
                    if (this.catalogReorderable && this.contextItems == null) {
                        this.pendingCardPress = cardPress;
                        this.pendingPressX = d2;
                        this.pendingPressY = d3;
                        return true;
                    }
                }
                boolean bl2 = this.isCapturingKeybind();
                hit.action().run();
                if (!bl2 && this.isCapturingKeybind()) {
                    this.captureOrigin.mark(hit.x(), hit.y(), hit.w(), hit.h(), System.currentTimeMillis());
                }
                return true;
            }
        }
        if (this.contextItems != null) {
            this.closeContextMenu();
            return true;
        }
        return super.method_25402(class_119092, bl);
    }

    public boolean method_25406(class_11909 class_119092) {
        if (this.touch.intercepts(class_119092)) {
            return this.touch.release((class_364)this, class_119092);
        }
        if (this.panning) {
            this.flingVel = ResponsiveLayout.flingFor(this.panVel, this.nanoTime() - this.panLastNs);
            this.flingX = (float)this.pressX;
            this.flingY = (float)this.pressY;
            this.panning = false;
        }
        if (class_119092.method_74245() == 0) {
            if (this.dragModule != null) {
                float f2 = this.pixelsPerGuiUnit();
                this.dragPointerX = (float)(class_119092.comp_4798() * (double)f2);
                this.dragPointerY = (float)(class_119092.comp_4799() * (double)f2);
                this.commitCardDrag();
            } else if (this.pendingCardPress != null) {
                CardPress cardPress = this.pendingCardPress;
                this.pendingCardPress = null;
                cardPress.run();
            }
        }
        this.windowDrag = 0;
        this.tour.mouseReleased();
        this.cosmeticsView.mouseReleased();
        if (this.kitFocus != null) {
            this.kitFocus.mouseReleased();
        }
        this.draggingSlider = null;
        this.crosshairPaintValue = -1;
        this.colorDrag = ColorDrag.NONE;
        this.settingPicker.release();
        return super.method_25406(class_119092);
    }

    public boolean method_25403(class_11909 class_119092, double d2, double d3) {
        if (this.touch.intercepts(class_119092)) {
            return this.touch.drag((class_364)this, class_119092, d2, d3);
        }
        float f2 = this.pixelsPerGuiUnit();
        double d4 = class_119092.comp_4798() * (double)f2;
        double d5 = class_119092.comp_4799() * (double)f2;
        if (this.crosshairMaker != null) {
            if (this.crosshairPaintValue != -1) {
                this.paintCrosshairAt(d4, d5);
            }
            return true;
        }
        if (this.tour.active() && this.tour.mouseDragged(d4, d5)) {
            return true;
        }
        if (this.pendingCardPress != null || this.dragModule != null) {
            if (this.dragModule == null && Math.hypot(d4 - this.pendingPressX, d5 - this.pendingPressY) > 6.0 * (double)Math.max(1.0f, this.lastFitS)) {
                Module module = this.pendingCardPress.module;
                this.pendingCardPress = null;
                if (this.catalogReorderable && this.lastCatalogModules.contains(module)) {
                    this.dragModule = module;
                    this.dragVisible = List.copyOf(this.lastCatalogModules);
                    this.dragDropIndex = -1;
                }
            }
            this.dragPointerX = (float)d4;
            this.dragPointerY = (float)d5;
            return true;
        }
        if (this.windowDrag != 0) {
            this.dragWindow(d4, d5);
            return true;
        }
        if (this.kitFocus != null && this.kitFocus.mouseDragged(d4, d5)) {
            return true;
        }
        if (this.view == View.COSMETICS && this.cosmeticsView.mouseDragged(d4, d5)) {
            return true;
        }
        Setting setting = this.draggingSlider;
        if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            if (this.sliderW > 0.0f) {
                double d6 = GpuClickGuiScreen.clamp01((float)((d4 - (double)this.sliderX) / (double)this.sliderW));
                double d7 = sliderSetting.B();
                sliderSetting.B(sliderSetting.D() + d6 * (sliderSetting.E() - sliderSetting.D()));
                int n2 = (int)Math.floor(d6 * 20.0);
                if (sliderSetting.B() != d7 && n2 != this.sliderSoundNotch) {
                    this.sliderSoundNotch = n2;
                    UiSounds.play(UiSounds.Event.SLIDER);
                }
                return true;
            }
        }
        if (this.colorPicker != null && this.settingPicker.drag(d4, d5)) {
            return true;
        }
        if (this.colorDrag == ColorDrag.RAINBOW_SPEED && this.rainbowSpeedW > 0.0f && this.colorPicker != null) {
            double d8 = GpuClickGuiScreen.clamp01((float)((d4 - (double)this.rainbowSpeedX) / (double)this.rainbowSpeedW));
            this.colorPicker.A(0.1 + d8 * 4.9);
            return true;
        }
        if (class_119092.method_74245() == 0 && this.panDrag(d4, d5)) {
            return true;
        }
        return super.method_25403(class_119092, d2, d3);
    }

    private float panStepPx(double d2, double d3) {
        float f2 = Math.max(1.0f, this.lastFitS);
        boolean bl = this.view == View.MODULES ? this.inspectorRevealW > 0.0f && GpuClickGuiScreen.inside(d2, d3, this.inspectorRevealX, this.inspectorRevealY, this.inspectorRevealW, this.inspectorRevealH) : d2 >= (double)(this.inspectorBoundaryX > 0.0f ? this.inspectorBoundaryX : (float)this.field_22789 * this.pixelsPerGuiUnit() * 0.5f);
        return bl ? 24.0f : 56.0f * f2;
    }

    private boolean panDrag(double d2, double d3) {
        if (this.contextItems != null || this.colorPicker != null || this.modalOverlayOpen()) {
            return false;
        }
        long l2 = this.nanoTime();
        if (!this.panning) {
            if (Math.hypot(d2 - this.pressX, d3 - this.pressY) <= (double)(6.0f * Math.max(1.0f, this.lastFitS))) {
                return false;
            }
            this.panning = true;
            this.panLastY = d3;
            this.panVel = 0.0f;
            this.panLastNs = l2;
            return true;
        }
        float f2 = (float)(d3 - this.panLastY);
        this.panLastY = d3;
        this.panVel = ResponsiveLayout.blendVelocity(this.panVel, f2, (float)(l2 - this.panLastNs) / 1.0E9f);
        this.panLastNs = l2;
        float f3 = this.pixelsPerGuiUnit();
        this.method_25401(this.pressX / (double)f3, this.pressY / (double)f3, 0.0, f2 / this.panStepPx(this.pressX, this.pressY));
        return true;
    }

    public boolean method_25401(double d2, double d3, double d4, double d5) {
        boolean bl;
        float f2 = this.pixelsPerGuiUnit();
        d2 *= (double)f2;
        d3 *= (double)f2;
        long l2 = this.nanoTime();
        float f3 = Math.max(1.0f, this.lastFitS);
        if (this.whatsNew.isOpen()) {
            return this.whatsNew.scroll(d2, d3, d5, f3);
        }
        if (this.tour.active()) {
            this.tour.scroll(d5);
            return true;
        }
        if (this.contextItems != null) {
            this.closeContextMenu();
            return true;
        }
        if (this.colorPicker != null || this.crosshairMaker != null) {
            return true;
        }
        if (this.settingsDrawerOpen || this.themesDrawerOpen) {
            if (this.drawerRegionW > 0.0f && GpuClickGuiScreen.inside(d2, d3, this.drawerRegionX, this.drawerRegionY, this.drawerRegionW, this.drawerRegionH)) {
                if (this.themesDrawerOpen && this.themeMakerOpen && this.themeMaker.scroll(d2, d3, d5, l2)) {
                    return true;
                }
                if (this.settingsDrawerOpen && this.settingsTab == SettingsTab.CONFIG_BINDS) {
                    this.hotkeyScroll = Math.max(0.0f, this.hotkeyScroll - (float)d5 * 36.0f * f3);
                } else {
                    this.appearanceScroll = Math.max(0.0f, this.appearanceScroll - (float)d5 * 36.0f);
                }
            }
            return true;
        }
        if (this.view == View.CONFIGS && this.configsView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view == View.MACROS && this.macroView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view == View.SCRIPTS && this.scriptView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view == View.FRIENDS && this.peopleView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view == View.KEYBINDS && this.keybindsView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view == View.COSMETICS && this.cosmeticsView.scroll(d2, d3, d5, l2)) {
            return true;
        }
        if (this.view != View.MODULES) {
            return true;
        }
        boolean bl2 = bl = this.inspectorRevealW > 0.0f && GpuClickGuiScreen.inside(d2, d3, this.inspectorRevealX, this.inspectorRevealY, this.inspectorRevealW, this.inspectorRevealH);
        if (!bl && this.moduleFloatActive()) {
            return true;
        }
        if (bl) {
            this.moduleInspectorScrollRemainder += d5 * 24.0;
            int n2 = (int)this.moduleInspectorScrollRemainder;
            if (n2 != 0) {
                this.settingsScroll = Math.max(0, this.settingsScroll - n2);
                this.moduleInspectorScrollRemainder -= (double)n2;
            }
        } else {
            this.catalogScroll.wheel(d5, 56.0f * f3, l2);
        }
        return true;
    }

    public boolean method_25404(class_11908 class_119082) {
        boolean bl;
        boolean bl2;
        int n2 = class_119082.comp_4795();
        this.noteKeySeen(n2);
        if (this.isCapturingKeybind()) {
            this.swallowCharsUntilMs = System.currentTimeMillis() + 250L;
            this.captureKeybind(n2);
            return true;
        }
        if (this.whatsNew.isOpen()) {
            if (n2 == 256) {
                this.whatsNew.close();
            }
            return true;
        }
        if (this.tour.active()) {
            if (n2 == 256) {
                this.tour.finish();
            } else if (n2 == 262 || n2 == 257 || n2 == 335) {
                this.tour.next();
            } else if (n2 == 263) {
                this.tour.back();
            }
            return true;
        }
        if (this.contextItems != null && n2 != 256) {
            this.closeContextMenu();
        }
        boolean bl3 = this.editingString != null || this.newConfigFocused || this.kitFocus != null;
        int n3 = class_119082.comp_4797();
        boolean bl4 = (n3 & 0xA) != 0;
        boolean bl5 = bl2 = (n3 & 1) != 0;
        if (n2 == 256) {
            if (this.contextItems != null) {
                this.closeContextMenu();
                return true;
            }
            if (this.dragModule != null || this.pendingCardPress != null) {
                this.cancelCardDrag();
                return true;
            }
            if (this.crosshairMaker != null) {
                this.closeCrosshairMaker();
                return true;
            }
            if (this.colorPicker != null) {
                this.closeColorPicker();
                return true;
            }
            if (this.editingString != null) {
                this.editingString.L(false);
                this.editingString = null;
                return true;
            }
            if (this.kitFocus != null) {
                if (this.kitFocus == this.sliderEdit.field()) {
                    this.sliderEdit.cancel();
                }
                this.clearTextFocus();
                return true;
            }
            if (this.newConfigFocused) {
                this.newConfigFocused = false;
                return true;
            }
            if (this.themesDrawerOpen && this.themeMakerOpen) {
                this.themeMakerOpen = false;
                this.appearanceScroll = 0.0f;
                return true;
            }
            if (this.settingsDrawerOpen || this.themesDrawerOpen) {
                this.settingsDrawerOpen = false;
                this.themesDrawerOpen = false;
                return true;
            }
            if (this.view == View.CONFIGS && this.configsView.editing()) {
                this.configsView.closeEdit();
                return true;
            }
            if (this.view == View.SCRIPTS && this.scriptView.fullscreen()) {
                this.scriptView.exitFullscreen();
                return true;
            }
            if (GpuClickGuiScreen.floatingView(this.view)) {
                this.closePage();
                return true;
            }
            if (this.moduleFloatActive() && this.moduleInspectorTargetProgress > 0.0f) {
                this.animateModuleInspectorTo(0.0f);
                return true;
            }
            this.method_25419();
            return true;
        }
        if (n2 == GpuClickGuiScreen.guiKey()) {
            if (!this.guiKeyReleasedSinceOpen) {
                return true;
            }
            if (bl3) {
                return true;
            }
            if (this.crosshairMaker != null) {
                this.closeCrosshairMaker();
                return true;
            }
            if (this.colorPicker != null) {
                this.closeColorPicker();
                return true;
            }
            this.method_25419();
            return true;
        }
        if (bl4 && n2 == 70 && this.colorPicker == null && this.crosshairMaker == null && this.editingString == null && (this.kitFocus == null || this.kitFocus == this.searchField)) {
            this.focusSearch();
            this.searchField.selectAll();
            return true;
        }
        boolean bl6 = bl = this.view == View.MODULES && this.colorPicker == null && this.crosshairMaker == null && this.editingString == null && !this.settingsDrawerOpen && !this.themesDrawerOpen;
        if (bl && this.kitFocus == this.searchField) {
            switch (n2) {
                case 264: 
                case 265: {
                    this.moveNav(n2);
                    return true;
                }
                case 257: 
                case 335: {
                    this.activateNav(bl2);
                    return true;
                }
            }
        } else if (bl && this.kitFocus == null && !this.newConfigFocused) {
            switch (n2) {
                case 262: 
                case 263: 
                case 264: 
                case 265: {
                    if (!this.moveNav(n2)) break;
                    return true;
                }
                case 257: 
                case 335: {
                    if (!this.navVisible || this.navIndex < 0) break;
                    this.activateNav(bl2);
                    return true;
                }
            }
        }
        if (this.kitFocus != null && this.kitFocus.keyPressed(n2, n3)) {
            return true;
        }
        if (this.editingString != null) {
            if (n2 == 257 || n2 == 335) {
                this.editingString.L(false);
                this.editingString = null;
                return true;
            }
            if (bl4 && n2 == 86 && this.field_22787 != null) {
                String string = this.field_22787.field_1774.method_1460();
                if (string != null && !string.isEmpty()) {
                    this.editingString.G(this.editingString.X() + string.replaceAll(GpuClickGuiScreen.bf("\u4146\uafbe\u8ca8\u1d5d\uf0c2\u110b\u891f\u92cb\u39af\u2b15\uaa27\u2d8f\u984a", 2110344896, 0x1B1EF9FC ^ CryptoVault.mask()), ""));
                }
                return true;
            }
        }
        if (n2 == 259) {
            if (this.newConfigFocused && !this.newConfigName.isEmpty()) {
                this.newConfigName = GpuClickGuiScreen.removeLastCodePoint(this.newConfigName);
                return true;
            }
            if (this.editingString != null && !this.editingString.X().isEmpty()) {
                this.editingString.G(GpuClickGuiScreen.removeLastCodePoint(this.editingString.X()));
                return true;
            }
        }
        return super.method_25404(class_119082);
    }

    private void focusSearch() {
        if (this.view != View.MODULES) {
            this.view = View.MODULES;
        }
        this.closeDrawers();
        this.focusView = View.MODULES;
        this.kitHost.focus(this.searchField);
    }

    protected final void noteKeySeen(int n2) {
        this.lastKeySeen = n2;
        this.lastKeySeenAt = System.currentTimeMillis();
    }

    public boolean sawKeyRecently(int n2, long l2) {
        return n2 == this.lastKeySeen && System.currentTimeMillis() - this.lastKeySeenAt <= l2;
    }

    private static int guiKey() {
        Module module = ModuleAccess.getModule(GpuClickGuiScreen.bf("\u0f10\u7ddd\ua610\ua803\u73e0\u76f9\u09c1\udb44\ub374\u9a52\u5e87", 985049639, 0x1B1EF9FC ^ CryptoVault.mask()));
        int n2 = module == null ? -1 : module.getKeyCode();
        return n2 == -1 || n2 == 0 ? 344 : n2;
    }

    public boolean isCapturingKeybind() {
        return this.bindingModuleKey || this.binding != null || this.bindingBool != null || this.bindingConfigName != null || this.kitKeyCapture != null;
    }

    public boolean capturedKeyRecently(int n2, long l2) {
        return n2 == this.capturedKey && System.currentTimeMillis() - this.capturedKeyAtMs <= l2;
    }

    protected void noteCapturedKey(int n2) {
        this.capturedKey = n2;
        this.capturedKeyAtMs = System.currentTimeMillis();
        UiSounds.play(n2 == 256 ? UiSounds.Event.CLICK : UiSounds.Event.KEYBIND);
    }

    private void captureKeybind(int n2) {
        this.captureOrigin.clear();
        this.noteCapturedKey(n2);
        if (n2 == 256) {
            n2 = -1;
        }
        if (this.bindingBool != null) {
            this.bindingBool.setKeyCode(n2);
            this.bindingBool = null;
            return;
        }
        if (this.kitKeyCapture != null) {
            IntConsumer intConsumer = this.kitKeyCapture;
            this.kitKeyCapture = null;
            intConsumer.accept(n2 == 256 ? -1 : n2);
            return;
        }
        if (this.bindingModuleKey) {
            if (!(this.bindingModuleTarget == null || this.bindingModuleTarget instanceof HitboxConfigScreen752809 && n2 == -1)) {
                this.bindingModuleTarget.setKeyCode(n2);
            }
            if (this.bindingModuleTarget != null) {
                this.bindingModuleTarget.setBinding(false);
            }
            this.bindingModuleKey = false;
            this.bindingModuleTarget = null;
        } else if (this.binding != null) {
            Module module = this.binding.a();
            if (module != null) {
                if (!(module instanceof HitboxConfigScreen752809) || n2 != -1) {
                    module.setKeyCode(n2);
                }
                module.setBinding(false);
            }
            this.binding = null;
            this.bindingModuleTarget = null;
        } else if (this.bindingConfigName != null) {
            String string = this.bindingConfigName;
            this.bindingConfigName = null;
            ConfigKeybindManager.getInstance().setKeybind(string, n2, -1);
        }
    }

    public boolean method_25400(class_11905 class_119052) {
        int n2 = class_119052.comp_4793();
        if (this.modalOverlayOpen()) {
            return true;
        }
        if (!Character.isValidCodePoint(n2) || Character.isISOControl(n2)) {
            return super.method_25400(class_119052);
        }
        if (this.swallowCharsUntilMs != 0L) {
            boolean bl = System.currentTimeMillis() <= this.swallowCharsUntilMs;
            this.swallowCharsUntilMs = 0L;
            if (bl) {
                return true;
            }
        }
        if (this.isCapturingKeybind()) {
            return true;
        }
        String string = new String(Character.toChars(n2));
        if (this.kitFocus != null) {
            return this.kitFocus.charTyped(string);
        }
        if (this.editingString != null) {
            this.editingString.G(this.editingString.X() + string);
            return true;
        }
        if (this.newConfigFocused) {
            this.newConfigName = this.newConfigName + string;
            return true;
        }
        if (!(this.view != View.MODULES || this.colorPicker != null || this.settingsDrawerOpen || this.themesDrawerOpen || Character.isWhitespace(n2))) {
            this.focusSearch();
            return this.searchField.charTyped(string);
        }
        return super.method_25400(class_119052);
    }

    private void beginEdit(StringSetting stringSetting) {
        this.clearTextFocus();
        ++this.editBeginCount;
        this.editingString = stringSetting;
        stringSetting.L(true);
    }

    private void clearTextFocus() {
        if (this.kitFocus != null) {
            UiTextTarget uiTextTarget = this.kitFocus;
            this.kitFocus = null;
            uiTextTarget.onBlur();
        }
        if (this.editingString != null) {
            this.editingString.L(false);
        }
        this.editingString = null;
        this.newConfigFocused = false;
    }

    private void hit(float f2, float f3, float f4, float f5, Runnable runnable) {
        Hit hit;
        if (this.clippingHits) {
            float f6 = Math.min(f2 + f4, this.clipX + this.clipW);
            float f7 = Math.min(f3 + f5, this.clipY + this.clipH);
            f2 = Math.max(f2, this.clipX);
            f3 = Math.max(f3, this.clipY);
            f4 = f6 - f2;
            f5 = f7 - f3;
            if (f4 <= 0.0f || f5 <= 0.0f) {
                return;
            }
        }
        if ((hit = GpuClickGuiScreen.intersectHit(new Hit(f2, f3, f4, f5, runnable), this.inspectorRevealX, this.inspectorRevealY, this.inspectorRevealW, this.inspectorRevealH, this.inspectorRevealHitClip)) != null) {
            this.hits.add(hit);
        }
    }

    private boolean guiKeyHeld() {
        try {
            long l2 = this.field_22787 == null ? 0L : this.field_22787.method_22683().method_4490();
            return l2 != 0L && GLFW.glfwGetKey((long)l2, (int)GpuClickGuiScreen.guiKey()) == 1;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public boolean method_16803(class_11908 class_119082) {
        if (class_119082.comp_4795() == GpuClickGuiScreen.guiKey()) {
            this.guiKeyReleasedSinceOpen = true;
        }
        return super.method_16803(class_119082);
    }

    private boolean shiftHeld() {
        try {
            long l2 = this.field_22787 == null ? 0L : this.field_22787.method_22683().method_4490();
            return l2 != 0L && (GLFW.glfwGetKey((long)l2, (int)340) == 1 || GLFW.glfwGetKey((long)l2, (int)344) == 1);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private void hitModuleCardRight(float f2, float f3, float f4, float f5, Runnable runnable) {
        Hit hit;
        if (this.clippingHits) {
            float f6 = Math.min(f2 + f4, this.clipX + this.clipW);
            float f7 = Math.min(f3 + f5, this.clipY + this.clipH);
            f2 = Math.max(f2, this.clipX);
            f3 = Math.max(f3, this.clipY);
            f4 = f6 - f2;
            f5 = f7 - f3;
            if (f4 <= 0.0f || f5 <= 0.0f) {
                return;
            }
        }
        if ((hit = GpuClickGuiScreen.intersectHit(new Hit(f2, f3, f4, f5, runnable), this.inspectorRevealX, this.inspectorRevealY, this.inspectorRevealW, this.inspectorRevealH, this.inspectorRevealHitClip)) != null) {
            this.moduleCardRightHits.add(hit);
        }
    }

    private static Hit intersectHit(Hit hit, float f2, float f3, float f4, float f5, boolean bl) {
        if (!bl) {
            return hit;
        }
        float f6 = Math.max(hit.x(), f2);
        float f7 = Math.max(hit.y(), f3);
        float f8 = Math.min(hit.x() + hit.w(), f2 + f4);
        float f9 = Math.min(hit.y() + hit.h(), f3 + f5);
        if (f8 <= f6 || f9 <= f7) {
            return null;
        }
        return new Hit(f6, f7, f8 - f6, f9 - f7, hit.action());
    }

    private void rebuildInputTree(float f2, float f3) {
        this.inputRoot.setBounds(new UiBounds(0.0f, 0.0f, f2, f3));
        for (int i2 = 0; i2 < this.hits.size(); ++i2) {
            ActionElement actionElement;
            Hit hit = this.hits.get(i2);
            if (i2 < this.inputNodes.size()) {
                actionElement = this.inputNodes.get(i2);
            } else {
                actionElement = new ActionElement(GpuClickGuiScreen.bf("\u343f\u9477\u8722\u6fba\uf7e4\udbdc\u8cf1\u0a2b\u9bf6\u81ea", -912473837, 0x1B1EF9FC ^ CryptoVault.mask()) + i2, hit.action());
                this.inputNodes.add(actionElement);
                this.inputRoot.add(actionElement);
            }
            actionElement.setAction(hit.action());
            actionElement.setBounds(new UiBounds(hit.x(), hit.y(), hit.w(), hit.h()));
            actionElement.setEnabled(true);
        }
        while (this.inputNodes.size() > this.hits.size()) {
            ActionElement actionElement = this.inputNodes.remove(this.inputNodes.size() - 1);
            this.inputRoot.remove(actionElement);
        }
    }

    private static boolean inside(double d2, double d3, double d4, double d5, double d6, double d7) {
        return d2 >= d4 && d3 >= d5 && d2 <= d4 + d6 && d3 <= d5 + d7;
    }

    protected boolean reducedMotion() {
        return HitboxConfigScreen752809.j != null && HitboxConfigScreen752809.j.isEnabled();
    }

    private int accentArgb() {
        return this.currentAccent;
    }

    private int popupTint(int n2) {
        return (n2 & 0xFF) << 24 | GpuClickGuiScreen.darken(UiKit.panelOpaque(), 0.15f) & 0xFFFFFF;
    }

    private int controlFill(int n2) {
        return (n2 & 0xFF) << 24 | this.controlRgb & 0xFFFFFF;
    }

    private static int brighten(int n2, float f2) {
        return GpuClickGuiScreen.mixRgb(n2, -1, f2);
    }

    private static int darken(int n2, float f2) {
        return GpuClickGuiScreen.mixRgb(n2, -16777216, f2);
    }

    private static int mixRgb(int n2, int n3, float f2) {
        float f3 = GpuClickGuiScreen.clamp01(f2);
        int n4 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f3) + (float)(n3 >> 16 & 0xFF) * f3);
        int n5 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f3) + (float)(n3 >> 8 & 0xFF) * f3);
        int n6 = Math.round((float)(n2 & 0xFF) * (1.0f - f3) + (float)(n3 & 0xFF) * f3);
        return 0xFF000000 | n4 << 16 | n5 << 8 | n6;
    }

    private boolean hovered(double d2, double d3, float f2, float f3, float f4, float f5) {
        if (!GpuClickGuiScreen.inside(d2, d3, f2, f3, f4, f5)) {
            return false;
        }
        if (this.clippingHits && !GpuClickGuiScreen.inside(d2, d3, this.clipX, this.clipY, this.clipW, this.clipH)) {
            return false;
        }
        return !this.inspectorRevealHitClip || GpuClickGuiScreen.inside(d2, d3, this.inspectorRevealX, this.inspectorRevealY, this.inspectorRevealW, this.inspectorRevealH);
    }

    private static float clamp01(float f2) {
        return Math.max(0.0f, Math.min(1.0f, f2));
    }

    private static String removeLastCodePoint(String string) {
        return string.substring(0, string.offsetByCodePoints(string.length(), -1));
    }

    private static List<String> wrapText(String string, float f2, float f3, int n2) {
        if (string == null || string.isBlank() || f2 <= 0.0f || n2 <= 0) {
            return new ArrayList<String>();
        }
        WrapKey wrapKey = new WrapKey(string, f2, f3, n2, GpuUiFontMetrics.textWidth("M", 100.0f));
        List<String> list = WRAP_CACHE.get(wrapKey);
        if (list != null) {
            return list;
        }
        List<String> list2 = List.copyOf(GpuClickGuiScreen.wrapTextUncached(string, f2, f3, n2));
        WRAP_CACHE.put(wrapKey, list2);
        return list2;
    }

    private static List<String> wrapTextUncached(String string, float f2, float f3, int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String[] stringArray = string.split(GpuClickGuiScreen.bf("\u0a65\ucda8\u9efa\u2e8e\u21cf\udd6c\uaed4", 791847752, 0x1B1EF9FC ^ CryptoVault.mask()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : stringArray) {
            String string3;
            if (string2.isEmpty()) continue;
            String string4 = string3 = stringBuilder.length() == 0 ? string2 : stringBuilder + " " + string2;
            if (GpuUiFontMetrics.textWidth(string3, f3) <= f2) {
                stringBuilder.setLength(0);
                stringBuilder.append(string3);
                continue;
            }
            if (stringBuilder.length() > 0) {
                arrayList.add(stringBuilder.toString());
                stringBuilder.setLength(0);
                if (arrayList.size() >= n2) break;
            }
            while (string2.length() > 1 && GpuUiFontMetrics.textWidth(string2, f3) > f2 && arrayList.size() < n2) {
                int n3;
                for (n3 = 1; n3 < string2.length() && GpuUiFontMetrics.textWidth(string2.substring(0, n3 + 1), f3) <= f2; ++n3) {
                }
                if (Character.isHighSurrogate(string2.charAt(n3 - 1)) && n3 < string2.length()) {
                    ++n3;
                }
                arrayList.add(string2.substring(0, n3));
                string2 = string2.substring(n3);
            }
            if (arrayList.size() >= n2) break;
            stringBuilder.append(string2);
        }
        if (stringBuilder.length() > 0 && arrayList.size() < n2) {
            arrayList.add(stringBuilder.toString());
        }
        if (!arrayList.isEmpty()) {
            int n4 = 0;
            for (String string5 : arrayList) {
                n4 += string5.replaceAll(GpuClickGuiScreen.bf("\ucbb5\u22c6\u4652\u843e", -1601096708, 0x1B1EF9FC ^ CryptoVault.mask()), "").length();
            }
            if (n4 < string.replaceAll(GpuClickGuiScreen.bf("\uc078\u5636\u4e3f\u1eb4\u8f32", -1458817927, 0x1B1EF9FC ^ CryptoVault.mask()), "").length()) {
                int n5 = arrayList.size() - 1;
                String string6 = ((String)arrayList.get(n5)).stripTrailing();
                while (!string6.isEmpty()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    if (!(GpuUiFontMetrics.textWidth(stringBuilder2.append(string6).append("\u2026").toString(), f3) > f2)) break;
                    string6 = string6.substring(0, string6.length() - 1).stripTrailing();
                }
                string6 = string6.replaceAll(GpuClickGuiScreen.bf("\u9801\u2821\uc94f\u4fb4\u3327\u1124\u259a\u4df9\ue8e6\uea39\ubf7b\uae7c\u2119\ufdf4\ub1d6", 161658529, 0x1B1EF9FC ^ CryptoVault.mask()), "");
                arrayList.set(n5, string6 + "\u2026");
            }
        }
        return arrayList;
    }

    protected static String safeText(String string) {
        return string == null ? "" : string;
    }

    private static String fitTail(String string, float f2, float f3) {
        if (string == null) {
            return "";
        }
        if (GpuUiFontMetrics.textWidth(string, f3) <= f2) {
            return string;
        }
        String string2 = "\u2026";
        float f4 = f2 - GpuUiFontMetrics.textWidth(string2, f3);
        int n2 = 0;
        int n3 = string.length();
        while (n2 < n3) {
            int n4 = n2 + n3 >>> 1;
            if (GpuUiFontMetrics.textWidth(string.substring(n4), f3) <= f4) {
                n3 = n4;
                continue;
            }
            n2 = n4 + 1;
        }
        if (n2 < string.length() && Character.isLowSurrogate(string.charAt(n2))) {
            ++n2;
        }
        return string2 + string.substring(n2);
    }

    /*
     * Exception decompiling
     */
    private static String sliderUnit(String var0, double var1_1) {
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

    protected static String stripUnitSuffix(String string) {
        if (string == null) {
            return "";
        }
        int n2 = string.lastIndexOf(40);
        int n3 = string.lastIndexOf(41);
        if (n2 > 0 && n3 == string.length() - 1 && !GpuClickGuiScreen.sliderUnit(string, 0.0).isEmpty()) {
            return string.substring(0, n2).trim();
        }
        return string;
    }

    protected static String keyLabel(int n2) {
        return n2 == -1 || n2 == 0 ? GpuClickGuiScreen.bf("\ub80e\u6739\u36cf\u66c6\uceb0", -2006042016, 0x1B1EF9FC ^ CryptoVault.mask()) : Module.getSingleKeyName(n2);
    }

    static {
        COLLAPSED_GROUPS = new HashSet<String>();
        DRAWER_FLOAT = new Object();
        CONFIG_EDIT_FLOAT = new Object();
        STANDARD_CURSORS = new HashMap<Integer, Long>();
        STAR_X = new float[10];
        STAR_Y = new float[10];
        for (int i2 = 0; i2 < 10; ++i2) {
            double d2 = Math.toRadians(-90 + i2 * 36);
            double d3 = i2 % 2 == 0 ? 1.0 : 0.45;
            GpuClickGuiScreen.STAR_X[i2] = (float)(Math.cos(d2) * d3);
            GpuClickGuiScreen.STAR_Y[i2] = (float)(Math.sin(d2) * d3);
        }
        SUB_VISIBLE = new WeakHashMap<Setting, Boolean>();
        SUB_DEPTH = new WeakHashMap<Setting, Integer>();
        WRAP_CACHE = new LinkedHashMap<WrapKey, List<String>>(256, 0.75f, true){

            @Override
            protected boolean removeEldestEntry(Map.Entry<WrapKey, List<String>> entry) {
                return this.size() > 768;
            }
        };
    }

    private static /* synthetic */ String bf(String string, int n2, int n3) {
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
    private static enum View {
        MODULES,
        CONFIGS,
        MACROS,
        SCRIPTS,
        FRIENDS,
        SELF_DESTRUCT,
        COSMETICS,
        LAYOUT,
        SS_PROOF,
        KEYBINDS;


        private static /* synthetic */ String bo(String string, int n2, int n3) {
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
    private static enum SettingsTab {
        CLIENT_CONTROLS,
        CONFIG_BINDS;


        private static /* synthetic */ String oq(String string, int n2, int n3) {
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
    protected static enum ColorDrag {
        NONE,
        RAINBOW_SPEED;


        private static /* synthetic */ String uo(String string, int n2, int n3) {
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
    private final class KitHost
    implements UiHost {
        private KitHost() {
        }

        @Override
        public void hit(float f2, float f3, float f4, float f5, Runnable runnable) {
            GpuClickGuiScreen.this.hit(f2, f3, f4, f5, runnable);
        }

        @Override
        public void hitSecondary(float f2, float f3, float f4, float f5, Runnable runnable) {
            GpuClickGuiScreen.this.hitModuleCardRight(f2, f3, f4, f5, runnable);
        }

        @Override
        public void beginHitClip(float f2, float f3, float f4, float f5) {
            GpuClickGuiScreen.this.clipX = f2;
            GpuClickGuiScreen.this.clipY = f3;
            GpuClickGuiScreen.this.clipW = f4;
            GpuClickGuiScreen.this.clipH = f5;
            GpuClickGuiScreen.this.clippingHits = true;
        }

        @Override
        public void endHitClip() {
            GpuClickGuiScreen.this.clippingHits = false;
        }

        @Override
        public boolean hovered(float f2, float f3, float f4, float f5, float f6, float f7) {
            return GpuClickGuiScreen.this.hovered(f2, f3, f4, f5, f6, f7);
        }

        @Override
        public int accent() {
            return GpuClickGuiScreen.this.accentArgb();
        }

        @Override
        public int textColor() {
            return GpuClickGuiScreen.this.textColor;
        }

        @Override
        public int mutedColor() {
            return GpuClickGuiScreen.this.mutedColor;
        }

        @Override
        public int muted2Color() {
            return GpuClickGuiScreen.this.muted2Color;
        }

        @Override
        public int titleColor() {
            return GpuClickGuiScreen.this.titleColor;
        }

        @Override
        public float cornerScale() {
            return GpuClickGuiScreen.this.cornerScale;
        }

        @Override
        public void panel(UiDisplayList.Builder builder, float f2, float f3, float f4, float f5, float f6) {
            if (GpuClickGuiScreen.this.lg) {
                GpuClickGuiScreen.this.glassPane(builder, f2, f3, f4, f5, f6, 1.0f, 68, false, 0);
            } else if (GpuClickGuiScreen.this.floatMaterial) {
                builder.clipPush(f2, f3, f4, f5, f6);
                GpuClickGuiScreen.this.paintSurface(builder, GpuClickGuiScreen.this.floatTheme, f2, f3, f4, f5, f6, GpuClickGuiScreen.this.floatMist);
                builder.rect(f2, f3, f4, f5, 0.0f, GpuClickGuiScreen.this.listBg);
                builder.clipPop();
            } else {
                builder.rect(f2, f3, f4, f5, f6, GpuClickGuiScreen.this.popupTint(128));
                builder.glass(f2, f3, f4, f5, f6, GpuClickGuiScreen.this.floatBlur, GpuClickGuiScreen.this.floatTint);
                GpuClickGuiScreen.this.drawGlassWash(builder, f2, f3, f4, f5, f6);
            }
        }

        @Override
        public int lineColor() {
            return GpuClickGuiScreen.this.lineColor;
        }

        @Override
        public int cardFill() {
            return GpuClickGuiScreen.this.cardBg;
        }

        @Override
        public int controlFill(int n2) {
            return GpuClickGuiScreen.this.controlFill(n2);
        }

        @Override
        public long now() {
            return GpuClickGuiScreen.this.nanoTime();
        }

        @Override
        public float frameDelta() {
            return GpuClickGuiScreen.this.frameDelta;
        }

        @Override
        public float anim(String string, float f2, float f3) {
            return GpuClickGuiScreen.this.anim(string, f2, f3);
        }

        @Override
        public double pressX() {
            return GpuClickGuiScreen.this.pressX;
        }

        @Override
        public double pressY() {
            return GpuClickGuiScreen.this.pressY;
        }

        @Override
        public void focus(UiTextTarget uiTextTarget) {
            if (uiTextTarget == GpuClickGuiScreen.this.kitFocus) {
                return;
            }
            GpuClickGuiScreen.this.clearTextFocus();
            GpuClickGuiScreen.this.kitFocus = uiTextTarget;
            if (uiTextTarget != null) {
                uiTextTarget.onFocus();
            }
        }

        @Override
        public UiTextTarget focused() {
            return GpuClickGuiScreen.this.kitFocus;
        }

        @Override
        public void captureKey(IntConsumer intConsumer) {
            GpuClickGuiScreen.this.clearTextFocus();
            GpuClickGuiScreen.this.cancelKeybindCapture();
            GpuClickGuiScreen.this.kitKeyCapture = intConsumer;
        }

        @Override
        public boolean capturingKey() {
            return GpuClickGuiScreen.this.kitKeyCapture != null;
        }

        @Override
        public String clipboard() {
            return GpuClickGuiScreen.this.field_22787 != null && ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1774 != null ? ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1774.method_1460() : "";
        }

        @Override
        public void setClipboard(String string) {
            if (GpuClickGuiScreen.this.field_22787 != null && ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1774 != null) {
                ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1774.method_1455(string);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    private final class ConfigsBackend
    implements ConfigsView.Backend {
        private ConfigsBackend() {
        }

        @Override
        public List<String> savedConfigs() {
            return GpuClickGuiScreen.this.savedConfigs;
        }

        @Override
        public ConfigsView.Info info(String string) {
            PrivateConfigManager.PrivateConfigInfo privateConfigInfo = GpuClickGuiScreen.this.cloudProfiles.get(string.toLowerCase(Locale.ROOT));
            return privateConfigInfo == null ? null : new ConfigsView.Info(privateConfigInfo.id, privateConfigInfo.gameMode, privateConfigInfo.active, privateConfigInfo.isPublic, privateConfigInfo.tags);
        }

        @Override
        public boolean signedIn() {
            return CloudConfigs.signedIn();
        }

        @Override
        public void load(String string) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u1a18\uf10f\udedf\u4128\ub0a3\u59bf\u897e\u3cfb\u4825\u1944\ub4ab\u8a73\uf18c\u57de\u9936\u0eb3\u00d6\ub221\ucf10\ue354\u776e\u150c\u6172\u1783\ua34c\ua23f\u5132\u9426\ufbd0\ud226\ub696", -1768323988, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.profileLoadPending = true;
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u5f64\u0e2a\u013a\u3093\u4716\u43b0\u6798\ud792\uca80\u9268\u43af", -113543453, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\u2c4c\ucd0a\u41b5\u95e3\uc2f3\uf370\u9337\u4256\u94d6\u84cc\uacd5\u4680\u6563\uc150\u4011\ucd8b\u25ce", 431361889, 0x69D72D63 ^ CryptoVault.mask());
            PrivateConfigManager.getInstance().loadConfig(string, true, bl -> {
                GpuClickGuiScreen.this.profileLoadPending = false;
                if (!bl.booleanValue()) {
                    GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\ucfe4\u9b53\u14a0\u7a33\u89b1\uc0e8\u1066\u1350\u6f47\u58d2\u7206\u0cc2\u5f65\u9e71\uac7f\u02e6\u1988", 546113705, 0x69D72D63 ^ CryptoVault.mask()) + string + "\"";
                    return;
                }
                lastCloudConfigStatusMessage = GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\uba4e\uf18d\u3d39\u9f4a\u6e1f\u5882\ue8e9\u4547\uf598\u9450", 1392599968, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\u1cf4\u1586\u91fa\u619b\ufd55\ucecd\u90fe\u3a58=\u67e3\ua320\ufceb\u0a60\ubba7\u828a\u9011\uc55e\u5755\u347e\ue1b1\u7a4a\u3ac3\u7e6c\u7f7f\uc68f\u2de9\u9423\u75f5\ua5bd\ub277\u1ef9\ua110\u71e8\ub4f0\ud7ac\uf9b9\u87d0\ua78e\ub5b5\u6232\u3f50\u328c\ua7e5\u358b\u4c2d\u1566\uf92c\u4244\udaba\udc65\u14f8\uf22f", -853652905, 0x69D72D63 ^ CryptoVault.mask());
                if (GpuClickGuiScreen.this.closing) {
                    lastCloudConfigStatusMessage = ConfigsBackend.xw("\u48d7\u717a\u5906\u1faa\u633bf\u296f\u5dbf\uc473\u0717\ue758\u91e1\ua4bb\u377b\uee83\u736c\u4865\ua4d3\u0690\u89bd\u8999\u75ed\ucff4\ua06d\ue9da\ua22a\u018e", -1176709878, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\uc27a\u5e0c\u63f8\ua0fa\u5b72\u5837", -1502666265, 0x69D72D63 ^ CryptoVault.mask());
                    PrivateConfigManager.getInstance().activateConfig(string, () -> {
                        lastCloudConfigStatusMessage = ConfigsBackend.xw("\u4336\u79f5\ua748\u2d3c\u675a\u217e\u4c29\u2a1a\ua7f8\u54d8\u7065\u945b\ufaf1\ua652\u546a\u258e\u1f7e\u03a2\u5bcd\u76f1\u018c\u2558\ua332\u4bf4", -1475548783, 0x69D72D63 ^ CryptoVault.mask()) + string + "\".";
                    }, string -> {
                        lastCloudConfigStatusMessage = ConfigsBackend.xw("\u5404\u7b49\u8505\udcbe\u90c9\u01f0\uc840\u3269\u41c5\u07bb\u13ea\udca5\u4c76\u7db1\u363d\uf6c4\u9bb9\u734d}\ub311\u5b10\u23ff\u3f45\u7917\u8dad\u6966\u18ba\ub9bf\u8ebf\u0aed\uc0fa\u86e7\u7d6d\u9324\ue457", 778431548, 0x69D72D63 ^ CryptoVault.mask()) + string;
                    });
                } else {
                    GpuClickGuiScreen.this.pendingProfileActivationName = string;
                }
            }, string2 -> {
                GpuClickGuiScreen.this.profileLoadPending = false;
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u84cf\u3ff1\ucbec\u5c5b\ua375\ua541\u711f\ud7f5\u95c5\ud878\u4986\uff84\u49ea\u11f1\ua437\ubca8\u415b\uc5e7\ub3d1", 2141098003, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\uaa62\u739c\u51d3\u05ea", 2040334640, 0x69D72D63 ^ CryptoVault.mask()) + string2;
            });
        }

        @Override
        public void save(String string, boolean bl, List<String> list) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\uca8a\u1dbc\u40d0\u9046\u9de7\uf699\u013c\uf45c\u15b8\u83df\u1788\ua62d\u4d4e\uac6c\u9d48\uc0b0\u23e4\ub60e\ub650\u0d74\u3e79\u4658\ub7ec\u48df\ucacd\u5f65\ueeb5\uca6e\ud293\uc337\ucbfa\uab94\u0e54\u192b\u148b\ub18b\u2e17\u8cd2\uf6d9\u14eb\ub9b2\uf14a\udbc3\u328d\u7692\u55a1\u179b\u16a6\u2144\uee39\ucd77\u14e7\uae5f\u6344\u4a77\uc10b\u82aa\u73d3\ud5e7\u7f1c\u3ff4\u7099\u5ac1\ud06b\u24b2\uf399\uf530\u331d\ubde5\u8e40\u7a5d\u36ba\u6770\ufcbc\u82af\u28e6\u30cc\ufeb8", 859002080, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            boolean bl2 = GpuClickGuiScreen.this.cloudProfiles.containsKey(string.toLowerCase(Locale.ROOT));
            if (!bl2 && GpuClickGuiScreen.cloudSlotsUsed(GpuClickGuiScreen.this.cloudProfiles) >= PremiumGate.cloudConfigLimit()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u737d\ud608\ud2d9\ued01\u2e32\ud23b\u161f\u6292\u6408\ufbd2\u59f8\u6426\u0190\u1d7f\u4321\ud43e\u46e7\ua91b\u249c\uf8b1\ua2b0\u58a3\ud935\u1904\u8c4c\uee80", -1220865947, 0x69D72D63 ^ CryptoVault.mask()) + PremiumGate.cloudConfigLimit() + ConfigsBackend.xw("\u7fb9\u87b4\uaefb\u4b6f\u218b\u44ee\ud21b\ucb41\u0b86\u472e\u6a7b\u7222\u92bf\u8757\u4263\ucb81\u2ba2\ufee2\ufdc7\u3748\u2ce8\ub3ed\u20ac", 57826294, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.pendingProfileSaveName = string;
            GpuClickGuiScreen.this.pendingSavePublic = bl;
            GpuClickGuiScreen.this.pendingSaveTags = list == null ? List.of() : List.copyOf(list);
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\ue829\ueb8c\ud351\ud12a\u6657\u6ca6\u83fe\ufc37\u18c7\ueeb0\ue37d\u7393\u8777\u523a", 354822975, 0x69D72D63 ^ CryptoVault.mask()) + string + "\"" + (bl ? ConfigsBackend.xw("\ub8fc\uc9d7\u641d\u5dcf\u71a7\ua8f8\u4d04\uda56\u448e\u99c4\u5633", -1996365024, 0x69D72D63 ^ CryptoVault.mask()) : "") + (list != null && !list.isEmpty() ? ConfigsBackend.xw("\u64bd\ud6b0\u098d\u52fb\u24f8\u25c6\u8665\u94f9\u008c\u4e83\u961e\uff6c\u238a\ue965", 882771932, 0x69D72D63 ^ CryptoVault.mask()) : "") + ConfigsBackend.xw("\ucc87\ua23d\ufdf8\u6631\u8a7a\u56a4\ue908\u59a0\udc3c\ud020\u85b9\uaa6a\u6955\ueecb\u15d8\ue788\ue0ad\u28db\u2105\u65a3\ued17\u652f\uea2f\u90f4\u764d\u720f", -1087195203, 0x69D72D63 ^ CryptoVault.mask());
        }

        @Override
        public void deleteCloud(String string) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u7b25\u228c\ud834\u0c88\u8b5c\u3739\u3e11\u14fb\u02f2\u2041\ucd71\u44b2\u2fad\u8b9b\uc226\u5722\u64c6\u7762", 1410425355, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u1549\u3736\ue4e8\uc8e3\u551e\u276e\ubd4c\u6684\u0c5d\ua5cc\u1bd3\u765e", -1631135139, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\ubeb1\u203d\uacea\u37ed\ucc60\ua2e5\uf500\u5bc9", -365995392, 0x69D72D63 ^ CryptoVault.mask());
            GpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.deleteNamed(string), bl -> {
                GpuClickGuiScreen.this.configStatusMessage = bl != false ? ConfigsBackend.xw("\u1c47\uf94d\u2109\u94fd\u92ec\ub098\ufda5\ue5d5\u631f\uc564\u08d6\u5a7b", -518770808, 0x69D72D63 ^ CryptoVault.mask()) + string + "\"" : ConfigsBackend.xw("\u8803\uf045\uff85\u9592\u85e8\u2dbc\uf74e\u83c5\u6a3a\u11b9\u5e85\ue22f\uec95\u6bbb\ue1c7\u1a5f\uf62a\ufaa2", -575653995, 0x69D72D63 ^ CryptoVault.mask()) + GpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void setPublic(String string, boolean bl) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u4d2c\u489d\uc8be\u8065\u192f\u3a6e\ub7e5\u3073\u7a62\uce04\u61f6\u030b\ub474\u3d40\uaa98", 1752854586, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u958d\u9887\u4a3a\u77e6\u36d9\uba38\u06ed\u9ae3\u29b5\ud952\u3964\ue1dc\ud690\uccfc\uefd4\u4277\u570f\u6760\u90bf\u5c0e\u2151\u65dd\uaa84\u5d3d\u0d23\u00fa", 1518084763, 0x69D72D63 ^ CryptoVault.mask());
            GpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.setPublic(string, bl), bl2 -> {
                GpuClickGuiScreen.this.configStatusMessage = bl2 != false ? "\"" + string + ConfigsBackend.xw("\u947f\u777a\u060d\u1e4a\u0815\u0013\u35fc\u4b81\uf6b5\u0dbd\u238b", 2026428898, 0x69D72D63 ^ CryptoVault.mask()) + (bl ? ConfigsBackend.xw("\u32e6\u2084\u1cd6\u85bc\u2214\u21ef\ube8d\uaacb\ub2bd", 1391187702, 0x69D72D63 ^ CryptoVault.mask()) : ConfigsBackend.xw("\u516a\u1dae\ue49c\u543d\ub86b\ua952\u27b6\u6e77\u589c\u6c42\u12e1", -1969370094, 0x69D72D63 ^ CryptoVault.mask())) : ConfigsBackend.xw("\u59fa\u86b6\ue8b7\u5474\u68a3\ue611\u23c6\u8546\ufbe6\u0dff\u5b31\u9a56\u81a9\ue59a\u3fd2\u1773\u03f1\ue01a\u1faf\u7801\u045d\u4b6a\u1e1f\u0996\u231e\u8b4f\u94ac\u17ba\u8ef0", -181967392, 0x69D72D63 ^ CryptoVault.mask()) + GpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void rename(String string, String string2) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u7cf5\u4896\ufad6\u0232\ud443\u3991\ucb6f\u9e52\u3fdc\u5fe2\ud972\u70f4\u3bea\ubd4c\u4a46\uf25a", -1043250133, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u31f9\ue77a\ueef3\u65cc\ufaa7\u7c99\u5b31\uf00f\u55f3\ue31a\u2824\uf7c8\u1006", 1660277804, 0x69D72D63 ^ CryptoVault.mask());
            GpuClickGuiScreen.this.runCloudOp(() -> {
                String string3 = CloudConfigs.idForName(string);
                return string3 != null && CloudConfigs.renameById(string3, string2);
            }, bl -> {
                GpuClickGuiScreen.this.configStatusMessage = bl != false ? ConfigsBackend.xw("\u7e43\u99eb\u64fb\u08bd\u6b55\uaec7\u44da\uacb1\uc9c4\uc22f\u3ed5\u30e4\u4e3c\ue66f\u47b9\ub385", -566119442, 0x69D72D63 ^ CryptoVault.mask()) + string2 + "\"" : ConfigsBackend.xw("\uc749\u8896\u1cb5\u28ba\u1683\ua954\u9cff\u534b\ub65c\ue788\u1e72\u6473\u0177\u05a4\u8af5\ue4f1\ue669\u3286", 1191060613, 0x69D72D63 ^ CryptoVault.mask()) + GpuClickGuiScreen.cloudError();
            });
        }

        @Override
        public void setTags(String string, List<String> list) {
            if (!CloudConfigs.signedIn()) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u0b22\uefda\ue744\ub9a1\uce37\u896a\u7c5d\u8f83\uf98a\uc631\ued76\uc04f\u9c40\ufb89\u30a4", 1845286837, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.runCloudOp(() -> {
                String string2 = CloudConfigs.idForName(string);
                return string2 != null && CloudConfigs.setTagsById(string2, list);
            }, bl -> {
                if (!bl.booleanValue()) {
                    GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u5505\uc246\u9f42\u7d6e\uc774\u601a\u0d4c\uef0a\u7445\ua076\ufb30\u2836\u776c\u12f0\uc527\u3bd7\u0920\u12e6\u10e8\u949e\u4f87\u5367\u9604\u23cc\u0902", -227878745, 0x69D72D63 ^ CryptoVault.mask()) + GpuClickGuiScreen.cloudError();
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
            String string5 = !CloudConfigs.signedIn() ? ConfigsBackend.xw("\u3394\u9508\ub763\ub16c\uadac\u5d8c\u101d\u729b\u1f19\ue2dd\u2c7a\ua1c4\u81b1\u58a2\u2704\u8fe6\u3162\uc5ba\u999a\u2994\u3fbc\ueb53\u49ab\uc6ea\u84ed\ubf46\u22ed\u9aa1\u6449\ud006\u3e7b\u4044\udcec\u9904\ua27d\uf7c8\u9954\u89d9\u17ed\u7d3d\u5311\u0f9c\ue0c3\u55a3\uc92e\u6595\u310b\u21d1\u34b9\u8a91\uf59a\u0ec7\u503d\u3fbd\uce64\u41fa\u6daa\u4c30\u5f6b\ud528\u6805\u1912\u756f\u1164\u8e67\u042c\u9f3f\u1995\ue55f\u1e84\u145c\ue0cf\u84da\ucda4\udba6", -1924629586, 0x69D72D63 ^ CryptoVault.mask()) : (GpuClickGuiScreen.this.cloudProfiles.containsKey(string.toLowerCase(Locale.ROOT)) ? ConfigsBackend.xw("\ub18b\u0a68\u4226\uc540\u1f65\u1024\u9b12\ub577\u5b7f\u1bdb\u9b74\u7354\ud482\u7503\u3aa7\u2191\uadfb\u29a5\u90c1\u3b0d\u19c3\ud60b\uae99\ua828\u657c\u634a\ub883\u483b\ue0bd\ua87e\uafe8\ua95d\uce0a\u00c9\ub707\ucf95\u4c24", -1376254532, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\ua946\uf4d2\u050a\u77aa\u5f35\u47fa\u11dd\ub249\u1ff3\u356b\ufe62\uc8f0\u3e8d\u67fc\uc157\u17da\ude55\uda04\uba40\u2dea\ubd2d\u3958", 1726064355, 0x69D72D63 ^ CryptoVault.mask()) : (string4 = GpuClickGuiScreen.cloudSlotsUsed(GpuClickGuiScreen.this.cloudProfiles) >= PremiumGate.cloudConfigLimit() ? ConfigsBackend.xw("\uaaa2\uc750\u6810\u1dd7\u7b32\ub020\u12bc\u99cb\ufad9\ucd8e\uadf5\ud891\ue675\u9364\uf369\uea5d\uee45\uca98\u70c8\u2131\ua782\uc03f\ucedf\ua0f0\u063f\u3ac2", 1211701009, 0x69D72D63 ^ CryptoVault.mask()) + PremiumGate.cloudConfigLimit() + ConfigsBackend.xw("\u1ba2\ua742\u10ad\u2f09\ua3e1\ua1ba\u474e\uc951\uae50\u36d0\u15ee\u33c9\u0e32\u791e\u82a6\ub1e3\ufe38\u85f0\u1274\u1cf0\u97e3\u9c60", 1122925730, 0x69D72D63 ^ CryptoVault.mask()) : null));
            if (string4 != null) {
                GpuClickGuiScreen.this.configStatusMessage = string4;
                consumer.accept(false);
                return;
            }
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u1cab\u3c33\ua5b3\uc18c\u2dac\ud5ff\u83d8\u019e\uc6ed\ud51b\ud47b", -1379600936, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\u821a\u5aff\u2b12\u918b\u0e0b", -899777378, 0x69D72D63 ^ CryptoVault.mask());
            GpuClickGuiScreen.this.runCloudOp(() -> CloudConfigs.saveNamed(string, string2, false, string3, ConfigsBackend.xw("\u4cde\u04e4\u4ea7\udb47\ubbb4\ubb59\u657e\b\u080b\uf768\uda05\u0df6\ua985\u9990\u242f\ub0de\u644a\u3bb5\u2a6c\uae1e\ub164\u6105\u555e\u2277\u965e", -1786998101, 0x69D72D63 ^ CryptoVault.mask()), list) != null, bl2 -> {
                consumer.accept((Boolean)bl2);
                if (!bl2.booleanValue()) {
                    GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u9cc8\u80e1\u829f\u568d\udee9\u972b\u9019\u3092\u890d\uf6da\u31c9\u9dce\uc29c\uf465\u7598", -515977488, 0x69D72D63 ^ CryptoVault.mask()) + GpuClickGuiScreen.cloudError();
                    return;
                }
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\ubea2\u5338\ue76c\ud9ea\u5647\u6985\u7ced\uebc9\u0595\u5d38\u333d", 647178641, 0x69D72D63 ^ CryptoVault.mask()) + string + ConfigsBackend.xw("\u6192\u63a1\u8d06\ucb7f\ube07\ud066\ub6d0\u7917\u6544\uf1da\u4edd\u1086\u0406\u1b92\ubfb8\u0c8a\u3e5f\ucee4\u9987\u8a57\u55fd\uade7\u2132\ue4d2\uf68f\u98b6\ubacb\ub125", 1149721081, 0x69D72D63 ^ CryptoVault.mask());
                if (bl) {
                    this.load(string);
                }
            });
        }

        @Override
        public boolean canDeleteLocal(String string) {
            return ConfigManager.isSaveLocalConfig() && !ConfigsBackend.xw("\ucc80\u7586\uee50\u6015\uf380\uc7cd\u6b54\u3e42\u111c\u52e1", 182895119, 0x69D72D63 ^ CryptoVault.mask()).equalsIgnoreCase(string);
        }

        @Override
        public void deleteLocal(String string) {
            try {
                if (!ConfigManager.getInstance().delete(string)) {
                    GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u553c\u295d\uc3ca\u4ed4\u9eb9\u98b0\ua29c\u99f7\u55ca\u663f\u6708\uda55\u27c6\u4c99\u1a6a\u1da4\ud555\u2ea7\u1eb6\u4d13\u8ce3", -1547075866, 0x69D72D63 ^ CryptoVault.mask()) + string + "\"";
                    return;
                }
                GpuClickGuiScreen.this.refreshSavedConfigs();
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\uec29\ucb38\u9d42\u2914\u89d1\u3365\u99a8\u5c81\u3712\u3c26\u69cb\u0bfc\u9e3b", 654095992, 0x69D72D63 ^ CryptoVault.mask()) + string + "\"";
            }
            catch (Exception exception) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u5444\ucf61\u5262\uc6b3\u6cad\u393f\u779b\ue3e4\u51a6\u7f1e\u3398\uff62\u96eb\ub656\u7235\u4c19\u9ea6\u2a46\ub34d", -1412486536, 0x69D72D63 ^ CryptoVault.mask()) + exception.getMessage();
            }
        }

        @Override
        public int cloudLimit() {
            return PremiumGate.cloudConfigLimit();
        }

        @Override
        public int cloudCount() {
            return GpuClickGuiScreen.cloudSlotsUsed(GpuClickGuiScreen.this.cloudProfiles);
        }

        @Override
        public String status() {
            return GpuClickGuiScreen.this.configStatusMessage;
        }

        @Override
        public void setStatus(String string) {
            GpuClickGuiScreen.this.configStatusMessage = string == null ? "" : string;
        }

        @Override
        public void closeMenu() {
            GpuClickGuiScreen.this.method_25419();
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
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\ub867\uc9fe\u8f0c\ua071\u8532\ud539\u4a29\u4505\u1dc4\u8452\ud676\u2d50\u4dab\u2c2a\ub547\ud80f\u3868\u0659\ua839\u1162\ubbe6\u2f22\ue078\u2824\u2f9c\u398d", 649202364, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            if (GpuClickGuiScreen.this.profileLoadPending) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u30fd\u1469\ud72c\u4399\u7958\ud6be\ub2e6\u1c63\uea7a\u7852\uef9b\ufae2\ua725\u20f4\u0587\u3fb2\u7b51\uc797\u1c70\ud6f1\u161b\u6079\u4a72\u4f71\u398d\uf50e\ucbcd\u30b3\u94b6\uec19\u4df6\uc127\ue6df\u07ed\u6a70\uee40\u85f7\u1858\u8be9\ue3e9\ubb55\uac23", -920092477, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            String string2 = ConfigManager.getInstance().getLoadedConfigName();
            if (string2 == null) {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u299f\u5e42\ue135\u8cb5\u5408\uf337\ud412\ue363\ud595\u0d89\u0b60\udbc5\ub4c7\u3124\ud74b\u790b\u5d83\udb10\u426f\uda8c\u60ce\u8923\ueb40\u9df2\ua111\u2ffc\uc074\ufd09\uffd7\u1dbf\u3552\u827e\u9f62\u7a76\u590b\u8e91\u5f3e\u3374\u14e9\uad57\u59bd\uc335\u8d92\u9e85\u6d82\u0406\ud225\ua170\u429e\u84d5\u0e6b\uda37\ue628\u421a", -1298974632, 0x69D72D63 ^ CryptoVault.mask());
                return;
            }
            GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u233b\ufa7c\ubd46\u6279\u499c\u6069\u9ae6\ua951\uf67d\u19a1\u60d4\ue6e8\ucccb\ub90d\uc3a9\uf4fb\uee95\u0959\u8a82\udd4b\u506c\u1fe8\uf854\u4070\uf0ab\uaa12\u6175", -463614702, 0x69D72D63 ^ CryptoVault.mask()) + string2 + ConfigsBackend.xw("\u742d\u1920\u5bef\u4b2c\udf72\u4114\u3950", 1755146181, 0x69D72D63 ^ CryptoVault.mask());
            PrivateConfigManager.getInstance().overwriteLoaded(string -> {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u2120\u9080\u82a6\u368b\u886f\u4daa\u2d7a\u48ae\ub3b3\uf8c8\u5528\uc49a\u56b6\uaf77\u2439\u277f\u7ed5\u6f96\ube38\u6171\u7ac3\ub74e\ufabc\u8502\u72e4\ude9c\u9fd4", -1892951775, 0x69D72D63 ^ CryptoVault.mask()) + string + "\".";
                GpuClickGuiScreen.this.refreshSavedConfigs();
            }, string -> {
                GpuClickGuiScreen.this.configStatusMessage = ConfigsBackend.xw("\u90cf\uc93c\u9a51\u20d4\u8571\u856f\u35c9\u6f85\u2947\ub5a6\uedab\ua53e\u6bdb\u3acf\u860f\udd7d\ufb02\ubea3\u6d79", 1225651049, 0x69D72D63 ^ CryptoVault.mask()) + string;
            });
        }

        private static /* synthetic */ String xw(String string, int n2, int n3) {
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
    private final class PeopleBackend
    implements PeopleView.Backend {
        private PeopleBackend() {
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
            if (GpuClickGuiScreen.this.field_22787 == null || ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1687 == null) {
                return List.of();
            }
            ArrayList<PeopleView.Person> arrayList = new ArrayList<PeopleView.Person>();
            for (class_1657 class_16572 : ((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1687.method_18456()) {
                if (((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1724 != null && class_16572.method_5667().equals(((GpuClickGuiScreen)GpuClickGuiScreen.this).field_22787.field_1724.method_5667())) continue;
                arrayList.add(new PeopleView.Person(class_16572.method_5477().getString(), class_16572.method_5667(), d.A().B(class_16572.method_5667()), c.A().A(class_16572.method_5667())));
            }
            arrayList.sort(Comparator.comparing(PeopleView.Person::name, String.CASE_INSENSITIVE_ORDER));
            return arrayList;
        }

        @Override
        public String toggleFriend(PeopleView.Person person) {
            if (d.A().B(person.uuid())) {
                d.A().B(person.uuid());
                return PeopleBackend.nn("\u6de1\uf4d0\u5185\u692f\u4914\u71e0\u4971\u9fc9\uecf3\u3035\ue19c\u6f3c\ua16d\u0647\u3257\ua383\u1c8e\u1b47\uc1fb", -992485780, 0x20A89B5B ^ CryptoVault.mask()) + person.name();
            }
            d.A().B(person.name(), person.uuid());
            return PeopleBackend.nn("\ub14f\u2bb2\u19a7\ua46d\u94b2\ue0a6\u9261C\ufa4d\u0a6f\uf79d\u2aa8\ubae4\u2b95", 727933122, 0x20A89B5B ^ CryptoVault.mask()) + person.name();
        }

        @Override
        public String toggleEnemy(PeopleView.Person person) {
            if (c.A().A(person.uuid())) {
                c.A().A(person.uuid());
                return PeopleBackend.nn("\u728c\u28bd\u2808\u6520\uec19\u5745\ub9b2\u054d\uc39a\u00d8\ubcd3\u4c24\u995e\ucde2\udbf5\ueb38", 926539218, 0x20A89B5B ^ CryptoVault.mask()) + person.name();
            }
            c.A().A(person.name(), person.uuid());
            return PeopleBackend.nn("\u70c3\ud9d0\u3719\u58b3\u1d4d\u2246\u338b\u7544\u584c\u2881\ub7b9\u2b2b\u27ae", 39381068, 0x20A89B5B ^ CryptoVault.mask()) + person.name();
        }

        @Override
        public String remove(PeopleView.Person person) {
            if (person.friend()) {
                d.A().B(person.uuid());
            }
            if (person.enemy()) {
                c.A().A(person.uuid());
            }
            return PeopleBackend.nn("\u91f4\u6855\u523d\uba2f\u6eb2\u523c\u0b30\u6148\u10e4\uc1bf\u45d9", 1044635955, 0x20A89B5B ^ CryptoVault.mask()) + person.name();
        }

        private static /* synthetic */ String nn(String string, int n2, int n3) {
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
    protected static enum ModuleFilter {
        ALL(ModuleFilter.zl("\uebd2\ubde5\u4eee\uf370\u935b", 498785685, 0x3CB3B0E ^ CryptoVault.mask())),
        ENABLED(ModuleFilter.zl("\ue57e\ufaaf\u2d67\u2817\u1350\u20f4\ud566\u8bf3\u840c\ua76a", 1493390224, 0x3CB3B0E ^ CryptoVault.mask())),
        FAVORITES(ModuleFilter.zl("\uf59b\u02b9\u04a3\uc61f\ua763\ubb14\u6e33\u440e\uc268\uabb5\uc666\ua11a\u4641", -881663856, 0x3CB3B0E ^ CryptoVault.mask())),
        BOUND(ModuleFilter.zl("\uffda\u53d2\u9134\ucf45\u7f65\ue5b4\u67c4\ud099\u2e7c", -76799816, 0x3CB3B0E ^ CryptoVault.mask()));

        final String label;

        private ModuleFilter(String string2) {
            this.label = string2;
        }

        private static /* synthetic */ String zl(String string, int n2, int n3) {
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
    protected static enum ModuleSort {
        DEFAULT(ModuleSort.wb("\ud7b5\uf19c\u5c71\u7fc7\uc1eb\u46e4\uce5c\u2e95\udbfd", 1359675901, 0x15BB4C6F ^ CryptoVault.mask())),
        AZ(ModuleSort.wb("\u151b\u232d\u4984\u64aa\u658c\uc499\ud057", -661774193, 0x15BB4C6F ^ CryptoVault.mask())),
        ENABLED_FIRST(ModuleSort.wb("\u8f38\u285c\ua5f8\u9539\u4d05\u8f63\ucf57\uc5a4\u340a\ufe15\u7d33\u719f\uefbe\u6257", 1047868182, 0x15BB4C6F ^ CryptoVault.mask()));

        final String label;

        private ModuleSort(String string2) {
            this.label = string2;
        }

        private static /* synthetic */ String wb(String string, int n2, int n3) {
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
    private record Hit(float x, float y, float w, float h, Runnable action) {
        boolean contains(double d2, double d3) {
            return d2 >= (double)this.x && d3 >= (double)this.y && d2 <= (double)(this.x + this.w) && d3 <= (double)(this.y + this.h);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record PageFloatKey(View view) {
    }

    @Environment(value=EnvType.CLIENT)
    private record FolderTarget(String id, float x, float y, float w, float h) {
    }

    @Environment(value=EnvType.CLIENT)
    private record MenuItem(String label, boolean checked, boolean enabled, boolean danger, Runnable action) {
        static MenuItem divider() {
            return new MenuItem(null, false, false, false, null);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class CardPress
    implements Runnable {
        final Module module;
        final Runnable click;

        CardPress(Module module, Runnable runnable) {
            this.module = module;
            this.click = runnable;
        }

        @Override
        public void run() {
            this.click.run();
        }
    }

    @Environment(value=EnvType.CLIENT)
    protected record SettingGroup(String title, List<Setting> settings) {
    }

    @Environment(value=EnvType.CLIENT)
    protected record CatalogEntry(Module module, int rank, String note, int noteMatch) {
    }

    @Environment(value=EnvType.CLIENT)
    protected record QuickPin(Module module, Setting setting) {
    }

    @Environment(value=EnvType.CLIENT)
    private static final class Fold {
        float value;
        long frame = -1L;

        private Fold() {
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class ActionElement
    extends UiElement {
        private Runnable action;

        private ActionElement(String string, Runnable runnable) {
            super(string);
            this.setFocusable(true);
            this.action = runnable;
        }

        private void setAction(Runnable runnable) {
            this.action = runnable;
        }

        @Override
        public boolean onPointerDown(float f2, float f3, int n2) {
            if (n2 != 0) {
                return false;
            }
            this.action.run();
            return true;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record WrapKey(String text, float maxWidth, float fontSize, int maxLines, float scale) {
    }
}

