package com.yourname.utilmod.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

/** S2C notification so the client HUD can show server-side AntiCheat flags. */
public record AntiCheatAlertPayload(UUID uuid, String reason, String detail, int count)
    implements CustomPayload {

    public static final Id<AntiCheatAlertPayload> ID =
        new Id<>(Identifier.of("utilmod", "anticheat_alert"));

    public static final PacketCodec<RegistryByteBuf, AntiCheatAlertPayload> CODEC =
        CustomPayload.codecOf(
            (payload, buf) -> {
                buf.writeUuid(payload.uuid());
                buf.writeString(payload.reason());
                buf.writeString(payload.detail());
                buf.writeInt(payload.count());
            },
            buf -> new AntiCheatAlertPayload(
                buf.readUuid(),
                buf.readString(32767),
                buf.readString(32767),
                buf.readInt()
            )
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
