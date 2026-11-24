package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Sent C2S when the FastMine multiplier changes. */
public record FastMinePayload(float multiplier) implements CustomPayload {
    public static final Id<FastMinePayload> ID =
        new Id<>(Identifier.of("utilmod", "fast_mine"));

    public static final PacketCodec<RegistryByteBuf, FastMinePayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> buf.writeFloat(payload.multiplier()),
            buf -> new FastMinePayload(buf.readFloat())
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
