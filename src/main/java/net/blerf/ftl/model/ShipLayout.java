package net.blerf.ftl.model;

import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.NoSuchElementException;


public class ShipLayout {
	public enum RoomInfo { LOCATION_X, LOCATION_Y, SQUARES_H, SQUARES_V }
	public enum DoorInfo { ROOM_ID_A, ROOM_ID_B }

	private int offsetX = 0, offsetY = 0, horizontal = 0, vertical = 0;
	private Rectangle shieldEllipse = new Rectangle();
	private final TreeMap<Integer, EnumMap<RoomInfo,Integer>> roomMap = new TreeMap<>();
	private final LinkedHashMap<DoorCoordinate, EnumMap<DoorInfo,Integer>> doorMap = new LinkedHashMap<>();

	/**
	 * Constructs a layout with uninteresting defaults.
	 */
	public ShipLayout() {}

	public void setOffsetX(int n) { offsetX = n; }
	public void setOffsetY(int n) { offsetY = n; }
	public void setHorizontal(int n) { horizontal = n; }
	public void setVertical(int n) { vertical = n; }

	public void setShieldEllipse(int w, int h, int x, int y) {
		shieldEllipse = new Rectangle(x, y, w, h);
	}

	public int getOffsetX() { return offsetX; }
	public int getOffsetY() { return offsetY; }
	public int getHorizontal() { return horizontal; }
	public int getVertical() { return vertical; }
	public Rectangle getShieldEllipse() { return shieldEllipse; }

	/**
	 * Sets a room's info.
	 *
	 * @param roomId a roomId
	 * @param locationX 0-based Nth square from the left (without layout offset)
	 * @param locationY 0-based Nth square from the top (without layout offset)
	 * @param squaresH horizontal count of tiles
	 * @param squaresV certical count of tiles
	 */
	public void setRoom(int roomId, int locationX, int locationY, int squaresH, int squaresV) {
		Integer roomIdObj = roomId;
		EnumMap<RoomInfo,Integer> infoMap = new EnumMap<>(RoomInfo.class);
		infoMap.put(RoomInfo.LOCATION_X, locationX);
		infoMap.put(RoomInfo.LOCATION_Y, locationY);
		infoMap.put(RoomInfo.SQUARES_H, squaresH);
		infoMap.put(RoomInfo.SQUARES_V, squaresV);
		roomMap.put(roomIdObj, infoMap);
	}

	public EnumMap<RoomInfo, Integer> getRoomInfo(int roomId) {
		return roomMap.get(roomId);
	}

	/**
	 * Returns the highest roomId + 1.
	 */
	public int getRoomCount() {
		try {
			return 1 + roomMap.lastKey();
		} catch (NoSuchElementException e) {
			return 0;
		}
	}

	/**
	 * Sets a door's info.
	 *
	 * @param wallX the 0-based Nth wall from the left
	 * @param wallY the 0-based Nth wall from the top
	 * @param vertical 1 for vertical wall coords, 0 for horizontal
	 * @param roomIdA an adjacent roomId, or -1 for vacuum
	 * @param roomIdB an adjacent roomId, or -1 for vacuum
	 */
	public void setDoor(int wallX, int wallY, int vertical, int roomIdA, int roomIdB) {
		DoorCoordinate doorCoord = new DoorCoordinate(wallX, wallY, vertical);
		EnumMap<DoorInfo, Integer> infoMap = new EnumMap<>(DoorInfo.class);
		infoMap.put(DoorInfo.ROOM_ID_A, roomIdA);
		infoMap.put(DoorInfo.ROOM_ID_B, roomIdB);
		doorMap.put(doorCoord, infoMap);
	}

	public EnumMap<DoorInfo, Integer> getDoorInfo(int wallX, int wallY, int vertical) {
		return doorMap.get(new DoorCoordinate(wallX, wallY, vertical));
	}

	public int getDoorCount() {
		return doorMap.size();
	}

	/**
	 * Returns the map containing this layout's door info.
	 *
	 * Keys are in the order of the original layout config file.
	 * That is NOT the same order as doors in saved games.
	 */
	public LinkedHashMap<DoorCoordinate, EnumMap<DoorInfo,Integer>> getDoorMap() {
		return doorMap;
	}



	// Regular int arrays don't override the methods needed for
	// use as Map keys, testing for identity instead of equality.
	public static class DoorCoordinate {
		public int x = 0;
		public int y = 0;
		public int v = 0;

		public DoorCoordinate(int x, int y, int v) {
			this.x = x;
			this.y = y;
			this.v = v;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof DoorCoordinate)) return false;
			DoorCoordinate d = (DoorCoordinate)o;
			return (x==d.x && y==d.y && v==d.v);
		}

		@Override
		public int hashCode() {
			return mangle(x) | (mangle(y) << 1) | (mangle(v) << 2);
		}

		// Use Z-Order Curve to interleve coords' bits for uniqueness.
		// http://stackoverflow.com/questions/9858376/hashcode-for-3d-integer-coordinates-with-high-spatial-coherence
		// http://www.opensourcescripts.com/info/interleave-bits--aka-morton-ize-aka-z-order-curve-.html
		private int mangle(int n) {
			n &= 0x000003ff;
			n = (n ^ (n << 16)) & 0xff0000ff;
			n = (n ^ (n <<  8)) & 0x0300f00f;
			n = (n ^ (n <<  4)) & 0x030c30c3;
			n = (n ^ (n <<  2)) & 0x09249249;
			return n;
		}
	}
}
