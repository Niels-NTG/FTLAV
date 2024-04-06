package net.blerf.ftl.xml;

import java.util.List;
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
@XmlRootElement(name = "blueprintList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlueprintList {

    @XmlAttribute
    private String name;

    @XmlElement(name = "name")
    private List<String> items;

}
