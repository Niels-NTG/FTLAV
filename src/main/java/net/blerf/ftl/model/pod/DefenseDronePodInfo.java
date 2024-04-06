package net.blerf.ftl.model.pod;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DefenseDronePodInfo extends ExtendedDronePodInfo {

    @Override
    public DefenseDronePodInfo copy() {
        return new DefenseDronePodInfo();
    }

    @Override
    public String toString() {
        return "N/A\n";
    }
}
