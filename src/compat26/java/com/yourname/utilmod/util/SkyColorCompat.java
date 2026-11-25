package com.yourname.utilmod.util;

import net.minecraft.client.render.state.SkyRenderState;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.lang.reflect.Field;

/**
 * Compile-time bridge for the 26.x sky render state. 26.1/26.2 use packed int
 * colours while 26.3 uses JOML vectors; reflection keeps one bridge source that
 * compiles and runs against both shapes without version checks in shared code.
 */
public final class SkyColorCompat {
    private SkyColorCompat() {}

    public static void setSky(SkyRenderState state, int argb) {
        set(state, "skyColor", argb);
    }

    public static void setHorizon(SkyRenderState state, int argb) {
        set(state, "sunriseAndSunsetColor", argb);
    }

    public static Vector3fc mixSky(Vector3fc current, int to, float t) {
        return sky(ColorUtil.mix(fromSky(current), to, t));
    }

    public static Vector4fc mixHorizon(Vector4fc current, int to, float t) {
        return horizon(ColorUtil.mix(fromHorizon(current), to, t));
    }

    private static void set(SkyRenderState state, String name, int argb) {
        try {
            Field field = SkyRenderState.class.getField(name);
            Class<?> type = field.getType();
            Object value;
            if (type == int.class || type == Integer.class) {
                value = argb;
            } else if (type == Vector3f.class || type == Vector3fc.class) {
                value = sky(argb);
            } else if (type == Vector4f.class || type == Vector4fc.class) {
                value = horizon(argb);
            } else {
                return;
            }
            field.set(state, value);
        } catch (ReflectiveOperationException ignored) {
            // A missing render-state field simply leaves the vanilla colour.
        }
    }

    private static Vector3fc sky(int argb) {
        return new Vector3f(
            channel((argb >> 16) & 0xFF),
            channel((argb >> 8) & 0xFF),
            channel(argb & 0xFF));
    }

    private static Vector4fc horizon(int argb) {
        return new Vector4f(
            channel((argb >> 16) & 0xFF),
            channel((argb >> 8) & 0xFF),
            channel(argb & 0xFF),
            channel((argb >>> 24) & 0xFF));
    }

    private static int fromSky(Vector3fc color) {
        return 0xFF000000
            | (byteValue(color.x()) << 16)
            | (byteValue(color.y()) << 8)
            | byteValue(color.z());
    }

    private static int fromHorizon(Vector4fc color) {
        return (byteValue(color.w()) << 24)
            | (byteValue(color.x()) << 16)
            | (byteValue(color.y()) << 8)
            | byteValue(color.z());
    }

    private static float channel(int value) {
        return Math.max(0.0f, Math.min(1.0f, value / 255.0f));
    }

    private static int byteValue(float value) {
        return Math.max(0, Math.min(255, Math.round(value * 255.0f)));
    }
}
