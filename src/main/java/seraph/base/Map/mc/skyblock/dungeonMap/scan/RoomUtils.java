package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static seraph.base.Naphthav2.mc;

public class RoomUtils {
    public static Set<RoomConfigData> getRoomList() {
        Set<RoomConfigData> roomList;
        try {
            InputStreamReader reader = new InputStreamReader(
                    mc.getResourceManager()
                            .getResource(new ResourceLocation("fish", "rooms.json"))
                            .getInputStream()
            );

            Gson gson = new Gson();
            Type setType = new TypeToken<Set<RoomConfigData>>() {}.getType();
            roomList = gson.fromJson(reader, setType);
        } catch (JsonSyntaxException | IOException | JsonIOException e) {
            throw new RuntimeException(e);
        }

        return roomList.isEmpty() ? Collections.emptySet() : roomList;
    }

    public static Set<RoomConfigData> roomList = getRoomList();

    public static RoomConfigData getRoomConfigData(int core) {
        for (RoomConfigData roomConfigData : roomList) {
            if (roomConfigData.getCores().contains(core)) {
                return roomConfigData;
            }
        }
        return null;
    }

    public static BlockPos getRotatedPos(BlockPos blockPos, int rotation) {
        if (rotation == 90 || rotation == -270) {
            return new BlockPos(-blockPos.getZ(), blockPos.getY(), blockPos.getX());
        } else if (rotation == 180 || rotation == -180) {
            return new BlockPos(-blockPos.getX(), blockPos.getY(), -blockPos.getZ());
        } else if (rotation == 270 || rotation == -90) {
            return new BlockPos(blockPos.getZ(), blockPos.getY(), -blockPos.getX());
        } else {
            return blockPos;
        }
    }

    public static Set<BlockPos> getRotatedPosSet(Set<BlockPos> posSet, int rotation) {
        Set<BlockPos> returnSet = new HashSet<>();
        for (BlockPos pos : posSet) {
            returnSet.add(getRotatedPos(pos, rotation));
        }
        return returnSet;
    }
}
