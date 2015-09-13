package net.ntg.ftl.ui.graph;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GraphicsEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.graph.GraphRenderer;
import net.ntg.ftl.ui.TogglePanel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class GraphInspector extends JToolBar {

	private static Logger log = LogManager.getLogger(GraphInspector.class);

	private FTLFrame frame;

	// Graph Settings
	private TogglePanel graphSettings = null;
	private static String SHOWTITLE = "Display Title";
	// private static String SHOWCREW  = "Display Crew"; // TODO toggle crew labels on/off

	// Ship Log
	private TogglePanel shipLogPanel = null;
	private static String TOTAL_DEFEATED = "Total Ships Defeated";
	private static String TOTAL_BEACONS  = "Total Beacons Explored";
	private static String TOTAL_SCRAP    = "Total Scrap Collected";
	private static String TOTAL_CREW     = "Total Crew Hired";
	// TODO distance to rebel fleet relative to player
	// TODO bar graph per sector

	// Ship Supplies
	private TogglePanel suppliesPanel = null;
	private static String SCRAP 		= "Scrap";
	private static String HULL 		= "Hull";
	private static String FUEL 		= "Fuel";
	private static String DRONE_PARTS = "Drone Parts";
	private static String MISSILES 	= "Missiles";
	private static String CREW_SIZE	= "Crew Size"; private int lastCrewSize = 0;
	private static String CARGO_SIZE  = "Cargo Size";
	private static String OXYGEN_LEVEL= "Oxygen Level";

	// Crewmembers
	// TODO make inspector more compact by subsituting text with icons in a grid. New method in TogglePanel to make buttons that fit next to each other
	private ArrayList<TogglePanel> crewPanelArray = new ArrayList<TogglePanel>();
	private static String CREW_NAME         = "Name";
	private static String CREW_RACE         = "Kind"; // battle = Anti Personel Drone, energy = Zoltan, anaerobic = Lanius
	private static String CREW_HEALTH       = "Health"; // part of getMaxHealth()
	private static String CREW_SKILL_PILOT  = "Pilot Skill";
	private static String CREW_SKILL_ENGINE = "Engine Skill";
	private static String CREW_SKILL_SHIELD = "Shield Skill";
	private static String CREW_SKILL_WEAPON = "Weapon Skill";
	private static String CREW_SKILL_REPAIR = "Repair Skill";
	private static String CREW_SKILL_COMBAT = "Combat Skill";
	private static String CREW_REPAIRS      = "Total Repairs";         // TODO bar graph per sector instead of line graph
	private static String CREW_KILLS        = "Total Combat Kills";	 // TODO bar graph per sector instead of line graph
	private static String CREW_EVASIONS     = "Total Piloted Evasions";// TODO bar graph per sector instead of line graph
	private static String CREW_JUMPS        = "Total Jumps Survided";	 // TODO bar graph per sector instead of line graph
	private static String CREW_SKILLS       = "Skills Mastered";       // TODO bar graph per sector instead of line graph

	// icons
	private static ImageIcon CREW_HEALTH_ICON       = new ImageIcon(ClassLoader.getSystemResource("crew-health.gif"));
	private static ImageIcon CREW_SKILL_PILOT_ICON  = new ImageIcon(ClassLoader.getSystemResource("crew-pilot.gif"));
	private static ImageIcon CREW_SKILL_ENGINE_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-engine.gif"));
	private static ImageIcon CREW_SKILL_SHIELD_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-shield.gif"));
	private static ImageIcon CREW_SKILL_WEAPON_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-weapon.gif"));
	private static ImageIcon CREW_SKILL_REPAIR_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-repair.gif"));
	private static ImageIcon CREW_SKILL_COMBAT_ICON = new ImageIcon(ClassLoader.getSystemResource("crew-combat.gif"));



	public GraphInspector (FTLFrame frame) {

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
			shipLogPanel.setBorder(BorderFactory.createTitledBorder("Ship Log"));
			shipLogPanel.addToggle(TOTAL_DEFEATED, false);
			shipLogPanel.addToggle(TOTAL_BEACONS, false);
			shipLogPanel.addToggle(TOTAL_SCRAP, false);
			shipLogPanel.addToggle(TOTAL_CREW, false);
			this.add(shipLogPanel);

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

			((FTLFrame)suppliesPanel.getTopLevelAncestor()).pack();
		}


		// Graph Settings
		if (graphSettings.getState(SHOWTITLE).isSelected()) {
			GraphRenderer.showTitle = true;
		} else {
			GraphRenderer.showTitle = false;
		}


		GraphRenderer.superArray.clear();


		// Ship Log
		shipLogPanel.setValue(TOTAL_DEFEATED, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalShipsDefeated());
		shipLogPanel.setValue(TOTAL_BEACONS, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalBeaconsExplored());
		shipLogPanel.setValue(TOTAL_SCRAP, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalScrapCollected());
		shipLogPanel.setValue(TOTAL_CREW, FTLAdventureVisualiser.gameStateArray.get(latest).getTotalCrewHired());

		if (shipLogPanel.getState(TOTAL_DEFEATED).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalShipsDefeated());
			}
			GraphRenderer.superArray.put(TOTAL_DEFEATED, intArray);
		}

		if (shipLogPanel.getState(TOTAL_BEACONS).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalBeaconsExplored());
			}
			GraphRenderer.superArray.put(TOTAL_BEACONS, intArray);
		}

		if (shipLogPanel.getState(TOTAL_SCRAP).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalScrapCollected());
			}
			GraphRenderer.superArray.put(TOTAL_SCRAP, intArray);
		}

		if (shipLogPanel.getState(TOTAL_CREW).isSelected()) {
			ArrayList<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.gameStateArray.get(i).getTotalCrewHired());
			}
			GraphRenderer.superArray.put(TOTAL_CREW, intArray);
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
			ArrayList<Integer> intArray = new ArrayList<Integer>();
			for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
				intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getScrapAmt());
			}
			GraphRenderer.superArray.put(SCRAP, intArray);
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

			((FTLFrame)crewPanelArray.get(i).getTopLevelAncestor()).pack();
			if (
				frame.getHeight() >
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()
			) {
				frame.setSize(
					frame.getWidth(),
					GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight() - 50
				);
			}

		}
		lastCrewSize = FTLAdventureVisualiser.playerCrewArray.get(latest).size();

		// TODO fix bug that resets crew stat history to 0 when losing a crewmember other than the last one in the list
		for (int i = 0; i < crewPanelArray.size(); i++) {

			try {

				for (int k = 0; k < FTLAdventureVisualiser.playerCrewArray.get(latest).size(); k++) {

					if (((TitledBorder)crewPanelArray.get(i).getBorder()).getTitle().equals(FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getName())) {

						log.info(
							"BorderTitle : " + ((TitledBorder)crewPanelArray.get(i).getBorder()).getTitle() +
							" CrewName : " + FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getName()
						);

						crewPanelArray.get(i).setLabelValue(CREW_RACE, ShipDataParser.getFullCrewType(latest, k));
						crewPanelArray.get(i).setValue(CREW_HEALTH, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getHealth());
						crewPanelArray.get(i).setValue(CREW_SKILL_PILOT, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getPilotSkill());
						crewPanelArray.get(i).setValue(CREW_SKILL_ENGINE, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getEngineSkill());
						crewPanelArray.get(i).setValue(CREW_SKILL_SHIELD, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getShieldSkill());
						crewPanelArray.get(i).setValue(CREW_SKILL_WEAPON, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getWeaponSkill());
						crewPanelArray.get(i).setValue(CREW_SKILL_REPAIR, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getRepairSkill());
						crewPanelArray.get(i).setValue(CREW_SKILL_COMBAT, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getCombatSkill());
						crewPanelArray.get(i).setValue(CREW_REPAIRS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getRepairs());
						crewPanelArray.get(i).setValue(CREW_KILLS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getCombatKills());
						crewPanelArray.get(i).setValue(CREW_EVASIONS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getPilotedEvasions());
						crewPanelArray.get(i).setValue(CREW_JUMPS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getJumpsSurvived());
						crewPanelArray.get(i).setValue(CREW_SKILLS, FTLAdventureVisualiser.playerCrewArray.get(latest).get(k).getSkillMasteries());


						String crewName = ((TitledBorder)crewPanelArray.get(i).getBorder()).getTitle();

						if (crewPanelArray.get(i).getState(CREW_HEALTH).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getHealth());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_HEALTH, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_PILOT).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getPilotSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_PILOT, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_ENGINE).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getEngineSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_ENGINE, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_SHIELD).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getShieldSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_SHIELD, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_WEAPON).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getWeaponSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_WEAPON, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_REPAIR).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getRepairSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_REPAIR, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILL_COMBAT).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getCombatSkill());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILL_COMBAT, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_REPAIRS).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getRepairs());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_REPAIRS, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_KILLS).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getCombatKills());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_KILLS, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_EVASIONS).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getPilotedEvasions());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_EVASIONS, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_JUMPS).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getJumpsSurvived());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_JUMPS, intArray);
						}

						if (crewPanelArray.get(i).getState(CREW_SKILLS).isSelected()) {
							ArrayList<Integer> intArray = new ArrayList<Integer>();
							for (int j = 0; j < FTLAdventureVisualiser.playerCrewArray.size(); j++) {
								try {
									intArray.add(FTLAdventureVisualiser.playerCrewArray.get(j).get(k).getSkillMasteries());
								} catch (IndexOutOfBoundsException exception) {
									intArray.add(-1);
								}
							}
							GraphRenderer.superArray.put(crewName+"_"+CREW_SKILLS, intArray);
						}

					}

				}

			} catch (IndexOutOfBoundsException exception) {
				log.info(exception);
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
		log.info("superArray:\n" + GraphRenderer.superArray);
		log.info("Ceiling " + GraphRenderer.ceiling);

	}

}