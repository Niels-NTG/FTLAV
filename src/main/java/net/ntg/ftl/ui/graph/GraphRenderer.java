package net.ntg.ftl.ui.graph;

import java.util.ArrayList;

import processing.core.*;

import net.ntg.ftl.FTLAdventureVisualiser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	public static ArrayList<ArrayList<Integer>> superArray = new ArrayList<ArrayList<Integer>>();
	public static int ceiling = 30;

	public static int panelWidth;
	public static int panelHeight;

	private int canvasWidth;
	private int canvasHeight;
	private int margin = 32; // px

	// graphics
	PFont mainFont;
	PFont headerFont;
	PFont headerFontAlt;


	public void setup() {

		size(panelWidth, panelHeight);

		canvasWidth = panelWidth  - (margin * 2);
		canvasHeight= panelHeight - (margin * 2);

		// graphics
		background(55,45,46);

		mainFont      = loadFont(ClassLoader.getSystemResource("C&CRedAlertINET-48.vlw").toString());
		headerFont    = loadFont(ClassLoader.getSystemResource("Half-Life1-48.vlw").toString());
		headerFontAlt = loadFont(ClassLoader.getSystemResource("Half-Life2-48.vlw").toString());

	}


	public void draw() {

		background(55,45,46);

		for (int a = 0; a < superArray.size(); ++a) {

			// graph line
			noFill();
			stroke(235, 245, 227);
			strokeWeight(4);
			strokeJoin(ROUND);
			strokeCap(ROUND);
			beginShape();
			for (int b = 0; b < superArray.get(a).size(); ++b) {
				vertex(
					margin + canvasWidth / superArray.get(a).size() * b,
					map(
						superArray.get(a).get(b),
						0, ceiling,
						canvasHeight - margin, margin
					)
				);
			}
			endShape();

			// graph x labels
			noStroke();
			fill(235, 245, 227);
			textFont(mainFont, 15);
			for (int b = 0; b < superArray.get(a).size(); ++b) {
				text(
					"B " + FTLAdventureVisualiser.gameStateArray.get(b).getTotalBeaconsExplored()+"\n"+
					"S " + FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber(),
					margin + (canvasWidth / superArray.get(a).size()) * b,
					canvasHeight + 20
				);
			}

		}

		// graph y labels
		noStroke();
		fill(235, 245, 227);
		textFont(mainFont, 15);
		textAlign(RIGHT, BASELINE);
		for (int y = ceiling; y > 0; --y) {
			if (y % floor(canvasHeight / ceiling * 3) == 0) {
				text(
					Integer.toString(y),
					margin / 4,
					map(y, 0, ceiling, canvasHeight+margin, margin)
				);
			}
		}

		// TODO draw a vertical line at the end of each sector
		// 		with the name of the sector center between sets of two seperators

	}

}