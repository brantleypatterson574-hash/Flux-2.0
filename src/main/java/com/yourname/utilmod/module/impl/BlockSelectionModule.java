package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

/**
 * Draws a custom outline around the block under the crosshair. The target is
 * read from the client's own crosshair result; the game's selection rendering
 * and all interactions are left untouched.
 */
public class BlockSelectionModule extends Module implements HudOverlay {
    private static final double EXPAND = 0.002;

    private final BooleanSetting fill = new BooleanSetting("Fill", true)
        .description("Adds a translucent tint inside the outline.");
    private final NumberSetting color = new NumberSetting("Color", 0xA7F3D0, 0x000000, 0xFFFFFF)
        .description("Colour of the outline around the block you are looking at.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Thickness of the outline in screen pixels.");
    private final BooleanSetting glow = new BooleanSetting("Glow", true)
        .description("Adds a soft halo around the outline.");

    private BlockPos target;
    private ClientWorld lastWorld;

    public BlockSelectionModule() {
        super("Block Selection", "Highlights the block you are looking at. Visual only. Even blocks deserve a spotlight.",
            Category.RENDER);
        addSetting(fill);
        addSetting(color);
        addSetting(lineWidth);
        addSetting(glow);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        target = null;
        lastWorld = null;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null) {
            target = null;
            lastWorld = null;
            return;
        }
        if (client.world != lastWorld) {
            lastWorld = client.world;
            target = null;
        }
        target = client.crosshairTarget instanceof BlockHitResult hit ? hit.getBlockPos() : null;
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || target == null || mc.world == null || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int outline = SlEspStyle.argb(235, rgb);
        int fillColor = fill.isEnabled() ? SlEspStyle.argb(42, rgb) : 0;

        OverlayRender.box3DFilled(context,
            target.getX() - EXPAND, target.getY() - EXPAND, target.getZ() - EXPAND,
            target.getX() + 1.0 + EXPAND, target.getY() + 1.0 + EXPAND, target.getZ() + 1.0 + EXPAND,
            outline, fillColor, Math.max(1, (int) lineWidth.getValue()), glow.isEnabled());
    }
}
