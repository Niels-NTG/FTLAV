package net.blerf.ftl.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blerf.ftl.constants.FTLConstants;
import net.blerf.ftl.parser.SavedGameParser.CrewType;


/**
 * Constants for FTL 1.01-1.03.3.
 */
public class OriginalFTLConstants implements FTLConstants {

	private final List<CrewType> crewTypes;


	public OriginalFTLConstants() {
		List<CrewType> mutableCrewTypes = new ArrayList<CrewType>();
		mutableCrewTypes.add( CrewType.BATTLE );
		mutableCrewTypes.add( CrewType.CRYSTAL );
		mutableCrewTypes.add( CrewType.ENERGY );
		mutableCrewTypes.add( CrewType.ENGI );
		mutableCrewTypes.add( CrewType.GHOST );
		mutableCrewTypes.add( CrewType.HUMAN );
		mutableCrewTypes.add( CrewType.MANTIS );
		mutableCrewTypes.add( CrewType.ROCK );
		mutableCrewTypes.add( CrewType.SLUG );
		crewTypes = Collections.unmodifiableList( mutableCrewTypes );
	}

	@Override
	public int getMaxReservePoolCapacity() { return 25; }


	@Override
	public int getBatteryPoolCapacity( int batterySystemCapacity ) {
		return 0;  // There was no battery in FTL 1.01-1.03.3. :P
	}


	@Override
	public int getMaxIonizedBars() { return 9; }


	@Override
	public List<CrewType> getCrewTypes() {
		return crewTypes;
	}


	@Override
	public int getMasteryIntervalPilot( String race ) { return 15; }

	@Override
	public int getMasteryIntervalEngine( String race ) { return 15; }

	@Override
	public int getMasteryIntervalShield( String race ) { return 55; }

	@Override
	public int getMasteryIntervalWeapon( String race ) { return 65; }

	@Override
	public int getMasteryIntervalRepair( String race ) { return 18; }

	@Override
	public int getMasteryIntervalCombat( String race ) { return 8; }
}
