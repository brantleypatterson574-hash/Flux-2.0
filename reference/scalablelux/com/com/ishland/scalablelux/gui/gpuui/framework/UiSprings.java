/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.framework;

import java.util.HashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UiSprings {
    private static final long PRUNE_FRAMES = 240L;
    private static final float SETTLE_POS = 0.0015f;
    private static final float SETTLE_VEL = 0.01f;
    private final HashMap<Object, State> states = new HashMap();
    private long frame;

    public float ease(Object object, float f2, float f3, float f4) {
        State state = this.state(object, f2);
        if (state.pos == f2 && state.vel == 0.0f) {
            return f2;
        }
        float f5 = Math.max(0.01f, f3 * 1.6f);
        float f6 = Math.max(0.0f, Math.min(0.25f, f4));
        float f7 = state.pos - f2;
        float f8 = state.vel + f5 * f7;
        float f9 = (float)Math.exp(-f5 * f6);
        float f10 = (f7 + f8 * f6) * f9;
        float f11 = (state.vel - f5 * f8 * f6) * f9;
        UiSprings.settle(state, f2, f10, f11);
        return state.pos;
    }

    public float spring(Object object, float f2, float f3, float f4, float f5) {
        float f6;
        State state = this.state(object, f2);
        float f7 = state.pos;
        float f8 = state.vel;
        for (float f9 = Math.max(0.0f, Math.min(0.25f, f5)); f9 > 0.0f; f9 -= f6) {
            f6 = Math.min(f9, 0.004166667f);
            f8 += ((f2 - f7) * f3 - f8 * f4) * f6;
            f7 += f8 * f6;
        }
        UiSprings.settle(state, f2, f7 - f2, f8);
        return state.pos;
    }

    public void seed(Object object, float f2) {
        State state = this.states.get(object);
        if (state == null) {
            state = new State(f2);
            this.states.put(object, state);
        }
        state.touched = this.frame;
    }

    public void set(Object object, float f2) {
        State state = this.state(object, f2);
        state.pos = f2;
        state.vel = 0.0f;
    }

    public void remove(Object object) {
        this.states.remove(object);
    }

    public void clear() {
        this.states.clear();
    }

    public void endFrame() {
        if (++this.frame % 240L != 0L) {
            return;
        }
        long l2 = this.frame - 240L;
        this.states.values().removeIf(state -> state.touched < l2);
    }

    private State state(Object object, float f2) {
        State state = this.states.get(object);
        if (state == null) {
            state = new State(f2);
            this.states.put(object, state);
        }
        state.touched = this.frame;
        return state;
    }

    private static void settle(State state, float f2, float f3, float f4) {
        if (!Float.isFinite(f3) || !Float.isFinite(f4) || Math.abs(f3) < 0.0015f && Math.abs(f4) < 0.01f) {
            state.pos = f2;
            state.vel = 0.0f;
        } else {
            state.pos = f2 + f3;
            state.vel = f4;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static final class State {
        float pos;
        float vel;
        long touched;

        State(float f2) {
            this.pos = f2;
        }
    }
}

