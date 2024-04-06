package net.blerf.ftl.model.sectortree;


public class SectorDot {
    private final String sectorType;
    private final String sectorId;
    private final String sectorTitle;
    private boolean visited = false;


    public SectorDot(String sectorType, String sectorId, String sectorTitle) {
        this.sectorType = sectorType;
        this.sectorId = sectorId;
        this.sectorTitle = sectorTitle;
    }

    /**
     * Copy constructor.
     */
    public SectorDot(SectorDot srcDot) {
        sectorType = srcDot.getType();
        sectorId = srcDot.getSectorId();
        sectorTitle = srcDot.getTitle();
        visited = srcDot.isVisited();
    }

    /**
     * Marks whether the player has been to this location.
     *
     * @see net.blerf.ftl.model.SectorTree#setSectorVisitation(List)
     * @see net.blerf.ftl.parser.SavedGameParser.SavedGameState#setSectorVisitation(List)
     */
    public void setVisited(boolean b) {
        visited = b;
    }

    public boolean isVisited() {
        return visited;
    }

    public String getType() {
        return sectorType;
    }

    public String getSectorId() {
        return sectorId;
    }

    public String getTitle() {
        return sectorTitle;
    }


    /**
     * Sort of tests for equality. (Visitation is ignored.)
     * <p>
     * Overriding equals()/hashCode() would mess with collections.
     */
    public boolean isSimilarTo(SectorDot otherDot) {
        if (sectorType != null) {
            if (!sectorType.equals(otherDot.getType())) return false;
        } else if (otherDot.getType() != null) {
            return false;
        }

        if (sectorId != null) {
            if (!sectorId.equals(otherDot.getSectorId())) return false;
        } else if (otherDot.getSectorId() != null) {
            return false;
        }

        if (sectorTitle != null) {
            return sectorTitle.equals(otherDot.getTitle());
        } else return otherDot.getTitle() == null;
    }
}
