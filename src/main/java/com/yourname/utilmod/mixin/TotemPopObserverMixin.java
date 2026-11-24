package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.AutoGgModule;
import com.yourname.utilmod.module.impl.AutoTotemModule;
import com.yourname.utilmod.module.impl.ConfettiDeathsModule;
import com.yourname.utilmod.module.impl.TotemPopFxModule;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Observes the vanilla totem-pop status purely to feed the cosmetic pop board.
 * The packet is never modified, cancelled, delayed or re-sent.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class TotemPopObserverMixin {
    @Inject(method = "onEntityStatus", at = @At("HEAD"))
    private void utilmod$observeTotemPop(EntityStatusS2CPacket packet, CallbackInfo ci) {
        AutoTotemModule.onEntityStatus(packet);
        TotemPopFxModule.onEntityStatus(packet);
        AutoGgModule.onEntityStatus(packet);
        ConfettiDeathsModule.onEntityStatus(packet);
    }
}
