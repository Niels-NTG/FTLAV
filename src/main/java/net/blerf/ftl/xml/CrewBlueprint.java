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
@XmlRootElement(name = "crewBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewBlueprint {

    @XmlAttribute(name = "name")
    private String id;

    private DefaultDeferredText desc;
    private int cost;

    @XmlElement(name = "bp")
    private int blueprint;

    private DefaultDeferredText title;

    @XmlElement(name = "short")
    private DefaultDeferredText shortTitle;

    private int rarity;

    @XmlElementWrapper(name = "powerList")
    @XmlElement(name = "power")
    private List<DefaultDeferredText> powerList;

    @XmlElementWrapper(name = "colorList")
    @XmlElement(name = "layer")
    private List<SpriteTintLayer> spriteTintLayerList;  // FTL 1.5.4 introduced sprite tinting.

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SpriteTintLayer {

        @XmlElement(name = "color")
        public List<SpriteTintColor> tintList;

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class SpriteTintColor {
            @XmlAttribute
            public int r, g, b;
            @XmlAttribute
            public float a;
        }
    }

}
