package seraph.base.Map.Gui;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import seraph.base.Naphthav2;

public class Animation {
    private final long duration;
    private long progress = 0;
    private float multiplier = 1;
    private boolean loop = false;
    private long lastTime = System.currentTimeMillis();
    private Runnable onEnd = () -> {};

    public Animation onEnd(Runnable r) {
        this.onEnd = r;
        return this;
    }


    public double getProgress() {
        return Math.max(Math.min((double) progress / duration, 1.f),0.f);
    }
    public Animation(int duration) {
        this.duration = duration;
        Naphthav2.register(this);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        progress += (long) ((System.currentTimeMillis() - lastTime) * multiplier);
        lastTime = System.currentTimeMillis();
        if(this.progress > this.duration || this.progress < 0) {
            this.progress = Math.max(0, Math.min(this.progress, this.duration));
            this.onEnd.run();
            if(this.loop) {
                this.multiplier *= -1;
            }
        }
    }

    public Animation flip() {
        this.multiplier *= -1;
        return this;
    }

    public Animation reset(float progress) {
        progress = Math.min(1.f, Math.max(0.f, progress));
        this.progress = (long) (progress * duration);
        return this;
    }

    public Animation reset(float progress, float multiplier) {
        this.multiplier = multiplier;
        return this.reset(progress);
    }

    public Animation multiplier(float multiplier) {
        this.multiplier = (this.multiplier < 0) ? -Math.abs(multiplier) : Math.abs(multiplier);
        return this;
    }

    public Animation pause() {
        Naphthav2.unregister(this);
        return this;
    }

    public Animation play() {
        this.lastTime = System.currentTimeMillis();
        Naphthav2.register(this);
        return this;
    }

    public Animation flip(AnimationDirection dir) {
        this.multiplier = dir == AnimationDirection.FORWARD ? 1 * Math.abs(this.multiplier) : -1 * Math.abs(this.multiplier);
        return this;
    }

    public Animation loop() {
        this.loop = !this.loop;
        return this;
    }

    public enum AnimationDirection {
        FORWARD,
        REVERSE
    }
}
