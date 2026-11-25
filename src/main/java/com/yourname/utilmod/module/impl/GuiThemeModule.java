package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.ui.MenuLayout;
import com.yourname.utilmod.util.ConfigStore;

/**
 * Settings container for the ClickGUI appearance. The theme applies while the
 * GUI is open regardless of whether this module is toggled on.
 */
public class GuiThemeModule extends Module {
    public static GuiThemeModule INSTANCE;

    /** Shared by both menu layouts and the preview so the slider never lies. */
    public static final float MIN_GUI_SCALE = 0.5f;
    public static final float MAX_GUI_SCALE = 2.0f;

    /** Mockup toolbar order; appended SL_* themes still use enum order as a fallback. */
    private static final GuiTheme[] THEME_CYCLE = {
        GuiTheme.PINK, GuiTheme.PRESTIGE, GuiTheme.MATRIX, GuiTheme.UTILMOD,
        GuiTheme.METEOR, GuiTheme.FLUX, GuiTheme.LUMI, GuiTheme.SAKURA,
        GuiTheme.OCEAN, GuiTheme.SLATE
    };

    /** The dropped dropdown lists the same themes the title tag cycles through. */
    public static GuiTheme[] themeCycle() {
        return THEME_CYCLE.clone();
    }

    /** Used by the dropdown swatches; writes through so the choice survives a restart. */
    public void setTheme(GuiTheme value) {
        theme.setValue(value);
        persist();
    }

    // Pink is the shipped default; PRESTIGE remains config-stable for older saves.
    private final EnumSetting<GuiTheme> theme = new EnumSetting<>("Theme", GuiTheme.class, GuiTheme.PINK)
        .description("Colour palette for the menu and its panels.");
    private final EnumSetting<MenuLayout> menuLayout =
        new EnumSetting<>("Menu Layout", MenuLayout.class, MenuLayout.DROPDOWN)
            .description("Choose the dropdown flyouts or the full ClickGUI window.");
    private final NumberSetting guiScale = new NumberSetting("Menu Scale", 1.0, MIN_GUI_SCALE, MAX_GUI_SCALE)
        .description("Menu size on screen. Below 1.0 shrinks both layouts; above 1.0 enlarges the dropdown shell. 1.0 is default size.");
    private final NumberSetting fontSize = new NumberSetting("Font Size", 1.0, 0.85, 1.25)
        .description("Scales the default Minecraft menu text between the supported label sizes.");
    private final NumberSetting sidebarWidth = new NumberSetting("Sidebar Width", 196.0, 160.0, 260.0)
        .description("Width of the category rail in the ClickGUI.");
    private final NumberSetting categorySpacing = new NumberSetting("Category Spacing", 52.0, 36.0, 80.0)
        .description("Vertical space between category rows in the ClickGUI sidebar.");
    private final NumberSetting panelOpacity = new NumberSetting("Panel Opacity", 100.0, 20.0, 100.0)
        .unit("%")
        .description("Panel background opacity. Lower values let the world show through more.");
    private final NumberSetting overlayOpacity = new NumberSetting("Overlay Opacity", 100.0, 10.0, 100.0)
        .unit("%")
        .description("Darkening behind the menu. Lower values keep the world visible.");
    private final BooleanSetting roundedCorners = new BooleanSetting("Rounded Corners", true)
        .description("Rounds panel and card corners when the theme allows it.");
    private final BooleanSetting gradientHeader = new BooleanSetting("Gradient Header", true)
        .description("Adds a soft colour gradient to menu headers.");
    private final BooleanSetting outlines = new BooleanSetting("Outlines", false)
        .description("Draws thin borders around panels and cards.");
    private final BooleanSetting panelShadow = new BooleanSetting("Panel Shadow", false)
        .description("Adds a drop shadow behind menu panels.");
    private final BooleanSetting performanceMode = new BooleanSetting("Performance Mode", true)
        .description("Uses flat fills and instant hover states in the ClickGUI to minimise frame time.");
    private final BooleanSetting cloudyColors = new BooleanSetting("Cloudy Colors", false)
        .description("Softens the active palette with hazy, cloud-like tones. Works with every theme.");
    private final BooleanSetting showThemeName = new BooleanSetting("Show Theme In Title", true)
        .description("Shows the active palette name in the menu title bar.");

    public GuiThemeModule() {
        super("Menu Theme", "Change the ClickGUI palette, layout, scale and typography.", Category.UTIL);
        INSTANCE = this;
        addSetting(theme);
        addSetting(menuLayout);
        addSetting(guiScale);
        addSetting(fontSize);
        addSetting(sidebarWidth);
        addSetting(categorySpacing);
        addSetting(panelOpacity);
        addSetting(overlayOpacity);
        addSetting(roundedCorners);
        addSetting(gradientHeader);
        addSetting(outlines);
        addSetting(panelShadow);
        addSetting(performanceMode);
        addSetting(cloudyColors);
        addSetting(showThemeName);
    }

    public GuiTheme getTheme() {
        syncPalette();
        return theme.getValue();
    }

    /** Keeps every renderer on the Cloudy choice, including theme swatches. */
    public void syncPalette() {
        GuiTheme.setCloudy(cloudyColors.isEnabled());
    }

    public MenuLayout menuLayout() {
        return menuLayout.getValue();
    }

    /** Top-bar shortcut: advance to the next palette without opening settings. */
    public void cycleTheme() {
        GuiTheme current = theme.getValue();
        for (int i = 0; i < THEME_CYCLE.length; i++) {
            if (current == THEME_CYCLE[i]) {
                theme.setValue(THEME_CYCLE[(i + 1) % THEME_CYCLE.length]);
                persist();
                return;
            }
        }
        theme.cycle();
        persist();
    }

    /** Top-bar shortcut: flip between click and hover category navigation. */
    public void toggleMenuLayout() {
        menuLayout.cycle();
        persist();
    }

    private void persist() {
        ConfigStore.save(UtilModClient.getModules());
    }

    public void setMenuLayout(MenuLayout value) {
        menuLayout.setValue(value);
    }

    /**
     * ClickGUI window size relative to the screen. Rendering stays at a crisp
     * 1:1 physical-pixel scale; this setting only resizes the shell, so glyphs
     * and icons are never resampled.
     */
    public float guiScale() {
        return (float) Math.max(MIN_GUI_SCALE, Math.min(MAX_GUI_SCALE, guiScale.getValue()));
    }

    /** Typography multiplier applied on top of the polished menu font baseline. */
    public float fontScale() {
        return (float) Math.max(0.85, Math.min(1.25, fontSize.getValue()));
    }

    /** ClickGUI category rail width in logical pixels. */
    public double sidebarWidth() {
        return sidebarWidth.getValue();
    }

    /** ClickGUI category row height in logical pixels. */
    public double categorySpacing() {
        return categorySpacing.getValue();
    }

    public int panelColor() {
        // Preserve each palette's own glass alpha. The old code overwrote it with
        // 255, which made the translucent ScalableLux palettes look fully opaque.
        int baseAlpha = (getTheme().panel >>> 24) & 0xFF;
        int alpha = (int) (baseAlpha * panelOpacity.getValue() / 100.0);
        return GuiTheme.withAlpha(getTheme().panel, Math.max(0, Math.min(255, alpha)));
    }

    /** Overlay opacity setting; the source backdrop alphas are applied by the renderer. */
    public int overlayColor() {
        int alpha = (int) (255 * overlayOpacity.getValue() / 100.0);
        return GuiTheme.withAlpha(getTheme().overlay, Math.max(0, Math.min(255, alpha)));
    }

    public boolean rounded() {
        return roundedCorners.isEnabled() && getTheme().rounded;
    }

    /** The Flux shell keeps rounded panels for every palette, like the mockup. */
    public boolean shellRounded() {
        return roundedCorners.isEnabled();
    }

    public boolean gradientHeader() {
        return gradientHeader.isEnabled();
    }

    public boolean outlines() {
        return outlines.isEnabled();
    }

    public boolean shadow() {
        return panelShadow.isEnabled();
    }

    /** Trades gradient/animation flourishes for a cheaper menu redraw. */
    public boolean performanceMode() {
        return performanceMode.isEnabled();
    }

    public boolean showThemeName() {
        return showThemeName.isEnabled();
    }

    /**
     * Restores the shipped Ochard-inspired palette/layout/scale. Called once by the
     * config migration so older saved configs cannot leave the menu on a different
     * palette/layout/scale after an update.
     */
    public void applyMockupDefaults() {
        theme.setValue(GuiTheme.PINK);
        menuLayout.setValue(MenuLayout.DROPDOWN);
        guiScale.setValue(1.0);
        fontSize.setValue(1.0);
        sidebarWidth.setValue(196.0);
        categorySpacing.setValue(52.0);
        panelOpacity.setValue(100.0);
        overlayOpacity.setValue(100.0);
        roundedCorners.setEnabled(true);
        gradientHeader.setEnabled(true);
        outlines.setEnabled(false);
        panelShadow.setEnabled(false);
        performanceMode.setEnabled(true);
        cloudyColors.setEnabled(false);
        showThemeName.setEnabled(true);
    }
}
