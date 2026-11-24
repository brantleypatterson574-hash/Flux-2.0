package com.yourname.utilmod.ui;

/** Selectable menu shell for the Flux 2.0 menu. */
public enum MenuLayout {
    DROPDOWN("Dropdown"),
    CLICK_GUI("ClickGUI");

    private final String label;

    MenuLayout(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
