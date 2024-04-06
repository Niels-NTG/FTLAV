package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "animations")
@XmlAccessorType(XmlAccessType.FIELD)
public class Animations {

    @XmlElement(name = "animSheet")
    private final List<AnimSheet> sheets = new ArrayList<AnimSheet>();

    @XmlElement(name = "anim")
    private final List<Anim> anims = new ArrayList<Anim>();

    @XmlElement(name = "weaponAnim")
    private final List<WeaponAnim> weaponAnims = new ArrayList<WeaponAnim>();

    /**
     * Returns an AnimSheet with a given id.
     * <p>
     * AnimSheets have a separate namespace from Anims and WeaponAnims.
     */
    public AnimSheet getSheetById(String id) {
        if (id == null || sheets == null) return null;

        AnimSheet result = null;
        for (AnimSheet tmpSheet : sheets) {
            if (id.equals(tmpSheet.getId())) result = tmpSheet;
        }

        return result;
    }

    /**
     * Returns all Anims that appear in a given AnimSheet.
     */
    public List<Anim> getAnimsBySheetId(String id) {
        if (id == null || anims == null) return null;

        List<Anim> results = new ArrayList<Anim>();
        for (Anim tmpAnim : anims) {
            if (id.equals(tmpAnim.getSheetId())) results.add(tmpAnim);
        }

        return results;
    }

    /**
     * Returns an Anim with a given id.
     * <p>
     * AnimSheets have a separate namespace from Anims and WeaponAnims.
     */
    public Anim getAnimById(String id) {
        if (id == null || anims == null) return null;

        Anim result = null;
        for (Anim tmpAnim : anims) {
            if (id.equals(tmpAnim.getId())) result = tmpAnim;
        }

        return result;
    }

    /**
     * Returns a WeaponAnim with a given id.
     * <p>
     * AnimSheets have a separate namespace from Anims and WeaponAnims.
     */
    public WeaponAnim getWeaponAnimById(String id) {
        if (id == null || weaponAnims == null) return null;

        WeaponAnim result = null;
        for (WeaponAnim tmpWeaponAnim : weaponAnims) {
            if (id.equals(tmpWeaponAnim.getId())) result = tmpWeaponAnim;
        }

        return result;
    }
}
