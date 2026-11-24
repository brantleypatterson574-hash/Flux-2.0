package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.HitboxManager;
import com.yourname.utilmod.server.ReachManager;
import net.minecraft.component.type.AttackRangeComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.11 validates every attack through the item's AttackRangeComponent.
 * The Reach module adds to the component's effective range; the Hitbox Adjuster
 * adds to its accepted margin. Physics/collision boxes stay untouched.
 */
@Mixin(AttackRangeComponent.class)
public abstract class AttackRangeComponentMixin {
    @Inject(method = "getEffectiveMaxRange", at = @At("RETURN"), cancellable = true)
    private void utilmod$extendMaxRange(Entity entity, CallbackInfoReturnable<Float> cir) {
        if (!ReachManager.hasAnyReach()) return;
        double extra = ReachManager.effectiveReach(entity);
        if (extra <= 0.0) return;
        cir.setReturnValue((float) Math.min(32.0, cir.getReturnValue() + extra));
    }

    /** Margin configured for the attacker being checked, not for the target. */
    private double utilmod$margin(Entity attacker) {
        return HitboxManager.hasAnyMargin()
            ? HitboxManager.effectiveMargin(attacker.getUuid())
            : 0.0;
    }

    @Inject(
        method = "isWithinRange(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/math/Vec3d;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void utilmod$extendPointRange(LivingEntity attacker, Vec3d pos,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (!HitboxManager.hasAnyMargin()) return;
        AttackRangeComponent component = (AttackRangeComponent) (Object) this;
        double margin = utilmod$margin(attacker);
        if (margin <= 0.0) return;

        double distance = Math.sqrt(attacker.getEyePos().squaredDistanceTo(pos));
        double min = component.getEffectiveMinRange(attacker) - component.hitboxMargin();
        // Reach is already included by getEffectiveMaxRange above.
        double max = component.getEffectiveMaxRange(attacker) + component.hitboxMargin() + margin;
        if (distance >= min && distance <= max) cir.setReturnValue(true);
    }

    @Inject(
        method = "isWithinRange(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/math/Box;D)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void utilmod$extendBoxRange(LivingEntity attacker, Box box, double extraRange,
                                        CallbackInfoReturnable<Boolean> cir) {
        if (!HitboxManager.hasAnyMargin()) return;
        AttackRangeComponent component = (AttackRangeComponent) (Object) this;
        double margin = utilmod$margin(attacker);
        if (margin <= 0.0) return;

        double distance = Math.sqrt(box.squaredMagnitude(attacker.getEyePos()));
        double min = component.getEffectiveMinRange(attacker) - component.hitboxMargin();
        // Reach is already included by getEffectiveMaxRange above.
        double max = component.getEffectiveMaxRange(attacker) + component.hitboxMargin()
            + margin + Math.max(0.0, extraRange);
        if (distance >= min && distance <= max) cir.setReturnValue(true);
    }
}
