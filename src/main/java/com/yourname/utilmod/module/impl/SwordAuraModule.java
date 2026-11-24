package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.setting.BooleanSetting;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

/** Sword-only attack assist with cooldown and critical-hit gates. */
public class SwordAuraModule extends AuraModule {
    private final BooleanSetting onlyOnCooldown = new BooleanSetting("Only On Cooldown", true).description("Waits for the sword attack cooldown before attacking again.");
    private final BooleanSetting criticalOnly = new BooleanSetting("Critical Only", false).description("Only attacks when the swing would be a critical hit.");

    public SwordAuraModule() {
        super("Sword Aura",
            "Attacks nearby targets only while a sword is held. Optional cooldown and critical-hit gates.",
            Category.SWORD);
        addSetting(onlyOnCooldown);
        addSetting(criticalOnly);
    }

    @Override
    protected boolean itemAllowed(ItemStack stack) {
        if (!stack.isIn(ItemTags.SWORDS)) return false;
        if (mc.player == null) return false;
        if (onlyOnCooldown.isEnabled()
            && mc.player.getAttackCooldownProgress(0.5f) < 1.0f) return false;
        if (criticalOnly.isEnabled() && mc.player.isOnGround()) return false;
        return true;
    }
}
