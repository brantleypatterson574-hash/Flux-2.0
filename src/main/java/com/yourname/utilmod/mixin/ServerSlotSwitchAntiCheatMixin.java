package com.yourname.utilmod.mixin;

import com.yourname.utilmod.server.AntiCheatManager;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Measures exact hotbar swap timing for the AntiCheat AutoBreach check. */
@Mixin(ServerPlayNetworkHandler.class)
public class ServerSlotSwitchAntiCheatMixin {
    @Inject(method = "onUpdateSelectedSlot", at = @At("HEAD"))
    private void utilmod$antiCheatSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) (Object) this;
        AntiCheatManager.onSlotPacket(handler.player, packet.getSelectedSlot());
    }
}
