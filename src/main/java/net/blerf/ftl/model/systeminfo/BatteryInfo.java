package net.blerf.ftl.model.systeminfo;

import net.blerf.ftl.model.type.SystemType;

public class BatteryInfo extends ExtendedSystemInfo {
    private boolean active = false;
    private int usedBattery = 0;
    private int dischargeTicks = 1000;

    // Plasma storms only halve *reserve* power.
    // The Battery system is unaffected by plasma storms (<environment type="storm"/>).


    public BatteryInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected BatteryInfo(BatteryInfo srcInfo) {
        super(srcInfo);
        active = srcInfo.isActive();
        usedBattery = srcInfo.getUsedBattery();
        dischargeTicks = srcInfo.getDischargeTicks();
    }

    @Override
    public BatteryInfo copy() {
        return new BatteryInfo(this);
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     */
    @Override
    public void commandeer() {
        setActive(false);
        setUsedBattery(0);
        setDischargeTicks(1000);
    }

    /**
     * Toggles whether the battery is turned on.
     *
     * @see #setDischargeTicks(int)
     */
    public void setActive(boolean b) {
        active = b;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Sets the total battery power currently assigned to systems.
     * <p>
     * This is subtracted from a pool based on the battery system's level
     * to calculate remaining battery power.
     */
    public void setUsedBattery(int n) {
        usedBattery = n;
    }

    public int getUsedBattery() {
        return usedBattery;
    }

    /**
     * Sets elapsed time while the battery is active.
     * <p>
     * This counts to 1000. When not discharging, it's 1000.
     * After it's fully discharged, the battery system will be locked for
     * a bit.
     *
     * @param n 0-1000
     * @see #setDischargeTicks(int)
     */
    public void setDischargeTicks(int n) {
        dischargeTicks = n;
    }

    public int getDischargeTicks() {
        return dischargeTicks;
    }

    @Override
    public String toString() {
        return String.format("SystemId:                 %s%n", SystemType.BATTERY.getId()) +
                String.format("Active:                   %5b%n", active) +
                String.format("Battery Power in Use:     %5d%n", usedBattery) +
                String.format("Discharge Ticks:          %5d%n", dischargeTicks);
    }
}
