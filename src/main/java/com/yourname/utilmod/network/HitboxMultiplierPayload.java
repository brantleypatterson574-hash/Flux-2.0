package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Sent from the client to the server when the HitBoxes size multiplier changes. */
public record HitboxMultiplierPayload(double multiplier) implements CustomPayload {
    public static final Id<HitboxMultiplierPayload> ID =
        new Id<>(Identifier.of("utilmod", "hitbox_multiplier"));

    public static final PacketCodec<RegistryByteBuf, HitboxMultiplierPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> buf.writeDouble(payload.multiplier()),
            buf -> new HitboxMultiplierPayload(buf.readDouble())
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
