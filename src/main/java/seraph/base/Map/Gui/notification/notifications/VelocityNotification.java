package seraph.base.Map.Gui.notification.notifications;

import seraph.base.Map.Gui.notification.notifications.impl.NotificationType;

public abstract class VelocityNotification implements NotificationImpl {
    public final int width, height, spacingX, spacingY, duration;
    public double posX, posY, veloX, veloY;
    public final double startX,startY;
    private final long startedAt;
    public String heading, body;
    public NotificationType type;
    public boolean isRetracting = false;
    public VelocityNotification(int width, int height, int spacingX, int spacingY, int startX, int startY, Integer durationMillis, String title, String body, NotificationType type) {
        this.startedAt = System.currentTimeMillis();
        this.width = width;
        this.height = height;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.duration = durationMillis;
        this.startX = startX;
        this.startY = startY;
        this.posX = this.startX;
        this.posY = this.startY;
        this.veloX = 0;
        this.veloY = 0;
        this.type = type;
        this.heading = title;
        this.body = body;
    }
    @Override
    public boolean hasExpired() {
        return this.startedAt + this.duration < System.currentTimeMillis();
    }

    @Override
    public void retract() {
        this.isRetracting = true;
    }

    @Override
    public boolean hasRetracted() {
        return this.isRetracting;
    }

    @Override
    public abstract boolean render();

    @Override
    public void refreshLocation(int i) {

    }
}
