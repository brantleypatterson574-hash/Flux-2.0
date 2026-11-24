package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.UtilModClient;
import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.KeybindSetting;
import com.yourname.utilmod.setting.NumberSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Records your movement keys for a short window and replays the last window
 * when you press the bound key. It only feeds the same local key states back
 * into vanilla movement; the server still owns your position.
 */
public class SnapTapModule extends Module {
    private record Frame(boolean forward, boolean back, boolean left, boolean right,
                         boolean jump, boolean sneak, boolean sprint) {}

    private final KeybindSetting replayKey = new KeybindSetting("Replay Key", GLFW.GLFW_KEY_V)
        .description("Press this key to repeat the movement you just made.");
    private final NumberSetting replayTicks = new NumberSetting("Replay Length (Ticks)", 10.0, 1.0, 120.0)
        .description("How many ticks of the recent movement are replayed.");
    private final NumberSetting memoryTicks = new NumberSetting("Memory Length (Ticks)", 60.0, 5.0, 240.0)
        .description("How much recent movement the client remembers before old ticks are forgotten.");
    private final BooleanSetting includeJump = new BooleanSetting("Record Jump", true)
        .description("Replays the jump key along with the movement keys.");
    private final BooleanSetting includeSneak = new BooleanSetting("Record Sneak", true)
        .description("Replays the sneak key along with the movement keys.");
    private final BooleanSetting includeSprint = new BooleanSetting("Record Sprint", true)
        .description("Replays the sprint key along with the movement keys.");
    private final BooleanSetting restoreLiveKeys = new BooleanSetting("Restore Live Keys", true)
        .description("Returns your own held movement keys to normal when the replay finishes.");

    private final Deque<Frame> history = new ArrayDeque<>();
    private List<Frame> playback;
    private int playbackIndex;
    private Frame liveSnapshot;

    public SnapTapModule() {
        super("Snap Tap",
            "Press a key to replay your last movement exactly. Local input only; disable Auto Walk so the two modules do not fight over the keys.",
            Category.MOVEMENT);
        addSetting(replayKey);
        addSetting(replayTicks);
        addSetting(memoryTicks);
        addSetting(includeJump);
        addSetting(includeSneak);
        addSetting(includeSprint);
        addSetting(restoreLiveKeys);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    @Override
    public void onDisable() {
        finish(mc);
        history.clear();
    }

    private void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.currentScreen != null
            || UtilModClient.isModuleEnabled(AutoWalkModule.class)) {
            if (playback != null) finish(client);
            history.clear();
            return;
        }

        if (playback != null) {
            if (playbackIndex >= playback.size()) {
                finish(client);
                return;
            }
            apply(playback.get(playbackIndex++), client);
            return;
        }

        if (replayKey.wasPressed(client)) {
            List<Frame> recent = recentFrames();
            if (hasMovement(recent)) {
                liveSnapshot = liveFrame(client);
                playback = new ArrayList<>(recent);
                playbackIndex = 0;
                apply(playback.get(playbackIndex++), client);
                return;
            }
        }

        recordFrame(client);
    }

    private List<Frame> recentFrames() {
        int wanted = Math.max(1, (int) replayTicks.getValue());
        List<Frame> all = new ArrayList<>(history);
        int from = Math.max(0, all.size() - wanted);
        return new ArrayList<>(all.subList(from, all.size()));
    }

    private void recordFrame(MinecraftClient client) {
        history.addLast(liveFrame(client));
        int limit = Math.max(1, (int) memoryTicks.getValue());
        while (history.size() > limit) history.removeFirst();
    }

    private Frame liveFrame(MinecraftClient client) {
        return new Frame(
            client.options.forwardKey.isPressed(),
            client.options.backKey.isPressed(),
            client.options.leftKey.isPressed(),
            client.options.rightKey.isPressed(),
            includeJump.isEnabled() && client.options.jumpKey.isPressed(),
            includeSneak.isEnabled() && client.options.sneakKey.isPressed(),
            includeSprint.isEnabled() && client.options.sprintKey.isPressed()
        );
    }

    private void apply(Frame frame, MinecraftClient client) {
        client.options.forwardKey.setPressed(frame.forward());
        client.options.backKey.setPressed(frame.back());
        client.options.leftKey.setPressed(frame.left());
        client.options.rightKey.setPressed(frame.right());
        client.options.jumpKey.setPressed(frame.jump());
        client.options.sneakKey.setPressed(frame.sneak());
        client.options.sprintKey.setPressed(frame.sprint());
    }

    private void finish(MinecraftClient client) {
        if (client != null) {
            if (restoreLiveKeys.isEnabled() && liveSnapshot != null) apply(liveSnapshot, client);
            else {
                client.options.forwardKey.setPressed(false);
                client.options.backKey.setPressed(false);
                client.options.leftKey.setPressed(false);
                client.options.rightKey.setPressed(false);
                client.options.jumpKey.setPressed(false);
                client.options.sneakKey.setPressed(false);
                client.options.sprintKey.setPressed(false);
            }
        }
        playback = null;
        playbackIndex = 0;
        liveSnapshot = null;
    }

    private static boolean hasMovement(List<Frame> frames) {
        for (Frame frame : frames) {
            if (frame.forward() || frame.back() || frame.left() || frame.right()) return true;
        }
        return false;
    }
}
