package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="augBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class AugBlueprint {

	@XmlAttribute(name="name")
	private String id;
	private String title;
	private String desc;
	@XmlElement(name="bp")
	private int bp;  // TODO: Rename this.
	private int cost, rarity;
	private boolean stackable;
	private float value;

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return desc;
	}

	// TODO: bp?

	public int getCost() {
		return cost;
	}

	public int getRarity() {
		return cost;
	}

	public boolean isStackable() {
		return stackable;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float n) {
		value = n;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
