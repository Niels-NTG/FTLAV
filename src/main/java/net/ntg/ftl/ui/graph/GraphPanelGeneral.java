package net.ntg.ftl.ui.graph;

import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JToolBar;
import javax.swing.BorderFactory;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.TogglePanel;
import net.ntg.ftl.ui.graph.GraphRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphPanelGeneral extends JToolBar {

	private static final Logger log = LogManager.getLogger(GraphPanelGeneral.class);

	private FTLFrame frame;

	private TogglePanel suppliesPanel = null;
	private static final String SCRAP 		= "Scrap";
	private static final String HULL 		= "Hull";
	private static final String FUEL 		= "Fuel";
	private static final String DRONE_PARTS = "Drone Parts";
	private static final String MISSILES 	= "Missiles";


	public GraphPanelGeneral (FTLFrame frame) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.frame = frame;

		// Ship Supplies
		suppliesPanel = new TogglePanel();
		suppliesPanel.setBorder (BorderFactory.createTitledBorder("Supplies"));
		suppliesPanel.addToggle(SCRAP, false);
		suppliesPanel.addToggle(HULL, false);
		suppliesPanel.addToggle(FUEL, false);
		suppliesPanel.addToggle(DRONE_PARTS, false);
		suppliesPanel.addToggle(MISSILES, false);
		this.add(suppliesPanel);

	}


	public void setGameState() {
		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		// TODO get value labels directly after loading savegame
		suppliesPanel.setValue(SCRAP, FTLAdventureVisualiser.shipStateArray.get(latest).getScrapAmt());
		suppliesPanel.setValue(HULL, FTLAdventureVisualiser.shipStateArray.get(latest).getHullAmt());
		suppliesPanel.setValue(FUEL, FTLAdventureVisualiser.shipStateArray.get(latest).getFuelAmt());
		suppliesPanel.setValue(DRONE_PARTS, FTLAdventureVisualiser.shipStateArray.get(latest).getDronePartsAmt());
		suppliesPanel.setValue(MISSILES, FTLAdventureVisualiser.shipStateArray.get(latest).getMissilesAmt());

		if (FTLAdventureVisualiser.gameStateArray.size() > 0) {

			GraphRenderer.superArray.clear();

			if (suppliesPanel.getState(SCRAP).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getScrapAmt());
				}
				GraphRenderer.superArray.add(intArray);
			}

			if (suppliesPanel.getState(HULL).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getHullAmt());
				}
				GraphRenderer.superArray.add(intArray);
			}

			if (suppliesPanel.getState(FUEL).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getFuelAmt());
				}
				GraphRenderer.superArray.add(intArray);
			}

			if (suppliesPanel.getState(DRONE_PARTS).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getDronePartsAmt());
				}
				GraphRenderer.superArray.add(intArray);
			}

			if (suppliesPanel.getState(MISSILES).isSelected()) {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				for (int i = 0; i < FTLAdventureVisualiser.gameStateArray.size(); i++) {
					intArray.add(FTLAdventureVisualiser.shipStateArray.get(i).getMissilesAmt());
				}
				GraphRenderer.superArray.add(intArray);
			}


			ArrayList<Integer> ceilingMeasureArray = new ArrayList<Integer>();
			for (int i = 0; i < GraphRenderer.superArray.size(); i++) {
				ceilingMeasureArray.add( Collections.max(GraphRenderer.superArray.get(i)) );
			}
			GraphRenderer.ceiling = Collections.max(ceilingMeasureArray) <= 30 ?
				30 : Collections.max(ceilingMeasureArray);


			log.info("superArray a " + GraphRenderer.superArray.size());
			for (int i = 0; i < GraphRenderer.superArray.size(); i++) {
				log.info("superArray a b (" + i + ") " + GraphRenderer.superArray.get(i).size());
			}
			log.info("Ceiling " + GraphRenderer.ceiling);

		}
	}
}