package com.yourname.utilmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * One entity list shared by ESP, HitboxEsp and Tracers. Each module supplies the
 * set of registry entity IDs it wants highlighted; the cache scans for the union
 * once and every module filters the shared list by its own selection.
 */
public final class EntityTargetCache {
    public static final EntityTargetCache INSTANCE = new EntityTargetCache();

    private record Config(boolean enabled, Set<String> types, double range, int max, int interval) {}

    private final Map<Object, Config> configs = new IdentityHashMap<>();
    private final List<Entity> targets = new ArrayList<>();
    // Entity types are immutable registry entries, so their id strings can be
    // computed once instead of once per module per entity per frame.
    private static final Map<EntityType<?>, String> IDS = new IdentityHashMap<>();

    private Set<String> unionTypes = Set.of();
    private double unionRange;
    private int unionMax;
    private int unionInterval = Integer.MAX_VALUE;
    private boolean unionDirty = true;
    private long lastScanTick = -1L;
    private Object lastWorld;

    private EntityTargetCache() {}

    /** Called by each module on client tick. The cache takes the union of all enabled sources. */
    public void configure(Object owner, boolean enabled, Set<String> types,
                          double range, int max, int interval) {
        int safeInterval = Math.max(1, interval);
        Config previous = configs.get(owner);
        if (previous != null
            && previous.enabled() == enabled
            && previous.range() == range
            && previous.max() == max
            && previous.interval() == safeInterval
            && previous.types().equals(types)) {
            return;
        }
        Config config = new Config(enabled, Set.copyOf(types), range, max, safeInterval);
        configs.put(owner, config);
        unionDirty = true;
        // A changed checklist must show up immediately instead of waiting for the interval.
        lastScanTick = -1L;
    }

    public void remove(Object owner) {
        configs.remove(owner);
        unionDirty = true;
        lastScanTick = -1L;
    }

    /** Returns the shared list. Safe to call every frame; the scan is throttled by world time. */
    public List<Entity> targets(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) {
            targets.clear();
            return targets;
        }

        if (unionDirty) rebuildUnion();
        if (unionMax == 0 || unionInterval == Integer.MAX_VALUE || unionTypes.isEmpty()) {
            targets.clear();
            return targets;
        }

        long now = mc.world.getTime();
        if (lastWorld == mc.world && lastScanTick >= 0 && now >= lastScanTick
            && now - lastScanTick < unionInterval) {
            return targets;
        }
        lastWorld = mc.world;
        lastScanTick = now;
        scan(mc, unionTypes, unionRange, unionMax);
        return targets;
    }

    /** Rebuild the enabled-module union only when a module's configuration changes. */
    private void rebuildUnion() {
        Set<String> types = new HashSet<>();
        double range = 0.0;
        int max = 0, interval = Integer.MAX_VALUE;
        for (Config config : configs.values()) {
            if (!config.enabled) continue;
            types.addAll(config.types);
            range = Math.max(range, config.range);
            max = Math.max(max, config.max);
            interval = Math.min(interval, config.interval);
        }
        unionTypes = Set.copyOf(types);
        unionRange = range;
        unionMax = max;
        unionInterval = interval;
        unionDirty = false;
    }

    private void scan(MinecraftClient mc, Set<String> types, double range, int max) {
        targets.clear();
        Box area = new Box(
            mc.player.getX() - range, mc.player.getY() - range, mc.player.getZ() - range,
            mc.player.getX() + range, mc.player.getY() + range, mc.player.getZ() + range
        );
        targets.addAll(mc.world.getEntitiesByClass(Entity.class, area, entity -> {
            if (entity == mc.player) return false;
            if (entity.isRemoved() || entity.isSpectator()) return false;
            if (entity instanceof LivingEntity living && living.isDead()) return false;
            return types.contains(id(entity));
        }));

        if (targets.size() > max) {
            targets.sort((a, b) -> Double.compare(mc.player.squaredDistanceTo(a), mc.player.squaredDistanceTo(b)));
            while (targets.size() > max) targets.remove(targets.size() - 1);
        }
    }

    /** Registry id string for an entity, matching the values stored in the checklist. */
    public static String id(Entity entity) {
        return id(entity.getType());
    }

    /** Cached registry id string for an entity type. */
    public static String id(EntityType<?> type) {
        String cached = IDS.get(type);
        if (cached == null) {
            cached = EspOptionLists.entityId(type);
            IDS.put(type, cached);
        }
        return cached;
    }

    /** Cheap per-entity filter so a module only draws the types it selected. */
    public static boolean matches(Entity entity, Set<String> types) {
        return types.contains(id(entity));
    }
}
