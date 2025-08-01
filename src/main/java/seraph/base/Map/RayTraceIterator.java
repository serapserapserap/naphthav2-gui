package seraph.base.Map;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Iterator;

public class RayTraceIterator implements Iterable<BlockPos>, Iterator<BlockPos> {

    @Override
    public Iterator<BlockPos> iterator() {
        return this;
    }

    private Vec3 vec31;
    private final int i;
    private final int j;
    private final int k;
    private final double endX, endY, endZ;
    private int l;
    private int i1;
    private int j1;
    private boolean next = true;

    public RayTraceIterator(Vec3 start, Vec3 end) {
        vec31 = start;
        i = MathHelper.floor_double(end.xCoord);
        j = MathHelper.floor_double(end.yCoord);
        k = MathHelper.floor_double(end.zCoord);
        l = MathHelper.floor_double(vec31.xCoord);
        i1 = MathHelper.floor_double(vec31.yCoord);
        j1 = MathHelper.floor_double(vec31.zCoord);
        endX = end.xCoord;
        endY = end.yCoord;
        endZ = end.zCoord;
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public BlockPos next() {
        BlockPos retValue = new BlockPos(l, i1, j1);
        if (l == i && i1 == j && j1 == k) {
            next = false;
            return retValue;
        }
        boolean flag2 = true;
        boolean flag = true;
        boolean flag1 = true;
        double d0 = 999.0D;
        double d1 = 999.0D;
        double d2 = 999.0D;

        if (i > l) {
            d0 = (double) l + 1.0D;
        } else if (i < l) {
            d0 = (double) l + 0.0D;
        } else {
            flag2 = false;
        }

        if (j > i1) {
            d1 = (double) i1 + 1.0D;
        } else if (j < i1) {
            d1 = (double) i1 + 0.0D;
        } else {
            flag = false;
        }

        if (k > j1) {
            d2 = (double) j1 + 1.0D;
        } else if (k < j1) {
            d2 = (double) j1 + 0.0D;
        } else {
            flag1 = false;
        }

        double d3 = 999.0D;
        double d4 = 999.0D;
        double d5 = 999.0D;
        double d6 = endX - vec31.xCoord;
        double d7 = endY - vec31.yCoord;
        double d8 = endZ - vec31.zCoord;

        if (flag2) {
            d3 = (d0 - vec31.xCoord) / d6;
        }

        if (flag) {
            d4 = (d1 - vec31.yCoord) / d7;
        }

        if (flag1) {
            d5 = (d2 - vec31.zCoord) / d8;
        }

        if (d3 == -0.0D) {
            d3 = -1.0E-4D;
        }

        if (d4 == -0.0D) {
            d4 = -1.0E-4D;
        }

        if (d5 == -0.0D) {
            d5 = -1.0E-4D;
        }

        if (d3 < d4 && d3 < d5) {
            vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
            if (i > l) {
                l = MathHelper.floor_double(vec31.xCoord);
            } else {
                l = MathHelper.floor_double(vec31.xCoord) - 1;
            }
            i1 = MathHelper.floor_double(vec31.yCoord);
            j1 = MathHelper.floor_double(vec31.zCoord);
        } else if (d4 < d5) {
            vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
            if (j > i1) {
                i1 = MathHelper.floor_double(vec31.yCoord);
            } else {
                i1 = MathHelper.floor_double(vec31.yCoord) - 1;
            }
            l = MathHelper.floor_double(vec31.xCoord);
            j1 = MathHelper.floor_double(vec31.zCoord);
        } else {
            vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
            if (k > j1) {
                j1 = MathHelper.floor_double(vec31.zCoord);
            } else {
                j1 = MathHelper.floor_double(vec31.zCoord) - 1;
            }
            l = MathHelper.floor_double(vec31.xCoord);
            i1 = MathHelper.floor_double(vec31.yCoord);
        }
        return retValue;
    }
}
