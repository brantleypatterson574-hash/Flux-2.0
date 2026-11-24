/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  org.lwjgl.glfw.GLFW
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.d.a;
import com.ishland.scalablelux.d.b;
import com.ishland.scalablelux.d.c;
import com.ishland.scalablelux.d.d;
import com.ishland.scalablelux.d.h;
import com.ishland.scalablelux.d.i;
import com.ishland.scalablelux.d.j;
import com.ishland.scalablelux.d.k;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
import com.ishland.scalablelux.gui.gpuui.kit.UiColorPicker;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public final class CosmeticsView {
    private static final float FRONT_YAW = -28.0f;
    private static final float BACK_YAW = 152.0f;
    private final Backend backend;
    private Section section = Section.CAPES;
    private final UiSmoothScroll scroll = new UiSmoothScroll();
    private final float[] centerRect = new float[4];
    private final float[] previewRect = new float[4];
    private float contentH;
    private float lastScale = 1.0f;
    private String status = "";
    private long statusAt = Long.MIN_VALUE;
    private float yaw = -28.0f;
    private float yawVel;
    private float pitch = 6.0f;
    private float roll;
    private double dragLastY;
    private long lastDragAt;
    private long lastFrameAt;
    private float frameDt = 0.016f;
    private final HashMap<String, Float> hoverAnim = new HashMap();
    private boolean dragging;
    private double dragLastX;
    private long lastSpinAt;
    private float yawTarget = Float.NaN;
    private final Random random = new Random();
    private final UiTextField textField = new UiTextField(CosmeticsView.xs("\udc38\uc45d\u248d\u61db\u4df1\u22bd\u1a9d\uc41c\u29c8\u26c9\uab02\ucb2d\u0948\u71b6\ue351\u902b\u6c89\u566a\ua2e6", -289870109, 0x232E9450 ^ CryptoVault.mask())).maxLength(4);
    private final UiColorPicker picker = new UiColorPicker();
    private int swatchSeq;
    private int openPicker = -1;
    private final List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
    private boolean capturingInspectKey;

    public CosmeticsView(Backend backend) {
        this.backend = backend;
        this.textField.allow(n2 -> Character.isLetterOrDigit(n2) && n2 < 128);
        this.textField.onChange(() -> {
            a a2 = backend.prefs().A().c(this.textField.value());
            this.applyCustom(a2);
        });
    }

    public float[] previewRect() {
        return this.previewRect;
    }

    public float previewYaw() {
        return this.yaw;
    }

    public float previewPitch() {
        return this.pitch;
    }

    public float previewRoll() {
        return this.roll;
    }

    private static boolean shiftDown() {
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null || class_3102.method_22683() == null) {
                return false;
            }
            long l2 = class_3102.method_22683().method_4490();
            return GLFW.glfwGetKey((long)l2, (int)340) == 1 || GLFW.glfwGetKey((long)l2, (int)344) == 1;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public List<Thumbnail> thumbnails() {
        return this.thumbnails;
    }

    public float[] thumbnailClip() {
        return this.centerRect;
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        if (CosmeticsView.inside(d2, d3, this.centerRect)) {
            this.scroll.wheel(d4, 56.0f * this.lastScale, l2);
            return true;
        }
        return false;
    }

    public boolean mouseDragged(double d2, double d3) {
        if (this.picker.drag(d2, d3)) {
            return true;
        }
        if (!this.dragging) {
            return false;
        }
        float f2 = (float)(d2 - this.dragLastX) * 0.55f / Math.max(0.5f, this.lastScale);
        float f3 = (float)(d3 - this.dragLastY) * 0.45f / Math.max(0.5f, this.lastScale);
        long l2 = System.nanoTime();
        float f4 = Math.max(0.004f, Math.min(0.1f, (float)(l2 - this.lastDragAt) / 1.0E9f));
        this.lastDragAt = l2;
        if (CosmeticsView.shiftDown()) {
            this.roll = Math.max(-75.0f, Math.min(75.0f, this.roll + f2 * 0.8f));
        } else {
            this.yaw += f2;
            this.yawVel = this.yawVel * 0.5f + f2 / f4 * 0.5f;
            this.pitch = Math.max(-60.0f, Math.min(60.0f, this.pitch - f3));
        }
        this.dragLastX = d2;
        this.dragLastY = d3;
        return true;
    }

    public void mouseReleased() {
        this.picker.release();
        this.dragging = false;
    }

    private static boolean inside(double d2, double d3, float[] fArray) {
        return d2 >= (double)fArray[0] && d2 <= (double)(fArray[0] + fArray[2]) && d3 >= (double)fArray[1] && d3 <= (double)(fArray[1] + fArray[3]);
    }

    private void say(String string, UiHost uiHost) {
        this.status = string == null ? "" : string;
        this.statusAt = uiHost.now();
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.lastScale = f6;
        this.swatchSeq = 0;
        long l2 = uiHost.now();
        this.frameDt = this.lastFrameAt == 0L ? 0.016f : Math.max(0.001f, Math.min(0.1f, (float)(l2 - this.lastFrameAt) / 1.0E9f));
        this.lastFrameAt = l2;
        c c2 = this.backend.prefs();
        this.backend.weaponPreview(this.section == Section.WEAPONS);
        this.thumbnails.clear();
        if (!this.backend.unlocked()) {
            this.previewRect[2] = 0.0f;
            return;
        }
        float f9 = Math.round(22.0f * f6);
        float f10 = f3 + (float)Math.round(20.0f * f6);
        float f11 = f3 + f5 - (float)Math.round(34.0f * f6);
        float f12 = Math.round(16.0f * f6);
        float f13 = Math.round(168.0f * f6);
        float f14 = f4 - f9 * 2.0f - f13 - f12 * 2.0f;
        float f15 = Math.round(Math.max(200.0f * f6, Math.min(380.0f * f6, f14 * 0.44f)));
        float f16 = f14 - f15;
        float f17 = f2 + f9;
        float f18 = f17 + f13 + f12;
        float f19 = f18 + f16 + f12;
        float f20 = Math.round(46.0f * f6);
        this.drawSections(builder, uiHost, c2, f17, f10, f13, f11 - f10, f6, f7, f8);
        builder.text(f18, f10 + (float)Math.round(2.0f * f6), 14.0f * f6, uiHost.textColor(), this.section.label, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f18, f10 + (float)Math.round(24.0f * f6), 9.5f * f6, uiHost.muted2Color(), UiKit.fit(CosmeticsView.blurb(this.section), f16, 9.5f * f6));
        this.drawCenter(builder, uiHost, c2, f18, f10 + f20, f16, f11 - f10 - f20, f6, f7, f8);
        this.drawPreview(builder, uiHost, c2, f19, f10, f15, f11 - f10, f6, f7, f8);
        if (!this.status.isEmpty() && uiHost.now() - this.statusAt < 6000000000L) {
            builder.text(f17, f11 + (float)Math.round(10.0f * f6), 10.0f * f6, uiHost.accent(), UiKit.fit(this.status, f4 - f9 * 2.0f, 10.0f * f6));
        } else {
            builder.text(f17, f11 + (float)Math.round(10.0f * f6), 10.0f * f6, uiHost.muted2Color(), UiKit.fit(CosmeticsView.xs("\uf2db\u3ba0\udf53\u6915\u7e3c\u1fbd\u43ca\ueb28\u5a18\u652a\uf5b4\u4a02\ub8ef\u5d60\ue586\u8bf5\u3afa\ub1fb\ue2fd\u9f90\u5cce\u67f9\ua01b\u52ed\uee60\u824d\ub065\uc234\u7762\u4ee5\u85ca\u121a\u86a6\ue9b0\u0011\ub573\uc9c8\udac9\u1798\uef53\u1768\ua0c5\u6435\u19fe\ub956\u7191\uda23\u24a6\u133a\ub87f\u4165\u5dd0\u1bea\u5a04\ucc5f\uedfe\u268a\ua93a\u2ba6\u37a2\ua349\uef3e\u2391\u2968\u4297\u5d36\u4311\uc490\u999b\u0d62\u8691\u2b36\u5fff\u6431\ua544\u46c0\u7fbc\ud3d3\ucb73\u878e", -1272924540, 0x232E9450 ^ CryptoVault.mask()), f4 - f9 * 2.0f, 10.0f * f6));
        }
    }

    private void drawSections(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        d d2 = c2.A();
        float f9 = Math.round(40.0f * f6);
        float f10 = Math.round(4.0f * f6);
        float f11 = f3;
        String string = null;
        for (Section section : Section.values()) {
            if (!section.group.equals(string)) {
                string = section.group;
                if (f11 > f3) {
                    f11 += (float)Math.round(12.0f * f6);
                }
                builder.text(f2 + (float)Math.round(6.0f * f6), f11, 8.5f * f6, uiHost.muted2Color(), string.toUpperCase(Locale.ROOT), UiDrawCommand.TextWeight.SEMIBOLD);
                f11 += (float)Math.round(18.0f * f6);
            }
            boolean bl = section == this.section;
            boolean bl2 = uiHost.hovered(f7, f8, f2, f11, f4, f9);
            float f12 = Math.round(10.0f * f6 * uiHost.cornerScale());
            if (bl) {
                builder.rect(f2, f11, f4, f9, f12, UiKit.withAlpha(uiHost.accent(), 38));
                builder.border(f2, f11, f4, f9, f12, 1.0f, UiKit.withAlpha(uiHost.accent(), 96));
                builder.rect(f2 + (float)Math.round(3.0f * f6), f11 + (float)Math.round(9.0f * f6), Math.max(2.0f, 3.0f * f6), f9 - (float)Math.round(18.0f * f6), Math.max(1.0f, 1.5f * f6), uiHost.accent());
            } else if (bl2) {
                builder.rect(f2, f11, f4, f9, f12, uiHost.controlFill(20));
            }
            float f13 = 11.0f * f6;
            builder.text(f2 + (float)Math.round(14.0f * f6), f11 + (float)Math.round(7.0f * f6), f13, bl ? uiHost.accent() : uiHost.textColor(), section.label, UiDrawCommand.TextWeight.SEMIBOLD);
            String string2 = switch (section.ordinal()) {
                case 0 -> {
                    if (d2.B() == null) {
                        yield CosmeticsView.xs("\u8ffd\ud1b8\u6fd6\u0cd2\ub4c5\u8d63\u370b", -1405887567, 0x232E9450 ^ CryptoVault.mask());
                    }
                    yield CosmeticsView.capeName(d2.B());
                }
                case 1 -> {
                    if (d2.A() == null) {
                        yield CosmeticsView.xs("\u9691\uecdb\ued2a\u7b2f\u0098\u60a5\ud9e3", 1197921483, 0x232E9450 ^ CryptoVault.mask());
                    }
                    yield CosmeticsView.itemName(b.no, d2.A().H());
                }
                case 3 -> {
                    if (d2.A() == null) {
                        yield CosmeticsView.xs("\u3d3a\u8a82\ubbbe\u1da4\u5228", -141385659, 0x232E9450 ^ CryptoVault.mask());
                    }
                    yield CosmeticsView.itemName(b.DR, d2.A().I());
                }
                case 4 -> c2.A().K() + CosmeticsView.xs("\uf35f\ufc0aJ\uee91\u040c", -418796107, 0x232E9450 ^ CryptoVault.mask()) + c2.A().p();
                case 5 -> {
                    if (c2.A().j()) {
                        yield c2.A().g();
                    }
                    yield CosmeticsView.xs("\u30df\u7aae\u6ce0\uc2a1", 1979286349, 0x232E9450 ^ CryptoVault.mask());
                }
                case 9 -> {
                    if (c2.A().B() != null) {
                        yield CosmeticsView.xs("\uc4af\u8a7f\u7934\u7ef7\u3ed7\uc6ad\u80eb\u515f\u190e\u6245\u7f20\u2755\ub798\ub199", -716768182, 0x232E9450 ^ CryptoVault.mask());
                    }
                    yield CosmeticsView.xs("\ua0ba\ua6f1\ubac6\u6c7c\u249c\u2a3a\ue09c\u94de\uae62\u4f05\u09d6\ubfb3\u188d\uad74\u4199\u5c7b", 54219344, 0x232E9450 ^ CryptoVault.mask());
                }
                case 10 -> {
                    if (c2.B()) {
                        yield CosmeticsView.xs("\u08dd\u5cc4\ufbd1\ua6b1\u1db6\u4231\u7b45\u1d32\ud9eb\u565d\u54c7", -1693879944, 0x232E9450 ^ CryptoVault.mask());
                    }
                    yield CosmeticsView.xs("\ubbb0\u7980\u7a78\u13b4\uaabe\udb6f\ufe88\ucf9e\u4f23\u87ec\u849b\u93f5\u5269", -1118509799, 0x232E9450 ^ CryptoVault.mask());
                }
                default -> d2.A(section.slot) == null ? CosmeticsView.xs("\ub084\u99f1\u7edf\u4919\uab5c\ub1bf\u7c38\u88b0", -1375135938, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.itemName(section.slot.tK, d2.A(section.slot).G());
            };
            builder.text(f2 + (float)Math.round(14.0f * f6), f11 + (float)Math.round(23.0f * f6), 9.0f * f6, uiHost.muted2Color(), UiKit.fit(string2, f4 - 28.0f * f6, 9.0f * f6));
            Section section2 = section;
            uiHost.hit(f2, f11, f4, f9, () -> {
                if (this.section != section2) {
                    this.section = section2;
                    this.scroll.reset();
                }
            });
            f11 += f9 + f10;
        }
    }

    private static String blurb(Section section) {
        return switch (section.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> CosmeticsView.xs("\u0212\uf28d\u31fb\u5ac4\u5753\ua96b\udc5d\uf13e\u7c9e\u0f28\u745a\ua245\u574e\u8329\u7eb0\u80ea\ue84f\uda12\u2535\u78a6\u30dc\ubf85\ud122\ucbdd\ua7f1\ub952\ucf8d\u8fea\ued3e\u241d\u65a3\ub5dd\u991c\uec72\u8283\ued4e\uc54f\u87a8\ucab0\ueecf\uca30\uadd5\ue3d7\uef4b\uccb9\ub021\u4fd0\u8ca0\u2543\u7236\u0803\uedaf\u06e9\u840a\u9204\uee93\u3c2b\u6269\u2294\uc2a6\uade5\u0558\u97d6\u5ec8\u0591\u73b5\u4a3d\u59a5\uf9d6\u7633", 1698189259, 0x232E9450 ^ CryptoVault.mask());
            case 1 -> CosmeticsView.xs("\u0508\uda5c\uf311\ufa6f\ue9ed\ua4e8\u7827\ubb79\u622e\u55fc\u2165\uc01a\ue11f\u4a10\u6a3d\u945a\ud314\uc216\u54b7\ue050\u1fcb\u436c\uf9c3\u788c\ude57\u778f\u9cf1\ua5f0\ub770\uecbe\ud5db\u0ebe\ua69f\u3340\ud89a\ubf9f\uca38\u69b8\ub6d1\u55b6\ue92f\u2dde\ucd30\u2bee\u5d1c\udfc9\u438c\uabec", 194492243, 0x232E9450 ^ CryptoVault.mask());
            case 2 -> CosmeticsView.xs("\uab8d\u60ce\u0bbe\u0802\ua28d\u1fba\u76fa\u6d3a\u0973\u139b\u4566\udf5c\u21a3\ub590\uc81f\u74bb\ud6bd\u3e66\uc689\ubd70\u74cb\u6b36\u0c4f\ua4f4\u14d1\ua250\u9b85\u18b4\u4921\u41e7\u0c48\ud847\ubfff\u0adb\u7c6e\u48a0\uc50b\ue824\u33f7\u39be\ucff7\u0b96\u02c0\uf2ca\u847a\ua9d7\u8dbc\u619e", -1772297125, 0x232E9450 ^ CryptoVault.mask());
            case 3 -> CosmeticsView.xs("\udfd9\u9959\ue535\u96ef\ue3bb\u427f\uc999\u8644\u9e9f\u9529\ued7b\uc2e3\u4d36\u26ce\ub145\u0f61\u0360\u2fce\u04eb\u41b3\udd45\uc2b6\ufa60\u2f19\u4d1c\uf048\ubb3e\ufde6\u7e81\uf8af\ue2de\u57c0\u27fd\u5d69\u8e47\u931a\uf2e5\ud6cc\u2ea1\uce5f\u953c\u2962\u5936\ue326\u38a8\u521a\ue213\u8392\u13d7\ue21b\u0791\uafce\u7022\u209a\u2a53\uceb7\u403a\u5c1c\ud1f6\ue8b0\u3e4e\uc21b\u5bf8", -2017152831, 0x232E9450 ^ CryptoVault.mask());
            case 4 -> CosmeticsView.xs("\u0441\ue7b4\u0856\u7a16\uf73a\uf192\ue70d\ufbba\uba74\uc589\u9b22\ucdc4\u7c3e\uc851\u229c\ueddc\ue403\ufed8\u6c9c\uaf76\u93c7\u9689\ucd9c\ufd3c\u408b\ud308\ud4a2\u5266\uef9e\u471a\u3cb4\uc621\ue7c2\u41d9\ua112\ud826\uea79\u3e7b\ud3af\ub4ff\u7088\u9d5d\ua6f0\u3566\u1251\u6ce8\ua8f8\u8dab\u12fb\u622b\ua140\u9b74\ub505\ue0f2\ue145\ufe05\u5687\u7108\u0f92\ua14c\u4ed8\u3bdc\u6921\u2e2a", -1394894678, 0x232E9450 ^ CryptoVault.mask());
            case 5 -> CosmeticsView.xs("\u8358\uafdd\u5b64\u919f\ub3d9\uc4f7\u460b\u0770\u648e\u8ec9\u04c6\u1dc8\u1c59\u6795\u1993\u7ea2\uf104\ub53f\u0d4d\u2a50\u8104\ue591\u7240\u74fa\u839d\u1697\udd45\u97da\u009d\u0717\u2630\u8baf\ud0f8\u7009\udbb8\u1a43\u00bb\u990a\u1f53\uc32e\u835f\uc948\u4143\u62fb\u49bb\u726f\ue6d9\uc78e\u8059\u293c\ue519\uce89\u23ae\udb35\u35ba\u3798\ueb4c\uc974\ue166\udd67\u900d\ub38f\ud1d5\u7c90\u1974\u5495\ud106\u2a50\u732f\udc60\u6abd\uce1f\ub838\uc5d2\u483f\u5201\u5109\u4787", 511278621, 0x232E9450 ^ CryptoVault.mask());
            case 6 -> CosmeticsView.xs("\ue63d\u5078\uc719\u4f10\u93aa\u029c\uc993\u1d05\uf170\u47fe\u27c9\ub95e\u69be\u84a8\u2033\u6502\u67bf\ub1db\ue1c8\ud06c\u9985\ufeab\ub064\u7144\ud7ae\u6307\u20d1\u3def\u99f3\u731c\ue4b4\udf9e\ud8be\u7bbe\u4d23\ueb34\ub631\ue586\u5519\u7fe1\u8c9a\uf8d5\u8ccc\u5546\u049a\u0696\uee0e\ue3d5\u79c7\ub911\u57ca\u28ed\ue074\u4f64\u0b10\uab44\u44f6\u7918\u50c7", -1456784867, 0x232E9450 ^ CryptoVault.mask());
            case 7 -> CosmeticsView.xs("\u7d05\u7a11\u072d\u5b92\u3db1\u46ea\u989c\uee98\u0c70\u3a59\u9395\u10ac\ufab7\u15c7\u92e0\uf51f\u054e\uedfe\u50e1\u18b0\u1751\ucec5\u6ffd\ud86d\uc066\u0303\u3471\u333d\ud5e4\u7a33\u441e\ua584\u83ba\u7bdf\uee6c\u4385\uce63\uc8c6\u29fb\u9f73\uc20c", 754313860, 0x232E9450 ^ CryptoVault.mask());
            case 8 -> CosmeticsView.xs("\u7077\u2267\u9816\u16c0\uf38f\u5cda\u0b55\uba47\u9fdf\uaeb7\u9e73\u9dd9\u5a64\u8751\ua7b7\u619f\u7ae0\u69d2\u667f\u8062\u3076\uc5e9\ud38f\ud83d\u85c2\u4467\ub4dd\u551c\u8d1b\ubbf0\u9d40\u218b\u9df4\uc6f0\u7351\u2593\uf24a\ude75\ud688\ub9a2", -632029864, 0x232E9450 ^ CryptoVault.mask());
            case 9 -> CosmeticsView.xs("\u25fb\ud375\u3455\u369e\uf224\u9604\uefc6\uee6b\u3954\ub31b\uf4f9\u61f0\u0ed1\u332e\u1a2e\uda18\u35ee\u3677\ua6e6\ub8ef\u4086\ua9cd\u0f57\u07f4\u5935\u75bf\u22c6\u39f1\u03d4\u6910\u6df0\u6ca2\u0bec\uf8d5\u8b16\u25c1\u8b78\u1fba\u4bee\u5dcf\u786f\ua031\ubc8e\u59f8\u1345\ue780\u126e\ue700\ua753\u7d97\u57fc", -1194975666, 0x232E9450 ^ CryptoVault.mask());
            case 10 -> CosmeticsView.xs("\u5a1f\u73ab\u59f5\u0872\ua29b\u6851\u9f0b\uc414\u4ffe\u0102\uf5d4\u4717\u697f\u4fb8\u37a4\u14a4\u8069\u7d7e\u6b88\u6ea6\ua581\uca38\uce69\u8774\u377c\ue453\u3a80\ucef0\udd99\u78fb\u6b76\ufadb\uc9a6\ucbaf\u95ff\u4fde\u1480\ua53c\u051d\uf615\u2984\ufc2f\u025d\u2628\uf94f\u3b7d\uf992\u4ddf\ufb05\u2841\u2266", 819089539, 0x232E9450 ^ CryptoVault.mask());
        };
    }

    private void drawCenter(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.centerRect[0] = f2;
        this.centerRect[1] = f3;
        this.centerRect[2] = f4;
        this.centerRect[3] = f5;
        this.scroll.update(uiHost.frameDelta(), f5, this.contentH, uiHost.now());
        float f9 = this.scroll.offset();
        builder.clipPush(f2, f3, f4, f5, 0.0f);
        uiHost.beginHitClip(f2, f3, f4, f5);
        float f10 = f3 - f9;
        float f11 = switch (this.section.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> this.drawCapes(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 1 -> this.drawHats(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 3 -> this.drawWings(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 4 -> this.drawWeapons(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 5 -> this.drawArmor(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 2 -> this.drawGear(builder, uiHost, c2, this.section.slot, 1, f2, f10, f4, f6, f7, f8);
            case 6 -> this.drawGear(builder, uiHost, c2, this.section.slot, 3, f2, f10, f4, f6, f7, f8);
            case 7, 8 -> this.drawGear(builder, uiHost, c2, this.section.slot, 2, f2, f10, f4, f6, f7, f8);
            case 9 -> this.drawMaker(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
            case 10 -> this.drawSettings(builder, uiHost, c2, f2, f10, f4, f6, f7, f8);
        };
        this.contentH = Math.max(0.0f, f11 - f10 + (float)Math.round(8.0f * f6));
        uiHost.endHitClip();
        builder.clipPop();
        this.scroll.paintBar(builder, f2 + f4 + (float)Math.round(3.0f * f6), f3, f5, f6, uiHost.textColor(), uiHost.now());
    }

    private float drawCapes(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        int n2;
        d d2 = c2.A();
        String string = d2.B() == null ? null : d2.B().n();
        int n3 = Math.max(2, (int)((f4 + 10.0f * f5) / (118.0f * f5)));
        float f8 = Math.round(10.0f * f5);
        float f9 = (f4 - f8 * (float)(n3 - 1)) / (float)n3;
        float f10 = Math.round(f9 * 1.28f);
        int n4 = a.sw.size() + 2;
        for (n2 = 0; n2 < n4; ++n2) {
            Record record;
            float f11 = f2 + (float)(n2 % n3) * (f9 + f8);
            float f12 = f3 + (float)(n2 / n3) * (f10 + f8);
            if (n2 == 0) {
                this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, CosmeticsView.xs("\u3dfd\ufb5a\u8e68\u5cb9\u10e3", -1656424536, 0x232E9450 ^ CryptoVault.mask()), d2.B() == null, null, 0, () -> {
                    c2.A(c2.A().A((a)null));
                    this.say(CosmeticsView.xs("\u3365\u601a\udd1d\u4e7e\u96c8\ucef6\u69e2\u06b6\uf1e5\udc49\u0401\ud2df\u4f3e\ue134\uf0fc", 2104005920, 0x232E9450 ^ CryptoVault.mask()), uiHost);
                });
                continue;
            }
            if (n2 == 1) {
                record = c2.A().a(CosmeticsView.xs("\u630d\u9793\u3376\u7faa\u0393\u141f\udde8\u6248\ua503", -1964824248, 0x232E9450 ^ CryptoVault.mask()));
                this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, CosmeticsView.xs("\u817a\uf65f\u0687\u6708\u84a1\u683f\u13ca\u6fbf\u8238\uc13c", 1979977851, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u3f55\ud19a\u48f4\u9697\u61d0\u2292\u42da\u6c0b\uf300\u9d46", 505757459, 0x232E9450 ^ CryptoVault.mask()).equals(string), (a)record, 0, () -> this.lambda$drawCapes$1(c2, (a)record, uiHost));
                continue;
            }
            record = a.sw.get(n2 - 2);
            a a2 = CosmeticsView.xs("\u75f4\u5b6a\u6605\u6a01\ufa20\u91a8\u7784\ucf75", -2103448548, 0x232E9450 ^ CryptoVault.mask()).equals(((a.e)record).k()) ? a.a(this.backend.accentRgb()) : ((a.e)record).a();
            this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, ((a.e)record).l(), ((a.e)record).k().equals(string), a2, 0, () -> this.lambda$drawCapes$2(c2, a2, (a.e)record, uiHost));
        }
        n2 = (n4 + n3 - 1) / n3;
        return f3 + (float)n2 * (f10 + f8) - f8;
    }

    private void tile(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, String string, boolean bl, a a2, int n2, Runnable runnable) {
        this.tile(builder, uiHost, f2, f3, f4, f5, f6, f7, f8, string, bl, a2, n2, null, 0, runnable);
    }

    private void tile(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, String string, boolean bl, a a2, int n2, d d2, int n3, Runnable runnable) {
        this.tile(builder, uiHost, f2, f3, f4, f5, f6, f7, f8, string, bl, a2, n2, d2, n3, null, runnable);
    }

    private void tile(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, String string, boolean bl, a a2, int n2, d d2, int n3, String string2, Runnable runnable) {
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        int n4;
        int n5;
        boolean bl2 = uiHost.hovered(f7, f8, f2, f3, f4, f5);
        float f14 = Math.round(12.0f * f6 * uiHost.cornerScale());
        String string3 = this.section.name() + ":" + string;
        float f15 = this.hoverAnim.getOrDefault(string3, Float.valueOf(0.0f)).floatValue();
        f15 += ((bl2 ? 1.0f : 0.0f) - f15) * Math.min(1.0f, this.frameDt * 14.0f);
        this.hoverAnim.put(string3, Float.valueOf(f15));
        float f16 = f3;
        f3 -= (float)Math.round(3.0f * f6 * f15);
        float f17 = 0.5f + 0.5f * (float)Math.sin((double)(uiHost.now() % 3000000000L) / 3.0E9 * Math.PI * 2.0);
        int n6 = a2 != null ? 0xFF000000 | a2.A() : (n5 = n2 != 0 ? 0xFF000000 | n2 : uiHost.accent());
        if (f15 > 0.01f || bl) {
            n4 = (int)(56.0f * f15 + (bl ? 32.0f + 24.0f * f17 : 0.0f));
            builder.shadow(f2, f3 + (float)Math.round(4.0f * f6), f4, f5, f14, Math.round(14.0f * f6), 0.0f, UiKit.withAlpha(bl ? uiHost.accent() : n5, Math.min(255, n4)));
        }
        builder.rect(f2, f3, f4, f5, f14, bl2 ? uiHost.controlFill(34) : uiHost.cardFill());
        n4 = bl ? UiKit.withAlpha(uiHost.accent(), 160 + (int)(80.0f * f17)) : UiKit.withAlpha(n5, 20 + (int)(90.0f * f15));
        builder.border(f2, f3, f4, f5, f14, bl ? Math.max(1.0f, 2.0f * f6) : 1.0f, n4);
        builder.rect(f2 + f14, f3, Math.max(0.0f, f4 - f14 * 2.0f), Math.max(1.0f, 2.0f * f6), Math.max(1.0f, f6), UiKit.withAlpha(n5, 80 + (int)(144.0f * Math.max(f15, bl ? 1.0f : 0.0f))));
        if (bl) {
            String string4 = CosmeticsView.xs("\u1fb7\ud422\ue170\u87b9\u75de\u6e99\uded8", -703698800, 0x232E9450 ^ CryptoVault.mask());
            f13 = 7.5f * f6;
            f12 = GpuUiFontMetrics.textWidth(string4, f13, UiDrawCommand.TextWeight.SEMIBOLD) + (float)Math.round(10.0f * f6);
            f11 = Math.round(13.0f * f6);
            f10 = f2 + f4 - f12 - (float)Math.round(6.0f * f6);
            f9 = f3 + (float)Math.round(7.0f * f6);
            builder.rect(f10, f9, f12, f11, f11 * 0.5f, uiHost.accent());
            builder.text(f10 + (float)Math.round(5.0f * f6), f9 + (f11 - f13) * 0.5f - 0.5f * f6, f13, -1, string4, UiDrawCommand.TextWeight.SEMIBOLD);
        }
        float f18 = Math.round(30.0f * f6);
        f13 = f5 - f18 - (float)Math.round(10.0f * f6);
        if (a2 != null) {
            f12 = f13;
            f11 = f12 * 10.0f / 16.0f;
            f10 = f2 + (f4 - f11) * 0.5f;
            f9 = f3 + (float)Math.round(10.0f * f6);
            builder.shadow(f10, f9, f11, f12, Math.round(3.0f * f6), Math.round(10.0f * f6), 0.0f, 0x60000000);
            class_2960 class_29602 = this.backend.capeTexture(a2);
            if (class_29602 != null) {
                builder.imagePixelated(f10, f9, f11, f12, Math.round(3.0f * f6), -1, class_29602, 0.015625f, 0.03125f, 0.171875f, 0.53125f);
            } else {
                builder.gradient(f10, f9, f11, f12, Math.round(3.0f * f6), a2.z(), a2.z(), a2.A(), a2.A());
            }
        } else if ((d2 != null || string2 != null) && this.backend.previewAvailable()) {
            f12 = f2 + (float)Math.round(6.0f * f6);
            f11 = f3 + (float)Math.round(6.0f * f6);
            f10 = f4 - (float)Math.round(12.0f * f6);
            f9 = f13 + (float)Math.round(4.0f * f6);
            float f19 = Math.round(9.0f * f6 * uiHost.cornerScale());
            int n7 = n2 == 0 ? uiHost.accent() : 0xFF000000 | n2;
            builder.gradient(f12, f11, f10, f9, f19, 0, 0, UiKit.withAlpha(n7, 48), UiKit.withAlpha(n7, 48));
            float[] fArray = this.centerRect;
            if (f11 + f9 > fArray[1] && f11 < fArray[1] + fArray[3]) {
                this.thumbnails.add(new Thumbnail(f12, f11, f10, f9, d2, n3, string2));
            }
        } else if (n2 != 0) {
            f12 = Math.min(f13, f4) * 0.3f;
            f11 = f2 + f4 * 0.5f;
            f10 = f3 + (float)Math.round(10.0f * f6) + f13 * 0.5f;
            builder.circle(f11, f10, f12 + Math.max(1.0f, f6), 0x40000000);
            builder.circle(f11, f10, f12, n2 | 0xFF000000);
        } else {
            f12 = 11.0f * f6;
            String string5 = "-";
            builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string5, f12)) * 0.5f, f3 + (float)Math.round(10.0f * f6) + f13 * 0.5f - f12 * 0.6f, f12, uiHost.muted2Color(), string5);
        }
        f12 = Math.max(f15, bl ? 0.6f : 0.0f);
        if (f12 > 0.02f) {
            float f20 = f2 + (float)Math.round(8.0f * f6);
            f10 = f3 + (float)Math.round(8.0f * f6);
            f9 = f4 - (float)Math.round(16.0f * f6);
            float f21 = f13;
            float f22 = (float)(uiHost.now() % 2600000000L) / 2.6E9f;
            float f23 = f9 * 0.38f;
            float f24 = f20 + f22 * (f9 - f23);
            int n8 = (int)(52.0f * f12 * (float)Math.sin(Math.PI * (double)f22));
            if (n8 > 0) {
                builder.gradient(f24, f10, f23 * 0.5f, f21, 0.0f, 0xFFFFFF, UiKit.withAlpha(0xFFFFFF, n8), UiKit.withAlpha(0xFFFFFF, n8), 0xFFFFFF);
                builder.gradient(f24 + f23 * 0.5f, f10, f23 * 0.5f, f21, 0.0f, UiKit.withAlpha(0xFFFFFF, n8), 0xFFFFFF, 0xFFFFFF, UiKit.withAlpha(0xFFFFFF, n8));
            }
        }
        float f25 = 10.0f * f6;
        String string6 = UiKit.fit(string, f4 - 12.0f * f6, f25, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string6, f25, UiDrawCommand.TextWeight.SEMIBOLD)) * 0.5f, f3 + f5 - f18 + (float)Math.round(6.0f * f6), f25, bl ? uiHost.accent() : uiHost.textColor(), string6, UiDrawCommand.TextWeight.SEMIBOLD);
        if (bl) {
            String string7 = CosmeticsView.xs("\u821d\ua851\ueee8\ud644\u0b95\u8a01\u76b7\u45eb\u56e2\ud613", 1208463105, 0x232E9450 ^ CryptoVault.mask());
            float f26 = 8.5f * f6;
            builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string7, f26)) * 0.5f, f3 + f5 - f18 + (float)Math.round(19.0f * f6), f26, uiHost.muted2Color(), string7);
        }
        uiHost.hit(f2, f16, f4, f5, runnable);
    }

    private float drawHats(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        int n3;
        d d2 = c2.A();
        String string = d2.A() == null ? null : d2.A().H();
        int n4 = Math.max(2, (int)((f4 + 10.0f * f5) / (118.0f * f5)));
        float f9 = Math.round(10.0f * f5);
        float f10 = (f4 - f9 * (float)(n4 - 1)) / (float)n4;
        float f11 = Math.round(Math.min(f10, 120.0f * f5));
        List<b.a> list = b.no;
        int n5 = list.size() + 1;
        for (n3 = 0; n3 < n5; ++n3) {
            f8 = f2 + (float)(n3 % n4) * (f10 + f9);
            float f12 = f3 + (float)(n3 / n4) * (f11 + f9);
            if (n3 == 0) {
                this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, CosmeticsView.xs("\u8339\u435d\u4adf\uabdc\u78c9\u66be\uadab\u71ab", 1503459809, 0x232E9450 ^ CryptoVault.mask()), string == null, null, 0, () -> {
                    c2.A(c2.A().A((d.b)null));
                    this.say(CosmeticsView.xs("\ud1c7\ub248\u2ab8\u35d9\u1e3f\u9c5f\u9e95\u22b4\u5844\uedae\u3233\ubb3b\ueba0\udd95\u0b17\u6f8f", 998536975, 0x232E9450 ^ CryptoVault.mask()), uiHost);
                });
                continue;
            }
            b.a a2 = list.get(n3 - 1);
            boolean bl = a2.C().equals(string);
            int n6 = bl ? this.displayColor(d2.A().K()) : this.displayColor(a2.F());
            d d3 = d.sm.A(new d.b(a2.C(), bl ? d2.A().K() : a2.F()));
            this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, a2.D(), bl, null, n6, d3, 0, () -> {
                c2.A(c2.A().A(new d.b(a2.C(), a2.F())));
                this.say(CosmeticsView.xs("\ua5f9\u5943\u25a7\u5e74\u0f6d\u2c38\u0ca6\u8e38\u37ef", -166628110, 0x232E9450 ^ CryptoVault.mask()) + a2.D() + ". " + a2.E(), uiHost);
            });
        }
        n3 = (n5 + n4 - 1) / n4;
        f8 = f3 + (float)n3 * (f11 + f9) + (float)Math.round(8.0f * f5);
        if (d2.A() != null) {
            UiKit.label(builder, uiHost, CosmeticsView.xs("\ue4c1\uabd3\u8932\uaf71\u945c\u218f\u3174\u1e79\u18a7\u935c\u991e\udd20", 2067644179, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 += (float)Math.round(18.0f * f5);
            int n7 = d2.A().K();
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, true, n7 == -1 ? -2 : n7 & 0xFFFFFF, n2 -> {
                d.b b2 = c2.A().A();
                if (b2 != null) {
                    c2.A(c2.A().A(new d.b(b2.H(), n2)));
                }
            });
        }
        return f8;
    }

    private int displayColor(int n2) {
        return n2 == -1 ? this.backend.accentRgb() & 0xFFFFFF : n2 & 0xFFFFFF;
    }

    private float drawWings(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        Object object;
        float f8;
        int n3;
        d d2 = c2.A();
        d.d d3 = d2.A();
        String string = d3 == null ? null : d3.I();
        int n4 = Math.max(2, (int)((f4 + 10.0f * f5) / (118.0f * f5)));
        float f9 = Math.round(10.0f * f5);
        float f10 = (f4 - f9 * (float)(n4 - 1)) / (float)n4;
        float f11 = Math.round(Math.min(f10, 120.0f * f5));
        int n5 = b.DR.size() + 1;
        for (n3 = 0; n3 < n5; ++n3) {
            f8 = f2 + (float)(n3 % n4) * (f10 + f9);
            float f12 = f3 + (float)(n3 / n4) * (f11 + f9);
            if (n3 == 0) {
                this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, CosmeticsView.xs("\u850e\u3f6c\ud0bd\ub8ae\u62b1\uf3b5\u1bbb", -364820371, 0x232E9450 ^ CryptoVault.mask()), d3 == null, null, 0, () -> {
                    c2.A(c2.A().A((d.d)null));
                    this.say(CosmeticsView.xs("\uc04a\u4109\u9ccd\u7eac\u9e7f\u3a2f\ue54f\ua972\u6eb5\u1d0a\u4598\u533b\u8074\u9d57\u3c38\uc22e\u97a8\u7c83", 774352176, 0x232E9450 ^ CryptoVault.mask()), uiHost);
                });
                continue;
            }
            object = b.DR.get(n3 - 1);
            boolean bl = ((b.a)object).C().equals(string);
            d d4 = d.sm.A(d3 == null ? d.d.A(((b.a)object).C()) : d3.B(((b.a)object).C()));
            this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, ((b.a)object).D(), bl, null, ((b.a)object).F(), d4, 1, () -> this.lambda$drawWings$1(c2, (b.a)object, uiHost));
        }
        n3 = (n5 + n4 - 1) / n4;
        f8 = f3 + (float)n3 * (f11 + f9) + (float)Math.round(16.0f * f5);
        if (d3 == null) {
            return f8;
        }
        b.b b2 = b.A(d3.I());
        if (b2 != null) {
            UiKit.label(builder, uiHost, CosmeticsView.xs("\u365a\u7650\u40a5\ue8e3\u60f7\uc827\ua3f1\u3eb4\u2236", 563416461, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            builder.text(f2, f8 += (float)Math.round(18.0f * f5), 10.0f * f5, uiHost.mutedColor(), UiKit.fit(b2.o() + CosmeticsView.xs("\u4060\u55f6\u5b60\u3495\u7a2d\uf224\ub5af\u248e\u6843\ue7b1\ubb51\uead0\u2671\ub45d\u25f9\ufa60\u365f\uc06e\u7e7a\u0cd7\uec02\uf11d\u3816\u2bee\ua3d9\udd65\u632b\ua422\uc384\u5ec3\u362e\uc632\uf569\ue142\uf94b\uafb8\u1fd8\u3471\uc6ce\u6100\u2b2c\u492e\u62c3\u39c5\u0373\u7e8a\u51c9\u42b0\u3d09\u4444\u7563\ub606\u55fb\u2f97\u6632\u16ff\uebcb\u7b9d\uce4d\u35fb\uc0c9\uaaca\uc1b1", -126537000, 0x232E9450 ^ CryptoVault.mask()), f4, 10.0f * f5));
            f8 += (float)Math.round(24.0f * f5);
        } else {
            UiKit.label(builder, uiHost, CosmeticsView.xs("\ud2d4\u04c9\u02aa\u92c8\u76a3\ua8f5\u7752", 493234751, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            object = d.d.ov;
            float f13 = Math.round(30.0f * f5);
            UiKit.segmented(builder, uiHost, object.toArray(new String[0]), object.indexOf(d3.J()), f2, f8 += (float)Math.round(18.0f * f5), f4, f13, f5, f6, f7, arg_0 -> CosmeticsView.lambda$drawWings$2(c2, (List)object, arg_0));
            f8 += f13 + (float)Math.round(10.0f * f5);
            if (CosmeticsView.xs("\u008d\u5522\u0eeb\u2289\u79f6\u83bd\u83d9\u6752", -1430632430, 0x232E9450 ^ CryptoVault.mask()).equals(d3.J())) {
                f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, d3.L() & 0xFFFFFF, n2 -> c2.A(c2.A().A(c2.A().A().A(n2))));
                f8 += (float)Math.round(6.0f * f5);
            }
        }
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\uee77\ue977\ud208\u4a11\u6b0a\u171c\u05c0\u3dea", -970724427, 0x232E9450 ^ CryptoVault.mask()), Math.round(d3.C() * 100.0f) + "%", () -> c2.A(c2.A().A(c2.A().A().A(c2.A().A().C() - 0.05f))), () -> c2.A(c2.A().A(c2.A().A().A(c2.A().A().C() + 0.05f))));
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\u2835\u79da\ubfb9\u0929\u6296\uea28\u33d2\u617c\ub567\ue3cb\uc481\u5974", 1800648194, 0x232E9450 ^ CryptoVault.mask()), Math.round(d3.D() * 100.0f) + "%", () -> c2.A(c2.A().A(c2.A().A().B(c2.A().A().D() - 0.05f))), () -> c2.A(c2.A().A(c2.A().A().B(c2.A().A().D() + 0.05f))));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ua2f5\u1cf4\u82c8\u38ac\u1938\ud1c5\u8e85\u7d12\ufc84\u8ea0\u15e0\ub576\u3e5a\ub316\u3f16", 471091155, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\uc7a8\u782f\u3e30\u8f67\u4eef\u0efd\u1939\u0419\ubec0\u2df6\u5d1c\u75c9\u2a9c\u6a49\udd8e\u3f67\u44ed\ue38b\u7529\udfb8\u20f6\u9421\u8830\u744f\u9f07\u87d9\u6f97\u4a4b\ueb78\u0d54\ua6bc\u2d14\ue9a6\u2c06\ud37d\u310e\u38f6\u624b\u9bfc\uec41\u16f7\u1adf\u4c61\u1758\u8681\u5872\u507e\u28ba\u4ff7\ufaf5\u6e09\uff23\uee41\ub61a\u1866\udb5a\u44c4\ua7eb\u56b6\u6d0a\ua566\u926b\udb47\u9939\u1330\ueffe\u6c2d\u6cdc\u4dc0\uaf96\udff1\u1e5f\u9c26\ucd6c\u7dfa\u9a05\u29da\u6808\u1693", -1523169651, 0x232E9450 ^ CryptoVault.mask()), d3.G(), () -> c2.A(c2.A().A(c2.A().A().A(!c2.A().A().G()))));
        return f8;
    }

    private float drawGear(UiDisplayList.Builder builder, UiHost uiHost, c c2, d.c c3, int n3, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        int n4;
        d d2 = c2.A();
        d.a a2 = d2.A(c3);
        String string = a2 == null ? null : a2.G();
        int n5 = Math.max(2, (int)((f4 + 10.0f * f5) / (112.0f * f5)));
        float f9 = Math.round(10.0f * f5);
        float f10 = (f4 - f9 * (float)(n5 - 1)) / (float)n5;
        float f11 = Math.round(Math.min(f10 * 1.1f, 132.0f * f5));
        List<b.a> list = c3.tK;
        int n6 = list.size() + 1;
        for (n4 = 0; n4 < n6; ++n4) {
            f8 = f2 + (float)(n4 % n5) * (f10 + f9);
            float f12 = f3 + (float)(n4 / n5) * (f11 + f9);
            if (n4 == 0) {
                this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, CosmeticsView.xs("\ud1f5\u0364\ub486\ud38a\u3c3b\u9982", -110608498, 0x232E9450 ^ CryptoVault.mask()), a2 == null, null, 0, () -> {
                    c2.A(c2.A().A(c3, null));
                    this.say(CosmeticsView.xs("\uf025\u6e5a\ua2f5\u7433\u3534\u6dde\uaf7d\ue797\u4243\u8b34", -118799717, 0x232E9450 ^ CryptoVault.mask()), uiHost);
                });
                continue;
            }
            b.a a3 = list.get(n4 - 1);
            boolean bl = a3.C().equals(string);
            int n7 = bl ? a2.J() : a3.F();
            d d3 = d.sm.A(c3, new d.a(a3.C(), n7));
            this.tile(builder, uiHost, f8, f12, f10, f11, f5, f6, f7, a3.D(), bl, null, this.displayColor(n7), d3, n3, () -> {
                c2.A(c2.A().A(c3, new d.a(a3.C(), a3.F())));
                this.say(a3.D() + ": " + a3.E(), uiHost);
            });
        }
        n4 = (n6 + n5 - 1) / n5;
        f8 = f3 + (float)n4 * (f11 + f9) + (float)Math.round(8.0f * f5);
        if (a2 != null) {
            UiKit.label(builder, uiHost, CosmeticsView.xs("\u2758\u130d\u90ff\u3732\uaf36\u98a6\u521f\uda1d\uc1d5\u3485", 1889191302, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 += (float)Math.round(18.0f * f5);
            int n8 = a2.J();
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, true, n8 == -1 ? -2 : n8 & 0xFFFFFF, n2 -> {
                d.a a2 = c2.A().A(c3);
                if (a2 != null) {
                    c2.A(c2.A().A(c3, new d.a(a2.G(), n2)));
                }
            });
        }
        return f8;
    }

    private static String keyName(int n2) {
        if (n2 < 0) {
            return CosmeticsView.xs("\u409d\udcf2\u2bcb\ubaaf\u5af7\u52f4\u287f\u7bd9\ufa95\uf687\ubb95", -1500896525, 0x232E9450 ^ CryptoVault.mask());
        }
        int n3 = 500;
        if (n2 >= n3) {
            return n2 == n3 + 2 ? CosmeticsView.xs("\u5edd\ud608\u6f03\u6884\u8086\u07e3\u1207\ud895\uc30b\u697c\u6277\u894a\u25ef\u18c9", -669979583, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.xs("\u49f8\u81b0\uf18f\u22fe\u3b6c\u53b8\u20bb\ubbd8", 1281768566, 0x232E9450 ^ CryptoVault.mask()) + (n2 - n3 + 1);
        }
        String string = GLFW.glfwGetKeyName((int)n2, (int)0);
        if (string != null) {
            return string.toUpperCase(Locale.ROOT);
        }
        return switch (n2) {
            case 32 -> CosmeticsView.xs("\u94e8\u57e4\u7e16\u111f\u267e\udf0e\ubfb7", -1366205714, 0x232E9450 ^ CryptoVault.mask());
            case 258 -> CosmeticsView.xs("\u2829\u8bf6\u35e1\u1daa\ua549\u8819", 89189829, 0x232E9450 ^ CryptoVault.mask());
            case 342 -> CosmeticsView.xs("\u0de3\uc2fa\uc874\uc4fd\u1027\ubb8b\uc6bd\uf14b\u6929\ue166\uae29", 1614914124, 0x232E9450 ^ CryptoVault.mask());
            case 346 -> CosmeticsView.xs("\udf72\u9c72\u211c\u2c03\u2b14\u6ba7\uf0a3\ued09\u223c\u4e4e\uba14\ua1dd", 720843224, 0x232E9450 ^ CryptoVault.mask());
            case 341 -> CosmeticsView.xs("\u9911\ua3a6\u57b7\u1b9c\u2d5d\u05a6\u851c\u4472\u90e3\uc4f3\ud72f", -1653406017, 0x232E9450 ^ CryptoVault.mask());
            case 345 -> CosmeticsView.xs("\ua046\uf1f6\u0a6a\u2cc7\u1638\ud39d\u841d\u14bb\u4fea\ufb01\ucdea", -2079202336, 0x232E9450 ^ CryptoVault.mask());
            case 340 -> CosmeticsView.xs("\ua212\ua091\u0a8b\ud1b4\u0587\u0654\uc8d5\u9e3d\u150d\u80df\ua164\ubc14\u0d23", 595567701, 0x232E9450 ^ CryptoVault.mask());
            case 344 -> CosmeticsView.xs("\u008d\u0084\u0e8c\udde5\ubace\u7b88\u822e\u8533\uaf52\u6f39\u730e\u3a08\ub9a7\u3802", -1013844533, 0x232E9450 ^ CryptoVault.mask());
            default -> n2 >= 290 && n2 <= 314 ? "F" + (n2 - 290 + 1) : CosmeticsView.xs("\uf241\u38c4\u6cf0\u17c5\udcea\u379d\u9c25", 812297512, 0x232E9450 ^ CryptoVault.mask()) + n2;
        };
    }

    private void pollInspectKey(c c2) {
        try {
            int n2;
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null || class_3102.method_22683() == null) {
                return;
            }
            long l2 = class_3102.method_22683().method_4490();
            if (GLFW.glfwGetKey((long)l2, (int)256) == 1) {
                this.capturingInspectKey = false;
                c2.A(c2.A().D(-1));
                return;
            }
            for (n2 = 2; n2 <= 7; ++n2) {
                if (GLFW.glfwGetMouseButton((long)l2, (int)n2) != 1) continue;
                this.capturingInspectKey = false;
                c2.A(c2.A().D(500 + n2));
                return;
            }
            for (n2 = 32; n2 <= 348; ++n2) {
                if (GLFW.glfwGetKey((long)l2, (int)n2) != 1) continue;
                this.capturingInspectKey = false;
                c2.A(c2.A().D(n2));
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private float optionTiles(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, List<String> list, String string, int[] nArray, Consumer<String> consumer) {
        return this.optionTiles(builder, uiHost, f2, f3, f4, f5, f6, f7, list, string, nArray, null, consumer);
    }

    private float optionTiles(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, List<String> list, String string, int[] nArray, String string2, Consumer<String> consumer) {
        int n2;
        int n3 = Math.max(3, (int)((f4 + 8.0f * f5) / (104.0f * f5)));
        float f8 = Math.round(8.0f * f5);
        float f9 = (f4 - f8 * (float)(n3 - 1)) / (float)n3;
        float f10 = Math.round(Math.min(f9 * 0.95f, 92.0f * f5));
        for (n2 = 0; n2 < list.size(); ++n2) {
            int n4;
            float f11 = f2 + (float)(n2 % n3) * (f9 + f8);
            float f12 = f3 + (float)(n2 / n3) * (f10 + f8);
            String string3 = list.get(n2);
            int n5 = n4 = nArray == null ? 12107980 : nArray[Math.min(n2, nArray.length - 1)];
            if (string2 != null) {
                this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, string3, string3.equals(string), null, 0, null, 0, string2 + ":" + n2, () -> consumer.accept(string3));
                continue;
            }
            this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, string3, string3.equals(string), null, n4, () -> consumer.accept(string3));
        }
        n2 = (list.size() + n3 - 1) / n3;
        return f3 + (float)n2 * (f10 + f8) + (float)Math.round(4.0f * f5);
    }

    private float drawWeapons(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        i i2 = c2.A();
        float f8 = f3;
        String string2 = this.backend.weaponCountLabel();
        if (!string2.isEmpty()) {
            float f9 = 9.5f * f5;
            for (String string3 : UiKit.wrap(string2, f4, f9, 3)) {
                builder.text(f2, f8, f9, uiHost.muted2Color(), string3);
                f8 += (float)Math.round(13.0f * f5);
            }
            f8 += (float)Math.round(8.0f * f5);
        }
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\u7d6e\ud286\uaaa0\u97b4\u9ebd\ue02d\ucce3\uc5e8\u254e\ub961\u09d5\udb2d\udc1d\u32c8\u5881\u0849\u9537\u00b3\uae9b\u981d\u6bb2", -1386431406, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\uda5c\u0bac\u40fa\ua604\uac4b\u2cd6\uf9c9\ua33f\u9e62\u8363\u0e9e\u1a90\udbda\u63a0\u46a4\u13c7\u4fa9\u0a70\u8ac3\u7511\u1876\u221b\ua2ac\u0e7e\u75a4\uddb4\u2614\u6220\ua805\u00f1\ufd6c\ua0b2\uc38c\ub30c\uebed\ud668\ue7bc\u0f54\u0f47\u5367\u8699\u2d1d\uaffe\ud781\u6aa1\ua5c3\u62f2\ua8f2\u41c4\uf308\u5205\ufc20\u0633\uaaf4\u30cd\u2d7b\ua452", -260743834, 0x232E9450 ^ CryptoVault.mask()), i2.I(), () -> c2.A(c2.A().A(!c2.A().I())));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\u8ee9\u648f\u9891\u7a78\u9362\u5224\uf4fa\u043f\u533c\ue9ba\u150f\u0a05\u74e3\uf42e\u53ea\u0094\uaa7c", 651307362, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\uad0f\udc88\ua27b\u3198\u2c56\u9118\u982a\u6067\ub59a\u6d2a\ue6b5\ucbe7\ua561\uf712\u5189\u7008\u636b\uefd0\u119c\ud930\u9a6d\ua972\ua7a1\ub5a9\u364d\u51d8\uf255\ub2fc\u79e5\u652e\u51f9\ua301\u34cd\u0d6c\uebf6\ufcc1\u4117\u6c59\u8e51\u0166\uec9d\ue40b\ucb3a\ucc8f\ufd6a\u75dd\ueb29\uc9e3\uea84\u51af\u1b34\u6e1c\u4da8\u0c19\u6686\ue30f\u3519\u2410\u01f3\u2130", 1039138174, 0x232E9450 ^ CryptoVault.mask()), i2.J(), () -> c2.A(c2.A().B(!c2.A().J())));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\uf9f5\ua69e\u933f\u9556\ueacf\u741e\u16f5\u02a9\ua3b8\u87e1\uf702\u757b\u7476\u495e\u2a2d", 1678572626, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\ue891\u0614\u8288\u7207\u7fa0\ue6aa\u8905\u4bfe\uaa29\uab3a\ub849\ud134\u3857\ub93b\u4595\u08f3\u93bf\u3542\uad04\u48e5\ub14a\uf6f1\ua4d2\uf034\u152b\u72cf\u6d64\u1ebd\u1ea4\u3c11\ub76a\u4f17\u3ef2\u4d18\u0f43\u3ea8\u1b18\ue407\uf70b\ufd27\ue484\ua832\u5216\u9600\ud5b4\u6e80\uc886\ub6d7\u4d8e\u15f6\u124f\ua60d\u7d99\u6a93\u79a3\u6381\u187c\u0d75\u527b\u5e08\u19a9\u0e72\ufab1\ub4e4\u9d2b\uac65\u61d7\ua6b1\u22af\u2530\ub2f0\u70c9\u13df\u3697\u16fb\ua9c7\u623b\ubac1\ueca3\ub84b\u4744\u347a\u00a0\uc0fe\ud58e\u356c\u1eb1\uf0e6\uea74\u83f7\u572a\u5885\u30d1\u39f2\u7771\uc473\u10a3", -1762394340, 0x232E9450 ^ CryptoVault.mask()), i2.K(), () -> c2.A(c2.A().C(!c2.A().K())));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ufc90\ubd2b\u6255\ud15c\uade1\u2744\ub1df\uc46a\u8413\u7113\u2ebf\u8037\uc0da", 1503749346, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u44d5\u68c5\ua327\u08fb\uf2ab\u47b3\u17a2\uaa29\ua5e4\u6924\u58a0\ubaaa\u7b72\uccc8\u2ac1\u3749\ud694\u8275\uc051\ue78d\u1860\u3bfd\ua397\ufb83\uf727\u49b2\u5da6\ue2a5\u3f81\uf13e\uff65\ud7fe\uc1c6\u070b\u7213\ub348\ub36d\u9dae\u9c9c\u5017\u9378\u442f\ue625\u7731\u7fca\u3554\ub03c\u3279\u33fc\ud203\ub1cf\u960d\u5622\u610b\u2706\ua843\u6c1c\u6d01\uf8d7\u339d\uad49\u79d0\u2456\u3ae1\u9fd0\u7440\u4ea9\ubdfe[\u3fca\u5f71\ud594\uf49d\u8a0b\ub85a\u9dcf\ud0a4", -1837698953, 0x232E9450 ^ CryptoVault.mask()), i2.L(), () -> c2.A(c2.A().D(!c2.A().L())));
        f8 += (float)Math.round(6.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u7d92\u24ff\u9f0e\u0f87\u23d1\u2bfb\u1981\ue087\ue7de\u8501\u46a5\uabc7\uc0c8\ub312", -1158174867, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.optionTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, h.zO, i2.K(), null, CosmeticsView.xs("\u2ecc\uda6d\u7a4b\u18c0\uab69\u78b0\u62d9\ue032\u8190", 905139318, 0x232E9450 ^ CryptoVault.mask()), string -> c2.A(c2.A().A((String)string)));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u3747\u24b4\u854f\u5448\u3fee\u5d73\uf591\u8394\u5eac\ue91c\ue51c", 1235377081, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.optionTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, h.pe, i2.L(), null, CosmeticsView.xs("\u7a5a\u9931\u4084\u2663\u1666\uaeaa", 598230274, 0x232E9450 ^ CryptoVault.mask()), string -> c2.A(c2.A().B((String)string)));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\ub660\udee2\uf9ac\uf482\u37ec\u811e\ud576\u9969\u9788\u47da", -1918321379, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.optionTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, h.pE, i2.p(), h.fb, string -> c2.A(c2.A().C((String)string)));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u8d5c\u13b7\u2173\u9e2b\u7c97\u6b56\u0bac\u91a7\u8b16\uf40b", -1161647440, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        ArrayList<String> arrayList = new ArrayList<String>(h.oB);
        arrayList.add(CosmeticsView.xs("\u00fc\u29c5\u733e\uba48\ua006\u44c6\uea23\uc689\u3474", 373560726, 0x232E9450 ^ CryptoVault.mask()));
        Object object = Arrays.copyOf(h.kR, arrayList.size());
        object[arrayList.size() - 1] = i2.M();
        f8 = this.optionTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, arrayList, i2.q(), (int[])object, string -> c2.A(c2.A().D((String)string)));
        if (CosmeticsView.xs("\uc38f\uc1d8\ud71b\u88e4\u5457\ud954\u3c6b\uba29\u60fe", -1150061450, 0x232E9450 ^ CryptoVault.mask()).equals(i2.q())) {
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\u8f07\u74b0\u95bd\ufe21\ub4af\ud924\u666a\u9169\u6a50\u8b19\ubbee\u9135\uf197\uf6b6\u0d26\u12e5", -1102689087, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, i2.M(), n2 -> c2.A(c2.A().A(n2, c2.A().N())));
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\ua3fe\ued84\u2c07\u2a6c\u915e\u8e15\u69bf\u8cd1\u0cf2\ua2b0\u9253\u9df6\uca1d\ubdc1\ub34d\u58d8\u0c61\u3cb2", 493743843, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, i2.N(), n2 -> c2.A(c2.A().A(c2.A().M(), n2)));
        }
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\u70b2\u6162\u8530\u7f47\ufadf", -2085596322, 0x232E9450 ^ CryptoVault.mask()), i2.O() + "%", () -> c2.A(c2.A().A(c2.A().O() - 5)), () -> c2.A(c2.A().A(c2.A().O() + 5)));
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\uad3d\u8b03\u6188\u8cdb\u5865\u3b33\u4a70\u2d45\ua5ac\uf692\uf639\ufa8f\u60cf\u7f36", 1892040446, 0x232E9450 ^ CryptoVault.mask()), Integer.toString(i2.P()), () -> c2.A(c2.A().B(c2.A().P() - (CosmeticsView.shiftDown() ? 100 : 1))), () -> c2.A(c2.A().B(c2.A().P() + (CosmeticsView.shiftDown() ? 100 : 1))));
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\uf081\u5fc5\u8d9a\u01ee\u51db\uf6c9", -1123466267, 0x232E9450 ^ CryptoVault.mask()), i2.Q() + "%", () -> c2.A(c2.A().C(c2.A().Q() - 5)), () -> c2.A(c2.A().C(c2.A().Q() + 5)));
        float f10 = Math.round(30.0f * f5);
        UiKit.button(builder, uiHost, CosmeticsView.xs("\u8925\u85a6\ue152\u9f41\u0ac4\ubf02\ud26c\ufe8b\uae18\ucd28", -1087406109, 0x232E9450 ^ CryptoVault.mask()), f2, f8, Math.min(f4, (float)Math.round(200.0f * f5)), f10, f5, UiKit.Style.PRIMARY, true, f6, f7, () -> {
            c2.A(c2.A().A(new Random()));
            this.say(CosmeticsView.xs("\u9201\u329c\u6fe5\uc890\ud717\ue55f\u5ecf\u1ddd\u68a4\ub3be\u2d17\u3805\u32ca\u980f\uc5e4\ua4ff\u3582\u57f5\u8c4d\u8e9e\ubfb0\u7009", -2090469948, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        f8 += f10 + (float)Math.round(14.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\uaad9\u6796\ud289\u55e2\uf3d7\uf673\u692d\u05fd\u6fdb\u1d76\ufafb\u2023\u12ad\u6d43\u735d\u42e4\u7b15\uac45", 1181464631, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.optionTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, h.ir, i2.M(), null, string -> c2.A(c2.A().E((String)string)));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\uefa5\u77d9\uf8b2\u90fc\u970b\u7ac7\u9df7\ua9a3\u058b\ua234\ubf94\u02cf", -1064608883, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        if (this.capturingInspectKey) {
            this.pollInspectKey(c2);
        }
        String string4 = this.capturingInspectKey ? CosmeticsView.xs("\u50af\u7107\uf6a1\u6635\u2ba3\ue44e\u429d\u08f2\ua158\u46a8\uaa53\ud97b\ub872\u7b22\uce50\u25da\u8566\ua088\u2749\u6a6f\u70ed\ub768\ue4cf\u9342\uaf69\u6ee2\uf6e9\u36b7\u11f9", 1536056336, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.keyName(i2.R());
        UiKit.button(builder, uiHost, string4, f2, f8, Math.min(f4, (float)Math.round(240.0f * f5)), f10, f5, this.capturingInspectKey ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, () -> {
            this.capturingInspectKey = !this.capturingInspectKey;
        });
        builder.text(f2, f8 += f10 + (float)Math.round(6.0f * f5), 9.5f * f5, uiHost.muted2Color(), UiKit.fit(CosmeticsView.xs("\u41f8\uf11b\ube65\u674d\u28c2\u9964\u9730\ue026\u5bd6\u96ca\u4ce2\u3b41\u8969\u4843\uefe6\ue011\u300f\ue0f1\ud6b1\uc829\ua368\u990f\u9397\u1f86\u2917\u9512\u62fb\uae69\u6d47\u0f89\u21ad\u4939\u065a\u2777\u989b\u1593\u6b98\u6976\udd0b\u641e\u95bb\ua76f\uc52e\u89de\u2113\uaa56\ub550\uf557\ube50\u1ea5\ue257\u62ab\uc436\u9e5b\u81b0\uad74\u3047\u5a41\ua9c0\ue2d0\uf5be\u5966\uba2e\u9487\uc3ab\u56cc\u3c84\uea51\u562e\ucbe6\uf89e\u0651\ub840\u661e\ufb64\uf563\u9734\u8a7d\u9e04\u3d3e\ue04b\u377a\uc834\u05c2\u533e\u4f57\u35db\ub1e1\ueaa8\u8c36\ub9b6\u87ce\u3d20\u74ef\u0d54\ua20b\u25be\u0777\uc71e\u4304\u88c8\uac53\u71e4\u105a\uda56\u1128\ua578\ucad2\u4556\ue526\u2cb7\u318e\uf266\u2567\ua8b6\u0eb3\u8820\u58d4\u00ef\ub19a\u16bc\u12bc\ufa98\ud6a1\uf5cc\u6236\u856b\u60bd\u8810\u7a83\u63d9\u03d5\u895c\u2976\u2823\ud8bc", 777462490, 0x232E9450 ^ CryptoVault.mask()), f4, 9.5f * f5));
        return f8 + (float)Math.round(24.0f * f5);
    }

    private float drawArmor(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        k k2 = c2.A();
        float f8 = this.toggleRow(builder, uiHost, f2, f3, f4, f5, CosmeticsView.xs("\ud3aa\uc903\ub60c\ua4ba\u4420\u9dd1\ua99c\u3967\u0f01\ua66d\u39e2", 1865208669, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u242e\u25f6\ub3e2\u03e4\u2571\u342b\ua85b\ub73a\u9369\uc995\u5e63\u5821\ud628\u2e7d\u95db\uad66\u64e5\ud092\u82f5\u1220\ubc43\u7834\u14d0\u25f6\u9bc8\u1cf2\u1919\uf529\ub68e\u1cdf\uc999\u8196\u7cfc\uf5db\ud5b9\u18de\u31c5\u021b\u7248\u3384\u4b57\u7cfa\u3f2c\u6fb4\u9eb0\uba79\uf5d7\u2047\ue479\u357a\u008f\uc4d6\u2cd1\u76b4\u9b7c\u949f\u9ef6\u5ec3\u6732\uaa4d\u0a06\ue87f\u1c24\u6cad\u90cb\u2368\uf91f\u02f2\u4928\u2b45\u2cff\u30e6\uf10a\u5bec\u12ae\u5420\u11b6\u85a3\u2522\u33a5\u7971\u0c3d\uea36\uc0b5\u79ce\ue46c\u6bfd\ubc10\ua7c6\ubc72\ue49f\ub08c\u5aa4\u1f52\ub1c0\u9d00\u4ac1\uc805\ubc89\uac6f\u5304\u5ecf\u45f9\u51b9\ue84e\u392a\uf411\u54ff\ue08e\uf340\ufe9b\uceb7\u44ef\u42e4\u0d92\u63dd\u7a0a\u3989\u1941\uf126\ubef7\u30d4\u8d10\u9a6c\u42bb\u409d\uc474\u1936\uc138\uf3ec\ue2f9\u43e6\u0a99", 1060031645, 0x232E9450 ^ CryptoVault.mask()), k2.j(), () -> c2.A(c2.A().a(!c2.A().j())));
        f8 += (float)Math.round(6.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u486a\u7216\uf396\u5e99\u12a6\u8f0b\u0603\u2f58\u243b\u73a9", 241221003, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.armorTiles(builder, uiHost, f2, f8, f4, f5, f6, f7, c2);
        j.a a2 = j.a(k2.g());
        if (a2 != null) {
            builder.text(f2, f8 - (float)Math.round(4.0f * f5), 9.5f * f5, uiHost.muted2Color(), UiKit.fit(a2.c() + ": " + a2.d() + CosmeticsView.xs("\u6404\u1844\ua5c4\uf207\u15da\u56ce\u77f7\udc31\u2d25\uac10\uc6c9\u2117\u939b\uec82\u0ee2\u6430\ua952\u06c4\u4828\u4f44\u5091\u7b69\u8ca9\u4c20\u920a\u530d\u84c4\uc3f7\uc15e\uac8a\u6ea2\u2ea6\ub8ed\u21b3\ud23e\u84c1\u7461\u60d2\u684c\u2b8d\ufe8b\ua97d", -1818403045, 0x232E9450 ^ CryptoVault.mask()), f4, 9.5f * f5));
            f8 += (float)Math.round(16.0f * f5);
        }
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u9b26\u2b78\u8417\ub8bf\ud607\u6502\u56da\u61cf\u11f6", -27748252, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        float f9 = Math.round(26.0f * f5);
        float f10 = Math.round(6.0f * f5);
        float f11 = f2;
        String[] stringArray = new String[]{CosmeticsView.xs("\ua363\u8956\uc995\u3c0e\uba96\u0580\u786a\u92e3\uf338", 1963455561, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u9719\u50ab\u9de8\u6d33\u10ad\u8952\u3bcd", -1015370669, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u64e2\u0e34\ua433\u4cf4\u7afb\u5e33\uae27\u7cfd", -1470192626, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\uf048\u3a9f\u75e6\uc85c\ud1d2\u76ad\uca8f\u9e12\ud8ff", -83787372, 0x232E9450 ^ CryptoVault.mask())};
        boolean[] blArray = new boolean[]{k2.k(), k2.l(), k2.m(), k2.n()};
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            int n3 = i2;
            float f12 = UiKit.buttonWidth(stringArray[i2], f5) - (float)Math.round(6.0f * f5);
            UiKit.button(builder, uiHost, stringArray[i2], f11, f8, f12, f9, f5, blArray[i2] ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, () -> c2.A(c2.A().a(a2 -> {
                switch (n3) {
                    case 0: {
                        a2.dZ = !a2.dZ;
                        break;
                    }
                    case 1: {
                        a2.ct = !a2.ct;
                        break;
                    }
                    case 2: {
                        a2.vJ = !a2.vJ;
                        break;
                    }
                    default: {
                        a2.bs = !a2.bs;
                    }
                }
            })));
            f11 += f12 + f10;
        }
        f8 += f9 + (float)Math.round(14.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u7def\u9537\u5eee\u650a\uec0e\u817a\ue5cf\u09fc\u49cd\ua554\ub2d8\u860e\u560c", -1857157936, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, k2.v(), n2 -> c2.A(c2.A().a(a2 -> {
            a2.sH = n2;
        })));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\uebcb\u2188\u8d17\ufee7\u6c77\ub42b\ub612\ud8a7\ufbc1\u04b4\u449b\uf612", 14928756, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, k2.w(), n2 -> c2.A(c2.A().a(a2 -> {
            a2.Lp = n2;
        })));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\uee5b\uf87a\u93f0\ube05\u14ce\u12f8\ub863\u7a5b\u5c8c\u3c0c\u01d1\uf352\u6f92", 755685174, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, k2.x(), n2 -> c2.A(c2.A().a(a2 -> {
            a2.pv = n2;
        })));
        f8 = this.stepper(builder, uiHost, f2, f8, f4, f5, f6, f7, CosmeticsView.xs("\u8183\u79ef\ue630\uaa40\u1e5f\uaed7\u71ce", 1743215723, 0x232E9450 ^ CryptoVault.mask()), k2.y() + "%", () -> c2.A(c2.A().a(a2 -> a2.JO -= 10)), () -> c2.A(c2.A().a(a2 -> a2.JO += 10)));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ua578\u2667\u28e3\uf75a\u874e\u6627\u98f0\u34a4\ua1d5\u2ead\ud8b0\uf799\u5576", 824425318, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u810c\u46a9\u1c6e\u9be2\u0925\u5d85\u0dd3\u77dd\u132b\ud7bb\ude6a\uc690\ue34e\u14cf\u2b03\u7faa\u577c\u20e0\u7015\u2787\u752e\u2fea\u9711\ub0cd\u7981\u48fb\ue6a0\u900f\u1204\uc5dd\uf304\u7047\ue07b\uba02\u7ef4\u941d\u4e25\u9e89\u30c3\ucdaa\u733a\uc18b\u4b94\u03b5\u4eab\u8a08\u9ba2\u85ca\u01db\u9088\uf047\ud34b\u36b5\ud87a\u5b7a\u7752\u2c8d\u82fd\ua7cb\ue163\u7f16\u5378\uab58\u89d5\ud896\u381a\u2b49\uadc1\u2023\ub9a0\udaa3\ubcb1", -1936197094, 0x232E9450 ^ CryptoVault.mask()), k2.o(), () -> c2.A(c2.A().a(a2 -> {
            a2.JF = !a2.JF;
        })));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\udb36\u437c\u6bf4\ua66c\u4e52\ub156\u79ad\u9da5\u07f3\u48ef", -1542182998, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u8e13\uad64\udd2d\u237e\u5362\ua448\uec6e\u4869\uaa50\u9ee5\u2ed6\u6858\ufafb\ucb09\uf66a\u00f1\u12e1\u7992\u001b\u7056\ufd02\u793a\u5392\u7a1f\ub78b\ubb66\u840b\ufeba\u3c70\u6176\u9778\uebfd\ua421\u4c42\u1ae9\uf459\u0fba\u6e65\ub4f6\uc11f\u4bf0\u5c51\ue0e8\uf831\ufc4d\ucfc7\ubc33\u38bb\uba89\u02a3\ub8e3\u2460\uf632\ubf4d\u32d5\uec70\u0abf", -1828153748, 0x232E9450 ^ CryptoVault.mask()), k2.p(), () -> c2.A(c2.A().a(a2 -> {
            a2.wC = !a2.wC;
        })));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ub4d1\u9117\u9a6a\ud467\u65f7\u62ad\u0896\uef81\u1084\u103c\ufd33\u3381\u8806\u7a52\u34ad", -1137437685, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u061a\u55ee\ue9be\u63ab\uaf52\u572c\u0efb\ue5dd\ucda5\ue1f1\u213b\ue593\ue94a\uc7ee\u2644\udab9\u4ffa\u7f64\u8e59\uc454\u74f1\ubf9c\uea5d\u1f46\u64fa\u4465\u2cc5\ude9a\u4332\u490c\u41bb\ucb24\uf6e3\ubc63\u3d4a\u29bb\ud466\u13dc\u860d\ucf62\ueb3a\u9305\u5f2e\ucced\u8d64\uc5a7\u6635\u51a3\u5008\uc45a\ufcf3\u1166\u7d21\u9b02\u1c5e\u582c\u2fc8\u99e1\uecfb\u41fc\u8349\u1f4d", -1363686486, 0x232E9450 ^ CryptoVault.mask()), k2.q(), () -> c2.A(c2.A().a(a2 -> {
            a2.go = !a2.go;
        })));
        f8 += (float)Math.round(6.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\ucb97\ubfa8\uc34f\u5c2e\ua8d3\ubb1f\ubf93\u757d\u6a5c\u93ee", -680461018, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.stringChoices(builder, uiHost, f2, f8, f4, f5, f6, f7, j.bx, k2.h(), string -> c2.A(c2.A().a(a2 -> {
            a2.LH = string;
        })));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\ue346\u7ccf\u0c65\u30d0\u41fc\u3b81\ufcbe", -2054867870, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.stringChoices(builder, uiHost, f2, f8, f4, f5, f6, f7, j.ED, k2.i(), string -> c2.A(c2.A().a(a2 -> {
            a2.hN = string;
        })));
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\uee67\u4574\u9efc\u2207\ub4df", -1169527673, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.stringChoices(builder, uiHost, f2, f8, f4, f5, f6, f7, j.by, k2.j(), string -> c2.A(c2.A().a(a2 -> {
            a2.Kq = string;
        })));
        builder.text(f2, f8 - (float)Math.round(6.0f * f5), 9.5f * f5, uiHost.muted2Color(), UiKit.fit(CosmeticsView.xs("\u201c\u9ccc\u2e01\u41fa\ued74\uf755\u4c16\u09fd\u4572\u87bf\ua2c2\u83dd\uf29d\ucd06\u9716\uee5e\u16ed\u5fe4\u390a\u4153\u7f6b\uf4d9\u03a0\ube61\ubc84\ue7f8\u3e9a\ueb40\ufc91\u83cf\u11a8\u1b65\u51cb\u0164\u95ec\u9098\u7be4\u697c\ue709\u2ec1\u9077\u7f0a\u5f0f\ubfcb\u6d9e\u2abb\u96d2\u3c4b\ua02e\uf039\u3fdc\u25f9\u98f3\u6164\udd19\uefc8\u91d6\u5b88\u1299\u00aa\ucc25\ub68c\ud3a3\u2f3f\u7c2f\ub647\u8752\u470f\u20f7\u886a\ub0b0\ueede\u5ddd\uffd0\u08e9\u5929\ua2cd\udf60\ubd16\u3a34\u190e\ucd46\u7722\ucbd3\u19ac\u706e\u9766\ua323\u663a\uc608\ucdf0\uddf7\u5894\u2fa9\ucd4c\u4769\ufd65\uc330\ua8dd\u6fa8\u2a80\u908a\ub5d1\u41e1\u0cbd\u0419\u7f8a\u879a\u3418\ucfcb\u0be1\u68a8", 1587709388, 0x232E9450 ^ CryptoVault.mask()), f4, 9.5f * f5));
        f8 += (float)Math.round(18.0f * f5);
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ue4a5\u60ce\u0eb1\u6c71\u9be5\ub0a8\u320c\ub4b8\ueae0\u3d2b\ucb50\u03a0\u9b26\u90b7\uab83\u4154\u385f\u3512\ua6d1\u7d8c\u1a29\u3371", 1557846124, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\ued39\ufc27\u948e\u977b\ufe22\u8fb9\u5cae\ueaa8\uc0b7\uce05\u38a2\u89ce\u96f2\ue1f2\uf9ba\ud6c9\u21d7\u78b4\u53e6\u5efa\u4329\u0e79\u9d15\u4a18\u9557\ud8df\u785a\uaad6\u78f0\u434f\u53ed\udf77\u9c18\u361b\ueb40\u50b6\ubb2e\u9112\u557d\u4daf\u2d6c\u62aa\ub740\u0a66\ude2c\u101c\uc47c\u3a9e\ue40c\uce8d\ueef1\u7f7d\u81a1\uea3a\uba42\u8941\uf25f\u7058\ua988\u9976\u8764\u41e8\ubd62\uda0d", 1934771055, 0x232E9450 ^ CryptoVault.mask()), k2.r(), () -> c2.A(c2.A().a(a2 -> {
            a2.ju = !a2.ju;
        })));
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\u5d3c\u9ddb\u24cf\u8d10\u283b\ucb2c\u07f7\u4913\uc37d\ubbd0\uc962\u6e42\u259d\u190d\u7814\uecbc\ufaf7\u5087\u5e76", 1725534148, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\uef3d\uf11e\ube1e\u98a0\u4165\ufc46\ub782\uc8f7\u4471\u0e97\u7992\u5ba1\u9390\ue5e1\u9735\u41a1\u23f5\u32bd\u7320\ua0ab\uf7ad\ufe96\u7527\u2483\u38bd\u0f4d\u4dd6\u15a3\u98fd\u742a\u2ba1\ud1e5\ud366\u9530\ue67b\u10a8\ub41f\u8c96\u1ec9\ua0b6\ubdd3\u92b5\ue509\uffe4\u8277\u2a3c\ua634\ufd17\u2c84\u13fc\u10c3\u822f\u7992\uf761\u38fa\u3625\u99e8\u65e6\u638e\u5bef\ud047\u4b2f\u2083\uf774\u573a\u91d3\ua0d1\u7831\ued7d\u99a6\uaff3\ufc4c\ucb2f\u5a5b\ud0c6\u9562\ufb33\ud1dd\udb46\u03b1\u3a40\u549c\u21f6\u736c", -849794714, 0x232E9450 ^ CryptoVault.mask()), k2.s(), () -> c2.A(c2.A().a(a2 -> {
            a2.ti = !a2.ti;
        })));
        return f8 + (float)Math.round(14.0f * f5);
    }

    private float armorTiles(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, c c2) {
        int n2;
        List<j.a> list = j.vP;
        int n3 = Math.max(2, (int)((f4 + 8.0f * f5) / (118.0f * f5)));
        float f8 = Math.round(8.0f * f5);
        float f9 = (f4 - f8 * (float)(n3 - 1)) / (float)n3;
        float f10 = Math.round(f9 * 1.5f);
        String string = c2.A().j() ? c2.A().g() : null;
        for (n2 = 0; n2 < list.size(); ++n2) {
            float f11 = f2 + (float)(n2 % n3) * (f9 + f8);
            float f12 = f3 + (float)(n2 / n3) * (f10 + f8);
            j.a a2 = list.get(n2);
            this.tile(builder, uiHost, f11, f12, f9, f10, f5, f6, f7, a2.c(), a2.c().equals(string), null, a2.s(), null, 0, CosmeticsView.xs("\ud1a2\ue292\u8a30\u77f8\u062f\ud09a\u2494\u1ed8", -1978425626, 0x232E9450 ^ CryptoVault.mask()) + a2.c(), () -> c2.A(c2.A().b(a2.c())));
        }
        n2 = (list.size() + n3 - 1) / n3;
        return f3 + (float)n2 * (f10 + f8) + (float)Math.round(4.0f * f5);
    }

    private float stringChoices(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, List<String> list, String string, Consumer<String> consumer) {
        float f8 = Math.round(26.0f * f5);
        float f9 = Math.round(6.0f * f5);
        float f10 = f2;
        float f11 = f3;
        for (String string2 : list) {
            float f12 = UiKit.buttonWidth(string2, f5) - (float)Math.round(6.0f * f5);
            if (f10 + f12 > f2 + f4 && f10 > f2) {
                f10 = f2;
                f11 += f8 + f9;
            }
            UiKit.button(builder, uiHost, string2, f10, f11, f12, f8, f5, string2.equals(string) ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, () -> consumer.accept(string2));
            f10 += f12 + f9;
        }
        return f11 + f8 + (float)Math.round(12.0f * f5);
    }

    private float drawMaker(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        a a2 = c2.A();
        if (!this.textField.focused(uiHost) && !this.textField.value().equals(a2.A())) {
            this.textField.setValue(a2.A());
        }
        float f8 = f3;
        float f9 = Math.round(150.0f * f5);
        float f10 = f9 * 10.0f / 16.0f;
        builder.shadow(f2, f8, f10, f9, Math.round(4.0f * f5), Math.round(14.0f * f5), 0.0f, 0x70000000);
        class_2960 class_29602 = this.backend.capeTexture(a2);
        if (class_29602 != null) {
            builder.imagePixelated(f2, f8, f10, f9, Math.round(4.0f * f5), -1, class_29602, 0.015625f, 0.03125f, 0.171875f, 0.53125f);
        } else {
            builder.gradient(f2, f8, f10, f9, Math.round(4.0f * f5), a2.z(), a2.z(), a2.A(), a2.A());
        }
        float f11 = f2 + f10 + (float)Math.round(18.0f * f5);
        float f12 = Math.min(f4 - f10 - 18.0f * f5, (float)Math.round(220.0f * f5));
        float f13 = Math.round(30.0f * f5);
        float f14 = Math.round(8.0f * f5);
        boolean bl = c2.A().B() != null && CosmeticsView.xs("\uc425\u6730\u28ec\u036b\u8bcc\u190f\u37e8\u1340\ue354\u6b16", -1518288346, 0x232E9450 ^ CryptoVault.mask()).equals(c2.A().B().n());
        UiKit.button(builder, uiHost, bl ? CosmeticsView.xs("\ue19d\ubac2\u3572\u4546\ub553\ue9e7\u1d5e\u1c04\ufd49\u2fff\u3267\u7cac\u6c01\uf016\u0614\u1406\u0003\uf207\uc0cd\u9949", -1583450011, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.xs("\u94f9\u845b\udc12\u59ff\uc227\ud584\ub598\u3d07\ua73b\u6602\uf1dd\u633b\u1212\u5c32\ue3dc", 425117024, 0x232E9450 ^ CryptoVault.mask()), f11, f8, f12, f13, f5, UiKit.Style.PRIMARY, !bl, f6, f7, () -> {
            c2.A(c2.A().A(c2.A().a(CosmeticsView.xs("\u8385\uc11c\u5ca6\uc6cf\u4dca\u9c74\u94bd", -1299809859, 0x232E9450 ^ CryptoVault.mask()))));
            this.say(CosmeticsView.xs("\u02fd\ud21a\u12a5\u1781\u8d87\uddf0\u4390\uc09d\u4c1b\u49cf\udb94\u494d\u86ea\u2c51\u1aee\uc9c7\uf8a7\u2d07\u0262\ubb68\u7408\u4ee4\u3999\u3028\uf812\u2c67\ud510\uac46", 1306092054, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\u9db8\uf73b\u9074\u9d63\uf63c\u8d0b\uc63f\ub93d\u1694\u27b1\uc99b\ucbaa\u1dbd\u1e70\u08f0\u0595\ud2ac\ud214\u4f21", -655420008, 0x232E9450 ^ CryptoVault.mask()), f11, f8 + f13 + f14, f12, f13, f5, UiKit.Style.SECONDARY, true, f6, f7, () -> {
            this.say(CosmeticsView.xs("\ubac2\uccc1\u1b9f\ue7e7\uf756\u7f72\u63e6\ud8ee\u17c3\u43aa\u0b98\u25dc\ue8d8\ua366\u8e47\ubc09\ue9a1\u02af\u6acb\uc17f\u2844\u43a5\u38c0\ub83c\u6c07\uf2b1\u4f26\u56da\u4c6d\u5897\u2f11\ua215\u69f5\u3269\u734c\u996d\ueb13\ubff1\ub007\uac4f\u4adb\u49fe\u64cd\u48f3\u21a7\u46e7\u68a0\u6ab0\u3590\u332a\u9d7c\u5f83\u358f\u561c\ube4b\u2df7\u6c6c\u82d9\u17c0\uda5e\u48a9\u799f\u7c33\u934e\uc3cf\u631f\u201e\u6169\uf46f\u7087\u09e0\u46e0\uea62\u1f93\uacbe\u2565\u0514\uf468\u5cf3\ub04b\u5d3a\ucb0e\u6e8c\u2782\ud877", -508192764, 0x232E9450 ^ CryptoVault.mask()), uiHost);
            this.backend.importImage(string -> {
                this.applyCustom(c2.A().b((String)string));
                this.say(CosmeticsView.xs("\u94ad\u618d\ueda4\uab3d\u7c6b\u6392\uebb0\ud420\u5364\ua4e6\u0877\uc647\u7136\ue5ab\udaea\u33a2\ufeed", 1061045265, 0x232E9450 ^ CryptoVault.mask()) + string.length() + CosmeticsView.xs("\u4ec9\u8b33\u6736\u09e7\u5413\ub7ce\u4af7\u895d\ub712\ub569\uc136\ub64b\u66fe\ub267\uc692\u31f3\u2b7b\u95df", 1843108917, 0x232E9450 ^ CryptoVault.mask()), uiHost);
            }, string -> this.say((String)string, uiHost));
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\u6abb\u09e5\u2d04\u7fe3\ue6c2\ub983\u280b\u81a2\u455e\uf781\ua557\ued6f\u3cbf", 586453650, 0x232E9450 ^ CryptoVault.mask()), f11, f8 + (f13 + f14) * 2.0f, f12, f13, f5, UiKit.Style.SECONDARY, a2.B() != null, f6, f7, () -> {
            this.applyCustom(c2.A().b(null));
            this.say(CosmeticsView.xs("\u5a10\ufabd\u7de2\u911b\u3c75\u7c1f\u0516\ub1e3\u1325\u28e5\u63e4\u44b0\u52b2\u0c64\u7a67", 688341276, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\u81c6\uac06\ua763\ufd6a\ud43d\u8c3e\uddcb\udafb\u97bb\ueacc\u92ed\u64ca\u8aa0\u9c83\ud6cc\u8d5d", 1459239785, 0x232E9450 ^ CryptoVault.mask()), f11, f8 + (f13 + f14) * 3.0f, f12, f13, f5, UiKit.Style.GHOST, true, f6, f7, () -> {
            this.applyCustom(a.b());
            this.textField.setValue("");
            this.say(CosmeticsView.xs("\u4091\u3701\uaf76\u23d0\u5bb7\u5c3b\ud461\u9b08\u476e\u6e86\u3bdb\u832d\u44c4\ub68d", 774964868, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        f8 += Math.max(f9, (f13 + f14) * 4.0f) + (float)Math.round(16.0f * f5);
        if (a2.B() != null) {
            builder.text(f2, f8, 10.0f * f5, uiHost.muted2Color(), UiKit.fit(CosmeticsView.xs("\uc04d\uf9c5\u2e46\u211c\u3cfa\uc7c1\u3e85\u01a7\ubdae\ue7cf\ubb56\ud0d3\ue953\udb6b\u9373\u688a\u33bb\u4dd3\u1d0c\udf3a\u73ee\ue66a\u2bc3\u7519\u53f0\u0242\ud087\u326f\u1890\uf9e0\u8d85\udfd6\uaf4d\u94d7\ue336\u414f\u53de\uc5ef\ud2cf\u187f\u53b8\uce50\u9e5b\udce6\u19c2\uafde\uf66e\u89b2\u3fa5\ua726\u14bc\u3cc7\ub494\u173d\u2bd7\uf701\u1393\u3abf\ue77b\u7bc1\u27ef\uffc3\uffaf\u70a1\u49d1\u5044\u6f5b\ua938\u29ef\u25c5\ua649\u2e69\u9774\u1da0\u1201\u5692\u755c\uf856\u0256\u1b23\ud28d\u05b1\uce6a\u1ded\uaa31\u172f\u141d\u27f1\u3809\u162f\u4e5a\u2eb2\ua824\ubcd2\u47b9\u3e6e\u332b", -1288923889, 0x232E9450 ^ CryptoVault.mask()), f4, 10.0f * f5));
            f8 += (float)Math.round(22.0f * f5);
        } else {
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\u68e9\u42ad\u2830\u95c1\udcd0\u87f3\u7a2b\u1114\ude71\ued80\u51c1\u6cbe\uc8f3\ud95f", 177476350, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.z() & 0xFFFFFF, n2 -> this.applyCustom(c2.A().a(n2, this.d2().A(), this.d2().B(), this.d2().C(), this.d2().D(), this.d2().E())));
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\uebfe\u885d\u6c08\ua35c\u031a\u46da\ufe68\u3ff9\ud1bc\ufa7b\u54d7\u7910\ud219\u5caf\u4805\u32d3\u55cc", -243643610, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.A() & 0xFFFFFF, n2 -> this.applyCustom(c2.A().a(this.d2().z(), n2, this.d2().B(), this.d2().C(), this.d2().D(), this.d2().E())));
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\uacb6\u959f\udb9e\ue77d\u9598\ue7e0\u979a\u4680\uf477\u85f6\ub222\ua06f", -819584390, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.choices(builder, uiHost, f2, f8, f4, f5, f6, f7, a.c.values(), a2.A().ordinal(), n2 -> this.applyCustom(this.d2().a(a.c.values()[n2], this.d2().A(), this.d2().A(), this.d2().A())));
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\uf280\u1061\u96a2\uc513\uc665\ud522\u3e9c\u7a8d\u919b\u7f57", -151881776, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.choices(builder, uiHost, f2, f8, f4, f5, f6, f7, a.d.values(), a2.A().ordinal(), n2 -> this.applyCustom(this.d2().a(this.d2().A(), a.d.values()[n2], this.d2().A(), this.d2().A())));
            if (a2.A() != a.d.ky) {
                f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.B() & 0xFFFFFF, n2 -> this.applyCustom(this.d2().a(this.d2().z(), this.d2().A(), n2, this.d2().C(), this.d2().D(), this.d2().E())));
            }
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\ubc13\u5716\u7635\u3a90\u8a47\u8a6e\u97b5\u922d\u311a\ubf38\ud260", 1442407382, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            float f15 = Math.round(30.0f * f5);
            this.textField.draw(builder, uiHost, f2, f8, Math.min(f4, (float)Math.round(220.0f * f5)), f15, f5);
            f8 += f15 + (float)Math.round(8.0f * f5);
            if (!a2.A().isEmpty()) {
                f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.C() & 0xFFFFFF, n2 -> this.applyCustom(this.d2().a(this.d2().z(), this.d2().A(), this.d2().B(), n2, this.d2().D(), this.d2().E())));
            }
            f8 = this.section(builder, uiHost, CosmeticsView.xs("\u2744\u04ee\ud82a\ua456\u6f7b\ufc9e\u896c\u8233\u65ab\u46bd", 86353351, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
            f8 = this.choices(builder, uiHost, f2, f8, f4, f5, f6, f7, a.b.values(), a2.A().ordinal(), n2 -> this.applyCustom(this.d2().a(this.d2().A(), this.d2().A(), a.b.values()[n2], this.d2().A())));
            if (a2.A() != a.b.ds) {
                f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.D() & 0xFFFFFF, n2 -> this.applyCustom(this.d2().a(this.d2().z(), this.d2().A(), this.d2().B(), this.d2().C(), n2, this.d2().E())));
            }
        }
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\ub711\ua846\u73aa\u0117\u2a17\ud654\u528c\uff53\u3a39", 1813649017, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.choices(builder, uiHost, f2, f8, f4, f5, f6, f7, a.a.values(), a2.A().ordinal(), n2 -> this.applyCustom(this.d2().a(this.d2().A(), this.d2().A(), this.d2().A(), a.a.values()[n2])));
        if (a2.A() != a.a.GJ) {
            f8 = this.swatches(builder, uiHost, f2, f8, f4, f5, f6, f7, false, a2.E() & 0xFFFFFF, n2 -> this.applyCustom(this.d2().a(this.d2().z(), this.d2().A(), this.d2().B(), this.d2().C(), this.d2().D(), n2)));
        }
        return f8;
    }

    private a d2() {
        return this.backend.prefs().A();
    }

    private void applyCustom(a a2) {
        c c2 = this.backend.prefs();
        a a3 = a2.a(CosmeticsView.xs("\u3ded\u37a6\ua6f2\u75bd\u129b\ua97e\u1493\u8d28\udb14\u69e6", -1352408683, 0x232E9450 ^ CryptoVault.mask()));
        c2.A(a3);
        d d2 = c2.A();
        if (d2.B() != null && CosmeticsView.xs("\ub952\u80d2\u9218\u2f76\u454e\ua891\ub93a\u5a11\u8562", -1355686777, 0x232E9450 ^ CryptoVault.mask()).equals(d2.B().n())) {
            c2.A(d2.A(a3));
        }
    }

    private float section(UiDisplayList.Builder builder, UiHost uiHost, String string, float f2, float f3, float f4) {
        UiKit.label(builder, uiHost, string, f2, f3, f4);
        return f3 + (float)Math.round(18.0f * f4);
    }

    private float choices(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, Enum<?>[] enumArray, int n2, IntConsumer intConsumer) {
        float f8 = Math.round(26.0f * f5);
        float f9 = Math.round(6.0f * f5);
        float f10 = f2;
        float f11 = f3;
        for (int i2 = 0; i2 < enumArray.length; ++i2) {
            String string = CosmeticsView.pretty(enumArray[i2].name());
            float f12 = UiKit.buttonWidth(string, f5) - (float)Math.round(6.0f * f5);
            if (f10 + f12 > f2 + f4 && f10 > f2) {
                f10 = f2;
                f11 += f8 + f9;
            }
            int n3 = i2;
            UiKit.button(builder, uiHost, string, f10, f11, f12, f8, f5, i2 == n2 ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, () -> intConsumer.accept(n3));
            f10 += f12 + f9;
        }
        return f11 + f8 + (float)Math.round(12.0f * f5);
    }

    private static String pretty(String string) {
        String string2 = string.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(string2.charAt(0)) + string2.substring(1);
    }

    private float swatches(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, boolean bl, int n3, IntConsumer intConsumer) {
        int n4;
        boolean bl2 = this.openPicker == (n4 = this.swatchSeq++);
        int n5 = this.backend.accentRgb() & 0xFFFFFF;
        boolean bl3 = bl && n3 == -2;
        int n6 = bl3 || n3 < 0 ? n5 : n3 & 0xFFFFFF;
        float f8 = Math.round(28.0f * f5);
        float f9 = f8 * 0.5f * Math.min(1.0f, uiHost.cornerScale());
        String string = bl3 ? CosmeticsView.xs("\u8a4b\u50d3\u94fe\ud717\ud975\u4470\u7312\u8cd0\u9e52\u3317\uc573\u4e61\u8637", -616410878, 0x232E9450 ^ CryptoVault.mask()) : String.format(Locale.ROOT, CosmeticsView.xs("\u9a28\ub27a\uebf3\ue82c\uc495\u101a\u0ed6\u275d", -1488354933, 0x232E9450 ^ CryptoVault.mask()), n6);
        float f10 = 10.5f * f5;
        float f11 = Math.min(f4, (float)Math.round(f8 + 14.0f * f5 + GpuUiFontMetrics.textWidth(string, f10, UiDrawCommand.TextWeight.SEMIBOLD) + 30.0f * f5));
        boolean bl4 = uiHost.hovered(f6, f7, f2, f3, f11, f8);
        builder.rect(f2, f3, f11, f8, f9, uiHost.controlFill(bl4 || bl2 ? 48 : 28));
        if (bl2) {
            builder.border(f2, f3, f11, f8, f9, Math.max(1.0f, f5), UiKit.withAlpha(uiHost.accent(), 176));
        }
        float f12 = f8 - (float)Math.round(8.0f * f5);
        builder.circle(f2 + f8 * 0.5f, f3 + f8 * 0.5f, f12 * 0.5f, 0xFF000000 | n6);
        builder.text(f2 + f8 + (float)Math.round(4.0f * f5), GpuUiFontMetrics.topInBox(f3, f8, f10), f10, uiHost.textColor(), string, UiDrawCommand.TextWeight.SEMIBOLD);
        UiIcons.draw(builder, bl2 ? UiIcons.Icon.CHEVRON_DOWN : UiIcons.Icon.CHEVRON_RIGHT, Math.round(f2 + f11 - 16.0f * f5), Math.round(f3 + (f8 - 10.0f * f5) * 0.5f), Math.round(10.0f * f5), uiHost.mutedColor());
        uiHost.hit(f2, f3, f11, f8, () -> {
            if (this.openPicker == n4) {
                this.openPicker = -1;
                return;
            }
            this.openPicker = n4;
            this.picker.reset();
        });
        if (bl) {
            String string2 = CosmeticsView.xs("\ufb9b\u6e51\u467a\u7a64\uc4e2\u364d\u7366\uf06f\ub0f1\ucaa7\u0e74", 1050601281, 0x232E9450 ^ CryptoVault.mask());
            float f13 = UiKit.buttonWidth(string2, f5 * 0.9f);
            float f14 = f2 + f11 + (float)Math.round(8.0f * f5);
            if (f14 + f13 <= f2 + f4) {
                UiKit.button(builder, uiHost, string2, f14, f3, f13, f8, f5 * 0.9f, bl3 ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, () -> intConsumer.accept(-1));
            }
        }
        float f15 = f3 + f8 + (float)Math.round(12.0f * f5);
        if (!bl2) {
            return f15;
        }
        return this.picker.draw(builder, uiHost, f2, f15, f4, f5, f6, f7, 0xFF000000 | n6, false, n2 -> intConsumer.accept(n2 & 0xFFFFFF));
    }

    private float stepper(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, String string, String string2, Runnable runnable, Runnable runnable2) {
        float f8 = Math.round(28.0f * f5);
        builder.text(f2, GpuUiFontMetrics.topInBox(f3, f8, 11.0f * f5), 11.0f * f5, uiHost.textColor(), string, UiDrawCommand.TextWeight.SEMIBOLD);
        float f9 = Math.round(32.0f * f5);
        float f10 = f2 + (float)Math.round(120.0f * f5);
        UiKit.button(builder, uiHost, "-", f10, f3, f9, f8, f5, UiKit.Style.SECONDARY, true, f6, f7, runnable);
        float f11 = 10.5f * f5;
        float f12 = Math.round(56.0f * f5);
        builder.text(f10 + f9 + (f12 - GpuUiFontMetrics.textWidth(string2, f11)) * 0.5f, GpuUiFontMetrics.topInBox(f3, f8, f11), f11, uiHost.textColor(), string2);
        UiKit.button(builder, uiHost, "+", f10 + f9 + f12, f3, f9, f8, f5, UiKit.Style.SECONDARY, true, f6, f7, runnable2);
        return f3 + f8 + (float)Math.round(10.0f * f5);
    }

    private float toggleRow(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, String string, String string2, boolean bl, Runnable runnable) {
        float f6 = Math.round(14.0f * f5);
        float f7 = f4 - f6 * 3.0f - 34.0f * f5;
        List<String> list = UiKit.wrap(string2, f7, 9.5f * f5, 5);
        float f8 = (float)(Math.round(22.0f * f5) + list.size() * Math.round(13.0f * f5)) + f6;
        UiKit.card(builder, uiHost, f2, f3, f4, f8, f5);
        builder.text(f2 + f6, f3 + (float)Math.round(10.0f * f5), 11.0f * f5, uiHost.textColor(), UiKit.fit(string, f7, 11.0f * f5), UiDrawCommand.TextWeight.SEMIBOLD);
        float f9 = f3 + (float)Math.round(27.0f * f5);
        for (String string3 : list) {
            builder.text(f2 + f6, f9, 9.5f * f5, uiHost.muted2Color(), string3);
            f9 += (float)Math.round(13.0f * f5);
        }
        UiKit.toggle(builder, uiHost, f2 + f4 - f6 - 34.0f * f5, f3 + (float)Math.round(10.0f * f5), f5, bl, runnable);
        return f3 + f8 + (float)Math.round(8.0f * f5);
    }

    private float drawSettings(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = this.section(builder, uiHost, CosmeticsView.xs("\u3325\ua312\u9439\u47d0\u290e\u9500\u8a63\u3413\uc9eb\u1551\ue970\u07c1\ud081\u13a8\ub81f", -1260540188, 0x232E9450 ^ CryptoVault.mask()), f2, f3, f5);
        c.a[] aArray = c.a.values();
        String[] stringArray = new String[aArray.length];
        for (int i2 = 0; i2 < aArray.length; ++i2) {
            stringArray[i2] = aArray[i2].mw;
        }
        float f9 = Math.round(30.0f * f5);
        UiKit.segmented(builder, uiHost, stringArray, c2.A().ordinal(), f2, f8, Math.min(f4, (float)Math.round(360.0f * f5)), f9, f5, f6, f7, n2 -> c2.A(aArray[n2]));
        f8 += f9 + (float)Math.round(8.0f * f5);
        String string = switch (c2.A()) {
            default -> throw new MatchException(null, null);
            case c.a.fs -> CosmeticsView.xs("\u24f2\udc44\u3cec\u80a2\u4afb\uc9d5\ub9f5\u8833\ub195\u75ad\ua0e6\uae8a\ubf40\u8b7b\ud037\u16cf\u14dd\u3f64\u299f\uc3e5\u6ad2\ufdd8\uded0\u9e3b\ud7fa\u3aed\u78f3\uc154\ud003\u4277\u7a6c\u14f3\u0f7b\ue8d6\uda0c\u6495\ua220\ua2a0\u0a90\u9723\u4505\ubfe2\u97d3\u8d0a\uc3cc\uf699\u5630\uf03b\uc06b\u6b97\u47db\uf00b\u6718\u7070\u549e\u917a\u8dc0\u74a8\ud864\u92ad\u2651\u667c\u8834\u3ad4\ufcbc\u6be8\u37fc\u68c4\uab34\u476e\u7a07\udad7\uea11\u7489\uc053\ubed0\u432f\ued29\uceba\u7c68\u48bc\u0e83\uc942\u0d24\u7d9a\u430b\u3f9a\u0d8d\u65fb\u28e0\u8f79\ucf85", -1339149955, 0x232E9450 ^ CryptoVault.mask());
            case c.a.X -> CosmeticsView.xs("\u7406\u9673\u2b03\u0bdc\u081e\u69c5\u37e2\ue9bb\ufbf0\u4be1\udbb6\u7364\u7c13\u9341\u69a2\ubf68\uc45c\u02dc\uc279\u0219\u8007\u1bfc\u3da1\uf60e\ua2dc\u6363\u49ae\ud024\u8af0\ud779\uab16\ue70a\ufacd\uc181\ue351\udafa\ufa90\ua0f4\u01d2\u8a27\u8094\u467e\u0e6e\ucb57\u4490\u75c9", -963373415, 0x232E9450 ^ CryptoVault.mask());
            case c.a.jo -> CosmeticsView.xs("\u8aa0\u3dd0\uf97e\ue1aa\uc277\u6af8{\udfef\u7537\u285b\u5b70\u5409\u86f6\uf530\uf2f0\ue440\u1b62\ub144\ufcfa\uf6eb\u1c09\uc6bd\ub289\uebec\uaebf\u248f\udc95\u0d3e\u8ffc\ud4e4\u38ac\u00bf\ucc58\u5d61\u8a41\uc27a\uba95\ue779\u8e93\u8661\u8e44\u4bc3\u5776\u5bd7\u4b9f\u4f1f\ue5fd\u691f\ufc99\uad43\ud2d9\u494b\u3527\uf12e\u28f3\ua510\u3c4e\u054c\ua4cc\u2300\ua174\u3377\u8593\u5a5c\u8332\u30e4\u8af1", 1528418601, 0x232E9450 ^ CryptoVault.mask());
        };
        for (String object2 : UiKit.wrap(string, f4, 9.5f * f5, 3)) {
            builder.text(f2, f8, 9.5f * f5, uiHost.muted2Color(), object2);
            f8 += (float)Math.round(13.0f * f5);
        }
        f8 += (float)Math.round(10.0f * f5);
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\u3c67\u23b2\ub742\u1b19\u0d9a\u215e\uba35\u1a06\u0164\ua979\u671f\uffd7\ubc32\u9183\u51bf\u8d88\ua9df\u70f1\u9593\u7ec1\u2f76\udb9e\u04ce\u76f2\ue97c\u1df1\u8deb\u7a7d\ua849\ua837\u3ba6", -705446170, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\ua670\ubf2f\ub245\u8db1\u9de7\ufd4e\ub501\uce5a\u9549\u06a1\ue475\u287c\uf895\u2511\u8409\ud208\ucd85\u4694\ue5c9\ufdaf\u814e\uaa18\u5aca\ua1a2\u2190\u536c\u5c09\u2e28\u516a\uecbe\uaad7\u30ea\ued21\ud638\u933a\ua82e\u70a1\u1267\u42fa\u34ff\u4a5e\ue2cb\u6713\u5d3e\uaf38\u45c9\u90ac\uf175\u2522\u2076\ufed0\u8e36\u23a0\u26d7\u252b\u0097\uea4b\ua22b\u752c\udecd\uffdc\u5e1a\u7ab8\uf878\u3690\uefbd\ub3fe\u951a\uc759\u200c\ua4eb\u3825\u55ca\u98da\ue6a6\u67b6\u0c4c\uaa67\u1f8f\ubded\ua25c\ub0d7\u1152\u3e77\u0bef\u6e3b\udea4\u3460\u4a2b\u3b70\u1dbc\ub4d6\uc2a5\u09f7\u3bb5\u0a26\u6947\u0ac6\u5a73\u47a9\u8e7f\uc4df\uba9e\u6bc7\uc5b3\ua238\uc335\ud4b8\u6977", 85539272, 0x232E9450 ^ CryptoVault.mask()), c2.A(), () -> c2.A(!c2.A()));
        f8 += (float)Math.round(6.0f * f5);
        f8 = this.section(builder, uiHost, CosmeticsView.xs("\u7553\u5123\u76df\u3257\u2d73\u61ce\u36c8\u5d33\ue851\u9438", 1310446689, 0x232E9450 ^ CryptoVault.mask()), f2, f8, f5);
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\ud2cc\u10cd\u1a74\u52ad\u540a\u2921\uce14\udaec\u6dae\ud062\ua53f\u144b\uf765\uc0cd\uc342\ue77a\uff04\u2cb4\u8e2e\ua083\ue6ac\u1a97\ua87a\u39cd\ue8b4\u8dec\u5f50\u6323\uad7e\ub54f\u1f38\ubb9f\u9cc8\u5c77\uba19\u35e8\u4827\u51b2\ua756", -1563044632, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\ud10b\uc228\u9782\uff45\u7a22\ufad9\u571d\u4529\u42f0\u13d8\u9649\u5267\u3cad\ua515\u3855\ue4c7\u78fd\u8e35\u0603\u27ff\u689c\u711c\u1b3d\u3c58\ufeae\ud76b\u39a5\ue0ad\uc9a3\uc6cb\u06dc\u321b\u139e\u0a9d\ua334\u3673\u80ca\u85e6\u54d4\u5a2f\u83f4\ue469\ucc95\u1adf\u07ef\ua3b5\u1c9b\u7713\ud69c\u5717\u4662\u0b9b\u979c\u9445\u579a\u0ea0\u6077\u7fe8\u6dba\u620c\u0485\u34db\u3c32\u7d2c\u8f27\u9a5d\u3f90\u2806\u9bd0\u9c5d\ud79f\u6b45\u1f62\u326d\u24be\u71ef\ucffa\u36ae\uc46d\ub7f9\ub58d\u6e5d\u2894\u9391\u746b\ud747\u4db9\u92cb\u1b41\u46b1\u53e2\uc5b0\u8d06\u438a\u1738\ufe7d\u20d0\u7fc9\u83cd\u1e7d\ufc64\u401b\u5bc7\u0ea9\u7d85\uaf15\uc172\ucad0\u0bc8\uef4c\u3f83\u97f3\ucfed\ude2c\uaaa4\u358c\ub9e7\u1477\ua2de\u74dd\uf7ec\u1d66\uca87\u9b44\uc547\ud056\u1571\ue915\ueeec\udfa3\u3510\u16f7\u606a\u1284\u2bee\ucbba\u9af9\u65b3\ud342\uab5f\ub380\ua601\u433f\ue8b2\u2acd\ucd75\u43da\u3485\ueb9e\u1d5f\u21a2\ua924\u16ca\u10e9\u0d19\u04da\u0926\u7729\ued49\u3d89\uc61e\u9240\u3ca5\u2090\u78f2\u1648\u398f\u19ef\u706d5\ufacc\u4ddb\ucce3\u4a3f\uef02\u726d\uf21d\u4aee\uee27\u2f15\u6a40\u1219\u8d9c\u5ee6\u68ea\ub34a\u238a\u93a6\u0dfb\u2d0e\u79a0\u6b9f\u79b9\ucf50\ub1db\u029f\ucc42\uaaf7\ub029\u361c\u9afa\ud42f\uab57\uff61\ue564\u77e2\u0e11\uc499\ubed2\uc882\ud4c3\u8cd9\u9374\u10da\u9543\u06b4\ucef0\u64a5\u959f\ue17e\ufacd\u071d\u3258\u8f80\ufc52\ucd1b\u8ae5\ua5ad\u1ab3\ud927\u595b\u7b7b\u5ac2\ufded\u14db\ue57e\ubc4e\ufff2\u70d0\u6564\u8d41\udfc2\ud059\ud1ec\u4a6a\u747e\ud5a6\u6cba\ub8ec\uf919\uddae\ue270\u5ec6\uabe2\u5d1d\u2a93\u5001\uedc4\u4ffd\u0be8\ue8cb\u998b\ua847\ucbd9\udfb0\u7b82\ueba3\ub9a3\ub5b5\ub5a6\u2f61\u806f\ub6b3\u439b\ue23a\u99e9\u2e0c\ub321\ud594\u7a41\u0357\uaa93\uf65a\u0879\u4235\u064d\u2fd2\u9c4a\u2129\ue071\u8635\uf6d1\u99cd\ue45f\u5a3f\ueff3\u29d6\ubd12\ubec9\u48b2\ucf77\uddc4\u3dce\u5844\uc238\u05a5\ubae1\u0317\uc345\u4963\uda89\u5895\ud0c2\uded7\ucdf8\u190e\u88b1\ucc46\udf42\ua4ee\u76e4\u769e\u6057\u89dc\u51b1\u2325\u6ef0\u6e4e\u0b46\u81c6\u0281\u63e8\uae81\u685b\u3d45\ueafd\ub16f\u6494\u4b97\u4843\ud305\ufe1e\uef95\u72ef\ub7b4", 1790779936, 0x232E9450 ^ CryptoVault.mask()), c2.B(), () -> {
            if (!this.backend.signedIn()) {
                this.say(CosmeticsView.xs("\u201f\u516b\u57f4\u6446\uac0d\uff60\u65d2\u8efd\u8eab\u9b35\ua32b\u29aa\u623c\ub308\u11e4\ud5f0\ua5dc\u7fff\u81cc\u4169\u9d69\uae73\u6f0c\uc835\u1367\udefe\u8821\u192f\ud1e1\uf4f2\u0c6a\u8429\u616a\u0337\u45fd\uaf55", 1552062710, 0x232E9450 ^ CryptoVault.mask()), uiHost);
                return;
            }
            c2.B(!c2.B());
            this.say(c2.B() ? CosmeticsView.xs("\u52c4\ue8ce\u9885\ucb19\u1a13\udb53\u1618\u26ba\u2e95\u3075\u80b7\u2319\ub4fb\u6d59\u8931\ua8b5\u8e9b\u06c2\u05fe\u5abd\u5552\u7895\u0bd7\u7687\u56bb\u030d\u992d\ue099\ud16e\u93f2\u71a8\u4a2d\u17b7\u129c\u219e\uc03b\u65cd\ue111\ufa40\u4008\u68eb\u39ef\u7000\udb08\ubab4\u3f6c\ua91b\ud2a4\u0014\uf21a", 556916277, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.xs("\u915a\ua31e\u8d15\uf472\u9f74\u4c38\ua89a\uebcf\u3b66\ua424\uc957\ue3bd\u67e0\u8b0b\u241e\ub76e\u2104\ube15\u15a1\uaf81\ub63d\u2dbe\u3381\u94dd\uf531M\ub933\uf416\uaec1\u6ddd\u8df4\u700d\u7584\uc116\ucc92\u8368\ud7f2\u82e2\u0ca1\u6e50\u3075\uce3e\u9464\uad19\u8af3\u6df5\u114e\u0676\u7483", -2109001815, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        f8 = this.toggleRow(builder, uiHost, f2, f8, f4, f5, CosmeticsView.xs("\uedfe\u9898\u2da4\uf010\u74fb\ua8c3\ub050\u1ad3\uba93\uf457\uef43\u10c3\u9a2d\u020b\u72dd\u014d\u01af\u57fe\u0367\u7a2c\u39ff\ue08b\u8a0c\ue8ba\ud0a4\u82fb\ud53d\u2cdc\udd83\u87bc\u3167\udc6f\u8ae6\u8618\ua18b", -1654222619, 0x232E9450 ^ CryptoVault.mask()), CosmeticsView.xs("\u200e\u1bb2\u3937\ua717\u5f2c\u1939\u58be\u331c\u42ca\uac3e\uf0cf\u0298\u33f2\u7b0c\u6884\uae18\ua314\u45df\u2100\ubf59\u24d1\u6e78\u7724\ub47b\u8e63\u08f2\uf995\uc1fc\u8897\u0011\uf132\u4af5\ud0f8\u58f9\uf4db\u039a\u597a\u21fa\u4acd\u2803\u2fcc\u9d36\u1593\u10d4\uf177\u50f2\u299d\ue98f\uddf2\ub728\u4ecc\u51fd\u4be1\ubb0b\uce2d\u75ea\ud2cb\u55f5\u08d1\u4903\u2b80\u04d7\uc92d\ud73b\u8976\ud04c\u2848\u6754\ufa2b\u1659\ufd8a\u9fb0\u6b2b\u3b43\u33a2\ue296\ud3b0\u6ede\ufeb8\u1a51\ud232\u3515\u3dc3\u826d\uf733\u4cb6\u3c9a\u5b5d\u1377\u8733\u0197\u211c\u53e2\ud760\u064e\ucd32\u173a\u1a10\u274c\u1397\u8131\u824d\uec6b\u0f9e\u2810\u4c07\u4f5b\u4682\u8d8c\u0efc\u8413\u4eee\u155d\ubcf5\ucf2d\ubec2\u9954\ucf78\ubd89\ud673\ua9ac\uebb6\uf3e4\u218d\u5f6e\u8f77\ud6f9\uceeb\ueebb\ub009\u811c\u59b4\u22fb\u608c\u02ca", 1054139647, 0x232E9450 ^ CryptoVault.mask()), c2.C(), () -> c2.C(!c2.C()));
        String string2 = this.backend.syncStatus();
        Object object = !this.backend.signedIn() ? CosmeticsView.xs("\ud7a1\u0be8\u1927\uc654\u2cab\u2c4d\u690e\u2eb7\u6c8d\ub38c\uf855\u4518\u4de6\u04ef\ubac9\u3461\u08b4\ua670\ua245\u0b4e\u62e1\uecf7\u1f59\u146f\u9afe\ud90d\u9f6c\uada1\u53b5\u079b\ube57\uea42\u096d\u3242\ud65a\ua61a\udde5\u6088\udb69\uc7a6\u8418\u9241\u9c48\u85ac\uace0\u7419\u636e\u5d72\u26a2\ud186\u7511\u0001\u40e4\u3686\u90f8\u6613\u0e11\ueb5b\u794d\u9017\u1be3\u34eb\uaf5f\u42ac\u0c31\u5476\u63f3\u69d4\u5e74\u4e98\u8611\u4d15\uea2e\u5cde\ub9a0\u383c\u75ad\udffe\uc71e\u1d03\udf47\uffc1\u26ac\u9b44", -1988631583, 0x232E9450 ^ CryptoVault.mask()) : (string2 == null || string2.isEmpty() ? (c2.B() ? CosmeticsView.xs("\uf5d8\u0ea1\u49b6\u9a9b\ued92\u8564\uedbf\ua0eb\uc53a\ua148\u16f4\udf22\uccaf\ufd00\u04dd", 487822771, 0x232E9450 ^ CryptoVault.mask()) : CosmeticsView.xs("\u2d08\u91a2\u3be3\ueab7\u0b6f\u1132\u3f81\uee29\uf3d9\u9f22\ud6cb\ude4e\ub704\ue7d8\u5e10\uf69c\u56a1", 321986417, 0x232E9450 ^ CryptoVault.mask())) : string2);
        for (String string3 : UiKit.wrap((String)object, f4, 9.5f * f5, 3)) {
            builder.text(f2, f8, 9.5f * f5, uiHost.mutedColor(), string3);
            f8 += (float)Math.round(13.0f * f5);
        }
        return f8;
    }

    private void drawPreview(UiDisplayList.Builder builder, UiHost uiHost, c c2, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        UiKit.card(builder, uiHost, f2, f3, f4, f5, f6);
        float f10 = Math.round(14.0f * f6);
        float f11 = Math.round(150.0f * f6);
        float f12 = f2 + f10;
        float f13 = f3 + f10;
        float f14 = f4 - f10 * 2.0f;
        float f15 = f5 - f10 * 2.0f - f11;
        float f16 = Math.round(10.0f * f6 * uiHost.cornerScale());
        builder.gradient(f12, f13, f14, f15, f16, 0, 0, UiKit.withAlpha(uiHost.accent(), 42), UiKit.withAlpha(uiHost.accent(), 42));
        builder.border(f12, f13, f14, f15, f16, 1.0f, 0x12FFFFFF);
        long l2 = uiHost.now();
        float f17 = 0.5f + 0.5f * (float)Math.sin((double)(l2 % 6000000000L) / 6.0E9 * Math.PI * 2.0);
        builder.circle(f12 + f14 * 0.5f, f13 + f15 * 0.62f, Math.min(f14, f15) * (0.3f + 0.02f * f17), UiKit.withAlpha(uiHost.accent(), 20 + (int)(10.0f * f17)));
        float f18 = this.lastSpinAt == 0L ? 0.0f : Math.min(0.1f, (float)(l2 - this.lastSpinAt) / 1.0E9f);
        this.lastSpinAt = l2;
        if (!Float.isNaN(this.yawTarget)) {
            float f19 = CosmeticsView.wrapDegrees(this.yawTarget - this.yaw);
            this.yaw += f19 * Math.min(1.0f, f18 * 9.0f);
            if (Math.abs(f19) < 0.4f) {
                this.yaw = this.yawTarget;
                this.yawTarget = Float.NaN;
            }
        } else if (this.dragging) {
            if (l2 - this.lastDragAt > 90000000L) {
                this.yawVel *= (float)Math.exp(-f18 * 12.0f);
            }
        } else if (Math.abs(this.yawVel) > 1.0f) {
            this.yaw += this.yawVel * f18;
            this.yawVel *= (float)Math.exp(-f18 * 3.2f);
        } else if (c2.D()) {
            this.yawVel = 0.0f;
            this.yaw += 32.0f * f18;
        }
        this.yaw = CosmeticsView.wrapDegrees(this.yaw);
        if (this.backend.previewAvailable() && f15 > 40.0f * f6) {
            this.previewRect[0] = f12;
            this.previewRect[1] = f13;
            this.previewRect[2] = f14;
            this.previewRect[3] = f15;
            uiHost.hit(f12, f13, f14, f15, () -> {
                this.dragging = true;
                this.yawTarget = Float.NaN;
                this.dragLastX = uiHost.pressX();
                this.dragLastY = uiHost.pressY();
            });
        } else {
            this.previewRect[2] = 0.0f;
            String string = CosmeticsView.xs("\u1f82\u3c96\u58a2\uae22\u6bc9\u198f\u2fa7\ue7f2\ua13e\u5c80\u134f\ude1a\ua6ac\u69e7\u9ab4\u078b\u9fb9\u015a\u72ed\u5071\udc20\u21b8\u91db\u581d\u0f0c\ufcd4\u7192", -438541726, 0x232E9450 ^ CryptoVault.mask());
            f9 = 10.0f * f6;
            builder.text(f12 + (f14 - GpuUiFontMetrics.textWidth(string, f9)) * 0.5f, f13 + f15 * 0.5f - f9 * 0.5f, f9, uiHost.muted2Color(), string);
        }
        String string = CosmeticsView.xs("\u47cb\ubec4\u549f\u2df8\u660b\uab5b\ud612\u7e4a\u854a\u2944\u0f4d\u05f7\u8751\uef0f\ubb3e\u1f97\u9731\u0652\uedde\u8ad0\u76ab\uafae\u0bc0\u8926\ub041\u7564\u8cfe\u8715\uf4da\ua82f\ue107\ucf01\u9ea0\u54a5\u8a46\u84bb\u2136\u86a5\u2d2f\u41e1\ufb8e\u9221", -1801287987, 0x232E9450 ^ CryptoVault.mask());
        f9 = 8.5f * f6;
        builder.text(f12 + (f14 - GpuUiFontMetrics.textWidth(string, f9)) * 0.5f, f13 + f15 - f9 - (float)Math.round(8.0f * f6), f9, uiHost.muted2Color(), string);
        float f20 = Math.round(6.0f * f6);
        float f21 = Math.round(28.0f * f6);
        float f22 = f13 + f15 + (float)Math.round(10.0f * f6);
        float f23 = (f14 - f20 * 2.0f) / 3.0f;
        boolean bl = !c2.D() && Math.abs(CosmeticsView.wrapDegrees(this.yaw - -28.0f)) < 40.0f;
        boolean bl2 = !c2.D() && Math.abs(CosmeticsView.wrapDegrees(this.yaw - 152.0f)) < 40.0f;
        UiKit.button(builder, uiHost, CosmeticsView.xs("\uf2c0\ue342\u3de6\ueeae\u047c\u508c\uf8e4\u7295\u5547", 71078985, 0x232E9450 ^ CryptoVault.mask()), f12, f22, f23, f21, f6, bl ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f7, f8, () -> {
            c2.D(false);
            this.yawTarget = -28.0f;
            this.pitch = 6.0f;
            this.roll = 0.0f;
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\uf1b3\u5b01\u8f73\u0674\u26aa\u6cef\uac7e\ubdf0", -2104328241, 0x232E9450 ^ CryptoVault.mask()), f12 + f23 + f20, f22, f23, f21, f6, bl2 ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f7, f8, () -> {
            c2.D(false);
            this.yawTarget = 152.0f;
            this.pitch = 6.0f;
            this.roll = 0.0f;
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\ucba0\u2986\u7b27\ua9db\ue5c2\uccfd\uae7c", 461059835, 0x232E9450 ^ CryptoVault.mask()), f12 + (f23 + f20) * 2.0f, f22, f23, f21, f6, c2.D() ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f7, f8, () -> {
            c2.D(!c2.D());
            this.yawTarget = Float.NaN;
        });
        float f24 = f22 + f21 + f20;
        float f25 = (f14 - f20) / 2.0f;
        UiKit.button(builder, uiHost, CosmeticsView.xs("\ub6f2\ub33d\u00c7\u9dc4\u2531\u6842\u1a67\ucae0\u1331\u9c72\u2867\ufb46", 2028716464, 0x232E9450 ^ CryptoVault.mask()), f12, f24, f25, f21, f6, UiKit.Style.GHOST, !c2.A().H(), f7, f8, () -> {
            c2.A(d.sm);
            this.say(CosmeticsView.xs("\u6e70\u1e32\u19f8\u6b6d\u975a\u6581\ud2a4\u6b82\ud297\uc7da\u6627\uc1a2\ue117\u7d4a\uee27\u10c6\u6c44\uc6d5\u28b7\u244f\u6820\u20fb\ua657", 451652382, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        UiKit.button(builder, uiHost, CosmeticsView.xs("\ue0cc\u5a9f\u99d9\u1a84\u3934\u8c8a\u78a3\u5e09\uce40\u338c\u0dab", -2042505058, 0x232E9450 ^ CryptoVault.mask()), f12 + f25 + f20, f24, f25, f21, f6, UiKit.Style.SECONDARY, true, f7, f8, () -> {
            this.shuffle(c2);
            this.say(CosmeticsView.xs("\ud558\u6eed\uc558\u88b5\u935d\ud16f\ucb6a\u8c70\u4129\uceee\ub0fe\uf678\u8c59\u278f\u2273\u44ce\u2923\ud456\u3c2d\uc243\u26dd\uaab7\u86d7\u1f08\u3112", 611537128, 0x232E9450 ^ CryptoVault.mask()), uiHost);
        });
        String string2 = CosmeticsView.worn(c2.A());
        float f26 = f24 + f21 + (float)Math.round(10.0f * f6);
        for (String string3 : UiKit.wrap(string2.isEmpty() ? CosmeticsView.xs("\u9e96\u864c\u24f9\u29c9\uf31c\u2286\u631d\u2e6d\ufd18\u3840\u1e75\uf496\u0201\u4647\u9562\u42d2\uf823\uc6df\u7160", 566440082, 0x232E9450 ^ CryptoVault.mask()) : string2, f14, 9.0f * f6, 3)) {
            builder.text(f12, f26, 9.0f * f6, string2.isEmpty() ? uiHost.muted2Color() : uiHost.mutedColor(), string3);
            f26 += (float)Math.round(12.0f * f6);
        }
    }

    private static float wrapDegrees(float f2) {
        return f2 - 360.0f * (float)Math.floor((f2 + 180.0f) / 360.0f);
    }

    private void shuffle(c c2) {
        Object object;
        d d2 = d.sm;
        a.e e2 = a.sw.get(this.random.nextInt(a.sw.size()));
        d2 = d2.A(CosmeticsView.xs("\u7b25\ufdb2\u9551\ube6b\uf686\u9153\u85b5\u4b59\u4261", -1159714904, 0x232E9450 ^ CryptoVault.mask()).equals(e2.k()) ? a.a(this.backend.accentRgb()) : e2.a());
        if (this.random.nextFloat() < 0.7f) {
            object = b.no.get(this.random.nextInt(b.no.size()));
            d2 = d2.A(new d.b(((b.a)object).C(), ((b.a)object).F()));
        }
        if (this.random.nextFloat() < 0.45f) {
            object = b.DR.get(this.random.nextInt(b.DR.size()));
            d2 = d2.A(d.d.A(((b.a)object).C()));
        }
        for (d.c c3 : d.c.values()) {
            if (!(this.random.nextFloat() < 0.5f)) continue;
            b.a a2 = c3.tK.get(this.random.nextInt(c3.tK.size()));
            d2 = d2.A(c3, new d.a(a2.C(), a2.F()));
        }
        c2.A(d2);
    }

    private static String worn(d d2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (d2.B() != null) {
            arrayList.add(CosmeticsView.capeName(d2.B()));
        }
        if (d2.A() != null) {
            arrayList.add(CosmeticsView.itemName(b.no, d2.A().H()));
        }
        if (d2.A() != null) {
            arrayList.add(CosmeticsView.itemName(b.DR, d2.A().I()));
        }
        for (d.c c2 : d.c.values()) {
            d.a a2 = d2.A(c2);
            if (a2 == null) continue;
            arrayList.add(CosmeticsView.itemName(c2.tK, a2.G()));
        }
        return String.join((CharSequence)CosmeticsView.xs("\ubaff\u9258\uc413\u8be7\uf20c", 1114082268, 0x232E9450 ^ CryptoVault.mask()), arrayList);
    }

    private static String capeName(a a2) {
        if (CosmeticsView.xs("\ub22f\u446b\u754b\ud65b\u07d0\u1d88\u0398\u1c84\u56fc", 531773903, 0x232E9450 ^ CryptoVault.mask()).equals(a2.n())) {
            return CosmeticsView.xs("\u7d3f\ub291\u9e21\u7c54\u1e94\uec96\udb40\uaafe\u3e9c\ubb83\ua13e\ue772\ufe8c", -743718768, 0x232E9450 ^ CryptoVault.mask());
        }
        a.e e2 = a.a(a2.n());
        return e2 == null ? CosmeticsView.xs("\uaa42\ud502\u916f\u9ee6\u2262\uf550\u11d1", -1442080021, 0x232E9450 ^ CryptoVault.mask()) : e2.l();
    }

    private static String itemName(List<b.a> list, String string) {
        for (b.a a2 : list) {
            if (!a2.C().equals(string)) continue;
            return a2.D();
        }
        return string;
    }

    private static /* synthetic */ void lambda$drawWings$2(c c2, List list, int n2) {
        c2.A(c2.A().A(c2.A().A().C((String)list.get(n2))));
    }

    private /* synthetic */ void lambda$drawWings$1(c c2, b.a a2, UiHost uiHost) {
        d.d d2 = c2.A().A();
        c2.A(c2.A().A(d2 == null ? d.d.A(a2.C()) : d2.B(a2.C())));
        this.say(CosmeticsView.xs("\u97bd\u9ca5\u5184\udbcb\u3276\ucc2b\ude60\uf479\u521d", -1330832757, 0x232E9450 ^ CryptoVault.mask()) + a2.D() + ". " + a2.E(), uiHost);
    }

    private /* synthetic */ void lambda$drawCapes$2(c c2, a a2, a.e e2, UiHost uiHost) {
        c2.A(c2.A().A(a2));
        this.say(CosmeticsView.xs("\uba95\uc3a6\uf4ce\ue044\u6bb3\u3a17\u18bc\u8270\ubdc3\u3eec\u363e\ubcf9", -987667934, 0x232E9450 ^ CryptoVault.mask()) + e2.l() + ".", uiHost);
    }

    private /* synthetic */ void lambda$drawCapes$1(c c2, a a2, UiHost uiHost) {
        c2.A(c2.A().A(a2));
        this.say(CosmeticsView.xs("\ued3f\u960d\u7ba4\uf304\u203b\u944f\u6b99\u7a52\ue088\uc957\u6d87\u634b\u490c\u9c2b\u4efa\ue5b1\ub15b\ua298\u17de\ude66\u12e1\ud2b3\u7225\u2151\udd23\u0217\ue364\ucd81\uc979\u3d57\u535d\ufd06\u3736\u1596\u3ddc\uc066\u3e4d\u3c72\ua24e\u95e7\u9a42\ub3ba\u2f98\u793f\u4847\u5ead\u30db\u4c87\u5187\u1345\ucc04\u3933\u6d2c\u9961", 713543142, 0x232E9450 ^ CryptoVault.mask()), uiHost);
    }

    private static /* synthetic */ String xs(String string, int n2, int n3) {
        int n4;
        int n5;
        char[] cArray = string.toCharArray();
        if (cArray.length == 0) {
            return "";
        }
        long l2 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
        l2 ^= 0x9E3779B97F4A7C15L;
        l2 = (l2 ^ l2 >>> 30) * -4658895280553007687L;
        l2 = (l2 ^ l2 >>> 27) * -7723592293110705685L;
        l2 ^= l2 >>> 31;
        long l3 = l2 ^ 0x5A5A5A5A5A5A5A5AL;
        l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
        l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
        l3 ^= l3 >>> 31;
        int n6 = (int)(l3 >>> 16) & 0xFFFF;
        long l4 = l2 ^ 0xA5A5A5A5A5A5A5A5L;
        l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
        l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
        if ((n5 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
            n5 = 19997;
        }
        int n7 = n5;
        for (int i2 = 0; i2 < 4; ++i2) {
            n7 *= 2 - n5 * n7;
        }
        long l5 = (l2 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        long l6 = (l2 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        l6 = (l6 ^ l6 >>> 30) * -4658895280553007687L;
        l6 = (l6 ^ l6 >>> 27) * -7723592293110705685L;
        int n8 = (n7 &= 0xFFFF) * (cArray[0] - n6 & 0xFFFF) & 0xFFFF;
        if ((n4 = n8 ^ (int)((l6 ^= l6 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
            return string;
        }
        char[] cArray2 = new char[n4];
        int n9 = 0;
        for (int i3 = 0; i3 < cArray.length - 1; ++i3) {
            int n10 = n7 * (cArray[i3 + 1] - n6 & 0xFFFF) & 0xFFFF;
            long l7 = (long)n9 * -7046029254386353131L;
            long l8 = l5 ^ l7;
            l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
            l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
            l8 ^= l8 >>> 31;
            if (i3 < n4) {
                cArray2[i3] = (char)(n10 ^ (int)(l8 >>> 48) & 0xFFFF);
            }
            l5 = l5 * 6364136223846793005L + 1442695040888963407L;
            n9 = n10;
        }
        return new String(cArray2);
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Section {
        CAPES(Section.gt("\u78c1\u3c09\uf924\u0e64\u8906\u742a\u117e\u76b1\uefa2\u28b9\u7246", 1101926977, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u3b87\u1c79\u6305\u0640\u0a60\u0199\u829a\u54d2", -2016570365, 0x9147CBA1 ^ CryptoVault.mask()), null),
        HATS(Section.gt("\u7881\ubb28\u8052\u268c\u3d5e\u1724\uaf24\u9f93\ub44b\u69f7\u8b32\u28f1", 448319327, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\uf1d7\uea8a\u13ac\ua378\ucfcd\u1ddc", -9450618, 0x9147CBA1 ^ CryptoVault.mask()), null),
        BACKS(Section.gt("\uc45a\u87ae\u581f\u04e3\u7030\uac1a\ub2ca\uca79\u7a4f\u08aa", -861596556, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\ueb25\ub8e3\u7f9c\u4e9a\ub614", -718945872, 0x9147CBA1 ^ CryptoVault.mask()), d.c.sc),
        WINGS(Section.gt("\u2976\u0a7a\u23bf\ud258\u52e8\u50bd\u2bbb\u44ef\ua2cc", -497209372, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u37b8\u7676\u8d53\uce77\u4312\uc072\u47c9", -1466566807, 0x9147CBA1 ^ CryptoVault.mask()), null),
        WEAPONS(Section.gt("\u9d02\u1db2\u1612\u296e\u625d\u6057\ubcc6\u5553\u52d8\u711c\u6c6c", -576876431, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\ud52b\u91a9\u5f3c\uc277\u948c\uee6b\u7b74\u4094\u7d01", -326970723, 0x9147CBA1 ^ CryptoVault.mask()), null),
        ARMOR(Section.gt("\u6d44\u42f3\u7a78\u71d6\u0953\ueca8\uf3ff\uff44\u3c9c\ue364\u7bc6", -1841785444, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u0ea7\u790c\u084a\ubb97\u9354\u5c25", -590944819, 0x9147CBA1 ^ CryptoVault.mask()), null),
        TRAILS(Section.gt("\u5b2a\u6d1c\u11c5\ud3b6\u1401\u1aaf\u0e7a\u279b\u85a4\u7f0e\ue753\u3990\ub894", -1222540924, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u89f6\ua068\u7c90\u4491\u968f\uedc1\uf5fe\u52ad", -149200339, 0x9147CBA1 ^ CryptoVault.mask()), d.c.ta),
        SKY(Section.gt("\ud18a\u93cc\ue4ca\u948d\u936a\u053d\u867a\uf6a2\u5923\u25f7\ua9e2\u46ec\u2f21", -969273083, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\ucf4c\uda8a\u758d\u7b8f\u4204\uf06e\u102a", -559050399, 0x9147CBA1 ^ CryptoVault.mask()), d.c.sh),
        COMPANIONS(Section.gt("\u3a19\u2103\ub0e8\u3fd0\uf6b5\uefe6\u280c\u38b7\uee61\u4bc1\u489a\ue1e7\ua49b\ufd81\u80dc\u05af", -310726228, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u4549\u00c6\u0da1\ub297\u71f6\u2e23\u0453\uc3f4\u6096\u6f1b\u6775\u3bdc", -1011255573, 0x9147CBA1 ^ CryptoVault.mask()), d.c.wh),
        MAKER(Section.gt("\u0767\uee7e\u5966\ub2f5\uc290\u45e1\u6eb6", 328588834, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\u58c5\u4249\ud421\ua8ee\u41b9\uf282\u1999\u1cad\u66a7\u4b05\u5061\u4332", 1104684542, 0x9147CBA1 ^ CryptoVault.mask()), null),
        SETTINGS(Section.gt("\ue9f5\uc962\ud735\u42e5\uf42f\udac9\u734c\uabed", -1597769474, 0x9147CBA1 ^ CryptoVault.mask()), Section.gt("\ue946\udb34\uef45\uc406\u83a6\ua413\u7e05\uc058\ub52f\u6e9d\u2e72\u396a", 1541853913, 0x9147CBA1 ^ CryptoVault.mask()), null);

        final String group;
        final String label;
        final d.c slot;

        private Section(String string2, String string3, d.c c2) {
            this.group = string2;
            this.label = string3;
            this.slot = c2;
        }

        private static /* synthetic */ String gt(String string, int n2, int n3) {
            int n4;
            int n5;
            char[] cArray = string.toCharArray();
            if (cArray.length == 0) {
                return "";
            }
            long l2 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
            l2 ^= 0x9E3779B97F4A7C15L;
            l2 = (l2 ^ l2 >>> 30) * -4658895280553007687L;
            l2 = (l2 ^ l2 >>> 27) * -7723592293110705685L;
            l2 ^= l2 >>> 31;
            long l3 = l2 ^ 0x5A5A5A5A5A5A5A5AL;
            l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
            l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
            l3 ^= l3 >>> 31;
            int n6 = (int)(l3 >>> 16) & 0xFFFF;
            long l4 = l2 ^ 0xA5A5A5A5A5A5A5A5L;
            l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
            l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
            if ((n5 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
                n5 = 19997;
            }
            int n7 = n5;
            for (int i2 = 0; i2 < 4; ++i2) {
                n7 *= 2 - n5 * n7;
            }
            long l5 = (l2 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            long l6 = (l2 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            l6 = (l6 ^ l6 >>> 30) * -4658895280553007687L;
            l6 = (l6 ^ l6 >>> 27) * -7723592293110705685L;
            int n8 = (n7 &= 0xFFFF) * (cArray[0] - n6 & 0xFFFF) & 0xFFFF;
            if ((n4 = n8 ^ (int)((l6 ^= l6 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
                return string;
            }
            char[] cArray2 = new char[n4];
            int n9 = 0;
            for (int i3 = 0; i3 < cArray.length - 1; ++i3) {
                int n10 = n7 * (cArray[i3 + 1] - n6 & 0xFFFF) & 0xFFFF;
                long l7 = (long)n9 * -7046029254386353131L;
                long l8 = l5 ^ l7;
                l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
                l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
                l8 ^= l8 >>> 31;
                if (i3 < n4) {
                    cArray2[i3] = (char)(n10 ^ (int)(l8 >>> 48) & 0xFFFF);
                }
                l5 = l5 * 6364136223846793005L + 1442695040888963407L;
                n9 = n10;
            }
            return new String(cArray2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Backend {
        public boolean unlocked();

        public c prefs();

        public class_2960 capeTexture(a var1);

        public int accentRgb();

        public boolean previewAvailable();

        public boolean signedIn();

        public String syncStatus();

        public String weaponCountLabel();

        public void weaponPreview(boolean var1);

        public void importImage(Consumer<String> var1, Consumer<String> var2);
    }

    @Environment(value=EnvType.CLIENT)
    public record Thumbnail(float x, float y, float w, float h, d selection, int frame, String weapon) {
        public Thumbnail(float f2, float f3, float f4, float f5, d d2, int n2) {
            this(f2, f3, f4, f5, d2, n2, null);
        }
    }
}

