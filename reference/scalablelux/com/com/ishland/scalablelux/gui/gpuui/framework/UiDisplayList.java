/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2960
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiMaterial;
import com.ishland.scalablelux.util.CryptoVault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;

@Environment(value=EnvType.CLIENT)
public final class UiDisplayList {
    private final List<UiDrawCommand> commands;

    private UiDisplayList(List<UiDrawCommand> list) {
        this.commands = List.copyOf(list);
    }

    public List<UiDrawCommand> commands() {
        return this.commands;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Builder {
        private final List<UiDrawCommand> commands = new ArrayList<UiDrawCommand>();

        private Builder() {
        }

        public Builder rect(float f2, float f3, float f4, float f5, float f6, int n2) {
            this.commands.add(new UiDrawCommand.Rect(f2, f3, f4, f5, f6, n2));
            return this;
        }

        public Builder circle(float f2, float f3, float f4, int n2) {
            return this.rect(f2 - f4, f3 - f4, f4 * 2.0f, f4 * 2.0f, f4, n2);
        }

        public Builder pill(float f2, float f3, float f4, float f5, int n2) {
            return this.rect(f2, f3, f4, f5, f5 * 0.5f, n2);
        }

        public Builder gradient(float f2, float f3, float f4, float f5, float f6, int n2, int n3, int n4, int n5) {
            this.commands.add(new UiDrawCommand.GradientRect(f2, f3, f4, f5, f6, n2, n3, n4, n5));
            return this;
        }

        public Builder text(float f2, float f3, float f4, int n2, String string) {
            return this.text(f2, f3, f4, n2, string, UiDrawCommand.TextWeight.REGULAR);
        }

        public Builder text(float f2, float f3, float f4, int n2, String string, UiDrawCommand.TextWeight textWeight) {
            this.commands.add(new UiDrawCommand.Text(f2, f3, f4, n2, string, textWeight));
            return this;
        }

        public Builder clipPush(float f2, float f3, float f4, float f5, float f6) {
            this.commands.add(new UiDrawCommand.ClipPush(f2, f3, f4, f5, f6));
            return this;
        }

        public Builder clipPop() {
            this.commands.add(new UiDrawCommand.ClipPop());
            return this;
        }

        public Builder effect(float f2, float f3, float f4, float f5, float f6, UiDrawCommand.EffectKind effectKind, float f7) {
            this.commands.add(new UiDrawCommand.Effect(f2, f3, f4, f5, f6, Objects.requireNonNull(effectKind, Builder.fj("\u71e1\ud005\uabdc\u66e5\u62ff\u2869", -1322627811, 0xCC5DA31C ^ CryptoVault.mask())), f7));
            return this;
        }

        public Builder glass(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.commands.add(new UiDrawCommand.Effect(f2, f3, f4, f5, f6, UiDrawCommand.EffectKind.GLASS, f7, n2));
            return this;
        }

        public Builder blur(float f2, float f3, float f4, float f5, float f6, float f7) {
            return this.effect(f2, f3, f4, f5, f6, UiDrawCommand.EffectKind.BLUR, f7);
        }

        public Builder shadow(float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2) {
            this.commands.add(new UiDrawCommand.Shadow(f2, f3, f4, f5, f6, f7, f8, n2));
            return this;
        }

        public Builder bloom(float f2, float f3, float f4, float f5, float f6, float f7) {
            return this.effect(f2, f3, f4, f5, f6, UiDrawCommand.EffectKind.BLOOM, f7);
        }

        public Builder border(float f2, float f3, float f4, float f5, float f6, float f7, int n2) {
            this.commands.add(new UiDrawCommand.Border(f2, f3, f4, f5, f6, f7, n2));
            return this;
        }

        public Builder image(float f2, float f3, float f4, float f5, float f6, int n2, class_2960 class_29602) {
            return this.image(f2, f3, f4, f5, f6, n2, class_29602, 0.0f, 0.0f, 1.0f, 1.0f);
        }

        public Builder image(float f2, float f3, float f4, float f5, float f6, int n2, class_2960 class_29602, float f7, float f8, float f9, float f10) {
            return this.image(f2, f3, f4, f5, f6, n2, class_29602, f7, f8, f9, f10, false);
        }

        public Builder imagePixelated(float f2, float f3, float f4, float f5, float f6, int n2, class_2960 class_29602, float f7, float f8, float f9, float f10) {
            return this.image(f2, f3, f4, f5, f6, n2, class_29602, f7, f8, f9, f10, true);
        }

        public Builder image(float f2, float f3, float f4, float f5, float f6, int n2, class_2960 class_29602, float f7, float f8, float f9, float f10, boolean bl) {
            this.commands.add(new UiDrawCommand.Image(f2, f3, f4, f5, f6, n2, Objects.requireNonNull(class_29602, Builder.fj("\u04c8\ubda7\u9b66\ubeb1\u60b7\u07fd\u82f5\uff04", -904218549, 0xCC5DA31C ^ CryptoVault.mask())), f7, f8, f9, f10, bl));
            return this;
        }

        public Builder material(float f2, float f3, float f4, float f5, float f6, UiMaterial uiMaterial) {
            this.commands.add(new UiDrawCommand.Material(f2, f3, f4, f5, f6, Objects.requireNonNull(uiMaterial, Builder.fj("\u838a\uc3d9\u941a\uceec\u8978\ude0d\uebba\u1d65\u67a0\u6f3b\u5882", -350637894, 0xCC5DA31C ^ CryptoVault.mask()))));
            return this;
        }

        public Builder fluid(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, int n2, int n3, int n4, int n5, float f11, float f12, boolean bl) {
            this.commands.add(new UiDrawCommand.Fluid(f2, f3, f4, f5, f6, f7, f8, f9, f10, n2, n3, n4, n5, f11, f12, bl));
            return this;
        }

        public UiDisplayList build() {
            int n2 = 0;
            for (UiDrawCommand uiDrawCommand : this.commands) {
                if (uiDrawCommand instanceof UiDrawCommand.ClipPush) {
                    ++n2;
                    continue;
                }
                if (!(uiDrawCommand instanceof UiDrawCommand.ClipPop) || --n2 >= 0) continue;
                throw new IllegalStateException(Builder.fj("\u517b\u9195\u3a52\u620f\u52b8\u6c80\u2395\uba86\u2fdc\u762b\u7578\ueec1\ub550\u53a4\u82b3\u5788\uf7b1\uea4a\ubad8\uf935\ufe58", -358444412, 0xCC5DA31C ^ CryptoVault.mask()));
            }
            if (n2 != 0) {
                throw new IllegalStateException(Builder.fj("\u727e\uda0e\ub70a\u8c0b\u4c4b\ucc15\ua827\u8fff\u5cc3\u1ca9\ub4a5\u106d\u2789\udac7\ua63e\ub639\u3926\u48ab\ud285\u44ac", 994673872, 0xCC5DA31C ^ CryptoVault.mask()));
            }
            return new UiDisplayList(this.commands);
        }

        private static /* synthetic */ String fj(String string, int n2, int n3) {
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

