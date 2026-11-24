package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.module.impl.GuiThemeModule;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.ui.MenuLayout;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.setting.StringSetting;
import com.yourname.utilmod.util.ConfigStore;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Dropdown layout: one panel per category, side by side. Left click toggles a
 * module, right click folds its settings open inside the panel, and a panel
 * header can be dragged. Uses the same palette, radii and motion as the
 * ClickGUI layout, and every label is centred on its control.
 */
public class DropdownMenuScreen extends Screen {
    private static final int PANEL_W = 190;
    private static final int PANEL_GAP = 14;
    private static final int HEADER_H = 42;
    private static final int ROW_H = 30;
    private static final int ROW_GAP = 6;
    private static final int PAD = 8;
    private static final int LINE_H = 16;
    private static final int PANEL_RADIUS = 14;
    private static final int ROW_RADIUS = 10;
    private static final int MAX_INLINE_OPTIONS = 12;

    /** The wordmark stays a Flux asset; category chips use vanilla item sprites. */
    private static final Identifier FLUX_LOGO =
        Identifier.of("flux", "textures/gui/categories/flux_logo.png");

    // The dropped copy carried a private seven-colour palette. It is removed so
    // this screen uses the same GuiTheme/GuiThemeModule selection as the ClickGUI.
    private static final int TEXT = 0xFFF4F4F5;
    private static final int TEXT_DIM = 0xFF8E8E93;
    private static final int TOGGLE_OFF = 0xFF303030;
    private static final int STATUS_GREEN = 0xFF3DDC84;
    private static final int STATUS_RED = 0xFFFF5555;

    /** All chrome colours come from the live GuiTheme, never a second palette. */
    private GuiTheme theme() {
        GuiThemeModule module = themeModule();
        return module == null ? GuiTheme.PINK : module.getTheme();
    }

    private int accent() { return theme().accent; }
    private int hi() { return theme().titleAccent; }

    private int cachedLoAccent = Integer.MIN_VALUE;
    private int cachedLoColor;

    private int lo() {
        int accent = theme().accent;
        if (accent != cachedLoAccent) {
            cachedLoAccent = accent;
            cachedLoColor = SlGuiPaint.mix(accent, 0xFF000000, 0.22f);
        }
        return cachedLoColor;
    }
    private int top() { return theme().titleBar; }
    private int bg() { return 0xFF000000 | (theme().panel & 0x00FFFFFF); }
    private int card() { return theme().moduleRow; }
    private int hoverColor() { return theme().moduleRowHover; }
    private int inner() { return theme().settingRow; }
    private int border() { return theme().panelBorder; }

    private boolean perf() {
        GuiThemeModule module = themeModule();
        return module == null || module.performanceMode();
    }

    private boolean rounded() {
        GuiThemeModule module = themeModule();
        return module == null || module.rounded();
    }

    private int r(int value) { return rounded() ? value : 0; }

    private enum Kind { HEADER, MODULE, BOOL, SLIDER, ENUM_OPTION, ENUM_CYCLE, KEYBIND, CHECKLIST, STRING, ADVANCED, THEME, SEARCH, LAYOUT }

    /** Clickable region recorded while drawing, so hit testing always matches what is on screen. */
    private record Hit(int x, int y, int w, int h, Kind kind, Category category,
                       Module module, Setting setting, Object value, int trackX, int trackW) {}

    private final List<Module> modules;
    private final Category[] categories = Category.values();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);
    private final Set<Module> expanded = new HashSet<>();
    private final Set<Module> advancedOpen = new HashSet<>();
    private final Map<Category, int[]> panelOffset = new EnumMap<>(Category.class);
    private final Map<Category, Float> scroll = new EnumMap<>(Category.class);
    private final Map<Category, Float> targetScroll = new EnumMap<>(Category.class);
    private final Map<Category, Integer> contentHeight = new EnumMap<>(Category.class);
    private final Map<Category, Integer> bodyHeight = new EnumMap<>(Category.class);
    private final Map<Category, int[]> panelRects = new EnumMap<>(Category.class);
    private final List<Hit> hits = new ArrayList<>();
    private final Map<Object, Float> hoverAnims = new IdentityHashMap<>();
    private final Map<Object, Float> toggleAnims = new IdentityHashMap<>();

    private long openStartNanos;
    private float openSlideY;
    private long lastFrameNanos;
    private float frameDt = 0.016f;
    private final Map<Category, int[]> scrollbarRects = new EnumMap<>(Category.class);
    private Category draggingScroll;
    private int dragScrollTop;
    private int dragScrollLength;
    private float dragScrollMax;
    private String search = "";
    private String searchText = "";
    private String cachedQuerySource;
    private String cachedQuery;
    private boolean editingSearch;

    private Category draggingPanel;
    private int grabX;
    private int grabY;
    private NumberSetting draggedSlider;
    private int dragTrackX;
    private int dragTrackW;
    private KeybindSetting bindingSetting;
    private StringSetting editingString;
    private String editingText;

    public DropdownMenuScreen(List<Module> modules) {
        super(Text.literal("Flux 2.0 Dropdown"));
        this.modules = modules;
        for (Category category : categories) byCategory.put(category, new ArrayList<>());
        for (Module module : modules) byCategory.get(module.getCategory()).add(module);
    }

    @Override
    protected void init() {
        super.init();
        if (openStartNanos == 0) openStartNanos = System.nanoTime();
    }

    // ------------------------------------------------------------------ layout

    private GuiThemeModule themeModule() {
        return GuiThemeModule.INSTANCE;
    }

    /**
     * Dropdown uses the native crisp scale as its base, then applies Menu Scale
     * on top. Lower values enlarge the virtual viewport while panel sizes stay
     * fixed, so the whole dropdown visibly shrinks instead of staying identical.
     */
    private float guiScale() {
        GuiThemeModule module = themeModule();
        float userScale = module == null ? 1.0f
            : Math.max(0.5f, Math.min(1.0f, module.guiScale()));
        return Math.max(0.01f, SlGuiPaint.nativeScale() * userScale);
    }

    private int vw() {
        return Math.max(1, (int) (width / guiScale()));
    }

    private int vh() {
        return Math.max(1, (int) (height / guiScale()));
    }

    /** Ten panels do not fit at 1280 physical pixels with the old 14px gap. */
    private int panelGap() {
        return vw() < 1500 ? 6 : PANEL_GAP;
    }

    private int panelWidth() {
        int n = categories.length;
        int available = vw() - 16 - (n - 1) * panelGap();
        return Math.max(96, Math.min(PANEL_W, available / n));
    }

    private int panelHeight() {
        return Math.max(220, Math.min(vh() - 104, 560));
    }

    private int baseX(int index) {
        int pw = panelWidth();
        int gap = panelGap();
        int total = categories.length * pw + (categories.length - 1) * gap;
        return (vw() - total) / 2 + index * (pw + gap);
    }

    private int baseY() {
        return 48 + Math.max(0, (vh() - 48 - panelHeight()) / 2);
    }

    /** Search filters the module rows without changing which panels exist. */
    private List<Module> visibleModules(Category category) {
        // Each panel asks for the same query; normalise it only once per change.
        String query = cachedQuery;
        if (!search.equals(cachedQuerySource)) {
            cachedQuerySource = search;
            query = search.trim().toLowerCase(Locale.ROOT);
            cachedQuery = query;
        }
        List<Module> source = byCategory.get(category);
        if (query.isEmpty()) return source;
        List<Module> result = new ArrayList<>();
        for (Module module : source) {
            if (module.getName().toLowerCase(Locale.ROOT).contains(query)
                || module.getDescription().toLowerCase(Locale.ROOT).contains(query)) {
                result.add(module);
            }
        }
        return result;
    }

    // ------------------------------------------------------------------ render

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float scale = guiScale();
        float progress = Math.min(1.0f, (System.nanoTime() - openStartNanos) / 220_000_000.0f);
        float ease = progress * progress * (3.0f - 2.0f * progress);
        openSlideY = (1.0f - ease) * 12.0f;
        int mx = (int) (mouseX / scale);
        int my = (int) (mouseY / scale + openSlideY);

        long now = System.nanoTime();
        frameDt = lastFrameNanos == 0 ? 0.016f : Math.min(0.1f, (now - lastFrameNanos) / 1.0e9f);
        lastFrameNanos = now;

        GuiThemeModule themeModule = themeModule();
        GuiTheme theme = themeModule != null ? themeModule.getTheme() : GuiTheme.PINK;
        int overlay = themeModule != null ? themeModule.overlayColor() : theme.overlay;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);
        SlGuiPaint.overlayWashBytes(context, vw(), vh(), overlay, 38, 112);
        int overlayAlpha = (overlay >>> 24) & 0xFF;
        float strength = overlayAlpha == 0 ? 1.0f : overlayAlpha / 255.0f;
        context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.25f * strength, 0x000000));
        context.getMatrices().translate(0.0f, -openSlideY);

        hits.clear();
        scrollbarRects.clear();
        float smooth = perf() ? 1.0f : 1.0f - (float) Math.exp(-frameDt * 14.0f);
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            float current = scroll.getOrDefault(category, 0.0f);
            float target = targetScroll.getOrDefault(category, 0.0f);
            current += (target - current) * smooth;
            if (Math.abs(target - current) < 0.4f) current = target;
            scroll.put(category, current);
            int[] off = panelOffset.get(category);
            int px = baseX(i) + (off == null ? 0 : off[0]);
            int py = baseY() + (off == null ? 0 : off[1]);
            drawPanel(context, category, px, py, panelWidth(), panelHeight(), mx, my);
        }

        drawSearch(context, mx, my);
        drawThemes(context, mx, my);

        float fade = (1.0f - ease) * 0.55f;
        if (fade > 0.001f) {
            int slide = Math.round(openSlideY);
            context.fill(0, slide, vw(), vh() + slide, SlGuiPaint.alpha(fade, 0x000000));
        }
        context.getMatrices().popMatrix();
    }

    /** Compact search field; filtering applies to the module rows in every panel. */
    private void drawSearch(DrawContext context, int mx, int my) {
        int w = Math.min(460, vw() - 32);
        int h = 30;
        int x = (vw() - w) / 2;
        int y = 9;
        GuiTheme theme = theme();
        boolean hovered = hover(mx, my, x, y, w, h);
        SlGuiPaint.field(context, x, y, w, h, theme, accent(), editingSearch, hovered, 14);
        String shown = editingSearch ? searchText
            : (search.isEmpty() ? "Search modules..." : search);
        String fitted = SlGuiPaint.fit(textRenderer, shown, w - 24, 11.0f);
        int color = editingSearch || !search.isEmpty() ? theme.text : theme.textSecondary;
        SlGuiPaint.textMid(context, textRenderer, fitted, x + 12, y + h / 2, color, 11.0f);
        if (editingSearch && (System.nanoTime() / 500_000_000L) % 2 == 0) {
            int caretX = x + 12 + SlGuiPaint.textWidth(textRenderer, fitted, 11.0f) + 1;
            context.fill(caretX, y + 7, caretX + 1, y + h - 7, accent());
        }
        hits.add(new Hit(x, y, w, h, Kind.SEARCH, null, null, null, null, 0, 0));
    }

    /** The swatch row is driven by GuiThemeModule, so both menus share one theme choice. */
    private void drawThemes(DrawContext context, int mx, int my) {
        GuiTheme[] themes = GuiThemeModule.themeCycle();
        int w = 176;
        int head = 38;
        int rowH = 22;
        int h = head + themes.length * rowH + 8;
        int x = vw() - w - 16;
        int y = vh() - h - 16;
        fillRounded(context, x - 1, y - 1, x + w + 1, y + h + 1, argb(accent(), 0.4f), r(13));
        if (perf()) {
            fillRounded(context, x, y, x + w, y + h, bg(), r(12));
        } else {
            SlGuiPaint.fillRoundedGradient(context, x, y, w, h, top(), bg(), r(12));
        }
        SlGuiPaint.titleMid(context, textRenderer, "Themes  /  Layout", x + 12, y + head / 2,
            SlGuiPaint.titleColor(theme().text), 12.0f);
        fillRounded(context, x + 2, y + head / 2 - 2, x + 6, y + head / 2 + 2, accent(), 2);
        context.fill(x + 1, y + head - 1, x + w - 1, y + head, border());
        for (int i = 0; i < themes.length; i++) {
            GuiTheme t = themes[i];
            int ry = y + head + 4 + i * rowH;
            boolean selected = t == theme();
            boolean hovered = hover(mx, my, x + 6, ry, w - 12, rowH);
            if (selected || hovered) {
                fillRounded(context, x + 6, ry, x + w - 6, ry + rowH,
                    selected ? argb(t.accent, 0.16f) : argb(0xFFFFFF, 0.04f), r(8));
            }
            int mid = ry + rowH / 2;
            fillRounded(context, x + 14, mid - 6, x + 28, mid + 6, t.accent, r(4));
            int low = SlGuiPaint.mix(t.accent, 0xFF000000, 0.22f);
            fillRounded(context, x + 31, mid - 6, x + 45, mid + 6, low, r(4));
            SlGuiPaint.textMid(context, textRenderer, t.getLabel(), x + 56, mid,
                selected ? theme().text : theme().textSecondary, 10.5f);
            hits.add(new Hit(x + 6, ry, w - 12, rowH, Kind.THEME, null, null, null, i, 0, 0));
        }
        // Same shortcuts as the ClickGUI title tag: click cycles theme, shift-click swaps layout.
        hits.add(new Hit(x, y, w, head, Kind.LAYOUT, null, null, null, null, 0, 0));
    }

    private void drawPanel(DrawContext context, Category category, int x, int y, int w, int h,
                           int mx, int my) {
        panelRects.put(category, new int[] {x, y, w, h});

        if (!perf()) {
            for (int i = 8; i >= 1; i--) {
                fillRounded(context, x - i, y - i + 5, x + w + i, y + h + i + 5,
                    argb(0x000000, 0.03f), r(PANEL_RADIUS + i));
            }
        }
        fillRounded(context, x - 1, y - 1, x + w + 1, y + h + 1, argb(accent(), 0.45f), r(PANEL_RADIUS + 1));
        if (perf()) {
            fillRounded(context, x, y, x + w, y + h, bg(), r(PANEL_RADIUS));
        } else {
            SlGuiPaint.fillRoundedGradient(context, x, y, w, h, top(), bg(), r(PANEL_RADIUS));
        }
        if (!perf()) context.fill(x + r(PANEL_RADIUS), y + 1, x + w - r(PANEL_RADIUS), y + 2, argb(0xFFFFFF, 0.06f));

        // Header: a real Minecraft item sprite, centred with the category name.
        int chip = 20;
        int mid = y + HEADER_H / 2;
        String name = category.getName();
        int nameW = SlGuiPaint.textWidth(textRenderer, name, 13.0f);
        int groupW = chip + 9 + nameW;
        int gx = x + (w - groupW) / 2;
        MenuItemIcons.draw(context, category, gx, mid - chip / 2, chip);
        SlGuiPaint.titleMid(context, textRenderer, name, gx + chip + 9, mid,
            SlGuiPaint.titleColor(theme().text), 13.0f);
        context.fill(x + 1, y + HEADER_H - 1, x + w - 1, y + HEADER_H, border());
        hits.add(new Hit(x, y, w, HEADER_H, Kind.HEADER, category, null, null, null, 0, 0));

        int bx = x + PAD;
        int bw = w - PAD * 2;
        int by = y + HEADER_H + PAD;
        int bh = h - HEADER_H - PAD * 2;
        bodyHeight.put(category, bh);

        float scrollValue = scroll.getOrDefault(category, 0.0f);
        int origin = by - (int) scrollValue;
        int cursor = origin;
        for (Module module : visibleModules(category)) {
            cursor = drawModule(context, category, module, bx, cursor, bw, by, bh, mx, my);
        }
        int total = cursor - origin;
        contentHeight.put(category, total);
        float maxScroll = Math.max(0, total - bh);
        if (targetScroll.getOrDefault(category, 0.0f) > maxScroll) targetScroll.put(category, maxScroll);

        if (maxScroll > 0) {
            int thumb = Math.max(18, bh * bh / Math.max(1, total));
            int thumbY = by + (int) ((bh - thumb) * (scrollValue / maxScroll));
            fillRounded(context, x + w - 5, thumbY, x + w - 2, thumbY + thumb, argb(accent(), 0.45f), 1);
            // trackX, trackY, trackW, trackH, thumbH, maxScroll
            scrollbarRects.put(category, new int[] {x + w - 6, by, 6, bh, thumb, (int) Math.ceil(maxScroll)});
        }
    }

    private static boolean fits(int y, int h, int top, int height) {
        return y >= top && y + h <= top + height;
    }

    private int drawModule(DrawContext context, Category category, Module module, int x, int y, int w,
                           int top, int bodyH, int mx, int my) {
        boolean enabled = module.isEnabled();
        boolean open = expanded.contains(module);

        if (fits(y, ROW_H, top, bodyH)) {
            boolean hovered = hover(mx, my, x, y, w, ROW_H);
            float hv = perf() ? (hovered ? 1.0f : 0.0f)
                : anim(hoverAnims, module, hovered ? 1.0f : 0.0f, 16.0f);
            float on = perf() ? (enabled ? 1.0f : 0.0f)
                : anim(toggleAnims, module, enabled ? 1.0f : 0.0f, 14.0f);
            int rim = mixColor(border(), lo(), Math.min(1.0f, hv * 0.6f + on * 0.4f));
            fillRounded(context, x - 1, y - 1, x + w + 1, y + ROW_H + 1, rim, r(ROW_RADIUS + 1));
            fillRounded(context, x, y, x + w, y + ROW_H, mixColor(card(), hoverColor(), hv), r(ROW_RADIUS));
            if (on > 0.01f) {
                if (perf()) {
                    fillRounded(context, x, y, x + w, y + ROW_H, argb(accent(), 0.10f * on), r(ROW_RADIUS));
                } else {
                    SlGuiPaint.fillRoundedGradient(context, x, y, w, ROW_H,
                        argb(accent(), 0.14f * on), argb(accent(), 0.03f * on), r(ROW_RADIUS));
                }
            }
            int mid = y + ROW_H / 2;
            String label = SlGuiPaint.fit(textRenderer, module.getName(), w - 36, 11.5f);
            int labelW = SlGuiPaint.textWidth(textRenderer, label, 11.5f);
            SlGuiPaint.titleMid(context, textRenderer, label, x + (w - labelW) / 2, mid,
                SlGuiPaint.titleColor(mixColor(0xFFB4B4B8, hi(), on)), 11.5f);
            // Dot: green while the settings are folded open, red while folded shut.
            fillRounded(context, x + w - 15, mid - 2, x + w - 11, mid + 2, open ? STATUS_GREEN : STATUS_RED, 2);
            hits.add(new Hit(x, y, w, ROW_H, Kind.MODULE, category, module, null, null, 0, 0));
        }
        y += ROW_H + ROW_GAP;
        if (!open) return y;

        List<Setting> normal = new ArrayList<>();
        List<Setting> advanced = new ArrayList<>();
        for (Setting setting : module.getSettings()) {
            if (setting.isAdvanced()) advanced.add(setting);
            else normal.add(setting);
        }
        for (Setting setting : normal) {
            y = drawSetting(context, category, module, setting, x, y, w, top, bodyH, mx, my);
        }
        if (!advanced.isEmpty()) {
            boolean showAdvanced = advancedOpen.contains(module);
            if (fits(y, 24, top, bodyH)) {
                context.fill(x, y, x + w, y + 24, inner());
                SlGuiPaint.textMid(context, textRenderer, "More settings", x + PAD, y + 12, TEXT_DIM, 10.0f);
                SlGuiPaint.textMid(context, textRenderer, showAdvanced ? "^" : "v",
                    x + w - PAD - 6, y + 12, accent(), 10.0f);
                hits.add(new Hit(x, y, w, 24, Kind.ADVANCED, category, module, null, null, 0, 0));
            }
            y += 24;
            if (showAdvanced) {
                for (Setting setting : advanced) {
                    y = drawSetting(context, category, module, setting, x, y, w, top, bodyH, mx, my);
                }
            }
        }
        return y + ROW_GAP;
    }

    private int settingHeight(Setting setting, int w) {
        if (setting instanceof NumberSetting) return 38;
        if (setting instanceof EnumSetting<?> enumer) {
            if (enumer.getValues().length > MAX_INLINE_OPTIONS) return 24;
            return 24 + enumLines(enumer, w) * LINE_H;
        }
        return 24;
    }

    private int enumLines(EnumSetting<?> setting, int w) {
        int lines = 1;
        int cursor = 0;
        int avail = w - PAD * 2;
        for (Enum<?> option : setting.getValues()) {
            int ow = optionWidth(option);
            if (cursor > 0 && cursor + ow > avail) {
                lines++;
                cursor = 0;
            }
            cursor += ow;
        }
        return lines;
    }

    private int optionWidth(Enum<?> option) {
        return SlGuiPaint.textWidth(textRenderer, enumLabel(option), 9.5f) + 14;
    }

    private int drawSetting(DrawContext context, Category category, Module module, Setting setting,
                            int x, int y, int w, int top, int bodyH, int mx, int my) {
        int h = settingHeight(setting, w);
        if (!fits(y, h, top, bodyH)) return y + h;

        context.fill(x, y, x + w, y + h, inner());
        if (hover(mx, my, x, y, w, h)) context.fill(x, y, x + w, y + h, argb(0xFFFFFF, 0.025f));
        int textX = x + PAD;
        int mid = y + 12; // single-line rows are 24px tall

        if (setting instanceof BooleanSetting bool) {
            boolean value = bool.isEnabled();
            int box = 12;
            int by = mid - box / 2;
            fillRounded(context, textX, by, textX + box, by + box,
                value ? argb(STATUS_GREEN, 0.28f) : argb(STATUS_RED, 0.18f), 3);
            if (value) {
                fillRounded(context, textX + 3, by + 6, textX + 5, by + 9, STATUS_GREEN, 1);
                fillRounded(context, textX + 5, by + 4, textX + 10, by + 8, STATUS_GREEN, 1);
            } else {
                int xw = SlGuiPaint.textWidth(textRenderer, "x", 10.0f);
                SlGuiPaint.textMid(context, textRenderer, "x", textX + (box - xw) / 2, mid, STATUS_RED, 10.0f);
            }
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w - box - PAD * 2 - 6, 10.0f),
                textX + box + 7, mid, value ? TEXT : TEXT_DIM, 10.0f);
            hits.add(new Hit(x, y, w, h, Kind.BOOL, category, module, setting, null, 0, 0));
        } else if (setting instanceof NumberSetting number) {
            String valueText = number.getDisplayValue();
            int valueW = SlGuiPaint.textWidth(textRenderer, valueText, 10.0f);
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w - valueW - PAD * 2 - 8, 10.0f),
                textX, y + 12, TEXT_DIM, 10.0f);
            SlGuiPaint.textMid(context, textRenderer, valueText, x + w - PAD - valueW, y + 12, TEXT, 10.0f);
            int trackW = w - PAD * 2;
            int trackY = y + 27;
            fillRounded(context, textX, trackY, textX + trackW, trackY + 5, TOGGLE_OFF, 2);
            double span = number.getMax() - number.getMin();
            double percent = span <= 0 ? 0 : clamp((number.getValue() - number.getMin()) / span, 0.0, 1.0);
            int fillEnd = textX + (int) (trackW * percent);
            if (fillEnd - textX > 1) {
                SlGuiPaint.fillRoundedGradient(context, textX, trackY, fillEnd - textX, 5, hi(), lo(), 2);
            }
            fillRounded(context, fillEnd - 5, trackY - 2, fillEnd + 6, trackY + 7, argb(accent(), 0.28f), 5);
            fillRounded(context, fillEnd - 4, trackY - 1, fillEnd + 5, trackY + 6, 0xFFFFFFFF, 4);
            hits.add(new Hit(x, y, w, h, Kind.SLIDER, category, module, setting, null, textX, trackW));
        } else if (setting instanceof EnumSetting<?> enumer) {
            Enum<?>[] values = enumer.getValues();
            if (values.length > MAX_INLINE_OPTIONS) {
                String value = SlGuiPaint.fit(textRenderer, enumer.getDisplayValue(), w / 2 - PAD, 10.0f);
                SlGuiPaint.textMid(context, textRenderer,
                    SlGuiPaint.fit(textRenderer, setting.getName() + ":", w / 2 - PAD, 10.0f),
                    textX, mid, TEXT_DIM, 10.0f);
                SlGuiPaint.textMid(context, textRenderer, value,
                    x + w - PAD - SlGuiPaint.textWidth(textRenderer, value, 10.0f), mid, hi(), 10.0f);
                hits.add(new Hit(x, y, w, h, Kind.ENUM_CYCLE, category, module, setting, null, 0, 0));
            } else {
                SlGuiPaint.textMid(context, textRenderer, setting.getName() + ":", textX, mid, TEXT_DIM, 10.0f);
                int cx = textX;
                int rowTop = y + 22;
                int avail = w - PAD * 2;
                Enum<?> selected = enumer.getValue();
                for (Enum<?> option : values) {
                    int ow = optionWidth(option);
                    if (cx > textX && cx - textX + ow > avail) {
                        cx = textX;
                        rowTop += LINE_H;
                    }
                    boolean isSelected = option == selected;
                    int pillW = ow - 2;
                    if (isSelected) fillRounded(context, cx, rowTop, cx + pillW, rowTop + 14, argb(accent(), 0.22f), 7);
                    String label = enumLabel(option);
                    int lw = SlGuiPaint.textWidth(textRenderer, label, 9.5f);
                    SlGuiPaint.textMid(context, textRenderer, label, cx + (pillW - lw) / 2, rowTop + 7,
                        isSelected ? hi() : TEXT_DIM, 9.5f);
                    hits.add(new Hit(cx, rowTop, pillW, 14, Kind.ENUM_OPTION, category, module, setting, option, 0, 0));
                    cx += ow;
                }
            }
        } else if (setting instanceof KeybindSetting keybind) {
            boolean capturing = bindingSetting == keybind;
            String shown = SlGuiPaint.fit(textRenderer, capturing ? "Press a key" : keybind.getDisplayValue(),
                w / 2 - PAD, 10.0f);
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w / 2 - PAD, 10.0f), textX, mid, TEXT_DIM, 10.0f);
            SlGuiPaint.textMid(context, textRenderer, shown,
                x + w - PAD - SlGuiPaint.textWidth(textRenderer, shown, 10.0f), mid,
                capturing ? accent() : TEXT, 10.0f);
            hits.add(new Hit(x, y, w, h, Kind.KEYBIND, category, module, setting, null, 0, 0));
        } else if (setting instanceof ChecklistSetting checklist) {
            String shown = SlGuiPaint.fit(textRenderer, checklist.getDisplayValue() + "  >", w / 2 - PAD, 10.0f);
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w / 2 - PAD, 10.0f), textX, mid, TEXT_DIM, 10.0f);
            SlGuiPaint.textMid(context, textRenderer, shown,
                x + w - PAD - SlGuiPaint.textWidth(textRenderer, shown, 10.0f), mid, accent(), 10.0f);
            hits.add(new Hit(x, y, w, h, Kind.CHECKLIST, category, module, setting, null, 0, 0));
        } else if (setting instanceof StringSetting text) {
            boolean editing = editingString == text;
            String value = editing ? editingText : text.getDisplayValue();
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w / 3, 10.0f), textX, mid, TEXT_DIM, 10.0f);
            String shown = SlGuiPaint.fit(textRenderer, value, w * 3 / 5 - PAD, 10.0f);
            int shownW = SlGuiPaint.textWidth(textRenderer, shown, 10.0f);
            int shownX = x + w - PAD - shownW;
            SlGuiPaint.textMid(context, textRenderer, shown, shownX, mid, editing ? accent() : TEXT, 10.0f);
            if (editing && (System.nanoTime() / 500_000_000L) % 2 == 0) {
                fillRounded(context, shownX + shownW + 1, mid - 6, shownX + shownW + 2, mid + 6, accent(), 1);
            }
            hits.add(new Hit(x, y, w, h, Kind.STRING, category, module, setting, null, 0, 0));
        }
        return y + h;
    }

    // ------------------------------------------------------------------- input

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        float scale = guiScale();
        double mx = click.x() / scale;
        double my = click.y() / scale + openSlideY;
        int button = click.button();
        commitEditing();
        bindingSetting = null;

        // Scrollbar thumbs are hit before the generic list so dragging works
        // even while the panel content is still easing toward its target.
        for (Map.Entry<Category, int[]> entry : scrollbarRects.entrySet()) {
            int[] rect = entry.getValue();
            if (hover(mx, my, rect[0], rect[1], rect[2], rect[3])) {
                draggingScroll = entry.getKey();
                dragScrollTop = rect[4]; // thumb height
                dragScrollLength = rect[3]; // track height
                dragScrollMax = rect[5];
                updateScroll(draggingScroll, my - rect[4] / 2.0);
                return true;
            }
        }

        for (int i = hits.size() - 1; i >= 0; i--) {
            Hit hit = hits.get(i);
            if (!hover(mx, my, hit.x(), hit.y(), hit.w(), hit.h())) continue;
            boolean left = button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
            boolean right = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
            switch (hit.kind()) {
                case HEADER -> {
                    if (left) {
                        draggingPanel = hit.category();
                        grabX = (int) (mx - hit.x());
                        grabY = (int) (my - hit.y());
                    }
                }
                case MODULE -> {
                    if (left) {
                        hit.module().toggle();
                    } else if (right && !hit.module().getSettings().isEmpty()) {
                        if (!expanded.remove(hit.module())) expanded.add(hit.module());
                    }
                }
                case BOOL -> {
                    BooleanSetting bool = (BooleanSetting) hit.setting();
                    if (left) bool.toggle();
                    else if (right) bool.reset();
                }
                case SLIDER -> {
                    NumberSetting number = (NumberSetting) hit.setting();
                    if (right) {
                        number.reset();
                    } else if (left) {
                        draggedSlider = number;
                        dragTrackX = hit.trackX();
                        dragTrackW = hit.trackW();
                        updateSlider(number, mx);
                    }
                }
                case ENUM_OPTION -> {
                    if (left) setEnum(hit.setting(), hit.value());
                    else if (right) hit.setting().reset();
                }
                case ENUM_CYCLE -> {
                    EnumSetting<?> enumer = (EnumSetting<?>) hit.setting();
                    if (left) {
                        if ((click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) enumer.cycleBack();
                        else enumer.cycle();
                    } else if (right) {
                        enumer.reset();
                    }
                }
                case KEYBIND -> {
                    KeybindSetting keybind = (KeybindSetting) hit.setting();
                    if (left) bindingSetting = keybind;
                    else if (right) keybind.clear();
                }
                case CHECKLIST -> {
                    if (left && client != null) {
                        client.setScreen(new ChecklistScreen(this, (ChecklistSetting) hit.setting()));
                    }
                }
                case STRING -> {
                    StringSetting text = (StringSetting) hit.setting();
                    if (left) {
                        editingString = text;
                        editingText = text.getValue();
                    } else if (right) {
                        text.reset();
                    }
                }
                case THEME -> {
                    if (left) {
                        GuiThemeModule module = themeModule();
                        GuiTheme[] themes = GuiThemeModule.themeCycle();
                        int index = (Integer) hit.value();
                        if (module != null && index >= 0 && index < themes.length) {
                            if ((click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) swapLayout();
                            else module.setTheme(themes[index]);
                        }
                    }
                }
                case LAYOUT -> {
                    if (left) {
                        GuiThemeModule module = themeModule();
                        if (module == null) return true;
                        if ((click.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) swapLayout();
                        else module.cycleTheme();
                    }
                }
                case SEARCH -> {
                    if (left) {
                        editingSearch = true;
                        searchText = search;
                    }
                }
                case ADVANCED -> {
                    if (left && !advancedOpen.remove(hit.module())) advancedOpen.add(hit.module());
                }
            }
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        float scale = guiScale();
        double mx = click.x() / scale;
        double my = click.y() / scale + openSlideY;
        if (draggingScroll != null) {
            updateScroll(draggingScroll, my - dragScrollTop / 2.0);
            return true;
        }
        if (draggingPanel != null) {
            int index = Arrays.asList(categories).indexOf(draggingPanel);
            int[] off = panelOffset.computeIfAbsent(draggingPanel, key -> new int[2]);
            int newX = (int) clamp(mx - grabX, 0, Math.max(0, vw() - panelWidth()));
            int newY = (int) clamp(my - grabY, 0, Math.max(0, vh() - HEADER_H));
            off[0] = newX - baseX(index);
            off[1] = newY - baseY();
            return true;
        }
        if (draggedSlider != null) {
            updateSlider(draggedSlider, mx);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        draggingPanel = null;
        draggingScroll = null;
        draggedSlider = null;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float scale = guiScale();
        double mx = mouseX / scale;
        double my = mouseY / scale + openSlideY;
        for (Category category : categories) {
            int[] rect = panelRects.get(category);
            if (rect == null || !hover(mx, my, rect[0], rect[1], rect[2], rect[3])) continue;
            double max = Math.max(0, contentHeight.getOrDefault(category, 0) - bodyHeight.getOrDefault(category, 0));
            double target = targetScroll.getOrDefault(category, 0.0f) - verticalAmount * 24.0;
            targetScroll.put(category, (float) clamp(target, 0.0, max));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (editingSearch) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                editingSearch = false;
                searchText = search;
            } else if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
                commitEditing();
            } else if (input.key() == GLFW.GLFW_KEY_BACKSPACE && !searchText.isEmpty()) {
                int last = searchText.codePointCount(0, searchText.length()) - 1;
                searchText = searchText.substring(0, searchText.offsetByCodePoints(0, last));
            }
            return true;
        }
        if (editingString != null) {
            if (input.key() == GLFW.GLFW_KEY_ESCAPE) {
                editingString = null;
                editingText = null;
            } else if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
                commitEditing();
            } else if (input.key() == GLFW.GLFW_KEY_BACKSPACE && editingText != null && !editingText.isEmpty()) {
                editingText = editingText.substring(0, editingText.length() - 1);
            }
            return true;
        }
        if (bindingSetting != null) {
            if (input.key() != GLFW.GLFW_KEY_ESCAPE) bindingSetting.set(input);
            bindingSetting = null;
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (editingSearch && input.isValidChar()) {
            String value = input.asString();
            if (value != null && !value.isEmpty()
                && value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && searchText.length() < 128) {
                searchText += value;
            }
            return true;
        }
        if (editingString != null && input.isValidChar()) {
            String value = input.asString();
            if (value != null && !value.isEmpty()
                && value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && editingText.length() < 128) {
                editingText += value;
            }
            return true;
        }
        return super.charTyped(input);
    }

    private void commitEditing() {
        if (editingSearch) {
            search = searchText == null ? "" : searchText;
            editingSearch = false;
        }
        if (editingString != null && editingText != null) editingString.setValue(editingText);
        editingString = null;
        editingText = null;
    }

    /** Title-tag parity: shift-click changes shell, plain click changes palette. */
    private void swapLayout() {
        GuiThemeModule module = themeModule();
        if (module == null || client == null) return;
        module.toggleMenuLayout();
        client.setScreen(module.menuLayout() == MenuLayout.DROPDOWN
            ? new DropdownMenuScreen(modules) : new ClickGuiScreen(modules));
    }

    private void updateScroll(Category category, double thumbTop) {
        int[] rect = scrollbarRects.get(category);
        if (rect == null) return;
        int trackHeight = Math.max(1, rect[3] - rect[4]);
        double progress = clamp((thumbTop - rect[1]) / trackHeight, 0.0, 1.0);
        targetScroll.put(category, (float) (progress * dragScrollMax));
    }

    /**
     * Raw texture draw. drawGuiTexture resolves through the sprite atlas and
     * shows the purple missing-texture tile for these standalone PNGs.
     */
    private void drawMenuIcon(DrawContext context, Identifier id, int x, int y, int size, float alpha) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, id, x, y,
            0.0f, 0.0f, size, size, 64, 64, 64, 64,
            (Math.max(0, Math.min(255, (int) (alpha * 255.0f))) << 24) | 0x00FFFFFF);
    }

    @Override
    public void close() {
        commitEditing();
        ConfigStore.save(modules);
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // ----------------------------------------------------------------- helpers

    private void updateSlider(NumberSetting setting, double mouseX) {
        if (dragTrackW <= 0) return;
        double percent = clamp((mouseX - dragTrackX) / (double) dragTrackW, 0.0, 1.0);
        double value = setting.getMin() + percent * (setting.getMax() - setting.getMin());
        boolean wholeRange = setting.getMin() == Math.floor(setting.getMin())
            && setting.getMax() == Math.floor(setting.getMax())
            && setting.getMax() - setting.getMin() <= 20.0;
        if (wholeRange) value = Math.round(value);
        setting.setValue(value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void setEnum(Setting setting, Object value) {
        ((EnumSetting) setting).setValue((Enum) value);
    }

    private static String enumLabel(Enum<?> value) {
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

    private float anim(Map<Object, Float> store, Object key, float target, float speed) {
        float current = store.getOrDefault(key, target);
        current += (target - current) * (1.0f - (float) Math.exp(-frameDt * speed));
        if (Math.abs(target - current) < 0.003f) current = target;
        store.put(key, current);
        return current;
    }

    private static boolean hover(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int argb(int color, float alpha) {
        int a = Math.max(0, Math.min(255, (int) (alpha * 255.0f)));
        return (a << 24) | (color & 0x00FFFFFF);
    }

    private static int mixColor(int from, int to, float t) {
        float f = Math.max(0.0f, Math.min(1.0f, t));
        int a = (int) (((from >>> 24) & 0xFF) * (1 - f) + ((to >>> 24) & 0xFF) * f);
        int r = (int) (((from >> 16) & 0xFF) * (1 - f) + ((to >> 16) & 0xFF) * f);
        int g = (int) (((from >> 8) & 0xFF) * (1 - f) + ((to >> 8) & 0xFF) * f);
        int b = (int) ((from & 0xFF) * (1 - f) + (to & 0xFF) * f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Anti-aliased rounded rectangle, same cached routine as the ClickGUI layout. */
    private static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int radius) {
        // Preserve this screen's old degenerate-rect call; the shared helper skips it.
        if (x2 <= x1 || y2 <= y1) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        SlGuiPaint.fillRounded(context, x1, y1, x2, y2, color, radius);
    }
}
