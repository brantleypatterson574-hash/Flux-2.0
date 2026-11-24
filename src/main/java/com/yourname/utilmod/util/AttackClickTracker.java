package com.yourname.utilmod.util;

import java.util.ArrayDeque;

/**
 * Rolling one-second window of in-world attack clicks, used by the client-side
 * reaction monitor. Kept separate from the module so the mixin hook stays tiny.
 */
public final class AttackClickTracker {
    private static final ArrayDeque<Long> CLICKS = new ArrayDeque<>();

    private AttackClickTracker() {}

    public static void record() {
        long now = System.currentTimeMillis();
        synchronized (CLICKS) {
            CLICKS.addLast(now);
            trim(now);
        }
    }

    public static int cps() {
        long now = System.currentTimeMillis();
        synchronized (CLICKS) {
            trim(now);
            return CLICKS.size();
        }
    }

    public static void clear() {
        synchronized (CLICKS) {
            CLICKS.clear();
        }
    }

    private static void trim(long now) {
        while (!CLICKS.isEmpty() && now - CLICKS.peekFirst() > 1000L) {
            CLICKS.pollFirst();
        }
    }
}
