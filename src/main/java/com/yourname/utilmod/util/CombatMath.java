package com.yourname.utilmod.util;

/**
 * Deterministic combat math shared by the mace modules and the hitbox overlay.
 *
 * <p>Ported from the readable numeric helpers in ScalableLux's combat hitbox
 * bundle (smash/density/breach curves, strike envelope and effective health).
 * Only plain formulas are kept here: no obfuscation, launch checks, native code
 * or encrypted payloads are part of this port.</p>
 */
public final class CombatMath {
    /** A mace only smashes after falling more than 1.5 blocks, out of elytra flight. */
    public static final double SMASH_MIN_FALL = 1.5;
    public static final double DENSITY_PER_LEVEL_PER_BLOCK = 0.5;
    public static final double BREACH_PER_LEVEL = 0.15;
    public static final float CRIT_MULTIPLIER = 1.5f;
    public static final int CONTACT_COOLDOWN_TICKS = 10;
    public static final double BASE_ATTACK_DAMAGE = 1.0;

    private CombatMath() {}

    public static boolean isSmash(double fallDistance, boolean gliding) {
        return fallDistance > SMASH_MIN_FALL && !gliding;
    }

    /** Mace smash bonus by fall distance (1.5-3, 3-8, 8+ blocks). */
    public static double smashBonus(double fallDistance) {
        if (!(fallDistance > SMASH_MIN_FALL)) return 0.0;
        if (fallDistance <= 3.0) return 4.0 * fallDistance;
        if (fallDistance <= 8.0) return 12.0 + 2.0 * (fallDistance - 3.0);
        return 22.0 + (fallDistance - 8.0);
    }

    /** Adds the Density contribution to the base smash bonus. */
    public static double smashBonus(double fallDistance, int densityLevels) {
        double base = smashBonus(fallDistance);
        if (base <= 0.0) return 0.0;
        return base + DENSITY_PER_LEVEL_PER_BLOCK * Math.max(0, densityLevels) * fallDistance;
    }

    /** Charge-scaled weapon hit, with an optional critical-strike multiplier. */
    public static double rawHit(double baseDamage, float charge, boolean critical, double bonus) {
        float c = clamp01(charge);
        double damage = baseDamage * (0.2 + (double) (c * c) * 0.8);
        if (critical) damage *= CRIT_MULTIPLIER;
        return damage + Math.max(0.0, bonus);
    }

    public static double rawSmash(double baseDamage, float charge, boolean critical,
                                  double fallDistance, int densityLevels) {
        return rawHit(baseDamage, charge, critical, smashBonus(fallDistance, densityLevels));
    }

    /** Approximate post-armour damage using Minecraft's 2 + armour/4 curve. */
    public static double afterArmor(double damage, double armor, double armorToughness, int breachLevels) {
        if (damage <= 0.0) return 0.0;
        double effectiveArmor = Math.max(0.0, armor);
        double divisor = 2.0 + Math.max(0.0, armorToughness) / 4.0;
        double reduced = clamp(effectiveArmor - damage / divisor, effectiveArmor * 0.2, 20.0);
        double multiplier = clamp(reduced / 25.0 - BREACH_PER_LEVEL * Math.max(0, breachLevels), 0.0, 1.0);
        return damage * (1.0 - multiplier);
    }

    /** Health + absorption, the value a hit has to remove to kill a target. */
    public static float effectiveHealth(float health, float absorption) {
        return Math.max(0.0f, health) + Math.max(0.0f, absorption);
    }

    public static boolean isLethal(float health, float absorption, float predictedDamage) {
        if (predictedDamage <= 0.0f) return false;
        float effective = effectiveHealth(health, absorption);
        return effective > 0.0f && predictedDamage >= effective;
    }

    public static float fallDamage(double fallDistance, double safeFall, double damagePerBlock) {
        if (!(fallDistance > safeFall) || !(damagePerBlock > 0.0)) return 0.0f;
        return (float) Math.ceil((fallDistance - safeFall) * damagePerBlock);
    }

    public static float elytraImpactDamage(double speed) {
        if (!(speed > 0.0) || !Double.isFinite(speed)) return 0.0f;
        return (float) Math.max(0.0, speed * 10.0 - 3.0);
    }

    public static float maceSmashDamage(double fallDistance, int densityLevels, double baseDamage) {
        double result = Math.max(0.0, baseDamage);
        if (!(fallDistance > SMASH_MIN_FALL) || !Double.isFinite(fallDistance)) return (float) result;
        return (float) (result + smashBonus(fallDistance, densityLevels));
    }

    /**
     * Whether a moving target can still be caught by a strike this tick.
     * reach = player reach + target reach + 2 * closing tolerance + 1.
     */
    public static boolean inStrikeEnvelope(double distance, double playerReach,
                                           double targetReach, double closingSpeed) {
        return distance <= playerReach + targetReach + 2.0 * Math.max(0.0, closingSpeed) + 1.0;
    }

    /** Ticks until an attack is fully charged, used for patient smash setups. */
    public static int ticksToFullCharge(float progress, float perTick) {
        if (progress >= 1.0f || perTick <= 0.0f) return 0;
        return (int) Math.ceil((1.0 - progress) / perTick);
    }

    private static double clamp(double value, double min, double max) {
        return value < min ? min : (value > max ? max : value);
    }

    private static float clamp01(float value) {
        return value < 0.0f ? 0.0f : (value > 1.0f ? 1.0f : value);
    }
}
