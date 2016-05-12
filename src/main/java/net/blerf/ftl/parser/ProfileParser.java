package net.blerf.ftl.parser;

import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.model.CrewRecord;
import net.blerf.ftl.model.Profile;
import net.blerf.ftl.model.Score;
import net.blerf.ftl.model.Stats;
import net.blerf.ftl.model.Stats.StatType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ProfileParser extends Parser {


	public ProfileParser() {
	}


	public Profile readProfile(InputStream in) throws IOException {
		Profile p = new Profile();

		int headerAlpha = readInt(in);
		if (headerAlpha == 4) {
			// FTL 1.03.3 and earlier.
			p.setHeaderAlpha(headerAlpha);
		} else if (headerAlpha == 9) {
			// FTL 1.5.4+.
			p.setHeaderAlpha(headerAlpha);
			readInt(in); // skip newbie flag
		} else {
			throw new IOException("Unexpected first byte ("+ headerAlpha +") for a PROFILE.");
		}

		// skips bytes on achievements and ship unlocks to get to the more interesting statistics part
		skipAchievements(in, headerAlpha);
		skipShipUnlocks(in, headerAlpha);

		p.setStats(readStats(in, headerAlpha));

		return p;
	}


	private void skipAchievements(InputStream in, int headerAlpha) throws IOException {
		int achievementCount = readInt(in); // number of achievements to skip
		for (int i = 0; i < achievementCount; i++) {
			String achID = readString(in); // skip achievement ID
			readInt(in); // skip difficulty flag
			if (headerAlpha == 9) {
				if (achID.contains("VICTORY")) {
					for (int j = 0; j < 3; j++) {
						readInt(in); // skip victory achievement
					}
				}
			}
		}
	}


	private void skipShipUnlocks(InputStream in, int headerAlpha ) throws IOException {
		// Yes, the profile format has 12 slots for 9 ships (10 ships with DLC).
		for (int i = 0; i < 12; i++) {
			readBool(in); // skip unlocked A/B ship layouts
			if (headerAlpha == 9) {
				readBool(in); // skip unlocked C ship layouts
			}
		}
	}


	private Stats readStats(InputStream in, int headerAlpha) throws IOException {
		Stats stats = new Stats();

		// Top Scores
		stats.setTopScores(readScoreList(in, headerAlpha));
		stats.setShipBest(readScoreList(in, headerAlpha));

		// Stats
		stats.setIntRecord(StatType.MOST_SHIPS_DEFEATED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_SHIPS_DEFEATED, readInt(in));
		stats.setIntRecord(StatType.MOST_BEACONS_EXPLORED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_BEACONS_EXPLORED, readInt(in));
		stats.setIntRecord(StatType.MOST_SCRAP_COLLECTED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_SCRAP_COLLECTED, readInt(in));
		stats.setIntRecord(StatType.MOST_CREW_HIRED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_CREW_HIRED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_GAMES_PLAYED, readInt(in));
		stats.setIntRecord(StatType.TOTAL_VICTORIES, readInt(in));

		stats.setCrewRecord(StatType.MOST_REPAIRS, readCrewRecord(in));
		stats.setCrewRecord(StatType.MOST_COMBAT_KILLS, readCrewRecord(in));
		stats.setCrewRecord(StatType.MOST_PILOTED_EVASIONS, readCrewRecord(in));
		stats.setCrewRecord(StatType.MOST_JUMPS_SURVIVED, readCrewRecord(in));
		stats.setCrewRecord(StatType.MOST_SKILL_MASTERIES, readCrewRecord(in));

		return stats;
	}


	private CrewRecord readCrewRecord(InputStream in) throws IOException {
		int value = readInt(in);
		String name = readString(in);
		String race = readString(in);
		boolean male = readBool(in);

		return new CrewRecord(name, race, male, value);
	}


	private ArrayList<Score> readScoreList(InputStream in, int headerAlpha) throws IOException {
		int scoreCount = readInt(in);

		ArrayList<Score> scores = new ArrayList<>(scoreCount);

		for (int i=0; i < scoreCount; i++) {
			String shipName = readString(in);

			String shipId = readString(in);
			int value = readInt(in);
			int sector = readInt(in);
			boolean victory = readInt(in) == 1;

			// In "profileFile.sav", scores' difficulty had the meanings of 0 and 1 backward.

			int diffFlag = readInt(in);
			Difficulty diff;
			if (diffFlag == 0 && headerAlpha == 4) {
				diff = Difficulty.NORMAL;
			} else if (diffFlag == 1 && headerAlpha == 4) {
				diff = Difficulty.EASY;
			} else if (diffFlag == 0 && headerAlpha == 9) {
				diff = Difficulty.EASY;
			} else if (diffFlag == 1 && headerAlpha == 9) {
				diff = Difficulty.NORMAL;
			} else if (diffFlag == 2 && headerAlpha == 9) {
				diff = Difficulty.HARD;
			} else {
				throw new IOException(
					String.format(
						"Unsupported difficulty flag for score %d (\"%s\"): %d",
						i,
						shipName,
						diffFlag
					)
				);
			}

			Score score = new Score(shipName, shipId, value, sector, diff, victory);

			if (headerAlpha == 9) {
				score.setDLCEnabled(readBool(in));
			}

			scores.add(score);
		}

		return scores;
	}

}
