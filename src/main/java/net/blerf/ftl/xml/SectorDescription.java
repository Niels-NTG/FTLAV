package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="sectorDescription")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorDescription {

	@XmlAttribute(name="name")
	private String id;

	@XmlAttribute
	private int minSector;

	@XmlAttribute
	private boolean unique;

	private NameList nameList;

	private TrackList trackList;

	@XmlElement(required=false)
	private String startEvent;

	@XmlElement(name="event")
	private List<EventDistribution> eventDistributions;

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class NameList {
		@XmlElement(name="name")
		public List<String> names;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TrackList {
		@XmlElement(name="track")
		public List<String> tracks;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class EventDistribution {
		@XmlAttribute
		public String name;

		@XmlAttribute
		public int min;

		@XmlAttribute
		public int max;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setMinSector(int minSector) {
		this.minSector = minSector;
	}

	public int getMinSector() {
		return minSector;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setNameList(NameList nameList) {
		this.nameList = nameList;
	}

	public NameList getNameList() {
		return nameList;
	}

	public void setTrackList(TrackList trackList) {
		this.trackList = trackList;
	}

	public TrackList getTrackList() {
		return trackList;
	}

	public void setStartEvent(String startEvent) {
		this.startEvent = startEvent;
	}

	public String getStartEvent() {
		return startEvent;
	}

	public void setEventDistributions(List<EventDistribution> eventDistributions) {
		this.eventDistributions = eventDistributions;
	}

	public List<EventDistribution> getEventDistributions() {
		return eventDistributions;
	}
}
