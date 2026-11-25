package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.TickModule;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatBlockHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

/** Places a cover block in your own head space when an anchor is close by. */
public class AntiAnchorModule extends TickModule {
    private enum Cover { SLAB, OBSIDIAN }

    private final NumberSetting range = new NumberSetting("Anchor Range", 10.0, 3.0, 24.0)
        .description("Only reacts when a respawn anchor is within this distance.");
    private final NumberSetting delay = new NumberSetting("Delay", 5.0, 0.0, 40.0)
        .description("Ticks between cover attempts.");
    private final EnumSetting<Cover> cover = new EnumSetting<>("Cover Block", Cover.class, Cover.SLAB)
        .description("A slab covers the head slot; obsidian makes a thicker shield.");
    private final BooleanSetting rotate = new BooleanSetting("Silent Rotate", true)
        .description("Faces the placement briefly, then restores your view.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Animates the hand when the placement is accepted.");

    private long nextAttempt;

    public AntiAnchorModule() {
        super("Anti Anchor", "Covers your head space when a nearby respawn anchor could hurt you.",
            Category.CRYSTAL);
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

        BlockPos anchor = CombatBlockHelper.findBlockNear(client, client.player.getBlockPos(),
            Blocks.RESPAWN_ANCHOR, (int) range.getValue());
        if (anchor == null) return;

        BlockPos head = client.player.getBlockPos().up();
        if (!client.world.getBlockState(head).isAir()) return;
        Item item = cover.getValue() == Cover.OBSIDIAN ? Items.OBSIDIAN : Items.SMOOTH_STONE_SLAB;
        if (CombatBlockHelper.place(client, head, item, rotate.isEnabled(), swing.isEnabled())) {
            nextAttempt = now + (long) delay.getValue();
        }
    }
}
