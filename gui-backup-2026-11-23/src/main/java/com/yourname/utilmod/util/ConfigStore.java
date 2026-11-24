package com.yourname.utilmod.util;

import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.module.impl.GuiThemeModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.setting.StringSetting;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Lightweight disk persistence for module enabled states and every ordinary
 * setting value. Checklist selections and friends keep their own files. The
 * format is deliberately tab-separated and human-readable so private testers
 * can inspect or edit it easily.
 */
public final class ConfigStore {
    /** Marker written after the menu palette/layout/scale defaults are migrated. */
    private static final String MENU_DEFAULTS_MARKER = "# MENU_PARITY_V3";

    private static boolean loading;
    private ConfigStore() {}

    public static boolean isLoading() {
        return loading;
    }

    private static Path file() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.runDirectory == null) return null;
        return client.runDirectory.toPath().resolve("config").resolve("utilmod-settings.txt");
    }

    public static void save(List<Module> modules) {
        if (loading || modules == null) return;
        Path path = file();
        if (path == null) return;
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Files.write(path, serialize(modules), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // Config saving is best-effort; the game keeps running if it fails.
        }
    }

    private static List<String> serialize(List<Module> modules) {
        List<String> lines = new ArrayList<>();
        lines.add("# UtilMod settings - module<TAB>setting<TAB>type<TAB>value");
        lines.add("# Generated automatically. Edit while the game is closed.");
        // Keep the one-time menu migration marker on every save; otherwise the
        // next launch would reset the user's theme/layout/scale choices again.
        lines.add(MENU_DEFAULTS_MARKER);
        for (Module module : modules) {
            lines.add("@ENABLED\t" + module.getName() + "\t" + module.isEnabled());
            for (Setting setting : module.getSettings()) {
                String encoded = encode(setting);
                if (encoded == null) continue;
                lines.add(module.getName() + "\t" + setting.getName() + "\t"
                    + type(setting) + "\t" + encoded);
            }
        }
        return lines;
    }

    public static void load(List<Module> modules) {
        if (modules == null) return;
        Path path = file();
        if (path == null || !Files.exists(path)) return;
        Map<String, Boolean> enabled = new LinkedHashMap<>();
        List<String> raw = new ArrayList<>();
        boolean menuDefaultsMigrated = false;
        loading = true;
        try {
            raw.addAll(Files.readAllLines(path, StandardCharsets.UTF_8));
            for (String line : raw) {
                if (line.equals(MENU_DEFAULTS_MARKER)) menuDefaultsMigrated = true;
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = line.split("\\t", 4);
                if (parts.length < 2) continue;

                if (parts[0].equals("@ENABLED") || parts[1].startsWith("@")) {
                    if (parts.length >= 3) enabled.put(parts[1], Boolean.parseBoolean(parts[2]));
                    continue;
                }
                Module module = findModule(modules, parts[0]);
                if (module == null) continue;
                if (parts.length < 4) continue;
                Setting setting = findSetting(module, parts[1]);
                if (setting != null) decode(setting, parts[2], parts[3]);
            }
            for (Map.Entry<String, Boolean> entry : enabled.entrySet()) {
                Module module = findModule(modules, entry.getKey());
                if (module != null && module.isEnabled() != entry.getValue()) module.toggle();
            }

            // Old configs could leave the menu on SAKURA/DROPDOWN after the mockup
            // parity update. Reset those appearance values once, then persist a
            // marker so later theme/layout choices survive restarts.
            if (!menuDefaultsMigrated) {
                Module menu = findModule(modules, "Menu Theme");
                if (menu instanceof GuiThemeModule themeModule) {
                    themeModule.applyMockupDefaults();
                }
                Files.write(path, serialize(modules), StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {
            // Corrupt or unreadable config is ignored; defaults remain active.
        } finally {
            loading = false;
        }
    }

    private static Module findModule(List<Module> modules, String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }

    private static Setting findSetting(Module module, String name) {
        for (Setting setting : module.getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) return setting;
        }
        return null;
    }

    private static String type(Setting setting) {
        if (setting instanceof BooleanSetting) return "bool";
        if (setting instanceof NumberSetting) return "num";
        if (setting instanceof EnumSetting<?>) return "enum";
        if (setting instanceof StringSetting) return "str";
        if (setting instanceof KeybindSetting) return "key";
        return "skip";
    }

    private static String encode(Setting setting) {
        if (setting instanceof BooleanSetting b) return Boolean.toString(b.isEnabled());
        if (setting instanceof NumberSetting n) return Double.toString(n.getValue());
        if (setting instanceof EnumSetting<?> e) return e.getValue().name();
        if (setting instanceof StringSetting s) {
            return URLEncoder.encode(s.getValue(), StandardCharsets.UTF_8);
        }
        if (setting instanceof KeybindSetting k) return Integer.toString(k.keyCode());
        return null;
    }

    private static void decode(Setting setting, String type, String value) {
        try {
            switch (type) {
                case "bool" -> {
                    if (setting instanceof BooleanSetting b) b.setEnabled(Boolean.parseBoolean(value));
                }
                case "num" -> {
                    if (setting instanceof NumberSetting n) n.setValue(Double.parseDouble(value));
                }
                case "enum" -> {
                    if (setting instanceof EnumSetting<?> e) setEnum(e, value);
                }
                case "str" -> {
                    if (setting instanceof StringSetting s) {
                        s.setValue(URLDecoder.decode(value, StandardCharsets.UTF_8));
                    }
                }
                case "key" -> {
                    if (setting instanceof KeybindSetting k) k.setKeyCode(Integer.parseInt(value));
                }
                default -> { }
            }
        } catch (RuntimeException ignored) {
            // One bad value should not discard the rest of the config.
        }
    }

    private static void setEnum(EnumSetting<?> setting, String name) {
        for (Object value : setting.getValues()) {
            if (((Enum<?>) value).name().equalsIgnoreCase(name)) {
                applyEnum(setting, value);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void applyEnum(EnumSetting<T> setting, Object value) {
        setting.setValue((T) value);
    }
}
