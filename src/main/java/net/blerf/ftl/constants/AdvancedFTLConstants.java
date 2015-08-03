package net.blerf.ftl.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.blerf.ftl.constants.FTLConstants;
import net.blerf.ftl.parser.SavedGameParser.CrewType;


/**
 * Constants for FTL 1.5.4.
 *
 * TODO: Determine the "ghost" race's skill mastery intervals in AE.
 */
public class AdvancedFTLConstants implements FTLConstants {

	private final List<CrewType> crewTypes;


	public AdvancedFTLConstants() {
		List<CrewType> mutableCrewTypes = new ArrayList<CrewType>();
		mutableCrewTypes.add( CrewType.ANAEROBIC );
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
		return batterySystemCapacity * 2;
	}


	@Override
	public int getMaxIonizedBars() { return 9; }


	@Override
	public List<CrewType> getCrewTypes() {
		return crewTypes;
	}


	@Override
	public int getMasteryIntervalPilot( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 13;
		return 15;
	}

	@Override
	public int getMasteryIntervalEngine( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 13;
		return 15;
	}

	@Override
	public int getMasteryIntervalShield( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 50;
		return 55;
	}

	@Override
	public int getMasteryIntervalWeapon( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 58;
		return 65;
	}

	@Override
	public int getMasteryIntervalRepair( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 16;
		return 18;
	}

	@Override
	public int getMasteryIntervalCombat( String race ) {
		if ( CrewType.HUMAN.getId().equals(race) ) return 7;
		return 8;
	}
}
