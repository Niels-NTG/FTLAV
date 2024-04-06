package net.blerf.ftl.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Stats {

    public enum StatType {
        // IntRecords
        MOST_SHIPS_DEFEATED("Most Ships Defeated"),
        MOST_BEACONS_EXPLORED("Most Beacons Explored"),
        MOST_SCRAP_COLLECTED("Most Scrap Collected"),
        MOST_CREW_HIRED("Most Crew Hired"),

        // CrewRecords
        MOST_REPAIRS("Most Repairs"),
        MOST_COMBAT_KILLS("Most Combat Kills"),
        MOST_PILOTED_EVASIONS("Most Piloted Evasions"),
        MOST_JUMPS_SURVIVED("Most Jumps Survived"),
        MOST_SKILL_MASTERIES("Most Skill Masteries"),

        // IntRecords
        TOTAL_SHIPS_DEFEATED("Total Ships Defeated"),
        TOTAL_BEACONS_EXPLORED("Total Beacons Explored"),
        TOTAL_SCRAP_COLLECTED("Total Scrap Collected"),
        TOTAL_CREW_HIRED("Total Crew Hired"),
        TOTAL_GAMES_PLAYED("Total Games Played"),
        TOTAL_VICTORIES("Total Victories");

        private final String name;

        StatType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }
    }

    private List<Score> topScores = new ArrayList<Score>();
    private List<Score> shipBest = new ArrayList<Score>();

    private final Map<StatType, CrewRecord> crewMap = new EnumMap<StatType, CrewRecord>(StatType.class);
    private final Map<StatType, Integer> intMap = new EnumMap<StatType, Integer>(StatType.class);


    public Stats() {
    }

    /**
     * Copy constructor.
     * <p>
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
            // Integer wrapper is immutable, no need for defensive copying.
            intMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void setTopScores(List<Score> topScores) {
        this.topScores = topScores;
    }

    public void setShipBest(List<Score> shipBest) {
        this.shipBest = shipBest;
    }

    public List<Score> getTopScores() {
        return topScores;
    }

    public List<Score> getShipBest() {
        return shipBest;
    }

    public Map<StatType, CrewRecord> getCrewRecordMap() {
        return crewMap;
    }

    public Map<StatType, Integer> getIntRecordMap() {
        return intMap;
    }

    public void setCrewRecord(StatType type, CrewRecord r) {
        crewMap.put(type, r);
    }

    public CrewRecord getCrewRecord(StatType type) {
        if (!crewMap.containsKey(type))
            log.error("No crew record found for type: {}", type);
        return crewMap.get(type);
    }

    public void setIntRecord(StatType type, int n) {
        intMap.put(type, n);
    }

    public int getIntRecord(StatType type) {
        if (!intMap.containsKey(type))
            log.error("No int record found for type: {}", type);
        return intMap.get(type);
    }

    public void setMostShipsDefeated(int n) {
        setIntRecord(StatType.MOST_SHIPS_DEFEATED, n);
    }

    public void setMostBeaconsExplored(int n) {
        setIntRecord(StatType.MOST_BEACONS_EXPLORED, n);
    }

    public void setMostScrapCollected(int n) {
        setIntRecord(StatType.MOST_SCRAP_COLLECTED, n);
    }

    public void setMostCrewHired(int n) {
        setIntRecord(StatType.MOST_CREW_HIRED, n);
    }

    public int getMostShipsDefeated() {
        return getIntRecord(StatType.MOST_SHIPS_DEFEATED);
    }

    public int getMostBeaconsExplored() {
        return getIntRecord(StatType.MOST_BEACONS_EXPLORED);
    }

    public int getMostScrapCollected() {
        return getIntRecord(StatType.MOST_SCRAP_COLLECTED);
    }

    public int getMostCrewHired() {
        return getIntRecord(StatType.MOST_CREW_HIRED);
    }

    public void setTotalShipsDefeated(int n) {
        setIntRecord(StatType.TOTAL_SHIPS_DEFEATED, n);
    }

    public void setTotalBeaconsExplored(int n) {
        setIntRecord(StatType.TOTAL_BEACONS_EXPLORED, n);
    }

    public void setTotalScrapCollected(int n) {
        setIntRecord(StatType.TOTAL_SCRAP_COLLECTED, n);
    }

    public void setTotalCrewHired(int n) {
        setIntRecord(StatType.TOTAL_CREW_HIRED, n);
    }

    public void setTotalGamesPlayed(int n) {
        setIntRecord(StatType.TOTAL_GAMES_PLAYED, n);
    }

    public void setTotalVictories(int n) {
        setIntRecord(StatType.TOTAL_VICTORIES, n);
    }

    public int getTotalShipsDefeated() {
        return getIntRecord(StatType.TOTAL_SHIPS_DEFEATED);
    }

    public int getTotalBeaconsExplored() {
        return getIntRecord(StatType.TOTAL_BEACONS_EXPLORED);
    }

    public int getTotalScrapCollected() {
        return getIntRecord(StatType.TOTAL_SCRAP_COLLECTED);
    }

    public int getTotalCrewHired() {
        return getIntRecord(StatType.TOTAL_CREW_HIRED);
    }

    public int getTotalGamesPlayed() {
        return getIntRecord(StatType.TOTAL_GAMES_PLAYED);
    }

    public int getTotalVictories() {
        return getIntRecord(StatType.TOTAL_VICTORIES);
    }

    public void setMostRepairs(CrewRecord record) {
        setCrewRecord(StatType.MOST_REPAIRS, record);
    }

    public void setMostKills(CrewRecord record) {
        setCrewRecord(StatType.MOST_COMBAT_KILLS, record);
    }

    public void setMostEvasions(CrewRecord record) {
        setCrewRecord(StatType.MOST_PILOTED_EVASIONS, record);
    }

    public void setMostJumps(CrewRecord record) {
        setCrewRecord(StatType.MOST_JUMPS_SURVIVED, record);
    }

    public void setMostSkills(CrewRecord record) {
        setCrewRecord(StatType.MOST_SKILL_MASTERIES, record);
    }

    public CrewRecord getMostRepairs() {
        return getCrewRecord(StatType.MOST_REPAIRS);
    }

    public CrewRecord getMostKills() {
        return getCrewRecord(StatType.MOST_COMBAT_KILLS);
    }

    public CrewRecord getMostEvasions() {
        return getCrewRecord(StatType.MOST_PILOTED_EVASIONS);
    }

    public CrewRecord getMostJumps() {
        return getCrewRecord(StatType.MOST_JUMPS_SURVIVED);
    }

    public CrewRecord getMostSkills() {
        return getCrewRecord(StatType.MOST_SKILL_MASTERIES);
    }
}
