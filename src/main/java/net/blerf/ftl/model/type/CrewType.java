package net.blerf.ftl.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrewType {
    // TODO: Magic numbers.
    ANAEROBIC("anaerobic", 100),
    BATTLE("battle", 150),
    CRYSTAL("crystal", 120),
    ENERGY("energy", 70),
    ENGI("engi", 100),
    GHOST("ghost", 50),
    HUMAN("human", 100),
    MANTIS("mantis", 100),
    ROCK("rock", 150),
    SLUG("slug", 100);

    // The following were introduced in FTL 1.5.4.
    // ANAEROBIC (when DLC is enabled)

    private final String id;
    private final int maxHealth;

    @Override
    public String toString() {
        return id;
    }

    public static CrewType findById(String id) {
        for (CrewType race : values()) {
            if (race.getId().equals(id)) return race;
        }
        return null;
    }
}
