package com.yourname.utilmod.ui;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Applies the {@code assets/flux/menu_fonts.json} hook after a resource reload
 * and invalidates cached text so a swapped font is re-measured, not reused with
 * stale glyph widths.
 */
public final class MenuResourceReloadListener implements SimpleResourceReloadListener<MenuFonts.Config> {
    public static final Identifier ID = Identifier.of("flux", "menu_font_cache");
    public static final MenuResourceReloadListener INSTANCE = new MenuResourceReloadListener();

    private MenuResourceReloadListener() {}

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public CompletableFuture<MenuFonts.Config> load(ResourceManager manager, Executor executor) {
        // Parsing happens off the render thread; apply installs the result.
        return CompletableFuture.completedFuture(MenuFonts.readConfig(manager));
    }

    @Override
    public CompletableFuture<Void> apply(MenuFonts.Config data, ResourceManager manager, Executor executor) {
        // Runs as part of the client resource reload; install synchronously so
        // no frame can render with a half-applied font set.
        MenuFonts.apply(data);
        SlGuiPaint.invalidateTextCaches();
        return CompletableFuture.completedFuture(null);
    }
}
