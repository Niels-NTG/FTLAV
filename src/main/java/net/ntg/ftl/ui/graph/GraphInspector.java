package net.ntg.ftl.ui.graph;

import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.swing.JToolBar;
import javax.swing.BorderFactory;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.TogglePanel;
import net.ntg.ftl.ui.graph.GraphRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphInspector extends JToolBar {

	private static final Logger log = LogManager.getLogger(GraphInspector.class);

	private FTLFrame frame;

	// Ship Log
	// private TogglePanel shipLogPanel = null;
	// Ship Name:              Mulsanne (as title)
	// Ship Type:              PLAYER_SHIP_FED (as title)
	// Difficulty:             EASY (as title)
	// Game start date-time (get savefile data of creation)
	// Game current date-time (get savefile last modified)
	//
	// bar graph per sector
	// Total Ships Defeated:       0 (per sector)
	// Total Beacons Explored:     1 (per sector)
	// Total Scrap Collected:      0 (per sector)
	// Total Crew Hired:           4 (per sector)

	// Ship Supplies
	private TogglePanel suppliesPanel = null;
	private static final String SCRAP 		= "Scrap";
	private static final String HULL 		= "Hull";
	private static final String FUEL 		= "Fuel";
	private static final String DRONE_PARTS = "Drone Parts";
	private static final String MISSILES 	= "Missiles";
	private static final String CREW_SIZE	= "Crew Size";
	private static final String CARGO_SIZE  = "Cargo Size";
	private static final String OXYGEN_LEVEL= "Oxygen Level";

	// Crewmembers
	// private TogglePanel crewPanel = null;


	public GraphInspector (FTLFrame frame) {

		// TODO align toolbar at top. Make it a collapable window or seperate window (toolbar?)
		// TODO make graph take up whole window with toolbar on top

		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.frame = frame;

		// Ship Log
		// shipLogPanel = new TogglePanel();
		// shipLogPanel.setBorder(BorderFactory.createTitledBorder("Ship Log"));
		// this.add(shipLogPanel);

		// Ship Supplies
		suppliesPanel = new TogglePanel();
		suppliesPanel.setBorder(BorderFactory.createTitledBorder("Supplies"));
		suppliesPanel.addToggle(SCRAP, false);
		suppliesPanel.addToggle(HULL, false);
		suppliesPanel.addToggle(FUEL, false);
		suppliesPanel.addToggle(DRONE_PARTS, false);
		suppliesPanel.addToggle(MISSILES, false);
		suppliesPanel.addToggle(CREW_SIZE, false);
		suppliesPanel.addToggle(OXYGEN_LEVEL, false);
		this.add(suppliesPanel);

		// Crewmembers (add a seperate panel for each crew member)
		// crewPanel = new TogglePanel();
		// crewPanel.setBorder(BorderFactory.createTitledBorder("Crewmember"));

	}


	public void setGameState() {
		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		// TODO get value labels directly after loading savegame

		// Ship Log
		log.info("Score : " + ShipDataParser.getCurrentScore(latest));

		// Ship Supplies
		suppliesPanel.setValue(SCRAP, FTLAdventureVisualiser.shipStateArray.get(latest).getScrapAmt());
		suppliesPanel.setValue(HULL, FTLAdventureVisualiser.shipStateArray.get(latest).getHullAmt());
		suppliesPanel.setValue(FUEL, FTLAdventureVisualiser.shipStateArray.get(latest).getFuelAmt());
		suppliesPanel.setValue(DRONE_PARTS, FTLAdventureVisualiser.shipStateArray.get(latest).getDronePartsAmt());
		suppliesPanel.setValue(MISSILES, FTLAdventureVisualiser.shipStateArray.get(latest).getMissilesAmt());
		suppliesPanel.setValue(CREW_SIZE, ShipDataParser.getPlayerCrewSize(latest));
		suppliesPanel.setValue(OXYGEN_LEVEL, ShipDataParser.getShipOxygenLevel(latest));

		// Crewmembers


		if (FTLAdventureVisualiser.gameStateArray.size() >= 0) {

			GraphRenderer.superArray.clear();

			// Ship Log

			// Ship Supplies
			if (suppliesPanel.getState(SCRAP).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getScrapAmt());
				}
				GraphRenderer.superArray.put(SCRAP,intArray);
			}

			if (suppliesPanel.getState(HULL).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getHullAmt());
				}
				GraphRenderer.superArray.put(HULL, intArray);
			}

			if (suppliesPanel.getState(FUEL).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getFuelAmt());
				}
				GraphRenderer.superArray.put(FUEL, intArray);
			}

			if (suppliesPanel.getState(DRONE_PARTS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getDronePartsAmt());
				}
				GraphRenderer.superArray.put(DRONE_PARTS, intArray);
			}

			if (suppliesPanel.getState(MISSILES).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getMissilesAmt());
				}
				GraphRenderer.superArray.put(MISSILES, intArray);
			}

			if (suppliesPanel.getState(CREW_SIZE).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(ShipDataParser.getPlayerCrewSize(i));
				}
				GraphRenderer.superArray.put(CREW_SIZE, intArray);
			}

			if (suppliesPanel.getState(OXYGEN_LEVEL).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(ShipDataParser.getShipOxygenLevel(i));
				}
				GraphRenderer.superArray.put(OXYGEN_LEVEL, intArray);
			}

			// Crewmembers

			// calculate ceiling value
			ArrayList<Integer> ceilingMeasureArray = new ArrayList<Integer>();
			for (int i = 0; i < GraphRenderer.superArray.size(); i++) {
				ceilingMeasureArray.add(
					Collections.max(
						new ArrayList<ArrayList<Integer>>(
							GraphRenderer.superArray.values()
						).get(i)
					)
				);
			}
			GraphRenderer.ceiling = ceilingMeasureArray.isEmpty() || Collections.max(ceilingMeasureArray) <= 20 ?
				20 : Collections.max(ceilingMeasureArray);

			log.info("superArray size " + GraphRenderer.superArray.size());
			log.info("superArray " + GraphRenderer.superArray);
			log.info("Ceiling " + GraphRenderer.ceiling);

		}
	}
}