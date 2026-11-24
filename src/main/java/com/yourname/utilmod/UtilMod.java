package com.yourname.utilmod;

import com.yourname.utilmod.network.AntiCheatAlertPayload;
import com.yourname.utilmod.network.AntiHungerPayload;
import com.yourname.utilmod.network.FastMinePayload;
import com.yourname.utilmod.network.HitboxMultiplierPayload;
import com.yourname.utilmod.network.NoFallPayload;
import com.yourname.utilmod.network.NoKnockbackPayload;
import com.yourname.utilmod.network.ReachPayload;
import com.yourname.utilmod.server.AntiCheatManager;
import com.yourname.utilmod.server.AntiHungerManager;
import com.yourname.utilmod.server.FastMineManager;
import com.yourname.utilmod.server.HitboxManager;
import com.yourname.utilmod.server.NoFallManager;
import com.yourname.utilmod.server.NoKnockbackManager;
import com.yourname.utilmod.server.ReachManager;
import net.fabricmc.api.ModInitializer;

import java.util.UUID;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * Common entrypoint. Runs for the client, the integrated server and the
 * dedicated server, so payload registration works in singleplayer too.
 */
public class UtilMod implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(
            HitboxMultiplierPayload.ID,
            HitboxMultiplierPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
            ReachPayload.ID,
            ReachPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
            NoKnockbackPayload.ID,
            NoKnockbackPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
            FastMinePayload.ID,
            FastMinePayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
            AntiHungerPayload.ID,
            AntiHungerPayload.CODEC
        );
        PayloadTypeRegistry.playC2S().register(
            NoFallPayload.ID,
            NoFallPayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
            AntiCheatAlertPayload.ID,
            AntiCheatAlertPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
            HitboxMultiplierPayload.ID,
            (payload, context) -> context.server().execute(
                () -> HitboxManager.setPlayerMultiplier(context.player().getUuid(), payload.multiplier())
            )
        );
        ServerPlayNetworking.registerGlobalReceiver(
            ReachPayload.ID,
            (payload, context) -> context.server().execute(
                () -> ReachManager.setPlayerReach(
                    context.player().getUuid(),
                    payload.blockReach(),
                    payload.entityReach()
                )
            )
        );
        ServerPlayNetworking.registerGlobalReceiver(
            NoKnockbackPayload.ID,
            (payload, context) -> context.server().execute(
                () -> NoKnockbackManager.set(
                    context.player().getUuid(),
                    payload.horizontal(),
                    payload.vertical()
                )
            )
        );
        ServerPlayNetworking.registerGlobalReceiver(
            FastMinePayload.ID,
            (payload, context) -> context.server().execute(
                () -> FastMineManager.set(context.player().getUuid(), payload.multiplier())
            )
        );
        ServerPlayNetworking.registerGlobalReceiver(
            AntiHungerPayload.ID,
            (payload, context) -> context.server().execute(
                () -> AntiHungerManager.set(context.player().getUuid(), new AntiHungerManager.Config(
                    payload.enabled(), payload.exhaustionAmount(),
                    payload.whileSneakingOnly(), payload.whileMovingOnly(), payload.whileFlyingOnly()
                ))
            )
        );
        ServerPlayNetworking.registerGlobalReceiver(
            NoFallPayload.ID,
            (payload, context) -> context.server().execute(
                () -> NoFallManager.set(context.player().getUuid(), new NoFallManager.Config(
                    payload.enabled(), payload.mode(), payload.minDistance(), payload.minimizedDistance(),
                    payload.onlyWhenFallingFast(), payload.fallSpeedThreshold(),
                    payload.onlyBelowHealth(), payload.healthThreshold()
                ))
            )
        );

        // Per-player state must not leak to a different player who gets the same
        // UUID slot after a reconnect.
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.getPlayer() == null) return;
            UUID uuid = handler.getPlayer().getUuid();
            ReachManager.clear(uuid);
            HitboxManager.clear(uuid);
            NoKnockbackManager.clear(uuid);
            FastMineManager.clear(uuid);
            AntiHungerManager.clear(uuid);
            NoFallManager.clear(uuid);
            AntiCheatManager.clear(uuid);
        });
    }
}
