package nl.nielspoldervaart.ftlav.ui;

import nl.nielspoldervaart.ftlav.data.TableColumnCategory;
import nl.nielspoldervaart.ftlav.data.TableRow;
import nl.nielspoldervaart.ftlav.data.VisualiserAnnotation;
import nl.nielspoldervaart.ftlav.visualiser.PlotType;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class GraphInspector extends JPanel {

	public GraphInspector(FTLFrame rootFrame) {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		LinkedHashMap<TableColumnCategory, TogglePanel> togglePanels = new LinkedHashMap<>();
		Field[] fields = TableRow.class.getDeclaredFields();
		for (Field field : fields) {
			VisualiserAnnotation annotation = field.getAnnotation(VisualiserAnnotation.class);
			if (annotation != null && annotation.category() != null && annotation.plotType() == PlotType.LINE) {
				TogglePanel togglePanel = togglePanels.get(annotation.category());
				if (togglePanel == null) {
					togglePanel = new TogglePanel(rootFrame, annotation.category().label);
					togglePanels.put(annotation.category(), togglePanel);
				}
				togglePanel.addDataTypeInputs(field.getName());
			}
		}

		togglePanels.values().forEach(this::add);
	}

}
