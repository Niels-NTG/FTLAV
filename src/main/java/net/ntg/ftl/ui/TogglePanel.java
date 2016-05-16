package net.ntg.ftl.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.prefs.Preferences;


public class TogglePanel extends JPanel {

	private static final Logger log = LogManager.getLogger(TogglePanel.class);

	private static final Preferences prefs = Preferences.userNodeForPackage(net.ntg.ftl.FTLAdventureVisualiser.class);

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

	public void addCheckBox(String valueName, boolean defaultValue) {
		addCheckBox(valueName, defaultValue, null);
	}
	public void addCheckBox(final String valueName, boolean defaultValue, ImageIcon icon) {
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

		JLabel label = new JLabel();
		if (icon != null) {
			label.setIcon(icon);
		} else {
			label.setIcon(emptyIcon);
		}
		checkboxPanel.add(label);

		boolean userValue = prefs.getBoolean(valueName + "_enabled", defaultValue);
		prefs.putBoolean(valueName + "_enabled", userValue);

		final JCheckBox checkBox = new JCheckBox();
		checkBox.setText(valueName);
		checkBox.setSelected(userValue);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				log.info("checkBox " + checkBox.getText() + ": " + checkBox.isSelected());
				prefs.putBoolean(valueName + "_enabled", checkBox.isSelected());
			}
		});
		checkboxPanel.add(checkBox);

		add(checkboxPanel, gridC);
		gridC.gridy++;

	}

}