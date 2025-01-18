package nl.nielspoldervaart.ftlav.visualiser;

import nl.nielspoldervaart.ftlav.data.DataUtil;
import nl.nielspoldervaart.ftlav.data.TableRow;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class GraphTextBox {

	private static final int BEVEL_SIZE = 4;

	static PGraphics createGraphTitle(Visualiser root) {
		TableRow lastRow = DataUtil.getLastRecord();
		if (lastRow == null) {
			return null;
		}
		String shipName = lastRow.getShipName();

		String shipType = lastRow.getShipType();
		String score = "Score: " + lastRow.getScore();
		String difficulty = "Difficulity: " + lastRow.getDifficulty();
		if (lastRow.isAEContentEnabled()) {
			difficulty = difficulty + " (ADVANCED EDITION)";
		}

		String totalScrapCollected = "Total scrap collected: " + lastRow.getTotalScrapCollected();
		String totalShipsDefeated = "Total ships defeated: " + lastRow.getTotalShipsDefeated();
		String totalCrewHired = "Total crew hired: " + lastRow.getTotalCrewHired();

		int shipTitleLabelWidth = root.getTextWidth(shipName, root.FONT_TITLE);
		int column2Width = PApplet.max(
			root.getTextWidth(shipType, root.FONT_MAIN),
			root.getTextWidth(difficulty, root.FONT_MAIN),
			root.getTextWidth(score, root.FONT_MAIN)
		);

		PGraphics titleGraphic = root.createGraphics(root.width, 54);
		titleGraphic.beginDraw();

		titleGraphic.fill(root.COLOR_MAIN_TEXT);
		titleGraphic.noStroke();

		titleGraphic.textFont(root.FONT_TITLE);
		titleGraphic.textAlign(PConstants.LEFT, PConstants.TOP);
		titleGraphic.text(shipName, 12, 12);

		titleGraphic.textFont(root.FONT_MAIN);
		titleGraphic.textLeading(15);
		titleGraphic.text(
			shipType + "\n" + difficulty + "\n" + score,
			12 + shipTitleLabelWidth + 12,
			8
		);
		titleGraphic.text(
			totalScrapCollected + "\n" + totalShipsDefeated + "\n" + totalCrewHired,
			12 + shipTitleLabelWidth + 12 + column2Width + 12,
			8
		);

		// Beacon event key
		titleGraphic.textAlign(PConstants.RIGHT, PConstants.TOP);
		titleGraphic.text(
			"RG = Red Gian, P = Pulsar, AF = Asteroid Field\nASB = Anti-Ship Battery, RF = Rebel Fleet\nS = Store, HS = Hostile Ship",
			root.width - 12,
			8
		);

		titleGraphic.endDraw();
		return titleGraphic;
	}

	static PGraphics createDataSeriesLabel(Visualiser root, String label) {
		return createTextBox(
			root,
			label.toUpperCase(),
			0, root.COLOR_MAIN_TEXT,
			root.FONT_SMALL,
			root.COLOR_BORDER, 1,
			PConstants.LEFT
		);
	}

	static void createSectorLabel(Visualiser root, PGraphics g, PVector size, PVector offset, int sectorNumber, String sectorName, String sectorType) {
		final int MARGIN = 2;

		PGraphics sectorLabelBox = root.createGraphics((int) size.x, (int) size.y);
		sectorLabelBox.beginDraw();

		// Create sector label graphics.
		PGraphics sectorNumberLabel = createTextBox(
			root,
			String.valueOf(sectorNumber),
			root.COLOR_STATUS_BACKGROUND, root.COLOR_MAIN_TEXT,
			root.FONT_MAIN,
			0, 0,
			PConstants.CENTER
		);
		PGraphics sectorNameLabel = createTextBox(root,
			sectorName,
			root.COLOR_STATUS_BACKGROUND, root.COLOR_MAIN_TEXT,
			root.FONT_MAIN
		);
		int labelWidth = MARGIN + sectorNumberLabel.width + MARGIN + sectorNameLabel.width + MARGIN;
		PGraphics background = createBevelBox(
			root,
			new PVector(labelWidth, 20),
			root.COLOR_BORDER, 0, 0
		);

		// Draw line and gradient portion.
		sectorLabelBox.noStroke();
		int[] sectorGradient = root.getSectorGradient(sectorType);
		for (int gradient = sectorGradient.length - 1; gradient >= 0; gradient--) {
			sectorLabelBox.fill(sectorGradient[gradient]);
			sectorLabelBox.rect(0, 0, size.x - 4, 2 * gradient);
		}
		sectorLabelBox.strokeWeight(1);
		sectorLabelBox.stroke(root.COLOR_BORDER);
		sectorLabelBox.line(0, 0, size.x - 4, 0);

		// Composite final sector label graphic.
		int labelYOffset = sectorLabelBox.height - background.height;
		sectorLabelBox.image(background, 0, labelYOffset);
		sectorLabelBox.image(sectorNumberLabel, MARGIN, labelYOffset + MARGIN);
		sectorLabelBox.image(sectorNameLabel, MARGIN + sectorNumberLabel.width + MARGIN, labelYOffset + MARGIN);

		sectorLabelBox.endDraw();
		g.image(sectorLabelBox, offset.x, offset.y);
	}

	static PGraphics createTextBox(Visualiser root, String text, int backgroundColor, int textColor, PFont textFont) {
		return createTextBox(root, text, backgroundColor, textColor, textFont, 0, 0, PConstants.LEFT);
	}
	static PGraphics createTextBox(
		Visualiser root,
	    String text,
	    int backgroundColor,
	    int textColor,
	    PFont textFont,
		int stroke,
		int strokeWeight,
	    int horizontalTextAlignment
	) {

		int margin = 6;
		int lineHeight = 24; // 24 is correct line height for root.FONT_LARGE
		int minWidth = 24;
		if (textFont == root.FONT_SMALL) {
			margin = 4;
			lineHeight = 16;
			minWidth = 8;
		} else if (textFont == root.FONT_MAIN) {
			margin = 4;
			lineHeight = 16;
			minWidth = 16;
		}

		int textWidth = Math.max(minWidth, root.getTextWidth(text, textFont));
		PVector size = new PVector(textWidth + margin + margin, lineHeight);

		PGraphics textBox = root.createGraphics((int) size.x, (int) size.y);
		textBox.beginDraw();
		textBox.textFont(textFont);
		textBox.textAlign(horizontalTextAlignment, PConstants.TOP);

		textBox.image(createBevelBox(
			root,
			new PVector(textBox.width, textBox.height),
			backgroundColor,
			stroke,
			strokeWeight
		), 0, 0);

		textBox.noStroke();
		textBox.fill(textColor);
		if (horizontalTextAlignment == PConstants.CENTER) {
			textBox.text(text, margin + (textWidth >> 1), margin);
		} else {
			textBox.text(text, margin, margin);
		}

		textBox.endDraw();
		return textBox;
	}

	static PGraphics createBevelBox(Visualiser root, PVector size, int fill) {
		return createBevelBox(root, size, fill, 0, 0, true, true, true, true);
	}
	static PGraphics createBevelBox(Visualiser root, PVector size, int fill, int stroke, int strokeWeight) {
		return createBevelBox(root, size, fill, stroke, strokeWeight, true, true, true, true);
	}
	static PGraphics createBevelBox(
		Visualiser root,
	    PVector size,
		int fill,
		int stroke,
		int strokeWeight,
	    boolean hasTopLeftBevel,
		boolean hasTopRightBevel,
		boolean hasBottomRightBevel,
		boolean hasBottomLeftBevel
	) {
		int w = (int) size.x;
		int h = (int) size.y;
		int x = 0;
		int y = 0;

		PGraphics box = root.createGraphics(w, h);
		box.beginDraw();

		box.beginShape();
		if (fill == 0) {
			box.noFill();
		} else {
			box.fill(fill);
		}
		if (stroke == 0 || strokeWeight == 0) {
			box.noStroke();
		} else {
			box.strokeWeight(strokeWeight);
			box.stroke(stroke);
		}
		if (hasTopLeftBevel) {
			box.vertex(x, y + BEVEL_SIZE);
			box.vertex(x + BEVEL_SIZE, y);
		} else {
			box.vertex(x, y);
		}
		if (hasTopRightBevel) {
			box.vertex(x + w - (BEVEL_SIZE + strokeWeight), y);
			box.vertex(x + w - strokeWeight, y + BEVEL_SIZE);
		} else {
			box.vertex(x + w, y);
		}
		if (hasBottomRightBevel) {
			box.vertex(x + w - strokeWeight, y + h - BEVEL_SIZE);
			box.vertex(x + w - (BEVEL_SIZE + strokeWeight), y + h - strokeWeight);
		} else {
			box.vertex(x + w - strokeWeight, y + h - strokeWeight);
		}
		if (hasBottomLeftBevel) {
			box.vertex(x + BEVEL_SIZE, y + h - strokeWeight);
			box.vertex(x, y + h - (BEVEL_SIZE + strokeWeight));
		} else {
			box.vertex(x, y + h - strokeWeight);
		}
		box.endShape(PConstants.CLOSE);

		box.endDraw();
		return box;
	}

}
