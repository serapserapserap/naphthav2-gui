package seraph.base.Map.Event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

public abstract class GuiScreenEvent extends Event {
    @Cancelable
    public static class Draw extends GuiScreenEvent{
        public float mx, my, partialTicks;
        public Draw(float mx, float my, float partialTicks) {
            this.mx = mx; this.my = my; this.partialTicks = partialTicks;
        }
    }

    public static class PostDraw extends GuiScreenEvent {
        public float mx, my, partialTicks;
        public PostDraw(float mx, float my, float partialTicks) {
            this.mx = mx; this.my = my; this.partialTicks = partialTicks;
        }
    }

    @Cancelable
    public static class Close extends GuiScreenEvent{}

    @Cancelable
    public static class Scroll extends GuiScreenEvent{
        float amnt;
        public Scroll(float amount) {
            this.amnt = amount;
        }
    }

    @Cancelable
    public static class MouseDrag extends GuiScreenEvent{
        public float mx, my;
        public int button;
        public long timeSinceLastClick;
        public MouseDrag(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick) {
            this.mx = mouseX; this.my = mouseY;
            this.button = mouseButton;
            this.timeSinceLastClick = timeSinceLastClick;
        }
    }

    @Cancelable
    public static class MouseClick extends GuiScreenEvent{
        public float mx, my;
        public int button;
        public MouseClick(float mouseX, float mouseY, int button) {
            this.mx = mouseX; this.my = mouseY;
            this.button = button;
        }
    }

    @Cancelable
    public static class MouseRelease extends GuiScreenEvent {
        public float mx, my;
        public int state;
        public MouseRelease(int mouseX, int mouseY, int state) {
            this.mx = mouseX; this.my = mouseY;
            this.state = state;
        }
    }

    @Cancelable
    public static class KeyPress extends GuiScreenEvent {
        public int keyCode;
        public int character;
        public KeyPress(char character, int keyCode) {
            this.keyCode = keyCode;
            this.character = character;
        }
    }

    @Cancelable
    public static class HandleInputPre extends GuiScreenEvent {}

    public static class HandleInputPost extends GuiScreenEvent {}
}
