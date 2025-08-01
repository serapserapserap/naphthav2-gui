package seraph.base.Map.Rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seraph.base.Naphthav2;
import seraph.base.Map.Event.MotionUpdateEvent;
import seraph.base.Map.sortingcomparitors.ArrayListSorters;

import java.util.ArrayList;
import java.util.List;

import static seraph.base.Naphthav2.mc;

public abstract class Rotater {

    private static final RotationManager johnFortnite = new RotationManager();
    public static Rotater rotater;
    static float pitch;
    static float yaw;
    static boolean fakeRotating;

    public void rotate(Vec3 vec){
        float[] angles = getYawPitchDistance(vec.xCoord, vec.yCoord,vec.zCoord);
        this.rotateTo(angles[0],angles[1]);
    }
    public void rotate(BlockPos block){
        float[] angles = getYawPitchDistance(block.getX() + .5D, block.getY() + .5D, block.getZ() + .5D );
        this.rotateTo(angles[0],angles[1]);
    }

    public void rotateToEntityBasic(Entity e){
        rotate(new Vec3(e.posX,e.posY+e.getEyeHeight(),e.posZ));
    }

    public void rotate(Entity e){
        AxisAlignedBB aabb = e.getEntityBoundingBox();
        double height = aabb.maxY - aabb.minY;
        double quarterHeight = height/4;

        List<Vec3> positions = new ArrayList<>();
        for(int i = 4 ; i>1 ; i--) positions.add(new Vec3(e.posX,e.posY + i * quarterHeight - quarterHeight / 2,e.posZ));

        ArrayListSorters.sortByClosestToPlayer(positions);
        rotate(positions.get(0));
    }

    public abstract void rotate(float yaw, float pitch);

    public void rotateTo(float yaw, float pitch){
        try{
            Rotater.rotater = this;
            Rotater.fakeRotating = false;
            rotater.rotate(yaw,pitch);
        } catch (Exception e){
            e.printStackTrace();
            Naphthav2.sendModMsg("Error Rotating");
        }
    }

    public static Rotation getRotation(double targetX, double targetY, double targetZ){
        float[] foat = getYawPitchDistance(targetX,targetY,targetZ);
        return new Rotation(foat[0],foat[1],mc.thePlayer.getPositionVector().distanceTo(new Vec3(targetX,targetY,targetZ)),foat[2]);
    }

    public static float[] getYawPitchDistance(double targetX, double targetY, double targetZ) {
        double playerX = mc.thePlayer.posX;
        double playerY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double playerZ = mc.thePlayer.posZ;

        double deltaX = targetX - playerX;
        double deltaY = targetY - playerY;
        double deltaZ = targetZ - playerZ;

        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90F;
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, distance));

        return new float[]{yaw, pitch, distance};
    }

    public static class Rotation{
        private final float yaw;
        private final float pitch;
        private final double distance3D;
        private final double distance2D;

        public float getYaw() {
            return this.yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public double getDistance3D() {
            return distance3D;
        }

        public double getDistance2D() {
            return distance2D;
        }

        Rotation(float yaw, float pitch, double distance3D, double distance2D){
            this.yaw = yaw;
            this.pitch = pitch;
            this.distance3D = distance3D;
            this.distance2D = distance2D;
        }
    }

    private static class RotationManager {

        public RotationManager(){
            Naphthav2.register(this);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onMotionUpdatePre(MotionUpdateEvent.PreMotionUpdateEvent e){
            if(fakeRotating){
                e.yaw = yaw;
                e.pitch = pitch;
                fakeRotating = false;
            }
        }
    }
}
