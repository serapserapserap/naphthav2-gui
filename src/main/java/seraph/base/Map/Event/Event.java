package seraph.base.Map.Event;

import net.minecraftforge.common.MinecraftForge;

public abstract class Event extends net.minecraftforge.fml.common.eventhandler.Event {
    public boolean post(){
            /*try{
                return MinecraftForge.EVENT_BUS.post(this);
            }catch (Exception e){
                e.printStackTrace();
                Base.sendModMsg("Error dispatching " + this.getClass().getSimpleName());
            }*/
            //rawdog it
        return MinecraftForge.EVENT_BUS.post(this);
    }
}
