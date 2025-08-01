package seraph.base.Map.mc.skyblock.dungeonMap.scan;

public class Door extends Tile{
    public DoorType type;
    public boolean opened = false;
    public Door(final int x, final int y, final DoorType type) {
        super(x,y);
        this.type = type;
    }
}
