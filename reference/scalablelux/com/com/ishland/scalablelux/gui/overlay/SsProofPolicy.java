/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.util.CryptoVault;
import java.util.Locale;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class SsProofPolicy {
    public static final long PENDING_TIMEOUT_MS = 8000L;
    public static final int FAIL_OPEN_THRESHOLD = 3;
    public static final int AFFINITY_UNKNOWN = 0;
    public static final int AFFINITY_VERIFIED = 1;
    public static final int AFFINITY_FAILED = 2;

    private SsProofPolicy() {
    }

    public static boolean shouldFailOpen(boolean bl, int n2) {
        return bl && n2 >= 3;
    }

    public static String randomWindowTitle(Random random) {
        String string = SsProofPolicy.wg("\ube4a\ucf7b\uc511\u1e5d\ucfc6\ud284\u7361\u6f73\u8909\uc54f\ue0cd\u24ad\u2f3b\u3b1c\ude52\uad92\uc277\uf17d\u792c\ub15d\u0f2a\u90fe\u48b5\u19cb\u5b39\u52e9\u126d\u3053\ud22f\u9339\u3fbc\uc3b0\u3872\u9033\u9cef\u683c\u1845", 1258648063, 0xEF21F8F5 ^ CryptoVault.mask());
        StringBuilder stringBuilder = new StringBuilder(12);
        for (int i2 = 0; i2 < 12; ++i2) {
            stringBuilder.append(string.charAt(random.nextInt(string.length())));
        }
        return stringBuilder.toString();
    }

    public static String failOpenNoticeText() {
        return SsProofPolicy.wg("\u107d\u1fb6\u7065\u3458\uea45\ue2bf\u50da\u0828\u94ba\u5875\u49c7\uf056\ude65\u796a\u963d\ube66\u6b07\u609d\u5181\u805b\u7873\u6612\uc91e\u3ed2\ua415\u9bfb\u4ae4\u9e78\u7ba2\uaf6f\u7af9\uac19\uf0e3\u8867\udf9f\u383b\u5428\ub76b\uf584\u4b98\u2f05\ub616\u6583\u99f1\u61f4\u3724\uf351\ufbbd\ue9c3\u2748\u1e27\u14f4\u9479\ud00b\u1647\u5d3a\u34cb\uafdc\u63c4\ud3f1\u5e6b\u3a2e\u2956\u1e97\u2401\uefbf\ubae1\u9c21\u3c48\u92e0\u4c60", 604958943, 0xEF21F8F5 ^ CryptoVault.mask());
    }

    public static boolean isSupportedPlatform(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        return string2.startsWith(SsProofPolicy.wg("\u1fcb\ufad9\ue2b1\u33ce\ub330\u2c27\u28f4\ue212\ue65d\udcb4", 59729988, 0xEF21F8F5 ^ CryptoVault.mask())) || string2.contains(SsProofPolicy.wg("\uce29\u8438\uf1d1\u9f2d", -1687069467, 0xEF21F8F5 ^ CryptoVault.mask())) || string2.contains(SsProofPolicy.wg("\u6900\ub300\ub144\uf063\u3064\u7069\ue97b\ub3a0\u79af\uef82", -449091641, 0xEF21F8F5 ^ CryptoVault.mask()));
    }

    public static State resolve(boolean bl, boolean bl2, boolean bl3, int n2) {
        if (!bl) {
            return State.OFF;
        }
        if (!bl2) {
            return State.BLOCKED;
        }
        if (n2 == 2) {
            return State.BLOCKED;
        }
        if (bl3 && n2 == 1) {
            return State.READY;
        }
        return State.PENDING;
    }

    public static Reason reason(State state, boolean bl, int n2, long l2, long l3) {
        if (state == State.BLOCKED) {
            return bl ? Reason.AFFINITY_FAILED : Reason.UNSUPPORTED_PLATFORM;
        }
        if (state == State.PENDING && l2 > 0L && l3 - l2 >= 8000L) {
            return Reason.START_TIMEOUT;
        }
        return Reason.NONE;
    }

    public static boolean routesToSink(State state) {
        return state != State.OFF;
    }

    public static boolean mayPresent(State state) {
        return state == State.READY;
    }

    public static boolean isFluxScreenClass(String string, String string2) {
        if (string == null || string2 == null) {
            return false;
        }
        int n2 = string2.lastIndexOf(46);
        if (n2 <= 0) {
            return false;
        }
        return string.startsWith(string2.substring(0, n2 + 1));
    }

    public static String noticeText(Reason reason) {
        if (reason == Reason.UNSUPPORTED_PLATFORM) {
            return SsProofPolicy.wg("\ud389\uc70a\u723c\u6776\u3ed7\ue3a0\uc9dd\u7a5d\u89bf\u6ebe\ub169\ue92d\u8726\u8ad0\u1e08\u4012\u41e2\u4c60\u5ae1\u30a5\u5c13\u06a8\u0927\u9258\u79c8\uc305\ucbbb\ua6af\ud657\ub038\u12ef\uf63a\u7f6f\u9206\u4da9\u1446\u7238\uef96\u342d\u7b07\ub4d9\u3c5a\u5087\ufcd7\ue101\u5d52\ucbd8\u8eb8\ua320\u8f5c\u71d9\u1904\u583e\ud74d\u6899\u7556\u37df\u2d49\u91e7\uc1df\udbe3\ue124\u1473\ua53a\u6b35\u88b7\ucf97\ubc24\u5c9f\u9c63\ub813\ub518\ub985\u48d1\u96fa\ueaf8\ud2e2\u3a24\udd36\ue966", -1404285599, 0xEF21F8F5 ^ CryptoVault.mask());
        }
        if (reason == Reason.AFFINITY_FAILED) {
            return SsProofPolicy.wg("\u7cbf\u1dad\u5962\u0f41\u3ee4\ude08\uf2a3\u9070\uc442\u801d\u950f\u368c\u01ee\u7e3c\u1510\u3efc\u1657\u9c30\u7656\u1342\u66ee\u3b41\u0bf6\u939f\u6359\u9372\ua617\ufb5e\uc5a2\ud466\u281b\u118e\ua56e\uae7d\u197f\u6cb8\u20c0\u88c4\u7563\u7f2e\ubf9a\u347f\ub3aa\u7f94\u44b7\ube19\u7ca5\u0441\u497d\u61ac\uc9ec\u1603\ud1ce\ua208\u5f0c\u7199\u2c6c\ueb7f\uec67\u8104\uad88\ub291\uaa7e", -1661166313, 0xEF21F8F5 ^ CryptoVault.mask());
        }
        if (reason == Reason.START_TIMEOUT) {
            return SsProofPolicy.wg("\ub258\uf445\uf6d6\u217b\u60e2\ucebd\u540f\u4f7e\ua4d5\u161e\u4321\ucaae\uf31d\ubca4\ub8e8\ua3d5\ubadc\u7c5b\u1a0b\u5ab6\u9072\ue761\u3adb\u694e\u02dc\u6e7d\u3f15\uf8a2\u9c5c\ucc0a\u07ec\udbb1\ue2ad\uafc3\ue524\u2739\u7450\u3a2c\u3c1e\u5a20\uae0d\u7b08\u02db\uf680\u1c7c\u8828\u9b7d\u24cd\ucaad\ub207\u44a0\u1eba\u8851\u69a1\u5ddc\uf4f2", 138223608, 0xEF21F8F5 ^ CryptoVault.mask());
        }
        if (reason == Reason.RENDER_MOD_CONFLICT) {
            return SsProofPolicy.wg("\u7419\u50f2\u47d5\u0392\u0552\u91a0\u421e\u8f42\u46d8\u8d12\u493c\u9e8f\u1b27\uf5c2\u9947\u0674\uf7e9\u38f7\ueb0e\ua688\u482b\ueb8c\ub5d5\u931b\ubfa7\u96f4\u0d81\u084c\u4415\ud412\u027a\u0e79\u1eb2\u37dc\uaf02\u7a65\u540c\ue23f\ucb5d\ue05b\u3021\u42f8\ue38b\u8e2b\u2c45\ua078\u76f9\u0620\ue0ff\u97e2\uf8f3\ud081\u99d5\u3379\uec02\ucca0\u6b4a\u0dc3\u08c2\u2477\ufe43\u38b5\uee42\u8b57\ua090\u1a03\u4516\u1768\u61be\u9230\ue091\uc8f7\u512f\uab57\u14a2\ubc3a\ued2f\uc01f\udfd0\u2bcd\uf493\u4c0d\uefb0\uc66b\u4c3a\u4674\u4c1d\ua73e\u9131\uaffe\u3ce9\ua63a\u0e4e/\u2027\u7fd9\u0dbb\uc005\ufcfd\u5503\u87eb\ub834\uffd7\ud32f\ud38e\udedd\u9757\ued64\u6e02\udfce\u4d0b\u196c\ub026\u079f\ua3d4\u5022\u85ad", -2094311219, 0xEF21F8F5 ^ CryptoVault.mask());
        }
        return "";
    }

    private static /* synthetic */ String wg(String string, int n2, int n3) {
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
    public static enum State {
        OFF,
        READY,
        PENDING,
        BLOCKED;


        private static /* synthetic */ String ej(String string, int n2, int n3) {
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
    public static enum Reason {
        NONE,
        UNSUPPORTED_PLATFORM,
        AFFINITY_FAILED,
        START_TIMEOUT,
        RENDER_MOD_CONFLICT;


        private static /* synthetic */ String zd(String string, int n2, int n3) {
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

