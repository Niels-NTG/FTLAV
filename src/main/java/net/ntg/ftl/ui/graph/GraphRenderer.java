package net.ntg.ftl.ui.graph;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import processing.core.*;

import net.ntg.ftl.FTLAdventureVisualiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	public static LinkedHashMap<String,ArrayList<Integer>> superArray = new LinkedHashMap<String,ArrayList<Integer>>();
	public static int ceiling = 30;

	int current = 0;
	int previous= 0;

	// window
	public static int panelWidth;
	public static int panelHeight;

	private int canvasWidth;
	private int canvasHeight;
	private int margin = 64; // px

	// graphics
	PFont mainFont;
	PFont headerFont;
	PFont headerFontAlt;

	int[] blueGlow = {
		color(235, 245, 227, 255),
		color(69, 110, 112, 226),
		color(61, 94, 97, 198),
		color(53, 80, 81, 170),
		color(47, 69, 70, 141),
		color(44, 61, 62, 113),
		color(40, 54, 54, 85),
		color(37, 50, 49, 56),
		color(36, 47, 46, 28)
	};

	int[] greenGlow = {
		color(235, 245, 229, 255),
		color(66, 119, 17, 226),
		color(53, 106, 4, 198),
		color(45, 92, 1, 170),
		color(40, 80, 1, 141),
		color(36, 72, 1, 113),
		color(32, 65, 1, 85),
		color(30, 61, 1, 56),
		color(29, 58, 1, 25)
	};

	int[] redGlow = {
		color(235, 245, 229, 255),
		color(182, 30, 30, 226),
		color(162, 24, 24, 198),
		color(140, 19, 19, 170),
		color(122, 14, 14, 141),
		color(112, 11, 11, 113),
		color(100, 7, 7, 85),
		color(93, 7, 7, 56),
		color(88, 5, 5, 25)
	};

	int[] purpleGlow = {
		color(235, 245, 229, 255),
		color(130, 4, 165, 226),
		color(117, 1, 150, 198),
		color(106, 1, 135, 170),
		color(95, 1, 121, 141),
		color(90, 1, 115, 113),
		color(83, 1, 106, 85),
		color(74, 1, 95, 56),
		color(70, 1, 90, 25)
	};

	Map<String,Integer> hudColor = new HashMap<String,Integer>();


	public void setup() {

		// window
		size(panelWidth, panelHeight);

		canvasWidth = panelWidth  - (margin * 2);
		canvasHeight= panelHeight - (margin * 2);

		// graphics
		mainFont      = loadFont(ClassLoader.getSystemResource("C&CRedAlertINET-48.vlw").toString());
		headerFont    = loadFont(ClassLoader.getSystemResource("Half-Life2-48.vlw").toString());
		headerFontAlt = loadFont(ClassLoader.getSystemResource("Half-Life1-48.vlw").toString());

		hudColor.put( "BG_NORMAL", color( 55, 45, 46 ) );			// dark purple brown 	(background color)
		hudColor.put( "BG_LIGHT", color( 122, 100, 99 ) );			// light purple brown	(background color)
		hudColor.put( "BG_DARK", color( 24, 20, 19 ) );				// dark brown			(background color)
		hudColor.put( "BORDER", color( 235, 245, 229 ) );			// white-greenish		(panel border color)
		hudColor.put( "BUTTON", color( 235, 245, 229 ) );			// white-greenish		(button color)
		hudColor.put( "BUTTON_ACTIVE", color( 255, 230, 94 ) );		// yellow				(button color)
		hudColor.put( "MAINTEXT", color( 255, 255, 255 ) );			// white				(standard text color)
		hudColor.put( "HEADERTEXT", color( 65, 120, 128 ) );		// turquoise			(standard text color)
		hudColor.put( "HEADERTEXT_ALT", color( 54, 78, 80 ) );		// dark turquoise		(standard text color)
		hudColor.put( "SECTOR_CIVILIAN", color( 135, 199, 74 ) );	// bright green			(sector color)
		hudColor.put( "SECTOR_HOSTILE", color( 214, 50, 50 ) );		// bright red			(sector color)
		hudColor.put( "SECTOR_NEBULA", color( 128, 51, 210 ) );		// pure purple			(sector color)
		hudColor.put( "SYSTEM_ACTIVE", color( 100, 255, 99 ) );		// bright green			(system status color)
		hudColor.put( "SYSTEM_OFF", color( 211, 211, 211 ) );		// light grey			(system status color)
		hudColor.put( "SYSTEM_DAMAGE", color( 248, 59, 51 ) );		// bright red			(system status color)
		hudColor.put( "SYSTEM_HACKED", color( 212, 70, 253 ) );		// magenta				(system status color)
		hudColor.put( "SYSTEM_IONIZED", color( 133, 231, 237 ) );	// cyan					(system status color)

		background(hudColor.get("BG_DARK"));

	}


	public void draw() {

		current = FTLAdventureVisualiser.gameStateArray.size() - 1;


		if (current > previous) {

			background(hudColor.get("BG_DARK"));

			for (int a = 0; a < superArray.size(); ++a) {

				ArrayList<Integer> lineArray = (new ArrayList<ArrayList>(superArray.values())).get(a);
				String lineLabel = (new ArrayList<String>(superArray.keySet())).get(a).toUpperCase();

				// graph line
				for (int s = blueGlow.length - 1; s >= 0; --s) {

					noFill();
					stroke(blueGlow[s]);
					strokeWeight(s == blueGlow.length - 1 ? 4 : 4 + (s * 2));
					strokeJoin(ROUND);
					strokeCap(ROUND);
					beginShape();
					for (int b = 0; b < lineArray.size(); ++b) {
						vertex(
							margin + (canvasWidth / lineArray.size()) * b,
							map(
								lineArray.get(b),
								0, ceiling,
								margin + canvasHeight, margin
							)
						);
					}
					endShape();
				}

				// draw label at end of line
				drawLineLabel(a, lineLabel, lineArray.size(), lineArray.get(lineArray.size()-1));

				// graph x labels
				noStroke();
				for (int b = 0; b < lineArray.size(); ++b) {

					// sector name labels
					if (b == 0 ||
						FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber() >
						FTLAdventureVisualiser.gameStateArray.get(b-1).getSectorNumber()
					) {
						drawSectorLabel(
							a,
							b,
							FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber(),
							lineArray.size()
						);
					}

					// beacon numbers
					fill(235, 245, 227);
					textFont(mainFont, 15);
					textAlign(LEFT, BOTTOM);
					text(
						FTLAdventureVisualiser.gameStateArray.get(b).getTotalBeaconsExplored(),
						margin + (canvasWidth / lineArray.size()) * b,
						canvasHeight + margin + (margin / 4)
					);
				}

			}

			// graph y labels
			noStroke();
			fill(235, 245, 227);
			textFont(mainFont, 15);
			textAlign(RIGHT, BASELINE);
			// TODO better algoritme for drawing y labels
			for (int y = canvasHeight; y > 0; y -= (canvasHeight * 0.04)) {
				text(
					Integer.toString(y),
					margin - (margin / 4),
					map(y, 0, ceiling, canvasHeight + margin, margin)
				);
			}

			// TODO draw text label on the end of each graph line that shows what data it is

		}

		previous = current;

	}


	private void drawLineLabel( int a, String lineLabel, int lineSize, int lastestValue ) {

		// TODO rewrite this function
		// TODO better looking labels

		int textSize       = 15;
		int xPos           = (margin + (canvasWidth / lineSize) * (lineSize-1)) + 8;
		int yPos           = round(map(lastestValue, 0, ceiling, margin + canvasHeight, margin)) - 8;
		int padding        = 4;
		float glowSpread   = 1.3f;

		textFont( mainFont, textSize );
		textAlign( LEFT, BOTTOM );

		// label
		fill( hudColor.get("BORDER") );
		beginShape();
		vertex(xPos, yPos);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2, yPos);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2 + padding, yPos - padding);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2 + padding, yPos - (padding + textSize + padding));
		vertex(xPos + padding, yPos - (padding + textSize + padding));
		vertex(xPos, yPos - (padding + textSize));
		endShape(CLOSE);

		beginShape();
		vertex(xPos, yPos);
		vertex(xPos - 8, yPos + 8);
		endShape(CLOSE);

		// label key
		fill( hudColor.get("HEADERTEXT_ALT") );
		text( lineLabel, xPos + padding, yPos - padding );

		// label value
		beginShape();
		vertex(xPos + padding + textWidth(lineLabel) + padding, yPos);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2 + padding, yPos);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2 + padding, yPos - padding);
		vertex(xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)) * 2 + padding, yPos - (padding + textSize + padding));
		vertex(xPos + padding + textWidth(lineLabel) + padding, yPos - (padding + textSize + padding));
		endShape(CLOSE);

		fill( hudColor.get("MAINTEXT") );
		textAlign(CENTER, BOTTOM);
		text(lastestValue, xPos + padding + textWidth(lineLabel) + padding + textWidth(Integer.toString(lastestValue)), yPos - padding);

	}


	private void drawSectorLabel( int a, int b, int sectorNumber, int lineSize ) {

		int textSize       = 22;
		int xPos           = margin + (canvasWidth / lineSize) * b;
		int yPos           = canvasHeight + margin + (margin / 3);
		int padding        = 6;
		float glowSpread   = 1.3f;
		String sectorTitle = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle().toUpperCase();
		String sectorType  = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType();

		textFont( headerFont, textSize );
		textAlign( LEFT, TOP );


		// sector label
		fill( hudColor.get("BORDER") );
		beginShape();
		vertex( xPos, yPos );															// TL
		vertex( xPos, yPos + textSize + padding );										// BL
		vertex( textWidth( sectorTitle ) + xPos + padding, yPos + textSize + padding );	// BR
		vertex( textWidth( sectorTitle ) + xPos + padding + textSize, yPos );			// TR
		endShape(CLOSE);

		// extended line
		rect( xPos, yPos, canvasWidth - xPos, padding );

		// glow color
		int[] gradient;
		if (sectorType == "CIVILIAN") {
			gradient = greenGlow;
		} else if (sectorType == "HOSTILE") {
			gradient = redGlow;
		} else if (sectorType == "NEBULA") {
			gradient = purpleGlow;
		} else {
			gradient = blueGlow;
		}

		// apply glow effect
		fill( hudColor.get("BG_DARK") ); // tape over existing glow
		int glowSize = gradient.length - 1;
		rect(xPos, yPos - (glowSize * glowSpread) + 1, canvasWidth - xPos, glowSize * glowSpread);
		for (int s = glowSize; s >= 0; --s) {
			fill(gradient[s]);
			rect(xPos, yPos - (s * glowSpread), canvasWidth - xPos, padding);
		}

		// sector title text
		fill( hudColor.get("HEADERTEXT_ALT") );
		text( sectorTitle, xPos + padding, yPos + padding );

		// tape over left side
		fill( hudColor.get("BG_DARK") );
		rect( xPos - padding, yPos - (glowSize * glowSpread), padding, textSize + padding + (glowSize * glowSpread) );

	}
}