package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Client -> server configuration for the server-authoritative AntiHunger check. */
public record AntiHungerPayload(boolean enabled, float exhaustionAmount,
                                boolean whileSneakingOnly, boolean whileMovingOnly,
                                boolean whileFlyingOnly) implements CustomPayload {
    public static final Id<AntiHungerPayload> ID =
        new Id<>(Identifier.of("utilmod", "anti_hunger"));

    public static final PacketCodec<RegistryByteBuf, AntiHungerPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> {
                buf.writeBoolean(payload.enabled());
                buf.writeFloat(payload.exhaustionAmount());
                buf.writeBoolean(payload.whileSneakingOnly());
                buf.writeBoolean(payload.whileMovingOnly());
                buf.writeBoolean(payload.whileFlyingOnly());
            },
            buf -> new AntiHungerPayload(
                buf.readBoolean(), buf.readFloat(),
                buf.readBoolean(), buf.readBoolean(), buf.readBoolean()
            )
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
