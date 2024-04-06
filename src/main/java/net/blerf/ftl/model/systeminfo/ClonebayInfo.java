package net.blerf.ftl.model.systeminfo;

import net.blerf.ftl.model.type.SystemType;

public class ClonebayInfo extends ExtendedSystemInfo {
    private int buildTicks = 0;
    private int buildTicksGoal = 0;
    private int doomTicks = 0;


    public ClonebayInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ClonebayInfo(ClonebayInfo srcInfo) {
        super(srcInfo);
        buildTicks = srcInfo.getBuildTicks();
        buildTicksGoal = srcInfo.getBuildTicksGoal();
        doomTicks = srcInfo.getDoomTicks();
    }

    @Override
    public ClonebayInfo copy() {
        return new ClonebayInfo(this);
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     */
    @Override
    public void commandeer() {
        setBuildTicks(0);
        setBuildTicksGoal(0);
        setDoomTicks(0);
    }

    /**
     * Sets elapsed time while building a clone.
     *
     * @param n a positive int less than, or equal to, the goal (0 when not engaged)
     * @see #setBuildTicksGoal(int)
     */
    public void setBuildTicks(int n) {
        buildTicks = n;
    }

    public int getBuildTicks() {
        return buildTicks;
    }


    /**
     * Sets total time needed to finish building a clone.
     * <p>
     * This can vary depending on the system level when the clonebay is
     * initially engaged. When not engaged, this value lingers.
     *
     * @see #setBuildTicks(int)
     */
    public void setBuildTicksGoal(int n) {
        buildTicksGoal = n;
    }

    public int getBuildTicksGoal() {
        return buildTicksGoal;
    }

    /**
     * Sets elapsed time while there are dead crew and the clonebay is unpowered.
     * <p>
     * This counts to 3000, at which point dead crew are lost.
     *
     * @param n 0-3000, or -1000
     */
    public void setDoomTicks(int n) {
        doomTicks = n;
    }

    public int getDoomTicks() {
        return doomTicks;
    }

    @Override
    public String toString() {
        return String.format("SystemId:                 %s%n", SystemType.CLONEBAY.getId()) +
                String.format("Build Ticks:            %7d (For the current dead crew being cloned)%n", buildTicks) +
                String.format("Build Ticks Goal:       %7d%n", buildTicksGoal) +
                String.format("DoomTicks:              %7d (If unpowered, dead crew are lost at 3000)%n", doomTicks);
    }
}
