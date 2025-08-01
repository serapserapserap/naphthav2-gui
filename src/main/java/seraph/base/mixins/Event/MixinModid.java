package seraph.base.mixins.Event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Naphthav2;
import seraph.base.Map.Event.HandshakeEvent;

import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public class MixinModid {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void removeModID(List<ModContainer> modList, CallbackInfo ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            HandshakeEvent e = new HandshakeEvent(modTags);
            this.modTags = e.modTags;
            modTags.remove(Naphthav2.MODID);
        }
    }
}
