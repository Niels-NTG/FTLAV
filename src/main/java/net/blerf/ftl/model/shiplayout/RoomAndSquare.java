package net.blerf.ftl.model.shiplayout;


/**
 * An immutable int pair, suitable for use as a Map key, unlike Point.
 */
public class RoomAndSquare {

    public final int roomId;
    public final int squareId;
    private final int hash;


    public RoomAndSquare(int roomId, int squareId) {
        this.roomId = roomId;
        this.squareId = squareId;

        // Pre-calculate the hashCode, since this is immutable.
        int sum = roomId + squareId;
        hash = sum * (sum + 1) / 2 + roomId;
    }

    public RoomAndSquare(RoomAndSquare srcRas) {
        this(srcRas.roomId, srcRas.squareId);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoomAndSquare)) return false;
        RoomAndSquare otherRas = (RoomAndSquare) o;
        return (roomId == otherRas.roomId && squareId == otherRas.squareId);
    }

    /**
     * Returns a hash code value for the object.
     * <p>
     * The algorithm is copied from java.awt.Dimension, as suggested here:
     * https://stackoverflow.com/a/16449092
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
