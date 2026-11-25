package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Sends a short chat message after a nearby player dies. The packet is only
 * observed; nothing about the death or the chat is rewritten. Use it where chat
 * messages are allowed, such as singleplayer, LAN or a private server.
 */
public class AutoGgModule extends Module {
    public static AutoGgModule INSTANCE;

    private record Pending(String message, long dueAt) {}

    private final StringSetting message = new StringSetting("Message", "GG")
        .description("The chat text to send after the target dies.");
    private final NumberSetting delay = new NumberSetting("Delay", 400.0, 0.0, 3000.0)
        .unit("ms")
        .description("How long to wait after the death before sending the message.");
    private final NumberSetting range = new NumberSetting("Range", 48.0, 8.0, 160.0)
        .unit("blocks")
        .description("Only react to deaths within this distance of you.");
    private final BooleanSetting onlyMyKills = new BooleanSetting("Only My Kills", true)
        .description("Only send the message for players you attacked shortly before they died.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Ignore mob deaths. Turn off to congratulate defeated hostile mobs too.");

    private final Map<UUID, Long> recentTargets = new HashMap<>();
    private final List<Pending> pending = new ArrayList<>();

    public AutoGgModule() {
        super("Auto GG", "Sends a friendly chat message after a nearby player dies. Chat is visible to everyone on the server.",
            Category.UTIL);
        INSTANCE = this;
        addSetting(message);
        addSetting(delay);
        addSetting(range);
        addSetting(onlyMyKills);
        addSetting(playersOnly);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    /** Called from the existing attack hook. Only records who you hit, never aims. */
    public static void onAttack(PlayerEntity attacker, Entity target) {
        AutoGgModule module = INSTANCE;
        if (module == null || !module.isEnabled() || attacker == null || target == null) return;
        if (!(target instanceof PlayerEntity) || target == attacker) return;
        module.recentTargets.put(target.getUuid(), System.currentTimeMillis());
    }

    /** Called from the existing observation mixin. The status packet is unchanged. */
    public static void onEntityStatus(EntityStatusS2CPacket packet) {
        AutoGgModule module = INSTANCE;
        if (module == null || !module.isEnabled() || packet == null) return;
        if (packet.getStatus() != EntityStatuses.ADD_DEATH_PARTICLES) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        Entity entity = packet.getEntity(client.world);
        if (entity == null || entity == client.player) return;
        if (module.playersOnly.isEnabled() && !(entity instanceof PlayerEntity)) return;

        double maxDistance = Math.max(1.0, module.range.getValue());
        if (entity.distanceTo(client.player) > maxDistance) return;

        UUID id = entity.getUuid();
        if (module.onlyMyKills.isEnabled()) {
            Long attackedAt = module.recentTargets.get(id);
            if (attackedAt == null || System.currentTimeMillis() - attackedAt > 8_000L) return;
        }
        module.recentTargets.remove(id);

        String text = module.message.getValue().trim();
        if (text.isEmpty()) return;
        module.pending.add(new Pending(text,
            System.currentTimeMillis() + Math.max(0L, (long) module.delay.getValue())));
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || pending.isEmpty()) return;
        if (client.player == null || client.player.networkHandler == null) return;

        long now = System.currentTimeMillis();
        Iterator<Pending> iterator = pending.iterator();
        while (iterator.hasNext()) {
            Pending next = iterator.next();
            if (now < next.dueAt()) continue;
            client.player.networkHandler.sendChatMessage(next.message());
            iterator.remove();
        }
        recentTargets.values().removeIf(time -> now - time > 8_000L);
    }

    @Override
    public void onDisable() {
        pending.clear();
        recentTargets.clear();
    }
}
