package net.ntg.ftl.ui.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import processing.core.*;

import net.ntg.ftl.FTLAdventureVisualiser;

import net.blerf.ftl.model.sectortree.SectorDot;
import net.blerf.ftl.parser.random.NativeRandom;
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GraphRenderer extends PApplet {

	private static final Logger log = LogManager.getLogger(GraphRenderer.class);

	public static ArrayList<ArrayList<Integer>> superArray = new ArrayList<ArrayList<Integer>>();
	public static int ceiling = 30;

	int current = 0;
	int previous= 0;

	// window
	public static int panelWidth;
	public static int panelHeight;

	private int canvasWidth;
	private int canvasHeight;
	private int margin = 64; // px

	// graphics
	PFont mainFont;
	PFont headerFont;
	PFont headerFontAlt;

	int[] blueGlow = {
		color(235, 245, 227, 255),
		color(69, 110, 112, 226),
		color(61, 94, 97, 198),
		color(53, 80, 81, 170),
		color(47, 69, 70, 141),
		color(44, 61, 62, 113),
		color(40, 54, 54, 85),
		color(37, 50, 49, 56),
		color(36, 47, 46, 28)
	};

	Map<String,Integer> sectorTypeColor = new HashMap<String,Integer>();

	public void setup() {

		// window
		size(panelWidth, panelHeight);

		canvasWidth = panelWidth  - (margin * 2);
		canvasHeight= panelHeight - (margin * 2);

		// graphics
		background(40, 36, 34);

		mainFont      = loadFont(ClassLoader.getSystemResource("C&CRedAlertINET-48.vlw").toString());
		headerFont    = loadFont(ClassLoader.getSystemResource("Half-Life1-48.vlw").toString());
		headerFontAlt = loadFont(ClassLoader.getSystemResource("Half-Life2-48.vlw").toString());

		sectorTypeColor.put( "CIVILIAN", color( 135, 199, 74 ) );
		sectorTypeColor.put( "HOSTILE", color( 214, 50, 50 ) );
		sectorTypeColor.put( "NEBULA", color( 128, 51, 210 ) );

	}


	public void draw() {

		current = FTLAdventureVisualiser.gameStateArray.size() - 1;


		if (current > previous) {

			background(40, 36, 34);

			for (int a = 0; a < superArray.size(); ++a) {

				// graph line
				for (int s = blueGlow.length - 1; s >= 0; --s) {

					noFill();
					stroke(blueGlow[s]);
					strokeWeight(s ==  blueGlow.length - 1 ? 4 : 4 + (s * 2));
					strokeJoin(ROUND);
					strokeCap(ROUND);
					beginShape();
					for (int b = 0; b < superArray.get(a).size(); ++b) {
						vertex(
							margin + canvasWidth / superArray.get(a).size() * b,
							map(
								superArray.get(a).get(b),
								0, ceiling,
								margin + canvasHeight, margin
							)
						);
					}
					endShape();
				}

				// graph x labels
				noStroke();
				for (int b = 0; b < superArray.get(a).size(); ++b) {
					fill(235, 245, 227);
					textFont(mainFont, 15);
					textAlign(LEFT, TOP);
					text(
						"B " + FTLAdventureVisualiser.gameStateArray.get(b).getTotalBeaconsExplored()+"\n"+
						"S " + FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber(),
						margin + (canvasWidth / superArray.get(a).size()) * b,
						canvasHeight + margin + (margin / 4)
					);

					if (b == 0 ||
						FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber() >
						FTLAdventureVisualiser.gameStateArray.get(b-1).getSectorNumber()
					) {
						textFont(headerFontAlt, 22);
						textAlign(LEFT, BOTTOM);
						fill(sectorTypeColor.get(
								getSectorInfo(
									FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber()
								).getType()
							)
						);
						text(getSectorInfo(
								FTLAdventureVisualiser.gameStateArray.get(b).getSectorNumber()
							).getTitle().toUpperCase(),
							margin + (canvasWidth / superArray.get(a).size()) * b,
							canvasHeight + margin + (margin / 4)
						);
					}
				}

			}

			// graph y labels
			noStroke();
			fill(235, 245, 227);
			textFont(mainFont, 15);
			textAlign(RIGHT, BASELINE);
			for (int y = canvasHeight; y > 0; y -= (canvasHeight * 0.06)) {
				text(
					Integer.toString(y),
					margin - (margin / 4),
					map(y, 0, ceiling, canvasHeight + margin, margin)
				);
			}

			// TODO draw a vertical line at the end of each sector

			// TODO draw text label on the end of each graph line that shows what data it is

		}

		previous = current;

	}


	private SectorDot getSectorInfo( int index ) {
		ArrayList<SectorDot> visitedSectors = new ArrayList<SectorDot>();

		RandomSectorTreeGenerator myGen = new RandomSectorTreeGenerator( new NativeRandom() );
		List<List<SectorDot>> myColumns = myGen.generateSectorTree(
			FTLAdventureVisualiser.gameStateArray.get(current).getSectorTreeSeed(),
			FTLAdventureVisualiser.gameStateArray.get(current).isDLCEnabled()
		);

		int columnsOffset = 0;
		for (int i = 0; i < myColumns.size(); i++) {
			for (int k = 0; k < myColumns.get(i).size(); k++) {
				if (FTLAdventureVisualiser.gameStateArray.get(current).getSectorVisitation().subList(
						columnsOffset, columnsOffset + myColumns.get(i).size()
					).get(k)
				) {
					visitedSectors.add( myColumns.get(i).get(k) );
				}
			}
			columnsOffset += myColumns.get(i).size();
		}

		return visitedSectors.get( index );
	}

}