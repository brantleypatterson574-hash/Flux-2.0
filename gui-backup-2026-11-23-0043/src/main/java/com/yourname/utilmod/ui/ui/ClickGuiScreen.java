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

    /** Original 64x64 menu icons. One per category, in Category enum order. */
    private static final Identifier[] CATEGORY_ICONS = {
        Identifier.of("flux", "textures/gui/categories/crystal.png"),
        Identifier.of("flux", "textures/gui/categories/sword.png"),
        Identifier.of("flux", "textures/gui/categories/mace.png"),
        Identifier.of("flux", "textures/gui/categories/movement.png"),
        Identifier.of("flux", "textures/gui/categories/player.png"),
        Identifier.of("flux", "textures/gui/categories/render.png"),
        Identifier.of("flux", "textures/gui/categories/world.png"),
        Identifier.of("flux", "textures/gui/categories/donut.png"),
        Identifier.of("flux", "textures/gui/categories/util.png"),
        Identifier.of("flux", "textures/gui/categories/hud.png")
    };
    private static final Identifier FLUX_LOGO =
        Identifier.of("flux", "textures/gui/categories/flux_logo.png");

    /** Enum.values() allocates a new array on every call; cache it for the menu loop. */
    private static final Category[] CATEGORIES = Category.values();

    private final Map<String, String> fitCache = new HashMap<>();
    private float cachedGuiScale = -1.0f;
    private int cachedScaleWidth = -1;
    private int cachedScaleHeight = -1;

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
    private final Set<String> expandedAdvanced = new HashSet<>();
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
    private final int[] creditsBox = new int[4];
    private final int[] closeBox = new int[4];
    private final int[] settingsCloseBox = new int[4];

    private Module hoveredModule;
    private float hoverAlpha;
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

    private GuiThemeModule themeModule;
    private int[] headerGradient;
    private int cachedGradientWidth = -1;
    private GuiTheme cachedGradientTheme;

    public ClickGuiScreen(List<Module> modules) {
        super(Text.literal("Flux 2.0 Menu"));
        this.modules = modules;
        for (Module module : modules) {
            categoryCounts.merge(module.getCategory(), 1, Integer::sum);
            modulesByCategory.computeIfAbsent(module.getCategory(), key -> new ArrayList<>()).add(module);
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
            openStart = System.currentTimeMillis();
        }
        clampWindow();
        headerGradient = null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (closing) {
            closeProgress = Math.min(1.0f,
                (System.currentTimeMillis() - closeStart) / 170.0f);
            if (closeProgress >= 1.0f) {
                super.close();
                return;
            }
        }

        float scale = guiScale();
        // Smoothstep gives a soft start and stop without an extra easing texture.
        openProgress = Math.min(1.0f, (System.currentTimeMillis() - openStart) / 220.0f);
        float openEase = openProgress * openProgress * (3.0f - 2.0f * openProgress);
        openSlideY = (1.0f - openEase) * 12.0f;
        int mx = (int) (mouseX / scale);
        int my = (int) (mouseY / scale + openSlideY);

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
        hoveredModule = tooltipModule;
        hoverAlpha += ((hoveredModule == null ? 0.0f : 1.0f) - hoverAlpha) * 0.2f;

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
        ttl(context, textRenderer, "Flux", x + 50, y + mid - 7,
            ttlColor(textColor), 15.0f);
        boolean showTheme = themeModule == null || themeModule.showThemeName();
        String tag = fit(textRenderer, showTheme ? theme.getLabel() : "2.0", 96, 9.5f);
        int tagW = tw(textRenderer, tag, 9.5f) + 14;
        int tagX = x + 50 + tw(textRenderer, "Flux", 15.0f) + 8;
        fillRounded(context, tagX, y + mid - 8, tagX + tagW, y + mid + 7, argb(accent, 0.18f), r(7));
        txt(context, textRenderer, tag, tagX + 7, y + mid - 4, ttlColor(accentHi), 9.5f);

        if (outlines) {
            context.fill(x + 1, y + TITLE_HEIGHT - 1, x + w - 1, y + TITLE_HEIGHT, border);
        }

        int[] credits = creditsBounds();
        if (w >= 520) {
            String activeText = enabledCount + " active";
            int aw = tw(textRenderer, activeText, 9.5f) + 30;
            int ax = credits[0] - 14 - aw;
            fillRounded(context, ax, y + mid - 11, ax + aw, y + mid + 11, argb(0xFFFFFF, 0.05f), 11);
            fillRounded(context, ax + 11, y + mid - 3, ax + 17, y + mid + 3, statusGreen, 3);
            txt(context, textRenderer, activeText, ax + 23, y + mid - 4, textDim, 9.5f);
        }

        boolean creditsHover = isHovered(mouseX, mouseY, credits);
        uiButton(context, credits[0], credits[1], credits[2], credits[3],
            false, creditsHover, credits[3] / 2);
        txt(context, textRenderer, "i",
            credits[0] + (credits[2] - tw(textRenderer, "i", 9.0f)) / 2,
            credits[1] + 11, creditsHover ? accentHi : textDim);

        int[] close = closeBounds();
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

            int chipX = sx + 21;
            int chipY = cy + (pillH - 20) / 2;
            if (active) {
                if (performanceMode) fillRounded(context, chipX, chipY, chipX + 20, chipY + 20,
                    accent, r(6));
                else gradientOrFill(context, chipX, chipY, 20, 20, accentHi, accentLo, r(6));
            } else {
                fillRounded(context, chipX, chipY, chipX + 20, chipY + 20,
                    argb(0xFFFFFF, 0.06f + 0.04f * hv), r(6));
            }
            // Original 64x64 icon, drawn smoothly at 18x18. The old single
            // letter chips are gone; every category now has a real picture.
            drawMenuIcon(context, CATEGORY_ICONS[category.ordinal()],
                chipX + 1, chipY + 1, 18,
                active || hv > 0.01f ? 1.0f : 0.82f);

            String categoryName = fitTracked(textRenderer, category.getName(),
                Math.max(40, sidebarWidth() - 96), 12.0f, 1);
            drawTracked(context, textRenderer, categoryName, chipX + 30, cy + (pillH - 16) / 2,
                ttlColor(active ? textColor : mixColor(textDim, textColor, hv * 0.6f)), 12.0f, 1);

            String count = String.valueOf(categoryCounts.getOrDefault(category, 0));
            int bw = tw(textRenderer, count, 8.5f) + 10;
            int bx = sx + 10 + pillW - 8 - bw;
            fillRounded(context, bx, cy + (pillH - 14) / 2, bx + bw, cy + (pillH - 14) / 2 + 14,
                active ? argb(accent, 0.25f) : argb(0xFFFFFF, 0.05f), 7);
            txt(context, textRenderer, count, bx + 5, cy + (pillH - 8) / 2,
                active ? accentHi : textDim, 8.5f);
            index++;
        }

        int fy = sy + sh - 56;
        if (fy >= sidebarTop() + CATEGORIES.length * categoryHeight() + 6) {
            uiPanel(context, sx + 12, fy, sw - 28, 40, cardBg, r(10));
            fillRounded(context, sx + 24, fy + 17, sx + 30, fy + 23, statusGreen, 3);
            ttl(context, textRenderer, "Flux Client", sx + 38, fy + 8,
                ttlColor(textColor), 10.0f);
            txt(context, textRenderer, enabledCount + " enabled", sx + 38, fy + 23,
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

        String heading = search.trim().isEmpty()
            ? selectedCategory.getName() + " Modules" : "Search Results";
        int headY = field[1] + field[3] + 14;
        ttl(context, textRenderer, heading, field[0] + 4, headY,
            ttlColor(textColor), 14.0f);
        int onCount = 0;
        for (Module m : list) if (m.isEnabled()) onCount++;
        String count = list.size() + " modules  |  " + onCount + " on";
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
        return settingsModule != null && expandedAdvanced.contains(settingsModule.getName());
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
            int valueW = Math.max(52, tw(textRenderer, valueText, 10.0f) + 18);
            int valueX = x2 - 8 - valueW;
            if (valueX > x + labelW + 8) {
                fillRounded(context, valueX, y + 7, x2 - 8, y + 27,
                    SlGuiPaint.alpha(editing ? 0.24f : 0.16f, accent), 10);
                txt(context, textRenderer, valueText, valueX + 9, y + 12,
                    editing ? accent : textColor, 10.0f);
                if (editing && (System.currentTimeMillis() / 500) % 2 == 0) {
                    int caretX = valueX + 9 + tw(textRenderer, valueText, 10.0f) + 1;
                    fillRounded(context, caretX, y + 11, caretX + 1, y + 23, accent, 1);
                }
            }
            if (number.isColor() && !editing) {
                int swatchX = x2 - 27;
                SlGuiPaint.fillRounded(context, swatchX, y + 12, swatchX + 18, y + 30,
                    0xFF000000 | number.getColorValue(), 9);
                SlGuiPaint.strokeRounded(context, swatchX, y + 12, 18, 18,
                    SlGuiPaint.alpha(0.22f, 0xFFFFFF), 9, 1);
            }
        } else if (setting instanceof BooleanSetting bool) {
            uiToggle(context, x2 - 42, y + 14, 34, 18,
                anim(toggleAnims, bool, bool.isEnabled() ? 1.0f : 0.0f, 14.0f), hovered);
        } else if (setting instanceof EnumSetting<?> enumer) {
            String value = fit(textRenderer, enumer.getDisplayValue(), 86, 10.0f);
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
        return lines;
    }

    private String trimText(String value, int maxWidth) {
        if (value == null || maxWidth <= 0) return "";
        if (tw(textRenderer, value, 9.0f) <= maxWidth) return value;
        String suffix = "...";
        int limit = maxWidth - tw(textRenderer, suffix, 9.0f);
        if (limit <= 0) return suffix;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            out.append(value.charAt(i));
            if (tw(textRenderer, out.toString(), 9.0f) > limit) {
                out.deleteCharAt(out.length() - 1);
                break;
            }
        }
        return out + suffix;
    }

    private void updateTooltip(DrawContext context, int mouseX, int mouseY, GuiTheme theme) {
        if (tooltipModule != lastTooltipModule) {
            lastTooltipModule = tooltipModule;
            tooltipStart = System.currentTimeMillis();
            tooltipLines = tooltipModule == null ? null : wrapText(tooltipModule.getDescription(), 46);
        }
        if (tooltipModule == null || tooltipLines == null
            || System.currentTimeMillis() - tooltipStart < 220) return;
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
        ttl(context, textRenderer, shown.getName(), x + 8, y + 5,
            ttlColor(accent));
        txt(context, textRenderer, status,
            x + width - 8 - tw(textRenderer, status, 9.0f), y + 5,
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
            settingTooltipStart = System.currentTimeMillis();
            settingTooltipLines = hoveredSetting == null ? null
                : wrapText(hoveredSetting.getDescription(), 44);
        }
        if (enumPopup != null || tooltipSetting == null || settingTooltipLines == null
            || System.currentTimeMillis() - settingTooltipStart < 260) return;
        int width = Math.max(160, Math.min(280, longestWidth(settingTooltipLines) + 20));
        int height = settingTooltipLines.size() * 12 + 12;
        int x = Math.max(4, Math.min(mouseX + 14, vw() - width - 4));
        int y = Math.max(4, Math.min(mouseY + 14, vh() - height - 4));
        uiPanel(context, x, y, width, height, sideBg, r(8));
        ttl(context, textRenderer, tooltipSetting.getName(), x + 8, y + 5,
            ttlColor(accent));
        int lineY = y + 19;
        for (String line : settingTooltipLines) {
            txt(context, textRenderer, line, x + 8, lineY, textDim);
            lineY += 12;
        }
    }

    private void drawEnumPopup(DrawContext context, GuiTheme theme) {
        if (enumPopup == null) return;
        Object[] values = enumPopup.getValues();
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
            Object[] values = enumPopup.getValues();
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
        if (isHovered(mouseX, mouseY, creditsBounds())) {
            // Credits screen was intentionally removed with the old GUI shell.
            return true;
        }
        if (winW() >= 560 && isHovered(mouseX, mouseY, searchBounds())) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) search = "";
            else searchFocused = true;
            return true;
        }
        searchFocused = false;

        if (settingsModule != null && isHovered(mouseX, mouseY, settingsCloseBounds())) {
            settingsModule = null;
            return true;
        }

        if (isHovered(mouseX, mouseY, windowX, windowY, winW(), TITLE_HEIGHT)) {
            draggingWindow = true;
            grabOffsetX = (int) (mouseX - windowX);
            grabOffsetY = (int) (mouseY - windowY);
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
                settingsModule = settingsModule == module ? null : module;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                module.toggle();
                settingsModule = module;
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
                int row = (int) ((mouseY - sy + settingsScroll) / step);
                if (isAdvancedHeaderRow(row)) {
                    String key = settingsModule.getName();
                    if (!expandedAdvanced.remove(key)) expandedAdvanced.add(key);
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
                        && mouseX >= settingsRowX() + settingsRowW() - 22;
                    if (doubled || swatchClick) {
                        editingSetting = number;
                        editingText = number.getDisplayValue();
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
                        int index = (int) ((mouseY - rowTop + settingsScroll) / (SETTING_ROW_HEIGHT + SETTING_GAP));
                        int rowY = rowTop + index * (SETTING_ROW_HEIGHT + SETTING_GAP) - (int) settingsScroll;
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
        if (draggedSetting != null) {
            updateSlider(draggedSetting, mouseX, dragTrackX, dragTrackW);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        draggingWindow = false;
        draggedSetting = null;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float scale = guiScale();
        mouseX /= scale;
        mouseY = mouseY / scale + openSlideY;
        if (enumPopup != null) {
            Object[] values = enumPopup.getValues();
            int rows = enumPopupVisibleRows(values.length);
            int popupH = rows * ENUM_ROW_HEIGHT + 6;
            int px = Math.min(enumPopupX, vw() - enumPopupW - 4);
            int py = Math.min(enumPopupY, vh() - popupH - 4);
            if (isHovered(mouseX, mouseY, px, py, enumPopupW, popupH)) {
                double maxScroll = Math.max(0, (values.length - rows) * ENUM_ROW_HEIGHT);
                enumPopupScroll = (float) clamp(enumPopupScroll - verticalAmount * ENUM_ROW_HEIGHT,
                    0.0, maxScroll);
                return true;
            }
        }
        if (settingsModule != null && isHovered(mouseX, mouseY,
            settingsLeft(), windowY + TITLE_HEIGHT, SETTINGS_WIDTH, winH() - TITLE_HEIGHT)) {
            settingsTargetScroll -= verticalAmount * 16.0;
            refreshSettingLists();
            settingsTargetScroll = clamp(settingsTargetScroll, 0, maxSettingsScroll(visibleSettingRows()));
            return true;
        }
        if (isHovered(mouseX, mouseY, contentLeft(), windowY + TITLE_HEIGHT,
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
                editingStringText = editingStringText.substring(0, editingStringText.length() - 1);
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
                editingText = editingText.substring(0, editingText.length() - 1);
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
                if (!search.isEmpty()) search = search.substring(0, search.length() - 1);
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
                && editingStringText.length() < 128) {
                editingStringText += value;
            }
            return true;
        }
        if (editingSetting != null && input.isValidChar()) {
            String value = input.asString();
            String pattern = editingSetting.isColor()
                ? "[0-9a-fA-FxX#+\\-]" : "[0-9.\\-+]";
            if (value.matches(pattern) && editingText.length() < 16) editingText += value;
            return true;
        }
        if (searchFocused && input.isValidChar()) {
            String value = input.asString();
            if (value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && search.length() < 96) {
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
            if (editingSetting.isColor() && (text.startsWith("#") || text.toLowerCase().startsWith("0x"))) {
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

    @Override
    public void close() {
        if (closing) return;
        ConfigStore.save(modules);
        // No world (server switch / shutdown) - close immediately, no frame to animate.
        if (client == null || client.world == null) {
            super.close();
            return;
        }
        closing = true;
        closeStart = System.currentTimeMillis();
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
        int index = (int) ((mouseY - y + scroll) / step);
        List<Module> list = visibleModules();
        if (index < 0 || index >= list.size()) return null;
        int rowY = y + index * step - (int) scroll;
        return mouseY <= rowY + MODULE_ROW_HEIGHT ? list.get(index) : null;
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
        int index = (int) ((mouseY - y + settingsScroll) / step);
        if (index < 0 || index >= visibleSettingRows()) return null;
        int rowY = y + index * step - (int) settingsScroll;
        return mouseY <= rowY + SETTING_ROW_HEIGHT ? settingForRow(index) : null;
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
     * Cached 1:1 scale. One menu unit now maps to exactly one physical pixel,
     * so no glyph atlas or icon texture is ever resampled by the matrix stack.
     */
    private float guiScale() {
        if (cachedGuiScale <= 0.0f
            || width != cachedScaleWidth || height != cachedScaleHeight) {
            cachedGuiScale = SlGuiPaint.nativeScale();
            cachedScaleWidth = width;
            cachedScaleHeight = height;
        }
        return cachedGuiScale;
    }

    /** Menu Scale resizes the shell rather than resampling the whole GUI. */
    private float menuScale() {
        if (themeModule == null) return 1.0f;
        return Math.max(0.5f, Math.min(1.0f, themeModule.guiScale()));
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

    /**
     * Category row pitch from Menu Theme. On short / low-resolution windows the
     * configured pitch is clamped so every tab stays visible above the footer
     * card instead of being clipped off the bottom of the rail.
     */
    private int categoryHeight() {
        int configured = themeModule == null
            ? ROW_HEIGHT_DEFAULT : (int) Math.round(themeModule.categorySpacing());
        int available = winH() - TITLE_HEIGHT - 30 - 56 - 10;
        int maxPitch = available / Math.max(1, CATEGORIES.length);
        return Math.max(32, Math.min(configured, maxPitch));
    }

    private int sidebarTop() {
        return windowY + TITLE_HEIGHT + 30;
    }

    private int contentLeft() {
        return windowX + sidebarWidth();
    }

    private int contentWidth() {
        return winW() - sidebarWidth() - (settingsModule != null ? SETTINGS_WIDTH : 0);
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

    private int[] creditsBounds() {
        creditsBox[0] = windowX + winW() - 14 - 30 - 8 - 30;
        creditsBox[1] = windowY + (TITLE_HEIGHT - 30) / 2;
        creditsBox[2] = 30;
        creditsBox[3] = 30;
        return creditsBox;
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
        statusGreen = theme.toggleOn;
        titleColor = lightTheme
            ? mixColor(theme.text, 0xFF101014, 0.18f)
            : SlGuiPaint.titleColor(theme.text);
    }

    /** Maps a semantic label size to the nearest pre-rasterised TTF tier. */
    private int fontTier(float size) {
        return MenuFonts.tier(Math.max(0.1f, size) * Math.max(0.85f, fontScale));
    }

    private Text menuText(String value, int tier) {
        return MenuFonts.text(value, tier);
    }

    private void txt(DrawContext c, TextRenderer r, String value, int x, int y, int color) {
        txt(c, r, value, x, y, color, 11.0f);
    }

    private void txt(DrawContext c, TextRenderer r, String value, int x, int y,
                     int color, float size) {
        if (value == null) return;
        c.drawText(r, menuText(value, fontTier(size)), x, y, color, false);
    }

    private void ttl(DrawContext c, TextRenderer r, String value, int x, int y, int color) {
        ttl(c, r, value, x, y, color, 11.0f);
    }

    private void ttl(DrawContext c, TextRenderer r, String value, int x, int y,
                     int color, float size) {
        if (value == null) return;
        c.drawText(r, menuText(value, Math.max(1, fontTier(size))), x, y, color, false);
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
        String key = tier + "\u0000" + maxWidth + "\u0000" + value;
        String cached = fitCache.get(key);
        if (cached != null) return cached;

        String result;
        Text full = menuText(value, tier);
        if (r.getWidth(full) <= maxWidth) {
            result = value;
        } else {
            String ellipsis = "...";
            int limit = maxWidth - r.getWidth(menuText(ellipsis, tier));
            if (limit <= 0) {
                result = ellipsis;
            } else {
                int low = 0;
                int high = value.length();
                while (low < high) {
                    int mid = (low + high + 1) >>> 1;
                    if (r.getWidth(menuText(value.substring(0, mid), tier)) <= limit) low = mid;
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
        String key = "t" + gap + "\u0000" + fontTier(size) + "\u0000" + maxWidth + "\u0000" + value;
        String cached = fitCache.get(key);
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
        if (fitCache.size() >= 1024) fitCache.clear();
        fitCache.put(key, result);
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

    private void uiScrollbar(DrawContext c, int x, int y, int h, int maxScroll, float scrollValue) {
        if (maxScroll <= 0 || h <= 0) return;
        int thumb = Math.max(16, h * h / (h + maxScroll));
        int thumbY = y + (int) ((h - thumb) * (scrollValue / (float) maxScroll));
        fillRounded(c, x, thumbY, x + 3, thumbY + thumb, argb(accent, 0.45f), 1);
    }

    private static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int radius) {
        int w = x2 - x1;
        int h = y2 - y1;
        int r = Math.min(radius, Math.min(w, h) / 2);
        if (r <= 0) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        // Centre band and side bands never overlap, so translucent colours stay even.
        context.fill(x1 + r, y1, x2 - r, y2, color);
        context.fill(x1, y1 + r, x1 + r, y2 - r, color);
        context.fill(x2 - r, y1 + r, x2, y2 - r, color);

        int alpha = color >>> 24;
        int rgb = color & 0x00FFFFFF;
        boolean smooth = alpha >= 40; // skip edge smoothing on faint shadow layers
        Corner corner = corner(r);
        for (int dy = 0; dy < r; dy++) {
            int solid = corner.insets[dy];
            if (solid < r) {
                context.fill(x1 + solid, y1 + dy, x1 + r, y1 + dy + 1, color);
                context.fill(x2 - r, y1 + dy, x2 - solid, y1 + dy + 1, color);
                context.fill(x1 + solid, y2 - dy - 1, x1 + r, y2 - dy, color);
                context.fill(x2 - r, y2 - dy - 1, x2 - solid, y2 - dy, color);
            }
            if (smooth && solid > 0) {
                float cover = corner.covers[dy];
                if (cover > 0.04f) {
                    int edgeColor = (Math.round(alpha * cover) << 24) | rgb;
                    context.fill(x1 + solid - 1, y1 + dy, x1 + solid, y1 + dy + 1, edgeColor);
                    context.fill(x2 - solid, y1 + dy, x2 - solid + 1, y1 + dy + 1, edgeColor);
                    context.fill(x1 + solid - 1, y2 - dy - 1, x1 + solid, y2 - dy, edgeColor);
                    context.fill(x2 - solid, y2 - dy - 1, x2 - solid + 1, y2 - dy, edgeColor);
                }
            }
        }
    }

    /** Cached quarter-circle masks; removes per-frame sqrt from every rounded widget. */
    private static final Map<Integer, Corner> CORNERS = new HashMap<>();

    private static Corner corner(int radius) {
        Corner cached = CORNERS.get(radius);
        if (cached != null) return cached;
        int[] insets = new int[radius];
        float[] covers = new float[radius];
        for (int dy = 0; dy < radius; dy++) {
            double dyFromCenter = radius - dy - 0.5;
            double edge = radius
                - Math.sqrt(Math.max(0.0, (double) radius * radius - dyFromCenter * dyFromCenter));
            insets[dy] = (int) Math.ceil(edge);
            covers[dy] = (float) (insets[dy] - edge);
        }
        Corner corner = new Corner(insets, covers);
        if (CORNERS.size() >= 64) CORNERS.clear();
        CORNERS.put(radius, corner);
        return corner;
    }

    private static final class Corner {
        final int[] insets;
        final float[] covers;

        Corner(int[] insets, float[] covers) {
            this.insets = insets;
            this.covers = covers;
        }
    }

    private static boolean isHovered(double mx, double my, int[] bounds) {
        return isHovered(mx, my, bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    private static boolean isHovered(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
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
