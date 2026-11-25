package com.yourname.utilmod.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

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

    /** Single glyphs dominate tracked-text drawing; keep them allocation-free. */
    private static final Text[] ASCII_GLYPHS = new Text[128];

    /** Wraps a string with Minecraft's default font for menu rendering. */
    public static Text styled(String value) {
        return styled(value, 11.0f);
    }

    /** Wraps a string in the default font; size is applied by MenuFonts.draw. */
    public static Text styled(String value, float size) {
        return MenuFonts.text(value, MenuFonts.tier(size));
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

    /**
     * Cancels Minecraft's GUI scale so one menu unit is exactly one physical
     * pixel. Fractional fit scaling was the reason every glyph and icon looked
     * pixelated on 1280x720 / 1366x768 screens; Menu Scale now resizes the
     * window instead of resampling the whole render.
     */
    public static float nativeScale() {
        MinecraftClient client = MinecraftClient.getInstance();
        double mcScale = 1.0;
        if (client != null && client.getWindow() != null) {
            mcScale = Math.max(1, client.getWindow().getScaleFactor());
        }
        return (float) (1.0 / mcScale);
    }

    /** Kept for existing callers; scale is always the crisp native 1:1 value. */
    public static float responsiveScale(float screenHeight, float userScale) {
        return nativeScale();
    }

    /** Kept for existing callers; design dimensions no longer resample the UI. */
    public static float responsiveScale(float screenHeight, float userScale,
                                        float designWidth, float designHeight) {
        return nativeScale();
    }

    /** Draws Minecraft's default font at the requested menu label size. */
    public static void text(DrawContext context, TextRenderer renderer, String value,
                            int x, int y, int color, float size) {
        if (value == null) return;
        MenuFonts.draw(context, renderer, value, x, y, color, MenuFonts.tier(size));
    }

    /** Compact default-size variant used by tool-strips and glyph buttons. */
    public static void text(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color) {
        text(context, renderer, value, x, y, color, FONT_UNIT);
    }

    /** Soft, non-shadowed title text using the same vanilla font as labels. */
    public static void title(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color) {
        title(context, renderer, value, x, y, color, FONT_UNIT);
    }

    /** Heading variant using the same vanilla font tiers. */
    public static void title(DrawContext context, TextRenderer renderer, String value,
                             int x, int y, int color, float size) {
        if (value == null) return;
        MenuFonts.draw(context, renderer, value, x, y, color, MenuFonts.tier(size));
    }

    /** Lightens a theme colour toward white for the soft title style. */
    public static int titleColor(int color) {
        return mix(color & 0x00FFFFFF, 0xFFFFFF, 0.24f) | 0xFF000000;
    }

    /** Draws Minecraft's default font vertically centred on {@code centerY}. */
    public static void textMid(DrawContext context, TextRenderer renderer, String value,
                               int x, int centerY, int color, float size) {
        MenuFonts.draw(context, renderer, value, x, Math.round(centerY - size / 2.0f),
            color, MenuFonts.tier(size));
    }

    /** Heading variant of {@link #textMid}, sharing the same fixed tiers. */
    public static void titleMid(DrawContext context, TextRenderer renderer, String value,
                                int x, int centerY, int color, float size) {
        textMid(context, renderer, value, x, centerY, color, size);
    }

    public static int textWidth(TextRenderer renderer, String value, float size) {
        if (value == null) return 0;
        return MenuFonts.width(renderer, value, MenuFonts.tier(size));
    }

    /** Typed cache keys avoid NUL separators in string identifiers. */
    private record FitKey(String value, int maxWidth, float size) {}

    /** Truncation is deterministic for a given label/width, so cache the result. */
    private static final Map<FitKey, String> FIT_CACHE = new HashMap<>();
    private static final int FIT_CACHE_LIMIT = 512;

    /** UTF-16 substring boundaries that never split a surrogate pair. */
    private static int[] codePointBounds(String value) {
        int count = value.codePointCount(0, value.length());
        int[] cuts = new int[count + 1];
        int offset = 0;
        for (int i = 0; i < count; i++) {
            cuts[i] = offset;
            offset = value.offsetByCodePoints(offset, 1);
        }
        cuts[count] = value.length();
        return cuts;
    }

    /** Truncates with an ellipsis using measured width at the requested source size. */
    public static String fit(TextRenderer renderer, String value, int maxWidth, float size) {
        if (value == null || maxWidth <= 0) return "";
        FitKey key = new FitKey(value, maxWidth, size);
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
                int[] cuts = codePointBounds(value);
                int low = 0;
                int high = cuts.length - 1;
                while (low < high) {
                    int mid = (low + high + 1) >>> 1;
                    if (textWidth(renderer, value.substring(0, cuts[mid]), size) <= limit) low = mid;
                    else high = mid - 1;
                }
                result = value.substring(0, cuts[low]).stripTrailing() + ellipsis;
            }
        }
        if (FIT_CACHE.size() >= FIT_CACHE_LIMIT) FIT_CACHE.clear();
        FIT_CACHE.put(key, result);
        return result;
    }

    /**
     * Clears every text cache that holds pre-reload glyph widths or styles.
     * The single-glyph cache matters too: those Text objects keep the old font
     * style even after MenuFonts swaps tiers.
     */
    public static void invalidateTextCaches() {
        for (int i = 0; i < ASCII_GLYPHS.length; i++) ASCII_GLYPHS[i] = null;
        FIT_CACHE.clear();
        FIT_TRACKED_CACHE.clear();
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

    /**
     * Quarter-arc span table. Radii repeat constantly across panels, chips and
     * toasts, so the sqrt/coverage pass happens once per radius instead of once
     * per widget per frame. The two rounding modes are kept separate: fills use
     * ceil (with fractional coverage for the boundary pixel) while strokes use
     * the old truncating inset, so every pixel stays identical.
     */
    private record ArcKey(int radius) {}

    private static final class ArcSpans {
        final int[] solid;
        final double[] cover;
        final int[] strokeInset;

        ArcSpans(int radius) {
            solid = new int[radius];
            cover = new double[radius];
            strokeInset = new int[radius];
            for (int dy = 0; dy < radius; dy++) {
                double dyFromCenter = radius - dy - 0.5;
                double inside = Math.max(0.0, (double) radius * radius - dyFromCenter * dyFromCenter);
                double edge = radius - Math.sqrt(inside);
                int firstSolid = (int) Math.ceil(edge);
                solid[dy] = firstSolid;
                cover[dy] = firstSolid - edge;
                strokeInset[dy] = (int) Math.max(0.0, radius - Math.sqrt(inside));
            }
        }
    }

    private static final Map<ArcKey, ArcSpans> ARC_SPANS = new HashMap<>();
    private static final int ARC_CACHE_LIMIT = 64;

    private static ArcSpans arcSpans(int radius) {
        ArcKey key = new ArcKey(radius);
        ArcSpans cached = ARC_SPANS.get(key);
        if (cached != null) return cached;
        if (ARC_SPANS.size() >= ARC_CACHE_LIMIT) ARC_SPANS.clear();
        cached = new ArcSpans(radius);
        ARC_SPANS.put(key, cached);
        return cached;
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
        ArcSpans spans = arcSpans(r);
        for (int dy = 0; dy < r; dy++) {
            // The circle boundary is at x1 + edge. Fill from the first column
            // wholly inside the arc through the start of the straight side band.
            int solid = spans.solid[dy];
            if (solid < r) {
                context.fill(x1 + solid, y1 + dy, x1 + r, y1 + dy + 1, color);
                context.fill(x2 - r, y1 + dy, x2 - solid, y1 + dy + 1, color);
                context.fill(x1 + solid, y2 - dy - 1, x1 + r, y2 - dy, color);
                context.fill(x2 - r, y2 - dy - 1, x2 - solid, y2 - dy, color);
            }
            if (smooth && solid > 0) {
                double cover = spans.cover[dy];
                if (cover > 0.04) {
                    int edgeColor = (Math.round((float) (alpha * cover)) << 24) | rgb;
                    int leftEdge = x1 + solid - 1;
                    int rightEdge = x2 - solid;
                    context.fill(leftEdge, y1 + dy, leftEdge + 1, y1 + dy + 1, edgeColor);
                    context.fill(rightEdge, y1 + dy, rightEdge + 1, y1 + dy + 1, edgeColor);
                    context.fill(leftEdge, y2 - dy - 1, leftEdge + 1, y2 - dy, edgeColor);
                    context.fill(rightEdge, y2 - dy - 1, rightEdge + 1, y2 - dy, edgeColor);
                }
            }
        }
    }

    /**
     * Non-antialiased rounded fill used by the small checklist controls. It keeps
     * the original overlapping bands (important for translucent colours) and only
     * replaces the repeated square-root pass with the shared arc cache.
     */
    public static void fillRoundedFlat(DrawContext context, int x1, int y1, int x2, int y2,
                                       int color, int radius) {
        if (radius <= 0) {
            context.fill(x1, y1, x2, y2, color);
            return;
        }
        int r = Math.min(radius, Math.min(x2 - x1, y2 - y1) / 2);
        context.fill(x1 + r, y1, x2 - r, y2, color);
        context.fill(x1, y1 + r, x2, y2 - r, color);
        if (r <= 0) return;
        ArcSpans spans = arcSpans(r);
        for (int dy = 0; dy < r; dy++) {
            int inset = spans.strokeInset[dy];
            context.fill(x1 + inset, y1 + dy, x1 + r, y1 + dy + 1, color);
            context.fill(x2 - r, y1 + dy, x2 - inset, y1 + dy + 1, color);
            context.fill(x1 + inset, y2 - dy - 1, x1 + r, y2 - dy, color);
            context.fill(x2 - r, y2 - dy - 1, x2 - inset, y2 - dy, color);
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
        ArcSpans spans = arcSpans(r);
        for (int dy = 0; dy < r; dy++) {
            int inset = spans.solid[dy];
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

    private record RowGradientKey(int h, int r, int topColor, int bottomColor) {}

    private static final Map<RowGradientKey, RowGradient> ROW_GRADIENTS = new HashMap<>();

    private static RowGradient rowGradient(int h, int r, int topColor, int bottomColor) {
        RowGradientKey key = new RowGradientKey(h, r, topColor, bottomColor);
        RowGradient cached = ROW_GRADIENTS.get(key);
        if (cached != null) return cached;
        int[] insets = new int[h];
        int[] colors = new int[h];
        // The bottom corner uses the same dy values as the top one, reversed.
        ArcSpans spans = r > 0 ? arcSpans(r) : null;
        for (int row = 0; row < h; row++) {
            int inset = 0;
            if (spans != null) {
                if (row < r) inset = spans.solid[row];
                else if (row >= h - r) inset = spans.solid[r - 1 - (row - (h - r))];
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

    private record ColumnGradientKey(int w, int r, int leftColor, int rightColor) {}

    private static final Map<ColumnGradientKey, ColumnGradient> COLUMN_GRADIENTS = new HashMap<>();
    private static final int GRADIENT_CACHE_LIMIT = 32;

    private static ColumnGradient columnGradient(int w, int r, int leftColor, int rightColor) {
        ColumnGradientKey key = new ColumnGradientKey(w, r, leftColor, rightColor);
        ColumnGradient cached = COLUMN_GRADIENTS.get(key);
        if (cached != null) return cached;
        int[] insets = new int[w];
        int[] colors = new int[w];
        // Same reversed-dy trick as the row table, for the right-hand corners.
        ArcSpans spans = r > 0 ? arcSpans(r) : null;
        for (int col = 0; col < w; col++) {
            int inset = 0;
            if (spans != null) {
                if (col < r) inset = spans.solid[col];
                else if (col >= w - r) inset = spans.solid[r - 1 - (col - (w - r))];
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
    private record DiagonalGradientKey(int w, int h, int r, int fromColor, int toColor) {}
    private static final Map<DiagonalGradientKey, int[]> DIAGONAL_GRADIENTS = new HashMap<>();

    private static int[] diagonalColors(int w, int h, int r, int fromColor, int toColor) {
        DiagonalGradientKey key = new DiagonalGradientKey(w, h, r, fromColor, toColor);
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
        MenuFonts.drawTracked(context, renderer, value, x, y, color,
            MenuFonts.tier(size), tracking);
    }

    /** Measured width for {@link #textTracked}. */
    public static int textWidthTracked(TextRenderer renderer, String value,
                                       float size, float tracking) {
        return MenuFonts.widthTracked(renderer, value, MenuFonts.tier(size), tracking);
    }

    /** Tracked truncation is deterministic too, so cache it like {@link #fit}. */
    private record TrackedFitKey(String value, int maxWidth, float size, float tracking) {}

    private static final Map<TrackedFitKey, String> FIT_TRACKED_CACHE = new HashMap<>();
    private static final int FIT_TRACKED_CACHE_LIMIT = 512;

    /** Tracked variant of {@link #fit}. */
    public static String fitTracked(TextRenderer renderer, String value, int maxWidth,
                                    float size, float tracking) {
        if (value == null || maxWidth <= 0) return "";
        TrackedFitKey key = new TrackedFitKey(value, maxWidth, size, tracking);
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
        int[] cuts = codePointBounds(value);
        int low = 0;
        int high = cuts.length - 1;
        while (low < high) {
            int mid = (low + high + 1) >>> 1;
            if (textWidthTracked(renderer, value.substring(0, cuts[mid]), size, tracking) <= limit) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return value.substring(0, cuts[low]).stripTrailing() + ellipsis;
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
        int base = (baseAlpha << 24) | (panelColor & 0x00FFFFFF);
        fillRounded(context, x, y, x2, y2, base, radius);

        fillRoundedGradient(context, x + 1, y + 1, w - 2, h - 2,
            alpha(0.06f, 0xFFFFFF), alpha(0.13f, theme.titleBar & 0x00FFFFFF),
            Math.max(0, radius - 1));
        if (outlines) {
            int rimAlpha = (theme.panelBorder >>> 24) & 0xFF;
            if (rimAlpha == 0) rimAlpha = 0x38;
            rimAlpha = Math.min(0x38, rimAlpha);
            // Stroke the rim only. The old full-panel fill tinted the entire
            // panel body with the border colour, muddying every translucent card.
            strokeRounded(context, x, y, w, h,
                alpha(rimAlpha, theme.panelBorder & 0x00FFFFFF), radius, 1);
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
        ArcSpans spans = arcSpans(r);
        for (int row = 0; row < r; row++) {
            int inset = spans.strokeInset[row];
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
        int thumbH = Math.min(h, Math.max(24, h * h / (h + (int) maxScroll)));
        int thumbY = y + (int) ((h - thumbH) * Math.max(0.0f,
            Math.min(1.0f, scroll / maxScroll)));
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
        String wordmark = "Onyx";
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
