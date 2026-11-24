package com.yourname.utilmod.ui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared primitives for the ScalableLux-style menu shells.
 *
 * <p>The original GPU menus draw rounded glass panels, soft shadows, accent
 * hairlines and pill chips through a shader display list. These helpers map the
 * same geometry and colour layering onto Minecraft's {@link DrawContext} so the
 * dropdown and ClickGUI keep a consistent look without any payload assets.
 */
public final class SlGuiPaint {
    /** ScalableLux dropdown/chip metrics, in virtual GUI pixels. */
    public static final int SIDE = 30;
    public static final int STRIP_Y = 66;
    public static final int STRIP_H = 28;
    public static final int PANEL_W = 206;
    public static final int PANEL_GAP = 12;
    public static final int HEADER_H = 36;
    public static final int ROW_H = 27;
    public static final int IPAD = 11;
    public static final int INDENT = 10;

    /**
     * Menu typeface. The staged font uses the semibold TTF at a high oversample,
     * so glyphs read as softly weighted and stay crisp at every GUI scale.
     */
    public static final Identifier FLUX_FONT = Identifier.of("scalablelux", "semibold");
    private static final Style FLUX_STYLE =
        Style.EMPTY.withFont(new StyleSpriteSource.Font(FLUX_FONT));

    private SlGuiPaint() {}

    /** Default vanilla font height; source label sizes are expressed in these units. */
    public static final float FONT_UNIT = 9.0f;

    /** Bounded cache: menu labels repeat every frame, so avoid rebuilding Text. */
    private static final Map<String, Text> STYLED_CACHE = new HashMap<>();
    private static final int STYLED_CACHE_LIMIT = 512;

    /** Wraps a string in the shared semibold style so width and drawing agree. */
    public static Text styled(String value) {
        String key = value == null ? "" : value;
        Text cached = STYLED_CACHE.get(key);
        if (cached != null) return cached;
        if (STYLED_CACHE.size() >= STYLED_CACHE_LIMIT) STYLED_CACHE.clear();
        Text text = Text.literal(key).setStyle(FLUX_STYLE);
        STYLED_CACHE.put(key, text);
        return text;
    }

    /** Source minimum scale, shared by the dropdown and classic shells. */
    public static final float MIN_AUTO_SCALE = 1.0909091f;

    /** Source classicAutoScale() with the user's Menu Scale value applied. */
    public static float responsiveScale(float screenHeight, float userScale) {
        float responsive = Math.max(0.7f, Math.min(3.2f, screenHeight / 800.0f));
        return Math.max(responsive * userScale, MIN_AUTO_SCALE);
    }

    /** Draws text at a source-style size without needing encrypted font assets. */
    public static void text(DrawContext context, TextRenderer renderer, String value,
                            int x, int y, int color, float size) {
        if (value == null) return;
        float factor = Math.max(0.1f, size / FONT_UNIT);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(factor, factor);
        context.drawText(renderer, styled(value), 0, 0, color, false);
        context.getMatrices().popMatrix();
    }

    /** Compact default-size variant used by tool-strips and glyph buttons. */
    public static void text(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color) {
        text(context, renderer, value, x, y, color, FONT_UNIT);
    }

    /**
     * Soft, non-shadowed title text. The weight comes from the semibold font
     * rather than the vanilla shadow pass, so headings stay clean and crisp.
     */
    public static void title(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color) {
        title(context, renderer, value, x, y, color, FONT_UNIT);
    }

    /** Scaled variant for headings that use a source-specific label size. */
    public static void title(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color, float size) {
        if (value == null) return;
        float factor = Math.max(0.1f, size / FONT_UNIT);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(factor, factor);
        context.drawText(renderer, styled(value), 0, 0, color, false);
        context.getMatrices().popMatrix();
    }

    /** Lightens a theme colour toward white for the soft title style. */
    public static int titleColor(int color) {
        return mix(color & 0x00FFFFFF, 0xFFFFFF, 0.24f) | 0xFF000000;
    }

    /** Draws text vertically centred on centerY. The text box is as tall as its source size. */
    public static void textMid(DrawContext context, TextRenderer renderer, String value,
                               int x, int centerY, int color, float size) {
        text(context, renderer, value, x, Math.round(centerY - size / 2.0f), color, size);
    }

    /** Title variant of {@link #textMid}. */
    public static void titleMid(DrawContext context, TextRenderer renderer, String value,
                                int x, int centerY, int color, float size) {
        title(context, renderer, value, x, Math.round(centerY - size / 2.0f), color, size);
    }

    public static int textWidth(TextRenderer renderer, String value, float size) {
        if (value == null) return 0;
        return (int) Math.ceil(renderer.getWidth(styled(value)) * (Math.max(0.1f, size / FONT_UNIT)));
    }

    /** Truncation is deterministic for a given label/width, so cache the result. */
    private static final Map<String, String> FIT_CACHE = new HashMap<>();
    private static final int FIT_CACHE_LIMIT = 512;

    /** Truncates with an ellipsis using measured width at the requested source size. */
    public static String fit(TextRenderer renderer, String value, int maxWidth, float size) {
        if (value == null || maxWidth <= 0) return "";
        String key = value + '\u0000' + maxWidth + '\u0000' + size;
        String cached = FIT_CACHE.get(key);
        if (cached != null) return cached;

        String result;
        if (textWidth(renderer, value, size) <= maxWidth) {
            result = value;
        } else {
            String ellipsis = "...";
            int limit = maxWidth - textWidth(renderer, ellipsis, size);
            if (limit <= 0) {
                result = ellipsis;
            } else {
                int low = 0;
                int high = value.length();
                while (low < high) {
                    int mid = (low + high + 1) >>> 1;
                    if (textWidth(renderer, value.substring(0, mid), size) <= limit) low = mid;
                    else high = mid - 1;
                }
                result = value.substring(0, low).stripTrailing() + ellipsis;
            }
        }
        if (FIT_CACHE.size() >= FIT_CACHE_LIMIT) FIT_CACHE.clear();
        FIT_CACHE.put(key, result);
        return result;
    }

    public static int alpha(float alpha, int rgb) {
        int a = (int) (Math.max(0.0f, Math.min(1.0f, alpha)) * 255.0f);
        return (a << 24) | (rgb & 0x00FFFFFF);
    }

    /** Byte-alpha overload for source colours that store opacity in 0..255. */
    public static int alpha(int alphaByte, int rgb) {
        return alpha(alphaByte / 255.0f, rgb);
    }

    public static int mix(int from, int to, float t) {
        float f = Math.max(0.0f, Math.min(1.0f, t));
        int a = channel(from >>> 24, to >>> 24, f);
        int r = channel(from >> 16, to >> 16, f);
        int g = channel(from >> 8, to >> 8, f);
        int b = channel(from, to, f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int channel(int from, int to, float t) {
        return (int) (((from & 0xFF) * (1.0f - t)) + ((to & 0xFF) * t));
    }

    public static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                   int color, int radius) {
        if (x2 <= x1 || y2 <= y1) return;
        if (radius <= 0) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        int w = x2 - x1;
        int h = y2 - y1;
        int r = Math.min(radius, Math.min(w, h) / 2);
        context.fill(x1 + r, y1, x2 - r, y2, color);
        context.fill(x1, y1 + r, x2, y2 - r, color);
        for (int dy = 0; dy < r; dy++) {
            double dyFromCenter = r - dy - 0.5;
            int inset = (int) Math.max(0.0,
                r - Math.sqrt(Math.max(0.0, r * r - dyFromCenter * dyFromCenter)));
            context.fill(x1 + inset, y1 + dy, x1 + r, y1 + dy + 1, color);
            context.fill(x2 - r, y1 + dy, x2 - inset, y1 + dy + 1, color);
            context.fill(x1 + inset, y2 - dy - 1, x1 + r, y2 - dy, color);
            context.fill(x2 - r, y2 - dy - 1, x2 - inset, y2 - dy, color);
        }
    }

    /** Vertical wash clipped to the same rounded silhouette as the panel. */
    public static void fillRoundedGradient(DrawContext context, int x, int y, int w, int h,
                                           int topColor, int bottomColor, int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        for (int row = 0; row < h; row++) {
            int inset = 0;
            if (r > 0) {
                double dy;
                if (row < r) {
                    dy = r - row - 0.5;
                } else if (row >= h - r) {
                    dy = row - (h - r) + 0.5;
                } else {
                    dy = -1.0;
                }
                if (dy >= 0.0) {
                    inset = (int) Math.max(0.0,
                        r - Math.sqrt(Math.max(0.0, r * r - dy * dy)));
                }
            }
            float t = h <= 1 ? 0.0f : row / (float) (h - 1);
            context.fill(x + inset, y + row, x + w - inset, y + row + 1,
                mix(topColor, bottomColor, t));
        }
    }

    /** Soft fake shadow, glass base, vertical wash, hairline rim and top glint. */
    public static void panel(DrawContext context, int x, int y, int w, int h,
                             GuiTheme theme, int accent, int radius) {
        panel(context, x, y, w, h, theme.panel, theme, accent, radius, true);
    }

    /** Overload used when the theme module supplies an opacity-adjusted panel colour. */
    public static void panel(DrawContext context, int x, int y, int w, int h,
                             int panelColor, GuiTheme theme, int accent, int radius) {
        panel(context, x, y, w, h, panelColor, theme, accent, radius, true);
    }

    /** Full panel overload; shadows can be disabled by the theme module. */
    public static void panel(DrawContext context, int x, int y, int w, int h,
                             int panelColor, GuiTheme theme, int accent, int radius,
                             boolean shadows) {
        panel(context, x, y, w, h, panelColor, theme, accent, radius, shadows, true);
    }

    /** Full panel overload with outline control from the theme module. */
    public static void panel(DrawContext context, int x, int y, int w, int h,
                             int panelColor, GuiTheme theme, int accent, int radius,
                             boolean shadows, boolean outlines) {
        if (w <= 0 || h <= 0) return;
        int x2 = x + w;
        int y2 = y + h;
        if (shadows) {
            fillRounded(context, x + 2, y + 9, x2 + 2, y2 + 9, alpha(0.29f, 0x000000), radius);
            fillRounded(context, x, y + 2, x2, y2 + 2, alpha(0.19f, 0x000000), radius);
        }
        // Source flat panel body is panelBase at alpha 236 with a subtle
        // white-to-deep wash and a low-alpha rim.
        int baseAlpha = (panelColor >>> 24) & 0xFF;
        if (baseAlpha == 0) baseAlpha = 236;
        baseAlpha = Math.min(236, baseAlpha);
        int base = (baseAlpha << 24) | (panelColor & 0x00FFFFFF);
        fillRounded(context, x, y, x2, y2, base, radius);

        fillRoundedGradient(context, x + 1, y + 1, w - 2, h - 2,
            alpha(0.06f, 0xFFFFFF), alpha(0.13f, theme.titleBar & 0x00FFFFFF),
            Math.max(0, radius - 1));
        if (outlines) {
            int rimAlpha = (theme.panelBorder >>> 24) & 0xFF;
            if (rimAlpha == 0) rimAlpha = 0x38;
            rimAlpha = Math.min(0x38, rimAlpha);
            fillRounded(context, x, y, x2, y2,
                alpha(rimAlpha, theme.panelBorder & 0x00FFFFFF), radius);
        }
        if (w > radius * 2 + 4) {
            context.fill(x + radius, y, x2 - radius, y + 1, alpha(0.14f, 0xFFFFFF));
        }
    }

    /** Rounded pill used for tabs, tool-strip chips and value fields. */
    public static void chip(DrawContext context, int x, int y, int w, int h,
                            GuiTheme theme, int accent, boolean active, boolean hovered,
                            int radius) {
        if (w <= 0 || h <= 0) return;
        int x2 = x + w;
        int y2 = y + h;
        int r = Math.min(radius, h / 2);
        fillRounded(context, x, y, x2, y2, alpha(0.35f, theme.panel & 0x00FFFFFF), r);
        if (active) {
            fillRounded(context, x, y, x2, y2, alpha(0.20f, accent), r);
            fillRounded(context, x, y, x2, y2, alpha(0.62f, accent), r);
        } else if (hovered) {
            fillRounded(context, x, y, x2, y2, alpha(0.10f, 0xFFFFFF), r);
        }
    }

    public static void toggle(DrawContext context, int x, int y, int w, int h,
                              GuiTheme theme, int accent, boolean enabled, boolean hovered) {
        if (w <= 0 || h <= 0) return;
        int r = h / 2;
        if (enabled) {
            fillRounded(context, x - 2, y - 2, x + w + 2, y + h + 2,
                alpha(hovered ? 0.28f : 0.20f, accent), r + 2);
        }
        fillRounded(context, x, y, x + w, y + h,
            alpha(enabled ? 0.92f : 0.68f,
                enabled ? accent & 0x00FFFFFF : theme.toggleOff & 0x00FFFFFF), r);
        fillRounded(context, x + 1, y + 1, x + w - 1, y + h - 1,
            alpha(enabled ? 0.14f : 0.05f, 0xFFFFFF), Math.max(0, r - 1));
        strokeRounded(context, x, y, w, h,
            alpha(enabled ? 0.62f : 0.34f, enabled ? accent : theme.panelBorder & 0x00FFFFFF), r, 1);
        int knob = h - 4;
        int kx = enabled ? x + w - knob - 2 : x + 2;
        fillRounded(context, kx + 1, y + 3, kx + knob + 1, y + 3 + knob,
            alpha(0.30f, 0x000000), knob / 2);
        fillRounded(context, kx, y + 2, kx + knob, y + 2 + knob,
            alpha(0.96f, 0xFFFFFF), knob / 2);
        fillRounded(context, kx + 1, y + 3, kx + knob - 1, y + 3 + Math.max(2, knob / 2),
            alpha(0.55f, 0xFFFFFF), Math.max(1, knob / 2 - 1));
    }

    /** One-pixel rounded rim. Kept separate from fills so translucent panels stay clean. */
    public static void strokeRounded(DrawContext context, int x, int y, int w, int h,
                                     int color, int radius, int thickness) {
        if (w <= 0 || h <= 0 || thickness <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        int t = Math.max(1, Math.min(thickness, Math.min(w, h) / 2));
        if (r <= 0) {
            context.fill(x, y, x + t, y + h, color);
            context.fill(x + w - t, y, x + w, y + h, color);
            context.fill(x, y, x + w, y + t, color);
            context.fill(x, y + h - t, x + w, y + h, color);
            return;
        }
        for (int row = 0; row < r; row++) {
            double dy = r - row - 0.5;
            int inset = (int) Math.max(0.0,
                r - Math.sqrt(Math.max(0.0, r * r - dy * dy)));
            context.fill(x + inset, y + row, x + w - inset, y + row + 1, color);
            context.fill(x + inset, y + h - row - 1, x + w - inset, y + h - row, color);
            for (int inner = 1; inner < t; inner++) {
                int innerInset = Math.max(0, inset - inner);
                context.fill(x + innerInset, y + row + inner, x + w - innerInset,
                    y + row + inner + 1, color);
                context.fill(x + innerInset, y + h - row - inner - 1, x + w - innerInset,
                    y + h - row - inner, color);
            }
        }
        context.fill(x, y + r, x + t, y + h - r, color);
        context.fill(x + w - t, y + r, x + w, y + h - r, color);
    }

    /** Shared pill button used by tabs, icon controls and compact value controls. */
    public static void button(DrawContext context, int x, int y, int w, int h,
                              GuiTheme theme, int accent, boolean active, boolean hovered,
                              int radius, boolean outlined) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, h / 2);
        fillRounded(context, x + 1, y + 2, x + w + 1, y + h + 2, alpha(0.24f, 0x000000), r);
        fillRounded(context, x, y, x + w, y + h,
            alpha(active ? 0.50f : hovered ? 0.42f : 0.35f, theme.panel & 0x00FFFFFF), r);
        fillRoundedGradient(context, x + 1, y + 1, w - 2, h - 2,
            alpha(active ? 0.31f : hovered ? 0.08f : 0.04f, accent & 0x00FFFFFF),
            alpha(active ? 0.19f : hovered ? 0.02f : 0.01f, 0x000000), Math.max(0, r - 1));
        if (outlined) {
            strokeRounded(context, x, y, w, h,
                alpha(active ? 0.69f : hovered ? 0.22f : 0.12f,
                    active ? accent & 0x00FFFFFF : 0xFFFFFF), r, 1);
        }
        if (active) {
            fillRounded(context, x + 2, y + h - 2, x + w - 2, y + h - 1,
                alpha(0.69f, accent), Math.max(1, r - 2));
        } else if (hovered) {
            fillRounded(context, x + 2, y + 1, x + w - 2, y + 2,
                alpha(0.20f, 0xFFFFFF), Math.max(1, r - 2));
        }
    }

    /** Recessed search/value field. */
    public static void field(DrawContext context, int x, int y, int w, int h,
                             GuiTheme theme, int accent, boolean focused, boolean hovered,
                             int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, h / 2);
        fillRounded(context, x, y + 1, x + w, y + h + 1, alpha(0.25f, 0x000000), r);
        fillRounded(context, x, y, x + w, y + h,
            alpha(focused ? 0.50f : hovered ? 0.42f : 0.35f, theme.searchBg & 0x00FFFFFF), r);
        fillRoundedGradient(context, x + 1, y + 1, w - 2, h - 2,
            alpha(focused ? 0.16f : 0.04f, accent & 0x00FFFFFF),
            alpha(0.06f, 0x000000), Math.max(0, r - 1));
        strokeRounded(context, x, y, w, h,
            alpha(focused ? 0.69f : hovered ? 0.22f : 0.12f,
                focused ? accent & 0x00FFFFFF : 0xFFFFFF), r, 1);
    }

    /** Source backdrop gradient with explicit byte alphas (64 top, 134 bottom). */
    public static void overlayWashBytes(DrawContext context, int w, int h, int color,
                                        int topAlpha, int bottomAlpha) {
        if (w <= 0 || h <= 0) return;
        int stored = (color >>> 24) & 0xFF;
        float strength = stored == 0 ? 1.0f : stored / 255.0f;
        int rgb = color & 0x00FFFFFF;
        int top = (int) Math.min(255, topAlpha * strength);
        int bottom = (int) Math.min(255, bottomAlpha * strength);
        int bands = Math.max(1, h / 24);
        for (int i = 0; i < bands; i++) {
            int y1 = (h * i) / bands;
            int y2 = (h * (i + 1)) / bands;
            float t = i / (float) Math.max(1, bands - 1);
            context.fill(0, y1, w, y2, mix(alpha(top, rgb), alpha(bottom, rgb), t));
        }
    }

    /** Source-style translucent top-to-bottom wash, shared by both shells. */
    public static void overlayWash(DrawContext context, int w, int h, int color,
                                   float topAlpha, float bottomAlpha) {
        if (w <= 0 || h <= 0) return;
        int storedAlpha = (color >>> 24) & 0xFF;
        float strength = storedAlpha == 0 ? 1.0f : storedAlpha / 255.0f;
        int bands = Math.max(1, h / 24);
        for (int i = 0; i < bands; i++) {
            int y1 = (h * i) / bands;
            int y2 = (h * (i + 1)) / bands;
            float t = i / (float) Math.max(1, bands - 1);
            context.fill(0, y1, w, y2, mix(alpha(topAlpha * strength, color & 0x00FFFFFF),
                alpha(bottomAlpha * strength, color & 0x00FFFFFF), t));
        }
    }

    /** Source-style module row: transparent base, accent wash, rim and left bar. */
    public static void moduleRow(DrawContext context, int x, int y, int w, int h,
                                 GuiTheme theme, int accent, boolean enabled,
                                 boolean selected, boolean hovered, float hoverAlpha,
                                 int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, h / 2);
        fillRounded(context, x, y, x + w, y + h,
            alpha(0.06f, theme.settingRow & 0x00FFFFFF), r);
        if (hovered) {
            fillRounded(context, x, y + 1, x + w, y + h - 1,
                alpha(0.07f * Math.max(0.25f, hoverAlpha), 0xFFFFFF), r);
        }
        if (enabled || selected) {
            fillRoundedGradient(context, x, y + 1, w, h - 2,
                alpha(0.19f, accent & 0x00FFFFFF), alpha(0.05f, accent & 0x00FFFFFF), r);
            strokeRounded(context, x, y, w, h, alpha(0.23f, accent), r, 1);
            fillRounded(context, x + 1, y + 3, x + 4, y + h - 6,
                alpha(0.30f, accent), 2);
        }
        fillRounded(context, x, y + 4, x + 3, y + h - 4,
            alpha(0.88f, enabled || selected
                ? accent & 0x00FFFFFF : theme.textSecondary & 0x00FFFFFF), 1);
    }

    /** Thin rounded scrollbar with the source's low track/high thumb opacity. */
    public static void scrollbar(DrawContext context, int x, int y, int h,
                                 float maxScroll, float scroll, GuiTheme theme, int accent,
                                 int width) {
        if (maxScroll <= 0 || h <= 0 || width <= 0) return;
        int r = Math.max(1, width / 2);
        fillRounded(context, x, y, x + width, y + h,
            alpha(0.07f, theme.scrollTrack & 0x00FFFFFF), r);
        int thumbH = Math.max(24, h * h / (h + (int) maxScroll));
        int thumbY = y + (int) ((h - thumbH) * (scroll / maxScroll));
        fillRounded(context, x, thumbY, x + width, thumbY + thumbH,
            alpha(0.44f, accent), r);
        fillRounded(context, x + 1, thumbY + 1, x + width - 1, thumbY + thumbH - 1,
            alpha(0.16f, 0xFFFFFF), Math.max(1, r - 1));
    }

    /** Rounded checklist square with a small check glyph. */
    public static void checkBox(DrawContext context, int x, int y, int size,
                                GuiTheme theme, int accent, boolean selected, boolean hovered) {
        if (size <= 0) return;
        int r = Math.max(2, size / 4);
        fillRounded(context, x, y, x + size, y + size,
            alpha(selected ? 0.88f : hovered ? 0.70f : 0.56f,
                selected ? accent & 0x00FFFFFF : theme.settingRow & 0x00FFFFFF), r);
        strokeRounded(context, x, y, size, size,
            alpha(selected ? 0.70f : 0.34f, selected ? accent : theme.panelBorder & 0x00FFFFFF), r, 1);
        if (!selected) return;
        int cx = x + size / 2;
        int cy = y + size / 2;
        fillRounded(context, cx - 4, cy - 1, cx - 1, cy + 2, 0xFFFFFFFF, 1);
        fillRounded(context, cx - 1, cy + 1, cx + 4, cy + 4, 0xFFFFFFFF, 1);
    }

    /**
     * Single source-style watermark. Returns its width so callers can position
     * neighbouring controls without re-measuring the wordmark.
     */
    public static int watermark(DrawContext context, TextRenderer renderer,
                                int x, int y, int h, GuiTheme theme, float progress) {
        if (h <= 0) return 0;
        String wordmark = "Flux";
        int icon = Math.min(22, Math.max(12, h - 16));
        int wordW = textWidth(renderer, wordmark, 14.0f);
        int w = 46 + wordW + 18;
        panel(context, x, y, w, h, theme, theme.accent, h / 2);

        int iconX = x + 14;
        int iconY = y + Math.max(4, (h - icon) / 2);
        int gap = Math.max(1, icon / 8);
        int tile = (icon - gap * 3) / 2;
        fillRounded(context, iconX, iconY, iconX + icon, iconY + icon,
            alpha(0.20f, theme.accent & 0x00FFFFFF), icon / 2);
        fillRounded(context, iconX + gap, iconY + gap, iconX + gap + tile, iconY + gap + tile,
            theme.titleAccent, 2);
        fillRounded(context, iconX + gap * 2 + tile, iconY + gap,
            iconX + gap * 2 + tile * 2, iconY + gap + tile, theme.accent, 2);
        fillRounded(context, iconX + gap, iconY + gap * 2 + tile,
            iconX + gap + tile, iconY + gap * 2 + tile * 2, theme.accent, 2);
        fillRounded(context, iconX + gap * 2 + tile, iconY + gap * 2 + tile,
            iconX + gap * 2 + tile * 2, iconY + gap * 2 + tile * 2, theme.titleAccent, 2);

        text(context, renderer, wordmark, x + 46, y + (h - 14) / 2 + 1, theme.text, 14.0f);
        fillRounded(context, x + 2, y + h - 2, x + 12, y + h,
            alpha((int) (200 * Math.max(0.0f, Math.min(1.0f, progress))), theme.accent), 1);
        return w;
    }
}
