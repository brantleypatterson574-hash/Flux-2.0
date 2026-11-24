/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui;

import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.util.CryptoVault;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
final class BindModeControl {
    private BindModeControl() {
    }

    private static BooleanSetting repeat(Module module) {
        BooleanSetting booleanSetting;
        Setting setting = module.getSetting(BindModeControl.ug("\u286d\u15b6\u8c3a\ud1f5\u6f34\u8c8e\u4795\ubb73\u5575\u6806\u89e6\u2ada\ud9d0\u8fce\u0a7e\u7519\u0148", 1469735374, 0xFC281EEA ^ CryptoVault.mask()));
        return setting instanceof BooleanSetting ? (booleanSetting = (BooleanSetting)setting) : null;
    }

    private static boolean pressMode(Module module) {
        return module.isOneShot() || module.isKeyTriggered();
    }

    static boolean available(Module module) {
        if (module == null || module instanceof HitboxConfigScreen752809 || module.getKeyCode() == -1 || module.getKeyCode() == 0) {
            return false;
        }
        return !BindModeControl.pressMode(module) || BindModeControl.repeat(module) != null;
    }

    static boolean hold(Module module) {
        if (BindModeControl.pressMode(module)) {
            BooleanSetting booleanSetting = BindModeControl.repeat(module);
            return booleanSetting != null && booleanSetting.isEnabled();
        }
        return module.isHoldMode();
    }

    static String label(Module module) {
        return BindModeControl.hold(module) ? BindModeControl.ug("\u3a7f\u7667\u4889\uc5e3\u10a0", 1725671437, 0xFC281EEA ^ CryptoVault.mask()) : (BindModeControl.pressMode(module) ? BindModeControl.ug("\u6740\u273a\u4c07\u6170\u2b58\ub06b\ue77e", -1634031749, 0xFC281EEA ^ CryptoVault.mask()) : BindModeControl.ug("\uf773\u558c\u6d38\u39f8\u7511\ub696\ufcb5\u3380\ua403", 1818752619, 0xFC281EEA ^ CryptoVault.mask()));
    }

    static void toggle(Module module) {
        if (BindModeControl.pressMode(module)) {
            BooleanSetting booleanSetting = BindModeControl.repeat(module);
            if (booleanSetting != null) {
                booleanSetting.toggle();
            }
        } else {
            module.setHoldMode(!module.isHoldMode());
        }
    }

    static String tip(Module module) {
        if (BindModeControl.pressMode(module)) {
            return BindModeControl.hold(module) ? BindModeControl.ug("\u34bf\u21e4\uc81c\u25b8\u1489\u4112\u723b\u0901\ub1c0\u5f49\udde7\u0cab\u2258\ufe6a\u2b3d\u4258\uc8f2\u0d69\ud8fe\ub173\u89c1\ud092\ue17a\u2fd5\ubad9\uc478\u049b\uf301\uab52\u8af5\ua221\ud3fb\u5b1c\u8f2c\u3715\u0848\u089e\u4dcd\u109c\u5d79\u6d38\u370d\uacce\uad43\uefbc\u5385\u959f\u8f86\u2058\ud82d\u5759\uf3d8\u9ea9", -1734413215, 0xFC281EEA ^ CryptoVault.mask()) : BindModeControl.ug("\u2d9b\u02fd\u1266\ub539\u7f86\u1a0f\ubc6d\u7a37\u9156\u1c94\u0c72\u36e4\u20b3\uf10b\u24aa\u54e5\ub8ab\uee14\u5d7f\u1a2f\ue395\u9eb2\uea1a\u242e\uad29\u061d\u2ff2\u6383\ud08c\u8823\u6c7d\u0372\ua72b\u4592\ufa3f\u1abb\u63a4\uea5c\ua659\u9faf\u4965\u675b\u8517\u1cbd\ue6d4\u8c1f\u4370\uab51\uce7d\u1753\u325f\u34de\u507b\u646d\u06e5\u70de\u7837\uf76a\u1b8f\ub572\u562c\u63ea\uaa44\ua68c\u721e", -2084204103, 0xFC281EEA ^ CryptoVault.mask());
        }
        return BindModeControl.hold(module) ? BindModeControl.ug("\ue40a\u1594\u0531\u9414\u5e4f\uf33f\uc622\ubd24\u0ba4\u1a1e\u294d\udd1a\uf4df\ud28a\u5e8f\ue48c\uf96d\u8a5b\uabab\ud06c\u1a99\ufa51\uaffc\ubebd\ue92f\u8b75\uc66c\u69c6\uad04\ub217\uc14d\ucb72\uf640\u37f0\ubbca\u70fb\u608c\u7b6d\u5b4f\u9e24\u4eac\ubc85\u7d91\u8f30\udacf\u7373\u4678\ua553\u0d0d\u442d\ua4dd\u29da\u244d\uaf75\u4aea\u3707", 735503938, 0xFC281EEA ^ CryptoVault.mask()) : BindModeControl.ug("\uba44\u0862\u7e48\u3ec4\uf31f\u73a9\ufb4d\u6731\u00f3\udc18\u63c7\u8ff3\ub2df\ude12\u2e41\u4025\ue7c0\u9961\u1e84\ub73c\u98fc\u84a7\u3af8\u632b\u3e26\u50ce\u6005\uc2c0\u615e\u76b0\u33a3\uc055\u855c\u42bb\ud5ce\u47d8\ub048\ua77b\u15c3\u9d42\ub0ac\ub964\uf059\ufe4f\u27a7\u4783\u8463\u984a\ub3f3\u5335\u2a51\ub883\u4d62\u958d\ubf32\ue6c0\ud341\uadcb\u80c7\u4c31\u8efb\ud989\u635e\u0ffc\u425c\u7c18\ua91f\u0b80\u1e25\u4896\u6a99\u69be\u1668\u1086\u779b", -475673405, 0xFC281EEA ^ CryptoVault.mask());
    }

    private static /* synthetic */ String ug(String string, int n2, int n3) {
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

