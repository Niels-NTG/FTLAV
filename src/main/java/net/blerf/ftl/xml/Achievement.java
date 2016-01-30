package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
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

	public int getMultiDifficulty() {
		return multiDifficulty;
	}

	public void setMultiDifficulty(int multiDifficulty) {
		this.multiDifficulty = multiDifficulty;
	}

	public boolean isVictory() {
		return victory;
	}

	public void setVictory(boolean b) {
		victory = b;
	}

	public boolean isQuest() {
		return quest;
	}

	public void setQuest(boolean b) {
		quest = b;
	}
}
