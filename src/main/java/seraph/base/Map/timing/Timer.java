package seraph.base.Map.timing;


public class Timer {
    private long lastUpdate = 0;
    private long cooldownMillis;

    public Timer(long timeout){
        this.cooldownMillis = timeout;
    }

    public boolean isReady(){
        boolean var0 = this.lastUpdate + cooldownMillis < System.currentTimeMillis();
        if(var0) lastUpdate = System.currentTimeMillis();
        return var0;
    }
}
