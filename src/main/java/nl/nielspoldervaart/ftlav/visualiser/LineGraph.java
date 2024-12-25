package nl.nielspoldervaart.ftlav.visualiser;

import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

public class LineGraph {

	private final Visualiser root;
	private final int GRAPHICS_WIDTH;
	private final int GRAPHICS_HEIGHT;

	HashMap<String, Boolean> timeSeriesData = new HashMap<>();

	private final int Y_INCREMENT = 20;
	private final int Y_AXIS_WIDTH = 32;
	private final int X_AXIS_HEIGHT = 32;
	private final int TOP_MARGIN = 32;

	LineGraph(Visualiser root, int width, int height) {
		this.root = root;
		this.GRAPHICS_WIDTH = width;
		this.GRAPHICS_HEIGHT = height;
	}

	PGraphics draw() {
		int yMax = Math.max(DataUtil.getMaxValue(FTLAdventureVisualiser.columnsInVisualiser), Y_INCREMENT * 2);
		yMax = yMax + Y_INCREMENT - yMax % Y_INCREMENT;

		PGraphics graphics = root.createGraphics(GRAPHICS_WIDTH, GRAPHICS_HEIGHT);
		graphics.beginDraw();

		drawYAxis(graphics, yMax);

		drawXAxis(graphics);

		plotData(graphics, yMax);

		graphics.endDraw();
		return graphics;
	}

	private void drawYAxis(PGraphics g, int yMax) {
		PGraphics yAxis = root.createGraphics(GRAPHICS_WIDTH - Y_AXIS_WIDTH, g.height);
		yAxis.beginDraw();
		yAxis.strokeCap(PConstants.SQUARE);
		yAxis.strokeJoin(PConstants.ROUND);
		yAxis.strokeWeight(0.4f);
		yAxis.fill(root.COLOR_MAIN_TEXT);
		yAxis.textFont(root.FONT_MAIN);
		yAxis.textAlign(PConstants.RIGHT, PConstants.BOTTOM);

		for (int i = 0; i <= yMax; i += Y_INCREMENT) {
			int y = (int) PApplet.map(i, 0, yMax, g.height - X_AXIS_HEIGHT, TOP_MARGIN);

			if (i > 0) {
				yAxis.stroke(root.COLOR_BORDER);
				yAxis.line(0, y, yAxis.width, y);
			}
			yAxis.noStroke();
			yAxis.text(i, Y_AXIS_WIDTH - 2, y - 2);
		}

		yAxis.endDraw();
		g.image(yAxis, 0, 0);
	}

	private void drawXAxis(PGraphics g) {
		ArrayList<Integer> beaconNumbers = DataUtil.extractIntColumn("beaconNumber");
		ArrayList<Integer> sectorNumbers = DataUtil.extractIntColumn("sectorNumber");
		ArrayList<String> sectorNames = DataUtil.extractStringColumn("sectorName");
		ArrayList<String> sectorTypes = DataUtil.extractStringColumn("sectorType");

		int lastSectorNumber = -1;

		PGraphics xAxis = root.createGraphics(g.width, g.height);
		xAxis.beginDraw();
		xAxis.textFont(root.FONT_MAIN);
		xAxis.textAlign(PConstants.LEFT, PConstants.BOTTOM);

		xAxis.strokeWeight(0.4f);
		for (int i = 0; i < beaconNumbers.size(); i++) {
			int x = (int) PApplet.map(i, 0, beaconNumbers.size(), Y_AXIS_WIDTH, xAxis.width);

			xAxis.stroke(root.COLOR_BORDER);
			xAxis.line(x, xAxis.height - X_AXIS_HEIGHT, x, TOP_MARGIN);

			if (sectorNumbers.get(i) != lastSectorNumber) {
				int nextSectorIndex = -1;
				for (int sectorNumberIndex = i; sectorNumberIndex < sectorNumbers.size(); sectorNumberIndex++) {
					if (sectorNumbers.get(sectorNumberIndex) != sectorNumbers.get(i)) {
						nextSectorIndex = sectorNumberIndex;
						break;
					}
				}
				int sectorWidth = (int) PApplet.map(nextSectorIndex == -1 ? beaconNumbers.size() : nextSectorIndex, 0, beaconNumbers.size(), Y_AXIS_WIDTH, xAxis.width) - x;
				GraphTextBox.createSectorLabel(
					root, g,
					new PVector(sectorWidth, X_AXIS_HEIGHT),
					new PVector(x, xAxis.height - X_AXIS_HEIGHT),
					sectorNumbers.get(i),
					sectorNames.get(i),
					sectorTypes.get(i)
				);
			}

			xAxis.noStroke();
			xAxis.fill(root.COLOR_MAIN_TEXT);
			xAxis.text(beaconNumbers.get(i), x + 4, xAxis.height - (X_AXIS_HEIGHT + 2));

			lastSectorNumber = sectorNumbers.get(i);
		}

		xAxis.endDraw();
		g.image(xAxis, 0, 0);
	}

	private void plotData(PGraphics g, int yMax) {
		PVector lineGraphicsSize = new PVector(GRAPHICS_WIDTH - Y_AXIS_WIDTH, GRAPHICS_HEIGHT - (X_AXIS_HEIGHT + TOP_MARGIN));
		PVector lineGraphicsOffset = new PVector(Y_AXIS_WIDTH, TOP_MARGIN);
		for (String key : FTLAdventureVisualiser.columnsInVisualiser.keySet()) {
			if (FTLAdventureVisualiser.columnsInVisualiser.get(key)) {
				GraphLine.createLine(root,
					g,
					lineGraphicsSize,
					lineGraphicsOffset,
					key,
					yMax,
					root.getLineColor(FTLAdventureVisualiser.colorsInVisualiser.get(key).label)
				);
			}
		}
	}

}
