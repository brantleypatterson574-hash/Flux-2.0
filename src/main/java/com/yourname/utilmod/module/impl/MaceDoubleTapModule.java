package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.mixin.MinecraftClientInvokerMixin;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

/** Follows a mace hit with one extra vanilla attack after a short delay. */
public class MaceDoubleTapModule extends Module {
    public static MaceDoubleTapModule INSTANCE;

    private final BooleanSetting requireMace = new BooleanSetting("Require Mace", true)
        .description("Only follows up while you are holding a mace.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 2.0, 1.0, 20.0)
        .description("Ticks between the first hit and the follow-up.");
    private final NumberSetting cooldownTicks = new NumberSetting("Cooldown", 12.0, 2.0, 80.0)
        .description("Minimum ticks between double taps.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Only follows up on other players.");

    private boolean pending;
    private boolean suppressNext;
    private long dueAt;
    private long nextAllowed;

    public MaceDoubleTapModule() {
        super("Mace Double Tap", "Adds one delayed follow-up attack after a mace hit.", Category.MACE);
        INSTANCE = this;
        addSetting(requireMace);
        addSetting(delayTicks);
        addSetting(cooldownTicks);
        addSetting(playersOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    /** Called from the shared attack hook after the original attack is queued. */
    public static void onAttack(PlayerEntity attacker, Entity target) {
        MaceDoubleTapModule module = INSTANCE;
        if (module == null || !module.isEnabled() || module.suppressNext || target == null) return;
        if (module.playersOnly.isEnabled() && !(target instanceof PlayerEntity)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != attacker) return;
        if (module.requireMace.isEnabled()
            && !attacker.getMainHandStack().isOf(Items.MACE)) return;
        long now = client.world == null ? System.currentTimeMillis() : client.world.getTime();
        if (now < module.nextAllowed || module.pending) return;
        module.pending = true;
        module.dueAt = now + (long) module.delayTicks.getValue();
        module.nextAllowed = now + (long) module.cooldownTicks.getValue();
    }

    @Override
    public void onEnable() {
        pending = false;
        suppressNext = false;
        nextAllowed = 0L;
    }

    @Override
    public void onDisable() {
        pending = false;
        suppressNext = false;
    }

    private void onTick(MinecraftClient client) {
        if (!pending || client.player == null || client.world == null) return;
        if (client.world.getTime() < dueAt) return;
        pending = false;
        suppressNext = true;
        ((MinecraftClientInvokerMixin) client).utilmod$doAttack();
        suppressNext = false;
    }
}
