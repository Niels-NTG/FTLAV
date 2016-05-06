package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


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

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return ""+id;
	}
}
