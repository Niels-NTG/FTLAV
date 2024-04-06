package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "animSheet")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimSheet {

    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute(name = "w")
    private int width;

    @XmlAttribute(name = "h")
    private int height;

    @XmlAttribute(name = "fw")
    private int frameWidth;

    @XmlAttribute(name = "fh")
    private int frameHeight;

    @XmlValue
    private String innerPath;  // Relative to "img/" (the top-level dir is omitted).

    @Override
    public String toString() {
        return "" + id;
    }
}
