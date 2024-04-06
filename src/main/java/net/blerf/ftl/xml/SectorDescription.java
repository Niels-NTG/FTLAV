package net.blerf.ftl.xml;

import java.util.List;
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
@XmlRootElement(name = "sectorDescription")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorDescription {

    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute
    private int minSector;

    @XmlAttribute
    private boolean unique;

    private NameList nameList;

    private TrackList trackList;

    private RarityList rarityList;

    @XmlElement()
    private String startEvent;

    @XmlElement(name = "event")
    private List<EventDistribution> eventDistributions;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class NameList {
        @XmlElement(name = "name")
        public List<DefaultDeferredText> names;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TrackList {
        @XmlElement(name = "track")
        public List<String> tracks;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RarityList {
        @XmlElement(name = "blueprint")
        public List<BlueprintRarity> blueprints;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BlueprintRarity {
        @XmlAttribute(name = "name")
        public String id;

        @XmlAttribute
        public int rarity;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EventDistribution {
        @XmlAttribute
        public String name;

        @XmlAttribute
        public int min;

        @XmlAttribute
        public int max;
    }

}
