package nl.nielspoldervaart.ftlav.ui;

import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import nl.nielspoldervaart.ftlav.visualiser.GraphLineColor;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.util.Arrays;

@Slf4j
public class TogglePanel extends JPanel {

	private final FTLFrame rootFrame;
	private static final Object[] COLORS = Arrays.stream(GraphLineColor.values()).map((color) -> color.icon).toArray();

	private final GridBagConstraints gridC = new GridBagConstraints();

	public TogglePanel(FTLFrame rootFrame, String borderTitle) {
		this.rootFrame = rootFrame;

		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createTitledBorder(borderTitle));

		gridC.gridx = 0;
		gridC.gridy = 0;
		gridC.weightx = 1.0;
		gridC.weighty = 1.0;
		gridC.fill = GridBagConstraints.BOTH;
	}

	public void addDataTypeInputs(String columnName) {
		final String displayLabel = DataUtil.getColumnDisplayName(columnName);

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 4));

		JCheckBox enabledCheckBox = new JCheckBox(
			displayLabel,
			FTLAdventureVisualiser.columnsInVisualiser.getOrDefault(columnName, false)
		);
		enabledCheckBox.setToolTipText("Toggle visibility");
		enabledCheckBox.addItemListener(e -> {
			FTLAdventureVisualiser.columnsInVisualiser.put(columnName, enabledCheckBox.isSelected());
			rootFrame.redrawVisualiser();
		});
		panel.add(enabledCheckBox);

		JComboBox<Object> colorSelector = new JComboBox<>(COLORS);
		colorSelector.setSelectedItem(FTLAdventureVisualiser.colorsInVisualiser.getOrDefault(columnName, GraphLineColor.PURPLE).icon);
		colorSelector.setToolTipText("Set graph line color");
		colorSelector.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				FTLAdventureVisualiser.colorsInVisualiser.put(columnName, GraphLineColor.values()[colorSelector.getSelectedIndex()]);
				rootFrame.redrawVisualiser();
			}
		});
		panel.add(colorSelector);

		add(panel, gridC);
		gridC.gridy++;
	}

}
