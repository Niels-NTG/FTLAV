package net.blerf.ftl.xml;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class FTLEvent {
	@XmlAttribute(name="name",required=false)
	private String id;

	// The rest is uninteresting. ;)

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
