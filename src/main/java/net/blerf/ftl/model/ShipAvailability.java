package net.blerf.ftl.model;


public class ShipAvailability {

	private String shipId;
	private boolean unlockedA;
	private boolean unlockedC;


	public ShipAvailability( String shipId, boolean unlockedA, boolean unlockedC ) {
		this.shipId = shipId;
		this.unlockedA = unlockedA;
		this.unlockedC = unlockedC;
	}

	public ShipAvailability( String shipId ) {
		this( shipId, false, false );
	}

	/**
	 * Copy constructor.
	 */
	public ShipAvailability( ShipAvailability srcAvail ) {
		this( srcAvail.getShipId(), srcAvail.isUnlockedA(), srcAvail.isUnlockedC() );
	}

	public String getShipId() { return shipId; }

	/**
	 * Sets whether the Type-A variant is unlocked.
	 */
	public void setUnlockedA( boolean b ) { unlockedA = b; }
	public boolean isUnlockedA() { return unlockedA; }

	/**
	 * Sets whether the Type-C variant is unlocked.
	 *
	 * This was introduced in FTL 1.5.4.
	 */
	public void setUnlockedC( boolean b ) { unlockedC = b; }
	public boolean isUnlockedC() { return unlockedC; }

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(String.format("ShipId: %-25s  Type-A: %-5b  Type-C: %-5b\n", shipId, unlockedA, unlockedC));

		return result.toString();
	}
}
