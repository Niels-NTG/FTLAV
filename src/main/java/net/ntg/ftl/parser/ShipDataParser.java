package net.ntg.ftl.parser;

import net.ntg.ftl.FTLAdventureVisualiser;

import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.SavedGameParser;


public class ShipDataParser {

	public static int getShipOxygenLevel(int index) {

		int roomCount = FTLAdventureVisualiser.shipStateArray.get(index).getRoomList().size();
		int sum = 0;

		for (int i = 0; i < roomCount; i++) {
			sum += FTLAdventureVisualiser.shipStateArray.get(index).getRoomList().get(i).getOxygen();
		}

		return (int)sum / roomCount;

	}

	public static int getNearbyShipOxygenLevel(int index) {

		if (FTLAdventureVisualiser.nearbyShipStateArray.get(index) != null) {
			int roomCount = FTLAdventureVisualiser.nearbyShipStateArray.get(index).getRoomList().size();
			int sum = 0;

			for (int i = 0; i < roomCount; i++) {
				sum += FTLAdventureVisualiser.nearbyShipStateArray.get(index).getRoomList().get(i).getOxygen();
			}

			return (int)sum / roomCount;
		} else {
			return -1;
		}

	}


	public static String getFullShipType() {
		return getFullShipType(0);
	}
	public static String getFullShipType(int index) {

		String shipType = "";

		switch (FTLAdventureVisualiser.gameStateArray.get(index).getPlayerShipBlueprintId()) {
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
				shipType = FTLAdventureVisualiser.gameStateArray.get(index).getPlayerShipBlueprintId().replaceAll("_"," ");
			break;
		}

		return shipType;

	}


	public static int getWeaponSlotCount() { return getWeaponSlotCount(0); }
	public static int getWeaponSlotCount(int index) {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipStateArray.get(index).getShipBlueprintId()).getWeaponSlots();
	}


	public static int getDroneSlotCount() { return getDroneSlotCount(0); }
	public static int getDroneSlotCount(int index) {
		return DataManager.get().getShip(FTLAdventureVisualiser.shipStateArray.get(index).getShipBlueprintId()).getDroneSlots();
	}


	public static String getCargoListing(int index) {
		String cargo = "";
		for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.get(index).getCargoIdList().size(); i++) {
			cargo += FTLAdventureVisualiser.gameStateArray.get(index).getCargoIdList().get(i) + ", ";
		}
		return cargo.replaceAll("_"," ").replaceAll(",\\s$","");
	}


	public static String getAugmentListing(int index) {
		String aug = "";
		for (int i = 0; i < FTLAdventureVisualiser.shipStateArray.get(index).getAugmentIdList().size(); i++) {
			aug += FTLAdventureVisualiser.shipStateArray.get(index).getAugmentIdList().get(i) + ", ";
		}
		return aug.replaceAll("_"," ").replaceAll(",\\s$","");
	}


	public static String getNearbyShipAugmentListing(int index) {
		String aug = "";
		for (int i = 0; i < FTLAdventureVisualiser.nearbyShipStateArray.get(index).getAugmentIdList().size(); i++) {
			aug += FTLAdventureVisualiser.nearbyShipStateArray.get(index).getAugmentIdList().get(i) + ", ";
		}
		return aug.replaceAll("_"," ").replaceAll(",\\s$","");
	}


	public static String getStoreListing(int index) {

		String storeItems = "";

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameStateArray.get(index).getBeaconList().get(
			FTLAdventureVisualiser.gameStateArray.get(index).getCurrentBeaconId()
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
				storeItems.replaceAll(",\\s$","");
			}
		}

		return storeItems.replaceAll("_"," ");

	}


	public static String getFullEnemyCrewType(int index, int crewIndex) {
		return getFullCrewType(index, crewIndex, true);
	}
	public static String getFullCrewType(int index, int crewIndex) {
		return getFullCrewType(index, crewIndex, false);
	}
	public static String getFullCrewType(int index, int crewIndex, boolean isEnemyCrew) {
		String rawCrewType = isEnemyCrew ?
			FTLAdventureVisualiser.enemyCrewArray.get(index).get(crewIndex).getRace() :
			FTLAdventureVisualiser.playerCrewArray.get(index).get(crewIndex).getRace();
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

		} else {
			fullCrewType = "";
		}

		return fullCrewType;

	}


	public static int getCurrentScore(int index) {

		int s = FTLAdventureVisualiser.gameStateArray.get(index).getTotalScrapCollected();
		int b = FTLAdventureVisualiser.gameStateArray.get(index).getTotalBeaconsExplored();
		int d = FTLAdventureVisualiser.gameStateArray.get(index).getTotalShipsDefeated();
		float dm;
		switch (FTLAdventureVisualiser.gameStateArray.get(index).getDifficulty().toString()) {
			case "EASY"  : dm = 1.0f;  break;
			case "NORMAL": dm = 1.25f; break;
			case "HARD"  : dm = 1.5f;  break;
			default      : dm = 1.0f;  break;
		}

		return (int)((s + 10*b + 20*d) * dm);

	}


	public static int getRebelFleetAdvancement(int index) {

		int rebelOffset = Math.abs(FTLAdventureVisualiser.gameStateArray.get(index).getRebelFleetOffset());
		int rebelFudge  = FTLAdventureVisualiser.gameStateArray.get(index).getRebelFleetFudge();

		int result = (int)(100 - (((rebelOffset - rebelFudge) * 100f) / 650));
		return result < 0 ? 0 : result;

	}


	public static String getBeaconHazards(int index) {

		String sb = "";

		SavedGameParser.BeaconState beacon = FTLAdventureVisualiser.gameStateArray.get(index).getBeaconList().get(
			FTLAdventureVisualiser.gameStateArray.get(index).getCurrentBeaconId()
		);

		if (FTLAdventureVisualiser.environmentArray.get(index).isRedGiantPresent()) sb += "Solar Flares, ";
		if (FTLAdventureVisualiser.environmentArray.get(index).isPulsarPresent()) sb += "Pulsar Star, ";
		if (FTLAdventureVisualiser.environmentArray.get(index).isPDSPresent()) {
			if (FTLAdventureVisualiser.environmentArray.get(index).getVulnerableShips() == SavedGameParser.HazardVulnerability.NEARBY_SHIP) {
				sb += "Allied Planetary Defense System, ";
			} else {
				sb += "Hostile Planetary Defense System, ";
			}
		}
		if (FTLAdventureVisualiser.environmentArray.get(index).getAsteroidField() != null) sb += "Asteroid Field, ";
		if (beacon.isEnemyPresent()) sb += "Enemy Ship, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.REBEL) sb += "Rebel Fleet, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.FEDERATION) sb += "Federation Fleet, ";
		if (beacon.getFleetPresence() == SavedGameParser.FleetPresence.BOTH) sb += "Rebel & Federation Fleet, ";
		// if (FTLAdventureVisualiser.gameStateArray.get(index).getStateVar("nebula") != null) {

		// }
		// TODO Nebula Storm event

		return sb.replaceAll(",\\s$","");

	}

}