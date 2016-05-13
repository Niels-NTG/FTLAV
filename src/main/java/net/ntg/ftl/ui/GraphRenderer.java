package net.ntg.ftl.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;

import java.util.Map;

import static net.ntg.ftl.FTLAdventureVisualiser.recording;
import static net.ntg.ftl.FTLAdventureVisualiser.recordingHeaders;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	// TODO make complete version in Processing

	// TODO delete columns if FTLAdventureVisualser.enabledRecordingHeaders.get(collumName iterator)

	private Table table;
	private int maxTableValue;
	private int startIndex = 0;
	private int endIndex;

	// graphics
	private final int margin = 32; // px
	private int jumpSize = 32; // px
	private final int glowSpread = 9; // px
	private int graphWidth;
	private int graphHeigth;

	private PFont mainFont16;
	private PFont mainFont32;
	private PFont headerFont11;
	private PFont headerFontAlt11;
	private PFont headerFont22;
	private PFont headerFontAlt22;

	private PImage crewCombat;
	private PImage crewEngine;
	private PImage crewHealth;
	private PImage crewPilot;
	private PImage crewRepairs;
	private PImage crewShield;
	private PImage crewWeapon;

	private final int BG_NORMAL = color(50, 43, 43);			// dark purple brown 	(background color)
	private final int BG_LIGHT = color(130, 109, 106);			// light purple brown	(background color)
	private final int BG_DARK = color(31, 35, 35);				// dark turquoise		(background color)
	private final int BORDER = color(230, 244, 222);			// white-greenish		(panel border color)
	private final int BUTTON = color(230, 244, 222);			// white-greenish		(button color)
	private final int BUTTON_ENABLED = color(253, 227, 77);		// yellow				(button color)
	private final int BUTTON_ACTIVE = color(148, 155, 143);		// greenish grey		(button color)
	private final int BUTTON_TEXT = color(20, 37, 39);			// dark turquoise		(button text color)
	private final int MAINTEXT = color(230, 244, 222);			// white-greenish		(standard text color)
	private final int SECTOR_CIVILIAN = color(118, 191, 58);	// bright green			(sector color)
	private final int SECTOR_HOSTILE = color(202, 29, 37);		// bright red			(sector color)
	private final int SECTOR_NEBULA = color(107, 18, 200);		// purple				(sector color)
	private final int SYSTEM_ACTIVE = color(89, 255, 82);		// bright green			(system status color)
	private final int SYSTEM_OFF = color(201, 201, 201);		// light grey			(system status color)
	private final int SYSTEM_DAMAGE = color(245, 56, 53);		// bright red			(system status color)
	private final int SYSTEM_HACKED = color(193, 27, 253);		// magenta				(system status color)
	private final int SYSTEM_IONIZED = color(119, 245, 253);	// cyan					(system status color)

	private final int[] GLOW_BLUE = {
		BORDER,
		color(69, 110, 112, 226),
		color(61, 94, 97, 198),
		color(53, 80, 81, 170),
		color(47, 69, 70, 141),
		color(44, 61, 62, 113),
		color(40, 54, 54, 85),
		color(37, 50, 49, 56),
		color(36, 47, 46, 28)
	};
	private final int[] GLOW_GREEN = {
		BORDER,
		color(66, 119, 17, 226),
		color(53, 106, 4, 198),
		color(45, 92, 1, 170),
		color(40, 80, 1, 141),
		color(36, 72, 1, 113),
		color(32, 65, 1, 85),
		color(30, 61, 1, 56),
		color(29, 58, 1, 25)
	};
	private final int[] GLOW_RED = {
		BORDER,
		color(182, 30, 30, 226),
		color(162, 24, 24, 198),
		color(140, 19, 19, 170),
		color(122, 14, 14, 141),
		color(112, 11, 11, 113),
		color(100, 7, 7, 85),
		color(93, 7, 7, 56),
		color(88, 5, 5, 25)
	};
	private final int[] GLOW_PURPLE = {
		BORDER,
		color(130, 4, 165, 226),
		color(117, 1, 150, 198),
		color(106, 1, 135, 170),
		color(95, 1, 121, 141),
		color(90, 1, 115, 113),
		color(83, 1, 106, 85),
		color(74, 1, 95, 56),
		color(70, 1, 90, 25)
	};


	public void setup() {

		// window
		size(800, 800);

		graphWidth = width - (margin + margin);
		graphHeigth = height - (margin + margin + margin);

		// data
		table = new Table();
		for (String recordingHeader : recordingHeaders) {
			table.addColumn(recordingHeader);
		}
		for (Map<String, String> aRecording : recording) {
			TableRow newRow = table.addRow();
			for (String recordingHeader : recordingHeaders) {
				try {
					newRow.setString(
						recordingHeader,
						aRecording.get(recordingHeader)
					);
				} catch (NullPointerException e) {
					newRow.setString(recordingHeader, "");
				}
			}
		}

		int[] columnMax = new int[table.getColumnCount()];
		for (int i = 0; i < table.getColumnCount(); ++i) {
			columnMax[i] = max(table.getIntColumn(i));
		}
		maxTableValue = max(columnMax);

		// graphics
		mainFont16		= loadFont(ClassLoader.getSystemResource("graph/JustinFont8-16.vlw").toString());
		mainFont32		= loadFont(ClassLoader.getSystemResource("graph/JustinFont8-32.vlw").toString());
		headerFont11	= loadFont(ClassLoader.getSystemResource("graph/Half-Life2-11.vlw").toString());
		headerFontAlt11	= loadFont(ClassLoader.getSystemResource("graph/Half-Life1-11.vlw").toString());
		headerFont22	= loadFont(ClassLoader.getSystemResource("graph/Half-Life2-22.vlw").toString());
		headerFontAlt22	= loadFont(ClassLoader.getSystemResource("graph/Half-Life1-22.vlw").toString());

		crewCombat	= loadImage(ClassLoader.getSystemResource("graph/crew-combat.gif").toString());
		crewEngine	= loadImage(ClassLoader.getSystemResource("graph/crew-engine.gif").toString());
		crewHealth	= loadImage(ClassLoader.getSystemResource("graph/crew-health.gif").toString());
		crewPilot	= loadImage(ClassLoader.getSystemResource("graph/crew-pilot.gif").toString());
		crewRepairs	= loadImage(ClassLoader.getSystemResource("graph/crew-repair.gif").toString());
		crewShield	= loadImage(ClassLoader.getSystemResource("graph/crew-shield.gif").toString());
		crewWeapon	= loadImage(ClassLoader.getSystemResource("graph/crew-weapon.gif").toString());

	}


	public void draw() {

		// background
		background(BG_DARK);

		// graphics with transparant background
		image(generateGraphics(), 0, 0);

		// overlay graphics (mouseover, etc)

		noLoop();

	}


	private PGraphics generateGraphics() {

		PGraphics pg = createGraphics(width, height);
		pg.beginDraw();

		pg.image(drawStandardAxisX(), margin, margin);
		try {
			pg.image(drawGraphLine(getDataRange(table.getIntColumn("SCORE")), GLOW_BLUE), margin, margin);
			pg.image(drawGraphLine(getDataRange(table.getIntColumn("TOTAL SCRAP COLLECTED")), GLOW_PURPLE), margin, margin);
			pg.image(drawGraphLine(getDataRange(table.getIntColumn("FLEET ADVANCEMENT")), GLOW_RED), margin, margin);
		} catch (IllegalArgumentException e) {
			println(e);
		}
		pg.image(drawStandardAxisY(), margin, margin + graphHeigth);

		pg.image(drawHeader(), 1, 1);

		pg.endDraw();
		return pg;

	}


	private PGraphics drawGraphLine(int[] dataPoints) {
		return drawGraphLine(dataPoints, GLOW_BLUE);
	}
	private PGraphics drawGraphLine(int[] dataPoints, int[] gradient) {

		PGraphics glowLine = createGraphics(graphWidth, graphHeigth);
		glowLine.beginDraw();
		glowLine.strokeCap(ROUND);
		glowLine.strokeJoin(ROUND);

		glowLine.noFill();
		for (int g = gradient.length - 1; g >= 0; --g) {
			glowLine.beginShape();
			glowLine.stroke(gradient[g]);
			glowLine.strokeWeight(g == gradient.length - 1 ? 2 : 2 + (g * 2));
			for (int i = 0; i < dataPoints.length; ++i) {
				glowLine.vertex(
					jumpSize * i,
					map(dataPoints[i], 0, maxTableValue, graphHeigth, 0) - glowSpread
				);
			}
			glowLine.endShape();
		}

		glowLine.endDraw();
		return glowLine;

	}


	private PGraphics drawStandardAxisX() {

		PGraphics graphics = createGraphics(graphWidth, graphHeigth);
		graphics.beginDraw();
		graphics.strokeWeight(0.8f);
		graphics.textFont(mainFont32, 16);
		graphics.textAlign(LEFT, BOTTOM);
		graphics.fill(MAINTEXT);

		int lastY = 0;
		for (int i = 0; i < maxTableValue; i++) {
			int y = (int)map(i, 0, maxTableValue, graphHeigth, 0) - glowSpread;
			if (y != lastY && y % jumpSize == 0) {
				graphics.stroke(BORDER);
				graphics.line(0, y, graphWidth - jumpSize, y);
				graphics.noStroke();
				graphics.text(i, 0, y);
			}
			lastY = y;
		}

		graphics.endDraw();
		return graphics;

	}


	private PGraphics drawStandardAxisY() {

		int[] beaconNumber = getDataRange(table.getIntColumn("BEACON"));
		int[] sectorNumber = getDataRange(table.getIntColumn("SECTOR NUMBER"));
		String[] sectorType = getDataRange(table.getStringColumn("SECTOR TYPE"));
		String[] sectorName = getDataRange(table.getStringColumn("SECTOR TITLE"));

		int lastSectorNumber = sectorNumber[0];

		PGraphics graphics = createGraphics(graphWidth, margin * 2);
		graphics.beginDraw();
		graphics.noStroke();
		graphics.textAlign(LEFT, TOP);

		for (int i = 0; i < beaconNumber.length; ++i) {

			graphics.pushMatrix();
			graphics.translate(jumpSize * i, 0);

			// draw sector label
			if (i == 0 || sectorNumber[i] != lastSectorNumber) {

				graphics.textFont(mainFont32, 16);
				int lastIndexSectorNumber = lastIndexOfIntArray(sectorNumber, sectorNumber[i]);
				int lastBeaconNumberTextWidth = (int)graphics.textWidth(Integer.toString(beaconNumber[lastIndexSectorNumber]));
				sectorName[i] = sectorName[i].toUpperCase().replaceAll("\\b(SECTOR|CONTROLLED|UNCHARTED|HOMEWORLDS|THE)\\b", "").trim();
				graphics.textFont(headerFontAlt11, 11);
				int sectorNumberTextWidth = (int)graphics.textWidth(Integer.toString(sectorNumber[i]));
				sectorNumberTextWidth = sectorNumberTextWidth < 22 ? 22 : sectorNumberTextWidth * 2;
				graphics.textFont(headerFont11, 11);
				int sectorNameTextWidth = (int)graphics.textWidth(sectorName[i]);

				// draw sectorName box
				graphics.fill(BORDER);
				graphics.beginShape();
				graphics.vertex(0, 18);
				graphics.vertex(jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, 18);
				graphics.vertex(jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, 20);
				graphics.vertex(sectorNumberTextWidth + sectorNameTextWidth + 28, 20);
				graphics.vertex(sectorNumberTextWidth + sectorNameTextWidth + 6, 35);
				graphics.vertex(6, 35);
				graphics.vertex(0, 29);
				graphics.endShape();

				// draw sectorName text
				graphics.fill(BUTTON_TEXT);
				graphics.text(sectorName[i], sectorNumberTextWidth + 4, 22);

				// draw sectorNumber box
				graphics.fill(BUTTON_TEXT);
				graphics.beginShape();
				graphics.vertex(2, 20);
				graphics.vertex(sectorNumberTextWidth, 20);
				graphics.vertex(sectorNumberTextWidth, 33);
				graphics.vertex(8, 33);
				graphics.vertex(2, 27);
				graphics.endShape();

				// draw sectorNumber text
				graphics.fill(MAINTEXT);
				graphics.textAlign(CENTER, TOP);
				graphics.textFont(headerFontAlt11, 11);
				graphics.text(sectorNumber[i], sectorNumberTextWidth / 2 + 2, 22);

				// draw glow accent
				int[] gradient;
				switch (sectorType[i]) {
					case "CIVILIAN": gradient = GLOW_GREEN; break;
					case "HOSTILE" : gradient = GLOW_RED; break;
					case "NEBULA"  : gradient = GLOW_PURPLE; break;
					default: gradient = GLOW_BLUE; break;
				}
				for (int g = 1; g < gradient.length; ++g) {
					graphics.fill(gradient[g]);
					graphics.rect(0, 18, jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, -g * 1.6f);
				}

				// reset
				graphics.textAlign(LEFT, TOP);

			}

			graphics.textFont(mainFont32, 16);
			graphics.fill(MAINTEXT);
			graphics.text(beaconNumber[i], 0, 6.5f);

			graphics.popMatrix();

			lastSectorNumber = sectorNumber[i];

		}

		graphics.endDraw();
		return graphics;

	}


	private PGraphics drawHeader() {

		int lastRowIndex = table.getRowCount() - 1;
		String lastChangedTimestamp = table.getString(lastRowIndex, "TIME");
		String shipName = table.getString(lastRowIndex, "SHIP NAME");
		String shipType = table.getString(lastRowIndex, "SHIP TYPE");
		String difficulty = table.getString(lastRowIndex, "DIFFICULTY");
		String ae = "AE content " + table.getString(lastRowIndex, "AE CONTENT");

		PGraphics graphics = createGraphics(width, height);
		graphics.beginDraw();

		graphics.textFont(mainFont32, 16);
		int lastChangedTimestampTextWidth = (int)graphics.textWidth(lastChangedTimestamp) + 8;
		graphics.textFont(mainFont32, 32);
		int shipNameTextWidth = (int)graphics.textWidth(shipName) + 32;

		// container
		graphics.fill(BORDER);
		graphics.beginShape();
		graphics.vertex(4, 0);
		graphics.vertex(lastChangedTimestampTextWidth + 4, 0);
		graphics.vertex(lastChangedTimestampTextWidth + 8, 4);
		graphics.vertex(lastChangedTimestampTextWidth + 8, 21);
		graphics.vertex(shipNameTextWidth + 4, 21);
		graphics.vertex(shipNameTextWidth + 8, 25);
		graphics.vertex(shipNameTextWidth + 8, 64);
		graphics.vertex(shipNameTextWidth + 4, 68);
		graphics.vertex(4, 68);
		graphics.vertex(0, 64);
		graphics.vertex(0, 4);
		graphics.endShape();

		// shipName box
		graphics.fill(BG_NORMAL);
		graphics.beginShape();
		graphics.vertex(8, 25);
		graphics.vertex(shipNameTextWidth, 25);
		graphics.vertex(shipNameTextWidth + 4, 29);
		graphics.vertex(shipNameTextWidth + 4, 60);
		graphics.vertex(shipNameTextWidth, 64);
		graphics.vertex(8, 64);
		graphics.vertex(4, 60);
		graphics.vertex(4, 29);
		graphics.endShape();

		// info text
		graphics.fill(BG_DARK);
		graphics.textAlign(LEFT, CENTER);
		graphics.textFont(mainFont32, 16);
		graphics.text(lastChangedTimestamp, 8, 13);

		// shipName text
		graphics.fill(MAINTEXT);
		graphics.textAlign(CENTER, CENTER);
		graphics.textFont(mainFont32, 32);
		graphics.text(shipName, 4 + shipNameTextWidth / 2, 44.5f);

		graphics.endDraw();
		return graphics;

	}


	// data util
	private int[] getDataRange(int[] dataPoints) {
		int count = dataPoints.length;
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		endIndex = dataPoints.length - count;
		dataPoints = subset(dataPoints, constrain(startIndex, 0, endIndex), count);
		return dataPoints;
	}
	private String[] getDataRange(String[] dataPoints) {
		int count = dataPoints.length;
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		endIndex = dataPoints.length - count;
		dataPoints = subset(dataPoints, constrain(startIndex, 0, endIndex), count);
		return dataPoints;
	}


	private int indexOfIntArray(int[] array, int key) {
		int returnvalue = -1;
		for (int i = 0; i < array.length; ++i) {
			if (key == array[i]) {
				returnvalue = i;
				break;
			}
		}
		return returnvalue;
	}
	private int lastIndexOfIntArray(int[] array, int key) {
		int returnvalue = -1;
		for (int i = array.length - 1; i >= 0; --i) {
			if (key == array[i]) {
				returnvalue = i;
				break;
			}
		}
		return returnvalue;
	}


	// Processing environment
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == LEFT || keyCode == RIGHT) {
				if (keyCode == LEFT) startIndex--;
				if (keyCode == RIGHT) startIndex++;
				startIndex = constrain(startIndex, 0, endIndex);
				redraw();
			}
		}
	}

}