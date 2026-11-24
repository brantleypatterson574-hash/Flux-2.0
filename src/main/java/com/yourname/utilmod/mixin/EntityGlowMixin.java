package com.yourname.utilmod.mixin;

import com.yourname.utilmod.util.GlowManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes selected entities report as glowing so Minecraft renders its built-in
 * silhouette outline around the actual model. Unlike setGlowing(), this does
 * not change entity state or network-synced glow flags.
 */
@Mixin(Entity.class)
public class EntityGlowMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void utilmod$modelGlow(CallbackInfoReturnable<Boolean> cir) {
        if (!GlowManager.isActive()) return;
        if (GlowManager.shouldGlow((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
