package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;

public class PosSetting extends Setting<Pair<Double, Double>> implements Changeable<Pair<Double,Double>> {
    private final double defaultX, defaultY;
    public PosSetting(String identifier, double x, double y) {
        super(
                identifier + " pos",
                ""
        );
        this.defaultX = x;
        this.defaultY = y;
        this.setValueConstructor(new Pair<>(x,y));
    }

    @Override
    public Object getJsonValue() {
        return new double[]{this.getValue().getKey(), this.getValue().getValue()};
    }

    @Override
    public Pair<Double, Double> getDefaultVal() {
        return new Pair<>(defaultX, defaultY);
    }

    @Override
    public void wipeConfig() {
        this.setValue(this.getDefaultVal());
    }
}
