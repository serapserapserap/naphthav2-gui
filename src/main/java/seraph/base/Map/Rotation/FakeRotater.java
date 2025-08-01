package seraph.base.Map.Rotation;

public class FakeRotater extends Rotater {
    @Override
    public void rotate(float yaw, float pitch) {
        Rotater.yaw = yaw;
        Rotater.pitch = pitch;
        Rotater.fakeRotating = true;
    }
}
