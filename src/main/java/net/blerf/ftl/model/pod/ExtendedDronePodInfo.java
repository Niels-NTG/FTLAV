package net.blerf.ftl.model.pod;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class ExtendedDronePodInfo {

    protected ExtendedDronePodInfo(ExtendedDronePodInfo srcInfo) {
    }

    /**
     * Blindly copy-constructs objects.
     * <p>
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedDronePodInfo copy();


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {
    }
}
