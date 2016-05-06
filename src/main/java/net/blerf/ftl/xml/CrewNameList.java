package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name="nameList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewNameList {
	@XmlAttribute
	private String race, sex;  // FTL ignores race.

	@XmlElement(name="name")
	private List<CrewName> names;

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CrewName {
		@XmlAttribute(required=false)
		public String shortName;
		@XmlValue
		public String name;
	}

	public String getRace() {
		return race;
	}

	public String getSex() {
		return sex;
	}

	public List<CrewName> getNames() {
		return names;
	}
}
