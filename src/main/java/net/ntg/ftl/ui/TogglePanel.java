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
	private static final String ENABLED = "_enabled";
	private static final String DELTA = "_isDelta";
	private static final String COLOR = "_color";
	private static final Object[] COLORS = { // TODO replace with color gradient icons
		new ImageIcon(ClassLoader.getSystemResource("color-blue.gif")),
		new ImageIcon(ClassLoader.getSystemResource("color-green.gif")),
		new ImageIcon(ClassLoader.getSystemResource("color-red.gif")),
		new ImageIcon(ClassLoader.getSystemResource("color-purple.gif"))
	};

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

		boolean userValue = prefs.getBoolean(valueName + ENABLED, defaultValue);
		prefs.putBoolean(valueName + ENABLED, userValue);

		final JCheckBox checkBox = new JCheckBox();
		checkBox.setText(valueName);
		checkBox.setSelected(userValue);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				log.info("checkBox " + checkBox.getText() + ": " + checkBox.isSelected());
				prefs.putBoolean(valueName + ENABLED, checkBox.isSelected());
			}
		});
		checkboxPanel.add(checkBox);

		add(checkboxPanel, gridC);
		gridC.gridy++;

	}

	public void addDataTypeSetting(String valueName, boolean defaultValue) {
		addDataTypeSetting(valueName, defaultValue, null);
	}
	public void addDataTypeSetting(final String valueName, boolean defaultValue, ImageIcon icon) {

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

		JLabel iconLabel = new JLabel();
		if (icon != null) {
			iconLabel.setIcon(icon);
		} else {
			iconLabel.setIcon(emptyIcon);
		}
		panel.add(iconLabel);

		boolean userValueEnabled = prefs.getBoolean(valueName + ENABLED, defaultValue);
		prefs.putBoolean(valueName + ENABLED, userValueEnabled);
		final JCheckBox enabledCheckBox = new JCheckBox();
		enabledCheckBox.setSelected(userValueEnabled);
		enabledCheckBox.setToolTipText("Enable visibility");
		enabledCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				log.info("checkBox " + enabledCheckBox.getToolTipText() + ": " + enabledCheckBox.isSelected());
				prefs.putBoolean(valueName + ENABLED, enabledCheckBox.isSelected());
			}
		});
		panel.add(enabledCheckBox);

		boolean userValueDeltaType = prefs.getBoolean(valueName + DELTA, false);
		prefs.putBoolean(valueName + ENABLED, userValueDeltaType);
		final JCheckBox isDeltaTypeCheckBox = new JCheckBox();
		isDeltaTypeCheckBox.setText("∆");
		isDeltaTypeCheckBox.setSelected(userValueDeltaType);
		isDeltaTypeCheckBox.setToolTipText("Enable relative view");
		isDeltaTypeCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				log.info("checkBox " + isDeltaTypeCheckBox.getText() + ": " + isDeltaTypeCheckBox.isSelected());
				prefs.putBoolean(valueName + DELTA, isDeltaTypeCheckBox.isSelected());
			}
		});
		panel.add(isDeltaTypeCheckBox);

		int userValueColor = prefs.getInt(valueName + COLOR, 0);
		prefs.putInt(valueName + COLOR, userValueColor);
		final JComboBox<Object> colorComboBox = new JComboBox<>(COLORS);
		colorComboBox.setToolTipText("Set graph line");
		colorComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					log.info("comboBox " + colorComboBox.getToolTipText() + ": " + colorComboBox.getSelectedIndex());
					prefs.putInt(valueName + COLOR, colorComboBox.getSelectedIndex());
				}
			}
		});
		panel.add(colorComboBox);

		JLabel label = new JLabel();
		label.setText(valueName);
		panel.add(label);

		add(panel, gridC);
		gridC.gridy++;

	}

}