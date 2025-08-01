package seraph.base.mixins.Event;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.MotionUpdateEvent;
import seraph.base.Map.Rotation.RotationUtils;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends EntityPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    private double oldPosX;
    private double oldPosY;
    private double oldPosZ;

    private float oldYaw;
    private float oldPitch;

    private boolean oldOnGround;

    @Inject(
            method = "onUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void onUpdatePre(CallbackInfo ci){

        this.oldPosX = this.posX;
        this.oldPosY = this.posY;
        this.oldPosZ = this.posZ;

        this.oldYaw = this.rotationYaw;
        this.oldPitch = this.rotationPitch;

        this.oldOnGround = this.onGround;

        MotionUpdateEvent.PreMotionUpdateEvent motionUpdateEvent = new MotionUpdateEvent.PreMotionUpdateEvent(this.posX,this.posY,this.posZ,this.motionX,this.motionY,this.motionZ,this.rotationYaw,this.rotationPitch,this.onGround);

        if(motionUpdateEvent.post()) ci.cancel();

        this.posX = motionUpdateEvent.x;
        this.posY = motionUpdateEvent.y;
        this.posZ = motionUpdateEvent.z;

        this.motionX = motionUpdateEvent.motionX;
        this.motionY = motionUpdateEvent.motionY;
        this.motionZ = motionUpdateEvent.motionZ;

        RotationUtils.prevTruePitch = RotationUtils.truePitch;
        this.rotationYaw = RotationUtils.trueYaw = motionUpdateEvent.yaw;
        this.rotationPitch = RotationUtils.truePitch =  motionUpdateEvent.pitch;

        this.onGround = motionUpdateEvent.onGround;
    }

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V",
                    shift = At.Shift.AFTER),
            cancellable = true)
    public void onUpdatePost(CallbackInfo ci){
        this.posX = this.oldPosX;
        this.posY = this.oldPosY;
        this.posZ = this.oldPosZ;

        this.rotationYaw = this.oldYaw;
        this.rotationPitch = this.oldPitch;

        this.onGround = this.oldOnGround;

        MotionUpdateEvent.PostMotionUpdateEvent motionUpdateEvent = new MotionUpdateEvent.PostMotionUpdateEvent(posX,posY,posZ,motionX,motionY,motionZ,rotationYaw,rotationPitch,onGround);

        if(motionUpdateEvent.post()) ci.cancel();

        this.posX = motionUpdateEvent.x;
        this.posY = motionUpdateEvent.y;
        this.posZ = motionUpdateEvent.z;

        this.motionX = motionUpdateEvent.motionX;
        this.motionY = motionUpdateEvent.motionY;
        this.motionZ = motionUpdateEvent.motionZ;

        this.rotationYaw = motionUpdateEvent.yaw;
        this.rotationPitch = motionUpdateEvent.pitch;

        this.onGround = motionUpdateEvent.onGround;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
}
