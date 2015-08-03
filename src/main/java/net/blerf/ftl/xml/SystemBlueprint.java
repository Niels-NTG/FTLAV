package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="systemBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemBlueprint {
	@XmlAttribute(name="name")
	private String id;
	private String type, title, desc;
	private int startPower;  // Initial system capacity.
	private int maxPower;    // Highest possible capacity attainable by upgrading.
	private int rarity;
	private UpgradeCost upgradeCost;
	private int cost;
	@XmlElement(required=false)
	private int locked;
	
	@XmlRootElement(name="upgradeCost")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class UpgradeCost {
		private List<Integer> level;

		public List<Integer> getLevel() {
			return level;
		}

		public void setLevel(List<Integer> level) {
			this.level = level;
		}
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

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.type = desc;
	}

	public int getStartPower() {
		return startPower;
	}

	public void setStartPower(int startPower) {
		this.startPower = startPower;
	}

	public int getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public UpgradeCost getUpgradeCost() {
		return upgradeCost;
	}

	public void setUpgradeCost(UpgradeCost upgradeCost) {
		this.upgradeCost = upgradeCost;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {
		this.locked = locked;
	}
}
