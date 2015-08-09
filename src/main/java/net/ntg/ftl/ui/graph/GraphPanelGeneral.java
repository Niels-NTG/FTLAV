package net.ntg.ftl.ui.graph;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.ui.FTLFrame;
import net.ntg.ftl.ui.TogglePanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphPanelGeneral extends JToolBar {

	private static final Logger log = LogManager.getLogger(GraphPanelGeneral.class);

	private FTLFrame frame;

	private TogglePanel suppliesPanel = null;

	public GraphPanelGeneral (FTLFrame frame) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.frame = frame;

		// Ship Supplies
		suppliesPanel = new TogglePanel();
		suppliesPanel.setBorder (BorderFactory.createTitledBorder("Supplies"));
		suppliesPanel.addToggle("Scrap");
		suppliesPanel.addToggle("Hull");
		suppliesPanel.addToggle("Fuel");
		suppliesPanel.addToggle("Drone Parts");
		suppliesPanel.addToggle("Missiles");
		suppliesPanel.addBlankRow();
		suppliesPanel.addFillRow();

		this.add(suppliesPanel);
	}

	public void setGameState() {
		int latest = FTLAdventureVisualiser.gameStateArray.size() - 1;

		suppliesPanel.setValue("Scrap", FTLAdventureVisualiser.shipStateArray.get(latest).getScrapAmt());
		suppliesPanel.setValue("Hull", FTLAdventureVisualiser.shipStateArray.get(latest).getHullAmt());
		suppliesPanel.setValue("Fuel", FTLAdventureVisualiser.shipStateArray.get(latest).getFuelAmt());
		suppliesPanel.setValue("Drone Parts", FTLAdventureVisualiser.shipStateArray.get(latest).getDronePartsAmt());
		suppliesPanel.setValue("Missiles", FTLAdventureVisualiser.shipStateArray.get(latest).getMissilesAmt());
	}

}