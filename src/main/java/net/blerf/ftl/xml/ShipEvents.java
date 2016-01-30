package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="shipEvents")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipEvents {

	@XmlElement(name="ship")
	private List<ShipEvent> shipEvents;

	public void setShipEvents(List<ShipEvent> shipEvents) {
		this.shipEvents = shipEvents;
	}

	public List<ShipEvent> getShipEvents() {
		return shipEvents;
	}
}
