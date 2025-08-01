package seraph.base.Map.Gui;

import static seraph.base.Naphthav2.mc;

public class FixedResolution {
    private final int width,height;

    public FixedResolution() {
        this.width = mc.displayWidth;
        this.height = mc.displayHeight;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}
