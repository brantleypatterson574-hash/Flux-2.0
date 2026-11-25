package org.lwjgl.glfw;

/**
 * Compile-time bridge for Minecraft 26.x, which no longer ships LWJGL's GLFW
 * module. The constants below are the stable GLFW key and modifier values the
 * shared Flux source uses for keybinds and menus.
 */
public final class GLFW {
    private GLFW() {}

    public static final int GLFW_MOUSE_BUTTON_LEFT = 1;
    public static final int GLFW_MOUSE_BUTTON_RIGHT = 2;

    public static final int GLFW_MOD_SHIFT = 0x00000001;
    public static final int GLFW_MOD_CONTROL = 0x00000002;
    public static final int GLFW_MOD_ALT = 0x00000004;
    public static final int GLFW_MOD_SUPER = 0x00000008;

    public static final int GLFW_KEY_BACKSPACE = 259;
    public static final int GLFW_KEY_ENTER = 257;
    public static final int GLFW_KEY_ESCAPE = 256;
    public static final int GLFW_KEY_KP_ENTER = 335;
    public static final int GLFW_KEY_LEFT_ALT = 342;
    public static final int GLFW_KEY_LEFT_CONTROL = 341;
    public static final int GLFW_KEY_LEFT_SHIFT = 340;
    public static final int GLFW_KEY_LEFT_SUPER = 343;
    public static final int GLFW_KEY_RIGHT_ALT = 348;
    public static final int GLFW_KEY_RIGHT_CONTROL = 345;
    public static final int GLFW_KEY_RIGHT_SHIFT = 344;
    public static final int GLFW_KEY_RIGHT_SUPER = 347;

    public static final int GLFW_KEY_C = 67;
    public static final int GLFW_KEY_F = 70;
    public static final int GLFW_KEY_G = 71;
    public static final int GLFW_KEY_V = 86;
    public static final int GLFW_KEY_X = 88;
}
