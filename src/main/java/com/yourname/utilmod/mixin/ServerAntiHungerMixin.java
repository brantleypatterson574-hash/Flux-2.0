package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.AntiHungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Applies the private-server AntiHunger setting before hunger is updated. */
@Mixin(PlayerEntity.class)
public abstract class ServerAntiHungerMixin {
    @ModifyVariable(
        method = "addExhaustion(F)V",
        at = @At("HEAD"),
        argsOnly = true,
        index = 0
    )
    private float utilmod$reduceExhaustion(float amount) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        return AntiHungerManager.apply(self.getUuid(), self, amount);
    }
}
