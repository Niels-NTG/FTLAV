package net.blerf.ftl.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "systemBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemBlueprint {

    @XmlAttribute(name = "name")
    private String id;

    private String type;
    private DefaultDeferredText title;
    private DefaultDeferredText desc;
    private int startPower;  // Initial system capacity.
    private int maxPower;    // Highest possible capacity attainable by upgrading.
    private int rarity;

    @XmlElementWrapper(name = "upgradeCost")
    @XmlElement(name = "level")
    private List<Integer> upgradeCosts;

    private int cost;

    @XmlElement()
    private Integer locked;

}
