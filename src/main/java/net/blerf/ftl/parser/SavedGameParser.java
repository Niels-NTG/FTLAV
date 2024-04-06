// Variables with unknown meanings are named with greek letters.
// Classes for unknown objects are named after deities.
// http://en.wikipedia.org/wiki/List_of_Greek_mythological_figures#Personified_concepts

// For reference on weapons and projectiles, see the "Complete Weapon Attribute Table":
// https://subsetgames.com/forum/viewtopic.php?f=12&t=24600


package net.blerf.ftl.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.constants.FleetPresence;
import net.blerf.ftl.constants.HazardVulnerability;
import net.blerf.ftl.model.XYPair;
import net.blerf.ftl.model.pod.BoarderDronePodInfo;
import net.blerf.ftl.model.pod.DefenseDronePodInfo;
import net.blerf.ftl.model.pod.ExtendedDronePodInfo;
import net.blerf.ftl.model.pod.HackingDronePodInfo;
import net.blerf.ftl.model.pod.IntegerDronePodInfo;
import net.blerf.ftl.model.pod.ShieldDronePodInfo;
import net.blerf.ftl.model.pod.ZigZagDronePodInfo;
import net.blerf.ftl.model.projectileinfo.BeamProjectileInfo;
import net.blerf.ftl.model.projectileinfo.BombProjectileInfo;
import net.blerf.ftl.model.projectileinfo.EmptyProjectileInfo;
import net.blerf.ftl.model.projectileinfo.ExtendedProjectileInfo;
import net.blerf.ftl.model.projectileinfo.IntegerProjectileInfo;
import net.blerf.ftl.model.projectileinfo.LaserProjectileInfo;
import net.blerf.ftl.model.projectileinfo.PDSProjectileInfo;
import net.blerf.ftl.model.shiplayout.DoorCoordinate;
import net.blerf.ftl.model.shiplayout.ShipLayout;
import net.blerf.ftl.model.shiplayout.ShipLayoutDoor;
import net.blerf.ftl.model.shiplayout.ShipLayoutRoom;
import net.blerf.ftl.model.state.AnimState;
import net.blerf.ftl.model.state.AsteroidFieldState;
import net.blerf.ftl.model.state.BeaconState;
import net.blerf.ftl.model.state.CrewState;
import net.blerf.ftl.model.state.DamageState;
import net.blerf.ftl.model.state.DoorState;
import net.blerf.ftl.model.state.DronePodState;
import net.blerf.ftl.model.state.DroneState;
import net.blerf.ftl.model.state.EncounterState;
import net.blerf.ftl.model.state.EnvironmentState;
import net.blerf.ftl.model.state.ExtendedDroneInfo;
import net.blerf.ftl.model.state.LockdownCrystal;
import net.blerf.ftl.model.state.NearbyShipAIState;
import net.blerf.ftl.model.state.ProjectileState;
import net.blerf.ftl.model.state.RebelFlagshipState;
import net.blerf.ftl.model.state.RoomState;
import net.blerf.ftl.model.state.SavedGameState;
import net.blerf.ftl.model.state.ShipState;
import net.blerf.ftl.model.state.SquareState;
import net.blerf.ftl.model.state.StandaloneDroneState;
import net.blerf.ftl.model.state.StartingCrewState;
import net.blerf.ftl.model.state.StoreItem;
import net.blerf.ftl.model.state.StoreShelf;
import net.blerf.ftl.model.state.StoreState;
import net.blerf.ftl.model.state.SystemState;
import net.blerf.ftl.model.state.WeaponModuleState;
import net.blerf.ftl.model.state.WeaponState;
import net.blerf.ftl.model.systeminfo.ArtilleryInfo;
import net.blerf.ftl.model.systeminfo.BatteryInfo;
import net.blerf.ftl.model.systeminfo.CloakingInfo;
import net.blerf.ftl.model.systeminfo.ClonebayInfo;
import net.blerf.ftl.model.systeminfo.HackingInfo;
import net.blerf.ftl.model.systeminfo.MindInfo;
import net.blerf.ftl.model.systeminfo.ShieldsInfo;
import net.blerf.ftl.model.type.CrewType;
import net.blerf.ftl.model.type.DroneType;
import net.blerf.ftl.model.type.ProjectileType;
import net.blerf.ftl.model.type.StationDirection;
import net.blerf.ftl.model.type.StoreItemType;
import net.blerf.ftl.model.type.SystemType;
import net.blerf.ftl.xml.DroneBlueprint;
import net.blerf.ftl.xml.ship.ShipBlueprint;
import net.blerf.ftl.xml.ship.SystemRoom;

@Slf4j
public class SavedGameParser extends Parser {

    public SavedGameParser() {
    }

    public SavedGameState readSavedGame(File savFile) throws IOException {
        try (FileInputStream in = new FileInputStream(savFile)) {
            return readSavedGame(in);
        }
    }

    public SavedGameState readSavedGame(FileInputStream in) throws IOException {
        SavedGameState gameState = new SavedGameState();

        int fileFormat = readInt(in);
        gameState.setFileFormat(fileFormat);

        // FTL 1.6.1 introduced UTF-8 strings.
        super.setUnicode(fileFormat >= 11);

        if (fileFormat == 11) {
            gameState.setRandomNative(readBool(in));
        } else {
            gameState.setRandomNative(true);  // Always native before FTL 1.6.1.
        }

        if (fileFormat == 2) {
            // FTL 1.03.3 and earlier.
            gameState.setDLCEnabled(false);  // Not present before FTL 1.5.4.
        } else if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            // FTL 1.5.4-1.5.10, 1.5.12, 1.5.13, or 1.6.1.
            gameState.setDLCEnabled(readBool(in));
        } else {
            throw new IOException(String.format("Unexpected first byte (%d) for a SAVED GAME.", fileFormat));
        }

        int diffFlag = readInt(in);
        Difficulty diff;
        if (diffFlag == 0) {
            diff = Difficulty.EASY;
        } else if (diffFlag == 1) {
            diff = Difficulty.NORMAL;
        } else if (diffFlag == 2 && (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11)) {
            diff = Difficulty.HARD;
        } else {
            throw new IOException(String.format("Unsupported difficulty flag for saved game: %d", diffFlag));
        }

        gameState.setDifficulty(diff);
        gameState.setTotalShipsDefeated(readInt(in));
        gameState.setTotalBeaconsExplored(readInt(in));
        gameState.setTotalScrapCollected(readInt(in));
        gameState.setTotalCrewHired(readInt(in));

        String playerShipName = readString(in);         // Redundant.
        gameState.setPlayerShipName(playerShipName);

        String playerShipBlueprintId = readString(in);  // Redundant.
        gameState.setPlayerShipBlueprintId(playerShipBlueprintId);

        int oneBasedSectorNumber = readInt(in);  // Redundant.

        // Always 0?
        gameState.setUnknownBeta(readInt(in));

        int stateVarCount = readInt(in);
        for (int i = 0; i < stateVarCount; i++) {
            String stateVarId = readString(in);
            Integer stateVarValue = readInt(in);
            gameState.setStateVar(stateVarId, stateVarValue);
        }

        ShipState playerShipState = readShip(in, false, fileFormat, gameState.isDLCEnabled());
        gameState.setPlayerShip(playerShipState);

        // Nearby ships have no cargo, so this isn't in readShip().
        int cargoCount = readInt(in);
        for (int i = 0; i < cargoCount; i++) {
            gameState.addCargoItemId(readString(in));
        }

        gameState.setSectorTreeSeed(readInt(in));

        gameState.setSectorLayoutSeed(readInt(in));

        gameState.setRebelFleetOffset(readInt(in));

        gameState.setRebelFleetFudge(readInt(in));

        gameState.setRebelPursuitMod(readInt(in));

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            gameState.setCurrentBeaconId(readInt(in));

            gameState.setWaiting(readBool(in));
            gameState.setWaitEventSeed(readInt(in));
            gameState.setUnknownEpsilon(readString(in));
            gameState.setSectorHazardsVisible(readBool(in));
            gameState.setRebelFlagshipVisible(readBool(in));
            gameState.setRebelFlagshipHop(readInt(in));
            gameState.setRebelFlagshipMoving(readBool(in));
            gameState.setRebelFlagshipRetreating(readBool(in));
            gameState.setRebelFlagshipBaseTurns(readInt(in));
        } else if (fileFormat == 2) {
            gameState.setSectorHazardsVisible(readBool(in));

            gameState.setRebelFlagshipVisible(readBool(in));

            gameState.setRebelFlagshipHop(readInt(in));

            gameState.setRebelFlagshipMoving(readBool(in));
        }

        int sectorVisitationCount = readInt(in);
        List<Boolean> route = new ArrayList<>();
        for (int i = 0; i < sectorVisitationCount; i++) {
            route.add(readBool(in));
        }
        gameState.setSectorVisitation(route);

        int sectorNumber = readInt(in);
        gameState.setSectorNumber(sectorNumber);

        gameState.setSectorIsHiddenCrystalWorlds(readBool(in));

        int beaconCount = readInt(in);
        for (int i = 0; i < beaconCount; i++) {
            gameState.addBeacon(readBeacon(in, fileFormat));
        }

        int questEventCount = readInt(in);
        for (int i = 0; i < questEventCount; i++) {
            String questEventId = readString(in);
            int questBeaconId = readInt(in);
            gameState.addQuestEvent(questEventId, questBeaconId);
        }

        int distantQuestEventCount = readInt(in);
        for (int i = 0; i < distantQuestEventCount; i++) {
            String distantQuestEventId = readString(in);
            gameState.addDistantQuestEvent(distantQuestEventId);
        }

        if (fileFormat == 2) {
            gameState.setCurrentBeaconId(readInt(in));

            boolean shipNearby = readBool(in);
            if (shipNearby) {
                ShipState nearbyShipState = readShip(in, true, fileFormat, gameState.isDLCEnabled());
                gameState.setNearbyShip(nearbyShipState);
            }

            RebelFlagshipState flagshipState = readRebelFlagship(in);
            gameState.setRebelFlagshipState(flagshipState);
        } else if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            // Current beaconId was set earlier.

            gameState.setUnknownMu(readInt(in));

            EncounterState encounter = readEncounter(in, fileFormat);
            gameState.setEncounter(encounter);

            boolean shipNearby = readBool(in);
            if (shipNearby) {
                gameState.setRebelFlagshipNearby(readBool(in));

                ShipState nearbyShipState = readShip(in, true, fileFormat, gameState.isDLCEnabled());
                gameState.setNearbyShip(nearbyShipState);

                gameState.setNearbyShipAI(readNearbyShipAI(in));
            }

            gameState.setEnvironment(readEnvironment(in));

            // Flagship state is set much later.

            int projectileCount = readInt(in);
            for (int i = 0; i < projectileCount; i++) {
                gameState.addProjectile(readProjectile(in, fileFormat));
            }

            readExtendedShipInfo(in, gameState.getPlayerShip(), fileFormat);

            if (gameState.getNearbyShip() != null) {
                readExtendedShipInfo(in, gameState.getNearbyShip(), fileFormat);
            }

            gameState.setUnknownNu(readInt(in));

            if (gameState.getNearbyShip() != null) {
                gameState.setUnknownXi(readInt(in));
            }

            gameState.setAutofire(readBool(in));

            RebelFlagshipState flagship = new RebelFlagshipState();

            flagship.setUnknownAlpha(readInt(in));
            flagship.setPendingStage(readInt(in));
            flagship.setUnknownGamma(readInt(in));
            flagship.setUnknownDelta(readInt(in));

            int flagshipOccupancyCount = readInt(in);
            for (int i = 0; i < flagshipOccupancyCount; i++) {
                flagship.setPreviousOccupancy(i, readInt(in));
            }

            gameState.setRebelFlagshipState(flagship);
        }

        // The stream should end here.

        int bytesRemaining = (int) (in.getChannel().size() - in.getChannel().position());
        if (bytesRemaining > 0) {
            gameState.addMysteryBytes(new MysteryBytes(in, bytesRemaining));
        }

        return gameState;
    }

    /**
     * Writes a gameState to a stream.
     * <p>
     * Any MysteryBytes will be omitted.
     */
    public void writeSavedGame(OutputStream out, SavedGameState gameState) throws IOException {

        int fileFormat = gameState.getFileFormat();
        writeInt(out, fileFormat);

        // FTL 1.6.1 introduced UTF-8 strings.
        super.setUnicode(fileFormat >= 11);

        if (fileFormat == 11) {
            writeBool(out, gameState.isRandomNative());
        }

        // TODO fix for ff==2
        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeBool(out, gameState.isDLCEnabled());
        } else {
            throw new IOException("Unsupported fileFormat: " + fileFormat);
        }

        int diffFlag = 0;
        if (gameState.getDifficulty() == Difficulty.EASY) {
            diffFlag = 0;
        } else if (gameState.getDifficulty() == Difficulty.NORMAL) {
            diffFlag = 1;
        } else if (gameState.getDifficulty() == Difficulty.HARD && (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11)) {
            diffFlag = 2;
        } else {
            log.warn("Substituting EASY for unsupported difficulty for saved game: {}", gameState.getDifficulty());
            diffFlag = 0;
        }
        writeInt(out, diffFlag);
        writeInt(out, gameState.getTotalShipsDefeated());
        writeInt(out, gameState.getTotalBeaconsExplored());
        writeInt(out, gameState.getTotalScrapCollected());
        writeInt(out, gameState.getTotalCrewHired());

        writeString(out, gameState.getPlayerShipName());
        writeString(out, gameState.getPlayerShipBlueprintId());

        // Redundant 1-based sector number.
        writeInt(out, gameState.getSectorNumber() + 1);

        writeInt(out, gameState.getUnknownBeta());

        writeInt(out, gameState.getStateVars().size());
        for (Map.Entry<String, Integer> entry : gameState.getStateVars().entrySet()) {
            writeString(out, entry.getKey());
            writeInt(out, entry.getValue());
        }

        writeShip(out, gameState.getPlayerShip(), fileFormat);

        writeInt(out, gameState.getCargoIdList().size());
        for (String cargoItemId : gameState.getCargoIdList()) {
            writeString(out, cargoItemId);
        }

        writeInt(out, gameState.getSectorTreeSeed());
        writeInt(out, gameState.getSectorLayoutSeed());
        writeInt(out, gameState.getRebelFleetOffset());
        writeInt(out, gameState.getRebelFleetFudge());
        writeInt(out, gameState.getRebelPursuitMod());

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, gameState.getCurrentBeaconId());

            writeBool(out, gameState.isWaiting());
            writeInt(out, gameState.getWaitEventSeed());
            writeString(out, gameState.getUnknownEpsilon());
            writeBool(out, gameState.areSectorHazardsVisible());
            writeBool(out, gameState.isRebelFlagshipVisible());
            writeInt(out, gameState.getRebelFlagshipHop());
            writeBool(out, gameState.isRebelFlagshipMoving());
            writeBool(out, gameState.isRebelFlagshipRetreating());
            writeInt(out, gameState.getRebelFlagshipBaseTurns());
        } else if (fileFormat == 2) {
            writeBool(out, gameState.areSectorHazardsVisible());
            writeBool(out, gameState.isRebelFlagshipVisible());
            writeInt(out, gameState.getRebelFlagshipHop());
            writeBool(out, gameState.isRebelFlagshipMoving());
        }

        writeInt(out, gameState.getSectorVisitation().size());
        for (Boolean visited : gameState.getSectorVisitation()) {
            writeBool(out, visited);
        }

        writeInt(out, gameState.getSectorNumber());
        writeBool(out, gameState.isSectorHiddenCrystalWorlds());

        writeInt(out, gameState.getBeaconList().size());
        for (BeaconState beacon : gameState.getBeaconList()) {
            writeBeacon(out, beacon, fileFormat);
        }

        writeInt(out, gameState.getQuestEventMap().size());
        for (Map.Entry<String, Integer> entry : gameState.getQuestEventMap().entrySet()) {
            writeString(out, entry.getKey());
            writeInt(out, entry.getValue());
        }

        writeInt(out, gameState.getDistantQuestEventList().size());
        for (String questEventId : gameState.getDistantQuestEventList()) {
            writeString(out, questEventId);
        }

        if (fileFormat == 2) {
            writeInt(out, gameState.getCurrentBeaconId());

            ShipState nearbyShip = gameState.getNearbyShip();
            writeBool(out, (nearbyShip != null));
            if (nearbyShip != null) {
                writeShip(out, nearbyShip, fileFormat);
            }

            writeRebelFlagship(out, gameState.getRebelFlagshipState());
        } else if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            // Current beaconId was set earlier.

            writeInt(out, gameState.getUnknownMu());

            writeEncounter(out, gameState.getEncounter(), fileFormat);

            ShipState nearbyShip = gameState.getNearbyShip();
            writeBool(out, (nearbyShip != null));
            if (nearbyShip != null) {
                writeBool(out, gameState.isRebelFlagshipNearby());

                writeShip(out, nearbyShip, fileFormat);

                writeNearbyShipAI(out, gameState.getNearbyShipAI());
            }

            writeEnvironment(out, gameState.getEnvironment());

            // Flagship state is set much later.

            writeInt(out, gameState.getProjectileList().size());
            for (ProjectileState projectile : gameState.getProjectileList()) {
                writeProjectile(out, projectile, fileFormat);
            }

            writeExtendedShipInfo(out, gameState.getPlayerShip(), fileFormat);

            if (gameState.getNearbyShip() != null) {
                writeExtendedShipInfo(out, gameState.getNearbyShip(), fileFormat);
            }

            writeInt(out, gameState.getUnknownNu());

            if (gameState.getNearbyShip() != null) {
                writeInt(out, gameState.getUnknownXi());
            }

            writeBool(out, gameState.getAutofire());

            RebelFlagshipState flagship = gameState.getRebelFlagshipState();

            writeInt(out, flagship.getUnknownAlpha());
            writeInt(out, flagship.getPendingStage());
            writeInt(out, flagship.getUnknownGamma());
            writeInt(out, flagship.getUnknownDelta());

            writeInt(out, flagship.getOccupancyMap().size());
            for (Map.Entry<Integer, Integer> entry : flagship.getOccupancyMap().entrySet()) {
                int occupantCount = entry.getValue();
                writeInt(out, occupantCount);
            }
        }
    }

    private ShipState readShip(InputStream in, boolean auto, int fileFormat, boolean dlcEnabled) throws IOException {

        String shipBlueprintId = readString(in);
        String shipName = readString(in);
        String shipGfxBaseName = readString(in);

        ShipBlueprint shipBlueprint = DataManager.get().getShip(shipBlueprintId);
        if (shipBlueprint == null) {
            throw new RuntimeException(String.format("Could not find blueprint for%s ship: %s", (auto ? " auto" : ""), shipName));
        }

        String shipLayoutId = shipBlueprint.getLayoutId();

        // Use this for room and door info later.
        ShipLayout shipLayout = DataManager.get().getShipLayout(shipLayoutId);
        if (shipLayout == null) {
            throw new RuntimeException(String.format("Could not find layout for%s ship: %s", (auto ? " auto" : ""), shipName));
        }

        ShipState shipState = new ShipState(shipName, shipBlueprintId, shipLayoutId, shipGfxBaseName, auto);

        int startingCrewCount = readInt(in);
        for (int i = 0; i < startingCrewCount; i++) {
            shipState.addStartingCrewMember(readStartingCrewMember(in));
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            shipState.setHostile(readBool(in));
            shipState.setJumpChargeTicks(readInt(in));
            shipState.setJumping(readBool(in));
            shipState.setJumpAnimTicks(readInt(in));
        }

        shipState.setHullAmt(readInt(in));
        shipState.setFuelAmt(readInt(in));
        shipState.setDronePartsAmt(readInt(in));
        shipState.setMissilesAmt(readInt(in));
        shipState.setScrapAmt(readInt(in));

        int crewCount = readInt(in);
        for (int i = 0; i < crewCount; i++) {
            shipState.addCrewMember(readCrewMember(in, fileFormat));
        }

        // System info is stored in this order.
        List<SystemType> systemTypes = new ArrayList<SystemType>();
        systemTypes.add(SystemType.SHIELDS);
        systemTypes.add(SystemType.ENGINES);
        systemTypes.add(SystemType.OXYGEN);
        systemTypes.add(SystemType.WEAPONS);
        systemTypes.add(SystemType.DRONE_CTRL);
        systemTypes.add(SystemType.MEDBAY);
        systemTypes.add(SystemType.PILOT);
        systemTypes.add(SystemType.SENSORS);
        systemTypes.add(SystemType.DOORS);
        systemTypes.add(SystemType.TELEPORTER);
        systemTypes.add(SystemType.CLOAKING);
        systemTypes.add(SystemType.ARTILLERY);
        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            systemTypes.add(SystemType.BATTERY);
            systemTypes.add(SystemType.CLONEBAY);
            systemTypes.add(SystemType.MIND);
            systemTypes.add(SystemType.HACKING);
        }

        shipState.setReservePowerCapacity(readInt(in));
        for (SystemType systemType : systemTypes) {
            shipState.addSystem(readSystem(in, systemType, fileFormat));

            // Systems that exist in multiple rooms have additional SystemStates.
            // Example: Flagship's artillery.
            //
            // In FTL 1.01-1.03.3 the flagship wasn't a nearby ship outside of combat,
            // So this never occurred. TODO: Confirm reports that 1.5.4 allows
            // multi-room systems on regular ships and check the editor's
            // compatibility.

            SystemRoom[] rooms = shipBlueprint.getSystemList().getSystemRoom(systemType);
            if (rooms != null && rooms.length > 1) {
                for (int q = 1; q < rooms.length; q++) {
                    shipState.addSystem(readSystem(in, systemType, fileFormat));
                }
            }
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {

            SystemState tmpSystem = null;

            tmpSystem = shipState.getSystem(SystemType.CLONEBAY);
            if (tmpSystem != null && tmpSystem.getCapacity() > 0) {
                ClonebayInfo clonebayInfo = new ClonebayInfo();

                clonebayInfo.setBuildTicks(readInt(in));
                clonebayInfo.setBuildTicksGoal(readInt(in));
                clonebayInfo.setDoomTicks(readInt(in));

                shipState.addExtendedSystemInfo(clonebayInfo);
            }
            tmpSystem = shipState.getSystem(SystemType.BATTERY);
            if (tmpSystem != null && tmpSystem.getCapacity() > 0) {
                BatteryInfo batteryInfo = new BatteryInfo();

                batteryInfo.setActive(readBool(in));
                batteryInfo.setUsedBattery(readInt(in));
                batteryInfo.setDischargeTicks(readInt(in));

                shipState.addExtendedSystemInfo(batteryInfo);
            }

            // The shields info always exists, even if the shields system doesn't.
            if (true) {
                ShieldsInfo shieldsInfo = new ShieldsInfo();

                shieldsInfo.setShieldLayers(readInt(in));
                shieldsInfo.setEnergyShieldLayers(readInt(in));
                shieldsInfo.setEnergyShieldMax(readInt(in));
                shieldsInfo.setShieldRechargeTicks(readInt(in));

                shieldsInfo.setShieldDropAnimOn(readBool(in));
                shieldsInfo.setShieldDropAnimTicks(readInt(in));    // TODO: Confirm.

                shieldsInfo.setShieldRaiseAnimOn(readBool(in));
                shieldsInfo.setShieldRaiseAnimTicks(readInt(in));   // TODO: Confirm.

                shieldsInfo.setEnergyShieldAnimOn(readBool(in));
                shieldsInfo.setEnergyShieldAnimTicks(readInt(in));  // TODO: Confirm.

                // A pair. Usually noise. Sometimes 0.
                shieldsInfo.setUnknownLambda(readInt(in));   // TODO: Confirm: Shield down point X.
                shieldsInfo.setUnknownMu(readInt(in));       // TODO: Confirm: Shield down point Y.

                shipState.addExtendedSystemInfo(shieldsInfo);
            }

            tmpSystem = shipState.getSystem(SystemType.CLOAKING);
            if (tmpSystem != null && tmpSystem.getCapacity() > 0) {
                CloakingInfo cloakingInfo = new CloakingInfo();

                cloakingInfo.setUnknownAlpha(readInt(in));
                cloakingInfo.setUnknownBeta(readInt(in));
                cloakingInfo.setCloakTicksGoal(readInt(in));
                cloakingInfo.setCloakTicks(readMinMaxedInt(in));

                shipState.addExtendedSystemInfo(cloakingInfo);
            }

            // Other ExtendedSystemInfo may be added to the ship later (FTL 1.5.4+).
        }

        // Room states are stored in roomId order.
        int roomCount = shipLayout.getRoomCount();
        for (int r = 0; r < roomCount; r++) {
            ShipLayoutRoom layoutRoom = shipLayout.getRoom(r);

            shipState.addRoom(readRoom(in, layoutRoom.squaresH, layoutRoom.squaresV, fileFormat));
        }

        int breachCount = readInt(in);
        for (int i = 0; i < breachCount; i++) {
            shipState.setBreach(readInt(in), readInt(in), readInt(in));
        }

        // Doors are defined in the layout text file, but their order is
        // different at runtime. Vacuum-adjacent doors are plucked out and
        // moved to the end... for some reason.
        Map<DoorCoordinate, ShipLayoutDoor> vacuumDoorMap = new LinkedHashMap<DoorCoordinate, ShipLayoutDoor>();
        Map<DoorCoordinate, ShipLayoutDoor> layoutDoorMap = shipLayout.getDoorMap();
        for (Map.Entry<DoorCoordinate, ShipLayoutDoor> entry : layoutDoorMap.entrySet()) {
            DoorCoordinate doorCoord = entry.getKey();
            ShipLayoutDoor layoutDoor = entry.getValue();

            if (layoutDoor.roomIdA == -1 || layoutDoor.roomIdB == -1) {
                vacuumDoorMap.put(doorCoord, layoutDoor);
                continue;
            }
            shipState.setDoor(doorCoord.x, doorCoord.y, doorCoord.v, readDoor(in, fileFormat));
        }
        for (Map.Entry<DoorCoordinate, ShipLayoutDoor> entry : vacuumDoorMap.entrySet()) {
            DoorCoordinate doorCoord = entry.getKey();

            shipState.setDoor(doorCoord.x, doorCoord.y, doorCoord.v, readDoor(in, fileFormat));
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            shipState.setCloakAnimTicks(readInt(in));
        }

        if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            int crystalCount = readInt(in);
            List<LockdownCrystal> crystalList = new ArrayList<LockdownCrystal>();
            for (int i = 0; i < crystalCount; i++) {
                crystalList.add(readLockdownCrystal(in));
            }
            shipState.setLockdownCrystalList(crystalList);
        }

        int weaponCount = readInt(in);
        for (int i = 0; i < weaponCount; i++) {
            WeaponState weapon = new WeaponState();
            weapon.setWeaponId(readString(in));
            weapon.setArmed(readBool(in));

            if (fileFormat == 2) {  // No longer used as of FTL 1.5.4.
                weapon.setCooldownTicks(readInt(in));
            }

            shipState.addWeapon(weapon);
        }
        // WeaponStates may have WeaponModules set on them later (FTL 1.5.4+).

        int droneCount = readInt(in);
        for (int i = 0; i < droneCount; i++) {
            shipState.addDrone(readDrone(in));
        }
        // DroneStates may have ExtendedDroneInfo set on them later (FTL 1.5.4+).

        int augmentCount = readInt(in);
        for (int i = 0; i < augmentCount; i++) {
            shipState.addAugmentId(readString(in));
        }

        // Standalone drones may be added to the ship later (FTL 1.5.4+).

        return shipState;
    }

    public void writeShip(OutputStream out, ShipState shipState, int fileFormat) throws IOException {
        String shipBlueprintId = shipState.getShipBlueprintId();

        ShipBlueprint shipBlueprint = DataManager.get().getShip(shipBlueprintId);
        if (shipBlueprint == null)
            throw new RuntimeException(String.format("Could not find blueprint for%s ship: %s", (shipState.isAuto() ? " auto" : ""), shipState.getShipName()));

        String shipLayoutId = shipBlueprint.getLayoutId();

        ShipLayout shipLayout = DataManager.get().getShipLayout(shipLayoutId);
        if (shipLayout == null)
            throw new RuntimeException(String.format("Could not find layout for%s ship: %s", (shipState.isAuto() ? " auto" : ""), shipState.getShipName()));


        writeString(out, shipBlueprintId);
        writeString(out, shipState.getShipName());
        writeString(out, shipState.getShipGraphicsBaseName());

        writeInt(out, shipState.getStartingCrewList().size());
        for (StartingCrewState startingCrew : shipState.getStartingCrewList()) {
            writeStartingCrewMember(out, startingCrew);
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeBool(out, shipState.isHostile());
            writeInt(out, shipState.getJumpChargeTicks());
            writeBool(out, shipState.isJumping());
            writeInt(out, shipState.getJumpAnimTicks());
        }

        writeInt(out, shipState.getHullAmt());
        writeInt(out, shipState.getFuelAmt());
        writeInt(out, shipState.getDronePartsAmt());
        writeInt(out, shipState.getMissilesAmt());
        writeInt(out, shipState.getScrapAmt());

        writeInt(out, shipState.getCrewList().size());
        for (CrewState crew : shipState.getCrewList()) {
            writeCrewMember(out, crew, fileFormat);
        }

        // System info is stored in this order.
        List<SystemType> systemTypes = new ArrayList<SystemType>();
        systemTypes.add(SystemType.SHIELDS);
        systemTypes.add(SystemType.ENGINES);
        systemTypes.add(SystemType.OXYGEN);
        systemTypes.add(SystemType.WEAPONS);
        systemTypes.add(SystemType.DRONE_CTRL);
        systemTypes.add(SystemType.MEDBAY);
        systemTypes.add(SystemType.PILOT);
        systemTypes.add(SystemType.SENSORS);
        systemTypes.add(SystemType.DOORS);
        systemTypes.add(SystemType.TELEPORTER);
        systemTypes.add(SystemType.CLOAKING);
        systemTypes.add(SystemType.ARTILLERY);
        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            systemTypes.add(SystemType.BATTERY);
            systemTypes.add(SystemType.CLONEBAY);
            systemTypes.add(SystemType.MIND);
            systemTypes.add(SystemType.HACKING);
        }

        writeInt(out, shipState.getReservePowerCapacity());

        for (SystemType systemType : systemTypes) {
            List<SystemState> systemList = shipState.getSystems(systemType);
            if (systemList.size() > 0) {
                for (SystemState systemState : systemList) {
                    writeSystem(out, systemState, fileFormat);
                }
            } else {
                writeInt(out, 0);  // Equivalent to constructing and writing a 0-capacity system.
            }
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {

            SystemState clonebayState = shipState.getSystem(SystemType.CLONEBAY);
            if (clonebayState != null && clonebayState.getCapacity() > 0) {
                ClonebayInfo clonebayInfo = shipState.getExtendedSystemInfo(ClonebayInfo.class);
                // This should not be null.
                writeInt(out, clonebayInfo.getBuildTicks());
                writeInt(out, clonebayInfo.getBuildTicksGoal());
                writeInt(out, clonebayInfo.getDoomTicks());
            }

            SystemState batteryState = shipState.getSystem(SystemType.BATTERY);
            if (batteryState != null && batteryState.getCapacity() > 0) {
                BatteryInfo batteryInfo = shipState.getExtendedSystemInfo(BatteryInfo.class);
                // This should not be null.
                writeBool(out, batteryInfo.isActive());
                writeInt(out, batteryInfo.getUsedBattery());
                writeInt(out, batteryInfo.getDischargeTicks());
            }

            if (true) {
                ShieldsInfo shieldsInfo = shipState.getExtendedSystemInfo(ShieldsInfo.class);
                // This should not be null.
                writeInt(out, shieldsInfo.getShieldLayers());
                writeInt(out, shieldsInfo.getEnergyShieldLayers());
                writeInt(out, shieldsInfo.getEnergyShieldMax());
                writeInt(out, shieldsInfo.getShieldRechargeTicks());

                writeBool(out, shieldsInfo.isShieldDropAnimOn());
                writeInt(out, shieldsInfo.getShieldDropAnimTicks());

                writeBool(out, shieldsInfo.isShieldRaiseAnimOn());
                writeInt(out, shieldsInfo.getShieldRaiseAnimTicks());

                writeBool(out, shieldsInfo.isEnergyShieldAnimOn());
                writeInt(out, shieldsInfo.getEnergyShieldAnimTicks());

                writeInt(out, shieldsInfo.getUnknownLambda());
                writeInt(out, shieldsInfo.getUnknownMu());
            }

            SystemState cloakingState = shipState.getSystem(SystemType.CLOAKING);
            if (cloakingState != null && cloakingState.getCapacity() > 0) {
                CloakingInfo cloakingInfo = shipState.getExtendedSystemInfo(CloakingInfo.class);
                if (cloakingInfo == null) {
                    cloakingInfo = new CloakingInfo();
                }
                // This should not be null.
                writeInt(out, cloakingInfo.getUnknownAlpha());
                writeInt(out, cloakingInfo.getUnknownBeta());
                writeInt(out, cloakingInfo.getCloakTicksGoal());

                writeMinMaxedInt(out, cloakingInfo.getCloakTicks());
            }
        }

        int roomCount = shipLayout.getRoomCount();
        for (int r = 0; r < roomCount; r++) {
            ShipLayoutRoom layoutRoom = shipLayout.getRoom(r);

            RoomState room = shipState.getRoom(r);
            writeRoom(out, room, layoutRoom.squaresH, layoutRoom.squaresV, fileFormat);
        }

        writeInt(out, shipState.getBreachMap().size());
        for (Map.Entry<XYPair, Integer> entry : shipState.getBreachMap().entrySet()) {
            writeInt(out, entry.getKey().x);
            writeInt(out, entry.getKey().y);
            writeInt(out, entry.getValue());
        }

        // Doors are defined in the layout text file, but their
        // order is different at runtime. Vacuum-adjacent doors
        // are plucked out and moved to the end... for some
        // reason.
        Map<DoorCoordinate, DoorState> shipDoorMap = shipState.getDoorMap();
        Map<DoorCoordinate, ShipLayoutDoor> vacuumDoorMap = new LinkedHashMap<DoorCoordinate, ShipLayoutDoor>();
        Map<DoorCoordinate, ShipLayoutDoor> layoutDoorMap = shipLayout.getDoorMap();
        for (Map.Entry<DoorCoordinate, ShipLayoutDoor> entry : layoutDoorMap.entrySet()) {
            DoorCoordinate doorCoord = entry.getKey();
            ShipLayoutDoor layoutDoor = entry.getValue();

            if (layoutDoor.roomIdA == -1 || layoutDoor.roomIdB == -1) {
                vacuumDoorMap.put(doorCoord, layoutDoor);
                continue;
            }
            writeDoor(out, shipDoorMap.get(doorCoord), fileFormat);
        }
        for (Map.Entry<DoorCoordinate, ShipLayoutDoor> entry : vacuumDoorMap.entrySet()) {
            DoorCoordinate doorCoord = entry.getKey();

            writeDoor(out, shipDoorMap.get(doorCoord), fileFormat);
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, shipState.getCloakAnimTicks());
        }

        if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, shipState.getLockdownCrystalList().size());
            for (LockdownCrystal crystal : shipState.getLockdownCrystalList()) {
                writeLockdownCrystal(out, crystal);
            }
        }

        writeInt(out, shipState.getWeaponList().size());
        for (WeaponState weapon : shipState.getWeaponList()) {
            writeString(out, weapon.getWeaponId());
            writeBool(out, weapon.isArmed());

            if (fileFormat == 2) {  // No longer used as of FTL 1.5.4.
                writeInt(out, weapon.getCooldownTicks());
            }
        }

        writeInt(out, shipState.getDroneList().size());
        for (DroneState drone : shipState.getDroneList()) {
            writeDrone(out, drone);
        }

        writeInt(out, shipState.getAugmentIdList().size());
        for (String augmentId : shipState.getAugmentIdList()) {
            writeString(out, augmentId);
        }
    }

    private StartingCrewState readStartingCrewMember(InputStream in) throws IOException {
        StartingCrewState startingCrew = new StartingCrewState();

        String raceString = readString(in);
        CrewType race = CrewType.findById(raceString);
        if (race != null) {
            startingCrew.setRace(race);
        } else {
            throw new IOException("Unsupported starting crew race: " + raceString);
        }

        startingCrew.setName(readString(in));

        return startingCrew;
    }

    public void writeStartingCrewMember(OutputStream out, StartingCrewState startingCrew) throws IOException {
        writeString(out, startingCrew.getRace().getId());
        writeString(out, startingCrew.getName());
    }

    private CrewState readCrewMember(InputStream in, int fileFormat) throws IOException {
        CrewState crew = new CrewState();
        crew.setName(readString(in));

        String raceString = readString(in);
        CrewType race = CrewType.findById(raceString);
        if (race != null) {
            crew.setRace(race);
        } else {
            throw new IOException("Unsupported crew race: " + raceString);
        }

        crew.setEnemyBoardingDrone(readBool(in));
        crew.setHealth(readInt(in));
        crew.setSpriteX(readInt(in));
        crew.setSpriteY(readInt(in));
        crew.setRoomId(readInt(in));
        crew.setRoomSquare(readInt(in));
        crew.setPlayerControlled(readBool(in));

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            crew.setCloneReady(readInt(in));

            int deathOrder = readInt(in);  // Redundant. Exactly the same as Clonebay Priority.

            int tintCount = readInt(in);
            List<Integer> spriteTintIndeces = new ArrayList<Integer>();
            for (int i = 0; i < tintCount; i++) {
                spriteTintIndeces.add(readInt(in));
            }
            crew.setSpriteTintIndices(spriteTintIndeces);

            crew.setMindControlled(readBool(in));
            crew.setSavedRoomSquare(readInt(in));
            crew.setSavedRoomId(readInt(in));
        }

        crew.setPilotSkill(readInt(in));
        crew.setEngineSkill(readInt(in));
        crew.setShieldSkill(readInt(in));
        crew.setWeaponSkill(readInt(in));
        crew.setRepairSkill(readInt(in));
        crew.setCombatSkill(readInt(in));
        crew.setMale(readBool(in));
        crew.setRepairs(readInt(in));
        crew.setCombatKills(readInt(in));
        crew.setPilotedEvasions(readInt(in));
        crew.setJumpsSurvived(readInt(in));
        crew.setSkillMasteriesEarned(readInt(in));

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            crew.setStunTicks(readInt(in));
            crew.setHealthBoost(readInt(in));
            crew.setClonebayPriority(readInt(in));
            crew.setDamageBoost(readInt(in));
            crew.setUnknownLambda(readInt(in));
            crew.setUniversalDeathCount(readInt(in));

            if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                crew.setPilotMasteryOne(readBool(in));
                crew.setPilotMasteryTwo(readBool(in));
                crew.setEngineMasteryOne(readBool(in));
                crew.setEngineMasteryTwo(readBool(in));
                crew.setShieldMasteryOne(readBool(in));
                crew.setShieldMasteryTwo(readBool(in));
                crew.setWeaponMasteryOne(readBool(in));
                crew.setWeaponMasteryTwo(readBool(in));
                crew.setRepairMasteryOne(readBool(in));
                crew.setRepairMasteryTwo(readBool(in));
                crew.setCombatMasteryOne(readBool(in));
                crew.setCombatMasteryTwo(readBool(in));
            }

            crew.setUnknownNu(readBool(in));

            crew.setTeleportAnim(readAnim(in));

            crew.setUnknownPhi(readBool(in));

            if (CrewType.CRYSTAL.equals(crew.getRace())) {
                crew.setLockdownRechargeTicks(readInt(in));
                crew.setLockdownRechargeTicksGoal(readInt(in));
                crew.setUnknownOmega(readInt(in));
            }
        }

        return crew;
    }

    public void writeCrewMember(OutputStream out, CrewState crew, int fileFormat) throws IOException {
        writeString(out, crew.getName());
        writeString(out, crew.getRace().getId());
        writeBool(out, crew.isEnemyBoardingDrone());
        writeInt(out, crew.getHealth());
        writeInt(out, crew.getSpriteX());
        writeInt(out, crew.getSpriteY());
        writeInt(out, crew.getRoomId());
        writeInt(out, crew.getRoomSquare());
        writeBool(out, crew.isPlayerControlled());

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, crew.getCloneReady());

            int deathOrder = crew.getClonebayPriority();  // Redundant.
            writeInt(out, deathOrder);

            writeInt(out, crew.getSpriteTintIndices().size());
            for (Integer tintInt : crew.getSpriteTintIndices()) {
                writeInt(out, tintInt);
            }

            writeBool(out, crew.isMindControlled());
            writeInt(out, crew.getSavedRoomSquare());
            writeInt(out, crew.getSavedRoomId());
        }

        writeInt(out, crew.getPilotSkill());
        writeInt(out, crew.getEngineSkill());
        writeInt(out, crew.getShieldSkill());
        writeInt(out, crew.getWeaponSkill());
        writeInt(out, crew.getRepairSkill());
        writeInt(out, crew.getCombatSkill());
        writeBool(out, crew.isMale());
        writeInt(out, crew.getRepairs());
        writeInt(out, crew.getCombatKills());
        writeInt(out, crew.getPilotedEvasions());
        writeInt(out, crew.getJumpsSurvived());
        writeInt(out, crew.getSkillMasteriesEarned());

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, crew.getStunTicks());
            writeInt(out, crew.getHealthBoost());
            writeInt(out, crew.getClonebayPriority());
            writeInt(out, crew.getDamageBoost());
            writeInt(out, crew.getUnknownLambda());
            writeInt(out, crew.getUniversalDeathCount());

            if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                writeBool(out, crew.isPilotMasteryOne());
                writeBool(out, crew.isPilotMasteryTwo());
                writeBool(out, crew.isEngineMasteryOne());
                writeBool(out, crew.isEngineMasteryTwo());
                writeBool(out, crew.isShieldMasteryOne());
                writeBool(out, crew.isShieldMasteryTwo());
                writeBool(out, crew.isWeaponMasteryOne());
                writeBool(out, crew.isWeaponMasteryTwo());
                writeBool(out, crew.isRepairMasteryOne());
                writeBool(out, crew.isRepairMasteryTwo());
                writeBool(out, crew.isCombatMasteryOne());
                writeBool(out, crew.isCombatMasteryTwo());
            }

            writeBool(out, crew.isUnknownNu());

            writeAnim(out, crew.getTeleportAnim());

            writeBool(out, crew.isUnknownPhi());

            if (CrewType.CRYSTAL.equals(crew.getRace())) {
                writeInt(out, crew.getLockdownRechargeTicks());
                writeInt(out, crew.getLockdownRechargeTicksGoal());
                writeInt(out, crew.getUnknownOmega());
            }
        }
    }

    private SystemState readSystem(InputStream in, SystemType systemType, int fileFormat) throws IOException {
        SystemState system = new SystemState(systemType);
        int capacity = readInt(in);

        // Normally systems are 28 bytes, but if not present on the
        // ship, capacity will be zero, and the system will only
        // occupy the 4 bytes that declared the capacity. And the
        // next system will begin 24 bytes sooner.
        if (capacity > 0) {
            system.setCapacity(capacity);
            system.setPower(readInt(in));
            system.setDamagedBars(readInt(in));
            system.setIonizedBars(readInt(in));       // TODO: Active mind control has -1?

            system.setDeionizationTicks(readMinMaxedInt(in));  // May be MIN_VALUE.

            system.setRepairProgress(readInt(in));
            system.setDamageProgress(readInt(in));

            if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                system.setBatteryPower(readInt(in));
                system.setHackLevel(readInt(in));
                system.setHacked(readBool(in));
                system.setTemporaryCapacityCap(readInt(in));
                system.setTemporaryCapacityLoss(readInt(in));
                system.setTemporaryCapacityDivisor(readInt(in));
            }
        }
        return system;
    }

    public void writeSystem(OutputStream out, SystemState system, int fileFormat) throws IOException {
        writeInt(out, system.getCapacity());
        if (system.getCapacity() > 0) {
            writeInt(out, system.getPower());
            writeInt(out, system.getDamagedBars());
            writeInt(out, system.getIonizedBars());

            writeMinMaxedInt(out, system.getDeionizationTicks());  // May be MIN_VALUE.

            writeInt(out, system.getRepairProgress());
            writeInt(out, system.getDamageProgress());

            if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                writeInt(out, system.getBatteryPower());
                writeInt(out, system.getHackLevel());
                writeBool(out, system.isHacked());
                writeInt(out, system.getTemporaryCapacityCap());
                writeInt(out, system.getTemporaryCapacityLoss());
                writeInt(out, system.getTemporaryCapacityDivisor());
            }
        }
    }

    private RoomState readRoom(InputStream in, int squaresH, int squaresV, int fileFormat) throws IOException {
        RoomState room = new RoomState();
        int oxygen = readInt(in);
        if (oxygen < 0 || oxygen > 100) {
            throw new IOException("Unsupported room oxygen: " + oxygen);
        }
        room.setOxygen(oxygen);

        // Squares are written to disk top-to-bottom, left-to-right. (Index != ID!)
        SquareState[][] tmpSquares = new SquareState[squaresH][squaresV];
        for (int h = 0; h < squaresH; h++) {
            for (int v = 0; v < squaresV; v++) {
                tmpSquares[h][v] = new SquareState(readInt(in), readInt(in), readInt(in));
            }
        }
        // Add them to the room left-to-right, top-to-bottom. (Index == ID)
        for (int v = 0; v < squaresV; v++) {
            for (int h = 0; h < squaresH; h++) {
                room.addSquare(tmpSquares[h][v]);
            }
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            room.setStationSquare(readInt(in));

            StationDirection stationDirection = null;
            int stationDirectionFlag = readInt(in);

            if (stationDirectionFlag == 0) {
                stationDirection = StationDirection.DOWN;
            } else if (stationDirectionFlag == 1) {
                stationDirection = StationDirection.RIGHT;
            } else if (stationDirectionFlag == 2) {
                stationDirection = StationDirection.UP;
            } else if (stationDirectionFlag == 3) {
                stationDirection = StationDirection.LEFT;
            } else if (stationDirectionFlag == 4) {
                stationDirection = StationDirection.NONE;
            } else {
                throw new IOException("Unsupported room station direction flag: " + stationDirection);
            }
            room.setStationDirection(stationDirection);
        }

        return room;
    }

    public void writeRoom(OutputStream out, RoomState room, int squaresH, int squaresV, int fileFormat) throws IOException {
        writeInt(out, room.getOxygen());

        // Squares referenced by IDs left-to-right, top-to-bottom. (Index == ID)
        List<SquareState> squareList = room.getSquareList();
        int squareIndex = 0;
        SquareState[][] tmpSquares = new SquareState[squaresH][squaresV];
        for (int v = 0; v < squaresV; v++) {
            for (int h = 0; h < squaresH; h++) {
                tmpSquares[h][v] = squareList.get(squareIndex++);
            }
        }
        // Squares are written to disk top-to-bottom, left-to-right. (Index != ID!)
        for (int h = 0; h < squaresH; h++) {
            for (int v = 0; v < squaresV; v++) {
                SquareState square = tmpSquares[h][v];
                writeInt(out, square.getFireHealth());
                writeInt(out, square.getIgnitionProgress());
                writeInt(out, square.getExtinguishmentProgress());
            }
        }

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, room.getStationSquare());

            int stationDirectionFlag = 0;
            if (room.getStationDirection() == StationDirection.DOWN) {
                stationDirectionFlag = 0;
            } else if (room.getStationDirection() == StationDirection.RIGHT) {
                stationDirectionFlag = 1;
            } else if (room.getStationDirection() == StationDirection.UP) {
                stationDirectionFlag = 2;
            } else if (room.getStationDirection() == StationDirection.LEFT) {
                stationDirectionFlag = 3;
            } else if (room.getStationDirection() == StationDirection.NONE) {
                stationDirectionFlag = 4;
            } else {
                throw new IOException("Unsupported room station direction: " + room.getStationDirection().toString());
            }
            writeInt(out, stationDirectionFlag);
        }
    }

    private DoorState readDoor(InputStream in, int fileFormat) throws IOException {
        DoorState door = new DoorState();

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            door.setCurrentMaxHealth(readInt(in));
            door.setHealth(readInt(in));
            door.setNominalHealth(readInt(in));
        }

        door.setOpen(readBool(in));
        door.setWalkingThrough(readBool(in));

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            door.setUnknownDelta(readInt(in));
            door.setUnknownEpsilon(readInt(in));  // TODO: Confirm: Drone lockdown.
        }

        return door;
    }

    public void writeDoor(OutputStream out, DoorState door, int fileFormat) throws IOException {
        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, door.getCurrentMaxHealth());
            writeInt(out, door.getHealth());
            writeInt(out, door.getNominalHealth());
        }

        writeBool(out, door.isOpen());
        writeBool(out, door.isWalkingThrough());

        if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
            writeInt(out, door.getUnknownDelta());
            writeInt(out, door.getUnknownEpsilon());
        }
    }

    private LockdownCrystal readLockdownCrystal(InputStream in) throws IOException {
        LockdownCrystal crystal = new LockdownCrystal();

        crystal.setCurrentPositionX(readInt(in));
        crystal.setCurrentPositionY(readInt(in));
        crystal.setSpeed(readInt(in));
        crystal.setGoalPositionX(readInt(in));
        crystal.setGoalPositionY(readInt(in));
        crystal.setArrived(readBool(in));
        crystal.setDone(readBool(in));
        crystal.setLifetime(readInt(in));
        crystal.setSuperFreeze(readBool(in));
        crystal.setLockingRoom(readInt(in));
        crystal.setAnimDirection(readInt(in));
        crystal.setShardProgress(readInt(in));

        return crystal;
    }

    public void writeLockdownCrystal(OutputStream out, LockdownCrystal crystal) throws IOException {
        writeInt(out, crystal.getCurrentPositionX());
        writeInt(out, crystal.getCurrentPositionY());
        writeInt(out, crystal.getSpeed());
        writeInt(out, crystal.getGoalPositionX());
        writeInt(out, crystal.getGoalPositionY());
        writeBool(out, crystal.isArrived());
        writeBool(out, crystal.isDone());
        writeInt(out, crystal.getLifetime());
        writeBool(out, crystal.isSuperFreeze());
        writeInt(out, crystal.getLockingRoom());
        writeInt(out, crystal.getAnimDirection());
        writeInt(out, crystal.getShardProgress());
    }

    private DroneState readDrone(InputStream in) throws IOException {
        DroneState drone = new DroneState();
        drone.setDroneId(readString(in));
        drone.setArmed(readBool(in));
        drone.setPlayerControlled(readBool(in));
        drone.setBodyX(readInt(in));
        drone.setBodyY(readInt(in));
        drone.setBodyRoomId(readInt(in));
        drone.setBodyRoomSquare(readInt(in));
        drone.setHealth(readInt(in));
        return drone;
    }

    public void writeDrone(OutputStream out, DroneState drone) throws IOException {
        writeString(out, drone.getDroneId());
        writeBool(out, drone.isArmed());
        writeBool(out, drone.isPlayerControlled());
        writeInt(out, drone.getBodyX());
        writeInt(out, drone.getBodyY());
        writeInt(out, drone.getBodyRoomId());
        writeInt(out, drone.getBodyRoomSquare());
        writeInt(out, drone.getHealth());
    }

    private BeaconState readBeacon(InputStream in, int fileFormat) throws IOException {
        BeaconState beacon = new BeaconState();

        beacon.setVisitCount(readInt(in));
        if (beacon.getVisitCount() > 0) {
            beacon.setBgStarscapeImageInnerPath(readString(in));
            beacon.setBgSpriteImageInnerPath(readString(in));
            beacon.setBgSpritePosX(readInt(in));
            beacon.setBgSpritePosY(readInt(in));
            beacon.setBgSpriteRotation(readInt(in));
        }

        beacon.setSeen(readBool(in));

        boolean enemyPresent = readBool(in);
        beacon.setEnemyPresent(enemyPresent);
        if (enemyPresent) {
            beacon.setShipEventId(readString(in));
            beacon.setAutoBlueprintId(readString(in));
            beacon.setShipEventSeed(readInt(in));

            // When player's at this beacon, the seed here matches
            // current encounter's seed.
        }

        beacon.setFleetPresence(FleetPresence.fromInt(readInt(in)));
        beacon.setUnderAttack(readBool(in));

        boolean storePresent = readBool(in);
        if (storePresent) {
            StoreState store = new StoreState();

            int shelfCount = 2;          // FTL 1.01-1.03.3 only had two shelves.
            if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                shelfCount = readInt(in);  // FTL 1.5.4 made shelves into an N-sized list.
            }
            for (int i = 0; i < shelfCount; i++) {
                store.addShelf(readStoreShelf(in, fileFormat));
            }

            store.setFuel(readInt(in));
            store.setMissiles(readInt(in));
            store.setDroneParts(readInt(in));
            beacon.setStore(store);
        }

        return beacon;

    }

    public void writeBeacon(OutputStream out, BeaconState beacon, int fileFormat) throws IOException {
        writeInt(out, beacon.getVisitCount());
        if (beacon.getVisitCount() > 0) {
            writeString(out, beacon.getBgStarscapeImageInnerPath());
            writeString(out, beacon.getBgSpriteImageInnerPath());
            writeInt(out, beacon.getBgSpritePosX());
            writeInt(out, beacon.getBgSpritePosY());
            writeInt(out, beacon.getBgSpriteRotation());
        }

        writeBool(out, beacon.isSeen());

        writeBool(out, beacon.isEnemyPresent());
        if (beacon.isEnemyPresent()) {
            writeString(out, beacon.getShipEventId());
            writeString(out, beacon.getAutoBlueprintId());
            writeInt(out, beacon.getShipEventSeed());
        }

        writeInt(out, beacon.getFleetPresence().toInt());

        writeBool(out, beacon.isUnderAttack());

        boolean storePresent = (beacon.getStore() != null);
        writeBool(out, storePresent);

        if (storePresent) {
            StoreState store = beacon.getStore();

            if (fileFormat == 2) {
                // FTL 1.01-1.03.3 always had two shelves.

                int shelfLimit = 2;
                int shelfCount = Math.min(store.getShelfList().size(), shelfLimit);
                for (int i = 0; i < shelfCount; i++) {
                    writeStoreShelf(out, store.getShelfList().get(i), fileFormat);
                }
                for (int i = 0; i < shelfLimit - shelfCount; i++) {
                    StoreShelf dummyShelf = new StoreShelf();
                    writeStoreShelf(out, dummyShelf, fileFormat);
                }
            } else if (fileFormat == 7 || fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                // FTL 1.5.4+ requires at least one shelf.
                int shelfReq = 1;

                List<StoreShelf> pendingShelves = new ArrayList<>(store.getShelfList());

                while (pendingShelves.size() < shelfReq) {
                    StoreShelf dummyShelf = new StoreShelf();
                    pendingShelves.add(dummyShelf);
                }

                writeInt(out, pendingShelves.size());

                for (StoreShelf shelf : pendingShelves) {
                    writeStoreShelf(out, shelf, fileFormat);
                }
            }

            writeInt(out, store.getFuel());
            writeInt(out, store.getMissiles());
            writeInt(out, store.getDroneParts());
        }
    }

    private StoreShelf readStoreShelf(InputStream in, int fileFormat) throws IOException {
        StoreShelf shelf = new StoreShelf();

        shelf.setItemType(StoreItemType.fromInt(readInt(in)));

        for (int i = 0; i < 3; i++) {
            int available = readInt(in); // -1=no item, 0=bought already, 1=buyable
            if (available < 0)
                continue;

            StoreItem item = new StoreItem(readString(in));
            item.setAvailable((available > 0));

            if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                item.setExtraData(readInt(in));
            }

            shelf.addItem(item);
        }

        return shelf;
    }

    public void writeStoreShelf(OutputStream out, StoreShelf shelf, int fileFormat) throws IOException {
        writeInt(out, shelf.getItemType().toInt());

        List<StoreItem> items = shelf.getItems();
        for (int i = 0; i < 3; i++) {  // TODO: Magic number.
            if (items.size() > i) {
                StoreItem item = items.get(i);

                int available = (item.isAvailable() ? 1 : 0);
                writeInt(out, available);
                writeString(out, item.getItemId());

                if (fileFormat == 8 || fileFormat == 9 || fileFormat == 11) {
                    writeInt(out, item.getExtraData());
                }
            } else {
                writeInt(out, -1);  // No item.
            }
        }
    }

    public EncounterState readEncounter(InputStream in, int fileFormat) throws IOException {
        EncounterState encounter = new EncounterState();

        encounter.setShipEventSeed(readInt(in));
        encounter.setSurrenderEventId(readString(in));
        encounter.setEscapeEventId(readString(in));
        encounter.setDestroyedEventId(readString(in));
        encounter.setDeadCrewEventId(readString(in));
        encounter.setGotAwayEventId(readString(in));

        encounter.setLastEventId(readString(in));

        if (fileFormat == 11) {
            encounter.setUnknownAlpha(readInt(in));
        }

        encounter.setText(readString(in));
        encounter.setAffectedCrewSeed(readInt(in));

        int choiceCount = readInt(in);
        List<Integer> choiceList = new ArrayList<Integer>();
        for (int i = 0; i < choiceCount; i++) {
            choiceList.add(readInt(in));
        }
        encounter.setChoiceList(choiceList);

        return encounter;
    }

    public void writeEncounter(OutputStream out, EncounterState encounter, int fileFormat) throws IOException {
        writeInt(out, encounter.getShipEventSeed());
        writeString(out, encounter.getSurrenderEventId());
        writeString(out, encounter.getEscapeEventId());
        writeString(out, encounter.getDestroyedEventId());
        writeString(out, encounter.getDeadCrewEventId());
        writeString(out, encounter.getGotAwayEventId());

        writeString(out, encounter.getLastEventId());

        if (fileFormat == 11) {
            writeInt(out, encounter.getUnknownAlpha());
        }

        writeString(out, encounter.getText());
        writeInt(out, encounter.getAffectedCrewSeed());

        writeInt(out, encounter.getChoiceList().size());
        for (Integer choiceInt : encounter.getChoiceList()) {
            writeInt(out, choiceInt);
        }
    }

    private NearbyShipAIState readNearbyShipAI(FileInputStream in) throws IOException {
        NearbyShipAIState ai = new NearbyShipAIState();

        ai.setSurrendered(readBool(in));
        ai.setEscaping(readBool(in));
        ai.setDestroyed(readBool(in));
        ai.setSurrenderThreshold(readInt(in));
        ai.setEscapeThreshold(readInt(in));
        ai.setEscapeTicks(readInt(in));
        ai.setStalemateTriggered(readBool(in));
        ai.setStalemateTicks(readInt(in));
        ai.setBoardingAttempts(readInt(in));
        ai.setBoardersNeeded(readInt(in));

        return ai;
    }

    public void writeNearbyShipAI(OutputStream out, NearbyShipAIState ai) throws IOException {
        writeBool(out, ai.hasSurrendered());
        writeBool(out, ai.isEscaping());
        writeBool(out, ai.isDestroyed());
        writeInt(out, ai.getSurrenderThreshold());
        writeInt(out, ai.getEscapeThreshold());
        writeInt(out, ai.getEscapeTicks());
        writeBool(out, ai.isStalemateTriggered());
        writeInt(out, ai.getStalemateTicks());
        writeInt(out, ai.getBoardingAttempts());
        writeInt(out, ai.getBoardersNeeded());
    }

    private EnvironmentState readEnvironment(FileInputStream in) throws IOException {
        EnvironmentState env = new EnvironmentState();

        env.setRedGiantPresent(readBool(in));
        env.setPulsarPresent(readBool(in));
        env.setPDSPresent(readBool(in));

        int vulnFlag = readInt(in);
        HazardVulnerability vuln = null;
        if (vulnFlag == 0) {
            vuln = HazardVulnerability.PLAYER_SHIP;
        } else if (vulnFlag == 1) {
            vuln = HazardVulnerability.NEARBY_SHIP;
        } else if (vulnFlag == 2) {
            vuln = HazardVulnerability.BOTH_SHIPS;
        } else {
            throw new IOException(String.format("Unsupported environment vulnerability flag: %d", vulnFlag));
        }
        env.setVulnerableShips(vuln);

        boolean asteroidsPresent = readBool(in);
        if (asteroidsPresent) {
            AsteroidFieldState asteroidField = new AsteroidFieldState();
            asteroidField.setUnknownAlpha(readInt(in));
            asteroidField.setStrayRockTicks(readInt(in));
            asteroidField.setUnknownGamma(readInt(in));
            asteroidField.setBgDriftTicks(readInt(in));
            asteroidField.setCurrentTarget(readInt(in));
            env.setAsteroidField(asteroidField);
        }
        env.setSolarFlareFadeTicks(readInt(in));
        env.setHavocTicks(readInt(in));
        env.setPDSTicks(readInt(in));

        return env;
    }

    public void writeEnvironment(OutputStream out, EnvironmentState env) throws IOException {
        writeBool(out, env.isRedGiantPresent());
        writeBool(out, env.isPulsarPresent());
        writeBool(out, env.isPDSPresent());

        int vulnFlag = 0;
        if (env.getVulnerableShips() == HazardVulnerability.PLAYER_SHIP) {
            vulnFlag = 0;
        } else if (env.getVulnerableShips() == HazardVulnerability.NEARBY_SHIP) {
            vulnFlag = 1;
        } else if (env.getVulnerableShips() == HazardVulnerability.BOTH_SHIPS) {
            vulnFlag = 2;
        } else {
            throw new IOException(String.format("Unsupported environment vulnerability: %s", env.getVulnerableShips().toString()));
        }
        writeInt(out, vulnFlag);

        boolean asteroidsPresent = (env.getAsteroidField() != null);
        writeBool(out, asteroidsPresent);
        if (asteroidsPresent) {
            AsteroidFieldState asteroidField = env.getAsteroidField();
            writeInt(out, asteroidField.getUnknownAlpha());
            writeInt(out, asteroidField.getStrayRockTicks());
            writeInt(out, asteroidField.getUnknownGamma());
            writeInt(out, asteroidField.getBgDriftTicks());
            writeInt(out, asteroidField.getCurrentTarget());
        }

        writeInt(out, env.getSolarFlareFadeTicks());
        writeInt(out, env.getHavocTicks());
        writeInt(out, env.getPDSTicks());
    }

    public RebelFlagshipState readRebelFlagship(InputStream in) throws IOException {
        RebelFlagshipState flagship = new RebelFlagshipState();

        flagship.setPendingStage(readInt(in));

        int previousRoomCount = readInt(in);
        for (int i = 0; i < previousRoomCount; i++) {
            flagship.setPreviousOccupancy(i, readInt(in));
        }

        return flagship;
    }

    public void writeRebelFlagship(OutputStream out, RebelFlagshipState flagship) throws IOException {
        writeInt(out, flagship.getPendingStage());

        writeInt(out, flagship.getOccupancyMap().size());
        for (Map.Entry<Integer, Integer> entry : flagship.getOccupancyMap().entrySet()) {
            int occupantCount = entry.getValue();
            writeInt(out, occupantCount);
        }
    }

    public AnimState readAnim(InputStream in) throws IOException {
        AnimState anim = new AnimState();

        anim.setPlaying(readBool(in));
        anim.setLooping(readBool(in));
        anim.setCurrentFrame(readInt(in));
        anim.setProgressTicks(readInt(in));
        anim.setScale(readInt(in));
        anim.setX(readInt(in));
        anim.setY(readInt(in));

        return anim;
    }

    public void writeAnim(OutputStream out, AnimState anim) throws IOException {
        writeBool(out, anim.isPlaying());
        writeBool(out, anim.isLooping());
        writeInt(out, anim.getCurrentFrame());
        writeInt(out, anim.getProgressTicks());
        writeInt(out, anim.getScale());
        writeInt(out, anim.getX());
        writeInt(out, anim.getY());
    }

    private ProjectileState readProjectile(FileInputStream in, int fileFormat) throws IOException {
//        log.debug( "Projectile: {}", in.getChannel().position() )

        ProjectileState projectile = new ProjectileState();

        int projectileTypeFlag = readInt(in);
        if (projectileTypeFlag == 0) {
            projectile.setProjectileType(ProjectileType.INVALID);
            return projectile;  // No other fields are set for invalid projectiles.
        } else if (projectileTypeFlag == 1) {
            projectile.setProjectileType(ProjectileType.LASER_OR_BURST);
        } else if (projectileTypeFlag == 2) {
            projectile.setProjectileType(ProjectileType.ROCK_OR_EXPLOSION);
        } else if (projectileTypeFlag == 3) {
            projectile.setProjectileType(ProjectileType.MISSILE);
        } else if (projectileTypeFlag == 4) {
            projectile.setProjectileType(ProjectileType.BOMB);
        } else if (projectileTypeFlag == 5) {
            projectile.setProjectileType(ProjectileType.BEAM);
        } else if (projectileTypeFlag == 6 && fileFormat == 11) {
            projectile.setProjectileType(ProjectileType.PDS);
        } else {
            throw new IOException(String.format("Unsupported projectileType flag: %d", projectileTypeFlag));
        }

        projectile.setCurrentPositionX(readInt(in));
        projectile.setCurrentPositionY(readInt(in));
        projectile.setPreviousPositionX(readInt(in));
        projectile.setPreviousPositionY(readInt(in));
        projectile.setSpeed(readInt(in));
        projectile.setGoalPositionX(readInt(in));
        projectile.setGoalPositionY(readInt(in));
        projectile.setHeading(readInt(in));
        projectile.setOwnerId(readInt(in));
        projectile.setSelfId(readInt(in));

        projectile.setDamage(readDamage(in));

        projectile.setLifespan(readInt(in));
        projectile.setDestinationSpace(readInt(in));
        projectile.setCurrentSpace(readInt(in));
        projectile.setTargetId(readInt(in));
        projectile.setDead(readBool(in));
        projectile.setDeathAnimId(readString(in));
        projectile.setFlightAnimId(readString(in));

        projectile.setDeathAnim(readAnim(in));
        projectile.setFlightAnim(readAnim(in));

        projectile.setVelocityX(readInt(in));
        projectile.setVelocityY(readInt(in));
        projectile.setMissed(readBool(in));
        projectile.setHitTarget(readBool(in));
        projectile.setHitSolidSound(readString(in));
        projectile.setHitShieldSound(readString(in));
        projectile.setMissSound(readString(in));
        projectile.setEntryAngle(readMinMaxedInt(in));
        projectile.setStartedDying(readBool(in));
        projectile.setPassedTarget(readBool(in));

        projectile.setType(readInt(in));
        projectile.setBroadcastTarget(readBool(in));

        ExtendedProjectileInfo extendedInfo = null;
        if (ProjectileType.LASER_OR_BURST.equals(projectile.getProjectileType())) {
            // Laser/Burst (2).
            // Usually getType() is 4 for Laser, 2 for Burst.
            extendedInfo = readLaserProjectileInfo(in);
        } else if (ProjectileType.ROCK_OR_EXPLOSION.equals(projectile.getProjectileType())) {
            // Explosion/Asteroid (0).
            // getType() is always 2?
            extendedInfo = new EmptyProjectileInfo();
        } else if (ProjectileType.MISSILE.equals(projectile.getProjectileType())) {
            // Missile (0).
            // getType() is always 1?
            extendedInfo = new EmptyProjectileInfo();
        } else if (ProjectileType.BOMB.equals(projectile.getProjectileType())) {
            // Bomb (5).
            // getType() is always 5?
            extendedInfo = readBombProjectileInfo(in);
        } else if (ProjectileType.BEAM.equals(projectile.getProjectileType())) {
            // Beam (25).
            // getType() is always 5?
            extendedInfo = readBeamProjectileInfo(in);
        } else if (ProjectileType.PDS.equals(projectile.getProjectileType())) {
            // PDS (12)
            // getType() is always 5?
            extendedInfo = readPDSProjectileInfo(in);
        }
        projectile.setExtendedInfo(extendedInfo);

        return projectile;
    }

    public void writeProjectile(OutputStream out, ProjectileState projectile, int fileFormat) throws IOException {

        int projectileTypeFlag = 0;
        if (ProjectileType.INVALID.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 0;
        } else if (ProjectileType.LASER_OR_BURST.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 1;
        } else if (ProjectileType.ROCK_OR_EXPLOSION.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 2;
        } else if (ProjectileType.MISSILE.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 3;
        } else if (ProjectileType.BOMB.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 4;
        } else if (ProjectileType.BEAM.equals(projectile.getProjectileType())) {
            projectileTypeFlag = 5;
        } else if (ProjectileType.PDS.equals(projectile.getProjectileType()) && fileFormat == 11) {
            projectileTypeFlag = 6;
        } else {
            throw new IOException(String.format("Unsupported projectileType: %s", projectile.getProjectileType().toString()));
        }
        writeInt(out, projectileTypeFlag);

        if (ProjectileType.INVALID.equals(projectile.getProjectileType())) {
            return;  // No other fields are set for invalid projectiles.
        }

        writeInt(out, projectile.getCurrentPositionX());
        writeInt(out, projectile.getCurrentPositionY());
        writeInt(out, projectile.getPreviousPositionX());
        writeInt(out, projectile.getPreviousPositionY());
        writeInt(out, projectile.getSpeed());
        writeInt(out, projectile.getGoalPositionX());
        writeInt(out, projectile.getGoalPositionY());
        writeInt(out, projectile.getHeading());
        writeInt(out, projectile.getOwnerId());
        writeInt(out, projectile.getSelfId());

        writeDamage(out, projectile.getDamage());

        writeInt(out, projectile.getLifespan());
        writeInt(out, projectile.getDestinationSpace());
        writeInt(out, projectile.getCurrentSpace());
        writeInt(out, projectile.getTargetId());
        writeBool(out, projectile.isDead());
        writeString(out, projectile.getDeathAnimId());
        writeString(out, projectile.getFlightAnimId());

        writeAnim(out, projectile.getDeathAnim());
        writeAnim(out, projectile.getFlightAnim());

        writeInt(out, projectile.getVelocityX());
        writeInt(out, projectile.getVelocityY());
        writeBool(out, projectile.hasMissed());
        writeBool(out, projectile.hasHitTarget());
        writeString(out, projectile.getHitSolidSound());
        writeString(out, projectile.getHitShieldSound());
        writeString(out, projectile.getMissSound());
        writeMinMaxedInt(out, projectile.getEntryAngle());
        writeBool(out, projectile.hasStartedDying());
        writeBool(out, projectile.hasPassedTarget());

        writeInt(out, projectile.getType());
        writeBool(out, projectile.getBroadcastTarget());

        ExtendedProjectileInfo extendedInfo = projectile.getExtendedInfo(ExtendedProjectileInfo.class);
        if (extendedInfo instanceof IntegerProjectileInfo) {
            IntegerProjectileInfo intInfo = projectile.getExtendedInfo(IntegerProjectileInfo.class);
            for (int i = 0; i < intInfo.getSize(); i++) {
                writeMinMaxedInt(out, intInfo.get(i));
            }
        } else if (extendedInfo instanceof BeamProjectileInfo) {
            writeBeamProjectileInfo(out, projectile.getExtendedInfo(BeamProjectileInfo.class));
        } else if (extendedInfo instanceof BombProjectileInfo) {
            writeBombProjectileInfo(out, projectile.getExtendedInfo(BombProjectileInfo.class));
        } else if (extendedInfo instanceof LaserProjectileInfo) {
            writeLaserProjectileInfo(out, projectile.getExtendedInfo(LaserProjectileInfo.class));
        } else if (extendedInfo instanceof PDSProjectileInfo) {
            writePDSProjectileInfo(out, projectile.getExtendedInfo(PDSProjectileInfo.class));
        } else if (extendedInfo instanceof EmptyProjectileInfo) {
            // No-op.
        } else {
            throw new IOException("Unsupported extended projectile info: " + extendedInfo.getClass().getSimpleName());
        }
    }

    public DamageState readDamage(InputStream in) throws IOException {
        DamageState damage = new DamageState();

        damage.setHullDamage(readInt(in));
        damage.setShieldPiercing(readInt(in));
        damage.setFireChance(readInt(in));
        damage.setBreachChance(readInt(in));
        damage.setIonDamage(readInt(in));
        damage.setSystemDamage(readInt(in));
        damage.setPersonnelDamage(readInt(in));
        damage.setHullBuster(readBool(in));
        damage.setOwnerId(readInt(in));
        damage.setSelfId(readInt(in));
        damage.setLockdown(readBool(in));
        damage.setCrystalShard(readBool(in));
        damage.setStunChance(readInt(in));
        damage.setStunAmount(readInt(in));

        return damage;
    }

    public void writeDamage(OutputStream out, DamageState damage) throws IOException {
        writeInt(out, damage.getHullDamage());
        writeInt(out, damage.getShieldPiercing());
        writeInt(out, damage.getFireChance());
        writeInt(out, damage.getBreachChance());
        writeInt(out, damage.getIonDamage());
        writeInt(out, damage.getSystemDamage());
        writeInt(out, damage.getPersonnelDamage());
        writeBool(out, damage.isHullBuster());
        writeInt(out, damage.getOwnerId());
        writeInt(out, damage.getSelfId());
        writeBool(out, damage.isLockdown());
        writeBool(out, damage.isCrystalShard());
        writeInt(out, damage.getStunChance());
        writeInt(out, damage.getStunAmount());
    }

    private BeamProjectileInfo readBeamProjectileInfo(FileInputStream in) throws IOException {
        BeamProjectileInfo beamInfo = new BeamProjectileInfo();

        beamInfo.setEmissionEndX(readInt(in));
        beamInfo.setEmissionEndY(readInt(in));
        beamInfo.setStrafeSourceX(readInt(in));
        beamInfo.setStrafeSourceY(readInt(in));

        beamInfo.setStrafeEndX(readInt(in));
        beamInfo.setStrafeEndY(readInt(in));
        beamInfo.setUnknownBetaX(readInt(in));
        beamInfo.setUnknownBetaY(readInt(in));

        beamInfo.setSwathEndX(readInt(in));
        beamInfo.setSwathEndY(readInt(in));
        beamInfo.setSwathStartX(readInt(in));
        beamInfo.setSwathStartY(readInt(in));

        beamInfo.setUnknownGamma(readInt(in));
        beamInfo.setSwathLength(readInt(in));
        beamInfo.setUnknownDelta(readInt(in));

        beamInfo.setUnknownEpsilonX(readInt(in));
        beamInfo.setUnknownEpsilonY(readInt(in));

        beamInfo.setUnknownZeta(readInt(in));
        beamInfo.setUnknownEta(readInt(in));
        beamInfo.setEmissionAngle(readInt(in));

        beamInfo.setUnknownIota(readBool(in));
        beamInfo.setUnknownKappa(readBool(in));
        beamInfo.setFromDronePod(readBool(in));
        beamInfo.setUnknownMu(readBool(in));
        beamInfo.setUnknownNu(readBool(in));

        return beamInfo;
    }

    public void writeBeamProjectileInfo(OutputStream out, BeamProjectileInfo beamInfo) throws IOException {
        writeInt(out, beamInfo.getEmissionEndX());
        writeInt(out, beamInfo.getEmissionEndY());
        writeInt(out, beamInfo.getStrafeSourceX());
        writeInt(out, beamInfo.getStrafeSourceY());

        writeInt(out, beamInfo.getStrafeEndX());
        writeInt(out, beamInfo.getStrafeEndY());
        writeInt(out, beamInfo.getUnknownBetaX());
        writeInt(out, beamInfo.getUnknownBetaY());

        writeInt(out, beamInfo.getSwathEndX());
        writeInt(out, beamInfo.getSwathEndY());
        writeInt(out, beamInfo.getSwathStartX());
        writeInt(out, beamInfo.getSwathStartY());

        writeInt(out, beamInfo.getUnknownGamma());
        writeInt(out, beamInfo.getSwathLength());
        writeInt(out, beamInfo.getUnknownDelta());

        writeInt(out, beamInfo.getUnknownEpsilonX());
        writeInt(out, beamInfo.getUnknownEpsilonY());

        writeInt(out, beamInfo.getUnknownZeta());
        writeInt(out, beamInfo.getUnknownEta());
        writeInt(out, beamInfo.getEmissionAngle());

        writeBool(out, beamInfo.getUnknownIota());
        writeBool(out, beamInfo.getUnknownKappa());
        writeBool(out, beamInfo.isFromDronePod());
        writeBool(out, beamInfo.getUnknownMu());
        writeBool(out, beamInfo.getUnknownNu());
    }

    private BombProjectileInfo readBombProjectileInfo(FileInputStream in) throws IOException {
        BombProjectileInfo bombInfo = new BombProjectileInfo();

        bombInfo.setUnknownAlpha(readInt(in));
        bombInfo.setFuseTicks(readInt(in));
        bombInfo.setUnknownGamma(readInt(in));
        bombInfo.setUnknownDelta(readInt(in));
        bombInfo.setArrived(readBool(in));

        return bombInfo;
    }

    public void writeBombProjectileInfo(OutputStream out, BombProjectileInfo bombInfo) throws IOException {
        writeInt(out, bombInfo.getUnknownAlpha());
        writeInt(out, bombInfo.getFuseTicks());
        writeInt(out, bombInfo.getUnknownGamma());
        writeInt(out, bombInfo.getUnknownDelta());
        writeBool(out, bombInfo.hasArrived());
    }

    private LaserProjectileInfo readLaserProjectileInfo(FileInputStream in) throws IOException {
        LaserProjectileInfo laserInfo = new LaserProjectileInfo();

        laserInfo.setUnknownAlpha(readInt(in));
        laserInfo.setSpin(readInt(in));

        return laserInfo;
    }

    public void writeLaserProjectileInfo(OutputStream out, LaserProjectileInfo laserInfo) throws IOException {
        writeInt(out, laserInfo.getUnknownAlpha());
        writeInt(out, laserInfo.getSpin());
    }

    private PDSProjectileInfo readPDSProjectileInfo(FileInputStream in) throws IOException {
        PDSProjectileInfo pdsInfo = new PDSProjectileInfo();

        pdsInfo.setUnknownAlpha(readInt(in));
        pdsInfo.setUnknownBeta(readInt(in));
        pdsInfo.setUnknownGamma(readInt(in));
        pdsInfo.setUnknownDelta(readInt(in));
        pdsInfo.setUnknownEpsilon(readInt(in));
        pdsInfo.setUnknownZeta(readAnim(in));

        return pdsInfo;
    }

    public void writePDSProjectileInfo(OutputStream out, PDSProjectileInfo pdsInfo) throws IOException {
        writeInt(out, pdsInfo.getUnknownAlpha());
        writeInt(out, pdsInfo.getUnknownBeta());
        writeInt(out, pdsInfo.getUnknownGamma());
        writeInt(out, pdsInfo.getUnknownDelta());
        writeInt(out, pdsInfo.getUnknownEpsilon());
        writeAnim(out, pdsInfo.getUnknownZeta());
    }


    // TODO remove
    private int readMinMaxedInt(InputStream in) throws IOException {
        int n = readInt(in);

        if (n == -2147483648) {
            n = Integer.MIN_VALUE;
        } else if (n == 2147483647) {
            n = Integer.MAX_VALUE;
        }

        return n;
    }

    // TODO remove
    private void writeMinMaxedInt(OutputStream out, int n) throws IOException {
        if (n == Integer.MIN_VALUE) {
            n = -2147483648;
        } else if (n == Integer.MAX_VALUE) {
            n = 2147483647;
        }

        writeInt(out, n);
    }

    /**
     * Reads additional fields of various ship-related classes.
     * <p>
     * This method does not involve a dedicated class.
     */
    private void readExtendedShipInfo(FileInputStream in, ShipState shipState, int fileFormat) throws IOException {
        // There is no explicit list count for drones.
        for (DroneState drone : shipState.getDroneList()) {
            ExtendedDroneInfo droneInfo = new ExtendedDroneInfo();

            droneInfo.setDeployed(readBool(in));
            droneInfo.setArmed(readBool(in));

            String droneId = drone.getDroneId();
            DroneBlueprint droneBlueprint = DataManager.get().getDrone(droneId);
            if (droneBlueprint == null) throw new IOException("Unrecognized DroneBlueprint: " + droneId);

            DroneType droneType = DroneType.findById(droneBlueprint.getType());
            if (droneType == null)
                throw new IOException(String.format("DroneBlueprint \"%s\" has an unrecognized type: %s", droneId, droneBlueprint.getType()));

            if (DroneType.REPAIR.equals(droneType) ||
                    DroneType.BATTLE.equals(droneType)) {
                // No drone pod for these types.
            } else {
                DronePodState dronePod = readDronePod(in, droneType);
                droneInfo.setDronePod(dronePod);
            }

            drone.setExtendedDroneInfo(droneInfo);
        }

        SystemState hackingState = shipState.getSystem(SystemType.HACKING);
        if (hackingState != null && hackingState.getCapacity() > 0) {
            HackingInfo hackingInfo = new HackingInfo();

            int targetSystemTypeFlag = readInt(in);
            SystemType targetSystemType;
            if (targetSystemTypeFlag == -1) targetSystemType = null;
            else if (targetSystemTypeFlag == 0) targetSystemType = SystemType.SHIELDS;
            else if (targetSystemTypeFlag == 1) targetSystemType = SystemType.ENGINES;
            else if (targetSystemTypeFlag == 2) targetSystemType = SystemType.OXYGEN;
            else if (targetSystemTypeFlag == 3) targetSystemType = SystemType.WEAPONS;
            else if (targetSystemTypeFlag == 4) targetSystemType = SystemType.DRONE_CTRL;
            else if (targetSystemTypeFlag == 5) targetSystemType = SystemType.MEDBAY;
            else if (targetSystemTypeFlag == 6) targetSystemType = SystemType.PILOT;
            else if (targetSystemTypeFlag == 7) targetSystemType = SystemType.SENSORS;
            else if (targetSystemTypeFlag == 8) targetSystemType = SystemType.DOORS;
            else if (targetSystemTypeFlag == 9) targetSystemType = SystemType.TELEPORTER;
            else if (targetSystemTypeFlag == 10) targetSystemType = SystemType.CLOAKING;
            else if (targetSystemTypeFlag == 11) targetSystemType = SystemType.ARTILLERY;
            else if (targetSystemTypeFlag == 12) targetSystemType = SystemType.BATTERY;
            else if (targetSystemTypeFlag == 13) targetSystemType = SystemType.CLONEBAY;
            else if (targetSystemTypeFlag == 14) targetSystemType = SystemType.MIND;
            else if (targetSystemTypeFlag == 15) targetSystemType = SystemType.HACKING;
            else {
                throw new IOException(String.format("Unsupported hacking targetSystemTypeFlag: %d", targetSystemTypeFlag));
            }
            hackingInfo.setTargetSystemType(targetSystemType);

            hackingInfo.setUnknownBeta(readInt(in));
            hackingInfo.setDronePodVisible(readBool(in));
            hackingInfo.setUnknownDelta(readInt(in));

            hackingInfo.setDisruptionTicks(readInt(in));
            hackingInfo.setDisruptionTicksGoal(readInt(in));

            hackingInfo.setDisrupting(readBool(in));

            DronePodState dronePod = readDronePod(in, DroneType.HACKING);  // The hacking drone.
            hackingInfo.setDronePod(dronePod);

            shipState.addExtendedSystemInfo(hackingInfo);
        }

        SystemState mindState = shipState.getSystem(SystemType.MIND);
        if (mindState != null && mindState.getCapacity() > 0) {
            MindInfo mindInfo = new MindInfo();

            mindInfo.setMindControlTicks(readInt(in));
            mindInfo.setMindControlTicksGoal(readInt(in));

            shipState.addExtendedSystemInfo(mindInfo);
        }

        SystemState weaponsState = shipState.getSystem(SystemType.WEAPONS);
        if (weaponsState != null && weaponsState.getCapacity() > 0) {

            int weaponCount = shipState.getWeaponList().size();
            int weaponModCount = readInt(in);
            if (weaponModCount != weaponCount) {
                throw new IOException(String.format("Found %d WeaponModules, but there are %d Weapons.", weaponModCount, weaponCount));
            }

            for (WeaponState weapon : shipState.getWeaponList()) {
                WeaponModuleState weaponMod = readWeaponModule(in, fileFormat);
                weapon.setWeaponModule(weaponMod);
            }
        }

        // Get ALL artillery rooms' SystemStates from the ShipState.
        List<SystemState> artilleryStateList = shipState.getSystems(SystemType.ARTILLERY);
        for (SystemState artilleryState : artilleryStateList) {

            if (artilleryState.getCapacity() > 0) {
                ArtilleryInfo artilleryInfo = new ArtilleryInfo();

                artilleryInfo.setWeaponModule(readWeaponModule(in, fileFormat));

                shipState.addExtendedSystemInfo(artilleryInfo);
            }
        }

        // A list of standalone drones, for flagship swarms. Always 0 for player.

        int standaloneDroneCount = readInt(in);
        for (int i = 0; i < standaloneDroneCount; i++) {
            String droneId = readString(in);
            DroneBlueprint droneBlueprint = DataManager.get().getDrone(droneId);
            if (droneBlueprint == null) throw new IOException("Unrecognized DroneBlueprint: " + droneId);

            StandaloneDroneState standaloneDrone = new StandaloneDroneState();
            standaloneDrone.setDroneId(droneId);

            DroneType droneType = DroneType.findById(droneBlueprint.getType());
            if (droneType == null)
                throw new IOException(String.format("DroneBlueprint \"%s\" has an unrecognized type: %s", droneId, droneBlueprint.getType()));

            DronePodState dronePod = readDronePod(in, droneType);
            standaloneDrone.setDronePod(dronePod);

            standaloneDrone.setUnknownAlpha(readInt(in));
            standaloneDrone.setUnknownBeta(readInt(in));
            standaloneDrone.setUnknownGamma(readInt(in));

            shipState.addStandaloneDrone(standaloneDrone);
        }
    }

    /**
     * Writes additional fields of various ship-related classes.
     * <p>
     * This method does not involve a dedicated class.
     */
    public void writeExtendedShipInfo(OutputStream out, ShipState shipState, int fileFormat) throws IOException {
        // There is no explicit list count for drones.
        for (DroneState drone : shipState.getDroneList()) {
            ExtendedDroneInfo droneInfo = drone.getExtendedDroneInfo();
            writeBool(out, droneInfo.isDeployed());
            writeBool(out, droneInfo.isArmed());

            if (droneInfo.getDronePod() != null) {
                writeDronePod(out, droneInfo.getDronePod());
            }
        }

        SystemState hackingState = shipState.getSystem(SystemType.HACKING);
        if (hackingState != null && hackingState.getCapacity() > 0) {
            // TODO: Compare system room count with extended info count.

            HackingInfo hackingInfo = shipState.getExtendedSystemInfo(HackingInfo.class);
            // This should not be null.

            SystemType targetSystemType = hackingInfo.getTargetSystemType();
            int targetSystemTypeFlag;
            if (targetSystemType == null) targetSystemTypeFlag = -1;
            else if (SystemType.SHIELDS.equals(targetSystemType)) targetSystemTypeFlag = 0;
            else if (SystemType.ENGINES.equals(targetSystemType)) targetSystemTypeFlag = 1;
            else if (SystemType.OXYGEN.equals(targetSystemType)) targetSystemTypeFlag = 2;
            else if (SystemType.WEAPONS.equals(targetSystemType)) targetSystemTypeFlag = 3;
            else if (SystemType.DRONE_CTRL.equals(targetSystemType)) targetSystemTypeFlag = 4;
            else if (SystemType.MEDBAY.equals(targetSystemType)) targetSystemTypeFlag = 5;
            else if (SystemType.PILOT.equals(targetSystemType)) targetSystemTypeFlag = 6;
            else if (SystemType.SENSORS.equals(targetSystemType)) targetSystemTypeFlag = 7;
            else if (SystemType.DOORS.equals(targetSystemType)) targetSystemTypeFlag = 8;
            else if (SystemType.TELEPORTER.equals(targetSystemType)) targetSystemTypeFlag = 9;
            else if (SystemType.CLOAKING.equals(targetSystemType)) targetSystemTypeFlag = 10;
            else if (SystemType.ARTILLERY.equals(targetSystemType)) targetSystemTypeFlag = 11;
            else if (SystemType.BATTERY.equals(targetSystemType)) targetSystemTypeFlag = 12;
            else if (SystemType.CLONEBAY.equals(targetSystemType)) targetSystemTypeFlag = 13;
            else if (SystemType.MIND.equals(targetSystemType)) targetSystemTypeFlag = 14;
            else if (SystemType.HACKING.equals(targetSystemType)) targetSystemTypeFlag = 15;
            else {
                throw new IOException(String.format("Unsupported hacking targetSystemType: %s", targetSystemType.getId()));
            }
            writeInt(out, targetSystemTypeFlag);

            writeInt(out, hackingInfo.getUnknownBeta());
            writeBool(out, hackingInfo.isDronePodVisible());
            writeInt(out, hackingInfo.getUnknownDelta());

            writeInt(out, hackingInfo.getDisruptionTicks());
            writeInt(out, hackingInfo.getDisruptionTicksGoal());

            writeBool(out, hackingInfo.isDisrupting());

            writeDronePod(out, hackingInfo.getDronePod());
        }

        SystemState mindState = shipState.getSystem(SystemType.MIND);
        if (mindState != null && mindState.getCapacity() > 0) {
            MindInfo mindInfo = shipState.getExtendedSystemInfo(MindInfo.class);
            // This should not be null.
            writeInt(out, mindInfo.getMindControlTicks());
            writeInt(out, mindInfo.getMindControlTicksGoal());
        }

        // If there's a Weapons system, write the weapon modules (even if there are 0 of them).
        SystemState weaponsState = shipState.getSystem(SystemType.WEAPONS);
        if (weaponsState != null && weaponsState.getCapacity() > 0) {

            int weaponCount = shipState.getWeaponList().size();
            writeInt(out, weaponCount);
            for (WeaponState weapon : shipState.getWeaponList()) {
                writeWeaponModule(out, weapon.getWeaponModule(), fileFormat);
            }
        }

        List<ArtilleryInfo> artilleryInfoList = shipState.getExtendedSystemInfoList(ArtilleryInfo.class);
        for (ArtilleryInfo artilleryInfo : artilleryInfoList) {
            writeWeaponModule(out, artilleryInfo.getWeaponModule(), fileFormat);
        }

        writeInt(out, shipState.getStandaloneDroneList().size());
        for (StandaloneDroneState standaloneDrone : shipState.getStandaloneDroneList()) {
            writeString(out, standaloneDrone.getDroneId());

            writeDronePod(out, standaloneDrone.getDronePod());

            writeInt(out, standaloneDrone.getUnknownAlpha());
            writeInt(out, standaloneDrone.getUnknownBeta());
            writeInt(out, standaloneDrone.getUnknownGamma());
        }
    }

    private DronePodState readDronePod(FileInputStream in, DroneType droneType) throws IOException {
        if (droneType == null) throw new IllegalArgumentException("DroneType cannot be null.");

//        log.debug("Drone Pod: {}", in.getChannel().position())

        DronePodState dronePod = new DronePodState();
        dronePod.setDroneType(droneType);
        dronePod.setMourningTicks(readInt(in));
        dronePod.setCurrentSpace(readInt(in));
        dronePod.setDestinationSpace(readInt(in));

        dronePod.setCurrentPositionX(readMinMaxedInt(in));
        dronePod.setCurrentPositionY(readMinMaxedInt(in));
        dronePod.setPreviousPositionX(readMinMaxedInt(in));
        dronePod.setPreviousPositionY(readMinMaxedInt(in));
        dronePod.setGoalPositionX(readMinMaxedInt(in));
        dronePod.setGoalPositionY(readMinMaxedInt(in));

        dronePod.setUnknownEpsilon(readMinMaxedInt(in));
        dronePod.setUnknownZeta(readMinMaxedInt(in));
        dronePod.setNextTargetX(readMinMaxedInt(in));
        dronePod.setNextTargetY(readMinMaxedInt(in));
        dronePod.setUnknownIota(readMinMaxedInt(in));
        dronePod.setUnknownKappa(readMinMaxedInt(in));

        dronePod.setBuildupTicks(readInt(in));
        dronePod.setStationaryTicks(readInt(in));
        dronePod.setCooldownTicks(readInt(in));
        dronePod.setOrbitAngle(readInt(in));
        dronePod.setTurretAngle(readInt(in));
        dronePod.setUnknownXi(readInt(in));
        dronePod.setHopsToLive(readMinMaxedInt(in));
        dronePod.setUnknownPi(readInt(in));
        dronePod.setUnknownRho(readInt(in));
        dronePod.setOverloadTicks(readInt(in));
        dronePod.setUnknownTau(readInt(in));
        dronePod.setUnknownUpsilon(readInt(in));
        dronePod.setDeltaPositionX(readInt(in));
        dronePod.setDeltaPositionY(readInt(in));

        dronePod.setDeathAnim(readAnim(in));

        ExtendedDronePodInfo extendedInfo = null;
        if (DroneType.BOARDER.equals(droneType)) {
            BoarderDronePodInfo boarderPodInfo = new BoarderDronePodInfo();
            boarderPodInfo.setUnknownAlpha(readInt(in));
            boarderPodInfo.setUnknownBeta(readInt(in));
            boarderPodInfo.setUnknownGamma(readInt(in));
            boarderPodInfo.setUnknownDelta(readInt(in));
            boarderPodInfo.setBodyHealth(readInt(in));
            boarderPodInfo.setBodyX(readInt(in));
            boarderPodInfo.setBodyY(readInt(in));
            boarderPodInfo.setBodyRoomId(readInt(in));
            boarderPodInfo.setBodyRoomSquare(readInt(in));
            extendedInfo = boarderPodInfo;
        } else if (DroneType.HACKING.equals(droneType)) {
            HackingDronePodInfo hackingPodInfo = new HackingDronePodInfo();
            hackingPodInfo.setAttachPositionX(readInt(in));
            hackingPodInfo.setAttachPositionY(readInt(in));
            hackingPodInfo.setUnknownGamma(readInt(in));
            hackingPodInfo.setUnknownDelta(readInt(in));
            hackingPodInfo.setLandingAnim(readAnim(in));
            hackingPodInfo.setExtensionAnim(readAnim(in));
            extendedInfo = hackingPodInfo;
        } else if (DroneType.COMBAT.equals(droneType) ||
                DroneType.BEAM.equals(droneType)) {

            ZigZagDronePodInfo zigPodInfo = new ZigZagDronePodInfo();
            zigPodInfo.setLastWaypointX(readInt(in));
            zigPodInfo.setLastWaypointY(readInt(in));
            zigPodInfo.setTransitTicks(readMinMaxedInt(in));
            zigPodInfo.setExhaustAngle(readMinMaxedInt(in));
            zigPodInfo.setUnknownEpsilon(readMinMaxedInt(in));
            extendedInfo = zigPodInfo;
        } else if (DroneType.DEFENSE.equals(droneType)) {
            extendedInfo = new DefenseDronePodInfo();
        } else if (DroneType.SHIELD.equals(droneType)) {
            ShieldDronePodInfo shieldPodInfo = new ShieldDronePodInfo();
            shieldPodInfo.setUnknownAlpha(readInt(in));
            extendedInfo = shieldPodInfo;
        } else if (DroneType.SHIP_REPAIR.equals(droneType)) {
            ZigZagDronePodInfo zigPodInfo = new ZigZagDronePodInfo();
            zigPodInfo.setLastWaypointX(readInt(in));
            zigPodInfo.setLastWaypointY(readInt(in));
            zigPodInfo.setTransitTicks(readMinMaxedInt(in));
            zigPodInfo.setExhaustAngle(readMinMaxedInt(in));
            zigPodInfo.setUnknownEpsilon(readMinMaxedInt(in));
            extendedInfo = zigPodInfo;
        } else {
            throw new IOException("Unsupported droneType for drone pod: " + droneType.getId());
        }

        dronePod.setExtendedInfo(extendedInfo);

        return dronePod;
    }

    public void writeDronePod(OutputStream out, DronePodState dronePod) throws IOException {
        writeInt(out, dronePod.getMourningTicks());
        writeInt(out, dronePod.getCurrentSpace());
        writeInt(out, dronePod.getDestinationSpace());

        writeMinMaxedInt(out, dronePod.getCurrentPositionX());
        writeMinMaxedInt(out, dronePod.getCurrentPositionY());
        writeMinMaxedInt(out, dronePod.getPreviousPositionX());
        writeMinMaxedInt(out, dronePod.getPreviousPositionY());
        writeMinMaxedInt(out, dronePod.getGoalPositionX());
        writeMinMaxedInt(out, dronePod.getGoalPositionY());

        writeMinMaxedInt(out, dronePod.getUnknownEpsilon());
        writeMinMaxedInt(out, dronePod.getUnknownZeta());
        writeMinMaxedInt(out, dronePod.getNextTargetX());
        writeMinMaxedInt(out, dronePod.getNextTargetY());
        writeMinMaxedInt(out, dronePod.getUnknownIota());
        writeMinMaxedInt(out, dronePod.getUnknownKappa());

        writeInt(out, dronePod.getBuildupTicks());
        writeInt(out, dronePod.getStationaryTicks());
        writeInt(out, dronePod.getCooldownTicks());
        writeInt(out, dronePod.getOrbitAngle());
        writeInt(out, dronePod.getTurretAngle());
        writeInt(out, dronePod.getUnknownXi());
        writeMinMaxedInt(out, dronePod.getHopsToLive());
        writeInt(out, dronePod.getUnknownPi());
        writeInt(out, dronePod.getUnknownRho());
        writeInt(out, dronePod.getOverloadTicks());
        writeInt(out, dronePod.getUnknownTau());
        writeInt(out, dronePod.getUnknownUpsilon());
        writeInt(out, dronePod.getDeltaPositionX());
        writeInt(out, dronePod.getDeltaPositionY());

        writeAnim(out, dronePod.getDeathAnim());

        ExtendedDronePodInfo extendedInfo = dronePod.getExtendedInfo(ExtendedDronePodInfo.class);
        if (extendedInfo instanceof IntegerDronePodInfo) {
            IntegerDronePodInfo intPodInfo = dronePod.getExtendedInfo(IntegerDronePodInfo.class);
            for (int i = 0; i < intPodInfo.getSize(); i++) {
                writeMinMaxedInt(out, intPodInfo.get(i));
            }
        } else if (extendedInfo instanceof BoarderDronePodInfo) {
            BoarderDronePodInfo boarderPodInfo = dronePod.getExtendedInfo(BoarderDronePodInfo.class);
            writeInt(out, boarderPodInfo.getUnknownAlpha());
            writeInt(out, boarderPodInfo.getUnknownBeta());
            writeInt(out, boarderPodInfo.getUnknownGamma());
            writeInt(out, boarderPodInfo.getUnknownDelta());
            writeInt(out, boarderPodInfo.getBodyHealth());
            writeInt(out, boarderPodInfo.getBodyX());
            writeInt(out, boarderPodInfo.getBodyY());
            writeInt(out, boarderPodInfo.getBodyRoomId());
            writeInt(out, boarderPodInfo.getBodyRoomSquare());
        } else if (extendedInfo instanceof ShieldDronePodInfo) {
            ShieldDronePodInfo shieldPodInfo = dronePod.getExtendedInfo(ShieldDronePodInfo.class);
            writeInt(out, shieldPodInfo.getUnknownAlpha());
        } else if (extendedInfo instanceof HackingDronePodInfo) {
            HackingDronePodInfo hackingPodInfo = dronePod.getExtendedInfo(HackingDronePodInfo.class);
            writeInt(out, hackingPodInfo.getAttachPositionX());
            writeInt(out, hackingPodInfo.getAttachPositionY());
            writeInt(out, hackingPodInfo.getUnknownGamma());
            writeInt(out, hackingPodInfo.getUnknownDelta());
            writeAnim(out, hackingPodInfo.getLandingAnim());
            writeAnim(out, hackingPodInfo.getExtensionAnim());
        } else if (extendedInfo instanceof ZigZagDronePodInfo) {
            ZigZagDronePodInfo zigPodInfo = dronePod.getExtendedInfo(ZigZagDronePodInfo.class);
            writeInt(out, zigPodInfo.getLastWaypointX());
            writeInt(out, zigPodInfo.getLastWaypointY());
            writeMinMaxedInt(out, zigPodInfo.getTransitTicks());
            writeMinMaxedInt(out, zigPodInfo.getExhaustAngle());
            writeMinMaxedInt(out, zigPodInfo.getUnknownEpsilon());
        } else if (extendedInfo instanceof DefenseDronePodInfo) {
            // No-op.
        } else {
            throw new IOException("Unsupported extended drone pod info: " + extendedInfo.getClass().getSimpleName());
        }
    }

    private WeaponModuleState readWeaponModule(FileInputStream in, int fileFormat) throws IOException {
        WeaponModuleState weaponMod = new WeaponModuleState();

        weaponMod.setCooldownTicks(readInt(in));
        weaponMod.setCooldownTicksGoal(readInt(in));
        weaponMod.setSubcooldownTicks(readInt(in));
        weaponMod.setSubcooldownTicksGoal(readInt(in));
        weaponMod.setBoost(readInt(in));
        weaponMod.setCharge(readInt(in));

        int currentTargetsCount = readInt(in);
        List<XYPair> currentTargetsList = new ArrayList<XYPair>();
        for (int i = 0; i < currentTargetsCount; i++) {
            currentTargetsList.add(readReticleCoordinate(in));
        }
        weaponMod.setCurrentTargets(currentTargetsList);

        int prevTargetsCount = readInt(in);
        List<XYPair> prevTargetsList = new ArrayList<XYPair>();
        for (int i = 0; i < prevTargetsCount; i++) {
            prevTargetsList.add(readReticleCoordinate(in));
        }
        weaponMod.setPreviousTargets(prevTargetsList);

        weaponMod.setAutofire(readBool(in));
        weaponMod.setFireWhenReady(readBool(in));
        weaponMod.setTargetId(readInt(in));

        weaponMod.setWeaponAnim(readAnim(in));

        weaponMod.setProtractAnimTicks(readInt(in));
        weaponMod.setFiring(readBool(in));
        weaponMod.setUnknownPhi(readBool(in));

        if (fileFormat == 9 || fileFormat == 11) {
            weaponMod.setAnimCharge(readInt(in));

            weaponMod.setChargeAnim(readAnim(in));
        }
        weaponMod.setLastProjectileId(readInt(in));

        int pendingProjectilesCount = readInt(in);
        List<ProjectileState> pendingProjectiles = new ArrayList<ProjectileState>();
        for (int i = 0; i < pendingProjectilesCount; i++) {
            pendingProjectiles.add(readProjectile(in, fileFormat));
        }
        weaponMod.setPendingProjectiles(pendingProjectiles);

        return weaponMod;
    }

    public void writeWeaponModule(OutputStream out, WeaponModuleState weaponMod, int fileFormat) throws IOException {
        if (weaponMod == null) {
            weaponMod = new WeaponModuleState();
        }
        writeInt(out, weaponMod.getCooldownTicks());
        writeInt(out, weaponMod.getCooldownTicksGoal());
        writeInt(out, weaponMod.getSubcooldownTicks());
        writeInt(out, weaponMod.getSubcooldownTicksGoal());
        writeInt(out, weaponMod.getBoost());
        writeInt(out, weaponMod.getCharge());

        writeInt(out, weaponMod.getCurrentTargets().size());
        for (XYPair target : weaponMod.getCurrentTargets()) {
            writeReticleCoordinate(out, target);
        }

        writeInt(out, weaponMod.getPreviousTargets().size());
        for (XYPair target : weaponMod.getPreviousTargets()) {
            writeReticleCoordinate(out, target);
        }

        writeBool(out, weaponMod.getAutofire());
        writeBool(out, weaponMod.getFireWhenReady());
        writeInt(out, weaponMod.getTargetId());

        writeAnim(out, weaponMod.getWeaponAnim());

        writeInt(out, weaponMod.getProtractAnimTicks());
        writeBool(out, weaponMod.isFiring());
        writeBool(out, weaponMod.getUnknownPhi());

        if (fileFormat == 9 || fileFormat == 11) {
            writeInt(out, weaponMod.getAnimCharge());

            writeAnim(out, weaponMod.getChargeAnim());
        }

        writeInt(out, weaponMod.getLastProjectileId());

        writeInt(out, weaponMod.getPendingProjectiles().size());
        for (ProjectileState projectile : weaponMod.getPendingProjectiles()) {
            writeProjectile(out, projectile, fileFormat);
        }
    }

    private XYPair readReticleCoordinate(FileInputStream in) throws IOException {
        int reticleX = readInt(in);
        int reticleY = readInt(in);

        return new XYPair(reticleX, reticleY);
    }

    public void writeReticleCoordinate(OutputStream out, XYPair reticle) throws IOException {
        writeInt(out, reticle.x);
        writeInt(out, reticle.y);
    }
}
