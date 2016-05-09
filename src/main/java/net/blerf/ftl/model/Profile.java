package net.blerf.ftl.model;


public class Profile {

	private int unknownHeaderAlpha;
	private Stats stats;

	public Profile() {
	}

	/**
	 * Copy constructor.
	 *
	 * Each Stats will be
	 * copy-constructed as well.
	 */
	public Profile(Profile srcProfile) {
		unknownHeaderAlpha = srcProfile.getHeaderAlpha();

		stats = new Stats(srcProfile.getStats());

	}


	public int getHeaderAlpha() {
		return unknownHeaderAlpha;
	}


	public Stats getStats() {
		return stats;
	}

}
