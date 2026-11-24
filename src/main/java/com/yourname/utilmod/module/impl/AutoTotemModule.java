package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.ui.NotificationManager;
import com.yourname.utilmod.util.ColorUtil;
import com.yourname.utilmod.util.ItemUtil;
import com.yourname.utilmod.util.SlEspStyle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Auto Totem, expanded.
 *
 * <p>Keeps a totem in the offhand, reacts to pops and damage, optionally holds a
 * second totem in an empty main hand, keeps reserve totems in the hotbar, and
 * exposes a small status card. Inventory reshuffles can be delayed while the
 * player is moving, which some private servers prefer, without delaying the
 * offhand equip that actually saves the player.</p>
 */
public class AutoTotemModule extends Module implements HudOverlay {
    private enum Mode { ALWAYS, HEALTH, FIRST_HIT, SMART }
    private enum SearchOrder { HOTBAR_FIRST, INVENTORY_FIRST }
    private enum PopStyle { PLAIN, NOTCH, DREAM, TECHNOBLADE }

    public static AutoTotemModule INSTANCE;

    private final EnumSetting<Mode> mode = new EnumSetting<>("Mode", Mode.class, Mode.SMART)
        .description("Always keeps a totem, or only reacts at low health, on first damage, or using the smart threat rules below.");
    private final NumberSetting healthThreshold = new NumberSetting("Health Threshold", 10.0, 1.0, 20.0)
        .description("Effective health at or below which Health, First Hit and Smart modes equip a totem.");
    private final BooleanSetting includeAbsorption = new BooleanSetting("Include Absorption", true)
        .description("Adds absorption hearts to the health check so shields count as survival health.");
    private final BooleanSetting detectPop = new BooleanSetting("Equip After Pop", true)
        .description("Watches for a totem being consumed and immediately queues a replacement.");
    private final BooleanSetting popAlert = new BooleanSetting("Pop Alert", true)
        .description("Shows a toast when a totem pop is detected. Toasts are always available; no extra module is required.");
    private final BooleanSetting fallThreat = new BooleanSetting("Fall Threat", true)
        .description("In Smart mode, protects when the player's fall distance exceeds the threshold below.");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", 8.0, 1.0, 40.0)
        .description("Fall distance that counts as a Smart-mode threat.");
    private final BooleanSetting elytraThreat = new BooleanSetting("Elytra Threat", true)
        .description("In Smart mode, protects while gliding faster than the speed below.");
    private final NumberSetting elytraSpeed = new NumberSetting("Elytra Speed", 1.2, 0.2, 5.0)
        .description("Gliding speed that counts as a Smart-mode threat.");
    private final NumberSetting delayTicks = new NumberSetting("Delay", 0.0, 0.0, 40.0)
        .description("Ticks to wait after a detected pop before replacing the totem. 0 replaces immediately.");
    private final EnumSetting<SearchOrder> searchOrder =
        new EnumSetting<>("Search Order", SearchOrder.class, SearchOrder.INVENTORY_FIRST)
            .description("Chooses whether spare totems are taken from the inventory or hotbar first.");
    private final NumberSetting reserveTotems = new NumberSetting("Reserve Totems", 0.0, 0.0, 8.0)
        .description("Keeps this many totems as a reserve instead of equipping the last one.");
    private final BooleanSetting fillHotbar = new BooleanSetting("Fill Hotbar", true)
        .description("Moves spare totems into empty hotbar slots for quick manual swaps.");
    private final NumberSetting reserveSlots = new NumberSetting("Reserve Slots", 1.0, 0.0, 9.0)
        .description("How many hotbar slots should hold totem stacks when Fill Hotbar is on.");
    private final BooleanSetting returnPrevious = new BooleanSetting("Return Previous Item", true)
        .description("Returns the item that was originally in the offhand once a totem is no longer needed.");
    private final BooleanSetting survivalOnly = new BooleanSetting("Survival Only", true)
        .description("Skips all logic in creative and spectator modes.");
    private final BooleanSetting doubleHand = new BooleanSetting("Double Hand", false)
        .description("Also puts a totem in an empty main hand while the offhand is protected. Off by default so weapons are not replaced.");
    private final BooleanSetting pauseMovementClicks = new BooleanSetting("Pause Movement Clicks", true)
        .description("Delays reserve and double-hand inventory reshuffles while sprinting or moving fast. The offhand equip is never delayed.");
    private final NumberSetting movementThreshold = new NumberSetting("Movement Threshold", 0.15, 0.02, 1.0)
        .description("Horizontal speed above which movement-click pausing applies.");
    private final BooleanSetting showStatus = new BooleanSetting("Show Status", false)
        .description("Draws a small card showing whether a totem is ready, the total count and the active mode.");
    private final NumberSetting statusX = new NumberSetting("Status Position X", 4.0, 0.0, 1000.0)
        .description("Horizontal position of the status card in screen pixels.").advanced();
    private final NumberSetting statusY = new NumberSetting("Status Position Y", 120.0, 0.0, 1000.0)
        .description("Vertical position of the status card in screen pixels.").advanced();
    private final NumberSetting statusColor = new NumberSetting("Status Color", 0x44DD88, 0x000000, 0xFFFFFF)
        .description("Accent colour for the status card.").advanced();

    // Appended ScalableLux TotemPopCustomizer / Totem Pops HUD behaviour.
    private final BooleanSetting popCounter = new BooleanSetting("Pop Counter", true)
        .description("Shows a local, record-only totem pop board. Never kicks or bans anyone.");
    private final NumberSetting maxPlayers = new NumberSetting("Max Players", 5.0, 1.0, 20.0)
        .description("Maximum number of players shown on the pop board.").advanced();
    private final BooleanSetting includeSelf = new BooleanSetting("Include Self", true)
        .description("Counts your own totem pops on the board.").advanced();
    private final BooleanSetting hideWhenEmpty = new BooleanSetting("Hide When Empty", true)
        .description("Hides the pop board until the first totem pop is recorded.").advanced();
    private final EnumSetting<PopStyle> popStyle =
        new EnumSetting<>("Pop Style", PopStyle.class, PopStyle.PLAIN)
            .description("Cosmetic colour palette for the pop board.").advanced();
    private final NumberSetting popColor = new NumberSetting("Pop Color", 0xFFD75F, 0x000000, 0xFFFFFF)
        .description("Accent colour for the Plain pop style and recent-pop markers.").advanced();
    private final NumberSetting popTicks = new NumberSetting("Pop Ticks", 20.0, 0.0, 80.0)
        .description("How many ticks a recent pop marker stays visible.").advanced();
    private final NumberSetting popCounterX = new NumberSetting("Pop Board Position X", 4.0, 0.0, 1000.0)
        .description("Horizontal position of the totem pop board.").advanced();
    private final NumberSetting popCounterY = new NumberSetting("Pop Board Position Y", 170.0, 0.0, 1000.0)
        .description("Vertical position of the totem pop board.").advanced();

    // Local observations only. Nothing here sends packets or takes action against players.
    private final Map<String, Integer> popCounts = new LinkedHashMap<>();
    private final Map<String, Long> lastPopTimes = new LinkedHashMap<>();

    private int timer;
    private int popCooldown;
    private int lastTotemCount = -1;
    private float lastHealth = -1.0f;
    private String cachedStatus;
    private int cachedCount = -1;
    private String cachedMode;
    private String cachedLine1;
    private String cachedLine2;
    private int cachedWidth;
    private boolean hadTotem;
    private boolean popPending;
    private boolean hitPending;
    private boolean swapped;
    private ItemStack previousOffhand = ItemStack.EMPTY;
    private int previousSlot = -1;

    private boolean doubleSwapped;
    private int doubleSourceSlot = -1;
    private int doubleSelectedSlot = -1;

    public AutoTotemModule() {
        super("Auto Totem", "Keeps a totem in your offhand, detects pops and damage, and holds reserves.", Category.CRYSTAL);
        INSTANCE = this;
        addSetting(mode);
        addSetting(healthThreshold);
        addSetting(includeAbsorption);
        addSetting(detectPop);
        addSetting(popAlert);
        addSetting(fallThreat);
        addSetting(fallDistance);
        addSetting(elytraThreat);
        addSetting(elytraSpeed);
        addSetting(delayTicks);
        addSetting(searchOrder);
        addSetting(reserveTotems);
        addSetting(fillHotbar);
        addSetting(reserveSlots);
        addSetting(returnPrevious);
        addSetting(survivalOnly);
        addSetting(doubleHand);
        addSetting(pauseMovementClicks);
        addSetting(movementThreshold);
        addSetting(showStatus);
        addSetting(statusX);
        addSetting(statusY);
        addSetting(statusColor);
        addSetting(popCounter);
        addSetting(maxPlayers);
        addSetting(includeSelf);
        addSetting(hideWhenEmpty);
        addSetting(popStyle);
        addSetting(popColor);
        addSetting(popTicks);
        addSetting(popCounterX);
        addSetting(popCounterY);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        popCounts.clear();
        lastPopTimes.clear();
        timer = 0;
        popCooldown = 0;
        lastTotemCount = -1;
        lastHealth = -1.0f;
        hadTotem = false;
        popPending = false;
        hitPending = false;
        swapped = false;
        previousOffhand = ItemStack.EMPTY;
        previousSlot = -1;
        resetDoubleHand();
    }

    @Override
    public void onDisable() {
        popCounts.clear();
        lastPopTimes.clear();
        if (mc.player != null) {
            restore(mc);
            restoreDoubleHand(mc);
        }
        resetDoubleHand();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (survivalOnly.isEnabled()
            && (client.player.isSpectator() || client.player.getAbilities().creativeMode)) return;

        ItemStack offhand = client.player.getStackInHand(Hand.OFF_HAND);
        boolean totemInOffhand = offhand.isOf(Items.TOTEM_OF_UNDYING);
        int count = countTotems(client);
        float health = client.player.getHealth()
            + (includeAbsorption.isEnabled() ? client.player.getAbsorptionAmount() : 0.0f);

        if (popCooldown > 0) popCooldown--;
        if (lastTotemCount >= 0 && count < lastTotemCount && detectPop.isEnabled()) {
            markPop(client);
        }
        if (lastHealth >= 0.0f && health < lastHealth) hitPending = true;

        if (totemInOffhand) {
            hadTotem = true;
            popPending = false;
            hitPending = false;
            swapped = false;
            previousOffhand = ItemStack.EMPTY;
            previousSlot = -1;
            if (!movementBlocked(client)) {
                if (doubleHand.isEnabled()) updateDoubleHand(client, true);
                if (fillHotbar.isEnabled()) fillHotbarReserves(client);
            }
            lastTotemCount = countTotems(client);
            lastHealth = health;
            return;
        }

        if (hadTotem && popCooldown == 0) {
            // The offhand lost its totem even if the count check missed the pop.
            markPop(client);
        }
        hadTotem = false;

        if (timer > 0) {
            timer--;
            lastTotemCount = count;
            lastHealth = health;
            return;
        }

        if (shouldProtect(client, health)) {
            equip(client);
        } else if (swapped && returnPrevious.isEnabled()) {
            restore(client);
        }

        if (fillHotbar.isEnabled() && !movementBlocked(client)) fillHotbarReserves(client);
        if (doubleHand.isEnabled() && !movementBlocked(client)) updateDoubleHand(client, false);
        if (!doubleHand.isEnabled() && doubleSwapped) restoreDoubleHand(client);

        lastTotemCount = countTotems(client);
        lastHealth = health;
    }

    /**
     * Read-only observer for the vanilla totem-pop entity status. It records a
     * name and a count for the cosmetic board only - no packets, no targeting,
     * no moderation action of any kind.
     */
    public static void onEntityStatus(EntityStatusS2CPacket packet) {
        AutoTotemModule self = INSTANCE;
        if (self == null || !self.isEnabled()) return;
        // Pop Alert must work independently of the cosmetic Pop Counter board.
        if (!self.popCounter.isEnabled() && !self.popAlert.isEnabled()) return;
        if (packet.getStatus() != EntityStatuses.USE_TOTEM_OF_UNDYING) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        var entity = packet.getEntity(client.world);
        if (!(entity instanceof PlayerEntity player)) return;
        // Local pops are already detected from the inventory/health changes below.
        if (player == client.player) return;
        self.recordPop(player.getName().getString(), false);
    }

    private void recordPop(String name, boolean selfPop) {
        if (selfPop && !includeSelf.isEnabled()) return;
        popCounts.merge(name, 1, Integer::sum);
        lastPopTimes.put(name, mc.world != null ? mc.world.getTime() : 0L);
        if (!selfPop && popAlert.isEnabled()) {
            NotificationManager.alert("Totem Pop", name + " popped a totem");
        }
    }

    private void markPop(MinecraftClient client) {
        if (popCooldown > 0) return;
        popCooldown = 20;
        popPending = true;
        hadTotem = true;
        if (detectPop.isEnabled()) {
            timer = Math.max(timer, (int) delayTicks.getValue());
        }
        if (popAlert.isEnabled()) {
            NotificationManager.alert("Totem", "Totem popped - refilling offhand");
        }
        if (client.player != null) {
            recordPop(client.player.getName().getString(), true);
        }
    }

    private boolean shouldProtect(MinecraftClient client, float health) {
        return switch (mode.getValue()) {
            case ALWAYS -> true;
            case HEALTH -> health <= healthThreshold.getValue();
            case FIRST_HIT -> hitPending || health <= healthThreshold.getValue();
            case SMART -> health <= healthThreshold.getValue()
                || popPending
                || hitPending
                || (fallThreat.isEnabled() && client.player.fallDistance >= fallDistance.getValue())
                || (elytraThreat.isEnabled() && client.player.isGliding()
                    && client.player.getVelocity().length() >= elytraSpeed.getValue());
        };
    }

    private boolean movementBlocked(MinecraftClient client) {
        if (!pauseMovementClicks.isEnabled()) return false;
        Vec3d velocity = client.player.getVelocity();
        double horizontal = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        return client.player.isSprinting() || horizontal > movementThreshold.getValue();
    }

    private void equip(MinecraftClient client) {
        int slot = findTotemSlot(client, -1);
        if (slot < 0) return;
        if (reserveTotems.getValue() > 0 && countTotems(client) <= reserveTotems.getValue()) return;

        ItemStack oldOffhand = client.player.getStackInHand(Hand.OFF_HAND).copy();
        if (!swapped && !oldOffhand.isEmpty()) {
            previousOffhand = oldOffhand;
            previousSlot = slot;
        }

        ItemUtil.swapOffhand(client, slot);
        swapped = true;
        popPending = false;
        hitPending = false;
    }

    private void restore(MinecraftClient client) {
        if (previousOffhand.isEmpty()) {
            swapped = false;
            previousSlot = -1;
            return;
        }
        // The source slot can be rewritten by reserve/fill swaps or by a totem
        // pop while the offhand item sits there. Only reuse it if it still holds
        // the item we stored; otherwise find that item again before falling back
        // to an empty slot.
        int target = -1;
        if (previousSlot >= 0 && previousSlot < 36) {
            ItemStack there = client.player.getInventory().getStack(previousSlot);
            if (!there.isEmpty() && there.isOf(previousOffhand.getItem())) target = previousSlot;
        }
        if (target < 0) {
            for (int i = 0; i < 36; i++) {
                ItemStack stack = client.player.getInventory().getStack(i);
                if (!stack.isEmpty() && stack.isOf(previousOffhand.getItem())) {
                    target = i;
                    break;
                }
            }
        }
        if (target < 0) target = firstEmptySlot(client);
        if (target < 0) return;

        ItemUtil.swapOffhand(client, target);
        previousOffhand = ItemStack.EMPTY;
        previousSlot = -1;
        swapped = false;
    }

    /** Double Hand: place a spare totem in the main hand only when it is empty. */
    private void updateDoubleHand(MinecraftClient client, boolean totemInOffhand) {
        if (!totemInOffhand) {
            restoreDoubleHand(client);
            return;
        }
        ItemStack main = client.player.getMainHandStack();
        if (doubleSwapped) {
            if (!main.isOf(Items.TOTEM_OF_UNDYING)) restoreDoubleHand(client);
            return;
        }
        if (!main.isEmpty()) return;
        int selected = client.player.getInventory().getSelectedSlot();
        int slot = findTotemSlot(client, selected);
        if (slot < 0 || slot == selected) return;

        doubleSelectedSlot = selected;
        doubleSourceSlot = slot;
        ItemUtil.swapHotbar(client, slot, selected);
        doubleSwapped = true;
    }

    private void restoreDoubleHand(MinecraftClient client) {
        if (!doubleSwapped) return;
        if (doubleSourceSlot >= 0 && doubleSourceSlot < 36 && doubleSelectedSlot >= 0 && doubleSelectedSlot < 9) {
            ItemUtil.swapHotbar(client, doubleSourceSlot, doubleSelectedSlot);
        }
        ItemUtil.selectSlot(client.player, Math.max(0, Math.min(8, doubleSelectedSlot)));
        resetDoubleHand();
    }

    private void resetDoubleHand() {
        doubleSwapped = false;
        doubleSourceSlot = -1;
        doubleSelectedSlot = -1;
    }

    private int findTotemSlot(MinecraftClient client, int exclude) {
        int size = Math.min(36, client.player.getInventory().size());
        if (searchOrder.getValue() == SearchOrder.HOTBAR_FIRST) {
            for (int i = 0; i < Math.min(9, size); i++) {
                if (i != exclude && isTotem(client.player.getInventory().getStack(i))) return i;
            }
            for (int i = 9; i < size; i++) {
                if (i != exclude && isTotem(client.player.getInventory().getStack(i))) return i;
            }
        } else {
            for (int i = size - 1; i >= 9; i--) {
                if (i != exclude && isTotem(client.player.getInventory().getStack(i))) return i;
            }
            for (int i = 0; i < Math.min(9, size); i++) {
                if (i != exclude && isTotem(client.player.getInventory().getStack(i))) return i;
            }
        }
        return -1;
    }

    private int firstEmptySlot(MinecraftClient client) {
        int size = Math.min(36, client.player.getInventory().size());
        for (int i = 0; i < size; i++) {
            if (client.player.getInventory().getStack(i).isEmpty()) return i;
        }
        return -1;
    }

    private void fillHotbarReserves(MinecraftClient client) {
        int wanted = (int) reserveSlots.getValue();
        if (wanted <= 0) return;
        int inHotbar = 0;
        for (int i = 0; i < 9; i++) {
            if (isTotem(client.player.getInventory().getStack(i))) inHotbar++;
        }
        if (inHotbar >= wanted) return;

        int size = Math.min(36, client.player.getInventory().size());
        for (int i = 9; i < size && inHotbar < wanted; i++) {
            if (!isTotem(client.player.getInventory().getStack(i))) continue;
            int empty = -1;
            for (int h = 0; h < 9; h++) {
                if (client.player.getInventory().getStack(h).isEmpty()
                    && h != client.player.getInventory().getSelectedSlot()) {
                    empty = h;
                    break;
                }
            }
            if (empty < 0) break;
            if (ItemUtil.swapHotbar(client, i, empty)) inHotbar++;
        }
    }

    private int countTotems(MinecraftClient client) {
        int count = 0;
        ItemStack offhand = client.player.getStackInHand(Hand.OFF_HAND);
        if (isTotem(offhand)) count += offhand.getCount();
        int size = Math.min(36, client.player.getInventory().size());
        for (int i = 0; i < size; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (isTotem(stack)) count += stack.getCount();
        }
        return count;
    }

    private static boolean isTotem(ItemStack stack) {
        return !stack.isEmpty() && stack.isOf(Items.TOTEM_OF_UNDYING);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || mc.options.hudHidden || mc.player == null || mc.world == null) return;
        if (!showStatus.isEnabled() && !popCounter.isEnabled()) return;

        GuiTheme theme = GuiThemeModule.INSTANCE != null
            ? GuiThemeModule.INSTANCE.getTheme() : GuiTheme.FLUX;

        if (showStatus.isEnabled()) {
            boolean ready = mc.player.getStackInHand(Hand.OFF_HAND).isOf(Items.TOTEM_OF_UNDYING);
            String status = ready ? "READY" : popPending ? "POPPED" : "EMPTY";
            int count = lastTotemCount >= 0 ? lastTotemCount : countTotems(mc);
            String modeLabel = mode.getDisplayValue();
            if (!status.equals(cachedStatus)
                || count != cachedCount
                || !modeLabel.equals(cachedMode)) {
                cachedLine1 = "TOTEM  " + status;
                cachedLine2 = "Count " + count + "   Mode " + modeLabel;
                cachedWidth = Math.max(mc.textRenderer.getWidth(cachedLine1),
                    mc.textRenderer.getWidth(cachedLine2)) + 16;
                cachedStatus = status;
                cachedCount = count;
                cachedMode = modeLabel;
            }

            int accent = ColorUtil.withAlpha(statusColor.getColorValue(), 255);
            int x = (int) statusX.getValue();
            int y = (int) statusY.getValue();
            int width = cachedWidth;

            context.fill(x - 4, y - 4, x + width + 4, y + 26, ColorUtil.withAlpha(theme.panel, 0xC0));
            context.fill(x - 4, y - 4, x - 1, y + 26, accent);
            context.drawText(mc.textRenderer, cachedLine1, x + 6, y, accent, false);
            context.drawText(mc.textRenderer, cachedLine2, x + 6, y + 12, theme.textSecondary, false);
        }

        if (popCounter.isEnabled()) drawPopBoard(context, theme);
    }

    /** Cosmetic, local-only totem pop board. No moderation or network effects. */
    private void drawPopBoard(DrawContext context, GuiTheme theme) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(popCounts.entrySet());
        if (!includeSelf.isEnabled() && mc.player != null) {
            String self = mc.player.getName().getString();
            entries.removeIf(entry -> entry.getKey().equals(self));
        }
        entries.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(entry -> entry.getValue())
            .reversed().thenComparing(entry -> entry.getKey()));

        int x = (int) popCounterX.getValue();
        int y = (int) popCounterY.getValue();
        boolean empty = entries.isEmpty();
        if (empty && hideWhenEmpty.isEnabled()) return;
        int limit = Math.max(1, (int) maxPlayers.getValue());
        if (entries.size() > limit) entries = entries.subList(0, limit);

        int width = 132;
        int height = 22 + entries.size() * 12;
        int accent = ColorUtil.withAlpha(popColor.getColorValue(), 255);
        context.fill(x - 4, y - 4, x + width + 4, y + height, ColorUtil.withAlpha(theme.panel, 0xC0));
        context.fill(x - 4, y - 4, x - 1, y + height, accent);
        context.drawText(mc.textRenderer, "TOTEM POPS", x + 6, y, accent, false);
        if (empty) {
            context.drawText(mc.textRenderer, "No pops yet", x + 6, y + 14,
                theme.textSecondary, false);
            return;
        }

        long now = mc.world.getTime();
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            int rgb = popEntryColor(i, entries.size());
            int rowY = y + 14 + i * 12;
            String count = "x" + entry.getValue();
            int countX = x + width - 6 - mc.textRenderer.getWidth(count);
            context.drawText(mc.textRenderer, entry.getKey(), x + 6, rowY, rgb, false);
            context.drawText(mc.textRenderer, count, countX, rowY, rgb, false);
            Long popped = lastPopTimes.get(entry.getKey());
            if (popped != null && now - popped <= (long) popTicks.getValue()) {
                context.fill(countX - 8, rowY + 1, countX - 3, rowY + 6, 0xFF000000 | accent);
            }
        }
    }

    private int popEntryColor(int index, int total) {
        int primary = popColor.getColorValue();
        return switch (popStyle.getValue()) {
            case PLAIN -> primary;
            case NOTCH -> SlEspStyle.palette(0xFF5555, 0xFFD75F, index, total);
            case DREAM -> SlEspStyle.palette(0x55E0FF, 0xB980FF, index, total);
            case TECHNOBLADE -> SlEspStyle.palette(0xFF77C8, 0xFFFFFF, index, total);
        };
    }
}
