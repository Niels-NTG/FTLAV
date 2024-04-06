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
public class ShieldsInfo extends ExtendedSystemInfo {
    /**
     * Sets the current number of normal shield layers.
     * <p>
     * This is indicated in-game by filled bubbles.
     */
    private int shieldLayers;
    /**
     * Sets the current number of energy shield layers.
     * <p>
     * This is indicated in-game by green rectangles.
     */
    private int energyShieldLayers;
    /**
     * Sets the number of energy shield layers when fully charged.
     * <p>
     * This is 0 until set by a mechanism that adds energy layers. This
     * value lingers after a temporary energy shield is exhausted.
     */
    private int energyShieldMax;
    /**
     * Sets elapsed time while waiting for the next normal shield layer
     * to recharge.
     * <p>
     * This counts to 2000. When not recharging, it is 0.
     */
    private int shieldRechargeTicks;
    /**
     * Toggles whether the regular shield drop animation is being played.
     * <p>
     * Note: The drop and raise anims can both play simultaneously.
     */
    private boolean shieldDropAnimOn;
    /**
     * Sets elapsed time while playing the regular shield drop anim 0-1000
     */
    private int shieldDropAnimTicks;
    /**
     * Toggles whether the regular shield raise animation is being played.
     * <p>
     * Note: The drop and raise anims can both play simultaneously.
     */
    private boolean shieldRaiseAnimOn;
    /**
     * Sets elapsed time while playing the regular shield raise anim 0-1000
     */
    private int shieldRaiseAnimTicks;
    /**
     * Toggles whether the energy shield animation is being played.
     */
    private boolean energyShieldAnimOn;
    /**
     * Sets elapsed time while playing the energy shield anim 0-1000
     */
    private int energyShieldAnimTicks;

    private int unknownLambda;
    private int unknownMu;

    @Override
    public ShieldsInfo copy() {
        return toBuilder().build();
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
        setShieldLayers(0);
        setShieldRechargeTicks(0);
        setShieldDropAnimOn(false);
        setShieldDropAnimTicks(0);   // TODO: Vet this default.
        setShieldRaiseAnimOn(false);
        setShieldRaiseAnimTicks(0);  // TODO: Vet this default.
    }

    @Override
    public String toString() {
        return String.format("SystemId:                 %s%n", SystemType.SHIELDS.getId()) +
                String.format("Shield Layers:            %5d (Currently filled bubbles)%n", shieldLayers) +
                String.format("Energy Shield Layers:     %5d%n", energyShieldLayers) +
                String.format("Energy Shield Max:        %5d (Layers when fully charged)%n", energyShieldLayers) +
                String.format("Shield Recharge Ticks:    %5d%n", shieldRechargeTicks) +
                "\n" +
                String.format("Shield Drop Anim:   Play: %-5b, Ticks: %4d%n", shieldDropAnimOn, shieldDropAnimTicks) +
                String.format("Shield Raise Anim:  Play: %-5b, Ticks: %4d%n", shieldRaiseAnimOn, shieldRaiseAnimTicks) +
                String.format("Energy Shield Anim: Play: %-5b, Ticks: %4d%n", energyShieldAnimOn, energyShieldAnimTicks) +
                String.format("Lambda?, Mu?:           %7d,%7d (Some kind of coord?)%n", unknownLambda, unknownMu);
    }

}
