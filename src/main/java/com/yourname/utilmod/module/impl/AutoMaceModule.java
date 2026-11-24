package com.yourname.utilmod.module.impl;
import com.yourname.utilmod.util.ItemUtil;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.util.RotationUtil;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Full Auto Mace combo module.
 *
 * Behaviour is based on the current mace-PvP client feature sets: Breach for
 * grounded shield fights, Density for fall strikes, wind-charge/elytra entries,
 * axe shield-stun combos, delayed snapback, and silent target acquisition.
 */
public class AutoMaceModule extends Module {
    private enum Trigger { SMART, FALL, VELOCITY, ELYTRA, ALWAYS, KEY }
    private enum MaceMode { AUTO, BREACH, DENSITY }
    private enum Priority { NEAREST, LOWEST_HEALTH, CROSSHAIR }
    private enum AimBone { HEAD, TORSO, FEET }

    private final EnumSetting<Trigger> trigger =
        new EnumSetting<>("Trigger", Trigger.class, Trigger.SMART).description("When to start: smart, fall, speed, elytra exit, key press, or always.");
    private final KeybindSetting triggerKey = new KeybindSetting("Trigger Key", GLFW.GLFW_KEY_X).description("Key that can start the combo when the trigger allows it.");
    private final BooleanSetting requireKey = new BooleanSetting("Require Key", false).description("Only attacks while the trigger key is held down.");
    private final NumberSetting minFallDist = new NumberSetting("Minimum Fall Distance", 2.5, 0.5, 20.0).description("Fall distance needed before a mace smash is attempted.");
    private final NumberSetting minFallVelocity = new NumberSetting("Minimum Fall Speed", 0.35, 0.05, 3.0).description("Downward speed needed before a smash is attempted.");
    private final NumberSetting elytraSpeed = new NumberSetting("Elytra Speed", 1.0, 0.2, 5.0).description("Flight speed that counts as an elytra entry for the trigger.");

    private final EnumSetting<MaceMode> maceMode =
        new EnumSetting<>("Mace Mode", MaceMode.class, MaceMode.AUTO).description("Which mace to use: automatic choice, Breach, or Density.");
    private final NumberSetting breachSlot = new NumberSetting("Breach Slot", 0.0, 0.0, 9.0).description("Hotbar slot with the Breach mace; 0 searches your inventory automatically.");
    private final NumberSetting densitySlot = new NumberSetting("Density Slot", 0.0, 0.0, 9.0).description("Hotbar slot with the Density mace; 0 searches your inventory automatically.");
    private final BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true).description("Swaps to the chosen mace instead of requiring it in your hand.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your original hotbar slot after the combo.");
    private final NumberSetting snapbackTicks = new NumberSetting("Snap Back Delay", 3.0, 0.0, 20.0).description("Ticks to wait before returning to your original hotbar slot.");

    private final BooleanSetting shieldStun = new BooleanSetting("Shield Stun Combo", true).description("Hits a shielded target with an axe before switching to the mace.");
    private final NumberSetting shieldHits = new NumberSetting("Shield Axe Hits", 1.0, 0.0, 5.0).description("How many axe hits are used against a target's shield.");
    private final NumberSetting axeSlot = new NumberSetting("Axe Slot (0 = Auto)", 0.0, 0.0, 9.0).description("Hotbar slot with the stun axe; 0 finds any axe automatically.");

    private final NumberSetting attackRange = new NumberSetting("Attack Range", 5.0, 1.0, 10.0).description("Maximum distance at which the module will pick a target.");
    private final NumberSetting fov = new NumberSetting("View Angle", 120.0, 10.0, 180.0).description("How wide the target search cone is, in degrees around your view.");
    private final EnumSetting<Priority> priority =
        new EnumSetting<>("Priority", Priority.class, Priority.CROSSHAIR).description("Picks the closest target, the one near your crosshair, or the weakest one.");
    private final BooleanSetting targetPlayers = new BooleanSetting("Target Players", true).description("Allow other players to be selected as targets.");
    private final BooleanSetting targetMobs = new BooleanSetting("Target Mobs", false).description("Allow mobs to be selected as targets.");
    private final BooleanSetting ignoreWalls = new BooleanSetting("Through Walls", false).description("Allow targeting an entity even when you cannot see it.");
    private final BooleanSetting stopOnDeath = new BooleanSetting("Stop On Death", true).description("Turns the module off after the current target dies.");

    private final BooleanSetting silentAim = new BooleanSetting("Silent Aim", true).description("Turns your view toward the target before the attack.");
    private final NumberSetting aimSpeed = new NumberSetting("Aim Speed", 180.0, 5.0, 180.0).description("How quickly your view moves toward the target, in degrees per step.");
    private final EnumSetting<AimBone> aimBone =
        new EnumSetting<>("Aim Bone", AimBone.class, AimBone.TORSO).description("Part of the target to aim at: head, body, or feet.");
    private final BooleanSetting restoreView = new BooleanSetting("Restore View", true).description("Puts your camera back where it was after the attack.");

    private final NumberSetting attackDelay = new NumberSetting("Attack Delay", 0.0, 0.0, 20.0).description("Ticks to wait before the first attack after a trigger.");
    private final NumberSetting comboDelay = new NumberSetting("Combo Delay", 2.0, 0.0, 20.0).description("Ticks to wait between hits during the combo.");
    private final BooleanSetting requireCooldown = new BooleanSetting("Require Cooldown", true).description("Waits for the normal attack cooldown before ground attacks.");

    private LivingEntity lastTarget;
    private float aimYaw;
    private float aimPitch;
    private boolean hasAimState;
    private int attackTimer;
    private int comboTimer;
    private int keyLatch;
    private int snapbackTimer;
    private int idleTimer;
    private int originalSlot = -1;
    private int activeSlot = -1;
    private int shieldHitsLeft;
    private boolean wasGliding;
    private int glideExitTimer;

    public AutoMaceModule() {
        super("Mace Swap", "Swaps to the mace for fall-strike hits, with Breach/Density swapping, shield stun and snapback.", Category.MACE);
        addSetting(trigger);
        addSetting(triggerKey);
        addSetting(requireKey);
        addSetting(minFallDist);
        addSetting(minFallVelocity);
        addSetting(elytraSpeed);
        addSetting(maceMode);
        addSetting(breachSlot);
        addSetting(densitySlot);
        addSetting(autoSwitch);
        addSetting(swapBack);
        addSetting(snapbackTicks);
        addSetting(shieldStun);
        addSetting(shieldHits);
        addSetting(axeSlot);
        addSetting(attackRange);
        addSetting(fov);
        addSetting(priority);
        addSetting(targetPlayers);
        addSetting(targetMobs);
        addSetting(ignoreWalls);
        addSetting(stopOnDeath);
        addSetting(silentAim);
        addSetting(aimSpeed);
        addSetting(aimBone);
        addSetting(restoreView);
        addSetting(attackDelay);
        addSetting(comboDelay);
        addSetting(requireCooldown);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        resetState();
    }

    @Override
    public void onDisable() {
        if (mc.player != null && swapBack.isEnabled() && originalSlot != -1) {
            selectSlot(mc.player, originalSlot);
        }
        resetState();
    }

    private void resetState() {
        lastTarget = null;
        hasAimState = false;
        attackTimer = 0;
        comboTimer = 0;
        keyLatch = 0;
        snapbackTimer = 0;
        idleTimer = 0;
        originalSlot = -1;
        activeSlot = -1;
        shieldHitsLeft = 0;
        wasGliding = false;
        glideExitTimer = 0;
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player.isSpectator() || player.getAbilities().creativeMode) return;

        if (tickSwapBack(player)) return;

        if (lastTarget != null && stopOnDeath.isEnabled()
            && (lastTarget.isDead() || lastTarget.getHealth() <= 0.0f)) {
            lastTarget = null;
            toggle();
            return;
        }

        if (triggerKey.wasPressed(client)) {
            keyLatch = Math.max(1, (int) attackDelay.getValue() + 1);
        } else if (keyLatch > 0) {
            keyLatch--;
        }
        if (requireKey.isEnabled() && !triggerKey.isHeld(client)) {
            idle(player);
            return;
        }
        if (!isTriggerReady(player)) {
            idle(player);
            return;
        }

        if (attackTimer < attackDelay.getValue()) {
            attackTimer++;
            return;
        }
        if (comboTimer > 0) {
            comboTimer--;
            return;
        }
        // Smash attacks ignore the attack cooldown (wiki: fall > 1.5 blocks, not
        // grounded, not gliding). Only gate normal/grounded swings on cooldown.
        if (requireCooldown.isEnabled() && !canSmash(player)
            && player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        LivingEntity target = findTarget(client);
        if (target == null) {
            idle(player);
            return;
        }

        if (target != lastTarget) {
            lastTarget = target;
            hasAimState = false;
            shieldHitsLeft = (int) shieldHits.getValue();
            attackTimer = 0;
        }

        float[] oldRotation = null;
        if (silentAim.isEnabled()) {
            oldRotation = applyAim(client, player, target);
        }

        boolean hit = performCombo(client, player, target);

        // Restore both client and server view only after the attack packet is out.
        if (oldRotation != null && restoreView.isEnabled()) {
            RotationUtil.restore(client, oldRotation);
        }
        if (hit) {
            attackTimer = 0;
            comboTimer = (int) comboDelay.getValue();
            idleTimer = 0;
            if (swapBack.isEnabled()) {
                snapbackTimer = Math.max(0, (int) snapbackTicks.getValue());
                if (snapbackTimer == 0) restoreSlot(player);
            }
        }
    }

    private boolean tickSwapBack(ClientPlayerEntity player) {
        if (snapbackTimer <= 0) return false;
        snapbackTimer--;
        if (snapbackTimer == 0 && originalSlot != -1) {
            restoreSlot(player);
            return true;
        }
        return false;
    }

    /**
     * A mace only smashes while airborne, out of elytra flight, after falling
     * at least 1.5 blocks. Attacking while gliding deals only the weak normal
     * hit, so gliding is tracked as a setup phase instead of a trigger.
     */
    private boolean isTriggerReady(ClientPlayerEntity player) {
        if (player.isGliding()) {
            wasGliding = player.getVelocity().length() >= elytraSpeed.getValue();
            glideExitTimer = wasGliding ? 20 : 0;
        } else if (glideExitTimer > 0) {
            glideExitTimer--;
        }

        boolean falling = !player.isOnGround() && !player.isGliding()
            && player.getVelocity().y < 0.0;
        boolean fall = canSmash(player) && player.fallDistance >= minFallDist.getValue();
        boolean velocity = falling && -player.getVelocity().y >= minFallVelocity.getValue();
        boolean elytraExit = glideExitTimer > 0 && canSmash(player)
            && player.fallDistance >= minFallDist.getValue();
        return switch (trigger.getValue()) {
            case SMART -> fall || velocity || elytraExit || keyLatch > 0;
            case FALL -> fall;
            case VELOCITY -> velocity;
            case ELYTRA -> elytraExit;
            case ALWAYS -> true;
            case KEY -> keyLatch > 0;
        };
    }

    private static boolean canSmash(ClientPlayerEntity player) {
        return !player.isOnGround() && !player.isGliding() && player.fallDistance >= 1.5f;
    }

    private boolean performCombo(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        if (shieldStun.isEnabled() && hasShield(target) && shieldHitsLeft > 0) {
            int axe = findAxeSlot(player);
            if (axe >= 0 && equip(player, axe)) {
                attack(client, player, target);
                shieldHitsLeft--;
                return true;
            }
        }

        int mace = findMaceSlot(player);
        if (mace < 0) return false;
        if (equip(player, mace)) {
            attack(client, player, target);
            shieldHitsLeft = 0;
            return true;
        }
        return false;
    }

    private void attack(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        if (client.getNetworkHandler() == null) return;
        // Raw packet guarantees slot -> rotation -> attack -> restore ordering and
        // prevents AutoBreach's attackEntity mixin from overriding the chosen mace.
        client.getNetworkHandler().sendPacket(
            PlayerInteractEntityC2SPacket.attack(target, player.isSneaking()));
        if (!player.isSpectator()) {
            // Replicate the vanilla client side effects of attackEntity without
            // routing through the mixin AutoBreach hooks. This is what actually
            // starts the cooldown, plays the swing sound and predicts the hit.
            player.attack(target);
            player.resetTicksSince();
        }
        player.swingHand(Hand.MAIN_HAND);
    }

    private LivingEntity findTarget(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        double rangeSq = attackRange.getValue() * attackRange.getValue();
        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;

        if (priority.getValue() == Priority.CROSSHAIR
            && client.crosshairTarget instanceof EntityHitResult entityHit
            && entityHit.getEntity() instanceof LivingEntity crosshair
            && isValid(player, crosshair)
            && player.squaredDistanceTo(crosshair) <= rangeSq) {
            return crosshair;
        }

        if (targetPlayers.isEnabled()) {
            for (PlayerEntity other : client.world.getPlayers()) {
                if (!isValid(player, other)) continue;
                double distSq = player.squaredDistanceTo(other);
                if (distSq > rangeSq || !canAimAt(player, other)) continue;
                double score = score(distSq, other);
                if (score < bestScore) {
                    bestScore = score;
                    best = other;
                }
            }
        }

        if (targetMobs.isEnabled()) {
            double r = attackRange.getValue();
            Box box = new Box(
                player.getX() - r, player.getY() - r, player.getZ() - r,
                player.getX() + r, player.getY() + r, player.getZ() + r
            );
            for (LivingEntity mob : client.world.getEntitiesByClass(LivingEntity.class, box,
                entity -> !(entity instanceof PlayerEntity))) {
                if (!isValid(player, mob)) continue;
                double distSq = player.squaredDistanceTo(mob);
                if (distSq > rangeSq || !canAimAt(player, mob)) continue;
                double score = score(distSq, mob);
                if (score < bestScore) {
                    bestScore = score;
                    best = mob;
                }
            }
        }
        return best;
    }

    private boolean isValid(PlayerEntity self, LivingEntity entity) {
        if (entity == self || entity.isDead() || entity.getHealth() <= 0.0f || entity.isSpectator()) return false;
        return entity instanceof PlayerEntity ? targetPlayers.isEnabled() : targetMobs.isEnabled();
    }

    private boolean canAimAt(PlayerEntity player, LivingEntity entity) {
        if (!ignoreWalls.isEnabled() && !player.canSee(entity)) return false;
        if (fov.getValue() >= 179.0) return true;
        Vec3d point = aimPoint(entity);
        Vec3d delta = point.subtract(player.getEyePos());
        double yaw = Math.toDegrees(Math.atan2(-delta.x, delta.z));
        double pitch = -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)));
        return Math.abs(wrapDegrees((float) (yaw - player.getYaw()))) <= fov.getValue() * 0.5f
            && Math.abs(pitch - player.getPitch()) <= fov.getValue() * 0.5f;
    }

    private double score(double distSq, LivingEntity entity) {
        return switch (priority.getValue()) {
            case LOWEST_HEALTH -> entity.getHealth();
            case NEAREST, CROSSHAIR -> distSq;
        };
    }

    private float[] applyAim(MinecraftClient client, ClientPlayerEntity player, LivingEntity target) {
        Vec3d point = aimPoint(target);
        Vec3d delta = point.subtract(player.getEyePos());
        float targetYaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
        float targetPitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)));
        targetPitch = clamp(targetPitch, -90.0f, 90.0f);

        if (!hasAimState) {
            aimYaw = player.getYaw();
            aimPitch = player.getPitch();
            hasAimState = true;
        }

        float step = aimSpeed.getFloat();
        float yawDiff = wrapDegrees(targetYaw - aimYaw);
        if (Math.abs(yawDiff) <= step) aimYaw = targetYaw;
        else aimYaw = wrapDegrees(aimYaw + Math.signum(yawDiff) * step);

        float pitchDiff = targetPitch - aimPitch;
        if (Math.abs(pitchDiff) <= step) aimPitch = targetPitch;
        else aimPitch += Math.signum(pitchDiff) * step;

        float[] old = new float[] { player.getYaw(), player.getPitch() };
        // Send the aim rotation to the server too, otherwise vanilla reach and
        // interaction checks validate against the un-rotated server-side view.
        RotationUtil.apply(client, aimYaw, clamp(aimPitch, -90.0f, 90.0f));
        return old;
    }

    private Vec3d aimPoint(LivingEntity entity) {
        return switch (aimBone.getValue()) {
            case HEAD -> entity.getEyePos();
            case TORSO -> entity.getEntityPos().add(0.0, entity.getHeight() * 0.55, 0.0);
            case FEET -> entity.getEntityPos();
        };
    }

    private boolean equip(ClientPlayerEntity player, int slot) {
        if (!autoSwitch.isEnabled() || slot < 0 || slot > 8) {
            return slot >= 0 && slot < 9 && player.getInventory().getSelectedSlot() == slot;
        }
        if (originalSlot == -1) originalSlot = player.getInventory().getSelectedSlot();
        selectSlot(player, slot);
        activeSlot = slot;
        return true;
    }

    private int findMaceSlot(ClientPlayerEntity player) {
        boolean smash = canSmash(player);
        boolean wantDensity = switch (maceMode.getValue()) {
            case DENSITY -> true;
            case BREACH -> false;
            case AUTO -> smash || trigger.getValue() == Trigger.ELYTRA || trigger.getValue() == Trigger.FALL;
        };

        int explicit = (int) (wantDensity ? densitySlot.getValue() : breachSlot.getValue());
        if (explicit >= 1 && explicit <= 9) {
            ItemStack stack = player.getInventory().getStack(explicit - 1);
            if (stack.isOf(Items.MACE)) return explicit - 1;
        }

        String enchant = wantDensity ? "density" : "breach";
        int fallback = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isOf(Items.MACE)) continue;
            if (fallback == -1) fallback = i;
            if (hasEnchant(stack, enchant)) return i;
        }
        return fallback;
    }

    private int findAxeSlot(ClientPlayerEntity player) {
        int explicit = (int) axeSlot.getValue();
        if (explicit >= 1 && explicit <= 9) {
            ItemStack stack = player.getInventory().getStack(explicit - 1);
            if (stack.isIn(ItemTags.AXES)) return explicit - 1;
        }
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isIn(ItemTags.AXES)) return i;
        }
        return -1;
    }

    private void idle(ClientPlayerEntity player) {
        idleTimer++;
        if (idleTimer > 6 && activeSlot != -1 && originalSlot != -1) {
            restoreSlot(player);
            idleTimer = 0;
        }
    }

    private void restoreSlot(ClientPlayerEntity player) {
        if (originalSlot != -1) selectSlot(player, originalSlot);
        originalSlot = -1;
        activeSlot = -1;
        snapbackTimer = 0;
        shieldHitsLeft = 0;
    }

    private static boolean hasShield(LivingEntity entity) {
        return entity.getStackInHand(Hand.OFF_HAND).isOf(Items.SHIELD)
            || entity.getStackInHand(Hand.MAIN_HAND).isOf(Items.SHIELD);
    }

    private static boolean hasEnchant(ItemStack stack, String name) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .anyMatch(enchantment -> enchantment.getKey()
                .map(key -> key.getValue().getPath().contains(name))
                .orElse(false));
    }

    private static void selectSlot(ClientPlayerEntity player, int slot) {
        if (slot < 0 || slot > 8 || player.getInventory().getSelectedSlot() == slot) return;
        ItemUtil.selectSlot(player, slot);
    }

    private static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) value -= 360.0f;
        if (value < -180.0f) value += 360.0f;
        return value;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
