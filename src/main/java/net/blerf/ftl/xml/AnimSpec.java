package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * A description of an Anim's location within an AnimSheet.
 * <p>
 * An anim appears in an AnimSheet starting at a certain row/column. Columns
 * are 0-based, left-to-right. Rows are 0-based, bottom-to-top.
 * <p>
 * TODO: Determine whether Anim frames can wrap around the sheet onto the next
 * row.
 *
 * @see net.blerf.ftl.xml.Anim
 * @see net.blerf.ftl.xml.WeaponAnim
 */
@XmlRootElement(name = "desc")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimSpec {

    @XmlAttribute(name = "length")
    public int frameCount;

    @XmlAttribute(name = "x")
    public int row;

    @XmlAttribute(name = "y")
    public int column;


    @Override
    public String toString() {
        return String.format("frames:%s, row:%d, col:%d", frameCount, row, column);
    }
}
