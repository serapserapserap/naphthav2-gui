package seraph.base.Map.Gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import seraph.base.Map.Gui.config.Config;

import java.io.IOException;

public class GuiscreenWrapper extends GuiScreen {

    private final GraphicalUserInterface GUI;
    private int lastScroll;

    public GuiscreenWrapper(GraphicalUserInterface style){
        super();
        this.GUI = style;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void initGui(){
        GUI.onOpen();
    }

    @Override
    public void onGuiClosed(){
        GUI.onClose();
        Config.saveConfig();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(this.GUI.onKeyTyped(typedChar,keyCode))
            super.keyTyped(typedChar,keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        GUI.render(mouseX,mouseY,partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame(){return false;}

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        GUI.onMouseDrag(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        GUI.onRelease(mouseX,mouseY,state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        GUI.onClick(mouseX,mouseY,mouseButton);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        GUI.onMouseScroll(lastScroll);
        lastScroll = 0;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0) {
            lastScroll = i;
        }

    }
}
