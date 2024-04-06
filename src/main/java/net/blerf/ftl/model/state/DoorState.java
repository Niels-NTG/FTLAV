package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoorState {
    private boolean open = false;
    private boolean walkingThrough = false;
    /**
     * Sets current max door health.
     * <p>
     * This is affected by situational modifiers like Crystal lockdown,
     * but it likely copies the nominal value at some point.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int currentMaxHealth = 0;
    /**
     * Sets the current door health.
     * <p>
     * Starting at current max, this decreases as someone tries to break it
     * down.
     * <p>
     * TODO: After combat in which a hacking drone boosts the door's health,
     * the current max returns to normal, but the actual health stays high
     * for some reason.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int health = 0;
    /**
     * Sets nominal max door health.
     * This is the value to which the current max will eventually reset.
     * <p>
     * Observed values:
     * 04 = Level 0 (un-upgraded or damaged Doors system).
     * 08 = Level 1 (???)
     * 12 = Level 2 (confirmed)
     * 16 = Level 3 (confirmed)
     * 20 = Level 4 (Level 3, plus manned; confirmed)
     * 18 = Level 3 (max, plus manned) (or is it 15, 10 while unmanned?)
     * 50 = Lockdown.
     * <p>
     * TODO: The Mantis Basilisk ship's doors went from 4 to 12 when the
     * 1-capacity Doors system was manned. Doors that were already hacked at
     * the time stayed at 16.
     * <p>
     * TODO: Check what the Rock B Ship's doors have (it lacks a Doors
     * system). Damaged system is 4 (hacked doors were still 16).
     * <p>
     * TODO: Investigate why an attached hacking drone adds to ALL THREE
     * healths (set on contact). Observed diffs: 4 to 16.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int nominalHealth = 0;
    /**
     * Unknown.
     * <p>
     * Observed values: 0 (normal), 1 (while level 2 Doors system is
     * unmanned), 1 (while level 1 Doors system is manned), 2 (while level 3
     * Doors system is unmanned), 3 (while level 3 Doors system is manned),
     * 2 (hacking pod passively attached, set on
     * contact). Still 2 while hack-disrupting.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int unknownDelta = 0;
    /**
     * Sets hacking drone lockdown status.
     * <p>
     * Observed values:
     * 0 = N/A
     * 1 = Hacking drone pod passively attached.
     * 2 = Hacking drone pod attached and disrupting.
     * <p>
     * A hacking system launches a drone pod that will latch onto a target
     * system room, granting visibility. While the pod is attached and there
     * is power to the hacking system, the doors of the room turn purple,
     * locked to the crew of the targeted ship, but passable to the hacker's
     * crew.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int unknownEpsilon = 0;


    /**
     * Copy constructor.
     */
    public DoorState(DoorState srcDoor) {
        open = srcDoor.isOpen();
        walkingThrough = srcDoor.isWalkingThrough();
        currentMaxHealth = srcDoor.getCurrentMaxHealth();
        health = srcDoor.getHealth();
        nominalHealth = srcDoor.getNominalHealth();
        unknownDelta = srcDoor.getUnknownDelta();
        unknownEpsilon = srcDoor.getUnknownEpsilon();
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        setCurrentMaxHealth(getNominalHealth());
        setHealth(getCurrentMaxHealth());

        setUnknownDelta(0);    // TODO: Vet this default.
        setUnknownEpsilon(0);  // TODO: Vet this default.
    }

    @Override
    public String toString() {
        return String.format("Open: %-5b, Walking Through: %-5b%n", open, walkingThrough) +
                String.format("Full HP: %3d, Current HP: %3d, Nominal HP: %3d, Delta?: %3d, Epsilon?: %3d%n", currentMaxHealth, health, nominalHealth, unknownDelta, unknownEpsilon);
    }
}
