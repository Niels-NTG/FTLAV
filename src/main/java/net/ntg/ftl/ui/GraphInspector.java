package net.ntg.ftl.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class GraphInspector extends JPanel {

	private static final Logger log = LogManager.getLogger(GraphInspector.class);

	// TODO construct tabbed pane UI

	// TODO make pane scrollable and fit screen heigth
	// TODO add actionListener to each checkbox. Should destroy and init GraphRenderer when toggled
	// TODO output checkbox status to FTLAdventureVisualer.enabledRecordingHeaders as true/false
	// TODO remember if box was already (un)checked by only adding new boxes that don't appear in enabledRecordingHeaders yet
	// TODO get values from latest entry FTLAdventureVisualer.recording to put next to checkboxes
	// TODO sort boxes into titled borderboxes, this is something this class should do, NOT TogglePanel

	private final FTLFrame frame;

	private JTabbedPane graphTypeInspectorPanel;

	// Graph settings
	private TogglePanel graphMainSettings;
	private TogglePanel graphSettings;
	private TogglePanel lineGraphShipLogSettings;

	// icons
	private final ImageIcon loadGameIcon = new ImageIcon(ClassLoader.getSystemResource("crew-shield.gif"));


	public GraphInspector(FTLFrame frame) {

		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());


		// main graph settings
		// TODO replace with TogglePanel class
		graphMainSettings = new TogglePanel("Main settings");
		graphMainSettings.addCheckBox("Graph title enabled");
		graphMainSettings.addCheckBox("Graph transparancy");
		graphMainSettings.addCheckBox("Show game info", loadGameIcon);

		add(graphMainSettings, BorderLayout.NORTH);


		// graph type settings
		graphTypeInspectorPanel = new JTabbedPane();

		// line graph settings
		setupLineGraphInspectorPanel();

		// bar graph
		setupBarGraphInspectorPanel();


		add(graphTypeInspectorPanel, BorderLayout.SOUTH);

		this.frame = frame;

	}


	private void setupLineGraphInspectorPanel() {

		JPanel graphInspectorPanel = new JPanel();
		graphInspectorPanel.setLayout(new GridBagLayout());
		// TODO fill the thing with switches using the TogglePanel class
		// TODO completly refactor and simplify TogglePanel class

		lineGraphShipLogSettings = new TogglePanel("Ship Log");
		graphInspectorPanel.add(lineGraphShipLogSettings);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(graphInspectorPanel);

		graphTypeInspectorPanel.addTab("Line graph", scrollPane);

	}


	private void setupBarGraphInspectorPanel() {

		JPanel graphInspectorPanel = new JPanel();
		graphInspectorPanel.setLayout(new GridBagLayout());
		// TODO fill the thing with switches using the TogglePanel class

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(graphInspectorPanel);

		graphTypeInspectorPanel.addTab("Bar graph", graphInspectorPanel);

	}

}