/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.I18n;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_2960;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class GpuUiAwtText {
    private static final int PAD = 2;
    private static final int REF = 64;
    private static final int CAPACITY = 700;
    private static final FontRenderContext FRC = new FontRenderContext(null, true, true);
    private static final List<String> DEVANAGARI = List.of(GpuUiAwtText.ad("\u7e3d\uf0ba\u85af\u3eb3\u6964\ue42e\ue759\u8ad7\u4f42\u5839\udbc2\ub29a\u34de", -1195324863, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u9a94\u07a3\ucc90\u6ff3\u1ac9\ubd35\u240d", -1580500997, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u5367\u1a67\ub876\ue20d\uf076\ue310\uedec\u9e8e\u8353\uaa92\ube82\u5d7b\u867a\u72d7\ub2e0\u15c7\u9413\u2a59\u96d0\ua1f0\uda0b\u28a6\u3df0\u98f2", -685037074, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uffbe\u8c3a\u41d6\u4c38\u49bc\u61f6\uf923\ue516\u6b76\ud9e1\u14da\ud94c\uc636\u5397\u8636\u6152\u30e1\u1f6e\ua468\u3cf8\u93c6\u98b2\ua7ab", -1464554792, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ua25b\u5cb9\u4d71\uceb6\u773b\u8402\u7cad\ue6bd\ua6fa\u59af\ucb0f\u52e3\uf376\ud4d6\u364e\udab0\u5c4a\u3242\u6726", -549074299, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u97d9\u0a44\ub091\uafbe\u60bb\ufc43\u9469\u26d2\u51ec\u072c\u9778\uefc2\u3e0b", -1564324199, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> BENGALI = List.of(GpuUiAwtText.ad("\u43a3\u2834\uda70\u840e\u483b\u1726\u403f\u362b\u0698\u9bcf\u0c42\u106e\u8f10\ub538", 610181666, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u3549\u6e6d\u2209\ue8c1\u9997\u1c0e\u485a\u418d", -720306115, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uc6e8\u4be2\ue3ee\u70e6\uc3ec\u4259\u1e2f\u1668\u434f\u8509\u63d0\ua033\uede0\u63c2\ud9f5\u21e0\uc914\u1202", -1733058410, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ubc43\u5470\u501e\ue439\ud3b6\uc8ad\u40e8\ub223\ubaaa\u1384\u736f\udd0d\uef80\u51d2\ub9e3\ueb0e\ubd04", -1853696758, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u44af\uedea\uc24d\u6a63\u6889\u75f7\u6bd1\u57eb\u4d96\u8cf8\u3f9d\u075a\uf695\ubdbc\ue46b\u25dc", -727469948, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> TAMIL = List.of(GpuUiAwtText.ad("\u613a\u1aae\u1d5e\u55c3\u2573\ubabf\uc45a\ub763\u5c77\udc82\ued55\u18da\uaa8b\u754b", -840074849, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u37f5\u6c47\u9b38\u0002\uf1a6\u8e2f", -956747724, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u026d\u9399\u19e7\u0b8b\u64de\u8f3a\u7cc8\u9908\uacb6\ud4e7\uf7e2\u6efb\uad3e\u83a5\u211f\u9c08\u5412\u3d71\ub10b", 997823349, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u05f7\u1b7d\ub9ed\ued8d\ub90b\u99e1\ue996\ud231\u1f2a\u26a9\u882e\u0df4\u5b1c\u8a8b\u0494\u5e5e\u4ec4\u6fdf", -952409902, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ua910\u4481\u6204\u8da2\ua915\u2596\ub812\u7599\u1917\ub0db\ub3ee\ucd59\u1156\u869e\uebef", -1961287866, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> TELUGU = List.of(GpuUiAwtText.ad("\u9842\u6097\u194a\u4084\u8681\ub682\ub8ef\uc231\udff3\u3063\u84aa", -586549636, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u32d9\ucd6c\udf62\u4e57\uee55\ua517\u53a1\u53a4\u8313", -1019491824, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uc58a\u1356\uc836\u998c\u04c5\u2f09\u03c7\u4dec\u143d\u7ab7\uc4e0\u5913\ufbd4\u207c\ub77e\uee57\u60b8\u5cef\u0363\u0b95", 838691549, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u0920\uec6e\ub116\u405e\ub0ba\ub1f3\u21f7\u04cc\u1d36\u34cb\u025e\u780a\ude6c\u6e81\u683c\u5be9\u3b22", 1666298465, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u0594\u4038\u87cd\uffd3\ud065\u269c\uea9b\u5656\u375a\u29ba\u755d\u085c\uf776", -1042049688, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> THAI = List.of(GpuUiAwtText.ad("\u7a18\u7ace\udb45\ue9b8\u470d\u723c\u2264\u1a03\u2fa9\uc1ce\u8682\ue1c9\u3dff\u0808\u92f0\u069a", -1721313856, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ud54c\u27f0\u47ef\u1d57\u1d0a\u14f9\uc0c2\uf282\u4410\uc42d\ua421\uf35f\u76db\u2c51", 1049131515, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uf129\u4a24\u79a0\ufd08\u115a\u19b5\u0617\u744c", -1294068338, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ud41d\u9aaa\u2793\uadcc\ua2e4\u1cb8\u1f83\u1707\u0f11\u6a85\u1270\u290d\u79ef\uec51\uda69\u02e0", 299481742, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ud89f\ubbb9\u3eb7\u313a\ucd2c\u0a1f\u71b6\uf46f\uef26\ue73c", 590342216, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u096b\u4be6\uf233\ue6b4\uddbf", 2101874273, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> ARABIC = List.of(GpuUiAwtText.ad("\u9488\ua583\u9ef9\u3921\uf74c\u246d\ud199\ufa4f\u69b9\u8060\ub271\u64dc", 348302786, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u7f72\u627f\u4d8f\ue924\u9748\u2c15\ub4c9\u8634\u8ec3\u7668", -1300916232, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ua6a9\u36f9\ua907\u95b0\u8c09\u38eb\uccb2\u8ccf\u0a10\uc69d\u7950\u5147\ub0e0\u63b7\u0828\uc056\u3e2e\u71f1\ub559\uc9a0", -1385776521, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u9643\ude24\ubffa\u3087\u945b\u4c94\u0f0a\u6a25\ucd1d\u855d\uaf76\u05a0\u410c\u9b15\uad4c\ud931\u1232\u6cee\ubc68\u4ec0\ud025", -1872872247, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u3c34\u7490\u61e8\u95fc\u43b8\u9598\u18f1\u3365\u3969\u5c6b", 1799159776, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u8b5b\u8dd9\u014b\u6991\u28f3\uf68a\u7fbd\u3fe6", -103299278, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u4c81\ud93b\u2832\u26ad\ua2b4\u9638\ue86b\u7a69\uf24d\u81f8\u360e\u6727", 289243663, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> LATIN = List.of(GpuUiAwtText.ad("\u7e2e\udd12\ub368\ue81a\ud361\u8a8d\ueabd\ubc7d\uf3c9\u5c1b", -432832054, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u861c\u676d\u8873\ua809\u298e\u90c5\u4d74\u4c13\u08f4\u6acb\ub7ea\u1542\ude2f\uabc8\uf3c6\u79dd\u89d7\u6fb2", -120439270, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u28a9\ue7a4\u4c94\ud039\uee07\u53bf\uf936\u5a9b", -2014377346, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uf27d\u9131\u311a\ubad7\udf37\uc40b\uac64\u7d5d\udf9e\u1d98\u6da6", -2037823934, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uce6f\u584f\uffcc\u37c2\uf268\u5dc8\uac1e\u485e\ubd44\u1770\u3537\u1015\uf41b", 276037257, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> CJK_SC = List.of(GpuUiAwtText.ad("\ufaad\uf914\ua576\u71d0\u7b73\u9d52\u90b5\u6d71\u0990\u72ea\u9a5b\u162d\u45cc\u3b0c\u9382\u47b7\u756f\ue8cb\ua8eb\u8532", 2112864687, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u54b0\uac5b\u71d9\u2218\ucb6d\u6137\uf663\u2f5a\ub8c7\u0641\ub184\ua5d1\ub964\u3b79\ub66d\uc3d8", 1833951282, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ub1a2\u4750\u10d6\u90e7\u48de\uc91f\uf0f5\u7968\u6d25\u3b0f\u0e60\u8075@", 760782220, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u0b88\u5387\u86a6\u53e5\u67a5\u08bd\ue410\ucb04\u8a87\u1e29\u2619\u1c2b\ub764\u2501\ub154\ua094\u117c", 570371214, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uab87\u3a68\ufb42\ub707\ud0d6\u6efd\ue307\uff5c\ucf33\u2c73\u480b\u33d2\ue7ad", 64570694, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u16f7\u17ea\u06f2\u5d38\u8970\u13e8\ubff3\u7264\u7cd7\ude59", 1155086350, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u5c57\u37e8\ud69c\uf87f\u0687\u65e8\u496b\ud0f2\u893a\u1f02\u4fb4\u54fc\u428a\u5de8\ua341\u7640\u6aa3\u951a\u5524\u4a8d", 1838059292, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> CJK_TC = List.of(GpuUiAwtText.ad("\ube5d\u8421\u9938\u5efe\u5e82\u5ba6\u59aa\u6053\ud062\uf4df\uf567\uae7f\u16ca\ud4e2\ua749\u1727\u0eaa\u5d76\u9969\ua690\u9088\u8eac\u9d5d", -1107504487, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u059b\u477b\ub49f\ud394\ud4c1\u561e\uaad9\u61b7\u0901\ufcb3\u950c\u17cd\u377d\u954b\u766f\u4163\u7651\ub2c6\uff71", 1533734626, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u5368\ud832\uc86b\uaf65\u40f0\u9b88\u189c\u0be2\uc213\u5f38\u6d0d\u8508\u1a5f", -471723709, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uaa83\ud046\u5954\u6549\u9e65\u9a7d\uf97c\u9f12\u88ff\u7b93\ue77d\uac1a\u704b\udafa\u745f\ueb2b\uc6d9\u4710", 1016578268, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u7c01\u4082\u8759\u0e8e\u9857\u9f39\ue213\u2d1e\u10f1\u165d\u6768\u01d2\u0828\ub38d\ucf2e\udc4f", 1112272875, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> CJK_JP = List.of(GpuUiAwtText.ad("\u7db7\u7f33\u4b0f\u8f80\u78f6\ua973\u2f16\ua1ee\uccfe\u9622\u38e0\u46f2\u8e36\u24ae\ub48b\u22a7", -2076214668, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u567b\ube7c\u311e\u543a\ub51f\ua4e5\u7d7f\ueb1d\u4611\ud8e9", 1680820002, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ue26d\u495f\u7d2a\u4c7f\u4287\u9c6a\uac0b\ua022\uc4a2\ua0e1", -2084561576, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u21f2\u6df4\uc278\u58e8\ubc1e\u386f\ucfa5\u6075\ud910\ucf9a\ub846\uc216\uc3b4\ude78\u6e1d\u71ef\u2168", 79106623, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u630f\ua978\ud5d9\u06c8\u68a2\u31f1\u1268\uc7b1\u5363\u4a31\ue75a\ue10c\u6f54\u1564\u3ce6\ueffc\u217f\u0d48\u643a\u80e1", 812208786, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u4698\ubc6d\ucb58\uf244\uf65d\u30ba\u2a66\u8265\u8866\ue9b3\ucb3a\u7814\ua37a\u9158", -1263521450, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u21e8\u9750\uf813\u8f37\uc059\ufae2\u80fe\u5057\ua04a\u0b91", 586060687, 0xB8852446 ^ CryptoVault.mask()));
    private static final List<String> CJK_KR = List.of(GpuUiAwtText.ad("\u89d1\u202a\u4320\ue303\u3906\u2ecc\u6b3d\u0b13\u6ead\u738f\u4ebb\uc6da\u2358\ud4c4\u4d1c\u2bc6\u47e2", 10794118, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u4c4d\ubce5\u5df0\u59ee\u0476\ud242\ud33b\u210a\uccc2\uf30c\u1fa6\u402f\u11c8\ud3d7\uf920\ua9bb\u1a39\u3644\u743e\u212a\uf056\u65b2\u508b", -1793180490, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\ucc61\ue086\u9a81\u245e\ub2de\u8001\ue749\u0fd8\uebab\u1152\ue195\u9e01\u7c84\u0b2e\uf89f\u4ea7\udbb2", 1438311730, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\uc367\udb4f\u740b\u21ad\u2d8c\u5ae1\ua287\ue0e3\udd46\ue80b\u60ab\u7429\u4afc", -925354006, 0xB8852446 ^ CryptoVault.mask()), GpuUiAwtText.ad("\u8b68\uae42\u8163\u3d91\ud2b3\uf559\uadc0\u444b", -397635401, 0xB8852446 ^ CryptoVault.mask()));
    private static volatile boolean disabled;
    private static Set<String> installed;
    private static final Map<String, Font> FONT_CACHE;
    private static final Map<String, Float> ADVANCE;
    private static final Map<String, Boolean> USABLE;
    private static final LinkedHashMap<String, Entry> ENTRIES;
    private static final ArrayDeque<Object[]> PENDING;
    private static int counter;

    private GpuUiAwtText() {
    }

    private static Set<String> installed() {
        if (installed == null) {
            HashSet<String> hashSet = new HashSet<String>();
            try {
                for (String string : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ROOT)) {
                    hashSet.add(string);
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            installed = hashSet;
        }
        return installed;
    }

    private static boolean has(int n2, int n3, int n4) {
        return n2 >= n3 && n2 <= n4;
    }

    /*
     * Exception decompiling
     */
    private static List<String> candidates(String var0) {
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

    private static Font font(String string, boolean bl) {
        List<String> list = GpuUiAwtText.candidates(string);
        String string2 = System.identityHashCode(list) + (bl ? "b" : "r");
        Font font = FONT_CACHE.get(string2);
        if (font != null) {
            return font;
        }
        Set<String> set = GpuUiAwtText.installed();
        int n2 = bl ? 1 : 0;
        Font font2 = null;
        for (String string3 : list) {
            if (!set.contains(string3)) continue;
            font2 = new Font(string3, n2, 64);
            break;
        }
        if (font2 == null) {
            font2 = new Font(GpuUiAwtText.ad("\uab47\uffd3\u7aa6\u9785\u00a6\u4993\u46b0\uf586\ua5a2\u81a2\ufff8\u3ff7\ud1e6", -1514336721, 0xB8852446 ^ CryptoVault.mask()), n2, 64);
        }
        FONT_CACHE.put(string2, font2);
        return font2;
    }

    private static Font base(String string, boolean bl) {
        Font font = GpuUiAwtText.font(string, bl);
        if (I18n.isRtl()) {
            HashMap<TextAttribute, Boolean> hashMap = new HashMap<TextAttribute, Boolean>();
            hashMap.put(TextAttribute.RUN_DIRECTION, TextAttribute.RUN_DIRECTION_RTL);
            font = font.deriveFont(hashMap);
        }
        return font;
    }

    public static boolean usable(String string, UiDrawCommand.TextWeight textWeight) {
        boolean bl;
        if (disabled) {
            return false;
        }
        String string2 = (textWeight == UiDrawCommand.TextWeight.SEMIBOLD ? "b" : "r") + string;
        Boolean bl2 = USABLE.get(string2);
        if (bl2 != null) {
            return bl2;
        }
        try {
            Font font = GpuUiAwtText.font(string, textWeight == UiDrawCommand.TextWeight.SEMIBOLD);
            bl = font.canDisplayUpTo(string) == -1;
        }
        catch (Throwable throwable) {
            disabled = true;
            bl = false;
        }
        if (USABLE.size() > 4000) {
            USABLE.clear();
        }
        USABLE.put(string2, bl);
        return bl;
    }

    public static float width(String string, float f2, UiDrawCommand.TextWeight textWeight) {
        boolean bl = textWeight == UiDrawCommand.TextWeight.SEMIBOLD;
        String string2 = (bl ? "b" : "r") + string;
        Float f3 = ADVANCE.get(string2);
        if (f3 == null) {
            float f4;
            try {
                f4 = new TextLayout(string, GpuUiAwtText.base(string, bl), FRC).getAdvance();
            }
            catch (Throwable throwable) {
                f4 = (float)(string.length() * 64) * 0.6f;
            }
            if (ADVANCE.size() > 4000) {
                ADVANCE.clear();
            }
            ADVANCE.put(string2, Float.valueOf(f4));
            f3 = Float.valueOf(f4);
        }
        return f3.floatValue() * f2 / 64.0f;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Entry get(String string, UiDrawCommand.TextWeight textWeight, float f2) {
        Object[] objectArray;
        if (disabled) {
            return null;
        }
        boolean bl = textWeight == UiDrawCommand.TextWeight.SEMIBOLD;
        int n2 = Math.max(8, Math.min(160, Math.round(f2)));
        String string2 = n2 + (bl ? "b" : "r") + string;
        Entry entry = ENTRIES.get(string2);
        long l2 = System.nanoTime();
        while (!PENDING.isEmpty() && l2 - (Long)PENDING.peek()[1] > 600000000L) {
            objectArray = PENDING.poll();
            try {
                class_310.method_1551().method_1531().method_4615((class_2960)objectArray[0]);
            }
            catch (Throwable throwable) {}
        }
        if (entry != null) {
            return entry;
        }
        try {
            objectArray = GpuUiAwtText.base(string, bl).deriveFont((float)n2);
            TextLayout textLayout = new TextLayout(string, (Font)objectArray, FRC);
            int n3 = (int)Math.ceil(textLayout.getAdvance()) + 4 + 2;
            float f3 = textLayout.getAscent();
            int n4 = (int)Math.ceil(textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 4;
            if (n3 <= 0 || n4 <= 0 || n3 > 4096 || n4 > 1024) {
                return null;
            }
            BufferedImage bufferedImage = new BufferedImage(n3, n4, 2);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            try {
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(Color.WHITE);
                textLayout.draw(graphics2D, 2.0f, 2.0f + f3);
            }
            finally {
                graphics2D.dispose();
            }
            class_1011 class_10112 = new class_1011(n3, n4, true);
            for (int i2 = 0; i2 < n4; ++i2) {
                for (int i3 = 0; i3 < n3; ++i3) {
                    class_10112.method_61941(i3, i2, bufferedImage.getRGB(i3, i2));
                }
            }
            class_2960 class_29602 = class_2960.method_60655((String)GpuUiAwtText.ad("\u9616\u13e5\u82dc\u31e0\u7f59\u4081\u40d2\u22ef\u115e\ufb4e\u25ae\uddff\ufc7b", -332325, 0xB8852446 ^ CryptoVault.mask()), (String)(GpuUiAwtText.ad("\u4331\u8406\u2480\u2f62\uf408\u5e1e\u4c8e\u2e65\uf91e\u2da3\u4e97\u50f7\u9a59\ud96f\ub370\u2c3e\u8783\ud9a1\u8128\u57a9\ube3e\u0e63\ua7fe\u5151", -220467903, 0xB8852446 ^ CryptoVault.mask()) + counter++));
            class_1043 class_10432 = new class_1043(() -> GpuUiAwtText.ad("\u4165\ueb52\uc39b\u82a8\u6930\u1482\u4765\ude98\uc649\ua1f0\ubfda\u153c\u90a0", 2002047349, 0xB8852446 ^ CryptoVault.mask()), class_10112);
            class_10432.method_4524();
            class_310.method_1551().method_1531().method_4616(class_29602, (class_1044)class_10432);
            Entry entry2 = new Entry(class_29602, n3, n4, 2, f3, n2);
            ENTRIES.put(string2, entry2);
            if (ENTRIES.size() > 700) {
                Iterator<Map.Entry<String, Entry>> iterator = ENTRIES.entrySet().iterator();
                for (int i4 = 0; i4 < 64 && iterator.hasNext(); ++i4) {
                    Entry entry3 = iterator.next().getValue();
                    iterator.remove();
                    PENDING.add(new Object[]{entry3.id(), l2});
                }
            }
            return entry2;
        }
        catch (Throwable throwable) {
            disabled = true;
            return null;
        }
    }

    public static void clear() {
        long l2 = System.nanoTime();
        for (Entry entry : ENTRIES.values()) {
            PENDING.add(new Object[]{entry.id(), l2});
        }
        ENTRIES.clear();
    }

    static {
        FONT_CACHE = new HashMap<String, Font>();
        ADVANCE = new HashMap<String, Float>();
        USABLE = new HashMap<String, Boolean>();
        ENTRIES = new LinkedHashMap(256, 0.75f, true);
        PENDING = new ArrayDeque();
    }

    private static /* synthetic */ String ad(String string, int n2, int n3) {
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
    public record Entry(class_2960 id, int width, int height, int padding, float ascent, int bucket) {
    }
}

