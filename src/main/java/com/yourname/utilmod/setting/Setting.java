package com.yourname.utilmod.setting;

/**
 * Base class for every menu setting. Settings carry a short description so
 * the menu can show a tooltip explaining what each one does - no more mystery
 * sliders. Modules may override the description with {@link #description(String)}.
 */
public abstract class Setting {
    private final String name;
    private String description;
    private boolean advanced;

    public Setting(String name) {
        this.name = name;
    }

    /**
     * Marks a setting as advanced placement detail. Advanced settings keep their
     * config key and value, but are tucked behind a collapsible row so the
     * settings panel stays clean by default. Intended for HUD positioning only.
     */
    @SuppressWarnings("unchecked")
    public <S extends Setting> S advanced() {
        this.advanced = true;
        return (S) this;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public String getName() {
        return name;
    }

    /** Attach a custom explanation shown as a tooltip in the settings panel. */
    @SuppressWarnings("unchecked")
    public <S extends Setting> S description(String text) {
        this.description = text;
        return (S) this;
    }

    /** The explanation shown in the GUI. Subtypes add helpful type-specific hints. */
    public String getDescription() {
        if (description != null && !description.isBlank()) return description;
        return getDefaultDescription();
    }

    /** Auto-generated explanation when a module did not supply one. */
    protected String getDefaultDescription() {
        return "Adjust the " + name + " setting. Right-click to reset it to default.";
    }

    /** Restores the value this setting was created with. */
    public abstract void reset();
}
