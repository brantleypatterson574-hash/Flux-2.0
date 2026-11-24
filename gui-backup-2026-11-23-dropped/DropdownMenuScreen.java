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
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
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

    private record Palette(String name, int accent, int hi, int lo, int top, int bg,
                           int card, int hover, int inner, int border) {}

    private static final Palette[] PALETTES = {
        new Palette("Pink",   0xFFD96AD8, 0xFFEE8EEA, 0xFFA45BC8, 0xFF181620, 0xFF0F0E15, 0xFF15141D, 0xFF1D1B27, 0xFF110F18, 0xFF272530),
        new Palette("Purple", 0xFF9B7BFF, 0xFFB9A2FF, 0xFF6A4DD0, 0xFF16151F, 0xFF0E0D14, 0xFF14131C, 0xFF1C1B26, 0xFF100F17, 0xFF262531),
        new Palette("Blue",   0xFF4D9DFF, 0xFF8CC0FF, 0xFF2F6FCC, 0xFF121620, 0xFF0B0E15, 0xFF131720, 0xFF1A1F2B, 0xFF0F121A, 0xFF242A38),
        new Palette("Green",  0xFF3DDC84, 0xFF7AEDAE, 0xFF22A35E, 0xFF121A16, 0xFF0B110E, 0xFF131B17, 0xFF1A241F, 0xFF0F1512, 0xFF243029),
        new Palette("Red",    0xFFFF5C5C, 0xFFFF8F8F, 0xFFC03A3A, 0xFF1A1314, 0xFF110B0C, 0xFF1B1415, 0xFF251A1B, 0xFF150F10, 0xFF302224),
        new Palette("Orange", 0xFFFF9F43, 0xFFFFC078, 0xFFCC7420, 0xFF1A1612, 0xFF110E0B, 0xFF1B1713, 0xFF25201A, 0xFF15110F, 0xFF302A22),
        new Palette("Grey",   0xFFB8B8BE, 0xFFE4E4E7, 0xFF6E6E73, 0xFF141414, 0xFF0C0C0C, 0xFF161616, 0xFF1D1D1D, 0xFF121212, 0xFF2B2B2B)
    };
    private static int savedPalette = 0;
    private Palette pal = PALETTES[Math.floorMod(savedPalette, PALETTES.length)];

    private static final int TEXT = 0xFFF4F4F5;
    private static final int TEXT_DIM = 0xFF8E8E93;
    private static final int TOGGLE_OFF = 0xFF303030;
    private static final int STATUS_GREEN = 0xFF3DDC84;
    private static final int STATUS_RED = 0xFFFF5555;

    private enum Kind { HEADER, MODULE, BOOL, SLIDER, ENUM_OPTION, ENUM_CYCLE, KEYBIND, CHECKLIST, STRING, ADVANCED, THEME }

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

    private long openStart;
    private float openSlideY;
    private long lastFrameNanos;
    private float frameDt = 0.016f;

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
        if (openStart == 0) openStart = System.currentTimeMillis();
    }

    // ------------------------------------------------------------------ layout

    private GuiThemeModule themeModule() {
        return GuiThemeModule.INSTANCE;
    }

    private float guiScale() {
        GuiThemeModule module = themeModule();
        return SlGuiPaint.responsiveScale(height, module == null ? 1.0f : module.guiScale());
    }

    private int vw() {
        return Math.max(1, (int) (width / guiScale()));
    }

    private int vh() {
        return Math.max(1, (int) (height / guiScale()));
    }

    private int panelWidth() {
        int n = categories.length;
        int available = vw() - 32 - (n - 1) * PANEL_GAP;
        return Math.max(130, Math.min(PANEL_W, available / n));
    }

    private int panelHeight() {
        return Math.max(220, Math.min(vh() - 96, 560));
    }

    private int baseX(int index) {
        int pw = panelWidth();
        int total = categories.length * pw + (categories.length - 1) * PANEL_GAP;
        return (vw() - total) / 2 + index * (pw + PANEL_GAP);
    }

    private int baseY() {
        return (vh() - panelHeight()) / 2;
    }

    // ------------------------------------------------------------------ render

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float scale = guiScale();
        float progress = Math.min(1.0f, (System.currentTimeMillis() - openStart) / 220.0f);
        float ease = progress * progress * (3.0f - 2.0f * progress);
        openSlideY = (1.0f - ease) * 12.0f;
        int mx = (int) (mouseX / scale);
        int my = (int) (mouseY / scale + openSlideY);

        long now = System.nanoTime();
        frameDt = lastFrameNanos == 0 ? 0.016f : Math.min(0.1f, (now - lastFrameNanos) / 1.0e9f);
        lastFrameNanos = now;

        GuiThemeModule themeModule = themeModule();
        GuiTheme theme = themeModule != null ? themeModule.getTheme() : GuiTheme.MATRIX;
        int overlay = themeModule != null ? themeModule.overlayColor() : theme.overlay;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);
        SlGuiPaint.overlayWashBytes(context, vw(), vh(), overlay, 38, 112);
        int overlayAlpha = (overlay >>> 24) & 0xFF;
        float strength = overlayAlpha == 0 ? 1.0f : overlayAlpha / 255.0f;
        context.fill(0, 0, vw(), vh(), SlGuiPaint.alpha(0.25f * strength, 0x000000));
        context.getMatrices().translate(0.0f, -openSlideY);

        hits.clear();
        float smooth = 1.0f - (float) Math.exp(-frameDt * 14.0f);
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

        drawThemes(context, mx, my);

        float fade = (1.0f - ease) * 0.55f;
        if (fade > 0.001f) {
            int slide = Math.round(openSlideY);
            context.fill(0, slide, vw(), vh() + slide, SlGuiPaint.alpha(fade, 0x000000));
        }
        context.getMatrices().popMatrix();
    }

    private void drawThemes(DrawContext context, int mx, int my) {
        int w = 176;
        int head = 38;
        int rowH = 26;
        int h = head + PALETTES.length * rowH + 8;
        int x = vw() - w - 16;
        int y = vh() - h - 16;
        fillRounded(context, x - 1, y - 1, x + w + 1, y + h + 1, argb(pal.accent(), 0.4f), 13);
        SlGuiPaint.fillRoundedGradient(context, x, y, w, h, pal.top(), pal.bg(), 12);
        SlGuiPaint.titleMid(context, textRenderer, "Themes", x + 18, y + head / 2,
            SlGuiPaint.titleColor(TEXT), 13.0f);
        fillRounded(context, x + 18 - 10, y + head / 2 - 2, x + 18 - 6, y + head / 2 + 2, pal.accent(), 2);
        context.fill(x + 1, y + head - 1, x + w - 1, y + head, pal.border());
        for (int i = 0; i < PALETTES.length; i++) {
            Palette p = PALETTES[i];
            int ry = y + head + 4 + i * rowH;
            boolean selected = p == pal;
            boolean hovered = hover(mx, my, x + 6, ry, w - 12, rowH);
            if (selected || hovered) {
                fillRounded(context, x + 6, ry, x + w - 6, ry + rowH,
                    selected ? argb(p.accent(), 0.16f) : argb(0xFFFFFF, 0.04f), 8);
            }
            int mid = ry + rowH / 2;
            fillRounded(context, x + 14, mid - 7, x + 28, mid + 7, p.accent(), 4);
            fillRounded(context, x + 31, mid - 7, x + 45, mid + 7, p.lo(), 4);
            SlGuiPaint.textMid(context, textRenderer, p.name(), x + 56, mid, selected ? TEXT : TEXT_DIM, 11.0f);
            hits.add(new Hit(x + 6, ry, w - 12, rowH, Kind.THEME, null, null, null, i, 0, 0));
        }
    }

    private void drawPanel(DrawContext context, Category category, int x, int y, int w, int h,
                           int mx, int my) {
        panelRects.put(category, new int[] {x, y, w, h});

        for (int i = 8; i >= 1; i--) {
            fillRounded(context, x - i, y - i + 5, x + w + i, y + h + i + 5,
                argb(0x000000, 0.03f), PANEL_RADIUS + i);
        }
        fillRounded(context, x - 1, y - 1, x + w + 1, y + h + 1, argb(pal.accent(), 0.45f), PANEL_RADIUS + 1);
        SlGuiPaint.fillRoundedGradient(context, x, y, w, h, pal.top(), pal.bg(), PANEL_RADIUS);
        context.fill(x + PANEL_RADIUS, y + 1, x + w - PANEL_RADIUS, y + 2, argb(0xFFFFFF, 0.06f));

        // Header: letter chip + category name, both centred on the header's midline.
        int chip = 20;
        int mid = y + HEADER_H / 2;
        String name = category.getName();
        int nameW = SlGuiPaint.textWidth(textRenderer, name, 13.0f);
        int groupW = chip + 9 + nameW;
        int gx = x + (w - groupW) / 2;
        SlGuiPaint.fillRoundedGradient(context, gx, mid - chip / 2, chip, chip, pal.hi(), pal.lo(), 7);
        String letter = name.substring(0, 1).toUpperCase(Locale.ROOT);
        SlGuiPaint.titleMid(context, textRenderer, letter,
            gx + (chip - SlGuiPaint.textWidth(textRenderer, letter, 10.0f)) / 2, mid,
            SlGuiPaint.titleColor(0xFFFFFFFF), 10.0f);
        SlGuiPaint.titleMid(context, textRenderer, name, gx + chip + 9, mid,
            SlGuiPaint.titleColor(TEXT), 13.0f);
        context.fill(x + 1, y + HEADER_H - 1, x + w - 1, y + HEADER_H, pal.border());
        hits.add(new Hit(x, y, w, HEADER_H, Kind.HEADER, category, null, null, null, 0, 0));

        int bx = x + PAD;
        int bw = w - PAD * 2;
        int by = y + HEADER_H + PAD;
        int bh = h - HEADER_H - PAD * 2;
        bodyHeight.put(category, bh);

        float scrollValue = scroll.getOrDefault(category, 0.0f);
        int origin = by - (int) scrollValue;
        int cursor = origin;
        for (Module module : byCategory.get(category)) {
            cursor = drawModule(context, category, module, bx, cursor, bw, by, bh, mx, my);
        }
        int total = cursor - origin;
        contentHeight.put(category, total);
        float maxScroll = Math.max(0, total - bh);
        if (targetScroll.getOrDefault(category, 0.0f) > maxScroll) targetScroll.put(category, maxScroll);

        if (maxScroll > 0) {
            int thumb = Math.max(18, bh * bh / Math.max(1, total));
            int thumbY = by + (int) ((bh - thumb) * (scrollValue / maxScroll));
            fillRounded(context, x + w - 5, thumbY, x + w - 2, thumbY + thumb, argb(pal.accent(), 0.45f), 1);
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
            float hv = anim(hoverAnims, module, hovered ? 1.0f : 0.0f, 16.0f);
            float on = anim(toggleAnims, module, enabled ? 1.0f : 0.0f, 14.0f);
            int rim = mixColor(pal.border(), pal.lo(), Math.min(1.0f, hv * 0.6f + on * 0.4f));
            fillRounded(context, x - 1, y - 1, x + w + 1, y + ROW_H + 1, rim, ROW_RADIUS + 1);
            fillRounded(context, x, y, x + w, y + ROW_H, mixColor(pal.card(), pal.hover(), hv), ROW_RADIUS);
            if (on > 0.01f) {
                SlGuiPaint.fillRoundedGradient(context, x, y, w, ROW_H,
                    argb(pal.accent(), 0.14f * on), argb(pal.accent(), 0.03f * on), ROW_RADIUS);
            }
            int mid = y + ROW_H / 2;
            String label = SlGuiPaint.fit(textRenderer, module.getName(), w - 36, 11.5f);
            int labelW = SlGuiPaint.textWidth(textRenderer, label, 11.5f);
            SlGuiPaint.titleMid(context, textRenderer, label, x + (w - labelW) / 2, mid,
                SlGuiPaint.titleColor(mixColor(0xFFB4B4B8, pal.hi(), on)), 11.5f);
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
                context.fill(x, y, x + w, y + 24, pal.inner());
                SlGuiPaint.textMid(context, textRenderer, "More settings", x + PAD, y + 12, TEXT_DIM, 10.0f);
                SlGuiPaint.textMid(context, textRenderer, showAdvanced ? "^" : "v",
                    x + w - PAD - 6, y + 12, pal.accent(), 10.0f);
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

        context.fill(x, y, x + w, y + h, pal.inner());
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
                SlGuiPaint.fillRoundedGradient(context, textX, trackY, fillEnd - textX, 5, pal.hi(), pal.lo(), 2);
            }
            fillRounded(context, fillEnd - 5, trackY - 2, fillEnd + 6, trackY + 7, argb(pal.accent(), 0.28f), 5);
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
                    x + w - PAD - SlGuiPaint.textWidth(textRenderer, value, 10.0f), mid, pal.hi(), 10.0f);
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
                    if (isSelected) fillRounded(context, cx, rowTop, cx + pillW, rowTop + 14, argb(pal.accent(), 0.22f), 7);
                    String label = enumLabel(option);
                    int lw = SlGuiPaint.textWidth(textRenderer, label, 9.5f);
                    SlGuiPaint.textMid(context, textRenderer, label, cx + (pillW - lw) / 2, rowTop + 7,
                        isSelected ? pal.hi() : TEXT_DIM, 9.5f);
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
                capturing ? pal.accent() : TEXT, 10.0f);
            hits.add(new Hit(x, y, w, h, Kind.KEYBIND, category, module, setting, null, 0, 0));
        } else if (setting instanceof ChecklistSetting checklist) {
            String shown = SlGuiPaint.fit(textRenderer, checklist.getDisplayValue() + "  >", w / 2 - PAD, 10.0f);
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w / 2 - PAD, 10.0f), textX, mid, TEXT_DIM, 10.0f);
            SlGuiPaint.textMid(context, textRenderer, shown,
                x + w - PAD - SlGuiPaint.textWidth(textRenderer, shown, 10.0f), mid, pal.accent(), 10.0f);
            hits.add(new Hit(x, y, w, h, Kind.CHECKLIST, category, module, setting, null, 0, 0));
        } else if (setting instanceof StringSetting text) {
            boolean editing = editingString == text;
            String value = editing ? editingText : text.getDisplayValue();
            SlGuiPaint.textMid(context, textRenderer,
                SlGuiPaint.fit(textRenderer, setting.getName(), w / 3, 10.0f), textX, mid, TEXT_DIM, 10.0f);
            String shown = SlGuiPaint.fit(textRenderer, value, w * 3 / 5 - PAD, 10.0f);
            int shownW = SlGuiPaint.textWidth(textRenderer, shown, 10.0f);
            int shownX = x + w - PAD - shownW;
            SlGuiPaint.textMid(context, textRenderer, shown, shownX, mid, editing ? pal.accent() : TEXT, 10.0f);
            if (editing && (System.currentTimeMillis() / 500) % 2 == 0) {
                fillRounded(context, shownX + shownW + 1, mid - 6, shownX + shownW + 2, mid + 6, pal.accent(), 1);
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
                        savedPalette = (Integer) hit.value();
                        pal = PALETTES[savedPalette];
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
        if (editingString != null && editingText != null) editingString.setValue(editingText);
        editingString = null;
        editingText = null;
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
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
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

    /** Anti-aliased rounded rectangle, same routine as the ClickGUI layout. */
    private static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int radius) {
        int w = x2 - x1;
        int h = y2 - y1;
        int r = Math.min(radius, Math.min(w, h) / 2);
        if (r <= 0) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        context.fill(x1 + r, y1, x2 - r, y2, color);
        context.fill(x1, y1 + r, x1 + r, y2 - r, color);
        context.fill(x2 - r, y1 + r, x2, y2 - r, color);
        int alpha = color >>> 24;
        int rgb = color & 0x00FFFFFF;
        boolean smooth = alpha >= 40;
        for (int dy = 0; dy < r; dy++) {
            double dyFromCenter = r - dy - 0.5;
            double edge = r - Math.sqrt(Math.max(0.0, (double) r * r - dyFromCenter * dyFromCenter));
            int solid = (int) Math.ceil(edge);
            if (solid < r) {
                context.fill(x1 + solid, y1 + dy, x1 + r, y1 + dy + 1, color);
                context.fill(x2 - r, y1 + dy, x2 - solid, y1 + dy + 1, color);
                context.fill(x1 + solid, y2 - dy - 1, x1 + r, y2 - dy, color);
                context.fill(x2 - r, y2 - dy - 1, x2 - solid, y2 - dy, color);
            }
            if (smooth && solid > 0) {
                double cover = solid - edge;
                if (cover > 0.04) {
                    int edgeColor = (Math.round((float) (alpha * cover)) << 24) | rgb;
                    context.fill(x1 + solid - 1, y1 + dy, x1 + solid, y1 + dy + 1, edgeColor);
                    context.fill(x2 - solid, y1 + dy, x2 - solid + 1, y1 + dy + 1, edgeColor);
                    context.fill(x1 + solid - 1, y2 - dy - 1, x1 + solid, y2 - dy, edgeColor);
                    context.fill(x2 - solid, y2 - dy - 1, x2 - solid + 1, y2 - dy, edgeColor);
                }
            }
        }
    }
}
