package net.blerf.ftl.xml.ship;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.xml.DefaultDeferredText;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "shipBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipBlueprint {

    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute(name = "layout")
    private String layoutId;

    @XmlAttribute(name = "img")
    private String graphicsBaseName;

    @XmlElement(name = "class")
    private DefaultDeferredText shipClass;

    @XmlElement()
    private DefaultDeferredText name;

    @XmlElement()
    private DefaultDeferredText unlockTip;

    @XmlElement()
    private DefaultDeferredText desc;

    private SystemList systemList;

    @XmlElement()  // Not present in autoBlueprints.xml.
    private Integer weaponSlots, droneSlots;

    @XmlElement()
    private WeaponList weaponList;

    @XmlElement(name = "aug")
    private List<AugmentId> augmentIds;

    @XmlElement()
    private DroneList droneList;

    private Health health;
    private MaxPower maxPower;   // Initial reserve power capacity.
    private CrewCount crewCount;

    @XmlElement()
    private String boardingAI;  // Only present in autoBlueprints.xml.

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Health {
        @XmlAttribute
        public int amount;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MaxPower {
        @XmlAttribute
        public int amount;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CrewCount {
        @XmlAttribute
        public int amount;

        @XmlAttribute()
        public Integer max;  // Only present in autoBlueprints.xml.

        @XmlAttribute(name = "class")
        public String race;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", id, shipClass);
    }
}
