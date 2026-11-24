package com.yourname.utilmod.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Exposes the effective (smooth) FOV so HUD ESP projection stays aligned with the world. */
@Mixin(GameRenderer.class)
public interface GameRendererFovAccessor {
    @Invoker("getFov")
    float utilmod$getFov(Camera camera, float tickDelta, boolean changingFov);
}
