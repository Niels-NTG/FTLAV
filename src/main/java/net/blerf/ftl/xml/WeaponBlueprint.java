package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="weaponBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeaponBlueprint {

	@XmlAttribute(name="name")
	private String id;
	private String type, title;
	@XmlElement(name="short")
	private String shortTitle;
	private String desc, tooltip;
	@XmlElement(name="sp")
	private int shieldPiercing;
	@XmlElement(name="bp")
	private int bp;  // TODO: Rename this.
	private int damage, shots, fireChance, breachChance, cooldown, power, cost, rarity;
	private String image;
	private SoundList launchSounds, hitShipSounds, hitShieldSounds, missSounds;
	private String weaponArt;

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SoundList {
		private List<String> sound;
	}

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

	public String getTooltip() {
		return tooltip;
	}

	public int getShieldPiercing() {
		return shieldPiercing;
	}

	// TODO: bp?

	public int getDamage() {
		return damage;
	}

	public int getShots() {
		return shots;
	}

	public int getFireChance() {
		return fireChance;
	}

	public int getBreachChance() {
		return breachChance;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getPower() {
		return power;
	}

	public int getCost() {
		return cost;
	}

	public int getRarity() {
		return cost;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
