package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.CombatMath;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.RotationUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Rule-based mace assistant. There is no learning or randomness here: every
 * tick the visible target is scored with fixed combat rules and the best of
 * four responses is carried out. Mace Swap (Auto Mace) owns the hotbar when it
 * is on, so this module stands down completely in that case.
 */
public class MaceAiModule extends Module implements HudOverlay {
    private enum Priority { NEAREST, LOWEST_HEALTH, CROSSHAIR }
    private enum Decision { SHIELD_STUN, DENSITY_SMASH, BREACH_HIT, WIND_CHARGE, PLAIN_HIT, WAIT }

    private final NumberSetting range = new NumberSetting("Range", 4.5, 1.0, 12.0)
        .description("How close a target must be before the AI considers it.");
    private final NumberSetting viewAngle = new NumberSetting("View Angle", 100.0, 15.0, 360.0)
        .description("Width of the cone in front of you that the AI searches for targets.");
    private final EnumSetting<Priority> targetPriority =
        new EnumSetting<>("Target Priority", Priority.class, Priority.NEAREST)
            .description("Which visible target to pick when more than one is in range.");
    private final NumberSetting smashFall = new NumberSetting("Smash Fall Distance", 2.5, 1.5, 20.0)
        .description("Minimum fall distance before the AI plans a Density smash. A mace smash needs more than 1.5 blocks.");
    private final NumberSetting shieldHits = new NumberSetting("Shield Hits", 1.0, 0.0, 5.0)
        .description("How many axe hits to land on a blocking shield before switching back to the mace.");
    private final NumberSetting attackDelay = new NumberSetting("Attack Delay", 2.0, 0.0, 20.0)
        .description("Ticks to wait after a hit before the AI can act again.");
    private final NumberSetting comboDelay = new NumberSetting("Combo Delay", 2.0, 0.0, 20.0)
        .description("Extra ticks after a shield stun before the follow-up hit.");
    private final NumberSetting swapBackDelay = new NumberSetting("Swap Back Delay", 3.0, 0.0, 20.0)
        .description("Ticks after a swapped hit before returning to your original hotbar slot.");
    private final BooleanSetting silentRotate = new BooleanSetting("Silent Rotate", true)
        .description("Turn toward the target for the hit, then restore your original view.");
    private final BooleanSetting swing = new BooleanSetting("Swing", true)
        .description("Swing your hand when the AI attacks.");
    private final BooleanSetting showDecision = new BooleanSetting("Show Decision", false)
        .description("Draws the current AI choice in the top-left of the HUD.");
    private final BooleanSetting throwWindCharges = new BooleanSetting("Throw Wind Charges", true)
        .description("Throws a wind charge from your hotbar to launch you into a mace smash setup.");
    private final NumberSetting windChargePitch = new NumberSetting("Wind Charge Pitch", 90.0, -90.0, 90.0)
        .description("Throw angle for the setup charge; 90 points straight down for the biggest launch.");
    private final NumberSetting windChargeCooldown = new NumberSetting("Wind Charge Cooldown", 20.0, 2.0, 120.0)
        .description("Ticks between automatic wind charge throws so the AI does not spam them.");

    private LivingEntity activeTarget;
    private Decision decision = Decision.WAIT;
    private String decisionLabel = "Idle";
    private int originalSlot = -1;
    private int swapBackTicks;
    private int attackTicks;
    private int comboTicks;
    private int shieldHitsLeft;
    private int windChargeTicks;

    public MaceAiModule() {
        super("Mace AI",
            "Rule-based mace decisions: wind-charge setup, shield stun, Density smash, Breach hit or a plain follow-up. Stands down while Mace Swap is on. Server rules still decide what lands.",
            Category.MACE);
        addSetting(range);
        addSetting(viewAngle);
        addSetting(targetPriority);
        addSetting(smashFall);
        addSetting(shieldHits);
        addSetting(attackDelay);
        addSetting(comboDelay);
        addSetting(swapBackDelay);
        addSetting(silentRotate);
        addSetting(swing);
        addSetting(showDecision);
        addSetting(throwWindCharges);
        addSetting(windChargePitch);
        addSetting(windChargeCooldown);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        restoreSlot(mc.player);
        clearState();
    }

    private void clearState() {
        restoreSlot(mc.player);
        activeTarget = null;
        decision = Decision.WAIT;
        decisionLabel = "Idle";
        originalSlot = -1;
        swapBackTicks = 0;
        attackTicks = 0;
        comboTicks = 0;
        shieldHitsLeft = 0;
        windChargeTicks = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        if (client.player == null || client.world == null || client.interactionManager == null) {
            clearState();
            return;
        }
        ClientPlayerEntity player = client.player;

        tickSwapBack(player);
        if (windChargeTicks > 0) windChargeTicks--;
        if (UtilModClient.isModuleEnabled(AutoMaceModule.class)) {
            decision = Decision.WAIT;
            decisionLabel = "Mace Swap active";
            return;
        }
        if (player.isSpectator() || player.getAbilities().creativeMode || client.currentScreen != null) {
            decision = Decision.WAIT;
            decisionLabel = "Paused";
            return;
        }
        if (attackTicks > 0) {
            attackTicks--;
            return;
        }
        if (comboTicks > 0) {
            comboTicks--;
            return;
        }

        LivingEntity target = findTarget(client, player);
        if (target == null) {
            activeTarget = null;
            decision = Decision.WAIT;
            decisionLabel = "No target";
            return;
        }
        if (target != activeTarget) {
            activeTarget = target;
            shieldHitsLeft = (int) shieldHits.getValue();
        }

        decision = chooseDecision(client, player, target);
        decisionLabel = label(decision);
        if (decision == Decision.WAIT) return;

        float[] previousRotation = null;
        if (silentRotate.isEnabled()) {
            Vec3d point = target.getEntityPos().add(0.0, target.getHeight() * 0.5, 0.0);
            previousRotation = RotationUtil.face(client, point);
        }
        boolean acted = perform(client, player, target, decision);
        if (previousRotation != null) RotationUtil.restore(client, previousRotation);

        if (!acted) {
            decision = Decision.WAIT;
            decisionLabel = "No weapon";
            return;
        }
        attackTicks = (int) attackDelay.getValue();
        if (decision == Decision.SHIELD_STUN) comboTicks = (int) comboDelay.getValue();
        swapBackTicks = (int) swapBackDelay.getValue();
        if (swapBackTicks == 0) restoreSlot(player);
    }

    /** Returns the original hotbar slot after a swap-based hit has had time to land. */
    private void tickSwapBack(ClientPlayerEntity player) {
        if (swapBackTicks <= 0) return;
        swapBackTicks--;
        if (swapBackTicks == 0) restoreSlot(player);
    }

    private Decision chooseDecision(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        double shieldScore = -1.0;
        if (isBlockingShield(target) && shieldHitsLeft > 0 && findAxeSlot(player) >= 0) {
            shieldScore = 100.0;
        }

        double fall = player.fallDistance;
        double smashScore = -1.0;
        if (CombatMath.isSmash(fall, player.isGliding()) && fall >= smashFall.getValue()
            && findMaceSlot(player, true) >= 0) {
            smashScore = 60.0 + CombatMath.smashBonus(fall) * 0.1;
        }

        double breachScore = -1.0;
        if (target.isOnGround() && target.getArmor() > 0 && findMaceSlot(player, false) >= 0) {
            double health = CombatMath.effectiveHealth(target.getHealth(), target.getAbsorptionAmount());
            breachScore = 40.0 + target.getArmor() * 0.5 + Math.max(0.0, 10.0 - health) * 0.2;
        }

        // A downward wind charge gives the fall height a mace smash needs. It
        // sits below a ready smash and a shield stun, but above a plain swing.
        double windScore = -1.0;
        boolean hasCharge = ItemUtil.handHolding(player, Items.WIND_CHARGE) != null
            || ItemUtil.findSlot(player, Items.WIND_CHARGE, true, false) >= 0;
        if (throwWindCharges.isEnabled() && windChargeTicks <= 0
            && player.isOnGround() && !CombatMath.isSmash(fall, player.isGliding()) && hasCharge) {
            windScore = 50.0;
        }

        double plainScore = -1.0;
        if (isMelee(player.getMainHandStack()) || findMaceSlot(player, false) >= 0) {
            plainScore = 10.0;
        }

        double best = Math.max(Math.max(shieldScore, windScore), Math.max(smashScore, breachScore));
        best = Math.max(best, plainScore);
        if (best < 0.0) return Decision.WAIT;
        if (best == shieldScore) return Decision.SHIELD_STUN;
        if (best == smashScore) return Decision.DENSITY_SMASH;
        if (best == breachScore) return Decision.BREACH_HIT;
        if (best == windScore) return Decision.WIND_CHARGE;
        return Decision.PLAIN_HIT;
    }

    private boolean perform(MinecraftClient client, ClientPlayerEntity player,
                            LivingEntity target, Decision chosen) {
        switch (chosen) {
            case SHIELD_STUN -> {
                int axe = findAxeSlot(player);
                if (axe < 0 || shieldHitsLeft <= 0 || !equip(player, axe)) return false;
                attack(client, player, target);
                shieldHitsLeft--;
                return true;
            }
            case DENSITY_SMASH -> {
                int mace = findMaceSlot(player, true);
                if (mace < 0 || !equip(player, mace)) return false;
                attack(client, player, target);
                return true;
            }
            case BREACH_HIT -> {
                int mace = findMaceSlot(player, false);
                if (mace < 0 || !equip(player, mace)) return false;
                attack(client, player, target);
                return true;
            }
            case WIND_CHARGE -> {
                if (!throwWindCharge(client, player)) return false;
                windChargeTicks = (int) windChargeCooldown.getValue();
                return true;
            }
            case PLAIN_HIT -> {
                if (!isMelee(player.getMainHandStack())) {
                    int mace = findMaceSlot(player, false);
                    if (mace < 0 || !equip(player, mace)) return false;
                }
                attack(client, player, target);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void attack(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        client.interactionManager.attackEntity(player, target);
        if (swing.isEnabled()) player.swingHand(Hand.MAIN_HAND);
    }

    /** Throws from the held hand or a hotbar slot, then restores the aim pitch. */
    private boolean throwWindCharge(MinecraftClient client, ClientPlayerEntity player) {
        if (client.interactionManager == null) return false;
        Hand hand = ItemUtil.handHolding(player, Items.WIND_CHARGE);
        if (hand == null) {
            int slot = ItemUtil.findSlot(player, Items.WIND_CHARGE, true, false);
            if (slot < 0 || !equip(player, slot)) return false;
            hand = Hand.MAIN_HAND;
        }

        float aimPitch = player.getPitch();
        player.setPitch((float) windChargePitch.getValue());
        try {
            client.interactionManager.interactItem(player, hand);
        } finally {
            player.setPitch(aimPitch);
        }
        return true;
    }

    private LivingEntity findTarget(MinecraftClient client, ClientPlayerEntity player) {
        double maxRange = range.getValue();
        Box area = player.getBoundingBox().expand(maxRange);
        Vec3d eyes = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        double cosLimit = Math.cos(Math.min(Math.PI, Math.toRadians(viewAngle.getValue()) / 2.0));

        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;
        for (Entity entity : client.world.getEntitiesByClass(Entity.class, area, e -> e != player)) {
            if (!(entity instanceof LivingEntity living) || !living.isAlive()) continue;
            // Combat-only pool: passive animals and villagers are never targets.
            if (!(living instanceof PlayerEntity) && !(living instanceof HostileEntity)) continue;
            if (!player.canSee(living)) continue;
            double distance = eyes.distanceTo(living.getEyePos());
            if (distance > maxRange) continue;
            Vec3d direction = living.getEyePos().subtract(eyes).normalize();
            double alignment = look.dotProduct(direction);
            if (alignment < cosLimit) continue;

            double score = switch (targetPriority.getValue()) {
                case LOWEST_HEALTH -> CombatMath.effectiveHealth(living.getHealth(), living.getAbsorptionAmount());
                case CROSSHAIR -> 1.0 - alignment;
                case NEAREST -> distance;
            };
            if (score < bestScore) {
                bestScore = score;
                best = living;
            }
        }
        return best;
    }

    private boolean equip(ClientPlayerEntity player, int slot) {
        if (slot < 0 || slot > 8) return false;
        int current = player.getInventory().getSelectedSlot();
        if (current == slot) return true;
        if (originalSlot == -1) originalSlot = current;
        ItemUtil.selectSlot(player, slot);
        return true;
    }

    private void restoreSlot(ClientPlayerEntity player) {
        if (player != null && originalSlot >= 0 && player.getInventory().getSelectedSlot() != originalSlot) {
            ItemUtil.selectSlot(player, originalSlot);
        }
        originalSlot = -1;
    }

    private static boolean isBlockingShield(LivingEntity entity) {
        boolean holdingShield = entity.getStackInHand(Hand.OFF_HAND).isOf(Items.SHIELD)
            || entity.getStackInHand(Hand.MAIN_HAND).isOf(Items.SHIELD);
        return holdingShield && entity.isBlocking();
    }

    private static boolean isMelee(ItemStack stack) {
        return stack.isIn(ItemTags.SWORDS) || stack.isIn(ItemTags.AXES) || stack.isOf(Items.MACE);
    }

    private static int findAxeSlot(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isIn(ItemTags.AXES)) return i;
        }
        return -1;
    }

    /** Prefers the requested enchantment, but still returns an ordinary mace as a fallback. */
    private static int findMaceSlot(ClientPlayerEntity player, boolean wantDensity) {
        String wanted = wantDensity ? "density" : "breach";
        int fallback = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isOf(Items.MACE)) continue;
            if (fallback < 0) fallback = i;
            if (hasEnchantment(stack, wanted)) return i;
        }
        return fallback;
    }

    private static boolean hasEnchantment(ItemStack stack, String pathFragment) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .anyMatch(enchantment -> enchantment.getKey()
                .map(key -> key.getValue().getPath().contains(pathFragment))
                .orElse(false));
    }

    private static String label(Decision value) {
        return switch (value) {
            case SHIELD_STUN -> "Shield stun";
            case DENSITY_SMASH -> "Density smash";
            case BREACH_HIT -> "Breach hit";
            case WIND_CHARGE -> "Wind charge setup";
            case PLAIN_HIT -> "Plain hit";
            case WAIT -> "Waiting";
        };
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || !showDecision.isEnabled() || mc.player == null || mc.options.hudHidden) return;
        OverlayRender.text(context, "Mace AI: " + decisionLabel, 4, 4, Category.MACE.getColor());
    }
}
