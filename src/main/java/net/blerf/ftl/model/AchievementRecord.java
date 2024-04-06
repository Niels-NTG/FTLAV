package net.blerf.ftl.model;

import java.util.List;
import java.util.ListIterator;
import net.blerf.ftl.constants.Difficulty;


public class AchievementRecord {

    private String achievementId;
    private Difficulty difficulty;

    private Difficulty typeADiff = null;
    private Difficulty typeBDiff = null;
    private Difficulty typeCDiff = null;


    public AchievementRecord(String achievementId, Difficulty difficulty) {
        this.achievementId = achievementId;
        this.difficulty = difficulty;
    }

    /**
     * Copy constructor.
     */
    public AchievementRecord(AchievementRecord srcAch) {
        achievementId = srcAch.getAchievementId();
        difficulty = srcAch.getDifficulty();

        typeADiff = srcAch.getCompletedWithTypeA();
        typeBDiff = srcAch.getCompletedWithTypeB();
        typeCDiff = srcAch.getCompletedWithTypeC();
    }

    public void setAchievementId(String s) {
        achievementId = s;
    }

    public String getAchievementId() {
        return achievementId;
    }

    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets whether a ship's Type-A layout completed this achievement.
     * <p>
     * The game will synchronize the difficulty of the achievement and all
     * layout types with the highest one among them.
     * <p>
     * This manifests in-game as a "V" on the ship list, with three dots.
     * <p>
     * Note: This is only used by PLAYER_SHIP_*_VICTORY achievements.
     * This was introduced in FTL 1.5.4.
     *
     * @param d difficulty for that run, or null
     */
    public void setCompletedWithTypeA(Difficulty d) {
        typeADiff = d;
    }

    public void setCompletedWithTypeB(Difficulty d) {
        typeBDiff = d;
    }

    public void setCompletedWithTypeC(Difficulty d) {
        typeCDiff = d;
    }

    public Difficulty getCompletedWithTypeA() {
        return typeADiff;
    }

    public Difficulty getCompletedWithTypeB() {
        return typeBDiff;
    }

    public Difficulty getCompletedWithTypeC() {
        return typeCDiff;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String typeADiffString = (typeADiff != null ? typeADiff.toString() : "N/A");
        String typeBDiffString = (typeBDiff != null ? typeBDiff.toString() : "N/A");
        String typeCDiffString = (typeCDiff != null ? typeCDiff.toString() : "N/A");

        result.append(String.format("AchId: %-30s  Difficulty: %-6s%n", achievementId, difficulty.toString()));

        result.append(String.format("With Type-A: %-6s  With Type-B: %-6s  With Type-C: %-6s%n", typeADiffString, typeBDiffString, typeCDiffString));

        return result.toString();
    }


    public static AchievementRecord getFromListById(List<AchievementRecord> achList, String achievementId) {
        for (AchievementRecord rec : achList) {
            if (rec.getAchievementId().equals(achievementId)) {
                return rec;
            }
        }
        return null;
    }

    public static void removeFromListById(List<AchievementRecord> achList, String achievementId) {
        for (ListIterator<AchievementRecord> it = achList.listIterator(); it.hasNext(); ) {
            AchievementRecord rec = it.next();
            if (rec.getAchievementId().equals(achievementId)) {
                it.remove();
            }
        }
    }
}
