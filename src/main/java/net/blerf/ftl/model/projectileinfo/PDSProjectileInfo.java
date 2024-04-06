package net.blerf.ftl.model.projectileinfo;

import net.blerf.ftl.model.state.AnimState;

/**
 * Extended info for PDS projectiles (called ASB in-game).
 * <p>
 * This was introduced in FTL 1.6.1.
 */
public class PDSProjectileInfo extends ExtendedProjectileInfo {
    private int unknownAlpha = 0;
    private int unknownBeta = 0;
    private int unknownGamma = 0;
    private int unknownDelta = 0;
    private int unknownEpsilon = 0;
    private AnimState unknownZeta = new AnimState();

    // This class represents projectiles from PDS hazards.

    /**
     * Constructor.
     */
    public PDSProjectileInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected PDSProjectileInfo(PDSProjectileInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = srcInfo.getUnknownAlpha();
        unknownBeta = srcInfo.getUnknownBeta();
        unknownGamma = srcInfo.getUnknownGamma();
        unknownDelta = srcInfo.getUnknownDelta();
        unknownEpsilon = srcInfo.getUnknownEpsilon();
        unknownZeta = srcInfo.getUnknownZeta();
    }

    @Override
    public PDSProjectileInfo copy() {
        return new PDSProjectileInfo(this);
    }

    /**
     * Unknown.
     * <p>
     * Seems to be the spawn X position relative to ship space?
     * <p>
     * This is a pseudo-float.
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
     * Seems to be the spawn Y position relative to ship space?
     * <p>
     * This is a pseudo-float.
     */
    public void setUnknownBeta(int n) {
        unknownBeta = n;
    }

    public int getUnknownBeta() {
        return unknownBeta;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 1, 0.
     */
    public void setUnknownGamma(int n) {
        unknownGamma = n;
    }

    public int getUnknownGamma() {
        return unknownGamma;
    }

    /**
     * Unknown.
     * <p>
     * Always matches the projectile's flightAnim scale.
     * <p>
     * Observed values: 10277; 11438, 15690, 19896, 26832, 34719; 1139.
     */
    public void setUnknownDelta(int n) {
        unknownDelta = n;
    }

    public int getUnknownDelta() {
        return unknownDelta;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: 0.
     */
    public void setUnknownEpsilon(int n) {
        unknownEpsilon = n;
    }

    public int getUnknownEpsilon() {
        return unknownEpsilon;
    }

    /**
     * Unknown.
     * <p>
     * Observed values:
     * Looping: 0.
     * Frame: 0, 2, 4, 5, 8.
     * Progress Ticks: 0, 245, 467, 648, 892.
     * Scale: 1.0
     * Position: (0, 0), (213000, 213000).
     */
    public void setUnknownZeta(AnimState anim) {
        unknownZeta = anim;
    }

    public AnimState getUnknownZeta() {
        return unknownZeta;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Type:               PDS Info%n"));
        result.append(String.format("Alpha?:             %7d%n", unknownAlpha));
        result.append(String.format("Beta?:              %7d%n", unknownBeta));
        result.append(String.format("Gamma?:             %7d%n", unknownGamma));
        result.append(String.format("Delta?:             %7d%n", unknownDelta));
        result.append(String.format("Epsilon?:           %7d%n", unknownEpsilon));

        result.append("\nZeta? Anim...\n");
        if (unknownZeta != null) {
            result.append(unknownZeta.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
