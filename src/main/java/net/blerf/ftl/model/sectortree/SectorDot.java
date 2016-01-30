package net.blerf.ftl.model.sectortree;


public class SectorDot {
	private String sectorType;
	private String sectorId;
	private String sectorTitle;
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

	public void setVisited(boolean b) { visited = b; }
	public boolean isVisited() { return visited; }

	public String getType() { return sectorType; }
	public String getSectorId() { return sectorId; }
	public String getTitle() { return sectorTitle; }


	/**
	 * Sort of tests for equality. (Visitation is ignored.)
	 *
	 * Overriding equals()/hashCode() would mess with collections.
	 */
	public boolean isSimilarTo(SectorDot otherDot) {
		if (sectorType != null) {
			if (!sectorType.equals(otherDot.getType())) return false;
		}
		else if (otherDot.getType() != null) {
			return false;
		}

		if (sectorId != null) {
			if (!sectorId.equals(otherDot.getSectorId())) return false;
		}
		else if (otherDot.getSectorId() != null) {
			return false;
		}

		if (sectorTitle != null) {
			if (!sectorTitle.equals(otherDot.getTitle())) return false;
		}
		else if (otherDot.getTitle() != null) {
			return false;
		}

		return true;
	}
}
