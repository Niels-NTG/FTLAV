package net.blerf.ftl.model.pod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Extended Combat/Beam/Ship_Repair drone info.
 * <p>
 * These drones flit to a random (?) point, stop, then move to another,
 * and so on.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ZigZagDronePodInfo extends ExtendedDronePodInfo {
    /**
     * Sets the cached position from when the drone last stopped.
     * <p>
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this has no effect, appearing to be read-only field for reference.
     * a pseudo-float
     */
    private int lastWaypointX;
    private int lastWaypointY;

    /**
     * Sets time elapsed while this drone moves.
     * <p>
     * This increments from 0 to 1000 as the drone drifts toward a new
     * waypoint. While this value is below 200, exhaust flames are
     * visible. Then they vanish. The moment the drone pauses at the
     * destination, this is set to 1000.
     * <p>
     * When not set, this is MIN_INT. This happens when stationary while
     * stunned.
     * <p>
     * Observed values: 153 (stunned drift begins), 153000 (mid drift),
     * 153000000 (near end of drift).
     * <p>
     * TODO: Modify this value in the editor.
     * In CheatEngine, changing this has no effect, appearing to be read-only field for reference.
     */
    private int transitTicks;
    /**
     * Sets the angle to display exhaust flames thrusting toward.
     * Pseudo-float (n degrees clockwise from east)
     * When not set, this is MIN_INT.
     * <p>
     * TODO: Modify this value in the editor.
     * In CheatEngine, changing this DOES work.
     */
    private int exhaustAngle;
    /**
     * When not set, this is MIN_INT.
     */
    private int unknownEpsilon;

    @Override
    public ZigZagDronePodInfo copy() {
        return toBuilder().build();
    }

    @Override
    public String toString() {
        return String.format("Last Waypoint:      %7d,%7d%n", lastWaypointX, lastWaypointY) +
                String.format("TransitTicks:       %7s%n", (transitTicks == Integer.MIN_VALUE ? "N/A" : transitTicks)) +
                String.format("Exhaust Angle:      %7s%n", (exhaustAngle == Integer.MIN_VALUE ? "N/A" : exhaustAngle)) +
                String.format("Epsilon?:           %7s%n", (unknownEpsilon == Integer.MIN_VALUE ? "N/A" : unknownEpsilon));
    }
}
