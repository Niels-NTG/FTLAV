package net.blerf.ftl.model.shiplayout;


/**
 * A door connects two rooms, or one room to vacuum.
 */
public class ShipLayoutDoor {

    /**
     * An adjacent roomId, or -1 for vacuum.
     */
    public final int roomIdA;

    /**
     * An adjacent roomId, or -1 for vacuum.
     */
    public final int roomIdB;


    public ShipLayoutDoor(int roomIdA, int roomIdB) {
        this.roomIdA = roomIdA;
        this.roomIdB = roomIdB;
    }
}
