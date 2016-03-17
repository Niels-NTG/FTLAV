package net.ntg.ftl.ui.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

import static net.ntg.ftl.FTLAdventureVisualiser.recording;
import static net.ntg.ftl.FTLAdventureVisualiser.recordingHeaders;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	// TODO constrcut Table from FTLAdventureVisualiser.recording with FTLAdventureVisualer.recodingHeaders as collumn names
	// TODO delete columns if FTLAdventureVisualser.enabledRecordingHeaders.get(collumName iterator)

	public Table constructTable() {
		Table table = new Table();
		for (int i = 0; i < recordingHeaders.length; i++) {
			table.addColumn(recordingHeaders[i]);
		}
		for (int i = 0; i < recording.size(); i++) {
			TableRow newRow = table.addRow();
			for (int k = 0; k < recordingHeaders.length; k++) {
				try {
					newRow.setString(
						recordingHeaders[k],
						recording.get(i).get(recordingHeaders[k])
					);
				} catch (NullPointerException e) {
					newRow.setString(recordingHeaders[k], "");
				}
			}
		}

		return table;
	}

	public void setup() {
		Table table = constructTable();
		for (int i = 0; i < table.getRowCount(); i++) {
			log.info(table.getRow(i));
		}
	}

}

// 	public static LinkedHashMap<String,ArrayList<Integer>> superArray = new LinkedHashMap<>();
// 	public static int ceiling = 20;

// 	int current  = 0;
// 	int previous = 0;

// 	// window
// 	public static int panelWidth;
// 	public static int panelHeight;

// 	private int canvasWidth;
// 	private int canvasHeight;
// 	private final int margin = 64; // px

// 	public static boolean refreshed = false;

// 	public static boolean captureImage = false;
// 	public static String  exportPath   = "FTLAV_######";

// 	// graphics
// 	PGraphics pg;

// 	public static boolean showTitle = true;

// 	PFont mainFont13;
// 	PFont mainFont39;
// 	PFont headerFont;
// 	PFont headerFontAlt;

// 	int[] blueGlow = {
// 		color(235, 245, 227, 255),
// 		color(69, 110, 112, 226),
// 		color(61, 94, 97, 198),
// 		color(53, 80, 81, 170),
// 		color(47, 69, 70, 141),
// 		color(44, 61, 62, 113),
// 		color(40, 54, 54, 85),
// 		color(37, 50, 49, 56),
// 		color(36, 47, 46, 28)
// 	};

// 	int[] greenGlow = {
// 		color(235, 245, 229, 255),
// 		color(66, 119, 17, 226),
// 		color(53, 106, 4, 198),
// 		color(45, 92, 1, 170),
// 		color(40, 80, 1, 141),
// 		color(36, 72, 1, 113),
// 		color(32, 65, 1, 85),
// 		color(30, 61, 1, 56),
// 		color(29, 58, 1, 25)
// 	};

// 	int[] redGlow = {
// 		color(235, 245, 229, 255),
// 		color(182, 30, 30, 226),
// 		color(162, 24, 24, 198),
// 		color(140, 19, 19, 170),
// 		color(122, 14, 14, 141),
// 		color(112, 11, 11, 113),
// 		color(100, 7, 7, 85),
// 		color(93, 7, 7, 56),
// 		color(88, 5, 5, 25)
// 	};

// 	int[] purpleGlow = {
// 		color(235, 245, 229, 255),
// 		color(130, 4, 165, 226),
// 		color(117, 1, 150, 198),
// 		color(106, 1, 135, 170),
// 		color(95, 1, 121, 141),
// 		color(90, 1, 115, 113),
// 		color(83, 1, 106, 85),
// 		color(74, 1, 95, 56),
// 		color(70, 1, 90, 25)
// 	};

// 	Map<String,Integer> hudColor = new HashMap<>();


// 	public void setup() {

// 		// window
// 		size(panelWidth, panelHeight);

// 		canvasWidth  = panelWidth  - (margin * 2);
// 		canvasHeight = panelHeight - (margin * 2);

// 		// graphics
// 		pg = createGraphics(panelWidth, panelHeight);
// 		pg.strokeJoin(ROUND);
// 		pg.strokeCap(ROUND);

// 		mainFont13    = loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-13.vlw").toString());
// 		mainFont39    = loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-39.vlw").toString());
// 		headerFont    = loadFont(ClassLoader.getSystemResource("graph/Half-Life2-22.vlw").toString());
// 		headerFontAlt = loadFont(ClassLoader.getSystemResource("graph/Half-Life1-22.vlw").toString());

// 		hudColor.put("BG_NORMAL", color(55, 45, 46));			// dark purple brown 	(background color)
// 		hudColor.put("BG_LIGHT", color(122, 100, 99));			// light purple brown	(background color)
// 		hudColor.put("BG_DARK", color(24, 20, 19));				// dark brown			(background color)
// 		hudColor.put("BORDER", color(235, 245, 229));			// white-greenish		(panel border color)
// 		hudColor.put("BUTTON", color(235, 245, 229));			// white-greenish		(button color)
// 		hudColor.put("BUTTON_ACTIVE", color(255, 230, 94));		// yellow				(button color)
// 		hudColor.put("MAINTEXT", color(255, 255, 255));			// white				(standard text color)
// 		hudColor.put("HEADERTEXT", color(65, 120, 128));		// turquoise			(standard text color)
// 		hudColor.put("HEADERTEXT_ALT", color(54, 78, 80));		// dark turquoise		(standard text color)
// 		hudColor.put("SECTOR_CIVILIAN", color(135, 199, 74));	// bright green			(sector color)
// 		hudColor.put("SECTOR_HOSTILE", color(214, 50, 50));		// bright red			(sector color)
// 		hudColor.put("SECTOR_NEBULA", color(128, 51, 210));		// pure purple			(sector color)
// 		hudColor.put("SYSTEM_ACTIVE", color(100, 255, 99));		// bright green			(system status color)
// 		hudColor.put("SYSTEM_OFF", color(211, 211, 211));		// light grey			(system status color)
// 		hudColor.put("SYSTEM_DAMAGE", color(248, 59, 51));		// bright red			(system status color)
// 		hudColor.put("SYSTEM_HACKED", color(212, 70, 253));		// magenta				(system status color)
// 		hudColor.put("SYSTEM_IONIZED", color(133, 231, 237));	// cyan					(system status color)

// 	}


// 	public void draw() {

// 		current = FTLAdventureVisualiser.gameStateArray.size() - 1;

// 		if (current > previous || current == 0 || isResized() || refreshed) {

// 			refreshed = false;

// 			// TODO mouseover+click shows seperate box about event, environment and stats of that particular beacon

// 			// TODO horizontal scroll if graph becomes too wide and too dense

// 			background(hudColor.get("BG_DARK"));

// 			pg.clear();
// 			pg = createGraphics(panelWidth, panelHeight);
// 			pg.beginDraw();

// 			// graph y labels
// 			drawYLabel();

// 			for (int a = 0; a < superArray.size(); ++a) {

// 				ArrayList<Integer> lineArray = (new ArrayList<>(superArray.values())).get(a);
// 				String lineLabel = (new ArrayList<>(superArray.keySet())).get(a).toUpperCase();

// 				// graph line
// 				int[] gradient;
// 				if (
// 					lineLabel.contains("HEALTH") ||
// 					lineLabel.contains(" SKILL") ||
// 					lineLabel.contains("TOTAL REPAIRS") ||
// 					lineLabel.contains("TOTAL COMBAT") ||
// 					lineLabel.contains("TOTAL PILOTED") ||
// 					lineLabel.contains("TOTAL JUMPS") ||
// 					lineLabel.contains("SKILLS MASTERED")
// 				) {
// 					gradient = purpleGlow;
// 				} else if (
// 					lineLabel.contains("TOTAL SHIPS DEFEATED") ||
// 					lineLabel.contains("TOTAL BEACONS EXPLORED") ||
// 					lineLabel.contains("TOTAL SCRAP COLLECTED") ||
// 					lineLabel.contains("TOTAL CREW HIRED") ||
// 					lineLabel.contains("FLEET") ||
// 					lineLabel.contains("SCORE")
// 				) {
// 					gradient = redGlow;
// 				} else {
// 					gradient = blueGlow;
// 				}

// 				for (int s = gradient.length - 1; s >= 0; --s) {

// 					pg.noFill();
// 					pg.stroke(gradient[s]);
// 					pg.strokeWeight(s == gradient.length - 1 ? 4 : 4 + (s * 2));
// 					pg.beginShape();
// 					for (int b = 0; b < lineArray.size(); ++b) {
// 						if (lineArray.get(b) > -1) {
// 							pg.vertex(
// 								margin + (canvasWidth / lineArray.size()) * b,
// 								map(lineArray.get(b), 0, ceiling, margin + canvasHeight, margin)
// 							);
// 						}
// 					}
// 					pg.endShape();

// 				}

// 				// draw label at end of line
// 				drawLineLabel(a, lineLabel, lineArray.size(), lineArray.get(lineArray.size()-1));

// 				// graph x labels
// 				for (int b = 0; b < lineArray.size(); ++b) {

// 					// sector name labels
// 					if (b == 0 ||
// 						FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber() >
// 						FTLAdventureVisualiser.gameStateArray.get(b-1).getSectorNumber()
// 					) {
// 						drawSectorLabel(b, FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber(), lineArray.size());
// 					}

// 					// beacon numbers
// 					drawBeaconLabel(b, lineArray.size());

// 				}

// 			}

// 			if (current >= 0 && showTitle) drawTitle(current);

// 			pg.endDraw();

// 			tint(255);
// 			image(pg, 0, 0);

// 		}

// 		if (captureImage) {
// 			String fileTimestamp = " " + year() + "-" + month() + "-" + day() + " " + hour() + "-" + minute();
// 			pg.save(exportPath + fileTimestamp + " (alpha).png");
// 			saveFrame(exportPath + fileTimestamp + ".png");
// 			captureImage = false;
// 		}

// 		previous = current;

// 	}


// 	private boolean isResized() {

// 		if (width != panelWidth || height != panelHeight) {
// 			panelWidth   = width;
// 			panelHeight  = height;
// 			canvasWidth  = panelWidth  - (margin * 2);
// 			canvasHeight = panelHeight - (margin * 2);
// 			pg = createGraphics(panelWidth, panelHeight);
// 			return true;
// 		} else {
// 			return false;
// 		}

// 	}


// 	// TODO design bar graph rendering
// 	// TODO draw bar graph option

// 	// TODO draw a label next to the title for each indivual crew member
// 	// TODO corosponding line color for each crewmember. These lines are thinners than the others


// 	private void drawYLabel() {

// 		pg.noStroke();
// 		pg.fill(235, 245, 227);
// 		pg.textFont(mainFont13, 13);
// 		pg.textAlign(RIGHT, BOTTOM);
// 		for (int y = 0; y < canvasHeight; ++y) {
// 			if (y % 10 == 0) {
// 				pg.text(
// 					Integer.toString(y),
// 					margin - (margin / 2),
// 					map(y, 0, ceiling, canvasHeight + margin, margin)
// 				);
// 				pg.rect(
// 					margin, map(y, 0, ceiling, canvasHeight + margin, margin),
// 					canvasWidth, 0.2f
// 				);
// 			}
// 		}

// 	}


// 	private void drawTitle(int latest) {

// 		// TODO title rendering and typography in style of ship window header title
// 		// TODO draw crew member bios next to ship name

// 		int shipNameTextSize = 39;
// 		int textSize         = 13;
// 		int padding          = 6;
// 		int offset           = margin / 2;
// 		int borderWeight     = 4;
// 		float glowSpread     = 1.3f;
// 		String shipName      = FTLAdventureVisualiser.gameStateArray.get(latest).getPlayerShipName();
// 		String shipType      = ShipDataParser.getFullShipType(latest);
// 		int score            = ShipDataParser.getCurrentScore(latest);
// 		String difficulty    = FTLAdventureVisualiser.gameStateArray.get(latest).getDifficulty().toString();
// 		String ae            = FTLAdventureVisualiser.gameStateArray.get(0).isDLCEnabled() ? " Advanced" : "";

// 		pg.pushMatrix();
// 		pg.translate(margin, margin);

// 		pg.noStroke();

// 		pg.textAlign(LEFT, TOP);

// 		pg.textFont(mainFont39, shipNameTextSize);
// 		int titleLabelWidth  = round(shipName.length() < 9 ? pg.textWidth("XXXXXXXX") : pg.textWidth(shipName) + (2 * (padding + borderWeight)));
// 		int titleLabelHeight = round((textSize * 3) + shipNameTextSize + padding);

// 		// title label
// 		for (int s = blueGlow.length - 1; s >= 0; --s) {
// 			pg.fill(hudColor.get("BG_LIGHT"));
// 			pg.stroke(blueGlow[s]);
// 			pg.strokeWeight(s == blueGlow.length - 1 ? borderWeight : borderWeight + (s * 2));
// 			pg.beginShape();
// 			pg.vertex(0, offset + padding);
// 			pg.vertex(padding, offset);
// 			pg.vertex(titleLabelWidth - padding, offset);
// 			pg.vertex(titleLabelWidth, offset + padding);
// 			pg.vertex(titleLabelWidth, offset + (titleLabelHeight - padding));
// 			pg.vertex(titleLabelWidth - padding, offset + titleLabelHeight);
// 			pg.vertex(padding, offset + titleLabelHeight);
// 			pg.vertex(0, offset + (titleLabelHeight - padding));
// 			pg.endShape(CLOSE);
// 		}
// 		pg.noStroke();

// 		// title text
// 		pg.fill(hudColor.get("MAINTEXT"));
// 		pg.text(shipName, borderWeight + padding, offset + borderWeight + padding);
// 		pg.textFont(mainFont13, textSize);
// 		pg.text(
// 			shipType+"\n"+
// 			"SCORE  "+ score+"\n"+
// 			difficulty +" "+ ae,
// 			borderWeight + padding, offset + borderWeight + shipNameTextSize
// 		);

// 		pg.popMatrix();

// 	}


// 	private void drawBeaconLabel(int b, int lineSize) {

// 		// TODO do not render all the numbers if screen pixel space is limited

// 		// TODO indicator to show if there where enemy ships, environmental hazards or BOSS at the beacon

// 		pg.noStroke();
// 		pg.fill(hudColor.get("MAINTEXT"));

// 		pg.textFont(mainFont13, 13);
// 		pg.textAlign(LEFT, BOTTOM);
// 		pg.text(
// 			FTLAdventureVisualiser.gameStateArray.get(b).getTotalBeaconsExplored(),
// 			margin + (canvasWidth / lineSize) * b,
// 			canvasHeight + margin + (margin / 4)
// 		);

// 	}


// 	private void drawSectorLabel(int b, int sectorNumber, int lineSize) {

// 		// TODO do not render sectorTitle if screenspace becomes too narrow

// 		int textSize       = 22;
// 		int padding        = 6;
// 		float glowSpread   = 1.3f;
// 		String sectorTitle = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getTitle().toUpperCase();
// 		String sectorType  = FTLAdventureVisualiser.sectorArray.get(sectorNumber).getType();

// 		// shorten sector name
// 		sectorTitle = sectorTitle.replaceAll("\\s*\\bSECTOR\\b\\s*","");
// 		sectorTitle = sectorTitle.replaceAll("\\s*\\bCONTROLLED\\b\\s*","");
// 		sectorTitle = sectorTitle.replaceAll("\\s*\\bUNCHARTED\\b\\s*","");
// 		sectorTitle = sectorTitle.replaceAll("\\s*\\bTHE\\b\\s*","");
// 		sectorTitle = sectorTitle.replaceAll("\\s*\\bHOMEWORLDS\\b\\s*"," HOME");

// 		pg.pushMatrix();
// 		pg.translate(margin + (canvasWidth / lineSize) * b, canvasHeight + margin + (margin / 3));

// 		pg.noStroke();

// 		pg.textAlign(LEFT, TOP);

// 		// sector title label
// 		pg.textFont(headerFont, textSize);

// 		pg.fill(hudColor.get("BORDER"));
// 		pg.beginShape();
// 		pg.vertex(0, 0);																		// TL
// 		pg.vertex(0, textSize + padding);														// BL
// 		pg.vertex(textSize + padding + pg.textWidth(sectorTitle) + padding, textSize + padding);// BR
// 		pg.vertex(textSize + padding + pg.textWidth(sectorTitle) + padding + textSize, 0);		// TR
// 		pg.endShape(CLOSE);

// 		// glow color
// 		int[] gradient = null;
// 		if (null != sectorType) switch (sectorType) {
//                 case "CIVILIAN":
//                     gradient = greenGlow;
//                     break;
//                 case "HOSTILE":
//                     gradient = redGlow;
//                     break;
//                 case "NEBULA":
//                     gradient = purpleGlow;
//                     break;
//                 default:
//                     gradient = blueGlow;
//                     break;
//             }

// 		// apply glow effect
// 		noStroke();
// 		fill(hudColor.get("BG_DARK")); // tape over existing glow
// 		int glowSize = gradient.length - 1;
// 		rect(0, -(glowSize * glowSpread), canvasWidth + margin, glowSize * glowSpread);
// 		for (int s = glowSize; s >= 0; --s) {
// 			fill(gradient[s]);
// 			rect(0, -(s * glowSpread), canvasWidth + margin, padding);
// 		}
// 		pg.fill(gradient[0]);
// 		pg.rect(0, 0, canvasWidth + margin, padding);

// 		// sector title text
// 		pg.fill(hudColor.get("HEADERTEXT_ALT"));
// 		pg.text(sectorTitle, padding + textSize + padding, padding);

// 		// sector number label
// 		pg.fill(hudColor.get("BG_LIGHT"));
// 		pg.rect(padding / 2, padding / 2, textSize + (padding / 2), textSize);

// 		// sector number text
// 		pg.fill(hudColor.get("MAINTEXT"));
// 		pg.textFont(headerFontAlt, textSize);
// 		pg.text(Integer.toString(sectorNumber + 1), textSize / 2, padding);

// 		pg.popMatrix();

// 	}


// 	private void drawLineLabel(int a, String lineLabel, int lineSize, int lastestValue) {

// 		if (lastestValue > -1) {

// 			int textSize = 13;
// 			int offset   = 8;
// 			int padding  = 6;

// 			pg.pushMatrix();
// 			pg.translate(
// 				margin + (canvasWidth / lineSize) * (lineSize - 1) + offset,
// 				round(map(lastestValue, 0, ceiling, margin + canvasHeight, margin)) - offset
// 			);

// 			pg.textFont(mainFont13, textSize);
// 			pg.textAlign(LEFT, BOTTOM);

// 			float keyPos = padding + pg.textWidth(lineLabel) + padding;

// 			// connecting line
// 			pg.noFill();
// 			pg.stroke(hudColor.get("BORDER"));
// 			pg.beginShape();
// 			pg.vertex(offset, -offset);
// 			pg.vertex(-offset, offset);
// 			pg.endShape();

// 			// label
// 			pg.noStroke();
// 			pg.fill(hudColor.get("BORDER"));
// 			pg.beginShape();
// 			pg.vertex(0, 0);
// 			pg.vertex(keyPos + pg.textWidth("0000"), 0);
// 			pg.vertex(keyPos + pg.textWidth("0000") + padding, -padding);
// 			pg.vertex(keyPos + pg.textWidth("0000") + padding, -(padding + textSize + padding));
// 			pg.vertex(padding, -(padding + textSize + padding));
// 			pg.vertex(0, -(padding + textSize));
// 			pg.endShape(CLOSE);

// 			// label key
// 			pg.tint(hudColor.get("HEADERTEXT_ALT"));
// 			pg.fill(hudColor.get("HEADERTEXT_ALT"));
// 			pg.text(lineLabel, padding, -padding);

// 			// label value
// 			pg.beginShape();
// 			pg.vertex(keyPos, -2);
// 			pg.vertex(keyPos + pg.textWidth("0000") - 2, -2);
// 			pg.vertex(keyPos + pg.textWidth("0000") + padding - 2, -(padding + 2));
// 			pg.vertex(keyPos + pg.textWidth("0000") + padding - 2, -((padding + textSize + padding) - 2));
// 			pg.vertex(keyPos, -((padding + textSize + padding) - 2));
// 			pg.endShape(CLOSE);
// 			pg.fill(hudColor.get("MAINTEXT"));
// 			pg.textAlign(CENTER, BOTTOM);
// 			pg.text(
// 				lastestValue,
// 				keyPos + (((keyPos + pg.textWidth("0000") + padding - 2) - keyPos) / 2),
// 				-padding
// 			);

// 			pg.popMatrix();

// 		}

// 	}

// }