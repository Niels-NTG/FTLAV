package net.blerf.ftl.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="shipChassis")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipChassis {
	@XmlElement(name="img")
	private ChassisImageBounds imageBounds;

	private Offsets offsets;  // FTL 1.5.4 introduced floor/cloak offsets.

	@XmlElement(name="weaponMounts")
	private WeaponMountList weaponMountList;

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ChassisImageBounds {
		@XmlAttribute
		public int x, y, w, h;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Offsets {

		@XmlElement(name="floor")
		public Offset floorOffset;

		@XmlElement(name="cloak")
		public Offset cloakOffset;

		@XmlAccessorType(XmlAccessType.FIELD)
		public static class Offset {
			@XmlAttribute
			public int x, y;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WeaponMountList {

		@XmlElement(name="mount")
		public List<WeaponMount> mount;

		@XmlAccessorType(XmlAccessType.FIELD)
		public static class WeaponMount {
			@XmlAttribute
			public int x, y, gib;
			@XmlAttribute
			public boolean rotate, mirror;
			@XmlAttribute
			public String slide;
		}
	}

	@XmlRootElement(name="explosion")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Explosion {
		private List<Gib> gib;

		@XmlRootElement(name="gib")
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class Gib {
			private FloatRange velocity;
			private FloatRange direction;
			private FloatRange angular;
			private int x, y;

			@XmlAccessorType(XmlAccessType.FIELD)
			public static class FloatRange {
				@XmlAttribute
				private float min, max;
			}
		}
	}

	public void setImageBounds( ChassisImageBounds imageBounds ) {
		this.imageBounds = imageBounds;
	}

	public ChassisImageBounds getImageBounds() {
		return imageBounds;
	}

	public void setOffsets( Offsets offsets ) {
		this.offsets = offsets;
	}

	public Offsets getOffsets() {
		return offsets;
	}

	public void setWeaponMountList( WeaponMountList weaponMountList ) {
		this.weaponMountList = weaponMountList;
	}

	public WeaponMountList getWeaponMountList() {
		return weaponMountList;
	}
}
