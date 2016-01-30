package net.blerf.ftl.model;

import net.blerf.ftl.constants.Difficulty;


/**
 * A summary of a single past campaign.
 */
public class Score implements Comparable<Score> {

	private String shipName = "";
	private String shipId = "";
	private int value = 0;
	private int sector = 1;
	private Difficulty difficulty = Difficulty.EASY;
	private boolean victory = false;
	private boolean dlcEnabled = false;


	/**
	 * Constructs an incomplete Score.
	 *
	 * All attributes will need to be set afterward.
	 */
	public Score() {}

	/**
	 * Constructs a Score.
	 *
	 * @param shipName an arbitrary label from the player
	 * @param shipId the id of the ShipBlueprint used
	 * @param value the number of points earned in that campaign
	 * @param sector the 1-based sector the player was in when that campaign ended
	 * @param difficulty the difficulty of that campaign
	 *
	 * @see #setDLCEnabled(boolean)
	 */
	public Score(String shipName, String shipId, int value, int sector, Difficulty difficulty, boolean victory) {
		this.shipName = shipName;
		this.shipId = shipId;
		this.value = value;
		this.sector = sector;
		this.difficulty = difficulty;
		this.victory = victory;
	}

	/**
	 * Copy constructor.
	 */
	public Score(Score srcScore) {
		this();
		this.shipName = srcScore.getShipName();
		this.shipId = srcScore.getShipId();
		this.value = srcScore.getValue();
		this.sector = srcScore.getSector();
		this.difficulty = srcScore.getDifficulty();
		this.victory = srcScore.isVictory();
		this.dlcEnabled = srcScore.isDLCEnabled();
	}

	public void setShipName(String s) { shipName = s; }
	public void setShipId(String s) { shipId = s; }
	public void setValue(int n) { value = n; }

	public String getShipName() { return shipName; }
	public String getShipId() { return shipId; }
	public int getValue() { return value; }

	/**
	 * Sets the 1-based sector the player was in when that campaign ended.
	 */
	public void setSector(int n) { sector = n; }
	public int getSector() { return sector; }

	public void setDifficulty(Difficulty d) { difficulty = d; }
	public void setVictory(boolean b) { victory = b; }

	public Difficulty getDifficulty() { return difficulty; }
	public boolean isVictory() { return victory; }

	/**
	 * Sets whether AE Content was enabled in that campaign.
	 *
	 * This was introduced in FTL 1.5.4.
	 */
	public void setDLCEnabled(boolean b) { dlcEnabled = b; }
	public boolean isDLCEnabled() { return dlcEnabled; }


	@Override
	public int compareTo(Score other) {
		if (this.getValue() > other.getValue()) return 1;
		if (this.getValue() < other.getValue()) return -1;
		return 0;
	}


	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(String.format("Name: %-25s  ShipId: %-25s  Score: %5d\n", shipName, shipId, value));
		result.append(String.format("Sector: %d  Difficulty: %-6s  Victory: %-5b  DLC Enabled: %-5b\n", sector, difficulty.toString(), victory, dlcEnabled));

		return result.toString();
	}
}
