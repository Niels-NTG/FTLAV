package net.blerf.ftl.model.shiplayout;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.xml.ship.ShipChassis;

@Getter
@Setter
@NoArgsConstructor
public class ShipLayout {

    /**
     * Sets a positive offset to the entire ship in square-sized (35x35) units.
     * <p>
     * This positive offset shifts right/down.
     * <p>
     * Sprite locations in saved games will have this offset baked in.
     * <p>
     * ShipChassis will further offset the ship images specifically.
     *
     * @see ShipChassis
     */
    private int offsetX = 0;
    private int offsetY = 0;
    /**
     * Sets an additional whole-ship offset in pixel units.
     * <p>
     * TODO: Reportedly horizontal doesn't apply for nearby ships!?
     */
    private int horizontal = 0;
    private int vertical = 0;
    private Rectangle shieldEllipse = new Rectangle();
    private final TreeMap<Integer, ShipLayoutRoom> roomMap = new TreeMap<>();
    private final Map<DoorCoordinate, ShipLayoutDoor> doorMap = new LinkedHashMap<>();

    /**
     * Sets the collision/orbit ellipse.
     * <p>
     * Note: This is abstract and does not affect how shields are painted.
     */
    public void setShieldEllipse(int w, int h, int x, int y) {
        shieldEllipse = new Rectangle(x, y, w, h);
    }

    /**
     * Sets a room's info.
     *
     * @param roomId     a roomId
     * @param layoutRoom room info
     */
    public void setRoom(int roomId, ShipLayoutRoom layoutRoom) {
        roomMap.put(roomId, layoutRoom);
    }

    public ShipLayoutRoom getRoom(int roomId) {
        return roomMap.get(roomId);
    }

    /**
     * Returns the highest roomId + 1.
     */
    public int getRoomCount() {
        try {
            int lastKey = roomMap.lastKey();
            return (lastKey + 1);
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    /**
     * Sets a door's info.
     *
     * @param wallX      the 0-based Nth wall from the left
     * @param wallY      the 0-based Nth wall from the top
     * @param vertical   1 for vertical wall coords, 0 for horizontal
     * @param layoutDoor dooor info
     * @see ShipLayoutDoor
     */
    public void setDoor(int wallX, int wallY, int vertical, ShipLayoutDoor layoutDoor) {
        DoorCoordinate doorCoord = new DoorCoordinate(wallX, wallY, vertical);

        doorMap.put(doorCoord, layoutDoor);
    }

    public ShipLayoutDoor getDoor(int wallX, int wallY, int vertical) {
        return doorMap.get(new DoorCoordinate(wallX, wallY, vertical));
    }

    public int getDoorCount() {
        return doorMap.size();
    }

    /**
     * Returns the map containing this layout's door info.
     * <p>
     * Keys are in the order of the original layout config file.
     * That is NOT the same order as doors in saved games.
     */
    public Map<DoorCoordinate, ShipLayoutDoor> getDoorMap() {
        return doorMap;
    }

    /**
     * Returns a list of roomIds connected via doors to a given room.
     * <p>
     * This is not based on any algorithm in FTL.
     * <p>
     * Note: Doors onmodded ships may connect spacially disperate rooms.
     */
    public List<Integer> getAdjacentRoomIds(int roomId) {
        List<Integer> result = new ArrayList<Integer>();

        for (ShipLayoutDoor layoutDoor : doorMap.values()) {
            if (layoutDoor.roomIdA == roomId && layoutDoor.roomIdB != -1) {
                result.add(layoutDoor.roomIdB);
            } else if (layoutDoor.roomIdB == roomId && layoutDoor.roomIdA != -1) {
                result.add(layoutDoor.roomIdA);
            }
        }

        return result;
    }
}
