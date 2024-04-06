package net.blerf.ftl.model.systeminfo;

import net.blerf.ftl.model.state.DoorState;
import net.blerf.ftl.model.state.DronePodState;
import net.blerf.ftl.model.state.SystemState;
import net.blerf.ftl.model.type.SystemType;

/**
 * Extended info about the Hacking system.
 *
 * @see DoorState
 * @see SystemState#setHacked(boolean)
 * @see SystemState#setHackLevel(int)
 */
public class HackingInfo extends ExtendedSystemInfo {
    private SystemType targetSystemType = null;
    private int unknownBeta = 0;
    private boolean dronePodVisible = false;
    private int unknownDelta = 0;

    private int unknownEpsilon = 0;
    private int unknownZeta = 0;
    private int unknownEta = 0;

    private int disruptionTicks = 0;
    private int disruptionTicksGoal = 10000;
    private boolean disrupting = false;

    private DronePodState dronePod = null;


    /**
     * Constructs an incomplete HackingInfo.
     * <p>
     * It will need a hacking DronePodState.
     */
    public HackingInfo() {
        super();
    }

    /**
     * Copy constructor.
     * <p>
     * The DronePodState will be copy-constructed as well.
     */
    protected HackingInfo(HackingInfo srcInfo) {
        super(srcInfo);
        targetSystemType = srcInfo.getTargetSystemType();
        unknownBeta = srcInfo.getUnknownBeta();
        dronePodVisible = srcInfo.isDronePodVisible();
        unknownDelta = srcInfo.getUnknownDelta();
        unknownEpsilon = srcInfo.getUnknownEpsilon();
        unknownZeta = srcInfo.getUnknownZeta();
        unknownEta = srcInfo.getUnknownEta();
        disruptionTicks = srcInfo.getDisruptionTicks();
        disruptionTicksGoal = srcInfo.getDisruptionTicksGoal();
        disrupting = srcInfo.isDisrupting();
        dronePod = new DronePodState(srcInfo.getDronePod());
    }

    @Override
    public HackingInfo copy() {
        return new HackingInfo(this);
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    @Override
    public void commandeer() {
        setTargetSystemType(null);
        setUnknownBeta(0);
        setDronePodVisible(false);
        setUnknownDelta(0);

        setUnknownEpsilon(0);
        setUnknownZeta(0);
        setUnknownEta(0);

        setDisruptionTicks(0);
        setDisruptionTicksGoal(10000);
        setDisrupting(false);

        if (getDronePod() != null) {
            getDronePod().commandeer();
        }
    }

    /**
     * Sets the target system to hack.
     * <p>
     * This is set when the drone pod is launched. Pressing the hack
     * button to select a system while paused will have no immediate
     * effect on a saved game; unpausing is necessary for tha pod to
     * launch and commit the changes.
     * <p>
     * Editing this value when a system had already been hacked will not
     * unhack the original system. Upon loading, FTL will modify the new
     * system's hacked and hackLevel values, reveal the room, lock the
     * doors, etc. If edited while disrupting, the previous system will
     * stay disrupted indefinitely. Only the current system will return to
     * normal when disruptionTicks reaches its goal.
     * <p>
     * FTL 1.5.13 bug: The hacking system only remembers the type of system
     * targeted, not a specific room. The rebel flagship has multiple
     * artillery rooms. An in-game choice to hack any of the right three
     * rooms will set the 'hacked' flag on that SystemState, but upon
     * reloading, the hacking system will seek the *first* artillery room
     * (the leftmost one) instead, which will get marked as 'hacked' and be
     * subject to disruption. The original room will still have its flag
     * lingering from before, but the hacking system only affects one room
     * and it already picked the left one. Both flagged rooms will be
     * revealed, but disruption will only affect the left one.
     * <p>
     * When not set, this is null.
     */
    public void setTargetSystemType(SystemType systemType) {
        targetSystemType = systemType;
    }

    public SystemType getTargetSystemType() {
        return targetSystemType;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: Went from 0 to 1 when drone pod was launched.
     * Went from 1 to 0 when hacking system was inoperative (either from
     * damage or depowering) while the pod was still attached.
     */
    public void setUnknownBeta(int n) {
        unknownBeta = n;
    }

    public int getUnknownBeta() {
        return unknownBeta;
    }

    /**
     * Sets the drone pod's visibility.
     * <p>
     * Editing this to false after the drone pod has been launched will
     * only make the pod invisible. The Hacking system will continue to
     * function normally as if the pod were there.
     * <p>
     * Observed values: true (when launched), false (when the nearby ship
     * is defeated and has disappeared).
     */
    public void setDronePodVisible(boolean b) {
        dronePodVisible = b;
    }

    public boolean isDronePodVisible() {
        return dronePodVisible;
    }

    /**
     * Unknown.
     * <p>
     * Went from 0 to 1 when hacking drone pod was launched.
     */
    public void setUnknownDelta(int n) {
        unknownDelta = n;
    }

    public int getUnknownDelta() {
        return unknownDelta;
    }

    public void setUnknownEpsilon(int n) {
        unknownEpsilon = n;
    }

    public int getUnknownEpsilon() {
        return unknownEpsilon;
    }

    public void setUnknownZeta(int n) {
        unknownZeta = n;
    }

    public int getUnknownZeta() {
        return unknownZeta;
    }

    public void setUnknownEta(int n) {
        unknownEta = n;
    }

    public int getUnknownEta() {
        return unknownEta;
    }

    /**
     * Sets elapsed time while systems are disrupted.
     * <p>
     * When this is not set, it is 0. After reaching or passing the goal,
     * this value lingers.
     * <p>
     * When the goal is reached, the Hacking system will get 4 ionized bars
     * (ionized bars had been -1 while disrupting).
     *
     * @param n a positive int less than, or equal to, the goal
     * @see #setDisruptionTicksGoal(int)
     */
    public void setDisruptionTicks(int n) {
        disruptionTicks = n;
    }

    public int getDisruptionTicks() {
        return disruptionTicks;
    }

    /**
     * Sets total time systems will stay disrupted.
     * <p>
     * This can vary depending on the system level when disruption is
     * initially engaged. When not engaged, this is 10000!?
     *
     * @see #setDisruptionTicks(int)
     */
    public void setDisruptionTicksGoal(int n) {
        disruptionTicksGoal = n;
    }

    public int getDisruptionTicksGoal() {
        return disruptionTicksGoal;
    }

    /**
     * Sets whether an enemy system is currently being disrupted.
     *
     * @see SystemState#setHackLevel(int)
     */
    public void setDisrupting(boolean b) {
        disrupting = b;
    }

    public boolean isDisrupting() {
        return disrupting;
    }

    public void setDronePod(DronePodState pod) {
        dronePod = pod;
    }

    public DronePodState getDronePod() {
        return dronePod;
    }


    private String prettyInt(int n) {
        if (n == Integer.MIN_VALUE) return "MIN";
        if (n == Integer.MAX_VALUE) return "MAX";

        return String.format("%d", n);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append(String.format("SystemId:                 %s%n", SystemType.HACKING.getId()));
        result.append(String.format("Target SystemId:          %s%n", (targetSystemType != null ? targetSystemType.getId() : "N/A")));
        result.append(String.format("Beta?:                  %7d%n", unknownBeta));
        result.append(String.format("Drone Pod Visible:      %7b%n", dronePodVisible));
        result.append(String.format("Delta?:                 %7d%n", unknownDelta));
        result.append(String.format("Epsilon?:               %7d%n", unknownEpsilon));
        result.append(String.format("Zeta?:                  %7d%n", unknownZeta));
        result.append(String.format("Eta?:                   %7d%n", unknownEta));
        result.append(String.format("Disruption Ticks:       %7d%n", disruptionTicks));
        result.append(String.format("Disruption Ticks Goal:  %7d%n", disruptionTicksGoal));
        result.append(String.format("Disrupting:             %7b%n", disrupting));

        result.append("\nDrone Pod...\n");
        if (dronePod != null) {
            result.append(dronePod.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
