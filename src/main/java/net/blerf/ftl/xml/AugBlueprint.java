package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


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

	public void setId( String id ) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription( String desc ) {
		this.desc = desc;
	}

	// TODO: bp?

	public int getCost() {
		return cost;
	}

	public void setCost( int cost ) {
		this.cost = cost;
	}

	public int getRarity() {
		return cost;
	}

	public void setRarity( int rarity ) {
		this.rarity = rarity;
	}

	public boolean isStackable() {
		return stackable;
	}

	public void setStackable( boolean b ) {
		stackable = b;
	}

	public float getValue() {
		return value;
	}

	public void setValue( float n ) {
		value = n;
	}

	@Override
	public String toString() {
		return ""+title;
	}
}
