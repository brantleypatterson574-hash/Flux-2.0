package com.yourname.utilmod.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Exposes vanilla attack/use methods so automation can reuse the real code path. */
@Mixin(MinecraftClient.class)
public interface MinecraftClientInvokerMixin {
    @Invoker("doAttack")
    boolean utilmod$doAttack();

    @Invoker("doItemUse")
    void utilmod$doItemUse();
}
