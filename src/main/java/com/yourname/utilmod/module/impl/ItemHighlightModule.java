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
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Marks dropped items with small screen-space boxes and optional labels. */
public class ItemHighlightModule extends Module implements HudOverlay {
    private final NumberSetting range = new NumberSetting("Range", 40.0, 6.0, 160.0)
        .description("Only dropped items within this distance are outlined.");
    private final NumberSetting color = new NumberSetting("Color", 0xFFD166, 0, 0xFFFFFF)
        .description("Colour of the item outline and label.");
    private final BooleanSetting showName = new BooleanSetting("Show Name", true)
        .description("Shows the item name above each outline.");
    private final BooleanSetting showDistance = new BooleanSetting("Show Distance", false)
        .description("Adds the distance to the item next to its name.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of each outline in screen pixels.");
    private final BooleanSetting glow = new BooleanSetting("Glow", true)
        .description("Adds a soft halo around each item outline.");

    private final List<ItemEntity> drops = new ArrayList<>();
    private int tick;

    public ItemHighlightModule() {
        super("Item Highlight", "Outlines dropped items nearby so they are easier to spot. Ground loot, but louder.",
            Category.RENDER);
        addSetting(range);
        addSetting(color);
        addSetting(showName);
        addSetting(showDistance);
        addSetting(lineWidth);
        addSetting(glow);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        drops.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            drops.clear();
            tick = 0;
            return;
        }
        if (tick++ % 3 != 0) return;
        drops.clear();

        double r = range.getValue();
        double rSq = r * r;
        Box area = new Box(
            client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
        );
        drops.addAll(client.world.getEntitiesByClass(ItemEntity.class, area,
            item -> !item.isRemoved() && client.player.squaredDistanceTo(item) <= rSq));
        drops.sort(Comparator.comparingDouble(client.player::squaredDistanceTo));
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || drops.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int thickness = Math.max(1, (int) lineWidth.getValue());
        double rSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);
        boolean useGlow = glow.isEnabled();

        for (ItemEntity item : drops) {
            if (item.isRemoved() || mc.player.squaredDistanceTo(item) > rSq) continue;
            int[] bounds = OverlayRender.entityBounds(item, 0.08f);
            if (bounds == null) continue;

            if (useGlow) {
                OverlayRender.glowBox(context, bounds[0], bounds[1], bounds[2], bounds[3],
                    rgb, thickness);
            } else {
                OverlayRender.box(context, bounds[0], bounds[1], bounds[2], bounds[3],
                    SlEspStyle.argb(235, rgb), thickness);
            }

            if (!showName.isEnabled() && !showDistance.isEnabled()) continue;
            String label = "";
            if (showName.isEnabled()) {
                label = item.getStack().getName().getString();
            }
            if (showDistance.isEnabled()) {
                label += label.isEmpty()
                    ? OverlayRender.meters(mc.player.distanceTo(item))
                    : "  " + OverlayRender.meters(mc.player.distanceTo(item));
            }
            OverlayRender.text(context, label, bounds[0], bounds[1] - 10,
                SlEspStyle.argb(240, rgb));
        }
    }
}
