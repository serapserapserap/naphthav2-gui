package seraph.base.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import static seraph.base.Naphthav2.mc;

public class RayTraceHelper {
    public static MovingObjectPosition fastRayTrace(Vec3 from, Vec3 to) {
        if (from == null || to == null) return null;

        for (BlockPos pos : getRayTrace(from, to)) {
            IBlockState state = mc.theWorld.getBlockState(pos);
            Block block = state.getBlock();
            if (!block.canCollideCheck(state, true)) continue;

            MovingObjectPosition hit = block.collisionRayTrace(mc.theWorld, pos, from, to);
            if (hit != null) return hit;
        }
        return null;
    }

    public static Iterable<BlockPos> getRayTrace(Vec3 start, Vec3 end) {
        return new RayTraceIterator(start, end);
    }

    public static boolean pointHittable(Vec3 from, Vec3 to) {
        MovingObjectPosition mop = fastRayTrace(from, to);
        return mop == null;
    }
}
