package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import java.util.List;

public class RoomData {
    private boolean hasMimic = false;
    private RoomConfigData configData;

    private String name;
    private RoomType type;
    private Integer maxSecrets;
    private Integer size;
    private int currentSecrets = 0;

    public RoomData() {
        this("Unknown", RoomType.UNKNOWN);
    }

    public RoomData(RoomConfigData configData) {
        this();
        this.configData = configData;
    }

    public RoomData(String name, RoomType type) {
        this.name = name;
        this.type = type;
    }

    public boolean hasMimic() {
        return hasMimic;
    }

    public void setHasMimic(boolean hasMimic) {
        this.hasMimic = hasMimic;
    }

    public RoomConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(RoomConfigData configData) {
        this.configData = configData;
    }

    public String getName() {
        return configData != null ? configData.getName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomType getType() {
        return configData != null ? configData.getType() : type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public Integer getMaxSecrets() {
        return configData != null ? configData.getSecrets() : maxSecrets;
    }

    public void setMaxSecrets(Integer maxSecrets) {
        this.maxSecrets = maxSecrets;
    }

    public Integer getSize() {
        return configData != null ? configData.getSize() : size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Integer> getCores() {
        return configData != null ? configData.getCores() : null;
    }

    public Integer getCrypts() {
        return configData != null ? configData.getCrypts() : null;
    }

    public Integer getTrappedChests() {
        return configData != null ? configData.getTrappedChests() : null;
    }

    public int getCurrentSecrets() {
        return currentSecrets;
    }

    public void setCurrentSecrets(int currentSecrets) {
        this.currentSecrets = currentSecrets;
    }
}

