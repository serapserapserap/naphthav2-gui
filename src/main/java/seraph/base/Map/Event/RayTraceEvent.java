package seraph.base.Map.Event;

import net.minecraft.util.MovingObjectPosition;

public abstract class RayTraceEvent extends Event {
    public MovingObjectPosition mop;
    public static class Pre extends RayTraceEvent {
        public float yaw;
        public float pitch;
        public double reach;
        public Pre(MovingObjectPosition mop, float y, float p, double r) {
            this.mop = mop;
            this.yaw = y;
            this.pitch = p;
            this.reach = r;
        }
    }
    public static class Post extends RayTraceEvent {
        public Post(MovingObjectPosition mop) {
            this.mop = mop;
        }
    }
}
