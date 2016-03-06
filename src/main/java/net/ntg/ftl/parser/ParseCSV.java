package net.ntg.ftl.parser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.blerf.ftl.parser.SavedGameParser;

import net.ntg.ftl.FTLAdventureVisualiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ParseCSV {

	private static final Logger log = LogManager.getLogger(ParseCSV.class);

	private static final char DELIMITER = ';';

	// TODO

	// NEW
	// when a new file is created, just write a new file with createCSV
	// EXISTING
	// when a adding a new entry, read the current file,
	// append the new row to the result,
	// then write the new result to a file with the same name

	// WHEN TO WRITE
	// replace line 565 in FTLFrame with a trigger to write to existing file

	// WHEN TO READ
	// when ParseCSV is done writing the new result,
	// send columns titles to GraphInspector
	// send result to GraphRenderer where it gets converted to Processing.data.Table
	// when GraphInspector checkboxes has changed, redraw GraphRenderer


	public static void readCSV(String fileName) {



	}


	public static boolean isValidCSV(String fileName) {

		// TODO check if CSV file is valid

		return true; // EVERYTHING IS FINE!

	}


	public static void createCSV(String fileName) {
		writeCSV(fileName, true);
	}


	public static void writeCSV(String fileName) {
		writeCSV(fileName, false);
	}
	private static void writeCSV(String fileName, boolean createNewFile) {

		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

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
		for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired(); i++) {
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
			if (i < FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired() - 1) {
				fileHeader += DELIMITER;
			}
		}

		FileWriter fw = null;

		try {

			// if (!createNewFile) {
				// do a readCSV first, get results
				// write results from readCSV + newest entry (.get(latest))
			// }

			fw = new FileWriter(fileName);

			fw.append(fileHeader);
			fw.append("\n");

			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {

				int sectorNumber = FTLAdventureVisualiser.gameStateArray.get(i).getSectorNumber();

				fw.append(FTLAdventureVisualiser.timeStampArray.get(i));
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.gameStateArray.get(i).getPlayerShipName());
				fw.append(DELIMITER);
				fw.append(ShipDataParser.getFullShipType());
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.gameStateArray.get(i).getDifficulty().toString());
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.gameStateArray.get(0).isDLCEnabled() ? "enabled" : "disabled");
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getTotalBeaconsExplored()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(sectorNumber + 1));
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType());
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle());
				fw.append(DELIMITER);
				fw.append(ShipDataParser.getRebelFleetAdvancement(i) + "%");
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getTotalShipsDefeated()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getTotalScrapCollected()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getTotalCrewHired()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(ShipDataParser.getCurrentScore(i)));
				fw.append(DELIMITER);
				fw.append("\"" + ShipDataParser.getBeaconHazards(i) + "\"");
				fw.append(DELIMITER);
				fw.append("\"" + FTLAdventureVisualiser.gameStateArray.get(i).getEncounter().getText().replaceAll("(\")|(\\n+)","") + "\"");
				fw.append(DELIMITER);
				fw.append("\"" + ShipDataParser.getStoreListing(i) + "\"");
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getScrapAmt()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getHullAmt()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getFuelAmt()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getDronePartsAmt()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getMissilesAmt()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).size()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(ShipDataParser.getShipOxygenLevel(i)));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getReservePowerCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getDamagedBars()));
				fw.append(DELIMITER);
				for (int w = 0; w < ShipDataParser.getWeaponSlotCount(); w++) {
					try {
						fw.append(FTLAdventureVisualiser.shipStateArray.get(i).getWeaponList().get(w).getWeaponId().replaceAll("_"," "));
					} catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
				}
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getDamagedBars()));
				fw.append(DELIMITER);
				for (int d = 0; d < ShipDataParser.getDroneSlotCount(); d++) {
					try {
						fw.append(FTLAdventureVisualiser.shipStateArray.get(i).getDroneList().get(d).getDroneId().replaceAll("_"," "));
					} catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
				}
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getPower()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.PILOT).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.PILOT).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.SENSORS).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.SENSORS).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.DOORS).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.DOORS).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.BATTERY).getCapacity()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(FTLAdventureVisualiser.shipStateArray.get(i).getSystem(SavedGameParser.SystemType.BATTERY).getDamagedBars()));
				fw.append(DELIMITER);
				fw.append("\"" + ShipDataParser.getAugmentListing(i) + "\"");
				fw.append(DELIMITER);
				fw.append("\"" + ShipDataParser.getCargoListing(i) + "\"");
				fw.append(DELIMITER);
				for (int c = 0; c < FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired(); c++) {
					try { fw.append(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getName()); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(ShipDataParser.getFullCrewType(i, c)); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getHealth())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getPilotSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getEngineSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getShieldSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getWeaponSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getRepairSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getCombatSkill())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getRepairs())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getCombatKills())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getPilotedEvasions())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getJumpsSurvived())); } catch (IndexOutOfBoundsException e) {}
					fw.append(DELIMITER);
					try { fw.append(Integer.toString(FTLAdventureVisualiser.playerCrewArray.get(i).get(c).getSkillMasteries())); } catch (IndexOutOfBoundsException e) {}
					if (c < FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired() - 1) {
						fw.append(DELIMITER);
					}
				}
				fw.append("\n");

			}

			log.info("CSV file has been created");

		} catch (Exception e) {
			log.error("Error creating CSV file!", e);
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				log.error("Error while closing filewriter!", e);
			}
		}

	}

}