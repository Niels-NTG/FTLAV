package net.blerf.ftl.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.blerf.ftl.constants.NewbieTipLevel;
import net.blerf.ftl.model.Stats.StatType;


public class Profile {

    private int fileFormat;
    private List<AchievementRecord> achievements;
    private Map<String, ShipAvailability> shipUnlockMap;
    private Stats stats;

    private NewbieTipLevel newbieTipLevel = NewbieTipLevel.VETERAN;


    public Profile() {
    }

    /**
     * Copy constructor.
     * <p>
     * Each AchievementRecord, ShipAvailability, and Stats will be
     * copy-constructed as well.
     */
    public Profile(Profile srcProfile) {
        fileFormat = srcProfile.getFileFormat();

        achievements = new ArrayList<>();
        for (AchievementRecord rec : srcProfile.getAchievements()) {
            achievements.add(new AchievementRecord(rec));
        }

        shipUnlockMap = new LinkedHashMap<>();
        for (Map.Entry<String, ShipAvailability> entry : srcProfile.getShipUnlockMap().entrySet()) {
            shipUnlockMap.put(entry.getKey(), new ShipAvailability(entry.getValue()));
        }

        stats = new Stats(srcProfile.getStats());

        newbieTipLevel = srcProfile.getNewbieTipLevel();
    }


    /**
     * Sets the magic number indicating file format.
     * <p>
     * Observed values:
     * 4 = Profile, FTL 1.01-1.03.3
     * 9 = AE Profile, FTL 1.5.4+
     * <p>
     * Originally, strings in FTL profiles were windows-1252.
     * <p>
     * Unicode strings were introduced in FTL 1.6.1. There's no
     * magic number to detect that version. Assuming UTF-8 for all
     * AE profiles is reasonably safe. However, when reading/writing
     * a profile meant for earlier FTL 1.5.4+, exotic windows-1252
     * characters will be a problem, albeit an unlikely one.
     * <p>
     * There's no way to warn about such characters because the file
     * format would technically be valid, just inappropriate for
     * FTL 1.5.4+. Only ASCII will be safe.
     * <p>
     * TODO: With FTL 1.5.4+ using ANSI and the editor using unicode,
     * it's unknown how FTL will react: crash or garbled text. This
     * editor will likely throw an exception if it sees ANSI accents.
     * <p>
     * TODO: A workaround would be to make corner-case FTL versions
     * user-selectable when opening/saving,
     *
     * @see net.blerf.ftl.parser.Parser#setUnicode(boolean)
     */
    public void setFileFormat(int n) {
        fileFormat = n;
    }

    public int getFileFormat() {
        return fileFormat;
    }

    /**
     * Sets the newbie tip level.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see net.blerf.ftl.constants.NewbieTipLevel
     */
    public void setNewbieTipLevel(NewbieTipLevel level) {
        newbieTipLevel = level;
    }

    public NewbieTipLevel getNewbieTipLevel() {
        return newbieTipLevel;
    }

    public void setAchievements(List<AchievementRecord> achievements) {
        this.achievements = achievements;
    }

    public List<AchievementRecord> getAchievements() {
        return achievements;
    }

    public void setShipUnlockMap(Map<String, ShipAvailability> shipUnlockMap) {
        this.shipUnlockMap = shipUnlockMap;
    }

    public Map<String, ShipAvailability> getShipUnlockMap() {
        return shipUnlockMap;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Stats getStats() {
        return stats;
    }


    public static Profile createEmptyProfile() {
        Profile profile = new Profile();
        profile.setFileFormat(4);
        profile.setAchievements(new ArrayList<AchievementRecord>());
        profile.setShipUnlockMap(new LinkedHashMap<String, ShipAvailability>());

        Stats stats = new Stats();
        stats.setTopScores(new ArrayList<Score>());
        stats.setShipBest(new ArrayList<Score>());

        StatType[] intStatTypes = {StatType.MOST_SHIPS_DEFEATED, StatType.MOST_BEACONS_EXPLORED,
                StatType.MOST_SCRAP_COLLECTED, StatType.MOST_CREW_HIRED,
                StatType.TOTAL_SHIPS_DEFEATED, StatType.TOTAL_BEACONS_EXPLORED,
                StatType.TOTAL_SCRAP_COLLECTED, StatType.TOTAL_CREW_HIRED,
                StatType.TOTAL_GAMES_PLAYED, StatType.TOTAL_VICTORIES};
        StatType[] crewStatTypes = {StatType.MOST_REPAIRS, StatType.MOST_COMBAT_KILLS,
                StatType.MOST_PILOTED_EVASIONS, StatType.MOST_JUMPS_SURVIVED,
                StatType.MOST_SKILL_MASTERIES};
        for (StatType type : intStatTypes) {
            stats.setIntRecord(type, 0);
        }
        for (StatType type : crewStatTypes) {
            stats.setCrewRecord(type, new CrewRecord("", "", true, 0));
        }
        profile.setStats(stats);

        return profile;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        StatType[] intStatTypes = {StatType.MOST_SHIPS_DEFEATED, StatType.MOST_BEACONS_EXPLORED,
                StatType.MOST_SCRAP_COLLECTED, StatType.MOST_CREW_HIRED,
                StatType.TOTAL_SHIPS_DEFEATED, StatType.TOTAL_BEACONS_EXPLORED,
                StatType.TOTAL_SCRAP_COLLECTED, StatType.TOTAL_CREW_HIRED,
                StatType.TOTAL_GAMES_PLAYED, StatType.TOTAL_VICTORIES};
        StatType[] crewStatTypes = {StatType.MOST_REPAIRS, StatType.MOST_COMBAT_KILLS,
                StatType.MOST_PILOTED_EVASIONS, StatType.MOST_JUMPS_SURVIVED,
                StatType.MOST_SKILL_MASTERIES};

        String formatDesc = null;
        switch (fileFormat) {
            case (4):
                formatDesc = "Profile, FTL 1.01-1.03.3";
                break;
            case (9):
                formatDesc = "AE Profile, FTL 1.5.4+";
                break;
            default:
                formatDesc = "???";
                break;
        }

        result.append(String.format("File Format:            %4d (%s)%n", fileFormat, formatDesc));
        result.append(String.format("Newbie Tip Level:       %4s%n", newbieTipLevel.toString()));

        result.append("\nShip Unlocks...\n");
        first = true;
        for (ShipAvailability shipAvail : shipUnlockMap.values()) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(shipAvail.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nStats...\n");
        for (StatType type : intStatTypes) {
            result.append(String.format("%-25s %5d%n", type, stats.getIntRecord(type)));
        }

        result.append("\nCrew Records...\n");
        first = true;
        for (StatType type : crewStatTypes) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }

            CrewRecord rec = stats.getCrewRecord(type);
            result.append(String.format("%s%n", type));
            if (rec != null) {
                result.append(rec.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
            } else {
                result.append("N/A\n");
            }
        }

        result.append("\nTop Scores...\n");
        first = true;
        for (Score score : stats.getTopScores()) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(score.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nShip Best...\n");
        first = true;
        for (Score score : stats.getShipBest()) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(score.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nAchievements...\n");
        first = true;
        for (AchievementRecord rec : achievements) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(rec.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
