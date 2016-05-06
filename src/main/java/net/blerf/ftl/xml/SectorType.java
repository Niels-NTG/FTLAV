package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="sectorType")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorType {

	@XmlAttribute(name="name")
	private String id;

	@XmlElement(name="sector")
	public List<String> sectorIds;

	public String getId() {
		return id;
	}

	public List<String> getSectorIds() {
		return sectorIds;
	}
}
