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
 * Fixed-size, pre-rasterised TTF tiers shared by every Flux menu surface.
 *
 * <p>The old menu scaled a single font with the matrix stack. Any non-integer
 * scale resampled the glyph atlas and made labels look pixelated/blurry. Every
 * tier here is rasterised at its final logical size with a high oversample, so
 * text is drawn with an identity text matrix and stays crisp at 1:1 output.
 */
public final class MenuFonts {
    /** Logical pixel heights. Sizes between these snap to the nearest tier. */
    private static final int[] SIZES = {8, 10, 12, 13, 14, 15, 16, 18, 19, 20};
    private static final String[] PATHS = {
        "menu_8", "menu_10", "menu_small", "menu_13", "menu",
        "menu_15", "menu_large", "menu_18", "menu_title", "menu_20"
    };
    private static final Style[] STYLES = new Style[SIZES.length];

    private static final Map<String, Text> TEXT_CACHE = new HashMap<>();
    private static final Map<String, Integer> WIDTH_CACHE = new HashMap<>();
    private static final int CACHE_LIMIT = 1024;

    static {
        for (int i = 0; i < PATHS.length; i++) {
            STYLES[i] = Style.EMPTY.withFont(
                new StyleSpriteSource.Font(Identifier.of("flux", PATHS[i])));
        }
    }

    private MenuFonts() {}

    /** Index of the closest rasterised size, so no matrix scaling is needed. */
    public static int tier(float size) {
        float target = Math.max(8.0f, Math.min(20.0f, size));
        int best = 0;
        float bestDelta = Float.MAX_VALUE;
        for (int i = 0; i < SIZES.length; i++) {
            float delta = Math.abs(SIZES[i] - target);
            // On a tie prefer the larger tier; 11px labels should not shrink.
            if (delta <= bestDelta) {
                bestDelta = delta;
                best = i;
            }
        }
        return best;
    }

    public static Style style(int tier) {
        return STYLES[Math.max(0, Math.min(STYLES.length - 1, tier))];
    }

    /** Cached Text for a tier; menu labels repeat every frame. */
    public static Text text(String value, int tier) {
        String safe = value == null ? "" : value;
        int index = Math.max(0, Math.min(STYLES.length - 1, tier));
        String key = index + "\u0000" + safe;
        Text cached = TEXT_CACHE.get(key);
        if (cached != null) return cached;
        Text made = Text.literal(safe).setStyle(STYLES[index]);
        if (TEXT_CACHE.size() >= CACHE_LIMIT) TEXT_CACHE.clear();
        TEXT_CACHE.put(key, made);
        return made;
    }

    /** Measured width of the exact tier that {@link #text} will draw. */
    public static int width(TextRenderer renderer, String value, int tier) {
        if (value == null || value.isEmpty()) return 0;
        int index = Math.max(0, Math.min(STYLES.length - 1, tier));
        String key = index + "\u0000" + value;
        Integer cached = WIDTH_CACHE.get(key);
        if (cached != null) return cached;
        int width = renderer.getWidth(text(value, index));
        if (WIDTH_CACHE.size() >= CACHE_LIMIT) WIDTH_CACHE.clear();
        WIDTH_CACHE.put(key, width);
        return width;
    }

    /** Draws at the current matrix without any extra font scaling. */
    public static void draw(DrawContext context, TextRenderer renderer, String value,
                            int x, int y, int color, int tier) {
        if (value == null) return;
        context.drawText(renderer, text(value, tier), x, y, color, false);
    }

    /** Letter-spaced draw using the same fixed tier for every glyph. */
    public static void drawTracked(DrawContext context, TextRenderer renderer, String value,
                                   int x, int y, int color, int tier, float tracking) {
        if (value == null || value.isEmpty()) return;
        float cursor = x;
        for (int i = 0; i < value.length(); i++) {
            String glyph = value.substring(i, i + 1);
            context.drawText(renderer, text(glyph, tier), Math.round(cursor), y, color, false);
            cursor += width(renderer, glyph, tier) + tracking;
        }
    }

    public static int widthTracked(TextRenderer renderer, String value, int tier, float tracking) {
        if (value == null || value.isEmpty()) return 0;
        int total = Math.round(tracking * (value.length() - 1));
        for (int i = 0; i < value.length(); i++) {
            total += width(renderer, value.substring(i, i + 1), tier);
        }
        return total;
    }
}
