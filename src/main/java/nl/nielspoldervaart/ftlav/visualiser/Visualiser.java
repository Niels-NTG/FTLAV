package nl.nielspoldervaart.ftlav.visualiser;

import lombok.extern.slf4j.Slf4j;
import processing.core.PApplet;
import processing.core.PFont;


@Slf4j
public class Visualiser extends PApplet {

	// layout
	final int MARGIN = 24; // 24px margin on all sides of graphs

	// colors
	final int COLOR_BG_NORMAL = color(50, 43, 43);			// dark purple brown 	(background color)
	final int COLOR_BG_LIGHT = color(130, 109, 106);		// light purple brown	(background color)
	final int COLOR_BG_DARK = color(31, 35, 35);			// dark turquoise		(background color)
	final int COLOR_BORDER = color(230, 244, 222);			// white-greenish		(panel border color)
	final int COLOR_BUTTON = color(230, 244, 222);			// white-greenish		(button color)
	final int COLOR_BUTTON_ENABLED = color(253, 227, 77);	// yellow				(button color)
	final int COLOR_BUTTON_ACTIVE = color(148, 155, 143);	// greenish grey		(button color)
	final int COLOR_BUTTON_TEXT = color(20, 37, 39);		// dark turquoise		(button text color)
	final int COLOR_STATUS_BACKGROUND = color(54, 74, 90);  // dark blue            (background of resource labels)
	final int COLOR_MAIN_TEXT = color(230, 244, 222);		// white-greenish		(standard text color)
	final int COLOR_SECTOR_CIVILIAN = color(118, 191, 58);	// bright green			(sector color)
	final int COLOR_SECTOR_HOSTILE = color(202, 29, 37);	// bright red			(sector color)
	final int COLOR_SECTOR_NEBULA = color(107, 18, 200);	// purple				(sector color)
	final int COLOR_SYSTEM_ACTIVE = color(89, 255, 82);		// bright green			(system status color)
	final int COLOR_SYSTEM_OFF = color(201, 201, 201);		// light grey			(system status color)
	final int COLOR_SYSTEM_DAMAGE = color(245, 56, 53);		// bright red			(system status color)
	final int COLOR_SYSTEM_HACKED = color(193, 27, 253);	// magenta				(system status color)
	final int COLOR_SYSTEM_IONIZED = color(119, 245, 253);	// cyan					(system status color)

	// gradients
	final int[] GLOW_BLUE = {
		COLOR_BORDER,
		color(69, 110, 112, 226),
		color(61, 94, 97, 198),
		color(53, 80, 81, 170),
		color(47, 69, 70, 141),
		color(44, 61, 62, 113),
		color(40, 54, 54, 85),
		color(37, 50, 49, 56),
		color(36, 47, 46, 28)
	};
	final int[] GLOW_GREEN = {
		COLOR_BORDER,
		color(66, 119, 17, 226),
		color(53, 106, 4, 198),
		color(45, 92, 1, 170),
		color(40, 80, 1, 141),
		color(36, 72, 1, 113),
		color(32, 65, 1, 85),
		color(30, 61, 1, 56),
		color(29, 58, 1, 25)
	};
	final int[] GLOW_RED = {
		COLOR_BORDER,
		color(182, 30, 30, 226),
		color(162, 24, 24, 198),
		color(140, 19, 19, 170),
		color(122, 14, 14, 141),
		color(112, 11, 11, 113),
		color(100, 7, 7, 85),
		color(93, 7, 7, 56),
		color(88, 5, 5, 25)
	};
	final int[] GLOW_PURPLE = {
		COLOR_BORDER,
		color(130, 4, 165, 226),
		color(117, 1, 150, 198),
		color(106, 1, 135, 170),
		color(95, 1, 121, 141),
		color(90, 1, 115, 113),
		color(83, 1, 106, 85),
		color(74, 1, 95, 56),
		color(70, 1, 90, 25)
	};

	// fonts
	final PFont FONT_SMALL;
	final PFont FONT_MAIN;
	final PFont FONT_LARGE;
	final PFont FONT_LARGE_BOLD;

	public Visualiser() {
		super();
		FONT_SMALL = loadFont(ClassLoader.getSystemResource("visualiser/JustinFont7-16.vlw").toString());
		FONT_MAIN = loadFont(ClassLoader.getSystemResource("visualiser/JustinFont8-16.vlw").toString());
		FONT_LARGE = loadFont(ClassLoader.getSystemResource("visualiser/JustinFont12-16.vlw").toString());
		FONT_LARGE_BOLD = loadFont(ClassLoader.getSystemResource("visualiser/JustinFont12Bold-16.vlw").toString());
	}

	public void setup() {
		size(1200, 700);
	}

	public void draw() {

		background(COLOR_BG_DARK);

		// TODO implement title graphic (no margin, upper-left corner, ship name, date, etc.)

		LineGraph lineGraph = new LineGraph(this, Math.max(256, width) - MARGIN * 2, Math.max(256, height) - MARGIN * 2);
		image(lineGraph.draw(), MARGIN, MARGIN);

		noLoop();

	}

	int getTextWidth(String text, PFont font) {
		textFont(font);
		return (int) textWidth(text);
	}

	int[] getSectorGradient(String sectorType) {
		if (sectorType.equalsIgnoreCase("civilian")) {
			return GLOW_GREEN;
		}
		if (sectorType.equalsIgnoreCase("nebula")) {
			return GLOW_PURPLE;
		}
		if (sectorType.equalsIgnoreCase("hostile")) {
			return GLOW_RED;
		}
		return GLOW_BLUE;
	}

	int[] getLineColor(String colorName) {
		switch (colorName) {
			case "Green":
				return GLOW_GREEN;
			case "Red":
				return GLOW_RED;
			case "Blue":
				return GLOW_BLUE;
			default:
				return GLOW_PURPLE;
		}
	}

}
