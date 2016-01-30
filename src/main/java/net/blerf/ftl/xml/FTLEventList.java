package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.blerf.ftl.xml.FTLEvent;


@XmlRootElement(name="eventList")
@XmlAccessorType(XmlAccessType.FIELD)
public class FTLEventList {
	@XmlAttribute(name="name")
	private String id;

	//@XmlElement(name="event",required=false)
	//private List<FTLEvent> eventList;

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
