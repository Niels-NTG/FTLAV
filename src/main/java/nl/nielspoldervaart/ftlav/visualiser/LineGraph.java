package nl.nielspoldervaart.ftlav.visualiser;

import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.data.DataUtil;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class LineGraph {

	private final Visualiser root;
	private final int GRAPHICS_WIDTH;
	private final int GRAPHICS_HEIGHT;

	private final int Y_INCREMENT = 20;
	private final int Y_MIN_VISUAL_INCREMENT = 32;
	private final int Y_AXIS_WIDTH = 32;
	private final int X_AXIS_HEIGHT = 36;
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

		PGraphics plotLabelArea = createPlotLabels(yMax);

		drawYAxis(graphics, yMax, plotLabelArea);

		drawXAxis(graphics, plotLabelArea);

		plotData(graphics, yMax, plotLabelArea);

		graphics.endDraw();
		return graphics;
	}

	private void drawYAxis(PGraphics g, int yMax, PGraphics plotLabelArea) {
		PGraphics yAxis = root.createGraphics(GRAPHICS_WIDTH, g.height);
		yAxis.beginDraw();
		yAxis.strokeCap(PConstants.SQUARE);
		yAxis.strokeJoin(PConstants.ROUND);
		yAxis.strokeWeight(0.4f);
		yAxis.fill(root.COLOR_MAIN_TEXT);
		yAxis.textFont(root.FONT_MAIN);
		yAxis.textAlign(PConstants.RIGHT, PConstants.BOTTOM);

		int lastY = 0;
		for (int i = 0; i <= yMax; i += Y_INCREMENT) {
			int y = (int) PApplet.map(i, 0, yMax, g.height - X_AXIS_HEIGHT, TOP_MARGIN);
			if (lastY != 0 && Math.abs(y - lastY) < Y_MIN_VISUAL_INCREMENT) {
				continue;
			}

			if (i > 0) {
				yAxis.stroke(root.COLOR_BORDER);
				yAxis.line(Y_AXIS_WIDTH, y, yAxis.width - plotLabelArea.width, y);
			}
			yAxis.noStroke();
			yAxis.text(i, Y_AXIS_WIDTH - 2, y - 2);
			lastY = y;
		}

		yAxis.endDraw();
		g.image(yAxis, 0, 0);
	}

	private void drawXAxis(PGraphics g, PGraphics plotLabelArea) {
		ArrayList<Integer> beaconNumbers = DataUtil.extractIntColumn("beaconsExplored");
		ArrayList<Integer> sectorNumbers = DataUtil.extractIntColumn("sectorNumber");
		ArrayList<String> sectorNames = DataUtil.extractStringColumn("sectorName");
		ArrayList<String> sectorTypes = DataUtil.extractStringColumn("sectorType");

		int lastSectorNumber = -1;

		PGraphics xAxis = root.createGraphics(g.width, g.height);
		xAxis.beginDraw();
		xAxis.textFont(root.FONT_MAIN);
		xAxis.textAlign(PConstants.LEFT, PConstants.TOP);

		xAxis.strokeWeight(0.4f);
		for (int i = 0; i < beaconNumbers.size(); i++) {
			int x = Visualiser.getDataX(i, Y_AXIS_WIDTH, xAxis.width - plotLabelArea.width);

			xAxis.stroke(root.COLOR_BORDER);
			xAxis.line(x, xAxis.height - X_AXIS_HEIGHT, x, TOP_MARGIN);

			if (sectorNumbers.get(i) != lastSectorNumber) {
				int nextSectorIndex = -1;
				for (int sectorNumberIndex = i; sectorNumberIndex < sectorNumbers.size(); sectorNumberIndex++) {
					if (!sectorNumbers.get(sectorNumberIndex).equals(sectorNumbers.get(i))) {
						nextSectorIndex = sectorNumberIndex;
						break;
					}
				}
				int sectorWidth = Visualiser.getDataX(
					nextSectorIndex == -1 ? beaconNumbers.size() : nextSectorIndex,
					Y_AXIS_WIDTH,
					xAxis.width - plotLabelArea.width
				) - x;
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
			xAxis.text(beaconNumbers.get(i), x + 4, xAxis.height - (X_AXIS_HEIGHT - 4));

			lastSectorNumber = sectorNumbers.get(i);
		}

		xAxis.endDraw();
		g.image(xAxis, 0, 0);
	}

	private void plotData(PGraphics g, int yMax, PGraphics plotLabels) {
		PVector lineGraphicsSize = new PVector(
			GRAPHICS_WIDTH - (Y_AXIS_WIDTH + plotLabels.width),
			GRAPHICS_HEIGHT - (X_AXIS_HEIGHT + TOP_MARGIN)
		);
		PVector lineGraphicsOffset = new PVector(Y_AXIS_WIDTH, TOP_MARGIN);
		for (String key : FTLAdventureVisualiser.getColumnsVisibleInVisualiser()) {
			GraphLine.createLine(root,
				g,
				lineGraphicsSize,
				lineGraphicsOffset,
				key,
				yMax,
				root.getLineColor(FTLAdventureVisualiser.colorsInVisualiser.get(key).label)
			);
		}

		g.image(plotLabels, GRAPHICS_WIDTH - plotLabels.width, TOP_MARGIN);
	}

	private PGraphics createPlotLabels(int yMax) {
		final int LABEL_MARGIN = 4;
		final int LABEL_AREA_MIN_WIDTH = 64;

		HashMap<String, PGraphics> labels =	new HashMap<>();
		ArrayList<Integer> labelWidths = new ArrayList<>();
		for (String key : FTLAdventureVisualiser.getColumnsVisibleInVisualiser()) {
			PGraphics labelGraphic = GraphTextBox.createDataSeriesLabel(root, DataUtil.getColumnDisplayName(key));
			labels.put(key, labelGraphic);
			labelWidths.add(labelGraphic.width + LABEL_MARGIN);
		}
		PGraphics labelsArea = root.createGraphics(
			labelWidths.isEmpty() ? LABEL_AREA_MIN_WIDTH : Math.max(Collections.max(labelWidths), LABEL_AREA_MIN_WIDTH),
			GRAPHICS_HEIGHT - (X_AXIS_HEIGHT + TOP_MARGIN)
		);
		labelsArea.beginDraw();
		labelsArea.noStroke();
		if (FTLAdventureVisualiser.recording.size() > 1) {
			for (String key : labels.keySet()) {
				int value;
				try {
					value = (int) Objects.requireNonNull(DataUtil.getLastRecord()).getFieldValue(key);
				} catch (NullPointerException e) {
					continue;
				}

				PGraphics label = labels.get(key);
				int y = (int) PApplet.map(
					value,
					0,
					yMax,
					labelsArea.height,
					0
				) - (label.height + LABEL_MARGIN);
				y = Math.max(y, LABEL_MARGIN);

				labelsArea.image(
					label,
					LABEL_MARGIN,
					y
				);
			}
		}

		labelsArea.endDraw();
		return labelsArea;
	}

}
