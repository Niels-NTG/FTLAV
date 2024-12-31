package nl.nielspoldervaart.ftlav.data;

import net.blerf.ftl.constants.FleetPresence;
import net.blerf.ftl.constants.HazardVulnerability;
import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.model.state.BeaconState;
import net.blerf.ftl.model.state.CrewState;
import net.blerf.ftl.model.state.DroneState;
import net.blerf.ftl.model.state.EnvironmentState;
import net.blerf.ftl.model.state.RoomState;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.model.state.ShipState;
import net.blerf.ftl.model.state.WeaponState;
import net.blerf.ftl.model.systeminfo.BatteryInfo;
import net.blerf.ftl.model.systeminfo.ShieldsInfo;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.AugBlueprint;
import net.blerf.ftl.xml.DroneBlueprint;
import net.blerf.ftl.xml.WeaponBlueprint;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class DataUtil {

	public static ArrayList<Integer> extractIntColumn(String columnName) {
		ArrayList<Integer> column = new ArrayList<>();
		for (TableRow row : FTLAdventureVisualiser.recording) {
			column.add((int)row.getFieldValue(columnName));
		}
		return column;
	}

	public static ArrayList<String> extractStringColumn(String columnName) {
		ArrayList<String> column = new ArrayList<>();
		for (TableRow row : FTLAdventureVisualiser.recording) {
			column.add((String)row.getFieldValue(columnName));
		}
		return column;
	}

	public static String getColumnDisplayName(String columnName) {
		try {
			return TableRow.getTableWriterHeader(TableRow.class.getDeclaredField(columnName));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static TableRow getLastRecord() {
		return FTLAdventureVisualiser.recording.get(FTLAdventureVisualiser.recording.size() - 1);
	}

	public static String getPlayerShipName() {
		return getLastRecord().getShipName();
	}

	public static String getTimeStampLastRecord() {
		return getLastRecord().getTime().replaceAll("[/:]", ".");
	}

	public static SectorDot getSector(SavedGameState gameState) {
		return FTLAdventureVisualiser.sectorList.get(gameState.getSectorNumber());
	}

	public static int getShipOxygenLevel(ShipState shipState) {
		List<RoomState> roomList = shipState.getRoomList();
		int roomCount = roomList.size();
		int sum = 0;
		for (RoomState roomState : roomList) {
			sum += roomState.getOxygen();
		}
		return sum / roomCount;
	}

	public static String getFullShipType(SavedGameState gameState) {
		switch (gameState.getPlayerShipBlueprintId()) {
			case "PLAYER_SHIP_HARD"   : return "Kestrel Cruiser A";
			case "PLAYER_SHIP_HARD_2" : return "Kestrel Cruiser B";
			case "PLAYER_SHIP_HARD_3" : return "Kestrel Cruiser C";

			case "PLAYER_SHIP_CIRCLE"   : return "Engi Cruiser A";
			case "PLAYER_SHIP_CIRCLE_2" : return "Engi Cruiser B";
			case "PLAYER_SHIP_CIRCLE_3" : return "Engi Cruiser C";

			case "PLAYER_SHIP_FED"   : return "Federation Cruiser A";
			case "PLAYER_SHIP_FED_2" : return "Federation Cruiser B";
			case "PLAYER_SHIP_FED_3" : return "Federation Cruiser C";

			case "PLAYER_SHIP_ENERGY"   : return "Zoltan Cruiser A";
			case "PLAYER_SHIP_ENERGY_2" : return "Zoltan Cruiser B";
			case "PLAYER_SHIP_ENERGY_3" : return "Zoltan Cruiser C";

			case "PLAYER_SHIP_STEALTH"   : return "Stealth Cruiser A";
			case "PLAYER_SHIP_STEALTH_2" : return "Stealth Cruiser B";
			case "PLAYER_SHIP_STEALTH_3" : return "Stealth Cruiser C";

			case "PLAYER_SHIP_ROCK"   : return "Rock Cruiser A";
			case "PLAYER_SHIP_ROCK_2" : return "Rock Cruiser B";
			case "PLAYER_SHIP_ROCK_3" : return "Rock Cruiser C";

			case "PLAYER_SHIP_MANTIS"   : return "Mantis Cruiser A";
			case "PLAYER_SHIP_MANTIS_2" : return "Mantis Cruiser B";
			case "PLAYER_SHIP_MANTIS_3" : return "Mantis Cruiser C";

			case "PLAYER_SHIP_JELLY"   : return "Slug Cruiser A";
			case "PLAYER_SHIP_JELLY_2" : return "Slug Cruiser B";
			case "PLAYER_SHIP_JELLY_3" : return "Slug Cruiser C";

			case "PLAYER_SHIP_ANAEROBIC"   : return "Lanius Cruiser A";
			case "PLAYER_SHIP_ANAEROBIC_2" : return "Lanius Cruiser B";
			case "PLAYER_SHIP_ANAEROBIC_3" : return "Lanius Cruiser C";

			default :
				return gameState.getPlayerShipBlueprintId().replaceAll("_"," ");
		}
	}

	public static JSONArray getShipAugments(ShipState shipState) {
		JSONArray augments = new JSONArray();
		for (String id : shipState.getAugmentIdList()) {
			AugBlueprint blueprint = DataManager.get().getAugment(id);
			JSONObject augmentObject = new JSONObject();
			augmentObject.setString("augmentId", blueprint.getId());
			augmentObject.setString("name", blueprint.getTitle().toString());
			augmentObject.setInt("cost", blueprint.getCost());
			augmentObject.setInt("rarity", blueprint.getRarity());
			augmentObject.setFloat("value", blueprint.getValue());
			augmentObject.setBoolean("isStackable", blueprint.isStackable());
			augments.append(augmentObject);
		}
		return augments;
	}

	public static int getShieldLayers(ShipState shipState) {
		ShieldsInfo info = shipState.getExtendedSystemInfo(ShieldsInfo.class);
		if (info != null) {
			return info.getShieldLayers();
		}
		return 0;
	}

	public static int getZoltanShieldLayers(ShipState shipState) {
		ShieldsInfo info = shipState.getExtendedSystemInfo(ShieldsInfo.class);
		if (info != null) {
			return info.getEnergyShieldLayers();
		}
		return 0;
	}

	public static JSONArray getWeaponLayout(ShipState shipState) {
		List<WeaponState> weaponStates = shipState.getWeaponList();
		JSONArray weaponList = new JSONArray();
		for (WeaponState weaponState : weaponStates) {
			JSONObject weaponObject = new JSONObject();
			weaponObject.setString("weaponId", weaponState.getWeaponId());
			weaponObject.setBoolean("isArmed", weaponState.isArmed());
			weaponObject.setJSONObject("blueprint", getWeaponBluePrint(weaponState));
			weaponList.append(weaponObject);
		}
		return weaponList;
	}
	private static JSONObject getWeaponBluePrint(WeaponState weaponState) {
		WeaponBlueprint blueprint = DataManager.get().getWeapon(weaponState.getWeaponId());
		JSONObject weaponObject = new JSONObject();
		weaponObject.setString("type", blueprint.getType());
		weaponObject.setString("fullName", blueprint.getTitle().toString());
		weaponObject.setInt("shieldPiercing", blueprint.getShieldPiercing());
		weaponObject.setInt("damage", blueprint.getDamage());
		weaponObject.setInt("shots", blueprint.getShots());
		weaponObject.setInt("fireChance", blueprint.getFireChance());
		weaponObject.setInt("breachChance", blueprint.getBreachChance());
		weaponObject.setInt("cooldown", blueprint.getCooldown());
		weaponObject.setInt("cost", blueprint.getCost());
		weaponObject.setInt("power", blueprint.getPower());
		weaponObject.setInt("rarity", blueprint.getRarity());
		return weaponObject;
	}

	public static JSONArray getDroneLayout(ShipState shipState) {
		List<DroneState> droneStates = shipState.getDroneList();
		JSONArray droneList = new JSONArray();
		for (DroneState droneState : droneStates) {
			JSONObject droneObject = new JSONObject();
			droneObject.setString("droneId", droneState.getDroneId());
			droneObject.setBoolean("isArmed", droneState.isArmed());
			droneObject.setInt("health", droneState.getHealth());
			droneObject.setBoolean("isDeployed", droneState.getExtendedDroneInfo().isDeployed());
			droneObject.setJSONObject("blueprint", getDroneBluePrint(droneState));
			droneList.append(droneObject);
		}
		return droneList;
	}
	private static JSONObject getDroneBluePrint(DroneState droneState) {
		DroneBlueprint blueprint = DataManager.get().getDrone(droneState.getDroneId());
		JSONObject droneObject = new JSONObject();
		droneObject.setString("type", blueprint.getType());
		droneObject.setString("fullName", blueprint.getTitle().toString());
		droneObject.setInt("cooldown", blueprint.getCooldown());
		droneObject.setInt("dodge", blueprint.getDodge());
		droneObject.setInt("speed", blueprint.getSpeed());
		droneObject.setInt("power", blueprint.getPower());
		droneObject.setInt("cost", blueprint.getCost());
		droneObject.setInt("rarity", blueprint.getRarity());
		return droneObject;
	}

	public static int getBatterySystemUse(ShipState shipState) {
		BatteryInfo info = shipState.getExtendedSystemInfo(BatteryInfo.class);
		if (info != null) {
			return info.getUsedBattery();
		}
		return 0;
	}

	public static int getWeaponSlotCount(SavedGameState gameState) {
		return DataManager.get().getShip(gameState.getPlayerShip().getShipBlueprintId()).getWeaponSlots();
	}

	public static int getDroneSlotCount(SavedGameState gameState) {
		return DataManager.get().getShip(gameState.getPlayerShip().getShipBlueprintId()).getDroneSlots();
	}

	public static int getPlayerCrewCount(ShipState shipState) {
		int playerControlledCrewCount = 0;
		for (CrewState crewState : shipState.getCrewList()) {
			if (crewState.isPlayerControlled()) {
				playerControlledCrewCount++;
			}
		}
		return playerControlledCrewCount;
	}

	public static JSONArray getCrewList(ShipState shipState) {
		JSONArray crewList = new JSONArray();
		for (CrewState crewState : shipState.getCrewList()) {
			JSONObject crewObject = new JSONObject();

			crewObject.setString("name", crewState.getName());
			crewObject.setString("race", getFullCrewType(crewState.getRace().toString()));
			crewObject.setInt("health", crewState.getHealth());
			crewObject.setInt("jumpsSurvived", crewState.getJumpsSurvived());
			crewObject.setInt("pilotedEvasions", crewState.getPilotedEvasions());
			crewObject.setInt("repairs", crewState.getRepairs());
			crewObject.setInt("combatKills", crewState.getCombatKills());
			crewObject.setInt("pilotSkill", crewState.getPilotSkill());
			crewObject.setInt("engineSkill", crewState.getEngineSkill());
			crewObject.setInt("shieldSkill", crewState.getShieldSkill());
			crewObject.setInt("weaponsSkill", crewState.getWeaponSkill());
			crewObject.setInt("repairSkill", crewState.getRepairSkill());
			crewObject.setInt("combatSkill", crewState.getCombatSkill());
			crewObject.setInt("skillMasteriesEarned", crewState.getSkillMasteriesEarned());
			crewObject.setBoolean("isEnemyBoardingDrone", crewState.isEnemyBoardingDrone());
			crewObject.setBoolean("isMindControlled", crewState.isMindControlled());
			crewObject.setBoolean("isPlayerControlled", crewState.isPlayerControlled());
			// TODO current room of crew member
//			DataManager.get().getShipLayout().getRoom(crewState.getRoomId());
			// TODO saved room of crew member
//			crewState.getSavedRoomId();

			crewList.append(crewObject);
		}
		return crewList;
	}

	private static String getFullCrewType(String rawCrewType) {
		if (rawCrewType.isEmpty()) {
			return "";
		}
		switch (rawCrewType) {
			case "battle"    : return "Anti-Personel Drone";
			case "energy"    : return "Zoltan";
			case "anaerobic" : return "Lanius";
			default:
				return rawCrewType.substring(0, 1).toUpperCase() + rawCrewType.substring(1);
		}
	}

	public static int getRebelFleetAdvancement(SavedGameState gameState) {
		int rebelOffset = Math.abs(gameState.getRebelFleetOffset());
		int rebelFudge = gameState.getRebelFleetFudge();
		int result = (int)(100 - (rebelOffset - rebelFudge) * 100f / 650);
		return Math.max(0, result);
	}

	public static JSONArray getBeaconHazards(SavedGameState gameState) {
		JSONArray hazards = new JSONArray();

		EnvironmentState environment = gameState.getEnvironment();
		if (environment.isRedGiantPresent()) {
			hazards.append("Red Giant");
		}
		if (environment.isPulsarPresent()) {
			hazards.append("Pulsar");
		}
		if (environment.isPDSPresent()) {
			if (environment.getVulnerableShips() == HazardVulnerability.BOTH_SHIPS) {
				hazards.append("Neutral Planetary Defense System");
			} else if (environment.getVulnerableShips() == HazardVulnerability.PLAYER_SHIP) {
				hazards.append("Hostile Planetary Defense System");
			} else if (environment.getVulnerableShips() == HazardVulnerability.NEARBY_SHIP) {
				hazards.append("Allied Planetary Defense System");
			}
		}
		if (environment.getAsteroidField() != null) {
			hazards.append("Asteroid Field");
		}

		BeaconState beacon = gameState.getBeaconList().get(gameState.getCurrentBeaconId());
		if (beacon.isEnemyPresent()) {
			hazards.append("Enemy Ship");
		}
		if (beacon.getFleetPresence() == FleetPresence.REBEL) {
			hazards.append("Rebel Fleet");
		}
		if (beacon.getFleetPresence() == FleetPresence.FEDERATION) {
			hazards.append("Federation Fleet");
		}

		// if (FTLAdventureVisualiser.gameState.getStateVar("nebula") != null) // TODO Nebula Storm event

		return hazards;

	}

	public static int getMaxValue(HashMap<String, Boolean> columns) {
		ArrayList<String> keys = new ArrayList<>();
		for (String key : columns.keySet()) {
			if (columns.get(key)) {
				keys.add(key);
			}
		}
		return getMaxValue(keys);
	}
	public static int getMaxValue(ArrayList<String> columns) {
		ArrayList<Integer> values = new ArrayList<>();
		for (String key : columns) {
			values.addAll(DataUtil.extractIntColumn(key));
		}
		if (values.isEmpty()) {
			return 0;
		}
		return Collections.max(values);
	}

}
