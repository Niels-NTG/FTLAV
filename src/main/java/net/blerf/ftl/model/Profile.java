package net.blerf.ftl.model;


public class Profile {

	private Stats stats;

	public Profile() {
	}

	/**
	 * Copy constructor.
	 *
	 * Each Stats will be
	 * copy-constructed as well.
	 */

	/**
	 * Sets the magic number indicating file format, apparently.
	 *
	 * 4 = Profile, FTL 1.01-1.03.3
	 * 9 = AE Profile, FTL 1.5.4+
	 */
	public void setHeaderAlpha( int n ) {
	}

	public void setStats( Stats stats ) {
		this.stats = stats;
	}
	public Stats getStats() {
		return stats;
	}

}
