package net.blerf.ftl.xml.ship;

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
@XmlRootElement(name = "shipEvents")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipEvents {

    @XmlElement(name = "ship")
    private List<ShipEvent> shipEvents;

}
