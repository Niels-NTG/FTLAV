package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "augBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class AugBlueprint {

    @XmlAttribute(name = "name")
    private String id;

    private DefaultDeferredText title;
    @XmlElement(name = "desc")
    private DefaultDeferredText description;

    @XmlElement(name = "bp")
    private int blueprint;

    private int cost;
    private int rarity;
    private boolean stackable;
    private float value;

    @Override
    public String toString() {
        return "" + title;
    }
}
