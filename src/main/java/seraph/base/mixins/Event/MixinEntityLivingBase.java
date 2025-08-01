package seraph.base.mixins.Event;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import seraph.base.Map.Rotation.RotationUtils;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase() {super(null);}

    @Shadow
    public float renderYawOffset;
    @Shadow
    public float swingProgress;
    @Shadow
    public float rotationYawHead;

    /**
     * @author strangerrs
     * @reason mixin func110146f
     */
    @Inject(method = "updateDistance", at = @At("HEAD"), cancellable = true)
    protected void func_110146_f(float p_1101461, float p_1101462, CallbackInfoReturnable<Float> cir) {
        float rotationYaw = this.rotationYaw;

        if ((EntityLivingBase) (Object) this instanceof EntityPlayerSP) {
            if (this.swingProgress > 0F) {
                p_1101461 = RotationUtils.trueYaw;
            }
            rotationYaw = RotationUtils.trueYaw;
            rotationYawHead = RotationUtils.trueYaw;
        }
        float f = MathHelper.wrapAngleTo180_float(p_1101461 - this.renderYawOffset);
        this.renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapAngleTo180_float(rotationYaw - this.renderYawOffset);
        boolean flag = f1 < 90.0F || f1 >= 90.0F;

        if (f1 < -75.0F) {
            f1 = -75.0F;
        }

        if (f1 >= 75.0F) {
            f1 = 75.0F;
        }

        this.renderYawOffset = rotationYaw - f1;

        if (f1 * f1 > 2500.0F) {
            this.renderYawOffset += f1 * 0.2F;
        }

        if (flag) {
            p_1101462 *= -1.0F;
        }

        cir.setReturnValue(p_1101462);
    }
}

