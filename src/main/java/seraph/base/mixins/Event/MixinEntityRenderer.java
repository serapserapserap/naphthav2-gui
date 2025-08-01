package seraph.base.mixins.Event;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.RayTraceEvent;
import seraph.base.Map.Event.SetupGuiScaleEvent;
import seraph.base.features.impl.NoMouseOverModule;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    private Minecraft mc;

    @Shadow
    private Entity pointedEntity;


    @Inject(method = "setupOverlayRendering", at = @At("HEAD"), cancellable = true)
    public void setupGuiScaleEvent(CallbackInfo ci) {
        SetupGuiScaleEvent event = new SetupGuiScaleEvent();
        if(event.post()) {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, event.width, event.height, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            ci.cancel();
        }
    }

    // one day serfer made me explod

    @Inject(method = "getMouseOver", at = @At("HEAD"), cancellable = true)
    public void getMouseOver(float partialTicks, CallbackInfo ci)
    {

        Entity entity = this.mc.getRenderViewEntity();

        if (entity != null)
        {
            if (this.mc.theWorld != null)
            {
                float yaw = mc.thePlayer.rotationYaw;
                float pitch = mc.thePlayer.rotationPitch;
                double d0 = (double)this.mc.playerController.getBlockReachDistance();
                RayTraceEvent.Pre e = new RayTraceEvent.Pre(mc.objectMouseOver, yaw, pitch, d0);
                e.post();
                yaw = e.yaw;
                pitch = e.pitch;
                d0 = e.reach;

                this.mc.mcProfiler.startSection("pick");
                this.mc.pointedEntity = null;

                this.mc.objectMouseOver = NoMouseOverModule.rayTrace(entity, d0, partialTicks, yaw, pitch); // explod
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(partialTicks);
                boolean flag = false;

                if (this.mc.playerController.extendedReach())
                {
                    d0 = 6.0D;
                    d1 = 6.0D;
                }
                else
                {
                    if (d0 > 3.0D)
                    {
                        flag = true;
                    }
                }

                if (this.mc.objectMouseOver != null) {
                    d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = NoMouseOverModule.getVectorForRotation(pitch,yaw);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                this.pointedEntity = null;
                Vec3 vec33 = null;
                float f = 1.0F;
                List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = (Entity)list.get(j);
                    float f1 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                    if (axisalignedbb.isVecInside(vec3))
                    {
                        if (d2 >= 0.0D)
                        {
                            this.pointedEntity = entity1;
                            vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                            d2 = 0.0D;
                        }
                    }
                    else if (movingobjectposition != null)
                    {
                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1 == entity.ridingEntity && !entity.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    this.pointedEntity = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                }
                            }
                            else
                            {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D)
                {
                    this.pointedEntity = null;
                    this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
                }

                if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null))
                {
                    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);

                    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame)
                    {
                        this.mc.pointedEntity = this.pointedEntity;
                    }
                }

                this.mc.mcProfiler.endSection();
                RayTraceEvent.Post r = new RayTraceEvent.Post(mc.objectMouseOver);
                this.mc.objectMouseOver = r.mop;
            }
        }
        ci.cancel();
    }
}
