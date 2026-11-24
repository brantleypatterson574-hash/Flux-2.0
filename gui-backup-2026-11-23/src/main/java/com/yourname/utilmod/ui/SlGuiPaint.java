package com.yourname.utilmod.ui;

import net.minecraft.client.MinecraftClient;
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

    /** Native height of the bundled TTF provider; label sizes are expressed in these units. */
    public static final float FONT_UNIT = 11.0f;

    /** Palette sampled from Ochard 1.21.11. Used as shared accents, not copied code. */
    public static final int OCHARD_SHELL = 0xFF1C1E24;
    public static final int OCHARD_PANEL = 0xE60F1117;
    public static final int OCHARD_PANEL_DEEP = 0xF0090A10;
    public static final int OCHARD_TEXT = 0xFFFDF5FF;
    public static final int OCHARD_TEXT_MUTED = 0xFFB5ADCA;
    public static final int OCHARD_TEXT_SECONDARY = 0xFFB9BCC2;
    public static final int OCHARD_ACCENT = 0xFFCF00FF;
    public static final int OCHARD_ACCENT_BLUE = 0xFF78B4FF;
    public static final int OCHARD_STATUS_BAD = 0xFFFF4040;
    public static final int OCHARD_STATUS_GOOD = 0xFF50DC78;

    /** Bounded cache: menu labels repeat every frame, so avoid rebuilding Text. */
    private static final Map<String, Text> STYLED_CACHE = new HashMap<>();
    private static final int STYLED_CACHE_LIMIT = 512;
    /** Single glyphs dominate tracked-text drawing; keep them allocation-free. */
    private static final Text[] ASCII_GLYPHS = new Text[128];

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

    /** Cached single-character variant used by letter-spaced labels. */
    public static Text styled(char value) {
        if (value < 128) {
            Text cached = ASCII_GLYPHS[value];
            if (cached == null) {
                cached = styled(String.valueOf(value));
                ASCII_GLYPHS[value] = cached;
            }
            return cached;
        }
        return styled(String.valueOf(value));
    }

    /** Cached advance widths; the menu uses a fixed set of labels and sizes. */
    private static final Map<String, Integer> WIDTH_CACHE = new HashMap<>();
    private static final int WIDTH_CACHE_LIMIT = 1024;

    /**
     * Returns the extra matrix scale needed to draw the menu at mockup size.
     *
     * <p>Minecraft's DrawContext is already scaled by the video GUI scale, so a
     * 980px shell drawn at 3x GUI scale becomes 2940 physical pixels. Dividing by
     * the window scale factor cancels that out and makes the shell land at a
     * true 980x600 physical pixels, matching menu-mockup.html at 1x. Smaller
     * windows shrink it; Menu Scale can only shrink it further, never enlarge
     * past 1:1.
     */
    public static float responsiveScale(float screenHeight, float userScale) {
        return responsiveScale(screenHeight, userScale, 980.0f, 600.0f);
    }

    /**
     * Same cancellation/fit behavior as {@link #responsiveScale(float, float)},
     * but for a caller-supplied virtual design size. The ScalableLux shell uses
     * the full 1400px panel plus the 460px inspector and 14px gap.
     */
    public static float responsiveScale(float screenHeight, float userScale,
                                        float designWidth, float designHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        double mcScale = 1.0;
        double physicalWidth = screenHeight;
        double physicalHeight = screenHeight;
        if (client != null && client.getWindow() != null) {
            mcScale = Math.max(1, client.getWindow().getScaleFactor());
            physicalWidth = client.getWindow().getWidth();
            physicalHeight = client.getWindow().getHeight();
        }

        double fit = Math.min(1.0 / mcScale,
            Math.min(Math.max(0.2, (physicalWidth - 28.0) / Math.max(1.0, designWidth)),
                     Math.max(0.2, (physicalHeight - 28.0) / Math.max(1.0, designHeight))));
        fit *= Math.max(0.5, Math.min(1.0, userScale));
        return (float) Math.max(0.25, fit);
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

    public static int textWidth(TextRenderer renderer, String value, float size) {
        if (value == null) return 0;
        String key = value + '\u0000' + size;
        Integer cached = WIDTH_CACHE.get(key);
        if (cached != null) return cached;
        int width = (int) Math.ceil(
            renderer.getWidth(styled(value)) * (Math.max(0.1f, size / FONT_UNIT)));
        if (WIDTH_CACHE.size() >= WIDTH_CACHE_LIMIT) WIDTH_CACHE.clear();
        WIDTH_CACHE.put(key, width);
        return width;
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

    /** Ochard's red-to-green status ramp, reduced to a two-stop linear blend. */
    public static int statusColor(float value) {
        return mix(OCHARD_STATUS_BAD, OCHARD_STATUS_GOOD, value);
    }

    public static void fillRounded(DrawContext context, int x1, int y1, int x2, int y2,
                                   int color, int radius) {
        if (x2 <= x1 || y2 <= y1) return;
        int w = x2 - x1;
        int h = y2 - y1;
        int r = Math.min(radius, Math.min(w, h) / 2);
        if (r <= 0) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        // Non-overlapping bands keep translucent fills even across the whole pill.
        context.fill(x1 + r, y1, x2 - r, y2, color);
        context.fill(x1, y1 + r, x1 + r, y2 - r, color);
        context.fill(x2 - r, y1 + r, x2, y2 - r, color);

        int alpha = color >>> 24;
        int rgb = color & 0x00FFFFFF;
        boolean smooth = alpha >= 40;
        for (int dy = 0; dy < r; dy++) {
            double dyFromCenter = r - dy - 0.5;
            double edge = r - Math.sqrt(Math.max(0.0, r * r - dyFromCenter * dyFromCenter));
            int inset = (int) Math.floor(edge);
            if (inset < r) {
                context.fill(x1 + inset, y1 + dy, x1 + r, y1 + dy + 1, color);
                context.fill(x2 - r, y1 + dy, x2 - inset, y1 + dy + 1, color);
                context.fill(x1 + inset, y2 - dy - 1, x1 + r, y2 - dy, color);
                context.fill(x2 - r, y2 - dy - 1, x2 - inset, y2 - dy, color);
            }
            if (smooth && inset > 0) {
                double cover = inset - edge;
                if (cover > 0.04) {
                    int edgeColor = (Math.round(alpha * (float) cover) << 24) | rgb;
                    context.fill(x1 + inset - 1, y1 + dy, x1 + inset, y1 + dy + 1, edgeColor);
                    context.fill(x2 - inset, y1 + dy, x2 - inset + 1, y1 + dy + 1, edgeColor);
                    context.fill(x1 + inset - 1, y2 - dy - 1, x1 + inset, y2 - dy, edgeColor);
                    context.fill(x2 - inset, y2 - dy - 1, x2 - inset + 1, y2 - dy, edgeColor);
                }
            }
        }
    }

    /** Flat fill with only the top corners clipped, for headers inside a rounded shell. */
    public static void fillRoundedTopOnly(DrawContext context, int x, int y, int w, int h,
                                          int color, int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        if (r <= 0) {
            context.fill(x, y, x + w, y + h, color);
            return;
        }
        context.fill(x, y + r, x + w, y + h, color);
        for (int dy = 0; dy < r; dy++) {
            double dyFromCenter = r - dy - 0.5;
            int inset = (int) Math.max(0.0,
                r - Math.sqrt(Math.max(0.0, r * r - dyFromCenter * dyFromCenter)));
            context.fill(x + inset, y + dy, x + w - inset, y + dy + 1, color);
        }
    }

    /** Vertical wash clipped to the same rounded silhouette as the panel. */
    public static void fillRoundedGradient(DrawContext context, int x, int y, int w, int h,
                                           int topColor, int bottomColor, int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        RowGradient gradient = rowGradient(h, r, topColor, bottomColor);
        int runStart = 0;
        int runInset = gradient.insets[0];
        int runColor = gradient.colors[0];
        for (int row = 1; row < h; row++) {
            int inset = gradient.insets[row];
            int color = gradient.colors[row];
            if (inset == runInset && color == runColor) continue;
            context.fill(x + runInset, y + runStart, x + w - runInset, y + row, runColor);
            runStart = row;
            runInset = inset;
            runColor = color;
        }
        context.fill(x + runInset, y + runStart, x + w - runInset, y + h, runColor);
    }

    private static final class RowGradient {
        final int[] insets;
        final int[] colors;
        RowGradient(int[] insets, int[] colors) {
            this.insets = insets;
            this.colors = colors;
        }
    }

    private static final Map<String, RowGradient> ROW_GRADIENTS = new HashMap<>();

    private static RowGradient rowGradient(int h, int r, int topColor, int bottomColor) {
        String key = h + ":" + r + ":" + topColor + ":" + bottomColor;
        RowGradient cached = ROW_GRADIENTS.get(key);
        if (cached != null) return cached;
        int[] insets = new int[h];
        int[] colors = new int[h];
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
            insets[row] = inset;
            colors[row] = mix(topColor, bottomColor, t);
        }
        if (ROW_GRADIENTS.size() >= GRADIENT_CACHE_LIMIT) ROW_GRADIENTS.clear();
        cached = new RowGradient(insets, colors);
        ROW_GRADIENTS.put(key, cached);
        return cached;
    }

    /** Horizontal (90deg) gradient clipped to a rounded silhouette. */
    public static void fillHorizontalRoundedGradient(DrawContext context, int x, int y,
                                                     int w, int h, int leftColor, int rightColor,
                                                     int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        ColumnGradient gradient = columnGradient(w, r, leftColor, rightColor);
        int runStart = 0;
        int runInset = gradient.insets[0];
        int runColor = gradient.colors[0];
        for (int col = 1; col < w; col++) {
            int inset = gradient.insets[col];
            int color = gradient.colors[col];
            if (inset == runInset && color == runColor) continue;
            context.fill(x + runStart, y + runInset, x + col, y + h - runInset, runColor);
            runStart = col;
            runInset = inset;
            runColor = color;
        }
        context.fill(x + runStart, y + runInset, x + w, y + h - runInset, runColor);
    }

    /** Horizontal gradient with square bottom corners, matching a clipped titlebar. */
    public static void fillHorizontalRoundedGradientTopOnly(DrawContext context, int x, int y,
                                                            int w, int h, int leftColor,
                                                            int rightColor, int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        ColumnGradient gradient = columnGradient(w, r, leftColor, rightColor);
        int runStart = 0;
        int runInset = gradient.insets[0];
        int runColor = gradient.colors[0];
        for (int col = 1; col < w; col++) {
            int inset = gradient.insets[col];
            int color = gradient.colors[col];
            if (inset == runInset && color == runColor) continue;
            context.fill(x + runStart, y + runInset, x + col, y + h, runColor);
            runStart = col;
            runInset = inset;
            runColor = color;
        }
        context.fill(x + runStart, y + runInset, x + w, y + h, runColor);
    }

    private static final class ColumnGradient {
        final int[] insets;
        final int[] colors;
        ColumnGradient(int[] insets, int[] colors) {
            this.insets = insets;
            this.colors = colors;
        }
    }

    private static final Map<String, ColumnGradient> COLUMN_GRADIENTS = new HashMap<>();
    private static final int GRADIENT_CACHE_LIMIT = 32;

    private static ColumnGradient columnGradient(int w, int r, int leftColor, int rightColor) {
        String key = w + ":" + r + ":" + leftColor + ":" + rightColor;
        ColumnGradient cached = COLUMN_GRADIENTS.get(key);
        if (cached != null) return cached;
        int[] insets = new int[w];
        int[] colors = new int[w];
        for (int col = 0; col < w; col++) {
            int inset = 0;
            if (r > 0) {
                double dy;
                if (col < r) {
                    dy = r - col - 0.5;
                } else if (col >= w - r) {
                    dy = col - (w - r) + 0.5;
                } else {
                    dy = -1.0;
                }
                if (dy >= 0.0) {
                    inset = (int) Math.max(0.0,
                        r - Math.sqrt(Math.max(0.0, r * r - dy * dy)));
                }
            }
            float t = w <= 1 ? 0.0f : col / (float) (w - 1);
            insets[col] = inset;
            colors[col] = mix(leftColor, rightColor, t);
        }
        if (COLUMN_GRADIENTS.size() >= GRADIENT_CACHE_LIMIT) COLUMN_GRADIENTS.clear();
        cached = new ColumnGradient(insets, colors);
        COLUMN_GRADIENTS.put(key, cached);
        return cached;
    }

    /** 135deg gradient: top-left to bottom-right, clipped to a rounded silhouette. */
    public static void fillDiagonalRoundedGradient(DrawContext context, int x, int y,
                                                   int w, int h, int fromColor, int toColor,
                                                   int radius) {
        if (w <= 0 || h <= 0) return;
        int r = Math.min(radius, Math.min(w, h) / 2);
        int[] colors = diagonalColors(w, h, r, fromColor, toColor);
        for (int py = 0; py < h; py++) {
            int row = py * w;
            int px = 0;
            while (px < w) {
                int color = colors[row + px];
                if (color == OUTSIDE_GRADIENT) {
                    px++;
                    continue;
                }
                int runStart = px;
                px++;
                while (px < w && colors[row + px] == color) px++;
                context.fill(x + runStart, y + py, x + px, y + py + 1, color);
            }
        }
    }

    private static final int OUTSIDE_GRADIENT = Integer.MIN_VALUE;
    private static final Map<String, int[]> DIAGONAL_GRADIENTS = new HashMap<>();

    private static int[] diagonalColors(int w, int h, int r, int fromColor, int toColor) {
        String key = w + ":" + h + ":" + r + ":" + fromColor + ":" + toColor;
        int[] cached = DIAGONAL_GRADIENTS.get(key);
        if (cached != null) return cached;
        int[] colors = new int[w * h];
        float divisor = Math.max(1, w + h - 2);
        for (int py = 0; py < h; py++) {
            for (int px = 0; px < w; px++) {
                int index = py * w + px;
                if (!insideRounded(px, py, w, h, r)) {
                    colors[index] = OUTSIDE_GRADIENT;
                    continue;
                }
                float t = (px + py) / divisor;
                colors[index] = mix(fromColor, toColor, t);
            }
        }
        if (DIAGONAL_GRADIENTS.size() >= GRADIENT_CACHE_LIMIT) DIAGONAL_GRADIENTS.clear();
        DIAGONAL_GRADIENTS.put(key, colors);
        return colors;
    }

    private static boolean insideRounded(int px, int py, int w, int h, int r) {
        if (px < 0 || py < 0 || px >= w || py >= h || r <= 0) {
            return px >= 0 && py >= 0 && px < w && py < h;
        }
        int cx;
        int cy;
        boolean left = px < r;
        boolean right = px >= w - r;
        boolean top = py < r;
        boolean bottom = py >= h - r;
        if (!left && !right && !top && !bottom) return true;
        if (left && top) {
            cx = r;
            cy = r;
        } else if (right && top) {
            cx = w - r - 1;
            cy = r;
        } else if (left && bottom) {
            cx = r;
            cy = h - r - 1;
        } else if (right && bottom) {
            cx = w - r - 1;
            cy = h - r - 1;
        } else {
            return true;
        }
        int dx = px - cx;
        int dy = py - cy;
        return dx * dx + dy * dy <= r * r;
    }

    /**
     * Draws text with CSS-style letter spacing. The mockup uses tracking on the
     * rail, status badges and advanced header, so plain drawText cannot match it.
     */
    public static void textTracked(DrawContext context, TextRenderer renderer, String value,
                                   int x, int y, int color, float size, float tracking) {
        if (value == null || value.isEmpty()) return;
        float factor = Math.max(0.1f, size / FONT_UNIT);
        float spacing = tracking / factor;
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(factor, factor);
        float cursor = 0.0f;
        for (int i = 0; i < value.length(); i++) {
            Text glyph = styled(value.charAt(i));
            context.drawText(renderer, glyph, Math.round(cursor), 0, color, false);
            cursor += renderer.getWidth(glyph) + spacing;
        }
        context.getMatrices().popMatrix();
    }

    /** Measured width for {@link #textTracked}. */
    public static int textWidthTracked(TextRenderer renderer, String value,
                                       float size, float tracking) {
        if (value == null || value.isEmpty()) return 0;
        float factor = Math.max(0.1f, size / FONT_UNIT);
        float width = 0.0f;
        for (int i = 0; i < value.length(); i++) {
            width += renderer.getWidth(styled(value.charAt(i)));
        }
        return (int) Math.ceil(width * factor + tracking * (value.length() - 1));
    }

    /** Tracked truncation is deterministic too, so cache it like {@link #fit}. */
    private static final Map<String, String> FIT_TRACKED_CACHE = new HashMap<>();
    private static final int FIT_TRACKED_CACHE_LIMIT = 512;

    /** Tracked variant of {@link #fit}. */
    public static String fitTracked(TextRenderer renderer, String value, int maxWidth,
                                    float size, float tracking) {
        if (value == null || maxWidth <= 0) return "";
        String key = value + '\u0000' + maxWidth + '\u0000' + size + '\u0000' + tracking;
        String cached = FIT_TRACKED_CACHE.get(key);
        if (cached != null) return cached;
        String result;
        if (textWidthTracked(renderer, value, size, tracking) <= maxWidth) result = value;
        else result = fitTrackedUncached(renderer, value, maxWidth, size, tracking);
        if (FIT_TRACKED_CACHE.size() >= FIT_TRACKED_CACHE_LIMIT) FIT_TRACKED_CACHE.clear();
        FIT_TRACKED_CACHE.put(key, result);
        return result;
    }

    private static String fitTrackedUncached(TextRenderer renderer, String value, int maxWidth,
                                             float size, float tracking) {
        if (textWidthTracked(renderer, value, size, tracking) <= maxWidth) return value;
        String ellipsis = "...";
        int limit = maxWidth - textWidthTracked(renderer, ellipsis, size, tracking);
        if (limit <= 0) return ellipsis;
        int low = 0;
        int high = value.length();
        while (low < high) {
            int mid = (low + high + 1) >>> 1;
            if (textWidthTracked(renderer, value.substring(0, mid), size, tracking) <= limit) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return value.substring(0, low).stripTrailing() + ellipsis;
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
