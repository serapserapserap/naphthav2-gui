package seraph.base.Map.tasks;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Predicate;

public class ConditionalTask<T extends Event> extends Task<T> {

    private final Predicate<T> condition;

    public ConditionalTask(Predicate<T> condition, Runnable exec){
        super(exec);
        this.condition = condition;
    }

    @Override
    @SubscribeEvent
    public void onEvent(T e){
        if(this.condition.test(e)){
            this.run();
        }
    }
}
