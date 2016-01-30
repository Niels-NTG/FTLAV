package net.ntg.ftl.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;

import net.blerf.ftl.parser.SavedGameParser;


public class CreateCSV {

	private static Logger log = LogManager.getLogger(CreateCSV.class);

	private static final String DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";


	public static void writeCSV (String fileName) {

		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		int maxNearbyShipWeaponListSize = 0;
		int maxNearbyShipDroneListSize  = 0;
		int maxEnemyCrewSize = 0;

		ArrayList<Integer> nearbyShipWeaponListSizes = new ArrayList<Integer>();
		for (int i = 0; i <= latest; i++) {
			if (FTLAdventureVisualiser.nearbyShipStateArray.get(i) != null) {
				nearbyShipWeaponListSizes.add(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getWeaponList().size());
			}
		}
		if (!nearbyShipWeaponListSizes.isEmpty()) {
			maxNearbyShipWeaponListSize = Collections.max(nearbyShipWeaponListSizes);
		}

		ArrayList<Integer> nearbyShipDroneListSizes = new ArrayList<Integer>();
		for (int i = 0; i <= latest; i++) {
			if (FTLAdventureVisualiser.nearbyShipStateArray.get(i) != null) {
				nearbyShipDroneListSizes.add(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getDroneList().size());
			}
		}
		if (!nearbyShipDroneListSizes.isEmpty()) {
			maxNearbyShipDroneListSize = Collections.max(nearbyShipDroneListSizes);
		}

		ArrayList<Integer> enemyCrewSizes = new ArrayList<Integer>();
		for (int i = 0; i <= latest; i++) {
			enemyCrewSizes.add(FTLAdventureVisualiser.enemyCrewArray.get(i).size());
		}
		if (!enemyCrewSizes.isEmpty()) {
			maxEnemyCrewSize = Collections.max(enemyCrewSizes);
		}

		String fileHeader = (
		// Location
			"BEACON,"+
			"SECTOR NUMBER,"+
			"SECTOR TYPE,"+
			"SECTOR TITLE,"+
			"FLEET ADVANCEMENT,"+
		// LOG
			"TOTAL SHIPS DEFEATED,"+
			"TOTAL SCRAP COLLECTED,"+
			"TOTAL CREW HIRED,"+
			"SCORE,"+
		// Encounter
			"HAZARDS,"+
			"EVENT TEXT,"+
			"STORE,"+
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
		for (int i = 0; i < ShipDataParser.getWeaponSlotCount(); i++) fileHeader += "WEAPON SLOT " + (i+1) + ",";
		fileHeader += (
			"DRONE CONTROL SYSTEM CAPACITY,"+
			"DRONE CONTROL SYSTEM POWER,"+
			"DRONE CONTROL SYSTEM DAMAGE,"
		);
		for (int i = 0; i < ShipDataParser.getDroneSlotCount(); i++) fileHeader += "DRONE SLOT " + (i+1) + ",";
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
		// Augments
			"AUGMENTS,"+
		// Cargo
			"CARGO,"
		);
		// Crew
		for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired(); i++) {
			fileHeader += ("CREW MEMBER " + (i+1) + " NAME,");
			fileHeader += ("CREW MEMBER " + (i+1) + " SPECIES,");
			fileHeader += ("CREW MEMBER " + (i+1) + " HEALTH,");
			fileHeader += ("CREW MEMBER " + (i+1) + " PILOT SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " ENGINE SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " SHIELD SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " WEAPON SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " REPAIR SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " COMBAT SKILL,");
			fileHeader += ("CREW MEMBER " + (i+1) + " REPAIRS,");
			fileHeader += ("CREW MEMBER " + (i+1) + " COMBAT KILLS,");
			fileHeader += ("CREW MEMBER " + (i+1) + " PILOTED EVASIONS,");
			fileHeader += ("CREW MEMBER " + (i+1) + " JUMPS SURVIVED,");
			fileHeader += ("CREW MEMBER " + (i+1) + " SKILLS MASTERED,");
		}
		// Nearby Ship
		fileHeader += (
			"NEARBY SHIP TYPE,"+
		// Nearby Ship Supplies
			"NEARBY SHIP HULL,"+
			"NEARBY SHIP FUEL,"+
			"NEARBY SHIP DRONE PARTS,"+
			"NEARBY SHIP MISSILES,"+
			"NEARBY SHIP CREW SIZE,"+
			"OXYGEN LEVEL,"+
		// Nearby Ship Systems
			"NEARBY SHIP POWER CAPACITY,"+
			"NEARBY SHIP SHIELDS CAPACITY,"+
			"NEARBY SHIP SHIELD POWER,"+
			"NEARBY SHIP SHIELD DAMAGE,"+
			"NEARBY SHIP ENGINES CAPACITY,"+
			"NEARBY SHIP ENGINES POWER,"+
			"NEARBY SHIP ENGINES DAMAGE,"+
			"NEARBY SHIP OXYGEN SYSTEM CAPACITY,"+
			"NEARBY SHIP OXYGEN SYSTEM POWER,"+
			"NEARBY SHIP OXYGEN SYSTEM DAMAGE,"+
			"NEARBY SHIP WEAPONS SYSTEM CAPACITY,"+
			"NEARBY SHIP WEAPONS SYSTEM POWER,"+
			"NEARBY SHIP WEAPONS SYSTEM DAMAGE,"
		);
		for (int i = 0; i < maxNearbyShipWeaponListSize; i++) fileHeader += ("NEARBY SHIP WEAPON SLOT " + (i+1) + ",");
		fileHeader += (
			"NEARBY SHIP DRONE CONTROL SYSTEM CAPACITY,"+
			"NEARBY SHIP DRONE CONTROL SYSTEM POWER,"+
			"NEARBY SHIP DRONE CONTROL SYSTEM DAMAGE,"
		);
		for (int i = 0; i < maxNearbyShipDroneListSize; i++) fileHeader += ("DRONE SLOT " + (i+1) + ",");
		fileHeader += (
			"NEARBY SHIP MEDBAY SYSTEM CAPACITY,"+
			"NEARBY SHIP MEDBAY SYSTEM POWER,"+
			"NEARBY SHIP MEDBAY SYSTEM DAMAGE,"+
			"NEARBY SHIP TELEPORTER SYSTEM CAPACITY,"+
			"NEARBY SHIP TELEPORTER SYSTEM POWER,"+
			"NEARBY SHIP TELEPORTER SYSTEM DAMAGE,"+
			"NEARBY SHIP CLOAKING SYSTEM CAPACITY,"+
			"NEARBY SHIP CLOAKING SYSTEM POWER,"+
			"NEARBY SHIP CLOAKING SYSTEM DAMAGE,"+
			"NEARBY SHIP ARTILLERY SYSTEM CAPACITY,"+
			"NEARBY SHIP ARTILLERY SYSTEM POWER,"+
			"NEARBY SHIP ARTILLERY SYSTEM DAMAGE,"+
			"NEARBY SHIP CLONEBAY SYSTEM CAPACITY,"+
			"NEARBY SHIP CLONEBAY SYSTEM POWER,"+
			"NEARBY SHIP CLONEBAY SYSTEM DAMAGE,"+
			"NEARBY SHIP MINDCONTROL SYSTEM CAPACITY,"+
			"NEARBY SHIP MINDCONTROL SYSTEM POWER,"+
			"NEARBY SHIP MINDCONTROL SYSTEM DAMAGE,"+
			"NEARBY SHIP HACKING SYSTEM CAPACITY,"+
			"NEARBY SHIP HACKING SYSTEM POWER,"+
			"NEARBY SHIP HACKING SYSTEM DAMAGE,"+
			"NEARBY SHIP PILOT SYTEM CAPACITY,"+
			"NEARBY SHIP PILOT SYSTEM DAMAGE,"+
			"NEARBY SHIP SENSORS SYSTEM CAPACITY,"+
			"NEARBY SHIP SENSORS SYSTEM DAMAGE,"+
			"NEARBY SHIP DOORS SYSTEM CAPACITY,"+
			"NEARBY SHIP DOORS SYSTEM DAMAGE,"+
			"NEARBY SHIP BATTERY SYSTEM CAPACITY,"+
			"NEARBY SHIP BATTERY SYSTEM DAMAGE,"+
		// Nearby Ship Augments
			"NEARBY SHIP AUGMENTS,"
		);
		// Enemy Crew
		for (int i = 0; i < maxEnemyCrewSize; i++) {
			fileHeader += ("ENEMY CREW MEMBER " + (i+1) + " NAME,");
			fileHeader += ("ENEMY CREW MEMBER " + (i+1) + " SPECIES,");
			fileHeader += ("ENEMY CREW MEMBER " + (i+1) + " HEALTH,");
			fileHeader += ("CREW MEMBER " + (i+1) + " REPAIRS,");
			fileHeader += ("CREW MEMBER " + (i+1) + " COMBAT KILLS,");
		}
		// Nearby Ship AI
		fileHeader += (
			"HAS SURRENDERED,"+
			"HULL DAMAGE NEEDED FOR SURRENDER,"+
			"IS TRYING TO ESCAPE,"+
			"HULL DAMAGE NEEDED FOR ESCAPE ATTEMPT,"+
			"ENEMY BOARDING ATTEMPTS,"+
			"ENEMY BOARDING STRENGTH"
		);


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

			fw.append(fileHeader);
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
					fw.append(DELIMITER);
				}
				if (FTLAdventureVisualiser.nearbyShipStateArray.get(i) != null) {
					fw.append(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getShipBlueprintId());
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getHullAmt()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getFuelAmt()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getDronePartsAmt()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getMissilesAmt()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.enemyCrewArray.get(i).size()));
					fw.append(DELIMITER);
					fw.append(ShipDataParser.getNearbyShipOxygenLevel(i) != -1 ? Integer.toString(ShipDataParser.getNearbyShipOxygenLevel(i)) : "");
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getReservePowerCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.SHIELDS).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ENGINES).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.OXYGEN).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.WEAPONS).getDamagedBars()));
					fw.append(DELIMITER);
					for (int w = 0; w < maxNearbyShipWeaponListSize; w++) {
						try {
							fw.append(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getWeaponList().get(w).getWeaponId().replaceAll("_"," "));
						} catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
					}
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.DRONE_CTRL).getDamagedBars()));
					fw.append(DELIMITER);
					for (int d = 0; d < maxNearbyShipDroneListSize; d++) {
						try {
							fw.append(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getDroneList().get(d).getDroneId().replaceAll("_"," "));
						} catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
					}
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MEDBAY).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.TELEPORTER).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLOAKING).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.ARTILLERY).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.CLONEBAY).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.MIND).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getPower()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.HACKING).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.PILOT).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.PILOT).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.SENSORS).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.SENSORS).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.DOORS).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.DOORS).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.BATTERY).getCapacity()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.nearbyShipStateArray.get(i).getSystem(SavedGameParser.SystemType.BATTERY).getDamagedBars()));
					fw.append(DELIMITER);
					fw.append("\"" + ShipDataParser.getNearbyShipAugmentListing(i) + "\"");
					fw.append(DELIMITER);
					for (int c = 0; c < maxEnemyCrewSize; c++) {
						try { fw.append(FTLAdventureVisualiser.enemyCrewArray.get(i).get(c).getName()); } catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
						try { fw.append(ShipDataParser.getFullEnemyCrewType(i, c)); } catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
						try { fw.append(Integer.toString(FTLAdventureVisualiser.enemyCrewArray.get(i).get(c).getHealth())); } catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
						try { fw.append(Integer.toString(FTLAdventureVisualiser.enemyCrewArray.get(i).get(c).getRepairs())); } catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
						try { fw.append(Integer.toString(FTLAdventureVisualiser.enemyCrewArray.get(i).get(c).getCombatKills())); } catch (IndexOutOfBoundsException e) {}
						fw.append(DELIMITER);
					}
					fw.append(Boolean.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().hasSurrendered()));
					fw.append(DELIMITER);
					fw.append(
						FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getSurrenderThreshold() <= 0 ?
						"" :
						Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getSurrenderThreshold())
					);
					fw.append(DELIMITER);
					fw.append(Boolean.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().isEscaping()));
					fw.append(DELIMITER);
					fw.append(
						FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getEscapeThreshold() <= 0 ?
						"" :
						Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getEscapeThreshold())
					);
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getBoardingAttempts()));
					fw.append(DELIMITER);
					fw.append(Integer.toString(FTLAdventureVisualiser.gameStateArray.get(i).getNearbyShipAI().getBoardersNeeded()));
				}
				fw.append(NEW_LINE_SEPARATOR);

			}

			log.info("CSV file has been created");
			log.info(FTLAdventureVisualiser.gameStateArray.get(latest).getRebelFleetOffset() + " " + FTLAdventureVisualiser.gameStateArray.get(latest).getRebelFleetFudge());

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