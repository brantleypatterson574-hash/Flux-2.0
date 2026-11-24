package com.yourname.utilmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

/**
 * Client rotation helpers that also inform the server. Changing yaw/pitch
 * locally alone only changes the client raycast; interactions are validated
 * against the server's copy of the player rotation.
 */
public final class RotationUtil {
    private RotationUtil() {}

    public static float[] face(MinecraftClient client, Vec3d point) {
        if (client.player == null) return new float[0];
        float oldYaw = client.player.getYaw();
        float oldPitch = client.player.getPitch();
        Vec3d eyes = client.player.getEyePos();
        double dx = point.x - eyes.x;
        double dy = point.y - eyes.y;
        double dz = point.z - eyes.z;
        float yaw = (float) (Math.atan2(-dx, dz) * 180.0 / Math.PI);
        float pitch = (float) (-Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * 180.0 / Math.PI);
        apply(client, yaw, Math.max(-90.0f, Math.min(90.0f, pitch)));
        return new float[] { oldYaw, oldPitch };
    }

    public static void apply(MinecraftClient client, float yaw, float pitch) {
        if (client.player == null) return;
        client.player.setYaw(yaw);
        client.player.setPitch(Math.max(-90.0f, Math.min(90.0f, pitch)));
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                yaw, client.player.getPitch(), client.player.isOnGround(), client.player.horizontalCollision));
        }
    }

    public static void restore(MinecraftClient client, float[] old) {
        if (client.player == null || old == null || old.length < 2) return;
        apply(client, old[0], old[1]);
    }
}
