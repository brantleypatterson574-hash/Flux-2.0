/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 */
package com.ishland.scalablelux.gui.overlay;

import com.ishland.scalablelux.gui.gpuui.framework.ClassicIcons;
import com.ishland.scalablelux.gui.overlay.OverlayWindow;
import com.ishland.scalablelux.util.CryptoVault;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

@Environment(value=EnvType.CLIENT)
public final class OverlayCursor {
    public static final float BASE_SIZE = 21.0f;
    public static final float HOTSPOT = 0.046875f;
    private static final float PAD = 0.1f;
    private static final long WANT_WINDOW_NANOS = 250000000L;
    private static volatile long wantedAtNanos;
    private static volatile boolean ready;
    private static boolean failed;
    private static int texture;
    private static int program;
    private static int vao;
    private static int vbo;
    private static int uTex;
    private static int uScale;
    private static int uOutline;

    private OverlayCursor() {
    }

    public static float[] layout(double d2, double d3, int n2, int n3, int n4, int n5, float f2, float f3) {
        if (n2 <= 0 || n3 <= 0 || n4 <= 0 || n5 <= 0) {
            return null;
        }
        if (!Double.isFinite(d2) || !Double.isFinite(d3)) {
            return null;
        }
        float f4 = f2 > 0.0f ? f2 : 1.0f;
        float f5 = f3 > 0.5f && f3 < 8.0f ? f3 : 1.0f;
        float f6 = 21.0f * f4 * f5;
        float f7 = (float)(d2 * (double)n4 / (double)n2);
        float f8 = (float)(d3 * (double)n5 / (double)n3);
        float f9 = f6 * 0.046875f;
        return new float[]{f7 - f9, f8 - f9, f6};
    }

    public static boolean takeOver() {
        wantedAtNanos = System.nanoTime();
        return ready && OverlayWindow.isShown();
    }

    static void draw(long l2, int n2, int n3) {
        if (failed || l2 == 0L) {
            return;
        }
        if (System.nanoTime() - wantedAtNanos > 250000000L) {
            return;
        }
        if (!ready && !OverlayCursor.init()) {
            return;
        }
        double[] dArray = new double[1];
        double[] dArray2 = new double[1];
        int[] nArray = new int[1];
        int[] nArray2 = new int[1];
        float[] fArray = new float[1];
        float[] fArray2 = new float[1];
        GLFW.glfwGetCursorPos((long)l2, (double[])dArray, (double[])dArray2);
        GLFW.glfwGetWindowSize((long)l2, (int[])nArray, (int[])nArray2);
        GLFW.glfwGetWindowContentScale((long)l2, (float[])fArray, (float[])fArray2);
        float[] fArray3 = OverlayCursor.layout(dArray[0], dArray2[0], nArray[0], nArray2[0], n2, n3, ClassicIcons.osCursorScaleNow(), fArray[0]);
        if (fArray3 == null) {
            return;
        }
        float f2 = fArray3[2];
        float f3 = f2 * 0.1f;
        int n4 = Math.round(fArray3[0] - f3);
        int n5 = n3 - Math.round(fArray3[1] + f2 + f3);
        int n6 = Math.round(f2 + f3 * 2.0f);
        GL11.glViewport((int)n4, (int)n5, (int)n6, (int)n6);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)1, (int)771);
        GL20.glUseProgram((int)program);
        GL13.glActiveTexture((int)33984);
        GL11.glBindTexture((int)3553, (int)texture);
        GL20.glUniform1i((int)uTex, (int)0);
        GL20.glUniform1f((int)uScale, (float)1.2f);
        GL20.glUniform1f((int)uOutline, (float)Math.max(0.6f / f2, 0.07f));
        GL30.glBindVertexArray((int)vao);
        GL11.glDrawArrays((int)5, (int)0, (int)4);
        GL30.glBindVertexArray((int)0);
        GL11.glBindTexture((int)3553, (int)0);
        GL20.glUseProgram((int)0);
        GL11.glDisable((int)3042);
        GL11.glViewport((int)0, (int)0, (int)n2, (int)n3);
    }

    private static boolean init() {
        try {
            int n2;
            int n3;
            BufferedImage bufferedImage;
            try (InputStream inputStream = class_310.method_1551().method_1478().open(ClassicIcons.texture(OverlayCursor.cn("\uc6a2\uf283\u1d34\u0675\u7173\ue42c\u152e\u4443", 2052068556, 0xCA0920B2 ^ CryptoVault.mask())));){
                bufferedImage = ImageIO.read(inputStream);
            }
            if (bufferedImage == null) {
                failed = true;
                return false;
            }
            int n4 = bufferedImage.getWidth();
            int n5 = bufferedImage.getHeight();
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer((int)(n4 * n5 * 4));
            for (int i2 = 0; i2 < n5; ++i2) {
                for (int i3 = 0; i3 < n4; ++i3) {
                    n3 = bufferedImage.getRGB(i3, i2);
                    n2 = n3 >>> 24;
                    byteBuffer.put((byte)(((n3 >> 16 & 0xFF) * n2 + 127) / 255));
                    byteBuffer.put((byte)(((n3 >> 8 & 0xFF) * n2 + 127) / 255));
                    byteBuffer.put((byte)(((n3 & 0xFF) * n2 + 127) / 255));
                    byteBuffer.put((byte)n2);
                }
            }
            byteBuffer.flip();
            texture = GL11.glGenTextures();
            GL11.glBindTexture((int)3553, (int)texture);
            GL11.glPixelStorei((int)3317, (int)1);
            GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)n4, (int)n5, (int)0, (int)6408, (int)5121, (ByteBuffer)byteBuffer);
            GL30.glGenerateMipmap((int)3553);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9987);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10242, (int)33069);
            GL11.glTexParameteri((int)3553, (int)10243, (int)33069);
            GL11.glBindTexture((int)3553, (int)0);
            String string = OverlayCursor.cn("\u842e\u4b27\ufdfa\uf292\ueb47\ua666\u4d30\u1c4d\ub9df\u3861\u56c3\u599e\u3eaf\u7388\u78c1\uc741\ubaf8\u5d6b\u84bf\ucbf1\uf673\ub713\ud86a\ubb1d\u666d\u5b5b\u12f8\uce2b\u0c49\uc528\u7b5d\u72f4\u5f6f\u0b63\u67d1\u8007\ua73f\u6d0d\u5e22\u7532\uea6b\u2f91\u6681\ub1be\u1963\u1ca0\u647a\u9cde\u23f0\u8d2d\ud876\u7c18\u2536\u67d0\u8768\u68da\ufbb8\u99b2\u0ae2\u4fe8\u9179\u3f67\ub4a6\ucc16\u024b\uaf34\ud889\uc31b\ud6e3\u887b\u5b10\u1170\u16ec\u12ea\ub2ca\u9cb1\u9f45\u3286\u8b7a\u4ede\ubb3d\u9fca\u5cf0\u66a6\u2016\u4171\u1d2e\udce4\ud826\u42c4\ud86f\u87c8\ue2d8\ud400\uf240\u630b\ud36d\u028b\ua397\u5e5f\u9e69\ua332\ubb69\u66b3\u328c\ueb86\ud52b\ud9b8\ua4c0\u8a6a\ud3ed\ubdfc\uf561\u02db\uae31\u57c4\uaa97\uc04a\u9659\u2070\u17b1\uc7c8\u4193\ue78e\u7525", 80750377, 0xCA0920B2 ^ CryptoVault.mask());
            String string2 = OverlayCursor.cn("\ua974\u662f\u4cf8\u6e01\ub888\ue974\u113b\u6ee5\u7b59\u59f8\u3d23\ud398\uf1b2\u23e7\u74b4\ucce2\udd8a\ubae5\u27d1\uf79c\ufd29\u49b8\uaea9\u87f3\ub588\u88a5\u1d58\u077b\ue183\uc3ea\uc9ca\u147a\u9392\udc1b\ue99f\u55f5\u7dc7\ud48f\u3503\u2073\uc970\ud7f2\udada\u5515\u7f54\uf5b0\u324f\ue5a6\u97e3\u8b69\u560d\ue9f3\u0b26\uf94a\u7007\u1dae\u461e\u4537\uf69cb\ubbce\u6dca\u8b50\uf82f\u5ae4\ub575\u5445\u5f6b\u4dde\u6f2f\ude97\ucd973\u731f\ub997\u4bb6\ud678\ud55e\u4a75\ue2c4\u618f\ua280\ub865\uc3f4\u4310\u6351\u3afc\ud458\u0b5b\u3162\ubb34\u0457\u3096\ud37f\u9244\u8420\u2fd9\ueb01\ua161\uc659\u53d8\uf453\u72ba\u5494\u6265\uee3a\u8de7\u1c64\uff30\u330a\u512c\u3749\u522c\u65e1\ub062\u0b2d\u5e28\ubdce\u930c\udaef\u5945\udf33\u5861\u86e9\ube75\u4f75\udf81\ue1d2\uf837\u45f1\ub914\u20e6\u1258\uafe2\ue479\u7b5f\u7a14\u336c\u2095\ubfb9\u8aa0\ubb6d\ufbbe\u177b\u8d8a\u2348\ufcd5\uc0fc\ud884\u348e\u1d69\uca1d\uc3a1\u3438\ua373\u6bec\u0a1c\u62a4\ue929\u62e7\ubdc2\uee2f\u64ed\u06de\u34b3\ubba8\u7977\ud812\u3bef\uac09\uef55\u134d\ue414\ud181\u17a6\ub7c8\ucc0b\u774f\u9793\uddfb\ud73a\u67a7\u477f\u3987\u8998\u480a\ubebf\u46b5\u85a4\ud4c0\u9040\u7c27\u81eb\ud69b\ud418\u6f6f\ub606\u8b99\u3570\u0848\uf3d0\u1c2e\u00da\u3732\u71d3\udd9a\u44cc\uedb6\ub6de\u68c9\u8081\u6ee8\u01e2\u5183\u8c01\u63c1\u9629\u3e90\uf98b\ue3b2\u0235\u1948\u26a7\u51c9\ud1c3\ua2b1\u3c7b\uff36\u5393\ufa20\uc332\u1f9a\u94ac\ufcc5\u8cce\uce42\uae36\ucca3\ue36a\u3519\uf81c\u73b1\u7c69\u092e\u7692\u0461\ubf43\u26fe\udfac\u7e1f\ub767\u6c19\u1cf4\uf883\u370d\u8489\ud2ef\u4ffe\uf827\u7af1\u179c\u2a6c\u715b\u9ccc\uffd0\u76bf\u41db\u0ef6\u2406\u7e13\ua470\u6a79\u14f1\u9104\udcd9\ud175\ue907\u3607\uafa8\u41aa\u57ef\u39c5\u7e02\u8c56\u1e0a\u6dde\ua563\u4e0a\ua305\u05ed\u38ea\u0e7b\ucc57\u23f4\u29f5\uddae\u3734\u1057\u7731\u7a54\uea3f\u18bc\u84cb\ue25f\u175f\u46b6\u4174\u50d1\u70df\u2795\u0bb3\u17e4\u20fb\ucafb\ud5fc\uf86b\uf578\u6a46\u26fa\ub27c\u5268\u4f18\ue3e9\u9b5d\u60be\uc78a\ua647\u68ae\ub922\udbe8\u4167\u695e\uc09a\u72fd\u9ab6\u7454\ua782\ufac6\u029d\u01d2\u12c8\u9fc5\u0279\uf3f5\uaef2\u5e74\u7652\u72cc\u5afc\u8ddc\u3730\u9ff8\ua0c8\uf223\ufdcb\uaa2d\u93bf\u5557\u316d\u14b1\ucda3\u5b27\u77bd\u1a56\u7a7c\u56b5\u48d1\u70d7\u773a\u3f08\ue79f\ue49d\u2945\u7e10\ue8e5\udc77\u4cff\u8b52\u257f\u724a\u7a57\u87a3\u805a\u4439\u8903\u2ee6\uaf08\u11de\ua398\u860a\u35ba\uc77c\ue33b\ubc13\uafb1\u2b1b\u13fa\u2201\u94ab\u7e56\ub6ae\u8654\u31db\u1847\ub28d\u8e5e\u94fb\uc7ee\u36fc\uc90b\u0a71\u46e6\u7e98\ub03b\u1c91\u267a\u0e81\uaeee\uc18e\ue6ba\u07ac\u5d75\ucf72\u77e6\u072e\u1e2a\ud4f7\u5049\u8f57\u4fa6\ufdbb\u7b22\u2c8e\u511d\u17f7\u8b35\u60ca\u2297", 1570949527, 0xCA0920B2 ^ CryptoVault.mask());
            n3 = OverlayCursor.compile(35633, string);
            n2 = OverlayCursor.compile(35632, string2);
            program = GL20.glCreateProgram();
            GL20.glAttachShader((int)program, (int)n3);
            GL20.glAttachShader((int)program, (int)n2);
            GL20.glBindAttribLocation((int)program, (int)0, (CharSequence)OverlayCursor.cn("\ud23d\u39cb\u91b8\u0b5f\u8004", 1211883885, 0xCA0920B2 ^ CryptoVault.mask()));
            GL20.glBindAttribLocation((int)program, (int)1, (CharSequence)OverlayCursor.cn("\u898b\uac65\u15ed\u6f88", -614431119, 0xCA0920B2 ^ CryptoVault.mask()));
            GL20.glLinkProgram((int)program);
            GL20.glDeleteShader((int)n3);
            GL20.glDeleteShader((int)n2);
            if (GL20.glGetProgrami((int)program, (int)35714) == 0) {
                GL20.glDeleteProgram((int)program);
                program = 0;
                failed = true;
                return false;
            }
            uTex = GL20.glGetUniformLocation((int)program, (CharSequence)OverlayCursor.cn("\uaa58\udfff\u6aa4\uf8f2\uda72\u1039\u9610\uf663", -1828839002, 0xCA0920B2 ^ CryptoVault.mask()));
            uScale = GL20.glGetUniformLocation((int)program, (CharSequence)OverlayCursor.cn("\ub664\u22a2\u6cc8\u3d47\u0337\u40ce\uf78a\u7471", -1598327737, 0xCA0920B2 ^ CryptoVault.mask()));
            uOutline = GL20.glGetUniformLocation((int)program, (CharSequence)OverlayCursor.cn("\uf772\u128c\uecc1\u7ccd\ub34e\uc2bc\u8568\u0dd5\u9c9d\u17aa\u313a\u4cb1", 870932956, 0xCA0920B2 ^ CryptoVault.mask()));
            float[] fArray = new float[]{-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
            FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)fArray.length);
            floatBuffer.put(fArray).flip();
            vao = GL30.glGenVertexArrays();
            vbo = GL15.glGenBuffers();
            GL30.glBindVertexArray((int)vao);
            GL15.glBindBuffer((int)34962, (int)vbo);
            GL15.glBufferData((int)34962, (FloatBuffer)floatBuffer, (int)35044);
            GL20.glEnableVertexAttribArray((int)0);
            GL20.glVertexAttribPointer((int)0, (int)2, (int)5126, (boolean)false, (int)16, (long)0L);
            GL20.glEnableVertexAttribArray((int)1);
            GL20.glVertexAttribPointer((int)1, (int)2, (int)5126, (boolean)false, (int)16, (long)8L);
            GL30.glBindVertexArray((int)0);
            GL15.glBindBuffer((int)34962, (int)0);
            ready = true;
            return true;
        }
        catch (Throwable throwable) {
            failed = true;
            return false;
        }
    }

    private static int compile(int n2, String string) {
        int n3 = GL20.glCreateShader((int)n2);
        GL20.glShaderSource((int)n3, (CharSequence)string);
        GL20.glCompileShader((int)n3);
        return n3;
    }

    static {
        uTex = -1;
        uScale = -1;
        uOutline = -1;
    }

    private static /* synthetic */ String cn(String string, int n2, int n3) {
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

