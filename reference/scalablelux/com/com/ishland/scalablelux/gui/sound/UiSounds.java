/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  net.minecraft.class_3419
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.openal.AL10
 *  org.lwjgl.openal.ALC10
 */
package com.ishland.scalablelux.gui.sound;

import com.ishland.scalablelux.SlApp;
import com.ishland.scalablelux.module.BooleanSetting;
import com.ishland.scalablelux.module.Module;
import com.ishland.scalablelux.module.Setting;
import com.ishland.scalablelux.module.SliderSetting;
import com.ishland.scalablelux.util.CryptoVault;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_3419;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;

@Environment(value=EnvType.CLIENT)
public final class UiSounds {
    private static final int RATE = 44100;
    private static final int POOL = 6;
    private static final long HOVER_GAP_NS = 55000000L;
    private static final long SLIDER_GAP_NS = 28000000L;
    private static volatile Map<Event, short[]> clips;
    private static volatile String clipsPack;
    private static volatile boolean synthRunning;
    private static long alContext;
    private static String uploadedPack;
    private static final int[] buffers;
    private static final int[] sources;
    private static int nextSource;
    private static int failures;
    private static long retryAfterNs;
    private static long lastHoverNs;
    private static long lastSliderNs;
    private static long lastOtherNs;
    private static int pressDepth;
    private static Setting pressChanged;
    private static boolean[] pressModules;
    private static boolean pressPlayed;

    private UiSounds() {
    }

    private static boolean on(BooleanSetting booleanSetting) {
        return booleanSetting == null || booleanSetting.isEnabled();
    }

    private static boolean enabledFor(Event event) {
        if (HitboxConfigScreen752809.aI == null || !HitboxConfigScreen752809.aI.isEnabled()) {
            return false;
        }
        return switch (event.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> UiSounds.on(HitboxConfigScreen752809.aM);
            case 1 -> UiSounds.on(HitboxConfigScreen752809.aN);
            case 2, 3 -> UiSounds.on(HitboxConfigScreen752809.aO);
            case 4, 5 -> UiSounds.on(HitboxConfigScreen752809.aP);
            case 6 -> UiSounds.on(HitboxConfigScreen752809.aQ);
            case 7 -> UiSounds.on(HitboxConfigScreen752809.aR);
        };
    }

    private static String pack() {
        return HitboxConfigScreen752809.aJ == null ? UiSounds.jn("\u3510\u5af5\u20bf\u8ec6\uf99e\u567b\u3578", -649256894, 0x5E2B3BC8 ^ CryptoVault.mask()) : HitboxConfigScreen752809.aJ.getCurrentMode();
    }

    public static void prewarm() {
        try {
            if (HitboxConfigScreen752809.aI == null || !HitboxConfigScreen752809.aI.isEnabled()) {
                return;
            }
            UiSounds.ensureClips(UiSounds.pack());
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void play(Event event) {
        if (event == null) {
            return;
        }
        try {
            class_310 class_3102 = class_310.method_1551();
            if (class_3102 == null) {
                return;
            }
            if (!class_3102.method_18854()) {
                class_3102.execute(() -> UiSounds.play(event));
                return;
            }
            if (!UiSounds.enabledFor(event)) {
                return;
            }
            long l2 = System.nanoTime();
            if (event == Event.HOVER) {
                if (l2 - lastHoverNs < 55000000L || l2 - lastOtherNs < 55000000L) {
                    return;
                }
                lastHoverNs = l2;
            } else if (event == Event.SLIDER) {
                if (l2 - lastSliderNs < 28000000L) {
                    return;
                }
                lastSliderNs = l2;
            } else {
                lastOtherNs = l2;
            }
            if (pressDepth > 0 && event != Event.HOVER) {
                pressPlayed = true;
            }
            UiSounds.emit(class_3102, event);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void beginPress() {
        try {
            if (pressDepth++ > 0) {
                return;
            }
            pressChanged = null;
            pressPlayed = false;
            pressModules = null;
            if (HitboxConfigScreen752809.aI == null || !HitboxConfigScreen752809.aI.isEnabled()) {
                return;
            }
            List<Module> list = UiSounds.modules();
            if (list != null) {
                boolean[] blArray = new boolean[list.size()];
                for (int i2 = 0; i2 < blArray.length; ++i2) {
                    blArray[i2] = list.get(i2).isEnabled();
                }
                pressModules = blArray;
            }
            Setting.fy = UiSounds::onSettingChanged;
        }
        catch (Throwable throwable) {
            Setting.fy = null;
        }
    }

    public static void endPress(boolean bl) {
        block13: {
            if (pressDepth <= 0) {
                return;
            }
            if (--pressDepth > 0) {
                return;
            }
            Setting.fy = null;
            if (!pressPlayed) break block13;
            pressChanged = null;
            pressModules = null;
            pressPlayed = false;
            return;
        }
        try {
            Event event = null;
            List<Module> list = UiSounds.modules();
            boolean[] blArray = pressModules;
            if (blArray != null && list != null && list.size() == blArray.length) {
                for (int i2 = 0; i2 < blArray.length; ++i2) {
                    boolean bl2 = list.get(i2).isEnabled();
                    if (bl2 == blArray[i2]) continue;
                    event = bl2 ? Event.TOGGLE_ON : Event.TOGGLE_OFF;
                    break;
                }
            }
            Setting setting = pressChanged;
            if (event == null && setting instanceof BooleanSetting) {
                BooleanSetting booleanSetting = (BooleanSetting)setting;
                event = booleanSetting.isEnabled() ? Event.TOGGLE_ON : Event.TOGGLE_OFF;
            } else if (event == null && setting instanceof SliderSetting) {
                event = Event.SLIDER;
            }
            if (event == null && (bl || setting != null)) {
                event = Event.CLICK;
            }
            if (event != null) {
                UiSounds.play(event);
            }
        }
        catch (Throwable throwable) {}
        finally {
            pressChanged = null;
            pressModules = null;
            pressPlayed = false;
        }
    }

    private static void onSettingChanged(Setting setting) {
        class_310 class_3102 = class_310.method_1551();
        if (class_3102 == null || !class_3102.method_18854() || pressDepth <= 0) {
            return;
        }
        if (pressChanged == null || !(pressChanged instanceof BooleanSetting) && setting instanceof BooleanSetting) {
            pressChanged = setting;
        }
    }

    private static List<Module> modules() {
        SlApp slApp = SlApp.getInstance();
        if (slApp == null || slApp.getModuleManager() == null) {
            return null;
        }
        return slApp.getModuleManager().getModules();
    }

    private static void emit(class_310 class_3102, Event event) {
        float f2;
        float f3;
        long l2 = System.nanoTime();
        if (failures >= 5 || l2 < retryAfterNs) {
            return;
        }
        String string = UiSounds.pack();
        Map<Event, short[]> map = clips;
        if (map == null || !string.equals(clipsPack)) {
            UiSounds.ensureClips(string);
            return;
        }
        float f4 = 1.0f;
        if (class_3102.field_1690 != null) {
            f4 = class_3102.field_1690.method_1630(class_3419.field_61058);
        }
        if ((f3 = Math.max(0.0f, Math.min(1.0f, (f2 = HitboxConfigScreen752809.aK == null ? 0.6f : (float)HitboxConfigScreen752809.aK.B() / 100.0f) * f4))) <= 0.001f) {
            return;
        }
        try {
            if (!UiSounds.ensureAl(string, map)) {
                return;
            }
            int n2 = buffers[event.ordinal()];
            if (n2 == 0) {
                return;
            }
            int n3 = UiSounds.pickSource();
            if (n3 == 0) {
                return;
            }
            float f5 = HitboxConfigScreen752809.aL == null ? 0.08f : (float)HitboxConfigScreen752809.aL.B() / 100.0f;
            float f6 = 1.0f + (f5 > 0.0f ? (float)ThreadLocalRandom.current().nextDouble(-f5, f5) : 0.0f);
            AL10.alSourceStop((int)n3);
            AL10.alSourcei((int)n3, (int)4105, (int)n2);
            AL10.alSourcef((int)n3, (int)4106, (float)f3);
            AL10.alSourcef((int)n3, (int)4099, (float)Math.max(0.5f, Math.min(2.0f, f6)));
            AL10.alSourcePlay((int)n3);
        }
        catch (Throwable throwable) {
            UiSounds.noteFailure(l2);
        }
    }

    private static void noteFailure(long l2) {
        ++failures;
        retryAfterNs = l2 + 5000000000L;
        alContext = 0L;
    }

    private static boolean ensureAl(String string, Map<Event, short[]> map) {
        long l2 = ALC10.alcGetCurrentContext();
        if (l2 == 0L) {
            return false;
        }
        if (l2 != alContext) {
            int n2;
            Arrays.fill(buffers, 0);
            Arrays.fill(sources, 0);
            uploadedPack = null;
            for (int i2 = 0; i2 < 6 && (n2 = AL10.alGenSources()) != 0 && AL10.alIsSource((int)n2); ++i2) {
                UiSounds.sources[i2] = n2;
                AL10.alSourcei((int)n2, (int)514, (int)1);
                AL10.alSource3f((int)n2, (int)4100, (float)0.0f, (float)0.0f, (float)0.0f);
                AL10.alSourcef((int)n2, (int)4129, (float)0.0f);
                AL10.alSourcei((int)n2, (int)4103, (int)0);
            }
            alContext = l2;
        }
        if (!string.equals(uploadedPack)) {
            for (int n3 : sources) {
                if (n3 == 0) continue;
                AL10.alSourceStop((int)n3);
                AL10.alSourcei((int)n3, (int)4105, (int)0);
            }
            for (int i3 = 0; i3 < buffers.length; ++i3) {
                if (buffers[i3] == 0) continue;
                AL10.alDeleteBuffers((int)buffers[i3]);
                UiSounds.buffers[i3] = 0;
            }
            for (Event event : Event.values()) {
                short[] sArray = map.get((Object)event);
                if (sArray == null || sArray.length == 0) continue;
                int n4 = AL10.alGenBuffers();
                ShortBuffer shortBuffer = BufferUtils.createShortBuffer((int)sArray.length);
                shortBuffer.put(sArray).flip();
                if (n4 == 0 || !AL10.alIsBuffer((int)n4)) continue;
                AL10.alBufferData((int)n4, (int)4353, (ShortBuffer)shortBuffer, (int)44100);
                UiSounds.buffers[event.ordinal()] = n4;
            }
            uploadedPack = string;
        }
        return true;
    }

    private static int pickSource() {
        int n2;
        for (n2 = 0; n2 < 6; ++n2) {
            int n3 = (nextSource + n2) % 6;
            int n4 = sources[n3];
            if (n4 == 0 || AL10.alGetSourcei((int)n4, (int)4112) == 4114) continue;
            nextSource = (n3 + 1) % 6;
            return n4;
        }
        n2 = sources[nextSource];
        nextSource = (nextSource + 1) % 6;
        return n2;
    }

    private static void ensureClips(String string) {
        if (string.equals(clipsPack) && clips != null) {
            return;
        }
        if (synthRunning) {
            return;
        }
        synthRunning = true;
        Thread thread = new Thread(() -> {
            try {
                EnumMap<Event, short[]> enumMap = new EnumMap<Event, short[]>(Event.class);
                for (Event event : Event.values()) {
                    enumMap.put(event, UiSounds.synth(string, event));
                }
                clips = enumMap;
                clipsPack = string;
            }
            finally {
                synthRunning = false;
            }
        }, UiSounds.jn("\u345f\u9fe4\u8c74\u7c33\ub76a\u42ce\ue0fa\ua190\u5342\ue82d\ubde8\uae75\u2cb9\u41e7\udcd5\u2a74\ue8ad", -1194319841, 0x5E2B3BC8 ^ CryptoVault.mask()));
        thread.setDaemon(true);
        thread.start();
    }

    /*
     * Exception decompiling
     */
    static short[] synth(String var0, Event var1_1) {
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

    private static short[] creamy(Event event) {
        Clip clip = new Clip(event == Event.OPEN || event == Event.CLOSE ? 190 : 110);
        switch (event.ordinal()) {
            case 0: {
                clip.tone(0.0f, 640.0f, 520.0f, 7.0f, 0.5f);
                clip.noise(0.0f, 2600.0f, 0.0f, 3.0f, 0.25f);
                return clip.done(0.16f);
            }
            case 1: {
                UiSounds.thock(clip, 0.0f, 250.0f, 1.0f);
                return clip.done(0.55f);
            }
            case 2: {
                UiSounds.thock(clip, 0.0f, 220.0f, 0.9f);
                UiSounds.thock(clip, 42.0f, 330.0f, 1.0f);
                return clip.done(0.55f);
            }
            case 3: {
                UiSounds.thock(clip, 0.0f, 330.0f, 1.0f);
                UiSounds.thock(clip, 42.0f, 205.0f, 0.9f);
                return clip.done(0.52f);
            }
            case 4: {
                clip.sweep(0.0f, 300.0f, 620.0f, 26.0f, 130.0f, 0.7f);
                UiSounds.thock(clip, 0.0f, 240.0f, 0.6f);
                return clip.done(0.42f);
            }
            case 5: {
                clip.sweep(0.0f, 560.0f, 260.0f, 18.0f, 120.0f, 0.7f);
                UiSounds.thock(clip, 70.0f, 210.0f, 0.5f);
                return clip.done(0.4f);
            }
            case 6: {
                clip.tone(0.0f, 980.0f, 900.0f, 4.0f, 0.6f);
                clip.noise(0.0f, 3200.0f, 0.0f, 2.0f, 0.3f);
                return clip.done(0.13f);
            }
            case 7: {
                UiSounds.thock(clip, 0.0f, 260.0f, 0.9f);
                UiSounds.thock(clip, 60.0f, 390.0f, 1.0f);
                return clip.done(0.55f);
            }
        }
        return clip.done(0.5f);
    }

    private static void thock(Clip clip, float f2, float f3, float f4) {
        clip.tone(f2, f3, f3 * 0.6f, 24.0f, f4);
        clip.tone(f2, f3 * 2.02f, f3 * 1.4f, 9.0f, f4 * 0.25f);
        clip.noise(f2, 1700.0f, 0.0f, 9.0f, f4 * 0.42f);
    }

    private static short[] clacky(Event event) {
        Clip clip = new Clip(event == Event.OPEN || event == Event.CLOSE ? 160 : 90);
        switch (event.ordinal()) {
            case 0: {
                clip.noise(0.0f, 9000.0f, 3600.0f, 2.0f, 0.6f);
                clip.tone(0.0f, 3300.0f, 3100.0f, 2.5f, 0.3f);
                return clip.done(0.14f);
            }
            case 1: {
                UiSounds.clack(clip, 0.0f, 1.0f);
                UiSounds.clack(clip, 30.0f, 0.5f);
                return clip.done(0.5f);
            }
            case 2: {
                UiSounds.clack(clip, 0.0f, 1.0f);
                clip.tone(22.0f, 1500.0f, 1500.0f, 18.0f, 0.35f);
                clip.tone(48.0f, 2000.0f, 2000.0f, 20.0f, 0.35f);
                return clip.done(0.5f);
            }
            case 3: {
                UiSounds.clack(clip, 0.0f, 1.0f);
                clip.tone(22.0f, 2000.0f, 2000.0f, 18.0f, 0.35f);
                clip.tone(48.0f, 1500.0f, 1500.0f, 20.0f, 0.35f);
                return clip.done(0.48f);
            }
            case 4: {
                for (int i2 = 0; i2 < 3; ++i2) {
                    UiSounds.clack(clip, i2 * 34, 0.6f + (float)i2 * 0.2f);
                    clip.tone(i2 * 34, 1300 + i2 * 380, 1300 + i2 * 380, 14.0f, 0.28f);
                }
                return clip.done(0.42f);
            }
            case 5: {
                for (int i3 = 0; i3 < 3; ++i3) {
                    UiSounds.clack(clip, i3 * 34, 1.0f - (float)i3 * 0.2f);
                    clip.tone(i3 * 34, 2060 - i3 * 380, 2060 - i3 * 380, 14.0f, 0.28f);
                }
                return clip.done(0.4f);
            }
            case 6: {
                clip.noise(0.0f, 10000.0f, 4200.0f, 1.4f, 0.7f);
                return clip.done(0.12f);
            }
            case 7: {
                UiSounds.clack(clip, 0.0f, 1.0f);
                UiSounds.clack(clip, 55.0f, 0.9f);
                clip.tone(55.0f, 2400.0f, 2400.0f, 16.0f, 0.25f);
                return clip.done(0.5f);
            }
        }
        return clip.done(0.5f);
    }

    private static void clack(Clip clip, float f2, float f3) {
        clip.noise(f2, 8000.0f, 2200.0f, 4.5f, f3);
        clip.tone(f2, 2600.0f, 2300.0f, 5.0f, f3 * 0.35f);
        clip.tone(f2, 620.0f, 480.0f, 6.0f, f3 * 0.2f);
    }

    private static short[] soft(Event event) {
        Clip clip = new Clip(event == Event.OPEN || event == Event.CLOSE ? 220 : 120);
        switch (event.ordinal()) {
            case 0: {
                clip.tone(0.0f, 880.0f, 860.0f, 10.0f, 0.5f, 1.5f);
                return clip.done(0.1f);
            }
            case 1: {
                clip.tone(0.0f, 720.0f, 640.0f, 18.0f, 1.0f, 1.2f);
                return clip.done(0.38f);
            }
            case 2: {
                clip.tone(0.0f, 600.0f, 600.0f, 22.0f, 1.0f, 1.5f);
                clip.tone(50.0f, 900.0f, 900.0f, 30.0f, 1.0f, 1.5f);
                return clip.done(0.38f);
            }
            case 3: {
                clip.tone(0.0f, 900.0f, 900.0f, 22.0f, 1.0f, 1.5f);
                clip.tone(50.0f, 600.0f, 600.0f, 30.0f, 1.0f, 1.5f);
                return clip.done(0.36f);
            }
            case 4: {
                clip.sweep(0.0f, 420.0f, 840.0f, 40.0f, 150.0f, 1.0f);
                return clip.done(0.3f);
            }
            case 5: {
                clip.sweep(0.0f, 800.0f, 400.0f, 30.0f, 140.0f, 1.0f);
                return clip.done(0.28f);
            }
            case 6: {
                clip.tone(0.0f, 1250.0f, 1250.0f, 5.0f, 1.0f, 1.0f);
                return clip.done(0.09f);
            }
            case 7: {
                clip.tone(0.0f, 660.0f, 660.0f, 24.0f, 1.0f, 1.5f);
                clip.tone(60.0f, 990.0f, 990.0f, 34.0f, 1.0f, 1.5f);
                return clip.done(0.38f);
            }
        }
        return clip.done(0.35f);
    }

    static {
        buffers = new int[Event.values().length];
        sources = new int[6];
    }

    private static /* synthetic */ String jn(String string, int n2, int n3) {
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
    public static enum Event {
        HOVER,
        CLICK,
        TOGGLE_ON,
        TOGGLE_OFF,
        OPEN,
        CLOSE,
        SLIDER,
        KEYBIND;


        private static /* synthetic */ String ij(String string, int n2, int n3) {
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
    private static final class Clip {
        private final float[] mix;
        private long seed = -7046029254386353131L;

        Clip(int n2) {
            this.mix = new float[Math.max(1, n2 * 44100 / 1000)];
        }

        void tone(float f2, float f3, float f4, float f5, float f6) {
            this.tone(f2, f3, f4, f5, f6, 0.4f);
        }

        void tone(float f2, float f3, float f4, float f5, float f6, float f7) {
            int n2 = (int)(f2 * 44100.0f / 1000.0f);
            float f8 = Math.max(0.3f, f5) * 44100.0f / 1000.0f;
            float f9 = Math.max(1.0f, f7 * 44100.0f / 1000.0f);
            int n3 = (int)Math.min((float)(this.mix.length - n2), f8 * 7.0f);
            double d2 = 0.0;
            for (int i2 = 0; i2 < n3; ++i2) {
                float f10 = (float)Math.exp((float)(-i2) / (f8 * 1.5f));
                double d3 = f4 + (f3 - f4) * f10;
                float f11 = (float)Math.exp((float)(-i2) / f8) * Math.min(1.0f, (float)i2 / f9);
                int n4 = n2 + i2;
                this.mix[n4] = this.mix[n4] + (float)Math.sin(d2 += Math.PI * 2 * d3 / 44100.0) * f11 * f6;
            }
        }

        void sweep(float f2, float f3, float f4, float f5, float f6, float f7) {
            int n2 = (int)(f2 * 44100.0f / 1000.0f);
            int n3 = (int)Math.min((float)(this.mix.length - n2), f6 * 44100.0f / 1000.0f);
            float f8 = Math.max(1.0f, f5 * 44100.0f / 1000.0f);
            double d2 = 0.0;
            for (int i2 = 0; i2 < n3; ++i2) {
                float f9 = (float)i2 / (float)n3;
                double d3 = (double)f3 * Math.pow(f4 / f3, f9);
                float f10 = Math.min(1.0f, (float)i2 / f8) * (1.0f - f9) * (1.0f - f9);
                int n4 = n2 + i2;
                this.mix[n4] = this.mix[n4] + (float)(Math.sin(d2 += Math.PI * 2 * d3 / 44100.0) + 0.18 * Math.sin(d2 * 2.0)) * f10 * f7;
            }
        }

        void noise(float f2, float f3, float f4, float f5, float f6) {
            int n2 = (int)(f2 * 44100.0f / 1000.0f);
            float f7 = Math.max(0.3f, f5) * 44100.0f / 1000.0f;
            int n3 = (int)Math.min((float)(this.mix.length - n2), f7 * 7.0f);
            float f8 = 1.0f - (float)Math.exp(Math.PI * -2 * (double)f3 / 44100.0);
            float f9 = f4 > 0.0f ? 1.0f - (float)Math.exp(Math.PI * -2 * (double)f4 / 44100.0) : 0.0f;
            float f10 = 0.0f;
            float f11 = 0.0f;
            for (int i2 = 0; i2 < n3; ++i2) {
                this.seed ^= this.seed << 13;
                this.seed ^= this.seed >>> 7;
                this.seed ^= this.seed << 17;
                float f12 = (float)(this.seed >>> 40) / 1.6777216E7f * 2.0f - 1.0f;
                f10 += f8 * (f12 - f10);
                float f13 = f10;
                if (f9 > 0.0f) {
                    f11 += f9 * (f13 - f11);
                    f13 -= f11;
                }
                int n4 = n2 + i2;
                this.mix[n4] = this.mix[n4] + f13 * (float)Math.exp((float)(-i2) / f7) * f6;
            }
        }

        short[] done(float f2) {
            float f3 = 0.0f;
            for (float f4 : this.mix) {
                f3 = Math.max(f3, Math.abs(f4));
            }
            float f5 = f3 > 0.0f ? f2 / f3 : 0.0f;
            int n2 = Math.min(this.mix.length, 88);
            short[] sArray = new short[this.mix.length];
            for (int i2 = 0; i2 < this.mix.length; ++i2) {
                float f6 = this.mix[i2] * f5;
                int n3 = this.mix.length - 1 - i2;
                if (n3 < n2) {
                    f6 *= (float)n3 / (float)n2;
                }
                sArray[i2] = (short)Math.round(Math.max(-1.0f, Math.min(1.0f, f6)) * 32767.0f);
            }
            return sArray;
        }
    }
}

