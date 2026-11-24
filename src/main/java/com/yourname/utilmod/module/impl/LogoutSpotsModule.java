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
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Remembers where other players were last seen before they disappeared from
 * the client world. Only positions already known to the client are used; the
 * module never reads inbound packets or changes server state.
 */
public class LogoutSpotsModule extends Module implements HudOverlay {
    private record Snapshot(UUID uuid, String name, double x, double y, double z) {}

    private final NumberSetting range = new NumberSetting("Range", 96.0, 16.0, 256.0)
        .description("How far away a player can be when they disappear from your client world.");
    private final NumberSetting maximumSpots = new NumberSetting("Maximum Spots", 16.0, 2.0, 64.0)
        .description("How many remembered logout positions to keep on screen at once.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 10.0, 2.0, 60.0)
        .description("Ticks between checks of the player list; the marker stays visible between checks.");
    private final NumberSetting color = new NumberSetting("Color", 0xF9A8D4, 0x000000, 0xFFFFFF)
        .description("Colour of the marker outline and label.");
    private final BooleanSetting showName = new BooleanSetting("Show Name", true)
        .description("Shows the player's name above their last known position.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0)
        .description("Thickness of the marker outline in screen pixels.");

    private final Map<UUID, Snapshot> lastSeen = new HashMap<>();
    private final List<Snapshot> spots = new ArrayList<>();
    private ClientWorld lastWorld;
    private int tick;

    public LogoutSpotsModule() {
        super("Logout Spots", "Marks the last known position of players who vanish nearby. Visual only. The client remembers the good times.",
            Category.RENDER);
        addSetting(range);
        addSetting(maximumSpots);
        addSetting(scanInterval);
        addSetting(color);
        addSetting(showName);
        addSetting(lineWidth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        clearState();
    }

    private void clearState() {
        lastSeen.clear();
        spots.clear();
        lastWorld = null;
        tick = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null || client.player == null) {
            clearState();
            return;
        }
        if (client.world != lastWorld) {
            clearState();
            lastWorld = client.world;
        }
        if (tick++ % Math.max(2, (int) scanInterval.getValue()) != 0) return;
        scan(client);
    }

    private void scan(MinecraftClient client) {
        double radius = range.getValue();
        double radiusSquared = radius * radius;
        Map<UUID, Snapshot> current = new HashMap<>();
        Set<UUID> presentInWorld = new HashSet<>();

        // Track everyone still in the world first. A player who merely walks out
        // of our display range must not be mistaken for someone who logged out.
        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            if (player == client.player) continue;
            presentInWorld.add(player.getUuid());
            current.put(player.getUuid(), new Snapshot(player.getUuid(), player.getName().getString(),
                player.getX(), player.getY(), player.getZ()));
        }

        for (Map.Entry<UUID, Snapshot> entry : lastSeen.entrySet()) {
            if (presentInWorld.contains(entry.getKey())) continue;
            Snapshot snapshot = entry.getValue();
            double dx = snapshot.x() - client.player.getX();
            double dy = snapshot.y() - client.player.getY();
            double dz = snapshot.z() - client.player.getZ();
            if (dx * dx + dy * dy + dz * dz <= radiusSquared) remember(snapshot);
        }

        lastSeen.clear();
        lastSeen.putAll(current);
    }

    /** Avoids stacking several markers for the same player at the same place. */
    private void remember(Snapshot snapshot) {
        for (int i = spots.size() - 1; i >= 0; i--) {
            Snapshot existing = spots.get(i);
            double dx = existing.x() - snapshot.x();
            double dy = existing.y() - snapshot.y();
            double dz = existing.z() - snapshot.z();
            if (existing.uuid().equals(snapshot.uuid()) && dx * dx + dy * dy + dz * dz < 1.0) {
                spots.remove(i);
                break;
            }
        }
        spots.add(snapshot);
        int cap = Math.max(2, (int) maximumSpots.getValue());
        while (spots.size() > cap) spots.remove(0);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || spots.isEmpty() || mc.world == null || mc.player == null
            || mc.options.hudHidden) return;

        int rgb = color.getColorValue();
        int width = Math.max(1, (int) lineWidth.getValue());
        int[] projected = new int[3];

        for (Snapshot spot : spots) {
            OverlayRender.box3D(context,
                spot.x() - 0.35, spot.y(), spot.z() - 0.35,
                spot.x() + 0.35, spot.y() + 2.0, spot.z() + 0.35,
                SlEspStyle.argb(225, rgb), width, false);

            if (showName.isEnabled()
                && OverlayRender.projectInto(spot.x(), spot.y() + 2.25, spot.z(), projected)) {
                OverlayRender.text(context, spot.name(), projected[0], projected[1] - 2,
                    SlEspStyle.argb(235, rgb));
            }
        }
    }
}
