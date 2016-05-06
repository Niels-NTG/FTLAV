package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="nameLists")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewNameLists {

	@XmlElement(name="nameList")
	private List<CrewNameList> crewNameLists;

	public List<CrewNameList> getCrewNameLists() {
		return crewNameLists;
	}
}
