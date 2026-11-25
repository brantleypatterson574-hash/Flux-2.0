package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/** Covers your head when a falling anvil is overhead. */
public class AntiAnvilModule extends TickModule {
    private enum Cover { SLAB, OBSIDIAN }

    private final NumberSetting range = new NumberSetting("Anvil Range", 10.0, 3.0, 24.0)
        .description("How close a falling anvil must be before cover is placed.");
    private final NumberSetting delay = new NumberSetting("Delay", 3.0, 0.0, 40.0)
        .description("Ticks between cover attempts.");
    private final EnumSetting<Cover> cover = new EnumSetting<>("Cover Block", Cover.class, Cover.SLAB)
        .description("A slab covers the head slot; obsidian makes a thicker shield.");
    private final BooleanSetting rotate = new BooleanSetting("Silent Rotate", true)
        .description("Faces the placement briefly, then restores your view.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Animates the hand when the placement is accepted.");

    private long nextAttempt;

    public AntiAnvilModule() {
        super("Anti Anvil", "Covers your head space when a falling anvil is overhead.",
            Category.UTIL);
        addSetting(range);
        addSetting(delay);
        addSetting(cover);
        addSetting(rotate);
        addSetting(swing);
    }

    @Override
    public void onEnable() {
        nextAttempt = 0L;
    }

    @Override
    protected void onTick(MinecraftClient client) {
        long now = client.world.getTime();
        if (now < nextAttempt) return;

        boolean anvilAbove = !client.world.getEntitiesByClass(FallingBlockEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            e -> e.getBlockState().isOf(Blocks.ANVIL) && e.getY() > client.player.getY()
        ).isEmpty();
        if (!anvilAbove) return;

        BlockPos head = client.player.getBlockPos().up();
        if (!client.world.getBlockState(head).isAir()) return;
        Item item = cover.getValue() == Cover.OBSIDIAN ? Items.OBSIDIAN : Items.SMOOTH_STONE_SLAB;
        if (CombatBlockHelper.place(client, head, item, rotate.isEnabled(), swing.isEnabled())) {
            nextAttempt = now + (long) delay.getValue();
        }
    }
}
