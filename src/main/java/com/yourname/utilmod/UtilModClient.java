package com.yourname.utilmod;

import com.yourname.utilmod.friend.FriendManager;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.module.impl.*;
import com.yourname.utilmod.setting.ChecklistSetting;
import com.yourname.utilmod.setting.SelectionStore;
import com.yourname.utilmod.setting.Setting;
import com.yourname.utilmod.network.AntiCheatAlertPayload;
import com.yourname.utilmod.ui.ClickGuiScreen;
import com.yourname.utilmod.ui.DropdownMenuScreen;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.ui.MenuLayout;
import com.yourname.utilmod.ui.MenuResourceReloadListener;
import com.yourname.utilmod.ui.NotificationManager;
import com.yourname.utilmod.util.ConfigStore;
import com.yourname.utilmod.util.OverlayRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilModClient implements ClientModInitializer {
    public static final String MOD_ID = "utilmod";

    /** Configurable in Options -> Controls -> UtilMod. Defaults to Right Shift. */
    private static final KeyBinding MENU_KEY = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.utilmod.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.Category.create(Identifier.of(MOD_ID, "general"))
        )
    );

    private static List<Module> modules;
    private static final Map<Class<?>, Module> MODULES_BY_TYPE = new HashMap<>();

    @Override
    public void onInitializeClient() {
        modules = List.of(
            // Crystal
            new AutoTotemModule(),
            new CrystalAuraModule(),
            new AutoCrystalModule(),
            // Clean-room crystal PvP additions based on public Meteor module names and behaviour
            new AnchorAuraModule(),
            new AntiAnchorModule(),
            new AntiAnvilModule(),
            new AutoAnvilModule(),
            new AutoCityModule(),
            new AutoTrapModule(),
            new BedAuraModule(),
            new BurrowModule(),
            new HoleFillerModule(),
            new OffhandModule(),
            new SelfAnvilModule(),
            new SelfTrapModule(),
            new SelfWebModule(),
            // Sword
            new KillAuraModule(),
            new SwordAuraModule(),
            new SwordSwapModule(),
            new CritAssistModule(),
            new TriggerBotModule(),
            new AutoClickerModule(),
            new ComboAssisterModule(),
            new ReachModule(),           // Reach
            // Mace
            new AutoMaceModule(),      // Mace Swap
            new DensityAssistModule(),
            new AutoBreachModule(),    // Breach Swap
            new MaceAuraModule(),
            new SmashAssistModule(),
            // Movement
            new SprintModule(),
            new AutoWalkModule(),
            new SnapTapModule(),       // Replay last movement
            new KeyWindChargeModule(), // Wind Charge Jump
            new AutoJumpModule(),
            new NoKnockbackModule(),   // Velocity
            // Player
            new FastPlaceModule(),
            new AutoEatModule(),
            new AutoRespawnModule(),   // Auto Respawn
            new AutoMendModule(),      // Auto Mend
            new AutoLogModule(),       // Auto Log
            // Render
            new FullbrightModule(),
            new HitFxModule(),         // Hit Color
            new ZoomModule(),
            new CrosshairModule(),
            // HUD
            new XrayModule(),          // Personal Xray
            new ESPModule(),           // Entity ESP
            new TargetHudModule(),
            new NameTagsModule(),      // Nametags
            new KeybindsModule(),
            new HudInfoModule(),       // Stats HUD
            // World
            new WeatherModule(),
            // Donut
            new BaseFinderModule(),
            // Util
            new ReactionMonitorModule(),
            new AntiAfkModule(),
            new GuiThemeModule(),
            new ConfigProfilesModule(), // Config
            new HitboxAdjusterModule(),
            new AutoObsidianModule(),
            new AutoAnchorModule(),
            new SkyPaletteModule(),
            new ThreatRadarModule(),
            new ProjectileTrailsModule(),
            new TotemPopFxModule(),
            new SafeSpotEspModule(),
            new TracersModule(),
            new ItemHighlightModule(),
            new StorageEspModule(),
            new CityEspModule(),
            new VoidEspModule(),
            new BreadcrumbsModule(),
            new TrajectoriesModule(),
            new LogoutSpotsModule(),
            new BlockSelectionModule(),
            new AutoArmorModule(),
            new WeatherChangerModule(),
            new LightOverlayModule(),
            new BreakIndicatorsModule(),
            new AutoWebModule(),
            new SurroundModule(),
            new AntiBedModule(),
            new BowAimbotModule(),
            new NoWebModule(),
            new MaceAiModule(),
            new StunSlamModule(),
            new ShieldBreakerModule(),
            // Party / joke extras
            new AutoGgModule(),
            new ToastRemoverModule(),
            new ChatFilterModule(),
            new LootAlertsModule(),
            new HonestHudModule(),
            new DinnerboneModeModule(),
            new MoonJumpModule(),
            new BananaTooltipsModule(),
            new DiscoOverlayModule(),
            new ConfettiDeathsModule()
        );
        for (Module module : modules) {
            MODULES_BY_TYPE.put(module.getClass(), module);
        }

        FriendManager.init();

        // Font glyph widths and fitted labels are cached; a resource-pack reload
        // must re-measure them or text keeps the old pack's metrics.
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(MenuResourceReloadListener.INSTANCE);

        ConfigStore.load(modules);
        // Module constructors can run before the run directory exists. Re-read
        // checklist selections now and push them into every checklist setting.
        SelectionStore.reload();
        for (Module module : modules) {
            for (Setting setting : module.getSettings()) {
                if (setting instanceof ChecklistSetting checklist) checklist.reloadFromStore();
            }
        }
        // Profiles are stored in their own file for the same early-init reason.
        for (Module module : modules) {
            if (module instanceof ConfigProfilesModule profiles) profiles.reloadFromDisk();
        }
        // Toast stack is theme-driven and cheap when idle; enable it once so
        // toggle feedback and Reaction Monitor alerts are actually visible.
        NotificationManager.setEnabled(true);

        ClientPlayNetworking.registerGlobalReceiver(
            AntiCheatAlertPayload.ID,
            (payload, context) -> { }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MENU_KEY.wasPressed()) {
                if (client.currentScreen == null) {
                    GuiThemeModule menuTheme = GuiThemeModule.INSTANCE;
                    client.setScreen(menuTheme != null && menuTheme.menuLayout() == MenuLayout.DROPDOWN
                        ? new DropdownMenuScreen(modules) : new ClickGuiScreen(modules));
                }
            }
        });

        // 1.21.11: HudRenderCallback is deprecated - use the identified HUD element registry.
        // One dispatcher replaces 30 individual callbacks. It walks modules in the
        // same order as before, so the visual draw order is unchanged.
        HudElementRegistry.addLast(Identifier.of(MOD_ID, "hud"), (context, deltaTracker) -> {
            OverlayRender.beginFrame();
            for (Module module : modules) {
                if (!module.isEnabled()) continue;
                if (module instanceof HudOverlay overlay) {
                    overlay.renderOverlay(context);
                }
            }

            // Toasts render last so Reaction Monitor / totem alerts stay on top
            // of ESP, Target HUD and the other overlays.
            // Keep toasts and the ClickGUI on the same palette before the menu
            // module exists (first frame / headless tests).
            GuiTheme theme = GuiThemeModule.INSTANCE != null
                ? GuiThemeModule.INSTANCE.getTheme() : GuiTheme.MATRIX;
            NotificationManager.render(context, NotificationManager.Position.TOP_RIGHT,
                theme.accent, theme.panel, theme.text, theme.textSecondary);
        });
    }

    public static List<Module> getModules() {
        return modules;
    }

    /** True if a module of the given type is currently enabled. O(1) map lookup. */
    public static boolean isModuleEnabled(Class<? extends Module> type) {
        Module module = MODULES_BY_TYPE.get(type);
        return module != null && module.isEnabled();
    }

    public static KeyBinding getMenuKey() {
        return MENU_KEY;
    }
}
