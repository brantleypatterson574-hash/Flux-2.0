package com.yourname.utilmod.server;

import com.yourname.utilmod.network.AntiCheatAlertPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight heuristic detector used by the server-side AntiCheat checks.
 * It only records suspicious events; it never kicks or bans anyone.
 */
public final class AntiCheatManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("UtilMod/AntiCheat");

    public static final double SPEED_LIMIT = 12.0;    // blocks/second
    public static final double VERTICAL_LIMIT = 10.0; // blocks/second
    public static final double REACH_LIMIT = 3.5;     // blocks from eye to target box

    /** Bot-like retaliations: very fast and unnaturally consistent. */
    private static final long REACTION_WINDOW_MS = 400L;
    private static final long REACTION_SUSPICIOUS_MS = 150L;
    private static final int REACTION_SAMPLES = 5;
    private static final double REACTION_MEAN_LIMIT = 120.0;
    private static final double REACTION_DEVIATION_LIMIT = 25.0;

    /** Same-tick Breach/Density swap before an attack, then swap back. */
    private static final long SWAP_WINDOW_MS = 250L;

    private static final Map<UUID, Violation> VIOLATIONS = new ConcurrentHashMap<>();
    private static final Map<UUID, MovementSample> MOVEMENT = new ConcurrentHashMap<>();
    private static final Map<UUID, CombatState> COMBAT = new ConcurrentHashMap<>();
    // Dedicated servers keep the detector on by default. An integrated server is
    // switched off until the client-side Anti-Cheat module is enabled.
    private static volatile boolean enabled = true;

    private AntiCheatManager() {}

    public static void setEnabled(boolean value) {
        enabled = value;
        if (!value) clearAll();
    }

    private record MovementSample(double x, double y, double z, float yaw, float pitch, long time) {}

    private static final class CombatState {
        long lastDamagedTime;
        long lastAttackTime;
        String lastAttackItemKind = "";
        long lastSlotChangeTime;
        String lastSlotKind = "";
        int preSwapAttacks;
        int postSwapBacks;
        int rotationSnaps;
        long rotationWindowStart;
        long lastReactionFlag;
        long lastRotationFlag;
        final Deque<Long> reactionSamples = new ArrayDeque<>();
    }

    public static final class Violation {
        private int count;
        private String reason = "";
        private String detail = "";
        private long lastTime;

        public int getCount() { return count; }
        public String getReason() { return reason; }
        public String getDetail() { return detail; }
        public long getLastTime() { return lastTime; }
    }

    public static void flag(ServerPlayerEntity player, String reason, String detail) {
        if (!enabled || player == null) return;
        UUID uuid = player.getUuid();
        if (uuid == null) return;

        Violation v = VIOLATIONS.computeIfAbsent(uuid, ignored -> new Violation());
        v.count++;
        v.reason = reason;
        v.detail = detail;
        v.lastTime = System.currentTimeMillis();

        if (v.count == 1 || v.count % 10 == 0) {
            LOGGER.warn("[UtilMod AntiCheat] {} ({}) flagged for {} ({}) - {} violation(s)",
                player.getName().getString(), uuid, reason, detail, v.count);
        }

        MinecraftServer server = player.getEntityWorld().getServer();
        if (server == null) return;
        AntiCheatAlertPayload alert = new AntiCheatAlertPayload(uuid, reason, detail, v.count);
        for (ServerPlayerEntity viewer : PlayerLookup.all(server)) {
            if (ServerPlayNetworking.canSend(viewer, AntiCheatAlertPayload.ID)) {
                ServerPlayNetworking.send(viewer, alert);
            }
        }
    }

    public static Violation getViolation(UUID uuid) {
        return uuid == null ? null : VIOLATIONS.get(uuid);
    }

    public static void clear(UUID uuid) {
        if (uuid == null) return;
        VIOLATIONS.remove(uuid);
        MOVEMENT.remove(uuid);
        COMBAT.remove(uuid);
    }

    public static void clearAll() {
        VIOLATIONS.clear();
        MOVEMENT.clear();
        COMBAT.clear();
    }

    private static CombatState combat(UUID uuid) {
        return COMBAT.computeIfAbsent(uuid, ignored -> new CombatState());
    }

    /** Called when a server player takes damage, to start a reaction-time sample. */
    public static void onDamaged(ServerPlayerEntity victim) {
        if (!enabled || victim == null || victim.getEntityWorld().isClient()) return;
        combat(victim.getUuid()).lastDamagedTime = System.currentTimeMillis();
    }

    /** Called from UpdateSelectedSlotC2SPacket so swap timing is measured exactly. */
    public static void onSlotPacket(ServerPlayerEntity player, int slot) {
        if (!enabled || player == null || player.getEntityWorld().isClient()) return;

        long now = System.currentTimeMillis();
        CombatState state = combat(player.getUuid());
        String kind = maceKind(player.getInventory().getStack(slot));

        if (state.lastAttackTime > 0 && now - state.lastAttackTime <= SWAP_WINDOW_MS) {
            boolean swappedAway = !kind.equals(state.lastAttackItemKind)
                && (kind.equals("other") || kind.equals("empty") || kind.equals("mace"));
            if (swappedAway) state.postSwapBacks++;
        }

        state.lastSlotChangeTime = now;
        state.lastSlotKind = kind;
        evaluateBreachSwap(player, state);
    }

    /** Called from ServerPlayerEntity.tick() on the logical server. */
    public static void checkMovement(ServerPlayerEntity player) {
        if (!enabled) return;
        if (player.getEntityWorld().isClient() || player.isDead() || player.isSpectator()
            || player.getVehicle() != null) {
            MOVEMENT.remove(player.getUuid());
            return;
        }

        long now = System.currentTimeMillis();
        MovementSample previous = MOVEMENT.get(player.getUuid());
        MovementSample current = new MovementSample(
            player.getX(), player.getY(), player.getZ(),
            player.getYaw(), player.getPitch(), now
        );
        MOVEMENT.put(player.getUuid(), current);

        if (previous == null) return;
        long elapsed = now - previous.time();
        if (elapsed < 40 || elapsed > 1000) return;

        double seconds = elapsed / 1000.0;
        double horizontal = Math.hypot(current.x() - previous.x(), current.z() - previous.z()) / seconds;
        if (horizontal > SPEED_LIMIT) {
            flag(player, "Speed", String.format("%.1f b/s", horizontal));
        }

        double vertical = (current.y() - previous.y()) / seconds;
        boolean creativeFlight = player.isCreative() || player.getAbilities().flying;
        if (vertical > VERTICAL_LIMIT && !creativeFlight) {
            flag(player, "Flight", String.format("%.1f b/s upward", vertical));
        }

        checkRotation(player, previous, current, now);
    }

    /** Called from ServerPlayerEntity.attack() on the logical server. */
    public static void checkAttack(PlayerEntity attacker, Entity target) {
        if (!enabled || attacker == null || target == null || attacker.getEntityWorld().isClient()) return;
        if (!(attacker instanceof ServerPlayerEntity player)) return;

        long now = System.currentTimeMillis();
        CombatState state = combat(player.getUuid());
        String heldKind = maceKind(player.getMainHandStack());
        state.lastAttackTime = now;
        state.lastAttackItemKind = heldKind;

        double distance = Math.sqrt(target.getBoundingBox().squaredMagnitude(attacker.getEyePos()));
        double granted = ReachManager.getPlayerReach(attacker.getUuid());
        double allowed = Math.max(REACH_LIMIT, granted + 0.5);
        if (distance > allowed) {
            flag(player, "Reach", String.format("%.1f blocks", distance));
        }

        // Same-tick attribute swap: slot packet arrived just before this attack.
        if (isBreachLike(heldKind) && heldKind.equals(state.lastSlotKind)
            && state.lastSlotChangeTime > 0
            && now - state.lastSlotChangeTime <= SWAP_WINDOW_MS) {
            state.preSwapAttacks++;
        }
        evaluateBreachSwap(player, state);

        // Retaliation reaction: only counts when the player was hit moments ago.
        if (state.lastDamagedTime > 0 && now - state.lastDamagedTime <= REACTION_WINDOW_MS) {
            long reaction = now - state.lastDamagedTime;
            if (reaction <= REACTION_SUSPICIOUS_MS) {
                state.reactionSamples.addLast(reaction);
                while (state.reactionSamples.size() > 20) state.reactionSamples.removeFirst();
                evaluateReaction(player, state);
            }
        }
    }

    private static void evaluateReaction(ServerPlayerEntity player, CombatState state) {
        if (state.reactionSamples.size() < REACTION_SAMPLES) return;
        long now = System.currentTimeMillis();
        if (now - state.lastReactionFlag < 2000L) return;

        int n = state.reactionSamples.size();
        double sum = 0.0;
        for (long sample : state.reactionSamples) sum += sample;
        double mean = sum / n;

        double variance = 0.0;
        for (long sample : state.reactionSamples) {
            double diff = sample - mean;
            variance += diff * diff;
        }
        double deviation = Math.sqrt(variance / n);

        if (mean <= REACTION_MEAN_LIMIT && deviation <= REACTION_DEVIATION_LIMIT) {
            flag(player, "Unhuman reaction",
                String.format("avg %.0f ms, deviation %.0f ms (%d samples)", mean, deviation, n));
            state.lastReactionFlag = now;
        }
    }

    private static void checkRotation(ServerPlayerEntity player, MovementSample previous,
                                      MovementSample current, long now) {
        double yawDelta = Math.abs(wrapDegrees(current.yaw() - previous.yaw()));
        double pitchDelta = Math.abs(current.pitch() - previous.pitch());
        if (yawDelta <= 35.0 && pitchDelta <= 25.0) return;

        CombatState state = combat(player.getUuid());
        if (now - state.rotationWindowStart > 5000L) {
            state.rotationWindowStart = now;
            state.rotationSnaps = 0;
        }
        state.rotationSnaps++;

        if (state.rotationSnaps >= 4 && now - state.lastRotationFlag > 2000L) {
            flag(player, "Unhuman rotation",
                String.format("%d snaps in 5s (last %.0f/%.0f deg)",
                    state.rotationSnaps, yawDelta, pitchDelta));
            state.lastRotationFlag = now;
        }
    }

    private static void evaluateBreachSwap(ServerPlayerEntity player, CombatState state) {
        if (state.preSwapAttacks < 3 || state.postSwapBacks < 3) return;
        flag(player, "AutoBreach swap",
            String.format("%d pre-attack swaps, %d swap-backs",
                state.preSwapAttacks, state.postSwapBacks));
        state.preSwapAttacks = 0;
        state.postSwapBacks = 0;
    }

    private static boolean isBreachLike(String kind) {
        return kind.equals("breach") || kind.equals("density") || kind.equals("both");
    }

    private static String maceKind(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "empty";
        if (!stack.isOf(Items.MACE)) return "other";

        boolean breach = hasEnchant(stack, Enchantments.BREACH);
        boolean density = hasEnchant(stack, Enchantments.DENSITY);
        if (breach && density) return "both";
        if (breach) return "breach";
        if (density) return "density";
        return "mace";
    }

    private static boolean hasEnchant(ItemStack stack, RegistryKey<Enchantment> key) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .anyMatch(entry -> entry.getKey().filter(key::equals).isPresent());
    }

    private static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) value -= 360.0f;
        if (value < -180.0f) value += 360.0f;
        return value;
    }
}
