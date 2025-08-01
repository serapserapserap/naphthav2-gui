package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;

import java.util.regex.Pattern;

import static seraph.base.Naphthav2.mc;

public class MapUtils {
    public static Pair<Integer, Integer> startCorner = new Pair<>(5,5);

    /*
     * Rooms have size 18x18 pixels on the vanilla map in floors 1..3, and 16x16 obove that.
     */
    public static int roomSize = 16;
    public static boolean calibrated = false;
    public static double coordMultiplier = 20.0/32.0;
    private static final Pattern regex16 = Pattern.compile((char) 30 + "{16}");
    private static final Pattern regex18 = Pattern.compile((char) 30 + "{18}");


    public static String getColorString() {
        MapData mapData = getMapData();
        if (mapData == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (byte color : mapData.colors) {
            result.append((char) (int) color);
        }
        return result.toString();
    }
    public static Integer getRoomSizeFromMap() {
        String mapColorString = getColorString();
        if (mapColorString == null) {
            return null;
        }

        if (regex18.matcher(mapColorString).find()) {
            return 18;
        } else if (regex16.matcher(mapColorString).find()) {
            return 16;
        } else {
            return null;
        }
    }

    public static MapData getMapData() {
        if (mc.thePlayer == null) {
            return null;
        }
        ItemStack map = mc.thePlayer.inventory.getStackInSlot(8);
        if (map == null || !(map.getItem() instanceof ItemMap) || !map.getDisplayName().contains("Magical Map")) {
            return null;
        }
        return ((ItemMap) map.getItem()).getMapData(map, mc.theWorld);
    }


    public static Pair<Integer, Integer> getStartCornerFromMap() {
        if (Dungeon.floor == 1) {
            return new Pair<>(22, 11);
        }

        String mapColorString = getColorString();
        if (mapColorString == null) {
            return null;
        }

        Integer greenCorner = regex18.matcher(mapColorString).find() ? Integer.valueOf(mapColorString.indexOf((char) 30)) :
                regex16.matcher(mapColorString).find() ? mapColorString.indexOf((char) 30) : null;
        if (greenCorner == null) {
            return null;
        }

        int increment = MapUtils.roomSize + 4;

        return new Pair<>(greenCorner % 128 % increment, greenCorner / 128 % increment);
    }
}
