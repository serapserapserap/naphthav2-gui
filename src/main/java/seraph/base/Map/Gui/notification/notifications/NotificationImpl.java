package seraph.base.Map.Gui.notification.notifications;

public interface NotificationImpl {
    boolean hasExpired();
    void retract();
    boolean hasRetracted();
    boolean render();
    void refreshLocation(int i);
}
