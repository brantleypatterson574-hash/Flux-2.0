package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;

/** Adds a few harmless joke lines to item tooltips. Held items are not changed. */
public class BananaTooltipsModule extends Module {
    public static BananaTooltipsModule INSTANCE;

    private enum LineStyle { FUNNY, COMPLIMENTS, FACTS }

    private static final String[] FUNNY = {
        "Banana approved.",
        "Sort of banana shaped.",
        "Comes with absolutely no bananas.",
        "Perfectly ripe for adventure.",
        "The fruit bowl is jealous."
    };
    private static final String[] COMPLIMENTS = {
        "You have excellent taste.",
        "This looks good on you.",
        "A bold and fruity choice.",
        "Ten out of ten, would carry again."
    };
    private static final String[] FACTS = {
        "Bananas are berries. This is not one.",
        "No bananas were harmed in the making of this item.",
        "Scientifically, still not a banana.",
        "Pairs well with a sensible helmet."
    };

    private final EnumSetting<LineStyle> lineStyle =
        new EnumSetting<>("Line Style", LineStyle.class, LineStyle.FUNNY)
            .description("Pick the kind of joke line added to item tooltips.");
    private final BooleanSetting showPrefix = new BooleanSetting("Banana Prefix", true)
        .description("Starts each added line with a small banana label so it is easy to spot.");
    private final NumberSetting extraLines = new NumberSetting("Extra Lines", 1.0, 1.0, 3.0)
        .description("How many joke lines to add to each item tooltip.");
    private final StringSetting customLine = new StringSetting("Custom Line", "")
        .description("Optional extra text to append. Leave empty to use only the built-in lines.");

    public BananaTooltipsModule() {
        super("Banana Tooltips", "Adds harmless banana jokes to item tooltips in your own menu. Items and their real stats are untouched.",
            Category.RENDER);
        INSTANCE = this;
        addSetting(lineStyle);
        addSetting(showPrefix);
        addSetting(extraLines);
        addSetting(customLine);
        ItemTooltipCallback.EVENT.register(this::appendTooltip);
    }

    private void appendTooltip(ItemStack stack, Item.TooltipContext context,
                               TooltipType type, List<Text> lines) {
        if (!isEnabled() || stack == null || lines == null) return;
        String[] pool = switch (lineStyle.getValue()) {
            case FUNNY -> FUNNY;
            case COMPLIMENTS -> COMPLIMENTS;
            case FACTS -> FACTS;
        };
        int seed = Math.floorMod(stack.getName().getString().hashCode(), pool.length);
        String prefix = showPrefix.isEnabled() ? "BANANA: " : "";
        int count = Math.max(1, Math.min(pool.length, (int) extraLines.getValue()));
        for (int i = 0; i < count; i++) {
            lines.add(Text.literal(prefix + pool[(seed + i) % pool.length]));
        }
        String custom = customLine.getValue().trim();
        if (!custom.isEmpty()) lines.add(Text.literal(prefix + custom));
    }
}
