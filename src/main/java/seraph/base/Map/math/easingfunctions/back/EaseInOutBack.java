package seraph.base.Map.math.easingfunctions.back;

import seraph.base.Map.math.EasingFunction;

public class EaseInOutBack implements EasingFunction {
    @Override
    public double apply(double val) {
        final double c1 = 1.70158;
        final double c2 = c1 * 1.525;

        return val < 0.5
                ? (Math.pow(2 * val, 2) * ((c2 + 1) * 2 * val - c2)) / 2
                : (Math.pow(2 * val - 2, 2) * ((c2 + 1) * (val * 2 - 2) + c2) + 2) / 2;
    }
}
