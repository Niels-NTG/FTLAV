package net.ntg.ftl.ui.graph;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.TogglePanel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class GraphInspector extends JToolBar {

	private static final Logger log = LogManager.getLogger(GraphInspector.class);

	private final FTLFrame frame;

	// Graph Settings
	private TogglePanel graphSettings = null;
	private static final String SHOWTITLE = "Display Title";
	// private static String SHOWCREW  = "Display Crew"; // TODO toggle crew labels on/off

	// Ship Log
	private TogglePanel shipLogPanel = null;
	private static final String TOTAL_DEFEATED	= "Total Ships Defeated";
	private static final String TOTAL_BEACONS	= "Total Beacons Explored";
	private static final String TOTAL_SCRAP		= "Total Scrap Collected";
	private static final String TOTAL_CREW		= "Total Crew Hired";
	private static final String FLEET_ADVANCE	= "Fleet Advancement";
	private static final String SCORE			= "Score";

	// Ship Supplies
	private TogglePanel suppliesPanel = null;
	private static final String SCRAP			= "Scrap";
	private static final String HULL			= "Hull";
	private static final String FUEL			= "Fuel";
	private static final String DRONE_PARTS		= "Drone Parts";
	private static final String MISSILES		= "Missiles";
	private static final String CREW_SIZE		= "Crew Size";
	private static final String CARGO_SIZE		= "Cargo Size";
	private static final String OXYGEN_LEVEL	= "Oxygen Level";


	public GraphInspector(FTLFrame frame) {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setFloatable(false);

		this.frame = frame;

	}


	public void setGameState() {

		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		if (latest == 0) {

			// Graph Settings
			graphSettings = new TogglePanel();
			graphSettings.setBorder(BorderFactory.createTitledBorder("Graph Settings"));
			graphSettings.addToggle(SHOWTITLE, true);
			this.add(graphSettings);

			// Ship Log
			shipLogPanel = new TogglePanel();
			shipLogPanel.setBorder(BorderFactory.createTitledBorder("Graph Ship Log"));
			shipLogPanel.addToggle(TOTAL_DEFEATED, false);
			shipLogPanel.addToggle(TOTAL_BEACONS, false);
			shipLogPanel.addToggle(TOTAL_SCRAP, false);
			shipLogPanel.addToggle(TOTAL_CREW, false);
			shipLogPanel.addToggle(FLEET_ADVANCE, false);
			shipLogPanel.addToggle(SCORE, false);
			this.add(shipLogPanel);

			// Ship Supplies
			suppliesPanel = new TogglePanel();
			suppliesPanel.setBorder(BorderFactory.createTitledBorder("Graph Ship Supplies"));
			suppliesPanel.addToggle(SCRAP, false);
			suppliesPanel.addToggle(HULL, false);
			suppliesPanel.addToggle(FUEL, false);
			suppliesPanel.addToggle(DRONE_PARTS, false);
			suppliesPanel.addToggle(MISSILES, false);
			suppliesPanel.addToggle(CREW_SIZE, false);
			suppliesPanel.addToggle(OXYGEN_LEVEL, false);
			this.add(suppliesPanel);

			((FTLFrame)suppliesPanel.getTopLevelAncestor()).pack();
		}


		// Graph Settings
		GraphRenderer.showTitle = graphSettings.getState(SHOWTITLE).isSelected();


		GraphRenderer.superArray.clear();


		// Ship Log
		shipLogPanel.setValue(TOTAL_DEFEATED, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalShipsDefeated());
		shipLogPanel.setValue(TOTAL_BEACONS, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalBeaconsExplored());
		shipLogPanel.setValue(TOTAL_SCRAP, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalScrapCollected());
		shipLogPanel.setValue(TOTAL_CREW, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired());
		shipLogPanel.setValue(FLEET_ADVANCE, ShipDataParser.getRebelFleetAdvancement(latest));
		shipLogPanel.setValue(SCORE, ShipDataParser.getCurrentScore(latest));

		if (shipLogPanel.getState(TOTAL_DEFEATED).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalShipsDefeated());
			}
			GraphRenderer.superArray.put(TOTAL_DEFEATED, intArray);
		}

		if (shipLogPanel.getState(TOTAL_BEACONS).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalBeaconsExplored());
			}
			GraphRenderer.superArray.put(TOTAL_BEACONS, intArray);
		}

		if (shipLogPanel.getState(TOTAL_SCRAP).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalScrapCollected());
			}
			GraphRenderer.superArray.put(TOTAL_SCRAP, intArray);
		}

		if (shipLogPanel.getState(TOTAL_CREW).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalCrewHired());
			}
			GraphRenderer.superArray.put(TOTAL_CREW, intArray);
		}

		if (shipLogPanel.getState(FLEET_ADVANCE).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(ShipDataParser.getRebelFleetAdvancement(i));
			}
			GraphRenderer.superArray.put(FLEET_ADVANCE, intArray);
		}

		if (shipLogPanel.getState(SCORE).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(ShipDataParser.getCurrentScore(i));
			}
			GraphRenderer.superArray.put(SCORE, intArray);
		}


		// Ship Supplies
		suppliesPanel.setValue(SCRAP, FTLAdventureVisualiser.shipStateArray.get(latest).getScrapAmt());
		suppliesPanel.setValue(HULL, FTLAdventureVisualiser.shipStateArray.get(latest).getHullAmt());
		suppliesPanel.setValue(FUEL, FTLAdventureVisualiser.shipStateArray.get(latest).getFuelAmt());
		suppliesPanel.setValue(DRONE_PARTS, FTLAdventureVisualiser.shipStateArray.get(latest).getDronePartsAmt());
		suppliesPanel.setValue(MISSILES, FTLAdventureVisualiser.shipStateArray.get(latest).getMissilesAmt());
		suppliesPanel.setValue(CREW_SIZE, FTLAdventureVisualiser.playerCrewArray.get(latest).size());
		suppliesPanel.setValue(OXYGEN_LEVEL, ShipDataParser.getShipOxygenLevel(latest));

		if (suppliesPanel.getState(SCRAP).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getScrapAmt());
			}
			GraphRenderer.superArray.put(SCRAP, intArray);
		}

		if (suppliesPanel.getState(HULL).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getHullAmt());
			}
			GraphRenderer.superArray.put(HULL, intArray);
		}

		if (suppliesPanel.getState(FUEL).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getFuelAmt());
			}
			GraphRenderer.superArray.put(FUEL, intArray);
		}

		if (suppliesPanel.getState(DRONE_PARTS).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getDronePartsAmt());
			}
			GraphRenderer.superArray.put(DRONE_PARTS, intArray);
		}

		if (suppliesPanel.getState(MISSILES).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getMissilesAmt());
			}
			GraphRenderer.superArray.put(MISSILES, intArray);
		}

		if (suppliesPanel.getState(CREW_SIZE).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.playerCrewArray.get(i).size());
			}
			GraphRenderer.superArray.put(CREW_SIZE, intArray);
		}

		if (suppliesPanel.getState(OXYGEN_LEVEL).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(ShipDataParser.getShipOxygenLevel(i));
			}
			GraphRenderer.superArray.put(OXYGEN_LEVEL, intArray);
		}


		// calculate ceiling value
		ArrayList<Integer> ceilingMeasureArray = new ArrayList<>();
		for (int i = 0; i < GraphRenderer.superArray.size(); i++) {
			ceilingMeasureArray.add(
				Collections.max(new ArrayList<ArrayList<Integer>>(GraphRenderer.superArray.values()).get(i))
			);
		}
		GraphRenderer.ceiling = ceilingMeasureArray.isEmpty() || Collections.max(ceilingMeasureArray) <= 20 ?
			20 : Collections.max(ceilingMeasureArray);

		log.info("superArray size " + GraphRenderer.superArray.size());
		log.info("superArray:\n" + GraphRenderer.superArray);
		log.info("Ceiling " + GraphRenderer.ceiling);

	}

}