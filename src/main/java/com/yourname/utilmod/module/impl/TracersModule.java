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
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Draws short-lived guide lines from the middle of the screen to nearby entities. */
public class TracersModule extends Module implements HudOverlay {
    private final NumberSetting range = new NumberSetting("Range", 48.0, 8.0, 160.0)
        .description("Only entities within this distance get a tracer line.");
    private final NumberSetting maxEntities = new NumberSetting("Maximum Entities", 64.0, 8.0, 200.0)
        .description("Caps how many tracer lines can be drawn at once.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 3.0, 1.0, 30.0)
        .description("Ticks between target scans. Existing lines stay visible between scans.");
    private final BooleanSetting showPlayers = new BooleanSetting("Show Players", true)
        .description("Draws tracer lines toward other players.");
    private final BooleanSetting showHostiles = new BooleanSetting("Show Hostiles", true)
        .description("Draws tracer lines toward hostile mobs.");
    private final BooleanSetting showItems = new BooleanSetting("Show Items", true)
        .description("Draws tracer lines toward dropped items.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 5.0)
        .description("Thickness of each tracer line in screen pixels.");
    private final NumberSetting color = new NumberSetting("Color", 0x67E8F9, 0, 0xFFFFFF)
        .description("Colour of the tracer lines.");

    private final List<Entity> targets = new ArrayList<>();
    private int tick;

    public TracersModule() {
        super("Tracers", "Draws client-side lines from your crosshair toward chosen entities nearby. Lines of mystery, mostly.",
            Category.RENDER);
        addSetting(range);
        addSetting(maxEntities);
        addSetting(scanInterval);
        addSetting(showPlayers);
        addSetting(showHostiles);
        addSetting(showItems);
        addSetting(lineWidth);
        addSetting(color);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        targets.clear();
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            targets.clear();
            tick = 0;
            return;
        }
        if (tick++ % Math.max(1, (int) scanInterval.getValue()) != 0) return;
        targets.clear();

        double r = range.getValue();
        double rSq = r * r;
        Box area = new Box(
            client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
        );

        List<Entity> found = client.world.getOtherEntities(client.player, area,
            entity -> entity != null && !entity.isRemoved() && !entity.isSpectator()
                && client.player.squaredDistanceTo(entity) <= rSq && isWanted(entity));
        found.sort(Comparator.comparingDouble(client.player::squaredDistanceTo));
        int cap = Math.max(1, (int) maxEntities.getValue());
        for (int i = 0; i < Math.min(cap, found.size()); i++) {
            targets.add(found.get(i));
        }
    }

    private boolean isWanted(Entity entity) {
        if (entity instanceof ItemEntity) return showItems.isEnabled();
        if (entity instanceof PlayerEntity) return showPlayers.isEnabled();
        return entity instanceof HostileEntity && showHostiles.isEnabled();
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || targets.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int centerX = mc.getWindow().getScaledWidth() / 2;
        int centerY = mc.getWindow().getScaledHeight() / 2;
        int rgb = color.getColorValue();
        int thickness = Math.max(1, (int) lineWidth.getValue());
        double rSq = (range.getValue() + 2.0) * (range.getValue() + 2.0);

        for (Entity entity : targets) {
            if (entity.isRemoved() || mc.player.squaredDistanceTo(entity) > rSq) continue;
            int[] projected = OverlayRender.project(
                entity.getX(), entity.getY() + entity.getHeight() * 0.5, entity.getZ());
            if (projected == null) continue;

            OverlayRender.line(context, centerX, centerY, projected[0], projected[1],
                SlEspStyle.argb(220, rgb), thickness);

            String label = entity instanceof ItemEntity item
                ? item.getStack().getName().getString()
                : entity.getName().getString();
            label += "  " + OverlayRender.meters(mc.player.distanceTo(entity));
            OverlayRender.text(context, label, projected[0] + 5, projected[1] - 9,
                SlEspStyle.argb(235, rgb));
        }
    }
}
