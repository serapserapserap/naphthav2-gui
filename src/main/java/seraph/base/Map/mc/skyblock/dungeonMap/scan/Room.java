package seraph.base.Map.mc.skyblock.dungeonMap.scan;

public class Room extends Tile {
    public RoomData data;
    public Integer core;
    public boolean isSeparator = false;
    public boolean isUnique = false;

    public Room(int x, int z, RoomData data) {
        super(x, z);
        this.data = data;
    }
    public Room(int x, int z, RoomConfigData configData) {
        this(x, z, new RoomData(configData));
    }

    public boolean isUnique() {
        return this.isUnique;
    }
}
