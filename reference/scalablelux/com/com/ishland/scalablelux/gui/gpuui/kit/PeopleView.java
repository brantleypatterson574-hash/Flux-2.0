/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.e.b;
import com.ishland.scalablelux.g.c;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class PeopleView {
    private final Backend backend;
    private Filter filter = Filter.ALL;
    private String status = "";
    private long statusAt = Long.MIN_VALUE;
    private final UiSmoothScroll savedScroll = new UiSmoothScroll();
    private final UiSmoothScroll nearbyScroll = new UiSmoothScroll();
    private final float[] savedRect = new float[4];
    private final float[] nearbyRect = new float[4];
    private float lastScale = 1.0f;
    private List<Person> fixedSaved;
    private List<Person> fixedNearby;
    private BindTarget binding = BindTarget.NONE;

    public PeopleView(Backend backend) {
        this.backend = backend;
    }

    public void preview(List<Person> list, List<Person> list2) {
        this.fixedSaved = list;
        this.fixedNearby = list2;
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        if (PeopleView.inside(d2, d3, this.savedRect)) {
            this.savedScroll.wheel(d4, 56.0f * this.lastScale, l2);
            return true;
        }
        if (PeopleView.inside(d2, d3, this.nearbyRect)) {
            this.nearbyScroll.wheel(d4, 56.0f * this.lastScale, l2);
            return true;
        }
        return false;
    }

    private static boolean inside(double d2, double d3, float[] fArray) {
        return d2 >= (double)fArray[0] && d2 <= (double)(fArray[0] + fArray[2]) && d3 >= (double)fArray[1] && d3 <= (double)(fArray[1] + fArray[3]);
    }

    private boolean matches(Person person) {
        return switch (this.filter.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> true;
            case 1 -> person.friend();
            case 2 -> person.enemy();
        };
    }

    private void say(String string, UiHost uiHost) {
        this.status = string == null ? "" : string;
        this.statusAt = uiHost.now();
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.lastScale = f6;
        float f9 = Math.round(22.0f * f6);
        UiKit.title(builder, uiHost, PeopleView.xq("\u884c\u7dd0\uc33a\u8282\u73e0\ue386\uaa8e", -1505363998, 0x563A7761 ^ CryptoVault.mask()), f2 + f9, f3 + (float)Math.round(20.0f * f6), f6);
        float f10 = Math.round(250.0f * f6);
        float f11 = Math.round(30.0f * f6);
        UiKit.segmented(builder, uiHost, new String[]{PeopleView.xq("\ueaec\ubf2b\u2c31\u7fba\u9f0d\uc179\ube2a", 482372059, 0x563A7761 ^ CryptoVault.mask()), PeopleView.xq("\u541a\u4796\u4e1a\u846c\u478a\u0acb\u3264\u8d50\u1a49\u1f02", 1357014677, 0x563A7761 ^ CryptoVault.mask()), PeopleView.xq("\ue4d1\u4f87\u4047\uaa1b\u8b41\u32c7\u6406\u9805\ua969\u1811\u204e", -2020029015, 0x563A7761 ^ CryptoVault.mask())}, this.filter.ordinal(), f2 + f4 - f9 - f10, f3 + (float)Math.round(14.0f * f6), f10, f11, f6, f7, f8, n2 -> {
            this.filter = Filter.values()[n2];
        });
        this.drawBindButtons(builder, uiHost, f2 + f4 - f9 - f10 - (float)Math.round(12.0f * f6), f3 + (float)Math.round(14.0f * f6), f11, f6, f7, f8);
        UiKit.divider(builder, uiHost, f2 + f9, f3 + (float)Math.round(56.0f * f6), f4 - f9 * 2.0f);
        float f12 = f3 + (float)Math.round(72.0f * f6);
        float f13 = f3 + f5 - (float)Math.round(36.0f * f6);
        float f14 = Math.round(20.0f * f6);
        float f15 = (f4 - f9 * 2.0f - f14) * 0.5f;
        float f16 = f2 + f9;
        float f17 = f16 + f15 + f14;
        ArrayList<Person> arrayList = new ArrayList<Person>();
        for (Person object : this.fixedSaved != null ? this.fixedSaved : this.backend.saved()) {
            if (!this.matches(object)) continue;
            arrayList.add(object);
        }
        ArrayList arrayList2 = new ArrayList();
        for (Person person : this.fixedNearby != null ? this.fixedNearby : this.backend.nearby()) {
            if (!this.matches(person)) continue;
            arrayList2.add(person);
        }
        UiKit.label(builder, uiHost, PeopleView.xq("\u3350\u7dd9\u86c5\u0820\u0544\uedf4\ucacb\u1bc3\uae21", -2021231254, 0x563A7761 ^ CryptoVault.mask()) + arrayList.size() + ")", f16, f12, f6);
        this.drawList(builder, uiHost, arrayList, true, f16, f12 + (float)Math.round(20.0f * f6), f15, f13 - f12 - (float)Math.round(20.0f * f6), f6, f7, f8, this.savedScroll, this.savedRect, this.filter == Filter.ALL ? PeopleView.xq("\ue747\u4b08\u0ed7\uc2f5\ud1fe\u95dd\uf060\udb63\u89f8\uaa2b\u023a\u7a69\u1784\uc9f3\u6f4f\u6580\ua567\u7c07\uc1ba\u740b\u6e70\u4dd2\u56cd\ud9c5\u9c31\ucc3b\u1014\u9f45\u58d4\u0674\uf2f4\u0635\uc0d3\ud36d", 290625485, 0x563A7761 ^ CryptoVault.mask()) : (this.filter == Filter.FRIENDS ? PeopleView.xq("\u17f4\u71b0\u6ec8\ua4ae\u76fb\u1697\uad8a\u330b\ucaba\ue05e\u963e\u871f\u78de\u0552\u53bc\ua018\u6b84\ud205\u16e4\u3c4a\u68f9\ua58b", 203388499, 0x563A7761 ^ CryptoVault.mask()) : PeopleView.xq("\ue3b5\u854e\ubd62\uec7b\u69c2\ueb65\uf058\u0a26\ub035\u65f2\ue962\ue1b3\u4c2e\u1277\u411c\u4e90\ucfda\u6893\u5a15\u1dd3\ubf18\u7e20", 2104457657, 0x563A7761 ^ CryptoVault.mask())));
        UiKit.label(builder, uiHost, PeopleView.xq("\u6dad\u64e5\u6fe1\u6aae\ucbd3\u32a5\ua7af\ub624\u29b4\u9cf1\u8422\u825c\uf06b\uc84d\u9f88\u4f11\u688f\ub307\uc941\u3903", 350383593, 0x563A7761 ^ CryptoVault.mask()) + arrayList2.size() + ")", f17, f12, f6);
        this.drawList(builder, uiHost, arrayList2, false, f17, f12 + (float)Math.round(20.0f * f6), f15, f13 - f12 - (float)Math.round(20.0f * f6), f6, f7, f8, this.nearbyScroll, this.nearbyRect, PeopleView.xq("\u9816\u6a4f\u81ef\u9800\u49ae\u403a\ufdd6\u8ac1\u3691\u0bc6\u6bca\u27c1\u3812\uec29\u78f5\u8497\u62fa\ub49f\u882e\u30aa\u1524\u1438\u8aad\u09bf\uab1e\ufc3e\ud7f9\ue704\ue975\u4097\u7b9b\ue18b\uaf24\u5b76\uf1e1\u73ba\u7dd1", -606008976, 0x563A7761 ^ CryptoVault.mask()));
        UiKit.divider(builder, uiHost, f2 + f9, f13 + (float)Math.round(4.0f * f6), f4 - f9 * 2.0f);
        if (!this.status.isEmpty() && uiHost.now() - this.statusAt < 5000000000L) {
            builder.text(f16, f13 + (float)Math.round(14.0f * f6), 10.0f * f6, uiHost.accent(), UiKit.fit(this.status, f4 - f9 * 2.0f, 10.0f * f6));
        } else {
            builder.text(f16, f13 + (float)Math.round(14.0f * f6), 10.0f * f6, uiHost.muted2Color(), PeopleView.xq("\ue4aa\u9f19\u3204\uf347\uda42\u99e3\ubc95\ude55\u59b1\u11d4\u7f06\u4de5\ub128\uef7a\u17f5\u3f59\u2227\uf638\ua702\u38ba\u4c93\u7b74\ucc8b\u6568\u343c\ucd02\ue365\ue846\ufb89\u8fa8\u84f0\ud7d5\u2c39\udfe0\uf7b7\u85ea\u86f1\u05e9\u548d\uffbb\u3cb2\uffa3\uc266\u787c\u1781\u44c5\ua00b\u6af8\ud58f\uf87e\ufc51\ufe39\u2e14\uc4d3\uab34\u956e\u45b9\u609d\u2fe7\u2f5a\u9381\ua6a9\u7d85\uec43\ubf45\ue861\udeb7\udd4f\u8dce\ubdea\u5edd\uba91\ub5eb\u8e78\ubecf\u770b\u3c65\ua3e9\u9342\uf6c8\u68fd", 960102501, 0x563A7761 ^ CryptoVault.mask()));
        }
    }

    private void drawBindButtons(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (this.binding != BindTarget.NONE && !uiHost.capturingKey()) {
            this.binding = BindTarget.NONE;
        }
        String string = PeopleView.xq("\u6e5c\u095f\ub5a4\u982e\u6bf4\ud9f3\u6e75\ufbdd\u959a\u7bf7\u962a\u0485\ud2f1\u3353", -1994922193, 0x563A7761 ^ CryptoVault.mask()) + (this.binding == BindTarget.FRIEND ? PeopleView.xq("\ubff4\u4f27\u1cd8\u4194\ueecb\u134e\u545d\u63b3\uc830\uce6b\u892e\ub2be\u2e23\u5738\u082e\uc5bf\uac33\u2ebb", 1907794041, 0x563A7761 ^ CryptoVault.mask()) : this.backend.friendKeyName());
        String string2 = PeopleView.xq("\udb83\ub80f\uf0c6\u9183\u8384\u51c1\uf213\u21a3\u809d\ud87f\uc0bc\uf036\uf420\u7d13", 110685460, 0x563A7761 ^ CryptoVault.mask()) + (this.binding == BindTarget.ENEMY ? PeopleView.xq("\u3392\u7083\u67bd\u3fc1\u465a\u8dbd\ua503\u2b80\ua7d7\ua487\u14f4\u997a\uf66c\u5df1\u4208\u4a22\u1052", 2017237550, 0x563A7761 ^ CryptoVault.mask()) : this.backend.enemyKeyName());
        float f8 = UiKit.buttonWidth(string2, f5);
        float f9 = UiKit.buttonWidth(string, f5);
        float f10 = f2 - f8;
        float f11 = f10 - (float)Math.round(8.0f * f5) - f9;
        boolean bl = this.binding == BindTarget.NONE && !uiHost.capturingKey();
        UiKit.button(builder, uiHost, string, f11, f3, f9, f4, f5, this.binding == BindTarget.FRIEND ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, bl, f6, f7, () -> {
            this.binding = BindTarget.FRIEND;
            this.say(PeopleView.xq("\ue889\u9839\u9075\uea5e\u3fa6\u52f5\u4df7\u5e72\u4cf6\u1eed\u301a\uae57\u0606\ua6ab\u4192\u985d\ubaf2\u80d5\u87e9\u452c\u6c6e\u6425\u6b7b\u56f7\ua997\u531c\u5587\u659a\uc2ac\u0539\u2357\u7785\uf097\u7b99\u1149\u42c4\ub5f2\u9ea2\u8ed4\u52c9\uafd8\u9dca\uda39\ub4dc\u5e70\u105c\u2ce0\u950d\u67a3\ufdd7\u4062\uaf93\u3f89\ub49b\u5815\u1735\u3404\u4b58\ud4bb\u566a\u85ca\uc47b\uf80c\u9e86\u9d80\ufbd6\u383d\u0b72\u9fca\ud0c2\u2254\uee63\u3086\u2a6e\u38d0\ue67a\ub272\u2e29\u3628\u20a4\u88fe\u4e19", 1698265578, 0x563A7761 ^ CryptoVault.mask()), uiHost);
            uiHost.captureKey(n2 -> {
                this.binding = BindTarget.NONE;
                this.backend.setFriendKey(n2);
                this.say(n2 == -1 ? PeopleView.xq("\u5005\u43dc\uf3d7\u2588\u6c69\u7868\uf67c\ud173\u31b3\ua2d4\uc403\u1fb1\u41f6\uf674\u56ac\u8f77\ucc39\u0391\uc403\u32a4", -1319796732, 0x563A7761 ^ CryptoVault.mask()) : PeopleView.xq("\u7d65\u58d3\u4a48\uf5d9\u5126\ub201\u4d50\u7152\u24ec\u8611\u8424\u4cfc\u551a\ufdb2\u74ee\u9260\u5181\u7fb2\ua407\ub879\u7750", -1220796686, 0x563A7761 ^ CryptoVault.mask()) + this.backend.friendKeyName() + ".", uiHost);
            });
        });
        UiKit.button(builder, uiHost, string2, f10, f3, f8, f4, f5, this.binding == BindTarget.ENEMY ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, bl, f6, f7, () -> {
            this.binding = BindTarget.ENEMY;
            this.say(PeopleView.xq("\uef3a\u0c75\u5a34\u6cfc\u34e8\u8b2f\u9d18\ua1f5\u9a8b\u786c\u9256\u2f2a\u1101\u9a40\u85b0\u65cb\ufa44\u94c6\u7daa\u5e3f\ue8f0\u6983\u60a3\u1737\ue27b\u21f7\ub1b3\uaf22\u6c34\uc24b\ub8cb\u9b38\u9c92\u93d4\uea6a\u714b\u47dd\u3e8c\ub16c\u7124\u9281\ub0a1\uc97d\u34d4\uc773\u5fab\ufb85\uaadd\u27c7\u8d68\u4489\u0590\u85fc\ube80\u9bca\uce44\ue918\uda9c\u0139\u40a0\ud8e7\ub957\u15fd\u3503\u3c58\uba35\uac20\ub4de\uef02\ucfd2\ubac9\u1c17\u6217\u2f6a\uc5e3\u4191\uf24b\u102e\ub555\u5b1d\u5eee\udc64\u8bec", 161658440, 0x563A7761 ^ CryptoVault.mask()), uiHost);
            uiHost.captureKey(n2 -> {
                this.binding = BindTarget.NONE;
                this.backend.setEnemyKey(n2);
                this.say(n2 == -1 ? PeopleView.xq("\u764b\u0568\u4dc2\u15fc\ud5e1\ufabd\u04ce\u5a17\uaf99\ueb99\ub58c\ua7b7\u982a\ud2d9\u8ceb\ucc14\udea5\u6242\u416a\u4a47\ua06f\u3756", -937981055, 0x563A7761 ^ CryptoVault.mask()) : PeopleView.xq("\u4816\u264c\u5388\udc66\udeb5\u371b\u3bfb\ucdd4\uf437\ub531\u05d1\u7a19\u7cc0\u54b3\u50fb\u54c3\udce6\uaebd\uc252", 996964052, 0x563A7761 ^ CryptoVault.mask()) + this.backend.enemyKeyName() + ".", uiHost);
            });
        });
    }

    private void drawList(UiDisplayList.Builder builder, UiHost uiHost, List<Person> list, boolean bl, float f2, float f3, float f4, float f5, float f6, float f7, float f8, UiSmoothScroll uiSmoothScroll, float[] fArray, String string) {
        fArray[0] = f2;
        fArray[1] = f3;
        fArray[2] = f4;
        fArray[3] = f5;
        float f9 = Math.round(48.0f * f6);
        float f10 = Math.round(6.0f * f6);
        float f11 = f9 + f10;
        uiSmoothScroll.update(uiHost.frameDelta(), f5, Math.max(0.0f, (float)list.size() * f11 - f10), uiHost.now());
        float f12 = uiSmoothScroll.offset();
        if (list.isEmpty()) {
            UiKit.emptyState(builder, uiHost, string, "", f2, f3 + (float)Math.round(30.0f * f6), f4, f6);
        }
        builder.clipPush(f2, f3, f4, f5, 0.0f);
        uiHost.beginHitClip(f2, f3, f4, f5);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f13;
            float f14;
            float f15;
            float f16;
            float f17 = f3 + (float)i2 * f11 - f12;
            if (f17 + f9 < f3 || f17 > f3 + f5) continue;
            Person person = list.get(i2);
            UiKit.card(builder, uiHost, f2, f17, f4, f9, f6);
            float f18 = Math.round(16.0f * f6);
            float f19 = Math.round(28.0f * f6);
            float f20 = f17 + (f9 - f19) * 0.5f;
            if (bl) {
                f16 = UiKit.buttonWidth(PeopleView.xq("\u5d7e\ub4ee\uab10D\u1365\u73bf\uf481\u5669\u4437", -343083379, 0x563A7761 ^ CryptoVault.mask()), f6);
                UiKit.button(builder, uiHost, PeopleView.xq("\u461a\u771b\u168d\uad3b\ub3b6\u98a8\u3681\u25cc\u9eeb\ucbdc", 389716207, 0x563A7761 ^ CryptoVault.mask()), f2 + f4 - f18 + (float)Math.round(4.0f * f6) - f16, f20, f16, f19, f6, UiKit.Style.DANGER, true, f7, f8, () -> this.say(this.backend.remove(person), uiHost));
                f15 = f4 - f18 * 2.0f - f16 - 70.0f * f6;
                builder.text(f2 + f18, GpuUiFontMetrics.topInBox(f17, f9, 11.5f * f6), 11.5f * f6, uiHost.textColor(), UiKit.fit(person.name(), f15, 11.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
                f14 = f2 + f4 - f18 - f16 - (float)Math.round(10.0f * f6);
                if (person.enemy()) {
                    f13 = GpuUiFontMetrics.textWidth(PeopleView.xq("\uba02\u3dd1\u43cd\u2284\u39c5\u08a2\u2866\u41d1\udaa5", -233084460, 0x563A7761 ^ CryptoVault.mask()), 8.5f * f6) + 14.0f * f6;
                    UiKit.chip(builder, uiHost, PeopleView.xq("\u74b5\u5143\uf51d\u723b\u5592\u5862\u48b6", -1344141282, 0x563A7761 ^ CryptoVault.mask()), f14 -= f13, f17 + (f9 - (float)Math.round(17.0f * f6)) * 0.5f, f6, -38037);
                    f14 -= (float)Math.round(6.0f * f6);
                }
                if (!person.friend()) continue;
                f13 = GpuUiFontMetrics.textWidth(PeopleView.xq("\u3d9c\u8f7b\u2c5b\ub54b\ub5e6\u637a\u7b68\u8f02\u8556", -367290993, 0x563A7761 ^ CryptoVault.mask()), 8.5f * f6) + 14.0f * f6;
                UiKit.chip(builder, uiHost, PeopleView.xq("\u07a3\ueb53\ucd80\u3add\u88b1\u066e\ue82c\u7018", 1481054471, 0x563A7761 ^ CryptoVault.mask()), f14 -= f13, f17 + (f9 - (float)Math.round(17.0f * f6)) * 0.5f, f6, uiHost.accent());
                continue;
            }
            f16 = UiKit.buttonWidth(PeopleView.xq("\u9083\uc5e4\u481c\ue6b2\ufa58\u924e\ua7b0\ue697\u4c9a\u1b66\u6862", -1955709796, 0x563A7761 ^ CryptoVault.mask()), f6);
            f15 = UiKit.buttonWidth(PeopleView.xq("\u2038\ud6c3\ua05b\u18fc\ubcfe\u246d\u78b6\u6493\uf476\ud2a0", 546975744, 0x563A7761 ^ CryptoVault.mask()), f6);
            f14 = f2 + f4 - f18 + (float)Math.round(4.0f * f6) - f16;
            f13 = f14 - (float)Math.round(6.0f * f6) - f15;
            UiKit.button(builder, uiHost, person.enemy() ? PeopleView.xq("\uc075\ub808\u1ffb\ua29f\u19fd\ue1f8\u9519\u8169\u07be\u36a4", 276747228, 0x563A7761 ^ CryptoVault.mask()) : PeopleView.xq("\u736f\uba52\u25a1\u55bc\u2b83\uaa60", -627212780, 0x563A7761 ^ CryptoVault.mask()), f14, f20, f16, f19, f6, person.enemy() ? UiKit.Style.DANGER : UiKit.Style.SECONDARY, true, f7, f8, () -> this.say(this.backend.toggleEnemy(person), uiHost));
            UiKit.button(builder, uiHost, person.friend() ? PeopleView.xq("\u5ee6\u5d40\u9be0\ue818\ubf41\u3841\u43cc\u0753\udbe0", -931013122, 0x563A7761 ^ CryptoVault.mask()) : PeopleView.xq("\ucc43\ud028\u2d77\u5252\u8a9a\u377c\uf25d\u3675\ua831\u5b3f", -213606507, 0x563A7761 ^ CryptoVault.mask()), f13, f20, f15, f19, f6, person.friend() ? UiKit.Style.DANGER : UiKit.Style.SECONDARY, true, f7, f8, () -> this.say(this.backend.toggleFriend(person), uiHost));
            builder.text(f2 + f18, GpuUiFontMetrics.topInBox(f17, f9, 11.5f * f6), 11.5f * f6, uiHost.textColor(), UiKit.fit(person.name(), f13 - f2 - f18 - 8.0f * f6, 11.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
        }
        uiHost.endHitClip();
        builder.clipPop();
        uiSmoothScroll.paintBar(builder, f2 + f4 + (float)Math.round(3.0f * f6), f3, f5, f6, uiHost.textColor(), uiHost.now());
    }

    private static /* synthetic */ String xq(String string, int n2, int n3) {
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
    private static enum Filter {
        ALL,
        FRIENDS,
        ENEMIES;


        private static /* synthetic */ String uw(String string, int n2, int n3) {
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
    private static enum BindTarget {
        NONE,
        FRIEND,
        ENEMY;


        private static /* synthetic */ String zx(String string, int n2, int n3) {
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
    public static interface Backend {
        public List<Person> saved();

        public List<Person> nearby();

        public String toggleFriend(Person var1);

        public String toggleEnemy(Person var1);

        public String remove(Person var1);

        default public int friendKey() {
            return c.A().T();
        }

        default public void setFriendKey(int n2) {
            c.A().B(n2);
        }

        default public String friendKeyName() {
            return c.A().S();
        }

        default public int enemyKey() {
            return b.A().S();
        }

        default public void setEnemyKey(int n2) {
            b.A().A(n2);
        }

        default public String enemyKeyName() {
            return b.A().O();
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record Person(String name, UUID uuid, boolean friend, boolean enemy) {
    }
}

