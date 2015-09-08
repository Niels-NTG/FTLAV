package net.ntg.ftl.parser;

import net.ntg.ftl.FTLAdventureVisualiser;

public class ShipDataParser {

	public static int getShipOxygenLevel ( int index ) {

		int roomCount = FTLAdventureVisualiser.shipStateArray.get(index).getRoomList().size();
		int sum = 0;

		for (int i = 0; i < roomCount; i++) {
			sum += FTLAdventureVisualiser.shipStateArray.get(index).getRoomList().get(i).getOxygen();
		}

		return (int)sum / roomCount;

	}


	public static String getFullShipType () {
		return getFullShipType(0);
	}
	public static String getFullShipType ( int index ) {

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
				shipType = FTLAdventureVisualiser.gameStateArray.get(index).getPlayerShipBlueprintId();
			break;
		}

		return shipType;

	}


	public static String getCrewHealthRatio ( int index, int crewIndex ) {
		return Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(index).get(crewIndex).getHealth());
		// TODO figure out how to get max-health
		//+
		// " / " +
		// FTLAdventureVisualiser.playerCrewArray.get(index).get(crewIndex).getMaxHealth();
	}


	public static String getFullCrewType ( int index, int crewIndex ) {

		String fullCrewType = "";

		String rawCrewType = FTLAdventureVisualiser.playerCrewArray.get(index).get(crewIndex).getRace();

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


	public static String getAEEnabled () {
		return getAEEnabled(0);
	}
	public static String getAEEnabled ( int index ) {
		if (FTLAdventureVisualiser.gameStateArray.get(index).isDLCEnabled()) {
			return "Advanced";
		} else {
			return "";
		}
	}


	public static int getCurrentScore ( int index ) {

		int s = FTLAdventureVisualiser.gameStateArray.get(index).getTotalScrapCollected();
		int b = FTLAdventureVisualiser.gameStateArray.get(index).getTotalBeaconsExplored();
		int d = FTLAdventureVisualiser.gameStateArray.get(index).getTotalShipsDefeated();
		float dm;
		switch (FTLAdventureVisualiser.gameStateArray.get(index).getDifficulty().toString()) {
			case "EASY" : dm = 1f;   break;
			case "HARD" : dm = 1.5f; break;
			default     : dm = 1.5f; break;
		}

		return Math.round( (s + 10 * b + 20 * d) * dm );

	}


	public static int getRebelFleetDistance ( int index ) {

		// TODO test if this really works
		// TODO calculate distance from offset to pixel position current beacon player is at. Ask Vhati on how to get pixel coords of current beacon
		// TODO make exception for The Last Stand to track proximity to BOSS instead of rebelFleet

		int rebelOffset = FTLAdventureVisualiser.gameStateArray.get(index).getRebelFleetOffset();
		int rebelFudge  = FTLAdventureVisualiser.gameStateArray.get(index).getRebelFleetFudge();

		return rebelOffset + rebelFudge;

	}

}