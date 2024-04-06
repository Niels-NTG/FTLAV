package net.blerf.ftl.xml;

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
@XmlRootElement(name = "weaponAnim")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeaponAnim {

    @XmlAttribute(name = "name")
    private String id;

    @XmlElement(name = "sheet")
    private String sheetId;

    @XmlElement(name = "desc")
    private AnimSpec spec;

    private int chargedFrame;
    private int fireFrame;

    private Offset firePoint;
    private Offset mountPoint;

    @XmlElement(name = "delayChargeAnim")
    private Float chargeDelay;

    @XmlElement(name = "chargeImage")
    private String chargeImagePath;

    @Override
    public String toString() {
        return "" + id;
    }
}
