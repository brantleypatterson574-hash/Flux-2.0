package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/** Mace-only attack assist with a smash-strike gate. */
public class MaceAuraModule extends AuraModule {
    private final BooleanSetting smashOnly = new BooleanSetting("Smash Only", false).description("Only attacks while a mace smash strike is available.");
    private final NumberSetting minFallDistance = new NumberSetting("Minimum Fall Distance", 1.5, 0.5, 20.0).description("Fall distance needed before a smash attack is allowed.");

    public MaceAuraModule() {
        super("Mace Aura",
            "Attacks nearby targets while holding a mace. Optional smash-only mode waits for a fall strike.",
            Category.MACE);
        addSetting(smashOnly);
        addSetting(minFallDistance);
    }

    @Override
    protected boolean itemAllowed(ItemStack stack) {
        if (!stack.isOf(Items.MACE)) return false;
        if (!smashOnly.isEnabled() || mc.player == null) return true;
        return mc.player.fallDistance >= minFallDistance.getValue()
            || (!mc.player.isOnGround() && mc.player.getVelocity().y < -0.35);
    }
}
