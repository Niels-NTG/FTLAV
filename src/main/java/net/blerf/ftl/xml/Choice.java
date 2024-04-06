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
@XmlRootElement(name = "choice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Choice {
    @XmlAttribute(name = "hidden")
    private boolean hidden;

    @XmlAttribute(name = "req")
    private String req;

    @XmlAttribute(name = "lvl")
    private String level;

    @XmlElement(name = "text")
    private NamedText text;

    @XmlElement(name = "event")
    private FTLEvent event;

    private StringBuilder indent(StringBuilder sb, int level) {
        sb.append(new String(new char[level]).replaceAll("\0", "    "));
        return sb;
    }

    public String toDescription(int level) {
        StringBuilder sb = new StringBuilder();
        if (text != null)
            indent(sb, level).append("text: ").append(text.getText()).append("\n");
        if (event != null)
            indent(sb, level).append("event: \n").append(event.toDescription(level + 1));

        return sb.toString();
    }
}
