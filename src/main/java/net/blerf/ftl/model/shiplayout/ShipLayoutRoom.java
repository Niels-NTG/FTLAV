package net.blerf.ftl.model.shiplayout;


public class ShipLayoutRoom {

    /**
     * 0-based Nth square from the left (without layout offset).
     */
    public final int locationX;

    /**
     * 0-based Nth square from the top (without layout offset).
     */
    public final int locationY;

    /**
     * Horizontal count of tiles.
     */
    public final int squaresH;

    /**
     * Vertical count of tiles.
     */
    public final int squaresV;


    public ShipLayoutRoom(int locationX, int locationY, int squaresH, int squaresV) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.squaresH = squaresH;
        this.squaresV = squaresV;
    }
}
