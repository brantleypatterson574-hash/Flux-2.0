package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.network.ReachPayload;
import com.yourname.utilmod.server.ReachManager;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Flux-compatible port of Meteor's Reach module.
 *
 * <p>Keeps the two original settings, but mirrors them to the private server so
 * the server-side attack check accepts the same extra reach. On vanilla servers
 * the block value only affects client-side block/entity interactions.</p>
 */
public class ReachModule extends Module {
    private final NumberSetting extraBlockReach = new NumberSetting("Extra Block Reach", 0.0, 0.0, 1.0).description("Extra distance for block interactions. A server running this mod can accept the longer reach.");
    private final NumberSetting extraEntityReach = new NumberSetting("Extra Entity Reach", 0.0, 0.0, 1.0).description("Extra distance for entity interactions. A server running this mod can accept the longer reach.");

    private ClientPlayerEntity lastPlayer;
    private double lastBlock = -1.0;
    private double lastEntity = -1.0;
    private double lastSentBlock = -1.0;
    private double lastSentEntity = -1.0;
    private boolean synced;

    public ReachModule() {
        super("Reach",
            "Gives you super long arms. Extra block reach affects client-side interactions; extra entity reach depends on what the server accepts.",
            Category.PLAYER);
        addSetting(extraBlockReach);
        addSetting(extraEntityReach);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        resetState();
        applyNow();
    }

    @Override
    public void onDisable() {
        ReachManager.clearLocal();
        send(0.0, 0.0);
        resetState();
    }

    private void resetState() {
        lastBlock = -1.0;
        lastEntity = -1.0;
        lastSentBlock = -1.0;
        lastSentEntity = -1.0;
        synced = false;
    }

    private void applyNow() {
        if (mc.player == null) return;
        double block = extraBlockReach.getValue();
        double entity = extraEntityReach.getValue();
        ReachManager.setLocalReach(block, entity, mc.player.getUuid());
        lastBlock = block;
        lastEntity = entity;
    }

    private void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) {
            lastPlayer = null;
            synced = false;
            return;
        }
        if (player != lastPlayer) {
            lastPlayer = player;
            resetState();
        }
        if (!isEnabled()) return;

        double block = extraBlockReach.getValue();
        double entity = extraEntityReach.getValue();

        if (block != lastBlock || entity != lastEntity) {
            ReachManager.setLocalReach(block, entity, player.getUuid());
            lastBlock = block;
            lastEntity = entity;
        }

        if (!synced || block != lastSentBlock || entity != lastSentEntity) {
            send(block, entity);
        }
    }

    private void send(double block, double entity) {
        if (mc.getNetworkHandler() == null) return;
        if (!ClientPlayNetworking.canSend(ReachPayload.ID)) return;
        ClientPlayNetworking.send(new ReachPayload(block, entity));
        lastSentBlock = block;
        lastSentEntity = entity;
        synced = true;
    }
}
