package net.blerf.ftl.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Container for "text" tags in string lookup files.
 * <p>
 * FTL 1.5.4 introduced "misc.xml".
 * FTL 1.6.1 introduced "text_*.xml" (and removed "misc.xml").
 */
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "namedTexts")
@XmlAccessorType(XmlAccessType.FIELD)
public class NamedTexts {

    @XmlElement(name = "text")
    private List<NamedText> namedTexts;

}
