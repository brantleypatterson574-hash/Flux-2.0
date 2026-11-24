package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ConfigStore;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigProfilesModule extends Module {
    private static final String HEADER = "# UtilMod profiles - index=comma separated enabled flags";

    private final NumberSetting profileIndex = new NumberSetting("Profile Index", 0.0, 0.0, 4.0).description("Which of the five saved config slots to save or load.");
    private final BooleanSetting save = new BooleanSetting("Save", false).description("Saves the current module setup to the chosen slot.");
    private final BooleanSetting load = new BooleanSetting("Load", false).description("Restores the module setup from the chosen slot.");
    private final BooleanSetting applyOnEnable = new BooleanSetting("Apply When Enabled", true).description("Loads the chosen profile as soon as this module is turned on.");

    private final boolean[][] profiles = new boolean[5][];
    private boolean pendingEnableLoad;

    public ConfigProfilesModule() {
        super("Config", "Saves and restores module enabled states in five persistent slots.", Category.UTIL);
        addSetting(profileIndex);
        addSetting(save);
        addSetting(load);
        addSetting(applyOnEnable);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private static Path file() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.runDirectory == null) return null;
        return client.runDirectory.toPath().resolve("config").resolve("utilmod-profiles.txt");
    }

    /** Reads saved slots. Called during client init, after the run dir is valid. */
    public void reloadFromDisk() {
        Path path = file();
        if (path == null || !Files.exists(path)) return;
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("#")) continue;
                int split = line.indexOf('=');
                if (split <= 0) continue;
                int index;
                try {
                    index = Integer.parseInt(line.substring(0, split).trim());
                } catch (NumberFormatException ignored) {
                    continue;
                }
                if (index < 0 || index >= profiles.length) continue;
                String[] parts = line.substring(split + 1).split(",");
                boolean[] state = new boolean[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    state[i] = Boolean.parseBoolean(parts[i].trim());
                }
                profiles[index] = state;
            }
        } catch (IOException ignored) {
            // Corrupt profile file: keep the empty in-memory slots.
        }
    }

    private void saveProfilesToDisk() {
        Path path = file();
        if (path == null) return;
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            lines.add(HEADER);
            for (int i = 0; i < profiles.length; i++) {
                boolean[] state = profiles[i];
                if (state == null) continue;
                StringBuilder row = new StringBuilder().append(i).append('=');
                for (int j = 0; j < state.length; j++) {
                    if (j > 0) row.append(',');
                    row.append(state[j] ? '1' : '0');
                }
                lines.add(row.toString());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // Persistence is best-effort; slots still work for this session.
        }
    }

    @Override
    public void onEnable() {
        pendingEnableLoad = applyOnEnable.isEnabled();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        List<Module> modules = UtilModClient.getModules();
        if (modules == null) return;

        int index = Math.max(0, Math.min(profiles.length - 1, (int) profileIndex.getValue()));

        if (save.isEnabled()) {
            saveProfile(index, modules);
            save.setEnabled(false);
        }
        if (load.isEnabled()) {
            loadProfile(index, modules);
            load.setEnabled(false);
            ConfigStore.save(modules);
        }
        if (pendingEnableLoad) {
            loadProfile(index, modules);
            pendingEnableLoad = false;
            ConfigStore.save(modules);
        }
    }

    private void saveProfile(int index, List<Module> modules) {
        boolean[] state = new boolean[modules.size()];
        for (int i = 0; i < modules.size(); i++) {
            state[i] = modules.get(i).isEnabled();
        }
        profiles[index] = state;
        saveProfilesToDisk();
    }

    private void loadProfile(int index, List<Module> modules) {
        boolean[] state = profiles[index];
        if (state == null) return;
        for (int i = 0; i < Math.min(state.length, modules.size()); i++) {
            Module module = modules.get(i);
            if (module.isEnabled() != state[i]) module.setEnabledQuiet(state[i]);
        }
    }
}
