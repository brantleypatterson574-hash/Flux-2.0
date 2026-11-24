package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class KeybindsModule extends Module implements HudOverlay {
    private final BooleanSetting showAll = new BooleanSetting("Show All", false).description("Lists keybinds for every module, not just enabled ones.");
    private final NumberSetting x = new NumberSetting("X", 4.0, 0.0, 1000.0).advanced().description("Horizontal position of the keybind list on screen.");
    private final NumberSetting y = new NumberSetting("Y", 60.0, 0.0, 1000.0).advanced().description("Vertical position of the keybind list on screen.");
    private final BooleanSetting background = new BooleanSetting("Background", true).description("Draws a shaded panel behind the keybind list.");
    private final NumberSetting color = new NumberSetting("Color", 0xFFFFFF, 0x000000, 0xFFFFFF).description("Colour of the keybind list text.");
    private final NumberSetting lineSpacing = new NumberSetting("Line Spacing", 12.0, 8.0, 32.0).description("Pixels between lines in the keybind list.");

    private final List<String> cachedLines = new ArrayList<>();
    private long cachedVersion = -1L;
    private int refreshTicks;

    public KeybindsModule() {
        super("Keybinds", "Lists module keybinds on the HUD.", Category.HUD);
        addSetting(showAll);
        addSetting(x);
        addSetting(y);
        addSetting(background);
        addSetting(color);
        addSetting(lineSpacing);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        refreshTicks = 0;
    }

    @Override
    public void onDisable() {
        cachedLines.clear();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        long version = Module.enabledVersion();
        if (version != cachedVersion || --refreshTicks <= 0) rebuild();
    }

    /** Keybind labels are cheap to build, but doing it every frame shows up in HUD cost. */
    private void rebuild() {
        cachedVersion = Module.enabledVersion();
        refreshTicks = 20;
        cachedLines.clear();
        List<Module> modules = UtilModClient.getModules();
        if (modules == null) return;
        for (Module module : modules) {
            if (module == this || (!showAll.isEnabled() && !module.isEnabled())) continue;
            String boundKey = null;
            for (Setting setting : module.getSettings()) {
                if (setting instanceof KeybindSetting keybind) {
                    boundKey = keybind.getDisplayValue();
                    break;
                }
            }
            if (boundKey != null) cachedLines.add(module.getName() + ": " + boundKey);
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden || cachedLines.isEmpty()) return;

        int startX = (int) x.getValue();
        int startY = (int) y.getValue();
        int spacing = Math.max(8, (int) lineSpacing.getValue());
        int rgb = 0xFF000000 | ((int) color.getValue() & 0xFFFFFF);

        if (background.isEnabled()) {
            int width = 0;
            for (String line : cachedLines) width = Math.max(width, mc.textRenderer.getWidth(line));
            context.fill(startX - 4, startY - 4, startX + width + 4,
                startY + cachedLines.size() * spacing + 4, 0x99000000);
        }

        for (int i = 0; i < cachedLines.size(); i++) {
            OverlayRender.text(context, cachedLines.get(i), startX, startY + i * spacing, rgb);
        }
    }
}
