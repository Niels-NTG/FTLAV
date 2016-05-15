// data
Table table;
int maxTableValue;
int startIndex = 0;
int endIndex;

// graphics
int margin = 32; // px
int jumpSize = 32; // px
int glowSpread = 9; // px
int graphWidth;
int graphHeigth;
int pWidth;
int pHeight;

PFont mainFont16;
PFont mainFont32;
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

final color BG_NORMAL = color(50, 43, 43);			// dark purple brown 	(background color)
final color BG_LIGHT = color(130, 109, 106);		// light purple brown	(background color)
final color BG_DARK = color(31, 35, 35);			// dark turquoise		(background color)
final color BORDER = color(230, 244, 222);			// white-greenish		(panel border color)
final color BUTTON = color(230, 244, 222);			// white-greenish		(button color)
final color BUTTON_ENABLED = color(253, 227, 77);	// yellow				(button color)
final color BUTTON_ACTIVE = color(148, 155, 143);	// greenish grey		(button color)
final color BUTTON_TEXT = color(20, 37, 39);		// dark turquoise		(button text color)
final color MAINTEXT = color(230, 244, 222);		// white-greenish		(standard text color)
final color SECTOR_CIVILIAN = color(118, 191, 58);	// bright green			(sector color)
final color SECTOR_HOSTILE = color(202, 29, 37);	// bright red			(sector color)
final color SECTOR_NEBULA = color(107, 18, 200);	// purple				(sector color)
final color SYSTEM_ACTIVE = color(89, 255, 82);		// bright green			(system status color)
final color SYSTEM_OFF = color(201, 201, 201);		// light grey			(system status color)
final color SYSTEM_DAMAGE = color(245, 56, 53);		// bright red			(system status color)
final color SYSTEM_HACKED = color(193, 27, 253);	// magenta				(system status color)
final color SYSTEM_IONIZED = color(119, 245, 253);	// cyan					(system status color)

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
	mainFont16		= loadFont("JustinFont8-16.vlw");		// for small sized text and user defined strings
	mainFont32		= loadFont("JustinFont8-32.vlw");
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

	pg.image(drawStandardAxisX(), margin, margin);
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
				map(dataPoints[i], 0, maxTableValue, graphHeigth, 0) - glowSpread
			);
		}
		glowLine.endShape();
	}

	glowLine.endDraw();
	return glowLine;

}


PGraphics drawStandardAxisX() {

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

			graphics.textFont(mainFont16);
			int lastIndexSectorNumber = lastIndexOfIntArray(sectorNumber, sectorNumber[i]);
			int lastBeaconNumberTextWidth = (int)graphics.textWidth(Integer.toString(beaconNumber[lastIndexSectorNumber]));
			sectorName[i] = sectorName[i].toUpperCase().replaceAll("\\b(SECTOR|CONTROLLED|UNCHARTED|HOMEWORLDS|THE)\\b","").trim();
			graphics.textFont(headerFontAlt11);
			int sectorNumberTextWidth = (int)graphics.textWidth(Integer.toString(sectorNumber[i]));
			sectorNumberTextWidth = sectorNumberTextWidth < 22 ? 22 : sectorNumberTextWidth * 2;
			graphics.textFont(headerFont11);
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
			graphics.fill(BUTTON_TEXT);
			graphics.text(sectorName[i], sectorNumberTextWidth + 4, 22);

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

		graphics.textFont(mainFont16);
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

	graphics.image(drawBox(lastChangedTimestamp, mainFont16, BUTTON_TEXT, BUTTON, false), 4, 4);

	graphics.image(drawBox(shipName, mainFont32, MAINTEXT, BG_DARK, true), 4, 30);

	graphics.endDraw();
	return graphics;

}


PGraphics drawBox(String text, PFont font, color textColor, color backgroundColor, boolean hasBorder) {

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