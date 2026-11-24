package com.yourname.utilmod.setting;

import java.util.Locale;

/**
 * A setting that cycles through the constants of an enum.
 * Left-click cycles forward in the dropdown menu, right-click resets it.
 */
public class EnumSetting<T extends Enum<T>> extends Setting {
    private final T[] values;
    private final String[] displayValues;
    private final T defaultValue;
    private T value;

    public EnumSetting(String name, Class<T> enumType, T defaultValue) {
        super(name);
        this.values = enumType.getEnumConstants();
        // Enum constants never change. Format every label once instead of
        // lowercasing, splitting and building a StringBuilder on each HUD frame.
        this.displayValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            displayValues[i] = format(values[i]);
        }
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public T getValue() {
        return value;
    }

    /** All options, for dropdown-style renderers. Returns a defensive copy. */
    public T[] getValues() {
        return values.clone();
    }

    /**
     * All options without cloning. Enum constants never change; callers must not
     * mutate the returned array. Used by the menu popup's per-frame rendering.
     */
    public T[] values() {
        return values;
    }

    /** Preformatted label for one option index, used by the menu popup. */
    public String getDisplayValue(int index) {
        return displayValues[index];
    }

    public void setValue(T value) {
        if (value != null) this.value = value;
    }

    public void cycle() {
        value = values[(value.ordinal() + 1) % values.length];
    }

    public void cycleBack() {
        value = values[(value.ordinal() - 1 + values.length) % values.length];
    }

    @Override
    public void reset() {
        value = defaultValue;
    }

    /** Human-readable name, e.g. TOP_RIGHT -> "Top Right". */
    public String getDisplayValue() {
        return displayValues[value.ordinal()];
    }

    private static String format(Enum<?> value) {
        // Enums that expose a human label through toString() keep their exact
        // spelling. Ordinary enums still get the old NAME_WITH_UNDERSCORES format.
        String raw = value.toString();
        if (raw.equals(value.name())) raw = raw.toLowerCase(Locale.ROOT);
        String[] parts = raw.replace('_', ' ').trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (builder.length() > 0) builder.append(' ');
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }

    @Override
    protected String getDefaultDescription() {
        return "Click to open the list of " + values.length + " options. Shift-click steps backward. Right-click resets.";
    }
}
