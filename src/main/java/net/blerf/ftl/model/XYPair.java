package net.blerf.ftl.model;


/**
 * An immutable int pair, suitable for use as a Map key, unlike Point.
 */
public class XYPair {

    public final int x;
    public final int y;
    private final int hash;


    public XYPair(int x, int y) {
        this.x = x;
        this.y = y;

        // Pre-calculate the hashCode, since this is immutable.
        int sum = x + y;
        hash = sum * (sum + 1) / 2 + x;
    }

    public XYPair(XYPair srcXYPair) {
        this(srcXYPair.x, srcXYPair.y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof XYPair)) return false;
        XYPair p = (XYPair) o;
        return (x == p.x && y == p.y);
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
