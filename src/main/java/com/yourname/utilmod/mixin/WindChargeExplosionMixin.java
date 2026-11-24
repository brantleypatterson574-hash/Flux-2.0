package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.KeyWindChargeModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Lets the Wind Charge Jump module trigger its jump on the exact local burst tick. */
@Mixin(WindChargeEntity.class)
public class WindChargeExplosionMixin {
    @Inject(method = "createExplosion(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"))
    private void utilmod$onWindChargeBurst(Vec3d pos, CallbackInfo ci) {
        WindChargeEntity self = (WindChargeEntity) (Object) this;
        Entity owner = self.getOwner();
        if (owner != null) KeyWindChargeModule.onExplosion(owner, pos);
    }
}
