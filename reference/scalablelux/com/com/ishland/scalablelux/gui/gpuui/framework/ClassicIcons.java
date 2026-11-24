/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.overlay.OverlayCursor;
import com.ishland.scalablelux.util.CryptoVault;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public final class ClassicIcons {
    private static final String PREFIX = ClassicIcons.kt("\u24a4\ua26d\u52c7\ufaca\u4ceb\u1142\u6235\u9ca5\u79b6\u9de9\u3403\ud3f8\ufcf0\u981b\u85f0\uf2f1\u257c\u9b96\ue2e2\u537f\ua1c5\u1a8f\u4934\ua858", -1123456448, 0xA7FA71F ^ CryptoVault.mask());
    private static final float CURSOR_HOTSPOT = 0.046875f;
    private static final float BASE_CURSOR_SIZE = 21.0f;
    private static volatile float cachedOsCursorScale = -1.0f;
    private static volatile boolean cursorProbeStarted;

    private ClassicIcons() {
    }

    /*
     * Exception decompiling
     */
    public static String forCategory(String var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static String forModule(String string, String string2) {
        return ClassicIcons.forCategory(string);
    }

    /*
     * Exception decompiling
     */
    public static String forRail(String var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static void draw(UiDisplayList.Builder builder, String string, float f2, float f3, float f4, int n2) {
        Objects.requireNonNull(builder, ClassicIcons.kt("\u9ac0\ue7f1\u73ae\u31b1\u015c\u8071", -809709825, 0xA7FA71F ^ CryptoVault.mask()));
        if (!Float.isFinite(f2) || !Float.isFinite(f3) || !Float.isFinite(f4) || f4 <= 0.0f) {
            return;
        }
        builder.image(f2, f3, f4, f4, 0.0f, n2, ClassicIcons.texture(string));
    }

    public static void drawLogo(UiDisplayList.Builder builder, float f2, float f3, float f4, int n2) {
        ClassicIcons.draw(builder, ClassicIcons.kt("\ud2ea\uaf8f\udd5f\u7005\u73f6\u6125", 1311730185, 0xA7FA71F ^ CryptoVault.mask()), f2, f3, f4, n2);
    }

    public static void warmCursorScale() {
        if (cursorProbeStarted) {
            return;
        }
        cursorProbeStarted = true;
        Thread thread = new Thread(() -> {
            cachedOsCursorScale = ClassicIcons.probeOsCursorScale();
        }, ClassicIcons.kt("\u63a6\ubfda\u3212\uaa26\u751f\u392d\u9455\u31ac\ubec9\u3a40\u12b1\ufe0e\u1687", -878800584, 0xA7FA71F ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    public static float osCursorScaleNow() {
        float f2 = cachedOsCursorScale;
        if (f2 > 0.0f) {
            return f2;
        }
        ClassicIcons.warmCursorScale();
        return 1.0f;
    }

    public static void drawCursor(UiDisplayList.Builder builder, float f2, float f3, int n2) {
        if (!Float.isFinite(f2) || !Float.isFinite(f3)) {
            return;
        }
        ClassicIcons.warmCursorScale();
        if (OverlayCursor.takeOver()) {
            return;
        }
        float f4 = 21.0f * ClassicIcons.osCursorScaleNow() / (float)Math.max(1, n2);
        float f5 = f4 * 0.046875f;
        float f6 = f2 - f5;
        float f7 = f3 - f5;
        class_2960 class_29602 = ClassicIcons.texture(ClassicIcons.kt("\u4f50\u6aba\u8147\ud08b\u3d76\u3f87\uc80d\u6250\u0354\ub28f", -457027009, 0xA7FA71F ^ CryptoVault.mask()));
        float f8 = Math.max(0.6f, f4 * 0.07f);
        int n3 = -436207616;
        for (int i2 = 0; i2 < 8; ++i2) {
            double d2 = (double)i2 * Math.PI / 4.0;
            builder.image(f6 + (float)Math.cos(d2) * f8, f7 + (float)Math.sin(d2) * f8, f4, f4, 0.0f, n3, class_29602);
        }
        builder.image(f6, f7, f4, f4, 0.0f, -1, class_29602);
    }

    private static float probeOsCursorScale() {
        String string = System.getProperty(ClassicIcons.kt("\uf203\u580d\u2361\u51ce\ufb0c\u9d0c\u470a\uff29\u2335\ufd38\u7f65", 471317805, 0xA7FA71F ^ CryptoVault.mask()), "").toLowerCase(Locale.ROOT);
        try {
            if (string.contains(ClassicIcons.kt("\u78ed\uf410\ue24f\ucf6d\u9cbd", -1426193540, 0xA7FA71F ^ CryptoVault.mask()))) {
                String string2 = ClassicIcons.runProbe(ClassicIcons.kt("\u31b8\u123f\u60f9\ufdab", -2128710318, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\ucc8a\u8261\u8fc9\ufe81\u161d\u5183\u3f1e\ub7b5\uc217", -457140751, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\u66e4\u60cf\u1724\u3274\ud2a2\u1ee0\u7973\u47bc\u7793\u7782\ud832\ua6f9\ucd51\u0aa3\u154c\ub4d7\u41ba\u6a49\u7da5\u9bde\u60d4\u3b2e\uad15\udcb9\u1bf2\u9801\ucd88\u8058\uc190\u4747", 910628477, 0xA7FA71F ^ CryptoVault.mask()), "/v", ClassicIcons.kt("\u874c\ud68b\ufd59\uc45e\u43cf\u1e7b\uac47\u3996\ucf5c\u1289\ue1b0\u85c1\ucd74\u0ce5\u3cea\u225f\u4186\u8556", -605963147, 0xA7FA71F ^ CryptoVault.mask()));
                Integer n2 = ClassicIcons.parseRegDword(string2, ClassicIcons.kt("\u0ba3\u46d1\ud804\u57cc\ucf86\u392d\ud270\u8531\u27ef\u63c0\u6735\u080c\uf2b5\ufc42\ubb11", -1804252231, 0xA7FA71F ^ CryptoVault.mask()));
                if (n2 != null && n2 > 0) {
                    return (float)n2.intValue() / 32.0f;
                }
            } else if (string.contains(ClassicIcons.kt("\ue24f\ucd75\u1c57\u4b66", -704319758, 0xA7FA71F ^ CryptoVault.mask()))) {
                String string3 = ClassicIcons.runProbe(ClassicIcons.kt("\u1f8e\u3f78\u07d3\u9ac4\u0cec\u078e\u1029\u91a8\u5a8c\u4bd9\u5ab2", 874912299, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\u8214\u2278\ue183\ua186\u310b\ud88a\uc6c4\u33b8", -1185537880, 0xA7FA71F ^ CryptoVault.mask()), "-g", ClassicIcons.kt("\u8a6d\u5a50\udd5c\ud815\ud8a5\u571e\u09ce\ub02d\ud7e3\ubddd\uacf0\ue9ce\ubfb9\ud5aa\ub948\u8e0d\u8226\u0bb4\uee68\ueb18\u3e84\u634f\uf311\ue0ce\u3613\ue9af\u92dd\u7eb5\u3a7b\u706e", -937500448, 0xA7FA71F ^ CryptoVault.mask()));
                Float f2 = ClassicIcons.parseFloat(string3);
                if (f2 != null && f2.floatValue() > 0.0f) {
                    return f2.floatValue();
                }
            } else {
                String string4 = ClassicIcons.runProbe(ClassicIcons.kt("\u9a90\u5b43\ubc19\ua9c8\ueb56\ubcf6\u8642\u650f\uc52f\u9aa8\uafb7\u0249", -1314297463, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\u5279\ud737\u2f1d\ua0e8\ucbf2", -241443477, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\u6cac\u4adf\uea92\u19bc\uc7c9\u1407\u98f8\ue9ec\uf91f\ua5d4\ub387\u7cce\u219c\u4061\ub9db\ub28e\uf9ad\udbd9\u7a15\u5c3a\udbe2\u2f4f\u64c4\ub506\u2d2e\u4899\ua961\u9e8d\u5517\u1234\u7759", -2081787286, 0xA7FA71F ^ CryptoVault.mask()), ClassicIcons.kt("\u331a\u36a5\u5c85\u29f6\u07be\u9dc6\u4092\u1227\u3903\u80e0\u9bab\u869c\uede3\u8622\u2c97", -1244184411, 0xA7FA71F ^ CryptoVault.mask()));
                Float f3 = ClassicIcons.parseFloat(string4);
                if (f3 != null && f3.floatValue() > 0.0f) {
                    return f3.floatValue() / 24.0f;
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return 1.0f;
    }

    private static Integer parseRegDword(String string, String string2) {
        if (string == null) {
            return null;
        }
        for (String string3 : string.split(ClassicIcons.kt("\ufafd\u0db3\uf94d\u73ff\u38c4\u1c78\u8669\u9921", -907322058, 0xA7FA71F ^ CryptoVault.mask()))) {
            if (!string3.contains(string2)) continue;
            String[] stringArray = string3.trim().split(ClassicIcons.kt("\ucd99\ud661\uc3fa\u7c68\u25da", -1585789011, 0xA7FA71F ^ CryptoVault.mask()));
            String string4 = stringArray[stringArray.length - 1].trim();
            try {
                return string4.toLowerCase(Locale.ROOT).startsWith("0x") ? Integer.parseInt(string4.substring(2), 16) : Integer.parseInt(string4);
            }
            catch (NumberFormatException numberFormatException) {
                return null;
            }
        }
        return null;
    }

    private static Float parseFloat(String string) {
        if (string == null) {
            return null;
        }
        try {
            return Float.valueOf(Float.parseFloat(string.trim()));
        }
        catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    private static String runProbe(String ... stringArray) {
        for (String string : stringArray) {
            if (string != null && string.matches(ClassicIcons.kt("\ubf5a\u1d77\u7261\u385c\udcb4\u2267\u7cd7\u51f2\ubd56\ufac5\ua91b\u8131\u2005\u317c\ue97e\ucb51\ub628\ub752\ua71f\u2a0a\u33a6\u680f\u8a34\u7c7f\ufec4\uc97c", -728371357, 0xA7FA71F ^ CryptoVault.mask()))) continue;
            return null;
        }
        Object object = null;
        try {
            object = new ProcessBuilder(stringArray).redirectErrorStream(true).start();
            if (!((Process)object).waitFor(800L, TimeUnit.MILLISECONDS)) {
                ((Process)object).destroyForcibly();
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((Process)object).getInputStream(), StandardCharsets.UTF_8));){
                String string;
                while ((string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string).append('\n');
                }
            }
            return ((Process)object).exitValue() == 0 ? stringBuilder.toString() : null;
        }
        catch (Throwable throwable) {
            if (object != null) {
                ((Process)object).destroyForcibly();
            }
            return null;
        }
    }

    public static class_2960 texture(String string) {
        return class_2960.method_60655((String)ClassicIcons.kt("\u6272\uc331\u8cc0\u2af7\ua0e6\ufd50\ue287\ufbcd\u1e4b\u7dc8F\ucfc5\u29aa", -335765615, 0xA7FA71F ^ CryptoVault.mask()), (String)(ClassicIcons.kt("\u2c0a\u0590\u0afc\uce2d\ud5d5\ueb9a\u19cd\uf355\u094d\u9419\u189b\ub614\u9a4c\u09c1\u2607\uf289\uba3a\ue258\uc788\u943a\u5bef\u4387\u23b7", -1308812037, 0xA7FA71F ^ CryptoVault.mask()) + ClassicIcons.knownIcon(string) + ClassicIcons.kt("\uf2ed\u5b41\u1c6f\u9b1e\udfd0\ua24c", -710473297, 0xA7FA71F ^ CryptoVault.mask())));
    }

    /*
     * Exception decompiling
     */
    private static String knownIcon(String var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getString(SwitchStringRewriter.java:404)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$600(SwitchStringRewriter.java:53)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:368)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:201)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:73)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:881)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static /* synthetic */ String kt(String string, int n2, int n3) {
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

