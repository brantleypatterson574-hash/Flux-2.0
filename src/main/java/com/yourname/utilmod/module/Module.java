package com.yourname.utilmod.module;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.ui.NotificationManager;
import com.yourname.utilmod.util.ConfigStore;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    // Incremented on every toggle so HUD caches can skip rebuilding their
    // enabled-module list until something actually changes.
    private static volatile long enabledVersion;

    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void toggle() {
        setEnabled(!this.enabled);
    }

    /**
     * Changes state without rewriting the whole config file. Used by profile
     * loading so one profile load produces one save instead of dozens.
     */
    public void setEnabledQuiet(boolean value) {
        setEnabledInternal(value, true);
    }

    /**
     * Restores a saved state at startup. Silent means no config write AND no
     * toggle toast - previously startup could spam the toast stack with every
     * module that was enabled in the config.
     */
    public void setEnabledForConfigLoad(boolean value) {
        setEnabledInternal(value, false);
    }

    private void setEnabledInternal(boolean value, boolean notify) {
        if (this.enabled == value) return;
        this.enabled = value;
        if (enabled) onEnable();
        else onDisable();
        if (notify) NotificationManager.moduleToggled(name, enabled);
        enabledVersion++;
    }

    public void setEnabled(boolean value) {
        if (this.enabled == value) return;
        setEnabledQuiet(value);
        ConfigStore.save(UtilModClient.getModules());
    }

    public static long enabledVersion() {
        return enabledVersion;
    }

    public void onEnable() {}
    public void onDisable() {}

    protected void addSetting(Setting setting) {
        settings.add(setting);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public List<Setting> getSettings() { return settings; }
}
