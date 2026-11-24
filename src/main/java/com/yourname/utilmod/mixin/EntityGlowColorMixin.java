package com.yourname.utilmod.mixin;

import com.yourname.utilmod.util.GlowManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Uses our per-entity colour for the vanilla glowing outline pass. */
@Mixin(Entity.class)
public class EntityGlowColorMixin {
    @Inject(method = "getTeamColorValue", at = @At("HEAD"), cancellable = true)
    private void utilmod$glowColor(CallbackInfoReturnable<Integer> cir) {
        if (!GlowManager.isActive()) return;
        Entity entity = (Entity) (Object) this;
        if (GlowManager.shouldGlow(entity)) {
            cir.setReturnValue(GlowManager.color(entity));
        }
    }
}
