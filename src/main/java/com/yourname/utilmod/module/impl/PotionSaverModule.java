package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

/** Stops drinking a potion when that effect is already active for long enough. */
public class PotionSaverModule extends Module {
    private final NumberSetting minimumDuration = new NumberSetting("Minimum Duration", 120.0, 0.0, 1200.0)
        .unit("ticks")
        .description("Keeps drinking only when the active effect has fewer ticks left than this.");
    private final BooleanSetting requireStronger = new BooleanSetting("Require Stronger", true)
        .description("Also keeps drinking when the potion's amplifier is higher than the active one.");
    private final BooleanSetting onlyBeneficial = new BooleanSetting("Only Beneficial", true)
        .description("Only saves drinkable potions; splash and lingering effects never trigger this.");

    public PotionSaverModule() {
        super("Potion Saver", "Stops drinking a potion when its effect is already active.", Category.PLAYER);
        addSetting(minimumDuration);
        addSetting(requireStronger);
        addSetting(onlyBeneficial);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || !client.player.isUsingItem()) return;
        ItemStack held = client.player.getActiveItem();
        if (held.isEmpty()) return;
        PotionContentsComponent potion = held.get(DataComponentTypes.POTION_CONTENTS);
        if (potion == null || (onlyBeneficial.isEnabled() && !held.isOf(net.minecraft.item.Items.POTION))) return;

        for (StatusEffectInstance wanted : potion.getEffects()) {
            StatusEffectInstance active = client.player.getStatusEffect(wanted.getEffectType());
            if (active == null) continue;

            boolean strongEnough = active.getAmplifier() >= wanted.getAmplifier();
            if (requireStronger.isEnabled() && active.getAmplifier() < wanted.getAmplifier()) strongEnough = false;
            if (!strongEnough) continue;
            if (active.getDuration() >= Math.min(wanted.getDuration(), minimumDuration.getValue())) {
                client.player.stopUsingItem();
                return;
            }
        }
    }
}
