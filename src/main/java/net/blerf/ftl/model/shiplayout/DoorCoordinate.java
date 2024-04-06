package net.blerf.ftl.model.shiplayout;


// Regular int arrays don't override the methods needed for
// use as Map keys, testing for identity instead of equality.
public class DoorCoordinate {
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
        DoorCoordinate d = (DoorCoordinate) o;
        return (x == d.x && y == d.y && v == d.v);
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
        n = (n ^ (n << 8)) & 0x0300f00f;
        n = (n ^ (n << 4)) & 0x030c30c3;
        n = (n ^ (n << 2)) & 0x09249249;
        return n;
    }
}