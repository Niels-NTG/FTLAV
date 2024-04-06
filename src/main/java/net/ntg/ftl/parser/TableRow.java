package net.ntg.ftl.parser;

import lombok.Data;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.model.state.ShipState;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

@Data
public class TableRow {

	// Non-temporal
	private String time;
	private String shipName;
	private String shipType;
	private Difficulty difficulty;
	private boolean AEContentEnabled;

	// Location
	private int beaconNumber;
	private int sectorNumber;
	private String sectorType;
	private String sectorName;
	private int fleetAdvancement;

	// Encounter
	private ArrayList<String> beaconHazards;
	private String encounterText;

	// Resources totals
	private int totalShipsDefeated;
	private int totalScrapCollected;
	private int totalCrewHired;
	private int totalScore;

	// Resources current
	private int scrap;
	private int fuel;
	private int missles;
	private int droneParts;
	private int crewSize;
	private int hull;
	private int oxygenLevel;

	// Systems current

	public TableRow(SavedGameState gameState, String timeStamp) {

		ShipState playerShip = gameState.getPlayerShip();

		// Non-temporal
		time = timeStamp;
		shipName = gameState.getPlayerShipName();
		shipType = DataUtil.getFullShipType(gameState);
		difficulty = gameState.getDifficulty();
		AEContentEnabled = gameState.isDLCEnabled();

		// Location
		beaconNumber = gameState.getTotalBeaconsExplored();
		sectorNumber = gameState.getSectorNumber() + 1;
		sectorType = DataUtil.getSector(gameState).getType();
		sectorName = DataUtil.getSector(gameState).getTitle();
		fleetAdvancement = DataUtil.getRebelFleetAdvancement(gameState);

		// Encounter
		beaconHazards = DataUtil.getBeaconHazards(gameState);
		encounterText = gameState.getEncounter().getText();

		// Resource totals
		totalShipsDefeated = gameState.getTotalShipsDefeated();
		totalScrapCollected = gameState.getTotalScrapCollected();
		totalCrewHired = gameState.getTotalCrewHired();
		totalScore = gameState.calculateScore();

		// Resources current
		scrap = playerShip.getScrapAmt();
		fuel = playerShip.getFuelAmt();
		missles = playerShip.getMissilesAmt();
		droneParts = playerShip.getDronePartsAmt();
		crewSize = playerShip.getCrewList().size();
		hull = playerShip.getHullAmt();
		oxygenLevel = DataUtil.getShipOxygenLevel(playerShip);

		// Systems current
	}

	public String[] getFieldNames() {
		return getFieldNames(null);
	}

	public String[] getFieldNames(Type type) {
		ArrayList<String> privateFields = new ArrayList<>();
		Field[] fields = this.getClass().getDeclaredFields();
		boolean hasNoTypeCheck = type == null;
		for (Field field : fields) {
			if (hasNoTypeCheck || field.getGenericType() == type) {
				privateFields.add(field.getName());
			}
		}
		return privateFields.toArray(new String[privateFields.size()]);
	}

	public Object getFieldValue(String fieldName) {
		try {
			return this.getClass().getDeclaredField(fieldName).get(this);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			return null;
		}
	}

}
