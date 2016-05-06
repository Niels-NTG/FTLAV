package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="blueprints")
@XmlAccessorType(XmlAccessType.FIELD)
public class Blueprints {

	private List<BlueprintList> blueprintList;
	private List<CrewBlueprint> crewBlueprint;
	private List<SystemBlueprint> systemBlueprint;
	private List<WeaponBlueprint> weaponBlueprint;
	private List<DroneBlueprint> droneBlueprint;
	private List<AugBlueprint> augBlueprint;
	private List<ShipBlueprint> shipBlueprint;
	public List<BlueprintList> getBlueprintList() {
		return blueprintList;
	}

	public List<CrewBlueprint> getCrewBlueprint() {
		return crewBlueprint;
	}

	public List<SystemBlueprint> getSystemBlueprint() {
		return systemBlueprint;
	}

	public List<WeaponBlueprint> getWeaponBlueprint() {
		return weaponBlueprint;
	}

	public List<DroneBlueprint> getDroneBlueprint() {
		return droneBlueprint;
	}

	public List<AugBlueprint> getAugBlueprint() {
		return augBlueprint;
	}

	public List<ShipBlueprint> getShipBlueprint() {
		return shipBlueprint;
	}
}
