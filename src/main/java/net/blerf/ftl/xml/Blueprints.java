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
	public void setBlueprintList(List<BlueprintList> blueprintList) {
		this.blueprintList = blueprintList;
	}
	public List<CrewBlueprint> getCrewBlueprint() {
		return crewBlueprint;
	}
	public void setCrewBlueprint(List<CrewBlueprint> crewBlueprint) {
		this.crewBlueprint = crewBlueprint;
	}
	public List<SystemBlueprint> getSystemBlueprint() {
		return systemBlueprint;
	}
	public void setSystemBlueprint(List<SystemBlueprint> systemBlueprint) {
		this.systemBlueprint = systemBlueprint;
	}
	public List<WeaponBlueprint> getWeaponBlueprint() {
		return weaponBlueprint;
	}
	public void setWeaponBlueprint(List<WeaponBlueprint> weaponBlueprint) {
		this.weaponBlueprint = weaponBlueprint;
	}
	public List<DroneBlueprint> getDroneBlueprint() {
		return droneBlueprint;
	}
	public void setDroneBlueprint(List<DroneBlueprint> droneBlueprint) {
		this.droneBlueprint = droneBlueprint;
	}
	public List<AugBlueprint> getAugBlueprint() {
		return augBlueprint;
	}
	public void setAugBlueprint(List<AugBlueprint> augBlueprint) {
		this.augBlueprint = augBlueprint;
	}
	public List<ShipBlueprint> getShipBlueprint() {
		return shipBlueprint;
	}
	public void setShipBlueprint(List<ShipBlueprint> shipBlueprint) {
		this.shipBlueprint = shipBlueprint;
	}
}
