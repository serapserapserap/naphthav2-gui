package seraph.base.Map.Rotation;

import static seraph.base.Naphthav2.mc;

public class RealRotater extends Rotater {
    @Override
    public void rotate(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }
}
