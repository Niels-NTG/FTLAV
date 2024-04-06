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
@XmlRootElement(name = "img")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImage {

    @XmlAttribute(name = "w")
    private int width;

    @XmlAttribute(name = "h")
    private int height;

    @XmlValue
    private String innerPath;

    @Override
    public String toString() {
        return String.format("%s (%dx%d)", innerPath, width, height);
    }
}
