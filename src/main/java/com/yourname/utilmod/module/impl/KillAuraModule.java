package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.setting.BooleanSetting;
import net.minecraft.item.ItemStack;

/** General-purpose aura: attacks nearby targets with whatever is in your hand. */
public class KillAuraModule extends AuraModule {
    private final BooleanSetting onlyOnCooldown = new BooleanSetting("Only On Cooldown", true)
        .description("Wait for the vanilla attack cooldown instead of attacking every tick.");

    public KillAuraModule() {
        super("Kill Aura",
            "Attacks nearby players and mobs with any held item, with range and target filtering.",
            Category.SWORD);
        addSetting(onlyOnCooldown);
    }

    @Override
    protected boolean itemAllowed(ItemStack stack) {
        if (stack.isEmpty() || mc.player == null) return false;
        return !onlyOnCooldown.isEnabled()
            || mc.player.getAttackCooldownProgress(0.5f) >= 1.0f;
    }
}
