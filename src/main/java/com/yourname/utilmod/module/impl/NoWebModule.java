package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * Client-side override for block-based movement slowdowns.
 *
 * <p>The server still owns your authoritative position. On a private server
 * that permits this jar the override is smooth; on other servers the server
 * may correct movement that ignores its own web slowdown.
 */
public class NoWebModule extends Module {
    public static NoWebModule INSTANCE;

    private final BooleanSetting webs = new BooleanSetting("Cobwebs", true)
        .description("Ignore the movement slow from cobwebs.");
    private final BooleanSetting powderSnow = new BooleanSetting("Powder Snow", true)
        .description("Ignore the movement slow from powder snow.");
    private final BooleanSetting honey = new BooleanSetting("Honey Blocks", true)
        .description("Ignore the movement slow from honey blocks.");
    private final BooleanSetting soulSand = new BooleanSetting("Soul Sand", true)
        .description("Ignore the movement slow from soul sand.");
    private final BooleanSetting otherBlocks = new BooleanSetting("Other Blocks", true)
        .description("Ignore slowdown from any other block the game applies.");

    public NoWebModule() {
        super("No Web", "Cancels the client-side slow from cobwebs and similar blocks. The server still decides your final speed; cobwebs are now a suggestion.", Category.MOVEMENT);
        INSTANCE = this;
        addSetting(webs);
        addSetting(powderSnow);
        addSetting(honey);
        addSetting(soulSand);
        addSetting(otherBlocks);
    }

    /** Called from the player mixin before vanilla applies a block slow. */
    public static boolean shouldIgnore(BlockState state) {
        NoWebModule module = INSTANCE;
        if (module == null || !module.isEnabled() || state == null) return false;
        if (state.isOf(Blocks.COBWEB)) return module.webs.isEnabled();
        if (state.isOf(Blocks.POWDER_SNOW)) return module.powderSnow.isEnabled();
        if (state.isOf(Blocks.HONEY_BLOCK)) return module.honey.isEnabled();
        if (state.isOf(Blocks.SOUL_SAND)) return module.soulSand.isEnabled();
        return module.otherBlocks.isEnabled();
    }

    @Override
    public void onDisable() {
        // Nothing to clear: the mixin reads the module state directly.
    }
}
