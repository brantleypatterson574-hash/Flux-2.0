package com.yourname.utilmod.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Menu text is now drawn with Minecraft's default font.
 *
 * <p>The tier list is kept only as a set of target pixel sizes so existing menu
 * labels keep their relative hierarchy. Each label is measured with the vanilla
 * {@link TextRenderer} and scaled to its target size; no custom Flux TTF or font
 * style is applied. A resource pack that replaces Minecraft's default font is
 * therefore picked up automatically after a reload.
 *
 * <p>{@code assets/flux/menu_fonts.json} is still read for compatibility with
 * old configs, but its font identifiers no longer select a custom glyph set.
 */
public final class MenuFonts {
    /** Resource-pack hook. Deliberately outside {@code font/}, which Minecraft scans as font definitions. */
    public static final Identifier CONFIG_ID = Identifier.of("flux", "menu_fonts.json");

    /** Kept for config compatibility. The menu no longer loads this TTF. */
    public static final String DEFAULT_TTF = "flux:semibold.ttf";

    /**
     * A tier is now just a target label size in logical pixels. The font field
     * is parsed for compatibility but ignored when drawing.
     */
    public record Tier(int size, Identifier font) {}

    /** Parsed/overridden tier list applied at resource reload. */
    public record Config(List<Tier> tiers) {
        public Config {
            tiers = List.copyOf(tiers);
        }
    }

    private static final List<Tier> DEFAULT_TIERS = List.of(
        new Tier(8, Identifier.of("flux", "menu_8")),
        new Tier(10, Identifier.of("flux", "menu_10")),
        new Tier(12, Identifier.of("flux", "menu_small")),
        new Tier(13, Identifier.of("flux", "menu_13")),
        new Tier(14, Identifier.of("flux", "menu")),
        new Tier(15, Identifier.of("flux", "menu_15")),
        new Tier(16, Identifier.of("flux", "menu_large")),
        new Tier(18, Identifier.of("flux", "menu_18")),
        new Tier(19, Identifier.of("flux", "menu_title")),
        new Tier(20, Identifier.of("flux", "menu_20"))
    );

    private record FontSet(List<Tier> tiers, Style[] styles) {
        FontSet(List<Tier> tiers) {
            this(tiers, buildStyles(tiers));
        }
    }

    /** One reference swap keeps tier lookup and style lookup in sync on reload. */
    private static volatile FontSet active = new FontSet(DEFAULT_TIERS);

    /** Typed keys avoid embedding a NUL separator in cache identifiers. */
    private record TextKey(int tier, String value) {}

    private static final Map<TextKey, Text> TEXT_CACHE = new HashMap<>();
    private static final Map<TextKey, Integer> WIDTH_CACHE = new HashMap<>();
    private static final int CACHE_LIMIT = 1024;

    private MenuFonts() {}

    public static Config defaultConfig() {
        return new Config(DEFAULT_TIERS);
    }

    /**
     * Reads the resource-pack hook. Missing, malformed or empty files fall back
     * to the bundled semibold tiers instead of breaking the menu.
     */
    public static Config readConfig(ResourceManager manager) {
        Optional<Resource> found = manager.getResource(CONFIG_ID);
        if (found.isEmpty()) return defaultConfig();

        try (InputStream stream = found.get().getInputStream();
             InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonObject()) return defaultConfig();

            JsonElement tiersElement = parsed.getAsJsonObject().get("tiers");
            if (tiersElement == null || !tiersElement.isJsonArray()) return defaultConfig();

            List<Tier> parsedTiers = new ArrayList<>();
            JsonArray array = tiersElement.getAsJsonArray();
            for (JsonElement element : array) {
                if (!element.isJsonObject()) continue;
                JsonObject object = element.getAsJsonObject();
                JsonElement sizeElement = object.get("size");
                JsonElement fontElement = object.get("font");
                if (sizeElement == null || !sizeElement.isJsonPrimitive()
                    || fontElement == null || !fontElement.isJsonPrimitive()) continue;

                int size;
                try {
                    size = sizeElement.getAsInt();
                } catch (RuntimeException ignored) {
                    continue;
                }
                Identifier font = parseIdentifier(fontElement.getAsString());
                if (size >= 1 && font != null) parsedTiers.add(new Tier(size, font));
            }

            if (parsedTiers.isEmpty()) return defaultConfig();
            parsedTiers.sort(Comparator.comparingInt(Tier::size));
            return new Config(parsedTiers);
        } catch (Exception ignored) {
            return defaultConfig();
        }
    }

    /** Applies a hook configuration and drops measurements made with the old font. */
    public static void apply(Config config) {
        List<Tier> tiers = config == null || config.tiers().isEmpty()
            ? DEFAULT_TIERS
            : config.tiers();
        active = new FontSet(List.copyOf(tiers));
        invalidate();
    }

    /**
     * Drops cached glyph widths and texts after a resource reload. A resource
     * pack can replace Minecraft's default font, so any width measured before
     * the reload must not be trusted afterwards.
     */
    public static void invalidate() {
        TEXT_CACHE.clear();
        WIDTH_CACHE.clear();
    }

    /** Index of the closest target size for vanilla-font labels. */
    public static int tier(float size) {
        List<Tier> tiers = active.tiers();
        float target = Math.max(tiers.get(0).size(),
            Math.min(tiers.get(tiers.size() - 1).size(), size));
        int best = 0;
        float bestDelta = Float.MAX_VALUE;
        for (int i = 0; i < tiers.size(); i++) {
            float delta = Math.abs(tiers.get(i).size() - target);
            // On a tie prefer the larger tier; 11px labels should not shrink.
            if (delta <= bestDelta) {
                bestDelta = delta;
                best = i;
            }
        }
        return best;
    }

    /** Default Minecraft font for every tier. Kept for existing callers. */
    public static Style style(int tier) {
        return Style.EMPTY;
    }

    /** Cached Text for a tier; menu labels repeat every frame. */
    public static Text text(String value, int tier) {
        String safe = value == null ? "" : value;
        int index = clamp(tier, active.styles().length);
        TextKey key = new TextKey(index, safe);
        Text cached = TEXT_CACHE.get(key);
        if (cached != null) return cached;
        Text made = Text.literal(safe);
        if (TEXT_CACHE.size() >= CACHE_LIMIT) TEXT_CACHE.clear();
        TEXT_CACHE.put(key, made);
        return made;
    }

    /** Measured width of the vanilla font at the requested menu label size. */
    public static int width(TextRenderer renderer, String value, int tier) {
        if (value == null || value.isEmpty()) return 0;
        int index = clamp(tier, active.styles().length);
        TextKey key = new TextKey(index, value);
        Integer cached = WIDTH_CACHE.get(key);
        if (cached != null) return cached;
        int width = Math.round(renderer.getWidth(text(value, index)) * scaleFor(renderer, index));
        if (WIDTH_CACHE.size() >= CACHE_LIMIT) WIDTH_CACHE.clear();
        WIDTH_CACHE.put(key, width);
        return width;
    }

    /** Draws vanilla text at the tier's target pixel size. */
    public static void draw(DrawContext context, TextRenderer renderer, String value,
                            int x, int y, int color, int tier) {
        if (value == null) return;
        int index = clamp(tier, active.styles().length);
        Text label = text(value, index);
        float scale = scaleFor(renderer, index);
        if (Math.abs(scale - 1.0f) < 0.01f) {
            context.drawText(renderer, label, x, y, color, false);
            return;
        }
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(scale, scale);
        context.drawText(renderer, label, 0, 0, color, false);
        context.getMatrices().popMatrix();
    }

    /** Letter-spaced draw using the same vanilla font tier for every glyph. */
    public static void drawTracked(DrawContext context, TextRenderer renderer, String value,
                                   int x, int y, int color, int tier, float tracking) {
        if (value == null || value.isEmpty()) return;
        int index = clamp(tier, active.styles().length);
        int[] points = value.codePoints().toArray();
        float spacing = tracking * scaleFor(renderer, index);
        float cursor = x;
        for (int i = 0; i < points.length; i++) {
            String glyph = new String(points, i, 1);
            draw(context, renderer, glyph, Math.round(cursor), y, color, index);
            cursor += width(renderer, glyph, index);
            // Tracking is a gap BETWEEN glyphs. Adding it after the final one
            // made the measured width one unit short and overflowed fitted text.
            if (i + 1 < points.length) cursor += spacing;
        }
    }

    public static int widthTracked(TextRenderer renderer, String value, int tier, float tracking) {
        if (value == null || value.isEmpty()) return 0;
        int index = clamp(tier, active.styles().length);
        int[] points = value.codePoints().toArray();
        float total = tracking * scaleFor(renderer, index) * (points.length - 1);
        for (int point : points) {
            total += width(renderer, new String(new int[]{point}, 0, 1), index);
        }
        return Math.round(total);
    }

    /** Converts a semantic tier into a multiplier over vanilla's font height. */
    private static float scaleFor(TextRenderer renderer, int tier) {
        List<Tier> tiers = active.tiers();
        int index = clamp(tier, tiers.size());
        float target = Math.max(1.0f, tiers.get(index).size());
        float base = renderer == null ? 9.0f : Math.max(1.0f, renderer.fontHeight);
        return target / base;
    }

    private static Style[] buildStyles(List<Tier> tiers) {
        Style[] styles = new Style[tiers.size()];
        for (int i = 0; i < tiers.size(); i++) {
            styles[i] = Style.EMPTY;
        }
        return styles;
    }

    private static int clamp(int tier, int length) {
        return Math.max(0, Math.min(length - 1, tier));
    }

    private static Identifier parseIdentifier(String raw) {
        if (raw == null) return null;
        int colon = raw.indexOf(':');
        if (colon <= 0 || colon == raw.length() - 1) return null;
        try {
            return Identifier.of(raw.substring(0, colon), raw.substring(colon + 1));
        } catch (RuntimeException ignored) {
            return null;
        }
    }
}
