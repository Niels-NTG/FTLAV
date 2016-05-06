package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="droneBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class DroneBlueprint {
	@XmlAttribute(name="name")
	private String id;
	private String type, title;
	@XmlElement(name="short")
	private String shortTitle;
	private String desc;
	@XmlElement(name="bp")
	private int bp;  // TODO: Rename this.
	@XmlElement(required=false)
	private int cooldown, dodge, speed;
	private int power, cost;
	@XmlElement(required=false)
	private String droneImage, image;
	@XmlElement(name="weaponBlueprint",required=false)
	private String weaponId;
	private int rarity;

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public String getDescription() {
		return desc;
	}

	// TODO: bp?

	public int getPower() {
		return power;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getDodge() {
		return dodge;
	}

	public int getSpeed() {
		return speed;
	}

	public int getCost() {
		return cost;
	}

	public String getDroneImage() {
		return droneImage;
	}

	public String getImage() {
		return image;
	}

	public String getWeaponId() {
		return weaponId;
	}

	public int getRarity() {
		return cost;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
