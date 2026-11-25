package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.SoundBlockerModule;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Stops selected sounds before they reach the client audio system. */
@Mixin(SoundManager.class)
public class SoundBlockerMixin {
    @Inject(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void utilmod$blockSound(SoundInstance sound, CallbackInfoReturnable<PlayResult> cir) {
        if (SoundBlockerModule.shouldBlock(sound)) {
            cir.setReturnValue(PlayResult.NOT_STARTED);
        }
    }
}
