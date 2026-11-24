/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package com.ishland.scalablelux.gui.mobile;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.SlAuth;
import com.ishland.scalablelux.auth.MobileSignIn;
import com.ishland.scalablelux.auth.PayloadLoader;
import com.ishland.scalablelux.auth.PayloadStatus;
import com.ishland.scalablelux.gui.ClickGuiRouter;
import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.mobile.MobileUi;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.McCompat;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

@Environment(value=EnvType.CLIENT)
public final class PayloadLoadingScreen
extends class_437 {
    private static final long HINT_DELAY_NANOS = 4000000000L;
    private static final long EXIT_NANOS = 180000000L;
    private int ACCENT = -6430259;
    private int textC = -1117194;
    private int mutedC = -8219739;
    private int panelC = -15328476;
    private int deepC = -15723492;
    private static final int WARN = -19316;
    private final class_437 parent;
    private final long openedAt;
    private long lastFrameNanos = this.openedAt = System.nanoTime();
    private long readyAt;
    private boolean plain;
    private UiDisplayList.Builder list;
    private class_332 context;
    private Mode mode = Mode.WAITING;
    private String status = "";
    private String hint = "";
    private PayloadStatus.Progress progress = PayloadStatus.progressSnapshot();
    private PayloadStatus.State payloadState = PayloadStatus.State.NOT_STARTED;
    private boolean signedIn;
    private float spinnerAlpha = 1.0f;
    private final float[] primary = new float[4];
    private final float[] retryLink = new float[4];
    private final float[] copyLink = new float[4];

    public PayloadLoadingScreen(class_437 class_4372) {
        super((class_2561)class_2561.method_43470((String)PayloadLoadingScreen.bf("\u4dd8\ucd0b\ub316\u2261\u3da5\u5346\u1f39\udd46\u9241\u0605\u0b40\u71e1\u504f\u59cf\ub0a6\u60c2", -1437699017, 0x5703A517 ^ CryptoVault.mask())));
        this.parent = class_4372;
        this.plain = false;
        this.refresh();
    }

    public boolean method_25421() {
        return false;
    }

    protected void method_57734(class_332 class_3322) {
    }

    public void method_25420(class_332 class_3322, int n2, int n3, float f2) {
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            McCompat.setScreen(this.field_22787, this.parent);
        }
    }

    public void method_25393() {
        if (this.field_22787 == null) {
            return;
        }
        if (this.readyAt == 0L && SlAuth.isAuthorized() && SlApp.isMenuReady()) {
            this.readyAt = System.nanoTime();
        }
        if (this.readyAt != 0L) {
            if (System.nanoTime() - this.readyAt >= 180000000L) {
                McCompat.setScreen(this.field_22787, ClickGuiRouter.create(this.parent));
            }
            return;
        }
        this.refresh();
    }

    private void refresh() {
        try {
            boolean bl;
            PayloadStatus.State state;
            this.payloadState = state = PayloadStatus.state();
            this.progress = PayloadStatus.progressSnapshot();
            this.signedIn = bl = SlAuth.isAuthorized();
            MobileSignIn.Phase phase = MobileSignIn.phase();
            if (!bl && phase != MobileSignIn.Phase.RESTORING && phase != MobileSignIn.Phase.SIGNING_IN) {
                this.mode = Mode.SIGNED_OUT;
            } else {
                if (!bl) {
                    this.mode = Mode.WAITING;
                    this.status = PayloadLoadingScreen.bf("\u2c3d\u0d67\u9b54\uebac\u0daa\ua041\uaaf9\uf3f4\u471a\ua5b4\ud6d3\u4b15\u5ec5", 513698996, 0x5703A517 ^ CryptoVault.mask());
                    this.hint = "";
                    return;
                }
                this.mode = state == PayloadStatus.State.NOT_ENTITLED ? Mode.NOT_ENTITLED : (state == PayloadStatus.State.INITIALIZATION_FAILED && !PayloadLoader.isRecovering() && !SlApp.isSettingUp() ? Mode.FAILED : Mode.WAITING);
            }
            this.status = PayloadStatus.waitingText();
            this.hint = PayloadStatus.hintText();
        }
        catch (Throwable throwable) {
            this.mode = Mode.WAITING;
        }
    }

    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        this.context = class_3322;
        this.list = UiDisplayList.builder();
        try {
            this.draw(n2, n3);
            if (!this.plain) {
                GpuUiRenderer.render(class_3322, this.list.build());
            }
        }
        catch (LinkageError | RuntimeException throwable) {
            if (this.plain) {
                throw throwable;
            }
            this.plain = true;
            this.draw(n2, n3);
        }
    }

    private void draw(int n2, int n3) {
        long l2 = System.nanoTime();
        float f2 = Math.min(0.1f, (float)(l2 - this.lastFrameNanos) / 1.0E9f);
        this.lastFrameNanos = l2;
        float f3 = (float)(l2 - this.openedAt) / 1.0E9f;
        float f4 = PayloadLoadingScreen.clamp(Math.min((float)this.field_22789 / 640.0f, (float)this.field_22790 / 400.0f), 0.75f, 1.15f);
        float f5 = PayloadLoadingScreen.ease(PayloadLoadingScreen.clamp(f3 / 0.22f, 0.0f, 1.0f));
        float f6 = this.readyAt == 0L ? 0.0f : PayloadLoadingScreen.ease(PayloadLoadingScreen.clamp((float)(l2 - this.readyAt) / 1.8E8f, 0.0f, 1.0f));
        float f7 = f5;
        float f8 = f5 * (1.0f - f6);
        boolean bl = this.mode == Mode.WAITING;
        this.spinnerAlpha += ((bl ? 1.0f : 0.0f) - this.spinnerAlpha) * Math.min(1.0f, f2 * 10.0f);
        this.loadTheme();
        int n4 = PayloadLoadingScreen.mix(this.deepC, -16777216, 0.15f);
        int n5 = PayloadLoadingScreen.mix(this.deepC, this.ACCENT, 0.1f);
        if (this.plain) {
            this.context.method_25294(0, 0, this.field_22789, this.field_22790, PayloadLoadingScreen.alpha(n4, f7));
        } else {
            this.list.gradient(0.0f, 0.0f, this.field_22789, this.field_22790, 0.0f, PayloadLoadingScreen.alpha(n4, f7), PayloadLoadingScreen.alpha(n4, f7), PayloadLoadingScreen.alpha(n5, f7), PayloadLoadingScreen.alpha(n5, f7));
        }
        float f9 = (float)this.field_22789 / 2.0f;
        float f10 = (float)this.field_22790 / 2.0f - (bl ? 104.0f : 50.0f) * f4;
        int n6 = bl ? this.ACCENT : -19316;
        float f11 = Math.min(380.0f * f4, (float)this.field_22789 - 24.0f);
        float f12 = f10 - (23.0f * f4 + 10.0f * f4) - 22.0f * f4;
        float f13 = f10 + (bl ? 238.0f : 146.0f) * f4;
        float f14 = Math.min(f13 - f12, (float)this.field_22790 - 16.0f);
        float f15 = f9 - f11 / 2.0f;
        float f16 = 22.0f * f4;
        if (!this.plain) {
            this.list.shadow(f15, f12 + 6.0f * f4, f11, f14, f16, 24.0f * f4, 0.0f, PayloadLoadingScreen.alpha(-16777216, f8 * 0.45f));
            this.list.shadow(f15, f12, f11, f14, f16, 30.0f * f4, 0.0f, PayloadLoadingScreen.alpha(n6, f8 * 0.12f));
        }
        this.rect(f15, f12, f11, f14, f16, PayloadLoadingScreen.alpha(this.panelC, f8 * 0.96f));
        if (!this.plain) {
            this.list.border(f15, f12, f11, f14, f16, Math.max(1.0f, f4), PayloadLoadingScreen.alpha(-1, f8 * 0.14f));
        }
        float f17 = bl ? 1.0f + 0.025f * (float)Math.sin(f3 * 2.4f) : 1.0f;
        float f18 = 46.0f * f4 * (1.0f + 0.06f * f6);
        if (!this.plain) {
            this.list.shadow(f9 - f18 / 2.0f, f10 - f18 / 2.0f, f18, f18, f18 * 0.3f, 26.0f * f4, 2.0f * f4, PayloadLoadingScreen.alpha(n6, f8 * 0.16f));
        }
        this.rect(f9 - f18 / 2.0f, f10 - f18 / 2.0f, f18, f18, f18 * 0.28f, PayloadLoadingScreen.alpha(this.panelC, f8));
        if (!this.plain) {
            this.list.border(f9 - f18 / 2.0f, f10 - f18 / 2.0f, f18, f18, f18 * 0.28f, Math.max(1.0f, f4), PayloadLoadingScreen.alpha(0x1AFFFFFF, f8));
        }
        float f19 = 24.0f * f4 * f17;
        if (this.plain) {
            this.text("A", f9, f10 - 9.0f * f4, 20.0f * f4, PayloadLoadingScreen.alpha(this.textC, f8), false);
        } else {
            ClassicIcons.drawLogo(this.list, f9 - f19 / 2.0f, f10 - f19 / 2.0f, f19, PayloadLoadingScreen.alpha(this.textC, f8));
        }
        float f20 = f18 / 2.0f + 10.0f * f4;
        float f21 = 2.4f * f4;
        if (!this.plain) {
            this.list.border(f9 - f20 - f21 / 2.0f, f10 - f20 - f21 / 2.0f, f20 * 2.0f + f21, f20 * 2.0f + f21, f20 + f21 / 2.0f, f21, PayloadLoadingScreen.alpha(bl ? 0x12FFFFFF : 654292108, f8));
        }
        if (this.spinnerAlpha > 0.01f) {
            this.drawArc(f9, f10, f20, f21, f3, PayloadLoadingScreen.alpha(n6, f8 * this.spinnerAlpha));
        }
        float f22 = f10 + f20 + 24.0f * f4;
        if (bl) {
            this.drawWaiting(f9, f22, f4, l2, f8, n2, n3);
        } else {
            this.drawNeedsPlayer(f9, f22, f4, f8, n2, n3);
        }
        if (l2 - this.openedAt > 4000000000L || !bl) {
            float f23 = bl ? PayloadLoadingScreen.clamp((float)(l2 - this.openedAt - 4000000000L) / 4.0E8f, 0.0f, 1.0f) : 1.0f;
            this.text(PayloadLoadingScreen.bf("\ud9ac\u6761\ubcca\u7efc\u7466\u28bc\u0c3e\u7a1d\u4027\u7f9c\u4249\uad60\u3e4e\u957c\u562b\u1829\uc1fa\u4bc9", 1882246344, 0x5703A517 ^ CryptoVault.mask()), f9, (float)this.field_22790 - 22.0f * f4, 8.0f * f4, PayloadLoadingScreen.alpha(PayloadLoadingScreen.alpha(this.mutedC, 0.7f), f8 * f23), false);
        }
    }

    private void loadTheme() {
        try {
            UiThemeMaterial.Theme theme = UiThemeMaterial.theme(HitboxConfigScreen752809.a == null ? "" : HitboxConfigScreen752809.a.getCurrentMode());
            this.ACCENT = 0xFF000000 | theme.accent();
            this.textC = 0xFF000000 | theme.text();
            this.mutedC = 0xFF000000 | theme.muted();
            this.panelC = 0xFF000000 | theme.panelBase();
            this.deepC = 0xFF000000 | theme.deep();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static int mix(int n2, int n3, float f2) {
        int n4 = Math.round((float)(n2 >> 16 & 0xFF) * (1.0f - f2) + (float)(n3 >> 16 & 0xFF) * f2);
        int n5 = Math.round((float)(n2 >> 8 & 0xFF) * (1.0f - f2) + (float)(n3 >> 8 & 0xFF) * f2);
        int n6 = Math.round((float)(n2 & 0xFF) * (1.0f - f2) + (float)(n3 & 0xFF) * f2);
        return 0xFF000000 | n4 << 16 | n5 << 8 | n6;
    }

    private void drawWaiting(float f2, float f3, float f4, long l2, float f5, int n2, int n3) {
        float f6;
        float f7;
        float f8;
        PayloadLoadingScreen.hide(this.primary);
        float f9 = Math.min(320.0f * f4, (float)this.field_22789 - 32.0f);
        float f10 = f2 - f9 / 2.0f;
        boolean bl = this.payloadState == PayloadStatus.State.LOADED || this.payloadState == PayloadStatus.State.PARTIAL;
        boolean bl2 = this.payloadState == PayloadStatus.State.LOADING || bl || !this.signedIn;
        String string = this.progress.firstInitialization() ? PayloadLoadingScreen.bf("\u507b\ua1e1\ud752\u8ac4\u88f0\u5151\u7037\u5b77\uce16\u0e51\u7851\u39d1\u18f6\ua1e6\u1e35\u82a9\u9842\u7926\u2528\ud5a8\u0a7a\u7b65\u2513\uc712\u8b9c\u3f19", 557869815, 0x5703A517 ^ CryptoVault.mask()) : PayloadLoadingScreen.bf("\uf262\udc12\u44f0\u4e6f\u442a\u712d\u73f1\ud55d\u4cc0\u474b\u50f6\ud96a\uf7e3\u0311\u9035\u39db\u3283\u25b8\u69d6\u4c07\ua6e3\ue357\u39ce\uf2d6\u603f\u7b62", 42992190, 0x5703A517 ^ CryptoVault.mask());
        this.text(string, f2, f3, 16.0f * f4, PayloadLoadingScreen.alpha(this.textC, f5), true);
        this.text(this.progress.firstInitialization() ? PayloadLoadingScreen.bf("\u0cb6\u66e8\u575e\uaa69\ue752\u9b23\uba16\uc413\u8c8c\ue784\u778d\ufdfd\ua5ba\uabf4\u2412\u3569\u1346\uf5c5\ua633\ubc8a\u0cd4\ua22c\u78e8\ua5aa\u618a\uc2fc\ue38f\ud764\u2306\uc692\u63b3\u772c\u0bcb\u74b9\u5b2b\u44e7\ue205\ub238\u5a18\u928a\u86d0\uf416", -1969280803, 0x5703A517 ^ CryptoVault.mask()) : PayloadLoadingScreen.bf("\u2067\uc86c\u6964\uca57\u48e2\uba0e\u9fa3\ueea4\udd7e\u17e1\u6227\uc67f\u368e\u7301\u951b\u9392\u3d2a\u29a9\uf5f7\udab2\ub13f\u9787\u8176\u7c07\u8ac6\uf4c6\ubbdc\u3928\u1418\ube99\ub456\ud5e1\u5d34\u7bd4\u1752\u8f16\u55f8\u0b8c\u0d35\ua7b7\ue778\u96b0\u6f43\uc8be\u7338\ua84d\u4b89\uda3e\ue6ac", -669477549, 0x5703A517 ^ CryptoVault.mask()), f2, f3 + 24.0f * f4, 8.5f * f4, PayloadLoadingScreen.alpha(this.mutedC, f5), false);
        String string2 = this.progress.total() > 0 && this.payloadState == PayloadStatus.State.LOADING ? this.progress.completed() + PayloadLoadingScreen.bf("\u7621\u3c34\u90f3\u330c", 1502983655, 0x5703A517 ^ CryptoVault.mask()) + this.progress.total() : "";
        float f11 = this.textWidth(string2, 9.0f * f4);
        this.textAt(this.fit(this.status, f9 - f11 - (string2.isEmpty() ? 0.0f : 8.0f * f4), 10.0f * f4), f10, f3 + 49.0f * f4, 10.0f * f4, PayloadLoadingScreen.alpha(bl2 ? this.mutedC : -19316, f5));
        this.textAt(string2, f10 + f9 - f11, f3 + 50.0f * f4, 9.0f * f4, PayloadLoadingScreen.alpha(this.ACCENT, f5));
        float f12 = f3 + 68.0f * f4;
        this.rect(f10, f12, f9, 5.0f * f4, 2.5f * f4, PayloadLoadingScreen.alpha(0x22FFFFFF, f5));
        float f13 = this.progress.fraction();
        if (bl2 && this.payloadState == PayloadStatus.State.LOADING && f13 >= 0.0f) {
            if (f13 > 0.0f) {
                this.rect(f10, f12, f9 * f13, 5.0f * f4, 2.5f * f4, PayloadLoadingScreen.alpha(this.ACCENT, f5));
            }
        } else if (bl2) {
            f8 = (float)(l2 - this.openedAt) / 1.0E9f * 0.6f % 1.0f;
            f7 = f9 * 0.24f;
            float f14 = f10 + (f8 * 1.24f - 0.24f) * f9;
            float f15 = Math.max(f10, f14);
            f6 = Math.min(f10 + f9, f14 + f7);
            if (f6 - f15 > 1.0f) {
                this.rect(f15, f12, f6 - f15, 5.0f * f4, Math.min(2.5f * f4, (f6 - f15) * 0.5f), PayloadLoadingScreen.alpha(this.ACCENT, f5));
            }
        }
        f8 = 12.0f * f4;
        f7 = (f9 - f8 * 2.0f) / 3.0f;
        String[] stringArray = new String[]{PayloadLoadingScreen.bf("\ud675\u64c7\u282f\ue197\u4bd7\u9b7b\u1c93\u204f\ucd96\u41dc\u29e3\ubf95\ub357\u9423\u8a3d\u7de5\ubc7b\ud889\ueb0a", 312546429, 0x5703A517 ^ CryptoVault.mask()), PayloadLoadingScreen.bf("\ufe84\u85a8\u75a6\u5312\u1160\u6e62\u0e40\u6cd9\u459f\u7512\uef67\ua4cc\ub69d\u764b", 1386801590, 0x5703A517 ^ CryptoVault.mask()), PayloadLoadingScreen.bf("\uc2c5\u64d7\u8a20\u0569\ub2bf\ue9f2\u4032\ue472\ub158\u65e9\uf162\uf8ec\u094f\u8157", -779235582, 0x5703A517 ^ CryptoVault.mask())};
        for (int i2 = 0; i2 < 3; ++i2) {
            f6 = f10 + (float)i2 * (f7 + f8);
            boolean bl3 = i2 == 0 ? this.signedIn : (i2 == 1 ? bl : this.readyAt != 0L);
            this.textAt(stringArray[i2], f6, f3 + 90.0f * f4, 8.0f * f4, PayloadLoadingScreen.alpha(bl3 ? this.ACCENT : this.mutedC, f5));
            this.rect(f6, f3 + 106.0f * f4, f7, 2.0f * f4, f4, PayloadLoadingScreen.alpha(bl3 ? this.ACCENT : 0x26FFFFFF, f5));
        }
        if (!this.hint.isEmpty()) {
            this.text(this.fit(this.hint, f9, 8.0f * f4), f2, f3 + 121.0f * f4, 8.0f * f4, PayloadLoadingScreen.alpha(this.mutedC, f5), false);
        }
        if (this.signedIn) {
            this.drawLinks(f2, f3 + 144.0f * f4, f4, f5, n2, n3, true);
        } else {
            PayloadLoadingScreen.hide(this.retryLink);
            PayloadLoadingScreen.hide(this.copyLink);
        }
    }

    private void drawNeedsPlayer(float f2, float f3, float f4, float f5, int n2, int n3) {
        String string = switch (this.mode.ordinal()) {
            case 1 -> PayloadLoadingScreen.bf("\u096b\ubc67\u8ebd\uc5a5\u20ff\ua41e\ua6fb\u258f\ub3c9\u438d\uc88f\ucaf7\u02a1\ue059", 1326378153, 0x5703A517 ^ CryptoVault.mask());
            case 2 -> PayloadLoadingScreen.bf("\uc444\u39e8\uadfa\u5ad9\u36cc\u1566\ua259\u7291\u7c4f\u6ffe\ueb12\u24f4\uf8ee\u08ab\ubdbf\u41f9\u9ac9\uf01b\ucfec", 1996044132, 0x5703A517 ^ CryptoVault.mask());
            default -> PayloadLoadingScreen.bf("\ub6a6\ua424\u2171\u8bfc\u9842\u6241\u361b\u067a\u801b\u1113\u2db4\u87a2\ubc5e\u6f4b\u2eef\u2b1f\u60c0\ueb74\u9039\u27b1\uc372\ufeb2\u509a\uf72c\uaccc", -213185923, 0x5703A517 ^ CryptoVault.mask());
        };
        String string2 = switch (this.mode.ordinal()) {
            case 1 -> PayloadLoadingScreen.bf("\uc915\u3688\ua1fe\u60da\ue2c4\u2754\u6650\u4565\u4f9a\u070a\u39e1\uec3b\uebae\ud737\u3e06\u71f8\u8c1c\u7990\u7c32\uaab3\u8dd9\u763b\u1f99\u413b\u25ab\uba6f\ub9e8\ua9cb\u807e\ud8b1\u8f4e\u605d\u22c7\u75a5\u57ef\u0c41\u3c48\u42d7\ub35a\u37ce\udf6c", -1453915594, 0x5703A517 ^ CryptoVault.mask());
            case 2 -> PayloadLoadingScreen.bf("\ue683\u4c5c\u3b65\ud0b3\u1d64\ua96f\u4a03\u9cf4\uc639\u8b06\u225d\u88b7\ue3ec\u6078\ue6e8\uf4f2\ue8bd\ud95b\u167e\ufe3e\uaee1\u0d19\u3615\u6945\u2299\ub4d8\u51a2\u222b\u3e44\u5c46\uee4c\uaefe\ubdb1\u4a70\u8b7e\u0453\u98f9\u789d\u3495\u8ffd\u74c2\u4710\u4d42\u5894\u45bb\ufa24\uf63b\u79c3\u7b77\ub1b5", 427738232, 0x5703A517 ^ CryptoVault.mask());
            default -> PayloadLoadingScreen.bf("\u0148\ua507\u4c6f\uada2\u182b\ub8b2\u6391\u0b3c\ue2d6\u0c29\uc181\u51c1\u9edf\u17bd\uc2a4\uefac\u7d16\ubbc9\uef96\uff00\u333e\u1440\u81b4\u2bd7\u6b0a\u1035\u9f4b\uca0f\u075c\ue9aa\uf352\u3cdc\uf7b0\u9656\uee13\uffd2\uf831\u32ab\u0b51\u9244\u3b6c\ud1dd\u2743\u7992\u51ee\u772b\u4392", 1541815930, 0x5703A517 ^ CryptoVault.mask());
        };
        this.text(string, f2, f3 - 4.0f * f4, 12.0f * f4, PayloadLoadingScreen.alpha(this.textC, f5), true);
        this.text(this.fit(string2, Math.min(320.0f * f4, (float)this.field_22789 - 32.0f), 8.5f * f4), f2, f3 + 13.0f * f4, 8.5f * f4, PayloadLoadingScreen.alpha(this.mutedC, f5), false);
        String string3 = this.mode == Mode.SIGNED_OUT ? PayloadLoadingScreen.bf("\uc239\u1d6a\u145b\ua260\ud27b\u14af\u7bcb\u7e8a\u33b4\u4b09\u8bed", 2754565, 0x5703A517 ^ CryptoVault.mask()) : PayloadLoadingScreen.bf("\uf043\u5b21\u46c5\ua872\ud734\ub9e0\ud73f\uc562\ua27e\u796f", -688165340, 0x5703A517 ^ CryptoVault.mask());
        float f6 = 96.0f * f4;
        float f7 = 24.0f * f4;
        PayloadLoadingScreen.set(this.primary, f2 - f6 / 2.0f, f3 + 32.0f * f4, f6, f7);
        boolean bl = PayloadLoadingScreen.inside(this.primary, n2, n3);
        this.rect(this.primary[0], this.primary[1], f6, f7, f7 / 2.0f, PayloadLoadingScreen.alpha(bl ? this.ACCENT : this.panelC, f5));
        this.text(string3, f2, this.primary[1] + f7 / 2.0f - 4.5f * f4, 9.0f * f4, PayloadLoadingScreen.alpha(this.textC, f5), true);
        this.drawLinks(f2, this.primary[1] + f7 + 10.0f * f4, f4, f5, n2, n3, false);
    }

    private void drawLinks(float f2, float f3, float f4, float f5, int n2, int n3, boolean bl) {
        float f6 = 8.5f * f4;
        String string = PayloadLoadingScreen.bf("\u9ecf\u153a\u7acd\u5a8b\u78b3\uf353\ub550\u786c\ub04d\u2982", 1961659243, 0x5703A517 ^ CryptoVault.mask());
        String string2 = PayloadLoadingScreen.bf("\ubc5e\u863f\u49be\u070b\u00a7\u86e4\u1ddb", -696206207, 0x5703A517 ^ CryptoVault.mask());
        String string3 = PayloadLoadingScreen.bf("\u61ed\u9cb5\u611e\u0359\ue8db\udda7\u0146\u7f5e\u4975\u78a8\u4fb8\uda61\u7df9\u5349\u7d04\ud879\u4d27\udc3f\u8e79", -592658598, 0x5703A517 ^ CryptoVault.mask());
        float f7 = bl ? this.textWidth(string, f6) : 0.0f;
        float f8 = bl ? this.textWidth(string2, f6) : 0.0f;
        float f9 = this.textWidth(string3, f6);
        float f10 = f2 - (f7 + f8 + f9) / 2.0f;
        if (bl) {
            PayloadLoadingScreen.set(this.retryLink, f10, f3 - 3.0f * f4, f7, f6 + 6.0f * f4);
            this.textAt(string, f10, f3, f6, PayloadLoadingScreen.alpha(PayloadLoadingScreen.inside(this.retryLink, n2, n3) ? this.textC : this.mutedC, f5));
            this.textAt(string2, f10 + f7, f3, f6, PayloadLoadingScreen.alpha(-12827310, f5));
        } else {
            PayloadLoadingScreen.hide(this.retryLink);
        }
        PayloadLoadingScreen.set(this.copyLink, f10 + f7 + f8, f3 - 3.0f * f4, f9, f6 + 6.0f * f4);
        this.textAt(string3, f10 + f7 + f8, f3, f6, PayloadLoadingScreen.alpha(PayloadLoadingScreen.inside(this.copyLink, n2, n3) ? this.textC : this.mutedC, f5));
    }

    private void drawArc(float f2, float f3, float f4, float f5, float f6, int n2) {
        float f7 = f6 / 1.35f;
        float f8 = (float)Math.floor(f7);
        float f9 = f7 - f8;
        float f10 = PayloadLoadingScreen.ease(PayloadLoadingScreen.clamp(f9 * 2.0f, 0.0f, 1.0f));
        float f11 = PayloadLoadingScreen.ease(PayloadLoadingScreen.clamp(f9 * 2.0f - 1.0f, 0.0f, 1.0f));
        double d2 = (double)f6 * 250.0;
        double d3 = (double)f8 * 255.0 + (double)f11 * 255.0 + d2;
        double d4 = (double)f8 * 255.0 + 15.0 + (double)f10 * 255.0 + d2;
        double d5 = Math.toRadians(d3 - 90.0);
        double d6 = Math.toRadians(d4 - 90.0);
        double d7 = Math.max(0.35f, f5 * (this.plain ? 0.8f : 0.45f)) / f4;
        float f12 = f5 / 2.0f;
        double d8 = d5;
        while (true) {
            boolean bl = d8 >= d6;
            double d9 = bl ? d6 : d8;
            float f13 = f2 + (float)Math.cos(d9) * f4;
            float f14 = f3 + (float)Math.sin(d9) * f4;
            this.rect(f13 - f12, f14 - f12, f5, f5, f12, n2);
            if (bl) break;
            d8 += d7;
        }
    }

    public boolean method_25402(class_11909 class_119092, boolean bl) {
        if (class_119092.method_74245() == 0 && this.readyAt == 0L) {
            if (PayloadLoadingScreen.inside(this.primary, class_119092.comp_4798(), class_119092.comp_4799())) {
                if (this.mode == Mode.SIGNED_OUT) {
                    MobileSignIn.promptForSignIn(PayloadLoadingScreen.bf("\u177c\ud92f\u7925\u7a1a\u53da\uc760\u644a\u6c8f\u5def\u24a2\u4379\ub1e3\u80eb\ub6dc\u6b15\u5d47\u0963\u4741\ub77d\uafee\u43d2\u97b4\uf59a\u8d62\u4ee3\ub3f9\u5f10\u9de4\ufffb\u723d\u7128\u4a98\udff6\u882a\uf22e\uffed\u8c7e\u468e\u0e29\u192d\u4e42\u1aaf\u2846\u2300\u32e5\u5f3a\ucacc\u43f8", -1405535248, 0x5703A517 ^ CryptoVault.mask()));
                } else {
                    this.retry();
                }
                return true;
            }
            if (PayloadLoadingScreen.inside(this.retryLink, class_119092.comp_4798(), class_119092.comp_4799())) {
                this.retry();
                return true;
            }
            if (PayloadLoadingScreen.inside(this.copyLink, class_119092.comp_4798(), class_119092.comp_4799())) {
                if (this.field_22787 != null) {
                    this.field_22787.field_1774.method_1455(PayloadLoader.describeDiagnostics());
                }
                return true;
            }
        }
        return super.method_25402(class_119092, bl);
    }

    private void retry() {
        if (PayloadLoader.retryNow()) {
            this.mode = Mode.WAITING;
        }
    }

    private void rect(float f2, float f3, float f4, float f5, float f6, int n2) {
        if (n2 >>> 24 == 0) {
            return;
        }
        if (this.plain) {
            MobileUi.roundRect(this.context, Math.round(f2), Math.round(f3), Math.round(f4), Math.round(f5), Math.round(f6), n2);
        } else {
            this.list.rect(f2, f3, f4, f5, f6, n2);
        }
    }

    private void text(String string, float f2, float f3, float f4, int n2, boolean bl) {
        if (string == null || string.isEmpty()) {
            return;
        }
        float f5 = this.plain ? MobileUi.width(this.field_22793, string, f4 / 9.0f) : GpuUiRenderer.textWidth(string, f4, bl ? UiDrawCommand.TextWeight.SEMIBOLD : UiDrawCommand.TextWeight.REGULAR);
        this.drawText(string, f2 - f5 / 2.0f, f3, f4, n2, bl);
    }

    private void textAt(String string, float f2, float f3, float f4, int n2) {
        this.drawText(string, f2, f3, f4, n2, false);
    }

    private void drawText(String string, float f2, float f3, float f4, int n2, boolean bl) {
        if (n2 >>> 24 < 8) {
            return;
        }
        if (this.plain) {
            MobileUi.text(this.context, this.field_22793, string, f2, f3, f4 / 9.0f, n2);
        } else {
            this.list.text(f2, f3, f4, n2, string, bl ? UiDrawCommand.TextWeight.SEMIBOLD : UiDrawCommand.TextWeight.REGULAR);
        }
    }

    private String fit(String string, float f2, float f3) {
        if (string == null || string.isEmpty() || this.textWidth(string, f3) <= f2) {
            return string;
        }
        String string2 = string;
        while (string2.length() > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!(this.textWidth(stringBuilder.append(string2).append("..").toString(), f3) > f2)) break;
            string2 = string2.substring(0, string2.length() - 1);
        }
        return string2 + "..";
    }

    private float textWidth(String string, float f2) {
        return this.plain ? MobileUi.width(this.field_22793, string, f2 / 9.0f) : GpuUiRenderer.textWidth(string, f2, UiDrawCommand.TextWeight.REGULAR);
    }

    private static int alpha(int n2, float f2) {
        int n3 = Math.round((float)(n2 >>> 24 & 0xFF) * PayloadLoadingScreen.clamp(f2, 0.0f, 1.0f));
        return n3 << 24 | n2 & 0xFFFFFF;
    }

    private static float ease(float f2) {
        return f2 < 0.5f ? 4.0f * f2 * f2 * f2 : 1.0f - (float)Math.pow(-2.0f * f2 + 2.0f, 3.0) / 2.0f;
    }

    private static float clamp(float f2, float f3, float f4) {
        return f2 < f3 ? f3 : Math.min(f2, f4);
    }

    private static void set(float[] fArray, float f2, float f3, float f4, float f5) {
        fArray[0] = f2;
        fArray[1] = f3;
        fArray[2] = f4;
        fArray[3] = f5;
    }

    private static void hide(float[] fArray) {
        fArray[3] = 0.0f;
        fArray[2] = 0.0f;
    }

    private static boolean inside(float[] fArray, double d2, double d3) {
        return fArray[2] > 0.0f && d2 >= (double)fArray[0] && d2 <= (double)(fArray[0] + fArray[2]) && d3 >= (double)fArray[1] && d3 <= (double)(fArray[1] + fArray[3]);
    }

    private static /* synthetic */ String bf(String string, int n2, int n3) {
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
    private static enum Mode {
        WAITING,
        SIGNED_OUT,
        NOT_ENTITLED,
        FAILED;


        private static /* synthetic */ String cj(String string, int n2, int n3) {
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
}

