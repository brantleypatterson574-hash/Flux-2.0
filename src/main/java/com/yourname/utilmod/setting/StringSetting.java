package com.yourname.utilmod.setting;

/** A text value editable by double-clicking its menu row. */
public class StringSetting extends Setting {
    private String value;
    private final String defaultValue;

    public StringSetting(String name, String defaultValue) {
        super(name);
        this.value = defaultValue == null ? "" : defaultValue;
        this.defaultValue = this.value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? "" : value;
    }

    public String getDisplayValue() {
        return value.isEmpty() ? "<empty>" : value;
    }

    @Override
    public void reset() {
        value = defaultValue;
    }

    @Override
    protected String getDefaultDescription() {
        return "Click to type a value. Press Enter to save, ESC to cancel. Right-click resets.";
    }
}
