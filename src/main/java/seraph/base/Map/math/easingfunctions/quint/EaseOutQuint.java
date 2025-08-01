package seraph.base.Map.math.easingfunctions.quint;

import seraph.base.Map.math.EasingFunction;

public class EaseOutQuint implements EasingFunction {
    @Override
    public double apply(double val) {
        return 1 - Math.pow(1 - val, 5);
    }
}
