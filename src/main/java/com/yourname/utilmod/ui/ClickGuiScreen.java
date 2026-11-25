package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.module.impl.GuiThemeModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.setting.StringSetting;
import com.yourname.utilmod.util.ConfigStore;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Classic centered ClickGUI layout. Kept as an alternative to the dropdown
 * menu so private testers can switch styles from Menu Theme -> Menu Layout.
 */
public class ClickGuiScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger("Flux/Menu");
    // ScalableLux classic layout metrics: 24 px window shell, 18 px catalog
    // cards, a roomy rail, 460 px inspector and 58 px cards on a 68 px pitch.
    private static final int TITLE_HEIGHT = 52;
    private static final int RAIL_WIDTH_DEFAULT = 196;
    private static final int SETTINGS_WIDTH = 460;
    // Fallback row pitch; the live value comes from Menu Theme -> Category Spacing.
    private static final int ROW_HEIGHT_DEFAULT = 52;
    private static final int MODULE_ROW_HEIGHT = 58;
    private static final int ROW_GAP = 8;
    private static final int SETTING_ROW_HEIGHT = 46;
    private static final int SETTING_GAP = 0;
    private static final int IPAD = 11;
    private static final int ENUM_ROW_HEIGHT = 18;
    private static final int WINDOW_RADIUS = 16;
    private static final int CARD_RADIUS = 12;
    private static final int RADIUS = 8;

    // Resolved from Menu Theme each frame. The neutral Flux palette is the
    // fallback; every colour below is rebuilt from the selected GuiTheme.
    private int accent = 0xFFB8B8BE;
    private int winBg = 0xFF0C0C0C;
    private int sideBg = 0xFF101010;
    private int cardBg = 0xFF161616;
    private int cardHover = 0xFF1D1D1D;
    private int cardActive = 0xFF242424;
    private int cardEnabled = 0xFF2C2C2C;
    private int fieldBg = 0xFF161616;
    private int border = 0xFF2B2B2B;
    private int textColor = 0xFFF4F4F5;
    private int textDim = 0xFF8E8E93;
    private int toggleOff = 0xFF303030;
    private int accentHi = 0xFFE4E4E7;
    private int accentLo = 0xFF6E6E73;
    private int winTop = 0xFF141414;
    private int statusGreen = 0xFF3DDC84;
    private int titleColor = 0xFFFFFFFF;

    // Live Menu Theme flags.
    private float fontScale = 1.0f;
    private boolean gradientHeader = true;
    private boolean outlines = true;
    private boolean shadows = true;
    private boolean performanceMode = true;
    private boolean lightTheme = false;

    /** The wordmark stays a Flux asset; category chips use vanilla item sprites. */
    private static final Identifier FLUX_LOGO =
        Identifier.of("flux", "textures/gui/categories/flux_logo.png");

    /** Enum.values() allocates a new array on every call; cache it for the menu loop. */
    private static final Category[] CATEGORIES = Category.values();

    /** Typed keys avoid NUL separators in cache identifiers. */
    private record FitKey(int tier, int maxWidth, String value) {}
    private record TrackedFitKey(int gap, int tier, int maxWidth, String value) {}

    private final Map<FitKey, String> fitCache = new HashMap<>();
    private final Map<TrackedFitKey, String> fitTrackedCache = new HashMap<>();
    // Static labels are built once; these caches only refresh when the count changes.
    private final Map<Category, String> categoryCountLabels = new EnumMap<>(Category.class);
    private final Map<Category, String> categoryHeadings = new EnumMap<>(Category.class);
    private int cachedEnabledCount = -1;
    private String cachedActiveLabel;
    private String cachedEnabledLabel;
    private int cachedListSize = -1;
    private int cachedListOn = -1;
    private String cachedListLabel;
    private float cachedGuiScale = -1.0f;
    private int cachedScaleWidth = -1;
    private int cachedScaleHeight = -1;
    private float cachedScaleFactor = -1.0f;

    private final List<Module> modules;
    private final Map<Category, Integer> categoryCounts = new EnumMap<>(Category.class);
    private final Map<Category, List<Module>> modulesByCategory = new EnumMap<>(Category.class);

    private Category selectedCategory = Category.CRYSTAL;
    /** Dropdown variant switches the visible category on hover instead of click. */
    private boolean hoverCategories;

    // Advanced HUD placement settings are cached per module and hidden behind a
    // single collapsible row, so scrolling and hit testing stay allocation-free.
    private Module cachedListsFor;
    private final List<Setting> normalSettings = new ArrayList<>();
    private final List<Setting> advancedSettings = new ArrayList<>();
    private final Set<Module> expandedAdvanced = new HashSet<>();
    private Module settingsModule;
    private String search = "";
    /** Cached once per frame so module rows don't re-trim the query. */
    private boolean searchActive;
    private boolean searchFocused;
    private boolean positioned;
    private boolean roundedEdges = true;

    private List<Module> cachedVisible;
    private String cachedSearch;
    private Category cachedCategory;

    private Module tooltipModule;
    private Module lastTooltipModule;
    private long tooltipStart;
    private List<String> tooltipLines;

    private Setting hoveredSetting;
    private Setting tooltipSetting;
    private long settingTooltipStart;
    private List<String> settingTooltipLines;
    private EnumSetting<?> enumPopup;
    private int enumPopupX;
    private int enumPopupY;
    private int enumPopupW;
    private float enumPopupScroll;

    private final int[] searchBox = new int[4];
    private final int[] themeTagBox = new int[4];
    private final int[] profileBox = new int[4];
    private final int[] closeBox = new int[4];
    private final int[] settingsCloseBox = new int[4];

    private float openProgress;
    private long openStart;
    /** Temporary Y offset while the shell slides into place on open. */
    private float openSlideY;

    // Closing is deferred so the shell can fade out instead of vanishing.
    private boolean closing;
    private long closeStart;
    private float closeProgress;

    // Per-object animation values (0..1): toggles, hovers and sidebar hovers.
    private final Map<Object, Float> toggleAnims = new IdentityHashMap<>();
    private final Map<Object, Float> hoverAnims = new IdentityHashMap<>();
    private final Map<Object, Float> catAnims = new IdentityHashMap<>();
    private long lastFrameNanos;
    private float frameDt = 0.016f;
    private int enabledCount;

    private int windowX;
    private int windowY;
    private boolean draggingWindow;
    private int grabOffsetX;
    private int grabOffsetY;

    private float scroll;
    private float targetScroll;
    private float settingsScroll;
    private float settingsTargetScroll;
    private float highlightY;

    private NumberSetting draggedSetting;
    private KeybindSetting bindingSetting;
    private NumberSetting editingSetting;
    private String editingText;
    private StringSetting editingStringSetting;
    private String editingStringText;
    private int dragTrackX;
    private int dragTrackW;

    // Scrollbar thumb dragging. The 3px thumb is too small to hit reliably, so
    // the whole 12px gutter is interactive and clicking the track pages.
    private boolean draggingScrollbar;
    private boolean dragScrollSettings;
    private int dragScrollTrackY;
    private int dragScrollTrackH;
    private int dragScrollThumbH;
    private int dragScrollMax;
    private double dragScrollGrab;

    private GuiThemeModule themeModule;
    private int[] headerGradient;
    private int cachedGradientWidth = -1;
    private GuiTheme cachedGradientTheme;

    public ClickGuiScreen(List<Module> modules) {
        super(Text.literal("Onyx Client Menu"));
        this.modules = modules;
        for (Module module : modules) {
            categoryCounts.merge(module.getCategory(), 1, Integer::sum);
            modulesByCategory.computeIfAbsent(module.getCategory(), key -> new ArrayList<>()).add(module);
        }
        for (Category category : CATEGORIES) {
            categoryCountLabels.put(category, String.valueOf(categoryCounts.getOrDefault(category, 0)));
            categoryHeadings.put(category, category.getName() + " Modules");
        }
    }

    /** Enables dropdown-style hover navigation for the shared three-column shell. */
    public void setHoverCategories(boolean value) {
        this.hoverCategories = value;
    }

    @Override
    protected void init() {
        super.init();
        if (!positioned) {
            windowX = (vw() - winW()) / 2;
            windowY = (vh() - winH()) / 2;
            highlightY = sidebarTop() + selectedCategory.ordinal() * categoryHeight();
            positioned = true;
            openStart = System.nanoTime();
        }
        clampWindow();
        headerGradient = null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (closing) {
            closeProgress = Math.min(1.0f,
                (System.nanoTime() - closeStart) / 170_000_000.0f);
            if (closeProgress >= 1.0f) {
                super.close();
                return;
            }
        }

        float scale = guiScale();
        // Smoothstep gives a soft start and stop without an extra easing texture.
        openProgress = Math.min(1.0f, (System.nanoTime() - openStart) / 220_000_000.0f);
        float openEase = openProgress * openProgress * (3.0f - 2.0f * openProgress);
        openSlideY = (1.0f - openEase) * 12.0f;
        int mx = (int) (mouseX / scale);
        int my = (int) (mouseY / scale + openSlideY);

        // A resize can shrink the shell below rail + inspector width. Drop the
        // inspector instead of laying out negative-width panels.
        if (settingsModule != null && !settingsPanelFits()) selectSettings(null);

        themeModule = themeModule();
        GuiTheme theme = theme();
        applyTheme(theme);
        roundedEdges = themeModule == null || themeModule.shellRounded();
        fontScale = themeModule == null ? 1.0f : themeModule.fontScale();
        gradientHeader = themeModule == null || themeModule.gradientHeader();
        outlines = themeModule == null || themeModule.outlines();
        shadows = themeModule == null || themeModule.shadow();
        performanceMode = themeModule != null && themeModule.performanceMode();
        if (performanceMode) {
            // Flat fills and no easing/shadows keep the menu cheap to redraw.
            gradientHeader = false;
            shadows = false;
            openSlideY = 0;
            my = (int) (mouseY / scale);
        }
        // DROPDOWN keeps the old hover-navigation behaviour on the same shell.
        hoverCategories = themeModule != null && themeModule.menuLayout() == MenuLayout.DROPDOWN;
        tooltipModule = null;
        hoveredSetting = null;
        searchActive = !search.trim().isEmpty();

        long nowNanos = System.nanoTime();
        frameDt = lastFrameNanos == 0 ? 0.016f : Math.min(0.1f, (nowNanos - lastFrameNanos) / 1.0e9f);
        lastFrameNanos = nowNanos;
        int active = 0;
        for (Module m : modules) if (m.isEnabled()) active++;
        enabledCount = active;
        if (cachedEnabledCount != enabledCount) {
            cachedEnabledCount = enabledCount;
            cachedActiveLabel = enabledCount + " active";
            cachedEnabledLabel = enabledCount + " enabled";
        }

        // Frame-rate independent smoothing: identical feel at 60 and 240 FPS.
        float ease = 1.0f - (float) Math.exp(-Math.max(0.001f, frameDt) * 14.0f);
        scroll += (targetScroll - scroll) * ease;
        settingsScroll += (settingsTargetScroll - settingsScroll) * ease;
        highlightY += (sidebarTop() + selectedCategory.ordinal() * categoryHeight() - highlightY) * ease;
        List<Module> visible = visibleModules();
        clampScrolls(visible);

        int x = windowX;
        int y = windowY;
        int w = winW();
        int h = winH();

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);

        // Classic source: 38->112 vertical gradient plus a flat wash. Performance
        // mode replaces the ~40 gradient bands with two flat full-screen quads.
        int overlay = themeModule != null ? themeModule.overlayColor() : theme.overlay;
        int overlayAlpha = (overlay >>> 24) & 0xFF;
        float overlayStrength = overlayAlpha == 0 ? 1.0f : overlayAlpha / 255.0f;
        if (performanceMode) {
            context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.30f * overlayStrength, overlay & 0xFFFFFF));
            context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.18f, 0x000000));
        } else {
            SlGuiPaint.overlayWashBytes(context, vw(), vh(), overlay, 38, 112);
            context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.25f * overlayStrength, 0x000000));
        }

        // Slide the shell up into place; input coordinates are shifted by the
        // same amount so clicks stay accurate during the short transition.
        context.getMatrices().translate(0.0f, -openSlideY);

        // Soft drop shadow, a faint accent halo, then the rim and a vertical-gradient body.
        if (shadows) {
            for (int i = 6; i >= 1; i--) {
                fillRounded(context, x - i * 2, y - i * 2 + 6,
                    x + w + i * 2, y + h + i * 2 + 6,
                    argb(0x000000, 0.055f), r(WINDOW_RADIUS) + i * 2);
            }
            for (int i = 3; i >= 1; i--) {
                fillRounded(context, x - i * 2, y - i * 2,
                    x + w + i * 2, y + h + i * 2,
                    argb(accent, 0.045f), r(WINDOW_RADIUS) + i * 2);
            }
        }
        if (outlines) {
            fillRounded(context, x - 1, y - 1, x + w + 1, y + h + 1,
                argb(accent, 0.55f), r(WINDOW_RADIUS) + 1);
        }
        gradientOrFill(context, x, y, w, h, winTop, winBg, r(WINDOW_RADIUS));
        context.fill(x + r(WINDOW_RADIUS), y + 1, x + w - r(WINDOW_RADIUS), y + 2,
            argb(0xFFFFFF, 0.06f));
        int cx = x + w / 2;
        context.fill(cx - 90, y - 1, cx + 90, y, argb(accentHi, 0.35f));
        context.fill(cx - 50, y - 1, cx + 50, y, argb(accentHi, 0.85f));

        drawTitleBar(context, mx, my, theme);
        drawSidebar(context, mx, my, theme);
        drawModuleList(context, mx, my, theme, visible);
        if (settingsModule != null) drawSettingsPanel(context, mx, my, theme);
        if (enumPopup != null) drawEnumPopup(context, theme);

        if (draggedSetting != null) {
            updateSlider(draggedSetting, mx, dragTrackX, dragTrackW);
        }
        updateTooltip(context, mx, my, theme);
        updateSettingTooltip(context, mx, my, theme);

        // Final fade: fades the whole shell in, including the accent rim.
        float fade = (1.0f - openEase) * 0.55f;
        if (fade > 0.001f) {
            int slide = Math.round(openSlideY);
            context.fill(0, slide, vw(), vh() + slide,
                SlGuiPaint.alpha(fade, 0x000000));
        }
        if (closing) {
            float out = closeProgress * closeProgress * (3.0f - 2.0f * closeProgress);
            context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.72f * out, 0x000000));
        }
        context.getMatrices().popMatrix();
    }

    private void drawTitleBar(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        int x = windowX;
        int y = windowY;
        int w = winW();
        int mid = TITLE_HEIGHT / 2;

        fillRounded(context, x + 15, y + mid - 14, x + 43, y + mid + 14, argb(accent, 0.16f), r(10));
        gradientOrFill(context, x + 18, y + mid - 11, 22, 22,
            accentHi, accentLo, r(7));
        drawMenuIcon(context, FLUX_LOGO, x + 19, y + mid - 10, 20, 1.0f);
        ttl(context, textRenderer, "Onyx", x + 50, y + mid - 7,
            ttlColor(textColor), 15.0f);
        boolean showTheme = themeModule == null || themeModule.showThemeName();
        String tag = fit(textRenderer, showTheme ? theme.getLabel() : "Client", 96, 9.5f);
        int tagW = tw(textRenderer, tag, 9.5f) + 14;
        int tagX = x + 50 + tw(textRenderer, "Onyx", 15.0f) + 8;
        themeTagBox[0] = tagX;
        themeTagBox[1] = y + mid - 8;
        themeTagBox[2] = tagW;
        themeTagBox[3] = 15;
        boolean tagHover = isHovered(mouseX, mouseY, themeTagBox);
        fillRounded(context, tagX, y + mid - 8, tagX + tagW, y + mid + 7,
            argb(accent, tagHover ? 0.30f : 0.18f), r(7));
        txt(context, textRenderer, tag, tagX + 7, y + mid - 4, ttlColor(accentHi), 9.5f);

        if (outlines) {
            context.fill(x + 1, y + TITLE_HEIGHT - 1, x + w - 1, y + TITLE_HEIGHT, border);
        }

        int[] close = closeBounds();
        if (w >= 520) {
            String activeText = cachedActiveLabel;
            int aw = tw(textRenderer, activeText, 9.5f) + 30;
            int ax = close[0] - 14 - aw;
            fillRounded(context, ax, y + mid - 11, ax + aw, y + mid + 11, argb(0xFFFFFF, 0.05f), 11);
            fillRounded(context, ax + 11, y + mid - 3, ax + 17, y + mid + 3, statusGreen, 3);
            txt(context, textRenderer, activeText, ax + 23, y + mid - 4, textDim, 9.5f);
        }

        int[] profile = profileBounds();
        if (w >= 680) {
            boolean profileHover = isHovered(mouseX, mouseY, profile);
            fillRounded(context, profile[0], profile[1], profile[0] + profile[2],
                profile[1] + profile[3], argb(accent, profileHover ? 0.32f : 0.16f), r(8));
            txt(context, textRenderer, "Profile",
                profile[0] + (profile[2] - tw(textRenderer, "Profile", 9.5f)) / 2,
                profile[1] + 7, profileHover ? accentHi : textDim, 9.5f);
        } else {
            profile[0] = 0;
            profile[1] = 0;
            profile[2] = 0;
            profile[3] = 0;
        }

        boolean closeHover = isHovered(mouseX, mouseY, close);
        uiButton(context, close[0], close[1], close[2], close[3],
            false, closeHover, close[3] / 2);
        txt(context, textRenderer, "x",
            close[0] + (close[2] - tw(textRenderer, "x", 9.0f)) / 2,
            close[1] + 11, closeHover ? accentHi : textDim);
    }

    private void drawSidebar(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        int sx = windowX;
        int sy = windowY + TITLE_HEIGHT;
        int sw = sidebarWidth();
        int sh = winH() - TITLE_HEIGHT;
        int pillW = sw - 24;
        int pillH = categoryHeight() - 4;

        if (outlines) {
            context.fill(sx + sw - 1, sy, sx + sw, sy + sh - r(WINDOW_RADIUS), border);
        }
        txt(context, textRenderer, "MODULES", sx + 18, sy + 12, textDim, 8.0f);

        int hy = (int) highlightY;
        if (performanceMode) fillRounded(context, sx + 10, hy, sx + 10 + pillW, hy + pillH,
            cardEnabled, r(10));
        else gradientOrFill(context, sx + 10, hy, pillW, pillH,
            cardEnabled, cardActive, r(10));
        fillRounded(context, sx + 10, hy + 8, sx + 13, hy + pillH - 8, accent, 1);

        int index = 0;
        for (Category category : CATEGORIES) {
            int cy = sidebarTop() + index * categoryHeight();
            boolean active = category == selectedCategory;
            boolean hov = isHovered(mouseX, mouseY, sx + 10, cy, pillW, pillH);
            float hv = performanceMode
                ? (hov && !active ? 1.0f : 0.0f)
                : anim(catAnims, category, hov && !active ? 1.0f : 0.0f, 14.0f);
            if (hv > 0.01f) {
                fillRounded(context, sx + 10, cy, sx + 10 + pillW, cy + pillH,
                    argb(0xFFFFFF, 0.045f * hv), r(10));
            }

            int iconSize = compactCategories() ? 16 : 20;
            int chipX = sx + 21;
            int chipY = cy + (pillH - iconSize) / 2;
            if (active) {
                if (performanceMode) fillRounded(context, chipX, chipY,
                    chipX + iconSize, chipY + iconSize, accent, r(6));
                else gradientOrFill(context, chipX, chipY, iconSize, iconSize,
                    accentHi, accentLo, r(6));
            } else {
                fillRounded(context, chipX, chipY, chipX + iconSize, chipY + iconSize,
                    argb(0xFFFFFF, 0.06f + 0.04f * hv), r(6));
            }
            // Vanilla item sprite, so the Crystal tab really shows an End Crystal.
            MenuItemIcons.draw(context, category,
                chipX + 1, chipY + 1, iconSize - 2);

            String categoryName = fitTracked(textRenderer, category.getName(),
                Math.max(40, sidebarWidth() - 96), 12.0f, 1);
            drawTracked(context, textRenderer, categoryName, chipX + 30, cy + (pillH - 16) / 2,
                ttlColor(active ? textColor : mixColor(textDim, textColor, hv * 0.6f)), 12.0f, 1);

            String count = categoryCountLabels.get(category);
            int bw = tw(textRenderer, count, 8.5f) + 10;
            int bx = sx + 10 + pillW - 8 - bw;
            fillRounded(context, bx, cy + (pillH - 14) / 2, bx + bw, cy + (pillH - 14) / 2 + 14,
                active ? argb(accent, 0.25f) : argb(0xFFFFFF, 0.05f), 7);
            txt(context, textRenderer, count, bx + 5, cy + (pillH - 8) / 2,
                active ? accentHi : textDim, 8.5f);
            index++;
        }

        int fy = sy + sh - 56;
        if (categoryFooterVisible()
            && fy >= sidebarTop() + CATEGORIES.length * categoryHeight() + 6) {
            uiPanel(context, sx + 12, fy, sw - 28, 40, cardBg, r(10));
            fillRounded(context, sx + 24, fy + 17, sx + 30, fy + 23, statusGreen, 3);
            ttl(context, textRenderer, "Onyx Client", sx + 38, fy + 8,
                ttlColor(textColor), 10.0f);
            txt(context, textRenderer, cachedEnabledLabel, sx + 38, fy + 23,
                textDim, 9.0f);
        }
    }

    private void drawModuleList(DrawContext context, int mouseX, int mouseY,
                                GuiTheme theme, List<Module> list) {
        // Search field, then "<Category> Modules" heading with a module count on the right.
        int[] field = searchBounds();
        boolean searchHover = isHovered(mouseX, mouseY, field);
        uiField(context, field[0], field[1], field[2], field[3],
            searchFocused, searchHover, field[3] / 2);
        String shownSearch = search.isEmpty() ? "Search modules..." : search;
        txt(context, textRenderer, shownSearch, field[0] + 30, field[1] + 12,
            search.isEmpty() ? textDim : textColor, 10.5f);
        int magX = field[0] + 14;
        int magY = field[1] + (field[3] - 13) / 2;
        fillRounded(context, magX, magY, magX + 7, magY + 7, textDim, 4);
        fillRounded(context, magX + 7, magY + 7, magX + 10, magY + 10, textDim, 1);
        if (searchFocused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int cursorX = field[0] + 30
                + tw(textRenderer, shownSearch, 10.5f) + 1;
            fillRounded(context, cursorX, field[1] + 9, cursorX + 1,
                field[1] + field[3] - 9, accent, 1);
        }

        String heading = searchActive ? "Search Results" : categoryHeadings.get(selectedCategory);
        int headY = field[1] + field[3] + 14;
        ttl(context, textRenderer, heading, field[0] + 4, headY,
            ttlColor(textColor), 14.0f);
        int onCount = 0;
        for (Module m : list) if (m.isEnabled()) onCount++;
        String count = moduleCountLabel(list.size(), onCount);
        txt(context, textRenderer, count,
            field[0] + field[2] - 4 - tw(textRenderer, count, 9.0f),
            field[1] + field[3] + 17, textDim, 9.0f);

        int x = contentLeft() + 16;
        int y = moduleListTop();
        int w = contentWidth() - 32;
        int h = moduleListHeight();
        int bottom = y + h;

        int step = MODULE_ROW_HEIGHT + ROW_GAP;
        int first = Math.max(0, (int) scroll / step - 1);
        for (int i = first; i < list.size(); i++) {
            int rowY = y + i * step - (int) scroll;
            if (rowY > bottom) break;
            if (rowY + MODULE_ROW_HEIGHT < y) continue;
            drawModuleRow(context, list.get(i), x, rowY, w, mouseX, mouseY, theme);
        }

        uiScrollbar(context, x + w + 6, y, h, maxMainScroll(list), scroll);
    }

    private void drawModuleRow(DrawContext context, Module module, int x, int y, int w,
                               int mouseX, int mouseY, GuiTheme theme) {
        int x2 = x + w;
        int rh = MODULE_ROW_HEIGHT;
        boolean hovered = isHovered(mouseX, mouseY, x, y, w, rh);
        if (hovered) tooltipModule = module;
        boolean enabled = module.isEnabled();
        boolean selected = module == settingsModule;
        // Performance mode skips the per-frame animation maps entirely.
        float hv = performanceMode
            ? (hovered ? 1.0f : 0.0f)
            : anim(hoverAnims, module, hovered ? 1.0f : 0.0f, 16.0f);
        float on = performanceMode
            ? (enabled ? 1.0f : 0.0f)
            : anim(toggleAnims, module, enabled ? 1.0f : 0.0f, 14.0f);

        // Menu Theme gives every category its own colour. Use it on the outside
        // edge so module cards are recognisable without reading their names.
        int categoryColor = theme.categoryColor(module.getCategory());
        if (outlines) {
            float rimAlpha = selected ? 0.95f : (on > 0.01f ? 0.72f : 0.40f + 0.28f * hv);
            fillRounded(context, x - 1, y - 1, x2 + 1, y + rh + 1,
                argb(categoryColor, rimAlpha), r(11));
        }
        fillRounded(context, x, y, x2, y + rh, mixColor(cardBg, cardHover, hv), r(10));
        if (on > 0.01f) {
            if (performanceMode) {
                fillRounded(context, x, y, x2, y + rh,
                    argb(categoryColor, 0.10f * on), r(10));
            } else {
                gradientOrFill(context, x, y, w, rh,
                    argb(categoryColor, 0.16f * on), argb(categoryColor, 0.04f * on), r(10));
            }
            int barH = (int) ((rh - 22) * on);
            int by = y + (rh - barH) / 2;
            fillRounded(context, x + 1, by, x + 4, by + barH, categoryColor, 1);
        }

        String name = module.getName();
        if (searchActive) name += "  [" + module.getCategory().getName() + "]";
        int textW = Math.max(40, w - 116);
        ttl(context, textRenderer, fit(textRenderer, name, textW, 12.5f),
            x + 18, y + 14, ttlColor(mixColor(0xFFB4B4B8, textColor, on)), 12.5f);
        txt(context, textRenderer,
            fit(textRenderer, module.getDescription(), textW, 9.0f),
            x + 18, y + 35, textDim, 9.0f);

        if (!module.getSettings().isEmpty()) {
            drawChevron(context, x2 - 76, y + rh / 2, selected,
                selected || hv > 0.5f ? accentHi : textDim);
        }
        uiToggle(context, x2 - 50, y + (rh - 18) / 2, 34, 18, on, hovered);
    }

    /** The catalog tally only changes when its two numbers change. */
    private String moduleCountLabel(int size, int on) {
        if (size != cachedListSize || on != cachedListOn) {
            cachedListSize = size;
            cachedListOn = on;
            cachedListLabel = size + " modules  |  " + on + " on";
        }
        return cachedListLabel;
    }

    /** Rebuilds the two setting lists only when the inspected module changes. */
    private void refreshSettingLists() {
        if (settingsModule == cachedListsFor) return;
        cachedListsFor = settingsModule;
        normalSettings.clear();
        advancedSettings.clear();
        if (settingsModule != null) {
            for (Setting setting : settingsModule.getSettings()) {
                if (setting.isAdvanced()) advancedSettings.add(setting);
                else normalSettings.add(setting);
            }
        }
    }

    private boolean hasAdvancedSettings() {
        return !advancedSettings.isEmpty();
    }

    private boolean advancedExpanded() {
        return settingsModule != null && expandedAdvanced.contains(settingsModule);
    }

    private int advancedHeaderIndex() {
        return normalSettings.size();
    }

    private int visibleSettingRows() {
        int rows = normalSettings.size();
        if (hasAdvancedSettings()) {
            rows++;
            if (advancedExpanded()) rows += advancedSettings.size();
        }
        return rows;
    }

    private boolean isAdvancedHeaderRow(int row) {
        return hasAdvancedSettings() && row == advancedHeaderIndex();
    }

    /** Maps a displayed row to a setting, or null for the Advanced header. */
    private Setting settingForRow(int row) {
        if (row < 0 || row >= visibleSettingRows()) return null;
        int header = advancedHeaderIndex();
        if (!hasAdvancedSettings()) return normalSettings.get(row);
        if (row < header) return normalSettings.get(row);
        if (!advancedExpanded()) return null;
        int index = row - header - 1;
        return index >= 0 && index < advancedSettings.size() ? advancedSettings.get(index) : null;
    }

    private void drawAdvancedHeader(DrawContext context, int x, int y, int w,
                                     int mouseX, int mouseY, GuiTheme theme) {
        int x2 = x + w;
        boolean hovered = isHovered(mouseX, mouseY, x, y, w, SETTING_ROW_HEIGHT);
        fillRounded(context, x, y, x2, y + SETTING_ROW_HEIGHT,
            hovered ? cardHover : cardBg, r(8));
        if (hovered) {
            fillRounded(context, x, y, x2, y + SETTING_ROW_HEIGHT,
                SlGuiPaint.alpha(0.08f, 0xFFFFFF), r(8));
        }
        fillRounded(context, x + 1, y + 8, x + 4, y + SETTING_ROW_HEIGHT - 8,
            SlGuiPaint.alpha(0.88f, accent), 2);
        ttl(context, textRenderer, "Advanced", x + 12, y + 17,
            ttlColor(textColor), 11.5f);
        String hint = advancedExpanded() ? "HIDE" : "HUD PLACEMENT";
        int arrowColor = hovered ? accent : textDim;
        txt(context, textRenderer, hint,
            x2 - 24 - tw(textRenderer, hint, 9.0f), y + 18,
            arrowColor, 9.0f);
        drawChevron(context, x2 - 12, y + SETTING_ROW_HEIGHT / 2 - 3,
            advancedExpanded(), arrowColor);
    }

    private void drawSettingsPanel(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        int px = settingsLeft();
        int py = windowY + TITLE_HEIGHT;
        int pw = SETTINGS_WIDTH;
        int ph = winH() - TITLE_HEIGHT;

        uiPanel(context, px + 3, py + 5, pw - 6, ph - 10, sideBg, r(CARD_RADIUS));

        gradientOrFill(context, px + 9, py + 11, pw - 18, 28,
            cardEnabled, cardActive, r(10));
        fillRounded(context, px + 9, py + 16, px + 12, py + 34, accent, 1);

        int[] settingsClose = settingsCloseBounds();
        boolean on = settingsModule.isEnabled();
        String status = on ? "ENABLED" : "DISABLED";
        int stW = tw(textRenderer, status, 8.0f) + 14;
        int stX = settingsClose[0] - 8 - stW;
        fillRounded(context, stX, py + 17, stX + stW, py + 33,
            on ? argb(accent, 0.22f) : argb(0xFFFFFF, 0.06f), 8);
        txt(context, textRenderer, status, stX + 7, py + 21,
            on ? accentHi : textDim, 8.0f);

        ttl(context, textRenderer,
            fit(textRenderer, settingsModule.getName(), pw - 106 - stW, 16.0f),
            px + 22, py + 17, ttlColor(textColor), 16.0f);

        boolean closeHover = isHovered(mouseX, mouseY,
            settingsClose[0], settingsClose[1], settingsClose[2], settingsClose[3]);
        uiButton(context, settingsClose[0], settingsClose[1], settingsClose[2], settingsClose[3],
            false, closeHover, 14);
        txt(context, textRenderer, "x",
            settingsClose[0] + (settingsClose[2] - tw(textRenderer, "x", 9.0f)) / 2,
            settingsClose[1] + 7, closeHover ? accentHi : textDim);

        refreshSettingLists();
        int rows = visibleSettingRows();
        int rowTop = py + 44;
        int rowBottom = py + ph - 8;
        int step = SETTING_ROW_HEIGHT + SETTING_GAP;
        int first = Math.max(0, (int) settingsScroll / step - 1);
        for (int i = first; i < rows; i++) {
            int rowY = rowTop + i * step - (int) settingsScroll;
            if (rowY > rowBottom) break;
            if (rowY + SETTING_ROW_HEIGHT < rowTop) continue;
            if (isAdvancedHeaderRow(i)) {
                drawAdvancedHeader(context, settingsRowX(), rowY, settingsRowW(),
                    mouseX, mouseY, theme);
            } else {
                Setting setting = settingForRow(i);
                if (setting != null) {
                    drawSettingRow(context, setting, settingsRowX(), rowY, settingsRowW(),
                        mouseX, mouseY, theme);
                }
            }
        }

        uiScrollbar(context, px + pw - 10, rowTop, rowBottom - rowTop,
            maxSettingsScroll(rows), settingsScroll);
    }

    private void drawSettingRow(DrawContext context, Setting setting, int x, int y, int w,
                                int mouseX, int mouseY, GuiTheme theme) {
        int x2 = x + w;
        boolean hovered = isHovered(mouseX, mouseY, x, y, w, SETTING_ROW_HEIGHT);
        float rowHover = anim(hoverAnims, setting, hovered ? 1.0f : 0.0f, 16.0f);
        fillRounded(context, x, y + 2, x2, y + SETTING_ROW_HEIGHT - 2,
            mixColor(border, 0xFF404045, rowHover), r(9));
        fillRounded(context, x + 1, y + 3, x2 - 1, y + SETTING_ROW_HEIGHT - 3,
            mixColor(cardBg, cardHover, rowHover), r(8));
        if (hovered && enumPopup == null) hoveredSetting = setting;

        int labelW = Math.max(40, w - 156);
        txt(context, textRenderer,
            fit(textRenderer, setting.getName(), labelW, 11.5f),
            x + 8, y + (setting instanceof NumberSetting ? 11 : 17), textDim, 11.5f);

        if (setting instanceof NumberSetting number) {
            boolean editing = editingSetting == number;
            if (editing) {
                uiField(context, x + 1, y + 1, w - 2, SETTING_ROW_HEIGHT - 2, true, true, 10);
            }

            int trackX = settingsTrackX();
            int trackW = settingsTrackW();
            int trackY = y + 36;
            int trackH = 5;
            fillRounded(context, trackX, trackY, trackX + trackW, trackY + trackH,
                toggleOff, r(2));
            double percent = clamp((number.getValue() - number.getMin())
                / (number.getMax() - number.getMin()), 0.0, 1.0);
            int fillEnd = trackX + (int) (trackW * percent);
            if (fillEnd - trackX > 1) {
                gradientOrFill(context, trackX, trackY, fillEnd - trackX, trackH,
                    accentHi, accentLo, r(2));
            }
            fillRounded(context, fillEnd - 6, trackY - 2, fillEnd + 7, trackY + trackH + 2,
                argb(accent, 0.28f), r(6));
            fillRounded(context, fillEnd - 5, trackY - 1, fillEnd + 6, trackY + trackH + 1,
                0xFFFFFFFF, r(5));

            String valueText = editing ? editingText : number.getDisplayValue();
            // Colour rows reserve space on the right for the swatch so the two
            // controls never overlap or steal each other's clicks.
            int rightPad = number.isColor() ? 40 : 8;
            int valueW = Math.max(52, tw(textRenderer, valueText, 10.0f) + 18);
            int valueX = x2 - rightPad - valueW;
            if (valueX > x + labelW + 8) {
                fillRounded(context, valueX, y + 7, x2 - rightPad, y + 27,
                    SlGuiPaint.alpha(editing ? 0.24f : 0.16f, accent), 10);
                txt(context, textRenderer, valueText, valueX + 9, y + 12,
                    editing ? accent : textColor, 10.0f);
                if (editing && (System.currentTimeMillis() / 500) % 2 == 0) {
                    int caretX = valueX + 9 + tw(textRenderer, valueText, 10.0f) + 1;
                    fillRounded(context, caretX, y + 11, caretX + 1, y + 23, accent, 1);
                }
            }
            if (number.isColor() && !editing) {
                int swatchX = x2 - 32;
                SlGuiPaint.fillRounded(context, swatchX, y + 12, swatchX + 18, y + 30,
                    0xFF000000 | number.getColorValue(), 9);
                SlGuiPaint.strokeRounded(context, swatchX, y + 12, 18, 18,
                    SlGuiPaint.alpha(0.22f, 0xFFFFFF), 9, 1);
            }
        } else if (setting instanceof BooleanSetting bool) {
            // Performance mode must skip per-setting animation maps too, not
            // just module rows, otherwise the setting panel still animates.
            float on = performanceMode
                ? (bool.isEnabled() ? 1.0f : 0.0f)
                : anim(toggleAnims, bool, bool.isEnabled() ? 1.0f : 0.0f, 14.0f);
            uiToggle(context, x2 - 42, y + 14, 34, 18, on, hovered);
        } else if (setting instanceof EnumSetting<?> enumer) {
            String value = fit(textRenderer, enumer.getDisplayValue(), 75, 10.0f);
            boolean open = enumPopup == enumer;
            int bx = x2 - 116;
            uiButton(context, bx, y + 11, 108, 24, open, hovered, 12);
            txt(context, textRenderer, value, bx + 9, y + 18,
                open ? accent : textColor, 10.0f);
            txt(context, textRenderer, open ? "^" : "v",
                bx + 92, y + 18, accent, 9.0f);
        } else if (setting instanceof KeybindSetting keybind) {
            boolean capturing = bindingSetting == keybind;
            String value = fit(textRenderer,
                capturing ? "Press a key" : keybind.getDisplayValue(), 84, 10.0f);
            int bx = x2 - 116;
            uiButton(context, bx, y + 11, 108, 24, capturing, hovered, 12);
            txt(context, textRenderer, value, bx + 9, y + 18,
                capturing ? accent : textColor, 10.0f);
        } else if (setting instanceof ChecklistSetting checklist) {
            String value = checklist.getDisplayValue() + "  >";
            int pillW = tw(textRenderer, value, 10.0f) + 20;
            int bx = x2 - 8 - pillW;
            fillRounded(context, bx, y + 11, x2 - 8, y + 35,
                SlGuiPaint.alpha(0.16f, accent), 12);
            txt(context, textRenderer, value, bx + 10, y + 18,
                accent, 10.0f);
        } else if (setting instanceof StringSetting text) {
            boolean editing = editingStringSetting == text;
            String value = editing ? editingStringText : text.getDisplayValue();
            if (editing) {
                uiField(context, x + 1, y + 1, w - 2, SETTING_ROW_HEIGHT - 2, true, true, 10);
            }
            int fieldW = Math.min(160, Math.max(60, w - labelW - 28));
            int fieldX = x2 - 8 - fieldW;
            if (!editing) {
                uiField(context, fieldX, y + 11, fieldW, 24, false, hovered, 12);
            }
            String displayed = fit(textRenderer, value, fieldW - 16, 9.5f);
            txt(context, textRenderer, displayed, fieldX + 8, y + 18,
                editing ? accent : textColor, 9.5f);
            if (editing && (System.currentTimeMillis() / 500) % 2 == 0) {
                int caretX = fieldX + 8 + tw(textRenderer, displayed, 9.5f) + 1;
                fillRounded(context, caretX, y + 15, caretX + 1, y + 31, accent, 1);
            }
        }
    }

    /** Rounded ON/OFF pill with a soft glow when enabled. */
    private void drawToggle(DrawContext context, int x, int y, int w, int h,
                            boolean enabled, GuiTheme theme) {
        if (enabled) {
            fillRounded(context, x - 2, y - 2, x + w + 2, y + h + 2,
                GuiTheme.withAlpha(accent, 0x44), h / 2 + 2);
        }
        fillRounded(context, x, y, x + w, y + h,
            enabled ? accent : border, h / 2);
        int knob = h - 4;
        int knobX = enabled ? x + w - knob - 2 : x + 2;
        fillRounded(context, knobX, y + 2, knobX + knob, y + 2 + knob,
            enabled ? 0xFFFFFFFF : textDim, knob / 2);
    }

    private int longestWidth(List<String> lines) {
        int width = 0;
        for (String line : lines) width = Math.max(width, tw(textRenderer, line, 9.0f));
        return width;
    }

    private List<String> wrapText(String text, int maxChars) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isBlank()) return lines;
        StringBuilder current = new StringBuilder();
        for (String word : text.trim().split("\\s+")) {
            if (current.length() > 0 && current.length() + word.length() + 1 > maxChars) {
                lines.add(current.toString());
                current.setLength(0);
            }
            if (current.length() > 0) current.append(' ');
            current.append(word);
        }
        if (current.length() > 0) lines.add(current.toString());
        // Character-count wrapping alone lets a single long word overflow the
        // 280px tooltip cap. Measure every line and ellipsize the stragglers.
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, fit(textRenderer, lines.get(i), 260, 9.0f));
        }
        return lines;
    }

    private void updateTooltip(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        if (tooltipModule != lastTooltipModule) {
            lastTooltipModule = tooltipModule;
            tooltipStart = System.nanoTime();
            tooltipLines = tooltipModule == null ? null : wrapText(tooltipModule.getDescription(), 46);
        }
        // Monotonic clock: a wall-clock adjustment must not make a tooltip
        // appear instantly or never appear at all.
        if (tooltipModule == null || tooltipLines == null
            || System.nanoTime() - tooltipStart < 220_000_000L) return;
        if (settingsModule != null && isHovered(mouseX, mouseY,
            settingsLeft(), windowY + TITLE_HEIGHT, SETTINGS_WIDTH, winH() - TITLE_HEIGHT)) return;

        Module shown = tooltipModule;
        List<String> lines = tooltipLines;
        int width = Math.max(150, Math.min(280,
            Math.max(tw(textRenderer, shown.getName(), 9.0f) + 40, longestWidth(lines) + 20)));
        int height = 22 + lines.size() * 12 + 8;
        int x = Math.max(4, Math.min(mouseX + 14, vw() - width - 4));
        int y = Math.max(4, Math.min(mouseY + 14, vh() - height - 4));

        uiPanel(context, x, y, width, height, sideBg, r(8));
        String status = shown.isEnabled() ? "ON" : "OFF";
        int statusW = tw(textRenderer, status, 9.0f);
        ttl(context, textRenderer,
            fit(textRenderer, shown.getName(), width - 24 - statusW, 9.0f),
            x + 8, y + 5, ttlColor(accent));
        txt(context, textRenderer, status,
            x + width - 8 - statusW, y + 5,
            shown.isEnabled() ? accent : textDim);
        int lineY = y + 22;
        for (String line : lines) {
            txt(context, textRenderer, line, x + 8, lineY, textDim);
            lineY += 12;
        }
    }

    private void updateSettingTooltip(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        if (tooltipSetting != hoveredSetting) {
            tooltipSetting = hoveredSetting;
            settingTooltipStart = System.nanoTime();
            settingTooltipLines = hoveredSetting == null ? null
                : wrapText(hoveredSetting.getDescription(), 44);
        }
        if (enumPopup != null || tooltipSetting == null || settingTooltipLines == null
            || System.nanoTime() - settingTooltipStart < 260_000_000L) return;
        int width = Math.max(160, Math.min(280, longestWidth(settingTooltipLines) + 20));
        int height = settingTooltipLines.size() * 12 + 12;
        int x = Math.max(4, Math.min(mouseX + 14, vw() - width - 4));
        int y = Math.max(4, Math.min(mouseY + 14, vh() - height - 4));
        uiPanel(context, x, y, width, height, sideBg, r(8));
        ttl(context, textRenderer, fit(textRenderer, tooltipSetting.getName(), width - 16, 9.0f),
            x + 8, y + 5, ttlColor(accent));
        int lineY = y + 19;
        for (String line : settingTooltipLines) {
            txt(context, textRenderer, line, x + 8, lineY, textDim);
            lineY += 12;
        }
    }

    private void drawEnumPopup(DrawContext context, GuiTheme theme) {
        if (enumPopup == null) return;
        Object[] values = enumPopup.values();
        int rows = enumPopupVisibleRows(values.length);
        int h = rows * ENUM_ROW_HEIGHT + 6;
        int px = Math.min(enumPopupX, vw() - enumPopupW - 4);
        int py = Math.min(enumPopupY, vh() - h - 4);
        int first = enumPopupFirst(values.length, rows);
        uiPanel(context, px, py, enumPopupW, h, sideBg, r(8));
        for (int i = first; i < Math.min(values.length, first + rows); i++) {
            int ry = py + 3 + (i - first) * ENUM_ROW_HEIGHT;
            Enum<?> value = (Enum<?>) values[i];
            boolean selected = value == enumPopup.getValue();
            if (selected) {
                fillRounded(context, px + 2, ry, px + enumPopupW - 2, ry + ENUM_ROW_HEIGHT - 1,
                    cardEnabled, r(4));
            }
            ttl(context, textRenderer, enumPopupLabel(value),
                px + 10, ry + 4, ttlColor(selected ? accent : textColor));
            if (selected) {
                txt(context, textRenderer, "*",
                    px + enumPopupW - 14, ry + 4, accent);
            }
        }
    }

    private int enumPopupVisibleRows(int count) {
        return Math.min(Math.max(1, count), Math.max(1, (vh() - 16) / ENUM_ROW_HEIGHT));
    }

    private int enumPopupFirst(int count, int rows) {
        return Math.max(0, Math.min(Math.max(0, count - rows),
            (int) (enumPopupScroll / ENUM_ROW_HEIGHT)));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void setEnumValue(EnumSetting<T> setting, Object value) {
        setting.setValue((T) value);
    }

    private static String enumPopupLabel(Enum<?> value) {
        String raw = value.toString();
        if (raw.equals(value.name())) raw = raw.toLowerCase(Locale.ROOT);
        String[] parts = raw.replace('_', ' ').trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (builder.length() > 0) builder.append(' ');
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (hoverCategories) {
            float scale = guiScale();
            double mx = mouseX / scale;
            double my = mouseY / scale + openSlideY;
            int sideY = windowY + TITLE_HEIGHT;
            if (isHovered(mx, my, windowX, sideY, sidebarWidth(), winH() - TITLE_HEIGHT)) {
                int index = (int) Math.floor((my - sidebarTop()) / (double) categoryHeight());
                Category[] values = CATEGORIES;
                if (index >= 0 && index < values.length && values[index] != selectedCategory) {
                    selectedCategory = values[index];
                    targetScroll = 0;
                    scroll = 0;
                }
            }
        }
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        float scale = guiScale();
        double mouseX = click.x() / scale;
        double mouseY = click.y() / scale + openSlideY;
        int button = click.button();
        commitEditing();
        bindingSetting = null;

        if (enumPopup != null) {
            Object[] values = enumPopup.values();
            int rows = enumPopupVisibleRows(values.length);
            int popupH = rows * ENUM_ROW_HEIGHT + 6;
            int px = Math.min(enumPopupX, vw() - enumPopupW - 4);
            int py = Math.min(enumPopupY, vh() - popupH - 4);
            if (isHovered(mouseX, mouseY, px, py, enumPopupW, popupH)) {
                int index = enumPopupFirst(values.length, rows)
                    + (int) ((mouseY - py - 3) / ENUM_ROW_HEIGHT);
                if (index >= 0 && index < values.length) setEnumValue(enumPopup, values[index]);
            }
            enumPopup = null;
            return true;
        }

        if (isHovered(mouseX, mouseY, closeBounds())) {
            close();
            return true;
        }
        if (isHovered(mouseX, mouseY, themeTagBox)) {
            // Palette tag doubles as a shortcut: click cycles themes,
            // shift-click swaps ClickGUI <-> Dropdown navigation.
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && themeModule != null) {
                if ((click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) {
                    themeModule.toggleMenuLayout();
                    if (client != null) {
                        client.setScreen(themeModule.menuLayout() == MenuLayout.DROPDOWN
                            ? new DropdownMenuScreen(modules) : new ClickGuiScreen(modules));
                    }
                } else {
                    themeModule.cycleTheme();
                }
            }
            return true;
        }
        if (profileBox[2] > 0 && isHovered(mouseX, mouseY, profileBox)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) MenuLinks.openProfile(this);
            return true;
        }
        if (winW() >= 560 && isHovered(mouseX, mouseY, searchBounds())) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) search = "";
            else searchFocused = true;
            return true;
        }
        searchFocused = false;

        if (settingsModule != null && isHovered(mouseX, mouseY, settingsCloseBounds())) {
            selectSettings(null);
            return true;
        }

        if (isHovered(mouseX, mouseY, windowX, windowY, winW(), TITLE_HEIGHT)) {
            draggingWindow = true;
            grabOffsetX = (int) (mouseX - windowX);
            grabOffsetY = (int) (mouseY - windowY);
            return true;
        }

        int[] bar = scrollbarAt(mouseX, mouseY);
        if (bar != null) {
            boolean settings = bar[4] == 1;
            float current = settings ? settingsScroll : scroll;
            int thumbY = scrollbarThumbY(bar[1], bar[2], bar[3], current);
            dragScrollSettings = settings;
            dragScrollTrackY = bar[1];
            dragScrollTrackH = bar[2];
            dragScrollMax = bar[3];
            dragScrollThumbH = scrollbarThumbHeight(bar[2], bar[3]);
            // Clicking the thumb keeps the grab offset; clicking the track jumps
            // so the thumb centres under the pointer.
            dragScrollGrab = mouseY >= thumbY && mouseY < thumbY + dragScrollThumbH
                ? mouseY - thumbY : dragScrollThumbH / 2.0;
            draggingScrollbar = true;
            updateScrollbarDrag(mouseY);
            return true;
        }

        int sideX = windowX;
        int sideY = windowY + TITLE_HEIGHT;
        if (isHovered(mouseX, mouseY, sideX, sideY, sidebarWidth(), winH() - TITLE_HEIGHT)) {
            int index = (int) Math.floor((mouseY - sidebarTop()) / (double) categoryHeight());
            Category[] values = CATEGORIES;
            if (index >= 0 && index < values.length) {
                selectedCategory = values[index];
                targetScroll = 0;
                scroll = 0;
            }
            return true;
        }

        Module module = moduleAt(mouseX, mouseY);
        if (module != null) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                selectSettings(settingsModule == module ? null : module);
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                module.toggle();
                selectSettings(module);
            }
            if (settingsModule == module) {
                LOGGER.info("Opening settings panel: {} ({} settings)",
                    module.getName(), module.getSettings().size());
            }
            settingsTargetScroll = 0;
            settingsScroll = 0;
            return true;
        }

        // The Advanced row is a header, not a Setting, so it is handled first.
        refreshSettingLists();
        if (settingsModule != null && hasAdvancedSettings()) {
            int sx = settingsRowX();
            int sy = windowY + TITLE_HEIGHT + 44;
            int sw = settingsRowW();
            int sh = winH() - TITLE_HEIGHT - 52;
            if (isHovered(mouseX, mouseY, sx, sy, sw, sh)) {
                int step = SETTING_ROW_HEIGHT + SETTING_GAP;
                int row = (int) ((mouseY - sy + (int) settingsScroll) / step);
                if (isAdvancedHeaderRow(row)) {
                    if (!expandedAdvanced.remove(settingsModule)) expandedAdvanced.add(settingsModule);
                    settingsTargetScroll = 0;
                    settingsScroll = 0;
                    return true;
                }
            }
        }

        Setting setting = settingAt(mouseX, mouseY);
        if (setting != null) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                if (setting instanceof KeybindSetting keybind) keybind.clear();
                else setting.reset();
                return true;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                if (setting instanceof NumberSetting number) {
                    boolean swatchClick = number.isColor()
                        && mouseX >= settingsRowX() + settingsRowW() - 32;
                    if (doubled || swatchClick) {
                        editingSetting = number;
                        editingText = number.getEditValue();
                        draggedSetting = null;
                    } else {
                        draggedSetting = number;
                        dragTrackX = settingsTrackX();
                        dragTrackW = settingsTrackW();
                        updateSlider(number, mouseX, dragTrackX, dragTrackW);
                    }
                } else if (setting instanceof BooleanSetting bool) {
                    bool.toggle();
                } else if (setting instanceof EnumSetting<?> enumer) {
                    if ((click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) {
                        enumer.cycleBack();
                    } else {
                        int rowTop = windowY + TITLE_HEIGHT + 44;
                        int scrollInt = (int) settingsScroll;
                        int index = (int) ((mouseY - rowTop + scrollInt) / (SETTING_ROW_HEIGHT + SETTING_GAP));
                        int rowY = rowTop + index * (SETTING_ROW_HEIGHT + SETTING_GAP) - scrollInt;
                        enumPopup = enumer;
                        enumPopupScroll = 0;
                        enumPopupX = settingsRowX();
                        enumPopupY = rowY + SETTING_ROW_HEIGHT + 2;
                        enumPopupW = settingsRowW();
                    }
                } else if (setting instanceof KeybindSetting keybind) {
                    bindingSetting = keybind;
                } else if (setting instanceof StringSetting text) {
                    editingStringSetting = text;
                    editingStringText = text.getValue();
                    draggedSetting = null;
                } else if (setting instanceof ChecklistSetting checklist) {
                    if (client != null) client.setScreen(new ChecklistScreen(this, checklist));
                    draggedSetting = null;
                }
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        float scale = guiScale();
        double mouseX = click.x() / scale;
        double mouseY = click.y() / scale + openSlideY;
        if (draggingWindow) {
            windowX = (int) (mouseX - grabOffsetX);
            windowY = (int) (mouseY - grabOffsetY);
            clampWindow();
            return true;
        }
        if (draggingScrollbar) {
            updateScrollbarDrag(mouseY);
            return true;
        }
        if (draggedSetting != null) {
            updateSlider(draggedSetting, mouseX, dragTrackX, dragTrackW);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        draggingWindow = false;
        draggingScrollbar = false;
        draggedSetting = null;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float scale = guiScale();
        // Keep the original screen-space coordinates for super(); only the
        // local menu-space copies are divided by the scale.
        double mx = mouseX / scale;
        double my = mouseY / scale + openSlideY;
        if (enumPopup != null) {
            Object[] values = enumPopup.values();
            int rows = enumPopupVisibleRows(values.length);
            int popupH = rows * ENUM_ROW_HEIGHT + 6;
            int px = Math.min(enumPopupX, vw() - enumPopupW - 4);
            int py = Math.min(enumPopupY, vh() - popupH - 4);
            if (isHovered(mx, my, px, py, enumPopupW, popupH)) {
                double maxScroll = Math.max(0, (values.length - rows) * ENUM_ROW_HEIGHT);
                enumPopupScroll = (float) clamp(enumPopupScroll - verticalAmount * ENUM_ROW_HEIGHT,
                    0.0, maxScroll);
                return true;
            }
        }
        if (settingsModule != null && isHovered(mx, my,
            settingsLeft(), windowY + TITLE_HEIGHT, SETTINGS_WIDTH, winH() - TITLE_HEIGHT)) {
            settingsTargetScroll -= verticalAmount * 16.0;
            refreshSettingLists();
            settingsTargetScroll = clamp(settingsTargetScroll, 0, maxSettingsScroll(visibleSettingRows()));
            return true;
        }
        if (isHovered(mx, my, contentLeft(), windowY + TITLE_HEIGHT,
            contentWidth(), winH() - TITLE_HEIGHT)) {
            targetScroll -= verticalAmount * 16.0;
            targetScroll = clamp(targetScroll, 0, maxMainScroll(visibleModules()));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (enumPopup != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE || input.key() == GLFW.GLFW_KEY_ENTER
                || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
                enumPopup = null;
            }
            return true;
        }
        if (editingStringSetting != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                editingStringSetting = null;
                editingStringText = null;
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
                commitEditing();
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_BACKSPACE && editingStringText != null
                && !editingStringText.isEmpty()) {
                editingStringText = dropLastCodePoint(editingStringText);
            }
            return true;
        }
        if (editingSetting != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                editingSetting = null;
                editingText = null;
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
                commitEditing();
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_BACKSPACE && editingText != null
                && !editingText.isEmpty()) {
                editingText = dropLastCodePoint(editingText);
                return true;
            }
            return true;
        }
        if (bindingSetting != null) {
            if (input.key() != GLFW.GLFW_KEY_ESCAPE) bindingSetting.set(input);
            bindingSetting = null;
            return true;
        }
        if (searchFocused) {
            if (input.key() == GLFW.GLFW_KEY_BACKSPACE) {
                if (!search.isEmpty()) search = dropLastCodePoint(search);
                return true;
            }
            if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER
                || input.key() == GLFW.GLFW_KEY_ESCAPE) {
                searchFocused = false;
                return true;
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (editingStringSetting != null && input.isValidChar()) {
            String value = input.asString();
            if (value != null && !value.isEmpty()
                && value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && editingStringText.length() + value.length() <= 128) {
                editingStringText += value;
            }
            return true;
        }
        if (editingSetting != null && input.isValidChar()) {
            String value = input.asString();
            String pattern = editingSetting.isColor()
                ? "[0-9a-fA-FxX#+\\-]" : "[0-9.\\-+]";
            if (value.matches(pattern) && editingText.length() + value.length() <= 16) editingText += value;
            return true;
        }
        if (searchFocused && input.isValidChar()) {
            String value = input.asString();
            if (value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && search.length() + value.length() <= 96) {
                search += value;
            }
            return true;
        }
        return super.charTyped(input);
    }

    private void commitEditing() {
        if (editingStringSetting != null) {
            if (editingStringText != null) editingStringSetting.setValue(editingStringText);
            editingStringSetting = null;
            editingStringText = null;
        }
        if (editingSetting == null || editingText == null) return;
        String text = editingText.trim();
        try {
            if (editingSetting.isColor() && isPlainHex(text)) {
                editingSetting.setValue(Long.parseLong(text, 16) & 0xFFFFFFL);
            } else if (editingSetting.isColor()
                && (text.startsWith("#") || text.toLowerCase().startsWith("0x"))) {
                String hex = text.startsWith("#") ? text.substring(1) : text.substring(2);
                editingSetting.setValue(Long.parseLong(hex, 16) & 0xFFFFFFL);
            } else {
                editingSetting.setValue(Double.parseDouble(text));
            }
        } catch (NumberFormatException ignored) {
            // Keep the old value when the user typed something invalid.
        }
        editingSetting = null;
        editingText = null;
    }

    /** Bare RRGGBB is a valid colour entry, not a decimal number. */
    private static boolean isPlainHex(String text) {
        if (text.length() != 6) return false;
        for (int i = 0; i < text.length(); i++) {
            if (Character.digit(text.charAt(i), 16) < 0) return false;
        }
        return true;
    }

    /** Backspaces one Unicode code point, never half of a surrogate pair. */
    private static String dropLastCodePoint(String value) {
        if (value == null || value.isEmpty()) return value;
        int cut = value.offsetByCodePoints(value.length(), -1);
        return value.substring(0, cut);
    }

    /** Switching panels must drop any open dropdown bound to the old panel. */
    private void selectSettings(Module module) {
        if (settingsModule == module) return;
        enumPopup = null;
        enumPopupScroll = 0.0f;
        tooltipSetting = null;
        hoveredSetting = null;
        editingSetting = null;
        editingText = null;
        bindingSetting = null;
        settingsModule = module;
    }

    @Override
    public void close() {
        if (closing) return;
        enumPopup = null;
        editingSetting = null;
        bindingSetting = null;
        draggingScrollbar = false;
        draggingWindow = false;
        ConfigStore.save(modules);
        // No world (server switch / shutdown) - close immediately, no frame to animate.
        if (client == null || client.world == null) {
            super.close();
            return;
        }
        closing = true;
        closeStart = System.nanoTime();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private GuiTheme theme() {
        return themeModule != null ? themeModule.getTheme() : GuiTheme.MATRIX;
    }

    private GuiThemeModule themeModule() {
        return GuiThemeModule.INSTANCE;
    }

    private int panelColor(GuiTheme theme) {
        return themeModule != null ? themeModule.panelColor() : theme.panel;
    }

    private boolean shadowsEnabled() {
        return themeModule == null || themeModule.shadow();
    }

    private boolean outlinesEnabled() {
        return themeModule == null || themeModule.outlines();
    }

    private List<Module> visibleModules() {
        String query = search.trim().toLowerCase(Locale.ROOT);
        if (cachedVisible != null && query.equals(cachedSearch) && selectedCategory == cachedCategory) {
            return cachedVisible;
        }
        cachedSearch = query;
        cachedCategory = selectedCategory;
        List<Module> result;
        if (query.isEmpty()) {
            result = modulesByCategory.getOrDefault(selectedCategory, java.util.Collections.emptyList());
        } else {
            result = new ArrayList<>();
            for (Module module : modules) {
                if (module.getName().toLowerCase(Locale.ROOT).contains(query)
                    || module.getDescription().toLowerCase(Locale.ROOT).contains(query)
                    || module.getCategory().getName().toLowerCase(Locale.ROOT).contains(query)) {
                    result.add(module);
                }
            }
        }
        cachedVisible = result;
        return result;
    }

    private Module moduleAt(double mouseX, double mouseY) {
        int x = contentLeft() + 16;
        int y = moduleListTop();
        int w = contentWidth() - 32;
        int h = moduleListHeight();
        if (!isHovered(mouseX, mouseY, x, y, w, h)) return null;
        int step = MODULE_ROW_HEIGHT + ROW_GAP;
        // Rendering truncates scroll to an int for row positions; hit-testing
        // must use the same value or a click lands on a neighbouring row while
        // the list is still easing.
        int scrollInt = (int) scroll;
        int index = (int) ((mouseY - y + scrollInt) / step);
        List<Module> list = visibleModules();
        if (index < 0 || index >= list.size()) return null;
        int rowY = y + index * step - scrollInt;
        return mouseY < rowY + MODULE_ROW_HEIGHT ? list.get(index) : null;
    }

    private Setting settingAt(double mouseX, double mouseY) {
        if (settingsModule == null) return null;
        int x = settingsRowX();
        int y = windowY + TITLE_HEIGHT + 44;
        int w = settingsRowW();
        int h = winH() - TITLE_HEIGHT - 52;
        if (!isHovered(mouseX, mouseY, x, y, w, h)) return null;
        refreshSettingLists();
        int step = SETTING_ROW_HEIGHT + SETTING_GAP;
        int scrollInt = (int) settingsScroll;
        int index = (int) ((mouseY - y + scrollInt) / step);
        if (index < 0 || index >= visibleSettingRows()) return null;
        int rowY = y + index * step - scrollInt;
        return mouseY < rowY + SETTING_ROW_HEIGHT ? settingForRow(index) : null;
    }

    private int maxMainScroll(List<Module> list) {
        int content = list.size() * (MODULE_ROW_HEIGHT + ROW_GAP);
        int view = moduleListHeight();
        return Math.max(0, content - view);
    }

    private int maxSettingsScroll(int count) {
        int content = count * (SETTING_ROW_HEIGHT + SETTING_GAP);
        int view = winH() - TITLE_HEIGHT - 52;
        return Math.max(0, content - view);
    }

    private void clampScrolls(List<Module> visible) {
        int maxMain = maxMainScroll(visible);
        targetScroll = clamp(targetScroll, 0, maxMain);
        scroll = clamp(scroll, 0, maxMain);
        refreshSettingLists();
        int maxSettings = settingsModule == null ? 0 : maxSettingsScroll(visibleSettingRows());
        settingsTargetScroll = clamp(settingsTargetScroll, 0, maxSettings);
        settingsScroll = clamp(settingsScroll, 0, maxSettings);
    }

    private void clampWindow() {
        windowX = (int) clamp(windowX, 4, Math.max(4, vw() - winW() - 4));
        windowY = (int) clamp(windowY, 4, Math.max(4, vh() - winH() - 4));
    }

    private void updateSlider(NumberSetting setting, double mouseX, int trackX, int trackWidth) {
        if (trackWidth <= 0) return;
        double percent = clamp((mouseX - trackX) / (double) trackWidth, 0.0, 1.0);
        double value = setting.getMin() + percent * (setting.getMax() - setting.getMin());
        boolean wholeRange = setting.getMin() == Math.floor(setting.getMin())
            && setting.getMax() == Math.floor(setting.getMax())
            && setting.getMax() - setting.getMin() <= 20.0;
        if (wholeRange) value = Math.round(value);
        setting.setValue(value);
    }

    private int winW() {
        int available = Math.max(4, vw() - 28);
        int minimum = Math.min(sidebarWidth() + SETTINGS_WIDTH + 8, available);
        int preferred = Math.max(4, (int) (available * menuScale()));
        return Math.min(1400, Math.max(minimum, preferred));
    }

    private int winH() {
        int available = Math.max(4, vh() - 28);
        int preferred = Math.max(240, (int) (available * menuScale()));
        return Math.min(1000, Math.min(available, Math.max(240, preferred)));
    }

    private int catalogTop() {
        return windowY + TITLE_HEIGHT + 5;
    }

    private int catalogHeight() {
        return winH() - TITLE_HEIGHT - 10;
    }

    /** Catalog header/title/search block before the module rows. */
    private int moduleListTop() {
        return catalogTop() + 86;
    }

    private int moduleListHeight() {
        return Math.max(0, catalogHeight() - 86 - 12);
    }

    /**
     * Cached 1:1 layout scale. Menu Scale is applied separately by menuScale();
     * only text tiers scale the vanilla glyph atlas to their target sizes.
     */
    private float guiScale() {
        float factor = client == null || client.getWindow() == null
            ? -1.0f : client.getWindow().getScaleFactor();
        if (cachedGuiScale <= 0.0f || factor != cachedScaleFactor
            || width != cachedScaleWidth || height != cachedScaleHeight) {
            cachedGuiScale = SlGuiPaint.nativeScale();
            cachedScaleWidth = width;
            cachedScaleHeight = height;
            cachedScaleFactor = factor;
        }
        return cachedGuiScale;
    }

    /** Menu Scale resizes the shell rather than resampling the whole GUI. */
    private float menuScale() {
        if (themeModule == null) return 1.0f;
        return Math.max(GuiThemeModule.MIN_GUI_SCALE,
            Math.min(GuiThemeModule.MAX_GUI_SCALE, themeModule.guiScale()));
    }

    private int vw() {
        return Math.max(1, (int) (width / guiScale()));
    }

    private int vh() {
        return Math.max(1, (int) (height / guiScale()));
    }

    /** Category rail width from Menu Theme, with a safe fallback before init. */
    private int sidebarWidth() {
        return themeModule == null
            ? RAIL_WIDTH_DEFAULT : (int) Math.round(themeModule.sidebarWidth());
    }

    /** Vertical space in the rail before the optional footer card. */
    private int categoryAvailable(boolean withFooter) {
        int reserved = TITLE_HEIGHT + 30 + 4;
        if (withFooter) reserved += 56 + 6;
        return Math.max(0, winH() - reserved);
    }

    /** The footer card only fits when the rows still get a comfortable pitch. */
    private boolean categoryFooterVisible() {
        return categoryAvailable(true) / Math.max(1, CATEGORIES.length) >= 32;
    }

    /**
     * Category row pitch from Menu Theme. On short windows the configured pitch
     * is clamped to the space that is actually available. The footer card is
     * dropped first, then rows compact down to an 18px floor so every tab stays
     * inside the rail instead of being clipped off the bottom.
     */
    private int categoryHeight() {
        int configured = themeModule == null
            ? ROW_HEIGHT_DEFAULT : (int) Math.round(themeModule.categorySpacing());
        int pitch = categoryAvailable(categoryFooterVisible()) / Math.max(1, CATEGORIES.length);
        return Math.max(18, Math.min(configured, pitch));
    }

    /** Rows tighter than this switch to a smaller icon so it never overlaps. */
    private boolean compactCategories() {
        return categoryHeight() < 26;
    }

    private int sidebarTop() {
        return windowY + TITLE_HEIGHT + 30;
    }

    private int contentLeft() {
        return windowX + sidebarWidth();
    }

    private int contentWidth() {
        int width = winW() - sidebarWidth() - (settingsModule != null ? SETTINGS_WIDTH : 0);
        // Very small windows can leave no room for the catalog. Never feed a
        // negative width into layout or fill code.
        return Math.max(0, width);
    }

    /** The 460px inspector does not fit beside the rail on narrow windows. */
    private boolean settingsPanelFits() {
        return winW() >= sidebarWidth() + SETTINGS_WIDTH + 24;
    }

    private int settingsLeft() {
        return windowX + winW() - SETTINGS_WIDTH;
    }

    private int settingsRowX() {
        return settingsLeft() + IPAD;
    }

    private int settingsRowW() {
        return SETTINGS_WIDTH - IPAD * 2;
    }

    private int settingsTrackX() {
        return settingsRowX() + 6;
    }

    private int settingsTrackW() {
        return settingsRowW() - 12;
    }

    private int[] searchBounds() {
        // Source catalog search: 16px side padding, 36px top, 36px height.
        searchBox[0] = contentLeft() + 16;
        searchBox[1] = catalogTop() + 10;
        searchBox[2] = Math.max(0, contentWidth() - 32);
        searchBox[3] = 34;
        return searchBox;
    }

    /** Profile chip in the title bar, to the left of the active-module chip. */
    private int[] profileBounds() {
        int[] close = closeBounds();
        int w = 82;
        String active = cachedActiveLabel == null ? "" : cachedActiveLabel;
        int activeW = tw(textRenderer, active, 9.5f) + 30;
        profileBox[0] = close[0] - 14 - activeW - 10 - w;
        profileBox[1] = windowY + (TITLE_HEIGHT - 24) / 2;
        profileBox[2] = w;
        profileBox[3] = 24;
        return profileBox;
    }

    private int[] closeBounds() {
        closeBox[0] = windowX + winW() - 14 - 30;
        closeBox[1] = windowY + (TITLE_HEIGHT - 30) / 2;
        closeBox[2] = 30;
        closeBox[3] = 30;
        return closeBox;
    }

    private int[] settingsCloseBounds() {
        settingsCloseBox[0] = settingsLeft() + SETTINGS_WIDTH - 35;
        settingsCloseBox[1] = windowY + TITLE_HEIGHT + 14;
        settingsCloseBox[2] = 22;
        settingsCloseBox[3] = 22;
        return settingsCloseBox;
    }

    /** Rebuilds every menu colour from the selected Menu Theme palette. */
    private void applyTheme(GuiTheme theme) {
        lightTheme = theme.light;
        int panel = themeModule != null ? themeModule.panelColor() : theme.panel;

        accent = theme.accent;
        accentHi = theme.titleAccent;
        accentLo = mixColor(theme.accent, theme.panelBorder, 0.45f);
        winTop = theme.titleBar;
        winBg = mixColor(theme.titleBar, 0xFF000000, 0.55f);
        sideBg = panel;
        cardBg = theme.moduleRow;
        cardHover = theme.moduleRowHover;
        cardActive = mixColor(theme.moduleRow, theme.accent, 0.14f);
        cardEnabled = theme.moduleRowEnabled;
        fieldBg = theme.searchBg;
        border = theme.panelBorder;
        textColor = theme.text;
        textDim = theme.textSecondary;
        toggleOff = theme.toggleOff;
        // Status dots are semantic ("ready"), not theme accents. Some palettes
        // use magenta or grey for toggleOn, which made a ready module look wrong.
        statusGreen = 0xFF50DC78;
        titleColor = lightTheme
            ? mixColor(theme.text, 0xFF101014, 0.18f)
            : SlGuiPaint.titleColor(theme.text);
    }

    /** Maps a semantic label size to the nearest pre-rasterised TTF tier. */
    private int fontTier(float size) {
        return MenuFonts.tier(Math.max(0.1f, size) * Math.max(0.85f, fontScale));
    }

    private void txt(DrawContext c, TextRenderer r, String value, int x, int y, int color) {
        txt(c, r, value, x, y, color, 11.0f);
    }

    private void txt(DrawContext c, TextRenderer r, String value, int x, int y,
                     int color, float size) {
        MenuFonts.draw(c, r, value, x, y, color, fontTier(size));
    }

    private void ttl(DrawContext c, TextRenderer r, String value, int x, int y, int color) {
        ttl(c, r, value, x, y, color, 11.0f);
    }

    private void ttl(DrawContext c, TextRenderer r, String value, int x, int y,
                     int color, float size) {
        MenuFonts.draw(c, r, value, x, y, color, Math.max(1, fontTier(size)));
    }

    private int tw(TextRenderer r, String value, float size) {
        if (value == null) return 0;
        return MenuFonts.width(r, value, fontTier(size));
    }

    /**
     * Draws one of Flux's raw PNG icons.
     *
     * <p>{@code drawGuiTexture} resolves through Minecraft's GUI sprite atlas,
     * so a loose texture file shows up as the purple missing-texture tile.
     * {@code drawTexture} reads straight from {@link net.minecraft.client.texture.TextureManager},
     * which is what these standalone icons need. The source texture is 64x64;
     * the destination is the 18-22px chip drawn in the menu.
     */
    private void drawMenuIcon(DrawContext c, Identifier id, int x, int y,
                              int size, float alpha) {
        int color = argb(0xFFFFFF, alpha);
        c.drawTexture(RenderPipelines.GUI_TEXTURED, id, x, y,
            0.0f, 0.0f, size, size, 64, 64, 64, 64, color);
    }

    private String fit(TextRenderer r, String value, int maxWidth, float size) {
        if (value == null || maxWidth <= 0) return "";
        int tier = fontTier(size);
        FitKey key = new FitKey(tier, maxWidth, value);
        String cached = fitCache.get(key);
        if (cached != null) return cached;

        String result;
        if (MenuFonts.width(r, value, tier) <= maxWidth) {
            result = value;
        } else {
            String ellipsis = "...";
            int limit = maxWidth - MenuFonts.width(r, ellipsis, tier);
            if (limit <= 0) {
                result = ellipsis;
            } else {
                int low = 0;
                int high = value.length();
                while (low < high) {
                    int mid = (low + high + 1) >>> 1;
                    if (MenuFonts.width(r, value.substring(0, mid), tier) <= limit) low = mid;
                    else high = mid - 1;
                }
                result = value.substring(0, low).stripTrailing() + ellipsis;
            }
        }
        if (fitCache.size() >= 1024) fitCache.clear();
        fitCache.put(key, result);
        return result;
    }

    /** Draws one glyph at a time so sidebar labels get a little letter spacing. */
    private void drawTracked(DrawContext c, TextRenderer r, String value, int x, int y,
                             int color, float size, int gap) {
        MenuFonts.drawTracked(c, r, value, x, y, color,
            Math.max(1, fontTier(size)), gap);
    }

    private int trackedWidth(TextRenderer r, String value, float size, int gap) {
        return MenuFonts.widthTracked(r, value, Math.max(1, fontTier(size)), gap);
    }

    private String fitTracked(TextRenderer r, String value, int maxWidth, float size, int gap) {
        if (value == null || maxWidth <= 0) return "";
        TrackedFitKey key = new TrackedFitKey(gap, fontTier(size), maxWidth, value);
        String cached = fitTrackedCache.get(key);
        if (cached != null) return cached;

        String result;
        if (trackedWidth(r, value, size, gap) <= maxWidth) {
            result = value;
        } else {
            int low = 0;
            int high = value.length();
            while (low < high) {
                int mid = (low + high + 1) >>> 1;
                if (trackedWidth(r, value.substring(0, mid), size, gap) <= maxWidth) low = mid;
                else high = mid - 1;
            }
            result = value.substring(0, low).stripTrailing();
        }
        if (fitTrackedCache.size() >= 1024) fitTrackedCache.clear();
        fitTrackedCache.put(key, result);
        return result;
    }

    private int ttlColor(int color) {
        return lightTheme ? mixColor(color, 0xFF101014, 0.16f) : SlGuiPaint.titleColor(color);
    }

    private void gradientOrFill(DrawContext c, int x, int y, int w, int h,
                                int from, int to, int radius) {
        if (gradientHeader) SlGuiPaint.fillRoundedGradient(c, x, y, w, h, from, to, radius);
        else fillRounded(c, x, y, x + w, y + h, from, radius);
    }

    private static int argb(int color, float alpha) {
        int a = Math.max(0, Math.min(255, (int) (alpha * 255.0f)));
        return (a << 24) | (color & 0x00FFFFFF);
    }

    private void uiPanel(DrawContext c, int x, int y, int w, int h, int fill, int radius) {
        if (outlines) fillRounded(c, x - 1, y - 1, x + w + 1, y + h + 1, border, radius + 1);
        fillRounded(c, x, y, x + w, y + h, fill, radius);
    }

    private void uiButton(DrawContext c, int x, int y, int w, int h,
                          boolean active, boolean hover, int radius) {
        if (active) fillRounded(c, x - 1, y - 1, x + w + 1, y + h + 1, argb(accent, 0.6f), radius + 1);
        fillRounded(c, x, y, x + w, y + h,
            active ? cardEnabled : hover ? cardHover : fieldBg, radius);
    }

    private void uiField(DrawContext c, int x, int y, int w, int h,
                         boolean focused, boolean hover, int radius) {
        if (outlines || focused) {
            fillRounded(c, x - 1, y - 1, x + w + 1, y + h + 1,
                focused ? argb(accent, 0.8f) : border, radius + 1);
        }
        fillRounded(c, x, y, x + w, y + h, hover && !focused ? cardHover : fieldBg, radius);
    }

    private void uiToggle(DrawContext c, int x, int y, int w, int h, boolean enabled, boolean hover) {
        uiToggle(c, x, y, w, h, enabled ? 1.0f : 0.0f, hover);
    }

    /** on is an animation value in 0..1 (0 = off, 1 = on). */
    private void uiToggle(DrawContext c, int x, int y, int w, int h, float on, boolean hover) {
        if (on > 0.01f) {
            fillRounded(c, x - 3, y - 3, x + w + 3, y + h + 3, argb(accent, 0.16f * on), h / 2 + 3);
        }
        fillRounded(c, x, y, x + w, y + h, mixColor(toggleOff, accent, on), h / 2);
        int knob = h - 6;
        int knobX = x + 3 + Math.round((w - knob - 6) * on);
        fillRounded(c, knobX, y + 3, knobX + knob, y + 3 + knob,
            mixColor(0xFFB0B0B5, 0xFFFFFFFF, on), knob / 2);
    }

    private static void drawChevron(DrawContext c, int cx, int cy, boolean up, int color) {
        int steps = 6;
        for (int i = 0; i < steps; i++) {
            int dy = up ? steps - 1 - i : i;
            int half = steps - 1 - i;
            c.fill(cx - half, cy + dy, cx - half + 2, cy + dy + 2, color);
            c.fill(cx + half - 1, cy + dy, cx + half + 1, cy + dy + 2, color);
        }
    }

    /** Frame-rate independent smoothing of a stored 0..1 value toward target. */
    private float anim(Map<Object, Float> store, Object key, float target, float speed) {
        float current = store.getOrDefault(key, target);
        current += (target - current) * (1.0f - (float) Math.exp(-frameDt * speed));
        if (Math.abs(target - current) < 0.003f) current = target;
        store.put(key, current);
        return current;
    }

    private static int scrollbarThumbHeight(int h, int maxScroll) {
        return Math.min(h, Math.max(16, h * h / (h + maxScroll)));
    }

    private static int scrollbarThumbY(int y, int h, int maxScroll, float scrollValue) {
        int thumb = scrollbarThumbHeight(h, maxScroll);
        return y + (int) ((h - thumb) * Math.max(0.0f,
            Math.min(1.0f, scrollValue / (float) maxScroll)));
    }

    private void uiScrollbar(DrawContext c, int x, int y, int h, int maxScroll, float scrollValue) {
        if (maxScroll <= 0 || h <= 0) return;
        int thumb = scrollbarThumbHeight(h, maxScroll);
        int thumbY = scrollbarThumbY(y, h, maxScroll, scrollValue);
        fillRounded(c, x, thumbY, x + 3, thumbY + thumb, argb(accent, 0.45f), 1);
    }

    /**
     * Returns the scrollbar geometry under the pointer as
     * {@code {trackX, trackY, trackH, maxScroll, settingsFlag}}, or null.
     * The hit area is widened to a 12px gutter around the 3px thumb.
     */
    private int[] scrollbarAt(double mx, double my) {
        int mainX = contentLeft() + 16 + (contentWidth() - 32) + 6;
        int mainY = moduleListTop();
        int mainH = moduleListHeight();
        int mainMax = maxMainScroll(visibleModules());
        if (mainMax > 0 && mainH > 0
            && mx >= mainX - 4 && mx < mainX + 8
            && my >= mainY && my < mainY + mainH) {
            return new int[]{mainX, mainY, mainH, mainMax, 0};
        }
        if (settingsModule != null) {
            int py = windowY + TITLE_HEIGHT;
            int rowTop = py + 44;
            int rowBottom = py + (winH() - TITLE_HEIGHT) - 8;
            int sx = settingsLeft() + SETTINGS_WIDTH - 10;
            int setMax = maxSettingsScroll(visibleSettingRows());
            if (setMax > 0 && rowBottom > rowTop
                && mx >= sx - 4 && mx < sx + 8
                && my >= rowTop && my < rowBottom) {
                return new int[]{sx, rowTop, rowBottom - rowTop, setMax, 1};
            }
        }
        return null;
    }

    /** Applies a scrollbar drag from the current pointer position. */
    private void updateScrollbarDrag(double my) {
        if (!draggingScrollbar) return;
        float usable = dragScrollTrackH - dragScrollThumbH;
        float value = usable <= 0 ? 0.0f
            : (float) ((my - dragScrollTrackY - dragScrollGrab) / usable) * dragScrollMax;
        value = clamp(value, 0, dragScrollMax);
        if (dragScrollSettings) {
            settingsTargetScroll = value;
            settingsScroll = value;
        } else {
            targetScroll = value;
            scroll = value;
        }
    }

    /**
     * Same antialiased rounded fill as the shared helper. Delegating keeps one
     * cached arc table for both shells instead of a duplicate corner cache.
     */
    private static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int radius) {
        // The old local helper passed degenerate rectangles straight to fill().
        // Keep that call so translucent one-pixel accents render identically.
        if (x2 <= x1 || y2 <= y1) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        SlGuiPaint.fillRounded(context, x1, y1, x2, y2, color, radius);
    }

    private static boolean isHovered(double mx, double my, int[] bounds) {
        return isHovered(mx, my, bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    private static boolean isHovered(double mx, double my, int x, int y, int w, int h) {
        // Half-open intervals match Minecraft's fill() (x2/y2 are exclusive)
        // and stop adjacent rows/buttons from both claiming the shared pixel.
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static int mixColor(int from, int to, float t) {
        float f = Math.max(0.0f, Math.min(1.0f, t));
        int a = (int) (((from >>> 24) & 0xFF) * (1 - f) + ((to >>> 24) & 0xFF) * f);
        int r = (int) (((from >> 16) & 0xFF) * (1 - f) + ((to >> 16) & 0xFF) * f);
        int g = (int) (((from >> 8) & 0xFF) * (1 - f) + ((to >> 8) & 0xFF) * f);
        int b = (int) ((from & 0xFF) * (1 - f) + (to & 0xFF) * f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int r(int base) {
        return roundedEdges ? base : 0;
    }

    private static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(float value, double min, double max) {
        return (float) Math.max(min, Math.min(max, value));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
