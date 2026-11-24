package com.yourname.utilmod.util;

import com.yourname.utilmod.mixin.GameRendererFovAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
 * Small 2D overlay helpers. World points are projected with the game camera and
 * drawn on a HUD element, which avoids the removed/reworked 1.21.9+ world
 * rendering events while still giving real screen-space entity boxes.
 *
 * <p>The camera basis is cached for the duration of a frame and the projection
 * helpers use primitive coordinates instead of allocating vectors for every
 * corner. Straight lines are drawn as one quad instead of one fill per pixel.</p>
 */
public final class OverlayRender {
    private OverlayRender() {}

    private static Camera cachedCamera;
    private static double camX, camY, camZ;
    private static double fwdX, fwdY, fwdZ;
    private static double rightX, rightY, rightZ;
    private static double upX, upY, upZ;
    private static double projectionScale, halfWidth, halfHeight;

    // One camera snapshot per frame. A single ESP box can call project()/line()
    // dozens of times; without this gate each call re-queried FOV and window size.
    private static boolean cameraCheckedThisFrame;
    private static boolean cameraUsableThisFrame;

    /** Called once at the start of each HUD frame, before any overlay draws. */
    public static void beginFrame() {
        cameraCheckedThisFrame = false;
    }

    private static boolean refreshCamera() {
        if (cameraCheckedThisFrame) return cameraUsableThisFrame;
        cameraCheckedThisFrame = true;
        cameraUsableThisFrame = computeCamera();
        return cameraUsableThisFrame;
    }

    private static boolean computeCamera() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null || mc.gameRenderer == null) {
            cachedCamera = null;
            return false;
        }

        Camera camera = mc.gameRenderer.getCamera();
        if (!camera.isReady()) {
            cachedCamera = null;
            return false;
        }

        double yaw = camera.getYaw();
        double pitch = camera.getPitch();
        // Use the effective FOV (speed potions, sprint, FOV setting) instead of the
        // raw option, otherwise boxes drift away from entities while it changes.
        double fov = ((GameRendererFovAccessor) mc.gameRenderer).utilmod$getFov(camera, 0.0f, true);
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        // Camera position is only fetched when something else changed. Every hitbox
        // projects eight corners, so avoiding eight Vec3d allocations per box matters.
        Vec3d pos = camera.getCameraPos();
        if (camera == cachedCamera
            && pos.x == camX && pos.y == camY && pos.z == camZ
            && yaw == cachedYaw && pitch == cachedPitch && fov == cachedFov
            && width == cachedWidth && height == cachedHeight) {
            return true;
        }

        cachedCamera = camera;
        cachedYaw = yaw;
        cachedPitch = pitch;
        cachedFov = fov;
        cachedWidth = width;
        cachedHeight = height;

        camX = pos.x;
        camY = pos.y;
        camZ = pos.z;

        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double cp = Math.cos(pitchRad);
        fwdX = -Math.sin(yawRad) * cp;
        fwdY = -Math.sin(pitchRad);
        fwdZ = Math.cos(yawRad) * cp;

        rightX = -Math.cos(yawRad);
        rightY = 0.0;
        rightZ = -Math.sin(yawRad);

        upX = rightY * fwdZ - rightZ * fwdY;
        upY = rightZ * fwdX - rightX * fwdZ;
        upZ = rightX * fwdY - rightY * fwdX;

        projectionScale = (height * 0.5)
            / Math.tan(Math.toRadians(clamp(fov, 1.0, 170.0)) * 0.5);
        halfWidth = width * 0.5;
        halfHeight = height * 0.5;
        return true;
    }

    private static double cachedYaw, cachedPitch, cachedFov;
    private static int cachedWidth, cachedHeight;

    /** Returns {screenX, screenY, depth} or null when behind the camera. */
    public static int[] project(Vec3d worldPos) {
        return worldPos == null ? null : project(worldPos.x, worldPos.y, worldPos.z);
    }

    /** Allocation-free projection (apart from the small result array). */
    public static int[] project(double x, double y, double z) {
        int[] out = new int[3];
        return projectInto(x, y, z, out) ? out : null;
    }

    /** Writes {screenX, screenY, depth} into a reusable array. No allocation. */
    public static boolean projectInto(double x, double y, double z, int[] out) {
        if (!refreshCamera()) return false;

        double dx = x - camX;
        double dy = y - camY;
        double dz = z - camZ;
        double depth = dx * fwdX + dy * fwdY + dz * fwdZ;
        if (depth <= 0.08) return false;

        double horizontal = dx * rightX + dy * rightY + dz * rightZ;
        double vertical = dx * upX + dy * upY + dz * upZ;
        double screenX = halfWidth + horizontal * projectionScale / depth;
        double screenY = halfHeight - vertical * projectionScale / depth;
        if (!Double.isFinite(screenX) || !Double.isFinite(screenY)) return false;

        // Keep coordinates finite and bounded. line() clips to the viewport anyway,
        // but this prevents integer overflow when a corner is extremely close to the near plane.
        int sx = (int) clamp(screenX, -1_000_000.0, 1_000_000.0);
        int sy = (int) clamp(screenY, -1_000_000.0, 1_000_000.0);
        out[0] = sx;
        out[1] = sy;
        out[2] = (int) Math.min(depth, Integer.MAX_VALUE);
        return true;
    }

    /** Projects an entity bounding box into a screen-space rectangle. */
    public static int[] entityBounds(Entity entity, float expand) {
        Box box = entity.getBoundingBox();
        int[] out = new int[4];
        return boxScreenBounds(box.minX - expand, box.minY - expand, box.minZ - expand,
            box.maxX + expand, box.maxY + expand, box.maxZ + expand, out) ? out : null;
    }

    /**
     * Draws a line as ONE rotated quad. The old implementation stamped a fill per
     * pixel (up to 4096 per line), which was by far the biggest ESP lag source.
     * DrawContext.fill snapshots the current matrix, so a translate + rotate lets
     * the GPU draw the whole line in a single batched quad.
     */
    public static void line(DrawContext context, int x1, int y1, int x2, int y2, int color, int thickness) {
        refreshCamera();
        lineNoRefresh(context, x1, y1, x2, y2, color, thickness);
    }

    /** Same as line(), but assumes refreshCamera() already ran this frame. */
    private static void lineNoRefresh(DrawContext context, int x1, int y1, int x2, int y2,
                                      int color, int thickness) {
        int t = Math.max(1, thickness);
        int margin = t + 3;
        double viewW = cachedWidth > 0 ? cachedWidth + margin : margin;
        double viewH = cachedHeight > 0 ? cachedHeight + margin : margin;
        if (!clipLine(x1, y1, x2, y2, -margin, viewW, -margin, viewH)) return;

        int ax = (int) clipX1;
        int ay = (int) clipY1;
        int bx = (int) clipX2;
        int by = (int) clipY2;
        double dx = bx - ax;
        double dy = by - ay;
        double length = Math.sqrt(dx * dx + dy * dy);
        int low = (t - 1) / 2;
        int high = t - low;

        if (length < 0.75) {
            context.fill(ax - low, ay - low, ax + high, ay + high, color);
            return;
        }

        // Boxes produce horizontal/vertical lines almost exclusively. Writing the
        // rotated quad directly avoids a matrix copy and atan2 for those cases.
        int extent = (int) Math.ceil(length) + 1;
        if (dx == 0.0) {
            int sy = dy > 0.0 ? ay : ay - extent;
            int sx = dy > 0.0 ? ax - high : ax - low;
            context.fill(sx, sy, sx + t, sy + extent, color);
            return;
        }
        if (dy == 0.0) {
            int sx = dx > 0.0 ? ax : ax - extent;
            int sy = dx > 0.0 ? ay - low : ay - high;
            context.fill(sx, sy, sx + extent, sy + t, color);
            return;
        }

        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(ax, ay);
        matrices.rotate((float) Math.atan2(dy, dx));
        context.fill(0, -low, extent, high, color);
        matrices.popMatrix();
    }

    /** Soft two-pass halo + bright core. Three quads total instead of thousands of fills. */
    public static void glowLine(DrawContext context, int x1, int y1, int x2, int y2, int rgb, int thickness) {
        int core = Math.max(1, thickness);
        // Refresh once for the whole group; the three passes share one camera snapshot.
        refreshCamera();
        lineNoRefresh(context, x1, y1, x2, y2, (26 << 24) | (rgb & 0xFFFFFF), core + 5);
        lineNoRefresh(context, x1, y1, x2, y2, (80 << 24) | (rgb & 0xFFFFFF), core + 2);
        lineNoRefresh(context, x1, y1, x2, y2, 0xFF000000 | (rgb & 0xFFFFFF), core);
    }

    // Liang-Barsky clip results are render-thread-confined scratch, not arrays.
    private static double clipX1, clipY1, clipX2, clipY2;

    /** Liang-Barsky viewport clip. Writes scratch coords; false when fully outside. */
    private static boolean clipLine(double x1, double y1, double x2, double y2,
                                    double minX, double maxX, double minY, double maxY) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double u0 = 0.0;
        double u1 = 1.0;

        // Same four (p, q) pairs and update order as the old array-based version,
        // computed inline so no arrays are allocated per line.
        for (int i = 0; i < 4; i++) {
            double p = switch (i) {
                case 0 -> -dx;
                case 1 -> dx;
                case 2 -> -dy;
                default -> dy;
            };
            double q = switch (i) {
                case 0 -> x1 - minX;
                case 1 -> maxX - x1;
                case 2 -> y1 - minY;
                default -> maxY - y1;
            };
            if (p == 0.0) {
                if (q < 0.0) return false;
                continue;
            }
            double r = q / p;
            if (p < 0.0) {
                if (r > u1) return false;
                if (r > u0) u0 = r;
            } else {
                if (r < u0) return false;
                if (r < u1) u1 = r;
            }
        }

        clipX1 = x1 + u0 * dx;
        clipY1 = y1 + u0 * dy;
        clipX2 = x1 + u1 * dx;
        clipY2 = y1 + u1 * dy;
        return true;
    }

    public static void box(DrawContext context, int x1, int y1, int x2, int y2, int color, int thickness) {
        line(context, x1, y1, x2, y1, color, thickness);
        line(context, x1, y2, x2, y2, color, thickness);
        line(context, x1, y1, x1, y2, color, thickness);
        line(context, x2, y1, x2, y2, color, thickness);
    }

    /** Full rectangle with the same soft glow as prestigeBox. */
    public static void glowBox(DrawContext context, int x1, int y1, int x2, int y2, int rgb, int thickness) {
        glowLine(context, x1, y1, x2, y1, rgb, thickness);
        glowLine(context, x1, y2, x2, y2, rgb, thickness);
        glowLine(context, x1, y1, x1, y2, rgb, thickness);
        glowLine(context, x2, y1, x2, y2, rgb, thickness);
    }

    /** Corner-only outline, the classic client ESP style. */
    public static void cornerBox(DrawContext context, int x1, int y1, int x2, int y2,
                                 int color, int thickness, float cornerFraction) {
        cornerBox(context, x1, y1, x2, y2, color, thickness, cornerFraction, false);
    }

    /** Corner outline with a soft glow pass, the Prestige-style look. */
    public static void glowCornerBox(DrawContext context, int x1, int y1, int x2, int y2,
                                     int rgb, int thickness, float cornerFraction) {
        cornerBox(context, x1, y1, x2, y2, 0xFF000000 | rgb, thickness, cornerFraction, true);
    }

    private static void cornerBox(DrawContext context, int x1, int y1, int x2, int y2,
                                  int color, int thickness, float cornerFraction, boolean glow) {
        int cx = Math.max(2, (int) ((x2 - x1) * cornerFraction));
        int cy = Math.max(2, (int) ((y2 - y1) * cornerFraction));
        drawSegment(context, x1, y1, x1 + cx, y1, color, thickness, glow);
        drawSegment(context, x1, y1, x1, y1 + cy, color, thickness, glow);
        drawSegment(context, x2, y1, x2 - cx, y1, color, thickness, glow);
        drawSegment(context, x2, y1, x2, y1 + cy, color, thickness, glow);
        drawSegment(context, x1, y2, x1 + cx, y2, color, thickness, glow);
        drawSegment(context, x1, y2, x1, y2 - cy, color, thickness, glow);
        drawSegment(context, x2, y2, x2 - cx, y2, color, thickness, glow);
        drawSegment(context, x2, y2, x2, y2 - cy, color, thickness, glow);
    }

    private static void drawSegment(DrawContext context, int x1, int y1, int x2, int y2,
                                    int color, int thickness, boolean glow) {
        if (glow) {
            glowLine(context, x1, y1, x2, y2, color, thickness);
        } else {
            line(context, x1, y1, x2, y2, color, thickness);
        }
    }

    private static final int[] BOX_EDGES = {
        0, 1, 0, 2, 0, 4, 1, 3, 1, 5, 2, 3,
        2, 6, 3, 7, 4, 5, 4, 6, 5, 7, 6, 7
    };
    // Flat scratch arrays. Rendering happens on one thread, so a box can be
    // projected without allocating nine tiny arrays per entity/block.
    private static final int[] PROJECT_SCRATCH = new int[3];
    private static final int[] CORNER_XY = new int[16];
    private static final boolean[] CORNER_OK = new boolean[8];
    private static final int[] BOX_BOUNDS_SCRATCH = new int[4];

    /**
     * Projects the eight corners of a world-space box and draws a 3D wireframe.
     * Corners behind the camera are skipped individually so partial boxes still show.
     */
    public static void box3D(DrawContext context, Box box, int color, int thickness) {
        box3D(context, box, color, thickness, false);
    }

    /** Same 3D wireframe with the soft glow pass. */
    public static void box3D(DrawContext context, Box box, int color, int thickness, boolean glow) {
        box3D(context, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ,
            color, thickness, glow);
    }

    /** Primitive overload: no Box allocation for block overlays. */
    public static void box3D(DrawContext context, double minX, double minY, double minZ,
                             double maxX, double maxY, double maxZ,
                             int color, int thickness, boolean glow) {
        if (!projectBox(minX, minY, minZ, maxX, maxY, maxZ)) return;
        drawBoxEdges(context, color, thickness, glow);
    }

    /**
     *Projects once and uses the same corners for both the translucent fill and
     *the wireframe. Hitbox ESP previously projected every entity twice.
     */
    public static void box3DFilled(DrawContext context, Box box, int outlineColor,
                                   int fillColor, int thickness, boolean glow) {
        box3DFilled(context, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ,
            outlineColor, fillColor, thickness, glow);
    }

    /** Primitive overload for block overlays that already know their corners. */
    public static void box3DFilled(DrawContext context, double minX, double minY, double minZ,
                                   double maxX, double maxY, double maxZ,
                                   int outlineColor, int fillColor, int thickness, boolean glow) {
        if (!projectBox(minX, minY, minZ, maxX, maxY, maxZ)) return;
        if (fillColor != 0) {
            context.fill(BOX_BOUNDS_SCRATCH[0], BOX_BOUNDS_SCRATCH[1],
                BOX_BOUNDS_SCRATCH[2], BOX_BOUNDS_SCRATCH[3], fillColor);
        }
        drawBoxEdges(context, outlineColor, thickness, glow);
    }

    /** Projects all eight corners once; also records their screen-space bounds. */
    private static boolean projectBox(double minX, double minY, double minZ,
                                      double maxX, double maxY, double maxZ) {
        if (!refreshCamera()) return false;
        int index = 0;
        int loX = Integer.MAX_VALUE, loY = Integer.MAX_VALUE;
        int hiX = Integer.MIN_VALUE, hiY = Integer.MIN_VALUE;
        boolean any = false;
        for (int xi = 0; xi < 2; xi++) {
            for (int yi = 0; yi < 2; yi++) {
                for (int zi = 0; zi < 2; zi++) {
                    int i = index++;
                    CORNER_OK[i] = projectInto(
                        xi == 0 ? minX : maxX,
                        yi == 0 ? minY : maxY,
                        zi == 0 ? minZ : maxZ,
                        PROJECT_SCRATCH);
                    if (!CORNER_OK[i]) continue;
                    int sx = PROJECT_SCRATCH[0];
                    int sy = PROJECT_SCRATCH[1];
                    CORNER_XY[i * 2] = sx;
                    CORNER_XY[i * 2 + 1] = sy;
                    any = true;
                    loX = Math.min(loX, sx);
                    loY = Math.min(loY, sy);
                    hiX = Math.max(hiX, sx);
                    hiY = Math.max(hiY, sy);
                }
            }
        }
        if (!any) return false;
        BOX_BOUNDS_SCRATCH[0] = loX;
        BOX_BOUNDS_SCRATCH[1] = loY;
        BOX_BOUNDS_SCRATCH[2] = hiX;
        BOX_BOUNDS_SCRATCH[3] = hiY;
        return true;
    }

    private static void drawBoxEdges(DrawContext context, int color, int thickness, boolean glow) {
        for (int e = 0; e < BOX_EDGES.length; e += 2) {
            int a = BOX_EDGES[e];
            int b = BOX_EDGES[e + 1];
            if (CORNER_OK[a] && CORNER_OK[b]) {
                drawSegment(context, CORNER_XY[a * 2], CORNER_XY[a * 2 + 1],
                    CORNER_XY[b * 2], CORNER_XY[b * 2 + 1], color, thickness, glow);
            }
        }
    }

    /** Screen-space bounds of a projected world box, or null when fully behind the camera. */
    public static int[] boxScreenBounds(Box box) {
        return boxScreenBounds(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    /** Primitive overload for callers that already have block coordinates. */
    public static int[] boxScreenBounds(double minX, double minY, double minZ,
                                        double maxX, double maxY, double maxZ) {
        int[] out = new int[4];
        return boxScreenBounds(minX, minY, minZ, maxX, maxY, maxZ, out) ? out : null;
    }

    /**
     * Writes {minX, minY, maxX, maxY} into a reusable array. No allocation in the
     * caller's per-block loop.
     */
    public static boolean boxScreenBounds(double minX, double minY, double minZ,
                                          double maxX, double maxY, double maxZ, int[] out) {
        if (!refreshCamera()) return false;
        int loX = Integer.MAX_VALUE, loY = Integer.MAX_VALUE;
        int hiX = Integer.MIN_VALUE, hiY = Integer.MIN_VALUE;
        boolean any = false;
        for (int xi = 0; xi < 2; xi++) {
            for (int yi = 0; yi < 2; yi++) {
                for (int zi = 0; zi < 2; zi++) {
                    if (!projectInto(
                        xi == 0 ? minX : maxX,
                        yi == 0 ? minY : maxY,
                        zi == 0 ? minZ : maxZ,
                        PROJECT_SCRATCH)) continue;
                    any = true;
                    loX = Math.min(loX, PROJECT_SCRATCH[0]);
                    loY = Math.min(loY, PROJECT_SCRATCH[1]);
                    hiX = Math.max(hiX, PROJECT_SCRATCH[0]);
                    hiY = Math.max(hiY, PROJECT_SCRATCH[1]);
                }
            }
        }
        if (!any) return false;
        out[0] = loX;
        out[1] = loY;
        out[2] = hiX;
        out[3] = hiY;
        return true;
    }

    /** Distance label used by ESP overlays. Avoids a Formatter allocation per entity. */
    public static String meters(double distance) {
        return Math.round(distance) + "m";
    }

    private static TextRenderer cachedTextRenderer;

    public static void text(DrawContext context, String text, int x, int y, int color) {
        if (cachedTextRenderer == null) {
            cachedTextRenderer = MinecraftClient.getInstance().textRenderer;
        }
        if (cachedTextRenderer != null) {
            context.drawTextWithShadow(cachedTextRenderer, text, x, y, color);
        }
    }

    /**
     * Prestige-style ESP box: soft translucent body plus crisp corner brackets.
     * Keeping this in one helper gives every overlay the same clean look.
     */
    public static void prestigeBox(DrawContext context, int x1, int y1, int x2, int y2,
                                   int rgb, int alpha) {
        prestigeBox(context, x1, y1, x2, y2, rgb, alpha, true);
    }

    public static void prestigeBox(DrawContext context, int x1, int y1, int x2, int y2,
                                   int rgb, int alpha, boolean glow) {
        int safeAlpha = (int) clamp(alpha, 0, 255);
        if (safeAlpha > 0 && x2 > x1 && y2 > y1) {
            context.fill(x1, y1, x2, y2, (safeAlpha << 24) | (rgb & 0xFFFFFF));
        }
        if (glow) {
            glowCornerBox(context, x1, y1, x2, y2, rgb, 2, 0.22f);
        } else {
            cornerBox(context, x1, y1, x2, y2, 0xFF000000 | (rgb & 0xFFFFFF), 2, 0.22f);
        }
    }

    /** Thin health bar with a dark track, drawn just under an ESP box. */
    public static void healthBar(DrawContext context, int x, int y, int width,
                                 float fraction, int rgb) {
        int w = Math.max(8, width);
        fraction = (float) clamp(fraction, 0.0, 1.0);
        context.fill(x - 1, y - 1, x + w + 1, y + 4, 0xB0000000);
        context.fill(x, y, x + w, y + 3, 0x80FFFFFF);
        if (fraction > 0.0f) {
            int fillWidth = Math.max(1, (int) (w * fraction));
            context.fill(x, y, x + fillWidth, y + 3, 0xFF000000 | (rgb & 0xFFFFFF));
        }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
