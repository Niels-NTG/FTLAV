package net.blerf.ftl.parser.sectormap;

import java.awt.Point;
import net.blerf.ftl.xml.FTLEvent;


/**
 * Beacon values FTL would generate at runtime.
 * <p>
 * FTL populates these values based on the sector layout seed. They are not
 * serialized into a BeaconState.
 * <p>
 * When FTL generates a map, it iterates over a rectangular grid, randomly
 * skipping spaces. When it loads a savedgame's list of BeaconStates, they are
 * associated with the nth.
 */
public class GeneratedBeacon {

    protected int throbTicks = 0;
    public int col = 0;
    public int row = 0;
    protected int x = 0;
    protected int y = 0;
    protected FTLEvent event = null;

    /* Distance from begin to finish, or -1 if not part of an optimal path */
    public int distance = -1;

    /**
     * Sets time elapsed while this beacon's 'under attack' throbber
     * oscillates.
     * <p>
     * This counts from 0 (normal) to 1000 (glowing) to 2000 (gone again).
     * <p>
     * FTL randomly assigns values to beacons, so they won't glow in sync.
     *
     * @see SavedGameParser.BeaconState#setUnderAttack(boolean)
     */
    public void setThrobTicks(int n) {
        throbTicks = n;
    }

    public int getThrobTicks() {
        return throbTicks;
    }

    /**
     * Sets the position in the grid
     */
    public void setGridPosition(int newCol, int newRow) {
        col = newCol;
        row = newRow;
    }

    public Point getGridPosition() {
        return new Point(col, row);
    }

    /**
     * Sets the pixel location of this beacon.
     * <p>
     * This offset is from a point within the sector map window.
     */
    public void setLocation(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * Sets the event for this beacon
     */
    public void setEvent(FTLEvent newEvent) {
        event = newEvent;
    }

    public FTLEvent getEvent() {
        return event;
    }


}
