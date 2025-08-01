package seraph.base.Map.mc.skyblock.dungeonMap.scan;

public class Tile {
    public RoomState state;
    public boolean visited;
    public boolean scanned;
    public int x;
    public int z;
    public Tile(final int x, final int z) {
        this.x = x;
        this.z = z;
        this.state = RoomState.UNDISCOVERED;
        this.visited = false;
        this.scanned = true;
    }
    public int getRow() {
        return (z - Dungeon.startZ) >> 4;
    }

    /**
     * Column in the dungeonList
     */
    public int getColumn() {
        return (x - Dungeon.startX) >> 4;
    }

    public boolean isUniqueScannedRoom() {
        return this instanceof Room && ((Room)this).isUnique;
    }
}
