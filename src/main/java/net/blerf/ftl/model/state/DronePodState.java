package net.blerf.ftl.model.state;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.pod.ExtendedDronePodInfo;
import net.blerf.ftl.model.type.DroneType;

@Getter
@Setter
@NoArgsConstructor
public class DronePodState {
    private DroneType droneType = null;
    /**
     * Sets time elapsed while this drone is un-redeployable after
     * destruction.
     * <p>
     * This value begins decrementing from 10000 when the drone pod is
     * destroyed and the deathAnim completes. After reaching or passing 0,
     * this value lingers.
     */
    private int mourningTicks = 0;
    private int currentSpace = 0;
    /**
     * Sets which ship to eventually use as the origin for position
     * coordinates.
     * <p>
     * This value is initially -1. It is set to one of the ships when the
     * drone pod is deployed. Then this value lingers.
     *
     * @param n player ship (0) or nearby ship (1) or none (-1)
     * @see #setCurrentSpace(int)
     */
    private int destinationSpace = -1;
    private int currentPositionX = 0;
    private int currentPositionY = 0;
    private int previousPositionX = 0;
    private int previousPositionY = 0;
    private int goalPositionX = 0;
    private int goalPositionY = 0;

    // This block was formerly a length 6 array named beta.
    private int unknownEpsilon = Integer.MIN_VALUE;
    private int unknownZeta = Integer.MIN_VALUE;
    private int nextTargetX = Integer.MIN_VALUE;
    private int nextTargetY = Integer.MIN_VALUE;
    private int unknownIota = Integer.MIN_VALUE;
    private int unknownKappa = Integer.MIN_VALUE;

    // This block was formerly a length 14 array named gamma.
    private int buildupTicks = -1000;
    private int stationaryTicks = 0;
    private int cooldownTicks = 0;
    /**
     * Sets the drone's orbital progress around the shield ellipse.
     * <p>
     * Drones which do not orbit will have some lingering value instead.
     * <p>
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this has no effect, appearing to be read-only field for reference.
     *
     * @param n a pseudo-float (n degrees clockwise from east)
     */
    private int orbitAngle = 0;
    /**
     * Sets the drone's turret angle.
     * <p>
     * When not set, this is 0.
     *
     * @param n a pseudo-float (n degrees clockwise from east)
     */
    private int turretAngle = 0;
    /**
     * Unknown.
     * <p>
     * Might be facing, to rotate the entire drone?
     * <p>
     * Observed values: Hacking (U:-89 L:179 R:8.745 D:89); Combat drones
     * have strange values. Boarder (Ion Drone) body in flight is rotated
     * as expected (Eastward:0 SW:121), and turret its value is synched.
     * <p>
     * When not set, this is 0.
     *
     * @param n a pseudo-float (n degrees clockwise from east)
     */
    private int unknownXi = 0;
    /**
     * Sets the number of waypoints this drone should arrive at before
     * disappearing.
     * <p>
     * This value decrements the moment this drone finishes idling at one
     * waypoint and begins moving toward the next. After reaching 0, the
     * drone vanishes. Then this value lingers.
     * <p>
     * When not set, this is MAX_INT.
     * <p>
     * Observed values: 4, 3, 2, 1, 0 (Ship_Repair).
     */
    private int hopsToLive = Integer.MAX_VALUE;
    private int unknownPi = 0;
    /**
     * Unknown.
     * <p>
     * Observed values: 1, 0.
     */
    private int unknownRho = 0;
    /**
     * Sets time elapsed while this drone is stunned, with a chance of
     * exploding.
     * <p>
     * This value begins decrementing from a positive integer after taking
     * ion damage (e.g., from an Anti-Combat Drone). After reaching 0, the
     * drone returns to normal.
     * <p>
     * While stunned, the drone will halt movement, it'll be covered in
     * arcs of electricity, and the turret will spin rapidly. It may
     * explode at a random moment prior to reaching 0 - at which point,
     * this value will be set to 0.
     * <p>
     * When not set, this is 0. This value lingers and may even end up a
     * little negative.
     * <p>
     * Observed values: 4378 (Combat drone shot by Anti-Combat Drone)
     * <p>
     * TODO: It's unclear what determines if/when an explosion occurs.
     */
    private int overloadTicks = 0;
    /**
     * Unknown.
     * <p>
     * Observed values: -5704; -173834; 110067, 230637.
     * <p>
     * When not set, this is -1000.
     */
    private int unknownTau = -1000;
    /**
     * Unknown.
     * <p>
     * Observed values: 1.
     */
    private int unknownUpsilon = 0;
    /**
     * Sets the recent change in position (Current - Previous + 1).
     * <p>
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this has no effect, appearing to be read-only field for reference.
     *
     * @param n a pseudo-float
     * @see #setCurrentPositionX(int)
     * @see #setPreviousPositionX(int)
     */
    private int deltaPositionX = 0;
    private int deltaPositionY = 0;

    private AnimState deathAnim = new AnimState();
    private ExtendedDronePodInfo extendedInfo = null;


    /**
     * Copy constructor.
     */
    public DronePodState(DronePodState srcPod) {
        droneType = srcPod.getDroneType();
        mourningTicks = srcPod.getMourningTicks();
        currentSpace = srcPod.getCurrentSpace();
        destinationSpace = srcPod.getDestinationSpace();
        currentPositionX = srcPod.getCurrentPositionX();
        currentPositionY = srcPod.getCurrentPositionY();
        previousPositionX = srcPod.getPreviousPositionX();
        previousPositionY = srcPod.getPreviousPositionY();
        goalPositionX = srcPod.getGoalPositionX();
        goalPositionY = srcPod.getGoalPositionY();

        unknownEpsilon = srcPod.getUnknownEpsilon();
        unknownZeta = srcPod.getUnknownZeta();
        nextTargetX = srcPod.getNextTargetX();
        nextTargetY = srcPod.getNextTargetY();
        unknownIota = srcPod.getUnknownIota();
        unknownKappa = srcPod.getUnknownKappa();

        buildupTicks = srcPod.getBuildupTicks();
        stationaryTicks = srcPod.getStationaryTicks();
        cooldownTicks = srcPod.getCooldownTicks();
        orbitAngle = srcPod.getOrbitAngle();
        turretAngle = srcPod.getTurretAngle();
        unknownXi = srcPod.getUnknownXi();
        hopsToLive = srcPod.getHopsToLive();
        unknownPi = srcPod.getUnknownPi();
        unknownRho = srcPod.getUnknownRho();
        overloadTicks = srcPod.getOverloadTicks();
        unknownTau = srcPod.getUnknownTau();
        unknownUpsilon = srcPod.getUnknownUpsilon();
        deltaPositionX = srcPod.getDeltaPositionX();
        deltaPositionY = srcPod.getDeltaPositionY();

        deathAnim = srcPod.getDeathAnim();

        if (srcPod.getExtendedInfo(ExtendedDronePodInfo.class) != null) {
            extendedInfo = srcPod.getExtendedInfo(ExtendedDronePodInfo.class).copy();
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        setMourningTicks(0);
        setCurrentSpace(0);
        setDestinationSpace(-1);

        //setNextTargetX( Integer.MIN_VALUE )?
        //setNextTargetY( Integer.MIN_VALUE )?

        setBuildupTicks(-1000);
        setStationaryTicks(0);

        setOverloadTicks(0);

        // TODO: Unknowns.

        getDeathAnim().setPlaying(false);
        getDeathAnim().setCurrentFrame(0);
        getDeathAnim().setProgressTicks(0);

        if (getExtendedInfo(ExtendedDronePodInfo.class) != null) {
            getExtendedInfo(ExtendedDronePodInfo.class).commandeer();
        }
    }

    /**
     * Sets which ship to use as the origin for position coordinates.
     *
     * @param n player ship (0) or nearby ship (1)
     * @see #setDestinationSpace(int)
     */
    public void setCurrentSpace(int n) {
        currentSpace = n;
    }

    public int getCurrentSpace() {
        return currentSpace;
    }


    /**
     * Unknown.
     * <p>
     * Observed values: Always MIN_INT?
     */
    public void setUnknownEpsilon(int n) {
        unknownEpsilon = n;
    }

    public int getUnknownEpsilon() {
        return unknownEpsilon;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: Always MIN_INT?
     */
    public void setUnknownZeta(int n) {
        unknownZeta = n;
    }

    public int getUnknownZeta() {
        return unknownZeta;
    }

    /**
     * Sets the position where this drone's next projectile will end up.
     * <p>
     * This is analogous to weapon targeting reticles deciding the next
     * target. The drone may spin or maneuver in transit, but it will
     * ultimately turn to face this point before firing.
     * <p>
     * Combat/Ship_Repair drones set a new value after each shot. Defense
     * drones intermittently set it (e.g., directly at an opposing drone)
     * and unset it.
     * <p>
     * When not set, this is MIN_INT.
     *
     * @param n a pseudo-float
     * @see WeaponModuleState#setCurrentTargets(List)
     */
    public void setNextTargetX(int n) {
        nextTargetX = n;
    }

    public void setNextTargetY(int n) {
        nextTargetY = n;
    }

    public int getNextTargetX() {
        return nextTargetX;
    }

    public int getNextTargetY() {
        return nextTargetY;
    }

    /**
     * Unknown.
     * <p>
     * When not set, this is MIN_INT.
     * <p>
     * Observed values: Defense (erratic +/- 0-20000); 962, 144, -988.
     */
    public void setUnknownIota(int n) {
        unknownIota = n;
    }

    public int getUnknownIota() {
        return unknownIota;
    }

    /**
     * Unknown.
     * <p>
     * When not set, this is MIN_INT.
     * <p>
     * Observed values: Defense (erratic +/- 0-20000); -2384, 26, 2373.
     */
    public void setUnknownKappa(int n) {
        unknownKappa = n;
    }

    public int getUnknownKappa() {
        return unknownKappa;
    }


    /**
     * Sets time elapsed while this drone is about to fire.
     * <p>
     * Drones telegraph when they're about to fire, a light will change
     * color (COMBAT_1) or glow intensely (COMBAT_BEAM). While positive,
     * this value decrements to 0. At that point, this is set to -1000,
     * firing occurs and a projectile is launched.
     * <p>
     * Observed values: 365, 43 (COMBAT_BEAM); 500 (when a launched Hacking
     * drone entered the target ship space).
     * <p>
     * When not set, this is -1000.
     */
    public void setBuildupTicks(int n) {
        buildupTicks = n;
    }

    public int getBuildupTicks() {
        return buildupTicks;
    }

    /**
     * Sets time elapsed while this drone is stationary.
     * <p>
     * While positive, this value decrements to 0. The drone will be
     * completely still for the duration. Beam drones set this when
     * buildupTicks reaches 0, so the drone will hold still to be the
     * beam's origin point.
     * <p>
     * This works on Combat drones, too, if edited.
     * <p>
     * Observed values: 470, 322, 167 (COMBAT_BEAM).
     * <p>
     * When not set, this is 0.
     */
    public void setStationaryTicks(int n) {
        stationaryTicks = n;
    }

    public int getStationaryTicks() {
        return stationaryTicks;
    }

    /**
     * Sets time elapsed while this drone is unable to shoot again after
     * firing.
     * <p>
     * This is based on the 'cooldown' tag of the DroneBlueprint's xml.
     * <p>
     * While positive, for Defense and Shield drones, this value decrements
     * to 0. The drone will be passive (e.g., not firing) for the duration.
     * After reaching or passing 0, this is set to -1000.
     * <p>
     * A Defense drone's light will turn red while passive (as opposed to
     * green).
     * <p>
     * Combat and Beam drones leave this at the xml's value without ever
     * decrementing.
     * <p>
     * When not set, this is 0.
     * <p>
     * TODO: Check Hacking and Ship_Repair drones.
     */
    public void setCooldownTicks(int n) {
        cooldownTicks = n;
    }


    public <T extends ExtendedDronePodInfo> T getExtendedInfo(Class<T> infoClass) {
        if (extendedInfo == null) return null;
        return infoClass.cast(extendedInfo);
    }


    private String prettyInt(int n) {
        if (n == Integer.MIN_VALUE) return "MIN";
        if (n == Integer.MAX_VALUE) return "MAX";

        return String.format("%d", n);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Drone Type:        %s%n", droneType.getId()));
        result.append(String.format("Mourning Ticks:    %7d (Decrements to 0 from 10000)%n", mourningTicks));
        result.append(String.format("Current Space:     %7d%n", currentSpace));
        result.append(String.format("Destination Space: %7d%n", destinationSpace));
        result.append(String.format("Current Position:  %7s,%7s%n", prettyInt(currentPositionX), prettyInt(currentPositionY)));
        result.append(String.format("Previous Position: %7s,%7s%n", prettyInt(previousPositionX), prettyInt(previousPositionY)));
        result.append(String.format("Goal Position:     %7s,%7s%n", prettyInt(goalPositionX), prettyInt(goalPositionY)));

        result.append(String.format("%n"));
        result.append(String.format("Epsilon?, Zeta?:   %7s,%7s%n", prettyInt(unknownEpsilon), prettyInt(unknownZeta)));
        result.append(String.format("Next Target:       %7s,%7s%n", prettyInt(nextTargetX), prettyInt(nextTargetY)));
        result.append(String.format("Iota?, Kappa?:     %7s,%7s%n", prettyInt(unknownIota), prettyInt(unknownKappa)));

        result.append(String.format("%n"));
        result.append(String.format("Buildup Ticks:     %7d (Decrements to 0 while about to fire)%n", buildupTicks));
        result.append(String.format("Stationary Ticks:  %7d (Decrements to 0 while stationary)%n", stationaryTicks));
        result.append(String.format("Cooldown Ticks:    %7d (Decrements to 0 while passive, Defense/Shield only)%n", cooldownTicks));
        result.append(String.format("Orbit Angle:       %7d%n", orbitAngle));
        result.append(String.format("Turret Angle:      %7d%n", turretAngle));
        result.append(String.format("Xi?:               %7d%n", unknownXi));
        result.append(String.format("Hops to Live:      %7s (Waypoints to idle at before undeploying)%n", prettyInt(hopsToLive)));
        result.append(String.format("Pi?:               %7d%n", unknownPi));
        result.append(String.format("Rho?:              %7d%n", unknownRho));
        result.append(String.format("Overload Ticks:    %7d (Decrements to 0 while shocked by ion weapons)%n", overloadTicks));
        result.append(String.format("Tau?:              %7d%n", unknownTau));
        result.append(String.format("Upsilon?:          %7d%n", unknownUpsilon));
        result.append(String.format("Delta Position:    %7d,%7d (Current - Previous + 1)%n", deltaPositionX, deltaPositionY));

        result.append("\nDeath Anim...\n");
        if (deathAnim != null) {
            result.append(deathAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nExtended Drone Pod Info... (Varies by Drone Type)\n");
        if (extendedInfo != null) {
            result.append(extendedInfo.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
