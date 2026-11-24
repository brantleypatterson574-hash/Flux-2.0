package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.DinnerboneModeModule;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Uses the vanilla upside-down render flag instead of touching entity names or
 * rotations. Physics, collision and hitboxes are never modified.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class DinnerboneMixin {
    @Inject(
        method = "shouldFlipUpsideDown(Lnet/minecraft/entity/LivingEntity;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void utilmod$flipLivingEntity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (DinnerboneModeModule.shouldFlip(entity)) cir.setReturnValue(true);
    }
}
