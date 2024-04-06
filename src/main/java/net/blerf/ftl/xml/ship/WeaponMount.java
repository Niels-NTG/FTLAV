package net.blerf.ftl.xml.ship;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class WeaponMount {

    @XmlAttribute
    public int x;
    @XmlAttribute
    public int y;
    @XmlAttribute
    public int gib;

    @XmlAttribute
    public boolean rotate;
    @XmlAttribute
    public boolean mirror;

    @XmlAttribute
    public String slide;
}
