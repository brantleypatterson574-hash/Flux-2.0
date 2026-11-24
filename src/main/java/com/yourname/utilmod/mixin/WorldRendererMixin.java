package com.yourname.utilmod.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.yourname.utilmod.module.impl.WeatherModule;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Client-only weather hooks used by the Weather module. */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void utilmod$skipWeather(FrameGraphBuilder frameGraph, GpuBufferSlice fogBuf, CallbackInfo ci) {
        if (WeatherModule.skipWeather()) ci.cancel();
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void utilmod$skipClouds(FrameGraphBuilder frameGraph, CloudRenderMode mode,
                                    Vec3d pos, long time, float tickDelta,
                                    int viewDistance, float cloudHeight, CallbackInfo ci) {
        if (WeatherModule.skipClouds()) ci.cancel();
    }
}
