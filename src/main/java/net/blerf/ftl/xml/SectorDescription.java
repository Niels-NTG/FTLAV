package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


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


	public String getId() {
		return id;
	}

	public int getMinSector() {
		return minSector;
	}

	public boolean isUnique() {
		return unique;
	}

	public NameList getNameList() {
		return nameList;
	}

	public TrackList getTrackList() {
		return trackList;
	}

	public String getStartEvent() {
		return startEvent;
	}

	public List<EventDistribution> getEventDistributions() {
		return eventDistributions;
	}
}
