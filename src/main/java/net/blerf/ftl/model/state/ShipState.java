package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.blerf.ftl.model.XYPair;
import net.blerf.ftl.model.shiplayout.DoorCoordinate;
import net.blerf.ftl.model.shiplayout.ShipLayout;
import net.blerf.ftl.model.shiplayout.ShipLayoutDoor;
import net.blerf.ftl.model.shiplayout.ShipLayoutRoom;
import net.blerf.ftl.model.systeminfo.ExtendedSystemInfo;
import net.blerf.ftl.model.type.SystemType;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.SystemBlueprint;
import net.blerf.ftl.xml.ship.AugmentId;
import net.blerf.ftl.xml.ship.ShipBlueprint;
import net.blerf.ftl.xml.ship.SystemList;
import net.blerf.ftl.xml.ship.SystemRoom;

public class ShipState {
    private String shipName;
    private String shipBlueprintId;
    private String shipLayoutId;
    private String shipGfxBaseName;
    private boolean auto;

    private final List<StartingCrewState> startingCrewList = new ArrayList<>();
    private boolean hostile = false;
    private int jumpChargeTicks = 0;
    private boolean jumping = false;
    private int jumpAnimTicks = 0;
    private int hullAmt = 0, fuelAmt = 0, dronePartsAmt = 0, missilesAmt = 0, scrapAmt = 0;
    private final List<CrewState> crewList = new ArrayList<>();
    private int reservePowerCapacity = 0;
    private final Map<SystemType, List<SystemState>> systemsMap = new LinkedHashMap<>();
    private List<ExtendedSystemInfo> extendedSystemInfoList = new ArrayList<>();
    private final List<RoomState> roomList = new ArrayList<>();
    private final Map<XYPair, Integer> breachMap = new LinkedHashMap<>();
    private final Map<DoorCoordinate, DoorState> doorMap = new LinkedHashMap<>();
    private int cloakAnimTicks = 0;
    private List<LockdownCrystal> lockdownCrystalList = new ArrayList<>();
    private final List<WeaponState> weaponList = new ArrayList<>();
    private final List<DroneState> droneList = new ArrayList<>();
    private final List<String> augmentIdList = new ArrayList<>();
    private final List<StandaloneDroneState> standaloneDroneList = new ArrayList<>();


    /**
     * Constructs an incomplete ShipState.
     * <p>
     * It will need systems, reserve power, rooms, doors, and supplies.
     */
    public ShipState(String shipName, ShipBlueprint shipBlueprint, boolean auto) {
        this(shipName, shipBlueprint.getId(), shipBlueprint.getLayoutId(), shipBlueprint.getGraphicsBaseName(), auto);
    }

    /**
     * Constructs an incomplete ShipState.
     * <p>
     * It will need systems, reserve power, rooms, doors, and supplies.
     */
    public ShipState(String shipName, String shipBlueprintId, String shipLayoutId, String shipGfxBaseName, boolean auto) {
        this.shipName = shipName;
        this.shipBlueprintId = shipBlueprintId;
        this.shipLayoutId = shipLayoutId;
        this.shipGfxBaseName = shipGfxBaseName;
        this.auto = auto;
    }

    /**
     * Assigns the missing defaults of an incomplete ship.
     * <p>
     * Based on its ShipBlueprint, the following will be set:
     * Systems, reserve power, rooms, doors, augments, and supplies.
     * <p>
     * Reserve power will be the total of all system rooms' initial 'power'
     * (aka minimum random capacity), capped by the shipBlueprint's
     * maxPower.
     */
    public void refit() {
        ShipBlueprint shipBlueprint = DataManager.get().getShip(shipBlueprintId);
        ShipLayout shipLayout = DataManager.get().getShipLayout(shipBlueprint.getLayoutId());

        // Systems.
        systemsMap.clear();
        int powerRequired = 0;
        for (SystemType systemType : SystemType.values()) {
            SystemState systemState = new SystemState(systemType);

            // Set capacity for systems that're initially present.
            SystemRoom[] systemRoom = shipBlueprint.getSystemList().getSystemRoom(systemType);
            if (systemRoom != null) {
                Boolean start = systemRoom[0].getStart();
                if (start == null || start) {
                    SystemBlueprint systemBlueprint = DataManager.get().getSystem(systemType.getId());
                    systemState.setCapacity(systemBlueprint.getStartPower());

                    // The optional room max attribute caps randomly generated ships' system capacity.
                    if (systemRoom[0].getMaxPower() != null) {
                        systemState.setCapacity(systemRoom[0].getMaxPower());
                    }

                    if (systemType.isSubsystem()) {
                        // Give subsystems all the power they want.
                        systemState.setPower(systemState.getCapacity());
                    } else {
                        // The room power attribute is for initial system power usage (or minimum if for randomly generated ships).
                        powerRequired += systemRoom[0].getPower();
                    }
                }
            }
            addSystem(systemState);
        }
        if (powerRequired > shipBlueprint.getMaxPower().amount) {
            powerRequired = shipBlueprint.getMaxPower().amount;
        }
        setReservePowerCapacity(powerRequired);

        // Rooms.
        getRoomList().clear();
        for (int r = 0; r < shipLayout.getRoomCount(); r++) {
            ShipLayoutRoom layoutRoom = shipLayout.getRoom(r);
            int squaresH = layoutRoom.squaresH;
            int squaresV = layoutRoom.squaresV;

            RoomState roomState = new RoomState();
            for (int s = 0; s < squaresH * squaresV; s++) {
                roomState.addSquare(new SquareState(0, 0, -1));
            }
            addRoom(roomState);
        }

        // Doors.
        getDoorMap().clear();
        Map<DoorCoordinate, ShipLayoutDoor> layoutDoorMap = shipLayout.getDoorMap();
        for (Map.Entry<DoorCoordinate, ShipLayoutDoor> entry : layoutDoorMap.entrySet()) {
            DoorCoordinate doorCoord = entry.getKey();

            setDoor(doorCoord.x, doorCoord.y, doorCoord.v, new DoorState());
        }

        // Augments.
        getAugmentIdList().clear();
        if (shipBlueprint.getAugmentIds() != null) {
            for (AugmentId augId : shipBlueprint.getAugmentIds()) {
                addAugmentId(augId.getName());
            }
        }

        // Supplies.
        setHullAmt(shipBlueprint.getHealth().amount);
        setFuelAmt(20);
        setDronePartsAmt(0);
        setMissilesAmt(0);
        if (shipBlueprint.getDroneList() != null) {
            setDronePartsAmt(shipBlueprint.getDroneList().drones);
        }
        if (shipBlueprint.getWeaponList() != null) {
            setMissilesAmt(shipBlueprint.getWeaponList().getMissiles());
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     * <p>
     * This should be called when turning a nearby ship into a player ship.
     * <p>
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {
        setHostile(false);
        setJumpChargeTicks(0);
        setJumping(false);
        setJumpAnimTicks(0);

        for (CrewState crew : getCrewList()) {
            crew.commandeer();
        }

        for (Map.Entry<SystemType, List<SystemState>> entry : getSystemsMap().entrySet()) {
            for (SystemState s : entry.getValue()) {
                s.commandeer();
            }
        }

        for (ExtendedSystemInfo info : getExtendedSystemInfoList()) {
            info.commandeer();
        }

        for (DoorState door : getDoorMap().values()) {
            door.commandeer();
        }

        setCloakAnimTicks(0);
        getLockdownCrystalList().clear();

        for (WeaponState weapon : getWeaponList()) {
            weapon.commandeer();
        }

        for (DroneState drone : getDroneList()) {
            drone.commandeer();
        }

        getStandaloneDroneList().clear();
    }

    public void setShipName(String s) {
        shipName = s;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipBlueprintId(String shipBlueprintId) {
        this.shipBlueprintId = shipBlueprintId;
    }

    public String getShipBlueprintId() {
        return shipBlueprintId;
    }

    public void setShipLayoutId(String shipLayoutId) {
        this.shipLayoutId = shipLayoutId;
    }

    public String getShipLayoutId() {
        return shipLayoutId;
    }

    /**
     * Sets the basename to use when loading ship images.
     * <p>
     * It often resembles the layout id, but they're not interchangeable.
     * The proper shipLayoutId comes from the ShipBlueprint.
     * <p>
     * FTL 1.01-1.03.3 used the following path for all ships.
     * FTL 1.5.4 used this path for player ships only.
     * "img/ship/{shipGfxBaseName}_base.png"
     * <p>
     * FTL 1.5.4 used the following path for enemy ships only.
     * "img/ships_glow/{shipGfxBaseName}_base.png"
     * <p>
     * The basename is combined with suffixes:
     * _base = Background hull layer.
     * _cloak = Foreground hull layer when cloaked.
     * _floor = Decorative floorplan outline layer.
     * _gib[0-9] = Post-destruction hull fragments.
     * <p>
     * The "_floor" image is displayed below the programmatically painted
     * grid of squares. It is typically only present for player ships.
     * <p>
     * Observed values: jelly_croissant_pirate, rebel_long_pirate.
     */
    public void setShipGraphicsBaseName(String shipGfxBaseName) {
        this.shipGfxBaseName = shipGfxBaseName;
    }

    public String getShipGraphicsBaseName() {
        return shipGfxBaseName;
    }

    /**
     * Sets whether this is a randomized NPC ship or player ship.
     */
    public void setAuto(boolean b) {
        auto = b;
    }

    public boolean isAuto() {
        return auto;
    }

    /**
     * Toggles whether this ship is hostile or neutral.
     * <p>
     * <p>
     * Neutral ships hide their floorplans unless a player-controlled crew
     * is on board. They also don't attack, of course.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setHostile(boolean b) {
        hostile = b;
    }

    public boolean isHostile() {
        return hostile;
    }

    /**
     * Sets time elapsed while waiting for the FTL drive to charge.
     * <p>
     * This counts to 85000.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setJumpChargeTicks(int n) {
        jumpChargeTicks = n;
    }

    public int getJumpChargeTicks() {
        return jumpChargeTicks;
    }

    /**
     * Toggles whether this ship is currently jumping away.
     * <p>
     * If true, this ship will fade out immediately upon loading. If
     * paused, the animation will play anyway, but the ship will still be
     * present, albeit invisible. Once unpaused, an event popup will
     * appear in response to the ship's departure.
     * <p>
     * This value is ignored on player ships.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setJumpAnimTicks(int)
     */
    public void setJumping(boolean b) {
        jumping = b;
    }

    public boolean isJumping() {
        return jumping;
    }

    /**
     * Sets time elapsed while jumping away.
     * <p>
     * This counts from 0 (normal) to 2000 (gone).
     * <p>
     * This value is ignored on player ships - and on nearby ships which
     * aren't currently jumping.
     * <p>
     * This was introduced in FTL 1.5.4.
     *
     * @see #setJumping(boolean)
     */
    public void setJumpAnimTicks(int n) {
        jumpAnimTicks = n;
    }

    public int getJumpAnimTicks() {
        return jumpAnimTicks;
    }

    public void setHullAmt(int n) {
        hullAmt = n;
    }

    public void setFuelAmt(int n) {
        fuelAmt = n;
    }

    public void setDronePartsAmt(int n) {
        dronePartsAmt = n;
    }

    public void setMissilesAmt(int n) {
        missilesAmt = n;
    }

    public void setScrapAmt(int n) {
        scrapAmt = n;
    }

    public int getHullAmt() {
        return hullAmt;
    }

    public int getFuelAmt() {
        return fuelAmt;
    }

    public int getDronePartsAmt() {
        return dronePartsAmt;
    }

    public int getMissilesAmt() {
        return missilesAmt;
    }

    public int getScrapAmt() {
        return scrapAmt;
    }

    /**
     * Adds an entry to the list of starting crew.
     * <p>
     * When the player flees a nearby ship and later revisits, the
     * starting crew are respawned. This list has no apparent effect on the
     * player ship, except to memorialize the crew decided in the hangar at
     * the start of the campaign.
     */
    public void addStartingCrewMember(StartingCrewState sc) {
        startingCrewList.add(sc);
    }

    public List<StartingCrewState> getStartingCrewList() {
        return startingCrewList;
    }

    public void addCrewMember(CrewState c) {
        crewList.add(c);
    }

    public CrewState getCrewMember(int n) {
        return crewList.get(n);
    }

    public List<CrewState> getCrewList() {
        return crewList;
    }


    /**
     * Sets the reserve power capacity, which systems draw upon.
     * <p>
     * Unlike SystemStates, there is no explicit field for temporary limits.
     * A hack-disrupted battery will cause a temporary loss of 2. And an
     * event, whose xml includes an "environment" tag with the "type=storm"
     * attribute, causes a temporary divisor of 2.
     *
     * @see SavedGameState#setSectorLayoutSeed(int)
     */
    public void setReservePowerCapacity(int n) {
        reservePowerCapacity = n;
    }

    public int getReservePowerCapacity() {
        return reservePowerCapacity;
    }

    public void addSystem(SystemState s) {
        List<SystemState> systemList = systemsMap.get(s.getSystemType());
        if (systemList == null) {
            systemList = new ArrayList<>(0);
            systemsMap.put(s.getSystemType(), systemList);
        }
        systemList.add(s);
    }

    /**
     * Returns the first SystemState of a given type, or null.
     */
    public SystemState getSystem(SystemType systemType) {
        List<SystemState> systemList = systemsMap.get(systemType);

        if (systemList != null && !systemList.isEmpty()) {
            return systemList.get(0);
        }

        return null;
    }

    /**
     * Returns a list of all SystemStates of a given type.
     * <p>
     * If no SystemStates are present, an empty list is returned.
     * That same list object will later contain systems if any are added.
     */
    public List<SystemState> getSystems(SystemType systemType) {
        List<SystemState> systemList = systemsMap.get(systemType);
        if (systemList == null) {
            systemList = new ArrayList<>(0);
            systemsMap.put(systemType, systemList);
        }

        return systemList;
    }

    public Map<SystemType, List<SystemState>> getSystemsMap() {
        return systemsMap;
    }


    public void addExtendedSystemInfo(ExtendedSystemInfo info) {
        extendedSystemInfoList.add(info);
    }

    public void setExtendedSystemInfoList(List<ExtendedSystemInfo> extendedSystemInfoList) {
        this.extendedSystemInfoList = extendedSystemInfoList;
    }

    public List<ExtendedSystemInfo> getExtendedSystemInfoList() {
        return extendedSystemInfoList;
    }

    public <T extends ExtendedSystemInfo> List<T> getExtendedSystemInfoList(Class<T> infoClass) {
        List<T> result = new ArrayList<>(1);
        for (ExtendedSystemInfo info : extendedSystemInfoList) {
            if (infoClass.isInstance(info)) {
                result.add(infoClass.cast(info));
            }
        }
        return result;
    }

    /**
     * Returns the first extended system info of a given class, or null.
     */
    public <T extends ExtendedSystemInfo> T getExtendedSystemInfo(Class<T> infoClass) {
        T result = null;
        for (ExtendedSystemInfo info : extendedSystemInfoList) {
            if (infoClass.isInstance(info)) {
                result = infoClass.cast(info);
                break;
            }
        }
        return result;
    }


    public void addRoom(RoomState r) {
        roomList.add(r);
    }

    public RoomState getRoom(int roomId) {
        return roomList.get(roomId);
    }

    public List<RoomState> getRoomList() {
        return roomList;
    }


    /**
     * Adds a hull breach.
     *
     * @param x            the 0-based Nth floor-square from the left (plus ShipLayout X_OFFSET)
     * @param y            the 0-based Nth floor-square from the top (plus ShipLayout Y_OFFSET)
     * @param breachHealth 0-100.
     */
    public void setBreach(int x, int y, int breachHealth) {
        breachMap.put(new XYPair(x, y), breachHealth);
    }

    public Map<XYPair, Integer> getBreachMap() {
        return breachMap;
    }


    /**
     * Adds a door.
     *
     * @param wallX    the 0-based Nth wall from the left
     * @param wallY    the 0-based Nth wall from the top
     * @param vertical 1 for vertical wall coords, 0 for horizontal
     * @param d        a DoorState
     * @see net.blerf.ftl.model.shiplayout.ShipLayout
     */
    public void setDoor(int wallX, int wallY, int vertical, DoorState d) {
        DoorCoordinate doorCoord = new DoorCoordinate(wallX, wallY, vertical);
        doorMap.put(doorCoord, d);
    }

    public DoorState getDoor(int wallX, int wallY, int vertical) {
        DoorCoordinate doorCoord = new DoorCoordinate(wallX, wallY, vertical);
        return doorMap.get(doorCoord);
    }

    /**
     * Returns the map containing this ship's door states.
     * <p>
     * Do not rely on the keys' order. ShipLayout config files have a
     * different order than saved game files.Entries will be in whatever
     * order setDoor was called, which generally will be in the saved game
     * file's order.
     */
    public Map<DoorCoordinate, DoorState> getDoorMap() {
        return doorMap;
    }


    /**
     * Sets visibility of the ship's cloak image.
     * <p>
     * This counts from 0 (uncloaked) to 500 (cloaked) and back.
     * <p>
     * Presumably, this is stored separately from CloakingInfo in case the
     * Cloaking system is uninstalled while still cloaked!?
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void setCloakAnimTicks(int n) {
        cloakAnimTicks = n;
    }

    public int getCloakAnimTicks() {
        return cloakAnimTicks;
    }


    /**
     * Sets deployed lockdown crystals.
     * <p>
     * This was introduced in FTL 1.5.12.
     */
    public void setLockdownCrystalList(List<LockdownCrystal> crystalList) {
        lockdownCrystalList = crystalList;
    }

    public List<LockdownCrystal> getLockdownCrystalList() {
        return lockdownCrystalList;
    }


    public void addWeapon(WeaponState w) {
        weaponList.add(w);
    }

    public List<WeaponState> getWeaponList() {
        return weaponList;
    }


    public void addDrone(DroneState d) {
        droneList.add(d);
    }

    public List<DroneState> getDroneList() {
        return droneList;
    }


    public void addAugmentId(String augmentId) {
        augmentIdList.add(augmentId);
    }

    public List<String> getAugmentIdList() {
        return augmentIdList;
    }


    /**
     * Adds a standalone surge drone.
     * <p>
     * TODO: See what happens when standalone drones are added to ships that
     * aren't rebel flagships.
     * <p>
     * This was introduced in FTL 1.5.4.
     */
    public void addStandaloneDrone(StandaloneDroneState standaloneDrone) {
        standaloneDroneList.add(standaloneDrone);
    }

    public List<StandaloneDroneState> getStandaloneDroneList() {
        return standaloneDroneList;
    }


    @Override
    public String toString() {
        // The blueprint fetching might vary if auto == true.
        // See "autoBlueprints.xml" vs "blueprints.xml".
        ShipBlueprint shipBlueprint = DataManager.get().getShip(shipBlueprintId);
        SystemList blueprintSystems = shipBlueprint.getSystemList();

        ShipLayout shipLayout = DataManager.get().getShipLayout(shipLayoutId);
        if (shipLayout == null)
            throw new RuntimeException(String.format("Could not find layout for%s ship: %s", (auto ? " auto" : ""), shipName));

        StringBuilder result = new StringBuilder();
        boolean first = true;
        result.append(String.format("Ship Name:    %s%n", shipName));
        result.append(String.format("Ship Type:    %s%n", shipBlueprintId));
        result.append(String.format("Ship Layout:  %s%n", shipLayoutId));
        result.append(String.format("Gfx BaseName: %s%n", shipGfxBaseName));

        result.append("\nSupplies...\n");
        result.append(String.format("Hull:        %3d%n", hullAmt));
        result.append(String.format("Fuel:        %3d%n", fuelAmt));
        result.append(String.format("Drone Parts: %3d%n", dronePartsAmt));
        result.append(String.format("Missiles:    %3d%n", missilesAmt));
        result.append(String.format("Scrap:       %3d%n", scrapAmt));
        result.append("\n");
        result.append(String.format("Hostile:           %7b%n", hostile));
        result.append(String.format("Jump Charge Ticks: %7d (85000 is fully charged)%n", jumpChargeTicks));
        result.append(String.format("Jumping:           %7b%n", jumping));
        result.append(String.format("Jump Anim Ticks:   %7d (0=Normal to 2000=Gone)%n", jumpAnimTicks));

        result.append("\nStarting Crew...\n");
        first = true;
        for (StartingCrewState sc : startingCrewList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(sc.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nCurrent Crew...\n");
        first = true;
        for (CrewState c : crewList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(c.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nSystems...\n");
        result.append(String.format("  Reserve Power Capacity: %2d%n", reservePowerCapacity));
        result.append("\n");
        first = true;
        for (Map.Entry<SystemType, List<SystemState>> entry : systemsMap.entrySet()) {
            for (SystemState s : entry.getValue()) {
                if (first) {
                    first = false;
                } else {
                    result.append(",\n");
                }
                result.append(s.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
            }
        }

        result.append("\nExtended System Info...\n");
        first = true;
        for (ExtendedSystemInfo info : extendedSystemInfoList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(info.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nRooms...\n");
        first = true;
        for (ListIterator<RoomState> it = roomList.listIterator(); it.hasNext(); ) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            int roomId = it.nextIndex();

            SystemType systemType = blueprintSystems.getSystemTypeByRoomId(roomId);
            String systemId = (systemType != null) ? systemType.getId() : "empty";

            result.append(String.format("Room Id: %2d (%s)%n", roomId, systemId));
            result.append(it.next().toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nHull Breaches...\n");
        int breachId = -1;
        first = true;
        for (Map.Entry<XYPair, Integer> entry : breachMap.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }

            XYPair breachCoord = entry.getKey();
            int breachHealth = entry.getValue();

            result.append(String.format("BreachId: %2d, Raw Coords: %2d,%2d (-Layout Offset: %2d,%2d)%n", ++breachId, breachCoord.x, breachCoord.y, breachCoord.x - shipLayout.getOffsetX(), breachCoord.y - shipLayout.getOffsetY()));
            result.append(String.format("  Breach HP: %3d%n", breachHealth));
        }

        result.append("\nDoors...\n");
        int doorId = -1;
        first = true;
        for (Map.Entry<DoorCoordinate, DoorState> entry : doorMap.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }

            DoorCoordinate doorCoord = entry.getKey();
            DoorState d = entry.getValue();
            String orientation = (doorCoord.v == 1) ? "V" : "H";

            result.append(String.format("DoorId: %2d (%2d,%2d,%2s)%n", ++doorId, doorCoord.x, doorCoord.y, orientation));
            result.append(d.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append(String.format("%nCloak Anim Ticks:  %3d (0=Uncloaked to 500=Cloaked)%n", cloakAnimTicks));

        result.append("\nLockdown Crystals...\n");
        first = true;
        for (LockdownCrystal c : lockdownCrystalList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(c.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nWeapons...\n");
        first = true;
        for (WeaponState w : weaponList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(w.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nDrones...\n");
        first = true;
        for (DroneState d : droneList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(d.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }


        result.append("\nStandalone Drones... (Surge)\n");
        int standaloneDroneIndex = 0;
        first = true;
        for (StandaloneDroneState standaloneDrone : standaloneDroneList) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(String.format("Surge Drone # %2d:%n", standaloneDroneIndex++));
            result.append(standaloneDrone.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        result.append("\nAugments...\n");
        for (String augmentId : augmentIdList) {
            result.append(String.format("AugmentId: %s%n", augmentId));
        }

        return result.toString();
    }
}
