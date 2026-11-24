/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui;

import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.ColorSetting;
import com.ishland.scalablelux.module.ConfigKeybindListSetting;
import com.ishland.scalablelux.module.ModeSetting;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.MultiSelectSetting;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.module.SliderSetting;
import com.ishland.scalablelux.module.StringSetting;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import me.sootysplash.box.render.HitboxKeyBinding655023;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ClassicSettingLayout {
    private ClassicSettingLayout() {
    }

    public static List<Row> rows(Module module) {
        if (module == null) {
            return List.of();
        }
        ArrayList<Row> arrayList = new ArrayList<Row>();
        for (Setting setting : module.getSettings()) {
            Row row = ClassicSettingLayout.row(module, setting);
            if (row == null) continue;
            arrayList.add(row);
        }
        return List.copyOf(arrayList);
    }

    public static List<Row> visibleRows(Module module) {
        return ClassicSettingLayout.rows(module).stream().filter(Row::visible).toList();
    }

    public static Row row(Module module, Setting setting) {
        Kind kind;
        if (setting == null) {
            return null;
        }
        List<String> list = List.of();
        String string = "";
        if (setting instanceof BooleanSetting) {
            BooleanSetting booleanSetting = (BooleanSetting)setting;
            kind = Kind.BOOLEAN;
            string = booleanSetting.isEnabled() ? "On" : ClassicSettingLayout.eb("\ude0c\uffb9\u1fd0\u13ff", -1369397992, 0x706E7265 ^ CryptoVault.mask());
        } else if (setting instanceof SliderSetting) {
            SliderSetting sliderSetting = (SliderSetting)setting;
            kind = Kind.SLIDER;
            string = sliderSetting.W();
        } else if (setting instanceof ModeSetting) {
            ModeSetting modeSetting = (ModeSetting)setting;
            kind = Kind.MODE;
            list = List.copyOf(modeSetting.getModes());
            string = modeSetting.getCurrentMode();
        } else if (setting instanceof MultiSelectSetting) {
            MultiSelectSetting multiSelectSetting = (MultiSelectSetting)setting;
            kind = Kind.MULTI;
            list = List.copyOf(multiSelectSetting.E());
            string = multiSelectSetting.V();
        } else if (setting instanceof StringSetting) {
            StringSetting stringSetting = (StringSetting)setting;
            kind = Kind.STRING;
            string = stringSetting.X();
        } else if (setting instanceof ColorSetting) {
            ColorSetting colorSetting = (ColorSetting)setting;
            kind = Kind.COLOR;
            string = String.format(Locale.ROOT, ClassicSettingLayout.eb("\u5083\u6172\ua650\ud1ff\u3f02\ude3c\u2173", 1984299026, 0x706E7265 ^ CryptoVault.mask()), colorSetting.A().getRGB() & 0xFFFFFF);
        } else if (setting instanceof HitboxKeyBinding655023) {
            HitboxKeyBinding655023 hitboxKeyBinding655023 = (HitboxKeyBinding655023)setting;
            kind = Kind.KEYBIND;
            string = hitboxKeyBinding655023.a() == null ? ClassicSettingLayout.eb("\u8e71\u936c\ueaa9\u541d\uab1f\u5714\ud292", -1624040907, 0x706E7265 ^ CryptoVault.mask()) : hitboxKeyBinding655023.a().getKeyDisplayName();
        } else if (setting instanceof ConfigKeybindListSetting) {
            kind = Kind.ACTION;
            string = ClassicSettingLayout.eb("\uc094\u8cbd\uee6d\ufb24\u0979\ufd44\u5e98\ufd17\u6b9f\u023d\u4bef\u5871\u3512\ua06a\ubae3\u5ee1\u179c\u4c42\uc512\uc288\ufba9\u8f20\u34ce\u0a6c\ud80f", 1319332079, 0x706E7265 ^ CryptoVault.mask());
        } else {
            return null;
        }
        boolean bl = setting.isVisible() && ClassicSettingLayout.isVisibleForDependency(module, setting, new HashSet<Setting>());
        return new Row(setting, kind, setting.getName(), setting.getDescription(), setting.getSection(), setting.getDependsOn(), bl, list, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    private static boolean isVisibleForDependency(Module var0, Setting var1_1, Set<Setting> var2_2) {
        if (var1_1 == null || !var2_2.add(var1_1)) {
            return false;
        }
        try {
            var3_3 = var1_1.getDependsOn();
            if (var3_3 == null || var3_3.isBlank()) {
                var4_4 = true;
                return var4_4;
            }
            for (String var7_9 : var3_3.split(",", -1)) {
                var8_10 = var7_9.trim();
                if (var8_10.isEmpty()) {
                    var9_11 = false;
                    return var9_11;
                }
                v0 = var9_11 = var8_10.charAt(0) == '!';
                if (var9_11) {
                    var8_10 = var8_10.substring(1).trim();
                }
                if ((var11_13 = ((var10_12 = var8_10.indexOf(58)) < 0 ? var8_10 : var8_10.substring(0, var10_12)).trim()).isEmpty()) {
                    var12_15 = false;
                    return var12_15;
                }
                var12_14 = var0.getSetting(var11_13);
                if (var12_14 == null || !var12_14.isVisible() || !ClassicSettingLayout.isVisibleForDependency(var0, var12_14, var2_2)) {
                    var13_16 = false;
                    return var13_16;
                }
                if (var10_12 < 0) ** GOTO lbl45
                var16_20 = var8_10.substring(var10_12 + 1).trim();
                if (var16_20.isEmpty()) {
                    var17_23 = false;
                    return var17_23;
                }
                var13_16 = false;
                if (var12_14 instanceof MultiSelectSetting) {
                    var18_24 = (MultiSelectSetting)var12_14;
                    for (String var22_29 : var16_20.split("\\|", -1)) {
                        if (!var18_24.D(var22_29.trim())) continue;
                        var13_16 = true;
                        break;
                    }
                } else if (var12_14 instanceof ModeSetting) {
                    var17_22 = (ModeSetting)var12_14;
                    for (String var22_29 : var16_20.split("\\|", -1)) {
                        if (!var17_22.getCurrentMode().equalsIgnoreCase(var22_29.trim())) continue;
                        var13_16 = true;
                        break;
                    }
                } else {
                    var19_26 = false;
                    return var19_26;
lbl45:
                    // 1 sources

                    if (var12_14 instanceof BooleanSetting) {
                        var15_18 = (BooleanSetting)var12_14;
                        var13_16 = var15_18.isEnabled();
                    } else if (var12_14 instanceof SliderSetting) {
                        var14_17 = (SliderSetting)var12_14;
                        var13_16 = var14_17.B() > var14_17.D();
                    } else {
                        var16_21 = false;
                        return var16_21;
                    }
                }
                if (var9_11 != var13_16) continue;
                var15_19 = false;
                return var15_19;
            }
            var4_6 = true;
            return var4_6;
        }
        finally {
            var2_2.remove(var1_1);
        }
    }

    private static /* synthetic */ String eb(String string, int n2, int n3) {
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
    public record Row(Setting setting, Kind kind, String name, String description, String section, String dependsOn, boolean visible, List<String> choices, String valueLabel) {
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Kind {
        BOOLEAN,
        SLIDER,
        MODE,
        MULTI,
        STRING,
        COLOR,
        KEYBIND,
        ACTION;


        private static /* synthetic */ String nv(String string, int n2, int n3) {
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

