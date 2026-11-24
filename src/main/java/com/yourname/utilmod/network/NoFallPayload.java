package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Client -> server configuration for the server-authoritative NoFall check. */
public record NoFallPayload(boolean enabled, int mode, float minDistance, float minimizedDistance,
                            boolean onlyWhenFallingFast, float fallSpeedThreshold,
                            boolean onlyBelowHealth, float healthThreshold) implements CustomPayload {
    public static final Id<NoFallPayload> ID =
        new Id<>(Identifier.of("utilmod", "no_fall"));

    public static final PacketCodec<RegistryByteBuf, NoFallPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> {
                buf.writeBoolean(payload.enabled());
                buf.writeVarInt(payload.mode());
                buf.writeFloat(payload.minDistance());
                buf.writeFloat(payload.minimizedDistance());
                buf.writeBoolean(payload.onlyWhenFallingFast());
                buf.writeFloat(payload.fallSpeedThreshold());
                buf.writeBoolean(payload.onlyBelowHealth());
                buf.writeFloat(payload.healthThreshold());
            },
            buf -> new NoFallPayload(
                buf.readBoolean(), buf.readVarInt(),
                buf.readFloat(), buf.readFloat(),
                buf.readBoolean(), buf.readFloat(),
                buf.readBoolean(), buf.readFloat()
            )
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
