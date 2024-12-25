package nl.nielspoldervaart.ftlav.ui;

import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import nl.nielspoldervaart.ftlav.data.ShipSystemType;
import nl.nielspoldervaart.ftlav.data.TableRow;
import nl.nielspoldervaart.ftlav.data.VisualiserAnnotation;
import nl.nielspoldervaart.ftlav.visualiser.GraphLineColor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.Arrays;

@Slf4j
public class TogglePanel extends JPanel {

	private final FTLFrame rootFrame;
	private static final Object[] COLORS = Arrays.stream(GraphLineColor.values()).map((color) -> color.icon).toArray();

	public TogglePanel(FTLFrame rootFrame, String borderTitle) {
		this.rootFrame = rootFrame;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(borderTitle));
	}

	public void addDataTypeInputs(String columnName) {
		String displayLabel = DataUtil.getColumnDisplayName(columnName);

		VisualiserAnnotation annotation = TableRow.getVisualiserAnnotation(columnName);
		boolean isEnabled = FTLAdventureVisualiser.columnsInVisualiser.getOrDefault(columnName, false);

		Container rowContainer = new Container();
		rowContainer.setLayout(new BoxLayout(rowContainer, BoxLayout.X_AXIS));

		JLabel label = new JLabel(new ImageIcon(annotation != null ? annotation.system().icon : ShipSystemType.NONE.icon));
		rowContainer.add(label);

		JComboBox<Object> colorSelector = new JComboBox<>(COLORS);
		colorSelector.setSelectedItem(FTLAdventureVisualiser.colorsInVisualiser.getOrDefault(columnName, GraphLineColor.PURPLE).icon);
		colorSelector.setToolTipText("Set graph line color");
		colorSelector.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				FTLAdventureVisualiser.colorsInVisualiser.put(columnName, GraphLineColor.values()[colorSelector.getSelectedIndex()]);
				rootFrame.redrawVisualiser();
			}
		});
		colorSelector.setEnabled(isEnabled);
		colorSelector.setMaximumSize(colorSelector.getPreferredSize());

		JCheckBox enabledCheckBox = new JCheckBox(
			displayLabel,
			isEnabled
		);
		enabledCheckBox.setToolTipText("Toggle visibility");
		enabledCheckBox.addItemListener(e -> {
			FTLAdventureVisualiser.columnsInVisualiser.put(columnName, enabledCheckBox.isSelected());
			rootFrame.redrawVisualiser();
			colorSelector.setEnabled(enabledCheckBox.isSelected());
		});
		rowContainer.add(enabledCheckBox);
		rowContainer.add(Box.createHorizontalGlue());
		rowContainer.add(colorSelector);
		add(rowContainer);
	}
}
