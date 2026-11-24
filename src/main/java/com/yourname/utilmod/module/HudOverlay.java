package com.yourname.utilmod.module;

import net.minecraft.client.gui.DrawContext;

/** Marker for modules that draw a screen-space overlay through the HUD registry. */
public interface HudOverlay {
    void renderOverlay(DrawContext context);
}
