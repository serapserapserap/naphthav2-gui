package seraph.base.Map.math.easingfunctions.cubic;

import seraph.base.Map.math.EasingFunction;

public class EaseOutCubic implements EasingFunction {
    @Override
    public double apply(double val) {
        return 1 - Math.pow(1 - val, 3);
    }
}
