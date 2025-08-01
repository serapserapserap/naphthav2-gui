package seraph.base.Map.Event;

import seraph.base.Map.mc.skyblock.dungeonMap.scan.Room;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;

public class RoomChangeEvent extends Event {
    private final Pair<Room, Integer> newRoomPair;
    private final Pair<Room, Integer> oldRoomPair;

    public RoomChangeEvent(Pair<Room, Integer> newRoomPair, Pair<Room, Integer> oldRoomPair) {
        this.newRoomPair = newRoomPair;
        this.oldRoomPair = oldRoomPair;
    }

    public Pair<Room, Integer> getNewRoomPair() {
        return newRoomPair;
    }

    public Pair<Room, Integer> getOldRoomPair() {
        return oldRoomPair;
    }
}
