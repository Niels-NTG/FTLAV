package net.blerf.ftl.model;

import net.blerf.ftl.constants.NewbieTipLevel;


public class Profile {

	private int unknownHeaderAlpha;
	private Stats stats;

	private NewbieTipLevel newbieTipLevel = NewbieTipLevel.VETERAN;


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

		newbieTipLevel = srcProfile.getNewbieTipLevel();
	}


	public int getHeaderAlpha() {
		return unknownHeaderAlpha;
	}

	/**
	 * Sets the newbie tip level.
	 *
	 * This was introduced in FTL 1.5.4.
	 *
	 * @see net.blerf.ftl.constants.NewbieTipLevel
	 */
	public void setNewbieTipLevel(NewbieTipLevel level) {
		newbieTipLevel = level;
	}
	public NewbieTipLevel getNewbieTipLevel() {
		return newbieTipLevel;
	}

	public Stats getStats() {
		return stats;
	}

}
