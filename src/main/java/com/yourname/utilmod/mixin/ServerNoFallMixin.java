package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.NoFallManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Rewrites the fall distance used by the server's damage calculation.
 * RESET sets it to zero; MINIMIZE clamps it to the configured distance.
 */
@Mixin(PlayerEntity.class)
public abstract class ServerNoFallMixin {
    @ModifyVariable(
        method = "handleFallDamage(DFLnet/minecraft/entity/damage/DamageSource;)Z",
        at = @At("HEAD"),
        argsOnly = true,
        index = 0
    )
    private double utilmod$rewriteFallDistance(double fallDistance) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        return NoFallManager.apply(self.getUuid(), self, fallDistance);
    }
}
