package net.blerf.ftl.model;

import net.blerf.ftl.constants.NewbieTipLevel;
import net.blerf.ftl.model.Stats.StatType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Profile {

	private int unknownHeaderAlpha;
	private List<AchievementRecord> achievements;
	private Map<String, ShipAvailability> shipUnlockMap;
	private Stats stats;

	private NewbieTipLevel newbieTipLevel = NewbieTipLevel.VETERAN;


	public Profile() {
	}

	/**
	 * Copy constructor.
	 *
	 * Each AchievementRecord, ShipAvailability, and Stats will be
	 * copy-constructed as well.
	 */
	public Profile(Profile srcProfile) {
		unknownHeaderAlpha = srcProfile.getHeaderAlpha();

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


	public int getHeaderAlpha() {
		return unknownHeaderAlpha;
	}

	/**
	 * Sets the newbie tip level.
	 *
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

	public List<AchievementRecord> getAchievements() {
		return achievements;
	}

	public Map<String, ShipAvailability> getShipUnlockMap() {
		return shipUnlockMap;
	}

	public Stats getStats() {
		return stats;
	}


	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		boolean first;

		StatType[] intStatTypes = {StatType.MOST_SHIPS_DEFEATED, StatType.MOST_BEACONS_EXPLORED,
		                           StatType.MOST_SCRAP_COLLECTED, StatType.MOST_CREW_HIRED,
		                           StatType.TOTAL_SHIPS_DEFEATED, StatType.TOTAL_BEACONS_EXPLORED,
		                           StatType.TOTAL_SCRAP_COLLECTED, StatType.TOTAL_CREW_HIRED,
                               StatType.TOTAL_GAMES_PLAYED, StatType.TOTAL_VICTORIES};
		StatType[] crewStatTypes = {StatType.MOST_REPAIRS, StatType.MOST_COMBAT_KILLS,
		                            StatType.MOST_PILOTED_EVASIONS, StatType.MOST_JUMPS_SURVIVED,
		                            StatType.MOST_SKILL_MASTERIES};

		String formatDesc;
		switch (unknownHeaderAlpha) {
			case(4): formatDesc = "Profile, FTL 1.01-1.03.3"; break;
			case(9): formatDesc = "AE Profile, FTL 1.5.4+"; break;
			default: formatDesc = "???"; break;
		}

		result.append(String.format("File Format:            %4d (%s)\n", unknownHeaderAlpha, formatDesc));
		result.append(String.format("Newbie Tip Level:       %4s\n", newbieTipLevel.toString()));

		result.append("\nShip Unlocks...\n");
		first = true;
		for(ShipAvailability shipAvail : shipUnlockMap.values()) {
			if (first) { first = false; }
			else { result.append(",\n"); }
			result.append(shipAvail.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
		}

		result.append("\nStats...\n");
		for (StatType type : intStatTypes) {
			result.append(String.format("%-25s %5d\n", type.toString(), stats.getIntRecord(type)));
		}

		result.append("\nCrew Records...\n");
		first = true;
		for (StatType type : crewStatTypes) {
			if (first) { first = false; }
			else { result.append(",\n"); }

			CrewRecord rec = stats.getCrewRecord(type);
			result.append(String.format("%s\n", type.toString()));
			if (rec != null) {
				result.append(rec.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
			} else {
				result.append("N/A\n");
			}
		}

		result.append("\nTop Scores...\n");
		first = true;
		for(Score score : stats.getTopScores()) {
			if (first) { first = false; }
			else { result.append(",\n"); }
			result.append(score.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
		}

		result.append("\nShip Best...\n");
		first = true;
		for(Score score : stats.getShipBest()) {
			if (first) { first = false; }
			else { result.append(",\n"); }
			result.append(score.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
		}

		result.append("\nAchievements...\n");
		first = true;
		for(AchievementRecord rec : achievements) {
			if (first) { first = false; }
			else { result.append(",\n"); }
			result.append(rec.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
		}

		return result.toString();
	}
}
