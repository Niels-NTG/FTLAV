package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.blerf.ftl.xml.BackgroundImageList;


@XmlRootElement(name="imageLists")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImageLists {
	@XmlElement(name="imageList")
	private List<BackgroundImageList> imageLists;

	public List<BackgroundImageList> getImageLists() {
		return imageLists;
	}

	public void setImageLists( List<BackgroundImageList> imageLists ) {
		this.imageLists = imageLists;
	}
}
