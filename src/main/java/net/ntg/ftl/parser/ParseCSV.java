package net.ntg.ftl.parser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.blerf.ftl.parser.SavedGameParser;

import net.ntg.ftl.FTLAdventureVisualiser;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;


public class ParseCSV {

	private static final Logger log = LogManager.getLogger(ParseCSV.class);

	private static final CsvPreference FTLCSV = (
		new CsvPreference.Builder('"', ';', "\n").surroundingSpacesNeedQuotes(true).build()
	);

	public void readCSV(String fileName) {

		FTLAdventureVisualiser.recording.clear();

		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(fileName), FTLCSV);

			final String[] header = mapReader.getHeader(true);
			FTLAdventureVisualiser.recordingHeaders = header;

			Map<String,String> customerMap;
			while((customerMap = mapReader.read(header)) != null) {
				log.info(String.format(
					"lineNo=%s, rowNo=%s, customerMap=%s",
					mapReader.getLineNumber(),
					mapReader.getRowNumber(),
					customerMap
				));
				FTLAdventureVisualiser.recording.add(customerMap);
			}
		} catch (Exception e) {
			log.error("Something went wrong while reading " + fileName, e);
		} finally {
			if (mapReader != null) {
				try {
					mapReader.close();
				} catch (IOException ex) {
					log.error(ex);
				}
			}
		}

	}


	public static boolean isValidCSV(String fileName) {

		// TODO check if CSV file is valid

		return true; // EVERYTHING IS FINE!

	}


	public void createCSV(String fileName) {
		writeCSV(fileName, true);
	}


	public void writeCSV(String fileName) {
		writeCSV(fileName, false);
	}
	private void writeCSV(String fileName, boolean isNewFile) {

		int sectorNumber = FTLAdventureVisualiser.gameState.getSectorNumber();

		LinkedHashMap<String, String> newRow = new LinkedHashMap<>();
		newRow.put("TIME", FTLAdventureVisualiser.fileChangedTimeStamp);
		newRow.put("SHIP NAME", FTLAdventureVisualiser.gameState.getPlayerShipName());
		newRow.put("SHIP TYPE", ShipDataParser.getFullShipType());
		newRow.put("DIFFICULTY", FTLAdventureVisualiser.gameState.getDifficulty().toString());
		newRow.put("AE CONTENT", FTLAdventureVisualiser.gameState.isDLCEnabled() ? "enabled" : "disabled");
		// Location
		newRow.put("BEACON", Integer.toString(FTLAdventureVisualiser.gameState.getTotalBeaconsExplored()));
		newRow.put("SECTOR NUMBER", Integer.toString(sectorNumber + 1));
		newRow.put("SECTOR TYPE", FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType());
		newRow.put("SECTOR TITLE", FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle());
		newRow.put("FLEET ADVANCEMENT", ShipDataParser.getRebelFleetAdvancement() + "%");
		// Log
		newRow.put("TOTAL SHIPS DEFEATED", Integer.toString(FTLAdventureVisualiser.gameState.getTotalShipsDefeated()));
		newRow.put("TOTAL SCRAP COLLECTED", Integer.toString(FTLAdventureVisualiser.gameState.getTotalScrapCollected()));
		newRow.put("TOTAL CREW HIRED", Integer.toString(FTLAdventureVisualiser.gameState.getTotalCrewHired()));
		newRow.put("SCORE", Integer.toString(ShipDataParser.getCurrentScore()));
		// Encounter
		newRow.put("HAZARDS", ShipDataParser.getBeaconHazards());
		newRow.put("EVENT TEXT", FTLAdventureVisualiser.gameState.getEncounter().getText().replaceAll("(\")|(\\n+)", ""));
		newRow.put("STORE", ShipDataParser.getStoreListing());
		// Supplies
		newRow.put("SCRAP", Integer.toString(FTLAdventureVisualiser.shipState.getScrapAmt()));
		newRow.put("HULL", Integer.toString(FTLAdventureVisualiser.shipState.getHullAmt()));
		newRow.put("FUEL", Integer.toString(FTLAdventureVisualiser.shipState.getFuelAmt()));
		newRow.put("DRONE PARTS", Integer.toString(FTLAdventureVisualiser.shipState.getDronePartsAmt()));
		newRow.put("MISSILES", Integer.toString(FTLAdventureVisualiser.shipState.getMissilesAmt()));
		newRow.put("CREW SIZE", Integer.toString(FTLAdventureVisualiser.playerCrewState.size()));
		newRow.put("CARGO", ShipDataParser.getCargoListing());
		newRow.put("AUGMENTS", ShipDataParser.getAugmentListing());
		newRow.put("OXYGEN LEVEL", Integer.toString(ShipDataParser.getShipOxygenLevel()));
		// Systems
		newRow.put("POWER CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getReservePowerCapacity()));
		newRow.put("SHIELD SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getCapacity()));
		newRow.put("SHIELD SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getPower()));
		newRow.put("SHIELD SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getDamagedBars()));
		newRow.put("ENGINE SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getCapacity()));
		newRow.put("ENGINE SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getPower()));
		newRow.put("ENGINE SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getDamagedBars()));
		newRow.put("OXYGEN SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getCapacity()));
		newRow.put("OXYGEN SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getPower()));
		newRow.put("OXYGEN SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getDamagedBars()));
		newRow.put("WEAPONS SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getCapacity()));
		newRow.put("WEAPONS SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getPower()));
		newRow.put("WEAPONS SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getDamagedBars()));
		for (int w = 0; w < ShipDataParser.getWeaponSlotCount(); w++) {
			try {
				newRow.put("WEAPON SLOT " + (w+1), FTLAdventureVisualiser.shipState.getWeaponList().get(w).getWeaponId().replaceAll("_"," "));
			} catch (IndexOutOfBoundsException e) {}
		}
		newRow.put("DRONE CONTROL SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getCapacity()));
		newRow.put("DRONE CONTROL SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getPower()));
		newRow.put("DRONE CONTROL SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getDamagedBars()));
		for (int d = 0; d < ShipDataParser.getDroneSlotCount(); d++) {
			try {
				newRow.put("DRONE SLOT " + (d+1), FTLAdventureVisualiser.shipState.getDroneList().get(d).getDroneId().replaceAll("_"," "));
			} catch (IndexOutOfBoundsException e) {}
		}
		newRow.put("MEDBAY SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getCapacity()));
		newRow.put("MEDBAY SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getPower()));
		newRow.put("MEDBAY SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getDamagedBars()));
		newRow.put("TELEPORTER SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getCapacity()));
		newRow.put("TELEPORTER SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getPower()));
		newRow.put("TELEPORTER SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getDamagedBars()));
		newRow.put("CLOAKING SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getCapacity()));
		newRow.put("CLOAKING SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getPower()));
		newRow.put("CLOAKING SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getDamagedBars()));
		newRow.put("ARTILLERY SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getCapacity()));
		newRow.put("ARTILLERY SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getPower()));
		newRow.put("ARTILLERY SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getDamagedBars()));
		newRow.put("CLONEBAY SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getCapacity()));
		newRow.put("CLONEBAY SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getPower()));
		newRow.put("CLONEBAY SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getDamagedBars()));
		newRow.put("MINDCONTROL SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getCapacity()));
		newRow.put("MINDCONTROL SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getPower()));
		newRow.put("MINDCONTROL SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getDamagedBars()));
		newRow.put("HACKING SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getCapacity()));
		newRow.put("HACKING SYSTEM POWER", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getPower()));
		newRow.put("HACKING SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getDamagedBars()));
		newRow.put("PILOT SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getCapacity()));
		newRow.put("PILOT SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getCapacity()));
		newRow.put("SENSOR SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getCapacity()));
		newRow.put("SENSOR SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getDamagedBars()));
		newRow.put("DOOR SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getCapacity()));
		newRow.put("DOOR SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getDamagedBars()));
		newRow.put("BATTERY SYSTEM CAPACITY", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getCapacity()));
		newRow.put("BATTERY SYSTEM DAMAGE", Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getDamagedBars()));
		// crew
		for (int c = 0; c < FTLAdventureVisualiser.gameState.getTotalCrewHired(); c++) {
			try {
				newRow.put("CREW MEMBER " + (c+1) + " NAME", FTLAdventureVisualiser.playerCrewState.get(c).getName());
				newRow.put("CREW MEMBER " + (c+1) + " SPECIES", ShipDataParser.getFullCrewType(c));
				newRow.put("CREW MEMBER " + (c+1) + " HEALTH", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getHealth()));
				newRow.put("CREW MEMBER " + (c+1) + " PILOT SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getPilotSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " ENGINE SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getEngineSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " SHIELD SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getShieldSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " WEAPON SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getWeaponSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " REPAIR SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getRepairSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " COMBAT SKILL", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getCombatSkill()));
				newRow.put("CREW MEMBER " + (c+1) + " REPAIRS", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getRepairs()));
				newRow.put("CREW MEMBER " + (c+1) + " COMBAT KILLS", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getCombatKills()));
				newRow.put("CREW MEMBER " + (c+1) + " PILOTED EVASIONS", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getPilotedEvasions()));
				newRow.put("CREW MEMBER " + (c+1) + " JUMPS SURVIVED", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getJumpsSurvived()));
				newRow.put("CREW MEMBER " + (c+1) + " SKILLS MASTERED", Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getSkillMasteries()));
			} catch (IndexOutOfBoundsException e) {}
		}

		FTLAdventureVisualiser.recording.add((Map<String,String>) newRow);
		String[] header = (String[]) newRow.keySet().toArray(new String[newRow.size()]);

		ICsvMapWriter mapWriter = null;
		try {
			mapWriter = new CsvMapWriter(new FileWriter(fileName), FTLCSV);

			// writer header
			mapWriter.writeHeader(header);

			// write all entries
			for (int i = 0; i < FTLAdventureVisualiser.recording.size(); i++) {
				// TODO prevent duplicate rows
				mapWriter.write(FTLAdventureVisualiser.recording.get(i), header);
			}
		} catch (Exception e) {
			log.error("Something went wrong while writing " + fileName, e);
		} finally {
			try {
				mapWriter.close();
			} catch (IOException ex) {
				log.error("Something went wrong closing fileWriter", ex);
			}
		}

	}

}