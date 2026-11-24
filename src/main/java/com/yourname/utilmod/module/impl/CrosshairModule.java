package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import net.minecraft.client.gui.DrawContext;

/** Client-side crosshair restyle. Purely local; it never changes what is targeted. */
public class CrosshairModule extends Module implements HudOverlay {
    public enum Style { DOT, PLUS, CROSS }

    private final EnumSetting<Style> style = new EnumSetting<>("Style", Style.class, Style.PLUS).description("Crosshair shape: dot, plus, or diagonal cross.");
    private final NumberSetting size = new NumberSetting("Size", 6.0, 2.0, 24.0).description("Length of each crosshair arm in screen pixels.");
    private final NumberSetting gap = new NumberSetting("Gap", 3.0, 0.0, 16.0).description("Empty space between the centre and the crosshair arms.");
    private final NumberSetting thickness = new NumberSetting("Thickness", 1.0, 1.0, 4.0).description("Thickness of the crosshair lines in pixels.");
    private final NumberSetting color = new NumberSetting("Color", 0xFFFFFF, 0x000000, 0xFFFFFF).description("Colour of the custom crosshair.");
    private final NumberSetting offsetX = new NumberSetting("Offset X", 0.0, -200.0, 200.0).advanced().description("Moves the crosshair left or right on screen.");
    private final NumberSetting offsetY = new NumberSetting("Offset Y", 0.0, -200.0, 200.0).advanced().description("Moves the crosshair up or down on screen.");

    public CrosshairModule() {
        super("Crosshair", "Draws a custom client-side crosshair style.", Category.RENDER);
        addSetting(style);
        addSetting(size);
        addSetting(gap);
        addSetting(thickness);
        addSetting(color);
        addSetting(offsetX);
        addSetting(offsetY);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden) return;
        int cx = context.getScaledWindowWidth() / 2 + (int) offsetX.getValue();
        int cy = context.getScaledWindowHeight() / 2 + (int) offsetY.getValue();
        int rgb = 0xFF000000 | ((int) color.getValue() & 0xFFFFFF);
        int half = Math.max(1, (int) size.getValue());
        int space = Math.max(0, (int) gap.getValue());
        int t = Math.max(1, (int) thickness.getValue());

        switch (style.getValue()) {
            case DOT -> context.fill(cx - t, cy - t, cx + t, cy + t, rgb);
            case PLUS -> {
                OverlayRender.line(context, cx - space - half, cy, cx - space, cy, rgb, t);
                OverlayRender.line(context, cx + space, cy, cx + space + half, cy, rgb, t);
                OverlayRender.line(context, cx, cy - space - half, cx, cy - space, rgb, t);
                OverlayRender.line(context, cx, cy + space, cx, cy + space + half, rgb, t);
            }
            case CROSS -> {
                OverlayRender.line(context, cx - space - half, cy - space - half,
                    cx - space, cy - space, rgb, t);
                OverlayRender.line(context, cx + space, cy + space,
                    cx + space + half, cy + space + half, rgb, t);
                OverlayRender.line(context, cx + space, cy - space - half,
                    cx + space + half, cy - space, rgb, t);
                OverlayRender.line(context, cx - space - half, cy + space,
                    cx - space, cy + space + half, rgb, t);
            }
        }
    }
}
