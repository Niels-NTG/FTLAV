package nl.nielspoldervaart.ftlav.data;

public enum TableColumnCategory {
	GAME("Game"),
	SECTOR("Sector"),
	ENCOUNTER("Encounter"),
	RESOURCE_TOTALS("Resource Totals"),
	RESOURCES("Resources"),
	SYSTEMS("Systems"),
	NEARBY_SHIP("Nearby Ship"),
	MISC("Misc");

	public final String label;
	TableColumnCategory(String label) {
		this.label = label;
	}
}
