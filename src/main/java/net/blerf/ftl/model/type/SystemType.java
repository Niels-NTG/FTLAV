package net.blerf.ftl.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Types of systems.
 * <p>
 * FTL 1.5.4 introduced BATTERY, CLONEBAY, MIND, and HACKING.
 */
@Getter
@AllArgsConstructor
public enum SystemType {
    // SystemType ids are tied to "img/icons/s_*_overlay.png" and store item ids.
    // TODO: Magic booleans.
    PILOT("pilot", true, 0),
    DOORS("doors", true, 0),
    SENSORS("sensors", true, 1),
    MEDBAY("medbay", false, 1),
    OXYGEN("oxygen", false, -2),
    SHIELDS("shields", false, 0),
    ENGINES("engines", false, 2),
    WEAPONS("weapons", false, 1),
    DRONE_CTRL("drones", false, -2),
    TELEPORTER("teleporter", false, -2),
    CLOAKING("cloaking", false, -2),
    ARTILLERY("artillery", false, -2),
    BATTERY("battery", true, -2),
    CLONEBAY("clonebay", false, 1),
    MIND("mind", false, -2),
    HACKING("hacking", false, -2);

    private final String id;
    private final boolean subsystem;
    /**
     * Returns the terminal/blocked square when slot was not defined, or -2.
     * <p>
     * FTL has hard-coded defaults for each system, to use when ShipBlueprint
     * doesn't override them. This only applies to player ships. For enemy
     * ships, each system's slot square appears to be on any random wall that's
     * not a door.
     * <p>
     * FTL 1.5.4 introduced mannable SENSORS and DOORS.
     * These values are for AE edition.
     */
    private final int operatorSquare;

    @Override
    public String toString() {
        return id;
    }

    public static SystemType findById(String id) {
        for (SystemType s : values()) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }
}
