package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.StringSetting;
import com.yourname.utilmod.ui.NotificationManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Client-side alert when a noteworthy dropped item appears near you. */
public class LootAlertsModule extends Module {
    public static LootAlertsModule INSTANCE;

    private enum ItemMode { VALUABLE, EVERYTHING, CUSTOM }

    private final EnumSetting<ItemMode> itemMode =
        new EnumSetting<>("Item Mode", ItemMode.class, ItemMode.VALUABLE)
            .description("Choose valuable items, every dropped item, or your own custom list.");
    private final StringSetting customNames = new StringSetting("Custom Item Names", "diamond, mace, elytra")
        .description("Used only in Custom mode. Comma-separated parts of item names to watch for.");
    private final NumberSetting range = new NumberSetting("Range", 28.0, 6.0, 96.0)
        .unit("blocks")
        .description("How far away a dropped item can be before it triggers an alert.");
    private final NumberSetting scanInterval = new NumberSetting("Scan Interval", 10.0, 2.0, 60.0)
        .unit("ticks")
        .description("Ticks between ground-item checks. Higher values use less CPU.");
    private final BooleanSetting showCount = new BooleanSetting("Show Stack Count", true)
        .description("Include how many items are in the stack in the alert message.");

    private final Set<ItemEntity> seen = Collections.newSetFromMap(new java.util.IdentityHashMap<>());
    private final List<String> customTerms = new ArrayList<>();
    private String cachedTerms;
    private int counter;

    public LootAlertsModule() {
        super("Loot Alerts", "Shows a corner alert when valuable items drop nearby. Reads local entities only; it never picks items up.",
            Category.HUD);
        INSTANCE = this;
        addSetting(itemMode);
        addSetting(customNames);
        addSetting(range);
        addSetting(scanInterval);
        addSetting(showCount);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        if (++counter < Math.max(2, (int) scanInterval.getValue())) return;
        counter = 0;

        seen.removeIf(entity -> !entity.isAlive());
        if (itemMode.getValue() == ItemMode.CUSTOM) refreshCustomTerms();

        double r = Math.max(1.0, range.getValue());
        Box box = new Box(
            client.player.getX() - r, client.player.getY() - r, client.player.getZ() - r,
            client.player.getX() + r, client.player.getY() + r, client.player.getZ() + r
        );
        List<ItemEntity> drops = client.world.getEntitiesByType(
            TypeFilter.instanceOf(ItemEntity.class), box, entity -> !seen.contains(entity));

        for (ItemEntity drop : drops) {
            seen.add(drop);
            ItemStack stack = drop.getStack();
            if (stack.isEmpty() || !interesting(stack)) continue;
            String label = stack.getName().getString();
            if (showCount.isEnabled() && stack.getCount() > 1) label += " x" + stack.getCount();
            NotificationManager.alert("Loot Alert", label);
        }
    }

    private boolean interesting(ItemStack stack) {
        ItemMode mode = itemMode.getValue();
        if (mode == ItemMode.EVERYTHING) return true;
        if (mode == ItemMode.VALUABLE) return valuable(stack);

        String name = stack.getName().getString().toLowerCase(Locale.ROOT);
        for (String term : customTerms) {
            if (name.contains(term)) return true;
        }
        return false;
    }

    private static boolean valuable(ItemStack stack) {
        return stack.isOf(Items.DIAMOND)
            || stack.isOf(Items.DIAMOND_BLOCK)
            || stack.isOf(Items.NETHERITE_INGOT)
            || stack.isOf(Items.NETHERITE_SCRAP)
            || stack.isOf(Items.NETHERITE_BLOCK)
            || stack.isOf(Items.ENCHANTED_BOOK)
            || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)
            || stack.isOf(Items.TOTEM_OF_UNDYING)
            || stack.isOf(Items.END_CRYSTAL)
            || stack.isOf(Items.ELYTRA)
            || stack.isOf(Items.MACE)
            || stack.isOf(Items.NETHER_STAR)
            || stack.isOf(Items.BEACON)
            || stack.isOf(Items.HEART_OF_THE_SEA);
    }

    private void refreshCustomTerms() {
        String source = customNames.getValue();
        if (source.equals(cachedTerms)) return;
        cachedTerms = source;
        customTerms.clear();
        for (String part : source.split(",")) {
            String term = part.trim().toLowerCase(Locale.ROOT);
            if (!term.isEmpty()) customTerms.add(term);
        }
    }

    @Override
    public void onEnable() {
        counter = 0;
        seen.clear();
        cachedTerms = null;
    }

    @Override
    public void onDisable() {
        counter = 0;
        seen.clear();
    }
}
