package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.setting.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;

/** Attack-assist tuned for crystal PvP: players first, optional crystal cleanup. */
public class CrystalAuraModule extends AuraModule {
    private final BooleanSetting attackCrystals = new BooleanSetting("Attack Crystals", true).description("Also attacks end crystals near the current target.");
    private final BooleanSetting onlyWhenCrystalNear = new BooleanSetting("Only When Crystal Near", false).description("Only targets players when an end crystal is close to them.");

    public CrystalAuraModule() {
        super("Crystal Aura",
            "Attacks nearby players during crystal PvP, with optional end-crystal cleanup.",
            Category.CRYSTAL);
        addSetting(attackCrystals);
        addSetting(onlyWhenCrystalNear);
    }

    @Override
    protected boolean targetAllowed(Entity entity) {
        if (entity instanceof EndCrystalEntity) return attackCrystals.isEnabled();
        if (!onlyWhenCrystalNear.isEnabled()) return true;
        if (mc.world == null) return false;
        return !mc.world.getEntitiesByClass(EndCrystalEntity.class,
            entity.getBoundingBox().expand(4.0), c -> true).isEmpty();
    }
}
