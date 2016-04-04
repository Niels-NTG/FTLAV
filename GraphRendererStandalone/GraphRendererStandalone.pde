// data
Table table;
int maxTableValue;
int startIndex = 0;
int endIndex;

// graphics
int margin = 32; // px
int jumpSize = 32; // px
int graphWidth;
int graphHeigth;
int pWidth;
int pHeight;

PFont mainFont13;
PFont mainFont39;
PFont headerFont11;
PFont headerFontAlt11;
PFont headerFont22;
PFont headerFontAlt22;

PImage crewCombat;
PImage crewEngine;
PImage crewHealth;
PImage crewPilot;
PImage crewRepairs;
PImage crewShield;
PImage crewWeapon;

final color BG_NORMAL = color(55, 45, 46);			// dark purple brown 	(background color)
final color BG_LIGHT = color(122, 100, 99);			// light purple brown	(background color)
final color BG_DARK = color(24, 20, 19);			// dark brown			(background color)
final color BORDER = color(235, 245, 229);			// white-greenish		(panel border color)
final color BUTTON = color(235, 245, 229);			// white-greenish		(button color)
final color BUTTON_ACTIVE = color(255, 230, 94);	// yellow				(button color)
final color MAINTEXT = color(255, 255, 255);		// white				(standard text color)
final color HEADERTEXT = color(65, 120, 128);		// turquoise			(standard text color)
final color HEADERTEXT_ALT = color(54, 78, 80);		// dark turquoise		(standard text color)
final color SECTOR_CIVILIAN = color(135, 199, 74);	// bright green			(sector color)
final color SECTOR_HOSTILE = color(214, 50, 50);	// bright red			(sector color)
final color SECTOR_NEBULA = color(128, 51, 210);	// pure purple			(sector color)
final color SYSTEM_ACTIVE = color(100, 255, 99);	// bright green			(system status color)
final color SYSTEM_OFF = color(211, 211, 211);		// light grey			(system status color)
final color SYSTEM_DAMAGE = color(248, 59, 51);		// bright red			(system status color)
final color SYSTEM_HACKED = color(212, 70, 253);	// magenta				(system status color)
final color SYSTEM_IONIZED = color(133, 231, 237);	// cyan					(system status color)

final color[] GLOW_BLUE = {
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
final color[] GLOW_GREEN = {
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
final color[] GLOW_RED = {
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
final color[] GLOW_PURPLE = {
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

// TODO draw xAxis
// TODO title header bar in the style of the ship info windows from the game
// TODO legend key list on the top row
// TODO make legend key list items into a toggle to show/hide series (Standalone only)
// TODO mouseover keys highlights corrosponding line
// TODO mouseover line hightlihgs corrosponding key
// TODO fix kerning/horizontal spacing of headerFont11 & headerFontAlt11
// TODO add flexible textbox PGraphics method
// TODO add README.md to this sketch folder
// TODO use horizontal scrollbar when option above is disabled
// TODO arrow buttons to bottom left and right corner to shift graph 1 jump back into history/present
// TODO mouseover sector displays tooltip with complete sector information (name, type)
// TODO mouseover beacon number display tooltip with complete beacon information (store, event text, hazards, etc.)
// TODO display "TOTAL" values as difference compared to previous beacon instead of absolute value
// TODO symbol beneath beacon number indicates if their was a enviourmental hazard at that beacon
// TODO graph type: systems stacked line chart
// TODO graph type: bar graph with win/los ratio per sector (positive and negative values)
// TODO graph type: timed scatterplot

void setup() {

	// window
	size(1200, 700);
	surface.setResizable(true);
	graphWidth = width - (margin + margin);
	graphHeigth = height - (margin + margin + margin);
	pWidth = width;
	pHeight = height;

	// data
	table = loadTable("data30 (FTL Adventure Visualiser 3) 20160317 161543.tsv", "header");
	int[] columnMax = new int[table.getColumnCount()];
	for (int i = 0; i < table.getColumnCount(); ++i) {
		columnMax[i] = max(table.getIntColumn(i));
	}
	maxTableValue = max(columnMax);

	// graphics
	mainFont13		= loadFont("C&CRedAlertINET-13.vlw");	// for small sized text and user defined strings
	mainFont39		= loadFont("C&CRedAlertINET-39.vlw");
	headerFont11	= loadFont("Half-Life2-11.vlw");
	headerFontAlt11	= loadFont("Half-Life1-11.vlw");
	headerFont22	= loadFont("Half-Life2-22.vlw");		// for labeling static things
	headerFontAlt22	= loadFont("Half-Life1-22.vlw");		// alternative for smaller text sizes

	crewCombat	= loadImage("combat.gif");
	crewEngine	= loadImage("engine.gif");
	crewHealth	= loadImage("health.gif");
	crewPilot	= loadImage("pilot.gif");
	crewRepairs	= loadImage("repairs.gif");
	crewShield	= loadImage("shield.gif");
	crewWeapon	= loadImage("weapon.gif");

	thread("checkForResize");

	noLoop();

}


void draw() {

	// background
	background(BG_DARK);

	// graphics with transparant background
	image(generateGraphics(), 0, 0);

	// overlay graphics (mouseover, etc)


}


PGraphics generateGraphics() {

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


PGraphics drawGraphLine(int[] dataPoints) {
	return drawGraphLine(dataPoints, GLOW_BLUE);
}
PGraphics drawGraphLine(int[] dataPoints, color[] gradient) {

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


PGraphics drawStandardAxisY() {

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
			graphics.textFont(headerFontAlt11, 11);
			int sectorNumberTextWidth = (int)graphics.textWidth(Integer.toString(sectorNumber[i]));
			sectorNumberTextWidth = sectorNumberTextWidth < 22 ? 22 : sectorNumberTextWidth * 2;
			graphics.textFont(headerFont11, 11);
			int sectorNameTextWidth = (int)graphics.textWidth(sectorName[i]);

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
			graphics.fill(HEADERTEXT_ALT);
			graphics.text(sectorName[i], sectorNumberTextWidth + 4, 22);

			// draw sectorNumber box
			graphics.fill(HEADERTEXT_ALT);
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
			graphics.textFont(headerFontAlt11, 11);
			graphics.text(sectorNumber[i], sectorNumberTextWidth / 2 + 2, 22);

			// draw glow accent
			color[] gradient;
			switch (sectorType[i]) {
				case "CIVILIAN": gradient = GLOW_GREEN; break;
				case "HOSTILE" : gradient = GLOW_RED; break;
				case "NEBULA"  : gradient = GLOW_PURPLE; break;
				default: gradient = GLOW_BLUE; break;
			}
			for (int g = 1; g < gradient.length; ++g) {
				graphics.fill(gradient[g]);
				graphics.rect(0, 18, jumpSize * (lastIndexSectorNumber - i) + lastBeaconNumberTextWidth, -g * 1.6);
			}

			// reset
			graphics.textAlign(LEFT, TOP);

		}

		graphics.textFont(mainFont13, 13);
		graphics.fill(MAINTEXT);
		graphics.text(beaconNumber[i], 0, 6.5);

		graphics.popMatrix();

		lastSectorNumber = sectorNumber[i];

	}

	graphics.endDraw();
	return graphics;

}


PGraphics drawHeader() {

	int lastRowIndex = table.getRowCount() - 1;
	String lastChangedTimestamp = table.getString(lastRowIndex, "TIME");
	String shipName = table.getString(lastRowIndex, "SHIP NAME");
	String shipType = table.getString(lastRowIndex, "SHIP TYPE");
	String difficulty = table.getString(lastRowIndex, "DIFFICULTY");
	String ae = "AE content " + table.getString(lastRowIndex, "AE CONTENT");

	PGraphics graphics = createGraphics(width, height);
	graphics.beginDraw();

	graphics.textFont(mainFont13, 13);
	int lastChangedTimestampTextWidth = (int)graphics.textWidth(lastChangedTimestamp) + 8;
	graphics.textFont(mainFont39, 39);
	int shipNameTextWidth = (int)graphics.textWidth(shipName) + 32;

	// container
	graphics.fill(BORDER);
	graphics.beginShape();
	graphics.vertex(4, 0);
	graphics.vertex(lastChangedTimestampTextWidth + 4, 0);
	graphics.vertex(lastChangedTimestampTextWidth + 8, 4);
	graphics.vertex(lastChangedTimestampTextWidth + 8, 21);
	graphics.vertex(shipNameTextWidth + 4, 21);
	graphics.vertex(shipNameTextWidth + 8, 25);
	graphics.vertex(shipNameTextWidth + 8, 64);
	graphics.vertex(shipNameTextWidth + 4, 68);
	graphics.vertex(4, 68);
	graphics.vertex(0, 64);
	graphics.vertex(0, 4);
	graphics.endShape();

	// shipName box
	graphics.fill(BG_NORMAL);
	graphics.beginShape();
	graphics.vertex(8, 25);
	graphics.vertex(shipNameTextWidth, 25);
	graphics.vertex(shipNameTextWidth + 4, 29);
	graphics.vertex(shipNameTextWidth + 4, 60);
	graphics.vertex(shipNameTextWidth, 64);
	graphics.vertex(8, 64);
	graphics.vertex(4, 60);
	graphics.vertex(4, 29);
	graphics.endShape();

	// info text
	graphics.fill(BG_DARK);
	graphics.textAlign(LEFT, CENTER);
	graphics.textFont(mainFont13, 13);
	graphics.text(lastChangedTimestamp, 8, 13);

	// shipName text
	graphics.fill(MAINTEXT);
	graphics.textAlign(CENTER, CENTER);
	graphics.textFont(mainFont39, 39);
	graphics.text(shipName, 4 + shipNameTextWidth / 2, 44.5);

	graphics.endDraw();
	return graphics;

}


// data util
int[] getDataRange(int[] dataPoints) {
	int count = dataPoints.length;
	jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
	while (graphWidth / count + 1 < jumpSize) count--;
	endIndex = dataPoints.length - count;
	dataPoints = subset(dataPoints, constrain(startIndex, 0, endIndex), count);
	return dataPoints;
}
String[] getDataRange(String[] dataPoints) {
	int count = dataPoints.length;
	jumpSize = graphWidth / count + 1 < jumpSize ? 32 : graphWidth / count + 1;
	while (graphWidth / count + 1 < jumpSize) count--;
	endIndex = dataPoints.length - count;
	dataPoints = subset(dataPoints, constrain(startIndex, 0, endIndex), count);
	return dataPoints;
}


int indexOfIntArray(int[] array, int key) {
	int returnvalue = -1;
	for (int i = 0; i < array.length; ++i) {
		if (key == array[i]) {
			returnvalue = i;
			break;
		}
	}
	return returnvalue;
}
int lastIndexOfIntArray(int[] array, int key) {
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
void keyPressed() {
	if (key == CODED) {
		if (keyCode == LEFT || keyCode == RIGHT) {
			if (keyCode == LEFT) startIndex--;
			if (keyCode == RIGHT) startIndex++;
			startIndex = constrain(startIndex, 0, endIndex);
			redraw();
		}
	}
}


void checkForResize(){
	try {
		while (true) {
			Thread.sleep(10); // 10ms
			if (pWidth != width || pHeight != height) {
				surface.setSize(constrain(width, 480, width), constrain(height, 480, height));
				graphWidth = width - (margin + margin);
				graphHeigth = height - (margin + margin + margin);
				pWidth = width;
				pHeight = height;
				redraw();
			}
		}
	} catch (Exception e) {};
}