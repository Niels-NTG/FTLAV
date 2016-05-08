package net.ntg.ftl.ui;

import javax.swing.*;
import java.awt.*;


public class TogglePanel extends JPanel {

	private final GridBagConstraints gridC = new GridBagConstraints();
	private static final ImageIcon emptyIcon = new ImageIcon(ClassLoader.getSystemResource("empty.gif"));

	public TogglePanel(String borderTitle) {

		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createTitledBorder(borderTitle));

		gridC.gridx = 0;
		gridC.gridy = 0;
		gridC.weightx = 1.0;
		gridC.weighty = 1.0;
		gridC.fill = GridBagConstraints.BOTH;

	}

	public void addCheckBox(String valueName) {
		addCheckBox(valueName, null);
	}
	public void addCheckBox(String valueName, ImageIcon icon) {
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

		JLabel label = new JLabel();
		if (icon != null) {
			label.setIcon(icon);
		} else {
			label.setIcon(emptyIcon);
		}
		checkboxPanel.add(label);

		JCheckBox checkBox = new JCheckBox();
		checkBox.setText(valueName);
		checkboxPanel.add(checkBox);
		add(checkboxPanel, gridC);
		gridC.gridy++;
	}

}