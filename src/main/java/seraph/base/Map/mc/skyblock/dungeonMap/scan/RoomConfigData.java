package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import java.util.List;

public class RoomConfigData {
    private final String name;
    private final RoomType type;
    private final int secrets;
    private final int size;
    private final List<Integer> cores;
    private final int crypts;
    private final int trappedChests;

    public RoomConfigData(String name, RoomType type, int secrets, int size, List<Integer> cores, int crypts, int trappedChests) {
        this.name = name;
        this.type = type;
        this.secrets = secrets;
        this.size = size;
        this.cores = cores;
        this.crypts = crypts;
        this.trappedChests = trappedChests;
    }

    public String getName() {
        return name;
    }

    public RoomType getType() {
        return type;
    }

    public int getSecrets() {
        return secrets;
    }

    public int getSize() {
        return size;
    }

    public List<Integer> getCores() {
        return cores;
    }

    public int getCrypts() {
        return crypts;
    }

    public int getTrappedChests() {
        return trappedChests;
    }
}
