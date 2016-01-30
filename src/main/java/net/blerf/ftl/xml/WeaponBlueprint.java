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

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public int getShieldPiercing() {
		return shieldPiercing;
	}

	public void setShieldPiercing(int shieldPiercing) {
		this.shieldPiercing = shieldPiercing;
	}

	// TODO: bp?

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getShots() {
		return shots;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}

	public int getFireChance() {
		return fireChance;
	}

	public void setFireChance(int fireChance) {
		this.fireChance = fireChance;
	}

	public int getBreachChance() {
		return breachChance;
	}

	public void setBreachChance(int breachChance) {
		this.breachChance = breachChance;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getRarity() {
		return cost;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
