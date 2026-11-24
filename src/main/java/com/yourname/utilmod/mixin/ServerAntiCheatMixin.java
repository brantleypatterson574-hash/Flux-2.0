package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.AntiCheatManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Server-side movement checks for the AntiCheat module. */
@Mixin(ServerPlayerEntity.class)
public class ServerAntiCheatMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void utilmod$antiCheatTick(CallbackInfo ci) {
        AntiCheatManager.checkMovement((ServerPlayerEntity) (Object) this);
    }
}
