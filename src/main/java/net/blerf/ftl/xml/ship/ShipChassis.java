package net.blerf.ftl.xml.ship;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.xml.Offset;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "shipChassis")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipChassis {

    @XmlElement(name = "img")
    private ChassisImageBounds imageBounds;

    @XmlElement()
    private Offsets offsets;  // FTL 1.5.4 introduced floor/cloak offsets.

    @XmlElementWrapper(name = "weaponMounts")
    @XmlElement(name = "mount")
    private List<WeaponMount> weaponMountList;

    private Explosion explosion;


    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ChassisImageBounds {
        @XmlAttribute
        public int x, y, w, h;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Offsets {

        @XmlElement(name = "floor")
        public Offset floorOffset;

        @XmlElement(name = "cloak")
        public Offset cloakOffset;
    }

    @XmlJavaTypeAdapter(ExplosionAdapter.class)
    public static class Explosion {
        public List<Gib> gibs = new ArrayList<Gib>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Gib {
        public FloatRange velocity;
        public FloatRange direction;
        public FloatRange angular;
        public int x;
        public int y;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FloatRange {
        @XmlAttribute
        public float min;
        @XmlAttribute
        public float max;
    }

}
