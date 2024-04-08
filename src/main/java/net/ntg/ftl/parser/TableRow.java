package net.ntg.ftl.parser;

import lombok.Data;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.model.state.*;
import net.blerf.ftl.model.type.SystemType;
import processing.data.JSONArray;
import processing.data.StringList;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

@SuppressWarnings("unused")
@Data
public class TableRow {

	// Non-temporal
	private String time;
	private String shipName;
	private String shipType;
	private Difficulty difficulty;
	private boolean AEContentEnabled;

	// Location
	private int beaconNumber;
	private int sectorNumber;
	private String sectorType;
	private String sectorName;
	private int fleetAdvancement;

	// Encounter
	private JSONArray beaconHazards;
	private String encounterText;

	// Resources totals
	private int totalShipsDefeated;
	private int totalScrapCollected;
	private int totalCrewHired;
	private int totalScore;

	// Resources current
	private int scrap;
	private int fuel;
	private int missles;
	private int droneParts;
	private int crewSize;
	private int hull;
	private int oxygenLevel;
	private JSONArray shipAugments;
	private JSONArray cargo;
	private JSONArray crew;

	// Systems current
	// Pilot
	private int pilotSystemPowerCapacity;
	private int pilotSystemDamage;
	// Sensors
	private int sensorSystemPowerCapacity;
	private int sensorSystemDamage;
	// Doors
	private int doorSystemPowerCapacity;
	private int doorSystemDamage;
	private int doorCount;
	private int doorsOpen;
	private int doorsDamaged;
	// Battery
	private int batterySystemPowerCapacity;
	private int batterySystemDamage;
	private int batterySystemUse;
	// Medbay
	private int medbaySystemPowerCapacity;
	private int medbaySystemPowerConsumption;
	private int medbaySystemDamage;
	// Oxygen
	private int oxygenSystemPowerCapacity;
	private int oxygenSystemPowerConsumption;
	private int oxygenSystemDamage;
	// Shields
	private int shieldSystemPowerCapacity;
	private int shieldSystemPowerConsumption;
	private int shieldSystemDamage;
	private int shieldLayers;
	private int zoltanShieldLayers;
	// Engines
	private int engineSystemPowerCapacity;
	private int engineSystemPowerConsumption;
	private int engineSystemDamage;
	// Weapons
	private int weaponsSystemPowerCapacity;
	private int weaponsSystemPowerConsumption;
	private int weaponsSystemDamage;
	private JSONArray weaponsLayout;
	// Drone Control
	private int droneControlSystemPowerCapacity;
	private int droneControlSystemPowerConsumption;
	private int droneControlSystemDamage;
	private JSONArray droneControlLayout;
	// Teleporter
	private int teleporterSystemPowerCapacity;
	private int teleporterSystemPowerConsumption;
	private int teleporterSystemDamage;
	// Cloaking
	private int cloakingSystemPowerCapacity;
	private int cloakingSystemPowerConsumption;
	private int cloakingSystemDamage;
	private boolean cloaked;
	// Artillery
	private int artillerySystemPowerCapacity;
	private int artillerySystemPowerConsumption;
	private int artillerySystemDamage;
	// Clonebay
	private int cloneBaySystemPowerCapacity;
	private int cloneBaySystemPowerConsumption;
	private int cloneBaySystemDamage;
	// Mind Control
	private int mindControlSystemPowerCapacity;
	private int mindControlSystemPowerConsumption;
	private int mindControlSystemDamage;
	// Hacking System
	private int hackingSystemPowerCapacity;
	private int hackingSystemPowerConsumption;
	private int hackingSystemDamage;

	// Nearby Ship
	private boolean nearbyShipExists;
	private boolean nearbyShipHostile;
	// Nearby Ship Resources current
	private int nearbyShipCrewSize;
	private int nearbyShipHull;
	private int nearbyShipOxygenLevel;
	private JSONArray nearbyShipAugments;
	private JSONArray nearbyShipCrew;
	// Nearby Ship Systems Current
	private int nearbyShipPilotSystemPowerCapacity;
	private int nearbyShipPilotSystemDamage;
	// Sensors
	private int nearbyShipSensorSystemPowerCapacity;
	private int nearbyShipSensorSystemDamage;
	// Doors
	private int nearbyShipDoorSystemPowerCapacity;
	private int nearbyShipDoorSystemDamage;
	private int nearbyShipDoorCount;
	private int nearbyShipDoorsOpen;
	private int nearbyShipDoorsDamaged;
	// Battery
	private int nearbyShipBatterySystemPowerCapacity;
	private int nearbyShipBatterySystemDamage;
	private int nearbyShipBatterySystemUse;
	// Medbay
	private int nearbyShipMedbaySystemPowerCapacity;
	private int nearbyShipMedbaySystemPowerConsumption;
	private int nearbyShipMedbaySystemDamage;
	// Oxygen
	private int nearbyShipOxygenSystemPowerCapacity;
	private int nearbyShipOxygenSystemPowerConsumption;
	private int nearbyShipOxygenSystemDamage;
	// Shields
	private int nearbyShipShieldSystemPowerCapacity;
	private int nearbyShipShieldSystemPowerConsumption;
	private int nearbyShipShieldSystemDamage;
	private int nearbyShipShieldLayers;
	private int nearbyShipZoltanShieldLayers;
	// Engines
	private int nearbyShipEngineSystemPowerCapacity;
	private int nearbyShipEngineSystemPowerConsumption;
	private int nearbyShipEngineSystemDamage;
	// Weapons
	private int nearbyShipWeaponsSystemPowerCapacity;
	private int nearbyShipWeaponsSystemPowerConsumption;
	private int nearbyShipWeaponsSystemDamage;
	private JSONArray nearbyShipWeaponsLayout;
	// Drone Control
	private int nearbyShipDroneControlSystemPowerCapacity;
	private int nearbyShipDroneControlSystemPowerConsumption;
	private int nearbyShipDroneControlSystemDamage;
	private JSONArray nearbyShipDroneControlLayout;
	// Teleporter
	private int nearbyShipTeleporterSystemPowerCapacity;
	private int nearbyShipTeleporterSystemPowerConsumption;
	private int nearbyShipTeleporterSystemDamage;
	// Cloaking
	private int nearbyShipCloakingSystemPowerCapacity;
	private int nearbyShipCloakingSystemPowerConsumption;
	private int nearbyShipCloakingSystemDamage;
	private boolean nearbyShipCloaked;
	// Artillery
	private int nearbyShipArtillerySystemPowerCapacity;
	private int nearbyShipArtillerySystemPowerConsumption;
	private int nearbyShipArtillerySystemDamage;
	// Clonebay
	private int nearbyShipCloneBaySystemPowerCapacity;
	private int nearbyShipCloneBaySystemPowerConsumption;
	private int nearbyShipCloneBaySystemDamage;
	// Mind Control
	private int nearbyShipmindControlSystemPowerCapacity;
	private int nearbyShipMindControlSystemPowerConsumption;
	private int nearbyShipMindControlSystemDamage;
	// Hacking System
	private int nearbyShipHackingSystemPowerCapacity;
	private int nearbyShipHackingSystemPowerConsumption;
	private int nearbyShipHackingSystemDamage;

	public TableRow() {}
	public TableRow(SavedGameState gameState, String timeStamp) {

		ShipState playerShip = gameState.getPlayerShip();

		// Non-temporal
		time = timeStamp;
		shipName = gameState.getPlayerShipName();
		shipType = DataUtil.getFullShipType(gameState);
		difficulty = gameState.getDifficulty();
		AEContentEnabled = gameState.isDLCEnabled();

		// Location
		beaconNumber = gameState.getTotalBeaconsExplored();
		sectorNumber = gameState.getSectorNumber() + 1;
		sectorType = DataUtil.getSector(gameState).getType();
		sectorName = DataUtil.getSector(gameState).getTitle();
		fleetAdvancement = DataUtil.getRebelFleetAdvancement(gameState);

		// Encounter
		beaconHazards = DataUtil.getBeaconHazards(gameState);
		encounterText = gameState.getEncounter().getText();

		// Resource totals
		totalShipsDefeated = gameState.getTotalShipsDefeated();
		totalScrapCollected = gameState.getTotalScrapCollected();
		totalCrewHired = gameState.getTotalCrewHired();
		totalScore = gameState.calculateScore();

		// Resources current
		scrap = playerShip.getScrapAmt();
		fuel = playerShip.getFuelAmt();
		missles = playerShip.getMissilesAmt();
		droneParts = playerShip.getDronePartsAmt();
		// TODO check if this also includes enemy boarders
		crewSize = playerShip.getCrewList().size();
		hull = playerShip.getHullAmt();
		oxygenLevel = DataUtil.getShipOxygenLevel(playerShip);
		shipAugments = DataUtil.getShipAugments(playerShip);
		cargo = new JSONArray(new StringList(gameState.getCargoIdList()));
		crew = DataUtil.getCrewList(playerShip);

		// Systems current
		// Pilot
		SystemState pilotSystem = playerShip.getSystem(SystemType.PILOT);
		if (pilotSystem.getCapacity() != 0) {
			pilotSystemPowerCapacity = pilotSystem.getCapacity();
			pilotSystemDamage = pilotSystem.getDamagedBars();
		}
		// Sensors
		SystemState sensorSystem = playerShip.getSystem(SystemType.SENSORS);
		if (sensorSystem.getCapacity() != 0) {
			sensorSystemPowerCapacity = sensorSystem.getCapacity();
			sensorSystemDamage = sensorSystem.getDamagedBars();
		}
		// Doors
		SystemState doorSystem = playerShip.getSystem(SystemType.DOORS);
		if (doorSystem.getCapacity() != 0) {
			doorSystemPowerCapacity = doorSystem.getCapacity();
			doorSystemDamage = doorSystem.getDamagedBars();
			for (DoorState doorState : playerShip.getDoorMap().values()) {
				doorCount++;
				if (doorState.isOpen()) {
					doorsOpen++;
				}
				if (doorState.getHealth() < doorState.getNominalHealth()) {
					doorsDamaged++;
				}
			}
		}
		// Battery
		SystemState batterySystem = playerShip.getSystem(SystemType.BATTERY);
		if (batterySystem.getCapacity() != 0) {
			batterySystemPowerCapacity = batterySystem.getCapacity();
			batterySystemDamage = batterySystem.getDamagedBars();
			batterySystemUse = DataUtil.getBatterySystemUse(playerShip);
		}
		// Medbay
		SystemState medbaySystem = playerShip.getSystem(SystemType.MEDBAY);
		if (medbaySystem.getCapacity() != 0) {
			medbaySystemPowerCapacity = medbaySystem.getCapacity();
			medbaySystemPowerConsumption = medbaySystem.getPower();
			medbaySystemDamage = medbaySystem.getDamagedBars();
		}
		// Oxygen
		SystemState oxygenSystem = playerShip.getSystem(SystemType.OXYGEN);
		if (oxygenSystem.getCapacity() != 0) {
			oxygenSystemPowerCapacity = oxygenSystem.getCapacity();
			oxygenSystemPowerConsumption = oxygenSystem.getPower();
			oxygenSystemDamage = oxygenSystem.getDamagedBars();
		}
		// Shields
		SystemState shieldSystem = playerShip.getSystem(SystemType.SHIELDS);
		if (shieldSystem.getCapacity() != 0) {
			shieldSystemPowerCapacity = shieldSystem.getCapacity();
			shieldSystemPowerConsumption = shieldSystem.getPower();
			shieldSystemDamage = shieldSystem.getDamagedBars();
			shieldLayers = DataUtil.getShieldLayers(playerShip);
			zoltanShieldLayers = DataUtil.getZoltanShieldLayers(playerShip);
		}
		// Engines
		SystemState engineSystem = playerShip.getSystem(SystemType.ENGINES);
		if (engineSystem.getCapacity() != 0) {
			engineSystemPowerCapacity = engineSystem.getCapacity();
			engineSystemPowerConsumption = engineSystem.getPower();
			engineSystemDamage = engineSystem.getDamagedBars();
		}
		// Weapons
		SystemState weaponsSystem = playerShip.getSystem(SystemType.WEAPONS);
		if (weaponsSystem.getCapacity() != 0) {
			weaponsSystemPowerCapacity = weaponsSystem.getCapacity();
			weaponsSystemPowerConsumption = weaponsSystem.getPower();
			weaponsSystemDamage = weaponsSystem.getDamagedBars();
			weaponsLayout = DataUtil.getWeaponLayout(playerShip);
		}
		// Drone Control
		SystemState droneControlSystem = playerShip.getSystem(SystemType.DRONE_CTRL);
		if (droneControlSystem.getCapacity() != 0) {
			droneControlSystemPowerCapacity = droneControlSystem.getCapacity();
			droneControlSystemPowerConsumption = droneControlSystem.getPower();
			droneControlSystemDamage = droneControlSystem.getDamagedBars();
			droneControlLayout = DataUtil.getDroneLayout(playerShip);
		}
		// Teleporter
		SystemState teleporterSystem = playerShip.getSystem(SystemType.TELEPORTER);
		if (teleporterSystem.getCapacity() != 0) {
			teleporterSystemPowerCapacity = teleporterSystem.getCapacity();
			teleporterSystemPowerConsumption = teleporterSystem.getPower();
			teleporterSystemDamage = teleporterSystem.getDamagedBars();
		}
		// Cloaking
		SystemState cloakingSystem = playerShip.getSystem(SystemType.CLOAKING);
		if (cloakingSystem.getCapacity() != 0) {
			cloakingSystemPowerCapacity = cloakingSystem.getCapacity();
			cloakingSystemPowerConsumption = cloakingSystem.getPower();
			cloakingSystemDamage = cloakingSystem.getDamagedBars();
			cloaked = playerShip.getCloakAnimTicks() > 0;
		}
		// Artillery
		SystemState artillerySystem = playerShip.getSystem(SystemType.ARTILLERY);
		if (artillerySystem.getCapacity() != 0) {
			artillerySystemPowerCapacity = artillerySystem.getCapacity();
			artillerySystemPowerConsumption = artillerySystem.getPower();
			artillerySystemDamage = artillerySystem.getDamagedBars();
		}
		// Clonebay
		SystemState clonebaySystem = playerShip.getSystem(SystemType.CLONEBAY);
		if (clonebaySystem.getCapacity() != 0) {
			cloneBaySystemPowerCapacity = clonebaySystem.getCapacity();
			cloneBaySystemPowerConsumption = clonebaySystem.getPower();
			cloneBaySystemDamage = clonebaySystem.getDamagedBars();
		}
		// Mind Control
		SystemState mindControlSystem = playerShip.getSystem(SystemType.MIND);
		if (mindControlSystem.getCapacity() != 0) {
			mindControlSystemPowerCapacity = mindControlSystem.getCapacity();
			mindControlSystemPowerConsumption = mindControlSystem.getPower();
			mindControlSystemDamage = mindControlSystem.getDamagedBars();
		}
		// Hacking System
		SystemState hackingSystem = playerShip.getSystem(SystemType.HACKING);
		if (hackingSystem.getCapacity() != 0) {
			hackingSystemPowerCapacity = hackingSystem.getCapacity();
			hackingSystemPowerConsumption = hackingSystem.getPower();
			hackingSystemDamage = hackingSystem.getDamagedBars();
		}

		// Nearby Ship
		ShipState nearbyShip = gameState.getNearbyShip();
		nearbyShipExists = nearbyShip != null;
		if (nearbyShipExists) {
			nearbyShipHostile = nearbyShip.isHostile();

			nearbyShipCrewSize = nearbyShip.getCrewList().size();
			nearbyShipHull = nearbyShip.getHullAmt();
			nearbyShipOxygenLevel = DataUtil.getShipOxygenLevel(nearbyShip);
			nearbyShipAugments = DataUtil.getShipAugments(nearbyShip);
			nearbyShipCrew = DataUtil.getCrewList(nearbyShip);

			SystemState nearbyShipPilotSystem = nearbyShip.getSystem(SystemType.PILOT);
			if (nearbyShipPilotSystem.getCapacity() != 0) {
				nearbyShipPilotSystemPowerCapacity = nearbyShipPilotSystem.getCapacity();
				nearbyShipPilotSystemDamage = nearbyShipPilotSystem.getDamagedBars();
			}
			// Sensors
			SystemState nearbyShipSensorSystem = nearbyShip.getSystem(SystemType.SENSORS);
			if (nearbyShipSensorSystem.getCapacity() != 0) {
				nearbyShipSensorSystemPowerCapacity = nearbyShipSensorSystem.getCapacity();
				nearbyShipSensorSystemDamage = nearbyShipSensorSystem.getDamagedBars();
			}
			// Doors
			SystemState nearbyShipDoorSystem = nearbyShip.getSystem(SystemType.DOORS);
			if (nearbyShipDoorSystem.getCapacity() != 0) {
				nearbyShipDoorSystemPowerCapacity = nearbyShipDoorSystem.getCapacity();
				nearbyShipDoorSystemDamage = nearbyShipDoorSystem.getDamagedBars();
				for (DoorState doorState : nearbyShip.getDoorMap().values()) {
					nearbyShipDoorCount++;
					if (doorState.isOpen()) {
						nearbyShipDoorsOpen++;
					}
					if (doorState.getHealth() < doorState.getNominalHealth()) {
						nearbyShipDoorsDamaged++;
					}
				}
			}
			// Battery
			SystemState nearbyShipBatterySystem = nearbyShip.getSystem(SystemType.BATTERY);
			if (nearbyShipBatterySystem.getCapacity() != 0) {
				nearbyShipBatterySystemPowerCapacity = nearbyShipBatterySystem.getCapacity();
				nearbyShipBatterySystemDamage = nearbyShipBatterySystem.getDamagedBars();
				nearbyShipBatterySystemUse = DataUtil.getBatterySystemUse(nearbyShip);
			}
			// Medbay
			SystemState nearbyShipMedbaySystem = nearbyShip.getSystem(SystemType.MEDBAY);
			if (nearbyShipMedbaySystem.getCapacity() != 0) {
				nearbyShipMedbaySystemPowerCapacity = nearbyShipMedbaySystem.getCapacity();
				nearbyShipMedbaySystemPowerConsumption = nearbyShipMedbaySystem.getPower();
				nearbyShipMedbaySystemDamage = nearbyShipMedbaySystem.getDamagedBars();
			}
			// Oxygen
			SystemState nearbyShipOxygenSystem = nearbyShip.getSystem(SystemType.OXYGEN);
			if (nearbyShipOxygenSystem.getCapacity() != 0) {
				nearbyShipOxygenSystemPowerCapacity = nearbyShipOxygenSystem.getCapacity();
				nearbyShipOxygenSystemPowerConsumption = nearbyShipOxygenSystem.getPower();
				nearbyShipOxygenSystemDamage = nearbyShipOxygenSystem.getDamagedBars();
			}
			// Shields
			SystemState nearbyShipShieldSystem = nearbyShip.getSystem(SystemType.SHIELDS);
			if (nearbyShipShieldSystem.getCapacity() != 0) {
				nearbyShipShieldSystemPowerCapacity = nearbyShipShieldSystem.getCapacity();
				nearbyShipShieldSystemPowerConsumption = shieldSystem.getPower();
				nearbyShipShieldSystemDamage = shieldSystem.getDamagedBars();
				nearbyShipShieldLayers = DataUtil.getShieldLayers(nearbyShip);
				nearbyShipZoltanShieldLayers = DataUtil.getZoltanShieldLayers(nearbyShip);
			}
			// Engines
			SystemState nearbyShipEngineSystem = nearbyShip.getSystem(SystemType.ENGINES);
			if (nearbyShipEngineSystem.getCapacity() != 0) {
				nearbyShipEngineSystemPowerCapacity = nearbyShipEngineSystem.getCapacity();
				nearbyShipEngineSystemPowerConsumption = nearbyShipEngineSystem.getPower();
				nearbyShipEngineSystemDamage = nearbyShipEngineSystem.getDamagedBars();
			}
			// Weapons
			SystemState nearbyShipWeaponsSystem = nearbyShip.getSystem(SystemType.WEAPONS);
			if (nearbyShipWeaponsSystem.getCapacity() != 0) {
				nearbyShipWeaponsSystemPowerCapacity = nearbyShipWeaponsSystem.getCapacity();
				nearbyShipWeaponsSystemPowerConsumption = nearbyShipWeaponsSystem.getPower();
				nearbyShipWeaponsSystemDamage = nearbyShipWeaponsSystem.getDamagedBars();
				nearbyShipWeaponsLayout = DataUtil.getWeaponLayout(nearbyShip);
			}
			// Drone Control
			SystemState nearbyShipDroneControlSystem = nearbyShip.getSystem(SystemType.DRONE_CTRL);
			if (nearbyShipDroneControlSystem.getCapacity() != 0) {
				nearbyShipDroneControlSystemPowerCapacity = nearbyShipDroneControlSystem.getCapacity();
				nearbyShipDroneControlSystemPowerConsumption = nearbyShipDroneControlSystem.getPower();
				nearbyShipDroneControlSystemDamage = nearbyShipDroneControlSystem.getDamagedBars();
				nearbyShipDroneControlLayout = DataUtil.getDroneLayout(nearbyShip);
			}
			// Teleporter
			SystemState nearbyShipTeleporterSystem = nearbyShip.getSystem(SystemType.TELEPORTER);
			if (nearbyShipTeleporterSystem.getCapacity() != 0) {
				nearbyShipTeleporterSystemPowerCapacity = nearbyShipTeleporterSystem.getCapacity();
				nearbyShipTeleporterSystemPowerConsumption = nearbyShipTeleporterSystem.getPower();
				nearbyShipTeleporterSystemDamage = nearbyShipTeleporterSystem.getDamagedBars();
			}
			// Cloaking
			SystemState nearbyShipCloakingSystem = nearbyShip.getSystem(SystemType.CLOAKING);
			if (nearbyShipCloakingSystem.getCapacity() != 0) {
				nearbyShipCloakingSystemPowerCapacity = nearbyShipCloakingSystem.getCapacity();
				nearbyShipCloakingSystemPowerConsumption = nearbyShipCloakingSystem.getPower();
				nearbyShipCloakingSystemDamage = nearbyShipCloakingSystem.getDamagedBars();
				nearbyShipCloaked = nearbyShip.getCloakAnimTicks() > 0;
			}
			// Artillery
			SystemState nearbyShipArtillerySystem = nearbyShip.getSystem(SystemType.ARTILLERY);
			if (nearbyShipArtillerySystem.getCapacity() != 0) {
				nearbyShipArtillerySystemPowerCapacity = nearbyShipArtillerySystem.getCapacity();
				nearbyShipArtillerySystemPowerConsumption = nearbyShipArtillerySystem.getPower();
				nearbyShipArtillerySystemDamage = nearbyShipArtillerySystem.getDamagedBars();
			}
			// Clonebay
			SystemState nearbyShipClonebaySystem = nearbyShip.getSystem(SystemType.CLONEBAY);
			if (nearbyShipClonebaySystem.getCapacity() != 0) {
				nearbyShipCloneBaySystemPowerCapacity = nearbyShipClonebaySystem.getCapacity();
				nearbyShipCloneBaySystemPowerConsumption = nearbyShipClonebaySystem.getPower();
				nearbyShipCloneBaySystemDamage = nearbyShipClonebaySystem.getDamagedBars();
			}
			// Mind Control
			SystemState nearbyShipMindControlSystem = nearbyShip.getSystem(SystemType.MIND);
			if (nearbyShipMindControlSystem.getCapacity() != 0) {
				nearbyShipmindControlSystemPowerCapacity = nearbyShipMindControlSystem.getCapacity();
				nearbyShipMindControlSystemPowerConsumption = nearbyShipMindControlSystem.getPower();
				nearbyShipMindControlSystemDamage = nearbyShipMindControlSystem.getDamagedBars();
			}
			// Hacking System
			SystemState nearbyShipHackingSystem = nearbyShip.getSystem(SystemType.HACKING);
			if (nearbyShipHackingSystem.getCapacity() != 0) {
				nearbyShipHackingSystemPowerCapacity = nearbyShipHackingSystem.getCapacity();
				nearbyShipHackingSystemPowerConsumption = nearbyShipHackingSystem.getPower();
				nearbyShipHackingSystemDamage = nearbyShipHackingSystem.getDamagedBars();
			}
		}
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = Difficulty.valueOf(difficulty);
	}
	public String[] getFieldNames() {
		return getFieldNames(null);
	}
	public String[] getFieldNames(Type type) {
		ArrayList<String> privateFields = new ArrayList<>();
		Field[] fields = this.getClass().getDeclaredFields();
		boolean hasNoTypeCheck = type == null;
		for (Field field : fields) {
			if (hasNoTypeCheck || field.getGenericType() == type) {
				privateFields.add(field.getName());
			}
		}
		return privateFields.toArray(new String[privateFields.size()]);
	}

	public Object getFieldValue(String fieldName) {
		try {
			return this.getClass().getDeclaredField(fieldName).get(this);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			return null;
		}
	}

}
