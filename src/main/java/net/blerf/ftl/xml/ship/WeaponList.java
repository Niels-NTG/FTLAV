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
public class WeaponList {

    // 'count' isn't an independent field; a getter/setter calc's it (See below).

    @XmlAttribute
    public int missiles;

    @XmlAttribute(name = "load")
    public String blueprintListId;

    @XmlElement(name = "weapon")
    private List<WeaponId> weaponIds;


    public void setCount(int n) { /* No-op */ }

    @XmlAttribute(name = "count")
    public int getCount() {
        return (weaponIds != null ? weaponIds.size() : 0);
    }

}
