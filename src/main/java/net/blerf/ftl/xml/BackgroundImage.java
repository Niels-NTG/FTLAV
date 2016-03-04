package net.blerf.ftl.xml;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name="img")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImage {

	@XmlAttribute(name="w")
	private int width;

	@XmlAttribute(name="h")
	private int height;

	@XmlValue
	private String innerPath;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getInnerPath() {
		return innerPath;
	}

	public void setInnerPath(String innerPath) {
		this.innerPath = innerPath;
	}

	@Override
	public String toString() {
		return String.format("%s (%dx%d)", innerPath, width, height);
	}
}
