package net.blerf.ftl.constants;

import java.util.List;

import net.blerf.ftl.parser.SavedGameParser.CrewType;


public interface FTLConstants {

	// ShipState constants.

	public int getMaxReservePoolCapacity();


	// SystemState constants.

	/**
	 * Returns the bonus system bars produced by a Battery system.
	 *
	 * @param batterySystemCapacity the capacity of the system itself (its level)
	 */
	public int getBatteryPoolCapacity(int batterySystemCapacity);

	public int getMaxIonizedBars();


	// CrewState constants.

	public List<CrewType> getCrewTypes();

	public int getMasteryIntervalPilot(String race);
	public int getMasteryIntervalEngine(String race);
	public int getMasteryIntervalShield(String race);
	public int getMasteryIntervalWeapon(String race);
	public int getMasteryIntervalRepair(String race);
	public int getMasteryIntervalCombat(String race);
}
