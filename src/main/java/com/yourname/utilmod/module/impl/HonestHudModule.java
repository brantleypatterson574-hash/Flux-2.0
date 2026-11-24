package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * A deliberately truthful HUD. It reports ordinary local values with labels
 * that refuse to pretend the client knows something the server did not say.
 */
public class HonestHudModule extends Module implements HudOverlay {
    private final BooleanSetting showPosition = new BooleanSetting("Show Position", true)
        .description("Shows your real block coordinates.");
    private final BooleanSetting showHealth = new BooleanSetting("Show Health", true)
        .description("Shows local health out of your actual maximum health.");
    private final BooleanSetting showPing = new BooleanSetting("Show Ping", true)
        .description("Shows the latency reported for your own player list entry.");
    private final BooleanSetting showDimension = new BooleanSetting("Show Dimension", true)
        .description("Shows the dimension name from your current world.");
    private final BooleanSetting showHonestNotes = new BooleanSetting("Show Honest Notes", true)
        .description("Adds small reminders about what the client can and cannot know.");
    private final NumberSetting x = new NumberSetting("X", 4.0, 0.0, 1000.0).advanced()
        .description("Horizontal position of the HUD panel, in screen pixels.");
    private final NumberSetting y = new NumberSetting("Y", 4.0, 0.0, 1000.0).advanced()
        .description("Vertical position of the HUD panel, in screen pixels.");
    private final NumberSetting spacing = new NumberSetting("Line Spacing", 11.0, 8.0, 24.0)
        .description("Pixels between HUD lines.");
    private final NumberSetting interval = new NumberSetting("Update Interval", 10.0, 1.0, 60.0)
        .unit("ticks")
        .description("Ticks between refreshes. Higher values make the HUD cheaper to draw.");
    private final BooleanSetting background = new BooleanSetting("Background", true)
        .description("Draws a dark panel behind the text for readability.");

    private final List<String> lines = new ArrayList<>();
    private int width;
    private int counter;

    public HonestHudModule() {
        super("Honest HUD", "A tiny HUD that reports only what your client actually knows, with a few reminders about the rest.",
            Category.HUD);
        addSetting(showPosition);
        addSetting(showHealth);
        addSetting(showPing);
        addSetting(showDimension);
        addSetting(showHonestNotes);
        addSetting(x);
        addSetting(y);
        addSetting(spacing);
        addSetting(interval);
        addSetting(background);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (++counter < Math.max(1, (int) interval.getValue())) return;
        counter = 0;
        lines.clear();

        if (showPosition.isEnabled()) {
            BlockPos pos = client.player.getBlockPos();
            lines.add("XYZ  " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ() + "  (actually here)");
        }
        if (showHealth.isEnabled()) {
            lines.add(String.format("Health  %.1f / %.1f  (yes, really)",
                client.player.getHealth(), client.player.getMaxHealth()));
            lines.add("Air  " + client.player.getAir() + " / " + client.player.getMaxAir());
        }
        if (showPing.isEnabled()) {
            PlayerListEntry entry = client.player.networkHandler == null ? null
                : client.player.networkHandler.getPlayerListEntry(client.player.getUuid());
            lines.add("Ping  " + (entry == null ? "?" : entry.getLatency() + " ms")
                + "  (server's number)");
        }
        if (showDimension.isEnabled()) {
            lines.add("Dimension  " + client.world.getRegistryKey().getValue().getPath());
        }
        if (showHonestNotes.isEnabled()) {
            lines.add("Luck  " + (client.player.experienceLevel % 2 == 0 ? "unmeasured" : "also unmeasured"));
            lines.add("Skill  not detected by thismod");
        }

        width = 0;
        for (String line : lines) width = Math.max(width, client.textRenderer.getWidth(line));
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden || lines.isEmpty()) return;
        int left = (int) x.getValue();
        int top = (int) y.getValue();
        int step = Math.max(8, (int) spacing.getValue());
        int height = lines.size() * step;

        if (background.isEnabled()) {
            context.fill(left - 4, top - 4, left + width + 6, top + height + 2, 0x990C0D12);
            context.fill(left - 4, top - 4, left - 2, top + height + 2, 0xFF67E8F9);
        }
        for (int i = 0; i < lines.size(); i++) {
            OverlayRender.text(context, lines.get(i), left, top + i * step, 0xFFFFFFFF);
        }
    }

    @Override
    public void onDisable() {
        lines.clear();
        counter = 0;
    }
}
