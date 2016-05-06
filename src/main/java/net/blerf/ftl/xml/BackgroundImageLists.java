package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name="imageLists")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImageLists {
	@XmlElement(name="imageList")
	private List<BackgroundImageList> imageLists;

	public List<BackgroundImageList> getImageLists() {
		return imageLists;
	}

}
