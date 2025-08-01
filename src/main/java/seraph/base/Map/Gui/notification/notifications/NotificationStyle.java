package seraph.base.Map.Gui.notification.notifications;

import seraph.base.Map.Gui.notification.notifications.impl.BasicNotification;

public enum NotificationStyle {
    BASIC(BasicNotification.class,"basic"),
    CHAT(Notification.class,"chat");
    public final Class<? extends NotificationImpl> clazz;
    public final String key;

    NotificationStyle(Class<? extends NotificationImpl> clazz,String key){
        this.clazz = clazz;
        this.key = key;
    }
}
