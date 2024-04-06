package net.blerf.ftl.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "weaponBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeaponBlueprint {

    @XmlAttribute(name = "name")
    private String id;
    private String type;
    private DefaultDeferredText title;
    private Integer locked;

    @XmlElement(name = "short")
    private DefaultDeferredText shortTitle;

    @XmlElement(name = "desc")
    private DefaultDeferredText description;
    @XmlElement(name = "tooltip")
    private DefaultDeferredText tooltip;

    @XmlElement(name = "sp")
    private int shieldPiercing;

    @XmlElement(name = "bp")
    private int blueprint;

    private int damage;
    private int shots;
    private int fireChance;
    private int breachChance;
    private int cooldown;
    private int power;
    private int cost;
    private int rarity;

    @XmlElement(name = "image")
    private String projectileAnimId;  // Projectile / Beam-spot anim.

    @XmlElementWrapper(name = "launchSounds")
    @XmlElement(name = "sound")
    private List<String> launchSounds;

    @XmlElementWrapper(name = "hitShipSounds")
    @XmlElement(name = "sound")
    private List<String> hitShipSounds;

    @XmlElementWrapper(name = "hitShieldSounds")
    @XmlElement(name = "sound")
    private List<String> hitShieldSounds;

    @XmlElementWrapper(name = "missSounds")
    @XmlElement(name = "sound")
    private List<String> missSounds;

    @XmlElement(name = "weaponArt")
    private String weaponAnimId;

    @Override
    public String toString() {
        return "" + title;
    }
}
