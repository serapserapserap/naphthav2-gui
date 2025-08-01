package seraph.base.mixins.Event;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import seraph.base.Map.Rotation.RotationUtils;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    @Unique
    private float naphthav2$cachedPrevRotationPitch;
    @Unique
    private float naphthav2$cachedRotationPitch;

    @Inject(method = "renderEntityStatic", at = @At("HEAD"))
    public void renderEntityStaticPre(final Entity entity, final float n, final boolean b, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (entity instanceof EntityPlayerSP) {
            final EntityPlayerSP player = (EntityPlayerSP)entity;
            naphthav2$cachedRotationPitch = player.rotationPitch;
            naphthav2$cachedPrevRotationPitch = player.prevRotationPitch;
            player.prevRotationPitch = RotationUtils.prevTruePitch;
            player.rotationPitch = RotationUtils.truePitch;
        }
    }

    @Inject(method = "renderEntityStatic", at = @At("RETURN"))
    public void renderEntityStaticPost(final Entity entity, final float n, final boolean b, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (entity instanceof EntityPlayerSP) {
            final EntityPlayerSP player = (EntityPlayerSP)entity;
            player.prevRotationPitch = this.naphthav2$cachedPrevRotationPitch;
            player.rotationPitch = this.naphthav2$cachedRotationPitch;
        }
    }
}
