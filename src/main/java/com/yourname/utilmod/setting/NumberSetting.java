package com.yourname.utilmod.setting;

public class NumberSetting extends Setting {
    private final double defaultValue;
    private final double min, max;
    private double value;
    private String unit = "";

    public NumberSetting(String name, double defaultValue, double min, double max) {
        super(name);
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.value = defaultValue;
    }

    public double getValue() {
        return value;
    }

    public float getFloat() {
        return (float) value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setValue(double value) {
        // A NaN/Infinity from a hand-edited config would poison slider math in the GUI.
        if (!Double.isFinite(value)) return;
        double clamped = Math.max(min, Math.min(max, value));
        // Percentages are whole steps so the slider and the label stay in sync.
        if ("%".equals(unit)) clamped = Math.round(clamped);
        this.value = clamped;
    }

    @Override
    public void reset() {
        setValue(defaultValue);
    }

    /** Appends a compact unit (for example "%") to the value shown in the menu. */
    public NumberSetting unit(String unit) {
        this.unit = unit == null ? "" : unit;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    /** Formats the value for display, including any unit suffix. */
    public String getDisplayValue() {
        if (isColor()) return String.format("#%06X", getColorValue());
        return formatValue() + unit;
    }

    /** Formats the value for the double-click editor, without a display unit. */
    public String getEditValue() {
        if (isColor()) return getDisplayValue();
        return formatValue();
    }

    private String formatValue() {
        // Percentages are whole numbers even when the slider span is wide.
        boolean whole = "%".equals(unit)
            || (min == Math.floor(min) && max == Math.floor(max) && max - min <= 20.0);
        return whole ? String.format("%.0f", value) : String.format("%.1f", value);
    }

    /** True for the 0x000000-0xFFFFFF colour settings, which get a swatch + hex UI. */
    public boolean isColor() {
        return min >= 0.0 && max >= 0xFFFFFF && min < 1.0;
    }

    /** The current value as a packed RGB integer. */
    public int getColorValue() {
        return ((int) value) & 0xFFFFFF;
    }

    @Override
    protected String getDefaultDescription() {
        if (isColor()) {
            return "Pick a colour. Double-click to type a hex value such as 0x22D3EE, or drag the slider.";
        }
        boolean wholeRange = min == Math.floor(min) && max == Math.floor(max);
        String range = wholeRange
            ? String.format("Range: %,d%s to %,d%s.", (long) min, unit, (long) max, unit)
            : String.format("Range: %.2f%s to %.2f%s.", min, unit, max, unit);
        return range + " Drag the slider, or double-click to type an exact value. Right-click resets.";
    }
}
