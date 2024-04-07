package net.ntg.ftl.parser;

import net.blerf.ftl.constants.FleetPresence;
import net.blerf.ftl.constants.HazardVulnerability;
import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.model.state.BeaconState;
import net.blerf.ftl.model.state.EnvironmentState;
import net.blerf.ftl.model.state.RoomState;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.model.state.ShipState;
import net.blerf.ftl.model.state.StoreState;
import net.blerf.ftl.model.state.WeaponState;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.WeaponBlueprint;
import net.ntg.ftl.FTLAdventureVisualiser;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
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

	public static LinkedHashSet<String> getTableHeaders() {
		return getTableHeaders(null);
	}
	public static LinkedHashSet<String> getTableHeaders(Type type) {
		LinkedHashSet<String> headers = new LinkedHashSet<>();
		for (TableRow row : FTLAdventureVisualiser.recording) {
			headers.addAll(Arrays.asList(row.getFieldNames(type)));
		}
		return headers;
	}

	public static TableRow getLastRecord() {
		return FTLAdventureVisualiser.recording.get(FTLAdventureVisualiser.recording.size() - 1);
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

	 public static int getNearbyShipOxygenLevel(SavedGameState gameState) {
		 ShipState nearbyShip = gameState.getNearbyShip();
		 if (nearbyShip != null) {
			 return getShipOxygenLevel(nearbyShip);
	 	}
		return -1;
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
	public static int getWeaponSlotCount(SavedGameState gameState) {
		return DataManager.get().getShip(gameState.getPlayerShip().getShipBlueprintId()).getWeaponSlots();
	}

	public static int getDroneSlotCount(SavedGameState gameState) {
		return DataManager.get().getShip(gameState.getPlayerShip().getShipBlueprintId()).getDroneSlots();
	}

	public static String getCargoListing(SavedGameState gameState) {
		List<String> cargoIdList = gameState.getCargoIdList();
		String cargo = String.join(", ", cargoIdList);
		return cargo.replaceAll("_"," ");
	}

	public static String getAugmentListing(SavedGameState gameState) {
		List<String> augmentIdList = gameState.getPlayerShip().getAugmentIdList();
		String aug = String.join(", ", augmentIdList);
		return aug.replaceAll("_"," ");
	}

	 public static String getNearbyShipAugmentListing(SavedGameState gameState) {
	 	List<String> augmentIdList = gameState.getNearbyShip().getAugmentIdList();
	 	String aug = String.join(", ", augmentIdList);
	 	return aug.replaceAll("_"," ");
	 }

	public static String getStoreListing(SavedGameState gameState) {
		String storeItems = "";
		BeaconState beacon = gameState.getBeaconList().get(gameState.getCurrentBeaconId());
		StoreState store = beacon.getStore();
		if (store != null) {
			storeItems = store.toString();
		}
		return storeItems.replaceAll("_"," ").replaceAll("\\s{2,}", " ");
	}


//	public static String getFullEnemyCrewType(SavedGameState gameState, int crewIndex) {
//		return getFullCrewType(gameState, crewIndex, true);
//	}
//	public static String getFullCrewType(SavedGameState gameState, int crewIndex) {
//		return getFullCrewType(gameState, crewIndex, false);
//	}
//	private static String getFullCrewType(SavedGameState gameState, int crewIndex, boolean isEnemyCrew) {
//		String rawCrewType = isEnemyCrew ?
//			gameState.enemyCrewState.get(crewIndex).getRace().toString() :
//			gameState.playerCrewState.get(crewIndex).getRace().toString();
//		return getFullCrewType(rawCrewType);
//	}
	private static String getFullCrewType(String rawCrewType) {

		String fullCrewType = "";

		if (rawCrewType.length() > 1) {

			switch (rawCrewType) {
				case "battle"    : fullCrewType = "Anti-Personel Drone"; break;
				case "energy"    : fullCrewType = "Zoltan"; break;
				case "anaerobic" : fullCrewType = "Lanius"; break;
				default :
					fullCrewType = rawCrewType.substring(0, 1).toUpperCase() + rawCrewType.substring(1);
				break;
			}

		}

		return fullCrewType;

	}

	public static int getRebelFleetAdvancement(SavedGameState gameState) {
		int rebelOffset = Math.abs(gameState.getRebelFleetOffset());
		int rebelFudge = gameState.getRebelFleetFudge();
		int result = (int)(100 - (rebelOffset - rebelFudge) * 100f / 650);
		return Math.max(0, result);
	}

	public static ArrayList<String> getBeaconHazards(SavedGameState gameState) {
		ArrayList<String> hazards = new ArrayList<>();

		EnvironmentState environment = gameState.getEnvironment();
		if (environment.isRedGiantPresent()) {
			hazards.add("Red Giant");
		}
		if (environment.isPulsarPresent()) {
			hazards.add("Pulsar");
		}
		if (environment.isPDSPresent()) {
			if (environment.getVulnerableShips() == HazardVulnerability.BOTH_SHIPS) {
				hazards.add("Neutral Planetary Defense System");
			} else if (environment.getVulnerableShips() == HazardVulnerability.PLAYER_SHIP) {
				hazards.add("Hostile Planetary Defense System");
			} else if (environment.getVulnerableShips() == HazardVulnerability.NEARBY_SHIP) {
				hazards.add("Allied Planetary Defense System");
			}
		}
		if (environment.getAsteroidField() != null) {
			hazards.add("Asteroid Field");
		}

		BeaconState beacon = gameState.getBeaconList().get(gameState.getCurrentBeaconId());
		if (beacon.isEnemyPresent()) {
			hazards.add("Enemy Ship");
		}
		if (beacon.getFleetPresence() == FleetPresence.REBEL) {
			hazards.add("Rebel Fleet");
		}
		if (beacon.getFleetPresence() == FleetPresence.FEDERATION) {
			hazards.add("Federation Fleet");
		}

		// if (FTLAdventureVisualiser.gameState.getStateVar("nebula") != null) // TODO Nebula Storm event

		return hazards;

	}

}
