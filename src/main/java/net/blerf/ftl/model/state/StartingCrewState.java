package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.type.CrewType;

@Getter
@Setter
@NoArgsConstructor
public class StartingCrewState {

    private String name = "Frank";
    private CrewType race = CrewType.HUMAN;

    @Override
    public String toString() {
        return String.format("Name: %s%n", name) +
                String.format("Race: %s%n", race.getId());
    }
}
