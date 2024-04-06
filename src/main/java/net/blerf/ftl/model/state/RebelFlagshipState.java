package net.blerf.ftl.model.state;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Info used for spawning the rebel flagship.
 * <p>
 * Whereas regular ship encounters are preserved in BeaconStates for repeat
 * visits, the flagship is not tied to a location.
 * <p>
 * In FTL 1.01-1.03.3, this info is not present in saved games until
 * after engaging the rebel flagship in sector 8 for the first time.
 * <p>
 * In FTL 1.5.4, this is always present, though the occupancy map may be
 * empty.
 */
public class RebelFlagshipState {
    private int unknownAlpha = 0;
    private int pendingStage = 1;
    private int unknownGamma = 30000;
    private int unknownDelta = 0;
    private final Map<Integer, Integer> occupancyMap = new LinkedHashMap<>();


    /**
     * Constructor.
     */
    public RebelFlagshipState() {
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 0 (normal), 1 (after encountering
     * first-stage boss), 2 (after encountering second-stage boss), 3
     * (after encountering third-stage boss).
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownAlpha(int n) {
        unknownAlpha = n;
    }

    public int getUnknownAlpha() {
        return unknownAlpha;
    }

    /**
     * Sets the next version of the flagship that will be encountered (1-based).
     * <p>
     * This must be one of the available stages: 1-3.
     */
    public void setPendingStage(int pendingStage) {
        this.pendingStage = pendingStage;
    }

    public int getPendingStage() {
        return pendingStage;
    }

    /**
     * Unknown.
     * <p>
     * During the third-stage boss fight, this does not change.
     * <p>
     * Observed values: 30000 (normal), 21326 (after encountering
     * first-stage boss).
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownGamma(int n) {
        unknownGamma = n;
    }

    public int getUnknownGamma() {
        return unknownGamma;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 0 (normal), 240 (after encountering
     * first-stage boss), 26563 (after defeating second-stage boss). Seems
     * to have no effect on first-stage boss, but this changes nonetheless.
     * During the second-stage boss, counts to ~25000, then it resets to 0,
     * and surge drones appear. During the third-stage boss, counts to
     * ~16000, then it either recharges its Zoltan shield or fires lots of
     * laser projectiles.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownDelta(int n) {
        unknownDelta = n;
    }

    public int getUnknownDelta() {
        return unknownDelta;
    }

    /**
     * Sets whether a room had crew members in the last seen layout.
     * <p>
     * Stage 1 sets this, but doesn't read it.
     * Fleeing stage 1, editing, then returning only results in a fresh
     * fight.
     * <p>
     * Upon first engaging stage 2, the layout is migrated.
     * The occupancy list is truncated to the new layout's rooms.
     * (The blueprints happen to have matching low roomIds.)
     * <p>
     * Stage 1 (BOSS_1): 19 rooms.
     * Stage 2 (BOSS_2): 15 rooms.
     * Stage 3 (BOSS_3): 11 rooms.
     * Having 0 rooms occupied is allowed, meaning AI took over.
     * <p>
     * Stage 2 will respond to pre-skirmish editing.
     * <p>
     * Stage 3 probably will, too. (TODO: Confirm this.)
     *
     * @param roomId a room in the last seen stage's shipLayout
     * @param n      the number of crew in that room
     */
    public void setPreviousOccupancy(int roomId, int n) {
        occupancyMap.put(roomId, n);
    }

    public Map<Integer, Integer> getOccupancyMap() {
        return occupancyMap;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Alpha?:                 %11d%n", unknownAlpha));
        result.append(String.format("Pending Flagship Stage: %11d%n", pendingStage));
        result.append(String.format("Gamma?:                 %11d%n", unknownGamma));
        result.append(String.format("Delta?:                 %11d%n", unknownDelta));

        result.append("\nOccupancy of Last Seen Flagship...\n");
        for (Map.Entry<Integer, Integer> entry : occupancyMap.entrySet()) {
            int roomId = entry.getKey();
            int occupantCount = entry.getValue();

            result.append(String.format("Room Id: %2d, Crew: %d%n", roomId, occupantCount));
        }

        return result.toString();
    }
}
