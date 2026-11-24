package com.yourname.utilmod.module;

/**
 * Menu sections. Combat is deliberately split into the three PvP weapon
 * families so each gets its own icon and its own settings space.
 */
public enum Category {
    CRYSTAL("Crystal", 0xFF67E8F9),
    SWORD("Sword", 0xFFFB7185),
    MACE("Mace", 0xFFFBBF24),
    MOVEMENT("Movement", 0xFFA3E635),
    PLAYER("Player", 0xFF4ADE80),
    RENDER("Render", 0xFFC084FC),
    WORLD("World", 0xFF38BDF8),
    DONUT("Donut", 0xFFF472B6),
    UTIL("Util", 0xFF9CA3AF),
    HUD("HUD", 0xFFF0ABFC);

    private final String name;
    private final int color;

    Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
