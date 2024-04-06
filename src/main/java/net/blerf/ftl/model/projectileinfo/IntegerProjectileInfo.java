package net.blerf.ftl.model.projectileinfo;

public class IntegerProjectileInfo extends ExtendedProjectileInfo {
    private final int[] unknownAlpha;

    /**
     * Constructs an incomplete IntegerProjectileInfo.
     * <p>
     * A number of integers equal to size will need to be set.
     */
    public IntegerProjectileInfo(int size) {
        super();
        unknownAlpha = new int[size];
    }

    /**
     * Copy constructor.
     */
    protected IntegerProjectileInfo(IntegerProjectileInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = new int[srcInfo.getSize()];
        for (int i = 0; i < unknownAlpha.length; i++) {
            unknownAlpha[i] = srcInfo.get(i);
        }
    }

    @Override
    public IntegerProjectileInfo copy() {
        return new IntegerProjectileInfo(this);
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

        result.append(String.format("Type:               Unknown Info%n"));

        result.append(String.format("%nAlpha?...%n"));
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
