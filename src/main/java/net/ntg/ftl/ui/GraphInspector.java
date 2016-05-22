package net.ntg.ftl.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;


public class GraphInspector extends JPanel {

	private static final Logger log = LogManager.getLogger(GraphInspector.class);

	private final FTLFrame frame;

	private JTabbedPane graphTypeInspectorPanel;

	// Graph settings
	private TogglePanel graphMainSettings;

	// icons
	private final ImageIcon loadGameIcon = new ImageIcon(ClassLoader.getSystemResource("crew-shield.gif"));


	public GraphInspector(FTLFrame frame) {

		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());


		// main graph settings
		graphMainSettings = new TogglePanel("Main settings");
		graphMainSettings.addCheckBox("Graph title enabled", true);
		graphMainSettings.addCheckBox("Graph transparancy", true);
		graphMainSettings.addCheckBox("Show game info", true, loadGameIcon);

		add(graphMainSettings, BorderLayout.NORTH);


		// graph type settings
		graphTypeInspectorPanel = new JTabbedPane();

		// line graph settings
		setupLineGraphInspectorPanel();

		// bar graph
		setupBarGraphInspectorPanel();

		graphTypeInspectorPanel.setPreferredSize(
			new Dimension(this.getWidth(), 400)
		);
		add(graphTypeInspectorPanel, BorderLayout.SOUTH);

		this.frame = frame;

	}


	private void setupLineGraphInspectorPanel() {

		JPanel graphInspectorPanel = new JPanel();
		graphInspectorPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridC = new GridBagConstraints();
		gridC.gridx = 0;
		gridC.gridy = 0;
		gridC.weightx = 1.0;
		gridC.weighty = 1.0;
		gridC.fill = GridBagConstraints.BOTH;

		TogglePanel graphSettingShipLog = new TogglePanel("Ship Log");
		graphSettingShipLog.addDataTypeSetting("Fleet Advancement", false, true);
		graphSettingShipLog.addDataTypeSetting("Total Ships Defeated", false, true);
		graphSettingShipLog.addDataTypeSetting("Total Scrap Collected", false, true);
		graphSettingShipLog.addDataTypeSetting("Total Crew Hired", false, true);
		graphSettingShipLog.addDataTypeSetting("Score", false, true);
		graphInspectorPanel.add(graphSettingShipLog, gridC);
		gridC.gridy++;

		TogglePanel graphSettingShipSupplies = new TogglePanel("Ship Supplies");
		graphSettingShipSupplies.addDataTypeSetting("Scrap", true, false);
		graphSettingShipSupplies.addDataTypeSetting("Hull", false, false);
		graphSettingShipSupplies.addDataTypeSetting("Fuel", true, false);
		graphSettingShipSupplies.addDataTypeSetting("Drone Parts", false, false);
		graphSettingShipSupplies.addDataTypeSetting("Missiles", false, false);
		graphSettingShipSupplies.addDataTypeSetting("Oxygen level", false, false);
		graphSettingShipSupplies.addDataTypeSetting("Power Capacity", false, true);
		graphInspectorPanel.add(graphSettingShipSupplies, gridC);
		gridC.gridy++;


		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setViewportView(graphInspectorPanel);

		graphTypeInspectorPanel.addTab("Line graph", scrollPane);

	}


	private void setupBarGraphInspectorPanel() {

		JPanel graphInspectorPanel = new JPanel();
		graphInspectorPanel.setLayout(new GridBagLayout());
		// TODO fill the thing with switches using the TogglePanel class

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setViewportView(graphInspectorPanel);

		graphTypeInspectorPanel.addTab("Bar graph", graphInspectorPanel);

	}

}