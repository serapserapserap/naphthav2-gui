package seraph.base.mixins.Event;

import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.GuiScreenEvent;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void drawScreenPreEvent(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if(new GuiScreenEvent.Draw(mouseX,mouseY,partialTicks).post()) {
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreenPostEvent(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        new GuiScreenEvent.PostDraw(mouseX,mouseY,partialTicks).post();
    }

    @Inject(method = "handleMouseInput", at = @At("HEAD"), cancellable = true)
    public void handleMouseInputPre(CallbackInfo ci) {
        if(new GuiScreenEvent.HandleInputPre().post()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleMouseInput", at = @At("TAIL"))
    public void handleMouseInputPost(CallbackInfo ci) {
        new GuiScreenEvent.HandleInputPost().post();
    }
}
