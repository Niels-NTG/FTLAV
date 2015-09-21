package net.ntg.ftl.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;

import net.blerf.ftl.parser.SavedGameParser;


public class CreateCSV {

	private static Logger log = LogManager.getLogger(CreateCSV.class);

	private static final String DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";


	public static void writeCSV ( String fileName ) {

		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		String fileHeader = (
		// Location
			"BEACON,"+
			"SECTOR NUMBER,"+
			"SECTOR TYPE,"+
			"SECTOR TITLE,"+
		// Encounter
			"HAZARDS,"+
			"EVENT TEXT,"+
		// Supplies
			"SCRAP,"+
			"HULL,"+
			"FUEL,"+
			"DRONE PARTS,"+
			"MISSILES,"+
			"CREW SIZE,"+
			"OXYGEN LEVEL,"+
		// Ship Systems
			"POWER CAPACITY,"+
			"SHIELDS CAPACITY,"+
			"SHIELD POWER,"+
			"SHIELD DAMAGE,"+
			"ENGINES CAPACITY,"+
			"ENGINES POWER,"+
			"ENGINES DAMAGE,"+
			"OXYGEN SYSTEM CAPACITY,"+
			"OXYGEN SYSTEM POWER,"+
			"OXYGEN SYSTEM DAMAGE,"+
			"WEAPONS SYSTEM CAPACITY,"+
			"WEAPONS SYSTEM POWER,"+
			"WEAPONS SYSTEM DAMAGE,"
		);
		for (int i = 0; i < ShipDataParser.getWeaponSlotCount(); i++) {
			fileHeader += ("WEAPON SLOT " + (i+1) + ",");
		}
		fileHeader += (
			"DRONE CONTROL SYSTEM CAPACITY,"+
			"DRONE CONTROL SYSTEM POWER,"+
			"DRONE CONTROL SYSTEM DAMAGE,"
		);
		for (int i = 0; i < ShipDataParser.getDroneSlotCount(); i++) {
			fileHeader += ("DRONE SLOT " + (i+1) + ",");
		}
		fileHeader += (
			"MEDBAY SYSTEM CAPACITY,"+
			"MEDBAY SYSTEM POWER,"+
			"MEDBAY SYSTEM DAMAGE,"+
			"TELEPORTER SYSTEM CAPACITY,"+
			"TELEPORTER SYSTEM POWER,"+
			"TELEPORTER SYSTEM DAMAGE,"+
			"CLOAKING SYSTEM CAPACITY,"+
			"CLOAKING SYSTEM POWER,"+
			"CLOAKING SYSTEM DAMAGE,"+
			"ARTILLERY SYSTEM CAPACITY,"+
			"ARTILLERY SYSTEM POWER,"+
			"ARTILLERY SYSTEM DAMAGE,"+
			"CLONEBAY SYSTEM CAPACITY,"+
			"CLONEBAY SYSTEM POWER,"+
			"CLONEBAY SYSTEM DAMAGE,"+
			"MINDCONTROL SYSTEM CAPACITY,"+
			"MINDCONTROL SYSTEM POWER,"+
			"MINDCONTROL SYSTEM DAMAGE,"+
			"HACKING SYSTEM CAPACITY,"+
			"HACKING SYSTEM POWER,"+
			"HACKING SYSTEM DAMAGE,"+
			"PILOT SYTEM CAPACITY,"+
			"PILOT SYSTEM DAMAGE,"+
			"SENSORS SYSTEM CAPACITY,"+
			"SENSORS SYSTEM DAMAGE,"+
			"DOORS SYSTEM CAPACITY,"+
			"DOORS SYSTEM DAMAGE,"+
			"BATTERY SYSTEM CAPACITY,"+
			"BATTERY SYSTEM DAMAGE,"+
		// Cargo
			"CARGO,"+
		// Augments
			"AUGMENTS"
		);
		// Crew


		FileWriter fw = null;

		try {

			fw = new FileWriter(
				fileName+
				" ("+
				ShipDataParser.getFullShipType() + " - "+
				FTLAdventureVisualiser.gameStateArray.get(0).getDifficulty().toString()+
				(FTLAdventureVisualiser.gameStateArray.get(0).isDLCEnabled() ? " AE" : "")+
				").csv"
			);

			fw.append(fileHeader.toString());
			fw.append(NEW_LINE_SEPARATOR);

			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {

				int sectorNumber = FTLAdventureVisualiser.gameStateArray.get(i).getSectorNumber();

				fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getTotalBeaconsExplored()));
				fw.append(DELIMITER);
				fw.append(Integer.toString(sectorNumber + 1));
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType());
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle());
				fw.append(DELIMITER);
				fw.append(ShipDataParser.getBeaconHazards(i));
				fw.append(DELIMITER);
				fw.append(FTLAdventureVisualiser.gameStateArray.get(i).getEncounter().getText());
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
				fw.append(ShipDataParser.getCargoListing(i).replaceAll("_"," "));
				fw.append(DELIMITER);
				fw.append(ShipDataParser.getAugmentListing(i).replaceAll("_"," "));
				fw.append(NEW_LINE_SEPARATOR);

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