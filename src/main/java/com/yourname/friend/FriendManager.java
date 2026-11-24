package com.yourname.utilmod.friend;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Client-side friend list with persistence and chat commands.
 *
 * <p>Use {@code /utilmod friend add <name>}, {@code remove}, {@code list}, and
 * {@code clear}. Names are case-insensitive and stored in
 * {@code config/utilmod-friends.txt} next to the game's other config files.</p>
 */
public final class FriendManager {
    private static final Set<String> FRIENDS = new LinkedHashSet<>();
    private static boolean loaded;

    private FriendManager() {}

    public static void init() {
        load();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var friend = ClientCommandManager.literal("friend")
                .executes(context -> {
                    context.getSource().sendFeedback(Text.literal(
                        "Usage: /utilmod friend <add|remove|list|clear> [name]"));
                    return 1;
                })
                .then(ClientCommandManager.literal("add")
                    .then(ClientCommandManager.argument("name", StringArgumentType.word())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            boolean added = add(name);
                            context.getSource().sendFeedback(Text.literal(added
                                ? "Added friend: " + name
                                : "Already a friend: " + name));
                            return added ? 1 : 0;
                        })))
                .then(ClientCommandManager.literal("remove")
                    .then(ClientCommandManager.argument("name", StringArgumentType.word())
                        .executes(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            boolean removed = remove(name);
                            context.getSource().sendFeedback(Text.literal(removed
                                ? "Removed friend: " + name
                                : "Not a friend: " + name));
                            return removed ? 1 : 0;
                        })))
                .then(ClientCommandManager.literal("list")
                    .executes(context -> {
                        load();
                        if (FRIENDS.isEmpty()) {
                            context.getSource().sendFeedback(Text.literal("No friends saved."));
                        } else {
                            context.getSource().sendFeedback(
                                Text.literal("Friends: " + String.join(", ", FRIENDS)));
                        }
                        return 1;
                    }))
                .then(ClientCommandManager.literal("clear")
                    .executes(context -> {
                        FRIENDS.clear();
                        save();
                        context.getSource().sendFeedback(Text.literal("Cleared friend list."));
                        return 1;
                    }));

            dispatcher.register(ClientCommandManager.literal("utilmod").then(friend));
        });
    }

    public static boolean isFriend(Entity entity) {
        return entity != null && isFriend(entity.getName().getString());
    }

    public static boolean isFriend(String name) {
        if (name == null || name.isEmpty()) return false;
        load();
        return FRIENDS.contains(normalize(name));
    }

    public static boolean add(String name) {
        if (name == null || name.isEmpty()) return false;
        load();
        boolean added = FRIENDS.add(normalize(name));
        if (added) save();
        return added;
    }

    public static boolean remove(String name) {
        if (name == null || name.isEmpty()) return false;
        load();
        boolean removed = FRIENDS.remove(normalize(name));
        if (removed) save();
        return removed;
    }

    public static void clear() {
        load();
        FRIENDS.clear();
        save();
    }

    public static List<String> names() {
        load();
        return List.copyOf(FRIENDS);
    }

    private static String normalize(String name) {
        return name.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
    }

    private static Path file() {
        return MinecraftClient.getInstance().runDirectory.toPath()
            .resolve("config").resolve("utilmod-friends.txt");
    }

    private static void load() {
        if (loaded) return;
        loaded = true;
        try {
            Path path = file();
            if (!Files.exists(path)) return;
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                String name = normalize(line);
                if (!name.isEmpty() && !name.startsWith("#")) FRIENDS.add(name);
            }
        } catch (Exception ignored) {
            // Missing or unreadable file simply means an empty friend list.
        }
    }

    private static void save() {
        try {
            Path path = file();
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);
            List<String> lines = new ArrayList<>(FRIENDS);
            Collections.sort(lines);
            lines.add(0, "# UtilMod friends - one name per line");
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // Persistence is best-effort; the in-memory list still works.
        }
    }
}
