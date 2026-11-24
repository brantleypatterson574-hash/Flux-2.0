package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.ui.GuiTheme;
import com.yourname.utilmod.util.OverlayRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Screen-space confetti burst when a nearby entity dies. Observation only. */
public class ConfettiDeathsModule extends Module implements HudOverlay {
    public static ConfettiDeathsModule INSTANCE;

    private static final class Piece {
        final double startX, startY, vx, vy;
        final long bornAt, duration;
        final int size, color;

        Piece(double startX, double startY, double vx, double vy,
              long bornAt, long duration, int size, int color) {
            this.startX = startX;
            this.startY = startY;
            this.vx = vx;
            this.vy = vy;
            this.bornAt = bornAt;
            this.duration = duration;
            this.size = size;
            this.color = color;
        }
    }

    private static final int[] COLORS = {
        0xFFFF5C8A, 0xFFFFD166, 0xFF67E8F9, 0xFFA3E635, 0xFFC084FC, 0xFFFFFFFF
    };

    private final NumberSetting pieces = new NumberSetting("Pieces", 54.0, 8.0, 160.0)
        .description("Number of confetti bits in each burst.");
    private final NumberSetting duration = new NumberSetting("Duration", 1500.0, 400.0, 4000.0)
        .unit("ms")
        .description("How long each confetti burst stays on screen.");
    private final NumberSetting range = new NumberSetting("Range", 64.0, 8.0, 160.0)
        .unit("blocks")
        .description("Only celebrate deaths within this distance of you.");
    private final BooleanSetting includeSelf = new BooleanSetting("Include Yourself", false)
        .description("Also throw confetti when your own player dies.");
    private final BooleanSetting playersOnly = new BooleanSetting("Players Only", true)
        .description("Ignore mob deaths. Turn off to celebrate every nearby living death.");
    private final BooleanSetting useTheme = new BooleanSetting("Use Theme Colors", true)
        .description("Blend confetti into the active menu palette instead of the fixed colours.");

    private final List<Piece> active = new ArrayList<>();
    private final Random random = new Random();

    public ConfettiDeathsModule() {
        super("Confetti Deaths", "Throws a small screen-space confetti burst when a nearby player dies. Purely cosmetic.",
            Category.RENDER);
        INSTANCE = this;
        addSetting(pieces);
        addSetting(duration);
        addSetting(range);
        addSetting(includeSelf);
        addSetting(playersOnly);
        addSetting(useTheme);
    }

    /** Called from the existing status-packet observer. The packet is unchanged. */
    public static void onEntityStatus(EntityStatusS2CPacket packet) {
        ConfettiDeathsModule module = INSTANCE;
        if (module == null || !module.isEnabled() || packet == null) return;
        if (packet.getStatus() != EntityStatuses.ADD_DEATH_PARTICLES) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;
        Entity entity = packet.getEntity(client.world);
        if (entity == null || entity == client.player && !module.includeSelf.isEnabled()) return;
        if (module.playersOnly.isEnabled() && !(entity instanceof PlayerEntity)) return;
        if (entity.distanceTo(client.player) > Math.max(1.0, module.range.getValue())) return;

        int[] center = OverlayRender.project(entity.getX(),
            entity.getY() + entity.getHeight() * 0.65, entity.getZ());
        if (center == null || center[2] > Math.max(8, module.range.getValue() * 2)) return;
        module.burst(center[0], center[1]);
    }

    private void burst(int x, int y) {
        long now = System.nanoTime();
        long life = Math.max(200L, (long) duration.getValue()) * 1_000_000L;
        int count = Math.max(4, Math.min(160, (int) pieces.getValue()));
        int palette = useTheme.isEnabled() ? themeColors() : COLORS.length;
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double speed = 30.0 + random.nextDouble() * 150.0;
            int color = palette == COLORS.length
                ? COLORS[random.nextInt(COLORS.length)] : themeColor(random.nextInt(palette));
            active.add(new Piece(
                x, y,
                Math.cos(angle) * speed,
                Math.sin(angle) * speed - 55.0,
                now,
                (long) (life * (0.65 + random.nextDouble() * 0.5)),
                2 + random.nextInt(3),
                color
            ));
        }
        while (active.size() > 400) active.remove(0);
    }

    private int themeColors() {
        GuiThemeModule theme = GuiThemeModule.INSTANCE;
        return theme == null ? COLORS.length : 3;
    }

    private int themeColor(int index) {
        GuiThemeModule module = GuiThemeModule.INSTANCE;
        GuiTheme theme = module == null ? GuiTheme.MATRIX : module.getTheme();
        int rgb = switch (Math.max(0, index) % 3) {
            case 0 -> theme.accent;
            case 1 -> theme.titleAccent;
            default -> theme.text;
        };
        return 0xFF000000 | (rgb & 0xFFFFFF);
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || active.isEmpty() || mc.options.hudHidden) return;
        long now = System.nanoTime();
        Iterator<Piece> iterator = active.iterator();
        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            double age = (now - piece.bornAt) / 1_000_000_000.0;
            double life = piece.duration / 1_000_000_000.0;
            if (age < 0.0 || age > life) {
                iterator.remove();
                continue;
            }
            double seconds = age;
            int x = (int) (piece.startX + piece.vx * seconds);
            int y = (int) (piece.startY + piece.vy * seconds + 260.0 * seconds * seconds);
            int alpha = (int) (255 * (1.0 - age / life));
            int color = (Math.max(0, Math.min(255, alpha)) << 24) | (piece.color & 0xFFFFFF);
            context.fill(x, y, x + piece.size, y + piece.size + 1, color);
        }
    }

    @Override
    public void onDisable() {
        active.clear();
    }
}
