package net.ntg.ftl.parser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.blerf.ftl.parser.SavedGameParser;

import net.ntg.ftl.FTLAdventureVisualiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;


public class ParseCSV {

	private static final Logger log = LogManager.getLogger(ParseCSV.class);

	private static final CsvPreference FTLCSV = (
		new CsvPreference.Builder('"', ';', "\n").useQuoteMode(new AlwaysQuoteMode()).surroundingSpacesNeedQuotes(true).build()
	);
	private static final char DELIMITER = (char) FTLCSV.getDelimiterChar();
	private static final String NEWLINE = FTLCSV.getEndOfLineSymbols();


	public void readCSV(String fileName) {

		FTLAdventureVisualiser.recording.clear();

		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(fileName), FTLCSV);

			final String[] header = mapReader.getHeader(true);
			FTLAdventureVisualiser.recordingHeaders = header;

			Map<String, String> customerMap;
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

					log.info(FTLAdventureVisualiser.recordingHeaders);
					log.info("FTLAdventureVisualiser.recording.size() : " + FTLAdventureVisualiser.recording.size());
					for (int i = 0; i < FTLAdventureVisualiser.recording.size(); i++) {
						log.info(FTLAdventureVisualiser.recording.get(i).toString());
					}

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

		String fileHeader = (
			"TIME" + DELIMITER +
			"SHIP NAME" + DELIMITER +
			"SHIP TYPE" + DELIMITER +
			"DIFFICULTY" + DELIMITER +
			"AE CONTENT" + DELIMITER +
		// Location
			"BEACON" + DELIMITER +
			"SECTOR NUMBER" + DELIMITER +
			"SECTOR TYPE" + DELIMITER +
			"SECTOR TITLE" + DELIMITER +
			"FLEET ADVANCEMENT" + DELIMITER +
		// LOG
			"TOTAL SHIPS DEFEATED" + DELIMITER +
			"TOTAL SCRAP COLLECTED" + DELIMITER +
			"TOTAL CREW HIRED" + DELIMITER +
			"SCORE" + DELIMITER +
		// Encounter
			"HAZARDS" + DELIMITER +
			"EVENT TEXT" + DELIMITER +
			"STORE" + DELIMITER +
		// Supplies
			"SCRAP" + DELIMITER +
			"HULL" + DELIMITER +
			"FUEL" + DELIMITER +
			"DRONE PARTS" + DELIMITER +
			"MISSILES" + DELIMITER +
			"CREW SIZE" + DELIMITER +
			"OXYGEN LEVEL" + DELIMITER +
		// Ship Systems
			"POWER CAPACITY" + DELIMITER +
			"SHIELDS CAPACITY" + DELIMITER +
			"SHIELD POWER" + DELIMITER +
			"SHIELD DAMAGE" + DELIMITER +
			"ENGINES CAPACITY" + DELIMITER +
			"ENGINES POWER" + DELIMITER +
			"ENGINES DAMAGE" + DELIMITER +
			"OXYGEN SYSTEM CAPACITY" + DELIMITER +
			"OXYGEN SYSTEM POWER" + DELIMITER +
			"OXYGEN SYSTEM DAMAGE" + DELIMITER +
			"WEAPONS SYSTEM CAPACITY" + DELIMITER +
			"WEAPONS SYSTEM POWER" + DELIMITER +
			"WEAPONS SYSTEM DAMAGE" + DELIMITER
		);
		for (int i = 0; i < ShipDataParser.getWeaponSlotCount(); i++) {
			fileHeader += "WEAPON SLOT " + (i+1) + DELIMITER;
		}
		fileHeader += (
			"DRONE CONTROL SYSTEM CAPACITY" + DELIMITER +
			"DRONE CONTROL SYSTEM POWER" + DELIMITER +
			"DRONE CONTROL SYSTEM DAMAGE" + DELIMITER
		);
		for (int i = 0; i < ShipDataParser.getDroneSlotCount(); i++) {
			fileHeader += "DRONE SLOT " + (i+1) + DELIMITER;
		}
		fileHeader += (
			"MEDBAY SYSTEM CAPACITY" + DELIMITER +
			"MEDBAY SYSTEM POWER" + DELIMITER +
			"MEDBAY SYSTEM DAMAGE" + DELIMITER +
			"TELEPORTER SYSTEM CAPACITY" + DELIMITER +
			"TELEPORTER SYSTEM POWER" + DELIMITER +
			"TELEPORTER SYSTEM DAMAGE" + DELIMITER +
			"CLOAKING SYSTEM CAPACITY" + DELIMITER +
			"CLOAKING SYSTEM POWER" + DELIMITER +
			"CLOAKING SYSTEM DAMAGE" + DELIMITER +
			"ARTILLERY SYSTEM CAPACITY" + DELIMITER +
			"ARTILLERY SYSTEM POWER" + DELIMITER +
			"ARTILLERY SYSTEM DAMAGE" + DELIMITER +
			"CLONEBAY SYSTEM CAPACITY" + DELIMITER +
			"CLONEBAY SYSTEM POWER" + DELIMITER +
			"CLONEBAY SYSTEM DAMAGE" + DELIMITER +
			"MINDCONTROL SYSTEM CAPACITY" + DELIMITER +
			"MINDCONTROL SYSTEM POWER" + DELIMITER +
			"MINDCONTROL SYSTEM DAMAGE" + DELIMITER +
			"HACKING SYSTEM CAPACITY" + DELIMITER +
			"HACKING SYSTEM POWER" + DELIMITER +
			"HACKING SYSTEM DAMAGE" + DELIMITER +
			"PILOT SYTEM CAPACITY" + DELIMITER +
			"PILOT SYSTEM DAMAGE" + DELIMITER +
			"SENSORS SYSTEM CAPACITY" + DELIMITER +
			"SENSORS SYSTEM DAMAGE" + DELIMITER +
			"DOORS SYSTEM CAPACITY" + DELIMITER +
			"DOORS SYSTEM DAMAGE" + DELIMITER +
			"BATTERY SYSTEM CAPACITY" + DELIMITER +
			"BATTERY SYSTEM DAMAGE" + DELIMITER +
		// Augments
			"AUGMENTS" + DELIMITER +
		// Cargo
			"CARGO" + DELIMITER
		);
		// Crew
		for (int i = 0; i < FTLAdventureVisualiser.gameState.getTotalCrewHired(); i++) {
			fileHeader += ("CREW MEMBER " + (i+1) + " NAME" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " SPECIES" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " HEALTH" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " PILOT SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " ENGINE SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " SHIELD SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " WEAPON SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " REPAIR SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " COMBAT SKILL" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " REPAIRS" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " COMBAT KILLS" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " PILOTED EVASIONS" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " JUMPS SURVIVED" + DELIMITER);
			fileHeader += ("CREW MEMBER " + (i+1) + " SKILLS MASTERED");
			if (i < FTLAdventureVisualiser.gameState.getTotalCrewHired() - 1) {
				fileHeader += DELIMITER;
			}
		}

		FileWriter fw = null;

		try {

			fw = new FileWriter(fileName);

			fw.append(fileHeader);
			fw.append(NEWLINE);

			// TODO use superCSV mapWriter to write the data (so it doesn't print null where it is empty or doesn't apply qoutes where needed)
			if (!isNewFile) {
				log.info("Adding existing data...");
				String[] headerArray = fileHeader.split(Character.toString(DELIMITER));
				for (int i = 0; i < FTLAdventureVisualiser.recording.size(); i++) {
					for (int k = 0; k < headerArray.length; k++) {
						try {
							fw.append(FTLAdventureVisualiser.recording.get(i).get(headerArray[k]));
						} catch (NullPointerException e) {
							log.error(headerArray[k] + " does not exist in FTLAdventureVisualiser.recording", e);
						}
						if (k < headerArray.length - 1) fw.append(DELIMITER);
					}
					fw.append(NEWLINE);
				}
			}

			int sectorNumber = FTLAdventureVisualiser.gameState.getSectorNumber();

			fw.append(FTLAdventureVisualiser.fileChangedTimeStamp);
			fw.append(DELIMITER);
			fw.append(FTLAdventureVisualiser.gameState.getPlayerShipName());
			fw.append(DELIMITER);
			fw.append(ShipDataParser.getFullShipType());
			fw.append(DELIMITER);
			fw.append(FTLAdventureVisualiser.gameState.getDifficulty().toString());
			fw.append(DELIMITER);
			fw.append(FTLAdventureVisualiser.gameState.isDLCEnabled() ? "enabled" : "disabled");
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.gameState.getTotalBeaconsExplored()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(sectorNumber + 1));
			fw.append(DELIMITER);
			fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType());
			fw.append(DELIMITER);
			fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle());
			fw.append(DELIMITER);
			fw.append(ShipDataParser.getRebelFleetAdvancement() + "%");
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.gameState.getTotalShipsDefeated()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.gameState.getTotalScrapCollected()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.gameState.getTotalCrewHired()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(ShipDataParser.getCurrentScore()));
			fw.append(DELIMITER);
			fw.append("\"" + ShipDataParser.getBeaconHazards() + "\"");
			fw.append(DELIMITER);
			fw.append("\"" + FTLAdventureVisualiser.gameState.getEncounter().getText().replaceAll("(\")|(\\n+)","") + "\"");
			fw.append(DELIMITER);
			fw.append("\"" + ShipDataParser.getStoreListing() + "\"");
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getScrapAmt()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getHullAmt()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getFuelAmt()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getDronePartsAmt()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getMissilesAmt()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.size()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(ShipDataParser.getShipOxygenLevel()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getReservePowerCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getDamagedBars()));
			fw.append(DELIMITER);
			for (int w = 0; w < ShipDataParser.getWeaponSlotCount(); w++) {
				try {
					fw.append(FTLAdventureVisualiser.shipState.getWeaponList().get(w).getWeaponId().replaceAll("_"," "));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
			}
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getDamagedBars()));
			fw.append(DELIMITER);
			for (int d = 0; d < ShipDataParser.getDroneSlotCount(); d++) {
				try {
					fw.append(FTLAdventureVisualiser.shipState.getDroneList().get(d).getDroneId().replaceAll("_"," "));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
			}
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getPower()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getCapacity()));
			fw.append(DELIMITER);
			fw.append(Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getDamagedBars()));
			fw.append(DELIMITER);
			fw.append("\"" + ShipDataParser.getAugmentListing() + "\"");
			fw.append(DELIMITER);
			fw.append("\"" + ShipDataParser.getCargoListing() + "\"");
			fw.append(DELIMITER);
			for (int c = 0; c < FTLAdventureVisualiser.gameState.getTotalCrewHired(); c++) {
				try {
					fw.append(FTLAdventureVisualiser.playerCrewState.get(c).getName());
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(ShipDataParser.getFullCrewType(c));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getHealth()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getPilotSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getEngineSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getShieldSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getWeaponSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getRepairSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getCombatSkill()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getRepairs()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getCombatKills()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getPilotedEvasions()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getJumpsSurvived()));
				} catch (IndexOutOfBoundsException e) {}
				fw.append(DELIMITER);
				try {
					fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewState.get(c).getSkillMasteries()));
				} catch (IndexOutOfBoundsException e) {}
				if (c < FTLAdventureVisualiser.gameState.getTotalCrewHired() - 1) {
					fw.append(DELIMITER);
				}
			}

		} catch (Exception e) {
			log.error("Error creating CSV file!", e);
		} finally {
			try {
				fw.flush();
				fw.close();
				log.info("CSV file has been written to " + fileName);
			} catch (IOException e) {
				log.error("Error while closing filewriter!", e);
			}
		}

	}

}