package seraph.base.Map.math.easingfunctions.back;

import seraph.base.Map.math.EasingFunction;

public class EaseInBack implements EasingFunction {
    @Override
    public double apply(double val) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;

        return c3 * val * val * val - c1 * val * val;
    }
}
