package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="sectorData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorData {
	@XmlElement(name="sectorType")
	private List<SectorType> sectorTypes;

	@XmlElement(name="sectorDescription")
	private List<SectorDescription> sectorDescriptions;

	public List<SectorType> getSectorTypes() {
		return sectorTypes;
	}

	public List<SectorDescription> getSectorDescriptions() {
		return sectorDescriptions;
	}

	/**
	 * Returns a SectorType with a given id.
	 */
	public SectorType getSectorTypeById(String id) {
		if (id == null || sectorTypes == null) return null;

		for (SectorType tmpType : sectorTypes) {
			if (id.equals(tmpType.getId())) return tmpType;
		}

		return null;
	}

	/**
	 * Returns a SectorDescription with a given id.
	 */
	public SectorDescription getSectorDescriptionById(String id) {
		if (id == null || sectorDescriptions == null) return null;

		SectorDescription result = null;
		for (SectorDescription tmpDesc : sectorDescriptions) {
			if (id.equals(tmpDesc.getId())) result = tmpDesc;
		}

		return result;
	}
}
