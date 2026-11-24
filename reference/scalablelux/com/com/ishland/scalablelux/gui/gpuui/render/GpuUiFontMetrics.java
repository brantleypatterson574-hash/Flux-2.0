/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3298
 */
package com.ishland.scalablelux.gui.gpuui.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiAwtText;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFallbackText;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.I18n;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;

@Environment(value=EnvType.CLIENT)
public final class GpuUiFontMetrics {
    public static final String METRICS_PATH = GpuUiFontMetrics.bj("\u755f\u5813\u81bd\u542b\u632c\ueccb\u8a71\u03fb\ub5a7\uf05d\u8449\u601e\ua29c\u8847\u1473\ua124\u203a\u3a6b\u0868\u8108\u0f94\u9486\u4523\ub52e\u2579\u3b48\u9f48\ud461\ubf22\u3a48\u40e7\ue59b\u4350\u949c\u0859\ude7e\u720d\ub33b\uebcf\u27f9\u6012\uf164\u89c1\u610f", -432091462, 0x8FBCDBE9 ^ CryptoVault.mask());
    public static final String ATLAS_PATH = GpuUiFontMetrics.bj("\uff7c\u46f1\ua24b\u1b80\u011f\u472b\u9a0d\u41e6\u5753\u3d79\ua4ee\ub631\u32c9\u3b33\ubc22\udbdb\u30e6\u9115\u6ebf\u0c96\u09df\u2e39\u0ee7\u2391\u7475\u9e1e\u6493\ued70\u230c\u18c2\uee49\ue752\u4dd1\u3a34\ue798\ud937\u5934\u6b26\ud44e\u0005\ufcab\u76cc\u1399\u16a7\ude96", -411908263, 0x8FBCDBE9 ^ CryptoVault.mask());
    private static final String REGULAR_BASE = GpuUiFontMetrics.bj("\u6e95\u7d22\ucba6\uef4a\uaab0\u79b7\u0b0b\ufe23\uf2f1\ubbd1\uadfd\u90bd\uf254\ue214\uee29\u6a84\ua9b6\u56d3\uabe9\uf04d\u3659\u1e62\uafc6\ud05b\u4869\u1111\ua679", -206202703, 0x8FBCDBE9 ^ CryptoVault.mask());
    private static final String SEMIBOLD_BASE = GpuUiFontMetrics.bj("\u2bd3\u2df5\uf1b5\u96d9\u2f58\u3ea7\uc006\u9b51\u41ab\u1e6d\uaca2\ud24a\u0850\ud6b0\u1b78\u735c\u6b67\u48d6\u733a\ufd29\u4afe\u2160\uea58\u52fb\uc14f\udde6\u0825\ub348\u3e8c", 1606824368, 0x8FBCDBE9 ^ CryptoVault.mask());
    private static final String FALLBACK_BASE = GpuUiFontMetrics.bj("\u57b1\u799b\ub0c5\ufd67\u718b\u1e91\u2736\u085c\ue44b\u577f\ueaf4\ubb1d\u15f0\u8b67\u2fe9\u853c\u4035\ud893\u29dc\u93a5\u99ac", -69354432, 0x8FBCDBE9 ^ CryptoVault.mask());
    public static final int ATLAS_SIZE = 4096;
    public static final float DISTANCE_RANGE = 32.0f;
    private static volatile Family activeFamily = Family.DEFAULT;
    private static volatile Map<String, Face> faces = Map.of();
    private static volatile float textScale = 1.0f;
    public static final float CAP_CENTER = 0.6055f;
    public static final float CAP_HEIGHT = 0.727f;
    private static final Map<Integer, String> SUBSTITUTES = Map.of(34, "''", 8230, GpuUiFontMetrics.bj("\u571b\u75d2\u2d8c\u678c\u525c\u74f6\ue413", 649381938, 0x8FBCDBE9 ^ CryptoVault.mask()), 215, "x", 8226, "\u00b7", 8594, ">", 8592, "<");
    private static final ConcurrentHashMap<String, WidthEntry> WIDTHS = new ConcurrentHashMap();
    private static final int WIDTH_CACHE_LIMIT = 16384;

    public static void setFamily(Family family) {
        activeFamily = family == null ? Family.DEFAULT : family;
    }

    public static Family family() {
        return activeFamily;
    }

    private static String baseFor(Family family, UiDrawCommand.TextWeight textWeight) {
        boolean bl = textWeight == UiDrawCommand.TextWeight.SEMIBOLD;
        return switch (family.ordinal()) {
            default -> throw new MatchException(null, null);
            case 1 -> {
                if (bl) {
                    yield GpuUiFontMetrics.bj("\u757f\u5add\ud27d\u19b3\ub9b6\u3f4d\u0f02\ufe50\u599f\u9232\u5f0a\udff9\ue54c\u6719\u9a51\u9f9e\u9c4d\ucf02\u218f\uadb7\u6d4d\ud292\u1400\u6306\uc82c\udc90\u408c\u4e15\uab28\u888f\ud314\uc864\ue061\ub61e", -2119184213, 0x8FBCDBE9 ^ CryptoVault.mask());
                }
                yield GpuUiFontMetrics.bj("\ubea5\u267b\u527d\uccbf\u3436\uf609\uc247\uc34a\u728d\uddfa\u307b\u430c\ua75f\uf19d\u4340\u16b3\ubf42\uf6e0\u9a99\u8662\ub3d2\ud3ff\u8644\ub2dc\uecf6\u6446\uf434\u9467\ue160\uda39\u08e3\u5c3b", -1373279137, 0x8FBCDBE9 ^ CryptoVault.mask());
            }
            case 2 -> {
                if (bl) {
                    yield GpuUiFontMetrics.bj("\ua76b\u2c28\u2cde\ucde3\ua20e\u4730\u8b17\u291f\u1703\u338c\u8d1d\ue232\u297b\ua128\u6f09\ucb33\u0d7f\ua3d6\u35a4\u8b54\uc5e1\uaf96\u8a11\ua229\ua160\ufe45\u995f\ud720", 698340504, 0x8FBCDBE9 ^ CryptoVault.mask());
                }
                yield GpuUiFontMetrics.bj("\u1dfc\ufdea\u0bb3\u008a\u72a0\u2d2f\ub3b9\u0461\ua11b\uc9db\u3f3f\u671c\u714a\uc5fc\u2a08\u1657\uc506\u3f27\u5f80\uedb8\ue053\ufaa3\u2844\u46e3\ud1c3\u7fac\u8e53\ufd74", -1799653790, 0x8FBCDBE9 ^ CryptoVault.mask());
            }
            case 3 -> {
                if (bl) {
                    yield GpuUiFontMetrics.bj("\ue565\uc3e4\u487c\u69af\u180c\u9556\u52fe\u3e72\u1c73\u1ed4\u5d3d\ue5ab\u3114\ufcca\u5e6e\uae42\ud432\u8477\u5c80\u99ee\ua94b\u6053\u61ca\u9f02\u7e4c\ua2a2\uc3dc\u620d\ue09a\u982a\u2a58\ude3d", -167300193, 0x8FBCDBE9 ^ CryptoVault.mask());
                }
                yield GpuUiFontMetrics.bj("\uc1a7\ucc05\u9958\u3dc0\u72c1\u1599\u521b\uec83\u6fef\u120c\uc607\uad45\ud111\u1c3c\ua677\ubc7b\u71a5\u9b9a\ub371\u7aa9\udae1\u0e76\ud1f1\u063a\u05f2\u1f0b\u4c51\ubea8\u26c4\u6add\u703f\ucde9\uba1d", 1001333438, 0x8FBCDBE9 ^ CryptoVault.mask());
            }
            case 0 -> bl ? GpuUiFontMetrics.bj("\ucdd6\u9f5e\u863e\uccb4\ua608\ud7b1\uac5f\uae93\ub4f3\uda9a\uc040\u2dd2\ua76c\u6ddd\ud94c\u95cc\u0f19\ue365\u3e2b\u4383\u8f5f\u5bde\u29f3\u96a6\u54c1\u1019\u9211\ud15e\u84b1", -172069614, 0x8FBCDBE9 ^ CryptoVault.mask()) : GpuUiFontMetrics.bj("\u2311\uaec6\u455a\ude9c\u09ca\uf359\ub9aa\u21e4\u4586\u1f63\uf27e\u6f63\u0b07\ubf55\uf523\u9f0c\u4e67\u9514\u83a3\u325f\u02dc\u5d2c\u6825\u32e2\u1a1d\u42c3", 750623161, 0x8FBCDBE9 ^ CryptoVault.mask());
        };
    }

    public static void setTextScale(float f2) {
        if (!Float.isFinite(f2)) {
            return;
        }
        textScale = Math.max(0.5f, Math.min(2.0f, f2));
    }

    public static float textScale() {
        return textScale;
    }

    private GpuUiFontMetrics() {
    }

    public static float topForCenter(float f2, float f3) {
        return f2 - f3 * 0.6055f;
    }

    public static float topInBox(float f2, float f3, float f4) {
        return GpuUiFontMetrics.topForCenter(f2 + f3 * 0.5f, f4);
    }

    private static String expand(String string, Map<Integer, Glyph> map) {
        int n2;
        StringBuilder stringBuilder = null;
        for (int i2 = 0; i2 < string.length(); i2 += n2) {
            String string2;
            int n3 = string.codePointAt(i2);
            n2 = Character.charCount(n3);
            String string3 = string2 = map.containsKey(n3) ? null : SUBSTITUTES.get(n3);
            if (string2 != null && stringBuilder == null) {
                stringBuilder = new StringBuilder(string.length() + 8).append(string, 0, i2);
            }
            if (stringBuilder == null) continue;
            if (string2 != null) {
                stringBuilder.append(string2);
                continue;
            }
            stringBuilder.appendCodePoint(n3);
        }
        return stringBuilder == null ? string : stringBuilder.toString();
    }

    public static void layout(String string, float f2, float f3, float f4, GlyphSink glyphSink) {
        GpuUiFontMetrics.layout(string, f2, f3, f4, UiDrawCommand.TextWeight.REGULAR, glyphSink);
    }

    public static void layout(String string, float f2, float f3, float f4, UiDrawCommand.TextWeight textWeight, GlyphSink glyphSink) {
        if (string == null || string.isEmpty() || f4 <= 0.0f) {
            return;
        }
        String string2 = I18n.tr(string);
        GpuUiFontMetrics.layoutPrepared(GpuUiFontMetrics.needsFallback(string2, textWeight) ? string : string2, f2, f3, f4, textWeight, glyphSink);
    }

    public static void layoutPrepared(String string, float f2, float f3, float f4, UiDrawCommand.TextWeight textWeight, GlyphSink glyphSink) {
        if (string == null || string.isEmpty() || f4 <= 0.0f) {
            return;
        }
        float f5 = f4;
        f4 *= textScale;
        Face face = GpuUiFontMetrics.face(textWeight);
        Map<Integer, Glyph> map = face.glyphs();
        Glyph[] glyphArray = face.ascii();
        boolean bl = true;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (c2 < '\u0080' && glyphArray[c2] != null) continue;
            bl = false;
            break;
        }
        if (!bl) {
            string = GpuUiFontMetrics.expand(string, map);
        }
        Glyph glyph = glyphArray[63];
        float f6 = f2;
        float f7 = f3 + f5 * 0.6055f + f4 * face.capHeight() * 0.5f;
        float f8 = 0.012f * f4;
        int n2 = 0;
        while (n2 < string.length()) {
            Glyph glyph2;
            int n3 = string.codePointAt(n2);
            n2 += Character.charCount(n3);
            Glyph glyph3 = glyph2 = n3 < 128 ? glyphArray[n3] : null;
            if (glyph2 == null) {
                glyph2 = map.getOrDefault(n3, glyph);
            }
            if (glyph2 == null) continue;
            if (glyph2.hasBounds()) {
                float f9 = f6 + glyph2.left() * f4;
                float f10 = f7 - glyph2.top() * f4;
                float f11 = (glyph2.right() - glyph2.left()) * f4;
                float f12 = (glyph2.top() - glyph2.bottom()) * f4;
                glyphSink.accept(f9, f10, f11, f12, glyph2.atlasLeft() / 4096.0f, 1.0f - glyph2.atlasTop() / 4096.0f, glyph2.atlasRight() / 4096.0f, 1.0f - glyph2.atlasBottom() / 4096.0f);
            }
            f6 += glyph2.advance() * f4 + f8;
        }
    }

    public static float textWidth(String string, float f2) {
        return GpuUiFontMetrics.textWidth(string, f2, UiDrawCommand.TextWeight.REGULAR);
    }

    public static float textWidth(String string, float f2, UiDrawCommand.TextWeight textWeight) {
        if (string == null || string.isEmpty() || f2 <= 0.0f) {
            return 0.0f;
        }
        I18n.Language language = I18n.active();
        WidthEntry widthEntry = WIDTHS.get(string);
        if (widthEntry != null && widthEntry.size == f2 && widthEntry.weight == textWeight && widthEntry.scale == textScale && widthEntry.faceSet == faces && widthEntry.language == language && widthEntry.family == activeFamily) {
            return widthEntry.width;
        }
        float f3 = GpuUiFontMetrics.measureWidth(string, f2, textWeight);
        if (WIDTHS.size() >= 16384) {
            WIDTHS.clear();
        }
        WIDTHS.put(string, new WidthEntry(f2, textWeight, textScale, faces, language, (Object)activeFamily, f3));
        return f3;
    }

    private static float measureWidth(String string, float f2, UiDrawCommand.TextWeight textWeight) {
        int n2;
        if (GpuUiFontMetrics.needsFallback(string = I18n.tr(string), textWeight)) {
            return GpuUiAwtText.usable(string, textWeight) ? GpuUiAwtText.width(string, f2 * textScale, textWeight) : GpuUiFallbackText.width(string, f2 * textScale, textWeight);
        }
        Map<Integer, Glyph> map = GpuUiFontMetrics.face(textWeight).glyphs();
        string = GpuUiFontMetrics.expand(string, map);
        float f3 = 0.0f;
        float f4 = 0.012f * (f2 *= textScale);
        for (int i2 = 0; i2 < string.length(); i2 += Character.charCount(n2)) {
            n2 = string.codePointAt(i2);
            Glyph glyph = map.getOrDefault(n2, map.get(63));
            if (glyph == null) continue;
            f3 += glyph.advance() * f2 + f4;
        }
        return f3;
    }

    public static boolean needsFallback(String string, UiDrawCommand.TextWeight textWeight) {
        int n2;
        if (string == null) {
            return false;
        }
        Map<Integer, Glyph> map = GpuUiFontMetrics.face(textWeight).glyphs();
        for (int i2 = 0; i2 < string.length(); i2 += Character.charCount(n2)) {
            n2 = string.codePointAt(i2);
            if (n2 < 128 || map.containsKey(n2) || SUBSTITUTES.containsKey(n2) || Character.isWhitespace(n2) || Character.isISOControl(n2)) continue;
            return true;
        }
        return false;
    }

    public static void invalidate() {
        faces = Map.of();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Face face(UiDrawCommand.TextWeight textWeight) {
        UiDrawCommand.TextWeight textWeight2 = textWeight == null ? UiDrawCommand.TextWeight.REGULAR : textWeight;
        String string = GpuUiFontMetrics.baseFor(activeFamily, textWeight2);
        Face face = faces.get(string);
        if (face != null) {
            return face;
        }
        Class<GpuUiFontMetrics> clazz = GpuUiFontMetrics.class;
        synchronized (GpuUiFontMetrics.class) {
            Face face2;
            face = faces.get(string);
            if (face != null) {
                // ** MonitorExit[var4_4] (shouldn't be in output)
                return face;
            }
            try {
                face2 = GpuUiFontMetrics.load(string);
            }
            catch (Exception exception) {
                try {
                    face2 = GpuUiFontMetrics.load(GpuUiFontMetrics.bj("\u7d2e\ufaf8\ud2f5\ua41e\u696c\uc2cc\u0913\ub75a\u5b7a\u0fab\ue522\u138e\u64dc\u836b\uff57\u3d52\ua1a8\uced8\u788e\udd9e\ua857", 1207790310, 0x8FBCDBE9 ^ CryptoVault.mask()));
                }
                catch (Exception exception2) {
                    exception2.addSuppressed(exception);
                    throw new IllegalStateException(GpuUiFontMetrics.bj("\u8d49\uc8cf\u8155\u3369\u455e\u80fb\u1df3\ue11f\u15f0\udfdf\u8fe7\u59de\u8709\uc885\u68fb\u5876\u15ac\u7e18\u33f8\u8af4\u7bf2\u5a62\ub57f\u4326\u3726\u2012\u29f1\u6c55\u960c\udbf3\ua82c\u6701\u1aad\u7c14\u060a\ub55al\u0354\ue513", -1479801580, 0x8FBCDBE9 ^ CryptoVault.mask()), exception2);
                }
            }
            HashMap<String, Face> hashMap = new HashMap<String, Face>(faces);
            hashMap.put(string, face2);
            faces = Map.copyOf(hashMap);
            // ** MonitorExit[var4_4] (shouldn't be in output)
            return face2;
        }
    }

    private static Face load(String string) {
        float f2;
        Glyph[] glyphArray;
        HashMap<Integer, Glyph> hashMap = new HashMap<Integer, Glyph>();
        try (Object object = GpuUiFontMetrics.openMetrics(string);
             InputStreamReader inputStreamReader = new InputStreamReader((InputStream)object, StandardCharsets.UTF_8);){
            glyphArray = JsonParser.parseReader((Reader)inputStreamReader).getAsJsonObject();
            JsonObject jsonObject = glyphArray.getAsJsonObject(GpuUiFontMetrics.bj("\u2cbe\u4a65\u63d9\u7907\ub1d6\u709d\u9a7a\uacec\u4cf5", 547138116, 0x8FBCDBE9 ^ CryptoVault.mask()));
            if (jsonObject.get(GpuUiFontMetrics.bj("\uff89\u2d2f\ua5dc\u0af9\ua6b4\u6bc3\ufb3c\u6722", 1592793015, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsInt() != 4096 || jsonObject.get(GpuUiFontMetrics.bj("\u9516\u6a75\u7277\u1862\uf859\ub253\u5c02\u73b8\ud0ff\u112a", -1324623592, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsInt() != 4096 || jsonObject.get(GpuUiFontMetrics.bj("\u889d\uc0bf\u4147\u83cf\u085e\u9a7c\u6ce9\u9fd6\u24cd\u3949\u1249\u3d19\ud4c9\u91db\u420e\u9172", 440619732, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat() != 32.0f || !GpuUiFontMetrics.bj("\u0f5e\u3d02\u342d\ufcee\u2b37\u79ce\ue11c\u88b1\u52df\ua7c5", 1102575132, 0x8FBCDBE9 ^ CryptoVault.mask()).equals(jsonObject.get(GpuUiFontMetrics.bj("\u3ff5\u232e\u23a0\u8e17\ueab3\u01c8\u48e4\ud084\u7713\ub7f0\u3fa4", -1237005103, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsString())) {
                throw new IllegalStateException(GpuUiFontMetrics.bj("\u8889\u0184\u0a8d\u08db\ue3fd\u0174\u83e6\u7721\u0823\u6484\u5ab9\u2c16\udc90\uf2ce\u93bd\ua3eb\uef0a\uacac\u6019\u9928\ue84c\u5d9c\u4af2\u1793\u16be\ua214\ucff7\u7cec\u9c87", 1532757064, 0x8FBCDBE9 ^ CryptoVault.mask()));
            }
            f2 = glyphArray.getAsJsonObject(GpuUiFontMetrics.bj("\u70d2\u98a5\u5fab\u2468\u546c\u479b\u64ec\u13a3\u0fae", -1829257346, 0x8FBCDBE9 ^ CryptoVault.mask())).get(GpuUiFontMetrics.bj("\ue841\u1f3b\u409e\ud9c8\u83e6\u30b2\uf78f\uc2eb\ud093\u1eaf\u8292\u3b91", 22827709, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat();
            JsonArray jsonArray = glyphArray.getAsJsonArray(GpuUiFontMetrics.bj("\ud7ae\uecfe\u6cba\u787a\uca9a\u755b\u5dc5\u2873", 707015205, 0x8FBCDBE9 ^ CryptoVault.mask()));
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                JsonObject jsonObject3 = jsonObject2.has(GpuUiFontMetrics.bj("\u9fa8\u08e7\ue5f0\u9615\u3cd5\ubd24\u177e\udd4e\u10e6\u7340\u1a08\u5b64\u7b9e\u122d\ud4ce", 1318729271, 0x8FBCDBE9 ^ CryptoVault.mask())) ? jsonObject2.getAsJsonObject(GpuUiFontMetrics.bj("\u3550\ueb47\u6cb8\u4a38\u0f6e\u4d88\u3459\ue94e\u2adf\u8da2\u8b0d\ua731\u7829\u6ffb\u7d27", 2017036803, 0x8FBCDBE9 ^ CryptoVault.mask())) : null;
                JsonObject jsonObject4 = jsonObject2.has(GpuUiFontMetrics.bj("\uc177\ua351\u1bec\u1037\uc058\u287e\ua61b\u2ac9\uf4a4\u4e33\u8ca8\ufe85\u9bc8\uddc6\u826b", 999939129, 0x8FBCDBE9 ^ CryptoVault.mask())) ? jsonObject2.getAsJsonObject(GpuUiFontMetrics.bj("\ua5eb\u5c36\uad8c\u15ca\u9098\uc549\u1f41\u317a\uf5f5\ufbbc\ue9fa\ud638\u4312", 332533851, 0x8FBCDBE9 ^ CryptoVault.mask())) : null;
                hashMap.put(jsonObject2.get(GpuUiFontMetrics.bj("\uac7a\u1843\ud03b\u2425\uc703\u2aff\ucfc9\u3427\u9c6d\u804c\u2bee", 421041312, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsInt(), new Glyph(jsonObject2.get(GpuUiFontMetrics.bj("\u0fa6\uaec0\u5021\u020d\u02e4\u1050\ubefd\uf7ae", 1634280847, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject3 == null ? 0.0f : jsonObject3.get(GpuUiFontMetrics.bj("\u345e\ua503\ua341\ud9a8\u1ff0", 953998525, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject3 == null ? 0.0f : jsonObject3.get(GpuUiFontMetrics.bj("\u71c8\u8f79\u7665\u0b71\ufb1e\uc321\u5882", 520928417, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject3 == null ? 0.0f : jsonObject3.get(GpuUiFontMetrics.bj("\u8cf1\u1092\ua379\u6f86\u117f\u1299", 24685863, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject3 == null ? 0.0f : jsonObject3.get(GpuUiFontMetrics.bj("\u3d50\u1a64\ucaa8\u1109\ue357", -836996839, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject4 == null ? 0.0f : jsonObject4.get(GpuUiFontMetrics.bj("\u6869\u1a47\uda25\uf0f3\u2970\u0089", -892635211, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject4 == null ? 0.0f : jsonObject4.get(GpuUiFontMetrics.bj("\u20a2\uf018\u6c5f\u41b4\ufc00\u1977\ua7a1\u5be4", -445188008, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject4 == null ? 0.0f : jsonObject4.get(GpuUiFontMetrics.bj("\ub85e\u5a11\u6a5f\u0546\ufc16\uffe2\ub6ec\u6147\u76ff", -304793070, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject4 == null ? 0.0f : jsonObject4.get(GpuUiFontMetrics.bj("\u539f\u4125\ubfd2\u4c4c\uf503\uaa62\u3ab4", -737336950, 0x8FBCDBE9 ^ CryptoVault.mask())).getAsFloat(), jsonObject3 != null && jsonObject4 != null));
            }
        }
        object = (Glyph)hashMap.get(72);
        float f3 = object != null && ((Glyph)object).hasBounds() && ((Glyph)object).top() + ((Glyph)object).bottom() > 0.3f ? ((Glyph)object).top() + ((Glyph)object).bottom() : 0.727f;
        glyphArray = new Glyph[128];
        for (int i2 = 0; i2 < 128; ++i2) {
            glyphArray[i2] = (Glyph)hashMap.get(i2);
        }
        return new Face(string, f2, f3, Map.copyOf(hashMap), glyphArray, class_2960.method_60655((String)GpuUiFontMetrics.bj("\u59c9\u983b\uf394\u89c5\u0c5d\u24f6\u46d0\u5646\u0f25\uf18f\ua4cc\udbbb\u82ba\u1899", 509107045, 0x8FBCDBE9 ^ CryptoVault.mask()), (String)(string + GpuUiFontMetrics.bj("\u4ec2\ub021\u6f50\u260d\ue8ab\ud4eb", 600722548, 0x8FBCDBE9 ^ CryptoVault.mask()))));
    }

    private static InputStream openMetrics(String string) {
        Object object;
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 != null && class_3102.method_1478() != null && ((Optional)(object = class_3102.method_1478().method_14486(class_2960.method_60655((String)GpuUiFontMetrics.bj("\ue4eb\u48a9\u0ad7\u7020\u498c\u24b2\u0215\u882e\ubb3a\uf348\u86f4\ua980\u2e2e\u7b21", 198766434, 0x8FBCDBE9 ^ CryptoVault.mask()), (String)(string + GpuUiFontMetrics.bj("\u88f5\ube26\ubb79\u8c46\u28f7\u2a06\u9f19", -1919301000, 0x8FBCDBE9 ^ CryptoVault.mask())))))).isPresent()) {
            return ((class_3298)((Optional)object).get()).method_14482();
        }
        object = GpuUiFontMetrics.bj("\u5eb6\uf7cc\u3e1f\u0ead\u17fd\uf4de\u0b8b\u88f7\udfb1\uf8a7\ueb50\u4512\u4939\uf159\u07d6\ub3d0\u430c\ua469\u77e2\u8304", -975713324, 0x8FBCDBE9 ^ CryptoVault.mask()) + string + GpuUiFontMetrics.bj("\u2917\ua42d\ucabf\u7acc\udd3a\ub84d\u5dda\u3e67", 469979962, 0x8FBCDBE9 ^ CryptoVault.mask());
        InputStream inputStream = GpuUiFontMetrics.class.getClassLoader().getResourceAsStream((String)object);
        if (inputStream == null) {
            throw new IOException(GpuUiFontMetrics.bj("\ue7a0\u9aa0\u285f\uaf83\ud31f\ubd97\uef7c\uba55\u1e62\u6ba0\ufe0c", -964172267, 0x8FBCDBE9 ^ CryptoVault.mask()) + (String)object);
        }
        return inputStream;
    }

    private static /* synthetic */ String bj(String string, int n2, int n3) {
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
    public static enum Family {
        DEFAULT,
        MINECRAFT,
        SERIF,
        SANS_SERIF;


        private static /* synthetic */ String hh(String string, int n2, int n3) {
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

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    public static interface GlyphSink {
        public void accept(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);
    }

    @Environment(value=EnvType.CLIENT)
    public record Face(String base, float ascender, float capHeight, Map<Integer, Glyph> glyphs, Glyph[] ascii, class_2960 texture) {
        public String atlasResource() {
            return Face.mi("\uc107\u6727\ub707\u32a7\ua522\uee7b\u4c58\u3f49\uc7b1\u1afb\u0996\uaf15\uf1af\u0fd1\u6776\ue204\u10d1\u80f2\ue188\u2d90\ua6bb\ub5f6", -1681203633, 0xA83614CF ^ CryptoVault.mask()) + this.base + Face.mi("\u989f\u64d4\u83ea\u98de\u75b4\u7e37", 1628300851, 0xA83614CF ^ CryptoVault.mask());
        }

        private static /* synthetic */ String mi(String string, int n2, int n3) {
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
    public record Glyph(float advance, float left, float bottom, float right, float top, float atlasLeft, float atlasBottom, float atlasRight, float atlasTop, boolean hasBounds) {
    }

    @Environment(value=EnvType.CLIENT)
    private record WidthEntry(float size, UiDrawCommand.TextWeight weight, float scale, Object faceSet, Object language, Object family, float width) {
    }
}

