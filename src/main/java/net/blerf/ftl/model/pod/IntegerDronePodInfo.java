package net.blerf.ftl.model.pod;

/**
 * Generic drone pod info consisting of an int array.
 * <p>
 * No longer used, this had allowed read/write of various drones' unknown
 * structures each with different lengths.
 */
public class IntegerDronePodInfo extends ExtendedDronePodInfo {
    private final int[] unknownAlpha;

    /**
     * Constructs an incomplete IntegerDronePodInfo.
     * <p>
     * A number of integers equal to size will need to be set.
     */
    public IntegerDronePodInfo(int size) {
        super();
        unknownAlpha = new int[size];
    }

    /**
     * Copy constructor.
     */
    protected IntegerDronePodInfo(IntegerDronePodInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = new int[srcInfo.getSize()];
        for (int i = 0; i < unknownAlpha.length; i++) {
            unknownAlpha[i] = srcInfo.get(i);
        }
    }

    @Override
    public IntegerDronePodInfo copy() {
        return new IntegerDronePodInfo(this);
    }


    @Override
    public void commandeer() {
    }


    public int getSize() {
        return unknownAlpha.length;
    }

    public void set(int index, int n) {
        unknownAlpha[index] = n;
    }

    public int get(int index) {
        return unknownAlpha[index];
    }


    private String prettyInt(int n) {
        if (n == Integer.MIN_VALUE) return "MIN";
        if (n == Integer.MAX_VALUE) return "MAX";

        return String.format("%d", n);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Alpha?...%n"));
        for (int i = 0; i < unknownAlpha.length; i++) {
            result.append(String.format("%7s", prettyInt(unknownAlpha[i])));

            if (i != unknownAlpha.length - 1) {
                if (i % 2 == 1) {
                    result.append(",\n");
                } else {
                    result.append(", ");
                }
            }
        }
        result.append("\n");

        return result.toString();
    }
}
