package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.constants.AdvancedFTLConstants;
import net.blerf.ftl.constants.FTLConstants;
import net.blerf.ftl.constants.OriginalFTLConstants;
import net.blerf.ftl.model.type.CrewType;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.CrewBlueprint;

/**
 * Crew.
 * <p>
 * Zoltan-produced power is not stored in SystemState.
 * <p>
 * TODO: Disrupting Mind system stuns and turns enemy crew against each
 * other, yet doesn't seem to modify CrewStates!?
 */
@Getter
@Setter
@NoArgsConstructor
public class CrewState {

    private String name = "Frank";
    private CrewType race = CrewType.HUMAN;
    /**
     * Whether this crew member is a hostile boarding drone.
     * <p>
     * Upon loading after setting this on a crew member, name will change
     * to "Anti-Personnel Drone", race will be "battle", and
     * playerControlled will be false on the player ship or true on a
     * nearby ship.
     * <p>
     * If after loading in-game, you re-edit this to false and leave the
     * "battle" race, the game will change it to "human".
     * <p>
     * Drones on nearby ships (which are playerControlled) will not be
     * preserved the next time the game saves, even if you modify the
     * player ship's drone list to have an armed boarder.
     * <p>
     * Presumably this is so intruders can persist without a ship, which
     * would normally have a drones section to contain them.
     * <p>
     * TODO: Jump away from Boss #2 to see what its drone is
     * ("blueprints.xml" mentions BOARDER_BOSS).
     */
    private boolean enemyBoardingDrone = false;
    /**
     * Sets this crew's current hit points.
     * <p>
     * For preserved dead crew, which have no body, this is 0.
     * <p>
     * This value includes any temporary modifiers, and may exceed the
     * CrewType's normal max health. FTL 1.01-1.03.3 had no such modifiers
     * and capped health at the max.
     *
     * @see #healthBoost
     */
    private int health = 0;

    /**
     * The position of the crew's image.
     * <p>
     * Technically the roomId/square fields set the crew's goal location.
     * This field is where the body really is, possibly en route.
     * <p>
     * It's the position of the body image's center, relative to the
     * top-left corner of the floor layout of the ship it's on.
     * <p>
     * For preserved dead crew, which have no body, this lingers, or may be
     * (0,0).
     */
    private int spriteX = 0;
    private int spriteY = 0;

    /**
     * Sets the room this crew is in (or at least trying to move toward).
     * <p>
     * For preserved dead crew, which have no body, this is -1.
     * <p>
     * roomId and roomSquare need to be specified together.
     */
    private int roomId = -1;
    private int roomSquare = -1;
    private boolean playerControlled = false;
    private int cloneReady = 0;
    /**
     * List of tints to apply to the sprite.
     * <p>
     * The tints themselves are defined in
     * "blueprints.xml":crewBlueprint/colorList
     * <p>
     * The first Integer in the list corresponds to the first 'layer' tag
     * in the xml, and the Integer's value is the nth 'color' tag to use.
     * <p>
     * Note: Normal NPC crew have a hardcoded range of layer/color indeces
     * based on stock blueprints. When mods add layers to a race blueprint,
     * NPCs that spawn won't use them. When mods remove layers, NPCs that
     * reference the missing layer/colors display as a gray rectangle.
     * <p>
     * Stock layers(colors): anaerobic=1(4), crystal=1(4), energy=1(5),
     * engi=0(0), human=2(2,4), mantis=1(9), rock=1(7), slug=1(8).
     * <p>
     * TODO: Test if FTL honors non-standard tints of edited NPCs.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private List<Integer> spriteTintIndices = new ArrayList<>();
    private boolean mindControlled = false;

    /**
     * Sets a saved position to return to.
     * <p>
     * roomId and roomSquare need to be specified together.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int savedRoomId = 0;
    private int savedRoomSquare = 0;
    private int pilotSkill = 0;
    private int engineSkill = 0;
    private int shieldSkill = 0;
    private int weaponSkill = 0;
    private int repairSkill = 0;
    private int combatSkill = 0;

    /**
     * Humans with this set to false have a female image. Other races
     * accept the flag but use the same image as male.
     * <p>
     * No Andorians in the game, so this is only a two-state boolean.
     */
    private boolean male = true;
    private int repairs = 0;
    private int combatKills = 0;
    private int pilotedEvasions = 0;
    private int jumpsSurvived = 0;

    /**
     * Total number of skill masteries ever earned by this crew.
     * <p>
     * This is incremented when any skill reaches the first or second
     * mastery interval. So this is intended to max out at 12.
     * <p>
     * Bug: In FTL 1.5.4-1.5.10, Clonebay skill penalties allowed crew to
     * re-earn masteries and increment further.
     * <p>
     * FTL 1.5.12 added methods to track each level of each skill
     * individually.
     */
    private int skillMasteriesEarned = 0;
    private int stunTicks = 0;
    /**
     * Sets temporary bonus health from a foreign Mind Control system.
     * <p>
     * Observed values:
     * 15 = Mind Ctrl Level 2.
     * 30 = Mind Ctrl Level 3.
     * <p>
     * When the mind control effect expires, the boost amount will be
     * subtracted from health, and this value will reset to 0.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private int healthBoost = 0;
    private int clonebayPriority = -1;

    /**
     * Sets a multiplier to apply to damage dealt by this crew.
     * <p>
     * Observed values:
     * 1250 (1.25) = Mind Ctrl Level 2.
     * 2000 (2.00) = Mind Ctrl Level 3.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @param a pseudo-float (1000 is 1.0)
     */
    private int damageBoost = 1000;
    private int unknownLambda = 0;
    private int universalDeathCount = 0;
    private boolean pilotMasteryOne = false;
    private boolean pilotMasteryTwo = false;
    private boolean engineMasteryOne = false;
    private boolean engineMasteryTwo = false;
    private boolean shieldMasteryOne = false;
    private boolean shieldMasteryTwo = false;
    private boolean weaponMasteryOne = false;
    private boolean weaponMasteryTwo = false;
    private boolean repairMasteryOne = false;
    private boolean repairMasteryTwo = false;
    private boolean combatMasteryOne = false;
    private boolean combatMasteryTwo = false;
    private boolean unknownNu = false;
    private AnimState teleportAnim = new AnimState();
    private boolean unknownPhi = false;
    private int lockdownRechargeTicks = 0;
    private int lockdownRechargeTicksGoal = 0;
    private int unknownOmega = 0;

    /**
     * Copy constructor.
     */
    public CrewState(CrewState srcCrew) {
        name = srcCrew.getName();
        race = srcCrew.getRace();
        enemyBoardingDrone = srcCrew.isEnemyBoardingDrone();
        health = srcCrew.getHealth();
        spriteX = srcCrew.getSpriteX();
        spriteY = srcCrew.getSpriteY();
        roomId = srcCrew.getRoomId();
        roomSquare = srcCrew.getRoomSquare();
        playerControlled = srcCrew.isPlayerControlled();
        cloneReady = srcCrew.getCloneReady();

        // Integer wrapper is immutable, no need for defensive copying.
        spriteTintIndices.addAll(srcCrew.getSpriteTintIndices());

        mindControlled = srcCrew.isMindControlled();
        savedRoomId = srcCrew.getSavedRoomId();
        savedRoomSquare = srcCrew.getSavedRoomSquare();
        pilotSkill = srcCrew.getPilotSkill();
        engineSkill = srcCrew.getEngineSkill();
        shieldSkill = srcCrew.getShieldSkill();
        weaponSkill = srcCrew.getWeaponSkill();
        repairSkill = srcCrew.getRepairSkill();
        combatSkill = srcCrew.getCombatSkill();
        male = srcCrew.isMale();
        repairs = srcCrew.getRepairs();
        combatKills = srcCrew.getCombatKills();
        pilotedEvasions = srcCrew.getPilotedEvasions();
        jumpsSurvived = srcCrew.getJumpsSurvived();
        skillMasteriesEarned = srcCrew.getSkillMasteriesEarned();
        stunTicks = srcCrew.getStunTicks();
        healthBoost = srcCrew.getHealthBoost();
        clonebayPriority = srcCrew.getClonebayPriority();
        damageBoost = srcCrew.getDamageBoost();
        unknownLambda = srcCrew.getUnknownLambda();
        universalDeathCount = srcCrew.getUniversalDeathCount();
        pilotMasteryOne = srcCrew.isPilotMasteryOne();
        pilotMasteryTwo = srcCrew.isPilotMasteryTwo();
        engineMasteryOne = srcCrew.isEngineMasteryOne();
        engineMasteryTwo = srcCrew.isEngineMasteryTwo();
        shieldMasteryOne = srcCrew.isShieldMasteryOne();
        shieldMasteryTwo = srcCrew.isShieldMasteryTwo();
        weaponMasteryOne = srcCrew.isWeaponMasteryOne();
        weaponMasteryTwo = srcCrew.isWeaponMasteryTwo();
        repairMasteryOne = srcCrew.isRepairMasteryOne();
        repairMasteryTwo = srcCrew.isRepairMasteryTwo();
        combatMasteryOne = srcCrew.isCombatMasteryOne();
        combatMasteryTwo = srcCrew.isCombatMasteryTwo();
        unknownNu = srcCrew.isUnknownNu();
        teleportAnim = srcCrew.getTeleportAnim();
        unknownPhi = srcCrew.isUnknownPhi();
        lockdownRechargeTicks = srcCrew.getLockdownRechargeTicks();
        lockdownRechargeTicksGoal = srcCrew.getLockdownRechargeTicksGoal();
        unknownOmega = srcCrew.getUnknownOmega();
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Normal Crew will NOT have their playerControlled status toggled.
     * <p>
     * Crew flagged as enemy boarding drones will remain so; when a nearby
     * ship becomes the player ship, such crew, which formerly belonged to
     * the player, will then be hostile to the player. Their playerControlled
     * status will be set to false, as FTL woulld set it on the player ship.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {

        if (isEnemyBoardingDrone()) {
            setPlayerControlled(false);
        }

        setCloneReady(0);  // TODO: Vet this default.
        setMindControlled(false);

        setSavedRoomId(-1);
        setSavedRoomSquare(-1);

        setStunTicks(0);

        if (getHealthBoost() > 0) {
            setHealth(getHealth() - getHealthBoost());
            setHealthBoost(0);
        }

        setDamageBoost(0);
        setUnknownLambda(0);               // TODO: Vet this default;
        setUnknownNu(false);               // TODO: Vet this default;

        getTeleportAnim().setPlaying(false);
        getTeleportAnim().setCurrentFrame(0);
        getTeleportAnim().setProgressTicks(0);

        setUnknownPhi(false);              // TODO: Vet this default;
        setLockdownRechargeTicks(0);
        setLockdownRechargeTicksGoal(0);
        setUnknownOmega(0);                // TODO: Vet this default;
    }


    /**
     * Sets the square this crew is in (or at least trying to move toward).
     * <p>
     * Squares are indexed horizontally, left-to-right, wrapping into the
     * next row down.
     * <p>
     * For preserved dead crew, which have no body, this is -1.
     * <p>
     * roomId and roomSquare need to be specified together.
     */
    public void setRoomSquare(int n) {
        roomSquare = n;
    }

    /**
     * Sets whether this crew is mind controlled.
     * <p>
     * While setPlayerControlled() is permanent this temporarily yields
     * control to the opposing side.
     * <p>
     * TODO: Determine what circumstances cause this to wear off.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setMindControlled(boolean b) {
        mindControlled = b;
    }

    /**
     * Sets time elapsed while waiting for a stun effect to wear off.
     * <p>
     * If greater than 0, the crew will become unresponsive while this
     * number decrements to 0.
     * <p>
     * A weapon sets X*1000 ticks, where X is the value of the 'stun' tag
     * in its WeaponBlueprint xml. Additional hits from a stun weapon will
     * reset this value, but only if it would be higher than the current
     * value.
     * <p>
     * When not stunned, this will be 0.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setStunTicks(int n) {
        stunTicks = n;
    }


    /**
     * Sets the Clonebay's queue priority for this crew (lowest is first).
     * <p>
     * When this crew dies, this is set to the newly incremented universal
     * death count. Then this value lingers. When this crew has not yet
     * died, this is -1.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setUniversalDeathCount(int)
     */
    public void setClonebayPriority(int n) {
        clonebayPriority = n;
    }


    /**
     * Unknown.
     * <p>
     * Matthew's hint: dyingTimer.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownLambda(int n) {
        unknownLambda = n;
    }


    /**
     * Sets the total deaths of all crew everywhere.
     * <p>
     * All crew, friend and foe, have an identical field, which increments
     * whenever someone dies. When nobody has died yet, this is 0.
     * <p>
     * According to Matthew, FTL made this a static variable on the crew
     * class. It's purpose was to serve as an ever-increasing number to mark
     * the deceased with a unique Clonebay queue priority to sort by.
     * There's a comment in FTL's source that says "This is stupid."
     * <p>
     * Note: Any time this is is set, the same value should be set on all
     * CrewStates.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setClonebayPriority(int)
     */
    public void setUniversalDeathCount(int n) {
        universalDeathCount = n;
    }

    /**
     * Sets whether this crew ever earned the first level of pilot mastery.
     * <p>
     * This value does not affect the in-game progress bars. It's probably
     * solely for the "Most Skill Masteries" stat.
     * <p>
     * This was introduced in FTL 1.5.12.
     *
     * @see #setSkillMasteries(int)
     * @see net.blerf.ftl.model.Stats.StatType#MOST_SKILL_MASTERIES
     */
    public void setPilotMasteryOne(boolean b) {
        pilotMasteryOne = b;
    }


    /**
     * Unknown.
     * <p>
     * Went from 0 to 1 while fresh clone materialized via the teleport
     * anim.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownNu(boolean b) {
        unknownNu = b;
    }


    /**
     * Sets the crew's teleport anim state.
     * <p>
     * After cloning, this plays as the new body spawns.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setTeleportAnim(AnimState anim) {
        teleportAnim = anim;
    }


    /**
     * Unknown.
     * <p>
     * Related to walking/fighting?
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownPhi(boolean b) {
        unknownPhi = b;
    }

    /**
     * Sets time elapsed waiting for the lockdown ability to recharge.
     * <p>
     * In 1.5.13, this was observed at 50000 when the game started.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @param n a positive int less than, or equal to, the goal (0 when not charging)
     * @see #setLockdownRechargeTicksGoal(int)
     */
    public void setLockdownRechargeTicks(int n) {
        lockdownRechargeTicks = n;
    }


    /**
     * Sets the ticks needed to recharge the lockdown ability.
     * <p>
     * This is normally 50000 while charging, 0 otherwise.
     * <p>
     * In 1.5.13, this was observed at 50000 when the game started.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setLockdownRechargeTicks(int)
     */
    public void setLockdownRechargeTicksGoal(int n) {
        lockdownRechargeTicksGoal = n;
    }


    /**
     * Unknown.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownOmega(int n) {
        unknownOmega = n;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        CrewBlueprint crewBlueprint = DataManager.get().getCrew(race.getId());

        List<CrewBlueprint.SpriteTintLayer> tintLayerList = null;
        if (crewBlueprint != null) {
            tintLayerList = crewBlueprint.getSpriteTintLayerList();
        }

        result.append(String.format("Name:                   %s%n", name));
        result.append(String.format("Race:                   %s%n", race.getId()));
        result.append(String.format("Enemy Drone:            %5b%n", enemyBoardingDrone));
        result.append(String.format("Sex:                    %s%n", (male ? "Male" : "Female")));
        result.append(String.format("Health:                 %5d%n", health));
        result.append(String.format("Sprite Position:          %3d,%3d%n", spriteX, spriteY));
        result.append(String.format("Room Id:                %5d%n", roomId));
        result.append(String.format("Room Square:            %5d%n", roomSquare));
        result.append(String.format("Player Controlled:      %5b%n", playerControlled));
        result.append(String.format("Clone Ready?:           %5d%n", cloneReady));
        result.append(String.format("Mind Controlled:        %5b%n", mindControlled));

        result.append("\nSprite Tints...\n");
        for (int i = 0; i < spriteTintIndices.size(); i++) {
            Integer colorIndex = spriteTintIndices.get(i);

            String colorHint = null;
            if (tintLayerList != null && i < tintLayerList.size()) {
                CrewBlueprint.SpriteTintLayer tintLayer = tintLayerList.get(i);
                if (tintLayer.tintList != null && colorIndex < tintLayer.tintList.size() && colorIndex >= 0) {
                    CrewBlueprint.SpriteTintLayer.SpriteTintColor tintColor = tintLayer.tintList.get(colorIndex);
                    colorHint = String.format("r=%3d,g=%3d,b=%3d,a=%.1f", tintColor.r, tintColor.g, tintColor.b, tintColor.a);
                } else {
                    colorHint = "Color not in blueprint's layer.";
                }
            } else {
                colorHint = "Layer not in blueprint's colorList.";
            }

            result.append(String.format("  Layer %2d: Color: %3d (%s)%n", i, colorIndex, colorHint));
        }
        result.append("\n");

        FTLConstants origConstants = new OriginalFTLConstants();
        FTLConstants advConstants = new AdvancedFTLConstants();

        result.append(String.format("Saved Room Id:          %5d%n", savedRoomId));
        result.append(String.format("Saved Room Square:      %5d%n", savedRoomSquare));
        result.append(String.format("Pilot Skill:            %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", pilotSkill, origConstants.getMasteryIntervalPilot(race), advConstants.getMasteryIntervalPilot(race)));
        result.append(String.format("Engine Skill:           %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", engineSkill, origConstants.getMasteryIntervalEngine(race), advConstants.getMasteryIntervalEngine(race)));
        result.append(String.format("Shield Skill:           %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", shieldSkill, origConstants.getMasteryIntervalShield(race), advConstants.getMasteryIntervalShield(race)));
        result.append(String.format("Weapon Skill:           %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", weaponSkill, origConstants.getMasteryIntervalWeapon(race), advConstants.getMasteryIntervalWeapon(race)));
        result.append(String.format("Repair Skill:           %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", repairSkill, origConstants.getMasteryIntervalRepair(race), advConstants.getMasteryIntervalRepair(race)));
        result.append(String.format("Combat Skill:           %5d (Mastery Interval: %2d in FTL:AE, Originally %2d)%n", combatSkill, origConstants.getMasteryIntervalCombat(race), advConstants.getMasteryIntervalCombat(race)));
        result.append(String.format("Repairs:                %5d%n", repairs));
        result.append(String.format("Combat Kills:           %5d%n", combatKills));
        result.append(String.format("Piloted Evasions:       %5d%n", pilotedEvasions));
        result.append(String.format("Jumps Survived:         %5d%n", jumpsSurvived));
        result.append(String.format("Skill Masteries Earned: %5d%n", skillMasteriesEarned));
        result.append(String.format("Stun Ticks:            %6d (Decrements to 0)%n", stunTicks));
        result.append(String.format("Health Boost:          %6d (Subtracted from health when Mind Ctrl expires)%n", healthBoost));
        result.append(String.format("Clonebay Priority:     %6d (On death, copies Universal Death Count for a big number)%n", clonebayPriority));
        result.append(String.format("Damage Boost:          %6d (%5.03f)%n", damageBoost, damageBoost / 1000f));
        result.append(String.format("Dying Ticks?:          %6d%n", unknownLambda));
        result.append(String.format("Universal Death Count:  %5d (Shared across crew everywhere, for assigning next Clonebay priority)%n", universalDeathCount));
        result.append(String.format("Pilot Mastery (1,2):   %6b, %5b%n", pilotMasteryOne, pilotMasteryTwo));
        result.append(String.format("Engine Mastery (1,2):  %6b, %5b%n", engineMasteryOne, engineMasteryTwo));
        result.append(String.format("Shield Mastery (1,2):  %6b, %5b%n", shieldMasteryOne, shieldMasteryTwo));
        result.append(String.format("Weapon Mastery (1,2):  %6b, %5b%n", weaponMasteryOne, weaponMasteryTwo));
        result.append(String.format("Repair Mastery (1,2):  %6b, %5b%n", repairMasteryOne, repairMasteryTwo));
        result.append(String.format("Combat Mastery (1,2):  %6b, %5b%n", combatMasteryOne, combatMasteryTwo));
        result.append(String.format("Nu?:                   %6b%n", unknownNu));

        result.append("\nTeleport Anim...\n");
        if (teleportAnim != null) {
            result.append(teleportAnim.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("%n");

        result.append(String.format("Phi?:                  %6b%n", unknownPhi));
        result.append(String.format("Lockdown Ticks:        %6d (Crystal only, time elapsed recharging ability)%n", lockdownRechargeTicks));
        result.append(String.format("Lockdown Ticks Goal:   %6d (Crystal only, time needed to recharge)%n", lockdownRechargeTicksGoal));
        result.append(String.format("Omega?:                %6d (Crystal only)%n", unknownOmega));
        return result.toString();
    }
}
