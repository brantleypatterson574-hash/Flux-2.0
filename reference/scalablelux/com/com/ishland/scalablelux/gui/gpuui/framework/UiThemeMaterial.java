/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiMaterial;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public final class UiThemeMaterial {
    private static final class_2960 METAL = class_2960.method_60655((String)UiThemeMaterial.yq("\u02ba\u877d\u9020\ubf00\udd5a\u33fa\u2cb2\u453e\uf3f3\u1966\u6a32\u362b\u2182", 1198118719, 0x3653F67B ^ CryptoVault.mask()), (String)UiThemeMaterial.yq("\u611d\u572a\u7568\u716b\u1995\u6feb\ua0ce\u52f0\u0336\ufe2e\ua21d\u6a70\ub677\u7ea1\ub92a\uc1a9\u051f\u731b\u8ffd\u584e", -1437784407, 0x3653F67B ^ CryptoVault.mask()));
    private static final class_2960 GRANITE_SHADER = class_2960.method_60655((String)UiThemeMaterial.yq("\ue41d\u069a\uf9fc\u2141\ubf8d\u7ad4\u8d44\ub4bc\u5dc7\uc889\uf760\u328d\u57e3\u059d\u09fd", -1174603069, 0x3653F67B ^ CryptoVault.mask()), (String)UiThemeMaterial.yq("\u6206\u42f0\u085d\u42a3\udaf9\ue99f\u04c0\ucfd5\u2c40\u8250\u8ada\u9c8f\uadec\uc63f\u6df2\u1307\u9f10\u3825\u4003", 688209705, 0x3653F67B ^ CryptoVault.mask()));
    private static final class_2960 PORTORO_SHADER = class_2960.method_60655((String)UiThemeMaterial.yq("\u076d\ua55b\ue996\u7c11\u756d\u8372\ua29a\ub4b1\u5e80\u8971\u72e1\u78ab", 1577248323, 0x3653F67B ^ CryptoVault.mask()), (String)UiThemeMaterial.yq("\uec09\uea36\uaa4c\u21aa\uf42c\ue498\u4131\ubab5\uf4c3\u8c20\u2087\u41a8\u1130\u333b\u4904\uef05\u4429\ubc63\u2086\uf957\u18c4", -1113797623, 0x3653F67B ^ CryptoVault.mask()));
    public static final Theme LIQUID_GLASS = new Theme(UiThemeMaterial.yq("\u6fa2\ud71e\ub413\uba5a\u64ef\u8dea\uc25d\u678e\ucf56\u5ebd\u9730\u3c40\u8540\u110b", -1228847364, 0x3653F67B ^ CryptoVault.mask()), false, -13703480, 724239, 329223, 0xFFFFFF, -1051917, -4932930, -6051410, -14342100, 0x38FFFFFF, -1377546, -14698336, new float[]{0.058f, 0.064f, 0.068f}, new float[]{0.01f, 0.012f, 0.016f}, new float[]{0.07f, 0.11f, 0.115f});
    public static final Theme FLUX = new Theme(UiThemeMaterial.yq("\u24c0\uc60c\u41d2\ue078\u992d", 1587284955, 0x3653F67B ^ CryptoVault.mask()), false, -13312769, 723983, 263431, 0xFFFFFF, -1117708, -6642771, -7629924, -14934236, 1714740479, -4198145, -11899168, new float[]{0.034f, 0.037f, 0.044f}, new float[]{0.01f, 0.011f, 0.014f}, new float[]{0.045f, 0.075f, 0.11f});
    public static final Theme GRANITE = new Theme(UiThemeMaterial.yq("\ua8ea\uf82e\u3ae1\u9a4c\uf0bf\uf0df\u7d22\u1a66\ufd09", -2041870242, 0x3653F67B ^ CryptoVault.mask()), true, -2509458, 920586, 328965, 16115400, -593175, -5396581, -6448749, -13882842, 1725543790, -597068, -5864898, new float[3], new float[3], new float[3]);
    public static final Theme NERO_PORTORO = new Theme(UiThemeMaterial.yq("\u6f5b\u0ea1\u9c89\u72f7\u5d7d\u2521\u050e\u2a05\ub8c6\u426e\u2cd4\u652a\u5bbe\u2f94\ue046", 2054770851, 0x3653F67B ^ CryptoVault.mask()), true, -2377614, 657932, 197380, 15260864, -921623, -5790818, -6777198, -14014169, 1490794610, -792896, -5864898, new float[3], new float[3], new float[3]);
    public static final Theme TEAL = new Theme(UiThemeMaterial.yq("\u5a41\u2509\u06a7\u0a45\uacee\uffea\u2f61\u11b3", -1826289201, 0x3653F67B ^ CryptoVault.mask()), false, -13773633, 398358, 132872, 12580846, -1377545, -7096141, -7691356, -14931154, 1714279615, -7342618, -15427928, new float[]{0.018f, 0.082f, 0.088f}, new float[]{0.004f, 0.02f, 0.034f}, new float[]{0.05f, 0.15f, 0.165f});
    public static final Theme RUBY = new Theme(UiThemeMaterial.yq("\u9daf\u707e\uc0af\uefb2\ubbea\ua2f9\u94cf", -251768237, 0x3653F67B ^ CryptoVault.mask()), false, -1030037, 1574411, 524804, 16763090, -135953, -4415835, -5927019, -13689566, 1727023211, -25168, -5761986, new float[]{0.105f, 0.016f, 0.034f}, new float[]{0.03f, 0.004f, 0.016f}, new float[]{0.2f, 0.055f, 0.095f});
    public static final Theme CATPPUCCIN = new Theme(UiThemeMaterial.yq("\ua9a0\u7cd7\u5b47\u406d\u4cec\ud752\ue113\u64f8\ud223\u1dec\u6d16\uedb6\u8dcc\uf799", 1046347324, 0x3653F67B ^ CryptoVault.mask()), false, -3430665, 0x1E1E2E, 0x11111B, 11845374, -3287308, -5853752, -7104078, -13553084, 1724622583, -671001, -5210144, new float[]{0.118f, 0.118f, 0.18f}, new float[]{0.05f, 0.05f, 0.086f}, new float[]{0.13f, 0.11f, 0.19f});
    public static final Theme PURPLE = new Theme(UiThemeMaterial.yq("\u501b\uaa91\u4b08\u331e\ub7b8\ucf3e\u1f53\u0a85\ud0e0", 1713479292, 0x3653F67B ^ CryptoVault.mask()), false, -5738753, 919324, 328202, 14469375, -791297, -5398581, -6450251, -14410952, 1722314495, -2575105, -9816632, new float[]{0.062f, 0.028f, 0.115f}, new float[]{0.016f, 0.006f, 0.044f}, new float[]{0.15f, 0.08f, 0.25f});
    public static final Theme EMERALD = new Theme(UiThemeMaterial.yq("\uafb8\ufe87\uf94c\u9a12\u236c\uec69\ucf23\u491e\u15cf", 412601182, 0x3653F67B ^ CryptoVault.mask()), false, -13315203, 398349, 133125, 12907992, -1312015, -7160668, -7690857, -14996446, 1714738045, -6556222, -15492502, new float[]{0.018f, 0.082f, 0.05f}, new float[]{0.004f, 0.02f, 0.02f}, new float[]{0.055f, 0.155f, 0.11f});
    public static final Theme OCEAN = new Theme(UiThemeMaterial.yq("\u18df\u91b8\u7082\u476d\u28de\uaec1\u901a", 663038071, 0x3653F67B ^ CryptoVault.mask()), false, -12877066, 462104, 132106, 13162743, -1379843, -6903096, -7561800, -14997962, 1715176182, -6438657, -14721066, new float[]{0.018f, 0.046f, 0.105f}, new float[]{0.004f, 0.01f, 0.034f}, new float[]{0.055f, 0.105f, 0.195f});
    public static final Theme SUNSET = new Theme(UiThemeMaterial.yq("\ua9f5\u2c25\ud6e1\u48f1\u82a5\u100c\u41e2", 1985417080, 0x3653F67B ^ CryptoVault.mask()), false, -29630, 1576970, 525314, 16769220, -134936, -3625832, -4874610, -13687781, 1728023618, -16246, -2731985, new float[]{0.112f, 0.05f, 0.022f}, new float[]{0.034f, 0.01f, 0.014f}, new float[]{0.21f, 0.105f, 0.07f});
    public static final Theme ROSE = new Theme(UiThemeMaterial.yq("\uda36\ud8da\ud8dd\u7d71\u9d7d", 85634121, 0x3653F67B ^ CryptoVault.mask()), false, -41054, 1574671, 524805, 16763611, -135949, -3694929, -4878432, -13689562, 1728012194, -22840, -3661976, new float[]{0.108f, 0.024f, 0.062f}, new float[]{0.03f, 0.006f, 0.022f}, new float[]{0.205f, 0.065f, 0.13f});
    public static final Theme MIDNIGHT = new Theme(UiThemeMaterial.yq("\ua69e\u1e5a\udb04\uf090\u009d\u3ee3\u9ffc\uc41b\ub584\u98f7\uff43", -78016533, 0x3653F67B ^ CryptoVault.mask()), false, -10785537, 526607, 131590, 13159671, -1446915, -6380856, -7104840, -14934474, 1717267711, -5327105, -13680682, new float[]{0.028f, 0.032f, 0.104f}, new float[]{0.008f, 0.008f, 0.034f}, new float[]{0.088f, 0.092f, 0.21f});
    public static final Theme GOLD = new Theme(UiThemeMaterial.yq("\ua4c8\u9c7c\uc7e4\ub2ee\u5876\ub51f\u9657\u604f", -1209076573, 0x3653F67B ^ CryptoVault.mask()), false, -1854655, 1315082, 460034, 15786688, -330522, -4148586, -5201013, -13883877, 1726198593, -795488, -4683489, new float[]{0.092f, 0.072f, 0.02f}, new float[]{0.026f, 0.017f, 0.006f}, new float[]{0.185f, 0.145f, 0.058f});
    public static final Theme SLATE = new Theme(UiThemeMaterial.yq("\ub8be\u1785\uf8a6\u9d7f\u03ac\u1631\ud0d7\ued96", 434719189, 0x3653F67B ^ CryptoVault.mask()), false, -7692108, 724240, 263430, 13687008, -1117708, -6379599, -7103323, -14538703, 1284153524, -4010536, -10851462, new float[]{0.045f, 0.054f, 0.066f}, new float[]{0.01f, 0.013f, 0.018f}, new float[]{0.095f, 0.112f, 0.135f});
    public static final Theme COPPER = new Theme(UiThemeMaterial.yq("\ue9f0\u6d2a\ud9cb\ufd5a\u48d6\ua063\u537e\ue51f", 1818188699, 0x3653F67B ^ CryptoVault.mask()), false, -3703990, 1248520, 393986, 16768192, -331544, -3297384, -4415350, -13884903, 1724349258, -1525878, -6530514, new float[]{0.088f, 0.055f, 0.035f}, new float[]{0.024f, 0.012f, 0.01f}, new float[]{0.175f, 0.11f, 0.068f});
    public static final Theme AMBER = new Theme(UiThemeMaterial.yq("\ud6e0\u8b8d\uc834\u7b1e\u7891\u4657\ub4fe\ued99", 357061423, 0x3653F67B ^ CryptoVault.mask()), false, -19712, 0x161006, 459778, 16771256, -200224, -3427448, -4545410, -13687792, 1728033536, -11664, -4687872, new float[]{0.112f, 0.072f, 0.01f}, new float[]{0.03f, 0.016f, 0.003f}, new float[]{0.225f, 0.15f, 0.02f});
    public static final Theme MONO = new Theme(UiThemeMaterial.yq("\u3b74\uc7a1\uf033\u8a47\u3c47", -2000570865, 0x3653F67B ^ CryptoVault.mask()), false, -1776409, 789517, 263172, 0xF0F0F2, -657930, -5592402, -6710882, -14145493, 1290069223, -657929, -6250330, new float[]{0.058f, 0.058f, 0.062f}, new float[]{0.013f, 0.013f, 0.014f}, new float[]{0.12f, 0.12f, 0.128f});
    public static final Theme INDIGO = new Theme(UiThemeMaterial.yq("\u858a\u0d95\u4c09\uea35\u8bcf\uf70a\uc2bf\u6c60\u79fe\u4e1e", 942865934, 0x3653F67B ^ CryptoVault.mask()), false, -11576363, 658198, 197384, 13159157, -1315844, -6380853, -7170628, -14802888, 1716476885, -5327617, -13682776, new float[]{0.028f, 0.032f, 0.098f}, new float[]{0.008f, 0.007f, 0.03f}, new float[]{0.082f, 0.09f, 0.2f});
    public static final Theme LIME = new Theme(UiThemeMaterial.yq("\u0835\u3ebd\u3e74\u13f5\u938a\u977c", -388243976, 0x3653F67B ^ CryptoVault.mask()), false, -6035915, 922632, 263938, 14546360, -721946, -4271720, -5127030, -14274534, 1722017333, -3606641, -9460974, new float[]{0.062f, 0.085f, 0.022f}, new float[]{0.014f, 0.022f, 0.008f}, new float[]{0.13f, 0.18f, 0.048f});
    public static final Theme CORAL = new Theme(UiThemeMaterial.yq("\u492d\u87db\u1d32\ua699\u1911\u765c\u2e76\u0557", 837249877, 0x3653F67B ^ CryptoVault.mask()), false, -38053, 1575432, 525059, 16763328, -135703, -3431781, -4548719, -13689831, 1728015195, -22888, -2735570, new float[]{0.11f, 0.04f, 0.03f}, new float[]{0.03f, 0.009f, 0.012f}, new float[]{0.205f, 0.092f, 0.068f});
    public static final Theme LAVENDER = new Theme(UiThemeMaterial.yq("\uc257\u9cb0\u43e7\uaac7\u13b0\u8c6f\u6538\ufd96\u246c\u8607", 1193355896, 0x3653F67B ^ CryptoVault.mask()), false, -3890177, 1183260, 328714, 14996735, -593665, -4609072, -5464380, -14015944, 1724163071, -2307841, -7708714, new float[]{0.065f, 0.05f, 0.112f}, new float[]{0.016f, 0.011f, 0.032f}, new float[]{0.145f, 0.112f, 0.235f});
    public static final Theme CRIMSON = new Theme(UiThemeMaterial.yq("\u30c7\u9028\uf551\u8213\u5d61\u39b3\ubfe4\u8a01", 31992514, 0x3653F67B ^ CryptoVault.mask()), false, -2349530, 1443591, 459266, 0xFFC4C4, -135956, -3433573, -4551283, -13690856, 1725703718, -30070, -6941676, new float[]{0.11f, 0.018f, 0.018f}, new float[]{0.028f, 0.004f, 0.006f}, new float[]{0.205f, 0.04f, 0.045f});
    public static final Theme GRAPHITE = new Theme(UiThemeMaterial.yq("\u2d72\u0461\u0285\u4e52\ud252\u4b7f\u0f2f\u3706\u65a0\u2aa5\u3e5a\u3a02", 230915467, 0x3653F67B ^ CryptoVault.mask()), false, -9524993, 0x101114, 394759, 13950704, -1249550, -5986128, -7104609, -14276563, 1483647231, -4401921, -11896632, new float[]{0.062f, 0.064f, 0.07f}, new float[]{0.014f, 0.014f, 0.017f}, new float[]{0.1f, 0.115f, 0.15f});
    public static final Theme AURORA_NIGHT = new Theme(UiThemeMaterial.yq("\u4980\u18c7\ucd39\u2a1a\u1181\ucd6c\u971b\ubabb\u06bc\u1333\uf9ee\ue749\ud5bf\u0fc0", 1589773515, 0x3653F67B ^ CryptoVault.mask()), false, -10816848, 396564, 132362, 12908772, -1443597, -7096140, -7888986, -15194576, 1717236400, -5702954, -8758568, new float[]{0.02f, 0.07f, 0.085f}, new float[]{0.018f, 0.01f, 0.05f}, new float[]{0.08f, 0.17f, 0.15f});
    public static final Theme SAKURA = new Theme(UiThemeMaterial.yq("\ub2ec\u192a\uc9cc\u5721\ube61\ub443\u2660", 30026252, 0x3653F67B ^ CryptoVault.mask()), false, -22328, 1445138, 459783, 16769260, -134923, -3559749, -4809303, -13886171, 1728030920, -12062, -2594662, new float[]{0.1f, 0.056f, 0.076f}, new float[]{0.028f, 0.014f, 0.024f}, new float[]{0.23f, 0.13f, 0.17f});
    public static final Theme ABYSS = new Theme(UiThemeMaterial.yq("\ue933\u1712\uec0e\ucf57\ucd35\u0181", -741864978, 0x3653F67B ^ CryptoVault.mask()), false, -5908225, 264206, 66052, 14216447, -1510661, -7297350, -8153432, -15655896, 1487263999, -3085569, -11893056, new float[]{0.012f, 0.03f, 0.062f}, new float[]{0.002f, 0.006f, 0.016f}, new float[]{0.06f, 0.1f, 0.16f});
    private static final List<Theme> THEMES = List.of(FLUX, LIQUID_GLASS, GRANITE, NERO_PORTORO, TEAL, RUBY, CATPPUCCIN, PURPLE, EMERALD, OCEAN, SUNSET, ROSE, MIDNIGHT, GOLD, SLATE, COPPER, AMBER, MONO, INDIGO, LIME, CORAL, LAVENDER, CRIMSON, GRAPHITE, AURORA_NIGHT, SAKURA, ABYSS);

    private UiThemeMaterial() {
    }

    public static List<Theme> themes() {
        return THEMES;
    }

    public static String[] names() {
        return (String[])THEMES.stream().map(Theme::name).toArray(String[]::new);
    }

    public static Theme theme(String string) {
        for (Theme theme : THEMES) {
            if (!theme.name().equalsIgnoreCase(string)) continue;
            return theme;
        }
        return FLUX;
    }

    public static boolean isStone(String string) {
        return UiThemeMaterial.theme(string).stone();
    }

    public static UiMaterial forTheme(String string) {
        Theme theme = UiThemeMaterial.theme(string);
        if (theme.stone()) {
            class_2960 class_29602 = string != null && string.equalsIgnoreCase(NERO_PORTORO.name()) ? PORTORO_SHADER : GRANITE_SHADER;
            return new UiMaterial(class_29602, null, -1, 1.0f, 0.6f, 0.55f, 0.35f, -2.0f, -2.0f, 0.0f, 0.0f);
        }
        float[] fArray = theme.metalHigh();
        float[] fArray2 = theme.metalLow();
        float[] fArray3 = theme.metalSheen();
        boolean bl = theme == FLUX;
        return new UiMaterial(METAL, null, -1, 1.0f, bl ? 0.22f : 0.7f, bl ? 0.3f : 0.8f, 0.4f, -2.0f, -2.0f, 0.0f, 0.0f, fArray[0], fArray[1], fArray[2], 0.0f, fArray2[0], fArray2[1], fArray2[2], 0.0f, fArray3[0], fArray3[1], fArray3[2], 0.0f);
    }

    public static UiMaterial preview(String string) {
        return UiThemeMaterial.forTheme(string);
    }

    private static float clamp(float f2, float f3, float f4) {
        return Float.isFinite(f2) ? Math.max(f3, Math.min(f2, f4)) : f3;
    }

    private static /* synthetic */ String yq(String string, int n2, int n3) {
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
    public record Theme(String name, boolean stone, int accent, int panelBase, int deep, int tint, int text, int muted, int muted2, int toggleOff, int outer, int logoLight, int logoDark, float[] metalHigh, float[] metalLow, float[] metalSheen) {
    }
}

