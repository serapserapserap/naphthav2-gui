package seraph.base.Map.mc.skyblock.dungeonMap.scan;

import seraph.base.Map.Event.RoomStateChangeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class MapUpdate {

    public static void calibrate() {
        Integer roomSize = MapUtils.getRoomSizeFromMap();
        if (roomSize == null) {
            return;
        }
        MapUtils.roomSize = roomSize;

        Pair<Integer, Integer> startCorner = MapUtils.getStartCornerFromMap();
        if (startCorner == null) {
            return;
        }
        MapUtils.startCorner = startCorner;

        MapUtils.coordMultiplier = (MapUtils.roomSize + 4.0) / Dungeon.roomSize;
        MapUtils.calibrated = true;
    }


    public static void synchConnectedRooms() {
        Map<RoomData, Set<RoomData>> bufferedData = new HashMap<>();
        List<Tile> dungeonTileList = Dungeon.getDungeonTileList();
        for (int index = 0; index < dungeonTileList.size(); index++) {
            Tile tile = dungeonTileList.get(index);
            if (!(tile instanceof Room)) continue;
            Room roomTile = (Room) tile;
            if (roomTile.data.getType() != RoomType.NORMAL) continue;
            if (roomTile.isSeparator) continue;
            int column = index / 11;
            int row = index % 11;
            Room leftConnector = (Room) Dungeon.getDungeonTile(column - 1, row);
            Room topConnector = (Room) Dungeon.getDungeonTile(column, row - 1);
            RoomData finalData = null;
            Set<RoomData> bufferedDataTemporary = new HashSet<>();
            boolean gotDataFromLeft = false;
            if (leftConnector != null && leftConnector.isSeparator) {
                Room leftRoom = (Room) Dungeon.getDungeonTile(column - 2, row);
                if (leftRoom != null) {
                    gotDataFromLeft = true;
                    finalData = leftRoom.data;
                    bufferedDataTemporary.add(roomTile.data);
                    leftConnector.data = leftRoom.data;
                    roomTile.data = leftRoom.data;
                }
            }
            if (topConnector != null && topConnector.isSeparator) {
                Room topRoom = (Room) Dungeon.getDungeonTile(column, row - 2);
                if (topRoom != null) {
                    if (gotDataFromLeft) {
                        bufferedDataTemporary.add(topRoom.data);
                        topRoom.data = roomTile.data;
                        topConnector.data = roomTile.data;
                    } else {
                        finalData = topRoom.data;
                        bufferedDataTemporary.add(roomTile.data);
                        topConnector.data = topRoom.data;
                        roomTile.data = topRoom.data;
                    }
                }
            }
            if (finalData != null) {
                bufferedData.computeIfAbsent(finalData, k -> new HashSet<>()).addAll(bufferedDataTemporary);
            }
        }
    }

    public void updateRooms() {
        if (!MapUtils.calibrated) return;
        byte[] mapColors = MapUtils.getMapData() != null ? MapUtils.getMapData().colors : null;
        if (mapColors == null || mapColors[0] != 0) return;
        boolean shouldConnectRooms = false;

        int startX = MapUtils.startCorner.getKey();
        int startZ = MapUtils.startCorner.getValue();
        int centerOffset = (MapUtils.roomSize >> 1);
        int increment = (MapUtils.roomSize >> 1) + 2;

        boolean scanRooms = shouldScanMapItem();

        for (int column = 0; column <= 10; column++) {
            for (int row = 0; row <= 10; row++) {
                Tile tile = Dungeon.getDungeonTile(column, row);

                // If room unknown try to get it from the map item.
                if (scanRooms && (tile == null || (tile.state == RoomState.QUESTION_MARK && !tile.scanned))) {
                    Tile newTile = getRoomFromMap(column, row, mapColors);
                    if (newTile != null) {
                        Dungeon.setDungeonTile(column, row, newTile);

                        if (newTile instanceof Room && ((Room) newTile).data.getType() == RoomType.NORMAL) {
                            shouldConnectRooms = true;
                        }

                        // Update the room size.
                        if (newTile instanceof Room && !((Room) newTile).isSeparator && ((Room) newTile).data != null && ((Room) newTile).data.getType() == RoomType.NORMAL) {
                            int size = Dungeon.getDungeonTileList(Room.class).stream()
                                    .filter(temporaryTile -> !temporaryTile.isSeparator && temporaryTile.data == ((Room) newTile).data)
                                    .collect(Collectors.toList()).size();
                            ((Room) newTile).data.setSize(size);
                        }
                    }
                }

                // Scan the room centers on the map for check marks.
                tile = Dungeon.getDungeonTile(column, row);
                if (tile != null) {
                    int centerX = startX + column * increment + centerOffset;
                    int centerZ = startZ + row * increment + centerOffset;
                    if (centerX >= 128 || centerZ >= 128) continue;

                    RoomState newState = RoomState.DISCOVERED; // Default state
                    switch (mapColors[(centerZ << 7) + centerX]) {
                        case 0:
                            newState = RoomState.UNDISCOVERED;
                            break;
                        case 85:
                            if (tile instanceof Door) {
                                newState = RoomState.DISCOVERED;
                            } else {
                                newState = RoomState.UNDISCOVERED; // should not happen
                            }
                            break;
                        case 119:
                            if (tile instanceof Room) {
                                newState = RoomState.QUESTION_MARK;
                            } else {
                                newState = RoomState.DISCOVERED; // wither door
                            }
                            break;
                        case 18:
                            if (tile instanceof Room) {
                                if (((Room) tile).data.getType() == RoomType.BLOOD) {
                                    newState = RoomState.DISCOVERED;
                                } else if (((Room) tile).data.getType() == RoomType.PUZZLE) {
                                    newState = RoomState.FAILED;
                                }
                            } else {
                                newState = RoomState.DISCOVERED;
                            }
                            break;
                        case 30:
                            if (tile instanceof Room) {
                                if (((Room) tile).data.getType() == RoomType.ENTRANCE) {
                                    newState = RoomState.DISCOVERED;
                                } else {
                                    newState = RoomState.GREEN;
                                }
                            } else {
                                newState = tile.state;
                            }
                            break;
                        case 34:
                            newState = RoomState.CLEARED;
                            break;
                        default:
                            if (tile instanceof Door) {
                                ((Door) tile).opened = true;
                            }
                            newState = RoomState.DISCOVERED;
                            break;
                    }

                    if (newState != tile.state) {
                        new RoomStateChangeEvent(tile, newState).post();
                        tile.state = newState;
                    }
                }
            }
        }

        if (shouldConnectRooms) {
            synchConnectedRooms();
        }
    }


    private Tile getRoomFromMap(int column, int row, byte[] mapColors) {

        int startX = MapUtils.startCorner.getKey();
        int startZ = MapUtils.startCorner.getValue();
        int increment = (MapUtils.roomSize >> 1) + 2;
        int centerOffset = (MapUtils.roomSize >> 1);

        int cornerX = startX + column * increment;
        int cornerZ = startZ + row * increment;

        int centerX = cornerX + centerOffset;
        int centerZ = cornerZ + centerOffset;

        if (cornerX >= 128 || cornerZ >= 128) return null;
        if (centerX >= 128 || centerZ >= 128) return null;

        int xPos = Dungeon.startX + column * (Dungeon.roomSize >> 1);
        int zPos = Dungeon.startZ + row * (Dungeon.roomSize >> 1);

        boolean rowEven = (row & 1) == 0;
        boolean columnEven = (column & 1) == 0;

        if (rowEven && columnEven) { // room
            int roomColor = mapColors[(cornerZ << 7) + cornerX] & 0xFF;
            RoomType roomType;
            switch (roomColor) {
                case 0:
                    return null;
                case 18:
                    roomType = RoomType.BLOOD;
                    break;
                case 30:
                    roomType = RoomType.ENTRANCE;
                    break;
                case 85:
                    roomType = RoomType.UNKNOWN;
                    break;
                case 63:
                    roomType = RoomType.NORMAL;
                    break;
                case 74:
                    roomType = RoomType.CHAMPION;
                    break;
                case 66:
                    roomType = RoomType.PUZZLE;
                    break;
                case 82:
                    roomType = RoomType.FAIRY;
                    break;
                case 62:
                    roomType = RoomType.TRAP;
                    break;
                default:
                    roomType = RoomType.NORMAL;
                    break;
            }

            RoomData data = new RoomData("Unknown" + column + row, roomType);
            return new Room(xPos, zPos, data);
        } else if (!rowEven && !columnEven) { // possible separator (only for 2x2)
            if ((mapColors[(centerZ << 7) + centerX] & 0xFF) != 0) {
                Tile tile = Dungeon.getDungeonTile(column - 1, row - 1);
                if (tile instanceof Room) {
                    Room room = (Room) tile;
                    Room newRoom = new Room(xPos, zPos, room.data);
                    newRoom.isSeparator = true;
                    return newRoom;
                }
            }
            return null;
        } else { // door or separator
            int mapColor = mapColors[(rowEven ? cornerZ : centerZ) << 7 + (rowEven ? centerX : cornerX)] & 0xFF;
            if (mapColor != 0) { // separator
                Tile tile = rowEven ? Dungeon.getDungeonTile(column - 1, row) : Dungeon.getDungeonTile(column, row - 1);
                if (tile instanceof Room) {
                    Room room = (Room) tile;
                    Room newRoom = new Room(xPos, zPos, room.data);
                    newRoom.isSeparator = true;
                    return newRoom;
                }
            } else { // door or nothing
                int doorColor = mapColors[(centerZ << 7) + centerX] & 0xFF;
                DoorType doorType;
                switch (doorColor) {
                    case 0:
                        return null;
                    case 119:
                        doorType = DoorType.WITHER;
                        break;
                    case 30:
                        doorType = DoorType.ENTRANCE;
                        break;
                    case 18:
                        doorType = DoorType.BLOOD;
                        break;
                    default:
                        doorType = DoorType.NORMAL;
                        break;
                }
                return new Door(xPos, zPos, doorType);
            }
        }

        return null; // Default return statement, should not be reached
    }


    private static boolean shouldScanMapItem() {
        return !Dungeon.fullyScanned && !Dungeon.inBoss && Dungeon.floor != -1;
    }

}
