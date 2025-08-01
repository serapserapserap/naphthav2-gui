package seraph.base.Map.Event;

import seraph.base.Map.mc.skyblock.dungeonMap.scan.RoomState;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Tile;

public class RoomStateChangeEvent extends Event{
    private final Tile tile;
    private final RoomState newState;

    public RoomStateChangeEvent(Tile tile, RoomState newState) {
        this.tile = tile;
        this.newState = newState;
    }

    public Tile getTile() {
        return tile;
    }

    public RoomState getNewState() {
        return newState;
    }
}
