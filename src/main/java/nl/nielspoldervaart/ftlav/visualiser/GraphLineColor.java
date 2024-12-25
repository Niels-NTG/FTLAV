package nl.nielspoldervaart.ftlav.visualiser;

import javax.swing.ImageIcon;

public enum GraphLineColor {
	PURPLE("Purple"),
	GREEN("Green"),
	RED("Red"),
	BLUE("Blue");

	public final String label;
	public final ImageIcon icon;

	GraphLineColor(String label) {
		this.label = label;
		icon = new ImageIcon(ClassLoader.getSystemResource("UI/color-" + label.toLowerCase() + ".gif"), label);
	}
}
