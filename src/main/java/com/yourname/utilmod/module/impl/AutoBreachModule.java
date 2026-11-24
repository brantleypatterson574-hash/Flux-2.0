package com.yourname.utilmod.module.impl;
import com.yourname.utilmod.util.ItemUtil;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
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
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Same-tick breach / attribute swapping.
 *
 * The swap is driven by ClientPlayerInteractionManagerMixin, which calls
 * {@link #handleAttack(ClientPlayerEntity, Entity)} at the HEAD of attackEntity.
 * That makes the hotbar slot (and its UpdateSelectedSlotC2SPacket) go out just
 * before the attack packet, so the server applies Breach/Density to the swing.
 *
 * The pacing, menu-pause and slot-ownership guards are taken from the readable
 * helpers in ScalableLux's combat bundle. Encrypted payloads, launch checks and
 * native/obfuscation infrastructure are deliberately not part of this port.
 */
public class AutoBreachModule extends Module {
    private enum ClickMode { LEGIT, DIRECT }

    /** Set by the constructor; the mixin delegates to this single instance. */
    public static AutoBreachModule INSTANCE;

    /** Module-driven swaps in the current second, sampled by the reaction monitor. */
    private static final AtomicInteger AUTO_SWAPS = new AtomicInteger();

    /** Returns and resets the sampled auto-swap count for this tick window. */
    public static int consumeAutoSwaps() {
        return AUTO_SWAPS.getAndSet(0);
    }

    private final NumberSetting targetSlot = new NumberSetting("Mace Slot", 1.0, 1.0, 9.0).description("Hotbar slot used when no better mace is found, counted from 1 to 9.");
    private final BooleanSetting smartSwitch = new BooleanSetting("Smart Switch", true).description("Picks Density while falling and Breach while on the ground.");
    private final BooleanSetting preferDensity = new BooleanSetting("Prefer Density", false).description("Looks for a Density mace first instead of Breach when Smart Switch is off.");
    private final BooleanSetting meleeOnly = new BooleanSetting("Melee Only", true).description("Only swaps to a melee weapon, never to another item type.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your previous hotbar slot after the swap.");
    private final NumberSetting swapDelay = new NumberSetting("Swap Delay", 2.0, 0.0, 10.0).description("Ticks to wait before returning to your original slot.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", false).description("Only triggers the swap when attacking another player.");
    private final NumberSetting minHealth = new NumberSetting("Minimum Health", 0.0, 0.0, 20.0).description("Ignores targets below this many health points, counted in half-hearts.");

    // Appended settings: existing config lines keep their original values.
    private final EnumSetting<ClickMode> clickAfterSwap = new EnumSetting<>(
        "Click After Swap", ClickMode.class, ClickMode.LEGIT)
        .description("Legit waits a short, varied moment after a slot swap; Direct clicks immediately after it.");
    private final NumberSetting clickDelay = new NumberSetting("Click Pacing (Ms)", 3.0, 0.0, 12.0)
        .description("Base pause between a hotbar swap and the click that follows it.");
    private final BooleanSetting pauseInMenus = new BooleanSetting("Pause Combat In Menus", true)
        .description("Stops swap/attack automation while a GUI, chat, inventory or the pause menu is open.");
    private final BooleanSetting respectUserSwaps = new BooleanSetting("Respect User Swaps", true)
        .description("Cancels a pending swap-back if you change hotbar slots yourself.");

    private static final long SWAP_LOCK_NANOS = 25_000_000L;

    private int originalSlot = -1;
    private int swapBackTicks = 0;
    private int lastKnownSlot = -1;
    private long lastSwapNanos = Long.MIN_VALUE;
    private boolean moduleSwapPending;

    public AutoBreachModule() {
        super("Breach Swap", "Same-tick Breach mace swap on attack (attribute swapping).", Category.MACE);
        INSTANCE = this;
        addSetting(targetSlot);
        addSetting(smartSwitch);
        addSetting(preferDensity);
        addSetting(meleeOnly);
        addSetting(swapBack);
        addSetting(swapDelay);
        addSetting(playersOnly);
        addSetting(minHealth);
        addSetting(clickAfterSwap);
        addSetting(clickDelay);
        addSetting(pauseInMenus);
        addSetting(respectUserSwaps);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    /**
     * Called from the attack mixin before the attack packet is sent.
     *
     * @return true when this module changed the held slot for this attack.
     */
    public static boolean handleAttack(ClientPlayerEntity player, Entity target) {
        AutoBreachModule self = INSTANCE;
        if (self == null || !self.isEnabled() || player == null) return false;
        if (self.pauseInMenus.isEnabled() && MinecraftClient.getInstance().currentScreen != null) return false;

        // A null target (manual trigger) is always allowed; filters only apply to real attacks.
        if (target != null) {
            if (self.playersOnly.isEnabled() && !(target instanceof PlayerEntity)) return false;
            if (target instanceof LivingEntity living && living.getHealth() < self.minHealth.getValue()) return false;
        }

        int current = player.getInventory().getSelectedSlot();
        if (self.respectUserSwaps.isEnabled()
            && self.lastKnownSlot != -1 && current != self.lastKnownSlot && !self.moduleSwapPending) {
            self.clearPendingSwap();
        }
        self.lastKnownSlot = current;
        self.moduleSwapPending = false;

        // Smart Switch: Density while falling, Breach while grounded (BetterMaceSwap behaviour).
        boolean falling = !player.isOnGround() && player.getVelocity().y < -0.3;
        boolean wantDensity = self.smartSwitch.isEnabled()
            ? falling
            : self.preferDensity.isEnabled();

        int slot = self.findBestMace(player, wantDensity);
        if (slot == -1) {
            // Fall back to the user-configured hotbar slot.
            slot = (int) self.targetSlot.getValue() - 1;
        }
        if (slot < 0 || slot > 8) return false;
        // Check the item we would actually attack with, not the item currently held.
        if (self.meleeOnly.isEnabled() && !isMelee(player.getInventory().getStack(slot))) return false;

        if (slot == current) {
            // Already on the target mace: just keep a pending swap-back alive.
            if (self.swapBack.isEnabled() && self.originalSlot != -1 && self.swapBackTicks > 0) {
                self.swapBackTicks = Math.max(0, (int) self.swapDelay.getValue());
            }
            return false;
        }

        // Swap-lock guard: one automated swap per attack window.
        long now = System.nanoTime();
        if (now - self.lastSwapNanos < SWAP_LOCK_NANOS) return false;
        self.lastSwapNanos = now;

        if (self.swapBack.isEnabled()) {
            self.originalSlot = current;
            self.swapBackTicks = Math.max(0, (int) self.swapDelay.getValue());
        } else {
            self.originalSlot = -1;
            self.swapBackTicks = 0;
        }

        selectSlot(player, slot);
        AUTO_SWAPS.incrementAndGet();
        self.moduleSwapPending = true;
        self.paceAfterSwap();
        return true;
    }

    /** Kept for backwards compatibility with anything calling the old helper directly. */
    public void triggerBreach() {
        if (!isEnabled() || mc.player == null) return;
        handleAttack(mc.player, null);
    }

    private void paceAfterSwap() {
        double base = clickDelay.getValue();
        if (clickAfterSwap.getValue() == ClickMode.DIRECT) {
            // Direct: only the configured few milliseconds, no extra spread.
            spin(base);
            return;
        }
        // Legit: a small deterministic spread so swaps do not all have an identical gap.
        long stamp = System.nanoTime();
        double spread = ((stamp >>> 18) & 0xFL) / 15.0 * 4.0;
        spin(base + spread);
    }

    /**
     * Short, bounded pause between a swap and the attack that follows it.
     * The old Thread.onSpinWait loop burned a full CPU core on the game thread
     * and could stall a frame. parkNanos yields the core, and the hard cap keeps
     * the pause well inside one frame even if the setting is raised.
     */
    private static void spin(double milliseconds) {
        if (!(milliseconds > 0.0) || !Double.isFinite(milliseconds)) return;
        long nanos = (long) (Math.min(milliseconds, 8.0) * 1_000_000.0);
        if (nanos <= 0L) return;
        java.util.concurrent.locks.LockSupport.parkNanos(nanos);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null) {
            resetState();
            return;
        }
        int current = client.player.getInventory().getSelectedSlot();
        if (respectUserSwaps.isEnabled()
            && lastKnownSlot != -1 && current != lastKnownSlot && !moduleSwapPending) {
            clearPendingSwap();
        }
        lastKnownSlot = current;
        moduleSwapPending = false;

        if (pauseInMenus.isEnabled() && client.currentScreen != null) return;
        if (swapBackTicks <= 0) return;

        swapBackTicks--;
        if (swapBackTicks == 0 && originalSlot != -1) {
            selectSlot(client.player, originalSlot);
            originalSlot = -1;
            moduleSwapPending = true;
        }
    }

    @Override
    public void onDisable() {
        if (originalSlot != -1 && mc.player != null) {
            selectSlot(mc.player, originalSlot);
        }
        resetState();
    }

    private void resetState() {
        clearPendingSwap();
        lastKnownSlot = -1;
    }

    private void clearPendingSwap() {
        originalSlot = -1;
        swapBackTicks = 0;
        moduleSwapPending = false;
        lastSwapNanos = Long.MIN_VALUE;
    }

    /** Changes the client slot and notifies the server in the same tick. */
    private static void selectSlot(ClientPlayerEntity player, int slot) {
        if (slot < 0 || slot > 8) return;
        ItemUtil.selectSlot(player, slot);
    }

    private int findBestMace(ClientPlayerEntity player, boolean wantDensity) {
        String wanted = wantDensity ? "density" : "breach";
        int fallback = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (!stack.isOf(Items.MACE)) continue;
            if (fallback == -1) fallback = i;
            if (hasEnchant(stack, wanted)) return i;
        }
        return fallback;
    }

    private static boolean hasEnchant(ItemStack stack, String name) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantments().stream()
            .anyMatch(enchantment -> enchantment.getKey()
                .map(key -> key.getValue().getPath().contains(name))
                .orElse(false));
    }

    private static boolean isMelee(ItemStack stack) {
        // 1.21.11: swords are identified via the item tag (SwordItem no longer exists).
        return stack.isIn(ItemTags.SWORDS) || stack.isIn(ItemTags.AXES) || stack.isOf(Items.MACE);
    }
}
