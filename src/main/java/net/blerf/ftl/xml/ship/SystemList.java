package net.blerf.ftl.xml.ship;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.type.SystemType;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SystemList {

    @XmlElement(name = "pilot")
    private SystemRoom pilotRoom;
    @XmlElement(name = "doors")
    private SystemRoom doorsRoom;
    @XmlElement(name = "sensors")
    private SystemRoom sensorsRoom;
    @XmlElement(name = "medbay")
    private SystemRoom medicalRoom;
    @XmlElement(name = "oxygen")
    private SystemRoom lifeSupportRoom;
    @XmlElement(name = "shields")
    private SystemRoom shieldRoom;
    @XmlElement(name = "engines")
    private SystemRoom engineRoom;
    @XmlElement(name = "weapons")
    private SystemRoom weaponRoom;
    @XmlElement(name = "drones")
    private SystemRoom droneRoom;
    @XmlElement(name = "teleporter")
    private SystemRoom teleporterRoom;
    @XmlElement(name = "cloaking")
    private SystemRoom cloakRoom;  // lol :)
    @XmlElement(name = "artillery")
    private List<SystemRoom> artilleryRooms;
    @XmlElement(name = "clonebay")
    private SystemRoom cloneRoom;
    @XmlElement(name = "hacking")
    private SystemRoom hackRoom;
    @XmlElement(name = "mind")
    private SystemRoom mindRoom;
    @XmlElement(name = "battery")
    private SystemRoom batteryRoom;


    public SystemRoom[] getSystemRooms() {
        SystemRoom[] rooms = new SystemRoom[]{
                pilotRoom, doorsRoom, sensorsRoom, medicalRoom, lifeSupportRoom, shieldRoom,
                engineRoom, weaponRoom, droneRoom, teleporterRoom, cloakRoom
        };

        List<SystemRoom> list = new ArrayList<SystemRoom>();
        for (SystemRoom room : rooms) {
            if (room != null) list.add(room);
        }
        if (artilleryRooms != null) {
            list.addAll(artilleryRooms);
        }
        return list.toArray(new SystemRoom[list.size()]);
    }

    /**
     * Returns SystemRooms, or null if not present.
     *
     * @return an array of SystemRooms, usually only containing one
     */
    public SystemRoom[] getSystemRoom(SystemType systemType) {
        SystemRoom systemRoom = null;
        if (SystemType.PILOT.equals(systemType)) systemRoom = getPilotRoom();
        else if (SystemType.DOORS.equals(systemType)) systemRoom = getDoorsRoom();
        else if (SystemType.SENSORS.equals(systemType)) systemRoom = getSensorsRoom();
        else if (SystemType.MEDBAY.equals(systemType)) systemRoom = getMedicalRoom();
        else if (SystemType.OXYGEN.equals(systemType)) systemRoom = getLifeSupportRoom();
        else if (SystemType.SHIELDS.equals(systemType)) systemRoom = getShieldRoom();
        else if (SystemType.ENGINES.equals(systemType)) systemRoom = getEngineRoom();
        else if (SystemType.WEAPONS.equals(systemType)) systemRoom = getWeaponRoom();
        else if (SystemType.DRONE_CTRL.equals(systemType)) systemRoom = getDroneRoom();
        else if (SystemType.TELEPORTER.equals(systemType)) systemRoom = getTeleporterRoom();
        else if (SystemType.CLOAKING.equals(systemType)) systemRoom = getCloakRoom();
        else if (SystemType.BATTERY.equals(systemType)) systemRoom = getBatteryRoom();
        else if (SystemType.CLONEBAY.equals(systemType)) systemRoom = getCloneRoom();
        else if (SystemType.MIND.equals(systemType)) systemRoom = getMindRoom();
        else if (SystemType.HACKING.equals(systemType)) systemRoom = getHackRoom();

        if (systemRoom != null) return new SystemRoom[]{systemRoom};

        if (SystemType.ARTILLERY.equals(systemType)) {
            if (getArtilleryRooms() != null && getArtilleryRooms().size() > 0) {
                int n = 0;
                SystemRoom[] result = new SystemRoom[getArtilleryRooms().size()];
                for (SystemRoom artilleryRoom : artilleryRooms) {
                    result[n++] = artilleryRoom;
                }
                return result;
            }
        }

        return null;
    }

    /**
     * Returns the SystemType in a given room, or null.
     * <p>
     * TODO: Make this return multiple SystemTypes (ex: medbay/clonebay).
     */
    public SystemType getSystemTypeByRoomId(int roomId) {
        for (SystemType systemType : SystemType.values()) {
            SystemRoom[] systemRooms = getSystemRoom(systemType);
            if (systemRooms != null) {
                for (SystemRoom systemRoom : systemRooms) {
                    if (systemRoom.getRoomId() == roomId)
                        return systemType;
                }
            }
        }
        return null;
    }

    /**
     * Returns roomId(s) that contain a given system, or null.
     *
     * @return an array of roomIds, usually only containing one
     */
    public int[] getRoomIdBySystemType(SystemType systemType) {
        int[] result = null;
        SystemRoom[] systemRooms = getSystemRoom(systemType);
        if (systemRooms != null) {
            result = new int[systemRooms.length];
            for (int i = 0; i < systemRooms.length; i++) {
                result[i] = systemRooms[i].getRoomId();
            }
        }
        return result;
    }
}
