package net.blerf.ftl.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "sectorData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorData {
    @XmlElement(name = "sectorType")
    private List<SectorType> sectorTypes;

    @XmlElement(name = "sectorDescription")
    private List<SectorDescription> sectorDescriptions;

}
