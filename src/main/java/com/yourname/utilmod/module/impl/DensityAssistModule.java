package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import net.minecraft.client.gui.DrawContext;

/** Small readout of your fall distance and the mace smash bonus it would give. */
public class DensityAssistModule extends Module implements HudOverlay {
    private final NumberSetting minFall = new NumberSetting("Ready Distance", 1.5, 0.5, 20.0)
        .description("Fall distance (blocks) at which the readout turns green. A mace smash needs more than 1.5.");
    private final BooleanSetting showDamage = new BooleanSetting("Show Bonus Damage", true)
        .description("Shows the extra smash damage from the current fall distance.");
    private final BooleanSetting background = new BooleanSetting("Background", true).description("Draws a shaded panel behind the fall-damage readout.");
    private final NumberSetting x = new NumberSetting("X", 4.0, 0.0, 1000.0).advanced().description("Horizontal position of the readout on screen.");
    private final NumberSetting y = new NumberSetting("Y", 120.0, 0.0, 1000.0).advanced().description("Vertical position of the readout on screen.");

    public DensityAssistModule() {
        super("Density Assist", "Shows your fall distance and the mace smash bonus damage it would deal.", Category.MACE);
        addSetting(minFall);
        addSetting(showDamage);
        addSetting(background);
        addSetting(x);
        addSetting(y);
    }

    /** Vanilla smash bonus: 4 per block for 3 blocks, 2 per block for the next 5, then 1 per block. */
    private static double smashBonus(double fall) {
        double first = Math.min(fall, 3.0) * 4.0;
        double second = Math.max(0.0, Math.min(fall - 3.0, 5.0)) * 2.0;
        double third = Math.max(0.0, fall - 8.0);
        return first + second + third;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.player == null || mc.options.hudHidden) return;
        double fall = mc.player.fallDistance;
        boolean ready = fall > minFall.getValue();

        String line1 = String.format("Fall: %.1f blocks", fall);
        String line2 = showDamage.isEnabled() ? String.format("Smash bonus: +%.1f", smashBonus(fall)) : null;

        int px = (int) x.getValue();
        int py = (int) y.getValue();
        int width = mc.textRenderer.getWidth(line1);
        if (line2 != null) width = Math.max(width, mc.textRenderer.getWidth(line2));
        int height = line2 != null ? 24 : 12;
        if (background.isEnabled()) {
            context.fill(px - 4, py - 4, px + width + 4, py + height, 0x99000000);
        }
        int color = ready ? 0xFF3DDC84 : 0xFFFFFFFF;
        OverlayRender.text(context, line1, px, py, color);
        if (line2 != null) OverlayRender.text(context, line2, px, py + 12, 0xFFB4B4B8);
    }
}
