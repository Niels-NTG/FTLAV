package net.blerf.ftl.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Stats {

	private static final Logger log = LogManager.getLogger(Stats.class);

	public enum StatType {
		// IntRecords
		MOST_SHIPS_DEFEATED   ("Most Ships Defeated"),
		MOST_BEACONS_EXPLORED ("Most Beacons Explored"),
		MOST_SCRAP_COLLECTED  ("Most Scrap Collected"),
		MOST_CREW_HIRED       ("Most Crew Hired"),

		// CrewRecords
		MOST_REPAIRS          ("Most Repairs"),
		MOST_COMBAT_KILLS     ("Most Combat Kills"),
		MOST_PILOTED_EVASIONS ("Most Piloted Evasions"),
		MOST_JUMPS_SURVIVED   ("Most Jumps Survived"),
		MOST_SKILL_MASTERIES  ("Most Skill Masteries"),

		// IntRecords
		TOTAL_SHIPS_DEFEATED  ("Total Ships Defeated"),
		TOTAL_BEACONS_EXPLORED("Total Beacons Explored"),
		TOTAL_SCRAP_COLLECTED ("Total Scrap Collected"),
		TOTAL_CREW_HIRED      ("Total Crew Hired"),
		TOTAL_GAMES_PLAYED    ("Total Games Played"),
		TOTAL_VICTORIES       ("Total Victories");

		private final String name;
		StatType(String name) {
			this.name = name;
		}
		public String getName() { return name; }
		public String toString() { return name; }
	}

	private List<Score> topScores = new ArrayList<>();
	private List<Score> shipBest = new ArrayList<>();

	private final Map<StatType,CrewRecord> crewMap = new EnumMap<>(StatType.class);
	private final Map<StatType,Integer> intMap = new EnumMap<>(StatType.class);


	public Stats() {
	}

	/**
	 * Copy constructor.
	 *
	 * Each Score and CrewRecord will be copy-constructed as well.
	 */
	public Stats(Stats srcStats) {
		for (Score s : srcStats.getTopScores()) {
			topScores.add(new Score(s));
		}

		for (Score s : srcStats.getShipBest()) {
			shipBest.add(new Score(s));
		}

		for (Map.Entry<StatType, CrewRecord> entry : srcStats.getCrewRecordMap().entrySet()) {
			crewMap.put(entry.getKey(), new CrewRecord(entry.getValue()));
		}

		for (Map.Entry<StatType, Integer> entry : srcStats.getIntRecordMap().entrySet()) {
			intMap.put(entry.getKey(), entry.getValue());
		}
	}

	public List<Score> getTopScores() { return topScores; }
	public List<Score> getShipBest() { return shipBest; }

	public Map<StatType, CrewRecord> getCrewRecordMap() { return crewMap; }
	public Map<StatType, Integer> getIntRecordMap() { return intMap; }

	public CrewRecord getCrewRecord(StatType  type) {
		if (!crewMap.containsKey(type))
			log.error("No crew record found for type: "+ type);
		return crewMap.get(type);
	}

	public int getIntRecord(StatType type) {
		if (!intMap.containsKey(type))
			log.error("No int record found for type: "+ type);
		return intMap.get(type);
	}

	public int getMostShipsDefeated() { return getIntRecord(StatType.MOST_SHIPS_DEFEATED); }
	public int getMostBeaconsExplored() { return getIntRecord(StatType.MOST_BEACONS_EXPLORED); }
	public int getMostScrapCollected() { return getIntRecord(StatType.MOST_SCRAP_COLLECTED); }
	public int getMostCrewHired() { return getIntRecord(StatType.MOST_CREW_HIRED); }

	public int getTotalShipsDefeated() { return getIntRecord(StatType.TOTAL_SHIPS_DEFEATED); }
	public int getTotalBeaconsExplored() { return getIntRecord(StatType.TOTAL_BEACONS_EXPLORED); }
	public int getTotalScrapCollected() { return getIntRecord(StatType.TOTAL_SCRAP_COLLECTED); }
	public int getTotalCrewHired() { return getIntRecord(StatType.TOTAL_CREW_HIRED); }
	public int getTotalGamesPlayed() { return getIntRecord(StatType.TOTAL_GAMES_PLAYED); }
	public int getTotalVictories() { return getIntRecord(StatType.TOTAL_VICTORIES); }

	public CrewRecord getMostRepairs() { return getCrewRecord(StatType.MOST_REPAIRS); }
	public CrewRecord getMostKills() { return getCrewRecord(StatType.MOST_COMBAT_KILLS); }
	public CrewRecord getMostEvasions() { return getCrewRecord(StatType.MOST_PILOTED_EVASIONS); }
	public CrewRecord getMostJumps() { return getCrewRecord(StatType.MOST_JUMPS_SURVIVED); }
	public CrewRecord getMostSkills() { return getCrewRecord(StatType.MOST_SKILL_MASTERIES); }

}
