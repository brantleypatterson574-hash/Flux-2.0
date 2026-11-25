package com.yourname.utilmod.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

/**
 * Compile-time bridge for the 26.x ItemStack API. ItemStack.isOf/isIn were
 * replaced by itemMatches(Predicate&lt;RegistryEntry&lt;Item&gt;&gt;).
 */
public final class StackCompat {
    private StackCompat() {}

    public static boolean is(ItemStack stack, Item item) {
        return stack.itemMatches(entry -> entry.comp_349() == item);
    }

    public static boolean isIn(ItemStack stack, TagKey<Item> tag) {
        return stack.itemMatches(entry -> entry.isIn(tag));
    }
}
