package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DamageState {
    private int hullDamage = 0;
    private int shieldPiercing = 0;
    private int fireChance = 0;
    private int breachChance = 0;
    private int ionDamage = 0;
    private int systemDamage = 0;
    /**
     * Sets damage to apply to personnel.
     * <p>
     * This is dealt per-square to each crew in the room hit. A Beam weapon
     * can injure someone twice if it follows them into another room.
     */
    private int personnelDamage = 0;
    /**
     * Toggles whether this projectile deals double hull damage against
     * systemless rooms.
     * <p>
     * This is based on the 'hullBust' tag (0/1) of a WeaponBlueprint's xml.
     */
    private boolean hullBuster = false;
    /**
     * Unknown.
     * <p>
     * When not set, this is -1.
     * <p>
     * This only seems to be set by projectiles from bomb weapons: 1 when
     * from the nearby ship, once it materializes (-1 a moment before).
     */
    private int ownerId = -1;
    /**
     * Unknown.
     * <p>
     * When not set, this is -1.
     */
    private int selfId = -1;
    private boolean lockdown = false;
    private boolean crystalShard = false;
    private int stunChance = 0;
    private int stunAmount = 0;


    /**
     * Copy constructor.
     */
    public DamageState(DamageState srcDamage) {
        hullDamage = srcDamage.getHullDamage();
        shieldPiercing = srcDamage.getShieldPiercing();
        fireChance = srcDamage.getFireChance();
        breachChance = srcDamage.getBreachChance();
        ionDamage = srcDamage.getIonDamage();
        systemDamage = srcDamage.getSystemDamage();
        personnelDamage = srcDamage.getPersonnelDamage();
        boolean hullBuster = srcDamage.isHullBuster();
        ownerId = srcDamage.getOwnerId();
        selfId = srcDamage.getSelfId();
        lockdown = srcDamage.isLockdown();
        crystalShard = srcDamage.isCrystalShard();
        stunChance = srcDamage.getStunChance();
        stunAmount = srcDamage.getStunAmount();
    }

    @Override
    public String toString() {
        return String.format("Hull Damage:       %7d%n", hullDamage) +
                String.format("ShieldPiercing:    %7d%n", shieldPiercing) +
                String.format("Fire Chance:       %7d%n", fireChance) +
                String.format("Breach Chance:     %7d%n", breachChance) +
                String.format("Ion Damage:        %7d%n", ionDamage) +
                String.format("System Damage:     %7d%n", systemDamage) +
                String.format("Personnel Damage:  %7d%n", personnelDamage) +
                String.format("Hull Buster:       %7b (2x Hull damage vs systemless rooms)%n", hullBuster) +
                String.format("Owner Id?:         %7d%n", ownerId) +
                String.format("Self Id?:          %7d%n", selfId) +
                String.format("Lockdown:          %7b%n", lockdown) +
                String.format("Crystal Shard:     %7b%n", crystalShard) +
                String.format("Stun Chance:       %7d%n", stunChance) +
                String.format("Stun Amount:       %7d%n", stunAmount);
    }
}
