package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import net.blerf.ftl.model.state.DoorState;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.model.state.ShipState;
import net.blerf.ftl.model.state.SystemState;
import net.blerf.ftl.model.type.SystemType;
import nl.nielspoldervaart.ftlav.visualiser.GraphLineColor;
import nl.nielspoldervaart.ftlav.visualiser.PlotType;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.StringList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

@Getter
public class TableRow {

	@CsvBindByName(column = "time")
	@CsvDate(DataUtil.DATE_FORMAT_PATTERN)
	private Date time;

	// Non-temporal
	@CsvBindByName(column = "ship name")
	private String shipName;
	@CsvBindByName(column = "ship type")
	private String shipType;
	@CsvBindByName(column = "difficulty")
	private String difficulty;
	@CsvBindByName(column = "ae content enabled")
	private boolean AEContentEnabled;

	// Sector
	@VisualiserAnnotation(category = TableColumnCategory.SECTOR)
	@CsvBindByName(column = "beacons explored")
	private int beaconsExplored;
	@VisualiserAnnotation(category = TableColumnCategory.SECTOR)
	@CsvBindByName(column = "sector number")
	private int sectorNumber;
	@VisualiserAnnotation(category = TableColumnCategory.SECTOR)
	@CsvBindByName(column = "sector type")
	private String sectorType;
	@VisualiserAnnotation(category = TableColumnCategory.SECTOR)
	@CsvBindByName(column = "sector name")
	private String sectorName;
	@VisualiserAnnotation(category = TableColumnCategory.SECTOR)
	@CsvBindByName(column = "fleet advancement")
	private int fleetAdvancement;

	// Beacon
	@VisualiserAnnotation(category = TableColumnCategory.BEACON)
	@CsvBindByName(column = "encounter text")
	private String encounterText;
	@VisualiserAnnotation(category = TableColumnCategory.BEACON)
	@CsvCustomBindByName(column = "beacon hazards", converter = JsonArrayCellProcessor.class)
	private JSONArray beaconHazards;
	@VisualiserAnnotation(category = TableColumnCategory.BEACON)
	@CsvCustomBindByName(column = "store", converter = JsonObjectCellProcessor.class)
	private JSONObject store;

	// Resources totals
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.RESOURCE_TOTALS, plotType = PlotType.LINE)
	@CsvBindByName(column = "total ships defeated")
	private int totalShipsDefeated;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.PURPLE, category = TableColumnCategory.RESOURCE_TOTALS, plotType = PlotType.LINE)
	@CsvBindByName(column = "total scrap collected")
	private int totalScrapCollected;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.RESOURCE_TOTALS, plotType = PlotType.LINE)
	@CsvBindByName(column = "total crew hired")
	private int totalCrewHired;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.RESOURCE_TOTALS, plotType = PlotType.LINE)
	@CsvBindByName(column = "score")
	private int score;

	// Resources current
	@VisualiserAnnotation(isEnabledByDefault = true, defaultGraphLineColor = GraphLineColor.PURPLE, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "scrap")
	private int scrap;
	@VisualiserAnnotation(isEnabledByDefault = true, defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "fuel")
	private int fuel;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "missles")
	private int missles;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "drone parts")
	private int droneParts;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "crew size")
	private int crewSize;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "hull")
	private int hull;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.RESOURCES, plotType = PlotType.LINE)
	@CsvBindByName(column = "oxygen level")
	private int oxygenLevel;
	@CsvCustomBindByName(column = "ship augments", converter = JsonArrayCellProcessor.class)
	private JSONArray shipAugments;
	@CsvCustomBindByName(column = "cargo", converter = JsonArrayCellProcessor.class)
	private JSONArray cargo;
	@CsvCustomBindByName(column = "crew", converter = JsonArrayCellProcessor.class)
	private JSONArray crew;

	// Systems current
	// Pilot
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.PILOT)
	@CsvBindByName(column = "pilot system power capacity")
	private int pilotSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.PILOT)
	@CsvBindByName(column = "pilot system damage")
	private int pilotSystemDamage;
	// Sensors
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SENSORS)
	@CsvBindByName(column = "sensor system power capacity")
	private int sensorSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SENSORS)
	@CsvBindByName(column = "sensor system damage")
	private int sensorSystemDamage;
	// Doors
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DOORS)
	@CsvBindByName(column = "door system power capacity")
	private int doorSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DOORS)
	@CsvBindByName(column = "door system damage")
	private int doorSystemDamage;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DOORS)
	@CsvBindByName(column = "door count")
	private int doorCount;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DOORS)
	@CsvBindByName(column = "doors open")
	private int doorsOpen;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DOORS)
	@CsvBindByName(column = "doors damaged")
	private int doorsDamaged;
	// Battery
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.BATTERY_BACKUP)
	@CsvBindByName(column = "battery system power capacity")
	private int batterySystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.BATTERY_BACKUP)
	@CsvBindByName(column = "battery system damage")
	private int batterySystemDamage;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.BATTERY_BACKUP)
	@CsvBindByName(column = "battery system use")
	private int batterySystemUse;
	// Medbay
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MED)
	@CsvBindByName(column = "medbay system power capacity")
	private int medbaySystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MED)
	@CsvBindByName(column = "medbay system power consumption")
	private int medbaySystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MED)
	@CsvBindByName(column = "medbay system damage")
	private int medbaySystemDamage;
	// Oxygen
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.OXYGEN)
	@CsvBindByName(column = "oxygen system power capacity")
	private int oxygenSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.OXYGEN)
	@CsvBindByName(column = "oxygen system power consumption")
	private int oxygenSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.OXYGEN)
	@CsvBindByName(column = "oxygen system damage")
	private int oxygenSystemDamage;
	// Shields
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SHIELDS)
	@CsvBindByName(column = "shield system power capacity")
	private int shieldSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SHIELDS)
	@CsvBindByName(column = "shield system power consumption")
	private int shieldSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SHIELDS)
	@CsvBindByName(column = "shield system damage")
	private int shieldSystemDamage;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.PURPLE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SHIELDS)
	@CsvBindByName(column = "shield layers")
	private int shieldLayers;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.SHIELDS)
	@CsvBindByName(column = "zoltan shield layers")
	private int zoltanShieldLayers;
	// Engines
	@CsvBindByName(column = "engine system power capacity")
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ENGINE)
	private int engineSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ENGINE)
	@CsvBindByName(column = "engine system power consumption")
	private int engineSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ENGINE)
	@CsvBindByName(column = "engine system damage")
	private int engineSystemDamage;
	// Weapons
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.WEAPONS)
	@CsvBindByName(column = "weapons system power capacity")
	private int weaponsSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.WEAPONS)
	@CsvBindByName(column = "weapons system power consumption")
	private int weaponsSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.WEAPONS)
	@CsvBindByName(column = "weapons system damage")
	private int weaponsSystemDamage;
	@VisualiserAnnotation(category = TableColumnCategory.SYSTEMS, system = ShipSystemType.WEAPONS)
	@CsvCustomBindByName(column = "weapons layout", converter = JsonArrayCellProcessor.class)
	private JSONArray weaponsLayout;
	// Drone Control
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DRONES)
	@CsvBindByName(column = "drone control system power capacity")
	private int droneControlSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DRONES)
	@CsvBindByName(column = "drone control system power consumption")
	private int droneControlSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DRONES)
	@CsvBindByName(column = "drone control system damage")
	private int droneControlSystemDamage;
	@VisualiserAnnotation(category = TableColumnCategory.SYSTEMS, system = ShipSystemType.DRONES)
	@CsvCustomBindByName(column = "drone control layout", converter = JsonArrayCellProcessor.class)
	private JSONArray droneControlLayout;
	// Teleporter
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.TELEPORT)
	@CsvBindByName(column = "teleporter system power capacity")
	private int teleporterSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.TELEPORT)
	@CsvBindByName(column = "teleporter system power consumption")
	private int teleporterSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.TELEPORT)
	@CsvBindByName(column = "teleporter system damage")
	private int teleporterSystemDamage;
	// Cloaking
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLOAK)
	@CsvBindByName(column = "cloaking system power capacity")
	private int cloakingSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLOAK)
	@CsvBindByName(column = "cloaking system power consumption")
	private int cloakingSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLOAK)
	@CsvBindByName(column = "cloaking system damage")
	private int cloakingSystemDamage;
	@VisualiserAnnotation(category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLOAK)
	@CsvBindByName(column = "cloaked")
	private boolean cloaked;
	// Artillery
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ARTILLERY)
	@CsvBindByName(column = "artillery system power capacity")
	private int artillerySystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ARTILLERY)
	@CsvBindByName(column = "artillery system power consumption")
	private int artillerySystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.ARTILLERY)
	@CsvBindByName(column = "artillery system damage")
	private int artillerySystemDamage;
	// Clonebay
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLONE)
	@CsvBindByName(column = "clone bay system power capacity")
	private int cloneBaySystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLONE)
	@CsvBindByName(column = "clone bay system power consumption")
	private int cloneBaySystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.CLONE)
	@CsvBindByName(column = "clone bay system damage")
	private int cloneBaySystemDamage;
	// Mind Control
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MINDCONTROL)
	@CsvBindByName(column = "mind control system power capacity")
	private int mindControlSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MINDCONTROL)
	@CsvBindByName(column = "mind control system power consumption")
	private int mindControlSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.MINDCONTROL)
	@CsvBindByName(column = "mind control system damage")
	private int mindControlSystemDamage;
	// Hacking System
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.GREEN, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.HACKING)
	@CsvBindByName(column = "hacking system power capacity")
	private int hackingSystemPowerCapacity;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.BLUE, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.HACKING)
	@CsvBindByName(column = "hacking system power consumption")
	private int hackingSystemPowerConsumption;
	@VisualiserAnnotation(defaultGraphLineColor = GraphLineColor.RED, category = TableColumnCategory.SYSTEMS, system = ShipSystemType.HACKING)
	@CsvBindByName(column = "hacking system damage")
	private int hackingSystemDamage;

	// Nearby Ship
	@CsvBindByName(column = "nearby ship exists")
	private boolean nearbyShipExists;
	@CsvBindByName(column = "nearby ship hostile")
	private boolean nearbyShipHostile;
	// Nearby Ship Resources current
	@CsvBindByName(column = "nearby ship crew size")
	private int nearbyShipCrewSize;
	@CsvBindByName(column = "nearby ship hull")
	private int nearbyShipHull;
	@CsvBindByName(column = "nearby ship oxygen level")
	private int nearbyShipOxygenLevel;
	@CsvCustomBindByName(column = "nearby ship augments", converter = JsonArrayCellProcessor.class)
	private JSONArray nearbyShipAugments;
	@CsvCustomBindByName(column = "nearby ship crew", converter = JsonArrayCellProcessor.class)
	private JSONArray nearbyShipCrew;
	// Nearby Ship Systems Current
	@CsvBindByName(column = "nearby ship pilot system power capacity")
	private int nearbyShipPilotSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship pilot system damage")
	private int nearbyShipPilotSystemDamage;
	// Sensors
	@CsvBindByName(column = "nearby ship sensor system power capacity")
	private int nearbyShipSensorSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship sensor system damage")
	private int nearbyShipSensorSystemDamage;
	// Doors
	@CsvBindByName(column = "nearby ship door system power capacity")
	private int nearbyShipDoorSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship door system damage")
	private int nearbyShipDoorSystemDamage;
	@CsvBindByName(column = "nearby ship door count")
	private int nearbyShipDoorCount;
	@CsvBindByName(column = "nearby ship doors open")
	private int nearbyShipDoorsOpen;
	@CsvBindByName(column = "nearby ship doors damaged")
	private int nearbyShipDoorsDamaged;
	// Battery
	@CsvBindByName(column = "nearby ship battery system power capacity")
	private int nearbyShipBatterySystemPowerCapacity;
	@CsvBindByName(column = "nearby ship battery system damage")
	private int nearbyShipBatterySystemDamage;
	@CsvBindByName(column = "nearby ship battery system use")
	private int nearbyShipBatterySystemUse;
	// Medbay
	@CsvBindByName(column = "nearby ship medbay system power capacity")
	private int nearbyShipMedbaySystemPowerCapacity;
	@CsvBindByName(column = "nearby ship medbay system power consumption")
	private int nearbyShipMedbaySystemPowerConsumption;
	@CsvBindByName(column = "nearby ship medbay system damage")
	private int nearbyShipMedbaySystemDamage;
	// Oxygen
	@CsvBindByName(column = "nearby ship oxygen system power capacity")
	private int nearbyShipOxygenSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship oxygen system power consumption")
	private int nearbyShipOxygenSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship oxygen system damage")
	private int nearbyShipOxygenSystemDamage;
	// Shields
	@CsvBindByName(column = "nearby ship shield system power capacity")
	private int nearbyShipShieldSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship shield system power consumption")
	private int nearbyShipShieldSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship shield system damage")
	private int nearbyShipShieldSystemDamage;
	@CsvBindByName(column = "nearby ship shield layers")
	private int nearbyShipShieldLayers;
	@CsvBindByName(column = "nearby ship zoltan shield layers")
	private int nearbyShipZoltanShieldLayers;
	// Engines
	@CsvBindByName(column = "nearby ship engine system power capacity")
	private int nearbyShipEngineSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship engine system power consumption")
	private int nearbyShipEngineSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship engine system damage")
	private int nearbyShipEngineSystemDamage;
	// Weapons
	@CsvBindByName(column = "nearby ship weapons system power capacity")
	private int nearbyShipWeaponsSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship weapons system power consumption")
	private int nearbyShipWeaponsSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship weapons system damage")
	private int nearbyShipWeaponsSystemDamage;
	@CsvCustomBindByName(column = "nearby ship weapons layout", converter = JsonArrayCellProcessor.class)
	private JSONArray nearbyShipWeaponsLayout;
	// Drone Control
	@CsvBindByName(column = "nearby ship drone control system power capacity")
	private int nearbyShipDroneControlSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship drone control system power consumption")
	private int nearbyShipDroneControlSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship drone control system damage")
	private int nearbyShipDroneControlSystemDamage;
	@CsvCustomBindByName(column = "nearby ship drone control layout", converter = JsonArrayCellProcessor.class)
	private JSONArray nearbyShipDroneControlLayout;
	// Teleporter
	@CsvBindByName(column = "nearby ship teleporter system power capacity")
	private int nearbyShipTeleporterSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship teleporter system power consumption")
	private int nearbyShipTeleporterSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship teleporter system damage")
	private int nearbyShipTeleporterSystemDamage;
	// Cloaking
	@CsvBindByName(column = "nearby ship cloaking system power capacity")
	private int nearbyShipCloakingSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship cloaking system power consumption")
	private int nearbyShipCloakingSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship cloaking system damage")
	private int nearbyShipCloakingSystemDamage;
	@CsvBindByName(column = "nearby ship cloaked")
	private boolean nearbyShipCloaked;
	// Artillery
	@CsvBindByName(column = "nearby ship artillery system power capacity")
	private int nearbyShipArtillerySystemPowerCapacity;
	@CsvBindByName(column = "nearby ship artillery system power consumption")
	private int nearbyShipArtillerySystemPowerConsumption;
	@CsvBindByName(column = "nearby ship artillery system damage")
	private int nearbyShipArtillerySystemDamage;
	// Clonebay
	@CsvBindByName(column = "nearby ship clone bay system power capacity")
	private int nearbyShipCloneBaySystemPowerCapacity;
	@CsvBindByName(column = "nearby ship clone bay system power consumption")
	private int nearbyShipCloneBaySystemPowerConsumption;
	@CsvBindByName(column = "nearby ship clone bay system damage")
	private int nearbyShipCloneBaySystemDamage;
	// Mind Control
	@CsvBindByName(column = "nearby ship mind control system power capacity")
	private int nearbyShipmindControlSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship mind control system power consumption")
	private int nearbyShipMindControlSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship mind control system damage")
	private int nearbyShipMindControlSystemDamage;
	// Hacking System
	@CsvBindByName(column = "nearby ship hacking system power capacity")
	private int nearbyShipHackingSystemPowerCapacity;
	@CsvBindByName(column = "nearby ship hacking system power consumption")
	private int nearbyShipHackingSystemPowerConsumption;
	@CsvBindByName(column = "nearby ship hacking system damage")
	private int nearbyShipHackingSystemDamage;

	@SuppressWarnings("unused")
	public TableRow() {}
	public TableRow(SavedGameState gameState, long gameStateFileModificationTime) {

		time = new Date(gameStateFileModificationTime);

		ShipState playerShip = gameState.getPlayerShip();

		// Non-temporal
		shipName = gameState.getPlayerShipName();
		shipType = DataUtil.getFullShipType(gameState);
		difficulty = gameState.getDifficulty().toString();
		AEContentEnabled = gameState.isDLCEnabled();

		// Location
		beaconsExplored = gameState.getTotalBeaconsExplored();
		sectorNumber = gameState.getSectorNumber() + 1;
		sectorType = DataUtil.getSector(gameState).getType();
		sectorName = DataUtil.getSector(gameState).getTitle();
		fleetAdvancement = DataUtil.getRebelFleetAdvancement(gameState);

		// Beacon
		encounterText = gameState.getEncounter().getText();
		beaconHazards = DataUtil.getBeaconHazards(gameState);
		store = DataUtil.getStoreContents(gameState);

		// Resource totals
		totalShipsDefeated = gameState.getTotalShipsDefeated();
		totalScrapCollected = gameState.getTotalScrapCollected();
		totalCrewHired = gameState.getTotalCrewHired();
		score = gameState.calculateScore();

		// Resources current
		scrap = playerShip.getScrapAmt();
		fuel = playerShip.getFuelAmt();
		missles = playerShip.getMissilesAmt();
		droneParts = playerShip.getDronePartsAmt();
		crewSize = DataUtil.getPlayerCrewCount(playerShip);
		hull = playerShip.getHullAmt();
		oxygenLevel = DataUtil.getShipOxygenLevel(playerShip);
		shipAugments = DataUtil.getShipAugments(playerShip);
		cargo = new JSONArray(new StringList(gameState.getCargoIdList()));
		crew = DataUtil.getCrewList(playerShip);

		// Systems current
		// Pilot
		SystemState pilotSystem = playerShip.getSystem(SystemType.PILOT);
		if (pilotSystem.getCapacity() != 0) {
			pilotSystemPowerCapacity = pilotSystem.getCapacity();
			pilotSystemDamage = pilotSystem.getDamagedBars();
		}
		// Sensors
		SystemState sensorSystem = playerShip.getSystem(SystemType.SENSORS);
		if (sensorSystem.getCapacity() != 0) {
			sensorSystemPowerCapacity = sensorSystem.getCapacity();
			sensorSystemDamage = sensorSystem.getDamagedBars();
		}
		// Doors
		SystemState doorSystem = playerShip.getSystem(SystemType.DOORS);
		if (doorSystem.getCapacity() != 0) {
			doorSystemPowerCapacity = doorSystem.getCapacity();
			doorSystemDamage = doorSystem.getDamagedBars();
			for (DoorState doorState : playerShip.getDoorMap().values()) {
				doorCount++;
				if (doorState.isOpen()) {
					doorsOpen++;
				}
				if (doorState.getHealth() < doorState.getNominalHealth()) {
					doorsDamaged++;
				}
			}
		}
		// Battery
		SystemState batterySystem = playerShip.getSystem(SystemType.BATTERY);
		if (batterySystem.getCapacity() != 0) {
			batterySystemPowerCapacity = batterySystem.getCapacity();
			batterySystemDamage = batterySystem.getDamagedBars();
			batterySystemUse = DataUtil.getBatterySystemUse(playerShip);
		}
		// Medbay
		SystemState medbaySystem = playerShip.getSystem(SystemType.MEDBAY);
		if (medbaySystem.getCapacity() != 0) {
			medbaySystemPowerCapacity = medbaySystem.getCapacity();
			medbaySystemPowerConsumption = medbaySystem.getPower();
			medbaySystemDamage = medbaySystem.getDamagedBars();
		}
		// Oxygen
		SystemState oxygenSystem = playerShip.getSystem(SystemType.OXYGEN);
		if (oxygenSystem.getCapacity() != 0) {
			oxygenSystemPowerCapacity = oxygenSystem.getCapacity();
			oxygenSystemPowerConsumption = oxygenSystem.getPower();
			oxygenSystemDamage = oxygenSystem.getDamagedBars();
		}
		// Shields
		SystemState shieldSystem = playerShip.getSystem(SystemType.SHIELDS);
		if (shieldSystem.getCapacity() != 0) {
			shieldSystemPowerCapacity = shieldSystem.getCapacity();
			shieldSystemPowerConsumption = shieldSystem.getPower();
			shieldSystemDamage = shieldSystem.getDamagedBars();
			shieldLayers = DataUtil.getShieldLayers(playerShip);
			zoltanShieldLayers = DataUtil.getZoltanShieldLayers(playerShip);
		}
		// Engines
		SystemState engineSystem = playerShip.getSystem(SystemType.ENGINES);
		if (engineSystem.getCapacity() != 0) {
			engineSystemPowerCapacity = engineSystem.getCapacity();
			engineSystemPowerConsumption = engineSystem.getPower();
			engineSystemDamage = engineSystem.getDamagedBars();
		}
		// Weapons
		SystemState weaponsSystem = playerShip.getSystem(SystemType.WEAPONS);
		if (weaponsSystem.getCapacity() != 0) {
			weaponsSystemPowerCapacity = weaponsSystem.getCapacity();
			weaponsSystemPowerConsumption = weaponsSystem.getPower();
			weaponsSystemDamage = weaponsSystem.getDamagedBars();
			weaponsLayout = DataUtil.getWeaponLayout(playerShip);
		}
		// Drone Control
		SystemState droneControlSystem = playerShip.getSystem(SystemType.DRONE_CTRL);
		if (droneControlSystem.getCapacity() != 0) {
			droneControlSystemPowerCapacity = droneControlSystem.getCapacity();
			droneControlSystemPowerConsumption = droneControlSystem.getPower();
			droneControlSystemDamage = droneControlSystem.getDamagedBars();
			droneControlLayout = DataUtil.getDroneLayout(playerShip);
		}
		// Teleporter
		SystemState teleporterSystem = playerShip.getSystem(SystemType.TELEPORTER);
		if (teleporterSystem.getCapacity() != 0) {
			teleporterSystemPowerCapacity = teleporterSystem.getCapacity();
			teleporterSystemPowerConsumption = teleporterSystem.getPower();
			teleporterSystemDamage = teleporterSystem.getDamagedBars();
		}
		// Cloaking
		SystemState cloakingSystem = playerShip.getSystem(SystemType.CLOAKING);
		if (cloakingSystem.getCapacity() != 0) {
			cloakingSystemPowerCapacity = cloakingSystem.getCapacity();
			cloakingSystemPowerConsumption = cloakingSystem.getPower();
			cloakingSystemDamage = cloakingSystem.getDamagedBars();
			cloaked = playerShip.getCloakAnimTicks() > 0;
		}
		// Artillery
		SystemState artillerySystem = playerShip.getSystem(SystemType.ARTILLERY);
		if (artillerySystem.getCapacity() != 0) {
			artillerySystemPowerCapacity = artillerySystem.getCapacity();
			artillerySystemPowerConsumption = artillerySystem.getPower();
			artillerySystemDamage = artillerySystem.getDamagedBars();
		}
		// Clonebay
		SystemState clonebaySystem = playerShip.getSystem(SystemType.CLONEBAY);
		if (clonebaySystem.getCapacity() != 0) {
			cloneBaySystemPowerCapacity = clonebaySystem.getCapacity();
			cloneBaySystemPowerConsumption = clonebaySystem.getPower();
			cloneBaySystemDamage = clonebaySystem.getDamagedBars();
		}
		// Mind Control
		SystemState mindControlSystem = playerShip.getSystem(SystemType.MIND);
		if (mindControlSystem.getCapacity() != 0) {
			mindControlSystemPowerCapacity = mindControlSystem.getCapacity();
			mindControlSystemPowerConsumption = mindControlSystem.getPower();
			mindControlSystemDamage = mindControlSystem.getDamagedBars();
		}
		// Hacking System
		SystemState hackingSystem = playerShip.getSystem(SystemType.HACKING);
		if (hackingSystem.getCapacity() != 0) {
			hackingSystemPowerCapacity = hackingSystem.getCapacity();
			hackingSystemPowerConsumption = hackingSystem.getPower();
			hackingSystemDamage = hackingSystem.getDamagedBars();
		}

		// Nearby Ship
		ShipState nearbyShip = gameState.getNearbyShip();
		nearbyShipExists = nearbyShip != null;
		if (nearbyShipExists) {
			nearbyShipHostile = nearbyShip.isHostile();

			nearbyShipCrewSize = nearbyShip.getCrewList().size();
			nearbyShipHull = nearbyShip.getHullAmt();
			nearbyShipOxygenLevel = DataUtil.getShipOxygenLevel(nearbyShip);
			nearbyShipAugments = DataUtil.getShipAugments(nearbyShip);
			nearbyShipCrew = DataUtil.getCrewList(nearbyShip);

			SystemState nearbyShipPilotSystem = nearbyShip.getSystem(SystemType.PILOT);
			if (nearbyShipPilotSystem.getCapacity() != 0) {
				nearbyShipPilotSystemPowerCapacity = nearbyShipPilotSystem.getCapacity();
				nearbyShipPilotSystemDamage = nearbyShipPilotSystem.getDamagedBars();
			}
			// Sensors
			SystemState nearbyShipSensorSystem = nearbyShip.getSystem(SystemType.SENSORS);
			if (nearbyShipSensorSystem.getCapacity() != 0) {
				nearbyShipSensorSystemPowerCapacity = nearbyShipSensorSystem.getCapacity();
				nearbyShipSensorSystemDamage = nearbyShipSensorSystem.getDamagedBars();
			}
			// Doors
			SystemState nearbyShipDoorSystem = nearbyShip.getSystem(SystemType.DOORS);
			if (nearbyShipDoorSystem.getCapacity() != 0) {
				nearbyShipDoorSystemPowerCapacity = nearbyShipDoorSystem.getCapacity();
				nearbyShipDoorSystemDamage = nearbyShipDoorSystem.getDamagedBars();
				for (DoorState doorState : nearbyShip.getDoorMap().values()) {
					nearbyShipDoorCount++;
					if (doorState.isOpen()) {
						nearbyShipDoorsOpen++;
					}
					if (doorState.getHealth() < doorState.getNominalHealth()) {
						nearbyShipDoorsDamaged++;
					}
				}
			}
			// Battery
			SystemState nearbyShipBatterySystem = nearbyShip.getSystem(SystemType.BATTERY);
			if (nearbyShipBatterySystem.getCapacity() != 0) {
				nearbyShipBatterySystemPowerCapacity = nearbyShipBatterySystem.getCapacity();
				nearbyShipBatterySystemDamage = nearbyShipBatterySystem.getDamagedBars();
				nearbyShipBatterySystemUse = DataUtil.getBatterySystemUse(nearbyShip);
			}
			// Medbay
			SystemState nearbyShipMedbaySystem = nearbyShip.getSystem(SystemType.MEDBAY);
			if (nearbyShipMedbaySystem.getCapacity() != 0) {
				nearbyShipMedbaySystemPowerCapacity = nearbyShipMedbaySystem.getCapacity();
				nearbyShipMedbaySystemPowerConsumption = nearbyShipMedbaySystem.getPower();
				nearbyShipMedbaySystemDamage = nearbyShipMedbaySystem.getDamagedBars();
			}
			// Oxygen
			SystemState nearbyShipOxygenSystem = nearbyShip.getSystem(SystemType.OXYGEN);
			if (nearbyShipOxygenSystem.getCapacity() != 0) {
				nearbyShipOxygenSystemPowerCapacity = nearbyShipOxygenSystem.getCapacity();
				nearbyShipOxygenSystemPowerConsumption = nearbyShipOxygenSystem.getPower();
				nearbyShipOxygenSystemDamage = nearbyShipOxygenSystem.getDamagedBars();
			}
			// Shields
			SystemState nearbyShipShieldSystem = nearbyShip.getSystem(SystemType.SHIELDS);
			if (nearbyShipShieldSystem.getCapacity() != 0) {
				nearbyShipShieldSystemPowerCapacity = nearbyShipShieldSystem.getCapacity();
				nearbyShipShieldSystemPowerConsumption = shieldSystem.getPower();
				nearbyShipShieldSystemDamage = shieldSystem.getDamagedBars();
				nearbyShipShieldLayers = DataUtil.getShieldLayers(nearbyShip);
				nearbyShipZoltanShieldLayers = DataUtil.getZoltanShieldLayers(nearbyShip);
			}
			// Engines
			SystemState nearbyShipEngineSystem = nearbyShip.getSystem(SystemType.ENGINES);
			if (nearbyShipEngineSystem.getCapacity() != 0) {
				nearbyShipEngineSystemPowerCapacity = nearbyShipEngineSystem.getCapacity();
				nearbyShipEngineSystemPowerConsumption = nearbyShipEngineSystem.getPower();
				nearbyShipEngineSystemDamage = nearbyShipEngineSystem.getDamagedBars();
			}
			// Weapons
			SystemState nearbyShipWeaponsSystem = nearbyShip.getSystem(SystemType.WEAPONS);
			if (nearbyShipWeaponsSystem.getCapacity() != 0) {
				nearbyShipWeaponsSystemPowerCapacity = nearbyShipWeaponsSystem.getCapacity();
				nearbyShipWeaponsSystemPowerConsumption = nearbyShipWeaponsSystem.getPower();
				nearbyShipWeaponsSystemDamage = nearbyShipWeaponsSystem.getDamagedBars();
				nearbyShipWeaponsLayout = DataUtil.getWeaponLayout(nearbyShip);
			}
			// Drone Control
			SystemState nearbyShipDroneControlSystem = nearbyShip.getSystem(SystemType.DRONE_CTRL);
			if (nearbyShipDroneControlSystem.getCapacity() != 0) {
				nearbyShipDroneControlSystemPowerCapacity = nearbyShipDroneControlSystem.getCapacity();
				nearbyShipDroneControlSystemPowerConsumption = nearbyShipDroneControlSystem.getPower();
				nearbyShipDroneControlSystemDamage = nearbyShipDroneControlSystem.getDamagedBars();
				nearbyShipDroneControlLayout = DataUtil.getDroneLayout(nearbyShip);
			}
			// Teleporter
			SystemState nearbyShipTeleporterSystem = nearbyShip.getSystem(SystemType.TELEPORTER);
			if (nearbyShipTeleporterSystem.getCapacity() != 0) {
				nearbyShipTeleporterSystemPowerCapacity = nearbyShipTeleporterSystem.getCapacity();
				nearbyShipTeleporterSystemPowerConsumption = nearbyShipTeleporterSystem.getPower();
				nearbyShipTeleporterSystemDamage = nearbyShipTeleporterSystem.getDamagedBars();
			}
			// Cloaking
			SystemState nearbyShipCloakingSystem = nearbyShip.getSystem(SystemType.CLOAKING);
			if (nearbyShipCloakingSystem.getCapacity() != 0) {
				nearbyShipCloakingSystemPowerCapacity = nearbyShipCloakingSystem.getCapacity();
				nearbyShipCloakingSystemPowerConsumption = nearbyShipCloakingSystem.getPower();
				nearbyShipCloakingSystemDamage = nearbyShipCloakingSystem.getDamagedBars();
				nearbyShipCloaked = nearbyShip.getCloakAnimTicks() > 0;
			}
			// Artillery
			SystemState nearbyShipArtillerySystem = nearbyShip.getSystem(SystemType.ARTILLERY);
			if (nearbyShipArtillerySystem.getCapacity() != 0) {
				nearbyShipArtillerySystemPowerCapacity = nearbyShipArtillerySystem.getCapacity();
				nearbyShipArtillerySystemPowerConsumption = nearbyShipArtillerySystem.getPower();
				nearbyShipArtillerySystemDamage = nearbyShipArtillerySystem.getDamagedBars();
			}
			// Clonebay
			SystemState nearbyShipClonebaySystem = nearbyShip.getSystem(SystemType.CLONEBAY);
			if (nearbyShipClonebaySystem.getCapacity() != 0) {
				nearbyShipCloneBaySystemPowerCapacity = nearbyShipClonebaySystem.getCapacity();
				nearbyShipCloneBaySystemPowerConsumption = nearbyShipClonebaySystem.getPower();
				nearbyShipCloneBaySystemDamage = nearbyShipClonebaySystem.getDamagedBars();
			}
			// Mind Control
			SystemState nearbyShipMindControlSystem = nearbyShip.getSystem(SystemType.MIND);
			if (nearbyShipMindControlSystem.getCapacity() != 0) {
				nearbyShipmindControlSystemPowerCapacity = nearbyShipMindControlSystem.getCapacity();
				nearbyShipMindControlSystemPowerConsumption = nearbyShipMindControlSystem.getPower();
				nearbyShipMindControlSystemDamage = nearbyShipMindControlSystem.getDamagedBars();
			}
			// Hacking System
			SystemState nearbyShipHackingSystem = nearbyShip.getSystem(SystemType.HACKING);
			if (nearbyShipHackingSystem.getCapacity() != 0) {
				nearbyShipHackingSystemPowerCapacity = nearbyShipHackingSystem.getCapacity();
				nearbyShipHackingSystemPowerConsumption = nearbyShipHackingSystem.getPower();
				nearbyShipHackingSystemDamage = nearbyShipHackingSystem.getDamagedBars();
			}
		}

	}

	public static ArrayList<String> getTableWriterHeaders() {
		ArrayList<String> headers = new ArrayList<>();
		Field[] fields = TableRow.class.getDeclaredFields();
		for (Field field : fields) {
			headers.add(getTableWriterHeader(field));
		}
		return headers;
	}

	public static String getTableWriterHeader(Field field) {
		CsvBindByName csvAnnotation1 = field.getAnnotation(CsvBindByName.class);
		if (csvAnnotation1 != null && csvAnnotation1.column() != null && !csvAnnotation1.column().isEmpty()) {
			return csvAnnotation1.column().toUpperCase();
		}
		CsvCustomBindByName csvAnnotation2 = field.getAnnotation(CsvCustomBindByName.class);
		if (csvAnnotation2 != null && csvAnnotation2.column() != null && !csvAnnotation2.column().isEmpty()) {
			return csvAnnotation2.column().toUpperCase();
		}
		return field.getName().toUpperCase();
	}

	public static VisualiserAnnotation getVisualiserAnnotation(String fieldName) {
		try {
			Field field = TableRow.class.getDeclaredField(fieldName);
			return field.getAnnotation(VisualiserAnnotation.class);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public Object getFieldValue(String fieldName) {
		try {
			return this.getClass().getDeclaredField(fieldName).get(this);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			return null;
		}
	}

}
