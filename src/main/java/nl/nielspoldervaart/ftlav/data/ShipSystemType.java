package nl.nielspoldervaart.ftlav.data;

import java.net.URL;

public enum ShipSystemType {
	ARTILLERY("Artillery", "system-artillery"),
	BATTERY_BACKUP("Backup Battery", "system-backup"),
	CLOAK("Cloaking", "system-cloak"),
	CLONE("Clone Bay", "system-clone"),
	DOORS("Doors", "system-doors"),
	DRONES("Drone System", "system-drones"),
	ENGINE("Engine", "system-engine"),
	HACKING("Hacking", "system-hacking"),
	MED("Medical", "system-med"),
	MINDCONTROL("Mind Control", "system-mindcontrol"),
	OXYGEN("Oxygen", "system-oxygen"),
	PILOT("Pilot", "system-pilot"),
	SENSORS("Sensors", "system-sensors"),
	SHIELDS("Shields", "system-shields"),
	TELEPORT("Teleport", "system-teleport"),
	WEAPONS("Weapons", "system-weapons"),
	NONE("", "empty");

	public final String name;
	public final URL icon;

	ShipSystemType(String name, String iconFileName) {
		this.name = name;
		icon = ClassLoader.getSystemResource("visualiser/" + iconFileName + ".gif");
	}
}
