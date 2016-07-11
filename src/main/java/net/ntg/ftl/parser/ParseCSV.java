package net.ntg.ftl.parser;

import net.blerf.ftl.parser.SavedGameParser;
import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.constants.RecordingHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class ParseCSV {

	private static final Logger log = LogManager.getLogger(ParseCSV.class);

	private static final CsvPreference FTLCSV = new CsvPreference.Builder('"', ';', "\n").surroundingSpacesNeedQuotes(true).build();

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

				if (!FTLAdventureVisualiser.recording.contains(customerMap)) {
					FTLAdventureVisualiser.recording.add(customerMap);
				}
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


	public boolean isValidCSV(String fileName) {

		// TODO check if CSV file is valid

		return true; // EVERYTHING IS FINE!

	}


	public void writeCSV(String fileName) {

		int sectorNumber = FTLAdventureVisualiser.gameState.getSectorNumber();

		LinkedHashMap<String, String> newRow = new LinkedHashMap<>();

		// Non Temporal
		newRow.put(
			RecordingHeader.TIME,
			FTLAdventureVisualiser.fileChangedTimeStamp
		);
		newRow.put(
			RecordingHeader.SHIP_NAME,
			FTLAdventureVisualiser.gameState.getPlayerShipName()
		);
		newRow.put(
			RecordingHeader.SHIP_TYPE,
			DataParser.getFullShipType()
		);
		newRow.put(
			RecordingHeader.DIFFICULTY,
			FTLAdventureVisualiser.gameState.getDifficulty().toString()
		);
		newRow.put(
			RecordingHeader.AE_CONTENT,
			FTLAdventureVisualiser.gameState.isDLCEnabled() ? "enabled" : "disabled"
		);

		// Location
		newRow.put(
			RecordingHeader.Location.BEACON_NUMBER,
			Integer.toString(FTLAdventureVisualiser.gameState.getTotalBeaconsExplored())
		);
		newRow.put(
			RecordingHeader.Location.SECTOR_NUMBER,
			Integer.toString(sectorNumber + 1)
		);
		newRow.put(
			RecordingHeader.Location.SECTOR_TYPE,
			FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType()
		);
		newRow.put(
			RecordingHeader.Location.SECTOR_TITLE,
			FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle()
		);
		newRow.put(
			RecordingHeader.Location.FLEET_ADVANCEMENT,
			Integer.toString(DataParser.getRebelFleetAdvancement())
		);

		// Log
		newRow.put(
			RecordingHeader.Log.TOTAL_SHIPS_DEFEATED,
			Integer.toString(FTLAdventureVisualiser.gameState.getTotalShipsDefeated())
		);
		newRow.put(
			RecordingHeader.Log.TOTAL_SCRAP_COLLECTED,
			Integer.toString(FTLAdventureVisualiser.gameState.getTotalScrapCollected())
		);
		newRow.put(
			RecordingHeader.Log.TOTAL_CREW_HIRED,
			Integer.toString(FTLAdventureVisualiser.gameState.getTotalCrewHired())
		);
		newRow.put(
			RecordingHeader.Log.SCORE,
			Integer.toString(DataParser.getCurrentScore())
		);

		// Encounter
		newRow.put(
			RecordingHeader.Encounter.HAZARDS,
			DataParser.getBeaconHazards()
		);
		newRow.put(
			RecordingHeader.Encounter.EVENT_TEXT,
			FTLAdventureVisualiser.gameState.getEncounter().getText()
		);
		newRow.put(
			RecordingHeader.Encounter.STORE_LIST,
			DataParser.getStoreListing()
		);

		// Supplies
		newRow.put(
			RecordingHeader.Supplies.SCRAP,
			Integer.toString(FTLAdventureVisualiser.shipState.getScrapAmt())
		);
		newRow.put(
			RecordingHeader.Supplies.HULL,
			Integer.toString(FTLAdventureVisualiser.shipState.getHullAmt())
		);
		newRow.put(
			RecordingHeader.Supplies.FUEL,
			Integer.toString(FTLAdventureVisualiser.shipState.getFuelAmt())
		);
		newRow.put(
			RecordingHeader.Supplies.DRONE_PARTS,
			Integer.toString(FTLAdventureVisualiser.shipState.getDronePartsAmt())
		);
		newRow.put(
			RecordingHeader.Supplies.MISSILES,
			Integer.toString(FTLAdventureVisualiser.shipState.getMissilesAmt())
		);
		newRow.put(
			RecordingHeader.Supplies.CREW_SIZE,
			Integer.toString(FTLAdventureVisualiser.playerCrewState.size())
		);
		newRow.put(
			RecordingHeader.Supplies.CARGO_LIST,
			DataParser.getCargoListing()
		);
		newRow.put(
			RecordingHeader.Supplies.AUGMENT_LIST,
			DataParser.getAugmentListing()
		);
		newRow.put(
			RecordingHeader.Supplies.OXYGEN_LEVEL,
			Integer.toString(DataParser.getShipOxygenLevel())
		);

		// Systems
		newRow.put(
			RecordingHeader.System.POWER_RESERVE,
			Integer.toString(FTLAdventureVisualiser.shipState.getReservePowerCapacity())
		);
		newRow.put(
			RecordingHeader.System.Shield.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Shield.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getPower())
		);
		newRow.put(
			RecordingHeader.System.Shield.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SHIELDS).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Engine.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Engine.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getPower())
		);
		newRow.put(
			RecordingHeader.System.Engine.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ENGINES).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Oxygen.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Oxygen.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getPower())
		);
		newRow.put(
			RecordingHeader.System.Oxygen.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.OXYGEN).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Weapons.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Weapons.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getPower())
		);
		newRow.put(
			RecordingHeader.System.Weapons.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.WEAPONS).getDamagedBars())
		);
		for (int i = 0; i < DataParser.getWeaponSlotCount(); i++) {
			try {
				newRow.put(
					String.format(RecordingHeader.System.Weapons.SLOT, i + 1),
					FTLAdventureVisualiser.shipState.getWeaponList().get(i).getWeaponId().replaceAll("_"," ")
				);
			} catch (IndexOutOfBoundsException e) {}
		}
		newRow.put(
			RecordingHeader.System.DroneControl.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.DroneControl.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getPower())
		);
		newRow.put(
			RecordingHeader.System.DroneControl.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DRONE_CTRL).getDamagedBars())
		);
		for (int i = 0; i < DataParser.getDroneSlotCount(); i++) {
			try {
				newRow.put(
					String.format(RecordingHeader.System.DroneControl.SLOT, i + 1),
					FTLAdventureVisualiser.shipState.getDroneList().get(i).getDroneId().replaceAll("_"," ")
				);
			} catch (IndexOutOfBoundsException e) {}
		}
		newRow.put(
			RecordingHeader.System.MedBay.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.MedBay.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getPower())
		);
		newRow.put(
			RecordingHeader.System.MedBay.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MEDBAY).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Teleporter.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Teleporter.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getPower())
		);
		newRow.put(
			RecordingHeader.System.Teleporter.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.TELEPORTER).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Cloaking.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Cloaking.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getPower())
		);
		newRow.put(
			RecordingHeader.System.Cloaking.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLOAKING).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Artillery.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Artillery.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getPower())
		);
		newRow.put(
			RecordingHeader.System.Artillery.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.ARTILLERY).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.CloneBay.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.CloneBay.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getPower())
		);
		newRow.put(
			RecordingHeader.System.CloneBay.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.CLONEBAY).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.MindControl.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.MindControl.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getPower())
		);
		newRow.put(
			RecordingHeader.System.MindControl.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.MIND).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Hacking.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Hacking.POWER,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getPower())
		);
		newRow.put(
			RecordingHeader.System.Hacking.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.HACKING).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Pilot.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Pilot.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.PILOT).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Sensors.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Sensors.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.SENSORS).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Doors.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Doors.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.DOORS).getDamagedBars())
		);
		newRow.put(
			RecordingHeader.System.Battery.CAPACITY,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getCapacity())
		);
		newRow.put(
			RecordingHeader.System.Battery.DAMAGE,
			Integer.toString(FTLAdventureVisualiser.shipState.getSystem(SavedGameParser.SystemType.BATTERY).getDamagedBars())
		);

		// crew
		// TODO keep track of which crewmember is which to prevent them shifting collumns
		for (int i = 0; i < FTLAdventureVisualiser.gameState.getTotalCrewHired(); i++) {
			try {
				newRow.put(
					String.format(RecordingHeader.CrewMember.NAME, i + 1),
					FTLAdventureVisualiser.playerCrewState.get(i).getName()
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.SPECIES, i + 1),
					DataParser.getFullCrewType(i)
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.HEALTH, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getHealth())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.PILOT_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getPilotSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.ENGINE_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getEngineSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.SHIELD_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getShieldSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.WEAPON_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getWeaponSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.REPAIR_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getRepairSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.COMBAT_SKILL, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getCombatSkill())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.REPAIRS, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getRepairs())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.COMBAT_KILLS, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getCombatKills())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.PILOTED_EVASIONS, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getPilotedEvasions())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.JUMPS_SURVIVED, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getJumpsSurvived())
				);
				newRow.put(
					String.format(RecordingHeader.CrewMember.SKILLS_MASTERED, i + 1),
					Integer.toString(FTLAdventureVisualiser.playerCrewState.get(i).getSkillMasteries())
				);
			} catch (IndexOutOfBoundsException e) {}
		}

		if (!FTLAdventureVisualiser.recording.contains(newRow)) {
			FTLAdventureVisualiser.recording.add(newRow);
		}
		String[] header = newRow.keySet().toArray(new String[newRow.size()]);

		// TODO prevent duplicate rows by casting FTLAdventureVisualiser.recording to a Hashset and back again

		ICsvMapWriter mapWriter = null;
		try {
			mapWriter = new CsvMapWriter(new FileWriter(fileName), FTLCSV);

			// writer header
			mapWriter.writeHeader(header);

			// write all entries
			for (int i = 0; i < FTLAdventureVisualiser.recording.size(); i++) {
				mapWriter.write(FTLAdventureVisualiser.recording.get(i), header);
			}
		} catch (Exception e) {
			log.error("Something went wrong while writing " + fileName, e);
		} finally {
			try {
				assert mapWriter != null;
				mapWriter.close();
			} catch (IOException ex) {
				log.error("Something went wrong closing fileWriter", ex);
			}
		}

	}

}