package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import scala.actors.threadpool.Arrays;
import seraph.base.Naphthav2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static seraph.base.Map.mc.Gui.getScoreboard;
import static seraph.base.Map.mc.Gui.removeSHIT;

public class Dungeon {
    public static final int roomSize = 32;
    public static final int startX = -185;
    public static final int startZ = -185;
    public static boolean fullyScanned = false;
    public static boolean fullyScammedRotation = false;
    public static int totalSecrets = 0;
    public static int cryptCount = 0;
    public static String trapType = "";
    public static int witherDoors = 0;
    public static final List<String> puzzles = new ArrayList<>();
    public static int floor = 0;
    public static boolean inBoss = false;
    public static Pair<Room, Integer> currentPair = null;
    private static Tile[] dungeonList = new Tile[121];

    public static void reset() {
        fullyScanned = false;
        fullyScammedRotation = false;
        totalSecrets = 0;
        cryptCount = 0;
        trapType = "";
        witherDoors = 0;
        puzzles.clear();
        floor = 0;
        inBoss = false;
        currentPair = null;
        dungeonList = new Tile[121];
        DungeonScan.rooms.clear();
    }

    public static Tile getDungeonTile(final int row, final int column) {
        if (row < 0 || row > 10 || column < 0 || column > 10) return null;
        return dungeonList[column * 11 + row];
    }

    public static boolean setDungeonTile(int column, int row, Tile tile) {
        if (row < 0 || row > 10 || column < 0 || column > 10) {
            return false;
        }
        dungeonList[column * 11 + row] = tile;
        return true;
    }

    public static List<Tile> getDungeonTileList() {
        return (List<Tile>) Arrays.asList(dungeonList);
    }

    public static <T extends Tile> List<T> getDungeonTileList(Class<T> clazz) {
        return getDungeonTileList().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    static Pattern pattern = Pattern.compile("Catacombs \\((\\w)(\\w)");

    public static int getFloor(){
        for(String line : getScoreboard()){
            String stripped = removeSHIT(line);
            Matcher matcher = pattern.matcher(stripped);
            if(matcher.find()){
                boolean isMastermode = matcher.group(1).equalsIgnoreCase("m");
                int floor = Integer.parseInt(matcher.group(2)) + (isMastermode ? 10 : 0);
                Naphthav2.sendModMsg(floor > 10 ? "m" + (floor - 10) : "f" + (floor));
                return floor;
            }
        }
        return -1;
    }

    public static String getFloorNice() {
        int floor = getFloor();
        return floor != -1 ? (floor > 10 ? "m" + (floor - 10) : "f" + (floor)) : null;
    }
}