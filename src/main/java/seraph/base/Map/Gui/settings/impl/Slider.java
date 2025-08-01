package seraph.base.Map.Gui.settings.impl;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Naphthav2;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.mixins.Accessors.AccessorMinecraft;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static seraph.base.Naphthav2.mc;

public class Slider extends Setting<Double> implements Changeable<Double> {
    private final Double min;
    private final Double max;
    public int decimalPlaces;
    public double lastVal;
    private double defaultVal;

    public Slider(String name, String description,Double minVal, Double maxVal, Double defaultVal) {
        super(name, description);
        this.min = minVal;
        this.max = maxVal;
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
        this.decimalPlaces = 1;
        Naphthav2.register(this);
    }

    public Slider(String name, String description,Double minVal, Double maxVal, Double defaultVal, int decimalPlaces) {
        super(name, description);
        this.min = minVal;
        this.max = maxVal;
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
        this.decimalPlaces = Math.max(decimalPlaces, 0);
        Naphthav2.register(this);
    }

    @Override
    public void onValueChange(Double oldVal){
        Double var0 = this.getValue();
        if(decimalPlaces != Integer.MAX_VALUE){
            var0 = roundDecimal(var0,this.decimalPlaces);
        }
        this.setValueConstructor(Math.min(this.max, Math.max(this.min, var0)));
    }

    public static Double roundDecimal(Double decimal, int decimalPlaces){
        if (decimalPlaces < 0) throw new IllegalArgumentException("Decimal places must be non-negative");

        BigDecimal bd = BigDecimal.valueOf(decimal);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase == TickEvent.Phase.END) this.lastVal = this.getValue();
    }

    public double getMax() {
        return this.max;
    }

    public double getFract() {
        double ratio1 = this.getValue() / this.max;
        double ratio2 = this.lastVal / this.max;
        return ratio2 + (ratio1 - ratio2) * ((AccessorMinecraft)mc).getTimer().renderPartialTicks;
    }

    @Override
    public void wipeConfig() {
        this.setValue(this.getDefaultVal());
    }

    @Override
    public Double getDefaultVal() {
        return this.defaultVal;
    }
}
