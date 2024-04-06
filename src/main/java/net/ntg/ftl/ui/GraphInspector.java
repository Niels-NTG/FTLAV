package net.ntg.ftl.ui;

import lombok.extern.slf4j.Slf4j;
import net.ntg.ftl.constants.RecordingHeader;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


@Slf4j
public class GraphInspector extends JPanel {

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
		graphSettingShipLog.addDataTypeSetting(RecordingHeader.Location.FLEET_ADVANCEMENT, false, true);
		graphSettingShipLog.addDataTypeSetting(RecordingHeader.Log.TOTAL_SHIPS_DEFEATED, false, true);
		graphSettingShipLog.addDataTypeSetting(RecordingHeader.Log.TOTAL_SCRAP_COLLECTED, false, true);
		graphSettingShipLog.addDataTypeSetting(RecordingHeader.Log.TOTAL_CREW_HIRED, false, true);
		graphSettingShipLog.addDataTypeSetting(RecordingHeader.Log.SCORE, false, true);
		graphInspectorPanel.add(graphSettingShipLog, gridC);
		gridC.gridy++;

		TogglePanel graphSettingShipSupplies = new TogglePanel("Ship Supplies");
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.SCRAP, true, false);
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.HULL, false, false);
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.FUEL, true, false);
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.DRONE_PARTS, false, false);
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.MISSILES, false, false);
		graphSettingShipSupplies.addDataTypeSetting(RecordingHeader.Supplies.CREW_SIZE, false, false);
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
