package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="imageList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImageList {
	@XmlAttribute(name="name")
	private String id;

	@XmlElement(name="img")
	private List<BackgroundImage> images;

	public List<BackgroundImage> getImages() {
		return images;
	}

	public void setImages(List<BackgroundImage> images) {
		this.images = images;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ""+id;
	}
}
