package seraph.base.mixins.Event;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.RenderItemEvent;


@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void transformFirstPersonItem(float equippedProgress, float swingProgress);
    @Inject(method = "transformFirstPersonItem",at = @At("HEAD"),cancellable = true)
    public void transformFirstPersonItem(float equipProgress, float swingProgress, CallbackInfo ci){
        if(new RenderItemEvent.TransformFirstPersonEvent(this.itemToRender, equipProgress, swingProgress).post()) {
            ci.cancel();
        }
    }

    @Inject(method = "performDrinking", at = @At("HEAD"), cancellable = true)
    public void preformDrinking(AbstractClientPlayer clientPlayer, float partialTicks, CallbackInfo ci) {
        if(new RenderItemEvent.PreformDrinkingEvent(partialTicks).post()) {
            ci.cancel();
        }
    }

    @Inject(method = "doItemUsedTransformations",at = @At("HEAD"),cancellable = true)
    public void doItemUsedTransformations(float swingProgress, CallbackInfo ci){
        if(new RenderItemEvent.ItemUsedTransformationEvent(this.itemToRender, swingProgress).post()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPlayerArm",at = @At("HEAD"), cancellable = true)
    private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress, CallbackInfo ci) {
        if(new RenderItemEvent.RenderHandEvent(equipProgress, swingProgress).post()) {
            ci.cancel();
        }
    }

    @Inject(method = "doBlockTransformations", at = @At("HEAD"),cancellable = true)
    public void doBlockTransformationsInjection(CallbackInfo ci) {
        if(new RenderItemEvent.ItemBlockTransformationEvent().post()) {
            ci.cancel();
        }
    }

    @Inject(method = "doBowTransformations", at = @At("HEAD"), cancellable = true)
    public void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if(new RenderItemEvent.BowTransformationEvent(partialTicks).post()) {
            ci.cancel();
        }
    }
}