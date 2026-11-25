package com.yourname.utilmod.mixin;

import com.yourname.utilmod.module.impl.AutoBreachModule;
import com.yourname.utilmod.module.impl.AutoGgModule;
import com.yourname.utilmod.module.impl.HitFxModule;
import com.yourname.utilmod.module.impl.MaceDoubleTapModule;
import com.yourname.utilmod.module.impl.WTapModule;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks the client attack so AutoBreach can swap the held slot in the same tick
 * the attack packet is produced. Based on the same hook used by BetterMaceSwap.
 */
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void utilmod$beforeAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player instanceof ClientPlayerEntity clientPlayer) {
            AutoBreachModule.handleAttack(clientPlayer, target);
        }
        HitFxModule.onAttack(player, target);
        AutoGgModule.onAttack(player, target);
        WTapModule.onAttack(player, target);
        MaceDoubleTapModule.onAttack(player, target);
    }
}
