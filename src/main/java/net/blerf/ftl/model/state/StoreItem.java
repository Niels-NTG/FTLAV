package net.blerf.ftl.model.state;

/**
 * An item in a store which either can be bought or has been bought already.
 */
public class StoreItem {
    private String itemId = null;
    private boolean available = false;
    private int extraData = 0;


    /**
     * Constructor.
     *
     * @param itemId a weapon/drone/augment blueprint or crew-race/system id
     */
    public StoreItem(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Copy constructor.
     */
    public StoreItem(StoreItem srcItem) {
        this(srcItem.getItemId());
        available = srcItem.isAvailable();
        extraData = srcItem.getExtraData();
    }

    public String getItemId() {
        return itemId;
    }

    /**
     * Sets whether this item has been sold already.
     */
    public void setAvailable(boolean b) {
        available = b;
    }

    public boolean isAvailable() {
        return available;
    }

    /**
     * Unknown.
     * <p>
     * Bonus drones (Repair/Defense 1) are not remembered, so it's not
     * that. Reloading at a store offering a bonus Repair always results in
     * a Defense 1.
     * <p>
     * Observed values: 1 (w/Drone_Ctrl+Repair), 2 (w/Cloaking),
     * 1 (w/Clonebay), 0 (on them all after reloading!?). Also seen:
     * 2 (w/Drone_Ctrl), 1 (w/Teleporter), 2 (w/Battery).
     * <p>
     * This was introduced in FTL 1.5.12.
     */
    public void setExtraData(int n) {
        extraData = n;
    }

    public int getExtraData() {
        return extraData;
    }

    @Override
    public String toString() {
        return String.format("Item: %s, Available: %5b, Extra?: %3d%n", itemId, available, extraData);
    }
}
