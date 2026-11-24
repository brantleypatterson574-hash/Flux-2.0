package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Carries the Reach module's extra block and entity reach to the private server. */
public record ReachPayload(double blockReach, double entityReach) implements CustomPayload {
    public static final Id<ReachPayload> ID =
        new Id<>(Identifier.of("utilmod", "reach"));

    public static final PacketCodec<RegistryByteBuf, ReachPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> {
                buf.writeDouble(payload.blockReach());
                buf.writeDouble(payload.entityReach());
            },
            buf -> new ReachPayload(buf.readDouble(), buf.readDouble())
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
