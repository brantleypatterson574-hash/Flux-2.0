package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.ChatFilterModule;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.ProfilelessChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cancels only the client-side display of matching chat lines. Packets are not
 * modified, re-sent or used to infer anything about other servers.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class ChatFilterMixin {
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void utilmod$filterChat(ChatMessageS2CPacket packet, CallbackInfo ci) {
        Text content = packet.unsignedContent();
        if (content == null && packet.body() != null) {
            content = Text.literal(packet.body().content());
        }
        if (ChatFilterModule.shouldHide(content)) ci.cancel();
    }

    @Inject(method = "onProfilelessChatMessage", at = @At("HEAD"), cancellable = true)
    private void utilmod$filterSystemChat(ProfilelessChatMessageS2CPacket packet, CallbackInfo ci) {
        if (ChatFilterModule.shouldHideSystem(packet.message())) ci.cancel();
    }
}
