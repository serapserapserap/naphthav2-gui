package seraph.base.mixins.Event;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import seraph.base.Map.Event.PacketEvent;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0*", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> packet, CallbackInfo ci) {
        if (new PacketEvent.PacketReceiveEvent(packet, (NetworkManager) (Object) (this)).post()) ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (new PacketEvent.PacketSendEvent(packet, (NetworkManager) (Object) (this)).post()) ci.cancel();
    }
}
