package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * For FTL 1.5.4+ saved games, extended info may be needed.
 */
@Getter
@Setter
@NoArgsConstructor
public class DroneState {
    private String droneId = null;
    /**
     * Sets whether this drone is powered.
     *
     * @see ExtendedDroneInfo#setArmed(boolean)
     */
    private boolean armed = false;
    /**
     * Sets whether this drone is controlled by the player.
     * <p>
     * When the drone is not armed, this should be set to false.
     */
    private boolean playerControlled = false;
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
     * <p>
     * Note: This is only set by drones which have a body on their own ship.
     */
    private int bodyX = -1;
    private int bodyY = -1;
    /**
     * Sets the room this drone's body is in (or at least trying to move
     * toward).
     * <p>
     * When body is not present, this is -1.
     * <p>
     * roomId and roomSquare need to be specified together.
     * <p>
     * Note: This is only set by drones which have a body on their own ship.
     */
    private int bodyRoomId = -1;
    private int bodyRoomSquare = -1;
    private int health = 1;
    /**
     * Sets additional drone fields.
     * <p>
     * Advanced Edition added extra drone fields at the end of saved game
     * files. They're nested inside this class for convenience.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private ExtendedDroneInfo extendedDroneInfo = null;

    /**
     * Copy constructor.
     * <p>
     * The extended info will be copy-constructed as well.
     */
    public DroneState(DroneState srcDrone) {
        droneId = srcDrone.getDroneId();
        armed = srcDrone.isArmed();
        playerControlled = srcDrone.isPlayerControlled();
        bodyX = srcDrone.getBodyX();
        bodyY = srcDrone.getBodyY();
        bodyRoomId = srcDrone.getBodyRoomId();
        bodyRoomSquare = srcDrone.getBodyRoomSquare();
        health = srcDrone.getHealth();

        if (srcDrone.getExtendedDroneInfo() != null) {
            extendedDroneInfo = new ExtendedDroneInfo(srcDrone.getExtendedDroneInfo());
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {
        setArmed(false);
        setPlayerControlled(false);

        if (getExtendedDroneInfo() != null) {
            getExtendedDroneInfo().commandeer();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("DroneId:           %s%n", droneId));
        result.append(String.format("Armed:             %5b%n", armed));
        result.append(String.format("Health:            %5d%n", health));
        result.append(String.format("Body Position:     %3d,%3d%n", bodyX, bodyY));
        result.append(String.format("Body Room Id:      %5d%n", bodyRoomId));
        result.append(String.format("Body Room Square:  %5d%n", bodyRoomSquare));
        result.append(String.format("Player Controlled: %5b%n", playerControlled));

        result.append("\nExtended Drone Info...\n");
        if (extendedDroneInfo != null) {
            result.append(extendedDroneInfo.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
