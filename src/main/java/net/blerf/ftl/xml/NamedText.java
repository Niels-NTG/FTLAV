package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


/**
 * One of the "text" tags in lookup files.
 */
@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
public class NamedText {

    @XmlAttribute(name = "name")
    private String id;

    @XmlAttribute(name = "load")
    private String load;

    @XmlValue
    private String text;

}
