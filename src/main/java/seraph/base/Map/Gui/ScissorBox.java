package seraph.base.Map.Gui;

import org.lwjgl.opengl.GL11;

public class ScissorBox {
    public int x,y,w,h;
    public ScissorBox(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = Math.max(0,w);
        this.h = Math.max(0,h);
    }

    public ScissorBox glScissor() {
        GL11.glScissor(x,y,w,h);
        return this;
    }

    public ScissorBox intersection(ScissorBox b) {
        int x = Math.max(this.x, b.x);
        int y = Math.max(this.y, b.y);
        int right = Math.min(this.x + this.w, b.x + b.w);
        int top = Math.min(this.y + this.h, b.y + b.h);
        int width = Math.max(0, right - x);
        int height = Math.max(0, top - y);
        return new ScissorBox(x, y, width, height);
    }
}
