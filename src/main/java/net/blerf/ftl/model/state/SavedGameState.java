package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.parser.MysteryBytes;

@Slf4j
public class SavedGameState {
    private int fileFormat = 0;
    private boolean randomNative = true;
    private boolean dlcEnabled = false;
    private Difficulty difficulty = Difficulty.EASY;
    private int totalShipsDefeated = 0;
    private int totalBeaconsExplored = 0;
    private int totalScrapCollected = 0;
    private int totalCrewHired = 0;
    private String playerShipName = "";
    private String playerShipBlueprintId = "";
    private int sectorNumber = 1;
    private int unknownBeta = 0;
    private final Map<String, Integer> stateVars = new LinkedHashMap<>();
    private ShipState playerShipState = null;
    private List<String> cargoIdList = new ArrayList<>();
    private int sectorTreeSeed = 42;      // Arbitrary default.
    private int sectorLayoutSeed = 42;    // Arbitrary default.
    private int rebelFleetOffset = -750;  // Arbitrary default.
    private int rebelFleetFudge = 100;    // Arbitrary default.
    private int rebelPursuitMod = 0;
    private int currentBeaconId = 0;
    private boolean waiting = false;
    private int waitEventSeed = -1;
    private String unknownEpsilon = "";
    private boolean sectorHazardsVisible = false;
    private boolean rebelFlagshipVisible = false;
    private int rebelFlagshipHop = 0;
    private boolean rebelFlagshipMoving = false;
    private boolean rebelFlagshipRetreating = false;
    private int rebelFlagshipBaseTurns = 0;
    private List<Boolean> sectorVisitationList = new ArrayList<>();
    private boolean sectorIsHiddenCrystalWorlds = false;
    private final List<BeaconState> beaconList = new ArrayList<>();
    private final Map<String, Integer> questEventMap = new LinkedHashMap<>();
    private final List<String> distantQuestEventList = new ArrayList<>();
    private int unknownMu = 0;
    private EncounterState encounter = null;
    private boolean rebelFlagshipNearby = false;
    private ShipState nearbyShipState = null;
    private NearbyShipAIState nearbyShipAI = null;
    private EnvironmentState environment = null;
    private final List<ProjectileState> projectileList = new ArrayList<>();
    private int unknownNu = 0;
    private Integer unknownXi = null;
    private boolean autofire = false;
    private RebelFlagshipState rebelFlagshipState = null;
    private final List<MysteryBytes> mysteryList = new ArrayList<>();


    public SavedGameState() {
    }

    /**
     * Sets the magic number indicating file format.
     * <p>
     * Observed values:
     * 2 = Saved Game, FTL 1.01-1.03.3
     * 7 = Saved Game, FTL 1.5.4-1.5.10
     * 8 = Saved Game, FTL 1.5.12
     * 9 = Saved Game, FTL 1.5.13
     * 11 = Saved Game, FTL 1.6.1
     * <p>
     * Unicode strings were introduced in FTL 1.6.1. Unlike
     * profiles, saved games DO have a magic number to detect that
     * version, so windows-1252 characters can be enforced for
     * earlier FTL versions.
     *
     * @see net.blerf.ftl.parser.Parser#setUnicode(boolean)
     */
    public void setFileFormat(int n) {
        fileFormat = n;
    }

    public int getFileFormat() {
        return fileFormat;
    }

    /**
     * Sets whether the native RNG was used.
     * <p>
     * FTL 1.6.1 introduced a hard-coded RNG to use on all platforms.
     * Earlier editions delegated to the OS srand()/rand() functions,
     * making saved games platform-dependent.
     * <p>
     * This value is set to true if a saved game from an earlier edition
     * is migrated, requiring a native RNG to interpret its seeds.
     * <p>
     * This was introduced in FTL 1.6.1.
     */
    public void setRandomNative(boolean b) {
        randomNative = b;
    }

    public boolean isRandomNative() {
        return randomNative;
    }

    /**
     * Sets the difficulty.
     * <p>
     * EASY
     * NORMAL
     * HARD (FTL 1.5.4+)
     */
    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the total number of ships defeated.
     * <p>
     * Either reducing hull to 0 or eliminating crew will increase this total.
     */
    public void setTotalShipsDefeated(int n) {
        totalShipsDefeated = n;
    }

    public int getTotalShipsDefeated() {
        return totalShipsDefeated;
    }

    public void setTotalBeaconsExplored(int n) {
        totalBeaconsExplored = n;
    }

    public int getTotalBeaconsExplored() {
        return totalBeaconsExplored;
    }

    /**
     * Sets the total scrap collected.
     * <p>
     * Sales at stores and the Scrap Recovery Arm do not increase this total.
     */
    public void setTotalScrapCollected(int n) {
        totalScrapCollected = n;
    }

    public int getTotalScrapCollected() {
        return totalScrapCollected;
    }

    public void setTotalCrewHired(int n) {
        totalCrewHired = n;
    }

    public int getTotalCrewHired() {
        return totalCrewHired;
    }

    /**
     * Returns the computed score, as would be displayed in FTL if the game had ended.
     *
     * @see #setDifficulty(Difficulty)
     * @see #setTotalShipsDefeated(int)
     * @see #setTotalBeaconsExplored(int)
     * @see #setTotalScrapCollected(int)
     */
    public int calculateScore() {
        float diffMod;
        if (difficulty == Difficulty.EASY) {
            diffMod = 1.0f;
        } else if (difficulty == Difficulty.NORMAL) {
            diffMod = 1.25f;
        } else if (difficulty == Difficulty.HARD) {
            diffMod = 1.5f;
        } else {
            log.warn(String.format("Substituting EASY for unsupported difficulty while calculating score: %s", difficulty.toString()));
            diffMod = 1.0f;
        }
        return (int) ((totalScrapCollected + 10 * totalBeaconsExplored + 20 * totalShipsDefeated) * diffMod);
    }

    /**
     * Sets a redundant player ship name.
     */
    public void setPlayerShipName(String shipName) {
        playerShipName = shipName;
    }

    public String getPlayerShipName() {
        return playerShipName;
    }

    /**
     * Sets a redundant player ship blueprint.
     */
    public void setPlayerShipBlueprintId(String shipBlueprintId) {
        playerShipBlueprintId = shipBlueprintId;
    }

    public String getPlayerShipBlueprintId() {
        return playerShipBlueprintId;
    }

    /**
     * Adds cargo to the player ship (N/A for enemy ships).
     */
    public void addCargoItemId(String cargoItemId) {
        cargoIdList.add(cargoItemId);
    }

    public void setCargoList(List<String> cargoIdList) {
        this.cargoIdList = cargoIdList;
    }

    public List<String> getCargoIdList() {
        return cargoIdList;
    }

    /**
     * Sets the current sector's number (0-based).
     * <p>
     * After editing, the map's displayed sector number, all visible
     * hazards, and point-of-interest labels will immediately change, but
     * not the beacons' pixel positions.
     * <p>
     * Previously-visited beacons with lingering ship encounters will
     * retain their events, as those details come from the fixed
     * BeaconState list.
     * <p>
     * Modifying this will not affect the sector tree.
     * <p>
     * After jumping from the exit into a new sector, FTL will increment
     * this number, and also set a redundant 1-based sector number in the
     * saved game's header.
     * <p>
     * TODO: Determine long-term effects of this. The Last Stand is baked
     * into the sector tree, but weird things might happen at or above #7.
     *
     * @see #addBeacon(BeaconState)
     * @see #setSectorLayoutSeed(int)
     * @see #setSectorTreeSeed(int)
     */
    public void setSectorNumber(int n) {
        sectorNumber = n;
    }

    public int getSectorNumber() {
        return sectorNumber;
    }

    /**
     * Toggles FTL:AE content.
     * <p>
     * Note: Bad things may happen if you change the value from true to
     * false, if this saved game depends on AE resources.
     * <p>
     * Sector tree reconstruction will be affected by changes to available
     * sectors.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setSectorLayoutSeed(int)
     */
    public void setDLCEnabled(boolean b) {
        dlcEnabled = b;
    }

    public boolean isDLCEnabled() {
        return dlcEnabled;
    }

    /**
     * Unknown.
     * <p>
     * Always 0?
     */
    public void setUnknownBeta(int n) {
        unknownBeta = n;
    }

    public int getUnknownBeta() {
        return unknownBeta;
    }

    /**
     * Sets the value of a state var.
     * <p>
     * State vars are mostly used to test candidacy for achievements.
     * <p>
     * See StateVar enums for known vars and descriptions.
     */
    public void setStateVar(String stateVarId, int stateVarValue) {
        stateVars.put(stateVarId, stateVarValue);
    }

    /**
     * Returns true if a state var has been set, false otherwise.
     */
    public boolean hasStateVar(String stateVarId) {
        return stateVars.containsKey(stateVarId);
    }

    /**
     * Returns the value of a state var.
     * <p>
     * If the state var has not been set, a NullPointerException will be
     * thrown.
     */
    public int getStateVar(String stateVarId) {
        Integer result = stateVars.get(stateVarId);
        return result;
    }

    public Map<String, Integer> getStateVars() {
        return stateVars;
    }

    public void setPlayerShip(ShipState shipState) {
        this.playerShipState = shipState;
    }

    public ShipState getPlayerShip() {
        return playerShipState;
    }

    /**
     * Sets the seed for generating the sector tree.
     * <p>
     * Note: When this is changed, you MUST reset sector visitation.
     *
     * @see #setSectorVisitation(List)
     * @see net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator
     */
    public void setSectorTreeSeed(int n) {
        sectorTreeSeed = n;
    }

    public int getSectorTreeSeed() {
        return sectorTreeSeed;
    }

    /**
     * Sets the seed for randomness in the current sector.
     * <p>
     * This determines the graphical positioning of beacons, as well as
     * their environment hazards (like nebula/storm) and events.
     * <p>
     * Reloading a saved game from the end of the previous sector, and
     * exiting again will yield a different seed. So sectors' layout seeds
     * aren't predetermined at the start of the game.
     * <p>
     * Changing this may affect the beacon count. The game will generate
     * additional beacons if it expects them (and probably truncate the
     * excess if there are too many).
     * <p>
     * Note: The RNG algorithm that FTL uses to interpret seeds will vary
     * with each platform. Results will be inconsistent if a saved game is
     * resumed on another operating system.
     *
     * @see #addBeacon(BeaconState)
     */
    public void setSectorLayoutSeed(int n) {
        sectorLayoutSeed = n;
    }

    public int getSectorLayoutSeed() {
        return sectorLayoutSeed;
    }

    /**
     * Sets the raw fleet position on the map.
     * <p>
     * This is always a negative value that, when added to rebelFleetFudge,
     * equals how far in from the right the warning circle has encroached.
     * <p>
     * Most sectors start with large negative value to keep this off-screen
     * and increment toward 0 from there.
     * <p>
     * In FTL 1.01-1.03.3, The Last Stand sector used a constant -25 and
     * moderate rebelFleetFudge value to cover the map. In other sectors,
     * This was always observed in multiples of 25.
     * <p>
     * The image is 'img/map/map_warningcircle_point.png' (650px wide, with
     * a ~50px margin).
     * <p>
     * TODO: After loading a saved game from FTL 1.03.3 into FTL 1.5.4,
     * this value was observed going from -250 to -459. The fudge was
     * unchanged. The significance of this is unknown.
     *
     * @param n pixels from the map's right edge
     * @see #setRebelFleetFudge(int)
     */
    public void setRebelFleetOffset(int n) {
        rebelFleetOffset = n;
    }

    public int getRebelFleetOffset() {
        return rebelFleetOffset;
    }

    /**
     * Sets an intra-sector constant adjusting initial fleet encroachment.
     * <p>
     * This is always a positive number around 75-310 that,
     * when added to rebelFleetOffset, equals how far in
     * from the right the warning circle has encroached.
     * <p>
     * This varies seemingly randomly from game to game and
     * sector to sector, but it's consistent while within
     * each sector. Except in The Last Stand, in which it is
     * always 200 (the warning circle will extend beyond
     * the righthand edge of the map, covering everything).
     *
     * @see #setRebelFleetOffset(int)
     */
    public void setRebelFleetFudge(int n) {
        rebelFleetFudge = n;
    }

    public int getRebelFleetFudge() {
        return rebelFleetFudge;
    }

    /**
     * Delays/alerts the rebel fleet (-/+).
     * <p>
     * This adjusts the thickness of the warning zone.
     * Example: Hiring a merc ship to distract sets -2.
     */
    public void setRebelPursuitMod(int n) {
        rebelPursuitMod = n;
    }

    public int getRebelPursuitMod() {
        return rebelPursuitMod;
    }

    /**
     * Toggles whether a wait event is active (as in out of fuel).
     * <p>
     * If true, the waitEventSeed must be set, or FTL will crash.
     * <p>
     * If true, a random fuel-related event will be chosen (distress signal
     * status is not saved, only remembered while FTL runs - even bouncing to
     * the main menu). The EncounterState's choice list will need to be empty
     * or incomplete for the event popup to appear.
     * <p>
     * Note: The EncounterState's text will NOT be synchronized with the
     * event. Any lingering value will display with this event's choices
     * below it... unless the text is set manually.
     * <p>
     * After the wait event has completed, this will be set to false.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setWaitEventSeed(int)
     * @see EncounterState#setText(String)
     * @see EncounterState#setChoiceList(List<Integer>)
     */
    public void setWaiting(boolean b) {
        waiting = b;
    }

    public boolean isWaiting() {
        return waiting;
    }

    /**
     * Sets a seed for wait events.
     * <p>
     * This has no effect when not waiting.
     * <p>
     * When not set, this is -1.
     * <p>
     * This value lingers.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setWaiting(boolean)
     */
    public void setWaitEventSeed(int n) {
        waitEventSeed = n;
    }

    public int getWaitEventSeed() {
        return waitEventSeed;
    }

    /**
     * Unknown.
     * <p>
     * This has been observed to be an eventId of some sort
     * ("FUEL_ESCAPE_ASTEROIDS") related to waiting.
     * <p>
     * When not set, this is "".
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownEpsilon(String s) {
        unknownEpsilon = s;
    }

    public String getUnknownEpsilon() {
        return unknownEpsilon;
    }

    /**
     * Toggles visibility of beacon hazards for this sector.
     */
    public void setSectorHazardsVisible(boolean b) {
        sectorHazardsVisible = b;
    }

    public boolean areSectorHazardsVisible() {
        return sectorHazardsVisible;
    }

    /**
     * Toggles the flagship on the map.
     * <p>
     * If true, this causes instant loss if not in sector 8.
     */
    public void setRebelFlagshipVisible(boolean b) {
        rebelFlagshipVisible = b;
    }

    public boolean isRebelFlagshipVisible() {
        return rebelFlagshipVisible;
    }

    /**
     * Sets the flagship's current beacon.
     * <p>
     * The flagship will be at its Nth random beacon. (0-based)
     * The sector layout seed affects where that will be.
     * <p>
     * At or above the last hop (which varies), it causes instant loss in
     * FTL 1.01-1.03.3. (Observed game-ending values: 5, 7, potentially 9.)
     * <p>
     * Since FTL 1.5.4, the flagship must idle at the federation base for
     * a few turns before the game ends.
     * <p>
     * If moving, this will be the beacon it's departing from.
     */
    public void setRebelFlagshipHop(int n) {
        rebelFlagshipHop = n;
    }

    public int getRebelFlagshipHop() {
        return rebelFlagshipHop;
    }

    /**
     * Sets whether the flagship is circling its beacon or moving toward the next.
     */
    public void setRebelFlagshipMoving(boolean b) {
        rebelFlagshipMoving = b;
    }

    public boolean isRebelFlagshipMoving() {
        return rebelFlagshipMoving;
    }

    /**
     * Sets whether the flagship is reversing course to a previous beacon.
     * <p>
     * FTL sets this immediately after defeating the flagship.
     * <p>
     * Observed values: 0 (Almost always), 1 (Immediately after defeating
     * the flagship, but reverts if loaded and saved again!?).
     * <p>
     * Bug in FTL 1.5.4-1.6.2: Bouncing to the main menu twice after
     * defeating the flagship at the base will reset hops and baseTurns to
     * 0, teleporting it to where it started.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setRebelFlagshipHop(int)
     */
    public void setRebelFlagshipRetreating(boolean b) {
        rebelFlagshipRetreating = b;
    }

    public boolean isRebelFlagshipRetreating() {
        return rebelFlagshipRetreating;
    }

    /**
     * Sets the number of turns the rebel flagship has started at the
     * federation base.
     * <p>
     * At the 4th turn, the game will end. (TODO: Confirm.)
     * This resets to 0 when the flagship flees to another beacon after
     * defeat.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @param n 0-4
     */
    public void setRebelFlagshipBaseTurns(int n) {
        rebelFlagshipBaseTurns = n;
    }

    public int getRebelFlagshipBaseTurns() {
        return rebelFlagshipBaseTurns;
    }

    /**
     * Toggles whether a dot on the sector tree has been visited.
     *
     * @param sector  an index of the sector list (0-based)
     * @param visited true if visited, false otherwise
     */
    public void setSectorVisited(int sector, boolean visited) {
        sectorVisitationList.set(sector, visited);
    }

    /**
     * Sets a list of sector tree breadcrumbs.
     * <p>
     * Saved games only contain a linear set of boolean flags to
     * track visited status. FTL reconstructs the sector tree at
     * runtime using the sector tree seed, and it maps these
     * booleans to the dots: top-to-bottom for each column,
     * left-to-right.
     *
     * @see #setSectorTreeSeed(int)
     * @see net.blerf.ftl.model.sectortree.SectorDot#setVisited(boolean)
     * @see net.blerf.ftl.model.sectortree.SectorTree#setSectorVisitation(List)
     */
    public void setSectorVisitation(List<Boolean> route) {
        sectorVisitationList = route;
    }

    public List<Boolean> getSectorVisitation() {
        return sectorVisitationList;
    }

    /**
     * Sets whether this sector is hidden.
     * The sector map will say "#? Hidden Crystal Worlds".
     * <p>
     * When jumping from the exit beacon, you won't get to
     * choose which branch of the sector tree will be
     * next.
     */
    public void setSectorIsHiddenCrystalWorlds(boolean b) {
        sectorIsHiddenCrystalWorlds = b;
    }

    public boolean isSectorHiddenCrystalWorlds() {
        return sectorIsHiddenCrystalWorlds;
    }

    /**
     * Adds a beacon to the sector map.
     * <p>
     * Beacons are indexed top-to-bottom for each column, left-to-right.
     * They're randomly offset a little when shown on screen to disguise
     * the columns.
     * <p>
     * The grid is approximately 6 x 4, but each column may be smaller.
     * <p>
     * Indexes can range from 0 to 23, but the sector layout seed may
     * generate fewer.
     *
     * @see #setSectorLayoutSeed(int)
     */
    public void addBeacon(BeaconState beacon) {
        beaconList.add(beacon);
    }

    public List<BeaconState> getBeaconList() {
        return beaconList;
    }

    public void addQuestEvent(String questEventId, int questBeaconId) {
        questEventMap.put(questEventId, questBeaconId);
    }

    public Map<String, Integer> getQuestEventMap() {
        return questEventMap;
    }

    public void addDistantQuestEvent(String questEventId) {
        distantQuestEventList.add(questEventId);
    }

    public List<String> getDistantQuestEventList() {
        return distantQuestEventList;
    }

    /**
     * Sets which beacon the the player ship is at.
     *
     * @see #getBeaconList()
     */
    public void setCurrentBeaconId(int n) {
        currentBeaconId = n;
    }

    public int getCurrentBeaconId() {
        return currentBeaconId;
    }

    /**
     * Unknown
     * <p>
     * Observed values: 1.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownMu(int n) {
        unknownMu = n;
    }

    public int getUnknownMu() {
        return unknownMu;
    }

    /**
     * Sets the currently active encounter.
     * <p>
     * The encounter will trigger upon loading.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setEncounter(EncounterState encounter) {
        this.encounter = encounter;
    }

    public EncounterState getEncounter() {
        return encounter;
    }

    /**
     * Toggles whether the nearby ship is the rebel flagship.
     * <p>
     * Saved games omit this value when a nearby ship is not present.
     * <p>
     * TODO: Document what happens when set to true.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setRebelFlagshipNearby(boolean b) {
        rebelFlagshipNearby = b;
    }

    public boolean isRebelFlagshipNearby() {
        return rebelFlagshipNearby;
    }

    /**
     * Sets a nearby ship, or null.
     * <p>
     * Since FTL 1.5.4, when this is non-null, a NearbyShipAI must be set.
     *
     * @see #setNearbyShipAI(NearbyShipAIState)
     */
    public void setNearbyShip(ShipState shipState) {
        this.nearbyShipState = shipState;
    }

    public ShipState getNearbyShip() {
        return nearbyShipState;
    }

    /**
     * Sets fields related to AI that controls the nearby ship, or null.
     * <p>
     * To set this, the nearby ship must be non-null.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setNearbyShipAI(NearbyShipAIState ai) {
        nearbyShipAI = ai;
    }

    public NearbyShipAIState getNearbyShipAI() {
        return nearbyShipAI;
    }

    /**
     * Sets fields related to a hostile environment at a beacon.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setEnvironment(EnvironmentState env) {
        environment = env;
    }

    public EnvironmentState getEnvironment() {
        return environment;
    }


    /**
     * Adds a projectile, currently in transit between ships.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void addProjectile(ProjectileState projectile) {
        projectileList.add(projectile);
    }

    public List<ProjectileState> getProjectileList() {
        return projectileList;
    }


    /**
     * Unknown.
     * <p>
     * Erratic values, large and small. Even changes mid-combat!?
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownNu(int n) {
        unknownNu = n;
    }

    public int getUnknownNu() {
        return unknownNu;
    }

    /**
     * Unknown.
     * <p>
     * Erratic values, large and small.
     * <p>
     * This is only set when a nearby ship is present.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownXi(Integer n) {
        unknownXi = n;
    }

    public Integer getUnknownXi() {
        return unknownXi;
    }

    /**
     * Toggles autofire.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setAutofire(boolean b) {
        autofire = b;
    }

    public boolean getAutofire() {
        return autofire;
    }

    /**
     * Sets info about the next encounter with the rebel flagship.
     */
    public void setRebelFlagshipState(RebelFlagshipState flagshipState) {
        this.rebelFlagshipState = flagshipState;
    }

    public RebelFlagshipState getRebelFlagshipState() {
        return rebelFlagshipState;
    }


    public void addMysteryBytes(MysteryBytes m) {
        mysteryList.add(m);
    }

    public List<MysteryBytes> getMysteryList() {
        return mysteryList;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String formatDesc = null;
        switch (fileFormat) {
            case (2):
                formatDesc = "Saved Game, FTL 1.01-1.03.3";
                break;
            case (7):
                formatDesc = "Saved Game, FTL 1.5.4-1.5.10";
                break;
            case (8):
                formatDesc = "Saved Game, FTL 1.5.12";
                break;
            case (9):
                formatDesc = "Saved Game, FTL 1.5.13";
                break;
            case (11):
                formatDesc = "Saved Game, FTL 1.6.1";
                break;
            default:
                formatDesc = "???";
                break;
        }

        boolean first = true;
        result.append(String.format("File Format:            %5d (%s)%n", fileFormat, formatDesc));
        result.append(String.format("Native RNG:             %5b (True for games migrated into FTL 1.6.1+)%n", randomNative));
        result.append(String.format("AE Content:             %5s%n", (dlcEnabled ? "Enabled" : "Disabled")));
        result.append(String.format("Ship Name:              %s%n", playerShipName));
        result.append(String.format("Ship Type:              %s%n", playerShipBlueprintId));
        result.append(String.format("Difficulty:             %s%n", difficulty.toString()));
        result.append(String.format("Sector:                 %5d (%d)%n", sectorNumber, sectorNumber + 1));
        result.append(String.format("Beta?:                  %5d (Always 0?)%n", unknownBeta));
        result.append(String.format("Total Ships Defeated:   %5d%n", totalShipsDefeated));
        result.append(String.format("Total Beacons Explored: %5d%n", totalBeaconsExplored));
        result.append(String.format("Total Scrap Collected:  %5d%n", totalScrapCollected));
        result.append(String.format("Total Crew Hired:       %5d%n", totalCrewHired));

        result.append("\nState Vars...\n");
        for (Map.Entry<String, Integer> entry : stateVars.entrySet()) {
            result.append(String.format("%-16s %4d%n", entry.getKey() + ":", entry.getValue()));
        }

        result.append("\nPlayer Ship...\n");
        if (playerShipState != null)
            result.append(playerShipState.toString().replaceAll("(^|\n)(.+)", "$1  $2"));

        result.append("\nCargo...\n");
        for (String cargoItemId : cargoIdList) {
            result.append(String.format("CargoItemId: %s%n", cargoItemId));
        }

        result.append("\nSector Data...\n");
        result.append(String.format("Sector Tree Seed:    %5d%n", sectorTreeSeed));
        result.append(String.format("Sector Layout Seed:  %5d%n", sectorLayoutSeed));
        result.append(String.format("Rebel Fleet Offset:  %5d%n", rebelFleetOffset));
        result.append(String.format("Rebel Fleet Fudge:   %5d%n", rebelFleetFudge));
        result.append(String.format("Rebel Pursuit Mod:   %5d%n", rebelPursuitMod));
        result.append(String.format("Player BeaconId:     %5d%n", currentBeaconId));
        result.append(String.format("Waiting:             %5b%n", waiting));
        result.append(String.format("Wait Event Seed:     %5d%n", waitEventSeed));
        result.append(String.format("Epsilon?:            %s%n", unknownEpsilon));
        result.append(String.format("Sector Hazards Map:  %5b%n", sectorHazardsVisible));
        result.append(String.format("In Hidden Sector:    %5b%n", sectorIsHiddenCrystalWorlds));
        result.append("\n");
        result.append(String.format("Flagship Visible:    %5b%n", rebelFlagshipVisible));
        result.append(String.format("Flagship Nth Hop:    %5d%n", rebelFlagshipHop));
        result.append(String.format("Flagship Moving:     %5b%n", rebelFlagshipMoving));
        result.append(String.format("Flagship Retreating: %5b%n", rebelFlagshipRetreating));
        result.append(String.format("Flagship Base Turns: %5d%n", rebelFlagshipBaseTurns));

        result.append("\nSector Tree Breadcrumbs...\n");
        first = true;
        for (Boolean b : sectorVisitationList) {
            if (first) {
                first = false;
            } else {
                result.append(",");
            }
            result.append((b ? "T" : "F"));
        }
        result.append("\n");

        result.append("\nSector Beacons...\n");
        int beaconId = 0;
        first = true;
        for (BeaconState beacon : beaconList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(String.format("BeaconId: %2d%n", beaconId++));
            result.append(beacon.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nQuests...\n");
        for (Map.Entry<String, Integer> entry : questEventMap.entrySet()) {
            String questEventId = entry.getKey();
            int questBeaconId = entry.getValue();
            result.append(String.format("QuestEventId: %s, BeaconId: %d%n", questEventId, questBeaconId));
        }

        result.append("\nNext Sector Quests...\n");
        for (String questEventId : distantQuestEventList) {
            result.append(String.format("QuestEventId: %s%n", questEventId));
        }

        result.append("\n");
        result.append(String.format("Mu?:                %5d%n", unknownMu));

        result.append("\nCurrent Encounter...\n");
        if (encounter != null) {
            result.append(encounter.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\n");
        result.append(String.format("Flagship Nearby:    %5b (Only set when a nearby ship is present)%n", rebelFlagshipNearby));

        result.append("\nNearby Ship...\n");
        if (nearbyShipState != null) {
            result.append(nearbyShipState.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nNearby Ship AI...\n");
        if (nearbyShipAI != null) {
            result.append(nearbyShipAI.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nEnvironment Hazards...\n");
        if (environment != null) {
            result.append(environment.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nProjectiles...\n");
        int projectileIndex = 0;
        first = true;
        for (ProjectileState projectile : projectileList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(String.format("Projectile # %2d:%n", projectileIndex++));
            result.append(projectile.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\n");
        result.append(String.format("Nu?:          %11d (Player Ship)%n", unknownNu));
        result.append(String.format("Xi?:          %11s (Nearby Ship)%n", (unknownXi != null ? unknownXi.intValue() : "N/A")));
        result.append(String.format("Autofire:           %5b%n", autofire));

        result.append("\nRebel Flagship...\n");
        if (rebelFlagshipState != null) {
            result.append(rebelFlagshipState.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nMystery Bytes...\n");
        first = true;
        for (MysteryBytes m : mysteryList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(m.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        // ...
        return result.toString();
    }
}