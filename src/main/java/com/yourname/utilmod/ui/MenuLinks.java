package com.yourname.utilmod.ui;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;

/** Safe, confirm-first links shared by both Onyx menu layouts. */
public final class MenuLinks {
    public static final String PROFILE_URL = "https://guns.lol/kitxxmod";

    private MenuLinks() {}

    /** Minecraft's confirmation screen is used instead of opening the browser directly. */
    public static void openProfile(Screen parent) {
        ConfirmLinkScreen.open(parent, PROFILE_URL, true);
    }
}
