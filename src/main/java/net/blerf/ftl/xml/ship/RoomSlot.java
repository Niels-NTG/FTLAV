package net.blerf.ftl.xml.ship;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class RoomSlot {

    /**
     * The direction crew will face when standing at the terminal.
     */
    @XmlElement()
    private String direction;

    private int number;  // The room square.

}
