package seraph.base.Map.math.easingfunctions.cubic;

import seraph.base.Map.math.EasingFunction;

public class EaseInOutCubic implements EasingFunction {
    @Override
    public double apply(double val) {
        return val < 0.5 ? 4 * val * val * val : 1 - Math.pow(-2 * val + 2, 3) / 2;
    }
}
