package net.blerf.ftl.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.blerf.ftl.model.type.CrewType;
import net.blerf.ftl.model.type.StationDirection;
import net.blerf.ftl.model.type.SystemType;


/**
 * Constants for FTL 1.5.4-1.5.13.
 * <p>
 * TODO: Determine the "ghost" race's skill mastery intervals in AE.
 */
public class AdvancedFTLConstants implements FTLConstants {

    private final List<CrewType> crewTypes;
    private final List<SystemType> systemTypes;


    public AdvancedFTLConstants() {
        List<CrewType> mutableCrewTypes = new ArrayList<CrewType>();
        mutableCrewTypes.add(CrewType.ANAEROBIC);
        mutableCrewTypes.add(CrewType.BATTLE);
        mutableCrewTypes.add(CrewType.CRYSTAL);
        mutableCrewTypes.add(CrewType.ENERGY);
        mutableCrewTypes.add(CrewType.ENGI);
        mutableCrewTypes.add(CrewType.GHOST);
        mutableCrewTypes.add(CrewType.HUMAN);
        mutableCrewTypes.add(CrewType.MANTIS);
        mutableCrewTypes.add(CrewType.ROCK);
        mutableCrewTypes.add(CrewType.SLUG);
        crewTypes = Collections.unmodifiableList(mutableCrewTypes);

        List<SystemType> mutableSystemTypes = new ArrayList<SystemType>();
        mutableSystemTypes.add(SystemType.PILOT);
        mutableSystemTypes.add(SystemType.DOORS);
        mutableSystemTypes.add(SystemType.SENSORS);
        mutableSystemTypes.add(SystemType.MEDBAY);
        mutableSystemTypes.add(SystemType.OXYGEN);
        mutableSystemTypes.add(SystemType.SHIELDS);
        mutableSystemTypes.add(SystemType.ENGINES);
        mutableSystemTypes.add(SystemType.WEAPONS);
        mutableSystemTypes.add(SystemType.DRONE_CTRL);
        mutableSystemTypes.add(SystemType.TELEPORTER);
        mutableSystemTypes.add(SystemType.CLOAKING);
        mutableSystemTypes.add(SystemType.ARTILLERY);
        mutableSystemTypes.add(SystemType.BATTERY);
        mutableSystemTypes.add(SystemType.CLONEBAY);
        mutableSystemTypes.add(SystemType.MIND);
        mutableSystemTypes.add(SystemType.HACKING);
        systemTypes = Collections.unmodifiableList(mutableSystemTypes);
    }


    @Override
    public int getMaxReservePoolCapacity() {
        return 25;
    }


    @Override
    public int getBatteryPoolCapacity(int batterySystemCapacity) {
        return batterySystemCapacity * 2;
    }


    @Override
    public int getMaxIonizedBars() {
        return 9;
    }


    @Override
    public List<CrewType> getCrewTypes() {
        return crewTypes;
    }


    @Override
    public int getMasteryIntervalPilot(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 13;
        return 15;
    }

    @Override
    public int getMasteryIntervalEngine(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 13;
        return 15;
    }

    @Override
    public int getMasteryIntervalShield(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 50;
        return 55;
    }

    @Override
    public int getMasteryIntervalWeapon(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 58;
        return 65;
    }

    @Override
    public int getMasteryIntervalRepair(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 16;
        return 18;
    }

    @Override
    public int getMasteryIntervalCombat(CrewType race) {
        if (CrewType.HUMAN.equals(race)) return 7;
        return 8;
    }


    /**
     * Returns a list of all valid systems.
     * <p>
     * FTL 1.5.4 introduced BATTERY, CLONEBAY, MIND, and HACKING.
     */
    @Override
    public List<SystemType> getSystemTypes() {
        return systemTypes;
    }


    /**
     * Returns the terminal/blocked square when slot was not defined, or -2.
     * <p>
     * FTL has hard-coded defaults for each system, to use when ShipBlueprint
     * doesn't override them. This only applies to player ships. For enemy
     * ships, each system's slot square appears to be on any random wall that's
     * not a door.
     * <p>
     * FTL 1.5.4 introduced mannable SENSORS and DOORS.
     */
    @Override
    public int getDefaultSystemRoomSlotSquare(SystemType systemType) {

        if (SystemType.PILOT.equals(systemType)) return 0;
        else if (SystemType.DOORS.equals(systemType)) return 0;
        else if (SystemType.SENSORS.equals(systemType)) return 1;
        else if (SystemType.MEDBAY.equals(systemType)) return 1;
        else if (SystemType.OXYGEN.equals(systemType)) return -2;
        else if (SystemType.SHIELDS.equals(systemType)) return 0;
        else if (SystemType.ENGINES.equals(systemType)) return 2;
        else if (SystemType.WEAPONS.equals(systemType)) return 1;
        else if (SystemType.DRONE_CTRL.equals(systemType)) return -2;
        else if (SystemType.TELEPORTER.equals(systemType)) return -2;
        else if (SystemType.CLOAKING.equals(systemType)) return -2;
        else if (SystemType.ARTILLERY.equals(systemType)) return -2;
        else if (SystemType.BATTERY.equals(systemType)) return -2;
        else if (SystemType.CLONEBAY.equals(systemType)) return 1;
        else if (SystemType.MIND.equals(systemType)) return -2;
        else if (SystemType.HACKING.equals(systemType)) return -2;
        else {
            throw new IllegalArgumentException("Unexpected SystemType: " + systemType);
        }
    }

    /**
     * Returns the terminal direction when slot was not defined, or null.
     * <p>
     * FTL has hard-coded defaults for each system, to use when ShipBlueprint
     * doesn't override them. This only applies to player ships. For enemy
     * ships, each system's slot square appears to be on any random wall that's
     * not a door.
     * <p>
     * FTL 1.5.4 introduced mannable SENSORS and DOORS.
     */
    @Override
    public StationDirection getDefaultSystemRoomSlotDirection(SystemType systemType) {

        if (SystemType.PILOT.equals(systemType)) return StationDirection.RIGHT;
        else if (SystemType.DOORS.equals(systemType)) return StationDirection.UP;
        else if (SystemType.SENSORS.equals(systemType)) return StationDirection.UP;
        else if (SystemType.MEDBAY.equals(systemType)) return StationDirection.NONE;
        else if (SystemType.OXYGEN.equals(systemType)) return null;
        else if (SystemType.SHIELDS.equals(systemType)) return StationDirection.LEFT;
        else if (SystemType.ENGINES.equals(systemType)) return StationDirection.DOWN;
        else if (SystemType.WEAPONS.equals(systemType)) return StationDirection.UP;
        else if (SystemType.DRONE_CTRL.equals(systemType)) return null;
        else if (SystemType.TELEPORTER.equals(systemType)) return null;
        else if (SystemType.CLOAKING.equals(systemType)) return null;
        else if (SystemType.ARTILLERY.equals(systemType)) return null;
        else if (SystemType.BATTERY.equals(systemType)) return null;
        else if (SystemType.CLONEBAY.equals(systemType)) return StationDirection.NONE;
        else if (SystemType.MIND.equals(systemType)) return null;
        else if (SystemType.HACKING.equals(systemType)) return null;
        else {
            throw new IllegalArgumentException("Unexpected SystemType: " + systemType);
        }
    }
}
