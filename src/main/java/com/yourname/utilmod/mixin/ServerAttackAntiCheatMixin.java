package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.AntiCheatManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Server-side reach check for the AntiCheat module. */
@Mixin(ServerPlayerEntity.class)
public class ServerAttackAntiCheatMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void utilmod$antiCheatAttack(Entity target, CallbackInfo ci) {
        AntiCheatManager.checkAttack((ServerPlayerEntity) (Object) this, target);
    }
}
