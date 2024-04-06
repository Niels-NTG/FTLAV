package net.blerf.ftl.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "nameList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewNameList {

    @XmlAttribute
    private String race;  // FTL ignores race.

    @XmlAttribute
    private String sex;

    @XmlElement(name = "name")
    private List<CrewName> names;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CrewName {

        @XmlAttribute()
        public String shortName;

        @XmlValue
        public String name;
    }

}
