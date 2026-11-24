/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package com.ishland.scalablelux.gui.mobile;

import com.ishland.scalablelux.auth.MobileSignIn;
import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiRenderer;
import com.ishland.scalablelux.gui.mobile.MobileUi;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.ScissorClamp;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;

@Environment(value=EnvType.CLIENT)
public final class MobileSignInScreen
extends class_437 {
    private static final int TEXT = -920588;
    private static final int MUTED = -6512218;
    private static final int DIM = -10065040;
    private static final int ACCENT = -2170138;
    private static final int FIELD = 0x16FFFFFF;
    private static final int FIELD_HOVER = 0x20FFFFFF;
    private static final int FIELD_EDGE = 0x2EFFFFFF;
    private static final int INK = -15591909;
    private static final int MAX_EMAIL = 254;
    private static final int MAX_PASSWORD = 256;
    private final StringBuilder email = new StringBuilder();
    private final StringBuilder password = new StringBuilder();
    private final List<Hit> hits = new ArrayList<Hit>();
    private boolean passwordFocused;
    private boolean showPassword;
    private long openedAtMillis;
    private boolean plainRenderer;

    public MobileSignInScreen() {
        super((class_2561)class_2561.method_43470((String)MobileSignInScreen.oe("\u89d2\u31f7\uf4d8\u5cb6\u95ba\u8c91\uc2da\u1fa9\u17dc\u53ce\u3cd5\ueb66\ua7e6\uf49f\u899e", 1306212329, 0x22B8D7D5 ^ CryptoVault.mask())));
    }

    protected void method_25426() {
        if (this.openedAtMillis == 0L) {
            this.openedAtMillis = System.currentTimeMillis();
        }
    }

    public boolean method_25422() {
        return false;
    }

    public boolean method_25421() {
        return false;
    }

    protected void method_57734(class_332 class_3322) {
    }

    public void method_25420(class_332 class_3322, int n2, int n3, float f2) {
    }

    private static int alpha(int n2, float f2) {
        int n3 = n2 >>> 24 & 0xFF;
        return Math.round((float)n3 * Math.max(0.0f, Math.min(1.0f, f2))) << 24 | n2 & 0xFFFFFF;
    }

    public void method_25394(class_332 class_3322, int n2, int n3, float f2) {
        try {
            this.draw(class_3322, n2, n3, this.plainRenderer);
            return;
        }
        catch (Throwable throwable) {
            this.plainRenderer = true;
            try {
                this.draw(class_3322, n2, n3, true);
            }
            catch (Throwable throwable2) {
                try {
                    class_3322.method_25294(0, 0, this.field_22789, this.field_22790, -16053230);
                }
                catch (Throwable throwable3) {
                    // empty catch block
                }
            }
            return;
        }
    }

    private void draw(class_332 class_3322, int n2, int n3, boolean bl) {
        String string;
        float f2;
        float f3;
        float f4;
        this.hits.clear();
        Gfx gfx = bl ? new FillGfx(this, class_3322, this.field_22793) : new GpuGfx(this, class_3322);
        MobileSignIn.Phase phase = MobileSignIn.phase();
        boolean bl2 = phase == MobileSignIn.Phase.NEEDS_LOGIN || phase == MobileSignIn.Phase.SIGNING_IN;
        boolean bl3 = phase == MobileSignIn.Phase.SIGNING_IN;
        boolean bl4 = MobileSignIn.browserActive();
        boolean bl5 = this.field_22789 >= 400 && (float)this.field_22789 >= (float)this.field_22790 * 1.35f;
        float f5 = f4 = bl5 ? (float)Math.round((float)this.field_22789 * 0.46f) : 0.0f;
        float f6 = (float)this.field_22789 - f4;
        String string2 = MobileSignIn.message();
        float f7 = f3 = bl5 ? 0.0f : 52.0f;
        float f8 = bl4 ? 256.0f : (bl2 ? 46.0f + (string2.isEmpty() ? 0.0f : 32.0f) + 112.0f + 14.0f + 18.0f + 44.0f + 12.0f + 36.0f + 18.0f + 12.0f : 88.0f);
        float f9 = (float)this.field_22790 / (f3 + f8 + (bl2 && string2.isEmpty() ? 32.0f : 0.0f) + 26.0f + 10.0f);
        float f10 = Math.max(0.5f, Math.min(2.0f, Math.min(Math.min((float)this.field_22790 / 360.0f, f6 / 300.0f), f9)));
        float f11 = Math.min(1.0f, (float)(System.currentTimeMillis() - this.openedAtMillis) / 320.0f);
        float f12 = 1.0f - (1.0f - f11) * (1.0f - f11);
        float f13 = (1.0f - f12) * 14.0f * f10;
        gfx.backdrop(this.field_22789, this.field_22790);
        gfx.card(0.0f, 0.0f, this.field_22789, this.field_22790, 0.0f, n2, n3);
        if (bl5) {
            gfx.gradient(f5, 0.0f, f6, this.field_22790, 0.0f, 0x38000000, 0x70000000);
            gfx.rect(f5, 0.0f, Math.max(1.0f, f10 * 0.8f), this.field_22790, 0.0f, 0x26FFFFFF);
            this.drawHero(gfx, 0.0f, f4, f10, f12);
        } else {
            gfx.gradient(0.0f, 0.0f, this.field_22789, this.field_22790, 0.0f, 0x30000000, 0x68000000);
        }
        float f14 = Math.min(f6 - 40.0f * f10, 340.0f * f10);
        float f15 = f5 + (f6 - f14) / 2.0f;
        float f16 = 26.0f * f10;
        float f17 = f3 * f10;
        float f18 = f17 + f8 * f10;
        float f19 = Math.max(10.0f * f10, ((float)this.field_22790 - f16 - f18) / 2.0f) + f13;
        if (!bl5) {
            f2 = 34.0f * f10;
            gfx.shadow(f15, f19 + 2.0f * f10, f2, f2, 10.0f * f10, 16.0f * f10, MobileSignInScreen.alpha(1356784358, f12));
            gfx.gradient(f15, f19, f2, f2, 10.0f * f10, -789002, -6643290);
            gfx.logo(f15 + 5.0f * f10, f19 + 5.0f * f10, 24.0f * f10, -15591909);
            gfx.text(f15 + f2 + 11.0f * f10, f19 + 9.0f * f10, 16.0f * f10, -920588, MobileSignInScreen.oe("\u17b9\ue49d\u292a\ud563\u5abf", 844487231, 0x22B8D7D5 ^ CryptoVault.mask()), true);
            f19 += f17;
        }
        if (bl4) {
            gfx.text(f15, f19, 26.0f * f10, MobileSignInScreen.alpha(-920588, f12), MobileSignInScreen.oe("\u10e9\ue16d\u5710\u0825\u9f7d\u175b\ud769\u390c\u7c01\ua759\u710c", 2130349239, 0x22B8D7D5 ^ CryptoVault.mask()), true);
            String string3 = MobileSignInScreen.oe("\ub4fe\ue812\u21c9\u416f\u6f5d\ua87f\ubdb8\ucc16\u4b35\ud63c\u0531\u5ba9\u8013\u5fc9\f\u412f\u2ae8\u0a21\u1eaf\u945a\u2ebf\ub221\ube0b\uae0e\u9e66\u737c\ue17a\u2b9f", -1941937206, 0x22B8D7D5 ^ CryptoVault.mask());
            gfx.text(f15, f19 += 46.0f * f10, 10.5f * f10, -6512218, MobileSignInScreen.fitText(gfx, MobileSignInScreen.oe("\udafd\u3d48\u7940\u0c39\u87b7\ub3da\u878c\u9663", 351843320, 0x22B8D7D5 ^ CryptoVault.mask()) + string3 + MobileSignInScreen.oe("\u43ef\u5a8b\uf684\ua84c\ua10d\u2fb4\u61e1\u4edc\u926a\u6dc4\u0799\u6cd8\u87f6\u8b98\u6c86\udf15\u1c95\ucb05\uc329", 2129054522, 0x22B8D7D5 ^ CryptoVault.mask()), f14, 10.5f * f10), false);
            gfx.text(f15, f19 += 15.0f * f10, 10.5f * f10, -6512218, MobileSignInScreen.fitText(gfx, MobileSignInScreen.oe("\u4031\u4733\u3b91\u9d31\ub91e\ua7a4\uf527\u241e\u4b2c\uf707\uab15\udb04\u72cc\u1754\u2ba4\u89b7\uc609\u86b4\udb97\u02a0\u6a3e\u8ece\u8b6e\uc75d\udf3e\ubdef\ucdc7\u919e\uc9bc\u91de\ue802\u1684\ufaba\ucd90", 1590823741, 0x22B8D7D5 ^ CryptoVault.mask()), f14, 10.5f * f10), false);
            f19 += 19.0f * f10;
            string = MobileSignIn.browserLinkCode();
            String string4 = MobileSignIn.browserCode();
            if (string4.isEmpty()) {
                gfx.text(f15, f19 + 12.0f * f10, 13.0f * f10, -920588, MobileSignInScreen.dots(MobileSignIn.status().isEmpty() ? MobileSignInScreen.oe("\u3eb9\u991f\ub9cf\u190f\uf080\u017a\u2d9b\u1fe0\u790e\ubc0e\uc37a\u6ea2", -1719380881, 0x22B8D7D5 ^ CryptoVault.mask()) : MobileSignIn.status()), true);
                f19 += 40.0f * f10 + 12.0f * f10 + 16.0f * f10 + 40.0f * f10;
            } else {
                f19 = this.codeBox(gfx, MobileSignInScreen.oe("\uf0aa\u3be9\u26f2\u0612\u67f8\u46d5\u02ec\ua831\u5d29\u8061", -314631769, 0x22B8D7D5 ^ CryptoVault.mask()), string.isEmpty() ? "-" : string.substring(0, 4) + " " + string.substring(4), f15, f19, f14, f10);
                f19 += 12.0f * f10;
                f19 = this.codeBox(gfx, MobileSignInScreen.oe("\uc05e\u9263\uabbe\u011ad\u9346\ub526\ud5e6\u7a9b\u336c\uee40\udf38\uf390", 1013045446, 0x22B8D7D5 ^ CryptoVault.mask()), string4.length() == 6 ? string4.substring(0, 3) + " " + string4.substring(3) : string4, f15, f19, f14, f10);
            }
            gfx.text(f15, f19 += 6.0f * f10, 9.5f * f10, -10065040, MobileSignInScreen.fitText(gfx, MobileSignIn.browserUrl().isEmpty() ? "" : MobileSignInScreen.oe("\u54d0\uda88\ud389\u7c36\u333b\uc270\u0645\ue05c\u3946\ud2a6\u7563\u8541\u183f\ub2c7\u820d\u320f\ub03a\ue613\u41b2\u9649\u45be\u250b\u78fe\u54d7\u6661\ub6b0\ud3c6\u96bd\u9942\u45ff\uc64b\ud307\ubfb8\udc8c\ud4a3\u6d01\u4a9e\u18f6\u5471\uc3a1\u46a7\u704c\u5a8a\u91be\u8cc7", -1023210549, 0x22B8D7D5 ^ CryptoVault.mask()), f14, 9.5f * f10), false);
            this.textButton(gfx, MobileSignInScreen.oe("\uc9bc\u38c0\ub48c\u6e97\u3449\ua62f\uc7b8\u7fbf", 97622125, 0x22B8D7D5 ^ CryptoVault.mask()), f15, f19 += 16.0f * f10, f14, f10, n2, n3, MobileSignIn::cancelBrowser);
        } else if (bl2) {
            gfx.text(f15, f19, 26.0f * f10, MobileSignInScreen.alpha(-920588, f12), MobileSignInScreen.oe("\u5a10\uf6da\u6ca1\u05fa\u5194\uea46\uf4d1\uf614\u85fe", 1869729598, 0x22B8D7D5 ^ CryptoVault.mask()), true);
            f19 += 46.0f * f10;
            if (!string2.isEmpty()) {
                f2 = 24.0f * f10;
                gfx.rect(f15, f19 + 2.0f * f10, f14, f2, 8.0f * f10, 585454411);
                gfx.border(f15, f19 + 2.0f * f10, f14, f2, 8.0f * f10, Math.max(1.0f, f10), 1726305099);
                String string5 = MobileSignInScreen.fitText(gfx, string2, f14 - 20.0f * f10, 10.0f * f10);
                gfx.text(f15 + 10.0f * f10, f19 + 2.0f * f10 + (f2 - 10.0f * f10) / 2.0f - 0.5f * f10, 10.0f * f10, -30078, string5, false);
                f19 += 32.0f * f10;
            }
            f19 = this.field(gfx, MobileSignInScreen.oe("\uf8fb\udd67\uc9d5\uc3cf\u1335\u67d5\uceea", 2100187743, 0x22B8D7D5 ^ CryptoVault.mask()), this.email, false, f15, f19, f14, f10, !this.passwordFocused, bl3, n2, n3);
            f19 += 14.0f * f10;
            f19 = this.field(gfx, MobileSignInScreen.oe("\ubae4\ua995\uf1e3\u6fed\u3dce\u4cf6\ube25\u097f\u707c\u2c2e\u976b", 857406602, 0x22B8D7D5 ^ CryptoVault.mask()), this.password, true, f15, f19, f14, f10, this.passwordFocused, bl3, n2, n3);
            f2 = 44.0f * f10;
            boolean bl6 = !bl3 && this.email.length() > 0 && this.password.length() > 0;
            boolean bl7 = bl6 && (float)n2 >= f15 && (float)n2 < f15 + f14 && (float)n3 >= (f19 += 18.0f * f10) && (float)n3 < f19 + f2;
            gfx.shadow(f15, f19 + 3.0f * f10, f14, f2, 11.0f * f10, 18.0f * f10, MobileSignInScreen.alpha(bl6 ? 1088348902 : 0, f12));
            if (bl6) {
                gfx.gradient(f15, f19, f14, f2, 11.0f * f10, bl7 ? -1 : -789002, bl7 ? -3551533 : -6643290);
            } else {
                gfx.rect(f15, f19, f14, f2, 11.0f * f10, 0x22FFFFFF);
            }
            String string6 = bl3 ? MobileSignInScreen.dots(MobileSignInScreen.oe("\uc3a6\ud7a0\u91f9\uf021\ud333\u2c32\ue676\u4fa0\u6dbb\u1b62\u56a9\ub934\u7ff0", 2096008640, 0x22B8D7D5 ^ CryptoVault.mask())) : MobileSignInScreen.oe("\u171a\u0a9d\ued66\u86e4\u7421\u0f01\ue1f1\u4efe\u0929", 961416341, 0x22B8D7D5 ^ CryptoVault.mask());
            float f20 = gfx.width(string6, 13.0f * f10, true);
            gfx.text(f15 + (f14 - f20) / 2.0f, f19 + (f2 - 13.0f * f10) / 2.0f - 0.5f * f10, 13.0f * f10, bl6 ? -15591909 : -6512218, string6, true);
            if (bl6) {
                this.hits.add(new Hit(f15, f19, f14, f2, this::submit));
            }
            f19 += f2 + 12.0f * f10;
            if (!bl3) {
                this.textButton(gfx, MobileSignInScreen.oe("\u83ca\uaba0\ue388\uc4b9\ubdc5\ub759\ub4f8\u7344\ua2d9\uafd0\u7d63\u445e\ua4a3\u29ed\u72bd\u876f\u9a9f\uda42\u1486\u9b18\u7b3d\ua850", -971066437, 0x22B8D7D5 ^ CryptoVault.mask()), f15, f19, f14, f10, n2, n3, MobileSignIn::beginBrowser);
            }
            String string7 = MobileSignInScreen.oe("\u023b\u9d34\u5aa8\u4b4e\u760d\ua940\ua7fd\u2fb4\uaff9\u43fe\u7ba0\u4d14\u9f09\u6872\u42b5\u8ae4\uc734\ua152\u2cca\u1de0\u23a4\u1f4f\u8cd1\u30c4\u6e80\u9a1d\u6646\u81c1\u1b33\u3d87\u6b62\uffab\u468c\ue026\u4c6a\u077f\u829a\ue945", 1974658611, 0x22B8D7D5 ^ CryptoVault.mask());
            string7 = MobileSignInScreen.fitText(gfx, string7, f14, 9.5f * f10);
            float f21 = gfx.width(string7, 9.5f * f10, false);
            gfx.text(f15 + (f14 - f21) / 2.0f, f19 += 36.0f * f10 + 18.0f * f10, 9.5f * f10, -10065040, string7, false);
        } else {
            String string8 = MobileSignIn.status();
            gfx.text(f15, f19, 20.0f * f10, -920588, string8.isEmpty() ? MobileSignInScreen.oe("\ubebc\u7bcc\u3fca\u4e94\uf307\u78f3\u350f\u7c6c\u7a2c\u8aa9\uc4ef\ud067\u6f4e\u40bf\uc33f\u4355\uae38\u3d9c\u7626", -1975785743, 0x22B8D7D5 ^ CryptoVault.mask()) : MobileSignInScreen.fitText(gfx, string8, f14, 20.0f * f10), true);
            gfx.text(f15, f19 += 30.0f * f10, 11.0f * f10, -6512218, MobileSignInScreen.oe("\u5a60\u1288\u2245\u068b\u4504\u291f\u30b3\ua719\u9cd3\u8d9c\u4c34\u35b0\u4a88\udf4e\u7c25\uad69\u4f13\u622a\u844f\u45bf\u4003\u1da4\uedea\u1f65\u8691\u1972", 117409311, 0x22B8D7D5 ^ CryptoVault.mask()), false);
            this.textButton(gfx, MobileSignInScreen.oe("\u089d\uad57\u45e0\u5a94\u5212\u3932\u12d7\u410c\u3ef6\uaad7\u9fa3\u06a3\uad6b\ub325\u9890\u7236\u77c3\u0e41\uc80a\uf748\u5484\u6d33\u0865\uaede\u7b8b", 1247780901, 0x22B8D7D5 ^ CryptoVault.mask()), f15, f19 += 22.0f * f10, f14, f10, n2, n3, MobileSignIn::useDifferentAccount);
        }
        float f22 = (float)this.field_22790 - 18.0f * f10;
        string = MobileSignInScreen.oe("\u5a02\u7290\udb5b\ub9c2\u1c19\u3e65\ubd95\ub18b\u8a8d\ub0bd\udb12\u0702", 155208900, 0x22B8D7D5 ^ CryptoVault.mask());
        float f23 = gfx.width(string, 10.0f * f10, false);
        boolean bl8 = (float)n2 >= f15 - 6.0f * f10 && (float)n2 < f15 + f23 + 6.0f * f10 && (float)n3 >= f22 - 6.0f * f10 && (float)n3 < f22 + 16.0f * f10;
        gfx.text(f15, f22, 10.0f * f10, bl8 ? -920588 : -6512218, string, false);
        this.hits.add(new Hit(f15 - 6.0f * f10, f22 - 6.0f * f10, f23 + 12.0f * f10, 22.0f * f10, () -> {
            if (this.field_22787 != null) {
                this.field_22787.method_1592();
            }
        }));
        gfx.flush();
    }

    private void drawHero(Gfx gfx, float f2, float f3, float f4, float f5) {
        float f6 = Math.min(72.0f * f4, f3 * 0.4f);
        float f7 = 30.0f * f4;
        float f8 = f6 + 22.0f * f4 + f7;
        float f9 = f2 + f3 / 2.0f;
        float f10 = ((float)this.field_22790 - f8) / 2.0f;
        float f11 = f6 / 2.0f;
        gfx.circle(f9, f10 + f11, f6 * 1.25f, MobileSignInScreen.alpha(283042534, f5));
        gfx.shadow(f9 - f11, f10 + 6.0f * f4, f6, f6, f6 * 0.28f, 44.0f * f4, MobileSignInScreen.alpha(1725883110, f5));
        gfx.gradient(f9 - f11, f10, f6, f6, f6 * 0.28f, -789002, -7366755);
        gfx.border(f9 - f11, f10, f6, f6, f6 * 0.28f, Math.max(1.0f, f4), MobileSignInScreen.alpha(0x66FFFFFF, f5));
        gfx.logo(f9 - f11 + f6 * 0.14f, f10 + f6 * 0.14f, f6 * 0.72f, -15591909);
        float f12 = gfx.width(MobileSignInScreen.oe("\u3608\u8971\u3c4c\udd8b\u20f6\u1b76", -1864634254, 0x22B8D7D5 ^ CryptoVault.mask()), f7, true);
        gfx.text(f9 - f12 / 2.0f, f10 += f6 + 22.0f * f4, f7, MobileSignInScreen.alpha(-920588, f5), MobileSignInScreen.oe("\u5ecc\ufbc0\u92c1\u3569\uae53", 1760638164, 0x22B8D7D5 ^ CryptoVault.mask()), true);
    }

    private float field(Gfx gfx, String string, StringBuilder stringBuilder, boolean bl, float f2, float f3, float f4, float f5, boolean bl2, boolean bl3, int n2, int n3) {
        gfx.text(f2, f3, 10.0f * f5, -6512218, string, false);
        float f6 = 42.0f * f5;
        boolean bl4 = !bl3 && (float)n2 >= f2 && (float)n2 < f2 + f4 && (float)n3 >= (f3 += 14.0f * f5) && (float)n3 < f3 + f6;
        gfx.rect(f2, f3, f4, f6, 9.0f * f5, bl4 && !bl2 ? 0x20FFFFFF : 0x16FFFFFF);
        gfx.border(f2, f3, f4, f6, 9.0f * f5, Math.max(1.0f, f5), bl2 && !bl3 ? MobileSignInScreen.alpha(-2170138, 0.85f) : 0x2EFFFFFF);
        float f7 = f2 + 12.0f * f5;
        float f8 = f3 + (f6 - 12.0f * f5) / 2.0f - 0.5f * f5;
        float f9 = 0.0f;
        if (bl) {
            String string2 = this.showPassword ? MobileSignInScreen.oe("\u74d6\u1626\uf309\u1be2\u30c7\u297d\u7c84\ua4ef", 1326500777, 0x22B8D7D5 ^ CryptoVault.mask()) : MobileSignInScreen.oe("\u176b\uc699\u3ef9\u1145\ua55e", -2136034163, 0x22B8D7D5 ^ CryptoVault.mask());
            f9 = gfx.width(string2, 9.5f * f5, true) + 8.0f * f5;
            float f10 = f2 + f4 - f9 - 6.0f * f5;
            boolean bl5 = (float)n2 >= f10 && (float)n2 < f10 + f9 + 6.0f * f5 && (float)n3 >= f3 && (float)n3 < f3 + f6;
            gfx.text(f10 + 4.0f * f5, f3 + (f6 - 9.5f * f5) / 2.0f - 0.5f * f5, 9.5f * f5, bl5 ? -920588 : -6512218, string2, true);
            this.hits.add(new Hit(f10, f3, f9 + 6.0f * f5, f6, () -> {
                this.showPassword = !this.showPassword;
            }));
        }
        float f11 = f4 - 24.0f * f5 - f9;
        gfx.clip(f7, f3, f11, f6, 0.0f);
        String string3 = stringBuilder.toString();
        long l2 = System.currentTimeMillis() / 500L;
        if (bl && !this.showPassword) {
            int n4;
            var22_24 = 2.7f * f5;
            float f12 = 9.0f * f5;
            int n5 = Math.max(1, (int)((f11 - 6.0f * f5) / f12));
            int n6 = stringBuilder.codePointCount(0, stringBuilder.length());
            for (int i2 = n4 = Math.max(0, n6 - n5); i2 < n6; ++i2) {
                gfx.circle(f7 + (float)(i2 - n4) * f12 + var22_24, f3 + f6 / 2.0f, var22_24, -920588);
            }
            if (bl2 && !bl3 && l2 % 2L == 0L) {
                gfx.rect(f7 + (float)(n6 - n4) * f12 + 1.0f * f5, f3 + 10.0f * f5, Math.max(1.0f, f5), f6 - 20.0f * f5, 0.0f, -920588);
            }
        } else {
            var22_24 = gfx.width(string3, 12.0f * f5, false);
            float f13 = Math.min(0.0f, f11 - var22_24 - 3.0f * f5);
            if (string3.isEmpty()) {
                gfx.text(f7, f8, 12.0f * f5, -10065040, bl ? MobileSignInScreen.oe("\u1188\u955f\u4fae\u9050\uef74\uc75f\u0524\uc11b\u40dd\u1de1\udcc4\ub249\u3cf3\u34e1\u8844", 1882076468, 0x22B8D7D5 ^ CryptoVault.mask()) : MobileSignInScreen.oe("\ud9c6\u03c9\u570f\ud1ca\u5596\ud0c7\u170b\u3b35\ue150\ucc4d\udab7\u1ff7\u17b5\u1429\u12de\ufa8b\udd68\ue5da", -1837138038, 0x22B8D7D5 ^ CryptoVault.mask()), false);
            } else {
                gfx.text(f7 + f13, f8, 12.0f * f5, -920588, string3, false);
            }
            if (bl2 && !bl3 && l2 % 2L == 0L) {
                gfx.rect(f7 + Math.max(0.0f, var22_24 + f13) + 1.0f * f5, f3 + 10.0f * f5, Math.max(1.0f, f5), f6 - 20.0f * f5, 0.0f, -920588);
            }
        }
        gfx.unclip();
        if (!bl3) {
            boolean bl6 = bl;
            this.hits.add(new Hit(f2, f3, f4 - (bl ? f9 + 6.0f * f5 : 0.0f), f6, () -> {
                this.passwordFocused = bl6;
            }));
        }
        return f3 + f6;
    }

    private float codeBox(Gfx gfx, String string, String string2, float f2, float f3, float f4, float f5) {
        gfx.text(f2, f3, 10.0f * f5, -6512218, string, false);
        float f6 = 40.0f * f5;
        gfx.rect(f2, f3 += 14.0f * f5, f4, f6, 9.0f * f5, 0x16FFFFFF);
        gfx.border(f2, f3, f4, f6, 9.0f * f5, Math.max(1.0f, f5), 0x2EFFFFFF);
        float f7 = gfx.width(string2, 18.0f * f5, true);
        gfx.text(f2 + (f4 - f7) / 2.0f, f3 + (f6 - 18.0f * f5) / 2.0f - 0.5f * f5, 18.0f * f5, -920588, string2, true);
        return f3 + f6;
    }

    private float textButton(Gfx gfx, String string, float f2, float f3, float f4, float f5, int n2, int n3, Runnable runnable) {
        float f6 = 36.0f * f5;
        boolean bl = (float)n2 >= f2 && (float)n2 < f2 + f4 && (float)n3 >= f3 && (float)n3 < f3 + f6;
        gfx.rect(f2, f3, f4, f6, 9.0f * f5, bl ? 0x20FFFFFF : 0x16FFFFFF);
        gfx.border(f2, f3, f4, f6, 9.0f * f5, Math.max(1.0f, f5), 0x2EFFFFFF);
        float f7 = gfx.width(string, 11.0f * f5, true);
        gfx.text(f2 + (f4 - f7) / 2.0f, f3 + (f6 - 11.0f * f5) / 2.0f - 0.5f * f5, 11.0f * f5, -920588, string, true);
        this.hits.add(new Hit(f2, f3, f4, f6, runnable));
        return f3 + f6;
    }

    private static String dots(String string) {
        return string + ".".repeat((int)(System.currentTimeMillis() / 400L % 4L));
    }

    private static String fitText(Gfx gfx, String string, float f2, float f3) {
        if (gfx.width(string, f3, false) <= f2) {
            return string;
        }
        String string2 = string;
        while (string2.length() > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            if (!(gfx.width(stringBuilder.append(string2).append("..").toString(), f3, false) > f2)) break;
            string2 = string2.substring(0, string2.length() - 1);
        }
        return string2 + "..";
    }

    private void submit() {
        MobileSignIn.submit(this.email.toString(), this.password.toString());
    }

    private static void removeLastCodePoint(StringBuilder stringBuilder) {
        if (stringBuilder.length() == 0) {
            return;
        }
        int n2 = stringBuilder.offsetByCodePoints(stringBuilder.length(), -1);
        stringBuilder.setLength(n2);
    }

    public boolean method_25402(class_11909 class_119092, boolean bl) {
        if (class_119092.method_74245() != 0) {
            return true;
        }
        for (int i2 = this.hits.size() - 1; i2 >= 0; --i2) {
            Hit hit = this.hits.get(i2);
            if (!hit.contains(class_119092.comp_4798(), class_119092.comp_4799())) continue;
            hit.action().run();
            return true;
        }
        return true;
    }

    public boolean method_25404(class_11908 class_119082) {
        StringBuilder stringBuilder;
        boolean bl = MobileSignIn.phase() == MobileSignIn.Phase.NEEDS_LOGIN;
        int n2 = class_119082.comp_4795();
        if (n2 == 258) {
            if (bl) {
                this.passwordFocused = !this.passwordFocused;
            }
            return true;
        }
        if (n2 == 257 || n2 == 335) {
            if (bl) {
                if (!this.passwordFocused && this.email.length() > 0) {
                    this.passwordFocused = true;
                } else {
                    this.submit();
                }
            }
            return true;
        }
        if (!bl) {
            return true;
        }
        StringBuilder stringBuilder2 = stringBuilder = this.passwordFocused ? this.password : this.email;
        if (n2 == 259) {
            MobileSignInScreen.removeLastCodePoint(stringBuilder);
            return true;
        }
        if (class_119082.method_74243()) {
            String string;
            String string2 = string = this.field_22787 == null ? "" : this.field_22787.field_1774.method_1460();
            if (string != null) {
                String string3 = string.replaceAll(MobileSignInScreen.oe("\u0919\u17ee\u415a\u1cc6\u2af1\ud1b0\u2a21\u4047\u5579\u064a\u66f3\u09e3\u88ca\u6a89", 189408494, 0x22B8D7D5 ^ CryptoVault.mask()), "").strip();
                int n3 = (this.passwordFocused ? 256 : 254) - stringBuilder.length();
                if (n3 > 0) {
                    stringBuilder.append(string3, 0, Math.min(string3.length(), n3));
                }
            }
            return true;
        }
        return true;
    }

    public boolean method_25400(class_11905 class_119052) {
        StringBuilder stringBuilder;
        if (MobileSignIn.phase() != MobileSignIn.Phase.NEEDS_LOGIN) {
            return true;
        }
        int n2 = class_119052.comp_4793();
        if (!Character.isValidCodePoint(n2) || Character.isISOControl(n2)) {
            return true;
        }
        StringBuilder stringBuilder2 = stringBuilder = this.passwordFocused ? this.password : this.email;
        if (!this.passwordFocused && Character.isWhitespace(n2)) {
            return true;
        }
        if (stringBuilder.length() < (this.passwordFocused ? 256 : 254)) {
            stringBuilder.appendCodePoint(n2);
        }
        return true;
    }

    private static /* synthetic */ String oe(String string, int n2, int n3) {
        int n4;
        long l2;
        int n5;
        int n6;
        char[] cArray = string.toCharArray();
        if (cArray.length == 0) {
            return "";
        }
        long l3 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
        l3 ^= 0x9E3779B97F4A7C15L;
        l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
        l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
        l3 ^= l3 >>> 31;
        long l4 = l3 ^ 0xA5A5A5A5A5A5A5A5L;
        l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
        l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
        if ((n6 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
            n6 = 19997;
        }
        long l5 = l3 ^ 0x5A5A5A5A5A5A5A5AL;
        l5 = (l5 ^ l5 >>> 30) * -4658895280553007687L;
        l5 = (l5 ^ l5 >>> 27) * -7723592293110705685L;
        l5 ^= l5 >>> 31;
        int n7 = (int)(l5 >>> 16) & 0xFFFF;
        int n8 = 1;
        for (n5 = 0; n5 < 5; ++n5) {
            n8 *= 2 - n6 * n8;
        }
        n5 = n8 & 0xFFFF;
        long l6 = (l3 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        long l7 = l2 = (l3 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        l7 = (l7 ^ l7 >>> 30) * -4658895280553007687L;
        l7 = (l7 ^ l7 >>> 27) * -7723592293110705685L;
        int n9 = n5 * (cArray[0] - n7 & 0xFFFF) & 0xFFFF;
        if ((n4 = n9 ^ (int)((l7 ^= l7 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
            return string;
        }
        char[] cArray2 = new char[n4];
        int n10 = 0;
        for (int i2 = 0; i2 < cArray.length - 1; ++i2) {
            int n11 = n5 * (cArray[i2 + 1] - n7 & 0xFFFF) & 0xFFFF;
            long l8 = l6 ^ (long)n10 * -7046029254386353131L;
            l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
            l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
            l8 ^= l8 >>> 31;
            int n12 = n11 ^ (int)(l8 >>> 48) & 0xFFFF;
            l6 = l6 * 6364136223846793005L + 1442695040888963407L;
            if (i2 < n4) {
                cArray2[i2] = (char)n12;
            }
            n10 = n11;
        }
        return new String(cArray2);
    }

    @Environment(value=EnvType.CLIENT)
    private final class FillGfx
    implements Gfx {
        private final class_332 c;
        private final class_327 tr;

        FillGfx(MobileSignInScreen mobileSignInScreen, class_332 class_3322, class_327 class_3272) {
            this.c = class_3322;
            this.tr = class_3272;
        }

        @Override
        public void backdrop(float f2, float f3) {
            this.c.method_25294(0, 0, (int)f2, (int)f3, -267711470);
        }

        @Override
        public void card(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            MobileUi.roundRect(this.c, (int)f2, (int)f3, (int)f4, (int)f5, (int)f6, -15329505);
        }

        @Override
        public void rect(float f2, float f3, float f4, float f5, float f6, int n2) {
            MobileUi.roundRect(this.c, (int)f2, (int)f3, (int)f4, (int)f5, (int)f6, n2);
        }

        @Override
        public void gradient(float f2, float f3, float f4, float f5, float f6, int n2, int n3) {
            MobileUi.roundRect(this.c, (int)f2, (int)f3, (int)f4, (int)f5, (int)f6, n2);
        }

        @Override
        public void border(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            int n3 = (int)f2;
            int n4 = (int)f3;
            int n5 = (int)f4;
            int n6 = (int)f5;
            int n7 = Math.max(1, (int)f7);
            this.c.method_25294(n3, n4, n3 + n5, n4 + n7, n2);
            this.c.method_25294(n3, n4 + n6 - n7, n3 + n5, n4 + n6, n2);
            this.c.method_25294(n3, n4, n3 + n7, n4 + n6, n2);
            this.c.method_25294(n3 + n5 - n7, n4, n3 + n5, n4 + n6, n2);
        }

        @Override
        public void shadow(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
        }

        @Override
        public void circle(float f2, float f3, float f4, int n2) {
            MobileUi.roundRect(this.c, (int)(f2 - f4), (int)(f3 - f4), (int)(f4 * 2.0f), (int)(f4 * 2.0f), (int)f4, n2);
        }

        @Override
        public void text(float f2, float f3, float f4, int n2, String string, boolean bl) {
            MobileUi.text(this.c, this.tr, string, f2, f3, f4 / 9.0f, n2);
        }

        @Override
        public float width(String string, float f2, boolean bl) {
            return MobileUi.width(this.tr, string, f2 / 9.0f);
        }

        @Override
        public void clip(float f2, float f3, float f4, float f5, float f6) {
            ScissorClamp.enable(this.c, (int)f2, (int)f3, (int)(f2 + f4), (int)(f3 + f5));
        }

        @Override
        public void unclip() {
            this.c.method_44380();
        }

        @Override
        public void logo(float f2, float f3, float f4, int n2) {
            MobileUi.text(this.c, this.tr, "A", f2 + f4 * 0.28f, f3 + f4 * 0.12f, f4 / 9.0f * 0.8f, n2);
        }

        @Override
        public void flush() {
        }
    }

    @Environment(value=EnvType.CLIENT)
    private final class GpuGfx
    implements Gfx {
        private final class_332 context;
        private final UiDisplayList.Builder b = UiDisplayList.builder();

        GpuGfx(MobileSignInScreen mobileSignInScreen, class_332 class_3322) {
            this.context = class_3322;
        }

        @Override
        public void backdrop(float f2, float f3) {
            this.b.blur(0.0f, 0.0f, f2, f3, 0.0f, 7.0f);
            this.b.gradient(0.0f, 0.0f, f2, f3, 0.0f, 0x55000000, 0x55000000, -1342177280, -1342177280);
        }

        @Override
        public void card(float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            this.b.material(f2, f3, f4, f5, f6, UiThemeMaterial.forTheme(GpuGfx.oe("\ud107\uc6e7\u4e8f\u8a86\u35fe\u062f\u55bf\uf112", 1570681552, 0x5AB7EF04 ^ CryptoVault.mask())));
        }

        @Override
        public void rect(float f2, float f3, float f4, float f5, float f6, int n2) {
            this.b.rect(f2, f3, f4, f5, f6, n2);
        }

        @Override
        public void gradient(float f2, float f3, float f4, float f5, float f6, int n2, int n3) {
            this.b.gradient(f2, f3, f4, f5, f6, n2, n2, n3, n3);
        }

        @Override
        public void border(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.b.border(f2, f3, f4, f5, f6, f7, n2);
        }

        @Override
        public void shadow(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.b.shadow(f2, f3, f4, f5, f6, f7, 0.0f, n2);
        }

        @Override
        public void circle(float f2, float f3, float f4, int n2) {
            this.b.circle(f2, f3, f4, n2);
        }

        @Override
        public void text(float f2, float f3, float f4, int n2, String string, boolean bl) {
            this.b.text(f2, f3, f4, n2, string, bl ? UiDrawCommand.TextWeight.SEMIBOLD : UiDrawCommand.TextWeight.REGULAR);
        }

        @Override
        public float width(String string, float f2, boolean bl) {
            return GpuUiRenderer.textWidth(string, f2, bl ? UiDrawCommand.TextWeight.SEMIBOLD : UiDrawCommand.TextWeight.REGULAR);
        }

        @Override
        public void clip(float f2, float f3, float f4, float f5, float f6) {
            this.b.clipPush(f2, f3, f4, f5, f6);
        }

        @Override
        public void unclip() {
            this.b.clipPop();
        }

        @Override
        public void logo(float f2, float f3, float f4, int n2) {
            ClassicIcons.drawLogo(this.b, f2, f3, f4, n2);
        }

        @Override
        public void flush() {
            GpuUiRenderer.render(this.context, this.b.build());
        }

        private static /* synthetic */ String oe(String string, int n2, int n3) {
            int n4;
            long l2;
            int n5;
            int n6;
            char[] cArray = string.toCharArray();
            if (cArray.length == 0) {
                return "";
            }
            long l3 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
            l3 ^= 0x9E3779B97F4A7C15L;
            l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
            l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
            l3 ^= l3 >>> 31;
            long l4 = l3 ^ 0xA5A5A5A5A5A5A5A5L;
            l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
            l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
            if ((n6 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
                n6 = 19997;
            }
            long l5 = l3 ^ 0x5A5A5A5A5A5A5A5AL;
            l5 = (l5 ^ l5 >>> 30) * -4658895280553007687L;
            l5 = (l5 ^ l5 >>> 27) * -7723592293110705685L;
            l5 ^= l5 >>> 31;
            int n7 = (int)(l5 >>> 16) & 0xFFFF;
            int n8 = 1;
            for (n5 = 0; n5 < 5; ++n5) {
                n8 *= 2 - n6 * n8;
            }
            n5 = n8 & 0xFFFF;
            long l6 = (l3 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            long l7 = l2 = (l3 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            l7 = (l7 ^ l7 >>> 30) * -4658895280553007687L;
            l7 = (l7 ^ l7 >>> 27) * -7723592293110705685L;
            int n9 = n5 * (cArray[0] - n7 & 0xFFFF) & 0xFFFF;
            if ((n4 = n9 ^ (int)((l7 ^= l7 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
                return string;
            }
            char[] cArray2 = new char[n4];
            int n10 = 0;
            for (int i2 = 0; i2 < cArray.length - 1; ++i2) {
                int n11 = n5 * (cArray[i2 + 1] - n7 & 0xFFFF) & 0xFFFF;
                long l8 = l6 ^ (long)n10 * -7046029254386353131L;
                l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
                l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
                l8 ^= l8 >>> 31;
                int n12 = n11 ^ (int)(l8 >>> 48) & 0xFFFF;
                l6 = l6 * 6364136223846793005L + 1442695040888963407L;
                if (i2 < n4) {
                    cArray2[i2] = (char)n12;
                }
                n10 = n11;
            }
            return new String(cArray2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static interface Gfx {
        public void backdrop(float var1, float var2);

        public void card(float var1, float var2, float var3, float var4, float var5, float var6, float var7);

        public void rect(float var1, float var2, float var3, float var4, float var5, int var6);

        public void gradient(float var1, float var2, float var3, float var4, float var5, int var6, int var7);

        public void border(float var1, float var2, float var3, float var4, float var5, float var6, int var7);

        public void shadow(float var1, float var2, float var3, float var4, float var5, float var6, int var7);

        public void circle(float var1, float var2, float var3, int var4);

        public void text(float var1, float var2, float var3, int var4, String var5, boolean var6);

        public float width(String var1, float var2, boolean var3);

        public void clip(float var1, float var2, float var3, float var4, float var5);

        public void unclip();

        public void logo(float var1, float var2, float var3, int var4);

        public void flush();
    }

    @Environment(value=EnvType.CLIENT)
    private record Hit(float x, float y, float w, float h, Runnable action) {
        boolean contains(double d2, double d3) {
            return d2 >= (double)this.x && d2 < (double)(this.x + this.w) && d3 >= (double)this.y && d3 < (double)(this.y + this.h);
        }
    }
}

