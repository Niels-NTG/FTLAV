package net.blerf.ftl.model.systeminfo;

import net.blerf.ftl.model.type.SystemType;

public class CloakingInfo extends ExtendedSystemInfo {
    private int unknownAlpha = 0;
    private int unknownBeta = 0;
    private int cloakTicksGoal = 0;
    private int cloakTicks = Integer.MIN_VALUE;


    public CloakingInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected CloakingInfo(CloakingInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = srcInfo.getUnknownAlpha();
        unknownBeta = srcInfo.getUnknownBeta();
        cloakTicksGoal = srcInfo.getCloakTicksGoal();
        cloakTicks = srcInfo.getCloakTicks();
    }

    @Override
    public CloakingInfo copy() {
        return new CloakingInfo(this);
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
        setUnknownAlpha(0);    // TODO: Vet this default.
        setUnknownBeta(0);     // TODO: Vet this default.
        setCloakTicksGoal(0);
        setCloakTicks(Integer.MIN_VALUE);
    }

    public void setUnknownAlpha(int n) {
        unknownAlpha = n;
    }

    public void setUnknownBeta(int n) {
        unknownBeta = n;
    }

    public int getUnknownAlpha() {
        return unknownAlpha;
    }

    public int getUnknownBeta() {
        return unknownBeta;
    }

    /**
     * Sets total time the cloak will stay engaged.
     * <p>
     * This can vary depending on the system level when the cloak is
     * initially engaged. When not engaged, this is 0.
     *
     * @see #setCloakTicks(int)
     */
    public void setCloakTicksGoal(int n) {
        cloakTicksGoal = n;
    }

    public int getCloakTicksGoal() {
        return cloakTicksGoal;
    }

    /**
     * Sets elapsed time while the cloak is engaged.
     * <p>
     * When this is not set, it is MIN_INT. After reaching or passing the
     * goal, this value lingers.
     *
     * @param n a positive int less than, or equal to, the goal (or MIN_INT)
     * @see #setCloakTicksGoal(int)
     */
    public void setCloakTicks(int n) {
        cloakTicks = n;
    }

    public int getCloakTicks() {
        return cloakTicks;
    }

    @Override
    public String toString() {
        return String.format("SystemId:                 %s%n", SystemType.CLOAKING.getId()) +
                String.format("Alpha?:                 %7d%n", unknownAlpha) +
                String.format("Beta?:                  %7d%n", unknownBeta) +
                String.format("Cloak Ticks Goal:       %7d%n", cloakTicksGoal) +
                String.format("Cloak Ticks:            %7s%n", (cloakTicks == Integer.MIN_VALUE ? "MIN" : cloakTicks));
    }
}
