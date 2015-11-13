package net.ntg.ftl.ui.graph;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import processing.core.*;

import net.ntg.ftl.FTLAdventureVisualiser;
import net.ntg.ftl.parser.ShipDataParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphRenderer extends PApplet {

	private static Logger log = LogManager.getLogger(GraphRenderer.class);

	public static LinkedHashMap<String,ArrayList<Integer>> superArray = new LinkedHashMap<String,ArrayList<Integer>>();
	public static int ceiling = 20;

	int current  = 0;
	int previous = 0;

	// window
	public static int panelWidth;
	public static int panelHeight;

	private int canvasWidth;
	private int canvasHeight;
	private int margin = 64; // px

	public static boolean captureImage = false;
	public static String  exportPath   = "FTLAV_######";

	// graphics
	PGraphics pg;

	public static boolean showTitle = true;

	PFont mainFont13;
	PFont mainFont39;
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
		pg = createGraphics(panelWidth, panelHeight);

		mainFont13    = loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-13.vlw").toString());
		mainFont39    = loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-39.vlw").toString());
		headerFont    = loadFont(ClassLoader.getSystemResource("graph/Half-Life2-22.vlw").toString());
		headerFontAlt = loadFont(ClassLoader.getSystemResource("graph/Half-Life1-22.vlw").toString());

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

	}


	public void draw() {

		current = FTLAdventureVisualiser.gameStateArray.size() - 1;

		if (current > previous || current == 0 || isResized()) {

			// TODO mouseover+click shows seperate box about event, environment and stats of that particular beacon

			// TODO horizontal scroll if graph becomes too wide and too dense

			background(hudColor.get("BG_DARK"));

			pg.clear();
			pg = createGraphics(panelWidth, panelHeight);
			pg.beginDraw();

			// graph y labels
			drawYLabel();

			for (int a = 0; a < superArray.size(); ++a) {

				ArrayList<Integer> lineArray = (new ArrayList<ArrayList<Integer>>(superArray.values())).get(a);
				String lineLabel = (new ArrayList<String>(superArray.keySet())).get(a).toUpperCase();

				// graph line
				int[] gradient;
				if (lineLabel.contains("HEALTH") ||
					lineLabel.contains(" SKILL") ||
					lineLabel.contains("TOTAL REPAIRS") ||
					lineLabel.contains("TOTAL COMBAT") ||
					lineLabel.contains("TOTAL PILOTED") ||
					lineLabel.contains("TOTAL JUMPS") ||
					lineLabel.contains("SKILLS MASTERED")
				) {
					gradient = purpleGlow;
				} else if (
					lineLabel.contains("TOTAL SHIPS DEFEATED") ||
					lineLabel.contains("TOTAL BEACONS EXPLORED") ||
					lineLabel.contains("TOTAL SCRAP COLLECTED") ||
					lineLabel.contains("TOTAL CREW HIRED") ||
					lineLabel.contains("FLEET") ||
					lineLabel.contains("SCORE")
				) {
					gradient = redGlow;
				} else {
					gradient = blueGlow;
				}

				for (int s = gradient.length - 1; s >= 0; --s) {

					pg.noFill();
					pg.stroke(gradient[s]);
					pg.strokeWeight(s == gradient.length - 1 ? 4 : 4 + (s * 2));
					pg.strokeJoin(ROUND);
					pg.strokeCap(ROUND);
					pg.beginShape();
					for (int b = 0; b < lineArray.size(); ++b) {
						if (lineArray.get(b) > -1) {
							pg.vertex(
								margin + (canvasWidth / lineArray.size()) * b,
								map(
									lineArray.get(b),
									0, ceiling,
									margin + canvasHeight, margin
								)
							);
						}
					}
					pg.endShape();

				}

				// draw label at end of line
				drawLineLabel( a, lineLabel, lineArray.size(), lineArray.get(lineArray.size()-1) );

				// graph x labels
				for (int b = 0; b < lineArray.size(); ++b) {

					// sector name labels
					if (b == 0 ||
						FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber() >
						FTLAdventureVisualiser.gameStateArray.get(b-1).getSectorNumber()
					) {
						drawSectorLabel( b, FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber(), lineArray.size() );
					}

					// beacon numbers
					drawBeaconLabel( b, lineArray.size() );

				}

			}

			if (current >= 0 && showTitle) {
				drawTitle( current );
			}

			pg.endDraw();

			tint(255);
			image(pg, 0, 0);

		}

		if (captureImage) {
			String fileTimestamp = " "+year()+"-"+month()+"-"+day()+" "+hour()+"-"+minute();
			pg.save(exportPath + fileTimestamp + " (alpha).png");
			saveFrame(exportPath + fileTimestamp + ".png");
			captureImage = false;
		}

		previous = current;

	}


	private boolean isResized () {

		if( width != panelWidth || height != panelHeight ) {
			panelWidth = width;
			panelHeight = height;
			canvasWidth = panelWidth  - (margin * 2);
			canvasHeight= panelHeight - (margin * 2);
			pg = createGraphics(panelWidth, panelHeight);
			return true;
		} else {
			return false;
		}

	}


	// TODO design bar graph rendering
	// TODO draw bar graph option

	// TODO draw a label next to the title for each indivual crew member
	// TODO corosponding line color for each crewmember. These lines are thinners than the others


	private void drawYLabel() {

		pg.noStroke();
		pg.fill(235, 245, 227);
		pg.textFont(mainFont13, 13);
		pg.textAlign(RIGHT, BOTTOM);
		for (int y = 0; y < canvasHeight; ++y) {
			if (y % 10 == 0) {
				pg.text(
					Integer.toString(y),
					margin - (margin / 2),
					map(y, 0, ceiling, canvasHeight + margin, margin)
				);
				pg.rect(
					margin, map(y, 0, ceiling, canvasHeight + margin, margin),
					canvasWidth, 0.2f
				);
			}
		}

	}


	private void drawTitle( int latest ) {

		// TODO title rendering and typography in style of ship window header title
		// TODO draw crew member bios next to ship name

		int shipNameTextSize = 39;
		int textSize         = 13;
		int padding          = 6;
		int offset           = margin / 2;
		int borderWeight     = 4;
		float glowSpread     = 1.3f;
		String shipName      = FTLAdventureVisualiser.gameStateArray.get(latest).getPlayerShipName();
		String shipType      = ShipDataParser.getFullShipType(latest);
		int score            = ShipDataParser.getCurrentScore(latest);
		String difficulty    = FTLAdventureVisualiser.gameStateArray.get(latest).getDifficulty().toString();
		String ae            = FTLAdventureVisualiser.gameStateArray.get(0).isDLCEnabled() ? " Advanced" : "";

		pg.noStroke();

		pg.textAlign( LEFT, TOP );

		pg.textFont( mainFont39, shipNameTextSize );
		int titleLabelWidth  = round(shipName.length() < 9 ? pg.textWidth("XXXXXXXX") : pg.textWidth(shipName) + (2 * (padding + borderWeight)));
		int titleLabelHeight = round((textSize * 3) + shipNameTextSize + padding);

		// title label
		for (int s = blueGlow.length - 1; s >= 0; --s) {
			pg.fill(hudColor.get("BG_LIGHT"));
			pg.stroke(blueGlow[s]);
			pg.strokeWeight(s == blueGlow.length - 1 ? borderWeight : borderWeight + (s * 2));
			pg.strokeJoin(ROUND);
			pg.strokeCap(ROUND);
			pg.beginShape();
			pg.vertex(margin, offset + padding);
			pg.vertex(margin + padding, offset);
			pg.vertex(margin + (titleLabelWidth - padding), offset);
			pg.vertex(margin + titleLabelWidth, offset + padding);
			pg.vertex(margin + titleLabelWidth, offset + (titleLabelHeight - padding));
			pg.vertex(margin + (titleLabelWidth - padding), offset + titleLabelHeight);
			pg.vertex(margin + padding, offset + titleLabelHeight);
			pg.vertex(margin, offset + (titleLabelHeight - padding));
			pg.endShape(CLOSE);
		}
		pg.noStroke();

		// title text
		pg.fill(hudColor.get("MAINTEXT"));
		pg.text(shipName, margin + borderWeight + padding, offset + borderWeight + padding);

		pg.textFont( mainFont13, textSize);
		pg.text(
			shipType+"\n"+
			"SCORE  "+ score+"\n"+
			difficulty +" "+ ae,
			margin + borderWeight + padding, offset + borderWeight + shipNameTextSize
		);

	}


	private void drawBeaconLabel ( int b, int lineSize ) {

		// TODO do not render all the numbers if screen pixel space is limited

		// TODO indicator to show if there where enemy ships, environmental hazards or BOSS at the beacon

		pg.noStroke();

		pg.fill(hudColor.get("MAINTEXT"));

		pg.textFont(mainFont13, 13);
		pg.textAlign(LEFT, BOTTOM);
		pg.text(
			FTLAdventureVisualiser.gameStateArray.get(b).getTotalBeaconsExplored(),
			margin + (canvasWidth / lineSize) * b,
			canvasHeight + margin + (margin / 4)
		);

	}


	private void drawSectorLabel( int b, int sectorNumber, int lineSize ) {

		// TODO do not render sectorTitle if screenspace becomes too narrow

		int textSize       = 22;
		int xPos           = margin + (canvasWidth / lineSize) * b;
		int yPos           = canvasHeight + margin + (margin / 3);
		int padding        = 6;
		float glowSpread   = 1.3f;
		String sectorTitle = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle().toUpperCase();
		String sectorType  = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType();

		// shorten sector name
		sectorTitle = sectorTitle.replaceAll("\\s*\\bSECTOR\\b\\s*","");
		sectorTitle = sectorTitle.replaceAll("\\s*\\bCONTROLLED\\b\\s*","");
		sectorTitle = sectorTitle.replaceAll("\\s*\\bUNCHARTED\\b\\s*","");
		sectorTitle = sectorTitle.replaceAll("\\s*\\bTHE\\b\\s*","");
		sectorTitle = sectorTitle.replaceAll("\\s*\\bHOMEWORLDS\\b\\s*"," HOME");

		pg.noStroke();

		pg.textAlign( LEFT, TOP );

		// sector title label
		pg.textFont( headerFont, textSize );

		pg.fill( hudColor.get("BORDER") );
		pg.beginShape();
		pg.vertex( xPos, yPos );																				  // TL
		pg.vertex( xPos, yPos + textSize + padding );															  // BL
		pg.vertex( textSize + padding + pg.textWidth( sectorTitle ) + xPos + padding, yPos + textSize + padding );// BR
		pg.vertex( textSize + padding + pg.textWidth( sectorTitle ) + xPos + padding + textSize, yPos );		  // TR
		pg.endShape(CLOSE);

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
		noStroke();
		fill( hudColor.get("BG_DARK") ); // tape over existing glow
		int glowSize = gradient.length - 1;
		rect(xPos, yPos - (glowSize * glowSpread) + 1, (canvasWidth + margin) - xPos, glowSize * glowSpread);
		for (int s = glowSize; s >= 0; --s) {
			fill(gradient[s]);
			rect(xPos, yPos - (s * glowSpread), (canvasWidth + margin) - xPos, padding);
		}
		pg.fill(gradient[0]);
		pg.rect(xPos, yPos, (canvasWidth + margin) - xPos, padding);

		// sector title text
		pg.fill( hudColor.get("HEADERTEXT_ALT") );
		pg.text( sectorTitle, padding + textSize + xPos + padding, yPos + padding );

		// sector number label
		pg.fill( hudColor.get("BG_LIGHT") );
		pg.rect( xPos + (padding / 2), yPos + (padding / 2), textSize + (padding / 2 ), textSize );

		// sector number text
		pg.fill( hudColor.get("MAINTEXT") );
		pg.textFont( headerFontAlt, textSize );
		pg.text( Integer.toString(sectorNumber + 1), xPos + (textSize / 2), yPos + padding );

	}


	private void drawLineLabel( int a, String lineLabel, int lineSize, int lastestValue ) {

		int textSize = 13;
		int offset   = 8;
		int xPos     = (margin + (canvasWidth / lineSize) * (lineSize-1)) + offset;
		int yPos     = round(map(lastestValue, 0, ceiling, margin + canvasHeight, margin)) - offset;
		int padding  = 6;
		float keyPos;

		if (lastestValue > -1) {

			pg.noStroke();

			pg.textFont( mainFont13, textSize );
			pg.textAlign( LEFT, BOTTOM );

			keyPos = xPos + padding + pg.textWidth(lineLabel) + padding;

			// connecting line
			pg.noFill();
			pg.stroke( hudColor.get("BORDER") );
			pg.strokeJoin(ROUND);
			pg.strokeCap(ROUND);
			pg.beginShape();
			pg.vertex(xPos + offset, yPos - offset);
			pg.vertex(xPos - offset, yPos + offset);
			pg.endShape();
			pg.noStroke();

			// label
			pg.fill( hudColor.get("BORDER") );
			pg.beginShape();
			pg.vertex(xPos, yPos);
			pg.vertex(keyPos + pg.textWidth("0000"), yPos);
			pg.vertex(keyPos + pg.textWidth("0000") + padding, yPos - padding);
			pg.vertex(keyPos + pg.textWidth("0000") + padding, yPos - (padding + textSize + padding));
			pg.vertex(xPos + padding, yPos - (padding + textSize + padding));
			pg.vertex(xPos, yPos - (padding + textSize));
			pg.endShape(CLOSE);

			// label key
			pg.tint(hudColor.get("HEADERTEXT_ALT"));
			pg.fill(hudColor.get("HEADERTEXT_ALT"));
			pg.text( lineLabel, xPos + padding, yPos - padding );

			// label value
			pg.beginShape();
			pg.vertex(keyPos, yPos - 2);
			pg.vertex(keyPos + pg.textWidth("0000") - 2, yPos - 2);
			pg.vertex(keyPos + pg.textWidth("0000") + padding - 2, yPos - (padding + 2));
			pg.vertex(keyPos + pg.textWidth("0000") + padding - 2, yPos - ((padding + textSize + padding) - 2));
			pg.vertex(keyPos, yPos - ((padding + textSize + padding) - 2));
			pg.endShape(CLOSE);
			pg.fill( hudColor.get("MAINTEXT") );
			pg.textAlign(CENTER, BOTTOM);
			pg.text(
				lastestValue,
				keyPos + (((keyPos + pg.textWidth("0000") + padding - 2) - keyPos) / 2),
				yPos - padding
			);

		}

	}

}