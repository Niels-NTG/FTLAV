package net.blerf.ftl.model.projectileinfo;

/**
 * Extended info for Beam projectiles.
 * <p>
 * Beam projectiles have several parts.
 * An emission line, drawn from the weapon.
 * A strafe line, drawn toward the target ship.
 * A spot, where the strafe line hits the target ship.
 * A swath, the path the spot tries to travel along.
 * <p>
 * For ship weapons, the emission line ends off-screen, and the strafe line
 * begins somewhere off-screen. For Beam drones, the emission line is
 * ignored, and the strafe line is drawn from the drone pod directly to the
 * swath.
 * <p>
 * The ProjectileState's current/previous position is the emission line's
 * source - at the weapon or drone pod that fired. The ProjectileState's
 * goal position is where the spot is, along the swath (shield blocking is
 * not considered).
 */
public class BeamProjectileInfo extends ExtendedProjectileInfo {
    private int emissionEndX = 0;
    private int emissionEndY = 0;
    private int strafeSourceX = 0;
    private int strafeSourceY = 0;
    private int strafeEndX = 0;
    private int strafeEndY = 0;
    private int unknownBetaX = 0;
    private int unknownBetaY = 0;
    private int swathEndX = 0;
    private int swathEndY = 0;
    private int swathStartX = 0;
    private int swathStartY = 0;
    private int unknownGamma = 0;
    private int swathLength = 0;
    private int unknownDelta = 0;
    private int unknownEpsilonX = 0;
    private int unknownEpsilonY = 0;
    private int unknownZeta = 0;
    private int unknownEta = 0;
    private int emissionAngle = 0;
    private boolean unknownIota = false;
    private boolean unknownKappa = false;
    private boolean fromDronePod = false;
    private boolean unknownMu = false;
    private boolean unknownNu = false;


    /**
     * Constructor.
     */
    public BeamProjectileInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected BeamProjectileInfo(BeamProjectileInfo srcInfo) {
        super(srcInfo);
        emissionEndX = srcInfo.getEmissionEndX();
        emissionEndY = srcInfo.getEmissionEndY();
        strafeSourceX = srcInfo.getStrafeSourceX();
        strafeSourceY = srcInfo.getStrafeSourceY();
        strafeEndX = srcInfo.getStrafeEndX();
        strafeEndY = srcInfo.getStrafeEndY();
        unknownBetaX = srcInfo.getUnknownBetaX();
        unknownBetaY = srcInfo.getUnknownBetaY();
        swathEndX = srcInfo.getSwathEndX();
        swathEndY = srcInfo.getSwathEndY();
        swathStartX = srcInfo.getSwathStartX();
        swathStartY = srcInfo.getSwathStartY();
        unknownGamma = srcInfo.getUnknownGamma();
        swathLength = srcInfo.getSwathLength();
        unknownDelta = srcInfo.getUnknownDelta();
        unknownEpsilonX = srcInfo.getUnknownEpsilonX();
        unknownEpsilonY = srcInfo.getUnknownEpsilonY();
        unknownZeta = srcInfo.getUnknownZeta();
        unknownEta = srcInfo.getUnknownEta();
        emissionAngle = srcInfo.getEmissionAngle();
        unknownIota = srcInfo.getUnknownIota();
        unknownKappa = srcInfo.getUnknownKappa();
        fromDronePod = srcInfo.isFromDronePod();
        unknownMu = srcInfo.getUnknownMu();
        unknownNu = srcInfo.getUnknownNu();
    }

    @Override
    public BeamProjectileInfo copy() {
        return new BeamProjectileInfo(this);
    }

    /**
     * Sets the off-screen endpoint of the line drawn from the weapon.
     * <p>
     * For Beam drones, this point will be the same as strafeSource, except
     * this y will be shifted upward by -2000. The emission line won't be
     * drawn in that case, obviously, since the drone is right there.
     * <p>
     * This is relative to the ship space the beam was emitted from
     * (e.g., weapon of a player ship, or a drone hovering over a nearby
     * ship).
     */
    public void setEmissionEndX(int n) {
        emissionEndX = n;
    }

    public void setEmissionEndY(int n) {
        emissionEndY = n;
    }

    public int getEmissionEndX() {
        return emissionEndX;
    }

    public int getEmissionEndY() {
        return emissionEndY;
    }

    /**
     * Sets the off-screen endpoint of the line drawn toward the swath.
     * <p>
     * This is relative to the target ship.
     */
    public void setStrafeSourceX(int n) {
        strafeSourceX = n;
    }

    public void setStrafeSourceY(int n) {
        strafeSourceY = n;
    }

    public int getStrafeSourceX() {
        return strafeSourceX;
    }

    public int getStrafeSourceY() {
        return strafeSourceY;
    }

    /**
     * Sets the on-screen endpoint of the line drawn toward the swath.
     * <p>
     * When shields are up, this point is not on the swath but at the
     * intersection of the line and shield oval.
     * <p>
     * This is relative to the target ship.
     */
    public void setStrafeEndX(int n) {
        strafeEndX = n;
    }

    public void setStrafeEndY(int n) {
        strafeEndY = n;
    }

    public int getStrafeEndX() {
        return strafeEndX;
    }

    public int getStrafeEndY() {
        return strafeEndY;
    }

    /**
     * Unknown.
     * <p>
     * Observed values: The current location of the travelling spot.
     * <p>
     * This is relative to the target ship.
     */
    public void setUnknownBetaX(int n) {
        unknownBetaX = n;
    }

    public void setUnknownBetaY(int n) {
        unknownBetaY = n;
    }

    public int getUnknownBetaX() {
        return unknownBetaX;
    }

    public int getUnknownBetaY() {
        return unknownBetaY;
    }

    /**
     * Sets the point the travelling spot will end at.
     * <p>
     * This is relative to the target ship.
     */
    public void setSwathEndX(int n) {
        swathEndX = n;
    }

    public void setSwathEndY(int n) {
        swathEndY = n;
    }

    public int getSwathEndX() {
        return swathEndX;
    }

    public int getSwathEndY() {
        return swathEndY;
    }

    /**
     * Sets the point the travelling spot will start from.
     * <p>
     * This is relative to the target ship.
     */
    public void setSwathStartX(int n) {
        swathStartX = n;
    }

    public void setSwathStartY(int n) {
        swathStartY = n;
    }

    public int getSwathStartX() {
        return swathStartX;
    }

    public int getSwathStartY() {
        return swathStartY;
    }

    /**
     * Unknown.
     * <p>
     * This is always 1000.
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
     * This is a pseudo-float based on the 'length' tag of the
     * WeaponBlueprint's xml.
     */
    public void setSwathLength(int n) {
        swathLength = n;
    }

    public int getSwathLength() {
        return swathLength;
    }

    /**
     * Unknown.
     * <p>
     * This is a constant, at least for a given WeaponBlueprint.
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
     * Observed values: The current location of the travelling spot.
     * <p>
     * This is relative to the target ship.
     */
    public void setUnknownEpsilonX(int n) {
        unknownEpsilonX = n;
    }

    public void setUnknownEpsilonY(int n) {
        unknownEpsilonY = n;
    }

    public int getUnknownEpsilonX() {
        return unknownEpsilonX;
    }

    public int getUnknownEpsilonY() {
        return unknownEpsilonY;
    }

    /**
     * Unknown.
     * <p>
     * This is an erratic int (seen 0-350) with no clear progression from
     * moment to moment.
     */
    public void setUnknownZeta(int n) {
        unknownZeta = n;
    }

    public int getUnknownZeta() {
        return unknownZeta;
    }

    /**
     * Unknown.
     * <p>
     * Possibly damage per room, based on the 'damage' tag of the
     * WeaponBlueprint's xml? (That's DamageState's hullDamage)
     * <p>
     * Observed values: 1, 2.
     */
    public void setUnknownEta(int n) {
        unknownEta = n;
    }

    public int getUnknownEta() {
        return unknownEta;
    }

    /**
     * Sets the angle of the line drawn from the weapon.
     * <p>
     * For ships, this will be 0 (player ship) or 270000 (nearby ship).
     * <p>
     * For Beam drones, this is related to the turret angle, though this
     * may be a large negative angle while the turret may be a small
     * positive one.
     * <p>
     * Observed values: 0, 270000, -323106.
     *
     * @param n a pseudo-float (n degrees clockwise from east)
     */
    public void setEmissionAngle(int n) {
        emissionAngle = n;
    }

    public int getEmissionAngle() {
        return emissionAngle;
    }

    /**
     * Unknown.
     */
    public void setUnknownIota(boolean b) {
        unknownIota = b;
    }

    public boolean getUnknownIota() {
        return unknownIota;
    }

    /**
     * Unknown.
     * <p>
     * Seems to be true only when the target ship's shields are down, and
     * the line will reach the swath without being blocked (even set while
     * pending).
     */
    public void setUnknownKappa(boolean b) {
        unknownKappa = b;
    }

    public boolean getUnknownKappa() {
        return unknownKappa;
    }

    /**
     * Sets whether this this beam was fired from a drone pod or a ship
     * weapon.
     * <p>
     * For ship weapons, this is false, and both the emission and strafe
     * lines will be drawn.
     * <p>
     * If true, only the strafe line be drawn - from the ProjectileState's
     * current position (the drone's aperture).
     * <p>
     * If edited to false on a drone, the emission line will be drawn,
     * northward, with no strafe line - completely missing the target ship.
     * This weirdness may have to to with current/destination space not
     * being separate, as they would be for a ship weapon?
     */
    public void setFromDronePod(boolean b) {
        fromDronePod = b;
    }

    public boolean isFromDronePod() {
        return fromDronePod;
    }

    /**
     * Unknown.
     */
    public void setUnknownMu(boolean b) {
        unknownMu = b;
    }

    public boolean getUnknownMu() {
        return unknownMu;
    }

    /**
     * Unknown.
     * <p>
     * Might have to do with the line having hit crew?
     */
    public void setUnknownNu(boolean b) {
        unknownNu = b;
    }

    public boolean getUnknownNu() {
        return unknownNu;
    }


    @Override
    public String toString() {
        return "Type:               Beam Info%n" +
                String.format("Emission End:       %8d,%8d (%9.03f,%9.03f) (Off-screen endpoint of line from weapon)%n", emissionEndX, emissionEndY, emissionEndX / 1000f, emissionEndY / 1000f) +
                String.format("Strafe Source:      %8d,%8d (%9.03f,%9.03f) (Off-screen endpoint of line drawn toward swath)%n", strafeSourceX, strafeSourceY, strafeSourceX / 1000f, strafeSourceY / 1000f) +
                String.format("Strafe End:         %8d,%8d (%9.03f,%9.03f) (On-screen endpoint of line drawn toward swath)%n", strafeEndX, strafeEndY, strafeEndX / 1000f, strafeEndY / 1000f) +
                String.format("Beta?:              %8d,%8d (%9.03f,%9.03f)%n", unknownBetaX, unknownBetaY, unknownBetaX / 1000f, unknownBetaY / 1000f) +
                String.format("Swath End:          %8d,%8d (%9.03f,%9.03f)%n", swathEndX, swathEndY, swathEndX / 1000f, swathEndY / 1000f) +
                String.format("Swath Start:        %8d,%8d (%9.03f,%9.03f)%n", swathStartX, swathStartY, swathStartX / 1000f, swathStartY / 1000f) +
                String.format("Gamma?:             %8d%n", unknownGamma) +
                String.format("Swath Length:       %8d (%9.03f)%n", swathLength, swathLength / 1000f) +
                String.format("Delta?:             %8d%n", unknownDelta) +
                String.format("Epsilon?:           %8d,%8d (%9.03f,%9.03f)%n", unknownEpsilonX, unknownEpsilonY, unknownEpsilonX / 1000f, unknownEpsilonY / 1000f) +
                String.format("Zeta?:              %8d%n", unknownZeta) +
                String.format("Eta?:               %8d%n", unknownEta) +
                String.format("Emission Angle:     %8d%n", emissionAngle) +
                String.format("Iota?:              %8b%n", unknownIota) +
                String.format("Kappa?:             %8b%n", unknownKappa) +
                String.format("From Drone Pod:     %8b%n", fromDronePod) +
                String.format("Mu?:                %8b%n", unknownMu) +
                String.format("Nu?:                %8b%n", unknownNu);
    }
}
