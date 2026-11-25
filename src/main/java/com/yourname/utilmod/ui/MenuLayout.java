package com.yourname.utilmod.ui;

/** Selectable menu shell for the Onyx Client menu. */
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
