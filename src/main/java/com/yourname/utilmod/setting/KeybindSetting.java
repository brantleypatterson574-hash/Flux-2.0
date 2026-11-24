package com.yourname.utilmod.setting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * A keycode setting that can be re-bound from the dropdown menu.
 * Left click the row, then press a key. Right click clears it.
 */
public class KeybindSetting extends Setting {
    private final KeyInput defaultValue;
    private KeyInput value;
    private boolean previousPressed;

    public KeybindSetting(String name, int defaultKeyCode) {
        super(name);
        this.defaultValue = defaultKeyCode < 0 ? null : new KeyInput(defaultKeyCode, 0, 0);
        this.value = defaultValue;
    }

    public KeyInput getKey() {
        return value;
    }

    public int keyCode() {
        return value == null ? -1 : value.key();
    }

    public void set(KeyInput input) {
        value = input == null ? null : new KeyInput(input.key(), input.scancode(), input.modifiers());
        previousPressed = false;
    }

    public void clear() {
        value = null;
    }

    /** Restores a binding from a persisted raw key code. */
    public void setKeyCode(int keyCode) {
        setInput(keyCode, 0, 0);
    }

    /** Restores the full binding captured by the GUI, including scancode and modifiers. */
    public void setInput(int keyCode, int scancode, int modifiers) {
        if (keyCode < 0) clear();
        else set(new KeyInput(keyCode, scancode, modifiers));
    }

    public int scancode() {
        return value == null ? 0 : value.scancode();
    }

    public int modifiers() {
        return value == null ? 0 : value.modifiers();
    }

    /** Modifier keys captured with the binding must be held for it to fire. */
    private boolean modifiersMatch(MinecraftClient client) {
        int required = value.modifiers();
        if (required == 0) return true;
        if ((required & GLFW.GLFW_MOD_SHIFT) != 0
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT)) return false;
        if ((required & GLFW.GLFW_MOD_CONTROL) != 0
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_CONTROL)) return false;
        if ((required & GLFW.GLFW_MOD_ALT) != 0
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_LEFT_ALT)
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_ALT)) return false;
        if ((required & GLFW.GLFW_MOD_SUPER) != 0
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_LEFT_SUPER)
            && !InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_SUPER)) return false;
        return true;
    }

    /** True on the first tick the bound key goes down. */
    public boolean wasPressed(MinecraftClient client) {
        if (value == null || client == null || client.getWindow() == null) return false;
        boolean pressed = InputUtil.isKeyPressed(client.getWindow(), value.key()) && modifiersMatch(client);
        boolean justPressed = pressed && !previousPressed;
        previousPressed = pressed;
        return justPressed;
    }

    /** True every tick the bound key is held. */
    public boolean isHeld(MinecraftClient client) {
        if (value == null || client == null || client.getWindow() == null) return false;
        return InputUtil.isKeyPressed(client.getWindow(), value.key()) && modifiersMatch(client);
    }

    public String getDisplayValue() {
        if (value == null) return "None";
        return InputUtil.fromKeyCode(value).getLocalizedText().getString();
    }

    @Override
    public void reset() {
        value = defaultValue;
        previousPressed = false;
    }

    @Override
    protected String getDefaultDescription() {
        return "Click, then press a key to bind it. ESC cancels capture. Right-click clears the binding.";
    }
}
