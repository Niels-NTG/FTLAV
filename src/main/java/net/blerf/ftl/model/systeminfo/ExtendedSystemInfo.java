package net.blerf.ftl.model.systeminfo;

public abstract class ExtendedSystemInfo {

    protected ExtendedSystemInfo() {
    }

    protected ExtendedSystemInfo(ExtendedSystemInfo srcInfo) {
    }

    /**
     * Blindly copy-constructs objects.
     * <p>
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedSystemInfo copy();

    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This will be called by the ship object when it is commandeered.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     */
    public abstract void commandeer();
}
