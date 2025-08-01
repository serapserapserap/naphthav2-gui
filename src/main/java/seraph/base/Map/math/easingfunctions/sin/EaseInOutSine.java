package seraph.base.Map.math.easingfunctions.sin;

import seraph.base.Map.math.EasingFunction;

public class EaseInOutSine implements EasingFunction {
    @Override
    public double apply(double val) {
        return -(Math.cos(Math.PI * val) - 1) / 2;
    }
}
