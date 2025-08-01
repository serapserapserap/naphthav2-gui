package seraph.base.Map.syshelpers;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import static seraph.base.Naphthav2.mc;

public class MouseHelper$1 {
    public static double[] getMouse(){
        double scale = new ScaledResolution(mc).getScaleFactor();
        double mouseX = Mouse.getX() / scale;
        double mouseY = (mc.displayHeight - Mouse.getY()) / scale;
        return new double[]{mouseX,mouseY};
    }

}
