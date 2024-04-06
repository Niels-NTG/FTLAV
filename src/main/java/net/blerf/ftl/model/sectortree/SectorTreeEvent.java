package net.blerf.ftl.model.sectortree;

import java.util.EventObject;


public class SectorTreeEvent extends EventObject {

    public static final int COLUMNS_CHANGED = 0;
    public static final int VISITATION_CHANGED = 1;

    protected final int type;


    public SectorTreeEvent(Object source, int type) {
        super(source);
        this.type = type;
    }

    /**
     * Copy constructor.
     */
    public SectorTreeEvent(SectorTreeEvent srcEvent) {
        super(srcEvent.getSource());
        this.type = srcEvent.getType();
    }


    public int getType() {
        return type;
    }
}
