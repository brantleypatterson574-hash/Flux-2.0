package com.yourname.utilmod.setting;

public class BooleanSetting extends Setting {
    private final boolean defaultEnabled;
    private boolean enabled;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name);
        this.defaultEnabled = defaultValue;
        this.enabled = defaultValue;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    @Override
    public void reset() {
        this.enabled = defaultEnabled;
    }

    @Override
    protected String getDefaultDescription() {
        return (defaultEnabled ? "Enabled" : "Disabled") + " by default. Click to toggle; right-click to reset.";
    }
}
