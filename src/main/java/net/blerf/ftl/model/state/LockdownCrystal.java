package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LockdownCrystal {
    private int currentPositionX = 0;
    private int currentPositionY = 0;
    private int speed = 0;
    private int goalPositionX = 0;
    private int goalPositionY = 0;
    private boolean arrived = false;
    private boolean done = false;
    private int lifetime = 0;
    private boolean superFreeze = false;
    private int lockingRoom = 0;
    private int animDirection = 0;
    private int shardProgress = 0;

    @Override
    public String toString() {
        return String.format("Current Position:  %8d,%8d (%9.03f,%9.03f)%n", currentPositionX, currentPositionY, currentPositionX / 1000f, currentPositionY / 1000f) +
                String.format("Speed?:            %8d%n", speed) +
                String.format("Goal Position:     %8d,%8d (%9.03f,%9.03f)%n", goalPositionX, goalPositionY, goalPositionX / 1000f, goalPositionY / 1000f) +
                String.format("Arrived?:          %8b%n", arrived) +
                String.format("Done?:             %8b%n", done) +
                String.format("Lifetime?:         %8d%n", lifetime) +
                String.format("SuperFreeze?:      %8b%n", superFreeze) +
                String.format("Locking Room?:     %8d%n", lockingRoom) +
                String.format("Anim Direction?:   %8d%n", animDirection) +
                String.format("Shard Progress?:   %8d%n", shardProgress);
    }
}
