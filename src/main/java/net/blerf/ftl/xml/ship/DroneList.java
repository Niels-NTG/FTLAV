package net.blerf.ftl.xml.ship;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class DroneList {

    // 'count' isn't an independent field; a getter/setter calc's it (See below).

    @XmlAttribute
    public int drones;

    @XmlAttribute(name = "load")
    public String blueprintListId;

    @XmlElement(name = "drone")
    private List<DroneId> droneIds;

    public void setCount(int n) { /* No-op */ }

    @XmlAttribute(name = "count")
    public int getCount() {
        return (droneIds != null ? droneIds.size() : 0);
    }

}
