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

		if (dataPoints.size() < 2) {
			return;
		}

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
				int x = Visualiser.getDataX(i, 0, glowLine.width);
				int y = (int)PApplet.map(dataPoints.get(i), 0, yMax, glowLine.height, 0) - GLOW_SPREAD / 2;
				glowLine.vertex(x, y);
			}

			glowLine.endShape();

		}
		glowLine.endDraw();
		g.image(glowLine, offset.x, offset.y);
	}

}
