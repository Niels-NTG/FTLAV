package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EncounterState {
    /**
     * Seed to randomly generate the enemy ship (layout, etc).
     * <p>
     * When the player ship visits a beacon, the resulting encounter
     * will use the beacon's enemy ship event seed.
     * <p>
     * When not set, this is 0. After encountering ships, this value lingers.
     */
    private int shipEventSeed = 0;
    private String surrenderEventId = "";
    private String escapeEventId = "";
    private String destroyedEventId = "";
    private String deadCrewEventId = "";
    private String gotAwayEventId = "";

    /**
     * The id of the most recent (possibly current) event id.
     * <p>
     * As secondary and tertiary events are triggered at a beacon, this
     * value will be replaced by their ids.
     * <p>
     * Sometimes this is blank.
     * <p>
     * Matthew's hint: There are two kinds of event: static events, assigned
     * randomly based on the sector seed and "sector_data.xml" (like for
     * nebula beacons); and dynamic events. This value only tracks dynamic
     * events.
     */
    private String lastEventId = "";

    /**
     * Unknown.
     * <p>
     * This was introduced in FTL 1.6.1.
     */
    private int unknownAlpha = 0;

    /**
     * Sets the last situation-describing text shown in an event window.
     * <p>
     * Any event - 'static', secondary, or wait - may set this value. It
     * may have no relation to the last event id.
     * <p>
     * Note: Wait events triggered in-game set this value. Toggling waiting
     * programmatically does NOT set this value. That must be done
     * manually.
     * <p>
     * FTL 1.6.1 introduced XML "id" attributes on elements, which
     * referenced text elsewhere. This value may be one of those references
     * instead of the actual text.
     * <p>
     * After the event popup is dismissed, this value lingers.
     * <p>
     * This may include line breaks ("\n").
     *
     * @see SavedGameState#setWaiting(boolean)
     */
    private String text = "";

    /**
     * Sets a seed used to randomly select crew.
     * <p>
     * When saved mid-event, this allows FTL to reselect the same crew.
     * <p>
     * When no random selection has been made, this is -1.
     */
    private int affectedCrewSeed = -1;

    /**
     * Sets a list of breadcrumbs for choices made during the last event.
     * <p>
     * Each integer in the list corresponds to a prompt, and the Integer's
     * value is the Nth choice that was clicked. (0-based)
     * <p>
     * TODO: 52 was observed in the list once!?
     * <p>
     * The event will still be in-progress if there aren't enough
     * breadcrumbs to renavigate to the end of the event.
     * <p>
     * The list typically ends with a 0, since events usually conclude with
     * a lone "continue" choice.
     * <p>
     * Note: If waiting, this list will cause a wait event to be selected
     * from fuel-related event lists, instead of a normal event.
     *
     * @see SavedGameState#setWaiting(boolean)
     */
    private List<Integer> choiceList = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Ship Event Seed:    %3d%n", shipEventSeed));
        result.append(String.format("Surrender Event:    %s%n", surrenderEventId));
        result.append(String.format("Escape Event:       %s%n", escapeEventId));
        result.append(String.format("Destroyed Event:    %s%n", destroyedEventId));
        result.append(String.format("Dead Crew Event:    %s%n", deadCrewEventId));
        result.append(String.format("Got Away Event:     %s%n", gotAwayEventId));

        result.append("\n");

        result.append(String.format("Last Event:         %s%n", lastEventId));
        result.append(String.format("Alpha?:             %3d%n", unknownAlpha));

        result.append("\nText...\n");
        result.append(String.format("%s%n", text));
        result.append("\n");

        result.append(String.format("Affected Crew Seed: %3d%n", affectedCrewSeed));

        result.append("\nLast Event Choices...\n");
        boolean first = true;
        for (Integer choiceInt : choiceList) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append(choiceInt);
        }
        result.append("\n");

        return result.toString();
    }
}
