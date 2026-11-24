package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.NoKnockbackManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/** Scales server-authoritative knockback for players using the Velocity module. */
@Mixin(LivingEntity.class)
public class ServerNoKnockbackMixin {
    @ModifyArgs(method = "takeKnockback(DDD)V", at = @At("HEAD"))
    private void utilmod$scaleKnockback(Args args) {
        if (!((Object) this instanceof ServerPlayerEntity player)) return;
        double h = NoKnockbackManager.horizontal(player.getUuid());
        double v = NoKnockbackManager.vertical(player.getUuid());
        if (h >= 1.0 && v >= 1.0) return;

        if (h <= 0.0 && v <= 0.0) {
            args.set(0, 0.0);
            return;
        }

        // Vanilla uses strength for lift and strength * ratio for horizontal push.
        // Rebalancing the ratio lets one strength carry two independent scales.
        double vertical = Math.max(0.01, v);
        double horizontalRatio = h / vertical;
        args.set(0, (Double) args.get(0) * vertical);
        args.set(1, (Double) args.get(1) * horizontalRatio);
        args.set(2, (Double) args.get(2) * horizontalRatio);
    }
}
