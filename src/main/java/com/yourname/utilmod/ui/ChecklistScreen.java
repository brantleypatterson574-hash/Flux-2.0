package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.impl.GuiThemeModule;
import com.yourname.utilmod.setting.ChecklistSetting;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Searchable checkbox screen for a {@link ChecklistSetting}. */
public class ChecklistScreen extends Screen {
    private static final int PANEL_WIDTH = 540;
    private static final int TITLE_HEIGHT = 32;
    private static final int ROW_HEIGHT = 20;
    private static final int ROW_GAP = 2;

    private final Screen parent;
    private final ChecklistSetting setting;

    private final String searchPlaceholder;
    private String cachedTitleValue;
    private String cachedTitle;
    private String search = "";
    private boolean searchFocused;
    private float scroll;
    private float targetScroll;
    private List<ChecklistSetting.Option> cachedOptions;
    private String cachedSearch;

    public ChecklistScreen(Screen parent, ChecklistSetting setting) {
        super(Text.literal(setting.getName()));
        this.parent = parent;
        this.setting = setting;
        this.searchPlaceholder = "Search " + setting.getName().toLowerCase(Locale.ROOT) + "...";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float scale = guiScale();
        int mx = (int) (mouseX / scale);
        int my = (int) (mouseY / scale);

        GuiTheme theme = theme();
        GuiThemeModule module = GuiThemeModule.INSTANCE;
        int radius = module == null || module.rounded() ? 18 : 0;
        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale);
        SlGuiPaint.overlayWashBytes(context, vw(), vh(),
            module != null ? module.overlayColor() : theme.overlay, 64, 134);

        int panelW = Math.min(PANEL_WIDTH, vw() - 24);
        int panelH = Math.min(560, vh() - 32);
        int px = (vw() - panelW) / 2;
        int py = (vh() - panelH) / 2;

        SlGuiPaint.panel(context, px, py, panelW, panelH,
            module != null ? module.panelColor() : theme.panel, theme, theme.accent,
            radius, module == null || module.shadow(), module == null || module.outlines());
        SlGuiPaint.fillRoundedGradient(context, px + 1, py + 1, panelW - 2, 40,
            SlGuiPaint.alpha(0.28f, theme.header & 0x00FFFFFF),
            SlGuiPaint.alpha(0.06f, theme.panel & 0x00FFFFFF), Math.max(0, radius - 1));
        SlGuiPaint.fillRounded(context, px + radius, py + TITLE_HEIGHT - 1,
            px + panelW - radius, py + TITLE_HEIGHT,
            SlGuiPaint.alpha(0.72f, theme.accent), 1);
        SlGuiPaint.fillRounded(context, px + 1, py + 8, px + 4, py + TITLE_HEIGHT - 8,
            theme.accent, 2);

        String displayValue = String.valueOf(setting.getDisplayValue());
        if (!displayValue.equals(cachedTitleValue)) {
            cachedTitleValue = displayValue;
            cachedTitle = setting.getName() + "  -  " + displayValue;
        }
        String title = SlGuiPaint.fit(textRenderer, cachedTitle, panelW - 88, 12.0f);
        SlGuiPaint.title(context, textRenderer, title, px + 12, py + 10,
            SlGuiPaint.titleColor(theme.titleAccent), 12.0f);

        int[] close = closeBounds(px, py, panelW);
        boolean closeHover = hovered(mx, my, close);
        SlGuiPaint.button(context, close[0], close[1], close[2], close[3],
            theme, theme.accent, false, closeHover, close[3] / 2, true);
        SlGuiPaint.text(context, textRenderer, "x",
            close[0] + (close[2] - SlGuiPaint.textWidth(textRenderer, "x", 9.0f)) / 2, close[1] + 5,
            closeHover ? theme.accent : theme.textSecondary);

        int[] searchBox = searchBounds(px, py, panelW);
        SlGuiPaint.field(context, searchBox[0], searchBox[1], searchBox[2], searchBox[3],
            theme, theme.accent, searchFocused, hovered(mx, my, searchBox), 10);
        String shownSearch = search.isEmpty() ? searchPlaceholder : search;
        SlGuiPaint.text(context, textRenderer,
            SlGuiPaint.fit(textRenderer, shownSearch, searchBox[2] - 16, 9.0f),
            searchBox[0] + 8, searchBox[1] + 5,
            search.isEmpty() ? theme.textSecondary : theme.text);

        int[] all = buttonBounds(px, py, panelW, 0);
        int[] clear = buttonBounds(px, py, panelW, 1);
        int[] done = buttonBounds(px, py, panelW, 2);
        drawButton(context, all, "All", mx, my, theme);
        drawButton(context, clear, "Clear", mx, my, theme);
        drawButton(context, done, "Done", mx, my, theme);

        int rowsTop = py + 100;
        // Leave the bottom band free for the hint instead of drawing over it.
        int rowsBottom = py + panelH - 30;
        List<ChecklistSetting.Option> options = options();
        int step = ROW_HEIGHT + ROW_GAP;
        int maxScroll = Math.max(0, options.size() * step - (rowsBottom - rowsTop));
        targetScroll = Math.max(0, Math.min(maxScroll, targetScroll));
        // Frame-rate independent smoothing, matching the main ClickGUI.
        float ease = 1.0f - (float) Math.exp(-Math.max(0.001f, delta) * 14.0f);
        scroll += (targetScroll - scroll) * ease;
        scroll = Math.max(0, Math.min(maxScroll, scroll));
        // Compute the first visible row only after the clamp, otherwise one
        // frame can render from a stale offset outside the new scroll range.
        int first = Math.max(0, (int) scroll / step - 1);

        for (int i = first; i < options.size(); i++) {
            int rowY = rowsTop + i * step - (int) scroll;
            if (rowY > rowsBottom) break;
            if (rowY + ROW_HEIGHT < rowsTop) continue;
            drawRow(context, options.get(i), px + 12, rowY, panelW - 24, mx, my, theme);
        }

        SlGuiPaint.scrollbar(context, px + panelW - 8, rowsTop, rowsBottom - rowsTop,
            maxScroll, scroll, theme, theme.accent, 3);

        String hint = "Click a row to toggle  -  ESC to save and return";
        SlGuiPaint.text(context, textRenderer, hint,
            px + (panelW - SlGuiPaint.textWidth(textRenderer, hint, 9.0f)) / 2,
            py + panelH - 18, theme.textSecondary);
        context.getMatrices().popMatrix();
    }

    private void drawButton(DrawContext context, int[] bounds, String label,
                            int mouseX, int mouseY, GuiTheme theme) {
        boolean hover = hovered(mouseX, mouseY, bounds);
        SlGuiPaint.button(context, bounds[0], bounds[1], bounds[2], bounds[3],
            theme, theme.accent, false, hover, bounds[3] / 2, true);
        SlGuiPaint.text(context, textRenderer, label,
            bounds[0] + (bounds[2] - SlGuiPaint.textWidth(textRenderer, label, 9.0f)) / 2,
            bounds[1] + 5, hover ? theme.accent : theme.text);
    }

    private void drawRow(DrawContext context, ChecklistSetting.Option option,
                         int x, int y, int w, int mouseX, int mouseY, GuiTheme theme) {
        boolean hover = hovered(mouseX, mouseY, x, y, w, ROW_HEIGHT);
        boolean selected = setting.isSelected(option.id());
        SlGuiPaint.fillRounded(context, x, y, x + w, y + ROW_HEIGHT,
            SlGuiPaint.alpha(hover ? 0.42f : 0.28f,
                (hover ? theme.settingRowHover : theme.settingRow) & 0x00FFFFFF), 8);
        SlGuiPaint.fillRounded(context, x, y + 4, x + 3, y + ROW_HEIGHT - 4,
            selected ? theme.accent : theme.outline, 1);
        SlGuiPaint.checkBox(context, x + 8, y + 3, 14, theme, theme.accent, selected, hover);
        SlGuiPaint.text(context, textRenderer, option.label(), x + 30, y + 4,
            selected ? theme.text : theme.textSecondary);

        int idX = x + w - 8 - SlGuiPaint.textWidth(textRenderer, option.id(), 9.0f);
        if (idX > x + 36 + SlGuiPaint.textWidth(textRenderer, option.label(), 9.0f)) {
            SlGuiPaint.text(context, textRenderer, option.id(), idX, y + 4, theme.textSecondary);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        float scale = guiScale();
        double mx = click.x() / scale;
        double my = click.y() / scale;
        int button = click.button();

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            search = "";
            searchFocused = false;
            return true;
        }

        int panelW = Math.min(PANEL_WIDTH, vw() - 24);
        int panelH = Math.min(560, vh() - 32);
        int px = (vw() - panelW) / 2;
        int py = (vh() - panelH) / 2;

        if (hovered(mx, my, closeBounds(px, py, panelW))) {
            close();
            return true;
        }
        if (hovered(mx, my, searchBounds(px, py, panelW))) {
            searchFocused = true;
            return true;
        }
        searchFocused = false;

        if (hovered(mx, my, buttonBounds(px, py, panelW, 0))) {
            setting.selectAll();
            return true;
        }
        if (hovered(mx, my, buttonBounds(px, py, panelW, 1))) {
            setting.clear();
            return true;
        }
        if (hovered(mx, my, buttonBounds(px, py, panelW, 2))) {
            close();
            return true;
        }

        int rowsTop = py + 100;
        int rowsBottom = py + panelH - 30;
        int rowX = px + 12;
        int rowW = panelW - 24;
        if (mx >= rowX && mx < rowX + rowW && my >= rowsTop && my < rowsBottom) {
            List<ChecklistSetting.Option> options = options();
            int step = ROW_HEIGHT + ROW_GAP;
            int index = (int) ((my - rowsTop + scroll) / step);
            if (index >= 0 && index < options.size()) {
                int rowY = rowsTop + index * step - (int) scroll;
                if (my < rowY + ROW_HEIGHT) {
                    setting.toggle(options.get(index).id());
                    return true;
                }
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int panelH = Math.min(560, vh() - 32);
        int rowsTop = 100;
        int rowsBottom = panelH - 30;
        int step = ROW_HEIGHT + ROW_GAP;
        int maxScroll = Math.max(0, options().size() * step - (rowsBottom - rowsTop));
        targetScroll = (float) Math.max(0.0,
            Math.min(maxScroll, targetScroll - verticalAmount * 24.0));
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_ESCAPE || input.key() == GLFW.GLFW_KEY_ENTER
            || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
            close();
            return true;
        }
        if (searchFocused && input.key() == GLFW.GLFW_KEY_BACKSPACE && !search.isEmpty()) {
            search = search.substring(0, search.offsetByCodePoints(search.length(), -1));
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (searchFocused && input.isValidChar()) {
            String value = input.asString();
            if (value != null && !value.isEmpty()
                && value.codePoints().allMatch(code -> !Character.isISOControl(code))
                && search.length() + value.length() <= 96) {
                search += value;
            }
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private List<ChecklistSetting.Option> options() {
        String query = search.trim().toLowerCase(Locale.ROOT);
        if (cachedOptions == null || !query.equals(cachedSearch)) {
            cachedSearch = query;
            if (query.isEmpty()) {
                cachedOptions = setting.getOptions();
            } else {
                List<ChecklistSetting.Option> result = new ArrayList<>();
                for (ChecklistSetting.Option option : setting.getOptions()) {
                    if (option.id().toLowerCase(Locale.ROOT).contains(query)
                        || option.label().toLowerCase(Locale.ROOT).contains(query)) {
                        result.add(option);
                    }
                }
                cachedOptions = result;
            }
            targetScroll = 0;
            scroll = 0;
        }
        return cachedOptions;
    }

    private GuiTheme theme() {
        return GuiThemeModule.INSTANCE == null
            ? GuiTheme.UTILMOD : GuiThemeModule.INSTANCE.getTheme();
    }

    private float guiScale() {
        return SlGuiPaint.responsiveScale(height,
            GuiThemeModule.INSTANCE == null ? 1.0f : GuiThemeModule.INSTANCE.guiScale());
    }

    private int vw() {
        return Math.max(1, (int) (width / guiScale()));
    }

    private int vh() {
        return Math.max(1, (int) (height / guiScale()));
    }

    private int[] closeBounds(int px, int py, int panelW) {
        return new int[]{px + panelW - 32, py + 6, 24, 24};
    }

    private int[] searchBounds(int px, int py, int panelW) {
        return new int[]{px + 12, py + 42, panelW - 24, 22};
    }

    private int[] buttonBounds(int px, int py, int panelW, int index) {
        int y = py + 70;
        return switch (index) {
            case 0 -> new int[]{px + 12, y, 68, 22};
            case 1 -> new int[]{px + 88, y, 68, 22};
            default -> new int[]{px + panelW - 80, y, 68, 22};
        };
    }

    private static boolean hovered(double mx, double my, int[] bounds) {
        return hovered(mx, my, bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    private static boolean hovered(double mx, double my, int x, int y, int w, int h) {
        // Half-open bounds match Minecraft's fill() and avoid double hits on
        // the shared pixel between adjacent rows and buttons.
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int radius) {
        SlGuiPaint.fillRoundedFlat(context, x1, y1, x2, y2, color, radius);
    }
}
