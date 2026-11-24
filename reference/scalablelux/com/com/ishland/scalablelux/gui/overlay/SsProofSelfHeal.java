/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.overlay;

import java.util.ArrayDeque;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class SsProofSelfHeal {
    public static final long QUICK_CLOSE_MS = 1500L;
    public static final long WINDOW_MS = 10000L;
    public static final int TRIGGER_COUNT = 2;
    private final ArrayDeque<Long> quickCloses = new ArrayDeque();

    public synchronized boolean note(long l2, long l3) {
        if (l2 < 0L || l2 > 1500L) {
            return false;
        }
        while (!this.quickCloses.isEmpty() && l3 - this.quickCloses.peekFirst() > 10000L) {
            this.quickCloses.pollFirst();
        }
        this.quickCloses.addLast(l3);
        if (this.quickCloses.size() < 2) {
            return false;
        }
        this.quickCloses.clear();
        return true;
    }

    public synchronized void reset() {
        this.quickCloses.clear();
    }
}

