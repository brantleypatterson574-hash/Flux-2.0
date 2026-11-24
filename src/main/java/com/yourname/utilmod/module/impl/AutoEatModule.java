package com.yourname.utilmod.module.impl;
import com.yourname.utilmod.util.ItemUtil;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoEatModule extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("Health Threshold", 10.0, 1.0, 20.0).description("Eats when your health drops to this many half-hearts or lower.");
    private final NumberSetting minHungerLevel = new NumberSetting("Minimum Hunger", 20, 0, 20).description("Eats when hunger falls below this level, if the hunger trigger is enabled.");
    private final BooleanSetting requireHunger = new BooleanSetting("Require Hunger", true)
        .description("Allow low hunger to trigger eating. Disable to only eat at low health.");
    private final NumberSetting preferSlot = new NumberSetting("Preferred Slot", -1, -1, 8).description("Hotbar slot with food to use first; -1 chooses automatically.");
    private final NumberSetting eatDelay = new NumberSetting("Eat Delay", 10, 0, 100).description("Ticks to wait before starting another eating attempt.");
    private final BooleanSetting allowWhileMoving = new BooleanSetting("Allow While Moving", false).description("Permits eating while you are walking or sprinting.");
    private final BooleanSetting swapBack = new BooleanSetting("Swap Back", true).description("Returns to your original hotbar slot after eating.");
    private final BooleanSetting preferBestFood = new BooleanSetting("Prefer Best Food", true)
        .description("Chooses the food that restores the most hunger and saturation instead of the first one found.");

    private int originalSlot = -1;
    private int delayTicks;
    private int lastFoodSlot = -1;

    public AutoEatModule() {
        super("Auto Eat", "Automatically eats food when your health gets low.", Category.PLAYER);
        addSetting(healthThreshold);
        addSetting(minHungerLevel);
        addSetting(requireHunger);
        addSetting(preferSlot);
        addSetting(eatDelay);
        addSetting(allowWhileMoving);
        addSetting(swapBack);
        addSetting(preferBestFood);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onEnable() {
        delayTicks = 0;
        lastFoodSlot = -1;
    }

    @Override
    public void onDisable() {
        restoreSlot();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        boolean hungry = client.player.getHungerManager().isNotFull()
            && client.player.getHungerManager().getFoodLevel() < minHungerLevel.getValue();
        boolean lowHealth = client.player.getHealth() <= healthThreshold.getValue();
        // Health and hunger are independent triggers. The old AND logic meant a
        // starving player at full health never ate, despite the module docs.
        boolean shouldEat = lowHealth || (requireHunger.isEnabled() && hungry);

        if (!shouldEat || (!allowWhileMoving.isEnabled() && isMoving(client))) {
            restoreSlot();
            delayTicks = 0;
            return;
        }

        int foodSlot = findFoodSlot(client);
        if (foodSlot == -1) {
            restoreSlot();
            return;
        }

        if (originalSlot == -1 && swapBack.isEnabled()) {
            originalSlot = client.player.getInventory().getSelectedSlot();
        }
        // Only touch the selected slot when it actually changes; reselecting the
        // same slot every tick can interrupt the eating animation.
        if (lastFoodSlot != foodSlot) {
            ItemUtil.selectSlot(client.player, foodSlot);
            lastFoodSlot = foodSlot;
        }

        if (delayTicks > 0) {
            delayTicks--;
            return; // keep the food held so the eating animation is not interrupted
        }

        if (client.interactionManager != null) {
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            client.player.swingHand(Hand.MAIN_HAND);
        }
        delayTicks = (int) eatDelay.getValue();
    }

    private int findFoodSlot(MinecraftClient client) {
        int preferred = (int) preferSlot.getValue();
        if (preferred >= 0 && preferred < 9) {
            ItemStack stack = client.player.getInventory().getStack(preferred);
            if (isFood(stack)) return preferred;
        }

        if (!preferBestFood.isEnabled()) {
            for (int i = 0; i < 9; i++) {
                if (isFood(client.player.getInventory().getStack(i))) return i;
            }
            return -1;
        }

        // Score every edible hotbar slot and keep the best one. Golden apples get
        // a healing bonus on top of their nutrition so they win over plain food.
        int bestSlot = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            double score = foodScore(stack);
            if (score > bestScore) {
                bestScore = score;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    private static boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && stack.get(DataComponentTypes.FOOD) != null;
    }

    /** Higher is better. Returns -1 for anything that is not edible. */
    private static double foodScore(ItemStack stack) {
        FoodComponent food = stack.get(DataComponentTypes.FOOD);
        if (food == null || stack.isEmpty()) return -1.0;
        double score = food.nutrition() + food.saturation() * 2.0;
        if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            score += 20.0;
        } else if (stack.isOf(Items.GOLDEN_APPLE)) {
            score += 10.0;
        }
        // A slight nudge toward fuller stacks avoids burning through several small ones.
        score += Math.min(stack.getCount(), 16) * 0.001;
        return score;
    }

    private boolean isMoving(MinecraftClient client) {
        return client.options.forwardKey.isPressed()
            || client.options.backKey.isPressed()
            || client.options.leftKey.isPressed()
            || client.options.rightKey.isPressed();
    }

    private void restoreSlot() {
        lastFoodSlot = -1;
        if (!swapBack.isEnabled() || mc.player == null || originalSlot == -1) return;
        ItemUtil.selectSlot(mc.player, originalSlot);
        originalSlot = -1;
    }
}
