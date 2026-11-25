package net.fabricmc.fabric.api.client.keybinding.v1;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.option.KeyBinding;

/** Compile-time bridge from the old Fabric keybinding API to 26.x KeyMappingHelper. */
public final class KeyBindingHelper {
    private KeyBindingHelper() {}

    public static KeyBinding registerKeyBinding(KeyBinding binding) {
        return KeyMappingHelper.registerKeyMapping(binding);
    }

    public static int getBoundKeyCode(KeyBinding binding) {
        return KeyMappingHelper.getBoundKeyOf(binding).getCode();
    }
}
