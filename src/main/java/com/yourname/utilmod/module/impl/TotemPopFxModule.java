package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.HudOverlay;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.EnumSetting;
import com.yourname.utilmod.setting.NumberSetting;
import com.yourname.utilmod.util.OverlayRender;
import com.yourname.utilmod.util.SlEspStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.math.Box;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

/** Cosmetic totem-pop rings and ghost outlines. Observes vanilla status packets only. */
public class TotemPopFxModule extends Module implements HudOverlay {
    private enum FxStyle { RING, GHOST, BOTH }

    private static final int SEGMENTS = 32;
    private static final double[] COS = new double[SEGMENTS + 1];
    private static final double[] SIN = new double[SEGMENTS + 1];
    static {
        for (int i = 0; i <= SEGMENTS; i++) {
            double angle = Math.PI * 2.0 * i / SEGMENTS;
            COS[i] = Math.cos(angle);
            SIN[i] = Math.sin(angle);
        }
    }

    private static final class Pop {
        final Entity entity;
        double x, y, z, height;
        long expiresAt;
        long duration;
        int count;

        Pop(Entity entity, long expiresAt, long duration) {
            this.entity = entity;
            this.expiresAt = expiresAt;
            this.duration = duration;
            this.count = 1;
        }
    }

    public static TotemPopFxModule INSTANCE;

    private final EnumSetting<FxStyle> style =
        new EnumSetting<>("Effect Style", FxStyle.class, FxStyle.BOTH).description("Which cosmetic effect appears on a totem pop: ring, ghost, or both.");
    private final NumberSetting duration = new NumberSetting("Duration", 900.0, 200.0, 3000.0)
        .unit("ms")
        .description("How long each pop effect stays on screen.");
    private final NumberSetting ringRadius = new NumberSetting("Ring Radius", 28.0, 8.0, 90.0).description("Size of the ring drawn at the totem-pop location.");
    private final NumberSetting lineWidth = new NumberSetting("Line Width", 2.0, 1.0, 6.0).description("Thickness of the effect outlines.");
    private final NumberSetting color = new NumberSetting("Effect Color", 0xF472B6, 0, 0xFFFFFF).description("Colour of the pop ring and ghost outline.");
    private final BooleanSetting includeSelf = new BooleanSetting("Include Yourself", true)
        .description("Also shows the effect when your own totem pops.");
    private final BooleanSetting showCount = new BooleanSetting("Show Pop Count", true).description("Shows how many times the player has popped a totem.");

    private final Map<Entity, Pop> pops = new IdentityHashMap<>();

    public TotemPopFxModule() {
        super("Totem Pop FX", "Purely cosmetic rings and ghost outlines when a totem saves a player.",
            Category.RENDER);
        INSTANCE = this;
        addSetting(style);
        addSetting(duration);
        addSetting(ringRadius);
        addSetting(lineWidth);
        addSetting(color);
        addSetting(includeSelf);
        addSetting(showCount);
    }

    /** Called by the existing observation mixin. The packet is never changed or cancelled. */
    public static void onEntityStatus(EntityStatusS2CPacket packet) {
        TotemPopFxModule module = INSTANCE;
        if (module == null || !module.isEnabled()) return;
        if (packet.getStatus() != EntityStatuses.USE_TOTEM_OF_UNDYING) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        Entity entity = packet.getEntity(client.world);
        if (entity == null) return;
        if (!module.includeSelf.isEnabled() && entity == client.player) return;

        long now = System.currentTimeMillis();
        long life = Math.max(200L, (long) module.duration.getValue());
        Pop pop = module.pops.get(entity);
        if (pop == null || now >= pop.expiresAt) {
            pop = new Pop(entity, now + life, life);
            module.pops.put(entity, pop);
        } else {
            pop.count++;
            pop.expiresAt = now + life;
            pop.duration = life;
        }
        pop.x = entity.getX();
        pop.y = entity.getY();
        pop.z = entity.getZ();
        pop.height = Math.max(0.6f, entity.getHeight());
    }

    @Override
    public void onDisable() {
        pops.clear();
    }

    @Override
    public void renderOverlay(DrawContext context) {
        if (!isEnabled() || pops.isEmpty() || mc.world == null || mc.player == null || mc.options.hudHidden) return;

        long now = System.currentTimeMillis();
        int rgb = color.getColorValue();
        int width = Math.max(1, (int) lineWidth.getValue());
        FxStyle fxStyle = style.getValue();

        Iterator<Map.Entry<Entity, Pop>> iterator = pops.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Entity, Pop> entry = iterator.next();
            Pop pop = entry.getValue();
            if (now >= pop.expiresAt) {
                iterator.remove();
                continue;
            }

            float life = 1.0f - (float) (now - (pop.expiresAt - pop.duration)) / pop.duration;
            life = Math.max(0.0f, Math.min(1.0f, life));
            int alpha = (int) (210 * life);
            int faded = SlEspStyle.blend(0x000000, rgb, life);

            if (fxStyle == FxStyle.GHOST || fxStyle == FxStyle.BOTH) {
                Box ghost = new Box(
                    pop.x - 0.38, pop.y, pop.z - 0.38,
                    pop.x + 0.38, pop.y + pop.height, pop.z + 0.38
                );
                OverlayRender.box3DFilled(context, ghost, SlEspStyle.argb(alpha, faded),
                    SlEspStyle.argb((int) (28 * life), rgb), width, true);
            }

            if (fxStyle == FxStyle.RING || fxStyle == FxStyle.BOTH) {
                int[] center = OverlayRender.project(pop.x, pop.y + pop.height * 0.6, pop.z);
                if (center == null) continue;

                double radius = ringRadius.getValue() * (0.8 + (1.0 - life) * 0.8);
                int previousX = center[0] + (int) (COS[0] * radius);
                int previousY = center[1] + (int) (SIN[0] * radius);
                for (int i = 1; i <= SEGMENTS; i++) {
                    int x = center[0] + (int) (COS[i] * radius);
                    int y = center[1] + (int) (SIN[i] * radius);
                    OverlayRender.line(context, previousX, previousY, x, y,
                        SlEspStyle.argb(alpha, rgb), width);
                    previousX = x;
                    previousY = y;
                }

                if (showCount.isEnabled() && pop.count > 1) {
                    OverlayRender.text(context, "POP x" + pop.count,
                        center[0] + (int) radius + 4, center[1] - (int) radius - 6,
                        SlEspStyle.argb(235, rgb));
                }
            }
        }
    }
}
