package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import net.blerf.ftl.xml.ship.ShipChassis;


/**
 * A simple (x,y) object.
 *
 * @see ShipChassis
 * @see net.blerf.ftl.xml.WeaponAnim
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Offset {

    @XmlAttribute
    public int x;
    @XmlAttribute
    public int y;
}
