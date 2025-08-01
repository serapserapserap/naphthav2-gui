package seraph.base.Map.Event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

public abstract class MotionUpdateEvent extends Event{
    public double x;
    public double y;
    public double z;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float yaw;
    public float pitch;
    public boolean onGround;

    public MotionUpdateEvent(
            double x, double y, double z,
            double motionX, double motionY, double motionZ,
            float yaw, float pitch,
            boolean onGround
    ){
        this.x = x;
        this.y = y;
        this.z = z;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Cancelable
    public static class PreMotionUpdateEvent extends MotionUpdateEvent{
        public PreMotionUpdateEvent(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, boolean onGround) {
            super(x, y, z, motionX, motionY, motionZ, yaw, pitch, onGround);
        }
    }

    @Cancelable
    public static class PostMotionUpdateEvent extends MotionUpdateEvent{
        public PostMotionUpdateEvent(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float pitch, boolean onGround) {
            super(x, y, z, motionX, motionY, motionZ, yaw, pitch, onGround);
        }
    }
}
