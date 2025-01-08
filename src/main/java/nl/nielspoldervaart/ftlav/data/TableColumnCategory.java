package nl.nielspoldervaart.ftlav.data;

public enum TableColumnCategory {
	GAME("Game"),
	SECTOR("Sector"),
	BEACON("Beacon"),
	RESOURCE_TOTALS("Resource Totals"),
	RESOURCES("Resources"),
	SYSTEMS("Player Ship Systems"),
	NEARBY_SHIP("Nearby Ship"),
	MISC("Misc");

	public final String label;
	TableColumnCategory(String label) {
		this.label = label;
	}
}
