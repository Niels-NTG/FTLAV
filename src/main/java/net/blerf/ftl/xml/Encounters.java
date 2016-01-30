package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.blerf.ftl.xml.FTLEvent;
import net.blerf.ftl.xml.FTLEventList;


@XmlRootElement(name="events")
@XmlAccessorType(XmlAccessType.FIELD)
public class Encounters {
	@XmlElement(name="event",required=false)
	private List<FTLEvent> events;

	@XmlElement(name="eventList",required=false)
	private List<FTLEventList> eventLists;

	@XmlElement(name="ship")
	private List<ShipEvent> shipEvents;

	public void setEvents(List<FTLEvent> events) {
		this.events = events;
	}

	public List<FTLEvent> getEvents() {
		return events;
	}

	public void setEventLists(List<FTLEventList> eventLists) {
		this.eventLists = eventLists;
	}

	public List<FTLEventList> getEventLists() {
		return eventLists;
	}

	public void setShipEvents(List<ShipEvent> shipEvents) {
		this.shipEvents = shipEvents;
	}

	public List<ShipEvent> getShipEvents() {
		return shipEvents;
	}

	/**
	 * Returns an Event with a given id.
	 *
	 * Events and EventLists share a namespace,
	 * so an id could belong to either.
	 */
	public FTLEvent getEventById(String id) {
		if (id == null || events == null) return null;

		FTLEvent result = null;
		for (FTLEvent tmpEvent : events) {
			if (id.equals(tmpEvent.getId())) result = tmpEvent;
		}

		return result;
	}

	/**
	 * Returns an EventList with a given id.
	 *
	 * Events and EventLists share a namespace,
	 * so an id could belong to either.
	 */
	public FTLEventList getEventListById(String id) {
		if (id == null || eventLists == null) return null;

		FTLEventList result = null;
		for (FTLEventList tmpEventList : eventLists) {
			if (id.equals(tmpEventList.getId())) result = tmpEventList;
		}

		return result;
	}

	/**
	 * Returns a ShipEvent with a given id.
	 *
	 * Events and EventLists share a namespace,
	 * so an id could belong to either.
	 */
	public ShipEvent getShipEventById(String id) {
		if (id == null || shipEvents == null) return null;

		ShipEvent result = null;
		for (ShipEvent tmpEvent : shipEvents) {
			if (id.equals(tmpEvent.getId())) result = tmpEvent;
		}

		return result;
	}
}
