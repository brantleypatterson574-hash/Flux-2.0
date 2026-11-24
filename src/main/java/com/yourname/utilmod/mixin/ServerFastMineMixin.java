package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.FastMineManager;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Multiplies server-authoritative block-breaking progress for FastMine users. */
@Mixin(ServerPlayerInteractionManager.class)
public class ServerFastMineMixin {
    @Shadow
    protected ServerPlayerEntity player;

    @Inject(
        method = "continueMining(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;I)F",
        at = @At("RETURN"),
        cancellable = true
    )
    private void utilmod$fastMine(BlockState state, BlockPos pos, int startTime,
                                  CallbackInfoReturnable<Float> cir) {
        float multiplier = FastMineManager.multiplier(player.getUuid());
        if (multiplier > 1.0f) {
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }
}
