package seraph.base.Map.Gui.notification;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import seraph.base.Map.Gui.notification.notifications.NotificationImpl;
import seraph.base.Map.Gui.notification.notifications.NotificationStyle;
import seraph.base.Map.Gui.notification.notifications.impl.NotificationType;
import seraph.base.Map.Instancer;
import seraph.base.Map.timing.Timer;
import seraph.base.Naphthav2;
import seraph.base.features.impl.NotificationsModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {
    private final List<NotificationImpl> notifications;
    private final Timer timer = new Timer(4);
    private NotificationImpl hovered;

    public List<NotificationImpl> getNotifications(){
        return this.notifications;
    }

    public NotificationImpl getHovered(){
        return this.hovered;
    }

    public NotificationManager(){
        Naphthav2.register(this);
        notifications = new ArrayList<>();
    }

    public void pushNotification(NotificationImpl notification){
        this.notifications.add(notification);
    }

    private static String getColourCodeFromType(NotificationType t) {
        switch (t) {
            case INFO:
                return "§b";
            case SUCCESS:
                return "§a";
            case ERROR:
                return "§c";
            case WARNING:
                return "§e";
        }
        return "";
    }

    public void pushNotification(int durationMillis, String topText, String bottomText, NotificationType notifType){
        NotificationStyle var0 = ((NotificationsModule) Naphthav2.INSTANCE.getModuleManager().getModuleFromAllModules(NotificationsModule.class)).getActiveStyle();
        if(var0 == NotificationStyle.CHAT){
            Naphthav2.print("§c[N>§7A>P>H>T>H>A§c] §f " + getColourCodeFromType(notifType) + (topText + " " + bottomText).toLowerCase());
            return;
        }
        this.pushNotification(Instancer.createInstance(var0.clazz,durationMillis,topText,bottomText,notifType));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<NotificationImpl> NotificationIterator = notifications.iterator();
            while (NotificationIterator.hasNext()) {
                NotificationImpl notification = NotificationIterator.next();
                if (notification.hasExpired()) {
                    notification.retract();
                }
                if(notification.hasRetracted()){
                    NotificationIterator.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event){
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        boolean r = this.timer.isReady();
        int i = 0;
        for(NotificationImpl notification : this.notifications){
            if(!notification.hasExpired()) i++;
            if(r){
                notification.refreshLocation(i);
            }
            if(notification.render()) this.hovered = notification;
        }
    }


}
