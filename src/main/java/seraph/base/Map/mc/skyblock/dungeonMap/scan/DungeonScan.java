package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import java.util.*;

import static seraph.base.Naphthav2.mc;

public class DungeonScan {

    public static void scanDungeon() {
        boolean allLoaded = true;
        boolean updateConnection = false;

        for (int column = 0; column <= 10; column++) {
            for (int row = 0; row <= 10; row++) {
                int xPos = Dungeon.startX + column * (Dungeon.roomSize >> 1);
                int zPos = Dungeon.startZ + row * (Dungeon.roomSize >> 1);

                if (!mc.theWorld.getChunkFromChunkCoords(xPos >> 4, zPos >> 4).isLoaded()) {
                    allLoaded = false;
                    continue;
                }
                Tile tile = Dungeon.getDungeonTile(column, row);
                if (tile != null && tile.scanned) continue;
                Tile newTile = getRoomFromWorld(xPos, zPos, column, row);
                if (newTile != null) {
                    Tile oldTile = Dungeon.getDungeonTile(column, row);
                    if (oldTile != null) {
                        if (oldTile instanceof Room && newTile instanceof Room) {
                            ((Room) oldTile).data.setConfigData(((Room) newTile).data.getConfigData());
                            ((Room) oldTile).core = ((Room) newTile).core;
                        } else {
                            oldTile.scanned = true;
                        }
                    } else {
                        Dungeon.setDungeonTile(column, row, newTile);
                        if (newTile instanceof Room && ((Room) newTile).data.getType() == RoomType.NORMAL) {
                            updateConnection = true;
                        }
                    }
                }
            }
        }

        if (updateConnection) {
            MapUpdate.synchConnectedRooms();
        }

        if (allLoaded) {
            Dungeon.fullyScanned = true;
            Dungeon.totalSecrets = Dungeon.getDungeonTileList(Room.class).stream()
                    .filter(Room::isUnique)
                    .mapToInt(r -> r.data.getMaxSecrets() != null ? r.data.getMaxSecrets() : 0)
                    .sum();
            Dungeon.cryptCount = Dungeon.getDungeonTileList(Room.class).stream()
                    .filter(Room::isUnique)
                    .mapToInt(r -> r.data.getCrypts() != null ? r.data.getCrypts() : 0)
                    .sum();
            Dungeon.trapType = Dungeon.getDungeonTileList(Room.class).stream()
                    .filter(r -> r.data.getType() == RoomType.TRAP)
                    .map(r -> r.data.getName())
                    .findFirst().orElse("");
            Dungeon.witherDoors = Dungeon.getDungeonTileList(Door.class).stream()
                    .filter(d -> d.type == DoorType.WITHER)
                    .toArray().length + 1;
            Dungeon.puzzles.addAll(Arrays.asList((String[])Dungeon.getDungeonTileList(Room.class).stream()
                    .filter(r -> r.data.getType() == RoomType.PUZZLE)
                    .map(r -> r.data.getType()).toArray()));
        }
    }

    public static Map<Room, Integer> rooms = new HashMap<>();

    public static void updateBoss() {
        switch (Dungeon.floor) {
            case 1:
                Dungeon.inBoss = mc.thePlayer.posX > -71 && mc.thePlayer.posZ > -39;
                break;
            case 2:
            case 3:
            case 4:
                Dungeon.inBoss = mc.thePlayer.posX > -39 && mc.thePlayer.posZ > -39;
                break;
            case 5:
            case 6:
                Dungeon.inBoss = mc.thePlayer.posX > -39 && mc.thePlayer.posZ > -7;
                break;
            case 7:
                Dungeon.inBoss = mc.thePlayer.posX > -7 && mc.thePlayer.posZ > -7;
                break;
        }
    }

    public static void scanDungeonRotation() {
        boolean allLoaded = true;

        for (Room room : Dungeon.getDungeonTileList(Room.class)) {
            if (RoomUtils.roomList.stream().anyMatch(r -> Objects.equals(r.getCores().get(0), room.core))) {
                if (!rooms.containsKey(room)) {
                    Integer rotation = getAbsoluteRoomRotation(room);
                    if (rotation != null) {
                        rooms.put(room, rotation);
                    } else {
                        allLoaded = false;
                    }
                }
            }
        }

        if (rooms.entrySet().stream().noneMatch(entry -> entry.getKey().data.getType() == RoomType.BOSS)) {
            Integer floor = Dungeon.getFloor();
            if (floor != -1) {
                Room bossRoom = new Room(0,0, new RoomData(Dungeon.getFloorNice() + " Boss", RoomType.BOSS));
                rooms.put(bossRoom, 0);
            }
        }

        if (Dungeon.fullyScanned && allLoaded) {
            Dungeon.fullyScammedRotation = true;
        }
    }

    private static Integer getAbsoluteRoomRotation(Tile room) {
        if (!(room instanceof Room)) return null;

        if (((Room) room).data.getType() == RoomType.ENTRANCE) {
            List<int[]> shifts = Arrays.asList(
                    new int[]{0, -7},
                    new int[]{7, 0},
                    new int[]{0, 7},
                    new int[]{-7, 0}
            );

            for (int i = 0; i < shifts.size(); i++) {
                int[] pair = shifts.get(i);
                BlockPos pos = new BlockPos(room.x + pair[0], 70, room.z + pair[1]);
                if (mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
                    return i * 90;
                }
            }
        }
        return null;
    }

    private static Tile getRoomFromWorld(int x, int z, int column, int row) {
        if (isColumnAir(x, z)) return null;
        boolean rowEven = (row & 1) == 0;
        boolean columnEven = (column & 1) == 0;

        if (rowEven && columnEven) {
            int core = getCore(x, z);
            RoomConfigData configData = RoomUtils.getRoomConfigData(core);
            if (configData != null) {
                RoomData data = new RoomData(configData);
                Room room = new Room(x, z, data);
                room.core = core;
                return room;
            }
        } else if (!rowEven && !columnEven) {
            Tile tile = Dungeon.getDungeonTile(column - 1, row - 1);
            if (tile instanceof Room) {
                Room room = new Room(x, z, ((Room) tile).data);
                room.isSeparator = true;
                return room;
            }
        } else if (isDoor(x, z)) {
            IBlockState bState = mc.theWorld.getBlockState(new BlockPos(x, 69, z));
            DoorType doorType;
            if (bState.getBlock() == Blocks.coal_block) doorType = DoorType.WITHER;
            else if (bState.getBlock() == Blocks.monster_egg) doorType = DoorType.ENTRANCE;
            else if (bState.getBlock() == Blocks.stained_hardened_clay && Blocks.stained_hardened_clay.getMetaFromState(bState) == 14)
                doorType = DoorType.BLOOD;
            else doorType = DoorType.NORMAL;
            return new Door(x, z, doorType);
        }
        return null;
    }

    public static Pair<Integer, Integer> getRoomCentre(int posX, int posZ) {
        int roomX = (posX - Dungeon.startX) >> 5;
        int roomZ = (posZ - Dungeon.startZ) >> 5;
        int x = 32 * roomX + Dungeon.startX;
        if (x < posX - 16 || x > posX + 16) x += 32;
        int z = 32 * roomZ + Dungeon.startZ;
        if (z < posZ - 16 || z > posZ + 16) z += 32;
        return new Pair<>(x, z);
    }

    private static boolean isColumnAir(int x, int z) {
        for (int y = 12; y <= 140; y++) {
            if (mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.air) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDoor(int x, int z) {
        boolean xPlus4 = isColumnAir(x + 4, z);
        boolean xMinus4 = isColumnAir(x - 4, z);
        boolean zPlus4 = isColumnAir(x, z + 4);
        boolean zMinus4 = isColumnAir(x, z - 4);
        return (xPlus4 && xMinus4 && !zPlus4 && !zMinus4) || (!xPlus4 && !xMinus4 && zPlus4 && zMinus4);
    }

    public static int getCore(int x, int z) {
        List<Integer> blocks = new ArrayList<>();
        for (int y = 140; y >= 12; y--) {
            int id = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
            if (id != 5 && id != 54) {
                blocks.add(id);
            }
        }
        return blocks.toString().hashCode();
    }

    public static Room getCurrentRoom() {
        Tile room;
        if (Dungeon.inBoss) {
            int floor = Dungeon.floor;
            if (floor != -1) {
                room = new Room(0,0, new RoomData(Dungeon.getFloorNice() + " Boss", RoomType.BOSS));
            } else {
                room = null;
            }
        } else {
            int x = ((int) (mc.thePlayer.posX - Dungeon.startX + 15) >> 5);
            int z = ((int) (mc.thePlayer.posZ - Dungeon.startZ + 15) >> 5);
            room = Dungeon.getDungeonTile(x * 2, z * 2);
        }
        return (room instanceof Room) ? (Room) room : null;
    }

    public static Pair<Room, Integer> getCurrentRoomPair() {
        Room room = getCurrentRoom();
        if (!(room instanceof Room)) {
            return null;
        }
        if (room.data.getType() == RoomType.BOSS) {
            return new Pair<>(room, 0);
        } else {
            return rooms.entrySet().stream()
                    .filter(entry -> entry.getKey().data.getName().equals(room.data.getName()))
                    .findFirst()
                    .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                    .orElse(null);
        }
    }
}