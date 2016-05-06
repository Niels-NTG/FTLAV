package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="achievement")
@XmlAccessorType(XmlAccessType.FIELD)
public class Achievement {

	@XmlAttribute
	private String id;

	private String name;

	@XmlElement(name="desc")
	private String description;

	@XmlElement(name="img")
	private String imagePath;

	@XmlElement(name="ship",required=false)
	private String shipId;

	@XmlElement(required=false)
	private int multiDifficulty;

	// Ship Victory achievements track *all* the variants which earned them.
	// Ship Victory achievements don't unlock ship variants.
	@XmlTransient
	private boolean victory = false;

	// Ship Quest achievements don't unlock ship variants.
	@XmlTransient
	private boolean quest = false;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getShipId() {
		return shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

	public void setVictory(boolean b) {
		victory = b;
	}

	public void setQuest(boolean b) {
		quest = b;
	}
}
