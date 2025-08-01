package seraph.base.Map.Gui.notification.notifications;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import seraph.base.Map.Gui.notification.notifications.impl.NotificationType;
import seraph.base.Map.math.EasingFunction;

import static seraph.base.Naphthav2.mc;

public abstract class Notification implements NotificationImpl{

    public final int width, height, spacingX, spacingY, duration, animationDurationX, animationDurationY;
    public double posX, finalX, startX, posY, finalY, startY;
    public final double originX,originY;
    private final EasingFunction easingFunctionX, easingFunctionY;
    private final long startedAt;
    private long animationXStartedAt;
    private long animationYStartedAt;
    public String heading, body;
    public NotificationType type;
    public boolean isRetracting,isStationary = false;


    public Notification(int width, int height, int spacingX, int spacingY,int animationDurationX, int animationDurationY,double startX, double startY, EasingFunction easingFunctionX, EasingFunction easingFunctionY, int durationMillis, String title, String body, NotificationType type){
        this.width = width;
        this.height = height;
        this.spacingX = spacingX;
        this.spacingY = spacingY;
        this.animationDurationX = animationDurationX;
        this.animationDurationY = animationDurationY;
        this.originX = this.posX = startX;
        this.originY = this.posY = startY;
        durationMillis+=this.animationDurationX;
        this.duration = durationMillis;
        this.easingFunctionX = easingFunctionX;
        this.easingFunctionY = easingFunctionY;
        this.type = type;
        this.startedAt = System.currentTimeMillis();
        this.heading = title;
        this.body = body;
        this.doStartMove();
    }

    public void doStartMove(){
        ScaledResolution sr = new ScaledResolution(mc);
        this.moveX(sr.getScaledWidth() - this.getXSpacingDistance());
    }

    public double getXSpacingDistance(){
        return (double) (this.width + this.spacingX) ;
    }

    public double getYSpacingDistance(){
        return (double) (this.height + this.spacingY);
    }

    public void refreshLocation(int numberOfNotifications){
        this.moveY(new ScaledResolution(mc).getScaledHeight() - this.getYSpacingDistance() * numberOfNotifications);

        final long time = System.currentTimeMillis();

        double progressX = (double) (time - this.animationXStartedAt) / this.animationDurationX;
        double progressY = (double) (time - this.animationYStartedAt) / this.animationDurationY;


        progressY = progressY > 1 ? 1 : progressY;
        if(progressX > 1){
            progressX = 1;
            this.isStationary = false;
        }
        this.posX = this.startX + (this.finalX - this.startX) * this.easingFunctionX.apply(progressX);
        this.posY = this.startY + (this.finalY - this.startY) * this.easingFunctionY.apply(progressY);
    }

    public abstract void draw();

    public boolean render(){
        this.draw();
        return this.isHovered();
    }

    public boolean isStationary(){
        return !this.isRetracting && this.isStationary;
    }

    private boolean isHovered() {
        int mouseX = Mouse.getX() / 2;
        int mouseY = Mouse.getY() / 2;
        return mouseX >= this.posX && mouseY >= this.posY && mouseX < this.posX + this.width && mouseY < this.posY + this.height;
    }

    public void retract(){
        this.moveX(this.originX);
        this.isRetracting = true;
    }

    public boolean hasRetracted(){
        return this.posX == this.finalX && this.isRetracting;
    }

    public boolean hasExpired(){
        return this.startedAt + this.duration < System.currentTimeMillis();
    }

    public void moveX(double location){
        if(location == this.finalX) return;
        this.finalX = location;
        this.startX = this.posX;
        this.animationXStartedAt = System.currentTimeMillis();
    }

    public void moveY(double location){
        if(location == this.finalY) return;
        this.finalY = location;
        this.startY = this.posY;
        this.animationYStartedAt = System.currentTimeMillis();
    }
}
