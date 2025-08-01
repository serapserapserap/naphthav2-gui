package seraph.base.mixins.Event;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.DrawPlayerEvent;

@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci) {
        DrawPlayerEvent e = new DrawPlayerEvent((EntityPlayer) entityIn);
        e.post();
        naphthav2$scalePlayer(e.scalex, e.scaley, e.scalez);
    }

    @Unique
    private void naphthav2$scalePlayer(float x, float y, float z){
        GL11.glScalef(x,y,z);
        GL11.glTranslatef(0,(1.5f-(1.5f*y))/y,0);
    }
}
