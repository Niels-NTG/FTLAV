package nl.nielspoldervaart.ftlav.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GraphInspector extends JPanel {

	public GraphInspector(FTLFrame rootFrame) {

		setLayout(new BorderLayout(4, 4));

		TogglePanel shipResourcesPanel = new TogglePanel(rootFrame, "Ship resources");
		shipResourcesPanel.addDataTypeInputs("scrap");
		shipResourcesPanel.addDataTypeInputs("fuel");
		shipResourcesPanel.addDataTypeInputs("missles");
		shipResourcesPanel.addDataTypeInputs("droneParts");
		shipResourcesPanel.addDataTypeInputs("crewSize");
		shipResourcesPanel.addDataTypeInputs("hull");
		add(shipResourcesPanel);
	}

}
