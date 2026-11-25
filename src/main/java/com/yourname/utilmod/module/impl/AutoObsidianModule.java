package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.ItemUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Blocks;

import java.util.HashSet;
import java.util.Set;

/** Places obsidian shells, pillars and floors around the nearest target. */
public class AutoObsidianModule extends Module {
    private enum PlaceMode { TRAP, PILLAR, FLOOR }

    private final EnumSetting<PlaceMode> mode =
        new EnumSetting<>("Mode", PlaceMode.class, PlaceMode.TRAP).description("Which obsidian shape to build: trap, pillar, or floor.");
    private final NumberSetting range = new NumberSetting("Range", 8.0, 2.0, 24.0).description("How close a player must be before obsidian is placed around them.");
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 3.0, 0.0, 40.0).description("Ticks between obsidian placements.");
    private final NumberSetting maxBlocks = new NumberSetting("Max Blocks", 9.0, 1.0, 24.0).description("Maximum number of obsidian blocks placed for one target.");
    private final BooleanSetting requireObsidian = new BooleanSetting("Require Obsidian", true).description("Only acts while obsidian is in your main hand.");
    private final BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true).description("Finds obsidian in your inventory and switches to it.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your original hotbar slot when finished.");

    private final Set<BlockPos> placed = new HashSet<>();
    private PlayerEntity target;
    private int originalSlot = -1;
    private long nextPlace;

    public AutoObsidianModule() {
        super("Auto Obsidian",
            "Places obsidian traps, pillars or floors around the nearest player. Server validates every placement.",
            Category.CRYSTAL);
        addSetting(mode);
        addSetting(range);
        addSetting(placeDelay);
        addSetting(maxBlocks);
        addSetting(requireObsidian);
        addSetting(autoSwitch);
        addSetting(swapBack);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        restore();
        reset();
    }

    private void reset() {
        placed.clear();
        target = null;
        originalSlot = -1;
        nextPlace = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null
            || client.interactionManager == null || client.currentScreen != null) return;

        PlayerEntity found = client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            p -> p != client.player && p.isAlive()).stream().findFirst().orElse(null);
        if (found == null) {
            restore();
            reset();
            return;
        }
        if (found != target) {
            restore();
            target = found;
            placed.clear();
            nextPlace = 0L;
        }

        int obsidianSlot = ItemUtil.findSlot(client.player, Items.OBSIDIAN, true, false);
        if (obsidianSlot < 0) {
            restore();
            return;
        }
        if (requireObsidian.isEnabled()
            && !client.player.getMainHandStack().isOf(Items.OBSIDIAN)) {
            if (!autoSwitch.isEnabled()) return;
            if (originalSlot < 0) originalSlot = client.player.getInventory().getSelectedSlot();
            ItemUtil.selectSlot(client.player, obsidianSlot);
            return;
        }

        long now = client.world.getTime();
        if (now < nextPlace || placed.size() >= maxBlocks.getValue()) return;

        BlockPos base = target.getBlockPos();
        for (BlockPos pos : candidates(base)) {
            if (placed.contains(pos) || !client.world.getBlockState(pos).isAir()) continue;
            if (place(client, pos)) {
                placed.add(pos);
                nextPlace = now + (long) placeDelay.getValue();
                return;
            }
        }
    }

    private BlockPos[] candidates(BlockPos base) {
        return switch (mode.getValue()) {
            case PILLAR -> new BlockPos[] {
                base.add(0, 2, 0), base.add(0, 3, 0), base.add(0, 4, 0), base.add(0, 5, 0)
            };
            case FLOOR -> new BlockPos[] {
                base.add(0, -1, 0), base.add(1, -1, 0), base.add(-1, -1, 0),
                base.add(0, -1, 1), base.add(0, -1, -1)
            };
            case TRAP -> new BlockPos[] {
                base.add(1, 0, 0), base.add(-1, 0, 0), base.add(0, 0, 1), base.add(0, 0, -1),
                base.add(1, 1, 0), base.add(-1, 1, 0), base.add(0, 1, 1), base.add(0, 1, -1),
                base.add(0, 2, 0)
            };
        };
    }

    private boolean place(MinecraftClient client, BlockPos pos) {
        // Click the face of the neighbour opposite the block we want to place.
        Direction face = Direction.UP;
        BlockPos neighbor = pos.down();
        if (!client.world.getBlockState(neighbor).isSolid()) {
            face = Direction.EAST;
            neighbor = pos.west();
            if (!client.world.getBlockState(neighbor).isSolid()) {
                face = Direction.SOUTH;
                neighbor = pos.north();
            }
        }
        Vec3d hitPos = new Vec3d(neighbor.getX() + 0.5, neighbor.getY() + 0.5, neighbor.getZ() + 0.5);
        BlockHitResult hit = new BlockHitResult(hitPos, face, neighbor, false);
        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        client.player.swingHand(Hand.MAIN_HAND);
        return true;
    }

    private void restore() {
        if (mc.player == null || originalSlot < 0) return;
        if (swapBack.isEnabled()) ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
