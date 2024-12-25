package nl.nielspoldervaart.ftlav.visualiser;

import nl.nielspoldervaart.ftlav.data.DataUtil;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class GraphLine {

	private static final int GLOW_SPREAD = 9; // 9px

	static void createLine(Visualiser root, PGraphics g, PVector size, PVector offset, String dataColumn, int yMax, int[] glowGradient) {
		ArrayList<Integer> dataPoints = DataUtil.extractIntColumn(dataColumn);

		PGraphics glowLine = root.createGraphics((int) size.x, (int) size.y);
		glowLine.beginDraw();
		glowLine.strokeCap(PConstants.ROUND);
		glowLine.strokeJoin(PConstants.ROUND);
		glowLine.noFill();

		for (int gradient = glowGradient.length - 1; gradient >= 0; --gradient) {

			glowLine.beginShape();
			glowLine.stroke(glowGradient[gradient]);
			glowLine.strokeWeight(gradient == glowGradient.length - 1 ? 2 : 2 + (gradient * 2));

			for (int i = 0; i < dataPoints.size(); ++i) {
				int x = root.getDataX(i, 0, glowLine.width);
				int y = (int)PApplet.map(dataPoints.get(i), 0, yMax, glowLine.height - GLOW_SPREAD, GLOW_SPREAD);
				glowLine.vertex(x, y);

				if (gradient == 0 && i == dataPoints.size() - 1) {
					GraphTextBox.createDataSeriesLabel(root, size, new PVector(x, y), dataColumn);
				}
			}

			glowLine.endShape();

		}
		glowLine.endDraw();
		g.image(glowLine, offset.x, offset.y);

		// Add label
		g.image(
			GraphTextBox.createDataSeriesLabel(
				root,
				size,
				new PVector(
					root.getDataX(dataPoints.size() - 1, 0, glowLine.width) + GLOW_SPREAD,
					PApplet.map(dataPoints.get(dataPoints.size() - 1), 0, yMax, glowLine.height, 0) - GLOW_SPREAD
				),
				DataUtil.getColumnDisplayName(dataColumn)
			),
			offset.x, offset.y
		);
	}

}
