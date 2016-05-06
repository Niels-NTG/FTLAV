package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name="crewBlueprint")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewBlueprint {
	@XmlAttribute(name="name")
	private String id;
	private String desc;
	private int cost;
	@XmlElement(name="bp")
	private int bp;  // TODO: Rename this.
	private String title;
	@XmlElement(name="short")
	private String shortTitle;
	private int rarity;
	private PowerList powerList;

	@XmlElementWrapper(name = "colorList")
	@XmlElement(name="layer")
	private List<SpriteTintLayer> spriteTintLayerList;  // FTL 1.5.4 introduced sprite tinting.

	@XmlRootElement(name="powerList")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class PowerList {
		private List<String> power;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class SpriteTintLayer {

		@XmlElement(name="color")
		public List<SpriteTintColor> tintList;

		@XmlAccessorType(XmlAccessType.FIELD)
		public static class SpriteTintColor {
			@XmlAttribute
			public int r, g, b;
			@XmlAttribute
			public float a;
		}
	}

	public String getId() {
		return id;
	}

	public List<SpriteTintLayer> getSpriteTintLayerList() {
		return spriteTintLayerList;
	}
}
