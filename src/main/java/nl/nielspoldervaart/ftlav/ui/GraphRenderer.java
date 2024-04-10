package nl.nielspoldervaart.ftlav.ui;

import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import nl.nielspoldervaart.ftlav.data.TableRow;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;


@Slf4j
public class GraphRenderer extends PApplet {

	// TODO make complete version in Processing

	// TODO delete columns if FTLAdventureVisualser.enabledRecordingHeaders.get(columnName iterator)

	private static final Preferences prefs = Preferences.userNodeForPackage(FTLAdventureVisualiser.class);

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
		size(1200, 700);

		graphWidth = width - (margin + margin);
		graphHeigth = height - (margin + margin + margin);

		// Data
		ArrayList<Integer> columnMax = new ArrayList<>();
		for (String columnName : DataUtil.getTableHeaders(int.class)) {
			columnMax.addAll(DataUtil.extractIntColumn(columnName));
		}
		maxTableValue = Collections.max(columnMax);

		// Preference change event listener
		prefs.addPreferenceChangeListener(new PreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent evt) {
				log.info("{}: {}", evt.getKey(), evt.getNewValue());
			}
		});

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

		graphWidth = width - (margin + margin);
		graphHeigth = height - (margin + margin + margin);

		// background
		background(BG_DARK);

		// graphics with transparant background
		image(generateGraphics(), 0, 0);

		// overlay graphics (mouseover, etc.)

		noLoop();

	}


	private PGraphics generateGraphics() {

		PGraphics pg = createGraphics(width, height);
		pg.beginDraw();

		pg.image(drawStandardAxisX(), margin, margin);
		try {
			pg.image(
				drawGraphLine(
					getDataRangeInt(
						DataUtil.extractIntColumn("totalScore")
					),
					GLOW_BLUE
				),
				margin,
				margin
			);
			pg.image(
				drawGraphLine(
					getDataRangeInt(
						DataUtil.extractIntColumn("totalScrapCollected")
					),
					GLOW_PURPLE),
				margin,
				margin
			);
			pg.image(
				drawGraphLine(
					getDataRangeInt(
						DataUtil.extractIntColumn("fleetAdvancement")
					),
					GLOW_RED
				),
				margin,
				margin
			);
		} catch (IllegalArgumentException e) {
			println(e);
		}
		pg.image(drawStandardAxisY(), margin, margin + graphHeigth);

		pg.image(drawHeader(), 1, 1);

		pg.endDraw();
		return pg;

	}


	private PGraphics drawGraphLine(List<Integer> dataPoints) {
		return drawGraphLine(dataPoints, GLOW_BLUE);
	}
	private PGraphics drawGraphLine(List<Integer> dataPoints, int[] gradient) {

		PGraphics glowLine = createGraphics(graphWidth, graphHeigth);
		glowLine.beginDraw();
		glowLine.strokeCap(ROUND);
		glowLine.strokeJoin(ROUND);

		glowLine.noFill();
		for (int g = gradient.length - 1; g >= 0; --g) {
			glowLine.beginShape();
			glowLine.stroke(gradient[g]);
			glowLine.strokeWeight(g == gradient.length - 1 ? 2 : 2 + (g * 2));
			for (int i = 0; i < dataPoints.size(); ++i) {
				glowLine.vertex(
					jumpSize * i,
					map(dataPoints.get(i), 0, maxTableValue, graphHeigth, 0) - glowSpread
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
		graphics.textFont(mainFont16);
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

		List<Integer> beaconNumber = getDataRangeInt(
			DataUtil.extractIntColumn("beaconNumber")
		);
		List<Integer> sectorNumber = getDataRangeInt(
			DataUtil.extractIntColumn("sectorNumber")
		);
		List<String> sectorType = getDataRangeString(
			DataUtil.extractStringColumn("sectorType")
		);
		List<String> sectorName = getDataRangeString(
			DataUtil.extractStringColumn("sectorName")
		);

		int lastSectorNumber = sectorNumber.get(0);

		PGraphics graphics = createGraphics(graphWidth, margin * 2);
		graphics.beginDraw();
		graphics.noStroke();
		graphics.textAlign(LEFT, TOP);

		for (int i = 0; i < beaconNumber.size(); ++i) {

			graphics.pushMatrix();
			graphics.translate(jumpSize * i, 0);

			// draw sector label
			if (i == 0 || sectorNumber.get(i) != lastSectorNumber) {

				graphics.textFont(mainFont16);
				int lastIndexSectorNumber = sectorNumber.lastIndexOf(sectorNumber.get(i));
				int lastBeaconNumberTextWidth = (int)graphics.textWidth(
					Integer.toString(beaconNumber.get(lastIndexSectorNumber))
				);
				sectorName.set(
					i,
					sectorName.get(i).toUpperCase().replaceAll("\\b(SECTOR|CONTROLLED|UNCHARTED|HOMEWORLDS|THE)\\b", "").trim()
				);
				graphics.textFont(headerFontAlt11);
				int sectorNumberTextWidth = (int)graphics.textWidth(
					Integer.toString(sectorNumber.get(i))
				);
				sectorNumberTextWidth = sectorNumberTextWidth < 22 ? 22 : sectorNumberTextWidth * 2;
				graphics.textFont(headerFont11);
				int sectorNameTextWidth = (int)graphics.textWidth(sectorName.get(i));

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
				graphics.text(sectorName.get(i), sectorNumberTextWidth + 4, 22);

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
				graphics.textFont(headerFontAlt11);
				graphics.text(sectorNumber.get(i), sectorNumberTextWidth / 2 + 2, 22);

				// draw glow accent
				int[] gradient;
				switch (sectorType.get(i)) {
					case "CIVILIAN": gradient = GLOW_GREEN; break;
					case "HOSTILE" : gradient = GLOW_RED; break;
					case "NEBULA"  : gradient = GLOW_PURPLE; break;
					default: gradient = GLOW_BLUE; break;
				}
				for (int g = 1; g < gradient.length; ++g) {
					graphics.fill(gradient[g]);
					graphics.rect(
						0,
						18,
						jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth,
						-g * 1.6f
					);
				}

				// reset
				graphics.textAlign(LEFT, TOP);

			}

			graphics.textFont(mainFont16);
			graphics.fill(MAINTEXT);
			graphics.text(beaconNumber.get(i), 0, 6.5f);

			graphics.popMatrix();

			lastSectorNumber = sectorNumber.get(i);

		}

		graphics.endDraw();
		return graphics;

	}


	private PGraphics drawHeader() {

		TableRow lastRow = DataUtil.getLastRecord();
		String lastChangedTimestamp = lastRow.getTime();
		String shipName = lastRow.getShipName();
		String shipType = lastRow.getShipType();
		String difficulty = lastRow.getDifficulty();
		String ae = "AE content " + (lastRow.isAEContentEnabled() ? "enabled" : "disabled");

		PGraphics graphics = createGraphics(width, height);
		graphics.beginDraw();

		graphics.image(drawBox(lastChangedTimestamp, mainFont16, BUTTON_TEXT, BUTTON, false), 4, 4);

		graphics.image(drawBox(shipName, mainFont32, MAINTEXT, BG_DARK, true), 4, 30);

		graphics.endDraw();
		return graphics;

	}


	private PGraphics drawBox(String text, PFont font, int textColor, int backgroundColor, boolean hasBorder) {

		PGraphics graphics = createGraphics(width, height);
		graphics.beginDraw();

		graphics.textFont(font);
		graphics.textAlign(CENTER, CENTER);

		int w = (int)graphics.textWidth(text) + 16;
		int h = (int)graphics.textSize;

		if (hasBorder) {
			graphics.stroke(BORDER);
			graphics.strokeWeight(4);
		}
		graphics.fill(backgroundColor);
		graphics.beginShape();
		graphics.vertex(4, 0);
		graphics.vertex(w - 4, 0);
		graphics.vertex(w, 4);
		graphics.vertex(w, h - 4);
		graphics.vertex(w - 4, h);
		graphics.vertex(4, h);
		graphics.vertex(0, h - 4);
		graphics.vertex(0, 4);
		graphics.endShape(CLOSE);

		graphics.noStroke();
		graphics.fill(textColor);
		graphics.text(text, w / 2, h / 2);

		graphics.endDraw();
		return graphics;

	}


	// data util
	private List<Integer> getDataRangeInt(ArrayList<Integer> dataPoints) {
		int count = dataPoints.size();
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		endIndex = dataPoints.size() - count;
		return dataPoints.subList(constrain(startIndex, 0, endIndex), count);
	}
	private List<String> getDataRangeString(ArrayList<String> dataPoints) {
		int count = dataPoints.size();
		jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
		while (graphWidth / count + 1 < jumpSize) count--;
		endIndex = dataPoints.size()- count;
		return dataPoints.subList(constrain(startIndex, 0, endIndex), count);
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
