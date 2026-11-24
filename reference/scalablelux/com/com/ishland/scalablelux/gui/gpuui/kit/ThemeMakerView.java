/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.google.gson.JsonElement;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
import com.ishland.scalablelux.gui.gpuui.framework.UiSurface;
import com.ishland.scalablelux.gui.gpuui.framework.UiThemeMaterial;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.l.d;
import com.ishland.scalablelux.l.e;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ThemeMakerView {
    private static final long ARM_NANOS = 3000000000L;
    private static final long STATUS_NANOS = 6000000000L;
    private String selected;
    private boolean renaming;
    private String renameTarget;
    private String deleteArmed;
    private long deleteArmedAt;
    private String status = "";
    private boolean statusError;
    private long statusAt = Long.MIN_VALUE;
    private boolean dirty;
    private long dirtyCheckedAt = Long.MIN_VALUE;
    private final UiSmoothScroll listScroll = new UiSmoothScroll();
    private final float[] listRect = new float[4];
    private float lastScale = 1.0f;
    private boolean revealEnd;
    private UiHost lastHost;
    private final UiTextField nameField = new UiTextField(ThemeMakerView.ut("\ua25e\ucf8e\u3968\u2e97\u74d1\ucb3c\u74bc\ubfb0\u41ba\ue0e6\u5b0a\u2e51", 2075344480, 0xD306B7BB ^ CryptoVault.mask())).maxLength(32).onSubmit(this::commitRename).onBlur(this::commitRename);

    public void open() {
        this.selected = d.ab();
        this.renaming = false;
        this.deleteArmed = null;
        this.status = "";
        this.listScroll.reset();
        this.dirtyCheckedAt = Long.MIN_VALUE;
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        if (d2 >= (double)this.listRect[0] && d2 <= (double)(this.listRect[0] + this.listRect[2]) && d3 >= (double)this.listRect[1] && d3 <= (double)(this.listRect[1] + this.listRect[3])) {
            this.listScroll.wheel(d4, 46.0f * this.lastScale, l2);
            return true;
        }
        return false;
    }

    private void say(String string, boolean bl, UiHost uiHost) {
        this.status = string == null ? "" : string;
        this.statusError = bl;
        this.statusAt = uiHost.now();
    }

    private e.b selectedLook() {
        return this.selected == null ? null : d.aa(this.selected);
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean bl, Editor editor) {
        String string;
        float f9;
        this.lastScale = f6;
        this.lastHost = uiHost;
        List<e.b> list = d.ab();
        if (this.selected != null && e.aa(list, this.selected) < 0) {
            this.selected = null;
        }
        if (this.renaming && uiHost.focused() != this.nameField) {
            this.renaming = false;
        }
        if (this.deleteArmed != null && uiHost.now() - this.deleteArmedAt > 3000000000L) {
            this.deleteArmed = null;
        }
        float f10 = Math.round(20.0f * f6);
        if (bl) {
            UiKit.title(builder, uiHost, ThemeMakerView.ut("\ub82a\u9040\u55bb\u0192\ufec7\u8740\ucdac\u7c42\u3bf3\u439e\u6bd0\u6def\udfda", -360165465, 0xD306B7BB ^ CryptoVault.mask()), f2 + f10, f3 + (float)Math.round(18.0f * f6), f6);
            builder.text(f2 + f10, f3 + (float)Math.round(40.0f * f6), 10.0f * f6, uiHost.mutedColor(), UiKit.fit(ThemeMakerView.ut("\ud1cb\u34c7\u7336\u548a\ud3a9\ube9e\ua720\u9379\u3633\u8cf4\u10d5\uae72\u3368\uf737\u1286\u86dd\u87f3\u7aa5\u76dd\ub918\u5c7f\u3f65\u69b1\u6f05\u7b08\ue863\u055d\u9d40\u2267\u73cd\ud59d\udc9f\u2572\uab7a\u7d16\uc20eA\u8fa3\u2f91\u8b60\u7390\u5f74\ub786\u59f7\u09da\ucd33\ua213\ue251\u2639\ucd1d\u2345\u36c4\ubab8\u28cd$\u90ff\uaa64\ub568\u5359\ud21a\uf98c\ua73e\uaaf5\ubed6\uf4f4\ue3ce\u9557\uc7a0", 1583315553, 0xD306B7BB ^ CryptoVault.mask()), f4 - f10 * 2.0f, 10.0f * f6));
            f9 = f3 + (float)Math.round(64.0f * f6);
        } else {
            f10 = Math.round(8.0f * f6);
            f9 = f3 + (float)Math.round(4.0f * f6);
        }
        float f11 = Math.round(22.0f * f6);
        float f12 = f3 + f5 - (float)(bl ? Math.round(14.0f * f6) : Math.round(4.0f * f6)) - f11;
        float f13 = Math.round(Math.max(190.0f * f6, Math.min(250.0f * f6, f4 * 0.3f)));
        float f14 = f2 + f10;
        this.drawLeft(builder, uiHost, list, f14, f9, f13, f12 - f9, f6, f7, f8);
        float f15 = Math.round(18.0f * f6);
        float f16 = f14 + f13 + f15;
        float f17 = f2 + f4 - f10 - f16;
        builder.rect(f16 - (float)Math.round(f15 * 0.5f), f9, 1.0f, f12 - f9, 0.0f, uiHost.lineColor());
        this.drawRight(builder, uiHost, f16, f9, f17, f12 - f9, f6, f7, f8, editor);
        float f18 = f12 + (float)Math.round(6.0f * f6);
        UiKit.divider(builder, uiHost, f2 + f10, f12, f4 - f10 * 2.0f);
        boolean bl2 = !this.status.isEmpty() && uiHost.now() - this.statusAt < 6000000000L;
        String string2 = string = bl2 ? this.status : ThemeMakerView.ut("\u31c6\ua321\ue7f1\u3fb7\u0bc4\uc79c\u9c71\uf334\u27e3\u0971\u3f8a\u8c27\u110d\u6f0d\u1f6d\ud144\ua2db\ua4f7\ub4a1\u3651\u1f3c\u0f16\u318c\u9aae\ub13b\uc54c\u70b4\u4fbc\uc9d8\u915f\ufd45\u510d\u9c47\ub244\u18ef\u9216\uc395\u27cc\ua67d\u1de9\u326b\u807a\u07a3\u8144\uf033\u6064\ud0ba\ufc12\u9d46\u553b\ueebe\u58b6\ue54b\u8aea\u8c24\u4e83\uc08c\u1c7b\ua32d\u9ede\u4458\u6991\u12e0\u13fa\u0f2c\u55ef\u1708\u8685\u3343\u5013\u2c8f\u8109\u7665\u58ae\ud729\uc73b\u727a\u0f26\ud2e3\u4020\u0a23\uefa6\u7b51\u0fd0\u9794\u1468\ue4df\udaa3", -2118977439, 0xD306B7BB ^ CryptoVault.mask());
        int n2 = bl2 ? (this.statusError ? -38037 : uiHost.accent()) : uiHost.muted2Color();
        builder.text(f2 + f10, GpuUiFontMetrics.topInBox(f18, f11 - (float)Math.round(6.0f * f6), 10.0f * f6), 10.0f * f6, n2, UiKit.fit(string, f4 - f10 * 2.0f, 10.0f * f6));
    }

    private void drawLeft(UiDisplayList.Builder builder, UiHost uiHost, List<e.b> list, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        UiKit.label(builder, uiHost, ThemeMakerView.ut("\u8b57\u1bf0\uad76\ue72d\u5b3d\ud92a\u2851\udeed\u976d\ud831\u2458\u26ee\u4506\u1a46\u6ebf", -768461059, 0xD306B7BB ^ CryptoVault.mask()), f2, f3, f6);
        String string = list.size() + ThemeMakerView.ut("\ud4f7\u1975\u4d28\u9c32\udaea", 1051426628, 0xD306B7BB ^ CryptoVault.mask()) + 30;
        float f9 = GpuUiFontMetrics.textWidth(string, 9.5f * f6);
        builder.text(f2 + f4 - f9, f3, 9.5f * f6, uiHost.muted2Color(), string);
        float f10 = Math.round(28.0f * f6);
        float f11 = Math.round(6.0f * f6);
        float f12 = f10 * 2.0f + f11;
        float f13 = f3 + (float)Math.round(20.0f * f6);
        float f14 = Math.max(0.0f, f5 - (f13 - f3) - f12 - (float)Math.round(12.0f * f6));
        this.drawList(builder, uiHost, list, f2, f13, f4, f14, f6, f7, f8);
        float f15 = f13 + f14 + (float)Math.round(12.0f * f6);
        boolean bl = list.size() >= 30;
        float f16 = (f4 - f11) * 0.5f;
        UiKit.button(builder, uiHost, ThemeMakerView.ut("\u304d\u13d0\ub923\uf300", -108163124, 0xD306B7BB ^ CryptoVault.mask()), f2, f15, f16, f10, f6, UiKit.Style.PRIMARY, !bl, f7, f8, () -> this.newFromLive(uiHost));
        UiKit.button(builder, uiHost, ThemeMakerView.ut("\uecb9\u0ccb\u9a60\udd05\u891a\u6aec\u5a8c\ub22b\u0e6c", 1480442353, 0xD306B7BB ^ CryptoVault.mask()), f2 + f16 + f11, f15, f4 - f16 - f11, f10, f6, UiKit.Style.SECONDARY, !bl, f7, f8, () -> this.importFromClipboard(uiHost));
        e.b b2 = this.selectedLook();
        boolean bl2 = b2 != null;
        float f17 = (f4 - f11 * 2.0f) / 3.0f;
        UiKit.button(builder, uiHost, ThemeMakerView.ut("\uc9ad\udfe0\ua637\ub4f3\u2c6a\ub0cd\u5dcb\u10d4\uc5f8\uf991\ucc64\u062d", -1691193636, 0xD306B7BB ^ CryptoVault.mask()), f2, f15 += f10 + f11, f17, f10, f6, UiKit.Style.SECONDARY, bl2 && !bl, f7, f8, () -> this.duplicate(uiHost));
        UiKit.button(builder, uiHost, ThemeMakerView.ut("\u45f8\ue1ba\u0d3b\ubaae\u85f5\u6d92\u35bc\u7868", -976658179, 0xD306B7BB ^ CryptoVault.mask()), f2 + f17 + f11, f15, f17, f10, f6, UiKit.Style.SECONDARY, bl2, f7, f8, () -> this.startRename(uiHost));
        boolean bl3 = bl2 && this.deleteArmed != null && this.deleteArmed.equalsIgnoreCase(b2.ad());
        UiKit.button(builder, uiHost, bl3 ? ThemeMakerView.ut("\u2aef\uad99\u1319\u60f9\u11bd\u8340\u6918\u4e61", -808701775, 0xD306B7BB ^ CryptoVault.mask()) : ThemeMakerView.ut("\u74d6\ud1c9\u8b6b\ud1b1\ud7c8\u89ac\u3dcb\u9a34\uc605", -1133222321, 0xD306B7BB ^ CryptoVault.mask()), f2 + (f17 + f11) * 2.0f, f15, f4 - (f17 + f11) * 2.0f, f10, f6, UiKit.Style.DANGER, bl2, f7, f8, () -> this.delete(uiHost));
    }

    private void drawList(UiDisplayList.Builder builder, UiHost uiHost, List<e.b> list, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.listRect[0] = f2;
        this.listRect[1] = f3;
        this.listRect[2] = f4;
        this.listRect[3] = f5;
        float f9 = Math.round(42.0f * f6);
        float f10 = Math.round(6.0f * f6);
        float f11 = f9 + f10;
        this.listScroll.update(uiHost.frameDelta(), f5, Math.max(0.0f, (float)list.size() * f11 - f10), uiHost.now());
        if (this.revealEnd) {
            this.listScroll.scrollTo(this.listScroll.max(), false);
            this.revealEnd = false;
        }
        float f12 = this.listScroll.offset();
        if (list.isEmpty()) {
            List<String> list2 = UiKit.wrap(ThemeMakerView.ut("\u890c\ubee7\ue4e1\u754a\ua051\u20cc\ubfba\ub4c8\ua40c\uee12\u0b02\u99cc\ue6b2\u5b0c\uf4fc\u4cc7\u0e62\u80e9\ud9b3\ub6c6\u3a47\u1267\ue382\u43f7\ue63d\ue69d\ueb9d\u12b0\ua045\ue203\u139a\u97f7\ubfae\uafd8\ud169\uf174\uf395\u6f62\u696c\u5f73\u9ce0\uf9df\u53f3\u2bbd\ub3d7\u493e\u50e2\uc6c5\ue0c4\uf97c\u666d\u8859\ub880\u75fe\u2ab0\u7aac\uceec\ua31b\u6db0\u6772\u5def\uefc2\uf4ce\ud0c5\u260d\u63b4\u4d5b\u0281\ufa60\ue9f7\uad05", 861399903, 0xD306B7BB ^ CryptoVault.mask()), f4 - 16.0f * f6, 10.0f * f6, 4);
            float f13 = f3 + (float)Math.round(14.0f * f6);
            for (String string : list2) {
                builder.text(f2 + (f4 - GpuUiFontMetrics.textWidth(string, 10.0f * f6)) * 0.5f, f13, 10.0f * f6, uiHost.muted2Color(), string);
                f13 += (float)Math.round(14.0f * f6);
            }
            return;
        }
        builder.clipPush(f2, f3, f4, f5, 0.0f);
        uiHost.beginHitClip(f2, f3, f4, f5);
        String string = d.ab();
        float f14 = Math.round(10.0f * f6 * uiHost.cornerScale());
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f15;
            float f16 = f3 + (float)i2 * f11 - f12;
            if (f16 + f9 < f3 || f16 > f3 + f5) continue;
            e.b b2 = list.get(i2);
            boolean bl = this.selected != null && this.selected.equalsIgnoreCase(b2.ad());
            boolean bl2 = string != null && string.equalsIgnoreCase(b2.ad());
            boolean bl3 = uiHost.hovered(f7, f8, f2, f16, f4, f9);
            builder.rect(f2, f16, f4, f9, f14, bl ? UiKit.withAlpha(uiHost.accent(), 28) : uiHost.cardFill());
            if (bl3 && !bl) {
                builder.rect(f2, f16, f4, f9, f14, uiHost.controlFill(18));
            }
            builder.border(f2, f16, f4, f9, f14, bl ? Math.max(1.0f, 1.5f * f6) : 1.0f, bl ? UiKit.withAlpha(uiHost.accent(), 192) : (bl3 ? 0x40FFFFFF : uiHost.lineColor()));
            float f17 = Math.round(26.0f * f6);
            float f18 = f2 + (float)Math.round(8.0f * f6);
            float f19 = f16 + (f9 - f17) * 0.5f;
            ThemeMakerView.preview(builder, ThemeMakerView.baseTheme(b2), f18, f19, f17, f17, Math.round(7.0f * f6 * uiHost.cornerScale()));
            builder.border(f18, f19, f17, f17, Math.round(7.0f * f6 * uiHost.cornerScale()), 1.0f, 0x22FFFFFF);
            float f20 = f18 + f17 + (float)Math.round(10.0f * f6);
            float f21 = 0.0f;
            if (bl2) {
                f15 = Math.round(GpuUiFontMetrics.textWidth(ThemeMakerView.ut("\u9dc4\u8bf3\u510f\u7585\u46e1\u46e0\u0b33\u372c\u1cc9\u3d56", 621482417, 0xD306B7BB ^ CryptoVault.mask()), 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f6);
                f21 = f15 + (float)Math.round(8.0f * f6);
                UiKit.chip(builder, uiHost, ThemeMakerView.ut("\u0fc4\u9ad4\u284e\uef8d\uadea\u2f3e\uaa8c\uf566\ua10a", -772027756, 0xD306B7BB ^ CryptoVault.mask()), f2 + f4 - (float)Math.round(8.0f * f6) - f15, f16 + (f9 - (float)Math.round(17.0f * f6)) * 0.5f, f6, uiHost.accent());
            }
            f15 = f2 + f4 - f20 - (float)Math.round(8.0f * f6) - f21;
            builder.text(f20, GpuUiFontMetrics.topInBox(f16, f9, 11.0f * f6), 11.0f * f6, bl ? uiHost.titleColor() : uiHost.textColor(), UiKit.fit(b2.ad(), Math.max(0.0f, f15), 11.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            String string2 = b2.ad();
            uiHost.hit(f2, f16, f4, f9, () -> {
                if (this.renaming) {
                    uiHost.focus(null);
                }
                if (this.selected == null || !this.selected.equalsIgnoreCase(string2)) {
                    this.deleteArmed = null;
                }
                this.selected = string2;
                this.dirtyCheckedAt = Long.MIN_VALUE;
            });
        }
        uiHost.endHitClip();
        builder.clipPop();
        this.listScroll.paintBar(builder, f2 + f4 + (float)Math.round(3.0f * f6), f3, f5, f6, uiHost.textColor(), uiHost.now());
    }

    private void drawRight(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8, Editor editor) {
        float f9;
        float f10;
        float f11;
        float f12;
        boolean bl;
        e.b b2 = this.selectedLook();
        boolean bl2 = bl = b2 != null && d.aa(b2);
        if (bl && uiHost.now() - this.dirtyCheckedAt > 200000000L) {
            this.dirty = d.ab(b2);
            this.dirtyCheckedAt = uiHost.now();
        } else if (!bl) {
            this.dirty = false;
        }
        float f13 = Math.round(30.0f * f6);
        float f14 = Math.round(6.0f * f6);
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<UiKit.Style> arrayList2 = new ArrayList<UiKit.Style>();
        ArrayList<Boolean> arrayList3 = new ArrayList<Boolean>();
        ArrayList<Runnable> arrayList4 = new ArrayList<Runnable>();
        if (b2 == null) {
            arrayList.add(ThemeMakerView.ut("\u334c\ua1a0\uef3e\u781f\uc52d\u4985\u7b4a\u6d82\u4a1c\ud50b\u6480\udb4a\u9a3b\ub152\ufa1a\ub3e4\ud0d8", 2009752185, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.PRIMARY);
            arrayList3.add(d.ab().size() < 30);
            arrayList4.add(() -> this.newFromLive(uiHost));
        } else if (bl) {
            arrayList.add(ThemeMakerView.ut("\u52e5\ue847\u2773\u2e23\u722b\u4055\u7dfa\u50ed", 733654986, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.GHOST);
            arrayList3.add(true);
            arrayList4.add(() -> this.export(uiHost));
            arrayList.add(ThemeMakerView.ut("\u051c\uf6c0\u2641\u2cf8\uf703\uf330\u8092\u1684\u3ae0\u58ca", -1165905288, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.SECONDARY);
            arrayList3.add(this.dirty);
            arrayList4.add(() -> this.revert(uiHost));
            arrayList.add(ThemeMakerView.ut("\ud253\ucec0\u42ab\u8050\u0983\uba86\uba4e\u5a82", -569750613, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.PRIMARY);
            arrayList3.add(this.dirty);
            arrayList4.add(() -> this.save(uiHost));
        } else {
            arrayList.add(ThemeMakerView.ut("\u48fa\uf221\u13bc\ubb8c\u716b\ua169\u06e9\u2478\u5822\u76e4", -686253727, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.GHOST);
            arrayList3.add(true);
            arrayList4.add(() -> this.export(uiHost));
            arrayList.add(ThemeMakerView.ut("\u131a\u2351\uf87f\u86af\u9af9\u3e23\u469b\u21c4", 80693377, 0xD306B7BB ^ CryptoVault.mask()));
            arrayList2.add(UiKit.Style.PRIMARY);
            arrayList3.add(true);
            arrayList4.add(() -> this.apply(uiHost));
        }
        float f15 = f2 + f4;
        for (int i2 = arrayList.size() - 1; i2 >= 0; --i2) {
            f12 = Math.max((float)Math.round(64.0f * f6), UiKit.buttonWidth((String)arrayList.get(i2), f6));
            UiKit.button(builder, uiHost, (String)arrayList.get(i2), f15 -= f12, f3, f12, f13, f6, (UiKit.Style)((Object)arrayList2.get(i2)), (Boolean)arrayList3.get(i2), f7, f8, (Runnable)arrayList4.get(i2));
            f15 -= f14;
        }
        float f16 = f15 - (float)Math.round(10.0f * f6);
        f12 = Math.max(0.0f, f16 - f2);
        if (this.renaming && b2 != null) {
            this.nameField.draw(builder, uiHost, f2, f3, Math.min(f12, (float)Math.round(260.0f * f6)), f13, f6);
        } else {
            String string;
            String string2 = b2 == null ? ThemeMakerView.ut("\ud197\ub2b1\u4e64\u490f\u14a9\ue4e3\ua5be\u8a26\u4581\ub3dd\u7281\ufb2f\u828f", 1218984678, 0xD306B7BB ^ CryptoVault.mask()) : b2.ad();
            f11 = 13.0f * f6;
            String string3 = b2 == null ? ThemeMakerView.ut("\uf8df\u1d3a\u91fe\uda3b\ub9f2\u5396\u1c17\u6cf6\u3ef4\u8f45\u28d7\u4e11\u7b03", -694954428, 0xD306B7BB ^ CryptoVault.mask()) : (bl ? (this.dirty ? ThemeMakerView.ut("\u4f1e\ud3b4\u58c6\u66e8\u553c\u8f1e\uc7dc\u3b9e\ubbc9\ue0cc\u2362\udae7\u0b1b\uc223\u8572\u9eaa\u910d", 503448579, 0xD306B7BB ^ CryptoVault.mask()) : ThemeMakerView.ut("\u493a~\ue010\uaf8d\u6670\udd3a\u527c", -1272094433, 0xD306B7BB ^ CryptoVault.mask())) : (string = ThemeMakerView.ut("\u7c79\u25f5\ud675\ua4db\u2f63\u3cfe\u7c12\ue5af\ufc11\u724b\ue34c\uc862\u92eb\uf799", -845168181, 0xD306B7BB ^ CryptoVault.mask())));
            int n2 = b2 == null || bl && this.dirty ? -15253 : (bl ? uiHost.accent() : uiHost.muted2Color());
            f10 = Math.round(GpuUiFontMetrics.textWidth(string, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f6);
            String string4 = UiKit.fit(string2, Math.max(0.0f, f12 - f10 - (float)Math.round(10.0f * f6)), f11, UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2, GpuUiFontMetrics.topInBox(f3, f13, f11), f11, uiHost.titleColor(), string4, UiDrawCommand.TextWeight.SEMIBOLD);
            f9 = GpuUiFontMetrics.textWidth(string4, f11, UiDrawCommand.TextWeight.SEMIBOLD);
            if (f12 > f10 + 20.0f * f6) {
                UiKit.chip(builder, uiHost, string, f2 + f9 + (float)Math.round(10.0f * f6), f3 + (f13 - (float)Math.round(17.0f * f6)) * 0.5f, f6, n2);
            }
        }
        float f17 = f3 + f13 + (float)Math.round(10.0f * f6);
        UiKit.divider(builder, uiHost, f2, f17, f4);
        f11 = f17 + (float)Math.round(10.0f * f6);
        float f18 = Math.max(0.0f, f3 + f5 - f11);
        if (b2 != null && !bl) {
            float f19 = f11 + Math.max((float)Math.round(30.0f * f6), f18 * 0.3f);
            f10 = Math.round(120.0f * f6);
            float f20 = Math.round(64.0f * f6);
            f9 = f2 + (f4 - f10) * 0.5f;
            float f21 = Math.round(12.0f * f6 * uiHost.cornerScale());
            ThemeMakerView.preview(builder, ThemeMakerView.baseTheme(b2), f9, f19, f10, f20, f21);
            builder.border(f9, f19, f10, f20, f21, 1.0f, 0x26FFFFFF);
            UiKit.emptyState(builder, uiHost, b2.ad() + ThemeMakerView.ut("\uee51\uce45\uc3fe\u7fa3\ufada\u019f\uaed9\ucedf\u9936\ud7eb\u7cbf\u1c8f\u34fb\u0915\u33d7\ua21f", 1383546867, 0xD306B7BB ^ CryptoVault.mask()), ThemeMakerView.ut("\u2e95\u2599\u95e4\u4cd1\u0b65\uaa91\u1f20\u77ff\u5be6\u9ecf\u1734\u9244\uf6c6\u8b89\uee9f\u26ad\u5eb6\u04f2\u52f1\u389f\ub073\ua6d9\u8fc1\u5999\u2be7\u1919\u61bb\u959a\u66d7\u95c9\u5797\ucca6\uc347\u31b2\ub8f3\u9e4e\ue4fe\u4bc3\u9b40\u2e90\u64e2\uebc9\u2818\u4a02\u47ad\u5bed\ua106\u93bc\u30d7\u1d42\ub219\u511d", -354494451, 0xD306B7BB ^ CryptoVault.mask()), f2, f19 + f20 + (float)Math.round(16.0f * f6), f4, f6);
            float f22 = Math.round(120.0f * f6);
            float f23 = Math.round(30.0f * f6);
            UiKit.button(builder, uiHost, ThemeMakerView.ut("\u44a1\u65b1\u7783\ub779\u555b\ua049\u5465", -656186107, 0xD306B7BB ^ CryptoVault.mask()), f2 + (f4 - f22) * 0.5f, f19 + f20 + (float)Math.round(56.0f * f6), f22, f23, f6, UiKit.Style.PRIMARY, true, f7, f8, () -> this.apply(uiHost));
            return;
        }
        if (editor != null && f18 > 0.0f) {
            editor.draw(builder, f2, f11, f4, f18, f6, f7, f8);
        }
    }

    private static String baseTheme(e.b b2) {
        JsonElement jsonElement = b2.ab().get(ThemeMakerView.ut("\u7c84\u3d83\u6b76\u990a\u63ee\u62cb\u8615\ud082", -1755422479, 0xD306B7BB ^ CryptoVault.mask()));
        return jsonElement == null ? UiThemeMaterial.FLUX.name() : jsonElement.getAsString();
    }

    private static void preview(UiDisplayList.Builder builder, String string, float f2, float f3, float f4, float f5, float f6) {
        UiSurface uiSurface = UiSurface.byName(string);
        if (!uiSurface.isTheme()) {
            builder.material(f2, f3, f4, f5, f6, uiSurface.preview(UiThemeMaterial.FLUX.accent() & 0xFFFFFF));
        } else if (UiThemeMaterial.LIQUID_GLASS.name().equalsIgnoreCase(string)) {
            int n2 = UiThemeMaterial.LIQUID_GLASS.accent() & 0xFFFFFF;
            builder.gradient(f2, f3, f4, f5, f6, n2 | 0x40000000, 0x22FFFFFF, n2 | 0x18000000, 0x10FFFFFF);
        } else {
            builder.material(f2, f3, f4, f5, f6, UiThemeMaterial.preview(string));
        }
    }

    private boolean store(List<e.b> list, UiHost uiHost) {
        if (d.aa(list)) {
            return true;
        }
        this.say(ThemeMakerView.ut("\u43c4\ue1bd\u1c8d\ucf7b\u8f1b\uf684\uff69\u2258\uc8f6\u69f4\u8651\uf193\u125f\u4c86\ubfe5\udcee\u6f66\ub910\u81d9\u413e\ua383\ufe61\u148b\uf5ff\u7de6\ua6a6\u2bd9S\udf0b\ucfcc\u33e0\u43b4\u5e43\ud715\u2a4f\u1248\u2ca2\ubdab\ubca6\ud329\u91d6\u3fbb\u36c7\u84a9\u6674\u0e7a\u4e37\u4f76\ufdc2\u92ea\uce88\u15b9\u31b4", -1559544072, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
        return false;
    }

    private void newFromLive(UiHost uiHost) {
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        if (arrayList.size() >= 30) {
            this.say(ThemeMakerView.ut("\uca4a\uce43\u7f86\ufab7\ub34d\u385f\uad17\uf1c3\u04db\u27f4\u51d3\u44b4\u94b4\u4b53\u72f6\ubeaa\u716d\u9a38\u6ae7\u8327\u5f10\ufffb\ufc16\u7e0d\u70ef\uaf7c\u4d79\u5f63\ub3ac\u73e0\u5c10\ud53b\ua6aa\ud6fb\u4fa5\u0fd6\u3062\ube9c\u45ea\u6085\u0c4d\uef74\u8ed0\u6a20\uc526\u5565\u943c\ua14f\u4c58\u8a39\u586a\ue5c2\u5cea\u7d52\u62f1\u94b1\ucdc8\uebde\uebd3\u99f0\ue721", -432833416, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
            return;
        }
        String string = e.aa(ThemeMakerView.ut("\u77d8\u5409\u4c4f\ue62b\uc93a\u059d\u1197\u3064\u393b\u23f1\u763c\u5a8a", -314046125, 0xD306B7BB ^ CryptoVault.mask()), arrayList);
        e.b b2 = new e.b(string, e.aa(d.aa(), d.aa()));
        arrayList.add(b2);
        if (!this.store(arrayList, uiHost)) {
            return;
        }
        d.aa(b2.ad());
        this.selected = b2.ad();
        this.revealEnd = true;
        this.say(ThemeMakerView.ut("\u4db8\u354e\u11bf\u8a84\u90dd\u9dba\uff4e\u2bec\uf92f\u9102\u9449\u2aeb\u8e77\u416a\ueff6\u97fa\uf46a\ud5dd\u1403\u5511\u6b49\u9dcc\uc70f\u5fa7\u9791\u4542\ub27c\ud462", 1142366192, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ThemeMakerView.ut("\uf189\uc876\u7388\ufe8e\u0efa\uff8f\udd19\ufc5b\u0706\ue7ab\ue908\u52e8\u7fff\u9667\udde0\ub151\u9389\ub8ca\u8afe\uf78f\u2605\ubd4d\u6cb4\u3d55\u9809\ud23a\u1e3f\u2467\udf22\u7344\u1dde", 261771409, 0xD306B7BB ^ CryptoVault.mask()), false, uiHost);
        this.startRename(uiHost);
    }

    private void duplicate(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        if (arrayList.size() >= 30) {
            this.say(ThemeMakerView.ut("\u135f\u4bce\u125f\u7b60\udf1b\ua2c4\uad6c\u52ad\uc2b6\u6842\u00f4\u4376\u9b5e\u0692\u7fe7\u3833\u63be\ud05e\u5b40\u72ba\u92f6\u2ee5\ua8db\uf0e8\u92de\ued9c\u4f6b\u9fae\uf8d4\u77a5\u4602\u0d96\u4984\ua14f\u064b\uc799\ub6e0\u5696\u87ab\u26b1\ud74a\ud3c1\u1739\ua314\u505b\u5ea7\u0ac7\u89b7\u03c7\u37d5\ueae5\u9ba5\u966c\u9556\u3547\u3eb5\u6bc5\u2920\ub245\u4c02\u133d\u3149", 579711070, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
            return;
        }
        String string = b2.ad().length() > 27 ? b2.ad().substring(0, 27).strip() : b2.ad();
        e.b b3 = b2.ab(e.aa(string + ThemeMakerView.ut("\u051a\ud7ad\u67b9\u48af\ufc30\u6cb0", -1491470597, 0xD306B7BB ^ CryptoVault.mask()), arrayList));
        arrayList.add(e.aa(arrayList, b2.ad()) + 1, b3);
        if (!this.store(arrayList, uiHost)) {
            return;
        }
        this.selected = b3.ad();
        this.say(ThemeMakerView.ut("\u35b0\u582d\u763d\u4876\ua7d3\u0561\u1112", 1263288030, 0xD306B7BB ^ CryptoVault.mask()) + b3.ad() + ".", false, uiHost);
    }

    private void startRename(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        this.renameTarget = b2.ad();
        this.nameField.setValue(b2.ad());
        this.renaming = true;
        uiHost.focus(this.nameField);
        this.nameField.selectAll();
    }

    private void commitRename() {
        if (!this.renaming) {
            return;
        }
        this.renaming = false;
        UiHost uiHost = this.lastHost;
        String string = this.renameTarget;
        this.renameTarget = null;
        if (this.lastHost != null && this.lastHost.focused() == this.nameField) {
            this.lastHost.focus(null);
        }
        if (string == null) {
            return;
        }
        String string2 = this.nameField.value();
        if (string2 == null || string2.isBlank()) {
            return;
        }
        String string3 = e.aa(string2);
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        int n2 = e.aa(arrayList, string);
        if (n2 < 0 || ((e.b)arrayList.get(n2)).ad().equals(string3)) {
            return;
        }
        int n3 = e.aa(arrayList, string3);
        if (n3 >= 0 && n3 != n2) {
            if (uiHost != null) {
                this.say(ThemeMakerView.ut("\u2490\u1924\ube4d\ue395\u3a02\u85e5\u9a0e\u564d\u964d\u1184\u70dd\ue1b6\ue2b7\u02b0\u8e7c\u8687\ue3d3\u92ef\u7369\u0c86\uddbd\u0b50\u1984\u483d\u6ce5\ub677\u6333\ubec3\uadf7\u53c8\ue0b1\u9b48\uf828\u8074\ubdc7\u7aef", 2100497252, 0xD306B7BB ^ CryptoVault.mask()) + string3 + ".", true, uiHost);
            }
            return;
        }
        boolean bl = d.aa((e.b)arrayList.get(n2));
        arrayList.set(n2, ((e.b)arrayList.get(n2)).ab(string3));
        if (!d.aa(arrayList)) {
            if (uiHost != null) {
                this.say(ThemeMakerView.ut("\u8fb8\uf4a0\ube72\u2a80\u5c18\u9171\u2f08\u76d0\u16f5\u42e4\u9f71\u72e1\uef74\ue247\u3e1b\u8a16\uf312\ud1b9\u20aa\u5fd1\u797b\ub554\ue25d\u26ab\u382c\u79fb\u053b\u93f0\u9c7e\u7956\ud9a7\u8ff5\u3d19\u7f6b\u0802\u1185\u6b02\u6aa3\u9bfa\u3efc\u8f3a\u25f3\uff94\udcc0\uea2d\uf8ed\u9140\u1fac\u08f8\u2f2b\ubdea\u8fb3\u556d", 536276593, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
            }
            return;
        }
        if (bl) {
            d.aa(string3);
        }
        this.selected = string3;
    }

    private void delete(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        if (this.deleteArmed == null || !this.deleteArmed.equalsIgnoreCase(b2.ad())) {
            this.deleteArmed = b2.ad();
            this.deleteArmedAt = uiHost.now();
            this.say(ThemeMakerView.ut("\u02eb\u425f\u3924\uedaf\u71a3\u48b8\uc28e\ufbc0\u99a4\u7232\u5d8b\u573e\u1525\ubed1\uc357\u3442\ud136\u1936\ue4ee\udeaf\u6cda\u67f0\ub516", -546452005, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ThemeMakerView.ut("\u5d37\uec93\ub655\u0a9a\ufd30\u11fb\u859b\u57de\u4233\u038f\u270a\u9704\ud729\ufbf9\u538d\ufa57\u4946\ua407\ud5cb\uc81d\u4b0d\u4162\uc63a\u3037\u9eb7\u4ee2\ud357", -876153999, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
            return;
        }
        this.deleteArmed = null;
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        int n2 = e.aa(arrayList, b2.ad());
        if (n2 < 0) {
            return;
        }
        boolean bl = d.aa(b2);
        arrayList.remove(n2);
        if (!this.store(arrayList, uiHost)) {
            return;
        }
        if (bl) {
            d.aa("");
        }
        this.selected = arrayList.isEmpty() ? null : ((e.b)arrayList.get(Math.min(n2, arrayList.size() - 1))).ad();
        this.say(ThemeMakerView.ut("\u7d94\ud37f\u9fbf\uac1f\u73fe\ud378\u25cc\u1e89\ud38b\u8d83", -599878983, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ".", false, uiHost);
    }

    private void apply(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        d.aa(b2);
        this.dirtyCheckedAt = Long.MIN_VALUE;
        this.say(ThemeMakerView.ut("\u8d0c\ufe16\u8af7\u6d9b\u22cf\ue9ab\uf2ec\ud5fb\u13e1\u9ef3\u23a9\ub441", 1906313482, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ThemeMakerView.ut("\u72a5\u4c15\ud645\u1a06\uab70\u3530\u11f0\ued34\ufe42\u79bb\u0233\u4bfb\u43df\u2e0e\u57ef\u53f0\u4ba6\u286c\u8c68\u8b29\ud4fa\u6daf\ue2c7\ue0c2\u875b\u1337\u57c5\u1c94\u55d8\u7ef2\u4daf\ubaa2\u7936\u3a79\u3b25\uef75\uc202\ua5d8\u84b0\uf992\u4a07\u4059\u4066\u6c65", 1637343277, 0xD306B7BB ^ CryptoVault.mask()), false, uiHost);
    }

    private void revert(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        d.aa(b2);
        this.dirtyCheckedAt = Long.MIN_VALUE;
        this.say(ThemeMakerView.ut("\u0c1e\udf75\u7164\ubc8d\u7143\u4f19\uba80\u8115\udaed\uc604\u3af1\u0250\u5e44\u662a\uf96b\u703f\u0cba\u1132\uf5a6\u00b1\ub00a\u5a3d", 506229065, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ".", false, uiHost);
    }

    private void save(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        int n2 = e.aa(arrayList, b2.ad());
        if (n2 < 0) {
            return;
        }
        arrayList.set(n2, b2.aa(e.aa(d.aa(), d.aa())));
        if (!this.store(arrayList, uiHost)) {
            return;
        }
        d.aa(b2.ad());
        this.dirtyCheckedAt = Long.MIN_VALUE;
        this.say(ThemeMakerView.ut("\ub253\u3a5d\ue5ca\u8a86\u8df9\uf71d\ud184\u163e\u65d2", -16837167, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ".", false, uiHost);
    }

    private void export(UiHost uiHost) {
        e.b b2 = this.selectedLook();
        if (b2 == null) {
            return;
        }
        uiHost.setClipboard(e.aa(b2));
        this.say(ThemeMakerView.ut("\u8238\u30cf\u8cb7\ucf71\u1d31\u30ce\u009f\ub807\uf047\u31db\u0f71\uad09\u6da1\u3014\u05c6\uef05\ue785\uf7e6\uc31c\u44ac\uc91f\ub762", 1576298587, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + (this.dirty ? ThemeMakerView.ut("\u3f69\ua7f1\u5a1c\uc0cc\u3d0b\u4f67\ueb38\u72d1\uc90b\u9513\u7c67\u03de\ud31d\u6605\u298a\u8282\u5e48\u74e5\uc31c\u8529\u2088\ue56f\u61fa\u7dcf", -24187848, 0xD306B7BB ^ CryptoVault.mask()) : "") + ThemeMakerView.ut("\uc22d\uaddf\u5553\u1e52\u2f92\uf4ae\u3f82\ue617\uf98a\u2669\uea20\u34c5\ua26a\udc68\ub0b8\u8d45\u1f1d\ueaf9\ube1b\udde2\ufb3d\u1a43\uc6d9\u1ec2\uce43\u7ea3\u1384\ue5c7\ub25e\ubac3\u8bb0\u632b\u221a\uc44a\ue36d", -230693421, 0xD306B7BB ^ CryptoVault.mask()), false, uiHost);
    }

    private void importFromClipboard(UiHost uiHost) {
        ArrayList<e.b> arrayList = new ArrayList<e.b>(d.ab());
        if (arrayList.size() >= 30) {
            this.say(ThemeMakerView.ut("\u349b\ua2c8\u9e56\u2422\u8e1f\u761c\u6d3a\ufe3b\u512c\u8110\u3675\uc0c0\u1149\u4a96\ufe06\u5e56\u265d\u45e6\u3cee\uaac0\u1e65\u5cd2\ufc6f\u72bb\ucf51\ufcfd\uff79\u0a22\u3193\u4b66\ub425\u2592\ue564\u4ff0\u2126\ue7b2\udfaf\ufaad\u79e5\ue2ad\uc515\u2c72\u0a95\u5b42\u1b79\u4be9\u09f1\u7e3c\u44d5\u2a77\u876c\u5f76\u1fd2\u72e7\u5b5f\ue68a\uecc0\u237d\ub640\u3621\u5eba", 818358605, 0xD306B7BB ^ CryptoVault.mask()), true, uiHost);
            return;
        }
        e.a a2 = e.aa(uiHost.clipboard(), d.aa());
        if (!a2.af()) {
            this.say(a2.ac(), true, uiHost);
            return;
        }
        e.b b2 = a2.aa().ab(e.aa(a2.aa().ad(), arrayList));
        arrayList.add(b2);
        if (!this.store(arrayList, uiHost)) {
            return;
        }
        this.selected = b2.ad();
        this.revealEnd = true;
        this.say(ThemeMakerView.ut("\u1ecc\u2a0e\ud000\uc09b\u8007\u6c99\u0a5a\u5cfa\ua750\uc88f", 539784779, 0xD306B7BB ^ CryptoVault.mask()) + b2.ad() + ThemeMakerView.ut("\u1eba\u6945N\uf25f\u2c30\u822f\ued9b\ud3bf\u6e33\u939e\ubc55\ufc43\u21a1\uaf1c\ubb40\u195f\u09ae\u48bc\u6927\u7c68\ucf09\u686b\u9b7b\u8db3\u7c8c\ubd45\ufe7f", 19174988, 0xD306B7BB ^ CryptoVault.mask()), false, uiHost);
    }

    private static /* synthetic */ String ut(String string, int n2, int n3) {
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
    public static interface Editor {
        public void draw(UiDisplayList.Builder var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);
    }
}

