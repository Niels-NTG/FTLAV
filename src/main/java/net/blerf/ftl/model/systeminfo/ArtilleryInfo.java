package net.blerf.ftl.model.systeminfo;

import net.blerf.ftl.model.state.WeaponModuleState;
import net.blerf.ftl.model.type.SystemType;

public class ArtilleryInfo extends ExtendedSystemInfo {
    private WeaponModuleState weaponMod = null;


    /**
     * Constructor.
     * <p>
     * It will need a WeaponModuleState.
     */
    public ArtilleryInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ArtilleryInfo(ArtilleryInfo srcInfo) {
        super(srcInfo);
        weaponMod = new WeaponModuleState(srcInfo.getWeaponModule());
    }

    @Override
    public ArtilleryInfo copy() {
        return new ArtilleryInfo(this);
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    @Override
    public void commandeer() {
        if (getWeaponModule() != null) {
            getWeaponModule().commandeer();
        }
    }

    public void setWeaponModule(WeaponModuleState weaponMod) {
        this.weaponMod = weaponMod;
    }

    public WeaponModuleState getWeaponModule() {
        return weaponMod;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("SystemId:                 %s%n", SystemType.ARTILLERY.getId()));

        result.append("\nWeapon Module...\n");
        if (weaponMod != null) {
            result.append(weaponMod.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
