package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.HitSelectModule;
import com.yourname.utilmod.util.AttackClickTracker;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Records real in-world left clicks and applies optional click rules. */
@Mixin(MinecraftClient.class)
public abstract class AttackClickMixin {
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void utilmod$recordAttack(CallbackInfoReturnable<Boolean> cir) {
        AttackClickTracker.record();
        if (HitSelectModule.shouldCancel(MinecraftClient.getInstance())) {
            cir.setReturnValue(false);
        }
    }
}
