package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.SkyPaletteModule;
import com.yourname.utilmod.module.impl.WeatherChangerModule;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Applies Flux's client-only sky mood after vanilla has built the render state. */
@Mixin(SkyRendering.class)
public class SkyRenderingMixin {
    @Inject(
        method = "updateRenderState(Lnet/minecraft/client/world/ClientWorld;FLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/state/SkyRenderState;)V",
        at = @At("RETURN")
    )
    private void utilmod$applySkyMood(ClientWorld world, float tickDelta, Camera camera,
                                      SkyRenderState state, CallbackInfo ci) {
        SkyPaletteModule.apply(world, tickDelta, state);
        WeatherChangerModule.apply(state);
    }
}
