package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.xml.ship.ShipBlueprint;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "blueprints")
@XmlAccessorType(XmlAccessType.FIELD)
public class Blueprints {

    @XmlElement(name = "blueprintList")
    private List<BlueprintList> blueprintLists = new ArrayList<>();

    @XmlElement(name = "crewBlueprint")
    private List<CrewBlueprint> crewBlueprints = new ArrayList<>();

    @XmlElement(name = "systemBlueprint")
    private List<SystemBlueprint> systemBlueprints = new ArrayList<>();

    @XmlElement(name = "weaponBlueprint")
    private List<WeaponBlueprint> weaponBlueprints = new ArrayList<>();

    @XmlElement(name = "droneBlueprint")
    private List<DroneBlueprint> droneBlueprints = new ArrayList<>();

    @XmlElement(name = "augBlueprint")
    private List<AugBlueprint> augBlueprints = new ArrayList<>();

    @XmlElement(name = "shipBlueprint")
    private List<ShipBlueprint> shipBlueprints = new ArrayList<>();

}
