package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="achievements")
@XmlAccessorType(XmlAccessType.FIELD)
public class Achievements {

	@XmlElement(name="achievement")
	private List<Achievement> achievements;

	public List<Achievement> getAchievements() {
		return achievements;
	}

}
