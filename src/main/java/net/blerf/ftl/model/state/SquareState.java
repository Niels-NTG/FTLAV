package net.blerf.ftl.model.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SquareState {
    /**
     * Health of a fire in this square 0-100.
     */
    private int fireHealth = 0;
    /**
     * Square's ignition progress 0-100.
     * <p>
     * Squares adjacent to a fire grow closer to igniting as
     * time passes. Then a new fire spawns in them at full health.
     */
    private int ignitionProgress = 0;
    /**
     * Unknown.
     * <p>
     * This is a rapidly decrementing number, as a fire disappears in a puff
     * of smoke. When not set, this is -1.
     * <p>
     * Starving a fire of oxygen does not affect its health.
     * <p>
     * In FTL 1.01-1.5.10 this always seemed to be -1. In FTL 1.5.13, other
     * values were finally observed.
     * <p>
     * Observed values: -1 (almost always), 9,8,7,6,5,2,1,0.
     */
    private int extinguishmentProgress = -1;

    @Override
    public String toString() {
        return String.format("Fire HP: %3d, Ignition: %3d%%, Extinguishment: %2d%n", fireHealth, ignitionProgress, extinguishmentProgress);
    }
}
