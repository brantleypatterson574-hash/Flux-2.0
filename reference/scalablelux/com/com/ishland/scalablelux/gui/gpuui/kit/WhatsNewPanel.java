/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.auth.DevlogClient;
import com.ishland.scalablelux.config.ClientPrefs;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class WhatsNewPanel {
    private static volatile List<DevlogClient.Entry> entries;
    private static volatile boolean loading;
    private boolean open;
    private float scroll;
    private float maxScroll;
    private final float[] listRect = new float[4];

    public static void preload() {
        if (entries != null || loading) {
            return;
        }
        loading = true;
        Thread thread = new Thread(() -> {
            List list;
            Object object;
            List<DevlogClient.Entry> list2;
            try {
                list2 = DevlogClient.fetchRecent();
                object = new ArrayList();
                if (list2 != null) {
                    for (DevlogClient.Entry entry : list2) {
                        if (entry == null) continue;
                        object.add(entry);
                    }
                }
                list = List.copyOf(object);
            }
            catch (Throwable throwable) {
                list = List.of();
            }
            list2 = list;
            object = () -> {
                entries = list2;
                loading = false;
            };
            try {
                class_310 class_3102 = class_310.method_1551();
                if (class_3102 != null) {
                    class_3102.execute((Runnable)object);
                } else {
                    object.run();
                }
            }
            catch (Throwable throwable) {
                object.run();
            }
        }, WhatsNewPanel.qd("\u5f92\u2497\u5b33\uc7c4\ue7a0\uff83\ucea2\u59a7\u2318\ue5e4\u18c0\u7078\u0767\uf86d\u956d", 412971689, 0x84456987 ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    static String idOf(DevlogClient.Entry entry) {
        return (entry.date == null ? "" : entry.date) + "|" + entry.title;
    }

    public static boolean hasUnread() {
        List<DevlogClient.Entry> list = entries;
        if (list == null || list.isEmpty()) {
            return false;
        }
        ClientPrefs clientPrefs = ClientPrefs.get();
        return clientPrefs.isOwned() && !WhatsNewPanel.idOf(list.get(0)).equals(clientPrefs.getLastSeenDevlog());
    }

    public boolean isOpen() {
        return this.open;
    }

    public void open() {
        this.open = true;
        this.scroll = 0.0f;
        WhatsNewPanel.preload();
    }

    public void close() {
        this.open = false;
    }

    public boolean scroll(double d2, double d3, double d4, float f2) {
        if (!this.open) {
            return false;
        }
        this.scroll = Math.max(0.0f, Math.min(this.maxScroll, this.scroll - (float)d4 * 40.0f * f2));
        return true;
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6) {
        if (!this.open) {
            return;
        }
        List<DevlogClient.Entry> list = entries;
        if (list != null && !list.isEmpty()) {
            ClientPrefs.get().setLastSeenDevlog(WhatsNewPanel.idOf(list.get(0)));
        }
        uiHost.hit(0.0f, 0.0f, f2, f3, this::close);
        builder.rect(0.0f, 0.0f, f2, f3, 0.0f, -1946157056);
        float f7 = Math.round(Math.min(560.0f * f4, f2 - 32.0f * f4));
        float f8 = Math.round(Math.min(540.0f * f4, f3 - 32.0f * f4));
        float f9 = Math.round((f2 - f7) * 0.5f);
        float f10 = Math.round((f3 - f8) * 0.5f);
        float f11 = Math.round(14.0f * f4 * uiHost.cornerScale());
        builder.shadow(f9, f10 + 2.0f * f4, f7, f8, f11, 14.0f * f4, 0.0f, Integer.MIN_VALUE);
        uiHost.panel(builder, f9, f10, f7, f8, f11);
        builder.border(f9, f10, f7, f8, f11, Math.max(1.0f, f4), UiKit.withAlpha(uiHost.accent(), 80));
        uiHost.hit(f9, f10, f7, f8, () -> {});
        float f12 = Math.round(20.0f * f4);
        builder.text(f9 + f12, f10 + f12, 16.0f * f4, uiHost.titleColor(), WhatsNewPanel.qd("\u0532\u6b69\u9670\ud218\u1b40\u4016\u1f77\u80b3\u6f2e\u8c48\u8fff\ua8b6", 555593786, 0x84456987 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f13 = UiKit.buttonWidth(WhatsNewPanel.qd("\uf38c\ub42b\uc3bc\ub573\u5ec0\u67e4\u7669\u5348\ub6f9", -481935784, 0x84456987 ^ CryptoVault.mask()), f4);
        float f14 = Math.round(28.0f * f4);
        UiKit.button(builder, uiHost, WhatsNewPanel.qd("\u0850\u66f9\ub254\u4c7e\u64a7\u5d5e\u861d", -224542432, 0x84456987 ^ CryptoVault.mask()), f9 + f7 - f12 - f13, f10 + f12 - 4.0f * f4, f13, f14, f4, UiKit.Style.SECONDARY, true, f5, f6, this::close);
        UiKit.divider(builder, uiHost, f9 + f12, f10 + f12 + 34.0f * f4, f7 - f12 * 2.0f);
        float f15 = f10 + f12 + 44.0f * f4;
        float f16 = f10 + f8 - f12;
        this.listRect[0] = f9;
        this.listRect[1] = f15;
        this.listRect[2] = f7;
        this.listRect[3] = f16 - f15;
        if (list == null) {
            builder.text(f9 + f12, f15 + 8.0f * f4, 11.0f * f4, uiHost.mutedColor(), WhatsNewPanel.qd("\u48ad\uba29\u4932\u75df\u7c35\u37f6\ube1d\u5a12\u86cf\u81ec\u57b1\ud1f9\uc103\ud900\u1caf\ud48d\u3797\udb93\uf52b\ud5a7\ufbad\ud1e5\u07a6\u3b3c\ubf0b", 1526504719, 0x84456987 ^ CryptoVault.mask()));
            return;
        }
        if (list.isEmpty()) {
            builder.text(f9 + f12, f15 + 8.0f * f4, 11.0f * f4, uiHost.mutedColor(), WhatsNewPanel.qd("\uf76e\ue18a\u35ad\uffa7\u8906\u0fac\ub3e8\u8b7f\u71ee\u91df\u0c0a\u353a\ud28b\u027f\u63ec\u34be\u9159\u6d4b\u5bb1\u4666\u80c0\u3597\u7216\uaab5\u1b40\u27e4\u7a1f\u8805\u4dfa\ucf41\ucb69\u9376\u3117\ub8f3\uf484\u3d96\uaff1\u434e\ue05c\u1ad1\u17f8\u8a32\u20f3\u318d\u441f\ue977\u89bd", -1612824859, 0x84456987 ^ CryptoVault.mask()));
            return;
        }
        float f17 = f7 - f12 * 2.0f;
        builder.clipPush(f9, f15, f7, f16 - f15, 0.0f);
        float f18 = f15 - this.scroll;
        float f19 = 10.5f * f4;
        float f20 = Math.round(15.0f * f4);
        for (DevlogClient.Entry entry : list) {
            builder.text(f9 + f12, f18, 13.0f * f4, uiHost.textColor(), UiKit.fit(entry.title, f17, 13.0f * f4, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            f18 += (float)Math.round(19.0f * f4);
            StringBuilder stringBuilder = new StringBuilder();
            if (entry.date != null && !entry.date.isBlank()) {
                stringBuilder.append(entry.date);
            }
            if (entry.version != null && !entry.version.isBlank()) {
                stringBuilder.append(stringBuilder.isEmpty() ? "" : WhatsNewPanel.qd("\ucc75\u9c38\udfa7\ue71b\uffa9\u1359\u092f\uf3c6\uca89", -2020894829, 0x84456987 ^ CryptoVault.mask())).append(entry.version);
            }
            if (entry.tag != null && !entry.tag.isBlank()) {
                stringBuilder.append(stringBuilder.isEmpty() ? "" : WhatsNewPanel.qd("\uc02e\u5f86\u0544\u0c4e\u3ffd\u2a23\ua8da\u3914", -1147732185, 0x84456987 ^ CryptoVault.mask())).append(entry.tag);
            }
            if (!stringBuilder.isEmpty()) {
                builder.text(f9 + f12, f18, 9.0f * f4, uiHost.accent(), UiKit.fit(stringBuilder.toString(), f17, 9.0f * f4));
                f18 += (float)Math.round(16.0f * f4);
            }
            if (entry.body != null) {
                for (String string : entry.body) {
                    List<String> list2 = UiKit.wrap(string, f17 - 14.0f * f4, f19, 8);
                    for (int i2 = 0; i2 < list2.size(); ++i2) {
                        if (i2 == 0) {
                            builder.text(f9 + f12, f18, f19, uiHost.accent(), "\u2022");
                        }
                        builder.text(f9 + f12 + 14.0f * f4, f18, f19, uiHost.mutedColor(), list2.get(i2));
                        f18 += f20;
                    }
                }
            }
            UiKit.divider(builder, uiHost, f9 + f12, (f18 += (float)Math.round(14.0f * f4)) - (float)Math.round(8.0f * f4), f17);
        }
        builder.clipPop();
        this.maxScroll = Math.max(0.0f, f18 + this.scroll - f15 - (f16 - f15));
        this.scroll = Math.min(this.scroll, this.maxScroll);
    }

    private static /* synthetic */ String qd(String string, int n2, int n3) {
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

