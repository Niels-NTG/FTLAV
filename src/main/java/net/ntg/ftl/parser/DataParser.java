package net.ntg.ftl.parser;

import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.model.Score;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.SavedGameParser;
import net.ntg.ftl.FTLAdventureVisualiser;

import java.util.ArrayList;
import java.util.List;


public class DataParser {


	public static int getShipOxygenLevel() {

		List<SavedGameParser.RoomState> roomList = FTLAdventureVisualiser.shipState.getRoomList();
		int roomCount = roomList.size();
		int sum = 0;

		for (int i = 0; i < roomList.size(); i++) {
			SavedGameParser.RoomState aRoomList = roomList.get(i);
			sum += aRoomList.getOxygen();
		}

		return sum / roomCount;

	}


	 public static int getNearbyShipOxygenLevel() {

	 	if (FTLAdventureVisualiser.nearbyShipState != null) {
	 		List<SavedGameParser.RoomState> roomList = FTLAdventureVisualiser.nearbyShipState.getRoomList();
	 		int roomCount = roomList.size();
	 		int sum = 0;

	 		for (int i = 0; i < roomCount; i++) {
	 			sum += roomList.get(i).getOxygen();
	 		}

	 		return sum / roomCount;
	 	} else {
	 		return -1;
	 	}

	 }


	public static String getFullShipType() {

		switch (FTLAdventureVisualiser.gameState.getPlayerShipBlueprintId()) {
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
				return FTLAdventureVisualiser.gameState.getPlayerShipBlueprintId().replaceAll("_"," ");
		}

	}


	public static int getWeaponSlotCount() {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipState.getShipBlueprintId()).getWeaponSlots();
	}


	public static int getDroneSlotCount() {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipState.getShipBlueprintId()).getDroneSlots();
	}


	public static String getCargoListing() {
		List<String> cargoIdList = FTLAdventureVisualiser.gameState.getCargoIdList();
		String cargo = String.join(", ", cargoIdList);
		return cargo.replaceAll("_"," ");
	}


	public static String getAugmentListing() {
		List<String> augmentIdList = FTLAdventureVisualiser.shipState.getAugmentIdList();
		String aug = String.join(", ", augmentIdList);
		return aug.replaceAll("_"," ");
	}


	 public static String getNearbyShipAugmentListing() {
	 	List<String> augmentIdList = FTLAdventureVisualiser.nearbyShipState.getAugmentIdList();
	 	String aug = String.join(", ", augmentIdList);
	 	return aug.replaceAll("_"," ");
	 }


	public static String getStoreListing() {

		String storeItems = "";

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameState.getBeaconList().get(
			FTLAdventureVisualiser.gameState.getCurrentBeaconId()
		);

		SavedGameParser.StoreState store = beacon.getStore();

		if (store != null) {
			storeItems = store.toString();
		}

		return storeItems.replaceAll("_"," ").replaceAll("\\s{2,}", " ");

	}


	public static String getFullEnemyCrewType(int crewIndex) {
		return getFullCrewType(crewIndex, true);
	}
	public static String getFullCrewType(int crewIndex) {
		return getFullCrewType(crewIndex, false);
	}
	private static String getFullCrewType(int crewIndex, boolean isEnemyCrew) {
		String rawCrewType = isEnemyCrew ?
			FTLAdventureVisualiser.enemyCrewState.get(crewIndex).getRace() :
			FTLAdventureVisualiser.playerCrewState.get(crewIndex).getRace();
		return getFullCrewType(rawCrewType);
	}
	private static String getFullCrewType(String rawCrewType) {

		String fullCrewType = "";

		if (rawCrewType.length() > 1) {

			switch (rawCrewType) {
				case "battle"    : fullCrewType = "Anti-Personnel Drone"; break;
				case "energy"    : fullCrewType = "Zoltan"; break;
				case "anaerobic" : fullCrewType = "Lanius"; break;
				default :
					fullCrewType = rawCrewType.substring(0, 1).toUpperCase() + rawCrewType.substring(1);
				break;
			}

		}

		return fullCrewType;

	}


	public static int getCurrentScore() {

		int s = FTLAdventureVisualiser.gameState.getTotalScrapCollected();
		int b = FTLAdventureVisualiser.gameState.getTotalBeaconsExplored();
		int d = FTLAdventureVisualiser.gameState.getTotalShipsDefeated();
		float dm;
		switch (FTLAdventureVisualiser.gameState.getDifficulty().toString()) {
			case "EASY"  : dm = 1.0f;  break;
			case "NORMAL": dm = 1.25f; break;
			case "HARD"  : dm = 1.5f;  break;
			default      : dm = 1.0f;  break;
		}

		return (int)((s + 10*b + 20*d) * dm);

	}


	public static int getRebelFleetAdvancement() {

		int rebelOffset = Math.abs(FTLAdventureVisualiser.gameState.getRebelFleetOffset());
		int rebelFudge  = FTLAdventureVisualiser.gameState.getRebelFleetFudge();

		int result = (int)(100 - (((rebelOffset - rebelFudge) * 100f) / 650));
		return result < 0 ? 0 : result;

	}


	public static String getBeaconHazards() {

		List<String> hazards = new ArrayList<>();

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameState.getBeaconList().get(
			FTLAdventureVisualiser.gameState.getCurrentBeaconId()
		);

		if (FTLAdventureVisualiser.environmentState.isRedGiantPresent()) hazards.add("Solar Flares");
		if (FTLAdventureVisualiser.environmentState.isPulsarPresent()) hazards.add("Pulsar Star");
		if (FTLAdventureVisualiser.environmentState.isPDSPresent()) {
			if (FTLAdventureVisualiser.environmentState.getVulnerableShips() == SavedGameParser.HazardVulnerability.NEARBY_SHIP) {
				hazards.add("Allied Planetary Defense System");
			} else {
				hazards.add("Hostile Planetary Defense System");
			}
		}
		if (FTLAdventureVisualiser.environmentState.getAsteroidField() != null) hazards.add("Asteroid Field");
		if (beacon.isEnemyPresent()) hazards.add("Enemy Ship");
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.REBEL) hazards.add("Rebel Fleet");
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.FEDERATION) hazards.add("Federation Fleet");
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.BOTH) hazards.add("Rebel & Federation Fleet");

		// TODO pulsar ion storm hazard
		// if (FTLAdventureVisualiser.gameState.getStateVar("nebula") != null) // TODO Nebula Storm event

		return String.join(", ", hazards);

	}


	public static int getBestShipScore() {

		int bestShipScore = 0;
		Score bestShip = getBestShip();
		if (bestShip != null) {
			bestShipScore= bestShip.getValue();
		}

		return bestShipScore;

	}


	public static String getBestShipName() {

		String bestShipName = "";
		Score bestShip = getBestShip();
		if (bestShip != null) {
			bestShipName= bestShip.getShipName();
		}

		return bestShipName;

	}


	public static int getBestShipSector() {

		int bestShipSector = 0;
		Score bestShip = getBestShip();
		if (bestShip != null) {
			bestShipSector= bestShip.getSector() + 1;
		}

		return bestShipSector;

	}


	private static Score getBestShip() {

		List<Score> shipBest = FTLAdventureVisualiser.profile.getStats().getShipBest();
		Score bestShip = null;

		for (int i = 0; i < shipBest.size(); i++) {
			String shipID = shipBest.get(i).getShipId();
			Difficulty difficulty = shipBest.get(i).getDifficulty();
			if (
				FTLAdventureVisualiser.gameState.getPlayerShipBlueprintId().equals(shipID) &&
				FTLAdventureVisualiser.gameState.getDifficulty().equals(difficulty)
			) {
				bestShip = shipBest.get(i);
				break;
			}
		}

		return bestShip;

	}

}