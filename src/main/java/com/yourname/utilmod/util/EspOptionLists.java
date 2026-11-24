package com.yourname.utilmod.util;

import com.yourname.utilmod.setting.ChecklistSetting;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Builds the ESP checklist options directly from the vanilla registries.
 *
 * <p>This means newly added mobs and blocks from the game (or from other mods)
 * automatically appear in the checklist without any hard-coded list here.</p>
 */
public final class EspOptionLists {
    private EspOptionLists() {}

    // Block registry entries are immutable. ESP scans call blockId() for every
    // non-air block in range, so cache the Identifier string per Block instance.
    private static final Map<Block, String> BLOCK_IDS = new IdentityHashMap<>();

    // Six modules share the entity list and two share the block list. Building
    // them once avoids repeating thousands of translation lookups on startup.
    private static final class Cache {
        static final List<ChecklistSetting.Option> ENTITY_OPTIONS = List.copyOf(buildEntityOptions());
        static final Set<String> DEFAULT_ENTITY_IDS = Set.copyOf(buildDefaultEntityIds());
        static final List<ChecklistSetting.Option> BLOCK_OPTIONS = List.copyOf(buildBlockOptions());
    }

    /** Every registered entity type, including the player and all mobs. */
    public static List<ChecklistSetting.Option> entityOptions() {
        return Cache.ENTITY_OPTIONS;
    }

    /** Sensible starting selection: the player plus every hostile mob. */
    public static Set<String> defaultEntityIds() {
        return Cache.DEFAULT_ENTITY_IDS;
    }

    /** Every registered block except air variants (air is never highlighted). */
    public static List<ChecklistSetting.Option> blockOptions() {
        return Cache.BLOCK_OPTIONS;
    }

    private static List<ChecklistSetting.Option> buildEntityOptions() {
        List<ChecklistSetting.Option> options = new ArrayList<>();
        for (EntityType<?> type : Registries.ENTITY_TYPE) {
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            if (id == null) continue;
            options.add(new ChecklistSetting.Option(id.toString(), type.getName().getString()));
        }
        options.sort(Comparator.comparing(
            option -> option.label().toLowerCase(Locale.ROOT)
        ));
        return options;
    }

    private static Set<String> buildDefaultEntityIds() {
        Set<String> ids = new LinkedHashSet<>();
        ids.add("minecraft:player");
        for (EntityType<?> type : Registries.ENTITY_TYPE) {
            if (type.getSpawnGroup() != SpawnGroup.MONSTER) continue;
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            if (id != null) ids.add(id.toString());
        }
        return ids;
    }

    private static List<ChecklistSetting.Option> buildBlockOptions() {
        List<ChecklistSetting.Option> options = new ArrayList<>();
        for (Block block : Registries.BLOCK) {
            Identifier id = Registries.BLOCK.getId(block);
            if (id == null || block.getDefaultState().isAir()) continue;
            options.add(new ChecklistSetting.Option(id.toString(), block.getName().getString()));
        }
        options.sort(Comparator.comparing(
            option -> option.label().toLowerCase(Locale.ROOT)
        ));
        return options;
    }

    /** The ores/chests/obsidian that the old BlockEsp module highlighted by default. */
    public static Set<String> defaultBlockIds() {
        return Set.of(
            "minecraft:diamond_ore", "minecraft:deepslate_diamond_ore",
            "minecraft:emerald_ore", "minecraft:deepslate_emerald_ore",
            "minecraft:ancient_debris",
            "minecraft:gold_ore", "minecraft:deepslate_gold_ore", "minecraft:nether_gold_ore",
            "minecraft:iron_ore", "minecraft:deepslate_iron_ore",
            "minecraft:coal_ore", "minecraft:deepslate_coal_ore",
            "minecraft:redstone_ore", "minecraft:deepslate_redstone_ore",
            "minecraft:lapis_ore", "minecraft:deepslate_lapis_ore",
            "minecraft:copper_ore", "minecraft:deepslate_copper_ore",
            "minecraft:chest", "minecraft:trapped_chest",
            "minecraft:ender_chest", "minecraft:copper_chest",
            "minecraft:obsidian", "minecraft:crying_obsidian"
        );
    }

    public static String entityId(EntityType<?> type) {
        Identifier id = Registries.ENTITY_TYPE.getId(type);
        return id == null ? "" : id.toString();
    }

    public static String blockId(Block block) {
        String cached = BLOCK_IDS.get(block);
        if (cached == null) {
            Identifier id = Registries.BLOCK.getId(block);
            cached = id == null ? "" : id.toString();
            BLOCK_IDS.put(block, cached);
        }
        return cached;
    }
}
