package net.blerf.ftl.model;

import net.blerf.ftl.constants.NewbieTipLevel;
import net.blerf.ftl.model.Stats.StatType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Profile {

	private int unknownHeaderAlpha;
	private Map<String, ShipAvailability> shipUnlockMap;
	private Stats stats;

	private NewbieTipLevel newbieTipLevel = NewbieTipLevel.VETERAN;


	public Profile() {
	}

	/**
	 * Copy constructor.
	 *
	 * Each ShipAvailability and Stats will be
	 * copy-constructed as well.
	 */
	public Profile(Profile srcProfile) {
		unknownHeaderAlpha = srcProfile.getHeaderAlpha();

		shipUnlockMap = new LinkedHashMap<>();
		for (Map.Entry<String, ShipAvailability> entry : srcProfile.getShipUnlockMap().entrySet()) {
			shipUnlockMap.put(entry.getKey(), new ShipAvailability(entry.getValue()));
		}

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

	public Map<String, ShipAvailability> getShipUnlockMap() {
		return shipUnlockMap;
	}

	public Stats getStats() {
		return stats;
	}

}
