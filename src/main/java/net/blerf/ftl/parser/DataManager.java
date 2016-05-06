package net.blerf.ftl.parser;

import net.blerf.ftl.model.ShipLayout;
import net.blerf.ftl.xml.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public abstract class DataManager implements Closeable {

	private static DataManager instance = null;

	protected boolean dlcEnabledByDefault = false;


	public static void setInstance(DataManager dataManager) {
		instance = dataManager;
	}

	public static DataManager getInstance() {
		return instance;
	}

	public static DataManager get() {
		return instance;
	}


	@Override
	public void close() {
	}

	public void setDLCEnabledByDefault(boolean b) {
		dlcEnabledByDefault = b;
	}

	public boolean isDLCEnabledByDefault() {
		return dlcEnabledByDefault;
	}

	public boolean hasDataInputStream(String innerPath) {
		throw new UnsupportedOperationException();
	}

	public InputStream getDataInputStream(String innerPath) throws IOException {
		throw new UnsupportedOperationException();
	}

	public boolean hasResourceInputStream(String innerPath) {
		throw new UnsupportedOperationException();
	}

	public InputStream getResourceInputStream(String innerPath) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void extractDataDat(File extractDir) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void extractResourceDat(File extractDir) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Achievement getAchievement(String id) {
		throw new UnsupportedOperationException();
	}

	public Map<String, Achievement> getAchievements() {
		throw new UnsupportedOperationException();
	}

	public AugBlueprint getAugment(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public AugBlueprint getAugment(String id) {
		return getAugment(id, dlcEnabledByDefault);
	}

	public Map<String, AugBlueprint> getAugments(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, AugBlueprint> getAugments() {
		return getAugments(dlcEnabledByDefault);
	}

	public CrewBlueprint getCrew(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public CrewBlueprint getCrew(String id) {
		return getCrew(id, dlcEnabledByDefault);
	}

	public Map<String, CrewBlueprint> getCrews(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, CrewBlueprint> getCrews() {
		return getCrews(dlcEnabledByDefault);
	}

	public DroneBlueprint getDrone(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public DroneBlueprint getDrone(String id) {
		return getDrone(id, dlcEnabledByDefault);
	}

	public Map<String, DroneBlueprint> getDrones(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, DroneBlueprint> getDrones() {
		return getDrones(dlcEnabledByDefault);
	}

	public SystemBlueprint getSystem(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public SystemBlueprint getSystem(String id) {
		return getSystem(id, dlcEnabledByDefault);
	}

	public WeaponBlueprint getWeapon(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public WeaponBlueprint getWeapon(String id) {
		return getWeapon(id, dlcEnabledByDefault);
	}

	public Map<String, WeaponBlueprint> getWeapons(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	public Map<String, WeaponBlueprint> getWeapons() {
		return getWeapons(dlcEnabledByDefault);
	}

	public ShipBlueprint getShip(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public ShipBlueprint getShip(String id) {
		return getShip(id, dlcEnabledByDefault);
	}

	public Map<String, ShipBlueprint> getShips(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, ShipBlueprint> getShips() {
		return getShips(dlcEnabledByDefault);
	}

	public Map<String, ShipBlueprint> getAutoShips(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, ShipBlueprint> getAutoShips() {
		return getAutoShips(dlcEnabledByDefault);
	}

	public Map<String, ShipBlueprint> getPlayerShips(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, ShipBlueprint> getPlayerShips() {
		return getPlayerShips(dlcEnabledByDefault);
	}

	public List<String> getPlayerShipBaseIds(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public List<String> getPlayerShipBaseIds() {
		return getPlayerShipBaseIds(dlcEnabledByDefault);
	}

	public ShipBlueprint getPlayerShipVariant(String baseId, int n, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public ShipBlueprint getPlayerShipVariant(String baseId, int n) {
		return getPlayerShipVariant(baseId, n, dlcEnabledByDefault);
	}

	public List<Achievement> getShipAchievements(ShipBlueprint ship, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public List<Achievement> getShipAchievements(ShipBlueprint ship) {
		return getShipAchievements(ship, dlcEnabledByDefault);
	}

	public List<Achievement> getGeneralAchievements() {
		throw new UnsupportedOperationException();
	}

	public ShipLayout getShipLayout(String id) {
		throw new UnsupportedOperationException();
	}

	public ShipChassis getShipChassis(String id) {
		throw new UnsupportedOperationException();
	}

	public FTLEvent getEventById(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public FTLEvent getEventById(String id) {
		return getEventById(id, dlcEnabledByDefault);
	}

	public FTLEventList getEventListById(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public FTLEventList getEventListById(String id) {
		return getEventListById(id, dlcEnabledByDefault);
	}

	public Map<String, Encounters> getEncounters(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, Encounters> getEncounters() {
		return getEncounters(dlcEnabledByDefault);
	}

	public ShipEvent getShipEventById(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public ShipEvent getShipEventById(String id) {
		return getShipEventById(id, dlcEnabledByDefault);
	}

	public Map<String, ShipEvent> getShipEvents(boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public Map<String, ShipEvent> getShipEvents() {
		return getShipEvents(dlcEnabledByDefault);
	}

	public boolean getCrewSex() {
		throw new UnsupportedOperationException();
	}

	public String getCrewName(boolean isMale) {
		throw new UnsupportedOperationException();
	}

	public SectorType getSectorTypeById(String id, boolean dlcEnabled) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A frontend using the global DLC default.
	 */
	public SectorType getSectorTypeById(String id) {
		return getSectorTypeById(id, dlcEnabledByDefault);
	}

	public SectorDescription getSectorDescriptionById(String id) {
		throw new UnsupportedOperationException();
	}

	public Map<String, BackgroundImageList> getBackgroundImageLists() {
		throw new UnsupportedOperationException();
	}
}
