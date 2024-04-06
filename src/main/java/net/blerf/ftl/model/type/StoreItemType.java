package net.blerf.ftl.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreItemType {
    WEAPON("Weapon"),
    DRONE("Drone"),
    AUGMENT("Augment"),
    CREW("Crew"),
    SYSTEM("System");

    private final String title;

    public static StoreItemType fromInt(int i) {
        if (i < 0 || i > StoreItemType.values().length) {
            return WEAPON;
        }
        return StoreItemType.values()[i];
    }

    public int toInt() {
        return ordinal();
    }

    @Override
    public String toString() {
        return title;
    }
}
