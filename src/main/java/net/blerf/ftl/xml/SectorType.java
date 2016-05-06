package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


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
