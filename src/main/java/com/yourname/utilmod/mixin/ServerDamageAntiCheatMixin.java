package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.AntiCheatManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Starts reaction-time samples when a server player takes damage. */
@Mixin(ServerPlayerEntity.class)
public class ServerDamageAntiCheatMixin {
    @Inject(
        method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",
        at = @At("HEAD")
    )
    private void utilmod$antiCheatDamage(ServerWorld world, DamageSource source, float amount,
                                         CallbackInfoReturnable<Boolean> cir) {
        AntiCheatManager.onDamaged((ServerPlayerEntity) (Object) this);
    }
}
