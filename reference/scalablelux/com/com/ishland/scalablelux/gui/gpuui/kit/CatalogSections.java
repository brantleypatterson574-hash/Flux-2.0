/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.module.SubCategory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class CatalogSections {
    private CatalogSections() {
    }

    public static <T> Arranged<T> arrange(List<T> list, Function<T, SubCategory> function) {
        Object object22;
        if (list.size() < 2) {
            return new Arranged<T>(list, List.of());
        }
        EnumMap<SubCategory, List> enumMap = new EnumMap<SubCategory, List>(SubCategory.class);
        for (Object object22 : list) {
            Object object3 = function.apply(object22);
            enumMap.computeIfAbsent((SubCategory)((Object)(object3 == null ? SubCategory.fP : object3)), subCategory -> new ArrayList()).add(object22);
        }
        if (enumMap.size() < 2) {
            return new Arranged<T>(list, List.of());
        }
        ArrayList arrayList = new ArrayList(list.size());
        object22 = new ArrayList(enumMap.size());
        for (Map.Entry entry : enumMap.entrySet()) {
            int n2 = arrayList.size();
            arrayList.addAll((Collection)entry.getValue());
            object22.add((Group)new Group((SubCategory)((Object)entry.getKey()), n2, arrayList.size()));
        }
        return new Arranged(arrayList, (List<Group>)object22);
    }

    public static int groupIndexOf(List<Group> list, int n2) {
        for (int i2 = 0; i2 < list.size(); ++i2) {
            Group group = list.get(i2);
            if (n2 < group.start() || n2 >= group.end()) continue;
            return i2;
        }
        return -1;
    }

    public static int clampDrop(int n2, int n3, List<Group> list, boolean bl, boolean[] blArray) {
        int n4;
        int n5 = CatalogSections.groupIndexOf(list, n3);
        int n6 = 0;
        int n7 = blArray.length;
        if (n5 >= 0) {
            n6 = list.get(n5).start();
            n7 = list.get(n5).end();
        }
        int n8 = 0;
        for (n4 = n6; n4 < n7; ++n4) {
            if (!blArray[n4]) continue;
            ++n8;
        }
        n4 = bl ? n6 : n6 + n8;
        int n9 = bl ? n6 + n8 : n7;
        return Math.max(n4, Math.min(n9, n2));
    }

    public static Layout layout(List<Group> list, int n2, int n3, float f2, float f3) {
        n3 = Math.max(1, n3);
        int[] nArray = new int[n2];
        int[] nArray2 = new int[n2];
        float[] fArray = new float[n2];
        if (list.isEmpty()) {
            for (int i2 = 0; i2 < n2; ++i2) {
                nArray[i2] = i2 / n3;
                nArray2[i2] = i2 % n3;
            }
            return new Layout(nArray, nArray2, fArray, new float[0], list, (float)((n2 + n3 - 1) / n3) * f2, f2, n3);
        }
        float[] fArray2 = new float[list.size()];
        int n4 = 0;
        for (int i3 = 0; i3 < list.size(); ++i3) {
            Group group = list.get(i3);
            fArray2[i3] = (float)n4 * f2 + f3 * (float)i3;
            for (int i4 = group.start(); i4 < group.end(); ++i4) {
                int n5 = i4 - group.start();
                nArray[i4] = n4 + n5 / n3;
                nArray2[i4] = n5 % n3;
                fArray[i4] = f3 * (float)(i3 + 1);
            }
            n4 += (group.size() + n3 - 1) / n3;
        }
        return new Layout(nArray, nArray2, fArray, fArray2, list, (float)n4 * f2 + f3 * (float)list.size(), f2, n3);
    }

    public static Drop drop(Layout layout, int n2, boolean[] blArray, boolean bl, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n3;
        int n4 = layout.n();
        int n5 = CatalogSections.groupIndexOf(layout.groups, n2);
        int n6 = 0;
        int n7 = n4;
        if (n5 >= 0) {
            n6 = layout.groups.get(n5).start();
            n7 = layout.groups.get(n5).end();
        }
        float f10 = f6 + f8;
        int n8 = n6;
        float f11 = Float.MAX_VALUE;
        for (n3 = n6; n3 <= n7; ++n3) {
            float f12;
            float f13;
            float f14;
            if (n3 < n7) {
                f14 = f4 + (float)layout.col[n3] * f10 - f8 * 0.5f;
                f13 = f5 + layout.y(n3) + f7 * 0.5f;
                if (layout.cols == 1) {
                    f14 = f2;
                    f13 = f5 + layout.y(n3) - f9 * 0.5f;
                }
            } else {
                if (n7 <= n6) continue;
                int n9 = n7 - 1;
                if (layout.cols == 1) {
                    f14 = f2;
                    f13 = f5 + layout.y(n9) + f7 + f9 * 0.5f;
                } else {
                    f14 = f4 + (float)(layout.col[n9] + 1) * f10 - f8 * 0.5f;
                    f13 = f5 + layout.y(n9) + f7 * 0.5f;
                }
            }
            if (!((f12 = Math.abs(f3 - f13) * 4.0f + Math.abs(f2 - f14)) < f11)) continue;
            f11 = f12;
            n8 = n3;
        }
        n3 = CatalogSections.clampDrop(n8, n2, layout.groups, bl, blArray);
        boolean bl2 = n3 >= n7 && n7 > n6;
        int n10 = bl2 ? n7 - 1 : Math.min(n3, n4 - 1);
        return new Drop(n3, bl2, Math.max(0, n10));
    }

    @Environment(value=EnvType.CLIENT)
    public record Arranged<T>(List<T> items, List<Group> groups) {
    }

    @Environment(value=EnvType.CLIENT)
    public record Group(SubCategory sub, int start, int end) {
        public int size() {
            return this.end - this.start;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Layout {
        public final int[] row;
        public final int[] col;
        public final float[] yOff;
        public final float[] headerY;
        public final List<Group> groups;
        public final float height;
        public final float rowPitch;
        public final int cols;

        private Layout(int[] nArray, int[] nArray2, float[] fArray, float[] fArray2, List<Group> list, float f2, float f3, int n2) {
            this.row = nArray;
            this.col = nArray2;
            this.yOff = fArray;
            this.headerY = fArray2;
            this.groups = list;
            this.height = f2;
            this.rowPitch = f3;
            this.cols = n2;
        }

        public float y(int n2) {
            return (float)this.row[n2] * this.rowPitch + this.yOff[n2];
        }

        public int n() {
            return this.row.length;
        }

        public int step(int n2, int n3) {
            if (n2 < 0 || n2 >= this.row.length) {
                return n2;
            }
            int n4 = this.row[n2] + n3;
            int n5 = -1;
            for (int i2 = 0; i2 < this.row.length; ++i2) {
                if (this.row[i2] != n4) continue;
                if (this.col[i2] == this.col[n2]) {
                    return i2;
                }
                if (n5 >= 0 && this.col[i2] <= this.col[n5]) continue;
                n5 = i2;
            }
            return n5 < 0 ? n2 : n5;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record Drop(int index, boolean after, int anchor) {
    }
}

