package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Carries the Velocity module's horizontal and vertical knockback scales C2S. */
public record NoKnockbackPayload(double horizontal, double vertical) implements CustomPayload {
    public static final Id<NoKnockbackPayload> ID =
        new Id<>(Identifier.of("utilmod", "no_knockback"));

    public static final PacketCodec<RegistryByteBuf, NoKnockbackPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> {
                buf.writeDouble(payload.horizontal());
                buf.writeDouble(payload.vertical());
            },
            buf -> new NoKnockbackPayload(buf.readDouble(), buf.readDouble())
        );

    /** Neutral state: knockback is left untouched. */
    public static NoKnockbackPayload disabled() {
        return new NoKnockbackPayload(1.0, 1.0);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
