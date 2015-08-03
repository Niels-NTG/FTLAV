package net.blerf.ftl.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.constants.NewbieTipLevel;
import net.blerf.ftl.model.AchievementRecord;
import net.blerf.ftl.model.CrewRecord;
import net.blerf.ftl.model.Profile;
import net.blerf.ftl.model.Score;
import net.blerf.ftl.model.ShipAvailability;
import net.blerf.ftl.model.Stats;
import net.blerf.ftl.model.Stats.StatType;
import net.blerf.ftl.xml.Achievement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProfileParser extends Parser {

	private static final Logger log = LogManager.getLogger(ProfileParser.class);


	public ProfileParser() {
	}

	public Profile readProfile( File savFile ) throws IOException {
		Profile p = null;

		FileInputStream in = null;
		try {
			in = new FileInputStream( savFile );
			p = readProfile(in);
		}
		finally {
			try {if ( in != null ) in.close();}
			catch ( IOException e ) {}
		}

		return p;
	}

	public Profile readProfile( InputStream in ) throws IOException {
		Profile p = new Profile();

		int headerAlpha = readInt(in);
		if ( headerAlpha == 4 ) {
			// FTL 1.03.3 and earlier.
			p.setHeaderAlpha( headerAlpha );
		}
		else if ( headerAlpha == 9 ) {
			// FTL 1.5.4+.
			p.setHeaderAlpha( headerAlpha );

			int newbieFlag = readInt(in);
			if ( newbieFlag == 0 ) {
				p.setNewbieTipLevel( NewbieTipLevel.SHIPS_UNLOCKED );
			}
			else if ( newbieFlag == 1 ) {
				p.setNewbieTipLevel( NewbieTipLevel.SHIP_LIST_INTRO );
			}
			else if ( newbieFlag == 2 ) {
				p.setNewbieTipLevel( NewbieTipLevel.VETERAN );
			}
			else {
				throw new IOException( String.format("Unsupported newbie tip level flag: %d", newbieFlag) );
			}
		}
		else {
			throw new IOException( "Unexpected first byte ("+ headerAlpha +") for a PROFILE." );
		}

		p.setAchievements( readAchievements(in, headerAlpha) );

		p.setShipUnlockMap( readShipUnlocks(in, headerAlpha) );

		p.setStats( readStats(in, headerAlpha) );

		return p;
	}

	public void writeProfile( OutputStream out, Profile p ) throws IOException {
		writeInt( out, p.getHeaderAlpha() );

		if ( p.getHeaderAlpha() == 9 ) {
			int newbieFlag = 0;
			if ( p.getNewbieTipLevel() == NewbieTipLevel.SHIPS_UNLOCKED ) {
				newbieFlag = 0;
			}
			else if ( p.getNewbieTipLevel() == NewbieTipLevel.SHIP_LIST_INTRO ) {
				newbieFlag = 1;
			}
			else if ( p.getNewbieTipLevel() == NewbieTipLevel.VETERAN ) {
				newbieFlag = 2;
			}
			else {
				//throw new IOException( String.format("Unsupported newbie tip level: %s", p.getNewbieTipLevel().toString()) );
				log.warn( String.format("Substituting VETERAN for unsupported newbie tip level: %s", p.getNewbieTipLevel().toString()) );
				newbieFlag = 0;
			}

			writeInt( out, newbieFlag );
		}

		writeAchievements( out, p.getAchievements(), p.getHeaderAlpha() );

		writeShipUnlocks( out, p.getShipUnlockMap(), p.getHeaderAlpha() );

		writeStats( out, p.getStats(), p.getHeaderAlpha() );
	}

	private List<AchievementRecord> readAchievements( InputStream in, int headerAlpha ) throws IOException {
		int achievementCount = readInt(in);

		List<AchievementRecord> achievements = new ArrayList<AchievementRecord>( achievementCount );

		for (int i=0; i < achievementCount; i++) {
			String achId = readString(in);

			int diffFlag = readInt(in);
			Difficulty diff;
			if ( diffFlag == 0 ) {
				diff = Difficulty.EASY;
			}
			else if ( diffFlag == 1 ) {
				diff = Difficulty.NORMAL;
			}
			else if ( diffFlag == 2 && headerAlpha == 9 ) {
				diff = Difficulty.HARD;
			}
			else {
				throw new IOException( String.format("Unsupported difficulty flag for achievement %d (\"%s\"): %d", i, achId, diffFlag) );
			}

			Achievement ach = DataManager.get().getAchievement( achId );
			if ( ach == null ) {
				log.warn( "Skipping unsupported achievement id: "+ achId );
				continue;
			}

			AchievementRecord rec = new AchievementRecord( achId, diff );

			if ( headerAlpha == 9 ) {
				boolean needsVariantFlags = ach.isVictory();

				if ( needsVariantFlags ) {
					// Set whether the Type-ABC layouts completed this achievement.
					// Flag is difficulty for that run (0=EASY, 1=NORMAL, 2=HARD, -1=N/A)

					Difficulty[] variantDiffs = new Difficulty[3];

					for (int j=0; j < variantDiffs.length; j++) {
						Difficulty variantDiff = null;
						int variantDiffFlag = readInt(in);

						if ( variantDiffFlag == -1 ) {
							variantDiff = null;
						}
						else if ( variantDiffFlag == 0 ) {
							variantDiff = Difficulty.EASY;
						}
						else if ( variantDiffFlag == 1 ) {
							variantDiff = Difficulty.NORMAL;
						}
						else if ( variantDiffFlag == 2 ) {
							variantDiff = Difficulty.HARD;
						}
						else {
							throw new IOException( String.format("Unsupported per-layout difficulty flag for achievement %d (\"%s\"): %d", i, achId, variantDiffFlag) );
						}
						variantDiffs[j] = variantDiff;
					}
					rec.setCompletedWithTypeA( variantDiffs[0] );
					rec.setCompletedWithTypeB( variantDiffs[1] );
					rec.setCompletedWithTypeC( variantDiffs[2] );
				}
			}

			achievements.add( rec );
		}

		return achievements;
	}

	private void writeAchievements( OutputStream out, List<AchievementRecord> achievements, int headerAlpha ) throws IOException {
		List<String> shipBaseIds = new ArrayList<String>();  // TODO: Magic strings.
		shipBaseIds.add( "PLAYER_SHIP_HARD" );
		shipBaseIds.add( "PLAYER_SHIP_STEALTH" );
		shipBaseIds.add( "PLAYER_SHIP_MANTIS" );
		shipBaseIds.add( "PLAYER_SHIP_CIRCLE" );
		shipBaseIds.add( "PLAYER_SHIP_FED" );
		shipBaseIds.add( "PLAYER_SHIP_JELLY" );
		shipBaseIds.add( "PLAYER_SHIP_ROCK" );
		shipBaseIds.add( "PLAYER_SHIP_ENERGY" );
		shipBaseIds.add( "PLAYER_SHIP_CRYSTAL" );
		if ( headerAlpha == 9 ) {
			shipBaseIds.add( "PLAYER_SHIP_ANAEROBIC" );
		}

		List<AchievementRecord> writableAchs = new ArrayList<AchievementRecord>( achievements.size() );

		for ( AchievementRecord rec : achievements ) {

			Achievement ach = DataManager.get().getAchievement( rec.getAchievementId() );
			if ( ach == null ) {
				log.warn( "Omitting unsupported achievement id: "+ rec.getAchievementId() );
				continue;
			}

			// FTL 1.01-1.03.3 profiles have no quest or victory achievements.
			if ( headerAlpha == 4 && ( ach.isVictory() || ach.isQuest() ) ) {
				continue;
			}

			// Drop achievements for invalid ships (such as the Lanius Cruiser in
			// old profiles).
			if ( ach.getShipId() != null && !shipBaseIds.contains( ach.getShipId() ) ) {
				continue;
			}

			writableAchs.add( rec );
		}

		writeInt( out, writableAchs.size() );

		for ( AchievementRecord rec : writableAchs ) {
			writeString( out, rec.getAchievementId() );

			int diffFlag = 0;
			if ( rec.getDifficulty() == Difficulty.EASY ) {
				diffFlag = 0;
			}
			else if ( rec.getDifficulty() == Difficulty.NORMAL ) {
				diffFlag = 1;
			}
			else if ( rec.getDifficulty() == Difficulty.HARD && headerAlpha == 9 ) {
				diffFlag = 2;
			}
			else {
				//throw new IOException( String.format("Unsupported difficulty for achievement (\"%s\"): %s", rec.getAchievementId(), rec.getDifficulty().toString()) );
				log.warn( String.format("Substituting EASY for unsupported difficulty for achievement (\"%s\"): %s", rec.getAchievementId(), rec.getDifficulty().toString()) );
				diffFlag = 0;
			}
			writeInt( out, diffFlag );

			if ( headerAlpha == 9 ) {
				boolean needsVariantFlags = DataManager.get().getAchievement( rec.getAchievementId() ).isVictory();

				if ( needsVariantFlags ) {
					Difficulty[] variantDiffs = new Difficulty[3];
					variantDiffs[0] = rec.getCompletedWithTypeA();
					variantDiffs[1] = rec.getCompletedWithTypeB();
					variantDiffs[2] = rec.getCompletedWithTypeC();
					int variantDiffFlag = 0;

					for ( Difficulty variantDiff : variantDiffs ) {
						if ( variantDiff == null ) {
							variantDiffFlag = -1;
						}
						else if ( variantDiff == Difficulty.EASY ) {
							variantDiffFlag = 0;
						}
						else if ( variantDiff == Difficulty.NORMAL ) {
							variantDiffFlag = 1;
						}
						else if ( variantDiff == Difficulty.HARD ) {
							variantDiffFlag = 2;
						}
						else {
							//throw new IOException( String.format("Unsupported per-layout difficulty for achievement (\"%s\"): %s", rec.getAchievementId(), variantDiff.toString()) );
							log.warn( String.format("Substituting EASY for unsupported per-layout difficulty for achievement (\"%s\"): %s", rec.getAchievementId(), variantDiff.toString()) );
							variantDiffFlag = 0;
						}
						writeInt( out, variantDiffFlag );
					}
				}
			}
		}
	}

	private Map<String, ShipAvailability> readShipUnlocks( InputStream in, int headerAlpha ) throws IOException {
		List<String> unlockableShipIds = new ArrayList<String>();
		unlockableShipIds.add( "PLAYER_SHIP_HARD" );
		unlockableShipIds.add( "PLAYER_SHIP_STEALTH" );
		unlockableShipIds.add( "PLAYER_SHIP_MANTIS" );
		unlockableShipIds.add( "PLAYER_SHIP_CIRCLE" );
		unlockableShipIds.add( "PLAYER_SHIP_FED" );
		unlockableShipIds.add( "PLAYER_SHIP_JELLY" );
		unlockableShipIds.add( "PLAYER_SHIP_ROCK" );
		unlockableShipIds.add( "PLAYER_SHIP_ENERGY" );
		unlockableShipIds.add( "PLAYER_SHIP_CRYSTAL" );
		if ( headerAlpha == 4 ) {
			unlockableShipIds.add( "UNKNOWN_ALPHA" );
			unlockableShipIds.add( "UNKNOWN_BETA" );
			unlockableShipIds.add( "UNKNOWN_GAMMA" );
		}
		else if ( headerAlpha == 9 ) {
			unlockableShipIds.add( "PLAYER_SHIP_ANAEROBIC" );
			unlockableShipIds.add( "UNKNOWN_BETA" );
			unlockableShipIds.add( "UNKNOWN_GAMMA" );
		}
		// Yes, the profile format has 12 slots for 9 ships (10 ships with DLC).

		Map<String, ShipAvailability> shipUnlockMap = new LinkedHashMap<String, ShipAvailability>( unlockableShipIds.size() );

		for ( String shipId : unlockableShipIds ) {
			ShipAvailability shipAvail = new ShipAvailability( shipId );
			shipAvail.setUnlockedA( readBool(in) );

			if ( headerAlpha == 9 ) {
				shipAvail.setUnlockedC( readBool(in) );
			}

			shipUnlockMap.put( shipId, shipAvail );
		}

		return shipUnlockMap;
	}

	private void writeShipUnlocks( OutputStream out, Map<String, ShipAvailability> shipUnlockMap, int headerAlpha ) throws IOException {
		List<String> unlockableShipIds = new ArrayList<String>();
		unlockableShipIds.add( "PLAYER_SHIP_HARD" );
		unlockableShipIds.add( "PLAYER_SHIP_STEALTH" );
		unlockableShipIds.add( "PLAYER_SHIP_MANTIS" );
		unlockableShipIds.add( "PLAYER_SHIP_CIRCLE" );
		unlockableShipIds.add( "PLAYER_SHIP_FED" );
		unlockableShipIds.add( "PLAYER_SHIP_JELLY" );
		unlockableShipIds.add( "PLAYER_SHIP_ROCK" );
		unlockableShipIds.add( "PLAYER_SHIP_ENERGY" );
		unlockableShipIds.add( "PLAYER_SHIP_CRYSTAL" );
		if ( headerAlpha == 4 ) {
			unlockableShipIds.add( "UNKNOWN_ALPHA" );
			unlockableShipIds.add( "UNKNOWN_BETA" );
			unlockableShipIds.add( "UNKNOWN_GAMMA" );
		}
		else if ( headerAlpha == 9 ) {
			unlockableShipIds.add( "PLAYER_SHIP_ANAEROBIC" );
			unlockableShipIds.add( "UNKNOWN_BETA" );
			unlockableShipIds.add( "UNKNOWN_GAMMA" );
		}
		// Yes, the profile format has 12 slots for 9 ships (10 ships with DLC).

		for ( String shipId : unlockableShipIds ) {
			ShipAvailability shipAvail = shipUnlockMap.get( shipId );
			boolean unlockedA = false;
			boolean unlockedC = false;

			if ( shipAvail != null ) {
				unlockedA = shipAvail.isUnlockedA();
				unlockedC = shipAvail.isUnlockedC();
			}

			writeBool( out, unlockedA );

			if ( headerAlpha == 9 ) {
				writeBool( out, unlockedC );
			}
		}
	}

	private Stats readStats( InputStream in, int headerAlpha ) throws IOException {
		Stats stats = new Stats();

		// Top Scores
		stats.setTopScores( readScoreList(in, headerAlpha) );
		stats.setShipBest( readScoreList(in, headerAlpha) );

		// Stats
		stats.setIntRecord( StatType.MOST_SHIPS_DEFEATED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_SHIPS_DEFEATED, readInt(in) );
		stats.setIntRecord( StatType.MOST_BEACONS_EXPLORED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_BEACONS_EXPLORED, readInt(in) );
		stats.setIntRecord( StatType.MOST_SCRAP_COLLECTED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_SCRAP_COLLECTED, readInt(in) );
		stats.setIntRecord( StatType.MOST_CREW_HIRED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_CREW_HIRED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_GAMES_PLAYED, readInt(in) );
		stats.setIntRecord( StatType.TOTAL_VICTORIES, readInt(in) );

		stats.setCrewRecord( StatType.MOST_REPAIRS, readCrewRecord(in) );
		stats.setCrewRecord( StatType.MOST_COMBAT_KILLS, readCrewRecord(in) );
		stats.setCrewRecord( StatType.MOST_PILOTED_EVASIONS, readCrewRecord(in) );
		stats.setCrewRecord( StatType.MOST_JUMPS_SURVIVED, readCrewRecord(in) );
		stats.setCrewRecord( StatType.MOST_SKILL_MASTERIES, readCrewRecord(in) );

		return stats;
	}

	private void writeStats( OutputStream out, Stats stats, int headerAlpha ) throws IOException {
		writeScoreList( out, stats.getTopScores(), headerAlpha );
		writeScoreList( out, stats.getShipBest(), headerAlpha );

		writeInt( out, stats.getIntRecord( StatType.MOST_SHIPS_DEFEATED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_SHIPS_DEFEATED ) );
		writeInt( out, stats.getIntRecord( StatType.MOST_BEACONS_EXPLORED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_BEACONS_EXPLORED ) );
		writeInt( out, stats.getIntRecord( StatType.MOST_SCRAP_COLLECTED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_SCRAP_COLLECTED ) );
		writeInt( out, stats.getIntRecord( StatType.MOST_CREW_HIRED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_CREW_HIRED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_GAMES_PLAYED ) );
		writeInt( out, stats.getIntRecord( StatType.TOTAL_VICTORIES ) );

		writeCrewRecord( out, stats.getCrewRecord( StatType.MOST_REPAIRS ) );
		writeCrewRecord( out, stats.getCrewRecord( StatType.MOST_COMBAT_KILLS ) );
		writeCrewRecord( out, stats.getCrewRecord( StatType.MOST_PILOTED_EVASIONS ) );
		writeCrewRecord( out, stats.getCrewRecord( StatType.MOST_JUMPS_SURVIVED ) );
		writeCrewRecord( out, stats.getCrewRecord( StatType.MOST_SKILL_MASTERIES ) );
	}

	private CrewRecord readCrewRecord( InputStream in ) throws IOException {
		int value = readInt(in);
		String name = readString(in);
		String race = readString(in);
		boolean male = readBool(in);

		return new CrewRecord(name, race, male, value);
	}

	private void writeCrewRecord( OutputStream out, CrewRecord rec ) throws IOException {
		writeInt( out, rec.getValue() );
		writeString( out, rec.getName() );
		writeString( out, rec.getRace() );
		writeBool( out, rec.isMale() );
	}

	private List<Score> readScoreList( InputStream in, int headerAlpha ) throws IOException {
		int scoreCount = readInt(in);

		List<Score> scores = new ArrayList<Score>(scoreCount);

		for (int i=0; i < scoreCount; i++) {
			String shipName = readString(in);

			String shipId = readString(in);
			int value = readInt(in);
			int sector = readInt(in);
			boolean victory = readInt(in) == 1;

			// In "profile.sav", scores' difficulty had the meanings of 0 and 1 backward.

			int diffFlag = readInt(in);
			Difficulty diff;
			if ( diffFlag == 0 && headerAlpha == 4 ) {
				diff = Difficulty.NORMAL;
			}
			else if ( diffFlag == 1 && headerAlpha == 4 ) {
				diff = Difficulty.EASY;
			}
			else if ( diffFlag == 0 && headerAlpha == 9 ) {
				diff = Difficulty.EASY;
			}
			else if ( diffFlag == 1 && headerAlpha == 9 ) {
				diff = Difficulty.NORMAL;
			}
			else if ( diffFlag == 2 && headerAlpha == 9 ) {
				diff = Difficulty.HARD;
			}
			else {
				throw new IOException( String.format("Unsupported difficulty flag for score %d (\"%s\"): %d", i, shipName, diffFlag) );
			}

			Score score = new Score( shipName, shipId, value, sector, diff, victory );

			if ( headerAlpha == 9 ) {
				score.setDLCEnabled( readBool(in) );
			}

			scores.add( score );
		}

		return scores;
	}

	private void writeScoreList( OutputStream out, List<Score> scores, int headerAlpha ) throws IOException {
		writeInt( out, scores.size() );

		for ( Score score : scores ) {
			writeString( out, score.getShipName() );
			writeString( out, score.getShipId() );
			writeInt( out, score.getValue() );
			writeInt( out, score.getSector() );
			writeInt( out, (score.isVictory() ? 1 : 0) );

			int diffFlag = 0;
			if ( score.getDifficulty() == Difficulty.NORMAL && headerAlpha == 4 ) {
				diffFlag = 0;
			}
			else if ( score.getDifficulty() == Difficulty.EASY && headerAlpha == 4 ) {
				diffFlag = 1;
			}
			else if ( score.getDifficulty() == Difficulty.EASY && headerAlpha == 9 ) {
				diffFlag = 0;
			}
			else if ( score.getDifficulty() == Difficulty.NORMAL && headerAlpha == 9 ) {
				diffFlag = 1;
			}
			else if ( score.getDifficulty() == Difficulty.HARD && headerAlpha == 9 ) {
				diffFlag = 2;
			}
			else {
				//throw new IOException( String.format("Unsupported difficulty for score (\"%s\"): %s", score.getShipName(), score.getDifficulty().toString()) );
				log.warn( String.format("Substituting EASY for unsupported difficulty for score (\"%s\"): %s", score.getShipName(), score.getDifficulty().toString()) );
				diffFlag = 0;
			}
			writeInt( out, diffFlag );

			if ( headerAlpha == 9 ) {
				writeBool( out, score.isDLCEnabled() );
			}
		}
	}
}
