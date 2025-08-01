package seraph.base.Map.Gui;

import seraph.base.mixins.Accessors.AccessorMinecraft;

import static seraph.base.Naphthav2.mc;

public class ScrollManager {
    private float lastScrollAmnt = 0;
    private float scrollAmnt = 0;
    public float maxScroll;

    public ScrollManager(float maxScroll) {
        this.maxScroll = maxScroll;
    }

    public void update(float amnt) {
        lastScrollAmnt = scrollAmnt;
        scrollAmnt += amnt;
        clampScroll();
    }

    public void setMaxScroll(float newMax) {
        this.maxScroll = newMax;
        float clamped = maxScroll <= 0 ? 0 : Math.max(-maxScroll, Math.min(0, scrollAmnt));
        if (clamped != scrollAmnt) {
            scrollAmnt = clamped;
            lastScrollAmnt = clamped;
        }
    }

    public float getScroll() {
        float nuts = lastScrollAmnt + (scrollAmnt - lastScrollAmnt) * ((AccessorMinecraft)mc).getTimer().renderPartialTicks;
        return nuts;
    }

    void clampScroll() {
        if (maxScroll <= 0) scrollAmnt = 0;
        else scrollAmnt = Math.max(-maxScroll, Math.min(0, scrollAmnt));
    }
}
