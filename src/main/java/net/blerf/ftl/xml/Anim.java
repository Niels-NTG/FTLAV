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
@XmlRootElement(name = "anim")
@XmlAccessorType(XmlAccessType.FIELD)
public class Anim {

    @XmlAttribute(name = "name")
    private String id;

    @XmlElement(name = "sheet")
    private String sheetId;

    @XmlElement(name = "desc")
    private AnimSpec animSpec;

    private float time;

    @Override
    public String toString() {
        return "" + id;
    }
}
