package net.ntg.ftl.parser;

import net.ntg.ftl.FTLAdventureVisualiser;

import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.SavedGameParser;


public class ShipDataParser {

	public static int getShipOxygenLevel() {

		int roomCount = FTLAdventureVisualiser.shipState.getRoomList().size();
		int sum = 0;

		for (int i = 0; i < roomCount; i++) {
			sum += FTLAdventureVisualiser.shipState.getRoomList().get(i).getOxygen();
		}

		return (int)sum / roomCount;

	}


	// public static int getNearbyShipOxygenLevel() {

	// 	if (FTLAdventureVisualiser.nearbyShipState != null) {
	// 		int roomCount = FTLAdventureVisualiser.nearbyShipState.getRoomList().size();
	// 		int sum = 0;

	// 		for (int i = 0; i < roomCount; i++) {
	// 			sum += FTLAdventureVisualiser.nearbyShipState.getRoomList().get(i).getOxygen();
	// 		}

	// 		return (int)sum / roomCount;
	// 	} else {
	// 		return -1;
	// 	}

	// }


	public static String getFullShipType() {

		String shipType = "";

		switch (FTLAdventureVisualiser.gameState.getPlayerShipBlueprintId()) {
			case "PLAYER_SHIP_HARD"   : shipType = "Kestrel Cruiser A"; break;
			case "PLAYER_SHIP_HARD_2" : shipType = "Kestrel Cruiser B"; break;
			case "PLAYER_SHIP_HARD_3" : shipType = "Kestrel Cruiser C"; break;

			case "PLAYER_SHIP_CIRCLE"   : shipType = "Engi Cruiser A"; break;
			case "PLAYER_SHIP_CIRCLE_2" : shipType = "Engi Cruiser B"; break;
			case "PLAYER_SHIP_CIRCLE_3" : shipType = "Engi Cruiser C"; break;

			case "PLAYER_SHIP_FED"   : shipType = "Federation Cruiser A"; break;
			case "PLAYER_SHIP_FED_2" : shipType = "Federation Cruiser B"; break;
			case "PLAYER_SHIP_FED_3" : shipType = "Federation Cruiser C"; break;

			case "PLAYER_SHIP_ENERGY"   : shipType = "Zoltan Cruiser A"; break;
			case "PLAYER_SHIP_ENERGY_2" : shipType = "Zoltan Cruiser B"; break;
			case "PLAYER_SHIP_ENERGY_3" : shipType = "Zoltan Cruiser C"; break;

			case "PLAYER_SHIP_STEALTH"   : shipType = "Stealth Cruiser A"; break;
			case "PLAYER_SHIP_STEALTH_2" : shipType = "Stealth Cruiser B"; break;
			case "PLAYER_SHIP_STEALTH_3" : shipType = "Stealth Cruiser C"; break;

			case "PLAYER_SHIP_ROCK"   : shipType = "Rock Cruiser A"; break;
			case "PLAYER_SHIP_ROCK_2" : shipType = "Rock Cruiser B"; break;
			case "PLAYER_SHIP_ROCK_3" : shipType = "Rock Cruiser C"; break;

			case "PLAYER_SHIP_MANTIS"   : shipType = "Mantis Cruiser A"; break;
			case "PLAYER_SHIP_MANTIS_2" : shipType = "Mantis Cruiser B"; break;
			case "PLAYER_SHIP_MANTIS_3" : shipType = "Mantis Cruiser C"; break;

			case "PLAYER_SHIP_JELLY"   : shipType = "Slug Cruiser A"; break;
			case "PLAYER_SHIP_JELLY_2" : shipType = "Slug Cruiser B"; break;
			case "PLAYER_SHIP_JELLY_3" : shipType = "Slug Cruiser C"; break;

			case "PLAYER_SHIP_ANAEROBIC"   : shipType = "Lanius Cruiser A"; break;
			case "PLAYER_SHIP_ANAEROBIC_2" : shipType = "Lanius Cruiser B"; break;
			case "PLAYER_SHIP_ANAEROBIC_3" : shipType = "Lanius Cruiser C"; break;

			default :
				shipType = FTLAdventureVisualiser.gameState.getPlayerShipBlueprintId().replaceAll("_"," ");
			break;
		}

		return shipType;

	}


	public static int getWeaponSlotCount() {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipState.getShipBlueprintId()).getWeaponSlots();
	}


	public static int getDroneSlotCount() {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipState.getShipBlueprintId()).getDroneSlots();
	}


	public static String getCargoListing() {
		String cargo = "";
		for (int i = 0; i < FTLAdventureVisualiser.gameState.getCargoIdList().size(); i++) {
			cargo += FTLAdventureVisualiser.gameState.getCargoIdList().get(i) + ", ";
		}
		return cargo.replaceAll("_"," ").replaceAll(",\\s$","");
	}


	public static String getAugmentListing() {
		String aug = "";
		for (int i = 0; i < FTLAdventureVisualiser.shipState.getAugmentIdList().size(); i++) {
			aug += FTLAdventureVisualiser.shipState.getAugmentIdList().get(i) + ", ";
		}
		return aug.replaceAll("_"," ").replaceAll(",\\s$","");
	}


	// public static String getNearbyShipAugmentListing() {
	// 	String aug = "";
	// 	for (int i = 0; i < FTLAdventureVisualiser.nearbyShipState.getAugmentIdList().size(); i++) {
	// 		aug += FTLAdventureVisualiser.nearbyShipState.getAugmentIdList().get(i) + ", ";
	// 	}
	// 	return aug.replaceAll("_"," ").replaceAll(",\\s$","");
	// }


	public static String getStoreListing() {

		String storeItems = "";

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameState.getBeaconList().get(
			FTLAdventureVisualiser.gameState.getCurrentBeaconId()
		);

		if (beacon.getStore() != null) {
			for (int i = 0; i < beacon.getStore().getShelfList().size(); i++) {
				storeItems += beacon.getStore().getShelfList().get(i).getItemType().toString() + ": ";
				for (int k = 0; k < beacon.getStore().getShelfList().get(i).getItems().size(); k++) {
					if (beacon.getStore().getShelfList().get(i).getItemType().toString().equals("Crew")) {
						storeItems += getFullCrewType(beacon.getStore().getShelfList().get(i).getItems().get(k).getItemId()) + ", ";
					} else {
						storeItems += beacon.getStore().getShelfList().get(i).getItems().get(k).getItemId() + ", ";
					}
				}
				storeItems.replaceAll(",\\s*$","");
			}
		}

		return storeItems.replaceAll("_"," ");

	}


	public static String getFullEnemyCrewType(int crewIndex) {
		return getFullCrewType(crewIndex, true);
	}
	public static String getFullCrewType(int crewIndex) {
		return getFullCrewType(crewIndex, false);
	}
	public static String getFullCrewType(int crewIndex, boolean isEnemyCrew) {
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

		String sb = "";

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameState.getBeaconList().get(
			FTLAdventureVisualiser.gameState.getCurrentBeaconId()
		);

		if (FTLAdventureVisualiser.environmentState.isRedGiantPresent()) sb += "Solar Flares, ";
		if (FTLAdventureVisualiser.environmentState.isPulsarPresent()) sb += "Pulsar Star, ";
		if (FTLAdventureVisualiser.environmentState.isPDSPresent()) {
			if (FTLAdventureVisualiser.environmentState.getVulnerableShips() == SavedGameParser.HazardVulnerability.NEARBY_SHIP) {
				sb += "Allied Planetary Defense System, ";
			} else {
				sb += "Hostile Planetary Defense System, ";
			}
		}
		if (FTLAdventureVisualiser.environmentState.getAsteroidField() != null) sb += "Asteroid Field, ";
		if (beacon.isEnemyPresent()) sb += "Enemy Ship, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.REBEL) sb += "Rebel Fleet, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.FEDERATION) sb += "Federation Fleet, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.BOTH) sb += "Rebel & Federation Fleet, ";
		// TODO pulsar ion storm hazard
		// if (FTLAdventureVisualiser.gameState.getStateVar("nebula") != null) // TODO Nebula Storm event

		return sb.replaceAll(",\\s*$","");

	}

}