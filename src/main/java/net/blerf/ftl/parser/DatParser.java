package net.blerf.ftl.parser;

import net.blerf.ftl.model.ShipLayout;
import net.blerf.ftl.xml.*;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DatParser {

	private final Pattern xmlDeclPtn = Pattern.compile("<[?]xml [^>]*?[?]>\n*");


	public DatParser() {
	}


	public List<Achievement> readAchievements(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<achievements>"+ streamText +"</achievements>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(Achievements.class);
		Unmarshaller u = jc.createUnmarshaller();
		Achievements ach = (Achievements)u.unmarshal(domOutputter.output(doc));

		return ach.getAchievements();
	}


	public Blueprints readBlueprints(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<blueprints>"+ streamText +"</blueprints>";
		StringBuilder sb = new StringBuilder(streamText);
		String ptn; Pattern p; Matcher m;

		if ("blueprints.xml".equals(fileName)) {
			// blueprints.xml: LONG_ELITE_MED shipBlueprint (FTL 1.03.1)
			// blueprints.xml: LONG_ELITE_HARD shipBlueprint (FTL 1.03.1)
			streamText = streamText.replaceAll(" img=\"rebel_long_hard\"", " img=\"rebel_long_elite\"");
		}

		if ("blueprints.xml".equals(fileName)) {
			// blueprints.xml: SYSTEM_CASING augBlueprint (FTL 1.02.6)
			ptn = "";
			ptn += "\\s*<title>Reinforced System Casing</title>"; // Extra title.
			ptn += "(\\s*<title>Titanium System Casing</title>)";

			p = Pattern.compile(ptn);
			m = p.matcher(sb);
			if (m.find()) {
				sb.replace(m.start(), m.end(), m.group(1));
				m.reset();
			}
		}

		Document doc = TextUtilities.parseStrictOrSloppyXML(sb.toString(), fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(Blueprints.class);
		Unmarshaller u = jc.createUnmarshaller();
		Blueprints bps = (Blueprints)u.unmarshal(domOutputter.output(doc));

		return bps;
	}


	public ShipLayout readLayout(InputStream stream, String fileName) throws IOException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		BufferedReader in = new BufferedReader(new StringReader(streamText));

		ShipLayout shipLayout = new ShipLayout();

		String line;
		while ((line = in.readLine()) != null) {
			if (line.length() == 0) continue;

			if (line.equals("X_OFFSET")) {
				shipLayout.setOffsetX(Integer.parseInt(in.readLine()));
			}
			else if (line.equals("Y_OFFSET")) {
				shipLayout.setOffsetY(Integer.parseInt(in.readLine()));
			}
			else if (line.equals("HORIZONTAL")) {
				shipLayout.setHorizontal(Integer.parseInt(in.readLine()));
			}
			else if (line.equals("VERTICAL")) {
				shipLayout.setVertical(Integer.parseInt(in.readLine()));
			}
			else if (line.equals("ELLIPSE")) {
				int w = Integer.parseInt(in.readLine());
				int h = Integer.parseInt(in.readLine());
				int x = Integer.parseInt(in.readLine());
				int y = Integer.parseInt(in.readLine());
				shipLayout.setShieldEllipse(w, h, x, y);
			}
			else if (line.equals("ROOM")) {
				int roomId = Integer.parseInt(in.readLine());
				int alpha = Integer.parseInt(in.readLine());
				int beta = Integer.parseInt(in.readLine());
				int hSquares = Integer.parseInt(in.readLine());
				int vSquares = Integer.parseInt(in.readLine());
				shipLayout.setRoom(roomId, alpha, beta, hSquares, vSquares);
			}
			else if (line.equals("DOOR")) {
				int wallX = Integer.parseInt(in.readLine());
				int wallY = Integer.parseInt(in.readLine());
				int roomIdA = Integer.parseInt(in.readLine());
				int roomIdB = Integer.parseInt(in.readLine());
				int vertical = Integer.parseInt(in.readLine());
				shipLayout.setDoor(wallX, wallY, vertical, roomIdA, roomIdB);
			}
		}
		return shipLayout;
	}


	public ShipChassis readChassis(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<shipChassis>"+ streamText +"</shipChassis>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(ShipChassis.class);
		Unmarshaller u = jc.createUnmarshaller();
		ShipChassis sch = (ShipChassis)u.unmarshal(domOutputter.output(doc));

		return sch;
	}

	public List<CrewNameList> readCrewNames(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<nameLists>"+ streamText  +"</nameLists>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(CrewNameLists.class);
		Unmarshaller u = jc.createUnmarshaller();
		CrewNameLists cnl = (CrewNameLists)u.unmarshal(domOutputter.output(doc));

		return cnl.getCrewNameLists();
	}


	public SectorData readSectorData(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<sectorData>"+ streamText +"</sectorData>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(SectorData.class);
		Unmarshaller u = jc.createUnmarshaller();
		SectorData sectorData = (SectorData)u.unmarshal(domOutputter.output(doc));

		return sectorData;
	}


	public Encounters readEvents(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<events>"+ streamText  +"</events>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(Encounters.class);
		Unmarshaller u = jc.createUnmarshaller();
		Encounters evts = (Encounters)u.unmarshal(domOutputter.output(doc));

		return evts;
	}


	public List<ShipEvent> readShipEvents(InputStream stream, String fileName) throws IOException, JAXBException, JDOMException {

		String streamText = TextUtilities.decodeText(stream, fileName).text;
		streamText = xmlDeclPtn.matcher(streamText).replaceFirst("");
		streamText = "<shipEvents>"+ streamText  +"</shipEvents>";
		Document doc = TextUtilities.parseStrictOrSloppyXML(streamText, fileName);
		DOMOutputter domOutputter = new DOMOutputter();

		JAXBContext jc = JAXBContext.newInstance(ShipEvents.class);
		Unmarshaller u = jc.createUnmarshaller();
		ShipEvents shvts = (ShipEvents)u.unmarshal(domOutputter.output(doc));

		return shvts.getShipEvents();
	}

}
