package nl.nielspoldervaart.ftlav.visualiser;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class GraphTextBox {

	static final int BEVEL_SIZE = 4;

	static PGraphics createDataSeriesLabel(Visualiser root, PVector size, PVector offset, String label) {
		PGraphics dataSeriesLabelBox = root.createGraphics((int) size.x, (int) size.y);
		dataSeriesLabelBox.beginDraw();

		PGraphics dataSeriesLabel = createTextBox(
			root,
			label.toUpperCase(),
			0, root.COLOR_MAIN_TEXT,
			root.FONT_SMALL,
			root.COLOR_BORDER, 1,
			PConstants.LEFT
		);
		dataSeriesLabelBox.image(dataSeriesLabel, offset.x, offset.y - dataSeriesLabel.height);

		dataSeriesLabelBox.endDraw();
		return dataSeriesLabelBox;
	}

	static void createSectorLabel(Visualiser root, PGraphics g, PVector size, PVector offset, int sectorNumber, String sectorName, String sectorType) {
		final int MARGIN = 4;

		PGraphics sectorLabelBox = root.createGraphics((int) size.x, (int) size.y);
		sectorLabelBox.beginDraw();

		PGraphics sectorNumberLabel = createTextBox(
			root,
			String.valueOf(sectorNumber),
			root.COLOR_STATUS_BACKGROUND, root.COLOR_MAIN_TEXT,
			root.FONT_LARGE
		);
		PGraphics sectorNameLabel = createTextBox(root,
			sectorName,
			root.COLOR_STATUS_BACKGROUND, root.COLOR_MAIN_TEXT,
			root.FONT_LARGE
		);
		int labelWidth = MARGIN + sectorNumberLabel.width + MARGIN + sectorNameLabel.width + MARGIN;
		PGraphics background = createBevelBox(
			root,
			new PVector(labelWidth, 32),
			root.COLOR_BORDER, 0, 0,
			false, false, true, true
		);

		sectorLabelBox.noStroke();
		int[] sectorGradient = root.getSectorGradient(sectorType);
		for (int gradient = sectorGradient.length - 1; gradient >= 0; gradient--) {
			sectorLabelBox.fill(sectorGradient[gradient]);
			sectorLabelBox.rect(0, 0, size.x - 4, 3 * gradient);
		}
		sectorLabelBox.strokeWeight(1);
		sectorLabelBox.stroke(root.COLOR_BORDER);
		sectorLabelBox.line(0, 0, size.x - 4, 0);

		sectorLabelBox.image(background, 0, 0);
		sectorLabelBox.image(sectorNumberLabel, MARGIN, MARGIN);
		sectorLabelBox.image(sectorNameLabel, MARGIN + sectorNumberLabel.width + MARGIN, MARGIN);

		sectorLabelBox.endDraw();
		g.image(sectorLabelBox, offset.x, offset.y);
	}

	static PGraphics createTextBox(Visualiser root, String text, int backgroundColor, int textColor, PFont textFont) {
		return createTextBox(root, text, backgroundColor, textColor, textFont, 0, 0, PConstants.CENTER);
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
		if (textFont == root.FONT_SMALL) {
			margin = 4;
			lineHeight = 18;
		}

		int textWidth = Math.max(24, root.getTextWidth(text, textFont));
		PVector size = new PVector(textWidth + margin + margin, lineHeight);

		PGraphics textBox = root.createGraphics((int) size.x, (int) size.y);
		textBox.beginDraw();
		textBox.textFont(textFont);
		textBox.textAlign(horizontalTextAlignment, PConstants.TOP);

		textBox.image(createBevelBox(root, new PVector(textBox.width, textBox.height), backgroundColor, stroke, strokeWeight), 0, 0);

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