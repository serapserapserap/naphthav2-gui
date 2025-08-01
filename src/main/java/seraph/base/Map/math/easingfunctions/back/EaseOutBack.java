package seraph.base.Map.math.easingfunctions.back;

import seraph.base.Map.math.EasingFunction;

public class EaseOutBack implements EasingFunction {
    @Override
    public double apply(double val) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;

        return 1 + c3 * Math.pow(val - 1, 3) + c1 * Math.pow(val - 1, 2);
    }
}
