package net.blerf.ftl.model.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.WeaponBlueprint;

/**
 * Constructs an incomplete WeaponState.
 * <p>
 * It will need a weaponId.
 * <p>
 * For FTL 1.5.4+ saved games, a weapon module will be needed.
 */
@Getter
@Setter
@NoArgsConstructor
public class WeaponState {
    private String weaponId = null;
    private boolean armed = false;
    private int cooldownTicks = 0;
    /**
     * Sets additional weapon fields.
     * <p>
     * Advanced Edition added extra weapon fields at the end of saved game
     * files. They're nested inside this class for convenience.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    private WeaponModuleState weaponModule = null;

    /**
     * Copy constructor.
     * <p>
     * The weapon module will be copy-constructed as well.
     */
    public WeaponState(WeaponState srcWeapon) {
        weaponId = srcWeapon.getWeaponId();
        armed = srcWeapon.isArmed();
        cooldownTicks = srcWeapon.getCooldownTicks();

        if (srcWeapon.getWeaponModule() != null) {
            weaponModule = new WeaponModuleState(srcWeapon.getWeaponModule());
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        setArmed(false);
        setCooldownTicks(0);

        if (getWeaponModule() != null) {
            getWeaponModule().commandeer();
        }
    }

    public void setArmed(boolean b) {
        armed = b;
        if (!b) cooldownTicks = 0;
    }

    public boolean isArmed() {
        return armed;
    }

    /**
     * Sets time elapsed waiting for the weapon to cool down.
     * <p>
     * This increments from 0, by 1 each second. Its goal is the value of
     * the 'cooldown' tag in its WeaponBlueprint xml (0 when not armed).
     * <p>
     * Since FTL 1.5.4, this is no longer saved.
     *
     * @see WeaponModuleState#setCooldownTicks(int)
     */
    public void setCooldownTicks(int n) {
        cooldownTicks = n;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        WeaponBlueprint weaponBlueprint = DataManager.get().getWeapon(weaponId);
        String cooldownString = (weaponBlueprint != null ? weaponBlueprint.getCooldown() + "" : "?");

        result.append(String.format("WeaponId:       %s%n", weaponId));
        result.append(String.format("Armed:          %b%n", armed));
        result.append(String.format("Cooldown Ticks: %2d (max: %2s) (Not used as of FTL 1.5.4)%n", cooldownTicks, cooldownString));

        result.append("\nWeapon Module...\n");
        if (weaponModule != null) {
            result.append(weaponModule.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
