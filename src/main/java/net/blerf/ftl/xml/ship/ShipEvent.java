package net.blerf.ftl.xml.ship;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "ship")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipEvent {

    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute(name = "load")
    private String load;

    @XmlAttribute
    private boolean hostile = false;

    private int seed;

    @XmlAttribute(name = "auto_blueprint")
    private String autoBlueprintId;

    @Override
    public String toString() {
        if (id != null)
            return "" + id;
        else if (load != null)
            return "" + load;
        return "<null>";
    }
}
