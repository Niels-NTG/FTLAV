package net.ntg.ftl.ui.graph;

import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.graph.GraphRenderer;
import net.ntg.ftl.ui.TogglePanel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class GraphInspector extends JToolBar {

	private static final Logger log = LogManager.getLogger(GraphInspector.class);

	private FTLFrame frame;

	private FlowLayout flowLayout;

	// Graph Settings
	private TogglePanel graphSettings = null;
	private static final String SHOWTITLE = "Display Title";
	private static final String SHOWCREW  = "Display Crew"; // TODO toggle crew labels on/off
	// Game start date-time (get savefile data of creation)
	// Game current date-time (get savefile last modified)

	// Ship Log
	// private TogglePanel shipLogPanel = null;
	//
	// TODO bar graph per sector
	// Total Ships Defeated:       0 (per sector)
	// Total Beacons Explored:     1 (per sector)
	// Total Scrap Collected:      0 (per sector)
	// Total Crew Hired:           4 (per sector)
	// Distance to rebel fleet relative to player

	// Ship Supplies
	private TogglePanel suppliesPanel = null;
	private static final String SCRAP 		= "Scrap";
	private static final String HULL 		= "Hull";
	private static final String FUEL 		= "Fuel";
	private static final String DRONE_PARTS = "Drone Parts";
	private static final String MISSILES 	= "Missiles";
	private static final String CREW_SIZE	= "Crew Size"; private int lastCrewSize = 0;
	private static final String CARGO_SIZE  = "Cargo Size";
	private static final String OXYGEN_LEVEL= "Oxygen Level";

	// Crewmembers
	// TODO horizontal scrolling so all panels fit smaller screens
	// TODO make it more compact by subsituting text with icons in a grid. New method in TogglePanel to make buttons that fit next to each other
	// TODO take a look at field-editor panel class from UI package to see how this is done
	private ArrayList<TogglePanel> crewPanelArray = new ArrayList<TogglePanel>();
	private static final String CREW_NAME         = "Name";
	private static final String CREW_RACE         = "Kind"; // battle = Anti Personel Drone, energy = Zoltan, anaerobic = Lanius
	private static final String CREW_HEALTH       = "Health"; // part of getMaxHealth()
	private static final String CREW_SKILL_PILOT  = "Pilot Skill";
	private static final String CREW_SKILL_ENGINE = "Engine Skill";
	private static final String CREW_SKILL_SHIELD = "Shield Skill";
	private static final String CREW_SKILL_WEAPON = "Weapon Skill";
	private static final String CREW_SKILL_REPAIR = "Repair Skill";
	private static final String CREW_SKILL_COMBAT = "Combat Skill";
	private static final String CREW_REPAIRS      = "Total Repairs";         // TODO bar graph per sector instead of line graph
	private static final String CREW_KILLS        = "Total Combat Kills";	 // TODO bar graph per sector instead of line graph
	private static final String CREW_EVASIONS     = "Total Piloted Evasions";// TODO bar graph per sector instead of line graph
	private static final String CREW_JUMPS        = "Total Jumps Survided";	 // TODO bar graph per sector instead of line graph
	private static final String CREW_SKILLS       = "Skills Mastered";       // TODO bar graph per sector instead of line graph

	// icons
	private static final ImageIcon CREW_HEALTH_ICON       = new ImageIcon(ClassLoader.getSystemResource("crew-health.gif"));
	private static final ImageIcon CREW_SKILL_PILOT_ICON  = new ImageIcon(ClassLoader.getSystemResource("crew-pilot.gif"));
	private static final ImageIcon CREW_SKILL_ENGINE_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-engine.gif"));
	private static final ImageIcon CREW_SKILL_SHIELD_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-shield.gif"));
	private static final ImageIcon CREW_SKILL_WEAPON_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-weapon.gif"));
	private static final ImageIcon CREW_SKILL_REPAIR_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-repairs.gif"));
	private static final ImageIcon CREW_SKILL_COMBAT_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-combat.gif"));



	public GraphInspector (FTLFrame frame) {

		// TODO change GraphInspector interface to a vertical scrolling BoxLayout
		flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setAlignOnBaseline(true);
		this.setLayout(flowLayout);
		this.setFloatable(false);

		this.frame = frame;

		// Graph Settings
		graphSettings = new TogglePanel();
		graphSettings.setBorder(BorderFactory.createTitledBorder("Graph Settings"));
		graphSettings.addToggle(SHOWTITLE, true);
		this.add(graphSettings);

		// Ship Log
		// shipLogPanel = new TogglePanel();
		// shipLogPanel.setBorder(BorderFactory.createTitledBorder("Ship Log"));
		// this.add(shipLogPanel);

		// Ship Supplies
		suppliesPanel = new TogglePanel();
		suppliesPanel.setBorder(BorderFactory.createTitledBorder("Ship Supplies"));
		suppliesPanel.addToggle(SCRAP, false);
		suppliesPanel.addToggle(HULL, false);
		suppliesPanel.addToggle(FUEL, false);
		suppliesPanel.addToggle(DRONE_PARTS, false);
		suppliesPanel.addToggle(MISSILES, false);
		suppliesPanel.addToggle(CREW_SIZE, false);
		suppliesPanel.addToggle(OXYGEN_LEVEL, false);
		this.add(suppliesPanel);

		// Crewmembers (add a seperate panel for each crew member)


	}


	public void setGameState() {

		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		// Graph Settings
		if (graphSettings.getState(SHOWTITLE).isSelected()) {
			GraphRenderer.showTitle = true;
		} else {
			GraphRenderer.showTitle = false;
		}

		// Ship Log

		// Ship Supplies
		suppliesPanel.setValue(SCRAP, FTLAdventureVisualiser.shipStateArray.get(latest).getScrapAmt());
		suppliesPanel.setValue(HULL, FTLAdventureVisualiser.shipStateArray.get(latest).getHullAmt());
		suppliesPanel.setValue(FUEL, FTLAdventureVisualiser.shipStateArray.get(latest).getFuelAmt());
		suppliesPanel.setValue(DRONE_PARTS, FTLAdventureVisualiser.shipStateArray.get(latest).getDronePartsAmt());
		suppliesPanel.setValue(MISSILES, FTLAdventureVisualiser.shipStateArray.get(latest).getMissilesAmt());
		suppliesPanel.setValue(CREW_SIZE, FTLAdventureVisualiser.playerCrewArray.get(latest).size());
		suppliesPanel.setValue(OXYGEN_LEVEL, ShipDataParser.getShipOxygenLevel(latest));

		// Crewmembers
		for (int i = lastCrewSize; i < FTLAdventureVisualiser.playerCrewArray.get(latest).size(); i++) {

			TogglePanel crewPanel = new TogglePanel();
			crewPanel.setBorder(BorderFactory.createTitledBorder(FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getName()));
			crewPanel.addLabel(CREW_RACE);
			crewPanel.addToggle(CREW_HEALTH, CREW_HEALTH_ICON, false);
			crewPanel.addToggle(CREW_SKILL_PILOT, CREW_SKILL_PILOT_ICON, false);
			crewPanel.addToggle(CREW_SKILL_ENGINE, CREW_SKILL_ENGINE_ICON, false);
			crewPanel.addToggle(CREW_SKILL_SHIELD, CREW_SKILL_SHIELD_ICON, false);
			crewPanel.addToggle(CREW_SKILL_WEAPON, CREW_SKILL_WEAPON_ICON, false);
			crewPanel.addToggle(CREW_SKILL_REPAIR, CREW_SKILL_REPAIR_ICON, false);
			crewPanel.addToggle(CREW_SKILL_COMBAT, CREW_SKILL_COMBAT_ICON, false);
			crewPanel.addToggle(CREW_REPAIRS, false);
			crewPanel.addToggle(CREW_KILLS, false);
			crewPanel.addToggle(CREW_EVASIONS, false);
			crewPanel.addToggle(CREW_JUMPS, false);
			crewPanel.addToggle(CREW_SKILLS, false);
			this.add(crewPanel);

			crewPanelArray.add(crewPanel);

		}
		lastCrewSize = FTLAdventureVisualiser.playerCrewArray.get(latest).size();

		for (int i = 0; i < crewPanelArray.size(); i++) {

			crewPanelArray.get(i).setLabelValue(CREW_RACE, ShipDataParser.getFullCrewType(latest, i));
			crewPanelArray.get(i).setValue(CREW_HEALTH, ShipDataParser.getCrewHealthRatio(latest, i));
			crewPanelArray.get(i).setValue(CREW_SKILL_PILOT, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getPilotSkill());
			crewPanelArray.get(i).setValue(CREW_SKILL_ENGINE, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getEngineSkill());
			crewPanelArray.get(i).setValue(CREW_SKILL_SHIELD, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getShieldSkill());
			crewPanelArray.get(i).setValue(CREW_SKILL_WEAPON, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getWeaponSkill());
			crewPanelArray.get(i).setValue(CREW_SKILL_REPAIR, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getRepairSkill());
			crewPanelArray.get(i).setValue(CREW_SKILL_COMBAT, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getCombatSkill());
			crewPanelArray.get(i).setValue(CREW_REPAIRS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getRepairs());
			crewPanelArray.get(i).setValue(CREW_KILLS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getCombatKills());
			crewPanelArray.get(i).setValue(CREW_EVASIONS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getPilotedEvasions());
			crewPanelArray.get(i).setValue(CREW_JUMPS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getJumpsSurvived());
			crewPanelArray.get(i).setValue(CREW_SKILLS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getSkillMasteries());

			((FTLFrame)crewPanelArray.get(i).getTopLevelAncestor()).pack();
		}


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
				intArray.add(FTLAdventureVisualiser.playerCrewArray.get(i).size());
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
		for (int i = 0; i < crewPanelArray.size(); i++) {

			String crewName = FTLAdventureVisualiser.playerCrewArray.get(latest).get(i).getName();

			if (crewPanelArray.get(i).getState(CREW_HEALTH).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getHealth());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_HEALTH, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_PILOT).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getPilotSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_PILOT, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_ENGINE).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getEngineSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_ENGINE, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_SHIELD).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getShieldSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_SHIELD, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_WEAPON).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getWeaponSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_WEAPON, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_REPAIR).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getRepairSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_REPAIR, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILL_COMBAT).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getCombatSkill());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_COMBAT, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_REPAIRS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getRepairs());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_REPAIRS, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_KILLS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getCombatKills());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_KILLS, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_EVASIONS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getPilotedEvasions());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_EVASIONS, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_JUMPS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getJumpsSurvived());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_JUMPS, intArray);
			}

			if (crewPanelArray.get(i).getState(CREW_SKILLS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int k = 0; k < FTLAdventureVisualiser.gameStateArray.size(); k++) {
					intArray.add(FTLAdventureVisualiser.playerCrewArray.get(k).get(i).getSkillMasteries());
				}
				GraphRenderer.superArray.put(crewName+"_"+CREW_SKILLS, intArray);
			}

		}


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