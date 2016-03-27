package net.ntg.ftl.ui.graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;

import static net.ntg.ftl.FTLAdventureVisualiser.recording;
import static net.ntg.ftl.FTLAdventureVisualiser.recordingHeaders;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	// TODO make complete version in Processing
	// TODO port Processing version to this class

	// TODO delete columns if FTLAdventureVisualser.enabledRecordingHeaders.get(collumName iterator)

	Table table;
	float maxTableValue;
	int startIndex = 0;

	// graphics
	int margin = 32; // px
	int jumpSize = 32; // px
	int graphWidth;
	int graphHeigth;

	PFont mainFont13;
	PFont mainFont39;
	PFont headerFont;
	PFont headerFontAlt;

	PImage crewCombat;
	PImage crewEngine;
	PImage crewHealth;
	PImage crewPilot;
	PImage crewRepairs;
	PImage crewShield;
	PImage crewWeapon;

	final int BG_NORMAL = color(55, 45, 46);			// dark purple brown 	(background color)
	final int BG_LIGHT = color(122, 100, 99);			// light purple brown	(background color)
	final int BG_DARK = color(24, 20, 19);			// dark brown			(background color)
	final int BORDER = color(235, 245, 229);			// white-greenish		(panel border color)
	final int BUTTON = color(235, 245, 229);			// white-greenish		(button color)
	final int BUTTON_ACTIVE = color(255, 230, 94);	// yellow				(button color)
	final int MAINTEXT = color(255, 255, 255);		// white				(standard text color)
	final int HEADERTEXT = color(65, 120, 128);		// turquoise			(standard text color)
	final int HEADERTEXT_ALT = color(54, 78, 80);		// dark turquoise		(standard text color)
	final int SECTOR_CIVILIAN = color(135, 199, 74);	// bright green			(sector color)
	final int SECTOR_HOSTILE = color(214, 50, 50);	// bright red			(sector color)
	final int SECTOR_NEBULA = color(128, 51, 210);	// pure purple			(sector color)
	final int SYSTEM_ACTIVE = color(100, 255, 99);	// bright green			(system status color)
	final int SYSTEM_OFF = color(211, 211, 211);		// light grey			(system status color)
	final int SYSTEM_DAMAGE = color(248, 59, 51);		// bright red			(system status color)
	final int SYSTEM_HACKED = color(212, 70, 253);	// magenta				(system status color)
	final int SYSTEM_IONIZED = color(133, 231, 237);	// cyan					(system status color)

	int[] GLOW_BLUE = {
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
	int[] GLOW_GREEN = {
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
	int[] GLOW_RED = {
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
	int[] GLOW_PURPLE = {
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

		maxTableValue = 1000f; // TODO figure efficient way to get max value table

		// graphics
		mainFont13		= loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-13.vlw").toString());
		mainFont39		= loadFont(ClassLoader.getSystemResource("graph/C&CRedAlertINET-39.vlw").toString());
		headerFont		= loadFont(ClassLoader.getSystemResource("graph/Half-Life2-22.vlw").toString());
		headerFontAlt	= loadFont(ClassLoader.getSystemResource("graph/Half-Life1-22.vlw").toString());

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
					map(dataPoints[i], 0, maxTableValue, graphHeigth, 0) - gradient.length
				);
			}
			glowLine.endShape();
		}

		glowLine.endDraw();
		return glowLine;

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

				graphics.textFont(mainFont13, 13);
				int lastIndexSectorNumber = lastIndexOfIntArray(sectorNumber, sectorNumber[i]);
				int lastBeaconNumberTextWidth = (int)graphics.textWidth(Integer.toString(beaconNumber[lastIndexSectorNumber]));
				sectorName[i] = sectorName[i].toUpperCase().replaceAll("\\b(SECTOR|CONTROLLED|UNCHARTED|HOMEWORLDS|THE)\\b", "").trim();
				graphics.textFont(headerFontAlt, 22);
				int sectorNumberTextWidth = (int)graphics.textWidth(Integer.toString(sectorNumber[i])) * 2;
				graphics.textFont(headerFont, 22);
				int sectorNameTextWidth = (int)graphics.textWidth(sectorName[i]);

				// draw sectorName box
				graphics.fill(BORDER);
				graphics.beginShape();
				graphics.vertex(0, 18);
				graphics.vertex(jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, 18);
				graphics.vertex(jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, 20);
				graphics.vertex(sectorNumberTextWidth + sectorNameTextWidth + 28, 20);
				graphics.vertex(sectorNumberTextWidth + sectorNameTextWidth + 6, 46);
				graphics.vertex(6, 46);
				graphics.vertex(0, 40);
				graphics.endShape();

				// draw sectorName text
				graphics.fill(HEADERTEXT_ALT);
				graphics.text(sectorName[i], sectorNumberTextWidth + 4, 22);

				// draw sectorNumber box
				graphics.fill(HEADERTEXT_ALT);
				graphics.beginShape();
				graphics.vertex(2, 20);
				graphics.vertex(sectorNumberTextWidth, 20);
				graphics.vertex(sectorNumberTextWidth, 44);
				graphics.vertex(8, 44);
				graphics.vertex(2, 38);
				graphics.endShape();

				// draw sectorNumber text
				graphics.fill(MAINTEXT);
				graphics.textAlign(CENTER, TOP);
				graphics.textFont(headerFontAlt, 22);
				graphics.text(sectorNumber[i], sectorNumberTextWidth / 2 + 2, 24);

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
					graphics.rect(0, 18, jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, (float) (-g * 1.6));
				}

				// reset
				graphics.textAlign(LEFT, TOP);

			}

			graphics.textFont(mainFont13, 13);
			graphics.fill(MAINTEXT);
			graphics.text(beaconNumber[i], 0, (float) 6.5);

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

		graphics.textFont(mainFont13, 13);
		int lastChangedTimestampTextWidth = (int)graphics.textWidth(lastChangedTimestamp) + 16;
		graphics.textFont(mainFont39, 39);
		int shipNameTextWidth = (int)graphics.textWidth(shipName) + 32;

		// container
		graphics.fill(BORDER);
		graphics.beginShape();
		graphics.vertex(4, 0);
		graphics.vertex(lastChangedTimestampTextWidth + 4, 0);
		graphics.vertex(lastChangedTimestampTextWidth + 8, 4);
		graphics.vertex(lastChangedTimestampTextWidth + 8, 34);
		graphics.vertex(shipNameTextWidth + 4, 34);
		graphics.vertex(shipNameTextWidth + 8, 38);
		graphics.vertex(shipNameTextWidth + 8, 77);
		graphics.vertex(shipNameTextWidth + 4, 81);
		graphics.vertex(4, 81);
		graphics.vertex(0, 77);
		graphics.vertex(0, 4);
		graphics.endShape();

		// shipName box
		graphics.fill(BG_NORMAL);
		graphics.beginShape();
		graphics.vertex(8, 38);
		graphics.vertex(shipNameTextWidth, 38);
		graphics.vertex(shipNameTextWidth + 4, 42);
		graphics.vertex(shipNameTextWidth + 4, 73);
		graphics.vertex(shipNameTextWidth, 77);
		graphics.vertex(8, 77);
		graphics.vertex(4, 73);
		graphics.vertex(4, 42);
		graphics.endShape();

		// info text
		graphics.fill(BG_DARK);
		graphics.textAlign(LEFT, CENTER);
		graphics.textFont(mainFont13, 13);
		graphics.text("FTLAV 3\n" + lastChangedTimestamp, 8, 18);

		// shipName text
		graphics.fill(MAINTEXT);
		graphics.textAlign(CENTER, CENTER);
		graphics.textFont(mainFont39, 39);
		graphics.text(shipName, 4 + shipNameTextWidth / 2, (float) 57.5);

		graphics.endDraw();
		return graphics;

	}


	// data util
	private int[] getDataRange(int[] dataPoints) {
		int count = dataPoints.length;
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		dataPoints = subset(dataPoints, constrain(startIndex, 0, dataPoints.length - count), count);
		return dataPoints;
	}
	private String[] getDataRange(String[] dataPoints) {
		int count = dataPoints.length;
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		dataPoints = subset(dataPoints, constrain(startIndex, 0, dataPoints.length - count), count);
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
				startIndex = constrain(startIndex, 0, table.getRowCount()); // TODO fix max value
				redraw();
			}
		}
	}

}