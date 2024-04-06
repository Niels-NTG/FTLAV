package net.blerf.ftl.model.projectileinfo;

public class LaserProjectileInfo extends ExtendedProjectileInfo {
    private int unknownAlpha = 0;
    private int spin = 0;

    // This class represents projectiles from both Laser and Burst weapons.

    /**
     * Constructor.
     */
    public LaserProjectileInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected LaserProjectileInfo(LaserProjectileInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = srcInfo.getUnknownAlpha();
        spin = srcInfo.getSpin();
    }

    @Override
    public LaserProjectileInfo copy() {
        return new LaserProjectileInfo(this);
    }

    /**
     * Unknown.
     * <p>
     * Observed values: For burst, it varies in the range 100000-3000000.
     * Some kind of seed? For regular lasers, it is 0.
     */
    public void setUnknownAlpha(int n) {
        unknownAlpha = n;
    }

    public int getUnknownAlpha() {
        return unknownAlpha;
    }

    /**
     * Unknown.
     * <p>
     * This is a pseudo-float based on the 'spin' tag of the
     * WeaponBlueprint's xml (burst-type weapons), if present, or 0.
     */
    public void setSpin(int n) {
        spin = n;
    }

    public int getSpin() {
        return spin;
    }

    @Override
    public String toString() {
        return "Type:               Laser/Burst Info\n" +
                String.format("Alpha?:             %7d%n", unknownAlpha) +
                String.format("Spin:               %7d%n", spin);
    }
}
