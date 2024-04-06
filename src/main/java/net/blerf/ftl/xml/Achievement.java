package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "achievement")
@XmlAccessorType(XmlAccessType.FIELD)
public class Achievement {

    @XmlAttribute
    private String id;

    private DefaultDeferredText name;

    @XmlElement()
    private DefaultDeferredText shortName;

    @XmlElement(name = "desc")
    private DefaultDeferredText description;

    @XmlElement(name = "img")
    private String imagePath;

    @XmlElement(name = "ship")
    private String shipId;

    @XmlElement()
    private int multiDifficulty;

    // Ship Victory achievements track *all* the variants which earned them.
    // Ship Victory achievements don't unlock ship variants.
    @XmlTransient
    private boolean victory = false;

    // Ship Quest achievements don't unlock ship variants.
    @XmlTransient
    private boolean quest = false;

}
