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

	public void setId( String id ) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle( String shortTitle ) {
		this.shortTitle = shortTitle;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription( String desc ) {
		this.desc = desc;
	}

	// TODO: bp?

	public int getPower() {
		return power;
	}

	public void setPower( int power ) {
		this.power = power;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown( int cooldown ) {
		this.cooldown = cooldown;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge( int dodge ) {
		this.dodge = dodge;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed( int speed ) {
		this.speed = speed;
	}

	public int getCost() {
		return cost;
	}

	public void setCost( int cost ) {
		this.cost = cost;
	}

	public String getDroneImage() {
		return droneImage;
	}

	public void setDroneImage( String droneImage ) {
		this.droneImage = droneImage;
	}

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

	public String getWeaponId() {
		return weaponId;
	}

	public void setWeaponId( String weaponId ) {
		this.weaponId = weaponId;
	}

	public int getRarity() {
		return cost;
	}

	public void setRarity( int rarity ) {
		this.rarity = rarity;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
