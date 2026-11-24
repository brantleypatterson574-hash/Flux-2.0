package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Places and breaks end crystals around the nearest player. Purely client-side
 * interactions: the server still applies its own placement and damage rules.
 */
public class AutoCrystalModule extends Module {
    private final NumberSetting range = new NumberSetting("Range", 8.0, 2.0, 16.0).description("Maximum distance for placing or breaking end crystals.");
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 10.0, 0.0, 40.0).description("Ticks between end crystal placements.");
    private final NumberSetting breakDelay = new NumberSetting("Break Delay", 2.0, 0.0, 40.0).description("Ticks between end crystal breaks.");
    private final BooleanSetting autoPlace = new BooleanSetting("Auto Place", true).description("Places end crystals near the target automatically.");
    private final BooleanSetting autoBreak = new BooleanSetting("Auto Break", true).description("Breaks nearby end crystals automatically.");
    private final BooleanSetting requireCrystal = new BooleanSetting("Require Crystal", true).description("Only runs while you are holding an end crystal.");
    private final NumberSetting minHealth = new NumberSetting("Min Target Health", 1.0, 1.0, 20.0).description("Ignores targets below this many health points, counted in half-hearts.");

    private long nextPlace, nextBreak;

    public AutoCrystalModule() {
        super("Auto Crystal",
            "Places and detonates end crystals around the nearest target. Server still validates every action.",
            Category.CRYSTAL);
        addSetting(range);
        addSetting(placeDelay);
        addSetting(breakDelay);
        addSetting(autoPlace);
        addSetting(autoBreak);
        addSetting(requireCrystal);
        addSetting(minHealth);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        nextPlace = 0L;
        nextBreak = 0L;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (requireCrystal.isEnabled()
            && !client.player.getMainHandStack().isOf(Items.END_CRYSTAL)) return;

        PlayerEntity target = client.world.getEntitiesByClass(PlayerEntity.class,
            client.player.getBoundingBox().expand(range.getValue()),
            p -> p != client.player && p.isAlive() && p.getHealth() >= minHealth.getValue()
        ).stream().findFirst().orElse(null);
        if (target == null) return;

        long now = client.world.getTime();
        if (autoBreak.isEnabled() && now >= nextBreak) {
            EndCrystalEntity crystal = client.world.getEntitiesByClass(EndCrystalEntity.class,
                target.getBoundingBox().expand(4.0), c -> c.isAlive()).stream().findFirst().orElse(null);
            if (crystal != null) {
                client.interactionManager.attackEntity(client.player, crystal);
                nextBreak = now + (long) breakDelay.getValue();
            }
        }

        if (autoPlace.isEnabled() && now >= nextPlace) {
            BlockPos below = BlockPos.ofFloored(
                target.getX(), target.getY() - 1.0, target.getZ());
            BlockHitResult hit = new BlockHitResult(
                new Vec3d(below.getX() + 0.5, below.getY() + 1.0, below.getZ() + 0.5),
                Direction.UP, below, false);
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
            nextPlace = now + (long) placeDelay.getValue();
        }
    }
}
