package seraph.base.Map.Event;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

import static seraph.base.Naphthav2.mc;

@Cancelable
public class SetupGuiScaleEvent extends Event {
    public double width, height;
    public SetupGuiScaleEvent() {
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        width = scaledresolution.getScaledWidth_double();
        height = scaledresolution.getScaledHeight_double();
    }
}
