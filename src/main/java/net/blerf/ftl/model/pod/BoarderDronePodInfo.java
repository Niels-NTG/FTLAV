package net.blerf.ftl.model.pod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.state.DroneState;

/**
 * Extended boarder drone info.
 * <p>
 * Boarder drones exclusively store body info in ExtendedDronePodInfo.
 * The traditional DroneState's body fields remain at inoperative defaults.
 * <p>
 * In FTL 1.01-1.03.3, Boarder drone bodies were actual crew on foreign
 * ships.
 *
 * @see DroneState
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BoarderDronePodInfo extends ExtendedDronePodInfo {
    private int unknownAlpha;
    private int unknownBeta;
    private int unknownGamma;
    private int unknownDelta;
    private int bodyHealth = 1;
    /**
     * Sets the position of the drone's body image.
     * <p>
     * Technically the roomId/square fields set the goal location.
     * This field is where the body really is, possibly en route.
     * <p>
     * It's the position of the body image's center, relative to the
     * top-left corner of the floor layout of the ship it's on.
     * <p>
     * This value lingers, even after the body is gone.
     */
    private int bodyX = -1;
    private int bodyY = -1;
    /**
     * Sets the room this drone's body is in (or at least trying to move
     * toward).
     * <p>
     * When no body is present, this is -1.
     * <p>
     * roomId and roomSquare need to be specified together.
     */
    private int bodyRoomId = -1;
    private int bodyRoomSquare = -1;

    @Override
    public BoarderDronePodInfo copy() {
        return toBuilder().build();
    }


    @Override
    public String toString() {
        return String.format("Alpha?:             %7d%n", unknownAlpha) +
                String.format("Beta?:              %7d%n", unknownBeta) +
                String.format("Gamma?:             %7d%n", unknownGamma) +
                String.format("Delta?:             %7d%n", unknownDelta) +
                String.format("Body Health:        %7d%n", bodyHealth) +
                String.format("Body Position:      %7d,%7d%n", bodyX, bodyY) +
                String.format("Body Room Id:       %7d%n", bodyRoomId) +
                String.format("Body Room Square:   %7d%n", bodyRoomSquare);
    }
}
