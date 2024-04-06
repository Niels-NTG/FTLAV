package net.blerf.ftl.constants;

public enum FleetPresence {
    NONE("None"), REBEL("Rebel"), FEDERATION("Federation"), BOTH("Both");

    private final String title;

    FleetPresence(String title) {
        this.title = title;
    }

    public static FleetPresence fromInt(int i) {
        if (i < 0 || i > FleetPresence.values().length) {
            return NONE;
        }
        return FleetPresence.values()[i];
    }

    public int toInt() {
        return ordinal();
    }

    public String getTitle() {
        return title;
    }
}
