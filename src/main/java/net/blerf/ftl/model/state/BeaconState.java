package net.blerf.ftl.model.state;

import net.blerf.ftl.constants.FleetPresence;

/**
 * A beacon on the sector map.
 * <p>
 * Beacon states do not contain their randomly determined values until they
 * are actually visited.
 * <p>
 * FTL uses the sector layout seed to decide pending events and such upon
 * entering the sector. Any distress, stores, etc. events get signs when
 * seen (or hazard icons when the map is revealed).
 *
 * @see SavedGameState#setSectorLayoutSeed(int)
 */
public class BeaconState {
    private int visitCount = 0;
    private String bgStarscapeImageInnerPath = null;
    private String bgSpriteImageInnerPath = null;
    private int bgSpritePosX = -1, bgSpritePosY = -1;
    private int bgSpriteRotation = 0;

    private boolean seen = false;

    private boolean enemyPresent = false;
    private String shipEventId = null;
    private String autoBlueprintId = null;
    private int shipEventSeed = 0;

    private FleetPresence fleetPresence = FleetPresence.NONE;

    private boolean underAttack = false;

    private StoreState store = null;


    /**
     * Constructor.
     */
    public BeaconState() {
    }

    /**
     * Copy constructor.
     * <p>
     * Any store will be copy-constructed as well.
     */
    public BeaconState(BeaconState srcBeacon) {
        visitCount = srcBeacon.getVisitCount();
        bgStarscapeImageInnerPath = srcBeacon.getBgStarscapeImageInnerPath();
        bgSpriteImageInnerPath = srcBeacon.getBgSpriteImageInnerPath();
        bgSpritePosX = srcBeacon.getBgSpritePosX();
        bgSpritePosY = srcBeacon.getBgSpritePosY();
        bgSpriteRotation = srcBeacon.getBgSpriteRotation();
        seen = srcBeacon.isSeen();
        enemyPresent = srcBeacon.isEnemyPresent();
        shipEventId = srcBeacon.getShipEventId();
        autoBlueprintId = srcBeacon.getAutoBlueprintId();
        shipEventSeed = srcBeacon.getShipEventSeed();
        fleetPresence = srcBeacon.getFleetPresence();
        underAttack = srcBeacon.isUnderAttack();

        if (srcBeacon.getStore() != null) {
            store = new StoreState(srcBeacon.getStore());
        }
    }

    /**
     * Sets the number of times the player has arrived at this beacon.
     * <p>
     * If non-zero, starscape and sprite paths must be set,
     * as well as the sprite's X, Y, and rotation.
     * <p>
     * When non-zero, this prevents randomly generated events
     * from triggering. The sector exit will still exist.
     */
    public void setVisitCount(int n) {
        visitCount = n;
    }

    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Sets a fullscreen starscape image for the background.
     * <p>
     * By convention, this path is from the BG_* imageLists.
     */
    public void setBgStarscapeImageInnerPath(String s) {
        bgStarscapeImageInnerPath = s;
    }

    public String getBgStarscapeImageInnerPath() {
        return bgStarscapeImageInnerPath;
    }

    /**
     * Sets a background sprite to draw over the starscape.
     * <p>
     * By convention, this path is from the PLANET_* imageLists.
     * To not display a sprite, set it to "NONE".
     */
    public void setBgSpriteImageInnerPath(String s) {
        bgSpriteImageInnerPath = s;
    }

    public String getBgSpriteImageInnerPath() {
        return bgSpriteImageInnerPath;
    }

    /**
     * Sets the position of the background sprite image.
     * <p>
     * When the sprite's inner path is "NONE",
     * X and Y should be 0.
     */
    public void setBgSpritePosX(int n) {
        bgSpritePosX = n;
    }

    public void setBgSpritePosY(int n) {
        bgSpritePosY = n;
    }

    public int getBgSpritePosX() {
        return bgSpritePosX;
    }

    public int getBgSpritePosY() {
        return bgSpritePosY;
    }

    /**
     * Sets the rotation of the background sprite image.
     * <p>
     * When the sprite's inner path is "NONE", this should be 0.
     *
     * @param n degrees clockwise (may be negative)
     */
    public void setBgSpriteRotation(int n) {
        bgSpriteRotation = n;
    }

    public int getBgSpriteRotation() {
        return bgSpriteRotation;
    }

    /**
     * Sets whether the player has been within one hop of this beacon.
     */
    public void setSeen(boolean b) {
        seen = b;
    }

    public boolean isSeen() {
        return seen;
    }

    /**
     * Sets whether an enemy ship is waiting at this beacon.
     * <p>
     * If true, a ShipEvent and AutoBlueprint must be set,
     * as well as the ShipEvent seed.
     */
    public void setEnemyPresent(boolean b) {
        enemyPresent = b;
    }

    public boolean isEnemyPresent() {
        return enemyPresent;
    }

    /**
     * Sets a ShipEvent to trigger upon arrival.
     */
    public void setShipEventId(String s) {
        shipEventId = s;
    }

    public String getShipEventId() {
        return shipEventId;
    }

    /**
     * Sets an auto blueprint (or blueprintList) to spawn with the ShipEvent.
     */
    public void setAutoBlueprintId(String s) {
        autoBlueprintId = s;
    }

    public String getAutoBlueprintId() {
        return autoBlueprintId;
    }

    /**
     * Sets a seed to randomly generate the enemy ship (layout, etc).
     * <p>
     * When the player ship visits this beacon, the resulting encounter
     * will use this seed. When no enemy ship is present, this is 0.
     * <p>
     * In distant beacons occupied by the rebel fleet, this has been
     * observed varying between saves during a single fight!?
     */
    public void setShipEventSeed(int n) {
        shipEventSeed = n;
    }

    public int getShipEventSeed() {
        return shipEventSeed;
    }

    /**
     * Sets fleet background sprites and possibly the beacon icon.
     * <p>
     * When FTL moves the rebel fleet over a beacon, the beacon's
     * fleet presence becomes REBEL, and if it was visited, a
     * LONG_FLEET ShipEvent is set. Otherwise, one of the FLEET_*
     * events will be triggered to spawn the LONG_FLEET upon arrival.
     */
    public void setFleetPresence(FleetPresence fp) {
        fleetPresence = fp;
    }

    public FleetPresence getFleetPresence() {
        return fleetPresence;
    }

    /**
     * Sets whether this beacon is under attack by rebels (flashing red).
     * <p>
     * If true, the next time the player jumps to a beacon, this one
     * will have a REBEL fleet and possibly a LONG_FLEET ShipEvent,
     * and will no longer be under attack.
     */
    public void setUnderAttack(boolean b) {
        underAttack = b;
    }

    public boolean isUnderAttack() {
        return underAttack;
    }

    /**
     * Places a store at this beacon, or null for none.
     */
    public void setStore(StoreState storeState) {
        store = storeState;
    }

    public StoreState getStore() {
        return store;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Visit Count:           %5d%n", visitCount));
        if (visitCount > 0) {
            result.append(String.format("  Bkg Starscape:       %s%n", bgStarscapeImageInnerPath));
            result.append(String.format("  Bkg Sprite:          %s%n", bgSpriteImageInnerPath));
            result.append(String.format("  Bkg Sprite Position:   %3d,%3d%n", bgSpritePosX, bgSpritePosY));
            result.append(String.format("  Bkg Sprite Rotation:   %3d%n", bgSpriteRotation));
        }

        result.append(String.format("Seen:                  %5b%n", seen));

        result.append(String.format("Enemy Present:         %5b%n", enemyPresent));
        if (enemyPresent) {
            result.append(String.format("  Ship Event ID:       %s%n", shipEventId));
            result.append(String.format("  Auto Blueprint ID:   %s%n", autoBlueprintId));
            result.append(String.format("  Ship Event Seed:     %5d%n", shipEventSeed));
        }

        result.append(String.format("Fleets Present:        %s%n", fleetPresence));

        result.append(String.format("Under Attack:          %5b%n", underAttack));

        if (store != null) {
            result.append("\nStore...\n");
            result.append(store.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
