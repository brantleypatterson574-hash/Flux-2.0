/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 */
package com.ishland.scalablelux.gui.new_ui.notifications;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_327;

@Environment(value=EnvType.CLIENT)
public class Notification {
    private final String title;
    private final String message;
    private final NotificationType type;
    private final long creationTime;
    private final long duration;
    private final double width;
    private final double height;
    private static final double ANIMATION_TIME = 400.0;

    public Notification(String string, String string2, NotificationType notificationType, long l2) {
        this.title = string;
        this.message = string2;
        this.type = notificationType;
        this.duration = l2;
        this.creationTime = System.currentTimeMillis();
        class_327 class_3272 = class_310.method_1551().field_1772;
        int n2 = class_3272.method_1727(string);
        int n3 = class_3272.method_1727(string2);
        int n4 = Math.max(n2, n3);
        this.width = 40 + n4 + 24;
        this.height = string2.isEmpty() ? 40.0 : 56.0;
    }

    public Notification(String string, NotificationType notificationType, long l2) {
        this(string, "", notificationType, l2);
    }

    public boolean isExpired() {
        return (double)System.currentTimeMillis() > (double)(this.creationTime + this.duration) + 800.0;
    }

    public double getAniProg() {
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.creationTime;
        if ((double)l3 < 400.0) {
            double d2 = (double)l3 / 400.0;
            return this.easeOutCubic(d2);
        }
        if ((double)l2 > (double)(this.creationTime + this.duration) + 400.0) {
            long l4 = (long)((double)l2 - ((double)(this.creationTime + this.duration) + 400.0));
            double d3 = 1.0 - Math.min((double)l4 / 400.0, 1.0);
            return this.easeInCubic(d3);
        }
        return 1.0;
    }

    private double easeOutCubic(double d2) {
        return 1.0 - Math.pow(1.0 - d2, 3.0);
    }

    private double easeInCubic(double d2) {
        return Math.pow(d2, 3.0);
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public NotificationType getType() {
        return this.type;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getDuration() {
        return this.duration;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum NotificationType {
        SUCCESS,
        WARNING,
        ERROR,
        INFO;


        private static /* synthetic */ String iy(String string, int n2, int n3) {
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
}

