package net.blerf.ftl.constants;


/**
 * Constants to track which newbie tips need showing.
 * <p>
 * Messages are defined in misc.xml.
 * SHIPS_UNLOCKED = "tutorial_list_open"
 * SHIP_LIST_INTRO = "list_tutorial" and "list_tutorial_2"
 * <p>
 * After a tip is seen once, the level increments.
 * <p>
 * When FTL 1.5.4+ migrates "prof.sav" to create "ae_prof.sav", the level is
 * SHIP_LIST_INTRO.
 */
public enum NewbieTipLevel {
    SHIPS_UNLOCKED("Ships Unlocked", "Highlight the hangar's LIST button when there are unlocked ships."),
    SHIP_LIST_INTRO("Ship List Intro", "An intro when the ship list is displayed."),
    VETERAN("Veteran", "No further tips.");

    private final String name;
    private final String description;

    NewbieTipLevel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name;
    }
}
