package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Category pictures are rendered from Minecraft's own item sprites at runtime.
 *
 * <p>Using the vanilla item models means the End Crystal tab really shows an End
 * Crystal, the Sword tab a diamond sword, and so on. No custom category PNGs are
 * sampled here, so resource packs and model changes stay in sync automatically.
 */
public final class MenuItemIcons {
    private static final ItemStack END_CRYSTAL = new ItemStack(Items.END_CRYSTAL);
    private static final ItemStack SWORD = new ItemStack(Items.DIAMOND_SWORD);
    private static final ItemStack MACE = new ItemStack(Items.MACE);
    private static final ItemStack MOVEMENT = new ItemStack(Items.ELYTRA);
    private static final ItemStack PLAYER = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ItemStack RENDER = new ItemStack(Items.SPYGLASS);
    private static final ItemStack WORLD = new ItemStack(Items.COMPASS);
    private static final ItemStack DONUT = new ItemStack(Items.COOKIE);
    private static final ItemStack UTIL = new ItemStack(Items.WRITABLE_BOOK);
    private static final ItemStack HUD = new ItemStack(Items.CLOCK);
    private static final ItemStack FALLBACK = new ItemStack(Items.BOOK);

    private MenuItemIcons() {}

    /** Draws one vanilla item sprite centred in the supplied square. */
    public static void draw(DrawContext context, Category category, int x, int y, int size) {
        ItemStack stack = stack(category);
        float scale = Math.max(0.1f, size / 16.0f);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(x, y);
        context.getMatrices().scale(scale, scale);
        context.drawItemWithoutEntity(stack, 0, 0);
        context.getMatrices().popMatrix();
    }

    private static ItemStack stack(Category category) {
        if (category == null) return FALLBACK;
        return switch (category) {
            case CRYSTAL -> END_CRYSTAL;
            case SWORD -> SWORD;
            case MACE -> MACE;
            case MOVEMENT -> MOVEMENT;
            case PLAYER -> PLAYER;
            case RENDER -> RENDER;
            case WORLD -> WORLD;
            case DONUT -> DONUT;
            case UTIL -> UTIL;
            case HUD -> HUD;
        };
    }
}
