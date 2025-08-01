package seraph.base.Map.math.easingfunctions.cubic;

import seraph.base.Map.math.EasingFunction;

public class EaseInCubic implements EasingFunction {
    @Override
    public double apply(double val) {
        return Math.pow(val,3);
    }
}
