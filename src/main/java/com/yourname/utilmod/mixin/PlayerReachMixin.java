package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.HitboxManager;
import com.yourname.utilmod.server.ReachManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Applies the Reach module's extra block/entity reach to client interaction and
 * attack checks. On a private server the same values are mirrored through
 * ReachPayload so the server-side checks agree.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerReachMixin {
    @Inject(method = "getEntityInteractionRange", at = @At("RETURN"), cancellable = true)
    private void utilmod$extendEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        if (!ReachManager.hasAnyReach()) return;
        PlayerEntity self = (PlayerEntity) (Object) this;
        double extra = ReachManager.effectiveReach(self);
        if (extra <= 0.0) return;
        cir.setReturnValue(cir.getReturnValue() + extra);
    }

    @Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
    private void utilmod$extendBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
        if (!ReachManager.hasAnyReach()) return;
        PlayerEntity self = (PlayerEntity) (Object) this;
        double extra = ReachManager.effectiveBlockReach(self);
        if (extra <= 0.0) return;
        cir.setReturnValue(cir.getReturnValue() + extra);
    }

    /**
     * 1.21.11 validates attacks through this check. Expand the target box by the
     * configured hitbox margin, then allow the vanilla base range plus the
     * Reach module's extra entity reach.
     */
    @Inject(method = "canAttackEntityIn", at = @At("HEAD"), cancellable = true)
    private void utilmod$extendAttackReach(Box box, double extraRange,
                                           CallbackInfoReturnable<Boolean> cir) {
        if (!ReachManager.hasAnyReach() && !HitboxManager.hasAnyMargin()) return;
        if (box == null) return;

        PlayerEntity self = (PlayerEntity) (Object) this;
        double extra = ReachManager.effectiveReach(self);
        double margin = HitboxManager.hasAnyMargin()
            ? HitboxManager.effectiveMargin(self.getUuid())
            : 0.0;
        if (extra <= 0.0 && margin <= 0.0) return;

        Box checkBox = margin > 0.0 ? box.expand(margin) : box;
        double base = self.isCreative() ? 5.0 : 3.0;
        double range = base + extra + Math.max(0.0, extraRange);
        double distanceSq = checkBox.squaredMagnitude(self.getEyePos());
        cir.setReturnValue(distanceSq <= range * range);
    }
}
