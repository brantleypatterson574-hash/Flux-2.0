/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextTarget;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import java.util.function.IntPredicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiTextField
implements UiTextTarget {
    private String placeholder;
    private String value = "";
    private int caret;
    private int anchor = -1;
    private float scrollX;
    private int maxLength = 64;
    private IntPredicate allowed = n2 -> true;
    private Runnable onSubmit;
    private Runnable onChange;
    private Runnable onBlur;
    private UiHost lastHost;
    private long focusedAtNanos;
    private boolean bare;
    private float padLeft = 12.0f;
    private float padRight = 12.0f;
    private float fontSize = 11.0f;

    public UiTextField(String string) {
        this.placeholder = string == null ? "" : string;
    }

    public UiTextField maxLength(int n2) {
        this.maxLength = Math.max(1, n2);
        return this;
    }

    public UiTextField allow(IntPredicate intPredicate) {
        this.allowed = intPredicate;
        return this;
    }

    public UiTextField onSubmit(Runnable runnable) {
        this.onSubmit = runnable;
        return this;
    }

    public UiTextField onChange(Runnable runnable) {
        this.onChange = runnable;
        return this;
    }

    public UiTextField onBlur(Runnable runnable) {
        this.onBlur = runnable;
        return this;
    }

    public UiTextField bare(boolean bl) {
        this.bare = bl;
        return this;
    }

    public UiTextField padding(float f2, float f3) {
        this.padLeft = f2;
        this.padRight = f3;
        return this;
    }

    public UiTextField fontSize(float f2) {
        this.fontSize = f2;
        return this;
    }

    public UiTextField placeholder(String string) {
        this.placeholder = string == null ? "" : string;
        return this;
    }

    public String value() {
        return this.value;
    }

    public void setValue(String string) {
        this.value = string == null ? "" : string.substring(0, Math.min(string.length(), this.maxLength));
        this.caret = this.value.length();
        this.anchor = -1;
        this.scrollX = 0.0f;
    }

    public void clear() {
        if (this.value.isEmpty()) {
            return;
        }
        this.setValue("");
        this.changed();
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public boolean focused(UiHost uiHost) {
        return uiHost.focused() == this;
    }

    public void selectAll() {
        this.anchor = 0;
        this.caret = this.value.length();
    }

    private boolean hasSelection() {
        return this.anchor >= 0 && this.anchor != this.caret;
    }

    private int selStart() {
        return this.hasSelection() ? Math.min(this.anchor, this.caret) : this.caret;
    }

    private int selEnd() {
        return this.hasSelection() ? Math.max(this.anchor, this.caret) : this.caret;
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        float f8;
        float f9;
        boolean bl;
        this.lastHost = uiHost;
        boolean bl2 = bl = uiHost.focused() == this;
        if (!this.bare) {
            f9 = Math.min(f5 * 0.5f, (float)Math.round(12.0f * f6 * uiHost.cornerScale()));
            builder.rect(f2, f3, f4, f5, f9, uiHost.controlFill(bl ? 38 : 20));
            int n2 = uiHost.accent();
            builder.border(f2, f3, f4, f5, f9, 1.0f, bl ? n2 & 0xFFFFFF | 0xA0000000 : uiHost.lineColor());
        }
        f9 = this.fontSize * f6;
        float f10 = Math.round(this.padLeft * f6);
        float f11 = Math.max(1.0f, f4 - f10 - (float)Math.round(this.padRight * f6));
        float f12 = GpuUiFontMetrics.topInBox(f3, f5, f9);
        this.caret = Math.max(0, Math.min(this.caret, this.value.length()));
        if (this.anchor > this.value.length()) {
            this.anchor = -1;
        }
        if (this.value.isEmpty()) {
            builder.text(f2 + f10, f12, f9, uiHost.muted2Color(), UiKit.fit(this.placeholder, f11, f9));
        } else {
            f8 = GpuUiFontMetrics.textWidth(this.value.substring(0, this.caret), f9);
            if (f8 - this.scrollX > f11 - 2.0f) {
                this.scrollX = f8 - f11 + 2.0f;
            }
            if (f8 - this.scrollX < 0.0f) {
                this.scrollX = f8;
            }
            if (GpuUiFontMetrics.textWidth(this.value, f9) <= f11) {
                this.scrollX = 0.0f;
            }
            builder.clipPush(f2 + f10, f3, f11, f5, 0.0f);
            if (bl && this.hasSelection()) {
                f7 = GpuUiFontMetrics.textWidth(this.value.substring(0, this.selStart()), f9) - this.scrollX;
                float f13 = GpuUiFontMetrics.textWidth(this.value.substring(0, this.selEnd()), f9) - this.scrollX;
                float f14 = Math.round(f9 * 1.35f);
                builder.rect(f2 + f10 + f7, Math.round(f3 + (f5 - f14) * 0.5f), Math.max(1.0f, f13 - f7), f14, Math.max(1.0f, 2.0f * f6), uiHost.accent() & 0xFFFFFF | 0x55000000);
            }
            builder.text(f2 + f10 - this.scrollX, f12, f9, uiHost.textColor(), this.value);
            builder.clipPop();
        }
        if (bl && !this.hasSelection() && (uiHost.now() - this.focusedAtNanos) / 530000000L % 2L == 0L) {
            f8 = GpuUiFontMetrics.textWidth(this.value.substring(0, this.caret), f9) - this.scrollX;
            f7 = Math.round(f9 * 1.15f);
            builder.rect(f2 + f10 + (float)Math.round(f8), Math.round(f3 + (f5 - f7) * 0.5f), 1.0f, f7, 0.0f, uiHost.textColor());
        }
        uiHost.hit(f2, f3, f4, f5, () -> {
            if (uiHost.focused() != this) {
                uiHost.focus(this);
            }
            this.caret = this.caretAt((float)uiHost.pressX() - (f2 + f10) + this.scrollX, f9);
            this.anchor = -1;
            this.focusedAtNanos = uiHost.now();
        });
    }

    private int caretAt(float f2, float f3) {
        int n2 = 0;
        float f4 = Float.MAX_VALUE;
        for (int i2 = 0; i2 <= this.value.length(); ++i2) {
            float f5 = Math.abs(GpuUiFontMetrics.textWidth(this.value.substring(0, i2), f3) - f2);
            if (!(f5 < f4)) continue;
            f4 = f5;
            n2 = i2;
        }
        return n2;
    }

    @Override
    public boolean charTyped(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        string.codePoints().filter(n2 -> !Character.isISOControl(n2) && this.allowed.test(n2)).forEach(stringBuilder::appendCodePoint);
        if (stringBuilder.length() == 0) {
            return true;
        }
        this.insert(stringBuilder.toString());
        return true;
    }

    private void insert(String string) {
        boolean bl = this.deleteSelection();
        int n2 = this.maxLength - this.value.length();
        if (n2 <= 0) {
            if (bl) {
                this.changed();
            }
            return;
        }
        if (string.length() > n2) {
            string = string.substring(0, n2);
        }
        this.value = this.value.substring(0, this.caret) + string + this.value.substring(this.caret);
        this.caret += string.length();
        this.anchor = -1;
        this.changed();
    }

    private boolean deleteSelection() {
        if (!this.hasSelection()) {
            this.anchor = -1;
            return false;
        }
        int n2 = this.selStart();
        int n3 = this.selEnd();
        this.value = this.value.substring(0, n2) + this.value.substring(n3);
        this.caret = n2;
        this.anchor = -1;
        return true;
    }

    private int prevIndex(int n2) {
        return n2 <= 0 ? 0 : n2 - Character.charCount(this.value.codePointBefore(n2));
    }

    private int nextIndex(int n2) {
        return n2 >= this.value.length() ? this.value.length() : n2 + Character.charCount(this.value.codePointAt(n2));
    }

    private void resetBlink() {
        if (this.lastHost != null) {
            this.focusedAtNanos = this.lastHost.now();
        }
    }

    private void moveCaret(int n2, boolean bl) {
        if (bl) {
            if (this.anchor < 0) {
                this.anchor = this.caret;
            }
        } else {
            this.anchor = -1;
        }
        this.caret = Math.max(0, Math.min(this.value.length(), n2));
        this.resetBlink();
    }

    @Override
    public boolean keyPressed(int n2, int n3) {
        boolean bl = (n3 & 0xA) != 0;
        boolean bl2 = (n3 & 1) != 0;
        switch (n2) {
            case 259: {
                if (this.deleteSelection()) {
                    this.changed();
                } else if (this.caret > 0) {
                    int n4 = bl ? this.wordStart(this.caret) : this.prevIndex(this.caret);
                    this.value = this.value.substring(0, n4) + this.value.substring(this.caret);
                    this.caret = n4;
                    this.changed();
                }
                return true;
            }
            case 261: {
                if (this.deleteSelection()) {
                    this.changed();
                } else if (this.caret < this.value.length()) {
                    int n5 = bl ? this.wordEnd(this.caret) : this.nextIndex(this.caret);
                    this.value = this.value.substring(0, this.caret) + this.value.substring(n5);
                    this.changed();
                }
                return true;
            }
            case 263: {
                if (!bl2 && this.hasSelection()) {
                    this.moveCaret(this.selStart(), false);
                } else {
                    this.moveCaret(bl ? this.wordStart(this.caret) : this.prevIndex(this.caret), bl2);
                }
                return true;
            }
            case 262: {
                if (!bl2 && this.hasSelection()) {
                    this.moveCaret(this.selEnd(), false);
                } else {
                    this.moveCaret(bl ? this.wordEnd(this.caret) : this.nextIndex(this.caret), bl2);
                }
                return true;
            }
            case 268: {
                this.moveCaret(0, bl2);
                return true;
            }
            case 269: {
                this.moveCaret(this.value.length(), bl2);
                return true;
            }
            case 257: 
            case 335: {
                if (this.onSubmit != null) {
                    this.onSubmit.run();
                }
                return true;
            }
            case 86: {
                if (!bl || this.lastHost == null) break;
                String string = this.lastHost.clipboard();
                if (string != null) {
                    this.charTyped(string.replace('\n', ' ').replace('\r', ' ').replace('\t', ' '));
                }
                return true;
            }
            case 67: {
                if (!bl || this.lastHost == null) break;
                this.lastHost.setClipboard(this.hasSelection() ? this.value.substring(this.selStart(), this.selEnd()) : this.value);
                return true;
            }
            case 88: {
                if (!bl || this.lastHost == null) break;
                if (this.hasSelection()) {
                    this.lastHost.setClipboard(this.value.substring(this.selStart(), this.selEnd()));
                    this.deleteSelection();
                    this.changed();
                }
                return true;
            }
            case 65: {
                if (!bl) break;
                this.selectAll();
                return true;
            }
        }
        return n2 != 256;
    }

    private int wordStart(int n2) {
        int n3;
        for (n3 = n2; n3 > 0 && this.value.charAt(n3 - 1) == ' '; --n3) {
        }
        while (n3 > 0 && this.value.charAt(n3 - 1) != ' ') {
            --n3;
        }
        return n3;
    }

    private int wordEnd(int n2) {
        int n3;
        int n4 = this.value.length();
        for (n3 = n2; n3 < n4 && this.value.charAt(n3) == ' '; ++n3) {
        }
        while (n3 < n4 && this.value.charAt(n3) != ' ') {
            ++n3;
        }
        return n3;
    }

    private void changed() {
        this.resetBlink();
        if (this.onChange != null) {
            this.onChange.run();
        }
    }

    @Override
    public void onBlur() {
        this.anchor = -1;
        if (this.onBlur != null) {
            this.onBlur.run();
        }
    }

    @Override
    public void onFocus() {
        this.caret = this.value.length();
        this.anchor = -1;
        if (this.lastHost != null) {
            this.focusedAtNanos = this.lastHost.now();
        }
    }

    public static boolean isDigit(int n2) {
        return n2 >= 48 && n2 <= 57;
    }

    public static boolean isHexChar(int n2) {
        return n2 >= 48 && n2 <= 57 || n2 >= 97 && n2 <= 102 || n2 >= 65 && n2 <= 70 || n2 == 35;
    }

    public static boolean isItemIdChar(int n2) {
        return Character.isLetterOrDigit(n2) || n2 == 58 || n2 == 95 || n2 == 47 || n2 == 46 || n2 == 45;
    }

    public static boolean isNameChar(int n2) {
        return Character.isLetterOrDigit(n2) || n2 == 32 || n2 == 95 || n2 == 45 || n2 == 46 || n2 == 35;
    }
}

