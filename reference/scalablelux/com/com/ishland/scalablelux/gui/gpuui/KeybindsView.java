/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.config.ConfigKeybindManager;
import com.ishland.scalablelux.e.b;
import com.ishland.scalablelux.g.c;
import com.ishland.scalablelux.gui.gpuui.BindModeControl;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
import com.ishland.scalablelux.gui.gpuui.kit.KeybindCapture;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.manager.ModuleManager;
import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.module.hooks.StudioProvider;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.ModuleAccess;
import com.ishland.scalablelux.util.PremiumGate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.IntConsumer;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class KeybindsView {
    private final UiTextField search = new UiTextField(KeybindsView.hp("\u7b20\u2119\u19b2\uf8b8\u655c\u363d\u0bde\ud676\u2805\u49bf\u5171\u4fd9\uab35\ucca7\ud95e\u8afb\uba0d", 443455614, 0x3DC366A2 ^ CryptoVault.mask())).maxLength(40);
    private final UiSmoothScroll scroll = new UiSmoothScroll();
    private final float[] listRect = new float[4];
    private boolean sortByKey;
    private String capturingId;
    private String status = "";
    private long statusAt = Long.MIN_VALUE;
    private float lastScale = 1.0f;
    private final boolean showTitle;

    public KeybindsView() {
        this(true);
    }

    public KeybindsView(boolean bl) {
        this.showTitle = bl;
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        if (d2 < (double)this.listRect[0] || d2 > (double)(this.listRect[0] + this.listRect[2]) || d3 < (double)this.listRect[1] || d3 > (double)(this.listRect[1] + this.listRect[3])) {
            return false;
        }
        this.scroll.wheel(d4, 56.0f * this.lastScale, l2);
        return true;
    }

    public void reset() {
        this.capturingId = null;
        this.search.setValue("");
        this.scroll.reset();
    }

    private void say(String string, UiHost uiHost) {
        this.status = string == null ? "" : string;
        this.statusAt = uiHost.now();
    }

    private static String keyLabel(int n2) {
        return n2 == -1 || n2 == 0 ? KeybindsView.hp("\u9955\ued19\u9b6f\u95cd\u06bd\u6c7f\u2b97\uf433", -1123339774, 0x3DC366A2 ^ CryptoVault.mask()) : Module.getSingleKeyName(n2);
    }

    private static boolean bound(int n2) {
        return n2 != -1 && n2 != 0;
    }

    private List<Entry> collect() {
        StudioProvider studioProvider;
        int n3;
        int n4;
        int n5;
        Object object;
        ModuleManager moduleManager;
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        SlApp slApp = SlApp.getInstance();
        ModuleManager moduleManager2 = moduleManager = slApp == null ? null : slApp.getModuleManager();
        if (moduleManager != null) {
            for (Module object32 : moduleManager.getModules()) {
                if (object32 == null || object32.isHidden() || object32 instanceof HitboxConfigScreen752809) continue;
                if (KeybindsView.bound(object32.getKeyCode())) {
                    boolean bl = BindModeControl.available(object32);
                    arrayList.add(new Entry("m:" + object32.getName(), Group.MODULES, object32.getName(), object32.getCategory().zw, object32.getKeyDisplayName(), bl ? BindModeControl.label(object32) : object32.getBindModeLabel(), bl ? () -> BindModeControl.toggle(object32) : null, n2 -> object32.setKeyCode(n2), () -> object32.setKeyCode(-1)));
                }
                for (Setting setting : object32.getSettings()) {
                    if (!(setting instanceof BooleanSetting) || !((BooleanSetting)(object = (BooleanSetting)setting)).isBindable() || !KeybindsView.bound(((BooleanSetting)object).getKeyCode())) continue;
                    arrayList.add(new Entry("s:" + object32.getName() + ":" + ((Setting)object).getName(), Group.MODULES, ((Setting)object).getName(), object32.getName(), ((BooleanSetting)object).getKeyDisplayName(), ((BooleanSetting)object).isKeyHoldMode() ? KeybindsView.hp("\ud48b\u4d30G\u6567\u95db\u396c", 1512415293, 0x3DC366A2 ^ CryptoVault.mask()) : KeybindsView.hp("\ua5f6\u813e\uecce\uab76\uc3df\u3bc6\uacf0", 799720666, 0x3DC366A2 ^ CryptoVault.mask()), () -> KeybindsView.lambda$collect$3((BooleanSetting)object), ((BooleanSetting)object)::setKeyCode, () -> KeybindsView.lambda$collect$4((BooleanSetting)object)));
                }
            }
        }
        ConfigKeybindManager configKeybindManager = ConfigKeybindManager.getInstance();
        for (ConfigKeybindManager.ConfigKeybind configKeybind : configKeybindManager.getKeybinds()) {
            if (configKeybind.configName == null || configKeybind.configName.isBlank() || !KeybindsView.bound(configKeybind.key1)) continue;
            String string = configKeybind.configName;
            arrayList.add(new Entry("c:" + string, Group.CONFIGS, string, KeybindsView.hp("\u8e4d\ucf36\ubb9d\u63b6\u72f1\ufff1\u09de\u69d2\uc728", -1395120986, 0x3DC366A2 ^ CryptoVault.mask()), configKeybind.getDisplayName(), null, null, n2 -> configKeybindManager.setKeybind(string, n2, -1), () -> configKeybindManager.setKeybind(string, -1, -1)));
        }
        Module module = moduleManager == null ? null : moduleManager.getModule(KeybindsView.hp("\uf8b1\u945e\uab18\u5ada\u3edd\uc9ed\ua455\ub1d8\uee54", -188204263, 0x3DC366A2 ^ CryptoVault.mask()));
        int n6 = n5 = module == null ? -1 : module.getKeyCode();
        if (!KeybindsView.bound(n5)) {
            n4 = 344;
        }
        arrayList.add(new Entry(KeybindsView.hp("\ub741\ufc72\u9760\u156a\u68c3\u7d6b\ua64a\u25dd\ud312", 2054382152, 0x3DC366A2 ^ CryptoVault.mask()), Group.CLIENT, KeybindsView.hp("\u3c78\u3464\ueb79\u419e\u5e2f\ua4b5\u1948\u58f6\u91f5\uf11d\u62c8", -2040036303, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.hp("\u2d96\uede4\u47db\u69f8\u2ea1\u86d8\uf3f3\u152d", 28079453, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.keyLabel(n4), null, null, n2 -> {
            if (module != null) {
                module.setKeyCode(n2);
            }
        }, null));
        c c2 = c.A();
        if (KeybindsView.bound(c2.T())) {
            arrayList.add(new Entry(KeybindsView.hp("\ud22a\u18ae\u0d8f\ud181\u3833\u3ef5\u7be8\u848a\u3fb6\u9100\u317d\ub66e", 112010023, 0x3DC366A2 ^ CryptoVault.mask()), Group.CLIENT, KeybindsView.hp("\u5181\u623d\udb89\ue7f3\ue3c1\uf4eb\uf5b9\u7766\uc8a1\ubc57\ud661\u4e66\u62f0\ud064", -166627810, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.hp("\ub3fa\u4633\u0f5c\ude21\u1f1b\ubb52\ub5f9\ud58f\ub6d3\u804c", 420225532, 0x3DC366A2 ^ CryptoVault.mask()), c2.S(), null, null, c2::B, () -> c2.B(-1)));
        }
        if (KeybindsView.bound(((b)(object = b.A())).S())) {
            arrayList.add(new Entry(KeybindsView.hp("\u89de\u025d\ud327\ua98a\uef59\u48c6\u8069\u1c79\u83e2\u4b9e\ud5cc", 1422037796, 0x3DC366A2 ^ CryptoVault.mask()), Group.CLIENT, KeybindsView.hp("\u124f\u8c0e\u9032\u3342\u51f9\u5de1\u4ca3\u83f4\uc01f\u2940\uc5ee\u4d81", -137935513, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.hp("\ue28f\ua7f2\uc3e5\u3d90\u0806\u83e6\uf988\ueab8\u2d62", -766042783, 0x3DC366A2 ^ CryptoVault.mask()), ((b)object).O(), null, null, ((b)object)::A, () -> KeybindsView.lambda$collect$9((b)object)));
        }
        if (PremiumGate.cosmetics() && (n3 = com.ishland.scalablelux.d.c.A().A().R()) >= 0) {
            int n7 = n3 >= 500 ? KeybindCapture.encodeMouse(n3 - 500) : n3;
            com.ishland.scalablelux.d.c c3 = com.ishland.scalablelux.d.c.A();
            arrayList.add(new Entry(KeybindsView.hp("\u86d0\ub55f\u04f4\u7126\u7205\uce5f\uc7de\u8016\uefeb\u7796", -2016018270, 0x3DC366A2 ^ CryptoVault.mask()), Group.CLIENT, KeybindsView.hp("\u6c74\uf1bb\ue5b6\u8d1b\u7a13\ueb81\ue750\u717f\u1e94\udf98\u73ea\u4d26\u4ef0\uaf27\ub15e\ue502\ud220\ud172", -1353079900, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.hp("\u1de9\uccff\u70f4\ue5aa\u3239\ub1c9\u0746\u6f0d\uf379\u62b4", 414985857, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.keyLabel(n7), null, null, n2 -> c3.A(c3.A().D(KeybindCapture.isMouseCode(n2) ? 500 + KeybindCapture.decodeMouse(n2) : n2)), () -> c3.A(c3.A().D(-1))));
        }
        StudioProvider studioProvider2 = studioProvider = PremiumGate.studio() ? ModuleAccess.getHook(StudioProvider.class) : null;
        if (studioProvider != null) {
            for (StudioProvider.MacroBind macroBind : studioProvider.macroBinds()) {
                if (!KeybindsView.bound(macroBind.keyCode())) continue;
                String string = macroBind.id();
                arrayList.add(new Entry("x:" + string, Group.CLIENT, macroBind.name(), KeybindsView.hp("\uea5d\uf503\u2080\ubb84\u9776\ue5ad", 432600967, 0x3DC366A2 ^ CryptoVault.mask()), macroBind.keyName(), null, null, n2 -> studioProvider.setMacroKey(string, n2), () -> studioProvider.setMacroKey(string, -1)));
            }
        }
        return arrayList;
    }

    private boolean matches(Entry entry, String string) {
        if (string.isEmpty()) {
            return true;
        }
        return entry.name().toLowerCase(Locale.ROOT).contains(string) || entry.section().toLowerCase(Locale.ROOT).contains(string) || entry.keyName().toLowerCase(Locale.ROOT).contains(string) || entry.mode() != null && entry.mode().toLowerCase(Locale.ROOT).contains(string);
    }

    private void beginCapture(Entry entry, UiHost uiHost) {
        this.capturingId = entry.id();
        this.say(KeybindsView.hp("\ud939\u5458\u3bcc\u0fa4\u2d0e\u4f84\u138d\u5aa6\ua551\ud742\u84e7\u4ce0\ufd71\u4aea\ubfdb\u69d4\uaa94\u4b2d\u2f22\ubdba\u15fd\u0a52\u095c\ucd19\u149b\ue85b\u3383\u4dc4\uc276\u345b\u230c\u3877\u1f5b\ube14\u09a3\u82c7", -1521542366, 0x3DC366A2 ^ CryptoVault.mask()) + entry.name() + KeybindsView.hp("\u853f\u74a5\u9910\uace0\u8349\ub51d\u2947\ueeb0\uc736\u3b1a\u7461\u6528\u908c\u7df2\u42c3\u7258\u102c\ucfba\ucefe\uf93a\u96b4\u2c36\u1c6a\u2222\u956d\u81ca\u021b\u54b4\u9baf\ucf8d\u117f\u431c\uc8b1\u228e\uc018\u30b9", 459030122, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
        uiHost.captureKey(n2 -> {
            this.capturingId = null;
            if (n2 == -1) {
                this.say(KeybindsView.hp("\ubf82\u1380\uceb2\uea6d\ub70c\u413f\u26eb\u0651\uddd9\u5b43\u559f\ufe83\u5fbf\u6409\uceb1\u815d\ub9ad\uf19c\u9a38\ue3b5\ud732", 1792965875, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
                return;
            }
            if (n2 == 259 || n2 == 261) {
                if (entry.clear() == null) {
                    this.say(entry.name() + KeybindsView.hp("\ufcad\u1ce9\u38d2\u63cb\uf0d1\u7f5c\u3e37\u0afc\uafad\u23c5\u44b3\u7ea3\u9229\uccd0\u6d54\ua199\ucd41\ud286\u8098\uc2b4", 242833010, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
                } else {
                    entry.clear().run();
                    this.say(entry.name() + KeybindsView.hp("\u8d5d\u3725\ud365\uf87d\u5d34\u05f3\u1302\ud4d8\uf1b8\uec7d\ucb43", -1481208551, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
                }
                return;
            }
            entry.rebind().accept(n2);
            this.say(entry.name() + KeybindsView.hp("\u1e91\u973b\uf227\u3224\u0999\u8928\u8572\uf83a\uab68", 1370201014, 0x3DC366A2 ^ CryptoVault.mask()) + KeybindsView.keyLabel(n2) + ".", uiHost);
        });
    }

    /*
     * WARNING - void declaration
     */
    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        void f28;
        this.lastScale = f6;
        if (this.capturingId != null && !uiHost.capturingKey()) {
            this.capturingId = null;
        }
        float f9 = Math.round(22.0f * f6);
        List<Entry> list = this.collect();
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for (Entry object2 : list) {
            hashMap.merge(object2.keyName(), 1, Integer::sum);
        }
        int n3 = 0;
        Iterator f10 = hashMap.values().iterator();
        while (f10.hasNext()) {
            int f11 = (Integer)f10.next();
            if (f11 <= 1) continue;
            n3 += f11;
        }
        float f11 = 0.0f;
        if (this.showTitle) {
            UiKit.title(builder, uiHost, KeybindsView.hp("\u6a27\u96c4\uc3dc\ucc71\u0791\u0d56\u0f85\u502c\u7fab\u2576", 1893924729, 0x3DC366A2 ^ CryptoVault.mask()), f2 + f9, f3 + (float)Math.round(20.0f * f6), f6);
            f11 = GpuUiFontMetrics.textWidth(KeybindsView.hp("\ua5c7\udf0f\u2f74\u974a\u49dc\u7bfe\uc688\u886c\u1dda", -942644321, 0x3DC366A2 ^ CryptoVault.mask()), 16.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2 + f9 + f11 + (float)Math.round(8.0f * f6), f3 + (float)Math.round(24.0f * f6), 10.0f * f6, uiHost.muted2Color(), String.valueOf(list.size()));
        }
        float f12 = Math.round(120.0f * f6);
        float f13 = Math.round(30.0f * f6);
        float f14 = f3 + (float)Math.round(14.0f * f6);
        float f15 = f2 + f4 - f9 - f12;
        UiKit.segmented(builder, uiHost, new String[]{KeybindsView.hp("\uff92\u4d8b\u3daa\ub7e2\u9058\u58fe", 703932154, 0x3DC366A2 ^ CryptoVault.mask()), KeybindsView.hp("\ua7f5\u4041\u8537\u8910", 1860573359, 0x3DC366A2 ^ CryptoVault.mask())}, this.sortByKey ? 1 : 0, f15, f14, f12, f13, f6, f7, f8, n2 -> {
            this.sortByKey = n2 == 1;
        });
        float f16 = GpuUiFontMetrics.textWidth(KeybindsView.hp("\u12a8\u18d4\u5459\ud70c\ufd42\u502e\u27d3", 514029274, 0x3DC366A2 ^ CryptoVault.mask()), 9.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
        float f17 = f15 - (float)Math.round(10.0f * f6) - f16 - (float)Math.round(8.0f * f6);
        builder.text(f17 + (float)Math.round(8.0f * f6), GpuUiFontMetrics.topInBox(f14, f13, 9.5f * f6), 9.5f * f6, uiHost.muted2Color(), KeybindsView.hp("\u9133\uacbc\ufa70\uc786\u043f", -257393988, 0x3DC366A2 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f18 = f2 + f9 + (this.showTitle ? f11 + (float)Math.round(52.0f * f6) : 0.0f);
        float f19 = Math.min((float)Math.round(this.showTitle ? 240.0f : 320.0f) * f6, f17 - f18);
        if (f19 >= (float)Math.round(90.0f * f6)) {
            this.search.draw(builder, uiHost, f17 - f19, f14, f19, f13, f6);
        }
        UiKit.divider(builder, uiHost, f2 + f9, f3 + (float)Math.round(56.0f * f6), f4 - f9 * 2.0f);
        String string = this.search.value().trim().toLowerCase(Locale.ROOT);
        float f20 = f3 + (float)Math.round(66.0f * f6);
        float f21 = f3 + f5 - (float)Math.round(32.0f * f6);
        float f22 = f2 + f9;
        float f23 = f4 - f9 * 2.0f;
        this.listRect[0] = f22;
        this.listRect[1] = f20;
        this.listRect[2] = f23;
        this.listRect[3] = f21 - f20;
        Comparator<Entry> comparator = this.sortByKey ? Comparator.comparing(Entry::keyName, String.CASE_INSENSITIVE_ORDER).thenComparing(Entry::name, String.CASE_INSENSITIVE_ORDER) : Comparator.comparing(Entry::name, String.CASE_INSENSITIVE_ORDER);
        float f24 = Math.round(36.0f * f6);
        float f25 = Math.round(5.0f * f6);
        float f26 = Math.round(24.0f * f6);
        ArrayList<String> arrayList = new ArrayList<String>();
        int n4 = 0;
        Group[] f262 = Group.values();
        int n5 = f262.length;
        boolean e2 = false;
        while (f28 < n5) {
            Group group = f262[f28];
            ArrayList<Entry> arrayList2 = new ArrayList<Entry>();
            for (Entry entry : list) {
                if (entry.group() != group || !this.matches(entry, string)) continue;
                arrayList2.add(entry);
            }
            if (!arrayList2.isEmpty()) {
                arrayList2.sort(comparator);
                arrayList.add(group.label + " (" + arrayList2.size() + ")");
                arrayList.addAll(arrayList2);
                n4 += arrayList2.size();
            }
            ++f28;
        }
        float f27 = 0.0f;
        for (Object e3 : arrayList) {
            f27 += e3 instanceof Entry ? f24 + f25 : f26;
        }
        this.scroll.update(uiHost.frameDelta(), f21 - f20, Math.max(0.0f, f27 - f25), uiHost.now());
        float f29 = this.scroll.offset();
        if (arrayList.isEmpty()) {
            UiKit.emptyState(builder, uiHost, list.isEmpty() ? KeybindsView.hp("\u879b\u0f6d\ud17d\u20dd\u1f33\uf5be\ue737\ub604\uaa12\ue468\u6a33\uda5e\uded4\u7828\u553b\u51cb\uad03\uab80\ud105\ufb31\u0397\uaabd\u1907\u6b99", -1897641656, 0x3DC366A2 ^ CryptoVault.mask()) : KeybindsView.hp("\uc392\ud162\uca82\u22ab\u31b7\uaaea\ue6b1\ud01f\u0b84\u02d2\u46bb\u5b6a\u8943\u52a7\ua621\u94c0\u6a82\ud0f5\ub05d\u6835\u2c10\u4fda\ucfd4\ub346\ubf9e\ub39d\ubffb\u8522\u30c5\u723c\u9dd1\u4e4a\u3fa3\ucc33", -283143192, 0x3DC366A2 ^ CryptoVault.mask()), list.isEmpty() ? KeybindsView.hp("\u0e1f\uf429\u3d4e\u985a\u25c6\u8759\ufd7b\u920e\u765a\uabbb\ud668\u57bc\u9447\ufa1e\ude5a\u1158\u1d88\u2f4e\u1a14\uf284\ud11d\u4c0e\udd82\ue1c7\uc61c\u2d47\uf441\u9eea\ud4d1\u06b7\u2f6d\ucec6\ue51a\u5d2a\u952d\u7e5b\ub3df\u0dde\u034b\ufa4b\ueeff\u7894\u6f46\u5212\uc114\u46fc\u1762\u7fc2\u4bc1\ude21\ub3f6\u69a0\uc50f\ue5c4\uc1ce\uabe5\uf42d", 1585932682, 0x3DC366A2 ^ CryptoVault.mask()) : "", f22, f20 + (float)Math.round(30.0f * f6), f23, f6);
        }
        builder.clipPush(f22, f20, f23, f21 - f20, 0.0f);
        uiHost.beginHitClip(f22, f20, f23, f21 - f20);
        float f30 = f20 - f29;
        for (Object e4 : arrayList) {
            Object object;
            if (e4 instanceof String) {
                object = (String)e4;
                if (f30 + f26 >= f20 && f30 <= f21) {
                    UiKit.label(builder, uiHost, (String)object, f22, f30 + (float)Math.round(6.0f * f6), f6);
                }
                f30 += f26;
                continue;
            }
            object = (Entry)e4;
            if (f30 + f24 >= f20 && f30 <= f21) {
                this.drawRow(builder, uiHost, (Entry)object, hashMap.getOrDefault(((Entry)object).keyName(), 0) > 1, f22, f30, f23, f24, f6, f7, f8);
            }
            f30 += f24 + f25;
        }
        uiHost.endHitClip();
        builder.clipPop();
        this.scroll.paintBar(builder, f22 + f23 + (float)Math.round(3.0f * f6), f20, f21 - f20, f6, uiHost.textColor(), uiHost.now());
        UiKit.divider(builder, uiHost, f2 + f9, f21 + (float)Math.round(4.0f * f6), f4 - f9 * 2.0f);
        float f31 = f21 + (float)Math.round(14.0f * f6);
        if (!this.status.isEmpty() && uiHost.now() - this.statusAt < 5000000000L) {
            builder.text(f22, f31, 10.0f * f6, uiHost.accent(), UiKit.fit(this.status, f23, 10.0f * f6));
        } else if (n3 > 0) {
            builder.text(f22, f31, 10.0f * f6, -38037, UiKit.fit(n3 + KeybindsView.hp("\u2de2\udfeb\uc553\u4f02\u8d70\uac9b\u0b81\u3d7e\u6b6e\ue5e7\u7599\u69a8\u24ff\u360b\uf601\uae06\uff56\u957a\u987a\u260b\uab65\u1d1d\u252e\u8bb3\uf403\u0c1a\uc9c4\u5cb7\u520e\u2d5c\u264b\ud24e\u4a28\u3863\u91b2\u62d0\u6949\u33bf\ub055\u2cc4\u2397\u928a\u55cf\u40cd\ue501\uecf1\u4154\ue28b\u5025\u6519\u5968\u0257\u2c8c", 1353698827, 0x3DC366A2 ^ CryptoVault.mask()), f23, 10.0f * f6));
        } else {
            builder.text(f22, f31, 10.0f * f6, uiHost.muted2Color(), UiKit.fit(KeybindsView.hp("\ufd1c\u622d\ud362\ufef3\u6795\u2b0a\uea73\u8c1c\u150e\u1ca4\u625e\u9623\u6bf1\ued09\uc9cb\u4d7d\u5538\ud82a\u7462\u7742\u1554\uc39e\u4437\u36f6\u2a60\ub038\u08f9\u3c6d\ufa16\uf98b\u187b\ue153\u9989\u74b4\u08e0\ue917\uab9c\u8ffe\ua9fe\u63ed\u1058\u1a5e\uabbe\u3467\uf2a4\udfae\ud159\u331b\u03c9\ua810\u016b\u9927\u1b3a\ued5b\uee15\ua3b5\ubbbe\u7368\u81f2\u4d99\uba9d\uafd8\uf64f\ubafd\ub525\ua77f\ufa8f\u4cb1\ue12c\u64e2\ub215\u8044\ue20d\u6d4d\u18ac\u2286\ufd5d\u0007\ue214\ufc82\u2a90\u65ca\u4012\u843b\uc299\u4a0f\u2b16\u5f0a\u02ba\u45c9\ue3b2\ue391\u0e30\u0dbc\u5422\ue184\u84f4\u3650\u8a61\u2ced\ua6b0", -80308701, 0x3DC366A2 ^ CryptoVault.mask()), f23, 10.0f * f6));
        }
    }

    private void drawRow(UiDisplayList.Builder builder, UiHost uiHost, Entry entry, boolean bl, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10;
        float f11;
        boolean bl2;
        float f12;
        float f13;
        UiKit.card(builder, uiHost, f2, f3, f4, f5, f6);
        float f14 = Math.round(14.0f * f6);
        float f15 = Math.round(24.0f * f6);
        float f16 = f3 + (f5 - f15) * 0.5f;
        float f17 = f2 + f4 - f14;
        if (entry.clear() != null) {
            float f18;
            boolean bl3 = uiHost.hovered(f7, f8, f17 - (f18 = f15), f16, f18, f15);
            builder.circle(f17 - f18 * 0.5f, f16 + f15 * 0.5f, f18 * 0.5f, bl3 ? UiKit.withAlpha(-38037, 64) : uiHost.controlFill(28));
            f13 = 12.0f * f6;
            f12 = GpuUiFontMetrics.textWidth("\u00d7", f13, UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f17 - f18 * 0.5f - f12 * 0.5f, GpuUiFontMetrics.topInBox(f16, f15, f13), f13, bl3 ? -25958 : uiHost.mutedColor(), "\u00d7", UiDrawCommand.TextWeight.SEMIBOLD);
            uiHost.hit(f17 - f18, f16, f18, f15, () -> {
                entry.clear().run();
                this.say(entry.name() + KeybindsView.hp("\u1428\u6664\u6c7e\uf30f\u2413\uf92d\u62e6\u9267\ub83f\u2e5e\u3528\ue4cf\u4903", -1226763572, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
            });
            f17 -= f18 + (float)Math.round(8.0f * f6);
        }
        String string = (bl2 = entry.id().equals(this.capturingId)) ? KeybindsView.hp("\u1862\u8092\ucfbf\uac58\ub721\u76d9\u3ecd\u5f8b\u8537\uf8dd\ucde7\u11d1\uddba\ube11\u05fe", -1524621448, 0x3DC366A2 ^ CryptoVault.mask()) : entry.keyName();
        f13 = 9.5f * f6;
        f12 = Math.max(Math.round(64.0f * f6), Math.round(GpuUiFontMetrics.textWidth(string, f13, UiDrawCommand.TextWeight.SEMIBOLD) + 24.0f * f6));
        float f19 = f17 - f12;
        boolean bl4 = uiHost.hovered(f7, f8, f19, f16, f12, f15);
        float f20 = f15 * 0.5f * Math.min(1.0f, uiHost.cornerScale());
        builder.rect(f19, f16, f12, f15, f20, bl2 ? UiKit.withAlpha(uiHost.accent(), 64) : (bl ? UiKit.withAlpha(-38037, bl4 ? 52 : 34) : uiHost.controlFill(bl4 ? 48 : 26)));
        builder.border(f19, f16, f12, f15, f20, 1.0f, bl2 ? uiHost.accent() : (bl ? UiKit.withAlpha(-38037, 144) : uiHost.lineColor()));
        String string2 = UiKit.fit(string, f12 - 14.0f * f6, f13, UiDrawCommand.TextWeight.SEMIBOLD);
        float f21 = GpuUiFontMetrics.textWidth(string2, f13, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f19 + (f12 - f21) * 0.5f, GpuUiFontMetrics.topInBox(f16, f15, f13), f13, bl2 ? uiHost.accent() : (bl ? -25958 : uiHost.textColor()), string2, UiDrawCommand.TextWeight.SEMIBOLD);
        uiHost.hit(f19, f16, f12, f15, () -> this.beginCapture(entry, uiHost));
        f17 = f19 - (float)Math.round(8.0f * f6);
        if (entry.mode() != null) {
            String string3 = entry.mode().substring(0, 1).toUpperCase(Locale.ROOT) + entry.mode().substring(1).toLowerCase(Locale.ROOT);
            f11 = 8.5f * f6;
            f10 = Math.round(GpuUiFontMetrics.textWidth(string3, f11, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f6);
            float f22 = Math.round(18.0f * f6);
            f9 = f3 + (f5 - f22) * 0.5f;
            boolean bl5 = entry.cycleMode() != null;
            boolean bl6 = bl5 && uiHost.hovered(f7, f8, f17 - f10, f9, f10, f22);
            int n2 = KeybindsView.hp("\uf688\u95ac\uc863\u6807\u91ee\uece5\u44a8\u3628", -582095180, 0x3DC366A2 ^ CryptoVault.mask()).equals(string3) ? uiHost.accent() : uiHost.mutedColor();
            builder.rect(f17 - f10, f9, f10, f22, f22 * 0.5f * Math.min(1.0f, uiHost.cornerScale()), UiKit.withAlpha(n2, bl6 ? 52 : 28));
            builder.text(f17 - f10 + 9.0f * f6, GpuUiFontMetrics.topInBox(f9, f22, f11), f11, n2, string3, UiDrawCommand.TextWeight.SEMIBOLD);
            if (bl5) {
                uiHost.hit(f17 - f10, f9, f10, f22, () -> {
                    entry.cycleMode().run();
                    this.say(entry.name() + KeybindsView.hp("\ud2f9\u67c3\u7256\u77a3\u2892\u12fe\u0f33\uf39f\u176c\u25bd\ud20d\ue2b9\u2f9e\u7894\uf9b9", -1367571826, 0x3DC366A2 ^ CryptoVault.mask()), uiHost);
                });
            }
            f17 -= f10 + (float)Math.round(10.0f * f6);
        }
        float f23 = 11.5f * f6;
        f11 = 9.0f * f6;
        f10 = Math.max(0.0f, f17 - (f2 + f14));
        String string4 = UiKit.fit(entry.name(), f10 * 0.7f, f23, UiDrawCommand.TextWeight.SEMIBOLD);
        f9 = GpuUiFontMetrics.textWidth(string4, f23, UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2 + f14, GpuUiFontMetrics.topInBox(f3, f5, f23), f23, uiHost.textColor(), string4, UiDrawCommand.TextWeight.SEMIBOLD);
        float f24 = f2 + f14 + f9 + (float)Math.round(10.0f * f6);
        float f25 = f17 - f24;
        if (f25 > (float)Math.round(30.0f * f6)) {
            builder.text(f24, GpuUiFontMetrics.topInBox(f3, f5, f11), f11, uiHost.muted2Color(), UiKit.fit(entry.section(), f25, f11));
        }
    }

    private static /* synthetic */ void lambda$collect$9(b b2) {
        b2.A(-1);
    }

    private static /* synthetic */ void lambda$collect$4(BooleanSetting booleanSetting) {
        booleanSetting.setKeyCode(-1);
    }

    private static /* synthetic */ void lambda$collect$3(BooleanSetting booleanSetting) {
        booleanSetting.setKeyHoldMode(!booleanSetting.isKeyHoldMode());
    }

    private static /* synthetic */ String hp(String string, int n2, int n3) {
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
    private record Entry(String id, Group group, String name, String section, String keyName, String mode, Runnable cycleMode, IntConsumer rebind, Runnable clear) {
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Group {
        MODULES(Group.or("\u21f5\u04db\u5d66\udcbf\uc540\u2cb8\ubc4e\uabd8\u47f1\u029b\u297d", -1766190650, 0xDA65F71B ^ CryptoVault.mask())),
        CONFIGS(Group.or("\uabb8\ubd50\udcd3\u617b\u987e\u3ea8\u123d\u2708\u2b6b\u3d46\u708c\u83a9\ucaa3\u1828\udbb2", 1625048746, 0xDA65F71B ^ CryptoVault.mask())),
        CLIENT(Group.or("\u5991\uebd8\uc8c5\u0632\u7819\uc7eb\u0d50\u6e37\u7b9b\ued91\u0588\udc15\ubb49", -737384388, 0xDA65F71B ^ CryptoVault.mask()));

        final String label;

        private Group(String string2) {
            this.label = string2;
        }

        private static /* synthetic */ String or(String string, int n2, int n3) {
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
}

