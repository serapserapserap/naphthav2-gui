package seraph.base.Map.Gui.notification.notifications.impl;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import seraph.base.Map.math.easingfunctions.linear.EaseInOutLinear;
import seraph.base.Naphthav2;
import seraph.base.Map.Gui.FixedResolution;
import seraph.base.Map.Gui.notification.notifications.Notification;
import seraph.base.Map.drawing.Drawer;
import seraph.base.Map.math.easingfunctions.cubic.EaseInOutCubic;

import static seraph.base.Naphthav2.jetBrainsMonoExtraSmall;
import static seraph.base.Naphthav2.mc;

public class BasicNotification extends Notification {
    public BasicNotification(Integer durationMillis, String title, String body, NotificationType type) {
        super(
                100,
                30,
                5,
                5,
                350,
                250,
                new FixedResolution().getWidth() + 105,
                new FixedResolution().getHeight() - Naphthav2.INSTANCE.notificationManager.getNotifications().size() * 30,
                new EaseInOutCubic(),
                new EaseInOutLinear(),
                durationMillis,
                title,
                body,
                type
        );
    }

    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        double var1 = (double) this.height / 3;
        GL11.glColor4f(.2f,.2f,.2f,.75f);
        Drawer.drawRectangle2D(
                this.posX,
                this.posY,
                this.width,
                var1
        );
        GL11.glColor4f(.2f,.2f,.2f,.55f);
        Drawer.drawRectangle2D(
                this.posX,
                this.posY + var1,
                this.width,
                this.height*0.6666666667D
        );

        GL11.glColor4f(1,1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.fontRendererObj.drawStringWithShadow(
                this.heading,
                (float) (this.posX +3),
                (float) (this.posY + 3),
                0xffffffff
        );
        mc.fontRendererObj.drawStringWithShadow(
                this.body,
                (float) this.posX  + 3,
                (float) (this.posY + var1 + 3),
                0xffffffff

        );
        GL11.glPopMatrix();
    }
}
