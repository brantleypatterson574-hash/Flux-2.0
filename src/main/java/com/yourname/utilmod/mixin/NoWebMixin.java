package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.NoWebModule;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Blocks No Web's selected slow effects before they reach the player movement vector. */
@Mixin(PlayerEntity.class)
public class NoWebMixin {
    @Inject(
        method = "slowMovement(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void utilmod$skipBlockSlow(BlockState state, Vec3d multiplier, CallbackInfo ci) {
        if (NoWebModule.shouldIgnore(state)) ci.cancel();
    }
}
