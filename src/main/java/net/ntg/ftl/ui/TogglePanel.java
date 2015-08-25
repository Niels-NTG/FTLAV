package net.ntg.ftl.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


public class TogglePanel extends JPanel {

	private Map<String, JToggleButton> toggleButtonMap = new HashMap<String, JToggleButton>();
	private Map<String, JLabel> labelMap = new HashMap<String, JLabel>();

	private GridBagConstraints gridC = new GridBagConstraints();
	private Component valueStrut = Box.createHorizontalStrut( 120 );

	public TogglePanel () {
		super(new GridBagLayout());

		gridC.anchor = GridBagConstraints.WEST;
		gridC.fill = GridBagConstraints.HORIZONTAL;
		gridC.weightx = 0.0;
		gridC.weighty = 0.0;
		gridC.gridwidth = 1;
		gridC.gridx = 0;
		gridC.gridy = 0;

		// No default width for col 0.
		gridC.gridx = 0;
		this.add( Box.createVerticalStrut(1), gridC );
		gridC.gridx++;
		this.add( valueStrut, gridC );
		gridC.gridx++;

		gridC.insets = new Insets( 2, 4, 2, 4 );
	}


	public void setValueWidth ( int width ) {
		valueStrut.setMinimumSize( new Dimension(width, 0) );
		valueStrut.setPreferredSize( new Dimension(width, 0) );
	}


	public void addToggle ( String valueName, boolean state ) {
		gridC.fill = GridBagConstraints.HORIZONTAL;
		gridC.gridwidth = 1;
		gridC.weighty = 0.0;
		gridC.gridx = 0;

		gridC.gridx++;

		gridC.anchor = GridBagConstraints.WEST;
		// TODO icon
		JToggleButton toggleButton = new JToggleButton(valueName, state);
		toggleButtonMap.put(valueName, toggleButton);
		this.add(toggleButton, gridC);

		gridC.gridx++;

		gridC.anchor = GridBagConstraints.WEST;
		JLabel valueLabel = new JLabel();
		labelMap.put( valueName, valueLabel );
		this.add( valueLabel, gridC );

		gridC.gridy++;
	}


	public void addBlankRow() {
		gridC.fill = GridBagConstraints.NONE;
		gridC.weighty = 0.0;
		gridC.gridwidth = GridBagConstraints.REMAINDER;
		gridC.gridx = 0;

		this.add(Box.createVerticalStrut(12), gridC);
		gridC.gridy++;
	}


	public void addFillRow() {
		gridC.fill = GridBagConstraints.VERTICAL;
		gridC.weighty = 1.0;
		gridC.gridwidth = GridBagConstraints.REMAINDER;
		gridC.gridx = 0;

		this.add(Box.createVerticalGlue(), gridC);
		gridC.gridy++;
	}


	public void addComponent( JComponent c ) {
		gridC.anchor = GridBagConstraints.CENTER;
		gridC.fill = GridBagConstraints.NONE;
		gridC.weighty = 0.0;
		gridC.gridwidth = GridBagConstraints.REMAINDER;
		gridC.gridx = 0;

		this.add( c, gridC );
		gridC.gridy++;
	}


	public void setValue( String valueName, int n ) {
		JLabel valueLabel = labelMap.get(valueName);
		valueLabel.setText( Integer.toString(n) );
	}


	public void setValue( String valueName, String label ) {
		JLabel valueLabel = labelMap.get(valueName);
		valueLabel.setText( label );
	}


	public JToggleButton getState( String valueName ) {
		return toggleButtonMap.get( valueName );
	}
}