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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * Draws a wireframe on the block under your crosshair while you mine it. The
 * outline shifts from the start colour to the end colour as breaking progress
 * advances. This is a HUD effect only - digging itself is untouched.
 */
public class BreakIndicatorsModule extends Module implements HudOverlay {
    private final NumberSetting colorStart = new NumberSetting("Color Start", 0x67E8F9, 0, 0xFFFFFF)
        .description("Outline colour at the beginning of a break.");
    private final NumberSetting colorEnd = new NumberSetting("Color End", 0xFBBF24, 0, 0xFFFFFF)
        .description("Outline colour when the block is nearly broken.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Thickness of the outline strokes in pixels.");
    private final BooleanSetting fill = new BooleanSetting("Fill", true)
        .description("Adds a faint translucent body inside the outline.");

    private BlockPos breakingPos;
    private float progress;

    public BreakIndicatorsModule() {
        super("Break Indicators", "Outlines the block you are mining and colours it by break progress. A progress bar for your commitment issues.",
            Category.RENDER);
        addSetting(colorStart);
        addSetting(colorEnd);
        addSetting(lineWidth);
        addSetting(fill);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        breakingPos = null;
        progress = 0.0f;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null || client.interactionManager == null) {
            breakingPos = null;
            progress = 0.0f;
            return;
        }
        if (client.interactionManager.isBreakingBlock()
            && client.crosshairTarget instanceof BlockHitResult hit
            && hit.getType() == HitResult.Type.BLOCK) {
            breakingPos = hit.getBlockPos();
            // The interaction manager counts 0..9 in this Minecraft version.
            progress = Math.max(0.0f, Math.min(1.0f, client.interactionManager.getBlockBreakingProgress() / 9.0f));
        } else {
            breakingPos = null;
            progress = 0.0f;
        }
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || breakingPos == null || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        int rgb = SlEspStyle.blend(colorStart.getColorValue(), colorEnd.getColorValue(), progress);
        int outline = SlEspStyle.argb(240, rgb);
        int body = fill.isEnabled() ? SlEspStyle.argb(42, rgb) : 0;
        BlockPos pos = breakingPos;
        OverlayRender.box3DFilled(context,
            pos.getX(), pos.getY(), pos.getZ(),
            pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0,
            outline, body, Math.max(1, (int) lineWidth.getValue()), false);
    }
}
