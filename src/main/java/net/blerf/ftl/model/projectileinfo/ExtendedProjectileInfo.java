package net.blerf.ftl.model.projectileinfo;

public abstract class ExtendedProjectileInfo {

    protected ExtendedProjectileInfo() {
    }

    protected ExtendedProjectileInfo(ExtendedProjectileInfo srcInfo) {
    }

    /**
     * Blindly copy-constructs objects.
     * <p>
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedProjectileInfo copy();
}
