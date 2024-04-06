package net.blerf.ftl.xml.ship;

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
public class SystemRoom {

    /**
     * Minimum random system capacity.
     * <p>
     * Systems will try to be fully powered unless the ship's reserve
     * is insufficient.
     */
    @XmlAttribute
    private int power;

    /**
     * Maximum random system capacity.
     * <p>
     * Not capped by SystemBlueprint's maxPower.
     */
    @XmlAttribute(name = "max")
    private Integer maxPower;

    @XmlAttribute(name = "room")
    private int roomId;

    /**
     * Whether this system comes pre-installed.
     * <p>
     * Treat null omissions as as true.
     * On randomly generated ships, false means it's sometimes present.
     */
    @XmlAttribute()
    private Boolean start;

    @XmlAttribute()
    private String img;

    /**
     * The room square that a system has its terminal at.
     * <p>
     * For the medbay and clonebay, this is the blocked square.
     * When omitted, each system has a different hard-coded default.
     */
    @XmlElement()
    private RoomSlot slot;
}
