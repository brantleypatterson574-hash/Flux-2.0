package com.yourname.utilmod.setting;

import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tiny persistent store for checklist selections.
 *
 * <p>Only selected IDs are written, so the file stays small even though the ESP
 * checklists contain every registered entity type and block.</p>
 */
public final class SelectionStore {
    private static final Map<String, Set<String>> DATA = new LinkedHashMap<>();
    private static boolean loaded;

    private SelectionStore() {}

    public static synchronized boolean has(String key) {
        ensureLoaded();
        return DATA.containsKey(key);
    }

    public static synchronized Set<String> load(String key) {
        ensureLoaded();
        Set<String> value = DATA.get(key);
        return value == null ? Set.of() : Set.copyOf(value);
    }

    public static synchronized void save(String key, Set<String> ids) {
        ensureLoaded();
        DATA.put(key, new LinkedHashSet<>(ids));
        write();
    }

    /**
     * Re-reads the file. Module constructors can run before the run directory
     * is available; call this once during client init so those early defaults
     * are replaced with the saved selections.
     */
    public static synchronized void reload() {
        loaded = false;
        DATA.clear();
        ensureLoaded();
    }

    private static void ensureLoaded() {
        if (loaded) return;

        Path file = file();
        // During early client init the run directory may not be available yet;
        // leave loaded false so a later call retries instead of losing selections.
        if (file == null) return;
        loaded = true;
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("#")) continue;
                int split = line.indexOf('=');
                if (split <= 0) continue;
                String key = line.substring(0, split).trim();
                Set<String> values = new LinkedHashSet<>();
                String raw = line.substring(split + 1).trim();
                if (!raw.isEmpty()) {
                    for (String id : raw.split(",")) {
                        if (!id.isBlank()) values.add(id.trim());
                    }
                }
                DATA.put(key, values);
            }
        } catch (IOException ignored) {
            // A missing or unreadable file simply means no saved selections.
        }
    }

    private static void write() {
        Path file = file();
        if (file == null) return;
        try {
            Path parent = file.getParent();
            if (parent != null) Files.createDirectories(parent);

            List<String> lines = new ArrayList<>();
            lines.add("# UtilMod checklist selections - key=id1,id2");
            DATA.forEach((key, ids) -> lines.add(key + "=" + String.join(",", ids)));
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
            // Persistence is best-effort; the in-memory checklist still works.
        }
    }

    private static Path file() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.runDirectory == null) return null;
        return client.runDirectory.toPath().resolve("config").resolve("utilmod-selections.txt");
    }
}
