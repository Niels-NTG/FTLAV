package net.blerf.ftl.model.state;

import net.blerf.ftl.constants.HazardVulnerability;

public class EnvironmentState {
    private boolean redGiantPresent = false;
    private boolean pulsarPresent = false;
    private boolean pdsPresent = false;
    private HazardVulnerability vulnerableShips = HazardVulnerability.BOTH_SHIPS;
    private AsteroidFieldState asteroidField = null;
    private int solarFlareFadeTicks = 0;
    private int havocTicks = 0;
    private int pdsTicks = 0;  // Used by: PDS. Value lingers after leaving a beacon (sometimes varying by 1).


    public EnvironmentState() {
    }


    /**
     * Toggles the presence of a red giant hazard.
     * <p>
     * Red giant, pulsar, and PDS hazards can coexist.
     */
    public void setRedGiantPresent(boolean b) {
        redGiantPresent = b;
    }

    public boolean isRedGiantPresent() {
        return redGiantPresent;
    }

    /**
     * Toggles the presence of a pulsar hazard.
     * <p>
     * Red giant, pulsar, and PDS hazards can coexist.
     */
    public void setPulsarPresent(boolean b) {
        pulsarPresent = b;
    }

    public boolean isPulsarPresent() {
        return pulsarPresent;
    }

    /**
     * Toggles the presence of a PDS hazard.
     * <p>
     * Red giant, pulsar, and PDS hazards can coexist.
     */
    public void setPDSPresent(boolean b) {
        pdsPresent = b;
    }

    public boolean isPDSPresent() {
        return pdsPresent;
    }

    /**
     * Sets which ships will be targeted by a PDS.
     * <p>
     * Matthew's hint: Values are 0,1,2 for player ship, nearby ship, or
     * both. (0 and 1 are unconfirmed.)
     */
    public void setVulnerableShips(HazardVulnerability vuln) {
        vulnerableShips = vuln;
    }

    public HazardVulnerability getVulnerableShips() {
        return vulnerableShips;
    }

    public void setAsteroidField(AsteroidFieldState asteroidField) {
        this.asteroidField = asteroidField;
    }

    public AsteroidFieldState getAsteroidField() {
        return asteroidField;
    }


    /**
     * Sets elapsed time while the screen fades to/from white during a
     * solar flare from a red giant or pulsar.
     * <p>
     * TODO: Determine the number this counts to.
     */
    public void setSolarFlareFadeTicks(int n) {
        solarFlareFadeTicks = n;
    }

    public int getSolarFlareFadeTicks() {
        return solarFlareFadeTicks;
    }

    /**
     * Sets elapsed time while waiting for havoc from a red giant/pulsar/PDS.
     * <p>
     * For red giants, This counts to 30000, triggers a solar flare, and
     * returns to 0. A warning appears around 25000.
     * <p>
     * For pulsars, this hasn't been observed over 11000.
     * <p>
     * For PDS, this might count to 20000 before firing AT the ship (as
     * opposed to decorative misses)?
     * <p>
     * After leaving a beacon with such hazards, this value lingers (+/-1).
     */
    public void setHavocTicks(int n) {
        havocTicks = n;
    }

    public int getHavocTicks() {
        return havocTicks;
    }

    public void setPDSTicks(int n) {
        pdsTicks = n;
    }

    public int getPDSTicks() {
        return pdsTicks;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Red Giant Present: %5b%n", redGiantPresent));
        result.append(String.format("Pulsar Present:    %5b%n", pulsarPresent));
        result.append(String.format("PDS Present:       %5b%n", pdsPresent));
        result.append(String.format("Vulnerable Ships:  %s (PDS only)%n", vulnerableShips.toString()));

        result.append("\nAsteroid Field...\n");
        if (asteroidField != null)
            result.append(asteroidField.toString().replaceAll("(^|\n)(.+)", "$1  $2"));

        result.append("\n");

        result.append(String.format("Flare Fade Ticks?: %7d%n", solarFlareFadeTicks));
        result.append(String.format("Havoc Ticks?:      %7d (Red Giant/Pulsar/PDS only, Goal varies)%n", havocTicks));
        result.append(String.format("PDS Ticks?:        %7d (PDS only)%n", pdsTicks));

        return result.toString();
    }
}
