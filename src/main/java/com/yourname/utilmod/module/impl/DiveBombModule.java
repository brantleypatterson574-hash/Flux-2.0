package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.mixin.MinecraftClientInvokerMixin;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

/** Swaps to a mace while gliding down toward an enemy and attacks on contact. */
public class DiveBombModule extends Module {
    private final BooleanSetting requireElytra = new BooleanSetting("Require Elytra", true)
        .description("Only arms while you are gliding with an elytra.");
    private final NumberSetting minFall = new NumberSetting("Minimum Fall Distance", 3.0, 0.5, 40.0)
        .unit("blocks")
        .description("Fall distance needed before the mace is readied.");
    private final NumberSetting targetRange = new NumberSetting("Target Range", 8.0, 2.0, 24.0)
        .unit("blocks")
        .description("How close the enemy below you must be.");
    private final NumberSetting attackRange = new NumberSetting("Attack Range", 3.5, 1.0, 8.0)
        .unit("blocks")
        .description("Attacks only once the enemy is this close.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true)
        .description("Returns to your previous hotbar slot afterwards.");

    private int originalSlot = -1;
    private long nextAttack;

    public DiveBombModule() {
        super("Dive Bomb", "Readies a mace while diving at an enemy and attacks when close.", Category.MACE);
        addSetting(requireElytra);
        addSetting(minFall);
        addSetting(targetRange);
        addSetting(attackRange);
        addSetting(swapBack);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        restore();
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (requireElytra.isEnabled() && !client.player.isGliding()) {
            restore();
            return;
        }
        if (client.player.fallDistance < minFall.getValue()
            || client.player.getVelocity().y >= 0.0) {
            restore();
            return;
        }

        PlayerEntity target = client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(targetRange.getValue()),
            p -> p != client.player && p.isAlive()).stream()
            .filter(p -> p.getY() < client.player.getY())
            .findFirst().orElse(null);
        if (target == null) {
            restore();
            return;
        }

        if (!client.player.getMainHandStack().isOf(Items.MACE)) {
            int mace = ItemUtil.findSlot(client.player, Items.MACE, true, false);
            if (mace < 0) return;
            if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
            ItemUtil.selectSlot(client.player, mace);
        }

        long now = client.world.getTime();
        if (target.distanceTo(client.player) > attackRange.getValue() || now < nextAttack) return;
        ((MinecraftClientInvokerMixin) client).utilmod$doAttack();
        nextAttack = now + 10L;
        restore();
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        if (swapBack.isEnabled()) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
