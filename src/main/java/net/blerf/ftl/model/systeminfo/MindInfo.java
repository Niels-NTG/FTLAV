package net.blerf.ftl.model.systeminfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.type.SystemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MindInfo extends ExtendedSystemInfo {
    /**
     * Total time crew will stay mind controlled.
     * <p>
     * This can vary depending on the system level when mind control is
     * initially engaged. When not engaged, this value lingers.
     */
    private int mindControlTicksGoal;
    /**
     * Sets elapsed time while crew are mind controlled.
     * A positive int less than, or equal to, the goal.
     * <p>
     * When the goal is reached, the Mind system will get 4 ionized bars
     * (ionized bars had been -1 while disrupting).
     * After reaching or passing the goal, this value lingers.
     */
    private int mindControlTicks;

    @Override
    public MindInfo copy() {
        return toBuilder().build();
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     */
    @Override
    public void commandeer() {
        setMindControlTicks(0);
        setMindControlTicksGoal(0);
    }

    @Override
    public String toString() {
        return String.format("SystemId:                 %s%n", SystemType.MIND.getId()) +
                String.format("Mind Ctrl Ticks:        %7d%n", mindControlTicks) +
                String.format("Mind Ctrl Ticks Goal:   %7d%n", mindControlTicksGoal);
    }
}
