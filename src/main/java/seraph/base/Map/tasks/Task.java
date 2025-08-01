package seraph.base.Map.tasks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Task<T extends Event> {
    private final Runnable exec;
    public Task(Runnable exec){
        this.exec = exec;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void exec() {
        exec.run();
    }

    protected void run(){
        this.exec();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onEvent(T e){
        this.run();
    }
}
