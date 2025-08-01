package seraph.base.features.impl;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seraph.base.Map.Event.RayTraceEvent;
import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.modules.Module;

import static seraph.base.Naphthav2.mc;

//@ModuleClass
public class NoMouseOverModule extends Module {
    public NoMouseOverModule() {
        super(
                "No Mouse Over",
                "prevents you from hovering over/clicking specific objects",
                Category.FIVE,
                SubCategory.GUI
        );
    }

    @SubscribeEvent
    public void onRayTracePost(RayTraceEvent.Post e) {
        e.mop = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, new Vec3(0,0,0), null, new BlockPos(0,0,0));
        mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, new Vec3(0,0,0), null, new BlockPos(0,0,0));
    }

    public static MovingObjectPosition rayTrace(Entity entity, double blockReachDistance, float partialTicks, float yaw, float pitch)
    {
        Vec3 vec3 = entity.getPositionEyes(partialTicks);
        Vec3 vec31 = getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static Vec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = MathHelper.cos(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-p_getVectorForRotation_1_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_getVectorForRotation_1_ * 0.017453292F);
        return new Vec3(f1 * f2, f3, f * f2);
    }
}
