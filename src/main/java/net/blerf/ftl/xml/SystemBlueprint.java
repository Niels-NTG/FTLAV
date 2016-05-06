package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


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

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return desc;
	}

	public int getStartPower() {
		return startPower;
	}

	public int getMaxPower() {
		return maxPower;
	}

	public int getRarity() {
		return rarity;
	}

	public UpgradeCost getUpgradeCost() {
		return upgradeCost;
	}

	public int getCost() {
		return cost;
	}

	public int getLocked() {
		return locked;
	}

}
