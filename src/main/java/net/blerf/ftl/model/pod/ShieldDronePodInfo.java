package net.blerf.ftl.model.pod;

public class ShieldDronePodInfo extends ExtendedDronePodInfo {
    private int unknownAlpha = -1000;

    /**
     * Constructor.
     */
    public ShieldDronePodInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ShieldDronePodInfo(ShieldDronePodInfo srcInfo) {
        super(srcInfo);
        unknownAlpha = srcInfo.getUnknownAlpha();
    }

    @Override
    public ShieldDronePodInfo copy() {
        return new ShieldDronePodInfo(this);
    }

    @Override
    public void commandeer() {
        setUnknownAlpha(-1000);
    }


    /**
     * Unknown.
     * <p>
     * Zoltan shield recharge ticks?
     * <p>
     * Observed values: -1000 (inactive)
     */
    public void setUnknownAlpha(int n) {
        unknownAlpha = n;
    }

    public int getUnknownAlpha() {
        return unknownAlpha;
    }

    @Override
    public String toString() {
        return String.format("Alpha?:             %7d%n", unknownAlpha);
    }
}
