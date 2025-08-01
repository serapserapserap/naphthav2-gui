package seraph.base.Map.Event;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public abstract class PacketEvent extends Event {

    public Packet<?> packet;
    public final NetworkManager netManager;

    public PacketEvent(Packet<?> packet, NetworkManager netManager) {
        this.packet = packet;
        this.netManager = netManager;
    }

    public static class PacketReceiveEvent extends PacketEvent {
        public PacketReceiveEvent(Packet<?> packet, NetworkManager networkManager) {
            super(packet, networkManager);
        }
    }

    public static class PacketSendEvent extends PacketEvent {
        public PacketSendEvent(Packet<?> packet, NetworkManager networkManager) {
            super(packet, networkManager);
        }
    }
}