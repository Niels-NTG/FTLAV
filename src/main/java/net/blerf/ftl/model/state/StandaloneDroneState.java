package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StandaloneDroneState {
    private String droneId = null;
    private DronePodState dronePod = null;
    private int unknownAlpha = 0;
    private int unknownBeta = 0;
    private int unknownGamma = 0;

    @Override
    public String toString() {
        return String.format("DroneId:           %s%n", droneId) +
                "\nDrone Pod...\n" +
                dronePod.toString().replaceAll("(^|\n)(.+)", "$1  $2") +
                "\n" +
                String.format("Alpha?:            %3d%n", unknownAlpha) +
                String.format("Beta?:             %3d%n", unknownBeta) +
                String.format("Gamma?:            %3d%n", unknownGamma);
    }
}
